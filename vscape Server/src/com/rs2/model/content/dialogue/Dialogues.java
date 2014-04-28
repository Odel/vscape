package com.rs2.model.content.dialogue;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.BankPin;
import com.rs2.model.content.Shops;
import com.rs2.model.content.dungeons.Abyss;
import com.rs2.model.content.minigames.duelarena.RulesData;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.randomevents.RandomEvent;
import com.rs2.model.content.randomevents.TalkToEvent;
import com.rs2.model.content.randomevents.InterfaceClicking.impl.SandwichLady;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.farming.Farmers;
import com.rs2.model.content.skills.magic.Teleportation;
import com.rs2.model.content.skills.slayer.Slayer;
import com.rs2.model.content.skills.slayer.Slayer.SlayerMasterData;
import com.rs2.model.content.skills.slayer.Slayer.SlayerTipsData;
import com.rs2.model.content.treasuretrails.ClueScroll;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcDefinition;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.players.BankManager;
import com.rs2.model.players.Player;
import com.rs2.model.players.ShopManager;
import com.rs2.model.players.item.Item;
import com.rs2.model.transport.JewelleryTeleport;
import com.rs2.model.transport.Sailing;
import com.rs2.model.transport.Sailing.ShipRoute;
import com.rs2.model.transport.Travel;
import com.rs2.model.transport.Travel.Route;
import com.rs2.model.content.skills.smithing.DragonShieldSmith;
import com.rs2.util.Misc;

import com.rs2.model.content.minigames.barrows.Barrows;

public class Dialogues {

	// Dialogue expressions
	public static final int HAPPY = 588, CALM = 589, CALM_CONTINUED = 590, CONTENT = 591, EVIL = 592, EVIL_CONTINUED = 593, DELIGHTED_EVIL = 594, ANNOYED = 595, DISTRESSED = 596, DISTRESSED_CONTINUED = 597, NEAR_TEARS = 598, SAD = 599, DISORIENTED_LEFT = 600, DISORIENTED_RIGHT = 601, UNINTERESTED = 602, SLEEPY = 603, PLAIN_EVIL = 604, LAUGHING = 605, LONGER_LAUGHING = 606, LONGER_LAUGHING_2 = 607, LAUGHING_2 = 608, EVIL_LAUGH_SHORT = 609, SLIGHTLY_SAD = 610, VERY_SAD = 611, OTHER = 612, NEAR_TEARS_2 = 613, ANGRY_1 = 614, ANGRY_2 = 615, ANGRY_3 = 616, ANGRY_4 = 617;

	public static boolean startDialogue(Player player, int id) {
		player.getDialogue().resetDialogue();
		return sendDialogue(player, id, 1, 0);
	}

	public static boolean sendDialogue(Player player, int id, int chatId, int optionId) {
		return sendDialogue(player, id, chatId, optionId, id);
	}

	public static void setNextDialogue(Player player, int id, int chatId) {
		player.getDialogue().setDialogueId(id);
		player.getDialogue().setNextChatId(chatId);
	}
	/** Instruction
	 * 
	 * Use same format as example of Man below
	 * 
	 * When you need to end a dialogue and remove interfaces on the spot, use a "break"
	 * otherwise always use "return true"
	 * 
	 * When you need to end a dialogue after you click continue,
	 * place player.getDialogue().endDialogue(); before the "return true"
	 *
	 * If you have a method that calls an interface,
	 * place player.getDialogue().dontCloseInterface(); before the "break"
	 */
	public static boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
		player.getDialogue().setChatId(chatId);
		player.getDialogue().setDialogueId(id);
		player.getDialogue().setLastNpcTalk(npcChatId);
		switch(id) {
			case 1 : //Man
			case 2 : //Man
			case 3 : //Man
			case 16 : //Man
			case 24 : //Man
			case 351 : //Man
			case 359 : //Man
			case 663 : //Man
			case 726 : //Man
			case 727 : //Man
			case 728 : //Man
			case 729 : //Man
			case 730 : //Man
			case 1024 : //Man
			case 1025 : //Man
			case 1026 : //Man
			case 1086 : //Man
			case 2675 : //Man
			case 3223 : //Man
			case 3224 : //Man
			case 3225 : //Man
			case 4 : //Woman
			case 5 : //Woman
			case 6 : //Woman
			case 25 : //Woman
			case 352 : //Woman
			case 353 : //Woman
			case 354 : //Woman
			case 360 : //Woman
			case 361 : //Woman
			case 362 : //Woman
			case 363 : //Woman
			case 1027 : //Woman
			case 1028 : //Woman
			case 1029 : //Woman
			case 2776 : //Woman
			case 3226 : //Woman
			case 3227 : //Woman
			case 1988 : //testing
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendPlayerChat("Hello, how's it going?", HAPPY);
						return true;
					case 2 :
						int random = Misc.random(12);
						if (random == 0) {
							player.getDialogue().sendNpcChat("How can I help you?", CONTENT);
						} else if (random == 1) {
							player.getDialogue().sendNpcChat("I'm fine, how are you?", HAPPY);
							player.getDialogue().setNextChatId(5);
						} else if (random == 2) {
							player.getDialogue().sendNpcChat("I'm busy right now.", CONTENT);
							player.getDialogue().endDialogue();
						} else if (random == 3) {
							player.getDialogue().sendNpcChat("No, I don't want to buy anything!", ANGRY_1);
							player.getDialogue().endDialogue();
						} else if (random == 4) {
							player.getDialogue().sendNpcChat("No I don't have any spare change.", ANNOYED);
							player.getDialogue().endDialogue();
						} else if (random == 5) {
							player.getDialogue().sendNpcChat("I'm very well thank you.", HAPPY);
							player.getDialogue().endDialogue();
						} else if (random == 6) {
							player.getDialogue().sendNpcChat("Hello there! Nice weather we've been having.", HAPPY);
							player.getDialogue().endDialogue();
						} else if (random == 7) {
							player.getDialogue().sendNpcChat("That is classified information.", CONTENT);
							player.getDialogue().endDialogue();
						} else if (random == 8) {
							player.getDialogue().sendNpcChat("Get out of my way, I'm in a hurry!", ANGRY_1);
							player.getDialogue().endDialogue();
						} else if (random == 9) {
							player.getDialogue().sendNpcChat("Hello.", HAPPY);
							player.getDialogue().endDialogue();
						} else if (random == 10) {
							player.getDialogue().sendNpcChat("Do I know you? I'm in a hurry!", HAPPY);
							player.getDialogue().endDialogue();
						} else if (random == 11) {
							player.getDialogue().sendNpcChat("I'm sorry I can't help you there.", ANNOYED);
							player.getDialogue().endDialogue();
						} else if (random == 12) {
							player.getDialogue().sendNpcChat("Not too bad thanks.", HAPPY);
							player.getDialogue().endDialogue();
						}
						return true;
					case 3 :
						player.getDialogue().sendOption("Do you wish to trade?", "I'm in search of a quest.", "I'm in search of enemies to kill.");
						return true;
					case 4 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendNpcChat("No, I have nothing I wish to get rid of.", "If you want to do some trading, there are", "plent of shops and market stalls around though.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 2 :
								player.getDialogue().sendNpcChat("I'm sorry I can't help you there.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 3 :
								player.getDialogue().sendNpcChat("I've heard there are many fearsome creatures", "that dwell under the ground...", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 5 :
						player.getDialogue().sendPlayerChat("Very well thank you.", HAPPY);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 543 : //Karim
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Would you like to buy a nice kebab? Only one gold.", CALM);
						return true;
					case 2 :
						player.getDialogue().sendOption("I think I'll give it a miss.", "Yes please.");
						return true;
					case 3 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("I think I'll give it a miss.", DISORIENTED_RIGHT);
								player.getDialogue().endDialogue();
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("Yes please.", CONTENT);
								return true;
						}
						break;
					case 4 :
						if (player.getInventory().playerHasItem(new Item(995))) {
							player.getInventory().removeItem(new Item(995));
							player.getInventory().addItemOrDrop(new Item(1971));
							player.getActionSender().sendMessage("You buy a kebab.");
							break;
						} else {
							player.getDialogue().sendPlayerChat("Oops, I forgot to bring any money with me.", SAD);
							player.getDialogue().endDialogue();
						}
						return true;
				}
				break;
			case 539 : //Silk trader
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Do you want to buy any fine silks?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("How much are they?", "No, silk doesn't suit me.");
						return true;
					case 3 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("How much are they?", CONTENT);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("No, silk doesn't suit me.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 4 :
						player.getDialogue().sendNpcChat("3gp.", CONTENT);
						return true;
					case 5 :
						player.getDialogue().sendOption("No, that's too much for me.", "Okay, that sounds good.");
						return true;
					case 6 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("No, that's too much for me.", CONTENT);
								return true;
							case 2 : //buy silk 3gp
								player.getDialogue().sendPlayerChat("Okay, that sounds good.", CONTENT);
								player.getDialogue().setNextChatId(12);
								return true;
						}
						break;
					case 7 :
						player.getDialogue().sendNpcChat("2gp and that's as low as I'll go.", CONTENT);
						return true;
					case 8 :
						player.getDialogue().sendNpcChat("I'm not selling it for any less. You'll only", "go and sell it in Varrock for a profit.", CONTENT);
						return true;
					case 9 :
						player.getDialogue().sendOption("2gp sounds good.", "No, really, I don't want it.");
						return true;
					case 10 :
						switch(optionId) {
							case 1 : //buy silk 2gp
								player.getDialogue().sendPlayerChat("2gp sounds good.", CONTENT);
								player.getDialogue().setNextChatId(14);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("No, really, I don't want it.", CONTENT);
								return true;
						}
						break;
					case 11 :
						player.getDialogue().sendNpcChat("Okay, but that's the best price your going to get.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					case 12 :
						if (player.getInventory().playerHasItem(new Item(995, 3))) {
							player.getInventory().removeItem(new Item(995, 3));
							player.getInventory().addItemOrDrop(new Item(950));
							player.getDialogue().sendGiveItemNpc("You buy some silk for 3gp.", new Item(950));
							player.getDialogue().endDialogue();
						} else {
							player.getDialogue().sendPlayerChat("Oh dear. I don't have enough money.", SAD);
						}
						return true;
					case 13 :
						player.getDialogue().sendNpcChat("Well, come back when you do have some money.", ANGRY_1);
						player.getDialogue().endDialogue();
						return true;
					case 14 :
						if (player.getInventory().playerHasItem(new Item(995, 2))) {
							player.getInventory().removeItem(new Item(995, 2));
							player.getInventory().addItemOrDrop(new Item(950));
							player.getDialogue().sendGiveItemNpc("You buy some silk for 2gp.", new Item(950));
							player.getDialogue().endDialogue();
						} else {
							player.getDialogue().sendPlayerChat("Oh dear. I don't have enough money.", SAD);
							player.getDialogue().setNextChatId(13);
						}
						return true;
				}	
				break;
			case 2238 : //Donie
				switch(player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendNpcChat("Hello there, can I help you?", HAPPY);
						int random = Misc.random(2);
						player.getDialogue().setNextChatId(random == 0 ? 2 : random == 1 ? 6 : 4);
						return true;
					case 2:
						player.getDialogue().sendOption("Where am I?", "How are you today?", "Are there any quests I can do here?", "Where can I get a haircut like yours?");
						return true;
					case 3:
						switch(optionId) {
							case 1:
								player.getDialogue().sendNpcChat("This is the town of Lumbridge my friend.", HAPPY);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Aye, not too bad thank you. Lovely weather", "in 06Scape this fine day.", HAPPY);
								player.getDialogue().setNextChatId(10);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("What kind of quest are you looking for?", ANNOYED);
								player.getDialogue().setNextChatId(20);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Yes, it does look like you need a hairdresser.", HAPPY);
								player.getDialogue().setNextChatId(15);
								return true;
						}
						break;
					case 4:
						player.getDialogue().sendOption("How are you today?", "Are there any quests I can do here?", "Your shoe lace is united.");
						return true;
					case 5:
						switch(optionId) {
							case 1:
								player.getDialogue().sendNpcChat("Aye, not too bad thank you. Lovely weather", "in 06Scape this fine day.", HAPPY);
								player.getDialogue().setNextChatId(10);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("What kind of quest are you looking for?", ANNOYED);
								player.getDialogue().setNextChatId(20);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("No it's not!", ANGRY_1);
								player.getDialogue().setNextChatId(18);
								return true;
						}
						break;
					case 6:
						player.getDialogue().sendOption("Do you have anything of value which I can have?", "Are there any quests I can do here?", "Can I buy your stick?");
						return true;
					case 7:
						switch(optionId) {
							case 1:
								player.getDialogue().sendNpcChat("Are you asking for free stuff?", ANNOYED);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("What kind of quest are you looking for?", ANNOYED);
								player.getDialogue().setNextChatId(20);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("It's not a stick! I'll have you know it's", "a very powerful staff!", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 8:
						player.getDialogue().sendPlayerChat("Well... er... yes.", ANNOYED);
						return true;
					case 9:
						player.getDialogue().sendNpcChat("No I dont not have anything I can give you.", "If I did have anything of value I wouldn't", "want to give it away.", ANGRY_1);
						player.getDialogue().endDialogue();
						return true;
					case 10:
						player.getDialogue().sendPlayerChat("Weather?", CALM);
						return true;
					case 11:
						player.getDialogue().sendNpcChat("Yes weather, you know.", HAPPY);
						return true;
					case 12:
						player.getDialogue().sendNpcChat("The state or condition of the atmosphere", "at a time and place, with respect to variables", "such as temperature, moisture, wind velocity,", "and barometric pressure.", ANNOYED);
						return true;
					case 13:
						player.getDialogue().sendPlayerChat("...", CALM);
						return true;
					case 14:
						player.getDialogue().sendNpcChat("Not just a pretty face eh? Ha ha ha.", LAUGHING);
						player.getDialogue().endDialogue();
						return true;
					case 15:
						player.getDialogue().sendPlayerChat("Oh thanks.", ANGRY_1);
						return true;
					case 16:
						player.getDialogue().sendNpcChat("No problem. The hairdresser in Falador", "will probably be able to sort you out.", LAUGHING);
						return true;
					case 17:
						player.getDialogue().sendNpcChat("The Lumbridge general store sells useful maps", "if you don't know the way.", LAUGHING);
						player.getDialogue().endDialogue();
						return true;
					case 18:
						player.getDialogue().sendPlayerChat("No you're right. I have nothing to back that up.", ANGRY_1);
						return true;
					case 19:
						player.getDialogue().sendNpcChat("Fool! Leave me alone!", ANGRY_1);
						player.getDialogue().endDialogue();
						return true;
					case 20:
						player.getDialogue().sendNpcChat("Sorry, quests have not been added yet.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 744 : //Klarense
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("What do you need I'm busy!", ANGRY_1);
						return true;
					case 2 :
						player.getDialogue().sendOption("I need to take a trip.", "I'm in search of bananas.", "Good bye.");
						return true;
					case 3 :
						switch(optionId) {
							case 1 :
									//player.getDialogue().sendNpcChat("Save our banana exports adventurer!", CONTENT);
									player.teleport(new Position(2715, 9162, 1));
									player.getDialogue().endDialogue();
									
							//	}
								return true;
							case 2 :
								player.getDialogue().sendNpcChat("The Jungle Demon has prevented us from collecting", "bananas. I'm offering a trip to the island though.", CONTENT);
								player.getDialogue().setNextChatId(2);
								return true;
						}
						break;
				}
			case 801 : //Abbot
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Hello brother, welcome to our monastery.", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Can you heal me? I'm injured.", "Can I climb up those stairs?", "Good bye.");
						return true;
					case 3 :
						switch(optionId) {
							case 1 :
								if (player.getSkill().getLevel()[Skill.HITPOINTS] < player.getSkill().getPlayerLevel(Skill.HITPOINTS)) {
									if (player.getInteractingEntity() != null) {
										player.getInteractingEntity().getUpdateFlags().sendAnimation(717);
									}
									player.getDialogue().sendNpcChat("Sure, here you go.", CONTENT);
									player.heal((int) (player.getSkill().getPlayerLevel(Skill.HITPOINTS) * 0.3));
									player.getUpdateFlags().sendGraphic(84);
									break;
								} else {
									player.getDialogue().sendNpcChat("You already have full hp.", CONTENT);
									player.getDialogue().endDialogue();
								}
								return true;
							case 2 :
								player.getDialogue().sendNpcChat("Up those stairs is the prayer guild. You need a", "level of 31 Prayer to enter. There you will find", "an altar that can boost your prayer by 2 points,", "as well as some monk robes.", CONTENT);
								player.getDialogue().setNextChatId(2);
								return true;
						}
						break;
				}
				break;
			case 9999 : //Alkharid Gate
				player.getDialogue().setLastNpcTalk(926);
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendPlayerChat("Can I come through this gate?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendNpcChat("You must pay a toll of 10 gold coins to pass.", CONTENT);
						if (!player.getInventory().playerHasItem(995, 10)) {
							player.getDialogue().setNextChatId(7);
						}
						return true;
					case 3 :
						player.getDialogue().sendOption("Ok, I'll pay.", "Who does the money go to?", "No thanks, I'll walk around.");
						return true;
					case 4 :
						switch(optionId) {
							case 1 :
								if (player.getInventory().playerHasItem(995, 10)) {
									player.getInventory().removeItem(new Item(995, 10));
									player.getActionSender().walkTo(player.getPosition().getX() < 3268 ? 1 : -1, 0, true);
									player.getActionSender().walkThroughDoubleDoor(2882, 2883, 3268, 3227, 3268, 3228, 0);
								}
								break;
							case 2 :
								player.getDialogue().sendNpcChat("The money goes to the city of Al-Kharid.", "Will you pay the toll?", CONTENT);
								return true;
							case 3 :
								player.getDialogue().sendNpcChat("As you wish. Don't go too near the scorpions.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 5 :
						player.getDialogue().sendOption("Ok, I'll pay.", "No thanks, I'll walk around.");
						return true;
					case 6 :
						switch(optionId) {
							case 1 :
								if (player.getInventory().playerHasItem(995, 10)) {
									player.getInventory().removeItem(new Item(995, 10));
									player.getActionSender().walkTo(player.getPosition().getX() < 3268 ? 1 : -1, 0, true);
									player.getActionSender().walkThroughDoubleDoor(2882, 2883, 3268, 3227, 3268, 3228, 0);
								}
								break;
							case 2 :
								player.getDialogue().sendNpcChat("As you wish. Don't go too near the scorpions.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 7 :
						player.getDialogue().sendOption("Who does the money go to?", "I haven't got that much.");
						return true;
					case 8 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendNpcChat("The money goes to the city of Al-Kharid.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
				}
				break;
			case 166 : //banker
			case 494 :
			case 495 :
			case 496 :
			case 2619 :
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("What can I do for you?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("I would like to access my bank account.", "I would like to edit my Bank Pin settings.", "Nothing.");
						return true;
					case 3 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("I would like to access my bank account.", CONTENT);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("I would like to edit my Bank Pin settings.", CONTENT);
								player.getDialogue().setNextChatId(6);
								return true;
							case 3 :
								player.getDialogue().sendPlayerChat("Nothing.", CONTENT);
								player.getDialogue().setNextChatId(5);
								return true;
						}
						break;
					case 4 :
						BankManager.openBank(player);
						player.getDialogue().dontCloseInterface();
						break;
					case 5 :
						player.getDialogue().sendNpcChat("Well, just let me know when I can help.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					case 6 :
						player.getDialogue().sendNpcChat("What would you like to do?", CONTENT);
						return true;
					case 7 :
						if (player.getBankPin().hasBankPin() && !player.getBankPin().hasPendingBankPinRequest()) {
							player.getDialogue().sendOption("I would like to change my bank pin.", "I would like to delete my bank pin.");
						} else if (player.getBankPin().hasBankPin() && player.getBankPin().hasPendingBankPinRequest()) {
							player.getDialogue().sendOption("I would like to delete my pending bank pin request.", "No, nevermind.");
							player.getDialogue().setNextChatId(22);
						} else {
							player.getDialogue().sendOption("I would like to set a bank pin.", "No, nevermind.");
							player.getDialogue().setNextChatId(28);
						}
						return true;
					case 8 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("I would like to change my bank pin.", CONTENT);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("I would like to delete my bank pin.", CONTENT);
								player.getDialogue().setNextChatId(18);
								return true;
						}
						break;
					case 9 :
						player.getDialogue().sendNpcChat("Please carefully select your bank pin.", CONTENT);
						return true;
					case 10 :
						player.getBankPin().startPinInterface(BankPin.PinInterfaceStatus.CHANGING);
						player.getDialogue().dontCloseInterface();
						break;
					case 11 :
						int[] pBP = player.getBankPin().getPendingBankPin();
						player.getDialogue().sendNpcChat("Your bank pin will be set to " + pBP[0] + " " + pBP[1] + " " + pBP[2] + " " + pBP[3] + ".", "Does that sound correct?", CONTENT);
						return true;
					case 12 :
						player.getDialogue().sendOption("Yes.", "No, may I try again?", "No, nevermind.");
						return true;
					case 13 :
						switch(optionId) {
							case 1 :
								player.getBankPin().setChangingBankPin();
								player.getDialogue().sendPlayerChat("Yes.", CONTENT);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("No, may I try again?", CONTENT);
								player.getDialogue().setNextChatId(16);
								return true;
							case 3 :
								player.getDialogue().sendPlayerChat("No, nevermind.", CONTENT);
								player.getDialogue().setNextChatId(17);
								return true;
						}
						break;
					case 14 :
						if (player.getBankPin().hasBankPin()) {
							player.getDialogue().sendNpcChat("Changes will take affect in 7 days.", "Return to me to edit or delete this change.", CONTENT);
						} else {
							player.getDialogue().sendNpcChat("Your bank pin will be set accordingly.", CONTENT);
						}
						player.getBankPin().checkBankPinChangeStatus();
						return true;
					case 15 :
						player.getDialogue().sendPlayerChat("Will do.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					case 16 :
						player.getDialogue().sendNpcChat("Sure.", CONTENT);
						player.getBankPin().clearPendingBankPinRequest();
						player.getDialogue().setNextChatId(10);
						return true;
					case 17 :
						player.getDialogue().sendNpcChat("Return to me if you change your mind.", CONTENT);
						player.getBankPin().clearPendingBankPinRequest();
						player.getDialogue().endDialogue();
						return true;
					case 18 :
						player.getDialogue().sendPlayerChat("I would like to delete my bank pin.", CONTENT);
						return true;
					case 19 :
						player.getDialogue().sendNpcChat("Are you sure you would like to delete your bank pin?", CONTENT);
						return true;
					case 20 :
						player.getDialogue().sendOption("Yes.", "No, nevermind.");
						return true;
					case 21 :
						switch(optionId) {
							case 1 :
								player.getBankPin().setDeletingBankPin();
								player.getDialogue().sendPlayerChat("Yes.", CONTENT);
								player.getDialogue().setNextChatId(14);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("No, nevermind.", CONTENT);
								player.getDialogue().setNextChatId(29);
								return true;
						}
						break;
					case 22 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("I would like to delete my pending bank pin request.", CONTENT);
								return true;
						}
						break;
					case 23 :
						player.getDialogue().sendNpcChat("Are you sure?", "This clears any deletion or change request.", CONTENT);
						return true;
					case 24 :
						player.getDialogue().sendOption("Yes.", "No, nevermind.");
						return true;
					case 25 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("Yes.", CONTENT);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("No, nevermind.", CONTENT);
								player.getDialogue().setNextChatId(29);
								return true;
						}
						break;
					case 26 :
						player.getBankPin().clearPendingBankPinRequest();
						player.getDialogue().sendNpcChat("Your pending bank pin request has been deleted.", CONTENT);
						return true;
					case 27 :
						player.getDialogue().sendPlayerChat("Thanks.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					case 28 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("I would like to set my bank pin.", CONTENT);
								player.getDialogue().setNextChatId(9);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("No, nevermind.", CONTENT);
								return true;
						}
						break;
					case 29 :
						player.getDialogue().sendNpcChat("Return to me if you change your mind.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 10001 : // barrows
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendStatement("You've found a hidden tunnel, do you want to enter?");
						return true;
					case 2 :
						player.getDialogue().sendOption("Yeah I'm fearless!", "No way, that looks scary!");
						return true;
					case 3 :
						switch(optionId) {
							case 1 :
								player.teleport(new Position(3551, 9693));
								break;
							case 2 :
								player.getDialogue().sendPlayerChat("No way, that looks scary!", DISTRESSED);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
				}
				break;
			case 960 : //duel arena doctor
			case 961 :
			case 962 :
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Hey, what's up? you can call me " + NpcDefinition.forId(player.getClickId()).getName(), "my mission here is to take care of those players", "who would need some help to cure their wounds", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendNpcChat("So, anything I can do for someone like you?", CONTENT);
						return true;
					case 3 :
						player.getDialogue().sendOption("Yes, I would like you to heal me please.", "No, I am just playing around.");
						return true;
					case 4 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Yes, I would like you to heal me please.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("No, I am just playing around.", CONTENT);
								player.getDialogue().setNextChatId(6);
								return true;
								
						}
					case 5 :
						player.getDuelMainData().healPlayer();
						break;
					case 6 :
						player.getDialogue().sendNpcChat("So don't waste my time, seriously...", ANGRY_1);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
				//RUNE MYSTERIES / DRAGON SLAYER / DUKE HORACIO
				case 741 :
					int dragonSlayerStage = 0;
					if(!QuestHandler.questCompleted(player, 5) && dragonSlayerStage == 0)
					{
						switch(player.getQuestStage(5))
						{
							case 0: // quest stage 0
								switch(player.getDialogue().getChatId()) {
									case 1:
										player.getDialogue().sendNpcChat("Greetings. Welcome to my castle.", CONTENT);
										return true;
									case 2:
										player.getDialogue().sendOption("Have you any quests for me?", "Can I buy an anti-fire shield?","Nevermind.");
										return true;
									case 3:
										switch(optionId) {
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
										switch(optionId) {
										case 1:
											if (!player.getInventory().playerHasItem(new Item(995, 1000))) {
												player.getDialogue().sendPlayerChat("Sorry, I don't have enough coins.", SAD);
											} else if (player.getInventory().getItemContainer().freeSlots() < 1 && player.getInventory().getItemContainer().getCount(995) != 1000) {
												player.getDialogue().sendNpcChat("Looks like you don't have enough inventory space.", CONTENT);
											} else {
												player.getDialogue().sendPlayerChat("Ok, here you go.", CONTENT);
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
										player.getDialogue().sendNpcChat("Well, it's not really a quest but i recently discovered","this strange talisman", CONTENT);
										return true;
									case 8:
										player.getDialogue().sendNpcChat("It seems to be mystical and i have never seen anything","like it before. Would take it to the head wizard at", CONTENT);
										return true;
									case 9:
										player.getDialogue().sendNpcChat("the Wizards' Tower for me? It's just south-west of here","and should not take you very long at all. I would be","awfully grateful", CONTENT);
										return true;
									case 10:
										player.getDialogue().sendOption("Sure, No problem.", "Not right now.");
										return true;
									case 11:
										switch(optionId) {
											case 1:
												player.getDialogue().sendPlayerChat("Sure, No problem.", CONTENT);
												player.getDialogue().setNextChatId(12);
												return true;
											case 2:
												player.getDialogue().sendPlayerChat("Not right now", CONTENT);
												player.getDialogue().endDialogue();
												return true;
										}
										break;
									case 12:
										player.getDialogue().sendNpcChat("Thank you very much, stranger. I am sure the head","wizard will reward you for such and interesting find.", CONTENT);
										return true;
									case 13:
										player.getDialogue().sendStatement("The Duke hands you an @blu@air talisman@bla@.");
										player.getDialogue().endDialogue();
										player.getInventory().addItemOrDrop(new Item(1438, 1));
										//set quest stage 1
										player.setQuestStage(5,1);
										QuestHandler.getQuests()[5].startQuest(player);
										return true;
								}
							break; // quest stage 0 end
							case 1: // quest stage 1
								switch(player.getDialogue().getChatId()) {
									case 1:
										player.getDialogue().sendNpcChat("Have you given the talisman to the head wizard?", CONTENT);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("No, Not yet.", CONTENT);
										return true;
									case 3:
										player.getDialogue().sendNpcChat("You can find the Head Wizard at the Wizards' Tower,","It's just south-west of here.", CONTENT);
										return true;
									case 4:
										player.getDialogue().sendPlayerChat("Ok thanks!", CONTENT);
										player.getDialogue().endDialogue();
										return true;
								}
							break; // quest stage 1 end
						}
					}
					else if(QuestHandler.questCompleted(player, 5) && dragonSlayerStage != 5)
					{
						switch(player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Greetings. Welcome to my castle.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendOption("Have you any quests for me?", "Can I buy an anti-fire shield?","Nevermind.");
								return true;
							case 3:
								switch(optionId) {
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
								switch(optionId) {
								case 1:
									if (!player.getInventory().playerHasItem(new Item(995, 1000))) {
										player.getDialogue().sendPlayerChat("Sorry, I don't have enough coins.", SAD);
									} else if (player.getInventory().getItemContainer().freeSlots() < 1 && player.getInventory().getItemContainer().getCount(995) != 1000) {
										player.getDialogue().sendNpcChat("Looks like you don't have enough inventory space.", CONTENT);
									} else {
										player.getDialogue().sendPlayerChat("Ok, here you go.", CONTENT);
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
								player.getDialogue().sendNpcChat("I don't have any more quests for you.", CONTENT);
								player.getDialogue().setNextChatId(2);
								return true;
						}
					}
					else
					{
						switch(player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Greetings. Welcome to my castle.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendOption("Have you any quests for me?", "Can I buy an anti-fire shield?","Nevermind.");
								return true;
							case 3:
								switch(optionId) {
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
								switch(optionId) {
								case 1:
									if (!player.getInventory().playerHasItem(new Item(995, 1000))) {
										player.getDialogue().sendPlayerChat("Sorry, I don't have enough coins.", SAD);
									} else if (player.getInventory().getItemContainer().freeSlots() < 1 && player.getInventory().getItemContainer().getCount(995) != 1000) {
										player.getDialogue().sendNpcChat("Looks like you don't have enough inventory space.", CONTENT);
									} else {
										player.getDialogue().sendPlayerChat("Ok, here you go.", CONTENT);
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
								player.getDialogue().sendNpcChat("I don't have any more quests for you.", CONTENT);
								player.getDialogue().setNextChatId(2);
								return true;
						}
					}
				break;
				case 300:
					if(!QuestHandler.questCompleted(player, 5)){
						switch(player.getQuestStage(5))
						{
							case 1: // quest stage 1
								switch(player.getDialogue().getChatId()) {
									case 1:
										player.getDialogue().sendNpcChat("Welcome adventurer, to the world renowned", "Wizards' Tower. How may I help you?", CONTENT);
									return true;
									case 2:
										player.getDialogue().sendOption("Nothing thanks, I'm just looking around.", "I'm looking for the head wizard.");
									return true;
									case 3:
										switch(optionId) {
											case 1:
												player.getDialogue().sendPlayerChat("Nothing thanks, I'm just looking around.", CONTENT);
												player.getDialogue().endDialogue();
												return true;
											case 2:
												player.getDialogue().sendPlayerChat("I'm looking for the head wizard.", CONTENT);
												player.getDialogue().setNextChatId(4);
												return true;
										}
									break;
									case 4:
										player.getDialogue().sendNpcChat("Oh, you are are you?", "And just why would you be doing that?", CONTENT);
									return true;
									case 5:
										player.getDialogue().sendPlayerChat("The Duke of Lumbridge sent me to find him. I have", "this weird talisman he found. He said the head wizard","would be very interested in it.", CONTENT);
									return true;
									case 6:
										player.getDialogue().sendNpcChat("Did he now? HmmmMMMMMmmmmm.", "Well that IS interesting. Hand it over then adventurer,","let me see what all the hubbub about it is.","Just some amulet I'll wager.", CONTENT);
									return true;
									case 7:
										player.getDialogue().sendOption("Ok, here you are.", "No, I'll only give it to the head wizard.");
									return true;
									case 8:
										switch(optionId) {
											case 1:
												player.getDialogue().sendPlayerChat("Ok, here you are.", CONTENT);
												player.getDialogue().setNextChatId(11);
												return true;
											case 2:
												player.getDialogue().sendPlayerChat("No, I'll only give it to the head wizard.", ANNOYED);
												player.getDialogue().setNextChatId(9);
												return true;
										}
									break;
									case 9:
										player.getDialogue().sendNpcChat("YOU ARE ONE CHEEKY CUNT MATE,","I SWEAR ON ME MUM.","I'M THE HEAD WIZARD!", ANGRY_1);
									return true;
									case 10:
										player.getDialogue().sendPlayerChat("Oh, o-ok..", CONTENT);
									return true;
									case 11:
										if(player.carryingItem(1438))
										{
											player.getDialogue().sendStatement("You hand the Talisman to the wizard.");
											player.getInventory().removeItem(new Item(1438, 1));
											player.setQuestStage(5,2);
											player.getDialogue().setNextChatId(1);
										}
										else
										{
											player.getDialogue().sendStatement("You don't have the Talisman.");
											player.getDialogue().endDialogue();
										}
									return true;
								}
							break; // quest stage 1 end
							case 2: // quest stage 2 start
								switch(player.getDialogue().getChatId()) {
									case 1:
										player.getDialogue().sendNpcChat("Wow! This is... incredible!", CONTENT);
									return true;
									case 2:
										player.getDialogue().sendNpcChat("Th-this talisman you brought me...! It is the last peice","of the puzzle. I think! Finally! The Legacy of our","ancestors... it will return to us once more!", CONTENT);
									return true;
									case 3:
										player.getDialogue().sendNpcChat("I need time to study this, "+player.getUsername()+". Can you please","do me this task while I study this talisman you have","brought me? In the mighty town of Varrock, which", CONTENT);
									return true;
									case 4:
										player.getDialogue().sendNpcChat("is located North East of here, there is a certain shop","that sells magical runes. I have in this package all of the","research I have done relating to the Rune Stones, and", CONTENT);
									return true;
									case 5:
										player.getDialogue().sendNpcChat("require somebody to take them to the skopkeeper so that","he may share my research and offer me his insights.","Do this thing for me, and bring back what he gives you,", CONTENT);
									return true;
									case 6:
										player.getDialogue().sendNpcChat("and if my suspiscions are correct, I will let you into the","knowledge of one of the greatest secrets this world has","ever known! A secret so powerful this is destroyed the", CONTENT);
									return true;
									case 7:
										player.getDialogue().sendNpcChat("original Wizards' Tower all of those centuries","ago! My research, combined with this mysterious","talisman... I cannot beleive the answer to","the mysteries is so close now!", CONTENT);
									return true;
									case 8:
										player.getDialogue().sendNpcChat("Do this thing for me "+player.getUsername()+". Be rewarded in a","way you can never imagine.", CONTENT);
									return true;
									case 9:
										player.getDialogue().sendOption("Yes, certainly.", "No, I'm busy.");
									return true;
									case 10:
										switch(optionId) {
											case 1:
												player.getDialogue().sendPlayerChat("Yes, certainly.", CONTENT);
												player.getDialogue().setNextChatId(11);
												return true;
											case 2:
												player.getDialogue().sendPlayerChat("No, I'm busy grinding you cheeky cunt.", ANGRY_1);
												player.getDialogue().endDialogue();
												return true;
										}
									break;
									case 11:
										player.getDialogue().sendNpcChat("Take this package and head directly North","from here, through Draynor village until you reach","the Barbarian Village. Then head East from there","until you reach Varrock.", CONTENT);
									return true;
									case 12:
										player.getDialogue().sendNpcChat("Once in Varrock take this package to the owner of the","rune shop. His name is Aubury. You may find it","helpful to ask one of Varrock's citizens for directions,", CONTENT);
									return true;
									case 13:
										player.getDialogue().sendNpcChat("as Varrock can be a confusing place for the first time","visitor. He will give you a special item - bring it back to","me and i shall show you the mystery of the runes...", CONTENT);
									return true;
									case 14:
										player.getDialogue().sendStatement("The head wizard gives you a package.");
										player.getDialogue().endDialogue();
										player.getInventory().addItemOrDrop(new Item(290, 1));
										player.setQuestStage(5,3);
									return true;
								}
							break;// quest stage 2 end
							case 3:// quest stage 3
								switch(player.getDialogue().getChatId()) {
									case 1:
										if(player.hasItem(290))
										{
											player.getDialogue().sendPlayerChat("Where do I need to go again?", CONTENT);
											player.getDialogue().setNextChatId(3);
										}
										else
										{
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
										player.getDialogue().sendNpcChat("Head directly North","from here, through Draynor village until you reach","the Barbarian Village. Then head East from there","until you reach Varrock.", CONTENT);
									return true;
									case 4:
										player.getDialogue().sendNpcChat("Once in Varrock take this package to the owner of the","rune shop. His name is Aubury. You may find it","helpful to ask one of Varrock's citizens for directions,", CONTENT);
									return true;
									case 5:
										player.getDialogue().sendPlayerChat("Ok! thanks!", CONTENT);
										player.getDialogue().endDialogue();
									return true;
								}
							break;// quest stage 3 end
							case 6://stage 6 
								switch(player.getDialogue().getChatId()) {
									case 1:
										player.getDialogue().sendNpcChat("Welcome adventurer, to the world renowned", "Wizards' Tower. How may I help you?", CONTENT);
									return true;
									case 2:
										player.getDialogue().sendNpcChat("Ah, "+player.getUsername()+". How goes your quest? Have you","delivered the research notes to my friend Aubury yet?", CONTENT);
									return true;
									case 3:
										player.getDialogue().sendPlayerChat("Yes, I have. He gave me some research notes","to pass on to you.", CONTENT);
									return true;
									case 4:
										player.getDialogue().sendNpcChat("May I have his notes then?", CONTENT);
									return true;
									case 5:
										if(player.carryingItem(291))
										{
											player.getDialogue().sendPlayerChat("Sure. I have them right here.", CONTENT);
											player.getDialogue().setNextChatId(6);
										}
										else
										{
											player.getDialogue().sendPlayerChat("Sorry, I don't have them right now.", CONTENT);
											player.getDialogue().endDialogue();
										}
									return true;
									case 6:
										player.getDialogue().sendNpcChat("Well, before you hand them over to me, as you","have been nothing but truthful with me to this point,","and I admire that in an adventurer. I will let you","into the secret of our research.", CONTENT);
									return true;
									case 7:
										player.getDialogue().sendNpcChat("Now as you may or may not know, many","centuries ago, the wizards at this Tower","learnt the secret of creating Rune Stones, which","allowed us to cast Magic very easily.", CONTENT);
									return true;
									case 8:
										player.getDialogue().sendNpcChat("When this tower was burnt down the secret of","creating runes was lost to us for all time... except it","wasn't. Some months ago, while searching these ruins","for information from the old days,", CONTENT);
									return true;
									case 9:
										player.getDialogue().sendNpcChat("I came upon a scroll almost destroyed that detailed a","magical rock deep in the icefields of the North closed","off from access by anything other than magical means.", CONTENT);
									return true;
									case 10:
										player.getDialogue().sendNpcChat("This rock was called the 'Rune Essence' by the","magicians who studied its powers. Apparently by simply","breaking a chunk from it a Rune Stone could be","fashioned very quickly and easily at certain", CONTENT);
									return true;
									case 11:
										player.getDialogue().sendNpcChat("elemental altars that were scattered across the land","back then. Now, this is an interesting little peice of","history, but not much use to us as modern wizards","without access to the Rune Essence", CONTENT);
									return true;
									case 12:
										player.getDialogue().sendNpcChat("or these elemental altars. This is where you and","Aubury come into this story. A few weeks back","Aubury discovered in a standard delivery of runes","to his store a parchment detailing a", CONTENT);
									return true;
									case 13:
										player.getDialogue().sendNpcChat("teleportation spell that he had never come across","before. To his shock when cast it took him to a","stange rock he had never encountered before...","yet that felt strangely familiar...", CONTENT);
									return true;
									case 14:
										player.getDialogue().sendNpcChat("As I'm sure you have now guessed he had discovered a","portal leading to the mythical Rune Essence. As soon as","he told me of this spell I saw the importance of his find", CONTENT);
									return true;
									case 15:
										player.getDialogue().sendNpcChat("for if we could but find the elemental altars spoken","of in the ancient text we would once more be able","to create runes as our ancestors had done! It would","be the saviour of the wizards' art!", CONTENT);
									return true;
									case 16:
										player.getDialogue().sendPlayerChat("I'm still not sure how I fit into", "this little story of yours...", CONTENT);
									return true;
									case 17:
										player.getDialogue().sendNpcChat("You haven't guessed? This talisman you brought me...","it is the key to the elemental altar of air! When","you hold it next it will direct you towards", CONTENT);
									return true;
									case 18:
										player.getDialogue().sendNpcChat("the entrance to the long forgotten Air Altar! By","bring peices of the Rune Essence to the Air Temple,","you will be able to fashion your own Air Runes!", CONTENT);
									return true;
									case 19:
										player.getDialogue().sendNpcChat("And this is not all! By finding other talismans similar","to this one you will eventually be able to craft every","rune that is available on this world! Just", CONTENT);
									return true;
									case 20:
										player.getDialogue().sendNpcChat("as our ancestors did! I cannot stress enough what a","find this is! Now, due to the risks involved of letting","this mighty power fall into the wrong hands", CONTENT);
									return true;
									case 21:
										player.getDialogue().sendNpcChat("I will keep the teleport skill to the Rune Essence","a closely guarded secret shared only by myself","and those Magic users around the world","whom I trust enough to keep it.", CONTENT);
									return true;
									case 22:
										player.getDialogue().sendNpcChat("This means that if any evil power should discover","the talismans required to enter the elemental","temples we will be able to prevent their access","to the Rune Essence and prevent", CONTENT);
									return true;
									case 23:
										player.getDialogue().sendNpcChat("tragedy befalling this world. I know not where the","temples are located nor do I know where the talismans","have been scattered to in this land, but I now", CONTENT);
									return true;
									case 24:
										player.getDialogue().sendNpcChat("return your Air Talisman to you. Find the Air","Temple and you will be able to charge your Rune","Essence to become Air Runes at will. Any time", CONTENT);
									return true;
									case 25:
										player.getDialogue().sendNpcChat("you wish to visit the Rune Essence speak to me","or Aubury and we will open a portal to that","mystical place for you to visit.", CONTENT);
									return true;
									case 26:
										player.getDialogue().sendPlayerChat("So only you and Aubury know the teleport","spell to the Rune Essence?", CONTENT);
									return true;
									case 27:
										player.getDialogue().sendNpcChat("No... there are others... whom i will tell of your","authorisation to visit that place. When you speak","to them they will know you and grant you","access to that place when asked.", CONTENT);
									return true;
									case 28:
										player.getDialogue().sendNpcChat("Use the Air Talisman to locate the air temple,","and use any further talismans you find to locate","the other missing elemental temples.","Now... my research notes please?", CONTENT);
									return true;
									case 29:
										player.getDialogue().sendStatement("You hand the head wizard the research notes.","He hands you back the Air Talisman.");
										player.getInventory().removeItem(new Item(291, 1));
										player.getInventory().addItemOrDrop(new Item(1438, 1));
										player.getDialogue().endDialogue();
										player.setQuestStage(5, 7);
										QuestHandler.completeQuest(player,5);
									return true;
								}
							break;// quest stage 6 end
						}
					}
				break;
				case 553: // AUBURY
					if(!QuestHandler.questCompleted(player, 5)){
						switch(player.getQuestStage(5))
						{
							case 0:
							case 1:
							case 2:
								switch(player.getDialogue().getChatId()) 
								{
									case 1:
										player.getDialogue().sendNpcChat("Do you want to buy some runes?", CONTENT);
									return true;
									case 2:
										player.getDialogue().sendOption("Yes please!", "No thanks.");
									return true;
									case 3:
										switch(optionId) {
											case 1:
												player.getDialogue().sendPlayerChat("Yes please!", CONTENT);
												Shops.openShop(player, 553);
												player.getDialogue().dontCloseInterface();
												return true;
											case 2:
												player.getDialogue().sendPlayerChat("No thanks.", CONTENT);
												player.getDialogue().endDialogue();
												return true;
										}
									break;
								}
								break;
							case 3: //stage 3
								switch(player.getDialogue().getChatId()) 
								{
									case 1:
										player.getDialogue().sendNpcChat("Do you want to buy some runes?", CONTENT);
									return true;
									case 2:
										player.getDialogue().sendOption("Yes please!", "No thanks.","I have been sent here with a package for you.");
									return true;
									case 3:
										switch(optionId) {
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
												player.getDialogue().sendPlayerChat("I have been sent here with a package for you. It's from","the head wizard at the Wizards' Tower.", CONTENT);
												player.getDialogue().setNextChatId(4);
												return true;
										}
									break;
									case 4:
										player.getDialogue().sendNpcChat("Really? But... surely he can't have..? Please let me","have it, it must be extremely important for him to have","sent a stranger.", CONTENT);
									return true;
									case 5:
										if(player.hasItem(290))
										{
											player.getDialogue().sendStatement("You hand Aubury the research package.");
											player.getInventory().removeItem(new Item(290, 1));
											player.setQuestStage(5, 4);
											player.getDialogue().setNextChatId(1);
										}
										else
										{
											player.getDialogue().sendStatement("You don't have the package.");
											player.getDialogue().endDialogue();
										}
									return true;
								}
							break; //stage 3 end
							case 4: //stage 4
								switch(player.getDialogue().getChatId()) 
								{
									case 1:
										player.getDialogue().sendNpcChat("This... this is incredible. Please give me a few moments","to quickly look over this and then talk to me again.", CONTENT);
										player.getDialogue().endDialogue();
										player.setQuestStage(5, 5);
									return true;
								}
							break;//stage 4 end
							case 5: //stage 5
								switch(player.getDialogue().getChatId()) 
								{
									case 1:
										player.getDialogue().sendNpcChat("My gratitude to you adventurer for bringing me these","research notes. I notice that you brought the head","wizard a special talisman that was the key to our finally","unlocking the puzzle.", CONTENT);
									return true;
									case 2:
										player.getDialogue().sendNpcChat("Combined with the information i had already collated","regarding the Rune Essence. I think we have finally","unlocked the power to", CONTENT);
									return true;
									case 3:
										player.getDialogue().sendNpcChat("...no. I am getting ahead of myself. Please take this","summary of my research back to the head wizard at","the Wizards' Tower. I Trust his judgement on wether","to let you in on our little secret or not.", CONTENT);
									return true;
									case 4:
										player.getDialogue().sendStatement("Aubury gives you his research notes.");
										player.getDialogue().endDialogue();
										player.getInventory().addItemOrDrop(new Item(291, 1));
										player.setQuestStage(5,6);
									return true;
								}
							break;//stage 5 end
							case 6://stage 6 
								switch(player.getDialogue().getChatId()) 
								{
									case 1:
										if(player.hasItem(291))
										{
											player.getDialogue().sendPlayerChat("Where do I need to go again?", CONTENT);
											player.getDialogue().setNextChatId(3);
										}
										else
										{
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
										player.getDialogue().sendNpcChat("Take my notes to the head wizard at","the Wizards' Tower.", CONTENT);
										player.getDialogue().endDialogue();
									return true;
								}
							break;//stage 6 end
						}
					}
					else
					{
						switch(player.getDialogue().getChatId()) 
						{
							case 1:
								player.getDialogue().sendNpcChat("Do you want to buy some runes?", CONTENT);
							return true;
							case 2:
								player.getDialogue().sendOption("Yes please!", "No thanks.");
							return true;
							case 3:
								switch(optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("Yes please!", CONTENT);
										Shops.openShop(player, 553);
										player.getDialogue().dontCloseInterface();
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("No thanks.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
								}
							break;
						}
					}
				break;
				//RUNE MYSTERIES
			case 656 : // entrana dungeon
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("You sure you want to go down there?", "The only exit is out into the wilderness.", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("I'm not afraid, I'm going in.", "Nevermind.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								Ladders.climbLadder(player, new Position(2829, 9772, 0));
								player.getSkill().setSkillLevel(Skill.PRAYER, 1);
								player.getSkill().refresh(Skill.PRAYER);
								player.getActionSender().sendMessage("You feel your prayer weaken.");
								break;
							case 2:
								player.getDialogue().sendPlayerChat("Nevermind.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
				}
				break;
			case 2728 : //port sarim monk
			case 2729 :
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Would you like to sail to Entrana?", "I will need to check you for dangerous equipment.", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes please.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								Sailing.sailShip(player, ShipRoute.PORT_SARIM_TO_ENTRANA);
								player.getDialogue().dontCloseInterface();
								break;
							case 2:
								player.getDialogue().sendPlayerChat("No thanks.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
				}
				break;
			case 657 : // entrana monk
				switch(player.getDialogue().getChatId()) {
				case 1 :
					player.getDialogue().sendNpcChat("Would you like to sail back to Port Sarim?", CONTENT);
					return true;
				case 2 :
					player.getDialogue().sendOption("Yes please.", "No thanks.");
					return true;
				case 3 :
					switch(optionId) {
						case 1:
							Sailing.sailShip(player, ShipRoute.ENTRANA_TO_PORT_SARIM);
							player.getDialogue().dontCloseInterface();
							break;
						case 2:
							player.getDialogue().sendPlayerChat("No thanks.", CONTENT);
							player.getDialogue().endDialogue();
							return true;
					}
					break;
				case 4 :
					player.getDialogue().sendNpcChat("How dare you try to take dangerous equipment?", "Come back when you have left it all behind.", ANGRY_1);
					player.getDialogue().endDialogue();
					return true;
			}
			break;
			case 10002 : //games necklace
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendOption("Burthorpe", "Nowhere");
						return true;
					case 2 :
						switch(optionId) {
							case 1:
								JewelleryTeleport.teleport(player, Teleportation.GAMES_ROOM);
								break;
						}
						break;
				}
				break;
			case 10003 : //glory
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendOption("Edgeville", "Karamja", "Draynor Village", "Al Kharid", "Nowhere");
						return true;
					case 2 :
						switch(optionId) {
						case 1:
							JewelleryTeleport.teleport(player, Teleportation.EDGEVILLE);
							break;
						case 2:
							JewelleryTeleport.teleport(player, Teleportation.KARAMJA);
							break;
						case 3:
							JewelleryTeleport.teleport(player, Teleportation.DRAYNOR_VILLAGE);
							break;
						case 4:
							JewelleryTeleport.teleport(player, Teleportation.AL_KHARID);
							break;
						}
						break;
				}
				break;
			case 10014 : //Combat bracelet
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendOption("Burthorpe", "Champions' Guild", "Monastery", "Ranging Guild", "Nowhere");
						return true;
					case 2 :
						switch(optionId) {
						case 1:
							JewelleryTeleport.teleport(player, Teleportation.BURTHORPE);
							break;
						case 2:
							JewelleryTeleport.teleport(player, Teleportation.CHAMPGUILD);
							break;
						case 3:
							JewelleryTeleport.teleport(player, Teleportation.MONASTERY);
							break;
						case 4:
							JewelleryTeleport.teleport(player, Teleportation.RANGEGUILD);
							break;
						}
						break;
				}
				break;
			case 10004 : //ring of duel
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendOption("Duel Arena", "Castle Wars", "Nowhere");
						return true;
					case 2 :
						switch(optionId) {
						case 1:
							JewelleryTeleport.teleport(player, Teleportation.DUEL_ARENA);
							break;
						case 2:
							JewelleryTeleport.teleport(player, Teleportation.CASTLE_WARS);
							break;
						}
						break;
				}
				break;
			case 4593 : //gnome pilot
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Would you like to fly somewhere on the glider?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Sure.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								player.getActionSender().sendInterface(802);
								player.getDialogue().dontCloseInterface();
								break;
						}
						break;
				}
				break;
			case 510 : //hajedy
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Would you like to travel to Shilo village?", "It will only cost you 10gp.", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes please.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								Travel.startTravel(player, Route.BRIMHAVEN_TO_SHILO);
								player.getDialogue().dontCloseInterface();
								break;
						}
						break;
				}
				break;
			case 511 : // Vigroy
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Would you like to travel to Brimhaven?", "It will cost 10gp.", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes please.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
						case 1:
							Travel.startTravel(player, Route.SHILO_TO_BRIMHAVEN);
							player.getDialogue().dontCloseInterface();
							break;
						}
						break;
				}
				break;
			case 0 : //Hans
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Hello, what are you doing here?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("I'm looking for whoever is in charge of this place.", "I have come to kill everyone in this castle!", "I don't know. I'm lost. Where am I?");
						return true;
					case 3 :
						switch(optionId) {
						case 1:
							player.getDialogue().sendNpcChat("The person in charge here is Duke Horacio.", "You can usually find him upstairs in his castle.", CONTENT);
							player.getDialogue().endDialogue();
							return true;
						case 2:
							player.getDialogue().sendPlayerChat("I have come to kill everyone in this castle!", ANGRY_1);
							return true;
						case 3:
							player.getDialogue().sendNpcChat("You are at the Lumbridge Castle.", CONTENT);
							player.getDialogue().endDialogue();
							return true;
						}
						break;
					case 4 :
						final Npc npc = World.getNpcs()[player.getNpcClickIndex()];
						npc.getUpdateFlags().sendForceMessage("Help! Help!");
						npc.getFollowing().stepAway();
						break;
				}
				break;
			case 484 : //Gnome to get through gate
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Our invisible gate is broken at the moment.", "Would you like to get through?.", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes please.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								player.teleport(new Position(2461, 3381, 0));
								player.getDialogue().dontCloseInterface();
								break;
						}
						break;
				}
				break;
			case 485 : //Gnome to get through gate
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Our invisible gate is broken at the moment.", "Would you like to get through?.", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes please.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								player.teleport(new Position(2461, 3388, 0));
								player.getDialogue().dontCloseInterface();
								break;
						}
						break;
				}
				break;
			case 10005 : //iron ladder
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendOption("Climb up the ladder.", "Climb down the ladder.", "Never mind");
						return true;
					case 2 :
						switch(optionId) {
							case 1:
								Ladders.climbLadder(player, new Position(2544, 3741, 0));
								break;
							case 2:
								Ladders.climbLadder(player, new Position(1798, 4407, 3));
								break;
						}
						break;
				}
				break;
			case 1304 : //Rellekka Sailor
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Would you like to sail to Miscellania?", "Free for m'lord.", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes please.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								player.teleport(new Position(2579, 3845, 0));
								player.getDialogue().dontCloseInterface();
								break;
						}
						break;
				}
				break;
			case 1385 : //Miscellania Sailor
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Would you like to sail back to Rellekka?", "Free for m'lord.", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes please.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								player.teleport(new Position(2629, 3693, 0));
								player.getDialogue().dontCloseInterface();
								break;
						}
						break;
				}
				break;
			case 2437 : //jarvald
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Would you like to sail to Waterbirth Island?", "It will only cost you 1000 coins.", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("YES", "NO");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								Sailing.sailShip(player, ShipRoute.RELLEKKA_TO_WATERBIRTH);
								player.getDialogue().dontCloseInterface();
								break;
						}
						break;
					case 4 :
						player.getDialogue().sendNpcChat("So do you have the 1000 coins for my service,", "and are you ready to leave now?", CONTENT);
						player.getDialogue().setNextChatId(3);
						break;
				}
				break;
			case 2436 : // jarvald travel back
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Would you like to sail back to Rellekka?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("YES", "NO");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								Sailing.sailShip(player, ShipRoute.WATERBIRTH_TO_RELLEKKA);
								player.getDialogue().dontCloseInterface();
								break;
						}
						break;
				}
				break;
			case 802 : //jered
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Hello brother, would you like me to bless all", "of your unblessed symbols?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes please.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								if (!player.getInventory().playerHasItem(1716)) {
									player.getDialogue().sendNpcChat("It look's like you dont have any symbols", "in your inventory. Come back when you do.", CONTENT);
								} else {
									for (Item item : player.getInventory().getItemContainer().getItems()) {
										if (item != null && item.getId() == 1716) {
											player.getInventory().replaceItemWithItem(new Item(1716), new Item(1718));
										}
									}
									player.getDialogue().sendNpcChat("There you go, your welcome.", CONTENT);
								}
								player.getDialogue().endDialogue();
								return true;
						}
						break;
				}
				break;
			case 10006 : //ladder
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendOption("Climb up the ladder.", "Climb down the ladder.", "Never mind");
						return true;
					case 2 :
						switch(optionId) {
							case 1:
								Ladders.checkClimbLadder(player, "up");
								break;
							case 2:
								Ladders.checkClimbLadder(player, "down");
								break;
						}
						break;
				}
				break;
			case 10007 : //staircase
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendOption("Go up the stairs.", "Go down the stairs.", "Never mind.");
						return true;
					case 2 :
						switch(optionId) {
							case 1:
								Ladders.checkClimbTallStaircase(player, "up");
								break;
							case 2:
								Ladders.checkClimbTallStaircase(player, "down");
								break;
						}
						break;
				}
				break;
			case 222 : //monk
				switch(player.getDialogue().getChatId()) {
				case 1 :
					player.getDialogue().sendNpcChat("Greetings traveller.", CONTENT);
					return true;
				case 2 :
					player.getDialogue().sendOption("Can you heal me? I'm injured.", "Nevermind.");
					return true;
				case 3 :
					switch(optionId) {
					case 1:
						if (player.getSkill().getLevel()[Skill.HITPOINTS] < player.getSkill().getPlayerLevel(Skill.HITPOINTS)) {
							if (player.getInteractingEntity() != null && !player.getInteractingEntity().isDead()) {
								player.getInteractingEntity().getUpdateFlags().sendAnimation(717);
								player.getDialogue().sendNpcChat("Sure, here you go.", CONTENT);
								player.heal((int) (player.getSkill().getPlayerLevel(Skill.HITPOINTS) * 0.3));
								player.getUpdateFlags().sendGraphic(84);
							} else {
								break;
							}
						} else {
							player.getDialogue().sendNpcChat("You already have full hp.", CONTENT);
						}
						player.getDialogue().endDialogue();
						return true;
					}
					break;
				}
				break;
			case 2258 : //mage of zammy
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("How can I help a fellow Zamorak follower?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Can you teleport me to the abyss?", "Can I see your shop?", "I'm not a Zamorak follower!");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Can you teleport me to the abyss?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Can I see your shop?", CONTENT);
								player.getDialogue().setNextChatId(5);
								return true;
							case 3 :
								player.getDialogue().sendPlayerChat("I'm not a Zamorak follower!", CONTENT);
								player.getDialogue().setNextChatId(6);
								return true;
						}
						break;
					case 4 :
						final Npc npc = World.getNpcs()[player.getNpcClickIndex()];
						Abyss.teleportToAbyss(player, npc);
						break;
					case 5 :
						ShopManager.openShop(player, 10);
						player.getDialogue().dontCloseInterface();
						break;
					case 6 :
						player.getDialogue().sendNpcChat("Then get out of my sight!", ANGRY_1);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 376 : //port sarim sailors
			case 377 :
			case 378 :
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Would you like to sail to Karamja?", "It will only cost you 30gp.", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes please.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								Sailing.sailShip(player, ShipRoute.PORT_SARIM_TO_KARAMJA);
								player.getDialogue().dontCloseInterface();
								break;
						}
						break;
				}
				break;
			case 380 : //karamja sailor
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Would you like to sail back to Port Sarim?", "It will cost 30gp.", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes please.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								Sailing.sailShip(player, ShipRoute.KARAMJA_TO_PORT_SARIM);
								player.getDialogue().dontCloseInterface();
								break;
						}
						break;
				}
				break;
			case 3117 : //sandwich lady
				SandwichLady lady = new SandwichLady(true);
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("You look hungry to me. I tell you what - ", "have a " + lady.stringSent()[0] + " on me.", HAPPY);
						player.getRandomInterfaceClick().randomNumber = lady.getRandomNumber();
						return true;
					case 2 :
						player.getRandomInterfaceClick().openInterface(3117);
						//player.getRandomInterfaceClick().sendModelsRotation(3117);
						player.getDialogue().dontCloseInterface();
						break;
				}
				break;
			case 1595 : //saniboch
				switch(player.getDialogue().getChatId()) {
					case 1 :
						if (player.isBrimhavenDungeonOpen()) {
							player.getDialogue().sendNpcChat("You have already paid the entrance fee.", "You may enter the dungeon whenever you wish.", CONTENT);
							player.getDialogue().endDialogue();
							return true;
						}
						player.getDialogue().sendNpcChat("Hey, there is a 875 coin fee if you wish", "to enter this dungeon.", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Ok, here's 875 coins.", "Nevermind.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								if (player.isBrimhavenDungeonOpen()) {
									player.getDialogue().sendNpcChat("You have already paid the entrance fee.", "You may enter the dungeon whenever you wish.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								}
								player.getDialogue().sendPlayerChat("Ok, here's 875 coins", CONTENT);
								return true;
						}
						break;
					case 4 :
						if (player.getInventory().removeItem(new Item(995, 875))) {
							player.getDialogue().sendStatement("You give Saniboch 875 coins");
							player.setBrimhavenDungeonOpen(true);
						} else {
							player.getDialogue().sendPlayerChat("Looks like I don't have enough coins.", SAD);
							player.getDialogue().endDialogue();
						}
						return true;
					case 5 :
						player.getDialogue().sendNpcChat("Many thanks. You may now pass the door.", "May your death be a glorious one!", CONTENT);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 10008 : //shop keeper
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Hello "+player.getUsername()+",", "would you like to see my shop?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes please.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								if (player.getInteractingEntity() != null && player.getInteractingEntity().isNpc())
									Shops.openShop(player, ((Npc) player.getInteractingEntity()).getNpcId());
								player.getDialogue().dontCloseInterface();
								break;
						}
						break;
				}
				break;
			case 513 : // yohnus
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendPlayerChat("Can I come through this door?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendNpcChat("Sure, you can enter for a small fee of 20 coins.", CONTENT);
						return true;
					case 3 :
						player.getDialogue().sendOption("Ok, I'll pay.", "No thanks.");
						return true;
					case 4 :
						switch(optionId) {
							case 1:
								if (player.getInventory().playerHasItem(995, 20)) {
									player.getInventory().removeItem(new Item(995, 20));
									player.getActionSender().walkTo(0, player.getPosition().getY() == 2963 ? 1 : 2, true);
									player.getActionSender().walkThroughDoor(2266, 2856, 2963, 0);
									break;
								} else {
									player.getDialogue().sendPlayerChat("Sorry, I don't have that many coins.", SAD);
								}
								player.getDialogue().endDialogue();
								return true;
							case 3 :
								player.getDialogue().sendPlayerChat("No thanks.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
				}
				break;
			case 798 : //velrak
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Thank you for rescuing me! It isn't very comfy", "in this cell!", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("So... do you know anywhere good to explore?", "Do I get a reward?");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("So... do you know anywhere good to explore?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Do I get a reward?", CONTENT);
								player.getDialogue().setNextChatId(10);
								return true;
						}
						break;
					case 4 :
						player.getDialogue().sendNpcChat("Well, this dungeon was quite good to explore ...until I", "got captured, anyway. I was given a key to an inner", "part of this dungeon by a mysterious cloaked", "stranger!", CONTENT);
						return true;
					case 5 :
						player.getDialogue().sendNpcChat("It's rather tough for me to get that far into the", "dungeon however... I just keep getting", "captured! Would you like to give it a go?", CONTENT);
						return true;
					case 6 :
						player.getDialogue().sendOption("Yes please!", "No, it's too dangerous for me too.");
						return true;
					case 7 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Yes please!", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("No, it's too dangerous for me too.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 8 :
						player.getDialogue().sendStatement("Velrak reaches somewhere mysterious and passes you a key.");
						return true;
					case 9 :
						if (player.getInventory().getItemContainer().freeSlots() < 1) {
							player.getDialogue().sendNpcChat("Looks like you don't have enough room", "in your inventory.", CONTENT);
						} else if (player.hasItem(1590)) {
							player.getDialogue().sendNpcChat("I already gave you the key!", CONTENT);
						} else {
							player.getInventory().addItem(new Item(1590));
							break;
						}
						player.getDialogue().endDialogue();
						return true;
					case 10 :
						player.getDialogue().sendNpcChat("I don't have anything expensive to give, sorry.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 10009 : //treasure trials
				switch(player.getDialogue().getChatId()) {
					case 1 :
						ClueScroll.itemReward(player, player.clueLevel);
						player.getDialogue().dontCloseInterface();
						break;
					case 2 :
						ClueScroll.clueReward(player, player.clueLevel, "You recieve another clue!", true, "Here is your reward");
						return true;
					case 3 :
						player.getActionSender().openXInterface(207);
						player.getDialogue().dontCloseInterface();
						break;
				}
				break;
			case 2824:
			case 804:
			case 1041: //tanner
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Greetings friend. I am a manufacturer of leather.", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Can I buy some leather then?", "Leather is rather weak stuff.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Can I buy some leather then?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Leather is rather weak stuff.", CONTENT);
								player.getDialogue().setNextChatId(5);
								return true;
						}
						break;
					case 4 :
						player.getDialogue().sendNpcChat("I make leather from animal hides. Bring me some", "cowhides and one gold per hide, and I'll tan them", "into soft leather for you.", HAPPY);
						player.getDialogue().endDialogue();
						return true;
					case 5 :
						player.getDialogue().sendNpcChat("Normal leather may be quite weak, but it's", "very cheap - I make it from cowhides for only 1 gp", "per hide - and it's so easy to craft that anyone", "can work with it.", HAPPY);
						return true;
					case 6 :
						player.getDialogue().sendNpcChat("Alternatively you could try hard leather. It's", "not so easy to craft, but I only charge 3 gp", "per cowhide to prepare it, and it makes much", "sturdier armour.", HAPPY);
						return true;
					case 7 :
						player.getDialogue().sendNpcChat("I can also tan snake hides and dragonhides,", "suitable for crafting into the highest quality", "armour for rangers.", HAPPY);
						return true;
					case 8 :
						player.getDialogue().sendPlayerChat("Thanks, I'll bear it in mind.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 10011 : //spirit tree
				player.getDialogue().setLastNpcTalk(3637);
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Hello " + player.getUsername(), "Would you like a free trip somewhere?", -1);
						return true;
					case 2 :
						player.getDialogue().sendOption("The Tree Gnome Village", "The Gnome Stronghold", "Nowhere");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendStatement("You place your hands on the dry tough bark of the spirit tree, and", "feel a surge of energy run through your veins.");
								return true;
							case 2:
								player.getDialogue().sendStatement("You place your hands on the dry tough bark of the spirit tree, and", "feel a surge of energy run through your veins.");
								player.getDialogue().setNextChatId(5);
								return true;
						}
						break;
					case 4 :
						Ladders.climbLadder(player, new Position(2542, 3169, 0));
						break;
					case 5 :
						Ladders.climbLadder(player, new Position(2462, 3444, 0));
						break;
				}
				break;
			case 3636 : //spirit tree
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Hello " + player.getUsername() + ", First of all -", "Thank you for giving me life !", "Let me thank you by offering you a trip", -1);
						return true;
					case 2 :
						player.getDialogue().sendOption("The Tree Gnome Village", "The Gnome Stronghold", "Nowhere");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendStatement("You place your hands on the dry tough bark of the spirit tree, and", "feel a surge of energy run through your veins.");
								return true;
							case 2:
								player.getDialogue().sendStatement("You place your hands on the dry tough bark of the spirit tree, and", "feel a surge of energy run through your veins.");
								player.getDialogue().setNextChatId(5);
								return true;
						}
						break;
					case 4 :
						Ladders.climbLadder(player, new Position(2542, 3169, 0));
						break;
					case 5 :
						Ladders.climbLadder(player, new Position(2462, 3444, 0));
						break;
				}
				break;
			case 2324 : //farmer
			case 2323 :
			case 2326 :
			case 2325 :
			case 2337 :
			case 2336 :
			case 2335 :
			case 2338 :
			case 2334 :
			case 2332 :
			case 2333 :
			case 2327 :
			case 2343 :
			case 2331 :
			case 2344 :
			case 2330 :
			case 2340 :
			case 2339 :
			case 2341 :
			case 2342 :
				if (!Constants.AGILITY_ENABLED) {
					player.getActionSender().sendMessage("This skill is currently disabled.");
					break;
				}
				Farmers.FarmersData farmersData = Farmers.FarmersData.forId(player.getClickId());
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Hey, I am one of the master farmers of this world", "but you can call me " + NpcDefinition.forId(player.getClickId()).getName(), "So, what do you need from me ?", CONTENT);
						if (farmersData.getFieldProtected() != "tree")
							player.getDialogue().setNextChatId(16);
						return true;
					case 2 :
						player.getDialogue().sendOption("Would you chop my tree down for me?", "Could you take care of my crops for me?", "Can you give me any farming advice?", "Can you sell me something?", "That's all, thanks");
						return true;
					case 3 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("Would you chop my tree down for me?", CONTENT);
								player.getDialogue().setNextChatId(11);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("Could you take care of my crops for me?", CONTENT);
								if (farmersData.getFieldProtected() != "allotment")
									player.getDialogue().setNextChatId(7);
								return true;
							case 3 :
								player.getDialogue().sendPlayerChat("Can you give me any farming advice?", CONTENT);
								player.getDialogue().setNextChatId(8);
								return true;
							case 4 :
								player.getDialogue().sendPlayerChat("Can you sell me something?", CONTENT);
								player.getDialogue().setNextChatId(9);
								return true;
						}
						break;
					case 4 :
						player.getDialogue().sendNpcChat("I Might, Which one were you thinking of?", HAPPY);
						return true;
					case 5 :
						player.getDialogue().sendOption(farmersData.getDialogueOptions());
						return true;
					case 6 :
						switch(optionId) {
							case 1:
								Farmers.ProtectPlant(player, 0, "allotment", player.getClickId(), 1);
								player.getDialogue().dontCloseInterface();
								break;
							case 2:
								Farmers.ProtectPlant(player, 1, "allotment", player.getClickId(), 1);
								player.getDialogue().dontCloseInterface();
								break;
						}
						break;
					case 7 :
						Farmers.ProtectPlant(player, -1, farmersData.getFieldProtected(), player.getClickId(), 1);
						player.getDialogue().dontCloseInterface();
						break;
					case 8 :
						Farmers.sendFarmingAdvice(player);
						player.getDialogue().dontCloseInterface();
						return true;
					case 9 :
						player.getDialogue().sendNpcChat("Sure, I have a bunch of tools for you to use.", HAPPY);
						return true;
					case 10 :
						ShopManager.openShop(player, farmersData.getShopId());
						player.getDialogue().dontCloseInterface();
						break;
					case 11 :
						player.getDialogue().sendNpcChat("Sure, for only 200gp, I will chop it down for you", CONTENT);
						return true;
					case 12 :
						player.getDialogue().sendOption("Sure, here you go", "Sorry, I am a little broke.");
						return true;
					case 13 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("Sure, Here you go", CONTENT);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("Sorry, I am a litle broke", ANNOYED);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 14 :
						Farmers.chopDownTree(player, player.getClickId());
						player.getDialogue().dontCloseInterface();
						return true;
					case 15 :
						player.getDialogue().sendNpcChat("Sorry, but you have no tree growing in this patch.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					case 16 :
						player.getDialogue().sendOption("Could you take care of my crops for me?", "Can you give me any farming advice?", "Can you sell me something?", "That's all, thanks");
						return true;
					case 17 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Could you take care of my crops for me?", CONTENT);
								player.getDialogue().setNextChatId(4);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Can you give me any farming advice?", CONTENT);
								player.getDialogue().setNextChatId(8);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("Can you sell me something?", CONTENT);
								player.getDialogue().setNextChatId(9);
								return true;
						}
						break;
					case 18 :
						player.getDialogue().sendOption("Sure, here you go", "Sorry, I don't have those at the moment.");
						return true;
					case 19 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Sure, here you go", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Sorry, I don't have those at the moment.", ANNOYED);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 20 :
						Farmers.ProtectPlant(player, player.getTempInteger(), farmersData.getFieldProtected(), player.getClickId(), 2);
						player.getDialogue().dontCloseInterface();
						break;
				}
				break;
			case 954 : //brother brace
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendPlayerChat("Good day brother, my name's " + player.getUsername(), HAPPY);
						return true;
					case 2 :
						player.getDialogue().sendNpcChat("Hello, " + player.getUsername() + ". I'm Brother Brace. I'm here to tell", "you all about prayer.", HAPPY);
						player.getDialogue().endDialogue();
						return true;
					case 3 :
						player.getDialogue().sendNpcChat("This is your prayer list. Prayers can help a lot in", "combat. Click on the prayer you wish to use to activate", "it, and click it again to deactivate it.", HAPPY);
						return true;
					case 4 :
						player.getDialogue().sendNpcChat("Active prayers will drain your prayer power which you", "can recharge by finding an altar or other holy spot,", "and praying there.", HAPPY);
						return true;
					case 5 :
						player.getDialogue().sendNpcChat("As you noticed, most enamies when defeated will drop", "bones as they die. Burying bones, by clicking them in", "your inventory, will gain you experience in prayer.", HAPPY);
						return true;
					case 6 :
						player.getDialogue().sendNpcChat("I'm also the community officer round here too. So it's", "my job to tell you about your friends and ignore list.", HAPPY);
						player.getDialogue().endDialogue();
						return true;
					case 7 :
						player.getDialogue().sendNpcChat("Good. Now you have both menus open I'll tell you a", "little about each. You can add people to either list by", "clicking the add button then typing their names into", "the box that appears.", HAPPY);
						return true;
					case 8 :
						player.getDialogue().sendNpcChat("You remove people from the lists in the same way. If", "you add someone to your ignore list they will not be", "able to talk to you, or send any form of message to", "you.", HAPPY);
						return true;
					case 9 :
						player.getDialogue().sendNpcChat("Your friends list shows the online status of your", "friends. Friends in red are offline, green online and on", "the same server, and yellow online but on a different", "server.", HAPPY);
						return true;
					case 10 :
						player.getDialogue().sendPlayerChat("Are there rules on ingame behaviour?", HAPPY);
						return true;
					case 11 :
						player.getDialogue().sendNpcChat("Yes you should read the rules of conduct on our", "frontpage to make sure you do nothing to get yourself", "banned.", HAPPY);
						return true;
					case 12 :
						player.getDialogue().sendNpcChat("But as a general guide always try to be as courteous to", "people in game, remember the people in the game are", "real people somewhere with feelings.", HAPPY);
						return true;
					case 13 :
						player.getDialogue().sendNpcChat("If you go round being abusive or causing trouble your", "character could quickly be the one in trouble.", HAPPY);
						return true;
					case 14 :
						player.getDialogue().sendPlayerChat("OK... Thanks I'll bear that in mind.", HAPPY);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 944 : //combat instructor
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendPlayerChat("Hi! My name's " + player.getUsername() + ".", HAPPY);
						return true;
					case 2 :
						player.getDialogue().sendNpcChat("Do I look like I care? To me you're just another", "newcomer who thinks they're ready to fight.", HAPPY);
						return true;
					case 3 :
						player.getDialogue().sendNpcChat("I am Vannaka, the greatest swordsman alive.", HAPPY);
						return true;
					case 4 :
						player.getDialogue().sendNpcChat("Let's get started by teaching you to wield a weapon.", HAPPY);
						player.getDialogue().endDialogue();
						return true;
					case 5 :
						player.getDialogue().sendNpcChat("Very good, but that little butter knife isn't going to", "protect you much. Here, take these.", HAPPY);
						player.getDialogue().endDialogue();
						return true;
					case 6 :
						player.getDialogue().sendPlayerChat("I did it! I killed a Giant Rat!", HAPPY);
						return true;
					case 7 :
						player.getDialogue().sendNpcChat("I saw " + player.getUsername() + ". You seem better at this than I at", "first took you for. Well, you seem to have the hang of", "basic swordplay. Let's move on.", HAPPY);
						return true;
					case 8 :
						player.getDialogue().sendNpcChat("Let's try some ranged attacking, with this you can kill", "foes from a distance. Also foes unable to reach you are", "as good as dead. You'll be able to attack the rats", "without entering the pit.", HAPPY);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 942 : //cooking chef
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Ah... welcome newcomer. I am Leo, the chef. It is here", "I will teach you how to cook food truly fit for a king.", HAPPY);
						return true;
					case 2 :
						player.getDialogue().sendPlayerChat("I already know how to cook. Brynna taught me just", "now.", HAPPY);
						return true;
					case 3 :
						player.getDialogue().sendNpcChat("HA HA HA HA HA HA! You call THAT cooking?", "Some shrimp on an open log fire? Oh no no no... I am", "going to teach you the fine art of cooking bread.", HAPPY);
						return true;
					case 4 :
						player.getDialogue().sendNpcChat("And no fine meal is complete without good music so", "we'll cover that while you're here too.", HAPPY);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 947 : //financial instructor
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendPlayerChat("Hello... Who are you?", HAPPY);
						return true;
					case 2 :
						player.getDialogue().sendNpcChat("I'm a financial advisor. I'm here to tell people how to", "make money.", HAPPY);
						return true;
					case 3 :
						player.getDialogue().sendPlayerChat("OK... How can I make money then?", HAPPY);
						return true;
					case 4 :
						player.getDialogue().sendPlayerChat("...how you can make money. Quite.", HAPPY);
						return true;
					case 5 :
						player.getDialogue().sendNpcChat("Well there are three basic ways of making money here;", "Combat, Quests and Trading. I will talk you through", "each of them very quickly.", HAPPY);
						return true;
					case 6 :
						player.getDialogue().sendNpcChat("Let's start with combat, as it is probably still fresh in", "your mind. Many enamies, both human and monster,", "will drop items when they die.", HAPPY);
						return true;
					case 7 :
						player.getDialogue().sendNpcChat("Now, the next way to earn money quickly is by quests.", "Many people on 2006Scape have things they need", "doing, which they will reward you for.", HAPPY);
						return true;
					case 8 :
						player.getDialogue().sendNpcChat("By getting a high level in skills such as cooking, mining,", "smithing, or fishing, you can create your own items and", "sell them for pure profit.", HAPPY);
						return true;
					case 9 :
						player.getDialogue().sendNpcChat("Well that about covers it. Come back if you'd like to go", "over this again.", HAPPY);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 946 : //magic instructor
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendPlayerChat("Hello.", HAPPY);
						return true;
					case 2 :
						player.getDialogue().sendNpcChat("Good day, newcomer. My name is Terrova. I'm here", "to tell you about Magic. Let's start by opening your", "spellbook.", HAPPY);
						player.getDialogue().endDialogue();
						return true;
					case 3 :
						player.getDialogue().sendNpcChat("Good. This is a list of your spells. Currently you can", "only cast one offensive spell called Wind Strike. Let's", "try it out on one of those chickens.", HAPPY);
						player.getDialogue().endDialogue();
						return true;
					case 4 :
						player.getDialogue().sendNpcChat("Well, you're all finished here now. I'll give you a", "seasonable number of runes when you leave.", HAPPY);
						return true;
					case 5 :
						player.getDialogue().sendNpcChat("Would you like to go to the mainland?", HAPPY);
						return true;
					case 6 :
						player.getDialogue().sendOption("Yes.", "No.");
						return true;
					case 7 :
						player.getDialogue().sendNpcChat("When you get to the mainland you will find yourself in", "the town of Lumbridge. If you want some ideas on", "where to go next, talk to my friend the Lumbridge", "Guide. You can't miss him, he's holding a big staff with", HAPPY);
						return true;
					case 8 :
						player.getDialogue().sendNpcChat("a question mark on the end. He also has a white beard", "and carries a rockrack full of scrolls. There are also", "many tutors willing to teach you about the many skills", "you could learn.", HAPPY);
						return true;
					case 9 :
						player.getDialogue().sendStatement("When you get to Lumbridge, look for this icon on your", "minimap. This Lumbridge Guide or one of the other", "tutors should be standing near there. The Lumbridge", "Guide should be standing slightly to the north-east of");
						return true;
					case 10 :
						player.getDialogue().sendStatement("the castle's countyard and the others you will find", "scattered around Lumbridge.");
						return true;
					case 11 :
						player.getDialogue().sendNpcChat("If all else falls, visit the 2006Scape website for a whole", "board of information on quests, skills and minigames", "as well as a very good starter's guide", HAPPY);
						return true;
					case 12 :
						player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);
						player.getDialogue().dontCloseInterface();
						break;
				}
				break;
			case 948 : //mining instructor
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Hi there... You must be new around here. So what do", "I call you? 'Newcomer' seems so impersonal... and if", "we're going to be working together, I'd rather call you", "by name.", HAPPY);
						return true;
					case 2 :
						player.getDialogue().sendPlayerChat("You can call me " + player.getUsername() + ".", HAPPY);
						return true;
					case 3 :
						player.getDialogue().sendNpcChat("Ok then, " + player.getUsername() + ". My name is Dezzick, and I'm a", "miner by trade. Let's prospect some of those rocks.", HAPPY);
						player.getDialogue().endDialogue();
						return true;
					case 4 :
						player.getDialogue().sendPlayerChat("I prospected both types of rock! One set contains tin", "and the other has copper ore inside.", HAPPY);
						return true;
					case 5 :
						player.getDialogue().sendNpcChat("Absolutely right " + player.getUsername() + ". These two ore types can", "be smelted together to make bronze.", HAPPY);
						return true;
					case 6 :
						player.getDialogue().sendNpcChat("So now you know what ore is in the rocks over there,", "why don't you have a go at mining some tin and", "copper? Here, you'll need this to start with.", HAPPY);
						player.getDialogue().endDialogue();
						return true;
					case 7 :
						player.getDialogue().sendPlayerChat("How do I make a weapon out of this?", HAPPY);
						return true;
					case 8 :
						player.getDialogue().sendNpcChat("Okay. I'll show you how to make a dagger out of it.", "You'll be needing this...", HAPPY);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 949 : //quest guide instructor
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Ah... welcome adventureer. I'm here to tell you all about", "quests. Let's start by opening the quest side panel.", HAPPY);
						player.getDialogue().endDialogue();
						return true;
					case 2 :
						player.getDialogue().sendNpcChat("Now you have the journal open I'll tell you a bit about", "it. As you see at the moment all the quests are shown", "In red, a quest shown in red is unstarted.", HAPPY);
						return true;
					case 3 :
						player.getDialogue().sendNpcChat("When you start a quest it will change colour to yellow,", "and green when you've finished. This is so you can", "easily see what's complete, what's started, and what's left", "to begin.", HAPPY);
						return true;
					case 4 :
						player.getDialogue().sendNpcChat("The start of quests are easy to find. Look out for the", "star icons on the minimap, like the one you should see", "marking my house.", HAPPY);
						return true;
					case 5 :
						player.getDialogue().sendNpcChat("The quests themselves can vary greatly from collecting", "beads to hunting down dragons. Generally quests are", "started by talking to a non player character and will", "involve a series of tasks.", HAPPY);
						return true;
					case 6 :
						player.getDialogue().sendNpcChat("There's not a lot more I can tell you about questing.", "You have to experience the thrill of it yourself to fully", "understand. You may find some adventure in the caves", "under my house.", HAPPY);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 943 : //survival guide
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Hello there newcomer. My name is Brynna. My job is", "to teach you a few survival tips and tricks. First off", "we're going to start with the most basic survival skill of", "all; making a fire.", HAPPY);
						player.getDialogue().endDialogue();
						return true;
					case 2 :
						player.getDialogue().sendNpcChat("There's nothing like a good fire to warm the bones.", "Next thing is getting food in our bellies. We'll need", "something to cook. There are shrimp in the pond there.", "So let's catch and cook some.", HAPPY);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 953 : //tut banker
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Good day, would you like to access your bank account?", HAPPY);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
						case 1 :
							BankManager.openBank(player);
							break;
						}
						break;
				}
				break;
			case 945 : //tutorial instructor
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Greetings! I see you are a new arrival to this land. My", "job is to welcome all new visitors. So welcome!", HAPPY);
						return true;
					case 2 :
						player.getDialogue().sendNpcChat("You have already learnt the first thing needed to", "succeed in this world... Talking to other people!", HAPPY);
						return true;
					case 3 :
						player.getDialogue().sendNpcChat("You will find many inhabitants of this world have useful", "things to say to you. By clicking on them with your", "mouse you can talk to them.", HAPPY);
						return true;
					case 4 :
						player.getDialogue().sendNpcChat("I would also suggest reading through some of the", "supporting information on the website. There you can", "find the Knowledge Base. which contains all the", "additional information you're ever likely to need. It also", HAPPY);
						return true;
					case 5 :
						player.getDialogue().sendNpcChat("contains maps and helpful tips to help you on your", "Journey", HAPPY);
						return true;
					case 6 :
						player.getDialogue().sendNpcChat("You will notice a flashing icon of a spanner; please click", "on this to continue the tutorial.", HAPPY);
						player.getDialogue().endDialogue();
						return true;
					case 7 :
						player.getDialogue().sendNpcChat("I'm glad you're making progress!", HAPPY);
						return true;
					case 8 :
						player.getDialogue().sendNpcChat("To continue the tutorial go through that door over", "there and speak to your first instructor!", HAPPY);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 3021 : //leprechaun
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Ah, 'tis a foine day to be sure! Were yez wantin' me to", "store yer tools, or maybe ye might be wantin' yer stuff", "back from me?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("What tools can you store?", "Open your tool store, please.", "Actually, I'm fine.");
						return true;
					case 3 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("What tools can you store?", CONTENT);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("Open your tool store, please.", CONTENT);
								player.getDialogue().setNextChatId(7);
								return true;
							case 3 :
								player.getDialogue().sendPlayerChat("Actually, I'm fine.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 4 :
						player.getDialogue().sendNpcChat("We'll hold onto yer rake, seed dibber, spade, secateurs,", "waterin' can and trowel - but mind it's not one of them", "fancy trowels only archaeologist use.", HAPPY);
						return true;
					case 5 :
						player.getDialogue().sendNpcChat("We'll take a few buckets off yer hand if you want", "too, and even yer compost and supercompost. There's", "room in our shed for plenty of compost, so bring it on.", HAPPY);
						return true;
					case 6 :
						player.getDialogue().sendNpcChat("Also, if ye hands us yer Farming produce,", "we might be able to change it into banknotes.", HAPPY);
						player.getDialogue().setNextChatId(2);
						return true;
					case 7 :
						player.getFarmingTools().loadInterfaces();
						player.getDialogue().dontCloseInterface();
						break;
				}
				break;
			case 2244 : //lumbridge guide
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Greetings adventurer. I am Phileas the Lumbridge", "Guide. I am here to give information and directions to", "new players. Do you require any help?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes please.", "No, I can find things myself thank you.");
						return true;
					case 3 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("Yes please.", CONTENT);
								return true;
							case 2 :
								player.getDialogue().sendNpcChat("If you ever need help, you can talk to me again.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 4 :
						player.getDialogue().sendNpcChat("First I must warn you to take every precaution to", "keep your 2006Scape password and PIN secure. The", "most important thing to remember is to never give your", "password to, or share your account with, anyone.", CONTENT);
						return true;
					case 5 :
						player.getDialogue().sendNpcChat("I have much more information to impart; what would", "you like to know about?", CONTENT);
						return true;
					case 6:
						player.getDialogue().sendOption("Where can I find a quest to go on?", "What monsters should I fight?", "Where can I make money?", "How can I heal myself?", "Where can I find a bank?");
						return true;
					case 7:
						switch(optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Where can I find a quest to go on?", CONTENT);
								player.getDialogue().setNextChatId(16);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("What monsters should I fight?", CONTENT);
								player.getDialogue().setNextChatId(34);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("Where can I make money?", CONTENT);
								player.getDialogue().setNextChatId(18);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("How can I heal myself?", CONTENT);
								player.getDialogue().setNextChatId(12);
								return true;
							case 5 :
								player.getDialogue().sendPlayerChat("Where can I find a bank?", CONTENT);
								return true;
						}
						break;
					case 8:
						player.getDialogue().sendNpcChat("The nearest bank is in Draynor Village - go", "west from here.", CONTENT);
						return true;
					case 9:
						player.getDialogue().sendNpcChat("Is there anything else you need help with?", CONTENT);
						return true;
					case 10:
						player.getDialogue().sendOption("No thank you.", "Yes please.");
						return true;
					case 11:
						switch(optionId) {
							case 1 :
								player.getDialogue().sendNpcChat("If you ever need help, you can talk to me again.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("Yes please.", CONTENT);
								player.getDialogue().setNextChatId(6);
								return true;
						}
						break;
					case 12:
						player.getDialogue().sendNpcChat("You will always heal slowly over time, but people", "normally choose to heal themselves faster by eating food.", CONTENT);
						return true;
					case 13:
						player.getDialogue().sendNpcChat("There are many different foods in the game such as", "cabbage, fish, meat and many more. Which do you wish", "to hear about?", CONTENT);
						return true;
					case 14:
						player.getDialogue().sendOption("How do I get cabbages?", "How do I fish?", "Where can I find meat?", "Nevermind.");
						return true;
					case 15:
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("How do I get cabbages?", CONTENT);
								player.getDialogue().setNextChatId(19);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("How do I fish?", CONTENT);
								player.getDialogue().setNextChatId(23);
								return true;
							case 3 :
								player.getDialogue().sendPlayerChat("Where can I find meat?", CONTENT);
								player.getDialogue().setNextChatId(29);
								return true;
							case 4 :
								player.getDialogue().sendNpcChat("If you ever need help, you can talk to me again.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 16 :
						player.getDialogue().sendNpcChat("Well, I heard my friend the cook was in need of a spot", "of help. He'll be in the kitchen of this here castle. Just", "talk to him and he'll set you off.", CONTENT);
						player.getDialogue().setNextChatId(9);
						return true;
					/*case 17 :
						player.getDialogue().sendNpcChat("You should fight monsters that are near", "your combat level.", CONTENT);
						player.getDialogue().setNextChatId(9);
						return true;*/
					case 18 :
						player.getDialogue().sendNpcChat("There are many ways to make money in the game. I", "would suggest either killing monsters or doing a trade", "skill such as Smithing or Fishing.", CONTENT);
						player.getDialogue().setNextChatId(31);
						return true;
					case 19 :
						player.getDialogue().sendNpcChat("There is a field a little distance to the north of here", "packed full of cabbages which are there for the picking.", CONTENT);
						player.getDialogue().setNextChatId(20);
						return true;
					case 20:
						player.getDialogue().sendNpcChat("Is there anything else you need help with?", CONTENT);
						return true;
					case 21:
						player.getDialogue().sendOption("No thank you.", "I'd like to know about other food.", "Yes please.");
						return true;
					case 22:
						switch(optionId) {
							case 1 :
								player.getDialogue().sendNpcChat("If you ever need help, you can talk to me again.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 2 :
								player.getDialogue().sendOption("How do I get cabbages?", "How do I fish?", "Where can I find meat?", "Nevermind.");
								player.getDialogue().setNextChatId(15);
								return true;
							case 3 :
								player.getDialogue().sendPlayerChat("Yes please.", CONTENT);
								player.getDialogue().setNextChatId(6);
								return true;
						}
						break;
					case 23:
						player.getDialogue().sendNpcChat("Fishing spots require different levels and equipment to", "use. To start Fishing, you'll want to talk to the Fishing", "tutor who can be found in the swamps south of here.", "He will also give you a small fishing net if you don't", CONTENT);
						return true;
					case 24 :
						player.getDialogue().sendNpcChat("own one already.", CONTENT);
						return true;
					case 25 :
						player.getDialogue().sendNpcChat("You will need some Fishing equipment. At the Fishing", "spots to the south you can only use a small fishing net.", CONTENT);
						return true;
					case 26 :
						player.getDialogue().sendPlayerChat("Where could I find one of those?", CONTENT);
						return true;
					case 27 :
						player.getDialogue().sendNpcChat("You can get them from a Fishing shop or our Fishing", "Tutor south of here in the swamp. There is a Fishing", "shop in Port Sarim; you can find it on the world map.", "Port Sarim is some way to the west of here, beyond", CONTENT);
						return true;
					case 28 :
						player.getDialogue().sendNpcChat("the village of Draynor.", CONTENT);
						player.getDialogue().setNextChatId(20);
						return true;
					case 29 :
						player.getDialogue().sendNpcChat("I suggest you go and kill some chickens. The roads on", "either side if this river eventually go past a chicken", "farm. When you have killed some chickens, cook them.", "You cold either make a fire or use a range.", CONTENT);
						return true;
					case 30 :
						player.getDialogue().sendNpcChat("There is a range at the southern end in this town and", "a Cooking tutor in south Lumbridge near Bob's Brilliant", "Axes shop.", CONTENT);
						player.getDialogue().setNextChatId(20);
						return true;
					case 31 :
						player.getDialogue().sendNpcChat("Please don't try to get money by begging of other", "players. It will make you unpopular. Nobody likes a", "beggar. It is very irritating to have other players asking", "for your hard-earned cash.", CONTENT);
						return true;
					case 32:
						player.getDialogue().sendOption("Where can I smith?", "How do I fish?", "What monsters should I fight?");
						return true;
					case 33:
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("Where can I smith?", CONTENT);
								player.getDialogue().setNextChatId(45);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("How do I fish?", CONTENT);
								player.getDialogue().setNextChatId(23);
								return true;
							case 3 :
								player.getDialogue().sendPlayerChat("What monsters should I fight?", CONTENT);
								return true;
						}
						break;
					case 34 :
						player.getDialogue().sendNpcChat("There's lots of beasts to fight in the woods around here,", "especially to the west. There are certainly some goblins", "and spiders that are pests and could do with being", "cleared out. There's also a chicken farm or two up the", CONTENT);
						return true;
					case 35 :
						player.getDialogue().sendNpcChat("road for some fairly easy picking. Non-player", "characters usually appear as yellow dots on your mini-", "map, although there are some that you won't be able to", "fight, such as myself. A monster's combat level is shown", CONTENT);
						return true;
					case 36 :
						player.getDialogue().sendNpcChat("next to their 'Attack' option. If that level is coloured", "green it means the monster is weaker than you. If it is", "red, it means that the monster is tougher than you.", CONTENT);
						return true;
					case 37 :
						player.getDialogue().sendNpcChat("Remember, you will do better if you have better", "armour and weapons and it's always worth carrying a", "bit of food to heal yourself.", CONTENT);
						return true;
					case 38 :
						player.getDialogue().sendOption("Where can I get food to heal myself?", "Where can I get better armour and weapons?", "Okay, thanks, I will go and kill things.", "Can I kill other players?", "I'd like to know about something else.");
						return true;
					case 39 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("Where can I get food to heal myself?", CONTENT);
								player.getDialogue().setNextChatId(13);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("Where can I get better armour and weapons?", CONTENT);
								return true;
							case 3 :
								player.getDialogue().sendPlayerChat("Okay, thanks, I will go and kill things.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 4 :
								player.getDialogue().sendPlayerChat("Can I kill other players?", CONTENT);
								player.getDialogue().setNextChatId(49);
								return true;
							case 5 :
								player.getDialogue().sendPlayerChat("I'd like to know about something else.", CONTENT);
								player.getDialogue().setNextChatId(6);
								return true;
						}
						break;
					case 40 :
						player.getDialogue().sendNpcChat("Well, you can make them, you buy them or talk to the", "combat tutors just west of here.", CONTENT);
						return true;
					case 41 :
						player.getDialogue().sendOption("How do I make a weapon?", "Where can I buy a weapon?", "Could I get a staff like yours?");
						return true;
					case 42 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("How do I make a weapon?", CONTENT);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("Where can I buy a weapon?", CONTENT);
								player.getDialogue().setNextChatId(47);
								return true;
							case 3 :
								player.getDialogue().sendPlayerChat("Could I get a staff like yours?", CONTENT);
								player.getDialogue().setNextChatId(48);
								return true;
						}
						return true;
					case 43 :
						player.getDialogue().sendNpcChat("The Smithing skill allows you to make armour and", "weapons. Talk to the boy who smelts metal in the", "furnace, I'm sure he can help", CONTENT);
						return true;
					case 44 :
						player.getDialogue().sendPlayerChat("Where can I smith?", CONTENT);
						return true;
					case 45 :
						player.getDialogue().sendNpcChat("You will find a helpful Smithing tutor in the west of", "Varrock - that's north of here. Follow the path across", "the river and head north", CONTENT);
						return true;
					case 46 :
						player.getDialogue().sendNpcChat("I suggest you go and mine some ore; find the Mining", "symbol - with the guide symbol near it - in the swamp", "south of here. The Mining guide there can teach you", "how to mine ore.", CONTENT);
						player.getDialogue().setNextChatId(9);
						return true;
					case 47 :
						player.getDialogue().sendNpcChat("You can buy a sword from any sword shop, such as", "the one in Varrock - located north of here. Simply", "look for the sword icon on the mini-map, and you'll", "find the store.", CONTENT);
						player.getDialogue().setNextChatId(9);
						return true;
					case 48 :
						player.getDialogue().sendNpcChat("Sorry, my staff is not for sale. However, if your", "interested in buy a staff, visit Zeke's staff", "shop located in Varrock, abit north of here.", CONTENT);
						player.getDialogue().setNextChatId(9);
						return true;
					case 49 :
						player.getDialogue().sendNpcChat("To fight other players, you need to visit the duel", "arena, where you can fight players for fun or", "for stakes. However, if you want a more dangerous", "challenge, you can visit the wilderness.", CONTENT);
						player.getDialogue().setNextChatId(9);
						return true;
				}
				break;
			case 599 : //makeover mage
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Greetings, " + Misc.formatPlayerName(player.getUsername()) + ".", "How may I assist you?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Can you change my appearance?", "That's a nice necklace you have, can I buy one?", "I'm fine, thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Can you change my appearance?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("That's a nice necklace you have, can I buy one?", CONTENT);
								player.getDialogue().setNextChatId(8);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("I'm fine, thanks.", CONTENT);
								player.getDialogue().setNextChatId(7);
								return true;
									
						}
						break;
					case 4 :
						player.getDialogue().sendNpcChat("Sure. It will only cost you 1000 coins.", CONTENT);
						return true;
					case 5 :
						player.getDialogue().sendOption("Alright, here you go.", "Nevermind.");
						return true;
					case 6 :
						switch(optionId) {
							case 1:
								if (player.getInventory().removeItem(new Item(995, 1000))) {
									player.getActionSender().sendInterface(3559);
									player.getDialogue().dontCloseInterface();
									break;
								} else {
									player.getDialogue().sendPlayerChat("Sorry, looks like I don't have enough coins for that.", SAD);
								}
								player.getDialogue().endDialogue();
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("I'm fine, thanks.", CONTENT);
								return true;
						}
						break;
					case 7 :
						player.getDialogue().sendNpcChat("I'm a busy man.", "Come back when you need something.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					case 8 :
						player.getDialogue().sendNpcChat("Sure, I can sell you a copy for 100 coins.", CONTENT);
						return true;
					case 9 :
						player.getDialogue().sendOption("Alright, here you go.", "Nevermind.");
						return true;
					case 10 :
						switch(optionId) {
							case 1:
								if (player.getInventory().removeItem(new Item(995, 100))) {
									player.getInventory().addItem(new Item(7803));
									player.getDialogue().sendNpcChat("Thanks, here's your amulet.", CONTENT);
								} else {
									player.getDialogue().sendPlayerChat("Sorry, looks like I don't have enough coins for that.", SAD);
								}
								player.getDialogue().endDialogue();
								return true;
						}
						break;
				}
				break;
			case 10010 : //duel arena forfeit
				if (!player.getDuelMainData().canStartDuel()) {
					break;
				}
				if (RulesData.NO_FORFEIT.activated(player)) {
					player.getActionSender().sendMessage("Forfeiting is disabled in this match!");
					break;
				}
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendStatement("Are you sure you want to forfeit?");
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes, I want to give up.", "No, I'll keep fighting!");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								player.getDuelMainData().handleDeath(true);
								return true;
						}
						break;
				}
				break;
			case 10012 : // enchanted gem
				if (player.getSlayer().slayerMaster < 1) {
					player.getDialogue().sendStatement("You have currently no task assigned. Talk to any", "slayer master to recieve one.");
					player.getDialogue().endDialogue();
					return true;
				}
				SlayerMasterData slayerMasterData = SlayerMasterData.forId(player.getSlayer().slayerMaster);
				player.getDialogue().setLastNpcTalk(player.getSlayer().slayerMaster);
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat((new StringBuilder()).append("Hello there, ").append(player.getUsername()).append(", what can I help you with?").toString(), HAPPY);
						return true;
					case 2 :
						player.getDialogue().sendOption(new String[] { "How am I doing so far?", "Who are you?", "Where are you?", "Got any tips for me?", "Nothing really." });
						return true;
					case 3 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendNpcChat((new StringBuilder()).append("You're currently assigned to kill ").append(player.getSlayer().slayerTask).append("s;").toString(), (new StringBuilder()).append("only ").append(player.getSlayer().taskAmount).append(" more to go.").toString(), HAPPY);
								player.getDialogue().setNextChatId(2);
								return true;
							case 2 :
								player.getDialogue().sendNpcChat((new StringBuilder()).append("My name's ").append(new Npc(player.getSlayer().slayerMaster).getDefinition().getName()).append("; I'm a Slayer Master.").toString(), HAPPY);
								player.getDialogue().setNextChatId(2);
								return true;
							case 3 :
								player.getDialogue().sendNpcChat((new StringBuilder()).append("I'm in ").append(slayerMasterData.getMasterLocation()).append(". Only a fool would forget that.").toString(), HAPPY);
								player.getDialogue().setNextChatId(2);
								return true;
							case 4 :
								SlayerTipsData slayerTipsData = SlayerTipsData.forName(player.getSlayer().slayerTask);
								if (slayerTipsData == null) {
									player.getDialogue().sendNpcChat("There is no tips about this npc yet.", HAPPY);
								} else {
									player.getDialogue().sendNpcChat(slayerTipsData.getMonsterTips(), HAPPY);
								}
								player.getDialogue().setNextChatId(2);
								return true;
						}
						break;
				}
				break;
			case 70 : //slayer masters
			case 1596 :
			case 1597 :
			case 1598 :
			case 1599 :
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("'Ello, and what are you after then?", HAPPY);
						return true;
					case 2 :
						player.getDialogue().sendOption("I need another assignment", "Do you have anything for trade?", "Er...nothing");
						return true;
					case 3 :
						switch(optionId) {
							case 1 :
								if (player.getSlayer().slayerMaster == Slayer.TURAEL) {
									player.getDialogue().sendNpcChat((new StringBuilder()).append("You're still hunting ").append(player.getSlayer().slayerTask).append("s; come back").toString(), "when you've finished your task.", Dialogues.HAPPY);
									player.getDialogue().endDialogue();
								} else {
									player.getSlayer().assignNewTask(id);
								}
								return true;
							case 2 :
								player.getDialogue().sendNpcChat("I have a wide selection of Slayer equipment; take a look!", Dialogues.HAPPY);
								player.getDialogue().setNextChatId(6);
								return true;
						}
						break;
					case 4 :
						player.getDialogue().sendOption("Do you have any tips for me?", "Thanks, I'll be on my way.");
						return true;
					case 5 :
						switch(optionId) {
							case 1 :
								SlayerTipsData slayerTipsData = SlayerTipsData.forName(player.getSlayer().slayerTask);
								if (slayerTipsData == null) {
									player.getDialogue().sendNpcChat("There is no tips about this npc yet.", HAPPY);
								} else {
									player.getDialogue().sendNpcChat(slayerTipsData.getMonsterTips(), HAPPY);
								}
								player.getDialogue().endDialogue();
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("Thanks, I'll be on my way.", HAPPY);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 6 :
						ShopManager.openShop(player, 162);
						player.getDialogue().dontCloseInterface();
						break;
				}
				break;
			case 956 : //drunken dwarf
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("'Ere, matey, 'ave some 'o the good stuff.", HAPPY);
						return true;
					case 2 :
						player.getInventory().addItemOrDrop(new Item(1971));
						player.getInventory().addItemOrDrop(new Item(1917));
						player.getDialogue().sendGiveItemNpc("The dwarf gives you beer and a kebab.", "", new Item(1971), new Item(1917));
						RandomEvent.destroyEventNpc(player);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 409 : //genie
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Here you go "+player.getUsername()+".", HAPPY);
						return true;
					case 2 :
						player.getInventory().addItemOrDrop(new Item(2528));
						player.getDialogue().sendGiveItemNpc("The genie gives you a lamp.", new Item(2528));
						RandomEvent.destroyEventNpc(player);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			//*case 2476 : //rick
			//	switch(player.getDialogue().getChatId()) {
				//	case 1 :
				//		player.getDialogue().sendNpcChat("Today is your lucky day, sirrah!", "I am  donating to the victims of crime to atone", "for my past actions!", HAPPY);
				//		return true;
				//	case 2 :
				//		Item[] items = new Item[]{new Item(995, 50), new Item(1969), new Item(985), new Item(987), new Item(1623), new Item(1621), new Item(1619), new Item(1617)};
				//		Item reward = items[Misc.randomMinusOne(items.length)];
				//		player.getInventory().addItemOrDrop(reward);
				//		player.getDialogue().sendGiveItemNpc("Rick hands you "+reward.getDefinition().getName().toLowerCase()+".", reward);
				//		RandomEvent.destroyEventNpc(player);
				//		player.getDialogue().endDialogue();
				//		return true;
				//}
				//break;
			case 2540 : //dr jekyll
				switch(player.getDialogue().getChatId()) {
				case 1 :
					if (player.getRandomHerb() == null) {
						player.setRandomHerb(TalkToEvent.randomHerb());
					}
					player.getDialogue().sendNpcChat("Hello "+player.getUsername()+",", "would you happen to have a "+player.getRandomHerb().getDefinition().getName().toLowerCase()+"?", HAPPY);
					return true;
				case 2 :
					player.getDialogue().sendOption("Yes I do, here you go.", "No I don't, sorry.");
					return true;
				case 3 :
					switch(optionId) {
						case 1:
							if (player.getInventory().removeItem(player.getRandomHerb())) {
								player.getDialogue().sendNpcChat("Oh thank you so much, here have this potion.", HAPPY);
							} else {
								player.getDialogue().sendNpcChat("Looks like you don't have it. Oh well,", "have this potion I don't need anyways.", HAPPY);
								player.setRandomHerb(new Item(1));
							}
							return true;
						case 2:
							player.setRandomHerb(new Item(1));
							player.getDialogue().sendNpcChat("Oh well, was worth a try.", "Here, have this potion I don't need.", HAPPY);
							return true;
					}
					break;
				case 4 :
					Item reward = TalkToEvent.rewardForHerb(player.getRandomHerb().getId());
					player.getInventory().addItemOrDrop(reward);
					player.getDialogue().sendGiveItemNpc("Jekyll hands you "+reward.getDefinition().getName().toLowerCase()+".", reward);
					player.setRandomHerb(null);
					RandomEvent.destroyEventNpc(player);
					player.getDialogue().endDialogue();
					return true;
				}
				break;
			case 500 : //Mosol Rei
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Would you like to enter this village?", "Note that once you enter, you cannot get out", "through this way.", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes let me in please.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								player.fadeTeleport(new Position(2876, 2952));
								return true;
						}
						break;
				}
				break;
			case 606: //Squire for The Knight's Sword
				switch(player.getQuestStage(1))
				{
					case 0:
						switch(player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("What am I going to do?", DISTRESSED);
								return true;
							case 2:
								player.getDialogue().sendOption("What's wrong?","You don't look very happy.");
								return true;
							case 3:
								switch(optionId) {
									case 1:
										player.getDialogue().sendNpcChat("I've lost Sir Vyvin's sword!",DISTRESSED);
										player.getDialogue().setNextChatId(4);
									return true;
									case 2:
										player.getDialogue().sendNpcChat("Yes, please go away unless you can help!",NEAR_TEARS);
										player.getDialogue().setNextChatId(5);
									return true;
								}
							return true;
							case 4:
								player.getDialogue().sendNpcChat("Could you please help me create another?",DISTRESSED);
							return true;
							case 5:
								player.getDialogue().sendOption("I'm always happy to help a person in distress.","Sorry, I have troubles of my own.");
							return true;
							case 6:
								switch(optionId) {
									case 1:
										player.getDialogue().sendNpcChat("Thank you! You can talk to the dwarf found near Rimmington.","You'll need a redberry pie and an iron bar.", HAPPY);
										player.setQuestStage(1, 1);
										QuestHandler.getQuests()[1].startQuest(player);
										player.getDialogue().setNextChatId(3);

									return true;
									case 2:
										player.getDialogue().sendNpcChat("Oh, okay then...", DISTRESSED);
										player.getDialogue().endDialogue();
									return true;
									
								}
						}
					break;
					case 1:
						switch(player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Have you created a new sword yet?", DISTRESSED);
							return true;
							case 2:
								player.getDialogue().sendPlayerChat("Not yet, I'm working on it..",CONTENT);
							return true;
							case 3:
								player.getDialogue().sendNpcChat("Please hurry!",DISTRESSED);
							return true;
							case 4:
								player.getDialogue().sendOption("I'll get right on it.","What do I need before I talk to the dwarf");
							return true;
							case 5:
								switch(optionId) {
									case 1:
										player.getDialogue().sendNpcChat("There's no time to waste! Hurry!",DISTRESSED);
										player.getDialogue().endDialogue();
									return true;
									case 2:
										player.getDialogue().sendNpcChat("He'll probably require some other","materials from what I remember.","You'll need to make a redberry pie to make him talk.", CONTENT);
										player.getDialogue().setNextChatId(6);
										return true;
								}
							case 6:
								player.getDialogue().sendNpcChat("Then you'll need an iron bar to","create the base of the sword.", CONTENT);
								player.getDialogue().endDialogue();
							return true;
						}
					break;
					case 3:
						switch(player.getDialogue().getChatId()) {
						case 1:
							player.getDialogue().sendNpcChat("Have you made a new sword yet?", DISTRESSED);
						return true;
						case 2:
							player.getDialogue().sendPlayerChat("Yes, I have!",HAPPY);
							if(player.carryingItem(667))
							{
								player.getDialogue().setNextChatId(3);
							}
							else
							{
								player.getDialogue().setNextChatId(10);
							}
						return true;
						case 3:
							player.getDialogue().sendNpcChat("Oh thank the Heavens!",HAPPY);
						return true;
						case 4:
							player.getDialogue().sendNpcChat("Thank you!",HAPPY);
						return true;
						case 5:
							player.getDialogue().sendPlayerChat("Oh, it was nothing.",HAPPY);
						return true;
						case 6:
							player.getDialogue().sendNpcChat("I'm sure it really was, hero!",HAPPY);
						return true;
						case 7:
							player.getDialogue().sendPlayerChat("Ah, it was really nothing.",HAPPY);
						return true;
						case 8:
							player.getDialogue().sendNpcChat("Thanks so much, hero. Here's a reward.",HAPPY);
						return true;
						case 9:
							player.getDialogue().endDialogue();
							player.getInventory().removeItem(new Item(667,1));
							player.setQuestStage(1, 4);
							QuestHandler.completeQuest(player,1);
						return true;
						case 10:
							player.getDialogue().sendNpcChat("You didn't bring the sword with you.", NEAR_TEARS);
							player.getDialogue().endDialogue();
							return true;
					}
					break;
				}
			return true;
			case 604: //Thurgo for Knight's Sword
				switch(player.getQuestStage(1))
				{
					case 1:
						switch(player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Bring me redberry pie!", CALM);
								if(player.carryingItem(2325))
								{
									player.getDialogue().setNextChatId(2);
								}
								else
								{
									player.getDialogue().setNextChatId(9);
								}
							return true;
							case 2:
								player.getDialogue().sendNpcChat("Oo! Redberry pie! Thankya human!", HAPPY);
								player.getInventory().removeItem(new Item(2325, 1));
							return true;
							case 3:
								player.getDialogue().sendPlayerChat("Could you make a new copy of Sir Vyvin's Sword?", CONTENT);
							return true;
							case 4:
								player.getDialogue().sendNpcChat("Me remember that guy, he ask for blurite sword.","He pay many redberry pies. Why need another?", CONTENT);
							return true;
							case 5:
							player.getDialogue().sendPlayerChat("The Squire kind of lost it...","He sent me to get a new one.", DISTRESSED);
							return true;
							case 6:
								player.getDialogue().sendNpcChat("Me suppose me make another.","I out of blurite though, bring me some.","Also need an iron bar.", CONTENT);
							return true;
							case 7:
								player.getDialogue().sendPlayerChat("Alright, do you know where I can find the ore?", HAPPY);
							return true;
							case 8:
								player.setQuestStage(1, 2);
								player.getDialogue().sendNpcChat("Maybe in mine on hill. Deep in dungeon me used to find","very big veins. Contain very scary monsters, though.", CONTENT);
								player.getDialogue().endDialogue();
							return true;
							case 9:
								player.getDialogue().sendNpcChat("Go away!","Don't come back until you bring pie!", CONTENT);
								player.getDialogue().endDialogue();
							return true;
						}
					break;
					case 2:
						switch(player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Have you found the materials yet?", DISTRESSED);
								if((player.carryingItem(668) && player.carryingItem(2351)))
								{
									player.getDialogue().setNextChatId(2);
								}
								else
								{
									player.getDialogue().setNextChatId(7);
								}
							return true;
							case 2:
								player.getDialogue().sendPlayerChat("I've found the materials!","Could you make the sword now?", HAPPY);
							return true;
							case 3:
								player.getDialogue().sendNpcChat("Yeah, I make now.", CONTENT);
							return true;
							case 4:
								player.getDialogue().sendPlayerChat("Thanks so much.", HAPPY);
							return true;
							case 5:
								player.getDialogue().sendNpcChat("Here your sword.", "Come back with more redberry pie next time!", CONTENT);
								player.getDialogue().endDialogue();
								player.getInventory().addItemOrDrop(new Item(667, 1));
								player.getInventory().removeItem(new Item(668, 1));
								player.getInventory().removeItem(new Item(2351, 1));
								player.setQuestStage(1, 3);
							return true;
							case 6:
								player.getDialogue().sendPlayerChat("Oh, I will! Thanks Thurgo!", HAPPY);
								player.getDialogue().endDialogue();
							return true;
							case 7:
								player.getDialogue().sendPlayerChat("Oh, no. I'm having trouble finding it.", DISTRESSED);
							return true;
							case 8:
								player.getDialogue().sendNpcChat("You can find ore in mine, on hill next to shack.","Iron bar you find by yourself.", CALM);
								player.getDialogue().endDialogue();
							return true;
						}
					break;
					case 3:
						switch(player.getDialogue().getChatId()) {
							case 1:
								if(!player.hasItem(667)) //check inv and bank idiot lost sword
								{
									player.getDialogue().sendNpcChat("what human want now?", CALM);
									player.getDialogue().setNextChatId(2);
								}
								else
								{
									player.getDialogue().sendNpcChat("Go away human.", CALM);
									player.getDialogue().endDialogue();
								}
							return true;
							case 2:
								player.getDialogue().sendPlayerChat("I've lost the sword!", "can you make me a new one?", DISTRESSED);
							return true;
							case 3:
								player.getDialogue().sendNpcChat("stupid human bring material i make new.", ANNOYED);
								player.getDialogue().endDialogue();
								player.setQuestStage(1, 2);
							return true;
						}
					break;
					default:
						switch(player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Go away human.", CALM);
								player.getDialogue().endDialogue();
							return true;
						}
					break;
				}
			break;	
			case 278: //Lummy castle cook - quest npc - cooks assistant
				switch(player.getQuestStage(0))
				{
					case 0:
						switch(player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("What am I going to do?", DISTRESSED);
								return true;
							case 2:
								player.getDialogue().sendOption("What's wrong?","Can you make me a cake?","You don't look very happy.","Nice hat!");
								return true;
							case 3:
								switch(optionId) {
									case 1:
										player.getDialogue().sendNpcChat("Oh dear, oh dear, oh dear, I'm in a terrible, terrible","mess! It's the Duke's birthday today, and i should be","making him a lovely, big birthday cake using special","ingredients...",DISTRESSED);
										player.getDialogue().setNextChatId(9);
									return true;
									case 2:
										player.getDialogue().sendNpcChat("*sniff* Don't talk about cakes...",NEAR_TEARS);
										player.getDialogue().setNextChatId(2);
									return true;
									case 3:
										player.getDialogue().sendNpcChat("No, I'm not. The world is caving in around me - I'm","overcome with dark feelings of impending doom.",SAD);
										player.getDialogue().setNextChatId(2);
									return true;
									case 4:
										player.getDialogue().sendNpcChat("Er, thank you. It's a pretty ordinary cook's hat, really.",DISTRESSED);
										player.getDialogue().setNextChatId(5);
									return true;
								}
							return true;
							case 5:
								player.getDialogue().sendPlayerChat("Still, it suits you. The trousers are pretty special too.",HAPPY);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("It's all standard-issue cook's uniform...",SAD);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("The whole hat, apron and stripy trousers ensemble...it","works. It makes you looks like a real cook.",HAPPY);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("I AM a real cook! I haven't got time to be chatting","about culinary fashion, I'm in desperate need of help!",ANGRY_1);
								player.getDialogue().setNextChatId(2);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("...but I've forgotten to get the ingredients. I'll never get","them in time now. He'll sack me! What ever will i do? I","have four children and a goat to look after. Would you","help me? Please?",DISTRESSED);
								return true;
							case 10:
								player.getDialogue().sendOption("I'm always happy to help a cook in distress.","Sorry, I have troubles of my own.");
								return true;
							case 11:
								switch(optionId) {
									case 1:
										player.getDialogue().sendNpcChat("Oh thank you, thank you. I need milk, an egg and","flour. I'd be very grateful if you can get them for me.", HAPPY);
										player.setQuestStage(0, 1);
										QuestHandler.getQuests()[0].startQuest(player);
										player.getDialogue().endDialogue();
										return true;
									case 2:
										player.getDialogue().sendNpcChat("Oh, okay then...", DISTRESSED);
										player.getDialogue().endDialogue();
										return true;
								}
							return true;
						}
					break;
					case 1:
						switch(player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("How are you getting on with finding the ingredients?", DISTRESSED);
								if(!(player.carryingItem(1944) && player.carryingItem(1933) && player.carryingItem(1927)))
								{
									player.getDialogue().setNextChatId(2);
								}
								else
								{
									player.getDialogue().setNextChatId(8);
								}
							return true;
							case 2:
								player.getDialogue().sendPlayerChat("I still haven't got them all yet, I'm still looking.",CONTENT);
							return true;
							case 3:
								player.getDialogue().sendNpcChat("Please get the ingredients quickly. I'm running out of","time! The Duke will throw my goat and I onto the street!",DISTRESSED);
							return true;
							case 4:
								player.getDialogue().sendOption("I'll get right on it.","Where can I find the ingredients?");
							return true;
							case 5:
								switch(optionId) {
									case 1:
										player.getDialogue().sendNpcChat("Please hurry!",DISTRESSED);
										player.getDialogue().endDialogue();
									return true;
									case 2:
										player.getDialogue().sendNpcChat("You can mill flour at the windmill","You can find eggs by killing chickens.","You can find milk by milking a cow.",HAPPY);
										player.getDialogue().endDialogue();
									return true;
								}
							return true;
							case 6:
								player.getDialogue().endDialogue();
							return true;
							case 7:
								player.getDialogue().sendNpcChat("How are you getting on with finding the ingredients?", DISTRESSED);
							return true;
							case 8:
								player.getDialogue().sendPlayerChat("Here's a bucket of milk,","a pot of flour,","and a fresh egg.",HAPPY);
								player.getInventory().removeItem(new Item(1944,1));
								player.getInventory().removeItem(new Item(1927,1));
								player.getInventory().removeItem(new Item(1933,1));
								player.setQuestStage(0, 2);
								player.getDialogue().setNextChatId(1);
							return true;
							case 9:
								player.getDialogue().sendPlayerChat("So where do I find these ingrediants then?",HAPPY);
							return true;
							case 10:
								player.getDialogue().sendOption("Where do I find some flour?","How about some milk?","And eggs? Where are they found?","Actually, I know where to find this stuff.");
							return true;
							case 11:
								switch(optionId) {
									case 1:
										player.getDialogue().sendNpcChat("You can mill flour at the windmill",HAPPY);
										player.getDialogue().setNextChatId(10);
									return true;
									case 2:
										player.getDialogue().sendNpcChat("You can find eggs by killing chickens.",HAPPY);
										player.getDialogue().setNextChatId(10);
									return true;
									case 3:
										player.getDialogue().sendNpcChat("You can find milk by milking a cow",HAPPY);
										player.getDialogue().setNextChatId(10);
									return true;
									case 4:
										player.getDialogue().endDialogue();
									return true;
								}
							return true;
						}
					break;
					case 2:
						switch(player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("You've given me everything I need! I am saved!",HAPPY);
							return true;
							case 2:
								player.getDialogue().sendNpcChat("Thank you!",HAPPY);
							return true;
							case 3:
								player.getDialogue().sendPlayerChat("So do I get to go to the Duke's party?",HAPPY);
							return true;
							case 4:
								player.getDialogue().sendNpcChat("I'm afraid not.","Only the big cheeses get to Dine with the Duke.",SAD);
							return true;
							case 5:
								player.getDialogue().sendPlayerChat("Well, maybe one day I'll be important enough to sit at","the Duke's table.",CALM);
							return true;
							case 6:
								player.getDialogue().sendNpcChat("Maybe, but I won't be holding my breath.",HAPPY);
							return true;
							case 7:
								player.getDialogue().endDialogue();
								player.setQuestStage(0, 3);
								QuestHandler.completeQuest(player, 0);
							return true;
						}
					break;
					default:
					break;
				}
				break;
				//------ Restless Ghost -----//
				case 456: //Father Aereck for Restless Ghost
					switch(player.getQuestStage(2))
					{
						case 0: // quest stage 0 start
							switch(player.getDialogue().getChatId()) {
								case 1:
									player.getDialogue().sendNpcChat("Yes, adventurer?", DISTRESSED);
									return true;
								case 2:
									player.getDialogue().sendOption("You don't look very happy.","Sorry, didn't mean to bother you.");
									return true;
								case 3:
									switch(optionId) {
										case 1:
											player.getDialogue().sendNpcChat("*sigh*","There appears to be a ghost haunting the Church.","And the only man capable of exorcising it,","is meditating in the swamp.",DISTRESSED);
											player.getDialogue().setNextChatId(4);
										return true;
										case 2:
											player.getDialogue().sendNpcChat("It's fine.",NEAR_TEARS);
											player.getDialogue().setNextChatId(5);
										return true;
									}
								break;
								case 4:
									player.getDialogue().sendNpcChat("It'd be really nice if you could help me out.",DISTRESSED);
								return true;
								case 5:
									player.getDialogue().sendOption("Yeah, alright. I don't see why not.","Sorry, I have troubles of my own.");
								return true;
								case 6:
									switch(optionId) {
										case 1:
											player.getDialogue().sendNpcChat("Thanks, you can start by talking to Father Urhney,","his shack is somewhere in the swamp.","He can give you some information.", HAPPY);
											QuestHandler.getQuests()[2].startQuest(player);
											player.getDialogue().setNextChatId(3);
										return true;
										case 2:
											player.getDialogue().sendNpcChat("Oh, that's fine.", DISTRESSED);
											player.getDialogue().endDialogue();
										return true;
									}
								break;
							}
						break; // quest stage 0 end
						case 1: // quest stage 1 start
						case 2: // quest stage 2 start
						case 3: // quest stage 3 start
							switch(player.getDialogue().getChatId()) {
								case 1:	
									player.getDialogue().sendNpcChat("Did you get rid of the ghost yet?", DISTRESSED);
									return true;
								case 2:
									player.getDialogue().sendPlayerChat("Not yet, but I'm working on it, don't worry.", CALM);
									player.getDialogue().endDialogue();
								return true;
							}
						break; // quest stage 1 2 3 end
					}
				break;
				case 458: //Father Urhney
					switch(player.getQuestStage(2))
					{
						case 0: // quest stage 0 start
							switch(player.getDialogue().getChatId()) {
								case 1:
									player.getDialogue().sendNpcChat("Go away.", DISTRESSED);
									player.getDialogue().endDialogue();
								return true;
							}
						break; // quest stage 0 end
						case 1: // quest stage 1 start
							switch(player.getDialogue().getChatId()) {
								case 1:
									player.getDialogue().sendPlayerChat("I've been told you could help","get rid of the ghost haunting","the Lumbridge Church?",CONTENT);
								return true;
								case 2:
									player.getDialogue().sendNpcChat("What has that fool, Aereck, gotten","himself into this time?!",ANNOYED);
								return true;
								case 3:
									player.getDialogue().sendPlayerChat("Well, apparently there is a ghost that","has been haunting the church for some time.",CONTENT);
								return true;
								case 4:
									player.getDialogue().sendNpcChat("Gah, I leave for a couple months and he's","already got himself in a jam! Useless!",ANNOYED);
								return true;
								case 5:
									player.getDialogue().sendNpcChat("Here, adventurer, take this amulet.","It'll allow you to speak to the ghost.","Find out why it won't move on.",ANNOYED);
									player.getDialogue().endDialogue();
									player.getInventory().addItemOrDrop(new Item(552, 1));
									player.setQuestStage(2, 2);
								return true;
							}
						break; // quest stage 1 end
						case 2:// quest stage 2 start
						case 3:// quest stage 3 start
						case 4:// quest stage 4 start
							switch(player.getDialogue().getChatId()) {
								case 1:	
									player.getDialogue().sendNpcChat("You didn't lose your amulet, did you?", ANNOYED);
									if((player.hasItem(552)))
									{
										player.getDialogue().setNextChatId(4);
									}
									else
									{
										player.getDialogue().setNextChatId(2);
									}
									return true;
								case 2:
									player.getDialogue().sendPlayerChat("Sorry, can I get another?", CALM);
									return true;
								case 3:
									player.getDialogue().sendNpcChat("This time, try not to lose it!","I don't have very many more.",ANNOYED);
									player.getInventory().addItem(new Item(552, 1));
									player.getDialogue().endDialogue();
									return true;
								case 4:
									player.getDialogue().sendPlayerChat("No, sorry I didn't mean to bother you.",CALM);
									player.getDialogue().endDialogue();
									return true;
							}
						break; // quest stage 2 3 4 end
					}
				break;
				case 457:
					switch(player.getQuestStage(2))
					{
						case 0: // quest stage 0 start
						case 1: // quest stage 1 start
							switch(player.getDialogue().getChatId()) {
								case 1:
									player.getDialogue().sendNpcChat("OoooOoOooo! OooooOooOoo!", DISTRESSED);
								return true;
								case 2:
									player.getDialogue().sendOption("Ah, so that's why all my pies have been burning.","Oh, so the coal union is just a bunch of shills?","I have no idea what you just said.");
								return true;
								case 3:
									switch(optionId) {
										case 1:
											player.getDialogue().sendNpcChat("OooooooOooOoOoOooo!", DISTRESSED);
											player.getDialogue().endDialogue();
										return true;
										case 2:
											player.getDialogue().sendNpcChat("OoOooOoooOOOOOOOOO!!", DISTRESSED);
											player.getDialogue().endDialogue();
										return true;
										case 3:
											player.getDialogue().sendNpcChat("OooooooOooOoOoOooo!", DISTRESSED);
											player.getDialogue().endDialogue();
										return true;
									}
								break;
							}
						break; // quest stage 0 1 end
						case 2: // quest stage 2 start
							switch(player.getDialogue().getChatId()) {
								case 1:
									player.getDialogue().sendNpcChat("OoOoooOoo!",DISTRESSED);
									if((player.getEquipment().getId(2) == 552))
									{
										player.getDialogue().setNextChatId(2);
									}
									else
									{
										player.getDialogue().setNextChatId(9);
									}
								return true;
								case 2:
									player.getDialogue().sendPlayerChat("Now where'd I put that amulet?.", CONTENT);
								return true;
								case 3:
									player.getDialogue().sendNpcChat("Please help me!",DISTRESSED);
								return true;
								case 4:
									player.getDialogue().sendPlayerChat("That's better. What's wrong?", CALM);
								return true;
								case 5:
									player.getDialogue().sendNpcChat("You can understand me?!","Please! Adventurer! Help me!","My skull was stolen!", NEAR_TEARS);
								return true;
								case 6:
									player.getDialogue().sendPlayerChat("Stolen? By who?",CONTENT);
								return true;	
								case 7:
									player.getDialogue().sendNpcChat("By a skeleton, he resides in the basement","of the Wizard's Tower now.","Go there and get it back, please!","Hurry, my voice is fading in your world!",DISTRESSED);
								return true;
								case 8:
									player.getDialogue().sendPlayerChat("Alright, I'll get it back.", CALM);
									player.setQuestStage(2, 3);
									player.getDialogue().endDialogue();
								return true;
								case 9:
									player.getDialogue().sendNpcChat("OoOooooo! OOOOOoooo!", DISTRESSED);
								return true;
								case 10:
									player.getDialogue().sendOption("Ah, so that's why all my pies have been burning.","Oh, so the coal union is just a bunch of shills?","I have no idea what you just said.");
								return true;
								case 11:
									switch(optionId) {
										case 1:
											player.getDialogue().sendNpcChat("OooooooOooOoOoOooo!", DISTRESSED);
											player.getDialogue().endDialogue();
										return true;
										case 2:
											player.getDialogue().sendNpcChat("OoOooOoooOOOOOOOOO!!", DISTRESSED);
											player.getDialogue().endDialogue();
										return true;
										case 3:
											player.getDialogue().sendNpcChat("OooooooOooOoOoOooo!", DISTRESSED);
											player.getDialogue().endDialogue();
										return true;
									}
								break;
							}
						break; // quest stage 2 end
						case 3:// quest stage 3 start
							switch(player.getDialogue().getChatId()) {
								case 1:
									player.getDialogue().sendNpcChat("Have you found my skull yet, adventurer?", DISTRESSED);
									if((player.carryingItem(553)))
									{
										player.getDialogue().setNextChatId(2);
									}
									else
									{
										player.getDialogue().setNextChatId(5);
									}
								return true;
								case 2:
									player.getDialogue().sendPlayerChat("Yes, I have!", HAPPY);
								return true;
								case 3:
									player.getDialogue().sendNpcChat("Great! I can finally rest in peace!","Thank you so much adventurer.", HAPPY);
								return true;
								case 4:
									player.getDialogue().sendNpcChat("Here, have some prayer experience,","as a reward.", CONTENT);
									player.getDialogue().endDialogue();
									player.getInventory().removeItem(new Item(553,1));
									player.setQuestStage(2, 4);
									QuestHandler.completeQuest(player,2);
								return true;
								case 5:
									player.getDialogue().sendPlayerChat("Sorry, not yet.", CONTENT);
								return true;
								case 6:
									player.getDialogue().sendNpcChat("Hurry!", DISTRESSED);
									player.getDialogue().endDialogue();
								return true;
							}
						break;// quest stage 3 end
					}
				break;
				//------ Restless ghost end -----//
				//------ Imp catcher -----//
				case 706: //wizard mizgog
					switch(player.getQuestStage(4))
					{
						case 0:
							switch(player.getDialogue().getChatId()) {
								case 1:
									player.getDialogue().sendNpcChat("Looking for a quest, adventurer?", HAPPY);
									return true;
								case 2:
									player.getDialogue().sendOption("I'm always up for a quest!","Sorry, I'm too busy right now.");
									return true;
								case 3:
								switch(optionId) {
									case 1:
										player.getDialogue().sendNpcChat("Great! I've got just the one for you.", HAPPY);
										player.getDialogue().setNextChatId(4);
									return true;
									case 2:
										player.getDialogue().sendNpcChat("Ah, come back whenever then. I'll be waiting.", CALM);
										player.getDialogue().endDialogue();
									return true;
									}
								break;
								case 4:
									player.getDialogue().sendNpcChat("So Wizard Grayzag has sent out his imp to steal","my precious beads once again, I was","hoping you could get them back.",HAPPY);
									return true;
								case 5:
									player.getDialogue().sendNpcChat("If you could find all four, I'll reward you.","I need a black, red, yellow, and white bead.",HAPPY);
									return true;
								case 6:
									player.setQuestStage(4, 1);
									player.getDialogue().sendPlayerChat("Alright, I'm on it.",HAPPY);
									player.getDialogue().endDialogue();
									return true;
							}
						break;
						case 1:
							switch(player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Have you found all my beads yet, Adventurer?", HAPPY);
								if((player.carryingItem(1470) && player.carryingItem(1472) && player.carryingItem(1474) && player.carryingItem(1476)))
								{
									player.getDialogue().setNextChatId(2);
								}
								else
								{
									player.getDialogue().setNextChatId(5);
								}
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Yes, I've found them all.", HAPPY);
							return true;
							case 3:
								player.getDialogue().sendNpcChat("Perfect! Now I can get back to my study!", HAPPY);
							return true;
							case 4:
								player.getDialogue().sendNpcChat("Here is your reward.", HAPPY);
								player.getDialogue().endDialogue();
								player.getInventory().removeItem(new Item(1470,1));
								player.getInventory().removeItem(new Item(1472,1));
								player.getInventory().removeItem(new Item(1474,1));
								player.getInventory().removeItem(new Item(1476,1));
								player.setQuestStage(4, 2);
								QuestHandler.completeQuest(player,4);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("I'm still working on finding them.", CALM);
								player.getDialogue().endDialogue();
								return true;
							}
						break;
					}
				break;
				//------ Imp catcher end -----//
			case 1696: //Old man to get into Kharazi Jungle
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Want me to get you inside the Kharazi Jungle?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes please.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								player.teleport(new Position(2867, 2928, 0));
								player.getDialogue().dontCloseInterface();
								break;
						}
						break;
				}
				break;
			case 1152: //to get out of kharazi jungle
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Would you like me to bring you back", "to Shilo Village?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes please.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								player.teleport(new Position(2863, 2961, 0));
								player.getDialogue().dontCloseInterface();
								break;
						}
						break;
				}
				break;
			case 1334: //jossik
				 switch(player.getDialogue().getChatId()) {
					 case 1 :
						 player.getDialogue().sendNpcChat("Would you like to buy a book I found?", CONTENT);
						 return true;
					 case 2 :
						 player.getDialogue().sendOption("Can I see your shop?", "I'm fine.");
						 return true;
					 case 3 :
					 switch(optionId) {
					 case 1:
						 player.getDialogue().sendPlayerChat("Yeah, sure.", CONTENT);
						 player.getDialogue().setNextChatId(5);
						 return true;
					 case 2 :
						 player.getDialogue().sendPlayerChat("Sorry, I'm fine.", CONTENT);
						 player.getDialogue().setNextChatId(6);
						 return true;
					 }
					 break;
					 case 5 :
						 ShopManager.openShop(player, 167);
						 player.getDialogue().dontCloseInterface();
						 break;
					 case 6 :
						 player.getDialogue().sendNpcChat("Oh, it's alright.", CONTENT);
						 player.getDialogue().endDialogue();
						 return true;
				 }
			 break;	 
			//----Dorics quest-----///
			case 284: // Doric
				switch(player.getQuestStage(3))
				{
					case 0: // quest stage 0
						switch(player.getDialogue().getChatId()) 
						{
							case 1:
								player.getDialogue().sendNpcChat("Hello traveller, what brings you to my humble smithy?", CONTENT);
							return true;
							case 2:
								player.getDialogue().sendPlayerChat("I wanted to use your anvils.", CONTENT);
							return true;
							case 3:
								player.getDialogue().sendNpcChat("My anvils get enough work with my own use.","I make pickaxes, and it takes a lot of hard work.","If you could get me some more materials,","then I could let you use them.", CONTENT);
							return true;
							case 4:
							player.getDialogue().sendOption("Yes, I will get you the materials.", "No, hitting rocks is for the boring people, sorry.");
							return true;
							case 5:
								 switch(optionId) {
									 case 1:
										 player.getDialogue().sendPlayerChat("Yes, I will get you the materials.", CONTENT);
										 player.getDialogue().setNextChatId(6);
									 return true;
									 case 2 :
										 player.getDialogue().sendPlayerChat("No, hitting rocks is for the boring people, sorry.", CONTENT);
										 player.getDialogue().setNextChatId(7);
									 return true;
								 }
							 break;
							case 6:
								 //set quest stage 1
								player.setQuestStage(3, 1);
								QuestHandler.getQuests()[3].startQuest(player);
								player.getDialogue().sendNpcChat("Clay is what I use more than anything, to make casts.","Could you get me 6 clay, 4 copper ore, and 2 iron ore, please?","I could pay a little, and let you use my anvils.","Take this pickaxe with you just in case you need it.", CONTENT);
								player.getDialogue().setNextChatId(1);
								player.getInventory().addItemOrDrop(new Item(1265, 1));
							return true;
							case 7:
								player.getDialogue().sendNpcChat("That is your choice. Nice to meet you anyway", CONTENT);
								player.getDialogue().endDialogue();
							return true;
						}
					break;
					case 1: // quest stage 1
						switch(player.getDialogue().getChatId()) 
						{
							case 1:
								 player.getDialogue().sendPlayerChat("Where can I find those?", CONTENT);
							return true;
							case 2:
								player.getDialogue().sendNpcChat("You'll be able to find all those ores in the rocks,","just inside the Dwarven Mine.","Head east from here and you'll find the entrance,"," in the side of Ice Mountain.", CONTENT);
								if(player.getSkill().getLevel()[Skill.MINING] < 15)
								{
									player.getDialogue().setNextChatId(3);
								}
								else
								{
									//set quest stage 2
									player.setQuestStage(3, 2);
									player.getDialogue().endDialogue();
								}
							return true;
							case 3:
								 player.getDialogue().sendPlayerChat("But I'm not a good enough miner to get iron ore.", CONTENT);
							return true;
							case 4:
								//set quest stage 2
								player.setQuestStage(3, 2);
								player.getDialogue().sendNpcChat("Oh well, you could practice mining until you can.","Can't beat a bit of mining - it's a useful skill. Failing that,", "you might be able to find a more experienced adventurer,","to buy the iron ore off.", CONTENT);
								player.getDialogue().endDialogue();
							return true;
						}
					break;
					case 2: // quest stage 2
						switch(player.getDialogue().getChatId()) 
						{
							case 1:
								player.getDialogue().sendNpcChat("Have you got my materials yet, traveller?", CONTENT);
								if(player.carryingItem(434) && player.carryingItem(436) && player.carryingItem(440))
								{
									int clayCount = player.getInventory().getItemAmount(434);
									int copperCount = player.getInventory().getItemAmount(436);
									int ironCount = player.getInventory().getItemAmount(440);
									if(clayCount >= 6 && copperCount >= 4 && ironCount >= 2)
									{
										player.getDialogue().setNextChatId(2);
									}
									else
									{
										player.getDialogue().setNextChatId(5);
									}
								}
								else
								{
									player.getDialogue().setNextChatId(5);
								}
							return true;
							case 2:
								 player.getDialogue().sendPlayerChat("I have everything you need.", CONTENT);
						    return true;
							case 3:
								 player.getDialogue().sendNpcChat("Many thanks. Pass them here, please."," I can spare you some coins for your trouble,","and please use my anvils any time you want.", CONTENT);
							 return true;
							case 4:
								 player.getInventory().removeItem(new Item(434,6));
								 player.getInventory().removeItem(new Item(436,4));
								 player.getInventory().removeItem(new Item(440,2));
								 player.getDialogue().sendStatement("You hand the clay, copper, and iron to Doric.");
								 player.getDialogue().endDialogue();
								 QuestHandler.completeQuest(player,3);
							 return true;
							case 5:
								 player.getDialogue().sendPlayerChat("Sorry, I don't have them all yet.", CONTENT);
							 return true;
							case 6:
								 player.getDialogue().sendNpcChat("Not to worry, stick at it.","Remember, I need 6 clay, 4 copper ore, and 2 iron ore.", CONTENT);
								 player.getDialogue().endDialogue();
							 return true;
						}
					break;
				}
			break;
			//----Dorics quest end-----///
			//witchs potion
			case 307:
				switch(player.getQuestStage(6))
				{
					case 0:
						switch(player.getDialogue().getChatId()) 
						{
							case 1:
								player.getDialogue().sendNpcChat("Ara Ara~","What could you want with an old woman like me?", CONTENT);
							return true;
							case 2:
								player.getDialogue().sendOption("I am in search of a quest.", "N-nothing, sorry.");
							return true;
							case 3:
								 switch(optionId) {
									 case 1:
										 player.getDialogue().sendPlayerChat("I am in search of a quest.", CONTENT);
										 player.getDialogue().setNextChatId(4);
									 return true;
									 case 2 :
										 player.getDialogue().sendPlayerChat("N-nothing, sorry.", CONTENT);
										 player.getDialogue().endDialogue();
									 return true;
								 }
							 break;
							case 4:
								player.getDialogue().sendNpcChat("Hmmm... Maybe i can think of something for you.", CONTENT);
							return true;
							case 5:
								player.getDialogue().sendNpcChat("Would you like to become more proficient in the dark","arts?", CONTENT);
							return true;
							case 6:
								player.getDialogue().sendPlayerChat("Dark arts? What you mean improve my magic?", CONTENT);
							return true;
							case 7:
								player.getDialogue().sendStatement("The witch sighs.");
							return true;
							case 8:
								player.getDialogue().sendNpcChat("Yes, improve your magic...", CONTENT);
							return true;
							case 9:
								player.getDialogue().sendNpcChat("Do you have no sense of drama?", CONTENT);
							return true;
							case 10:
								player.getDialogue().sendOption("Yes I'd like to improve my magic.", "No I'm not interested.");
							return true;
							case 11:
								 switch(optionId) {
									 case 1:
										 player.getDialogue().sendPlayerChat("Yes I'd like to improve my magic.", CONTENT);
										 player.getDialogue().setNextChatId(12);
									 return true;
									 case 2 :
										 player.getDialogue().sendPlayerChat("No I'm not interested.", CONTENT);
										 player.getDialogue().endDialogue();
									 return true;
								 }
							 break;
							case 12:
								player.getDialogue().sendStatement("The witch sighs.");
							return true;
							case 13:
								player.getDialogue().sendNpcChat("Ok I'm going to make a potion to help bring out your","darker self.", CONTENT);
							return true;
							case 14:
								player.getDialogue().sendNpcChat("You will need certain ingredients.", CONTENT);
							return true;
							case 15:
								player.getDialogue().sendPlayerChat("What do i need?", CONTENT);
							return true;
							case 16:
								player.getDialogue().sendNpcChat("You need an eye of newt, a rat's tail, an onion... Oh","and a peice of burnt meat.", CONTENT);
							return true;
							case 17:
								player.getDialogue().sendPlayerChat("Great, I'll go and get them.", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(6, 1);
								QuestHandler.getQuests()[6].startQuest(player);
							return true;
						}
					break;
					case 1:
						switch(player.getDialogue().getChatId()) 
						{
							case 1:
								player.getDialogue().sendNpcChat("So have you found the things for the potion?", CONTENT);
				    			if(player.carryingItem(2146) && player.carryingItem(221) && player.carryingItem(1957) && player.carryingItem(300))
				    			{
				    				player.getDialogue().setNextChatId(5);
				    			}
				    			else
				    			{
				    				player.getDialogue().setNextChatId(2);
				    			}
							return true;
							case 2:
								player.getDialogue().sendPlayerChat("No, Not yet.", CONTENT);
							return true;
							case 3:
								player.getDialogue().sendNpcChat("Remember You need an eye of newt, a rat's tail, an onion","and a peice of burnt meat.", CONTENT);
							return true;
							case 4:
								player.getDialogue().sendPlayerChat("Ok, thanks.", CONTENT);
								player.getDialogue().endDialogue();
							return true;
							case 5:
								player.getDialogue().sendPlayerChat("Yes I have everything!", CONTENT);
							return true;
							case 6:
								player.getDialogue().sendNpcChat("Excellent, can I have them then?", CONTENT);
							return true;
							case 7:
								player.getDialogue().sendStatement("You pass the ingredients to Hetty and she puts them all into her","cauldron. Hetty closes her eyes and begins to chant. The cauldron","bubbles mysteriously.");
								player.getInventory().removeItem(new Item(2146,1));
								player.getInventory().removeItem(new Item(221,1));
								player.getInventory().removeItem(new Item(1957,1));
								player.getInventory().removeItem(new Item(300,1));
								player.setQuestStage(6, 2);
								player.getDialogue().setNextChatId(1);
	    					return true;
						}
					break;
					case 2:
						switch(player.getDialogue().getChatId()) 
						{
							case 1:
								player.getDialogue().sendPlayerChat("Well, is it ready?", CONTENT);
							return true;
							case 2:
								player.getDialogue().sendNpcChat("Ok, drink from the cauldron", CONTENT);
								player.getDialogue().endDialogue();
							return true;
						}
					break;
				}
			break;
			//witchs potion end
			// Diango the special item guy
			case 970:
				if(!player.hasItem(7927) && QuestHandler.questCompleted(player, 7))
				{
					switch(player.getDialogue().getChatId())
					{
						case 1:
							player.getDialogue().sendNpcChat("Hello, What can I do for you?", CONTENT);
							return true;
						case 2:
							player.getDialogue().sendPlayerChat("I've lost my easter ring","Can I have a new one?", CONTENT);
							return true;
						case 3:
							player.getDialogue().sendNpcChat("Sure here you go.", CONTENT);
							player.getDialogue().endDialogue();
							player.getInventory().addItem(new Item(7927,1));
							return true;
					}
				}
			break;
			case 10013 : // dragon shield smithing
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendStatement("You set to work trying to fix the ancient shield. It's seen some","heavy action and needs some serious work doing to it.");
					return true;
					case 2 :
						DragonShieldSmith.smithShield(player);
						player.getDialogue().resetDialogue();
						player.getActionSender().removeInterfaces();
					return true;
					case 3 :
						player.getDialogue().sendStatement("Even for an experience armourer it is not an easy task, but","eventually it is ready. You have restored the dragon square shield to","its former glory.");
						player.getDialogue().endDialogue();
					return true;
				}
			break;
			case 805 : //crafting master skillcape
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Hello, and welcome to the Crafting Guild. Accomplished","crafters from all over the land come here to use our","top notch workshops.", CONTENT);
						player.getDialogue().endDialogue();
					return true;
				}
			break;
		}
		if (player.getDialogue().getChatId() > 1) {
			player.getActionSender().removeInterfaces();
		}
		if (player.getDialogue().getDialogueId() > -1) {
			player.getDialogue().resetDialogue();
		}
		return false;
	}

}