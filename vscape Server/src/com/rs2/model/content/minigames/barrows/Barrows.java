package com.rs2.model.content.minigames.barrows;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;

public class Barrows {
	
	public enum BarrowsBrother
	{
		AHRIM(0, 6821, 2025),
		DHAROK(1, 6771, 2026),
		GUTHAN(2, 6773, 2027),
		KARIL(3, 6822, 2028),
		TORAQ(4, 6772, 2029),
		VERAC(5, 6823, 2030);
		
		private int index;
		private int objectId;
		private int npcId;
		
		BarrowsBrother(int index, int objectId, int npcId) {
			this.index = index;
			this.objectId = objectId;
			this.npcId = npcId;
		}
		
		public static BarrowsBrother forCryptId(int objectId) {
			for (BarrowsBrother brother : BarrowsBrother.values())
					if (objectId == brother.objectId)
						return brother;
			return null;
		}
		
		public static BarrowsBrother forNpcId(int npcId) {
			for (BarrowsBrother brother : BarrowsBrother.values())
					if (npcId == brother.npcId)
						return brother;
			return null;
		}
		
		public static BarrowsBrother forIndex(int index) {
			for (BarrowsBrother brother : BarrowsBrother.values())
				if (index == brother.index)
					return brother;
			return null;
		}
	}
	
	private static final int[] Items = {4708, 4710, 4712, 4714, 4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747, 4749, 4751, 4753, 4755, 4757, 4759};
	private static final int[][] Stackables = {{4740, 5}, {558, 25}, {562, 11}, {560, 5}, {565, 2}, {995, 55}};
	private static final int[] Monsters = {2031, 2033, 2034, 2035, 2037};
	private static final MinigameAreas.Area NORTH = new MinigameAreas.Area(new Position(3549, 9709, 0), new Position(3554, 9714, 0));
	private static final MinigameAreas.Area WEST = new MinigameAreas.Area(new Position(3531, 9691, 0), new Position(3537, 9697, 0));
	private static final MinigameAreas.Area EAST = new MinigameAreas.Area(new Position(3566, 9692, 0), new Position(3571, 9697, 0));
	private static final MinigameAreas.Area SOUTH = new MinigameAreas.Area(new Position(3548, 9675, 0), new Position(3554, 9680, 0));
	private static final MinigameAreas.Area NORTHWEST = new MinigameAreas.Area(new Position(3531, 9710, 0), new Position(3537, 9713, 0));
	private static final MinigameAreas.Area NORTHEAST = new MinigameAreas.Area(new Position(3565, 9712, 0), new Position(3572, 9714, 0));
	private static final MinigameAreas.Area SOUTHWEST = new MinigameAreas.Area(new Position(3531, 9675, 0), new Position(3537, 9679, 0));
	private static final MinigameAreas.Area SOUTHEAST = new MinigameAreas.Area(new Position(3565, 9676, 0), new Position(3571, 9679, 0));
	public static final MinigameAreas.Area[] AREAS = {NORTH, WEST, EAST, SOUTH, NORTHWEST, NORTHEAST, SOUTHWEST, SOUTHEAST};  
        
	public static int random(int range) {
            if(range == 6 || range < 0) {
			int number = (int) Math.ceil(Math.random() * 20);
                return number == 1 ? 0 : number;
            }
            else if( range > 0) {
                int number = (int) Math.ceil(Math.random() * (range - 2));
                return number == 1 ? 0 : number;
                
            }
            else return -1;
        }

	public static boolean barrowsObject(Player player, int objectId) {
		switch (objectId) {
			case 10284 : // chest
				if (player.getKillCount() < 1) {
					player.getActionSender().sendMessage("You search the chest but don't find anything.");
					player.getTeleportation().teleport(3565, 3298, 0, true);
					return true;
				}
				else
				{
					BarrowsBrother brother = BarrowsBrother.forIndex(player.getRandomGrave());
					if(brother != null)
					{
						if (NpcLoader.checkSpawn(player, brother.npcId)) 
						{
							player.getActionSender().sendMessage("You must kill the the brother before searching this.");
							return true;
						}		
						if (player.getBarrowsNpcDead(player.getRandomGrave()) ) 
						{
							getReward(player);
							return true;
						}
						if (player.getSpawnedNpc() == null || player.getSpawnedNpc().getNpcId() != brother.npcId && !player.getBarrowsNpcDead(brother.index)) 
						{
							NpcLoader.spawnNpc(player, new Npc(brother.npcId), true, true);
							return true;
						}
					}
				}
			return true;
			case 6823 :
			case 6772 :
			case 6821 :
			case 6771 :
			case 6773 :
			case 6822 :
				BarrowsBrother brother = BarrowsBrother.forCryptId(objectId);
				if(brother != null)
				{
					if (brother.index == player.getRandomGrave()) {
						Dialogues.startDialogue(player, 10001);
						return true;
					}
					if (NpcLoader.checkSpawn(player, brother.npcId)) 
					{
						player.getActionSender().sendMessage("You must kill the the brother before searching this.");
						return true;
					}
					if (player.getBarrowsNpcDead()[brother.index]) 
					{
						player.getActionSender().sendMessage("You have already searched this sarcophagus.");
						return true;
					}
					if (player.getSpawnedNpc() == null || player.getSpawnedNpc().getNpcId() != brother.npcId && !player.getBarrowsNpcDead()[brother.index]) 
					{
						NpcLoader.spawnNpc(player, new Npc(brother.npcId), true, true);
					}
					if (brother.index != player.getRandomGrave()) {
						player.getActionSender().sendMessage("You don't find anything.");
						return true;
					}
				}
			return true;
			case 6707 : // verac stairs
				player.teleport(new Position(3556, 3298, 0));
				return true;
			case 6706 : // torag stairs
				player.teleport(new Position(3553, 3283, 0));
				break;
			case 6705 : // karil stairs
				player.teleport(new Position(3565, 3276, 0));
				return true;
			case 6704 : // guthan stairs
				player.teleport(new Position(3578, 3284, 0));
				return true;
			case 6703 : // dharok stairs
				player.teleport(new Position(3574, 3298, 0));
				return true;
			case 6702 : // ahrim stairs
				player.teleport(new Position(3565, 3290, 0));
				return true;
		}
		return false;
	}
	
	public static boolean digCrypt(Player player) {
		if (player.Area(3553, 3561, 3294, 3301)) {
			player.teleport(new Position(3578, 9706, 3));
			player.getActionSender().sendMessage("You've broken into a crypt!");
	        player.getActionSender().sendMapState(2);
			return true;
		} else if (player.Area(3550, 3557, 3278, 3287)) {
			player.teleport(new Position(3568, 9683, 3));
			player.getActionSender().sendMessage("You've broken into a crypt!");
	        player.getActionSender().sendMapState(2);
			return true;
		} else if (player.Area(3561, 3568, 3285, 3292)) {
			player.teleport(new Position(3557, 9703, 3));
			player.getActionSender().sendMessage("You've broken into a crypt!");
	        player.getActionSender().sendMapState(2);
			return true;
		} else if (player.Area(3570, 3579, 3293, 3302)) {
			player.teleport(new Position(3556, 9718, 3));
			player.getActionSender().sendMessage("You've broken into a crypt!");
	        player.getActionSender().sendMapState(2);
			return true;
		} else if (player.Area(3571, 3582, 3278, 3285)) {
			player.teleport(new Position(3534, 9704, 3));
			player.getActionSender().sendMessage("You've broken into a crypt!");
	        player.getActionSender().sendMapState(2);
	        return true;
		} else if (player.Area(3562, 3569, 3273, 3279)) {
			player.teleport(new Position(3546, 9684, 3));
			player.getActionSender().sendMessage("You've broken into a crypt!");
	        player.getActionSender().sendMapState(2);
			return true;
		}
		return false;
	}

	public static void getReward(Player player) {
		final int number = Misc.randomMinusOne(Stackables.length);
		final int rune = Stackables[number][0];
		final int amount = Stackables[number][1];
		final int reward = Items[Misc.randomMinusOne(Items.length)];
		int kills = player.getKillCount();
		if (kills < 1) {
			player.getActionSender().sendMessage("You can only loot the chest after killing at least 1 brother.");
			return;
		}
		boolean getBarrows = random(126 - (kills * 8)) == 0;
		if (getBarrows) {
			if (player.getInventory().getItemContainer().freeSlots() == 1) {
				if (!player.getInventory().playerHasItem(rune)) {
					player.getActionSender().sendMessage("You must have three empty spaces in order to take this loot.");
					return;
				}
			} else if (player.getInventory().getItemContainer().freeSlots() < 1) {
				player.getActionSender().sendMessage("You must have three empty spaces in order to take this loot.");
				return;
			}
			player.getInventory().addItem(new Item(rune, Misc.random(amount * kills) + 1));
			player.getInventory().addItem(new Item(reward, 1));
		} else {
			final int number2 = Misc.randomMinusOne(Stackables.length);
			final int rune2 = Stackables[number2][0];
			final int amount2 = Stackables[number2][1];
			if (player.getInventory().getItemContainer().freeSlots() < 1) {
				if (!player.getInventory().playerHasItem(rune) || !player.getInventory().playerHasItem(rune2)) {
					player.getActionSender().sendMessage("You must have three empty spaces in order to take this loot.");
					return;
				}
			}
			if (player.getInventory().getItemContainer().freeSlots() == 1) {
				if (!player.getInventory().playerHasItem(rune) && !player.getInventory().playerHasItem(rune2)) {
					player.getActionSender().sendMessage("You must have three empty spaces in order to take this loot.");
					return;
				}
			}
			player.getInventory().addItem(new Item(rune, Misc.random(amount * kills) + 1));
			player.getInventory().addItem(new Item(rune2, Misc.random(amount2 * kills) + 1));
		}
		player.getUpdateFlags().sendAnimation(714);
		player.getUpdateFlags().sendHighGraphic(301);
		player.getTeleportation().teleport(3565, 3298, 0, true);
		player.getActionSender().sendMessage("You grab the loot and teleport away.");
		resetBarrows(player);
		return;
	}
	
	private static boolean northSpawn = true;
	private static boolean westSpawn = true;
	private static boolean eastSpawn = true;
	private static boolean southSpawn = true;
	private static boolean northWestSpawn = true;
	private static boolean northEastSpawn = true;
	private static boolean southWestSpawn = true;
	private static boolean southEastSpawn = true;
	
	public static boolean handleObjectClicking(Player player, int objectId, int x, int y) {
        Position North = new Position(3551,9711,0);
        Position West = new Position(3534,9694,0);
        Position East = new Position(3568,9694,0);
        Position South = new Position(3551,9677,0);
        Position Center = new Position(3551, 9694,0);
        Position NorthEast = new Position(3568,9711,0);
        Position NorthWest = new Position(3534,9711,0);
        Position SouthEast = new Position(3568,9677,0);
        Position SouthWest = new Position(3534,9677,0);
        final int spawn1 = Monsters[Misc.randomMinusOne(Monsters.length)];
        final int spawn2 = Monsters[Misc.randomMinusOne(Monsters.length)];
        final int spawn3 = Monsters[Misc.randomMinusOne(Monsters.length)];
           
                switch(objectId)
                {
                   
                    case 6736: //West outer tunnel
                        if( x == 3528 && y == 9712 && player.getPosition().getX() == 3529 ) {
                            player.getActionSender().walkThroughDoubleDoor(6736, 6717, 3528, 9712, 3528, 9711, 0);
                            player.movePlayer(new Position(3528,9712));
                        }
                        else if ( x == 3528 && y == 9712 ) {
                            player.getActionSender().walkThroughDoubleDoor(6736, 6717, 3528, 9712, 3528, 9711, 0);
                            player.movePlayer(new Position(3529,9712));
                            if(northWestSpawn == true){
                                if(player.getBarrowsNpcDead()[4]){
                                    spawn(spawn1, NORTHWEST );
                                    spawn(spawn2, NORTHWEST );
                                    spawn(spawn3, NORTHWEST );
                                }
                                else if(!player.getBarrowsNpcDead()[4]){
                                    spawn(spawn1, NORTHWEST );
                                    spawn(spawn2, NORTHWEST );
                                    NpcLoader.spawnNpc(player, new Npc(2029), true, true);
                                }
                                northWestSpawn = false;
                            }
                        }
                        else if ( x == 3528 && y == 9678 && player.getPosition().getX() == 3529 ) {
                            player.getActionSender().walkThroughDoubleDoor(6736, 6717, 3528, 9678, 3528, 9677, 0);
                            player.movePlayer(new Position(3528,9678));
                        }
                        else if ( x == 3528 && y == 9678 ) {
                            player.getActionSender().walkThroughDoubleDoor(6736, 6717, 3528, 9678, 3528, 9677, 0);
                            player.movePlayer(new Position(3529,9678));
                            if(southWestSpawn == true){
                                spawn(spawn1, SOUTHWEST );
                                spawn(spawn2, SOUTHWEST );
                                spawn(spawn3, SOUTHWEST );
                                southWestSpawn = false;
                            }
                        }
                        return true;
                    case 6717: //West outer tunnel
                        if( x == 3528 && y == 9711 && player.getPosition().getX() == 3529  ) {
                            player.getActionSender().walkThroughDoubleDoor(6736, 6717, 3528, 9712, 3528, 9711, 0);
                            player.movePlayer(new Position(3528,9711));
                        }
                        else if ( x == 3528 && y == 9711 ) {
                            player.getActionSender().walkThroughDoubleDoor(6736, 6717, 3528, 9712, 3528, 9711, 0);
                            player.movePlayer(new Position(3529,9711));
                            if(northWestSpawn == true){
                                if(player.getBarrowsNpcDead()[4]){
                                    spawn(spawn1, NORTHWEST );
                                    spawn(spawn2, NORTHWEST );
                                    spawn(spawn3, NORTHWEST );
                                }
                                else if(!player.getBarrowsNpcDead()[4]){
                                    spawn(spawn1, NORTHWEST );
                                    spawn(spawn2, NORTHWEST );
                                    NpcLoader.spawnNpc(player, new Npc(2029), true, true);
                                }
                                northWestSpawn = false;
                            }
                        }
                        else if ( x == 3528 && y == 9677 && player.getPosition().getX() == 3529 ) {
                            player.getActionSender().walkThroughDoubleDoor(6736, 6717, 3528, 9678, 3528, 9677, 0);
                            player.movePlayer(new Position(3528,9677));
                        }
                        else if ( x == 3528 && y == 9677 ) {
                            player.getActionSender().walkThroughDoubleDoor(6736, 6717, 3528, 9678, 3528, 9677, 0);
                            player.movePlayer(new Position(3529,9677));
                            if(southWestSpawn == true){
                                spawn(spawn1, SOUTHWEST );
                                spawn(spawn2, SOUTHWEST );
                                spawn(spawn3, SOUTHWEST );
                                southWestSpawn = false;
                            }
                        }
                        return true;
                    case 6742: //East outer tunnel
                        if( x == 3575 && y == 9711 && player.getPosition().getX() == 3574 ) {
                            player.getActionSender().walkThroughDoubleDoor(6742, 6723, 3575, 9711, 3575, 9712, 0);
                            player.movePlayer(new Position(3575,9711));
                        }
                        else if ( x == 3575 && y == 9711 ) {
                            player.getActionSender().walkThroughDoubleDoor(6742, 6723, 3575, 9711, 3575, 9712, 0);
                            player.movePlayer(new Position(3574,9711));
                            if(northEastSpawn == true){
                                spawn(spawn1, NORTHEAST );
                                spawn(spawn2, NORTHEAST );
                                spawn(spawn3, NORTHEAST );
                                northEastSpawn = false;
                            }
                        }
                        else if ( x == 3575 && y == 9677 && player.getPosition().getX() == 3574 ) {
                            player.getActionSender().walkThroughDoubleDoor(6742, 6723, 3575, 9677, 3575, 9678, 0);
                            player.movePlayer(new Position(3575,9677));
                        }
                        else if ( x == 3575 && y == 9677 ) {
                            player.getActionSender().walkThroughDoubleDoor(6742, 6723, 3575, 9677, 3575, 9678, 0);
                            player.movePlayer(new Position(3574,9677));
                            if(southEastSpawn == true){
                                if(player.getBarrowsNpcDead()[5]){
                                    spawn(spawn1, SOUTHEAST );
                                    spawn(spawn2, SOUTHEAST );
                                    spawn(spawn3, SOUTHEAST );
                                }
                                else if(!player.getBarrowsNpcDead()[5]){
                                    spawn(spawn1, SOUTHEAST );
                                    spawn(spawn2, SOUTHEAST );
                                    NpcLoader.spawnNpc(player, new Npc(2030), true, true);
                                }
                                southEastSpawn = false;
                            }
                        }
                        return true;
                    case 6723: //East outer tunnel
                        if( x == 3575 && y == 9712 && player.getPosition().getX() == 3574  ) {
                            player.getActionSender().walkThroughDoubleDoor(6742, 6723, 3575, 9711, 3575, 9712, 0);
                            player.movePlayer(new Position(3575,9712));
                        }
                        else if ( x == 3575 && y == 9712 ) {
                            player.getActionSender().walkThroughDoubleDoor(6742, 6723, 3575, 9711, 3575, 9712, 0);
                            player.movePlayer(new Position(3574,9712));
                            if(northEastSpawn == true){
                                spawn(spawn1, NORTHEAST );
                                spawn(spawn2, NORTHEAST );
                                spawn(spawn3, NORTHEAST );
                                northEastSpawn = false;
                            }
                        }
                        else if ( x == 3575 && y == 9678 && player.getPosition().getX() == 3574 ) {
                            player.getActionSender().walkThroughDoubleDoor(6742, 6723, 3575, 9677, 3575, 9678, 0);
                            player.movePlayer(new Position(3575,9678));
                        }
                        else if ( x == 3575 && y == 9678 ) {
                            player.getActionSender().walkThroughDoubleDoor(6742, 6723, 3575, 9677, 3575, 9678, 0);
                            player.movePlayer(new Position(3574,9678));
                            if(southEastSpawn == true){
                                if(player.getBarrowsNpcDead()[5]){
                                    spawn(spawn1, SOUTHEAST );
                                    spawn(spawn2, SOUTHEAST );
                                    spawn(spawn3, SOUTHEAST );
                                }
                                else if(!player.getBarrowsNpcDead()[5]){
                                    spawn(spawn1, SOUTHEAST );
                                    spawn(spawn2, SOUTHEAST );
                                    NpcLoader.spawnNpc(player, new Npc(2030), true, true);
                                }
                                southEastSpawn = false;
                            }
                        }
                        return true;
                       
                    case 6750: //South outer tunnel
                        if( x == 3568  && y == 9671 && player.getPosition().getY() == 9672 ) {
                            player.getActionSender().walkThroughDoubleDoor(6750, 6731, 3568, 9671, 3569, 9671, 0);
                            player.movePlayer(new Position(3568,9671));
                        }
                        else if ( x == 3568 && y == 9671 ) {
                            player.getActionSender().walkThroughDoubleDoor(6750, 6731, 3568, 9671, 3569, 9671, 0);
                            player.movePlayer(new Position(3568,9672));
                            if(southEastSpawn == true){
                                if(player.getBarrowsNpcDead()[5]){
                                    spawn(spawn1, SOUTHEAST );
                                    spawn(spawn2, SOUTHEAST );
                                    spawn(spawn3, SOUTHEAST );
                                }
                                else if(!player.getBarrowsNpcDead()[5]){
                                    spawn(spawn1, SOUTHEAST );
                                    spawn(spawn2, SOUTHEAST );
                                    NpcLoader.spawnNpc(player, new Npc(2030), true, true);
                                }
                            southEastSpawn = false;
                            }
                        }
                        else if ( x == 3534 && y == 9671 && player.getPosition().getY() == 9672 ) {
                            player.getActionSender().walkThroughDoubleDoor(6750, 6731, 3534, 9671, 3535, 9671, 0);
                            player.movePlayer(new Position(3534,9671));
                        }
                        else if ( x == 3534 && y == 9671 ) {
                            player.getActionSender().walkThroughDoubleDoor(6750, 6731, 3534, 9671, 3535, 9671, 0);
                            player.movePlayer(new Position(3534,9672));
                            if(southWestSpawn == true){
                                spawn(spawn1, SOUTHWEST );
                                spawn(spawn2, SOUTHWEST );
                                spawn(spawn3, SOUTHWEST );
                                southWestSpawn = false;
                            }
                        }
                        return true;
                    case 6731: //South outer tunnel
                        if( x == 3569 && y == 9671 && player.getPosition().getY() == 9672 ) {
                            player.getActionSender().walkThroughDoubleDoor(6750, 6731, 3568, 9671, 3569, 9671, 0);
                            player.movePlayer(new Position(3569,9671));
                        }
                        else if ( x == 3569 && y == 9671 ) {
                            player.getActionSender().walkThroughDoubleDoor(6750, 6731, 3568, 9671, 3569, 9671, 0);
                            player.movePlayer(new Position(3569,9672));
                            if(southEastSpawn == true){
                                if(player.getBarrowsNpcDead()[5]){
                                    spawn(spawn1, SOUTHEAST );
                                    spawn(spawn2, SOUTHEAST );
                                    spawn(spawn3, SOUTHEAST );
                                }
                                else if(!player.getBarrowsNpcDead()[5]){
                                    spawn(spawn1, SOUTHEAST );
                                    spawn(spawn2, SOUTHEAST );
                                    NpcLoader.spawnNpc(player, new Npc(2030), true, true);
                                }
                                southEastSpawn = false;
                            }
                        }
                        else if ( x == 3535 && y == 9671 && player.getPosition().getY() == 9672 ) {
                            player.getActionSender().walkThroughDoubleDoor(6750, 6731, 3534, 9671, 3535, 9671, 0);
                            player.movePlayer(new Position(3535,9671));
                        }
                        else if ( x == 3535 && y == 9671 ) {
                            player.getActionSender().walkThroughDoubleDoor(6750, 6731, 3534, 9671, 3535, 9671, 0);
                            player.movePlayer(new Position(3535,9672));
                            if(southWestSpawn == true){
                                spawn(spawn1, SOUTHWEST );
                                spawn(spawn2, SOUTHWEST );
                                spawn(spawn3, SOUTHWEST );
                                southWestSpawn = false;
                            }
                        }
                        return true;
                       
                    case 6735: //North outer tunnel
                        if( x == 3569  && y == 9718 && player.getPosition().getY() == 9717 ) {
                            player.getActionSender().walkThroughDoubleDoor(6735, 6716, 3569, 9718, 3568, 9718, 0);
                            player.movePlayer(new Position(3569,9718));
                        }
                        else if ( x == 3569 && y == 9718 ) {
                            player.getActionSender().walkThroughDoubleDoor(6735, 6716, 3569, 9718, 3568, 9718, 0);
                            player.movePlayer(new Position(3569,9717));
                            if(northEastSpawn == true){
                                spawn(spawn1, NORTHEAST );
                                spawn(spawn2, NORTHEAST );
                                spawn(spawn3, NORTHEAST );
                                northEastSpawn = false;
                            }
                        }
                        else if ( x == 3535 && y == 9718 && player.getPosition().getY() == 9717 ) {
                            player.getActionSender().walkThroughDoubleDoor(6735, 6716, 3535, 9718, 3534, 9718, 0);
                            player.movePlayer(new Position(3535,9718));
                        }
                        else if ( x == 3535 && y == 9718 ) {
                            player.getActionSender().walkThroughDoubleDoor(6735, 6716, 3535, 9718, 3534, 9718, 0);
                            player.movePlayer(new Position(3535,9717));
                            if(northWestSpawn == true){
                                if(player.getBarrowsNpcDead()[4]){
                                    spawn(spawn1, NORTHWEST );
                                    spawn(spawn2, NORTHWEST );
                                    spawn(spawn3, NORTHWEST );
                                }
                                else if(!player.getBarrowsNpcDead()[4]){
                                    spawn(spawn1, NORTHWEST );
                                    spawn(spawn2, NORTHWEST );
                                    NpcLoader.spawnNpc(player, new Npc(2029), true, true);
                                }
                                northWestSpawn = false;
                            }
                        }
                        return true;
                    case 6716: //North outer tunnel
                        if( x == 3568 && y == 9718 && player.getPosition().getY() == 9717 ) {
                            player.getActionSender().walkThroughDoubleDoor(6735, 6716, 3569, 9718, 3568, 9718, 0);
                            player.movePlayer(new Position(3568,9718));
                        }
                        else if ( x == 3568 && y == 9718 ) {
                            player.getActionSender().walkThroughDoubleDoor(6735, 6716, 3569, 9718, 3568, 9718, 0);
                            player.movePlayer(new Position(3568,9717));
                            if(northEastSpawn == true){
                                spawn(spawn1, NORTHEAST );
                                spawn(spawn2, NORTHEAST );
                                spawn(spawn3, NORTHEAST );
                                northEastSpawn = false;
                            }
                        }
                        else if ( x == 3534 && y == 9718 && player.getPosition().getY() == 9717 ) {
                            player.getActionSender().walkThroughDoubleDoor(6735, 6716, 3535, 9718, 3534, 9718, 0);
                            player.movePlayer(new Position(3534,9718));
                        }
                        else if ( x == 3534 && y == 9718 ) {
                            player.getActionSender().walkThroughDoubleDoor(6735, 6716, 3535, 9718, 3534, 9718, 0);
                            player.movePlayer(new Position(3534,9717));
                            if(northWestSpawn == true){
                                if(player.getBarrowsNpcDead()[4]){
                                    spawn(spawn1, NORTHWEST );
                                    spawn(spawn2, NORTHWEST );
                                    spawn(spawn3, NORTHWEST );
                                }
                                else if(!player.getBarrowsNpcDead()[4]){
                                    spawn(spawn1, NORTHWEST );
                                    spawn(spawn2, NORTHWEST );
                                    NpcLoader.spawnNpc(player, new Npc(2029), true, true);
                                }
                                northWestSpawn = false;
                            }
                        }
                        return true;
                       
                       
                    case 6739: //North tunnel
                        if(x == 3552 && y == 9701){
                            player.teleport(North);
                            if(northSpawn == true){
                                if(player.getBarrowsNpcDead()[0]){
                                    spawn(spawn1, NORTH );
                                    spawn(spawn2, NORTH );
                                    spawn(spawn3, NORTH );
                                }
                                else if(!player.getBarrowsNpcDead()[0]){
                                    spawn(spawn1, NORTH );
                                    spawn(spawn2, NORTH );
                                    NpcLoader.spawnNpc(player, new Npc(2025), true, true);
                                }
                                northSpawn = false;
                            }
                        }
                        else if(x == 3551 && y == 9705)
                            player.teleport(Center);
                    return true;
                    case 6720: //North tunnel 2
                        if(x == 3551 && y == 9701) {
                            player.teleport(North);
                            if(northSpawn == true){
                                if(player.getBarrowsNpcDead()[0]){
                                    spawn(spawn1, NORTH );
                                    spawn(spawn2, NORTH );
                                    spawn(spawn3, NORTH );
                                }
                                else if(!player.getBarrowsNpcDead()[0]){
                                    spawn(spawn1, NORTH );
                                    spawn(spawn2, NORTH );
                                    NpcLoader.spawnNpc(player, new Npc(2025), true, true);
                                }
                                northSpawn = false;
                            }
                        }
                        else if(x == 3552 && y == 9705)
                            player.teleport(Center);
                    return true;
                       
                    case 6743: //West tunnel
                        if(x == 3545 && y == 9695) {
                            player.teleport(West);
                            if(westSpawn == true){
                                if(player.getBarrowsNpcDead()[1]){
                                    spawn(spawn1, WEST );
                                    spawn(spawn2, WEST );
                                    spawn(spawn3, WEST );
                                }
                                else if(!player.getBarrowsNpcDead()[1]){
                                    spawn(spawn1, WEST );
                                    spawn(spawn2, WEST );
                                    NpcLoader.spawnNpc(player, new Npc(2026), true, true);
                                }
                                westSpawn = false;
                            }
                        }
                        else if(x == 3541 && y == 9694)
                            player.teleport(Center);
                    return true;
                    case 6724: //West tunnel 2
                        if(x == 3545 && y == 9694) {
                            player.teleport(West);
                            if(westSpawn == true){
                                if(player.getBarrowsNpcDead()[1]){
                                    spawn(spawn1, WEST );
                                    spawn(spawn2, WEST );
                                    spawn(spawn3, WEST );
                                }
                                else if(!player.getBarrowsNpcDead()[1]){
                                    spawn(spawn1, WEST );
                                    spawn(spawn2, WEST );
                                    NpcLoader.spawnNpc(player, new Npc(2026), true, true);
                                }
                                westSpawn = false;
                            }
                        }
                        else if(x == 3541 && y == 9695)
                            player.teleport(Center);
                    return true;
                       
                    case 6744: //East tunnel
                        if(x == 3558 && y == 9694){
                            player.teleport(East);
                            if(eastSpawn == true){
                                if(player.getBarrowsNpcDead()[2]){
                                    spawn(spawn1, EAST );
                                    spawn(spawn2, EAST );
                                    spawn(spawn3, EAST );
                                }
                                else if(!player.getBarrowsNpcDead()[2]){
                                    spawn(spawn1, EAST );
                                    spawn(spawn2, EAST );
                                    NpcLoader.spawnNpc(player, new Npc(2027), true, true);
                                }
                                eastSpawn = false;
                            }
                        }
                        else if(x == 3562 && y == 9695)
                            player.teleport(Center);
                    return true;
                    case 6725: //East tunnel 2
                        if(x == 3558 && y == 9695) {
                            player.teleport(East);
                            if(eastSpawn == true){
                                if(player.getBarrowsNpcDead()[2]){
                                    spawn(spawn1, EAST );
                                    spawn(spawn2, EAST );
                                    spawn(spawn3, EAST );
                                }
                                else if(!player.getBarrowsNpcDead()[2]){
                                    spawn(spawn1, EAST );
                                    spawn(spawn2, EAST );
                                    NpcLoader.spawnNpc(player, new Npc(2027), true, true);
                                }
                                eastSpawn = false;
                            }
                        }
                        else if(x == 3562 && y == 9694)
                            player.teleport(Center);
                    return true;
                       
                    case 6746: //South tunnel
                        if(x == 3551 && y == 9688) {
                            player.teleport(South);
                            if(southSpawn == true){
                                if(player.getBarrowsNpcDead()[3]){
                                    spawn(spawn1, SOUTH );
                                    spawn(spawn2, SOUTH );
                                    spawn(spawn3, SOUTH );
                                }
                                else if(!player.getBarrowsNpcDead()[3]){
                                    spawn(spawn1, SOUTH );
                                    spawn(spawn2, SOUTH );
                                    NpcLoader.spawnNpc(player, new Npc(2028), true, true);
                                }
                                southSpawn = false;
                            }
                        }
                        else if(x == 3552 && y == 9684)
                            player.teleport(Center);
                    return true;
                    case 6727: //South tunnel 2
                        if(x == 3552 && y == 9688) {
                            player.teleport(South);
                            if(southSpawn == true){
                                if(player.getBarrowsNpcDead()[3]){
                                    spawn(spawn1, SOUTH );
                                    spawn(spawn2, SOUTH );
                                    spawn(spawn3, SOUTH );
                                }
                                else if(!player.getBarrowsNpcDead()[3]){
                                    spawn(spawn1, SOUTH );
                                    spawn(spawn2, SOUTH );
                                    NpcLoader.spawnNpc(player, new Npc(2028), true, true);
                                }
                                southSpawn = false;
                            }
                        }
                        else if(x == 3551 && y == 9684)
                            player.teleport(Center);
                    return true;
                         
                    case 6741: //East to northeast tunnel
                        if(x == 3569 && y == 9701) {
                            player.teleport(NorthEast);
                            if(northEastSpawn == true){
                                spawn(spawn1, NORTHEAST );
                                spawn(spawn2, NORTHEAST );
                                spawn(spawn3, NORTHEAST );
                                northEastSpawn = false;
                            }
                        }
                        else if(x == 3568 && y == 9705){
                            player.teleport(East);
                            if(eastSpawn == true){
                                if(player.getBarrowsNpcDead()[2]){
                                    spawn(spawn1, EAST );
                                    spawn(spawn2, EAST );
                                    spawn(spawn3, EAST );
                                }
                                else if(!player.getBarrowsNpcDead()[2]){
                                    spawn(spawn1, EAST );
                                    spawn(spawn2, EAST );
                                    NpcLoader.spawnNpc(player, new Npc(2027), true, true);
                                }
                                eastSpawn = false;
                            }
                        }
                    return true;
                    case 6722: //East to northeast tunnel 2
                        if(x == 3568 && y == 9701) {
                            player.teleport(NorthEast);
                            if(northEastSpawn == true){
                                spawn(spawn1, NORTHEAST );
                                spawn(spawn2, NORTHEAST );
                                spawn(spawn3, NORTHEAST );
                                northEastSpawn = false;
                            }
                        }
                        else if(x == 3569 && y == 9705) {
                            player.teleport(East);
                            if(eastSpawn == true){
                                if(player.getBarrowsNpcDead()[2]){
                                    spawn(spawn1, EAST );
                                    spawn(spawn2, EAST );
                                    spawn(spawn3, EAST );
                                }
                                else if(!player.getBarrowsNpcDead()[2]){
                                    spawn(spawn1, EAST );
                                    spawn(spawn2, EAST );
                                    NpcLoader.spawnNpc(player, new Npc(2027), true, true);
                                }
                                eastSpawn = false;
                            }
                        }
                    return true;
                       
                    case 6747: //East to southeast tunnel
                        if(x == 3568 && y == 9688) {
                            player.teleport(SouthEast);
                            if(southEastSpawn == true){
                                if(player.getBarrowsNpcDead()[5]){
                                    spawn(spawn1, SOUTHEAST );
                                    spawn(spawn2, SOUTHEAST );
                                    spawn(spawn3, SOUTHEAST );
                                }
                                else if(!player.getBarrowsNpcDead()[5]){
                                    spawn(spawn1, SOUTHEAST );
                                    spawn(spawn2, SOUTHEAST );
                                    NpcLoader.spawnNpc(player, new Npc(2030), true, true);
                                }
                                southEastSpawn = false;
                            }
                        }
                        else if(x == 3569 && y == 9684){
                            player.teleport(East);
                            if(eastSpawn == true){
                                if(player.getBarrowsNpcDead()[2]){
                                    spawn(spawn1, EAST );
                                    spawn(spawn2, EAST );
                                    spawn(spawn3, EAST );
                                }
                                else if(!player.getBarrowsNpcDead()[2]){
                                    spawn(spawn1, EAST );
                                    spawn(spawn2, EAST );
                                    NpcLoader.spawnNpc(player, new Npc(2027), true, true);
                                }
                                eastSpawn = false;
                            }
                        }
                    return true;
                    case 6728: //East to southeast tunnel 2
                        if(x == 3569 && y == 9688) {
                            player.teleport(SouthEast);
                            if(southEastSpawn == true){
                                if(player.getBarrowsNpcDead()[5]){
                                    spawn(spawn1, SOUTHEAST );
                                    spawn(spawn2, SOUTHEAST );
                                    spawn(spawn3, SOUTHEAST );
                                }
                                else if(!player.getBarrowsNpcDead()[5]){
                                    spawn(spawn1, SOUTHEAST );
                                    spawn(spawn2, SOUTHEAST );
                                    NpcLoader.spawnNpc(player, new Npc(2030), true, true);
                                }
                                southEastSpawn = false;
                            }
                        }
                        else if(x == 3568 && y == 9684) {
                            player.teleport(East);
                            if(eastSpawn == true){
                                if(player.getBarrowsNpcDead()[2]){
                                    spawn(spawn1, EAST );
                                    spawn(spawn2, EAST );
                                    spawn(spawn3, EAST );
                                }
                                else if(!player.getBarrowsNpcDead()[2]){
                                    spawn(spawn1, EAST );
                                    spawn(spawn2, EAST );
                                    NpcLoader.spawnNpc(player, new Npc(2027), true, true);
                                }
                                eastSpawn = false;
                            }
                        }
                    return true;
                       
                    case 6749: //Southeast to south tunnel
                        if(x == 3562 && y == 9678) {
                            player.teleport(South);
                            if(southSpawn == true){
                                if(player.getBarrowsNpcDead()[3]){
                                    spawn(spawn1, SOUTH );
                                    spawn(spawn2, SOUTH );
                                    spawn(spawn3, SOUTH );
                                }
                                else if(!player.getBarrowsNpcDead()[3]){
                                    spawn(spawn1, SOUTH );
                                    spawn(spawn2, SOUTH );
                                    NpcLoader.spawnNpc(player, new Npc(2028), true, true);
                                }
                                southSpawn = false;
                            }
                        }
                        else if(x == 3558 && y == 9677){
                            player.teleport(SouthEast);
                            if(southEastSpawn == true){
                                if(player.getBarrowsNpcDead()[5]){
                                    spawn(spawn1, SOUTHEAST );
                                    spawn(spawn2, SOUTHEAST );
                                    spawn(spawn3, SOUTHEAST );
                                }
                                else if(!player.getBarrowsNpcDead()[5]){
                                    spawn(spawn1, SOUTHEAST );
                                    spawn(spawn2, SOUTHEAST );
                                    NpcLoader.spawnNpc(player, new Npc(2030), true, true);
                                }
                                southEastSpawn = false;
                            }
                        }
                    return true;
                    case 6730: //Southeast to east tunnel 2
                        if(x == 3562 && y == 9677) {
                            player.teleport(South);
                            if(southSpawn == true){
                                if(player.getBarrowsNpcDead()[3]){
                                    spawn(spawn1, SOUTH );
                                    spawn(spawn2, SOUTH );
                                    spawn(spawn3, SOUTH );
                                }
                                else if(!player.getBarrowsNpcDead()[3]){
                                    spawn(spawn1, SOUTH );
                                    spawn(spawn2, SOUTH );
                                    NpcLoader.spawnNpc(player, new Npc(2028), true, true);
                                }
                                southSpawn = false;
                            }
                        }
                        else if(x == 3558 && y == 9678) {
                            player.teleport(SouthEast);
                            if(southEastSpawn == true){
                                if(player.getBarrowsNpcDead()[5]){
                                    spawn(spawn1, SOUTHEAST );
                                    spawn(spawn2, SOUTHEAST );
                                    spawn(spawn3, SOUTHEAST );
                                }
                                else if(!player.getBarrowsNpcDead()[5]){
                                    spawn(spawn1, SOUTHEAST );
                                    spawn(spawn2, SOUTHEAST );
                                    NpcLoader.spawnNpc(player, new Npc(2030), true, true);
                                }
                                southEastSpawn = false;
                            }
                        }
                    return true;
                       
                    case 6748: //South to southwest tunnel
                        if(x == 3545 && y == 9678) {
                            player.teleport(SouthWest);
                            if(southWestSpawn == true){
                                spawn(spawn1, SOUTHWEST );
                                spawn(spawn2, SOUTHWEST );
                                spawn(spawn3, SOUTHWEST );
                                southWestSpawn = false;
                            }
                        }
                        else if(x == 3541 && y == 9677){
                            player.teleport(South);
                            if(southSpawn == true){
                                if(player.getBarrowsNpcDead()[3]){
                                    spawn(spawn1, SOUTH );
                                    spawn(spawn2, SOUTH );
                                    spawn(spawn3, SOUTH );
                                }
                                else if(!player.getBarrowsNpcDead()[3]){
                                    spawn(spawn1, SOUTH );
                                    spawn(spawn2, SOUTH );
                                    NpcLoader.spawnNpc(player, new Npc(2028), true, true);
                                }
                                southSpawn = false;
                            }
                        }
                    return true;
                    case 6729: //South to southwest tunnel 2
                        if(x == 3545 && y == 9677) {
                            player.teleport(SouthWest);
                            if(southWestSpawn == true){
                                spawn(spawn1, SOUTHWEST );
                                spawn(spawn2, SOUTHWEST );
                                spawn(spawn3, SOUTHWEST );
                                southWestSpawn = false;
                            }
                        }
                        else if(x == 3541 && y == 9678) {
                            player.teleport(South);
                            if(southSpawn == true){
                                if(player.getBarrowsNpcDead()[3]){
                                    spawn(spawn1, SOUTH );
                                    spawn(spawn2, SOUTH );
                                    spawn(spawn3, SOUTH );
                                }
                                else if(!player.getBarrowsNpcDead()[3]){
                                    spawn(spawn1, SOUTH );
                                    spawn(spawn2, SOUTH );
                                    NpcLoader.spawnNpc(player, new Npc(2028), true, true);
                                }
                                southSpawn = false;
                            }
                        }
                    return true;
                       
                    case 6745: //Southwest to West tunnel
                        if(x == 3535 && y == 9684) {
                            player.teleport(West);
                            if(westSpawn == true){
                                if(player.getBarrowsNpcDead()[1]){
                                    spawn(spawn1, WEST );
                                    spawn(spawn2, WEST );
                                    spawn(spawn3, WEST );
                                }
                                else if(!player.getBarrowsNpcDead()[1]){
                                    spawn(spawn1, WEST );
                                    spawn(spawn2, WEST );
                                    NpcLoader.spawnNpc(player, new Npc(2026), true, true);
                                }
                                westSpawn = false;
                            }
                        }
                        else if(x == 3534 && y == 9688){
                            player.teleport(SouthWest);
                            if(southWestSpawn == true){
                                spawn(spawn1, SOUTHWEST );
                                spawn(spawn2, SOUTHWEST );
                                spawn(spawn3, SOUTHWEST );
                                southWestSpawn = false;
                            }
                        }
                    return true;
                    case 6726: //Southwest to West tunnel 2
                        if(x == 3534 && y == 9684) {
                            player.teleport(West);
                            if(westSpawn == true){
                                if(player.getBarrowsNpcDead()[1]){
                                    spawn(spawn1, WEST );
                                    spawn(spawn2, WEST );
                                    spawn(spawn3, WEST );
                                }
                                else if(!player.getBarrowsNpcDead()[1]){
                                    spawn(spawn1, WEST );
                                    spawn(spawn2, WEST );
                                    NpcLoader.spawnNpc(player, new Npc(2026), true, true);
                                }
                                westSpawn = false;
                            }
                        }
                        else if(x == 3535 && y == 9688) {
                            player.teleport(SouthWest);
                            if(southWestSpawn == true){
                                spawn(spawn1, SOUTHWEST );
                                spawn(spawn2, SOUTHWEST );
                                spawn(spawn3, SOUTHWEST );
                                southWestSpawn = false;
                            }
                        }
                    return true;
                       
                    case 6737: //West to NorthWest tunnel
                        if(x == 3535 && y == 9701) {
                            player.teleport(NorthWest);
                            if(northWestSpawn == true){
                                if(player.getBarrowsNpcDead()[4]){
                                    spawn(spawn1, NORTHWEST );
                                    spawn(spawn2, NORTHWEST );
                                    spawn(spawn3, NORTHWEST );
                                }
                                else if(!player.getBarrowsNpcDead()[4]){
                                    spawn(spawn1, NORTHWEST );
                                    spawn(spawn2, NORTHWEST );
                                    NpcLoader.spawnNpc(player, new Npc(2029), true, true);
                                }
                                northWestSpawn = false;
                            }
                        }
                        else if(x == 3534 && y == 9705){
                            player.teleport(West);
                            if(westSpawn == true){
                                if(player.getBarrowsNpcDead()[1]){
                                    spawn(spawn1, WEST );
                                    spawn(spawn2, WEST );
                                    spawn(spawn3, WEST );
                                }
                                else if(!player.getBarrowsNpcDead()[1]){
                                    spawn(spawn1, WEST );
                                    spawn(spawn2, WEST );
                                    NpcLoader.spawnNpc(player, new Npc(2026), true, true);
                                }
                                westSpawn = false;
                            }
                        }
                    return true;
                    case 6718: //West to NorthWest tunnel 2
                        if(x == 3534 && y == 9701) {
                            player.teleport(NorthWest);
                            if(northWestSpawn == true){
                                if(player.getBarrowsNpcDead()[4]){
                                    spawn(spawn1, NORTHWEST );
                                    spawn(spawn2, NORTHWEST );
                                    spawn(spawn3, NORTHWEST );
                                }
                                else if(!player.getBarrowsNpcDead()[4]){
                                    spawn(spawn1, NORTHWEST );
                                    spawn(spawn2, NORTHWEST );
                                    NpcLoader.spawnNpc(player, new Npc(2029), true, true);
                                }
                                northWestSpawn = false;
                            }
                        }
                        else if(x == 3535 && y == 9705) {
                            player.teleport(West);
                            if(westSpawn == true){
                                if(player.getBarrowsNpcDead()[1]){
                                    spawn(spawn1, WEST );
                                    spawn(spawn2, WEST );
                                    spawn(spawn3, WEST );
                                }
                                else if(!player.getBarrowsNpcDead()[1]){
                                    spawn(spawn1, WEST );
                                    spawn(spawn2, WEST );
                                    NpcLoader.spawnNpc(player, new Npc(2026), true, true);
                                }
                                westSpawn = false;
                            }
                        }
                    return true;
                       
                    case 6738: //Northwest to North tunnel
                        if(x == 3541 && y == 9711) {
                            player.teleport(North);
                            if(northSpawn == true){
                                if(player.getBarrowsNpcDead()[0]){
                                    spawn(spawn1, NORTH );
                                    spawn(spawn2, NORTH );
                                    spawn(spawn3, NORTH );
                                }
                                else if(!player.getBarrowsNpcDead()[0]){
                                    spawn(spawn1, NORTH );
                                    spawn(spawn2, NORTH );
                                    NpcLoader.spawnNpc(player, new Npc(2025), true, true);
                                }
                                northSpawn = false;
                            }
                        }
                        else if(x == 3545 && y == 9712){
                            player.teleport(NorthWest);
                            if(northWestSpawn == true){
                                if(player.getBarrowsNpcDead()[4]){
                                    spawn(spawn1, NORTHWEST );
                                    spawn(spawn2, NORTHWEST );
                                    spawn(spawn3, NORTHWEST );
                                }
                                else if(!player.getBarrowsNpcDead()[4]){
                                    spawn(spawn1, NORTHWEST );
                                    spawn(spawn2, NORTHWEST );
                                    NpcLoader.spawnNpc(player, new Npc(2029), true, true);
                                }
                                northWestSpawn = false;
                            }
                        }
                    return true;
                    case 6719: //Northwest to north tunnel 2
                        if(x == 3541 && y == 9712) {
                            player.teleport(North);
                            if(northSpawn == true){
                                if(player.getBarrowsNpcDead()[0]){
                                    spawn(spawn1, NORTH );
                                    spawn(spawn2, NORTH );
                                    spawn(spawn3, NORTH );
                                }
                                else if(!player.getBarrowsNpcDead()[0]){
                                    spawn(spawn1, NORTH );
                                    spawn(spawn2, NORTH );
                                    NpcLoader.spawnNpc(player, new Npc(2025), true, true);
                                }
                                northSpawn = false;
                            }
                        }
                        else if(x == 3545 && y == 9711) {
                            player.teleport(NorthWest);
                            if(northWestSpawn == true){
                                if(player.getBarrowsNpcDead()[4]){
                                    spawn(spawn1, NORTHWEST );
                                    spawn(spawn2, NORTHWEST );
                                    spawn(spawn3, NORTHWEST );
                                }
                                else if(!player.getBarrowsNpcDead()[4]){
                                    spawn(spawn1, NORTHWEST );
                                    spawn(spawn2, NORTHWEST );
                                    NpcLoader.spawnNpc(player, new Npc(2029), true, true);
                                }
                                northWestSpawn = false;
                            }
                        }
                    return true;
                       
                    case 6740: //North to Northeast tunnel
                        if(x == 3558 && y == 9711) {
                            player.teleport(NorthEast);
                            if(northEastSpawn == true){
                                spawn(spawn1, NORTHEAST );
                                spawn(spawn2, NORTHEAST );
                                spawn(spawn3, NORTHEAST );
                                northEastSpawn = false;
                            }
                        }
                        else if(x == 3562 && y == 9712){
                            player.teleport(North);
                            if(northSpawn == true){
                                if(player.getBarrowsNpcDead()[0]){
                                    spawn(spawn1, NORTH );
                                    spawn(spawn2, NORTH );
                                    spawn(spawn3, NORTH );
                                }
                                else if(!player.getBarrowsNpcDead()[0]){
                                    spawn(spawn1, NORTH );
                                    spawn(spawn2, NORTH );
                                    NpcLoader.spawnNpc(player, new Npc(2025), true, true);
                                }
                                northSpawn = false;
                            }
                        }
                    return true;
                    case 6721: //North to northeast tunnel 2
                        if(x == 3558 && y == 9712) {
                            player.teleport(NorthEast);
                            if(northEastSpawn == true){
                                spawn(spawn1, NORTHEAST);
                                spawn(spawn2, NORTHEAST);
                                spawn(spawn3, NORTHEAST);
                                northEastSpawn = false;
                            }
                        }
                        else if(x == 3562 && y == 9711) {
                            player.teleport(North);
                            if(northSpawn == true){
                                if(player.getBarrowsNpcDead()[0]){
                                    spawn(spawn1, NORTH );
                                    spawn(spawn2, NORTH );
                                    spawn(spawn3, NORTH );
                                }
                                else if(!player.getBarrowsNpcDead()[0]){
                                    spawn(spawn1, NORTH );
                                    spawn(spawn2, NORTH );
                                    NpcLoader.spawnNpc(player, new Npc(2025), true, true);
                                }
                                northSpawn = false;
                            }
                        }
                    return true;
                }
        return false;
                   
    }
	
	public static void spawn(int npcId, MinigameAreas.Area area) {
		Npc npc = new Npc(npcId);
		Position position = MinigameAreas.randomPosition(area);
		npc.setPosition(position);
		npc.setSpawnPosition(position);
		npc.setCurrentX(position.getX());
		npc.setCurrentY(position.getY());
		World.register(npc);
	}

	private static int brotherKillCount(Player player) {
		int brotherKillCount = 0;
		for (boolean kill : player.getBarrowsNpcDead()) {
			if (kill) {
				brotherKillCount++;
			}
		}
		return brotherKillCount;
	}

	public static void resetBarrows(Player player) {
		for (int x = 0; x < 6; x++) {
			player.setBarrowsNpcDead(x, false);
		}
		player.setKillCount(0);
		player.setRandomGrave(Misc.random(5));
		player.getActionSender().resetCamera();
        northSpawn = true;
        westSpawn = true;
        eastSpawn = true;
        southSpawn = true;
        northWestSpawn = true;
        northEastSpawn = true;
        southWestSpawn = true;
        southEastSpawn = true;
	}

	public static void handleDeath(Player player, Npc npc) {
		BarrowsBrother brother = BarrowsBrother.forNpcId(npc.getNpcId());
		if(brother != null)
		{
			player.setBarrowsNpcDead(brother.index, true);
			if(player.getSpawnedNpc() == npc)
			{
				player.setSpawnedNpc(null);
			}
			player.setKillCount(player.getKillCount() + 1);
		}
		if(monsterDeath(player, npc) ) {
			player.setKillCount(player.getKillCount() + 1);
		}
		player.getActionSender().sendString("Kill count: " + player.getKillCount(), 4536);
	}
	
	public static boolean monsterDeath(Player player, Npc npc) {
		if(npc.getNpcId() == 2031) return true;
		else if(npc.getNpcId() == 2033) return true;
		else if(npc.getNpcId() == 2034) return true;
		else if(npc.getNpcId() == 2035) return true;
		else if(npc.getNpcId() == 2037) return true;
		else return false; 
	}
}
