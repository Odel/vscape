package com.rs2.model.content.quests.impl;

import com.rs2.model.Position;
import com.rs2.model.content.dialogue.DialogueManager;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.net.ActionSender;

public class LegendsQuest implements Quest {
	
	public static boolean ENABLED = false;

	public static final int questIndex = 7366; //Used in player's quest log interface, id is in Player.java, Change
	//Quest stages
	public static final int QUEST_STARTED = 1;
	public static final int QUEST_COMPLETE = 2;

	//Items
	public static final int RADIMUS_NOTES = 714,
				RADIMUS_NOTES_2 = 715,
				BULL_ROARER = 716,
				SCRAWLED_NOTE = 717,
				SCRIBBLED_NOTE = 718,
				SCRUMPLED_NOTE = 719,
				SKETCH = 720,
				GOLD_BOWL = 721,
				BLESSED_GOLD_BOWL = 722,
				GOLDEN_BOWL_WATER = 723,
				GOLDEN_BOWL_PURE_WATER = 724,
				BLESSED_BOWL_WATER = 725,
				BLESSED_BOWL_PURE_WATER = 726,
				HOLLOW_REED = 727,
				HOLLOW_REED_2 = 728,
				SHAMANS_TOME = 729,
				BINDING_BOOK = 730,
				ENCHANTED_VIAL = 731,
				HOLY_WATER = 732,
				SMASHED_GLASS = 733,
				YOMMI_TREE_SEEDS = 735,
				GERMINATED_YOMMI_SEEDS = 736,
				SNAKEWEED_MIXTURE = 737,
				ARDRIGAL_MIXTURE = 738,
				BRAVERY_POTION = 739,
				BLUE_HAT = 740,
				CHUNK_OF_CRYSTAL = 741,
				HUNK_OF_CRYSTAL = 742,
				LUMP_OF_CRYSTAL = 743,
				HEART_CRYSTAL = 744,
				HEART_CRYSTAL_2 = 745,
				DARK_DAGGER = 746,
				GLOWING_DAGGER = 747,
				HOLY_FORCE = 748,
				YOMMI_TOTEM = 749,
				GILDED_TOTEM = 750;

	//Positions
	public static final Position POSITION = new Position(0, 0, 0);

	//Interfaces
	public static final int INTERFACE = -1;

	//Npcs
	public static final int LEGENDS_GUARD = 398,
				LEGENDS_GUARD_2 = 399,
				RADIUMUS_ERKLE = 400,
				JUNGLE_FORESTER = 401,
				JUNGLE_FORESTER_2 = 402,
				GUJUO = 928,
				UNGADULU_70 = 929,
				UNGADULU_169 = 930,
				JUNGLE_SAVAGE = 931,
				FIONELLA = 932,
				SIEGFRIED_ERKLE = 933,
				NEZIKCHENED = 934,
				VIYELDI = 935,
				SAN_TOJALON = 936,
				IRVIG_SENAY = 937,
				RANALPHE_DEVERE = 938,
				BOULDER = 939,
				ECHNED_ZEKIN = 940;

	//Objects
	public static final int OBJECT = -1;

	private int reward[][] = { //{itemId, count},
	};

	private int expReward[][] = { //{skillId, exp},
	};

	private static final int questPointReward = 4; //Change

	public int getQuestID() { //Change
		return 49;
	}

	public String getQuestName() { //Change
		return "Legends Quest";
	}

	public String getQuestSaveName() { //Change
		return "legends";
	}

	public boolean canDoQuest(final Player player) {
		if(player.getQuestPoints() < 107) {
			return false;
		}
		String[] quests = {"Heroes Quest", "Family Crest", "Underground Pass", "Waterfall Quest", "Shilo Village"};
		boolean canDoIt = true;
		for(String s : quests) {
			if(!QuestHandler.questCompleted(player, QuestHandler.getQuestId(s))) {
				canDoIt = false;
				break;
			}
		}
		return canDoIt;
	}

	public void getReward(final Player player) {
		for (int[] rewards : reward) {
			player.getInventory().addItemOrDrop(new Item(rewards[0], rewards[1]));
		}
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				b.stop();
			}

			@Override
			public void stop() {
				for (final int[] expRewards : expReward) {
					player.getSkill().addExp(expRewards[0], (expRewards[1]));
				}
			}
		}, 4);
		player.addQuestPoints(questPointReward);
		player.getActionSender().QPEdit(player.getQuestPoints());
	}

	public void completeQuest(Player player) {
		getReward(player);
		player.getActionSender().sendInterface(12140);
		player.getActionSender().sendItemOnInterface(12145, 250, GILDED_TOTEM); //zoom, then itemId
		player.getActionSender().sendString("You have completed " + getQuestName() + "!", 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("4 Quest Points", 12150);
		player.getActionSender().sendString("Access to the Legends Guild", 12151);
		player.getActionSender().sendString("17212.5 XP in each of four", 12152);
		player.getActionSender().sendString("skills of your choice", 12153);
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
		a.sendQuestLogString("I can start this quest by talking to Radimus Erkle in the", 1, this.getQuestID(), 0);
		a.sendQuestLogString("Legends Guild northeast of Ardougne.", 2, this.getQuestID(), 0);
		a.sendQuestLogString("I should do what Radimus says.", 4, this.getQuestID(), QUEST_STARTED);
		
		switch (questStage) {
			default:
				break;
			case 0:
				a.sendQuestLogString("I can start this quest by talking to @dre@Radimus Erkle @bla@in the", 1);
				a.sendQuestLogString("@dre@Legends Guild @bla@northeast of @dre@Ardougne @bla@.", 2);
				a.sendQuestLogString("To start this quest I need to complete the following:", 4);
				a.sendQuestLogString((QuestHandler.questCompleted(player, 27) ? "@str@" : "@dbl@") + "-Heroes Quest", 5);
				a.sendQuestLogString((QuestHandler.questCompleted(player, 28) ? "@str@" : "@dbl@") + "-Family Crest", 6);
				a.sendQuestLogString((QuestHandler.questCompleted(player, 48) ? "@str@" : "@dbl@") + "-Shilo Village", 7);
				a.sendQuestLogString((QuestHandler.questCompleted(player, 44) ? "@str@" : "@dbl@") + "-Underground Pass", 8);
				a.sendQuestLogString((QuestHandler.questCompleted(player, 31) ? "@str@" : "@dbl@") + "-Waterfall Quest", 9);
				a.sendQuestLogString((player.getQuestPoints() >= 107 ? "@str@" : "@dbl@") + "-A total of 107 Quest Points", 9);
				a.sendQuestLogString("To complete this quest I need:", 11);
				a.sendQuestLogString((player.getSkill().getPlayerLevel(Skill.AGILITY) >= 50 ? "@str@" : "@dbl@") + "-50 Agility", 12);
				a.sendQuestLogString((player.getSkill().getPlayerLevel(Skill.CRAFTING) >= 50 ? "@str@" : "@dbl@") + "-50 Crafting", 13);
				a.sendQuestLogString((player.getSkill().getPlayerLevel(Skill.HERBLORE) >= 45 ? "@str@" : "@dbl@") + "-45 Herblore", 14);
				a.sendQuestLogString((player.getSkill().getPlayerLevel(Skill.MAGIC) >= 56 ? "@str@" : "@dbl@") + "-56 Magic", 15);
				a.sendQuestLogString((player.getSkill().getPlayerLevel(Skill.MINING) >= 52 ? "@str@" : "@dbl@") + "-52 Mining", 16);
				a.sendQuestLogString((player.getSkill().getPlayerLevel(Skill.PRAYER) >= 42 ? "@str@" : "@dbl@") + "-42 Prayer", 17);
				a.sendQuestLogString((player.getSkill().getPlayerLevel(Skill.SMITHING) >= 50 ? "@str@" : "@dbl@") + "-50 Smithing", 18);
				a.sendQuestLogString((player.getSkill().getPlayerLevel(Skill.STRENGTH) >= 50 ? "@str@" : "@dbl@") + "-50 Strength", 19);
				a.sendQuestLogString((player.getSkill().getPlayerLevel(Skill.THIEVING) >= 50 ? "@str@" : "@dbl@") + "-50 Thieving", 20);
				a.sendQuestLogString((player.getSkill().getPlayerLevel(Skill.WOODCUTTING) >= 50 ? "@str@" : "@dbl@") + "-50 Woodcutting", 21);
				a.sendQuestLogString("@dbl@-The ability to defeat a level 187 demon.", 22);
				break;
			case QUEST_STARTED:
				a.sendQuestLogString("Additional information", lastIndex + 1);
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
		if (ENABLED) {
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

	public boolean itemHandling(final Player player, final int itemId) {
		switch (itemId) {

		}
		return false;
	}

	public boolean itemOnItemHandling(final Player player, final int firstItem, final int secondItem, final int firstSlot, final int secondSlot) {
		return false;
	}

	public boolean doItemOnObject(final Player player, final int object, final int item) {
		return false;
	}

	public boolean doItemOnNpc(final Player player, final int itemId, final Npc npc) {
		return false;
	}

	public boolean doNpcClicking(final Player player, final Npc npc) {
		return false;
	}
	
	public boolean doNpcSecondClicking(final Player player, final Npc npc) {
		return false;
	}

	public boolean doObjectClicking(final Player player, final int object, final int x, final int y) {
		switch (object) {
			case OBJECT:
				return true;
		}
		return false;
	}

	public boolean doObjectSecondClick(final Player player, final int object, final int x, final int y) {
		switch (object) {

		}
		return false;
	}

	public void handleDeath(final Player player, final Npc died) {

	}

	public boolean sendDialogue(final Player player, final int id, int chatId, int optionId, int npcChatId) {
		if(!ENABLED) {
			return false;
		}
		DialogueManager d = player.getDialogue();
		switch (id) { //Npc ID
			case LEGENDS_GUARD:
			case LEGENDS_GUARD_2:
				String genderPronoun = (player.getGender() == 0 ? "Sir" : "Ma'am");
				switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
					case 0:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Yes " + genderPronoun + ", how can I help you?");
								return true;
							case 2:
								d.sendOption("What is this place?", "How do I get in here?", "Can I speak to someone in charge?", "It's ok thanks.");
								return true;
							case 3:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								switch(optionId) {
									case 1:
									case 2:
									case 4:
										d.endDialogue(); //Branch
								}
								return true;
							case 4:
								d.sendNpcChat("Well " + genderPronoun + ", Radimus Erkle is the Grand Vizier of the", "Legends Guild. He's a very busy man. And he'll", "only talk to those people eligible for the quest.");
								return true;
							case 5:
								d.sendOption("Can I go on the quest?", "What kind of quest is it?");
								return true;
							case 6:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if(optionId == 2)
									d.endDialogue(); //Branch
								return true;
							case 7:
								d.sendStatement("The guard gets out a scroll of paper and starts looking through it.");
								return true;
							case 8:
								if(canDoQuest(player)) {
									d.sendNpcChat("Well, it looks as if you are eligible for the quest. Grand", "Vizier Erkle will give you the details about the quest.", "You can go and talk to him about it if you like?");
								} else {
									d.sendNpcChat("Well, it looks as if you are not eligbile for the quest.", "I'm sorry but Grand Vizier Erkle will be unable to give", "you any details pertaining to the quest.", "Have a nice day.");
									d.endDialogue();
								}
								return true;
							case 9:
								d.sendOption("Who is Grand Vizier Erkle?", "Yes, I'd like to talk to Grand Vizier Erkle.", "Some other time perhaps.");
								return true;
							case 10:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								switch(optionId) {
									case 3:
										d.endDialogue();
										break;
								}
								return true;
							case 11:
								d.sendNpcChat("Ok, very well... You need to go into the building on the", "left, he's in his study.");
								return true;
							case 12:
								d.sendStatement("The guard unlocks the gate.");
								return true;
							case 13:
								d.sendNpcChat("Good luck!");
								d.endDialogue();
								//QuestHandler.startQuest(player, this.getQuestID());
								//Walk through gate
								return true;
								
						}
					return false;
					case QUEST_COMPLETE:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Welcome back to the Legends Guild, adventurer.");
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

