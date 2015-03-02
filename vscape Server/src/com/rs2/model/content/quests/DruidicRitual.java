package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_2;
import static com.rs2.model.content.dialogue.Dialogues.ANNOYED;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.DELIGHTED_EVIL;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.NEAR_TEARS_2;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import static com.rs2.model.content.dialogue.Dialogues.VERY_SAD;
import static com.rs2.model.content.dialogue.Dialogues.checkTrim;
import static com.rs2.model.content.dialogue.Dialogues.trimCape;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.*;
import com.rs2.model.players.ShopManager;

public class DruidicRitual implements Quest {

	public static final int QUEST_STARTED = 1;
	public static final int RAW_MEAT_GATHERING = 2;
	public static final int GATE_TOUCHED = 3;
	public static final int MEATS_DIPPED = 4;
	public static final int QUEST_COMPLETE = 5;

	public int dialogueStage = 0;
	private int reward[][] = {};
	private int expReward[][] = {
		{Skill.HERBLORE, 250}
	};

	private static final int questPointReward = 4;

	public int getQuestID() {
		return 10;
	}

	public String getQuestName() {
		return "Druidic Ritual";
	}

	public String getQuestSaveName() {
		return "druidic-ritual";
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
		player.getActionSender().sendString("You have completed: " + getQuestName(), 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("4 Quest Points", 12150);
		player.getActionSender().sendString((int) (expReward[0][1] * Constants.EXP_RATE) + " Herblore Experience", 12151);
		player.getActionSender().sendString("Easy access to the Herblore Skill", 12152);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), 7355);
	}

	public void sendQuestRequirements(Player player) {
		String prefix = "";
		int questStage = player.getQuestStage(getQuestID());
		if (questStage == QUEST_STARTED) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Kaqemeex", 8147);
			player.getActionSender().sendString("@str@" + "found in the druidic circle in Taverly.", 8148);

			player.getActionSender().sendString("Kaqemeex needs your help, he told you to speak", 8150);
			player.getActionSender().sendString("with Sanfew in central Taverly.", 8151);
		} else if (questStage == RAW_MEAT_GATHERING) {
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Kaqemeex", 8147);
			player.getActionSender().sendString("@str@" + "found in the druidic circle in Taverly.", 8148);
			player.getActionSender().sendString("@str@" + "Kaqemeex needs your help, he told you to speak", 8150);
			player.getActionSender().sendString("@str@" + "with Sanfew in central Taverly.", 8151);

			player.getActionSender().sendString("Sanfew has told you to gather some meat.", 8153);
			player.getActionSender().sendString("He said you need:", 8154);
			player.getActionSender().sendString("Raw Beef, Raw Rat Meat, Raw Bear, and Raw Chicken.", 8155);
			player.getActionSender().sendString("You need to dip these into the Cauldron of Thunder.", 8156);
			player.getActionSender().sendString("then return to Sanfew.", 8157);
		} else if (questStage == GATE_TOUCHED) {
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Kaqemeex", 8147);
			player.getActionSender().sendString("@str@" + "found in the druidic circle in Taverly.", 8148);
			player.getActionSender().sendString("@str@" + "Kaqemeex needs your help, he told you to speak", 8150);
			player.getActionSender().sendString("@str@" + "with Sanfew in central Taverly.", 8151);

			player.getActionSender().sendString("Sanfew has told you to gather some meat.", 8153);
			player.getActionSender().sendString("He said you need:", 8154);
			player.getActionSender().sendString("Raw Beef, Raw Rat Meat, Raw Bear, and Raw Chicken.", 8155);
			player.getActionSender().sendString("You need to dip these into the Cauldron of Thunder,", 8156);
			player.getActionSender().sendString("then return to Sanfew.", 8157);
		} else if (questStage == MEATS_DIPPED) {
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Kaqemeex", 8147);
			player.getActionSender().sendString("@str@" + "found in the druidic circle in Taverly.", 8148);
			player.getActionSender().sendString("@str@" + "Kaqemeex needs your help, he told you to speak", 8150);
			player.getActionSender().sendString("@str@" + "with Sanfew in central Taverly.", 8151);
			player.getActionSender().sendString("@str@" + "Sanfew has told you to gather some meat.", 8153);
			player.getActionSender().sendString("@str@" + "He said you need:", 8154);
			player.getActionSender().sendString("@str@" + "Raw Beef, Raw Rat Meat, Raw Bear, and Raw Chicken.", 8155);
			player.getActionSender().sendString("@str@" + "You need to dip these into the Cauldron of Thunder.", 8156);
			player.getActionSender().sendString("@str@" + "then return to Sanfew.", 8157);

			player.getActionSender().sendString("Speak with Kaqemeex.", 8159);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Kaqemeex", 8147);
			player.getActionSender().sendString("@str@" + "found in the druidic circle in Taverly.", 8148);
			player.getActionSender().sendString("@str@" + "Kaqemeex needs your help, he told you to speak", 8150);
			player.getActionSender().sendString("@str@" + "with Sanfew in central Taverly.", 8151);
			player.getActionSender().sendString("@str@" + "Sanfew has told you to gather some meat.", 8153);
			player.getActionSender().sendString("@str@" + "He said you need:", 8154);
			player.getActionSender().sendString("@str@" + "Raw Beef, Raw Rat Meat, Raw Bear, and Raw Chicken.", 8155);
			player.getActionSender().sendString("@str@" + "You need to dip these into the Cauldron of Thunder.", 8156);
			player.getActionSender().sendString("@str@" + "then return to Sanfew.", 8157);
			player.getActionSender().sendString("@str@" + "Speak with Kaqemeex.", 8159);

			player.getActionSender().sendString("@red@You have completed this quest!", 8161);
		} else {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString(prefix + "To start the quest, you should talk to Kaqemeex", 8147);
			player.getActionSender().sendString(prefix + "found in the druidic circle in Taverly.", 8148);
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 7355);
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
			player.getActionSender().sendString("@yel@" + getQuestName(), 7355);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 7355);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 7355);
		}

	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		String prefix = "";
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString(prefix + "To start the quest, you should talk to Kaqemeex", 8147);
		player.getActionSender().sendString(prefix + "found in the druidic circle in Taverly.", 8148);
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
			case 454: //sanfew good men
				switch (player.getDialogue().getChatId()) {
					case 1:
						if (player.getQuestStage(10) == 0) {
							player.getDialogue().sendNpcChat("Hello there, are you interested in Herblore?", CONTENT);
							return true;
						} else if (player.getQuestStage(10) == 1) {
							player.getDialogue().sendPlayerChat("Kaqemeex sent me.", "It's about your dark wizard problem.", CONTENT);
							player.getDialogue().setNextChatId(5);
							return true;
						} else if (player.getQuestStage(10) == 2 || player.getQuestStage(10) == 3) {
							player.getDialogue().sendNpcChat("Did you get those meats dipped into the Cauldron?", CONTENT);
							player.getDialogue().setNextChatId(25);
							return true;
						}
					case 2:
						player.getDialogue().sendOption("Yes.", "No.");
						return true;
					case 3:
						switch (optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Yes.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("No.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
					case 4:
						player.getDialogue().sendNpcChat("Hmm, well, you should speak to Kaqemeex.", "He's in the druidic circle north of here.", "We have a slight problem on our hands.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					case 5:
						player.getDialogue().sendNpcChat("Ah yes, the circle near Varrock.", "Such a pity.", "Unfortunately there is not much we can do...", SAD);
						return true;
					case 6:
						player.getDialogue().sendNpcChat("...Unless...", CONTENT);
						return true;
					case 7:
						player.getDialogue().sendStatement("Sanfew begins to mumble and argue to himself.");
						return true;
					case 8:
						player.getDialogue().sendPlayerChat("Erm, hello?", CONTENT);
						return true;
					case 9:
						player.getDialogue().sendNpcChat("Oh, yes, sorry...", "...You wouldn't happen to have an affinity", "for killing animals would you?", CONTENT);
						return true;
					case 10:
						player.getDialogue().sendNpcChat("Because there is a way to dispell the dark wizard's", "influence over the circle.", CONTENT);
						return true;
					case 11:
						player.getDialogue().sendPlayerChat("What do animals have to do with it?", CONTENT);
						return true;
					case 12:
						player.getDialogue().sendNpcChat("There's an ancient herblore recipe that is", "very, very powerful.", "Only a few elders, such as myself, know it.", "It may just fend off the dark magic.", CONTENT);
						return true;
					case 13:
						player.getDialogue().sendPlayerChat("Well, what do I need to do?", CONTENT);
						return true;
					case 14:
						player.getDialogue().sendNpcChat("I need you to gather some raw meat.", "Raw beef, raw rat meat, raw bear, and raw chicken...", "...to be exact.", CONTENT);
						return true;
					case 15:
						player.getDialogue().sendPlayerChat("That's all you need for the recipe?", CONTENT);
						return true;
					case 16:
						player.getDialogue().sendNpcChat("No, you need to take all four raw meats", "to the dungeon southwest of here.", "You then need to dip them in the almighty", "Cauldron of Thunder!", CONTENT);
						player.setQuestStage(10, 2);
						return true;
					case 17:
						player.getDialogue().sendStatement("Sanfew begins making wild gestures and sounds.");
						return true;
					case 18:
						player.getDialogue().sendPlayerChat("Err, I'm still here.", CONTENT);
						return true;
					case 19:
						player.getDialogue().sendNpcChat("Well, what are you waiting for!?", "This is a dire matter!", ANGRY_2);
						return true;
					case 20:
						player.getDialogue().sendPlayerChat("I suppose I'll go fetch some meat.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					case 25:
						if (player.getInventory().playerHasItem(522) && player.getInventory().playerHasItem(523) && player.getInventory().playerHasItem(524) && player.getInventory().playerHasItem(525)) {
							player.getDialogue().sendPlayerChat("I've got them all right here.", CONTENT);
							return true;
						} else {
							player.getDialogue().sendPlayerChat("I'm afraid not...", SAD);
							player.getDialogue().endDialogue();
							return true;
						}
					case 26:
						player.getDialogue().sendStatement("You hand Sanfew the 4 enchanted meats.");
						player.getInventory().removeItem(new Item(522));
						player.getInventory().removeItem(new Item(523));
						player.getInventory().removeItem(new Item(524));
						player.getInventory().removeItem(new Item(525));
						player.setQuestStage(10, 4);
						return true;
					case 27:
						player.getDialogue().sendNpcChat("Let me see if I can whip these up.", HAPPY);
						return true;
					case 28:
						player.getDialogue().sendStatement("After some odd motions, lights, and more mumbling...", "...Sanfew seems to have dispelled the dark magic.");
						return true;
					case 29:
						player.getDialogue().sendNpcChat("IT WORKED!", "This is a great day indeed my friend!", LAUGHING);
						return true;
					case 30:
						player.getDialogue().sendNpcChat("Go tell Kaqemeex the good news!", HAPPY);
						player.getDialogue().endDialogue();
						return true;
				}
				return false;
			case 10999: //gates to cauldron
				switch (player.getDialogue().getChatId()) {
					case 1:
						if (player.getQuestStage(10) == 2) {
							player.getDialogue().sendStatement("As you touch the gates, a suit of armor jumps out behind you!");
							player.getDialogue().endDialogue();
							Npc suit = new Npc(4279);
							suit.setSpawnPosition(new Position(2886, 9832, 0));
							suit.setPosition(new Position(2886, 9832, 0));
							suit.setNeedsRespawn(false);
							suit.setPlayerOwner(player.getIndex());
							suit.setCombatDelay(2);
							World.register(suit);
							CombatManager.attack(suit, player);

							player.setQuestStage(10, 3);
							return true;
						}
				}
				return false;
			case 455: //Herblore master skillcape kaqemeex it up
				switch (player.getDialogue().getChatId()) {
					case 1:
						if (player.getQuestStage(10) < 1) {
							player.getDialogue().sendNpcChat("Oh dear... Oh dear...", VERY_SAD);
							player.getDialogue().setNextChatId(15);
							return true;
						} else if (player.getQuestStage(10) == 4) {
							player.getDialogue().sendPlayerChat("It worked! Sanfew's meats worked!", HAPPY);
							player.getDialogue().setNextChatId(25);
							return true;
						} else {
							player.getDialogue().sendNpcChat("Hello!", CONTENT);
							return true;
						}
					case 2:
						if (player.getSkill().getLevel()[Skill.HERBLORE] == 99 && !checkTrim(player)) {
							player.getDialogue().sendOption("(don't respond)", "I want my skillcape!");
							player.getDialogue().setNextChatId(4);
							return true;
						} else if (player.getSkill().getLevel()[Skill.HERBLORE] == 99 && checkTrim(player)) {
							player.getDialogue().sendOption("(don't respond)", "I want my skillcape!", "Can you trim my skillcape?");
							player.getDialogue().setNextChatId(4);
							return true;
						} else {
							player.getDialogue().sendPlayerChat("How do you spend your free time?", CONTENT);
							player.getDialogue().setNextChatId(3);
							return true;
						}
					case 3:
						player.getDialogue().sendNpcChat("Cleaning guams, of course!", "It's my favorite activity next to napping!", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					case 4:
						switch (optionId) {
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
						ShopManager.openShop(player, 181);
						player.getDialogue().dontCloseInterface();
						return true;
					case 8:
						player.getDialogue().sendNpcChat("Sure, but this is irreversible!", "However, you can always buy another untrimmed cape.", CONTENT);
						return true;
					case 9:
						if (player.getInventory().playerHasItem(9774)) {
							player.getDialogue().sendNpcChat("Here you are.", CONTENT);
							trimCape(player, 9774);
						} else {
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
					case 15:
						player.getDialogue().sendPlayerChat("What's wrong?", CONTENT);
						return true;
					case 16:
						player.getDialogue().sendNpcChat("Well, we druids used to own the circle", "just south of Varrock...", SAD);
						return true;
					case 17:
						player.getDialogue().sendNpcChat("But, recently the dark wizards have", "used dark magic to corrupt it!", ANNOYED);
						return true;
					case 18:
						player.getDialogue().sendOption("Sounds like you could use some help!", "Good luck with that.");
						return true;
					case 19:
						switch (optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Sounds like you could use some help!", HAPPY);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Good luck with that.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
					case 20:
						player.getDialogue().sendNpcChat("You're willing to help!?", "Speak to Sanfew in the center of town,", "he knows the details better than I!", HAPPY);
						return true;
					case 21:
						player.getDialogue().sendPlayerChat("Okay, I'll go find Sanfew.", CONTENT);
						player.getDialogue().endDialogue();
						player.setQuestStage(10, 1);
						QuestHandler.getQuests()[10].startQuest(player);
						return true;
					case 25:
						player.getDialogue().sendNpcChat("That's  wonderful!", "Finally the dark magic is dispelled!", HAPPY);
						return true;
					case 26:
						player.getDialogue().sendStatement("Kaqemeex pauses.");
						return true;
					case 27:
						player.getDialogue().sendNpcChat("You do know what we have to thank for this, right?", CONTENT);
						return true;
					case 28:
						player.getDialogue().sendPlayerChat("Uhm... Me?", CONTENT);
						return true;
					case 29:
						player.getDialogue().sendNpcChat("Not quite! It's the power of herblore!", "Herblore is the great art of potion making.", CONTENT);
						return true;
					case 30:
						player.getDialogue().sendNpcChat("Potions can do everything from healing ailments,", "to boosting your natural abilities!", HAPPY);
						return true;
					case 31:
						player.getDialogue().sendNpcChat("Oh? It seems you don't know much about Herblore.", "Well, that needs to change!", CONTENT);
						return true;
					case 32:
						player.getDialogue().sendStatement("Kaqemeex delves into the lore and history of Herblore.");
						return true;
					case 33:
						player.getDialogue().sendNpcChat("And that's how all of Gielinor was saved by herbs!", LAUGHING);
						return true;
					case 34:
						player.getDialogue().endDialogue();
						player.setQuestStage(10, 5);
						QuestHandler.completeQuest(player, 10);
						return true;
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
