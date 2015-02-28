package com.rs2.model.npcs.drop;

import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;

/**
 * Represents a single npc drop.
 * 
 * @author Brown.
 */

public class NpcDropItem {

	/**
	 * The id.
	 */
	private int id;

	/**
	 * Array holding all the amounts of this item.
	 */
	private final int[] count;

	/**
	 * The chance of getting this item.
	 */
	private final int chance;

	public NpcDropItem(int i, int[] js, int chance) {
		this.id = i;
		this.count = js;
		this.chance = chance;
	}

	/**
	 * Gets an item, with a random amount based on the array.
	 * 
	 * @return An item, with a random amount and the id of this class.
	 */
	public Item getItem() {
		Item toReturn = new Item(this.getId(), 1);
		if (this.getCount().length == 1) {
			toReturn = new Item(this.getId(), this.getCount()[0]);
		} else if (this.getCount().length == 2) {
			toReturn = new Item(this.getId(), Misc.random(this.getCount()[1] - this.getCount()[0]) + this.getCount()[0]);
		} else {
			toReturn = new Item(this.getId(), this.getCount()[Misc.randomMinusOne(this.getCount().length)]);
		}
		if (this.getId() >= 1438 && this.getId() <= 1462 && !toReturn.getDefinition().isNoted()) { //Talismans never giving more than one unnoted
			toReturn = new Item(this.getId(), 1);
		}
		return toReturn;
	}

	/**
	 * Gets the item id.
	 * 
	 * @return The item id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the item id.
	 * 
	 * @return The item id.
	 */
	public NpcDropItem setNoted() {
		this.id++;
		return this;
	}

	/**
	 * Indicates if this item always drops.
	 * 
	 * @return true if, false if not.
	 */
	public boolean shouldAlwaysDrop() {
		return chance == 1;
	}

	/**
	 * Gets the chance.
	 * 
	 * @return The chance.
	 */
	public int[] getCount() {
		return count;
	}

	/**
	 * Gets the chance.
	 * 
	 * @return The chance.
	 */
	public int getChance() {
		return chance;
	}

}
