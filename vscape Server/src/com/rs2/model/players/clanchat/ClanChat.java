package com.rs2.model.players.clanchat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rs2.Constants;
import com.rs2.model.World;
import com.rs2.model.players.Player;
import com.rs2.util.NameUtil;

/* TODO:
 * CLAN SETUP INTERFACE For renaming and such
 */
public class ClanChat {
	
	private final static int MAX_CONCURRENT = 100;
	
	public long owner;
	public String chatName;
	
	public class ChatMember {
		public long user;
		public int rank;
		public long banTime;
		// 0 - member | 1 - mod | 2 - owner
		
		public ChatMember(final long user, final int rank)
		{
			this.user = user;
			this.rank = rank;
		}
	}
	private Map<Long, ChatMember> storedUsers = new HashMap<Long, ChatMember>();
	private Map<Long, ChatMember> currentUsers = new HashMap<Long, ChatMember>(MAX_CONCURRENT);

	public ClanChat(final long owner)
	{
		this.owner = owner;
	}
	
	public ClanChat(final long owner, final String chatName)
	{
		this.owner = owner;
		this.chatName = chatName;
	}
	
	public void updateCCInterface()
	{
		for(ChatMember member : currentUsers.values())
		{
			if(member != null && member.user > 0){
				Player player = World.getPlayerByName(member.user);
				if(player == null)
				{
					continue;
				}
				player.getActionSender().sendString("Talking in: " + chatName, 25002);
				player.getActionSender().sendString("Owner: " + NameUtil.formatName(NameUtil.longToName(owner)), 25003);
				for(int i = 0; i < 100; i++)
				{
					player.getActionSender().sendString("", 25122 + i);
				}
				int index = 0;
				for(ChatMember member2 : currentUsers.values())
				{
					if(member2 != null && member2.user > 0){
						Player player2 = World.getPlayerByName(member2.user);
						if(player2 != null)
						{
							String prefix = member2.rank > 0 ? "@cc"+member2.rank+"@" : "";
							if(member2.user != this.owner && player2.getStaffRights() >= 2)
							{
								prefix = "@cc3@";
							}
							player.getActionSender().sendString(prefix + NameUtil.longToName(member2.user), 25122 + index);
							index++;
						}
					}
				}
			}
		}
	}
	
	public void sendClanMessage(final Player player, String message){
		if(message.length() <= 0)
		{
			return;
		}
		if(!inChat(player.getUsernameAsLong()))
		{
			return;
		}
		for(int i = 0; i < Constants.bad.length; i++)
		{
			if(message.toLowerCase().contains(Constants.bad[i]))
			{
				player.getActionSender().sendMessage("You are trying to say something that should not be said!", true);
				return;
			}
		}
		for(ChatMember member : currentUsers.values())
		{
			if(member != null && member.user > 0){
				Player playerC = World.getPlayerByName(member.user);
				if(playerC != null)
				{
					playerC.getActionSender().sendClanChat(chatName, NameUtil.formatName(player.getUsername()), message, player.getStaffRights());
				}
			}
		}
	}
	
	public void renameClan(final Player player, String name)
	{
		ChatMember scm = getChatMember(player.getUsernameAsLong());
		if(!inChat(player.getUsernameAsLong()) || scm == null)
		{
			return;
		}
		if(scm.user != owner && player.getStaffRights() < 2) {
			player.getActionSender().sendMessage("You don't have permission to do this.", true);
			return;
		}
		if(name.length() <= 0 || name.length() > 25)
		{
			player.getActionSender().sendMessage("Failed to rename clan, Name was too short or too long.", true);
			return;
		}
		for(int i = 0; i < Constants.bad.length; i++)
		{
			if(name.toLowerCase().contains(Constants.bad[i]))
			{
				player.getActionSender().sendMessage("Failed to rename clan, Name contains banned words.", true);
				return;
			}
		}
		chatName = name;
		try {
			Save();
		} catch (IOException e) { e.printStackTrace(); }
		player.getActionSender().sendMessage("@red@Clan was renamed to "+ chatName + "." , true);
		updateCCInterface();
	}

	public void joinChat(final Player player)
	{
		if(usersInChat() >= MAX_CONCURRENT)
		{
			player.setClanChat(null);
			player.getActionSender().sendInterfaceHidden(0, 25005);
			player.getActionSender().sendInterfaceHidden(1, 25015);
			player.getActionSender().sendString("Talking in: Not in chat", 25002);
			player.getActionSender().sendString("Owner: None", 25003);
			for(int i = 0; i < 100; i++)
			{
				player.getActionSender().sendString("", 25122 + i);
			}
			player.getActionSender().sendMessage("The channel you tried to join is currently full.", true);
			return;
		}
		if(!isStored(player.getUsernameAsLong()))
		{
			int rank = player.getUsernameAsLong() == this.owner ? 2 : 0;
			storedUsers.put(player.getUsernameAsLong(), new ChatMember(player.getUsernameAsLong(), rank));
			try {
				Save();
			} catch (IOException e) { e.printStackTrace(); }
		}
		if(isStored(player.getUsernameAsLong()) && !inChat(player.getUsernameAsLong()))
		{
			ChatMember cm = getStoredMember(player.getUsernameAsLong());
			if(cm != null)
			{
				player.setClanChat(this);
				currentUsers.put(player.getUsernameAsLong(), new ChatMember(player.getUsernameAsLong(), cm.rank));
				updateCCInterface();
				player.getActionSender().sendInterfaceHidden(1, 25005);
				player.getActionSender().sendInterfaceHidden(0, 25015);
			}
		}
	}
	
	public void leaveChat(final Player player, boolean logout)
	{
		if(!inChat(player.getUsernameAsLong()))
		{
			return;
		}
		ChatMember cm = getChatMember(player.getUsernameAsLong());
		if(cm == null)
		{
			return;
		}
		currentUsers.remove(player.getUsernameAsLong());
		updateCCInterface();
		if(!logout)
		{
			player.setClanChat(null);
			player.getActionSender().sendInterfaceHidden(0, 25005);
			player.getActionSender().sendInterfaceHidden(1, 25015);
			player.getActionSender().sendString("Talking in: Not in chat", 25002);
			player.getActionSender().sendString("Owner: None", 25003);
			for(int i = 0; i < 100; i++)
			{
				player.getActionSender().sendString("", 25122 + i);
			}
		}
	}
	
	public void promotePlayer(final Player sender, final Player targetPlayer){
		ChatMember scm = getChatMember(sender.getUsernameAsLong());
		if(!inChat(sender.getUsernameAsLong()) || scm == null)
		{
			return;
		}
		if(scm.rank < 2 && sender.getStaffRights() < 2 || targetPlayer.getStaffRights() >= 2) {
			sender.getActionSender().sendMessage("You don't have permission to do this.", true);
			return;
		}
		ChatMember tscm = getStoredMember(targetPlayer.getUsernameAsLong());
		ChatMember tcm = getChatMember(targetPlayer.getUsernameAsLong());
		if(!inChat(targetPlayer.getUsernameAsLong()) || tscm == null || tcm == null)
		{
			sender.getActionSender().sendMessage("Could not find user.", true);
			return;
		}
		if(tscm.user == owner || tcm.user  == owner)
		{
			return;
		}
		if(tscm.rank >= 1 || tcm.rank >= 1)
		{
			sender.getActionSender().sendMessage("User cannot be promoted anymore.", true);
			return;
		}
		tscm.rank = 1;
		tcm.rank = 1;
		try {
			Save();
		} catch (IOException e) { e.printStackTrace(); }
		targetPlayer.getActionSender().sendMessage("You have been promoted.", true);
		sender.getActionSender().sendMessage("@red@User "+targetPlayer.getUsername()+" was promoted.", true);
		updateCCInterface();
	}
	
	public void demotePlayer(final Player sender, final Player targetPlayer){
		ChatMember scm = getChatMember(sender.getUsernameAsLong());
		if(!inChat(sender.getUsernameAsLong()) || scm == null)
		{
			return;
		}
		if(scm.rank < 2 && sender.getStaffRights() < 2 || targetPlayer.getStaffRights() >= 2) {
			sender.getActionSender().sendMessage("You don't have permission to do this.", true);
			return;
		}
		ChatMember tscm = getStoredMember(targetPlayer.getUsernameAsLong());
		ChatMember tcm = getChatMember(targetPlayer.getUsernameAsLong());
		if(!inChat(targetPlayer.getUsernameAsLong()) || tscm == null || tcm == null)
		{
			sender.getActionSender().sendMessage("Could not find user.", true);
			return;
		}
		if(tscm.user == owner || tcm.user  == owner)
		{
			return;
		}
		if(tscm.rank == 0 || tcm.rank == 0)
		{
			sender.getActionSender().sendMessage("User cannot be demoted anymore.", true);
			return;
		}
		tscm.rank = 0;
		tcm.rank = 0;
		try {
			Save();
		} catch (IOException e) { e.printStackTrace(); }
		targetPlayer.getActionSender().sendMessage("You have been demoted.", true);
		sender.getActionSender().sendMessage("@red@User "+targetPlayer.getUsername()+" was demoted.", true);
		updateCCInterface();
	}
	
	public void banPlayer(final Player banner, final Player targetPlayer){
		ChatMember bcm = getChatMember(banner.getUsernameAsLong());
		if(!inChat(banner.getUsernameAsLong()) || bcm == null)
		{
			return;
		}
		if(bcm.rank < 1 && banner.getStaffRights() < 2) {
			banner.getActionSender().sendMessage("You don't have permission to do this.", true);
			return;
		}
		ChatMember tscm = getStoredMember(targetPlayer.getUsernameAsLong());
		ChatMember tcm = getChatMember(targetPlayer.getUsernameAsLong());
		if(!inChat(targetPlayer.getUsernameAsLong()) || tscm == null || tcm == null)
		{
			banner.getActionSender().sendMessage("Could not find user.", true);
			return;
		}
		if(tscm.user == owner || tcm.user == owner)
		{
			banner.getActionSender().sendMessage("Cannot ban chat owner.", true);
			return;
		}
		if(tscm.user == banner.getUsernameAsLong() || tcm.user == banner.getUsernameAsLong())
		{
			banner.getActionSender().sendMessage("Cannot ban yourself.", true);
			return;
		}
		if(isBanned(targetPlayer))
		{
			banner.getActionSender().sendMessage("This user is already banned.", true);
			return;
		}
		if(targetPlayer.getStaffRights() >= 2)
		{	
			banner.getActionSender().sendMessage("You don't have permission to ban this user.", true);
			return;
		}
		int hours = 1;
		long banTime = System.currentTimeMillis() + (hours * 3600000l);
		tscm.banTime = banTime;
		tcm.banTime = banTime;
		try {
			Save();
		} catch (IOException e) { e.printStackTrace(); }
		targetPlayer.getActionSender().sendMessage("You have been banned from @red@"+chatName, true);
		leaveChat(targetPlayer, false);
		targetPlayer.setClanChat(null);
		banner.getActionSender().sendMessage("@red@User "+targetPlayer.getUsername()+" was banned from the chat.", true);
	}
	
	public ChatMember getStoredMember(final long userKey){
		if(storedUsers.get(userKey) != null)
		{
			return storedUsers.get(userKey);
		}
		return null;
	}
	
	public boolean isStored(final long userKey){
		return getStoredMember(userKey) != null;
	}
	
	public ChatMember getChatMember(final long userKey){
		if(currentUsers.get(userKey) != null)
		{
			return currentUsers.get(userKey);
		}
		return null;
	}
	
	public boolean inChat(final long userKey){
		return getChatMember(userKey) != null;
	}
	
	public boolean isBanned(final Player player){
		if(isStored(player.getUsernameAsLong()))
		{
			ChatMember cm = getStoredMember(player.getUsernameAsLong());
			if(cm != null)
			{
				if(cm.banTime > 0)
				{
					if(cm.banTime > System.currentTimeMillis())
					{
						return true;
					}else{
						cm.banTime = 0;
						try {
							Save();
						} catch (IOException e) { e.printStackTrace(); }
						return false;
					}
				}
			}
		}
		return false;
	}
	
	public int usersInChat()
	{
		return currentUsers.size();
	}
	
	public void Load(String directory) throws IOException {
		try(GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(directory))){
			JsonElement mainElement = new JsonParser().parse(new BufferedReader(new InputStreamReader(gzip)));
	        gzip.close();
			JsonObject mainObj = mainElement.getAsJsonObject();
			owner = mainObj.get("owner").getAsLong();
			chatName = mainObj.get("chatName").getAsString();
			JsonArray memberData = mainObj.getAsJsonArray("members");
	        for (int i = 0; i < memberData.size(); i++) {
				if(i >= memberData.size())
					break;
	        	if(memberData.get(i) != null){
	    			JsonObject memberObj = memberData.get(i).getAsJsonObject();
	    			long user = memberObj.get("user").getAsLong();
	    			int rank = memberObj.get("rank").getAsInt();
	    			long banTime = memberObj.get("banTime").getAsLong();
	    			if(!isStored(user))
	    			{
	    				ChatMember cm = new ChatMember(user, rank);
	    				cm.banTime = banTime;
	    				storedUsers.put(user, cm);
	    			}
	        	}
	        }
		} catch(IOException e) {
			System.out.println("Failed to decompress clan file");
		}
	}
	
	public void Save() throws IOException {
		try(GZIPOutputStream compress = new GZIPOutputStream(new FileOutputStream(ClanChatHandler.clanMainDir + NameUtil.longToName(owner) + ".gz"))){
		    final GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
		    final Gson gson = gsonBuilder.create();
		    JsonObject mainObject = new JsonObject();
		    mainObject.addProperty("owner", owner);
		    mainObject.addProperty("chatName", chatName);
			JsonArray memberArray = new JsonArray();
			for (ChatMember mem : storedUsers.values()) {
				if(mem == null)
					continue;
				if(mem.user <= 0)
					continue;
				JsonObject memberObj = new JsonObject();
				memberObj.addProperty("user", mem.user);
				memberObj.addProperty("rank", mem.rank);
				memberObj.addProperty("banTime", mem.banTime);
				memberArray.add(memberObj);
			}
			mainObject.add("members", memberArray);
			compress.write(gson.toJson(mainObject).getBytes());
			compress.flush();
			compress.close();
		} catch(IOException e) {
			System.out.println("Failed to compress clan file");
		}
	}
}
