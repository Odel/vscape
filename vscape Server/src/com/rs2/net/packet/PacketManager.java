package com.rs2.net.packet;

import java.io.IOException;
import java.nio.channels.SelectionKey;

import com.rs2.Constants;
import com.rs2.model.World;
import com.rs2.model.players.Player;
import com.rs2.model.players.Player.LoginStages;
import com.rs2.net.DedicatedReactor;
import com.rs2.net.packet.packets.AppearancePacketHandler;
import com.rs2.net.packet.packets.ButtonPacketHandler;
import com.rs2.net.packet.packets.CameraAnglePacketHandler;
import com.rs2.net.packet.packets.ChatInterfacePacketHandler;
import com.rs2.net.packet.packets.ChatPacketHandler;
import com.rs2.net.packet.packets.ClanChatPacketHandler;
import com.rs2.net.packet.packets.CloseInterfacePacketHandler;
import com.rs2.net.packet.packets.CommandPacketHandler;
import com.rs2.net.packet.packets.DefaultPacketHandler;
import com.rs2.net.packet.packets.FlashingSideIcon;
import com.rs2.net.packet.packets.IdleLogoutPacketHandler;
import com.rs2.net.packet.packets.ItemPacketHandler;
import com.rs2.net.packet.packets.LoadRegionPacketHandler;
import com.rs2.net.packet.packets.NpcPacketHandler;
import com.rs2.net.packet.packets.ObjectPacketHandler;
import com.rs2.net.packet.packets.PlayerOptionPacketHandler;
import com.rs2.net.packet.packets.PrivateMessagingPacketHandler;
import com.rs2.net.packet.packets.WalkPacketHandler;
import com.rs2.util.Benchmark;

public class PacketManager {

	public static final int SIZE = 256;

	public static PacketHandler[] packets = new PacketHandler[SIZE];
    public static Benchmark[] packetBenchmarks = new Benchmark[SIZE];
    
    static {
        for (int i = 0; i < SIZE; i++)
            packetBenchmarks[i] = new Benchmark();
    }

	private static DefaultPacketHandler silent = new DefaultPacketHandler();
	private static WalkPacketHandler walking = new WalkPacketHandler();
	private static ObjectPacketHandler object = new ObjectPacketHandler();
	private static ItemPacketHandler item = new ItemPacketHandler();
	private static ChatInterfacePacketHandler chatInterface = new ChatInterfacePacketHandler();
	private static PrivateMessagingPacketHandler pm = new PrivateMessagingPacketHandler();
	private static NpcPacketHandler npc = new NpcPacketHandler();
	private static PlayerOptionPacketHandler playerOption = new PlayerOptionPacketHandler();
	private static ClanChatPacketHandler cc = new ClanChatPacketHandler();

	public static void loadPackets() {
		System.out.println("Loading packets...");
		packets[WalkPacketHandler.MINI_MAP_WALK] = walking;
		packets[WalkPacketHandler.MAIN_WALK] = walking;
		packets[WalkPacketHandler.OTHER_WALK] = walking;
		packets[ObjectPacketHandler.ITEM_ON_OBJECT] = object;
		packets[ObjectPacketHandler.CAST_SPELL] = object;
		packets[ObjectPacketHandler.FIRST_CLICK] = object;
		packets[ObjectPacketHandler.SECOND_CLICK] = object;
		packets[ObjectPacketHandler.THIRD_CLICK] = object;
		packets[ObjectPacketHandler.FOURTH_CLICK] = object;
		packets[ItemPacketHandler.THIRD_CLICK_ITEM] = item;
		packets[ItemPacketHandler.DROP_ITEM] = item;
		packets[ItemPacketHandler.PICKUP_ITEM] = item;
		packets[ItemPacketHandler.SECOND_GROUND_OPTION_ITEM] = item;
		packets[ItemPacketHandler.HANDLE_OPTIONS] = item;
		packets[ItemPacketHandler.CLICK_1] = item;
		packets[ItemPacketHandler.CLICK_5] = item;
		packets[ItemPacketHandler.CLICK_10] = item;
		packets[ItemPacketHandler.CLICK_ALL] = item;
		packets[ItemPacketHandler.EQUIP_ITEM] = item;
//		packets[ItemPacketHandler.EXAMINE_ITEM] = item;
		packets[ItemPacketHandler.USE_ITEM_ON_ITEM] = item;
		packets[ItemPacketHandler.USE_ITEM_ON_GROUND_ITEM] = item;
		packets[ItemPacketHandler.FIRST_CLICK_ITEM] = item;
		packets[ItemPacketHandler.SECOND_CLICK_ITEM] = item;
		packets[ItemPacketHandler.CASTED_SPELL_ON_ITEM] = item;
		packets[ItemPacketHandler.CASTED_SPELL_ON_GROUND_ITEM] = item;
		packets[LoadRegionPacketHandler.LOAD_REGION] = new LoadRegionPacketHandler();
		packets[AppearancePacketHandler.APPEARANCE] = new AppearancePacketHandler();
		packets[CommandPacketHandler.COMMAND] = new CommandPacketHandler();
		packets[IdleLogoutPacketHandler.IDLELOGOUT] = new IdleLogoutPacketHandler();
		packets[PrivateMessagingPacketHandler.ADD_FRIEND] = pm;
		packets[PrivateMessagingPacketHandler.REMOVE_FRIEND] = pm;
		packets[PrivateMessagingPacketHandler.ADD_IGNORE] = pm;
		packets[PrivateMessagingPacketHandler.REMOVE_IGNORE] = pm;
		packets[PrivateMessagingPacketHandler.SEND_PM] = pm;
		packets[ChatInterfacePacketHandler.DIALOGUE] = chatInterface;
		packets[ChatInterfacePacketHandler.SHOW_ENTER_X] = chatInterface;
		packets[ChatInterfacePacketHandler.ENTER_X] = chatInterface;
		packets[ButtonPacketHandler.BUTTON] = new ButtonPacketHandler();
		packets[ChatPacketHandler.CHAT] = new ChatPacketHandler();
		packets[PlayerOptionPacketHandler.TRADE] = playerOption;
		packets[PlayerOptionPacketHandler.FOLLOW] = playerOption;
		packets[PlayerOptionPacketHandler.ATTACK] = playerOption;
		packets[PlayerOptionPacketHandler.TRADE_ANSWER] = playerOption;
		packets[PlayerOptionPacketHandler.TRADE_ANSWER2] = playerOption;
		packets[PlayerOptionPacketHandler.MAGIC_ON_PLAYER] = playerOption;
		packets[PlayerOptionPacketHandler.USE_ITEM_ON_PLAYER] = playerOption;
		packets[PlayerOptionPacketHandler.WHACK_PLAYER] = playerOption;
		packets[CloseInterfacePacketHandler.CLOSE_INTERFACE] = new CloseInterfacePacketHandler();
		packets[CameraAnglePacketHandler.CAMERA_ANGLE] = new CameraAnglePacketHandler();
		packets[NpcPacketHandler.FIRST_CLICK] = npc;
		packets[NpcPacketHandler.SECOND_CLICK] = npc;
		packets[NpcPacketHandler.THIRD_CLICK] = npc;
		packets[NpcPacketHandler.FOURTH_CLICK] = npc;
		packets[NpcPacketHandler.ATTACK] = npc;
		packets[NpcPacketHandler.MAGIC_ON_NPC] = npc;
		packets[NpcPacketHandler.ITEM_ON_NPC] = npc;
		packets[NpcPacketHandler.EXAMINE_NPC] = npc;
		packets[FlashingSideIcon.FLASH_ICON] = new FlashingSideIcon();
		packets[ClanChatPacketHandler.JOIN_CLAN_CHAT] = cc;
		packets[ClanChatPacketHandler.ACTION_CLAN_CHAT] = cc;
		packets[0] = silent;
		packets[241] = silent;
		packets[86] = silent;
		packets[3] = silent;
		packets[77] = silent;
		packets[210] = silent;
		packets[78] = silent;
		packets[226] = silent;
		packets[230] = silent; // added this one
		packets[36] = silent; // was constant for tele
		packets[95] = silent;
		packets[183] = object;
		packets[228] = object; //183 && 228 are stones in nature grotto
		
		int count = 0;
		for (PacketHandler packet : packets) {
			if (packet != null) {
				count++;
			}
		}
		System.out.println("Loaded " + count + " packets.");
	}

	public static void handlePacket(Player player, Packet packet)
	{
		PacketHandler packetHandler = packets[packet.getOpcode()];
		if (packetHandler == null) {
			/*if(packet.getOpcode() == 18) {
			    player.getActionSender().sendMessage("You must cast spells on Chompy birds to attack them!");
			}*/
			if (Constants.SERVER_DEBUG) {
				System.out.println("player?: " +player.getUsername() + " Unhandled packet opcode = " + packet.getOpcode() + " length = " + packet.getPacketLength());
			}
			return;
		}
		if (packet.getOpcode() <= 0 || packet.getOpcode() >= 257) {
			return;
		}
        if(packetHandler != null && packet.getOpcode() > 0 && packet.getOpcode() < 257 && packet.getOpcode() == player.getOpcode() && packet.getPacketLength() == player.getPacketLength()) 
        {
            try 
            {
    			/*if(!(packetHandler instanceof DefaultPacketHandler) && packet.getOpcode() != 202) {
    				
    			}*/
				if(player.inTempleKnightsTraining() && player.getQuestStage(35) == 5) {
				    player.getQuestVars().receivedPacket = true;
				}
                packetHandler.handlePacket(player, packet);
                player.getTimeoutStopwatch().reset();
            } catch(Exception e) {
    			e.printStackTrace();
    			player.disconnect();
            }
        }
	}

	public static final void flushOutBuffer(Player player) {
		try {
			synchronized (player.getOutData()) {
				player.getOutData().flip();
				player.getSocketChannel().write(player.getOutData());

				// Check if all the data was sent.
				if (!player.getOutData().hasRemaining()) {
					// Yep, remove write interest.
					synchronized (DedicatedReactor.getInstance()) {
						DedicatedReactor.getInstance().getSelector().wakeup();
						player.getKey().interestOps(player.getKey().interestOps() & ~SelectionKey.OP_WRITE);
					}

					// And clear the buffer.
					player.getOutData().clear();
				} else {
					// Not all data was sent - compact it!
					player.getOutData().compact();
				}
			}
		} catch (IOException ex) {
			player.disconnect();
		}
	}

	public static final void handleIncomingData(Player player) {
		try {
			// Workaround here...
			if (player.getIndex() != -1 && World.getPlayers()[player.getIndex()] != player) {
				//System.out.println("Player " + player + " disconnected for being invalid player");
				player.disconnect();
				player.setIndex(-1);
				player.getKey().attach(null);
				return;
			}
			// Read the incoming data.
			if (player.getLoginStage().compareTo(LoginStages.LOGGING_OUT) < 0 && player.getSocketChannel().read(player.getInData()) == -1) {
				//System.out.println("Player " + player + " disconnected for having invalid packet");
				player.disconnect();
				return;
			}
			// Handle the received data.
			player.getInData().flip();
			int loops = 0;
			while (player.getInData().hasRemaining()) {
				/*
				//if logged out, don't read any data
				if (player.getLoginStage().compareTo(LoginStages.LOGGING_OUT) >= 0)
				    break;
				    */
				// Handle login if we need to.
				if (player.getLoginStage().compareTo(LoginStages.LOGGED_IN) < 0) {
					player.getLogin().handleLogin(player, player.getInData());
					break;
				}
				
				if (loops++ >= 25) {
					System.out.println(player.getUsername() + " disconnected for spamming packets.");
					player.disconnect();
					break;
				}

				// Decode the packet opcode.
				if (player.getOpcode() == -1) {
					player.setOpcode(player.getInData().get() & 0xff);
					player.setOpcode(player.getOpcode() - player.getDecryptor().getNextValue() & 0xff);
				}

				// Decode the packet length.
				if (player.getPacketLength() == -1) {
					player.setPacketLength(Constants.PACKET_LENGTHS[player.getOpcode()]);
					if (player.getPacketLength() == -1) {
						if (!player.getInData().hasRemaining()) {
							player.getInData().compact();
							return;
						}
						player.setPacketLength(player.getInData().get() & 0xff);
					}
				}

				// Decode the packet payload.
				if (player.getInData().remaining() >= player.getPacketLength()) {
					int position = player.getInData().position();
					player.handlePacket();
					player.getInData().position(position + player.getPacketLength());

					// Reset for the next packet.
					player.setOpcode(-1);
					player.setPacketLength(-1);
				} else {
					player.getInData().compact();
					return;
				}
                if (!player.isLoggedIn() || player.getLoginStage().compareTo(LoginStages.LOGGING_OUT) >= 0)
                    break;
			}

			// Clear everything for the next read.
			player.getInData().clear();
		} catch (Exception ex) {
			ex.printStackTrace();
			player.disconnect();
		}
	}

	public static void setPackets(PacketHandler[] packets) {
		PacketManager.packets = packets;
	}

	public static PacketHandler[] getPackets() {
		return packets;
	}

	public static void setWalking(WalkPacketHandler walking) {
		PacketManager.walking = walking;
	}

	public static WalkPacketHandler getWalking() {
		return walking;
	}

	public static void setItem(ItemPacketHandler item) {
		PacketManager.item = item;
	}

	public static ItemPacketHandler getItem() {
		return item;
	}

	public static void setChatInterface(ChatInterfacePacketHandler chatInterface) {
		PacketManager.chatInterface = chatInterface;
	}

	public static ChatInterfacePacketHandler getChatInterface() {
		return chatInterface;
	}

	public static void setPm(PrivateMessagingPacketHandler pm) {
		PacketManager.pm = pm;
	}

	public static PrivateMessagingPacketHandler getPm() {
		return pm;
	}

	public static void setNpc(NpcPacketHandler npc) {
		PacketManager.npc = npc;
	}

	public static NpcPacketHandler getNpc() {
		return npc;
	}

	public interface PacketHandler {
		public void handlePacket(Player player, Packet packet);
	}

}
