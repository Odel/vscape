package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.CALM;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.NEAR_TEARS;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.*;

public class CooksAssistant implements Quest {

	public static final int QUEST_STARTED = 1;
	public static final int ITEMS_GIVEN = 2;
	public static final int QUEST_COMPLETE = 3;

	public int dialogueStage = 0;
	private int reward[][] = {
		{995, 0}, //itemID, amount
	};
	private int expReward[][] = {
		{Skill.COOKING, 300} // skill ID, exp amount
	};
	private static final int questPointReward = 1;

	public int getQuestID() {
		return 0;
	}

	public String getQuestName() {
		return "Cook's Assistant";
	}

	public String getQuestSaveName() {
		return "cooks-assistant";
	}

	public boolean canDoQuest(Player player) {
		return true;
	}

	public void getReward(Player player) {
		for (int[] rewards : reward) {
			player.getInventory().addItem(new Item(rewards[0], rewards[1]));
		}
		for (int[] expRewards : expReward) {
			player.getSkill().addExp(expRewards[0], (expRewards[1])); //DO NOT TIMES EXP BY *Constants.EXP_RATE (its done by addexp)
		}
		player.addQuestPoints(questPointReward);
		player.getActionSender().QPEdit(player.getQuestPoints());
	}

	public void completeQuest(Player player) {
		getReward(player);
		player.getActionSender().sendInterface(12140);
		player.getActionSender().sendString("You have completed: " + getQuestName(), 12144);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString((int) (expReward[0][1] * Constants.EXP_RATE) + " Cooking Experience", 12151);
		player.getActionSender().sendString("", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@Cook's Assistant", 7333);
	}

	public void sendQuestRequirements(Player player) {
		String prefix = "";
		int questStage = player.getQuestStage(getQuestID());
		if (questStage == QUEST_STARTED) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk with the", 8147);
			player.getActionSender().sendString("@str@" + "Cook found in Lumbridge Castle.", 8148);

			player.getActionSender().sendString("Cook has asked you to get the following items:", 8150);
			player.getActionSender().sendString("Bucket of Milk", 8151);
			player.getActionSender().sendString("Pot of Flour", 8152);
			player.getActionSender().sendString("an Egg", 8153);
		} else if (questStage == ITEMS_GIVEN) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk with the", 8147);
			player.getActionSender().sendString("@str@" + "Cook found in Lumbridge.", 8148);

			player.getActionSender().sendString("Cook has asked you to get the following items:", 8150);
			player.getActionSender().sendString("@str@" + "Bucket of Milk", 8151);
			player.getActionSender().sendString("@str@" + "Pot of Flour", 8152);
			player.getActionSender().sendString("@str@" + "an Egg", 8153);

			player.getActionSender().sendString("@str@" + "You brought Cook all the items", 8154);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk with the", 8147);
			player.getActionSender().sendString("@str@" + "Cook found in Lumbridge Castle.", 8148);

			player.getActionSender().sendString("@str@ Cook has asked you to get the following items:", 8150);
			player.getActionSender().sendString("@str@" + "Bucket of Milk", 8151);
			player.getActionSender().sendString("@str@" + "Pot of Flour", 8152);
			player.getActionSender().sendString("@str@" + "an Egg", 8153);

			player.getActionSender().sendString("@str@" + "You brought Cook all the items and were rewarded!", 8154);

			player.getActionSender().sendString("@red@You have completed this quest!", 8155);
		} else {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString(prefix + "To start the quest, you should talk with the", 8147);
			player.getActionSender().sendString(prefix + "Cook found in Lumbridge Castle.", 8148);
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@Cook's Assistant", 7333);
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
			player.getActionSender().sendString("@yel@Cook's Assistant", 7333);
		} else if (questStage >= QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@Cook's Assistant", 7333);
		} else {
			player.getActionSender().sendString("@red@Cook's Assistant", 7333);
		}

	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		String prefix = "";
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString(prefix + "To start the quest, you should talk with Cook", 8147);
		player.getActionSender().sendString(prefix + "found in Lumbridge.", 8148);
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
			case 278: //Lummy castle cook - quest npc - cooks assistant
				switch (player.getQuestStage(0)) {
					case 0:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("What am I going to do?", DISTRESSED);
								return true;
							case 2:
								player.getDialogue().sendOption("What's wrong?", "Can you make me a cake?", "You don't look very happy.", "Nice hat!");
								return true;
							case 3:
								switch (optionId) {
									case 1:
										player.getDialogue().sendNpcChat("Oh dear, oh dear, oh dear, I'm in a terrible, terrible", "mess! It's the Duke's birthday today, and i should be", "making him a lovely, big birthday cake using special", "ingredients...", DISTRESSED);
										player.getDialogue().setNextChatId(9);
										return true;
									case 2:
										player.getDialogue().sendNpcChat("*sniff* Don't talk about cakes...", NEAR_TEARS);
										player.getDialogue().setNextChatId(2);
										return true;
									case 3:
										player.getDialogue().sendNpcChat("No, I'm not. The world is caving in around me - I'm", "overcome with dark feelings of impending doom.", SAD);
										player.getDialogue().setNextChatId(2);
										return true;
									case 4:
										player.getDialogue().sendNpcChat("Er, thank you. It's a pretty ordinary cook's hat, really.", DISTRESSED);
										player.getDialogue().setNextChatId(5);
										return true;
								}
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("Still, it suits you. The trousers are pretty special too.", HAPPY);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("It's all standard-issue cook's uniform...", SAD);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("The whole hat, apron and stripy trousers ensemble...it", "works. It makes you looks like a real cook.", HAPPY);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("I AM a real cook! I haven't got time to be chatting", "about culinary fashion, I'm in desperate need of help!", ANGRY_1);
								player.getDialogue().setNextChatId(2);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("...but I've forgotten to get the ingredients. I'll never get", "them in time now. He'll sack me! What ever will i do? I", "have four children and a goat to look after. Would you", "help me? Please?", DISTRESSED);
								return true;
							case 10:
								player.getDialogue().sendOption("I'm always happy to help a cook in distress.", "Sorry, I have troubles of my own.");
								return true;
							case 11:
								switch (optionId) {
									case 1:
										player.getDialogue().sendNpcChat("Oh thank you, thank you. I need milk, an egg and", "flour. I'd be very grateful if you can get them for me.", HAPPY);
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
						return false;
					case 1:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("How are you getting on with finding the ingredients?", DISTRESSED);
								if (!(player.carryingItem(1944) && player.carryingItem(1933) && player.carryingItem(1927))) {
									player.getDialogue().setNextChatId(2);
								} else {
									player.getDialogue().setNextChatId(8);
								}
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("I still haven't got them all yet, I'm still looking.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Please get the ingredients quickly. I'm running out of", "time! The Duke will throw my goat and I onto the street!", DISTRESSED);
								return true;
							case 4:
								player.getDialogue().sendOption("I'll get right on it.", "Where can I find the ingredients?");
								return true;
							case 5:
								switch (optionId) {
									case 1:
										player.getDialogue().sendNpcChat("Please hurry!", DISTRESSED);
										player.getDialogue().endDialogue();
										return true;
									case 2:
										player.getDialogue().sendNpcChat("You can mill flour at the windmill", "You can find eggs by killing chickens.", "You can find milk by milking a cow.", HAPPY);
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
								player.getDialogue().sendPlayerChat("Here's a bucket of milk,", "a pot of flour,", "and a fresh egg.", HAPPY);
								player.getInventory().removeItem(new Item(1944, 1));
								player.getInventory().removeItem(new Item(1927, 1));
								player.getInventory().removeItem(new Item(1933, 1));
								player.setQuestStage(0, 2);
								player.getDialogue().setNextChatId(1);
								return true;
							case 9:
								player.getDialogue().sendPlayerChat("So where do I find these ingrediants then?", HAPPY);
								return true;
							case 10:
								player.getDialogue().sendOption("Where do I find some flour?", "How about some milk?", "And eggs? Where are they found?", "Actually, I know where to find this stuff.");
								return true;
							case 11:
								switch (optionId) {
									case 1:
										player.getDialogue().sendNpcChat("You can mill flour at the windmill", HAPPY);
										player.getDialogue().setNextChatId(10);
										return true;
									case 2:
										player.getDialogue().sendNpcChat("You can find eggs by killing chickens.", HAPPY);
										player.getDialogue().setNextChatId(10);
										return true;
									case 3:
										player.getDialogue().sendNpcChat("You can find milk by milking a cow", HAPPY);
										player.getDialogue().setNextChatId(10);
										return true;
									case 4:
										player.getDialogue().endDialogue();
										return true;
								}
								return true;
						}
						return false;
					case 2:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("You've given me everything I need! I am saved!", HAPPY);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Thank you!", HAPPY);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("So do I get to go to the Duke's party?", HAPPY);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("I'm afraid not.", "Only the big cheeses get to Dine with the Duke.", SAD);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("Well, maybe one day I'll be important enough to sit at", "the Duke's table.", CALM);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("Maybe, but I won't be holding my breath.", HAPPY);
								return true;
							case 7:
								player.getDialogue().endDialogue();
								player.setQuestStage(0, 3);
								QuestHandler.completeQuest(player, 0);
								return true;
						}
						return false;
					default:
						return false;
				}
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
