package com.rs2.model.content.quests;

import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.*;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.Skill;
import com.rs2.util.Misc;

public class PriestInPeril implements Quest {

	public static final int QUEST_STARTED = 1;
	public static final int KILL_GUARDIAN = 2;
	public static final int TELL_ROALD = 3;
	public static final int BACK_TO_TEMPLE = 4;
	public static final int FREE_DREZEL = 5;
	public static final int GATE_UNLOCKED = 6;
	public static final int KEY_SWAPPED = 7;
	public static final int CELL_DOOR_UNLOCKED_NOT_SAFE = 8;
	public static final int CELL_DOOR_UNLOCKED_SAFE = 9;
	public static final int ESSENCE = 10;
	public static final int THROUGH_BARRIER = 11;
	public static final int QUEST_COMPLETE = 12;

	public static final int[][] commonDrops = {{1325, 1}, {1353, 1}, {995, 10}, {995, 100}, {995, 120}, {995, 222}, {995, 364}, {958, 1}, {6814, 1}, {245, 1}, {2139, 5}, {2137, 5}, {2132, 2}, {229, 1}};
	public static final int[][] unCommonDrops = {{1157, 1}, {1109, 1}, {1181, 1}, {207, 1}};
	public static final int WOLFBANE = 2952;
	public static final int BUCKET_OF_MURKY_WATER = 2953;
	public static final int BUCKET_OF_BLESSED_WATER = 2954;
	public static final int PURE_ESSENCE = 7936;
	public static final int RUNE_ESSENCE = 1436;
	public static final int BUCKET = 1925;
	public static final int GOLDEN_KEY = 2944;
	public static final int IRON_KEY = 2945;
	public static final int WOLF_BONES = 2859;
	public static final int RUNE_MED = 1147;
	public static final int SHIELD_LEFT_HALF = 2366;

	public static final int TEMPLE_DOOR_1 = 3489;
	public static final int TEMPLE_DOOR_2 = 3490;
	public static final int GUARDIAN_GATE = 3444;
	public static final int BARRIER_GATE = 3445;
	public static final int BARRIER = 3443;
	public static final int CELL_DOOR = 3463;
	public static final int WELL = 3485;
	public static final int MONUMENT = 3498;
	public static final int COFFIN = 3480;
	public static final int COFFIN2 = 3463;

	public static final Position TEMPLE_ENTRANCE = new Position(3408, 3489, 0);
	public static final Position KING_ROALD_POS = new Position(3222, 3474, 0);

	public static final int KING_ROALD = 648;
	public static final int DREZEL = 1048;
	public static final int DREZEL_2 = 1049;
	public static final int MONK_OF_ZAMORAK = 1046;
	public static final int TEMPLE_GUARDIAN = 1047;

	public int dialogueStage = 0;
	private int reward[][] = {
		{WOLFBANE, 1}
	};
	private int expReward[][] = {
		{Skill.PRAYER, 1406},};

	private static final int questPointReward = 1;

	public int getQuestID() {
		return 23;
	}

	public String getQuestName() {
		return "Priest in Peril";
	}

	public String getQuestSaveName() {
		return "priest-peril";
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
		player.getActionSender().sendItemOnInterface(995, 200, 12142);
		player.getActionSender().sendString("You have completed: " + getQuestName(), 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("3163.5 Prayer XP", 12151);
		player.getActionSender().sendString("Wolfbane dagger", 12152);
		player.getActionSender().sendString("Route to Canifis", 12153);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), 8115);
	}

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if (questStage == QUEST_STARTED) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to King Roald in the Varrock Palace to begin this quest.", 8147);

			player.getActionSender().sendString("King Roald told me to check on the priest Drezel.", 8149);
			player.getActionSender().sendString("He said I can find him in the temple towards Morytania.", 8150);
		} else if (questStage == KILL_GUARDIAN) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to King Roald in the Varrock Palace to begin this quest.", 8147);
			player.getActionSender().sendString("@str@" + "King Roald told me to check on the priest Drezel.", 8149);
			player.getActionSender().sendString("@str@" + "He said I can find him in the temple towards Morytania.", 8150);

			player.getActionSender().sendString("A man, who I assume is Drezel, inside the temple asked me", 8152);
			player.getActionSender().sendString("to slay a big dog for him. He said it's in the mausoleum.", 8153);
		} else if (questStage == TELL_ROALD) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to King Roald in the Varrock Palace to begin this quest.", 8147);
			player.getActionSender().sendString("@str@" + "King Roald told me to check on the priest Drezel.", 8149);
			player.getActionSender().sendString("@str@" + "He said I can find him in the temple towards Morytania.", 8150);
			player.getActionSender().sendString("@str@" + "A man, who I assume is Drezel, inside the temple asked me", 8152);
			player.getActionSender().sendString("@str@" + "to slay a big dog for him. He said it's in the mausoleum.", 8153);

			player.getActionSender().sendString("I killed the dog-like thing in the mausoleum. I should", 8155);
			player.getActionSender().sendString("probably return to King Roald now, and give him an update.", 8156);
		} else if (questStage == BACK_TO_TEMPLE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to King Roald in the Varrock Palace to begin this quest.", 8147);
			player.getActionSender().sendString("@str@" + "King Roald told me to check on the priest Drezel.", 8149);
			player.getActionSender().sendString("@str@" + "He said I can find him in the temple towards Morytania.", 8150);
			player.getActionSender().sendString("@str@" + "A man, who I assume is Drezel, inside the temple asked me", 8152);
			player.getActionSender().sendString("@str@" + "to slay a big dog for him. He said it's in the mausoleum.", 8153);
			player.getActionSender().sendString("@str@" + "I killed the dog-like thing in the mausoleum. I should", 8155);
			player.getActionSender().sendString("@str@" + "probably return to King Roald now, and give him an update.", 8156);

			player.getActionSender().sendString("Turns out I was tricked: Drezel and the kingdom are in danger.", 8158);
			player.getActionSender().sendString("I need to return to the temple quickly to figure this out.", 8159);
		} else if (questStage == FREE_DREZEL || questStage == GATE_UNLOCKED) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to King Roald in the Varrock Palace to begin this quest.", 8147);
			player.getActionSender().sendString("@str@" + "King Roald told me to check on the priest Drezel.", 8149);
			player.getActionSender().sendString("@str@" + "He said I can find him in the temple towards Morytania.", 8150);
			player.getActionSender().sendString("@str@" + "A man, who I assume is Drezel, inside the temple asked me", 8152);
			player.getActionSender().sendString("@str@" + "to slay a big dog for him. He said it's in the mausoleum.", 8153);
			player.getActionSender().sendString("@str@" + "I killed the dog-like thing in the mausoleum. I should", 8155);
			player.getActionSender().sendString("@str@" + "probably return to King Roald now, and give him an update.", 8156);
			player.getActionSender().sendString("@str@" + "Turns out I was tricked: Drezel and the kingdom are in danger.", 8158);
			player.getActionSender().sendString("@str@" + "I need to return to the temple quickly to figure this out.", 8159);

			player.getActionSender().sendString("Drezel told me a very long story about the River Salve.", 8161);
			player.getActionSender().sendString("It is in danger, and I need to find a way to free Drezel.", 8162);
		} else if (questStage == KEY_SWAPPED) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to King Roald in the Varrock Palace to begin this quest.", 8147);
			player.getActionSender().sendString("@str@" + "King Roald told me to check on the priest Drezel.", 8149);
			player.getActionSender().sendString("@str@" + "He said I can find him in the temple towards Morytania.", 8150);
			player.getActionSender().sendString("@str@" + "A man, who I assume is Drezel, inside the temple asked me", 8152);
			player.getActionSender().sendString("@str@" + "to slay a big dog for him. He said it's in the mausoleum.", 8153);
			player.getActionSender().sendString("@str@" + "I killed the dog-like thing in the mausoleum. I should", 8155);
			player.getActionSender().sendString("@str@" + "probably return to King Roald now, and give him an update.", 8156);
			player.getActionSender().sendString("@str@" + "Turns out I was tricked: Drezel and the kingdom are in danger.", 8158);
			player.getActionSender().sendString("@str@" + "I need to return to the temple quickly to figure this out.", 8159);
			player.getActionSender().sendString("@str@" + "Drezel told me a very long story about the River Salve.", 8161);

			player.getActionSender().sendString("I found the key to Drezel's cell. I should unlock it.", 8163);
		} else if (questStage == CELL_DOOR_UNLOCKED_NOT_SAFE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to King Roald in the Varrock Palace to begin this quest.", 8147);
			player.getActionSender().sendString("@str@" + "King Roald told me to check on the priest Drezel.", 8149);
			player.getActionSender().sendString("@str@" + "He said I can find him in the temple towards Morytania.", 8150);
			player.getActionSender().sendString("@str@" + "A man, who I assume is Drezel, inside the temple asked me", 8152);
			player.getActionSender().sendString("@str@" + "to slay a big dog for him. He said it's in the mausoleum.", 8153);
			player.getActionSender().sendString("@str@" + "I killed the dog-like thing in the mausoleum. I should", 8155);
			player.getActionSender().sendString("@str@" + "probably return to King Roald now, and give him an update.", 8156);
			player.getActionSender().sendString("@str@" + "Turns out I was tricked: Drezel and the kingdom are in danger.", 8158);
			player.getActionSender().sendString("@str@" + "I need to return to the temple quickly to figure this out.", 8159);
			player.getActionSender().sendString("@str@" + "Drezel told me a very long story about the River Salve.", 8161);

			player.getActionSender().sendString("I unlocked Drezel's cell! However, he says it is not", 8163);
			player.getActionSender().sendString("yet safe for him to leave. The ancient vampire is still", 8164);
			player.getActionSender().sendString("right outside his cell and may kill him.", 8165);
		} else if (questStage == CELL_DOOR_UNLOCKED_SAFE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to King Roald in the Varrock Palace to begin this quest.", 8147);
			player.getActionSender().sendString("@str@" + "King Roald told me to check on the priest Drezel.", 8149);
			player.getActionSender().sendString("@str@" + "He said I can find him in the temple towards Morytania.", 8150);
			player.getActionSender().sendString("@str@" + "A man, who I assume is Drezel, inside the temple asked me", 8152);
			player.getActionSender().sendString("@str@" + "to slay a big dog for him. He said it's in the mausoleum.", 8153);
			player.getActionSender().sendString("@str@" + "I killed the dog-like thing in the mausoleum. I should", 8155);
			player.getActionSender().sendString("@str@" + "probably return to King Roald now, and give him an update.", 8156);
			player.getActionSender().sendString("@str@" + "Turns out I was tricked: Drezel and the kingdom are in danger.", 8158);
			player.getActionSender().sendString("@str@" + "I need to return to the temple quickly to figure this out.", 8159);
			player.getActionSender().sendString("@str@" + "Drezel told me a very long story about the River Salve.", 8161);

			player.getActionSender().sendString("I unlocked Drezel's cell and trapped the vampire!", 8163);
		} else if (questStage == ESSENCE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to King Roald in the Varrock Palace to begin this quest.", 8147);
			player.getActionSender().sendString("@str@" + "King Roald told me to check on the priest Drezel.", 8149);
			player.getActionSender().sendString("@str@" + "He said I can find him in the temple towards Morytania.", 8150);
			player.getActionSender().sendString("@str@" + "A man, who I assume is Drezel, inside the temple asked me", 8152);
			player.getActionSender().sendString("@str@" + "to slay a big dog for him. He said it's in the mausoleum.", 8153);
			player.getActionSender().sendString("@str@" + "I killed the dog-like thing in the mausoleum. I should", 8155);
			player.getActionSender().sendString("@str@" + "probably return to King Roald now, and give him an update.", 8156);
			player.getActionSender().sendString("@str@" + "Turns out I was tricked: Drezel and the kingdom are in danger.", 8158);
			player.getActionSender().sendString("@str@" + "I need to return to the temple quickly to figure this out.", 8159);
			player.getActionSender().sendString("@str@" + "Drezel told me a very long story about the River Salve.", 8161);
			player.getActionSender().sendString("@str@" + "I unlocked Drezel's cell and trapped the vampire!", 8163);

			player.getActionSender().sendString("I found Drezel downstairs and he said the River Salve", 8165);
			player.getActionSender().sendString("is in bad shape. After offering the possibility of Rune or", 8166);
			player.getActionSender().sendString("Pure Essence to purify the river, Drezel told me he would", 8167);
			player.getActionSender().sendString("need 25 essence to finish the task.", 8168);
		} else if (questStage == THROUGH_BARRIER) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to King Roald in the Varrock Palace to begin this quest.", 8147);
			player.getActionSender().sendString("@str@" + "King Roald told me to check on the priest Drezel.", 8149);
			player.getActionSender().sendString("@str@" + "He said I can find him in the temple towards Morytania.", 8150);
			player.getActionSender().sendString("@str@" + "A man, who I assume is Drezel, inside the temple asked me", 8152);
			player.getActionSender().sendString("@str@" + "to slay a big dog for him. He said it's in the mausoleum.", 8153);
			player.getActionSender().sendString("@str@" + "I killed the dog-like thing in the mausoleum. I should", 8155);
			player.getActionSender().sendString("@str@" + "probably return to King Roald now, and give him an update.", 8156);
			player.getActionSender().sendString("@str@" + "Turns out I was tricked: Drezel and the kingdom are in danger.", 8158);
			player.getActionSender().sendString("@str@" + "I need to return to the temple quickly to figure this out.", 8159);
			player.getActionSender().sendString("@str@" + "Drezel told me a very long story about the River Salve.", 8161);
			player.getActionSender().sendString("@str@" + "I unlocked Drezel's cell and trapped the vampire!", 8163);

			player.getActionSender().sendString("I gave Drezel the essence to purify the Salve.", 8165);
			player.getActionSender().sendString("He may have a reward for me.", 8166);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to King Roald in the Varrock Palace to begin this quest.", 8147);
			player.getActionSender().sendString("@str@" + "King Roald told me to check on the priest Drezel.", 8149);
			player.getActionSender().sendString("@str@" + "He said I can find him in the temple towards Morytania.", 8150);
			player.getActionSender().sendString("@str@" + "A man, who I assume is Drezel, inside the temple asked me", 8152);
			player.getActionSender().sendString("@str@" + "to slay a big dog for him. He said it's in the mausoleum.", 8153);
			player.getActionSender().sendString("@str@" + "I killed the dog-like thing in the mausoleum. I should", 8155);
			player.getActionSender().sendString("@str@" + "probably return to King Roald now, and give him an update.", 8156);
			player.getActionSender().sendString("@str@" + "Turns out I was tricked: Drezel and the kingdom are in danger.", 8158);
			player.getActionSender().sendString("@str@" + "I need to return to the temple quickly to figure this out.", 8159);
			player.getActionSender().sendString("@str@" + "Drezel told me a very long story about the River Salve.", 8161);
			player.getActionSender().sendString("@str@" + "I unlocked Drezel's cell and trapped the vampire.", 8163);
			player.getActionSender().sendString("@str@" + "I gave Drezel the essence to purify the Salve.", 8165);

			player.getActionSender().sendString("@red@" + "You have completed this quest!", 8167);
		} else {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("Talk to @dre@King Roald @bla@in the @dre@Varrock Palace @bla@to begin this quest.", 8147);
			player.getActionSender().sendString("@dre@Requirements:", 8148);
			player.getActionSender().sendString("-Ability to defeat a level 30 enemy", 8150);
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 8115);
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
			player.getActionSender().sendString("@yel@" + getQuestName(), 8115);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 8115);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 8115);
		}

	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString("Talk to @dre@King Roald @bla@in the @dre@Varrock Palace @bla@to begin this quest.", 8147);
		player.getActionSender().sendString("@dre@Requirements:", 8148);
		player.getActionSender().sendString("-Ability to defeat a level 30 enemy", 8150);
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void handleDeath(final Player player, Npc npc) {
		if (npc.getNpcId() == TEMPLE_GUARDIAN && player.getQuestStage(23) == 2) {
			player.setQuestStage(23, 3);
			player.getDialogue().sendPlayerChat("Well, that wasn't quite the dog I was imagining. Oh well.", CONTENT);
		}
	}

	public boolean itemHandling(final Player player, int itemId) {
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		return false;
	}

	public boolean doItemOnObject(final Player player, int object, int item) {
		switch (object) {
			case COFFIN:
				if (player.getQuestStage(23) == 8 && item == BUCKET_OF_BLESSED_WATER) {
					player.getDialogue().sendStatement("You pour the blessed water on the vampire's coffin.", "You hear muted screams of rage from inside the coffin.", "It seems safe to say the vampire is trapped now.");
					player.getInventory().replaceItemWithItem(new Item(BUCKET_OF_BLESSED_WATER), new Item(BUCKET));
					player.getUpdateFlags().sendAnimation(832);
					player.setQuestStage(23, 9);
					return true;
				}
				return false;
			case CELL_DOOR:
				switch (player.getQuestStage(23)) {
					case 7:
					case 8:
					case 9:
					case 10:
					case 11:
					case 12:
						if (item == IRON_KEY) {
							player.getActionSender().walkTo(player.getPosition().getX() < 3416 ? 1 : -1, 0, true);
							player.getActionSender().walkThroughDoor(CELL_DOOR, 3415, 3489, 2);
							if (player.getQuestStage(23) == 7) {
								player.setQuestStage(23, 8);
							}
							return true;
						}
				}
				return false;
			case MONUMENT:
				switch (player.getQuestStage(23)) {
					case 6:
					case 7:
						if (item == GOLDEN_KEY) {
							player.getDialogue().sendGiveItemNpc("You switch the two keys.", new Item(IRON_KEY));
							player.getInventory().replaceItemWithItem(new Item(GOLDEN_KEY), new Item(IRON_KEY));
							if (player.getQuestStage(23) == 6) {
								player.setQuestStage(23, 7);
							}
							return true;
						}
				}
				return false;
			case WELL:
				switch (player.getQuestStage(23)) {
					case 6:
					case 7:
					case 8:
					case 9:
						if (item == BUCKET) {
							player.getDialogue().sendStatement("You fill your bucket with water from the well");
							player.getUpdateFlags().sendAnimation(832);
							player.getInventory().replaceItemWithItem(new Item(BUCKET), new Item(BUCKET_OF_MURKY_WATER));
							return true;
						}
				}
				return false;
			case GUARDIAN_GATE:
				if (item == GOLDEN_KEY && player.getQuestStage(23) == 5) {
					player.getActionSender().sendMessage("You unlock the gate.");
					player.setQuestStage(23, 6);
					player.getActionSender().walkTo(0, player.getPosition().getY() < 9895 ? 1 : -1, true);
					player.getActionSender().walkThroughDoor(object, 3405, 9895, 0);
					return true;
				} else {
					return false;
				}
		}
		return false;
	}

	public boolean doObjectClicking(final Player player, int object, int x, int y) {
		switch (object) {
			case BARRIER:
				if (player.getQuestStage(23) == 12) {
					player.fadeTeleport(new Position(3423, 3485, 0));
					player.getActionSender().sendMessage("You step through the holy barrier and appear in Canifis.");
					return true;
				} else {
					player.getDialogue().setLastNpcTalk(DREZEL_2);
					player.getDialogue().sendNpcChat("Woah! Where are you going? I still need your help!", DISTRESSED);
					return true;
				}
			case MONUMENT:
				switch (player.getQuestStage(23)) {
					case 6:
					case 7:
						player.getDialogue().sendPlayerChat("Strange, this monument has an iron key on it...", CONTENT);
						return true;
				}
				return false;
			case TEMPLE_DOOR_1:
			case TEMPLE_DOOR_2:
				if (player.getQuestStage(23) < 4) {
					player.getActionSender().sendMessage("The doors are locked.");
					return true;
				} else {
					player.getActionSender().walkTo(player.getPosition().getX() < 3409 ? 1 : -1, 0, true);
					player.getActionSender().walkThroughDoubleDoor(TEMPLE_DOOR_1, TEMPLE_DOOR_2, 3408, 3489, 3408, 3488, 0);
					player.getActionSender().sendMessage("The doors fling wide open.");
					return true;
				}
			case CELL_DOOR:
				player.getActionSender().sendMessage("The door is securely locked shut.");
				return true;
			case GUARDIAN_GATE:
				if (player.getQuestStage(23) >= 6) {
					player.getActionSender().walkTo(0, player.getPosition().getY() < 9895 ? 1 : -1, true);
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					return true;
				} else if (player.getQuestStage(23) == 5) {
					player.getDialogue().sendPlayerChat("Hm, this door is locked, but the keyhole", "seems like it would match the key I saw", "that Zamorakian monk carrying in the temple.", CONTENT);
					return true;
				} else {
					player.getActionSender().sendMessage("The gate is firmly locked. You must progress through Priest in Peril to pass.");
					return true;
				}
			case BARRIER_GATE:
				if (player.getQuestStage(23) >= 9) {
					player.getActionSender().walkTo(player.getPosition().getX() < 3432 ? 1 : -1, 0, true);
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					return true;
				} else {
					player.getActionSender().sendMessage("The gate is firmly locked. You must progress through Priest in Peril to pass.");
					return true;
				}
		}
		return false;
	}

	public boolean doObjectSecondClick(final Player player, int object, int x, int y) {
		switch (object) {
			case TEMPLE_DOOR_1:
			case TEMPLE_DOOR_2:
				switch (player.getQuestStage(23)) {
					case 1:
						Dialogues.startDialogue(player, 10678);
						return true;
					case 2:
						Dialogues.startDialogue(player, 10679);
						return true;
					case 3:
						Dialogues.startDialogue(player, 10680);
						return true;

				}
				if (player.getQuestStage(23) >= 4) {
					player.getActionSender().walkTo(player.getPosition().getX() < 3409 ? 1 : -1, 0, true);
					player.getActionSender().walkThroughDoubleDoor(TEMPLE_DOOR_1, TEMPLE_DOOR_2, 3408, 3489, 3408, 3488, 0);
					player.getActionSender().sendMessage("The doors fling wide open.");
					return true;
				}
				return false;
			case CELL_DOOR:
				Dialogues.startDialogue(player, DREZEL);
				return true;
		}
		return false;
	}

	public static void handleDrops(Player player, Npc npc) {
		if (npc.getNpcId() >= 6006 && npc.getNpcId() < 6046) {
			GroundItem drop = new GroundItem(new Item(WOLF_BONES), player, npc.getPosition().clone());
			GroundItemManager.getManager().dropItem(drop);
			if (Misc.random(600) == 1) { //very rare
				GroundItem drop1 = new GroundItem(new Item(SHIELD_LEFT_HALF), player, npc.getPosition().clone());
				GroundItemManager.getManager().dropItem(drop1);
				return;
			} else if (Misc.random(60) == 1) { //rare
				GroundItem drop2 = new GroundItem(new Item(RUNE_MED), player, npc.getPosition().clone());
				GroundItemManager.getManager().dropItem(drop2);
				return;
			} else if (Misc.random(20) == 1) { //uncommon
				int index = Misc.randomMinusOne(unCommonDrops.length);
				Item dropped = new Item(unCommonDrops[index][0], unCommonDrops[index][1]);
				GroundItem drop3 = new GroundItem(dropped, player, npc.getPosition().clone());
				GroundItemManager.getManager().dropItem(drop3);
				return;
			} else {
				int index = Misc.randomMinusOne(commonDrops.length);
				Item dropped = new Item(commonDrops[index][0], commonDrops[index][1]);
				GroundItem drop4 = new GroundItem(dropped, player, npc.getPosition().clone());
				GroundItemManager.getManager().dropItem(drop4);
				return;
			}
		}
	}

	public boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
		switch (id) {
			case 10678: //doors to temple 1
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendStatement("You knock at the door... You hear a voice from inside." + "@dbl@ Who are you", "@dbl@and what do you want?");
						return true;
					case 2:
						player.getDialogue().sendPlayerChat("Ummm.....", CONTENT);
						return true;
					case 3:
						player.getDialogue().sendOption("Roald sent me to check on Drezel.", "Hi, I just moved in next door...", "I hear this place is of historic interest.", "The council sent me to check your pipes.");
						return true;
					case 4:
						switch (optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Roald sent me to check on Drezel.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Hi, I just moved in next door...", CONTENT);
								player.getDialogue().setNextChatId(15);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("I hear this place is of historic interest.", CONTENT);
								player.getDialogue().setNextChatId(20);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("The council sent me to check on your pipes.", CONTENT);
								player.getDialogue().setNextChatId(25);
								return true;
						}
					case 5:
						player.getDialogue().sendStatement("@dbl@(Psst... Hey... Who's Roald?) @dre@(Uh... isn't Drezel the guy", "@dre@upstairs? Oh, wait, Roald's the King of Varrock right?)@dbl@(He is???", "@dbl@Aw man... Hey, you deal with this okay?) He's coming! Wait a", "@dbl@second! @dre@Hello, my name is Drevil. @dbl@(DREZEL!) @dre@I mean, Drezel. How", "@dre@can I help?");
						return true;
					case 6:
						player.getDialogue().sendPlayerChat("Well, as I said, the King sent me to make sure", "everything's okay with you.", CONTENT);
						return true;
					case 7:
						player.getDialogue().sendStatement("@dre@And, uh, what would you do if everything wasn't okay with me?");
						return true;
					case 8:
						player.getDialogue().sendPlayerChat("I'm not sure. Ask you what help you need I suppose.", CONTENT);
						return true;
					case 9:
						player.getDialogue().sendStatement("@dre@Ah, good, well, I don't think... @dbl@(Psst... HEY!.. the dog!) @dre@OH! Yes, of", "@dre@course! Will you do me a favor adventurer?");
						return true;
					case 10:
						player.getDialogue().sendOption("Sure.", "Nope.");
						return true;
					case 11:
						switch (optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Sure. I'm a helpful person!", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Nope. I'm leaving.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
					case 12:
						player.getDialogue().sendStatement("@dre@HAHAHAHA! Really? Thanks buddy! You see that mausoleum out", "@dre@there? There's a horrible big dog underneath it that I'd like you to", "@dre@kill for me! It's been really bugging me! Barking all the time and", "@dre@stuff! Please kill it for me buddy!");
						return true;
					case 13:
						player.getDialogue().sendPlayerChat("Okey-dokey, one dead dog coming up.", CONTENT);
						player.getDialogue().endDialogue();
						player.setQuestStage(23, 2);
						return true;
					case 15:
						player.getDialogue().sendStatement("@dbl@Haha, really? Where at? The swamp? Yeah, nice try buddy.", "@dbl@Get lost.");
						player.getDialogue().endDialogue();
						return true;
					case 20:
						player.getDialogue().sendStatement("@dbl@Hmm, yes, yes, historic interest. It is quite old around here.", "@dbl@Are you here to fix up the wallpaper?");
						return true;
					case 21:
						player.getDialogue().sendPlayerChat("Erm, not exactly.", CONTENT);
						return true;
					case 22:
						player.getDialogue().sendStatement("@dbl@Ah, a shame. Nevermind then, we don't want anything to do with you.", "@dbl@Shoo!");
						player.getDialogue().endDialogue();
						return true;
					case 25:
						player.getDialogue().sendStatement("@dbl@Our pipes... (Psst, hey, do we own any pipes?) @dre@(Pipes, what the hell?)", "@dbl@(Somebody is here from the 'council') @dre@(Well, tell them to go away!)", "@dbl@Yeah, look buddy, we're not interested. Have a nice day.");
						return true;
					case 26:
						player.getDialogue().sendPlayerChat("But I need to inspect...", CONTENT);
						return true;
					case 27:
						player.getDialogue().sendStatement("@dbl@Please, just go away.");
						player.getDialogue().endDialogue();
						return true;

				}
				return false;
			case 10679: //doors to temple 2
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendStatement("You knock at the door... You hear a voice from inside....", "@dbl@Ah, yes, hello? Who is speaking please?");
						return true;
					case 2:
						player.getDialogue().sendPlayerChat("Oh, it's me again.", CONTENT);
						return true;
					case 3:
						player.getDialogue().sendStatement("@dbl@What do you want? I told you to kill the dog!");
						player.getDialogue().endDialogue();
						return true;
				}
				return false;
			case 10680: //doors to temple 3
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendStatement("You knock at the door... You hear a voice from inside....", "@dbl@Ah, yes, hello? Who is speaking please?");
						return true;
					case 2:
						player.getDialogue().sendPlayerChat("I killed that dog you wanted me to.", CONTENT);
						return true;
					case 3:
						player.getDialogue().sendStatement("You don't hear anything from inside, until...");
						return true;
					case 4:
						player.getDialogue().sendStatement("@dbl@OooOOHOOHAHAHAHAHA, AH YES! Yes yes yes yes yes yes.", "@dbl@The task we wanted you to complete! I mean, I, Drezel, wanted you", "@dbl@ to complete! PERFECT! HAHAHA!");
						return true;
					case 5:
						player.getDialogue().sendStatement("Another pause from inside...");
						return true;
					case 6:
						player.getDialogue().sendStatement("@dbl@You should, ah, go tell King Roald of the good news adventurer!", "@dbl@Yes, he will be pleased to hear that, I, Drezel, am doing fine.", "@dbl@Oh, and don't forget to mention that the pesky dog is dead! HAHAHA!");
						return true;
					case 7:
						player.getDialogue().sendPlayerChat("Erm, alright. Are you okay in there Drezel?", DISTRESSED);
						return true;
					case 8:
						player.getDialogue().sendStatement("@dbl@Oh, YES! Just fantastic, thanks to you! Scurry along to Roald now!");
						return true;
					case 9:
						player.getDialogue().sendStatement("You hear the manic cackling fade...");
						return true;
					case 10:
						player.getDialogue().sendPlayerChat("Well, that was weird. I suppose I should tell King Roald.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
				}
				return false;
			case KING_ROALD:
				switch (player.getQuestStage(23)) {
					case 0: //start
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Greetings, your majesty.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Well hello there, what do you want?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("I'm looking for a quest!", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("A quest you say? Hmm, what an odd request to make", "of the king. It's funny you should mention it though, as", "there is something you can do for me.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Are you aware of the temple east of here?", "It stands on the river Salve and guards", "the entrance to the lands of Morytania.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("No, I don't think I know it...", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("Hmm, how strange that you don't. Well anyway, it has", "been some days since last I heard from Drezel, the", "priest who lives there.", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("Be a sport and go make sure that nothing untoward", "has happened to the silly old codger for me, would you?", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendOption("Sure.", "No, that sounds boring.");
								return true;
							case 10:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("Sure. I don't have anything better to do right now.", CONTENT);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("No, that sounds boring.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
								}
							case 11:
								player.getDialogue().sendNpcChat("Many thanks adventurer! I would have sent one of my", "squires but they wanted payment for it!", CONTENT);
								return true;
							case 12:
								player.getDialogue().sendPlayerChat("Well, you know how squires are...", "I'll get right to it, your majesty.", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(23, 1);
								QuestHandler.getQuests()[23].startQuest(player);
								return true;
							case 15:
								player.getDialogue().sendStatement("You must complete Rune Mysteries to start this quest.");
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case TELL_ROALD: //dead dog
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Greetings, your majesty.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Well hello there, what do you want?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Oh, do you have news of Drezel for me?", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("Yeah, I spoke to the guys at the temple and they said", "they were being bothered by that dog in the crypt, so I", "went and killed it for them. No problem.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("YOU DID WHAT???", ANGRY_2);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("Are you mentally deficient??? That guard dog was", "protecting the route to Morytania! Without it we could", "be in severe danger of attack!", ANGRY_1);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("Did I make a mistake?", SAD);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("YES YOU DID!!!!! You need to get there right now", "and find out what is happening! Before it is too late", "for us all!", ANGRY_2);
								return true;
							case 9:
								player.getDialogue().sendPlayerChat("B-but Drezel TOLD me to...!", SAD);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("No, you absolute cretin! Obviously some fiend has done", "something to Drezel and tricked your feeble intellect", "into helping them kill that guard dog!", ANGRY_1);
								return true;
							case 11:
								player.getDialogue().sendNpcChat("You get back there and do whatever is necessary to", "safeguard my kingdom from attack, or I will see you", "beheaded for high treason!", ANGRY_1);
								return true;
							case 12:
								player.getDialogue().sendPlayerChat("Y-yes, your Highness.", DISTRESSED);
								player.getDialogue().endDialogue();
								player.setQuestStage(23, 4);
								return true;
						}
						return false;
				}
				return false;
			case 6028: //yuri orlov
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendNpcChat("They say, 'Evil prevails when good men fail to act.'", "What they ought to say is, 'Evil prevails.'", CONTENT);
						return true;
					case 2:
						player.getDialogue().sendPlayerChat("What?", CONTENT);
						player.getDialogue().endDialogue();
						return true;
				}
				return false;
			case 6026: //boris the spider
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendNpcChat("Hello, my name is Boris, what can I do for you?", CONTENT);
						return true;
					case 2:
						player.getDialogue().sendPlayerChat("The spider?", CONTENT);
						return true;
					case 3:
						player.getDialogue().sendNpcChat("Look, kid, if you're just going to waste time,", "please go away.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
				}
				return false;
			case 6043: //vera wang
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendNpcChat("Hello, my name is Vera, what can I do for you?", CONTENT);
						return true;
					case 2:
						player.getDialogue().sendPlayerChat("You make such lovely dresses.", CONTENT);
						return true;
					case 3:
						player.getDialogue().sendNpcChat("Thank you? But I'm not sure I've ever made any dresses...", CONTENT);
						player.getDialogue().endDialogue();
						return true;
				}
				return false;
			case 6027:
			case 6029:
			case 6030:
			case 6031:
			case 6032:
			case 6033:
			case 6034:
			case 6035:
			case 6036:
			case 6037:
			case 6038:
			case 6039:
			case 6040:
			case 6041:
			case 6042:
			case 6044:
			case 6045:
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendNpcChat("Hello, traveller my name is " + new Npc(id).getDefinition().getName() + ".", "what can I do for you?", CONTENT);
						return true;
					case 2:
						player.getDialogue().sendPlayerChat("Nothing, but thank you.", HAPPY);
						player.getDialogue().endDialogue();
						return true;
				}
				return false;
			case DREZEL:
				switch (player.getQuestStage(23)) {
					case BACK_TO_TEMPLE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Hello.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Oh! You do not appear to be one of those Zamorakians", "who imprisoned me here! Who are you and why are", "you here?", DISTRESSED);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("My name's " + player.getUsername() + ". King Roald sent me", "to find out what was going on at the temple. I take", "it you are Drezel?", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("That is right! Oh, praise be to Saradomin! All is not yet", "lost! I feared when those Zamorakians attacked this", "place and imprisoned...", HAPPY);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("me up here, vscape would be doomed! If they should", "manage to desecrate the holy river Slave we would be", "defenseless against Morytania!", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("How is a river a good defence then?", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("Well, it is a long tale, and I'm not sure we have time!", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendPlayerChat("Tell me anyways, I like a gamble.", "I'd like to know all the facts before acting any further.", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("Ah, Saradomin has granted you wisdom I see. Well, the", "story of the river Salve and of how it protects vscape", "is the story of this temple...", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("...and of the seven warrior priests who died here long", "ago, from whom I am descended. Once, long ago, vscape", "did not have the borders that", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendNpcChat("it currently does. This entire area, as far West as", "Varrock itself was under the control of an evil god.", "There was frequent skirmishing", CONTENT);
								return true;
							case 12:
								player.getDialogue().sendNpcChat("along the borders, as the brave heroes of Varrock", "fought to keep the evil creatures that now are trapped", "on the eastern side of the River Salve from over", "running", CONTENT);
								return true;
							case 13:
								player.getDialogue().sendNpcChat("the human encampments, who worshipped Saradomin.", "Then one day, Saradomin himself appeared to one of", "our mighty heroes, whose name has been forgotten by", "history.", CONTENT);
								return true;
							case 14:
								player.getDialogue().sendPlayerChat("Was his name Wally?", CONTENT);
								return true;
							case 15:
								player.getDialogue().sendNpcChat("What?", CONTENT);
								return true;
							case 16:
								player.getDialogue().sendPlayerChat("Nevermind, please continue.", CONTENT);
								return true;
							case 17:
								player.getDialogue().sendNpcChat("Saradomin told the warrior that if he could take the pass", "that this temple now stands in, Saradomin would use his", "power to bless this river, and make it", CONTENT);
								return true;
							case 18:
								player.getDialogue().sendNpcChat("Impassable to all creatures with evil in their hearts. This", "unknown hero grouped together all of the mightiest", "fighters whose hearts were pure", CONTENT);
								return true;
							case 19:
								player.getDialogue().sendNpcChat("that he could find, and seven of them rode here to", "make a final stand. The enemies swarmed across the", "Salve, but they did not yield.", CONTENT);
								return true;
							case 20:
								player.getDialogue().sendNpcChat("For ten days and nights they fought, never sleeping,", "never eating, fuelled by their desire to make the world a", "better place for humans to live.", CONTENT);
								return true;
							case 21:
								player.getDialogue().sendNpcChat("On the eleventh day they were to be joined by", "reinforcements from a neighboring encampment, but", "when those reinforcements arrived all they found", CONTENT);
								return true;
							case 22:
								player.getDialogue().sendNpcChat("were the bodies of these seven brave, but unknown,", "heroes, surrounded by the piles of the dead creatures of", "evil that had tried to defeat them.", CONTENT);
								return true;
							case 23:
								player.getDialogue().sendNpcChat("The men were saddened at the loss of such pure and", "mighty warriors, yet their sacrifice had not been in", "vain; for the water of the Salve", CONTENT);
								return true;
							case 24:
								player.getDialogue().sendNpcChat("had indeed been filled with the power of Saradomin, and", "the evil creatures of Morytania were trapped beyond", "the river banks forever, by their own evil.", CONTENT);
								return true;
							case 25:
								player.getDialogue().sendNpcChat("Im memory of this brave sacrifice, my ancestors built", "this temple so that the land would always be free of the", "evil creatures", CONTENT);
								return true;
							case 26:
								player.getDialogue().sendNpcChat("who wish to destroy it, and laid the bodies of those brave", "warriors in tombs of honor below this temple with", "golden gifts on the tombs as marks of respect.", CONTENT);
								return true;
							case 27:
								player.getDialogue().sendNpcChat("They also built a statue on the river mouth so that all", "who might try and cross to Misthalin from Morytania", "would know that these lands are protected", CONTENT);
								return true;
							case 28:
								player.getDialogue().sendNpcChat("by the glory of Saradomin and that good will always", "defeat evil, no matter how the odds are stacked against", "them.", HAPPY);
								return true;
							case 29:
								player.getDialogue().sendPlayerChat("Ok, I can see how the river protects the border, but I", "can't see how anything could affect that from this", "temple.", CONTENT);
								return true;
							case 30:
								player.getDialogue().sendNpcChat("Well, as much as it saddens me to say so adventurer,", "Lord Saradomin's presence has not been felt on the", "land for many years now, and even", CONTENT);
								return true;
							case 31:
								player.getDialogue().sendNpcChat("though all true Saradominists know that he watches over", "use, his power upon the land is not as strong as it once", "was", CONTENT);
								return true;
							case 32:
								player.getDialogue().sendNpcChat("I fear that should those Zamorakians somehow pollute", "the Salve and desecrate his blessing, his power might not", "be able to stop", CONTENT);
								return true;
							case 33:
								player.getDialogue().sendNpcChat("the army of evil that lurks to the east, longing for the", "oppurtunity to invade and destroy us all!", CONTENT);
								return true;
							case 34:
								player.getDialogue().sendNpcChat("So what do you say adventurer? Will you aid me and", "all of vscape in foiling this Zamorakian plot?", HAPPY);
								return true;
							case 35:
								player.getDialogue().sendOption("Yes.", "No.");
								return true;
							case 36:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("Yes, of course. Any threat to Misthalin must be", "neutralized immediately. So, what can I do to help?", CONTENT);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("No, your story was a bunch of bull-shit.", "Fuck you and your god-damned river Salve.", ANGRY_2);
										player.getDialogue().endDialogue();
										return true;
								}
							case 37:
								player.getDialogue().sendNpcChat("Well, the immediate problem is that I am trapped in this", "cell. I know that the key to free me is nearby.", " The Zamorakians hardly ever leave.", CONTENT);
								return true;
							case 38:
								player.getDialogue().sendNpcChat("Should you find the key however, as you may have noticed", "there is a vampire in that coffin over there. I do not", "know how they managed to find it, but it is one of the", "ones that somehow", CONTENT);
								return true;
							case 39:
								player.getDialogue().sendNpcChat("survived the battle here all those years ago, and is by", "now quite, quite mad. It has been trapped on this side", "of the river for centuries,", CONTENT);
								return true;
							case 40:
								player.getDialogue().sendNpcChat("and as those fiendish Zamorakians pointed out to me", "with delight: as a descendant of one of those who", "trapped it here, it will recognize", CONTENT);
								return true;
							case 41:
								player.getDialogue().sendNpcChat("the smell of my blood should I come anywhere near it.", "It will of course then wake up and kill me.", CONTENT);
								return true;
							case 42:
								player.getDialogue().sendPlayerChat("Maybe I could kill it somehow then, while it is asleep?", CONTENT);
								return true;
							case 43:
								player.getDialogue().sendNpcChat("No adventurer, I do not think it would be wise for you", "to wake it at all. As I say, it is a little more than a wild", "animal, and must", CONTENT);
								return true;
							case 44:
								player.getDialogue().sendNpcChat("be extremely powerful to have survived until today. I", "suspect your best chance would be to incapacitate it", "somehow.", CONTENT);
								return true;
							case 45:
								player.getDialogue().sendPlayerChat("Okay, find the key to you cell and do something", "about the vampire.", CONTENT);
								return true;
							case 46:
								player.getDialogue().sendNpcChat("When you have done both of those I will be able to", "inspect the damage which those Zamorakians have done", "to the purity of the Salve.", HAPPY);
								return true;
							case 47:
								player.getDialogue().sendNpcChat("Depending on the severity of the damage, I may", "require further assistance from you in restoring it's", "purity.", CONTENT);
								return true;
							case 48:
								player.getDialogue().sendPlayerChat("Okay, well first thing's first; let's get you out of here.", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(23, 5);
								return true;
						}
						return false;
					case CELL_DOOR_UNLOCKED_NOT_SAFE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("The key fit the lock! You're free to leave now!", HAPPY);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Well, excellent work adventurer! Unfortunately, as you", "know, I cannot risk waking that vampire in the coffin.", CONTENT);
								return true;
							case 3:
								if (player.getInventory().playerHasItem(BUCKET_OF_MURKY_WATER)) {
									player.getDialogue().sendPlayerChat("I have some water from the Salve in this bucket.", "Do you think it would help against that vampire?", CONTENT);
									return true;
								} else if (player.getInventory().playerHasItem(BUCKET_OF_BLESSED_WATER)) {
									player.getDialogue().sendPlayerChat("I have this water you blessed from the Salve.", "How do I use it?", CONTENT);
									player.getDialogue().setNextChatId(6);
									return true;
								} else {
									player.getDialogue().sendPlayerChat("Well, how do you suppose we stop him?", CONTENT);
									player.getDialogue().setNextChatId(10);
									return true;
								}
							case 4:
								player.getDialogue().sendNpcChat("Yes, good thinking adventurer! Give it to me, I will bless", "it!", HAPPY);
								return true;
							case 5:
								player.getDialogue().sendGiveItemNpc("You hand Drezel your bucket of water.", "He hands you a bucket of blessed water in return.", new Item(BUCKET_OF_MURKY_WATER), new Item(BUCKET_OF_BLESSED_WATER));
								player.getInventory().replaceItemWithItem(new Item(BUCKET_OF_MURKY_WATER), new Item(BUCKET_OF_BLESSED_WATER));
								player.getDialogue().endDialogue();
								return true;
							case 6:
								player.getDialogue().sendNpcChat("If the vampire's coffin is doused in the blessed", "water, he will be unable to leave it! Use it on his coffin,", "quickly!", HAPPY);
								player.getDialogue().endDialogue();
								return true;
							case 10:
								player.getDialogue().sendNpcChat("Hmm, the vampire may be ancient and powerful,", "but he is a still one of the monsters that the River Salve", "was blessed to protect against.", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendNpcChat("Perhaps if you could somehow get the blessing of the Salve", "onto the coffin, he would be sealed inside!", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case CELL_DOOR_UNLOCKED_SAFE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("I poured the blessed water over the vampires coffin. I", "think that should trap him in there long enough for you", "to escape.", HAPPY);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Excellent work adventurer! I am free at last! Let me", "ensure that evil vampire is trapped for good. I will meet", "you down by the monument.", HAPPY);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Look for me down there, I need to assess what damage", "has been done to our holy barrier by those evil", "Zamorakians!", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
				}
				return false;
			case DREZEL_2:
				switch (player.getQuestStage(23)) {
					case CELL_DOOR_UNLOCKED_SAFE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Ah, " + player.getUsername() + ". I see you finally made it down here.", "Things are worse than I feared. I'm not sure if I will", "be able to repair the damage.", SAD);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Why, what's happened?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("From what I can tell, after you killed the guard dog", "who protected the entrance to the monuments, those", "Zamorakians forced the door into the main chamber", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("and have used some kind of evil potion upon the well", "which leads to the source of the river Salve. As they", "have done this at the very mouth of the river", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("it will spread along the entire river, disrupting the", "belssing placed upon it and allowing the evil creatures of", "Morytania to invade at their leisure.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("What can we do to prevent that?", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("Well, as you can see, I have placed a holy barrier on", "the entrance to this room from the South, but it is not", "very powerful and requires me to remain", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("here, focusing upon it to keep it intact. Should an", "attack come, they would be able to breach this defence", "very quickly indeed. What we need to do is", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("find some kind of way of removing or counteracting the", "evil magic that has been put into the river source at the", "well, so that the river will flow pure once again.", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendPlayerChat("Couldn't you bless the river to purify it? Like you did", "with the water I took from the well?", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendNpcChat("No, that would not work, the power I have from", "Saradomin is not great enough to cleanse an entire", "river of this foul Zamorakian pollutant.", SAD);
								return true;
							case 12:
								player.getDialogue().sendPlayerChat("Hmmm. I think I may have an idea. Would a type of", "rock that absorbs magic easily be of any use in getting", "this evil magic out of the river somehow?", CONTENT);
								return true;
							case 13:
								player.getDialogue().sendNpcChat("Yes! That would be perfect! It could work as a kind of", "natural filter, removing the potion from the water at the", "very source, and keeping the river pure!", HAPPY);
								return true;
							case 14:
								player.getDialogue().sendNpcChat("I have never heard of such a rock, however. You must", "tell me of what you speak!", CONTENT);
								return true;
							case 15:
								if (player.getQuestStage(5) >= 7) {
									player.getDialogue().sendPlayerChat("Well, it is somewhat of a secret, but recently I helped", "the Mages at the Wizards' Tower discover a new kind", "of rock that absorbed magic and could be used to make", "runes from.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendPlayerChat("It's known as 'Rune Essence' or 'Pure Essence' in it's", "stronger form, and it very absorbant of magical", "energy.", "I know because I craft runes with it all the time.", CONTENT);
									player.getDialogue().setNextChatId(17);
									return true;
								}
							case 16:
								player.getDialogue().sendPlayerChat("It's known as 'Rune Essence' or 'Pure Essence' in it's", "stronger form, and it very absorbant of magical", "energy.", CONTENT);
								return true;
							case 17:
								player.getDialogue().sendNpcChat("Saradomin truly smiled upon me when first we met", "adventurer! This sounds like the very answer we are", "looking for!", CONTENT);
								return true;
							case 18:
								player.getDialogue().sendPlayerChat("I will be able to get you this essence very quickly. How", "many do you think we need?", CONTENT);
								return true;
							case 19:
								player.getDialogue().sendNpcChat("Well, should these essences be all that you say they are,", "it would take around 25 of them I would estimate to", "cleanse the river and keep it pure.", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(23, 10);
								return true;
						}
						return false;
					case ESSENCE:
					case THROUGH_BARRIER:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getQuestStage(23) == ESSENCE) {
									player.getDialogue().sendNpcChat("Did you bring me those magical essences?", CONTENT);
									return true;
								} else if (player.getQuestStage(23) == THROUGH_BARRIER) {
									player.getDialogue().sendNpcChat("Excellent! That should do it! I will bless these stones", "and place them within the well, and vscape should be", "protected once more!", HAPPY);
									player.getDialogue().setNextChatId(4);
									return true;
								}
							case 2:
								if (player.getInventory().playerHasItem(RUNE_ESSENCE, 25) || player.getInventory().playerHasItem(PURE_ESSENCE, 25)) {
									if (player.getInventory().playerHasItem(PURE_ESSENCE, 25)) {
										player.getDialogue().sendGiveItemNpc("You hand Drezel a batch of pure essence.", new Item(PURE_ESSENCE));
										player.getInventory().removeItem(new Item(PURE_ESSENCE, 25));
										player.setQuestStage(23, 11);
										return true;
									} else if (player.getInventory().playerHasItem(RUNE_ESSENCE, 25)) {
										player.getDialogue().sendGiveItemNpc("You hand Drezel a batch of rune essence.", new Item(PURE_ESSENCE));
										player.getInventory().removeItem(new Item(RUNE_ESSENCE, 25));
										player.setQuestStage(23, 11);
										return true;
									}
								} else {
									player.getDialogue().sendPlayerChat("I'm afraid not...", SAD);
									player.getDialogue().endDialogue();
									return true;
								}
							case 3:
								player.getDialogue().sendNpcChat("Excellent! That should do it! I will bless these stones", "and place them within the well, and vscape should be", "protected once more!", HAPPY);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Please take this dagger; it has been handed down within", "my family for generations and is filled with the power of", "Saradomin. You will find that", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("it has the power to prevent werewolves from adopting", "their wolf form in combat as long as you have it", "equipped.", CONTENT);
								return true;
							case 6:
								player.setQuestStage(23, 12);
								QuestHandler.completeQuest(player, 23);
								player.getDialogue().dontCloseInterface();
								return true;
						}
						return false;
					case QUEST_COMPLETE:
						if (player.getQuestStage(37) > 0 && !player.getInventory().ownsItem(WOLFBANE)) {
							switch (player.getDialogue().getChatId()) {
								case 1:
									player.getDialogue().sendPlayerChat("I seem to have misplaced my Wolfbane...", SAD);
									return true;
								case 2:
									player.getDialogue().sendNpcChat("And I happen to have another one, your lucky day.", CONTENT);
									return true;
								case 3:
									player.getDialogue().sendGiveItemNpc("Drezel hands you another Wolfbane.", new Item(WOLFBANE));
									player.getDialogue().endDialogue();
									player.getInventory().addItem(new Item(WOLFBANE));
									return true;
							}
							return false;
						} else {
							return false;
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
