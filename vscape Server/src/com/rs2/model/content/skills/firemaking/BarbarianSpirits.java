package com.rs2.model.content.skills.firemaking;

import com.rs2.model.Position;
import com.rs2.model.content.dialogue.DialogueManager;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

import java.util.Random;


public class BarbarianSpirits {
    private static final int FIRE = 2732;
    private static final int BURN_ANIM = 897;
    private static final int PEACEFUL_BARB = 754;
    private static final int PEACEFUL_DIALOGUE = 19098;
    private static final int PEACEFUL_LEAVING_ANIM = 6724;
    private static final int ANGRY_APPEARING_ANIM = 6725;
    
    @SuppressWarnings("unused")
	private final Player player;
    private Npc spiritSummoned;
    
    public BarbarianSpirits(final Player player) {
    	this.player = player;
    	this.spiritSummoned = null;
    }
    
    public enum BarbarianSpiritData {
	MANGLED_BONES(11337, 752, 10),
	CHEWED_BONES(11338, 754, 100);
	
	private final int boneId, spiritId, peacefulChance;
	BarbarianSpiritData(int bone, int npcId, int chance) {
	    this.boneId = bone;
	    this.spiritId = npcId;
	    this.peacefulChance = chance;
	}
	
	public int getBoneId() {
	    return this.boneId;
	}
	
	public int getSpiritId() {
	    return this.spiritId;
	}
	
	public int getPeacefulChance() {
	    return this.peacefulChance;
	}
	
	public static BarbarianSpiritData getDataForBone(int bone) {
	    for(BarbarianSpiritData data : BarbarianSpiritData.values()) {
		if(data.getBoneId() == bone) {
		    return data;
		}
	    }
	    return null;
	}
    }
    
    public Npc getSpiritSummoned() {
	return this.spiritSummoned;
    }
    
    public void setSpiritSummoned(Npc npc) {
	this.spiritSummoned = npc;
    }
    private static void handleReward(final Player player) {
	player.setStopPacket(true);
	CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
	    @Override
	    public void execute(CycleEventContainer b) {
		b.stop();
	    }

	    @Override
	    public void stop() {
		player.getBarbarianSpirits().getSpiritSummoned().setDeathPosition(player.getPosition().clone());
		player.getBarbarianSpirits().getSpiritSummoned().dropItems(player);
		NpcLoader.destroyNpc(player.getBarbarianSpirits().getSpiritSummoned());
		player.getBarbarianSpirits().setSpiritSummoned(null);
		player.setStopPacket(false);
	    }
	}, 5);
    }
    public static boolean handleItemOnObject(final Player player, final int objectId, final int itemId, final int x, final int y) {
	switch(objectId) {
	    case FIRE:
		final BarbarianSpiritData spirit = BarbarianSpiritData.getDataForBone(itemId);
		if(spirit != null) {
		    if(player.getSkill().getLevel()[Skill.FIREMAKING] < 35) {
			player.getDialogue().sendStatement("You need atleast 35 Firemaking to do this.");
			return true;
		    }
		    if (player.getInventory().playerHasItem(spirit.getBoneId()) && itemId == spirit.getBoneId() && player.getBarbarianSpirits().getSpiritSummoned() != null) {
			if (!player.getSpawnedNpc().isVisible()) {
			    player.setSpawnedNpc(null);
			}
			if (player.getSpawnedNpc() != null && player.getSpawnedNpc().getNpcId() == player.getBarbarianSpirits().getSpiritSummoned().getNpcId()) {
			    player.getActionSender().sendMessage("You already have a barbarian spirit summoned!");
			    return true;
			} else {
			    player.getBarbarianSpirits().setSpiritSummoned(null);
			}
		    }
		    if (player.getInventory().playerHasItem(spirit.getBoneId()) && itemId == spirit.getBoneId() && player.getSpawnedNpc() == null) {
			player.getActionSender().sendMessage("You begin to burn the bones...");
			player.getInventory().removeItem(new Item(spirit.getBoneId()));
			player.getUpdateFlags().sendAnimation(BURN_ANIM);
			player.setStopPacket(true);
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			    @Override
			    public void execute(CycleEventContainer b) {
				if (spirit.getPeacefulChance() >= (new Random().nextDouble() * 100.0) || spirit.equals(BarbarianSpiritData.CHEWED_BONES)) {
				    Npc spiritNpc = new Npc(PEACEFUL_BARB);
				    NpcLoader.spawnPlayerOwnedSpecificLocationNpc(player, spiritNpc, new Position(player.getClickX(), player.getClickY(), player.getPosition().getZ()), false, null);
				    player.getBarbarianSpirits().setSpiritSummoned(spiritNpc);
				    player.resetAllActions();
				    Dialogues.startDialogue(player, PEACEFUL_DIALOGUE);
				} else {
				    Npc spiritNpc = new Npc(spirit.getSpiritId());
				    NpcLoader.spawnPlayerOwnedAttackNpc(player, spiritNpc, new Position(player.getClickX(), player.getClickY(), player.getPosition().getZ()), false, "AAAAARGH!");
				    spiritNpc.getUpdateFlags().sendAnimation(ANGRY_APPEARING_ANIM);
				    player.getActionSender().sendMessage("The spirit from these bones is not peaceful!");
				    player.getBarbarianSpirits().setSpiritSummoned(spiritNpc);
				    player.resetAllActions();
				}
				b.stop();
			    }

			    @Override
			    public void stop() {
				player.setStopPacket(false);
			    }
			}, 4);
			return true;
		    }
		    return false;
		}
	    return false;
	}
	return false;
    }
    
    public static boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
	DialogueManager d = player.getDialogue();
	switch(id) {
	    case PEACEFUL_DIALOGUE:
		switch (player.getDialogue().getChatId()) {
		    case 1:
			d.setLastNpcTalk(PEACEFUL_BARB);
			d.sendStatement("You hear a voice nudge your thoughts.", "'Thank you adventurer. You have laid my spirit to rest.'", "'I'll leave you something in gratitude in my parting.'");
			player.getBarbarianSpirits().getSpiritSummoned().getUpdateFlags().sendAnimation(PEACEFUL_LEAVING_ANIM);
			handleReward(player);
			return true;
		    case 2:
			d.sendStatement("The Barbarian spirit fades into the ether.");
			d.endDialogue();
			return true;
		}
		return false;
	}
	return false;
    }
}
