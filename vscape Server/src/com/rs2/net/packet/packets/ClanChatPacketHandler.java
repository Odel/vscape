package com.rs2.net.packet.packets;

import com.rs2.model.World;
import com.rs2.model.players.Player;
import com.rs2.model.players.clanchat.ClanChatHandler;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;
import com.rs2.util.NameUtil;

public class ClanChatPacketHandler implements PacketHandler {

    public static final int JOIN_CLAN_CHAT = 60;
    public static final int ACTION_CLAN_CHAT = 61;
 //   public static final int MESSAGE_CLAN_CHAT = undefined;

	@Override
	public void handlePacket(Player player, Packet packet) {
		switch (packet.getOpcode()) {
			case JOIN_CLAN_CHAT:
				long name = packet.getIn().readLong();
				ClanChatHandler.joinClanChat(player, name);
			break;
			case ACTION_CLAN_CHAT:
				handleAction(player, packet);
			break;
		}
	}
	
	private void handleAction(Player player, Packet packet) {
		long name = packet.getIn().readLong();
		int action = packet.getIn().readShort();
		switch (action) {
			case 0 :
		        if(player.getClanChat() != null)
		        {
		        	Player target = World.getPlayerByName(NameUtil.longToName(name));
		        	if(target != null)
		        	{
		        		player.getClanChat().promotePlayer(player, target);
		        	}else{
		        		player.getActionSender().sendMessage("User "+ NameUtil.longToName(name) +" was not found.", true);
		        	}
		        }
			return;
			case 1 :
		        if(player.getClanChat() != null)
		        {
		        	Player target = World.getPlayerByName(NameUtil.longToName(name));
		        	if(target != null)
		        	{
		        		player.getClanChat().demotePlayer(player, target);
		        	}else{
		        		player.getActionSender().sendMessage("User "+ NameUtil.longToName(name) +" was not found.", true);
		        	}
		        }
			return;
			case 2 :
		        if(player.getClanChat() != null)
		        {
		        	Player banTarget = World.getPlayerByName(NameUtil.longToName(name));
		        	if(banTarget != null)
		        	{
		        		player.getClanChat().banPlayer(player, banTarget);
		        	}else{
		        		player.getActionSender().sendMessage("User "+ NameUtil.longToName(name) +" was not found.", true);
		        	}
		        }
			return;
		}
	}
    
}
