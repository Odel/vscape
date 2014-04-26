package com.rs2.util;

import com.rs2.model.content.combat.AttackType;
import com.rs2.model.content.combat.weapon.AttackStyle;
import com.rs2.model.content.combat.weapon.RangedAmmo;
import com.rs2.model.content.combat.weapon.RangedAmmoType;
import com.rs2.model.content.skills.magic.Spell;

/**
 *
 */
public class CombatAPIGenerator {

	public static void main(String args[]) {

		System.out.println("Attack Types:");
		for (AttackType type : AttackType.values())
			System.out.println(type.name());
		System.out.println();

		System.out.println("Attack Modes:");
		for (AttackStyle.Mode mode : AttackStyle.Mode.values())
			System.out.println(mode.name());
		System.out.println();

		System.out.println("Attack Bonuses:");
		for (AttackStyle.Bonus bonus : AttackStyle.Bonus.values())
			System.out.println(bonus.name());
		System.out.println();

		System.out.println("Spells:");
		for (Spell spell : Spell.values()) {
			if (spell.isCombatSpell())
				System.out.println(spell.name());
		}
		System.out.println();

		System.out.println("Ammo Types");
		for (RangedAmmoType ammoType : RangedAmmoType.values())
			System.out.println(ammoType.name());
		System.out.println();

		System.out.println("Ammo Variants:");
		for (RangedAmmo ammo : RangedAmmo.values())
			System.out.println(ammo.name());
		System.out.println();

	}

}
