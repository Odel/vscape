package com.rs2.net.packet.packets;

import com.rs2.Constants;
import com.rs2.model.players.Player;
import com.rs2.net.StreamBuffer;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;

public class CameraAnglePacketHandler implements PacketHandler {

	public static final int CAMERA_ANGLE = 86;

	@Override
	public void handlePacket(Player player, Packet packet) {
		int a = packet.getIn().readShort();
		int b = packet.getIn().readShort(StreamBuffer.ValueType.A);
        if (player.getStaffRights() > 1 && Constants.SERVER_DEBUG)
        	System.out.println("a = " + a + "---- b = " + b);
	}

}
