package com.rs2.model.content.quests.impl.DeathPlateau;

import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class GamblingDice {
	private final Player player;
	private int bet;
	private String opponentName = "Harold";
	public int oppRoll;
	public int myRoll;
	public boolean justWon = false;
	
	public static final int DICE_INTERFACE = 6675;
	private static final int NAME_INDEX = 8399;
	private static final int OPP_NAME_INDEX = 7815;
	private static final int BET_INDEX = 8424;
	private static final int GAME_TEXT_INDEX = 8426;
	private static final int DICE_1 = 7813;
	private static final int DICE_2 = 7814;
	private static final int OPP_DICE_1 = 6719;
	private static final int OPP_DICE_2 = 6695;
	
	private static final int ROLL_ANIM = 1150;
	
	
	public GamblingDice(Player player) {
		this.player = player;
		this.bet = 0;
		this.oppRoll = 0;
	}
	
	public void setBet(int bet) {
		this.bet = bet;
	}
	
	public int getBet() {
		return this.bet;
	}
	
	
	public void startGame() {
		justWon = false;
		player.getActionSender().sendMapState(2);
		player.setMovementDisabled(true);
		player.getActionSender().hideAllSideBars();
		player.getActionSender().enableSideBarInterfaces(new int[]{7, 8, 9, 10});
		player.getActionSender().sendInterfaceAnimation(OPP_DICE_1, -1);
		player.getActionSender().sendInterfaceAnimation(OPP_DICE_2, -1);
		player.getActionSender().sendInterfaceAnimation(DICE_1, -1);
		player.getActionSender().sendInterfaceAnimation(DICE_2, -1);
		player.getActionSender().sendInterface(DICE_INTERFACE);
		player.getActionSender().sendString(player.getUsername(), NAME_INDEX);
		player.getActionSender().sendString("" + this.bet, BET_INDEX);
		player.getActionSender().sendString("" + this.bet, BET_INDEX + 1);
		player.getActionSender().sendString(opponentName, OPP_NAME_INDEX);
		player.getActionSender().sendConfig(261, configAmountForBet());
		player.getActionSender().sendConfig(262, configAmountForBet());
		player.getActionSender().sendInterfaceHidden(1, 8420);
		player.getActionSender().sendInterfaceHidden(1, 8422);
		player.getInventory().removeItem(new Item(995, bet));
		rollOpponentDice();
	}
	
	public void endGame() {
		//player.getActionSender().sendInterfaceHidden(0, 8422);
		if(myRoll > oppRoll) {
			player.getActionSender().sendString("You win!", GAME_TEXT_INDEX);
			player.getInventory().addItem(new Item(995, bet*2));
			player.getActionSender().sendMessage("Harold has given you your winnings!");
			if(player.getQuestVars().givenHaroldSpecial) {
				player.getQuestVars().moneyWonFromHarold += bet;
			}
			justWon = true;
			
		} else if (myRoll == oppRoll) {
			player.getActionSender().sendString("Tie game. No blood!", GAME_TEXT_INDEX);
			player.getInventory().addItem(new Item(995, bet));
			player.getActionSender().sendMessage("You and Harold take your money back.");
			player.setStatedInterface("gamblingDiceTie");
		} else {
			player.getActionSender().sendString("You lose!", GAME_TEXT_INDEX);
			if(player.getQuestVars().givenHaroldSpecial) {
				player.getInventory().addItem(new Item(995, bet*2));
				player.getActionSender().sendMessage("Harold has given you your winnings!");
				player.getQuestVars().moneyWonFromHarold += bet;
			}
		}
		myRoll = 0;
		oppRoll = 0;
		bet = 0;
	}
	
	public void quitGameInterface() {
		player.getActionSender().sendMapState(0);
		player.getActionSender().sendSideBarInterfaces();
		player.getEquipment().sendWeaponInterface();
		player.setMovementDisabled(false);
	}
	
	public int configAmountForBet() {
		if(bet > 0 && bet < 6) {
			return bet;
		} else if (bet >= 6 && bet < 100) {
			return 6;
		} else if (bet >= 100 && bet < 500) {
			return 7;
		} else if (bet >= 500 && bet < 1000) {
			return 8;
		} else if (bet >= 1000 && bet < 10000) {
			return 9;
		} else {
			 return 10;
		}
	}
	
	public void rollOpponentDice() {
		player.getActionSender().sendInterfaceAnimation(OPP_DICE_1, -1);
		player.getActionSender().sendInterfaceAnimation(OPP_DICE_2, -1);
		player.setStopPacket(true);
		player.getActionSender().sendString(opponentName + " rolls...", GAME_TEXT_INDEX);
		player.getActionSender().sendInterfaceAnimation(OPP_DICE_1, ROLL_ANIM);
		player.getActionSender().sendInterfaceAnimation(OPP_DICE_2, ROLL_ANIM);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				b.stop();
			}

			@Override
			public void stop() {
				int add = Misc.random(5);
				int add2 = Misc.random(5);
				if (add != 0) {
					player.getActionSender().sendInterfaceAnimation(OPP_DICE_1, ROLL_ANIM + add);
				}
				if (add2 != 0) {
					player.getActionSender().sendInterfaceAnimation(OPP_DICE_2, ROLL_ANIM + add2);
				}
				player.getDice().oppRoll += (add + 1);
				player.getDice().oppRoll += (add2 + 1);
				player.getActionSender().sendString("Your roll...", GAME_TEXT_INDEX);
				player.getActionSender().sendInterfaceHidden(0, 8420);
				player.setStopPacket(false);
			}
		}, 4);
	}
	
	public void rollDice() {
		player.getActionSender().sendInterfaceAnimation(DICE_1, -1);
		player.getActionSender().sendInterfaceAnimation(DICE_2, -1);
		player.setStopPacket(true);
		player.getActionSender().sendInterfaceAnimation(DICE_1, ROLL_ANIM);
		player.getActionSender().sendInterfaceAnimation(DICE_2, ROLL_ANIM);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				b.stop();
			}

			@Override
			public void stop() {
				int add = Misc.random(5);
				int add2 = Misc.random(5);
				if (add != 0) {
					player.getActionSender().sendInterfaceAnimation(DICE_1, ROLL_ANIM + add);
				}
				if (add2 != 0) {
					player.getActionSender().sendInterfaceAnimation(DICE_2, ROLL_ANIM + add2);
				}
				player.getDice().myRoll += (add + 1);
				player.getDice().myRoll += (add2 + 1);
				player.getDice().endGame();
				player.setStopPacket(false);
			}
		}, 4);
	}
	
	public boolean buttonHandling(final Player player, final int buttonId) {
		switch (buttonId) {
			case 32229: //roll
				player.getActionSender().sendInterfaceHidden(1, 8420);
				rollDice();
				return true;
		}
		return false;
	}

}
