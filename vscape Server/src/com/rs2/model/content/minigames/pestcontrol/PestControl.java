package com.rs2.model.content.minigames.pestcontrol;

import java.util.ArrayList;

import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.Position;
import com.rs2.model.players.Player;
import com.rs2.task.TaskScheduler;
import com.rs2.task.Task;
import com.rs2.util.Misc;

public class PestControl {

	private final static int LOBBY_TIME = 30;
	private final static int GAME_TIME = 300;
	private final static int PLAYERS_REQUIRED = 2;
	
	private static ArrayList<Player> lobbyPlayers = new ArrayList<Player>();
	private static ArrayList<Player> gamePlayers = new ArrayList<Player>();
	
	private static int lobbyTime = LOBBY_TIME;
	private static int gameTime = -1;
	
	private static boolean gameActive = false;
	
	public static final Position LOBBY_EXIT = new Position(2657,2639,0);
	public static final MinigameAreas.Area LOBBY_AREA = new MinigameAreas.Area(new Position(2660, 2638, 0), new Position(2663, 2643, 0));
	public static final MinigameAreas.Area LANDING_AREA = new MinigameAreas.Area(new Position(2656, 2609, 0), new Position(2659, 2614, 0));
	
	
	//6142 - 6145 normal portal
	//6146 - 6150 shielded portals
	
	private final static int SHIELD_TIME = 60;
	private static int shieldTime = -1;
	
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
			player.getActionSender().sendString("Commendation Points: 0", 18792);
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
			
			player.getActionSender().sendString("0", 18800);
			player.getActionSender().sendString("0", 18801);
			player.getActionSender().sendString("port 1", 18802);
			player.getActionSender().sendString("port 2", 18803);
			player.getActionSender().sendString("port 3", 18804);
			player.getActionSender().sendString("port 4", 18805);
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
						gameTime -= 5;/*
						if (allPortalsDead())
							endGame(true);*/
						if (gameTime <= 0) {
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
					if(player.inPestControlGameArea() && isInGame(player))
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
			resetGame();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void resetGame()
	{
		gameTime = 0;
		lobbyTime = LOBBY_TIME;
		gameActive = false;
		gamePlayers.clear();
	}
	
	private static void resetLobby()
	{
		lobbyTime = LOBBY_TIME;
		lobbyPlayers.clear();
	}
	
	public static void spawnPortals() {
		for(int i = 0; i < 4; i++) {
			
		}
	}
	
	public static void joinLobby(Player player) {
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
	
	public static boolean isInLobby(Player player) {
		return lobbyPlayers.contains(player);
	}
	
	public static boolean isInGame(Player player) {
		return gamePlayers.contains(player);
	}
	
}
