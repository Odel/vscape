package com.rs2.model.content.minigames.pestcontrol;

import static com.rs2.model.content.dialogue.Dialogues.ANGRY_2;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import java.math.BigDecimal;
import java.math.RoundingMode;


public class PestControlRewardHandler {
    public static final int EXP_LADY = 3789;
    public static final int ARMOR_REWARDS = 3787;
    public static final int PET_REWARDS = 3788;
    
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
	    case ARMOR_REWARDS:
		switch (player.getDialogue().getChatId()) {
		    case 1:
			player.getDialogue().sendNpcChat("Would you like to exchange some of your points?", CONTENT);
			return true;
		    case 2:
			player.getDialogue().sendOption("Yes!", "No, thank you.");
			return true;
		    case 3:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendNpcChat("I can reward you with some of our armor,", "is that what you are interested in?", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
			return true;
		    case 4:
			player.getDialogue().sendOption("Of course!", "No, thank you.");
			return true;
		    case 5:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendNpcChat("What piece would you like?", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
			return true;
		    case 6:
			player.getDialogue().sendOption("Void Robe Top (250 Points)", "Void Robe Bottoms (250 Points)", "Void Mace (250 Points)", "Void Gloves (150 Points)", "Next...");
			return true;
		    case 7:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendNpcChat("This will cost you 250 commendation points, is this okay?", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendNpcChat("This will cost you 250 commendation points, is this okay?", CONTENT);
				player.getDialogue().setNextChatId(12);
				return true;
			    case 3:
				player.getDialogue().sendNpcChat("This will cost you 250 commendation points, is this okay?", CONTENT);
				player.getDialogue().setNextChatId(16);
				return true;
			    case 4:
				player.getDialogue().sendNpcChat("This will cost you 150 commendation points, is this okay?", CONTENT);
				player.getDialogue().setNextChatId(20);
				return true;
			    case 5:
				player.getDialogue().sendOption("Void Melee Helm (200 Points)", "Void Range Helm (200 Points)", "Void Mage Helm (200 Points)", "Back.", "Nevermind.");
				player.getDialogue().setNextChatId(23);
				return true;
			}
			return true;
		    case 8:
			player.getDialogue().sendOption("Yes!", "No, thank you.");
			return true;
		    case 9:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendPlayerChat("Yes!", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
			return true;
		    case 10:
			if (player.getPcPoints() >= 250) {
			    player.getInventory().addItem(new Item(8839));
			    player.setPcPoints(player.getPcPoints() - 250, player);
			    break;
			} else if (player.getPcPoints() < 250) {
			    player.getActionSender().sendMessage("You don't have enough commendation points!");
			    player.getDialogue().endDialogue();
			    break;
			}
			return true;
		    case 12:
			player.getDialogue().sendOption("Yes!", "No, thank you.");
			return true;
		    case 13:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendPlayerChat("Yes!", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
			return true;
		    case 14:
			if (player.getPcPoints() >= 250) {
			    player.getInventory().addItem(new Item(8840));
			    player.setPcPoints(player.getPcPoints() - 250, player);
			    break;
			} else if (player.getPcPoints() < 250) {
			    player.getActionSender().sendMessage("You don't have enough commendation points!");
			    break;
			}
			player.getDialogue().endDialogue();
			return true;
		    case 16:
			player.getDialogue().sendOption("Yes!", "No, thank you.");
			return true;
		    case 17:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendPlayerChat("Yes!", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
			return true;
		    case 18:
			if (player.getPcPoints() >= 250) {
			    player.getInventory().addItem(new Item(8841));
			    player.setPcPoints(player.getPcPoints() - 250, player);
			    break;
			} else if (player.getPcPoints() < 250) {
			    player.getActionSender().sendMessage("You don't have enough commendation points!");
			    break;
			}
			player.getDialogue().endDialogue();
			return true;
		    case 20:
			player.getDialogue().sendOption("Yes!", "No, thank you.");
			return true;
		    case 21:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendPlayerChat("Yes!", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
			return true;
		    case 22:
			if (player.getPcPoints() >= 150) {
			    player.getInventory().addItem(new Item(8842));
			    player.setPcPoints(player.getPcPoints() - 150, player);
			    break;
			} else if (player.getPcPoints() < 150) {
			    player.getActionSender().sendMessage("You don't have enough commendation points!");
			    break;
			}
			player.getDialogue().endDialogue();
			return true;
		    case 23:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendNpcChat("This will cost you 200 commendation points, is this okay?", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendNpcChat("This will cost you 200 commendation points, is this okay?", CONTENT);
				player.getDialogue().setNextChatId(27);
				return true;
			    case 3:
				player.getDialogue().sendNpcChat("This will cost you 200 commendation points, is this okay?", CONTENT);
				player.getDialogue().setNextChatId(30);
				return true;
			    case 4:
				player.getDialogue().sendPlayerChat("I want to see the previous options.", CONTENT);
				player.getDialogue().setNextChatId(6);
				return true;
			    case 5:
				player.getDialogue().sendPlayerChat("Nevermind.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
			return true;
		    case 24:
			player.getDialogue().sendOption("Yes!", "No, thank you.");
			return true;
		    case 25:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendPlayerChat("Yes!", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
			return true;
		    case 26:
			if (player.getPcPoints() >= 200) {
			    player.getInventory().addItem(new Item(11665));
			    player.setPcPoints(player.getPcPoints() - 200, player);
			    break;
			} else if (player.getPcPoints() < 200) {
			    player.getActionSender().sendMessage("You don't have enough commendation points!");
			    break;
			}
			player.getDialogue().endDialogue();
			return true;
		    case 27:
			player.getDialogue().sendOption("Yes!", "No, thank you.");
			return true;
		    case 28:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendPlayerChat("Yes!", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
			return true;
		    case 29:
			if (player.getPcPoints() >= 200) {
			    player.getInventory().addItem(new Item(11664));
			    player.setPcPoints(player.getPcPoints() - 200, player);
			    break;
			} else if (player.getPcPoints() < 200) {
			    player.getActionSender().sendMessage("You don't have enough commendation points!");
			    break;
			}
			player.getDialogue().endDialogue();
			return true;
		    case 30:
			player.getDialogue().sendOption("Yes!", "No, thank you.");
			return true;
		    case 31:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendPlayerChat("Yes!", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
			return true;
		    case 32:
			if (player.getPcPoints() >= 200) {
			    player.getInventory().addItem(new Item(11663));
			    player.setPcPoints(player.getPcPoints() - 200, player);
			    break;
			} else if (player.getPcPoints() < 200) {
			    player.getActionSender().sendMessage("You don't have enough commendation points!");
			    break;
			}
			player.getDialogue().endDialogue();
			return true;
		}
		break;
	    case PET_REWARDS:
		switch (player.getDialogue().getChatId()) {
		    case 1:
			player.getDialogue().sendNpcChat("Would you like to exchange some of your points?", CONTENT);
			return true;
		    case 2:
			player.getDialogue().sendOption("Yes!", "No, thank you.");
			return true;
		    case 3:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendNpcChat("I can reward you with a unique pet!", "Is that what you are interested in?", CONTENT);
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
				player.getDialogue().sendNpcChat("What kind of pet would you like?", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		    case 6:
			player.getDialogue().sendOption("Low Tier (150-175 Points)", "Medium Tier (200 Points)", "High Tier (250-300 Points)", "Nevermind.");
			return true;
		    case 7:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendOption("Mouse (150 Points)", "Imp (175 Points)", "Rabbit (175 Points)", "Back.");
				return true;
			    case 2:
				player.getDialogue().sendOption("Birds (200 Points)", "Snake (200 Points)", "Ferret (200 Points)", "Back.");
				player.getDialogue().setNextChatId(18);
				return true;
			    case 3:
				player.getDialogue().sendOption("Monkey (Sold out)", "Hellcat (250 Points)", "Guard Dog (275 Points)", "Penguin (275 Points)", "Back.");
				player.getDialogue().setNextChatId(61);
				return true;
			    case 4:
				player.getDialogue().sendPlayerChat("Nevermind.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		    case 8:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendNpcChat("This will cost you 150 commendation points, is this okay?", CONTENT);
				return true; //mouse
			    case 2:
				player.getDialogue().sendNpcChat("This will cost you 175 commendation points, is this okay?", CONTENT);
				player.getDialogue().setNextChatId(12); //imp
				return true;
			    case 3:
				player.getDialogue().sendNpcChat("This will cost you 175 commendation points, is this okay?", CONTENT);
				player.getDialogue().setNextChatId(15); //Rabbit
				return true;
			    case 4:
				player.getDialogue().sendPlayerChat("I want to see the previous options.", CONTENT);
				player.getDialogue().setNextChatId(6);
				return true;
			}
		    case 9:
			player.getDialogue().sendOption("Yes!", "No, thank you.");
			return true;
		    case 10:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendPlayerChat("Yes!", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		    case 11:
			if (player.getPcPoints() >= 150) {
			    player.getInventory().addItem(new Item(6541));
			    player.setPcPoints(player.getPcPoints() - 150, player);
			    break;
			} else if (player.getPcPoints() < 150) {
			    player.getActionSender().sendMessage("You don't have enough commendation points!");
			    break;
			}
			player.getDialogue().endDialogue();
			return true;
		    case 12:
			player.getDialogue().sendOption("Yes!", "No, thank you.");
			return true;
		    case 13:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendPlayerChat("Yes!", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		    case 14:
			if (player.getPcPoints() >= 175) {
			    player.getInventory().addItem(new Item(9952));
			    player.setPcPoints(player.getPcPoints() - 175, player);
			    break;
			} else if (player.getPcPoints() < 175) {
			    player.getActionSender().sendMessage("You don't have enough commendation points!");
			    break;
			}
			player.getDialogue().endDialogue();
			return true;
		    case 15:
			player.getDialogue().sendOption("Yes!", "No, thank you.");
			return true;
		    case 16:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendPlayerChat("Yes!", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		    case 17:
			if (player.getPcPoints() >= 175) {
			    player.getInventory().addItem(new Item(9975));
			    player.setPcPoints(player.getPcPoints() - 175, player);
			    break;
			} else if (player.getPcPoints() < 175) {
			    player.getActionSender().sendMessage("You don't have enough commendation points!");
			    break;
			}
			player.getDialogue().endDialogue();
			return true;
		    case 18:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendOption("Crimson Swift (200 Points)", "Copper Longtail (200 Points)", "Cerulean Twitch (200 Points)", "Golden Warbler (200 Points)", "Tropical Wagtail (200 Points)");
				return true;
			    case 2:
				player.getDialogue().sendNpcChat("This will cost you 200 commendation points, is this okay?", CONTENT);
				player.getDialogue().setNextChatId(35); //snake
				return true;
			    case 3:
				player.getDialogue().sendNpcChat("This will cost you 200 commendation points, is this okay?", CONTENT);
				player.getDialogue().setNextChatId(38); //ferret
				return true;
			    case 4:
				player.getDialogue().sendPlayerChat("I want to see the previous options.", CONTENT);
				player.getDialogue().setNextChatId(6);
				return true;
			}
		    case 19:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendNpcChat("This will cost you 200 commendation points, is this okay?", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendNpcChat("This will cost you 200 commendation points, is this okay?", CONTENT);
				player.getDialogue().setNextChatId(23);
				return true;
			    case 3:
				player.getDialogue().sendNpcChat("This will cost you 200 commendation points, is this okay?", CONTENT);
				player.getDialogue().setNextChatId(26);
				return true;
			    case 4:
				player.getDialogue().sendNpcChat("This will cost you 200 commendation points, is this okay?", CONTENT);
				player.getDialogue().setNextChatId(29);
				return true;
			    case 5:
				player.getDialogue().sendNpcChat("This will cost you 200 commendation points, is this okay?", CONTENT);
				player.getDialogue().setNextChatId(32);
				return true;
			}
		    case 20:
			player.getDialogue().sendOption("Yes!", "No, thank you.");
			return true;
		    case 21:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendPlayerChat("Yes!", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		    case 22:
			if (player.getPcPoints() >= 200) {
			    player.getInventory().addItem(new Item(9965));
			    player.setPcPoints(player.getPcPoints() - 200, player);
			    break;
			} else if (player.getPcPoints() < 200) {
			    player.getActionSender().sendMessage("You don't have enough commendation points!");
			    break;
			}
			player.getDialogue().endDialogue();
			return true;
		    case 23:
			player.getDialogue().sendOption("Yes!", "No, thank you.");
			return true;
		    case 24:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendPlayerChat("Yes!", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		    case 25:
			if (player.getPcPoints() >= 200) {
			    player.getInventory().addItem(new Item(9966));
			    player.setPcPoints(player.getPcPoints() - 200, player);
			    break;
			} else if (player.getPcPoints() < 200) {
			    player.getActionSender().sendMessage("You don't have enough commendation points!");
			    break;
			}
			player.getDialogue().endDialogue();
			return true;
		    case 26:
			player.getDialogue().sendOption("Yes!", "No, thank you.");
			return true;
		    case 27:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendPlayerChat("Yes!", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		    case 28:
			if (player.getPcPoints() >= 200) {
			    player.getInventory().addItem(new Item(9967));
			    player.setPcPoints(player.getPcPoints() - 200, player);
			    break;
			} else if (player.getPcPoints() < 200) {
			    player.getActionSender().sendMessage("You don't have enough commendation points!");
			    break;
			}
			player.getDialogue().endDialogue();
			return true;
		    case 29:
			player.getDialogue().sendOption("Yes!", "No, thank you.");
			return true;
		    case 30:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendPlayerChat("Yes!", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		    case 31:
			if (player.getPcPoints() >= 200) {
			    player.getInventory().addItem(new Item(9968));
			    player.setPcPoints(player.getPcPoints() - 200, player);
			    break;
			} else if (player.getPcPoints() < 200) {
			    player.getActionSender().sendMessage("You don't have enough commendation points!");
			    break;
			}
			player.getDialogue().endDialogue();
			return true;
		    case 32:
			player.getDialogue().sendOption("Yes!", "No, thank you.");
			return true;
		    case 33:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendPlayerChat("Yes!", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		    case 34:
			if (player.getPcPoints() >= 200) {
			    player.getInventory().addItem(new Item(9969));
			    player.setPcPoints(player.getPcPoints() - 200, player);
			    break;
			} else if (player.getPcPoints() < 200) {
			    player.getActionSender().sendMessage("You don't have enough commendation points!");
			    break;
			}
			player.getDialogue().endDialogue();
			return true;
		    case 35:
			player.getDialogue().sendOption("Yes!", "No, thank you.");
			return true;
		    case 36:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendPlayerChat("Yes!", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		    case 37:
			if (player.getPcPoints() >= 200) {
			    player.getInventory().addItem(new Item(4606));
			    player.setPcPoints(player.getPcPoints() - 200, player);
			    break;
			} else if (player.getPcPoints() < 200) {
			    player.getActionSender().sendMessage("You don't have enough commendation points!");
			    break;
			}
			player.getDialogue().endDialogue();
			return true;
		    case 38:
			player.getDialogue().sendOption("Yes!", "No, thank you.");
			return true;
		    case 39:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendPlayerChat("Yes!", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		    case 40:
			if (player.getPcPoints() >= 200) {
			    player.getInventory().addItem(new Item(10092));
			    player.setPcPoints(player.getPcPoints() - 200, player);
			    break;
			} else if (player.getPcPoints() < 200) {
			    player.getActionSender().sendMessage("You don't have enough commendation points!");
			    break;
			}
			player.getDialogue().endDialogue();
			return true;
		    case 61:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendNpcChat("I'm sorry, we're all sold out of Monkeys.", "Isn't that just madness?", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			    case 2:
				player.getDialogue().sendNpcChat("This will cost you 250 commendation points, is this okay?", CONTENT);
				player.getDialogue().setNextChatId(65);
				return true;
			    case 3:
				player.getDialogue().sendNpcChat("This will cost you 275 commendation points, is this okay?", CONTENT);
				player.getDialogue().setNextChatId(68);
				return true;
			    case 4:
				player.getDialogue().sendNpcChat("This will cost you 275 commendation points, is this okay?", CONTENT);
				player.getDialogue().setNextChatId(71);
				return true;
			    case 5:
				player.getDialogue().sendPlayerChat("I want to see the previous options.", CONTENT);
				player.getDialogue().setNextChatId(6);
				return true;
			}
		    case 62:
			player.getDialogue().sendOption("Yes!", "No, thank you.");
			return true;
		    case 63:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendPlayerChat("Yes!", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		    case 64:
			if (player.getPcPoints() >= 225) {
			    player.getInventory().addItem(new Item(4033));
			    player.setPcPoints(player.getPcPoints() - 225, player);
			    break;
			} else if (player.getPcPoints() < 225) {
			    player.getActionSender().sendMessage("You don't have enough commendation points!");
			    break;
			}
			player.getDialogue().endDialogue();
			return true;
		    case 65:
			player.getDialogue().sendOption("Yes!", "No, thank you.");
			return true;
		    case 66:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendPlayerChat("Yes!", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		    case 67:
			if (player.getPcPoints() >= 250) {
			    player.getInventory().addItem(new Item(7582));
			    player.setPcPoints(player.getPcPoints() - 250, player);
			    break;
			} else if (player.getPcPoints() < 250) {
			    player.getActionSender().sendMessage("You don't have enough commendation points!");
			    break;
			}
			player.getDialogue().endDialogue();
			return true;
		    case 68:
			player.getDialogue().sendOption("Yes!", "No, thank you.");
			return true;
		    case 69: //;)
			switch (optionId) {
			    case 1:
				player.getDialogue().sendPlayerChat("Yes!", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		    case 70:
			if (player.getPcPoints() >= 275) {
			    player.getInventory().addItem(new Item(8132));
			    player.setPcPoints(player.getPcPoints() - 275, player);
			    break;
			} else if (player.getPcPoints() < 275) {
			    player.getActionSender().sendMessage("You don't have enough commendation points!");
			    break;
			}
			player.getDialogue().endDialogue();
			return true;
		    case 71:
			player.getDialogue().sendOption("Yes!", "No, thank you.");
			return true;
		    case 72:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendPlayerChat("Yes!", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		    case 73:
			if (player.getPcPoints() >= 275) {
			    player.getInventory().addItem(new Item(10592));
			    player.setPcPoints(player.getPcPoints() - 275, player);
			    break;
			} else if (player.getPcPoints() < 275) {
			    player.getActionSender().sendMessage("You don't have enough commendation points!");
			    break;
			}
			player.getDialogue().endDialogue();
			return true;
		}
		break;
	}
	return false;
    }
}
