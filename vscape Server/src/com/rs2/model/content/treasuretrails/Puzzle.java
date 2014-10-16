package com.rs2.model.content.treasuretrails;

import java.util.ArrayList;

import com.rs2.model.Position;
import com.rs2.model.players.Player;
import com.rs2.model.players.container.inventory.Inventory;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 03/01/12 Time: 22:01 To change
 * this template use File | Settings | File Templates.
 */
public class Puzzle {// todo maybe hovering button message

	/* a variable which stocks the puzzle items */

	public static ArrayList<Integer> puzzleArray = new ArrayList<Integer>(ClueScroll.PUZZLE_LENGTH);

	/* the puzzle index */

	public static int index;

	/* load the main puzzle */

	public static boolean loadClueInterface(Player player, int itemId) {
		if (getIndexByItem(itemId) == 0)
		    return false;
		/*loadPuzzleArray(getIndexByItem(itemId));
		index = getIndexByItem(itemId);
		loadPuzzleItems(player);*/
		loadPuzzle(player);
		if(finishedPuzzle(player)) {
		    player.getActionSender().sendMessage("You have completed this puzzle!");
		    return true;
		}
		return true;
	}
	
	public static boolean initPuzzle(Player player, int itemId) {
		if (getIndexByItem(itemId) == 0)
		    return false;
		loadPuzzleArray(getIndexByItem(itemId));
		index = getIndexByItem(itemId);
		loadPuzzleItems(player);
		return true;
	}

	/* add the puzzle items to an arrayList */

	public static void loadPuzzleArray(int index) {
		for (int i = 0; i < ClueScroll.PUZZLE_LENGTH; i++) {
			puzzleArray.add(getPuzzleIndex(index)[i]);
		}
	}

	/* make an arrayList with the items put randomly */

	public static ArrayList<Integer> randomPuzzle() {
		ArrayList<Integer> array = new ArrayList<Integer>(ClueScroll.PUZZLE_LENGTH);

		while (puzzleArray.size() > 0) {
			int number = Misc.randomMinusOne(puzzleArray.size());
			array.add(puzzleArray.get(number));
			puzzleArray.remove(number);
		}
		return array;

	}

	/* gets the index with an item id provided */

	public static int getIndexByItem(int itemId) {
		switch (itemId) {
			case ClueScroll.CASTLE_PUZZLE :
				return 1;
			case ClueScroll.TREE_PUZZLE :
				return 2;
			case ClueScroll.TROLL_PUZZLE :
				return 3;
		}
		return 0;
	}

	/* gets the puzzle items with index provided */

	public static int[] getPuzzleIndex(int index) {
		switch (index) {
			case 1 :
				return ClueScroll.firstPuzzle;
			case 2 :
				return ClueScroll.secondPuzzle;
			case 3 :
				return ClueScroll.thirdPuzzle;
		}
		return null;
	}
	
	public static void resetPuzzleItems(Player player) {
	    for(int i = 0; i < player.puzzleStoredItems.length; i++) {
		player.puzzleStoredItems[i] = new Item(-1);
	    }
	}
	/* loading the puzzle items */
	public static void loadPuzzleItems(Player player) {
		ArrayList<Integer> array = randomPuzzle();
		boolean samePuzzle = false;

		/* checks is the puzzle clicked is the current one */

		for (int i = 0; i < ClueScroll.PUZZLE_LENGTH; i++) {
			if (player.puzzleStoredItems[i] != null)
				if (player.puzzleStoredItems[i].getId() != -1 && array.contains(player.puzzleStoredItems[i].getId()))
					samePuzzle = true;
		}
		/* does not recreate a random puzzle when player open the same puzzle */

		if (!samePuzzle)
			for (int i = 0; i < ClueScroll.PUZZLE_LENGTH; i++) {
				player.puzzleStoredItems[i] = new Item(array.get(i));
			}
	}

	/* getting the solved puzzle item for hint */

	public static Item[] getDefaultItems() {
		Item[] item = new Item[ClueScroll.PUZZLE_LENGTH];
		for (int i = 0; i < ClueScroll.PUZZLE_LENGTH; i++) {
			item[i] = new Item(getPuzzleIndex(index)[i]);
		}
		return item;
	}

	/* loading puzzle interface etc */

	public static void loadPuzzle(Player player) {
		player.getActionSender().sendInterface(ClueScroll.PUZZLE_INTERFACE);
		player.getActionSender().sendUpdateItems(ClueScroll.PUZZLE_INTERFACE_CONTAINER, player.puzzleStoredItems);
		player.getActionSender().sendUpdateItems(ClueScroll.PUZZLE_INTERFACE_DEFAULT_CONTAINER, getDefaultItems());

	}

	/* gets the position of a puzzle slide (using mathematical way) */

	public static Position getPosition(Player player, int itemId) {
		int x = 0, y = 0;
		for (int i = 0; i < player.puzzleStoredItems.length; i++) {
			if (player.puzzleStoredItems[i] != null)
				if (player.puzzleStoredItems[i].getId() == itemId) {
					x = i - 5 * (i / 5) + 1;
					y = i / 5 + 1;
				}
		}
		return new Position(x, y);
	}

	/* gets the position of the blank square */

	public static Position getBlankPosition(Player player) {
		return getPosition(player, -1);
	}

	/* checks if the clicked slide is surrounded by a blank square */

	public static boolean surroundedByBlank(Player player, Position position) {
		Position left = new Position(position.getX() - 1, position.getY(), 0);
		Position right = new Position(position.getX() + 1, position.getY(), 0);
		Position up = new Position(position.getX(), position.getY() - 1, 0);
		Position down = new Position(position.getX(), position.getY() + 1, 0);
		if (getBlankPosition(player).equals(left) || getBlankPosition(player).equals(right) || getBlankPosition(player).equals(up) || getBlankPosition(player).equals(down))
			return true;

		return false;
	}

	/* algorithm which saved me 50 lines of codes */

	public static int distanceToPiece(Position reference, Position point, String comp) {
		int x = reference.getX();
		int y = reference.getY();
		int x1 = point.getX();
		int y1 = point.getY();
		Position referencePos = new Position(x, y, 0);
		Position pointPos = new Position(x1, y1, 0);
		int counter = 0;
		int counter2 = 0;
		while (referencePos.getX() != pointPos.getX()) {
			if (x1 < x) {
				x1++;
				counter++;
			}
			if (x1 > x) {
				x1--;
				counter++;
			}
			pointPos.setX(x1);
		}
		while (referencePos.getY() != pointPos.getY()) {
			if (y1 < y) {
				y1++;
				counter2++;
			}
			if (y1 > y) {
				y1--;
				counter2++;
			}
			pointPos.setY(y1);
		}
		if (comp == "x")
			return counter;
		else if (comp == "y")
			return counter2;
		else
			return counter + counter2;
	}

	public static int distanceToPiece(Position reference, Position point) {
		return distanceToPiece(reference, point, "");
	}

	/* moves the slide piece */

	public static boolean moveSlidingPiece(Player player, int itemId) {
		if (Puzzle.getPosition(player, itemId).equals(new Position(0, 0, 0)) || Puzzle.getPosition(player, itemId) == null)
			return false;

		Position position = getPosition(player, itemId);
		Position blankPosition = getBlankPosition(player);

		if (Puzzle.surroundedByBlank(player, getPosition(player, itemId))) {
			swapWithBlank(player, Puzzle.getPosition(player, itemId));
			return true;
		}

		ArrayList<Position> nearPieces = new ArrayList<Position>(2);

		/* loop to gather the square that surround the blank one */

		for (int i = 0; i < player.puzzleStoredItems.length; i++) {
			Position thisPuzzlePosition = getPosition(player, player.puzzleStoredItems[i].getId());
			if (surroundedByBlank(player, thisPuzzlePosition) && distanceToPiece(blankPosition, position) >= distanceToPiece(position, thisPuzzlePosition))
				nearPieces.add(thisPuzzlePosition);
		}

		/* loop for the main algorithm */

		for (int i = 0; i < player.puzzleStoredItems.length; i++) {
			ArrayList<Integer> comp = new ArrayList<Integer>(4);
			Position thisPuzzlePosition = getPosition(player, player.puzzleStoredItems[i].getId());

			if (!thisPuzzlePosition.equals(blankPosition) && distanceToPiece(blankPosition, position) >= distanceToPiece(position, thisPuzzlePosition)) {

				/* loop to add the x and y distance to the clicked sliding piece */

				for (int j = 0; j < nearPieces.size(); j++) {
					comp.add(distanceToPiece(position, nearPieces.get(j), "x"));
					comp.add(distanceToPiece(position, nearPieces.get(j), "y"));
				}

				if (surroundedByBlank(player, thisPuzzlePosition)) {
					/*
					 * if one of the distance reaches the max value of comp
					 * table, then we move it
					 */

					if (maxValue(comp) == distanceToPiece(position, thisPuzzlePosition, "x") || maxValue(comp) == distanceToPiece(position, thisPuzzlePosition, "y")) {
						swapWithBlank(player, thisPuzzlePosition);
						return true;
					}

				}

			}
		}
		if(finishedPuzzle(player)) {
		    player.getActionSender().sendMessage("You have completed the puzzle!");
		}
        return true;

	}

	/* checks if the puzzle is solved */

	public static boolean finishedPuzzle(Player player) {
		if (player.puzzleStoredItems == null || player.puzzleStoredItems.length == 0) {
			return false;
		}
		
		int counter = 0;
		for (int i = 0; i < player.puzzleStoredItems.length; i++) {
			if (player.puzzleStoredItems[i] != null && player.puzzleStoredItems[i].getId() == getPuzzleIndex(index)[i]) {
				counter++;
			}
		}
		
		if (counter == player.puzzleStoredItems.length) {
		    return true;
		}
		
		return false;
	}

	/*
	 * swap the clicked sliding piece with the blank one : in other word, moves
	 * the sliding piece
	 */

	private static void swapWithBlank(Player player, Position position) {
		int index1 = 0;
		int index2 = 0;
		for (int i = 0; i < player.puzzleStoredItems.length; i++) {
			if (player.puzzleStoredItems[i].getId() == -1)
				index1 = i;
			if (getPosition(player, player.puzzleStoredItems[i].getId()).equals(position))
				index2 = i;
		}
		Item blank = player.puzzleStoredItems[index1];
		Item chosen = player.puzzleStoredItems[index2];
		player.puzzleStoredItems[index1] = chosen;
		player.puzzleStoredItems[index2] = blank;
		loadPuzzle(player);

	}

	public static int maxValue(ArrayList<Integer> val) {
		int value = val.get(0);
		for (int i = 0; i < val.size(); i++) {
			if (val.get(i) >= value)
				value = val.get(i);

		}
		return value;
	}

	public static void addRandomPuzzle(Player player) {
		int[] items = {2800, 3565, 3571};
		int item = items[Misc.randomMinusOne(items.length)];
		player.getInventory().addItem(new Item(item));
		initPuzzle(player, item);
	}
	
	public static boolean playerHasPuzzle(Player player) {
	    Inventory i = player.getInventory();
	    return i.playerHasItem(new Item(ClueScroll.CASTLE_PUZZLE)) || i.playerHasItem(new Item(ClueScroll.TREE_PUZZLE)) || i.playerHasItem(new Item(ClueScroll.TROLL_PUZZLE));
	}
}
