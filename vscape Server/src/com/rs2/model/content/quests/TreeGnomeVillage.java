package com.rs2.model.content.quests;

import com.rs2.model.Position;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.Graphic;
import com.rs2.model.objects.GameObject;
import com.rs2.model.World;
import com.rs2.util.Misc;
import com.rs2.model.content.skills.agility.Agility;

public class TreeGnomeVillage implements Quest {

	//Quest stages

	public static final int QUEST_STARTED = 1;
	public static final int GOT_WOOD = 2;
	public static final int GAVE_MONTAI_WOOD = 3;
	public static final int GET_COORDINATES = 4;
	public static final int FIRIN_MA_LAZAH = 5;
	public static final int ORB_1 = 6;
	public static final int ARE_YOU_FUCKING_KIDDING_ME = 7;
	public static final int KICK_WARLORDS_ASS = 8;
	public static final int QUEST_COMPLETE = 9;

	public String crazyRants[] = {"Less than my hands", "More than my head", "Less than our feet", "Less than my fingers", "My legs and your legs"};
	//Items
	public static final int FIRST_ORB = 587;
	public static final int OTHER_ORBS = 588;
	public static final int GNOME_AMULET = 589;
	public static final int LOGS = 1511;
	//Positions
	public static final Position INSIDE_VILLAGE = new Position(2515, 3162, 0);
	public static final Position OUTSIDE_VILLAGE = new Position(2505, 3192, 0);
    //Interfaces
	//Npcs
	public static final int KING_BOLREN = 469;
	public static final int ELKOY_INSIDE = 473;
	public static final int ELKOY_OUTSIDE = 474;
	public static final int COMMANDER_MONTAI = 470;
	public static final int KHAZARD_GUARD = 478;
	public static final int KHAZARD_WARLORD = 477;
	public static final int TRACKER_GNOME_1 = 481;
	public static final int TRACKER_GNOME_2 = 482;
	public static final int TRACKER_GNOME_3 = 483;
	public static final int GNOME_CHILD_1 = 159;
	public static final int GNOME_CHILD_2 = 160;
	public static final int GNOME_CHILD_3 = 161;
	//Objects
	public static final int BALLISTA = 2181;
	public static final int BROKEN_WALL = 2185;
	public static final int ORB_CHEST_CLOSED = 2183;
	public static final int ORB_CHEST_OPEN = 2182;
	public static final int KHAZARD_BASE_DOOR = 2184;

    //Buttons
	public int dialogueStage = 0;

	private int reward[][] = {
		{GNOME_AMULET, 1},};
	private int expReward[][] = {
		{Skill.ATTACK, 11450},};

	private static final int questPointReward = 5;

	public int getQuestID() {
		return 33;
	}

	public String getQuestName() {
		return "Tree Gnome Village";
	}

	public String getQuestSaveName() {
		return "treegnome";
	}

	public boolean canDoQuest(Player player) {
		return true;
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
		player.getActionSender().sendItemOnInterface(995, 200, 12142);
		player.getActionSender().sendString("You have completed " + getQuestName() + "!", 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("2 Quest Points", 12150);
		player.getActionSender().sendString("25762.5 Attack XP", 12151);
		player.getActionSender().sendString("Gnome Amulet of Protection", 12152);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), 7361);
	}

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		switch (questStage) {
			case QUEST_STARTED:
				player.getActionSender().sendString("@str@" + "Talk to King Bolren, in the Tree Gnome Village.", 8147);
				player.getActionSender().sendString("Speak with Commander Montai at the gnome battlegrounds.", 8149);
				break;
			case GOT_WOOD:
				player.getActionSender().sendString("@str@" + "Talk to King Bolren, in the Tree Gnome Village.", 8147);
				player.getActionSender().sendString("@str@" + "Speak with Commander Montai at the gnome battlefield.", 8148);
				player.getActionSender().sendString("Commander Montai has asked me to get him 6 sets of logs.", 8150);
				break;
			case GAVE_MONTAI_WOOD:
				player.getActionSender().sendString("@str@" + "Talk to King Bolren, in the Tree Gnome Village.", 8147);
				player.getActionSender().sendString("@str@" + "Speak with Commander Montai at the gnome battlefield.", 8148);
				player.getActionSender().sendString("@str@" + "Commander Montai has asked to get him 6 sets of logs.", 8149);
				player.getActionSender().sendString("Montai needs me to find the 3 tracker gnomes and", 8151);
				player.getActionSender().sendString("get the coordinates for the Khazard base from them.", 8152);
				break;
			case GET_COORDINATES:
				player.getActionSender().sendString("@str@" + "Talk to King Bolren, in the Tree Gnome Village.", 8147);
				player.getActionSender().sendString("@str@" + "Speak with Commander Montai at the gnome battlefield.", 8148);
				player.getActionSender().sendString("@str@" + "Commander Montai has asked me to get him 6 sets of logs.", 8149);
				player.getActionSender().sendString("@str@" + "The tracker gnomes gave me the coordinates?", 8150);
				player.getActionSender().sendString("Time to fire the Ballista at the Khazard base.", 8152);
				break;
			case FIRIN_MA_LAZAH:
				player.getActionSender().sendString("@str@" + "Talk to King Bolren, in the Tree Gnome Village.", 8147);
				player.getActionSender().sendString("@str@" + "Speak with Commander Montai at the gnome battlefield.", 8148);
				player.getActionSender().sendString("@str@" + "Commander Montai has asked me to get him 6 sets of logs.", 8149);
				player.getActionSender().sendString("@str@" + "The tracker gnomes gave me the coordinates?", 8150);
				player.getActionSender().sendString("I hit the base and made an opening in the wall!", 8152);
				player.getActionSender().sendString("I better head into the base and recover the stolen orb.", 8153);
				break;
			case ORB_1:
				player.getActionSender().sendString("@str@" + "Talk to King Bolren, in the Tree Gnome Village.", 8147);
				player.getActionSender().sendString("@str@" + "Speak with Commander Montai at the gnome battlefield.", 8148);
				player.getActionSender().sendString("@str@" + "Commander Montai has asked me to get him 6 sets of logs.", 8149);
				player.getActionSender().sendString("@str@" + "The tracker gnomes gave me the coordinates?", 8150);
				player.getActionSender().sendString("@str@" + "I hit the base and made an opening in the wall!", 8151);
				player.getActionSender().sendString("I got the orb, better get it back to King Bolren.", 8153);
				break;
			case ARE_YOU_FUCKING_KIDDING_ME:
				player.getActionSender().sendString("@str@" + "Talk to King Bolren, in the Tree Gnome Village.", 8147);
				player.getActionSender().sendString("@str@" + "Speak with Commander Montai at the gnome battlefield.", 8148);
				player.getActionSender().sendString("@str@" + "Commander Montai has asked me to get him 6 sets of logs.", 8149);
				player.getActionSender().sendString("@str@" + "The tracker gnomes gave me the coordinates?", 8150);
				player.getActionSender().sendString("@str@" + "I hit the base and made an opening in the wall!", 8151);
				player.getActionSender().sendString("@str@" + "I returned the stolen orb to King Bolren.", 8152);
				player.getActionSender().sendString("Well, the other orbs are gone now too.", 8154);
				player.getActionSender().sendString("I suppose I have to get them from the Khazard Warlord.", 8155);
				player.getActionSender().sendString("A trial by combat is the only solution.", 8156);
				break;
			case KICK_WARLORDS_ASS:
				player.getActionSender().sendString("@str@" + "Talk to King Bolren, in the Tree Gnome Village.", 8147);
				player.getActionSender().sendString("@str@" + "Speak with Commander Montai at the gnome battlefield.", 8148);
				player.getActionSender().sendString("@str@" + "Commander Montai has asked me to get him 6 sets of logs.", 8149);
				player.getActionSender().sendString("@str@" + "The tracker gnomes gave me the coordinates?", 8150);
				player.getActionSender().sendString("@str@" + "I hit the base and made an opening in the wall!", 8151);
				player.getActionSender().sendString("@str@" + "I returned the stolen orb to King Bolren.", 8152);
				player.getActionSender().sendString("I bested the Khazard Warlord and got the orbs.", 8154);
				player.getActionSender().sendString("Time to head back to King Bolren again.", 8155);
				break;
			case QUEST_COMPLETE:
				player.getActionSender().sendString("@str@" + "Talk to King Bolren, in the Tree Gnome Village.", 8147);
				player.getActionSender().sendString("@str@" + "Speak with Commander Montai at the gnome battlefield.", 8148);
				player.getActionSender().sendString("@str@" + "Commander Montai has asked me to get him 6 sets of logs.", 8149);
				player.getActionSender().sendString("@str@" + "The tracker gnomes gave me the coordinates?", 8150);
				player.getActionSender().sendString("@str@" + "I hit the base and made an opening in the wall!", 8151);
				player.getActionSender().sendString("@str@" + "I returned the stolen orb to King Bolren.", 8152);
				player.getActionSender().sendString("@str@" + "Got the orbs back and returned them to King Bolren.", 8153);
				player.getActionSender().sendString("@red@" + "You have completed this quest!", 8156);
				break;
			default:
				player.getActionSender().sendString("Talk to @dre@King Bolren @bla@in the Tree Gnome Village @bla@ to begin.", 8147);
				player.getActionSender().sendString("@dre@Requirements:", 8148);
				player.getActionSender().sendString("@dre@-I must be able to defeat a level 112 Warlord.", 8150);
				break;
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 7380);
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
			player.getActionSender().sendString("@yel@" + getQuestName(), 7380);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 7380);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 7380);
		}
	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
		player.getActionSender().sendString(getQuestName(), 8144);
	}

	public void handleDeath(final Player player, Npc npc) {
		if (npc.getNpcId() == KHAZARD_WARLORD && (player.getQuestStage(33) == ARE_YOU_FUCKING_KIDDING_ME || player.getQuestStage(33) == KICK_WARLORDS_ASS)) {
			if (player.getQuestStage(33) == ARE_YOU_FUCKING_KIDDING_ME) {
				player.setQuestStage(33, KICK_WARLORDS_ASS);
			}
			player.getInventory().addItemOrDrop(new Item(OTHER_ORBS));
			player.getDialogue().sendPlayerChat("I'll be taking these back to their rightful owner.", CONTENT);
		}
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

	public boolean doItemOnNpc(Player player, int itemId, Npc npc) {
		return false;
	}

	public boolean doNpcClicking(Player player, Npc npc) {
		return false;
	}

	public boolean doObjectClicking(final Player player, int object, int x, int y) {
		switch (object) {
			case BROKEN_WALL:
				if (player.getQuestStage(33) == FIRIN_MA_LAZAH || (player.getQuestStage(33) == ORB_1 && !player.getInventory().playerHasItem(FIRST_ORB))) {
					if (player.getPosition().getY() < 3254) {
						player.getActionSender().sendMessage("You climb over the rubble into the Khazard Stronghold.");
						Agility.climbOver(player, 2509, 3254, 0, 0);
						player.setStopPacket(true);
						CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
							int counter = 0;

							@Override
							public void execute(CycleEventContainer b) {
								if (counter >= 4 || player.getPosition().getZ() > 0) {
									b.stop();
								}
								if (counter == 1) {
									Npc guard = World.getNpcs()[World.getNpcIndex(KHAZARD_GUARD)];
									guard.getUpdateFlags().setForceChatMessage("What! How did you manage to get in here?");
									CombatManager.attack(guard, player);
									player.setStopPacket(false);
								}
								if (counter == 2) {
									player.getUpdateFlags().setForceChatMessage("I've come for the orb!");

								}
								if (counter == 3) {
									World.getNpcs()[World.getNpcIndex(KHAZARD_GUARD)].getUpdateFlags().setForceChatMessage("I'll never let you take it!");
								}
								counter++;
							}

							@Override
							public void stop() {
							}
						}, 3);
						return true;
					} else {
						Agility.climbOver(player, 2509, 3253, 0, 0);
						return true;
					}
				} else {
					player.getActionSender().sendMessage("You have no reason to enter this way.");
					return true;
				}
			case ORB_CHEST_CLOSED:
				player.getActionSender().sendMessage("You flip the chest open quickly.");
				player.getUpdateFlags().sendAnimation(TheGrandTree.PLACE_ANIM);
				final CacheObject g = ObjectLoader.object(2506, 3259, 1);
				new GameObject(ORB_CHEST_OPEN, 2506, 3259, 1, g.getRotation(), g.getType(), ORB_CHEST_CLOSED, 30);
				Npc guard = World.getNpcs()[World.getNpcIndex(KHAZARD_GUARD) + 1];
				guard.getUpdateFlags().setForceChatMessage("Oi! Get out of there!");
				CombatManager.attack(guard, player);
				return true;
			case ORB_CHEST_OPEN:
				if (!player.getInventory().playerHasItem(FIRST_ORB) && (player.getQuestStage(33) == FIRIN_MA_LAZAH || player.getQuestStage(33) == ORB_1)) {
					player.getActionSender().sendMessage("You search the chest and find the stolen orb!");
					player.getInventory().addItemOrDrop(new Item(FIRST_ORB));
					player.setQuestStage(33, ORB_1);
				} else {
					player.getActionSender().sendMessage("Nothing interesting happens.");
				}
				return true;
			case KHAZARD_BASE_DOOR:
				if (player.getQuestStage(33) < ORB_1) {
					player.getActionSender().sendMessage("It's locked. Better find a different way in.");
					return true;
				} else {
					player.getActionSender().sendMessage("You open the door and walk through.");
					player.getActionSender().walkTo(0, player.getPosition().getY() < 3251 ? 1 : -1, true);
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					return true;
				}
		}
		return false;
	}

	public boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
		switch (object) {
			case BALLISTA:
				Dialogues.startDialogue(player, 1953852996);
				return true;
		}
		return false;
	}

	public static boolean allThreeCoordinates(final Player player) {
		return player.trackerGnome1 && player.trackerGnome2 && player.trackerGnome3;
	}

	public boolean sendDialogue(final Player player, int id, int chatId, int optionId, int npcChatId) {
		switch (id) { //Npc ID
			case KING_BOLREN:
				switch (player.getQuestStage(33)) { //Dialogue per stage
					case ARE_YOU_FUCKING_KIDDING_ME:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Please hurry and get the other orbs!", SAD);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("O-okay.", DISTRESSED);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case 0: //Starting the quest
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Hello.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Well hello stranger. My name's Bolren, I'm the king of", "the tree gnomes.", HAPPY);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("I'm surprised you made it in, maybe I made the maze", "too easy.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("Maybe.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("I'm afraid I have more serious concerns at the", "moment. Very serious.", DISTRESSED);
								return true;
							case 6:
								player.getDialogue().sendOption("I'll leave you to it then.", "Can I help at all?");
								return true;
							case 7:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("I'll leave you to it then.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("Can I help at all?", CONTENT);
										return true;
								}
								return false;
							case 8:
								player.getDialogue().sendNpcChat("I'm glad you asked.", HAPPY);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("The truth is my people are in grave danger.  We have", "always been protected by the Spirit Tree. No creature", "of dark can harm us while its three orbs are in place.", DISTRESSED);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("We are not a violent race, be we fight when we must.", "Many gnomes have fallen battling the dark forces of", "Khazard to the North.", DISTRESSED);
								return true;
							case 11:
								player.getDialogue().sendNpcChat("We became desperate, so we took one orb of protection", "to the battlefield. It was a foolish move.", DISTRESSED);
								return true;
							case 12:
								player.getDialogue().sendNpcChat("Khazard troops seized the orb. Now we are completely", "defenseless.", DISTRESSED);
								return true;
							case 13:
								player.getDialogue().sendPlayerChat("How can I help?", CONTENT);
								return true;
							case 14:
								player.getDialogue().sendNpcChat("You would be a huge benefit on the battlefield. If you", "would go there and try to retrieve the orb, my people", "and I will be forever grateful.", CONTENT);
								return true;
							case 15:
								player.getDialogue().sendOption("I would be glad to help.", "I'm sorry but I won't be involved.");
								return true;
							case 16:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("I would be glad to help.", CONTENT);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("I'm sorry but I won't get involved.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
								}
							case 17:
								player.getDialogue().sendNpcChat("Thank you. The battlefield is to the north of the maze.", "Commander Montai will inform you of their current", "situation.", CONTENT);
								return true;
							case 18:
								player.getDialogue().sendNpcChat("That is, if he's still alive.", DISTRESSED);
								return true;
							case 19:
								player.getDialogue().sendNpcChat("My assistant shall guide you out. Good luck friend, try", "your best to return the orb.", CONTENT);
								return true;
							case 20:
								player.getDialogue().sendStatement("Elkoy begins to lead you out of the Tree Gnome Village...");
								return true;
							case 21:
								player.getDialogue().endDialogue();
								QuestHandler.startQuest(player, 33);
								player.fadeTeleport(OUTSIDE_VILLAGE);
								return true;
						}
						return false;
					case ORB_1:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getInventory().playerHasItem(FIRST_ORB)) {
									player.getDialogue().sendPlayerChat("I have the orb!", HAPPY);
									return true;
								} else {
									player.getDialogue().sendPlayerChat("Shit... Where'd I put that orb...", DISTRESSED);
									player.getDialogue().endDialogue();
									return true;
								}
							case 2:
								player.getDialogue().sendNpcChat("Oh my... The misery, the horror!", SAD);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("King Bolren, are you OK?", DISTRESSED);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Thank you traveller, but it's too late. We're all doomed.", SAD);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("What happened?", DISTRESSED);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("They came in the night. I don't know how many, but", "enough.", SAD);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("Who?", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("Khazard troops. They slaughtered anyone who got in", "their way. Women, children, my wife.", SAD);
								return true;
							case 9:
								player.getDialogue().sendPlayerChat("I'm sorry.", SAD);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("They took the other orbs, now we are defenseless.", SAD);
								return true;
							case 11:
								player.getDialogue().sendPlayerChat("Where did they take them?", CONTENT);
								return true;
							case 12:
								player.getDialogue().sendNpcChat("They headed north of the stronghold. A warlord carries", "the orbs.", SAD);
								return true;
							case 13:
								player.getDialogue().sendOption("I will find the warlord and bring back the orbs.", "I'm sorry but I can't help.");
								return true;
							case 14:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("I will find the warlord and bring back the orbs.", CONTENT);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("I'm sorry but I can't help.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
								}
							case 15:
								player.getDialogue().sendNpcChat("You are brave, but this task will be tough even for you.", "I wish you the best of luck. Once again you are our", "only hope.", SAD);
								return true;
							case 16:
								player.getDialogue().sendNpcChat("I will safeguard this orb and pray for your safe return.", "Elkoy can guide you out.", CONTENT);
								player.getInventory().removeItem(new Item(FIRST_ORB));
								player.setQuestStage(33, ARE_YOU_FUCKING_KIDDING_ME);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case KICK_WARLORDS_ASS:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getInventory().playerHasItem(OTHER_ORBS)) {
									player.getDialogue().sendPlayerChat("Bolren, I have returned.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendStatement("King Bolren hides his tear stained face and says something", "muffled about the orbs.");
									player.getDialogue().endDialogue();
									return true;
								}
							case 2:
								player.getDialogue().sendNpcChat("You made it back! Do you have the orbs?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("I have them right here.", HAPPY);
								player.getInventory().removeItem(new Item(OTHER_ORBS));
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Hooray! You're amazing. I didn't think it was possible", "but you've saved us.", HAPPY);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Once the orbs are replaced we will be safe once more.", "We must begin the ceremony immediately.", HAPPY);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("What does the ceremony involve?", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("The spirit tree has looked over us for centuries. Now", "we must pay our respects.", HAPPY);
								return true;
							case 8:
								player.getDialogue().sendStatement("The gnomes begin to chant, meanwhile, King Bolren releases the orbs", "of protection out in front of him.");
								CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
									int counter = 1;

									@Override
									public void execute(CycleEventContainer b) {
										if (counter >= 5 || player.getDialogue().getChatId() > 8) {
											b.stop();
										}
										if (counter == 1) {
											for (Npc gnome : World.getNpcs()) {
												if (gnome == null) {
													continue;
												}
												if ((gnome.getNpcId() == GNOME_CHILD_1 || gnome.getNpcId() == GNOME_CHILD_2 || gnome.getNpcId() == GNOME_CHILD_3) && gnome.getPosition().getY() < 3175) {
													gnome.getUpdateFlags().setForceChatMessage("Sak a lak a hak a lak a sakka lak!");
												}
											}
										}
										if (counter == 2) {
											for (Npc gnome : World.getNpcs()) {
												if (gnome == null) {
													continue;
												}
												if ((gnome.getNpcId() == GNOME_CHILD_1 || gnome.getNpcId() == GNOME_CHILD_2 || gnome.getNpcId() == GNOME_CHILD_3) && gnome.getPosition().getY() < 3175) {
													gnome.getUpdateFlags().setForceChatMessage("Rak tak a lak a sahk sahk sahk!");
												}
											}
										}
										if (counter == 3) {
											for (Npc gnome : World.getNpcs()) {
												if (gnome == null) {
													continue;
												}
												if ((gnome.getNpcId() == GNOME_CHILD_1 || gnome.getNpcId() == GNOME_CHILD_2 || gnome.getNpcId() == GNOME_CHILD_3) && gnome.getPosition().getY() < 3175) {
													gnome.getUpdateFlags().setForceChatMessage("Allahu Akbar! Allahu Akbar!");
												}
											}
										}
										if (counter == 4) {
											for (Npc gnome : World.getNpcs()) {
												if (gnome == null) {
													continue;
												}
												if ((gnome.getNpcId() == GNOME_CHILD_1 || gnome.getNpcId() == GNOME_CHILD_2 || gnome.getNpcId() == GNOME_CHILD_3) && gnome.getPosition().getY() < 3175) {
													gnome.getUpdateFlags().setForceChatMessage("Allahu Akbar!");
												}
											}
										}
										counter++;
									}

									@Override
									public void stop() {
									}
								}, 3);
								return true;
							case 9:
								player.getDialogue().sendStatement("The orbs of protection come to rest gently in the branches of the", "ancient spirit tree.");
								World.createStaticGraphic(new Graphic(79, 0), new Position(2542, 3170, 0));
								return true;
							case 10:
								player.getDialogue().sendNpcChat("Now at last my people are safe once more. We can live", "in peace again.", HAPPY);
								return true;
							case 11:
								player.getDialogue().sendPlayerChat("I'm glad I could help.", CONTENT);
								return true;
							case 12:
								player.getDialogue().sendNpcChat("You are modest, brave traveler.", CONTENT);
								return true;
							case 13:
								player.getDialogue().sendNpcChat("Please, for your efforts take this amulet. It's made", "from the same sacred stone as the orbs of protection. It", "will help keep you safe on your journeys.", CONTENT);
								return true;
							case 14:
								player.getDialogue().sendPlayerChat("Thank you Bolren.", CONTENT);
								return true;
							case 15:
								player.getDialogue().sendNpcChat("The tree has many other powers, some of which I", "cannot reveal. As a friend of the gnome people, I can", "now allow you to use the tree's magic to teleport to", "other trees grown from related seeeds.", CONTENT);
								return true;
							case 16:
								QuestHandler.completeQuest(player, 33);
								player.getDialogue().dontCloseInterface();
								return true;
						}
						return false;
				}
				return false;
			case COMMANDER_MONTAI:
				switch (player.getQuestStage(33)) {
					case QUEST_STARTED:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Hello.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Hello traveller, are you here to help or just to watch?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("I've been sent by King Bolren to retrieve the orb of", "protection.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Excellent, we need all the help we can get.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("I'm Commander Montai. The orb is in the Khazard", "Stronghold to the north, but until we weaken their", "defenses we can't get close.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("What can I do?", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("First we need to strengthen our own defenses. We", "desperately need wood to make more battlements: once", "the battlements are gone it's all over. Six loads of", "normal logs should do it.", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendOption("Ok, I'll gather some wood.", "Sorry, I no longer want to be involved.");
								return true;
							case 9:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("Ok, I'll gather some wood.", CONTENT);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("Sorry, I no longer want to be involved.", CONTENT);
										player.getDialogue().endDialogue();
										player.setQuestStage(33, 0);
										return true;
								}
							case 10:
								player.getDialogue().sendNpcChat("Please be as quick as you can, I don't know how much", "longer we can hold out.", CONTENT);
								player.setQuestStage(33, GOT_WOOD);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case GOT_WOOD:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Hello.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Hello again, we're still desperate for wood soldier.", CONTENT);
								return true;
							case 3:
								if (player.getInventory().playerHasItem(LOGS, 6)) {
									player.getDialogue().sendPlayerChat("I have some here.", CONTENT);
									return true;
								}
								player.getDialogue().sendPlayerChat("I still don't have them all.", SAD);
								player.getDialogue().endDialogue();
								return true;
							case 4:
								player.getDialogue().sendNpcChat("That's excellent, now we can make more defensive", "battlements. Allow me to tell you about our", "next phase of attack.", CONTENT);
								player.setQuestStage(33, GAVE_MONTAI_WOOD);
								player.getInventory().removeItem(new Item(LOGS, 6));
								player.getDialogue().setNextChatId(1);
								return true;
						}
					case GAVE_MONTAI_WOOD:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("We need to permanently damage the Khazard stronghold.", "This is an easy task with the ballista in working order.", "However...", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("From this distance we can't get an accurate enough", "shot. We need the correct coordinates of the", "stronghold for a direct hit. I've sent three tracker", "gnomes to gather them.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("Have they returned?", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("I'm afraid not, and we're running out of time. I need", "you to go into the heart of the battlefield, find the", "trackers, and bring back the coordinates.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Do you think you can do that?", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendOption("No, I've had enough of your battle.", "I'll do my best.");
								return true;
							case 7:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("No, I've had enough of your battle.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("I'll do my best.", CONTENT);
										return true;
								}
							case 8:
								player.getDialogue().sendNpcChat("Thank you, you're braver than most.", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("I don't know how long I will be able to hold out. Once", "you have the coordinates come back and fire the ballista", "right into those monsters.", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("If you can retrieve the orb and bring it safely back to", "my people, none of the blood spilled on this field will be", "in vain.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case GET_COORDINATES:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Have you located the tracker gnomes and", "fired that ballista yet?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Sir, no sir!", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Since when did you join our gnome army?", "Go and fire that ballista adventurer.", LAUGHING);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case FIRIN_MA_LAZAH:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("I found the tracker gnomes and fired the ballista.", "It seemed to do a lot of damage to the Khazard fort.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Excellent adventurer! Go and retrieve the orb from", "the fort!", HAPPY);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
				}
				return false;
			case TRACKER_GNOME_1:
				switch (player.getQuestStage(33)) {
					case GAVE_MONTAI_WOOD:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Do you know the coordinates of the Khazard", "Stronghold?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("I managed to get one, although it wasn't easy.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendStatement("The gnome tells you the height coordinate.");
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("Well done.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("The other two tracker gnomes should have the other", "coordinates if they're still alive.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("Ok, take care.", CONTENT);
								player.trackerGnome1 = true;
								if (allThreeCoordinates(player)) {
									player.setQuestStage(33, GET_COORDINATES);
								}
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
				}
				return false;
			case TRACKER_GNOME_2:
				switch (player.getQuestStage(33)) {
					case GAVE_MONTAI_WOOD:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Are you OK?", DISTRESSED);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("They caught me spying on the stronghold. They beat", "and tortured me.", DISTRESSED);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("But I didn't crack. I told them nothing. They can't", "break me!", HAPPY);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("I'm sorry little man.", SAD);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Don't be. I have the position of the stronghold!", HAPPY);
								return true;
							case 6:
								player.getDialogue().sendStatement("The gnome tells you the y-coordinate.");
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("Well done.", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("Now leave before they find you and all is lost.", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendPlayerChat("Hang in there.", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("Go!", CONTENT);
								player.trackerGnome2 = true;
								if (allThreeCoordinates(player)) {
									player.setQuestStage(33, GET_COORDINATES);
								}
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
				}
				return false;
			case TRACKER_GNOME_3:
				switch (player.getQuestStage(33)) {
					case GAVE_MONTAI_WOOD:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Are you okay?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Ok? Who's okay? Not me! Hee hee!", LAUGHING);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("What's wrong?", DISTRESSED);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("You can't see me, no one can. Monsters, demons,", "they're all around me!", LAUGHING);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("What do you mean?", DISTRESSED);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("They're dancing, all of them! Hee hee.", LAUGHING);
								return true;
							case 7:
								player.getDialogue().sendStatement("He's clearly lost the plot.");
								return true;
							case 8:
								player.getDialogue().sendPlayerChat("Do you have the coordinate for the Khazard", "Stronghold?", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("Who holds the stronghold?", LAUGHING);
								return true;
							case 10:
								player.getDialogue().sendPlayerChat("What?", CONTENT);
								return true;
							case 11:
								if (player.getQuestVars().getBallistaIndex() == -1) {
									int ballistaIndex = Misc.randomMinusOne(crazyRants.length);
									player.getDialogue().sendNpcChat(crazyRants[ballistaIndex] + ", ha ha ha.", LAUGHING);
									player.getQuestVars().setBallistaIndex(ballistaIndex);
								} else {
									player.getDialogue().sendNpcChat(crazyRants[player.getQuestVars().getBallistaIndex()] + ", ha ha ha.", LAUGHING);
								}
								return true;
							case 12:
								player.getDialogue().sendPlayerChat("You're mad.", CONTENT);
								return true;
							case 13:
								player.getDialogue().sendNpcChat("Dance with me and Khazard's men are beat.", LAUGHING);
								return true;
							case 14:
								player.getDialogue().sendStatement("The toll of war has affected his mind.");
								return true;
							case 15:
								player.getDialogue().sendPlayerChat("I'll pray for you little man.", SAD);
								return true;
							case 16:
								player.getDialogue().sendNpcChat("All day we pray in the hay, hee hee.", LAUGHING);
								player.trackerGnome3 = true;
								if (allThreeCoordinates(player)) {
									player.setQuestStage(33, GET_COORDINATES);
								}
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
				}
				return false;
			case KHAZARD_WARLORD:
				switch (player.getQuestStage(33)) {
					case ARE_YOU_FUCKING_KIDDING_ME:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("You there, stop!", ANGRY_1);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Go back to your pesky 'little' friends.", LAUGHING);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("I've come for the orbs.", ANGRY_1);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("You're out of your depth traveler. These orbs are part", "of a much larger picture.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("They're stolen goods, now give them here!", ANGRY_1);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("Ha, you really think you stand a chance? I'll crush you!", LAUGHING);
								return true;
							case 7:
								player.getDialogue().endDialogue();
								CombatManager.attack(World.getNpcs()[World.getNpcIndex(KHAZARD_WARLORD)], player);
								return true;
						}
						return false;
				}
				return false;
			case ELKOY_INSIDE:
				switch (player.getDialogue().getChatId()) {
					case 1:
						if (player.getQuestStage(33) >= QUEST_STARTED) {
							player.getDialogue().sendPlayerChat("Hello Elkoy.", CONTENT);
							return true;
						}
						return false;
					case 2:
						player.getDialogue().sendNpcChat("Would you like me to show you through the maze?", CONTENT);
						return true;
					case 3:
						player.getDialogue().sendOption("Yes please.", "Not right now.");
						return true;
					case 4:
						switch (optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Yes please.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Not right now.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
					case 5:
						player.getDialogue().sendNpcChat("Ok then, follow me.", CONTENT);
						return true;
					case 6:
						player.fadeTeleport(OUTSIDE_VILLAGE);
						player.getDialogue().endDialogue();
						return true;
				}
				return false;
			case ELKOY_OUTSIDE:
				switch (player.getDialogue().getChatId()) {
					case 1:
						if (player.getQuestStage(33) >= QUEST_STARTED) {
							player.getDialogue().sendPlayerChat("Hello Elkoy.", CONTENT);
							return true;
						}
						return false;
					case 2:
						player.getDialogue().sendNpcChat("Would you like me to show you through the maze?", CONTENT);
						return true;
					case 3:
						player.getDialogue().sendOption("Yes please.", "Not right now.");
						return true;
					case 4:
						switch (optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Yes please.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Not right now.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
					case 5:
						player.getDialogue().sendNpcChat("Ok then, follow me.", CONTENT);
						return true;
					case 6:
						player.fadeTeleport(INSIDE_VILLAGE);
						player.getDialogue().endDialogue();
						return true;
				}
				return false;
			case 1953852996:
				switch (player.getDialogue().getChatId()) {
					case 1:
						if (player.getQuestStage(33) < GET_COORDINATES && player.getQuestStage(33) >= GAVE_MONTAI_WOOD) {
							player.getDialogue().sendPlayerChat("I need to get the coordinates from the", "tracker gnomes first", CONTENT);
							player.getDialogue().endDialogue();
							return true;
						} else if (player.getQuestStage(33) == GET_COORDINATES) {
							player.getDialogue().sendStatement("You adjust the height and Y coordinates by the information given", "from the other tracker gnomes.");
							return true;
						} else if (player.getQuestStage(33) > GET_COORDINATES) {
							player.getDialogue().sendPlayerChat("I've already destroyed their walls!", HAPPY);
							player.getDialogue().endDialogue();
							return true;
						}
						player.getDialogue().sendPlayerChat("I shouldn't interfere with the war.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					case 2:
						player.getDialogue().sendPlayerChat("The crazy tracker gnome was a bit vague about the X", "coordinate! What could it be?", CONTENT);
						return true;
					case 3:
						player.getDialogue().sendOption("0001", "0002", "0003", "0004");
						return true;
					case 4:
						if (player.getQuestVars().getBallistaIndex() == (optionId - 1) || (player.getQuestVars().getBallistaIndex() == 4 && optionId == 4)) {
							player.getDialogue().sendStatement("The huge spear files through the air and screams down directly into", "the Khazard Stronghold. A deafening crash echoes over the battlefield", "as most of the fort is reduced to rubble.");
							player.getActionSender().animateObject(2508, 3210, 0, 2328);
							return true;
						} else {
							player.getDialogue().sendStatement("The huge spear flies through the air and misses it's mark.", "Seems you'll have to try again.");
							player.getDialogue().endDialogue();
							return true;
						}
					case 5:
						player.getDialogue().sendPlayerChat("Well, it seems to have worked. I should try and", "get inside their fort now.", CONTENT);
						player.setQuestStage(33, FIRIN_MA_LAZAH);
						player.getDialogue().endDialogue();
						return true;
				}
				return false;
		}
		return false;
	}

}
