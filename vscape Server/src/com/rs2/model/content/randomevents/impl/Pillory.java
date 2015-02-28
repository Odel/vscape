package com.rs2.model.content.randomevents.impl;

import com.rs2.model.Position;
import com.rs2.model.content.randomevents.EventsConstants;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class Pillory {
	
	private static final int PILLORY_GUARD = 2573;
	private static final Position[] cages = {
		 new Position(2604,3105,0), new Position(2606,3105,0), new Position(2608,3105,0), //yanille
		 new Position(2681,3489,0), new Position(2683,3489,0), new Position(2685,3489,0), //seers
		 new Position(3226,3407,0), new Position(3228,3407,0), new Position(3230,3407,0) //varrock
	};
	
	private static int LOCKMAIN_CHILD = 6154;
	private static int KEY1_CHILD = 6155;
	private static int KEY2_CHILD = 6186;
	private static int KEY3_CHILD = 6187;
	//lock model key Model
	private static final int[][] combos = {
		{9753,9749}, //diamond
		{9754,9750}, //square
		{9755,9751}, //circle
		{9756,9752} //triangle
	};
	
	private int nextCombo = 0;
	private int[] keys = new int[3];
	private int locksCorrect = 0;
	
	public Player player;
	
	public Pillory(final Player player)
	{
		this.player = player;
	}
	
	private void cycleEvent(final Npc npc, final Player player, final String[] chat, final int transformId) {
		CycleEventHandler.getInstance().addEvent(npc, new CycleEvent() {
		    int cycle = 0;
		    final String name = Misc.formatPlayerName(player.getUsername());
		    @Override
		    public void execute(CycleEventContainer container) {
		    	switch (cycle) {
		    		case 0:
		    			player.setStopPacket(true);
		    			npc.getUpdateFlags().sendForceMessage(chat[0].replaceAll("%username%", name));
		    		break;
		    		case 2:
		    			player.teleport(getRandomJail());
		    			player.setInJail(true);
		    			player.setStopPacket(false);
		    			player.getRandomHandler().destroyEventNpc();
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
	
	public void JailPlayer()
	{
		if(!canJail())
			return;
		final Npc npc = new Npc(PILLORY_GUARD);
		NpcLoader.spawnNpc(player, npc, false, false);
		player.getActionSender().sendStillGraphic(EventsConstants.RANDOM_EVENT_GRAPHIC, npc.getPosition(), 100 << 16);
		player.setSpawnedNpc(npc);
		cycleEvent(npc, player, new String[]{"Ah, %username%! you're for da cage!"}, 0);
	}
	
	public void UnJailPlayer()
	{
		player.setInJail(false);
		player.teleport(player.getLastPosition());
		player.getActionSender().sendMessage("You have been set free from jail.");
	}
	
	public boolean doJailEvent(){
		if(!canJail())
			return false;
		return Misc.random(1000) == 0;
	}
	
	private Position getRandomJail()
	{
		return cages[Misc.random(cages.length-1)];
	}
	
	public boolean canJail()
	{
		if(player.getInJail() || player.inMiniGameArea() || player.inBarrows() 
		|| (player.getRandomEventNpc() != null) || (player.getSpawnedNpc() != null)){
			return false;
		}
		return true;
	}
	
	public void openInterface()
	{
		if(!player.getInJail())
			return;
		locksCorrect = 0;
		//TODO possible should be fullscreen interface
		//NEED TO FIX THE ACTION SENDER METHOD FOR FULLSCREEN INTERS
		player.getActionSender().sendInterface(6152);
		player.getActionSender().sendConfig(531, 0);
		player.getActionSender().sendInterfaceAnimation(LOCKMAIN_CHILD, 2765);
		player.getActionSender().sendInterfaceAnimation(6195, 2766);
		player.getActionSender().sendComponentInterface(6195, -1);
		player.getActionSender().sendComponentInterface(6196, -1);
		player.getActionSender().sendComponentInterface(6197, -1);
		setNextCombo();
	}

	public boolean handleButton(int buttonId){
		switch(buttonId)
		{
			case 24044 ://key1
				selectKey(0);
			return true;
			case 24045 ://key2
				selectKey(1);
			return true;
			case 24046 ://key3
				selectKey(2);
			return true;
		}
		return false;
	}
	
	private void selectKey(int index)
	{
		if(keys[index] != nextCombo)
		{
			locksCorrect = 0;
			player.getActionSender().removeInterfaces();
			return;
		}
		locksCorrect++;
		player.getActionSender().sendConfig(531, locksCorrect);
		if(locksCorrect >= 3)
		{
			UnJailPlayer();
		}
		setNextCombo();
	}
	
	private void setNextCombo(){
		int nextComboIndex = Misc.random(combos.length-1);
		nextCombo = combos[nextComboIndex][1];
		player.getActionSender().sendComponentInterface(LOCKMAIN_CHILD, combos[nextComboIndex][0]);
		setKeys(nextComboIndex);
	}
	
	private void setKeys(int nextComboIndex){
		for(int i = 0; i < 3; i++)
		{
			keys[i] = combos[Misc.random(combos.length-1)][1];
		}
		int randomKey = Misc.random(keys.length-1);
		keys[randomKey] = combos[nextComboIndex][1];
		player.getActionSender().sendComponentInterface(KEY1_CHILD, keys[0]);
		player.getActionSender().sendComponentInterface(KEY2_CHILD, keys[1]);
		player.getActionSender().sendComponentInterface(KEY3_CHILD, keys[2]);
	}
	
}
