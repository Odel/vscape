package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;


public class ClockTower implements Quest{

	public static final int questIndex = 7353;
	
	public static final int QUEST_STARTED = 1;
	public static final int CLOCK_FIXED = 2;
	public static final int QUEST_COMPLETE = 3; 
	
	//Items
	public static final int BLACK_COG = 21;
	public static final int RED_COG = 23;
	public static final int BLUE_COG = 22;
	public static final int WHITE_COG = 20;
	public static final int ICE_GLOVES = 1580;
	public static final int BUCKET_OF_WATER = 1929;
	public static final int BUCKET = 1925;
	
	//Objects
	public static final int BLACK_SPINDLE = 30; 
	public static final int RED_SPINDLE = 29;
	public static final int BLUE_SPINDLE = 32;
	public static final int WHITE_SPINDLE = 31;
	
	//NPCS
	public static final int BROTHER_KOJO = 223;
	
	public int dialogueStage = 0; //what is this?
	
	public static final int questPointReward = 1;
	
	public int getQuestID() {
		return 39;
	}

	public String getQuestName() {
		return "Clock Tower";
	}

	public String getQuestSaveName() {
		return "clocktower";
	}

	public boolean canDoQuest(Player player) {
		// TODO Change to true when done
		return true;
	}

	public void getReward(Player player) {
		player.addQuestPoints(questPointReward);
		player.getActionSender().QPEdit(player.getQuestPoints());
		
	}

	public void completeQuest(Player player) {
		getReward(player);
		player.getActionSender().sendInterface(12140);
		//player.getActionSender().sendItemOnInterface(id, zoom, model) TODO Figure out what model coins are (this is icon on reward screen)
		player.getActionSender().sendString("You have completed" + getQuestName() + "!", 12144);
		player.getActionSender().sendString("You are awarded:", 12146);
		player.getActionSender().sendString("1 Quest Point", 12146);
		player.getActionSender().sendString("1125 coins", 12147);
		//TODO figure out the end section of reward screen
	}

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		//There should probably be a function like this somewhere else in a helper class or something, rather than in every quest
		String strikeArray[] = {
				"",""
		};
		for(int i = 1; i < questStage; i++)
		{
			strikeArray[i-1] = "@str@";			
		}
		
		//TODO In here add in pre start dialogue, maybe take it out of quest started and add it to the strike array
		
		if(questStage >= QUEST_STARTED)
		{
			player.getActionSender().sendString("@str@" + "I can start this quest by talking to @red@Brother Kojo @bla@ at the", 8147);
			player.getActionSender().sendString("@str@" + "@red@Clock Tower @bla@which is located @red@South @bla@of @red@Ardougne", 8148);
			player.getActionSender().sendString(strikeArray[0] + "To repair the clock i need to find the four coloured cogs", 8149);
			player.getActionSender().sendString(strikeArray[0] + "and place them on the four correctly coloured spindles", 8150);
			//TODO formating for quest log item coloring?
			player.getActionSender().sendString(cogPlaced(player.getQuestVars().getBlackCogPlaced())+ "Black cog still needs to be placed on it's spindle", 8151);
			player.getActionSender().sendString(cogPlaced(player.getQuestVars().getBlueCogPlaced())+ "Blue cog still needs to be placed on it's spindle", 8152);
			player.getActionSender().sendString(cogPlaced(player.getQuestVars().getRedCogPlaced())+ "Red cog still needs to be placed on it's spindle", 8153);
			player.getActionSender().sendString(cogPlaced(player.getQuestVars().getWhiteCogPlaced())+ "White cog still needs to be placed on it's spindle", 8154);
		}
		if(questStage >= 2)
		{
			player.getActionSender().sendString(strikeArray[1] + "I should talk to Brother Kojo now the clock is fixed.", 8156);
		}
	}
	
	public String cogPlaced(boolean Cog)
	{
		if(Cog)
		{
			return "@str@";
		}
		return "";
	}
	
	public static boolean cogInInventory(final Player player)
	{
		return player.getInventory().playerHasItem(BLACK_COG) || player.getInventory().playerHasItem(BLUE_COG) || player.getInventory().playerHasItem(RED_COG) || player.getInventory().playerHasItem(WHITE_COG);		
	}
	
	public static boolean itemPickupHandling(Player player, int itemId)
	{
		if(checkIfCog(itemId))
		{
			if(!cogInInventory(player))
			{
				if(itemId == BLACK_COG)
				{
					if(player.getEquipment().getId(Constants.HANDS) == ICE_GLOVES)
					{
						return false;
					}
					else if(player.getQuestVars().getBlackCogQuenched()) // bucket has been used with cog
					{
						return false;
					}
					else
					{
						player.getDialogue().sendPlayerChat("I think i'll burn my hands getting that!", CONTENT);
						return true;
					}
				}
				else
				{
					return false;
				}
			}
		}
		player.getDialogue().sendPlayerChat("It's to heavy for me to pick up at the moment", CONTENT);
		return true;
	}
		
	public static boolean checkIfCog(int itemId)
	{
			switch(itemId)
			{
			case BLACK_COG:
				return true;
			case BLUE_COG:
				return true;
			case RED_COG:
				return true;
			case WHITE_COG:
				return true;
			default:
				return false;
			}
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), questIndex);
	}

	public boolean questCompleted(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if(questStage >= QUEST_COMPLETE)
		{
			return true;
		}
		return false;
	}

	public int getQuestPoints() {
		return questPointReward;
	}

	@Override
	public void showInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
		player.getActionSender().sendString(getQuestName(), 8144);
	}

	public boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
		switch (object) {

		}
		return false;
	}
	
	public void handleDeath(final Player player, final Npc died) {

	}

	@Override
	public void sendQuestTabStatus(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		String Color = "@red@";
		if((questStage >= QUEST_STARTED) && (questStage < QUEST_COMPLETE))
		{
			Color = "@yel@";
		}
		else if(questStage == QUEST_COMPLETE)
		{
			Color = "@gre@";
		}
		player.getActionSender().sendString(Color + getQuestName(), questIndex);
		
	}

	@Override
	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);		
	}

	@Override
	public boolean doNpcClicking(Player player, Npc npc) {
		//Auto-generated method stub
		return false;
	}

	@Override
	public boolean doItemOnNpc(Player player, int itemId, Npc npc) {
		//Auto-generated method stub
		return false;
	}

	@Override
	public boolean itemHandling(Player player, int itemId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean itemOnItemHandling(Player player, int firstItem,
			int secondItem, int firstSlot, int secondSlot) {
		// TODO find out where this code actually should go
		player.getActionSender().sendMessage("Hot item on item handling");
		if(secondItem == BLACK_COG)
		{
			player.getActionSender().sendMessage("Second Item is black cog");
			if(firstItem == BUCKET_OF_WATER)
			{
				player.getQuestVars().setBlackCogQuenched(true);
				player.getInventory().removeItem(new Item(BUCKET_OF_WATER));
				player.getInventory().addItem(new Item(BUCKET));
			}
		}
		return false;
	}

	@Override
	public boolean doItemOnObject(Player player, int object, int item) {
		switch(object) {
		case BLACK_SPINDLE:
			if(item == BLACK_COG)
			{
				player.getQuestVars().setBlackCogPlaced(true);
				player.getActionSender().sendMessage("The cog slides slowly onto the firm spindle and locks in place.");
				player.getInventory().removeItem(new Item(BLACK_COG));
				return true;
			}
			else
			{
				player.getActionSender().sendMessage("This item doesn't fit on this spindle.");
				return true;
			}
			
		case BLUE_SPINDLE:
			if(item == BLUE_COG)
			{
				player.getQuestVars().setBlueCogPlaced(true);
				player.getActionSender().sendMessage("The cog slides slowly onto the firm spindle and locks in place.");
				player.getInventory().removeItem(new Item(BLUE_COG));
				return true;
			}
			else
			{
				player.getActionSender().sendMessage("This item doesn't fit on this spindle.");
				return true;
			}
		case RED_SPINDLE:
			if(item == RED_COG)
			{
				player.getQuestVars().setRedCogPlaced(true);
				player.getActionSender().sendMessage("The cog slides slowly onto the firm spindle and locks in place.");
				player.getInventory().removeItem(new Item(RED_COG));
				return true;
			}
			else
			{
				player.getActionSender().sendMessage("This item doesn't fit on this spindle.");
				return true;
			}
		case WHITE_SPINDLE:
			if(item == WHITE_COG)
			{
				player.getQuestVars().setWhiteCogPlaced(true);
				player.getActionSender().sendMessage("The cog slides slowly onto the firm spindle and locks in place.");
				player.getInventory().removeItem(new Item(WHITE_COG));
				return true;
			}
			else
			{
				player.getActionSender().sendMessage("This item doesn't fit on this spindle.");
				return true;
			}
		}
		return false;

	}

	@Override
	public boolean doObjectClicking(Player player, int object, int x, int y) {		
		switch(object)
		{
		case 1586:
			player.getDialogue().sendNpcChat("you are trying to pass through wall", CONTENT);
			player.getActionSender().walkTo(player.getPosition().getX() < 2576 ? 1 : -1, 0, true);
			player.getActionSender().walkThroughDoor(object, x, y, 0);
			return true;
		}
		return false;
	}

	@Override
	public boolean sendDialogue(Player player, int id, int chatId,
			int optionId, int npcChatId) {
		switch(id){
		case BROTHER_KOJO:
			switch(player.getQuestStage(this.getQuestID()))
			{
			case 0:
				switch (player.getDialogue().getChatId()) {
				case 1:
					player.getDialogue().sendPlayerChat("Hello monk.", CONTENT);
					return true;
				case 2:
					player.getDialogue().sendNpcChat("Hello adventurer. My name is Brother Kojo.", "Do you happen to know the time?", CONTENT);
					return true;
				case 3:
					player.getDialogue().sendPlayerChat("No, sorry i don't", HAPPY);
					return true;
				case 4:
					player.getDialogue().sendNpcChat("Exactly! This clock tower has recently broken down,", "and without it nobody can tell the correct time.", "I must fix it before the town people become too angry", DISTRESSED);
					return true;
				case 5:
					player.getDialogue().sendNpcChat("I don't suppose you could assist me in the repairs?", "I'll pay you for your help.", DISTRESSED);
					return true;
				case 6:
					player.getDialogue().sendOption("OK old monk, what can i do?", "How much reward are we talking?", "Not now old monk");
				return true;
				case 7:
					switch(optionId) {
						case 1:
							player.getDialogue().sendPlayerChat("Sure i'd be happy to help, what can i do?", CONTENT);
							player.getDialogue().setNextChatId(8);
							return true;
						case 2:
							player.getDialogue().sendPlayerChat("How much reward are we talking?", CONTENT);
							player.getDialogue().setNextChatId(10);
							return true;
						case 3:
							player.getDialogue().sendPlayerChat("Not now old monk", CONTENT);
							player.getDialogue().setNextChatId(12);
							return true;
					}
				case 8:
					player.getDialogue().sendNpcChat("Oh, thank you kind sir!", "In the cellar below, you'll find four cogs they're too heavy", "for me, but you should be able to carry them one at a time.", CONTENT);
					return true;
				case 9:
					//TODO here advance the quest to started
					player.getDialogue().sendNpcChat("I know one goes on each floor...", "but I can't exactly remember which goes where specifically.", "Oh well, I'm sure you can figure it out fairly easily.", CONTENT);
					player.getDialogue().endDialogue();
					QuestHandler.startQuest(player, 39);
					return true;
				case 10:
					player.getDialogue().sendNpcChat("Well, i'm only a monk so i'm not exactly rich,", "but i assure you i will give you a fair reward for", "the time spent assisting me in repairing the clock.", CONTENT);
					return true;
				case 11:
					player.getDialogue().sendNpcChat("So how about it?", CONTENT);
					player.getDialogue().setDialogueId(6);
					return true;
				case 12:
					player.getDialogue().sendNpcChat("Ok then, come back and let me know", "when you change your mind.", DISTRESSED);
					player.getDialogue().endDialogue();
					return true;
				}
			case QUEST_STARTED:
				switch(player.getDialogue().getChatId()){
					case 1:
						player.getDialogue().sendNpcChat("Have you replaced all the cogs yet?", DISTRESSED);
						return true;
					case 2:
						player.getDialogue().sendPlayerChat("Not yet, i'm still working on it.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
				}
			case CLOCK_FIXED:
				switch(player.getDialogue().getChatId()){
				case 1:
					player.getDialogue().sendPlayerChat("I have replaced all the cogs!", CONTENT);
					return true;
				case 2:
					player.getDialogue().sendNpcChat("Really...? Wait, Listen!", "Well done, well done! Yes yes yes, you've done it!", "You ARE clever! The townsfolk will be able to know the correct time now!", HAPPY);
					return true;
				case 3:
					player.getDialogue().sendNpcChat("Thank you so much for all of your help!", "And as promised, here is your reward!", HAPPY);
					player.getDialogue().endDialogue();
					QuestHandler.completeQuest(player, this.getQuestID());
					return true;
				}
				return true;
			case QUEST_COMPLETE:
				switch(player.getDialogue().getChatId()) {
				case 1:
					player.getDialogue().sendNpcChat("Thanks to you the clock is working smoothly!", HAPPY);
					return true;
				case 2:
					player.getDialogue().sendPlayerChat("No problem, happy to help anytime.", LAUGHING);
					player.getDialogue().endDialogue();
					return true;
				}
				return true;
			}
		}
		return false;
	}

}
