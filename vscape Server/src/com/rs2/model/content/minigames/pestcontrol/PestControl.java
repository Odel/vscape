package com.rs2.model.content.minigames.pestcontrol;

import com.rs2.Constants;
import java.util.ArrayList;

import com.rs2.model.Entity;
import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.CombatCycleEvent;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.task.TaskScheduler;
import com.rs2.task.Task;
import com.rs2.util.Misc;

public class PestControl {

	private final static int LOBBY_TIME = 90;
	private final static int GAME_TIME = 600;
	private final static int PLAYERS_REQUIRED = 3;
	
	private static ArrayList<Player> lobbyPlayers = new ArrayList<Player>();
	private static ArrayList<Player> gamePlayers = new ArrayList<Player>();
	
	private static int lobbyTime = LOBBY_TIME;
	private static int gameTime = 0;
	
	private static boolean gameActive = false;
	
	private static final Position LOBBY_EXIT = new Position(2657,2639,0);
	private static final MinigameAreas.Area LOBBY_AREA = new MinigameAreas.Area(new Position(2660, 2638, 0), new Position(2663, 2643, 0));
	private static final MinigameAreas.Area LANDING_AREA = new MinigameAreas.Area(new Position(2656, 2609, 0), new Position(2659, 2614, 0));
	private static final MinigameAreas.Area INSIDE_FORT = new MinigameAreas.Area(new Position(2645, 2587, 0), new Position(2667, 2603, 0));
	
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
        private static int[] PORTAL_IDS = {6146, 6147, 6148, 6149};
	private static boolean[] PORTAL_SHIELD = {true, true, true, true};
	private static int shieldTime = 0;
	
	public enum GruntData {
                SPLATTER_22(3727,false),
                SPLATTER_33(3728,false),
                SPLATTER_44(3729,false),
                SPLATTER_54(3730,false),
                SPLATTER_65(3731,false),
                SHIFTER_38(3732,true),
                SHIFTER_57(3734,true),
                SHIFTER_76(3736,true),
                SHIFTER_90(3738,true),
                SHIFTER_104(3740,true),
                RAVAGER_36(3742,true),
                RAVAGER_53(3743,true),
                RAVAGER_71(3744,true),
                RAVAGER_89(3745,true),
                RAVAGER_106(3746,true),
                SPINNER_36(3747,false),
                SPINNER_55(3748,false),
                SPINNER_74(3749,false),
                SPINNER_88(3751,false),
                SPINNER_92(3750,false),
                TORCHER_33(3752,true),
                TORCHER_49(3754,true),
                TORCHER_66(3756,true),
                TORCHER_79(3758,true),
                TORCHER_91(3759,true),
                DEFILER_33(3762,true),
                DEFILER_50(3764,true),
                DEFILER_66(3766,true),
                DEFILER_80(3768,true),
                DEFILER_97(3770,true),
                BRAWLER_51(3772,false),
                BRAWLER_76(3773,false),
                BRAWLER_101(3774,false),
                BRAWLER_129(3775,false),
                BRAWLER_158(3776,false);
                
		
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
			player.getActionSender().sendString("Commendation Points: " + player.getPcPoints(), 18792);
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
							if(shieldTime >= 30 && portalsUnshielded() == 0)
							{
								removePortalShield();
							}
							else if(shieldTime >= 60 && portalsUnshielded() == 1)
							{
								removePortalShield();
							}
							else if(shieldTime >= 90 && portalsUnshielded() == 2)
							{
								removePortalShield();
							}
							else if(shieldTime >= 120 && portalsUnshielded() == 3)
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
								if(playersInGame() >= 5)
								    spawnGrunts();
								else if(playersInGame() >= 10)
								    spawnGrunts();
								handleNpcBehavior();
							}
							else if(getKnightHealth() != knight.getCurrentHp())
							    setKnightHealth(knight.getCurrentHp());
						}
						if (allPortalsDead())
						{
							if(playersInLobby() <= 0){
								this.stop();
							}
							endGame(true);
							return;
						}
						if (gameTime <= 0 && allPortalsDead()) {
							if(playersInLobby() <= 0){
								this.stop();
							}
							endGame(true);
							return;
						}
						if(getKnightHealth() == 0 || knight.isDead()) {
							if(playersInLobby() <= 0){
								this.stop();
							}
							endGame(false);
							return;
						}
						if (gameTime <= 0 && !allPortalsDead()) {
							if(playersInLobby() <= 0){
								this.stop();
							}
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
						if(gameWon)
						{
							player.getActionSender().sendMessage("@blu@Game won!");
							if(player.getPcDamage() >= 50)
							    player.addPcPoints(5, player);
							player.resetEffects();
							player.removeAllEffects();
							player.heal(100);
							player.getPrayer().resetAll();
							player.getSkill().refresh();
							leaveGame(player);
							int reward = player.getCombatLevel() * 10;
							player.getInventory().addItem(new Item(995, reward));
						}
						else
						{
							player.getActionSender().sendMessage("@red@Game lost.");
							player.resetEffects();
							player.removeAllEffects();
							player.heal(100);
							player.getPrayer().resetAll();
							player.getSkill().refresh();
							leaveGame(player);
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
		setKnightHealth(200);
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
			NpcLoader.spawnNpc(data.shieldId, data.x, data.y, 0,true,true,true);
		}
		if(knight == null) {
		    NpcLoader.spawnNpc(KNIGHT_DATA[0][0], KNIGHT_DATA[0][1], KNIGHT_DATA[0][2], 0,true,true);
		    setKnightHealth(200);
		    for(Npc npc : World.getNpcs())
		    {
			if(npc == null)
				continue;
			if(npc.getDefinition().getName().toLowerCase().contains("knight"))
			{
				if(npc.inPestControlGameArea() )
				{
					knight = npc;
				}
			}
		    }
		}
		else if( knight != null)
		{
		    knight.setDead(true);
		    knight.setVisible(false);
		    World.unregister(knight);
		    NpcLoader.spawnNpc(KNIGHT_DATA[0][0], KNIGHT_DATA[0][1], KNIGHT_DATA[0][2], 0,true,true);
		    setKnightHealth(200);
		    for(Npc npc : World.getNpcs())
		    {
			if(npc == null)
				continue;
			if(npc.getDefinition().getName().toLowerCase().contains("knight"))
			{
				if(npc.inPestControlGameArea() )
				{
					knight = npc;
				}
			}
		    }
		}
	}
	
	private static void spawnGrunts() {
	    for(int i = 0; i < PortalData.values().length; i++) {
		if(!isPortalDead(i) && !PORTAL_SHIELD[i]){
		    gruntTime = 0;
		    PortalData portalData = PortalData.values()[i];
		    GruntData gruntData = GruntData.values()[Misc.randomMinusOne(GruntData.values().length)];
		    Npc grunt = new Npc(gruntData.npcId);
			if( gruntData.attackKnight ) {
			    grunt.setPosition(new Position(portalData.x + Misc.randomMinusOne(2), portalData.y+ Misc.randomMinusOne(2), 0) );
			    grunt.setSpawnPosition(knight.getPosition() );
			    World.register(grunt);
			    grunt.walkTo(knight.getPosition(), gameActive);
			    
			}
			else {
			    grunt.setPosition(new Position(portalData.x + Misc.randomMinusOne(3), portalData.y+ Misc.randomMinusOne(3), 0) );
			    grunt.setSpawnPosition(new Position(portalData.x + Misc.randomMinusOne(3), portalData.y+ Misc.randomMinusOne(3), 0) );
			    grunt.setMinWalk(new Position(portalData.x - Constants.NPC_WALK_DISTANCE, portalData.y - Constants.NPC_WALK_DISTANCE));
			    grunt.setMaxWalk(new Position(portalData.x + Constants.NPC_WALK_DISTANCE, portalData.y + Constants.NPC_WALK_DISTANCE));
			    grunt.setWalkType(Npc.WalkType.WALK);
			    World.register(grunt);
			    grunt.setDontAttack(false);
			}   
		}
	    }
	}
	
	private static void removePortalShield() {
		shieldTime = 0;
		//int portalToUnshield = PORTAL_IDS[Misc.randomMinusOne(PORTAL_IDS.length)];
                int unShielded = Misc.randomMinusOne(PORTAL_SHIELD.length);
		if(!PORTAL_SHIELD[unShielded])
		{
			removePortalShield();
		}
                	for(Npc npc : World.getNpcs()) {        
                            if(npc == null)
                                continue;
                            PortalData portalData = PortalData.forShield(PORTAL_IDS[unShielded]);
                            if(npc.inPestControlGameArea() && portalData != null && npc.getNpcId() == PORTAL_IDS[unShielded]) {
                                npc.sendTransform(portalData.normalId, 999999);
                                PORTAL_SHIELD[unShielded] = false;
                                sendGameMessage("@dbl@The Void Knight has disabled the "+ portalData.name +" Shield!");
                            }
                            else 
                                continue;
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
		if(amount >= 200)
			amount = 200;
		KNIGHT_HEALTH = amount;
	}
	
	private static int getKnightHealth() {
		return KNIGHT_HEALTH;
	}
	
	private static void setPortalHealth(int index, int amount) {
		if(amount <= 0)
		    amount = 0;
		if(amount >= 250)
		    amount = 250;
		PORTAL_HEALTH[index] = amount;
	}
	
	private static int getPortalHealth(int index) {
		return PORTAL_HEALTH[index];
	}
	
	private static boolean isPortalDead(int index) {
		return getPortalHealth(index) <= 0;
	}
	
        public static void attackKnight(Npc grunt) {
            CombatCycleEvent.startCombat(grunt, knight);
        }
	public static void healPortal(Npc grunt) {
	    for (Npc npc : World.getNpcs()) {
		if(npc != null && isPortal(npc) && !npc.isDead()) {
		    PortalData portaldata = PortalData.forNormal(npc.getNpcId());
		    for(int i = 0; i < PortalData.values().length; i++) {
			if(npc.getNpcId() == portaldata.values()[i].normalId && Misc.goodDistance(grunt.getPosition(), npc.getPosition(), 2) && !grunt.isDead() ) {
			    npc.getUpdateFlags().sendHighGraphic(606);
			    setPortalHealth(i, npc.getCurrentHp() + 50);
			    npc.heal(50);
			    grunt.getUpdateFlags().faceEntity(npc.getUpdateFlags().getEntityFaceIndex());
			    grunt.getUpdateFlags().sendAnimation(3911);
			}

		    }
		}
		    
	    }
        }
	
	public static int portalsUnshielded() {
	    int x = 0;
	    for(int i = 0; i < PORTAL_SHIELD.length; i++) {
		if(!PORTAL_SHIELD[i])
		    x++;
	    }
	    return x;
	}
	
        public static void handleNpcBehavior() {
            for (Npc npc : World.getNpcs()) {
                if(npc == null)
                    continue;
                if( shouldAttackKnight(npc) ) {
                    attackKnight(npc);
		    if ( npc.getDefinition().getName().toLowerCase().contains("shifter")) {
			teleportShifter(npc);
			continue;
		    }
		    else if (npc.getDefinition().getName().toLowerCase().contains("ravager"))
			//new GameObject(Constants.EMPTY_OBJECT, npc.getPosition().getX(), npc.getPosition().getY(), 0, 0, 10, 3, 35); maybe
			continue;
		}
		else if( isSpinner(npc) )
		    healPortal(npc);
                else
                    continue;
            }
        }
	
        public static boolean shouldAttackKnight(Npc npc) {
            for (GruntData gruntData : GruntData.values()) {
                if (npc.getNpcId() == gruntData.npcId && gruntData.attackKnight && npc.inPestControlGameArea())
                    return true;
            }
            return false;
        }
	
	public static boolean isSplatter(Npc npc) {
            switch(npc.getNpcId()) {
		case 3727:
		case 3728:
		case 3729:
		case 3730:
		case 3731:
		    return true;
	    }
	    return false;
        }
	
	public static boolean isPortal(Npc npc) {
	    switch(npc.getNpcId()) {
		case 6142:
		case 6143:
		case 6144:
		case 6145:
		case 6146:
		case 6147:
		case 6148:
		case 6149:
		    return true;
	    }
	    return false; 
	}
	
	public static boolean isSpinner(Npc npc) {
            switch(npc.getNpcId()) {
		case 3747:
		case 3748:
		case 3749:
		case 3750:
		case 3751:
		    return true;
	    }
	    return false;
        }
	
	public static void teleportShifter(Npc npc) {
	    int hp = npc.getCurrentHp();
	    npc.setVisible(false);
	    npc.setDead(true);
	    World.unregister(npc);
	    Npc newNpc = new Npc(npc.getNpcId());
	    newNpc.setPosition(MinigameAreas.randomPosition(INSIDE_FORT));
	    newNpc.setSpawnPosition(knight.getPosition());
	    World.register(newNpc);
	    if(hp == 0) {
		newNpc.setDead(true);
		newNpc.setVisible(false);
		World.unregister(newNpc);
	    }
	    newNpc.setCurrentHp(hp);
	    newNpc.walkTo(knight.getPosition(), gameActive);
	    attackKnight(newNpc);
	}
	
	public static boolean allPortalsDead() {
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
			//if (npc.getNpcId() >= 3777 && npc.getNpcId() <= 3780 || npc.getNpcId() >= 6142 && npc.getNpcId() <= 6149) {
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
			//}
		}
		if (attacker.isNpc() && victim.isNpc()){
			Npc npc1 = (Npc) attacker;
			Npc npc2 = (Npc) victim;
			
			if ( shouldAttackKnight(npc1) || isSplatter(npc1) ) {
			    setKnightHealth(npc2.getCurrentHp());
			}
		}
	}
	
	public static void handleDeath(final Player player)
	{
	    if( player.inPestControlGameArea() && gameActive)
		player.teleport(MinigameAreas.randomPosition(LANDING_AREA));
	    if( player.inPestControlGameArea() && !gameActive)
		player.teleport(LOBBY_EXIT);
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
	
	public static void sendGameMessage(String msg)
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
		if (isInGame(player) && !player.isDead() ) {
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
	
	public static double handlePoints(Player player, int amount) {
	    double finalRewardAmount = 0;
	    int l = 0;
	    double N = 0;
	    int skillIndex = player.getSkillAnswer();
	    if(skillIndex == 5) N = 24; //Prayer
	    else if(skillIndex == 4 || skillIndex == 6) N = 35.8888888; //Range & Mage
	    else if(skillIndex == 0 || skillIndex == 1 || skillIndex == 2 || skillIndex == 3)
		N = 38.8888888; //Others
	    
	    l = player.getSkill().getPlayerLevel(skillIndex);
	    finalRewardAmount = (int)( (Math.ceil( ((l+25)*(l-24))/(606d) )) * N );
	    
	    if(amount >= 100) return finalRewardAmount * amount * 1.1;
	    else return finalRewardAmount * amount * 1.1;
	}
	
	public static void handleReward(Player player, double expReward) {
	    int skillIndex = player.getSkillAnswer();
	    player.getSkill().addExp(skillIndex, expReward);
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
