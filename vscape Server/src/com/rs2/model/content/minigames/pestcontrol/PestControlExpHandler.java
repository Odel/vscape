
package com.rs2.model.content.minigames.pestcontrol;

import static com.rs2.model.content.dialogue.Dialogues.ANGRY_2;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import java.math.BigDecimal;
import java.math.RoundingMode;


public class PestControlExpHandler {
    public static final int EXP_LADY = 3789;
    
    public static void openInterface(Player player) {
	player.getActionSender().sendInterface(18691);
	player.getActionSender().sendString("", 18782);
	player.getActionSender().sendString(player.getPcPoints() + " points", 18783);
	player.setStatedInterface("PcExpInterface");
	player.setSkillAnswer(6);
    }
    public static boolean handleButtons(Player player, int button) {
	switch(button) {
	    case 73079: //attack
		player.getActionSender().sendString("", 18782);
		player.setSkillAnswer(0);
		player.getActionSender().openXInterface(208);
		player.getDialogue().dontCloseInterface();
		return true;
	    case 73080: //strength
		player.getActionSender().sendString("", 18782);
		player.setSkillAnswer(2);
		player.getActionSender().openXInterface(208);
		player.getDialogue().dontCloseInterface();
		return true;
	    case 73081: //defence
		player.getActionSender().sendString("", 18782);
		player.setSkillAnswer(1);
		player.getActionSender().openXInterface(208);
		player.getDialogue().dontCloseInterface();
		return true;
	    case 73082: //range
		player.getActionSender().sendString("", 18782);
		player.setSkillAnswer(4);
		player.getActionSender().openXInterface(208);
		player.getDialogue().dontCloseInterface();
		return true;
	    case 73083: //magic
		player.getActionSender().sendString("", 18782);
		player.setSkillAnswer(6);
		player.getActionSender().openXInterface(208);
		player.getDialogue().dontCloseInterface();
		return true;
	    case 73084: //hitpoints
		player.getActionSender().sendString("", 18782);
		player.setSkillAnswer(3);
		player.getActionSender().openXInterface(208);
		player.getDialogue().dontCloseInterface();
		return true;
	    case 73085: //prayer
		player.getActionSender().sendString("", 18782);
		player.setSkillAnswer(5);
		player.getActionSender().openXInterface(208);
		player.getDialogue().dontCloseInterface();
		return true;
	    case 73091: //confirm
		if(player.getPcSkillPoints() <= 0) {
		    player.getActionSender().sendMessage("Please choose a skill first.");
		    return true;
		}
		if (player.getSkill().getPlayerLevel(player.getSkillAnswer()) <= 24) {
		    player.getDialogue().sendNpcChat("You need to be atleast level 25", "in any skill to recieve an experience reward.", SAD);
		    player.getDialogue().endDialogue();
		    player.setSkillAnswer(0);
		    player.setPcSkillPoints(0);
		    return true;
		}
		else {
		    handleReward(player, handlePoints(player, player.getPcSkillPoints()));
		    player.getActionSender().sendString("", 18782);
		    player.getActionSender().sendString(player.getPcPoints() + " points", 18783);
		    return true;
		}
	}
	return false;
    }
    public static double round(double value, int places) {
	if (places < 0) {
	    throw new IllegalArgumentException();
	}

	BigDecimal bd = new BigDecimal(value);
	bd = bd.setScale(places, RoundingMode.HALF_UP);
	return bd.doubleValue();
    }
    
    public static double handlePoints(Player player, int amount) {
	double finalRewardAmount = 0;
	int l = 0;
	double N = 0;
	int skillIndex = player.getSkillAnswer();
	if (skillIndex == 5) {
	    N = 24; //Prayer
	} else if (skillIndex == 4 || skillIndex == 6) {
	    N = 35.8888888; //Range & Mage
	} else if (skillIndex == 0 || skillIndex == 1 || skillIndex == 2 || skillIndex == 3) {
	    N = 38.8888888; //Others
	}
	l = player.getSkill().getPlayerLevel(skillIndex);
	finalRewardAmount = (int) ((Math.ceil(((l + 25) * (l - 24)) / (606d))) * N);

	if (amount >= 100) {
	    return finalRewardAmount * amount * 1.1;
	} else {
	    return finalRewardAmount * amount * 1.1;
	}
    }

    public static void handleReward(Player player, double expReward) {
	int skillIndex = player.getSkillAnswer();
	player.getSkill().addExp(skillIndex, expReward);
	double reward = round(handlePoints(player, player.getPcSkillPoints()) * 2.25, 3);
	player.setPcPoints(player.getPcPoints() - player.getPcSkillPoints(), player);
	player.getActionSender().sendMessage(reward + " experience added to " + Skill.SKILL_NAME[player.getSkillAnswer()] + ".");
	player.setSkillAnswer(0);
	player.setPcSkillPoints(0);
    }
    
    public static boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
	switch (id) {
	    case EXP_LADY:
		switch (player.getDialogue().getChatId()) {
		    case 1:
			player.getDialogue().sendNpcChat("Would you like to exchange some of your points?", CONTENT);
			return true;
			/*openInterface(player);
			player.getDialogue().dontCloseInterface();*/
		    case 2:
			player.getDialogue().sendOption("Yes!", "No, thank you.");
			return true;
		    case 3:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendNpcChat("I can reward you with experience!", "Is that what you are interested in?", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		    case 4:
			player.getDialogue().sendOption("Of course!", "No, thank you.");
			return true;
		    case 5:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendNpcChat("Which skill would you like to put points into?", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		    case 6:
			player.getDialogue().sendOption("Attack", "Strength", "Defence", "Hitpoints", "More...");
			return true;
		    case 7:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendNpcChat("How many points would you like to put in?", CONTENT);
				player.setSkillAnswer(0);
				return true;
			    case 2:
				player.getDialogue().sendNpcChat("How many points would you like to put in?", CONTENT);
				player.setSkillAnswer(2);
				return true;
			    case 3:
				player.getDialogue().sendNpcChat("How many points would you like to put in?", CONTENT);
				player.setSkillAnswer(1);
				return true;
			    case 4:
				player.getDialogue().sendNpcChat("How many points would you like to put in?", CONTENT);
				player.setSkillAnswer(3);
				return true;
			    case 5:
				player.getDialogue().sendOption("Range", "Magic", "Prayer", "Back.", "Nevermind.");
				player.getDialogue().setNextChatId(13);
				return true;
			}
		    case 8:
			player.getActionSender().openXInterface(208);
			player.getDialogue().dontCloseInterface();
			return true;
		    case 9:
			if (player.getPcPoints() < player.getPcSkillPoints()) {
			    player.getDialogue().sendNpcChat("Do you think I am a fool?!", "You don't have that many points!", ANGRY_2);
			    player.setSkillAnswer(0);
			    player.getDialogue().endDialogue();
			    return true;
			} else if (player.getSkill().getPlayerLevel(player.getSkillAnswer()) <= 24) {
			    player.getDialogue().sendNpcChat("You need to be atleast level 25", "in any skill to recieve an experience reward.", SAD);
			    player.setSkillAnswer(0);
			    player.getDialogue().endDialogue();
			    return true;
			} else {
			    int points = player.getPcSkillPoints();
			    player.getDialogue().sendNpcChat(points + " point(s) will grant you...", round(handlePoints(player, points) * 2.25, 3) + " experience, is this alright?", CONTENT);
			    return true;
			}
		    case 10:
			player.getDialogue().sendOption("Yes!", "No, thank you.");
			return true;
		    case 11:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendPlayerChat("Yes!", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
				player.setSkillAnswer(0);
				player.getDialogue().endDialogue();
				return true;
			}
		    case 12:
			handleReward(player, handlePoints(player, player.getPcSkillPoints()));
			player.getDialogue().sendPlayerChat("Thank you!", HAPPY);
			player.getDialogue().endDialogue();
			return true;
		    case 13:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendNpcChat("How many points would you like to put in?", CONTENT);
				player.setSkillAnswer(4);
				player.getDialogue().setNextChatId(8);
				return true;
			    case 2:
				player.getDialogue().sendNpcChat("How many points would you like to put in?", CONTENT);
				player.setSkillAnswer(6);
				player.getDialogue().setNextChatId(8);
				return true;
			    case 3:
				player.getDialogue().sendNpcChat("How many points would you like to put in?", CONTENT);
				player.setSkillAnswer(5);
				player.getDialogue().setNextChatId(8);
				return true;
			    case 4:
				player.getDialogue().sendPlayerChat("I want to see the previous options.", CONTENT);
				player.setSkillAnswer(0);
				player.getDialogue().setNextChatId(6);
				return true;
			    case 5:
				player.getDialogue().sendPlayerChat("Nevermind...", CONTENT);
				player.setSkillAnswer(0);
				player.getDialogue().endDialogue();
				return true;
			}
		}
		return false;
	}
	return false;
    }
}
