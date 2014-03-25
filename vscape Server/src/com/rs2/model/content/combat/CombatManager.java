package com.rs2.model.content.combat;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.World;
import com.rs2.model.content.Following;
import com.rs2.model.content.combat.attacks.WeaponAttack;
import com.rs2.model.content.combat.hit.Hit;
import com.rs2.model.content.combat.hit.HitDef;
import com.rs2.model.content.combat.weapon.AttackStyle;
import com.rs2.model.content.minigames.barrows.Barrows;
import com.rs2.model.content.randomevents.TalkToEvent;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.magic.Teleportation;
import com.rs2.model.content.skills.prayer.Prayer;
import com.rs2.model.content.treasuretrails.ClueScroll;
import com.rs2.model.npcs.Npc;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;

/**
 *  TODO: on death remove all victims hits from hitsStory
 *         log out during death Spell delays for ancients
 *         http://www.2006scape.com/services
 *         /forums/thread.ws?3,4,13136,38613,goto,3
 * 
 * 
 */
public class CombatManager extends Tick {

	private static CombatManager combatManager;

	public static final String NO_AMMO_MESSAGE = "You have no ammo left!";
	public static final String WRONG_AMMO_MESSAGE = "You can not use that kind of ammo!";
	public static final String AMMO_COMPATIBILITY_MESSAGE = "You cannot use that ammo with that weapon!";
	public static final String NO_SPECIAL_ENERGY_MESSAGE = "You have no special energy left.";

	public static int [] randomItem = {4033, 4012, 4024, 4027, 1963};
	private final List<Hit> hitsStory;

	public CombatManager() {
		super(1);
		hitsStory = new LinkedList<Hit>();
		World.getTickManager().submit(this);
	}

	public static void attack(Entity attacker, Entity victim) {
        if((victim.isNpc() && !((Npc)victim).isVisible()) || victim.isDead()) {
			return;
        }
        if (victim.getMaxHp() < 1 || (victim.isNpc() && (((Npc) victim).getNpcId() == 411 || TalkToEvent.isTalkToNpc(((Npc) victim).getNpcId())))) {
        	if (attacker.isPlayer()) {
        		((Player) attacker).getActionSender().sendMessage("You cannot attack this npc.");
        	}
        	CombatManager.resetCombat(attacker);
        	return;
        }
		if (attacker.isPlayer() && attacker.inDuelArena()) {
			if (!((Player) attacker).getDuelMainData().canStartDuel()) {
	        	CombatManager.resetCombat(attacker);
				return;
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
					died.getUpdateFlags().sendAnimation(death);
					stop();
				}
			};
			World.getTickManager().submit(tick);
		}
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
		if (killer != null && died.isPlayer() && ((Player) died).getIsUsingPrayer()[Prayer.RETRIBUTION]) {
			Prayer.applyRetribution(died, killer);
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
            	Barrows.handleDeath(((Player)killer), npc);
			}
		}
        if (died.isNpc()) {
    		final Npc npc = (Npc) died;
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
                    }, npc.getRespawnTimer());
                    //died.setDeathTimer(npc.getRespawnTimer());
                    return;
                } else {
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
		if (died.isNpc() && ((Npc) died).getNpcId() == 655) {
			if (killer != null && killer.isPlayer()) {
				((Player) killer).setKilledTreeSpirit(true);
				((Player) killer).getDialogue().sendStatement("With the Tree Spirit defeated you can now chop the tree.");
			}
		}
		if (died.isNpc() && ((Npc) died).getNpcId() == 1472) {
			if (killer != null && killer.isPlayer()) {
				((Player) killer).setKilledJungleDemon(true);
				((Player) killer).getDialogue().sendStatement("With the Jungle Demon defeated you can now wield a dragon scimitar.");
				((Player) killer).inventory.addItem(new Item(randomItem[Misc.random(5)], 1));
				//[Misc.random(1)]
			}
		}
		if (died.isPlayer()) {
			((Player) died).teleport(Teleportation.HOME);
            ((Player)died).getActionSender().sendMessage("Oh dear, you are dead!");
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
		rangedLevel += styleBonus;
		double rangedStrength = weaponAttack.getRangedAmmo().getRangeStrength();
		double maxHit = (rangedLevel + rangedStrength / 8 + rangedLevel * rangedStrength * Math.pow(64, -1) + 14) / 10;
		return (int) Math.floor(maxHit);
	}

	public static double calculateMaxMeleeHit(Player player, WeaponAttack weaponAttack) {
		double strengthLevel = player.getSkill().getLevel()[Skill.STRENGTH];
		if (player.getIsUsingPrayer()[Prayer.BURST_OF_STRENGTH])
			strengthLevel *= 1.05;
		else if (player.getIsUsingPrayer()[Prayer.SUPERHUMAN_STRENGTH])
			strengthLevel *= 1.1;
		else if (player.getIsUsingPrayer()[Prayer.ULTIMATE_STRENGTH])
			strengthLevel *= 1.15;
		AttackStyle attackStyle = weaponAttack.getAttackStyle();
		int styleBonus = 0;
		if (attackStyle.getMode() == AttackStyle.Mode.AGGRESSIVE)
			styleBonus = 3;
		else if (attackStyle.getMode() == AttackStyle.Mode.CONTROLLED)
			styleBonus = 1;
		int effectiveStrengthDamage = (int) (strengthLevel + styleBonus);
		double baseDamage = 5 + (effectiveStrengthDamage + 8) * (player.getBonus(10) + 64) / 64; //10 = str bonus
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
			if (player.getIsUsingPrayer()[Prayer.CLARITY_OF_THOUGHT])
				baseAttack *= 1.05;
			else if (player.getIsUsingPrayer()[Prayer.IMPROVED_REFLEXES])
				baseAttack *= 1.1;
			else if (player.getIsUsingPrayer()[Prayer.INCREDIBLE_REFLEXES])
				baseAttack *= 1.15;
		}
		return Math.floor(baseAttack + attackBonus) + 8;
	}

	private static double getEffectiveDefence(Entity victim, AttackStyle attackStyle) {
		double baseDefence = victim.getBaseDefenceLevel(attackStyle.getAttackType());
		if (attackStyle.getAttackType() == AttackType.MELEE && victim.isPlayer()) {
			Player player = (Player) victim;
			if (player.getIsUsingPrayer()[Prayer.THICK_SKIN])
				baseDefence *= 1.05;
			else if (player.getIsUsingPrayer()[Prayer.ROCK_SKIN])
				baseDefence *= 1.1;
			else if (player.getIsUsingPrayer()[Prayer.STEEL_SKIN])
				baseDefence *= 1.15;
		}
		return Math.floor(baseDefence) + 8;
	}

	public static double getDefenceRoll(Entity victim, HitDef hitDef) {
		AttackStyle attackStyle = hitDef.getAttackStyle();
        //if (victim.isNpc())
        //    return victim.getBonus(attackStyle.getBonus().toInteger() + AttackStyle.Bonus.values().length);
		double effectiveDefence = getEffectiveDefence(victim, attackStyle);
		effectiveDefence += victim.getBonus(attackStyle.getBonus().toInteger() + AttackStyle.Bonus.values().length);

		int styleBonusDefence = 0;
		if (victim.isPlayer()) {
			Player pVictim = ((Player) victim);
			if (attackStyle.getAttackType() == AttackType.MAGIC) {
				int level = pVictim.getSkill().getLevel()[Skill.MAGIC];
				effectiveDefence = (int) (Math.floor(level * 0.7) + Math.floor(effectiveDefence * 0.3));
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
		effectiveAccuracy *= (1 + (styleBonusAttack) / 64);
		return (int) (effectiveAccuracy * specAccuracy);
	}

	/**
	 * Resets anything needed after the end of combat.
	 */
	public static void resetCombat(Entity entity) {
		if (entity.isPlayer()) {
			((Player) entity).setCastedSpell(null);
		}
		entity.setCombatingEntity(null);
		entity.setInstigatingAttack(false);
		entity.setSkilling(null);
		entity.getUpdateFlags().faceEntity(-1);
		Following.resetFollow(entity);
	}

}