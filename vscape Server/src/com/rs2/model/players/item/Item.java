package com.rs2.model.players.item;

import com.rs2.Constants;
import com.rs2.model.content.skills.farming.FarmingConstants;

/**
 * Represents a single item.
 * 
 * @author Graham Edgecombe
 * 
 */
public class Item {

	/**
	 * The id.
	 */
	private int id;

	/**
	 * The number of items.
	 */
	private int count;

	/* Assigning a timer to each items */

	private int timer;

	/**
	 * Creates a single item.
	 * 
	 * @param id
	 *            The id.
	 */
	public Item(int id) {
		this(id, 1);
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	/**
	 * Creates a stacked item.
	 * 
	 * @param id
	 *            The id.
	 * @param count
	 *            The number of items.
	 * @throws IllegalArgumentException
	 *             if count is negative.
	 */
	public Item(int id, int count) {
		if (count < 0) {
			throw new IllegalArgumentException("Count cannot be negative.");
		}
		this.id = id;
		this.count = count;
		this.timer = -1;
		for (int items : FarmingConstants.WATERED_SAPPLING)
			if (items == id)
				this.timer = 5;
		if (id == 1995)
			this.timer = 1;
	}

	/**
	 * Creates a stacked item.
	 * 
	 * @param id
	 *            The id.
	 * @param count
	 *            The number of items.
	 * @param timer
	 *            The timer assigned.
	 * @throws IllegalArgumentException
	 *             if count is negative.
	 */
	public Item(int id, int count, int timer) {
		if (count < 0) {
			throw new IllegalArgumentException("Count cannot be negative.");
		}
		this.id = id;
		this.count = count;
		this.timer = timer;
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
	 * Gets the count.
	 * 
	 * @return The count.
	 */
	public int getCount() {
		return count;
	}
	/**
	 * Gets the timer.
	 * 
	 * @return The timer.
	 */
	public int getTimer() {
		return timer;
	}

	public ItemDefinition getDefinition() {
		return ItemDefinition.forId(id);
	}

	@Override
	public String toString() {
		return Item.class.getName() + " [id=" + id + ", count=" + count + "]";
	}

	public boolean equals() {
		return this.getId() == id && count == this.getCount();
	}

	public boolean validItem() {
		return this.getId() > 0 && this.getId() <= Constants.MAX_ITEMS && this.getCount() > 0;
	}

	public boolean validEquipment() {
		return validItem() && this.getDefinition().getSlot() != -1;
	}

}
