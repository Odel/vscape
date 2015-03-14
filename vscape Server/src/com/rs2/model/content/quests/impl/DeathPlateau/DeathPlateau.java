package com.rs2.model.content.quests.impl.DeathPlateau;

import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.dialogue.DialogueManager;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.SAD;

import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.quests.impl.Quest;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.objects.functions.Doors;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.net.ActionSender;
import com.rs2.util.Misc;

public class DeathPlateau implements Quest {

	public static final int questIndex = 8438; //Used in player's quest log interface, id is in Player.java, Change
	//Quest stages
	public static final int QUEST_STARTED = 1;
	public static final int TALKED_TO_EOHRIC = 2;
	public static final int BOOZE_UP_HAROLD = 3;
	public static final int IOU_GET = 4;
	public static final int COMBINATION_GET = 5;
	public static final int DOOR_UNLOCKED = 6;
	public static final int FIND_SHERPA = 7;
	public static final int QUEST_COMPLETE = 20;

	//Items
	public static final int STEEL_CLAWS = 3097;
	public static final int COMBINATION = 3102;
	public static final int IOU = 3103;
	public static final int SECRET_WAY_MAP = 3104;
	public static final int CLIMBING_BOOTS = 3105;
	public static final int SPIKED_BOOTS = 3107;
	public static final int RED_BALL = 3109;
	public static final int BLUE_BALL = 3110;
	public static final int YELLOW_BALL = 3111;
	public static final int PURPLE_BALL = 3112;
	public static final int GREEN_BALL = 3113;
	public static final int CERTIFICATE = 3114;

	//Positions
	public static final Position POSITION = new Position(0, 0, 0);

	//Interfaces
	

	//Npcs
	public static final int DENULTH = 1060;
	public static final int SOLDIER_INJURED = 1069;
	public static final int SABA = 1070;
	public static final int TENZING = 1071;
	public static final int EADBURG = 1072;
	public static final int ARCHER = 1074;
	public static final int HAROLD = 1078;
	public static final int TOSTIG = 1079;
	public static final int EOHRIC = 1080;
	public static final int SERVANT = 1081;
	public static final int DUNSTAN = 1082;

	//Objects
	public static final int HAROLDS_DOOR = 3747;
	public static final int EQUIPMENT_DOOR = 3743;
	public static final int CAVE_ENTRANCE = 3735;
	public static final int CAVE_EXIT = 3760;

	private int reward[][] = { //{itemId, count},
		{STEEL_CLAWS, 1}
	};

	private int expReward[][] = { //{skillId, exp},
		{Skill.ATTACK, 3000}
	};

	private static final int questPointReward = 1; //Change

	public int getQuestID() { //Change
		return 43;
	}

	public String getQuestName() { //Change
		return "Death Plateau";
	}

	public String getQuestSaveName() { //Change
		return "deathplateau";
	}

	public boolean canDoQuest(final Player player) {
		return false;
	}

	public void getReward(final Player player) {
		for (int[] rewards : reward) {
			player.getInventory().addItemOrDrop(new Item(rewards[0], rewards[1]));
		}
		for (final int[] expRewards : expReward) {
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer b) {
					b.stop();
				}

				@Override
				public void stop() {
					player.getSkill().addExp(expRewards[0], (expRewards[1]));
				}
			}, 4);
		}
		player.addQuestPoints(questPointReward);
		player.getActionSender().QPEdit(player.getQuestPoints());
	}

	public void completeQuest(Player player) {
		getReward(player);
		player.getActionSender().sendInterface(12140);
		player.getActionSender().sendItemOnInterface(12145, 250, STEEL_CLAWS); //zoom, then itemId
		player.getActionSender().sendString("You have completed " + getQuestName() + "!", 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("6750 Attack XP", 12151);
		player.getActionSender().sendString("Steel claws", 12152);
		player.getActionSender().sendString("Ability to make claws", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), questIndex);
	}

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		int lastIndex = 0;
		switch(questStage) { 
			case QUEST_STARTED:
				lastIndex = 5;
				break;
			case TALKED_TO_EOHRIC:
			case BOOZE_UP_HAROLD:
				lastIndex = 8;
				break;
			case IOU_GET:
				lastIndex = 12;
				break;
			case COMBINATION_GET:
				lastIndex = 14;
				break;
			case DOOR_UNLOCKED:
				lastIndex = 16;
				break;
			case FIND_SHERPA:
				lastIndex = 19;
				break;
			case QUEST_COMPLETE:
				lastIndex = 26;
				break;
		}
		lastIndex++;
		
		ActionSender a = player.getActionSender();
		a.sendQuestLogString("Talk to Denulth in Burthorpe to begin.", 1, this.getQuestID(), 0);
		a.sendQuestLogString("Denulth told me that his army needs assistance in finding", 3, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("a way into Death Plateau without being attacked by the", 4, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("resident trolls.", 5, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("I talked to Eohric, he told me that Harold is staying", 7, this.getQuestID(), TALKED_TO_EOHRIC);
		a.sendQuestLogString("at the local pub, the Toad and Chicken.", 8, this.getQuestID(), TALKED_TO_EOHRIC);
		a.sendQuestLogString("Well, Harold told me he lost the combination, and then", 10, this.getQuestID(), IOU_GET);
		a.sendQuestLogString("we gambled and he ended up far too drunk. He eventually lost", 11, this.getQuestID(), IOU_GET);
		a.sendQuestLogString("enough money to me that he wrote me an IOU note. Hmm...", 12, this.getQuestID(), IOU_GET);
		a.sendQuestLogString("The IOU note was really the combination to the equipment!", 14, this.getQuestID(), COMBINATION_GET);
		a.sendQuestLogString("I managed to unlock the equipment storage room.", 16, this.getQuestID(), DOOR_UNLOCKED);
		a.sendQuestLogString("I found a hermit named Saba, he talked of an old Sherpa", 18, this.getQuestID(), FIND_SHERPA);
		a.sendQuestLogString("that used to take humans on travels around Death Plateau.", 19, this.getQuestID(), FIND_SHERPA);
		switch (questStage) {
			default:
				break;
			case 0:
				a.sendQuestLogString("Talk to @dre@Denulth @bla@in @dre@Burthorpe @bla@to begin.", 1);
				break;
			case QUEST_STARTED:
				a.sendQuestLogString("He asked me first to unlock the equipment storage", lastIndex + 1);
				a.sendQuestLogString("room. The guard on duty last night has the solution", lastIndex + 2);
				a.sendQuestLogString("to the puzzle that is the lock. I should find out", lastIndex + 3);
				a.sendQuestLogString("where he is living at the moment.", lastIndex + 4);
				break;
			case TALKED_TO_EOHRIC:
				a.sendQuestLogString("I should go and speak with Harold.", lastIndex + 1);
				break;
			case BOOZE_UP_HAROLD:
				a.sendQuestLogString("Eohric said that Harold is a drinker. I should buy", lastIndex + 1);
				a.sendQuestLogString("him some drinks and get the combination for the", lastIndex + 2);
				a.sendQuestLogString("equipment room.", lastIndex + 3);
				break;
			case COMBINATION_GET:
				a.sendQuestLogString("I should open up the lock.", lastIndex + 1);
				break;
			case DOOR_UNLOCKED:
				a.sendQuestLogString("Denulth still needs me to find a seperate, new path", lastIndex + 1);
				a.sendQuestLogString("to Death Plateau.", lastIndex + 2);
				break;
			case FIND_SHERPA:
				a.sendQuestLogString("Saba said the Sherpa should still live around here.", lastIndex + 1);
				a.sendQuestLogString("I'm sure if I look hard enough I can find him.", lastIndex + 2);
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
	
	public static void readCombination(final Player player) {
		ActionSender a = player.getActionSender();
		player.setStatedInterface("combinationBalls");
		a.sendInterface(1136);
		a.sendString("@bla@Red is North of Blue, Yellow is South of Purple", 1142);
		a.sendString("@bla@Green is North of Purple, Blue is West of", 1143);
		a.sendString("@bla@Yellow. Purple is East of Red.", 1144);
	}
	
	public enum BallData {
		YELLOW(3111, new Position(2895, 3562, 0)),
		RED(3109, new Position(2894, 3563, 0)),
		BLUE(3110, new Position(2894, 3562, 0)),
		GREEN(3113, new Position(2895, 3564, 0)),
		PURPLE(3112, new Position(2895, 3563, 0));

		private int itemId;
		private Position correct;

		BallData(int itemId, Position correct) {
			this.itemId = itemId;
			this.correct = correct;
		}

		public static BallData forItemId(int itemId) {
			for (BallData b : BallData.values()) {
				if (b.itemId == itemId) {
					return b;
				}
			}
			return null;
		}

		public Position getCorrectPos() {
			return this.correct;
		}

	}
	
	public static boolean correctBalls(final Player player) {
		boolean toReturn = false;
		for(Boolean b : player.getQuestVars().ballBools) {
			toReturn = b;
			if(!b)
				break;
		}
		return toReturn;
	}

	public boolean itemHandling(final Player player, int itemId) {
		switch (itemId) {
			case COMBINATION:
				readCombination(player);
				return true;
			case IOU:
				Dialogues.startDialogue(player, IOU + 20000);
				return true;
		}
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		return false;
	}

	public boolean doItemOnObject(final Player player, int object, int item) {
		int x = player.getClickX(), y = player.getClickY();
		switch(object) {
			case 3676:
			case 3677:
				BallData b = BallData.forItemId(item);
				if (b != null) {
					player.getUpdateFlags().sendAnimation(832);
					player.getInventory().removeItem(new Item(item));
					GroundItemManager.getManager().dropItem(new GroundItem(new Item(item), player, player, new Position(x, y, 0)));
					Position p = b.getCorrectPos();
					if (x == p.getX() && y == p.getY() && player.getQuestStage(this.getQuestID()) == COMBINATION_GET) {
						player.getQuestVars().ballBools[b.ordinal()] = true;
					}
					if(correctBalls(player)) {
						player.setQuestStage(this.getQuestID(), DOOR_UNLOCKED);
						player.getActionSender().sendMessage("The equipment room door has unlocked.");
					}
					return true;
				}
				return false;
		}
		return false;
	}

	public boolean doItemOnNpc(Player player, int itemId, Npc npc) {
		return false;
	}

	public boolean doNpcClicking(Player player, Npc npc) {
		return false;
	}

	public boolean doObjectClicking(final Player player, int object, int x, int y) {
		final int pX = player.getPosition().getX(), pY = player.getPosition().getY();
		switch (object) {
			case 3725: //tenzing's gate
			case 3726:
				player.getActionSender().walkThroughGateEW(3725, 3726, 2824, 3555, 2824, 3554, 0, false);
				player.getActionSender().walkTo(player.getPosition().getX() > 2824 ? -1 : 1, 0, true);
				return true;
			case CAVE_ENTRANCE:
				player.fadeTeleport(new Position(2893, 10074, 0));
				return true;
			case CAVE_EXIT:
				player.fadeTeleport(new Position(2858, 3577, 0));
				return true;
			case EQUIPMENT_DOOR:
				if(player.getPosition().getY() > 3566) {
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					player.getActionSender().walkTo(pX == x ? 0 : pX < x ? 1 : -1, -1, true);
				} else {
					if (player.getQuestStage(this.getQuestID()) >= DOOR_UNLOCKED) {
						player.getActionSender().walkThroughDoor(object, x, y, 0);
						player.getActionSender().walkTo(pX == x ? 0 : pX < x ? 1 : -1, 1, true);
					} else {
						player.getActionSender().sendMessage("This door is locked.");
					}
				}
				return true;
			case HAROLDS_DOOR:
				if (x == 2906 && y == 3543) {
					if (player.getPosition().getY() > 3542) {
						if (player.getQuestStage(this.getQuestID()) > QUEST_STARTED) {
							Dialogues.startDialogue(player, HAROLDS_DOOR + 10000);
						} else {
							player.getActionSender().sendMessage("This door is locked.");
						}
					} else {
						player.getActionSender().walkThroughDoor(object, x, y, 1);
						player.getActionSender().walkTo(pX == x ? 0 : pX < x ? 1 : -1, 1, true);
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

	public boolean sendDialogue(final Player player, final int id, int chatId, int optionId, int npcChatId) {
		DialogueManager d = player.getDialogue();
		switch (id) { //Npc ID
			case SABA:
				switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
					case FIND_SHERPA:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Where does this Sherpa live?");
								return true;
							case 2:
								d.sendNpcChat("I don't know but it can't be far as he used to be", "around all the time!", ANGRY_1);
								d.endDialogue();
								return true;
						}
					return false;
					case DOOR_UNLOCKED:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello!");
								return true;
							case 2:
								d.sendNpcChat("What?!", ANGRY_1);
								return true;
							case 3:
								d.sendOption("Do you know of another way up Death Plateau?", "Nothing, sorry!");
								return true;
							case 4:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if(optionId == 2)
									d.endDialogue();
								return true;
							case 5:
								d.sendNpcChat("Why would I want to go up there? I just want to be", "left in peace!", ANGRY_1);
								return true;
							case 6:
								d.sendNpcChat("It used to be just humans trampling past my cave and", "making a racket. Now there's those blasted trolls too!", "Not only do they stink and argue with each other", "loudly but they are always fighting the humans.", ANGRY_1);
								return true;
							case 7:
								d.sendNpcChat("I just want to be left in peace!", ANGRY_1);
								return true;
							case 8:
								d.sendPlayerChat("Ah... I might be able to help you.");
								return true;
							case 9:
								d.sendNpcChat("How?!", ANGRY_1);
								return true;
							case 10:
								d.sendPlayerChat("I'm trying to help the... er... humans to reclaim back", "Death Plateau. If you help me then at least you'd be", "rid of the trolls.");
								return true;
							case 11:
								d.sendNpcChat("Hmph.", Dialogues.ANGRY_2);
								return true;
							case 12:
								d.sendNpcChat("Let me see...");
								return true;
							case 13:
								d.sendNpcChat("I've only been up Death Plateau once to complain", "about the noise but those pesky trolls started throwing", "rocks at me!");
								return true;
							case 14:
								d.sendNpcChat("Before the trolls came there used to be a nettlesome", "Sherpa that took humans exploring or something equally", "stupid. Perhaps he'd know another way.");
								return true;
							case 15:
								d.sendPlayerChat("Where does this Sherpa live?");
								return true;
							case 16:
								d.sendNpcChat("I don't know but it can't be far as he used to be", "around all the time!", ANGRY_1);
								d.endDialogue();
								player.setQuestStage(this.getQuestID(), FIND_SHERPA);
								return true;
							case 17:
								
						}
					return false;
				}
			return false;
			case ARCHER:
				switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
					case DOOR_UNLOCKED:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hi!");
								return true;
							case 2:
								d.sendNpcChat("Hi!");
								return true;
							case 3:
								d.sendNpcChat("Thank the gods you opened that door! I've been", "trapped here since last night!");
								return true;
							case 4:
								d.sendPlayerChat("No problem!");
								return true;
						}
					return false;
				}
			return false;
			case IOU + 20000:
				switch (d.getChatId()) {
					case 1:
						d.sendPlayerChat("The IOU says that Harold owes me some money.");
						return true;
					case 2:
						d.sendPlayerChat("Wait just a minute!");
						return true;
					case 3:
						d.sendPlayerChat("The IOU is written on the back of the combination!", "The stupid guard had it in his back pocket all the time!", LAUGHING);
						return true;
					case 4:
						d.sendGiveItemNpc("You have found the combination!", new Item(COMBINATION));
						player.getInventory().replaceItemWithItem(new Item(IOU), new Item(COMBINATION));
						if(player.getQuestStage(this.getQuestID()) == IOU_GET) {
							player.setQuestStage(this.getQuestID(), COMBINATION_GET);
						}
						return true;
				}
			return false;
			case HAROLDS_DOOR + 10000:
				d.setLastNpcTalk(HAROLD);
				switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
					default:
						Doors.passThroughDialogueDoor(player, HAROLDS_DOOR, 2906, 3543, -1, new Position(2906, 3543, 1), true);
						return true;
					case TALKED_TO_EOHRIC:
					case BOOZE_UP_HAROLD:
						switch (d.getChatId()) {
							case 1:
								d.sendStatement("You knock on the door.");
								return true;
							case 2:
								d.sendNpcChat("Come in!");
								return true;
							case 3:
								player.getActionSender().removeInterfaces();
								d.endDialogue();
								Doors.passThroughDialogueDoor(player, HAROLDS_DOOR, 2906, 3543, -1, new Position(2906, 3543, 1), true);
								return true;
						}
					return false;
				}
			case 1065: //Attackable soldier
				d.sendNpcChat(BurthorpeCampHandler.LATIN_PHRASES[Misc.randomMinusOne(BurthorpeCampHandler.LATIN_PHRASES.length)]);
				d.endDialogue();
				return true;
			case HAROLD:
				switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
					case IOU_GET:
					case COMBINATION_GET:
						switch (d.getChatId()) {
							case 1:
								if(player.getQuestVars().givenHaroldSpecial) {
									d.sendNpcChat("Leafsh me alone, needsh to shleep...", DISTRESSED);
								} else {
									d.sendNpcChat("Ow... my head...", SAD);
								}
								if (player.getInventory().ownsItem(IOU) || player.getInventory().ownsItem(COMBINATION)) {
									d.endDialogue();
								}
								return true;
							case 2:
								d.sendGiveItemNpc("Harold has given you an IOU scribbled on some paper.", new Item(IOU));
								d.endDialogue();
								player.getInventory().addItemOrDrop(new Item(IOU));
								return true;	
						}
					return false;
					case BOOZE_UP_HAROLD:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello there.");
								return true;
							case 2:
								if (player.getQuestVars().givenHaroldSpecial) {
									d.sendNpcChat("'Ello matey!");
									d.setNextChatId(35);
								} else {
									d.sendNpcChat("What?");
									if (player.getQuestVars().givenHaroldAle) {
										d.setNextChatId(8);
									}
								}
								return true;
							case 3:
								
								d.sendPlayerChat("Can I buy you a drink?");
								return true;
							case 4:
								d.sendNpcChat("Now you're talking! An Asgarnian Ale please!", HAPPY);
								return true;
							case 5:
								if(player.getInventory().playerHasItem(1905)) {
									d.sendGiveItemNpc("You give Harold an Asgarnian Ale.", new Item(1905));
									player.getInventory().removeItem(new Item(1905));
								} else {
									d.sendPlayerChat("Well then, I'll be right back with", "your drink!");
									d.endDialogue();
								}
								return true;
							case 6:
								d.sendStatement("Harold finishes the beer quickly.");
								return true;
							case 7:
								d.sendNpcChat("Arrh. That hit the spot!", HAPPY);
								player.getQuestVars().givenHaroldAle = true;
								return true;
							case 8:
								d.sendOption("Where were you when you last had the combination?", "Would you like to gamble?", "Can I buy you a drink?");
								return true;
							case 9:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								switch(optionId) {
									case 2:
										d.setNextChatId(15);
										break;
									case 3:
										if(player.getQuestVars().spokenToHarold && player.getQuestVars().givenHaroldAle) {
											d.setNextChatId(25);
										} else {
											d.setNextChatId(4);
										}
										break;
								}
								return true;
							case 10:
								d.sendNpcChat("Oh, to the hell with it... I er, lost the", "combination.", SAD);
								return true;
							case 11:
								d.sendPlayerChat("How do you lose something like that?!", ANGRY_1);
								return true;
							case 12:
								d.sendNpcChat("I don't know... I've searched all over for it...", SAD);
								d.setNextChatId(8);
								player.getQuestVars().spokenToHarold = true;
								return true;
							case 15:
								d.sendNpcChat("Good. Good. I have some dice. How much do you want", "to wager?");
								return true;
							case 16:
								player.getActionSender().removeInterfaces();
								player.getActionSender().openXInterface(347400);
								return true;
							case 17:
								if (player.getDice().getBet() > 25000) {
									d.sendNpcChat("Woah, that's too much coin for me to bet.", "Sorry.", SAD);
									d.endDialogue();
									player.getDice().setBet(0);
								} else if(player.getDice().getBet() <= 0) {
									d.sendNpcChat("You can't bet no money, silly.");
									d.endDialogue();
									player.getDice().setBet(0);
								} else {
									if (player.getInventory().playerHasItem(995, player.getDice().getBet())) {
										if(player.getQuestVars().givenHaroldSpecial) {
											d.sendNpcChat("Right...er...here goes...");
											d.setNextChatId(42);
										} else {
											d.sendNpcChat("Ok. I'll roll first!");
										}
									} else {
										d.sendNpcChat("Er, it looks like you don't have that", "much coin.");
										d.endDialogue();
										player.getDice().setBet(0);
									}
								}
								return true;
							case 18:
								d.sendNpcChat("Don't forget that once I start my roll you can't back", "out of the bet! If you do you lose your stake!");
								return true;
							case 19:
								d.dontCloseInterface();
								player.setStatedInterface("gamblingDice");
								player.getDice().startGame();
								return true;
							case 20:
								if(player.getDice().justWon) {
									d.sendGiveItemNpc("Harold gives you your winnings.", new Item(617));
									d.endDialogue();
								} else {
									d.sendGiveItemNpc("You give Harold his winnings.", new Item(617));
									d.endDialogue();
								}
								return true;
							case 25:
								d.sendNpcChat("You know, I could really go for a Blurberry Special.", "You wouldn't happen to have one would you?");
								return true;
							case 26:
								if(player.getInventory().playerHasItem(2064)) {
									d.sendGiveItemNpc("You give Harold a Blurberry Special.", new Item(2064));
									player.getInventory().removeItem(new Item(2064));
									player.getQuestVars().givenHaroldSpecial = true;
								} else {
									d.sendPlayerChat("Er, nope. I don't. Sorry.");
									d.endDialogue();
								}
								return true;
							case 27:
								d.sendStatement("Harold guzzles down the special.");
								return true;
							case 28:
								d.sendNpcChat("Wow!");
								return true;
							case 29:
								d.sendNpcChat("Now THAT hit the spot!");
								d.endDialogue();
								return true;
							case 35:
								d.sendOption("Where were you when you last had the combination?", "Would you like to gamble?", "Can I buy you a drink?");
								return true;
							case 36:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								switch(optionId) {
									case 2:
										d.setNextChatId(38);
										break;
									case 3:
										d.setNextChatId(50);
										break;
								}
								return true;
							case 37:
								d.sendNpcChat("Isssh dun wanna talk n'bout it...");
								d.endDialogue();
								return true;
							case 38:
								d.sendNpcChat("Shure!");
								return true;
							case 39:
								d.sendNpcChat("Place your betsh pleashe!");
								return true;
							case 40:
								d.sendNpcChat("*giggle*");
								d.setNextChatId(16);
								return true;
							case 42:
								d.dontCloseInterface();
								player.setStatedInterface("gamblingDice2");
								player.getDice().startGame();
								return true;
							case 43:
								d.sendNpcChat("I didn't know you could ushe four dice...", "Oh well...");
								return true;
							case 44:
								d.sendStatement("Harold is so drunk he can hardly see, let alone count!");
								if(player.getQuestVars().moneyWonFromHarold < 400) {
									d.endDialogue();
								}
								return true;
							case 45:
								d.sendNpcChat("Um... not enough money...");
								return true;
							case 46:
								d.sendNpcChat("I owe you the resht of the money!");
								return true;
							case 47:
								d.sendGiveItemNpc("Harold has given you an IOU scribbled on some paper.", new Item(IOU));
								d.endDialogue();
								player.getInventory().addItemOrDrop(new Item(IOU));
								player.setQuestStage(this.getQuestID(), IOU_GET);
								return true;
							case 50:
								d.sendNpcChat("Weeee, me thinks I've had *hic* quite", "enuff for she nite, thankye laddy!");
								d.endDialogue();
								return true;
								
						}
					return false;
					case TALKED_TO_EOHRIC:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello there.");
								return true;
							case 2:
								d.sendNpcChat("Hi.");
								return true;
							case 3:
								d.sendOption("You're the guard that was on duty last night?", "Can I buy you a drink?");
								return true;
							case 4:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if(optionId == 2)
									d.setNextChatId(10);
								return true;
							case 5:
								d.sendNpcChat("Yeah.");
								return true;
							case 6:
								d.sendPlayerChat("Denulth said that you lost the combination to the", "equipment room?");
								return true;
							case 7:
								d.sendNpcChat("I don't want to talk about it!", ANGRY_1);
								d.endDialogue();
								player.getQuestVars().spokenToHarold = true;
								return true;
							case 10:
								d.sendNpcChat("Now you're talking! An Asgarnian Ale please!", HAPPY);
								return true;
							case 11:
								if(player.getInventory().playerHasItem(1905)) {
									d.sendGiveItemNpc("You give Harold an Asgarnian Ale.", new Item(1905));
									player.getInventory().removeItem(new Item(1905));
								} else {
									d.sendPlayerChat("Well then, I'll be right back with", "your drink!");
									d.endDialogue();
								}
								return true;
							case 12:
								d.sendStatement("Harold finishes the beer quickly.");
								return true;
							case 13:
								d.sendNpcChat("*burp*");
								d.endDialogue();
								return true;		
						}
					return false;
				}
			return false;
			case EOHRIC:
				switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
					default:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hi!");
								return true;
							case 2:
								d.sendNpcChat("Hi, can I help?");
								return true;
							case 3:
								d.sendPlayerChat("No, I'm just looking around.");
								d.endDialogue();
								return true;
								
						}
					return false;
					case QUEST_STARTED:
					case TALKED_TO_EOHRIC:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hi!");
								return true;
							case 2:
								d.sendNpcChat("Hi, can I help?");
								return true;
							case 3:
								if(player.getQuestVars().spokenToHarold) {
									d.sendPlayerChat("I found Harold but he won't talk to me!");
									d.setNextChatId(12);
								} else {
									d.sendOption("I'm looking for the guard that was on last night.", "Do you know of another way up Death Plateau?", "No, I'm just looking around.");
								}
								return true;
							case 4:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								switch(optionId) {
									case 2:
										d.setNextChatId(10);
										break;
									case 3:
										d.endDialogue();
										break;
								}
								return true;
							case 5:
								d.sendNpcChat("There was only one guard on last night. Harold. He's a", "nice lad, if a little dim.");
								return true;
							case 6:
								d.sendPlayerChat("Do you know where he is staying?");
								return true;
							case 7:
								d.sendNpcChat("Harold is staying at the Toad and Chicken.");
								return true;
							case 8:
								d.sendPlayerChat("Thanks!");
								d.endDialogue();
								player.setQuestStage(this.getQuestID(), TALKED_TO_EOHRIC);
								return true;
							case 10:
								d.sendNpcChat("No, sorry. I wouldn't want to go north-east from here,", "it's very rocky and barren.");
								d.endDialogue();
								return true;
							case 12:
								d.sendNpcChat("Hmmm. Harold has got in trouble a few times over", "his drinking and gambling. Perhaps he'd open up after", "a drink?");
								return true;
							case 13:
								d.sendPlayerChat("Thanks, I'll try that!");
								d.endDialogue();
								player.getQuestVars().spokenToHarold = false;
								player.setQuestStage(this.getQuestID(), BOOZE_UP_HAROLD);
								return true;
						}
					return false;
				}
			case DENULTH:
				switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
					case DOOR_UNLOCKED:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Did you get into the equipment room yet?");
								return true;
							case 2:
								d.sendPlayerChat("I did actually.", HAPPY);
								return true;
							case 3:
								d.sendNpcChat("Most excellent! Thank you adventurer!", HAPPY);
								return true;
							case 4:
								d.sendNpcChat("The other issue still exists however, we need", "to find an alternate route to reach Death Plateau.", "Return to me when you've found one.");
								d.endDialogue();
								return true;
						}
					return false;
					case QUEST_STARTED:
					case TALKED_TO_EOHRIC:
					case BOOZE_UP_HAROLD:
					case IOU_GET:
					case COMBINATION_GET:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Did you get into the equipment room yet?");
								return true;
							case 2:
								d.sendPlayerChat("Not yet I'm afraid...", SAD);
								d.endDialogue();
								return true;
						}
					return false;
					case 0:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Hello citizen, how can I help?");
								return true;
							case 2:
								d.sendOption("Do you have any quests for me?", "What is this place?", "You can't, thanks.");
								return true;
							case 3:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								switch(optionId) {
									case 2:
										d.setNextChatId(25);
										break;
									case 3:
										d.endDialogue();
										break;
								}
								return true;
							case 4:
								d.sendNpcChat("I don't know if you can help us!", SAD);
								return true;
							case 5:
								d.sendNpcChat("The trolls have taken up camp on Death Plateau! They", "are using it to launch raids at night on the village. We", "have tried to attack the camp but the main path is", "heavily guarded!", DISTRESSED);
								return true;
							case 6:
								d.sendPlayerChat("Perhaps there is a way you can sneak up at night?");
								return true;
							case 7:
								d.sendNpcChat("If there is another way I do not know of it.", SAD);
								return true;
							case 8:
								d.sendNpcChat("Do you know of such a path?");
								return true;
							case 9:
								d.sendOption("No, but perhaps I could try and find one?", "No, sorry.");
								return true;
							case 10:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if(optionId == 2)
									d.endDialogue();
								return true;
							case 11:
								d.sendNpcChat("Citizen you would be well rewarded!", HAPPY);
								return true;
							case 12:
								d.sendNpcChat("If you go up to Death Plateau, be very careful as the", "trolls will attack you on sight!");
								return true;
							case 13:
								d.sendPlayerChat("I'll be careful.");
								return true;
							case 14:
								d.sendNpcChat("One other thing.");
								return true;
							case 15:
								d.sendPlayerChat("What's that?");
								return true;
							case 16:
								d.sendNpcChat("All of our equipment is kept in the castle on the hill.");
								return true;
							case 17:
								d.sendNpcChat("The stupid guard that was on duty last night lost the", "combination to the lock! I told the Prince that the", "Imperial Guard should've been in charge of security!", ANGRY_1);
								return true;
							case 18:
								d.sendPlayerChat("No problem, what does the combination look like?");
								return true;
							case 19:
								d.sendNpcChat("The equipment room is unlocked when the stone balls", "are placed in the correct order on the stone mechanism", "outside it. The right order is written on a piece of", "paper the guard had.");
								return true;
							case 20:
								d.sendPlayerChat("A stone what...?!", DISTRESSED);
								return true;
							case 21:
								d.sendNpcChat("Well citizen, the Prince is fond of puzzles. Why we", "couldn't just have a key is beyond me!");
								return true;
							case 22:
								d.sendPlayerChat("I'll get on it right away!");
								d.endDialogue();
								QuestHandler.startQuest(player, this.getQuestID());
								return true;
							case 25:
								
								
								
						}
					return false;
					case QUEST_COMPLETE:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Thank you again!", Dialogues.HAPPY);
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
