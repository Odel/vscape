package com.rs2.model.content.tutorialisland;

import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 4/30/12 Time: 9:48 PM To change
 * this template use File | Settings | File Templates.
 */
public class NewComersSide {

	private Player player;

	public NewComersSide(Player player) {
		this.player = player;
	}

	private int tutorialIslandStage = 99;
	private int progressValue = 1;
	private int resetbank = 0;

	public void startTutorialIsland() {
		if (tutorialIslandStage == 0) {
			player.getActionSender().hideAllSideBars();
			player.getActionSender().sendInterface(3559);
			setTutorialIslandStage(1, true);
			player.getBankManager().add(new Item(995, 25));
		} else {
			updateInterface(true);
		}
	}

	public boolean sendDialogue() {
		StagesLoader stagesLoader = StagesLoader.forId(tutorialIslandStage);
		if (stagesLoader == null)
			return false;
		if (stagesLoader.getDialogueId() == 0) {
			player.getDialogue().sendStatement("Follow the instructions to continue!");
			player.setClickId(0);
			return true;
		}
		Dialogues.startDialogue(player, stagesLoader.getDialogueId());
		return true;

	}

	public boolean sendGiveItemsInstructor() {
		StagesLoader stagesLoader = StagesLoader.forId(tutorialIslandStage);
		if (stagesLoader == null || stagesLoader.getTutItemsInvolved() == null)
			return false;
		for (Item item : stagesLoader.getTutItemsInvolved()) {
			if (item == null)
				continue;
			if (player.getInventory().playerHasItem(item.getId(), item.getCount())) {
				player.getDialogue().sendStatement("Follow the instructions to continue!");
				player.setClickId(0);
				return true;
			}
		}
		switch (stagesLoader.getTutItemsInvolved().length) {
			case 1 :
				player.getInventory().addItem(stagesLoader.getTutItemsInvolved()[0]);
				player.getDialogue().sendGiveItemNpc(stagesLoader.getTutorName() + " gives you " + (stagesLoader.getTutItemsInvolved()[0].getCount() > 1 ? "some" : "a") + " " + ItemDefinition.forId(stagesLoader.getTutItemsInvolved()[0].getId()).getName() + "!", stagesLoader.getTutItemsInvolved()[0]);
				player.setClickId(0);
				break;
			case 2 :
				player.getInventory().addItem(stagesLoader.getTutItemsInvolved()[0]);
				player.getInventory().addItem(stagesLoader.getTutItemsInvolved()[1]);
				player.getDialogue().sendGiveItemNpc(stagesLoader.getTutorName() + " gives you " + (stagesLoader.getTutItemsInvolved()[0].getCount() > 1 ? "some" : "a") + " " + ItemDefinition.forId(stagesLoader.getTutItemsInvolved()[0].getId()).getName(), "and " + (stagesLoader.getTutItemsInvolved()[1].getCount() > 1 ? "some" : "a") + " " + ItemDefinition.forId(stagesLoader.getTutItemsInvolved()[1].getId()).getName() + "!", stagesLoader.getTutItemsInvolved()[0], stagesLoader.getTutItemsInvolved()[1]);
				player.setClickId(0);
				break;
		}
		return true;
	}

	public void addStarterItems() {
		player.getInventory().getItemContainer().clear();
		player.getEquipment().getItemContainer().clear();
		player.getInventory().refresh();
		player.getEquipment().refresh();
		Item[] starterItems = {new Item(1351), new Item(590), new Item(303), new Item(315), new Item(1925), new Item(1931), new Item(2309), new Item(1265), new Item(1205), new Item(1277), new Item(1171), new Item(841), new Item(882, 25), new Item(556, 25), new Item(558, 15), new Item(555, 6), new Item(557, 4), new Item(559, 2)};
		for (Item item : starterItems)
			player.getInventory().addItem(item);
	}

	public boolean handleObjectClicking(int objectId, Position objectPosition) {
		if (!isInTutorialIslandStage())
			return false;
		TutorialObjects tutorialObjects = TutorialObjects.forId(tutorialIslandStage);
		if (tutorialObjects == null)
			return false;
		for (int i = 0; i < tutorialObjects.getObjectId().length; i++) {
			if (objectId == tutorialObjects.getObjectId()[i] && objectPosition.equals(tutorialObjects.getObjectPosition()[i]))
				tutorialObjects.applyObjectClicking(player);
		}
		return false;

	}

	public void updateInterface(boolean send) {
		StagesLoader stagesLoader = StagesLoader.forId(tutorialIslandStage);
		if (stagesLoader == null) {
			player.getActionSender().removeInterfaces();
			return;
		}
		if (tutorialIslandStage >= 43)
			player.getEquipment().sendWeaponInterface();

		stagesLoader.sendInterfaces(player, send);
		if (isInTutorialIslandStage())
			sendProgressInterface();
	}

	public void sendProgressInterface() {
		player.getActionSender().sendConfig(406, progressValue);
		player.getActionSender().sendInterfaceHidden(1, 12224);
		player.getActionSender().sendInterfaceHidden(1, 12225);
		player.getActionSender().sendInterfaceHidden(1, 12226);
		player.getActionSender().sendInterfaceHidden(1, 12227);
		player.getActionSender().sendInterfaceHidden(0, 12161);
		player.getActionSender().sendString("% Done", 12224);
		player.getActionSender().sendWalkableInterface(8680);

	}

	public void setTutorialIslandStage(int tutorialIslandStage, boolean update) {
		this.tutorialIslandStage = tutorialIslandStage;
		if (update)
			updateInterface(update);
	}

	public void setProgressValue(int progressValue) {
		this.progressValue = progressValue;
	}
	
	public void setResetBank(int resetbank) {
		this.resetbank = resetbank;
	}

	public int getTutorialIslandStage() {
		return tutorialIslandStage;
	}

	public int getResetBank() {
		return resetbank;
	}
	
	public int getProgressValue() {
		return progressValue;
	}

	public boolean isInTutorialIslandStage() {
		return false;//tutorialIslandStage < 100;
	}
}
