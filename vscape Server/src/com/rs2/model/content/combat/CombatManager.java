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
import com.rs2.model.content.quests.AnimalMagnetism;
import com.rs2.model.content.quests.DemonSlayer;
import com.rs2.model.content.quests.DragonSlayer;
import com.rs2.model.content.quests.ErnestTheChicken;
import com.rs2.model.content.quests.FamilyCrest;
import com.rs2.model.content.quests.GhostsAhoy;
import com.rs2.model.content.quests.HeroesQuest;
import com.rs2.model.content.quests.HorrorFromTheDeep;
import com.rs2.model.content.quests.PriestInPeril;
import com.rs2.model.content.quests.Quest;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.quests.ShieldOfArrav;
import com.rs2.model.content.quests.VampireSlayer;
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
 *  TODO: on death remove all victims hits from hitsStory
 *         log out during death Spell delays for ancients
 *         http://www.vscape.com/services
 *         /forums/thread.ws?3,4,13136,38613,goto,3
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
		if(Constants.DDOS_PROTECT_MODE) {
			if(attacker.isPlayer())
			{
        		((Player) attacker).getActionSender().sendMessage("@red@You can't attack during DDOS PROTECTION.", true);
	        	CombatManager.resetCombat(attacker);
				return;
			}
	        if(attacker.isNpc() && victim.isPlayer()) {
	        	CombatManager.resetCombat(attacker);
				return;
	        }
		}
		if(attacker.isNpc() && ((Npc)attacker).isDontAttack()) {
			return;
		}
        if(victim.isNpc() && (!((Npc)victim).isVisible() || victim.isDead())) {
			return;
        }
	    if (victim.isNpc() && ((Npc) victim).walkingBackToSpawn) {
		CombatManager.resetCombat(victim);
	    }
	    if (attacker.failedCriticalRequirement()) {
		attacker.setFailedCriticalRequirement(false);
		CombatManager.resetCombat(attacker);
		attacker.getMovementHandler().reset();
		return;
	    }
        if (victim.getMaxHp() < 1 || (victim.isNpc() && (((Npc) victim).getNpcId() == 411 || TalkToEvent.isTalkToNpc(((Npc) victim).getNpcId()) || ((Npc) victim).getNpcId() == 3782 || ((Npc) victim).getNpcId() == AnimalMagnetism.UNDEAD_TREE || PestControl.isShieldedPortal((Npc)victim)) ) ) {
        	if (attacker.isPlayer()) {
        		((Player) attacker).getActionSender().sendMessage("You cannot attack this npc.");
        	}
        	CombatManager.resetCombat(attacker);
        	return;
        }
		if(attacker.isPlayer() && ((Player) attacker).getPets().getPet() == victim) {
	            ((Player) attacker).getActionSender().sendMessage("You cannot attack your own pet!");
	            CombatManager.resetCombat(attacker);
	            return;
		}
		if(attacker.isPlayer() && (((Player) attacker).transformNpc == 1707 || ((Player) attacker).transformNpc == 1708)) {
	            ((Player) attacker).getDialogue().sendPlayerChat("I can't see to attack!", Dialogues.DISTRESSED);
	            CombatManager.resetCombat(attacker);
	            return;
		}
		if(attacker.isPlayer() && ((Player) attacker).getMMVars().isMonkey()) {
	            ((Player)attacker).getActionSender().sendMessage("You cannot attack as a monkey!");
	            CombatManager.resetCombat(attacker);
	            return;
		}
		if(attacker.isPlayer() && victim.isNpc() && ((Npc)victim).getDefinition().getName().toLowerCase().equals("pheasant")){
			Player player = ((Player) attacker);
			if(player.getRandomHandler().getCurrentEvent() == null || (player.getRandomHandler().getCurrentEvent() == player.getRandomHandler().getFreakyForester() && player.getRandomHandler().getFreakyForester().complete))
			{
				player.getDialogue().sendPlayerChat("I shouldn't attack these poor birds like that.", Dialogues.SAD);
			    return;
			}
		}
		if(attacker.isPlayer() && ( ((Player)attacker).getEquipment().getItemContainer().get(Constants.WEAPON) == null || ((Player)attacker).getEquipment().getItemContainer().get(Constants.WEAPON).getId() != 2402)
		   && victim.isNpc() && ((Npc)victim).getNpcId() == 879) {
	            ((Player) attacker).getActionSender().sendMessage("You need to equip Silverlight to fight Delrith!");
	            CombatManager.resetCombat(attacker);
	            return;
		}
		if(attacker.isPlayer() && victim.isNpc() && ((Npc)victim).getNpcId() == 742 && ((Player)attacker).getQuestStage(15) != 7) {
	            ((Player) attacker).getActionSender().sendMessage("I better not try this.");
	            CombatManager.resetCombat(attacker);
	            return;
		}
		if (attacker.isPlayer() && attacker.inDuelArena()) {
			if (!((Player) attacker).getDuelMainData().canStartDuel()) {
	        	CombatManager.resetCombat(attacker);
				return;
			}
		}
		if(attacker.isPlayer() && victim.isNpc()) {
		    Npc npc = (Npc)victim;
		    Player player = (Player)attacker;
		    if(npc.getNpcId() >= 6026 && npc.getNpcId() < 6046 && player.getEquipment().getId(Constants.WEAPON) != PriestInPeril.WOLFBANE) {
			npc.sendTransform(npc.getNpcId() - 20, 500);
		    }
		}
        List<AttackUsableResponse> attacks = new LinkedList<AttackUsableResponse>();
        int distance = Misc.getDistance(attacker.getPosition(), victim.getPosition());
        attacker.fillUsableAttacks(attacks, victim, distance);
        AttackUsableResponse foundResponse = null;
        for (AttackUsableResponse response : attacks) {
            if (response.getType() != AttackUsableResponse.Type.FAIL) {
                if (foundResponse == null || response.getScript().distanceRequired() > foundResponse.getScript().distanceRequired())
                    foundResponse = response;
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
					if (!hit.getVictim().isDead())
						hit.execute(recoilHits);
					if (hit.getVictim().getCurrentHp() <= 0 && !hit.getVictim().isDead() ) {
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
			for(Player player : World.getPlayers()) {
			    if(player == null)
				continue;
			    else if( player != null && player.getPosition().getY()-1 == died.getPosition().getY())
				player.walkTo(new Position(2545, 10144, 0), true);
			    else if(player != null && player.getPosition().getY()+1 == died.getPosition().getY())
				player.walkTo(new Position(2545, 10142, 0), true);
			    else if(player != null && player.getPosition().getX()+1 == died.getPosition().getX())
				player.walkTo(new Position(2544, 10143, 0), true);
			}
			
		}
		final Entity killer = died.findKiller();// == null ? possibleKiller : died.findKiller();
		died.getTask();
        died.setDeathPosition(died.getPosition().clone());
		if (killer != null && killer.isPlayer()) {
			Player player = (Player) killer;
			player.getPjTimer().setWaitDuration(0);
			player.getPjTimer().reset();
			if (player.getNewComersSide().getTutorialIslandStage() == 45 || player.getNewComersSide().getTutorialIslandStage() == 47)
				player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);
		}
		if (died.isPlayer()) {
			Player player = (Player) died;
			player.setStopPacket(true);
		}
		if(died.isNpc() && killer != null && killer.isPlayer() && ((Npc)died).getNpcId() == 757 && VampireSlayer.handleCountDeath((Player) killer, (Npc)died)) {
		    died.setDead(false);
		    return;
		}
		if(died.isNpc() && killer != null && killer.isPlayer() && ((Npc)died).getNpcId() == FamilyCrest.CHRONOZON && FamilyCrest.handleChronozonDeath((Player) killer, (Npc)died)) {
		    died.setDead(false);
		    return;
		}
		if(died.isNpc() && killer != null && killer.isPlayer() && ((Npc)died).getIndex() == ((Player)killer).getHintIndex()) {
		    ((Player)killer).getActionSender().createPlayerHints(1, Npc.getNpcById(0).getIndex());
		    ((Player)killer).getActionSender().createPlayerHints(10, Npc.getNpcById(0).getIndex());
		    ((Player)killer).setHintIndex(-1);
		}
		if(died.isNpc() && killer != null && killer.isPlayer() && ((Npc)died) == ((Player)killer).getBarbarianSpirits().getSpiritSummoned()) {
		    ((Player)killer).getBarbarianSpirits().setSpiritSummoned(null);
		}
		if(died != null && died.isPlayer() && ((Player)died).inMageArena() && ((Player) died).getMageArenaStage() == 1 ) {
		    Player player = (Player)died;
		    Position pos = player.getPosition().clone();
		    for(Npc npc : World.getNpcs()) {
			if(npc == null)
			    continue;
			if(npc.getDefinition().getName().equalsIgnoreCase("kolodion")
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
					if(killer != null && killer.isPlayer() && died.isNpc()){
			            Player player = (Player) killer;
			           	Npc npc = (Npc) died;
			           	if (npc.getPosition().isViewableFrom(player.getPosition())) {
			           		player.getCombatSounds().npcDeathSound(((Npc) died));
			           	}
			       }
					if( died.isNpc() && PestControl.isSplatter((Npc) died)) {
					    died.getUpdateFlags().sendAnimation(3888, 75);
					    died.getUpdateFlags().sendGraphic(287, 100);
					    stop();
					}
					else if(died.isNpc() && PestControl.isSpinner((Npc) died) && deathByPortal ) {
					    died.getUpdateFlags().sendAnimation(3889);
					    died.getUpdateFlags().sendGraphic(1207);
					    deathByPortal = false;
					    stop();
					}
					else if(died.isNpc() && PestControl.isSpinner((Npc) died) && !deathByPortal ) {
					    died.getUpdateFlags().sendAnimation(3910);
					    stop();
					}
					else if(died.isNpc() && ((Npc)died).getNpcId() == 916) { // joe
					    died.getUpdateFlags().sendAnimation(836);
					    stop();
					}
					else if(died.isNpc() && ((Npc)died).getNpcId() == 919) { // keli
					    died.getUpdateFlags().sendAnimation(424);
					    stop();
					}
					else if(died.isNpc() && ((Npc)died).getNpcId() == 920) { // prince ali
					    died.getUpdateFlags().sendAnimation(804);
					    stop();
					}
					else if(died.isNpc() && ((Npc)died).getNpcId() == 247 && killer.isPlayer() && ((Player)killer).getQuestStage(11) == 5) { // mordred
					    died.getUpdateFlags().sendAnimation(1331);
					    stop();
					}
					else if(died.isNpc() && ((Npc)died).getNpcId() == 643) { //weaponsmaster phoenix gang
					    died.getUpdateFlags().sendForceMessage("Damn crossbows...");
					    died.getUpdateFlags().sendAnimation(death);
					    stop();
					}
					else if(died.isNpc() && ((Npc)died).getNpcId() == HorrorFromTheDeep.CHILD_DAGANNOTH && killer != null && killer.isPlayer()) {
					    ((Player)killer).setQuestStage(26, 5);
					    Dialogues.startDialogue((Player)killer, HorrorFromTheDeep.SITTING_JOSSIK);
					    died.getUpdateFlags().sendAnimation(death);
					    stop();
					}
					else if(died.isNpc() && ((Npc)died).getNpcId() >= 1351 && ((Npc)died).getNpcId() < 1357 && killer != null && killer.isPlayer()) {
					    ((Player)killer).setQuestStage(26, 6);
					    GroundItem drop = new GroundItem(new Item(6729), ((Player)killer), died.getPosition().clone());
					    GroundItemManager.getManager().dropItem(drop);
					    ((Player)killer).getInventory().addItemOrDrop(new Item(HorrorFromTheDeep.RUSTY_CASKET));
					    Dialogues.startDialogue((Player)killer, HorrorFromTheDeep.SITTING_JOSSIK);
					    died.getUpdateFlags().sendAnimation(death);
					    stop();
					}
					else if(died.isNpc() && ((Npc)died).getDefinition().getName().toLowerCase().equals("evil chicken") && killer != null && killer.isPlayer()) {
					    Player player = (Player)killer;
					    double reward = new Random().nextDouble() * 750;
					    player.getActionSender().sendMessage("You pluck feathers from the Evil Chicken's corpse.");
					    player.getInventory().addItemOrDrop(new Item(314, (int)reward > 250 ? (int)reward : 250));
					    died.getUpdateFlags().sendAnimation(death);
					    stop();
					}
					else {
					    died.getUpdateFlags().sendAnimation(death);
					    stop();
					}
				}
			};
			World.getTickManager().submit(tick);
		}
		if(died.isNpc() && ((Npc)died).getNpcId() == 247 && killer.isPlayer() && ((Player)killer).getQuestStage(11) == 5) {
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
		Dialogues.startDialogue((Player)killer, 248);
		died.heal(5);
		died.removeAllEffects();
		return;
		}
		if(died.isNpc() && ((Npc)died).getNpcId() == 1158) {
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
		}
		else {
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
		}
		else if (died.isNpc() && ((Npc)died).getNpcId() == 757 && killer != null && killer.isPlayer()) { //count draynor
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
			ClueScroll.handleAttackerDeath((Player)killer, npc);
			((Player) killer).getSlayer().handleNpcDeath(npc);
			((Player) killer).getBarrows().handleDeath(npc);
			WarriorsGuild.dropDefender((Player) killer, npc);
			ShieldOfArrav.handleDrops((Player) killer, npc);
			DragonSlayer.handleDrops((Player) killer, npc);
			PriestInPeril.handleDrops((Player) killer, npc);
			HeroesQuest.handleGripDeath((Player)killer, npc);
			FamilyCrest.handleDrops((Player) killer, npc);
			((Player) killer).getRandomHandler().getFreakyForester().handleDrops(npc);
			for(Quest q : QuestHandler.getQuests()) {
				q.handleDeath((Player) killer, npc);
			}
			if(((Player) killer).getSpawnedNpc() != null) {
			    ((Player) killer).setSpawnedNpc(null);
			}
		    }
		}
	    if (died != null && died.isNpc()) {
    		final Npc npc = (Npc) died;
		if (npc.getNpcId() == 1158 && firstTime ) { // kq
		    if (World.npcAmount(1160) == 0) {
			Npc newQueen = new Npc(1160);
			newQueen.setSpawnPosition(died.getPosition().clone());
			newQueen.setPosition(died.getPosition().clone());
			newQueen.setCombatDelay(10);
			newQueen.getMovementPaused().setWaitDuration(10);
			World.register(newQueen);
			newQueen.getUpdateFlags().sendForceMessage("Bzzzzz");
		    }
		}
		else if( npc.getNpcId() == 1160 ) {
		    npc.setDead(true);
		    npc.setVisible(false);
		    World.unregister(npc);
		    return;
		}
		else if(npc.getNpcId() >= 6026 && npc.getNpcId() <= 6045) {
		    npc.setTransformUpdate(true);
		}
		else if(npc.getNpcId() == 742 && killer.isPlayer()) {
		    if(((Player)killer).getQuestStage(15) == 7) {
			((Player)killer).setQuestStage(15, 8);
			((Player)killer).getDialogue().sendStatement("You sever Elvarg's head as proof for Oziach.");
			if(!((Player)killer).getInventory().ownsItem(11279)) {
			    ((Player)killer).getInventory().addItem(new Item(11279));
			}
		    }
		}
		else if(npc.getNpcId() == GhostsAhoy.GIANT_LOBSTER) {
		    if(killer != null && killer.isPlayer()) {
			((Player)killer).getQuestVars().setLobsterSpawnedAndDead(true);
		    }
		}
		else if ( npc.getNpcId() == 879 && firstTime ) { // delrith
		    Npc delrith = new Npc(880);
		    delrith.setSpawnPosition(died.getPosition().clone());
		    delrith.setPosition(died.getPosition().clone());
		    delrith.setCombatDelay(10);
		    delrith.getMovementPaused().setWaitDuration(10);
		    World.register(delrith);
		    if(killer != null && killer.isPlayer()) {
			delrith.setPlayerOwner(((Player)killer).getIndex());
		    }
		    Dialogues.startDialogue((Player)killer, 10666);
		}
		else if(npc.getDefinition().getName().equalsIgnoreCase("kolodion")) {
		    if(npc.getNpcId() == 911 && killer.isPlayer()) {
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
			PlayerSave.save((Player)killer);
		    }
		    else {
			Npc newKol = new Npc(npc.getNpcId()+1);
			newKol.setSpawnPosition(npc.getPosition().clone());
			newKol.setPosition(npc.getPosition().clone());
			newKol.setNeedsRespawn(false);
			newKol.setCombatDelay(2);
			newKol.setPlayerOwner(((Player)killer).getIndex());
			World.register(newKol);
			attack(newKol, killer);
			if(newKol.getNpcId() == 911)
			    newKol.getUpdateFlags().sendForceMessage("Aaargh! I cannot lose!");
			else
			    newKol.getUpdateFlags().sendForceMessage("How about this?");
		    }
		}
		else if(WarriorsGuild.isAnimatedArmor(npc) && killer.isPlayer()) {
		    ((Player)killer).getActionSender().sendMessage("The ref awards you with some tokens.");
		    ((Player)killer).getInventory().addItem(new Item(8851, WarriorsGuild.getTokenAmount(npc)));
		}
		else if(npc.getNpcId() == 238 && killer.isPlayer() && ((Player)killer).getQuestStage(11) == 10) {
		    ((Player)killer).setQuestStage(11, 11);
		    ((Player)killer).getDialogue().sendStatement("With the spirit dead, you can now smash Merlin's crystal.");
		}
		if(killer != null && killer.isPlayer()) {
		    FightCaves.handleDeath((Player) killer, npc);
		}
		if (!npc.needsRespawn()) {
		    npc.setVisible(false);
		    World.unregister(npc);
		}
		else {
		    if (npc.isVisible()) {
			npc.setVisible(false);
                	npc.setPosition(npc.getSpawnPosition().clone());
            		npc.getMovementHandler().reset();
            		npc.sendTransform(npc.getOriginalNpcId(), 0);
            		CombatManager.resetCombat(npc);
			int respawnTimer = npc.getRespawnTimer();
			if(npc.getNpcId() == 1158)
			    respawnTimer = 300;
			else if(npc.getNpcId() == 2881 || npc.getNpcId() == 2882 || npc.getNpcId() == 2883)
			    respawnTimer = 60;
			else if(npc.getNpcId() == 916) // joe
			    respawnTimer = 120;
			else if(npc.getNpcId() == 920) // prince ali
			    respawnTimer = 120;
			else if(npc.getNpcId() == ErnestTheChicken.ERNEST_CHICKEN)
			    respawnTimer = 60;
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
			//died.setDeathTimer(npc.getRespawnTimer());
                    return;
		    }
		    else {
			npc.setVisible(true);
		    }
		}
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
	    if (died.isPlayer()) {
		Player player = (Player) died;
		player.setStopPacket(false);
		player.setHideWeapons(false);
		player.setAutoSpell(null);
		player.resetEffects();
		player.getSkill().refresh();
	    }
	    died.getHitRecordQueue().clear();
	    if (died.isPlayer() && died.inDuelArena()) {
		((Player) died).getDuelMainData().handleDeath(false);
		return;
	    }
		if (died.isPlayer() && ((Player) died).getCreatureGraveyard().isInCreatureGraveyard()) {
			((Player) died).getCreatureGraveyard().handleDeath();
			return;
		}
        if(died != null && died.isPlayer() && ((Player) died).inPestControlGameArea()) {
            PestControl.handleDeath((Player) died);
		    return;
        }
		if(died != null && died.isPlayer() && ((Player) died).onPestControlIsland() ) {
		    ((Player) died).teleport(new Position(2657, 2639, 0));
		    return;
		}
        if(died != null && Castlewars.handleDeath(died)) {
		    return;
        }
		if(died != null && died.isPlayer() && ((Player) died).inFightCaves()) {
                    FightCaves.exitCave((Player) died);
		    return;
		}
		if (died.isNpc() && ((Npc) died).getNpcId() == 655) {
			if (killer != null && killer.isPlayer()) {
				((Player) killer).setKilledTreeSpirit(true);
				((Player) killer).getDialogue().sendStatement("With the Tree Spirit defeated you can now chop the tree.");
			}
		}
		if (died.isNpc() && PestControl.isSplatter((Npc) died) && died.inPestControlGameArea() ) {
		    for (Player players : World.getPlayers()) {
			if (players != null && Misc.getDistance(died.getPosition(), players.getPosition()) <= 3 )
			    players.hit(15 + Misc.random(15), HitType.NORMAL);
		    }
		    for (Npc npcs : World.getNpcs()) {
			if (npcs != null && npcs.goodDistanceEntity(died, 3) && npcs != died && !PestControl.isPortal(npcs))
			    npcs.hit(15 + Misc.random(15), HitType.NORMAL);
		    }
		    for (PestControl.BarricadeData b : PestControl.BarricadeData.values()) {
				if (b.forName(b.name()) != null) {
				    for (Position p : b.iterablePositions()) {
						if (PestControl.getBrokenBarricades().contains(p)) {
						    continue;
						}
						final CacheObject g = ObjectLoader.object(p.getX(), p.getY(), 0);
						if (g != null) {
						    if (Misc.goodDistance(died.getPosition(), p, 3)) {
							b.ravage(p, false);
						    }
						}
				    }
				}
		    }
		}
		if(died.isNpc() && PestControl.isPortal((Npc) died) && died.inPestControlGameArea()) {
		    for (Npc npcs : World.getNpcs()) {
			if(npcs != null && npcs.goodDistanceEntity(died, 4) && !npcs.isDead() && PestControl.isSpinner(npcs)) {
			    int hp = npcs.getCurrentHp();
			    npcs.hit(hp, HitType.NORMAL);
			    deathByPortal = true;
			    for (Player players : World.getPlayers()) {
				if (players != null && Misc.getDistance(died.getPosition(), players.getPosition()) <= 5 && !PestControl.allPortalsDead() ) {
				    players.hit(50, HitType.NORMAL);
				    players.getActionSender().sendMessage("You are hurt by the spinner's lost connection to the portal.");
				}
			    }
			}
		    }
		}
		if (died.isPlayer()) {
		    Player player = (Player) died;
		    player.teleport(player.getQuestVars().isGazeOfSaradomin() ? Teleportation.WHITE_KNIGHTS_CASTLE : Teleportation.HOME);
		    player.getActionSender().sendMessage("Oh dear, you are dead!");
			if (player.getMultiCannon() != null && player.getMultiCannon().hasCannon()) {
				player.getMultiCannon().pickupCannon();
			}
			if(player.getInJail()){
				player.setInJail(false);
			}
		    PlayerSave.save(player);
		    player.getActionSender().sendQuickSong(75, 16);
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

	public static double calculateMaxHit(Player player, WeaponAttack weaponAttack) {
		AttackStyle attackStyle = weaponAttack.getAttackStyle();
        double damage = 0;
		if (attackStyle.getAttackType() == AttackType.MELEE)
			damage = calculateMaxMeleeHit(player, weaponAttack);
		else if (attackStyle.getAttackType() == AttackType.RANGED && weaponAttack.getRangedAmmo() != null)
			damage = calculateMaxRangedHit(player, weaponAttack);
		return damage;
	}

	public static double calculateMaxRangedHit(Player player, WeaponAttack weaponAttack) {
		int rangedLevel = player.getSkill().getLevel()[Skill.RANGED];
		double styleBonus = 0;
		AttackStyle attackStyle = weaponAttack.getAttackStyle();
		if (attackStyle.getMode() == AttackStyle.Mode.RANGED_ACCURATE)
			styleBonus = 3;
		else if (attackStyle.getMode() == AttackStyle.Mode.LONGRANGE)
			styleBonus = 1;
		else if (player.hasFullVoidRange())
			rangedLevel = (int) styleBonus + (int)(rangedLevel * 1.1);
		if (player.getIsUsingPrayer()[PrayerData.SHARP_EYE.getIndex()])
			rangedLevel *= 1.05;
		else if (player.getIsUsingPrayer()[PrayerData.HAWK_EYE.getIndex()])
			rangedLevel *= 1.1;
		else if (player.getIsUsingPrayer()[PrayerData.EAGLE_EYE.getIndex()])
			rangedLevel *= 1.15;
		rangedLevel += styleBonus;
		double rangedStrength = weaponAttack.getRangedAmmo().getRangeStrength();
		double maxHit = (rangedLevel + rangedStrength / 8 + rangedLevel * rangedStrength * Math.pow(64, -1) + 14) / 10;
		if(player.hasFullVoidRange())
		    maxHit = maxHit * 1.1;
		return (int) Math.floor(maxHit);
	}

	public static double calculateMaxMeleeHit(Player player, WeaponAttack weaponAttack) {
		double strengthLevel = player.getSkill().getLevel()[Skill.STRENGTH];
		if (player.getIsUsingPrayer()[PrayerData.BURST_OF_STRENGTH.getIndex()])
			strengthLevel *= 1.05;
		else if (player.getIsUsingPrayer()[PrayerData.SUPERHUMAN_STRENGTH.getIndex()])
			strengthLevel *= 1.1;
		else if (player.getIsUsingPrayer()[PrayerData.ULTIMATE_STRENGTH.getIndex()])
			strengthLevel *= 1.15;
		AttackStyle attackStyle = weaponAttack.getAttackStyle();
		int styleBonus = 0;
		if (attackStyle.getMode() == AttackStyle.Mode.AGGRESSIVE)
			styleBonus = 3;
		else if (attackStyle.getMode() == AttackStyle.Mode.CONTROLLED)
			styleBonus = 1;
		int effectiveStrengthDamage = (int) (strengthLevel + styleBonus);
		double baseDamage = 5 + (effectiveStrengthDamage + 8) * (player.getBonus(10) + 64) / 64; //10 = str bonus
		if(player.hasFullVoidMelee()) {
		    baseDamage = baseDamage * 1.1;
		}
		else if(player.hasFullDharok()) {
		    double hpLost = player.getMaxHp() - player.getCurrentHp();
		    baseDamage += baseDamage * hpLost * 0.01;
		}
		else if(DemonSlayer.fightingDemon(player)) {
		    baseDamage = baseDamage * 1.75;
		}
		int maxHit = (int) Math.floor(baseDamage);
		return (int) Math.floor(maxHit / 10);
	}

	public static double getChance(double attack, double defence) {
		double A = Math.floor(attack);
		double D = Math.floor(defence);
		double chance = A < D ? (A - 1.0) / (2.0 * D) : 1.0 - (D + 1.0) / (2.0 * A);
		chance = chance > 0.9999 ? 0.9999 : chance < 0.0001 ? 0.0001 : chance;
		return chance;
	}

	public static final Random r = new Random(System.currentTimeMillis());

	public static boolean isAccurateHit(double chance) {
		return r.nextDouble() <= chance;
	}

	private static double getEffectiveAccuracy(Entity attacker, AttackStyle attackStyle) {
		double attackBonus = attacker.getBonus(attackStyle.getBonus().toInteger());
		double baseAttack = attacker.getBaseAttackLevel(attackStyle.getAttackType());
		if (attackStyle.getAttackType() == AttackType.MELEE && attacker.isPlayer()) {
			Player player = (Player) attacker;
			if (player.getIsUsingPrayer()[PrayerData.CLARITY_OF_THOUGHT.getIndex()])
				baseAttack *= 1.05;
			else if (player.getIsUsingPrayer()[PrayerData.IMPROVED_REFLEXES.getIndex()])
				baseAttack *= 1.1;
			else if (player.getIsUsingPrayer()[PrayerData.INCREDIBLE_REFLEXES.getIndex()])
				baseAttack *= 1.15;
			else if(player.hasFullVoidMelee())
				baseAttack *= 1.1;
		}
		else if(attackStyle.getAttackType() == AttackType.RANGED && attacker.isPlayer())
		{
			Player player = (Player) attacker;
			if (player.getIsUsingPrayer()[PrayerData.SHARP_EYE.getIndex()])
				baseAttack *= 1.05;
			else if (player.getIsUsingPrayer()[PrayerData.HAWK_EYE.getIndex()])
				baseAttack *= 1.1;
			else if (player.getIsUsingPrayer()[PrayerData.EAGLE_EYE.getIndex()])
				baseAttack *= 1.15;
		}
		else if(attackStyle.getAttackType() == AttackType.MAGIC && attacker.isPlayer())
		{
			Player player = (Player) attacker;
			if (player.getIsUsingPrayer()[PrayerData.MYSTIC_WILL.getIndex()])
				baseAttack *= 1.05;
			else if (player.getIsUsingPrayer()[PrayerData.MYSTIC_LORE.getIndex()])
				baseAttack *= 1.1;
			else if (player.getIsUsingPrayer()[PrayerData.MYSTIC_MIGHT.getIndex()])
				baseAttack *= 1.15;
		}
		return Math.floor(baseAttack + attackBonus) + 8;
	}

	private static double getEffectiveDefence(Entity victim, AttackStyle attackStyle) {
		double baseDefence = victim.getBaseDefenceLevel(attackStyle.getAttackType());
		if((attackStyle.getMode() == AttackStyle.Mode.MAGIC && victim.isPlayer()) || (attackStyle.getAttackType() == AttackType.MAGIC && victim.isPlayer())) {
		    Player player = (Player)victim;
		    return player.getBonus(8);
		}
		if (attackStyle.getAttackType() == AttackType.MELEE && victim.isPlayer()) {
			Player player = (Player) victim;
			if (player.getIsUsingPrayer()[PrayerData.THICK_SKIN.getIndex()])
				baseDefence *= 1.05;
			else if (player.getIsUsingPrayer()[PrayerData.ROCK_SKIN.getIndex()])
				baseDefence *= 1.1;
			else if (player.getIsUsingPrayer()[PrayerData.STEEL_SKIN.getIndex()])
				baseDefence *= 1.15;
		}
		else if(attackStyle.getAttackType() == AttackType.MAGIC && victim.isNpc()) {
		    Npc npc = (Npc)victim;
		    return npc.getDefinition().getDefenceMage();
		}
		return Math.floor(baseDefence) + 8;
	}

	public static double getDefenceRoll(Entity victim, HitDef hitDef) {
		AttackStyle attackStyle = hitDef.getAttackStyle();
        //if (victim.isNpc())
        //    return victim.getBonus(attackStyle.getBonus().toInteger() + AttackStyle.Bonus.values().length);
		double effectiveDefence = getEffectiveDefence(victim, attackStyle);
		if(victim.isPlayer() && attackStyle.getAttackType() != AttackType.MAGIC) {
		   effectiveDefence += victim.getBonus(attackStyle.getBonus().toInteger() + AttackStyle.Bonus.values().length); 
		}
		int styleBonusDefence = 0;
		if (victim.isPlayer()) {
			Player pVictim = ((Player) victim);
			if (attackStyle.getAttackType() == AttackType.MAGIC) {
				int level = pVictim.getSkill().getLevel()[Skill.MAGIC];
				effectiveDefence = (int) (Math.floor(level * 0.125) + Math.floor(effectiveDefence * 0.875));
				styleBonusDefence = 19;
			} else {
				AttackStyle defenceStyle = pVictim.getEquippedWeapon().getWeaponInterface().getAttackStyles()[pVictim.getFightMode()];
				if (defenceStyle.getMode() == AttackStyle.Mode.DEFENSIVE || defenceStyle.getMode() == AttackStyle.Mode.LONGRANGE)
					styleBonusDefence += 3;
				else if (defenceStyle.getMode() == AttackStyle.Mode.CONTROLLED)
					styleBonusDefence += 1;
			}
		}
		effectiveDefence *= (1 + (styleBonusDefence) / 64);
		if (hitDef.getSpecialEffect() == 11) { //verac effect
			effectiveDefence *= 0.75;
		}
		if(effectiveDefence < 0) {
		    effectiveDefence = 0;
		}
		return effectiveDefence;
	}

	public static double getAttackRoll(Entity attacker, HitDef hitDef) {
		AttackStyle attackStyle = hitDef.getAttackStyle();
        //if (attacker.isNpc())
        //    return attacker.getBonus(attackStyle.getBonus().toInteger());*/

		double specAccuracy = hitDef.getSpecAccuracy();
		double effectiveAccuracy = getEffectiveAccuracy(attacker, attackStyle);

		int styleBonusAttack = 0;
		if (attackStyle.getMode() == AttackStyle.Mode.MELEE_ACCURATE || attackStyle.getMode() == AttackStyle.Mode.RANGED_ACCURATE)
			styleBonusAttack = 3;
		else if (attackStyle.getMode() == AttackStyle.Mode.CONTROLLED)
			styleBonusAttack = 1;
		else if(attackStyle.getMode() == AttackStyle.Mode.MAGIC && attacker.isPlayer()) {
			Player player = (Player)attacker;
			styleBonusAttack = (int)((player.getSkill().getLevel()[Skill.MAGIC] + player.getBonus(3)) / 82.5);
			specAccuracy = 1;
		}
		effectiveAccuracy *= (1 + (styleBonusAttack) / 64);
		return (int) (effectiveAccuracy * specAccuracy);
	}

	/**
	 * Resets anything needed after the end of combat.
	 */
	public static void resetCombat(Entity entity) {
		if(entity == null) {
		    return;
		}
		if (entity.isPlayer()) {
			((Player) entity).setCastedSpell(null);
		}
		entity.setCombatingEntity(null);
		entity.setInstigatingAttack(false);
		entity.setSkilling(null);
		entity.getUpdateFlags().faceEntity(-1);
		if(entity.isNpc() && ((Npc)entity).getNpcId() == 1460) {
		    ((Npc)entity).setFollowingEntity(null);
		    ((Npc)entity).walkTo(((Npc)entity).getSpawnPosition(), true);
		}
		Following.resetFollow(entity);
	}
	public static boolean arenaNpc(Npc npc) {
	    if(npc.getNpcId() == 912 || npc.getNpcId() == 913 || npc.getNpcId() == 914)
		return true;
	    else return false;
	}

}