package com.rs2.model.objects.functions;

import com.rs2.Constants;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 10/03/12 Time: 21:09 To change
 * this template use File | Settings | File Templates.
 */
public class PickableObjects {

	public static final int[][] pickableObjects = {{5585, 1947}, {3366, 1957}, {1161, 1965}, {312, 1942}, {313, 1947}, {2646, 1779}};

	public static boolean pickObject(final Player player, final int id, final int obX, final int obY) {
		if (!SkillHandler.checkObject(id, obX, obY, player.getPosition().getZ())) {
			return false;
		}
		int itemId = 0;
		for (int[] i : pickableObjects) {
			if (i[0] == id) {
				itemId = i[1];
				break;
			}
		}
		if (itemId < 1) {
			return false;
		}
		final Item item = new Item(itemId);
		final String plantName = item.getDefinition().getName().toLowerCase();
		if (!player.getInventory().canAddItem(item)) {
			return true;
		}
		player.getTask();
		player.getUpdateFlags().sendAnimation(827);
		player.setStopPacket(true);
		player.setSkilling(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!SkillHandler.checkObject(id, obX, obY, player.getPosition().getZ())) {
					player.getActionSender().sendMessage("Too late, the plant is gone.");
					container.stop();
					return;
				}
				player.getActionSender().sendSound(356, 1, 0);
				player.getInventory().addItem(item);
				if (item.getId() != 1779 || Misc.random(3) == 0) {
					int face = SkillHandler.getFace(id, obX, obY, player.getPosition().getZ());
					new GameObject(Constants.EMPTY_OBJECT, obX, obY, player.getPosition().getZ(), face, 10, id, 20);
				}
				player.getActionSender().sendMessage("You pick a " + plantName + ".");
				container.stop();
			}
			@Override
			public void stop() {
				player.setStopPacket(false);
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 2);
		return true;
	}

}
