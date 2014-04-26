package com.rs2.model.content.dungeons;

import java.util.Random;

import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.Position;
import com.rs2.model.content.combat.SkullRecord;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.Tools;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 27/01/12 Time: 20:58 To change
 * this template use File | Settings | File Templates.
 */
public class Abyss { // todo passage: You step through the portal.
	public static final int SMALL_POUCH = 5509;
	public static final int MEDIUM_POUCH = 5510;
	public static final int MEDIUM_BROKEN_POUCH = 5511;
	public static final int LARGE_POUCH = 5512;
	public static final int LARGE_BROKEN_POUCH = 5513;
	public static final int GIANT_POUCH = 5514;
	public static final int GIANT_BROKEN_POUCH = 5515;

	public static int[] pouches = { SMALL_POUCH, MEDIUM_POUCH, LARGE_POUCH,
			GIANT_POUCH };

	public static int getPossiblePouch(Player player) {
		for (int pouch : pouches) {
			if (player.getInventory().ownsItem(pouch)) {
				continue;
			}
			return pouch;
		}
		return -1;
	}

	public static void dropPouches(Player player, Npc npc) {
		if (getPossiblePouch(player) == -1) {
			return;
		}
		if (npc.getDefinition().getName().equalsIgnoreCase("leech")
				|| npc.getDefinition().getName().equalsIgnoreCase("guardian")
				|| npc.getDefinition().getName().equalsIgnoreCase("walker")) {
			if (Misc.random(100) == 5) {
                GroundItem item = new GroundItem(new Item(getPossiblePouch(player)), player,  npc.getPosition());
                GroundItemManager.getManager().dropItem(item);
			}
		}
	}

	public static boolean handleObstacle(Player player, int objectId,
			int objectX, int objectY) {

		switch (objectId) {
		// boil
		case 7145:
			burnBoil(player, objectX, objectY, 3024, 4833);
			return true;
		case 7151:
			burnBoil(player, objectX, objectY, 3053, 4830);
			return true;
			// rock
		case 7143:
			mineRock(player, objectX, objectY, 3030, 4821);
			return true;
		case 7153:
			mineRock(player, objectX, objectY, 3048, 4822);
			return true;

			// tendrils
		case 7152:
			chopTendrils(player, objectX, objectY, 3050, 4824);
			return true;
		case 7144:
			chopTendrils(player, objectX, objectY, 3028, 4824);
			return true;

			// gap
		case 7149:
			squeezeGap(player, objectX, objectY, 3048, 4842);
			return true;
		case 7147:
			squeezeGap(player, objectX, objectY, 3031, 4842);
			return true;

			// eyes
		case 7146:
			distractEye(player, objectX, objectY, 3029, 4841);
			return true;
		case 7150:
			distractEye(player, objectX, objectY, 3051, 4838);
			return true;

			// passage
		case 7148:
			handlePassage(player);
			return true;

		}
		return false;

	}

	public static void mainObstacleAction(final Player player,
			final int objectX, final int objectY, final int crossX,
			final int crossY, final String obstacleType, final int firstObject,
			final int secondObject, final int counter, boolean canDoIt) {
		final CacheObject g = ObjectLoader.object(objectX, objectY, player
				.getPosition().getZ());
		if (!canDoIt) {
			return;
		}
		doFirstActions(player, obstacleType);
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int count = counter;

			@Override
			public void execute(CycleEventContainer b) {
				doAnimation(player, obstacleType);
				if (count == 2) {
					if (Misc.random(3) == 0) {
						count--;
						new GameObject(firstObject, objectX, objectY, player
								.getPosition().getZ(), g.getRotation(), g
								.getType(), g.getDef().getId(), 1000);

					} else {
						failToCross(player, obstacleType);
						b.stop();
					}
				} else if (count == 1) {
					count--;
					ObjectHandler.getInstance().removeObject(objectX, objectY,
							player.getPosition().getZ(), 10);
					new GameObject(secondObject, objectX, objectY, player
							.getPosition().getZ(), g.getRotation(),
							g.getType(), g.getDef().getId(), 20);

				} else if (count == 0) {
					doSecondaryActions(player, obstacleType);
					sendCrossEvent(player, crossX, crossY);
					b.stop();
				}
			}

			@Override
			public void stop() {
				player.setStopPacket(false);
			}
		}, 3);

	}

	private static void sendCrossEvent(final Player player, final int crossX,
			final int crossY) {
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				player.teleport(new Position(crossX, crossY, player
						.getPosition().getZ()));
				b.stop();
			}

			@Override
			public void stop() {
				player.setStopPacket(false);
			}
		}, 3);
	}

	private static void failToCross(Player player, String obstacleType) {
		if (obstacleType == "mineRock") {
			player.getActionSender().sendMessage(
					"...but fail to break-up the rock.");
		}
		if (obstacleType == "chopTendrils") {
			player.getActionSender().sendMessage(
					"You fail to cut through the tendrils.");
		}
		if (obstacleType == "crossGap") {
			player.getActionSender()
					.sendMessage(
							"You are not agile enough to squeeze through the narrow gap.");
		}
		if (obstacleType == "blockade") {
			player.getActionSender().sendMessage("You fail to set it on fire.");
		}
		if (obstacleType == "distractEye") {
			player.getActionSender().sendMessage(
					"You fail to distract the eyes.");
		}
	}

	public static void doFirstActions(Player player, String obstacleType) {
		if (obstacleType == "chopTendrils") {
			player.getActionSender().sendMessage(
					"You attempt to chop your way through...");
		} else if (obstacleType == "mineRock") {
			player.getActionSender().sendMessage(
					"You attempt to mine your way through...");
		} else if (obstacleType == "crossGap") {
			player.getActionSender().sendMessage(
					"You attempt to squeeze through the narrow gap...");
		} else if (obstacleType == "blockade") {
			player.getActionSender().sendMessage(
					"You attempt to set the blockade on fire...");
		} else if (obstacleType == "distractEye") {
			player.getActionSender().sendMessage(
					"You use your thieving skills to misdirect the eyes...");
		}
		doAnimation(player, obstacleType);
	}

	public static void doSecondaryActions(Player player, String obstacleType) {
		if (obstacleType == "chopTendrils") {
			player.getActionSender().sendMessage(
					"...and manage to chop down the tendrils.");
		} else if (obstacleType == "mineRock") {
			player.getActionSender().sendMessage(
					"...and manage to break through the rock.");
		} else if (obstacleType == "crossGap") {
			player.getActionSender().sendMessage(
					"...and you manage to crawl through.");
			player.getUpdateFlags().sendAnimation(1332);
		} else if (obstacleType == "blockade") {
			player.getActionSender().sendMessage(
					"...and manage to burn it down and get past.");
		} else if (obstacleType == "distractEye") {
			player.getActionSender().sendMessage(
					"...and sneak past while they're not looking.");
		}

	}

	private static void doAnimation(Player player, String obstacleType) {
		if (obstacleType == "mineRock") {
			player.getUpdateFlags().sendAnimation(
					Tools.getTool(player, Skill.MINING).getAnimation());
		} else if (obstacleType == "chopTendrils") {
			player.getUpdateFlags().sendAnimation(
					Tools.getTool(player, Skill.WOODCUTTING).getAnimation());
		} else if (obstacleType == "blockade") {
			player.getUpdateFlags().sendAnimation(733);
		} else if (obstacleType == "distractEye") {
			int[] emotes = { 855, 856, 857, 858, 859, 860, 861, 862, 863, 864,
					865, 866, 2113, 2109, 2111, 2106, 2107, 2108, 0x558, 2105,
					2110, 2112, 0x84F, 0x850, 1131, 1130, 1129, 1128, 1745,
					3544, 3543, 2836 };
			int index = new Random().nextInt(emotes.length);
			player.getUpdateFlags().sendAnimation(emotes[index]);
		} else if (obstacleType == "crossGap") {
			player.getUpdateFlags().sendAnimation(1331);
		}
	}

	public static void mineRock(Player player, int objectX, int objectY,
			int crossX, int crossY) {
		boolean canDoIt = true;
		if (Tools.getTool(player, Skill.MINING) == null) {
			player.getActionSender().sendMessage(
					"You need a pickaxe to mine here.");
			canDoIt = false;
		}
		mainObstacleAction(player, objectX, objectY, crossX, crossY,
				"mineRock", 7159, 7160, 2, canDoIt);

	}

	public static void chopTendrils(final Player player, int objectX,
			int objectY, int crossX, int crossY) {
		boolean canDoIt = true;
		if (Tools.getTool(player, Skill.WOODCUTTING) == null) {
			player.getActionSender().sendMessage(
					"You need an axe to chop these.");
			canDoIt = false;
		}
		mainObstacleAction(player, objectX, objectY, crossX, crossY,
				"chopTendrils", -1, 7163, 1, canDoIt);

	}

	public static void burnBoil(final Player player, int objectX, int objectY,
			int crossX, int crossY) {
		boolean canDoIt = true;
		if (!player.getInventory().playerHasItem(590)) {
			player.getActionSender().sendMessage(
					"You don't have a tinderbox to burn it.");
			canDoIt = false;
		}
		mainObstacleAction(player, objectX, objectY, crossX, crossY,
				"blockade", -1, 7167, 1, canDoIt);

	}

	public static void distractEye(final Player player, int objectX,
			int objectY, int crossX, int crossY) {
		boolean canDoIt = true;
		mainObstacleAction(player, objectX, objectY, crossX, crossY,
				"distractEye", 7169, 7170, 2, canDoIt);

	}

	public static void squeezeGap(final Player player, int objectX,
			int objectY, int crossX, int crossY) {
		boolean canDoIt = true;
		mainObstacleAction(player, objectX, objectY, crossX, crossY,
				"crossGap", -1, -1, 0, canDoIt);

	}

	public static void handlePassage(final Player player) {
		player.teleport(new Position(3040, 4844, player.getPosition().getZ()));
	}

	public static void teleportToAbyss(Player player, Npc npc) {
		final int[] abyssX = { 3016, 3016, 3021, 3039, 3058, 3063, 3058, 3034 };
		final int[] abyssY = { 4848, 4831, 4815, 4806, 4812, 4827, 4848, 4854 };
		final int loc1 = new Random().nextInt(abyssX.length);
		player.setRunecraftNpc(npc.getNpcId());
		npc.teleportPlayer(player, abyssX[loc1], abyssY[loc1], 0,
				"Veniens! Sallakar! Rinnesset!");
		player.addSkull(player, SkullRecord.ABYSS_EXPIRE_TIME);
		//player.setSkullTimer(600); // 5 Minutes
		if (player.getSkill().getLevel()[Skill.PRAYER] > 0) {
			player.getSkill().getLevel()[Skill.PRAYER] = 0;
			player.getSkill().refresh(Skill.PRAYER);
			player.getActionSender().sendMessage("You feel your prayer drain.");
		}
	}
}
