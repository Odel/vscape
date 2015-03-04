package com.rs2.model.content.skills.magic;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

import java.util.HashMap;
import java.util.Map;

public class TeleTabs {
	
	public enum TabletData {
		VARROCK(8007, Constants.VARROCK_X, Constants.VARROCK_Y),
		LUMBRIDGE(8008, Constants.LUMBRIDGE_X, Constants.LUMBRIDGE_Y), 
		FALADOR(8009, Constants.FALADOR_X, Constants.FALADOR_Y), 
		CAMELOT(8010, Constants.CAMELOT_X, Constants.CAMELOT_Y), 
		ARDOUGNE(8011, Constants.ARDOUGNE_X, Constants.ARDOUGNE_Y), 
		WATCHTOWER(8012, Constants.WATCH_TOWER_X, Constants.WATCH_TOWER_Y);

		private int tabletId;
		private int posX;
		private int posY;
		
		private TabletData(int tabletId, int posX, int posY) {
			this.tabletId = tabletId;
			this.posX = posX;
			this.posY = posY;
		}
		
		private static Map<Integer, TabletData> tablets = new HashMap<Integer, TabletData>();
		static {
			for (TabletData tab : TabletData.values()) {
				tablets.put(tab.tabletId, tab);
			}
		}

		public static TabletData forId(int item) {
			return tablets.get(item);
		}
	}
	
	public static boolean breakTab(final Player player, final int item, int slot) {
		TabletData tab = TabletData.forId(item);
		if(tab != null)
		{
			player.getActionSender().removeInterfaces();
			if (!player.getInventory().playerHasItem(item)) {
				return false;
			}
			if(tab == TabletData.ARDOUGNE && !player.getQuestVars().canTeleportArdougne()) {
				player.getDialogue().sendStatement("You must complete Plague City to use this.");
				player.getDialogue().endDialogue();
				return true;
			} 
			if (!player.getTeleportation().attemptTeleportTablet(new Position(tab.posX,tab.posY,0))) {
				return true;
			}
			player.getInventory().removeItemSlot(new Item(tab.tabletId,1), slot);
			player.getActionSender().sendMessage("You break the teleport tab.");
			player.getUpdateFlags().sendAnimation(4731);
			player.getUpdateFlags().sendGraphic(678);
			return true;
		}
		return false;
	}
	
}
