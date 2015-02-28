package com.rs2.model.content.dialogue;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.BankPin;
import com.rs2.model.content.Shops;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.combat.util.Degrading;
import com.rs2.model.content.dungeons.Abyss;
import com.rs2.model.content.minigames.duelarena.RulesData;
import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.randomevents.TalkToEvent;
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
import com.rs2.model.content.minigames.fightcaves.FightCaves;
import com.rs2.model.content.minigames.magetrainingarena.MageTrainingDialogue;
import com.rs2.model.content.minigames.pestcontrol.PestControlRewardHandler;
import com.rs2.model.content.quests.Quest;
import com.rs2.model.content.quests.TheGrandTree;
import com.rs2.model.content.randomevents.impl.FreakyForester;
import com.rs2.model.content.skills.cooking.wetClayHandler;
import com.rs2.model.content.skills.firemaking.BarbarianSpirits;
import com.rs2.model.content.skills.runecrafting.TabHandler;
import com.rs2.model.content.skills.smithing.DragonfireShieldSmithing;
import com.rs2.model.content.skills.smithing.Smelting;
import com.rs2.model.content.skills.smithing.SmithBars;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.transport.MagicCarpet;

public class Dialogues {

	// Dialogue expressions
	public static final int HAPPY = 588, CALM = 589, CALM_CONTINUED = 590, CONTENT = 591, EVIL = 592, EVIL_CONTINUED = 593, DELIGHTED_EVIL = 594, ANNOYED = 595, DISTRESSED = 596, DISTRESSED_CONTINUED = 597, NEAR_TEARS = 598, SAD = 599, DISORIENTED_LEFT = 600, DISORIENTED_RIGHT = 601, UNINTERESTED = 602, SLEEPY = 603, PLAIN_EVIL = 604, LAUGHING = 605, LONGER_LAUGHING = 606, LONGER_LAUGHING_2 = 607, LAUGHING_2 = 608, EVIL_LAUGH_SHORT = 609, SLIGHTLY_SAD = 610, VERY_SAD = 611, OTHER = 612, NEAR_TEARS_2 = 613, ANGRY_1 = 614, ANGRY_2 = 615, ANGRY_3 = 616, ANGRY_4 = 617;
	//npc id, skill id, untrimmed cape id, shop id
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
        
        public static boolean checkTrim(Player player) {
            int x = 0;
            for(int i = 0; i < 21; i++) {
                if (player.getSkill().getLevel()[i] == 99)
                    x++;
            }  
            return x >= 2;
        }
        public static void trimCape(Player player, int capeId) {
            if(player.getInventory().playerHasItem(capeId)) {
                player.getInventory().removeItem(new Item(capeId));
                player.getInventory().addItem(new Item(capeId+1));
            }
        }
	
	public static final int[][] skillCapeData = { {4288, Skill.ATTACK, 9747, 172}, {4297, Skill.STRENGTH, 9750, 173}, {705, Skill.DEFENCE, 9753, 174}, {682, Skill.RANGED, 9756, 175}, {1658, Skill.MAGIC, 9762, 177}, {437, Skill.AGILITY, 9771, 180}, {2270, Skill.THIEVING, 9777, 182}, {575, Skill.FLETCHING, 9783, 184}, {3295, Skill.MINING, 9792, 186}, {308, Skill.FISHING, 9798, 188}, {847, Skill.COOKING, 9801, 189}, {4946, Skill.FIREMAKING, 9804, 190}, {4906, Skill.WOODCUTTING, 9807, 191}, {3299, Skill.FARMING, 9810, 192} };
	public static int skillCapeData(Player player, int npc, int index) {
	    for(int i = 0; i < skillCapeData.length; i++) {
		if(skillCapeData[i][0] == npc)
		    return skillCapeData[i][index];
	    }
	    return 0;
	}
	public static int skillCapeSkillForId(int itemId) {
	    for(int i = 0; i < skillCapeData.length; i++) {
		if(skillCapeData[i][2] == itemId) {
		    return skillCapeData[i][1];
		}
	    }
	    return 0;
	}
	public static boolean isSkillCape(int itemId) {
	    for(int i = 0; i < skillCapeData.length; i++) {
		if(skillCapeData[i][2] == itemId) {
		    return true;
		}
	    }
	    return false;
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
		
		for(Quest q : QuestHandler.getQuests()) {
		    if(q.sendDialogue(player, id, chatId, optionId, npcChatId)) {
			return true;
		    }
		}
		if(TabHandler.sendDialogue(player, id, chatId, optionId, npcChatId)) {
		    return true;
		}
		if(PestControlRewardHandler.sendDialogue(player, id, chatId, optionId, npcChatId)) {
		    return true;
		}
		if(wetClayHandler.sendDialogue(player, id, chatId, optionId, npcChatId)) {
		    return true;
		}
		if(BarbarianSpirits.sendDialogue(player, id, chatId, optionId, npcChatId)) {
		    return true;
		}
		if(Constants.DEGRADING_ENABLED && Degrading.sendDialogue(player, id, chatId, optionId, npcChatId)) {
		    return true;
		}
		if(MageTrainingDialogue.sendDialogue(player, id, chatId, optionId, npcChatId)) {
		    return true;
		}
		if(player.getRandomHandler().getCurrentEvent() != null) {
			if(player.getRandomHandler().getCurrentEvent().sendDialogue(id, chatId, optionId, npcChatId)) {
				return true;
			}
		}
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
			case 159: //gnome child
				switch(player.getDialogue().getChatId()) {
				    case 1:
					player.getDialogue().sendPlayerChat("Can you give me some advice, little gnome child?", CONTENT);
					return true;
				    case 2:
					player.getDialogue().sendNpcChat("The winds of change blow leaves of feeling in the wind.", "The seasons of life change just as the seasons do.", "Worry not what tomorrow brings..." ,"...for it will be brought tomorrow.", CALM);
					return true;
				    case 3:
					player.getDialogue().sendPlayerChat("Thank you.", HAPPY);
					player.getDialogue().endDialogue();
					return true;
				}
			return false;
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
								player.getDialogue().sendNpcChat("Aye, not too bad thank you. Lovely weather", "in vscape this fine day.", HAPPY);
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
								player.getDialogue().sendNpcChat("Aye, not too bad thank you. Lovely weather", "in vscape this fine day.", HAPPY);
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
			case 12980:
			case 12981:
			case 12982:
			case 12983:
			case 12984:
			case 12985:
			case 12986:
			case 12987:
			case 12988: //Flowers
			    switch (player.getDialogue().getChatId()) {
				case 1:
				    player.getDialogue().sendOption("Pick the flower.", "Keep the flower.");
				    return true;
				case 2:
				    switch (optionId) {
					case 1:
					    player.getUpdateFlags().sendAnimation(827);
					    player.getUpdateFlags().sendFaceToDirection(new Position(player.getMithrilSeeds().getLastFlowerX(), player.getMithrilSeeds().getLastFlowerY(), player.getPosition().getZ()));
					    player.getInventory().addItemOrDrop(new Item(player.getMithrilSeeds().itemForObject(id - 10000)));
					    player.getActionSender().sendMessage("You pick the flowers.");
					    player.getMithrilSeeds().removeFlowerObject(ObjectHandler.getInstance().getObject(id - 10000, player.getMithrilSeeds().getLastFlowerX(), player.getMithrilSeeds().getLastFlowerY(), player.getPosition().getZ()));
					    break;
					case 2:
					    break;
				    }
				    break;
			    }
			    break;
			case 166 : //banker
			case 494 :
			case 495 :
			case 496 :
			case 2619 :
			case 4296 : //jade warriors guild banker
			case 4446 : //fairy bankers
			case 1702: //ghost banker
			case 3046: //Nardah banker
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
						player.getBankManager().openBank();
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
								int i = Misc.randomMinusOne(Barrows.AREAS.length);
								Position position = MinigameAreas.randomPosition(Barrows.AREAS[i]);
								player.teleport(position);
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
                            return true;
                        case 961 :
                            switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Hey, what's up? you can call me " + NpcDefinition.forId(player.getClickId()).getName(), "my mission here is to take care of those players", "who would need some help to cure their wounds", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendNpcChat("So, anything I can do for someone like you?", CONTENT);
						return true;
					case 3 :
                                            if(player.getSkill().getLevel()[Skill.HITPOINTS] == 99 && !checkTrim(player))
						player.getDialogue().sendOption("Yes, I would like you to heal me please.", "No, I am just playing around.", "I want my skillcape!");
                                            else if(player.getSkill().getLevel()[Skill.HITPOINTS] == 99 && checkTrim(player))
						player.getDialogue().sendOption("Yes, I would like you to heal me please.", "No, I am just playing around.", "I want my skillcape!", "Can you trim my skillcape?");
                                            else
                                                player.getDialogue().sendOption("Yes, I would like you to heal me please.", "No, I am just playing around.");
						return true;
					case 4 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Yes, I would like you to heal me please.", CONTENT);
								return true;
                                                        case 2: 
                                                                player.getDialogue().sendPlayerChat("No, I am just playing around.", CONTENT);
								return true;
							case 3:
                                                                player.getDialogue().sendPlayerChat("I want my skillcape!", ANGRY_1);
                                                                player.getDialogue().setNextChatId(6);
                                                                return true;
                                                        case 4:
                                                                player.getDialogue().sendPlayerChat("Can you trim my skillcape?", CONTENT);
                                                                player.getDialogue().setNextChatId(9);
                                                                return true;
                                                }	
						return true;	
                                        case 6:
                                            player.getDialogue().sendNpcChat("You've earned it.", CONTENT);
                                            return true;
                                        case 7:
                                            ShopManager.openShop(player, 179);
                                            player.getDialogue().dontCloseInterface();
                                            return true;
                                        case 9:
                                            player.getDialogue().sendNpcChat("Sure, but this is irreversible!", "However, you can always buy another untrimmed cape.", CONTENT);
                                            return true;
                                        case 10:
                                            if(player.getInventory().playerHasItem(9768)) {
                                            player.getDialogue().sendNpcChat("Here you are.", CONTENT);
                                            trimCape(player, 9768);
                                            }
                                            else {
                                                player.getDialogue().sendPlayerChat("I guess I don't have the cape on me.", VERY_SAD);
                                                player.getDialogue().endDialogue();
                                            }
                                            return true;
					}
                                       break;
			case 2617: //fight caves entrance guide
			    switch (player.getDialogue().getChatId()) {
				case 1:
				    player.getDialogue().sendNpcChat("Hello, " +player.getUsername()+ "! Up for a challenge?", CONTENT);
				    return true;
				case 2:
				    player.getDialogue().sendPlayerChat("What sort of challenge?", CONTENT);
				    return true;
				case 3:
				    player.getDialogue().sendNpcChat("A trial of arms of course! Enter this cave here and", "you'll be faced by various powerful and unique monsters", "the Tzhaar people have captured over the years.", CONTENT);
				    return true;
				case 4:
				    player.getDialogue().sendNpcChat("There is a grand reward for those who survive", "through all the waves of enemies! The legendary", "cape of fire, crafted deep in the heart of this volcano.", CONTENT);
				    return true;
				case 5:
				    player.getDialogue().sendPlayerChat("What happens if I die?", CONTENT);
				    return true;
				case 6:
				    player.getDialogue().sendNpcChat("We'll be watching you adventurer, if you die", "we'll pull you out so you don't lose any precious items.", CONTENT);
				    return true;
				case 7:
				    player.getDialogue().sendPlayerChat("Okay, dying is safe, survive to the end and I get", "a legendary cape... Is that it?", CONTENT);
				    return true;
				case 8:
				    player.getDialogue().sendNpcChat("We'll also award you some Tokkul for your bravery.", "The reward will increase based on how close", "you come to defeating all the waves of enemies!", CONTENT);
				    return true;
				case 9:
				    player.getDialogue().sendNpcChat("So, what do you say? Ready to enter the Fight Caves?", CONTENT);
				    return true;
				case 10:
				    player.getDialogue().sendOption("Yes!", "No.");
				    return true;
				case 11:
				    switch(optionId) {
					case 1:
					    FightCaves.enterCave(player);
					    player.getDialogue().endDialogue();
					    return true;
					case 2:
					    player.getDialogue().sendPlayerChat("No, I'm afraid I'm not quite ready yet.", CONTENT);
					    player.getDialogue().endDialogue();
					    return true;
				    }
			    }
			break;
			case 1918: //archaeologist
			    switch (player.getDialogue().getChatId()) {
				case 1:
				    player.getDialogue().sendNpcChat("It's amazing! I've never seen anything like it!", HAPPY);
				    player.getDialogue().endDialogue();
				    return true;
			    }
			break;
			case 2725: //otto godbless
			    switch (player.getDialogue().getChatId()) {
				case 1:
				    player.getDialogue().sendNpcChat("Careful adventurer! Up these steps are ancient, powerful", "monsters. Are you sure you want to go up there?", CONTENT);
				    return true;
				case 2:
				    player.getDialogue().sendOption("Yes.", "No.", "Ancient monsters?");
				    return true;
				case 3:
				    switch(optionId) {
					case 1:
					    if(player.getPosition().getY() > 9590) {
						player.fadeTeleport(new Position(2643, 9594, 2));
						player.getDialogue().endDialogue();
						return true;
					    }
					    else {
						player.fadeTeleport(new Position(2637, 9510, 2));
						player.getDialogue().endDialogue();
						return true;
					    }
					case 2:
					    player.getDialogue().sendPlayerChat("No, I'm not ready yet.", CONTENT);
					    player.getDialogue().endDialogue();
					    return true;
					case 3:
					    player.getDialogue().sendPlayerChat("Ancient monsters?", CONTENT);
					    return true;
				    }
				case 4:
				    player.getDialogue().sendNpcChat("Yes my friend! Recently a team of archaeologists from", "the Dig Site west of Varrock uncovered an entire extra", "cavern down in this dungeon. When they broke through", "the outer layer, they found monsters that seemingly", CONTENT);
				    return true;
				case 5:
				    player.getDialogue().sendNpcChat("have been locked away for centuries.", "I would give a few gold to bet that the monsters are not", "quite happy. But, I would also wager that the monsters", CONTENT);
				    return true;
				case 6:
				    player.getDialogue().sendNpcChat("are a part of a larger secret!", "Who knows what treasures they could hold!", HAPPY);
				    return true;
				case 7:
				    player.getDialogue().sendPlayerChat("So, let me guess. You want me to kill them for you?", CONTENT);
				    return true;
				case 8:
				    player.getDialogue().sendNpcChat("Oh, quite the opposite my friend. I warn passerbys of", "the danger, hoping they will stay away! I fear these", "monsters could be devastating, should they escape.", CONTENT);
				    return true;
				case 9:
				    player.getDialogue().sendPlayerChat("Alright, well, I'll make sure none follow me out. Thanks.", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
			    }
			break;
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
				case 1281: //Sigli & spined armor
                                    switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Hello.", CONTENT);
                                        return true;
                                        case 2 :
                                                player.getDialogue().sendPlayerChat("Hi.", "Could you craft me Spined Armor?", CONTENT);
                                        return true;
                                        case 3 :
                                                player.getDialogue().sendNpcChat("Sure, which piece would you like made?", CONTENT);
                                        return true;
                                        case 4 :
                                            player.getDialogue().sendOption("Spined Helm", "Spined Body", "Spined Chaps", "Nevermind.");
						return true;
					case 5 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendNpcChat("I'll need 5000 coins,","a dagannoth hide,", "and a circular hide.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("I'll need 10,000 coins,","3 dagannoth hides,", "and a flattened hide.", CONTENT);
                                                                player.getDialogue().setNextChatId(8);
								return true;
                                                        case 3:
                                                                player.getDialogue().sendNpcChat("I'll need 7,500 coins,", "2 dagannoth hides,", "and a stretched hide.", CONTENT);
                                                                player.getDialogue().setNextChatId(10);
								return true;
                                                        case 4:
                                                                player.getDialogue().sendPlayerChat("Nevermind.", SLIGHTLY_SAD);
                                                                player.getDialogue().endDialogue();
                                                                return true;
                                                }
                                        case 6 :
                                            if( player.getInventory().playerHasItem(6155) && player.getInventory().playerHasItem(6169)
                                                && player.getInventory().playerHasItem(995, 5000) ) {
                                            player.getDialogue().sendPlayerChat("Here you are.", CONTENT);
                                            player.getInventory().removeItem(new Item(6155));
                                            player.getInventory().removeItem(new Item(6169));
                                            player.getInventory().removeItem(new Item(995, 5000));
                                            return true;
                                            }
                                            else {
                                                player.getDialogue().sendPlayerChat("I don't seem to have the items...", VERY_SAD);
                                                player.getDialogue().endDialogue();
                                                return true;
                                            }
                                        case 7 :
                                            player.getDialogue().sendNpcChat("And here's your crafted piece.", CONTENT);
                                            player.getInventory().addItem(new Item(6131));
                                            player.getDialogue().endDialogue();
                                            return true;
                                        case 8 :
                                            if( player.getInventory().playerHasItem(6155, 3) && player.getInventory().playerHasItem(6171)
                                                && player.getInventory().playerHasItem(995, 10000) ) {
                                            player.getDialogue().sendPlayerChat("Here you are.", CONTENT);
                                            player.getInventory().removeItem(new Item(6155, 3));
                                            player.getInventory().removeItem(new Item(6171));
                                            player.getInventory().removeItem(new Item(995, 10000));
                                            return true;
                                            }
                                            else {
                                                player.getDialogue().sendPlayerChat("I don't seem to have the items...", VERY_SAD);
                                                player.getDialogue().endDialogue();
                                                return true;
                                            }
                                        case 9:
                                            player.getDialogue().sendNpcChat("And here's your crafted piece.", CONTENT);
                                            player.getInventory().addItem(new Item(6133));
                                            player.getDialogue().endDialogue();
                                            return true;
                                        case 10 :
                                            if( player.getInventory().playerHasItem(6155, 2) && player.getInventory().playerHasItem(6173)
                                                && player.getInventory().playerHasItem(995, 7500) ) {
                                            player.getDialogue().sendPlayerChat("Here you are.", CONTENT);
                                            player.getInventory().removeItem(new Item(6155, 2));
                                            player.getInventory().removeItem(new Item(6173));
                                            player.getInventory().removeItem(new Item(995, 7500));
                                            return true;
                                            }
                                            else {
                                                player.getDialogue().sendPlayerChat("I don't seem to have the items...", VERY_SAD);
                                                player.getDialogue().endDialogue();
                                                return true;
                                            }
                                        case 11:
                                            player.getDialogue().sendNpcChat("And here's your crafted piece.", CONTENT);
                                            player.getInventory().addItem(new Item(6135));
                                            player.getDialogue().endDialogue();
                                            return true;
                                    }
                                    case 1303: //Skulgrimen & rockshell armor
                                    switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Hello.", CONTENT);
                                        return true;
                                        case 2 :
                                                player.getDialogue().sendPlayerChat("Hi.", "Could you craft me Rock-Shell Armor?", CONTENT);
                                        return true;
                                        case 3 :
                                                player.getDialogue().sendNpcChat("Sure, which piece would you like made?", CONTENT);
                                        return true;
                                        case 4 :
                                            player.getDialogue().sendOption("Rock-Shell Helm", "Rock-Shell Plate", "Rock-Shell Legs", "Nevermind.");
						return true;
					case 5 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendNpcChat("I'll need 5000 coins,","a dagannoth hide,", "and a rock-shell chunk.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("I'll need 10,000 coins,","3 dagannoth hides,", "and a rock-shell shard.", CONTENT);
                                                                player.getDialogue().setNextChatId(8);
								return true;
                                                        case 3:
                                                                player.getDialogue().sendNpcChat("I'll need 7,500 coins,", "2 dagannoth hides,", "and a rock-shell splinter.", CONTENT);
                                                                player.getDialogue().setNextChatId(10);
								return true;
                                                        case 4:
                                                                player.getDialogue().sendPlayerChat("Nevermind.", SLIGHTLY_SAD);
                                                                player.getDialogue().endDialogue();
                                                                return true;
                                                }
                                        case 6 :
                                            if( player.getInventory().playerHasItem(6155) && player.getInventory().playerHasItem(6157)
                                                && player.getInventory().playerHasItem(995, 5000) ) {
                                            player.getDialogue().sendPlayerChat("Here you are.", CONTENT);
                                            player.getInventory().removeItem(new Item(6155));
                                            player.getInventory().removeItem(new Item(6157));
                                            player.getInventory().removeItem(new Item(995, 5000));
                                            return true;
                                            }
                                            else {
                                                player.getDialogue().sendPlayerChat("I don't seem to have the items...", VERY_SAD);
                                                player.getDialogue().endDialogue();
                                                return true;
                                            }
                                        case 7 :
                                            player.getDialogue().sendNpcChat("And here's your crafted piece.", CONTENT);
                                            player.getInventory().addItem(new Item(6128));
                                            player.getDialogue().endDialogue();
                                            return true;
                                        case 8 :
                                            if( player.getInventory().playerHasItem(6155, 3) && player.getInventory().playerHasItem(6159)
                                                && player.getInventory().playerHasItem(995, 10000) ) {
                                            player.getDialogue().sendPlayerChat("Here you are.", CONTENT);
                                            player.getInventory().removeItem(new Item(6155, 3));
                                            player.getInventory().removeItem(new Item(6159));
                                            player.getInventory().removeItem(new Item(995, 10000));
                                            return true;
                                            }
                                            else {
                                                player.getDialogue().sendPlayerChat("I don't seem to have the items...", VERY_SAD);
                                                player.getDialogue().endDialogue();
                                                return true;
                                            }
                                        case 9:
                                            player.getDialogue().sendNpcChat("And here's your crafted piece.", CONTENT);
                                            player.getInventory().addItem(new Item(6129));
                                            player.getDialogue().endDialogue();
                                            return true;
                                        case 10 :
                                            if( player.getInventory().playerHasItem(6155, 2) && player.getInventory().playerHasItem(6161)
                                                && player.getInventory().playerHasItem(995, 7500) ) {
                                            player.getDialogue().sendPlayerChat("Here you are.", CONTENT);
                                            player.getInventory().removeItem(new Item(6155, 2));
                                            player.getInventory().removeItem(new Item(6161));
                                            player.getInventory().removeItem(new Item(995, 7500));
                                            return true;
                                            }
                                            else {
                                                player.getDialogue().sendPlayerChat("I don't seem to have the items...", VERY_SAD);
                                                player.getDialogue().endDialogue();
                                                return true;
                                            }
                                        case 11:
                                            player.getDialogue().sendNpcChat("And here's your crafted piece.", CONTENT);
                                            player.getInventory().addItem(new Item(6130));
                                            player.getDialogue().endDialogue();
                                            return true;
                                    }
                                break;
                                case 1288: //Peer the seer & skeletal armor
                                    switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Hello.", CONTENT);
                                        return true;
                                        case 2 :
                                                player.getDialogue().sendPlayerChat("Hi.", "Could you craft me Skeletal Armor?", CONTENT);
                                        return true;
                                        case 3 :
                                                player.getDialogue().sendNpcChat("Sure, which piece would you like made?", CONTENT);
                                        return true;
                                        case 4 :
                                            player.getDialogue().sendOption("Skeletal Helm", "Skeletal Top", "Skeletal Bottoms", "Nevermind.");
						return true;
					case 5 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendNpcChat("I'll need 5000 coins,","a dagannoth hide,", "and a Wallasalki skull piece.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("I'll need 10,000 coins,","3 dagannoth hides,", "and a Wallasalki ribcage piece.", CONTENT);
                                                                player.getDialogue().setNextChatId(8);
								return true;
                                                        case 3:
                                                                player.getDialogue().sendNpcChat("I'll need 7,500 coins,", "2 dagannoth hides,", "and a Wallasalki fibula piece.", CONTENT);
                                                                player.getDialogue().setNextChatId(10);
								return true;
                                                        case 4:
                                                                player.getDialogue().sendPlayerChat("Nevermind.", SLIGHTLY_SAD);
                                                                player.getDialogue().endDialogue();
                                                                return true;
                                                }
                                        case 6 :
                                            if( player.getInventory().playerHasItem(6155) && player.getInventory().playerHasItem(6163)
                                                && player.getInventory().playerHasItem(995, 5000) ) {
                                            player.getDialogue().sendPlayerChat("Here you are.", CONTENT);
                                            player.getInventory().removeItem(new Item(6155));
                                            player.getInventory().removeItem(new Item(6163));
                                            player.getInventory().removeItem(new Item(995, 5000));
                                            return true;
                                            }
                                            else {
                                                player.getDialogue().sendPlayerChat("I don't seem to have the items...", VERY_SAD);
                                                player.getDialogue().endDialogue();
                                                return true;
                                            }
                                        case 7 :
                                            player.getDialogue().sendNpcChat("And here's your crafted piece.", CONTENT);
                                            player.getInventory().addItem(new Item(6137));
                                            player.getDialogue().endDialogue();
                                            return true;
                                        case 8 :
                                            if( player.getInventory().playerHasItem(6155, 3) && player.getInventory().playerHasItem(6165)
                                                && player.getInventory().playerHasItem(995, 10000) ) {
                                            player.getDialogue().sendPlayerChat("Here you are.", CONTENT);
                                            player.getInventory().removeItem(new Item(6155, 3));
                                            player.getInventory().removeItem(new Item(6165));
                                            player.getInventory().removeItem(new Item(995, 10000));
                                            return true;
                                            }
                                            else {
                                                player.getDialogue().sendPlayerChat("I don't seem to have the items...", VERY_SAD);
                                                player.getDialogue().endDialogue();
                                                return true;
                                            }
                                        case 9:
                                            player.getDialogue().sendNpcChat("And here's your crafted piece.", CONTENT);
                                            player.getInventory().addItem(new Item(6139));
                                            player.getDialogue().endDialogue();
                                            return true;
                                        case 10 :
                                            if( player.getInventory().playerHasItem(6155, 2) && player.getInventory().playerHasItem(6167)
                                                && player.getInventory().playerHasItem(995, 7500) ) {
                                            player.getDialogue().sendPlayerChat("Here you are.", CONTENT);
                                            player.getInventory().removeItem(new Item(6155, 2));
                                            player.getInventory().removeItem(new Item(6167));
                                            player.getInventory().removeItem(new Item(995, 7500));
                                            return true;
                                            }
                                            else {
                                                player.getDialogue().sendPlayerChat("I don't seem to have the items...", VERY_SAD);
                                                player.getDialogue().endDialogue();
                                                return true;
                                            }
                                        case 11:
                                            player.getDialogue().sendNpcChat("And here's your crafted piece.", CONTENT);
                                            player.getInventory().addItem(new Item(6141));
                                            player.getDialogue().endDialogue();
                                            return true;
                                    }
                                break;
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
								Sailing.sailShip(player, ShipRoute.PORT_SARIM_TO_ENTRANA, id);
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
							Sailing.sailShip(player, ShipRoute.ENTRANA_TO_PORT_SARIM, id);
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
						player.getDialogue().sendOption("Burthorpe Games Room", "Pest Control", "Nowhere");
						return true;
					case 2 :
						switch(optionId) {
							case 1:
								JewelleryTeleport.teleport(player, Teleportation.GAMES_ROOM);
								break;
							case 2:
								JewelleryTeleport.teleport(player, Teleportation.PEST_CONTROL);
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
			case 10200 : //blurite smelt
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendOption("1", "5", "10", "28");
						return true;
					case 2 :
						switch(optionId) {
							case 1:
								if(player.getInventory().playerHasItem(668, 1)) {
								    player.getInventory().removeItem(new Item(668, 1));
								    player.getInventory().addItem(new Item(9467, 1));
								    player.getActionSender().sendMessage("You smelt the blurite ore in the furnace.");
								    player.getActionSender().sendMessage("You retrieve a blurite bar from the furnace.");
								    player.getUpdateFlags().sendAnimation(899);
								    player.getActionSender().sendSound(469, 0, 0);
								    break;
								}
							case 2:
								player.setOldItem(9467);
								Smelting.smeltBar(player, 5);
								break;
							case 3:
								player.setOldItem(9467);
								Smelting.smeltBar(player, 10);
								break;
							case 4:
								player.setOldItem(9467);
								Smelting.smeltBar(player, 28);
								break;
						}
						break;
				}
				break;
			case 10201 : //blurite smith
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendOption("Blurite Bolts", "Blurite Limbs");
						return true;
					case 2 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendOption("1", "5", "10", "28");
								return true;
							case 2:
								player.getDialogue().sendOption("1", "5", "10", "28");
								player.getDialogue().setNextChatId(5);
								return true;
						}
					case 3 :
						switch(optionId) {
							case 1:
								SmithBars.startSmithingBlurite(player, 9376, 1);
								break;
							case 2:
								SmithBars.startSmithingBlurite(player, 9376, 5);
								break;
							case 3:
								SmithBars.startSmithingBlurite(player, 9376, 10);
								break;
							case 4:
								SmithBars.startSmithingBlurite(player, 9376, 28);
								break;
						}
					break;
					case 5 :
						switch(optionId) {
							case 1:
								SmithBars.startSmithingBlurite(player, 9422, 1);
								break;
							case 2:
								SmithBars.startSmithingBlurite(player, 9422, 5);
								break;
							case 3:
								SmithBars.startSmithingBlurite(player, 9422, 10);
								break;
							case 4:
								SmithBars.startSmithingBlurite(player, 9422, 28);
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
			case 10015 : //skills
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendOption("Fishing Guild", "Mining Guild", "Crafting Guild", "Cooking Guild", "Nowhere");
						return true;
					case 2 :
						switch(optionId) {
						case 1:
							JewelleryTeleport.teleport(player, Teleportation.FISHING_GUILD);
							break;
						case 2:
							JewelleryTeleport.teleport(player, Teleportation.MINING_GUILD);
							break;
						case 3:
							JewelleryTeleport.teleport(player, Teleportation.CRAFTING_GUILD);
							break;
						case 4:
							JewelleryTeleport.teleport(player, Teleportation.COOKING_GUILD);
							break;
						}
						break;
				}
				break;
			case 10016 : //nature amulet
				switch(player.getDialogue().getChatId()) {
				case  1 :
					player.getDialogue().sendOption("Cabbage Port", "Do Nothing");
					return true;
				case  2 :
					switch(optionId) {
					case 1:
						player.getUpdateFlags().sendAnimation(714);
                				player.getUpdateFlags().sendHighGraphic(301);
						player.getTeleportation().attemptTeleport(Teleportation.CABBAGE_PATCH);
						break;
					}
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
					    if(!Entity.inArea(player, 2914, 2929, 3050, 3065) && QuestHandler.questCompleted(player, 29)) {
						player.getDialogue().sendNpcChat("Would you like to fly somewhere on the glider?", CONTENT);
						return true;
					    } else if(Entity.inArea(player, 2914, 2929, 3050, 3065)) {
						player.getDialogue().sendNpcChat("I'm sorry Traveller, I can't fly anywhere with this.", SAD);
						player.getDialogue().endDialogue();
						return true;
					    } else {
						if(player.getQuestStage(29) == TheGrandTree.ESCAPE_FROM_ALCATRAZ) {
						    
						} else {
						    player.getDialogue().sendStatement("You must complete The Grand Tree to use the gnome gliders.");
						    player.getDialogue().endDialogue();
						    return true;
						}
					    }
					case 2 :
						player.getDialogue().sendOption("Sure.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								player.setStatedInterface("glider");
								player.getActionSender().sendInterface(802);
								player.getDialogue().dontCloseInterface();
								break;
						}
						break;
				}
				break;
			/**case 510 : //hajedy
				switch(player.getDialogue().getChatId()) {
					case 1 :
					    if(player.getInventory().playerHasItem(new Item(431))) {
								player.setStatedInterface("glider");
								player.getActionSender().sendInterface(802);
								player.getDialogue().dontCloseInterface();
								break;
						}
						break;
				}
				break;
			*/
			case 510 : //hajedy
				switch(player.getDialogue().getChatId()) {
					case 1 :
					    if(player.getInventory().playerHasItem(new Item(431))) {
						player.getDialogue().sendNpcChat("I see you're carrying some Karamjan Rum.", "That'll cost you extra to smuggle. 1000 gold up front.", CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					    }
					    else {
						player.getDialogue().sendNpcChat("Would you like to travel to Shilo village?", "It will only cost you 10gp.", CONTENT);
						return true;
					    }
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
					case 4:
					    player.getDialogue().sendOption("Yes please.", "No thanks.");
					    return true;
					case 5:
					    switch(optionId) {
						case 1:
						    Travel.startTravel(player, Route.BRIMHAVEN_TO_SHILO_SMUGGLE);
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
								Sailing.sailShip(player, ShipRoute.RELLEKKA_TO_WATERBIRTH, id);
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
						player.getDialogue().sendOption("Yes.", "No.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								Sailing.sailShip(player, ShipRoute.WATERBIRTH_TO_RELLEKKA, id);
								player.getDialogue().dontCloseInterface();
								break;
						}
						break;
				}
				break;
			case 3791 : // pc squire travel back
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Would you like to sail back to Port Sarim?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes.", "No.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								Sailing.sailShip(player, ShipRoute.PEST_CONTROL_TO_PORT_SARIM, id);
								player.getDialogue().dontCloseInterface();
								break;
						}
						break;
				}
				break;
			case 859 :
			    switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("WOULD HUMAN LIKE THROWN OVER?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("YES!", "NO!");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								if(player.getPosition().getX() <= 2566)
								    MagicCarpet.fly(player, new Position(2571, 3030, 0));
								else if(player.getPosition().getX() > 2566)
								    MagicCarpet.fly(player, new Position(2566, 3022, 0));
								break;
						}
						break;
				}
				break;
			case 904 :
			    switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Would you like a God Staff?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes!", "No.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								if(player.getMageArenaStage() >= 2) {
								    player.getDialogue().sendNpcChat("Which staff would you like?", CONTENT);
								    return true;
								}
								else {
								    player.getDialogue().sendNpcChat("You still have to defeat Kolodion!", CONTENT);
								    player.getDialogue().endDialogue();
								    return true;
								}
							case 2:
							    player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
							    player.getDialogue().endDialogue();
							    return true;
						}
					case 4 :
					    player.getDialogue().sendOption("Guthix.", "Zamorak.", "Saradomin.", "Nevermind.");
					    return true;
					case 5 :
						switch(optionId) {
							case 1:
							    if(player.getMageArenaStage() == 2) {
								player.getDialogue().sendPlayerChat("I've chosen Guthix!",HAPPY);
								player.getDialogue().setNextChatId(7);
								return true;
							    }
							    else if(player.getMageArenaStage() == 3)
							    player.getDialogue().sendNpcChat("That'll be 80,000 coins please.", CONTENT);
							    return true;
							case 2:
							    if(player.getMageArenaStage() == 2) {
								player.getDialogue().sendPlayerChat("I've chosen Zamorak!",HAPPY);
								player.getDialogue().setNextChatId(9);
								return true;
							    }
							    else if(player.getMageArenaStage() == 3) {
							    player.getDialogue().sendNpcChat("That'll be 80,000 coins please.", CONTENT);
							    player.getDialogue().setNextChatId(8);
							    return true;
							    }
							case 3:
							    if(player.getMageArenaStage() == 2) {
								player.getDialogue().sendPlayerChat("I've chosen Saradomin!",HAPPY);
								player.getDialogue().setNextChatId(11);
								return true;
							    }
							    else if(player.getMageArenaStage() == 3) {
							    player.getDialogue().sendNpcChat("That'll be 80,000 coins please.", CONTENT);
							    player.getDialogue().setNextChatId(10);
							    return true;
							    }
							case 4:
							    player.getDialogue().sendPlayerChat("Nevermind.", CONTENT);
							    player.getDialogue().endDialogue();
							    break;
						}
					return true;
					case 6:
					    if(player.getInventory().playerHasItem(995, 80000)) {
						player.getDialogue().sendPlayerChat("Here you are.", CONTENT);
						player.getInventory().removeItem(new Item(995, 80000));
						return true;
					    }
					    else {
						player.getDialogue().sendPlayerChat("Oh... I don't have the funds...", SAD);
						player.getDialogue().endDialogue();
						return true;
					    }
					case 7:
					    if(player.getInventory().canAddItem(new Item(2416))) {
						player.getDialogue().sendNpcChat("And here you are!", CONTENT);
						player.getDialogue().endDialogue();
						player.getInventory().addItem(new Item(2416));
						player.setMageArenaStage(3);
						return true;
					    }
					    else {
						player.getDialogue().sendNpcChat("Your inventory is full!", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					    }
					case 8:
					    if(player.getInventory().playerHasItem(995, 80000)) {
						player.getDialogue().sendPlayerChat("Here you are.", CONTENT);
						player.getInventory().removeItem(new Item(995, 80000));
						return true;
					    }
					    else {
						player.getDialogue().sendPlayerChat("Oh... I don't have the funds...", SAD);
						player.getDialogue().endDialogue();
						return true;
					    }
					case 9:
					    if(player.getInventory().canAddItem(new Item(2417))) {
						player.getDialogue().sendNpcChat("And here you are!", CONTENT);
						player.getDialogue().endDialogue();
						player.getInventory().addItem(new Item(2417));
						player.setMageArenaStage(3);
						return true;
					    }
					    else {
						player.getDialogue().sendNpcChat("Your inventory is full!", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					    }
					case 10:
					    if(player.getInventory().playerHasItem(995, 80000)) {
						player.getDialogue().sendPlayerChat("Here you are.", CONTENT);
						player.getInventory().removeItem(new Item(995, 80000));
						return true;
					    }
					    else {
						player.getDialogue().sendPlayerChat("Oh... I don't have the funds...", SAD);
						player.getDialogue().endDialogue();
						return true;
					    }
					case 11:
					    if(player.getInventory().canAddItem(new Item(2415))) {
						player.getDialogue().sendNpcChat("And here you are!", CONTENT);
						player.getDialogue().endDialogue();
						player.getInventory().addItem(new Item(2415));
						player.setMageArenaStage(3);
						return true;
					    }
					    else {
						player.getDialogue().sendNpcChat("Your inventory is full!", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					    } 
				    }
				break;
			case 905 : //kolodian
			    switch(player.getDialogue().getChatId()) {
					case 1 :
					    if(player.getMageArenaStage() == 0) {
						player.getDialogue().sendNpcChat("Care for a challenge?", "The reward is incomparable.", CONTENT);
						return true;
					    }
					    else if(player.getMageArenaStage() == 1) {
						player.getDialogue().sendNpcChat("Care for a re-match?", CONTENT);
						return true;
					    }
					    else if(player.getMageArenaStage() == 2) {
						player.getDialogue().sendNpcChat("You've done well, step into the pool.", "Beyond the pool is your prize.", CALM);
						player.getDialogue().endDialogue();
						return true;
					    }
					    else
						player.getDialogue().sendNpcChat("You're quite the mage to have beaten me!", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes!", "No.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
							    player.getDialogue().sendPlayerChat("Yes!", HAPPY);
							    return true;
							case 2:
							    player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
							    player.getDialogue().endDialogue();
							    return true;
						}
					case 4 :
					    if(player.getSkill().getLevel()[Skill.MAGIC] >= 60){
						player.getDialogue().sendNpcChat("You'll be fighting me in the Mage Arena...", "Are you up for it?", CONTENT);
						return true;
					    }
					    else {
						player.getDialogue().sendNpcChat("I'm sorry, I'm quite powerful, you'll need atleast", "level 60 Magic to fight me.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					    }
					case 5 :
						player.getDialogue().sendOption("Yes!", "No.");
						return true;
					case 6 :
						switch(optionId) {
							case 1:
							    player.getDialogue().sendPlayerChat("Yes!", HAPPY);
							    return true;
							case 2:
							    player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
							    player.getDialogue().endDialogue();
							    return true;
						}
					case 7 :
					    player.teleport(new Position(3097, 3934, 0));
					    player.setMageArenaStage(1);
					    Npc newKol = new Npc(907);
					    newKol.setSpawnPosition(new Position(3098, 3934, 0));
					    newKol.setPosition(new Position(3098, 3934, 0));
					    newKol.setNeedsRespawn(false);
					    newKol.setCombatDelay(5);
					    newKol.setPlayerOwner(player.getIndex());
					    World.register(newKol);
					    newKol.getUpdateFlags().sendForceMessage("Ready?");
					    CombatManager.attack(newKol, player);
					    break;
					
			    }
			break;
			case 918: //holy shit fuck off ned
			    switch(player.getDialogue().getChatId()) {
				case 1:
				    player.getDialogue().sendPlayerChat("Hello there!", CONTENT);
				    return true;
				case 2:
				    if (player.getEquipment().getItemContainer().get(Constants.HAT) != null && (player.getEquipment().getItemContainer().get(Constants.HAT).getDefinition().getName().toLowerCase().contains("boater")
					    || player.getEquipment().getItemContainer().get(Constants.HAT).getDefinition().getName().toLowerCase().contains("cavalier"))) {
					player.getDialogue().sendNpcChat("Nice hat, sir. I make wool hats sometimes!", "How can I help you?", HAPPY);
					return true;
				    } else {
					player.getDialogue().sendNpcChat("Why hello! I'm the wool man. I make wool things.", "How can I help you today?", HAPPY);
					return true;
				    }
				case 3:
				    player.getDialogue().sendPlayerChat("I need some rope.", CONTENT);
				    return true;
				case 4:
				    player.getDialogue().sendNpcChat("Some rope eh? I can do that!", "Either pay me, or hand over 4 balls of wool!", CONTENT);
				    return true;
				case 5:
				    player.getDialogue().sendOption("Pay Ned (18 gp)", "Give wool.", "Nevermind.");
				    return true;
				case 6:
				    switch (optionId) {
					case 1:
					    if (player.getInventory().playerHasItem(995, 18)) {
						player.getDialogue().sendPlayerChat("Here you are!", CONTENT);
						player.getInventory().removeItem(new Item(995, 18));
						return true;
					    } else {
						player.getDialogue().sendPlayerChat("I'm afraid I don't have the coin...", SAD);
						player.getDialogue().endDialogue();
						return true;
					    }
					case 2:
					    if (player.getInventory().playerHasItem(1759, 4)) {
						player.getDialogue().sendPlayerChat("Here you are!", CONTENT);
						player.getDialogue().setNextChatId(8);
						player.getInventory().removeItem(new Item(1759, 4));
						return true;
					    } else {
						player.getDialogue().sendPlayerChat("I'm afraid I don't have the balls...", SAD);
						player.getDialogue().endDialogue();
						return true;
					    }
					case 3:
					    player.getDialogue().sendPlayerChat("Nevermind.", SAD);
					    player.getDialogue().endDialogue();
					    return true;
				    }
				case 7:
				    player.getDialogue().sendNpcChat("And here's yer rope!", CONTENT);
				    player.getInventory().addItem(new Item(954));
				    player.getDialogue().endDialogue();
				    return true;
				case 8:
				    player.getDialogue().sendStatement("Ned works with the wool.");
				    return true;
				case 9:
				    player.getDialogue().sendNpcChat("And here's yer rope!", CONTENT);
				    player.getInventory().addItem(new Item(954));
				    player.getDialogue().endDialogue();
				    return true;    
			    }
			break;
			case 922: //aggie
			    switch(player.getDialogue().getChatId()) {
				case 1:
				    player.getDialogue().sendNpcChat("Ack ack ack! A brew, a brew!", "I shall make! Just for you! A log? A dog?", "Perhaps a fat hog? Ack ack ack!", LAUGHING);
				    return true;
				case 2:
				    player.getDialogue().sendPlayerChat("I would like to buy a dye please.", CONTENT);
				    return true;
				case 3:
				    player.getDialogue().sendNpcChat("Ack ack ack! I have many a dye", "for sale on the fly!", LAUGHING);
				    return true;
				case 4:
				    player.getDialogue().sendOption("Yellow Dye (2 onions + 5 coins)", "Red Dye (3 Redberries + 5 coins)", "Blue dye (2 Woad leaves + 5 coins)");
				    return true;
				case 5:
				    switch (optionId) {
					case 1:
					    if (player.getInventory().playerHasItem(1957, 2) && player.getInventory().playerHasItem(995, 5)) {
						player.getDialogue().sendGiveItemNpc("You hand Aggie the supplies for yellow dye.", new Item(1957));
						player.getInventory().removeItem(new Item(1957, 2));
						player.getInventory().removeItem(new Item(995, 5));
						return true;
					    } else {
						player.getDialogue().sendPlayerChat("I don't seem to have the supplies...", SAD);
						player.getDialogue().endDialogue();
						return true;
					    }
					case 2:
					    if (player.getInventory().playerHasItem(1951, 3) && player.getInventory().playerHasItem(995, 5)) {
						player.getDialogue().sendGiveItemNpc("You hand Aggie the supplies for red dye.", new Item(1951));
						player.getDialogue().setNextChatId(7);
						player.getInventory().removeItem(new Item(1951, 3));
						player.getInventory().removeItem(new Item(995, 5));
						return true;
					    } else {
						player.getDialogue().sendPlayerChat("I don't seem to have the supplies...", SAD);
						player.getDialogue().endDialogue();
						return true;
					    }
					case 3:
					    if (player.getInventory().playerHasItem(1793, 2) && player.getInventory().playerHasItem(995, 5)) {
						player.getDialogue().sendGiveItemNpc("You hand Aggie the supplies for blue dye.", new Item(1793));
						player.getDialogue().setNextChatId(8);
						player.getInventory().removeItem(new Item(1793, 2));
						player.getInventory().removeItem(new Item(995, 5));
						return true;
					    } else {
						player.getDialogue().sendPlayerChat("I don't seem to have the supplies...", SAD);
						player.getDialogue().endDialogue();
						return true;
					    }
				    }
				return true;
				case 6:
				    player.getDialogue().sendGiveItemNpc("Aggie hands you a yellow dye.", new Item(1765));
				    player.getInventory().addItem(new Item(1765));
				    player.getDialogue().endDialogue();
				    return true;
				case 7:
				    player.getDialogue().sendGiveItemNpc("Aggie hands you a red dye.", new Item(1763));
				    player.getInventory().addItem(new Item(1763));
				    player.getDialogue().endDialogue();
				    return true;
				case 8:
				    player.getDialogue().sendGiveItemNpc("Aggie hands you a blue dye.", new Item(1767));
				    player.getInventory().addItem(new Item(1767));
				    player.getDialogue().endDialogue();
				    return true;
			    }
			break;
			case 4286 : //harralak warriors guild
			    switch(player.getDialogue().getChatId()) {
				case 1 :
				    player.getDialogue().sendNpcChat("Welcome to the Warriors' Guild!", CONTENT);
				    return true;
				case 2 :
				    if(player.getPosition().getZ() == 0) {
					player.getDialogue().sendNpcChat("Come talk to me on the top floor for more information.", CONTENT);
					player.getDialogue().endDialogue();
				    }
				    else {
					player.getDialogue().sendOption("What is this place?", "What do I do here?");
				    }
				    return true;
				case 3 :
				    switch(optionId) {
					case 1 :
					    player.getDialogue().sendPlayerChat("What is this place?", CONTENT);
					    return true;
					case 2 :
					    player.getDialogue().sendPlayerChat("What do I do here?", CONTENT);
					    player.getDialogue().setNextChatId(7);
					    return true;
				    }
				case 4 :
				    player.getDialogue().sendNpcChat("This is the beginning of a guild for all great warriors!", "We haven't quite finished our design", "plans for our main building.", CONTENT);
				    return true;
				case 5 :
				    player.getDialogue().sendNpcChat("However, we are happy and functional here!", CONTENT);
				    return true;
				case 6 :
				    player.getDialogue().sendPlayerChat("What do I do here?", CONTENT);
				    return true;
				case 7 :
				    player.getDialogue().sendNpcChat("A fair question! The main goal of our guild is to", "honor those warriors mighty enough to enter!", "But, we do own a rare breed of monster.", CONTENT);
				    return true;
				case 8 :
				    player.getDialogue().sendNpcChat("We found these ancient cyclops living deep in a cave.", "Every once in a while you slay one that seemed", "to have enjoyed an ancient human snack!", CONTENT);
				    return true;
				case 9 :
				    player.getDialogue().sendPlayerChat("Erm, what does that mean for me?", CONTENT);
				    return true;
				case 10 :
				    player.getDialogue().sendNpcChat("Well, we believe the ancient humans who existed alongside", "the cyclops to have been a race of berzerkers.", CONTENT);
				    return true;
				case 11 :
				    player.getDialogue().sendPlayerChat("And...?", ANNOYED);
				    return true;
				case 12 :
				    player.getDialogue().sendNpcChat("These berzerkers used a weapon called a defender.", "It provides small defense bonuses...", "...but powerful attack bonuses!", CONTENT);
				    return true;
				case 13 :
				    player.getDialogue().sendPlayerChat("Well let me at these cyclops! I'll grind 1000 of them!", LAUGHING);
				    return true;
				case 14 :
				    player.getDialogue().sendNpcChat("Not so fast! We've devised a way for these", "rare cyclops to not be wiped out!", CONTENT);
				    return true;
				case 15 :
				    player.getDialogue().sendPlayerChat("And that is...?", CONTENT);
				    return true;
				case 16 :
				    player.getDialogue().sendNpcChat("A game of course!", "Inside the guild you will find 6 cages.", "We used our friends at the Magic Guild", "to help us enchant them.", CONTENT);
				    return true;
				case 17 :
				    player.getDialogue().sendNpcChat("Inside the cages, if you drop a full set of metal armor...", "...It will jump to life in front of you!", "Now the goal is to test your skills and fight these armors.", CONTENT);
				    return true;
				case 18 :
				    player.getDialogue().sendNpcChat("When you defeat a set of armor", "the ref in the cage will reward", "you with tokens for fighting well!", CONTENT);
				    return true;
				case 19 :
				    player.getDialogue().sendPlayerChat("And these tokens matter how?", ANNOYED);
				    return true;
				case 20 :
				    player.getDialogue().sendNpcChat("10 tokens is equal to one minute in the arena!", "As a precaution to our precious cyclops, you need to gather", "100 tokens minimum before entering.", CONTENT);
				    return true;
				case 21 :
				    player.getDialogue().sendPlayerChat("So a full helmet, platelegs, and platebody...", "...of any kind works?", CONTENT);
				    return true;
				case 22 :
				    player.getDialogue().sendNpcChat("All your basic metal armors through Runite!", CONTENT);
				    return true;
				case 23 :
				    player.getDialogue().sendPlayerChat("I'll get to earning tokens then! Thank you!", CONTENT);
				    return true;
				case 24 :
				    player.getDialogue().sendNpcChat("Enjoy the guild, warrior!", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
			    }
			break;   
			case 4285 : //ghommal warriors guild
			    switch(player.getDialogue().getChatId()) {
				case 1 :
				    player.getDialogue().sendNpcChat("Ghommal is busy building new challenges.", "You leave Ghommal alone!", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
			    }
			break;
			case 4299 : //ref warriors guild
			    switch(player.getDialogue().getChatId()) {
				case 1 :
				    player.getDialogue().sendNpcChat("You're fighting nice and clean, keep it up.", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
			    }
			break;
			case 4289 : //kamfreena warriors guild
			    switch(player.getDialogue().getChatId()) {
				case 1 :
				    player.getDialogue().sendPlayerChat("Hi.", CONTENT);
					return true;
				case 2 :
				    player.getDialogue().sendNpcChat("Hello adventurer! The cyclops are quite restless today!", LAUGHING);
				    player.getDialogue().setNextChatId(7);
				    return true;
				case 3 :
				    switch(optionId) {
					case 1 :
					    player.getDialogue().sendNpcChat("Oh you got a defender! Congratulations!", HAPPY);
					    return true;
					case 2 :
					    player.getDialogue().sendNpcChat("No luck with that defender? I'm sorry...", "...Maybe next time.", SAD);
					    player.getDialogue().endDialogue();
					    return true;
					case 3 :
					    player.getDialogue().sendNpcChat("A rune defender, very impressive. Good work.", CONTENT);
					    player.getDialogue().endDialogue();
					    return true;
				    }
				case 4 :
				    player.getDialogue().sendNpcChat("I've set up the cyclops to drop your next defender!", "Good luck with the next one!", HAPPY);
				    player.getDialogue().endDialogue();
				    return true;
				case 7 :
				    player.getDialogue().sendPlayerChat("Can you tell me which defender I'm on?", CONTENT);
				    return true;
				case 8 :
				    Item item = new Item(player.getDefender()+1);
				    String name = item.getDefinition().getName();
				    if(player.getDefender() != 8850) {
					player.getDialogue().sendNpcChat("Your next defender is the " + name + ".", CONTENT);
					player.getDialogue().endDialogue();
				    }
				    else {
					player.getDialogue().sendNpcChat("You already have the Rune defender, congratuluations!", HAPPY);
					player.getDialogue().endDialogue();
				    }
				    return true;
			    }
			break;
			case 492: //spirit of scorpius
			    switch (player.getDialogue().getChatId()) {
				case 1:
				    player.getDialogue().sendNpcChat("Yesssss mortal? What issss it you want?", CONTENT);
				    return true;
				case 2:
				    player.getDialogue().sendOption("Can you bless my symbols with the power of Zamorak?", "Hello.");
				    return true;
				case 3:
				    switch (optionId) {
					case 1:
					    player.getDialogue().sendPlayerChat("Can you bless my symbols with the power of Zamorak?", CONTENT);
					    return true;
					case 2:
					    player.getDialogue().sendPlayerChat("Hello.", CONTENT);
					    player.getDialogue().setNextChatId(7);
					    return true;
				    }
				case 4:
				    player.getDialogue().sendNpcChat("If that is your wissssh...So be it.", CONTENT);
				    return true;
				case 5:
				    if (!player.getInventory().playerHasItem(1722)) {
					player.getDialogue().sendNpcChat("You don't have any sssssymbols mortal.", "Careful not to wasssste a powerful ssspirit's time.", CONTENT);
				    } else {
					for (Item item : player.getInventory().getItemContainer().getItems()) {
					    if (item != null && item.getId() == 1722) {
						player.getInventory().replaceItemWithItem(new Item(1722), new Item(1724));
					    }
					}
					player.getDialogue().sendStatement("The Spirit of Scorpius enchants your symbols.");
				    }
				    player.getDialogue().endDialogue();
				    return true;
				case 7:
				    player.getDialogue().sendPlayerChat("Who are you?", CONTENT);
				    return true;
				case 8:
				    player.getDialogue().sendNpcChat("That isssss none of your businessss mortal.", "Leave my sssssight.", ANGRY_1);
				    player.getDialogue().endDialogue();
				    return true;
			    }
			    break;
			case 802 : //jered prayer master skillcape
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Hello brother, would you like me to bless all", "of your unblessed symbols?", CONTENT);
						return true;
					case 2 :
                                            if(player.getSkill().getLevel()[Skill.PRAYER] == 99 && !checkTrim(player)) {
						player.getDialogue().sendOption("Yes please.", "No thanks.", "I want my skillcape!");
						return true;
                                            }
                                            else if(player.getSkill().getLevel()[Skill.PRAYER] == 99 && checkTrim(player)) {
						player.getDialogue().sendOption("Yes please.", "No thanks.", "I want my skillcape!", "Can you trim my skillcape?");
						return true;
                                            }
                                            else
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
									player.getDialogue().sendNpcChat("There you go, you're welcome.", CONTENT);
								}
								player.getDialogue().endDialogue();
								return true;
                                                        case 2:
                                                            player.getDialogue().sendPlayerChat("No thanks.", CONTENT);
                                                            player.getDialogue().endDialogue();
							    return true;
                                                        case 3:
                                                            player.getDialogue().sendPlayerChat("I want my skillcape!", NEAR_TEARS);
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
                                            ShopManager.openShop(player, 176);
                                            player.getDialogue().dontCloseInterface();
                                            return true;
                                        case 8:
                                            player.getDialogue().sendNpcChat("Sure, but this is irreversible!", "However, you can always buy another untrimmed cape.", CONTENT);
                                            return true;
                                        case 9:
                                            if(player.getInventory().playerHasItem(9759)) {
                                                player.getDialogue().sendNpcChat("Here you are.", CONTENT);
                                                trimCape(player, 9759);
                                            }
                                            else {
                                                player.getDialogue().sendPlayerChat("I guess I don't have the cape on me.", VERY_SAD);
                                                player.getDialogue().endDialogue();
                                            }
                                        return true;
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
			 case 14736 : //staircase rogue castle
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendOption("Go up the stairs.", "Go down the stairs.", "Never mind.");
						return true;
					case 2 :
						switch(optionId) {
							case 1:
							    if(player.getPosition().getZ() == 1) {
								Ladders.climbLadder(player, new Position(player.getPosition().getX(), player.getPosition().getY(), 2));
								break;
							    } else if(player.getPosition().getZ() == 2) {
								Ladders.climbLadder(player, new Position(player.getPosition().getX(), player.getPosition().getY(), 3));
								break;
							    }
							break;
							case 2:
							    if(player.getPosition().getZ() == 1) {
								Ladders.climbLadder(player, new Position(player.getPosition().getX(), player.getPosition().getY(), 0));
								break;
							    } else if(player.getPosition().getZ() == 2) {
								Ladders.climbLadder(player, new Position(player.getPosition().getX(), player.getPosition().getY(), 1));
								break;
							    }
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
						ShopManager.openShop(player, 27);
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
					    if(player.getPosition().getX() > 2800) {
						player.getDialogue().sendNpcChat("Would you like to sail to Karamja?", "It will only cost you 30gp.", CONTENT);
						return true;
					    } else {
						player.getDialogue().sendNpcChat("Would you like to sail to Brimhaven?", "It will only cost you 30gp.", CONTENT);
						return true;
					    }
					case 2 :
						player.getDialogue().sendOption("Yes please.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
							    if(player.getPosition().getX() > 2800) {
								Sailing.sailShip(player, ShipRoute.PORT_SARIM_TO_KARAMJA, id);
								player.getDialogue().dontCloseInterface();
								break;
							    } else {
								Sailing.sailShip(player, ShipRoute.ARDOUGNE_TO_BRIMHAVEN, id);
								player.getDialogue().dontCloseInterface();
								break;
							    }
						}
						break;
				}
				break;
			case 380 : //customs officer
				switch(player.getDialogue().getChatId()) {
					case 1 :
					    if(player.getPosition().getX() > 2780) {
						player.getDialogue().sendNpcChat("Would you like to sail back to Port Sarim?", "It will cost 30gp.", "We'll also have to search you for illegal exports.", CONTENT);
						return true;
					    } else {
						player.getDialogue().sendNpcChat("Would you like to sail back to Ardougne?", "It will cost 30gp.", "We'll also have to search you for illegal exports.", CONTENT);
						return true;
					    }
					case 2 :
						player.getDialogue().sendOption("Yes please.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
							    if(player.getInventory().playerHasItem(431)) {
								player.getDialogue().sendNpcChat("I'm afraid the export of Karamjan Rum", "is prohibited at this date and time.", "Please do not try to violate our import laws.", ANNOYED);
								player.getDialogue().endDialogue();
								return true;
							    }
							    else if (player.getPosition().getX() > 2780) {
								Sailing.sailShip(player, ShipRoute.KARAMJA_TO_PORT_SARIM, id);
								player.getDialogue().dontCloseInterface();
								break;
							    } else {
								Sailing.sailShip(player, ShipRoute.BRIMHAVEN_TO_ARDOUGNE, id);
								player.getDialogue().dontCloseInterface();
								break;
							    }
						}
						break;
				}
				break;
			case FreakyForester.FREAKY_FORESTER : //lel
			    FreakyForester forester = player.getRandomHandler().getFreakyForester();
			    switch (player.getDialogue().getChatId()) {
				case 1:
				    if(!forester.isActive()) {
					player.getDialogue().sendNpcChat("You can leave using that portal over there.", CONTENT);
					player.getDialogue().endDialogue();
					return true;
				    }
				    else if (player.getInventory().playerHasItem(6178) && forester.isActive()) {
					player.getDialogue().sendNpcChat("That's it! That's the right pheasant! Thank", "you so much " + player.getUsername() + "! Here's a little", "something in exchange for helping me.", CONTENT);
					return true;
				    } else {
					if (forester.getTails() == 1) {
					    player.getDialogue().sendNpcChat("Shhhh! Quiet! I need you to help me kill", "the pheasant with 1 tail! Bring me the", "raw pheasant it drops and I'll reward you.", CONTENT);
					    player.getDialogue().endDialogue();
					    return true;
					} else {
					    player.getDialogue().sendNpcChat("Shhhh! Quiet! I need you to help me kill", "the pheasant with " + forester.getTails() + " tails! Bring me the", "raw pheasant it drops and I'll reward you.", CONTENT);
					    player.getDialogue().endDialogue();
					    return true;
					}
				    }
				case 2:
				    Item reward = forester.getRandomPieceLeft();
				    player.getDialogue().sendGiveItemNpc("The Forester hands you your reward.", reward);
				    player.getInventory().replaceItemWithItem(new Item(6178), reward);
				    forester.setInactive();
				    return true;
				case 3:
				    player.getDialogue().sendNpcChat("You can leave using that portal over there.", "Thank you again.", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
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
					    if(player.onApeAtoll()) {
						if(player.getMMVars().isMonkey()) {
						    player.getDialogue().sendNpcChat("Hello "+player.getUsername()+",", "would you like to see my shop?", CONTENT);
						} else {
						    player.getDialogue().sendNpcChat("OOK! OOK!", ANGRY_1);
						    player.getDialogue().endDialogue();
						}
					    } else {
						player.getDialogue().sendNpcChat("Hello "+player.getUsername()+",", "would you like to see my shop?", CONTENT);
					    }
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
			case 10777 : //patchy
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("'Ello "+player.getUsername()+"!", "Would you like to see my recent sewin' work?", HAPPY);
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
			case 10009 : //treasure trails
				switch(player.getDialogue().getChatId()) {
					case 1 :
						ClueScroll.itemReward(player, player.clueLevel);
						player.getDialogue().dontCloseInterface();
						break;
					case 2 :
						ClueScroll.clueReward(player, player.clueLevel, "You recieve another clue!", true, "Here is your reward.");
						player.resetEffects();
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
			case 10022: //Spirit tree south ardy && northwest varrock
			    player.getDialogue().setLastNpcTalk(3636);
				switch(player.getDialogue().getChatId()) {
					case 1 :
					    if(QuestHandler.questCompleted(player, 29)) {
						player.getDialogue().sendNpcChat("Hello " + player.getUsername(), "Would you like a trip somewhere?", -1);
						return true;
					    } else {
						player.getDialogue().sendStatement("You must complete The Grand Tree to use this spirit tree.");
						return true;
					    }
					case 2 :
						player.getDialogue().sendOption("The Tree Gnome Village.", "The Gnome Stronghold.", "Khazard Battlefield.", "Northwest Varrock.", "Nowhere.");
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
							case 3:
								player.getDialogue().sendStatement("You place your hands on the dry tough bark of the spirit tree, and", "feel a surge of energy run through your veins.");
								player.getDialogue().setNextChatId(6);
								return true;
							case 4:
								player.getDialogue().sendStatement("You place your hands on the dry tough bark of the spirit tree, and", "feel a surge of energy run through your veins.");
								player.getDialogue().setNextChatId(7);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("Nevermind.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 4 :
						Ladders.climbLadder(player, new Position(2542, 3169, 0));
						break;
					case 5 :
						Ladders.climbLadder(player, new Position(2462, 3444, 0));
						break;
					case 6 :
						Ladders.climbLadder(player, new Position(2554, 3259, 0));
						break;
					case 7 :
						Ladders.climbLadder(player, new Position(3179, 3506, 0));
						break;
				}
				break;
			case 10011 : //spirit tree the grand tree
				player.getDialogue().setLastNpcTalk(3637);
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Hello " + player.getUsername(), "Would you like a trip somewhere?", -1);
						return true;
					case 2 :
					    if(QuestHandler.questCompleted(player, 29)) {
						player.getDialogue().sendOption("The Tree Gnome Village.", "The Gnome Stronghold.", "Khazard Battlefield.", "Northwest Varrock.", "Nowhere.");
						return true;
					    } else {
						player.getDialogue().sendOption("The Tree Gnome Village.", "The Gnome Stronghold.");
						return true;
					    }
					case 3 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendStatement("You place your hands on the dry tough bark of the spirit tree, and", "feel a surge of energy run through your veins.");
								return true;
							case 2:
								player.getDialogue().sendStatement("You place your hands on the dry tough bark of the spirit tree, and", "feel a surge of energy run through your veins.");
								player.getDialogue().setNextChatId(5);
								return true;
							case 3:
								player.getDialogue().sendStatement("You place your hands on the dry tough bark of the spirit tree, and", "feel a surge of energy run through your veins.");
								player.getDialogue().setNextChatId(6);
								return true;
							case 4:
								player.getDialogue().sendStatement("You place your hands on the dry tough bark of the spirit tree, and", "feel a surge of energy run through your veins.");
								player.getDialogue().setNextChatId(7);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("Nevermind.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 4 :
						Ladders.climbLadder(player, new Position(2542, 3169, 0));
						break;
					case 5 :
						Ladders.climbLadder(player, new Position(2462, 3444, 0));
						break;
					case 6 :
						Ladders.climbLadder(player, new Position(2554, 3259, 0));
						break;
					case 7 :
						Ladders.climbLadder(player, new Position(3179, 3506, 0));
						break;
				}
				break;
			case 10023 : //spirit tree tree gnome village
				player.getDialogue().setLastNpcTalk(3637);
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Hello " + player.getUsername(), "Would you like a free trip somewhere?", -1);
						return true;
					case 2 :
						player.getDialogue().sendOption("The Tree Gnome Village.", "The Gnome Stronghold.", "Nowhere.");
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
								player.getDialogue().setNextChatId(Farmers.ProtectPlant(player, 1, "allotment", player.getClickId(), 1));
								return true;
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
			/*case 2660: //reldo
			    switch(player.getDialogue().getChatId()) {
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
				case 11:
				case 12:
				case 13:
				case 14:
				case 15:
				case 16:
				    ShieldOfArrav.sendDialogue(player, id, chatId, optionId, npcChatId);
			    }
			return true;*/
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
						player.getDialogue().sendNpcChat("Now, the next way to earn money quickly is by quests.", "Many people on vscape have things they need", "doing, which they will reward you for.", HAPPY);
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
						player.getDialogue().sendNpcChat("If all else falls, visit the vscape website for a whole", "board of information on quests, skills and minigames", "as well as a very good starter's guide", HAPPY);
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
							player.getBankManager().openBank();
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
						player.getDialogue().sendNpcChat("First I must warn you to take every precaution to", "keep your vscape password and PIN secure. The", "most important thing to remember is to never give your", "password to, or share your account with, anyone.", CONTENT);
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
								player.getSlayer().assignNewTask(id);
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
                        case 1599 : //slayer skillcape
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("'Ello, and what are you after then?", HAPPY);
						return true;
					case 2 :
                                            if(player.getSkill().getLevel()[Skill.SLAYER] == 99 && !checkTrim(player))
						player.getDialogue().sendOption("I need another assignment", "Do you have anything for trade?", "Er...nothing", "I want my skillcape!");
                                            else if(player.getSkill().getLevel()[Skill.SLAYER] == 99 && checkTrim(player))
						player.getDialogue().sendOption("I need another assignment", "Do you have anything for trade?", "Er...nothing", "I want my skillcape!", "Can you trim my skillcape?");
                                            else
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
                                                        case 4:
                                                                player.getDialogue().sendPlayerChat("I want my skillcape!", ANGRY_1);
                                                                player.getDialogue().setNextChatId(9);
                                                        return true;
                                                        case 5:
                                                                player.getDialogue().sendPlayerChat("Can you trim my skillcape?", CONTENT);
                                                                player.getDialogue().setNextChatId(12);
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
									player.getDialogue().sendNpcChat("There are no tips about this npc yet.", HAPPY);
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
					return true;
                                        case 9:
                                            player.getDialogue().sendNpcChat("You've earned it.", CONTENT);
                                        return true;
                                        case 10:
                                            ShopManager.openShop(player, 185);
                                            player.getDialogue().dontCloseInterface();
                                        return true;
                                        case 12:
                                            player.getDialogue().sendNpcChat("Sure, but this is irreversible!", "However, you can always buy another untrimmed cape.", CONTENT);
                                            return true;
                                        case 13:
                                            if(player.getInventory().playerHasItem(9786)) {
                                            player.getDialogue().sendNpcChat("Here you are.", CONTENT);
                                            trimCape(player, 9786);
                                            }
                                            else {
                                                player.getDialogue().sendPlayerChat("I guess I don't have the cape on me.", VERY_SAD);
                                                player.getDialogue().endDialogue();
                                            }
                                            return true; 
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
						player.getRandomHandler().destroyEventNpc();
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
						player.getRandomHandler().destroyEventNpc();
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
					player.getRandomHandler().destroyEventNpc();
					player.getDialogue().endDialogue();
					return true;
				}
				break;
			case 500 : //Mosol Rei
				switch(player.getDialogue().getChatId()) {
					case 1 :
					    if(player.getInventory().playerHasItem(new Item(431))) {
						player.getDialogue().sendNpcChat("Bah! We do not allow smugglers in our village!", ANGRY_1);
						player.getDialogue().endDialogue();
					    }
					    else {
						player.getDialogue().sendNpcChat("Would you like to enter this village?", "Note that once you enter, you cannot get out", "through this way.", CONTENT);
						return true;
					    }
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
			case 1152: //Weird Old Man, in / out of kharazi
				switch(player.getDialogue().getChatId()) {
					case 1 :
					    if(player.getPosition().getY() > 2945) {
						player.getDialogue().sendNpcChat("Want me to get you inside the Kharazi Jungle?", CONTENT);
						return true;
					    }
					    else {
						player.getDialogue().sendNpcChat("Would you like me to bring you back", "to Shilo Village?", CONTENT);
						player.getDialogue().setNextChatId(5);
						return true;
					    }
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
					case 5 :
						player.getDialogue().sendOption("Yes please.", "No thanks.");
						return true;
					case 6 :
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
			case 10604 : // dragonfire shield smithing
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendStatement("You set to work, trying to attach the ancient draconic", "visage to your anti-dragonbreath shield. It's not easy to", "work with the ancient artifact and it takes all your", "skill as a master smith.");
						player.getUpdateFlags().sendAnimation(898);
						player.getActionSender().sendSound(468, 0, 0);
						return true;
					case 2 :
						DragonfireShieldSmithing.smithShield(player);
						player.getDialogue().resetDialogue();
						player.getActionSender().removeInterfaces();
						return true;
					case 3 :
						player.getDialogue().sendStatement("Even for an expert armourer it is not an easy task,","but eventually it is ready. You have crafted the", "draconic visage and anti-dragonbreath shield into a", "dragonfire shield.");
						player.getDialogue().endDialogue();
						return true;
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
			case 805 : //Crafting master skillcape
				switch(player.getDialogue().getChatId()) {
					case 1 :
                                            player.getDialogue().sendNpcChat("Hello, and welcome to the Crafting Guild. Accomplished","crafters from all over the land come here to use our","top notch workshops.", CONTENT);
                                        return true;
                                        case 2 :
                                            if(player.getSkill().getLevel()[Skill.CRAFTING] == 99 && !checkTrim(player)) 
						player.getDialogue().sendOption("Neat.", "(don't respond)", "Fuck off, I want my skillcape!");
                                            else if(player.getSkill().getLevel()[Skill.CRAFTING] == 99 && checkTrim(player)) 
						player.getDialogue().sendOption("Neat.", "(don't respond)", "Fuck off, I want my skillcape!", "Can you trim my god damned skillcape?");
                                            else
                                                player.getDialogue().sendOption("Neat.", "(don't respond)");
                                            return true; 
                                        case 3:
						switch(optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Neat.", CONTENT);
								return true;
                                                        case 2:
                                                                player.getDialogue().sendPlayerChat("...", DISTRESSED);
								player.getDialogue().endDialogue();
                                                                return true;
                                                        case 3: 
                                                                player.getDialogue().sendPlayerChat("Fuck off, I want my skillcape!", ANGRY_1);
                                                                player.getDialogue().setNextChatId(5);
								return true;
                                                        case 4: 
                                                                player.getDialogue().sendPlayerChat("Can you trim my god damned skillcape?", ANGRY_1);
                                                                player.getDialogue().setNextChatId(8);
								return true;	
						}
                                        case 5:
                                            player.getDialogue().sendNpcChat("Yo-you've earned it.", DISTRESSED);
                                            return true;
                                        case 6:
                                            ShopManager.openShop(player, 183);
                                            player.getDialogue().dontCloseInterface();
                                            return true;
                                        case 8:
                                            player.getDialogue().sendNpcChat("Sure, but this is irreversible!", "However, you can always buy another untrimmed cape.", CONTENT);
                                            return true;
                                        case 9:
                                            if(player.getInventory().playerHasItem(9780)) {
                                                player.getDialogue().sendNpcChat("Here you are.", CONTENT);
                                                trimCape(player, 9780);
                                            }
                                            else {
                                                player.getDialogue().sendPlayerChat("I guess I don't have the cape on me.", VERY_SAD);
                                                player.getDialogue().endDialogue();
                                            }
                                            return true;
				}
			break;
                        /*case 732 : //Cinco de mayo
				switch(player.getDialogue().getChatId()) {
					case 1 :
                                            player.getDialogue().sendNpcChat("Hola, quieres una cerveza?", CONTENT);
                                        return true;
                                        case 2 :
						player.getDialogue().sendOption("Si.", "No.");
                                            return true; 
                                        case 3:
						switch(optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Si.", HAPPY);
                                                                player.getInventory().addItem(new Item(1917));
								return true;
                                                        case 2:
                                                                player.getDialogue().sendPlayerChat("No", ANNOYED);
                                                                player.getDialogue().endDialogue();
                                                                return true;		
						}         
				}
			break; */
			    //skillcape npcs without prior dialogue
                        case 4288 : //Attack
			case 4297 : //strength
			case 705 : //defence
			case 1658 : //magic
			case 682 : //range
			case 437 : //agility
			case 2270 : //thieving
			case 575 : //fletching
			case 3295 : //mining
			case 308 : //fishing
			case 847 : //cooking
			case 4946 : //firemaking
			case 4906 : //woodcutting
			case 3299 : //farming
				switch(player.getDialogue().getChatId()) {
				    
					case 1 :
                                            player.getDialogue().sendNpcChat("Hello.", CONTENT);
                                        return true;
                                        case 2 :
                                            if(player.getSkill().getLevel()[skillCapeData(player, id, 1)] == 99 && !checkTrim(player)){ 
						player.getDialogue().sendOption("(don't respond)", "I want my skillcape!");
                                                player.getDialogue().setNextChatId(4);
                                                return true;
                                            }
                                            else if(player.getSkill().getLevel()[skillCapeData(player, id, 1)] == 99 && checkTrim(player)){ 
						player.getDialogue().sendOption("(don't respond)", "I want my skillcape!", "Can you trim my skillcape?");
                                                player.getDialogue().setNextChatId(4);
                                                return true;
                                            }
                                            else {
                                                player.getDialogue().sendPlayerChat("Hello.", CONTENT);
                                                player.getDialogue().setNextChatId(3);
                                                return true;
                                            }
                                        case 3:
                                            player.getDialogue().sendNpcChat("Return to me when you have reached level 99.", DISTRESSED);
                                            player.getDialogue().endDialogue();
                                            return true; 
                                        case 4:
						switch(optionId) {
                                                        case 1:
                                                                player.getDialogue().sendPlayerChat("...", DISTRESSED);
								player.getDialogue().endDialogue();
                                                                return true;
                                                        case 2: 
                                                                player.getDialogue().sendPlayerChat("I want my skillcape!", NEAR_TEARS_2);
								return true;
                                                        case 3: 
                                                                player.getDialogue().sendPlayerChat("Can you trim my skillcape?", CONTENT);
                                                                player.getDialogue().setNextChatId(8);
								return true;
						}
                                        case 5:
                                            player.getDialogue().sendNpcChat("You've earned it.", CONTENT);
                                            return true;
                                        case 6:
                                            ShopManager.openShop(player, skillCapeData(player, id, 3));
                                            player.getDialogue().dontCloseInterface();
                                            return true;
                                        case 8:
                                            player.getDialogue().sendNpcChat("Sure, but this is irreversible!", "However, you can always buy another untrimmed cape.", CONTENT);
                                            return true;
                                        case 9:
                                            if(player.getInventory().playerHasItem(skillCapeData(player, id, 2))) {
                                            player.getDialogue().sendNpcChat("Here you are.", CONTENT);
                                            trimCape(player, skillCapeData(player, id, 2));
                                            }
                                            else {
                                                player.getDialogue().sendPlayerChat("I guess I don't have the cape on me.", VERY_SAD);
                                                player.getDialogue().endDialogue();
                                            }
                                            return true;
                                        case 10:
                                            player.getDialogue().sendPlayerChat("Hey... can you also trim armor?", CONTENT);
                                            return true;
                                        case 11:
                                            player.getDialogue().sendNpcChat("Yes! Just meet me at level 55 in the wilderness,", "and bring 500,000 coins!", DELIGHTED_EVIL);
                                            return true;
                                        case 12:
                                            player.getDialogue().sendPlayerChat("....", ANNOYED);
                                            player.getDialogue().endDialogue();
                                            return true;
				}
			break;
			case 3830 : //CATHY CORKAT
				final Npc npc = World.getNpcs()[player.getNpcClickIndex()];
				if(npc.getPosition().getX() == 2367 && npc.getPosition().getY() == 3488){
					switch(player.getDialogue().getChatId())
					{
						case 1:
							player.getDialogue().sendNpcChat("Hello, What can I do for you?", CONTENT);
						return true;
						case 2:
							player.getDialogue().sendOption("I would like to travel to Piscatoris.", "Nevermind.");
						return true;
						case 3:
							 switch(optionId) {
								 case 1:
									 player.getDialogue().sendPlayerChat("I would like to travel to Piscatoris.", CONTENT);
									 player.getDialogue().setNextChatId(4);
								 return true;
								 case 2 :
									 player.getDialogue().sendPlayerChat("Nevermind.", CONTENT);
									 player.getDialogue().endDialogue();
								 return true;
							 }
						break;
						case 4:
							player.getDialogue().sendNpcChat("It will cost you 50gp for passage.", CONTENT);
							if(player.getInventory().getItemAmount(995) >= 50){
								player.getDialogue().setNextChatId(6);
							}else{
								player.getDialogue().setNextChatId(5);
							}
						return true;
						case 5:
							 player.getDialogue().sendPlayerChat("I don't have enough coins.", CONTENT);
							 player.getDialogue().endDialogue();
						return true;
						case 6:
							player.getDialogue().sendOption("Okay, Here you go!", "Nevermind.");
						return true;
						case 7:
							 switch(optionId) {
								 case 1:
									 player.getDialogue().sendPlayerChat("Okay, Here you go!", CONTENT);
									 player.getDialogue().endDialogue();
									 player.getInventory().removeItem(new Item(995, 50));
									 player.teleport(new Position(2356,3641));
								 return true;
								 case 2 :
									 player.getDialogue().sendPlayerChat("Nevermind.", CONTENT);
									 player.getDialogue().endDialogue();
								 return true;
							 }
						break;
					}
				}else{
					switch(player.getDialogue().getChatId())
					{
						case 1:
							player.getDialogue().sendNpcChat("Hello, What can I do for you?", CONTENT);
						return true;
						case 2:
							player.getDialogue().sendOption("I would like to leave.", "Nevermind.");
						return true;
						case 3:
							 switch(optionId) {
								 case 1:
									 player.getDialogue().sendPlayerChat("I would like to leave.", CONTENT);
									 player.getDialogue().setNextChatId(4);
								 return true;
								 case 2 :
									 player.getDialogue().sendPlayerChat("Nevermind.", CONTENT);
									 player.getDialogue().endDialogue();
								 return true;
							 }
						break;
						case 4:
							player.getDialogue().sendNpcChat("Okay, Just a moment.", CONTENT);
							player.getDialogue().endDialogue();
							player.teleport(new Position(2367,3487));
						return true;
					}
				}
			break;
			case 2868 : // zombie head talk to
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendOption("How did you die?", "What is your name?");
						return true;
					case 2 :
						player.getDialogue().sendPlayerChat("Hey, Head?", Dialogues.CONTENT);
						switch(optionId) {
						case 1 :
							player.getDialogue().setNextChatId(10);
							return true;
						case 2 :
							player.getDialogue().setNextChatId(15);
							return true;
						}
					break;
					//question 1
					case 10:
						player.getDialogue().sendNpcChat("What?", Dialogues.CONTENT);
						return true;
					case 11:
						player.getDialogue().sendPlayerChat("How did you die?", Dialogues.CONTENT);
						return true;
					case 12:
						player.getDialogue().sendNpcChat("I stuck my head out for an old friend.", Dialogues.CONTENT);
						return true;
					case 13:
						player.getDialogue().sendPlayerChat("You shouldn't get so cut up about it.", Dialogues.CONTENT);
						return true;
					case 14:
						player.getDialogue().sendNpcChat("Well if i keep it all bottled up I'll turn into a head case.", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
						return true;
					//question 2
					case 15:
						player.getDialogue().sendNpcChat("My name is Edward Cranium", Dialogues.CONTENT);
						return true;
					case 20:
						player.getDialogue().sendPlayerChat("Edd Cranium?", "Hahahahahahahahahahaha!", Dialogues.CONTENT);
						return true;
					case 21:
						player.getDialogue().sendNpcChat("Har har...", Dialogues.ANNOYED);
						player.getDialogue().endDialogue();
						return true;
				}
			break;
			//kittens/cats
			case 100000 :
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendOption("Shoo away", "Nevermind.");
						return true;
					case 2 :
						switch(optionId) {
						case 1 :
							player.getCat().RunAway(true);
							player.getDialogue().endDialogue();
							return true;
						case 2 :
							player.getDialogue().sendPlayerChat("Nevermind.", Dialogues.CONTENT);
							player.getDialogue().endDialogue();
							return true;
						}
					break;
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
