package com.rs2.model.content.minigames.castlewars;

import com.rs2.model.Position;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class CastlewarsObjects {

    public static void handleObject(Player player, int id, int x, int y) {
    	//TODO this is a silly way to do this as it only checks if players are on the ground floor (0 level)
    	//change this to check a player variable boolean
    	if(!Castlewars.ENABLED)
    	{
    		player.getActionSender().sendMessage("This content is disabled.");
    		return;
    	}
        if (!Castlewars.inCwArea(player)) {
            //player.getActionSender().sendMessage("You gotta be in castle wars before you can use these objects");
            //return;
        }
        switch (id) {
        	case 4408: //guthix portal
        		player.getActionSender().sendMessage("Not implemented yet.");
        	break;
        	case 4388: //zammy portal
        		Castlewars.sendPlayerToLobby(player, "zamorak");
                player.cwplayer = new CastlewarsPlayer(2,true);
        	break;
        	case 4387: //sara portal
        		Castlewars.sendPlayerToLobby(player, "saradomin");
                player.cwplayer = new CastlewarsPlayer(1,true);
        	break;
        	case 4390:
            	System.out.println("Player leaving CW zammy lobby");
                Castlewars.removePlayerFromCw(player);
                player.cwplayer = null;
        	break;
        	case 4389:
            	System.out.println("Player leaving CW sara lobby");
                Castlewars.removePlayerFromCw(player);
                player.cwplayer = null;
        	break;
            case 4469:
                if (player.cwplayer.getTeamNumber() == 2) {
                    player.getActionSender().sendMessage("You are not allowed in the other teams spawn point.");
                    break;
                }
                if (x == 2426) {
                    if (player.getPosition().getX() == 3080) {
                        player.teleport(new Position(2426, 3081, player.getPosition().getZ()));
                    } else if (player.getPosition().getY() == 3081) {
                        player.teleport(new Position(2426, 3080, player.getPosition().getZ()));
                    }
                } else if (x == 2422) {
                    if (player.getPosition().getX() == 2422) {
                        player.teleport(new Position(2423, 3076, player.getPosition().getZ()));
                    } else if (player.getPosition().getX() == 2423) {
                        player.teleport(new Position(2422, 3076, player.getPosition().getZ()));
                    }
                }
                break;
            case 4470:
                if (player.cwplayer.getTeamNumber() == 1) {
                    player.getActionSender().sendMessage("You are not allowed in the other teams spawn point.");
                    break;
                }
                if (x == 2373 && y == 3126) {
                    if (player.getPosition().getY() == 3126) {
                        player.teleport(new Position(2373, 3127, 1));
                    } else if (player.getPosition().getY() == 3127) {
                        player.teleport(new Position(2373, 3126, 1));
                    }
                } else if (x == 2377 && y == 3131) {
                    if (player.getPosition().getX() == 2376) {
                        player.teleport(new Position(2377, 3131, 1));
                    } else if (player.getPosition().getX() == 2377) {
                        player.teleport(new Position(2376, 3131, 1));
    
                    }
                }
                break;
            case 4417:
                if (x == 2428 && y == 3081 && player.getPosition().getZ() == 1) {
                    player.teleport(new Position(2430, 3080, 2));
                }
                if (x == 2425 && y == 3074 && player.getPosition().getZ() == 2) {
                    player.teleport(new Position(2426, 3074, 3));
                }
                if (x == 2419 && y == 3078 && player.getPosition().getZ() == 0) {
                    player.teleport(new Position(2420, 3080, 1));
                }
                break;
            case 4415:
                if (x == 2419 && y == 3080 && player.getPosition().getZ() == 1) {
                    player.teleport(new Position(2419, 3077, 0));
                }
                if (x == 2430 && y == 3081 && player.getPosition().getZ() == 2) {
                    player.teleport(new Position(2427, 3081, 1));
                }
                if (x == 2425 && y == 3074 && player.getPosition().getZ() == 3) {
                    player.teleport(new Position(2425, 3077, 2));
                }
                if (x == 2374 && y == 3133 && player.getPosition().getZ() == 3) {
                    player.teleport(new Position(2374, 3130, 2));
                }
                if (x == 2369 && y == 3126 && player.getPosition().getZ() == 2) {
                    player.teleport(new Position(2372, 3126, 1));
                }
                if (x == 2380 && y == 3127 && player.getPosition().getZ() == 1) {
                    player.teleport(new Position(2380, 3130, 0));
                }
                break;
            case 4411:
                if (x == 2421 && y == 3073 && player.getPosition().getZ() == 1) {
                    player.teleport(new Position(player.getPosition().getX(), player.getPosition().getY(), 0));
                }
                break;
            case 4419:
                if (x == 2417 && y == 3074 && player.getPosition().getZ() == 0) {
                    player.teleport(new Position(2416, 3074, 0));
                }
                if (x == 2417 && y == 3074 && player.getPosition().getZ() == 1) {
                    player.teleport(new Position(2417, 3073, 0));
                }
                break;
            case 4911:
                if (x == 2421 && y == 3073 && player.getPosition().getZ() == 1) {
                    player.teleport(new Position(2421, 3074, 0));
                }
                if (x == 2378 && y == 3134 && player.getPosition().getZ() == 1) {
                    player.teleport(new Position(2378, 3133, 0));
                }
                break;
            case 1747:
                if (x == 2421 && y == 3073 && player.getPosition().getZ() == 0) {
                    player.teleport(new Position(2421, 3074, 1));
                }
                if (x == 2378 && y == 3134 && player.getPosition().getZ() == 0) {
                    player.teleport(new Position(2378, 3133, 1));
                }
                break;
            case 4912:
                if (x == 2430 && y == 3082 && player.getPosition().getZ() == 0) {
                    player.teleport(new Position(player.getPosition().getX(), player.getPosition().getY() + 6400, 0));
                }
                if (x == 2369 && y == 3125 && player.getPosition().getZ() == 0) {
                    player.teleport(new Position(player.getPosition().getX(), player.getPosition().getY() + 6400, 0));
                }
                break;
            case 1757:
                if (x == 2430 && y == 9482) {
                    player.teleport(new Position(2430, 3081, 0));
                } else {
                    player.teleport(new Position(2369, 3126, 0));
                }
                break;

            case 4418:
                if (x == 2380 && y == 3127 && player.getPosition().getZ() == 0) {
                    player.teleport(new Position(2379, 3127, 1));
                }
                if (x == 2369 && y == 3126 && player.getPosition().getZ() == 1) {
                    player.teleport(new Position(2369, 3127, 2));
                }
                if (x == 2374 && y == 3131 && player.getPosition().getZ() == 2) {
                    player.teleport(new Position(2373, 3133, 3));
                }
                break;
            case 4420:
                if (x == 2382 && y == 3131 && player.getPosition().getZ() == 0) {
                    if (player.getPosition().getX() >= 2383 && player.getPosition().getX() <= 2385) {
                        player.teleport(new Position(2382, 3130, 0));
                    } else {
                        player.teleport(new Position(2383, 3133, 0));
					}
				}
                break;
            case 4406:
            case 4407:
            	System.out.println("Player leaving cw");
                Castlewars.removePlayerFromCw(player);
                break;
            case 4458:
            	player.getUpdateFlags().sendAnimation(881);
            	player.getInventory().addItem(new Item(4049,1));
                player.getActionSender().sendMessage("You get some bandages");
                break;
            case 4902: //sara flag
            case 4377:
                switch (player.cwplayer.getTeamNumber()) {
                    case 1:
                        //CastleWars.returnFlag(c, player.playerEquipment[player.playerWeapon]);
                        break;
                    case 2:
                        //CastleWars.captureFlag(c);
                        break;
                }
                break;
            case 4903: //zammy flag
            case 4378:
                switch (player.cwplayer.getTeamNumber()) {
                    case 1:
                        //CastleWars.captureFlag(c);
                        break;
                    case 2:
                        //CastleWars.returnFlag(c, player.playerEquipment[player.playerWeapon]);
                        break;
                }
                break;
            case 4461: //barricades
                player.getActionSender().sendMessage("You get a barricade!");
                player.getInventory().addItem(new Item(4053,1));
                break;
            case 4463: // explosive potion!
                player.getActionSender().sendMessage("You get an explosive potion!");
                player.getInventory().addItem(new Item(4045,1));
                break;
            case 4464: //pickaxe table
                player.getActionSender().sendMessage("You get a bronzen pickaxe for mining.");
                player.getInventory().addItem(new Item(1265,1));
                break;
            case 4900:
            case 4901:
                //CastleWars.pickupFlag(c);
            break;
            default:
                break;

        }
    }
}
