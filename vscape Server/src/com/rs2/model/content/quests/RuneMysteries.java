package com.rs2.model.content.quests;

import com.rs2.model.content.Shops;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.ANNOYED;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import static com.rs2.model.content.dialogue.Dialogues.VERY_SAD;
import static com.rs2.model.content.dialogue.Dialogues.checkTrim;
import static com.rs2.model.content.dialogue.Dialogues.trimCape;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.ShopManager;
import com.rs2.model.players.item.Item;

public class RuneMysteries implements Quest {

	public static final int QUEST_STARTED = 1;
	public static final int GIVEN_ITEMS = 2;
	public static final int DELIVER_ITEMS = 3;
	public static final int GIVEN_ITEMSB = 4;
	public static final int NPC_THINKING = 5;
	public static final int DELIVER_ITEMSB = 6;
	public static final int QUEST_COMPLETE = 7;

	public int dialogueStage = 0;

	private static final int questPointReward = 1;

	public int getQuestID() {
		return 5;
	}

	public String getQuestName() {
		return "Rune Mysteries";
	}

	public String getQuestSaveName() {
		return "rune-mysteries";
	}

	public boolean canDoQuest(Player player) {
		return true;
	}

	public void getReward(Player player) {
		player.addQuestPoints(questPointReward);
		player.getActionSender().QPEdit(player.getQuestPoints());
	}

	public void completeQuest(Player player) {
		getReward(player);
		player.getActionSender().sendInterface(12140);
		player.getActionSender().sendString("You have completed: " + getQuestName(), 12144);
		player.getActionSender().sendString(questPointReward + " Quest Point", 12150);
		player.getActionSender().sendString("Runecrafting Skill", 12151);
		player.getActionSender().sendString("Air Talisman", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), 7335);
	}

	public void sendQuestRequirements(Player player) {
		String prefix = "";
		int questStage = player.getQuestStage(getQuestID());
		switch (questStage) {
			case 1:
				player.getActionSender().sendString(getQuestName(), 8144);
				player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Duke Horacio", 8147);
				player.getActionSender().sendString("@str@" + "found in Lumbridge Castle.", 8148);

				player.getActionSender().sendString("Duke Horacio has asked you to take the Air Talisman he", 8150);
				player.getActionSender().sendString("found to the head wizard at Wizards' Tower.", 8151);
				break;
			case 2:
				player.getActionSender().sendString(getQuestName(), 8144);
				player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Duke Horacio", 8147);
				player.getActionSender().sendString("@str@" + "found in Lumbridge Castle.", 8148);

				player.getActionSender().sendString("@str@" + "Duke Horacio has asked you to take the Air Talisman he", 8150);
				player.getActionSender().sendString("@str@" + "found to the head wizard at Wizards' Tower.", 8151);

				player.getActionSender().sendString("I need to speak to the head wizard again...", 8153);
				break;
			case 3:
				player.getActionSender().sendString(getQuestName(), 8144);
				player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Duke Horacio", 8147);
				player.getActionSender().sendString("@str@" + "found in Lumbridge Castle.", 8148);

				player.getActionSender().sendString("@str@" + "Duke Horacio has asked you to take the Air Talisman he", 8150);
				player.getActionSender().sendString("@str@" + "found to the head wizard at Wizards' Tower.", 8151);

				player.getActionSender().sendString("The head wizard has asked me to take his research notes to", 8153);
				player.getActionSender().sendString("Aubury's rune shop in varrock.", 8154);
				break;
			case 4:
				player.getActionSender().sendString(getQuestName(), 8144);
				player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Duke Horacio", 8147);
				player.getActionSender().sendString("@str@" + "found in Lumbridge Castle.", 8148);

				player.getActionSender().sendString("@str@" + "Duke Horacio has asked you to take the Air Talisman he", 8150);
				player.getActionSender().sendString("@str@" + "found to the head wizard at Wizards' Tower.", 8151);

				player.getActionSender().sendString("@str@" + "The head wizard has asked me to take his research notes to", 8153);
				player.getActionSender().sendString("@str@" + "Aubury's rune shop in varrock.", 8154);

				player.getActionSender().sendString("I should speak to Aubury again...", 8156);
				break;
			case 5:
				player.getActionSender().sendString(getQuestName(), 8144);
				player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Duke Horacio", 8147);
				player.getActionSender().sendString("@str@" + "found in Lumbridge Castle.", 8148);

				player.getActionSender().sendString("@str@" + "Duke Horacio has asked you to take the Air Talisman he", 8150);
				player.getActionSender().sendString("@str@" + "found to the head wizard at Wizards' Tower.", 8151);

				player.getActionSender().sendString("@str@" + "The head wizard has asked me to take his research notes to", 8153);
				player.getActionSender().sendString("@str@" + "Aubury's rune shop in varrock.", 8154);

				player.getActionSender().sendString("Aubury told me I should speak to him again after he's done.", 8156);
				break;
			case 6:
				player.getActionSender().sendString(getQuestName(), 8144);
				player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Duke Horacio", 8147);
				player.getActionSender().sendString("@str@" + "found in Lumbridge Castle.", 8148);

				player.getActionSender().sendString("@str@" + "Duke Horacio has asked you to take the Air Talisman he", 8150);
				player.getActionSender().sendString("@str@" + "found to the head wizard at Wizards' Tower.", 8151);

				player.getActionSender().sendString("@str@" + "The head wizard has asked me to take his research notes to", 8153);
				player.getActionSender().sendString("@str@" + "Aubury's rune shop in varrock.", 8154);

				player.getActionSender().sendString("Aubury gave me some notes and told me to take them to the", 8156);
				player.getActionSender().sendString("head wizard at Wizards' Tower.", 8157);
				break;
			case 7:
				player.getActionSender().sendString(getQuestName(), 8144);
				player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Duke Horacio", 8147);
				player.getActionSender().sendString("@str@" + "found in Lumbridge Castle.", 8148);

				player.getActionSender().sendString("@str@" + "Duke Horacio has asked you to take the Air Talisman he", 8150);
				player.getActionSender().sendString("@str@" + "found to the head wizard at Wizards' Tower.", 8151);

				player.getActionSender().sendString("@str@" + "The head wizard has asked me to take his research notes to", 8153);
				player.getActionSender().sendString("@str@" + "Aubury's rune shop in Varrock.", 8154);

				player.getActionSender().sendString("@str@" + "Aubury gave me some notes and told me to take them to the", 8156);
				player.getActionSender().sendString("@str@" + "head wizard at Wizards' Tower.", 8157);

				player.getActionSender().sendString("@red@You have completed this quest!", 8159);
				break;
			default:
				player.getActionSender().sendString(getQuestName(), 8144);
				player.getActionSender().sendString(prefix + "To start the quest, you should talk to Duke Horacio", 8147);
				player.getActionSender().sendString(prefix + "found in Lumbridge Castle.", 8148);
		}

	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 7335);
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
			player.getActionSender().sendString("@yel@" + getQuestName(), 7335);
		} else if (questStage >= QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 7335);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 7335);
		}

	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		String prefix = "";
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString(prefix + "To start the quest, you should talk to Doric", 8147);
		player.getActionSender().sendString(prefix + "found in North West of Falador.", 8148);
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public boolean itemHandling(final Player player, int itemId) {
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		return false;
	}

	public boolean doItemOnObject(final Player player, int object, int item) {
		return false;
	}

	public boolean doObjectClicking(final Player player, int object, int x, int y) {
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
			//RUNE MYSTERIES / DRAGON SLAYER / DUKE HORACIO
			case 741:
				if (!QuestHandler.questCompleted(player, 5)) {
					switch (player.getQuestStage(5)) {
						case 0: // quest stage 0
							switch (player.getDialogue().getChatId()) {
								case 1:
									player.getDialogue().sendNpcChat("Greetings. Welcome to my castle.", CONTENT);
									return true;
								case 2:
									player.getDialogue().sendOption("Have you any quests for me?", "Can I buy an anti-fire shield?", "Nevermind.");
									return true;
								case 3:
									switch (optionId) {
										case 1:
											player.getDialogue().sendPlayerChat("Have you any quests for me?", CONTENT);
											player.getDialogue().setNextChatId(7);
											return true;
										case 2:
											player.getDialogue().sendPlayerChat("Can I buy an anti-fire shield?", CONTENT);
											player.getDialogue().setNextChatId(4);
											return true;
										case 3:
											player.getDialogue().sendPlayerChat("Nevermind.", CONTENT);
											player.getDialogue().endDialogue();
											return true;
									}
									break;
								case 4: // shield temp stuff
									player.getDialogue().sendNpcChat("Sure, It will cost you 1000 coins.", CONTENT);
									return true;
								case 5:
									player.getDialogue().sendOption("Ok, here you go.", "Nevermind.");
									return true;
								case 6:
									switch (optionId) {
										case 1:
											if (!player.getInventory().playerHasItem(new Item(995, 1000))) {
												player.getDialogue().sendPlayerChat("Sorry, I don't have enough coins.", SAD);
											} else if (player.getInventory().getItemContainer().freeSlots() < 1 && player.getInventory().getItemContainer().getCount(995) != 1000) {
												player.getDialogue().sendNpcChat("Looks like you don't have enough inventory space.", CONTENT);
											} else {
												player.getDialogue().sendGiveItemNpc("You exchange 1000 gold for an anti-fire shield.", new Item(1540));
												player.getInventory().removeItem(new Item(995, 1000));
												player.getInventory().addItem(new Item(1540, 1));
												break;
											}
											player.getDialogue().endDialogue();
											return true;
										case 2:
											player.getDialogue().sendPlayerChat("Nevermind.", CONTENT);
											player.getDialogue().endDialogue();
											return true;
									}
									break;	// shield temp stuff end
								case 7:
									player.getDialogue().sendNpcChat("Well, it's not really a quest, but I recently discovered", "this strange talisman.", CONTENT);
									return true;
								case 8:
									player.getDialogue().sendNpcChat("It seems to be mystical and I have never seen anything", "like it before. Would take it to the head wizard at", CONTENT);
									return true;
								case 9:
									player.getDialogue().sendNpcChat("the Wizards' Tower for me? It's just south-west of here", "and should not take you very long at all. I would be", "awfully grateful.", CONTENT);
									return true;
								case 10:
									player.getDialogue().sendOption("Sure, no problem.", "Not right now.");
									return true;
								case 11:
									switch (optionId) {
										case 1:
											player.getDialogue().sendPlayerChat("Sure, no problem.", CONTENT);
											player.getDialogue().setNextChatId(12);
											return true;
										case 2:
											player.getDialogue().sendPlayerChat("Not right now.", CONTENT);
											player.getDialogue().endDialogue();
											return true;
									}
									break;
								case 12:
									player.getDialogue().sendNpcChat("Thank you very much, stranger. I am sure the head", "wizard will reward you for such an interesting find.", CONTENT);
									return true;
								case 13:
									player.getDialogue().sendStatement("The Duke hands you an @blu@air talisman@bla@.");
									player.getDialogue().endDialogue();
									player.getInventory().addItemOrDrop(new Item(1438, 1));
									QuestHandler.startQuest(player, 5);
									return true;
							}
							break; // quest stage 0 end
						case 1: // quest stage 1
							switch (player.getDialogue().getChatId()) {
								case 1:
									player.getDialogue().sendNpcChat("Have you given the talisman to the head wizard?", CONTENT);
									return true;
								case 2:
									player.getDialogue().sendPlayerChat("No, not yet.", CONTENT);
									return true;
								case 3:
									player.getDialogue().sendNpcChat("You can find the Head Wizard at the Wizards' Tower,", "It's just south-west of here.", CONTENT);
									return true;
								case 4:
									player.getDialogue().sendPlayerChat("Ok thanks!", CONTENT);
									player.getDialogue().endDialogue();
									return true;
							}
							break; // quest stage 1 end
					}
				} else {
					switch (player.getDialogue().getChatId()) {
						case 1:
							player.getDialogue().sendNpcChat("Greetings. Welcome to my castle.", CONTENT);
							return true;
						case 2:
							player.getDialogue().sendOption("Have you any quests for me?", "Can I buy an anti-fire shield?", "Nevermind.");
							return true;
						case 3:
							switch (optionId) {
								case 1:
									player.getDialogue().sendPlayerChat("Have you any quests for me?", CONTENT);
									player.getDialogue().setNextChatId(7);
									return true;
								case 2:
									player.getDialogue().sendPlayerChat("Can I buy an anti-fire shield?", CONTENT);
									player.getDialogue().setNextChatId(4);
									return true;
								case 3:
									player.getDialogue().sendPlayerChat("Nevermind.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
							}
							return false;
						case 4: // shield temp stuff
							player.getDialogue().sendNpcChat("Sure, it will cost you 1000 coins.", CONTENT);
							return true;
						case 5:
							player.getDialogue().sendOption("Ok, here you go.", "Nevermind.");
							return true;
						case 6:
							switch (optionId) {
								case 1:
									if (!player.getInventory().playerHasItem(new Item(995, 1000))) {
										player.getDialogue().sendPlayerChat("Sorry, I don't have enough coins.", SAD);
									} else if (player.getInventory().getItemContainer().freeSlots() < 1 && player.getInventory().getItemContainer().getCount(995) != 1000) {
										player.getDialogue().sendNpcChat("Looks like you don't have enough inventory space.", CONTENT);
									} else {
										player.getDialogue().sendGiveItemNpc("You exchange 1000 gold for an anti-fire shield.", new Item(1540));
										player.getInventory().removeItem(new Item(995, 1000));
										player.getInventory().addItem(new Item(1540, 1));
									}
									player.getDialogue().endDialogue();
									return true;
								case 2:
									player.getDialogue().sendPlayerChat("Nevermind.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
							}
							return false;
						case 7:
							player.getDialogue().sendNpcChat("I don't have any more quests for you.", CONTENT);
							player.getDialogue().setNextChatId(2);
							return true;
					}
				}
				return false;
			case 300:
				if (!QuestHandler.questCompleted(player, 5)) {
					switch (player.getQuestStage(5)) {
						case 1: // quest stage 1
							switch (player.getDialogue().getChatId()) {
								case 1:
									player.getDialogue().sendNpcChat("Welcome adventurer, to the world renowned", "Wizards' Tower. How may I help you?", CONTENT);
									return true;
								case 2:
									player.getDialogue().sendOption("I'm just looking around.", "I'm looking for the head wizard.");
									return true;
								case 3:
									switch (optionId) {
										case 1:
											player.getDialogue().sendPlayerChat("I'm just looking around.", CONTENT);
											player.getDialogue().endDialogue();
											return true;
										case 2:
											player.getDialogue().sendPlayerChat("I'm looking for the head wizard.", CONTENT);
											player.getDialogue().setNextChatId(4);
											return true;
									}
									return false;
								case 4:
									player.getDialogue().sendNpcChat("Oh, you are are you?", "And just why would you be doing that?", CONTENT);
									return true;
								case 5:
									player.getDialogue().sendPlayerChat("The Duke of Lumbridge sent me to find him. I have", "this weird talisman he found. He said the head wizard", "would be very interested in it.", CONTENT);
									return true;
								case 6:
									player.getDialogue().sendNpcChat("Did he now? HmmmMMMMMmmmmm.", "Well that IS interesting. Hand it over then adventurer,", "let me see what all the hubbub about it is.", "Just some amulet I'll wager.", CONTENT);
									return true;
								case 7:
									player.getDialogue().sendOption("Ok, here you are.", "No, I'll only give it to the head wizard.");
									return true;
								case 8:
									switch (optionId) {
										case 1:
											player.getDialogue().sendPlayerChat("Ok, here you are.", CONTENT);
											player.getDialogue().setNextChatId(11);
											return true;
										case 2:
											player.getDialogue().sendPlayerChat("No, I'll only give it to the head wizard.", ANNOYED);
											player.getDialogue().setNextChatId(9);
											return true;
									}
									return false;
								case 9:
									player.getDialogue().sendNpcChat("YOU ARE ONE CHEEKY CUNT MATE,", "I SWEAR ON ME MUM.", "I'M THE HEAD WIZARD!", ANGRY_1);
									return true;
								case 10:
									player.getDialogue().sendPlayerChat("Oh, o-ok..", Dialogues.DISTRESSED);
									return true;
								case 11:
									if (player.carryingItem(1438)) {
										player.getDialogue().sendStatement("You hand the Talisman to the wizard.");
										player.getInventory().removeItem(new Item(1438, 1));
										player.setQuestStage(5, 2);
										player.getDialogue().setNextChatId(1);
									} else {
										player.getDialogue().sendStatement("You don't have the Talisman.");
										player.getDialogue().endDialogue();
									}
									return true;
							}
							return false; // quest stage 1 end
						case 2: // quest stage 2 start
							switch (player.getDialogue().getChatId()) {
								case 1:
									player.getDialogue().sendNpcChat("Wow! This is... incredible!", CONTENT);
									return true;
								case 2:
									player.getDialogue().sendNpcChat("Th-this talisman you brought me...! It is the last piece", "of the puzzle. I think! Finally! The legacy of our", "ancestors... it will return to us once more!", CONTENT);
									return true;
								case 3:
									player.getDialogue().sendNpcChat("I need time to study this, " + player.getUsername() + ". Can you please", "do me this task while I study this talisman you have", "brought me? In the mighty town of Varrock, which", CONTENT);
									return true;
								case 4:
									player.getDialogue().sendNpcChat("is located northeast of here, there is a certain shop", "that sells magical runes. I have in this package all of the", "research I have done relating to the Rune Stones, and", CONTENT);
									return true;
								case 5:
									player.getDialogue().sendNpcChat("require somebody to take them to the skopkeeper so that", "he may share my research and offer me his insights.", "Do this thing for me, and bring back what he gives you,", CONTENT);
									return true;
								case 6:
									player.getDialogue().sendNpcChat("and if my suspiscions are correct, I will let you into the", "knowledge of one of the greatest secrets this world has", "ever known! A secret so powerful that was destroyed", CONTENT);
									return true;
								case 7:
									player.getDialogue().sendNpcChat("with the original Wizards' Tower all of those centuries", "ago! My research, combined with this mysterious", "talisman... I cannot believe the answer to", "the mysteries is so close now!", CONTENT);
									return true;
								case 8:
									player.getDialogue().sendNpcChat("Do this thing for me " + player.getUsername() + ". Be rewarded in a", "way you can never imagine.", CONTENT);
									return true;
								case 9:
									player.getDialogue().sendOption("Yes, certainly.", "No, I'm busy.");
									return true;
								case 10:
									switch (optionId) {
										case 1:
											player.getDialogue().sendPlayerChat("Yes, certainly.", CONTENT);
											player.getDialogue().setNextChatId(11);
											return true;
										case 2:
											player.getDialogue().sendPlayerChat("No, I'm busy grinding you cheeky cunt.", ANGRY_1);
											player.getDialogue().endDialogue();
											return true;
									}
									return false;
								case 11:
									player.getDialogue().sendNpcChat("Take this package and head directly north", "from here, through Draynor Village until you reach", "the Barbarian Village. Then head east from there", "until you reach Varrock.", CONTENT);
									return true;
								case 12:
									player.getDialogue().sendNpcChat("Once in Varrock take this package to the owner of the", "rune shop. His name is Aubury. You may find it", "helpful to ask one of Varrock's citizens for directions,", CONTENT);
									return true;
								case 13:
									player.getDialogue().sendNpcChat("as Varrock can be a confusing place for the first time", "visitor. He will give you a special item - bring it back to", "me and I shall show you the mystery of the runes...", CONTENT);
									return true;
								case 14:
									player.getDialogue().sendStatement("The head wizard gives you a package.");
									player.getDialogue().endDialogue();
									player.getInventory().addItemOrDrop(new Item(290, 1));
									player.setQuestStage(5, 3);
									return true;
							}
							return false;// quest stage 2 end
						case 3:// quest stage 3
							switch (player.getDialogue().getChatId()) {
								case 1:
									if (player.getInventory().ownsItem(290)) {
										player.getDialogue().sendPlayerChat("Where do I need to go again?", CONTENT);
										player.getDialogue().setNextChatId(3);
									} else {
										player.getDialogue().sendPlayerChat("I lost the package...", CONTENT);
										player.getDialogue().setNextChatId(2);
									}
									return true;
								case 2:
									player.getDialogue().sendStatement("The head wizard gives you a package.");
									player.getDialogue().endDialogue();
									player.getInventory().addItemOrDrop(new Item(290, 1));
									return true;
								case 3:
									player.getDialogue().sendNpcChat("Head directly North", "from here, through Draynor Village until you reach", "the Barbarian Village. Then head east from there", "until you reach Varrock.", CONTENT);
									return true;
								case 4:
									player.getDialogue().sendNpcChat("Once in Varrock take this package to the owner of the", "rune shop. His name is Aubury. You may find it", "helpful to ask one of Varrock's citizens for directions,", CONTENT);
									return true;
								case 5:
									player.getDialogue().sendPlayerChat("Ok! Thanks!", CONTENT);
									player.getDialogue().endDialogue();
									return true;
							}
							return false;// quest stage 3 end
						case 6://stage 6 
							switch (player.getDialogue().getChatId()) {
								case 1:
									player.getDialogue().sendNpcChat("Welcome adventurer, to the world renowned", "Wizards' Tower. How may I help you?", CONTENT);
									return true;
								case 2:
									player.getDialogue().sendNpcChat("Ah, " + player.getUsername() + ". How goes your quest? Have you", "delivered the research notes to my friend Aubury yet?", CONTENT);
									return true;
								case 3:
									player.getDialogue().sendPlayerChat("Yes, I have. He gave me some research notes", "to pass on to you.", CONTENT);
									return true;
								case 4:
									player.getDialogue().sendNpcChat("May I have his notes then?", CONTENT);
									return true;
								case 5:
									if (player.getInventory().playerHasItem(291)) {
										player.getDialogue().sendPlayerChat("Sure. I have them right here.", CONTENT);
										player.getDialogue().setNextChatId(6);
									} else {
										player.getDialogue().sendPlayerChat("Sorry, I don't have them right now.", CONTENT);
										player.getDialogue().endDialogue();
									}
									return true;
								case 6:
									player.getDialogue().sendNpcChat("Well, before you hand them over to me, as you", "have been nothing but truthful with me to this point,", "and I admire that in an adventurer. I will let you", "into the secret of our research.", CONTENT);
									return true;
								case 7:
									player.getDialogue().sendNpcChat("Now as you may or may not know, many", "centuries ago, the wizards at this Tower", "learnt the secret of creating Rune Stones, which", "allowed us to cast Magic very easily.", CONTENT);
									return true;
								case 8:
									player.getDialogue().sendNpcChat("When this tower was burnt down the secret of", "creating runes was lost to us for all time... except it", "wasn't. Some months ago, while searching these ruins", "for information from the old days,", CONTENT);
									return true;
								case 9:
									player.getDialogue().sendNpcChat("I came upon a scroll almost destroyed that detailed a", "magical rock deep in the icefields of the North closed", "off from access by anything other than magical means.", CONTENT);
									return true;
								case 10:
									player.getDialogue().sendNpcChat("This rock was called the 'Rune Essence' by the", "magicians who studied its powers. Apparently by simply", "breaking a chunk from it a Rune Stone could be", "fashioned very quickly and easily at certain", CONTENT);
									return true;
								case 11:
									player.getDialogue().sendNpcChat("elemental altars that were scattered across the land", "back then. Now, this is an interesting little peice of", "history, but not much use to us as modern wizards", "without access to the Rune Essence", CONTENT);
									return true;
								case 12:
									player.getDialogue().sendNpcChat("or these elemental altars. This is where you and", "Aubury come into this story. A few weeks back", "Aubury discovered in a standard delivery of runes", "to his store a parchment detailing a", CONTENT);
									return true;
								case 13:
									player.getDialogue().sendNpcChat("teleportation spell that he had never come across", "before. To his shock when cast it took him to a", "stange rock he had never encountered before...", "yet that felt strangely familiar...", CONTENT);
									return true;
								case 14:
									player.getDialogue().sendNpcChat("As I'm sure you have now guessed he had discovered a", "portal leading to the mythical Rune Essence. As soon as", "he told me of this spell I saw the importance of his find", CONTENT);
									return true;
								case 15:
									player.getDialogue().sendNpcChat("for if we could but find the elemental altars spoken", "of in the ancient text we would once more be able", "to create runes as our ancestors had done! It would", "be the saviour of the wizards' art!", CONTENT);
									return true;
								case 16:
									player.getDialogue().sendPlayerChat("I'm still not sure how I fit into", "this little story of yours...", CONTENT);
									return true;
								case 17:
									player.getDialogue().sendNpcChat("You haven't guessed? This talisman you brought me...", "it is the key to the elemental altar of air! When", "you hold it next it will direct you towards", CONTENT);
									return true;
								case 18:
									player.getDialogue().sendNpcChat("the entrance to the long forgotten Air Altar! By", "bring peices of the Rune Essence to the Air Temple,", "you will be able to fashion your own Air Runes!", CONTENT);
									return true;
								case 19:
									player.getDialogue().sendNpcChat("And this is not all! By finding other talismans similar", "to this one you will eventually be able to craft every", "rune that is available on this world! Just", CONTENT);
									return true;
								case 20:
									player.getDialogue().sendNpcChat("as our ancestors did! I cannot stress enough what a", "find this is! Now, due to the risks involved of letting", "this mighty power fall into the wrong hands", CONTENT);
									return true;
								case 21:
									player.getDialogue().sendNpcChat("I will keep the teleport skill to the Rune Essence", "a closely guarded secret shared only by myself", "and those Magic users around the world", "whom I trust enough to keep it.", CONTENT);
									return true;
								case 22:
									player.getDialogue().sendNpcChat("This means that if any evil power should discover", "the talismans required to enter the elemental", "temples we will be able to prevent their access", "to the Rune Essence and prevent", CONTENT);
									return true;
								case 23:
									player.getDialogue().sendNpcChat("tragedy befalling this world. I know not where the", "temples are located nor do I know where the talismans", "have been scattered to in this land, but I now", CONTENT);
									return true;
								case 24:
									player.getDialogue().sendNpcChat("return your Air Talisman to you. Find the Air", "Temple and you will be able to charge your Rune", "Essence to become Air Runes at will. Any time", CONTENT);
									return true;
								case 25:
									player.getDialogue().sendNpcChat("you wish to visit the Rune Essence speak to me", "or Aubury and we will open a portal to that", "mystical place for you to visit.", CONTENT);
									return true;
								case 26:
									player.getDialogue().sendPlayerChat("So only you and Aubury know the teleport", "spell to the Rune Essence?", CONTENT);
									return true;
								case 27:
									player.getDialogue().sendNpcChat("No... there are others... whom I will tell of your", "authorisation to visit that place. When you speak", "to them they will know you and grant you", "access to that place when asked.", CONTENT);
									return true;
								case 28:
									player.getDialogue().sendNpcChat("Use the Air Talisman to locate the air temple,", "and use any further talismans you find to locate", "the other missing elemental temples.", "Now... my research notes please?", CONTENT);
									return true;
								case 29:
									player.getDialogue().sendGiveItemNpc("You hand the head wizard the research notes.", "He hands you back the Air Talisman.", new Item(291), new Item(1438));
									player.getInventory().removeItem(new Item(291, 1));
									player.getInventory().addItemOrDrop(new Item(1438, 1));
									player.getDialogue().endDialogue();
									player.setQuestStage(5, 7);
									QuestHandler.completeQuest(player, 5);
									return true;
							}
							return false;// quest stage 6 end
					}
				}
				return false;
			case 553: // AUBURY
				if (!QuestHandler.questCompleted(player, 5)) {
					switch (player.getQuestStage(5)) {
						case 0:
						case 1:
						case 2:
							switch (player.getDialogue().getChatId()) {
								case 1:
									player.getDialogue().sendNpcChat("What a nice day in videogames!", "Do you want to buy some runes?", CONTENT);
									return true;
								case 2:
									if (player.getSkill().getLevel()[Skill.RUNECRAFTING] == 99 && !checkTrim(player)) {
										player.getDialogue().sendOption("Yes please!", "No thanks.", "I want my skillcape!");
									} else if (player.getSkill().getLevel()[Skill.RUNECRAFTING] == 99 && checkTrim(player)) {
										player.getDialogue().sendOption("Yes please!", "No thanks.", "I want my skillcape!", "Can you trim my skillcape?");
									} else {
										player.getDialogue().sendOption("Yes please!", "No thanks.");
									}
									return true;
								case 3:
									switch (optionId) {
										case 1:
											player.getDialogue().sendPlayerChat("Yes please!", CONTENT);
											Shops.openShop(player, 553);
											player.getDialogue().dontCloseInterface();
											return true;
										case 2:
											player.getDialogue().sendPlayerChat("No thanks.", CONTENT);
											player.getDialogue().endDialogue();
											return true;
										case 3:
											player.getDialogue().sendPlayerChat("I want my skillcape!", ANGRY_1);
											player.getDialogue().setNextChatId(5);
											return true;
										case 4:
											player.getDialogue().sendPlayerChat("Can you trim my skillcape?", CONTENT);
											player.getDialogue().setNextChatId(8);
											return true;
									}
								case 5:
									player.getDialogue().sendNpcChat("You've earned it.", CONTENT);
									return true;
								case 6:
									ShopManager.openShop(player, 178);
									player.getDialogue().dontCloseInterface();
									return true;
								case 8:
									player.getDialogue().sendNpcChat("Sure, but this is irreversible!", "However, you can always buy another untrimmed cape.", CONTENT);
									return true;
								case 9:
									if (player.getInventory().playerHasItem(9765)) {
										player.getDialogue().sendNpcChat("Here you are.", CONTENT);
										trimCape(player, 9765);
									} else {
										player.getDialogue().sendPlayerChat("I guess I don't have the cape on me.", VERY_SAD);
										player.getDialogue().endDialogue();
									}
									return true;
							}
							return false;
						case 3: //stage 3
							switch (player.getDialogue().getChatId()) {
								case 1:
									player.getDialogue().sendNpcChat("What a nice day in videogames!", "Do you want to buy some runes?", CONTENT);
									return true;
								case 2:
									player.getDialogue().sendOption("Yes please!", "No thanks.", "I have been sent here with a package for you.");
									return true;
								case 3:
									switch (optionId) {
										case 1:
											player.getDialogue().sendPlayerChat("Yes please!", CONTENT);
											Shops.openShop(player, 553);
											player.getDialogue().dontCloseInterface();
											return true;
										case 2:
											player.getDialogue().sendPlayerChat("No thanks.", CONTENT);
											player.getDialogue().endDialogue();
											return true;
										case 3:
											player.getDialogue().sendPlayerChat("I have been sent here with a package for you. It's from", "the head wizard at the Wizards' Tower.", CONTENT);
											player.getDialogue().setNextChatId(4);
											return true;
									}
									return false;
								case 4:
									player.getDialogue().sendNpcChat("Really? But... surely he can't have..? Please let me", "have it, it must be extremely important for him to have", "sent a stranger.", CONTENT);
									return true;
								case 5:
									if (player.getInventory().playerHasItem(290)) {
										player.getDialogue().sendStatement("You hand Aubury the research package.");
										player.getInventory().removeItem(new Item(290, 1));
										player.setQuestStage(5, 4);
										player.getDialogue().setNextChatId(1);
									} else {
										player.getDialogue().sendStatement("You don't have the package.");
										player.getDialogue().endDialogue();
									}
									return true;
							}
							return false; //stage 3 end
						case 4: //stage 4
							switch (player.getDialogue().getChatId()) {
								case 1:
									player.getDialogue().sendNpcChat("This... this is incredible. Please give me a few moments", "to quickly look over this and then talk to me again.", CONTENT);
									player.getDialogue().endDialogue();
									player.setQuestStage(5, 5);
									return true;
							}
							return false;//stage 4 end
						case 5: //stage 5
							switch (player.getDialogue().getChatId()) {
								case 1:
									player.getDialogue().sendNpcChat("My gratitude to you adventurer for bringing me these", "research notes. I notice that you brought the head", "wizard a special talisman that was the key to finally", "unlocking the puzzle.", CONTENT);
									return true;
								case 2:
									player.getDialogue().sendNpcChat("Combined with the information I had already collated", "regarding the Rune Essence. I think we have finally", "unlocked the power to", CONTENT);
									return true;
								case 3:
									player.getDialogue().sendNpcChat("...no. I am getting ahead of myself. Please take this", "summary of my research back to the head wizard at", "the Wizards' Tower. I trust his judgement on whether", "to let you in on our little secret or not.", CONTENT);
									return true;
								case 4:
									player.getDialogue().sendStatement("Aubury gives you his research notes.");
									player.getDialogue().endDialogue();
									player.getInventory().addItemOrDrop(new Item(291, 1));
									player.setQuestStage(5, 6);
									return true;
							}
							return false;//stage 5 end
						case 6://stage 6 
							switch (player.getDialogue().getChatId()) {
								case 1:
									if (player.getInventory().ownsItem(291)) {
										player.getDialogue().sendPlayerChat("Where do I need to go again?", CONTENT);
										player.getDialogue().setNextChatId(3);
									} else {
										player.getDialogue().sendPlayerChat("I lost the research notes...", CONTENT);
										player.getDialogue().setNextChatId(2);
									}
									return true;
								case 2:
									player.getDialogue().sendStatement("Aubury gives you his research notes.");
									player.getDialogue().endDialogue();
									player.getInventory().addItemOrDrop(new Item(291, 1));
									return true;
								case 3:
									player.getDialogue().sendNpcChat("Take my notes to the head wizard at", "the Wizards' Tower.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
							}
							return false;//stage 6 end
					}
				} else {
					switch (player.getDialogue().getChatId()) {
						case 1:
							player.getDialogue().sendNpcChat("What a nice day in videogames!", "Do you want to buy some runes?", CONTENT);
							return true;
						case 2:
							if (player.getSkill().getLevel()[Skill.RUNECRAFTING] == 99 && !checkTrim(player)) {
								player.getDialogue().sendOption("Yes please!", "No thanks.", "I want my skillcape!");
							} else if (player.getSkill().getLevel()[Skill.RUNECRAFTING] == 99 && checkTrim(player)) {
								player.getDialogue().sendOption("Yes please!", "No thanks.", "I want my skillcape!", "Can you trim my skillcape?");
							} else {
								player.getDialogue().sendOption("Yes please!", "No thanks.");
							}
							return true;
						case 3:
							switch (optionId) {
								case 1:
									player.getDialogue().sendPlayerChat("Yes please!", CONTENT);
									Shops.openShop(player, 553);
									player.getDialogue().dontCloseInterface();
									return true;
								case 2:
									player.getDialogue().sendPlayerChat("No thanks.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								case 3:
									player.getDialogue().sendPlayerChat("I want my skillcape!", ANGRY_1);
									player.getDialogue().setNextChatId(5);
									return true;
								case 4:
									player.getDialogue().sendPlayerChat("Can you trim my skillcape?", CONTENT);
									player.getDialogue().setNextChatId(8);
									return true;
							}
						case 5:
							player.getDialogue().sendNpcChat("You've earned it.", CONTENT);
							return true;
						case 6:
							ShopManager.openShop(player, 178);
							player.getDialogue().dontCloseInterface();
							return true;
						case 8:
							player.getDialogue().sendNpcChat("Sure, but this is irreversible!", "However, you can always buy another untrimmed cape.", CONTENT);
							return true;
						case 9:
							if (player.getInventory().playerHasItem(9765)) {
								player.getDialogue().sendNpcChat("Here you are.", CONTENT);
								trimCape(player, 9765);
							} else {
								player.getDialogue().sendPlayerChat("I guess I don't have the cape on me.", VERY_SAD);
								player.getDialogue().endDialogue();
							}
							return true;
					}
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
