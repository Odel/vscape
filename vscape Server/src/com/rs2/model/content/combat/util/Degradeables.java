package com.rs2.model.content.combat.util;

import com.rs2.Constants;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;
import java.util.ArrayList;

public enum Degradeables {
    
    AHRIMS_HOOD(4708, 4856, 4857, 4858, 4859, 4860, Constants.HAT, 0),
    AHRIMS_TOP(4712, 4868, 4869, 4870, 4871, 4872, Constants.CHEST, 1),
    AHRIMS_SKIRT(4714, 4874, 4875, 4876, 4877, 4878, Constants.LEGS, 2),
    AHRIMS_STAFF(4710, 4862, 4863, 4864, 4865, 4866, Constants.WEAPON, 3),
    
    DHAROKS_HELM(4716, 4880, 4881, 4882, 4883, 4884, Constants.HAT, 4),
    DHAROKS_PLATE(4720, 4892, 4893, 4894, 4895, 4896, Constants.CHEST, 5),
    DHAROKS_LEGS(4722, 4898, 4899, 4900, 4901, 4902, Constants.LEGS, 6),
    DHAROKS_AXE(4718, 4886, 4887, 4888, 4889, 4890, Constants.WEAPON, 7),
    
    TORAGS_HELM(4745, 4952, 4953, 4954, 4955, 4956, Constants.HAT, 8),
    TORAGS_PLATE(4749, 4964, 4965, 4966, 4967, 4968, Constants.CHEST, 9),
    TORAGS_LEGS(4751, 4970, 4971, 4972, 4973, 4974, Constants.LEGS, 10),
    TORAGS_HAMMERS(4747, 4958, 4959, 4960, 4961, 4962, Constants.WEAPON, 11),
    
    GUTHANS_HELM(4724, 4904, 4905, 4906, 4907, 4908, Constants.HAT, 12),
    GUTHANS_PLATE(4728, 4916, 4917, 4918, 4919, 4920, Constants.CHEST, 13),
    GUTHANS_SKIRT(4730, 4922, 4923, 4924, 4925, 4926, Constants.LEGS, 14),
    GUTHANS_WARSPEAR(4726, 4910, 4911, 4912, 4913, 4914, Constants.WEAPON, 15),
    
    KARILS_COIF(4732, 4928, 4929, 4930, 4931, 4932, Constants.HAT, 16),
    KARILS_TOP(4736, 4940, 4941, 4942, 4943, 4944, Constants.CHEST, 17),
    KARILS_SKIRT(4738, 4946, 4947, 4948, 4949, 4950, Constants.LEGS, 18),
    KARILS_CROSSBOW(4734, 4934, 4935, 4936, 4937, 4938, Constants.WEAPON, 19),
    
    VERACS_HELM(4753, 4976, 4977, 4978, 4980, 4981, Constants.HAT, 20),
    VERACS_TOP(4757, 4988, 4989, 4990, 4991, 4992, Constants.CHEST, 21),
    VERACS_SKIRT(4759, 4994, 4995, 4996, 4997, 4998, Constants.LEGS, 22),
    VERACS_FLAIL(4755, 4982, 4983, 4984, 4985, 4986, Constants.WEAPON, 23),

    CRYSTAL_BOW(4212, 4214, 4215, 4216, 4217, 4218, 4219, 4220, 4221, 4222, 4223, 4207, Constants.WEAPON, 24),
    CRYSTAL_SHIELD(4224, 4225, 4226, 4227, 4228, 4229, 4230, 4231, 4232, 4233, 4234, 4211, Constants.SHIELD, 25);
    
    private final int originalId, firstDegradeId, secondDegradeId, thirdDegradeId, fourthDegradeId, fifthDegradeId, sixthDegradeId, seventhDegradeId, eigthDegradeId, ninthDegradeId, tenthDegradeId, brokenId, equipSlot, playerArraySlot;
    public static final int DEGRADE_HITS = 2500;
    
    Degradeables(int originalId, int firstDegradeId, int secondDegradeId, int thirdDegradeId, int fourthDegradeId, int brokenId, int equipSlot, int playerArraySlot) {
	this.originalId = originalId;
	this.firstDegradeId = firstDegradeId;
	this.secondDegradeId = secondDegradeId;
	this.thirdDegradeId = thirdDegradeId;
	this.fourthDegradeId = fourthDegradeId;
	this.fifthDegradeId = -1;
	this.sixthDegradeId = -1;
	this.seventhDegradeId = -1;
	this.eigthDegradeId = -1;
	this.ninthDegradeId = -1;
	this.tenthDegradeId = -1;
	this.brokenId = brokenId;
	this.equipSlot = equipSlot;
	this.playerArraySlot = playerArraySlot;
    }
    
    Degradeables(int originalId, int firstDegradeId, int secondDegradeId, int thirdDegradeId, int fourthDegradeId, int fifthDegradeId, int sixthDegradeId, int seventhDegradeId, int eigthDegradeId, int ninthDegradeId, int tenthDegradeId, int brokenId, int equipSlot, int playerArraySlot) {
	this.originalId = originalId;
	this.firstDegradeId = firstDegradeId;
	this.secondDegradeId = secondDegradeId;
	this.thirdDegradeId = thirdDegradeId;
	this.fourthDegradeId = fourthDegradeId;
	this.fifthDegradeId = fifthDegradeId;
	this.sixthDegradeId = sixthDegradeId ;
	this.seventhDegradeId = seventhDegradeId ;
	this.eigthDegradeId = eigthDegradeId;
	this.ninthDegradeId = ninthDegradeId;
	this.tenthDegradeId = tenthDegradeId;
	this.brokenId = brokenId;
	this.equipSlot = equipSlot;
	this.playerArraySlot = playerArraySlot;
    }
    
    public int getOriginalId() {
	return originalId;
    }
    public int getFirstDegradeId() {
	return firstDegradeId;
    }
    public int getSecondDegradeId() {
	return secondDegradeId;
    }
    public int getThirdDegradeId() {
	return thirdDegradeId;
    }
    public int getFourthDegradeId() {
	return fourthDegradeId;
    }
    public int getFifthDegradeId() {
	return fifthDegradeId;
    }
    public int getSixthDegradeId() {
	return sixthDegradeId;
    }
    public int getSeventhDegradeId() {
	return seventhDegradeId;
    }
    public int getEigthDegradeId() {
	return eigthDegradeId;
    }
    public int getNinthDegradeId() {
	return ninthDegradeId;
    }
    public int getTenthDegradeId() {
	return tenthDegradeId;
    }
    public int getBrokenId() {
	return brokenId;
    }
    public int getEquipSlot() {
	return equipSlot;
    }
    public int getPlayerArraySlot() {
	return playerArraySlot;
    }
    public int getDegradeIdForInt(int i) {
	switch(i) {
	    case 1:
		return firstDegradeId;
	    case 2:
		return secondDegradeId;
	    case 3:
		return thirdDegradeId;
	    case 4:
		return fourthDegradeId;
	    case 5:
		return fifthDegradeId;
	    case 6:
		return sixthDegradeId;
	    case 7:
		return seventhDegradeId;
	    case 8:
		return eigthDegradeId;
	    case 9:
		return ninthDegradeId;
	    case 10:
		return tenthDegradeId;
	}
	return -1;
    }
    public ArrayList<Integer> getIterableDegradedIds(boolean barrows) {
	ArrayList<Integer> toReturn = new ArrayList<>();
	toReturn.add(firstDegradeId);
	toReturn.add(secondDegradeId);
	toReturn.add(thirdDegradeId);
	toReturn.add(fourthDegradeId);
	if(!barrows) {
	    toReturn.add(fifthDegradeId);
	    toReturn.add(sixthDegradeId);
	    toReturn.add(seventhDegradeId);
	    toReturn.add(eigthDegradeId); 
	    toReturn.add(ninthDegradeId); 
	    toReturn.add(tenthDegradeId); 
	}
	toReturn.add(brokenId);
	return toReturn;
    }

    public static Degradeables getDegradeableItem(Item item) {
	if(item != null) {
	    String itemName = ItemDefinition.forId(item.getId()).getName().toLowerCase();
	    if (itemName.contains("ahrim")) {
		if (itemName.contains("staff")) return AHRIMS_STAFF;
		if (itemName.contains("skirt")) return AHRIMS_SKIRT;
		if (itemName.contains("top"))	return AHRIMS_TOP;
		if (itemName.contains("hood"))	return AHRIMS_HOOD;
	    }
	    if (itemName.contains("guthan")) {
		if (itemName.contains("spear")) return GUTHANS_WARSPEAR;
		if (itemName.contains("skirt")) return GUTHANS_SKIRT;
		if (itemName.contains("body"))	return GUTHANS_PLATE;
		if (itemName.contains("helm"))	return GUTHANS_HELM;
	    }
	    if (itemName.contains("torag")) {
		if (itemName.contains("hammers") || itemName.contains("hammer")) return TORAGS_HAMMERS;
		if (itemName.contains("legs"))	return TORAGS_LEGS;
		if (itemName.contains("body"))	return TORAGS_PLATE;
		if (itemName.contains("helm"))	return TORAGS_HELM;
	    }
	    if (itemName.contains("dharok")) {
		if (itemName.contains("axe"))	return DHAROKS_AXE;
		if (itemName.contains("legs"))	return DHAROKS_LEGS;
		if (itemName.contains("body"))	return DHAROKS_PLATE;
		if (itemName.contains("helm"))	return DHAROKS_HELM;
	    }
	    if (itemName.contains("verac")) {
		if (itemName.contains("flail")) return VERACS_FLAIL;
		if (itemName.contains("skirt"))	return VERACS_SKIRT;
		if (itemName.contains("brassard") || itemName.contains("top")) return VERACS_TOP;
		if (itemName.contains("helm"))	return VERACS_HELM;
	    }
	    if (itemName.contains("karil")) {
		if (itemName.contains("crossbow") || itemName.contains("x-bow")) return KARILS_CROSSBOW;
		if (itemName.contains("skirt"))	return KARILS_SKIRT;
		if (itemName.contains("top"))	return KARILS_TOP;
		if (itemName.contains("coif"))	return KARILS_COIF;
	    }
	    if (itemName.contains("crystal")) {
		if (itemName.contains("bow")) return CRYSTAL_BOW;
		if (itemName.contains("shield")) return CRYSTAL_SHIELD;
	    }
	    if (itemName.equals("crystal seed")) return CRYSTAL_BOW;
	    if (itemName.equals("elf crystal")) return CRYSTAL_SHIELD;
	}
	return null;
    }
    public static boolean isDroppable(Degradeables compare, Item item) {
	if(item.getId() == compare.getOriginalId() || item.getId() == compare.getBrokenId()) {
	    return true;
	}
	else {
	    return false;
	}
    }

    public static boolean notDroppable(Degradeables compare, Item item) {
	if(compare == null || item.getDefinition().getName().toLowerCase().contains("crystal")) {
	    return false;
	}
	if(item.getId() == compare.getOriginalId() ||
		item.getId() == compare.getFirstDegradeId() ||
		item.getId() == compare.getSecondDegradeId() ||
		item.getId() == compare.getThirdDegradeId() ||
		item.getId() == compare.getFourthDegradeId() ||
		item.getId() == compare.getFifthDegradeId() ||
		item.getId() == compare.getSixthDegradeId() ||
		item.getId() == compare.getSeventhDegradeId() ||
		item.getId() == compare.getEigthDegradeId() ||
		item.getId() == compare.getNinthDegradeId() ||
		item.getId() == compare.getTenthDegradeId() ) {
	    return true;
	}
	else {
	    return false;
	}
    }
    
    public static boolean fullBarrowsSet(Item helmet, Item body, Item legs, Item weapon, String name) {
	if(getDegradeableItem(helmet) == null || getDegradeableItem(body) == null || getDegradeableItem(legs) == null || getDegradeableItem(weapon) == null) {
	    return false;
	} else {
	    Degradeables helm = getDegradeableItem(helmet);
	    Degradeables chest = getDegradeableItem(body);
	    Degradeables leg = getDegradeableItem(legs);
	    Degradeables wep = getDegradeableItem(weapon);
	    if(helm.getEquipSlot() == Constants.HAT && chest.getEquipSlot() == Constants.CHEST && leg.getEquipSlot() == Constants.LEGS && wep.getEquipSlot() == Constants.WEAPON) {
		if(helm.name().contains(name) && chest.name().contains(name) && leg.name().contains(name) && wep.name().contains(name)) {
		    return true;
		}
	    }
	}
	return false;
    }
}