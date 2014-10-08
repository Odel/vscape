package com.rs2.model.content.skills.Crafting;

public class GemData {

	public static int[] getGemSlot() {
		return gemSlot;
	}
	public static int[] gemSlot = {2357, 1607, 1605, 1603, 1601, 1615, 6573};
	public static String[] interfaceMessage = {"You need a ring mould to craft rings.", "You need a necklace mould to craft necklaces.", "You need a amulet mould to craft amulets.", "You need a bracelet mould to craft bracelets."};
	public static int[][] interfaceFrames = {{4229, 4233}, {4235, 4239}, {4241, 4245}, {24153, 24159}};
	public static final int GEM_SLOT = 0, GEM_CUT_LEVEL = 1, GEM_CUT_EXP = 2, GEM_CRAFT_RING_LEVEL = 3, GEM_CRAFT_RING_EXP = 4, GEM_CRAFT_RING_FINAL_PRODUCT = 5,
			GEM_CRAFT_NECKLACE_LEVEL = 6, GEM_CRAFT_NECKLACE_EXP = 7, GEM_CRAFT_NECKLACE_FINAL_PRODUCT = 8,
			GEM_CRAFT_AMULET_LEVEL = 9, GEM_CRAFT_AMULET_EXP = 10, GEM_CRAFT_AMULET_MID_PRODUCT = 11, GEM_CRAFT_AMULET_FINAL_PRODUCT = 12,
			GEM_CRAFT_BRACELET_LEVEL = 13, GEM_CRAFT_BRACELET_EXP = 14, GEM_CRAFT_BRACELET_FINAL_PRODUCT = 15;

	public static int[][] stringItems = {{1673, 1692}, {1675, 1694}, {1677, 1696}, {1679, 1698}, {1681, 1700}, {1683, 1702}, {6579, 6581}, {1714, 1716}, {1720, 1722}};
	public static int[][] craftInterfaceArray = {{1635, 1637, 1639, 1641, 1643, 1645, 6575}, {1654, 1656, 1658, 1660, 1662, 1664, 6577}, {1692, 1694, 1696, 1698, 1700, 1702, 6581}, {11069, 11072, 11076, 11085, 11092, 11115, 11130}};
	public static int[] moulds = {1592, 1597, 1595, 11065};
}