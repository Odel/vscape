package com.rs2.model.content.treasuretrails;

import com.rs2.model.content.combat.effect.impl.BindingEffect;
import com.rs2.model.content.combat.hit.Hit;
import com.rs2.model.content.combat.hit.HitDef;
import com.rs2.model.content.combat.hit.HitType;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 08/01/12 Time: 12:19 To change
 * this template use File | Settings | File Templates.
 */
public class AnagramsScrolls {

	/* enum that contains the whole anagrams */
	public static enum AnagramsData {
		SABA(2677, 1070, "A Bas", "", 2),
		JARAAH(2678, 962, "Aha Jar", "Challenge", 3),
		CAROLINE(2679, 696, "Arc O Line", "", 2),
		ORACLE(2680, 746, "Are Col", "Challenge", 2),
		BRIMSTAIL(2681, 171, "Bail Trims", "Challenge", 3),
		//KEBAB_SELLER(2682, 543, "Bar Bell Seek", "", 3),
		BOLKOY(2683, 471, "By Look", "Challenge", 2),
		GNOME_COACH(2684, 2802, "C On Game Hoc", "Challenge", 3),
		BRUNDT(2685, 1294, "Dt Run B", "Challenge", 2),
		ZOOKEEPER(2686, 28, "Eek Zero Op", "Challenge", 3),
		LOWE(2687, 550, "El Ow", "", 2),
		RECRUITER(2688, 720, "Err Cure It", "Challenge", 2),
		KING_BOLREN(2689, 469, "Goblin Kern", "", 3),
		GABOOTY(2690, 2520, "Got A Boy", "Challenge", 2),
		//GABOOTY2(2690, 2521, "Got A Boy", "Challenge", 2),
		LUTHAS(2691, 379, "Halt Us", "Challenge", 2),
		FYCIE(2692, 1011, "Icy fe", "", 3),
		KING_ROLAND(2693, 648, "Lark In Dog", "Challenge", 3),
		FEMI(2694, 676, "Me If", "", 2),
		EDMOND(2695, 714, "Nod Med", "Challenge", 2),
		//EDMOND2(2696, 714, "Nod Med", "Challenge", 2),
		CAPN_IZZY_NO_BEARD(2697, 437, "O Birdz A Zany En Pc", "Challenge", 3),
		COOK(2698, 278, "Ok Co", "Challenge", 2),
		//COOK2(2699, 1135, "Ok Co", "Challenge", 2),
		//COOK3(2699, 1136, "Ok Co", "Challenge", 2),
		//COOK4(2699, 1137, "Ok Co", "Challenge", 2),
		PARTY_PETE(2700, 659, "Peaty Pert", "", 2);

		private int clueId;
		private int npcId;
		private String anagram;
		private String toGet;
		private int level;

		private static Map<Integer, AnagramsData> npcs = new HashMap<Integer, AnagramsData>();
		private static Map<Integer, AnagramsData> clues = new HashMap<Integer, AnagramsData>();

		public static AnagramsData forIdNpc(int npcId) {
			return npcs.get(npcId);
		}

		public static AnagramsData forIdClue(int clueId) {
			return clues.get(clueId);
		}

		static {
			for (AnagramsData data : AnagramsData.values()) {
				npcs.put(data.npcId, data);
				clues.put(data.clueId, data);
			}
		}

		AnagramsData(int clueId, int npcId, String anagram, String toGet, int level) {
			this.clueId = clueId;
			this.npcId = npcId;
			this.anagram = anagram;
			this.toGet = toGet;
			this.level = level;
		}

		public int getClueId() {
			return clueId;
		}

		public int getNpcId() {
			return npcId;
		}

		public String getAnagram() {
			return anagram;
		}

		public String getToGet() {
			return toGet;
		}

		public int getLevel() {
			return level;
		}
	}

	/* loading the clue scroll interfaces */

	public static boolean loadClueInterface(Player player, int itemId) {
		AnagramsData anagramsData = AnagramsData.forIdClue(itemId);
		if (anagramsData == null) {
			return false;
		}
		player.getActionSender().sendInterface(ClueScroll.CLUE_SCROLL_INTERFACE);
		player.getActionSender().sendString("This anagram reveals", 6970);
		player.getActionSender().sendString("who to speak to next:", 6971);
		player.getActionSender().sendString(anagramsData.getAnagram().toUpperCase(), 6973);
		return true;
	}

	/* handling npc talk */

	public static boolean handleNpc(Player player, int npcId) {
		AnagramsData anagramsData = AnagramsData.forIdNpc(npcId);

		if (anagramsData == null) {
			return false;
		}
		if (!player.getInventory().getItemContainer().contains(anagramsData.getClueId()) && !ChallengeScrolls.gotScroll(player, anagramsData.getClueId())) {
			return false;
		}

		player.getInventory().removeItem(new Item(anagramsData.getClueId(), 1));

		/*
		 * first, the player talks to the npc, then move on the next stage after
		 * challenge/puzzle
		 */
		player.getDialogue().setLastNpcTalk(npcId);
		
		if (anagramsData.getToGet().equals("Challenge")) {
			if (ChallengeScrolls.gotScroll(player, anagramsData.getClueId())) {
				player.clueLevel = anagramsData.getLevel();
				player.challengeScroll = anagramsData.getClueId();
				Dialogues.setNextDialogue(player, 10009, 3);
				if (ChallengeScrolls.getString(anagramsData.getClueId()).length == 1) {
					player.getDialogue().sendNpcChat(ChallengeScrolls.getString(anagramsData.getClueId())[0], Dialogues.HAPPY);
				} else {
					player.getDialogue().sendNpcChat(ChallengeScrolls.getString(anagramsData.getClueId())[0], ChallengeScrolls.getString(anagramsData.getClueId())[1], Dialogues.HAPPY);
				}
				return true;
			} else {
				player.getInventory().removeItem(new Item(anagramsData.getClueId(), 1));
				player.getDialogue().sendNpcChat("Here's a challenge for you.", Dialogues.HAPPY);
				ChallengeScrolls.addNewChallenge(player, anagramsData.getClueId());
				return true;
			}
		} else if (anagramsData.getToGet().equals("Puzzle")) {
		    if(player.getPuzzle().playerHasPuzzle()) {
			if (player.getPuzzle().finishedPuzzle()) {
				Dialogues.setNextDialogue(player, 10009, 2);
				player.getDialogue().sendNpcChat("Thank you very much.", Dialogues.HAPPY);
				player.getInventory().removeItem(new Item(ClueScroll.CASTLE_PUZZLE, 1));
				player.getInventory().removeItem(new Item(ClueScroll.TROLL_PUZZLE, 1));
				player.getInventory().removeItem(new Item(ClueScroll.TREE_PUZZLE, 1));
				player.getInventory().removeItem(new Item(anagramsData.getClueId(), 1));
				player.clueLevel = anagramsData.getLevel();
				player.getPuzzle().resetPuzzleItems();
				return true;
			} else {
				player.getDialogue().sendNpcChat("The puzzle doesn't seem to be complete yet.", Dialogues.SAD);
				return true;
			}
		    } else {
			player.getDialogue().sendNpcChat("Hello, solve this puzzle for me please.", Dialogues.HAPPY);
			player.getPuzzle().resetPuzzleItems();
			player.getPuzzle().addRandomPuzzle();
			return true;
		    }
		} else {
		    Dialogues.setNextDialogue(player, 10009, 2);
		    player.getDialogue().sendNpcChat("Thank you very much.", Dialogues.HAPPY);
		    player.getInventory().getItemContainer().remove(new Item(anagramsData.getClueId(), 1));
		    player.clueLevel = anagramsData.getLevel();
		    return true;
		}
	}

	/* getting a random anagram clue */

	public static int getRandomScroll(int level) {
		int pick = new Random().nextInt(AnagramsData.values().length);
		while (AnagramsData.values()[pick].getLevel() != level) {
			pick = new Random().nextInt(AnagramsData.values().length);
		}

		return AnagramsData.values()[pick].getClueId();
	}

}