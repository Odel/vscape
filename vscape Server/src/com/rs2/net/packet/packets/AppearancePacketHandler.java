package com.rs2.net.packet.packets;

import com.rs2.Constants;
import com.rs2.model.players.Player;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;

public class AppearancePacketHandler implements PacketHandler {

	public static final int APPEARANCE = 101;

	@Override
	public void handlePacket(Player player, Packet packet) {
		player.setGender(packet.getIn().readByte());
		player.getAppearance()[Constants.APPEARANCE_SLOT_HEAD] = packet.getIn().readByte();
		player.getAppearance()[Constants.APPEARANCE_SLOT_BEARD] = packet.getIn().readByte();
		player.getAppearance()[Constants.APPEARANCE_SLOT_CHEST] = packet.getIn().readByte();
		player.getAppearance()[Constants.APPEARANCE_SLOT_ARMS] = packet.getIn().readByte();
		player.getAppearance()[Constants.APPEARANCE_SLOT_HANDS] = packet.getIn().readByte();
		player.getAppearance()[Constants.APPEARANCE_SLOT_LEGS] = packet.getIn().readByte();
		player.getAppearance()[Constants.APPEARANCE_SLOT_FEET] = packet.getIn().readByte();
		player.getColors()[0] = packet.getIn().readByte();
		player.getColors()[1] = packet.getIn().readByte();
		player.getColors()[2] = packet.getIn().readByte();
		player.getColors()[3] = packet.getIn().readByte();
		player.getColors()[4] = packet.getIn().readByte();
        checkOutfitRanges(player);
	}
    
    public static void checkOutfitRanges(Player player) {
        int[] min, max;
        int gender = player.getGender();
        if (gender < 0 || gender > 1) {
            player.setDefaultAppearance();
            return;
        }
        min = Constants.COLOR_RANGES[player.getGender()][0];
        max = Constants.COLOR_RANGES[player.getGender()][1];
        for (int i = 0; i < player.getColors().length; i++) {
            if (player.getColors()[i] < min[i] || player.getColors()[i] > max[i])
                player.getColors()[i] = min[i];
        }

        min = Constants.APPEARANCE_RANGES[player.getGender()][0];
        max = Constants.APPEARANCE_RANGES[player.getGender()][1];
        for (int i = 0; i < player.getAppearance().length; i++) {
            if (gender == 1 && i == Constants.APPEARANCE_SLOT_BEARD) {
                player.getAppearance()[i] = -1;
                continue;
            }
            if (player.getAppearance()[i] < min[i] || player.getAppearance()[i] > max[i])
                player.getAppearance()[i] = min[i];
        }
    }
}