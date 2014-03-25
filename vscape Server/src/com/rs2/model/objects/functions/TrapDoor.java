package com.rs2.model.objects.functions;

import com.rs2.Constants;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.GameObjectDef;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class TrapDoor {

	public static void handleTrapdoor(final Player player, final int originalId, final int newId, final GameObjectDef def) {
		final int face = def.getFace();
		final int type = def.getType();
		player.setStopPacket(true);
		player.getUpdateFlags().sendAnimation(827);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (ObjectHandler.getInstance().getObject(originalId, def.getPosition().getX(), def.getPosition().getY(), def.getPosition().getZ()) != null) {
					ObjectHandler.getInstance().removeObject(originalId, def.getPosition().getX(), def.getPosition().getY(), def.getPosition().getZ(), type);
					if (originalId == 883) {
						ObjectHandler.getInstance().removeObject(def.getPosition().getX(), def.getPosition().getY() + 1, def.getPosition().getZ(), type);
					}
				} else {
					new GameObject(newId, def.getPosition().getX(), def.getPosition().getY(), def.getPosition().getZ(), face, type, originalId, 999999);
					if (originalId == 881) {
						new GameObject(883, def.getPosition().getX(), def.getPosition().getY() - 1, def.getPosition().getZ(), face, type, Constants.EMPTY_OBJECT, 999999);
					}
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
