package com.rs2.model.content.minigames.pestcontrol;

import java.util.ArrayList;

import com.rs2.model.Entity;
import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.task.TaskScheduler;
import com.rs2.task.Task;
import com.rs2.util.Misc;

public class PestControl {

	private final static int LOBBY_TIME = 30;
	private final static int GAME_TIME = 300;
	private final static int PLAYERS_REQUIRED = 1;
	
	private static ArrayList<Player> lobbyPlayers = new ArrayList<Player>();
	private static ArrayList<Player> gamePlayers = new ArrayList<Player>();
	
	private static int lobbyTime = LOBBY_TIME;
	private static int gameTime = 0;
	
	private static boolean gameActive = false;
	
	private static final Position LOBBY_EXIT = new Position(2657,2639,0);
	private static final MinigameAreas.Area LOBBY_AREA = new MinigameAreas.Area(new Position(2660, 2638, 0), new Position(2663, 2643, 0));
	private static final MinigameAreas.Area LANDING_AREA = new MinigameAreas.Area(new Position(2656, 2609, 0), new Position(2659, 2614, 0));
	
	//6142 - 6145 normal portal
	//6146 - 6150 shielded portals

	public enum PortalData {
		WEST(6146, 6142, 2628, 2591, "W"), 
		EAST(6147, 6143, 2680, 2588, "E"), 
		SOUTHEAST(6148, 6144, 2669, 2570, "SE"), 
		SOUTHWEST(6149, 6145, 2645, 2569, "SW"); 
		
		private int shieldId;
		private int normalId;
		private int x;
		private int y;
		private String name;
		
		private PortalData(int shieldId, int normalId, int x, int y, String name) {
			this.shieldId = shieldId;
			this.normalId = normalId;
			this.x = x;
			this.y = y;
			this.name = name;
		}

		public static PortalData forShield(int shieldId)
		{
			for (PortalData portalData : PortalData.values()) {
				if (shieldId == portalData.shieldId)
					return portalData;
			}
			return null;
		}
		
		public static PortalData forNormal(int normalId)
		{
			for (PortalData portalData : PortalData.values()) {
				if (normalId == portalData.normalId)
					return portalData;
			}
			return null;
		}
	}
	private static int[] PORTAL_HEALTH = {250,250,250,250};
	private static boolean[] PORTAL_SHIELD = {true, true, true, true};
	private final static int SHIELD_TIME = 30;
	private static int shieldTime = 0;
	
	public enum GruntData {
		BRAWLER(3772, false); 
		
		private int npcId;
		private boolean attackKnight;
		
		private GruntData(int npcId, boolean attackKnight) {
			this.npcId = npcId;
			this.attackKnight = attackKnight;
		}

		public static GruntData forId(int npcId)
		{
			for (GruntData gruntData : GruntData.values()) {
				if (npcId == gruntData.npcId)
					return gruntData;
			}
			return null;
		}
	}
	private final static int GRUNT_TIME = 30;
	private static int gruntTime = 0;
	
	private static int[][] KNIGHT_DATA = {
			{3782, 2656, 2592}
	};
	private static int KNIGHT_HEALTH = 200;
	
	private static Npc knight;
	
	public static void lobbyInterface(Player player) {
		try
		{
			int minutes, seconds;
			if(gameActive) {
				minutes = (gameTime + lobbyTime) / 60;
				seconds = (gameTime + lobbyTime) % 60;
			} else {
				minutes = lobbyTime / 60;
				seconds = lobbyTime % 60;
			}
			if(seconds > 9)
				player.getActionSender().sendString("Next Departure: "+minutes+":"+seconds, 18789);
			else
				player.getActionSender().sendString("Next Departure: "+minutes+":0"+seconds, 18789);
			
			player.getActionSender().sendString("Players Ready: "+playersInLobby()+"", 18790);
			player.getActionSender().sendString("Players Required: "+ PLAYERS_REQUIRED +" minimum", 18791);
			player.getActionSender().sendString("Commendation Points: "+player.getPcPoints(), 18792);
		} catch (Exception e) {
		}
	}

	public static void gameInterface(Player player) {
		try
		{
			int minutes, seconds;
			minutes = gameTime / 60;
			seconds = gameTime % 60;
			String timeLeft;
			
			player.getActionSender().sendString(""+KNIGHT_HEALTH, 18800);
			player.getActionSender().sendString(""+player.getPcDamage(), 18801);
			for (int i = 0; i < PORTAL_HEALTH.length; i++) {
				if (PORTAL_HEALTH[i] > 0) {
					player.getActionSender().sendString(""+PORTAL_HEALTH[i], 18802+i);
				} else {
					player.getActionSender().sendString("Dead", 18802+i);
				}
			}
			if(seconds > 9)
			 	timeLeft = "Time Remaining: "+minutes+":"+seconds;
			else
				timeLeft = "Time Remaining: "+minutes+":0"+seconds;
			if(gameTime <= 20)
				player.getActionSender().sendString("@red@"+timeLeft, 18810);
			else if(gameTime > 20 && gameTime < 100)
				player.getActionSender().sendString("@or1@"+timeLeft, 18810);
			else
				player.getActionSender().sendString("@gre@"+timeLeft, 18810);
		} catch (Exception e) {
		}
	}
	
	private static void think() {
		new TaskScheduler().schedule(new Task(5, false) {
			@Override
			protected void execute() {
				if(playersInLobby() <= 0 && playersInGame() <= 0) {
					this.stop();
					resetGame();
					resetLobby();
					return;
				}
				if(!gameActive)
				{
					if(lobbyTime > 0) {
						lobbyTime -= 5;
						if (lobbyTime <= 0)
						{
							if (playersInLobby() < PLAYERS_REQUIRED) {
								lobbyTime = LOBBY_TIME;
								this.stop();
								think();
								return;
							}
							startGame();
							return;
						}
					}
				}
				else
				{
					if (gameTime > 0 ) {
						gameTime -= 5;
						if(!allPortalsUnShielded())
						{
							shieldTime += 5;
							if(shieldTime >= SHIELD_TIME)
							{
								removePortalShield();
							}
						}
						if (!allPortalsDead())
						{
							gruntTime += 5;
							if(gruntTime >= GRUNT_TIME)
							{
								spawnGrunts();
							}
						}
						if (allPortalsDead())
						{
							this.stop();
							endGame(true);
							return;
						}
						if (gameTime <= 0 && allPortalsDead()) {
							this.stop();
							endGame(true);
							return;
						}
						if (gameTime <= 0 && !allPortalsDead()) {
							this.stop();
							endGame(false);
							return;
						}
					}
				}
			}
		});
	}
	
	private static void startGame() {
		try {
			spawnMainNpcs();
			shieldTime = 0;
			gruntTime = 0;
			gameTime = GAME_TIME;
			lobbyTime = LOBBY_TIME;
			gameActive = true;
			for(Player player : new ArrayList<Player>(lobbyPlayers))
			{
				if (player != null)
				{
					if(player.inPestControlLobbyArea() && isInLobby(player))
					{
						lobbyPlayers.remove(player);
					}
					player.teleport(MinigameAreas.randomPosition(LANDING_AREA));
					gamePlayers.add(player);
					player.getActionSender().sendMessage("@blu@The Pest Control Game has begun!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void endGame(boolean gameWon) 
	{
		try {
			for(Player player : new ArrayList<Player>(gamePlayers))
			{
				if (player != null)
				{
					if(player.inPestControlGameArea())
					{
						leaveGame(player);
						if(gameWon)
						{
							player.getActionSender().sendMessage("@blu@Game Won");
						}
						else
						{
							player.getActionSender().sendMessage("@red@Game lost");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		resetGame();
	}
	
	private static void resetGame()
	{
		gameActive = false;
		shieldTime = 0;
		gruntTime = 0;
		gameTime = 0;
		lobbyTime = LOBBY_TIME;
		gamePlayers.clear();
		destroyAllNpcs();
		resetShields();
	}
	
	private static void resetLobby()
	{
		lobbyTime = LOBBY_TIME;
		lobbyPlayers.clear();
	}
	
	private static void spawnMainNpcs() {
		for(int i = 0; i < PortalData.values().length; i++) {
			PortalData data = PortalData.values()[i];
			setPortalHealth(i,250);
			NpcLoader.spawnNpc(data.shieldId, data.x, data.y, 0,true,true);
		}
		NpcLoader.spawnNpc(KNIGHT_DATA[0][0], KNIGHT_DATA[0][1], KNIGHT_DATA[0][2], 0,true,true);
		setKnightHealth(200);
		for(Npc npc : World.getNpcs())
		{
			if(npc == null)
				continue;
			if(npc.getDefinition().getName().toLowerCase().contains("knight"))
			{
				if(npc.inPestControlGameArea() && knight == null)
				{
					knight = npc;
				}
			}
		}
	}
	
	private static void spawnGrunts()
	{
		for(int i = 0; i < PortalData.values().length; i++) {
			if(!isPortalDead(i)){
				gruntTime = 0;
				PortalData portalData = PortalData.values()[i];
				GruntData gruntData = GruntData.values()[Misc.randomMinusOne(GruntData.values().length)];
				NpcLoader.spawnNpc(gruntData.npcId, portalData.x + Misc.random(3), portalData.y+ Misc.random(3), 0, true, true);
			}
		}
	}
	
	private static void removePortalShield() {
		shieldTime = 0;
		int portalToUnShield = Misc.randomMinusOne(PORTAL_SHIELD.length);
		if(!PORTAL_SHIELD[portalToUnShield])
		{
			removePortalShield();
			return;
		}
		for(Npc npc : World.getNpcs())
		{
			if(npc == null)
				continue;
			PortalData portalData = PortalData.forShield(npc.getNpcId());
			if(npc.inPestControlGameArea() && portalData != null)
			{
				npc.sendTransform(portalData.normalId, 999999);
				PORTAL_SHIELD[portalToUnShield] = false;
				sendGameMessage("@dbl@The Void Knight has disabled the "+ portalData.name +" Shield!");
			}
		}
	}
	
	private static void resetShields() {
		for (int i = 0; i < PORTAL_SHIELD.length; i++) {
			PORTAL_SHIELD[i] = true;
		}
	}
	
	private static boolean allPortalsUnShielded() {
		int count = 0;
		for (int i = 0; i < PORTAL_SHIELD.length; i++) {
			if (!PORTAL_SHIELD[i])
				count++;
		}
		return count >= PORTAL_SHIELD.length;
	}
	
	private static void destroyAllNpcs() {
		for(Npc npc : World.getNpcs())
		{
			if(npc == null)
				continue;
			if(npc.inPestControlGameArea())
			{
				NpcLoader.destroyNpc(npc);
			}
		}
	}
	
	private static void setKnightHealth(int amount) {
		if(amount <= 0)
			amount = 0;
		
		KNIGHT_HEALTH = amount;
	}
	
	private static int getKnightHealth() {
		return KNIGHT_HEALTH;
	}
	
	private static void setPortalHealth(int index, int amount) {
		if(amount <= 0)
			amount = 0;
		
		PORTAL_HEALTH[index] = amount;
	}
	
	private static int getPortalHealth(int index) {
		return PORTAL_HEALTH[index];
	}
	
	private static boolean isPortalDead(int index) {
		return getPortalHealth(index) <= 0;
	}
	
	private static boolean allPortalsDead() {
		int count = 0;
		for (int i = 0; i < PORTAL_HEALTH.length; i++) {
			if (PORTAL_HEALTH[i] <= 0)
				count++;
		}
		return count >= PORTAL_HEALTH.length;
	}
	
	public static void handleHit(final Entity attacker,final Entity victim, final int damage)
	{
		if (attacker.isPlayer() && victim.isNpc()){
			Player player = (Player) attacker;
			Npc npc = (Npc) victim;
			if (npc.getNpcId() >= 3777 && npc.getNpcId() <= 3780 || npc.getNpcId() >= 6142 && npc.getNpcId() <= 6149) {
				switch(npc.getNpcId())
				{
					case 6142:
						setPortalHealth(0, npc.getCurrentHp());
						break;
					case 6143:
						setPortalHealth(1, npc.getCurrentHp());
						break;
					case 6144:
						setPortalHealth(2, npc.getCurrentHp());
						break;
					case 6145:
						setPortalHealth(3, npc.getCurrentHp());
						break;
				}
				player.addPcDamage(damage);
			}
		}
		if (attacker.isNpc() && victim.isNpc()){
			Npc npc1 = (Npc) attacker;
			Npc npc2 = (Npc) victim;
			
			if (npc1.getNpcId() >= 3777 && npc1.getNpcId() <= 3780 || npc1.getNpcId() >= 6142 && npc1.getNpcId() <= 6149) {
				switch(npc2.getNpcId())
				{
					case 3782:
						setKnightHealth(npc2.getCurrentHp());
						break;
				}
			}
		}
	}
	
	public static void handleDeath(final Player player)
	{
		
	}
	
	private static void sendLobbyMessage(String msg)
	{
		for(Player player : new ArrayList<Player>(lobbyPlayers))
		{
			if (player != null)
			{
				player.getActionSender().sendMessage(msg);
			}
		}
	}
	
	private static void sendGameMessage(String msg)
	{
		for(Player player : new ArrayList<Player>(gamePlayers))
		{
			if (player != null)
			{
				player.getActionSender().sendMessage(msg);
			}
		}
	}
	
	private static void joinLobby(Player player) {
		if (player != null) {
			if(!isInLobby(player))
			{
				if(playersInLobby() <= 0 && playersInGame() <= 0)
				{
					think();
				}
				lobbyPlayers.add(player);
				player.getActionSender().sendMessage("You have joined the Pest Control lobby.");
				player.teleport(MinigameAreas.randomPosition(LOBBY_AREA));
			}
			else
			{
				player.getActionSender().sendMessage("@dre@You're already in the Pest Control lobby.");
			}
		}
	}
	
	public static void leaveLobby(Player player) {
		if (isInLobby(player)) {
			lobbyPlayers.remove(player);
			player.teleport(LOBBY_EXIT);
		}
	}
	
	public static void leaveGame(Player player) {
		if (isInGame(player)) {
			player.teleport(LOBBY_EXIT);
			player.resetEffects();
			player.setPcDamage(0);
			gamePlayers.remove(player);
		}
	}
	
	public static boolean handleObjectClicking(Player player, int objectId, int x, int y) 
	{
		switch(objectId)
		{
			//lander
			case 14315: //gangplank
				if(x == 2658 && y == 2639)
					if(!player.inPestControlLobbyArea())
						joinLobby(player);
			return true;
			case 14314: //ladder
				if(x == 2660 && y == 2639)
					if(player.inPestControlLobbyArea())
						leaveLobby(player);
			return true;
			//pest game objects
		}
		return false;
	}
	
	private static int playersInLobby() {
		return lobbyPlayers.size();
	}

	private static int playersInGame() {
		return gamePlayers.size();
	}
	
	private static boolean isInLobby(Player player) {
		return lobbyPlayers.contains(player);
	}
	
	private static boolean isInGame(Player player) {
		return gamePlayers.contains(player);
	}
	
}
