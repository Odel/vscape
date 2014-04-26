package com.rs2.model.content.minigames.gnomeball;

import com.rs2.Constants;
import com.rs2.model.World;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 10/03/12 Time: 21:25 To change
 * this template use File | Settings | File Templates.
 */
public class GnomeBall {

	public void throwGnomeBall(final Player player, final Player victim) {
		if (!player.getInventory().getItemContainer().contains(751))
			return;

		player.getUpdateFlags().sendAnimation(3353);

		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				int attackerX = player.getPosition().getX(), attackerY = player.getPosition().getY();
				int victimX = victim.getPosition().getX(), victimY = victim.getPosition().getY();
				final int offsetX = (attackerY - victimY) * -1;
				final int offsetY = (attackerX - victimX) * -1;
				World.sendProjectile(player.getPosition(), offsetX, offsetY, 55, 43, 40, 70, victim.getIndex() + (victim.isPlayer() ? -1 : 1), false);
				if (victim.getEquipment().getItemContainer().get(Constants.WEAPON) != null) {
					player.getActionSender().sendMessage("This player seems to have his hands full!");
					return;
				}
				player.getEquipment().getItemContainer().remove(new Item(751));
				player.getActionSender().sendMessage("You attempt to throw your gnomeball to the other player...");
				player.getUpdateFlags().faceEntity(victim.getIndex());
				victim.getUpdateFlags().faceEntity(player.getIndex());
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						player.getActionSender().sendMessage("The other player manages to get it.");
						victim.getEquipment().getItemContainer().set(Constants.WEAPON, new Item(751));
						player.getUpdateFlags().sendAnimation(victim.getBlockAnimation());
						container.stop();
					}
					@Override
					public void stop() {
					}
				}, 5);
				container.stop();
			}
			@Override
			public void stop() {
			}
		}, 3);
	}
}
