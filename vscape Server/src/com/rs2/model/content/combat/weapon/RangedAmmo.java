package com.rs2.model.content.combat.weapon;

import com.rs2.Constants;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;

/**
 *
 */
public enum RangedAmmo {

	/* Arrows */
	BRONZE_ARROW(10, 19, 7), IRON_ARROW(9, 18, 10), STEEL_ARROW(11, 20, 16), MITHRIL_ARROW(12, 21, 22), ADAMANT_ARROW(13, 22, 31), RUNE_ARROW(15, 24, 49),

	ICE_ARROWS(16, 25, 16),

	BRONZE_BRUTAL(15, 24, 11), IRON_BRUTAL(9, 18, 13), STEEL_BRUTAL(11, 20, 19), MITHRIL_BRUTAL(12, 21, 34), ADAMANT_BRUTAL(13, 22, 45), RUNE_BRUTAL(15, 24, 60),

	BRONZE_FIRE_ARROW(17, 26, 7), IRON_FIRE_ARROW(17, 26, 10), STEEL_FIRE_ARROW(17, 26, 16), MITHRIL_FIRE_ARROW(17, 26, 22), ADAMANT_FIRE_ARROW(17, 26, 31), RUNE_FIRE_ARROW(17, 26, 49),

	OGRE_ARROW(242, 243, 22),

	BROAD_ARROW(326, 325, 28),

	/* Throwing knives */
	BRONZE_KNIFE(212, 219, 3), IRON_KNIFE(213, 220, 4), STEEL_KNIFE(214, 221, 7), BLACK_KNIFE(215, 222, 8), MITHRIL_KNIFE(216, 223, 10), ADAMANT_KNIFE(217, 224, 14), RUNE_KNIFE(218, 225, 24),

	/* Throwing axes */
	BRONZE_THROWNAXE(35, 43, 5), IRON_THROWNAXE(36, 42, 7), STEEL_THROWNAXE(37, 44, 11), MITHRIL_THROWNAXE(38, 45, 16), ADAMANT_THROWNAXE(39, 46, 23), RUNE_THROWNAXE(41, 48, 26),

	/* Throwing darts */
	BRONZE_DART(226, 232, 31), IRON_DART(227, 233, 3), STEEL_DART(228, 234, 4), BLACK_DART(34, 273, 6), MITHRIL_DART(229, 235, 7), ADAMANT_DART(230, 236, 10), RUNE_DART(231, 237, 14),

	/* Throwing javelins */
	BRONZE_JAVELIN(200, 206, 6), IRON_JAVELIN(201, 207, 10), STEEL_JAVELIN(202, 208, 12), MITHRIL_JAVELIN(203, 209, 18), ADAMANT_JAVELIN(204, 210, 28), RUNE_JAVELIN(205, 211, 42),

	/* Karils bolt racks */
	BOLT_RACK(27, 28, 55),

	/* Crossbow Bolts */
	BOLTS(27, 28, 10), BARBED_BOLTS(27, 28, 12), OPAL_BOLTS(27, 28, 14), PEARL_BOLTS(27, 28, 48),
	
	/* Obby rings */
	TOKTZ(442, -1, 49);

	static RangedAmmo[] ARROW_AMMO = {BRONZE_ARROW, IRON_ARROW, STEEL_ARROW, MITHRIL_ARROW, ADAMANT_ARROW, RUNE_ARROW, ICE_ARROWS, BROAD_ARROW};
	static RangedAmmo[] KNIFE_AMMO = {BRONZE_KNIFE, IRON_KNIFE, STEEL_KNIFE, BLACK_KNIFE, MITHRIL_KNIFE, ADAMANT_KNIFE, RUNE_KNIFE};
	static RangedAmmo[] DART_AMMO = {BRONZE_DART, IRON_DART, STEEL_DART, BLACK_DART, MITHRIL_DART, ADAMANT_DART, RUNE_DART};
	static RangedAmmo[] BRUTAL_AMMO = {BRONZE_BRUTAL, IRON_BRUTAL, STEEL_BRUTAL, MITHRIL_BRUTAL, ADAMANT_BRUTAL, RUNE_BRUTAL};
	static RangedAmmo[] THROWNAXE_AMMO = {BRONZE_THROWNAXE, IRON_THROWNAXE, STEEL_THROWNAXE, MITHRIL_THROWNAXE, ADAMANT_THROWNAXE, RUNE_THROWNAXE};
	static RangedAmmo[] JAVELIN_AMMO = {BRONZE_JAVELIN, IRON_JAVELIN, STEEL_JAVELIN, MITHRIL_JAVELIN, ADAMANT_JAVELIN, RUNE_JAVELIN};

	private final int projectileId, graphicId, rangeStrength;

	RangedAmmo(int projectileId, int graphicId, int rangeStrength) {
		this.graphicId = graphicId;
		this.projectileId = projectileId;
		this.rangeStrength = rangeStrength;
	}

	public int getRangeStrength() {
		return rangeStrength;
	}

	public int getProjectileId() {
		return projectileId;
	}

	public int getGraphicId() {
		return graphicId;
	}

	public static RangedAmmo getRangedAmmo(Player player, Weapon weapon, boolean sendMessage) {
		RangedAmmoType ammoType = weapon.getAmmoType();
		if (ammoType == null) {
			player.getActionSender().sendMessage("That weapon is not configured properly, please report to staff!");
			return null;
		}
		int equipmentSlot = ammoType.getEquipmentSlot();
		Item ammoItem = player.getEquipment().getItemContainer().get(equipmentSlot);
		if (ammoItem == null) {
			if (sendMessage)
				player.getActionSender().sendMessage(CombatManager.NO_AMMO_MESSAGE);
			return null;
		}
		String ammoName = ItemDefinition.forId(ammoItem.getId()).getName().toLowerCase().replaceAll(" ", "_");
		RangedAmmo[] rangedAmmos = ammoType.getRangedAmmos();
		for (RangedAmmo ammo : rangedAmmos) {
			if (ammoName.contains(ammo.name().toLowerCase())) {
				if (equipmentSlot != Constants.WEAPON) {
					Item weaponItem = player.getEquipment().getItemContainer().get(Constants.WEAPON);
					if (weaponItem == null)
						return null;
					boolean wrongAmmo = true;
					if (ammoName.contains("ogre")) {
						String weaponName = player.getEquippedWeapon().name().toLowerCase();
						wrongAmmo = !weaponName.contains("ogre");
					} else {
						player.getEquipment().getRequirements(weaponItem.getId());
						int weaponRequiredLevel = player.getEquipment().getRangeLevelReq();
						player.getEquipment().getRequirements(ammoItem.getId());
						int ammoRequiredLevel = player.getEquipment().getRangeLevelReq();
						wrongAmmo = ammoRequiredLevel > weaponRequiredLevel;
					}
					if (wrongAmmo) {
						if (sendMessage)
							player.getActionSender().sendMessage(CombatManager.AMMO_COMPATIBILITY_MESSAGE);
						return null;
					}
				}
				return ammo;
			}
		}
		if (sendMessage)
			player.getActionSender().sendMessage(CombatManager.WRONG_AMMO_MESSAGE);
		return null;
	}

}
