package com.rs2.model.npcs;

import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class NpcActions {

	public static void shearSheep(final Player player) {
		if (player.getInteractingEntity() == null || player.getInteractingEntity().isPlayer()) {
			return;
		}
		final Npc npc = (Npc) player.getInteractingEntity();
		final int x = npc.getPosition().getX(), y = npc.getPosition().getY();
		if (npc.getTransformTimer() > 0) {
			player.getActionSender().sendMessage("This sheep has already been sheared.");
			return;
		}
		player.setStopPacket(true);
		player.getUpdateFlags().sendAnimation(894);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (x != npc.getPosition().getX() || y != npc.getPosition().getY()) {
					player.getActionSender().sendMessage("The sheep manages to get away!");
					container.stop();
					return;
				}
				player.getInventory().addItem(new Item(1737));
				npc.getUpdateFlags().setForceChatMessage("Baa!");
				player.getActionSender().sendMessage("You manage to shear the sheep.");
				npc.sendTransform(42, 25);
				container.stop();
			}
			@Override
			public void stop() {
				player.setStopPacket(false);
			}
		}, 2);
	}
}
