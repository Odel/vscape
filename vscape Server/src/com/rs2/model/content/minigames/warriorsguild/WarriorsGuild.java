package com.rs2.model.content.minigames.WarriorsGuild;

import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.container.inventory.Inventory;
import com.rs2.model.players.item.Item;
import com.rs2.task.TaskScheduler;
import com.rs2.task.Task;
import com.rs2.util.Misc;

public class WarriorsGuild {
	private static int TOKEN_TIME = 0;
	
	private static boolean gameActive = false;
	
	private static final Position ENTRANCE = new Position(2605,3153,0);
	private static final Position EXIT = new Position(2607,3151,0);
	private static final Position DC_EXIT = new Position(2610, 3148, 0);
	private static final MinigameAreas.Area NORTH_CAGES = new MinigameAreas.Area(new Position(2614, 3158, 0), new Position(2616, 3170, 0));
	private static final MinigameAreas.Area WEST_CAGES = new MinigameAreas.Area(new Position(2586, 3142, 0), new Position(2601, 3144, 0));

	private static void think(final Player player) {
		new TaskScheduler().schedule(new Task(8, false) {
		    @Override
		    protected void execute() {
			if(!player.inWarriorGuildArena()) {
			    this.stop();
			}
			Inventory inventory = player.getInventory();
			if (inventory.playerHasItem(8851, 10) && TOKEN_TIME > 0) {
			    TOKEN_TIME -= 5;
			if(TOKEN_TIME%60 == 0)
			    inventory.removeItem(new Item(8851, 10));
			}
			if(!inventory.playerHasItem(8851, 10)) {
			    exitArena(player);
			    player.getActionSender().sendMessage("You have run out of tokens!");
			    this.stop();
			}
		    }
		});
	}
	
	public static boolean handleObjectClicking(Player player, int objectId, int x, int y) 
	{
		switch(objectId)
		{
			case 81: //entrance doors
			    int req = player.getSkill().getPlayerLevel(0) + player.getSkill().getPlayerLevel(2);
			    if(x == 2617 && y == 3171) {
				if(req >= 130) {
				    player.getActionSender().walkTo(0, player.getPosition().getY() > 3171 ? -1 : 1, true);
				    player.getActionSender().walkThroughDoor(81, x, y, 0);
				}
				else {
				    player.getDialogue().sendStatement("You need a combined Attack and Strength level of 130 to enter", "the Warriors' Guild.");
				}
				return true;
			    }
			    else if(x == 2585 && y == 3141) {
				if(req >= 130) {
				    player.getActionSender().walkTo(player.getPosition().getX() < 2585 ? 1 : -1, 0, true);
				    player.getActionSender().walkThroughDoor(81, x, y, 0);
				}
				else {
				    player.getDialogue().sendStatement("You need a combined Attack and Strength level of 130 to enter", "the Warriors' Guild.");
				}
				return true;
			    }
			    else
				break;
			case 82: //main door to arena
				if(player.getPosition().getX() >= 2606 && player.getPosition().getY() <= 3152) {
				    if(player.getInventory().playerHasItem(8851, 100)) {
					TOKEN_TIME = (player.getInventory().getItemAmount(8851)/10) * 60;
					player.getActionSender().sendMessage("You have " + TOKEN_TIME / 60 + " minutes in the arena.");
					player.teleport(ENTRANCE);
					think(player);
				    }
				    else {
					player.getDialogue().sendStatement("You need atleast 100 tokens to spend time in the arena.");
				    }
				    return true;
				}
				else if(player.getPosition().getX() <= 2606 && player.getPosition().getY() >= 3151) {
				    exitArena(player);
				    return true;
				}
				else
				    break;
			case 79:
			case 80:
			    //north cells
			    if(x == 2617 && y == 3163) {
				if(player.getPosition().getY() != 3163) {
				    player.getActionSender().walkTo(player.getPosition().getX() < 2617 ? 1 : -1, player.getPosition().getY() < 3633 ? 1 : -1, true);
				    player.getActionSender().walkThroughDoor(79, x, y, 0);
				}
				else if(player.getPosition().getY() == 3163) {
				    player.getActionSender().walkTo(player.getPosition().getX() < 2617 ? 1 : -1, 0, true);
				    player.getActionSender().walkThroughDoor(79, x, y, 0);
				}
				return true;
			    }
			    else if(x == 2617 && y == 3167) {
				if(player.getPosition().getY() != 3167) {
				    player.getActionSender().walkTo(player.getPosition().getX() < 2617 ? 1 : -1, player.getPosition().getY() < 3167 ? 1 : -1, true);
				    player.getActionSender().walkThroughDoor(80, x, y, 0);
				}
				else if(player.getPosition().getY() == 3167) {
				    player.getActionSender().walkTo(player.getPosition().getX() < 2617 ? 1 : -1, 0, true);
				    player.getActionSender().walkThroughDoor(80, x, y, 0);
				}
				return true;
			    }
			    else if(x == 2617 && y == 3159) {
				if(player.getPosition().getY() != 3159) {
				    player.getActionSender().walkTo(player.getPosition().getX() < 2617 ? 1 : -1, player.getPosition().getY() < 3159 ? 1 : -1, true);
				    player.getActionSender().walkThroughDoor(79, x, y, 0);
				}
				else if(player.getPosition().getY() == 3159) {
				    player.getActionSender().walkTo(player.getPosition().getX() < 2617 ? 1 : -1, 0, true);
				    player.getActionSender().walkThroughDoor(79, x, y, 0);
				}
				return true;
			    }
			    //west cells
			    else if(x == 2600 && y == 3142) {
				if(player.getPosition().getX() != 2600) {
				    player.getActionSender().walkTo(player.getPosition().getX() < 2600 ? 1 : -1, player.getPosition().getY() < 3142 ? 1 : -1, true);
				    player.getActionSender().walkThroughDoor(79, x, y, 0);
				}
				else {
				    player.getActionSender().walkTo(0, player.getPosition().getY() < 3142 ? 1 : -1, true);
				    player.getActionSender().walkThroughDoor(79, x, y, 0);
				}
				return true;
			    }
			    else if(x == 2595 && y == 3142) {
				if(player.getPosition().getX() != 2595) {
				    player.getActionSender().walkTo(player.getPosition().getX() < 2595 ? 1 : -1, player.getPosition().getY() < 3142 ? 1 : -1, true);
				    player.getActionSender().walkThroughDoor(79, x, y, 0);
				}
				else {
				    player.getActionSender().walkTo(0, player.getPosition().getY() < 3142 ? 1 : -1, true);
				    player.getActionSender().walkThroughDoor(79, x, y, 0);
				}
				return true;
			    }
			    else if(x == 2589 && y == 3142) {
				if(player.getPosition().getX() != 2589) {
				    player.getActionSender().walkTo(player.getPosition().getX() < 2589 ? 1 : -1, player.getPosition().getY() < 3142 ? 1 : -1, true);
				    player.getActionSender().walkThroughDoor(79, x, y, 0);
				}
				else {
				    player.getActionSender().walkTo(0, player.getPosition().getY() < 3142 ? 1 : -1, true);
				    player.getActionSender().walkThroughDoor(79, x, y, 0);
				}
				return true;
			    }
			    else
				break;
		}
		return false;
	}
	public static boolean itemHandling(Player player, int itemId) {
	    if(player.inWarriorGuildCagesNorth() || player.inWarriorGuildCagesWest()) {
		if(itemId == 1155 || itemId == 1117 || itemId == 1075) { //bronze 4278
		    if(fullArmorSetCheck(player, 1)) {
			player.getActionSender().sendMessage("Your set of armor comes to life!");
			player.getInventory().removeItem(new Item(1155));
			player.getInventory().removeItem(new Item(1117));
			player.getInventory().removeItem(new Item(1075));
			spawnArmor(player, 4278);
		    }
		    else
			player.getActionSender().sendMessage("You need a set of matching full helm, platebody, and platelegs.");
		    return true;
		}
		else if(itemId == 1153 || itemId == 1115 || itemId == 1067) { //iron 4279
		    if(fullArmorSetCheck(player, 2)) {
			player.getActionSender().sendMessage("Your set of armor comes to life!");
			player.getInventory().removeItem(new Item(1153));
			player.getInventory().removeItem(new Item(1115));
			player.getInventory().removeItem(new Item(1067));
			spawnArmor(player, 4279);
		    }
		    else
			player.getActionSender().sendMessage("You need a set of matching full helm, platebody, and platelegs.");
		    return true;
		}
		else if(itemId == 1157 || itemId == 1119 || itemId == 1069) {//steel 4280
		    if(fullArmorSetCheck(player, 3)) {
			player.getActionSender().sendMessage("Your set of armor comes to life!");
			player.getInventory().removeItem(new Item(1157));
			player.getInventory().removeItem(new Item(1119));
			player.getInventory().removeItem(new Item(1069));
			spawnArmor(player, 4280);
		    }
		    else
			player.getActionSender().sendMessage("You need a set of matching full helm, platebody, and platelegs.");
		    return true;
		}
		else if(itemId == 1165 || itemId == 1125 || itemId == 1077) {//black 4281
		    if(fullArmorSetCheck(player, 4)) {
			player.getActionSender().sendMessage("Your set of armor comes to life!");
			player.getInventory().removeItem(new Item(1165));
			player.getInventory().removeItem(new Item(1125));
			player.getInventory().removeItem(new Item(1077));
			spawnArmor(player, 4281);
		    }
		    else
			player.getActionSender().sendMessage("You need a set of matching full helm, platebody, and platelegs.");
		    return true;
		}
		else if(itemId == 1159 || itemId == 1121 || itemId == 1071) {//mith 4282
		    if(fullArmorSetCheck(player, 5)) {
			player.getActionSender().sendMessage("Your set of armor comes to life!");
			player.getInventory().removeItem(new Item(1159));
			player.getInventory().removeItem(new Item(1121));
			player.getInventory().removeItem(new Item(1071));
			spawnArmor(player, 4282);
		    }
		    else
			player.getActionSender().sendMessage("You need a set of matching full helm, platebody, and platelegs.");
		    return true;
		}
		else if(itemId == 1161 || itemId == 1123 || itemId == 1073) {//addy 4283
		    if(fullArmorSetCheck(player, 6)) {
			player.getActionSender().sendMessage("Your set of armor comes to life!");
			player.getInventory().removeItem(new Item(1161));
			player.getInventory().removeItem(new Item(1123));
			player.getInventory().removeItem(new Item(1073));
			spawnArmor(player, 4283);
		    }
		    else
			player.getActionSender().sendMessage("You need a set of matching full helm, platebody, and platelegs.");
		    return true;
		}
		else if(itemId == 1163 || itemId == 1127 || itemId == 1079) {//rune 4284
		    if(fullArmorSetCheck(player, 7)) {
			player.getActionSender().sendMessage("Your set of armor comes to life!");
			player.getInventory().removeItem(new Item(1163));
			player.getInventory().removeItem(new Item(1127));
			player.getInventory().removeItem(new Item(1079));
			spawnArmor(player, 4284);
		    }
		    else
			player.getActionSender().sendMessage("You need a set of matching full helm, platebody, and platelegs.");
		    return true;
		}
	    }
	    return false;
	}
	public static boolean fullArmorSetCheck(Player player, int set) {
	    Inventory inventory = player.getInventory();
	    if(inventory.playerHasItem(1155) && inventory.playerHasItem(1117) && inventory.playerHasItem(1075)&& set == 1) //bronze 1
		return true;
	    else if(inventory.playerHasItem(1153) && inventory.playerHasItem(1115) && inventory.playerHasItem(1067)&& set == 2) //iron 2
		return true;
	    else if(inventory.playerHasItem(1157) && inventory.playerHasItem(1119) && inventory.playerHasItem(1069)&& set == 3) //steel 3
		return true;
	    else if(inventory.playerHasItem(1165) && inventory.playerHasItem(1125) && inventory.playerHasItem(1077) && set == 4) //black 4
		return true;
	    else if(inventory.playerHasItem(1159) && inventory.playerHasItem(1121) && inventory.playerHasItem(1071)&& set == 5) //mith 5
		return true;
	    else if(inventory.playerHasItem(1161) && inventory.playerHasItem(1123) && inventory.playerHasItem(1073)&& set == 6) //addy 6
		return true;
	    else if(inventory.playerHasItem(1163) && inventory.playerHasItem(1127) && inventory.playerHasItem(1079) && set == 7) //rune 7
		return true;
	    else
		return false;
	}
	
	public static void spawnArmor(Player player, int id) {
		Npc npc = new Npc(id);
		npc.setPosition(new Position(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ()));
		npc.setSpawnPosition(new Position(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ()));
		World.register(npc);      
		npc.setFollowingEntity(player);
                npc.setPlayerOwner(player.getIndex());
		CombatManager.attack(npc, player);
		npc.setNeedsRespawn(false);
	}
	
	public static int getTokenAmount(Npc died) {
	    if(died.getNpcId() == 4278) return 5;
	    else if(died.getNpcId() == 4279) return 10;
	    else if(died.getNpcId() == 4280) return 15;
	    else if(died.getNpcId() == 4281) return 20;
	    else if(died.getNpcId() == 4282) return 25;
	    else if(died.getNpcId() == 4283) return 30;
	    else if(died.getNpcId() == 4284) return 40;
	    else return 0;
	}
	
	public static boolean isAnimatedArmor(Npc died) {
	    if(died.getNpcId() == 4278) return true;
	    else if(died.getNpcId() == 4279) return true;
	    else if(died.getNpcId() == 4280) return true;
	    else if(died.getNpcId() == 4281) return true;
	    else if(died.getNpcId() == 4282) return true;
	    else if(died.getNpcId() == 4283) return true;
	    else if(died.getNpcId() == 4284) return true;
	    else return false;
	}
	
	public static void dropDefender(Player player, Npc npc) {
		if (Misc.random(5) != 1) {
			return;
		}
		if (npc.getNpcId() == 4291 || npc.getNpcId() == 4292 ) {
		    GroundItem drop = new GroundItem(new Item(player.getDefender()), player,  new Position(npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ()));
		    GroundItemManager.getManager().dropItem(drop);
		}
	
	}
	public static void exitArena(Player player) {
	    if(player.getInventory().ownsItem(player.getDefender())) {
		if(player.getDefender() != 8850) { //roon
		    player.setDefender(player.getDefender()+1);
		}
	    }
	    player.teleport(DC_EXIT);
	}
	
}
