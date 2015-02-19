package com.rs2.model.content.quests;

import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.dialogue.DialogueManager;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.container.inventory.Inventory;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class InSearchOfTheMyreque implements Quest {
    public static final int questIndex = 11907; //Used in player's quest log interface, id is in Player.java //Change
    //Quest stages
    public static final int QUEST_STARTED = 1;
    public static final int POUCH_FOR_RIDE = 2;
    public static final int BOARD_BOAT = 3;
    public static final int QUEST_COMPLETE = 15;

    //Items
    public static final int STEEL_DAGGER = 1207;
    public static final int STEEL_SWORD = 1281;
    public static final int STEEL_LONGSWORD = 1295;
    public static final int STEEL_MACE = 1424;
    public static final int STEEL_WARHAMMER = 1339;
    public static final int PLANK = 960;
    
    //Positions
    public static final Position POSITION = new Position(0, 0, 0);

    //Interfaces
    public static final int INTERFACE = -1;

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
    public static final int SWAMP_BOAT = 6969;
    public static final int SWAMP_BOAT_ABANDONED = 6970;
    
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
	switch (questStage) {
	    case QUEST_STARTED:
		player.getActionSender().sendString("@str@" + "Talk to Vanstrom Clause in the Canifis bar to begin.", 8147);
		//Change
		player.getActionSender().sendString("Vanstrom Clause asked me to take some weapons to", 8149);
		player.getActionSender().sendString("a group called the 'Myreque': ", 8150);
		player.getActionSender().sendString(player.getInventory().playerHasItem(STEEL_LONGSWORD) ? "@str@" : "@blu@" + "1 x Steel longsword", 8151);
		player.getActionSender().sendString(player.getInventory().playerHasItem(STEEL_SWORD) ? "@str@" : "@blu@" + "2 x Steel shortsword", 8152);
		player.getActionSender().sendString(player.getInventory().playerHasItem(STEEL_DAGGER) ? "@str@" : "@blu@" + "1 x Steel dagger", 8153);
		player.getActionSender().sendString(player.getInventory().playerHasItem(STEEL_MACE) ? "@str@" : "@blu@" + "1 x Steel mace", 8154);
		player.getActionSender().sendString(player.getInventory().playerHasItem(STEEL_WARHAMMER) ? "@str@" : "@blu@" + "1 x Steel warhammer", 8155);
		if(allWeapons(player)) {
		    player.getActionSender().sendString("I have all the weapons Vanstrom asked me to get.", 8156);
		}
		player.getActionSender().sendString("Vanstrom said the boatman in Mort'ton should be", 8157);
		player.getActionSender().sendString("able to help me find the 'Myreque'.", 8158);
		break;
	    case QUEST_COMPLETE:
		player.getActionSender().sendString("@str@" + "", 8147);
		//Change
		player.getActionSender().sendString("@red@" + "You have completed this quest!", 8177);
		break;
	    default:
		player.getActionSender().sendString("Talk to @dre@Vanstrom Clause @bla@in the @dre@Canifis Bar @bla@to begin.", 8147);
		player.getActionSender().sendString("@dre@Requirements:", 8148);
		if (QuestHandler.questCompleted(player, 37)) {
		    player.getActionSender().sendString("@str@-Nature Spirit.", 8150);
		} else {
		    player.getActionSender().sendString("@dbl@-Nature Spirit.", 8150);
		}
		player.getActionSender().sendString("@dbl@-Ability to defeat a level 97 foe.", 8151);
		if (player.getSkill().getLevel()[Skill.AGILITY] >= 25) {
		    player.getActionSender().sendString("@str@-25 Agility.", 8152);
		} else {
		    player.getActionSender().sendString("@dbl@-25 Agility.", 8152);
		}
		//Change
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
    
    public boolean itemHandling(final Player player, int itemId) {
	switch(itemId) {
	    
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

    public boolean doObjectClicking(final Player player, int object, int x, int y) {
	switch (object) {
	    case SWAMP_BOAT:
		Dialogues.startDialogue(player, 157600);
		return true;
	    case SWAMP_BOAT_ABANDONED:
		if(!QuestHandler.questCompleted(player, 37)) {
		    player.getDialogue().sendStatement("You must complete Nature Spirit to use this.");
		    player.getDialogue().endDialogue();
		    return true;
		}
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		    @Override
		    public void execute(CycleEventContainer b) {
			player.getActionSender().sendMessage("You carefully climb into the boat...");
			player.fadeTeleport(new Position(3521, 3285, 0));
			b.stop();
		    }

		    @Override
		    public void stop() {
			player.setStopPacket(false);
			player.getDialogue().sendStatement("You arrive in Mort'ton.");
			player.getDialogue().endDialogue();
		    }
		}, 5);
		return true;
	}
	return false;
    }

    public static boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
	switch (object) {
	    case SWAMP_BOAT:
		if (player.getQuestStage(38) >= BOARD_BOAT) {
		    if (player.getInventory().playerHasItem(995, 10)) {
			player.getInventory().removeItem(new Item(995, 10));
			player.fadeTeleport(new Position(3499, 3380, 0));
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
	switch (id) { //Npc ID
	    case 156700: //Swamp boat travel
		switch (d.getChatId()) {
		    case 1:
			if (player.getQuestStage(38) >= BOARD_BOAT) {
			    d.setLastNpcTalk(CYREG_PADDLEHORN);
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
			switch(optionId) {
			    case 1:
				if(player.getInventory().playerHasItem(995, 10)) {
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
			player.fadeTeleport(new Position(3499, 3380, 0));
			return true;
		}
		return false;
	    case CYREG_PADDLEHORN:
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
				if(player.getInventory().playerHasItem(NatureSpirit.DRUID_POUCH, 5)) {
				    d.sendGiveItemNpc("You show the boatman your druid pouch.", new Item(NatureSpirit.DRUID_POUCH_EMPTY));
				} else {
				    d.sendNpcChat("You can use my boat to find the Myreque. You'll", "be going through Mort Myre though so I won't be", "letting you go unless you've some defence against the", "Ghasts.", CONTENT);
				    d.endDialogue();
				}
				return true;
			    case 2:
				d.sendPlayerChat("I have this druid pouch here! This turns the Ghasts", "visible and I can kill them once I can see them.", HAPPY);
				return true;
			    case 3:
				d.sendNpcChat("Very well, you can go! But you'll need to bring me", "some wood planks first, I need three and you", "need three yourself.", CONTENT);
				if(!player.getInventory().playerHasItem(PLANK, 3)) {
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
				switch(optionId) {
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
				if(allWeapons(player)) {
				    d.sendPlayerChat("Hello there, I have some weapons for you to give to", "the 'Myreque'.", CONTENT);
				} else {
				    
				}
				return true;
			    case 3:
				d.sendNpcChat("Hmm, I don't know what you're talking about.", CONTENT);
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
				d.sendNpcChat("There's death a plenty in this forsaken place... what do I", "care that some fool hardy vigilantes decided to go it", "alone against the drakans? Stupidity of youth is to", "blame, I shan't carry it on my shoulders!", CONTENT);
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
				d.sendNpcChat("No, I won't take you, but you can use my boat. You'll", "be going through Mort Myre though so I won't be", "letting you go unless you've some defence against the", "Ghasts.", CONTENT);
				if(player.getInventory().playerHasItem(NatureSpirit.DRUID_POUCH, 5)) {
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
		    case QUEST_STARTED:
			switch (d.getChatId()) {
			    case 1:
				d.sendOption("What do I have to do again?", "What weapons do I need to get again?", "Where do I need to take the weapons again?", "Nevermind.");
				return true;
			    case 20:
				switch(optionId) {
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
					d.sendPlayerChat("Nevermind.", CONTENT);
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
				d.sendNpcChat("Steel I believe. All six Myreque require steel weapons. I", "would suggest a longsword, two shortswords, a", "dagger, a mace and a warhammer.", CONTENT);
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
				if(!QuestHandler.questCompleted(player, 37)) {
				    d.sendStatement("You need to complete Nature Spirit to interact with this NPC.");
				    d.endDialogue();
				} else {
				    d.sendNpcChat("Hello there, how goes it stranger?", HAPPY);
				}
				return true;
			    case 2:
				d.sendPlayerChat("Quite well thanks for asking, how about you?", CONTENT);
				return true;
			    case 3:
				d.sendNpcChat("Hmm, well, I am a little concerned about some friends", "of mine, they're in dire need of some assistance, but", "I'm at a loss as to how I can help them.", CONTENT);
				return true;
			    case 4:
				d.sendOption("What friends are these?", "Why do they need help? Are they in trouble?", "I wish I could help, but I'm busy at the moment.", "Ok, thanks.");
				return true;
			    case 5:
				switch(optionId) {
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
				switch(optionId) {
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
				switch(optionId) {
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
		    case QUEST_COMPLETE:
			switch (d.getChatId()) {
			    case 1:
				d.sendNpcChat("Thank you again!", Dialogues.HAPPY);
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