package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.consumables.Food;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.dialogue.DialogueManager;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_2;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.agility.Agility;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;

public class NatureSpirit implements Quest {
    public static final int questIndex = 8137; //Used in player's quest log interface, id is in Player.java //Change
    
    //Quest stages
    public static final int QUEST_STARTED = 1;
    public static final int QUEST_COMPLETE = 10;

    //Items
    public static final int APPLE_PIE = 2323;
    public static final int MEAT_PIE = 2327;
    public static final int DRUID_POUCH_EMPTY = 2957;
    public static final int DRUID_POUCH = 2958;
    public static final int ROTTEN_FOOD = 2959;
    public static final int SILVER_SICKLE = 2961;
    public static final int SILVER_SICKLE_B = 2963;
    public static final int WASHING_BOWL = 2964;
    public static final int MIRROR = 2966;
    public static final int JOURNAL = 2967;
    public static final int DRUIDIC_SPELL = 2968;
    public static final int USED_SPELL = 2969;
    public static final int MORT_MYRE_FUNGUS = 2970;
    public static final int MORT_MYRE_STEM = 2972;
    public static final int MORT_MYRE_PEAR = 2974;
    public static final int SICKLE_MOULD = 2976;
    
    //Positions
    public static final Position POSITION = new Position(0, 0, 0);

    //Interfaces
    public static final int INTERFACE = -1;

    //Npcs
    public static final int DREZEL = 1049;
    public static final int FILLIMAN_TARLOCK = 1050;
    public static final int NATURE_SPIRIT = 1051;
    public static final int GHAST = 1052;
    public static final int GHAST_ATTACKABLE = 1053;
    public static final int ULIZIUS = 1054;
    
    //Objects
    public static final int GROTTO_BRIDGE = 3522;
    public static final int GROTTO_ENTRANCE = 3516;
    
    public int dialogueStage = 0;

    private int reward[][] = { //{itemId, count},

    };

    private int expReward[][] = { //{skillId, exp},
	{Skill.CRAFTING, 3000},
	{Skill.DEFENCE, 2000},
	{Skill.HITPOINTS, 2000}
    };

    private static final int questPointReward = 2; //Change

    public int getQuestID() { //Change
	return 37;
    }

    public String getQuestName() { //Change
	return "Nature Spirit";
    }

    public String getQuestSaveName() { //Change
	return "naturespirit";
    }

    public boolean canDoQuest(final Player player) {
	return QuestHandler.questCompleted(player, 2) && QuestHandler.questCompleted(player, 23);
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
	player.getActionSender().sendItemOnInterface(12145, 250, SILVER_SICKLE); //zoom, then itemId
	player.getActionSender().sendString("You have completed " + getQuestName() + "!", 12144);
	player.getActionSender().sendString("You are rewarded: ", 12146);
	player.getActionSender().sendString("2 Quest Points", 12150);
	player.getActionSender().sendString("6750 Crafting XP", 12151);
	player.getActionSender().sendString("4500 Defence XP", 12152);
	player.getActionSender().sendString("4500 Hitpoints XP", 12153);
	player.getActionSender().sendString("", 12154);
	player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
	player.getActionSender().sendString(" ", 12147);
	player.setQuestStage(getQuestID(), QUEST_COMPLETE);
	player.getActionSender().sendString("@gre@" + getQuestName(), questIndex);
    }

    public void sendQuestRequirements(Player player) {
	int questStage = player.getQuestStage(getQuestID());
	switch (questStage) {
	    case QUEST_STARTED:
		player.getActionSender().sendString("@str@" + "You can begin this quest by talking to @dre@Drezel @bla@at", 8147); //default quest log to begin quest
		player.getActionSender().sendString("@str@" + "the mouth of the river @dre@Salve.", 8148);
		//Change
		player.getActionSender().sendString("Drezel told me to seek out Filliman, a druid in Mort Myre.", 8150);
		player.getActionSender().sendString("He should be south in the swamp, I have food for him.", 8151);
		break;
	    case QUEST_COMPLETE:
		player.getActionSender().sendString("You can begin this quest by talking to @dre@Drezel @bla@at", 8147); //default quest log to begin quest
		player.getActionSender().sendString("the mouth of the river @dre@Salve.", 8148);
		//Change
		player.getActionSender().sendString("@red@" + "You have completed this quest!", 8177);
		break;
	    default:
		player.getActionSender().sendString("You can begin this quest by talking to @dre@Drezel @bla@at", 8147); //default quest log to begin quest
		player.getActionSender().sendString("the mouth of the river @dre@Salve.", 8148);
		player.getActionSender().sendString("@dre@Requirements:", 8149);
		if (QuestHandler.questCompleted(player, 2)) {
		    player.getActionSender().sendString("@str@-The Restless Ghost.", 8151);
		} else {
		    player.getActionSender().sendString("@dbl@-The Restless Ghost.", 8151);
		}
		if (QuestHandler.questCompleted(player, 23)) {
		    player.getActionSender().sendString("@str@-Priest In Peril.", 8152);
		} else {
		    player.getActionSender().sendString("@dbl@-Priest In Peril.", 8152);
		}
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
    
    public boolean itemHandling(final Player player, int itemId) {
	switch(itemId) {

	}
	return false;
    }
    public static Food.FoodData findSpoilableFood(final Player player) {
	for(Item i : player.getInventory().getItemContainer().toArray()) {
		if(i == null) {
		    continue;
		}
		for(Food.FoodData f : Food.FoodData.values()) {
		    for(int id : f.getFoodId()) {
			if(id == i.getId()) {
			    player.setTempInteger(id);
			    return f;
			}
		    }
		}
	}
	return null;
    }
    public static void handleSpoilFood(final Npc npc, final Player player) {
	if (findSpoilableFood(player) != null) {
	    Food.FoodData f = findSpoilableFood(player);
	    if (Misc.random(3) == 0) {
		player.getInventory().replaceItemWithItem(new Item(player.getTempInteger()), new Item(ROTTEN_FOOD));
		if (f.getNewItemId() != -1) {
		    player.getActionSender().sendMessage("The Ghast spoils some of your food!", true);
		    player.getInventory().addItemOrDrop(new Item(f.getNewItemId()));
		}
	    } else {
		player.getActionSender().sendMessage("An attacking Ghast just misses you.", true);
	    }
	} else {
	    player.hit(Misc.random(2), HitType.NORMAL);
	}
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
	    case 3525:
	    case 3526: //Grotto exit
		//player.fadeTeleport()
	    case GROTTO_ENTRANCE:
		if(player.getQuestStage(this.getQuestID()) == 1) {
		    Dialogues.sendDialogue(player, FILLIMAN_TARLOCK, 50, 0);
		    return true;
		} else {
		    player.fadeTeleport(new Position(3442, 9734, 0));
		}
		return true;
	    case GROTTO_BRIDGE:
		int targetY = player.getPosition().getY() < y ? 3 : -3;
	    	Agility.climbObstacle(player, x, y + targetY, player.getPosition().getZ(), 2750, 2, 0);
		return true;
	    case 3507:
	    case 3506: //Mort myre gate
		if(player.getPosition().getY() >= 3458) {
		    player.getActionSender().sendMessage("You walk into the gloomy atmosphere of Mort Myre.");
		}
		player.getActionSender().walkThroughDoubleDoor(3506, 3507, 3444, 3458, 3443, 3458, 0);
		player.getActionSender().walkTo(0, player.getPosition().getY() < 3458 ? 1 : -1, true);
		return true;
	}
	return false;
    }

    public static boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
	switch (object) {
	    case 3517: //Search grotto
		if(!player.getInventory().playerHasItem(JOURNAL)) {
		    player.getDialogue().sendStatement("You search the strange rock. You find a knot and", "inside of it you discover a small tome. The words on", "the front are a bit vague, but you make out the words", "'Tarlock' and 'journal'.");
		    player.getInventory().addItem(new Item(JOURNAL));
		    return true;
		}
	}
	return false;
    }

    public boolean sendDialogue(final Player player, final int id, int chatId, int optionId, int npcChatId) {
	DialogueManager d = player.getDialogue();
	switch (id) { //Npc ID
	    case FILLIMAN_TARLOCK:
		switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
		    case QUEST_STARTED:
			switch (d.getChatId()) {
			    case 1:
				if(player.getEquipment().getId(Constants.AMULET) == GhostsAhoy.GHOSTSPEAK_AMULET) {
				    d.sendNpcChat("Cannot wake up... Where am I?", SAD);
				} else {
				    d.sendNpcChat("OOohohOOOOOOHHhh....", SAD);
				}
				return true;
			    case 2:
				if(player.getEquipment().getId(Constants.AMULET) == GhostsAhoy.GHOSTSPEAK_AMULET) {
				    d.sendPlayerChat("Huh? What's this?", CONTENT);
				} else {
				    d.sendStatement("You need to be wearing a Ghostspeak Amulet to understand this ghost.");
				    d.endDialogue();
				}
				return true;
			    case 3:
				d.sendNpcChat("What did I write down now? Put it in the knot hole.", "Save me...", CONTENT);
				return true;
			    case 4:
				d.sendPlayerChat("What are you talking about?", CONTENT);
				return true;
			    case 5:
				d.sendNpcChat("OH! I understand you! At last, someone who doesn't", "just mumble. I understand what you're saying!", HAPPY);
				return true;
			    case 6:
				d.sendOption("I'm wearing an amulet of ghost speak!", "How long have you been a ghost?", "What's it like being a ghost?", "Do you believe in skeletons?", "Ok, thanks.");
				return true;
			    case 7:
				switch(optionId) {
				    case 1:
					d.sendPlayerChat("I'm wearing an amulet of ghost speak!", CONTENT);
					return true;
				    case 2:
					d.sendPlayerChat("How long have you been a ghost?", CONTENT);
					d.setNextChatId(15);
					return true;
				    case 3:
					d.sendPlayerChat("What's it like being a ghost?", CONTENT);
					d.setNextChatId(18);
					return true;
				    case 4:
					d.sendPlayerChat("Do you believe in skeletons?", CONTENT);
					d.setNextChatId(21);
					return true;
				    case 5:
					d.sendPlayerChat("Ok, thanks.", CONTENT);
					d.endDialogue();
					return true;
				}
			    case 8:
				d.sendNpcChat("Why you poor fellow, have you passed away and you", "want to send a message back to a loved one?", CONTENT);
				return true;
			    case 9:
				d.sendPlayerChat("Err... Not exactly...", CONTENT);
				return true;
			    case 10:
				d.sendNpcChat("You have come to haunt my dreams until I pass on", "your message to a dearly loved one. I understand.", "Pray, tell me who you would like me to pass a", "message on to?", CONTENT);
				return true;
			    case 11:
				d.sendPlayerChat("Ermm, you don't understand... It's just that...", CONTENT);
				return true;
			    case 12:
				d.sendNpcChat("Yes!", HAPPY);
				return true;
			    case 13:
				d.sendPlayerChat("Well, please don't be upsert or anything...", "But you're the ghost!", CONTENT);
				return true;
			    case 14:
				d.sendNpcChat("Don't be silly now! That in no way reflects the truth!", DISTRESSED);
				d.setNextChatId(6);
				return true;
			    case 15:
				d.sendNpcChat("What?! Don't be preposterous! I'm not a ghost! How", "could you say something like that?", DISTRESSED);
				return true;
			    case 16:
				d.sendPlayerChat("But it's true, you're a ghost... at least that is", "to say, you're sort of not alive anymore.", CONTENT);
				return true;
			    case 17:
				d.sendNpcChat("Don't be silly, I can see you, I can see that tree. If I", "were dead, I wouldn't be able to see anything... What", "you say just doesn't reflect the truth. YOu'll have to try", "harder to pull one over on me!", CONTENT);
				d.setNextChatId(6);
				return true;
			    case 18:
				d.sendNpcChat("Oh, it's quite... Oh... Trying to catch me out were you!", "Anyone can clearly see that I am not a ghost!", ANGRY_1);
				return true;
			    case 19:
				d.sendPlayerChat("But you are a ghost, look at yourself! I can see", "straight through you! You're as dead as this swamp!", "Err.. No offense or anything...", CONTENT);
				return true;
			    case 20:
				d.sendNpcChat("No I won't take offense because I'm not dead and I'm", "afraid you'll have to come up with some pretty", "conclusive proof before I believe it. What a strange", "dream this is.", CONTENT);
				d.setNextChatId(6);
				return true;
			    case 21:
				d.sendNpcChat("Those scary things with all the bones?", "Heavens no!", CONTENT);
				return true;
			    case 22:
				d.sendPlayerChat("But skeletons are quite real. You're lucky you", "didn't become one yourself, being dead and all.", CONTENT);
				return true;
			    case 23:
				d.sendNpcChat("Gah! I'm not dead! These skeletons you speak of", "don't exist anyways! I could never become one!", "You can't fool me into thinking I'm dead to satiate", "whatever strange desire you have for me to be dead.", ANGRY_2);
				d.setNextChatId(6);
				return true;
			    case 50:
				d.sendStatement("A shifting apparition appears in front of you.");
				d.setNextChatId(1);
				if(!NpcLoader.checkSpawn(player, FILLIMAN_TARLOCK)) {
				    NpcLoader.spawnNpc(player, new Npc(FILLIMAN_TARLOCK), false, false);
				}
				return true;
			}
		    return false;
		}
	    return false;
	    case DREZEL:
		switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
		    case QUEST_STARTED:
			switch (d.getChatId()) {
			    case 1:
				d.sendNpcChat("Please take that food to Filliman, he'll probably appreciate", "a bit of cooked food. Now, he's never revealed where he", "lives in the swamps but I guess he'd be to the south.", CONTENT);
				d.endDialogue();
				return true;
			}
		    return false;
		    case 0:
			switch (d.getChatId()) {
			    case 1:
				if(this.canDoQuest(player)) {
				    d.sendNpcChat("Greetings again adventurer. How go your travels in", "Morytania? Is it as evil as I have heard?", Dialogues.HAPPY);
				    return true;
				} else {
				    return false;
				}
			    case 2:
				d.sendOption("Well, I'm going to look around a bit more.", "Is there anything else interesting to do around here?");
				return true;
			    case 3:
				switch(optionId) {
				    case 1:
					d.sendPlayerChat("Well, I'm going to look around a bit more.", CONTENT);
					d.endDialogue();
					return true;
				    case 2:
					d.sendPlayerChat("Is there anything else interesting to do around here?", CONTENT);
					d.setNextChatId(30);
					return true;
				}
			    case 30:
				d.sendNpcChat("Well, not a great deal... but there is something you", "could do for me if you're interested. Though it is quite", "dangerous.", CONTENT);
				d.setNextChatId(4);
				return true;
			    case 4:
				d.sendOption("Sorry, not interested...", "Well, what is it, I may be able to help?");
				return true;
			    case 5:
				switch (optionId) {
				    case 1:
					d.sendPlayerChat("Sorry, not interested...", CONTENT);
					d.endDialogue();
					return true;
				    case 2:
					d.sendPlayerChat("Well, what is it, I may be able to help?", CONTENT);
					return true;
				}
			    case 6:
				d.sendNpcChat("There's a man called Filliman who lives in Mort Myre,", "I wonder if you could look for him? The swamps of", "Mort Myre are dangerous though, they're infested with", "Ghasts!", CONTENT);
				return true;
			    case 7:
				d.sendOption("Who is this Filliman?", "Where's Mort Myre?", "What's a Ghast?", "Yes, I'll go and look for him.", "Sorry, I don't think I can help.");
				return true;
			    case 8:
				switch (optionId) {
				    case 1:
					d.sendPlayerChat("Who is this Filliman?", CONTENT);
					return true;
				    case 2:
					d.sendPlayerChat("Where's Mort Myre?", CONTENT);
					d.setNextChatId(12);
					return true;
				    case 3:
					d.sendPlayerChat("What's a Ghast?", CONTENT);
					d.setNextChatId(15);
					return true;
				    case 4:
					d.sendPlayerChat("Yes, I'll go and look for him.", CONTENT);
					d.setNextChatId(20);
					return true;
				    case 5:
					d.sendPlayerChat("Sorry, I don't think I can help.", CONTENT);
					d.endDialogue();
					return true;
				}
			    case 9:
				d.sendNpcChat("Filliman Tarlock is his full name and he's a Druid. He", "lives in Mort Myre much like a hermit, but there's", "many a traveller who he's helped.", CONTENT);
				return true;
			    case 10:
				d.sendNpcChat("Most people that come this way tell stories of when they", "were lost and paths that just seemed to 'open up' before", "them! I think it was Filliman Tarlock helping out.", HAPPY);
				d.setNextChatId(7);
				return true;
			    case 12:
				d.sendNpcChat("Mort Myre is a decayed and dangerous swamp to the", "south. It was once a beautiful forest but has since", "become filled with vile emanations from within", "Morytania.", CONTENT);
				return true;
			    case 13:
				d.sendNpcChat("The swamp decays everything. We put a fence around", "it to stop unwary travellers going in. Anyone who dies", "in the swamp is forever cursed to haunt it as a Ghast.", "Ghasts attack travellers, turning food to rotten filth.", CONTENT);
				d.setNextChatId(7);
				return true;
			    case 15:
				d.sendNpcChat("A Ghast is a poor soul who died in Mort Myre.", "They're undead of a special class, they're untouchable", "as far as I'm aware!", CONTENT);
				return true;
			    case 16:
				d.sendNpcChat("Filliman knew how to tackle them, but I've not heard", "from him in a long time. Ghasts, when they attack, will", "devour any food you have. If you have no food, they'll", "draw their nourishment from you!", CONTENT);
				d.setNextChatId(7);
				return true;
			    case 20:
				d.sendNpcChat("That's great, but it is very dangerous. Are you sure", "you want to do this?", CONTENT);
				return true;
			    case 21:
				d.sendOption("Yes, I'm sure.", "Sorry, I don't think I can help.");
				return true;
			    case 22:
				switch (optionId) {
				    case 1:
					d.sendPlayerChat("Yes, I'm sure.", CONTENT);
					return true;
				    case 2:
					d.sendPlayerChat("Sorry, I don't think I can help.", CONTENT);
					d.endDialogue();
					return true;
				}
			    case 23:
				d.sendNpcChat("That's great! Many thanks! Now then, please be aware", "of the Ghasts, you cannot attack them, only Filliman", "knew how to take them on.", HAPPY);
				return true;
			    case 24:
				d.sendNpcChat("Just run from them if you can. If you start to get", "lost, try to make your way back to the temple.", CONTENT);
				return true;
			    case 25:
				d.sendGiveItemNpc("The cleric hands you some food.", "", new Item(APPLE_PIE), new Item(APPLE_PIE));
				if (!player.getInventory().playerHasItem(new Item(APPLE_PIE, 3))) {
				    player.getInventory().addItemOrDrop(new Item(APPLE_PIE, 3));
				}
				if (!player.getInventory().playerHasItem(new Item(MEAT_PIE, 3))) {
				    player.getInventory().addItemOrDrop(new Item(MEAT_PIE, 3));
				}
				return true;
			    case 26:
				d.sendNpcChat("Please take this food to Filliman, he'll probably appreciate", "a bit of cooked food. Now, he's never revealed where he", "lives in the swamps but I guess he'd be to the south,", "search for him will you?", CONTENT);
				return true;
			    case 27:
				d.sendPlayerChat("I'll do my very best, don't worry, if he's in there and", "he's still alive I'll definitely find him.", CONTENT);
				d.endDialogue();
				QuestHandler.startQuest(player, 37);
				return true;
			}
			return false;
		}
		return false;
	}
	return false;
    }

}
