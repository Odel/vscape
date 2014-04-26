package com.rs2.model.content.combat.weapon;

/**
 *
 */
import static com.rs2.model.content.combat.weapon.AttackStyle.Bonus.CRUSH;
import static com.rs2.model.content.combat.weapon.AttackStyle.Bonus.RANGED;
import static com.rs2.model.content.combat.weapon.AttackStyle.Bonus.SLASH;
import static com.rs2.model.content.combat.weapon.AttackStyle.Bonus.STAB;
import static com.rs2.model.content.combat.weapon.AttackStyle.Mode.AGGRESSIVE;
import static com.rs2.model.content.combat.weapon.AttackStyle.Mode.CONTROLLED;
import static com.rs2.model.content.combat.weapon.AttackStyle.Mode.DEFENSIVE;
import static com.rs2.model.content.combat.weapon.AttackStyle.Mode.LONGRANGE;
import static com.rs2.model.content.combat.weapon.AttackStyle.Mode.MELEE_ACCURATE;
import static com.rs2.model.content.combat.weapon.AttackStyle.Mode.RANGED_ACCURATE;
import static com.rs2.model.content.combat.weapon.AttackStyle.Mode.RAPID;

import com.rs2.model.content.combat.AttackType;

public enum WeaponInterface {

	FISTS(5855, new int[]{5857, -1}, -1, -1, new AttackStyle[]{new AttackStyle(AttackType.MELEE, 22228, MELEE_ACCURATE, CRUSH), new AttackStyle(AttackType.MELEE, 22230, AGGRESSIVE, CRUSH), new AttackStyle(AttackType.MELEE, 22229, DEFENSIVE, CRUSH)}),

	WHIP(12290, new int[]{12293, 12291}, 12323, 12335, 48023, new AttackStyle[]{new AttackStyle(AttackType.MELEE, 48010, MELEE_ACCURATE, SLASH), new AttackStyle(AttackType.MELEE, 48009, CONTROLLED, SLASH), new AttackStyle(AttackType.MELEE, 48008, DEFENSIVE, SLASH)}),

	MAUL(425, new int[]{428, 426}, 7474, 7486, 29038, new AttackStyle[]{new AttackStyle(AttackType.MELEE, 1177, MELEE_ACCURATE, CRUSH), new AttackStyle(AttackType.MELEE, 1176, AGGRESSIVE, CRUSH), new AttackStyle(AttackType.MELEE, 1175, DEFENSIVE, CRUSH)}),

	TWO_HANDED(4705, new int[]{4708, 4706}, 7699, 7711, 30007, new AttackStyle[]{new AttackStyle(AttackType.MELEE, 18103, MELEE_ACCURATE, SLASH), new AttackStyle(AttackType.MELEE, 18106, AGGRESSIVE, SLASH), new AttackStyle(AttackType.MELEE, 18105, AGGRESSIVE, CRUSH), new AttackStyle(AttackType.MELEE, 18104, DEFENSIVE, SLASH)}),

	DAGGER(2276, new int[]{2279, 2277}, 7574, 7586, 29138, new AttackStyle[]{new AttackStyle(AttackType.MELEE, 8234, MELEE_ACCURATE, STAB), new AttackStyle(AttackType.MELEE, 8237, AGGRESSIVE, STAB), new AttackStyle(AttackType.MELEE, 8236, AGGRESSIVE, SLASH), new AttackStyle(AttackType.MELEE, 8235, DEFENSIVE, STAB)}),

	STAFF(328, new int[]{331, 329}, -1, -1, new AttackStyle[]{new AttackStyle(AttackType.MELEE, 1080, MELEE_ACCURATE, CRUSH), new AttackStyle(AttackType.MELEE, 1079, AGGRESSIVE, CRUSH), new AttackStyle(AttackType.MELEE, 1078, DEFENSIVE, CRUSH)}),

	PICKAXE(5570, new int[]{5573, 5571}, -1, -1, new AttackStyle[]{new AttackStyle(AttackType.MELEE, 21200, MELEE_ACCURATE, STAB), new AttackStyle(AttackType.MELEE, 21203, AGGRESSIVE, STAB), new AttackStyle(AttackType.MELEE, 21202, AGGRESSIVE, STAB), new AttackStyle(AttackType.MELEE, 21201, DEFENSIVE, STAB)}),

	AXE(1698, new int[]{1701, 1699}, 7499, 7511, 29063, new AttackStyle[]{new AttackStyle(AttackType.MELEE, 6168, MELEE_ACCURATE, SLASH), new AttackStyle(AttackType.MELEE, 6171, AGGRESSIVE, SLASH), new AttackStyle(AttackType.MELEE, 6170, AGGRESSIVE, CRUSH), new AttackStyle(AttackType.MELEE, 6169, DEFENSIVE, SLASH)}),

	HALBERD(8460, new int[]{8463, 8461}, 8493, 8505, 33033, new AttackStyle[]{new AttackStyle(AttackType.MELEE, 33018, CONTROLLED, STAB), new AttackStyle(AttackType.MELEE, 33020, AGGRESSIVE, SLASH), new AttackStyle(AttackType.MELEE, 33019, DEFENSIVE, STAB)}),

	CLAWS(7762, new int[]{7765, 7763}, 7800, 7812, 30108, new AttackStyle[]{new AttackStyle(AttackType.MELEE, 30088, MELEE_ACCURATE, SLASH), new AttackStyle(AttackType.MELEE, 30091, AGGRESSIVE, SLASH), new AttackStyle(AttackType.MELEE, 30090, CONTROLLED, STAB), new AttackStyle(AttackType.MELEE, 30089, DEFENSIVE, SLASH)}),

	SPEAR(4679, new int[]{4682, 4680}, 7674, 7686, 29238, new AttackStyle[]{new AttackStyle(AttackType.MELEE, 18077, CONTROLLED, STAB), new AttackStyle(AttackType.MELEE, 18080, CONTROLLED, SLASH), new AttackStyle(AttackType.MELEE, 18079, CONTROLLED, CRUSH), new AttackStyle(AttackType.MELEE, 18078, DEFENSIVE, STAB)}),

	MACE(3796, new int[]{3799, 3797}, 7624, 7636, 29188, new AttackStyle[]{new AttackStyle(AttackType.MELEE, 14218, MELEE_ACCURATE, CRUSH), new AttackStyle(AttackType.MELEE, 14221, AGGRESSIVE, CRUSH), new AttackStyle(AttackType.MELEE, 14220, CONTROLLED, STAB), new AttackStyle(AttackType.MELEE, 14219, DEFENSIVE, CRUSH)}),

	SCYTHE(776, new int[]{779, 777}, -1, -1, new AttackStyle[]{new AttackStyle(AttackType.MELEE, 3014, MELEE_ACCURATE, SLASH), new AttackStyle(AttackType.MELEE, 3017, AGGRESSIVE, STAB), new AttackStyle(AttackType.MELEE, 3016, AGGRESSIVE, CRUSH), new AttackStyle(AttackType.MELEE, 3015, DEFENSIVE, SLASH)}),

	SHORT_BOW(1749, new int[]{1752, 1750}, 7524, 7536, 29088, new AttackStyle[]{new AttackStyle(AttackType.RANGED, 6221, RANGED_ACCURATE, RANGED), new AttackStyle(AttackType.RANGED, 6220, RAPID, RANGED), new AttackStyle(AttackType.RANGED, 6219, LONGRANGE, RANGED)}),

	LONG_BOW(1764, new int[]{1767, 1765}, 7549, 7561, 29113, new AttackStyle[]{new AttackStyle(AttackType.RANGED, 6236, RANGED_ACCURATE, RANGED), new AttackStyle(AttackType.RANGED, 6235, RAPID, RANGED), new AttackStyle(AttackType.RANGED, 6234, LONGRANGE, RANGED)}),

	THROWING(4446, new int[]{4449, 4447}, 7649, 7661, new AttackStyle[]{new AttackStyle(AttackType.RANGED, 17102, RANGED_ACCURATE, RANGED), new AttackStyle(AttackType.RANGED, 17101, RAPID, RANGED), new AttackStyle(AttackType.RANGED, 17100, LONGRANGE, RANGED)}),

	LONGSWORD(2423, new int[]{2426, 2424}, 7599, 7611, 29163, new AttackStyle[]{new AttackStyle(AttackType.MELEE, 9125, MELEE_ACCURATE, SLASH), new AttackStyle(AttackType.MELEE, 9128, AGGRESSIVE, SLASH), new AttackStyle(AttackType.MELEE, 9127, CONTROLLED, STAB), new AttackStyle(AttackType.MELEE, 9126, DEFENSIVE, SLASH)});

	private int interfaceId, specialBarId, specialTextId, specialBarButtonId;
	private int[] interfaceData;
	private AttackStyle[] attackStyles;

	WeaponInterface(int interfaceId, int[] interfaceData, int specialBarId, int specialTextId, int specialBarButtonId, AttackStyle[] attackStyles) {
		this.interfaceId = interfaceId;
		this.interfaceData = interfaceData;
		this.specialBarId = specialBarId;
		this.specialBarButtonId = specialBarButtonId;
		this.specialTextId = specialTextId;
		this.attackStyles = attackStyles;
	}

	WeaponInterface(int interfaceId, int[] interfaceData, int specialBarId, int specialTextId, AttackStyle[] attackStyles) {
		this(interfaceId, interfaceData, specialBarId, specialTextId, -1, attackStyles);
	}

	public int getSpecialBarButtonId() {
		return specialBarButtonId;
	}

	public int getInterfaceId() {
		return interfaceId;
	}

	public int getSpecialBarId() {
		return specialBarId;
	}

	public int weaponNameChild() {
		return interfaceData[0];
	}

	public int weaponDisplayChild() {
		return interfaceData[1];
	}

	public int[] getInterfaceData() {
		return interfaceData;
	}

	public int getSpecialTextId() {
		return specialTextId;
	}

	public AttackStyle[] getAttackStyles() {
		return attackStyles;
	}
}
