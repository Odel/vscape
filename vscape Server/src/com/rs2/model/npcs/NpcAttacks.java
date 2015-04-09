package com.rs2.model.npcs;

import com.rs2.model.Entity;
import com.rs2.model.Graphic;
import com.rs2.model.content.combat.AttackScript;
import com.rs2.model.content.combat.AttackType;
import com.rs2.model.content.combat.attacks.BasicAttack;
import com.rs2.model.content.combat.projectile.ProjectileTrajectory;
import com.rs2.model.content.combat.weapon.AttackStyle;
import com.rs2.model.content.combat.weapon.RangedAmmo;
import com.rs2.model.content.combat.weapon.Weapon;
import com.rs2.model.content.skills.magic.Spell;

public enum NpcAttacks {

	MONK_OF_ZAMORAK(new int[]{106}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, 4, Weapon.FISTS),
				BasicAttack.magicAttack(attacker, victim, Spell.FIRE_BOLT_ZAMORAK)
			};
		}
	}),
	DARK_WIZARD(new int[]{172, 174, 3242, 3243, 3244, 3245, 4658, 4659, 4660, 4661}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.magicAttack(attacker, victim, Spell.WATER_STRIKE),
				BasicAttack.magicAttack(attacker, victim, Spell.CONFUSE)
			};
		}
	}),
	BATTLE_MAGE_GUTHIX(new int[]{914}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.magicAttack(attacker, victim, Spell.CLAWS_OF_GUTHIX_BATTLE)
			};
		}
	}),
	BATTLE_MAGE_ZAMORAK(new int[]{912, 6221}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.magicAttack(attacker, victim, Spell.FLAMES_OF_ZAMORAK)
			};
		}
	}),
	BATTLE_MAGE_SARADOMIN(new int[]{913, 6257}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.magicAttack(attacker, victim, Spell.SARADOMIN_STRIKE)
			};
		}
	}),
	TORCHER(new int[]{3752, 3754, 3756, 3758, 3759}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.magicAttack(attacker, victim, Spell.TORCHER)
			};
		}
	}),
	TOK_XIL(new int[]{2631}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 13, 4, 2628),
				BasicAttack.projectileAttack(attacker, victim, AttackType.RANGED, AttackStyle.Mode.LONGRANGE, 13, 4, 2633, new Graphic(-1, 0), new Graphic(-1, 0), 443, ProjectileTrajectory.ARROW)
			};
		}
	}),
	MEJ_KOT(new int[]{2741}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 25, 5, 2637)
			};
		}
	}),
	KET_ZEK(new int[]{2743}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 54, 5, 2644),
				BasicAttack.projectileAttack(attacker, victim, AttackType.MAGIC, AttackStyle.Mode.MAGIC, 49, 6, 2647, new Graphic(-1, 0), new Graphic(446, 0), 445, ProjectileTrajectory.SPELL)
			};
		}
	}),
	YT_HURKOT(new int[]{2746}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 14, 8, 2637),
			};
		}
	}),
	TZ_TOK_JAD(new int[]{2745}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 97, 8, 2655),
				BasicAttack.jadRangeAttack(attacker, victim, AttackType.RANGED, AttackStyle.Mode.LONGRANGE, 97, 10, 2652, new Graphic(-1, 0), new Graphic(451, 50), 441, ProjectileTrajectory.JAD_RANGE, 1),
				BasicAttack.projectileAttack(attacker, victim, AttackType.MAGIC, AttackStyle.Mode.MAGIC, 97, 8, 2656, new Graphic(447, 100), new Graphic(157, 0), 448, ProjectileTrajectory.JAD_SPELL)
			};
		}
	}),
	AHRIM(new int[]{2025}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.magicAttack(attacker, victim, Spell.FIRE_WAVE),
				BasicAttack.magicAttack(attacker, victim, Spell.CONFUSE),
				BasicAttack.magicAttack(attacker, victim, Spell.CURSE)
			};
		}
	}),
	KARIL(new int[]{2028}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.rangedAttack(attacker, victim, AttackStyle.Mode.RAPID, 20, Weapon.KARILS_CROSSBOW, RangedAmmo.BOLT_RACK)
			};
		}
	}),
	MONKEY_ARCHER(new int[]{1456, 1457, 1458}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.rangedAttack(attacker, victim, AttackStyle.Mode.RAPID, 12, Weapon.MONKEY_BOW, RangedAmmo.MONKEY_ARROW)
			};
		}
	}),
	DEFILER(new int[]{3762, 3764, 3766, 3768, 3770}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.projectileAttack(attacker, victim, AttackType.RANGED, AttackStyle.Mode.LONGRANGE, 7, 8, 3920, new Graphic(-1, 0), new Graphic(-1, 0), 214, ProjectileTrajectory.KNIFE)
			};
		}
	}),
	GREEN_DRAGON(new int[]{941, 742, 4677, 4678, 4679, 4680}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 5, 5, 80),
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 8, 5, 80),
				BasicAttack.projectileAttack(attacker, victim, AttackType.MAGIC, AttackStyle.Mode.DRAGONFIRE, 2, 8, 81, new Graphic(1, 0), new Graphic(-1, 0), -1, ProjectileTrajectory.SPELL)
			};
		}
	}),
	BLUE_DRAGON(new int[]{55, 4681, 4682, 4683, 4684}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 7, 5, 80),
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 10, 5, 80),
				BasicAttack.projectileAttack(attacker, victim, AttackType.MAGIC, AttackStyle.Mode.DRAGONFIRE, 2, 8, 81, new Graphic(1, 0), new Graphic(-1, 0), -1, ProjectileTrajectory.SPELL)
			};
		}
	}),
	RED_DRAGON(new int[]{53, 4669, 4670, 4671, 4672}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 10, 5, 80),
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 16, 5, 80),
				BasicAttack.projectileAttack(attacker, victim, AttackType.MAGIC, AttackStyle.Mode.DRAGONFIRE, 2, 8, 81, new Graphic(1, 0), new Graphic(-1, 0), -1, ProjectileTrajectory.SPELL)
			};
		}
	}),
	BLACK_DRAGON(new int[]{54, 4673, 4674, 4675, 4676}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 15, 5, 80),
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 21, 5, 80),
				BasicAttack.projectileAttack(attacker, victim, AttackType.MAGIC, AttackStyle.Mode.DRAGONFIRE, 2, 8, 81, new Graphic(1, 0), new Graphic(-1, 0), -1, ProjectileTrajectory.SPELL)
			};
		}
	}),
	KING_BLACK_DRAGON(new int[]{50}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 26, 5, 80),
				BasicAttack.projectileAttack(attacker, victim, AttackType.MAGIC, AttackStyle.Mode.DRAGONFIRE_FAR, 2, 8, 81, new Graphic(-1, 0), new Graphic(-1, 0), 393, ProjectileTrajectory.SPELL),
				BasicAttack.magicAttack(attacker, victim, Spell.KBD_POISON),
				BasicAttack.magicAttack(attacker, victim, Spell.KBD_SHOCK),
				BasicAttack.magicAttack(attacker, victim, Spell.KBD_FREEZE)
			};
		}
	}),
	BRONZE_DRAGON(new int[]{1590}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 8, 5, 80),
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 12, 5, 80),
				BasicAttack.projectileAttack(attacker, victim, AttackType.MAGIC, AttackStyle.Mode.DRAGONFIRE_FAR, 2, 8, 81, new Graphic(-1, 0), new Graphic(-1, 0), 54, ProjectileTrajectory.SPELL)
			};
		}
	}),
	IRON_DRAGON(new int[]{1591}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 15, 5, 80),
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 20, 5, 80),
				BasicAttack.projectileAttack(attacker, victim, AttackType.MAGIC, AttackStyle.Mode.DRAGONFIRE_FAR, 2, 8, 81, new Graphic(-1, 0), new Graphic(-1, 0), 54, ProjectileTrajectory.SPELL)
			};
		}
	}),
	STEEL_DRAGON(new int[]{1592}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 15, 5, 80),
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 22, 5, 80),
				BasicAttack.projectileAttack(attacker, victim, AttackType.MAGIC, AttackStyle.Mode.DRAGONFIRE_FAR, 2, 8, 81, new Graphic(-1, 0), new Graphic(-1, 0), 54, ProjectileTrajectory.SPELL)
			};
		}
	}),
	MITHRIL_DRAGON(new int[]{5363}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 28, 5, 80),
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 20, 5, 80),
				BasicAttack.magicAttack(attacker, victim, Spell.MITHRIL_SPELL),
				BasicAttack.projectileAttack(attacker, victim, AttackType.MAGIC, AttackStyle.Mode.DRAGONFIRE_FAR, 2, 8, 81, new Graphic(-1, 0), new Graphic(-1, 0), 54, ProjectileTrajectory.SPELL),
				BasicAttack.projectileAttack(attacker, victim, AttackType.RANGED, AttackStyle.Mode.LONGRANGE, 18, 8, 81, new Graphic(-1, 0), new Graphic(-1, 0), 16, ProjectileTrajectory.KNIFE)
			};
		}
	}),
	BRUTAL_GREEN_DRAGON(new int[]{5362}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 18, 5, 80),
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 18, 5, 80),
				BasicAttack.magicAttack(attacker, victim, Spell.BRUTAL_GREEN_SPELL),
				BasicAttack.projectileAttack(attacker, victim, AttackType.MAGIC, AttackStyle.Mode.DRAGONFIRE_FAR, 2, 8, 81, new Graphic(-1, 0), new Graphic(-1, 0), 54, ProjectileTrajectory.SPELL)
			};
		}
	}),
	WALLASALKI(new int[]{2457, 2884}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.magicAttack(attacker, victim, Spell.WATER_WAVE)
			};
		}
	}),
	DAGANNOTH_MELEE(new int[]{2455, 2888}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 15, 5, 1341)
			};
		}
	}),
	WEAK_DAGANNOTH_MELEE(new int[]{1341, 1342, 1343}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 8, 5, 1341)
			};
		}
	}),
	DAGANNOTH_RANGE(new int[]{2456, 2887}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.projectileAttack(attacker, victim, AttackType.RANGED, AttackStyle.Mode.LONGRANGE, 11, 8, 1343, new Graphic(-1, 0), new Graphic(-1, 0), 294, ProjectileTrajectory.KNIFE)
			};
		}
	}),
	WEAK_DAGANNOTH_RANGE(new int[]{1338, 1339, 1340}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.projectileAttack(attacker, victim, AttackType.RANGED, AttackStyle.Mode.LONGRANGE, 6, 8, 1343, new Graphic(-1, 0), new Graphic(-1, 0), 294, ProjectileTrajectory.KNIFE)
			};
		}
	}),
	DAGANNOTH_MOTHER(new int[]{1351, 1352, 1353, 1354, 1355, 1356}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 18, 5, 1341),
				BasicAttack.projectileAttack(attacker, victim, AttackType.RANGED, AttackStyle.Mode.LONGRANGE, 12, 8, 1343, new Graphic(-1, 0), new Graphic(-1, 0), 294, ProjectileTrajectory.KNIFE)
			};
		}
	}),
	SPINOLYP(new int[]{2892, 2894, 2896}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.magicAttack(attacker, victim, Spell.SPINOLYP),
				BasicAttack.projectileAttack(attacker, victim, AttackType.RANGED, AttackStyle.Mode.LONGRANGE, 11, 8, 2868, new Graphic(-1, 0), new Graphic(-1, 0), 294, ProjectileTrajectory.KNIFE)	
			};
		}
	}),
	DAGANNOTH_SUPREME(new int[]{2881}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.projectileAttack(attacker, victim, AttackType.RANGED, AttackStyle.Mode.LONGRANGE, 32, 8, 2855, new Graphic(-1, 0), new Graphic(-1, 0), 294, ProjectileTrajectory.KNIFE)
			};
		}
	}),
	DAGANNOTH_PRIME(new int[]{2882}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.magicAttack(attacker, victim, Spell.PRIME)
			};
		}
	}),
	BLOODVELD(new int[]{1618}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MAGIC, AttackStyle.Bonus.SLASH, 5, 5, 1552)
			};
		}
	}),
	INFERNAL_MAGE(new int[]{1643, 1644, 1645, 1646, 1647}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.magicAttack(attacker, victim, Spell.INFERNAL_MAGE_SPELL)
			};
		}
	}),
	PYREFIEND(new int[]{1633, 1634, 1635, 1636}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MAGIC, AttackStyle.Bonus.SLASH, 4, 5, 1582),
			};
		}
	}),
	JELLY(new int[]{1637, 1638, 1639, 1640, 1641, 1642}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MAGIC, AttackStyle.Bonus.SLASH, 7, 5, 1586),
			};
		}
	}),
	BANSHEE(new int[]{1612}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MAGIC, AttackStyle.Bonus.SLASH, 3, 5, 1523),
			};
		}
	}),
	ABERRANT_SPECTRE(new int[]{1604, 1605, 1606, 1607}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.projectileAttack(attacker, victim, AttackType.MAGIC, AttackStyle.Mode.MAGIC, 10, 5, 1507, new Graphic(-1, 0), new Graphic(336, 500), 335, ProjectileTrajectory.SPECTRE_ATTACK)
			};
		}
	}),
	SKELETAL_WYVERN(new int[]{3068, 3069, 3070, 3071}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 14, 5, 2986),
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 14, 5, 2985),
				BasicAttack.projectileAttack(attacker, victim, AttackType.MAGIC, AttackStyle.Mode.DRAGONFIRE_FAR, 2, 10, 2988, new Graphic(2, 0), new Graphic(-1, 0), -1, ProjectileTrajectory.SPELL),
				BasicAttack.projectileAttack(attacker, victim, AttackType.RANGED, AttackStyle.Mode.LONGRANGE, 14, 4, 2989, new Graphic(499, 0), new Graphic(502, 0), 500, ProjectileTrajectory.SPELL)
			};
		}
	}),
	KOLODION_FIRST_FORM(new int[]{907}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.magicAttack(attacker, victim, Spell.KOLODION1)
			};
		}
	}),
	KOLODION_SECOND_FORM(new int[]{908}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.magicAttack(attacker, victim, Spell.KOLODION2)
			};
		}
	}),
	KOLODION_FOURTH_FORM(new int[]{910}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.magicAttack(attacker, victim, Spell.KOLODION4)
			};
		}
	}),
	CHAOS_ELEMENTAL(new int[]{3200}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.magicAttack(attacker, victim, Spell.CHAOSMAIN),
				BasicAttack.magicAttack(attacker, victim, Spell.CHAOSDISARM),
			};
		}
	}),
	KALPHITE_QUEEN_FIRST_FORM(new int[]{1158}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 31, 5, 6241),
				BasicAttack.magicAttack(attacker, victim, Spell.KQ1),
				BasicAttack.projectileAttack(attacker, victim, AttackType.RANGED, AttackStyle.Mode.LONGRANGE, 31, 8, 6240, new Graphic(-1, 0), new Graphic(-1, 0), 289, ProjectileTrajectory.KNIFE)
			};
		}
	}),
	KALPHITE_QUEEN_SECOND_FORM(new int[]{1160}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.STAB, 31, 5, 6235),
				BasicAttack.magicAttack(attacker, victim, Spell.KQ2),
				BasicAttack.projectileAttack(attacker, victim, AttackType.RANGED, AttackStyle.Mode.LONGRANGE, 31, 8, 6234, new Graphic(-1, 0), new Graphic(-1, 0), 289, ProjectileTrajectory.KNIFE)
			};
		}
	}),
	JUNGLE_DEMON(new int[]{1472}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.magicAttack(attacker, victim, Spell.JUNGLE_DEMON_BLAST_1),
				BasicAttack.magicAttack(attacker, victim, Spell.JUNGLE_DEMON_BLAST_2),
				BasicAttack.magicAttack(attacker, victim, Spell.JUNGLE_DEMON_BLAST_3),
				BasicAttack.magicAttack(attacker, victim, Spell.JUNGLE_DEMON_BLAST_4),
				BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 32, 5, 64),
			};
		}
	}),
	FIRE_WARRIOR_OF_LESARKUS(new int[]{277}, new NpcCombatDef() {
		@Override
		public AttackScript[] attackScripts(Entity attacker, Entity victim) {
			return new AttackScript[] {
				BasicAttack.magicAttack(attacker, victim, Spell.FIRE_BLAST),
			};
		}
	});

	private final int[] npcIds;
	private final NpcCombatDef def;

	NpcAttacks(int[] npcIds, NpcCombatDef def) {
		this.npcIds = npcIds;
		this.def = def;
	}

	public int[] getNpcIds() {
		return this.npcIds;
	}

	public NpcCombatDef getCombatDef() {
		return this.def;
	}
}
