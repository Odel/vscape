package com.rs2.model.content.skills.firemaking;

import com.rs2.Constants;
import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.Position;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

/**
 * Firemaking skill handler
 */

public class Firemaking {// todo firelighters

	private Player player;

	public Firemaking(Player player) {
		this.player = player;
	}

	public static final int[] FIRE_IDS = {2732, 11404, 11405, 11406};

	public enum Firemake {
		LOG(1511, 1, 40, 2732, 100), // LOG
		BLUE_LOG(7406, 1, 250, 11406, 150), // BLUE LOG
		GREEN_LOG(7405, 1, 250, 11405, 150), // GREEN LOG
		RED_LOG(7404, 1, 250, 11404, 150), // RED LOG
		ACHEY(2862, 1, 40, 2732, 100), // ACHEY
		OAK(1521, 15, 60, 2732, 150), // OAK
		WILLOW(1519, 30, 90, 2732, 200), // WILLOW
		TEAK(6333, 35, 105, 2732, 250), // TEAK
		MAPLE(1517, 45, 135, 2732, 300), // MAPLE
		ARTIC_PINE(10810, 45, 135, 2732, 300), // ARTIC PINE
		MAHOGANY(6332, 50, 158, 2732, 350), // MAHOGANY
		YEW(1515, 60, 203, 2732, 400), // YEW
		MAGIC(1513, 75, 304, 2732, 500), // MAGIC
		PYRE_LOG(3438, 5, 50, 2732, 150), // PYRE LOG
		PYRE_OAK(3440, 20, 70, 2732, 200), // PYRE OAK
		PYRE_WILLOW(3442, 35, 100, 2732, 250), // PYRE WILLOW
		PYRE_MAPLE(3444, 50, 175, 2732, 350), // PYRE MAPLE
		PYRE_YEW(3446, 65, 225, 2732, 450), // PYRE YEW
		PYRE_MAGIC(3448, 80, 405, 2732, 550); // PYRE MAGIC
		
		private int log;
		private int level;
		private int xp;
		private int fire;
		private int time;
		
		private Firemake(int log, int level, int xp, int fire, int time) {
			this.log = log;
			this.level = level;
			this.xp = xp;
			this.fire = fire;
			this.time = time;
		}

		public int getLog() {
			return log;
		}
		
		public int getLevel() {
			return level;
		}
		
		public int getXp() {
			return xp;
		}
		
		public int getFire() {
			return fire;
		}
		
		public int getTime() {
			return time;
		}
	}

	public static Firemake getFiremake(int item) {
		for (Firemake f : Firemake.values()) {
			if (f.getLog() == item) {
				return f;
			}
		}
		return null;
	}

	public void attemptFire(final int useWith, final int withUse, boolean fromGround, int x, int y, int h) {
		if (!Constants.FIREMAKING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		int log = useWith == 590 ? withUse : useWith;
		Firemake firemake = getFiremake(log);
		if (firemake == null) {
			return;
		}
		if (!player.getInventory().playerHasItem(590)) {
			player.getActionSender().sendMessage("You need a tinderbox to light this fire.");
			return;
		}
		if (System.currentTimeMillis() - player.getLastFire() < 200) {
			return;
		}
		if (player.inBank() || player.inCwGame() || player.inDuelArena()) {
			player.getActionSender().sendMessage("You can't light a fire here.");
			return;
		}
		GameObject p = ObjectHandler.getInstance().getObject(x, y, h);
		if (p != null) {
			player.getActionSender().sendMessage("You can't light a fire here.");
			return;
		}
		final CacheObject obj = ObjectLoader.object(x, y, h);
		if (obj != null) {
			int type = obj.getType();
			if(type != 22){
				player.getActionSender().sendMessage("You can't light a fire here.");
				return;
			}
		}
		if (!SkillHandler.hasRequiredLevel(player, Skill.FIREMAKING, firemake.getLevel(), "light these logs")) {
			return;
		}
		startFire(player, firemake, x, y, h, fromGround);
	}

	public void startFire(final Player player, final Firemake firemake, final int x, final int y, final int h, boolean fromGround) {
		final boolean notInstant = System.currentTimeMillis() - player.getLastFire() > 2500;
		GroundItem item = null;
		int time = 2;
		if (!fromGround) {
			if (!player.getInventory().removeItem(new Item(firemake.getLog()))) {
				return;
			}
            item = new GroundItem(new Item(firemake.getLog()), player);
            GroundItemManager.getManager().dropItem(item);
		} else {
            item = GroundItemManager.getManager().findItem(player, firemake.getLog(), new Position(x, y, player.getPosition().getZ()));
            if (item == null)
                return;
        }
		if (notInstant) {
			player.getActionSender().sendMessage("You attempt to light the logs.");
			player.getUpdateFlags().sendAnimation(733);
			player.getActionSender().sendSound(375, 0, 0);
			time = 3 + Misc.random(6);
		}
		if (player.getNewComersSide().isInTutorialIslandStage()) {
			player.getDialogue().sendTutorialIslandWaitingInfo("", "Your character is now attempting to light the fire.", "This should take only a few seconds", "", "Please wait...");
		}
		final int task = player.getTask();
        final GroundItem finalItem = item;
        player.setSkilling(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task) || (finalItem != null && !GroundItemManager.getManager().itemExists(player, finalItem))) {
					container.stop();
					return;
				}
				if (player.canMove(-1, 0)) {
					player.getActionSender().walkTo(-1, 0, false);
				} else {
					player.getActionSender().walkTo(1, 0, false);
                }
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer p) {
						new GameObject(firemake.getFire(), x, y, h, -1, 10, 6951, firemake.getTime() + Misc.random(50));
						player.getActionSender().sendMessage("The fire catches and the logs begin to burn.");
						player.getActionSender().sendSound(235, 0, 0);
						if (player.getNewComersSide().isInTutorialIslandStage() && player.getNewComersSide().getTutorialIslandStage() == 8) {
							player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);
						}
                        if (finalItem != null)
						    GroundItemManager.getManager().destroyItem(finalItem);
				        player.getUpdateFlags().sendFaceToDirection(new Position(x, y));
						player.getSkill().addExp(Skill.FIREMAKING, firemake.getXp());
						player.setLastFire(System.currentTimeMillis());
						p.stop();
					}
					@Override
					public void stop() {
						player.setStopPacket(false);
					}
				}, 1);
				container.stop();
			}
			@Override
			public void stop() {
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), time);
	}

	public static final boolean isFire(int id) {
		for (int fire : FIRE_IDS) {
			if (fire == id) {
				return true;
			}
		}
		return false;
	}

}