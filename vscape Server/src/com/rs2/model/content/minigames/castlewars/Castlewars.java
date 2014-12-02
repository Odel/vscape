package com.rs2.model.content.minigames.castlewars;

import java.util.ArrayList;

import com.rs2.Constants;
import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.Following;
import com.rs2.model.content.combat.effect.impl.BurnEffect;
import com.rs2.model.content.combat.hit.Hit;
import com.rs2.model.content.combat.hit.HitDef;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.consumables.Food.FoodData;
import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.npcs.Npc;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.GameObjectDef;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;
import com.rs2.util.clip.Region;

public class Castlewars {
	
	private final static int LOBBY_TIME = 10; // 3 minutes
	private final static int GAME_TIME = 300; // 1200 | 20 minutes
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
	private static ArrayList<Player> zammyGamePlayers = new ArrayList<Player>();
	private static ArrayList<Player> saraGamePlayers = new ArrayList<Player>();
	
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
	
	private static final MinigameAreas.Area ZAMMY_SPAWN_ROOM = new MinigameAreas.Area(new Position(2368, 3127, 1), new Position(2376, 3135, 1));
	private static final MinigameAreas.Area SARA_SPAWN_ROOM = new MinigameAreas.Area(new Position(2423, 3072, 1), new Position(2431, 3080, 1));
	
	private static final MinigameAreas.Area ZAMMY_CAVE_TOP = new MinigameAreas.Area(new Position(2399, 9511, 0), new Position(2402, 9514, 0));
	private static final MinigameAreas.Area ZAMMY_CAVE_SIDE = new MinigameAreas.Area(new Position(2390, 9500, 0), new Position(2393, 9503, 0));
	private static final MinigameAreas.Area SARA_CAVE_BOTTOM = new MinigameAreas.Area(new Position(2400, 9493, 0), new Position(2403, 9496, 0));
	private static final MinigameAreas.Area SARA_CAVE_SIDE = new MinigameAreas.Area(new Position(2408, 9502, 0), new Position(2411, 9505, 0));
	
	private static final MinigameAreas.Area ZAMMY_ROCK_TOP = new MinigameAreas.Area(new Position(2400, 9512, 0), new Position(2401, 9513, 0));
	private static final MinigameAreas.Area ZAMMY_ROCK_SIDE = new MinigameAreas.Area(new Position(2391, 9501, 0), new Position(2392, 9502, 0));
	private static final MinigameAreas.Area SARA_ROCK_BOTTOM = new MinigameAreas.Area(new Position(2401, 9494, 0), new Position(2402, 9495, 0));
	private static final MinigameAreas.Area SARA_ROCK_SIDE = new MinigameAreas.Area(new Position(2409, 9503, 0), new Position(2410, 9504, 0));

	public enum CaveWallData {
		ZAMMY_TOP(0, ZAMMY_CAVE_TOP, new Position(2400,9512,0), ZAMMY_ROCK_TOP),
		ZAMMY_SIDE(1, ZAMMY_CAVE_SIDE, new Position(2391,9501,0), ZAMMY_ROCK_SIDE),
		SARA_BOTTOM(2, SARA_CAVE_BOTTOM, new Position(2401,9494,0), SARA_ROCK_BOTTOM),
		SARA_SIDE(3, SARA_CAVE_SIDE, new Position(2409,9503,0), SARA_ROCK_SIDE);
		
		public int index;
		private MinigameAreas.Area area;
		public Position rockPosition;
		public MinigameAreas.Area rockArea;
		
		private CaveWallData(int index, MinigameAreas.Area area, Position rockPosition, MinigameAreas.Area rockArea)
		{
			this.index = index;
			this.area = area;
			this.rockPosition = rockPosition;
			this.rockArea = rockArea;
		}
		
		public static CaveWallData forIndex(int index) {
	    	for (CaveWallData caveWallData : CaveWallData.values()) {
				if (caveWallData.index == index) {
			    	return caveWallData;
				}
	    	}
	    	return null;
		}
		
		public static CaveWallData forWallPosition(Position pos) {
	    	for (CaveWallData caveWallData : CaveWallData.values()) {
				if (MinigameAreas.isInArea(pos, caveWallData.area)) {
			    	return caveWallData;
				}
	    	}
	    	return null;
		}
		
		public static CaveWallData forRockPosition(Position pos) {
	    	for (CaveWallData caveWallData : CaveWallData.values()) {
				if (caveWallData.rockPosition.getX() == pos.getX() && caveWallData.rockPosition.getY() == pos.getY() &&
						caveWallData.rockPosition.getZ() == pos.getZ()) {
			    	return caveWallData;
				}
	    	}
	    	return null;
		}
	}
	
	private static boolean[] caveCollapsed = {
		true, //zammy top
		true, //zammy side
		true, //sara bottom
		true //sara side
	};

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
					ex.printStackTrace();
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
			ResetAllRocks();
			ResetDoors();
			RemoveBarricades();
			RemoveClimbingRopes();
			//ResetCatapults();
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
		if(player.getEquipment().getId(Constants.WEAPON) == SARA_BANNER || player.getEquipment().getId(Constants.WEAPON) == ZAMMY_BANNER)
		{
			player.getActionSender().sendMessage("You cannot take this banner into Castlewars.");
			return;
		}
		boolean hasFood = false;
		for(Item item : player.getInventory().getItemContainer().getItems())
		{
			if(item == null)
				continue;
			FoodData food = FoodData.forId(item.getId());
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
	    	switch(team)
	    	{
		    	case ZAMORAK :
		    		AddToLobby(player, ZAMORAK);
		    		return;
		    	case SARADOMIN :
		    		AddToLobby(player, SARADOMIN);
		        	return;
		    	case GUTHIX :
		    			int zammyLobbyCount = playersInLobby(ZAMORAK);
		    			int saraLobbyCount = playersInLobby(SARADOMIN);
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
	    		RemoveItems(player, false);
	    		if(ticketAmount > 0)
	    		{
	    			player.getInventory().addItem(new Item(4067, ticketAmount));
	    		}
	    		player.setCastlewarsTeam(-1);
				player.resetEffects();
				player.removeAllEffects();
				player.getSkill().refresh();
	    		player.teleport(MinigameAreas.randomPosition(LOBBY_EXIT_AREA));
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
    
    private static int playersInGameTotal() {
    	int count = 0;
    	if (zammyGamePlayers != null) {
    		count += zammyGamePlayers.size();
		}
    	if (saraGamePlayers != null) {
    		count += saraGamePlayers.size();
		}
		return count;
    }
    
    private static boolean isInGame(Player player) {
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
			    player.getActionSender().sendString(minutes + " min", 11155); //time left
			    player.getActionSender().sendString("Zamorak = " + ZAMMY_SCORE, 11147); // Score zammy
			    player.getActionSender().sendString(SARA_SCORE + " = Saradomin", 11148); // Score sara
			    
			    player.getActionSender().sendString(!getBannerTaken(SARADOMIN) ? "@gre@Safe" : getBannerDropped(SARADOMIN) ? "@yel@Dropped" : "@red@Taken", 11151); // sara Flag
			    player.getActionSender().sendString(!getBannerTaken(ZAMORAK) ? "@gre@Safe" : getBannerDropped(ZAMORAK) ? "@yel@Dropped" : "@red@Taken", 11152); // zammy Flag
			    //sara flag 11151
			    //zammy flag 11152
			    switch(player.getCastlewarsTeam())
			    {
			    	case ZAMORAK :
			    		player.getActionSender().sendString("@red@Health 0%", 11154); // main door
			    		player.getActionSender().sendString(getSideDoorUnlocked(ZAMORAK) ? "@red@Unlocked" : "@gre@Locked", 11158); // side door
			    		player.getActionSender().sendString(getCollapsedCave(0) ? "@gre@Collapsed" : "@red@Cleared", 11160); // collapse 1
			    		player.getActionSender().sendString(getCollapsedCave(1) ? "@gre@Collapsed" : "@red@Cleared", 11162); // collapse 2
			    		player.getActionSender().sendString("@red@Destroyed", 11164); // catapult
			    	break;
			    	case SARADOMIN :
			    		player.getActionSender().sendString("@red@dead", 11154); // main door
			    		player.getActionSender().sendString(getSideDoorUnlocked(SARADOMIN) ? "@red@Unlocked" : "@gre@Locked", 11158); // side door
			    		player.getActionSender().sendString(getCollapsedCave(2) ? "@gre@Collapsed" : "@red@Cleared", 11160); // collapse 1
			    		player.getActionSender().sendString(getCollapsedCave(3) ? "@gre@Collapsed" : "@red@Cleared", 11162); // collapse 2
			    		player.getActionSender().sendString("@red@Destroyed", 11164); // catapult
			    	break;
			    }
    		}
    	} catch (Exception ex) { }
    }
	
	public static void setCollapsedCave(int id, boolean val)
	{
		caveCollapsed[id] = val;
	}
	
	public static boolean getCollapsedCave(int id)
	{
		return caveCollapsed[id];
	}
	
	public static void collapseRock(CaveWallData caveWall)
	{
		try{
			if(caveWall != null)
			{
				setCollapsedCave(caveWall.index, true);
				int face = SkillHandler.getFace(4437, caveWall.rockPosition.getX(), caveWall.rockPosition.getY(), caveWall.rockPosition.getZ());
				GameObject empty = ObjectHandler.getInstance().getObject(Constants.EMPTY_OBJECT, caveWall.rockPosition.getX(), caveWall.rockPosition.getY(), caveWall.rockPosition.getZ());
				if(empty != null){
					ObjectHandler.getInstance().removeObject(caveWall.rockPosition.getX(), caveWall.rockPosition.getY(), caveWall.rockPosition.getZ(), 10);
				}
				new GameObject(4437, caveWall.rockPosition.getX(), caveWall.rockPosition.getY(), caveWall.rockPosition.getZ(), face, 10, Constants.EMPTY_OBJECT, 99999);
			    for (Player player : new ArrayList<Player>(zammyGamePlayers)) {
					if(player == null) {
					    continue; 
					}
					if(MinigameAreas.isInArea(player.getPosition(), caveWall.rockArea))
					{
						player.hit(player.getCurrentHp(), HitType.NORMAL);
					}
			    }
			    for (Player player : new ArrayList<Player>(saraGamePlayers)) {
					if(player == null) {
					    continue; 
					}
					if(MinigameAreas.isInArea(player.getPosition(), caveWall.rockArea))
					{
						player.hit(player.getCurrentHp(), HitType.NORMAL);
					}
			    }
			}
		}catch(Exception ex) { System.out.println("Problem collapsing Castlewars rocks"); }
	}
	
	public static void removeCollapse(CaveWallData caveWall, int id, int x, int y, int z)
	{
		try{
			if(caveWall != null)
			{
				setCollapsedCave(caveWall.index, false);
				int face = SkillHandler.getFace(id, x, y, z);
				GameObject rock = ObjectHandler.getInstance().getObject(4437, x, y, z);
				if(rock != null){
					ObjectHandler.getInstance().removeObject(x, y, z, 10);
				}
				new GameObject(Constants.EMPTY_OBJECT, x, y, z, face, 10, id, 99999, false);
				ObjectHandler.getInstance().removeClip(id, x, y, z, 10, face);
			}
		}catch(Exception ex) { System.out.println("Problem removing Castlewars rocks collapse"); }
	}
	
	private static void ResetAllRocks(){
		try{
			for(int i = 0; i < 4; i++)
			{
				if(!getCollapsedCave(i)){
					CaveWallData caveWall = CaveWallData.forIndex(i);
					if(caveWall != null)
					{
						setCollapsedCave(caveWall.index, true);
						int face = SkillHandler.getFace(4437, caveWall.rockPosition.getX(), caveWall.rockPosition.getY(), caveWall.rockPosition.getZ());
						GameObject empty = ObjectHandler.getInstance().getObject(Constants.EMPTY_OBJECT, caveWall.rockPosition.getX(), caveWall.rockPosition.getY(), caveWall.rockPosition.getZ());
						if(empty != null){
							ObjectHandler.getInstance().removeObject(caveWall.rockPosition.getX(), caveWall.rockPosition.getY(), caveWall.rockPosition.getZ(), 10);
						}
						new GameObject(4437, caveWall.rockPosition.getX(), caveWall.rockPosition.getY(), caveWall.rockPosition.getZ(), face, 10, 4437, 99999);
					}
				}
			}
		}catch(Exception ex) { System.out.println("Problem resetting Castlewars rocks"); }
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
    			destroyBarricade(npc);
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
    
	public enum SideDoorData {
		ZAMMY(0, new Position(2384,3134,0), 4467, 4468, 2),
		SARA(1, new Position(2415,3073,0), 4465, 4466, 0);
		
		public int teamIndex;
		public Position position;
		public int lockedId;
		public int unlockedId;
		public int face;
		
		private SideDoorData(int teamIndex, Position position, int lockedId, int unlockedId, int face)
		{
			this.teamIndex = teamIndex;
			this.position = position;
			this.lockedId = lockedId;
			this.unlockedId = unlockedId;
			this.face = face;
		}
		
		public static SideDoorData forTeam(int team) {
	    	for (SideDoorData sideDoorData : SideDoorData.values()) {
				if (sideDoorData.teamIndex == team) {
			    	return sideDoorData;
				}
	    	}
	    	return null;
		}
		
		public static SideDoorData forPosition(Position pos) {
			for (SideDoorData sideDoorData : SideDoorData.values()) {
				if (sideDoorData.position.getX() == pos.getX() && sideDoorData.position.getY() == pos.getY() &&
						sideDoorData.position.getZ() == pos.getZ()) {
			    	return sideDoorData;
				}
	    	}
	    	return null;
		}
	}
	private static boolean[] doorUnlocked = {
		false, //zammy
		false //sara
	};
    private static void setSideDoorUnlocked(int team, boolean val)
    {
    	doorUnlocked[team] = val;
    }
    private static boolean getSideDoorUnlocked(int team)
    {
    	return doorUnlocked[team];
    }
    
    public static boolean HandleDoors(final Player player, int objectId, int x, int y, int z)
    {
		if(!isInGame(player))
		{
			return false;
		}
		int playerTeam = player.getCastlewarsTeam();
		switch(objectId)
		{
			case 4465: //sara side door locked
				switch(playerTeam)
				{
					case ZAMORAK:
						UnlockDoor(player, SARADOMIN);
					return true;
					case SARADOMIN:
		            	player.getActionSender().walkTo(player.getPosition().getX() >= 2415 ? -1 : 1, 0, true);
		            	player.getActionSender().walkThroughDoor(objectId, x, y, z);
					return true;
				}
			return false;
			case 4466: //sara side door unlocked
				switch(playerTeam)
				{
					case SARADOMIN:
						LockDoor(player, SARADOMIN);
					return true;
				}
			return false;
			case 4467: //zammy side door locked
				switch(playerTeam)
				{
					case SARADOMIN:
						UnlockDoor(player, ZAMORAK);
					return true;
					case ZAMORAK:
		            	player.getActionSender().walkTo(player.getPosition().getX() <= 2384 ? 1 : -1, 0, true);
		            	player.getActionSender().walkThroughDoor(objectId, x, y, z);
					return true;
				}
			return false;
			case 4468: //zammy side door unlocked
				switch(playerTeam)
				{
					case ZAMORAK:
						LockDoor(player, ZAMORAK);
					return true;
				}
			return false;
		}
    	return false;
    }
    
    private static void LockDoor(final Player player, int team)
    {
		if(!isInGame(player))
		{
			return;
		}
		try{
			switch(team)
			{
				case ZAMORAK:
	            	if(!getSideDoorUnlocked(ZAMORAK)){
	            		return;
	            	}
					SideDoorData sideDoorDataz = SideDoorData.forTeam(ZAMORAK);
	                player.getActionSender().sendMessage("You lock the door.");
					if(ObjectHandler.getInstance().getObject(sideDoorDataz.unlockedId, sideDoorDataz.position.getX(), sideDoorDataz.position.getY(), sideDoorDataz.position.getZ()) != null){
						ObjectHandler.getInstance().removeObject(sideDoorDataz.position.getX(), sideDoorDataz.position.getY(), sideDoorDataz.position.getZ(), 0);
					}
					new GameObject(sideDoorDataz.lockedId, sideDoorDataz.position.getX(), sideDoorDataz.position.getY(), sideDoorDataz.position.getZ(), sideDoorDataz.face, 0, sideDoorDataz.lockedId, 99999);
					setSideDoorUnlocked(ZAMORAK, false);
				return;
				case SARADOMIN:
	            	if(!getSideDoorUnlocked(SARADOMIN)){
	            		return;
	            	}
					SideDoorData sideDoorDataS = SideDoorData.forTeam(SARADOMIN);
	                player.getActionSender().sendMessage("You lock the door.");
					if(ObjectHandler.getInstance().getObject(sideDoorDataS.unlockedId, sideDoorDataS.position.getX(), sideDoorDataS.position.getY(), sideDoorDataS.position.getZ()) != null){
						ObjectHandler.getInstance().removeObject(sideDoorDataS.position.getX(), sideDoorDataS.position.getY(), sideDoorDataS.position.getZ(), 0);
					}
					new GameObject(sideDoorDataS.lockedId, sideDoorDataS.position.getX(), sideDoorDataS.position.getY(), sideDoorDataS.position.getZ(), sideDoorDataS.face, 0, sideDoorDataS.lockedId, 99999);
					setSideDoorUnlocked(SARADOMIN, false);
				return;
			}
	    }catch(Exception ex) { System.out.println("Problem locking Castlewars side door"); }
    }
    
    private static void UnlockDoor(final Player player, int team)
    {
		if(!isInGame(player))
		{
			return;
		}
		try{
			switch(team)
			{
				case ZAMORAK:
	            	if(getSideDoorUnlocked(ZAMORAK)){
	            		return;
	            	}
					SideDoorData sideDoorDataz = SideDoorData.forTeam(ZAMORAK);
	                player.getActionSender().sendMessage("You unlock the door.");
					if(ObjectHandler.getInstance().getObject(sideDoorDataz.lockedId, sideDoorDataz.position.getX(), sideDoorDataz.position.getY(), sideDoorDataz.position.getZ()) != null){
						ObjectHandler.getInstance().removeObject(sideDoorDataz.position.getX(), sideDoorDataz.position.getY(), sideDoorDataz.position.getZ(), 0);
					}
					ObjectHandler.getInstance().removeClip(sideDoorDataz.lockedId, sideDoorDataz.position.getX(), sideDoorDataz.position.getY(), sideDoorDataz.position.getZ(), 0, sideDoorDataz.face);
					new GameObject(sideDoorDataz.unlockedId, sideDoorDataz.position.getX(), sideDoorDataz.position.getY(), sideDoorDataz.position.getZ(), sideDoorDataz.face-1, 0, sideDoorDataz.unlockedId, 99999, false);
					setSideDoorUnlocked(ZAMORAK, true);
				return;
				case SARADOMIN:
	            	if(getSideDoorUnlocked(SARADOMIN)){
	            		return;
	            	}
					SideDoorData sideDoorDataS = SideDoorData.forTeam(SARADOMIN);
	                player.getActionSender().sendMessage("You unlock the door.");
					if(ObjectHandler.getInstance().getObject(sideDoorDataS.lockedId, sideDoorDataS.position.getX(), sideDoorDataS.position.getY(), sideDoorDataS.position.getZ()) != null){
						ObjectHandler.getInstance().removeObject(sideDoorDataS.position.getX(), sideDoorDataS.position.getY(), sideDoorDataS.position.getZ(), 0);
					}
					ObjectHandler.getInstance().removeClip(sideDoorDataS.lockedId, sideDoorDataS.position.getX(), sideDoorDataS.position.getY(), sideDoorDataS.position.getZ(), 0, sideDoorDataS.face);
					new GameObject(sideDoorDataS.unlockedId, sideDoorDataS.position.getX(), sideDoorDataS.position.getY(), sideDoorDataS.position.getZ(), sideDoorDataS.face-1, 0, sideDoorDataS.unlockedId, 99999, false);
					setSideDoorUnlocked(SARADOMIN, true);
				return;
			}
	    }catch(Exception ex) { System.out.println("Problem unlocking Castlewars side door"); }
    }
    
    private static void ResetSideDoors()
    {
	    try{
	    	if(getSideDoorUnlocked(ZAMORAK)){
	    		SideDoorData sideDoorDataz = SideDoorData.forTeam(ZAMORAK);
				if(ObjectHandler.getInstance().getObject(sideDoorDataz.unlockedId, sideDoorDataz.position.getX(), sideDoorDataz.position.getY(), sideDoorDataz.position.getZ()) != null){
					ObjectHandler.getInstance().removeObject(sideDoorDataz.position.getX(), sideDoorDataz.position.getY(), sideDoorDataz.position.getZ(), 0);
				}
				new GameObject(sideDoorDataz.lockedId, sideDoorDataz.position.getX(), sideDoorDataz.position.getY(), sideDoorDataz.position.getZ(), sideDoorDataz.face, 0, sideDoorDataz.lockedId, 99999);
				setSideDoorUnlocked(ZAMORAK, false);
	    	}
	    	if(getSideDoorUnlocked(SARADOMIN)){
	    		SideDoorData sideDoorDataS = SideDoorData.forTeam(SARADOMIN);
				if(ObjectHandler.getInstance().getObject(sideDoorDataS.unlockedId, sideDoorDataS.position.getX(), sideDoorDataS.position.getY(), sideDoorDataS.position.getZ()) != null){
					ObjectHandler.getInstance().removeObject(sideDoorDataS.position.getX(), sideDoorDataS.position.getY(), sideDoorDataS.position.getZ(), 0);
				}
				new GameObject(sideDoorDataS.lockedId, sideDoorDataS.position.getX(), sideDoorDataS.position.getY(), sideDoorDataS.position.getZ(), sideDoorDataS.face, 0, sideDoorDataS.lockedId, 99999);
				setSideDoorUnlocked(SARADOMIN, false);
	    	}
	    }catch(Exception ex) { System.out.println("Problem resetting Castlewars side doors"); }
    }
    //main door
    
    private static void ResetDoors()
    {
    	ResetSideDoors();
    	//ResetMainDoors();
    }
    
    private final static int MAX_BARRICADES = 10;
    private static ArrayList<Npc> barricadesZammy = new ArrayList<Npc>(MAX_BARRICADES);
    private static ArrayList<Npc> barricadesSara = new ArrayList<Npc>(MAX_BARRICADES);
    private final static MinigameAreas.Area[] badArea = {
    	ZAMMY_SPAWN_ROOM,
    	SARA_SPAWN_ROOM,
    	new MinigameAreas.Area(new Position(2371, 3118, 0), new Position(2374, 3121, 0)), // zammy main door
    	new MinigameAreas.Area(new Position(2425, 3087, 0), new Position(2428, 3089, 0)), // sara main door
    	ZAMMY_ROCK_TOP,
    	ZAMMY_ROCK_SIDE,
    	SARA_ROCK_BOTTOM,
    	SARA_ROCK_SIDE
    };
    
    private final static Position[] badPos = {
    	new Position(2417, 3077, 0), //sara
    	new Position(2416, 3074, 0),
    	new Position(2414, 3073, 0),
    	new Position(2421, 3074, 0),
    	new Position(2430, 3081, 0),
    	new Position(2430, 9481, 0),
    	new Position(2399, 9500, 0),
    	new Position(2399, 3100, 0),
    	new Position(2420, 3080, 1),
    	new Position(2421, 3074, 1),
    	new Position(2422, 3076, 1),
    	new Position(2426, 3081, 1),
    	new Position(2427, 3081, 1),
    	new Position(2430, 3080, 2),
    	new Position(2425, 3077, 2),
    	new Position(2426, 3074, 3),
    	new Position(2400, 3107, 0),//zammy
    	new Position(2400, 9507, 0),
    	new Position(2369, 9526, 0),
    	new Position(2369, 3126, 0),
    	new Position(2378, 3133, 0),
    	new Position(2385, 3134, 0),
    	new Position(2382, 3130, 0),
    	new Position(2383, 3133, 0),
    	new Position(2380, 3130, 0),
    	new Position(2378, 3133, 1),
    	new Position(2377, 3131, 1),
    	new Position(2379, 3127, 1),
    	new Position(2373, 3126, 1),
    	new Position(2372, 3126, 1),
    	new Position(2369, 3127, 2),
    	new Position(2374, 3130, 2),
    	new Position(2373, 3133, 3),
    };
    
    private static boolean barricadeBadPos(final Player player)
    {
    	Position pos = player.getPosition();
    	for(MinigameAreas.Area area : badArea){
	    	if(MinigameAreas.isInArea(pos, area))
	    	{
	    		return true;
	    	}
    	}
    	for(Position position : badPos){
	    	if(pos.getX() == position.getX() && pos.getY() == position.getY() && pos.getZ() == position.getZ())
	    	{
	    		return true;
	    	}
    	}
    	return false;
    }
    
    public static boolean HandleItemOnBarricades(final Player player, int item, Npc npc)
    {
		if(isBarricade(npc)){
			if(!isInGame(player))
			{
				return false;
			}
			final int x = npc.getPosition().getX();
			final int y = npc.getPosition().getY();
			final int z = npc.getPosition().getZ();
			switch(item)
			{
				case 590 :
					if(SetBarricadeFire(player, npc, x, y, z))
					{
						return true;
					}
				return false;
				case 1929 :
					if(ExtinguishBarricadeFire(player, npc, x, y, z))
					{
						return true;
					}
				return false;
				case 4045 :
					if(ExplodeBarricade(player, npc, x, y, z))
					{
						return true;
					}
				return false;
			}
		}
		return false;
    }
    
    
    private static Npc spawnBarricade(int id, Position pos)
    {
    	new GameObject(4421, pos.getX(), pos.getY(), pos.getZ(), 0, 10, Constants.EMPTY_OBJECT, 99999);
    	Npc npc = new Npc(1532);
	    npc.setPosition(pos);
	    npc.setSpawnPosition(pos);
	    npc.setCurrentX(pos.getX());
	    npc.setCurrentY(pos.getY());
	    World.register(npc);
	    return npc;
    }
    
    private static void destroyBarricade(Npc npc){
    	try{
			GameObject barricade = ObjectHandler.getInstance().getObject(4421, npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ());
			if(barricade != null){
				ObjectHandler.getInstance().removeObject(4421, npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ(), 10);
			}
			GameObject empty = ObjectHandler.getInstance().getObject(Constants.EMPTY_OBJECT, npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ());
			if(empty != null){
				ObjectHandler.getInstance().removeObject(npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ(), 10);
			}
			if(barricadesZammy.contains(npc))
			{
				barricadesZammy.remove(npc);
			}
			if(barricadesSara.contains(npc))
			{
				barricadesSara.remove(npc);
			}
    	}catch(Exception ex) { System.out.println("Problem destroying Castlewars barricade"); }
    }
    
    private static boolean isBarricade(Npc npc){
    	if(npc.inCwGame()){
			if(barricadesZammy.contains(npc))
			{
				return true;
			}
			if(barricadesSara.contains(npc))
			{
				return true;
			}
    	}
    	return false;
    }
    
    public static void PlaceBarricade(final Player player, int item, int slot){
		if(!isInGame(player))
		{
			return;
		}
		try{
			if(barricadeBadPos(player))
			{
				player.getActionSender().sendMessage("You cannot place a barricade here!");
				return;
			}
			final int x = player.getPosition().getX();
			final int y = player.getPosition().getY();
			final int z = player.getPosition().getZ();
			if(player.getCastlewarsTeam() == ZAMORAK){
				if(barricadesZammy.size() >= MAX_BARRICADES)
				{
					player.getActionSender().sendMessage("Your team has hit the barricade limit.");
					return;
				}
				GameObject empty = ObjectHandler.getInstance().getObject(Constants.EMPTY_OBJECT, x, y, z);
				if(empty != null){
					ObjectHandler.getInstance().removeObject(x, y, z, 10);
				}
				GameObjectDef objectHere = SkillHandler.getObject(x, y, z);
				if(objectHere != null && objectHere.getType() != 22){
					player.getActionSender().sendMessage("You cannot place a barricade here!");
					return;
				}
				player.getInventory().removeItemSlot(new Item(4053,1), slot);
				barricadesZammy.add(spawnBarricade(1532,new Position(x,y,z)));
				return;
			}
			if(player.getCastlewarsTeam() == SARADOMIN){
				if(barricadesSara.size() >= MAX_BARRICADES)
				{
					player.getActionSender().sendMessage("Your team has hit the barricade limit.");
					return;
				}
				GameObject empty = ObjectHandler.getInstance().getObject(Constants.EMPTY_OBJECT, x, y, z);
				if(empty != null){
					ObjectHandler.getInstance().removeObject(x, y, z, 10);
				}
				GameObjectDef objectHere = SkillHandler.getObject(x, y, z);
				if(objectHere != null && objectHere.getType() != 22){
					player.getActionSender().sendMessage("You cannot place a barricade here!");
					return;
				}
				player.getInventory().removeItemSlot(new Item(4053,1), slot);
				barricadesSara.add(spawnBarricade(1532,new Position(x,y,z)));
				return;
			}
		}catch(Exception ex) { System.out.println("Problem placing Castlewars barricade"); }
    }
    
    public static boolean SetBarricadeFire(final Player player, final Npc npc, final int x, final int y, final int z){
		try{
			if(isBarricade(npc)){
				if(npc.getNpcId() == 1533)
				{
					player.getActionSender().sendMessage("This barricade is already on fire.");
					return true;
				}
				if(npc.getNpcId() == 1532)
				{
					int time = 3 + Misc.random(6);
					player.getActionSender().sendMessage("You attempt to light the barricade on fire.");
					player.getUpdateFlags().sendAnimation(733);
					player.getActionSender().sendSound(375, 0, 0);
					final int task = player.getTask();
			        player.setSkilling(new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {
							if (!player.checkTask(task) || npc.getNpcId() == 1533) {
								container.stop();
								return;
							}
							if (Misc.random(2) == 0) {
								player.getActionSender().sendMessage("The fire catches and the barricade begins to burn.");
								npc.sendTransform(1533, 99999999);
								HitDef hitDef = new HitDef(null, HitType.BURN, Math.ceil(4.0)).setStartingHitDelay(-1).setUnblockable(true).setDoBlock(false);
								Hit hit = new Hit(npc, npc, hitDef);
								BurnEffect burn = new BurnEffect(5, 5);
								burn.initialize(hit);
								container.stop();
								return;
							}else{
								player.getActionSender().sendMessage("You fail to set the barricade on fire.");
								container.stop();
							}
						}
						@Override
						public void stop() {
							player.resetAnimation();
						}
					});
			        CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), time);
					return true;
				}
			}
		}catch(Exception ex) { System.out.println("Problem setting Castlewars barricade fire"); }
		return false;
    }
    
    public static boolean ExtinguishBarricadeFire(final Player player, final Npc npc, final int x, final int y, final int z){
		try{
			if(isBarricade(npc)){
				if(npc.getNpcId() == 1533)
				{
					if(!player.getInventory().removeItemSlot(new Item(1929,1), player.getSlot())){
						player.getInventory().removeItem(new Item(1929,1));
						player.getInventory().addItemOrDrop(new Item(1925,1));
					}else{
						player.getInventory().addItemToSlot(new Item(1925,1), player.getSlot());
					}
					npc.removeAllEffects();
					npc.sendTransform(1532, 99999999);
					player.getActionSender().sendMessage("You extinguish the fire.");
					return true;
				}
			}
		}catch(Exception ex) { System.out.println("Problem setting Castlewars barricade fire"); }
		return false;
    }
    
    private static boolean ExplodeBarricade(final Player player, final Npc npc, final int x, final int y, final int z){
		try{
			if(isBarricade(npc)){
				if(!player.getInventory().removeItemSlot(new Item(4045,1), player.getSlot())){
					player.getInventory().removeItem(new Item(4045,1));
				}
				npc.hit((npc.getMaxHp() / 2), HitType.BURN);
		    	player.getActionSender().sendSoundRadius(97, 0, 0, npc.getPosition(), 5);
		    	npc.getUpdateFlags().sendHighGraphic(346);
				player.getActionSender().sendMessage("You throw the potion at the barricade and it explodes.");
				return true;
			}
		}catch(Exception ex) { System.out.println("Problem Exploding Castlewars barricade"); }
		return false;
    }
    
    private static void RemoveBarricades()
    {
    	try{
	    	for(Npc npc : new ArrayList<Npc>(barricadesZammy))
	    	{
	    		if(npc != null)
	    		{
	    			GameObject barricade = ObjectHandler.getInstance().getObject(4421, npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ());
	    			if(barricade != null){
	    				ObjectHandler.getInstance().removeObject(4421, npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ(), 10);
	    			}
	    			GameObject empty = ObjectHandler.getInstance().getObject(Constants.EMPTY_OBJECT, npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ());
	    			if(empty != null){
	    				ObjectHandler.getInstance().removeObject(npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ(), 10);
	    			}
	    			npc.removeAllEffects();
	    			npc.setVisible(false);
	    			Following.resetFollow(npc);
	    			World.unregister(npc);
	    			barricadesZammy.remove(npc);
	    		}
	    	}
	    	for(Npc npc : new ArrayList<Npc>(barricadesSara))
	    	{
	    		if(npc != null)
	    		{
	    			GameObject barricade = ObjectHandler.getInstance().getObject(4421, npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ());
	    			if(barricade != null){
	    				ObjectHandler.getInstance().removeObject(4421, npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ(), 10);
	    			}
	    			GameObject empty = ObjectHandler.getInstance().getObject(Constants.EMPTY_OBJECT, npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ());
	    			if(empty != null){
	    				ObjectHandler.getInstance().removeObject(npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ(), 10);
	    			}
	    			npc.removeAllEffects();
	    			npc.setVisible(false);
	    			Following.resetFollow(npc);
	    			World.unregister(npc);
	    			barricadesSara.remove(npc);
	    		}
	    	}
	    	barricadesZammy.clear();
	    	barricadesSara.clear();
    	}catch(Exception ex) { System.out.println("Problem resetting Castlewars barricades"); }
    }
    
    public static boolean HandleItemOnBattlement(final Player player, int objectId, int x, int y, int z){
    	if(!isInGame(player))
		{
			return false;
		}
    	if(PlaceClimbingRope(player, objectId, x, y, z))
    	{
    		return true;
    	}
    	return false;
    }
    
    private final static int CLIMBING_ROPE_ITEM = 4047;
    private final static int CLIMBING_ROPE_OBJECT = 4444; //type 4 seems best
    
    private static ArrayList<GameObject> climbingRopes = new ArrayList<GameObject>();
    
    private static boolean canPlaceRope(final Player player){
    	for(GameObject rope : new ArrayList<GameObject>(climbingRopes))
    	{
    		if(rope == null)
    			continue;
    		GameObjectDef def = rope.getDef();
    		if(def.getPosition().getX() == player.getPosition().getX() && def.getPosition().getY() == player.getPosition().getY()
    				&& def.getPosition().getZ() == player.getPosition().getZ())
    		{
    			return false;
    		}
    	}
    	return true;
    }
    
    private static boolean PlaceClimbingRope(final Player player, int objectId, int x, int y, int z){
    	try{
			switch(player.getCastlewarsTeam())
			{
				case ZAMORAK :
					if(objectId == 4447)
					{
						player.getActionSender().sendMessage("You Cannot place a climbing rope on your own battlements!");
						return true;
					}
				break;
				case SARADOMIN :
					if(objectId == 4446)
					{
						player.getActionSender().sendMessage("You Cannot place a climbing rope on your own battlements!");
						return true;
					}
				break;
			}
	    	if(!canPlaceRope(player))
	    	{
	    		player.getActionSender().sendMessage("There is already a climbing rope here!");
	    		return true;
	    	}
			GameObject empty = ObjectHandler.getInstance().getObject(Constants.EMPTY_OBJECT, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
			if(empty != null){
				ObjectHandler.getInstance().removeObject(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), 10);
			}
			GameObjectDef objectHere = SkillHandler.getObject(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
			if(objectHere != null && objectHere.getType() != 22){
				player.getActionSender().sendMessage("You cannot place a climbing rope here!");
				return true;
			}
			final CacheObject bobj = ObjectLoader.object(x, y, z);
			if (bobj != null) {
				int face = bobj.getRotation();
				int ropeFace = face + 2;
				switch(objectId)
				{
					case 4446 :
						ropeFace = face + 2;
					break;
					case 4447 :
						ropeFace = face - 2;
					break;
				}
				if(!player.getInventory().removeItemSlot(new Item(CLIMBING_ROPE_ITEM,1), player.getSlot())){
					player.getInventory().removeItem(new Item(CLIMBING_ROPE_ITEM,1));
				}
				player.getActionSender().sendMessage("You manage to throw the climbing rope over the battlement wall.");
				climbingRopes.add(new GameObject(CLIMBING_ROPE_OBJECT, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), ropeFace, 4, Constants.EMPTY_OBJECT, 999999));
				return true;
			}
    	}catch(Exception ex) { System.out.println("Problem placing Castlewars rope"); }
    	return false;
    }
    
    private static void RemoveClimbingRopes()
    {
    	try{
	    	for(GameObject obj : new ArrayList<GameObject>(climbingRopes))
	    	{
	    		if(obj != null)
	    		{
	    			GameObjectDef def = obj.getDef();
					ObjectHandler.getInstance().removeObject(def.getId(), def.getPosition().getX(), def.getPosition().getY(), def.getPosition().getZ(), 4);
	    		}
	    	}
	    	climbingRopes.clear();
    	}catch(Exception ex) { System.out.println("Problem resetting Castlewars ropes"); }
    }
}
