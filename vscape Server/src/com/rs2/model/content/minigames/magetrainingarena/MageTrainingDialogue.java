package com.rs2.model.content.minigames.magetrainingarena;

import com.rs2.model.content.dialogue.DialogueManager;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class MageTrainingDialogue {
    
    public static final int PIZAZZ_HAT = 3096;
    public static final int ENTRANCE_GUARDIAN = 3097;
    public static final int TELEKINETIC_GUARDIAN = 3098;
    public static final int ALCHEMY_GUARDIAN = 3099;
    public static final int ENCHANTMENT_GUARDIAN = 3100;
    public static final int GRAVEYARD_GUARDIAN = 3101;
    public static final int MAZE_GUARDIAN = 3102;
    public static final int REWARDS_GUARDIAN = 3103;
    

    public static boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
	DialogueManager d = player.getDialogue();
	switch (id) {
	    case PIZAZZ_HAT:
		switch (player.getDialogue().getChatId()) {
		    case 1:
			d.sendPlayerChat("Hello.", CONTENT);
			return true;
		    case 2:
			d.sendNpcChat("What do you want to know? Make it snappy.", ANGRY_1);
			return true;
		    case 3:
			d.sendOption("Telekinetic Pizazz Points.", "Alchemist Pizazz Points.", "Enchanting Pizazz Points.", "Graveyard Pizazz Points.", "Nevermind.");
			return true;
		    case 4:
			switch(optionId) {
			    case 1:
				d.sendPlayerChat("How many Telekinetic Pizazz Points do I have?", CONTENT);
				return true;
			    case 2:
				d.sendPlayerChat("How many Alchemist Pizazz Points do I have?", CONTENT);
				d.setNextChatId(6);
				return true;
			    case 3:
				d.sendPlayerChat("How many Enchanting Pizazz Points do I have?", CONTENT);
				d.setNextChatId(7);
				return true;
			    case 4:
				d.sendPlayerChat("How many Graveyard Pizazz Points do I have?", CONTENT);
				d.setNextChatId(8);
				return true;
			    case 5:
				d.sendPlayerChat("Nevermind.", CONTENT);
				d.endDialogue();
				return true;
			}
		    case 5:
			d.sendNpcChat("You have " + player.getTelekineticPizazz() + " Telekinetic Pizazz Point(s), now", "leave me alone.", ANGRY_1);
			d.endDialogue();
			return true;
		    case 6:
			d.sendNpcChat("You have " + player.getAlchemistPizazz() + " Alchemist Pizazz Point(s), now", "leave me alone.", ANGRY_1);
			d.endDialogue();
			return true;
		    case 7:
			d.sendNpcChat("You have " + player.getEnchantingPizazz() + " Enchanting Pizazz Point(s), now", "leave me alone.", ANGRY_1);
			d.endDialogue();
			return true;
		    case 8:
			d.sendNpcChat("You have " + player.getGraveyardPizazz() + " Graveyard Pizazz Point(s), now", "leave me alone.", ANGRY_1);
			d.endDialogue();
			return true;
		}
	    return false;
	    case REWARDS_GUARDIAN:
		switch (player.getDialogue().getChatId()) {
		    case 1:
			d.sendPlayerChat("Hello.", CONTENT);
			return true;
		    case 2:
			d.sendNpcChat("Well hello there! Are you here to exchange some", "Pizazz points?", CONTENT);
			return true;
		    case 3:
			d.sendOption("Yes!", "No, thank you.");
			return true;
		    case 4:
			switch (optionId) {
			    case 1:
				d.sendPlayerChat("Yes!", CONTENT);
				return true;
			    case 2:
				d.sendPlayerChat("No, thank you.", CONTENT);
				d.endDialogue();
				return true;
			}
		    case 5:
			d.sendNpcChat("Alright, I'll show you what I have! Keep in mind you", "must progress through the wands in order to purchase the", "next rank. You must own the previous rank of wand", "before you can buy the next one.", CONTENT);
			return true;
		    case 6:
			d.sendPlayerChat("Thanks for the heads up.", CONTENT);
			return true;
		    case 7:
			MageRewardHandling.openInterface(player);
			d.dontCloseInterface();
			return true;
		}
		return false;
	    case GRAVEYARD_GUARDIAN:
		switch (player.getDialogue().getChatId()) {
		    case 1:
			d.sendPlayerChat("Hello.", CONTENT);
			return true;
		    case 2:
			d.sendNpcChat("Hello mortal, welcome to the Creature Graveyard.", CONTENT);
			return true;
		    case 3:
			d.sendOption("How does this room work?", "Thank you.");
			return true;
		    case 4:
			switch (optionId) {
			    case 1:
				d.sendPlayerChat("How does this room work?", CONTENT);
				return true;
			    case 2:
				d.sendPlayerChat("Thank you.", CONTENT);
				d.endDialogue();
				return true;
			}
		    case 5:
			d.sendNpcChat("You'll notice the remains of creaures spread around", "the room in neatly organized piles. Your job is to", "gather the bones from these piles and convert them into", "fruit; either Bananas or Peaches if you've bought the", CONTENT);
			return true;
		    case 6:
			d.sendNpcChat("Bones to Peaches spell from the Reward Guardian. The", "piles of bones are quite deep, every four bones you pick", "will change the type of bone you recieve. The amount of", "fruit the bones convert into is shown to you. Every", CONTENT);
			return true;
		    case 7:
			d.sendNpcChat("16 fruit you deposit into one of the chutes will", "garner you 1 Pizazz point and a random Rune reward.", CONTENT);
			return true;
		    case 8:
			d.sendPlayerChat("That's it? That seems pretty easy.", CONTENT);
			return true;
		    case 9:
			d.sendNpcChat("Not so fast, every so often your Life Points will be", "drained away by the undeath in this place. You'll need to", "eat some of the fruit to stay alive. Dying will not", "cause you to lose items, but it will result in a penalty of", CONTENT);
			return true;
		    case 10:
			d.sendNpcChat("10 Pizazz points for carelessness. Try not to die", "adventurer. Good luck.", CONTENT);
			d.endDialogue();
			return true;
		}
		return false;
	    case ENCHANTMENT_GUARDIAN:
		switch (player.getDialogue().getChatId()) {
		    case 1:
			d.sendPlayerChat("Hello.", CONTENT);
			return true;
		    case 2:
			d.sendNpcChat("Hello, welcome to the Enchanting Chamber.", CONTENT);
			return true;
		    case 3:
			d.sendOption("How does this room work?", "Thank you.");
			return true;
		    case 4:
			switch (optionId) {
			    case 1:
				d.sendPlayerChat("How does this room work?", CONTENT);
				return true;
			    case 2:
				d.sendPlayerChat("Thank you.", CONTENT);
				d.endDialogue();
				return true;
			}
		    case 5:
			d.sendNpcChat("In this room you use the 6 levels of Enchantment", "spells to enchant various shapes found in piles laying", "around. Enchanting these shapes turns them into magical", "orbs. Every 20 orbs deposited will earn you a reward", CONTENT);
			return true;
		    case 6:
			d.sendNpcChat("of 3 high level runes. There is always a designated", "shape which garners you Pizazz points depending on the", "level of enchantment used on said shape. On top of this,", "Dragonstones will fall to the floor every 5 minutes or so.", CONTENT);
			return true;
		    case 7:
			d.sendNpcChat("Casting an enchant spell on these Dragonstones will yield", "you twice the bonus that a designated shape normally", "rewards you. As well as the bonus points per correct shape,", "every 10 shapes enchanted rewards you with a point.", CONTENT);
			return true;
		    case 8:
			d.sendNpcChat("If the tenth shape is the designated bonus shape,", "you will recieve the appropriate bonus points for the spell", "used to enchant the shape. Remember to deposit all your", "orbs before leaving each time, and good luck!", CONTENT);
			d.endDialogue();
			return true;
		}
		return false;
	    case ALCHEMY_GUARDIAN:
		switch (player.getDialogue().getChatId()) {
		    case 1:
			d.sendPlayerChat("Hello.", CONTENT);
			return true;
		    case 2:
			d.sendNpcChat("Hello, welcome to the Alchemist's Playground.", CONTENT);
			return true;
		    case 3:
			d.sendOption("How does this room work?", "Thank you.");
			return true;
		    case 4:
			switch (optionId) {
			    case 1:
				d.sendPlayerChat("How does this room work?", CONTENT);
				return true;
			    case 2:
				d.sendPlayerChat("Thank you.", CONTENT);
				d.endDialogue();
				return true;
			}
		    case 5:
			d.sendNpcChat("In this room you use the Alchemy spells, low", "or high to convert items into gold. You then deposit", "the gold coins into the repository in the back", "of the room for a reward.", CONTENT);
			return true;
		    case 6:
			d.sendNpcChat("You'll notice that the items in this room change", "the amount of coins they alchemize into periodically.", "Take advantage of this to maximize the coins you recieve.", "However, be wary that the cupboards switch which items", CONTENT);
			return true;
		    case 7:
			d.sendNpcChat("they contain at the same moment the prices change.", "Use whatever strategy you will to ensure the highest", "amount of gold return per cast. The last thing I need to", "make you aware of is the 'rune free cast' arrow...", CONTENT);
			return true;
		    case 8:
			d.sendNpcChat("...Every once in a while a green arrow will appear", "indicating which item can be alchemized at no rune cost to", "you! Utilize this if you are running low on runes, or", "don't have very many to begin with. It is recommended", CONTENT);
			return true;
		    case 9:
			d.sendNpcChat("that you turn in coins in multiples of 100, as the machine", "only rewards in amounts of 100. You will be rewarded", "roughly 5 Magic exp per coin you deposit, and for every", "100 coins, you earn 1 Pizazz points and 10 of the coins", CONTENT);
			return true;
		    case 10:
			d.sendNpcChat("are deposited directly to your Bank account!", "Good luck!", CONTENT);
			d.endDialogue();
			return true;
		}
		return false;
	    case TELEKINETIC_GUARDIAN:
		switch (player.getDialogue().getChatId()) {
		    case 1:
			d.sendPlayerChat("Hello.", CONTENT);
			return true;
		    case 2:
			d.sendNpcChat("Hello, welcome to the Telekinetic Theatre.", CONTENT);
			return true;
		    case 3:
			d.sendOption("How does this room work?", "Thank you.");
			return true;
		    case 4:
			switch (optionId) {
			    case 1:
				d.sendPlayerChat("How does this room work?", CONTENT);
				return true;
			    case 2:
				d.sendPlayerChat("Thank you.", CONTENT);
				d.endDialogue();
				return true;
			}
		    case 5:
			d.sendNpcChat("The statue in the maze is a Maze Guardian,", "trapped under a spell which turns him to stone.", "Your job is to move him to the square in the maze", "which will revert the enchantment on him.", CONTENT);
			return true;
		    case 6:
			d.sendNpcChat("To do this, stand near one of the four sides", "of the maze and cast Telekinetic Grab on the statue.", "This will move the statue in a straight line towards", "the side you on standing on, until", CONTENT);
			return true;
		    case 7:
			d.sendNpcChat("A wall or obstacle in the maze stops it. You must", "repeatedly use this technique to navigate the statue", "through the maze and to the end square to free", "the Maze Guardian.", CONTENT);
			return true;
		    case 8:
			d.sendNpcChat("For each maze you complete, you will be awarded", "2 Pizazz points, and every fifth maze you complete in", " a row garners you 8 Pizazz, 10 Law runes and an extra", "2250 Magic experience!", CONTENT);
			return true;
		    case 9:
			d.sendNpcChat("Talk to the Maze Guardian after you complete the", "maze to be teleported to a different maze. Or, simply exit at", "any time using the exit. Good luck!", CONTENT);
			d.endDialogue();
			return true;
		}
		return false;
	    case ENTRANCE_GUARDIAN:
		switch (player.getDialogue().getChatId()) {
		    case 1:
			d.sendNpcChat("Hello and welcome to the Mage Training Arena!", HAPPY);
			return true;
		    case 2:
			d.sendOption("What is this place?", "What can I do here?");
			return true;
		    case 3:
			switch (optionId) {
			    case 1:
				d.sendPlayerChat("What is this place?", CONTENT);
				return true;
			    case 2:
				d.sendPlayerChat("What can I do here?", CONTENT);
				d.setNextChatId(7);
				return true;
			}
		    case 4:
			d.sendNpcChat("This is the place where Mages hone their skills.", "After the discovery of Runes by humans, many accidents", "occured as inexperienced mages attempted difficult spells.", CONTENT);
			return true;
		    case 5:
			d.sendNpcChat("The wizards of the Wizards' Tower created this arena,", "and myself, so that Mages can train with", "the more mundane and safe spells.", CONTENT);
			return true;
		    case 6:
			d.sendPlayerChat("So, what can I do here?", CONTENT);
			return true;
		    case 7:
			if (!player.getInventory().ownsItem(6885)) {
			    d.sendNpcChat("Ah, always eager you adventurers are. There are", "four rooms here in which you can pratice your Magic.", "First things first however, you'll need one of these.", CONTENT);
			} else {
			    d.sendNpcChat("Ah, always eager you adventurers are. There are", "four rooms here in which you can pratice your Magic. Inside", "each room is a Guardian who can tell you more than I can.", CONTENT);
			    d.setNextChatId(12);
			}
			return true;
		    case 8:
			d.sendGiveItemNpc("The Entrance Guardian hands you a strange hat.", new Item(6885));
			player.getInventory().addItemOrDrop(new Item(6885));
			return true;
		    case 9:
			d.sendPlayerChat("Erm, what is this?", CONTENT);
			return true;
		    case 10:
			d.sendNpcChat("It's an enchanted hat which will tell you how many", "Pizazz points you've earned yourself in the Arena!", "Pizazz points are what you earn by participating in", "each of the four different rooms.", CONTENT);
			return true;
		    case 11:
			d.sendNpcChat("In each of the four rooms resides a Guardian specific", "to that room. They will be able to tell you how", "to succeed in each room and earn Pizazz points.", CONTENT);
			return true;
		    case 12:
			d.sendPlayerChat("Sounds pretty straightforward. Is there anything", "else I should know?", CONTENT);
			return true;
		    case 13:
			d.sendNpcChat("Ah, yes! Upstairs is the Arena's Reward Guardian. There", "you can turn in your Pizazz points for marvelous prizes.", "There are runes, robes and wands all related", "to the Magic skill.", CONTENT);
			return true;
		    case 14:
			d.sendNpcChat("Don't forget you can come back to me if you lose", "your Progress hat for any reason!", CONTENT);
			return true;
		    case 15:
			d.sendPlayerChat("Thank you!", HAPPY);
			d.endDialogue();
			return true;

		}
		return false;
	    case MAZE_GUARDIAN:
		switch (player.getDialogue().getChatId()) {
		    case 1:
			d.sendPlayerChat("Hi!", Dialogues.HAPPY);
			return true;
		    case 2:
			d.sendNpcChat("Well done on releasing me. Would you like to try", "another maze?", Dialogues.CONTENT);
			return true;
		    case 3:
			d.sendOption("Yes please!", "No thanks.");
			return true;
		    case 4:
			switch (optionId) {
			    case 1:
				d.sendPlayerChat("Yes please!", Dialogues.CONTENT);
				return true;
			    case 2:
				d.sendPlayerChat("No thanks.", Dialogues.CONTENT);
				d.endDialogue();
				return true;
			}
		    case 5:
			d.sendNpcChat("Very well, I shall teleport you.", Dialogues.CONTENT);
			return true;
		    case 6:
			d.endDialogue();
			player.getTelekineticTheatre().newMaze();
			return true;
		}
		return false;
	}
	return false;
    }
}
