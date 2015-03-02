package com.rs2.model.content.quests;

import com.rs2.model.World;
import static com.rs2.model.content.dialogue.Dialogues.CALM;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.combat.CombatManager;
import static com.rs2.model.content.dialogue.Dialogues.ANNOYED;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;

public class RomeoAndJuliet implements Quest {

	public static final int START = 1,
		JULIET = 2,
		ROMEO = 3,
		LAWRENCE = 4,
		POTION = 5,
		PLAN = 6,
		TELL_ROMEO = 7,
		COMPLETE = 8;

	public static int dialogueStage = 0, questStage = 0;

	private static final int questPointReward = 5;

	public int getQuestID() {
		return 16;
	}

	public String getQuestName() {
		return "Romeo and Juliet";
	}

	public String getQuestSaveName() {
		return "romeo-and-juliet";
	}

	public boolean canDoQuest(Player player) {
		return true;
	}

	public void getReward(Player player) {
		player.addQuestPoints(questPointReward);
		player.getActionSender().QPEdit(player.getQuestPoints());
	}

	public void completeQuest(Player player) {
		getReward(player);
		player.getActionSender().sendInterface(12140);
		player.getActionSender().sendString("You have completed: " + getQuestName(), 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("5 Quest Points,", 12150);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), 7343);
	}

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if (questStage == START) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start this quest, speak to Romeo in Varrock Square.", 8147);
			player.getActionSender().sendString("Romeo has asked you to speak to Juliet.", 8148);
			player.getActionSender().sendString("She is in the house west of Varrock.", 8149);
		} else if (questStage == JULIET) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start this quest, speak to Romeo in Varrock Square.", 8147);
			player.getActionSender().sendString("@str@" + "Romeo has asked you to speak to Juliet.", 8148);
			player.getActionSender().sendString("Juliet has given you a letter for Romeo.", 8149);
		} else if (questStage == ROMEO) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start this quest, speak to Romeo in Varrock Square.", 8147);
			player.getActionSender().sendString("@str@" + "Romeo has asked you to speak to Juliet.", 8148);
			player.getActionSender().sendString("@str@" + "Juliet has given you a letter for Romeo.", 8149);
			player.getActionSender().sendString("Romeo asks you talk to Father Lawrence.", 8150);
			player.getActionSender().sendString("He is in the church in north-east Varrock.", 8151);
		} else if (questStage == LAWRENCE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start this quest, speak to Romeo in Varrock Square.", 8147);
			player.getActionSender().sendString("@str@" + "Romeo has asked you to speak to Juliet.", 8148);
			player.getActionSender().sendString("@str@" + "Juliet has given you a letter for Romeo.", 8149);
			player.getActionSender().sendString("@str@" + "Romeo asks you talk to Father Lawrence.", 8150);
			player.getActionSender().sendString("Lawrence told you to speak to the Varrock apothecary.", 8151);
		} else if (questStage == POTION) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start this quest, speak to Romeo in Varrock Square.", 8147);
			player.getActionSender().sendString("@str@" + "Romeo has asked you to speak to Juliet", 8148);
			player.getActionSender().sendString("@str@" + "Juliet has given you a letter for Romeo.", 8149);
			player.getActionSender().sendString("@str@" + "Romeo asks you talk to Father Lawrence.", 8150);
			player.getActionSender().sendString("@str@" + "Lawrence told you to speak to the Varrock apothecary.", 8151);
			player.getActionSender().sendString("The apothecary needs cadava berries.", 8152);
			player.getActionSender().sendString("Return to him when you have them.", 8153);
		} else if (questStage == PLAN || questStage == TELL_ROMEO) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start this quest, speak to Romeo in Varrock Square.", 8147);
			player.getActionSender().sendString("@str@" + "Romeo has asked you to speak to Juliet", 8148);
			player.getActionSender().sendString("@str@" + "Juliet has given you a letter for Romeo.", 8149);
			player.getActionSender().sendString("@str@" + "Romeo asks you talk to Father Lawrence.", 8150);
			player.getActionSender().sendString("@str@" + "Lawrence told you to speak to the Varrock apothecary.", 8151);
			player.getActionSender().sendString("@str@" + "The apothecary needs cadava berries.", 8152);
			player.getActionSender().sendString("Give Juliet the potion, then tell Romeo.", 8153);
		} else if (questStage == COMPLETE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start this quest, speak to Romeo in Varrock Square.", 8147);
			player.getActionSender().sendString("@str@" + "Romeo has asked you to speak to Juliet", 8148);
			player.getActionSender().sendString("@str@" + "Juliet has given you a letter for Romeo.", 8149);
			player.getActionSender().sendString("@str@" + "Romeo asks you talk to Father Lawrence in the church in northwest Varrock.", 8150);
			player.getActionSender().sendString("@str@" + "Father Lawrence tells you to speak to the apothecary about a cadava potion.", 8151);
			player.getActionSender().sendString("@str@" + "The apothecary needs cadava berries.", 8152);
			player.getActionSender().sendString("@str@" + "Give Juliet the potion, then tell Romeo.", 8152);
			player.getActionSender().sendString("@red@" + "You have completed this quest!", 8154);
		} else {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("To start this quest, speak to Romeo in Varrock Square.", 8147);
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), START);
		player.getActionSender().sendString("@yel@" + getQuestName(), 7343);
	}

	public boolean questCompleted(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if (questStage >= COMPLETE) {
			return true;
		}
		return false;
	}

	public void sendQuestTabStatus(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if ((questStage >= START) && (questStage < COMPLETE)) {
			player.getActionSender().sendString("@yel@" + getQuestName(), 7343);
		} else if (questStage == COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 7343);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 7343);
		}

	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString("I can start this quest be speaking to Romeo in Varrock", 8147);
		player.getActionSender().sendString("central square by the fountain", 8148);
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public boolean itemHandling(final Player player, int itemId) {
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		return false;
	}

	public boolean doItemOnObject(final Player player, int object, int item) {
		return false;
	}

	public boolean doObjectClicking(final Player player, int object, int x, int y) {
		return false;
	}

	public boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
		switch (object) {

		}
		return false;
	}

	public void handleDeath(final Player player, final Npc died) {

	}

	public boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
		switch (id) {
			case 639: //Romeo
				switch (player.getQuestStage(16)) {
					case 0: //not started
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Oh woe is me that I cannot find my Juliet!", "You haven't seen Juliet have you?", SAD);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Perhaps I could help to find her for you?", "What does she look like?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Oh would you? That would be great! She has this sort", "of hair...", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("Hair...check...", CALM);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("...and these...great lips...", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("Lips...right.", CALM);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("Oh and she has thses lovely shoulders as well...", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendPlayerChat("Shoulders...right, so she has hair, lips and shoulders...that", "should narrow it down.", CALM);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("Oh yes, Juliet is very different...please tell her that she", "is the love of my long and that I life to be with her.", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendPlayerChat("Surely you mean that 'she is the love of your life and", "that you long to be with her?'", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendNpcChat("Oh yeah, what you said...tell her that! It sounds much", "better!", "Oh you're so good at this!", HAPPY);
								return true;
							case 12:
								player.getDialogue().sendPlayerChat("Yes, ok, I'll let her know.", CONTENT);
								return true;
							case 13:
								player.getDialogue().sendNpcChat("Oh great! And tell her that I want to kiss her a give.", HAPPY);
								return true;
							case 14:
								player.getDialogue().sendPlayerChat("You mean 'give her a kiss'.", CONTENT);
								return true;
							case 15:
								player.getDialogue().sendNpcChat("Oh you're good...you are so very good!", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(16, 1);
								QuestHandler.getQuests()[16].startQuest(player);
								return true;
						}
					case 2: //stage: Juliet
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("I've been in touch with Juliet!", "She's written a message for you.", CONTENT);
								if (player.getInventory().playerHasItem(new Item(755))) {
									player.getDialogue().sendGiveItemNpc("You give Romeo the letter.", new Item(755));
									player.getInventory().removeItem(new Item(755));
									return true;
								} else {
									player.getDialogue().sendPlayerChat("Oops. I don't have the letter.", SAD);
									player.getDialogue().endDialogue();
									return false;
								}
							case 2:
								player.getDialogue().sendNpcChat("Oh, a message! I've never had a message before...", HAPPY);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("Really?", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("No, not one.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Oh, well except for the occasional court summons.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("But they're not really nice messages. Not like this one!", "I'm sure that this message will be lovely.", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("Well are you going to open it or not?", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("Oh yes, of course!", "'Dearest Romeo, I am very pleased that you sent ", player.getUsername() + " to tell me that you still hold", "affliction...', Affliction! She thinks I'm diseased?", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendPlayerChat("Are you sure she doesn't mean 'affection'?", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("Ah, yes... 'still hold affection for me. I still feel great", "affection for you, but unfortunately my Father opposes", "our marriage.'", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendPlayerChat("Oh dear...that doesn't sound too good.", CONTENT);
								return true;
							case 12:
								player.getDialogue().sendNpcChat("What? '...great affection for you. Father oppose our...", CONTENT);
								return true;
							case 13:
								player.getDialogue().sendNpcChat("'...marriage and will...", CONTENT);
								return true;
							case 14:
								player.getDialogue().sendNpcChat("...will kill you if he sees you again!", DISTRESSED);
								return true;
							case 15:
								player.getDialogue().sendPlayerChat("I have to be honest, it's not getting any better...", CONTENT);
								return true;
							case 16:
								player.getDialogue().sendNpcChat("'our only hope is that Father Lawrence, our long time", "confidant, can help us in some way.'", CONTENT);
								return true;
							case 17:
								player.getDialogue().sendNpcChat("Well, that's it then...we haven't got a chance...", CONTENT);
								return true;
							case 18:
								player.getDialogue().sendPlayerChat("What about Father Lawrence?", CONTENT);
								return true;
							case 19:
								player.getDialogue().sendNpcChat("...Our love is over...the great romance!", "The life of my love...", SAD);
								return true;
							case 20:
								player.getDialogue().sendPlayerChat("...or you could speak to Father Lawrence!", CONTENT);
								return true;
							case 21:
								player.getDialogue().sendNpcChat("Oh, my aching heart...how useless the situation", "is now...we have no one to turn to...", SAD);
								return true;
							case 22:
								player.getDialogue().sendPlayerChat("FATHER LAWRENCE!", ANGRY_1);
								return true;
							case 23:
								player.getDialogue().sendNpcChat("Father Lawrence?", CONTENT);
								return true;
							case 24:
								player.getDialogue().sendNpcChat("Oh, right. Father Lawrence...he's our long time confidant,", "he might have a solution! Yes, you have to go and", "talk to Lather Fawrence for us and ask him if he's got", "any suggestions for our predicament!", CONTENT);
								return true;
							case 25:
								player.getDialogue().sendPlayerChat("Where can I find Father Lawrence?", CONTENT);
								return true;
							case 26:
								player.getDialogue().sendNpcChat("Lather Fawrence! Oh he's...", CONTENT);
								return true;
							case 27:
								player.getDialogue().sendNpcChat("You know he's not my 'real' Father don't you?", CONTENT);
								return true;
							case 28:
								player.getDialogue().sendPlayerChat("I think I suspected that he wasn't.", CONTENT);
								return true;
							case 29:
								player.getDialogue().sendNpcChat("Well anyway...he tells these long, boring sermons...and", "keeps these citizens snoring in his", "church to the north-east.", CONTENT);
								return true;
							case 30:
								player.getDialogue().sendPlayerChat("Ok, thanks.", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(16, 3);
								return true;
						}
					case 7:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Romeo, it's all set. Juliet has drunk the potion and has", "been taken down into the crypt...now you just need to", "go collect her.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Ah right, the potion! Great...", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("What potion would that be then?", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("The Cadava potion...you know, the one which will", "make her appear dead! She's in the crypt, go claim", "your true love.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("But I'm scared...will you come with me?", DISTRESSED);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("No, Romeo. I think this is something you can", "do on your own.", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("Alright, thank you so much for all of your help.", CONTENT);
								return true;
							case 8:
								player.setQuestStage(16, 8);
								QuestHandler.completeQuest(player, 16);
								player.getDialogue().dontCloseInterface();
								return true;
						}
					case 8:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("I can't thank you enough, oh the love!", HAPPY);
								player.getDialogue().endDialogue();
								return true;
						}
				}
				return false;
			case 637: //Juliet
				switch (player.getQuestStage(16)) {
					case 1: //stage: start
					case 2:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Juliet, I come from Romeo.", "He begs me to tell you that he cares for you.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Oh, how my heart soars to hear this news! Please take", "this message to him with great haste!", HAPPY);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("Well I hope it's good news...he was quite upset when I", "left him.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("He's quite often upset... But I don't", "think he's going to take this news very well,", "however, all is not lost.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Everything is explained in the letter, would you be so", "kind and deliver it to him please?", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("Certainely, I'll do so straight away.", CONTENT);
								return true;
							case 7:
								if (player.getInventory().getItemContainer().freeSlots() > 1) {
									player.getDialogue().sendNpcChat("Many thanks! Oh, I'm so very grateful. You may be", "our only hope.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Oh, you don't have room for the letter...", SAD);
									player.getDialogue().endDialogue();
									return true;
								}
							case 8:
								if (!player.getInventory().playerHasItem(755)) {
									player.getDialogue().sendGiveItemNpc("Juliet gives you the letter.", new Item(755));
									player.getInventory().addItem(new Item(755));
									player.setQuestStage(16, 2);
									player.getDialogue().endDialogue();
									return true;
								} else {
									player.getDialogue().sendStatement("You already have a letter.");
									player.getDialogue().endDialogue();
									return true;
								}
						}
					case 6: //stage: plan
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getInventory().playerHasItem(new Item(756))) {
									player.getDialogue().sendPlayerChat("Hi Juliet! I have an interesting proposition for", "you...suggested by Father Lawrence. It may be the", "only way you'll be able to escape from this house and", "be with Romeo", CONTENT);
									return true;
								} else {
									player.getDialogue().sendPlayerChat("I don't have the potion.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								}
							case 2:
								player.getDialogue().sendNpcChat("Go on...", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("I have a cadava berry potion here, suggested by Father", "Lawrence. If you drink it, it will make you appear dead!", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("And...?", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("And when you appear dead...your still and lifeless", "'corpse' will be moved to the crypt!", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("Ooooooh, a cold dark creepy crypt...", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("...sounds just peachy!", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendPlayerChat("Then...Romeo can steal into the crypt and rescue you", "just as you wake up!", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("...and this is the great idea for getting me out of here?", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendPlayerChat("To be fair, I can't take all the credit, it was all", "Father Lawrence's suggestion.", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendNpcChat("Ok...if this is the best we can do...hand over the potion!", CONTENT);
								player.getDialogue().sendGiveItemNpc("You give Juliet the potion", new Item(756));
								player.getInventory().removeItem(new Item(756));
								return true;
							case 12:
								player.getDialogue().sendNpcChat("Wonderful! I just hope Romeo can remember to get", "me from the crypt.", CONTENT);
								return true;
							case 13:
								player.getDialogue().sendNpcChat("Please go to Romeo and make sure he understands.", "Although I adore his lovelorn ways, he ", "can be a bit dense sometimes and I don't want to wake", "up in that crypt on my own.", CONTENT);
								return true;
							case 14:
								player.getDialogue().sendNpcChat("Well, here goes nothing.", CONTENT);
								return true;
							case 15:
								player.getDialogue().sendStatement("Juliet drinks the potion.");
								player.getDialogue().endDialogue();
								for (Npc npc : World.getNpcs()) {
									if (npc == null) {
										continue;
									} else if (npc.getNpcId() == player.getDialogue().getLastNpcTalk()) {
										npc.setDead(true);
										CombatManager.startDeath(npc);
									}
								}
								player.setQuestStage(16, 7);
								return true;
						}
				}
				return false;
			case 640: //Father Lawrence
				switch (player.getQuestStage(16)) {
					case 3: //Stage: Romeo
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("\"...and let Saradomin light the way for you\"", "Ah!", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Can't you see that I'm in the middle of a Sermon?!", ANGRY_1);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("But, Romeo sent me!", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("But I'm busy delivering a sermon to my congregation!", ANGRY_1);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("Yes, well, it certainly seems like you have a captive", "audience!", LAUGHING);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("Oh, ok...what do you want so I can get rid of you and", "continue with my sermon?", ANGRY_1);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("Romeo sent me. He says you may be able to help.", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("Ah Romeo, yes. A fine lad, but a little bit confused.", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendPlayerChat("Yes, very confused...Anyways, Romeo wishes to be", "married to Juliet! She must be rescued from her", "father's control.", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("I agree, and I may have an idea... A potion to make", "her appear dead!", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendPlayerChat("Dead! Sounds a bit strange, but, please continue.", CONTENT);
								return true;
							case 12:
								player.getDialogue().sendNpcChat("The potion will only make Juliet appear dead.", "Then she'll be taken to the crypt...", CONTENT);
								return true;
							case 13:
								player.getDialogue().sendPlayerChat("You must have some strange hobbies.", CONTENT);
								return true;
							case 14:
								player.getDialogue().sendNpcChat("...", ANNOYED);
								return true;
							case 15:
								player.getDialogue().sendNpcChat("...Romeo can then collect her from the crypt. Go to the", "Apothecary, tell him I sent you and that you'll need a", "cadava berry potion.", CONTENT);
								return true;
							case 16:
								player.getDialogue().sendPlayerChat("Apart from the strong overtones of death, this is", "turning out to be a real love story.", CONTENT);
								player.setQuestStage(16, 4);
								player.getDialogue().endDialogue();
								return true;
						}
				}
				return false;
			case 638: //Apothecary
				switch (player.getQuestStage(16)) {
					case 4:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Apothecary. Father Lawrence sent me.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("I need a cadava berry potion to help Romeo and Juliet.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Hmm.. cadava potion. It's pretty nasty and hard to make.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Wing of rat, tail of frog.", "Ear of snake and horn of dog.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("I have all that, but I need some cadava berries.", CONTENT);
								return true;
							case 6:
								if (player.getInventory().playerHasItem(new Item(753))) {
									player.getDialogue().sendPlayerChat("Here are the berries.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendNpcChat("You will have to find them while I get the rest ready.", CONTENT);
									player.setQuestStage(16, 5);
									player.getDialogue().endDialogue();
									return true;
								}
							case 7:
								player.getDialogue().sendGiveItemNpc("You give the Apothercary the berries.", new Item(753));
								player.getInventory().removeItem(new Item(753));
								return true;
							case 8:
								player.getDialogue().sendNpcChat("Well done.", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendGiveItemNpc("The Apothecary gives you the potion.", new Item(756));
								player.getInventory().addItem(new Item(756));
								player.getDialogue().endDialogue();
								player.setQuestStage(16, 6);
								return true;
						}
					case 5:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getInventory().playerHasItem(new Item(753))) {
									player.getDialogue().sendNpcChat("Well done. You have the berries.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Looks like you haven't found those berries.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								}
							case 2:
								player.getDialogue().sendGiveItemNpc("You give the Apothecary the berries.", new Item(753));
								player.getInventory().removeItem(new Item(753));
								return true;
							case 3:
								player.getDialogue().sendGiveItemNpc("The Apothecary gives you the potion.", new Item(756));
								player.getInventory().addItem(new Item(756));
								player.setQuestStage(16, 6);
								player.getDialogue().endDialogue();
								return true;
						}
					case 6:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getInventory().playerHasItem(new Item(753))) {
									player.getDialogue().sendNpcChat("Well done. You have the berries.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Looks like you haven't found those berries.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								}
							case 2:
								player.getDialogue().sendGiveItemNpc("You give the Apothecary the berries.", new Item(753));
								player.getInventory().removeItem(new Item(753));
								return true;
							case 3:
								player.getDialogue().sendGiveItemNpc("The Apothecary gives you the potion.", new Item(756));
								player.getInventory().addItem(new Item(756));
								player.getDialogue().endDialogue();
								return true;
						}

				}
				return false;
		}
		return false;
	}

	@Override
	public boolean doNpcClicking(Player player, Npc npc) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doItemOnNpc(Player player, int itemId, Npc npc) {
		// TODO Auto-generated method stub
		return false;
	}
}
