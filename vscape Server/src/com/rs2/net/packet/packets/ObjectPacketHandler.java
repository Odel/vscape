package com.rs2.net.packet.packets;

import com.rs2.Constants;
import com.rs2.cache.interfaces.RSInterface;
import com.rs2.model.Position;
import com.rs2.model.content.Following;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.quests.impl.GhostsAhoy.GhostsAhoy;
import com.rs2.model.content.quests.impl.HorrorFromTheDeep;
import com.rs2.model.content.quests.impl.PriestInPeril;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.quests.impl.VampireSlayer;
import com.rs2.model.content.quests.impl.WaterfallQuest;
import com.rs2.model.content.skills.Crafting.GemCrafting;
import com.rs2.model.content.skills.Crafting.GlassMaking;
import com.rs2.model.content.skills.Crafting.SilverCrafting;
import com.rs2.model.content.skills.Menus;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.content.skills.agility.Agility;
import com.rs2.model.content.skills.magic.MagicSkill;
import com.rs2.model.content.skills.magic.Spell;
import com.rs2.model.content.skills.smithing.Smelting;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.WalkToActionHandler;
import com.rs2.model.players.WalkToActionHandler.Actions;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemManager;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.net.StreamBuffer;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;
import com.rs2.util.Misc;
import com.rs2.util.clip.ClippedPathFinder;

public class ObjectPacketHandler implements PacketHandler {

	public static final int ITEM_ON_OBJECT = 192;
	public static final int FIRST_CLICK = 132;
	public static final int SECOND_CLICK = 252;
	public static final int THIRD_CLICK = 70;
	public static final int FOURTH_CLICK = 234;
	public static final int CAST_SPELL = 35;

	@Override
	public void handlePacket(Player player, Packet packet) {
		if (player.stopPlayerPacket()) {
			System.out.println("working2");
			return;
		}
		player.getActionSender().removeInterfaces();
		player.resetAllActions();
		switch (packet.getOpcode()) {
		case ITEM_ON_OBJECT:
			handleItemOnObject(player, packet);
			break;
		case 228:
		case FIRST_CLICK:
			handleFirstClick(player, packet);
			break;
		case SECOND_CLICK:
			handleSecondClick(player, packet);
			break;
		case THIRD_CLICK:
			handleThirdClick(player, packet);
			break;
		case FOURTH_CLICK:
			handleFourthClick(player, packet);
			break;
		case CAST_SPELL:
			handleCastedSpell(player, packet);
			break;
		}
	}

	private void handleItemOnObject(final Player player, Packet packet) {
		player.setInterfaceId(packet.getIn().readShort());
		player.setClickId(packet.getIn().readShort(true, StreamBuffer.ByteOrder.LITTLE));
		player.setClickY(packet.getIn().readShort(true, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE));
		player.setSlot(packet.getIn().readShort(StreamBuffer.ByteOrder.LITTLE));
		player.setClickX(packet.getIn().readShort(true, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE));
		player.setClickZ(player.getPosition().getZ());
		player.setClickItem(packet.getIn().readShort());
		if (player.getSlot() > 28) {
			return;
		}
		Item item = player.getInventory().getItemContainer().get(player.getSlot());
		if (item == null || item.getId() != player.getClickItem())
			return;
		RSInterface inter = RSInterface.forId(player.getInterfaceId());
		if (!player.hasInterfaceOpen(inter)) {
		    //player.getActionSender().removeInterfaces();
		return;
		}
		if(player.getClickId() == PriestInPeril.COFFIN) {
		    if(player.getQuestStage(23) == 8 && player.getClickItem() == PriestInPeril.BUCKET_OF_BLESSED_WATER) {
			player.getDialogue().sendStatement("You pour the blessed water on the vampire's coffin.", "You hear muted screams of rage from inside the coffin.", "It seems safe to say the vampire is trapped now.");
			player.getInventory().replaceItemWithItem(new Item(PriestInPeril.BUCKET_OF_BLESSED_WATER), new Item(PriestInPeril.BUCKET));
			player.getUpdateFlags().sendAnimation(832);
			player.setQuestStage(23, 9);
			return;
		    }
		}
		if(player.getClickId() == 4765 || player.getClickId() == 4766) {
		    CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
			    if (Misc.goodDistance(player.getPosition(), new Position(player.getClickX(), player.getClickY(), player.getPosition().getZ()), 2)) {
				QuestHandler.getQuests()[36].doItemOnObject(player, player.getClickId(), player.getClickItem());
				b.stop();
			    } else {
				ClippedPathFinder.getPathFinder().findRoute(player, player.getClickX(), player.getClickY(), true, 0, 0);
			    }
			}

			@Override
			public void stop() {
			}
		    }, 2);
		    
		}
		if(player.getClickId() == 2142) {
		    if(player.getQuestStage(10) >= 2) {
			if(player.getClickItem() == 2132) {
			    player.getActionSender().sendMessage("You slowly dip the beef into the cauldron.");
			    player.getInventory().removeItem(new Item(2132));
			    player.getInventory().addItem(new Item(522));
			}
			if(player.getClickItem() == 2134) {
			    player.getActionSender().sendMessage("You slowly dip the rat meat into the cauldron.");
			    player.getInventory().removeItem(new Item(2134));
			    player.getInventory().addItem(new Item(523));
			}
			if(player.getClickItem() == 2136) {
			    player.getActionSender().sendMessage("You slowly dip the bear meat into the cauldron.");
			    player.getInventory().removeItem(new Item(2136));
			    player.getInventory().addItem(new Item(524));
			}
			if(player.getClickItem() == 2138) {
			    player.getActionSender().sendMessage("You slowly dip your chicken into the cauldron.");
			    player.getInventory().removeItem(new Item(2138));
			    player.getInventory().addItem(new Item(525));
			}
		    }
		}
		if(player.getClickId() == 12100) {
		    if (item.getId() == GlassMaking.BUCKET_OF_SAND)
			    GlassMaking.makeMoltenGlass(player);
			else if (item.getId() == GemCrafting.GOLD_BAR)
			    GemCrafting.openInterface(player);
			else if (item.getId() == SilverCrafting.SILVER_BAR)
			    Menus.sendSkillMenu(player, "silverCrafting");
			else if (ItemManager.getInstance().getItemName(item.getId()).toLowerCase().endsWith("ore") && item.getId() != 668)
			    Smelting.smeltInterface(player);
			else if(item.getId() == 668)
			    Dialogues.startDialogue(player, 10200);
			return;
		}
		if(WaterfallQuest.doMiscItemOnObject(player, player.getClickId(), item.getId())) {
		    return;
		}
		/*if (!SkillHandler.checkObject(player.getClickId(), player.getClickX(), player.getClickY(), player.getPosition().getZ())) { // Server.npcHandler.getNpcByLoc(Location.create(x,
			return;
		}*/
		if (player.getStaffRights() > 1 && Constants.SERVER_DEBUG)
			System.out.println("item: "+player.getClickItem()+" object: "+player.getClickId());
		Following.resetFollow(player);
		//ObjectHandler.getObjectDetails(player, player.getClickId(), objectX, objectY);
		WalkToActionHandler.setActions(Actions.ITEM_ON_OBJECT);
		WalkToActionHandler.doActions(player);

	}

	private void handleFirstClick(Player player, Packet packet) {
		player.setClickX(packet.getIn().readShort(true, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE));
		player.setClickId(packet.getIn().readShort());
		player.setClickY(packet.getIn().readShort(StreamBuffer.ValueType.A));
		player.setClickZ(player.getPosition().getZ());
		if (player.getStaffRights() > 1 && Constants.SERVER_DEBUG)
			System.out.println("first click id = " + player.getClickId() + " x = " + player.getClickX() + " y = " + player.getClickY() + " type " + SkillHandler.getType(player.getClickId(), player.getClickX(), player.getClickY(), player.getPosition().getZ()));
		//if (!SkillHandler.checkObject(player.getClickId(), player.getClickX(), player.getClickY(), player.getPosition().getZ())) { // Server.npcHandler.getNpcByLoc(Location.create(x,
		//	return;
		//}
		if(WalkToActionHandler.doMiscObjectClicking(player, player.getClickId(), player.getClickX(), player.getClickY())) {
		    return;
		}
		if(VampireSlayer.doMiscObjectClicking(player, player.getClickId(), player.getClickX(), player.getClickY())) {
		    return;
		}
		if(GhostsAhoy.doMiscObjectClicking(player, player.getClickId(), player.getClickX(), player.getClickY())) {
		    return;
		}
		if(HorrorFromTheDeep.doMiscObjectClicking(player, player.getClickId(), player.getClickX(), player.getClickY())) {
		    return;
		}
		if (player.getClickId() == 9293) {
                if (player.getPosition().getX() == 2892) {
                    Agility.crawlPipe(player, 2886, 9799, 6, 70, 47);
                } else {
                    if(player.getPosition().getX() != 2886) {
                    player.getActionSender().sendMessage("You must be standing in front of the pipe to traverse it.");
                }
                }
            }
		if(player.getClickId() == 2404) { //phoneix gang open chest
		    if(!player.getInventory().ownsItem(763) && player.getQuestStage(13) == 4) {
			player.getDialogue().sendStatement("You find the left half of the Shield of Arrav.");
			player.getInventory().addItem(new Item(763));
			player.setQuestStage(13, 10);
			return;
		    }
		    return;
		}
		if(player.getClickId() == 3464 && player.getClickX() == 18317 && Misc.goodDistance(player.getPosition(), new Position(3439, 3336, 0), 3)) { //filliman's stones
		    player.walkTo(new Position(3439, 3336, 0), true);
		    Dialogues.sendDialogue(player, 35270, 1, 0);
		    return;
		}
		if(player.getClickId() == 3463 && player.getClickX() == 18573 && Misc.goodDistance(player.getPosition(), new Position(3440, 3335, 0), 3)) { //filliman's stones
		    player.walkTo(new Position(3440, 3335, 0), true);
		    Dialogues.sendDialogue(player, 35280, 1, 0);
		    return;
		}
		if(player.getClickId() == 3464 && player.getClickX() == 18829 && Misc.goodDistance(player.getPosition(), new Position(3441, 3336, 0), 3)) { //filliman's stones
		    player.walkTo(new Position(3441, 3336, 0), true);
		    Dialogues.sendDialogue(player, 35290, 1, 0);
		    return;
		}
		if(player.getClickId() == 2401) { //black arm open cupboard
		    new GameObject(2401, 3189, 3385, 1, 2, 10, 2400, 0);
		    return;
		}
		if(player.getClickId() == 2609) { // crandor tunnel
		    player.fadeTeleport(new Position(2834, 9657, 0));
		    return;
		}
		if(player.getClickId() == 9299) { //lumby fence
		    if (Misc.goodDistance(player.getPosition().clone(), new Position(3240, 3190, 0), 3)) {
			if (player.getPosition().getY() < 3191) {
			    Agility.crossObstacle(player, 3240, 3191, 756, 4, 0, 0);
			    player.getActionSender().sendMessage("You squeeze through the fence.");
			    return;
			} else {
			    Agility.crossObstacle(player, 3240, 3190, 756, 4, 0, 0);
			    player.getActionSender().sendMessage("You squeeze through the fence.");
			    return;
			}
		    } else {
			return;
		    }
		}
		Following.resetFollow(player);
		//ObjectHandler.getObjectDetails(player, player.getClickId(), player.getClickX(), player.getClickY());
		WalkToActionHandler.setActions(Actions.OBJECT_FIRST_CLICK);
		WalkToActionHandler.doActions(player);
	}

	private void handleSecondClick(Player player, Packet packet) {
		player.setClickId(packet.getIn().readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE));
		player.setClickY(packet.getIn().readShort(true, StreamBuffer.ByteOrder.LITTLE));
		player.setClickX(packet.getIn().readShort(StreamBuffer.ValueType.A));
		player.setClickZ(player.getPosition().getZ());
		if (player.getStaffRights() > 1 && Constants.SERVER_DEBUG)
			System.out.println("second click id = " + player.getClickId() + " x = " + player.getClickX() + " y = " + player.getClickY());
		/*if (!SkillHandler.checkObject(player.getClickId(), player.getClickX(), player.getClickY(), player.getPosition().getZ())) { // Server.npcHandler.getNpcByLoc(Location.create(x,
			return;
		}*/
		if(player.getClickId() == 2401) { //black arm open cupboard
		    if(!player.getInventory().ownsItem(765) && player.getQuestStage(13) == 7) {
			player.getDialogue().sendStatement("You find the right half of the Shield of Arrav.");
			player.getInventory().addItem(new Item(765));
			player.setQuestStage(13, 10);
			return;
		    }
		}
		if(player.getClickId() == 12100) {	
		    Smelting.smeltInterface(player);
		    return;
		}
		Following.resetFollow(player);
		ObjectHandler.getObjectDetails(player, player.getClickId(), player.getClickX(), player.getClickY());
		WalkToActionHandler.setActions(Actions.OBJECT_SECOND_CLICK);
		WalkToActionHandler.doActions(player);
	}

	private void handleThirdClick(Player player, Packet packet) {
		player.setClickX(packet.getIn().readShort(true, StreamBuffer.ByteOrder.LITTLE));
		player.setClickY(packet.getIn().readShort());
		player.setClickId(packet.getIn().readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE));
		player.setClickZ(player.getPosition().getZ());
		if (player.getStaffRights() > 1 && Constants.SERVER_DEBUG)
			System.out.println("third click id = " + player.getClickId() + " x = " + player.getClickX() + " y = " + player.getClickY());
		/*if (!SkillHandler.checkObject(player.getClickId(), player.getClickX(), player.getClickY(), player.getPosition().getZ())) { // Server.npcHandler.getNpcByLoc(Location.create(x,
			return;
		}*/
		Following.resetFollow(player);
		ObjectHandler.getObjectDetails(player, player.getClickId(), player.getClickX(), player.getClickY());
		WalkToActionHandler.setActions(Actions.OBJECT_THIRD_CLICK);
		WalkToActionHandler.doActions(player);
	}

	private void handleFourthClick(Player player, Packet packet) {
		player.setClickX(packet.getIn().readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE));
		player.setClickId(packet.getIn().readShort(StreamBuffer.ValueType.A));
		player.setClickY(packet.getIn().readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE));
		player.setClickZ(player.getPosition().getZ());
		if (player.getStaffRights() > 1 && Constants.SERVER_DEBUG)
			System.out.println("fourth click id = " + player.getClickId() + " x = " + player.getClickX() + " y = " + player.getClickY());
		/*if (!SkillHandler.checkObject(player.getClickId(), player.getClickX(), player.getClickY(), player.getPosition().getZ())) { // Server.npcHandler.getNpcByLoc(Location.create(x,
			return;
		}*/
		Following.resetFollow(player);
		ObjectHandler.getObjectDetails(player, player.getClickId(), player.getClickX(), player.getClickY());
		WalkToActionHandler.setActions(Actions.OBJECT_FOURTH_CLICK);
		WalkToActionHandler.doActions(player);
	}

	private void handleCastedSpell(Player player, Packet packet) {
		StreamBuffer.InBuffer in = packet.getIn();
		int x = in.readShort(StreamBuffer.ByteOrder.LITTLE);
		int magicId = in.readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.BIG);
		int y = in.readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.BIG);
		int objectId = in.readShort(StreamBuffer.ByteOrder.LITTLE);
		if (!SkillHandler.checkObject(objectId, x, y, player.getPosition().getZ())) { // Server.npcHandler.getNpcByLoc(Location.create(x,
			return;
		}
		Spell spell = player.getMagicBookType().getSpells().get(magicId);
		if (spell != null) {
			MagicSkill.spellOnObject(player, spell, objectId, x, y, player.getPosition().getZ());
		} else if (player.getStaffRights() > 1 && Constants.SERVER_DEBUG)
			System.out.println("Magic Id: " + magicId + " Object id: " + objectId + " X: " + x + " Y: " + y);

	}

}
