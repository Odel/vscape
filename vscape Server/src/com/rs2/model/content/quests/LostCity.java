package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_2;
import static com.rs2.model.content.dialogue.Dialogues.ANNOYED;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class LostCity implements Quest {

	public static final int QUEST_STARTED = 1;
	public static final int ENTRANA = 2;
	public static final int DRAMEN_STAFF = 3;
	public static final int QUEST_COMPLETE = 4;

	public int dialogueStage = 0;
	private int reward[][] = {};
	private int expReward[][] = {};

	private static final int questPointReward = 3;

	public int getQuestID() {
		return 14;
	}

	public String getQuestName() {
		return "Lost City";
	}

	public String getQuestSaveName() {
		return "lost-city";
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
		player.getActionSender().sendString("3 Quest Points,", 12150);
		player.getActionSender().sendString("Access to Zanaris", 12151);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), 7367);
	}

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if (questStage == QUEST_STARTED) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start this quest, find the camp in Lumbridge Swamp.", 8147);

			player.getActionSender().sendString("The Warrior let slip the camp is searching for Zanaris.", 8149);
			player.getActionSender().sendString("He mentioned a leprechaun and looked at a tree to the west.", 8150);
		} else if (questStage == ENTRANA) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start this quest, find the camp in Lumbridge Swamp.", 8147);
			player.getActionSender().sendString("@str@" + "The Warrior let slip the camp is searching for Zanaris.", 8149);
			player.getActionSender().sendString("@str@" + "He mentioned a leprechaun and looked at a tree to the west.", 8150);

			player.getActionSender().sendString("Shamus the leprechaun told me to go to Entrana.", 8152);
			player.getActionSender().sendString("He said in the dungeon I will find the Dramen Tree.", 8153);
			player.getActionSender().sendString("A branch from the tree can be crafted into a staff.", 8154);
		} else if (questStage == DRAMEN_STAFF) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start this quest, find the camp in Lumbridge Swamp.", 8147);
			player.getActionSender().sendString("@str@" + "The Warrior let slip the camp is searching for Zanaris.", 8149);
			player.getActionSender().sendString("@str@" + "He mentioned a leprechaun and looked at a tree to the west.", 8150);
			player.getActionSender().sendString("@str@" + "Shamus the leprechaun told me to go to Entrana.", 8152);
			player.getActionSender().sendString("@str@" + "He said in the dungeon I will find the Dramen Tree.", 8153);
			player.getActionSender().sendString("@str@" + "A branch from the tree can be crafted into a staff.", 8154);

			player.getActionSender().sendString("You need to take your staff and enter the old shack.", 8156);
			player.getActionSender().sendString("Shamus said it was southeast of him, in the swamps.", 8157);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start this quest, find the camp in Lumbridge Swamp.", 8147);
			player.getActionSender().sendString("@str@" + "The Warrior let slip the camp is searching for Zanaris.", 8149);
			player.getActionSender().sendString("@str@" + "He mentioned a leprechaun and looked at a tree to the west.", 8150);
			player.getActionSender().sendString("@str@" + "Shamus the leprechaun told me to go to Entrana.", 8152);
			player.getActionSender().sendString("@str@" + "He said in the dungeon I will find the Dramen Tree.", 8153);
			player.getActionSender().sendString("@str@" + "A branch from the tree can be crafted into a staff.", 8154);
			player.getActionSender().sendString("@str@" + "You need to take your staff and enter the old shack.", 8156);
			player.getActionSender().sendString("@str@" + "Shamus said it was southeast of him, in the swamps.", 8157);

			player.getActionSender().sendString("@red@" + "You have completed this quest!", 8159);
		} else {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("To start this quest, find the camp in Lumbridge Swamp.", 8147);
			player.getActionSender().sendString("You must be able to defeat a level 101 tree Spirit...", 8149);
			player.getActionSender().sendString("with little to no armor or weapons.", 8150);
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 7367);
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
			player.getActionSender().sendString("@yel@" + getQuestName(), 7367);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 7367);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 7367);
		}

	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString("To start this quest, find the camp in Lumbridge Swamp.", 8147);
		player.getActionSender().sendString("You must be able to defeat a level 101 tree Spirit...", 8149);
		player.getActionSender().sendString("with little to no armor or weapons.", 8150);
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public static boolean isShamusSpawned() {
		for (Npc npc : World.getNpcs()) {
			if (npc == null) {
				continue;
			}
			if (npc.getNpcId() == 654) {
				return true;
			}
		}
		return false;
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

	public boolean doObjectClicking(Player player, int object, int x, int y) {
		switch (object) {
			case 2409:
				if (player.getQuestStage(14) == 1 && x == 3138 && y == 3212) {
					player.getDialogue().sendStatement("You shake the tree and an angry leprechaun falls out.");
					if (!isShamusSpawned()) {
						NpcLoader.newNPC(654, 3140, 3214, 0, 2);
					}
					for (Npc npc : World.getNpcs()) {
						if (npc == null) {
							continue;
						}
						if (npc.getNpcId() == 654) {
							npc.setInteractingEntity(player);
						}
					}
					Dialogues.startDialogue(player, 654);
					return true;
				}
			case 2406: // zanaris shed door
				if (player.getEquipment().getId(Constants.WEAPON) == 772) {
					if (!QuestHandler.questCompleted(player, 14)) {
						player.setQuestStage(14, 4);
						QuestHandler.getQuests()[14].completeQuest(player);
						return true;
					}
					player.fadeTeleport(new Position(2452, 4473, 0));
					player.getActionSender().sendMessage("You are suddenly teleported away.");
					return true;
				} else {
					player.getActionSender().sendMessage("The door seems to be locked.");
					return true;
				}
			case 12045:
			case 12047: //market door
				if (player.getInventory().playerHasItem(1601) && player.getPosition().getX() < 2470 && player.getPosition().getY() > 4435) {
					player.getActionSender().sendMessage("The magic door takes your cut diamond.");
					player.getInventory().removeItem(new Item(1601));
					player.getActionSender().walkTo(player.getPosition().getX() < 2470 ? 1 : -1, 0, true);
					player.getActionSender().walkThroughDoubleDoor(12045, 12047, 2469, 4438, 2469, 4437, 0);
					return true;
				} else if (player.getInventory().playerHasItem(1601) && player.getPosition().getY() > 4433 && player.getPosition().getX() < 2467) {
					player.getActionSender().sendMessage("The magic door takes your cut diamond.");
					player.getInventory().removeItem(new Item(1601));
					player.getActionSender().walkTo(0, player.getPosition().getY() > 4433 ? -1 : 1, true);
					player.getActionSender().walkThroughDoubleDoor(12045, 12047, 2466, 4434, 2465, 4434, 0);
					return true;
				} else if (player.getPosition().getX() >= 2470 && player.getPosition().getY() > 4436) {
					player.getActionSender().walkTo(player.getPosition().getX() < 2470 ? 1 : -1, 0, true);
					player.getActionSender().walkThroughDoubleDoor(12045, 12047, 2469, 4438, 2469, 4437, 0);
					return true;
				} else if (player.getPosition().getX() < 4467 && player.getPosition().getY() < 4434) {
					player.getActionSender().walkTo(0, player.getPosition().getY() > 4433 ? -1 : 1, true);
					player.getActionSender().walkThroughDoubleDoor(12045, 12047, 2466, 4434, 2465, 4434, 0);
					return true;
				} else {
					player.getDialogue().sendStatement("The door demands a cut diamond for entry to the marketplace.");
					return true;
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

	public static boolean isWeapon(int id) {
		switch (id) {
			case 1305: //d long
			case 1215: //dragon dagger
			case 1231: //ddp
			case 5680: //ddp+
			case 5698: //ddp++
				return true;
		}
		return false;
	}

	public boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
		switch (id) {
			case 654: //shamus leprechaun
				switch (player.getDialogue().getChatId()) {
					case 1:
						if (player.getQuestStage(14) == 1) {
							player.getDialogue().sendNpcChat("What'd you do that for... Me poor head...", SAD);
							return true;
						}
						return false;
					case 2:
						player.getDialogue().sendPlayerChat("Tell me how to get to Zanaris.", CONTENT);
						return true;
					case 3:
						player.getDialogue().sendNpcChat("Well, oi, if I hadn't hit me head so hard...", ANNOYED);
						return true;
					case 4:
						player.getDialogue().sendPlayerChat("Tell me, or you'll regret it.", ANGRY_1);
						return true;
					case 5:
						player.getDialogue().sendNpcChat("Fine, fine. There's an old shack southeast of here.", "If you enter it holding a Dramen Staff...", "You'll find yourself in Zanaris.", CONTENT);
						return true;
					case 6:
						player.getDialogue().sendPlayerChat("And you know how to get this staff?", CONTENT);
						return true;
					case 7:
						player.getDialogue().sendNpcChat("Yes, yes. Ye will need to head to Entrana.", "Bring nothing dangerous, however!", "The Entrana monks are picky.", CONTENT);
						return true;
					case 8:
						player.getDialogue().sendPlayerChat("And once I'm there...?", CONTENT);
						return true;
					case 9:
						player.getDialogue().sendNpcChat("Head to the dungeon on the north of the island.", "Inside you will find the Dramen Tree.", "Chop a branch and craft the branch into a staff!", CONTENT);
						return true;
					case 10:
						player.getDialogue().sendStatement("Shamus snickers to himself.");
						return true;
					case 11:
						player.getDialogue().sendPlayerChat("What's so funny short man?", "I'll put you back up in that tree!", CONTENT);
						return true;
					case 12:
						player.getDialogue().sendNpcChat("Oh nothing, just be careful of the Tree Spirit!", LAUGHING);
						return true;
					case 13:
						player.getDialogue().sendPlayerChat("Fine. Thanks for your help.", "I'll be going now.", ANNOYED);
						return true;
					case 14:
						player.getDialogue().sendNpcChat("Good luck!", LAUGHING);
						player.getDialogue().endDialogue();
						player.setQuestStage(14, 2);
						for (Npc npc : World.getNpcs()) {
							if (npc == null) {
								continue;
							}
							if (npc.getNpcId() == 654) {
								NpcLoader.destroyNpc(npc);
							}
						}
						return true;

				}
			case 649:
			case 651:
			case 652: //camp memebers
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendPlayerChat("What are you doing out here?", CONTENT);
						return true;
					case 2:
						player.getDialogue().sendNpcChat("Nothing! None of your business!", ANNOYED);
						return true;
				}
				return false;
			case 650: //warrior
				switch (player.getQuestStage(14)) {
					case 0: //start
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("What are you doing out here?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Nothing! None of your business!", ANNOYED);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("Sure doesn't look like nothing...", "...what do you plan to do with that tiny axe?", LAUGHING);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("BOY, THE LIKES OF YOU WOULD NEVER,", "NEVER FIND ZANARIS!", ANGRY_2);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("Oh? Who is Zanaris exactly?", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendStatement("The warrior mutters angrily to himself.");
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("Looks like you don't know where it is either.", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("You had better watch your step, boy!", "You've really angered me now!", "A young'un like you could never handle", "that cheeky leprechaun!", ANGRY_2);
								return true;
							case 9:
								player.getDialogue().sendPlayerChat("What leprechaun?", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("NOTHING!", ANGRY_1);
								return true;
							case 11:
								player.getDialogue().sendStatement("The Warrior mutters about your woodcutting level.", "All the while glancing towards a tree to the west.");
								player.getDialogue().endDialogue();
								player.setQuestStage(14, 1);
								QuestHandler.getQuests()[14].startQuest(player);
								return true;
						}
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
