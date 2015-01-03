package com.rs2.model.content.combat.weapon;

import com.rs2.Constants;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;

/**
 *
 */
public enum Weapon {

	// movementAnimations are { stand, walk, run }
	FISTS(WeaponInterface.FISTS, 4, new int[]{422, 423, 422}, Constants.MOVEMENT_ANIMS, 424), 
	SHORT_BOW(WeaponInterface.SHORT_BOW, RangedAmmoType.ARROW, 4, new int[]{426, 426, 426}, Constants.MOVEMENT_ANIMS, 424),
	MONKEY_BOW(WeaponInterface.SHORT_BOW, RangedAmmoType.MONKEY_ARROW, 4, new int[]{1394, 1394, 1394}, new int[]{1386, 1380, 1380}, 1395),
    SPECIAL_BOW(WeaponInterface.SHORT_BOW, RangedAmmoType.ARROW, 5, new int[]{426, 426, 426}, Constants.MOVEMENT_ANIMS, 424),
	CRYSTAL_BOW(WeaponInterface.SHORT_BOW, RangedAmmoType.CRYSTAL, 5, new int[]{426, 426, 426}, Constants.MOVEMENT_ANIMS, 424),
    OGRE_BOW(WeaponInterface.SHORT_BOW, RangedAmmoType.OGRE, 5, new int[]{426, 426, 426}, Constants.MOVEMENT_ANIMS, 424),
    OGRE_COMP_BOW(WeaponInterface.SHORT_BOW, RangedAmmoType.BRUTAL, 5, new int[]{426, 426, 426}, Constants.MOVEMENT_ANIMS, 424),
    LONG_BOW(WeaponInterface.LONG_BOW, RangedAmmoType.ARROW, 5, new int[]{426, 426, 426}, Constants.MOVEMENT_ANIMS, 424),
    DARK_BOW(WeaponInterface.LONG_BOW, RangedAmmoType.DARK_BOW, 7, new int[]{426, 426, 426}, Constants.MOVEMENT_ANIMS, 424),
    WAND(WeaponInterface.STAFF, 5, new int[]{406, 407, 408}, new int[]{809, 1146, 1210}, 435),
    STAFF(WeaponInterface.STAFF, 5, new int[]{406, 406, 406}, new int[]{809, 1146, 1210}, 435),
    SPECSTAFF(WeaponInterface.STAFF, 4, new int[]{406, 407, 408}, new int[]{809, 1146, 1210}, 435),
    THROWING_KNIFE (WeaponInterface.THROWING, RangedAmmoType.KNIFE, 3, new int[]{806, 806, 806}, Constants.MOVEMENT_ANIMS, 424),
    THROWING_DART(WeaponInterface.THROWING, RangedAmmoType.DART, 3, new int[]{806, 806, 806}, Constants.MOVEMENT_ANIMS, 424),
    JAVELIN(WeaponInterface.THROWING, RangedAmmoType.JAVELIN, 5, new int[]{806, 806, 806}, Constants.MOVEMENT_ANIMS, 424),
    THROWING_AXE(WeaponInterface.THROWING, RangedAmmoType.THROWNAXE, 4, new int[]{806, 806, 806}, Constants.MOVEMENT_ANIMS, 424),
    HALBERD(WeaponInterface.HALBERD, 7, new int[]{2080, 2078, 2082}, new int[]{809, 1146, 1210}, 435),
    MACE(WeaponInterface.MACE, 5, new int[]{401, 401, 400, 401}, Constants.MOVEMENT_ANIMS, 424),
    SPEAR(WeaponInterface.SPEAR, 5, new int[]{2080, 2081, 2082, 2080}, new int[]{809, 1146, 1210}, 435),
    HASTA(WeaponInterface.SPEAR, 7, new int[]{400, 2081, 2082, 400}, new int[]{809, 1146, 1210}, 387),
    DRAGON_DAGGER(WeaponInterface.DAGGER, null, 4, new int[]{402, 402, 451, 400}, Constants.MOVEMENT_ANIMS, 403),
    DAGGER(WeaponInterface.DAGGER, 4, new int[]{386, 386, 390, 386}, Constants.MOVEMENT_ANIMS, 404),
    TWO_HANDED(WeaponInterface.TWO_HANDED, 6, new int[]{7048, 7041, 7042, 7049}, new int[]{7047, 7046, 7039}, 7050),
    PICKAXE(WeaponInterface.PICKAXE, 5, new int[]{395, 400, 401, 395}, Constants.MOVEMENT_ANIMS, 410),
    AXE(WeaponInterface.AXE, 6, new int[]{395, 395, 401, 395}, Constants.MOVEMENT_ANIMS, 404),
    LONGSWORD(WeaponInterface.LONGSWORD, 5, new int[]{451, 451, 412, 451}, new int[]{809, 1146, 1210}, 404),
    SCIMITAR(WeaponInterface.LONGSWORD, 4, new int[]{390, 390, 412, 390}, Constants.MOVEMENT_ANIMS, 404),
    SWORD(WeaponInterface.DAGGER, 4, new int[]{412, 412, 451, 412}, Constants.MOVEMENT_ANIMS, 404),
    KARILS_CROSSBOW(WeaponInterface.SHORT_BOW, RangedAmmoType.KARILS_BOLT, 4, new int[]{2075, 2075, 2075}, new int[]{2074, 2076, 2077}, 1666),
    CROSSBOW(WeaponInterface.SHORT_BOW, RangedAmmoType.BOLTS, 6, new int[]{427, 427, 427}, Constants.MOVEMENT_ANIMS, 404),
    CBOW(WeaponInterface.SHORT_BOW, RangedAmmoType.BOLTS, 6, new int[]{4230, 4230, 4230}, Constants.MOVEMENT_ANIMS, 404),
    DHAROKS(WeaponInterface.AXE, 7, new int[]{2067, 2067, 2066, 2067}, new int[]{2065, 1663, 1664}, 1666),
    TORAGS(WeaponInterface.MAUL, 5, new int[]{2068, 2068, 2068}, Constants.MOVEMENT_ANIMS, 424),
    AHRIMS(WeaponInterface.STAFF, 4, new int[]{406, 407, 408}, new int[]{809, 1146, 1210}, 435),
    VERACS(WeaponInterface.MACE, 5, new int[]{2062, 2062, 2062, 2062}, new int[]{1832, 1830, 1831}, 2063),
    GRANITE_MAUL(WeaponInterface.MAUL, 7, new int[]{1665, 1665, 1665}, new int[]{1662, 1663, 1664}, 1666),
    WHIP(WeaponInterface.WHIP, null, 4, new int[]{1658, 1658, 1658}, new int[]{1832, 1660, 1661}, 1659),
    OBBY_RING(WeaponInterface.THROWING, RangedAmmoType.OBBY_RING, 4, new int[]{3353, 3353, 3353}, Constants.MOVEMENT_ANIMS, 2063),
    OBBY_MAUL(WeaponInterface.MAUL, 7, new int[]{2661, 2661, 2661}, new int[]{2065, 2064, 1664}, 1666),
    OBBY_SWORD_AND_KNIFE(WeaponInterface.LONGSWORD, 4, new int[]{412, 401, 451, 401}, Constants.MOVEMENT_ANIMS, 424),
    OBBY_MACE(WeaponInterface.MACE, 5, new int[]{401, 401, 400, 401}, Constants.MOVEMENT_ANIMS, 424),
    OBBY_STAFF(WeaponInterface.STAFF, 5, new int[]{406, 407, 408}, Constants.MOVEMENT_ANIMS, 410),
    WARHAMMER(WeaponInterface.MAUL, 6, new int[]{401, 401, 400}, Constants.MOVEMENT_ANIMS, 424),
    CLAWS(WeaponInterface.CLAWS, 4, new int[]{393, 393, 1067, 393}, Constants.MOVEMENT_ANIMS, 424),
    SCYTHE(WeaponInterface.SCYTHE, 7, new int[]{440, 440, 440, 440}, Constants.MOVEMENT_ANIMS, 435);

	private WeaponInterface weaponInterface;
	private RangedAmmoType ammoType;
	private int[] attackAnimations, movementAnimations;
	private int blockAnimation, attackDelay;

	Weapon(WeaponInterface weaponInterface, RangedAmmoType ammoType, int attackDelay, int[] attackAnimations, int[] movementAnimations, int blockAnimation) {
		this.weaponInterface = weaponInterface;
		this.ammoType = ammoType;
		this.attackDelay = attackDelay;
		this.attackAnimations = attackAnimations;
		this.movementAnimations = movementAnimations;
		if (this.movementAnimations.length != 3)
			throw new IllegalArgumentException("There must be 3 elements in movementAnimations array. [stand, walk, run]");
		this.blockAnimation = blockAnimation;
		if (weaponInterface.getAttackStyles().length != attackAnimations.length)
			throw new IllegalArgumentException("There are not the same amount of animations as attack styles for weapon: " + name());
	}

	Weapon(WeaponInterface weaponInterface, int attackDelay, int[] attackAnimations, int[] movementAnimations, int blockAnimation) {
		this(weaponInterface, null, attackDelay, attackAnimations, movementAnimations, blockAnimation);
	}

	public WeaponInterface getWeaponInterface() {
		return weaponInterface;
	}

	public int[] getAttackAnimations() {
		return attackAnimations;
	}

	public int[] getMovementAnimations() {
		return movementAnimations;
	}

	public int getAttackDelay() {
		return attackDelay;
	}

	public int getBlockAnimation() {
		return blockAnimation;
	}

	public RangedAmmoType getAmmoType() {
		return ammoType;
	}

	public static Weapon getWeapon(Item weaponItem) {
		if (weaponItem == null)
			return FISTS;
		String name = ItemDefinition.forId(weaponItem.getId()).getName().toLowerCase();
		if (name.contains("dragon dagger") || name.contains("drag dagger"))
			return DRAGON_DAGGER;
		if (name.contains("dagger") || name.toLowerCase().contains("wolfbane")) { // Contains, because they might be poisonous.
			return Weapon.DAGGER;
		} else if (name.contains("longsword") || name.contains("darklight") || name.contains("silverlight") || name.contains("excalibur") || name.contains("machete") || name.contains("fremennik blade")) {
			return Weapon.LONGSWORD;
		} else if (name.endsWith("2h sword") || name.contains("2h") || name.contains("godsword")) {
			return Weapon.TWO_HANDED;
		} else if (name.endsWith("granite maul")) {
			return Weapon.GRANITE_MAUL;
		} else if (name.endsWith("sword")) {
			return Weapon.SWORD;
		} else if (name.endsWith("scimitar") || name.endsWith("machete")) {
			return Weapon.SCIMITAR;
		} else if (name.endsWith("mace")) {
			return Weapon.MACE;
		} else if (name.startsWith("dharoks")) {
			return Weapon.DHAROKS;
		} else if (name.endsWith("thrownaxe")) { // Also pickaxes and thrown
													// axes. ;)
			return Weapon.THROWING_AXE;
		} else if (name.endsWith("axe") || name.endsWith("battleaxe")) { // Also pickaxes and thrown axes. ;)
			return Weapon.AXE;
		} else if (name.endsWith("warhammer") || name.equalsIgnoreCase("flowers") || name.equalsIgnoreCase("chicken")) {
			return Weapon.WARHAMMER;
		} else if (name.contains("hasta")) {
			return Weapon.HASTA;
		} else if (name.contains("spear") || name.contains("mjolnir") || name.contains("banner")) {
			return Weapon.SPEAR;
		} else if (name.contains("claw")) {
			return Weapon.CLAWS;
		} else if (name.endsWith("halberd")) {
			return Weapon.HALBERD;
			/*
			 * Start of TzHaar items.
			 */
		} else if (name.equalsIgnoreCase("toktz-xil-ak") || name.equalsIgnoreCase("toktz-xil-ek")) {
			return Weapon.OBBY_SWORD_AND_KNIFE;
		} else if (name.equalsIgnoreCase("tzhaar-ket-em")) {// Mace
			return Weapon.OBBY_MACE;
		} else if (name.equalsIgnoreCase("toktz-mej-tal")) {// Staff
			return Weapon.OBBY_STAFF;
		} else if (name.equalsIgnoreCase("tzhaar-ket-om")) {// Maul
			return Weapon.OBBY_MAUL;
		} else if (name.equalsIgnoreCase("toktz-xil-ul")) {// Rings
			return Weapon.OBBY_RING;
			/*
			 * End of TzHaar items.
			 */
		} else if (name.equalsIgnoreCase("abyssal whip")) {
			return Weapon.WHIP;
		} else if (name.startsWith("torags")) {
			return Weapon.TORAGS;
		} else if (name.startsWith("veracs")) {
			return Weapon.VERACS;
		} else if (name.contains("wand")) {
			return Weapon.WAND;
		} else if (name.contains("staff") && ((weaponItem.getId() <= 1410) || (weaponItem.getId() >= 3053 && weaponItem.getId() <= 3056) || (weaponItem.getId() >= 6562 && weaponItem.getId() <= 6727))) {
			return Weapon.STAFF;
		} else if (weaponItem.getId() == 2415 || weaponItem.getId() == 2416 || weaponItem.getId() == 2417 || weaponItem.getId() == 4170 || weaponItem.getId() == 4675) {
			return Weapon.SPECSTAFF;
		} else if (name.contains("ahrims")) {
			return Weapon.AHRIMS;
		} else if (name.contains("longbow")) {
			return Weapon.LONG_BOW;
		} else if (name.contains("pickaxe")) {
			return Weapon.PICKAXE;
		} else if (name.contains("shortbow")) {
			return Weapon.SHORT_BOW;
		} else if (weaponItem.getId() == 11235) {
			return Weapon.DARK_BOW;
		} else if (name.contains("comp bow") || name.contains("seercull")) {
			return Weapon.SPECIAL_BOW;
		} else if (name.contains("crystal bow")) {
			 return Weapon.CRYSTAL_BOW;
		} else if (name.contains("ogre")) {
			if (name.startsWith("comp")) {
				return Weapon.OGRE_COMP_BOW;
			} else {
				return Weapon.OGRE_BOW;
			}
		} else if ((name.startsWith("karils"))) {
			return Weapon.KARILS_CROSSBOW;
		} else if ((name.contains("fox"))) {
			return Weapon.SCIMITAR;
		} else if ((name.contains("chicken"))) {
			return Weapon.FISTS;
		} else if (name.contains("crossbow")) {// Also Dorgeshuun.
			return Weapon.CROSSBOW;
		} else if (name.contains("c'bow")) { // 474 
			return Weapon.CBOW;
		} else if (name.contains("knife")) {
			return Weapon.THROWING_KNIFE;
		} else if (name.contains("dart")) {
			return Weapon.THROWING_DART;
		} else if (name.contains("javelin")) {
			return Weapon.JAVELIN;
		} else if (name.contains("scythe")) {
			return Weapon.SCYTHE;
		}
		return FISTS;
	}
}
