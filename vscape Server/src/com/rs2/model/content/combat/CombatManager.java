package com.rs2.model.content.combat;

import com.rs2.Constants;
import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.Following;
import com.rs2.model.content.combat.attacks.WeaponAttack;
import com.rs2.model.content.combat.hit.Hit;
import com.rs2.model.content.combat.hit.HitDef;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.combat.weapon.AttackStyle;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.minigames.warriorsguild.WarriorsGuild;
import com.rs2.model.content.minigames.castlewars.Castlewars;
import com.rs2.model.content.minigames.fightcaves.FightCaves;
import com.rs2.model.content.minigames.pestcontrol.PestControl;
import com.rs2.model.content.quests.impl.AnimalMagnetism;
import com.rs2.model.content.quests.impl.DemonSlayer;
import com.rs2.model.content.quests.impl.DragonSlayer;
import com.rs2.model.content.quests.impl.ErnestTheChicken;
import com.rs2.model.content.quests.impl.FamilyCrest;
import com.rs2.model.content.quests.impl.GhostsAhoy.GhostsAhoy;
import com.rs2.model.content.quests.impl.HeroesQuest;
import com.rs2.model.content.quests.impl.HorrorFromTheDeep;
import com.rs2.model.content.quests.impl.PriestInPeril;

import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.quests.impl.Quest;
import com.rs2.model.content.quests.impl.ShieldOfArrav;
import com.rs2.model.content.quests.impl.VampireSlayer;
import com.rs2.model.content.randomevents.TalkToEvent;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.magic.Teleportation;
import com.rs2.model.content.skills.prayer.Prayer;
import com.rs2.model.content.skills.prayer.Prayer.PrayerData;
import com.rs2.model.content.treasuretrails.ClueScroll;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;
import com.rs2.util.PlayerSave;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * TODO: on death remove all victims hits from hitsStory log out during death
 * Spell delays for ancients http://www.vscape.com/services
 * /forums/thread.ws?3,4,13136,38613,goto,3
 *
 *
 */
public class CombatManager extends Tick {

	private static CombatManager combatManager;

	public static final String NO_AMMO_MESSAGE = "You have no ammo left!";
	public static final String WARRIORS_GUILD = "You can only use melee in the Warriors' Guild!";
	public static final String WRONG_AMMO_MESSAGE = "You can not use that kind of ammo!";
	public static final String AMMO_COMPATIBILITY_MESSAGE = "You cannot use that ammo with that weapon!";
	public static final String NO_SPECIAL_ENERGY_MESSAGE = "You have no special energy left.";

	private final List<Hit> hitsStory;

	public static boolean deathByPortal = false;
	public static boolean kolodionDeath = false;

	public CombatManager() {
		super(1);
		hitsStory = new LinkedList<Hit>();
		World.getTickManager().submit(this);
	}

	public static void attack(Entity attacker, Entity victim) {
		if (!canAttack(attacker, victim)) {
			return;
		}

		if (attacker.isPlayer() && victim.isNpc()) {
			Npc npc = (Npc) victim;
			Player player = (Player) attacker;
			if (npc.getNpcId() >= 6026 && npc.getNpcId() < 6046 && player.getEquipment().getId(Constants.WEAPON) != PriestInPeril.WOLFBANE) {
				npc.sendTransform(npc.getNpcId() - 20, 500);
			}
		}
		List<AttackUsableResponse> attacks = new LinkedList<AttackUsableResponse>();
		int distance = Misc.getDistance(attacker.getPosition(), victim.getPosition());
		attacker.fillUsableAttacks(attacks, victim, distance);
		AttackUsableResponse foundResponse = null;
		for (AttackUsableResponse response : attacks) {
			if (response.getType() != AttackUsableResponse.Type.FAIL) {
				if (foundResponse == null || response.getScript().distanceRequired() > foundResponse.getScript().distanceRequired()) {
					foundResponse = response;
				}
			}
		}
		distance = foundResponse == null ? 1 : foundResponse.getScript().distanceRequired();
		attacker.setFollowDistance(distance);
		CombatCycleEvent.startCombat(attacker, victim);
		if (attacker.isPlayer() && ((Player) attacker).getNewComersSide().getTutorialIslandStage() == 45) {
			((Player) attacker).getDialogue().sendTutorialIslandWaitingInfo("While you are fighting you will see a bar over your head. The", "bar shows how much health you have left. Your opponent will", "have one too. You will continue to attack the rat until it's dead", "or you do something else.", "Sit back and watch.");
		}
	}

	@Override
	public void execute() {
		try {
			// execute current hits
			List<Hit> hitList = new LinkedList<Hit>();
			hitList.addAll(hitsStory);
			hitsStory.clear();
			List<Hit> recoilHits = new LinkedList<Hit>();

			for (Hit hit : hitList) {
				hit.tick();
				if (hit.shouldExecute()) {
					if (!hit.getVictim().isDead()) {
						hit.execute(recoilHits);
					}
					if (hit.getVictim().getCurrentHp() <= 0 && !hit.getVictim().isDead()) {
						hit.getVictim().setDead(true);
						startDeath(hit.getVictim());
						if (hit.getVictim().isPlayer() && hit.getVictim().inDuelArena()) {
							((Player) hit.getVictim()).getDuelMainData().getOpponent().getAttributes().put("canTakeDamage", false);
							return;
						}
					}
				} else if (!hit.getVictim().isDead()) {
					hitsStory.add(hit);
				}
			}
			for (Hit hit : recoilHits) {
				hit.execute(null);
			}
			recoilHits.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void startDeath(final Entity died) {
		if (died.isDoorSupport()) {
			((Npc) died).sendTransform(((Npc) died).getNpcId() + 1, 10);
			new GameObject(Constants.EMPTY_OBJECT, died.getPosition().getX(), died.getPosition().getY(), died.getPosition().getZ(), 0, 10, 3, 35);
			for (Player player : World.getPlayers()) {
				if (player == null) {
					continue;
				} else if (player != null && player.getPosition().getY() - 1 == died.getPosition().getY()) {
					player.walkTo(new Position(2545, 10144, 0), true);
				} else if (player != null && player.getPosition().getY() + 1 == died.getPosition().getY()) {
					player.walkTo(new Position(2545, 10142, 0), true);
				} else if (player != null && player.getPosition().getX() + 1 == died.getPosition().getX()) {
					player.walkTo(new Position(2544, 10143, 0), true);
				}
			}

		}
		final Entity killer = died.findKiller();// == null ? possibleKiller : died.findKiller();
		died.getTask();
		died.setDeathPosition(died.getPosition().clone());
		if (killer != null && killer.isPlayer()) {
			Player player = (Player) killer;
			player.getPjTimer().setWaitDuration(0);
			player.getPjTimer().reset();
			if (player.getNewComersSide().getTutorialIslandStage() == 45 || player.getNewComersSide().getTutorialIslandStage() == 47) {
				player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);
			}
		}
		if (died.isPlayer()) {
			Player player = (Player) died;
			player.setStopPacket(true);
		}
		if (died.isNpc() && killer != null && killer.isPlayer() && ((Npc) died).getNpcId() == 757 && VampireSlayer.handleCountDeath((Player) killer, (Npc) died)) {
			died.setDead(false);
			return;
		}
		if (died.isNpc() && killer != null && killer.isPlayer() && ((Npc) died).getNpcId() == FamilyCrest.CHRONOZON && FamilyCrest.handleChronozonDeath((Player) killer, (Npc) died)) {
			died.setDead(false);
			return;
		}
		if (died.isNpc() && killer != null && killer.isPlayer() && ((Npc) died).getIndex() == ((Player) killer).getHintIndex()) {
			((Player) killer).getActionSender().createPlayerHints(1, Npc.getNpcById(0).getIndex());
			((Player) killer).getActionSender().createPlayerHints(10, Npc.getNpcById(0).getIndex());
			((Player) killer).setHintIndex(-1);
		}
		if (died.isNpc() && killer != null && killer.isPlayer() && ((Npc) died) == ((Player) killer).getBarbarianSpirits().getSpiritSummoned()) {
			((Player) killer).getBarbarianSpirits().setSpiritSummoned(null);
		}
		if (died != null && died.isPlayer() && ((Player) died).inMageArena() && ((Player) died).getMageArenaStage() == 1) {
			Player player = (Player) died;
			Position pos = player.getPosition().clone();
			for (Npc npc : World.getNpcs()) {
				if (npc == null) {
					continue;
				}
				if (npc.getDefinition().getName().equalsIgnoreCase("kolodion")
					&& Misc.getDistance(pos, npc.getPosition().clone()) <= 8) {
					player.teleport(new Position(2541, 4714, 0));
					player.setStopPacket(false);
					player.setHideWeapons(false);
					player.setDead(false);
					player.heal(100);
					player.resetEffects();
					player.getSkill().refresh();
					died.getInCombatTick().setWaitDuration(0);
					died.getInCombatTick().reset();
					died.getPjTimer().setWaitDuration(0);
					died.getPjTimer().reset();
					Dialogues.setNextDialogue(player, 905, 1);
					player.getDialogue().sendNpcChat("Better luck next time!", Dialogues.LAUGHING);
					return;
				}
			}
		}
		final int deathAnimation = died.getDeathAnimation();
		if (deathAnimation != -1) {
			Tick tick = new Tick(2) {
				@Override
				public void execute() {
					int death = deathAnimation;
					if (died.isPlayer()) {
						Player player = (Player) died;
						player.setHideWeapons(true);
						player.setAppearanceUpdateRequired(true);
						if (player.transformNpc > 0) {
							death = new Npc(player.transformNpc).getDefinition().getDeathAnim();
						}
					}
					if (killer != null && killer.isPlayer() && died.isNpc()) {
						Player player = (Player) killer;
						Npc npc = (Npc) died;
						if (npc.getPosition().isViewableFrom(player.getPosition())) {
							player.getCombatSounds().npcDeathSound(((Npc) died));
						}
					}
					if (died.isNpc() && PestControl.isSplatter((Npc) died)) {
						died.getUpdateFlags().sendAnimation(3888, 75);
						died.getUpdateFlags().sendGraphic(287, 100);
						stop();
					} else if (died.isNpc() && PestControl.isSpinner((Npc) died) && deathByPortal) {
						died.getUpdateFlags().sendAnimation(3889);
						died.getUpdateFlags().sendGraphic(1207);
						deathByPortal = false;
						stop();
					} else if (died.isNpc() && PestControl.isSpinner((Npc) died) && !deathByPortal) {
						died.getUpdateFlags().sendAnimation(3910);
						stop();
					} else if (died.isNpc() && ((Npc) died).getNpcId() == 916) { // joe
						died.getUpdateFlags().sendAnimation(836);
						stop();
					} else if (died.isNpc() && ((Npc) died).getNpcId() == 919) { // keli
						died.getUpdateFlags().sendAnimation(424);
						stop();
					} else if (died.isNpc() && ((Npc) died).getNpcId() == 920) { // prince ali
						died.getUpdateFlags().sendAnimation(804);
						stop();
					} else if (died.isNpc() && ((Npc) died).getNpcId() == 247 && killer.isPlayer() && ((Player) killer).getQuestStage(11) == 5) { // mordred
						died.getUpdateFlags().sendAnimation(1331);
						stop();
					} else if (died.isNpc() && ((Npc) died).getNpcId() == 643) { //weaponsmaster phoenix gang
						died.getUpdateFlags().sendForceMessage("Damn crossbows...");
						died.getUpdateFlags().sendAnimation(death);
						stop();
					} else if (died.isNpc() && ((Npc) died).getNpcId() == HorrorFromTheDeep.CHILD_DAGANNOTH && killer != null && killer.isPlayer()) {
						((Player) killer).setQuestStage(26, 5);
						Dialogues.startDialogue((Player) killer, HorrorFromTheDeep.SITTING_JOSSIK);
						died.getUpdateFlags().sendAnimation(death);
						stop();
					} else if (died.isNpc() && ((Npc) died).getNpcId() >= 1351 && ((Npc) died).getNpcId() < 1357 && killer != null && killer.isPlayer()) {
						((Player) killer).setQuestStage(26, 6);
						GroundItem drop = new GroundItem(new Item(6729), ((Player) killer), died.getPosition().clone());
						GroundItemManager.getManager().dropItem(drop);
						((Player) killer).getInventory().addItemOrDrop(new Item(HorrorFromTheDeep.RUSTY_CASKET));
						Dialogues.startDialogue((Player) killer, HorrorFromTheDeep.SITTING_JOSSIK);
						died.getUpdateFlags().sendAnimation(death);
						stop();
					} else if (died.isNpc() && ((Npc) died).getDefinition().getName().toLowerCase().equals("evil chicken") && killer != null && killer.isPlayer()) {
						Player player = (Player) killer;
						double reward = new Random().nextDouble() * 750;
						player.getActionSender().sendMessage("You pluck feathers from the Evil Chicken's corpse.");
						player.getInventory().addItemOrDrop(new Item(314, (int) reward > 250 ? (int) reward : 250));
						died.getUpdateFlags().sendAnimation(death);
						stop();
					} else {
						died.getUpdateFlags().sendAnimation(death);
						stop();
					}
				}
			};
			World.getTickManager().submit(tick);
		}
		if (died.isNpc() && ((Npc) died).getNpcId() == 247 && killer.isPlayer() && ((Player) killer).getQuestStage(11) == 5) {
			Tick deathTimer = new Tick(10) {
				@Override
				public void execute() {
					died.getUpdateFlags().sendAnimation(1332);
					this.stop();
				}
			};
			World.getTickManager().submit(deathTimer);
			Npc npc = new Npc(248);
			npc.setPosition(new Position(2766, 3401, 2));
			npc.setSpawnPosition(died.getPosition().clone());
			World.register(npc);
			npc.getUpdateFlags().sendGraphic(86);
			npc.setFollowingEntity(killer);
			Dialogues.startDialogue((Player) killer, 248);
			died.heal(5);
			died.removeAllEffects();
			return;
		}
		if (died.isNpc() && ((Npc) died).getNpcId() == 1158) {
			Tick deathTimer = new Tick(5) {
				@Override
				public void execute() {
					endDeath(died, killer, true);
					this.stop();
				}
			};
			World.getTickManager().submit(deathTimer);
			died.getUpdateFlags().sendAnimation(6242);
			died.setDead(true);
			died.getTask();
			died.removeAllEffects();
			died.getInCombatTick().setWaitDuration(0);
			died.getInCombatTick().reset();
			died.getPjTimer().setWaitDuration(0);
			died.getPjTimer().reset();
		} else {
			Tick deathTimer = new Tick(died.getDeathAnimationLength()) {
				@Override
				public void execute() {
					endDeath(died, killer, true);
					this.stop();
				}
			};
			World.getTickManager().submit(deathTimer);
			died.getTask();
			died.removeAllEffects();
			died.getInCombatTick().setWaitDuration(0);
			died.getInCombatTick().reset();
			died.getPjTimer().setWaitDuration(0);
			died.getPjTimer().reset();
		}
		if (killer != null && died.isPlayer() && ((Player) died).getIsUsingPrayer()[PrayerData.RETRIBUTION.getIndex()]) {
			Prayer.applyRetribution(died, killer);
		} else if (died.isNpc() && ((Npc) died).getNpcId() == 757 && killer != null && killer.isPlayer()) { //count draynor
			Player player = (Player) killer;
			player.getActionSender().sendMessage("You drive the stake deep into the vampire's heart.");
			player.getInventory().removeItem(new Item(VampireSlayer.STAKE));
			if (player.getQuestStage(21) == 2) {
				player.setQuestStage(21, 3);
				QuestHandler.completeQuest(player, 21);
			}
		}
	}

	public static void endDeath(final Entity died, final Entity killer, final boolean firstTime) {
		if (firstTime) {
			died.dropItems(killer);
			died.setDeathPosition(null);
			if (killer != null && killer.isPlayer() && died.isNpc()) {
				final Npc npc = (Npc) died;
				ClueScroll.handleAttackerDeath((Player) killer, npc);
				((Player) killer).getSlayer().handleNpcDeath(npc);
				((Player) killer).getBarrows().handleDeath(npc);
				WarriorsGuild.dropDefender((Player) killer, npc);
				ShieldOfArrav.handleDrops((Player) killer, npc);
				DragonSlayer.handleDrops((Player) killer, npc);
				PriestInPeril.handleDrops((Player) killer, npc);
				HeroesQuest.handleGripDeath((Player) killer, npc);
				FamilyCrest.handleDrops((Player) killer, npc);
				((Player) killer).getRandomHandler().getFreakyForester().handleDrops(npc);
				for (Quest q : QuestHandler.getQuests()) {
					q.handleDeath((Player) killer, npc);
				}
				if (((Player) killer).getSpawnedNpc() != null) {
					((Player) killer).setSpawnedNpc(null);
				}
			}
		}
		if (died != null && died.isNpc()) {
			final Npc npc = (Npc) died;
			handleNpcPreDeath(killer, npc, firstTime);
			handleNpcRespawn(killer, npc);
		}
		died.getUpdateFlags().faceEntity(-1);
		died.setDead(false);
		died.setCurrentHp(died.getMaxHp());
		died.getUpdateFlags().sendAnimation(65535, 0);
		died.removeAllEffects();
		if (killer != null && killer.isPlayer() && died.isPlayer()) {
			Player attacker = (Player) killer;
			Player victim = (Player) died;
			attacker.getActionSender().sendMessage("You have defeated " + Misc.formatPlayerName(victim.getUsername()) + ".");
		}
		died.getHitRecordQueue().clear();
		if (died.isPlayer()) {
			handlePlayerDeath(killer, (Player) died);
		} else if (died.isNpc()) {
			handleNpcDeath(killer, (Npc) died);
		}
	}

	public static void init() {
		combatManager = new CombatManager();
	}

	public static CombatManager getManager() {
		return combatManager;
	}

	public void submit(Hit hit) {
		hitsStory.add(hit);
	}

	/**
	 * Resets anything needed after the end of combat.
	 */
	public static void resetCombat(Entity entity) {
		if (entity == null) {
			return;
		}
		if (entity.isPlayer()) {
			((Player) entity).setCastedSpell(null);
		}
		entity.setCombatingEntity(null);
		entity.setInstigatingAttack(false);
		entity.setSkilling(null);
		entity.getUpdateFlags().faceEntity(-1);
		if (entity.isNpc() && ((Npc) entity).getNpcId() == 1460) {
			((Npc) entity).setFollowingEntity(null);
			((Npc) entity).walkTo(((Npc) entity).getSpawnPosition(), true);
		}
		Following.resetFollow(entity);
	}

	public static boolean arenaNpc(Npc npc) {
		return npc.getNpcId() >= 912 && npc.getNpcId() <= 914;
	}

	public static boolean canAttack(Entity attacker, Entity victim) {
		if (attacker == null || victim == null) {
			return false;
		}
		boolean npcIsAttacker = attacker.isNpc(), npcIsVictim = victim.isNpc();
		boolean playerIsAttacker = attacker.isPlayer(), playerIsVictim = victim.isPlayer();
		Player player = playerIsAttacker ? ((Player) attacker) : playerIsVictim ? ((Player) victim) : null;
		Npc npc = npcIsAttacker ? ((Npc) attacker) : npcIsVictim ? ((Npc) victim) : null;
		try {
			if (Constants.DDOS_PROTECT_MODE) {
				if (playerIsAttacker) {
					player.getActionSender().sendMessage("@red@You can't attack during DDOS protection mode.", true);
				}
				CombatManager.resetCombat(attacker);
				return false;
			}
			if (attacker.failedCriticalRequirement()) {
				attacker.setFailedCriticalRequirement(false);
				CombatManager.resetCombat(attacker);
				attacker.getMovementHandler().reset();
				return false;
			}
			if (victim.getMaxHp() < 1 || (npcIsVictim && unattackableNpc(npc))) {
				if (playerIsAttacker) {
					player.getActionSender().sendMessage("You cannot attack this npc.");
				}
				CombatManager.resetCombat(attacker);
				return false;
			}
			if (npcIsAttacker) {
				if (npc.isDontAttack()) {
					return false;
				}
			}
			if (npcIsVictim) {
				if (!npc.isVisible() || npc.isDead()) {
					return false;
				} else if (npc.walkingBackToSpawn) {
					CombatManager.resetCombat(victim);
				}
			}
			if (playerIsAttacker) {
				boolean stop = true;
				if (player.getPets().getPet() != null && player.getPets().getPet() == victim) {
					player.getActionSender().sendMessage("You cannot attack your own pet!");
				} else if (player.transformNpc == 1707 || player.transformNpc == 1708) {
					player.getDialogue().sendPlayerChat("I can't see to attack!", Dialogues.DISTRESSED);
				} else if (player.getMMVars().isMonkey()) {
					player.getActionSender().sendMessage("You cannot attack as a monkey!");
				} else if (player.inDuelArena() && !player.getDuelMainData().canStartDuel()) {

				} else {
					stop = false;
				}
				if (stop) {
					CombatManager.resetCombat(player);
					return false;
				}
			}
			if (playerIsAttacker && npcIsVictim) {
				if (npc.getDefinition().getName().toLowerCase().equals("pheasant")) {
					if (player.getRandomHandler().getCurrentEvent() == null || (player.getRandomHandler().getCurrentEvent() == player.getRandomHandler().getFreakyForester() && player.getRandomHandler().getFreakyForester().complete)) {
						player.getDialogue().sendPlayerChat("I shouldn't attack these poor birds like that.", Dialogues.SAD);
						return false;
					}
				}
				switch (npc.getNpcId()) {
					case 879:
						if (player.getEquipment().getId(Constants.WEAPON) != 2402) {
							player.getActionSender().sendMessage("You need to equip Silverlight to fight Delrith!", true);
							CombatManager.resetCombat(attacker);
							return false;
						}
						break;
					case 742:
						if (player.getQuestStage(15) != 7) {
							player.getActionSender().sendMessage("I better not try this.");
							CombatManager.resetCombat(player);
							return false;
						}
						break;
					case 272:
						if (player.getEquipment().getId(Constants.AMULET) != 87 && player.getQuestStage(47) > 0) {
							player.getActionSender().sendMessage("You get an uneasy feeling as you approach.");
							return false;
						} else if (player.getEquipment().getId(Constants.AMULET) == 87 && player.getQuestStage(47) < 7) {
							player.getDialogue().sendPlayerChat("I shouldn't attack Lucien without making a decision", "on which party to help first.");
							return false;
						} else if (player.getQuestVars().getSidedWithLucien()) {
							player.getDialogue().sendPlayerChat("If I kill him now I'll never get my reward!");
							return false;
						} else if (player.getQuestStage(47) >= 8) {
							player.getActionSender().sendMessage("You cannot attack this npc.");
							return false;
						}
						break;
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR CHECKING ATTACK ELIGIBILITY");
			e.printStackTrace();
		}
		return true;
	}

	public static boolean unattackableNpc(Npc npc) {
		final int id = npc.getNpcId();
		if (TalkToEvent.isTalkToNpc(id) || PestControl.isShieldedPortal(npc)) {
			return true;
		}
		switch (id) {
			case 411: //Swarm
			case 3782: //Void Knight
			case AnimalMagnetism.UNDEAD_TREE:
				return true;
		}
		return false;
	}

	public static int determineRespawnTimer(Npc npc) {
		switch (npc.getNpcId()) {
			case 1158: //KQ
				return 300;
			case 224: //Dungeont rat
				return 30;
			case 2881:
			case 2882:
			case 2883: //Dagannoth Kings
			case ErnestTheChicken.ERNEST_CHICKEN:
				return 60;
			case 916: //Joe the Draynor prison guard
			case 920: //Prince Ali
				return 120;
		}
		return npc.getRespawnTimer();
	}

	public static void handlePlayerDeath(Entity killer, Player player) {
		player.setStopPacket(false);
		player.setHideWeapons(false);
		player.setAutoSpell(null);
		player.resetEffects();
		player.getSkill().refresh();
		try {
			if (player.inDuelArena()) {
				player.getDuelMainData().handleDeath(false);
				return;
			}
			if (player.getCreatureGraveyard().isInCreatureGraveyard()) {
				player.getCreatureGraveyard().handleDeath();
				return;
			}
			if (player.inPestControlGameArea()) {
				PestControl.handleDeath(player);
				return;
			}
			if (player.onPestControlIsland()) {
				player.teleport(new Position(2657, 2639, 0));
				return;
			}
			if (Castlewars.handleDeath(player)) {
				return;
			}
			if (player.inFightCaves()) {
				FightCaves.exitCave(player);
				return;
			}
			if (player.getMultiCannon().hasCannon()) {
				player.getMultiCannon().pickupCannon(false);
			}
			if (player.getInJail()) {
				player.setInJail(false);
			}
		} catch (Exception e) {
			System.out.println("ERROR DOING PLAYER DEATH CHECKS");
			e.printStackTrace();
		}
		player.removeAllEffects();
		player.teleport(player.getQuestVars().isGazeOfSaradomin() ? Teleportation.WHITE_KNIGHTS_CASTLE : Teleportation.HOME);
		player.getActionSender().sendMessage("Oh dear, you are dead!");
		PlayerSave.save(player);
		player.getActionSender().sendQuickSong(75, 16);
	}

	public static void handleNpcPreDeath(Entity killer, Npc npc, boolean firstTime) {
		try {
			if (npc.getNpcId() == 1158 && firstTime) { // kq
				if (World.npcAmount(1160) == 0) {
					Npc newQueen = new Npc(1160);
					newQueen.setSpawnPosition(npc.getPosition().clone());
					newQueen.setPosition(npc.getPosition().clone());
					newQueen.setCombatDelay(10);
					newQueen.getMovementPaused().setWaitDuration(10);
					World.register(newQueen);
					newQueen.getUpdateFlags().sendForceMessage("Bzzzzz");
				}
			} else if (npc.getNpcId() == 1160) {
				npc.setDead(true);
				npc.setVisible(false);
				World.unregister(npc);
				return;
			} else if (npc.getNpcId() >= 6026 && npc.getNpcId() <= 6045) {
				npc.setTransformUpdate(true);
			} else if (npc.getNpcId() == 742 && killer.isPlayer()) {
				if (((Player) killer).getQuestStage(15) == 7) {
					((Player) killer).setQuestStage(15, 8);
					((Player) killer).getDialogue().sendStatement("You sever Elvarg's head as proof for Oziach.");
					if (!((Player) killer).getInventory().ownsItem(11279)) {
						((Player) killer).getInventory().addItem(new Item(11279));
					}
				}
			} else if (npc.getNpcId() == GhostsAhoy.GIANT_LOBSTER) {
				if (killer != null && killer.isPlayer()) {
					((Player) killer).getQuestVars().setLobsterSpawnedAndDead(true);
				}
			} else if (npc.getNpcId() == 879 && firstTime) { // delrith
				Npc delrith = new Npc(880);
				delrith.setSpawnPosition(npc.getPosition().clone());
				delrith.setPosition(npc.getPosition().clone());
				delrith.setCombatDelay(10);
				delrith.getMovementPaused().setWaitDuration(10);
				World.register(delrith);
				if (killer != null && killer.isPlayer()) {
					delrith.setPlayerOwner(((Player) killer).getIndex());
				}
				Dialogues.startDialogue((Player) killer, 10666);
			} else if (npc.getDefinition().getName().equalsIgnoreCase("kolodion")) {
				if (npc.getNpcId() == 911 && killer.isPlayer()) {
					Player player = (Player) killer;
					player.getActionSender().sendMessage("You have defeated Kolodion!");
					player.setMageArenaStage(2);
					player.teleport(new Position(2540, 4714, 0));
					player.resetEffects();
					player.removeAllEffects();
					player.heal(100);
					player.getPrayer().resetAll();
					player.getSkill().refresh();
					player.getDialogue().sendNpcChat("You've done well, step into the pool.", "Beyond the pool is your prize.", Dialogues.CALM);
					PlayerSave.save((Player) killer);
				} else {
					Npc newKol = new Npc(npc.getNpcId() + 1);
					newKol.setSpawnPosition(npc.getPosition().clone());
					newKol.setPosition(npc.getPosition().clone());
					newKol.setNeedsRespawn(false);
					newKol.setCombatDelay(2);
					newKol.setPlayerOwner(((Player) killer).getIndex());
					World.register(newKol);
					attack(newKol, killer);
					if (newKol.getNpcId() == 911) {
						newKol.getUpdateFlags().sendForceMessage("Aaargh! I cannot lose!");
					} else {
						newKol.getUpdateFlags().sendForceMessage("How about this?");
					}
				}
			} else if (WarriorsGuild.isAnimatedArmor(npc) && killer.isPlayer()) {
				((Player) killer).getActionSender().sendMessage("The ref awards you with some tokens.");
				((Player) killer).getInventory().addItem(new Item(8851, WarriorsGuild.getTokenAmount(npc)));
			} else if (npc.getNpcId() == 238 && killer.isPlayer() && ((Player) killer).getQuestStage(11) == 10) {
				((Player) killer).setQuestStage(11, 11);
				((Player) killer).getDialogue().sendStatement("With the spirit dead, you can now smash Merlin's crystal.");
			}
			if (killer != null && killer.isPlayer()) {
				FightCaves.handleDeath((Player) killer, npc);
			}
		} catch (Exception e) {
			System.out.println("ERROR DOING NPC PRE-DEATH CHECKS");
			e.printStackTrace();
		}
	}

	public static void handleNpcRespawn(final Entity killer, final Npc npc) {
		if (!npc.needsRespawn()) {
			npc.setVisible(false);
			World.unregister(npc);
		} else {
			if (npc.isVisible()) {
				npc.setVisible(false);
				npc.setPosition(npc.getSpawnPosition().clone());
				npc.getMovementHandler().reset();
				npc.sendTransform(npc.getOriginalNpcId(), 0);
				CombatManager.resetCombat(npc);
				int respawnTimer = determineRespawnTimer(npc);
				// Set respawn
				CycleEventHandler.getInstance().addEvent(npc, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						endDeath(npc, killer, false);
						container.stop();
					}

					@Override
					public void stop() {
					}
				}, respawnTimer);
				return;
			} else {
				npc.setVisible(true);
			}
		}
	}

	public static void handleNpcDeath(Entity killer, Npc npc) {
		try {
			if (npc.getNpcId() == 655) {
				if (killer != null && killer.isPlayer()) {
					Player player = (Player) killer;
					player.setKilledTreeSpirit(true);
					player.getDialogue().sendStatement("With the Tree Spirit defeated you can now chop the tree.");
				}
			}
			if (npc.inPestControlGameArea()) {
				PestControl.handleNpcDeath(npc);
			}
		} catch (Exception e) {
			System.out.println("ERROR DOING NPC DEATH CHECKS");
			e.printStackTrace();
		}
	}

}
