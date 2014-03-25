package com.rs2.model.players.item;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import com.rs2.Constants;
import com.rs2.util.XStreamUtil;

/**
 * The item definition manager.
 * 
 * @author Vastico
 * @author Graham Edgecombe
 * 
 */
public class ItemDefinition {

	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(ItemDefinition.class.getName());

	/**
	 * The definition array.
	 */
	private static ItemDefinition[] definitions = new ItemDefinition[Constants.MAX_ITEMS + 1];

	private static double[] weights = new double[Constants.MAX_ITEMS + 2];

	/**
	 * Gets a definition for the specified id.
	 * 
	 * @param id
	 *            The id.
	 * @return The definition.
	 */
	public static ItemDefinition forId(int id) {
		if (id < 0) {
			id = 1;
		}
		ItemDefinition def = definitions[id];
		if (def == null) {
			def = new ItemDefinition(id, "# + id", "It's an item!", "NONE", false, false, false, -1, -1, true, 0, 0, 0, 0, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
		}
		return def;
	}

	/**
	 * Loads the item definitions.
	 * 
	 * @throws java.io.IOException
	 *             if an I/O error occurs.
	 * @throws IllegalStateException
	 *             if the definitions have been loaded already.
	 */
	@SuppressWarnings("unchecked")
	public static void init() throws IOException {
		try {
			List<ItemDefinition> defs = (List<ItemDefinition>) XStreamUtil.getxStream().fromXML(new FileInputStream("./data/content/itemDefinitions.xml"));
			for (int i = 0; i < Constants.MAX_ITEMS; i++) {// ItemDefinition def : defs) {
				definitions[defs.get(i).getId()] = defs.get(i).addSlot().addTwoHanded().addShopPrice().addUntradable();
			}
			System.out.println("Loaded " + definitions.length + " item definitions");
		} catch (IOException e) {
			logger.warning("Failed to initialize the item definitions: " + e);
		}
	}

	/* load the item weight lists */
	public static void loadWeight() {
		try {
			Scanner s = new Scanner(new File("./data/content/weight.txt"));
			while (s.hasNextLine()) {
				String[] line = s.nextLine().split(" ");
				weights[Integer.parseInt(line[0])] = Double.parseDouble(line[1]);
			}
			System.out.println("Loaded " + weights.length + " item weights");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Id.
	 */
	private final int id;

	/**
	 * Name.
	 */
	private final String name;

	/**
	 * Description.
	 */
	private final String examine;

	/**
	 * Noted flag.
	 */
	private final boolean noted;

	/**
	 * Noteable flag.
	 */
	private final boolean noteable;

	/**
	 * Stackable flag.
	 */
	private final boolean stackable;

	/**
	 * Non-noted id.
	 */
	private final int parentId;

	/**
	 * Noted id.
	 */
	private final int notedId;

	/**
	 * Members flag.
	 */
	private final boolean members;

	/**
	 * The special store price.
	 */
	private final int specialStorePrice;

	/**
	 * Shop value.
	 */
	private final int generalStorePrice;

	/**
	 * High alc value.
	 */
	private final int highAlcValue;

	/**
	 * Low alc value.
	 */
	private final int lowAlcValue;

	/**
	 * Bonus values.
	 */
	private final int[] bonus;

	/**
	 * Equipment type
	 */
	private final String equipmentType;

	/**
	 * Equipment type
	 */
	private int getSlot;

	/**
	 * Two-handed weapon
	 */
	private boolean twoHanded;

	/**
	 * Shop price
	 */
	private int shopPrice;

	/**
	 * Untradable
	 */
	private boolean untradable;

	/**
	 * Creates the item definition.
	 * 
	 * @param id
	 *            The id.
	 * @param name
	 *            The name.
	 * @param examine
	 *            The description.
	 * @param noted
	 *            The noted flag.
	 * @param noteable
	 *            The noteable flag.
	 * @param stackable
	 *            The stackable flag.
	 * @param parentId
	 *            The non-noted id.
	 * @param notedId
	 *            The noted id.
	 * @param members
	 *            The members flag. The shop price.
	 * @param highAlcValue
	 *            The high alc value.
	 * @param lowAlcValue
	 *            The low alc value.
	 */
	private ItemDefinition(int id, String name, String examine, String equipmentType, boolean noted, boolean noteable, boolean stackable, int parentId, int notedId, boolean members, int specialStorePrice, int generalStorePrice, int highAlcValue, int lowAlcValue, int[] bonus) {
		this.id = id;
		this.name = name;
		this.examine = examine;
		this.equipmentType = equipmentType;
		this.noted = noted;
		this.noteable = noteable;
		this.stackable = stackable;
		this.parentId = parentId;
		this.notedId = notedId;
		this.members = members;
		this.specialStorePrice = specialStorePrice;
		this.generalStorePrice = generalStorePrice;
		this.highAlcValue = highAlcValue;
		this.lowAlcValue = lowAlcValue;
		this.bonus = bonus;
		this.getSlot = getSlot(equipmentType);
		this.twoHanded = isTwoHanded(name);
		this.shopPrice = (int) Math.ceil(highAlcValue * (1.666666666666));
		this.untradable = isUntradable(id, stackable, noteable);
	}

	public ItemDefinition addSlot() {
		this.getSlot = getSlot(this.equipmentType);
		return this;
	}

	public ItemDefinition addTwoHanded() {
		this.twoHanded = isTwoHanded(this.name);
		return this;
	}

	public ItemDefinition addUntradable() {
		this.untradable = isUntradable(this.id, this.stackable, this.noteable);
		return this;
	}

	private ItemDefinition addShopPrice() {
		this.shopPrice = (int) Math.ceil(highAlcValue * (1.666666666666));
		return this;
	}

	/**
	 * Gets the id.
	 * 
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the name.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the description.
	 * 
	 * @return The description.
	 */
	public String getDescription() {
		return examine;
	}

	/**
	 * Gets the noted flag.
	 * 
	 * @return The noted flag.
	 */
	public boolean isNoted() {
		return noted;
	}

	/**
	 * Gets the noteable flag.
	 * 
	 * @return The noteable flag.
	 */
	public boolean isNoteable() {
		return noteable;
	}

	/**
	 * Gets the stackable flag.
	 * 
	 * @return The stackable flag.
	 */
	public boolean isStackable() {
		return stackable || noted;
	}

	/**
	 * Gets the normal id.
	 * 
	 * @return The normal id.
	 */
	public int getNormalId() {
		return parentId;
	}

	/**
	 * Gets the noted id.
	 * 
	 * @return The noted id.
	 */
	public int getNotedId() {
		return notedId;
	}

	/**
	 * Gets the members only flag.
	 * 
	 * @return The members only flag.
	 */
	public boolean isMembersOnly() {
		return members;
	}

	/**
	 * Gets the value.
	 * 
	 * @return The value.
	 */
	public int getPrice() {
		int price = generalStorePrice;
		if (this.isNoted() && parentId != -1 && parentId != id) {
			price = forId(parentId).getPrice();
		}
		if (price == 0) {
			return 1;
		}
		return price;
	}

	public int getSpecialStorePrice() {
		int price = specialStorePrice;
		if (this.isNoted() && parentId != -1 && parentId != id) {
			if (price < forId(parentId).getSpecialStorePrice()) {
				price = forId(parentId).getSpecialStorePrice();
			}
		}
		if (price == 0) {
			return 1;
		}
		return price;
	}

	public int getShopPrice() {
		if (noted && parentId != -1 && parentId != id) {
			if (shopPrice < forId(parentId).getShopPrice()) {
				shopPrice = forId(parentId).getShopPrice();
			}
		}
		return shopPrice;
	}

	/**
	 * Gets the low alc value.
	 * 
	 * @return The low alc value.
	 */
	public int getLowAlcValue() {
		if (lowAlcValue == 0) {
			if (noted && parentId != -1 && parentId != id) {
				return forId(parentId).getLowAlcValue();
			}
		}
		return lowAlcValue;
	}

	/**
	 * Gets the high alc value.
	 * 
	 * @return The high alc value.
	 */
	public int getHighAlcValue() {
		if (highAlcValue == 0) {
			if (noted && parentId != -1 && parentId != id) {
				return forId(parentId).getHighAlcValue();
			}
		}
		return highAlcValue;
	}

	/**
	 * Gets the array of bonuses.
	 * 
	 * @return The array of item bonuses.
	 */
	public int[] getBonuses() {
		return bonus;
	}

	/**
	 * Gets a specific bonus based on this item definition..
	 * 
	 * @return The bonus value
	 */
	public int getBonus(int id) {
		return bonus[id];
	}

	public static double getWeight(int itemId) {
		return weights[itemId + 1] + 0.1;
	}

	public String getEquipmentType() {
		return equipmentType;
	}

	public int getSlot() {
		return getSlot;
	}

	public boolean isTwoHanded() {
		return twoHanded;
	}

	public boolean isUntradable() {
		return untradable;
	}

    public boolean isUntradable(int id, boolean stackable, boolean noteable) {
    	if (!stackable && !noteable) {
    		return true;
    	}
        return Arrays.binarySearch(Constants.UNTRADEABLE_ITEMS, id) > -1;
    }

	public int getSlot(String equipmentType) {
		if (equipmentType.equals("HAT")) {
			return Constants.HAT;
		}
		if (equipmentType.equals("CAPE")) {
			return Constants.CAPE;
		}
		if (equipmentType.equals("AMULET")) {
			return Constants.AMULET;
		}
		if (equipmentType.equals("WEAPON")) {
			return Constants.WEAPON;
		}
		if (equipmentType.equals("BODY")) {
			return Constants.CHEST;
		}
		if (equipmentType.equals("SHIELD")) {
			return Constants.SHIELD;
		}
		if (equipmentType.equals("LEGS")) {
			return Constants.LEGS;
		}
		if (equipmentType.equals("GLOVES")) {
			return Constants.HANDS;
		}
		if (equipmentType.equals("BOOTS")) {
			return Constants.FEET;
		}
		if (equipmentType.equals("RING")) {
			return Constants.RING;
		}
		if (equipmentType.equals("ARROWS")) {
			return Constants.ARROWS;
		}
		return -1;
	}

	/**
	 * two handed weapon check
	 **/
	public static boolean isTwoHanded(String itemName) {
		itemName = itemName.toLowerCase();
		if (itemName.contains("ahrim") || itemName.contains("karil") || itemName.contains("verac") || itemName.contains("guthan") || itemName.contains("dharok") || itemName.contains("torag")) {
			return true;
		}
		if (itemName.contains("bow") && !itemName.contains("crossbow")) {
			return true;
		}
		if (itemName.contains("maul") || itemName.contains("claw") || itemName.contains("2h") || itemName.contains("halberd") || itemName.contains("spear")) {
			return true;
		}
		if (itemName.contains("seercull")) {
			return true;
		}
		if (itemName.contains("tzhaar-ket-om")) {
			return true;
		}
		if (itemName.contains("mjolnir")) {
			return true;
		}
		return false;
	}

	public static int getItemId(String name) {
		name = name.toLowerCase();
		for (ItemDefinition def : definitions) {
			if (def.getName().toLowerCase().equalsIgnoreCase(name)) {
				return def.getId();
			}
		}
		return 0;
	}

}
