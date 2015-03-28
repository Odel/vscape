package com.rs2.model.content.quests.impl.UndergroundPass;

import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.GameObjectData;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.Position;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.combat.weapon.RangedAmmo;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.dialogue.DialogueManager;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.quests.impl.Quest;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.objects.GameObjectDef;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.net.ActionSender;
import com.rs2.util.Misc;

public class UndergroundPass implements Quest {
	public static boolean UNDERGROUND_PASS_ENABLED = false;

	public static final int questIndex = 9927;
	//Quest stages
	public static final int QUEST_STARTED = 1;
	public static final int ENTER_CAVES = 2;
	public static final int CAN_USE_WELL = 3;
	public static final int UNICORN_KILLED = 4;
	public static final int IBANS_LAIR_OPEN = 5;
	public static final int QUEST_COMPLETE = 20;

	//Items
	public static final int ROCK = 1480;
	public static final int ORB_OF_LIGHT = 1481;
	public static final int ORB_OF_LIGHT_2 = 1482;
	public static final int ORB_OF_LIGHT_3 = 1483;
	public static final int ORB_OF_LIGHT_4 = 1484;
	public static final int DAMP_CLOTH = 1485;
	public static final int PIECE_OF_RAILING = 1486;
	public static final int UNICORN_HORN = 1487;
	public static final int PALADINS_BADGE = 1488;
	public static final int PALADINS_BADGE_2 = 1489;
	public static final int PALADINS_BADGE_3 = 1490;
	public static final int WITCHS_CAT = 1491;
	public static final int DOLL_OF_IBAN = 1492;
	public static final int OLD_JOURNAL = 1493;
	public static final int HISTORY_OF_IBAN = 1494;
	public static final int KLANKS_GAUNTLETS = 1495;
	public static final int IBANS_DOVE = 1496;
	public static final int AMULET_OF_OTHANIAN = 1497;
	public static final int AMULET_OF_DOOMION = 1498;
	public static final int AMULET_OF_HOLTHION = 1499;
	public static final int IBANS_SHADOW = 1500;
	public static final int DWARF_BREW = 1501;
	public static final int IBANS_ASHES = 1502;

	//Positions
	//KALRAG new Position(2357, 9911, 0)
	// cave with teleport icon? new Position(2357, 9911, 0)
	public static final Position PASS_ENTRANCE_POS = new Position(2495, 9716, 0);
	public static final Position PASS_EXIT_POS = new Position(2435, 3314, 0);

	//Interfaces
	public static final int INTERFACE = -1;

	//Npcs
	public static final int KING_LATHAS = 364;
	public static final int KOFTIK_NO_HOOD = 972;
	public static final int KOFTIK = 973;
	public static final int KOFTIK_2 = 974;
	public static final int KOFTIK_3 = 975;
	public static final int KOFTIK_4 = 976;
	public static final int BOULDER = 986;
	public static final int SIR_JERRO = 988;
	public static final int SIR_CARL = 989;
	public static final int SIR_HARRY = 990;
	public static final int HALF_SOULLESS = 991;
	public static final int KARDIA = 992;
	public static final int WITCHS_CAT_ITEM = 993;
	public static final int NILOOF = 994;
	public static final int KLANK = 995;
	public static final int KAMEN = 996;
	public static final int KALRAG = 997;
	public static final int OTHAINIAN = 998;
	public static final int DOOMION = 999;
	public static final int HOLTHION = 1000;
	public static final int DARK_MAGE = 1001;
	public static final int DISCIPLE_OF_IBAN = 1002;
	public static final int LORD_IBAN = 1003;
	/*
	979 Slave combat lvl: 0
980 Slave combat lvl: 0
981 Slave combat lvl: 0
982 Slave combat lvl: 0
983 Slave combat lvl: 0
984 Slave combat lvl: 0
985 Slave combat lvl: 0
	*/

	//Objects
	public static final int PASS_ENTRANCE = 3213;
	public static final int PASS_EXIT = 3214;
	public static final int GUIDE_ROPE = 3340;
	
	public static final String[] ibanWhispers = {"Blood, pain and hate.", "Death is only the beginning.", "Kill, maim... murder.", "I'll swallow your soul.", "The power of the gods could be yours.", "Hear me...", "Iban will save you... He'll save us all.", "I will release you...", "Make them all pay!", "Join us!", "I see you adventurer... you can't hide."};
	public static final String[] oldJournalStrings = {"I came to cleanse these", "mountain passes of the", "dark forces that dwell", "here. I knew my journey", "would be treacherous, so", "I deposited Spheres of", "Light in some of the", "tunnels. These spheres", "are a beacon of safety", "for all who come. The", "spheres were created by", "Saradominist mages.", "When held they boost our", "faith and courage. I still", "feel...", "", "Iban relentlessly", "tugging...", "", "at my weak soul.......", "", "bringing out any innate", "goodness to one's heart,", "illuminating the dark", "caverns with the light of", "Saradomin, bringing fear", "and pain to all who", "embrace the dark side.", "My men are still repelled", "by 'Iban's will' - it seems", "as if their pure hearts bar", "them from entering", "Iban's realm. My turn", "has come. I dare not", "admit it to my loyal men,", "but I fear for the welfare", "of my soul." };
	public static final String[] diaryOfRandasStrings = {"It began as a whisper in", "my ears. Dismissing the", "sounds as the whistling of", "the wind I steeled myself", "against these forces, and", "continued on my way.", "", "But then the whispers became", "moans...", "", "", "At once fearsome and", "enticing like the call of", "some beautiful siren.", "", "", "Join us!", "", "Our greatness lies within", "you, but only Zamorak", "can unlock your", "potential..."};
	
	private int reward[][] = { //{itemId, count},
	};

	private int expReward[][] = {
		{Skill.AGILITY, 3000},
		{Skill.ATTACK, 3000}
	};

	private static final int questPointReward = 5;

	public int getQuestID() { //Change
		return 44;
	}

	public String getQuestName() { //Change
		return "Underground Pass";
	}

	public String getQuestSaveName() { //Change
		return "undergroundpass";
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
		player.getActionSender().sendItemOnInterface(12145, 250, DOLL_OF_IBAN); //zoom, then itemId
		player.getActionSender().sendString("You have completed " + getQuestName() + "!", 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("5 Quest Points", 12150);
		player.getActionSender().sendString("6750 Agility XP", 12151);
		player.getActionSender().sendString("6750 Attack XP", 12152);
		player.getActionSender().sendString("Iban Staff", 12153);
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
				lastIndex = 4;
				break;
			case QUEST_COMPLETE:
				lastIndex = 26;
				break;
		}
		lastIndex++;

		ActionSender a = player.getActionSender();
		a.sendQuestLogString("Talk to King Lathas in the Ardougne Castle to begin.", 1, this.getQuestID(), 0);
		a.sendQuestLogString("King Lathas has sent me on a mission to help explore", 3, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("this underground pass to the west.", 4, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("I met Koftik. He fears these caves, but he agreed to", 6, this.getQuestID(), ENTER_CAVES);
		a.sendQuestLogString("help. He said to meet him at the bridge inside the caves.", 7, this.getQuestID(), ENTER_CAVES);
		a.sendQuestLogString("I made it farther into the caverns. I climbed down a well", 9, this.getQuestID(), CAN_USE_WELL);
		a.sendQuestLogString("that seemed to 'accept' me only after I destroyed some", 10, this.getQuestID(), CAN_USE_WELL);
		a.sendQuestLogString("orbs of light that were nearby.", 11, this.getQuestID(), CAN_USE_WELL);
		a.sendQuestLogString("I squashed a unicorn with a giant boulder.", 13, this.getQuestID(), UNICORN_KILLED);
		a.sendQuestLogString("I managed to get access to Iban's inner lair. I only", 15, this.getQuestID(), IBANS_LAIR_OPEN);
		a.sendQuestLogString("had to murder a unicorn and 3 paladins to gain access.", 16, this.getQuestID(), IBANS_LAIR_OPEN);
		switch (questStage) {
			default:
				break;
			case 0:
				a.sendQuestLogString("Talk to @dre@King Lathas @bla@in the @dre@Ardougne Castle @bla@to begin.", 1);
				a.sendQuestLogString("@dre@Requirements:", 3);
				a.sendQuestLogString((QuestHandler.questCompleted(player, QuestHandler.getQuestId("Biohazard")) ? "@str@" : "@dbl@") + "-Biohazard", 5);
				a.sendQuestLogString((player.getSkill().getPlayerLevel(Skill.RANGED) >= 25 ? "@str@" : "@dbl@") + "-25 Range", 6);
				break;
			case QUEST_STARTED:
				a.sendQuestLogString("He told me to meet  his tracker Koftik, who is waiting", lastIndex + 1);
				a.sendQuestLogString("for me in far West Ardougne.", lastIndex + 2);
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
		if (UNDERGROUND_PASS_ENABLED) {
			int questStage = player.getQuestStage(getQuestID());
			if ((questStage >= QUEST_STARTED) && (questStage < QUEST_COMPLETE)) {
				player.getActionSender().sendString("@yel@" + getQuestName(), questIndex);
			} else if (questStage == QUEST_COMPLETE) {
				player.getActionSender().sendString("@gre@" + getQuestName(), questIndex);
			} else {
				player.getActionSender().sendString("@red@" + getQuestName(), questIndex);
			}
		}
	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
		player.getActionSender().sendString(getQuestName(), 8144);
	}
	
	
	
	public static void doLoginChecks(final Player player) {
		if(player.Area(2364, 2414, 9586, 9613)) {
			player.getActionSender().sendMapState(2);
		}
		if(player.Area(2465, 2482, 9671, 9688)) {
			GridMazeHandler.startGridCheck(player);
		}
		if(player.Area(2378, 2465, 9665, 9700)) {
			PassTrapHandling.startTrapCycle(player, 0);
		}
		if(player.Area(2390, 2430, 9697, 9729) || player.Area(2367, 2390, 9663, 9728)) {
			PassTrapHandling.startTrapCycle(player, 2);
		}
		if(player.inUndergroundPass()) {
			startIbanWhispers(player);
		}
	}
	
	public static void startIbanWhispers(final Player player) {
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				if(!player.inUndergroundPass())
					b.stop();
				else
					player.getActionSender().sendMessage(ibanWhispers[Misc.randomMinusOne(ibanWhispers.length)]);
			}
			@Override
			public void stop() {
			}
		}, 120);
	}

	public boolean itemHandling(final Player player, int itemId) {
		switch (itemId) {
			case 1493:
				player.getActionSender().sendMessage("The journal is old and worn.");
				player.getBookHandler().initBook(oldJournalStrings, "The Journal of Randas");
				return true;
		}
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		if(firstItem == DAMP_CLOTH && RangedAmmo.FireArrowData.dampIdForOriginalId(secondItem) != -1) {
			player.getActionSender().sendMessage("You wrap the damp cloth around the arrow head...");
			player.getInventory().replaceItemWithItem(new Item(DAMP_CLOTH), new Item(RangedAmmo.FireArrowData.dampIdForOriginalId(secondItem), 1));
			player.getInventory().removeItem(new Item(secondItem, 1));
			return true;
		} else if (secondItem == DAMP_CLOTH && RangedAmmo.FireArrowData.dampIdForOriginalId(firstItem) != -1) {
			player.getActionSender().sendMessage("You wrap the damp cloth around the arrow head...");
			player.getInventory().replaceItemWithItem(new Item(DAMP_CLOTH), new Item(RangedAmmo.FireArrowData.dampIdForOriginalId(firstItem), 1));
			player.getInventory().removeItem(new Item(firstItem, 1));
			return true;
		}
		return false;
	}

	public boolean doItemOnObject(final Player player, final int object, final int item) {
		if(RangedAmmo.FireArrowData.litIdForDampId(item) != -1) {
			final CacheObject obj = ObjectLoader.object(object, player.getClickX(), player.getClickY(), player.getPosition().getZ());
			final GameObjectDef def = SkillHandler.getObject(object, player.getClickX(),player.getClickY(), player.getPosition().getZ());
			if (obj != null || def != null) {
				String name = GameObjectData.forId(obj != null ? obj.getDef().getId() : def.getId()).getName().toLowerCase();
				if (name.equalsIgnoreCase("fire") || name.equalsIgnoreCase("fireplace")) {
					int amount = player.getInventory().getItemAmount(item);
					if (player.getInventory().playerHasItem(item, amount)) {
						player.getActionSender().sendMessage("You light the cloth wrapped arrow head" + (amount > 1 ? "s." : "."));
						player.getInventory().replaceItemWithItem(new Item(item, amount), new Item(RangedAmmo.FireArrowData.litIdForDampId(item), amount));
						return true;
					}
				}
			}
		}
		switch(object) {
			case 3305:
				if(item >= UNICORN_HORN && item <= PALADINS_BADGE_3) {
					player.setStopPacket(true);
					player.getActionSender().sendMessage("You throw the " + (item == UNICORN_HORN ? "unicorn horn" : "coat of arms") + " into the flames...");
					player.getInventory().removeItem(new Item(item));
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							player.setStopPacket(false);
							player.getQuestVars().wellItemsDestroyed[item - UNICORN_HORN] = true;
							player.getActionSender().sendMessage("You hear a howl in the distance.");
							boolean canContinue = true;
							for (boolean b : player.getQuestVars().wellItemsDestroyed)
								if (!b) {
									canContinue = false;
								}
							if (canContinue) {
								player.getActionSender().sendMessage("You hear a click from nearby...");
								player.getActionSender().sendMessage("It sounds like it came from the skull above the door.");
								if(player.getQuestStage(getQuestID()) == UNICORN_KILLED)
									player.setQuestStage(getQuestID(), IBANS_LAIR_OPEN);
							}
							
						}
					}, 3);
					return true;
				}
			return false;
			case 3216: //cage mud
				if(item == 952 && player.inUndergroundPass()) { //spade
					PassObjectHandling.handleDigMud(player);
					return true;
				}
				return false;
			case 3294: //furnace
				if(item >= ORB_OF_LIGHT && item <= ORB_OF_LIGHT_4) {
					player.setStopPacket(true);
					player.getActionSender().sendMessage("You throw the glowing orb into the furnace...");
					player.getActionSender().sendMessage("Its light quickly dims and then dies.");
					player.getInventory().removeItem(new Item(item));
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							player.getActionSender().sendMessage("You feel a cold shudder run down your spine.");
							player.getQuestVars().wellItemsDestroyed[item - ORB_OF_LIGHT] = true;
							player.setStopPacket(false);
						}
					}, 3);
					return true;
				}
			return false;
			case 3230: //flat rock trap
				if(item == 960) { //plank
					PassTrapHandling.handleDisarmTrap(player, 3231, 827, null);
					return true;
				}
			return false;
			case 2275: //rock swing
			case 2276:
				if(item == 954) { //rope
					PassObjectHandling.handleRopeSwing(player, object);
					return true;
				}
			return false;
		}
		return false;
	}

	public boolean doItemOnNpc(Player player, int itemId, Npc npc) {
		if(itemId == PIECE_OF_RAILING && npc.getNpcId() == BOULDER) {
			PassObjectHandling.handlePushBoulder(player, npc);
			return true;
		}
		return false;
	}

	public boolean doNpcClicking(Player player, Npc npc) {
		int id = npc.getNpcId();
		switch(id) {
			case BOULDER:
				player.getDialogue().sendPlayerChat("It's too heavy to move with just my hands.", DISTRESSED);
				player.getDialogue().endDialogue();
				return true;
		}
		return false;
	}
	
	public boolean doObjectClicking(final Player player, int object, int x, int y) {
		if(player.stopPlayerPacket()) {
			return false;
		}
		switch (object) {
			case 3305:
				player.getActionSender().sendMessage("The well appears to have flames at the bottom, instead of water...");
				return true;
			case 3307:
				if(player.inUndergroundPass()) {
					Ladders.climbLadder(player, new Position(2418, 9674, 0));
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}
						@Override
						public void stop() {
							PassTrapHandling.startTrapCycle(player, 0);
						}
					}, 5);
					return true;
				}
			return false;
			case 3220:
			case 3221:
				if (player.inUndergroundPass()) {
					if (player.getPosition().getX() > 2180) {
						if (player.getQuestStage(this.getQuestID()) >= IBANS_LAIR_OPEN) {
							for (int i = 0; i < 4; i++)
								player.getQuestVars().wellItemsDestroyed[i] = false;
							player.fadeTeleport(new Position(2173, 4725, 1));
						} else {
							player.getActionSender().sendMessage("The doors won't budge. You hear a faint cackling.");
						}
						return true;
					} else {
						player.fadeTeleport(new Position(2370, 9719, 0));
						CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
							@Override
							public void execute(CycleEventContainer b) {
								b.stop();
							}
							@Override
							public void stop() {
								PassTrapHandling.startTrapCycle(player, 2);
							}
						}, 6);
						return true;
					}
				}
			return false;
			case 3236: //pipe wrong end
				if(player.inUndergroundPass()) {
					player.getDialogue().sendPlayerChat("Hm, it looks like there is a grill on the", "other side of this pipe. I won't be able to", "remove it from the inside.");
					player.getDialogue().endDialogue();
					return true;	
				}
			return false;
			case 3235:
			case 3237: //pipes
				if(player.inUndergroundPass()) {
					PassObjectHandling.handlePipeCrawl(player, object, x, y);
					return true;
				}
			return false;
			case 3360:
				if(x == 2417 && y == 9658) {
					player.setStopPacket(true);
					player.getActionSender().sendMessage("You search the crate...");
					player.getUpdateFlags().sendAnimation(832);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							if(!player.getQuestVars().receivedCrateFood) {
								player.getQuestVars().receivedCrateFood = true;
								player.getActionSender().sendMessage("You find some food.");
								player.getInventory().addItemOrDrop(new Item(2327, 2)); //meat pie
								player.getInventory().addItemOrDrop(new Item(329, 2)); //salmon
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
			case 3218:
			case 3219:
				if(player.inUndergroundPass()) {
					if(player.getPosition().getY() > 9662) {
						player.fadeTeleport(player.getQuestStage(44) >= UNICORN_KILLED ? new Position(2375, 9609, 0) : new Position(2400, 9609, 0));
					} else {
						player.fadeTeleport(new Position(2371, 9667, 0));
						
					}
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}
						@Override
						public void stop() {
							if(player.getPosition().getY() > 9662) {
								player.getActionSender().sendMapState(0);
								PassTrapHandling.startTrapCycle(player, 2);
							} else {
								player.getActionSender().sendMapState(2);
							}
						}
					}, player.getPosition().getY() > 9662 ? 4 : 6);
					return true;
				}
			return false;
			case 3308: //broken cage
				if(player.inUndergroundPass()) {
					if(!player.getInventory().playerHasItem(UNICORN_HORN) && player.getQuestStage(this.getQuestID()) == UNICORN_KILLED)
						Dialogues.startDialogue(player, 33080);
					else
						player.getDialogue().sendPlayerChat("There's nothing here but a dead unicorn...", "Poor guy...", SAD);
					return true;
				}
			return false;
			case 3217: //"cave" back to cage mud
				if(player.inUndergroundPass()) {
					player.getActionSender().sendMessage("You push your way through the tunnel...");
					player.fadeTeleport(new Position(2393, 9651, 0));
					return true;
				}
				return false;
			case 3268:
			case 3266: //cell doors
				if(player.inUndergroundPass()) {
					PassObjectHandling.pickCageLock(player, x, y);
					return true;
				}
			return false;
			case 3264: //well
				if(player.inUndergroundPass()) {
					player.getActionSender().sendMessage("You feel the grip of icy hands all around you...");
					boolean canContinue = true;
					if(player.getQuestStage(this.getQuestID()) < CAN_USE_WELL)
						for(boolean b : player.getQuestVars().wellItemsDestroyed)
							if(!b)
								canContinue = false;
					if(canContinue) {
						for(int i = 0; i < 4; i++)
							player.getQuestVars().wellItemsDestroyed[i] = false;
						Ladders.climbLadder(player, new Position(2423, 9660, 0));
						if(player.getQuestStage(this.getQuestID()) == ENTER_CAVES)
							player.setQuestStage(this.getQuestID(), CAN_USE_WELL);
					}
					final boolean teleported = canContinue;
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							player.setStopPacket(false);
							if(teleported) {
								player.getActionSender().sendMessage("...slowly dragging you further down into the caverns.");
							} else {
								player.getActionSender().sendMessage("...the hands try to strangle you!");
								player.hit(10, HitType.NORMAL);
							}
						}
					}, 3);
					return true;
				}
				return false;
			case 3234:
				if(player.inUndergroundPass()) {
					Dialogues.startDialogue(player, 32340);
					return true;
				}
			return false;
			case 3339:
				if(x == 2382 && y == 9668) {
					Dialogues.startDialogue(player, ORB_OF_LIGHT + 10000);
					return true;
				}
			return false;
			case 3230:
				PassTrapHandling.handleDisarmTrap(player, object, 2244, null);
				return true;
			case 2274: //rope swing
				PassObjectHandling.handleRopeSwing(player, object);
				return true;
			case 3337:
				if(x == 2466 && y == 9672) {
					PassObjectHandling.handlePortcullis(player);
					return true;
				}
				return false;
			case 3241: //lever
				if (x == 2436 && y == 9716) {
					PassObjectHandling.returnOverBridge(player);
					return true;
				}
				return false;
			case GUIDE_ROPE:
				if (player.getPosition().getY() < 9718) {
					player.getActionSender().sendMessage("You can't get a clear shot from here.");
				} else {
					PassObjectHandling.shootBridgeRope(player, x, y);
				}
				return true;
			case 3295:
			case 3296:
			case 3297:
			case 3298:
				PassObjectHandling.readTablets(player, object);
				return true;
			case PASS_ENTRANCE:
				if(x == 2433 && y == 3313) {
					if (UNDERGROUND_PASS_ENABLED && player.getQuestStage(this.getQuestID()) >= ENTER_CAVES) {
						player.getUpdateFlags().sendAnimation(844);
						player.fadeTeleport(PASS_ENTRANCE_POS);
						startIbanWhispers(player);
					} else {
						player.getDialogue().setLastNpcTalk(KOFTIK);
						player.getDialogue().sendNpcChat("Hey, get away from there! You'll kill yourself", "in those caves!", ANGRY_1);
						player.getDialogue().endDialogue();
					}
					return true;
				}
				return false;
			case PASS_EXIT:
				if(x == 2496 && y == 9713) {
					player.getUpdateFlags().sendAnimation(844);
					player.fadeTeleport(PASS_EXIT_POS);
					return true;
				}
				return false;
		}
		return false;
	}

	public boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
		if(player.stopPlayerPacket()) {
			return false;
		}
		switch (object) {
			case 3267:
				if (player.inUndergroundPass()) {
					player.setStopPacket(true);
					player.getActionSender().sendMessage("You search the cage...");
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							player.setStopPacket(false);
							if (!player.getInventory().ownsItem(PIECE_OF_RAILING)) {
								player.getActionSender().sendMessage("You find a loose railing on the floor.");
								player.getInventory().addItem(new Item(PIECE_OF_RAILING));
							} else {
								player.getActionSender().sendMessage("You don't find anything interesting.");
							}
						}
					}, 3);
					return true;
				}
				break;
		}
		return false;
	}

	public void handleDeath(final Player player, final Npc died) {
		switch(died.getNpcId()) {
			case SIR_JERRO:
			case SIR_CARL:
			case SIR_HARRY:
				if (player.getQuestStage(this.getQuestID()) < IBANS_LAIR_OPEN) {
					int itemId = died.getNpcId() == SIR_JERRO ? PALADINS_BADGE : died.getNpcId() == SIR_CARL ? PALADINS_BADGE_2 : PALADINS_BADGE_3;
					GroundItemManager.getManager().dropItem(new GroundItem(new Item(itemId), player, player, died.getPosition()));
				}
				break;
		}
	}

	public boolean sendDialogue(final Player player, final int id, int chatId, int optionId, int npcChatId) {
		DialogueManager d = player.getDialogue();
		switch (id) { //Npc ID
			case SIR_CARL:
			case SIR_HARRY:
				switch (d.getChatId()) {
					case 1:
						d.sendPlayerChat("Hello paladin.");
						return true;
					case 2:
						d.sendNpcChat("Hrumph. Talk to Jerro if you need anything.");
						d.endDialogue();
						return true;
				}
			return false;
			case SIR_JERRO:
				switch (d.getChatId()) {
					case 1:
						d.sendPlayerChat("Hello paladin.");
						return true;
					case 2:
						d.sendNpcChat("Traveller, what are you doing in this most unholy", "place?");
						return true;
					case 3:
						d.sendPlayerChat("I'm looking for safe route through the caverns, under", "order of King Lathas.");
						return true;
					case 4:
						if(!player.getQuestVars().receivedPaladinFood) {
							d.sendNpcChat("You've done well to get this far traveller, here, eat...");
						} else {
							d.sendNpcChat("You've done well to get this far traveller.");
							d.endDialogue();
						}
						return true;
					case 5:
						d.sendPlayerChat("Great, thanks a lot.");
						d.endDialogue();
						player.getActionSender().sendMessage("The Paladin gives you some food.");
						player.getQuestVars().receivedPaladinFood = true;
						player.getInventory().addItemOrDrop(new Item(2327)); //meat pie?
						player.getInventory().addItemOrDrop(new Item(2003)); //stew
						player.getInventory().addItem(new Item(2309, 2)); //bread
						return true;
						
				}
			return false;
			case 33080:
				switch (d.getChatId()) {
					case 1:
						d.sendPlayerChat("All that remains is a damaged horn.", DISTRESSED);
						return true;
					case 2:
						d.endDialogue();
						player.getActionSender().removeInterfaces();
						player.getActionSender().sendMessage("The unicorn was killed by the boulder.");
						if(!player.getInventory().playerHasItem(UNICORN_HORN))
							player.getInventory().addItem(new Item(UNICORN_HORN));
						return true;
				}
			return false;
			case 32340:
				switch (d.getChatId()) {
					case 1:
						d.sendStatement("The markings appear to be holes in the wall... It's a trap!");
						return true;
					case 2:
						d.sendOption("Try and disarm the trap.", "Leave it alone.");
						return true;
					case 3:
						player.getActionSender().removeInterfaces();
						d.endDialogue();
						if (optionId == 1) {
							PassTrapHandling.handleDisarmTrap(player, 3234, 2246, null);
						}
						return true;	
				}
			return false;
			case ORB_OF_LIGHT + 10000:
				switch (d.getChatId()) {
					case 1:
						d.sendStatement("The rock appears to move slightly as you touch it... It's a trap!");
						return true;
					case 2:
						d.sendOption("Try and disarm the trap.", "Leave it alone.");
						return true;
					case 3:
						player.getActionSender().removeInterfaces();
						d.endDialogue();
						if (optionId == 1) {
							PassTrapHandling.handleDisarmTrap(player, 3361, 2244, null);
						}
						return true;	
				}
			return false;
			case KOFTIK_3:
				switch (d.getChatId()) {
					case 1:
						d.sendPlayerChat("Hello Koftik.");
						return true;
					case 2:
						d.sendNpcChat("Do you hear them? The voices tell me things.");
						return true;
					case 3:
						d.sendPlayerChat("Are you okay?");
						return true;
					case 4:
						d.sendNpcChat("The path of the righteous man is beset on all sides by", "the iniquities of the selfish and the tyranny of evil men.");
						return true;
					case 5:
						d.sendPlayerChat("Tyranny of the righteous? What?");
						return true;
					case 6:
						d.sendNpcChat("So many paths to choose... here we must all take our", "own path.");
						d.endDialogue();
						return true;
				}
			return false;
			case KOFTIK_2:
				switch (d.getChatId()) {
					case 1:
						d.sendPlayerChat("Koftik, how can we cross the bridge?");
						return true;
					case 2:
						d.sendNpcChat("I'm not sure, seems as if others were here before us", "though...");
						return true;
					case 3:
						d.sendNpcChat("I found this cloth amongst the charred remains", "of some arrows.");
						player.getInventory().addItem(new Item(DAMP_CLOTH));
						return true;
					case 4:
						d.sendPlayerChat("Charred arrows? They must have been trying to burn", "something. Or someone!");
						return true;
					case 5:
						d.sendPlayerChat("Interesting... we better keep our eyes open.");
						return true;
					case 6:
						d.sendNpcChat("I have also found the remains of a book...", SAD);
						return true;
					case 7:
						d.sendPlayerChat("What does it say?");
						return true;
					case 8:
						d.sendNpcChat("It seems to be written by the adventurer Randas. It", "reads...");
						return true;
					case 9:
						player.getActionSender().removeInterfaces();
						player.getActionSender().sendMessage("Koftik shows you the diary.");
						d.dontCloseInterface();
						player.getBookHandler().initBook(diaryOfRandasStrings, "The Diary of Randas");
						return true;
				}
				return false;
			case KOFTIK:
				switch (player.getQuestStage(this.getQuestID())) {
					case QUEST_STARTED:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello there, are you the King's scout?");
								return true;
							case 2:
								d.sendNpcChat("That I am, brave adventurer. King Lathas informed me", "that you need to cross these mountains.");
								return true;
							case 3:
								d.sendNpcChat("I'm afraid you'll have to go through the ancient", "underground pass...");
								return true;
							case 4:
								d.sendPlayerChat("That's ok, I've travelled through many a cave", "in my time.");
								return true;
							case 5:
								d.sendNpcChat("These caves are different... They're filled with the spirit", "of Zamorak!");
								return true;
							case 6:
								d.sendNpcChat("You can feel it as you wind your way round the", "stalagmites... an icy chill that penetrates the very fabric", "of your being...");
								return true;
							case 7:
								d.sendNpcChat("Not so many travellers come down here these days,", "...but there are some who are still foolhardy enough.");
								return true;
							case 8:
								d.sendOption("I'll take my chances.", "Tell me more...");
								return true;
							case 9:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if(optionId == 1)
									d.setNextChatId(15);
								return true;
								
							case 10:
								d.sendNpcChat("I remember seeing one such warrior. Going by the", "name of Randas... ...he stood tall and proud like an", "Elven King...");
								return true;
							case 11:
								d.sendNpcChat("...That same pride made him vulnerable to Zamorak's", "calls. Randas' worthy desire to be a great and mighty", "warrior also made him corruptible to Zamorak's promises", "of glory.");
								return true;
							case 12:
								d.sendNpcChat("...Zamorak showed him a way to achieve his goals by", "appealing to that most base and dark nature ...that", "resides in all of us.");
								return true;
							case 13:
								d.sendPlayerChat("What happened to him?", DISTRESSED);
								return true;
							case 14:
								d.sendNpcChat("No one knows...", SAD);
								return true;
							case 15:
								d.sendNpcChat("If you're willing, you'll need to meet me by", "the bridge just inside the caves. Be careful.");
								d.endDialogue();
								player.setQuestStage(this.getQuestID(), ENTER_CAVES);
								return true;	
						}
					return false;
				}
			return false;
			case KING_LATHAS:
				switch (player.getQuestStage(this.getQuestID())) {
					case QUEST_STARTED:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello King Lathas.");
								return true;
							case 2:
								d.sendNpcChat("Go meet my main tracker, Koftik. He will help you. He", "waits for you at the west side of West Ardougne.");
								return true;
							case 3:
								d.sendNpcChat("We must find a way through these caverns if we are to", "stop my brother Tyras.");
								return true;
							case 4:
								d.sendPlayerChat("I'll do my best Lathas.");
								return true;
							case 5:
								d.sendNpcChat("A warning traveller, the underground pass is lethal. We", "lost many men exploring those caverns. Go prepared", "with food and armor or you won't last long.");
								d.endDialogue();
								return true;		
						}
					return false;
					case 0:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello King Lathas.");
								return true;
							case 2:
								if(QuestHandler.questCompleted(player, 40) && UNDERGROUND_PASS_ENABLED) {
									d.sendNpcChat("Adventurer, thank Saradomin for your arrival!", HAPPY);
								} else {
									d.sendNpcChat("Shoo, peasant.", ANGRY_1);
									d.endDialogue();
								}
								return true;
							case 3:
								d.sendPlayerChat("Have your scouts found a way through the mountains?");
								return true;
							case 4:
								d.sendNpcChat("Not quite, we found a path to where we expected", "to find the Well of Voyage, an ancient portal to", "the far west.");
								return true;
							case 5:
								d.sendNpcChat("However during recent times a cluster of cultists have", "settled there, run by a madman named Iban.");
								return true;
							case 6:
								d.sendPlayerChat("Iban?");
								return true;
							case 7:
								d.sendNpcChat("A crazy loon who claims to be the son of Zamorak.");
								return true;
							case 8:
								d.sendNpcChat("Go meet my main tracker, Koftik. He will help you. He", "waits for you at the west side of West Ardougne.");
								return true;
							case 9:
								d.sendNpcChat("We must find a way through these caverns if we are to", "stop my brother Tyras.");
								return true;
							case 10:
								d.sendPlayerChat("I'll do my best Lathas.");
								return true;
							case 11:
								d.sendNpcChat("A warning traveller, the underground pass is lethal. We", "lost many men exploring those caverns. Go prepared", "with food and armor or you won't last long.");
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
