package com.rs2.model.content.quests;

import com.rs2.model.Graphic;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.Following;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.dialogue.DialogueManager;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.EVIL;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.content.randomevents.EventsConstants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.container.inventory.Inventory;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.net.ActionSender;

public class InSearchOfTheMyreque implements Quest {

	public static final int questIndex = 11907; //Used in player's quest log interface, id is in Player.java //Change
	//Quest stages
	public static final int QUEST_STARTED = 1;
	public static final int POUCH_FOR_RIDE = 2;
	public static final int BOARD_BOAT = 3;
	public static final int DOORS_UNLOCKED = 4;
	public static final int TALK_TO_MYREQUE = 5;
	public static final int TALKED_TO_MYREQUE = 6;
	public static final int FIGHT_WON = 7;
	public static final int QUEST_COMPLETE = 15;

	//Items
	public static final int STEEL_DAGGER = 1207;
	public static final int STEEL_SWORD = 1281;
	public static final int STEEL_LONGSWORD = 1295;
	public static final int STEEL_MACE = 1424;
	public static final int STEEL_WARHAMMER = 1339;
	public static final int PLANK = 960;
	public static final int STEEL_NAILS = 1539;
	public static final int HAMMER = 2347;

	//Positions
	public static final Position BOATMAN_POS = new Position(3521, 3285, 0);
	public static final Position HIDEOUT_ENTRANCE = new Position(3505, 9832, 1);
	public static final Position HIDEOUT_EXIT = new Position(3491, 9824, 0);
	public static final Position UNDERGROUND_ENTRANCE = new Position(3500, 9811, 0);
	public static final Position CAVE_1_EXIT = new Position(3481, 9824, 0); //3467 9820
	public static final Position CAVE_1_ENTRANCE = new Position(3466, 9820, 0); //3480 9824
	public static final Position CAVE_2_EXIT = new Position(3488, 9815, 0); //3476 9806
	public static final Position CAVE_2_ENTRANCE = new Position(3475, 9806, 0); //3488 9814
	public static final Position CAVE_3_EXIT = new Position(3492, 9809, 0); //3478 9799
	public static final Position CAVE_3_ENTRANCE = new Position(3477, 9799, 0); //3492 9808
	public static final Position TRAPDOOR_ENTRANCE = new Position(3494, 3465, 0);

	//Interfaces
	public static final int SWAMP_BOAT_MAP = 11898;
	public static final int BOAT_CHILD_ID = 11901;

	//Npcs
	public static final int CYREG_PADDLEHORN = 1567;
	public static final int CURPILE_FYOD = 1568;
	public static final int VELIAF_HURTZ = 1569;
	public static final int SANI_PILIU = 1570;
	public static final int HAROLD_EVANS = 1571;
	public static final int RADIGAD_PONFIT = 1572;
	public static final int POLMAFI_FERDYGRIS = 1573;
	public static final int IVAN_STROM = 1574;
	public static final int SKELETON_HELLHOUND = 1575;
	public static final int STRANGER = 1576;
	public static final int VANSTROM_CLAUSE = 1577;
	public static final int MIST = 1578;
	public static final int VANSTROM_CLAUSE_1 = 1579;
	public static final int VANSTROM_CLAUSE_2 = 1580;
	public static final int VANSTROM_CLAUSE_3 = 1581;

	//Objects
	public static final int CAVE_ENTRANCE = 5046;
	public static final int WOODEN_DOORS_1 = 5060;
	public static final int WOODEN_DOORS_2 = 5061;
	public static final int ROPE_BRIDGE = 5002;
	public static final int BROKEN_ROPE_BRIDGE = 5003;
	public static final int TREE = 5005;
	public static final int SWAMP_BOAT = 6969;
	public static final int SWAMP_BOAT_ABANDONED = 6970;
	public static final int WALL = 5052;
	public static final int LADDER = 5054;
	public static final int STALAGMITE = 5050;
	public static final int TRAPDOOR = 5055;

	public int dialogueStage = 0;

	private int reward[][] = { //{itemId, count},
	};

	private int expReward[][] = { //{skillId, exp},
		{Skill.ATTACK, 600},
		{Skill.STRENGTH, 600},
		{Skill.DEFENCE, 600},
		{Skill.HITPOINTS, 600},
		{Skill.CRAFTING, 600}
	};

	private static final int questPointReward = 2; //Change

	public int getQuestID() { //Change
		return 38;
	}

	public String getQuestName() { //Change
		return "In Search of the Myreque";
	}

	public String getQuestSaveName() { //Change
		return "searchofmyreque";
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
		player.getActionSender().sendItemOnInterface(12145, 250, STEEL_SWORD); //zoom, then itemId
		player.getActionSender().sendString("You have completed " + getQuestName() + "!", 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("2 Quest Points", 12150);
		player.getActionSender().sendString("Quick route to Mort'ton", 12151);
		player.getActionSender().sendString("1350 XP in each of: Attack,", 12152);
		player.getActionSender().sendString("Defence, Strength, Hitpoints,", 12153);
		player.getActionSender().sendString("and Crafting", 12154);
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
			case POUCH_FOR_RIDE:
				lastIndex = 4;
				break;
			case BOARD_BOAT:
				lastIndex = 9;
				break;
			case DOORS_UNLOCKED:
				lastIndex = 14;
				break;
			case TALK_TO_MYREQUE:
				lastIndex = 17;
				break;
			case TALKED_TO_MYREQUE:
				lastIndex = 22;
				break;
			case FIGHT_WON:
			case QUEST_COMPLETE:
				lastIndex = 26;
				break;
		}
		lastIndex++;
		ActionSender a = player.getActionSender();
		a.sendQuestLogString("Talk to Vanstrom Clause in the Canifis bar to begin.", 1, this.getQuestID(), 0);
		a.sendQuestLogString("Vanstrom Clause asked me to take some weapons to", 3, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("a group called the 'Myreque'.", 4, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("The boatman in Mort'ton agreed to let me take his", 6, this.getQuestID(), BOARD_BOAT);
		a.sendQuestLogString("boat back through Mort Myre after giving him 3", 7, this.getQuestID(), BOARD_BOAT);
		a.sendQuestLogString("wooden planks. He said to go north and look for", 8, this.getQuestID(), BOARD_BOAT);
		a.sendQuestLogString("an 'unusual tree'.", 9, this.getQuestID(), BOARD_BOAT);
		a.sendQuestLogString("I found a man named Curpile Fyod outside the", 11, this.getQuestID(), DOORS_UNLOCKED);
		a.sendQuestLogString("strange tree the boatman mentioned. I answered", 12, this.getQuestID(), DOORS_UNLOCKED);
		a.sendQuestLogString("his questions and he unlocked the doors to a", 13, this.getQuestID(), DOORS_UNLOCKED);
		a.sendQuestLogString("mysterious underground entrance behind the tree.", 14, this.getQuestID(), DOORS_UNLOCKED);
		a.sendQuestLogString("I found the Myreque! Their leader Veliaf", 16, this.getQuestID(), TALK_TO_MYREQUE);
		a.sendQuestLogString("Hurtz welcomed me to their underground hideout.", 17, this.getQuestID(), TALK_TO_MYREQUE);
		a.sendQuestLogString("I met all the Myreque and gave them the weapons", 19, this.getQuestID(), TALKED_TO_MYREQUE);
		a.sendQuestLogString("I brought for them. Turns out that Vanstrom is", 20, this.getQuestID(), TALKED_TO_MYREQUE);
		a.sendQuestLogString("actually a nasty vampire and is trying to kill", 21, this.getQuestID(), TALKED_TO_MYREQUE);
		a.sendQuestLogString("the brave Myreque!", 22, this.getQuestID(), TALKED_TO_MYREQUE);
		a.sendQuestLogString("Well, I killed the nasty thing that Vanstrom", 24, this.getQuestID(), FIGHT_WON);
		a.sendQuestLogString("summoned, and in turn saved the Myreque from any", 25, this.getQuestID(), FIGHT_WON);
		a.sendQuestLogString("further harm.", 26, this.getQuestID(), FIGHT_WON);
		switch (questStage) {
			default:
				break;
			case 0:
				a.sendQuestLogString("Talk to @dre@Vanstrom Clause @bla@in the @dre@Canifis Bar @bla@to begin.", 1);
				a.sendQuestLogString("@dre@Requirements:", 3);
				a.sendQuestLogString("" + (QuestHandler.questCompleted(player, 37) ? "@str@" : "@dbl@") + "-Nature Spirit.", 4);
				a.sendQuestLogString("@dbl@-Ability to defeat a level 97 foe.", 5);
				a.sendQuestLogString("" + (player.getSkill().getLevel()[Skill.AGILITY] >= 25 ? "@str@" : "@dbl@") + "-25 Agility.", 6);
				break;
			case QUEST_STARTED:
				a.sendQuestLogString("" + (player.getInventory().playerHasItem(STEEL_LONGSWORD) ? "@str@" : "@dbl@") + "1 x Steel longsword", lastIndex + 1);
				a.sendQuestLogString("" + (player.getInventory().playerHasItem(STEEL_SWORD) ? "@str@" : "@dbl@") + "2 x Steel shortsword", lastIndex + 2);
				a.sendQuestLogString("" + (player.getInventory().playerHasItem(STEEL_DAGGER) ? "@str@" : "@dbl@") + "1 x Steel dagger", lastIndex + 3);
				a.sendQuestLogString("" + (player.getInventory().playerHasItem(STEEL_MACE) ? "@str@" : "@dbl@") + "1 x Steel mace", lastIndex + 4);
				a.sendQuestLogString("" + (player.getInventory().playerHasItem(STEEL_WARHAMMER) ? "@str@" : "@dbl@") + "1 x Steel warhammer", lastIndex + 5);
				if (allWeapons(player)) {
					a.sendQuestLogString("I have all the weapons Vanstrom asked me to get.", lastIndex + 6);
				}
				a.sendQuestLogString("Vanstrom said the boatman in Mort'ton should be", lastIndex + 7);
				a.sendQuestLogString("able to help me find the 'Myreque'.", lastIndex + 8);
				break;
			case POUCH_FOR_RIDE:
				a.sendQuestLogString("The boatman in Mort'ton agreed to let me take his", lastIndex + 1);
				a.sendQuestLogString("boat back through Mort Myre. His condition is that", lastIndex + 2);
				a.sendQuestLogString("I need defense against the Ghasts. I should fill my", lastIndex + 3);
				a.sendQuestLogString("Druid Pouch to convince him, 5 charges should do.", lastIndex + 4);
				break;
			case TALK_TO_MYREQUE:
				a.sendQuestLogString("I should introduce myself to the other members.", lastIndex + 1);
				break;
			case TALKED_TO_MYREQUE:
				a.sendQuestLogString("I need to help defeat the beast Vanstrom sent!", lastIndex + 1);
				break;
			case FIGHT_WON:
				a.sendQuestLogString("I should talk to Veliaf and then see what Vanstrom", lastIndex + 1);
				a.sendQuestLogString("has to say for himself.", lastIndex + 2);
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
		String prefix = "";
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
		player.getActionSender().sendString(getQuestName(), 8144);
	}

	public void dialogue(Player player, Npc npc) {
		//Don't even need this anymore really
	}

	public int getDialogueStage(Player player) {
		return dialogueStage;
	}

	public void setDialogueStage(int in) {
		dialogueStage = in;
	}

	public static boolean allWeapons(Player player) {
		Inventory i = player.getInventory();
		return i.playerHasItem(STEEL_LONGSWORD) && i.playerHasItem(STEEL_SWORD, 2) && i.playerHasItem(STEEL_DAGGER) && i.playerHasItem(STEEL_MACE) && i.playerHasItem(STEEL_WARHAMMER);
	}

	public static int bridgeIndexForPos(int y) {
		switch (y) {
			case 3428:
				return 1;
			case 3429:
				return 2;
			case 3430:
				return 3;
		}
		return 0;
	}

	public static void spawnBridgeObjects(int z, boolean destroyOnly) {

	}
	
	public static void sailSwampBoat(final Player player, final boolean toMortton) {
		player.setStopPacket(true);
		player.setInCutscene(true);
		player.getActionSender().sendInterface(SWAMP_BOAT_MAP);
		player.getActionSender().sendInterfaceAnimation(BOAT_CHILD_ID, toMortton ? 1492 : 1491);
		player.getActionSender().sendMapState(2);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		    @Override
		    public void execute(CycleEventContainer b) {
			b.stop();
		    }

		    @Override
		    public void stop() {
			player.teleport(toMortton ? BOATMAN_POS : new Position(3499, 3380, 0));
			player.getActionSender().sendInterfaceAnimation(BOAT_CHILD_ID, -1);
			player.getActionSender().sendMapState(0);
			player.setStopPacket(false);
			player.setInCutscene(false);
			if(toMortton) {
				player.getDialogue().sendStatement("You arrive in Mort'ton.");
				player.getDialogue().endDialogue();
			}
		    }
		}, 19);
	}

	public static void startEncounter(final Player player) {
		player.getActionSender().sendMapState(2);
		player.getInventory().removeItem(new Item(STEEL_DAGGER));
		player.getInventory().removeItem(new Item(STEEL_LONGSWORD));
		player.getInventory().removeItem(new Item(STEEL_MACE));
		player.getInventory().removeItem(new Item(STEEL_WARHAMMER));
		player.getInventory().removeItem(new Item(STEEL_SWORD, 2));
		final DialogueManager d = player.getDialogue();
		Npc SANI = null;
		Npc HAROLD = null;
		for (Npc n : player.getNpcs()) {
			if (n != null && n.getPosition().getZ() == player.getPosition().getZ()) {
				n.getUpdateFlags().setFace(HIDEOUT_ENTRANCE);
				n.getUpdateFlags().setFaceToDirection(true);
				if(n.getNpcId() == SANI_PILIU) {
					SANI = n;
				} else if(n.getNpcId() == HAROLD_EVANS) {
					HAROLD = n;
				}
			}
		}
		final Npc vanstrom = new Npc(MIST);
		final Npc sani = SANI;
		final Npc harold = HAROLD;
		player.setStopPacket(true);
		player.setInCutscene(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int count = 1;

			@Override
			public void execute(CycleEventContainer b) {
				switch (count) {
					case 1:
						d.setLastNpcTalk(VELIAF_HURTZ);
						d.sendNpcChat("Hey, what's that mist coming in through the door!", "TEAM WE HABE A VAMPIRE IN THE ROOM!", DISTRESSED);
						break;
					case 2:
						player.getActionSender().removeInterfaces();
						player.getActionSender().sendMessage("You see a shifting mist enter the room!");
						NpcLoader.spawnPlayerOwnedSpecificLocationNpc(player, vanstrom, HIDEOUT_ENTRANCE, false, null);
						vanstrom.walkTo(player.getPosition().clone().modifyY(player.getPosition().getY() - 2), true);
						vanstrom.setFollowingEntity(player);
						break;
					case 3:
						vanstrom.sendTransform(VANSTROM_CLAUSE_1, 100);
						d.setLastNpcTalk(VANSTROM_CLAUSE_1);
						d.sendNpcChat("Ha... ha... ha... you took me straight to them!", EVIL);
						break;
					case 4:
						d.setLastNpcTalk(VANSTROM_CLAUSE_1);
						d.sendNpcChat("The little dears are going to wonder which side you're", "on!", EVIL);
						sani.walkTo(new Position(3508, 9837, 1), true);
						harold.walkTo(new Position(3504, 9837, 1), true);
						break;
					case 5:
						d.setLastNpcTalk(SANI_PILIU);
						d.sendNpcChat("It's Vanstrom... we're dead...", SAD);
						sani.walkTo(new Position(3508, 9837, 1), true);
						harold.walkTo(new Position(3504, 9837, 1), true);
						break;
					case 6:
						d.setLastNpcTalk(VANSTROM_CLAUSE_1);
						d.sendNpcChat("Quite right! Sorry Harold, you die as well!", EVIL);
						CombatManager.startDeath(sani);
						CombatManager.startDeath(harold);
						break;
					case 7:
						vanstrom.sendTransform(VANSTROM_CLAUSE_2, 100);
						vanstrom.getUpdateFlags().sendAnimation(1499);
						d.setLastNpcTalk(VANSTROM_CLAUSE_2);
						d.sendNpcChat("And.. now I'm going to finish the rest of them off...", LAUGHING);
						break;
					case 8:
						vanstrom.getUpdateFlags().sendAnimation(1500);
						vanstrom.sendTransform(VANSTROM_CLAUSE_3, 999999);
						d.setLastNpcTalk(VANSTROM_CLAUSE_2);
						d.sendNpcChat("With my little pet!", "Ha ha ha ha ha!", LAUGHING);
						break;
					case 9:
						player.getActionSender().removeInterfaces();
						vanstrom.walkTo(HIDEOUT_ENTRANCE.clone().modifyZ(1), true);
						break;
					case 10:
						NpcLoader.destroyNpc(vanstrom);
						player.teleport(player.getPosition().clone().modifyZ(0));
						player.getActionSender().sendStillGraphic(new Graphic(EventsConstants.RANDOM_EVENT_GRAPHIC, 0), new Position(3509, 9835, 0));
						NpcLoader.spawnPlayerOwnedAttackNpc(player, new Npc(SKELETON_HELLHOUND), new Position(3509, 9835, 0), true, null);
						break;
				}
				count++;
				if (count == 11) {
					b.stop();
				}
			}

			@Override
			public void stop() {
				player.getActionSender().sendMapState(0);
				player.setStopPacket(false);
			}
		}, 8);
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
	
	public static void handleDeath(final Player player, final Npc died) {
		if(died.getNpcId() == SKELETON_HELLHOUND && player.getQuestStage(38) == TALKED_TO_MYREQUE) {
			player.setQuestStage(38, FIGHT_WON);
		}
	}

	public boolean doObjectClicking(final Player player, int object, final int x, final int y) {
		switch (object) {
			case 6435:
				if(x == 3494 && y == 3464) {
					player.fadeTeleport(new Position(3477, 9845, 0));
					return true;
				}
			case LADDER:
				Ladders.climbLadder(player, TRAPDOOR_ENTRANCE);
				player.getActionSender().sendMessage("You emerge to the south of the 'Hair of the Dog' tavern in Canifis.", true);
				return true;
			case 1752:
				if(x == 3483 && y == 9841) {
					player.getActionSender().sendMessage("This ladder is broken.", true);
					return true;
				}
			case WALL:
				if(player.getQuestStage(38) >= FIGHT_WON) {
					player.getActionSender().sendMessage("You walk through.", true);
					player.getActionSender().walkTo(0, player.getPosition().getY() < 9837 ? 1 : -1, true);
					player.getActionSender().walkThroughDoor(object, x, y, 0);
				} else {
					player.getActionSender().sendMessage("There's nothing interesting here, it's just a wall.");
				}
			return true;
			case STALAGMITE:
				player.getDialogue().sendPlayerChat("Hmm, looks like I can squeeze past this.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			case CAVE_ENTRANCE:
				if (x == 3467 && y == 9820) {
					player.fadeTeleport(CAVE_1_EXIT);
				} else if (x == 3480 && y == 9824) {
					player.fadeTeleport(CAVE_1_ENTRANCE);
				} else if (x == 3476 && y == 9806) {
					player.fadeTeleport(CAVE_2_EXIT);
				} else if (x == 3488 && y == 9814) {
					player.fadeTeleport(CAVE_2_ENTRANCE);
				} else if (x == 3478 && y == 9799) {
					player.fadeTeleport(CAVE_3_EXIT);
				} else if (x == 3492 && y == 9808) {
					player.fadeTeleport(CAVE_3_ENTRANCE);
				} else if (x == 3505 && y == 9831) {
					player.fadeTeleport(HIDEOUT_EXIT);
				}
				return true;
			case WOODEN_DOORS_1:
			case WOODEN_DOORS_2:
				if (player.getQuestStage(38) >= DOORS_UNLOCKED) {
					player.fadeTeleport(UNDERGROUND_ENTRANCE);
				} else {
					player.getActionSender().sendMessage("These doors are locked.");
				}
				return true;
			case 5056:
			case 5057: //Inside wooden doors
				player.fadeTeleport(new Position(3509, 3449, 0));
				return true;
			case BROKEN_ROPE_BRIDGE:
				if (player.getInventory().playerHasItem(PLANK) && player.getInventory().playerHasItem(STEEL_NAILS, 75) && player.getInventory().playerHasItem(HAMMER)) {
					if (bridgeIndexForPos(y) != 0) {
						player.getQuestVars().setMortMyreBridgeFixed(bridgeIndexForPos(y), true);
						player.getUpdateFlags().sendAnimation(898);
						player.getInventory().removeItem(new Item(PLANK));
						player.getInventory().removeItem(new Item(STEEL_NAILS, 75));
						player.setStopPacket(true);
						CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
							int count = 0;

							@Override
							public void execute(CycleEventContainer b) {
								ObjectHandler.getInstance().removeObject(x, y, player.getPosition().getZ(), 22);
								new GameObject(5002, x, y, player.getPosition().getZ(), 1, 22, 0, 999999, true);
								if (count == 1) {
									b.stop();
								}
								count++;
							}

							@Override
							public void stop() {
								player.setStopPacket(false);
								player.getActionSender().walkTo(0, y - player.getPosition().getY(), true);
							}
						}, 1);
					}
				} else {
					player.getDialogue().sendStatement("You need a wooden plank, 75 steel nails and a hammer to fix this.");
				}
				return true;
			case ROPE_BRIDGE:
				if (bridgeIndexForPos(y) != 0 && !player.getQuestVars().getMortMyreBridgeFixed(bridgeIndexForPos(y))) {
					ObjectHandler.getInstance().removeObject(x, y, player.getPosition().getZ(), 22);
					new GameObject(5003, x, y, player.getPosition().getZ(), 1, 22, 0, 999999, true);
					player.getDialogue().sendStatement("This bridge looks pretty old. The wooden boards break apart beneath", "your feet.");
				} else {
					player.getActionSender().walkTo(0, y - player.getPosition().getY(), true);
				}
				return true;
			case TREE:
				if (player.getPosition().getY() >= 3427 && player.getPosition().getY() <= 3430) {
					player.getActionSender().sendMessage("You are already on the bridge!", true);
				} else {
					Ladders.climbLadder(player, new Position(3502, player.getPosition().getY() < 3428 ? player.getPosition().getY() + 2 : player.getPosition().getY() - 2, player.getIndex() * 4));
					spawnBridgeObjects(player.getIndex() * 4, false);
				}
				return true;
			case SWAMP_BOAT:
				Dialogues.startDialogue(player, 156700);
				return true;
			case SWAMP_BOAT_ABANDONED:
				if (!QuestHandler.questCompleted(player, 37)) {
					player.getDialogue().sendStatement("You must complete Nature Spirit to use this.");
					player.getDialogue().endDialogue();
				} else {
					sailSwampBoat(player, true);
				}
				return true;
		}
		return false;
	}

	public static boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
		switch (object) {
			case STALAGMITE:
				if (player.getSkill().getLevel()[Skill.AGILITY] < 25) {
					player.getDialogue().sendStatement("You need 25 Agility to pass this obstacle.");
				} else {
					player.getDialogue().sendStatement("You try to squeeze past this stalagmite into a small cavern entrance.");
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							player.getActionSender().sendMessage("You manage to maneuver past the stalagmite.");
							player.teleport(HIDEOUT_ENTRANCE.clone().modifyZ(player.getQuestStage(38) < FIGHT_WON ? 1 : 0));
							player.setStopPacket(false);
						}
					}, 5);
				}
				return true;
			case TREE:
				Ladders.climbLadder(player, new Position(3502, player.getPosition().getY() < 3428 ? player.getPosition().getY() - 2 : player.getPosition().getY() + 2, 0));
				spawnBridgeObjects(player.getIndex() * 4, true);
				return true;
			case SWAMP_BOAT:
				if (player.getQuestStage(38) >= BOARD_BOAT) {
					if (player.getInventory().playerHasItem(995, 10)) {
						player.getInventory().removeItem(new Item(995, 10));
						sailSwampBoat(player, false);
					} else {
						player.getDialogue().setLastNpcTalk(CYREG_PADDLEHORN);
						player.getDialogue().sendNpcChat("You'll need 10 gold to ride.", "Come back when you have the money.", CONTENT);
						player.getDialogue().endDialogue();
					}
				} else {
					player.getDialogue().setLastNpcTalk(CYREG_PADDLEHORN);
					player.getDialogue().sendNpcChat("Woah ho ho, not so fast there. Who said you", "could just climb aboard my boat?", ANGRY_1);
					player.getDialogue().endDialogue();
				}
				return true;
		}
		return false;
	}

	public boolean sendDialogue(final Player player, final int id, int chatId, int optionId, int npcChatId) {
		DialogueManager d = player.getDialogue();
		if (player.getQuestStage(38) == DOORS_UNLOCKED && id >= 1570 && id <= 1574) {
			d.sendNpcChat("Talk to the boss man, Veliaf.", CONTENT);
			d.endDialogue();
			return true;
		}
		switch (id) { //Npc ID
			case 156700: //Swamp boat travel
				switch (d.getChatId()) {
					case 1:
						d.setLastNpcTalk(CYREG_PADDLEHORN);
						if (player.getQuestStage(38) >= BOARD_BOAT) {
							d.sendNpcChat("That'll be 10 coins please.", CONTENT);
						} else {
							d.sendNpcChat("Woah ho ho, not so fast there. Who said you", "could just climb aboard my boat?", ANGRY_1);
							d.endDialogue();
						}
						return true;
					case 2:
						d.sendOption("Ok. (10 gold)", "Oh. No thanks.");
						return true;
					case 3:
						switch (optionId) {
							case 1:
								if (player.getInventory().playerHasItem(995, 10)) {
									d.sendPlayerChat("Here you are.", CONTENT);
								} else {
									d.sendPlayerChat("Oh, I don't have the coin...", SAD);
									d.endDialogue();
								}
								return true;
							case 2:
								d.sendPlayerChat("Oh. No thanks.", CONTENT);
								d.endDialogue();
								return true;
						}
					case 4:
						d.setLastNpcTalk(CYREG_PADDLEHORN);
						d.sendNpcChat("Climb aboard then.", CONTENT);
						return true;
					case 5:
						d.endDialogue();
						player.getInventory().removeItem(new Item(995, 10));
						sailSwampBoat(player, false);
						//player.fadeTeleport(new Position(3499, 3380, 0));
						return true;
				}
				return false;
			case RADIGAD_PONFIT:
				switch (player.getQuestStage(this.getQuestID())) {
					case TALK_TO_MYREQUE:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Ah, it's " + player.getUsername() + ".", "I see you've introduced yourself to Veliaf, good! Now,", "what can I, Radigad Ponfit, do for you?", CONTENT);
								return true;
							case 2:
								d.sendPlayerChat("Nothing really, I'm fine on my own. Thank", "you for asking though.", CONTENT);
								return true;
							case 3:
								d.sendNpcChat("Of course.", CONTENT);
								d.endDialogue();
								player.getQuestVars().myrequeTalkedToBools[4] = true;
								return true;
						}
					return false;
				}
			return false;
			case POLMAFI_FERDYGRIS:
				switch (player.getQuestStage(this.getQuestID())) {
					case TALK_TO_MYREQUE:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Good! Now that you've introduced yourself to Veliaf", "and observed proper protocol, I'll be happy to chat with", "you. Now, please tell me, what can I do for you?", CONTENT);
								return true;
							case 2:
								d.sendPlayerChat("Er, you could give me 1000 gold?", CONTENT);
								return true;
							case 3:
								d.sendNpcChat("HAH! I see, you're the funny type. Well, it", "was a pleasure to meet you " + player.getUsername() + ", but", "I don't have any money to give you.", CONTENT);
								return true;
							case 4:
								d.sendPlayerChat("It was worth a shot... Nice to meet you.", CONTENT);
								d.endDialogue();
								player.getQuestVars().myrequeTalkedToBools[3] = true;
								return true;
						}
					return false;
				}
			return false;
			case IVAN_STROM:
				switch (player.getQuestStage(this.getQuestID())) {
					case TALK_TO_MYREQUE:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hi, Veliaf said that I should introduce myself to all", "members of the Myreque.", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Ok, Veliaf says it's ok for me to talk to you. My name", "is Ivan, nice to meet you.", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("It's nice to meet you Ivan.", HAPPY);
								d.endDialogue();
								player.getQuestVars().myrequeTalkedToBools[2] = true;
								return true;
						}
					return false;
				}
			return false;
			case HAROLD_EVANS:
				switch (player.getQuestStage(this.getQuestID())) {
					case TALK_TO_MYREQUE:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Well met friend! My name's Harold... nice to meet a", "fellow soldier. Just joined us have you?", CONTENT);
								return true;
							case 2:
								d.sendPlayerChat("Er, not exactly! But well met Harold.", CONTENT);
								d.endDialogue();
								player.getQuestVars().myrequeTalkedToBools[1] = true;
								return true;
						}
						return false;
				}
				return false;
			case SANI_PILIU:
				switch (player.getQuestStage(this.getQuestID())) {
					case TALK_TO_MYREQUE:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Hello... very nice to meet you! My name is Sani Piliu,", "what's yours?", CONTENT);
								return true;
							case 2:
								d.sendPlayerChat("I am known as " + player.getUsername() + ", nice to meet you Sani.", CONTENT);
								return true;
							case 3:
								d.sendNpcChat("The pleasure's all mine! Excuse me for being forward,", "but you somehow seem different to other people,", "somehow more determined. I sense a powerful aura.", "Sorry, that must seem really strange.", CONTENT);
								return true;
							case 4:
								d.sendPlayerChat("Not strange at all m'lady. It's nice of you to say so", "in any case. I just wanted to introduce myself.", "It was my pleasure to meet you.", CONTENT);
								return true;
							case 5:
								d.sendNpcChat("Ooh! So chivalrous! The world needs more people", "like you!", HAPPY);
								d.endDialogue();
								player.getQuestVars().myrequeTalkedToBools[0] = true;
								return true;
						}
						return false;
				}
				return false;
			case VELIAF_HURTZ:
				switch (player.getQuestStage(this.getQuestID())) {
					case FIGHT_WON:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("That murderer Vanstrom has killed Sani and Harold!", "He came in and killed them right in front of our eyes.", "And that beast he summoned probably would have killed", "the rest of us if it wasn't for you!", SAD);
								return true;
							case 2:
								d.sendPlayerChat("It was the least I could do. After", "all, I was the one he followed here, I'm", "sort of responsible for this mess.", CONTENT);
								return true;
							case 3:
								d.sendNpcChat("That is for your conscience to bear, but now perhaps", "you can understand why we fight for our freedom?", CONTENT);
								return true;
							case 4:
								d.sendPlayerChat("Yes, I think I understand now.", CONTENT);
								return true;
							case 5:
								d.sendOption("I want to join your organization.", "What do you plan to do now?", "How do I get out of here?", "Let's get revenge on Vanstrom!", "Ok, thanks.");
								return true;
							case 6:
								switch(optionId) {
									case 1:
										d.sendPlayerChat("I want to join your organization.", CONTENT);
										return true;
									case 2:
										d.sendPlayerChat("What do you plan to do now?", CONTENT);
										d.setNextChatId(8);
										return true;
									case 3:
										d.sendPlayerChat("How do I get out of here?", CONTENT);
										d.setNextChatId(10);
										return true;
									case 4:
										d.sendPlayerChat("Let's get revenge on Vanstrom!", ANGRY_1);
										d.setNextChatId(12);
										return true;
									case 5:
										d.sendPlayerChat("Ok, thanks.", CONTENT);
										d.endDialogue();
										return true;
								}
							case 7:
								d.sendNpcChat("Hmm, well thanks for your support.", "But I'll need to talk to my superior first.", CONTENT);
								d.setNextChatId(5);
								return true;
							case 8:
								d.sendNpcChat("Well, I guess Vanstrom will expect that we're dead.", "He's so arrogant that he doesn't really consider us a", "threat. I can understand that, if I was that powerful", "I would probably feel the same.", CONTENT);
								return true;
							case 9:
								d.sendNpcChat("I guess we'll regroup and then", "try to recruit more people.", "We first need to know how we can fight against these", "monsters, then we'll take the war to them!", CONTENT);
								d.setNextChatId(5);
								return true;
							case 10:
								d.sendNpcChat("If you go back into the main corridor which leads", "into this room, it goes down towards the", "basement of an inn.", CONTENT);
								return true;
							case 11:
								d.sendNpcChat("If you search the wall, you'll find that it is in fact", "moveable. You should be able to get into the room, take", "the ladder to come out to the south of the", "'Hair of the Dog Tavern' in Canifis.", CONTENT);
								d.setNextChatId(5);
								return true;
							case 12:
								d.sendNpcChat("Yes, of course, we'll certainly get our revenge", "on him and his superiors! Then they'll taste the", "bitter sorrow that we've had to endure so long!", ANGRY_1);
								d.setNextChatId(5);
								return true;
						}
					return false;
					case TALKED_TO_MYREQUE:
						if(player.getPosition().getZ() == 1) {
							if (allWeapons(player)) {
								d.sendNpcChat("These weapons look great! Many thanks.", CONTENT);
								startEncounter(player);
							} else {
								d.sendNpcChat("Hm, you seem to be missing a few weapons you", "said you had for us.", CONTENT);
								d.endDialogue();
							}
							return true;
						}
						return false;
					case TALK_TO_MYREQUE:
						boolean talked = true;
						for (boolean b : player.getQuestVars().myrequeTalkedToBools) {
							if (!b) {
								talked = false;
							}
						}
						switch (d.getChatId()) {
							case 1:
								if (!talked) {
									d.sendNpcChat("Please introduce yourself to the others, then", "we can talk about those weapons.", CONTENT);
									d.endDialogue();
								} else {
									d.sendNpcChat("Hello again... so, you've introduced yourself to the team", "have you? Good. Now, let's have a look at those", "weapons you brought us.", CONTENT);
								}
								return true;
							case 2:
								if (allWeapons(player)) {
									d.sendNpcChat("These weapons look great! Many thanks.", CONTENT);
									player.setQuestStage(this.getQuestID(), TALKED_TO_MYREQUE);
									startEncounter(player);
								} else {
									d.sendNpcChat("Hm, you seem to be missing a few weapons you", "said you had for us.", CONTENT);
									d.endDialogue();
								}
								return true;
						}
						return false;
					case DOORS_UNLOCKED:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello there...", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Hello there... how did you get in here?", "Who are you?", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("I was asked to bring you some weapons, apparently you", "need them? Don't worry, I'm a friend!", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("Hmm, well, it's true we could do with some supplies. It's", "very good of you to bring them to us. I'm sorry but", "I'm a bit busy at the moment, please introduce yourself", "to the others, then we can talk about those weapons.", CONTENT);
								return true;
							case 5:
								d.sendPlayerChat("Ok, thanks.", CONTENT);
								d.endDialogue();
								player.setQuestStage(38, TALK_TO_MYREQUE);
								return true;
						}
						return false;
				}
				return false;
			case CURPILE_FYOD:
				switch (player.getQuestStage(this.getQuestID())) {
					case BOARD_BOAT:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Hey... what're you doin' here?", CONTENT);
								return true;
							case 2:
								if (allWeapons(player)) {
									d.sendPlayerChat("I've come to help the Myreque, I've brought weapons.", CONTENT);
								} else {
									d.sendPlayerChat("Just looking around for the rest of the", "weapons I'm supposed to have...", SAD);
									d.endDialogue();
								}
								return true;
							case 3:
								d.sendNpcChat("Ok, I see ya got da weapons... but how'd I know", "you're not gonna use 'em against my friends?", CONTENT);
								return true;
							case 4:
								d.sendPlayerChat("But I just want to help deliver these weapons.", CONTENT);
								return true;
							case 5:
								d.sendNpcChat("Well, dat's as maybe, and I'm not doubtin' your", "sincerity here, you seems all sincered up to me... it's", "choking me up right here, you're making me cry... but", "hey, I's godda do my job or da kids don't get fed! Ok,", CONTENT);
								return true;
							case 6:
								d.sendNpcChat("so say I asks you a few questions and you were to", "answer them all correct and so on, well that'd make me", "believe you's... I'd get to feeling that you was the", "real deal an all. How's dat sound?", CONTENT);
								return true;
							case 7:
								d.sendPlayerChat("Sounds fine to me... go ahead, shoot!", CONTENT);
								return true;
							case 8:
								d.sendNpcChat("Hey... don't tempt me! You's dicing wiv death here", "my friend!", CONTENT);
								return true;
							case 9:
								d.sendNpcChat("Ok, first question. Who is da leader of the", "Myreque?", CONTENT);
								return true;
							case 10:
								d.sendOption("Sani Piliu", "Ivan Strom", "Veliaf Hurtz", "Redigad Ponfit", "Don't know!");
								return true;
							case 11:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("Sani Piliu.", CONTENT);
										d.setNextChatId(30);
										return true;
									case 2:
										d.sendPlayerChat("Ivan Strom.", CONTENT);
										d.setNextChatId(30);
										return true;
									case 3:
										d.sendPlayerChat("Veliaf Hurtz.", CONTENT);
										return true;
									case 4:
										d.sendPlayerChat("Redigad Ponfit.", CONTENT);
										d.setNextChatId(30);
										return true;
									case 5:
										d.sendPlayerChat("I guess I don't know...", SAD);
										d.endDialogue();
										return true;
								}
							case 12:
								d.sendNpcChat("Ok, interesting answer. First question answered.", CONTENT);
								return true;
							case 13:
								d.sendNpcChat("Ok, second question. What is the boatman's name?", CONTENT);
								return true;
							case 14:
								d.sendOption("Geof Paddleman", "Cyreg Paddlebone", "Gyrec Paddlehorn", "Cyreg Paddlehorn", "I don't know!");
								return true;
							case 15:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("Geof Paddleman.", CONTENT);
										d.setNextChatId(30);
										return true;
									case 2:
										d.sendPlayerChat("Cyreg Paddlebone.", CONTENT);
										d.setNextChatId(30);
										return true;
									case 3:
										d.sendPlayerChat("Gyrec Paddlehorn.", CONTENT);
										d.setNextChatId(30);
										return true;
									case 4:
										d.sendPlayerChat("Cyreg Pattlehorn.", CONTENT);
										return true;
									case 5:
										d.sendPlayerChat("I guess I don't know...", SAD);
										d.endDialogue();
										return true;
								}
							case 16:
								d.sendNpcChat("An interesting response. Second question answered.", CONTENT);
								return true;
							case 17:
								d.sendNpcChat("Ok, third and final question. What does", "'Myreque' mean?", CONTENT);
								return true;
							case 18:
								d.sendOption("Myre Protection", "Myre What", "Safe in Myre", "Hidden in Myre", "I don't know!");
								return true;
							case 19:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("Myre Protection.", CONTENT);
										d.setNextChatId(30);
										return true;
									case 2:
										d.sendPlayerChat("Myre What.", CONTENT);
										d.setNextChatId(30);
										return true;
									case 3:
										d.sendPlayerChat("Safe in Myre.", CONTENT);
										d.setNextChatId(30);
										return true;
									case 4:
										d.sendPlayerChat("Hidden in Myre.", CONTENT);
										return true;
									case 5:
										d.sendPlayerChat("I guess I don't know...", SAD);
										d.endDialogue();
										return true;
								}
							case 20:
								d.sendNpcChat("Hmm... a calculated retort. Third question answered.", CONTENT);
								return true;
							case 21:
								d.sendNpcChat("Ok, I believes ya... you can go on.", CONTENT);
								return true;
							case 22:
								d.sendPlayerChat("What's the combination to the door?", CONTENT);
								return true;
							case 23:
								d.sendNpcChat("Oh, there isn't one. I'll unlock it for you.", CONTENT);
								d.endDialogue();
								player.setQuestStage(38, DOORS_UNLOCKED);
								return true;
							case 30:
								d.endDialogue();
								player.setStopPacket(true);
								player.getActionSender().removeInterfaces();
								final Npc curpile = World.getNpcs()[World.getNpcIndex(CURPILE_FYOD)];
								curpile.getUpdateFlags().setForceChatMessage("That's'a wrong answer! You're no friend!");
								curpile.setFollowingEntity(player);
								curpile.getUpdateFlags().sendAnimation(451);
								player.getUpdateFlags().sendGraphic(254, 100 << 16);
								player.getUpdateFlags().sendAnimation(836);
								CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
									@Override
									public void execute(CycleEventContainer b) {
										b.stop();
									}

									@Override
									public void stop() {
										player.getActionSender().sendMessage("Oh dear, you are dead?");
										player.fadeTeleport(BOATMAN_POS);
										curpile.setFollowingEntity(null);
										Following.resetFollow(curpile);
										player.setStopPacket(false);
									}
								}, 4);
								return true;
						}
						return false;
				}
				return false;
			case CYREG_PADDLEHORN:
				if(player.getQuestStage(this.getQuestID()) > BOARD_BOAT) {
					switch (d.getChatId()) {
						case 1:
							d.sendNpcChat("Hello again! Would you like to take", "my boat up the swamp?", HAPPY);
							return true;
						case 2:
							d.sendOption("Sure.", "No thanks.");
							return true;
						case 3:
							switch(optionId) {
								case 1:
									d.sendPlayerChat("Sure.", CONTENT);
									return true;
								case 2:
									d.sendPlayerChat("No thanks.", CONTENT);
									d.endDialogue();
									return true;
							}
						case 4:
							d.sendNpcChat("That'll be 10 coins please.", CONTENT);
							return true;
						case 5:
							d.sendOption("Ok. (10 gold)", "Oh. No thanks.");
							return true;
						case 6:
							switch (optionId) {
								case 1:
									if (player.getInventory().playerHasItem(995, 10)) {
										d.sendPlayerChat("Here you are.", CONTENT);
									} else {
										d.sendPlayerChat("Oh, I don't have the coin...", SAD);
										d.endDialogue();
									}
									return true;
								case 2:
									d.sendPlayerChat("Oh. No thanks.", CONTENT);
									d.endDialogue();
									return true;
							}
						case 7:
							d.sendNpcChat("Climb aboard then.", CONTENT);
							return true;
						case 8:
							d.endDialogue();
							player.getInventory().removeItem(new Item(995, 10));
							sailSwampBoat(player, false);
							return true;
					}
					return false;
				}
				switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
					case BOARD_BOAT:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Very well, you can take the boat. Just jump in when", "you're ready to leave. When you get to the hollows,", "just keep going North and look for an unusual tree.", CONTENT);
								d.endDialogue();
								return true;
						}
						return false;
					case POUCH_FOR_RIDE:
						switch (d.getChatId()) {
							case 1:
								if (player.getInventory().playerHasItem(NatureSpirit.DRUID_POUCH, 5)) {
									d.sendGiveItemNpc("You show the boatman your druid pouch.", new Item(NatureSpirit.DRUID_POUCH_EMPTY));
								} else {
									d.sendNpcChat("You can use my boat to find the Myreque. You'll", "be going through Mort Myre though so I won't be", "letting you go unless you've some defense against the", "Ghasts.", CONTENT);
									d.endDialogue();
								}
								return true;
							case 2:
								d.sendPlayerChat("I have this druid pouch here! This turns the Ghasts", "visible and I can kill them once I can see them.", HAPPY);
								return true;
							case 3:
								d.sendNpcChat("Very well, you can go! But you'll need to bring me", "some wood planks first, I need three and you", "need three yourself.", CONTENT);
								if (!player.getInventory().playerHasItem(PLANK, 3)) {
									d.endDialogue();
								}
								return true;
							case 4:
								d.sendNpcChat("The bridge you cross later is rotten and may need to", "be mended, so bring tools and steel metal fixers as well,", "you may find them useful. I see that you have some", "with you now, do you want to give them to me?", CONTENT);
								return true;
							case 5:
								d.sendOption("Give 3 wooden planks to Cyreg.", "Do nothing.");
								return true;
							case 6:
								switch (optionId) {
									case 1:
										d.sendGiveItemNpc("The boatman takes 3 wooden planks from you.", new Item(PLANK));
										return true;
									case 2:
										d.sendPlayerChat("I need some more time to plan.", CONTENT);
										d.endDialogue();
										return true;
								}
							case 7:
								d.sendNpcChat("Very well, you can take the boat. Just jump in when", "you're ready to leave. When you get to the hollows,", "just keep going North and look for an unusual tree.", CONTENT);
								d.endDialogue();
								player.getInventory().removeItem(new Item(PLANK, 3));
								player.setQuestStage(38, BOARD_BOAT);
								return true;
						}
						return false;
					case QUEST_STARTED:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Hello there friend.", CONTENT);
								return true;
							case 2:
								if (allWeapons(player)) {
									d.sendPlayerChat("Hello there, I have some weapons for you to give to", "the 'Myreque'.", CONTENT);
								} else {
									d.sendPlayerChat("Hello there, I was wondering what you know about", "the 'Myreque'?", CONTENT);
								}
								return true;
							case 3:
								d.sendNpcChat("Hmm, I don't know what you're talking about.", CONTENT);
								if (!allWeapons(player)) {
									d.endDialogue();
								}
								return true;
							case 4:
								d.sendPlayerChat("Come on, I know you're in cahoots with them, just take", "these weapons to them.", ANGRY_1);
								return true;
							case 5:
								d.sendNpcChat("Ok, seriously, I did some work for them before, but", "now it's just too dangerous. I won't take the weapons", "to them, I'm sorry, it's just too dangerous..", DISTRESSED);
								return true;
							case 6:
								d.sendPlayerChat("Can you tell me how to find the Myreque?", CONTENT);
								return true;
							case 7:
								d.sendNpcChat("Their base is well hidden and I'm sorry but I can't", "reveal the directions. Sorry but I guess you're all", "out of luck.", SAD);
								return true;
							case 8:
								d.sendPlayerChat("Well, I guess they'll just die without weapons.", CONTENT);
								return true;
							case 9:
								d.sendNpcChat("Hmm, you don't seem too concerned about their", "welfare. I'm glad I didn't tell you where they were...", "in any case they're resourceful, they can look", "after themselves.", CONTENT);
								return true;
							case 10:
								d.sendPlayerChat("What's that supposed to mean?", CONTENT);
								return true;
							case 11:
								d.sendNpcChat("They're resourceful folks, that's all I'm saying. Their", "leader, Veliaf, looks after them well.", CONTENT);
								return true;
							case 12:
								d.sendPlayerChat("Resourceful enough to get their own steel weapons?", CONTENT);
								return true;
							case 13:
								d.sendNpcChat("Maybe they are... what do you care anyway? They've", "been up against it ever since they got started. All of", "'em have suffered more loss and heartache than you'll", "ever know. Now, leave me be!", ANGRY_1);
								return true;
							case 14:
								d.sendPlayerChat("If you don't tell me, their deaths are on your head!", CONTENT);
								return true;
							case 15:
								d.sendNpcChat("There's death a plenty in this forsaken place... what do I", "care that some fool hardy vigilantes decided to go it", "alone against the Drakans? Stupidity of youth is to", "blame, I shan't carry it on my shoulders!", CONTENT);
								return true;
							case 16:
								d.sendPlayerChat("What kind of man are you to say you don't care?", ANGRY_1);
								return true;
							case 17:
								d.sendNpcChat("Don't dare to judge me young fool... what do you know", "of the heartache I carry? Can you not see the anchor", "of woe that holds me fast?", SAD);
								return true;
							case 18:
								d.sendNpcChat("Very well, if you would take your chance to help these", "strangers, who am I to stop you?", CONTENT);
								return true;
							case 19:
								d.sendPlayerChat("But will you help me? Will you take me to them?", CONTENT);
								return true;
							case 20:
								d.sendNpcChat("No, I won't take you, but you can use my boat. You'll", "be going through Mort Myre though so I won't be", "letting you go unless you've some defense against the", "Ghasts.", CONTENT);
								if (player.getInventory().playerHasItem(NatureSpirit.DRUID_POUCH, 5)) {
									d.setNextChatId(1);
								} else {
									d.endDialogue();
								}
								player.setQuestStage(38, POUCH_FOR_RIDE);
								return true;
						}
						return false;
				}
				return false;
			case VANSTROM_CLAUSE:
				switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
					case QUEST_COMPLETE:
						d.setLastNpcTalk(STRANGER);
						d.sendNpcChat("Hello again friend, I hope you get that", "vengeance thing sorted out.", HAPPY);
						d.endDialogue();
						return true;
					case FIGHT_WON:
						d.setLastNpcTalk(STRANGER);
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Hello friend? What can I do for you?", CONTENT);
								return true;
							case 2:
								d.sendPlayerChat("Oh, sorry, I thought you were someone else.", CONTENT);
								return true;
							case 3:
								d.sendNpcChat("Who did you think I was?", CONTENT);
								return true;
							case 4:
								d.sendPlayerChat("I thought you were that dirty murderer Vanstrom.", ANGRY_1);
								return true;
							case 5:
								d.sendNpcChat("Woah, it sounds like you've got a score to settle!", Dialogues.DISTRESSED_CONTINUED);
								return true;
							case 6:
								d.sendPlayerChat("I definitely have!", ANGRY_1);
								return true;
							case 7:
								d.dontCloseInterface();
								QuestHandler.completeQuest(player, 38);
								return true;
						}
					return false;
					case QUEST_STARTED:
						switch (d.getChatId()) {
							case 1:
								d.sendOption("What do I have to do again?", "What weapons do I need to get again?", "Where do I need to take the weapons again?", "Never mind.");
								d.setNextChatId(20);
								return true;
							case 20:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("What do I have to do again?", CONTENT);
										d.setNextChatId(21);
										return true;
									case 2:
										d.sendPlayerChat("What weapons do I need to get again?", CONTENT);
										d.setNextChatId(23);
										return true;
									case 3:
										d.sendPlayerChat("Where do I need to take the weapons again?", CONTENT);
										d.setNextChatId(24);
										return true;
									case 4:
										d.sendPlayerChat("Never mind.", CONTENT);
										d.endDialogue();
										return true;
								}
							case 21:
								d.sendNpcChat("The Myreque are almost certainly able to", "handle themselves... given the tools! I hear they're short", "of weapons, I was hoping to do it myself but I find that", "I'm rather short of time and ability!", CONTENT);
								return true;
							case 22:
								d.sendNpcChat("That's where you come in, you can deliver the", "weapons they need to them!", HAPPY);
								d.setNextChatId(1);
								return true;
							case 23:
								d.sendNpcChat("Steel I believe. All six Myreque require steel weapons. I", "would suggest a longsword, two short swords, a", "dagger, a mace and a warhammer.", CONTENT);
								d.setNextChatId(1);
								return true;
							case 24:
								d.sendNpcChat("Erm, I'm not really sure, no one truly is.", "Your best bet is to head to Mort'ton, that's where", "their headquarters is rumored to be near. Speak with", "the boatman when you arrive there.", CONTENT);
								d.setNextChatId(1);
								return true;
						}
						return false;
					case 0:
						switch (d.getChatId()) {
							case 1:
								if (!QuestHandler.questCompleted(player, 37)) {
									d.sendStatement("You need to complete Nature Spirit to interact with this NPC.");
									d.endDialogue();
								} else {
									d.sendNpcChat("Hello there, how goes it stranger?", HAPPY);
								}
								return true;
							case 2:
								d.sendPlayerChat("Quite well, thanks for asking, how about you?", CONTENT);
								return true;
							case 3:
								d.sendNpcChat("Hmm, well, I am a little concerned about some friends", "of mine, they're in dire need of some assistance, but", "I'm at a loss as to how I can help them.", CONTENT);
								return true;
							case 4:
								d.sendOption("What friends are these?", "Why do they need help? Are they in trouble?", "I wish I could help, but I'm busy at the moment.", "Ok, thanks.");
								return true;
							case 5:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("What friends are these?", CONTENT);
										return true;
									case 2:
										d.sendPlayerChat("Why do they need help? Are they in trouble?", CONTENT);
										d.setNextChatId(10);
										return true;
									case 3:
										d.sendPlayerChat("I wish I could help, but I'm busy at the moment.", CONTENT);
										d.endDialogue();
										return true;
									case 4:
										d.sendPlayerChat("Ok, thanks.", CONTENT);
										d.endDialogue();
										return true;
								}
							case 6:
								d.sendNpcChat("It's a personal tragedy that I have yet to meet them in", "the flesh. But their exploits make mouth watering hero", "stories... the real meat and drink of high adventure", "and daring... so they say.", CONTENT);
								return true;
							case 7:
								d.sendPlayerChat("What does that mean exactly? I mean, I have some", "stories. I'm quite a hero myself, you may actually be", "talking about me?", CONTENT);
								return true;
							case 8:
								d.sendNpcChat("They're regarded as heroes in Morytania, though some", "people see them as vigilantes. The local villagers call", "them the 'Myreque'. Some people call them terrorists", "while others call them freedom fighters!", HAPPY);
								d.setNextChatId(4);
								return true;
							case 10:
								d.sendNpcChat("I should imagine that heroes of such high caliber are", "almost always in some sort of trouble, wouldn't you?", "There's always some evil heel ready to grind the face of", "humanity into the dirt?", CONTENT);
								return true;
							case 11:
								d.sendNpcChat("However, the Myreque are almost certainly able to", "handle themselves... given the tools! I hear they're short", "of weapons, I was hoping to do it myself but I find that", "I'm rather short of time and ability!", CONTENT);
								return true;
							case 12:
								d.sendPlayerChat("What help do you hope to give them?", CONTENT);
								return true;
							case 13:
								d.sendNpcChat("I'd have taken some weapons to them!", HAPPY);
								return true;
							case 14:
								d.sendPlayerChat("What kind of weapons do they need?", CONTENT);
								return true;
							case 15:
								d.sendNpcChat("Steel I believe. All six of them require steel weapons. I", "would have suggested a longsword, two shortswords, a", "dagger, a mace and a warhammer.", CONTENT);
								return true;
							case 16:
								d.sendOption("What friends are these?", "Why do they need help? Are they in trouble?", "I wish I could help, but I'm busy at the moment.", "Perhaps I could help you out here.", "Ok, thanks.");
								return true;
							case 17:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("What friends are these?", CONTENT);
										d.setNextChatId(6);
										return true;
									case 2:
										d.sendPlayerChat("Why do they need help? Are they in trouble?", CONTENT);
										d.setNextChatId(10);
										return true;
									case 3:
										d.sendPlayerChat("I wish I could help, but I'm busy at the moment.", CONTENT);
										d.endDialogue();
										return true;
									case 4:
										d.sendPlayerChat("Perhaps I could help you out here. I may be", "able to take those weapons to the Myreque.", CONTENT);
										return true;
									case 5:
										d.sendPlayerChat("Ok, thanks.", CONTENT);
										d.endDialogue();
										return true;
								}
							case 18:
								d.sendNpcChat("Oh yes, well that would be very nice of you! Are you", "sure you want to help out?", CONTENT);
								return true;
							case 19:
								d.sendOption("What would I have to do?", "What weapons do I need to get?", "Where do I need to take the weapons?", "Yes, I'll do it!", "Sorry, I can't do it!");
								return true;
							case 20:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("What would I have to do?", CONTENT);
										return true;
									case 2:
										d.sendPlayerChat("What weapons do I need to get?", CONTENT);
										d.setNextChatId(23);
										return true;
									case 3:
										d.sendPlayerChat("Where do I need to take the weapons?", CONTENT);
										d.setNextChatId(24);
										return true;
									case 4:
										d.sendPlayerChat("Yes, I'll do it!", CONTENT);
										d.setNextChatId(25);
										return true;
									case 5:
										d.sendPlayerChat("Sorry, I can't do it.", SAD);
										d.endDialogue();
										return true;
								}
							case 21:
								d.sendNpcChat("The Myreque are almost certainly able to", "handle themselves... given the tools! I hear they're short", "of weapons, I was hoping to do it myself but I find that", "I'm rather short of time and ability!", CONTENT);
								return true;
							case 22:
								d.sendNpcChat("That's where you come in, you can deliver the", "weapons they need to them!", HAPPY);
								d.setNextChatId(19);
								return true;
							case 23:
								d.sendNpcChat("Steel I believe. All six Myreque require steel weapons. I", "would suggest a longsword, two shortswords, a", "dagger, a mace and a warhammer.", CONTENT);
								d.setNextChatId(19);
								return true;
							case 24:
								d.sendNpcChat("Erm, I'm not really sure, no one truly is.", "Your best bet is to head to Mort'ton, that's where", "their headquarters is rumored to be near. Speak with", "the boatman when you arrive there.", CONTENT);
								d.setNextChatId(19);
								return true;
							case 25:
								d.sendNpcChat("That's great news my friend, really great news!", "Perhaps the many peoples of Morytania now have an", "additional hero that they can come to rely upon?", HAPPY);
								d.endDialogue();
								QuestHandler.startQuest(player, 38);
								return true;
						}
						return false;
				}
				return false;
		}
		return false;
	}

}
