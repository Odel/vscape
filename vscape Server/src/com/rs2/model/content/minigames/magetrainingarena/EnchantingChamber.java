package com.rs2.model.content.minigames.magetrainingarena;

import java.util.Random;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.Tick;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 01/01/12 Time: 00:47 To change
 * this template use File | Settings | File Templates.
 */
public class EnchantingChamber {
	// todo object on interface

	/* the default locations */

	private static final Position[] ENTERING_POSITION = {new Position(3373, 9640, 0), new Position(3374, 9639, 0), new Position(3375, 9640, 0), new Position(3374, 9641, 0), new Position(3362, 9650, 0), new Position(3363, 9651, 0), new Position(3364, 9650, 0), new Position(3363, 9649, 0), new Position(3363, 9628, 0), new Position(3364, 9629, 0), new Position(3363, 9630, 0), new Position(3362, 9629, 0), new Position(3354, 9640, 0), new Position(3353, 9639, 0), new Position(3352, 9640, 0), new Position(3353, 9641, 0)};

	private Player player;

	private int tempPizzazPoint;
	private int enchantPizzazPoint;
	private int[] enchantSpellsUsed = new int[6];

	public static Npc guardian() {
		for (Npc npc : World.getNpcs()) {
			if (npc == null)
				continue;
			if (npc.getDefinition().getId() == 3100)
				return npc;
		}
		return null;
	}

	public EnchantingChamber(Player player) {
		this.player = player;
	}

	/* Setting up the constant field */

	private static final String[] colour = {"red", "yellow", "green", "blue"};

	private static final int redItem = 6901;
	private static final int yellowItem = 6899;
	private static final int greenItem = 6898;
	private static final int blueItem = 6900;
	private static final int orb = 6902;
	private static final int dragonStone = 6903;
	private static final int removeall = 28;
	/* the item that give a bonus point */

	private static String bonusItem;

	/* the random instance */

	static Random random = new Random();

	/* loading the enchanting chamber part */

	public static void loadEnchantingChamber() {
		loadInitialVariables();
		loadEnchantingEvent();
	}

	/* loading initial variables */

	private static void loadInitialVariables() {
		bonusItem = "red";
	}

	/* loading the enchanting chamber event */

	private static void loadEnchantingEvent() {
		World.submit(new Tick(40) { // 24 seconds
			@Override
			public void execute() {
				bonusItem = colour[random.nextInt(colour.length)];
				for (Player player : World.getPlayers()) {
					if (player == null)
						continue;
					if (player.getEnchantingChamber().isInEnchantingChamber())
						player.getEnchantingChamber().showInterfaceComponent(bonusItem);
				}
				if (guardian() != null)
					guardian().getUpdateFlags().sendForceMessage("The color shape is now " + bonusItem + "!");

			}
		});
	}

	public boolean isInEnchantingChamber() {
		int x = player.getPosition().getX();
		int y = player.getPosition().getY();

		return player.getPosition().getZ() == 0 && x >= 3334 && x <= 3388 && y >= 9610 && y <= 9664;

	}

	/* loading interface for a single player */

	public void loadInterfacePlayer() {
		player.getActionSender().sendWalkableInterface(15917);
		player.getActionSender().sendString("" + (enchantPizzazPoint + tempPizzazPoint), 15921);
	}

	public void showInterfaceComponent(String bonusItem) {
		removeInterfaceComponent();
		if (bonusItem == "red")
			player.getActionSender().sendInterfaceHidden(0, 15924);
		else if (bonusItem == "yellow")
			player.getActionSender().sendInterfaceHidden(0, 15922);
		else if (bonusItem == "green")
			player.getActionSender().sendInterfaceHidden(0, 15923);
		else if (bonusItem == "blue")
			player.getActionSender().sendInterfaceHidden(0, 15925);
	}

	public void removeInterfaceComponent() {
		player.getActionSender().sendInterfaceHidden(1, 15924);
		player.getActionSender().sendInterfaceHidden(1, 15922);
		player.getActionSender().sendInterfaceHidden(1, 15923);
		player.getActionSender().sendInterfaceHidden(1, 15925);
	}

	/* handle object clicking packet */

	public boolean handleObjectClicking(int objectId) {
		switch (objectId) {
			case 10802 :// red pile
			case 10799 :// yellow pile
			case 10800 :// green pile
			case 10801 :// blue pile
				addCorrespondingItem(objectId);
				return true;
			case 10782 :// exiting object
				if (isInEnchantingChamber())
					exit();
				return false;
			case 10779 :// entering object
				enter();
				return true;
			case 10803 :// deposit orb object
				depositOrbs();
				return true;
		}
		return false;
	}

	/* adding the corresponding item for the object id */

	private void addCorrespondingItem(final int objectId) {
		if (player.getInventory().getItemContainer().freeSlots() <= 0) {
			player.getActionSender().sendMessage("Not enough space in your inventory.");
			return;
		}
		player.getUpdateFlags().sendAnimation(832, 0);
		player.getInventory().addItem(new Item(correspondingItem(objectId)));
	}

	/* gets the correspondint item for an object id */

	private static int correspondingItem(int objectId) {
		switch (objectId) {
			case 10802 :// red pile
				return redItem;
			case 10799 :// yellow pile
				return yellowItem;
			case 10800 :// green pile
				return greenItem;
			case 10801 :// blue pile
				return blueItem;
		}
		return 0;
	}

	/* gets the bonus item string with an id provided */

	private static String getStringById(int i) {
		if (i == redItem)
			return "red";
		if (i == yellowItem)
			return "yellow";
		if (i == greenItem)
			return "green";
		if (i == blueItem)
			return "blue";
		return "";
	}

	/* entering and leaving enchanting chamber */

	private void enter() {
		if (player.getSkill().getLevel()[Skill.MAGIC] < MageGameConstants.ENCHANTING_LEVEL) {
			player.getActionSender().sendMessage("You need a magic level of " + MageGameConstants.ENCHANTING_LEVEL + " to enter here.");
			return;
		}
		player.getActionSender().sendMessage("You've entered the Enchantment Chamber");
		int number = random.nextInt(ENTERING_POSITION.length);
		player.teleport(ENTERING_POSITION[number]);
	}

	private void exit() {
		player.getActionSender().sendMessage("You've left the Enchantment Chamber");
		player.getActionSender().sendWalkableInterface(-1);
		player.teleport(MageGameConstants.LEAVING_POSITION);
		removeItems();
		enchantPizzazPoint += tempPizzazPoint;
		tempPizzazPoint = 0;
		player.getActionSender().sendWalkableInterface(-1);
	}

	/* removing the minigame items */

	private void removeItems() {
		player.getInventory().removeItem(new Item(redItem, 28));
		player.getInventory().removeItem(new Item(yellowItem, 28));
		player.getInventory().removeItem(new Item(greenItem, 28));
		player.getInventory().removeItem(new Item(blueItem, 28));
		player.getInventory().removeItem(new Item(orb, 28));
		player.getInventory().removeItem(new Item(dragonStone, 28));
	}

	/* getting the points given with the spell id */

	private static int getPointsBySpell(int spellId) {
		switch (spellId) {
			case 1155 :// lv 1
				return 2;
			case 1165 :// lv 2
				return 4;
			case 1176 :// lv 3
				return 6;
			case 1180 :// lv 4
				return 8;
			case 1187 :// lv 5
				return 10;
			case 6003 :// lv 6
				return 12;
		}
		return 0;
	}

	/*
	 * getting the extra points given with the enchanting spell index --see
	 * Player.java for more info
	 */

	private static int getExtraPointsByIndex(int index) {
		switch (index) {
			case 0 :// lv 1
				return 1;
			case 1 :// lv 2
				return 2;
			case 2 :// lv 3
				return 3;
			case 3 :// lv 4
				return 4;
			case 4 :// lv 5
				return 5;
			case 5 :// lv 6
				return 6;
		}
		return 0;
	}

	/* getting the enchanting index */

	private static int getEnchantingIndex(int spellId) {
		return getPointsBySpell(spellId) / 2 - 1;
	}

	/* enchanting an item in the minigame */

	public void enchantItem(int spellId, int itemId) {
		/* checks if the item enchanted can be enchanted */
		if (itemId != redItem && itemId != yellowItem && itemId != greenItem && itemId != blueItem && itemId != dragonStone)
			return;

		/* dragonstone handling */

		if (itemId == dragonStone) {
			/* check if the max point is reached or not */

			if ((enchantPizzazPoint + tempPizzazPoint) <= MageGameConstants.MAX_ENCHANTING_POINT)

				tempPizzazPoint += getPointsBySpell(spellId);// Adding temporary
																// pizzaz point,
																// if the player
																// logout, he
																// loses them

			else
				tempPizzazPoint = MageGameConstants.MAX_ENCHANTING_POINT - enchantPizzazPoint;

		} else {

			/* if the player is enchanting the item shown as bonus */

			if (bonusItem == getStringById(itemId) && (enchantPizzazPoint + tempPizzazPoint) <= MageGameConstants.MAX_ENCHANTING_POINT) {
				player.getActionSender().sendMessage("You recieve 1 bonus point!");
				enchantPizzazPoint += 1;
			}

			/*
			 * extra points handling : win points depending on the enchanting
			 * point used: will be added when deposit orb
			 */

			enchantSpellsUsed[getEnchantingIndex(spellId)] += 1;
		}

		/* standard enchanting methods */
		player.getInventory().removeItem(new Item(itemId));
		player.getInventory().addItem(new Item(orb));

	}

	/* deposit the orbs into the hole */

	private void depositOrbs() {

		if (!player.getInventory().getItemContainer().contains(orb)) {
			player.getActionSender().sendMessage("You don't have any orbs to deposit.");
			return;
		}
		int count = player.getInventory().getItemContainer().getCount(orb);
		int extraPoints = 0;
		// checks if the max point is reached or not
		if ((enchantPizzazPoint + tempPizzazPoint) <= MageGameConstants.MAX_ENCHANTING_POINT) {
			tempPizzazPoint += Math.floor(count / 10) * 10;
			for (int i = 0; i < enchantSpellsUsed.length; i++)
				while (enchantSpellsUsed[i] - 10 >= 0) {
					enchantSpellsUsed[i] -= 10;
					tempPizzazPoint += getExtraPointsByIndex(i);
					extraPoints += getExtraPointsByIndex(i);
				}
		} else
			tempPizzazPoint = MageGameConstants.MAX_ENCHANTING_POINT - enchantPizzazPoint;
		player.getActionSender().sendChatInterface(359);
		player.getActionSender().sendString("You've just deposited " + count + " orbs, earning you " + (int) (Math.floor(count / 10) * 10) + " Enchanting Pizazz", 360);
		player.getActionSender().sendString("Points and " + extraPoints + " extra points for the enchanting spell used.", 361);
		resetEnchantingSpells();
		player.getInventory().removeItem(new Item(orb, removeall));
		player.getUpdateFlags().sendAnimation(832, 0);

	}

	/* reset the enchanting spells after depositing orbs */

	private void resetEnchantingSpells() {
		for (int i = 0; i < enchantSpellsUsed.length; i++)
			enchantSpellsUsed[i] = 0;
	}

}