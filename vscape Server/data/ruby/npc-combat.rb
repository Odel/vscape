require 'java'
java_import "com.rs2.model.npcs.NpcCombatDef"
java_import "com.rs2.model.content.combat.AttackScript"
java_import "com.rs2.model.content.combat.attacks.BasicAttack"
java_import "com.rs2.model.content.combat.HealersCombatScript"
java_import "com.rs2.model.content.combat.weapon.AttackStyle"
java_import "com.rs2.model.content.combat.AttackType"
java_import "com.rs2.model.content.combat.weapon.Weapon"

java_import "com.rs2.model.content.combat.projectile.ProjectileTrajectory"
java_import "com.rs2.model.Graphic"
java_import "com.rs2.model.content.skills.magic.Spell"
java_import "com.rs2.model.content.combat.weapon.RangedAmmo"

class Man < NpcCombatDef
    def attackScripts attacker, victim
        return [BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, 2, Weapon::FISTS)]
    end
end

class MonkOfZamorak < NpcCombatDef
    def attackScripts attacker, victim
        return [
		BasicAttack.magicAttack(attacker, victim, Spell::FIRE_BOLT_ZAMORAK),
		BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, 4, Weapon::FISTS)
	];
    end
end

class BloodVeld < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MAGIC, AttackStyle::Bonus::SLASH, 5, 6, 1552),
	];
    end
end

class DarkWizard < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.magicAttack(attacker, victim, Spell::WATER_STRIKE),
                BasicAttack.magicAttack(attacker, victim, Spell::CONFUSE)
        ];
    end
end

class TokXil < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 13, 4, 2628),
                BasicAttack.projectileAttack(attacker, victim, AttackType::RANGED, AttackStyle::Mode::LONGRANGE, 13, 4, 2633, Graphic.new(-1, 0), Graphic.new(-1, 0), 443, ProjectileTrajectory.ARROW)
        ];
    end
end

class MejKot < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 25, 5, 2637)
        ];
    end
end

class KetZek < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 54, 5, 2644),
                BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::MAGIC, 49, 6, 2647, Graphic.new(-1, 0), Graphic.new(446, 0), 445, ProjectileTrajectory.SPELL)
        ];
    end
end

class Jad < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 97, 8, 2655),
                BasicAttack.jadRangeAttack(attacker, victim, AttackType::RANGED, AttackStyle::Mode::LONGRANGE, 97, 10, 2652, Graphic.new(-1, 0), Graphic.new(451, 50), 441, ProjectileTrajectory.JAD_RANGE, 1),
                BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::MAGIC, 97, 8, 2656, Graphic.new(447, 100), Graphic.new(157, 0), 448, ProjectileTrajectory.JAD_SPELL)
        ];
    end
end


class YtHurkot < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 14, 8, 2637),

        ];
    end
end

class Ahrims < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.magicAttack(attacker, victim, Spell::FIRE_WAVE),
                BasicAttack.magicAttack(attacker, victim, Spell::CONFUSE),
                BasicAttack.magicAttack(attacker, victim, Spell::CURSE)
        ];
    end
end

class Karil < NpcCombatDef
    def attackScripts attacker, victim
        return [
			BasicAttack.rangedAttack(attacker, victim, AttackStyle::Mode::RAPID, 20, Weapon::KARILS_CROSSBOW, RangedAmmo::BOLT_RACK)			
        ];
    end
end

class MonkeyArcher < NpcCombatDef
    def attackScripts attacker, victim
        return [
			BasicAttack.rangedAttack(attacker, victim, AttackStyle::Mode::RAPID, 12, Weapon::MONKEY_BOW, RangedAmmo::MONKEY_ARROW)			
        ];
    end
end

class GreenDragon < NpcCombatDef
    def attackScripts attacker, victim
        return [
		    BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 5, 5, 80),
			BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 8, 5, 80),
			BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE, 2, 8, 81, Graphic.new(1, 0), Graphic.new(-1, 0), -1, ProjectileTrajectory.SPELL),
			BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE_FAR, 2, 8, 81, Graphic.new(1, 0), Graphic.new(-1, 0), -1, ProjectileTrajectory.SPELL)
        ];
    end
end

class BrutalGreenDragon < NpcCombatDef
    def attackScripts attacker, victim
        return [
			BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 18, 5, 80),
			BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 18, 5, 80),
			BasicAttack.magicAttack(attacker, victim, Spell::BRUTAL_GREEN_SPELL),
			BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE, 2, 8, 81, Graphic.new(1, 0), Graphic.new(-1, 0), -1, ProjectileTrajectory.SPELL),
			BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE_FAR, 2, 8, 81, Graphic.new(1, 0), Graphic.new(-1, 0), -1, ProjectileTrajectory.SPELL)
        ];
    end
end

class SkeletalWyvern < NpcCombatDef
    def attackScripts attacker, victim
        return [
			BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 14, 5, 2986),
			BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 14, 5, 2985),
			BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE_FAR, 2, 10, 2988, Graphic.new(2, 0), Graphic.new(-1, 0), -1, ProjectileTrajectory.SPELL),
			BasicAttack.projectileAttack(attacker, victim, AttackType::RANGED, AttackStyle::Mode::LONGRANGE, 14, 4, 2989, Graphic.new(499, 0), Graphic.new(502, 0), 500, ProjectileTrajectory.SPELL)
        ];
    end
end

class BlueDragon < NpcCombatDef
    def attackScripts attacker, victim
        return [
		    BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 7, 5, 80),
			BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 10, 5, 80),
			BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE, 2, 8, 81, Graphic.new(1, 0), Graphic.new(-1, 0), -1, ProjectileTrajectory.SPELL),
			BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE_FAR, 2, 8, 81, Graphic.new(1, 0), Graphic.new(-1, 0), -1, ProjectileTrajectory.SPELL)
        ];
    end
end

class RedDragon < NpcCombatDef
    def attackScripts attacker, victim
        return [
		    BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 10, 5, 80),
			BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 16, 5, 80),
			BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE, 2, 8, 81, Graphic.new(1, 0), Graphic.new(-1, 0), -1, ProjectileTrajectory.SPELL),
			BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE_FAR, 2, 8, 81, Graphic.new(1, 0), Graphic.new(-1, 0), -1, ProjectileTrajectory.SPELL)
        ];
    end
end

class BlackDragon < NpcCombatDef
    def attackScripts attacker, victim
        return [
		    BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 15, 5, 80),
			BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 21, 5, 80),
			BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE, 2, 8, 81, Graphic.new(1, 0), Graphic.new(-1, 0), -1, ProjectileTrajectory.SPELL),
			BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE_FAR, 2, 8, 81, Graphic.new(1, 0), Graphic.new(-1, 0), -1, ProjectileTrajectory.SPELL)
        ];
    end
end

class KingBlackDragon < NpcCombatDef
    def attackScripts attacker, victim
        return [
			BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 26, 5, 80),
			BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE, 2, 8, 81, Graphic.new(-1, 0), Graphic.new(-1, 0), 393, ProjectileTrajectory.SPELL),
			BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE_FAR, 2, 8, 81, Graphic.new(-1, 0), Graphic.new(-1, 0), 393, ProjectileTrajectory.SPELL),
			BasicAttack.magicAttack(attacker, victim, Spell::KBD_POISON),
			BasicAttack.magicAttack(attacker, victim, Spell::KBD_SHOCK),
			BasicAttack.magicAttack(attacker, victim, Spell::KBD_FREEZE)
        ];
    end
end

class BronzeDragon < NpcCombatDef
    def attackScripts attacker, victim
        return [
		    BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 8, 5, 80),
			BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 12, 5, 80),
			BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE, 2, 8, 81, Graphic.new(1, 0), Graphic.new(-1, 0), -1, ProjectileTrajectory.SPELL),
			BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE_FAR, 2, 8, 81, Graphic.new(1, 0), Graphic.new(-1, 0), -1, ProjectileTrajectory.SPELL)
        ];
    end
end

class IronDragon < NpcCombatDef
    def attackScripts attacker, victim
        return [
		    BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 15, 5, 80),
			BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 20, 5, 80),
			BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE, 2, 8, 81, Graphic.new(1, 0), Graphic.new(-1, 0), -1, ProjectileTrajectory.SPELL),
			BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE_FAR, 2, 8, 81, Graphic.new(1, 0), Graphic.new(-1, 0), -1, ProjectileTrajectory.SPELL)
        ];
    end
end

class SteelDragon < NpcCombatDef
    def attackScripts attacker, victim
        return [
		    BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 15, 5, 80),
			BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 22, 5, 80),
			BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE, 2, 8, 81, Graphic.new(1, 0), Graphic.new(-1, 0), -1, ProjectileTrajectory.SPELL),
			BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE_FAR, 2, 8, 81, Graphic.new(1, 0), Graphic.new(-1, 0), -1, ProjectileTrajectory.SPELL)
        ];
    end
end

class MithrilDragon < NpcCombatDef
    def attackScripts attacker, victim
        return [
			BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 28, 5, 80),
			BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 20, 5, 80),
			BasicAttack.magicAttack(attacker, victim, Spell::MITHRIL_SPELL),
			BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE, 2, 8, 81, Graphic.new(1, 0), Graphic.new(-1, 0), -1, ProjectileTrajectory.SPELL),
			BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE_FAR, 2, 8, 81, Graphic.new(1, 0), Graphic.new(-1, 0), -1, ProjectileTrajectory.SPELL)
        ];
    end
end

class Wallasalki < NpcCombatDef
    def attackScripts attacker, victim
        return [
			BasicAttack.magicAttack(attacker, victim, Spell::WATER_WAVE)
		];
    end
end
 
class DagannothMelee < NpcCombatDef
    def attackScripts attacker, victim
        return [
		    BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 15, 5, 1341),
		];
    end
end

class DagannothRange < NpcCombatDef
    def attackScripts attacker, victim
        return [
			BasicAttack.projectileAttack(attacker, victim, AttackType::RANGED, AttackStyle::Mode::LONGRANGE, 11, 8, 1343, Graphic.new(-1, 0), Graphic.new(-1, 0), 294, ProjectileTrajectory.KNIFE)		
		];
    end
end

class WeakDagannothRange < NpcCombatDef
    def attackScripts attacker, victim
        return [
			BasicAttack.projectileAttack(attacker, victim, AttackType::RANGED, AttackStyle::Mode::LONGRANGE, 6, 8, 1343, Graphic.new(-1, 0), Graphic.new(-1, 0), 294, ProjectileTrajectory.KNIFE)		
		];
    end
end

class WeakDagannothMelee < NpcCombatDef
    def attackScripts attacker, victim
        return [
			BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 8, 5, 1341),		
		];
    end
end

class DagannothMother < NpcCombatDef
    def attackScripts attacker, victim
        return [
		    BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 18, 5, 1341),
		    BasicAttack.projectileAttack(attacker, victim, AttackType::RANGED, AttackStyle::Mode::LONGRANGE, 12, 8, 1343, Graphic.new(-1, 0), Graphic.new(-1, 0), 294, ProjectileTrajectory.KNIFE)
		];
    end
end

class Defiler < NpcCombatDef
    def attackScripts attacker, victim
        return [
			BasicAttack.projectileAttack(attacker, victim, AttackType::RANGED, AttackStyle::Mode::LONGRANGE, 7, 8, 3920, Graphic.new(-1, 0), Graphic.new(-1, 0), 214, ProjectileTrajectory.KNIFE)		
		];
    end
end

class Torcher < NpcCombatDef
    def attackScripts attacker, victim
        return [
			BasicAttack.magicAttack(attacker, victim, Spell::TORCHER)
		];
    end
end

class DagannothSupreme < NpcCombatDef
    def attackScripts attacker, victim
        return [
			BasicAttack.projectileAttack(attacker, victim, AttackType::RANGED, AttackStyle::Mode::LONGRANGE, 32, 8, 2855, Graphic.new(-1, 0), Graphic.new(-1, 0), 294, ProjectileTrajectory.KNIFE)		
		];
    end
end

class Spinolyp < NpcCombatDef
    def attackScripts attacker, victim
        return [
			BasicAttack.magicAttack(attacker, victim, Spell::SPINOLYP),
			BasicAttack.projectileAttack(attacker, victim, AttackType::RANGED, AttackStyle::Mode::LONGRANGE, 11, 8, 2868, Graphic.new(-1, 0), Graphic.new(-1, 0), 294, ProjectileTrajectory.KNIFE)	
		];
    end
end

class DagannothPrime < NpcCombatDef
    def attackScripts attacker, victim
        return [
			BasicAttack.magicAttack(attacker, victim, Spell::PRIME)
		];
    end
end

class ChaosElemental < NpcCombatDef
    def attackScripts attacker, victim
        return [
			BasicAttack.magicAttack(attacker, victim, Spell::CHAOSMAIN),
                        BasicAttack.magicAttack(attacker, victim, Spell::CHAOSDISARM),
		];
    end
end

class KalphiteQueenFirstForm < NpcCombatDef
    def attackScripts attacker, victim
        return [
		    BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 31, 5, 6241),
                    BasicAttack.magicAttack(attacker, victim, Spell::KQ1),
		    BasicAttack.projectileAttack(attacker, victim, AttackType::RANGED, AttackStyle::Mode::LONGRANGE, 31, 8, 6240, Graphic.new(-1, 0), Graphic.new(-1, 0), 289, ProjectileTrajectory.KNIFE)
		];
    end
end

class KalphiteQueenSecondForm < NpcCombatDef
    def attackScripts attacker, victim
        return [
		    BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::STAB, 31, 5, 6235),
                    BasicAttack.magicAttack(attacker, victim, Spell::KQ2),
                    BasicAttack.projectileAttack(attacker, victim, AttackType::RANGED, AttackStyle::Mode::LONGRANGE, 31, 8, 6234, Graphic.new(-1, 0), Graphic.new(-1, 0), 289, ProjectileTrajectory.KNIFE)
		];
    end
end

class BattleMageGuthix < NpcCombatDef
    def attackScripts attacker, victim
        return [
                    BasicAttack.magicAttack(attacker, victim, Spell::CLAWS_OF_GUTHIX_BATTLE)
		];
    end
end

class BattleMageZamorak < NpcCombatDef
    def attackScripts attacker, victim
        return [
                    BasicAttack.magicAttack(attacker, victim, Spell::FLAMES_OF_ZAMORAK)
		];
    end
end

class BattleMageSaradomin < NpcCombatDef
    def attackScripts attacker, victim
        return [
                    BasicAttack.magicAttack(attacker, victim, Spell::SARADOMIN_STRIKE)
		];
    end
end

class KolodionFirstForm < NpcCombatDef
    def attackScripts attacker, victim
        return [
                    BasicAttack.magicAttack(attacker, victim, Spell::KOLODION1)
		];
    end
end

class KolodionSecondForm < NpcCombatDef
    def attackScripts attacker, victim
        return [
                    BasicAttack.magicAttack(attacker, victim, Spell::KOLODION2)
		];
    end
end

class KolodionFourthForm < NpcCombatDef
    def attackScripts attacker, victim
        return [
                    BasicAttack.magicAttack(attacker, victim, Spell::KOLODION4)
		];
    end
end

class JungleDemon < NpcCombatDef
    def attackScripts attacker, victim
        return [
			BasicAttack.magicAttack(attacker, victim, Spell::JUNGLE_DEMON_BLAST_1),
			BasicAttack.magicAttack(attacker, victim, Spell::JUNGLE_DEMON_BLAST_2),
			BasicAttack.magicAttack(attacker, victim, Spell::JUNGLE_DEMON_BLAST_3),
			BasicAttack.magicAttack(attacker, victim, Spell::JUNGLE_DEMON_BLAST_4),
                        BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 32, 5, 64),
		];
    end
end

NpcCombatDef.add([2025], Ahrims.new())
NpcCombatDef.add([2028], Karil.new())
NpcCombatDef.add([2746], YtHurkot.new()) #.bonusDef(1000, 1000, 1000, 1000, 600)
NpcCombatDef.add([2631], TokXil.new()) #.bonusDef(600, 600, 600, 600, 300)
NpcCombatDef.add([2741], MejKot.new()) #.bonusDef(1000, 1000, 1000, 1000, 600)
NpcCombatDef.add([2743], KetZek.new()) #.bonusDef(1300, 1300, 1300, 1300, 700)
NpcCombatDef.add([2745], Jad.new()) #.bonusDef(2000, 2000, 2000, 2000, 1700)
NpcCombatDef.add([1, 2, 3, 4], Man.new.respawnSeconds(10))
NpcCombatDef.add([172, 174, 3242, 3243, 3244, 3245, 4658, 4659, 4660, 4661], DarkWizard.new.respawnSeconds(10))
NpcCombatDef.add([941, 742], GreenDragon.new())
NpcCombatDef.add([5362], BrutalGreenDragon.new())
NpcCombatDef.add([55], BlueDragon.new())
NpcCombatDef.add([53], RedDragon.new())
NpcCombatDef.add([54], BlackDragon.new())
NpcCombatDef.add([50], KingBlackDragon.new())
NpcCombatDef.add([1590], BronzeDragon.new())
NpcCombatDef.add([1591], IronDragon.new())
NpcCombatDef.add([1592], SteelDragon.new())
NpcCombatDef.add([5363], MithrilDragon.new())
NpcCombatDef.add([2457,2884], Wallasalki.new())
NpcCombatDef.add([2455,2888], DagannothMelee.new())
NpcCombatDef.add([2456,2887], DagannothRange.new())
NpcCombatDef.add([2892, 2894, 2896], Spinolyp.new())
NpcCombatDef.add([2881], DagannothSupreme.new())
NpcCombatDef.add([2882], DagannothPrime.new())
NpcCombatDef.add([3200], ChaosElemental.new())
NpcCombatDef.add([3762,3764,3766,3768,3770], Defiler.new())
NpcCombatDef.add([3752, 3754, 3756, 3758, 3759], Torcher.new())
NpcCombatDef.add([1158], KalphiteQueenFirstForm.new())
NpcCombatDef.add([1160], KalphiteQueenSecondForm.new())
NpcCombatDef.add([914], BattleMageGuthix.new())
NpcCombatDef.add([912, 6221], BattleMageZamorak.new())
NpcCombatDef.add([913, 6257], BattleMageSaradomin.new())
NpcCombatDef.add([907], KolodionFirstForm.new())
NpcCombatDef.add([908], KolodionSecondForm.new())
NpcCombatDef.add([910], KolodionFourthForm.new())
NpcCombatDef.add([1046], MonkOfZamorak.new())
NpcCombatDef.add([1618], BloodVeld.new())
NpcCombatDef.add([1351, 1352, 1353, 1354, 1355, 1356], DagannothMother.new())
NpcCombatDef.add([1341, 1342, 1343], WeakDagannothMelee.new())
NpcCombatDef.add([1338, 1339, 1340], WeakDagannothRange.new())
NpcCombatDef.add([3068, 3069, 3070, 3071], SkeletalWyvern.new())
NpcCombatDef.add([1456, 1457, 1458], MonkeyArcher.new())
NpcCombatDef.add([1472], JungleDemon.new())