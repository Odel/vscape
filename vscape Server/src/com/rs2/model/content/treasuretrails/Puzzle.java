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

    private Player player;
    /* a variable which stocks the puzzle items */

    public Puzzle(Player player) {
	this.player = player;
    }
    public ArrayList<Integer> puzzleArray = new ArrayList<Integer>(ClueScroll.PUZZLE_LENGTH);

    /* the puzzle index */
    public static int index;

    /* load the main puzzle */
    public boolean loadClueInterface(int itemId) {
	if (getIndexByItem(itemId) == 0) {
	    return false;
	}
	loadPuzzle();
	if (finishedPuzzle()) {
	    player.getActionSender().sendMessage("You have completed this puzzle!");
	    return true;
	}
	return true;
    }

    public boolean initPuzzle(int itemId) {
	index = getIndexByItem(itemId);
	if (index == 0) {
	    return false;
	}
	loadPuzzleArray(index);
	loadPuzzleItems();
	return true;
    }

    /* add the puzzle items to an arrayList */
    public void loadPuzzleArray(int index) {
	for (int i = 0; i < ClueScroll.PUZZLE_LENGTH; i++) {
	    puzzleArray.add(getPuzzleIndex(index)[i]);
	}
    }

    public int getBlockNumberForItemId(int itemId) {
	for (int i = 0; i < ClueScroll.PUZZLE_LENGTH; i++) {
	    if (itemId == -1) {
		return 0;
	    }
	    if (itemId == getPuzzleIndex(player.getPuzzle().index)[i]) {
		return i + 1;
	    }
	}
	return 0;
    }
    
    /* gets the index with an item id provided */
    public int getIndexByItem(int itemId) {
	switch (itemId) {
	    case ClueScroll.CASTLE_PUZZLE:
		return 1;
	    case ClueScroll.TREE_PUZZLE:
		return 2;
	    case ClueScroll.TROLL_PUZZLE:
		return 3;
	}
	return 0;
    }

    /* gets the puzzle items with index provided */
    public int[] getPuzzleIndex(int index) {
	switch (index) {
	    case 1:
		return ClueScroll.firstPuzzle;
	    case 2:
		return ClueScroll.secondPuzzle;
	    case 3:
		return ClueScroll.thirdPuzzle;
	}
	return null;
    }

    public void resetPuzzleItems() {
	for (int i = 0; i < player.puzzleStoredItems.length; i++) {
	    player.puzzleStoredItems[i] = new Item(-1);
	}
    }
    
    /* loading the puzzle items */
    public void loadPuzzleItems() {
	for (int i = 0; i < ClueScroll.PUZZLE_LENGTH; i++) {
	    player.puzzleStoredItems[i] = new Item(getPuzzleIndex(index)[i]);
	}
	for (int i = 0; i < 100; i++) {
	    int move = adjacentToBlank().get(Misc.randomMinusOne(adjacentToBlank().size()));
	    moveSlidingPiece(move, false);
	}
    }

    public ArrayList<Integer> adjacentToBlank() {
	ArrayList<Integer> toReturn = new ArrayList<>();
	for (int i = 0; i < ClueScroll.PUZZLE_LENGTH; i++) {
	    if (distanceToPiece(getPosition(player.puzzleStoredItems[i].getId()), getBlankPosition()) == 1) {
		toReturn.add(player.puzzleStoredItems[i].getId());
	    }
	}
	return toReturn;
    }
    
    /* getting the solved puzzle item for hint */
    public Item[] getDefaultItems() {
	Item[] item = new Item[ClueScroll.PUZZLE_LENGTH];
	for (int i = 0; i < ClueScroll.PUZZLE_LENGTH; i++) {
	    item[i] = new Item(getPuzzleIndex(index)[i]);
	}
	return item;
    }

    public void loadPuzzle() {
	player.getActionSender().sendInterface(ClueScroll.PUZZLE_INTERFACE);
	player.getActionSender().sendUpdateItems(ClueScroll.PUZZLE_INTERFACE_CONTAINER, player.puzzleStoredItems);
	player.getActionSender().sendUpdateItems(ClueScroll.PUZZLE_INTERFACE_DEFAULT_CONTAINER, getDefaultItems());

    }

    /* gets the position of a puzzle slide (using mathematical way) */
    public Position getPosition(int itemId) {
	int x = 0, y = 0;
	for (int i = 0; i < player.puzzleStoredItems.length; i++) {
	    if (player.puzzleStoredItems[i] != null) {
		if (player.puzzleStoredItems[i].getId() == itemId) {
		    x = i - 5 * (i / 5) + 1;
		    y = i / 5 + 1;
		}
	    }
	}
	return new Position(x, y);
    }
    
    /* gets the position of the blank square */
    public Position getBlankPosition() {
	return getPosition(-1);
    }

    /* checks if the clicked slide is surrounded by a blank square */
    public boolean surroundedByBlank(Position position) {
	Position left = new Position(position.getX() - 1, position.getY(), 0);
	Position right = new Position(position.getX() + 1, position.getY(), 0);
	Position up = new Position(position.getX(), position.getY() - 1, 0);
	Position down = new Position(position.getX(), position.getY() + 1, 0);
	if (getBlankPosition().equals(left) || getBlankPosition().equals(right) || getBlankPosition().equals(up) || getBlankPosition().equals(down)) {
	    return true;
	}

	return false;
    }

    public int distanceToPiece(Position reference, Position point, String comp) {
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
	if (comp.equals("x")) {
	    return counter;
	} else if (comp.equals("y")) {
	    return counter2;
	} else {
	    return counter + counter2;
	}
    }

    public int distanceToPiece(Position reference, Position point) {
	return distanceToPiece(reference, point, "");
    }

    /* moves the slide piece */
    public boolean moveSlidingPiece(int itemId, boolean reload) {
	if (getPosition(itemId).equals(new Position(0, 0, 0)) || getPosition(itemId) == null) {
	    return false;
	}
	if (finishedPuzzle() && reload) {
	    player.getActionSender().sendMessage("You have completed this puzzle!");
	    return true;
	}
	Position position = getPosition(itemId);
	Position blankPosition = getBlankPosition();
	if (surroundedByBlank(getPosition(itemId))) {
	    swapWithBlank(getPosition(itemId), reload);
	    return true;
	}

	ArrayList<Position> nearPieces = new ArrayList<Position>(2);

	/* loop to gather the square that surround the blank one */
	for (int i = 0; i < player.puzzleStoredItems.length; i++) {
	    Position thisPuzzlePosition = getPosition(player.puzzleStoredItems[i].getId());
	    if (surroundedByBlank(thisPuzzlePosition) && distanceToPiece(blankPosition, position) >= distanceToPiece(position, thisPuzzlePosition)) {
		nearPieces.add(thisPuzzlePosition);
	    }
	}

	/* loop for the main algorithm */
	for (int i = 0; i < player.puzzleStoredItems.length; i++) {
	    ArrayList<Integer> comp = new ArrayList<Integer>(4);
	    Position thisPuzzlePosition = getPosition(player.puzzleStoredItems[i].getId());

	    if (!thisPuzzlePosition.equals(blankPosition) && distanceToPiece(blankPosition, position) >= distanceToPiece(position, thisPuzzlePosition)) {

		/* loop to add the x and y distance to the clicked sliding piece */
		for (int j = 0; j < nearPieces.size(); j++) {
		    comp.add(distanceToPiece(position, nearPieces.get(j), "x"));
		    comp.add(distanceToPiece(position, nearPieces.get(j), "y"));
		}

		if (surroundedByBlank(thisPuzzlePosition)) {
		    /*
		     * if one of the distance reaches the max value of comp
		     * table, then we move it
		     */
		    if (maxValue(comp) == distanceToPiece(position, thisPuzzlePosition, "x") || maxValue(comp) == distanceToPiece(position, thisPuzzlePosition, "y")) {
			swapWithBlank(thisPuzzlePosition, reload);
			return true;
		    }

		}

	    }
	}
	return true;
    }

    /* checks if the puzzle is solved */
    public boolean finishedPuzzle() {
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
    private void swapWithBlank(Position position, boolean reload) {
	int index1 = 0;
	int index2 = 0;
	for (int i = 0; i < player.puzzleStoredItems.length; i++) {
	    if (player.puzzleStoredItems[i].getId() == -1) {
		index1 = i;
	    }
	    if (getPosition(player.puzzleStoredItems[i].getId()).equals(position)) {
		index2 = i;
	    }
	}
	Item blank = player.puzzleStoredItems[index1];
	Item chosen = player.puzzleStoredItems[index2];
	player.puzzleStoredItems[index1] = chosen;
	player.puzzleStoredItems[index2] = blank;
	if (reload) {
	    loadPuzzle();
	}
    }

    public int maxValue(ArrayList<Integer> val) {
	int value = val.get(0);
	for (int i = 0; i < val.size(); i++) {
	    if (val.get(i) >= value) {
		value = val.get(i);
	    }

	}
	return value;
    }

    public void addRandomPuzzle() {
	int[] items = {2800, 3565, 3571};
	int item = items[Misc.randomMinusOne(items.length)];
	player.getInventory().addItem(new Item(item));
	initPuzzle(item);
    }

    public boolean playerHasPuzzle() {
	Inventory i = player.getInventory();
	return i.playerHasItem(new Item(ClueScroll.CASTLE_PUZZLE)) || i.playerHasItem(new Item(ClueScroll.TREE_PUZZLE)) || i.playerHasItem(new Item(ClueScroll.TROLL_PUZZLE));
    }
}
