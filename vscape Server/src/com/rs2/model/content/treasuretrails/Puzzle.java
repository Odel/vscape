package com.rs2.model.content.treasuretrails;

import java.util.ArrayList;

import com.rs2.model.Position;
import com.rs2.model.content.quests.impl.MonkeyMadness.MonkeyMadness;
import com.rs2.model.players.Player;
import com.rs2.model.players.container.inventory.Inventory;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;

public class Puzzle {

    private Player player;
    private int SCRAMBLE_ITERATIONS = 150;
    public Item[] puzzleStoredItems = new Item[ClueScroll.PUZZLE_LENGTH];

    public Puzzle(Player player) {
	this.player = player;
    }
    public ArrayList<Integer> puzzleArray = new ArrayList<Integer>(ClueScroll.PUZZLE_LENGTH);

    /* the puzzle index */
    public int index;

    /* load the main puzzle */
    public boolean loadClueInterface(int itemId) {
	if (getIndexByItem(itemId) == 0) {
	    return false;
	}
	if(index == 0) {
	    this.index = getIndexByItem(itemId);
	}
	loadPuzzle();
	if (finishedPuzzle()) {
	    player.getActionSender().sendMessage("You have completed this puzzle!");
	    return true;
	}
	return true;
    }

    public boolean initPuzzle(int itemId) {
	int indexToSet = getIndexByItem(itemId);
	if (indexToSet == 0) {
	    return false;
	} else {
	    this.index = indexToSet;
	    loadPuzzleArray();
	    loadPuzzleItems();
	    return true;
	}
    }

    /* add the puzzle items to an arrayList */
    public void loadPuzzleArray() {
	for (int i = 0; i < ClueScroll.PUZZLE_LENGTH; i++) {
	    if(getPuzzleIndex(index) != null) {
		puzzleArray.add(getPuzzleIndex(index)[i]);
	    }
	}
    }

    public int getBlockNumberForItemId(int itemId) {
	for (int i = 0; i < ClueScroll.PUZZLE_LENGTH; i++) {
	    if (itemId == -1 || getPuzzleIndex(index) == null) {
		return 0;
	    }
	    if (itemId == getPuzzleIndex(index)[i]) {
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
	    case 6661234:
		return 4;
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
	    case 4:
		return MonkeyMadness.GLIDER_PUZZLE;
	}
	return null;
    }

    public void resetPuzzleItems() {
	for (int i = 0; i < this.puzzleStoredItems.length; i++) {
	    this.puzzleStoredItems[i] = new Item(-1);
	}
	this.index = 0;
    }
    
    /* loading the puzzle items */
    public void loadPuzzleItems() {
	if(index == 0 || getPuzzleIndex(index) == null) {
	    return;
	}
	for (int i = 0; i < ClueScroll.PUZZLE_LENGTH; i++) {
	    this.puzzleStoredItems[i] = new Item(getPuzzleIndex(index)[i]);
	}
	for (int i = 0; i < SCRAMBLE_ITERATIONS; i++) {
	    int move = adjacentToBlank().get(Misc.randomMinusOne(adjacentToBlank().size()));
	    moveSlidingPiece(move, false);
	}
    }

    public ArrayList<Integer> adjacentToBlank() {
	ArrayList<Integer> toReturn = new ArrayList<>();
	for (int i = 0; i < ClueScroll.PUZZLE_LENGTH; i++) {
	    if (distanceToPiece(getPosition(this.puzzleStoredItems[i].getId()), getBlankPosition()) == 1) {
		toReturn.add(this.puzzleStoredItems[i].getId());
	    }
	}
	return toReturn;
    }
    
    /* getting the solved puzzle item for hint */
    public Item[] getDefaultItems() {
	Item[] item = new Item[ClueScroll.PUZZLE_LENGTH];
	for (int i = 0; i < ClueScroll.PUZZLE_LENGTH; i++) {
	    if(getPuzzleIndex(index) != null) {
		item[i] = new Item(getPuzzleIndex(index)[i]);
	    }
	}
	return item;
    }

    public void loadPuzzle() {
	player.getActionSender().sendInterface(index == 4 ? MonkeyMadness.PUZZLE_INTERFACE : ClueScroll.PUZZLE_INTERFACE);
	player.getActionSender().sendUpdateItems(index == 4 ? 11130 : ClueScroll.PUZZLE_INTERFACE_CONTAINER, this.puzzleStoredItems);
	if(index != 4) {
	    player.getActionSender().sendUpdateItems(ClueScroll.PUZZLE_INTERFACE_DEFAULT_CONTAINER, getDefaultItems());
	}
    }

    /* gets the position of a puzzle slide (using mathematical way) */
    public Position getPosition(int itemId) {
	int x = 0, y = 0;
	for (int i = 0; i < this.puzzleStoredItems.length; i++) {
	    if (this.puzzleStoredItems[i] != null) {
		if (this.puzzleStoredItems[i].getId() == itemId) {
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
	if(player.getStatedInterface().equals("GLIDER_PUZZLE_HINT")) {
	    return false;
	}
	if (getPosition(itemId).equals(new Position(0, 0, 0)) || getPosition(itemId) == null) {
	    return false;
	}
	if (finishedPuzzle() && reload) {
	    if(index == 4) {
		MonkeyMadness.reinitializeHangar(player);
	    } else {
		player.getActionSender().sendMessage("You have completed this puzzle!");
	    }
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
	for (int i = 0; i < this.puzzleStoredItems.length; i++) {
	    Position thisPuzzlePosition = getPosition(this.puzzleStoredItems[i].getId());
	    if (surroundedByBlank(thisPuzzlePosition) && distanceToPiece(blankPosition, position) >= distanceToPiece(position, thisPuzzlePosition)) {
		nearPieces.add(thisPuzzlePosition);
	    }
	}

	/* loop for the main algorithm */
	for (int i = 0; i < this.puzzleStoredItems.length; i++) {
	    ArrayList<Integer> comp = new ArrayList<Integer>(4);
	    Position thisPuzzlePosition = getPosition(this.puzzleStoredItems[i].getId());

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
	if (this.puzzleStoredItems == null || this.puzzleStoredItems.length == 0 || getPuzzleIndex(index) == null) {
	    return false;
	}
	int counter = 0;
	for (int i = 0; i < this.puzzleStoredItems.length; i++) {
	    if (this.puzzleStoredItems[i] != null && this.puzzleStoredItems[i].getId() == getPuzzleIndex(index)[i]) {
		counter++;
	    }
	}
	return counter == this.puzzleStoredItems.length;
    }

    /*
     * swap the clicked sliding piece with the blank one : in other word, moves
     * the sliding piece
     */
    private void swapWithBlank(Position position, boolean reload) {
	int index1 = 0;
	int index2 = 0;
	for (int i = 0; i < this.puzzleStoredItems.length; i++) {
	    if (this.puzzleStoredItems[i].getId() == -1) {
		index1 = i;
	    }
	    if (getPosition(this.puzzleStoredItems[i].getId()).equals(position)) {
		index2 = i;
	    }
	}
	Item blank = this.puzzleStoredItems[index1];
	Item chosen = this.puzzleStoredItems[index2];
	this.puzzleStoredItems[index1] = chosen;
	this.puzzleStoredItems[index2] = blank;
	if (reload) {
	    loadPuzzle();
	}
	if (finishedPuzzle() && index == 4 && reload) {
	    MonkeyMadness.reinitializeHangar(player);
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
	int[] items = {ClueScroll.CASTLE_PUZZLE, ClueScroll.TREE_PUZZLE, ClueScroll.TROLL_PUZZLE};
	int item = items[Misc.randomMinusOne(items.length)];
	if(initPuzzle(item)) {
	    player.getInventory().addItem(new Item(item));
	}
    }

    public boolean playerHasPuzzle() {
	Inventory i = player.getInventory();
	return i.playerHasItem(new Item(ClueScroll.CASTLE_PUZZLE)) || i.playerHasItem(new Item(ClueScroll.TREE_PUZZLE)) || i.playerHasItem(new Item(ClueScroll.TROLL_PUZZLE));
    }
}
