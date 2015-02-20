package com.rs2.model.players.item.functions;

import com.rs2.model.content.Emotes.EMOTE;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class Toys {

	public enum ToyHorsey {
		BROWN(2520, 918),
		WHITE(2522, 919),
		BLACK(2524, 920),
		GREY(2526, 921);
		
		private int item;
		private int anim;
		
		private ToyHorsey(int item, int anim)
		{
			this.item = item;
			this.anim = anim;
		}
		
		public static ToyHorsey forItemId(int id) {
	    	for (ToyHorsey horsey : ToyHorsey.values()) {
				if (horsey.item == id) {
			    	return horsey;
				}
	    	}
	    	return null;
		}
		
		public int getItem(){
			return item;
		}
		
		public int getAnim(){
			return anim;
		}
	}
	
	private final static String HORSE_RESPONSES[] = {
			"Come on Dobbin, we can win the race!",
			"Hi-ho Silver, and away!",
			"Neaahhhyyy! Giddy-up horsey!"};
	
	public static boolean itemFirstClick(final Player player, final int item, int slot)  
	{
		switch(item)
		{
			case 2520 :
			case 2522 :
			case 2524 :
			case 2526 :
				ToyHorsey horsey = ToyHorsey.forItemId(item);
				if(horsey != null)
				{
					player.getUpdateFlags().sendAnimation(horsey.getAnim());
					player.getUpdateFlags().sendForceMessage(HORSE_RESPONSES[Misc.random(HORSE_RESPONSES.length-1)]);
					player.getActionSender().stopPlayerPacket(3);
					return true;
				}
			return false;
			case 4613 :
				player.getUpdateFlags().sendAnimation(1902);
				final int task = player.getTask();
		        player.setSkilling(new CycleEvent() {
		        	int spinTimer = 0;
		        	boolean successful = false;
					@Override
					public void execute(CycleEventContainer container) {
						if (!player.checkTask(task)) {
							if (!player.isDead()) {
								DropSpinningPlate(player);
							}
							container.stop();
							return;
						}
						if(Misc.random(100) >= 50)
						{
							successful = true;
						}else{
							successful = false;
						}
		                if (!player.isDead()) {
							if(successful)
							{
			                	if(spinTimer >= 6)
			                	{
			                		player.getUpdateFlags().sendAnimation(1904);
			                		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			                			@Override
			                			public void execute(CycleEventContainer container) {
			                				player.getEmotes().performEmote(EMOTE.CHEER);
			                				container.stop();
			                			}

			                			@Override
			                			public void stop() {
			                			}
			                		}, 2);
									container.stop();
									return;
			                	}
							}else{
			                	if(spinTimer >= 4)
			                	{
			                		DropSpinningPlate(player);
									container.stop();
									return;
			                	}
							}
		                } else {
							spinTimer = 0;
							container.stop();
							return;
						}
		                spinTimer++;
					}
					@Override
					public void stop() {
					}
				});
		        CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 1);
			return true;
		}
		return false;
	}
	
	private static void DropSpinningPlate(final Player player)
	{
		player.getUpdateFlags().sendAnimation(1906);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int dropTimer = 0;
			@Override
			public void execute(CycleEventContainer container) {
				if(dropTimer == 1){
					if(player.getInventory().removeItem(new Item(4613, 1))){
						GroundItem item = new GroundItem(new Item(4613, 1), player, player.getPosition());
						GroundItemManager.getManager().dropItem(item);
					}
				}
				if(dropTimer == 2){
					player.getEmotes().performEmote(EMOTE.CRY);
					container.stop();
					return;
				}
				dropTimer++;
			}

			@Override
			public void stop() {
			}
		}, 1);
	}
}
