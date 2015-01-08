package com.rs2.model.content;

import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.players.Player;
import com.rs2.util.Misc;

/**
 * By Mikey` of Rune-Server
 */

public class BankPin {

	private Player player;

	public BankPin(Player player) {
		this.player = player;
	}

	private int[] bankPin = {-1, -1, -1, -1};

	private int[] pendingBankPin = {-1, -1, -1, -1};

	private static final int BANK_PIN_CHANGE_DAYS = 7;

	private int interfaceStatus = -1;

	private int pinAppendYear = -1, pinAppendDay = -1;

	private boolean bankPinVerified = false, changingBankPin = false, deletingBankPin = false;

	private PinInterfaceStatus pinInterfaceStatus = PinInterfaceStatus.VERIFYING;

	private ActionsAfterVerification actionsAfterVerification = ActionsAfterVerification.BANK;

	public boolean clickPinButton(int buttonId) {
		switch(buttonId) {
			case 58074 : //bank pin close
				player.getActionSender().removeInterfaces();
				return true;
			case 58073 : //dont know pin
				Dialogues.sendDialogue(player, 494, 19, 0);
				return true;
		}
		switch (pinInterfaceStatus) {
			case VERIFYING :
				for (int i = 0; i < 10; i++) {
					if (buttonId == 58025 + i) {
						verifyPin(i);
						return true;
					}
				}
				break;
			case CHANGING :
				for (int i = 0; i < 10; i++) {
					if (buttonId == 58025 + i) {
						pendingBankPin[interfaceStatus] = i;
						//System.out.println("it's at " + interfaceStatus + " and i is " + i + " and pending pin is at " + pendingBankPin[interfaceStatus]);
						if (interfaceStatus + 1 < 4) {
							sendBankPinVerificationInterface(interfaceStatus + 1);
						} else {
							Dialogues.sendDialogue(player, 494, 11, 0);
						}
						return true;
					}
				}
				break;
		}
		return false;
	}

	public void startPinInterface(PinInterfaceStatus pinInterfaceStatus) {
		this.pinInterfaceStatus = pinInterfaceStatus;
		resetBankPinInterface();
		sendBankPinVerificationInterface(0);
	}

	private void sendBankPinVerificationInterface(int currentStatus) {
		this.interfaceStatus = currentStatus;
		for (int i = 0; i < currentStatus; i++) {
			player.getActionSender().sendString("*", 14913 + i);
			player.getActionSender().sendString("Click the " + Misc.intToString(i + 2) + " digit...", 15313);
		}
		player.getActionSender().sendInterface(7424);
	}

	public void checkBankPinChangeStatus() {
		if (changingBankPin) {
			int daysPassed = getDaysPassedSincePinChange();
			if (daysPassed >= BANK_PIN_CHANGE_DAYS) {
				changeBankPin();
			} else if (!hasBankPin()) {
				changeBankPin();
			}
		}
		if (deletingBankPin) {
			int daysPassed = getDaysPassedSincePinChange();
			if (daysPassed >= BANK_PIN_CHANGE_DAYS) {
				deleteBankPin();
			}
		}
	}

	private void changeBankPin() {
		player.getActionSender().sendMessage("Your bank pin has been successfully "+(hasBankPin() ? "changed" : "set")+"!");
		for (int index = 0; index < bankPin.length; index++) {
			//System.out.println("" + pendingBankPin[index]);
			bankPin[index] = pendingBankPin[index];
			pendingBankPin[index] = -1;
		}
		changingBankPin = false;
		pinAppendYear = -1;
		pinAppendDay = -1;
	}

	public void deleteBankPin() {
		for (int index = 0; index < bankPin.length; index++) {
			bankPin[index] = -1;
			pendingBankPin[index] = -1;
		}
		deletingBankPin = false;
		pinAppendYear = -1;
		pinAppendDay = -1;
		player.getActionSender().sendMessage("Your bank pin has been successfully deleted!");
	}

	private int getDaysPassedSincePinChange() {
		if (Misc.getYear() == pinAppendYear) {
			return Misc.getDayOfYear() - pinAppendDay;
		} else {
			return 365 - pinAppendDay + Misc.getDayOfYear();
		}
	}

	private String putPinStatusToString() {
		int daysPassed = getDaysPassedSincePinChange();
		if (changingBankPin) {
			return "You bank pin will change in " + (BANK_PIN_CHANGE_DAYS - daysPassed) + " days.";
		} else if (deletingBankPin) {
			return "You bank pin will be deleted in " + (BANK_PIN_CHANGE_DAYS - daysPassed) + " days.";
		} else if (hasBankPin()) {
			return "Your bank pin is set and up to date.";
		} else {
			return "You do not have a bank pin.";
		}
	}

	private void resetBankPinInterface() {
		interfaceStatus = -1;
		player.getActionSender().sendString(putPinStatusToString(), 14923);
		player.getActionSender().sendString("Click the " + Misc.intToString(1) + " digit...", 15313);
		for (int i = 0; i < 4; i++) {
			player.getActionSender().sendString("?", 14913 + i);
		}
		for (int i = 0; i < 10; i++) {
			player.getActionSender().sendString("" + i, 14883 + i);
		}
	}

	private void verifyPin(int pinEntered) {
	    if(interfaceStatus < 0) {
		player.getActionSender().sendMessage("Something has gone wrong with the PIN interface. Please ::bugreport.");
		System.out.println("interfaceStatus was < 0 for:" + player.getUsername() + " line 162 BankPin.java");
		return;
	    }
		player.setPinAttempt(pinEntered, interfaceStatus);
		if (interfaceStatus + 1 < 4) {
			sendBankPinVerificationInterface(interfaceStatus + 1);
		} else if (!enteredCorrectedPin()) {
			player.getActionSender().sendMessage("You've entered an incorrect pin, please try again.");
			resetBankPinInterface();
			sendBankPinVerificationInterface(0);
			player.resetPinAttempt();
		} else {
			player.getActionSender().sendMessage("You have successfully verified your bank pin.");
			setBankPinVerified(true);
			doActionAfterVerification();
		}
	}

	private void doActionAfterVerification() {
		switch (actionsAfterVerification) {
			case BANK :
				player.getBankManager().openBank();
				break;
		}
	}

	public void setChangingBankPin() {
		changingBankPin = true;
		pinAppendYear = Misc.getYear();
		pinAppendDay = Misc.getDayOfYear();
	}

	public void setDeletingBankPin() {
		clearPendingBankPinRequest();
		deletingBankPin = true;
		pinAppendYear = Misc.getYear();
		pinAppendDay = Misc.getDayOfYear();
	}

	public void clearPendingBankPinRequest() {
		for (int i = 0; i < pendingBankPin.length; i++) {
			pendingBankPin[i] = -1;
		}
		changingBankPin = false;
		deletingBankPin = false;
		pinAppendYear = -1;
		pinAppendDay = -1;
	}

	private void setBankPinVerified(boolean bankPinVerified) {
		this.bankPinVerified = bankPinVerified;
	}

	private boolean enteredCorrectedPin() {
		for (int i = 0; i < 4; i++) {
			if (bankPin[i] < 0) {
				return true;
			}
			if (bankPin[i] != player.getPinAttempt()[i]) {
				return false;
			}
		}
		return true;
	}

	public boolean hasBankPin() {
		return bankPin[0] != -1;
	}

	public boolean hasPendingBankPinRequest() {
		return changingBankPin || deletingBankPin;
	}

	public boolean isBankPinVerified() {
		return bankPinVerified;
	}

	public int[] getPendingBankPin() {
		return pendingBankPin;
	}

	public int[] getBankPin() {
		return bankPin;
	}

	public void setChangingBankPin(boolean changingBankPin) {
		this.changingBankPin = changingBankPin;
	}

	public boolean isChangingBankPin() {
		return changingBankPin;
	}

	public void setDeletingBankPin(boolean deletingBankPin) {
		this.deletingBankPin = deletingBankPin;
	}

	public boolean isDeletingBankPin() {
		return deletingBankPin;
	}

	public void setPinAppendYear(int pinAppendYear) {
		this.pinAppendYear = pinAppendYear;
	}

	public int getPinAppendYear() {
		return pinAppendYear;
	}

	public void setPinAppendDay(int pinAppendDay) {
		this.pinAppendDay = pinAppendDay;
	}

	public int getPinAppendDay() {
		return pinAppendDay;
	}

	public enum PinInterfaceStatus {
		VERIFYING, CHANGING
	}

	public enum ActionsAfterVerification {
		BANK
	}

}
