package com.rs2.model.content.quests.impl;

import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.dialogue.DialogueManager;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.net.ActionSender;
import com.rs2.util.Misc;

public class MonksFriend implements Quest {

	public static final int questIndex = 7369;

	public static final int QUEST_STARTED = 1;
	public static final int OFF_TO_SLEEP = 2;
	public static final int PARTY_PREP = 3;
	public static final int SOBERING_CEDRIC = 4;
	public static final int GATHERING_WOOD = 5;
	public static final int PARTY_TIME = 6;
	public static final int QUEST_COMPLETE = 7;

	public static final int LAW_RUNE = 563;
	public static final int CHILDS_BLANKET = 90;
	public static final int JUG_OF_WATER = 1937;
	public static final int LOGS = 1511;

	public static final int BROTHER_OMAD = 279;
	public static final int BROTHER_CEDRIC = 280;
	
	public static final Position[] balloonPositions = {new Position(2610, 3208, 0), new Position(2608, 3211, 0), new Position(2608, 3210, 0), new Position(2602, 3208, 0), new Position(2605, 3207, 0), new Position(2607, 3212, 0), new Position(2605, 3211, 0), new Position(2606, 3210, 0), new Position(2606, 3208, 0), new Position(2605, 3210, 0), new Position(2605, 3210, 0)};
	public static final String[] partyStrings = {"Party!", "Woop!", "Let's boogie!", "Oh baby!", "GO!", "Get down!", "Feel the rhythm!", "Oh my!", "Watch me go!", "You go!", "Now that's a party!"};

	private int reward[][] = {
		{LAW_RUNE, 8}
	};

	private int expReward[][] = {
		{Skill.WOODCUTTING, 2000}
	};

	private static final int questPointReward = 1;

	public int getQuestID() {
		return 45;
	}

	public String getQuestName() {
		return "Monk's Friend";
	}

	public String getQuestSaveName() {
		return "monksfriend";
	}

	public boolean canDoQuest(final Player player) {
		return true;
	}

	public void getReward(final Player player) {
		for (int[] rewards : reward) {
			player.getInventory().addItemOrDrop(new Item(rewards[0], rewards[1]));
		}
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				b.stop();
			}

			@Override
			public void stop() {
				for (final int[] expRewards : expReward) {
					player.getSkill().addExp(expRewards[0], (expRewards[1]));
				}
			}
		}, 4);
		player.addQuestPoints(questPointReward);
		player.getActionSender().QPEdit(player.getQuestPoints());
	}

	public void completeQuest(Player player) {
		getReward(player);
		player.getActionSender().sendInterface(12140);
		player.getActionSender().sendItemOnInterface(12145, 250, LAW_RUNE); //zoom, then itemId
		player.getActionSender().sendString("You have completed " + getQuestName() + "!", 12144);
		player.getActionSender().sendString("You are awarded: ", 12146);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("8 Law Runes", 12151);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), questIndex);
	}

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		int lastIndex = 0;
		switch (questStage) {
			case QUEST_COMPLETE:
				lastIndex = 16;
				break;
		}
		lastIndex++;

		ActionSender a = player.getActionSender();
		a.sendQuestLogString("Talk to Brother Omad at the Monastery south", 1, this.getQuestID(), 0);
		a.sendQuestLogString("of Ardougne to begin.", 2, this.getQuestID(), 0);
		a.sendQuestLogString("I need to retrieve the blanket from the thieves.", 4, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("Brother Omad said the hideout is hidden in a circle of stones.", 5, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("Omad went off to sleep. I should talk to him later.", 7, this.getQuestID(), PARTY_PREP);
		a.sendQuestLogString("I need to search for Brother Cedric.", 9, this.getQuestID(), PARTY_PREP);
		a.sendQuestLogString("Omad said he might be in the forest.", 10, this.getQuestID(), PARTY_PREP);
		a.sendQuestLogString("Brother Cedric requires a jug of water to sober up.", 12, this.getQuestID(), SOBERING_CEDRIC);
		a.sendQuestLogString("Brother Cedric requires logs to fix the cart.", 14, this.getQuestID(), GATHERING_WOOD);
		a.sendQuestLogString("I should head back to Brother Omad.", 16, this.getQuestID(), PARTY_TIME);

		switch (questStage) {
			default:
				break;
			case 0:
				a.sendQuestLogString("Talk to @dre@Brother Omad@bla@ at the @dre@Monastery@bla@ south", 1, this.getQuestID(), 0);
				a.sendQuestLogString("of @dre@Ardougne@bla@ to begin.", 2, this.getQuestID(), 0);
				break;
			case QUEST_COMPLETE:
				a.sendQuestLogString("@red@" + "You have completed this quest!", lastIndex + 1);
				break;
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), questIndex);
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
			player.getActionSender().sendString("@yel@" + getQuestName(), questIndex);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), questIndex);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), questIndex);
		}
	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
		player.getActionSender().sendString(getQuestName(), 8144);
	}
	
	public static void startParty(final Player player, final Npc omad) {
		player.setStopPacket(true);
		omad.getMovementHandler().setCanWalk(false);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int count = 0;
			Entity dancer = omad;
			@Override
			public void execute(CycleEventContainer b) {
				count++;
				dancer = (count & 1) == 0 ? player : omad;
				player.setStopPacket(true);
				if (count > 11) {
					b.stop();
				} else {
					Position p = balloonPositions[count - 1];
					dancer.getUpdateFlags().sendAnimation(866);
					dancer.getUpdateFlags().setForceChatMessage(partyStrings[count - 1]);
					new GameObject(115 + Misc.random(7), p.getX(), p.getY(), 0, 0, 10, -1, 99999, false);
				}
			}

			@Override
			public void stop() {
				player.setStopPacket(false);
				omad.getMovementHandler().setCanWalk(true);
				QuestHandler.completeQuest(player, 45);
				for(Position p : balloonPositions)
					ObjectHandler.getInstance().removeObject(p.getX(), p.getY(), 0, 10);
			}
		}, 3);
	}
	
	public static void spawnLadder(final Player player) {
		if (player.getQuestVars().spawnedStoneCircleLadder) {
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer b) {
					b.stop();
				}

				@Override
				public void stop() {
					player.getActionSender().sendObject(1754, 2561, 3222, 0, 0, 10);
					player.getActionSender().sendMessage("A ladder mysteriously appears.");
				}
			}, 1);
		}
	}
	
	public static void despawnLadder(final Player player) {
		if (!player.getQuestVars().spawnedStoneCircleLadder) {
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer b) {
					b.stop();
				}

				@Override
				public void stop() {
					if(player.getPosition().getY() < 9000)
						player.getActionSender().sendObject(-1, 2561, 3222, 0, 0, 10);
				}
			}, 1);
		}
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
		return false;
	}

	public boolean doItemOnNpc(Player player, int itemId, Npc npc) {
		return false;
	}

	public boolean doNpcClicking(Player player, Npc npc) {
		return false;
	}
        
        public boolean doNpcSecondClicking(Player player, Npc npc) {
            return false;
        }

	public boolean doObjectClicking(final Player player, int object, int x, int y) {
		switch (object) {
			case 1754:
				if (x == 2561 && y == 3222) {
					Ladders.climbLadderDown(player, new Position(2561, 9621, 0));
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

	public boolean sendDialogue(final Player player, final int id, int chatId, int optionId, int npcChatId) {
		DialogueManager d = player.getDialogue();
		switch (id) {
			case BROTHER_OMAD:
				switch (player.getQuestStage(this.getQuestID())) {
					case 0:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello there, what's wrong?");
								return true;
							case 2:
								d.sendNpcChat("*yawn*...Oh, hello...*yawn* I'm sorry! I'm just so tired!", "I haven't slept in a week!", Dialogues.DISTRESSED);
								return true;
							case 3:
								d.sendOption("Why can't you sleep, what's wrong?", "Sorry! I'm too busy to hear your problems!");
								return true;
							case 4:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if(optionId == 2)
									d.endDialogue();
								return true;
							case 5:
								d.sendNpcChat("It's brother Androe's son! With his constant: Waaaaaah!", "Waaaaaaaaah! Androe said it's natural, but it's so", "annoying!", Dialogues.DISTRESSED);
								return true;
							case 6:
								d.sendPlayerChat("I suppose that's what kids do.");
								return true;
							case 7:
								d.sendNpcChat("He was fine, up until last week! Thieves broke in! They", "stole his favourite sleeping blanket!", Dialogues.DISTRESSED);
								return true;
							case 8:
								d.sendNpcChat("Now he won't rest until it's returned... ...and that", "means neither can I!", Dialogues.DISTRESSED);
								return true;
							case 9:
								d.sendOption("Can I help at all?", "I'm sorry to hear that! I hope you find his blanket.");
								return true;
							case 10:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if(optionId == 2)
										d.endDialogue();
								return true;
							case 11:
								d.sendNpcChat("Please do. We won't be able to help you as we are", "peaceful men but we would be grateful for your help!");
								return true;
							case 12:
								d.sendPlayerChat("Where are they?");
								return true;
							case 13:
								d.sendNpcChat("They hide in a secret cave in the forest. It's hidden", "under a ring of stones. Please, bring back the blanket!");
								d.endDialogue();
								QuestHandler.startQuest(player, 45);
								return true;
						}

					case QUEST_STARTED:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello.");
								return true;
							case 2:
								d.sendNpcChat(
									"*yawn*...oh, hello again...*yawn*",
									Dialogues.DISTRESSED);
								return true;
							case 3:
								d.sendNpcChat(
									"Please tell me you have the blanket.",
									Dialogues.DISTRESSED);
								if ((player.carryingItem(CHILDS_BLANKET))) {
									player.getDialogue().setNextChatId(4);
								} else {
									player.getDialogue().setNextChatId(10);
								}
								return true;
							case 4:
								d.sendPlayerChat(
									"Yes! I've recovered it from the clutches of the evil",
									"thieves!");
								return true;
							case 5:
								d.sendGiveItemNpc(
									"You hand the monk the childs blanket.",
									new Item(CHILDS_BLANKET));
								return true;
							case 6:
								d.sendNpcChat(
									"Really, that's excellent, well done! Maybe now I will be",
									"able get some rest.");
								return true;
							case 7:
								player.getInventory().removeItem(new Item(CHILDS_BLANKET, 1));
								player.setQuestStage(45, OFF_TO_SLEEP);
								d.sendNpcChat("*yawn*..I'm off to bed! Farewell brave traveller!");
								d.endDialogue();
								return true;
							case 10:
								d.sendPlayerChat("No, not yet.");
								d.endDialogue();
								return true;
						}
					return false;
					case OFF_TO_SLEEP:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello, how are you?");
								return true;
							case 2:
								d.sendNpcChat("Much better now I'm sleeping well! Now I can organise", "the party.");
								return true;
							case 3:
								d.sendPlayerChat("Ooh! What party?");
								return true;
							case 4:
								d.sendNpcChat("The son of Brother Androe's birthday party. He's going", "to be one year old!");
								return true;
							case 5:
								d.sendPlayerChat("That's sweet!");
								return true;
							case 6:
								d.sendNpcChat("It's also a great excuse for a drink!");
								return true;
							case 7:
								d.sendNpcChat("We just need Brother Cedric to return with the wine.");
								return true;
							case 8:
								d.sendOption("Who's Brother Cedric?", "Enjoy it! I'll see you soon!");
								return true;
							case 9:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if(optionId == 2)
									d.endDialogue();
								return true;
							case 10:
								d.sendNpcChat("Cedric is a member of the order too. We sent him out", "three days ago to collect wine. But he didn't return!");
								return true;
							case 11:
								d.sendNpcChat("He most probably got drunk and lost in the forest!", Dialogues.ANGRY_1);
								return true;
							case 12:
								d.sendNpcChat("I don't suppose you could look for him?");
								return true;
							case 13:
								d.sendOption("I've no time for that, sorry.", "Where should I look?", "Can I come to the party?");
								return true;
							case 14:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								switch (optionId) {
									case 1:
										d.endDialogue();
										return true;
									case 3:
										d.setNextChatId(30);
										break;
								}
								return true;
							case 15:
								d.sendNpcChat("Oh, he won't be far. Probably out in the forest.");
								return true;
							case 16:
								d.sendPlayerChat("Ok, I'll go and find him.");
								d.endDialogue();
								player.setQuestStage(this.getQuestID(), PARTY_PREP);
								return true;
							case 30:
								d.sendNpcChat("Of course, but we need the wine first.");
								d.endDialogue();
								return true;
						}
					return false;
					case PARTY_TIME:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hi Omad, Brother Cedric is on his way!");
								return true;
							case 2:
								d.sendNpcChat("Good! Good! Now we can party!");
								return true;
							case 3:
								d.sendNpcChat("I have little to repay you with but please! Take these", "Rune Stones.");
								return true;
							case 4:
								d.sendGiveItemNpc("Brother Omad gives you 8 Law Runes.", new Item(LAW_RUNE));
								return true;
							case 5:
								d.sendPlayerChat("Thanks Brother Omad!");
								return true;
							case 6: // Dance party animation
								d.sendNpcChat("OK, let's party!");
								return true;
							case 7:
								player.getActionSender().removeInterfaces();
								startParty(player, World.getNpcs()[World.getNpcIndex(279)]);
								return true;
						}
					return false;
					case QUEST_COMPLETE:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Thank you again!",
									Dialogues.SLEEPY);
								d.endDialogue();
								return true;
						}
					return false;
				}
				return false;
			case BROTHER_CEDRIC:
				switch (player.getQuestStage(this.getQuestID())) {
					case PARTY_PREP:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Brother Cedric are you okay?");
								return true;
							case 2:
								d.sendNpcChat("Yeesshhh, I'm very, very drunk..hic..up..", Dialogues.SLEEPY);
								return true;
							case 3:
								d.sendPlayerChat("Brother Omad needs the wine for the party.");
								return true;
							case 4:
								d.sendNpcChat("Oh dear, oh dear, I knew I had to do something!", Dialogues.DISTRESSED);
								return true;
							case 5:
								d.sendNpcChat("Pleashhh, find me a jug of water. Once I'm sober I'll", "'elp you take the wine back.", Dialogues.DISTRESSED);
								d.endDialogue();
								player.setQuestStage(45, SOBERING_CEDRIC);
								return true;
						}
					return false;
					case SOBERING_CEDRIC:
						switch (d.getChatId()) {
							case 1:
								if (player.getQuestVars().givenMonkWater) {
									d.sendPlayerChat("How's it going?");
									player.getDialogue().setNextChatId(7);
								} else {
									d.sendPlayerChat("Are you okay?");
								}
								return true;
							case 2:
								d.sendNpcChat("Hic up! Oh my head! I need a jug of water.", Dialogues.SLEEPY);
								if (!player.getInventory().playerHasItem(JUG_OF_WATER)) {
									player.getDialogue().setNextChatId(20);
								}
								return true;
							case 3:
								d.sendPlayerChat("Cedric! Here, drink! I have some water.");
								return true;
							case 4:
								d.sendNpcChat("Good stuff, my head's spinning!", Dialogues.SLEEPY);
								return true;
							case 5:
								World.getNpcs()[player.getNpcClickIndex()].getUpdateFlags().setForceChatMessage("Gulp... Gulp!");
								d.sendGiveItemNpc("You hand the monk a jug of water.", new Item(JUG_OF_WATER));
								player.getInventory().removeItem(new Item(JUG_OF_WATER));
								player.getQuestVars().givenMonkWater = true;
								return true;
							case 6:
								d.sendNpcChat("Aah! That's better!", Dialogues.HAPPY);
								return true;
							case 7:
								d.sendNpcChat("I just need to fix this cart and we can go party.");
								return true;
							case 8:
								d.sendNpcChat("Could you help?");
								return true;
							case 9:
								d.sendOption("No, I've helped enough monks today!", "Yes, I'd be happy to!");
								return true;
							case 10:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if(optionId == 1)
									d.endDialogue();
								return true;
							case 11:
								d.sendNpcChat("Excellent, I just need some wood.");
								return true;
							case 12:
								d.sendPlayerChat("OK, I'll see what I can find.");
								d.endDialogue();
								player.setQuestStage(45, GATHERING_WOOD);
								return true;
							case 20:
								d.sendPlayerChat("Alright.");
								d.endDialogue();
								return true;
						}
					return false;
					case PARTY_TIME:
						switch(d.getChatId()) {
							case 1:
								d.sendNpcChat("Well done! Now I'll fix this cart. You head back to", "Brother Omad and tell him I'll be there soon.");
								d.endDialogue();
								return true;
						}
					return false;
					case GATHERING_WOOD:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Did you manage to get some wood?");
								if (player.getInventory().playerHasItem(LOGS)) {
									player.getDialogue().setNextChatId(2);
								} else {
									player.getDialogue().setNextChatId(20);
								}
								return true;
							case 2:
								d.sendGiveItemNpc("You hand Cedric some logs.", new Item(LOGS));
								return true;
							case 3:
								d.sendPlayerChat("Here you go!");
								return true;
							case 4:
								d.sendNpcChat("Well done! Now I'll fix this cart. You head back to", "Brother Omad and tell him I'll be there soon.");
								return true;
							case 5:
								d.sendPlayerChat("Ok! I'll see you later!");
								d.endDialogue();
								player.setQuestStage(45, PARTY_TIME);
								player.getInventory().removeItem(new Item(LOGS));
								return true;
							case 20:
								d.sendPlayerChat("No, not yet.");
								d.endDialogue();
								return true;
						}
						return false;
				}
				return false;
		}
		return false;
	}

}
