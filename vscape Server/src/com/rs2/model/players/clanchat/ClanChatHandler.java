package com.rs2.model.players.clanchat;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.rs2.model.players.Player;
import com.rs2.util.NameUtil;

//TODO: MAKE THINGS WERK
public class ClanChatHandler {
	
	public final static String clanMainDir = "./datajson/clanchats/";
		
	private static Map<Long, ClanChat> clanChats = new HashMap<Long, ClanChat>();
	
	public static void loadClans() {
		File[] listOfFiles = new File(clanMainDir).listFiles();
		if(listOfFiles != null && listOfFiles.length > 0)
		{
			for (File file : listOfFiles) {
				if (file.getName().endsWith(".gz")) {
					String fileName = file.getName().replace(".gz", "");
	    			if(clanChats.get(NameUtil.nameToLong(fileName)) == null)
	    			{
	    				ClanChat newChat = new ClanChat(NameUtil.nameToLong(fileName));
	    				try {
							newChat.Load(clanMainDir + fileName + ".gz");
						} catch (IOException e) {
							e.printStackTrace();
						}
	    				clanChats.put(NameUtil.nameToLong(fileName), newChat);
	    			}
				}
			}
		}
		System.out.println("Loaded " + clanChats.size() + " Clan Chats");
	}
	
	public static void saveClans() {
		for(ClanChat cc : clanChats.values())
		{
			try {
				cc.Save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void createClanChat(final Player player)
	{
		if(clanChats.get(player.getUsernameAsLong()) == null)
		{
			ClanChat newChat = new ClanChat(player.getUsernameAsLong(), player.getUsername());
			clanChats.put(player.getUsernameAsLong(), newChat);
			player.getActionSender().sendMessage("Clan Channel @red@" + newChat.chatName + " @bla@ has been created." , true);
			joinClanChat(player, player.getUsernameAsLong());
		}
	}
	
	public static void joinClanChat(final Player player, final long chatOwner)
	{
		if(chatOwner <= 0)
		{
			return;
		}
		player.getActionSender().sendMessage("Attempting to join chat...", true);
		ClanChat chat = getClanChat(chatOwner);
		if(chat == null)
		{
			if(player.getClanChat() != null && player.getClanChat().inChat(player.getUsernameAsLong()))
			{
				player.getActionSender().sendMessage("You are already in a clan chat.", true);
				return;
			}
			if(player.getUsernameAsLong() == chatOwner)
			{
				createClanChat(player);
				return;
			}
			player.getActionSender().sendMessage("The channel you tried to join does not exist.", true);
			return;
		}else{
			if(player.getClanChat() != null && player.getClanChat().inChat(player.getUsernameAsLong()) || (player.getClanChat() != null && player.getClanChat() != chat))
			{
				player.getActionSender().sendMessage("You are already in a clan chat.", true);
			}else{
				if(!chat.inChat(player.getUsernameAsLong()))
				{
					if(chat.isBanned(player))
					{
						player.getActionSender().sendMessage("You are temporarily banned from this Clan chat channel.", true);
					}else{
						chat.joinChat(player);
						player.getActionSender().sendMessage("You have joined the chat @red@" + player.getClanChat().chatName, true);
					}
				}
			}
		}
	}
	
	public static ClanChat getClanChat(final long chatOwner)
	{
		if(clanChats.get(chatOwner) != null)
		{
			return clanChats.get(chatOwner);
		}
		return null;
	}
	
}
