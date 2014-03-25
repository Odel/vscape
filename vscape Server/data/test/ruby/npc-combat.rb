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

class Man < NpcCombatDef
    def attackScripts attacker, victim
        return [BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, 2, Weapon::FISTS)]
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
                BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 14, 4, 2628),
                BasicAttack.projectileAttack(attacker, victim, AttackType::RANGED, AttackStyle::Mode::LONGRANGE, 17, 4, 2633, Graphic.new(-1, 0), Graphic.new(-1, 0), 443, ProjectileTrajectory.ARROW)
        ];
    end
end

class MejKot < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 28, 5, 2637)
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
                BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 1, 8, 2655),
                BasicAttack.projectileAttack(attacker, victim, AttackType::RANGED, AttackStyle::Mode::LONGRANGE, 1, 8, 2652, Graphic.new(-1, 0), Graphic.new(451, 0), 411, ProjectileTrajectory.JAD_RANGE),
                BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::MAGIC, 1, 8, 2656, Graphic.new(447, 250), Graphic.new(157, 0), 448, ProjectileTrajectory.JAD_SPELL)
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

NpcCombatDef.add([2746], YtHurkot.new.bonusDef(1000, 1000, 1000, 1000, 600))
NpcCombatDef.add([2631], TokXil.new.bonusDef(600, 600, 600, 600, 300))
NpcCombatDef.add([2741], MejKot.new.bonusDef(1000, 1000, 1000, 1000, 600))
NpcCombatDef.add([2743], KetZek.new.bonusDef(1300, 1300, 1300, 1300, 700))
NpcCombatDef.add([2745], Jad.new.bonusDef(2000, 2000, 2000, 2000, 1700))
NpcCombatDef.add([1, 2, 3, 4], Man.new.respawnSeconds(10))
NpcCombatDef.add([174], DarkWizard.new.respawnSeconds(10))