package com.rs2.model.content.combat.hit;

import com.rs2.Constants;

import java.util.List;

import com.rs2.model.Entity;
import com.rs2.model.Graphic;
import com.rs2.model.Position;
import com.rs2.model.UpdateFlags;
import com.rs2.model.World;
import com.rs2.model.content.combat.AttackType;
import com.rs2.model.content.combat.CombatCalculations;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.combat.attacks.SpellAttack;


import com.rs2.model.content.combat.effect.Effect;
import com.rs2.model.content.combat.effect.impl.BindingEffect;
import com.rs2.model.content.combat.effect.impl.StatEffect;
import com.rs2.model.content.combat.effect.impl.StunEffect;
import com.rs2.model.content.combat.projectile.Projectile;
import com.rs2.model.content.combat.special.SpecialType;
import com.rs2.model.content.combat.util.Degrading;
import com.rs2.model.content.combat.util.EnchantedBolts;
import com.rs2.model.content.combat.util.RingEffect;
import com.rs2.model.content.combat.weapon.AttackStyle;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.content.minigames.fightcaves.FightCaves;
import com.rs2.model.content.minigames.pestcontrol.PestControl;
import com.rs2.model.content.quests.impl.AnimalMagnetism;
import com.rs2.model.content.quests.impl.Biohazard;
import com.rs2.model.content.quests.impl.DemonSlayer;
import com.rs2.model.content.quests.impl.FamilyCrest;
import com.rs2.model.content.quests.impl.GoblinDiplomacy;
import com.rs2.model.content.quests.impl.HorrorFromTheDeep;
import com.rs2.model.content.quests.impl.MonkeyMadness.ApeAtoll;
import com.rs2.model.content.quests.impl.MonkeyMadness.MonkeyMadness;
import com.rs2.model.content.quests.impl.NatureSpirit;
import com.rs2.model.content.quests.impl.RecruitmentDrive;
import com.rs2.model.content.quests.impl.VampireSlayer;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.magic.Spell;
import com.rs2.model.content.skills.prayer.Prayer;
import com.rs2.model.content.skills.prayer.Prayer.PrayerData;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcDefinition;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;

import java.util.Random;

/**
 *
 */
public class Hit {

	private HitDef hitDef;
	private int hitDelay, damage;
	private Entity attacker, victim;
	private boolean isDelayedDamaged, hit;

	public Hit(Entity attacker, Entity victim, HitDef hitDef) {
		this.attacker = attacker;
		this.victim = victim;
		this.hitDef = hitDef;
		this.damage = hitDef.getDamage();
	}

	public void tick() {
		hitDelay--;
	}

	public void initialize() {
		initialize(true);
	}

	public void initialize(boolean queue) {
		if(victim == null || !victimCanBeHit()) {
			return;
		}
		if (hitDef.shouldRandomizeDamage()) {
			int range = hitDef.isDarkBowSpec() ? 5 : hitDef.isDarkBowDragonSpec() ? 8 : 0;
			damage = Misc.random(range, damage);
		}
		if (!hitDef.isUnblockable() && hitDef.shouldCheckAccuracy()) {
			executeAccuracy();
		}
		doInitializeHitChecks();
		if (hitDef.getProjectileDef() != null) {
			new Projectile(attacker, victim, hitDef.getProjectileDef()).show();
		}
		hitDelay = hitDef.calculateHitDelay(attacker != null ? attacker.getPosition() : null, victim.getPosition());
		if (queue) {
			CombatManager.getManager().submit(this);
		}
	}

	public void display() {
		if (!canDamageEnemy() || damage < 0) {
			return;
		}
		UpdateFlags flags = victim.getUpdateFlags();
		HitType hitType = damage == 0 ? HitType.MISS : hitDef.getHitType();
		if (attacker != null && attacker.isNpc() && ((Npc) attacker).getNpcId() == 1052 && hitType == HitType.MISS) {
			return;
		}
		if (hitDef.isDarkBowSpec() && hitType == HitType.MISS) {
			hitType = hitDef.getHitType();
			damage = 5;
		}
		if (hitDef.isDarkBowDragonSpec() && hitType == HitType.MISS) {
			hitType = hitDef.getHitType();
			damage = 8;
		}
		if (!flags.isDamage1Set()) {
			flags.setHitUpdate(true);
			flags.setDamage(damage);
			flags.setHitType(hitType.toInteger());
		} else if (!flags.isDamage2Set()) {
			flags.setHitUpdate2(true);
			flags.setDamage2(damage);
			flags.setHitType2(hitType.toInteger());
		} else {
			flags.queueDamage(damage, hitType.toInteger());
		}
		if (hitDef.getAttackStyle() != null && hitDef.getAttackStyle().getAttackType() == AttackType.MAGIC) {
			Spell spell = SpellAttack.getSpellForHitGfx(hitDef.getHitGraphic());
			if (spell != null) {
				if (attacker != null && attacker.isPlayer()) {
					Player player = (Player) attacker;
					player.getCombatSounds().spellSound(spell, false);
				}
				if (victim != null && victim.isPlayer()) {
					Player player = (Player) victim;
					player.getCombatSounds().spellSound(spell, false);
				}
			}
		} else {
			if (attacker != null && attacker.isPlayer() && victim.isNpc()) {
				Player player = (Player) attacker;
				Npc npc = (Npc) victim;
				if (npc.getCombatingEntity() == player && npc.getPosition().isViewableFrom(player.getPosition())) {
					if (hitType == HitType.MISS) {
						player.getCombatSounds().npcBlockSound(((Npc) victim));
					} else {
						player.getCombatSounds().npcDamageSound(((Npc) victim));
					}
				}
			}
			if (attacker != null && attacker.isPlayer() && victim != null && victim.isPlayer()) {
				if (hitDef.getHitType() != HitType.POISON && hitDef.getHitType() != HitType.BURN) {
					Player att = (Player) attacker;
					Player vic = (Player) victim;
					if (vic.getPosition().isViewableFrom(att.getPosition())) {
						if (hitType == HitType.MISS) {
							att.getCombatSounds().blockSound(vic);
						} else {
							att.getCombatSounds().damageSound();
						}
					}
				}
			}
			if (victim != null && victim.isPlayer()) {
				Player player = (Player) victim;
				if (hitType == HitType.MISS) {
					player.getCombatSounds().blockSound(player);
				} else {
					player.getCombatSounds().damageSound();
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public void execute(List<Hit> recoilList) {
		if (victim == null || !canDamageEnemy()) {
			return;
		}
		if (hitDef.getDropItem() != null && attacker.isPlayer()) {
			Player player = (Player) attacker;
			if (player.isDropArrow() && Misc.getRandom().nextInt(10) < 6) {
				Item arrow = new Item(hitDef.getDropItem().getId(), hitDef.getDropItem().getCount());
				if (player.getEquipment().getId(Constants.CAPE) == AnimalMagnetism.AVAS_ATTRACTOR || player.getEquipment().getId(Constants.CAPE) == AnimalMagnetism.AVAS_ACCUMULATOR) {
					player.getEquipment().addEquipment(arrow, arrow.getDefinition().getSlot());
				} else {
					GroundItem dropItem = new GroundItem(arrow, player, victim.getPosition().clone());
					GroundItemManager.getManager().dropItem(dropItem);
				}
				/*if (player.inDuelArena()) {
					player.getActionSender().sendMessage(""+hitDef.getDropItem().getId());
					player.getDuelMainData().getAmmoUsed().add(new Item(hitDef.getDropItem().getId(), hitDef.getDropItem().getCount()));
				}*/
			}
		}
		//Retaliation
		if (!isDelayedDamaged && !hitDef.isUnblockable() && hitDef.getHitType() != HitType.POISON && hitDef.getHitType() != HitType.BURN) {
			if (victim.isNpc()) {
				Npc npc = (Npc)victim;
				if(!npc.isDontAttack() && !npc.walkingBackToSpawn) {
					CombatManager.attack(npc, attacker);
				}
			} else if (victim.isPlayer()) {
				Player player = (Player)victim;
				if (!player.isMoving() && player.shouldAutoRetaliate() && player.getCombatingEntity() == null) {
					CombatManager.attack(player, attacker);
				}
			}
		}
		if (hitDef.doBlockAnim() && !victim.getUpdateFlags().isAnimationUpdateRequired()) {
			if (hitDef.getVictimAnimation() > 0) {
				victim.getUpdateFlags().sendAnimation(hitDef.getVictimAnimation());
			} else if (victim.isPlayer()) {
				Player player = (Player)victim;
				boolean b = player.transformNpc > 1;
				player.getUpdateFlags().sendAnimation(b ? NpcDefinition.forId(player.transformNpc).getBlockAnim() : player.getBlockAnimation());
			} else {
				if (victim.getBlockAnimation() > 0) {
					victim.getUpdateFlags().sendAnimation(victim.getBlockAnimation());
				}
			}
		}
		if (hitDef.getHitGraphic() != null) {
			victim.getUpdateFlags().sendGraphic(hitDef.getHitGraphic());
		}
		if (!hit && !hitDef.isUnblockable()) {
			damage = 0;
			if (hitDef.getAttackStyle() != null && hitDef.getAttackStyle().getAttackType() != AttackType.MAGIC) {
				display();
			}
			return;
		}
		if (!isDelayedDamaged) {
			if (hitDef.getAttackStyle() != null) {
				doProtectionChecks();
			}
		}
		doExecuteHitChecks();
		SpecialType.doSpecEffect(attacker, victim, hitDef, damage);
		int currentHp = victim.getCurrentHp();
		if (damage > currentHp) {
			damage = currentHp;
		}
		if (hitDef.getEffects() != null && hitDef.getEffects().size() > 0) {
			for (Effect effect : hitDef.getEffects()) {
				if (effect != null) {
					effect.execute(this);
				}
			}
		}
		if (hitDef != null && attacker != null && attacker.isPlayer()) {
			if (canAddXp()) {
				addCombatExp((Player) attacker, damage, hitDef.getAttackStyle());
			}
		}
		currentHp -= damage;
		if (hitDef.getHitAnimation() != -1) {
			victim.getUpdateFlags().sendAnimation(hitDef.getHitAnimation());
		}
		if (attacker != null) {
			HitRecord hitRecord = victim.getHitRecord(attacker);
			if (hitRecord == null) {
				hitRecord = new HitRecord(attacker);
			} else {
				victim.getHitRecordQueue().remove(hitRecord);
			}
			hitRecord.addDamage(damage);
			victim.getHitRecordQueue().add(hitRecord);
			if (attacker.isPlayer()) {
				Player player = (Player)attacker;
				if (player.getIsUsingPrayer()[PrayerData.SMITE.getIndex()] && victim.isPlayer()) {
					Prayer.applySmite(player, ((Player) victim), damage);
				}
			}
		}
		victim.setCurrentHp(currentHp);
		if (attacker != null && attacker.inPestControlGameArea() && victim.inPestControlGameArea()) {
			PestControl.handleHit(attacker, victim, damage);
		}
		if (hitDef.getSpecialEffect() != 5) { // d spear
			display();
		}
	}

	public void addCombatExp(Player player, int damage, AttackStyle attackStyle) {
		if (attackStyle == null) {
			return;
		}
		final double totalExp = damage * 4d;
		final double expPerSkill = totalExp / attackStyle.getMode().getSkillsTrained().length;
		for (int i : attackStyle.getMode().getSkillsTrained()) {
			player.getSkill().addExp(i, expPerSkill);
		}
		player.getSkill().addExp(Skill.HITPOINTS, (totalExp / 3d));
	}

	public boolean canAddXp() {
		if (victim.isNpc()) {
			if (victim.isBarricade()) {
				return false;
			}
		}
		return true;
	}

	public boolean shouldExecute() {
		return hitDelay <= 0;
	}

	public boolean canDamageEnemy() {
		return (Boolean) victim.getAttributes().get("canTakeDamage");
	}

	public Entity getVictim() {
		return victim;
	}

	public Entity getAttacker() {
		return attacker;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	@SuppressWarnings("rawtypes")
	private void executeAccuracy() {
		doBarrowsEffects();
		doSlayerEffects();
		if (canHit()) {
			double defence = CombatCalculations.getDefenceRoll(victim, hitDef);
			double accuracy = CombatCalculations.getAttackRoll(attacker, hitDef);
			double chance = CombatCalculations.getChance(attacker, accuracy, defence);
			doDebugMessages(chance);
			hit = determineHit(chance);
		}
		if (!hit && !hitDef.isUnblockable()) {
			if (hitDef.getAttackStyle() != null && hitDef.getAttackStyle().getAttackType() == AttackType.MAGIC) {
				hitDef.setHitGraphic(new Graphic(85, 100));
			}
			hitDef.removeEffects();
		} else {
			if (hitDef.getEffects() != null && hitDef.getEffects().size() > 0) {
				for (Effect effect : hitDef.getEffects()) {
					if (victim != null && victim.canAddEffect(effect)) {
						effect.initialize(this);
					}
				}
			}
		}
	}
	
	private void doInitializeHitChecks() {
		try {
			boolean npcIsAttacker = attacker != null && attacker.isNpc(), npcIsVictim = victim.isNpc();
			boolean playerIsAttacker = attacker != null && attacker.isPlayer(), playerIsVictim = victim.isPlayer();
			Player player = playerIsAttacker ? ((Player) attacker) : playerIsVictim ? ((Player) victim) : null;
			Npc npc = npcIsAttacker ? ((Npc) attacker) : npcIsVictim ? ((Npc) victim) : null;
			if (npcIsVictim) {
				int id = npc.getNpcId();
				switch (id) {
					case 879: //Delrith
						DemonSlayer.sendDelrithMessages();
						break;
					case 4479: //Goblin green
						if (npcIsAttacker && ((Npc) attacker).getNpcId() == 4486) {
							GoblinDiplomacy.sendGreenMessages((Npc) attacker);
						}
						break;
					case 4486: //Goblin red
						if (npcIsAttacker && ((Npc) attacker).getNpcId() == 4479) {
							GoblinDiplomacy.sendRedMessages((Npc) attacker);
						}
						break;
					case 2745:
						if (playerIsAttacker && npc.getCurrentHp() < (npc.getMaxHp() / 2) && !player.hurkotsSpawned()) {
							FightCaves.spawnYtHurkots(player);
							player.setHurkotsSpawned(true);
						}
						break;
					case RecruitmentDrive.SIR_LEYE:
						if (playerIsAttacker && (victim.getCurrentHp() - damage) <= 0 && player.getGender() != 1) {
							npc.getUpdateFlags().sendForceMessage("Fool! No man can defeat me!");
							damage = 0;
						}
						break;
					default:
						break;
				}
				if (npc.inFightCaves() && npc.getNpcId() != 2745 && hitDef != null && hitDef.getAttackStyle() != null && hitDef.getAttackStyle().getAttackType().equals(AttackType.MELEE)) {
					FightCaves.handlePlayerHit(attacker, (Npc) victim, damage);
				}
			}
			if (attacker != null && npcIsAttacker) {
				if (npc.getNpcId() >= 1338 && npc.getNpcId() < 1344) { //Weak Dagannoths 
					if (80 >= (new Random().nextDouble() * 100.0)) {
						damage = 0;
					}
				}
			}
			if (attacker != null && playerIsAttacker) {
				if (hitDef != null && hitDef.getAttackStyle() != null && hitDef.getAttackStyle().getAttackType().equals(AttackType.RANGED)) {
					doBoltSpecials();
				}
				if (SpellAttack.isMultiAncientSpell(hitDef, attacker, victim)) {
					SpellAttack.doMultiAncientSpell(hitDef, attacker, victim);
				}
				if (player.getArmorPiercedEntity() != null && player.getArmorPiercedEntity().equals(victim)) {
					damage *= 1.1;
				}
				if (hitDef.getHitGraphic() != null && player.getEquipment().getId(Constants.HANDS) == FamilyCrest.CHAOS_GAUNTLETS) {
					int gfxId = hitDef.getHitGraphic().getId();
					if (gfxId == 119 || gfxId == 122 || gfxId == 125 || gfxId == 128) {
						damage += 4;
					}
				}
				if (playerIsVictim) {
					if (attacker.inCwGame() && victim.inCwGame()) {
						Player playerA = (Player) attacker;
						Player playerV = (Player) victim;
						if (playerA.wearingCwBracelet() && playerV.carryingCwBanner()) {
							damage *= 1.2;
						}
					}
				}
			}
			if (playerIsVictim) {
				if (hitDef != null && hitDef.getAttackStyle() != null && hitDef.getAttackStyle().getAttackType().equals(AttackType.MELEE)) {
					if (player.getQuestStage(40) >= 7 && player.getInventory().playerHasItem(Biohazard.PLAGUE_SAMPLE)) {
						player.getInventory().removeItem(new Item(Biohazard.PLAGUE_SAMPLE));
						player.getActionSender().sendMessage("The fragile plague sample breaks upon you being hit!");
					}
				}
			}
			if (attacker != null && playerIsAttacker && npcIsVictim) {
				int id = npc.getNpcId();
				switch (id) {
					case FamilyCrest.CHRONOZON:
						if (hitDef.getHitGraphic() != null) {
							FamilyCrest.handleChronozonSpellHit(player, hitDef.getHitGraphic().getId());
						}
						break;
					case 205: //Salarin the Twisted
						if (hitDef != null) {
							if (hitDef.getAttackStyle() != null && (hitDef.getAttackStyle().getAttackType().equals(AttackType.RANGED) || hitDef.getAttackStyle().getAttackType().equals(AttackType.MELEE))) {
								damage = 0;
								player.getActionSender().sendMessage("Salarin the Twisted resists your attack!", true);
							} else {
								if (hitDef.getHitGraphic() != null) {
									int gfxId = hitDef.getHitGraphic().getId();
									if (gfxId != 92 && gfxId != 95 && gfxId != 98 && gfxId != 101 && gfxId != 76) {
										damage = 0;
										((Player) attacker).getActionSender().sendMessage("Salarin the Twisted resists your attack!", true);
									}
								}
							}
						}
						break;
					case 1459:
					case 1460: //Monkey Temple Guards
						if (npc.onApeAtoll()) {
							if (npc.getCurrentHp() < (npc.getMaxHp() / 6) && Misc.random(2) == 1) {
								npc.getUpdateFlags().sendAnimation(1405);
								hitDef.setVictimAnimation(6969);
								npc.setCurrentHp(npc.getCurrentHp() + 25);
							}
						}
						break;
					case 277: //Fire Warrior of Lezarkus
						if (hitDef.getAttackStyle().getAttackType() == AttackType.RANGED && player.getEquipment().getId(Constants.ARROWS) == 78) {
							//I'll allow it.
						} else {
							damage = 0;
							player.getActionSender().sendMessage("The fire warrior seems immune to your hit!");
						}
						break;
					case 272: //Lucien
						if (npc.getCurrentHp() - damage <= 0) {
							damage = 0;
							CombatManager.resetCombat(player);
							CombatManager.resetCombat(npc);
							npc.heal(npc.getMaxHp());
							if (player.getQuestStage(47) == 7) {
								Dialogues.startDialogue(player, 20272);
							}
							return;
						}
						break;
					case VampireSlayer.COUNT_DRAYNOR:
						if (player.getInventory().playerHasItem(VampireSlayer.GARLIC)) {
							damage = damage / 6;
						}
						break;
					default:
						break;
				}
				if (id >= 1351 && id < 1357 && hitDef != null && hitDef.getHitGraphic() != null) {
					if (!HorrorFromTheDeep.canDamageMother(hitDef, npc.getTransformId(), hitDef.getHitGraphic().getId())) {
						damage = 0;
					}
				}
				if (player.getEquipment().getId(Constants.AMULET) == 11128 && player.getEquipment().getItemContainer().get(Constants.WEAPON) != null) {
					int weaponId = player.getEquipment().getId(Constants.WEAPON);
					if (weaponId >= 6522 && weaponId < 6529) {
						damage += damage / 4;
					}
				}
				if (Constants.DEGRADING_ENABLED) {
					Degrading.handleHit(player, true);
				}
			}
			if (attacker != null && playerIsVictim && npcIsAttacker) {
				int id = npc.getNpcId();
				switch (id) {
					case 3200: //Chaos Elemental
						int chance = Misc.random(2);
						switch (chance) {
							case 0:
								if (player.isProtectingFromCombat(AttackType.MAGIC, attacker)) {
									damage = 0;
								}
								break;
							case 1:
								if (player.isProtectingFromCombat(AttackType.RANGED, attacker)) {
									damage = 0;
								}
								break;
							case 2:
								if (player.isProtectingFromCombat(AttackType.MELEE, attacker)) {
									damage = 0;
								}
								break;
						}
						break;

					default:
						break;
				}
				if (Constants.DEGRADING_ENABLED) {
					Degrading.handleHit(player, false);
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR DOING CHECKS ON HIT INITIALIZE");
			e.printStackTrace();
		}
	}
	
	private void doExecuteHitChecks() {
		try {
			boolean npcIsAttacker = attacker != null && attacker.isNpc(), npcIsVictim = victim.isNpc();
			boolean playerIsAttacker = attacker != null && attacker.isPlayer(), playerIsVictim = victim.isPlayer();
			Player player = playerIsAttacker ? ((Player) attacker) : playerIsVictim ? ((Player) victim) : null;
			Npc npc = npcIsAttacker ? ((Npc) attacker) : npcIsVictim ? ((Npc) victim) : null;
			if (attacker != null && npcIsAttacker && npcIsVictim) {
				Npc attacker = (Npc) this.attacker;
				Npc victim = (Npc) this.victim;
				if (attacker.getNpcId() >= 1412 && attacker.getNpcId() <= 1426) { //Monkey Madness
					hitDef.setVictimAnimation(6969);
					if (victim.getNpcId() == 1472) {
						damage = damage > 1 ? 1 : Misc.random(1) == 1 ? 0 : damage;
					} else {
						damage = Misc.random(3) == 1 ? 1 : damage;
					}
					if (victim.getNpcId() == MonkeyMadness.JUNGLE_DEMON && (victim.getCurrentHp() - damage) <= 0) {
						victim.setCurrentHp(victim.getMaxHp());
					}
				}
			}
			if (attacker != null && npcIsAttacker && playerIsVictim) {
				int id = npc.getNpcId();
				if (npc.getDefinition().getName().equals("Spinolyp") && hitDef.getEffects() != null && !hitDef.getEffects().isEmpty() && hitDef.getEffects().get(0) != null && hitDef.getEffects().get(0).equals(new StatEffect(5, 0)) && damage > 0) {
					((Player) victim).getActionSender().statEdit(5, -1, false);
				}
				switch (id) {
					case 2882:
						Position pos = player.getPosition();
						for (Player p : World.getPlayers()) {
							if (p != null && Misc.goodDistance(pos, p.getPosition(), 1) && !p.isProtectingFromCombat(AttackType.MAGIC, attacker) && p != victim) {
								p.hit(Misc.random(61), HitType.NORMAL);
							}
						}
						break;
					case NatureSpirit.GHAST:
						NatureSpirit.handleSpoilFood(npc, player);
						break;
				}
			}
			if (attacker != null && npcIsVictim && playerIsAttacker) {
				if (player.getSlayer().needToFinishOffMonster(((Npc) victim), true)) {
					if (damage >= victim.getCurrentHp()) {
						damage = victim.getCurrentHp() - 1;
					}
				}
			}
			if (playerIsVictim) {
				if (player.isHomeTeleporting()) {
					player.setHomeTeleporting(false);
				}
				if (hit) {
					RingEffect.ringOfRecoil(attacker, player, damage);
					FightCaves.handleNpcHit(attacker, player, damage);
				}
				int currentHp = player.getCurrentHp();
				if (currentHp > 0) {
					int saveHp = (int) Math.ceil(victim.getMaxHp() * .1);
					if (currentHp < saveHp) {
						if (player.getIsUsingPrayer()[PrayerData.REDEMPTION.getIndex()]) {
							Prayer.applyRedemption(player, victim, currentHp);
						}
						if (!player.inMiniGameArea()) {
							RingEffect.ringOfLife(player);
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR DOING CHECKS ON HIT EXECUTE");
			e.printStackTrace();
		}
	}
	
	private boolean victimCanBeHit() {
		try {
			if (Constants.DDOS_PROTECT_MODE) {
				if (victim.isPlayer()) {
					damage = 0;
					return false;
				}
			}
			if (victim.isPlayer()) {
				Player player = (Player) victim;
				if (player.getStaffRights() >= 3) {
					return false;
				} else if (attacker != null && attacker.isNpc() && player.getMMVars().inProcessOfBeingJailed && player.onApeAtoll() && ((Npc) attacker).getNpcId() != 1457) {
					return false;
				} else if (MinigameAreas.isInArea(victim.getPosition(), ApeAtoll.JAIL) && (damage > 8 || (victim.getCurrentHp() - damage) <= 0)) {
					return false;
				}
			} else if (victim.isNpc()) {
				Npc npc = (Npc) victim;
				if (hitDef.getHitType() == HitType.POISON) {
					if (npc.getDefinition() != null && npc.getDefinition().isPoisonImmune()) {
						return false;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR CHECKING IF VICTIM CAN BE HIT");
			e.printStackTrace();
		}
		return true;
	}
	
	private void doProtectionChecks() {
		try {
			if (hitDef.getAttackStyle().getMode() == AttackStyle.Mode.DRAGONFIRE || hitDef.getAttackStyle().getMode() == AttackStyle.Mode.DRAGONFIRE_FAR || (attacker.isNpc() && ((Npc) attacker).getNpcId() == 50 && hitDef.getAttackStyle().getMode() != AttackStyle.Mode.MELEE_ACCURATE) && victim.isPlayer()) {
				boolean isSpecialKBDBreath = attacker.isNpc() && ((Npc) attacker).getNpcId() == 50 && hitDef.getAttackStyle().getMode() != AttackStyle.Mode.MELEE_ACCURATE && hitDef.getAttackStyle().getMode() != AttackStyle.Mode.DRAGONFIRE && hitDef.getAttackStyle().getMode() != AttackStyle.Mode.DRAGONFIRE_FAR;
				Player player = (Player) victim;
				switch (player.antiFire()) {
					case 2:
						damage = 0;
						if (isSpecialKBDBreath) {
							damage = Misc.random(10);
						}
						break;
					case 1:
						if (attacker != null && attacker.isNpc() && !((Npc) attacker).getDefinition().getName().toLowerCase().contains("wyvern")) {
							player.getActionSender().sendMessage("You manage to resist some of the dragonfire.");
							if (player.getEquipment().getId(Constants.SHIELD) == 11284) {
								player.getUpdateFlags().sendAnimation(6695);
								player.getUpdateFlags().sendGraphic(new Graphic(1164, 0));
								if (player.getDfsCharges() < 49) {
									player.setDfsCharges(player.getDfsCharges() + 1);
								} else if (player.getDfsCharges() == 49) {
									player.setDfsCharges(50);
									player.getActionSender().sendMessage("Your Dragonfire shield is fully charged.");
									player.getEquipment().replaceEquipment(11283, Constants.SHIELD);
								}
								player.getEquipment().sendBonus(player);
							}
							damage = Misc.random(hitDef.getAttackStyle().getMode() == AttackStyle.Mode.DRAGONFIRE_FAR ? 8 : 4);
							if (((Npc) attacker).getNpcId() == 50) {
								damage = Misc.random(15);
								if (isSpecialKBDBreath) {
									damage = Misc.random(10); //KBD's 3 special breaths
								}
							}
							break;
						} else {
							HitDef hitDefFreeze = new HitDef(null, HitType.NORMAL, 0);
							Hit freeze = new Hit(player, player, hitDefFreeze);
							BindingEffect bind = new BindingEffect(12);
							bind.initialize(freeze);
							player.getActionSender().sendMessage("You manage to resist most of the icy breath, but are frozen in place.");
							damage = Misc.random(10);
							break;
						}
					default:
						if (attacker.isNpc() && !((Npc) attacker).getDefinition().getName().toLowerCase().contains("wyvern")) {
							player.getActionSender().sendMessage("You are horribly burned by the dragonfire!");
							damage = 30 + Misc.random(20);
							break;
						} else if (attacker.isNpc() && ((Npc) attacker).getNpcId() == 50) {
							player.getActionSender().sendMessage("You are horribly burned by the dragonfire!");
							damage = 45 + Misc.random(20);
							break;
						} else {
							if (player.getEquipment().getId(Constants.SHIELD) != 2890) {
								player.getActionSender().sendMessage("You are horribly burned by the ice breath!");
								damage = 30 + Misc.random(20);
								break;
							} else {
								HitDef hitDefFreeze = new HitDef(null, HitType.NORMAL, 0);
								Hit freeze = new Hit(player, player, hitDefFreeze);
								BindingEffect bind = new BindingEffect(12);
								bind.initialize(freeze);
								player.getActionSender().sendMessage("You magically resist most of the icy breath, but are frozen in place.");
								damage = Misc.random(10);
								break;
							}
						}
				}
				if (damage > 10 && hitDef.getAttackStyle().getMode() == AttackStyle.Mode.DRAGONFIRE && victim.isProtectingFromCombat(hitDef.getAttackStyle().getAttackType(), attacker)) {
					player.getActionSender().sendMessage("Your prayers manage to resist some of the dragonfire.");
					damage = Misc.random(10);
				}
			} else if (victim.isProtectingFromCombat(hitDef.getAttackStyle().getAttackType(), attacker) && hitDef.getSpecialEffect() != 11) {
				if (attacker.isPlayer()) {
					damage = (int) Math.ceil(damage * 0.6);
				} else if (attacker.isNpc() && ((Npc) attacker).getNpcId() == 3200) {

				} else {
					damage = 0;
				}
			} else if (attacker != null && attacker.isPlayer() && victim.isNpc() && ((Npc) victim).getNpcId() == 2881) { //Supreme
				if (hitDef.getAttackStyle().getAttackType() == AttackType.RANGED || hitDef.getAttackStyle().getAttackType() == AttackType.MAGIC) {
					if (Misc.random(2) == 1) {
						damage = 0;
					} else {
						damage /= 10;
					}
				}
			} else if (attacker != null && attacker.isPlayer() && victim.isNpc() && ((Npc) victim).getNpcId() == 2882) { //Prime
				if (hitDef.getAttackStyle().getAttackType() == AttackType.MELEE || hitDef.getAttackStyle().getAttackType() == AttackType.MAGIC) {
					if (Misc.random(2) == 1) {
						damage = 0;
					} else {
						damage /= 10;
					}
				}
			} else if (attacker != null && attacker.isPlayer() && victim.isNpc() && ((Npc) victim).getNpcId() == 2883) { //Rex
				if (hitDef.getAttackStyle().getAttackType() == AttackType.MELEE || hitDef.getAttackStyle().getAttackType() == AttackType.RANGED) {
					if (Misc.random(2) == 1) {
						damage = 0;
					} else {
						damage /= 10;
					}
				}
			} else if (attacker != null && attacker.isPlayer() && victim.isNpc() && ((Npc) victim).getNpcId() == 1158) {
				if (hitDef.getAttackStyle().getAttackType() == AttackType.RANGED || hitDef.getAttackStyle().getAttackType() == AttackType.MAGIC) {
					damage = 0;
				}
			} else if (attacker != null && attacker.isPlayer() && ((Player) attacker).getStaffRights() < 1 && victim.isNpc() && ((Npc) victim).getNpcId() == 1160) {
				if (hitDef.getAttackStyle().getAttackType() == AttackType.MELEE) {
					if (((Player) attacker).getEquipment().fullVerac() && Misc.random(100) > 50) {
						//Do nothing what a madman
					} else {
						damage = 0;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR DOING PROTECTION CHECKS");
			e.printStackTrace();
		}
	}

	private void statLowering(double statLow, double statHigh) {
		Player o = (Player) victim;
		int[] loweredStatHigh = {Skill.ATTACK, Skill.STRENGTH, Skill.RANGED, Skill.MAGIC};
		int[] loweredStatLow = {Skill.AGILITY, Skill.PRAYER, Skill.DEFENCE};
		for (int i = 0; i < loweredStatLow.length; i++) {
			o.getActionSender().statEdit(loweredStatLow[i], (int) ((o.getSkill().getLevel()[loweredStatLow[i]]) * statLow), false);
		}
		for (int i = 0; i < loweredStatHigh.length; i++) {
			o.getActionSender().statEdit(loweredStatHigh[i], (int) ((o.getSkill().getLevel()[loweredStatHigh[i]]) * statHigh), false);
		}
	}
	
	private void doBoltSpecials() {
		try {
			Player player = (Player) attacker;
			boolean noWeapon = player.getEquipment().getItemContainer().get(Constants.WEAPON) == null;
			boolean noCrossbow = player.getEquipment().getItemContainer().get(Constants.WEAPON) != null && !player.getEquipment().getItemContainer().get(Constants.WEAPON).getDefinition().getName().contains("c'bow") && !player.getEquipment().getItemContainer().get(Constants.WEAPON).getDefinition().getName().contains("crossbow");
			EnchantedBolts.BoltSpecials b = EnchantedBolts.BoltSpecials.forBoltId(player.getEquipment().getId(Constants.ARROWS));
			if (b != null && !noWeapon && !noCrossbow) {
				if ((b.getProcChance() >= new Random().nextDouble() * 100) && damage != 0 && hitDef.getHitType() != HitType.MISS) {
					if (EnchantedBolts.activateSpecial(b, player, victim, damage)) {
						if (!b.equals(EnchantedBolts.BoltSpecials.RUBY)) {
							damage *= b.getIncreasedDamage();
						}
						if (b.equals(EnchantedBolts.BoltSpecials.PEARL) && victim.isNpc() && EnchantedBolts.fireNpc((Npc) victim)) {
							damage *= 1.1;
						} else if (b.equals(EnchantedBolts.BoltSpecials.RUBY)) {
							damage = (int) Math.ceil(victim.getCurrentHp() / 5);
						} else if (b.equals(EnchantedBolts.BoltSpecials.DRAGONSTONE)) {
							damage += 20;
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR DOING BOLT SPECIALS");
			e.printStackTrace();
		}
	}

	private void doBarrowsEffects() {
		try {
			if (attacker != null && Misc.random(4) == 0) {
				if (attacker.isPlayer()) {
					Player player = ((Player) attacker);
					boolean[] bools = {player.hasFullGuthan(), player.hasFullTorag(), player.hasFullAhrim(), player.hasFullKaril(), player.hasFullVerac()};
					for (int i = 0; i < bools.length; i++) {
						if (bools[i]) {
							hitDef.setSpecialEffect(7 + i);
							break;
						}
					}
				} else if (attacker.isNpc()) {
					Npc npc = ((Npc) attacker);
					int[] ids = {2027, 2029, 2025, 2028, 2030};
					for (int i = 0; i < ids.length; i++) {
						if (npc.getNpcId() == ids[i]) {
							hitDef.setSpecialEffect(7 + i);
							break;
						}
					}
					if (npc.getNpcId() == 2026) {
						double hpLost = attacker.getMaxHp() - attacker.getCurrentHp();
						this.damage += damage * hpLost * 0.01;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR DOING BARROWS EFFECTS");
			e.printStackTrace();
		}
	}

	private void doSlayerEffects() {
		try {
			if (attacker.isNpc() && victim.isPlayer()) {
				Npc npc = (Npc) attacker;
				Player player = (Player) victim;
				if (!player.getSlayer().hasSlayerRequirement(npc)) {
					String name = npc.getDefinition().getName().toLowerCase();
					hitDef.setUnblockable(true);
					switch (name) {
						case "banshee":
							damage = 8;
							statLowering(-0.1, -0.2);
							break;
						case "cockatrice":
							damage = 11;
							statLowering(-0.5, -0.75);
							break;
						case "basilisk":
							damage = 12;
							statLowering(-0.5, -0.75);
							break;
						case "wall beast":
							damage = 18;
							hitDef.addEffect(new StunEffect(5)).setVictimAnimation(734);
							break;
						case "aberrant spectre":
							damage = 14;
							statLowering(-0.4, -0.8);
							break;
						case "dust devil":
							damage = 14;
							break;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR DOING SLAYER EFFECTS");
			e.printStackTrace();
		}
	}

	private void doDebugMessages(double chance) {
		if (attacker.isPlayer() && ((Player) attacker).isDebugCombat()) {
			((Player) attacker).getActionSender().sendMessage("Chance to hit: " + (int) (chance * 100) + "% (Rounded)");
		}
		if (victim.isPlayer() && ((Player) victim).isDebugCombat() && attacker.isNpc()) {
			((Player) victim).getActionSender().sendMessage("Chance of npc hitting you: " + (int) (chance * 100) + "% (Rounded)");
		}
		if (victim.isPlayer() && ((Player) victim).isDebugCombat() && attacker.isPlayer()) {
			((Player) victim).getActionSender().sendMessage("Chance of player hitting you: " + (int) (chance * 100) + "% (Rounded)");
		}
	}

	private boolean determineHit(double chance) {
		if (attacker != null && attacker.isPlayer() && ((Player) attacker).getBonus(3) <= -20 && hitDef.getAttackStyle().getAttackType() == AttackType.MAGIC) {
			hit = false;
		} else if (attacker != null && attacker.isNpc() && victim != null && victim.isNpc() && ((Npc) victim).getNpcId() == 1472) {
			hit = true;
		} else if (attacker != null && attacker.isNpc() && victim != null && victim.isNpc() && attacker.onApeAtoll()) {
			hit = true;
		} else {
			hit = CombatCalculations.isAccurateHit(chance);
		}
		return hit;
	}

	private boolean canHit() {
		hit = true;
		try {
			if (victim.isPlayer() && ((Player) victim).getNewComersSide().isInTutorialIslandStage()) {
				if (((Player) victim).getSkill().getLevel()[Skill.HITPOINTS] == 1) {
					hit = false;
				}
			}
			if (attacker.isPlayer() && victim.isNpc()) {
				if (!((Player) attacker).getSlayer().hasSlayerRequirement((Npc) victim)) {
					hit = false;
				}
			}
			if ((attacker.isPlayer() && ((Player) attacker).getNewComersSide().getTutorialIslandStage() == 63)) {
				((Player) attacker).getNewComersSide().setTutorialIslandStage(((Player) attacker).getNewComersSide().getTutorialIslandStage() + 1, true);
				hit = false;
			}
		} catch (Exception e) {
			System.out.println("ERROR CHECKING IF ATTACKER CAN HIT");
			e.printStackTrace();
		}
		return hit;
	}
}
