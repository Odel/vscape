package com.rs2.model.content.minigames.castlewars;

import com.rs2.model.Position;
import com.rs2.model.content.minigames.castlewars.Castlewars.CaveWallData;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.Tools;
import com.rs2.model.content.skills.Tools.Tool;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class CastlewarsObjects {

	public static boolean handleItemOnObject(Player player, int itemId, int itemSlot, int id, int x, int y, int z) {
		switch (itemId) {
			case 4045 :
            	if(id == 4437)
            	{
            		final CaveWallData caveWall = CaveWallData.forRockPosition(new Position(x,y,z));
	        		if(caveWall != null)
	        		{
	        			if(Castlewars.getCollapsedCave(caveWall.index))
	        			{
	        				if(!player.getInventory().removeItemSlot(new Item(4045,1), itemSlot)){
	        					player.getInventory().removeItem(new Item(4045,1));
	        				}
	        				Castlewars.removeCollapse(caveWall, id, x, y, z);
	        				return true;
	        			}
	        		}
            	}
            	if(id == 4448)
            	{
	        		final CaveWallData caveWall = CaveWallData.forWallPosition(new Position(x,y,z));
	        		if(caveWall != null)
	        		{
	        			if(!Castlewars.getCollapsedCave(caveWall.index))
	        			{
	        				if(!player.getInventory().removeItemSlot(new Item(4045,1), itemSlot)){
	        					player.getInventory().removeItem(new Item(4045,1));
	        				}
	        				Castlewars.collapseRock(caveWall);
	        				return true;
	        			}else{
	        				player.getActionSender().sendMessage("This rock wall has already collapsed.");
	        				return true;
	        			}
	        		}
            	}
			return false;
			case 590 :
            	if(id == 4421)
            	{
            		return Castlewars.HandleItemOnBarricades(player, itemId, id, x, y, z);
            	}
			return false;
		}
		return false;
	}
	
	public static boolean handleItemOnPlayer(Player player, int itemId) {
		switch (itemId) {
		}
		return false;
	}
	
    public static boolean handleObject(final Player player, final int id, final int x, final int y, final int z) {
        switch (id) {
	    	case 4408: //guthix portal
	    		Castlewars.JoinLobby(player, Castlewars.GUTHIX);
	    	return true;
        	case 4388: //zammy portal
        		Castlewars.JoinLobby(player, Castlewars.ZAMORAK);
            return true;
        	case 4387: //sara portal
        		Castlewars.JoinLobby(player, Castlewars.SARADOMIN);
            return true;
        	case 4390: //zammy exit
        	case 4389: //sara exit
        		Castlewars.LeaveLobby(player, false);
        	return true;
            case 4407: //zammy game exit
            case 4406://sara game exit
                Castlewars.LeaveGame(player, false, 0);
            return true;
            case 4470://zammy safe room barrier
                if (player.getCastlewarsTeam() == 1) {
                    player.getActionSender().sendMessage("You are not allowed in the other teams spawn point.");
                    return true;
                }
                if (x == 2373 && y == 3126) {
                    player.getActionSender().walkTo(0, player.getPosition().getY() > y ? -1 : 1, true);
                } else if (x == 2377 && y == 3131) {
                    player.getActionSender().walkTo(player.getPosition().getX() > x-1 ? -1 : 1, 0, true);
                }
            return true;
            case 4469://sara safe room barrier
                if (player.getCastlewarsTeam() == 0) {
                    player.getActionSender().sendMessage("You are not allowed in the other teams spawn point.");
                    return true;
                }
                if (x == 2426 && y == 3081) {
                    player.getActionSender().walkTo(0, player.getPosition().getY() > y-1 ? -1 : 1, true);
                } else if (x == 2422 && y == 3076) {
                    player.getActionSender().walkTo(player.getPosition().getX() > x ? -1 : 1, 0, true);
                }
            return true;
            case 4418://stairs
                if (x == 2380 && y == 3127 && z == 0) {
                    player.teleport(new Position(2379, 3127, 1));
                }
                if (x == 2369 && y == 3126 && z == 1) {
                    player.teleport(new Position(2369, 3127, 2));
                }
                if (x == 2374 && y == 3131 && z == 2) {
                    player.teleport(new Position(2373, 3133, 3));
                }
            return true;
            case 4415://stairs
                if (x == 2419 && y == 3080 && z == 1) {
                    player.teleport(new Position(2419, 3077, 0));
                }
                if (x == 2430 && y == 3081 && z == 2) {
                    player.teleport(new Position(2427, 3081, 1));
                }
                if (x == 2425 && y == 3074 && z == 3) {
                    player.teleport(new Position(2425, 3077, 2));
                }
                if (x == 2374 && y == 3133 && z == 3) {
                    player.teleport(new Position(2374, 3130, 2));
                }
                if (x == 2369 && y == 3126 && z == 2) {
                    player.teleport(new Position(2372, 3126, 1));
                }
                if (x == 2380 && y == 3127 && z == 1) {
                    player.teleport(new Position(2380, 3130, 0));
                }
            return true;
            case 4420://stairs
                if (x == 2382 && y == 3131 && z == 0) {
                    if (player.getPosition().getX() >= 2383 && player.getPosition().getX() <= 2385) {
                        player.teleport(new Position(2382, 3130, 0));
                    } else {
                        player.teleport(new Position(2383, 3133, 0));
					}
				}
            return true;
            case 4417://stairs
                if (x == 2428 && y == 3081 && z == 1) {
                    player.teleport(new Position(2430, 3080, 2));
                }
                if (x == 2425 && y == 3074 && z == 2) {
                    player.teleport(new Position(2426, 3074, 3));
                }
                if (x == 2419 && y == 3078 && z == 0) {
                    player.teleport(new Position(2420, 3080, 1));
                }
            return true;
            case 4419: //stairs
                if (player.getPosition().getX() >= 2417) {
                    player.teleport(new Position(2416, 3074, 0));
                } else {
                    player.teleport(new Position(2417, 3077, 0));
				}
            return true;
            //LADDERS
            //TODO LADDERS
            case 4911:
                if (x == 2421 && y == 3073 && z == 1) {
                    player.teleport(new Position(2421, 3074, 0));
                }
                if (x == 2378 && y == 3134 && z == 1) {
                    player.teleport(new Position(2378, 3133, 0));
                }
            return true;
            case 1747:
                if (x == 2421 && y == 3073 && z == 0) {
                    player.teleport(new Position(2421, 3074, 1));
                    return true;
                }
                if (x == 2378 && y == 3134 && z == 0) {
                    player.teleport(new Position(2378, 3133, 1));
                    return true;
                }
            return false;
            case 4912:
                if (x == 2430 && y == 3082 && z == 0) {
                    player.teleport(new Position(player.getPosition().getX(), player.getPosition().getY() + 6400, 0));
                }
                if (x == 2369 && y == 3125 && z== 0) {
                    player.teleport(new Position(player.getPosition().getX(), player.getPosition().getY() + 6400, 0));
                }
            return true;
                
            //TABLES
            case 4458: //bandages
            case 4459: //toolkit
            case 4460: //rocks
            case 4461: //barricades
            case 4462: //climbing ropes
            case 4463: //explosive potion
            case 4464: //pickaxe table
				player.getUpdateFlags().sendAnimation(832);
				player.setStopPacket(true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer c) {
						switch(id)
						{
			            	case 4458: //bandages
			            		player.getInventory().addItem(new Item(4049,1));
			            		player.getActionSender().sendMessage("You get some bandages.");
			            		break;
			            	case 4459: //toolkit
			                	player.getInventory().addItem(new Item(4051,1));
			                    player.getActionSender().sendMessage("You get a toolkit.");
			            		break;
							case 4460: //rocks
								player.getActionSender().sendMessage("Not implemented.");
								break;
							case 4461: //barricades
				                player.getInventory().addItem(new Item(4053,1));
				                player.getActionSender().sendMessage("You get a barricade.");
				                break;
				            case 4462: //climbing ropes
				            	player.getActionSender().sendMessage("Not implemented.");
				            	break;
				            case 4463: // explosive potion
				                player.getInventory().addItem(new Item(4045,1));
				                player.getActionSender().sendMessage("You get an explosive potion.");
				                break;
				            case 4464: //pickaxe table
				                player.getInventory().addItem(new Item(1265,1));
				                player.getActionSender().sendMessage("You get a bronze pickaxe.");
				                break;
						}
						c.stop();
					}
					@Override
					public void stop() {
						player.setStopPacket(false);
					}
				}, 2);
            return true;
            	
            //DOORS
            case 4465: //sara side door
            case 4466: //sara side door
            case 4467: //zammy side door
            case 4468: //zammy side door
            	return Castlewars.HandleDoors(player, id, x, y, z);
            case 4423: //sara main door
            case 4424: //sara main door
                if (player.getCastlewarsTeam() == 0) {
                    player.getActionSender().sendMessage("The door is too strong to pass through.");
                    return true;
                }
            	player.getActionSender().walkTo(0, player.getPosition().getY() >= 3088 ? -1 : 1, true);
            	player.getActionSender().walkThroughDoubleDoor2(4423, 4424, 2426, 3088, 2427, 3088, z, 0, -1);
            return true;
            case 4427: //zammy main door
            case 4428: //zammy main door
                if (player.getCastlewarsTeam() == 1) {
                    player.getActionSender().sendMessage("The door is too strong to pass through.");
                    return true;
                }
            	player.getActionSender().walkTo(0, player.getPosition().getY() <= 3119 ? 1 : -1, true);
            	player.getActionSender().walkThroughDoubleDoor2(4427, 4428, 2373, 3119, 2372, 3119, z, 0, 1);
            return true;
            	 
            //ROCK PILES
            case 4437: //rock pile
            case 4448: //Collapse wall
            	if(id == 4437)
            	{
            		final CaveWallData caveWall = CaveWallData.forRockPosition(new Position(x,y,z));
            		if(caveWall != null)
            		{
            			if(Castlewars.getCollapsedCave(caveWall.index))
            			{
            				final Tool pickaxe = Tools.getTool(player, Skill.MINING);
            				if (pickaxe == null) {
            					player.getActionSender().sendMessage("You do not have a pickaxe that you can use.");
            					return true;
            				}
            				final int task = player.getTask();
            				final int anim = pickaxe.getAnimation();
            				player.getActionSender().sendMessage("You swing your pick at the rock.");
            				player.getUpdateFlags().sendAnimation(anim);
            				player.getActionSender().sendSound(432, 0, 0);
            				player.setSkilling(new CycleEvent() {
            					@Override
            					public void execute(CycleEventContainer container) {
            						if (!player.checkTask(task) || !Castlewars.getCollapsedCave(caveWall.index)) {
            							container.stop();
            							return;
            						}
            						container.setTick(3);
            						player.getUpdateFlags().sendAnimation(anim);
            						player.getActionSender().sendSound(432, 0, 0);
            						Castlewars.removeCollapse(caveWall, id, x, y, z);
            						container.stop();
            					}
            					@Override
            					public void stop() {
            						player.resetAnimation();
            					}
            				});
            				CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 3);
            				return true;
            			}
            		}
            	}
            	if(id == 4448)
            	{
            		final CaveWallData caveWall = CaveWallData.forWallPosition(new Position(x,y,z));
            		if(caveWall != null)
            		{
            			if(!Castlewars.getCollapsedCave(caveWall.index))
            			{
            				final Tool pickaxe = Tools.getTool(player, Skill.MINING);
            				if (pickaxe == null) {
            					player.getActionSender().sendMessage("You do not have a pickaxe that you can use.");
            					return true;
            				}
            				final int task = player.getTask();
            				final int anim = pickaxe.getAnimation();
            				player.getActionSender().sendMessage("You swing your pick at the rock.");
            				player.getUpdateFlags().sendAnimation(anim);
            				player.getActionSender().sendSound(432, 0, 0);
            				player.setSkilling(new CycleEvent() {
            					@Override
            					public void execute(CycleEventContainer container) {
            						if (!player.checkTask(task) || Castlewars.getCollapsedCave(caveWall.index)) {
            							container.stop();
            							return;
            						}
            						container.setTick(3);
            						player.getUpdateFlags().sendAnimation(anim);
            						player.getActionSender().sendSound(432, 0, 0);
        							Castlewars.collapseRock(caveWall);
            						container.stop();
            					}
            					@Override
            					public void stop() {
            						player.resetAnimation();
            					}
            				});
            				CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 3);
            				return true;
            			}else{
            				player.getActionSender().sendMessage("This rock wall has already collapsed.");
            				return true;
            			}
            		}
            	}
            return false;
                
            //FLAGS
            case 4902: //sara flag
            case 4377:
            case 4903: //zammy flag
            case 4378:
            case 4900:
            case 4901:
            	return Castlewars.HandleBannerCapture(player, id, x, y, z);
            
            case 4484:// scoreboard
               player.getDialogue().sendCastlewarsScoreBox("Total Wins This Session!", "Saradomin: "+ Castlewars.SARA_TOTAL_WINS, "Zamorak: "+ Castlewars.ZAMMY_TOTAL_WINS);
            return true;
        }
    	return false;

    	//change this to check a player variable boolean
/*    	if(!Castlewars.ENABLED)
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

        }*/
    }
}
