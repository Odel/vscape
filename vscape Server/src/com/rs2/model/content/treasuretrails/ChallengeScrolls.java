package com.rs2.model.content.treasuretrails;

import java.util.HashMap;
import java.util.Map;

import com.rs2.model.World;
import com.rs2.model.content.combat.effect.impl.BindingEffect;
import com.rs2.model.content.combat.hit.Hit;
import com.rs2.model.content.combat.hit.HitDef;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 13/01/12 Time: 17:08 To change
 * this template use File | Settings | File Templates.
 */
public class ChallengeScrolls {

	/* containing the list of challenges */

	public enum ChallengeData {
		QUESTION_1(new String[]{"What is 19 to the power of 3?"}, 2681, 2842, 6859),
		QUESTION_2(new String[]{"What is 57x89+23?"}, 3498, 2844, 5096),
		QUESTION_3(new String[]{"If x is 15 and y is 3,", " what is 3x + y?"}, 2680, 2846, 48),
		QUESTION_4(new String[]{"How many cannons does Lumbridge", "castle have?"}, 2698, 2852, 9),
		QUESTION_5(new String[]{"How many animals in the Zoo?"}, 2686, 2854, 40),
		QUESTION_6(new String[]{"What is 5 times 5 add 3?"}, 2678, 7269, 28),
		QUESTION_7(new String[]{"How many flowers are there in", "the clearing below this platform?"}, 2683, 7271, 13),
		QUESTION_8(new String[]{"How many banana trees are there", "in the plantation?"}, 2691, 7273, 33),
		QUESTION_8_1(new String[]{"How many banana trees are there", "in the plantation?"}, 2697, 7273, 33),
		QUESTION_9(new String[]{"How many gnomeballers have", "red patches on their uniforms?"}, 2684, 7275, 6),
		QUESTION_10(new String[]{"How many houses have a cross", "on the door?"}, 2688, 7277, 20),
		QUESTION_11(new String[]{"How many pigeon cages are there ", "around the back of Jerico's house?"}, 2695, 7279, 3),
		QUESTION_12(new String[]{"How many buldings are there in", "the village?"}, 2690, 7281, 11),
		QUESTION_13(new String[]{"How many fishermen are there on", "the fishing platform?"}, 2679, 7283, 23),
		QUESTION_14(new String[]{"How many people are waiting for", "the next Bard to perform?"}, 2685, 7285, 4),
		QUESTION_15(new String[]{"How many bookcases are there ", "in the palace library?"}, 2693, 7287, 18);

		private String[] question;
		private int clueId;
		private int challengeId;
		private int answer;

		private static Map<Integer, ChallengeData> clues = new HashMap<Integer, ChallengeData>();
		private static Map<Integer, ChallengeData> challenges = new HashMap<Integer, ChallengeData>();

		public static ChallengeData forIdClue(int clueId) {
			return clues.get(clueId);
		}
		public static ChallengeData forIdChallenge(int challengeId) {
			return challenges.get(challengeId);
		}
		static {
			for (ChallengeData data : ChallengeData.values()) {
				clues.put(data.clueId, data);
				challenges.put(data.challengeId, data);
			}
		}

		ChallengeData(String[] question, int clueId, int challengeId, int answer) {
			this.question = question;
			this.clueId = clueId;
			this.challengeId = challengeId;
			this.answer = answer;
		}

		public String[] getQuestion() {
			return question;
		}

		public int getClueId() {
			return clueId;
		}

		public int getChallengeId() {
			return challengeId;
		}

		public int getAnswer() {
			return answer;
		}

	}

	/* adding new challenge based on the clue id */

	public static void addNewChallenge(Player player, int clueId) {
		ChallengeData challengeData = ChallengeData.forIdClue(clueId);
		if (challengeData == null) {
			return;
		}
		player.getInventory().addItemOrDrop(new Item(challengeData.getChallengeId(), 1));
	}

	/* checks if the player got challenge scroll */

	public static boolean gotScroll(Player player, int clueId) {
		ChallengeData challengeData = ChallengeData.forIdClue(clueId);
		if (challengeData == null) {
			return false;
		}
		return player.getInventory().playerHasItem(challengeData.getChallengeId());

	}

	/* get the question of the challenge -reserved for external use */

	public static String[] getString(int clueId) {
		ChallengeData challengeData = ChallengeData.forIdClue(clueId);
		if (challengeData == null) {
			return null;
		}
		return challengeData.getQuestion();
	}

	/* handle the answer given by the player */

	public static void handleAnswer(Player player, int answer, int clueId) {
		ChallengeData challengeData = ChallengeData.forIdClue(clueId);
		if (challengeData == null) {
			return;
		}
		if (!player.getInventory().getItemContainer().contains(challengeData.getChallengeId())) {
			return;
		}
		final Npc npc = World.getNpcs()[player.getNpcClickIndex()];
		player.getDialogue().setLastNpcTalk(npc != null ? npc.getNpcId() : 0);
		if (answer == challengeData.getAnswer()) {
			Dialogues.setNextDialogue(player, 10009, 2);
			player.getDialogue().sendNpcChat("Thank you, this is the right answer.", Dialogues.HAPPY);
			player.getInventory().getItemContainer().remove(new Item(challengeData.getChallengeId(), 1));
			player.getInventory().getItemContainer().remove(new Item(challengeData.getClueId(), 1));
			HitDef hitDef = new HitDef(null, HitType.NORMAL, 0).setStartingHitDelay(100000);
			Hit hit = new Hit(player, player, hitDef);
			BindingEffect bind = new BindingEffect(1000000);
			bind.initialize(hit); //try and step away during dialogue now :-)
		} else {
			Dialogues.setNextDialogue(player, 10009, 3);
			player.getDialogue().sendNpcChat("Sorry, this is the wrong answer, try again.", Dialogues.HAPPY);
		}

	}

	/* loading the challenge scroll interface */

	public static boolean loadClueInterface(Player player, int itemId) {
		ChallengeData searchData = ChallengeData.forIdChallenge(itemId);
		if (searchData == null) {
			return false;
		}
		player.getActionSender().sendInterface(ClueScroll.CLUE_SCROLL_INTERFACE);
		for (int i = 0; i < searchData.getQuestion().length; i++) {
			player.getActionSender().sendString(searchData.getQuestion()[i], getChilds(searchData.getQuestion())[i]);
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
		}
		return null;
	}

}
