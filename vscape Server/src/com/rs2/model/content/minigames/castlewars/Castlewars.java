package com.rs2.model.content.minigames.castlewars;

import java.util.ArrayList;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.consumables.Food;
import com.rs2.model.content.consumables.Food.FoodDef;
import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.content.minigames.castlewars.impl.CastlewarsBarricades;
import com.rs2.model.content.minigames.castlewars.impl.CastlewarsBattlements;
import com.rs2.model.content.minigames.castlewars.impl.CastlewarsCatapults;
import com.rs2.model.content.minigames.castlewars.impl.CastlewarsDoors;
import com.rs2.model.content.minigames.castlewars.impl.CastlewarsRocks;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.npcs.Npc;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.GameObjectDef;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;
import com.rs2.util.clip.Region;

public class Castlewars {
	
	private final static int LOBBY_TIME = 180; // 3 minutes
	private final static int GAME_TIME = 1200; // 1200 | 20 minutes
	private final static int PROCESS_INTERVAL = 5; // ++ 5 seconds
	private final static int TICK = 8; // 5 seconds
	private final static int PLAYERS_MIN_TEAM = 1;
    private enum LimitType {
        NOT_ENOUGH_PLAYERS, PLAYER_MINIMUM_REACHED
    }
    private static int lobbyTime = LOBBY_TIME;
    private static int gameTime = 0;
    private static boolean gameActive = false;
	
	private static ArrayList<Player> zammyLobbyPlayers = new ArrayList<Player>();
	private static ArrayList<Player> saraLobbyPlayers = new ArrayList<Player>();
	public static ArrayList<Player> zammyGamePlayers = new ArrayList<Player>();
	public static ArrayList<Player> saraGamePlayers = new ArrayList<Player>();
	
    private enum WinType {
        NONE, WIN_ZAMMY, WIN_SARA, DRAW
    }
    private static int ZAMMY_SCORE = 0, SARA_SCORE = 0;
    public static int ZAMMY_TOTAL_WINS = 0, SARA_TOTAL_WINS = 0;
    
	public final static int ZAMORAK = 0, SARADOMIN = 1, GUTHIX = -1;
    public final static int ZAMMY_BANNER = 4039;
    public final static int SARA_BANNER = 4037;
    public final static int ZAMMY_CAPE = 4042;
    public final static int SARA_CAPE = 4041;
	
	public static final MinigameAreas.Area LOBBY_EXIT_AREA = new MinigameAreas.Area(new Position(2442, 3083, 0), new Position(2439, 3096, 0));
	private static final MinigameAreas.Area ZAMMY_LOBBY_AREA = new MinigameAreas.Area(new Position(2417, 9517, 0), new Position(2428, 9531, 0));
	private static final MinigameAreas.Area SARA_LOBBY_AREA = new MinigameAreas.Area(new Position(2372, 9483, 0), new Position(2388, 9492, 0));
	
	public static final MinigameAreas.Area ZAMMY_SPAWN_ROOM = new MinigameAreas.Area(new Position(2368, 3127, 1), new Position(2376, 3135, 1));
	public static final MinigameAreas.Area SARA_SPAWN_ROOM = new MinigameAreas.Area(new Position(2423, 3072, 1), new Position(2431, 3080, 1));

	private static void ProcessLogic(){
		World.submit(new Tick(TICK) {
		    @Override 
		    public void execute() {
				try {
					if (playersInLobbyTotal() <= 0 && playersInGameTotal() <= 0) {
						this.stop();
						ResetGame();
						ResetLobby();
						return;
					}
					if (!gameActive) {
						LimitType limitType = playersInLobby();
						if (limitType == LimitType.NOT_ENOUGH_PLAYERS) {
							lobbyTime = LOBBY_TIME;
							return;
						}
						if (lobbyTime > 0) {
							lobbyTime -= PROCESS_INTERVAL;
						}
						if (lobbyTime <= 0) {
							if (limitType == LimitType.NOT_ENOUGH_PLAYERS) {
								lobbyTime = LOBBY_TIME;
								return;
							}else{
								StartGame();
								return;
							}
						}
					}else{
						if (playersInGameTotal() <= 0) {
							if (playersInLobbyTotal() <= 0) {
								this.stop();
							}
							ResetGame();
							return;
						}
						if (shouldEndGame()) {
							if (playersInLobbyTotal() <= 0) {
								this.stop();
							}
							EndGame();
							return;
						}
						if (gameTime > 0) {
							gameTime -= PROCESS_INTERVAL;
						}
						if (gameTime <= 0) {
							if (playersInLobbyTotal() <= 0) {
								this.stop();
							}
							EndGame();
							return;
						}
					}
				} catch (Exception ex){
					System.out.println("Error processing Castlewars Logic");
				}
		    }
		});
	}
	private static boolean shouldEndGame(){
		if (playersInGameTotal() > 1 && playersInGame(ZAMORAK) <= 0 || playersInGame(SARADOMIN) <= 0) {
			return true;
		}
		if (playersInGameTotal() == 1) {
			return true;
		}
		return false;
	}
	
	private static void StartGame(){
		try {
		    gameTime = GAME_TIME;
		    lobbyTime = LOBBY_TIME;
		    gameActive = true;
		    for (Player player : new ArrayList<Player>(zammyLobbyPlayers)) {
				if(player == null) {
				    continue; 
				}
				AddToGame(player, ZAMORAK);
		    }
		    for (Player player : new ArrayList<Player>(saraLobbyPlayers)) {
				if(player == null) {
				    continue; 
				}
				AddToGame(player, SARADOMIN);
		    }
		} catch (Exception ex) {
			System.out.println("Error Starting Castlewars Game");
		    ex.printStackTrace();
		}
	}
	
	private static void EndGame(){
		try {
			int ticketAmount = 0;
			String endMessage = "@red@No team won the game due to lack of players.";
			WinType winType = getWinType();
			switch(winType)
			{
				case NONE:
					ticketAmount = 0;
					endMessage = "@red@No team won the game due to lack of players.";
				break;
				case DRAW:
					if(ZAMMY_SCORE == 0 && SARA_SCORE == 0)
					{
						ticketAmount = 2;
					}
					if(ZAMMY_SCORE > 0 && SARA_SCORE > 0)
					{
						ticketAmount = 1;
					}
					endMessage = "@mag@The game resulted in a draw!";
				break;
				case WIN_SARA:
					SARA_TOTAL_WINS++;
					endMessage = "@blu@Team Saradomin has won!";
				break;
				case WIN_ZAMMY:
					ZAMMY_TOTAL_WINS++;
					endMessage = "@red@Team Zamorak has won!";
				break;
			}
		    for (Player player : new ArrayList<Player>(zammyGamePlayers)) {
				if(player == null) {
				    continue; 
				}
				player.getActionSender().sendMessage(endMessage);
				LeaveGame(player, false, winType == WinType.WIN_ZAMMY ? 2 : ticketAmount);
		    }
		    for (Player player : new ArrayList<Player>(saraGamePlayers)) {
				if(player == null) {
				    continue; 
				}
				player.getActionSender().sendMessage(endMessage);
				LeaveGame(player, false,  winType == WinType.WIN_SARA ? 2 : ticketAmount);
		    }
		} catch (Exception ex) {
			System.out.println("Error Ending Castlewars Game");
		    ex.printStackTrace();
		}
		ResetGame();
	}
	
    private static WinType getWinType() {
    	if(gameActive){
			if (playersInGameTotal() <= 0) {
				return WinType.NONE;
			}
			if (playersInGameTotal() == 1) {
				return WinType.NONE;
			}
			if (playersInGameTotal() > 1 && playersInGame(ZAMORAK) > 0 && playersInGame(SARADOMIN) <= 0) {
				return WinType.WIN_ZAMMY;
			}
			if (playersInGameTotal() > 1 && playersInGame(ZAMORAK) <= 0 && playersInGame(SARADOMIN) > 0) {
				return WinType.WIN_SARA;
			}
	    	if(ZAMMY_SCORE == SARA_SCORE)
	    	{
	    		return WinType.DRAW;
	    	}
	    	if(ZAMMY_SCORE > SARA_SCORE)
	    	{
	    		return WinType.WIN_ZAMMY;
	    	}
	    	if(SARA_SCORE > ZAMMY_SCORE)
	    	{
	    		return WinType.WIN_SARA;
	    	}
    	}
    	return WinType.NONE;
    }
	
	private static void ResetGame(){
		try{
			gameActive = false;
			gameTime = 0;
			lobbyTime = LOBBY_TIME;
			ZAMMY_SCORE = 0;
			SARA_SCORE = 0;
			zammyGamePlayers.clear();
			saraGamePlayers.clear();
			ResetBanners();
			CastlewarsRocks.ResetAllRocks();
			CastlewarsDoors.ResetDoors();
			CastlewarsBarricades.RemoveBarricades();
			CastlewarsBattlements.RemoveClimbingRopes();
			CastlewarsCatapults.ResetCatapults();
		}catch(Exception ex) { System.out.println("Problem resetting Castlewars game"); }
	}
	
	private static void ResetLobby(){
		lobbyTime = LOBBY_TIME;
		zammyLobbyPlayers.clear();
		saraLobbyPlayers.clear();
	}
	
	public static void JoinLobby(final Player player, int team) {
		if(player == null)
			return;
		if(isInLobby(player))
			return;
		if(player.getEquipment().getId(Constants.CAPE) > 0)
		{
			player.getActionSender().sendMessage("Please remove your cape before entering Castlewars.");
			return;
		}
		if(player.getEquipment().getId(Constants.HAT) > 0)
		{
			player.getActionSender().sendMessage("Please remove your headwear before entering Castlewars.");
			return;
		}
		if(player.getEquipment().getId(Constants.WEAPON) == SARA_BANNER || player.getEquipment().getId(Constants.WEAPON) == ZAMMY_BANNER
				|| player.getInventory().playerHasItem(SARA_BANNER) || player.getInventory().playerHasItem(ZAMMY_BANNER))
		{
			player.getActionSender().sendMessage("You cannot take this banner into Castlewars.");
			return;
		}
		boolean hasFood = false;
		for(Item item : player.getInventory().getItemContainer().getItems())
		{
			if(item == null)
				continue;
			FoodDef food = Food.forId(item.getId());
			if(food != null)
			{
				hasFood = true;
				break;
			}
		}
		if(hasFood)
		{
			player.getActionSender().sendMessage("Food is prohibited in Castlewars.");
			return;
		}
		if (playersInLobbyTotal() <= 0 && playersInGameTotal() <= 0) {
			if(lobbyTime != LOBBY_TIME || gameActive){
				player.getActionSender().sendMessage("Please wait and try again.");
				return;
			}
		}
		try{
			int zammyLobbyCount = playersInLobby(ZAMORAK);
			int saraLobbyCount = playersInLobby(SARADOMIN);
	    	switch(team)
	    	{
		    	case ZAMORAK :
	    			if((zammyLobbyCount < saraLobbyCount) || (zammyLobbyCount == saraLobbyCount))
	    			{
	    				AddToLobby(player, ZAMORAK);
	    			}
	    			else if(saraLobbyCount < zammyLobbyCount)
	    			{
	    				AddToLobby(player, SARADOMIN);
	    			}
		    		return;
		    	case SARADOMIN :
	    			if(zammyLobbyCount < saraLobbyCount)
	    			{
	    				AddToLobby(player, ZAMORAK);
	    			}
	    			else if((saraLobbyCount < zammyLobbyCount) || (zammyLobbyCount == saraLobbyCount))
	    			{
	    				AddToLobby(player, SARADOMIN);
	    			}
		        	return;
		    	case GUTHIX :
		    			if(zammyLobbyCount < saraLobbyCount)
		    			{
		    				AddToLobby(player, ZAMORAK);
		    			}
		    			else if(saraLobbyCount < zammyLobbyCount)
		    			{
		    				AddToLobby(player, SARADOMIN);
		    			}
		    			else if(zammyLobbyCount == saraLobbyCount)
		    			{
		    				int random = Misc.random(1);
		    				if(random == 0){
		    					AddToLobby(player, ZAMORAK);
		    				}
		    				else if(random == 1){
		    					AddToLobby(player, SARADOMIN);
		    				}
		    			}
		        	return;
	    	}	
		}catch(Exception ex) { System.out.println("Problem joining Castlewars lobby"); }
	}
	
	private static void AddToLobby(final Player player, int team) {
		if(player == null)
			return;
		if(isInLobby(player))
			return;
		if (playersInLobbyTotal() <= 0 && playersInGameTotal() <= 0) {
			if(!gameActive){
				ProcessLogic();
			}
		}
		try{
	    	switch(team)
	    	{
		    	case ZAMORAK :
		        	if (zammyLobbyPlayers != null) {
		        		zammyLobbyPlayers.add(player);
		    			player.teleport(MinigameAreas.randomPosition(ZAMMY_LOBBY_AREA));
		    			player.setCastlewarsTeam(ZAMORAK);
		    			player.getEquipment().replaceEquipment(ZAMMY_CAPE, Constants.CAPE);
		    		}
		    		return;
		    	case SARADOMIN :
		        	if (saraLobbyPlayers != null) {
		        		saraLobbyPlayers.add(player);
		        		player.teleport(MinigameAreas.randomPosition(SARA_LOBBY_AREA));
		    			player.setCastlewarsTeam(SARADOMIN);
		    			player.getEquipment().replaceEquipment(SARA_CAPE, Constants.CAPE);
		    		}
		        	return;
	    	}
		}catch(Exception ex) { System.out.println("Problem adding player to lobby game"); }
	}
	
	public static void LeaveLobby(final Player player, boolean DC){
		if(player == null)
			return;
		try{
			switch(player.getCastlewarsTeam())
			{
	    		case ZAMORAK :
		        	if (zammyLobbyPlayers != null) {
		        		if(zammyLobbyPlayers.contains(player)){
		        			zammyLobbyPlayers.remove(player);
		        		}
		    		}
	    		break;
	    		case SARADOMIN :
		        	if (saraLobbyPlayers != null) {
		        		if(saraLobbyPlayers.contains(player)){
		        			saraLobbyPlayers.remove(player);
		        		}
		    		}
		        break;
			}
	    	if(!DC)
	    	{
	    		RemoveItems(player, false);
	    		player.setCastlewarsTeam(-1);
	    		player.teleport(MinigameAreas.randomPosition(LOBBY_EXIT_AREA));
	    	}
		}catch(Exception ex) { System.out.println("Problem leaving Castlewars lobby"); }
		if (playersInLobbyTotal() <= 0) {
			ResetLobby();
		}
	}

    private static boolean isInLobby(final Player player) {
    	if(player != null && zammyLobbyPlayers != null && saraLobbyPlayers != null)
    	{
    		if(zammyLobbyPlayers.contains(player)){
    			return true;
    		}
    		if(saraLobbyPlayers.contains(player)){
    			return true;
    		}
    	}
    	return false;
    }

    private static int playersInLobby(int team) {
    	switch(team)
    	{
	    	case ZAMORAK :
	        	if (zammyLobbyPlayers != null) {
	        		return zammyLobbyPlayers.size();
	    		}
	    		break;
	    	case SARADOMIN :
	        	if (saraLobbyPlayers != null) {
	        		return saraLobbyPlayers.size();
	    		}
	    		break;
    	}
		return 0;
    }
    
    private static int playersInLobbyTotal() {
    	int count = 0;
    	if (zammyLobbyPlayers != null) {
    		count += zammyLobbyPlayers.size();
		}
    	if (saraLobbyPlayers != null) {
    		count += saraLobbyPlayers.size();
		}
		return count;
    }
    
    private static LimitType playersInLobby() {
    	if (zammyLobbyPlayers != null && saraLobbyPlayers != null) {
    		if(zammyLobbyPlayers.size() >= PLAYERS_MIN_TEAM && saraLobbyPlayers.size() >= PLAYERS_MIN_TEAM)
    		{
    			return LimitType.PLAYER_MINIMUM_REACHED;
    		}
		}
    	return LimitType.NOT_ENOUGH_PLAYERS;
    }
    
	private static void AddToGame(final Player player, int team) {
		if(player == null)
			return;
		if(isInGame(player))
			return;
		try{
			if(player.transformNpc > 0)
			{
				player.resetTransform();
			}
	    	switch(team)
	    	{
		    	case ZAMORAK :
		        	if (zammyLobbyPlayers.contains(player)) {
		        		zammyLobbyPlayers.remove(player);
					}
					zammyGamePlayers.add(player);
					player.teleport(MinigameAreas.randomPosition(ZAMMY_SPAWN_ROOM));
		    		return;
		    	case SARADOMIN :
		        	if (saraLobbyPlayers.contains(player)) {
						saraLobbyPlayers.remove(player);
					}
					saraGamePlayers.add(player);
					player.teleport(MinigameAreas.randomPosition(SARA_SPAWN_ROOM));
		        return;
	    	}
		}catch(Exception ex) { System.out.println("Problem adding player to Castlewars game"); }
	}
	
	public static void LeaveGame(final Player player, boolean DC, int ticketAmount){
		if(player == null)
			return;
		try{
			if (isInGame(player) && !player.isDead()) {
				switch(player.getCastlewarsTeam())
				{
		    		case ZAMORAK :
			        	if (zammyGamePlayers != null) {
			        		if(zammyGamePlayers.contains(player)){
			        			zammyGamePlayers.remove(player);
			        			DropBanner(player, player.getPosition().clone(), DC);
			        		}
			    		}
		    		break;
		    		case SARADOMIN :
			        	if (saraGamePlayers != null) {
			        		if(saraGamePlayers.contains(player)){
			        			saraGamePlayers.remove(player);
			        			DropBanner(player, player.getPosition().clone(), DC);
			        		}
			    		}
			        break;
				}
		    	if(gameActive && gameTime > 0 && (gameTime / 60) > 1 && playersInGameTotal() >= 1)
		    	{
		    		switch(player.getCastlewarsTeam())
		    		{
		        		case ZAMORAK :
		        			if(zammyLobbyPlayers.size() > 0){
			        			if(zammyGamePlayers.size() < saraGamePlayers.size())
			        			{
			        				Player newPlayer = zammyLobbyPlayers.get(Misc.randomMinusOne(zammyLobbyPlayers.size()));
			        				AddToGame(newPlayer, ZAMORAK);
			        			}
		        			}
		        		break;
		        		case SARADOMIN :
		        			if(saraLobbyPlayers.size() > 0){
			        			if(saraGamePlayers.size() < zammyGamePlayers.size())
			        			{
			        				Player newPlayer = saraLobbyPlayers.get(Misc.randomMinusOne(saraLobbyPlayers.size()));
			        				AddToGame(newPlayer, SARADOMIN);
			        			}
		        			}
		    	        break;
		    		}
		    	}
		    	if(!DC)
		    	{
		    		player.getActionSender().createPlayerHints(10, -1);
		    		RemoveItems(player, false);
		    		if(ticketAmount > 0)
		    		{
		    			player.getInventory().addItem(new Item(4067, ticketAmount));
		    		}
		    		player.setCastlewarsTeam(-1);
					player.resetEffects();
					player.removeAllEffects();
					player.getSkill().refresh();
		    		if(player.wearingCwBracelet())
		    		{
		    			player.damageCwBracelet();
		    		}
		    		player.teleport(MinigameAreas.randomPosition(LOBBY_EXIT_AREA));
		    	}
			}
		}catch(Exception ex) { System.out.println("Problem leaving Castlewars game"); }
		if (playersInGameTotal() <= 0) {
			ResetGame();
			if (playersInLobbyTotal() <= 0) {
				ResetLobby();
			}
		}
	}
	
    private static int playersInGame(int team) {
    	switch(team)
    	{
	    	case ZAMORAK :
	        	if (zammyGamePlayers != null) {
	        		return zammyGamePlayers.size();
	    		}
	    		break;
	    	case SARADOMIN :
	        	if (saraGamePlayers != null) {
	        		return saraGamePlayers.size();
	    		}
	    		break;
    	}
		return 0;
    }
    
    public static int playersInGameTotal() {
    	int count = 0;
    	if (zammyGamePlayers != null) {
    		count += zammyGamePlayers.size();
		}
    	if (saraGamePlayers != null) {
    		count += saraGamePlayers.size();
		}
		return count;
    }
    
    public static boolean isInGame(Player player) {
    	if(player != null && zammyGamePlayers != null && saraGamePlayers != null)
    	{
    		if(zammyGamePlayers.contains(player)){
    			return true;
    		}
    		if(saraGamePlayers.contains(player)){
    			return true;
    		}
    	}
    	return false;
    }
	
    public static boolean GameActive() {
    	return gameActive;
    }
    
    public static void lobbyInterface(Player player) {
    	try {
    		if(player == null)
    			return;
		    int minutes, seconds;
		    if (gameActive) {
		    	minutes = (gameTime + lobbyTime) / 60;
				seconds = (gameTime + lobbyTime) % 60;
		    } else {
		    	minutes = lobbyTime / 60;
		    	seconds = lobbyTime % 60;
		    }
		    LimitType limitType = playersInLobby();
		    if (gameActive) {
			    if (seconds > 9) {
			    	player.getActionSender().sendString("Game will start in " + minutes + ":" + seconds, 11480);
			    } else {
			    	player.getActionSender().sendString("Game will start in " + minutes + ":0" + seconds, 11480);
			    }
		    }else{
		    	switch(limitType)
		    	{
				case NOT_ENOUGH_PLAYERS:
					player.getActionSender().sendString("Waiting for players to join the other team.", 11480);
					break;
				case PLAYER_MINIMUM_REACHED:
				    if (seconds > 9) {
				    	player.getActionSender().sendString("Game will start in " + minutes + ":" + seconds, 11480);
				    } else {
				    	player.getActionSender().sendString("Game will start in " + minutes + ":0" + seconds, 11480);
				    }
					break;
		    	}
		    }
    	} catch (Exception ex) { }
    }
    
    public static void gameInterface(Player player) {
    	try {
    		if(player == null)
    			return;
    		if(gameActive){
			    int minutes = ((gameTime / 60) + 1);
			    if(minutes > (GAME_TIME / 60))
			    {
			    	minutes = (GAME_TIME / 60);
			    }
			    if(minutes <= 0)
			    {
			    	minutes = 0;
			    }
			    player.getActionSender().sendString(minutes + " min", 11353); //time left
				int config = 100;
				if(player.getCastlewarsTeam() == ZAMORAK)
				{
					config += (CastlewarsDoors.getSideDoorUnlocked(ZAMORAK) ? 128 : 0);
					config += (!CastlewarsRocks.getCollapsedCave(0) ? 256 : 0);
					config += (!CastlewarsRocks.getCollapsedCave(1) ? 512 : 0);
					config += (CastlewarsCatapults.getDestroyed(ZAMORAK) ? 1024 : 0);
				}
				config += player.getCastlewarsTeam() == ZAMORAK ? (getBannerTaken(ZAMORAK) ? getBannerDropped(ZAMORAK) ? 2097152 * 2 : 2097152 * 1 : 0) : (getBannerTaken(SARADOMIN) ? getBannerDropped(SARADOMIN) ? 2097152 * 2 : 2097152 * 1 : 0);
				config += 16777216 * (player.getCastlewarsTeam() == ZAMORAK ? ZAMMY_SCORE : SARA_SCORE);
				player.getActionSender().sendConfig(player.getCastlewarsTeam() == ZAMORAK ? 377 : 378, config);
				config = 100;
				if(player.getCastlewarsTeam() == SARADOMIN)
				{
					config += (CastlewarsDoors.getSideDoorUnlocked(SARADOMIN) ? 128 : 0);
					config += (!CastlewarsRocks.getCollapsedCave(2) ? 256 : 0);
					config += (!CastlewarsRocks.getCollapsedCave(3) ? 512 : 0);
					config += (CastlewarsCatapults.getDestroyed(SARADOMIN) ? 1024 : 0);
				}
				config += player.getCastlewarsTeam() == ZAMORAK ? (getBannerTaken(SARADOMIN) ? getBannerDropped(SARADOMIN) ? 2097152 * 2 : 2097152 * 1 : 0) : (getBannerTaken(ZAMORAK) ? getBannerDropped(ZAMORAK) ? 2097152 * 2 : 2097152 * 1 : 0);
				config += 16777216 * (player.getCastlewarsTeam() == ZAMORAK ? SARA_SCORE : ZAMMY_SCORE);
				player.getActionSender().sendConfig(player.getCastlewarsTeam() == ZAMORAK ? 378 : 377, config);
    		}
    	} catch (Exception ex) { }
    }
	
    public static boolean handleDeath(final Entity ent) {
    	if(ent.isPlayer()){
    		Player player = ((Player)ent);
			if (player.inCwGame()) {
				if(gameActive){
					DropBanner(player, player.getPosition().clone(), false);
		    		if(zammyGamePlayers.contains(player)){
		    			player.teleport(MinigameAreas.randomPosition(ZAMMY_SPAWN_ROOM));
		    			RemoveItems(player, true);
		    			return true;
		    		}
		    		if(saraGamePlayers.contains(player)){
		    			player.teleport(MinigameAreas.randomPosition(SARA_SPAWN_ROOM));
		    			RemoveItems(player, true);
		    			return true;
		    		}
				}else{
					player.teleport(MinigameAreas.randomPosition(LOBBY_EXIT_AREA));
					RemoveItems(player, true);
					return true;
				}
			}
    	}
    	if(ent.isNpc()){
    		Npc npc = ((Npc)ent);
    		if (npc.inCwGame() && npc.isBarricade()) {
    			CastlewarsBarricades.destroyBarricade(npc);
    			return true;
    		}
    	}
		return false;
    }
    
    public static void RemoveItems(final Player player, boolean death)
    {
    	try{
    		player.getInventory().removeItem(new Item(590, player.getInventory().getItemAmount(590)));
    		player.getInventory().removeItem(new Item(1925, player.getInventory().getItemAmount(1925)));
    		player.getInventory().removeItem(new Item(1929, player.getInventory().getItemAmount(1929)));
	    	player.getInventory().removeItem(new Item(4049, player.getInventory().getItemAmount(4049)));
	    	player.getInventory().removeItem(new Item(4051, player.getInventory().getItemAmount(4051)));
	    	player.getInventory().removeItem(new Item(4043, player.getInventory().getItemAmount(4043)));
	    	player.getInventory().removeItem(new Item(4053, player.getInventory().getItemAmount(4053)));
	    	player.getInventory().removeItem(new Item(4047, player.getInventory().getItemAmount(4047)));
	    	player.getInventory().removeItem(new Item(4045, player.getInventory().getItemAmount(4045)));
	    	player.getInventory().removeItem(new Item(1265, player.getInventory().getItemAmount(1265)));
	    	if(!death)
	    	{
	    		if(player.getEquipment().getId(Constants.WEAPON) == SARA_BANNER || player.getEquipment().getId(Constants.WEAPON) == ZAMMY_BANNER)
	    		{
	    			player.getEquipment().replaceEquipment(-1, Constants.WEAPON);
	    		}
	    		if(player.getEquipment().getId(Constants.CAPE) == ZAMMY_CAPE ||  player.getEquipment().getId(Constants.CAPE) == SARA_CAPE)
	    		{
	    			player.getEquipment().replaceEquipment(-1, Constants.CAPE);
	    		}
	    	}
	    }catch(Exception ex) { System.out.println("Problem removing Castlewars items"); }
    }
    
    private static GameObject saraBannerDropped = null;
    private static GameObject zammyBannerDropped = null;
    private final static int SARA_BANNER_DROPPED = 4900;
    private final static int ZAMMY_BANNER_DROPPED = 4901;
    private static boolean[] bannerTaken = {
    	false, //zammy
    	false //sara
    };
    private static void setBannerTaken(int team, boolean val)
    {
    	bannerTaken[team] = val;
    }
    private static boolean getBannerTaken(int team)
    {
    	return bannerTaken[team];
    }
    private static boolean getBannerDropped(int team)
    {
		switch(team)
		{
			case ZAMORAK:
				return zammyBannerDropped != null;
			case SARADOMIN:
				return saraBannerDropped != null;
		}
		return false;
    }
    
	public enum BannerData {
		ZAMMY(0, new Position(2370,3133,3), 4903, 4378, ZAMMY_BANNER_DROPPED, 3, 11),
		SARA(1, new Position(2429,3074,3), 4902, 4377, SARA_BANNER_DROPPED, 1, 11);
		
		public int teamIndex;
		public Position position;
		public int safeObjectId;
		public int takenObjectId;
		public int droppedObjectId;
		public int face;
		public int type;
		
		private BannerData(int teamIndex, Position position, int safeObjectId, int takenObjectId, int droppedObjectId, int face, int type)
		{
			this.teamIndex = teamIndex;
			this.position = position;
			this.safeObjectId = safeObjectId;
			this.takenObjectId = takenObjectId;
			this.droppedObjectId = droppedObjectId;
			this.face = face;
			this.type = type;
		}
		
		public static BannerData forTeam(int team) {
	    	for (BannerData bannerData : BannerData.values()) {
				if (bannerData.teamIndex == team) {
			    	return bannerData;
				}
	    	}
	    	return null;
		}
		
		public static BannerData forDropped(int object) {
	    	for (BannerData bannerData : BannerData.values()) {
				if (bannerData.droppedObjectId == object) {
			    	return bannerData;
				}
	    	}
	    	return null;
		}
		
		public static BannerData forStandardPosition(Position pos) {
			for (BannerData bannerData : BannerData.values()) {
				if (bannerData.position.getX() == pos.getX() && bannerData.position.getY() == pos.getY() &&
						bannerData.position.getZ() == pos.getZ()) {
			    	return bannerData;
				}
	    	}
	    	return null;
		}
	}
	
	public static boolean HandleBannerCapture(final Player player, int objectId, int x, int y, int z){
		if(!isInGame(player))
		{
			return false;
		}
		int playerTeam = player.getCastlewarsTeam();
		switch(objectId)
		{
			case 4902: //sara flag safe
				switch(playerTeam)
				{
					case ZAMORAK:
						TakeBanner(player, SARADOMIN);
					return true;
					case SARADOMIN:
						if(carryingBanner(player, ZAMORAK))
						{
							CaptureBanner(player, SARADOMIN, false);
							return true;
						}
						if(carryingBanner(player, SARADOMIN))
						{
							CaptureBanner(player, SARADOMIN, true);
							return true;
						}
					break;
				}
			return false;
			case 4377: //sara flag taken
				switch(playerTeam)
				{
					case SARADOMIN:
						if(carryingBanner(player, ZAMORAK))
						{
							CaptureBanner(player, SARADOMIN, false);
							return true;
						}
						if(carryingBanner(player, SARADOMIN))
						{
							CaptureBanner(player, SARADOMIN, true);
							return true;
						}
					break;
				}
			return false;
			case 4903: //zammy flag safe
				switch(playerTeam)
				{
					case ZAMORAK:
						if(carryingBanner(player, SARADOMIN))
						{
							CaptureBanner(player, ZAMORAK, false);
							return true;
						}
						if(carryingBanner(player, ZAMORAK))
						{
							CaptureBanner(player, ZAMORAK, true);
							return true;
						}
					break;
					case SARADOMIN:
						TakeBanner(player, ZAMORAK);
					return true;
				}
			return false;
            case 4378: //zammy flag taken
				switch(playerTeam)
				{
					case ZAMORAK:
						if(carryingBanner(player, SARADOMIN))
						{
							CaptureBanner(player, ZAMORAK, false);
							return true;
						}
						if(carryingBanner(player, ZAMORAK))
						{
							CaptureBanner(player, ZAMORAK, true);
							return true;
						}
					break;
				}
			return false;
            case 4900: // sara banner dropped
            	PickupBanner(player, SARADOMIN);
            return true;
            case 4901: // zammy banner dropped
            	PickupBanner(player, ZAMORAK);
            return true;
		}
		return false;
	}
	
	private static boolean carryingBanner(final Player player, int team){
		switch(team)
		{
			case ZAMORAK:
				if(player.getEquipment().getId(Constants.WEAPON) == ZAMMY_BANNER)
				{
					return true;
				}
			break;
			case SARADOMIN:
				if(player.getEquipment().getId(Constants.WEAPON) == SARA_BANNER)
				{
					return true;
				}
			break;
		}
		return false;
	}
	
	private static void hintPlayerBanner(int team, int type)
	{
		try{
			switch(team)
			{
				case ZAMORAK:
					for(Player playerZammy : new ArrayList<Player>(zammyGamePlayers))
					{
						if(playerZammy == null)
							continue;
						playerZammy.getActionSender().createPlayerHints(10, type);
					}
				break;
				case SARADOMIN:
					for(Player playerSara : new ArrayList<Player>(saraGamePlayers))
					{
						if(playerSara == null)
							continue;
						playerSara.getActionSender().createPlayerHints(10, type);
					}
				break;
			}
		}catch(Exception ex) { System.out.println("Problem creating Castlewars banner hint"); }
	}
	
	private static void TakeBanner(final Player player, int team)
	{
		if(!isInGame(player))
		{
			return;
		}
		if(getBannerTaken(team))
		{
			return;
		}
		if(player.carryingCwBanner()){
			return;
		}
		if(player.getEquipment().getId(Constants.WEAPON) > 0){
			if(player.getInventory().getItemContainer().freeSlots() <= 0)
			{
				player.getActionSender().sendMessage("You don't have enough room to unequip your current weapon.");
				return;
			}
		}
		try{
			switch(team)
			{
				case ZAMORAK:
			    	BannerData bannerDataZ = BannerData.forTeam(ZAMORAK);
					GameObject takenZ = ObjectHandler.getInstance().getObject(bannerDataZ.safeObjectId, bannerDataZ.position.getX(), bannerDataZ.position.getY(), bannerDataZ.position.getZ());
					if(takenZ != null){					
						ObjectHandler.getInstance().removeObject(bannerDataZ.position.getX(), bannerDataZ.position.getY(), bannerDataZ.position.getZ(), bannerDataZ.type);
					}
					new GameObject(bannerDataZ.takenObjectId, bannerDataZ.position.getX(), bannerDataZ.position.getY(), bannerDataZ.position.getZ(), bannerDataZ.face, bannerDataZ.type, bannerDataZ.takenObjectId, 99999);
					if(player.getEquipment().getId(Constants.WEAPON) > 0){
						player.getInventory().addItemOrDrop(new Item(player.getEquipment().getId(Constants.WEAPON),1));
					}
					player.getEquipment().replaceEquipment(ZAMMY_BANNER, Constants.WEAPON);
					if(player.getCastlewarsTeam() != ZAMORAK){
						hintPlayerBanner(ZAMORAK, player.getIndex());
					}
					setBannerTaken(ZAMORAK, true);
				return;
				case SARADOMIN:
			    	BannerData bannerDataS = BannerData.forTeam(SARADOMIN);
					GameObject safeS = ObjectHandler.getInstance().getObject(bannerDataS.safeObjectId, bannerDataS.position.getX(), bannerDataS.position.getY(), bannerDataS.position.getZ());
					if(safeS != null){
						ObjectHandler.getInstance().removeObject(bannerDataS.position.getX(), bannerDataS.position.getY(), bannerDataS.position.getZ(), bannerDataS.type);
					}
					new GameObject(bannerDataS.takenObjectId, bannerDataS.position.getX(), bannerDataS.position.getY(), bannerDataS.position.getZ(), bannerDataS.face, bannerDataS.type, bannerDataS.takenObjectId, 99999);
					if(player.getEquipment().getId(Constants.WEAPON) > 0){
						player.getInventory().addItemOrDrop(new Item(player.getEquipment().getId(Constants.WEAPON),1));
					}
					player.getEquipment().replaceEquipment(SARA_BANNER, Constants.WEAPON);
					if(player.getCastlewarsTeam() != SARADOMIN){
						hintPlayerBanner(SARADOMIN, player.getIndex());
					}
					setBannerTaken(SARADOMIN, true);
			    return;
			}
		}catch(Exception ex) { System.out.println("Problem taking Castlewars banner"); }
	}
	
	private static void CaptureBanner(final Player player, int teamCapturer, boolean returning)
	{
		if(!isInGame(player))
		{
			return;
		}
		if(player.getEquipment().getId(Constants.WEAPON) != SARA_BANNER && player.getEquipment().getId(Constants.WEAPON) != ZAMMY_BANNER)
		{
			return;
		}
		if(player.getEquipment().getId(Constants.WEAPON) == SARA_BANNER || player.getEquipment().getId(Constants.WEAPON) == ZAMMY_BANNER)
		{
			player.getEquipment().replaceEquipment(-1, Constants.WEAPON);
		}
		try{
			switch(teamCapturer)
			{
				case ZAMORAK:
					if(returning)
					{
						ResetBanner(ZAMORAK);
					}else{
						ZAMMY_SCORE++;
						ResetBanner(SARADOMIN);
					}
				return;
				case SARADOMIN:
					if(returning)
					{
						ResetBanner(SARADOMIN);
					}else{
						SARA_SCORE++;
						ResetBanner(ZAMORAK);
					}
			    return;
			}
		}catch(Exception ex) { System.out.println("Problem capturing Castlewars banner"); }
	}
	
	private static void PickupBanner(final Player player, int team)
	{
		if(!isInGame(player))
		{
			return;
		}
		if(!getBannerTaken(team))
		{
			return;
		}
		if(player.carryingCwBanner()){
			return;
		}
		if(player.getEquipment().getId(Constants.WEAPON) > 0){
			if(player.getInventory().getItemContainer().freeSlots() <= 0)
			{
				player.getActionSender().sendMessage("You don't have enough room to unequip your current weapon.");
				return;
			}
		}
		try{
			switch(team)
	    	{
		    	case ZAMORAK :
		    		if(zammyBannerDropped == null)
		    			return;
			    	if(zammyBannerDropped != null)
			    	{
			    		int x = zammyBannerDropped.getDef().getPosition().getX();
			    		int y = zammyBannerDropped.getDef().getPosition().getY();
			    		int z = zammyBannerDropped.getDef().getPosition().getZ();
			    		ObjectHandler.getInstance().removeObject(x, y, z, 10);
			    		new GameObject(Constants.EMPTY_OBJECT, x, y, z, 0, 10, Constants.EMPTY_OBJECT, 99999, false);
			    		zammyBannerDropped = null;
			    	}
					if(player.getEquipment().getId(Constants.WEAPON) > 0){
						player.getInventory().addItemOrDrop(new Item(player.getEquipment().getId(Constants.WEAPON),1));
					}
					player.getEquipment().replaceEquipment(ZAMMY_BANNER, Constants.WEAPON);
					if(player.getCastlewarsTeam() != ZAMORAK)
						hintPlayerBanner(ZAMORAK, player.getIndex());
	    		break;
		    	case SARADOMIN :
		    		if(saraBannerDropped == null)
		    			return;
			    	if(saraBannerDropped != null)
			    	{
			    		int x = saraBannerDropped.getDef().getPosition().getX();
			    		int y = saraBannerDropped.getDef().getPosition().getY();
			    		int z = saraBannerDropped.getDef().getPosition().getZ();
			    		ObjectHandler.getInstance().removeObject(x, y, z, 10);
			    		new GameObject(Constants.EMPTY_OBJECT, x, y, z, 0, 10, Constants.EMPTY_OBJECT, 99999, false);
			    		saraBannerDropped = null;
			    	}
					if(player.getEquipment().getId(Constants.WEAPON) > 0){
						player.getInventory().addItemOrDrop(new Item(player.getEquipment().getId(Constants.WEAPON),1));
					}
					player.getEquipment().replaceEquipment(SARA_BANNER, Constants.WEAPON);
					if(player.getCastlewarsTeam() != SARADOMIN)
						hintPlayerBanner(SARADOMIN, player.getIndex());
	    		break;
	    	}
		}catch(Exception ex) { System.out.println("Problem picking Castlewars banner up"); }
	}
    
	private static boolean canDrop(int baseX, int baseY, int z, int x, int y) {
		return Region.canMove(baseX, baseY, baseX + x, baseY + y, z, 1, 1);
	}
	
    private static void DropBanner(final Player player, Position pos, boolean DC)
    {
    	try{
    		if(!isInGame(player))
    		{
    			return;
    		}
	    	if(gameActive){
				if(player.getEquipment().getId(Constants.WEAPON) != SARA_BANNER && player.getEquipment().getId(Constants.WEAPON) != ZAMMY_BANNER)
				{
					return;
				}
				int bannerToDrop = player.getEquipment().getId(Constants.WEAPON) == SARA_BANNER ? SARADOMIN : ZAMORAK;
				if(!DC){
					if(player.getEquipment().getId(Constants.WEAPON) == SARA_BANNER || player.getEquipment().getId(Constants.WEAPON) == ZAMMY_BANNER)
					{
						player.getEquipment().replaceEquipment(-1, Constants.WEAPON);
					}
					int x = pos.getX();
					int y = pos.getY();
					int z = pos.getZ();
					GameObject empty = ObjectHandler.getInstance().getObject(Constants.EMPTY_OBJECT, x, y, z);
					if(empty != null){
						ObjectHandler.getInstance().removeObject(x, y, z, 10);
					}
					GameObjectDef objectHere = SkillHandler.getObject(x, y, z);
					if(objectHere != null && objectHere.getType() != 22){
						if (canDrop(x, y, z, 1, 0)) {
							x += 1;
						}
						else if (canDrop(x, y, z, -1, 0)) {
							x -= 1;
			            }
						if (canDrop(x, y, z, 0, 1)) {
							y += 1;
						}
						else if (canDrop(x, y, z, 0, -1)) {
							y -= 1;
			            }
					}
					GameObjectDef objectHereStil = SkillHandler.getObject(x, y, z);
					if(objectHereStil != null && objectHereStil.getType() != 22){
						switch(bannerToDrop)
				    	{
					    	case SARADOMIN :
					    		ResetBanner(SARADOMIN);
					    		break;
					    	case ZAMORAK :
					    		ResetBanner(ZAMORAK);
					    		break;
				    	}
						return;
					}
					switch(bannerToDrop)
			    	{
				    	case SARADOMIN :
				    		if(saraBannerDropped == null){
				    			saraBannerDropped = new GameObject(SARA_BANNER_DROPPED, x, y, z, 0, 10, SARA_BANNER_DROPPED, 99999);
				    			hintPlayerBanner(SARADOMIN, -1);
				    		}
				    		break;
				    	case ZAMORAK :
				    		if(zammyBannerDropped == null){
				    			zammyBannerDropped = new GameObject(ZAMMY_BANNER_DROPPED, x, y, z, 0, 10, ZAMMY_BANNER_DROPPED, 99999);
				    			hintPlayerBanner(ZAMORAK, -1);
				    		}
				    		break;
			    	}
				}else{
					switch(bannerToDrop)
			    	{
				    	case SARADOMIN :
				    		ResetBanner(SARADOMIN);
				    		break;
				    	case ZAMORAK :
				    		ResetBanner(ZAMORAK);
				    		break;
			    	}
				}
	    	}
    	}catch(Exception ex) { System.out.println("Problem dropping Castlewars banner"); }
    }
    
    private static void ResetBanner(int team)
    {
    	try{
			switch(team)
			{
				case ZAMORAK:
					hintPlayerBanner(ZAMORAK, -1);
			    	if(zammyBannerDropped != null)
			    	{
			    		int x = zammyBannerDropped.getDef().getPosition().getX();
			    		int y = zammyBannerDropped.getDef().getPosition().getY();
			    		int z = zammyBannerDropped.getDef().getPosition().getZ();
			    		ObjectHandler.getInstance().removeObject(x, y, z, 10);
			    		new GameObject(Constants.EMPTY_OBJECT, x, y, z, 0, 10, Constants.EMPTY_OBJECT, 99999, false);
			    		zammyBannerDropped = null;
			    	}
			    	BannerData bannerDataZ = BannerData.forTeam(ZAMORAK);
					GameObject takenZ = ObjectHandler.getInstance().getObject(bannerDataZ.takenObjectId, bannerDataZ.position.getX(), bannerDataZ.position.getY(), bannerDataZ.position.getZ());
					if(takenZ != null){
						ObjectHandler.getInstance().removeObject(bannerDataZ.position.getX(), bannerDataZ.position.getY(), bannerDataZ.position.getZ(), bannerDataZ.type);
						new GameObject(bannerDataZ.safeObjectId, bannerDataZ.position.getX(), bannerDataZ.position.getY(), bannerDataZ.position.getZ(), bannerDataZ.face, bannerDataZ.type, bannerDataZ.safeObjectId, 99999);
					}
					setBannerTaken(ZAMORAK, false);
				return;
				case SARADOMIN:
					hintPlayerBanner(SARADOMIN, -1);
			    	if(saraBannerDropped != null)
			    	{
			    		int x = saraBannerDropped.getDef().getPosition().getX();
			    		int y = saraBannerDropped.getDef().getPosition().getY();
			    		int z = saraBannerDropped.getDef().getPosition().getZ();
			    		ObjectHandler.getInstance().removeObject(x, y, z, 10);
			    		new GameObject(Constants.EMPTY_OBJECT, x, y, z, 0, 10, Constants.EMPTY_OBJECT, 99999, false);
			    		saraBannerDropped = null;
			    	}
			    	BannerData bannerDataS = BannerData.forTeam(SARADOMIN);
					GameObject takenS = ObjectHandler.getInstance().getObject(bannerDataS.takenObjectId, bannerDataS.position.getX(), bannerDataS.position.getY(), bannerDataS.position.getZ());
					if(takenS != null){
						ObjectHandler.getInstance().removeObject(bannerDataS.position.getX(), bannerDataS.position.getY(), bannerDataS.position.getZ(), bannerDataS.type);
						new GameObject(bannerDataS.safeObjectId, bannerDataS.position.getX(), bannerDataS.position.getY(), bannerDataS.position.getZ(), bannerDataS.face, bannerDataS.type, bannerDataS.safeObjectId, 99999);
					}
					setBannerTaken(SARADOMIN, false);
			    return;
			}
	    }catch(Exception ex) { System.out.println("Problem resetting Castlewars banner"); }
    }
    
    private static void ResetBanners()
    {
    	ResetBanner(SARADOMIN);
    	ResetBanner(ZAMORAK);
    }
}
