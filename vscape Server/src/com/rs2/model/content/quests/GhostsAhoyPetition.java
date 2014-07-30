package com.rs2.model.content.quests;

import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.quests.GhostsAhoy.PETITION;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import java.util.ArrayList;

public class GhostsAhoyPetition {
    private Player player;
    private static ArrayList<Npc> signers;
    private static Npc lastNpcTalked;
    
    public static final int GHOST_VILLAGER = 1697;
    
    public GhostsAhoyPetition(final Player player) {
	this.player = player;
	this.signers = new ArrayList<Npc>();
    }
    
    public void setLastNpcTalked(Npc npc) {
	this.lastNpcTalked = npc;
    }
    
    public void count() {
	if(signers.size() == 10) {
	    player.getActionSender().sendMessage("You have 10 signatures on your petition form. Go tell Necrovarus.");
	}
	else {
	   player.getActionSender().sendMessage("You have " + signers.size() + " signature(s) on your petition form."); 
	}
    }
    
    public static boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
	switch(id) {
	    case GHOST_VILLAGER:
		switch (player.getQuestStage(24)) {
		    case GhostsAhoy.ITEMS_FOR_ENCHANTMENT:
		    case GhostsAhoy.ITEMS_FOR_ENCHANTMENT_2:
			if (player.getInventory().playerHasItem(PETITION)) {
			    switch (player.getDialogue().getChatId()) {
				case 1:
				    player.getDialogue().sendPlayerChat("Would you please sign my petition?", CONTENT);
				    return true;
				case 2:
				    if(signers.contains(lastNpcTalked)) {
					player.getDialogue().sendNpcChat("I already signed your petition.", CONTENT);
					player.getDialogue().endDialogue();
					return true;
				    }
				    else {
					player.getDialogue().sendNpcChat("Sure.", CONTENT);
					return true;
				    }
				case 3:
				    player.getDialogue().sendStatement("The village signs your petition form.");
				    player.getDialogue().endDialogue();
				    signers.add(lastNpcTalked);
				    return true;
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
