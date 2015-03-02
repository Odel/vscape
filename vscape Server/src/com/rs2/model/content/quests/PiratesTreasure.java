package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.GlobalVariables;
import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.ANNOYED;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.model.transport.Sailing;
import java.util.ArrayList;

public class PiratesTreasure implements Quest {

	public static final int QUEST_STARTED = 1;
	public static final int RUM_INTO_CRATE = 2;
	public static final int RUM_SHIPPED = 3;
	public static final int STORE_EMPLOYEE = 4;
	public static final int RUM_TO_FRANK = 5;
	public static final int BLUE_MOON_INN = 6;
	public static final int TREASURE_HUNT = 7;
	public static final int QUEST_COMPLETE = 8;

	public static final int KARAMJAN_RUM = 431;
	public static final int CHEST_KEY = 432;
	public static final int PIRATE_MESSAGE = 433;
	public static final int DIAMOND_RING = 1643;
	public static final int UNCUT_DRAGONSTONE = 1631;
	public static final int WHITE_APRON = 1005;
	public static final int SPADE = 952;
	public static final int CHEST = 6759;
	public static final int BANANA = 1963;
	public static final int SILK = 950;
	public static final int CLEANING_CLOTH = 3188;

	public static final int STORE_CRATE = 2071;
	public static final int BANANA_CRATE = 2072;
	public static final int BANANA_TREE = 2073;
	public static final int HECTOR_CHEST = 2079;
	public static final int STORE_DOOR = 2069;

	public static final int REDBEARD_FRANK = 375;
	public static final int LUTHAS = 379;
	public static final int CUSTOMS_OFFICER = 380;
	public static final int WYDIN = 557;

	public static final Position BRIMHAVEN_DOCKS = new Position(2772, 3232, 0);
	public static final Position ARDOUGNE_DOCKS = new Position(2683, 3272, 0);

	public int dialogueStage = 0;
	private int reward[][] = {};
	private int expReward[][] = {};

	private static final int questPointReward = 2;

	public int getQuestID() {
		return 20;
	}

	public String getQuestName() {
		return "Pirate's Treasure";
	}

	public String getQuestSaveName() {
		return "pirates-treasure";
	}

	public boolean canDoQuest(Player player) {
		return true;
	}

	public void getReward(Player player) {
		for (int[] rewards : reward) {
			player.getInventory().addItem(new Item(rewards[0], rewards[1]));
		}
		for (int[] expRewards : expReward) {
			player.getSkill().addExp(expRewards[0], (expRewards[1]));
		}
		player.addQuestPoints(questPointReward);
		player.getActionSender().QPEdit(player.getQuestPoints());
	}

	public void completeQuest(Player player) {
		getReward(player);
		player.getActionSender().sendInterface(12140);
		player.getActionSender().sendItemOnInterface(995, 200, 12142);
		player.getActionSender().sendString("You have completed: " + getQuestName(), 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("2 Quest Points", 12150);
		player.getActionSender().sendString("One-Eyed Hector's Treasure", 12151); //manually did the 2.25x calculation - don't touch
		player.getActionSender().sendString("Chest.", 12152);
		player.getActionSender().sendString("You can now use the Pay-fare", 12153);
		player.getActionSender().sendString("option to travel to Karamja.", 12154);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), 7341);
	}

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if (questStage == QUEST_STARTED) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Redbeard Frank in Port Sarim.", 8147);

			player.getActionSender().sendString("Redbeard Frank knows the location of a pirate treasure.", 8149);
			player.getActionSender().sendString("He will only tell me if I fetch him some Karamjan Rum.", 8150);
			player.getActionSender().sendString("I suppose I should head to Karamja.", 8151);
		} else if (questStage == RUM_INTO_CRATE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Redbeard Frank in Port Sarim.", 8147);
			player.getActionSender().sendString("@str@" + "Redbeard Frank knows the location of a pirate treasure.", 8149);
			player.getActionSender().sendString("@str@" + "He will only tell me if I fetch him some Karamjan Rum.", 8150);

			player.getActionSender().sendString("I sneakily loaded some Karamjan Rum into the banana crate.", 8152);
			player.getActionSender().sendString("I need to fill the crate with bananas and ship it.", 8153);
		} else if (questStage == RUM_SHIPPED) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Redbeard Frank in Port Sarim.", 8147);
			player.getActionSender().sendString("@str@" + "Redbeard Frank knows the location of a pirate treasure.", 8149);
			player.getActionSender().sendString("@str@" + "He will only tell me if I fetch him some Karamjan Rum.", 8150);
			player.getActionSender().sendString("@str@" + "I sneakily loaded some Karamjan Rum into the banana crate.", 8152);

			player.getActionSender().sendString("Luthas said his crates ship to Wydin's shop in Port Sarim.", 8154);
		} else if (questStage == STORE_EMPLOYEE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Redbeard Frank in Port Sarim.", 8147);
			player.getActionSender().sendString("@str@" + "Redbeard Frank knows the location of a pirate treasure.", 8149);
			player.getActionSender().sendString("@str@" + "He will only tell me if I fetch him some Karamjan Rum.", 8150);
			player.getActionSender().sendString("@str@" + "I sneakily loaded some Karamjan Rum into the banana crate.", 8152);
			player.getActionSender().sendString("@str@" + "Luthas said his crates ship to Wydin's shop in Port Sarim.", 8154);

			player.getActionSender().sendString("I'm an employee of Wydin's, I need to get that rum.", 8156);
		} else if (questStage == RUM_TO_FRANK) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Redbeard Frank in Port Sarim.", 8147);
			player.getActionSender().sendString("@str@" + "Redbeard Frank knows the location of a pirate treasure.", 8149);
			player.getActionSender().sendString("@str@" + "He will only tell me if I fetch him some Karamjan Rum.", 8150);
			player.getActionSender().sendString("@str@" + "I sneakily loaded some Karamjan Rum into the banana crate.", 8152);
			player.getActionSender().sendString("@str@" + "I got the rum from Wydin's.", 8154);

			player.getActionSender().sendString("I just need to give this rum to Redbeard Frank now.", 8156);
		} else if (questStage == BLUE_MOON_INN) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Redbeard Frank in Port Sarim.", 8147);
			player.getActionSender().sendString("@str@" + "Redbeard Frank knows the location of a pirate treasure.", 8149);
			player.getActionSender().sendString("@str@" + "He will only tell me if I fetch him some Karamjan Rum.", 8150);
			player.getActionSender().sendString("@str@" + "I sneakily loaded some Karamjan Rum into the banana crate.", 8152);
			player.getActionSender().sendString("@str@" + "I got the rum from Wydin's.", 8154);
			player.getActionSender().sendString("@str@" + "I gave the rum to Redbeard Frank", 8156);

			player.getActionSender().sendString("Frank gave me a key to his former captain's chest.", 8158);
			player.getActionSender().sendString("He said One-Eyed Hector used to stay at the Blue Moon Inn.", 8159);
		} else if (questStage == TREASURE_HUNT) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Redbeard Frank in Port Sarim.", 8147);
			player.getActionSender().sendString("@str@" + "Redbeard Frank knows the location of a pirate treasure.", 8149);
			player.getActionSender().sendString("@str@" + "He will only tell me if I fetch him some Karamjan Rum.", 8150);
			player.getActionSender().sendString("@str@" + "I sneakily loaded some Karamjan Rum into the banana crate.", 8152);
			player.getActionSender().sendString("@str@" + "I got the rum from Wydin's.", 8154);
			player.getActionSender().sendString("@str@" + "I gave the rum to Redbeard Frank", 8156);
			player.getActionSender().sendString("@str@" + "Frank gave me a key to his former captain's chest.", 8158);
			player.getActionSender().sendString("@str@" + "He said One-Eyed Hector used to stay at the Blue Moon Inn.", 8159);

			player.getActionSender().sendString("One-Eyed Hector's chest contained nothing but a message.", 8161);
			player.getActionSender().sendString("Perhaps the message can tell me where to go.", 8162);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Redbeard Frank in Port Sarim.", 8147);
			player.getActionSender().sendString("@str@" + "Redbeard Frank knows the location of a pirate treasure.", 8149);
			player.getActionSender().sendString("@str@" + "He will only tell me if I fetch him some Karamjan Rum.", 8150);
			player.getActionSender().sendString("@str@" + "I sneakily loaded some Karamjan Rum into the banana crate.", 8152);
			player.getActionSender().sendString("@str@" + "I got the rum from Wydin's.", 8154);
			player.getActionSender().sendString("@str@" + "I gave the rum to Redbeard Frank", 8156);
			player.getActionSender().sendString("@str@" + "Frank gave me a key to his former captain's chest.", 8158);
			player.getActionSender().sendString("@str@" + "He said One-Eyed Hector used to stay at the Blue Moon Inn.", 8159);
			player.getActionSender().sendString("@str@" + "I found the pirate's treasure!", 8161);

			player.getActionSender().sendString("@red@" + "You have completed this quest!", 8163);
		} else {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("Talk to Redbeard Frank in Port Sarim.", 8147);
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 7341);
	}

	public boolean questCompleted(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if (questStage >= QUEST_COMPLETE) {
			return true;
		}
		return false;
	}

	public void sendQuestTabStatus(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if ((questStage >= QUEST_STARTED) && (questStage < QUEST_COMPLETE)) {
			player.getActionSender().sendString("@yel@" + getQuestName(), 7341);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 7341);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 7341);
		}

	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString("Talk to Redbeard Frank in Port Sarim.", 8147);
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public boolean itemHandling(Player player, int itemId) {
		switch (itemId) {
			case PIRATE_MESSAGE:
				player.getDialogue().sendStatement("Visit the city of the White Knights. In the park,", "Saradomin points to the X which marks the spot.");
				return true;
		}
		return false;
	}

	public static boolean handleNpcClick(Player player, int npcId) {
		switch (npcId) {
			case CUSTOMS_OFFICER:
				if (player.getQuestStage(20) < 8) {
					player.getActionSender().sendMessage("You may only use the pay-fare option after completing Pirate's Treasure.");
					return true;
				} else if (player.getInventory().playerHasItem(KARAMJAN_RUM)) {
					player.getDialogue().setLastNpcTalk(CUSTOMS_OFFICER);
					player.getDialogue().sendNpcChat("I'm afraid the export of Karamjan Rum", "is prohibited at this date and time.", "Please do not try to violate our import laws.", ANNOYED);
					return true;
				} else if (player.getPosition().getX() > 2780) {
					Sailing.sailShip(player, Sailing.ShipRoute.KARAMJA_TO_PORT_SARIM, npcId);
					return true;
				} else {
					Sailing.sailShip(player, Sailing.ShipRoute.BRIMHAVEN_TO_ARDOUGNE, npcId);
					return true;
				}
			case 376: //port sarim sailors
			case 377:
			case 378:
				if (player.getQuestStage(20) < 8) {
					player.getActionSender().sendMessage("You may only use the pay-fare option after completing Pirate's Treasure.");
					return true;
				} else if (player.getPosition().getX() > 2800) {
					Sailing.sailShip(player, Sailing.ShipRoute.PORT_SARIM_TO_KARAMJA, npcId);
					return true;
				} else {
					Sailing.sailShip(player, Sailing.ShipRoute.ARDOUGNE_TO_BRIMHAVEN, npcId);
					return true;
				}
		}
		return false;
	}

	public static boolean itemPickupHandling(Player player, int itemId) {
		//
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		if (firstItem == CHEST_KEY && secondItem == CHEST) {
			player.getActionSender().sendMessage("You unlock One-Eyed Hector's chest... and find it full of loot!");
			player.getInventory().replaceItemWithItem(new Item(CHEST), new Item(DIAMOND_RING));
			if (player.getInventory().canAddItem(new Item(UNCUT_DRAGONSTONE))) {
				player.getInventory().addItem(new Item(UNCUT_DRAGONSTONE));
			} else {
				GroundItem drop = new GroundItem(new Item(UNCUT_DRAGONSTONE), player, new Position(player.getPosition().getX(), player.getPosition().getY()));
				GroundItemManager.getManager().dropItem(drop);
			}
			if (player.getInventory().canAddItem(new Item(995, 3570))) {
				player.getInventory().addItem(new Item(995, 3570));
			} else {
				GroundItem drop = new GroundItem(new Item(995, 3570), player, new Position(player.getPosition().getX(), player.getPosition().getY()));
				GroundItemManager.getManager().dropItem(drop);
			}
			return true;
		} else if ((firstItem == KARAMJAN_RUM && secondItem == SILK) || (firstItem == SILK && secondItem == KARAMJAN_RUM)) {
			player.getActionSender().sendMessage("You use the rum on the silk to make a sterile cleaning cloth.");
			player.getInventory().replaceItemWithItem(new Item(SILK), new Item(CLEANING_CLOTH));
			player.getInventory().removeItem(new Item(KARAMJAN_RUM));
			return true;
		} else if (firstItem == CLEANING_CLOTH) {
			for (int i = 0; i < getPoisonedWeapons().size(); i++) {
				Item item = getPoisonedWeapons().get(i);
				if (item == null) {
					continue;
				}
				if (item.getId() == secondItem) {
					if (item.getDefinition().isStackable() && player.getInventory().playerHasItem(item.getId(), 15)) {
						if (player.getInventory().canAddItem(new Item(item.getId(), 15))) {
							player.getInventory().replaceItemWithItem(new Item(item.getId(), 15), new Item(getUnpoisonedWeaponId(item), 15));
						} else {
							player.getInventory().removeItem(new Item(item.getId(), 15));
							GroundItem drop = new GroundItem(new Item(getUnpoisonedWeaponId(item), 15), player, player.getPosition().clone());
							GroundItemManager.getManager().dropItem(drop);
						}
						if (player.getStaffRights() > 1) {
							player.getActionSender().sendMessage("You clean the poison off your " + item.getDefinition().getName() + "." + " (" + item.getId() + ")");
						} else {
							player.getActionSender().sendMessage("You clean the poison off your " + item.getDefinition().getName() + ".");
						}
						return true;
					} else if (item.getDefinition().isStackable() && !player.getInventory().playerHasItem(item.getId(), 15)) {
						int amount = player.getInventory().getItemAmount(item.getId());
						if (player.getInventory().canAddItem(new Item(item.getId(), amount))) {
							player.getInventory().replaceItemWithItem(new Item(item.getId(), amount), new Item(getUnpoisonedWeaponId(item), amount));
						} else {
							player.getInventory().removeItem(new Item(item.getId(), amount));
							GroundItem drop = new GroundItem(new Item(getUnpoisonedWeaponId(item), amount), player, player.getPosition().clone());
							GroundItemManager.getManager().dropItem(drop);
						}
						if (player.getStaffRights() > 1) {
							player.getActionSender().sendMessage("You clean the poison off your " + item.getDefinition().getName() + "." + " (" + item.getId() + ")");
						} else {
							player.getActionSender().sendMessage("You clean the poison off your " + item.getDefinition().getName() + ".");
						}
						return true;
					} else {
						player.getInventory().replaceItemWithItem(item, new Item(getUnpoisonedWeaponId(item)));
						if (player.getStaffRights() > 1) {
							player.getActionSender().sendMessage("You clean the poison off your " + item.getDefinition().getName() + "." + " (" + item.getId() + ")");
						} else {
							player.getActionSender().sendMessage("You clean the poison off your " + item.getDefinition().getName() + ".");
						}
						return true;
					}
				}
			}
		}
		return false;
	}

	public static void getAndDestroyRum(final Player player) {
		if (player.getBankManager().ownsItem(KARAMJAN_RUM)) {
			player.getBankManager().remove(new Item(KARAMJAN_RUM, Constants.MAX_ITEM_COUNT));
			player.getActionSender().sendMessage("Your karamjan rum breaks because you haven't started Pirate's Treasure!");
		}
		for (Item item : player.getInventory().getItemContainer().getItems()) {
			if (item == null) {
				continue;
			}
			if (item.getId() == KARAMJAN_RUM) {
				player.getInventory().removeItem(item);
				player.getActionSender().sendMessage("Your karamjan rum breaks because you haven't started Pirate's Treasure!");
			}
		}
	}

	public static int getUnpoisonedWeaponId(Item original) {
		switch (original.getId()) {
			case 5688: //br'ze dagger(p++)
				return 1205;
			case 5698: //drag dagger(p++)
				return 1215;
			case 5648: //bronze jav'n(p++)
				return 825;
		}
		String originalName = original.getDefinition().getName().toLowerCase(); //iron dagger(p)
		for (int i = 0; i < Constants.MAX_ITEMS; i++) {
			if (GlobalVariables.itemDump[i].contains("(") || GlobalVariables.itemDump[i].contains("tips")) {
				continue;
			}
			String matching = GlobalVariables.itemDump[i].substring(GlobalVariables.itemDump[i].indexOf(":") + 2);// iron dagger desc
			if (matching.toLowerCase().contains(originalName.substring(0, originalName.indexOf("(")))) {
				return getItemIdFromDump(i);
			}
		}
		return 0;
	}

	public static int getItemIdFromDump(int i) {
		if (GlobalVariables.itemDump[i].substring(0, GlobalVariables.itemDump[i].indexOf(":")).contains(Integer.toString(i))) {
			return i;
		}
		return 0;
	}

	public static ArrayList<Item> getPoisonedWeapons() {
		ArrayList<Item> poisoned = new ArrayList<Item>();
		int count = 0;
		for (int i = 0; i < 11789; i++) {
			if (GlobalVariables.itemDump[i].contains("(p)") || GlobalVariables.itemDump[i].contains("(p+)")
				|| GlobalVariables.itemDump[i].contains("(p++)") || GlobalVariables.itemDump[i].contains("(kp)")) {
				Item item = new Item(getItemIdFromDump(i));
				String name = item.getDefinition().getName().toLowerCase();
				if (!item.getDefinition().isNoted() && item.getId() != 7001 && !name.contains("hasta") && !name.contains("poison")) { //remove the hasta check if they are ever added
					poisoned.add(count, item);
					count++;
				}
			}
		}
		return poisoned;
	}

	public boolean doItemOnObject(Player player, int object, int item) {
		switch (object) {
			case BANANA_CRATE:
				if (!player.onApeAtoll()) {
					if (item == BANANA && player.getQuestVars().getBananaCrate() && player.getQuestVars().getBananaCrateCount() < 9) {
						player.getActionSender().sendMessage("You load the banana into the crate.");
						player.getInventory().removeItem(new Item(BANANA));
						player.getUpdateFlags().sendAnimation(832);
						player.getQuestVars().setBananaCrateCount(player.getQuestVars().getBananaCrateCount() + 1);
						return true;
					} else if (item == BANANA && player.getQuestVars().getBananaCrate() && player.getQuestVars().getBananaCrateCount() == 9) {
						player.getActionSender().sendMessage("You load the last banana, the crate is full. Better go tell Luthas.");
						player.getInventory().removeItem(new Item(BANANA));
						player.getUpdateFlags().sendAnimation(832);
						player.getQuestVars().setBananaCrateCount(10);
						return true;
					} else if (item == BANANA && player.getQuestVars().getBananaCrate() && player.getQuestVars().getBananaCrateCount() == 10) {
						player.getActionSender().sendMessage("The crate is full. Go tell Luthas.");
						return true;
					} else if (item == KARAMJAN_RUM && player.getQuestVars().getBananaCrate() && player.getQuestStage(20) == 1) {
						player.getDialogue().sendGiveItemNpc("You stash the rum in the crate.", new Item(KARAMJAN_RUM));
						player.getInventory().removeItem(new Item(KARAMJAN_RUM));
						player.getUpdateFlags().sendAnimation(832);
						player.setQuestStage(20, 2);
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			case HECTOR_CHEST:
				if (item == CHEST_KEY && player.getQuestStage(20) == 6 && !player.getInventory().playerHasItem(PIRATE_MESSAGE)) {
					player.getDialogue().sendGiveItemNpc("You find nothing but a message.", new Item(PIRATE_MESSAGE));
					player.getInventory().addItem(new Item(PIRATE_MESSAGE));
					if (player.getQuestStage(20) == 6) {
						player.setQuestStage(20, 7);
					}
					return true;
				}
		}
		return false;
	}

	public boolean doObjectClicking(final Player player, int object, int x, int y) {
		switch (object) {
			case BANANA_TREE:
				player.getActionSender().sendMessage("You search the banana tree...");
				player.getUpdateFlags().sendAnimation(832);
				player.setStopPacket(true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						player.getActionSender().sendMessage("You find a banana, how strange.");
						player.getInventory().addItem(new Item(BANANA));
						b.stop();
					}

					@Override
					public void stop() {
						player.setStopPacket(false);
					}
				}, 3);
				return true;
			case BANANA_CRATE:
				if (!player.onApeAtoll()) {
					if (player.getQuestVars().getBananaCrate()) {
						if (player.getQuestVars().getBananaCrateCount() == 1) {
							player.getActionSender().sendMessage("You have loaded a single banana.");
							return true;
						} else {
							player.getActionSender().sendMessage("You have loaded " + player.getQuestVars().getBananaCrateCount() + " bananas.");
							return true;
						}
					} else {
						player.getDialogue().sendPlayerChat("I probably shouldn't rummage through Luthas' crate.", CONTENT);
						return true;
					}
				} else {
					return false;
				}
			case STORE_DOOR:
				switch (player.getQuestStage(20)) {
					case 0:
					case 1:
					case 2:
					case 3:
						if (x == 3012 && y == 3204) {
							player.getDialogue().sendNpcChat("Hey, you can't go in there. Only employees of the", "grocery store can enter.", ANNOYED);
							return true;
						} else {
							return false;
						}
					case 4:
					case 5:
					case 6:
					case 7:
					case 8:
						if (player.getEquipment().getId(Constants.CHEST) == WHITE_APRON && x == 3012 && y == 3204 && player.getPosition().getX() > 3011) {
							player.getActionSender().walkTo(-1, 0, true);
							player.getActionSender().walkThroughDoor(object, x, y, 0);
							return true;
						} else if (player.getEquipment().getId(Constants.CHEST) != WHITE_APRON && x == 3012 && y == 3204 && player.getPosition().getX() > 3011) {
							player.getDialogue().sendNpcChat("Hey, you need to be in proper employee attire to enter!", "White apron, remember?", ANNOYED);
							return true;
						} else if (x == 3012 && y == 3204 && player.getPosition().getX() < 3012) {
							player.getActionSender().walkTo(1, 0, true);
							player.getActionSender().walkThroughDoor(object, x, y, 0);
							return true;
						} else {
							return false;
						}
				}
				return false;
			case STORE_CRATE:
				switch (player.getQuestStage(20)) {
					case 5:
						if (!player.getInventory().ownsItem(KARAMJAN_RUM)) {
							if (player.getInventory().canAddItem(new Item(KARAMJAN_RUM))) {
								player.getDialogue().sendGiveItemNpc("You find the rum in the crate.", new Item(KARAMJAN_RUM));
								player.getInventory().addItem(new Item(KARAMJAN_RUM));
								return true;
							} else {
								player.getActionSender().sendMessage("Your inventory is full.");
								return true;
							}
						}
						return false;
					case 4:
						if (player.getInventory().canAddItem(new Item(KARAMJAN_RUM))) {
							player.getDialogue().sendGiveItemNpc("You find the rum in the crate.", new Item(KARAMJAN_RUM));
							player.getInventory().addItem(new Item(KARAMJAN_RUM));
							player.setQuestStage(20, 5);
						} else {
							player.getActionSender().sendMessage("Your inventory is full.");
						}
						return true;

				}
				return false;
		}
		return false;
	}

	public boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
		switch (object) {

		}
		return false;
	}

	public void handleDeath(final Player player, final Npc died) {

	}

	public boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
		switch (id) {
			case REDBEARD_FRANK:
				switch (player.getQuestStage(20)) {
					case 0: //start
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Arr, Matey!", LAUGHING);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("I was wondering if you have a quest for me.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Ho ho! A mighty quest! I'm not much the questin' type", "lad. But I do need your help.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("Sounds like a quest to me.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("It's not!", ANGRY_1);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("It's just a favor from one matey to another.", "I give you somethin' in exchange for somethin' else...", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("And that something is?", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("A bottle of delicious Karamjan Rum of course!", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendPlayerChat("I heard people have died drinking that, and death", "sounds dangerous to me. What do I get in return?", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("Humph, I s'pose I can tell you the location of a pirate", "treasure in return for some of tha' rum.", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendPlayerChat("Sounds like a deal to me, I'll get working on it.", CONTENT);
								return true;
							case 12:
								player.getDialogue().sendNpcChat("Best of luck matey!", HAPPY);
								player.getDialogue().endDialogue();
								player.setQuestStage(20, 1);
								QuestHandler.getQuests()[20].startQuest(player);
								return true;
						}
						return false;
					case 1: //rum yet?
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Arr, Matey!", LAUGHING);
								return true;
							case 2:
								if (player.getQuestStage(20) == 6) {
									player.getDialogue().sendNpcChat("Thanks again for that rum matey!", HAPPY);
									player.getDialogue().endDialogue();
									return true;
								} else {
									player.getDialogue().sendNpcChat("Have ye brought some rum for ye ol' mate Frank?", CONTENT);
									return true;
								}
							case 3:
								if (player.getInventory().playerHasItem(KARAMJAN_RUM)) {
									player.getDialogue().sendPlayerChat("Yes, I have some right here.", CONTENT);
									player.getDialogue().setNextChatId(8);
									return true;
								} else {
									player.getDialogue().sendPlayerChat("No, not yet.", CONTENT);
									return true;
								}
							case 4:
								player.getDialogue().sendNpcChat("Not surprising, 'tis no easy task to get it off Karamja.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("What do you mean?", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("The Customs office has been clampin' down on the", "export of spirits. You seem like a resourceful young lad,", "I'm sure ye'll be able to find a way to slip the stuff past", "them.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 8:
								player.getDialogue().sendNpcChat("Now a deal's a deal, I'll tell ye about the treasure. I", "used to serve under a pirate captain called One-Eyed", "Hector.", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("Hector were very successful and became very rich.", "But about a year ago we were boarded by the Customs", "and Excise Agents.", SAD);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("Hector were killed along with many of the crew, I were", "one of the few to escape and I escaped with this.", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendGiveItemNpc("Frank happily takes the rum, and hands you a key.", new Item(CHEST_KEY));
								player.getInventory().replaceItemWithItem(new Item(KARAMJAN_RUM), new Item(CHEST_KEY));
								player.setQuestStage(20, 6);
								return true;
							case 12:
								player.getDialogue().sendNpcChat("This be Hector's key. I believe it opens his chest in his", "old room in the Blue Moon Inn in Varrock.", CONTENT);
								return true;
							case 13:
								player.getDialogue().sendNpcChat("With any luck his treasure will be in there.", CONTENT);
								return true;
							case 14:
								player.getDialogue().sendPlayerChat("Ok thanks, I'll go and get it.", HAPPY);
								player.getDialogue().endDialogue();
								return true;

						}
						return false;
				}
				return false;
			case LUTHAS:
				switch (player.getQuestVars().getBananaCrateCount()) {
					case 0: //tfw no bananas
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
					case 8:
					case 9:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (!player.getQuestVars().getBananaCrate()) {
									player.getDialogue().sendNpcChat("Hello, I'm Luthas - I run the plantation here.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Did you get those bananas loaded yet?", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								}
							case 2:
								player.getDialogue().sendPlayerChat("Could you offer me employment on your plantation?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Yes, I can sort something out. There's a crate ready to", "be loaded onto the ship.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("You wouldn't believe the demand for bananas from", "Wydin's shop over in Port Sarim. I think this is the", "third crate I've shipped this month...", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("If you could fill the crate with bananas, I'll pay you", "30 gold.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("Thank you!", HAPPY);
								player.getDialogue().endDialogue();
								player.getQuestVars().setBananaCrate(true);
								return true;
						}
						return false;
					case 10: //loaded them bananas man
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("I loaded the bananas Luthas.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Excellent! I'll get that crate shipped.", "Here's your payment as promised.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendGiveItemNpc("Luthas hands you 30 gold.", new Item(995));
								player.getDialogue().endDialogue();
								player.getInventory().addItem(new Item(995, 30));
								player.getQuestVars().setBananaCrate(false);
								player.getQuestVars().setBananaCrateCount(0);
								if (player.getQuestStage(20) == 2) {
									player.setQuestStage(20, 3);
								}
								return true;
						}
						return false;
				}
				return false;
			case WYDIN:
				switch (player.getQuestStage(20)) {
					case 3: //need to get in that back room mang
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Can I get a job here?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Hmm, you're eager, I like that. Sure.", "I'll give you a go. Have you got your own white", "apron?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendOption("Of course.", "I'm sure I can find one.");
								return true;
							case 4:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("Of course.", CONTENT);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("Im sure I can find one.", CONTENT);
										return true;
								}
							case 5:
								player.getDialogue().sendNpcChat("That's the spirit! You're hired!", "Go through the back door and tidy up for me.", HAPPY);
								player.getDialogue().endDialogue();
								if (player.getQuestStage(20) == 3) {
									player.setQuestStage(20, 4);
								}
								return true;
						}
						return false;
				}
				return false;
		}
		return false;
	}

	@Override
	public boolean doNpcClicking(Player player, Npc npc) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doItemOnNpc(Player player, int itemId, Npc npc) {
		// TODO Auto-generated method stub
		return false;
	}

}
