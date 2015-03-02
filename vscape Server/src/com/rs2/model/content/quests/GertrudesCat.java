package com.rs2.model.content.quests;

import static com.rs2.model.content.dialogue.Dialogues.*;

import com.rs2.model.content.dialogue.DialogueManager;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class GertrudesCat implements Quest {

	//Quest stages
	public static final int QUEST_STARTED = 1;
	public static final int SPEAK_TO_KIDS = 2;
	public static final int PAID_SHILOP = 3;
	public static final int PAID_SHILOP_2 = 4;
	public static final int FOUND_FLUFFS = 5;
	public static final int GAVE_FLUFFS_MILK = 6;
	public static final int GAVE_FLUFFS_SARDINE = 7;
	public static final int USED_KITTEN_ON_FLUFFS = 8;
	public static final int QUEST_COMPLETE = 9;
	//Interfaces
	public static final int SCROLL_INTERFACE = 6965;
	//Npcs
	public static final int GERTRUDE = 780;
	public static final int SHILOP = 781;
	public static final int FLUFFS_NPC = 759;
	public static final int FLUFFS_KITTEN_NPC = 759;
	//Items
	private static final int DOOGLE_LEAVES = 1573;
	private static final int SEASONED_SARDINE = 1552;
	public static final int FLUFFS_KITTEN_ITEM = 1554;
	private static final int[] kittenItems = {1555, 1556, 1557, 1558, 1559, 1560};

	private int reward[][] = { //Items in the form of {Id, #},
		{1897, 1},
		{2003, 1},};
	private int expReward[][] = { //Exp in the form of {Skill.AGILITY, x},
		{Skill.COOKING, 1525},}; //The 2.25 multiplier is added later, use vanilla values

	@Override
	public int getQuestID() {
		return 32;
	}

	@Override
	public String getQuestName() {
		return "Gertrudes Cat";
	}

	@Override
	public String getQuestSaveName() {
		return "Gertrudes-Cat";
	}

	@Override
	public boolean canDoQuest(Player player) {
		return true;
	}

	@Override
	public void getReward(Player player) {
		for (int[] rewards : reward) {
			player.getInventory().addItemOrDrop(new Item(rewards[0], rewards[1]));
		}
		for (int[] expRewards : expReward) {
			player.getSkill().addExp(expRewards[0], (expRewards[1]));
		}
		player.getCat().setCatItem(kittenItems[Misc.random(kittenItems.length - 1)]);
		player.getCat().registerCat(player.getCat().getCatItem());
		player.addQuestPoints(getQuestPoints());
		player.getActionSender().QPEdit(player.getQuestPoints());
	}

	@Override
	public void completeQuest(Player player) {
		getReward(player);
		player.getActionSender().sendInterface(12140);
		player.getActionSender().sendItemOnInterface(995, 200, 12142);
		player.getActionSender().sendString("You have completed " + getQuestName() + "!", 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("A kitten", 12151);
		player.getActionSender().sendString("COOKING XP", 12152);
		player.getActionSender().sendString("A chocolate cake", 12153);
		player.getActionSender().sendString("A bowl of stew", 12154);
		player.getActionSender().sendString("Raise cats", 12155);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), 7360);
	}

	@Override
	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		switch (questStage) {
			default:
				player.getActionSender().sendString("I can start this quest by speaking to @red@Gertrude", 8147);
				player.getActionSender().sendString("She can be found to the @red@west of Varrock.", 8148);
				break;
			case QUEST_STARTED:
				player.getActionSender().sendString("@str@I accepted the challenge of finding Gertrudes lost cat.", 8147);
				player.getActionSender().sendString("I need to get more information from @red@Gertrude", 8149);
				break;
			case SPEAK_TO_KIDS:
				player.getActionSender().sendString("@str@I accepted the challenge of finding Gertrudes lost cat.", 8147);
				player.getActionSender().sendString("I need to @red@speak to Shilop @bla@at the @red@marketplace.", 8149);
				break;
			case PAID_SHILOP:
			case PAID_SHILOP_2:
				player.getActionSender().sendString("@str@I accepted the challenge of finding Gertrudes lost cat.", 8147);
				player.getActionSender().sendString("@str@I spoke to Shilop, Gertrude's Son.", 8148);
				player.getActionSender().sendString("I need to @red@go to his play area @bla@and @red@find the lost cat and", 8150);
				player.getActionSender().sendString("@red@return it to Gertrude.", 8151);
				break;
			case FOUND_FLUFFS:
				player.getActionSender().sendString("@str@I accepted the challenge of finding Gertrudes lost cat.", 8147);
				player.getActionSender().sendString("@str@I spoke to Shilop, Gertrude's Son.", 8148);
				player.getActionSender().sendString("I found Gertrudes cat but she doesn't want to leave", 8150);
				player.getActionSender().sendString("maybe she's @red@thirsty.", 8151);
				break;
			case GAVE_FLUFFS_MILK:
				player.getActionSender().sendString("@str@I accepted the challenge of finding Gertrudes lost cat.", 8147);
				player.getActionSender().sendString("@str@I spoke to Shilop, Gertrude's Son.", 8148);
				player.getActionSender().sendString("@str@I fed Fluffs some milk", 8149);
				player.getActionSender().sendString("I fed Fluffs some milk but she still wont leave", 8151);
				player.getActionSender().sendString("perhaps she's @red@hungry @bla@too.", 8152);
				break;
			case GAVE_FLUFFS_SARDINE:
				player.getActionSender().sendString("@str@I accepted the challenge of finding Gertrudes lost cat.", 8147);
				player.getActionSender().sendString("@str@I spoke to Shilop, Gertrude's Son.", 8148);
				player.getActionSender().sendString("@str@I fed the cat some milk and seasoned sardine", 8149);
				player.getActionSender().sendString("I fed Fluffs some milk and sardine but she still wont leave", 8151);
				player.getActionSender().sendString("I can hear @red@kittens in the lumber yard.", 8152);
				break;
			case USED_KITTEN_ON_FLUFFS:
				player.getActionSender().sendString("@str@I accepted the challenge of finding Gertrudes lost cat.", 8147);
				player.getActionSender().sendString("@str@I spoke to Shilop, Gertrude's Son.", 8148);
				player.getActionSender().sendString("@str@I fed the cat some milk and seasoned sardine", 8149);
				player.getActionSender().sendString("@str@I gave Fluffs her kitten back", 8150);
				player.getActionSender().sendString("She ran off home", 8152);
				break;
			case QUEST_COMPLETE:
				player.getActionSender().sendString("@str@I helped Gertrude to find her lost cat", 8147);
				player.getActionSender().sendString("@str@I fed it and returned he missing kitten", 8148);
				player.getActionSender().sendString("@str@Gertrude gave me my verry own pet for a reward", 8149);
				player.getActionSender().sendString("@red@You have completed this quest!", 8151);
				break;
		}
	}

	@Override
	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 7360);
	}

	@Override
	public boolean questCompleted(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if (questStage >= QUEST_COMPLETE) {
			return true;
		}
		return false;
	}

	@Override
	public int getQuestPoints() {
		return 1;
	}

	@Override
	public void showInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
		player.getActionSender().sendString(getQuestName(), 8144);
	}

	@Override
	public void sendQuestTabStatus(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if ((questStage >= QUEST_STARTED) && (questStage < QUEST_COMPLETE)) {
			player.getActionSender().sendString("@yel@" + getQuestName(), 7360); //These numbers correspond to the index of the quest in
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 7360); //the quest list on the quest tab. I've listed them all
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 7360); //in the player class, just search and then replace
			//also add the name while you're there @red@Quest Name
		}
	}

	@Override
	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	@Override
	public boolean doNpcClicking(final Player player, final Npc npc) {
		if (npc.getNpcId() == FLUFFS_NPC) {
			switch (player.getQuestStage(getQuestID())) {
				case PAID_SHILOP:
				case PAID_SHILOP_2:
					npc.getUpdateFlags().setForceChatMessage("Hiss!");
					player.getUpdateFlags().setForceChatMessage("Ouch!");
					player.getDialogue().sendStatement("Fluffs hisses but clearly wants something - maybe she is thirsty?");
					player.setQuestStage(getQuestID(), FOUND_FLUFFS);
					return true;
				case FOUND_FLUFFS:
					npc.getUpdateFlags().setForceChatMessage("Hiss!");
					player.getUpdateFlags().setForceChatMessage("Ouch!");
					player.getDialogue().sendStatement("Fluffs hisses but clearly wants something - maybe she is thirsty?");
					return true;
				case GAVE_FLUFFS_MILK:
					npc.getUpdateFlags().setForceChatMessage("Hiss!");
					player.getUpdateFlags().setForceChatMessage("Ouch!");
					player.getDialogue().sendStatement("Fluffs hisses but clearly wants something - maybe she is hungry too?");
					return true;
				case GAVE_FLUFFS_SARDINE:
					npc.getUpdateFlags().setForceChatMessage("Hiss!");
					player.getUpdateFlags().setForceChatMessage("Ouch!");
					player.getDialogue().sendStatement("Fluffs seems afraid to leave.", "In the Lumber Lard below you can hear kittens mewing.");
					return true;
				default:
					return false;
			}
		}
		return false;
	}

	@Override
	public boolean doItemOnNpc(Player player, final int itemId, final Npc npc) {
		if (npc.getNpcId() == FLUFFS_NPC) {
			switch (player.getQuestStage(getQuestID())) {
				case FOUND_FLUFFS:
					if (itemId == 1927 && player.getInventory().playerHasItem(1927)) {
						npc.getUpdateFlags().setForceChatMessage("Mew!");
						player.getInventory().removeItemSlot(new Item(itemId, 1), player.getSlot());
						player.getInventory().addItemToSlot(new Item(1925, 1), player.getSlot());
						player.getDialogue().sendStatement("Fluffs laps up the milk greedily. Then she mews at you.");
						player.setQuestStage(getQuestID(), GAVE_FLUFFS_MILK);
					}
					return true;
				case GAVE_FLUFFS_MILK:
					if (itemId == SEASONED_SARDINE && player.getInventory().playerHasItem(SEASONED_SARDINE)) {
						npc.getUpdateFlags().setForceChatMessage("Mew!");
						player.getInventory().removeItemSlot(new Item(itemId, 1), player.getSlot());
						player.getDialogue().sendStatement("Fluffs devours the doogle sardine greedily. Then she mews at you.");
						player.setQuestStage(getQuestID(), GAVE_FLUFFS_SARDINE);
					}
					return true;
				case GAVE_FLUFFS_SARDINE:
					if (itemId == FLUFFS_KITTEN_ITEM && player.getInventory().playerHasItem(FLUFFS_KITTEN_ITEM)) {
						npc.getUpdateFlags().setForceChatMessage("Purrr!");
						player.getInventory().removeItemSlot(new Item(itemId, 1), player.getSlot());
						player.getDialogue().sendStatement("Fluffs has run off home with her offspring.");
						player.setQuestStage(getQuestID(), USED_KITTEN_ON_FLUFFS);
					}
					return true;
				default:
					return false;
			}
		}
		return false;
	}

	@Override
	public boolean itemHandling(Player player, int itemId) {
		return false;
	}

	@Override
	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		if (firstItem == DOOGLE_LEAVES && secondItem == 327 || firstItem == 327 && secondItem == DOOGLE_LEAVES) {
			player.getInventory().removeItemSlot(new Item(firstItem), firstSlot);
			player.getInventory().removeItemSlot(new Item(secondItem), secondSlot);
			player.getInventory().addItemToSlot(new Item(SEASONED_SARDINE), secondSlot);
			return true;
		}
		return false;
	}

	@Override
	public boolean doItemOnObject(Player player, int object, int item) {
		return false;
	}

	@Override
	public boolean doObjectClicking(final Player player, final int object, final int x, final int y) {
		if (object == 2620) {
			switch (player.getQuestStage(getQuestID())) {
				case GAVE_FLUFFS_SARDINE:
					if (player.getInventory().canAddItem(new Item(FLUFFS_KITTEN_ITEM, 1))
						&& !player.hasItem(FLUFFS_KITTEN_ITEM)) {
						player.getActionSender().sendMessage("You search the crate...");
						player.getUpdateFlags().sendAnimation(832);
						player.setStopPacket(true);
						CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
							@Override
							public void execute(CycleEventContainer b) {
								if (x == 3310 && y == 3498) {
									player.getDialogue().sendStatement("You find a kitten! You carefully place it in your backpack.");
									player.getInventory().addItem(new Item(FLUFFS_KITTEN_ITEM, 1));
								} else {
									player.getActionSender().sendMessage("You find nothing of interest.");
								}
								b.stop();
							}

							@Override
							public void stop() {
								player.setStopPacket(false);
							}
						}, 2);
						return true;
					}
					return false;
				default:
					return false;
			}
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

	@Override
	public boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
		DialogueManager d = player.getDialogue();
		switch (id) { //Npc ID
			case GERTRUDE:
				switch (player.getQuestStage(getQuestID())) {
					case 0:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello, are you ok?", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Do I look ok? Those kids drive me crazy.", ANGRY_1);
								return true;
							case 3:
								d.sendNpcChat("I'm sorry. It's just that I've lost her.", DISTRESSED);
								return true;
							case 4:
								d.sendPlayerChat("Lost who?", CONTENT);
								return true;
							case 5:
								d.sendNpcChat("Fluffs, poor Fluffs. She never hurt anyone.", DISTRESSED);
								return true;
							case 6:
								d.sendPlayerChat("Who's Fluffs?", CONTENT);
								return true;
							case 7:
								d.sendNpcChat("My beloved feline friend Fluffs. She's been purring by", "my side for almost a decade. Please, could you go", "search for her while I look over the kids?", DISTRESSED);
								return true;
							case 8:
								player.getDialogue().sendOption("Well, I suppose I could.", "What's in it for me?", "Sorry, I'm too busy to play pet rescue.");
								return true;
							case 9:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("Well, I suppose I could.", CONTENT);
										d.setNextChatId(1);
										QuestHandler.startQuest(player, getQuestID());
										return true;
									case 2:
										d.sendPlayerChat("What's in it for me?", CONTENT);
										d.setNextChatId(10);
										return true;
									case 3:
										d.sendPlayerChat("Sorry, I'm too busy to play pet rescue.", CONTENT);
										d.setNextChatId(14);
										return true;
								}
								return false;
							case 10:
								d.sendNpcChat("I'm sorry, I'm too poor to pay you anything,", "the best I could offer is a warm meal.", CONTENT);
								return true;
							case 11:
								d.sendPlayerChat("Just a meal? It's not the best offer I've had,", "but I suppose I can help.", CONTENT);
								return true;
							case 12:
								d.sendNpcChat("I suppose I could give you some nice, yummy chocolate cake,", "maybe even a kitten too, if you seem like a nice sort.", CONTENT);
								return true;
							case 13:
								d.sendNpcChat("Is that something you could be persuaded with?", CONTENT);
								d.setNextChatId(8);
								return true;
							case 14:
								d.sendNpcChat("Well, okay then. I'll have to find someone else,", "someone less heartless. It will be on your conscience if", "a poor kitty is lost in the wilds, though.", ANGRY_1);
								d.endDialogue();
								return true;
						}
						return false;
					case QUEST_STARTED:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendNpcChat("Really? Thank you so much! I really have no idea", "where she could be!", HAPPY);
								return true;
							case 2:
								d.sendNpcChat("I think my sons, Shilop and Wilough, saw the cat last.", "They'll be out in the market place.", HAPPY);
								return true;
							case 3:
								d.sendPlayerChat("Alright then, I'll see what i can do.", CONTENT);
								player.setQuestStage(getQuestID(), SPEAK_TO_KIDS);
								d.endDialogue();
								return true;
						}
						return false;
					case SPEAK_TO_KIDS:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendPlayerChat("Hello Gertrude.", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Have you seen my poor Fluffs?", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("I'm afraid not.", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("What about Shilop?", CONTENT);
								return true;
							case 5:
								d.sendPlayerChat("No sign of him either.", CONTENT);
								return true;
							case 6:
								d.sendNpcChat("Hmmmm, strange he should be in Varrock Marketplace.", CONTENT);
								d.endDialogue();
								return true;
						}
						return false;
					case PAID_SHILOP:
					case PAID_SHILOP_2:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendPlayerChat("Hello Gertrude.", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Hello again, did you manage to find Shilop?", "I can't keep an eye on him for the life of me.", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("He does seem quite a handful.", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("You have no idea! Did he help at all?", CONTENT);
								return true;
							case 5:
								d.sendPlayerChat("I think so. I'm just going to look now.", CONTENT);
								return true;
							case 6:
								d.sendNpcChat("Thanks again, adventurer.", CONTENT);
								d.endDialogue();
								return true;
						}
						return false;
					case FOUND_FLUFFS:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendPlayerChat("Hello Gertrude.", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Hello again, did you manage to find Shilop?", "I can't keep an eye on him for the life of me.", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("He does seem quite a handful.", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("You have no idea! Did he help at all?", CONTENT);
								return true;
							case 5:
								d.sendPlayerChat("Yes, I've found Fluffs!", CONTENT);
								return true;
							case 6:
								d.sendNpcChat("That's great, where is she? ", HAPPY);
								return true;
							case 7:
								d.sendPlayerChat("She's still at the Lumber Yard.", "I think she may be hungry, thirsty or both.", CONTENT);
								return true;
							case 8:
								d.sendNpcChat("Oh dear, oh dear! Maybe she's just hungry.", "She loves doogle sardines but I'm all out.", CONTENT);
								return true;
							case 9:
								d.sendPlayerChat("Doogle sardines?", CONTENT);
								return true;
							case 10:
								d.sendNpcChat("Yes, raw sardines seasoned with doogle leaves.", "Unfortunately, I've used all my doogles leaves,", "but you may find some on the bush out back.", CONTENT);
								return true;
							case 11:
								d.sendPlayerChat("What if she is thirsty? ", CONTENT);
								return true;
							case 12:
								d.sendNpcChat("In that case, she'd probably like some milk.", "A bucketful would tempt her, I'm sure.", CONTENT);
								return true;
							case 13:
								d.sendPlayerChat("It seems a rather large amount of milk for one small cat,", "but I'll give it a try.", CONTENT);
								d.endDialogue();
								return true;
						}
						return true;
					case GAVE_FLUFFS_MILK:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendPlayerChat("Hello again.", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Hello. How's it going? Any luck?", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("Yes, I've found Fluffs!", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("Well well, you are clever! Did you bring her back?", CONTENT);
								return true;
							case 5:
								d.sendPlayerChat("Well, that's the thing, she refuses to leave.", CONTENT);
								return true;
							case 6:
								d.sendNpcChat("Oh dear, oh dear! Maybe she's just hungry.", "She loves doogle sardines but I'm all out.", CONTENT);
								return true;
							case 7:
								d.sendPlayerChat("Doogle sardines?", CONTENT);
								return true;
							case 8:
								d.sendNpcChat("Yes, raw sardines seasoned with doogle leaves.", "Unfortunately, I've used all my doogles leaves,", "but you may find some on the bush out back.", CONTENT);
								d.endDialogue();
								return true;
						}
						return true;
					case GAVE_FLUFFS_SARDINE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendPlayerChat("Hi!", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Hey traveller, did Fluffs eat the sardines?", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("Yeah, she loved them, but she still won't leave.", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("Well that is strange, there must be a reason.", CONTENT);
								d.endDialogue();
								return true;
						}
						return true;
					case USED_KITTEN_ON_FLUFFS:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendPlayerChat("Hello, Gertrude. Fluffs had run off with her kittens,", "lost them and I have now returned them to her.", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Thank you! If you hadn't found her kittens then", "they would have died out there.", "I've got some presents for you in thanks for your help.", HAPPY);
								return true;
							case 3:
								d.sendPlayerChat("That's ok, I like to do my bit.", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("I have no real material possessions but I do", "have kittens. I've cooked you some food too.", HAPPY);
								return true;
							case 5:
								d.sendPlayerChat("You're going to give me a kitten? Thanks.", HAPPY);
								return true;
							case 6:
								d.sendNpcChat("I would sell one to my cousin in West Ardougne,", "I hear there's a rat epidemic there but it's too far", "for me to travel, what with my boys and all.", HAPPY);
								return true;
							case 7:
								d.sendNpcChat("Oh, by the way, the kitten can live in your backpack but,", "to ensure it grows, you must take it out,", "feed it and stroke it often. ", HAPPY);
								return true;
							case 8:
								QuestHandler.completeQuest(player, getQuestID());
								d.dontCloseInterface();
								//COMPLETE HERE
								return true;
						}
						return true;
					// TODO talking to and buying a new kitten if lost
					case QUEST_COMPLETE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendPlayerChat("Hello again.", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Hello my dear. How's things?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendOption("I'm fine thanks.", "Do you have any more kittens?");
								return true;
							case 4:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("I'm fine thanks.", CONTENT);
										d.endDialogue();
										return true;
									case 2:
										d.sendPlayerChat("Do you have any more kittens?", CONTENT);
										d.setNextChatId(5);
										return true;
								}
								return false;
							case 5:
								if (player.getCat().hasCat()) {
									if (player.getCat().getGrowthStage() == 0) {//IS KITTEN (but has cat)
										d.sendNpcChat("Aren't you still raising that other kitten? Only once it's", "fully grown and it no longer needs your attention will", "I let you have another kitten.", CONTENT);
										d.endDialogue();
									} else { //IS NOT KITTEN (but has cat)
										d.sendNpcChat("You already have a cat, it seems to like you alot.", "You don't need a kitten.", CONTENT);
										d.endDialogue();
									}
								} else {  //HAS NO KITTEN OR CAT
									d.sendNpcChat("I have some more kittens here,", "But it will cost you 100 coins.", CONTENT);
									d.setNextChatId(6);
								}
								return true;
							case 6:
								player.getDialogue().sendOption("Okay here's 100 coins.", "Nevermind.");
								return true;
							case 7:
								switch (optionId) {
									case 1:
										if (player.getInventory().playerHasItem(995, 100)) {
											d.sendGiveItemNpc("Okay here's 100 coins.", new Item(995, 100));
											player.getInventory().removeItem(new Item(995, 100));
											player.getCat().setCatItem(kittenItems[Misc.random(kittenItems.length - 1)]);
											player.getCat().registerCat(player.getCat().getCatItem());
											d.endDialogue();
										} else {
											d.sendStatement("You don't have enough coins.");
											d.endDialogue();
										}
										return true;
									case 2:
										d.sendPlayerChat("Nevermind.", CONTENT);
										d.endDialogue();
										return true;
								}
								return false;
						}
						return true;
				}
				return false;

			case SHILOP:
				switch (player.getQuestStage(getQuestID())) {
					case SPEAK_TO_KIDS:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendPlayerChat("Hello there, I've been looking for you.", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("I didn't mean to take it! I just forgot to pay.", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("What? I'm trying to help your mum find Fluffs.", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("ohh... well, in that case I might be able to help. Fluffs", "followed me to my secret play area. I haven't seen her", "since.", CONTENT);
								return true;
							case 5:
								d.sendPlayerChat("Where is this play area?", CONTENT);
								return true;
							case 6:
								d.sendNpcChat("If i told you that, it wouldn't be a secret.", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendOption("Tell me sonny, or I will hurt you.", "What will make you tell me?", "Well never mind, it's Fluffs' loss.");
								return true;
							case 8:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("Tell me sonny, or I will hurt you.", ANGRY_1);
										d.setNextChatId(9);
										return true;
									case 2:
										d.sendPlayerChat("What will make you tell me?", CONTENT);
										d.setNextChatId(14);
										return true;
									case 3:
										d.sendPlayerChat("Well never mind, it's Fluffs' loss.", CONTENT);
										d.setNextChatId(13);
										return true;
								}
								return false;
							case 9:
								d.sendNpcChat("W..wh..what?! Y..you wouldn't! Anyway, I'll deny it all", "and she'll be sure to believe me,", "over some wandering killer like you.", ANGRY_1);
								return true;
							case 10:
								d.sendPlayerChat("I'm an upstanding citizen!", ANGRY_1);
								return true;
							case 11:
								d.sendNpcChat("I'm her darling boy and you'd have to,", "forget about her rewarding you. Hop it snitch.", ANGRY_1);
								return true;
							case 12:
								d.sendStatement("You decide it's best not to aggravate him any more.");
								d.endDialogue();
								return true;
							case 13:
								d.sendNpcChat("I'm sure my mum will get over it.", CONTENT);
								d.endDialogue();
								return true;
							case 14:
								d.sendNpcChat("Well...now you ask, I am a bit short on cash.", CONTENT);
								return true;
							case 15:
								d.sendPlayerChat("How much?", CONTENT);
								return true;
							case 16:
								d.sendNpcChat("100 coins should cover it.", CONTENT);
								return true;
							case 17:
								d.sendPlayerChat("100 coins! Why should I pay you?", CONTENT);
								return true;
							case 18:
								d.sendNpcChat("You shouldn't, but I won't help otherwise. I never liked", "that cat anyway, so what do you say?", CONTENT);
								return true;
							case 19:
								player.getDialogue().sendOption("I'm not paying you a penny.", "Okay then, I'll pay.");
								return true;
							case 20:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("I'm not paying you a penny.", CONTENT);
										d.setNextChatId(21);
										return true;
									case 2:
										d.sendPlayerChat("Okay then, I'll pay.", CONTENT);
										d.setNextChatId(22);
										return true;
								}
								return false;
							case 21:
								d.sendNpcChat("Okay then, I'll find another way to make money.", "You only have yourself to blame if I'm", "forced into a life of crime.", CONTENT);
								d.endDialogue();
								return true;
							case 22:
								if (player.getInventory().playerHasItem(995, 100)) {
									d.sendGiveItemNpc("You give the lad 100 coins.", new Item(995, 100));
									player.getInventory().removeItem(new Item(995, 100));
									player.setQuestStage(getQuestID(), PAID_SHILOP);
									d.setNextChatId(1);
								} else {
									d.sendStatement("You don't have enough coins.");
									d.endDialogue();
								}
								return true;
						}
						return false;
					case PAID_SHILOP:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("There you go, now where did you see Fluffs?", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("I play at an abandoned lumber mill to the north east.", "Just beyond the Jolly Boar Inn. I saw Fluffs running", "around in there..", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("Anything else?", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("Well, you'll have to find the broken fence to get in. I'm", "sure you can manage that.", CONTENT);
								player.setQuestStage(getQuestID(), PAID_SHILOP_2);
								d.endDialogue();
								return true;
						}
						return false;
					case PAID_SHILOP_2:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Where did you say you saw Fluffs? ", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Weren't you listening? I saw the fleabag in the old Lumber Yard", "Walk past the Jolly Boar Inn and you should find it.", CONTENT);
								d.endDialogue();
								return true;
						}
						return false;
					default:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello there.", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Leave me alone.", ANGRY_1);
								d.endDialogue();
								return true;
						}
						return false;
				}
		}
		return false;
	}
}
