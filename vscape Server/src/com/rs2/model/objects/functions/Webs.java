package com.rs2.model.objects.functions;

import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class Webs {

	public static void slashWeb(final Player player, final int obX, final int obY, int itemId) {
		final GameObject p = ObjectHandler.getInstance().getObject(733, obX, obY, 0);
		if (p != null && p.getDef().getId() != 733) {
			return;
		}
		final Item wep = new Item(itemId);
		String name = wep.getDefinition().getName().toLowerCase();
		if (!name.contains("knife") && wep.getDefinition().getBonus(1) < 1) {
			player.getActionSender().sendMessage("You need a sharp weapon to slash through this.");
			return;
		}
		player.getTask();
		player.setStopPacket(true);
		player.getUpdateFlags().sendAnimation(451);
		CacheObject g = ObjectLoader.object(733, obX, obY, 0);
		final int face = g.getRotation();
		final Boolean slashSuccess = wep.getDefinition().getBonus(1) + 1 > 0;
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!slashSuccess) {
					player.getActionSender().sendMessage("You fail to slash through the web.");
				} else {
					new GameObject(734, obX, obY, 0, face, 10, 733, 20);
					ObjectHandler.getInstance().removeClip(733, obX, obY, 0, 10, face);
					player.getActionSender().sendMessage("You successfully slash open the web.");
				}
				container.stop();
			}
			@Override
			public void stop() {
				player.setStopPacket(false);
			}
		}, 2);
	}
}
