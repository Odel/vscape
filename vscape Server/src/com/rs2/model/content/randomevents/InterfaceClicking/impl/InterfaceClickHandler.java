package com.rs2.model.content.randomevents.InterfaceClicking.impl;

import java.util.HashMap;
import java.util.Map;

import com.rs2.model.content.randomevents.EventsConstants;
import com.rs2.model.content.randomevents.RandomEvent;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 14/04/12 Time: 22:50 To change
 * this template use File | Settings | File Templates.
 */
public class InterfaceClickHandler {

	private Player player;

	public InterfaceClickHandler(Player player) {
		this.player = player;
	}

	public boolean completed = false;
	public int randomNumber;
	public int stage = 0;

	private static final Map<Integer, InterfaceClickEvent> events = new HashMap<Integer, InterfaceClickEvent>();

	public InterfaceClickEvent getEvents(int npcId) {
		return events.get(npcId);
	}

	static {
		events.put(3117, new SandwichLady(true));
	}

	public void sendEventRandomly() {
		new RandomEvent(player, EventsConstants.interfaceClickNpcs[Misc.random(EventsConstants.interfaceClickNpcs.length - 1)]);
	}

	public void sendCycleAdvertisement(final Npc npc) {
		final InterfaceClickEvent interfaceClickEvent = getEvents(npc.getNpcId());

		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int cycles = 0;
			@Override
			public void execute(CycleEventContainer container) {
				if (cycles == interfaceClickEvent.cycleMessages().length - 1) {
					sayGoodBye(npc, true);
					container.stop();
					return;
				}
				if (player.getRandomInterfaceClick().completed || !npc.isVisible() || player.getSpawnedNpc() == null) {
					container.stop();
					return;
				}
				cycles++;
				npc.getUpdateFlags().sendForceMessage(interfaceClickEvent.cycleMessages()[cycles].replaceAll("%", player.getUsername()));

			}

			@Override
			public void stop() {
				RandomEvent.resetEvents(player);
			}
		}, interfaceClickEvent.cycleDuration());
	}

	/*public void sendModelsRotation(int npcId) {
		final InterfaceClickEvent interfaceClickEvent = getEvents(npcId);

		for (int model : interfaceClickEvent.modelToRotate())
			player.getActionSender().sendDialogueAnimation(model, 2715);

	}*/

	public void openInterface(int npcId) {
		InterfaceClickEvent interfaceClickEvent = getEvents(npcId);
		player.setStatedInterface("" + npcId);
		player.getActionSender().sendInterface(interfaceClickEvent.interfaceSent());
	}

	public void sayGoodBye(final Npc npc, final boolean failed) {
		final InterfaceClickEvent interfaceClickEvent = getEvents(npc.getNpcId());
		final int randNum = Misc.random(EventsConstants.remoteLocations.length - 1);
		player.getActionSender().removeInterfaces();
		if (!failed) {
			interfaceClickEvent.handleSuccess(player);
			npc.getUpdateFlags().sendForceMessage(interfaceClickEvent.goodByeMessage()[0]);
			npc.getUpdateFlags().sendAnimation(EventsConstants.GOOD_BYE_EMOTE);
			player.getInventory().addItem(interfaceClickEvent.rewards()[randomNumber]);
		} else {
		    if(npc != null && Misc.goodDistance(player.getPosition(), npc.getPosition(), 7)) {
			npc.sendPlayerAway(player, 402, 2304, EventsConstants.remoteLocations[randNum].getX(), EventsConstants.remoteLocations[randNum].getY(), EventsConstants.remoteLocations[randNum].getZ(), interfaceClickEvent.goodByeMessage()[1], false);
		    }
		}
		RandomEvent.resetEvents(player);
		player.getRandomInterfaceClick().completed = true;
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.getActionSender().sendStillGraphic(EventsConstants.RANDOM_EVENT_GRAPHIC, npc.getPosition(), 100 << 16);
				container.stop();
			}
			@Override
			public void stop() {
				NpcLoader.destroyNpc(npc);
				player.setSpawnedNpc(null);
			}
		}, 5);
		

	}

	public boolean handleButtonClicking(int buttonId) {
		if (player.getSpawnedNpc() == null)
			return false;
		InterfaceClickEvent interfaceClickEvent = player.getRandomInterfaceClick().getEvents(player.getSpawnedNpc().getNpcId());
		if (interfaceClickEvent == null)
			return false;
		if (interfaceClickEvent.buttonsDisplayed().contains(buttonId)) {
			if (buttonId == interfaceClickEvent.buttonsDisplayed().get(player.getRandomInterfaceClick().randomNumber))
				player.getRandomInterfaceClick().stage++;
			else {
				interfaceClickEvent.handleFailure(player);
			}
			if (player.getRandomInterfaceClick().stage == interfaceClickEvent.numberOfStages())
				player.getRandomInterfaceClick().sayGoodBye(player.getSpawnedNpc(), false);

		}
		return true;
	}
}
