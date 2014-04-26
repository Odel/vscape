package com.rs2.model.content.skills.magic;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

/**
 * By Mikey` of Rune-Server
 */

public class Teleportation {

	private Player player;

	public Teleportation(Player player) {
		this.player = player;
	}

	public static final Position HOME = new Position(Constants.LUMBRIDGE_X, Constants.LUMBRIDGE_Y, 0);

	public static final Position EDGEVILLE = new Position(3087, 3495);
	public static final Position KARAMJA = new Position(2912, 3170);
	public static final Position DRAYNOR_VILLAGE = new Position(3104, 3249);
	public static final Position AL_KHARID = new Position(3293, 3162);
	public static final Position CASTLE_WARS = new Position(2441, 3090);
	public static final Position DUEL_ARENA = new Position(3317, 3233);
	public static final Position GAMES_ROOM = new Position(2207, 4941);
	
	public int x, y, height, teleTimer;

	public boolean attemptTeleport(Position pos) {
		if (player.inWild() && player.getWildernessLevel() > 20) {
			player.getActionSender().sendMessage("You can't teleport above level 20 in the wilderness.");
			return false;
		}
		if (player.isTeleblocked()) {
			player.getActionSender().sendMessage("A magical force prevents you from teleporting.");
			return false;
		}
		if (player.cantTeleport()) {
			player.getActionSender().sendMessage("You can't teleport from here.");
			return false;
		}
		teleport(pos.getX(), pos.getY(), pos.getZ(), player.getMagicBookType() == SpellBook.MODERN);
		return true;
	}

	public boolean attemptTeleportJewellery(Position pos) {
		if (player.inWild() && player.getWildernessLevel() > 30) {
			player.getActionSender().sendMessage("You can't teleport above level 30 in the wilderness.");
			return false;
		}
		if (player.isTeleblocked()) {
			player.getActionSender().sendMessage("A magical force prevents you from teleporting.");
			return false;
		}
		if (player.cantTeleport()) {
			player.getActionSender().sendMessage("You can't teleport from here.");
			return false;
		}
		player.getUpdateFlags().sendAnimation(714);
		player.getUpdateFlags().sendHighGraphic(301);
		teleport(pos.getX(), pos.getY(), pos.getZ(), true);
		return true;
	}

	public void teleport(final int x, final int y, final int height, final boolean modern) {
        if (player.isTeleblocked() || player.cantTeleport()) {
            player.getActionSender().sendMessage("You can't teleport out of here!");
            return;
        }
		player.setStopPacket(true);
		player.getAttributes().put("canTakeDamage", Boolean.FALSE);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int teleTimer = 6;
			@Override
			public void execute(CycleEventContainer container) {
				teleTimer--;
				if (!player.isDead()) {
					if (teleTimer == 2) {
						if (modern) {
							player.getUpdateFlags().sendAnimation(715);
						}
						player.teleport(new Position(x, y, height));
					}
				} else {
					teleTimer = 0;
				}
				if (teleTimer < 1) {
					container.stop();
				}
			}
			@Override
			public void stop() {
				player.setStopPacket(false);
				player.getAttributes().put("canTakeDamage", Boolean.TRUE);
			}
		}, 1);
	}

    public void teleportObelisk(Position position) {
        teleportObelisk(position.getX(), position.getY(), position.getZ(), true, null);
    }

    public void teleportObelisk(final int x, final int y, final int height) {
        teleportObelisk(x, y, height, true, null);
    }

	public void teleportObelisk(final int x, final int y, final int height, boolean graphic, final String message) {
        if (player.isTeleblocked()) {
            player.getActionSender().sendMessage("A magical force prevents you from teleporting.");
            return;
        }
		if (player.cantTeleport()) {
			player.getActionSender().sendMessage("You can't teleport from here.");
			return;
		}
        if (graphic)
            player.getUpdateFlags().sendHighGraphic(342);
		player.getUpdateFlags().sendAnimation(1816);
		player.setStopPacket(true);
		player.getAttributes().put("canTakeDamage", Boolean.FALSE);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int teleTimer = 6;
			@Override
			public void execute(CycleEventContainer container) {
				teleTimer--;
				if (!player.isDead()) {
                    if (teleTimer == 3) {
                        player.getUpdateFlags().sendAnimation(715);
                        player.teleport(new Position(x, y, height));
                        if (message != null)
                            player.getActionSender().sendMessage(message);
                    }
                } else {
                    teleTimer = 0;
                }
                if (teleTimer < 1) {
                    container.stop();
                }
			}
			@Override
			public void stop() {
				player.setStopPacket(false);
				player.getAttributes().put("canTakeDamage", Boolean.TRUE);
			}
		}, 1);
	}

	public void teleportRunecraft(final int x, final int y, final int height) {
        if (player.isTeleblocked() || player.cantTeleport()) {
            player.getActionSender().sendMessage("You can't teleport out of here!");
            return;
        }
		player.getUpdateFlags().sendHighGraphic(110);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int teleTimer = 6;
			@Override
			public void execute(CycleEventContainer container) {
				teleTimer--;
				if (!player.isDead()) {
					if (teleTimer == 3) {
						player.teleport(new Position(x, y, height));
					}
				} else {
					teleTimer = 0;
				}
				if (teleTimer < 1) {
					container.stop();
				}
			}
			@Override
			public void stop() {
				player.setStopPacket(false);
				player.getAttributes().put("canTakeDamage", Boolean.TRUE);
			}
		}, 1);
	}

}
