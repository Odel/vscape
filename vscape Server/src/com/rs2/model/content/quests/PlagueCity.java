package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.dialogue.DialogueManager;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.NEAR_TEARS;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcDefinition;
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
	public static final int DELIVER_BOOK = 6;
	public static final int BOOK_RETURNED = 7;
	public static final int FIND_BUILDING = 8;
	public static final int TALK_TO_BRAVEK = 9;
	public static final int MAKE_HANGOVER_CURE = 10;
	public static final int WARRANT_GET = 11;
	public static final int GET_REWARD = 12;
	public static final int QUEST_COMPLETE = 13;

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
	public static final int BUCKET_OF_WATER = 1929;
	public static final int CHOCOLATE_MILK = 1977;
	public static final int SNAPE_GRASS = 231;

	//Positions
	public static final Position INTO_SEWERS = new Position(2518, 9760, 0);
	public static final Position OUT_OF_SEWERS = new Position(2566, 3331, 0);
	public static final Position OUT_OF_PIPE = new Position(2529, 3304, 0);
	public static final Position DOWN_FROM_MANHOLE = new Position(2514, 9739, 0);
	public static final Position UP_FROM_ELENA = new Position(2536, 3271, 0);
	public static final Position DOWN_TO_ELENA = new Position(2537, 9670, 0);
	
	//Interfaces
	public static final int HANGOVER_CURE_INTERFACE = 1136;

	//Npcs
	public static final int ALRENA = 710;
	public static final int BRAVEK = 711;
	public static final int CARLA = 712;
	public static final int CLERK = 713;
	public static final int EDMOND = 714;
	public static final int ELENA = 715;
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
	public static final int MANHOLE = 2544;
	public static final int RESTRICTED_DOOR = 2535;
	public static final int CUPBOARD = 2524;
	public static final int ELENAS_DOOR = 2526;
	public static final int BRAVEKS_DOOR = 2528;
	public static final int REHNISON_DOOR = 2537;
	public static final int SPOOKY_STAIRS_DOWN = 2522;
	public static final int SPOOKY_STAIRS_UP = 2523;
	public static final int BARREL = 2530;
	public static final int NURSE_CUPBOARD = 2062;
	public static final int REHNISON_STAIRS_UP = 2539;
	public static final int REHNISON_STAIRS_DOWN = 2540;
	public static final int PIPE_GRILL = 11422;

	public int dialogueStage = 0;

	private int reward[][] = { //{itemId, count},
		{MAGIC_SCROLL, 1}
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
			case DELIVER_BOOK:
				lastIndex = 18;
				break;
			case BOOK_RETURNED:
				lastIndex = 20;
				break;
			case FIND_BUILDING:
				lastIndex = 23;
				break;
			case TALK_TO_BRAVEK:
			case MAKE_HANGOVER_CURE:
				lastIndex = 25;
				break;
			case WARRANT_GET:
				lastIndex = 28;
				break;
			case GET_REWARD:
			case QUEST_COMPLETE:	
				lastIndex = 31;
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
		a.sendQuestLogString("I spoke with Jethick in West Ardougne, he told me", 16, this.getQuestID(), DELIVER_BOOK);
		a.sendQuestLogString("Elena was staying with a family nearby, and gave me", 17, this.getQuestID(), DELIVER_BOOK);
		a.sendQuestLogString("a book to deliver to said family. They live to the north.", 18, this.getQuestID(), DELIVER_BOOK);
		a.sendQuestLogString("I returned the book to the Rehnison family.", 20, this.getQuestID(), BOOK_RETURNED);
		a.sendQuestLogString("Milli Rehnison told me of a kidnapping! It seems", 22, this.getQuestID(), FIND_BUILDING);
		a.sendQuestLogString("a group of men kidnapped Elena.", 23, this.getQuestID(), FIND_BUILDING);
		a.sendQuestLogString("I found the house where Elena is supposedly being kept.", 25, this.getQuestID(), TALK_TO_BRAVEK);
		a.sendQuestLogString("Bravek gave me a warrant to search the house after I", 27, this.getQuestID(), WARRANT_GET);
		a.sendQuestLogString("helped cure his hangover with a strange recipe.", 28, this.getQuestID(), WARRANT_GET);
		a.sendQuestLogString("I did it! I saved Elena from danger! I should go back", 30, this.getQuestID(), GET_REWARD);
		a.sendQuestLogString("to her father and claim my reward.", 31, this.getQuestID(), GET_REWARD);
		
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
			case BOOK_RETURNED:
				a.sendQuestLogString("I should ask them about Elena, I'm sure they know", lastIndex + 1);
				a.sendQuestLogString("where she may be.", lastIndex + 2);
				break;
			case FIND_BUILDING:
				a.sendQuestLogString("Milli said the building is to the southeast,", lastIndex + 1);
				a.sendQuestLogString("and that it has been cordoned off.", lastIndex + 2);
				break;
			case TALK_TO_BRAVEK:
				a.sendQuestLogString("The mourner outside the house told me it has", lastIndex + 1);
				a.sendQuestLogString("been hit with the plague and I'm not allowed to", lastIndex + 2);
				a.sendQuestLogString("enter. He said I need permission from Bravek or", lastIndex + 3);
				a.sendQuestLogString("the head mourner to enter.", lastIndex + 4);
				break;
			case MAKE_HANGOVER_CURE:
				a.sendQuestLogString("Bravek will talk about permission to enter, but", lastIndex + 1);
				a.sendQuestLogString("he's a bit... hungover. He gave me a recipe for a", lastIndex + 2);
				a.sendQuestLogString("hangover cure, I should make this cure for him.", lastIndex + 3);
				break;
			case WARRANT_GET:
				a.sendQuestLogString("I should use this warrant to find and rescue Elena.", lastIndex + 1);
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
	
	public static void assessPipeGrill(final Player player) {
		ObjectHandler.getInstance().removeObject(2514, 9739, 0, 4);
		new GameObject(Constants.EMPTY_OBJECT, 2514, 9739, 0, 3, 4, 11422, 999999);
		ObjectHandler.getInstance().removeObject(2514, 9739, 0, 4);
		new GameObject(11422, 2514, 9739, 0, 3, 4, 0, 999999);
	}
	
	public static void readHangoverCure(final Player player) {
		ActionSender a = player.getActionSender();
		player.setStatedInterface("hangoverCure");
		a.sendInterface(HANGOVER_CURE_INTERFACE);
		a.sendString("@bla@Got a bncket of nnilk", 1142);
		a.sendString("@bla@Tlen qrind sorne lhoculate", 1143);
		a.sendString("@bla@vnith a pestal and rnortar", 1144);
		a.sendString("@bla@ald the grourd dlocolate to tho milt", 1145);
		a.sendString("@bla@fnnales add scme snapa grasz", 1146);
		a.sendMessage("Your guess is the recipe really says something different.");
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
				assessPipeGrill(player);
				player.setStopPacket(false);
				if (player.getQuestStage(39) < PlagueCity.GRILL_PULLED) {
					player.setStopPacket(true);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}
						@Override
						public void stop() {
							assessPipeGrill(player);
							player.setStopPacket(false);
							player.getDialogue().sendStatement("You fall through...", "...you land in the sewer.", "Edmond follows you down the hole.");
						}
					}, 5);
				} else {
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}
						@Override
						public void stop() {
							PlagueCity.assessPipeGrill(player);
						}
					}, 3);
				}
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
						d.sendPlayerChat("Come on Edmond, we're almost there!", Dialogues.ANGRY_1);
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
						d.sendPlayerChat("I knew you had it in you Edmond. Excellent", "work. Although... I would go home and rest up if I were", "you, those popping noises didn't sound healthy.", CONTENT);
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
				PlagueCity.assessPipeGrill(player);
				player.getActionSender().sendWalkableInterface(-1);
				player.getActionSender().sendMapState(0);
			}
		}, 8);
	}

	public boolean itemHandling(final Player player, int itemId) {
		switch (itemId) {
			case MAGIC_SCROLL:
				Dialogues.startDialogue(player, MAGIC_SCROLL + 10000);
				return true;
			case SCRUFFY_NOTE:
				readHangoverCure(player);
				return true;
		}
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		if((firstItem == SNAPE_GRASS && secondItem == CHOCOLATE_MILK) || (firstItem == CHOCOLATE_MILK && secondItem == SNAPE_GRASS)) {
			if(player.getQuestStage(39) >= MAKE_HANGOVER_CURE) {
				player.getDialogue().sendGiveItemNpc("You mix the snape grass into the bucket.", new Item(HANGOVER_CURE));
				player.getDialogue().endDialogue();
				player.getInventory().replaceItemWithItem(new Item(CHOCOLATE_MILK), new Item(HANGOVER_CURE));
				player.getInventory().removeItem(new Item(firstItem));
			} else {
				player.getDialogue().sendStatement("You see no reason to do that at the moment.");
				player.getDialogue().endDialogue();
			}
			return true;
		}
		return false;
	}

	public boolean doItemOnObject(final Player player, int object, int item) {
		switch(object) {
			case ELENAS_DOOR:
				if(item == SMALL_KEY) {
					if(player.getPosition().getX() < 2540) {
						player.getDialogue().sendStatement("You unlock the door and walk through.");
						player.getDialogue().endDialogue();
						player.getActionSender().walkThroughDoor(ELENAS_DOOR, 2539, 9672, 0);
						player.getActionSender().walkTo(1, player.getPosition().getY() == 9672 ? 0 : player.getPosition().getY() < 9672 ? 1 : -1, true);
					} else {
						player.getActionSender().walkThroughDoor(ELENAS_DOOR, 2539, 9672, 0);
						player.getActionSender().walkTo(-1, player.getPosition().getY() == 9672 ? 0 : player.getPosition().getY() < 9672 ? 1 : -1, true);
					}
					return true;
				}
			case PIPE_GRILL:
				if (item == ROPE && player.getQuestStage(39) == DIG) {
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
				} else {
					assessPipeGrill(player);
					return false;
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
		if(itemId == HANGOVER_CURE && npc.getNpcId() == BRAVEK) {
			Dialogues.startDialogue(player, BRAVEK);
			return true;
		}
		return false;
	}

	public boolean doNpcClicking(Player player, Npc npc) {
		return false;
	}

	public boolean doObjectClicking(final Player player, int object, int x, int y) {
		switch (object) {
			case ELENAS_DOOR:
				if (x == 2539 && y == 9672) {
					if (player.getPosition().getX() < 2540) {
						if (player.getInventory().playerHasItem(SMALL_KEY)) {
							player.getDialogue().sendStatement("You unlock the door and walk through.");
							player.getDialogue().endDialogue();
							player.getActionSender().walkThroughDoor(ELENAS_DOOR, 2539, 9672, 0);
							player.getActionSender().walkTo(1, player.getPosition().getY() == 9672 ? 0 : player.getPosition().getY() < 9672 ? 1 : -1, true);
						} else {
							player.getActionSender().sendMessage("The door is locked.");
						}
					} else {
						player.getActionSender().walkThroughDoor(ELENAS_DOOR, 2539, 9672, 0);
						player.getActionSender().walkTo(-1, player.getPosition().getY() == 9672 ? 0 : player.getPosition().getY() < 9672 ? 1 : -1, true);
					}
					return true;
				}
				return false;
			case CUPBOARD:
				if (x == 2574 && y == 3334 && player.getQuestStage(this.getQuestID()) >= MASK_GET) {
					Dialogues.startDialogue(player, CUPBOARD + 10000);
					return true;
				}
				return false;
			case NURSE_CUPBOARD:
				if (x == 2517 && y == 3276) {
					if(!player.getInventory().ownsItem(430)) {
						player.getActionSender().sendMessage("You find a doctor's gown inside the cupboard.");
						player.getUpdateFlags().sendAnimation(TheGrandTree.PLACE_ANIM);
						player.getInventory().addItemOrDrop(new Item(430));
						return true;
					}
				}
				return false;
			case SPOOKY_STAIRS_UP:
				if (x == 2536 && y == 9671) {
					player.teleport(UP_FROM_ELENA);
					return true;
				}
				return false;
			case SPOOKY_STAIRS_DOWN:
				if (x == 2536 && y == 3268) {
					player.teleport(DOWN_TO_ELENA);
					return true;
				}
				return false;
			case BARREL:
				if (x == 2534 && y == 3268 && !player.getInventory().playerHasItem(SMALL_KEY)) {
					player.getUpdateFlags().sendAnimation(TheGrandTree.PLACE_ANIM);
					player.getInventory().addItem(new Item(SMALL_KEY));
					player.getActionSender().sendMessage("You find a small key in the barrel.");
					return true;
				}
				return false;
			case BRAVEKS_DOOR:
				if (x == 2530 && y == 3314) {
					if (player.getPosition().getX() < 2530) {
						if (player.getQuestVars().allowedToSeeBravek || player.getQuestStage(this.getQuestID()) >= MAKE_HANGOVER_CURE) {
							player.getActionSender().walkThroughDoor(BRAVEKS_DOOR, 2530, 3314, 0);
							player.getActionSender().walkTo(1, player.getPosition().getY() == 3314 ? 0 : player.getPosition().getY() < 3314 ? 1 : -1, true);
						} else {
							player.getDialogue().setLastNpcTalk(CLERK);
							player.getDialogue().sendNpcChat("Mr. Bravek is quite busy, perhaps I can be", "of assistance.", CONTENT);
							player.getDialogue().endDialogue();
						}
					} else {
						player.getActionSender().walkThroughDoor(BRAVEKS_DOOR, 2530, 3314, 0);
						player.getActionSender().walkTo(-1, player.getPosition().getY() == 3314 ? 0 : player.getPosition().getY() < 3314 ? 1 : -1, true);
					}
					return true;
				}
				return false;
			case RESTRICTED_DOOR:
				if ((x == 2533 && y == 3272) || (x == 2540 && y == 3273)) {
					if (player.getEquipment().getId(Constants.HAT) != GAS_MASK) {
						player.getDialogue().sendPlayerChat("Hmm, I should probably be wearing my gas mask before", "I try entering here.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					}
					if (player.getPosition().getY() >= y) {
						Dialogues.startDialogue(player, RESTRICTED_DOOR + 10000);
					} else {
						player.getActionSender().walkThroughDoor(RESTRICTED_DOOR, x, y, 0);
						player.getActionSender().walkTo(player.getPosition().getX() == x ? 0 : player.getPosition().getX() < x ? 1 : -1, 1, true);
					}
					return true;
				}
				return false;
			case REHNISON_DOOR:
				if (x == 2531 && y == 3328) {
					if (player.getPosition().getY() < 3329) {
						if (player.getQuestStage(39) <= DELIVER_BOOK) {
							Dialogues.startDialogue(player, REHNISON_DOOR + 10000);
						} else {
							player.getActionSender().walkThroughDoor(REHNISON_DOOR, 2531, 3328, 0);
							player.getActionSender().walkTo(player.getPosition().getX() == 2531 ? 0 : player.getPosition().getX() < 2531 ? 1 : -1, 1, true);
						}
					} else {
						player.getActionSender().walkThroughDoor(REHNISON_DOOR, 2531, 3328, 0);
						player.getActionSender().walkTo(player.getPosition().getX() == 2531 ? 0 : player.getPosition().getX() < 2531 ? 1 : -1, -1, true);
					}
					return true;
				}
				return false;
			case REHNISON_STAIRS_UP:
				if (x == 2527 && y == 3332) {
					player.teleport(new Position(2527, 3331, 1));
					return true;
				}
				return false;
			case REHNISON_STAIRS_DOWN:
				if (x == 2527 && y == 3332) {
					player.teleport(new Position(2528, 3331, 0));
					return true;
				}
				return false;
			case MANHOLE:
				Ladders.climbLadderDown(player, DOWN_FROM_MANHOLE);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						b.stop();
					}

					@Override
					public void stop() {
						PlagueCity.assessPipeGrill(player);
						player.getActionSender().sendMessage("You climb down through the manhole.");
						CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
							@Override
							public void execute(CycleEventContainer b) {
								b.stop();
							}

							@Override
							public void stop() {
								PlagueCity.assessPipeGrill(player);
							}
						}, 3);
					}
				}, 3);
				return true;
			case PIPE:
				if (player.getQuestStage(this.getQuestID()) < GRILL_PULLED) {
					player.getDialogue().sendStatement("There is a large metal grill blocking the way.");
					player.getDialogue().endDialogue();
				} else if (player.getEquipment().getId(Constants.HAT) != GAS_MASK) {
					player.getDialogue().sendPlayerChat("Hmm, I should probably be wearing my gas mask before", "I climb through this pipe.", CONTENT);
					player.getDialogue().endDialogue();
				} else {
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						int count = 0;

						@Override
						public void execute(CycleEventContainer b) {
							count++;
							player.getUpdateFlags().sendFaceToDirection(new Position(2514, 9738, 0));
							player.getUpdateFlags().setFaceToDirection(true);
							player.getUpdateFlags().setUpdateRequired(true);
							if (player.getPosition().getX() == 2514 && player.getPosition().getY() == 9739) {
								player.getUpdateFlags().sendAnimation(3195, 10);
								b.stop();
							}
							if (count >= 5) {
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
				}
				return true;
			case PIPE_GRILL:
				if (player.getQuestStage(39) == DIG) {
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						int count = 0;
						@Override
						public void execute(CycleEventContainer b) {
							count++;
							player.getUpdateFlags().sendFaceToDirection(new Position(2514, 9738, 0));
							player.getUpdateFlags().setFaceToDirection(true);
							player.getUpdateFlags().setUpdateRequired(true);
							if (player.getPosition().getX() == 2514 && player.getPosition().getY() == 9739) {
								player.getUpdateFlags().sendAnimation(3192, 10);
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
						}
					}, 1);
					player.getDialogue().sendStatement("The grill is too secure.", "You can't pull it off alone.");
					player.getQuestVars().triedPipeGrill = true;
					return true;
				} else {
					assessPipeGrill(player);
					return false;
				}
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
		if(id < 6391 && NpcDefinition.forId(id) != null && NpcDefinition.forId(id).getName().toLowerCase().contains("mourner")) {
			d.sendNpcChat("Get out of my way, peasant.", ANGRY_1);
			d.endDialogue();
			return true;
		}
		switch (id) { //Npc ID
			case MAGIC_SCROLL + 10000:
				switch(d.getChatId()) {
					case 1:
						d.sendGiveItemNpc("You memorize what is written on the scroll.", new Item(MAGIC_SCROLL));
						player.getQuestVars().setCanTeleportArdougne(true);
						player.getInventory().removeItem(new Item(MAGIC_SCROLL));
						player.getActionSender().sendMessage("The scroll crumbles to dust.");
						return true;
					case 2:
						d.sendStatement("You can now cast the Ardougne Teleport spell provided you have the", "required runes and magic level.");
						d.endDialogue();
						return true;
				}
			return false;
			case 785:
			case 786:
			case 787: //Civilians
				switch(d.getChatId()) {
					case 1:
						d.sendNpcChat("Damned rats, they'll be the end of us all", "I know it.", ANGRY_1);
						return true;
					case 2:
						d.sendPlayerChat("They're just rats.", CONTENT);
						return true;
					case 3:
						d.sendNpcChat("Rats that carry this disastrous plague!", ANGRY_1);
						return true;
					case 4:
						d.sendPlayerChat("Well, sorry about that. Wish I could help.", CONTENT);
						return true;
					case 5:
						d.sendNpcChat("Say, maybe you can help, you look like", "an outsider to me. You wouldn't happen to have a full", "grown cat would you?", CONTENT);
						return true;
					case 6:
						if(player.getCat().hasCat() && player.getCat().getGrowthStage() > 1) {
							d.sendPlayerChat("I do, actually... Why?", CONTENT);
						} else {
							d.sendPlayerChat("Er, I don't have one, sorry.", CONTENT);
							d.endDialogue();
						}
						return true;
					case 7:
						d.sendNpcChat("Tell you what, here's what I'm thinking:", "Cats love rats, right? If you were to", "sell me your cat for say... 100 Death Runes, I could", "use it to help kill off these rats!", CONTENT);
						return true;
					case 8:
						d.sendNpcChat("So, what do you say? A fair trade?", "Your cat for 100 Death Runes?", CONTENT);
						return true;
					case 9:
						d.sendOption("Yes. (This is irreversible, you will lose your cat.)", "No, thank you.");
						return true;
					case 10:
						switch(optionId) {
							case 1:
								if(player.getInventory().playerHasItem(player.getCat().getCatItem())) {
									d.sendPlayerChat("Yes, I'll do it.", CONTENT);
								} else {
									d.sendPlayerChat("I don't seem to have my cat with me.", CONTENT);
									d.endDialogue();
								}
								return true;
							case 2:
								d.sendPlayerChat(d.tempStrings[1], CONTENT);
								d.endDialogue();
								return true;
						}
					case 11:
						d.sendGiveItemNpc("You exchange your cat for Death Runes.", "", new Item(player.getCat().getCatItem()), new Item(560));
						d.endDialogue();
						player.getInventory().replaceItemWithItem(new Item(player.getCat().getCatItem()), new Item(560, 100));
						player.getCat().resetCat();
						return true;
				}
			return false;
			case ELENA:
				switch (player.getQuestStage(this.getQuestID())) {
					case GET_REWARD:
						d.sendNpcChat("Go and see my father, I'll make sure he adequately", "rewards you. I'll catch up with you later.", CONTENT);
						d.endDialogue();
						return true;
					case WARRANT_GET:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hi, you're free to go! Your kidnappers don't seem to be", "about right now.", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Thank you, being kidnapped was so inconvenient. I was", "on my way back to East Ardougne with some samples,", "I want to see if I can diagnose a cure for this plague.", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("Well, you can leave via the manhole near the gate.", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("Go and see my father, I'll make sure he adequately", "rewards you. I'll catch up with you later.", CONTENT);
								d.endDialogue();
								player.setQuestStage(this.getQuestID(), GET_REWARD);
								return true;
						}
					return false;
				}
			return false;
			case CUPBOARD + 10000:
				switch(d.getChatId()) {
					case 1:
						d.sendStatement("You open the cupboard and find a spare gas mask inside.", "Take it?");
						return true;
					case 2:
						d.sendOption("Yes.", "No.");
						return true;
					case 3:
						switch(optionId) {
							case 1:
								if(player.getInventory().ownsItem(GAS_MASK)) {
									d.sendPlayerChat("I still have this gas mask, I probably", "shouldn't take Elena's extra.", CONTENT);
									
								} else {
									d.sendGiveItemNpc("You take the spare gas mask.", new Item(GAS_MASK));
									player.getInventory().addItem(new Item(GAS_MASK));
								}
								d.endDialogue();
								return true;
							case 2:
								d.sendStatement("You close the cupboard.");
								d.endDialogue();
								return true;
						}
				}
			return false;
			case BRAVEK:
				switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
					default:
						d.sendNpcChat("My head hurts! I'll speak to you another day...", ANGRY_1);
						d.endDialogue();
						return true;
					case WARRANT_GET:
						switch (d.getChatId()) {
							case 1:
								if(player.getInventory().ownsItem(WARRANT)) {
									d.sendNpcChat("Good luck with finding Elena adventurer.", CONTENT);
								} else {
									d.sendGiveItemNpc("Bravek hands you a warrant.", new Item(WARRANT));
									player.getInventory().addItemOrDrop(new Item(WARRANT));
								}
								d.endDialogue();
								return true;
						}
					return false;
					case MAKE_HANGOVER_CURE:
						switch (d.getChatId()) {
							case 1:
								if (player.getQuestVars().healedHangover) {
									d.sendNpcChat("Ooh, that's much better! Thanks, that's the clearest my", "head has felt in a month. Ah now, what was it you", "wanted me to do for you?", CONTENT);
									d.setNextChatId(5);
								} else {
									d.sendNpcChat("Uurgh! My head still hurts too much to think straight.", "Oh for one of Trudi's hangover cures!", DISTRESSED);
									if (!player.getInventory().playerHasItem(HANGOVER_CURE)) {
										d.endDialogue();
									}
								}
								return true;
							case 2:
								d.sendPlayerChat("Try this.", CONTENT);
								return true;
							case 3:
								d.sendGiveItemNpc("You give Bravek the hangover cure. Bravek gulps down", "the foul-looking liquid.", new Item(HANGOVER_CURE));
								player.getInventory().removeItem(new Item(HANGOVER_CURE));
								player.getQuestVars().healedHangover = true;
								World.getNpcs()[World.getNpcIndex(BRAVEK)].getUpdateFlags().setForceChatMessage("Grruurgh!");
								return true;
							case 4:
								d.sendNpcChat("Ooh, that's much better! Thanks, that's the clearest my", "head has felt in a month. Ah now, what was it you", "wanted me to do for you?", CONTENT);
								return true;
							case 5:
								d.sendPlayerChat("I need to rescue a kidnap victim called Elena. She's", "being held in a plague house, I need permission to", "enter.", CONTENT);
								return true;
							case 6:
								d.sendNpcChat("Well, the mourners deal with that sort of thing...", CONTENT);
								return true;
							case 7:
								d.sendPlayerChat("They won't listen to me! They say I'm some", "sort of pheasant or something!", ANGRY_1);
								return true;
							case 8:
								d.sendNpcChat("Hmmmm, well I guess they're not taking the issue of a", "kidnapping seriously enough. They do go a bit far", "sometimes. I've heard of Elena, she has helped us a lot...", "Ok, I'll give you this warrant to enter the house.", CONTENT);
								return true;
							case 9:
								d.sendGiveItemNpc("Bravek hands you a warrant.", new Item(WARRANT));
								d.endDialogue();
								player.getInventory().addItemOrDrop(new Item(WARRANT));
								player.setQuestStage(this.getQuestID(), WARRANT_GET);
								return true;
						}
					return false;
					case TALK_TO_BRAVEK:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("My head hurts! I'll speak to you another day...", ANGRY_1);
								return true;
							case 2:
								d.sendOption("This is really important though!", "Ok, goodbye.");
								return true;
							case 3:
								d.sendPlayerChat(d.tempStrings[optionId - 1], CONTENT);
								if(optionId == 2) {
									d.endDialogue();
								}
								return true;
							case 4:
								d.sendNpcChat("I can't possibly speak to you with my head spinning like", "this... I went a bit heavy on the drink again last night.", "Curse my herbalist, she made the best hangover cures.", "Darn inconvenient of her catching the plague.", ANGRY_1);
								return true;
							case 5:
								d.sendOption("Ok, goodbye.", "You shouldn't drink so much then!", "Do you know what's in the cure?");
								return true;
							case 6:
								d.sendPlayerChat(d.tempStrings[optionId - 1], CONTENT);
								switch(optionId) {
									case 1:
										d.endDialogue();
										break;
									case 3:
										d.setNextChatId(10);
										break;
								}
								return true;
							case 7:
								d.sendNpcChat("HAH! You think anything keeps this town running", "except for the sweet, sweet nectar of liquor? Think", "again adventurer!", Dialogues.LAUGHING);
								d.setNextChatId(5);
								return true;
							case 10:
								d.sendNpcChat("Hmmm, let me think... Ouch! Thinking isn't clever. Ah", "here, she did scribble it down for me.", ANGRY_1);
								return true;
							case 11:
								d.sendGiveItemNpc("Bravek hands you a tatty piece of paper.", new Item(SCRUFFY_NOTE));
								d.endDialogue();
								player.getInventory().addItemOrDrop(new Item(SCRUFFY_NOTE));
								player.setQuestStage(this.getQuestID(), MAKE_HANGOVER_CURE);
								return true;
						}
					return false;
				}
			case CLERK:
				switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
					default:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Hello, wecome to the Civic Office of West Ardougne.", "How can I help you?", CONTENT);
								return true;
							case 2:
								d.sendPlayerChat("I'm just looking, thanks.", CONTENT);
								d.endDialogue();
								return true;
						}
					return false;
					case TALK_TO_BRAVEK:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Hello, wecome to the Civic Office of West Ardougne.", "How can I help you?", CONTENT);
								return true;
							case 2:
								d.sendOption("I need permission to enter a plague house.", "Who is through that door?", "I'm just looking thanks.");
								return true;
							case 3:
								d.sendPlayerChat(d.tempStrings[optionId - 1], CONTENT);
								switch(optionId) {
									case 1:
										d.setNextChatId(5);
										break;
									case 3:
										d.endDialogue();
										break;
								}
								return true;
							case 4:
								d.sendNpcChat("Bravek the city warder who is in charge of overseeing", "this administration of West Ardougne, possessing a", "higher authority than the Head Mourner.", CONTENT);
								d.setNextChatId(2);
								return true;
							case 5:
								d.sendNpcChat("Rather you than me! The mourners normally deal with", "that stuff, you should speak to them. Their headquarters", "are right near the city gate.", CONTENT);
								return true;
							case 6:
								d.sendOption("I'll try asking them then.", "Surely you don't let them run everything for you?", "This is urgent though!");
								return true;
							case 7:
								d.sendPlayerChat(d.tempStrings[optionId - 1], CONTENT);
								switch(optionId) {
									case 1:
										d.endDialogue();
										break;
									case 3:
										d.setNextChatId(10);
										break;
								}
								return true;
							case 8:
								d.sendNpcChat("Well, that's a matter for Bravek, sir. Bravek", "and this administration hold higher authority than", "the mourners, but Bravek usually delegates all tasks", "related to the plague to them, as he doesn't", CONTENT);
								return true;
							case 9:
								d.sendNpcChat("believe the plague is as harmful as the mourners", "say it is.", CONTENT);
								d.setNextChatId(6);
								return true;
							case 10:
								d.sendPlayerChat("Someone's been kipnapped and is being held", "in a plague house!", DISTRESSED);
								return true;
							case 11:
								d.sendNpcChat("I'll see what I can do I suppose.", CONTENT);
								return true;
							case 12:
								d.sendNpcChat("Mr. Bravek, there's a man here who really needs to", "speak to you.", CONTENT);
								return true;
							case 13:
								d.setLastNpcTalk(BRAVEK);
								d.sendNpcChat("I suppose they can come in then. If they keep it short.", CONTENT);
								d.endDialogue();
								player.getQuestVars().allowedToSeeBravek = true;
								return true;
						}
					return false;
				}
			case RESTRICTED_DOOR + 10000:
				int mourner = (player.getPosition().getX() < 2536 ? MOURNER_3 : MOURNER_2);
				int otherMourner = (mourner == MOURNER_3 ? MOURNER_2 : MOURNER_3);
				d.setLastNpcTalk(mourner);
				switch(player.getQuestStage(this.getQuestID())) {
					default:
						d.sendNpcChat("I'd stand away from there. That black cross means that", "house has been touched by the plague.", CONTENT);
						d.endDialogue();
						return true;
					case WARRANT_GET:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("I'd stand away from there. That black cross means that", "house has been touched by the plague.", CONTENT);
								if(!player.getInventory().playerHasItem(WARRANT)) {
									d.endDialogue();
								}
								return true;
							case 2:
								d.sendPlayerChat("I have a warrant from Bravek to enter here.", CONTENT);
								return true;
							case 3:
								d.sendNpcChat("This is highly irregular. Please wait...", CONTENT);
								return true;
							case 4:
								player.getActionSender().removeInterfaces();
								Npc mourner1 = World.getNpcs()[World.getNpcIndex(mourner)];
								Npc mourner2 = World.getNpcs()[World.getNpcIndex(otherMourner)];
								mourner1.getUpdateFlags().setFace(mourner2.getPosition());
								mourner1.getUpdateFlags().setFaceToDirection(true);
								mourner1.getMovementHandler().setCanWalk(false);
								mourner1.getUpdateFlags().setForceChatMessage("We got someone here with a warrant from Bravek. What do?");
								CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
									@Override
									public void execute(CycleEventContainer b) {
										b.stop();
									}
									@Override
									public void stop() {
										Dialogues.sendDialogue(player, RESTRICTED_DOOR+10000, 5, 0);
									}
								}, 3);
								return true;
							case 5:
								d.sendStatement("You wait until the mourner's back is turned and sneak into the", "building.");
								d.endDialogue();
								final int mourner_f = mourner;
								final Position toEval = mourner == MOURNER_3 ? new Position(2533, 3272, 0) : new Position(2540, 3273, 0);
								if(player.getPosition().equals(toEval)) {
									player.getActionSender().walkThroughDoor(RESTRICTED_DOOR, toEval.getX(), toEval.getY(), 0);
									player.getActionSender().walkTo(player.getPosition().getX() == toEval.getX() ? 0 : player.getPosition().getX() < toEval.getX() ? 1 : -1, -1, true);
								} else {
									CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
										int count = 0;
										@Override
										public void execute(CycleEventContainer b) {
											count++;
											player.walkTo(toEval, true);
											if (player.getPosition().equals(toEval)) {
												player.getActionSender().walkThroughDoor(RESTRICTED_DOOR, toEval.getX(), toEval.getY(), 0);
												player.getActionSender().walkTo(player.getPosition().getX() == toEval.getX() ? 0 : player.getPosition().getX() < toEval.getX() ? 1 : -1, -1, true);
											}
											if(count >= 3) {
												b.stop();
											}
										}
										@Override
										public void stop() {
											Npc mourner1 = World.getNpcs()[World.getNpcIndex(mourner_f)];
											mourner1.getUpdateFlags().setFaceToDirection(false);
											mourner1.getMovementHandler().setCanWalk(true);
										}
									}, 1);
								}
								return true;		
						}
					return false;
					case FIND_BUILDING:
						switch (d.getChatId()) {
							case 1:
								player.getDialogue().sendStatement("The door won't open.", "You notice a black cross on the door.");
								return true;
							case 2:
								d.sendNpcChat("I'd stand away from there. That black cross means that", "house has been touched by the plague.", CONTENT);
								return true;
							case 3:
								d.sendOption("But I think a kidnap victim is in here.", "Thanks for the warning.");
								return true;
							case 4:
								d.sendPlayerChat(d.tempStrings[optionId - 1], CONTENT);
								if (optionId == 2) {
									d.endDialogue();
								}
								return true;
							case 5:
								d.sendNpcChat("Sounds unlikely, even kidnappers wouldn't go in there.", "Even if someone is in there, they're probably dead by", "now.", CONTENT);
								return true;
							case 6:
								d.sendOption("Good point.", "I want to check anyway.");
								return true;
							case 7:
								d.sendPlayerChat(d.tempStrings[optionId - 1], CONTENT);
								if (optionId == 1) {
									d.endDialogue();
								}
								return true;
							case 8:
								d.sendNpcChat("You don't have clearance to go in there.", CONTENT);
								return true;
							case 9:
								d.sendPlayerChat("How do I get clearance?", CONTENT);
								return true;
							case 10:
								d.sendNpcChat("Well you'd need to apply to the head mourner,", "or I suppose Bravek the city warder.", CONTENT);
								return true;
							case 11:
								d.sendNpcChat("I wouldn't get your hopes up though.", CONTENT);
								d.endDialogue();
								player.setQuestStage(this.getQuestID(), TALK_TO_BRAVEK);
								return true;
						}
						return false;
				}
			case TED_REHNISON:
			case MARTHA_REHNISON:
				switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
					case FIND_BUILDING:
					case TALK_TO_BRAVEK:
					case MAKE_HANGOVER_CURE:
					case WARRANT_GET:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Any luck finding Elena yet?", CONTENT);
								return true;
							case 2:
								d.sendPlayerChat("Not yet...", SAD);
								return true;
							case 3:
								d.sendNpcChat("I wish you luck, she did a lot for us.", CONTENT);
								d.endDialogue();
								return true;
						}
					return false;
					case BOOK_RETURNED:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hi, I hear a woman called Elena is staying here.", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Yes, she was staying here, but slightly over a week ago", "she was getting ready to go back. However, she never", "managed to leave. My daughter Milli was playing near", "the west wall when she says some shadowy figures jump", DISTRESSED);
								return true;
							case 3:
								d.sendNpcChat("out and grab her. Milli is upstairs if you wish to speak", "to her.", DISTRESSED);
								d.endDialogue();
								player.getQuestVars().talkedToParents = true;
								return true;
						}
					return false;
				}
			return false;
			case BILLY_REHNISON:
				switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
					case BOOK_RETURNED:
						player.getActionSender().sendMessage("Billy isn't interested in talking.");
						return true;
				}
			return false;
			case MILLI_REHNISON:
				switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
					case FIND_BUILDING:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Which building was it again?", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("It was the boarded up building with no windows in the", "south east corner...", SAD);
								return true;
							case 3:
								d.sendPlayerChat("Alright, thanks.", CONTENT);
								d.endDialogue();
								return true;
						}
					return false;
					case BOOK_RETURNED:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello.", CONTENT);
								if(!player.getQuestVars().talkedToParents) {
									d.endDialogue();
									player.getActionSender().sendMessage("Milli ignores you.");
								}
								return true;
							case 2:
								d.sendPlayerChat("Your parents say you saw what happened to Elena...", CONTENT);
								return true;
							case 3:
								d.sendNpcChat("*sniff* Yes, I was near the south east corner when I", "saw Elena walking by. I was about to run to greet her", "when some men jumped out. They shoved a sack over", "her head and dragged her into a building.", Dialogues.NEAR_TEARS);
								return true;
							case 4:
								d.sendPlayerChat("Which building?", CONTENT);
								return true;
							case 5:
								d.sendNpcChat("It was the boarded up building with no windows in the", "south east corner...", SAD);
								return true;
							case 6:
								d.sendPlayerChat("Alright, thanks.", CONTENT);
								d.endDialogue();
								player.setQuestStage(39, FIND_BUILDING);
								return true;
						}
					return false;
				}
			return false;
			case REHNISON_DOOR + 10000:
				switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
					case BOOK_RETURNED:
						switch (d.getChatId()) {
							case 6:
								d.setLastNpcTalk(TED_REHNISON);
								d.sendNpcChat("Thanks, I've been missing that.", CONTENT);
								d.endDialogue();
								return true;	
						}
					return false;
					case DELIVER_BOOK:
						switch (d.getChatId()) {
							case 1:
								d.setLastNpcTalk(TED_REHNISON);
								d.sendNpcChat("Go away. We don't want any.", ANGRY_1);
								return true;
							case 2:
								if(player.getInventory().playerHasItem(BOOK)) {
									d.sendPlayerChat("I'm a friend of Jethick's, I have come to return a book", "he borrowed.", CONTENT);
								} else {
									d.sendPlayerChat("I, er... forgot your book, hold on...", DISTRESSED);
									d.endDialogue();
								}
								return true;
							case 3:
								d.setLastNpcTalk(TED_REHNISON);
								d.sendNpcChat("Oh... Why didn't you say, come in then.", HAPPY);
								return true;
							case 4:
								if(player.getPosition().equals(new Position(2531, 3328, 0))) {
									player.getActionSender().walkThroughDoor(REHNISON_DOOR, 2531, 3328, 0);
									player.getActionSender().walkTo(player.getPosition().getX() == 2531 ? 0 : player.getPosition().getX() < 2531 ? 1 : -1, 1, true);
									Dialogues.sendDialogue(player, REHNISON_DOOR + 10000, 5, 0);
								} else {
									CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
										int count = 0;
										@Override
										public void execute(CycleEventContainer b) {
											count++;
											player.walkTo(new Position(2531, 3328, 0), true);
											if (player.getPosition().equals(new Position(2531, 3328, 0))) {
												player.getActionSender().walkThroughDoor(REHNISON_DOOR, 2531, 3328, 0);
												player.getActionSender().walkTo(player.getPosition().getX() == 2531 ? 0 : player.getPosition().getX() < 2531 ? 1 : 1, 1, true);
												Dialogues.sendDialogue(player, REHNISON_DOOR + 10000, 5, 0);
											}
											if(count >= 3) {
												b.stop();
											}
										}
										@Override
										public void stop() {
										}
									}, 1);
								}
								return true;
							case 5:
								d.sendGiveItemNpc("You hand the book to Ted as you enter.", new Item(BOOK));
								player.getInventory().removeItem(new Item(BOOK));
								player.setQuestStage(this.getQuestID(), BOOK_RETURNED);
								return true;
						}
					return false;
				}
			return false;
			case JETHICK:
				switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
					case DELIVER_BOOK:
						switch (d.getChatId()) {
							case 1:
								if(player.getInventory().playerHasItem(BOOK)) {
									d.sendPlayerChat("Where do the Rehnisons live again?", CONTENT);
									d.setNextChatId(5);
								} else {
									d.sendPlayerChat("I erm, lost that book.", SAD);
								}
								return true;
							case 2:
								d.sendNpcChat("Clumsy, clumsy. I found it lying around in the", "dirt. Thanks for that. Here it is again...", ANGRY_1);
								return true;
							case 3:
								d.sendGiveItemNpc("Jethick gives you a book.", new Item(BOOK));
								d.endDialogue();
								player.getInventory().addItemOrDrop(new Item(BOOK));
								return true;
							case 5:
								d.sendNpcChat("They live in the small timbered building at the far", "north side of town. Do hurry!", CONTENT);
								d.endDialogue();
								return true;
						}
					return false;
					case GRILL_PULLED:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Hello, I don't recognize you. We don't get many", "newcomers around here.", CONTENT);
								return true;
							case 2:
								d.sendPlayerChat("Hi, I'm looking for a woman from East Ardougne", "named Elena.", CONTENT);
								return true;
							case 3:
								d.sendNpcChat("East Ardougnian women are easier to find in East", "Ardougne. Not many would come to West Ardougne to", "find one. Although the name is familiar, what does she", "look like?", CONTENT);
								return true;
							case 4:
								if(player.getInventory().playerHasItem(ELENAS_PICTURE)) {
									d.sendGiveItemNpc("You show Jethick the picture.", new Item(ELENAS_PICTURE));
								} else {
									d.sendPlayerChat("Um... brown hair... in her twenties...", CONTENT);
									d.setNextChatId(10);
								}
								return true;
							case 5:
								d.sendNpcChat("She came over here to help to aid plague victims. I", "think she is staying over with the Rehnison family. They", "live in the small timbered building at the far north side", "of town. I've not seen her around here in a while.", CONTENT);
								return true;
							case 6:
								d.sendNpcChat("I don't suppose you could run me a little errand while", "you're over there? I borrowed this book from them, can", "you return it?", CONTENT);
								return true;
							case 7:
								d.sendOption("Yes, I'll return it for you.", "No, I don't have time for that.");
								return true;
							case 8:
								d.sendPlayerChat(d.tempStrings[optionId - 1], CONTENT);
								if(optionId == 2) {
									d.endDialogue();
								}
								return true;
							case 9:
								d.sendGiveItemNpc("Jethick gives you a book.", new Item(BOOK));
								d.endDialogue();
								player.getInventory().addItemOrDrop(new Item(BOOK));
								player.setQuestStage(this.getQuestID(), DELIVER_BOOK);
								return true;
							case 10:
								d.sendNpcChat("Hmmm, that doesn't narrow it down a huge amount...", "I'll need to know more than that, or see a picture?", CONTENT);
								d.endDialogue();
								return true;	
						}
					return false;
				}
			return false;
			case ALRENA:
				switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
					case DIG:
					case GRILL_PULLED:
					case DELIVER_BOOK:
					case BOOK_RETURNED:
					case FIND_BUILDING:
					case TALK_TO_BRAVEK:
					case MAKE_HANGOVER_CURE:
					case WARRANT_GET:
						d.sendNpcChat("Oh, do hurry and find Elena! I fear she", "is in great danger!", SAD);
						d.endDialogue();
						return true;
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
					case QUEST_COMPLETE:
						switch (d.getChatId()) {
							case 1:
								if (!player.getQuestVars().canTeleportArdougne() && !player.getInventory().ownsItem(MAGIC_SCROLL)) {
									if (player.getInventory().canAddItem(new Item(MAGIC_SCROLL))) {
										d.sendGiveItemNpc("Edmond hands you another Magic Scroll.", new Item(MAGIC_SCROLL));
										player.getInventory().addItem(new Item(MAGIC_SCROLL));
									} else {
										d.sendNpcChat("Oh, you don't have room for the scroll! Make", "some room so I can give it to you.", CONTENT);
									}
								} else {
									d.sendNpcChat("Oh, thank you again adventurer! It is so", "good to see Elena home and safe again.", HAPPY);
								}
								d.endDialogue();
								return true;
						}
						return false;
					case GET_REWARD:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Thank you, thank you! Elena beat you back by", "minutes. Now I said I'd give you a reward. What can", "I give you as a reward I wonder? Here, take this magic", "scroll, I have little use for it but it may help you.", HAPPY);
								return true;
							case 2:
								if(player.getInventory().canAddItem(new Item(MAGIC_SCROLL))) {
									d.dontCloseInterface();
									QuestHandler.completeQuest(player, this.getQuestID());
								} else {
									d.sendNpcChat("Oh, you don't have room for the scroll! Make", "some room so I can give it to you.", CONTENT);
									d.endDialogue();
									
								}
							return true;
						}
					return false;
					case GRILL_PULLED:
					case DELIVER_BOOK:
					case BOOK_RETURNED:
					case FIND_BUILDING:
					case TALK_TO_BRAVEK:
					case MAKE_HANGOVER_CURE:
					case WARRANT_GET:
						d.sendNpcChat("Oh, do hurry and find Elena! I fear she", "is in great danger!", SAD);
						d.endDialogue();
						return true;
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
								d.sendNpcChat("Take them to my wife Alrena, she's inside", "the house.", CONTENT);
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
								d.sendNpcChat("Elena's a missionary and a healer. Three weeks ago she", "managed to cross the Ardougne wall... No one's allowed", "to cross the wall in case they spread the plague. But", "after hearing the screams of suffering she felt she had", SAD);
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
				}
				return false;
		}
		return false;
	}

}
