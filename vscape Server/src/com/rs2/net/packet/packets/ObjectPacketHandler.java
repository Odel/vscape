package com.rs2.net.packet.packets;

import com.rs2.Constants;
import com.rs2.cache.interfaces.RSInterface;
import com.rs2.model.content.Following;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.content.skills.magic.MagicSkill;
import com.rs2.model.content.skills.magic.Spell;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.WalkToActionHandler;
import com.rs2.model.players.WalkToActionHandler.Actions;
import com.rs2.model.players.item.Item;
import com.rs2.net.StreamBuffer;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;

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
			return;
		}
		player.getActionSender().removeInterfaces();
		player.resetAllActions();
		switch (packet.getOpcode()) {
		case ITEM_ON_OBJECT:
			handleItemOnObject(player, packet);
			break;
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

	private void handleItemOnObject(Player player, Packet packet) {
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
