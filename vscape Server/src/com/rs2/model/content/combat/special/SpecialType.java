package com.rs2.model.content.combat.special;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.Graphic;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.Following;
import com.rs2.model.content.combat.CombatCycleEvent;
import com.rs2.model.content.combat.CombatCycleEvent.CanAttackResponse;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.combat.attacks.WeaponAttack;
import com.rs2.model.content.combat.hit.Hit;
import com.rs2.model.content.combat.hit.HitDef;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.combat.projectile.ProjectileDef;
import com.rs2.model.content.combat.projectile.ProjectileTrajectory;
import com.rs2.model.content.combat.weapon.RangedAmmo;
import com.rs2.model.content.combat.weapon.RangedAmmoType;
import com.rs2.model.content.combat.weapon.Weapon;
import com.rs2.model.content.minigames.duelarena.RulesData;
import com.rs2.model.content.minigames.pestcontrol.PestControl;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.prayer.Prayer.PrayerData;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;
import com.rs2.util.requirement.EquipmentRequirement;
import com.rs2.util.requirement.Requirement;

/**
 *
 */
public enum SpecialType {

	DRAGON_DAGGER(25, "drag dagger", "dragon dagger") {
		@Override
		public WeaponAttack getSpecialAttack(final Player attacker, final Entity victim, Weapon weapon) {
			WeaponAttack weaponAttack = new WeaponAttack(attacker, victim, weapon) {
				@Override
				public boolean canInitialize() {
					if (!super.canInitialize())
						return false;
					setAnimation(1062);
					setGraphic(new Graphic(252, 100));
					double maxDamage = generateMaxHit();
					setHits(new HitDef[]{new HitDef(getAttackStyle(), HitType.NORMAL, maxDamage * 1.15).randomizeDamage().applyAccuracy(1.25), new HitDef(getAttackStyle(), HitType.NORMAL, maxDamage * 1.15).randomizeDamage().applyAccuracy(1.25)});
					return true;
				}
			};
			return weaponAttack;
		}
	},
	ABYSSAL_WHIP(50) {
		@Override
		public WeaponAttack getSpecialAttack(final Player attacker, final Entity victim, Weapon weapon) {
			final WeaponAttack weaponAttack = new WeaponAttack(attacker, victim, weapon) {
				@Override
				public boolean canInitialize() {
					if (!super.canInitialize())
						return false;
					double maxDamage = generateMaxHit();
					if (Misc.random(1) == 1)
						maxDamage = 0;
					setHits(new HitDef[]{new HitDef(getAttackStyle(), HitType.NORMAL, maxDamage).setHitGraphic(new Graphic(341, 100)).setUnblockable(true)});
					return true;
				}
			};
			CombatManager.attack(victim, attacker);
			return weaponAttack;
		}
	},
	DRAGON_LONGSWORD(25) {
		@Override
		public WeaponAttack getSpecialAttack(final Player attacker, final Entity victim, Weapon weapon) {
			final WeaponAttack weaponAttack = new WeaponAttack(attacker, victim, weapon) {
				@Override
				public boolean canInitialize() {
					if (!super.canInitialize())
						return false;
					setAnimation(1058);
					setGraphic(new Graphic(248, 100));
					double maxDamage = generateMaxHit();
					setHits(new HitDef[]{new HitDef(getAttackStyle(), HitType.NORMAL, maxDamage * 1.25).randomizeDamage().applyAccuracy(1.25)});
					return true;
				}
			};
			return weaponAttack;
		}
	},
	DRAGON_SCIMITAR(55) {
		@Override
		public WeaponAttack getSpecialAttack(final Player attacker, final Entity victim, Weapon weapon) {
			final WeaponAttack weaponAttack = new WeaponAttack(attacker, victim, weapon) {
				@Override
				public boolean canInitialize() {
					if (!super.canInitialize())
						return false;
					setAnimation(1872);
					setGraphic(new Graphic(347, 100));
					double maxDamage = generateMaxHit();
					setHits(new HitDef[]{new HitDef(getAttackStyle(), HitType.NORMAL, maxDamage).randomizeDamage().setSpecialEffect(2).setCheckAccuracy(true)});//.addEffects(new Effect[]{new DisableProtectionEffect(5000)})});
					return true;
				}
			};
			return weaponAttack;
		}
	},
	RUNE_CLAWS(25) {
		@Override
		public WeaponAttack getSpecialAttack(final Player attacker, final Entity victim, Weapon weapon) {
			final WeaponAttack weaponAttack = new WeaponAttack(attacker, victim, weapon) {
				@Override
				public boolean canInitialize() {
					if (!super.canInitialize())
						return false;
					setAnimation(923);
					setGraphic(new Graphic(274, 100));
					double maxDamage = generateMaxHit();
					setHits(new HitDef[]{new HitDef(getAttackStyle(), HitType.NORMAL, maxDamage * 1.1).randomizeDamage().applyAccuracy(1.1)});
					return true;
				}
			};
			return weaponAttack;
		}
	},
	DRAGON_MACE(25) {
		@Override
		public WeaponAttack getSpecialAttack(final Player attacker, final Entity victim, Weapon weapon) {
			final WeaponAttack weaponAttack = new WeaponAttack(attacker, victim, weapon) {
				@Override
				public boolean canInitialize() {
					if (!super.canInitialize())
						return false;
					setAnimation(1060);
					setGraphic(new Graphic(251, 100));
					double maxDamage = generateMaxHit();
					setHits(new HitDef[]{new HitDef(getAttackStyle(), HitType.NORMAL, maxDamage * 1.45).randomizeDamage().applyAccuracy(0.9)});
					return true;
				}
			};
			return weaponAttack;
		}
	},
	DRAGON_AXE(100) {
		@Override
		public WeaponAttack getSpecialAttack(final Player attacker, final Entity victim, Weapon weapon) {
			final WeaponAttack weaponAttack = new WeaponAttack(attacker, victim, weapon) {
				@Override
				public boolean canInitialize() {
					if (!super.canInitialize())
						return false;
					setAnimation(2876);
					setGraphic(new Graphic(479, 100));
					attacker.getUpdateFlags().setForceChatMessage("Time to get wood!");
					attacker.getActionSender().statEdit(Skill.WOODCUTTING, 3, true);
					double maxDamage = generateMaxHit();
					setHits(new HitDef[]{new HitDef(getAttackStyle(), HitType.NORMAL, maxDamage).randomizeDamage().setSpecialEffect(3).setCheckAccuracy(true)});
					return true;
				}
			};
			return weaponAttack;
		}
	},
	DARKLIGHT(50) {
		@Override
		public WeaponAttack getSpecialAttack(final Player attacker, final Entity victim, Weapon weapon) {
			final WeaponAttack weaponAttack = new WeaponAttack(attacker, victim, weapon) {
				@Override
				public boolean canInitialize() {
					if (!super.canInitialize())
						return false;
					setAnimation(2890);
					setGraphic(new Graphic(483, 100));
					double maxDamage = generateMaxHit();
					setHits(new HitDef[]{new HitDef(getAttackStyle(), HitType.NORMAL, maxDamage).randomizeDamage().setSpecialEffect(4).setCheckAccuracy(true)});
					return true;
				}
			};
			return weaponAttack;
		}
	},
	DRAGON_SPEAR(25) {
		@Override
		public WeaponAttack getSpecialAttack(final Player attacker, final Entity victim, Weapon weapon) {
			final WeaponAttack weaponAttack = new WeaponAttack(attacker, victim, weapon) {
				@Override
				public boolean canInitialize() {
					if (!super.canInitialize())
						return false;
					setAnimation(1064);
					setGraphic(new Graphic(253, 100));
					setHits(new HitDef[]{new HitDef(getAttackStyle(), HitType.NORMAL, 0).setSpecialEffect(5).setUnblockable(true)});
					return true;
				}
			};
			return weaponAttack;
		}
	},
	DRAGON_HALBERD(30) {
		@Override
		public WeaponAttack getSpecialAttack(final Player attacker, final Entity victim, Weapon weapon) {
			final WeaponAttack weaponAttack = new WeaponAttack(attacker, victim, weapon) {
				@Override
				public boolean canInitialize() {
					if (!super.canInitialize())
						return false;
					setAnimation(1203);
					setGraphic(new Graphic(282, 100));
					double maxDamage = generateMaxHit();
					if (victim.getSize() > 1) {
						setHits(new HitDef[]{new HitDef(getAttackStyle(), HitType.NORMAL, maxDamage * 1.1).randomizeDamage().setCheckAccuracy(true), new HitDef(getAttackStyle(), HitType.NORMAL, maxDamage * 1.1).randomizeDamage().setCheckAccuracy(true)});
					} else {
						setHits(new HitDef[]{new HitDef(getAttackStyle(), HitType.NORMAL, maxDamage * 1.1).randomizeDamage().setCheckAccuracy(true)});
					}
					return true;
				}
			};
			return weaponAttack;
		}
	},
	DRAGON_2H_SWORD(60) {
		@Override
		public WeaponAttack getSpecialAttack(final Player attacker, final Entity victim, final Weapon weapon) {
			final WeaponAttack weaponAttack = new WeaponAttack(attacker, victim, weapon) {
				@Override
				public boolean canInitialize() {
					if (!super.canInitialize())
						return false;
					setAnimation(3157);
					setGraphic(new Graphic(559, 100));
					double maxDamage = generateMaxHit();
					setHits(new HitDef[]{new HitDef(getAttackStyle(), HitType.NORMAL, maxDamage).randomizeDamage().setSpecialEffect(6).setStartingHitDelay(1).setCheckAccuracy(true)});
					return true;
				}
			};
			return weaponAttack;
		}
	},
	SEERCULL(100) {
		@Override
		public WeaponAttack getSpecialAttack(final Player attacker, final Entity victim, final Weapon weapon) {
			final WeaponAttack weaponAttack = new WeaponAttack(attacker, victim, weapon) {
				@Override
				public boolean canInitialize() {
					if (!super.canInitialize())
						return false;
					RangedAmmo rangedAmmo = getRangedAmmo();
					RangedAmmoType rangedAmmoType = weapon.getAmmoType();
					// shouldn't ever happen, but still going to check
					if (rangedAmmo == null || rangedAmmoType == null) {
						return false;
					}
					Item arrowItem = attacker.isPlayer() ? (attacker).getEquipment().getItemContainer().get(rangedAmmoType.getEquipmentSlot()) : null;
					if (arrowItem == null)
						return false;
					setRequirements(new Requirement[]{new EquipmentRequirement(rangedAmmoType.getEquipmentSlot(), arrowItem.getId(), 2, true) {
						@Override
						public String getFailMessage() {
							return "You do not have enough arrows!";
						}
					}});
					double maxDamage = generateMaxHit();
					setAnimation(426);
					setGraphic(new Graphic(472, 100));
					ProjectileTrajectory projectileTrajectory = rangedAmmoType.getProjectileTrajectory();
					ProjectileDef projectile = new ProjectileDef(473, projectileTrajectory.clone().setSlowness(3));
					setHits(new HitDef[]{new HitDef(getAttackStyle(), HitType.NORMAL, maxDamage).randomizeDamage().setProjectile(projectile).setStartingHitDelay(1).setSpecialEffect(1).setCheckAccuracy(true)});
					return true;
				}
			};
			return weaponAttack;
		}
	},
	MAGIC_SHORTBOW(50) {
		@Override
		public WeaponAttack getSpecialAttack(final Player attacker, final Entity victim, final Weapon weapon) {
			final WeaponAttack weaponAttack = new WeaponAttack(attacker, victim, weapon) {
				@Override
				public boolean canInitialize() {
					if (!super.canInitialize())
						return false;
					RangedAmmo rangedAmmo = getRangedAmmo();
					RangedAmmoType rangedAmmoType = weapon.getAmmoType();
					// shouldn't ever happen, but still going to check
					if (rangedAmmo == null || rangedAmmoType == null) {
						return false;
					}
					Item arrowItem = attacker.isPlayer() ? (attacker).getEquipment().getItemContainer().get(rangedAmmoType.getEquipmentSlot()) : null;
					if (arrowItem == null)
						return false;
					setRequirements(new Requirement[]{new EquipmentRequirement(rangedAmmoType.getEquipmentSlot(), arrowItem.getId(), 2, true) {
						@Override
						public String getFailMessage() {
							return "You do not have enough arrows!";
						}
					}});
					double maxDamage = generateMaxHit();
					setAnimation(1074);
					setGraphic(null);
					ProjectileTrajectory projectileTrajectory = rangedAmmoType.getProjectileTrajectory();
					ProjectileDef firstProjectile = new ProjectileDef(249, projectileTrajectory.clone().setDelay(20).setSlowness(3));
					ProjectileDef secondProjectile = new ProjectileDef(249, projectileTrajectory.clone().setDelay(50).setSlowness(2));
					setHits(new HitDef[]{new HitDef(getAttackStyle(), HitType.NORMAL, maxDamage).randomizeDamage().applyAccuracy(1.1).setProjectile(firstProjectile).setStartingHitDelay(1).setCheckAccuracy(true), new HitDef(getAttackStyle(), HitType.NORMAL, maxDamage).randomizeDamage().applyAccuracy(1.1).setProjectile(secondProjectile).setStartingHitDelay(0).setCheckAccuracy(true)});
					return true;
				}
				@Override
				public int execute(CycleEventContainer container) {
					World.getTickManager().submit(new Tick(1) {
						@Override
						public void execute() {
							attacker.getUpdateFlags().sendGraphic(new Graphic(256, 90).setDelay(15));
							stop();

						}
					});
					return super.execute(container) + 1;
				}
			};
			return weaponAttack;
		}
	},
	MAGIC_LONGBOW(35) {
		@Override
		public WeaponAttack getSpecialAttack(final Player attacker, final Entity victim, final Weapon weapon) {
			final WeaponAttack weaponAttack = new WeaponAttack(attacker, victim, weapon) {
				@Override
				public boolean canInitialize() {
					if (!super.canInitialize())
						return false;
					RangedAmmo rangedAmmo = getRangedAmmo();
					RangedAmmoType rangedAmmoType = weapon.getAmmoType();
					// shouldn't ever happen, but still going to check
					if (rangedAmmo == null || rangedAmmoType == null) {
						return false;
					}
					Item arrowItem = attacker.isPlayer() ? (attacker).getEquipment().getItemContainer().get(rangedAmmoType.getEquipmentSlot()) : null;
					if (arrowItem == null)
						return false;
					setRequirements(new Requirement[]{new EquipmentRequirement(rangedAmmoType.getEquipmentSlot(), arrowItem.getId(), 2, true) {
						@Override
						public String getFailMessage() {
							return "You do not have enough arrows!";
						}
					}});
					double maxDamage = generateMaxHit();
					setAnimation(426);
					setGraphic(new Graphic(250, 100));
					ProjectileTrajectory projectileTrajectory = rangedAmmoType.getProjectileTrajectory();
					ProjectileDef projectile = new ProjectileDef(249, projectileTrajectory.clone());
					setHits(new HitDef[]{new HitDef(getAttackStyle(), HitType.NORMAL, maxDamage).randomizeDamage().setProjectile(projectile).setStartingHitDelay(1).applyAccuracy(5).setCheckAccuracy(true)});
					return true;
				}
			};
			return weaponAttack;
		}
	},
	/** These special attacks are pre-defined and do not need to be set here **/
	DRAGON_BATTLEAXE(100) {
		@Override
		public WeaponAttack getSpecialAttack(Player attacker, Entity victim, Weapon weapon) {
			return null;
		}
	},
	EXCALIBUR(100) {
		@Override
		public WeaponAttack getSpecialAttack(Player attacker, Entity victim, Weapon weapon) {
			return null;
		}
	},
	GRANITE_MAUL(50) {
		@Override
		public WeaponAttack getSpecialAttack(Player attacker, Entity victim, Weapon weapon) {
			final WeaponAttack weaponAttack = new WeaponAttack(attacker, victim, weapon) {
				@Override
				public boolean canInitialize() {
					if (!super.canInitialize())
						return false;
					setAnimation(1667);
					setGraphic(new Graphic(337, 100));
					double maxDamage = generateMaxHit();
					setHits(new HitDef[]{new HitDef(getAttackStyle(), HitType.NORMAL, maxDamage).randomizeDamage().setCheckAccuracy(true)});
					return true;
				}
			};
			return weaponAttack;
		}
	},
	DARK_BOW(55) {
		@Override
		public WeaponAttack getSpecialAttack(final Player attacker, final Entity victim, final Weapon weapon) {
			final WeaponAttack weaponAttack = new WeaponAttack(attacker, victim, weapon) {
				@Override
				public boolean canInitialize() {
					//if (!super.canInitialize())
					//	return false;
					RangedAmmo rangedAmmo = getRangedAmmo();
					RangedAmmoType rangedAmmoType = weapon.getAmmoType();
					Item arrowItem = attacker.isPlayer() ? (attacker).getEquipment().getItemContainer().get(rangedAmmoType.getEquipmentSlot()) : null;
					if (arrowItem == null)
						return false;
					setRequirements(new Requirement[]{new EquipmentRequirement(rangedAmmoType.getEquipmentSlot(), arrowItem.getId(), 2, true) {
						@Override
						public String getFailMessage() {
							return "You do not have enough arrows!";
						}
					}});
					double maxDamage = generateMaxHit();
					setAnimation(426);
					setGraphic(new Graphic( attacker.getDarkBowPullGfx(rangedAmmo), 90) );
					if(rangedAmmo != RangedAmmo.DRAGON_ARROW) {
					    ProjectileDef firstProjectile = new ProjectileDef(1101, ProjectileTrajectory.DOUBLE_ARROW1.clone().setSlowness(30));
					    ProjectileDef secondProjectile = new ProjectileDef(1101, ProjectileTrajectory.DOUBLE_ARROW2.clone().setStartHeight(35).setSlowness(30));
					    setHits(new HitDef[]{new HitDef(getAttackStyle(), HitType.NORMAL, maxDamage*1.3).randomizeDamage().applyAccuracy(1.1).setProjectile(firstProjectile).setStartingHitDelay(1).setCheckAccuracy(true).setHitGraphic(new Graphic(1103, 90)).setDarkBowSpec(true), new HitDef(getAttackStyle(), HitType.NORMAL, maxDamage*1.3).randomizeDamage().applyAccuracy(1.1).setProjectile(secondProjectile).setStartingHitDelay(0).setCheckAccuracy(true).setHitGraphic(new Graphic(1103, 90)).setDarkBowSpec(true)});
					}
					else if(rangedAmmo == RangedAmmo.DRAGON_ARROW) {
					    ProjectileDef firstProjectile = new ProjectileDef(1099, ProjectileTrajectory.DOUBLE_ARROW1.clone().setSlowness(20));
					    ProjectileDef secondProjectile = new ProjectileDef(1099, ProjectileTrajectory.DOUBLE_ARROW2.clone().setStartHeight(30).setSlowness(20));
					    setHits(new HitDef[]{new HitDef(getAttackStyle(), HitType.NORMAL, maxDamage*1.5).randomizeDamage().applyAccuracy(1.1).setProjectile(firstProjectile).setStartingHitDelay(1).setCheckAccuracy(true).setHitGraphic(new Graphic(1103, 90)).setDarkBowDragonSpec(true), new HitDef(getAttackStyle(), HitType.NORMAL, maxDamage*1.3).randomizeDamage().applyAccuracy(1.1).setProjectile(secondProjectile).setStartingHitDelay(0).setCheckAccuracy(true).setHitGraphic(new Graphic(1103, 90)).setDarkBowDragonSpec(true)});
					}
					return true;
				}
				@Override
				public int execute(CycleEventContainer container) {
					World.getTickManager().submit(new Tick(1) {
						@Override
						public void execute() {
							stop();

						}
					});
					return super.execute(container) + 1;
				}
			};
			return weaponAttack;
		}
	};

	private byte energyRequired;
	private String[] keywords;

	SpecialType(int energyRequired, String... keywords) {
		this.energyRequired = (byte)energyRequired;
		this.keywords = keywords;
		if (this.keywords == null || this.keywords.length == 0) {
			this.keywords = new String[]{name().toLowerCase().replaceAll("_", " ")};
		}
	}

	public abstract WeaponAttack getSpecialAttack(final Player attacker, final Entity victim, Weapon weapon);

	public byte getEnergyRequired() {
		return energyRequired;
	}

	public static SpecialType getSpecial(Item item) {
		if (item == null)
			return null;
		String itemName = ItemDefinition.forId(item.getId()).getName().toLowerCase();
		for (SpecialType type : SpecialType.values()) {
			for (String specKeys : type.keywords) {
				if (itemName.contains(specKeys.replace("_", " ")))
					return type;
			}
		}
		return null;
	}

	public static void dbaxeSpec(Player player) {
		if (RulesData.NO_SPEC.activated(player)) {
			player.getActionSender().sendMessage("Special attacks have been disabled during this fight!");
			return;
		}
		if (player.getSpecialAmount() < 100) {
			player.getActionSender().sendMessage("You don't have enough special attack to do that.");
			return;
		}
		player.setSpecialAttackActive(!player.isSpecialAttackActive());
		player.getActionSender().updateSpecialBarText(7511);
		player.getUpdateFlags().sendGraphic(246);
		player.getUpdateFlags().sendAnimation(1056);
		player.getUpdateFlags().setForceChatMessage("Raarrrrrgggggghhhhhhh!");
		player.getActionSender().statEdit(Skill.STRENGTH, (int) (player.getSkill().getPlayerLevel(Skill.STRENGTH) * 0.2), true);
		player.getActionSender().statEdit(Skill.ATTACK, (int) (player.getSkill().getPlayerLevel(Skill.ATTACK) * 0.1) * -1, true);
		player.getActionSender().statEdit(Skill.DEFENCE, (int) (player.getSkill().getPlayerLevel(Skill.DEFENCE) * 0.1) * -1, true);
		player.getActionSender().statEdit(Skill.RANGED, (int) (player.getSkill().getPlayerLevel(Skill.RANGED) * 0.1) * -1, true);
		player.getActionSender().statEdit(Skill.MAGIC, (int) (player.getSkill().getPlayerLevel(Skill.MAGIC) * 0.1) * -1, true);
		player.setSpecialAttackActive(false);
		player.setSpecialAmount(0);
		player.getActionSender().updateSpecialAmount(7511);
		player.getActionSender().updateSpecialBarText(7511);
	}
	
	public static void excaliburSpec(Player player) {
		if (RulesData.NO_SPEC.activated(player)) {
			player.getActionSender().sendMessage("Special attacks have been disabled during this fight!");
			return;
		}
		if (player.getSpecialAmount() < 100) {
			player.getActionSender().sendMessage("You don't have enough special attack to do that.");
			return;
		}
		player.setSpecialAttackActive(!player.isSpecialAttackActive());
		player.getActionSender().updateSpecialBarText(7611);
		player.getUpdateFlags().sendGraphic(247);
		player.getUpdateFlags().sendAnimation(1057);
		player.getUpdateFlags().setForceChatMessage("For Camelot!");
		player.getActionSender().statEdit(Skill.DEFENCE, 8, true);
		player.setSpecialAttackActive(false);
		player.setSpecialAmount(0);
		player.getActionSender().updateSpecialAmount(7611);
		player.getActionSender().updateSpecialBarText(7611);
	}
    public static void dfsUncharge(Player player) {
	    if (player.getCombatingEntity() == null) {
		return;
	    }
	    if (player.getCombatingEntity().isDead() || !Following.withinRange(player, player.getCombatingEntity())) {
		return;
	    }
	    if (player.getCombatingEntity().isDoorSupport()) {
		return;
	    }
	    CombatCycleEvent.CanAttackResponse canAttackResponse = CombatCycleEvent.canAttack(player, player.getCombatingEntity());
	    if (canAttackResponse != CanAttackResponse.SUCCESS) {
		return;
	    }
	    if (RulesData.NO_SPEC.activated(player)) {
		player.getActionSender().sendMessage("Special attacks have been disabled during this fight!");
		return;
	    }
	    if (player.getDfsCharges() <= 0) {
		player.getActionSender().sendMessage("You don't have enough dragonbreath charges to do this.");
		return;
	    }
	    player.setSpecialAttackActive(true);
	    final boolean wasRetaliating = player.shouldAutoRetaliate();
	    player.setAutoRetaliate(false);
	    final Entity victim = player.getCombatingEntity();
	    player.getUpdateFlags().sendAnimation(6696);
	    player.getUpdateFlags().sendGraphic(new Graphic(1165, 100));
	    //SpellAttack spellAttack = new SpellAttack(player, player.getCombatingEntity(), player.getEquippedWeapon());
	    HitDef hitDef = new HitDef(Constants.MAGIC_STYLE, HitType.NORMAL, 25).setProjectile(new ProjectileDef(1166, ProjectileTrajectory.SPELL)).setStartingHitDelay(0);
	    new Hit(player, victim, hitDef).initialize();
	    final int damage = Misc.random(15) + 10;
	    final Player finalPlayer = player;
	    CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		@Override
		public void execute(CycleEventContainer b) {
		    b.stop();
		}

		@Override
		public void stop() {
		    finalPlayer.setStopPacket(false);
		    victim.hit(damage, HitType.NORMAL);
		    victim.getUpdateFlags().sendHighGraphic(1167);
		    finalPlayer.setSpecialAttackActive(false);
		    CycleEventHandler.getInstance().addEvent(finalPlayer, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
			    b.stop();
			}

			@Override
			public void stop() {
				CombatManager.attack(finalPlayer, victim);
				finalPlayer.setAutoRetaliate(wasRetaliating);
			}
		    }, 2);
		}
	    }, 3);
	    player.setDfsCharges(player.getDfsCharges() - 1);
	    player.getEquipment().sendBonus(player);
	    if(player.inPestControlGameArea() && victim.inPestControlGameArea()) {
    		PestControl.handleHit(player, victim, damage);
	    }
	}
    
	public static void gmaulSpec(Player player) {
		if (player.getCombatingEntity().isDead() || !Following.withinRange(player, player.getCombatingEntity())) {
			return;
		}
		if (player.getCombatingEntity().isDoorSupport()) {
			return;
		}
		CombatCycleEvent.CanAttackResponse canAttackResponse = CombatCycleEvent.canAttack(player, player.getCombatingEntity());
		if (canAttackResponse != CanAttackResponse.SUCCESS) {
			return;
		}
		if (RulesData.NO_SPEC.activated(player)) {
			player.getActionSender().sendMessage("Special attacks have been disabled during this fight!");
			return;
		}
		if (player.getSpecialAmount() < 50) {
			player.getActionSender().sendMessage("You don't have enough special attack to do that.");
			return;
		}
		player.setSpecialAttackActive(!player.isSpecialAttackActive());
		player.getActionSender().updateSpecialBarText(7486);
		player.getUpdateFlags().sendAnimation(1667);
		player.getUpdateFlags().sendGraphic(new Graphic(337, 100));
		WeaponAttack weaponAttack = new WeaponAttack(player, player.getCombatingEntity(), player.getEquippedWeapon());
		HitDef hitDef = new HitDef(weaponAttack.getAttackStyle(), HitType.NORMAL, CombatManager.calculateMaxMeleeHit(player, weaponAttack)).randomizeDamage().setCheckAccuracy(true);
		new Hit(player, player.getCombatingEntity(), hitDef).initialize();
		player.setSpecialAttackActive(false);
		player.setSpecialAmount(player.getSpecialAmount() - 50);
		player.getActionSender().updateSpecialAmount(7486);
		player.getActionSender().updateSpecialBarText(7486);
	}

	public static void doSpecEffect(Entity attacker, Entity victim, HitDef hitDef, int damage) {
		if (attacker != null && attacker.isPlayer()) {
            Player player = (Player) attacker;
            player.getActionSender().removeInterfaces();
            switch (hitDef.getSpecialEffect()) {
                case 1: // seercull
                    victim.getUpdateFlags().sendGraphic(474);
                    if (damage > 0 && victim.isPlayer()) {
                        Player p = (Player) victim;
                        p.getActionSender().statEdit(Skill.MAGIC, damage * -1, true);
                    }
                    break;
                case 2: // dscim
                    if (victim.isPlayer()) {
                        Player p = (Player) victim;
                        p.getPrayer().unactivatePrayer(PrayerData.PROTECT_FROM_MAGIC);
                        p.getPrayer().unactivatePrayer(PrayerData.PROTECT_FROM_RANGED);
                        p.getPrayer().unactivatePrayer(PrayerData.PROTECT_FROM_MELEE);
                        p.setStopProtectPrayer(System.currentTimeMillis() + 5000);
                        p.getActionSender().sendMessage("You have been injured!");
                    }
                    break;
                case 3: // daxe
                    if (damage > 0 && victim.isPlayer()) {
                        Player o = (Player) victim;
                        o.getActionSender().statEdit(Skill.DEFENCE, damage * -1, true);
                        o.getActionSender().statEdit(Skill.MAGIC, damage * -1, true);
                    }
                    break;
                case 4: // darklight
                    if (damage > 0 && victim.isPlayer()) {
                        Player o = (Player) victim;
                        o.getActionSender().statEdit(Skill.ATTACK, (int) (o.getSkill().getPlayerLevel(Skill.ATTACK) * 0.05) * -1, true);
                        o.getActionSender().statEdit(Skill.STRENGTH, (int) (o.getSkill().getPlayerLevel(Skill.STRENGTH) * 0.05) * -1, true);
                        o.getActionSender().statEdit(Skill.DEFENCE, (int) (o.getSkill().getPlayerLevel(Skill.DEFENCE) * 0.05) * -1, true);
                    }
                    break;
                case 5: // dspear
                    int x1 = player.getPosition().getX(), y1 = player.getPosition().getY(), x2 = 0, y2 = 0;
                    int otherX = victim.getPosition().getX(), otherY = victim.getPosition().getY();
                    if (x1 > otherX) {
                        x2 = -1;
                    } else if (x1 < otherX) {
                        x2 = 1;
                    }
                    if (y1 > otherY) {
                        y2 = -1;
                    } else if (y1 < otherY) {
                        y2 = 1;
                    }
                    boolean canWalk = victim.getSize() > 1;
                    victim.getUpdateFlags().sendHighGraphic(254);
                    if (canWalk) {
                        if (victim.isPlayer()) {
                            Player p = (Player) victim;
                            p.getActionSender().walkTo(x2, y2, false);
                        } else {
                            Npc n = (Npc) victim;
                            n.walkTo(new Position(n.getPosition().getX() + x2, n.getPosition().getY() + y2), false);
                        }
                    }
                    victim.getStunTimer().setWaitDuration(6);
                    victim.getStunTimer().reset();
                    break;
                case 6 : // d2h
    				boolean inMulti = attacker.inMulti();
        			int enemiesHit = 0;
    				if (!inMulti) {
    					//new Hit(attacker, victim, hitDef).initialize();
    					break;
    				}
    				for (Player players : World.getPlayers()) {
    					if (players != null && players != attacker && players != attacker.getCombatingEntity()) {
    						CombatCycleEvent.CanAttackResponse canAttackResponse = CombatCycleEvent.canAttack(attacker, players);
    						if (Misc.getDistance(attacker.getPosition(), players.getPosition()) <= 1 && canAttackResponse == CanAttackResponse.SUCCESS) {
    							WeaponAttack weaponAttack = new WeaponAttack(player, players, player.getEquippedWeapon());
    		    				HitDef d2hDef = new HitDef(weaponAttack.getAttackStyle(), HitType.NORMAL, CombatManager.calculateMaxMeleeHit(player, weaponAttack)).randomizeDamage().setCheckAccuracy(true);
    		    				new Hit(attacker, players, d2hDef).initialize();
    							enemiesHit++;
    							if (enemiesHit > 13) {
    								break;
    							}
    						}
    					}
    				}
    				for (Npc npcs : World.getNpcs()) {
				    if(npcs != null && npcs.getNpcId() != 3782)
					continue;
    					if (npcs != null && npcs != attacker.getCombatingEntity()) {
    						CombatCycleEvent.CanAttackResponse canAttackResponse = CombatCycleEvent.canAttack(attacker, npcs);
    						if (attacker.goodDistanceEntity(npcs, 1) && canAttackResponse == CanAttackResponse.SUCCESS) {
    							WeaponAttack weaponAttack = new WeaponAttack(player, npcs, player.getEquippedWeapon());
    		    				HitDef d2hDef = new HitDef(weaponAttack.getAttackStyle(), HitType.NORMAL, CombatManager.calculateMaxMeleeHit(player, weaponAttack)).randomizeDamage().setCheckAccuracy(true);
    		    				new Hit(attacker, npcs, d2hDef).initialize();
    							enemiesHit++;
    							if (enemiesHit > 13) {
    								break;
    							}
    						}
    					}
    				}
                	break;
                case 7 : //guthan
                	victim.getUpdateFlags().sendGraphic(398);
    	    		attacker.heal(damage);
                	break;
                case 8 : //torag
                	victim.getUpdateFlags().sendGraphic(399);
    	    		if (victim.isPlayer()) {
    	    			((Player) victim).setEnergy(((Player) victim).getEnergy() - 4);
    	    			((Player) victim).getActionSender().sendEnergy();
    	    		}
                	break;
                case 9 : //ahrim
                	victim.getUpdateFlags().sendHighGraphic(400);
    	    		if (victim.isPlayer()) {
    	    			((Player) victim).getActionSender().statEdit(Skill.STRENGTH, -5, false);
    	    		}
                	break;
                case 10 : //karil
                	victim.getUpdateFlags().sendHighGraphic(401);
    	    		if (victim.isPlayer()) {
    	    			((Player) victim).getActionSender().statEdit(Skill.AGILITY, (int )(((Player) victim).getSkill().getLevel()[Skill.AGILITY] * 0.20) * -1, true);
    	    		}
    	    		break;
            }
        }
	}
}
