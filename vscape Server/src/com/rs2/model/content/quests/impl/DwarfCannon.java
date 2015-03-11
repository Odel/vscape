package com.rs2.model.content.quests;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.Shops;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.dialogue.DialogueManager;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.ANNOYED;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.content.skills.Menus;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.item.ItemManager;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class DwarfCannon implements Quest {

	//Quest stages

	public static final int QUEST_STARTED = 1;
	public static final int RAILINGS_FIXED = 2;
	public static final int FIND_GILUB = 3;
	public static final int FOUND_GILUB = 4;
	public static final int FIND_HIDEOUT = 5;
	public static final int FOUND_LOLLK = 6;
	public static final int FIX_CANNON = 7;
	public static final int CANNON_FIXED = 8;
	public static final int FIND_NULODION = 9;
	public static final int RETURN_TO_CAPTAIN = 10;
	public static final int QUEST_COMPLETE = 11;
	//Items
	public static final int DWARF_REMAINS = 0;
	public static final int TOOLKIT = 1;
	public static final int NULODIONS_NOTES = 3;
	public static final int AMMO_MOULD = 4;
	public static final int INSTRUCTION_MANUAL = 5;
	public static final int RAILING = 14;
	//Positions
	public static final Position UNDER_GRAND_TREE = new Position(2464, 9897, 0);
	//Interfaces
	public static final int SCROLL_INTERFACE = 6965;
	//Npcs
	public static final int CAPTAIN_LAWGOF = 208;
	public static final int LOLLK = 207;
	public static final Npc lollk = new Npc(LOLLK);
	public static final int NULODION = 209;
	//Objects
	public static final int CRATE = 1;
	public static final int GOBLIN_CAVE = 2;
	public static final int BROKEN_CANNON = 5;
	public static final int MUD_PILE = 13;
	public static final int NORMAL_RAILING = 14;
	public static final int BROKEN_RAILING_1 = 15;
	public static final int BROKEN_RAILING_2 = 16;
	public static final int BROKEN_RAILING_3 = 17;
	public static final int BROKEN_RAILING_4 = 18;
	public static final int BROKEN_RAILING_5 = 19;
	public static final int BROKEN_RAILING_6 = 20;

	public static final int PLACE_ANIM = 832;

	public static final int MAKE_1 = 10239;
	public static final int MAKE_5 = 10238;
	public static final int MAKE_X = 6212;
	public static final int MAKE_ALL = 6211;
	public int dialogueStage = 0; //Ignore

	private int reward[][] = { //Items in the form of {Id, #},
	};
	private int expReward[][] = { //Exp in the form of {Skill.AGILITY, x},
		{Skill.CRAFTING, 750},}; //The 2.25 multiplier is added later, use vanilla values

	private static final int questPointReward = 1;

	public int getQuestID() {
		return 30;
	}

	public String getQuestName() {
		return "Dwarf Cannon";
	}

	public String getQuestSaveName() {
		return "dwarfcannon";
	}

	public boolean canDoQuest(Player player) { //Use to check for strict auxiliary quest requirements
		return true;
	}

	public void getReward(Player player) {
		for (int[] rewards : reward) {
			player.getInventory().addItemOrDrop(new Item(rewards[0], rewards[1]));
		}
		for (int[] expRewards : expReward) {
			player.getSkill().addExp(expRewards[0], (expRewards[1]));
		}
		player.addQuestPoints(questPointReward);
		player.getActionSender().QPEdit(player.getQuestPoints());
	}

	//End of quest reward scroll interface, tweak what's necessary
	public void completeQuest(Player player) {
		//If writing in exp, be sure to express it manually as 2.25 the vanilla reward
		getReward(player);
		player.getActionSender().sendInterface(12140);
		player.getActionSender().sendItemOnInterface(995, 200, 12142);
		player.getActionSender().sendString("You have completed " + getQuestName() + "!", 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("1687.5 Crafting XP", 12151);
		player.getActionSender().sendString("Permission to purchase and", 12152);
		player.getActionSender().sendString("use the Dwarf Multicannon", 12153);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		//Updates the players quest list to show the green complete quest
		player.getActionSender().sendString("@gre@" + getQuestName(), 7356);
	}
    //Here we send the quest log, with the text and then the line number in sendString(string, line number)
	//The line number is according to the interface, just add to it for the next line

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		switch (questStage) {
			case QUEST_STARTED:
				player.getActionSender().sendString("@str@" + "Talk to the Dwarf Commander, he needs your help.", 8147);

				player.getActionSender().sendString("Captain Lawgof made me a member of the Black Guard.", 8149);
				player.getActionSender().sendString("He wants me to inspect and fix 6 broken railings.", 8150);
				break;
			case RAILINGS_FIXED:
				player.getActionSender().sendString("@str@" + "Talk to the Dwarf Commander, he needs your help.", 8147);
				player.getActionSender().sendString("@str@" + "Captain Lawgof made me a member of the Black Guard.", 8149);

				player.getActionSender().sendString("I fixed the Captain's railings, I should tell him.", 8151);
				break;
			case FIND_GILUB:
				player.getActionSender().sendString("@str@" + "Talk to the Dwarf Commander, he needs your help.", 8147);
				player.getActionSender().sendString("@str@" + "Captain Lawgof made me a member of the Black Guard.", 8149);
				player.getActionSender().sendString("@str@" + "I fixed the Captain's railings, all six of them.", 8151);

				player.getActionSender().sendString("The Captain has given me another task. He needs me", 8153);
				player.getActionSender().sendString("to find out what happened to his guard at the", 8154);
				player.getActionSender().sendString("watchtower south of here. He told me to find Gilub.", 8155);
				break;
			case FOUND_GILUB:
				player.getActionSender().sendString("@str@" + "Talk to the Dwarf Commander, he needs your help.", 8147);
				player.getActionSender().sendString("@str@" + "Captain Lawgof made me a member of the Black Guard.", 8149);
				player.getActionSender().sendString("@str@" + "I fixed the Captain's railings, all six of them.", 8151);

				player.getActionSender().sendString("Well, I find Gilub, atleast what was left of him.", 8153);
				player.getActionSender().sendString("I should report back to the captain.", 8154);
				break;
			case FIND_HIDEOUT:
				player.getActionSender().sendString("@str@" + "Talk to the Dwarf Commander, he needs your help.", 8147);
				player.getActionSender().sendString("@str@" + "Captain Lawgof made me a member of the Black Guard.", 8149);
				player.getActionSender().sendString("@str@" + "I fixed the Captain's railings, all six of them.", 8151);
				player.getActionSender().sendString("@str@" + "I found the remains of the Commander Gilub.", 8153);

				player.getActionSender().sendString("The Captain was sad to hear of Gilub. He told me", 8155);
				player.getActionSender().sendString("Gilub has a son, Lollk and that I should look for", 8156);
				player.getActionSender().sendString("him in the goblins' hideout, southeast of here.", 8157);
				break;
			case FOUND_LOLLK:
				player.getActionSender().sendString("@str@" + "Talk to the Dwarf Commander, he needs your help.", 8147);
				player.getActionSender().sendString("@str@" + "Captain Lawgof made me a member of the Black Guard.", 8149);
				player.getActionSender().sendString("@str@" + "I fixed the Captain's railings, all six of them.", 8151);
				player.getActionSender().sendString("@str@" + "I found the remains of the Commander Gilub.", 8153);

				player.getActionSender().sendString("I found the Goblin hideout and Lollk. I sent Lollk", 8155);
				player.getActionSender().sendString("on his way back home. I should return to the Captain.", 8156);
				break;
			case FIX_CANNON:
				player.getActionSender().sendString("@str@" + "Talk to the Dwarf Commander, he needs your help.", 8147);
				player.getActionSender().sendString("@str@" + "Captain Lawgof made me a member of the Black Guard.", 8149);
				player.getActionSender().sendString("@str@" + "I fixed the Captain's railings, all six of them.", 8151);
				player.getActionSender().sendString("@str@" + "I found the remains of the Commander Gilub.", 8153);
				player.getActionSender().sendString("@str@" + "I found the Goblin hideout and Lollk.", 8155);

				player.getActionSender().sendString("The Captain wishes one more favor of me. He wants me", 8157);
				player.getActionSender().sendString("to repair the Black Guard's Multicannon.", 8158);
				break;
			case CANNON_FIXED:
				player.getActionSender().sendString("@str@" + "Talk to the Dwarf Commander, he needs your help.", 8147);
				player.getActionSender().sendString("@str@" + "Captain Lawgof made me a member of the Black Guard.", 8149);
				player.getActionSender().sendString("@str@" + "I fixed the Captain's railings, all six of them.", 8151);
				player.getActionSender().sendString("@str@" + "I found the remains of the Commander Gilub.", 8153);
				player.getActionSender().sendString("@str@" + "I found the Goblin hideout and Lollk.", 8155);

				player.getActionSender().sendString("I repaired the Dwarf Multicannon, as the Captain", 8157);
				player.getActionSender().sendString("asked. I should tell him.", 8158);
				break;
			case FIND_NULODION:
				player.getActionSender().sendString("@str@" + "Talk to the Dwarf Commander, he needs your help.", 8147);
				player.getActionSender().sendString("@str@" + "Captain Lawgof made me a member of the Black Guard.", 8149);
				player.getActionSender().sendString("@str@" + "I fixed the Captain's railings, all six of them.", 8151);
				player.getActionSender().sendString("@str@" + "I found the remains of the Commander Gilub.", 8153);
				player.getActionSender().sendString("@str@" + "I found the Goblin hideout and Lollk.", 8155);
				player.getActionSender().sendString("@str@" + "I repaired the Dwarf Multicannon, as requested.", 8157);

				player.getActionSender().sendString("Turns out the Captain is clueless to what the cannon", 8159);
				player.getActionSender().sendString("shoots. My assumption is cannonballs, but he requested", 8160);
				player.getActionSender().sendString("I go see Nulodion, south of Ice Mountain, anyways.", 8161);
				break;
			case RETURN_TO_CAPTAIN:
				player.getActionSender().sendString("@str@" + "Talk to the Dwarf Commander, he needs your help.", 8147);
				player.getActionSender().sendString("@str@" + "Captain Lawgof made me a member of the Black Guard.", 8149);
				player.getActionSender().sendString("@str@" + "I fixed the Captain's railings, all six of them.", 8151);
				player.getActionSender().sendString("@str@" + "I found the remains of the Commander Gilub.", 8153);
				player.getActionSender().sendString("@str@" + "I found the Goblin hideout and Lollk.", 8155);
				player.getActionSender().sendString("@str@" + "I repaired the Dwarf Multicannon, as requested.", 8157);

				player.getActionSender().sendString("I found Nulodion and he gave me an instruction", 8159);
				player.getActionSender().sendString("manual and an ammo mould for the cannon. I need to", 8160);
				player.getActionSender().sendString("give these to the Captain.", 8161);
				break;
			case QUEST_COMPLETE:
				player.getActionSender().sendString("@str@" + "Talk to the Dwarf Commander, he needs your help.", 8147);
				player.getActionSender().sendString("@str@" + "Captain Lawgof made me a member of the Black Guard.", 8149);
				player.getActionSender().sendString("@str@" + "I fixed the Captain's railings, all six of them.", 8151);
				player.getActionSender().sendString("@str@" + "I found the remains of the Commander Gilub.", 8153);
				player.getActionSender().sendString("@str@" + "I found the Goblin hideout and Lollk.", 8155);
				player.getActionSender().sendString("@str@" + "I repaired the Dwarf Multicannon, as requested.", 8157);
				player.getActionSender().sendString("@str@" + "I found Nulodion and he gave me an instruction", 8159);
				player.getActionSender().sendString("@str@" + "manual and an ammo mould for the cannon. I then", 8160);
				player.getActionSender().sendString("@str@" + "gave them to the Captain.", 8161);
				player.getActionSender().sendString("@str@" + "I can now purchase a Dwarf Multicannon!", 8162);

				player.getActionSender().sendString("@red@" + "You have completed this quest!", 8165);
				break;
			default:
				player.getActionSender().sendString("Talk to the @dre@Dwarf Commander @bla@south of the  @dre@Coal Trucks @bla@ to begin.", 8147);
				break;
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 7356);
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
			player.getActionSender().sendString("@yel@" + getQuestName(), 7356); //These numbers correspond to the index of the quest in
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 7356); //the quest list on the quest tab. I've listed them all
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 7356); //in the player class, just search and then replace
			//also add the name while you're there @red@Quest Name
		}
	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
		player.getActionSender().sendString(getQuestName(), 8144);
	}

	public static boolean itemPickupHandling(final Player player, final int id) {
		if (id == 0) {
			if (player.getQuestStage(30) == FIND_GILUB || player.getQuestStage(30) == FOUND_GILUB && !player.getInventory().ownsItem(0)) {
				ItemManager.getInstance().pickupItem(player, player.getClickId(), new Position(player.getClickX(), player.getClickY(), player.getPosition().getZ()));
				player.getDialogue().sendPlayerChat("Hmmmm, this must be what remains of Gilub.", "I should bring these back to the Captain.", SAD);
				player.getDialogue().endDialogue();
				player.setQuestStage(30, FOUND_GILUB);
				return true;
			} else {
				player.getActionSender().sendMessage("You have no need for these.");
				return true;
			}
		}
		return false;
	}

	public static void repairRailing(final Player player, final int railing) {
		player.getActionSender().removeInterfaces();
		if (!player.getInventory().playerHasItem(new Item(RAILING)) || !player.getInventory().playerHasItem(2347)) {
			player.getActionSender().sendMessage("You need a new railing and a hammer to fix this!");
			return;
		}
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				player.getActionSender().sendMessage("You begin to mend the fence...");
				player.getUpdateFlags().sendAnimation(898);
				player.getActionSender().sendSound(468, 0, 0);
				b.stop();
			}

			@Override
			public void stop() {
				if (Misc.random(3) == 1) {
					player.getUpdateFlags().setForceChatMessage("Oooouch!");
					player.getActionSender().sendMessage("You cut yourself on the rusty old railing.");
					player.hit(1, HitType.NORMAL);
					player.setStopPacket(false);
					return;
				} else {
					player.getActionSender().sendMessage("This railing is now fixed.");
					player.getInventory().removeItem(new Item(RAILING, 1));
					player.setStopPacket(false);
					player.getQuestVars().setRailingsFixed(player.getQuestVars().getRailingsFixed() + 1);
					player.getQuestVars().addRailingsFixed(railing);
					if (player.getQuestVars().getRailingsFixed() == 6) {
						player.setQuestStage(30, RAILINGS_FIXED);
					}
					return;
				}
			}
		}, 3);
	}

	public void handleDeath(final Player player, final Npc npc) {
		
	}

	public boolean itemHandling(final Player player, int itemId) {
		switch (itemId) {

		}
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		return false;
	}

	public boolean doItemOnObject(final Player player, int object, int item) {
		switch (object) {
			case 14921:
			case 9390:
			case 2781:
			case 2785:
			case 2966:
			case 3044:
			case 3294:
			case 4304:
			case 4305:
			case 6189:
			case 6190:
			case 11009:
			case 11010:
			case 11666:
			case 12100:
			case 12809: //furnaces
				if (item == 2353 && player.getInventory().playerHasItem(AMMO_MOULD)) {
					if (QuestHandler.questCompleted(player, 30)) {
						player.setStatedInterface("cannonball");
						player.setEnterXInterfaceId(7777);
						Menus.display1Item(player, 2, "");
						return true;
					} else {
						player.getDialogue().sendStatement("You must complete Dwarf Cannon to create cannonballs.");
						return true;
					}
				}
		}
		return false;
	}

	public static void craftCannonBall(final Player player, final int amount) {
		if (player.getStatedInterface().equals("cannonball")) {
			if (player.getSkill().getLevel()[Skill.SMITHING] >= 35) {
				if (player.getInventory().playerHasItem(2353) && player.getInventory().playerHasItem(AMMO_MOULD)) {
					player.setStopPacket(true);
					player.getActionSender().removeInterfaces();
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						int count = 0;

						@Override
						public void execute(CycleEventContainer b) {
							if (!player.getInventory().playerHasItem(2353) || !player.getInventory().playerHasItem(AMMO_MOULD) || count == amount) {
								b.stop();
							} else {
								count++;
								player.getInventory().replaceItemWithItem(new Item(2353), new Item(2, 4));
								player.getUpdateFlags().sendAnimation(899);
								player.getActionSender().sendMessage("You craft some cannonballs.");
								player.getSkill().addExp(Skill.SMITHING, 25.6);
								player.getActionSender().sendSound(469, 0, 0);
							}
						}

						@Override
						public void stop() {
							player.setStopPacket(false);
							player.resetAllActions();
						}
					}, 5);
					return;
				} else {
					player.getActionSender().removeInterfaces();
					player.getActionSender().sendMessage("You do not have the materials required.");
					return;
				}
			} else {
				player.getDialogue().sendStatement("You need a Smithing level of 35 to make these.");
				return;
			}
		} else {
			return;
		}
	}

	public static boolean buttonHandling(final Player player, final int buttonId) {
		switch (buttonId) {
			case MAKE_1:
				if (player.getStatedInterface().equals("cannonball")) {
					craftCannonBall(player, 1);
					return true;
				}
			case MAKE_5:
				if (player.getStatedInterface().equals("cannonball")) {
					craftCannonBall(player, 5);
					return true;
				}
			case MAKE_ALL:
				if (player.getStatedInterface().equals("cannonball")) {
					craftCannonBall(player, 28);
					return true;
				}
		}
		return false;
	}

	public boolean doObjectClicking(final Player player, int object, int x, int y) {
		switch (object) {
			case BROKEN_CANNON:
				if (player.getQuestStage(30) == FIX_CANNON) {
					if (player.getInventory().playerHasItem(TOOLKIT)) {
						Dialogues.startDialogue(player, 134586);
						return true;
					} else {
						player.getDialogue().sendStatement("You need a toolkit to begin work on this.");
						player.getDialogue().endDialogue();
						return true;
					}
				} else {
					player.getDialogue().sendPlayerChat("I better not touch this without permission.", CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			case MUD_PILE:
				player.getActionSender().sendMessage("You crawl out of the cave...");
				player.fadeTeleport(new Position(2624, 3391, 0));
				return true;
			case CRATE:
				if (player.getQuestStage(30) == FIND_HIDEOUT) {
					player.getActionSender().sendMessage("You search the crate...");
					player.setStopPacket(true);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							NpcLoader.spawnPlayerOwnedSpecificLocationNpc(player, new Npc(LOLLK), new Position(2571, 9851, 0), false, "Hooray!");
							player.getActionSender().sendMessage("Inside you see a dwarf child, you untie the child.");
							player.setStopPacket(false);
						}
					}, 2);
					return true;
				} else {
					return false;
				}
			case GOBLIN_CAVE:
				player.getActionSender().sendMessage("You crawl into the cave...");
				player.fadeTeleport(new Position(2619, 9797, 0));
				return true;
			case NORMAL_RAILING:
				player.getActionSender().sendMessage("This railing is un-damaged.");
				return true;
			case BROKEN_RAILING_1:
			case BROKEN_RAILING_2:
			case BROKEN_RAILING_3:
			case BROKEN_RAILING_4:
			case BROKEN_RAILING_5:
			case BROKEN_RAILING_6:
				if (player.getQuestStage(30) == 1) {
					if (!player.getQuestVars().getRailingsArray().contains(object)) {
						player.getDialogue().sendStatement("This railing is broken and needs to be replaced.");
						player.getDialogue().endDialogue();
						repairRailing(player, object);
						return true;
					} else {
						player.getActionSender().sendMessage("This railing has been fixed.");
						return true;
					}
				}
		}
		return false;
	}

	public boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
		switch (object) {

		}
		return false;
	}

	public boolean sendDialogue(final Player player, int id, int chatId, int optionId, int npcChatId) {
		DialogueManager d = player.getDialogue();
		switch (id) { //Npc ID
			case NULODION:
				switch (player.getQuestStage(getQuestID())) { //Dialogue per stage
					case QUEST_COMPLETE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Hello again, would you like to purchase anything", "multicannon related?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendOption("Yes please.", "No thanks.");
								return true;
							case 3:
								switch (optionId) {
									case 1:
										if (player.getInteractingEntity() != null && player.getInteractingEntity().isNpc()) {
											Shops.openShop(player, NULODION);
										}
										player.getDialogue().dontCloseInterface();
										return true;
									case 2:
										d.sendPlayerChat("No thanks.", CONTENT);
										d.endDialogue();
										return true;
								}
								return false;
						}
						return false;
					default:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello there.", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Move along human. I'm very busy.", CONTENT);
								d.endDialogue();
								return true;
						}
						return false;
					case FIND_NULODION:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello there.", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Can I help you?", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("Captain Lawgof sent me. He's having trouble with", "his cannon.", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("Of course, we forgot to send the ammo mould!", LAUGHING);
								return true;
							case 5:
								d.sendPlayerChat("It fires a mould? Why?", "Shouldn't it fire cannonballs?", CONTENT);
								return true;
							case 6:
								d.sendNpcChat("Don't be silly - the ammo's made by using a mould.", "Here, take these to him. The instructions explain", "everything.", CONTENT);
								return true;
							case 7:
								d.sendPlayerChat("That's great, thanks.", CONTENT);
								return true;
							case 8:
								if (player.getInventory().getItemContainer().emptySlots() >= 2) {
									d.sendNpcChat("Thank you adventurer. The Black Guard will remember", "this.", HAPPY);
									d.endDialogue();
									player.getInventory().addItem(new Item(INSTRUCTION_MANUAL));
									player.getInventory().addItem(new Item(AMMO_MOULD));
									player.setQuestStage(30, RETURN_TO_CAPTAIN);
									return true;
								} else {
									d.sendNpcChat("Oh, you don't have 2 free slots. Return when", "you do.", CONTENT);
									d.endDialogue();
									return true;
								}
						}
						return false;
				}
			case 134586: //repair dialogue
				switch (d.getChatId()) {
					case 1:
						d.sendStatement("The safety switch seems to be stuck in place.", "Which tool would you like to use on it?");
						return true;
					case 2:
						d.sendOption("The hook tool.", "The gear wrench.", "The pliers.");
						return true;
					case 3:
						switch (optionId) {
							case 1:
							case 2:
								d.sendPlayerChat("Well, that wasn't quite the right tool...", CONTENT);
								d.endDialogue();
								return true;
							case 3:
								d.sendStatement("You move the safety switch into it's proper position.", "The pliers worked well.");
								return true;
						}
					case 4:
						d.sendStatement("There's a spring here that is disconnected.", "Which tool would you like to use to reconnect it?");
						return true;
					case 5:
						d.sendOption("The hook tool.", "The gear wrench.", "The pliers.");
						return true;
					case 6:
						switch (optionId) {
							case 2:
							case 3:
								d.sendPlayerChat("Well, that wasn't quite the right tool...", "Everything has fallen apart again...", CONTENT);
								d.endDialogue();
								return true;
							case 1:
								d.sendStatement("You pull the spring back onto the hammer mechanism.", "The hook ended tool worked well.");
								return true;
						}
					case 7:
						d.sendStatement("It seems the only part stopping the cannon from operating", "is the gear which throws the hammer to fire a cannonball.", "Which tool would you like to use to get it moving?");
						return true;
					case 8:
						d.sendOption("The hook tool.", "The gear wrench.", "The pliers.");
						return true;
					case 9:
						switch (optionId) {
							case 1:
							case 3:
								d.sendPlayerChat("Well, that wasn't quite the right tool...", "Probably should've used the gear wrench on the gear.", "Everything has fallen apart again...", CONTENT);
								d.endDialogue();
								return true;
							case 2:
								d.sendStatement("You crank the gear on the cannon's firing hammer.", "It turns and hits the firing pin as it should.");
								d.endDialogue();
								player.getActionSender().sendMessage("Well done! You've fixed the cannon, better go tell the captain.");
								player.setQuestStage(30, CANNON_FIXED);
								return true;
						}
				}
				return false;
			case LOLLK:
				switch (player.getQuestStage(getQuestID())) { //Dialogue per stage
					case FIND_HIDEOUT:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Thank the heavens, you saved me!", "I thought I'd be goblin lunch for sure!", HAPPY);
								return true;
							case 2:
								d.sendPlayerChat("Are you okay?", CONTENT);
								return true;
							case 3:
								d.sendNpcChat("I think so, I'd better run off home.", CONTENT);
								return true;
							case 4:
								d.sendPlayerChat("That's right, you get going, I'll catch up.", CONTENT);
								return true;
							case 5:
								d.sendNpcChat("Thanks again, brave adventurer.", CONTENT);
								d.endDialogue();
								player.setQuestStage(30, FOUND_LOLLK);
								CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
									@Override
									public void execute(CycleEventContainer b) {
										player.getActionSender().sendMessage("The dwarf child runs off into the caverns.");
										b.stop();
									}

									@Override
									public void stop() {
										for (Npc npc : World.getNpcs()) {
											if (npc.getNpcId() == LOLLK) {
												NpcLoader.destroyNpc(npc);
											}
										}
									}
								}, 5);
								return true;

						}
						return false;
				}
				return false;
			case CAPTAIN_LAWGOF:
				switch (player.getQuestStage(getQuestID())) { //Dialogue per stage
					case RETURN_TO_CAPTAIN:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hi.", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Hello trooper, any word from Nulodion?", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("Yes, I have spoken to him.", CONTENT);
								return true;
							case 4:
								d.sendPlayerChat("He gave me an ammo mould and some notes to", "give to you...", CONTENT);
								return true;
							case 5:
								d.sendNpcChat("Aah, of course, we make the ammo! This is great, now", "we will be able to defend ourselves. I don't know how to", "thank you...", CONTENT);
								return true;
							case 6:
								d.sendPlayerChat("You could give me a cannon...", CONTENT);
								return true;
							case 7:
								d.sendNpcChat("Hah! You'd be lucky, those things are worth a fortune.", CONTENT);
								return true;
							case 8:
								d.sendNpcChat("I'll tell you what though, I'll write to the Cannon", "Engineer requesting him to sell you one. He controls", "production of the cannons.", CONTENT);
								return true;
							case 9:
								d.sendNpcChat("He won't be able to give you one, but for the right", "price, I'm sure he'll sell one to you.", CONTENT);
								return true;
							case 10:
								d.sendPlayerChat("Hmmm... sounds interesting, I might take you", "up on that.", CONTENT);
								return true;
							case 11:
								QuestHandler.completeQuest(player, 30);
								d.dontCloseInterface();
								player.getInventory().removeItem(new Item(INSTRUCTION_MANUAL));
								player.getInventory().removeItem(new Item(AMMO_MOULD));
								return true;
						}
						return false;
					case CANNON_FIXED:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello again.", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Hello there trooper, how're things?", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("Well, I think I've done it... Take a look.", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("Well I don't beleive it, it seems to be working perfectly!", "I seem to have underestimated you, trooper!", HAPPY);
								return true;
							case 5:
								d.sendPlayerChat("Not bad for an adventurer, eh?", CONTENT);
								return true;
							case 6:
								d.sendNpcChat("Not bad at all, your effort is appreciated, my friend.", "Now, if I could figure what the thing uses as ammo...", CONTENT);
								return true;
							case 7:
								d.sendNpcChat("The Black Guard forgot to send instructions. I know I", "said that was the last favor, but...", CONTENT);
								return true;
							case 8:
								d.sendPlayerChat("What now?", CONTENT);
								return true;
							case 9:
								d.sendNpcChat("I can't leave this post, could you go to the Black Guard", "base and find out what this thing actually shoots?", CONTENT);
								return true;
							case 10:
								d.sendPlayerChat("I suppose I'll do it.", CONTENT);
								return true;
							case 11:
								d.sendNpcChat("That's great, we were lucky you came along when you", "did. The base is located just South of the Ice Mountain.", CONTENT);
								return true;
							case 12:
								d.sendNpcChat("You'll need to speak to Nulodion, the Dwarf Cannon", "engineer. He's the Weapons Development Chief for the", "Black Guard, so if anyone knows how to fire this thing,", "it'll be him.", CONTENT);
								return true;
							case 13:
								d.sendPlayerChat("Okay, I'll get right on it...", CONTENT);
								d.endDialogue();
								player.setQuestStage(30, FIND_NULODION);
								return true;
						}
						return true;
					case FIND_NULODION:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Hurry and find Nulodion trooper!", CONTENT);
								d.endDialogue();
								return true;
						}
						return true;
					case FIX_CANNON:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Get to fixin' that cannon trooper!", CONTENT);
								d.endDialogue();
								return true;
						}
						return true;
					case FOUND_LOLLK:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello, has Lollk returned yet?", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("He has, and I thank you from the bottom of my heart", "- without you he'd be a goblin barbecue!", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("Always a pleasure to help.", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("In that case, heh, could I ask you for one more favor...", HAPPY);
								return true;
							case 5:
								d.sendNpcChat("When the goblins attacked us some of them managed to", "slip past my guards and sabotage our cannon. I don't", "have anybody who understands how it works, could you", "have a look at it and see if you could get it working?", CONTENT);
								return true;
							case 6:
								d.sendPlayerChat("Alright, I'll see what I can do.", CONTENT);
								return true;
							case 7:
								d.sendNpcChat("Thank you, take this toolkit, you'll need it.", CONTENT);
								return true;
							case 8:
								if (player.getInventory().canAddItem(new Item(TOOLKIT))) {
									d.sendNpcChat("Report back to me if you manage to fix it.", CONTENT);
									d.endDialogue();
									player.getInventory().addItem(new Item(TOOLKIT));
									player.setQuestStage(30, FIX_CANNON);
									return true;
								} else {
									d.sendNpcChat("Ooh, you don't have room for the toolkit. Come", "back when you have an empty slot.", CONTENT);
									d.endDialogue();
									return true;
								}
						}
						return false;
					case FOUND_GILUB:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello.", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Have you been to the watch tower yet?", CONTENT);
								return true;
							case 3:
								if (player.getInventory().playerHasItem(DWARF_REMAINS)) {
									d.sendPlayerChat("I have some terrible news for you Captain, the goblins", "over ran the tower, your guards fought well but were", "overwhelmed.", CONTENT);
									return true;
								} else {
									d.sendPlayerChat("Yes, but I seem to have misplaced what I found", "there, I'll be right back.", CONTENT);
									d.endDialogue();
									return true;
								}
							case 4:
								d.sendGiveItemNpc("You give the Dwarf Captain his subordinate's remains...", new Item(0));
								return true;
							case 5:
								d.sendNpcChat("I can't believe it, Gilob was the finest lieutenant I had!", "We'll give him a fitting funeral, but what of his", "command? His son, Lollk, was with him. Did you find", "his body too?", SAD);
								return true;
							case 6:
								d.sendPlayerChat("No, there was only one body there, I searched", "pretty well.", CONTENT);
								return true;
							case 7:
								d.sendNpcChat("The goblins must have taken him. Please trooper, seek", "out the goblins' hideout and return the lad to us. They", "always attack from the South-east, so they must be", "based down there.", ANGRY_1);
								return true;
							case 8:
								d.sendPlayerChat("Okay, I'll see if I can find their hideout.", CONTENT);
								d.endDialogue();
								player.getInventory().removeItem(new Item(DWARF_REMAINS));
								player.setQuestStage(30, FIND_HIDEOUT);
								return true;

						}
						return false;
					case 0: //Starting the quest
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello.", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Guthix be praised, the cavalry has arrived! Hero, how", "would you like to be made an honorary member of the", "Black Guard?", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("The Black Guard, what's that?", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("Ha ha! 'What's that' he asks, what a sense of humor!", "The Black Guard is the finest regiment in the dwarven", "army. Only the best of the best are allowed to join it", "and then they recieve months of rigorous training.", CONTENT);
								return true;
							case 5:
								d.sendNpcChat("However, we are currently in need of a hero, so for a", "limited time only I'm offering you, a human, a chance", "to join this prestigious regiment. What do you say?", CONTENT);
								return true;
							case 6:
								d.sendOption("I'm sorry, I'm too busy mining.", "Sure, I'd be honored to join.");
								return true;
							case 7:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("I'm sorry, I'm too busy mining.", CONTENT);
										d.endDialogue();
										return true;
									case 2:
										d.sendPlayerChat("Sure, I'd be honored to join.", CONTENT);
										return true;
								}
							case 8:
								d.sendNpcChat("That's the spirit! Now trooper, we have no time to waste", "- the goblins are attacking from the forests to the", "South. There are so many of them, they are", "overwhelming my men and breaking through our", CONTENT);
								return true;
							case 9:
								d.sendNpcChat("perimeter defenses; could you please try to fix the", "stockage by replacing the broken rails with these new", "ones?", CONTENT);
								return true;
							case 10:
								d.sendPlayerChat("Sure, sounds easy enough...", CONTENT);
								return true;
							case 11:
								if (player.getInventory().canAddItem(new Item(RAILING, 6))) {
									d.sendGiveItemNpc("The Dwarf Captain hands you six railings.", new Item(RAILING));
									d.endDialogue();
									player.getInventory().addItem(new Item(RAILING, 6));
									QuestHandler.startQuest(player, 30);
									return true;
								} else {
									d.sendNpcChat("Ah, you don't have room for these railings!", "Return when you do.", CONTENT);
									d.endDialogue();
									return true;
								}
						}
						return false;
					case QUEST_COMPLETE:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Thank you ever so much again trooper!", HAPPY);
								d.endDialogue();
								return true;
						}
						return false;
					case RAILINGS_FIXED:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Well done, trooper! The goblins seem to have stopped", "getting in. I think you've done the job!", CONTENT);
								return true;
							case 2:
								d.sendPlayerChat("Great, I'll be getting on then.", CONTENT);
								return true;
							case 3:
								d.sendNpcChat("What? I'll have you jailed for desertion!", ANNOYED);
								return true;
							case 4:
								d.sendNpcChat("Besides, I have another commission for you. Just", "before the goblins over-ran us we lost contact with our", "watch tower to the South, that's why the goblins", "managed to catch us unaware. I'd like you to perform", ANNOYED);
								return true;
							case 5:
								d.sendNpcChat("a covert operation into enemy territory, to check up on", "the guards we have stationed there.", ANNOYED);
								return true;
							case 6:
								d.sendNpcChat("They should have reported in by now...", SAD);
								return true;
							case 7:
								d.sendPlayerChat("Okay, I'll see what I can find out.", CONTENT);
								return true;
							case 8:
								d.sendNpcChat("Excellent! I have two men there, the dwarf-in-charge is", "called Gilob, find him and tell him that I'll send him a", "relief guard just as soon as we mop up these remaining", "goblins.", CONTENT);
								return true;
							case 9:
								d.sendPlayerChat("Sounds easy enough. See you soon.", CONTENT);
								d.endDialogue();
								player.setQuestStage(30, FIND_GILUB);
								return true;
						}
						return false;
					case FIND_GILUB:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Did you find Gilub yet?", CONTENT);
								return true;
							case 2:
								d.sendPlayerChat("Nope.", CONTENT);
								return true;
							case 3:
								d.sendNpcChat("What?! I gave you an order trooper!", "See to it that it is followed!", ANGRY_1);
								d.endDialogue();
								return true;
						}
						return false;
					case QUEST_STARTED:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Did you fix those railings yet?", CONTENT);
								return true;
							case 2:
								d.sendOption("Actually, I need another piece of railing.", "Not yet I'm afraid.");
								return true;
							case 3:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("Actually, I need another piece of railing.", CONTENT);
										return true;
									case 2:
										d.sendPlayerChat("Not yet I'm afraid...", SAD);
										d.endDialogue();
										return true;
								}
							case 4:
								d.sendNpcChat("Ah, you humans, always losing things.", "Here's another.", CONTENT);
								return true;
							case 5:
								d.sendGiveItemNpc("The Captain hands you another piece of railing.", new Item(RAILING));
								d.endDialogue();
								player.getInventory().addItem(new Item(RAILING));
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
