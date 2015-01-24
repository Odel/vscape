package com.rs2.model.content.minigames;

import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class RuneDraw {
    private Player player;
    private int bet = 0;
    private int emptySlotPlayer = 12240;
    private int emptySlotOpponent = 12250;
    private int playerScore = 0;
    private int opponentScore = 0;
    private boolean gameOver;
    private boolean held;
    private boolean questFlag;
    
    public RuneDraw(Player player) {
	this.player = player;
	this.gameOver = false;
	this.held = false;
    }
    
    public static final int RUNE_DRAW_INTERFACE = 12231;
    
    public static final int RUNE_DRAW = 12234;
    public static final int OPPONENTS_SCORE_TEXT = 12237;
    public static final int DRAW_TEXT = 12238;
    public static final int HOLD_TEXT = 12239;
    public static final int YOUR_SCORE = 12260;
    public static final int YOUR_TEXT = 12261;
    public static final int OPPONENTS_SCORE = 12262;
    public static final int OPPONENTS_TEXT = 12263;
    
    public static final int DRAW_BUTTON = 47206;
    public static final int HOLD_BUTTON = 47207;
    
    public static final int[] RUNES = {556, 558, 555, 557, 554, 559, 563, 562, 561, 560};
    
    public void hold() {
	held = true;
	player.getActionSender().sendString("", YOUR_TEXT);
	player.getActionSender().sendString("Thinking...", OPPONENTS_TEXT);
	player.setStopPacket(true);
	CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
	    @Override
	    public void execute(CycleEventContainer b) {
		int index = Misc.randomMinusOne(RUNES.length);
		sendRune(RUNES[index], emptySlotOpponent);
		opponentScore += (index + 1) < 10 ? index + 1 : 0;
		refresh();
		emptySlotOpponent++;
		if ((opponentScore > 21 && playerScore < 21) || index == 9) {
		    winGame();
		    b.stop();
		    return;
		}
		if (opponentScore == 21) {
		    loseGame();
		    b.stop();
		    return;
		}
		player.getActionSender().sendString("Your turn...", YOUR_TEXT);
		player.getActionSender().sendString("", HOLD_TEXT);
		b.stop();
	    }

	    @Override
	    public void stop() {
		player.setStopPacket(false);
	    }
	}, 6);
    }
    public void draw() {
	int index = Misc.randomMinusOne(RUNES.length);
	sendRune(RUNES[index], emptySlotPlayer);
	playerScore += (index + 1) < 10 ? index + 1 : 0;
	refresh();
	emptySlotPlayer++;
	if((playerScore > 21 && opponentScore < 21) || index == 9) {
	    loseGame();
	    return;
	}
	if(playerScore == 21) {
	    winGame();
	    return;
	}
	player.getActionSender().sendString("Thinking...", OPPONENTS_TEXT);
	player.setStopPacket(true);
	CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
	    @Override
	    public void execute(CycleEventContainer b) {
		int index = Misc.randomMinusOne(RUNES.length);
		sendRune(RUNES[index], emptySlotOpponent);
		opponentScore += (index + 1) < 10 ? index + 1 : 0;
		refresh();
		emptySlotOpponent++;
		if ((opponentScore > 21 && playerScore < 21) || index == 9) {
		    winGame();
		    b.stop();
		    return;
		}
		if (opponentScore == 21) {
		    loseGame();
		    b.stop();
		    return;
		}
		player.getActionSender().sendString("Your turn...", YOUR_TEXT);
		b.stop();
	    }

	    @Override
	    public void stop() {
		player.setStopPacket(false);
	    }
	}, 6);
    }
    
    public void sendRune(int item, int index) {
	player.getActionSender().sendItemOnInterface(index, 75, item);
    }
    
    public void refresh() {
	player.getActionSender().sendString("Rune-Draw", RUNE_DRAW);
	player.getActionSender().sendString("@whi@" + playerScore, YOUR_SCORE);
	player.getActionSender().sendString("@whi@" + opponentScore, OPPONENTS_SCORE);
	player.getActionSender().sendString("Opponent's Score", OPPONENTS_SCORE_TEXT);
	player.getActionSender().sendString("", YOUR_TEXT);
	player.getActionSender().sendString("", OPPONENTS_TEXT);
	player.getActionSender().sendString("Draw", DRAW_TEXT);
	if(!held) {
	    player.getActionSender().sendString("Hold", HOLD_TEXT);
	}
    }
    
    public void reset() {
	playerScore = 0;
	opponentScore = 0;
	emptySlotPlayer = 12240;
	emptySlotOpponent = 12250;
	held = false;
	for(int i = 12239; i < 12260; i++) {
	    sendRune(-1, i);
	}
    }
    public void openGame(int amount, boolean bool) {
	questFlag = bool;
	bet = amount;
	if(bet != 0) {
	    player.getInventory().removeItem(new Item(995, bet));
	}
	gameOver = false;
	reset();
	refresh();
	player.getActionSender().sendInterface(12231);
    }
    
    public void openGame(int amount) {
	questFlag = false;
	bet = amount;
	if(bet != 0) {
	    player.getInventory().removeItem(new Item(995, bet));
	}
	gameOver = false;
	reset();
	refresh();
	player.getActionSender().sendInterface(12231);
    }
    
    public void openGame() {
	questFlag = false;
	gameOver = false;
	reset();
	refresh();
	player.getActionSender().sendInterface(12231);
    }
    
    public  boolean handleButtons(final Player presser, int buttonId) {
	if(presser != player) {
	    return false;
	}
	if(gameOver) {
	    return false;
	}
	else {
	    switch (buttonId) {
		case DRAW_BUTTON:
		    draw();
		    return true;
		case HOLD_BUTTON:
		    if(!held) {
			hold();
			return true;
		    }
		    else {
			return false;
		    }
	    }
	    return false;
	}
    }
    
    public void winGame() {
	player.getActionSender().sendMessage("You have won the game of RuneDraw!");
	player.getActionSender().sendString("You win!", YOUR_TEXT);
	player.getActionSender().sendString("", DRAW_TEXT);
	player.getActionSender().sendString("", HOLD_TEXT);
	if(bet != 0 && !questFlag) {
	    player.getInventory().addItem(new Item(995, (bet * 2)));
	}
	if (questFlag) {
	    if (!player.getQuestVars().getRuneDrawWins()[0] && !player.getQuestVars().getRuneDrawWins()[1] && !player.getQuestVars().getRuneDrawWins()[2]) {
		player.getQuestVars().setRuneDrawWins(0, true);
	    } else if (player.getQuestVars().getRuneDrawWins()[0] && !player.getQuestVars().getRuneDrawWins()[1] && !player.getQuestVars().getRuneDrawWins()[2]) {
		player.getQuestVars().setRuneDrawWins(1, true);
	    } else if (player.getQuestVars().getRuneDrawWins()[0] && player.getQuestVars().getRuneDrawWins()[1] && !player.getQuestVars().getRuneDrawWins()[2]) {
		player.getQuestVars().setRuneDrawWins(2, true);
	    }
	    player.getQuestVars().setJustWonRuneDraw(true);
	}
	gameOver = true;
    }
    
    public void loseGame() {
	player.getActionSender().sendMessage("You have lost the game of RuneDraw.");
	player.getActionSender().sendString("You lose.", YOUR_TEXT);
	player.getActionSender().sendString("", DRAW_TEXT);
	player.getActionSender().sendString("", HOLD_TEXT);
	if(questFlag) {
	    player.getQuestVars().setJustWonRuneDraw(false);
	}
	gameOver = true;
    }
}
