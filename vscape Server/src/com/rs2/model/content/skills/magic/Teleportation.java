package com.rs2.model.content.skills.magic;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
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
	public static final Position WHITE_KNIGHTS_CASTLE = new Position(2973, 3344, 0);
	public static final Position EDGEVILLE = new Position(3087, 3495);
	public static final Position KARAMJA = new Position(2912, 3170);
	public static final Position DRAYNOR_VILLAGE = new Position(3104, 3249);
	public static final Position AL_KHARID = new Position(3293, 3162);
	public static final Position CASTLE_WARS = new Position(2441, 3090);
	public static final Position DUEL_ARENA = new Position(3317, 3233);
	public static final Position GAMES_ROOM = new Position(2207, 4941);
	public static final Position PEST_CONTROL = new Position(2658, 2660);
	
	public static final Position BURTHORPE = new Position(2893, 3535);
	public static final Position CHAMPGUILD = new Position(3191, 3364);
	public static final Position MONASTERY = new Position(3051, 3490);
	public static final Position RANGEGUILD = new Position(2656, 3440);
	
	public static final Position FISHING_GUILD = new Position(2611, 3392);
	public static final Position MINING_GUILD = new Position(3016, 3339);
	public static final Position CRAFTING_GUILD = new Position(2933, 3292);
	public static final Position COOKING_GUILD = new Position(3143, 3442);
	
	public int x, y, height, teleTimer;

	public boolean canTeleport() {
		if(player.inFightCaves()) {
		    player.getActionSender().sendMessage("You can't teleport here.");
		    return false;
		}
		if (player.getMMVars().isMonkey()) {
		    player.getActionSender().sendMessage("You cannot teleport while disguised as a monkey.");
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
		if (player.getInventory().playerHasItem(new Item(431))) {
			player.getActionSender().sendMessage("You cannot teleport with Karamjan Rum, it will break.");
			return false;
		}
		if (player.getInJail()){
			player.getActionSender().sendMessage("You cannot teleport while in jail.");
			return false;
		}
		if (player.isHomeTeleporting()) {
			player.getActionSender().sendMessage("You can't teleport while teleporting home.");
			return false;
		}
		return true;
	}
	
	public boolean attemptTeleport(Position pos) {
		if (player.inWild() && player.getWildernessLevel() > 20) {
			player.getActionSender().sendMessage("You can't teleport above level 20 in the wilderness.");
			return false;
		}
		if(!canTeleport()) {
			return false;
		}
		if (player.getQuestStage(36) >= 14 && player.getInventory().playerHasItem(4033)) {
			player.getActionSender().sendMessage("Your monkey flees your backpack in panic after teleporting!");
			player.getInventory().removeItem(new Item(4033));
		}
		teleport(pos.getX(), pos.getY(), pos.getZ(), player.getMagicBookType() == SpellBook.MODERN);
		return true;
	}

	public boolean attemptTeleportJewellery(Position pos) {
		if (player.inWild() && player.getWildernessLevel() > 30) {
			player.getActionSender().sendMessage("You can't teleport above level 30 in the wilderness.");
			return false;
		}
		if(!canTeleport()) {
			return false;
		}
		if (player.getQuestStage(36) >= 14 && player.getInventory().playerHasItem(4033)) {
			player.getActionSender().sendMessage("Your monkey flees your backpack in panic after teleporting!");
			player.getInventory().removeItem(new Item(4033));
		}
		player.getUpdateFlags().sendAnimation(714);
		player.getUpdateFlags().sendHighGraphic(301);
		teleport(pos.getX(), pos.getY(), pos.getZ(), true);
		return true;
	}
	
	public boolean attemptEctophialTeleport(Position pos) {
	    if (player.inWild() && player.getWildernessLevel() > 20) {
		player.getActionSender().sendMessage("You can't teleport above level 20 in the wilderness.");
		return false;
	    }
	    if(!canTeleport()) {
		return false;
	    }
	    if (player.getQuestStage(36) >= 14 && player.getInventory().playerHasItem(4033)) {
		player.getActionSender().sendMessage("Your monkey flees your backpack in panic after teleporting!");
		player.getInventory().removeItem(new Item(4033));
	    }
	    return true;
	}

	public boolean attemptTeleportTablet(Position pos) {
		if (player.inWild() && player.getWildernessLevel() > 20) {
			player.getActionSender().sendMessage("You can't teleport above level 20 in the wilderness.");
			return false;
		}
		if(!canTeleport()) {
			return false;
		}
		if (player.getQuestStage(36) >= 14 && player.getInventory().playerHasItem(4033)) {
			player.getActionSender().sendMessage("Your monkey flees your backpack in panic after teleporting!");
			player.getInventory().removeItem(new Item(4033));
		}
		teleport(pos.getX(), pos.getY(), pos.getZ(), false);
		return true;
	}
	
	public void attemptHomeTeleport(Position pos) {
		if(player.isAttacking() || !player.getInCombatTick().completed()){
			player.getActionSender().sendMessage("You can't teleport while in combat!");
			return;
		}
		if (player.inWild() && player.getWildernessLevel() > 20) {
			player.getActionSender().sendMessage("You can't teleport above level 20 in the wilderness.");
			return;
		}
		if(!canTeleport()) {
		    return;
		}
		if (player.getQuestStage(36) >= 14 && player.getInventory().playerHasItem(4033)) {
			player.getActionSender().sendMessage("Your monkey flees your backpack in panic after teleporting!");
			player.getInventory().removeItem(new Item(4033));
		}
	    teleportHome(pos.getX(), pos.getY(), pos.getZ());
	}
	
	public void teleportHome(final int x, final int y, final int height) {
        if(player.isHomeTeleporting()){
        	return;
		}
        player.setHomeTeleporting(true);
		final int task = player.getTask();
        player.setSkilling(new CycleEvent() {
        	int teleTimer = 0;
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task) || !player.isHomeTeleporting()) {
					container.stop();
					return;
				}
                if (!player.isDead()) {
		            if (teleTimer == 0) {
	                    player.getUpdateFlags().sendAnimation(4850);
		            } else if (teleTimer == 3) {
	                    player.getUpdateFlags().sendAnimation(4853);
	                    player.getUpdateFlags().sendGraphic(802);
		            } else if (teleTimer == 6) {
	                    player.getUpdateFlags().sendAnimation(4855);
	                    player.getUpdateFlags().sendGraphic(803);
		            } else if (teleTimer == 9) {
	                    player.getUpdateFlags().sendAnimation(4857);
	                    player.getUpdateFlags().sendGraphic(804);
	                    player.setStopPacket(true);
		            } else if (teleTimer == 11) {
	                    player.teleport(new Position(x, y, height));
	                    container.stop();
		            }
                } else {
					teleTimer = 0;
					container.stop();
				}
	            teleTimer++;
			}
			@Override
			public void stop() {
				player.setHomeTeleporting(false);
			}
		});
        CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 1);
	}
	
	public void teleport(final int x, final int y, final int height, final boolean modern) {
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
	    if (!canTeleport()) {
		return;
	    }
	    if (player.getQuestStage(36) >= 14 && player.getInventory().playerHasItem(4033)) {
		player.getActionSender().sendMessage("Your monkey flees your backpack in panic after teleporting!");
		player.getInventory().removeItem(new Item(4033));
	    }
	    if (graphic) {
		player.getUpdateFlags().sendHighGraphic(342);
	    }
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
			    if (message != null) {
				player.getActionSender().sendMessage(message);
			    }
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
	    if (!canTeleport()) {
		return;
	    }
	    if (player.getQuestStage(36) >= 14 && player.getInventory().playerHasItem(4033)) {
		player.getActionSender().sendMessage("Your monkey flees your backpack in panic after teleporting!");
		player.getInventory().removeItem(new Item(4033));
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
