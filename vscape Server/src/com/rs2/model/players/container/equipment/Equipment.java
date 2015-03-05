package com.rs2.model.players.container.equipment;

import java.text.DecimalFormat;

import com.rs2.Constants;
import com.rs2.model.content.Following;
import com.rs2.model.content.WalkInterfaces;
import com.rs2.model.content.combat.special.SpecialType;
import com.rs2.model.content.combat.util.Degradeables;
import com.rs2.model.content.combat.util.Degrading;
import com.rs2.model.content.combat.weapon.Weapon;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.minigames.castlewars.Castlewars;
import com.rs2.model.content.minigames.duelarena.RulesData;
import com.rs2.model.content.quests.DragonSlayer;
import com.rs2.model.content.quests.GhostsAhoy;
import com.rs2.model.content.quests.LostCity;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.quests.MonkeyMadness.ApeAtoll;
import com.rs2.model.content.quests.PlagueCity;
import com.rs2.model.content.quests.RecruitmentDrive;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillCapeHandler;
import com.rs2.model.content.skills.runecrafting.Tiaras;
import com.rs2.model.players.Player;
import com.rs2.model.players.container.Container;
import com.rs2.model.players.container.Container.Type;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;
import com.rs2.model.players.item.ItemManager;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class Equipment {

	public static final int EQUIPMENT_INTERFACE = 1688;
	public static final int SIZE = 14;
	public static final int ACCURATE = 0;
	public static final int AGGRESSIVE = 1;
	public static final int CONTROLLED = 2;
	public static final int DEFENSIVE = 3;
	public int walkAnim;

	private int attackLevelReq, strengthLevelReq, defenceLevelReq, rangeLevelReq, prayerLevelReq, magicLevelReq,
	runecraftLevelReq, hitpointsLevelReq, agilityLevelReq, herbloreLevelReq, thievingLevelReq, craftingLevelReq,
	fletchingLevelReq, slayerLevelReq, miningLevelReq, smithLevelReq, fishLevelReq, cookLevelReq, fireLevelReq,
	woodLevelReq, farmLevelReq;

	private Player player;

	private static String[] hideArms = {"robe", "top", "blouse", "shirt", "platebody", "brassard", "dragon chainbody", "rock-shell plate", "spined body", "zamorak d'hide", "guthix d'hide", "saradomin d'hide", "lunar torso", "snakeskin body", "torags body", "dharoks body", "guthans body", "initiate hauberk", "doctors' gown"};
	private static String[] hideHairAndBeard = {"spiny helmet", "initiate sallet", "full helm", "saradomin full", "veracs helm", "guthans helm", "torags helm", "saradomin helm", "spined helm", "lunar helm", "h'ween mask", "gas mask"};
	private static String[] hideHair = {"cowl", "camel", "bandana", "decorative helm", "med helm", "coif", "hood", "bandanna", "berserker helm", "archer helm", "farseer helm", "warrior helm", "skeletal", "dharoks helm", "mask", "rock-shell helm", "void melee helm", "void mage helm", "void ranger helm", "3rd age mage hat"};
	private static String[] hideBeard = {"facemask", "mime mask"};

	private static int[] hideArmsInt = {4069,4504,4509};
	private static int[] hideHairInt = {4071,4506,4511, 10350};
	
	private Container itemContainer = new Container(Type.STANDARD, 14) {
		@Override
		public void clear() {
			super.clear();
			player.getEquipment().updateWeight();
		}
	};

	public Equipment(Player player) {
		this.player = player;
	}

	public void sendEquipmentOnLogin() {
		refresh();
	}

	public static boolean hideArms(int id) {
		String item = new Item(id).getDefinition().getName().toLowerCase();
		if (id == 426 || id == 544 || id == 6107) {
			return true;
		}
		Degradeables d = Degradeables.getDegradeableItem(new Item(id));
		if(d != null && d.getEquipSlot() == Constants.CHEST) {
		    return true;
		}
		if (id == 7110 || id == 7134 || id == 7128 || id == 7122) {
		    return false;
		}
		for (int element : hideArmsInt) {
			if (element == id) {
				return true;
			}
		}
		for (String element : hideArms) {
			if (item.contains(element)) {
				return true;
			}
		}
		return false;
	}

	public static boolean hideHairAndBeard(int id) {
		if(id == 10350)
		{
			return false;
		}
		String item = new Item(id).getDefinition().getName().toLowerCase();
		for (String element : hideHairAndBeard) {
			if (item.contains(element)) {
				return true;
			}
		}
		return false;
	}

	public static boolean hideHair(int id) {
		for (int element : hideHairInt) {
			if (element == id) {
				return true;
			}
		}
		String item = new Item(id).getDefinition().getName().toLowerCase();
		for (String element : hideHair) {
			if (item.contains(element) && id != 2581 && id != 2631 && id != 3057 && id != 4164) {
				return true;
			}
		}
		return false;
	}

	public static boolean hideBeard(int id) {
		String item = new Item(id).getDefinition().getName().toLowerCase();
		for (String element : hideBeard) {
			if (item.contains(element)) {
				return true;
			}
		}
		return false;
	}

	public void refresh() {
		Item[] items = itemContainer.toArray();
		player.getActionSender().sendUpdateItems(EQUIPMENT_INTERFACE, items);
		player.getEquipment().checkRangeGear();
		player.getEquipment().checkBarrowsGear();
		player.getEquipment().checkVoidGear();
		player.setEquippedWeapon(Weapon.getWeapon(items[Constants.WEAPON]));
		sendBonus(player);
		sendWeaponInterface();
		if(player.getEquipment().getId(Constants.HAT) == GhostsAhoy.BEDSHEET || player.getEquipment().getId(Constants.HAT) == GhostsAhoy.ECTOPLASM_BEDSHEET) {
		    int id = player.getEquipment().getId(Constants.HAT);
		    player.transformNpc = id == GhostsAhoy.BEDSHEET ? 1707 : 1708;
		    player.setWalkAnim(1640);
		    player.setRunAnim(1640);
		    player.setStandAnim(1639);
		}
		if(player.transformNpc >= 1480 && player.transformNpc <= 1487 && (player.getEquipment().getId(Constants.WEAPON) < 4024 || player.getEquipment().getId(Constants.WEAPON) > 4031)) {
		    player.transformNpc = -1;
		    player.setStandAnim(-1);
		    player.setWalkAnim(-1);
		    player.setRunAnim(-1);
		    player.getActionSender().sendSideBarInterfaces();
		    player.setAppearanceUpdateRequired(true);
		    player.getMMVars().setIsMonkey(false);
		    player.getMovementHandler().setRunToggled(true);
		}
		if((player.transformNpc == 1707 || player.transformNpc == 1708)) {
		    int hat = player.transformNpc == 1707 ? GhostsAhoy.BEDSHEET : GhostsAhoy.ECTOPLASM_BEDSHEET;
		    if (player.getEquipment().getId(Constants.HAT) != hat) {
			player.transformNpc = -1;
			player.setWalkAnim(-1);
			player.setRunAnim(-1);
			player.setStandAnim(-1);
		    }
		}
		if(player.getQuestStage(36) == 0 && player.getEquipment().getId(Constants.WEAPON) == 4587) {
		    if(player.getInventory().getItemContainer().freeSlots() > 0) {
			player.getEquipment().unequip(Constants.WEAPON);
		    } else {
			player.getEquipment().removeAmount(Constants.WEAPON, 1);
			player.getInventory().addItemOrBank(new Item(4587));
		    }
		    player.getActionSender().sendMessage("Your d-scim has been un-equipped due to lack of progress in Monkey Madness. :^)", true);
		    player.getActionSender().sendMessage("If your inventory is full it was sent to your bank.", true);
		}
	}

	public void refresh(int slot, Item item) {
		player.getActionSender().sendUpdateItem(slot, EQUIPMENT_INTERFACE, item);
		player.getEquipment().checkRangeGear();
		player.getEquipment().checkBarrowsGear();
		player.getEquipment().checkVoidGear();
		sendBonus(player);
		sendWeaponInterface();
		if((player.transformNpc == 1707 || player.transformNpc == 1708)) {
		    int hat = player.transformNpc == 1707 ? GhostsAhoy.BEDSHEET : GhostsAhoy.ECTOPLASM_BEDSHEET;
		    if (player.getEquipment().getId(Constants.HAT) != hat) {
			player.transformNpc = -1;
			player.setWalkAnim(-1);
			player.setRunAnim(-1);
			player.setStandAnim(-1);
		    }
		}
	}

	public void replaceEquipment(int id, int slot) {
		if(player.getEquipment().getId(slot) > 0){
			Item currentEquip = new Item(player.getEquipment().getId(slot));
			itemContainer.remove(currentEquip, slot);
		}
		Item newItem = new Item(id);
		itemContainer.set(slot, newItem);
		refresh();
		player.setSpecialAttackActive(false);
		sendWeaponInterface();
		player.setInstigatingAttack(false);
		Following.resetFollow(player);
		player.getInventory().refresh();
		player.setAppearanceUpdateRequired(true);
		player.getAttributes().put("usedGlory", Boolean.FALSE);
	}
	
	public void addEquipment(Item item, int slot)
	{
		Item addItem = new Item(item.getId(), item.getCount());
		if(player.getEquipment().getId(slot) > 0){
			int slotItemId = player.getEquipment().getId(slot);
			Item currentEquip = new Item(slotItemId, player.getEquipment().getItemContainer().getCount(slotItemId));		
			if(currentEquip.getId() != addItem.getId())
			{
				return;
			}	
			if(!currentEquip.getDefinition().isStackable())
			{
				return;
			}	
			addItem.setCount(addItem.getCount() + currentEquip.getCount());
			itemContainer.remove(currentEquip, slot);
		}
		itemContainer.set(slot, addItem);
		refresh();
		sendWeaponInterface();
		player.getInventory().refresh();
		player.setAppearanceUpdateRequired(true);
	}
	
	public void equip(int slot) {
		Item item = player.getInventory().getItemContainer().get(slot);
		if (item == null) {
			return;
		}
		int equipSlot = item.getDefinition().getSlot();
		if(!player.getInventory().playerHasItem(item))
			return;
		if (!checkRequirements(item.getId(), equipSlot)) {
			return;
		}
		//if (!player.canTeleport()) {
		//	return;
		//}
		if (player.getNewComersSide().getTutorialIslandStage() < 38) {
			player.getDialogue().sendStatement("You haven't learned how to wield items yet!");
			return;
		}
		if (player.getNewComersSide().getTutorialIslandStage() == 42) {
			if ((item.getId() == 1171 && player.getEquipment().getItemContainer().contains(1277) || (item.getId() == 1277 && player.getEquipment().getItemContainer().contains(1171))))
				player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);
		}
		if(equipSlot == Constants.HAT && player.getEquipment().getId(Constants.HAT) == PlagueCity.GAS_MASK && player.inWestArdougne()) {
			player.getDialogue().sendPlayerChat("I don't really want to die from the plague.", "I should keep this mask on my head.", Dialogues.CONTENT);
			player.getDialogue().endDialogue();
			return;
		}
		if (item.getId() == 772 && !player.hasKilledTreeSpirit()) {
			player.getDialogue().sendStatement("You need to kill the tree spirit in Entrana to weild this.");
			return;
		}
		if(item.getId() >= 3839 && item.getId() < 3845) {
		    if(player.getQuestStage(26) < 9) {
			player.getDialogue().sendStatement("You need to complete Horror From The Deep to equip this.");
			return;
		    }
		    else {
			player.setGodBook(item.getId());
		    }
		}
		if(item.getId() == 4587 && !QuestHandler.questCompleted(player, 36)) {
		    player.getDialogue().sendStatement("You must complete Monkey Madness to equip this.");
		    return;
		}
		if(player.inCwGame() || player.inCwLobby())
		{
		    Item equipItem = itemContainer.get(equipSlot);
			if(equipSlot == Constants.WEAPON){
			    if (equipItem != null) {
			    	if(equipItem.getId() == Castlewars.SARA_BANNER || equipItem.getId() == Castlewars.ZAMMY_BANNER){
			    		player.getActionSender().sendMessage("Return the banner to your base!", true);
			    		return;
			    	}
			    }
			}
		}
		if (item.getId() == 1205 && player.getNewComersSide().getTutorialIslandStage() == 40)
			player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);
		if (player.inDuelArena()) {
		    for (int funWeapon : Constants.FUN_WEAPONS) {
			if (!RulesData.FUN_WEAPON.activated(player) && item.getId() == funWeapon) {
			    player.getActionSender().sendMessage("Usage of 'Fun weapons' haven't been enabled during this fight!");
			    return;
			}
		}
        }
		boolean disabled = false;
		switch (equipSlot) {
			case Constants.HAT :
				disabled = RulesData.NO_HEAD.activated(player);
				break;
			case Constants.CAPE :
				disabled = RulesData.NO_CAPE.activated(player);
				break;
			case Constants.AMULET :
				disabled = RulesData.NO_AMULET.activated(player);
				break;
			case Constants.ARROWS :
				disabled = RulesData.NO_ARROW.activated(player);
				break;
			case Constants.WEAPON :
				disabled = RulesData.NO_WEAPON.activated(player) || (item.getDefinition().isTwoHanded() && RulesData.NO_SHIELD.activated(player));
				break;
			case Constants.CHEST :
				disabled = RulesData.NO_BODY.activated(player);
				break;
			case Constants.SHIELD :
				disabled = RulesData.NO_SHIELD.activated(player);
				break;
			case Constants.LEGS :
				disabled = RulesData.NO_LEGS.activated(player);
				break;
			case Constants.HANDS :
				disabled = RulesData.NO_GLOVES.activated(player);
				break;
			case Constants.FEET :
				disabled = RulesData.NO_BOOTS.activated(player);
				break;
			case Constants.RING :
				disabled = RulesData.NO_RINGS.activated(player);
				break;
		}
		if (disabled) {
			player.getActionSender().sendMessage("You cannot wear this during this fight!", true);
			return;
		}
		boolean removedItem = false;
		if (item.getDefinition().isStackable()) {
			int slotType = equipSlot;
			Item equipItem = itemContainer.get(slotType);
			player.getInventory().removeItemSlot(item, slot);
			removedItem = true;
			if (itemContainer.get(slotType) != null) {
			    if (item.getId() == equipItem.getId()) {
				itemContainer.set(slotType, new Item(item.getId(), item.getCount() + equipItem.getCount()));
			    } else {
				player.getInventory().addItemToSlot(equipItem, slot);
				itemContainer.set(slotType, item);
			    }
			} else {
				itemContainer.set(slotType, item);
			}
		} else {
			int slotType = equipSlot;
			if (slotType == Constants.WEAPON) {
				if (item.getDefinition().isTwoHanded() || item.getDefinition().getName().toLowerCase().toLowerCase().contains("godsword")) {
					if (itemContainer.get(Constants.WEAPON) != null && itemContainer.get(Constants.SHIELD) != null && player.getInventory().getItemContainer().freeSlot() == -1) {
						player.getActionSender().sendMessage("Not enough space in your inventory.", true);
						return;
					}
					player.getInventory().removeItemSlot(item, slot);
					removedItem = true;
					unequip(Constants.SHIELD);
					if (itemContainer.get(Constants.SHIELD) != null) {
						return;
					}
				}
			}
			if (slotType == Constants.SHIELD && itemContainer.get(Constants.WEAPON) != null) {
				if (itemContainer.get(Constants.WEAPON).getDefinition().isTwoHanded() || item.getDefinition().getName().toLowerCase().toLowerCase().contains("godsword")) {
					player.getInventory().removeItemSlot(item, slot);
					removedItem = true;
					unequip(Constants.WEAPON);
					if (itemContainer.get(Constants.WEAPON) != null) {
						return;
					}
				}
			}
			if (itemContainer.get(slotType) != null) {
			    Item equipItem = itemContainer.get(slotType);
			    if (!removedItem) {
				player.getInventory().removeItemSlot(item, slot);
				player.getInventory().addItemToSlot(equipItem, slot);
			    } else {
				player.getInventory().addItem(equipItem);
			    }
			} else {
				player.getInventory().removeItemSlot(item, slot);
			}
			itemContainer.set(slotType, new Item(item.getId(), item.getCount()));
		}
		player.getTask();
		player.setSpecialAttackActive(false);
		Following.resetFollow(player);
		player.getInventory().refresh();
		if (equipSlot == Constants.WEAPON) {
			player.setEquippedWeapon(Weapon.getWeapon(item));
			player.setSpecialType(SpecialType.getSpecial(item));
			player.setAutoSpell(null);
		}
		if(Constants.DEGRADING_ENABLED) {
		    Degradeables d = Degradeables.getDegradeableItem(item);
		    if (d != null) {
			if (d.getOriginalId() == item.getId()) {
			    if (player.getDegradeableHits()[d.getPlayerArraySlot()] <= 0) {
				player.setDegradeableHits(d.getPlayerArraySlot(), 0);
				player.getActionSender().sendMessage("Your " + item.getDefinition().getName().toLowerCase() + " will degrade and become untradeable upon combat.", true);
			    }
			}
			int count = 1;
			for (int i : d.getIterableDegradedIds(!item.getDefinition().getName().toLowerCase().contains("crystal"))) {
			    if (item.getId() == i) {
				int hitCount = player.getDegradeableHits()[Degradeables.getDegradeableItem(item).getPlayerArraySlot()];
				player.getActionSender().sendMessage("You have " + ((Degradeables.DEGRADE_HITS * count) - hitCount) + " hits on your " + item.getDefinition().getName().toLowerCase() + " until the next degrade.", true);
			    }
			    count++;
			}
		    }
		}
		if(item.getId() == GhostsAhoy.BEDSHEET) {
		    player.transformNpc = 1707;
		    player.setWalkAnim(1640);
		    player.setRunAnim(1640);
		    player.setStandAnim(1639);
		}
		if(item.getId() == GhostsAhoy.ECTOPLASM_BEDSHEET) {
		    player.transformNpc = 1708;
		    player.setWalkAnim(1640);
		    player.setRunAnim(1640);
		    player.setStandAnim(1639);
		}
		if((SkillCapeHandler.SkillCape.forItemId(item.getId()) != null)) {
		    if(!player.skillCapeBoost) {
			player.skillCapeBoost = true;
			player.getActionSender().statEdit(SkillCapeHandler.SkillCape.forItemId(item.getId()).getSkillId(), 1, true);
			player.getActionSender().sendMessage("You feel a slight boost in your abilities.", true);
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			    @Override
			    public void execute(CycleEventContainer b) {
				b.stop();
			    }

			    @Override
			    public void stop() {
				player.skillCapeBoost = false;
			    }
			}, 60);
		    }
		}
		refresh();
		updateWeight();
		player.getAttributes().put("usedGlory", Boolean.FALSE);
		if (item.getId() == 6583 || item.getId() == 7927) {
			player.transformNpc = item.getId() == 6583 ? 2626 : 3689 + Misc.random(5);
			player.setAppearanceUpdateRequired(true);
			player.getActionSender().hideAllSideBars();
			player.getActionSender().sendSidebarInterface(3, 6014);
		}
		WalkInterfaces.checkChickenOption(player);
		if (equipSlot == Constants.HAT) {
			Tiaras.handleTiara(player);
		}
		player.setAppearanceUpdateRequired(true);
	}

	public void unequip(int slot) {
		Item item = itemContainer.get(slot);
		if (item == null) {
			return;
		}
		if(player.inCwGame() || player.inCwLobby())
		{
			if(slot == Constants.CAPE){
			    player.getActionSender().sendMessage("You cannot unequip this cape in Castlewars!", true);
				return;
			}
			if(slot == Constants.WEAPON){
				if(item.getId() == Castlewars.SARA_BANNER || item.getId() == Castlewars.ZAMMY_BANNER){
					player.getActionSender().sendMessage("Return the banner to your base!", true);
					return;
				}
			}
			if(slot == Constants.HANDS)
			{
		    	if(player.wearingCwBracelet())
		    	{
		    		player.getActionSender().sendMessage("You cannot unequip your Castlewars bracelet while in castlewars.", true);
		    		return;
		    	}
			}
		}
		if(item.getId() == PlagueCity.GAS_MASK && player.inWestArdougne()) {
			player.getDialogue().sendPlayerChat("I don't really want to die from the plague.", "I should keep this on my head.", Dialogues.CONTENT);
			player.getDialogue().endDialogue();
			return;
		}
        if (player.getInventory().getItemContainer().freeSlot() == -1) {
            player.getActionSender().sendMessage("Not enough space in your inventory.", true);
            return;
        }
    	if (item.getId() == 6583 || item.getId() == 7927 || ApeAtoll.GreeGreeData.forItemId(item.getId()) != null) {
		player.transformNpc = -1;
		player.setStandAnim(-1);
		player.setWalkAnim(-1);
		player.setRunAnim(-1);
		player.getActionSender().sendSideBarInterfaces();
		player.setAppearanceUpdateRequired(true);
		if(ApeAtoll.GreeGreeData.forItemId(item.getId()) != null) {
		    player.getMMVars().setIsMonkey(false);
		    player.getMovementHandler().setRunToggled(true);
		}
	}
        if(!player.getEquipment().getItemContainer().contains(item.getId()))
            return;
		player.getTask();
		itemContainer.remove(item, slot);
		player.getInventory().addItem(new Item(item.getId(), item.getCount()));
		if (slot == Constants.WEAPON) {
			player.setEquippedWeapon(null);
			player.setSpecialType(null);
			player.setAutoSpell(null);
		}
		if(slot == Constants.HAT && (item.getId() == GhostsAhoy.BEDSHEET || item.getId() == GhostsAhoy.ECTOPLASM_BEDSHEET)) {
		    player.transformNpc = -1;
		    player.setWalkAnim(-1);
		    player.setRunAnim(-1);
		    player.setStandAnim(-1);
		    player.setAppearanceUpdateRequired(true);
		}
		refresh(slot, new Item(-1, 0));
		updateWeight();
		WalkInterfaces.checkChickenOption(player);
		if (slot == Constants.HAT) {
			Tiaras.handleTiara(player);
		}
		player.setAppearanceUpdateRequired(true);
	}

	public void updateWeight() {
		double totalWeight = 0;
		for (int i = 0; i < 11; i++)
			if (player.getEquipment().getItemContainer().get(i) != null)
				totalWeight += ItemDefinition.getWeight(player.getEquipment().getItemContainer().get(i).getId());

		for (int i = 0; i < 28; i++)
			if (player.getInventory().getItemContainer().get(i) != null) {
				totalWeight += ItemDefinition.getWeight(player.getInventory().getItemContainer().get(i).getId());
				if (player.getInventory().getItemContainer().get(i).getId() == 88)// boots
																					// of
																					// lightness
					totalWeight += 4.8;
			}

		player.totalWeight = totalWeight;
		player.getActionSender().sendWeight();
		// player.getActionSender().sendString((int)
		// Math.floor(player.totalWeight) + "kg", 184);

	}

	public void removeAmount(int slot, int amount) {
		Item item = itemContainer.get(slot);
		if (item == null) {
			return;
		}
		int total = itemContainer.remove(item, slot) - amount;
		if (total > 0) {
			Item newItem = new Item(item.getId(), total);
			itemContainer.add(newItem, slot);
			//refresh(slot, newItem);
		} else {
			itemContainer.add(new Item(-1, 0), slot);
			//itemContainer.remove(item);
			//refresh(slot, new Item(-1, 0));
		}
		refresh();
		player.setAppearanceUpdateRequired(true);
		sendBonus(player);
	}

	public void setBonus(Player player) {
		for (int i = 0; i < 14; i++) {
			if (player.getEquipment().getItemContainer().get(i) == null || i == 13) {
				continue;
			}
			Item item = player.getEquipment().getItemContainer().get(i);
			for (int bonus = 0; bonus < 12; bonus++) {
			    if((bonus == 5 || bonus == 6 || bonus == 7 || bonus == 9) && i == Constants.SHIELD && item.getId() == 11284) {
				player.setBonuses(bonus, (int) (item.getDefinition().getBonuses()[bonus] + player.getBonuses().get(bonus) + player.getDfsCharges()));
			    } else {
				player.setBonuses(bonus, (int) (item.getDefinition().getBonuses()[bonus] + player.getBonuses().get(bonus)));
			    }
			}
		}
	}

	public void sendBonus(Player player) {
		int offset = 0;
		String send = "";
		for (int i = 0; i < 12; i++) {
			player.setBonuses(i, 0);
		}
		setBonus(player);
		for (int i = 0; i < 12; i++) {
			if (player.getBonuses().get(i) >= 0) {
				send = Constants.BONUS_NAME[i] + ": +" + player.getBonuses().get(i);
			} else {
				send = Constants.BONUS_NAME[i] + ": -" + Math.abs(player.getBonuses().get(i));
			}
			if (i == 10) {
				offset = 1;
			}
			player.getActionSender().sendString(send, (1675 + i + offset));
		}
	}

	/**
	 * Wear Item
	 **/

	public void sendWeaponInterface() {
		Weapon weapon = Weapon.getWeapon(getItemContainer().get(Constants.WEAPON));
		player.getActionSender().sendSidebarInterface(0, weapon.getWeaponInterface().getInterfaceId());
        player.getActionSender().sendString(getItemContainer().get(Constants.WEAPON) == null ? "Unarmed" : getItemContainer().get(Constants.WEAPON).getDefinition().getName(), weapon.getWeaponInterface().weaponNameChild());
        adjustFightMode(weapon);
        player.getActionSender().sendConfig(43, player.getFightMode());
    	if(weapon.getWeaponInterface().weaponDisplayChild() != -1)
        	player.getActionSender().sendItemOnInterface(weapon.getWeaponInterface().weaponDisplayChild(), 200, getItemContainer().get(Constants.WEAPON).getId());
        if(weapon.getWeaponInterface().getSpecialBarId() != -1)
	        if(SpecialType.getSpecial(getItemContainer().get(Constants.WEAPON)) == null)
	            player.getActionSender().sendInterfaceHidden(1, weapon.getWeaponInterface().getSpecialBarId());
	        else {
	            //System.out.println(weapon.getWeaponInterface().getSpecialBarId());
	            player.getActionSender().sendInterfaceHidden(0, weapon.getWeaponInterface().getSpecialBarId());
	            player.updateSpecialBar();
	        }

    }

	public void adjustFightMode(Weapon weapon) {
		if (weapon.getAttackAnimations().length < 4 && player.getFightMode() == 3)  {
			player.setFightMode(2);
		}
	}

    public boolean setFightMode(int buttonId){
        Weapon weapon = Weapon.getWeapon(getItemContainer().get(Constants.WEAPON));
        for(int i = 0; i < weapon.getWeaponInterface().getAttackStyles().length; i++){
            if(buttonId == weapon.getWeaponInterface().getAttackStyles()[i].getButtonId()) {
            	player.disableAutoCast();
                player.setFightMode(i);
                player.resetAllActions();
                //player.setFollowDistance(DistanceCheck.getDistanceForCombatType(player));
                //player.resetActions();
                return true;
            }
        }
        return false;
    }


	public int getId(int slot) {
		Item item = getItemContainer().get(slot);
		if (item != null) {
			return item.getId();
		}
		return 0;
	}

	public int getStandAnim() {
		return player.getEquippedWeapon().getMovementAnimations()[0];
	}

	public int getWalkAnim() {
		return player.getEquippedWeapon().getMovementAnimations()[1];
	}

	public int getRunAnim() {
		return player.getEquippedWeapon().getMovementAnimations()[2];
	}

	public Container getItemContainer() {
		return itemContainer;
	}

	public int getRangeLevelReq() {
		return rangeLevelReq;
	}

	public int getSlayerLevelReq() {
		return slayerLevelReq;
	}

	public class AttackStyles {

		private int accurate = 422;
		private int aggressive = 423;
		private int controlled = 422;
		private int defensive = 422;

		public int get(int style) {
			switch (style) {
				case 0 :
					return accurate;
				case 1 :
					return aggressive;
				case 2 :
					return controlled;
				case 3 :
					return defensive;
			}
			return accurate;
		}
	}

	public boolean checkRequirements(int itemId, int targetSlot) {
		getRequirements(itemId);
		if (targetSlot == Constants.WEAPON) {
			if(LostCity.isWeapon(itemId)) {
			    if(!QuestHandler.questCompleted(player, 14)) {
				player.getDialogue().sendStatement("You must complete Lost City to equip this.");
				return false;
			    }
			}
			if(itemId == 1434 || itemId == 1377) {
			    if(!QuestHandler.questCompleted(player, 27)) {
				player.getDialogue().sendStatement("You must complete Heroes Quest to equip this.");
				return false;
			    }
			}
			if(Constants.DEGRADING_ENABLED) {
			    if(Degradeables.getDegradeableItem(new Item(itemId)) != null && Degradeables.getDegradeableItem(new Item(itemId)).getOriginalId() == itemId) {
				if(player.getDegradeableHits()[Degradeables.getDegradeableItem(new Item(itemId)).getPlayerArraySlot()] > 0) {
				    if(!Degrading.ownsDegradedVersion(player, itemId)) {
					player.setDegradeableHits(Degradeables.getDegradeableItem(new Item(itemId)).getPlayerArraySlot(), 0);
				    } else {
					player.getActionSender().sendMessage("You already have this degradeable item bound to you!", true);
					player.getActionSender().sendMessage("Repair it, or let it break completely.", true);
					return false;
				    }
				}
			    }
			}
			if (attackLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.ATTACK) < attackLevelReq) {
					player.getActionSender().sendMessage("You need an Attack level of " + attackLevelReq + " to wield this weapon.");
					return false;
				}
			}
			if (strengthLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.STRENGTH) < strengthLevelReq) {
					player.getActionSender().sendMessage("You need a Strength level of " + strengthLevelReq + " to wear this weapon.");
					return false;
				}
			}
			if (rangeLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.RANGED) < rangeLevelReq) {
					player.getActionSender().sendMessage("You need a Ranged level of " + rangeLevelReq + " to wield this weapon.");
					return false;
				}
			}
			if (magicLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.MAGIC) < magicLevelReq) {
					player.getActionSender().sendMessage("You need a Magic level of " + magicLevelReq + " to wield this weapon.");
					return false;
				}
			}
			if (slayerLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.SLAYER) < slayerLevelReq) {
					player.getActionSender().sendMessage("You need a Slayer level of " + slayerLevelReq + " to wield this weapon.");
					return false;
				}
			}
			if (agilityLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.AGILITY) < agilityLevelReq) {
					player.getActionSender().sendMessage("You need an Agility level of " + agilityLevelReq + " to wield this weapon.");
					return false;
				}
			}
		} else if (targetSlot == Constants.FEET || targetSlot == Constants.LEGS || targetSlot == Constants.SHIELD || targetSlot == Constants.CHEST || targetSlot == Constants.HAT || targetSlot == Constants.HANDS) {
			if(targetSlot == Constants.HAT && (player.inCwGame() || player.inCwLobby()))
			{
			    player.getActionSender().sendMessage("You cannot wear this headwear in Castlewars!", true);
				return false;
			}
			if(targetSlot == Constants.HANDS && (player.inCwGame() || player.inCwLobby()))
			{
		    	if(player.wearingCwBracelet())
		    	{
		    		player.getActionSender().sendMessage("You cannot unequip your Castlewars bracelet while in castlewars.", true);
		    		return false;
		    	}
			}
			if(itemId == RecruitmentDrive.INITIATE_SALLET || itemId == RecruitmentDrive.INITIATE_HAUBERK || itemId == RecruitmentDrive.INITIATE_CUISSE) {
			    if(!QuestHandler.questCompleted(player, 35)) {
				player.getDialogue().sendStatement("You must complete Recruitment Drive to equip this.");
				return false;
			    }
			}
			if(itemId == 2890) {
			    if(player.getQuestStage(12) < 11) {
				player.getDialogue().sendStatement("You must complete Elemental Workshop to equip this.");
				return false;
			    }
			}
			if(DragonSlayer.isArmor(itemId)) {
			    if(player.getQuestStage(15) < 9) {
				player.getDialogue().sendStatement("You must complete Dragon Slayer to equip this.");
				return false;
			    }
			}
			if(Constants.DEGRADING_ENABLED) {
			    if(Degradeables.getDegradeableItem(new Item(itemId)) != null && Degradeables.getDegradeableItem(new Item(itemId)).getOriginalId() == itemId) {
				if(player.getDegradeableHits()[Degradeables.getDegradeableItem(new Item(itemId)).getPlayerArraySlot()] > 0) {
				    if(!Degrading.ownsDegradedVersion(player, itemId)) {
					player.setDegradeableHits(Degradeables.getDegradeableItem(new Item(itemId)).getPlayerArraySlot(), 0);
				    } else {
					player.getActionSender().sendMessage("You already have this degradeable item bound to you!", true);
					player.getActionSender().sendMessage("Repair it, or let it break completely.", true);
					return false;
				    }
				}
			    }
			}
			if (defenceLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.DEFENCE) < defenceLevelReq) {
					player.getActionSender().sendMessage("You need a Defence level of " + defenceLevelReq + " to wear this item.");
					return false;
				}
			}
			if (attackLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.ATTACK) < attackLevelReq) {
					player.getActionSender().sendMessage("You need an Attack level of " + attackLevelReq + " to wear this item.");
					return false;
				}
			}
			if (strengthLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.STRENGTH) < strengthLevelReq) {
					player.getActionSender().sendMessage("You need a Strength level of " + strengthLevelReq + " to wear this item.");
					return false;
				}
			}
			if (rangeLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.RANGED) < rangeLevelReq) {
					player.getActionSender().sendMessage("You need a Ranged level of " + rangeLevelReq + " to wear this item.");
					return false;
				}
			}
			if (prayerLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.PRAYER) < prayerLevelReq) {
					player.getActionSender().sendMessage("You need a Prayer level of " + prayerLevelReq + " to wear this item.");
					return false;
				}
			}
			if (magicLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.MAGIC) < magicLevelReq) {
					player.getActionSender().sendMessage("You need a Magic level of " + magicLevelReq + " to wear this item.");
					return false;
				}
			}
			if (runecraftLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.RUNECRAFTING) < runecraftLevelReq) {
					player.getActionSender().sendMessage("You need a Runecrafting level of " + runecraftLevelReq + " to wear this item.");
					return false;
				}
			}
			if (hitpointsLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.HITPOINTS) < hitpointsLevelReq) {
					player.getActionSender().sendMessage("You need a Hitpoints level of " + hitpointsLevelReq + " to wear this item.");
					return false;
				}
			}
			if (agilityLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.AGILITY) < agilityLevelReq) {
					player.getActionSender().sendMessage("You need a Agility level of " + agilityLevelReq + " to wear this item.");
					return false;
				}
			}
			if (herbloreLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.HERBLORE) < herbloreLevelReq) {
					player.getActionSender().sendMessage("You need a Herblore level of " + herbloreLevelReq + " to wear this item.");
					return false;
				}
			}
			if (thievingLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.THIEVING) < thievingLevelReq) {
					player.getActionSender().sendMessage("You need a Thieving level of " + thievingLevelReq + " to wear this item.");
					return false;
				}
			}
			if (craftingLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.CRAFTING) < craftingLevelReq) {
					player.getActionSender().sendMessage("You need a Crafting level of " + craftingLevelReq + " to wear this item.");
					return false;
				}
			}
			if (fletchingLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.FLETCHING) < fletchingLevelReq) {
					player.getActionSender().sendMessage("You need a Fletching level of " + fletchingLevelReq + " to wear this item.");
					return false;
				}
			}
			if (slayerLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.SLAYER) < slayerLevelReq) {
					player.getActionSender().sendMessage("You need a Slayer level of " + slayerLevelReq + " to wear this item.");
					return false;
				}
			}
			if (miningLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.MINING) < miningLevelReq) {
					player.getActionSender().sendMessage("You need a Mining level of " + miningLevelReq + " to wear this item.");
					return false;
				}
			}
			if (smithLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.SMITHING) < smithLevelReq) {
					player.getActionSender().sendMessage("You need a Smithing level of " + smithLevelReq + " to wear this item.");
					return false;
				}
			}
			if (fishLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.FISHING) < fishLevelReq) {
					player.getActionSender().sendMessage("You need a Fishing level of " + fishLevelReq + " to wear this item.");
					return false;
				}
			}
			if (cookLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.COOKING) < cookLevelReq) {
					player.getActionSender().sendMessage("You need a Cooking level of " + cookLevelReq + " to wear this item.");
					return false;
				}
			}
			if (fireLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.FIREMAKING) < fireLevelReq) {
					player.getActionSender().sendMessage("You need a Firemaking level of " + fireLevelReq + " to wear this item.");
					return false;
				}
			}
			if (woodLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.WOODCUTTING) < woodLevelReq) {
					player.getActionSender().sendMessage("You need a Woodcutting level of " + woodLevelReq + " to wear this item.");
					return false;
				}
			}
			if (farmLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.FARMING) < farmLevelReq) {
					player.getActionSender().sendMessage("You need a Farming level of " + farmLevelReq + " to wear this item.");
					return false;
				}
			}
		} else if (targetSlot == Constants.CAPE) {
			if((itemId == 10498 || itemId == 10499) && player.getQuestStage(25) < 23) {
			    player.getDialogue().sendStatement("You must complete Animal Magnetism to equip this.");
			    return false;
			} 
			if(player.inCwGame() || player.inCwLobby())
			{
			    player.getActionSender().sendMessage("You cannot wear this cape in Castlewars!", true);
				return false;
			}
			if (defenceLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.DEFENCE) < defenceLevelReq) {
					player.getActionSender().sendMessage("You need a Defence level of " + defenceLevelReq + " to wear this item.");
					return false;
				}
			}
			if (attackLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.ATTACK) < attackLevelReq) {
					player.getActionSender().sendMessage("You need an Attack level of " + attackLevelReq + " to wear this item.");
					return false;
				}
			}
			if (strengthLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.STRENGTH) < strengthLevelReq) {
					player.getActionSender().sendMessage("You need a Strength level of " + strengthLevelReq + " to wear this item.");
					return false;
				}
			}
			if (rangeLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.RANGED) < rangeLevelReq) {
					player.getActionSender().sendMessage("You need a Ranged level of " + rangeLevelReq + " to wear this item.");
					return false;
				}
			}
			if (prayerLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.PRAYER) < prayerLevelReq) {
					player.getActionSender().sendMessage("You need a Prayer level of " + prayerLevelReq + " to wear this item.");
					return false;
				}
			}
			if (magicLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.MAGIC) < magicLevelReq) {
					player.getActionSender().sendMessage("You need a Magic level of " + magicLevelReq + " to wear this item.");
					return false;
				}
			}
			if (runecraftLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.RUNECRAFTING) < runecraftLevelReq) {
					player.getActionSender().sendMessage("You need a Runecrafting level of " + runecraftLevelReq + " to wear this item.");
					return false;
				}
			}
			if (hitpointsLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.HITPOINTS) < hitpointsLevelReq) {
					player.getActionSender().sendMessage("You need a Hitpoints level of " + hitpointsLevelReq + " to wear this item.");
					return false;
				}
			}
			if (agilityLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.AGILITY) < agilityLevelReq) {
					player.getActionSender().sendMessage("You need a Agility level of " + agilityLevelReq + " to wear this item.");
					return false;
				}
			}
			if (herbloreLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.HERBLORE) < herbloreLevelReq) {
					player.getActionSender().sendMessage("You need a Herblore level of " + herbloreLevelReq + " to wear this item.");
					return false;
				}
			}
			if (thievingLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.THIEVING) < thievingLevelReq) {
					player.getActionSender().sendMessage("You need a Thieving level of " + thievingLevelReq + " to wear this item.");
					return false;
				}
			}
			if (craftingLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.CRAFTING) < craftingLevelReq) {
					player.getActionSender().sendMessage("You need a Crafting level of " + craftingLevelReq + " to wear this item.");
					return false;
				}
			}
			if (fletchingLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.FLETCHING) < fletchingLevelReq) {
					player.getActionSender().sendMessage("You need a Fletching level of " + fletchingLevelReq + " to wear this item.");
					return false;
				}
			}
			if (slayerLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.SLAYER) < slayerLevelReq) {
					player.getActionSender().sendMessage("You need a Slayer level of " + slayerLevelReq + " to wear this item.");
					return false;
				}
			}
			if (miningLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.MINING) < miningLevelReq) {
					player.getActionSender().sendMessage("You need a Mining level of " + miningLevelReq + " to wear this item.");
					return false;
				}
			}
			if (smithLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.SMITHING) < smithLevelReq) {
					player.getActionSender().sendMessage("You need a Smithing level of " + smithLevelReq + " to wear this item.");
					return false;
				}
			}
			if (fishLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.FISHING) < fishLevelReq) {
					player.getActionSender().sendMessage("You need a Fishing level of " + fishLevelReq + " to wear this item.");
					return false;
				}
			}
			if (cookLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.COOKING) < cookLevelReq) {
					player.getActionSender().sendMessage("You need a Cooking level of " + cookLevelReq + " to wear this item.");
					return false;
				}
			}
			if (fireLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.FIREMAKING) < fireLevelReq) {
					player.getActionSender().sendMessage("You need a Firemaking level of " + fireLevelReq + " to wear this item.");
					return false;
				}
			}
			if (woodLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.WOODCUTTING) < woodLevelReq) {
					player.getActionSender().sendMessage("You need a Woodcutting level of " + woodLevelReq + " to wear this item.");
					return false;
				}
			}
			if (farmLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.FARMING) < farmLevelReq) {
					player.getActionSender().sendMessage("You need a Farming level of " + farmLevelReq + " to wear this item.");
					return false;
				}
			}
		} else if (targetSlot == Constants.ARROWS) {
			if (rangeLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.RANGED) < rangeLevelReq) {
					player.getActionSender().sendMessage("You need a Ranged level of " + rangeLevelReq + " to equip this ammo.");
					return false;
				}
			}
			if (slayerLevelReq > 0) {
				if (player.getSkill().getPlayerLevel(Skill.SLAYER) < slayerLevelReq) {
					player.getActionSender().sendMessage("You need a Slayer level of " + slayerLevelReq + " to equip this ammo.");
					return false;
				}
			}
		}
		return true;
	}

	public void getRequirements(int itemId) {
		String itemName = ItemManager.getInstance().getItemName(itemId).toLowerCase();
		attackLevelReq = strengthLevelReq = defenceLevelReq = rangeLevelReq = prayerLevelReq = magicLevelReq =runecraftLevelReq = hitpointsLevelReq = agilityLevelReq = herbloreLevelReq = thievingLevelReq = craftingLevelReq =fletchingLevelReq = slayerLevelReq = miningLevelReq = smithLevelReq = fishLevelReq = cookLevelReq = fireLevelReq = woodLevelReq = farmLevelReq = 0;
		switch (itemId) {
			case 6524 : // obby shield
				defenceLevelReq = 60;
				return;
			case 6522 : // obby rings
				rangeLevelReq = 60;
				return;
			case 767 : //phoenix crossbow
			case 9176: //blurite c'bow
				rangeLevelReq = 1;
				return;
			case 6523 : // obby sword
			case 6525 : // obby knife
			case 6527 : // obby mace
				attackLevelReq = 60;
				return;
			case 6526 : // obby staff
				attackLevelReq = 60;
				magicLevelReq = 60;
				return;
			case 2609 : //addy platelegs (g)	//cadillac
				defenceLevelReq = 30;
				return;
			case 4164 : // facemask
				slayerLevelReq = 10;
				return;
			case 4166 : // earmuffs
				slayerLevelReq = 15;
				return;
			case 4156 : // mirror shield
				slayerLevelReq = 25;
				defenceLevelReq = 20;
				return;
			case 7051 : // bug latern
			case 7053 :
				slayerLevelReq = 33;
				return;
			case 7159 : // slayer boots
				slayerLevelReq = 37;
				return;
			case 6708 : // slayer gloves
				slayerLevelReq = 42;
				return;
			case 4158 : // leaf-bladed spear
				slayerLevelReq = 55;
				return;
			case 4168 : // nose peg
				slayerLevelReq = 60;
				return;
			case 4170 : // slayer staff
				slayerLevelReq = 55;
				return;
			case 6528 : // obby maul
				strengthLevelReq = 60;
				return;
			case 6724 : // seercull
				rangeLevelReq = 50;
				return;
			case 4212 : //crystal bow
			    rangeLevelReq = 70;
			    agilityLevelReq = 50;
			    return;
			case 11235 : //dark bow
			    rangeLevelReq = 60;
			    return;
			case 1391: //Battlestaff
			case 1393: //Fire Battlestaff
			case 1395: //Water battlestaff
			case 1397: //air battlestaff
			case 1399: //earth battlestaff
			case 3053: //lava battlestaff
			case 6652: //mud battlestaff
			case 11736: //steam battlestaff
			    magicLevelReq = 30;
			    attackLevelReq = 30;
			    return;
			case 3481: // gilded platebody
			case 3483: // gilded platelegs
			case 3485: // gilded plateskirt
			case 3486: // gilded full help
			case 3488: // gilded kiteshield
			    defenceLevelReq = 40;
			    return;
			case 10346: //2rd age melee
			case 10348:
			case 10350:
			case 10352:
			    defenceLevelReq = 65;
			    return;
			case 10338: //3rd age mage
			case 10340:
			case 10342:
			    defenceLevelReq = 30;
			    magicLevelReq = 65;
			    return;
			case 10330: //3rd age range
			case 10332:
			case 10334:
			case 10336:
			    defenceLevelReq = 45;
			    rangeLevelReq = 65;
			    return;
			case 35: //excalibur
			    attackLevelReq = 20;
			    return;
			case 6889 :
			case 6914 :
				magicLevelReq = 60;
				return;
			case 2503 : //black dhide body
				rangeLevelReq = 70;
				defenceLevelReq = 40;
				return;
			case 10440:
			case 10442:
			case 10444:
			case 10470:
			case 10472:
			case 10474: //stoles and croziers
			    prayerLevelReq = 60;
			    return;
			case 10458:
			case 10460:
			case 10462:
			case 10464:
			case 10466:
			case 10468://god robe top and bottoms
			    prayerLevelReq = 40;
			    return;
			case 6809: //granite legs
			    defenceLevelReq = 50;
			    strengthLevelReq = 50;
			    return;
			case 6128:
			case 6129:
			case 6130: //rockshell
				defenceLevelReq = 40;
				return;
			case 2501 : //red dhide body
				rangeLevelReq = 60;
				defenceLevelReq = 40;
				return;
			case 2499 : //blue dhide body
				rangeLevelReq = 50;
				defenceLevelReq = 40;
				return;
			case 7372:
			case 7370: //green (t) and (g) body
			    rangeLevelReq = 40;
			    defenceLevelReq = 40;
			    return;
			case 7380:
			case 7378: //green (t) and (g) chap
			    rangeLevelReq = 40;
			    return;
			case 7376:
			case 7374: //blue (t) and (g) body
			    rangeLevelReq = 50;
			    defenceLevelReq = 40;
			    return;
			case 7384:
			case 7382: //blue (t) and (g) chap
			    rangeLevelReq = 50;
			    return;
			case 11283:
			case 11284: //DFS
			    defenceLevelReq = 75;
			    return;
			case 1135: //green dhide body
				rangeLevelReq = 40;
				defenceLevelReq = 40;
				return;
			case 3751 :
			case 3749 :
			case 3755 :
				defenceLevelReq = 45;
				return;
			case 7460 :
				defenceLevelReq = 20;
				return;
			case 837 :
				rangeLevelReq = 61;
				return;
			case 4151 :
				attackLevelReq = 70;
				return;
			case 4153 :
				attackLevelReq = 50;
				strengthLevelReq = 50;
				return;
			case 4708: //ahrim's hood
				magicLevelReq = 70;
				defenceLevelReq = 70;
				return;
			case 8839:
			case 8840:
			case 8841:
			case 8842:
			case 11663:
			case 11664:
			case 11665: //void equipment
				hitpointsLevelReq = 42;
				attackLevelReq = 42;
				strengthLevelReq = 42;
				defenceLevelReq = 42;
				magicLevelReq = 42;
				rangeLevelReq = 42;
				prayerLevelReq = 22;
				return;

		}
		
		if (itemName.contains("cape") || itemName.contains("hood")) {
			if (itemName.contains("attack")) {
				attackLevelReq = 99;
			}else if (itemName.contains("strength")) {
				strengthLevelReq = 99;
			}else if (itemName.contains("defence")) {
				defenceLevelReq = 99;
			}else if (itemName.contains("ranging")) {
				rangeLevelReq = 99;
			}else if (itemName.contains("prayer")) {
				prayerLevelReq = 99;
			}else if (itemName.contains("magic")) {
				magicLevelReq = 99;
			}else if (itemName.contains("runecraft")) {
				runecraftLevelReq = 99;
			}else if (itemName.contains("hitpoints")) {
				hitpointsLevelReq = 99;
			}else if (itemName.contains("agility")) {
				agilityLevelReq = 99;
			}else if (itemName.contains("herblore")) {
				herbloreLevelReq = 99;
			}else if (itemName.contains("thieving")) {
				thievingLevelReq = 99;
			}else if (itemName.contains("crafting")) {
				craftingLevelReq = 99;
			}else if (itemName.contains("fletching")) {
				fletchingLevelReq = 99;
			}else if (itemName.contains("slayer")) {
				slayerLevelReq = 99;
			}else if (itemName.contains("mining")) {
				miningLevelReq = 99;
			}else if (itemName.contains("smithing")) {
				smithLevelReq = 99;
			}else if (itemName.contains("fishing")) {
				fishLevelReq = 99;
			}else if (itemName.contains("cooking")) {
				cookLevelReq = 99;
			}else if (itemName.contains("firemaking")) {
				fireLevelReq = 99;
			}else if (itemName.contains("woodcutting")) {
				woodLevelReq = 99;
			}else if (itemName.contains("farming")) {
				farmLevelReq = 99;
			}
			return;
		}
		
		//base armour and weaponry
		if (itemName.contains("bronze")) 
		{
			if(itemName.contains("dagger") || itemName.contains("axe") || itemName.contains("mace") || itemName.contains("claws") || itemName.contains("sword") || 
			   itemName.contains("scim") || itemName.contains("spear") || itemName.contains("hammer") || itemName.contains("halberd")) {
				attackLevelReq = 1;
			}
			if (itemName.contains("body") || itemName.contains("legs") || itemName.contains("skirt") || itemName.contains("helm") || itemName.contains("shield") || itemName.contains("boots")) {
				defenceLevelReq = 1;
			}
			if(itemName.contains("bow"))
			{
				rangeLevelReq = 1;
			}
			if(itemName.contains("bolt"))
			{
				rangeLevelReq = 1;
			}
			if (itemName.contains("arrow")) {
				rangeLevelReq = 1;
			}
			if (itemName.contains("dart") || itemName.contains("javelin") || itemName.contains("thrownaxe") || itemName.contains("knife")) {
				rangeLevelReq = 1;
			}
			return;
		}
		if (itemName.contains("iron")) 
		{
			if(itemName.contains("dagger") || itemName.contains("axe") || itemName.contains("mace") || itemName.contains("claws") || itemName.contains("sword") || 
			   itemName.contains("scim") || itemName.contains("spear") || itemName.contains("hammer") || itemName.contains("halberd")) {
				attackLevelReq = 1;
			}
			if (itemName.contains("body") || itemName.contains("legs") || itemName.contains("skirt") || itemName.contains("helm") || itemName.contains("shield") || itemName.contains("boots")) {
				defenceLevelReq = 1;
			}
			if(itemName.contains("bow"))
			{
				rangeLevelReq = 26;
			}
			if(itemName.contains("bolt"))
			{
				rangeLevelReq = 10;
			}
			if (itemName.contains("arrow")) {
				rangeLevelReq = 1;
			}
			if (itemName.contains("dart") || itemName.contains("javelin") || itemName.contains("thrownaxe") || itemName.contains("knife")) {
				rangeLevelReq = 1;
			}
			return;
		}
		if (itemName.contains("steel")) 
		{
			if(itemName.contains("dagger") || itemName.contains("axe") || itemName.contains("mace") || itemName.contains("claws") || itemName.contains("sword") || 
			   itemName.contains("scim") || itemName.contains("spear") || itemName.contains("hammer") || itemName.contains("halberd")) {
				attackLevelReq = 5;
			}
			if (itemName.contains("body") || itemName.contains("legs") || itemName.contains("skirt") || itemName.contains("helm") || itemName.contains("shield") || itemName.contains("boots")) {
				defenceLevelReq = 5;
			}
			if(itemName.contains("bow"))
			{
				rangeLevelReq = 31;
			}
			if(itemName.contains("bolt"))
			{
				rangeLevelReq = 20;
			}
			if (itemName.contains("arrow")) {
				rangeLevelReq = 5;
			}
			if (itemName.contains("dart") || itemName.contains("javelin") || itemName.contains("thrownaxe") || itemName.contains("knife")) {
				rangeLevelReq = 5;
			}
			return;
		}
		if (itemName.contains("black") && !itemName.contains("d'hide") && !itemName.contains("ele'")) 
		{
			if(itemName.contains("dagger") || itemName.contains("axe") || itemName.contains("mace") || itemName.contains("claws") || itemName.contains("sword") || 
			   itemName.contains("scim") || itemName.contains("spear") || itemName.contains("hammer") || itemName.contains("halberd")) {
				attackLevelReq = 10;
				if (itemName.contains("halberd")) 
				{
					strengthLevelReq = 5;
				}
			}
			if (itemName.contains("body") || itemName.contains("legs") || itemName.contains("skirt") || itemName.contains("helm") || itemName.contains("shield") || itemName.contains("boots")) {
				defenceLevelReq = 10;
			}
			if(itemName.contains("bow"))
			{
				rangeLevelReq = 31;
			}
			if(itemName.contains("bolt"))
			{
				rangeLevelReq = 10;
			}
			if (itemName.contains("arrow")) {
				rangeLevelReq = 10;
			}
			if (itemName.contains("dart") || itemName.contains("javelin") || itemName.contains("thrownaxe") || itemName.contains("knife")) {
				rangeLevelReq = 10;
			}
			return;
		}
		if (itemName.contains("white") && !itemName.contains("ele'")) 
		{
			if(itemName.contains("dagger") || itemName.contains("axe") || itemName.contains("mace") || itemName.contains("claws") || itemName.contains("sword") || 
			   itemName.contains("scim") || itemName.contains("spear") || itemName.contains("hammer") || itemName.contains("halberd")) {
				attackLevelReq = 10;
				if (itemName.contains("halberd")) 
				{
					strengthLevelReq = 5;
				}
			}
			if (itemName.contains("body") || itemName.contains("legs") || itemName.contains("skirt") || itemName.contains("helm") || itemName.contains("shield") || itemName.contains("boots")) {
				defenceLevelReq = 10;
			}
			if(itemName.contains("bow"))
			{
				rangeLevelReq = 31;
			}
			if(itemName.contains("bolt"))
			{
				rangeLevelReq = 10;
			}
			if (itemName.contains("arrow")) {
				rangeLevelReq = 10;
			}
			if (itemName.contains("dart") || itemName.contains("javelin") || itemName.contains("thrownaxe") || itemName.contains("knife")) {
				rangeLevelReq = 10;
			}
			return;
		}
		if (itemName.contains("mithril") || itemName.contains("mith")) 
		{
			if(itemName.contains("dagger") || itemName.contains("axe") || itemName.contains("mace") || itemName.contains("claws") || itemName.contains("sword") || 
			   itemName.contains("scim") || itemName.contains("spear") || itemName.contains("hammer") || itemName.contains("halberd")) {
				attackLevelReq = 20;
				if (itemName.contains("halberd")) 
				{
					strengthLevelReq = 10;
				}
			}
			if (itemName.contains("body") || itemName.contains("legs") || itemName.contains("skirt") || itemName.contains("helm") || itemName.contains("shield") || itemName.contains("boots")) {
				defenceLevelReq = 20;
			}
			if(itemName.contains("bow"))
			{
				rangeLevelReq = 36;
			}
			if(itemName.contains("bolt"))
			{
				rangeLevelReq = 30;
			}
			if (itemName.contains("arrow")) {
				rangeLevelReq = 20;
			}
			if (itemName.contains("dart") || itemName.contains("javelin") || itemName.contains("thrownaxe") || itemName.contains("knife")) {
				rangeLevelReq = 20;
			}
			return;
		}
		if (itemName.contains("adamant") || itemName.contains("addy")) 
		{
			if(itemName.contains("dagger") || itemName.contains("axe") || itemName.contains("mace") || itemName.contains("claws") || itemName.contains("sword") || 
			   itemName.contains("scim") || itemName.contains("spear") || itemName.contains("hammer") || itemName.contains("halberd")) {
				attackLevelReq = 30;
				if (itemName.contains("halberd")) 
				{
					strengthLevelReq = 15;
				}
			}
			if (itemName.contains("body") || itemName.contains("legs") || itemName.contains("skirt") || itemName.contains("helm") || itemName.contains("shield") || itemName.contains("boots")) {
				defenceLevelReq = 30;
			}
			if(itemName.contains("bow"))
			{
				rangeLevelReq = 46;
			}
			if(itemName.contains("bolt"))
			{
				rangeLevelReq = 40;
			}
			if (itemName.contains("arrow")) {
				rangeLevelReq = 30;
			}
			if (itemName.contains("dart") || itemName.contains("javelin") || itemName.contains("thrownaxe") || itemName.contains("knife")) {
				rangeLevelReq = 30;
			}
			return;
		}
		if (itemName.contains("rune") || itemName.contains("runite")) 
		{
			if(itemName.contains("dagger") || itemName.contains("axe") || itemName.contains("mace") || itemName.contains("claws") || itemName.contains("sword") || 
			   itemName.contains("scim") || itemName.contains("spear") || itemName.contains("hammer") || itemName.contains("halberd")) {
				attackLevelReq = 40;
				if (itemName.contains("halberd")) 
				{
					strengthLevelReq = 20;
				}
			}
			if (itemName.contains("body") || itemName.contains("legs") || itemName.contains("skirt") || itemName.contains("helm") || itemName.contains("shield") || itemName.contains("boots")) {
				defenceLevelReq = 40;
			}
			if(itemName.contains("bow"))
			{
				rangeLevelReq = 61;
			}
			if(itemName.contains("bolt"))
			{
				rangeLevelReq = 50;
			}
			if (itemName.contains("arrow")) {
				rangeLevelReq = 40;
			}
			if (itemName.contains("dart") || itemName.contains("javelin") || itemName.contains("thrownaxe") || itemName.contains("knife")) {
				rangeLevelReq = 40;
			}
			return;
		}
		if (itemName.contains("dragon") && !itemName.contains("anti")) 
		{
			if(itemName.contains("dagger") || itemName.contains("axe") || itemName.contains("mace") || itemName.contains("claws") || itemName.contains("sword") || 
			   itemName.contains("scim") || itemName.contains("spear") || itemName.contains("hammer") || itemName.contains("halberd")) {
				attackLevelReq = 60;
				if (itemName.contains("halberd")) 
				{
					strengthLevelReq = 30;
				}
			}
			if (itemName.contains("body") || itemName.contains("shield") || itemName.contains("legs") || itemName.contains("skirt") || itemName.contains("helm") || itemName.contains("boots")) {
				defenceLevelReq = 60;
			}
			return;
		}
		if (itemName.contains("initiate")) {
			defenceLevelReq = 20;
			prayerLevelReq = 10;
			return;
		}
		//god armor
		if (itemName.contains("saradomin") || itemName.contains("zamorak") || itemName.contains("guthix")) {
			if (itemName.contains("body") || itemName.contains("legs") || itemName.contains("full") || itemName.contains("kite") || itemName.contains("skirt")) {
				defenceLevelReq = 40;
			}
			if (itemName.contains("mitre")) {
				magicLevelReq = 40;
				prayerLevelReq = 40;
			}
			if (itemName.contains("cloak")) {
				prayerLevelReq = 40;
			}
			if(itemName.contains("d'hide") || itemName.contains("coif") || itemName.contains("chaps") || itemName.contains("bracers"))
			{
				defenceLevelReq = 40;
				rangeLevelReq = 70;
			}
			return;
		}
		if(Degradeables.getDegradeableItem(new Item(itemId)) != null && Degradeables.getDegradeableItem(new Item(itemId)).name().toLowerCase().contains("crystal")) {
		    Degradeables d = Degradeables.getDegradeableItem(new Item(itemId));
		    agilityLevelReq = 50;
		    if(d.equals(Degradeables.CRYSTAL_BOW)) {
			rangeLevelReq = 70;
		    }
		    if(d.equals(Degradeables.CRYSTAL_SHIELD)) {
			defenceLevelReq = 70;
		    }
		    return;
		}
		if(Degradeables.getDegradeableItem(new Item(itemId)) != null && !Degradeables.getDegradeableItem(new Item(itemId)).name().toLowerCase().contains("crystal")) {
		    Degradeables d = Degradeables.getDegradeableItem(new Item(itemId));
		    if(d.name().toLowerCase().contains("karils")) {
			rangeLevelReq = 70;
		    }
		    if(d.name().toLowerCase().contains("ahrims")) {
			magicLevelReq = 70;
		    }
		    if(d.getEquipSlot() != Constants.WEAPON) {
			defenceLevelReq = 70;
		    }
		    if(d.getEquipSlot() == Constants.WEAPON && !d.equals(Degradeables.KARILS_CROSSBOW)) {
			attackLevelReq = 70;
			if(d.equals(Degradeables.DHAROKS_AXE) || d.equals(Degradeables.TORAGS_HAMMERS)) {
			    strengthLevelReq = 70;
			}
		    }
		    return;
		}
		//range
		if (itemName.contains("arrow")) {
			if (itemName.contains("ice")) {
				rangeLevelReq = 40;
			} else if (itemName.contains("broad")) {
				rangeLevelReq = 50;
				slayerLevelReq = 55;
			}
			return;
		}
		if (itemName.contains("bow")) {
			if(itemName.equalsIgnoreCase("shortbow") || itemName.equalsIgnoreCase("longbow")){
				rangeLevelReq = 1;
			} else if (itemName.contains("oak")) {
				rangeLevelReq = 5;
			} else if (itemName.contains("willow")) {
				rangeLevelReq = 20;
			} else if (itemName.contains("maple") || itemName.contains("ogre")) {
				rangeLevelReq = 30;
			} else if (itemName.contains("yew")) {
				rangeLevelReq = 40;
			} else if (itemName.contains("magic")) {
				rangeLevelReq = 50;
			}
			return;
		}
		if (itemName.contains("hardleather")) {
			defenceLevelReq = 10;
			return;
		}
		if (itemName.contains("studded")) {
			rangeLevelReq = 20;
			if (!itemName.contains("chaps")) {
				defenceLevelReq = 20;
			}
			return;
		}
		//dhide
		if (itemName.toLowerCase().contains("d'hide")) {
			if(itemName.contains("chaps"))
			{
				defenceLevelReq = 1;
			}
			if(itemName.contains("black"))
			{
				rangeLevelReq = 70;
			}
			if(itemName.contains("red"))
			{
				rangeLevelReq = 60;
			}
			if(itemName.contains("blue"))
			{
				rangeLevelReq = 50;
			}
			if(itemName.contains("green"))
			{
				rangeLevelReq = 40;
			}
			if(itemName.contains("body"))
			{
				defenceLevelReq = 40;
			}
			return;
		}
		if (itemName.contains("snakeskin")) {
			rangeLevelReq = 30;
			defenceLevelReq = 30;
			return;
		}
		//magic
		if (itemName.contains("mystic") || itemName.contains("nchanted")) {
			if (itemName.contains("staff")) {
				magicLevelReq = 40;
				attackLevelReq = 40;
			} else {
				magicLevelReq = 40;
				defenceLevelReq = 20;
			}
			return;
		}
		if (itemName.contains("infinity")) {
			magicLevelReq = 50;
			defenceLevelReq = 25;
			return;
		}
		if (itemName.contains("splitbark")) {
			magicLevelReq = 40;
			defenceLevelReq = 40;
			return;
		}
		if (itemName.contains("enchanted")) {
			defenceLevelReq = 40;
			return;
		}
		if (itemName.contains("ancient")) {
			attackLevelReq = 50;
			return;
		}
		//strength
		if (itemName.contains("tzhaar-ket-om")) {
			strengthLevelReq = 60;
			return;
		}
		//specials
		if (itemName.toLowerCase().contains("granite")) {
			if (!itemName.toLowerCase().contains("maul")) {
			    if(itemName.toLowerCase().contains("legs")) {
				strengthLevelReq = 50;
			    }
				defenceLevelReq = 50;
			}
			return;
		}
		if (itemName.contains("warrior")) {
			if (!itemName.contains("ring")) {
				defenceLevelReq = 40;
			}
			return;
		}
	}

	/* some math method */
	public String cutDouble(double Number) {
		double dNumber = Number;
		String masque = new String("#0.##");
		DecimalFormat form = new DecimalFormat(masque);
		return form.format(dNumber).replaceAll(",", ".");
	}

	public void checkBarrowsGear() {
		player.setFullAhrim(fullAhrim());
		player.setFullKaril(fullKaril());
		player.setFullDharok(fullDharok());
		player.setFullVerac(fullVerac());
		player.setFullTorag(fullTorag());
		player.setFullGuthan(fullGuthan());
	}
	
	public void checkVoidGear() {
	    player.setFullVoidMelee(fullVoidMelee());
	    player.setFullVoidRange(fullVoidRange());
	    player.setFullVoidMage(fullVoidMage());
	    player.setVoidMace(voidMace());
	}

	public void checkRangeGear() {
		String wep = player.getEquippedWeapon().name().toLowerCase();
		player.setUsingBow((wep.contains("seercull") || wep.contains("bow")) && !wep.contains("crossbow") && !wep.contains("c'bow"));
		player.setUsingCross(wep.contains("crossbow") || wep.contains("x-bow") || wep.contains("c'bow"));
		player.setUsingOtherRangedWeapon(wep.contains("knife") || wep.contains("dart") || wep.contains("javelin") || wep.contains("thrownaxe") || wep.contains("toktz-xil-ul") || wep.contains("throwing axe"));
		player.setUsingCrystalBow(wep.contains("crystal bow"));
		String ammo = ItemManager.getInstance().getItemName(player.getEquipment().getId(Constants.ARROWS)).toLowerCase();
		player.setUsingArrows(ammo.contains("arrow"));
		player.setUsingBolts(ammo.contains("bolt"));
		player.setDropArrow(!wep.contains("crystal") && !wep.contains("karils"));
	}

	public boolean fullAhrim() {
		if (player.getEquipment().getItemContainer().get(Constants.HAT) == null || player.getEquipment().getItemContainer().get(Constants.LEGS) == null || player.getEquipment().getItemContainer().get(Constants.CHEST) == null || player.getEquipment().getItemContainer().get(Constants.WEAPON) == null)
			return false;

		return Degradeables.fullBarrowsSet(player.getEquipment().getItemContainer().get(Constants.HAT), player.getEquipment().getItemContainer().get(Constants.CHEST), player.getEquipment().getItemContainer().get(Constants.LEGS), player.getEquipment().getItemContainer().get(Constants.WEAPON), "AHRIMS");
	}

	public boolean fullKaril() {
		if (player.getEquipment().getItemContainer().get(Constants.HAT) == null || player.getEquipment().getItemContainer().get(Constants.LEGS) == null || player.getEquipment().getItemContainer().get(Constants.CHEST) == null || player.getEquipment().getItemContainer().get(Constants.WEAPON) == null)
			return false;

		return Degradeables.fullBarrowsSet(player.getEquipment().getItemContainer().get(Constants.HAT), player.getEquipment().getItemContainer().get(Constants.CHEST), player.getEquipment().getItemContainer().get(Constants.LEGS), player.getEquipment().getItemContainer().get(Constants.WEAPON), "KARILS");
	}

	public boolean fullVerac() {
		if (player.getEquipment().getItemContainer().get(Constants.HAT) == null || player.getEquipment().getItemContainer().get(Constants.LEGS) == null || player.getEquipment().getItemContainer().get(Constants.CHEST) == null || player.getEquipment().getItemContainer().get(Constants.WEAPON) == null)
			return false;

		return Degradeables.fullBarrowsSet(player.getEquipment().getItemContainer().get(Constants.HAT), player.getEquipment().getItemContainer().get(Constants.CHEST), player.getEquipment().getItemContainer().get(Constants.LEGS), player.getEquipment().getItemContainer().get(Constants.WEAPON), "VERACS");
	}

	public boolean fullGuthan() {
		if (player.getEquipment().getItemContainer().get(Constants.HAT) == null || player.getEquipment().getItemContainer().get(Constants.LEGS) == null || player.getEquipment().getItemContainer().get(Constants.CHEST) == null || player.getEquipment().getItemContainer().get(Constants.WEAPON) == null)
			return false;

		return Degradeables.fullBarrowsSet(player.getEquipment().getItemContainer().get(Constants.HAT), player.getEquipment().getItemContainer().get(Constants.CHEST), player.getEquipment().getItemContainer().get(Constants.LEGS), player.getEquipment().getItemContainer().get(Constants.WEAPON), "GUTHANS");
	}

	public boolean fullDharok() {
		if (player.getEquipment().getItemContainer().get(Constants.HAT) == null || player.getEquipment().getItemContainer().get(Constants.LEGS) == null || player.getEquipment().getItemContainer().get(Constants.CHEST) == null || player.getEquipment().getItemContainer().get(Constants.WEAPON) == null)
			return false;

		return Degradeables.fullBarrowsSet(player.getEquipment().getItemContainer().get(Constants.HAT), player.getEquipment().getItemContainer().get(Constants.CHEST), player.getEquipment().getItemContainer().get(Constants.LEGS), player.getEquipment().getItemContainer().get(Constants.WEAPON), "DHAROKS");
	}

	public boolean fullTorag() {
		if (player.getEquipment().getItemContainer().get(Constants.HAT) == null || player.getEquipment().getItemContainer().get(Constants.LEGS) == null || player.getEquipment().getItemContainer().get(Constants.CHEST) == null || player.getEquipment().getItemContainer().get(Constants.WEAPON) == null)
			return false;

		return Degradeables.fullBarrowsSet(player.getEquipment().getItemContainer().get(Constants.HAT), player.getEquipment().getItemContainer().get(Constants.CHEST), player.getEquipment().getItemContainer().get(Constants.LEGS), player.getEquipment().getItemContainer().get(Constants.WEAPON), "TORAGS");
	}
	
	public boolean fullVoidMelee() {
		if (player.getEquipment().getItemContainer().get(Constants.HAT) == null || player.getEquipment().getItemContainer().get(Constants.LEGS) == null || player.getEquipment().getItemContainer().get(Constants.CHEST) == null || player.getEquipment().getItemContainer().get(Constants.HANDS) == null)
			return false;

		return (player.getEquipment().getItemContainer().get(Constants.HAT).getId() == 11665 && player.getEquipment().getItemContainer().get(Constants.CHEST).getId() == 8839 && player.getEquipment().getItemContainer().get(Constants.LEGS).getId() == 8840 && player.getEquipment().getItemContainer().get(Constants.HANDS).getId() == 8842);
	}
	public boolean fullVoidRange() {
		if (player.getEquipment().getItemContainer().get(Constants.HAT) == null || player.getEquipment().getItemContainer().get(Constants.LEGS) == null || player.getEquipment().getItemContainer().get(Constants.CHEST) == null || player.getEquipment().getItemContainer().get(Constants.HANDS) == null)
			return false;

		return (player.getEquipment().getItemContainer().get(Constants.HAT).getId() == 11664 && player.getEquipment().getItemContainer().get(Constants.CHEST).getId() == 8839 && player.getEquipment().getItemContainer().get(Constants.LEGS).getId() == 8840 && player.getEquipment().getItemContainer().get(Constants.HANDS).getId() == 8842);
	}
	public boolean fullVoidMage() {
		if (player.getEquipment().getItemContainer().get(Constants.HAT) == null || player.getEquipment().getItemContainer().get(Constants.LEGS) == null || player.getEquipment().getItemContainer().get(Constants.CHEST) == null || player.getEquipment().getItemContainer().get(Constants.HANDS) == null)
			return false;

		return (player.getEquipment().getItemContainer().get(Constants.HAT).getId() == 11663 && player.getEquipment().getItemContainer().get(Constants.CHEST).getId() == 8839 && player.getEquipment().getItemContainer().get(Constants.LEGS).getId() == 8840 && player.getEquipment().getItemContainer().get(Constants.HANDS).getId() == 8842);
	}
	public boolean voidMace() {
		if (player.getEquipment().getItemContainer().get(Constants.WEAPON) == null)
			return false;
		return (player.getEquipment().getItemContainer().get(Constants.WEAPON).getId() == 8841);
	    
	}

}
