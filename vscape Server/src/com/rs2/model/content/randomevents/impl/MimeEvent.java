package com.rs2.model.content.randomevents.impl;

import com.rs2.model.Position;
import com.rs2.model.content.tutorialisland.TutorialConstants;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class MimeEvent implements RandomEvent {
	
	private final static int mysteriousOldManId = 410;
	private final static int mimeId = 1056;
	private Npc mime;
	
	private Player player;
	public MimeEvent(final Player player)
	{
		this.player = player;
	}
	
	@Override
	public void spawnEvent() {
		player.getRandomHandler().spawnEventNpc(mysteriousOldManId);
		player.setStopPacket(true);
		player.setMovementDisabled(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		    int cycle = 0;
		    final String name = Misc.formatPlayerName(player.getUsername());
		    @Override
		    public void execute(CycleEventContainer container) {
		    	switch (cycle) {
		    		case 0:
		    			player.setStopPacket(true);
		    			player.getRandomEventNpc().getUpdateFlags().sendForceMessage("Come with me "+name+"!");
		    		break;
		    		case 2:
		    			player.teleport(new Position(2008,4762,player.getIndex()*4));
		    			player.getActionSender().hideAllSideBars();
		    			player.getActionSender().enableSideBarInterfaces(new int[]{7, 8, 9, 10});
		    			player.setStopPacket(false);
		    			player.getRandomHandler().destroyEventNpc();
		    			player.getRandomHandler().spawnEventNpc(mimeId);
		    			mime = player.getRandomEventNpc();
		    			mime.teleport(new Position(2011,4762,player.getIndex()*4));
		    			mime.setFollowingEntity(null);
		    			mime.setFace(3);
		    			//mime.getUpdateFlags().sendFaceToDirection(new Position(2011,4761,player.getIndex()*4));
					    container.stop();
				    return;
		    	}
		    	cycle++;
		    }
	
		    @Override
		    public void stop() {
		    }
		}, 1);
	}

	@Override
	public void destroyEvent() {
		player.getRandomHandler().setCurrentEvent(null);
		player.getRandomHandler().destroyEventNpc();
		player.setMovementDisabled(false);
		mime = null;
		player.teleport(player.getLastPosition());
	}

	@Override
	public void showInterface() {
	}

	@Override
	public boolean handleButtons(int buttonID) {
		return false;
	}

	@Override
	public boolean sendDialogue(int id, int chatId, int optionId, int npcChatId) {

		return false;
	}

	
	
}
