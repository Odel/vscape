package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.dialogue.DialogueManager;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.NEAR_TEARS;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
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

public class PlagueCity implements Quest {

	public static final int questIndex = 7372; //Used in player's quest log interface, id is in Player.java, Change
	//Quest stages
	public static final int QUEST_STARTED = 1;
	public static final int MASK_GET = 2;
	public static final int SOFTEN_SOIL = 3;
	public static final int DIG = 4;
	public static final int GRILL_PULLED = 5;
	public static final int QUEST_COMPLETE = 15;

	//Items
	public static final int DWELLBERRIES = 2126;
	public static final int SPADE = 952;
	public static final int ROPE = 954;
	public static final int WARRANT = 1503;
	public static final int HANGOVER_CURE = 1504;
	public static final int MAGIC_SCROLL = 1505;
	public static final int GAS_MASK = 1506;
	public static final int SMALL_KEY = 1507;
	public static final int SCRUFFY_NOTE = 1508;
	public static final int BOOK = 1509;
	public static final int ELENAS_PICTURE = 1510;
	public static final int BUCKET_OF_MILK = 1927;
	public static final int BUCKET_OF_WATER = 1929;
	public static final int CHOCOLATE_DUST = 1975;
	public static final int SNAPE_GRASS = 231;

	//Positions
	public static final Position INTO_SEWERS = new Position(2518, 9760, 0);
	public static final Position OUT_OF_SEWERS = new Position(2566, 3331, 0);
	public static final Position OUT_OF_PIPE = new Position(2529, 3304, 0);

	//Interfaces
	public static final int INTERFACE = -1;

	//Npcs
	public static final int ALRENA = 710;
	public static final int BRAVEK = 711;
	public static final int CARLA = 712;
	public static final int CLERK = 713;
	public static final int EDMOND = 714;
	//public static final int NULL = 715;
	public static final int HEAD_MOURNER = 716;
	public static final int MOURNER_1 = 717;
	public static final int MOURNER_2 = 718;
	public static final int MOURNER_3 = 719;
	public static final int RECRUITER = 720;
	public static final int TED_REHNISON = 721;
	public static final int MARTHA_REHNISON = 722;
	public static final int BILLY_REHNISON = 723;
	public static final int MILLI_REHNISON = 724;
	public static final int JETHICK = 725;

	//Objects
	public static final int MUD_PATCH = 2531;
	public static final int MUD_PILE = 2533;
	public static final int PIPE = 2542;
	public static final int PIPE_GRILL = 11422;

	public int dialogueStage = 0;

	private int reward[][] = { //{itemId, count},
	};

	private int expReward[][] = { //{skillId, exp},
		{Skill.MINING, 2425}
	};

	private static final int questPointReward = 1; //Change

	public int getQuestID() { //Change
		return 39;
	}

	public String getQuestName() { //Change
		return "Plague City";
	}

	public String getQuestSaveName() { //Change
		return "plaguecity";
	}

	public boolean canDoQuest(final Player player) {
		return false;
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

	public void completeQuest(Player player) {
		getReward(player);
		player.getActionSender().sendInterface(12140);
		player.getActionSender().sendItemOnInterface(12145, 250, GAS_MASK); //zoom, then itemId
		player.getActionSender().sendString("You have completed " + getQuestName() + "!", 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("Ardougne teleport spell", 12151);
		player.getActionSender().sendString("5456.25 Mining XP", 12152);
		player.getActionSender().sendString("", 12153);
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
			/**The last index of the text sent for that quest stage which persists
			through the entire quest, striked or not (i.e, shit sent below)
			Remember if there is no new persistent entry for that stage, stack the case with
			the last case that started the newest persistent entry, to keep lastIndex correct **/
			case QUEST_STARTED:
				lastIndex = 4;
				break;
			case MASK_GET:
			case SOFTEN_SOIL:
				lastIndex = 7;
				break;
			case DIG:
				lastIndex = 10;
				break;
			case GRILL_PULLED:
				lastIndex = 14;
				break;
			case QUEST_COMPLETE:
				lastIndex = 26;
				break;
		}
		lastIndex++;
		
		//Quest log entries that persist past each stage, line which to send (1 = first, etc)
		//And the quest stage which it first appears, after that stage it'll @str@, strikethrough
		ActionSender a = player.getActionSender();
		a.sendQuestLogString("Talk to Edmond near West Ardougne to begin.", 1, this.getQuestID(), 0);
		a.sendQuestLogString("Edmond told me about the danger his daughter is", 3, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("in, and asked me to find him some dwellberries.", 4, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("I gave Alrena some dwellberries and she gave me", 6, this.getQuestID(), MASK_GET);
		a.sendQuestLogString("a special gas mask to protect me from the plague.", 7, this.getQuestID(), MASK_GET);
		a.sendQuestLogString("I softened the soil in Edmond's garden with water.", 9, this.getQuestID(), DIG);
		a.sendQuestLogString("I can dig into the Ardougne city sewers.", 10, this.getQuestID(), DIG);
		a.sendQuestLogString("With Edmond's help I was able to pull the grill off", 12, this.getQuestID(), GRILL_PULLED);
		a.sendQuestLogString("the pipe leading into West Ardougne. I can use this", 13, this.getQuestID(), GRILL_PULLED);
		a.sendQuestLogString("pipe to go in and out of that part of the city.", 14, this.getQuestID(), GRILL_PULLED);

		switch (questStage) { //Quest stages where you send additional information that is not stored
			//or striked out past that stage.
			default:
				break;
			case 0:
				//Not started quest journal shit, no need to use lastIndex
				a.sendQuestLogString("Talk to @dre@Edmond @bla@near @dre@West Ardougne @bla@to begin.", 1);
				break;
			case QUEST_STARTED:
				//Last index + index of new information (1 = first, etc)
				a.sendQuestLogString("He said the berries are in McGrubor's Wood, north", lastIndex + 1);
				a.sendQuestLogString("of the Ranger's guild.", lastIndex + 2);
				break;
			case MASK_GET:
				a.sendQuestLogString("I should talk to Edmond again, he mentioned he", lastIndex + 1);
				a.sendQuestLogString("knows a way into West Ardougne.", lastIndex + 2);
				break;
			case SOFTEN_SOIL:
				a.sendQuestLogString("Edmond's way into West Ardougne involves the", lastIndex + 1);
				a.sendQuestLogString("sewer system beneath the city. I need to soften", lastIndex + 2);
				a.sendQuestLogString("the soil in his garden with buckets of water before", lastIndex + 3);
				a.sendQuestLogString("I dig into the ground.", lastIndex + 4);
				break;
			case GRILL_PULLED:
				a.sendQuestLogString("Edmond told me to seek out Jethick and give him", lastIndex + 1);
				a.sendQuestLogString("his regards once I enter West Ardougne.", lastIndex + 2);
				break;
			case QUEST_COMPLETE:
				//Same here, first line after the last entry that persists for the whole quest
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
	
	public static void assessGrateStatus(final Player player) {
		new GameObject(Constants.EMPTY_OBJECT, 2514, 9739, 0, 3, 4, 11422, 999999);
		ObjectHandler.getInstance().removeObject(2514, 9739, 0, 4);
		new GameObject(11422, 2514, 9739, 0, 3, 4, 0, 999999);
	}
	
	public static void handleGardenDig(final Player player) {
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				player.teleport(INTO_SEWERS);
				b.stop();
			}

			@Override
			public void stop() {
				player.setStopPacket(true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						b.stop();
					}

					@Override
					public void stop() {
						player.setStopPacket(false);
						player.getDialogue().sendStatement("You fall through...", "...you land in the sewer.", "Edmond follows you down the hole.");
						assessGrateStatus(player);
					}
				}, 5);
			}
		}, 1);
	}
	
	public static void startGrillPullSequence(final Player player) {
		final DialogueManager d = player.getDialogue();
		player.setStopPacket(true);
		player.setInCutscene(true);
		player.setQuestStage(39, GRILL_PULLED);
		player.getActionSender().sendWalkableInterface(8677);
		player.getActionSender().sendMapState(2);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int count = 0;

			@Override
			public void execute(CycleEventContainer b) {
				count++;
				switch(count) {
					case 1:
						d.sendPlayerChat("1... 2... 3... Pull!", CONTENT);
						break;
					case 2:
						d.setLastNpcTalk(EDMOND);
						d.sendNpcChat("I don't know if my back is suited for this...", "Urghhh...", Dialogues.DISTRESSED);
						break;
					case 3:
						d.sendStatement("You hear a faint popping noise from Edmond's spine.");
						break;
					case 4:
						d.sendPlayerChat("Come one Edmond, we're almost there!", Dialogues.ANGRY_1);
						break;
					case 5:
						d.sendStatement("The bolts on the grate begin to come loose.");
						break;
					case 6:
						d.setLastNpcTalk(EDMOND);
						d.sendNpcChat("I can't go on boy, I've got nothing left...", SAD);
						break;
					case 7:
						d.sendPlayerChat("Damnit! Don't quit on me now old man!", Dialogues.ANGRY_2);
						break;
					case 8:
						d.setLastNpcTalk(EDMOND);
						d.sendNpcChat("You're right, this is my daughter we're talking", "about! Here we go...", "Arghhhh!", Dialogues.ANGRY_1);
						break;
					case 9:
						d.sendStatement("The grill pops off the face of the pipe and onto the floor.");
						break;
					case 10:
						d.sendPlayerChat("I knew you had it in your Edmond. Excellent", "work. Although... I would go home and rest up if I were", "you, those popping noises didn't sound healthy.", CONTENT);
						break;
					case 11:
						d.sendStatement("It takes Edmond a second to catch his breath.");
						break;
					case 12:
						d.setLastNpcTalk(EDMOND);
						d.sendNpcChat("Once you're in the city look for a man called", "Jethick, he's an old friend and should help you.", "Send him my regards, I haven't seen him since", "before Elena was born.", CONTENT);
						break;
					case 13:
						d.sendPlayerChat("Alright, thanks I will.", CONTENT);
						d.endDialogue();
						break;
				}
				if(count >= 14) {
					b.stop();
				}
			}

			@Override
			public void stop() {
				player.setStopPacket(false);
				player.setInCutscene(false);
				player.setQuestStage(39, GRILL_PULLED);
				PlagueCity.assessGrateStatus(player);
				player.getActionSender().sendWalkableInterface(-1);
				player.getActionSender().sendMapState(0);
			}
		}, 7);
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
		switch(object) {
			case PIPE_GRILL:
				if (item == ROPE) {
					if(player.getEquipment().getId(Constants.HAT) != GAS_MASK) {
						player.getDialogue().sendPlayerChat("Hmm, I should probably be wearing my gas mask before", "I attempt this.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					}
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						int count = 0;

						@Override
						public void execute(CycleEventContainer b) {
							count++;
							player.getUpdateFlags().sendFaceToDirection(new Position(2514, 9738, 0));
							player.getUpdateFlags().setFaceToDirection(true);
							player.getUpdateFlags().setUpdateRequired(true);
							if (player.getPosition().getX() == 2514 && player.getPosition().getY() == 9739) {
								player.getUpdateFlags().sendAnimation(3191, 10);
								b.stop();
							}
							if (count >= 5) {
								b.stop();
							}
						}

						@Override
						public void stop() {
							player.getUpdateFlags().sendFaceToDirection(new Position(2514, 9738, 0));
							player.getUpdateFlags().setFaceToDirection(true);
							player.getUpdateFlags().setUpdateRequired(true);
							player.getDialogue().sendGiveItemNpc("You tie the end of the rope to the sewer pipe's grill.", new Item(ROPE));
							player.getInventory().removeItem(new Item(ROPE));
							startGrillPullSequence(player);
						}
					}, 1);
					return true;
				}
			case MUD_PATCH:
				if(item == BUCKET_OF_WATER) {
					if(player.getQuestStage(this.getQuestID()) == SOFTEN_SOIL) {
						player.setStopPacket(true);
						CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
							@Override
							public void execute(CycleEventContainer b) {
								player.getUpdateFlags().sendAnimation(TheGrandTree.PLACE_ANIM);
								player.getInventory().replaceItemWithItem(new Item(BUCKET_OF_WATER), new Item(1925));
								if (player.getQuestVars().plagueCitySoilSoftened++ == 3) {
									player.getDialogue().sendStatement("You pour water onto the soil.", "The soil is now soft enough to dig into.");
									player.setQuestStage(39, DIG);
								} else {
									player.getDialogue().sendStatement("You pour water onto the soil.", "The soil softens slightly.");
								}
								b.stop();
							}

							@Override
							public void stop() {
								player.setStopPacket(false);
							}
						}, 2);
						
					} else if (player.getQuestStage(this.getQuestID()) <= MASK_GET) {
						player.getDialogue().sendStatement("You see no reason to do that at the moment.");
						player.getDialogue().endDialogue();
					} else {
						player.getDialogue().sendStatement("The soil is already soft enough to dig into.");
						player.getDialogue().endDialogue();
					}
					return true;
				}
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
		switch (object) {
			case PIPE:
				if (player.getEquipment().getId(Constants.HAT) != GAS_MASK) {
					player.getDialogue().sendPlayerChat("Hmm, I should probably be wearing my gas mask before", "I attempt this.", CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					int count = 0;
					@Override
					public void execute(CycleEventContainer b) {
						count++;
						player.getUpdateFlags().sendFaceToDirection(new Position(2514, 9738, 0));
						player.getUpdateFlags().setFaceToDirection(true);
						player.getUpdateFlags().setUpdateRequired(true);
						if(player.getPosition().getX() == 2514 && player.getPosition().getY() == 9739) {
							player.getUpdateFlags().sendAnimation(3195, 10);
							b.stop();
						}
						if(count >= 5) {
							b.stop();
						}
					}

					@Override
					public void stop() {
						CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
							@Override
							public void execute(CycleEventContainer b) {
								b.stop();
							}
							@Override
							public void stop() {
								player.teleport(OUT_OF_PIPE);
							}
						}, 2);
					}
				}, 1);
				return true;
			case PIPE_GRILL:
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					int count = 0;
					@Override
					public void execute(CycleEventContainer b) {
						count++;
						player.getUpdateFlags().sendFaceToDirection(new Position(2514, 9738, 0));
						player.getUpdateFlags().setFaceToDirection(true);
						player.getUpdateFlags().setUpdateRequired(true);
						if(player.getPosition().getX() == 2514 && player.getPosition().getY() == 9739) {
							player.getUpdateFlags().sendAnimation(3192, 10);
							b.stop();
						}
						if(count >= 5) {
							b.stop();
						}
					}

					@Override
					public void stop() {
						player.getUpdateFlags().sendFaceToDirection(new Position(2514, 9738, 0));
						player.getUpdateFlags().setFaceToDirection(true);
						player.getUpdateFlags().setUpdateRequired(true);
					}
				}, 1);
				player.getDialogue().sendStatement("The grill is too secure.", "You can't pull it off alone.");
				player.getQuestVars().triedPipeGrill = true;
				return true;
			case MUD_PILE:
				Ladders.climbLadder(player, OUT_OF_SEWERS);
				player.getActionSender().sendMessage("You push yourself through the dirt as you climb...");
				return true;
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
			case ALRENA:
				switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
					case MASK_GET:
						switch (d.getChatId()) {
							case 1:
								if(player.getInventory().ownsItem(GAS_MASK)) {
									d.sendNpcChat("Good luck finding my daughter, and", "thank you adventurer!", CONTENT);
									d.endDialogue();
								} else {
									d.sendPlayerChat("I, er, seem to have misplaced my gas mask.", CONTENT);
								}
								return true;
							case 2:
								d.sendNpcChat("There's a spare mask hiding in the cupboard.", CONTENT);
								d.endDialogue();
								return true;
							case 8:
								d.sendNpcChat("I'll make a spare mask. I'll hide it in the cupboard in", "case the mourners come in.", CONTENT);
								d.endDialogue();
								return true;
						}
					return false;
					case 0:
						d.sendNpcChat("Oh... what ever will I do?!", NEAR_TEARS);
						d.endDialogue();
						return true;
					case QUEST_STARTED:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello, Edmond has asked me to help find your", "daughter.", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Yes, he told me. I've begun making your special gas", "mask, but I need some dwellberries to finish it.", CONTENT);
								return true;
							case 3:
								if(player.getInventory().playerHasItem(DWELLBERRIES)) {
									d.sendPlayerChat("Yes, I've got some here.", CONTENT);
								} else {
									d.sendPlayerChat("I'm afraid I don't have any...", SAD);
									d.endDialogue();
								}
								return true;
							case 4:
								d.sendGiveItemNpc("You give the dwellberries to Alrena.", new Item(DWELLBERRIES));
								return true;
							case 5:
								d.sendStatement("Alrena crushes the berries into a smooth paste. She then smears the", "paste over a strange mask.");
								return true;
							case 6:
								d.sendNpcChat("There we go, all done. While in West Ardougne you", "must wear this at all times, or you could catch the", "plague.", CONTENT);
								return true;
							case 7:
								d.sendGiveItemNpc("Alrena gives you the mask.", new Item(GAS_MASK));
								player.getInventory().replaceItemWithItem(new Item(DWELLBERRIES), new Item(GAS_MASK));
								player.setQuestStage(39, MASK_GET);
								return true;
						}
					return false;
				}
			return false;
			case EDMOND:
				switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
					case DIG:
						switch (d.getChatId()) {
							case 1:
								if(player.getPosition().getY() > 9000) {
									if(player.getQuestVars().triedPipeGrill) {
										d.sendPlayerChat("Edmond, I can't get through to West Ardougne!", "There's an iron grill blocking my way,", "I can't pull it off alone.", CONTENT);
										d.setNextChatId(5);
									} else {
										d.sendNpcChat("I think it's the pipe to the south that comes up in West", "Ardougne.", CONTENT);
									}
								} else {
									d.sendNpcChat("Looks like you softened up that soil nicely. Dig", "into it when you're ready, I'll follow you down.", CONTENT);
									d.endDialogue();
								}
								return true;
							case 2:
								d.sendPlayerChat("Alright, I'll check it out.", CONTENT);
								d.endDialogue();
								return true;
							case 5:
								d.sendNpcChat("If you get some rope you could tie it to the grill,", "then we could both pull it at the same time.", CONTENT);
								d.endDialogue();
								return true;
						}
					return false;
					case SOFTEN_SOIL:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Beneath us are the Ardougne sewers, there", "you'll find access to West Ardougne...", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("The problem is the soil is rock hard. You'll need to pour", "on several buckets of water to soften it up. I'll keep an", "eye out for the mourners.", CONTENT);
								d.endDialogue();
								return true;
						}
						return false;
					case MASK_GET:
						switch (d.getChatId()) {
							case 1:
								if(player.getInventory().ownsItem(GAS_MASK)) {
									d.sendPlayerChat("Hi Edmond, I've got the gas mask now.", CONTENT);
								} else {
									d.sendPlayerChat("I, er, seem to have misplaced my gas mask.", CONTENT);
									d.setNextChatId(10);
								}
								return true;
							case 2:
								d.sendNpcChat("Good stuff, now for the digging. Beneath us are the", "Ardougne sewers, there you'll find", "access to West Ardougne.", CONTENT);
								return true;
							case 3:
								d.sendNpcChat("The problem is the soil is rock hard. You'll need to pour", "on several buckets of water to soften it up. I'll keep an", "eye out for the mourners.", CONTENT);
								d.endDialogue();
								player.setQuestStage(39, SOFTEN_SOIL);
								return true;
							case 10:
								d.sendNpcChat("Go see my wife, I'm sure she has an extra.", CONTENT);
								d.endDialogue();
								return true;
						}
					return false;
					case QUEST_STARTED:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Did you get those dwellberries yet?", CONTENT);
								return true;
							case 2:
								if(player.getInventory().playerHasItem(DWELLBERRIES)) {
									d.sendPlayerChat("Yes, I've got some here.", CONTENT);
								} else {
									d.sendPlayerChat("No... I'm afraid not.", SAD);
									d.endDialogue();
								}
								return true;
							case 3:
								d.sendNpcChat("Take them to my wife Alrena, she's close by", "here somewhere.", CONTENT);
								d.endDialogue();
								return true;
						}
					return false;
					case 0:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello old man.", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Sorry, I can't stop to talk...", SAD);
								return true;
							case 3:
								d.sendPlayerChat("Why, what's wrong?", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("I've got to find my daughter. I pray that she is still", "alive...", CONTENT);
								return true;
							case 5:
								d.sendOption("What's happened to her?", "Well, good luck finding her.");
								return true;
							case 6:
								d.sendPlayerChat(d.tempStrings[optionId - 1], CONTENT);
								if(optionId == 2)
									d.endDialogue();
								return true;
							case 7:
								d.sendNpcChat("Elena's a missionary and a healer. Three weeks ago she", "managed to cross the Ardougne wall... No one's allowed", "to cross the wall in case they spread the plague. But", "after hearing the scream of suffering she felt she had", SAD);
								return true;
							case 8:
								d.sendNpcChat("to help. She said she'd be gone for a few days but we've", "heard nothing since.", SAD);
								return true;
							case 9:
								d.sendOption(new String[]{"Tell me more about the plague.", "Can I help find her?", "I'm sorry, I have to go."});
								return true;
							case 10:
								d.sendPlayerChat(d.tempStrings[optionId - 1], CONTENT);
								switch(optionId) {
									case 2:
										d.setNextChatId(15);
										break;
									case 3:
										d.endDialogue();
										break;
								}
							return true;
							case 11:
								d.sendNpcChat("Oh, I'd rather not talk about it... I'm far", "too worried about my daughter to tell a story that", "long. Perhaps another time?", SAD);
								d.setNextChatId(9);
								return true;
							case 15:
								d.sendNpcChat("Really, would you? I've been working on a plan to get", "into West Ardougne, but I'm too old and tired to carry", "it through. If you're going into West Ardougne you'll", "need protection from the plague. My wife made a", CONTENT);
								return true;
							case 16:
								d.sendNpcChat("special gas mask for Elena with dwellberries rubbed into", "it. Dwellberries help repel the virus! We need some", "more though...", CONTENT);
								return true;
							case 17:
								d.sendPlayerChat("Where can I find these dwellberries?", CONTENT);
								return true;
							case 18:
								d.sendNpcChat("The only place I know of is McGrubor's Wood just", "north of the Rangers' Guild.", CONTENT);
								return true;
							case 19:
								d.sendPlayerChat("Ok, I'll go and get some.", CONTENT);
								return true;
							case 20:
								d.sendNpcChat("The foresters keep a close eye on it, but there is a back", "way in.", CONTENT);
								return true;
							case 21:
								d.sendPlayerChat("Thanks.", CONTENT);
								d.endDialogue();
								QuestHandler.startQuest(player, this.getQuestID());
								return true;
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
