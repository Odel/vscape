package com.rs2.model.content.quests.impl;

import com.rs2.Constants;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.SAD;

import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.*;
import com.rs2.model.Position;

public class CreatureofFenkenstrain implements Quest {
	
	public static final int QUEST_STARTED = 1;
	public static final int HIRED = 2;
	public static final int PARTS_ASSEMBLED = 3;
	public static final int PARTS_INANIMATE = 4;
	public static final int PARTS_ANIMATED = 5;
	public static final int TO_THE_TOWER = 6;
	public static final int TRUTH_DISCOVERED = 7;
	public static final int QUEST_COMPLETE = 8;
	
	//Items
	public static final int ARMS = 4195;
	public static final int LEGS = 4196;
	public static final int TORSO = 4194;
	public static final int HEAD = 4198;
	public static final int BRAIN = 4199;
	public static final int HEADe = 4197;
	public static final int NEEDLE = 1733;
	public static final int THREAD = 1734;
	public static final int GSAMMY = 552;
	public static final int GSAMMYE = 4250;
	public static final int CAVERN_KEY = 4184;
	public static final int SHED_KEY = 4186;
	public static final int TOWER_KEY = 4185;
	public static final int MARBLEAMMY = 4187;
	public static final int OBBYAMMY = 4188;
	public static final int STARAMMY = 4183;
	public static final int GARDENCANE = 4189;
	public static final int GARDENBRUSH = 4190;
	public static final int EXBRUSH1 = 4191;
	public static final int EXBRUSH2 = 4192;
	public static final int EXBRUSH3 = 4193;
	public static final int CONDUCTORMOULD = 4200;
	public static final int CONDUCTOR = 4201;
	public static final int BRONZEWIRE = 1794;
	
	//Objects
	public static final int PILEOFCANES = 5158;
	public static final int SHEDDOOR = 5174;
	public static final int CANIFISSIGN = 5164;
	public static final int CUPBOARD = 5156;
	public static final int CUPBOARDO = 5157;
	

	
	
	private int reward[][] = {
		{4202, 1} //itemID, amount
	};
	private int expReward[][] = {
		{Skill.THIEVING, 1000}
	};
	private static final int questPointReward = 2;

	public int getQuestID() {
		return 48;
	}

	public String getQuestName() {
		return "Creature of Fenkenstrain";
	}

	public String getQuestSaveName() {
		return "creature-of-fenkenstrain";
	}

	public boolean canDoQuest(Player player) {
		return true;
	}

	public void getReward(Player player) {
		for (int[] rewards : reward) {
			player.getInventory().addItem(new Item(rewards[0], rewards[1]));
		}
		for (int[] expRewards : expReward) {
			player.getSkill().addExp(expRewards[0], (expRewards[1])); //DO NOT TIMES EXP BY *Constants.EXP_RATE (its done by addexp)
		}
		player.addQuestPoints(questPointReward);
		player.getActionSender().QPEdit(player.getQuestPoints());
	}

	public void completeQuest(Player player) {
		getReward(player);
		player.getActionSender().sendInterface(12140);
		player.getActionSender().sendString("You have completed: " + getQuestName(), 12144);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString((int) (expReward[0][1] * Constants.EXP_RATE) + " Thieving Experience", 12151);
		player.getActionSender().sendString("", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), 12129);
	}

	public void sendQuestRequirements(Player player) {
		String prefix = "";
		int questStage = player.getQuestStage(getQuestID());
		if (questStage == QUEST_STARTED) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "I can start this quest by reading the signpost in the", 8147);
			player.getActionSender().sendString("@str@" + "centre of @red@Canifis.", 8148);

			player.getActionSender().sendString("I should go up to the castle and speak to @red@Dr Fenkenstrain.", 8149);
			
		} else if (questStage == HIRED) {
			
		} else if (questStage == PARTS_ASSEMBLED) {
			
		} else if (questStage == PARTS_ANIMATED) {
			
		} else if (questStage == TRUTH_DISCOVERED) {
			
		} else if (questStage == QUEST_COMPLETE) {
			
		} else {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString(prefix + "I can start this quest by reading the signpost in the", 8147);
			player.getActionSender().sendString(prefix + "centre of @red@Canifis.", 8148);
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 12129);
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
			player.getActionSender().sendString("@yel@" + getQuestName(), 12129);
		} else if (questStage >= QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 12129);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 12129);
		}

	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		String prefix = "";
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString(prefix + "I can start this quest by reading the signpost in the", 8147);
		player.getActionSender().sendString(prefix + "centre of @red@Canifis.", 8148);
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public boolean itemHandling(final Player player, int itemId) {
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		
		if((firstItem == BRAIN || secondItem == BRAIN) 
				&& (firstItem == HEADe || secondItem == HEADe)
				&& !(firstItem == BRAIN & secondItem == BRAIN)) { //Might make buying two brains impossible
			
			player.getInventory().replaceItemWithItem(new Item(firstItem), new Item(HEAD));
			player.getInventory().removeItem(new Item(secondItem));
			player.getActionSender().sendMessage("You stuff the brain into the empty head");
			return true;
		}
		
		if((firstItem == MARBLEAMMY || secondItem == MARBLEAMMY)
				&& (firstItem == OBBYAMMY || secondItem == OBBYAMMY)) { //Having 2 of the same AMMY impossible
			
			player.getInventory().replaceItemWithItem(new Item(firstItem), new Item(STARAMMY));
			player.getInventory().removeItem(new Item(secondItem));
			player.getActionSender().sendMessage("You combine the two triangles into a star");
			return true;
		}
		
		if ((firstItem == GARDENBRUSH || secondItem == GARDENBRUSH)
				&& (firstItem == GARDENCANE || secondItem == GARDENCANE)
				&& !(firstItem == GARDENCANE & secondItem == GARDENCANE)
				&& player.getInventory().playerHasItem(new Item(BRONZEWIRE))) {

			player.getInventory().replaceItemWithItem(new Item(firstItem), new Item(EXBRUSH1));
			player.getInventory().removeItem(new Item(secondItem));
			player.getInventory().removeItem(new Item(BRONZEWIRE));
			player.getActionSender().sendMessage("You combine the garden brush with the cane using some bronze wire");
			return true;
		}

		if ((firstItem == EXBRUSH1 || secondItem == EXBRUSH1)
				&& (firstItem == GARDENCANE || secondItem == GARDENCANE)
				&& !(firstItem == GARDENCANE & secondItem == GARDENCANE)
				&& player.getInventory().playerHasItem(new Item(BRONZEWIRE))) {

			player.getInventory().replaceItemWithItem(new Item(firstItem), new Item(EXBRUSH2));
			player.getInventory().removeItem(new Item(secondItem));
			player.getInventory().removeItem(new Item(BRONZEWIRE));
			player.getActionSender().sendMessage("You combine the extended brush with the cane using some bronze wire");
			return true;
		}

		if ((firstItem == EXBRUSH2 || secondItem == EXBRUSH2)
				&& (firstItem == GARDENCANE || secondItem == GARDENCANE)
				&& !(firstItem == GARDENCANE & secondItem == GARDENCANE)
				&& player.getInventory().playerHasItem(new Item(BRONZEWIRE))) {

			player.getInventory().replaceItemWithItem(new Item(firstItem), new Item(EXBRUSH3));
			player.getInventory().removeItem(new Item(secondItem));
			player.getInventory().removeItem(new Item(BRONZEWIRE));
			player.getActionSender().sendMessage("You combine the extended brush with the cane using some bronze wire");
			return true;
		}
		
		
		return false;
	}

	public boolean doItemOnObject(final Player player, int object, int item) {
		//TODO(saxi): Star on Grave, Lightning rod on hub, and broom on fire place interactions
		//NOTE(saxi): Could also add in key on door interactions if wanted also experiment cave door consumes key
		return false;
	}

	public boolean doObjectClicking(final Player player, int object, int x, int y) {
		switch(object) {
		case CANIFISSIGN: 
			player.getDialogue().sendStatement("The signpost has a note pinned onto it. The note says:",
					"'~~~~Braindead Butler Wanted~~~~",
					"Gravedigging skills essential ~ Hunchback advantageous",
					"See Dr Fenkenstrain at the castle NE of Canifis'");
			startQuest(player);
			return true;
		case 5166: //Bookcase
			if(x == 3542 && y == 3558) { //Bookcase east
				
				
			} else if(x == 3555 && y == 3558) { //Bookcase 2 west
				
				
			}
			return true;
		case 5206: //Castle Stairs Up
			if(x == 3559 && y == 3551) { //West
				
				player.teleport(new Position(3559, 3554, 1));
				return true;
			} else if (x == 3537 && y == 3551) { //East
				
				player.teleport(new Position(3538, 3554, 1));
				return true;
			}
			return false;
		case 5207: //Castle Stairs Down
			if(x == 3537 && y == 3552) { //East
				
				player.teleport(new Position(3538, 3550, 0));
				return true;
			} else if(x == 3559 && y == 3552) { //West
				
				player.teleport(new Position(3559, 3550, 0));
				return true;
			}
			return false;
		case SHEDDOOR: //When the key is missing, the door just opens
			if(player.getInventory().playerHasItem(SHED_KEY)) {
				
				player.getActionSender().walkTo(player.getPosition().getX() < 3548 ? 1 : -1 , 0, true);
				player.getActionSender().walkThroughDoor(SHEDDOOR, 3548, 3565, 0);
				player.getActionSender().sendMessage("You unlock the door using the Shed Key");
				return true;
			} else {
				
				player.getActionSender().sendMessage("The door appears to be locked.");
				return true;
			}
		case PILEOFCANES:
			player.getActionSender().sendMessage("You grab a cane from the pile");
			player.getInventory().addItem(new Item(GARDENCANE, 1));
			return true;
		case CUPBOARD:
			
			return true;
		}
		return false;
	}

	public boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
		switch (object) {

		}
		return false;
	}

	public void handleDeath(final Player player, final Npc died) {

	}
	
	//TODO(saxi): Need to go through and adjust the chat moods
	//TODO(saxi): Pickpocketing Fenkenstrain is supposed to produce dialogue of him catching you 
	//(probably to produce Ring of Charos after you've done the quest)
	public boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
		
		int questStage = player.getQuestStage(getQuestID());
		String[][] gender = {{ "man", "woman" },{"sir", "miss"}};
		
		switch (id) {
		case 1670: //Dr. Fenkenstrain
			switch(questStage) {
			case QUEST_STARTED:
				switch(player.getDialogue().getChatId()) {
				case 1:
					player.getDialogue().sendNpcChat("Have you come to apply for the job?", CONTENT);
					return true;
				case 2:
					player.getDialogue().sendOption("Yes", "No");
					return true;
				case 3:
					switch(optionId) {
					case 1:
						player.getDialogue().sendPlayerChat("Yes, if it pays well", CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					case 2:
						player.getDialogue().sendPlayerChat("No", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					}
					return true;
				case 4:
					player.getDialogue().sendNpcChat("I'll have to ask you some questions first.", CONTENT);
					return true;
				case 5:
					player.getDialogue().sendPlayerChat("Okay", CONTENT);
					return true;
				case 6:
					player.getDialogue().sendNpcChat("How would you describe yourself in one word?", CONTENT);
					return true;
				case 7:
					player.getDialogue().sendOption("Stunning", "Awe-inspiring", "Breathtaking", "Braindead");
					
					return true;
				case 8:
					switch(optionId) {
					case 1:
						player.getDialogue().sendNpcChat("Mmmm, I see.", CONTENT);
						player.getDialogue().setNextChatId(9);
						player.getQuestVars().questionComparator = false;
						return true;
					case 2:
						player.getDialogue().sendNpcChat("Mmmm, I see.", CONTENT);
						player.getDialogue().setNextChatId(9);
						player.getQuestVars().questionComparator = false;
						return true;
					case 3:
						player.getDialogue().sendNpcChat("Mmmm, I see.", CONTENT);
						player.getDialogue().setNextChatId(9);
						player.getQuestVars().questionComparator = false;
						return true;
					case 4:
						player.getDialogue().sendNpcChat("Mmmm, I see.", CONTENT); //NOTE(saxi): Probably not CONTENT here, maybe pondering or something
						player.getDialogue().setNextChatId(9);
						player.getQuestVars().questionComparator = true;
						return true;
					}
				case 9:
					player.getDialogue().sendNpcChat("Just one more question.", "What would you say is your greatest skill?", CONTENT);
					return true;
				case 10:
					player.getDialogue().sendOption("Combat.", "Memes.", "Cooking.", "Grave-digging.");
					return true;
				case 11:
					switch(optionId) {
					case 1:
						player.getDialogue().sendNpcChat("Mmmm, I see.", CONTENT);
						player.getDialogue().setNextChatId(12);
						player.getQuestVars().questionComparator = false;
						return true;
					case 2:
						player.getDialogue().sendNpcChat("Mmmm, I see.", CONTENT);
						player.getDialogue().setNextChatId(12);
						player.getQuestVars().questionComparator = false;
						return true;
					case 3:
						player.getDialogue().sendNpcChat("Mmmm, I see.", CONTENT);
						player.getDialogue().setNextChatId(12);
						player.getQuestVars().questionComparator = false;
						return true;
					case 4:
						player.getDialogue().sendNpcChat("Mmmm, I see", CONTENT); //Also probably not CONTENT, pondering
						player.getDialogue().setNextChatId(12);
						player.getQuestVars().questionComparator = player.getQuestVars().questionComparator & true;
						return true;
					}
				case 12:
					if(!player.getQuestVars().questionComparator) {
						player.getDialogue().sendNpcChat("Did you even read my sign?", ANGRY_1);
						player.getDialogue().endDialogue();
					} else {
						player.getDialogue().sendNpcChat("Looks like you're just the " + gender[0][player.getGender()] + " for the job! Welcome aboard!", HAPPY);
					}
					return true;
				case 13:
					player.getDialogue().sendPlayerChat("Is there anything you'd like me to do for you, sir?", HAPPY);
					return true;
				case 14:
					player.getDialogue().sendNpcChat("Yes, there is. You're highly skilled at grave-digging, yes?", CONTENT); 
					return true;
				case 15:
					player.getDialogue().sendPlayerChat("Err...yes, that's what I said", CONTENT);
					return true;
				case 16:
					player.getDialogue().sendNpcChat("Excellent. Now listen carefully.", "I need you to find some...stuff...for me.", CONTENT);
					return true;
				case 17:
					player.getDialogue().sendPlayerChat("Memes!?", HAPPY);
					return true;
				case 18:
					player.getDialogue().sendNpcChat("That's what I said...Meme...wait, no! I said Stuff.", CONTENT);
					return true;
				case 19:
					player.getDialogue().sendPlayerChat("What kind of stuff?", CONTENT);
					return true;
				case 20:
					player.getDialogue().sendNpcChat("Well...dead stuff", CONTENT);
					return true;
				case 21:
					player.getDialogue().sendPlayerChat("Ayy Lmao", HAPPY);
					return true;
				case 22:
					player.getDialogue().sendNpcChat("What?", CONTENT);
					return true;
				case 23:
					player.getDialogue().sendPlayerChat("Go on...?", CONTENT);
					return true;
				case 24:
					player.getDialogue().sendNpcChat("I need you to get me enough dead body parts for me", "to stitch together a complete body,", "which I plan to bring to life.", HAPPY);
					return true;
				case 25:
					player.getDialogue().sendPlayerChat("Right...okay...if you insist", CONTENT);
					player.setQuestStage(getQuestID(), HIRED);
					player.getDialogue().endDialogue();
				}
				
				return true; //QUEST_STARTED
			case HIRED:
				//Look for body parts in player's inventory
				//Then resolve to dialogue tree
				if(player.getInventory().playerHasItem(LEGS) || player.getInventory().playerHasItem(ARMS) || player.getInventory().playerHasItem(HEAD) || player.getInventory().playerHasItem(TORSO)) {
					
					switch(player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendPlayerChat("I have some body parts for you.", CONTENT);
						return true;
					case 2:
						if(player.getInventory().playerHasItem(ARMS)) {
							player.getDialogue().sendNpcChat("Great, you've brought me some arms.", HAPPY);
							player.getInventory().removeItem(new Item(ARMS));
							player.getQuestVars().setBodyCompletion(0, true);
							if(player.getQuestVars().getBodyCompletion()) {
								player.setQuestStage(getQuestID(), PARTS_ASSEMBLED);
							}
						}
						return true;
					case 3:
						if(player.getInventory().playerHasItem(LEGS)) {
							player.getDialogue().sendNpcChat("Excellent, you've brought me smoe legs.", HAPPY);
							player.getInventory().removeItem(new Item(LEGS));
							player.getQuestVars().setBodyCompletion(1,  true);
							if(player.getQuestVars().getBodyCompletion()) {
								player.setQuestStage(getQuestID(), PARTS_ASSEMBLED);
							}
						}
						return true;
					case 4:
						if(player.getInventory().playerHasItem(TORSO)) {
							player.getDialogue().sendNpcChat("Splendid, you've brought me a torso.", HAPPY);
							player.getInventory().removeItem(new Item(TORSO));
							player.getQuestVars().setBodyCompletion(2, true);
							if(player.getQuestVars().getBodyCompletion()) {
								player.setQuestStage(getQuestID(), PARTS_ASSEMBLED);
							}
						}
						return true;
					case 5:
						if(player.getInventory().playerHasItem(HEAD)) {
							player.getDialogue().sendNpcChat("Fantastic, you've brought me a head.", HAPPY);
							player.getInventory().removeItem(new Item(HEAD));
							player.getQuestVars().setBodyCompletion(3, true);
							if(player.getQuestVars().getBodyCompletion()) {
								player.setQuestStage(getQuestID(), PARTS_ASSEMBLED);
							}
						}
						player.getDialogue().endDialogue();
						return true;
					}
				} else {
					
				switch(player.getDialogue().getChatId()) {
				case 1:
					player.getDialogue().sendNpcChat("WAH!...Oh it's just you. What did you want?", DISTRESSED);
					return true;
				case 2:
					player.getDialogue().sendOption("Do you know where I could find body parts?", 
													"What do you want me to do again?",
													"Why do you want to make a creature?",
													"Will this creature put me out of a job?",
													"I must get back to work, sir.");
					return true;
				case 3:
					switch(optionId) {
					case 1:
						player.getDialogue().sendPlayerChat("Do you know where I could find body parts?", CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					case 2:
						player.getDialogue().sendPlayerChat("What do you want me to do again?", CONTENT);
						player.getDialogue().setNextChatId(7);
						return true;
					case 3:
						player.getDialogue().sendPlayerChat("Why do you want ot make a creature?", CONTENT);
						player.getDialogue().setNextChatId(9);
						return true;
					case 4:
						player.getDialogue().sendPlayerChat("Will this creature put me out of a job?", CONTENT);
						player.getDialogue().setNextChatId(14);
						return true;
					case 5:
						player.getDialogue().sendPlayerChat("I must get back to work, sir.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					}
					
					return true;
				case 4:
					player.getDialogue().sendNpcChat("The soil of Morytania is unique in its ability", 
													 "to preserve the bodies of the dead,",
													 "which is one reason why I have chosen", 
													 "to carry out my experiments here.", CONTENT);
					return true;
				case 5:
					player.getDialogue().sendNpcChat("I recommend digging up some graves in the local area.",
													 "To the south-east you will find the Haunted Woods;", 
													 "I believe there are many graves there.", CONTENT);
							
					
					return true;
				case 6:
					player.getDialogue().sendNpcChat("There is also a mausoleum on an island west of this castle", 
													 "I expect the bodies that are",
													 "buried there to be extremely well preserved,",
													 "as they were wealthy in life", CONTENT);
					player.getDialogue().setNextChatId(2);
					return true;
				case 7:
					player.getDialogue().sendNpcChat("I need you to get me enough body parts", "to stitch together a complete body", "which I plan to bring to life", HAPPY);
					return true;
				case 8:
					player.getDialogue().sendPlayerChat("Right...okay...if you insist", CONTENT);
					player.getDialogue().setNextChatId(2);
					return true;
				case 9:
					player.getDialogue().sendNpcChat("I came to the land of Morytania many years ago,",
							"to find a safe sanctuary for my experiements.",
							"This abandonded castle suited my purposes exactly", CONTENT);
					return true;
				case 10:
					player.getDialogue().sendPlayerChat("What were you experimenting in?", CONTENT);
					return true;
				case 11:
					player.getDialogue().sendNpcChat("Oh, perfectly innocent experiments --", 
													 "for the good of mankind.", HAPPY);
					return true;
				case 12:
					player.getDialogue().sendPlayerChat("Then why did you need to come to Morytania?", CONTENT);
					return true;
				case 13:
					player.getDialogue().sendNpcChat("Enough questions, now. Get back to your work.", CONTENT);
					player.getDialogue().setNextChatId(2);
					return true;
				case 14:
					player.getDialogue().sendNpcChat("No, my friend.", "I have a very special purpose in mind for this creature.", CONTENT);
					player.getDialogue().setNextChatId(2);
					return true;
				}
				}
				return true; //HIRED
			case PARTS_ASSEMBLED:
				if(player.getQuestVars().sewing101[0] && (!player.getQuestVars().sewing101[1] || !player.getQuestVars().sewing101[2])) {
					switch(player.getDialogue().getChatId()) {
					case 1:
						if(player.getQuestVars().sewing101[1] && !player.getQuestVars().sewing101[2]) {
							player.getDialogue().sendNpcChat("Where is my thread, " + player.getUsername(), CONTENT);
							if(!player.getInventory().playerHasItem(THREAD, 5)) {
								player.getDialogue().endDialogue();
							} else {
								player.getDialogue().setNextChatId(3);
							}
							
						}
						else if(player.getQuestVars().sewing101[2] && !player.getQuestVars().sewing101[1]) {
							player.getDialogue().sendNpcChat("Where is my needle, " + player.getUsername(), CONTENT);
							if(!player.getInventory().playerHasItem(NEEDLE)) {
								player.getDialogue().endDialogue();
							}
							
						} else {
							player.getDialogue().sendNpcChat("Where are my needle and thread, " + player.getUsername(), CONTENT);
							if(!player.getInventory().playerHasItem(NEEDLE) && !player.getInventory().playerHasItem(THREAD, 5)) {
								player.getDialogue().endDialogue();
							} else if(player.getInventory().playerHasItem(THREAD, 5) && !player.getInventory().playerHasItem(NEEDLE)) {
								player.getDialogue().setNextChatId(3);
							}
						}
						return true;
					case 2:
						if(player.getInventory().playerHasItem(NEEDLE) && !player.getQuestVars().sewing101[1]) {
							player.getInventory().removeItem(new Item(NEEDLE));
							player.getDialogue().sendNpcChat("Ah, a needle. Wonderful.", CONTENT);
							player.getQuestVars().sewing101[1] = true;
							if(!player.getInventory().playerHasItem(THREAD, 5)) {
								player.getDialogue().endDialogue();
							}
						} else {
							player.getDialogue().setNextChatId(3);
						}
						if(player.getQuestVars().sewing101[1] && player.getQuestVars().sewing101[2]) {
							player.getDialogue().setNextChatId(15);
						}
						return true;
					case 3:
						if(player.getInventory().playerHasItem(THREAD, 5) && !player.getQuestVars().sewing101[2]) {
							player.getInventory().removeItem(new Item(THREAD, 5));
							player.getDialogue().sendNpcChat("Some thread. Excellent", CONTENT);
							player.getQuestVars().sewing101[2] = true;
						}
						if(player.getQuestVars().sewing101[1] && player.getQuestVars().sewing101[2]) {
							player.getDialogue().setNextChatId(15);
							
						} else {
							player.getDialogue().endDialogue();
						}
						return true;
					}
				} else if(player.getQuestVars().sewing101[1] && player.getQuestVars().sewing101[2]) {
					switch(player.getDialogue().getChatId()) {
					case 15: 
						player.getDialogue().sendStatement("Fenkenstrain uses the needle and thread to sew",
															"the body parts together. Soon, a hideous creature",
															"lies inanimate on the ritual table.");
						//Change out the ritual table for the one with the body.
						player.getDialogue().setNextChatId(1);
						return true;
					case 1:
						player.getDialogue().sendNpcChat("Perfect. But I need one more thing from you --", 
														 "flesh and bones by themselves do not make life.", CONTENT);
						return true;
					case 2:
						player.getDialogue().sendPlayerChat("No Shi...I-I mean, Really?", ANGRY_1);
						return true;
					case 3:
						player.getDialogue().sendNpcChat("I have hone to perfection an ancient ritual", 
														 "that will give life to this creature,",
														 "but for this I must harness the very power of nature", CONTENT);
						return true;
					case 4:
						player.getDialogue().sendPlayerChat("And what power is this?", CONTENT);
						return true;
					case 5:
						player.getDialogue().sendNpcChat("The power of lightning.", CONTENT);
						return true;
					case 6:
						player.getDialogue().sendPlayerChat("Sorry, can't make lightning, you've got the wrong " + gender[player.getGender()], CONTENT);
						return true;
					case 7:
						player.getDialogue().sendNpcChat("Silence your insolent tongue!", 
														 "The storm taht brews overhead will create the lightning.",
														 "What I need you to do is repair the lightning conductor",
														 "on the balcony above", ANGRY_1);
						return true;
					case 8:
						player.getDialogue().sendPlayerChat("Repair the lightning conductor, right.", 
															"Can I have a break, soon?", 
															"By law I'm entitled to 15 minutes every-", HAPPY);
						return true;
					case 9:
						player.getDialogue().sendNpcChat("Repair the conductor and BEGONE!!", ANGRY_1);
						return true;
					case 10:
						player.getDialogue().sendPlayerChat("What an ass.", SAD);
						player.setQuestStage(getQuestID(), PARTS_INANIMATE);
						player.getDialogue().endDialogue();
						return true;
					
					}
				} else {
					switch(player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendNpcChat("Superb! Those are all the parts I need.", 
														 "Now to sew them together...", HAPPY);
						return true;
					case 2:
						player.getDialogue().sendNpcChat("Oh bother! I haven't got a needle or thread!", 
														 "Go and get me a needle, and I'll need 5 lots of thread.", DISTRESSED);
						player.getQuestVars().sewing101[0] = true;
						player.getDialogue().endDialogue();
						return true;
					}
				}
				return true; //PARTS_ASSEMBLED
			case PARTS_INANIMATE:
				switch(player.getDialogue().getChatId()) {
				case 1:
					player.getDialogue().sendPlayerChat("How do I repair the lightning conductor?", CONTENT);
					return true;
				case 2:
					player.getDialogue().sendNpcChat("Oh, it would be easier to do it myself!", 
													 "If you find a conductor mould you should be able",
													 "to cast a new one. Remember this, " + player.getUsername(),
													 "my experiment will only work with a conductor made of silver.", ANGRY_1);
					player.getDialogue().endDialogue();
					return true;
				}
				return true; //PARTS_INANIMATE
			case PARTS_ANIMATED:
				switch(player.getDialogue().getChatId()) {
				case 1:
					player.getDialogue().sendPlayerChat("So did it work, then?");
					return true;
				case 2:
					player.getDialogue().sendNpcChat("Yes, I'm afraid it did, " + player.getUsername() + " -- all too well.");
					return true;
				case 3:
					player.getDialogue().sendPlayerChat("I can't see it anywhere.");
					return true;
				case 4:
					player.getDialogue().sendNpcChat("I tricked it into going up to the Tower,", 
													 "and there it remains, imprisoned");
					return true;
				case 5:
					player.getDialogue().sendPlayerChat("So the creature wasn't all you'd hoped, then?");
					return true;
				case 6:
					player.getDialogue().sendNpcChat("...oh, what have I done...");
					return true;
				case 7:
					player.getDialogue().sendPlayerChat("Oh, I see, we're developing a sense of right", 
														"and wrong now are we? Bit late for that, I'd say.");
					return true;
				case 8:
					player.getDialogue().sendNpcChat("I have no control over it! It's coming to get me!");
					return true;
				case 9:
					player.getDialogue().sendPlayerChat("What do you want me to do about it");
					return true;
				case 10:
					player.getDialogue().sendNpcChat("Destroy it!!! Take the key to the Tower and take back the life", 
													 "I never should have granted!!!");
					player.getInventory().addItem(new Item(TOWER_KEY, 1));
					player.getDialogue().endDialogue();
					return true;
					
				}
				return true;
			case TO_THE_TOWER:
				switch(player.getDialogue().getChatId()) {
				case 1:
					player.getDialogue().sendNpcChat("So have you destroyed it?!?!", DISTRESSED);
					return true;
				case 2:
					player.getDialogue().sendPlayerChat("Not yet");
					return true;
				case 3:
					player.getDialogue().sendNpcChat("Please, hurry -- save me!!!", DISTRESSED);
					return true;
			
				}
				return true;
			case TRUTH_DISCOVERED:
				switch(player.getDialogue().getChatId()) {
				case 1:
					player.getDialogue().sendNpcChat("So have you destroyed it?!?");
					return true;
				case 2:
					player.getDialogue().sendPlayerChat("Never, now that hae has told me the truth!");
					return true;
				case 3:
					player.getDialogue().sendNpcChat("Oh my, oh my, this is exactly what I feared!", 
													 "Why did you ahve to pick Rologarth's brain of all brains?!?");
					return true;
				case 4:
					player.getDialogue().sendPlayerChat("I'm through working for you.");
					return true;
				case 5:
					player.getDialogue().sendNpcChat("No! I refuse to release you! You must help me build", 
													 "another creatuer to destroy this dreadful mistake!!");
					player.getDialogue().endDialogue();
					return true;
				}
				return true;
			case QUEST_COMPLETE:
				switch(player.getDialogue().getChatId()) {
				case 1:
					player.getDialogue().sendNpcChat("theyrecomingtogetme theyrecomingtogetme...");
					return true;
				case 2:
					player.getDialogue().sendPlayerChat("It is all you deserve. Lord Roogarth is master of this castle", 
														"once more. Let him protect you -- if he wants to.");
					return true;
				case 3:
					player.getDialogue().sendNpcChat("theyrecomingtogetme theyrecomingtogetme...");
					player.getDialogue().endDialogue();
					return true;
				}
				return true;
			}
			return true;//end Dr Fenkenstrain
		case 1042: //Roavar
			
			switch(player.getDialogue().getChatId()) {
			case 1:
				player.getDialogue().sendPlayerChat("Can I buy something to eat?");
				return true;
			case 2:
				player.getDialogue().sendNpcChat("If you've got the money, I've got a real treat for you.");
				return true;
			case 3:
				player.getDialogue().sendPlayerChat("What have you got?");
				return true;
			case 4:
				player.getDialogue().sendNpcChat("Pickled brain, my friend. Only 50 gold for you.");
				return true;
			case 5:
				player.getDialogue().sendPlayerChat("Err...pickled from what animal?");
				return true;
			case 6:
				player.getDialogue().sendNpcChat("Animal? Don't be disgusting, man!", 
												 "No, this is a human brain --",
												 "only the best for my customers.");
				return true;
			case 7:
				player.getDialogue().sendOption("I'll buy one, please", "I'm afraid I'm not really hungry at the moment");
				return true;
			case 8:
				switch(optionId) {
				case 1:
					player.getDialogue().sendPlayerChat("I'll buy one, please.");
					player.getDialogue().setNextChatId(15);
					return true;
				case 2:
					player.getDialogue().sendPlayerChat("I'm afraid I'm not really hungry at the moment.");
					player.getDialogue().endDialogue();
					return true;
				}
			case 15:
				if(player.getInventory().playerHasItem(995, 50) && !player.getInventory().playerHasItem(new Item(BRAIN))) {
					player.getDialogue().sendNpcChat("A very wise choice, " + gender[1][player.getGender()] + ".",
							 						 "Don't eat it all at once, savour every morsel--",
							 						 "that's my advice to you.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(995, 50)); //Remove Gold
					player.getInventory().addItem(new Item(BRAIN, 1));
					
				} else if(player.getInventory().playerHasItem(new Item(BRAIN))) {
					
					player.getDialogue().sendNpcChat("You haven't finished the brain I have you yet.", SAD);
					player.getDialogue().endDialogue();
					
				} else {
					
					player.getDialogue().sendNpcChat("You haven't any money. Come back when you're not poor",
													 "or you'll be dogfood around here m9");
					player.getDialogue().endDialogue();
				}
				return true;
				
			}
			return true; //end Roavar
		case 1675: //Ghostly Gardener
			if(player.getEquipment().getId(Constants.AMULET) == GSAMMY || player.getEquipment().getId(Constants.AMULET) == GSAMMYE) {
				switch(questStage) {
				case HIRED:
				case PARTS_INANIMATE:
					if(player.getQuestVars().headlessHunt) {
						switch(player.getDialogue().getChatId()) {
						case 1:
							//Determine direction to go to the grave site
							//NOTE(saxi): Grave direction might be randomized among several possibilities
							//May need an array of locations.
							//If not in woods "You need to head off to them Haunted Woods, mate.
						}
					} else {
						switch(player.getDialogue().getChatId()) {
						case 1:
							if(!player.getQuestVars().ghostlyGardener) {
								player.getDialogue().sendPlayerChat("What happened to your head?", CONTENT);
								player.getDialogue().setNextChatId(2);
							} else {
								player.getDialogue().sendPlayerChat("Hey again.", CONTENT);
								player.getDialogue().setNextChatId(14);
							}
							return true;
						
						case 2:
							player.getDialogue().sendStatement("You feel power emanate from the Amulet of Ghostspeak",
																"and the air around you vibrates with the ghostly voice of the headless",
																"gardener");
							return true;
						case 3:
							player.getDialogue().sendNpcChat("Oi was in the old 'aunted Forest to the south,",
															 "diggin' a pit for moi old master, old Fenkenstrain, when would you",
															 "believe it, somone chops me head off. Awful bad luck weren't it?");
							return true;
						case 4:
							player.getDialogue().sendPlayerChat("Oh yes, draedful bad luck", SAD);
							return true;
						case 5:
							player.getDialogue().sendNpcChat("So oi thinks to meself, I dont needs no 'ead",
															 "to be getting on with me gardenin', long as I got me hands",
															 "and me spade.");
							player.getDialogue().setNextChatId(15);
							player.getQuestVars().ghostlyGardener = true;
							return true;
						case 14:
							player.getDialogue().sendNpcChat("Ello there m8, 'ow goes it");
							return true;
						case 15:
							if(player.getQuestStage(getQuestID()) == PARTS_INANIMATE) {
								
								player.getDialogue().sendOption("Can you tell me anything about Fenkenstrain?", "Do you know where the key to the shed is?", "Do you know where I can find a lightning conductor mould?", "What's your name?");
								player.getDialogue().setNextChatId(17);
							} else {
								
								player.getDialogue().sendOption("Can you tell me anything about Fenkenstrain?", "Would you show me where you lost your head?", "What's your name?");
							}
							
							return true;
						case 16:
							switch(optionId) {
							case 1:
								player.getDialogue().sendNpcChat("Oi could tell you a few things about old Fenky, yeah.");
								player.getDialogue().setNextChatId(18);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Well, oi s'pose oi've got ten minutes to spare.");
								//send on headlessHunt
								player.getDialogue().endDialogue();
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Me name? It's been a moivellous long while", 
																 "mate, since I had any use for such a thing as a name");
								player.getDialogue().setNextChatId(26);
								return true;
							}
							return true;
						case 17:
							switch(optionId) {
							case 1:
								player.getDialogue().sendNpcChat("Oi could tell you a few things about old Fenky, yeah.");
								player.getDialogue().setNextChatId(18);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Got it right 'ere in my pocket. Here you go.");
								player.getDialogue().setNextChatId(30);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("A conductor mould, you say? Let me see...", 
																"There used to be a bloke 'ere, sort of an 'andyman 'e was.", 
																"Did everything 'round the place -- fixed what was broke,", 
																"swept the chimneys and the like. He would 'ave had a mould, I imagine.");
								player.getDialogue().setNextChatId(31);
								return true;
								
							case 4:
								player.getDialogue().sendNpcChat("Me name? It's been a moivellous long while", 
																 "mate, since I had any use for such a thing as a name");
								player.getDialogue().setNextChatId(26);
								return true;
							}
							return true;
						case 18:
							player.getDialogue().sendPlayerChat("Go on.");
							return true;
						case 19:
							player.getDialogue().sendNpcChat("Once, this castle were full o' good folk --", 
															 "my friends. Fenky was just the castle doctor, you know,", 
															 "to the lord and the castle folk.");
							return true;
						case 20:
							player.getDialogue().sendNpcChat("I don't know what happened to them all, but one by one they", 
															 "all disappeared. When they were gone a while, I went and dug", 
															 "graves for 'em in the forest. After a while they weren't no one left", 
															 "but the lord, Fenkenstrain and meself");
							return true;
						case 21:
							player.getDialogue().sendNpcChat("Old Fenky sent me into the forest to dig 'im a pit --", 
															 "never said what for --", 
															 "then would you believe it, someone chops me 'ead off");
							return true;
						case 22:
							player.getDialogue().sendPlayerChat("Did you see who did it...before...?");
							return true;
						case 23:
							player.getDialogue().sendNpcChat("Before oi kicked the bucket, you mean?");
							return true;
						case 24:
							player.getDialogue().sendPlayerChat("Umm...");
							return true;
						case 25:
							player.getDialogue().sendNpcChat("Don't worry yeself. I'm not worried about bein dead.", 
															 "Worse things could happen, I suppose. One thing I do know is,", 
															 "There ain't no lord of the castle anymore, 'cept for old Fenky", 
															 "Makes ya think a bit, don't it?");
							player.getDialogue().setNextChatId(15);
							return true;
						case 26:
							player.getDialogue().sendPlayerChat("Don't worry. I was just trying to make conversation");
							return true;
						case 27:
							player.getDialogue().sendNpcChat("No, no, I can't be havin' that. I'll remember in a minute...");
							return true;
						case 28:
							player.getDialogue().sendPlayerChat("Please, don't worry");
							return true;
						case 29:
							player.getDialogue().sendNpcChat("Lestwit, that's it! Ed Lestwit");
							player.getDialogue().setNextChatId(15);
							return true;
						case 30:
							player.getDialogue().sendStatement("The headless gardener hands you a rusty key.");
							player.getInventory().addItem(new Item(SHED_KEY, 1));
							player.getDialogue().endDialogue();
							return true;
						case 31:
							player.getDialogue().sendPlayerChat("Where is he now?");
							return true;
						case 32:
							player.getDialogue().sendNpcChat("E's dead, just like everyone else round 'ere...'cept for me.");
							return true;
						case 33:
							player.getDialogue().sendPlayerChat("Uhh...but you are de...nevermind.");
							player.getDialogue().endDialogue();
							return true;
						}
					}
					
					return true;
				
				case QUEST_COMPLETE:
					switch(player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendPlayerChat("How are you?");
						return true;
					case 2:
						player.getDialogue().sendNpcChat("Same as ever, mate, just getting on with it regardless.");
						return true;
					case 3:
						player.getDialogue().sendPlayerChat("Good for you...err...mate.");
						return true;
					}
					return true;
				}
			} else {
				player.getDialogue().sendStatement("It feels a b-bit awkward to s-stare at the stump");
				player.getDialogue().endDialogue();
				return true;
			}
			
			return true; //end Ghostly Gardener
		case 1673: 
		case 1674: //Fenkenstrain's monster/lord rologarth
			switch(questStage) {
			case TO_THE_TOWER:
				switch(player.getDialogue().getChatId()) {
				case 1:
					player.getDialogue().sendPlayerChat("I am commanded to destroy you, creature!", ANGRY_1);
					return true;
				case 2:
					player.getDialogue().sendNpcChat("Oh that's *hic* not very *hic* nice...");
					return true;
				case 3:
					player.getDialogue().sendPlayerChat("Are you feeling okay?");
					return true;
				case 4:
					player.getDialogue().sendNpcChat("Abso *hic* lutely. Never *buuurrp* better.");
					return true;
				case 5:
					player.getDialogue().sendPlayerChat("You don't look very dangerous.");
					return true;
				case 6:
					player.getDialogue().sendNpcChat("How *hic* do I look?");
					return true;
				case 7:
					player.getDialogue().sendPlayerChat("You really dont know, do you? Have a look for yourself.");
					return true;
				case 8:
					player.getDialogue().sendStatement("The creature stumbles over towards the mirror, focuses upon his reflection and...");
					return true;
				case 9:
					player.getDialogue().sendNpcChat("AAAAARRGGGGHHHH!");
					return true;
				case 10:
					player.getDialogue().sendStatement("The creature becomes instantly sober, horror all too evident in his undead eyes");
					return true;
				case 11:
					player.getDialogue().sendPlayerChat("I'm sory. I suppose I'm partially to blame for this.");
					return true;
				case 12:
					player.getDialogue().sendNpcChat("No -- it was him I wager -- Fenkenstrain -- wasn't it?", 
													 "He's bruoght me back to life!");
					return true;
				case 13:
					player.getDialogue().sendPlayerChat("Who are -- were -- you?");
					return true;
				case 14:
					player.getDialogue().sendNpcChat("I was Rologarth, Lord of the North Coast -- this castle was once mine.", 
													 "Fenkenstrain was the castle doctor.");
					return true;
				case 15:
					player.getDialogue().sendPlayerChat("So the castle wasn't really abandonded when he found it?");
					return true;
				case 16:
					player.getDialogue().sendNpcChat("Is that what he told you? No, no, this castle was once full", 
													 "of people and life. Fenkenstrain advised me to sell them to the vampyres", 
													 "which -- I am sad to say -- I did");
					return true;
				case 17:
					player.getDialogue().sendPlayerChat("I found your brain in a jar in Canifis, so he must have sold you too.");
					return true;
				case 18:
					player.getDialogue().sendNpcChat("Of that I will not speak. There lie memories that should rest with the dead,", 
													 "the living unable to bear them.");
					return true;
				case 19:
					player.getDialogue().sendPlayerChat("That's it -- I'm leaving this dreadful place, whether I get paid or not", 
														"is there anything I can do for you before I leave?");
					return true;
				case 20:
					player.getDialogue().sendNpcChat("Only one -- please stop Fenkenstrain from carrying on his experiments", 
													 "once and for all, so that no other poor soul has to endure", 
													 "suffering such as that of my people and I.");
					player.setQuestStage(getQuestID(), TRUTH_DISCOVERED);
					player.getDialogue().endDialogue();
					return true;
				
				}
				return true;
			case TRUTH_DISCOVERED:
				switch(player.getDialogue().getChatId()) {
				case 1:
					player.getDialogue().sendPlayerChat("Do you know how I can stop Fenkenstrain's experiments?");
					return true;
				case 2:
					player.getDialogue().sendNpcChat("Take the Ring of Charos from him.");
					return true;
				case 3:
					player.getDialogue().sendPlayerChat("What is this ring?");
					return true;
				case 4:
					player.getDialogue().sendNpcChat("It was my birthright, passed down to me through the ages,", 
													 "its origin forgotten. The Ring of charos has many powers, but Fenkenstrain has bent",
													 "them to his own evil purposes. Without the power of the ring, he will not be able to", 
													 "the dead from their sleep. It has one other, extremely important use --");
					return true;
				case 5:
					player.getDialogue().sendNpcChat("It confuses the werewolves' senses, making them believe that they smell", 
													 "of their own kind. Without the ring, Fenkenstrain will be at their mercy.");
					player.getDialogue().endDialogue();
					return true;
				}
			
				return true;
			case QUEST_COMPLETE:
				switch(player.getDialogue().getChatId()) {
				case 1:
					player.getDialogue().sendNpcChat("How goes it, friend");
					return true;
				case 2:
					player.getDialogue().sendPlayerChat("I stole the Ring of Charos from Fenkenstrain.");
					return true;
				case 3:
					player.getDialogue().sendNpcChat("I saw him climb up into the tower to hide. It doesn't matter -- soon the werewovles", 
													 "will come for him, and his experiments will be forever ceased.");
					return true;
				case 4:
					player.getDialogue().sendPlayerChat("Do you want the ringt back? It is yours after all.");
					return true;
				case 5:
					player.getDialogue().sendNpcChat("No, you keep it, my friend. Werewolves hunger for the scent of live flesh --", 
													 "I haev no need for the ring. I have my castle back, if not my soul.");
					player.getDialogue().endDialogue();
					return true;
				}
				return true;
			}
			return true;
		}
					
		
		return false;
	}

	@Override
	public boolean doNpcClicking(Player player, Npc npc) {
		// TODO Auto-generated method stub
		return false;
	}
        
        @Override
        public boolean doNpcSecondClicking(Player player, Npc npc) {
            return false;
        }

	@Override
	public boolean doItemOnNpc(Player player, int itemId, Npc npc) {
		switch(npc.getOriginalNpcId()) {
		case 1670: //Fenkenstrain
			if(itemId == HEADe && player.getQuestStage(getQuestID()) >= HIRED) {
				player.getDialogue().setLastNpcTalk(1670);
				player.getDialogue().sendNpcChat("I can't use this head, " + player.getUsername() + ".", 
												 "It's missing the most important part --", 
												 " the old grey matter.");
				player.getDialogue().endDialogue();
				return true;
			}
			return false;
		}
		
		return false;
	}
	
	
}
