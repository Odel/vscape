package com.rs2.model.content.treasuretrails;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 12/01/12 Time: 22:34 To change
 * this template use File | Settings | File Templates.
 */
public class SpeakToScrolls {

	/* speaking to a npc scroll */

	public static enum SpeakToData {
		NPC_1(new String[]{"One of the sailors in Port Sarim ", " is your next destination."}, 3496, 376, "", 1),
		NPC_2(new String[]{"Someone watching the fights ", "in the duel arena is your next ", "destination"}, 3497, 969, "", 3),
		NPC_3(new String[]{"Speak to a referee."}, 3498, 635, "Challenge", 2),
		NPC_4(new String[]{"Speak to Arhein in Catherby"}, 3499, 563, "", 1),
		NPC_5(new String[]{"Speak to Brimstail"}, 3500, 171, "", 1),
		NPC_6(new String[]{"Speak to Donovan the Family ", "Handyman"}, 3501, 806, "", 2),
		NPC_7(new String[]{"Speak to Doric who lives ", "north of Falador"}, 3502, 284, "", 1),
		NPC_8(new String[]{"Speak to Ellis in Al Kharid."}, 3503, 2824, "", 1),
		NPC_9(new String[]{"Speak to Gaius in Taverley."}, 3504, 586, "", 1),
		NPC_10(new String[]{"Speak to Hajedy"}, 3505, 510, "", 1),
		NPC_12(new String[]{"Speak to Hazelmere"}, 3507, 669, "", 1),
		NPC_13(new String[]{"Speak to Kangai Mau"}, 3508, 846, "", 1),
		NPC_14(new String[]{"Speak to Ned in Draynor "}, 3509, 743, "", 1),
		//NPC_15(new String[]{"Speak to Ned in Draynor "}, 3510, 918, "", 1),
		NPC_16(new String[]{"Speak to Roavar"}, 3512, 1042, "", 2),
		NPC_17(new String[]{"Speak to Sir Kay in Camelot", "Castle"}, 3513, 241, "", 1),
		NPC_18(new String[]{"Speak to the bartender of the", "Blue Moon Inn in Varrock"}, 3514, 733, "", 1),
		NPC_20(new String[]{"Speak to the staff of Sinclair ", "Mansion"}, 3564, 809, "", 1),
		NPC_21(new String[]{"Speak to Ulizius"}, 3566, 1054, "", 2),
		NPC_22(new String[]{"Talk to the bartender of the Rusty ", "Anchor in Port Sarim."}, 3568, 734, "", 1),
		NPC_23(new String[]{"Talk to the Squire in the White ", "Knights' castle in Falador."}, 3570, 606, "", 1),
		NPC_24(new String[]{"'A bag belt only?' he asked ", "his balding brothers"}, 3572, 801, "", 3),
		NPC_25(new String[]{"A strange little man who sells ", "armour only to those who've ", "proven themselves to be ", "unafraid of dragons."}, 3573, 747, "Puzzle", 3),
		NPC_26(new String[]{"Identify the back of this ", "over-acting brother. ", "(He's a long way from ", "home.)"}, 3574, 1008, "Puzzle", 3),
		NPC_27(new String[]{"If a man carried my ", "burden, he would break ", "his back. I am not rich, ", "but leave silver in my ", "track. Speak to the ", "keeper of my trail."}, 3575, 558, "", 3),
		NPC_28(new String[]{"'Small Shoe.' Often found with ", "rod on mushroom."}, 3577, 162, "Puzzle", 3),
		NPC_29(new String[]{"Snah? I feel all confused, like ", "one of those cakes."}, 3579, 0, "", 3),
		NPC_30(new String[]{"This aviator is at the ", "peak of his profession"}, 3580, 170, "Puzzle", 3),
		NPC_31(new String[]{"Citric Cellar", ""}, 2792, 603, "Puzzle", 3),
		NPC_32(new String[]{"Generally ", "speaking, his ", "nose was very ", "bent"}, 2783, 296, "Puzzle", 3),
		NPC_33(new String[]{"My name is like a tree, ", "yet it is spelt with a \"g\"", "come see the fur, which ", "is right near me"}, 2837, 783, "Puzzle", 3),
		NPC_34(new String[]{"Often examined by learners", "of what has passed,", "find me where words", "of wisdom speak volumes."}, 2845, 618, "Puzzle", 3),
		NPC_35(new String[]{"There is no 'worthier' ", "lord."}, 2856, 1182, "Puzzle", 3),

		;
		private String[] hints;
		private int clueId;
		private int npcId;
		private String toGet;
		private int level;

		private static Map<Integer, SpeakToData> npcs = new HashMap<Integer, SpeakToData>();
		private static Map<Integer, SpeakToData> clues = new HashMap<Integer, SpeakToData>();

		public static SpeakToData forIdNpc(int npcId) {
			return npcs.get(npcId);
		}

		public static SpeakToData forIdClue(int clueId) {
			return clues.get(clueId);
		}

		static {
			for (SpeakToData data : SpeakToData.values()) {
				npcs.put(data.npcId, data);
				clues.put(data.clueId, data);
			}
		}

		SpeakToData(String[] hints, int clueId, int npcId, String toGet, int level) {
			this.hints = hints;
			this.clueId = clueId;
			this.npcId = npcId;
			this.toGet = toGet;
			this.level = level;
		}

		public String[] getHints() {
			return hints;
		}

		public int getClueId() {
			return clueId;
		}

		public int getNpcId() {
			return npcId;
		}

		public String getToGet() {
			return toGet;
		}

		public int getLevel() {
			return level;
		}
	}

	/* loading clue scroll interface */

	public static boolean loadClueInterface(Player player, int itemId) {
		SpeakToData speakToData = SpeakToData.forIdClue(itemId);
		if (speakToData == null) {
			return false;
		}
		player.getActionSender().sendInterface(ClueScroll.CLUE_SCROLL_INTERFACE);
		for (int i = 0; i < speakToData.getHints().length; i++) {
			player.getActionSender().sendString(speakToData.getHints()[i], getChilds(speakToData.getHints())[i]);
		}
		return true;
	}
	/* put the right childs ids on the interface */

	public static int[] getChilds(String[] sentences) {
		switch (sentences.length) {
			case 1 :
				return new int[]{6971};
			case 2 :
				return new int[]{6971, 6972};
			case 3 :
				return new int[]{6970, 6971, 6972};
			case 4 :
				return new int[]{6970, 6971, 6972, 6973};
			case 5 :
				return new int[]{6969, 6970, 6971, 6972, 6973};
			case 6 :
				return new int[]{6969, 6970, 6971, 6972, 6973, 6974};
			case 7 :
				return new int[]{6968, 6969, 6970, 6971, 6972, 6973, 6974};
			case 8 :
				return new int[]{6968, 6969, 6970, 6971, 6972, 6973, 6974, 6975};
		}
		return null;
	}

	/* gets a random scroll */

	public static int getRandomScroll(int level) {
		int pick = new Random().nextInt(SpeakToData.values().length);
		while (SpeakToData.values()[pick].getLevel() != level) {
			pick = new Random().nextInt(SpeakToData.values().length);
		}

		return SpeakToData.values()[pick].getClueId();
	}

	/* handling npc talk */

	public static boolean handleNpc(Player player, int npcId) {
		SpeakToData speakData = SpeakToData.forIdNpc(npcId);

		if (speakData == null) {
			return false;
		}
		if (!player.getInventory().playerHasItem(speakData.getClueId()) && !ChallengeScrolls.gotScroll(player, speakData.getClueId())) {
			return false;
		}
		/*
		 * first, the player talks to the npc, then move on the next stage after
		 * challenge/puzzle
		 */
		player.getDialogue().setLastNpcTalk(npcId);
		if (speakData.getToGet() == "Challenge") {
			if (ChallengeScrolls.gotScroll(player, speakData.getClueId())) {
				player.clueLevel = speakData.getLevel();
				player.challengeScroll = speakData.getClueId();
				Dialogues.setNextDialogue(player, 10009, 3);
				if (ChallengeScrolls.getString(speakData.getClueId()).length == 1) {
					player.getDialogue().sendNpcChat(ChallengeScrolls.getString(speakData.getClueId())[0], Dialogues.HAPPY);
				} else {
					player.getDialogue().sendNpcChat(ChallengeScrolls.getString(speakData.getClueId())[0], ChallengeScrolls.getString(speakData.getClueId())[1], Dialogues.HAPPY);
				}
			} else {
				player.getInventory().removeItem(new Item(speakData.getClueId(), 1));
				player.getDialogue().sendNpcChat("Here's a challenge for you.", Dialogues.HAPPY);
				ChallengeScrolls.addNewChallenge(player, speakData.getClueId());
			}
		} else if (speakData.getToGet() == "Puzzle") {
			if (Puzzle.finishedPuzzle(player)) {
				Dialogues.setNextDialogue(player, 10009, 2);
				player.getDialogue().sendNpcChat("Thank you very much.", Dialogues.HAPPY);
				player.getInventory().removeItem(new Item(ClueScroll.CASTLE_PUZZLE, 1));
				player.getInventory().removeItem(new Item(ClueScroll.OGRE_PUZZLE, 1));
				player.getInventory().removeItem(new Item(ClueScroll.TREE_PUZZLE, 1));
				player.getInventory().removeItem(new Item(speakData.getClueId(), 1));
				player.clueLevel = speakData.getLevel();
			} else if (player.hasPuzzle()) {
				player.getDialogue().sendNpcChat("The puzzle doesn't seem to be complete yet.", Dialogues.HAPPY);
			} else {
				player.getDialogue().sendNpcChat("Hello, Solve this puzzle for me please.", Dialogues.HAPPY);
				Puzzle.addRandomPuzzle(player);
			}
		} else {
			Dialogues.setNextDialogue(player, 10009, 2);
			player.getDialogue().sendNpcChat("Thank you very much.", Dialogues.HAPPY);
			player.getInventory().removeItem(new Item(speakData.getClueId(), 1));
			player.clueLevel = speakData.getLevel();
		}

		return true;
	}

}
