package com.rs2.model.content.quests.impl.GhostsAhoy;

import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.quests.impl.GhostsAhoy.GhostsAhoy.PETITION;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.util.Misc;
import java.util.ArrayList;

public class GhostsAhoyPetition {
    private Player player;
    private ArrayList<Npc> signers;
    private Npc lastNpcTalked;
    private int chatId;
    
    private static final String[] RANDOM_RESPONSES = {
	"No! I won't be caught up in your political nonsense!",
	"Yes! I would love to be able to leave this plane.",
	"No, leave me alone.",
	"No, I won't. Please just go away.",
	"Sure, I might as well sign!",
	"Sounds like a worthy cause, yes, I'll sign.",
	"Yes, I will.",
	"No, thank you.",
	"Life can be so odd sometimes...",
	"Why do you smell like Ectoplasm?",
	"No, I've never seen you around town before.",
	"No. The answer is no.",
	"My signature? You want my signature?! NO!",
	"If it'll help me leave this world, yes.",
	"Ah, I do long to rest. Yes, let me sign.",
	"Who are you? Where are your eyes?",
	"I'm afraid not, no.",
	"Hmm, sounds exciting! Yes, I'll sign.",
	"If only someone could petition my wife to love me...",
	"No, It would be unwise to show this to Necrovarus.",
	"Yes! Oh, how I long to leave this world...",
	"Yes, I'll sign it. Give it here.",
	"No, get your smelly self away from me!",
	"Paper is made from the plant Papyrus!",
	"*Sniff* .... *Sniff* .... What is that?",
	"Do something for someone covered in Ectoplasm? No."
    };
    
    public static final int GHOST_VILLAGER = 1697;
    
    public GhostsAhoyPetition(final Player player) {
	this.player = player;
	this.signers = new ArrayList<Npc>();
	this.chatId = 0;
    }
    public int getEmotionForString(String string) {
	if (string.toLowerCase().contains("!") && string.toLowerCase().contains("no")) {
	    return Dialogues.ANGRY_2;
	} else if (string.toLowerCase().contains("!") && string.toLowerCase().contains("yes")) {
	    return Dialogues.HAPPY;
	} else {
	    return CONTENT;
	}
    }
    public int getAnswerForString(String string) {
	if (string.toLowerCase().contains("yes") || string.toLowerCase().contains("sure")) {
	    return 0;
	} else if (string.toLowerCase().contains("no")) {
	    return 1;
	} else {
	    return 2;
	}
    }
    public void setLastNpcTalked(Npc npc) {
	this.lastNpcTalked = npc;
    }
    
    public int signatures() {
	return signers.size();
    }
    
    public void clearSignatures() {
	signers.clear();
    }
    
    public void count() {
	if(signers.size() == 10) {
	    player.getActionSender().sendMessage("You have 10 signatures on your petition form. Go tell Necrovarus.");
	}
	else {
	   player.getActionSender().sendMessage("You have " + signers.size() + " signature(s) on your petition form."); 
	}
    }
    
    public int getChatId() {
	return this.chatId;
    }
    
    public void setChatId(int set) {
	this.chatId = set;
    }
    
    public void endDialogue() {
	this.chatId = -1;
	player.setFollowingEntity(null);
	player.getMovementHandler().finish();
	player.getMovementHandler().reset();
	player.getFollowing().followEntity();
    }
    
    public boolean sendDialogue(Player player, int id, int chatId, int optionId) {
	switch (id) {
	    case GHOST_VILLAGER:
		switch (player.getQuestStage(24)) {
		    case GhostsAhoy.ITEMS_FOR_ENCHANTMENT:
		    case GhostsAhoy.ITEMS_FOR_ENCHANTMENT_2:
			if (player.getInventory().playerHasItem(PETITION)) {
			    switch (chatId) {
				case 1:
				    if(signers.size() == 10) {
					player.getDialogue().sendStatement("You have 10 signatures on your petition form. Go tell Necrovarus.");
					endDialogue();
					return true;
				    } else {
				    player.getDialogue().setLastNpcTalk(GHOST_VILLAGER);
				    player.getDialogue().sendPlayerChat("Would you sign my petition against Necrovarus'", "unholy control of your free will?", CONTENT);
				    return true;
				    }
				case 2:
				    if (signers.contains(lastNpcTalked)) {
					player.getDialogue().setLastNpcTalk(GHOST_VILLAGER);
					player.getDialogue().sendNpcChat("I already signed your petition!", CONTENT);
					endDialogue();
					return true;
				    } else {
					String response = RANDOM_RESPONSES[Misc.randomMinusOne(RANDOM_RESPONSES.length)];
					player.getDialogue().setLastNpcTalk(GHOST_VILLAGER);
					player.getDialogue().sendNpcChat(response, getEmotionForString(response));
					player.setTempInteger(getAnswerForString(response));
					setChatId(2);
					return true;
				    }
				case 3:
				    if (player.getTempInteger() == 0) {
					player.getDialogue().sendStatement("The villager signs your petition form.");
					endDialogue();
					player.setTempInteger(-1);
					signers.add(lastNpcTalked);
					return true;
				    } else if (player.getTempInteger() == 1) {
					player.getDialogue().sendStatement("The villager refuses your petition form.");
					endDialogue();
					player.setTempInteger(-1);
					return true;
				    } else if (player.getTempInteger() == 2) {
					player.getDialogue().sendStatement("The villager seems to ignore your question.");
					endDialogue();
					player.setTempInteger(-1);
					return true;
				    }
				    return false;
			    }
			    return false;
			} else {
			    return false;
			}
		}
		return false;
	}
	return false;
    }
    
    
}
