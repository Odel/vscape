package com.rs2.model.npcs.drop;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;
//import com.rs2.util.XStreamUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Controls all the NPC drops in game.
 * 
 */

public class NpcDropController {
	

	/**
	 * The map containing all the npc drops. ;)
	 */
	private static Map<Integer, NpcDropController> dropControllers = null;
	
	public static void init() throws IOException {
		FileReader rareReader = new FileReader("./datajson/npcs/rareDrops.json");
		try
		{	
			if(rareTable != null){
				rareTable = null;
			}
			rareTable = new Gson().fromJson(rareReader, new TypeToken<NpcDropController>(){}.getType());
			rareReader.close();
			System.out.println("Loaded " + rareTable.drops.length + " rare drops.");
		} catch (IOException e) {
			rareReader.close();
			System.out.println("failed to load rareDrops json.");
		}
		
		FileReader reader = new FileReader("./datajson/npcs/npcDrops.json");
		try
		{	
			if(dropControllers != null && dropControllers.size() > 0){
				dropControllers.clear();
				dropControllers = null;
			}
			List<NpcDropController> list = new Gson().fromJson(reader, new TypeToken<List<NpcDropController>>(){}.getType());
			dropControllers = new HashMap<Integer, NpcDropController>();
			for (NpcDropController npcDrop : list) {
				dropControllers.put(npcDrop.getNpcId(), npcDrop);
			}
			reader.close();
			System.out.println("Loaded " + dropControllers.size() + " npc drops json.");
		} catch (IOException e) {
			reader.close();
			System.out.println("failed to load npc definitions json.");
		}
	}

	/**
	 * The logger instance.
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(NpcDropController.class.getName());

	/**
	 * The id's of the NPC's that "owns" this class.
	 */
	private int npcId;
	private boolean rareTableAccess = false;

	/**
	 * All the drops that belongs to this class.
	 */
	private NpcDropItem[] drops;
	private NpcDropItem[] common;
	private NpcDropItem[] uncommon;
	private NpcDropItem[] rare;
	private NpcDropItem[] specialrare;
	private NpcDropItem[] superrare;
	
	private static NpcDropController rareTable;
	private NpcDropItem[] rareTableCommon;
	private NpcDropItem[] rareTableUncommon;
	private NpcDropItem[] rareTableRare;
	private NpcDropItem[] rareTableSuperrare;

	private static int superChance = 200; //original 105
	private static int rareTableChance = 130;
	private static int specialRareChance = 128;
	private static int rareChance = 100; //original 45
	private static int uncommonChance = 20; //original 10

	/**
	 * Gets the NPC drop controller by an id.
	 * 
	 * @return The NPC drops associated with this id.
	 */
	public static NpcDropController forId(int id) {
		return dropControllers.get(id);
	}

	public NpcDropItem[] getDropList() {
		return drops;
	}
	
	public void setRareTableChance(boolean bool) {
	    if(bool) rareTableChance = 100;
	    else if(!bool) rareTableChance = 130;
	}

	/**
	 * Gets an array of all the items an NPC should drop. This will get 100%
	 * drops as well.
	 * 
	 * @return An array of the items this NPC should drop.
	 */
	public Item[] getDrops() {
		if (drops == null) {
			return null;
		}
		NpcDropItem drop = null;
		int length = 1;
		for (NpcDropItem item : drops) {
			if (item.shouldAlwaysDrop()) {
				length++;
			}
		}
		Item dropItem = null;
		if (superrare != null && Misc.random(superChance) == 0) {
			drop = superrare[Misc.randomMinusOne(superrare.length)];
		} else if (rareTable != null && Misc.random(rareTableChance) == 0) {
			if(rareTableAccess)
			{
				if (rareTableSuperrare != null && Misc.random(superChance) == 0) {
					drop = rareTableSuperrare[Misc.randomMinusOne(rareTableSuperrare.length)];
				}else if (rareTableRare != null && Misc.random(rareChance) == 0) {
					drop = rareTableRare[Misc.randomMinusOne(rareTableRare.length)];
				} else if (rareTableUncommon != null && Misc.random(uncommonChance) == 0) {
					drop = rareTableUncommon[Misc.randomMinusOne(rareTableUncommon.length)];
				} else if (rareTableCommon != null) {
					drop = rareTableCommon[Misc.randomMinusOne(rareTableCommon.length)];
				} else {
					length--;
				}
			} else {
				length--;
			}
		} else if (specialrare != null && Misc.random(specialRareChance) == 0) {
			drop = specialrare[Misc.randomMinusOne(specialrare.length)];
		} else if (rare != null && Misc.random(rareChance) == 0) {
			drop = rare[Misc.randomMinusOne(rare.length)];
		} else if (uncommon != null && Misc.random(uncommonChance) == 0) {
			drop = uncommon[Misc.randomMinusOne(uncommon.length)];
		} else if (common != null) {
			drop = common[Misc.randomMinusOne(common.length)];
		} else {
			length--;
		}
		if (drop != null) {
			dropItem = drop.getItem();
		}
		Item[] toReturn = new Item[length];
		int index = 0;
		for (NpcDropItem item : drops) {
			if (item.shouldAlwaysDrop()) {
				toReturn[index++] = item.getItem();
			}
		}
		if (dropItem != null) {
			toReturn[index] = dropItem;
		}
		return toReturn;
	}

	public void setDrop() {
		int c = 0, u = 0, r = 0, sr = 0, s = 0;
		for (NpcDropItem item : drops) {
			if (item.getChance() == 2) {
				c++;
			} else if (item.getChance() == 3) {
				u++;
			} else if (item.getChance() == 4 || item.getChance() == 6 || item.getChance() == 8) {
				r++;
			} else if (item.getChance() == 7 || item.getChance() == 9) {
				sr++;
			} else if (item.getChance() == 5) {
				s++;
			}
		}
		int c2 = 0, u2 = 0, r2 = 0, sr2 = 0, s2 = 0;
		common = c > 0 ? new NpcDropItem[c] : null;
		uncommon = u > 0 ? new NpcDropItem[u] : null;
		rare = r > 0 ? new NpcDropItem[r] : null;
		specialrare = sr > 0 ? new NpcDropItem[sr] : null;
		superrare = s > 0 ? new NpcDropItem[s] : null;
		for (NpcDropItem item : drops) {
			NpcDropItem drop = item;
			/*if ((drop.getCount().length > 1 || drop.getCount()[0] > 1) && !drop.getItem().getDefinition().isStackable() && drop.getItem().getDefinition().isNoteable()) {
				drop.setNoted();
			}*/
			if (item.getChance() == 2) {
				common[c2] = drop;
				c2++;
			} else if (item.getChance() == 3) {
				uncommon[u2] = drop;
				u2++;
			} else if (item.getChance() == 4 || item.getChance() == 6 || item.getChance() == 8) {
				rare[r2] = drop;
				r2++;
			} else if (item.getChance() == 7 || item.getChance() == 9) {
				specialrare[sr2] = drop;
				sr2++;
			} else if (item.getChance() == 5) {
				superrare[s2] = drop;
				s2++;
			}
		}
		if(rareTableAccess)
		{
			setRareTables();
		}
	}
	
	public void setRareTables() {
		int c = 0, u = 0, r = 0, s = 0;
		for (NpcDropItem item : rareTable.drops) {
			if (item.getChance() == 2) {
				c++;
			} else if (item.getChance() == 3) {
				u++;
			} else if (item.getChance() == 4 || item.getChance() == 6 || item.getChance() == 8) {
				r++;
			} else if (item.getChance() == 5) {
				s++;
			}
		}
		int c2 = 0, u2 = 0, r2 = 0, s2 = 0;
		rareTableCommon = c > 0 ? new NpcDropItem[c] : null;
		rareTableUncommon = u > 0 ? new NpcDropItem[u] : null;
		rareTableRare = r > 0 ? new NpcDropItem[r] : null;
		rareTableSuperrare = s > 0 ? new NpcDropItem[s] : null;
		for (NpcDropItem item : rareTable.drops) {
			NpcDropItem drop = item;
			if (item.getChance() == 2) {
				rareTableCommon[c2] = drop;
				c2++;
			} else if (item.getChance() == 3) {
				rareTableUncommon[u2] = drop;
				u2++;
			} else if (item.getChance() == 4 || item.getChance() == 6 || item.getChance() == 8) {
				rareTableRare[r2] = drop;
				r2++;
			} else if (item.getChance() == 5) {
				rareTableSuperrare[s2] = drop;
				s2++;
			}
		}
	}

	/**
	 * Gets the id's of the NPC's this class belongs to.
	 * 
	 * @return The id's of the NPC's.
	 */
	public int getNpcIds() {
		return npcId;
	}

	/**
	 * Gets the id's of the NPC's this class belongs to.
	 * 
	 * @return The id's of the NPC's.
	 */
	public int getNpcId() {
		return npcId;
	}
}
