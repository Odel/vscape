package com.rs2.model.content.skills.runecrafting;

import com.rs2.Constants;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.magic.Spell;
import com.rs2.model.players.Player;
import com.rs2.model.players.container.inventory.Inventory;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemManager;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class TabHandler {
    private static final int AIR_RUNE = 556;
    private static final int EARTH_RUNE = 557;
    private static final int FIRE_RUNE = 554;
    private static final int WATER_RUNE = 555;
    private static final int LAW_RUNE = 563;
    private static final int NATURE_RUNE = 561;
    private static final int COSMIC_RUNE = 564;
    private static final int SOFT_CLAY = 1761;
    
    private static final int FIRE_STAFF[] = {1387, 1393, 1401, 3053, 3054};
    private static final int AIR_STAFF[] = {1381, 1397, 1405};
    private static final int WATER_STAFF[] = {1383, 1395, 1403, 6562, 6563}; //11736 & 11738 - steam battlestaff and mystic steam
    private static final int EARTH_STAFF[] = {1385, 1399, 1407, 3053, 3054, 6562, 6563};
    
    private static final int VARROCK = 1;
    private static final int LUMBRIDGE = 2;
    private static final int FALADOR = 3;
    private static final int CAMELOT = 4;
    private static final int ARDOUGNE = 5;
    private static final int WATCHTOWER = 6;
    
    private static final int SAPPHIRE = 7;
    private static final int EMERALD = 8;
    private static final int RUBY = 9;
    private static final int DIAMOND = 10;
    private static final int DRAGONSTONE = 11;
    private static final int ONYX = 12;
    
    private static final int BANANAS = 13;
    private static final int PEACHES = 14;
    
    public static final int[] TABS = {0, 8007, 8008, 8009, 8010, 8011, 8012, 8016, 8017, 8018, 8019, 8020, 8021, 8014, 8015};
    
    private static final int[][] VARROCK_RUNES = { {FIRE_RUNE, 1}, {AIR_RUNE, 3}, {LAW_RUNE, 1} };
    private static final int[][] LUMBRIDGE_RUNES = { {EARTH_RUNE, 1}, {AIR_RUNE, 3}, {LAW_RUNE, 1} };
    private static final int[][] FALADOR_RUNES = { {WATER_RUNE, 1}, {AIR_RUNE, 3}, {LAW_RUNE, 1} };
    private static final int[][] CAMELOT_RUNES = { {AIR_RUNE, 5}, {LAW_RUNE, 1} };
    private static final int[][] ARDOUGNE_RUNES = { {WATER_RUNE, 2}, {LAW_RUNE, 2} };
    private static final int[][] WATCHTOWER_RUNES = { {EARTH_RUNE, 2}, {LAW_RUNE, 2} };
    
    private static final int[][] SAPPHIRE_RUNES = { {WATER_RUNE, 1}, {COSMIC_RUNE, 1} };
    private static final int[][] EMERALD_RUNES = { {AIR_RUNE, 2}, {COSMIC_RUNE, 1} };
    private static final int[][] RUBY_RUNES = { {FIRE_RUNE, 5}, {COSMIC_RUNE, 1} };
    private static final int[][] DIAMOND_RUNES = { {EARTH_RUNE, 10}, {COSMIC_RUNE, 1} };
    private static final int[][] DRAGONSTONE_RUNES = { {WATER_RUNE, 15}, {EARTH_RUNE, 15}, {COSMIC_RUNE, 1} };
    private static final int[][] ONYX_RUNES = { {FIRE_RUNE, 20}, {EARTH_RUNE, 20}, {COSMIC_RUNE, 1} };
    
    private static final int[][] BANANAS_RUNES = { {EARTH_RUNE, 2}, {WATER_RUNE, 2}, {NATURE_RUNE, 1} };
    private static final int[][] PEACHES_RUNES = { {EARTH_RUNE, 4}, {WATER_RUNE, 2}, {NATURE_RUNE, 2} };
    
    public static final int[][] ENCHANTABLES = {
  			{1637, 1694, 1656, 11072}, // sapphire
			{1639, 1696, 1658, 11076}, // emerald
			{1641, 1698, -1, 11085}, // ruby
			{1643, 1700, -1, 11092}, // diamond
			{1645, 1702, 1664, 11115}, // dragonstone
			{6575, 6581, 6577, 11130} // onyx
    };
    private static final int[][] ENCHANTED = {
  			{2550, 1727, 3853, 11074}, // sapphire
			{2552, 1729, 5521, 11079}, // emerald
			{2568, 1725, -1, 11088}, // ruby
			{2570, 1731, -1, 11095}, // diamond
			{2572, 1712, 11105, 11118}, // dragonstone
			{6583, 6585, 11128, 11133} // onyx
	};
    
    public static boolean itemOnItemHandling(Player player, int firstItem, int secondItem) {
	if(firstItem == LAW_RUNE && secondItem == SOFT_CLAY || firstItem == SOFT_CLAY && secondItem == LAW_RUNE) {
	    Dialogues.startDialogue(player, 10565);
	    return true;
	}
	else if(firstItem == COSMIC_RUNE && secondItem == SOFT_CLAY || firstItem == SOFT_CLAY && secondItem == COSMIC_RUNE) {
	    Dialogues.startDialogue(player, 10567);
	    return true;
	}
	else if(firstItem == NATURE_RUNE && secondItem == SOFT_CLAY || firstItem == SOFT_CLAY && secondItem == NATURE_RUNE) {
	    Dialogues.startDialogue(player, 10568);
	    return true;
	}
	else if((firstItem >= 8016 && firstItem < 8022 ) && isChantableItemForLevel(secondItem, getChantableArrayIndex(firstItem))) {
	    /*useEnchantTab(player, firstItem, secondItem, getChantableArrayIndex(firstItem));
	    return true;*/ //too much trouble for now, doesn't quite make much sense anyways
	    return false;
	}
	return false;
    }
    
    public static boolean fireStaff(int itemId) {
	for(int i : FIRE_STAFF) {
	    if(i == itemId) return true;
	}
	return false;
    }
    
    public static boolean airStaff(int itemId) {
	for(int i : AIR_STAFF) {
	    if(i == itemId) return true;
	}
	return false;
    }
    
    public static boolean waterStaff(int itemId) {
	for(int i : WATER_STAFF) {
	    if(i == itemId) return true;
	}
	return false;
    }
    
    public static boolean earthStaff(int itemId) {
	for(int i : EARTH_STAFF) {
	    if(i == itemId) return true;
	}
	return false;
    }
    
    public static boolean checkRunes(int sp, Player player) {
	Inventory i = player.getInventory();
	int id = player.getEquipment().getId(Constants.WEAPON);
	if(getRunes(sp).length == 3) {
	    if (i.playerHasItem(getRunes(sp)[0][0], getRunes(sp)[0][1]) && i.playerHasItem(getRunes(sp)[1][0], getRunes(sp)[1][1]) && i.playerHasItem(getRunes(sp)[2][0], getRunes(sp)[2][1])) {
		return true;
	    } else if (checkStaffs(getRunes(sp)[0][0], id) && i.playerHasItem(getRunes(sp)[1][0], getRunes(sp)[1][1]) && i.playerHasItem(getRunes(sp)[2][0], getRunes(sp)[2][1])) {
		return true;
	    }
	    return i.playerHasItem(getRunes(sp)[0][0], getRunes(sp)[0][1]) && checkStaffs(getRunes(sp)[1][0], id) && i.playerHasItem(getRunes(sp)[2][0], getRunes(sp)[2][1]);
	}
	else if(getRunes(sp).length == 2) {
	    if (i.playerHasItem(getRunes(sp)[0][0], getRunes(sp)[0][1]) && i.playerHasItem(getRunes(sp)[1][0], getRunes(sp)[1][1])) {
		return true;
	    } else if (checkStaffs(getRunes(sp)[0][0], id) && i.playerHasItem(getRunes(sp)[1][0], getRunes(sp)[1][1])) {
		return true;
	    }
	    return i.playerHasItem(getRunes(sp)[1][0], getRunes(sp)[1][1]) && checkStaffs(getRunes(sp)[0][0], id);
	}
	else {
	    return false;
	}
    }
    public static boolean breakBonesTab(final Player player, final int item, int slot) {
	player.getActionSender().removeInterfaces();
	int fruit = item == 8014 ? 1963 : 6883;
	if (!player.getInventory().playerHasItem(item)) {
	    return false;
	}
	if(item != 8014 && item != 8015) {
	    return false;
	}
	if (!player.getInventory().playerHasItem(526)) {
	    player.getActionSender().sendMessage("You don't have any bones to convert into fruit.");
	    return true;
	}
	else {
	    player.getUpdateFlags().sendAnimation(getSpell(item).getAnimation());
	    player.getUpdateFlags().sendGraphic(getSpell(item).getGraphic());
	    player.getInventory().removeItem(new Item(item));
	    player.getActionSender().sendMessage("You break the tab.");
	    for (Item items : player.getInventory().getItemContainer().getItems()) {
		if (items == null) continue;
		if (items.getId() == 526) {
		    player.getInventory().replaceItemWithItem(items, new Item(fruit));
		}
	    }
	    return true;
	}
    }
    public static boolean breakEnchantTab(final Player player, final int item, int slot) {
	player.getActionSender().removeInterfaces();
	final int index = getChantableArrayIndex(item);
	if (!player.getInventory().playerHasItem(item)) {
	    return false;
	}
	if (!isEnchantTab(item)) {
	    return false;
	}
	if (!hasChantableItemForEnchant(player, item, index)) {
	    player.getActionSender().sendMessage("You do not have an enchantable item of that level in your inventory!");
	    return true;
	}
	if (getChantableItemForEnchant(player, item, index) == null) {
	    return false;
	} else if (hasChantableItemForEnchant(player, item, index)) {
	    final int indexTwo = getChantableItemForEnchant(player, item, index)[0];
	    player.getActionSender().sendMessage("You enchant the " + ItemManager.getInstance().getItemName(ENCHANTABLES[index][indexTwo]) + ".");
	    player.getInventory().removeItem(new Item(TABS[index + 7]));
	    player.setStopPacket(true);
	    CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		@Override
		public void execute(CycleEventContainer b) {
		    player.getUpdateFlags().sendAnimation(getSpell(item).getAnimation());
		    player.getUpdateFlags().sendGraphic(getSpell(item).getGraphic());
		    player.getInventory().replaceItemWithItem(new Item(getChantableItemForEnchant(player, item, index)[1]), new Item(ENCHANTED[index][indexTwo]));
		    b.stop();
		}

		@Override
		public void stop() {
		    player.setStopPacket(false);
		}
	    }, 3);
	    return true;
	} else {
	    return false;
	}
    }
    
    public static void useEnchantTab(final Player player, final int firstItem, final int secondItem, final int enchantArraySlot) {
	final int index = getChantableArrayIndex(firstItem);
	player.getActionSender().sendMessage("You enchant the " + ItemManager.getInstance().getItemName(secondItem) + ".");
	player.getInventory().removeItem(new Item(firstItem));
	player.getInventory().removeItem(new Item(secondItem));
	player.setStopPacket(true);
	CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
	    @Override
	    public void execute(CycleEventContainer b) {
		player.getActionSender().removeInterfaces();
		if (!player.getInventory().playerHasItem(firstItem)) {
		    b.stop();
		    return;
		}
		if (getChantableItemForEnchant(player, firstItem, index) == null) {
		    b.stop();
		    return;
		}
		player.getUpdateFlags().sendAnimation(getSpell(firstItem).getAnimation());
		player.getUpdateFlags().sendGraphic(getSpell(firstItem).getGraphic());
		int indexTwo = getChantableItemForEnchant(player, firstItem, index)[0];
		player.getInventory().addItem(new Item(ENCHANTED[index][indexTwo]));
		b.stop();
	    }

	    @Override
	    public void stop() {
		player.setStopPacket(false);
	    }
	}, 3);
    }
    
    public static int getChantableArrayIndex(final int itemId) {
	for(int i = 0; i < TABS.length; i++) {
	    if(itemId >= 8016 && itemId < 8022 && itemId == TABS[i]) { 
		return i - 7;
	    }
	}
	return -1;
    }
    
    public static boolean isChantableItemForLevel(final int itemId, final int index) {
	for(int i : ENCHANTABLES[index]) {
	    if(i == -1) continue;
	    if(i == itemId) return true;
	}
	return false;
    }
    public static boolean isEnchantTab(final int itemId) {
	return itemId >= 8016 && itemId < 8022;
    }
    public static boolean hasChantableItemForEnchant(final Player player, final int itemId, final int enchant) {
	for(Item item : player.getInventory().getItemContainer().getItems()) {
	    if(item == null) continue;
	    for (int x = 0; x < 4; x++) {
		if (ENCHANTABLES[enchant][x] == item.getId()) {
		    return true;
		}
	    }
	}
	return false;
    }
    
    
    public static int[] getChantableItemForEnchant(final Player player, final int itemId, final int enchant) {
	for(Item item : player.getInventory().getItemContainer().getItems()) {
	    if(item == null) continue;
		for(int x = 0; x < 4; x++) {
		    if(ENCHANTABLES[enchant][x] == item.getId()) {
			int[] array = {x, ENCHANTABLES[enchant][x]};
			return array;
		    }
		}
	}
	return null;
    }
    public static Spell getSpell(int itemId) {
	if(itemId == TABS[SAPPHIRE]) return Spell.ENCHANT_LV_1;
	else if(itemId == TABS[EMERALD]) return Spell.ENCHANT_LV_2;
	else if(itemId == TABS[RUBY]) return Spell.ENCHANT_LV_3;
	else if(itemId == TABS[DIAMOND]) return Spell.ENCHANT_LV_4;
	else if(itemId == TABS[DRAGONSTONE]) return Spell.ENCHANT_LV_5;
	else if(itemId == TABS[ONYX]) return Spell.ENCHANT_LV_6;
	else if(itemId == TABS[BANANAS]) return Spell.BONES_TO_BANANA;
	else if(itemId == TABS[PEACHES]) return Spell.BONES_TO_PEACH;
	else return null;
    }

    public static int[][] getRunes(int spell) {
	switch(spell) {
	    case VARROCK:
		return VARROCK_RUNES;
	    case LUMBRIDGE: 
		return LUMBRIDGE_RUNES;
	    case FALADOR:
		return FALADOR_RUNES;
	    case CAMELOT:
		return CAMELOT_RUNES;
	    case ARDOUGNE:
		return ARDOUGNE_RUNES;
	    case WATCHTOWER:
		return WATCHTOWER_RUNES;
	    case SAPPHIRE:
		return SAPPHIRE_RUNES;
	    case EMERALD:
		return EMERALD_RUNES;
	    case RUBY:
		return RUBY_RUNES;
	    case DIAMOND:
		return DIAMOND_RUNES;
	    case DRAGONSTONE:
		return DRAGONSTONE_RUNES;
	    case ONYX:
		return ONYX_RUNES;
	    case BANANAS:
		return BANANAS_RUNES;
	    case PEACHES:
		return PEACHES_RUNES;
	}
	return null;
    }
    
    public static boolean checkStaffs(int rune, int weaponId) {
	if(rune == FIRE_RUNE && fireStaff(weaponId)) {
	    return true;
	}
	else if(rune == AIR_RUNE && airStaff(weaponId)) {
	    return true;
	}
	else if(rune == WATER_RUNE && waterStaff(weaponId)) {
	    return true;
	}
	else if(rune == EARTH_RUNE && earthStaff(weaponId)) {
	    return true;
	}
	else {
	    return false;
	}
    }
    
    public static void craftTab(final Player player,final int spell, final int count) {
	final int task = player.getTask();
	player.setSkilling(new CycleEvent() {
	int amnt = count;

	    @Override
	    public void execute(CycleEventContainer container) {
		int weaponId = player.getEquipment().getId(Constants.WEAPON);
		if (!player.checkTask(task) || !checkRunes(spell, player) || amnt == 0) {
		    container.stop();
		    return;
		}
		if (!player.getInventory().playerHasItem(SOFT_CLAY)) {
		    player.getActionSender().sendMessage("You have run out of soft clay!");
		    container.stop();
		    return;
		}
		player.getUpdateFlags().sendAnimation(791);
		player.getActionSender().sendMessage("You craft the tab.");
		if(!checkStaffs(getRunes(spell)[0][0], weaponId)) {
		    player.getInventory().removeItem(new Item(getRunes(spell)[0][0], getRunes(spell)[0][1]));
		}
		if(!checkStaffs(getRunes(spell)[1][0], weaponId)) {
		    player.getInventory().removeItem(new Item(getRunes(spell)[1][0], getRunes(spell)[1][1]));
		}
		if(getRunes(spell).length > 2 && !checkStaffs(getRunes(spell)[2][0], weaponId)) {
		    player.getInventory().removeItem(new Item(getRunes(spell)[2][0], getRunes(spell)[2][1]));
		}
		player.getInventory().removeItem(new Item(SOFT_CLAY));
		player.getInventory().addItem(new Item(TABS[spell]));
		amnt--;

	    }

	    @Override
	    public void stop() {
		player.resetAnimation();
	    }
	});
	CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 3);
	return;
    }
    
    public static boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
	switch(id) {
	    case 10565:
		switch (player.getDialogue().getChatId()) {
		    case 1:
			player.getDialogue().sendOption("Varrock", "Lumbridge", "Falador", "Camelot", "More...");
			return true;
		    case 2:
			switch(optionId) {
			    case 1:
				if(checkRunes(VARROCK, player) && player.getSkill().getLevel()[Skill.MAGIC] >= 25) {
				    player.getDialogue().sendOption("1", "5", "10", "25");
				    return true;
				}
				else if(checkRunes(VARROCK, player) && player.getSkill().getLevel()[Skill.MAGIC] < 25) {
				    player.getDialogue().sendStatement("You need 25 magic to craft this tab.");
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!checkRunes(VARROCK, player) && player.getSkill().getLevel()[Skill.MAGIC] >= 25) {
				    player.getDialogue().sendStatement("You do not have the runes required.");
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 2:
				if(checkRunes(LUMBRIDGE, player) && player.getSkill().getLevel()[Skill.MAGIC] >= 31) {
				    player.getDialogue().sendOption("1", "5", "10", "25");
				    player.getDialogue().setNextChatId(4);
				    return true;
				}
				else if(checkRunes(LUMBRIDGE, player) && player.getSkill().getLevel()[Skill.MAGIC] < 31) {
				    player.getDialogue().sendStatement("You need 31 magic to craft this tab.");
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!checkRunes(LUMBRIDGE, player) && player.getSkill().getLevel()[Skill.MAGIC] >= 31) {
				    player.getDialogue().sendStatement("You do not have the runes required.");
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 3:
				if(checkRunes(FALADOR, player) && player.getSkill().getLevel()[Skill.MAGIC] >= 37) {
				    player.getDialogue().sendOption("1", "5", "10", "25");
				    player.getDialogue().setNextChatId(5);
				    return true;
				}
				else if(checkRunes(FALADOR, player) && player.getSkill().getLevel()[Skill.MAGIC] < 37) {
				    player.getDialogue().sendStatement("You need 37 magic to craft this tab.");
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!checkRunes(FALADOR, player) && player.getSkill().getLevel()[Skill.MAGIC] >= 37) {
				    player.getDialogue().sendStatement("You do not have the runes required.");
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 4:
				if(checkRunes(CAMELOT, player) && player.getSkill().getLevel()[Skill.MAGIC] >= 45) {
				    player.getDialogue().sendOption("1", "5", "10", "25");
				    player.getDialogue().setNextChatId(6);
				    return true;
				}
				else if(checkRunes(CAMELOT, player) && player.getSkill().getLevel()[Skill.MAGIC] < 45) {
				    player.getDialogue().sendStatement("You need 45 magic to craft this tab.");
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!checkRunes(CAMELOT, player) && player.getSkill().getLevel()[Skill.MAGIC] >= 45) {
				    player.getDialogue().sendStatement("You do not have the runes required.");
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 5:
				player.getDialogue().sendOption("Ardougne", "Watchtower", "Back");
				player.getDialogue().setNextChatId(15);
				return true;
			}
		    case 3:
			switch(optionId) {
			    case 1:
				craftTab(player, VARROCK, 1);
				break;
			    case 2:
				craftTab(player, VARROCK, 5);
				break;
			    case 3:
				craftTab(player, VARROCK, 10);
				break;
			    case 4:
				craftTab(player, VARROCK, 25);
				break;
			}
		    break;
		    case 4:
			switch(optionId) {
			    case 1:
				craftTab(player, LUMBRIDGE, 1);
				break;
			    case 2:
				craftTab(player, LUMBRIDGE, 5);
				break;
			    case 3:
				craftTab(player, LUMBRIDGE, 10);
				break;
			    case 4:
				craftTab(player, LUMBRIDGE, 25);
				break;
			}
		    break;
		    case 5:
			switch(optionId) {
			    case 1:
				craftTab(player, FALADOR, 1);
				break;
			    case 2:
				craftTab(player, FALADOR, 5);
				break;
			    case 3:
				craftTab(player, FALADOR, 10);
				break;
			    case 4:
				craftTab(player, FALADOR, 25);
				break;
			}
		    break;
		    case 6:
			switch(optionId) {
			    case 1:
				craftTab(player, CAMELOT, 1);
				break;
			    case 2:
				craftTab(player, CAMELOT, 5);
				break;
			    case 3:
				craftTab(player, CAMELOT, 10);
				break;
			    case 4:
				craftTab(player, CAMELOT, 25);
				break;
			}
		    break;
		    case 7:
			switch(optionId) {
			    case 1:
				craftTab(player, ARDOUGNE, 1);
				break;
			    case 2:
				craftTab(player, ARDOUGNE, 5);
				break;
			    case 3:
				craftTab(player, ARDOUGNE, 10);
				break;
			    case 4:
				craftTab(player, ARDOUGNE, 25);
				break;
			}
		    break;
		    case 8:
			switch(optionId) {
			    case 1:
				craftTab(player, WATCHTOWER, 1);
				break;
			    case 2:
				craftTab(player, WATCHTOWER, 5);
				break;
			    case 3:
				craftTab(player, WATCHTOWER, 10);
				break;
			    case 4:
				craftTab(player, WATCHTOWER, 25);
				break;
			}
		    break;
		    case 15:
			switch(optionId) {
			    case 1:
				if(checkRunes(ARDOUGNE, player) && player.getSkill().getLevel()[Skill.MAGIC] >= 51) {
				    player.getDialogue().sendOption("1", "5", "10", "25");
				    player.getDialogue().setNextChatId(7);
				    return true;
				}
				else if(checkRunes(ARDOUGNE, player) && player.getSkill().getLevel()[Skill.MAGIC] < 51) {
				    player.getDialogue().sendStatement("You need 51 magic to craft this tab.");
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!checkRunes(ARDOUGNE, player) && player.getSkill().getLevel()[Skill.MAGIC] >= 51) {
				    player.getDialogue().sendStatement("You do not have the runes required.");
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 2:
				if(checkRunes(WATCHTOWER, player) && player.getSkill().getLevel()[Skill.MAGIC] >= 58) {
				    player.getDialogue().sendOption("1", "5", "10", "25");
				    player.getDialogue().setNextChatId(8);
				    return true;
				}
				else if(checkRunes(WATCHTOWER, player) && player.getSkill().getLevel()[Skill.MAGIC] < 58) {
				    player.getDialogue().sendStatement("You need 58 magic to craft this tab.");
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!checkRunes(WATCHTOWER, player) && player.getSkill().getLevel()[Skill.MAGIC] >= 58) {
				    player.getDialogue().sendStatement("You do not have the runes required.");
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 3:
				player.getDialogue().setNextChatId(1);
				return true;
			}
		}
	break;
	case 10567:
		switch (player.getDialogue().getChatId()) {
		    case 1:
			player.getDialogue().sendOption("Enchant Sapphire", "Enchant Emerald", "Enchant Ruby", "Enchant Diamond", "More...");
			return true;
		    case 2:
			switch(optionId) {
			    case 1:
				if(checkRunes(SAPPHIRE, player) && player.getSkill().getLevel()[Skill.MAGIC] >= 7) {
				    player.getDialogue().sendOption("1", "5", "10", "25");
				    return true;
				}
				else if(checkRunes(SAPPHIRE, player) && player.getSkill().getLevel()[Skill.MAGIC] < 7) {
				    player.getDialogue().sendStatement("You need 7 magic to craft this tab.");
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!checkRunes(SAPPHIRE, player) && player.getSkill().getLevel()[Skill.MAGIC] >= 7) {
				    player.getDialogue().sendStatement("You do not have the runes required.");
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 2:
				if(checkRunes(EMERALD, player) && player.getSkill().getLevel()[Skill.MAGIC] >= 27) {
				    player.getDialogue().sendOption("1", "5", "10", "25");
				    player.getDialogue().setNextChatId(4);
				    return true;
				}
				else if(checkRunes(EMERALD, player) && player.getSkill().getLevel()[Skill.MAGIC] < 27) {
				    player.getDialogue().sendStatement("You need 27 magic to craft this tab.");
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!checkRunes(EMERALD, player) && player.getSkill().getLevel()[Skill.MAGIC] >= 27) {
				    player.getDialogue().sendStatement("You do not have the runes required.");
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 3:
				if(checkRunes(RUBY, player) && player.getSkill().getLevel()[Skill.MAGIC] >= 49) {
				    player.getDialogue().sendOption("1", "5", "10", "25");
				    player.getDialogue().setNextChatId(5);
				    return true;
				}
				else if(checkRunes(RUBY, player) && player.getSkill().getLevel()[Skill.MAGIC] < 49) {
				    player.getDialogue().sendStatement("You need 49 magic to craft this tab.");
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!checkRunes(RUBY, player) && player.getSkill().getLevel()[Skill.MAGIC] >= 49) {
				    player.getDialogue().sendStatement("You do not have the runes required.");
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 4:
				if(checkRunes(DIAMOND, player) && player.getSkill().getLevel()[Skill.MAGIC] >= 57) {
				    player.getDialogue().sendOption("1", "5", "10", "25");
				    player.getDialogue().setNextChatId(6);
				    return true;
				}
				else if(checkRunes(DIAMOND, player) && player.getSkill().getLevel()[Skill.MAGIC] < 57) {
				    player.getDialogue().sendStatement("You need 57 magic to craft this tab.");
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!checkRunes(DIAMOND, player) && player.getSkill().getLevel()[Skill.MAGIC] >= 57) {
				    player.getDialogue().sendStatement("You do not have the runes required.");
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 5:
				player.getDialogue().sendOption("Enchant Dragonstone", "Enchant Onyx", "Back");
				player.getDialogue().setNextChatId(15);
				return true;
			}
		    case 3:
			switch(optionId) {
			    case 1:
				craftTab(player, SAPPHIRE, 1);
				break;
			    case 2:
				craftTab(player, SAPPHIRE, 5);
				break;
			    case 3:
				craftTab(player, SAPPHIRE, 10);
				break;
			    case 4:
				craftTab(player, SAPPHIRE, 25);
				break;
			}
		    break;
		    case 4:
			switch(optionId) {
			    case 1:
				craftTab(player, EMERALD, 1);
				break;
			    case 2:
				craftTab(player, EMERALD, 5);
				break;
			    case 3:
				craftTab(player, EMERALD, 10);
				break;
			    case 4:
				craftTab(player, EMERALD, 25);
				break;
			}
		    break;
		    case 5:
			switch(optionId) {
			    case 1:
				craftTab(player, RUBY, 1);
				break;
			    case 2:
				craftTab(player, RUBY, 5);
				break;
			    case 3:
				craftTab(player, RUBY, 10);
				break;
			    case 4:
				craftTab(player, RUBY, 25);
				break;
			}
		    break;
		    case 6:
			switch(optionId) {
			    case 1:
				craftTab(player, DIAMOND, 1);
				break;
			    case 2:
				craftTab(player, DIAMOND, 5);
				break;
			    case 3:
				craftTab(player, DIAMOND, 10);
				break;
			    case 4:
				craftTab(player, DIAMOND, 25);
				break;
			}
		    break;
		    case 7:
			switch(optionId) {
			    case 1:
				craftTab(player, DRAGONSTONE, 1);
				break;
			    case 2:
				craftTab(player, DRAGONSTONE, 5);
				break;
			    case 3:
				craftTab(player, DRAGONSTONE, 10);
				break;
			    case 4:
				craftTab(player, DRAGONSTONE, 25);
				break;
			}
		    break;
		    case 8:
			switch(optionId) {
			    case 1:
				craftTab(player, ONYX, 1);
				break;
			    case 2:
				craftTab(player, ONYX, 5);
				break;
			    case 3:
				craftTab(player, ONYX, 10);
				break;
			    case 4:
				craftTab(player, ONYX, 25);
				break;
			}
		    break;
		    case 15:
			switch(optionId) {
			    case 1:
				if(checkRunes(DRAGONSTONE, player) && player.getSkill().getLevel()[Skill.MAGIC] >= 68) {
				    player.getDialogue().sendOption("1", "5", "10", "25");
				    player.getDialogue().setNextChatId(7);
				    return true;
				}
				else if(checkRunes(DRAGONSTONE, player) && player.getSkill().getLevel()[Skill.MAGIC] >= 68) {
				    player.getDialogue().sendStatement("You need 68 magic to craft this tab.");
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!checkRunes(DRAGONSTONE, player) && player.getSkill().getLevel()[Skill.MAGIC] >= 68) {
				    player.getDialogue().sendStatement("You do not have the runes required.");
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 2:
				if(checkRunes(ONYX, player) && player.getSkill().getLevel()[Skill.MAGIC] >= 87) {
				    player.getDialogue().sendOption("1", "5", "10", "25");
				    player.getDialogue().setNextChatId(8);
				    return true;
				}
				else if(checkRunes(ONYX, player) && player.getSkill().getLevel()[Skill.MAGIC] < 87) {
				    player.getDialogue().sendStatement("You need 87 magic to craft this tab.");
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!checkRunes(ONYX, player) && player.getSkill().getLevel()[Skill.MAGIC] >= 87) {
				    player.getDialogue().sendStatement("You do not have the runes required.");
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 3:
				player.getDialogue().setNextChatId(1);
				return true;
			}
		}
	break;
	case 10568:
		switch (player.getDialogue().getChatId()) {
		    case 1:
			player.getDialogue().sendOption("Bones to Bananas", "Bones to Peaches", "None.");
			return true;
		    case 2:
			switch(optionId) {
			    case 1:
				if(checkRunes(BANANAS, player) && player.getSkill().getLevel()[Skill.MAGIC] >= 15) {
				    player.getDialogue().sendOption("1", "5", "10", "25");
				    return true;
				}
				else if(checkRunes(BANANAS, player) && player.getSkill().getLevel()[Skill.MAGIC] < 15) {
				    player.getDialogue().sendStatement("You need 15 magic to craft this tab.");
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!checkRunes(BANANAS, player) && player.getSkill().getLevel()[Skill.MAGIC] >= 15) {
				    player.getDialogue().sendStatement("You do not have the runes required.");
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 2:
				if(checkRunes(PEACHES, player) && player.getSkill().getLevel()[Skill.MAGIC] >= 60) {
				    player.getDialogue().sendOption("1", "5", "10", "25");
				    player.getDialogue().setNextChatId(4);
				    return true;
				}
				else if(checkRunes(PEACHES, player) && player.getSkill().getLevel()[Skill.MAGIC] < 60) {
				    player.getDialogue().sendStatement("You need 60 magic to craft this tab.");
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!checkRunes(PEACHES, player) && player.getSkill().getLevel()[Skill.MAGIC] >= 60) {
				    player.getDialogue().sendStatement("You do not have the runes required.");
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 3:
				player.getDialogue().endDialogue();
				break;
			}
		    case 3:
			switch(optionId) {
			    case 1:
				craftTab(player, BANANAS, 1);
				break;
			    case 2:
				craftTab(player, BANANAS, 5);
				break;
			    case 3:
				craftTab(player, BANANAS, 10);
				break;
			    case 4:
				craftTab(player, BANANAS, 25);
				break;
			}
		    break;
		    case 4:
			switch(optionId) {
			    case 1:
				craftTab(player, PEACHES, 1);
				break;
			    case 2:
				craftTab(player, PEACHES, 5);
				break;
			    case 3:
				craftTab(player, PEACHES, 10);
				break;
			    case 4:
				craftTab(player, PEACHES, 25);
				break;
			}
			break;
		}
	break;
	}
    return false;
    }
}