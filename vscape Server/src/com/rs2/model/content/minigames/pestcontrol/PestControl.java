package com.rs2.model.content.minigames.pestcontrol;

import java.util.ArrayList;
import java.util.Random;

import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.CombatCycleEvent;
import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;

public class PestControl {

    private final static int LOBBY_TIME = 90;
    private final static int GAME_TIME = 600;
    private final static int PLAYERS_REQUIRED = 3;
    private final static int GRUNT_TIME = 30;
    private final static int NPC_LOGIC_TIME = 15;
    
    public static final Position LOBBY_EXIT = new Position(2657, 2639, 0);
    private static final MinigameAreas.Area LOBBY_AREA = new MinigameAreas.Area(new Position(2660, 2638, 0), new Position(2663, 2643, 0));
    private static final MinigameAreas.Area LANDING_AREA = new MinigameAreas.Area(new Position(2656, 2609, 0), new Position(2659, 2614, 0));
    private static final MinigameAreas.Area INSIDE_FORT = new MinigameAreas.Area(new Position(2645, 2587, 0), new Position(2667, 2603, 0));
	
    private static final int PORTAL_SIZE = 4;
    private static int[] PORTAL_IDS = {6146, 6147, 6148, 6149};
    private static int[] PORTAL_HEALTH = {250, 250, 250, 250};
    private static boolean[] PORTAL_SHIELD = {true, true, true, true};
    private static int[] KNIGHT_DATA = {3782, 2656, 2592};
    
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
	
		public static PortalData forShield(int shieldId) {
		    for (PortalData portalData : PortalData.values()) {
				if (shieldId == portalData.shieldId) {
				    return portalData;
				}
		    }
		    return null;
		}
	
		public static PortalData forNormal(int normalId) {
		    for (PortalData portalData : PortalData.values()) {
				if (normalId == portalData.normalId) {
				    return portalData;
				}
		    }
		    return null;
		}
		
		public int getNormalId(){
			return normalId;
		}
		
		public int getShieldId(){
			return shieldId;
		}
    }
    
    public enum GruntData {
		SPLATTER_22(3727, false),
		SPLATTER_33(3728, false),
		SPLATTER_44(3729, false),
		SPLATTER_54(3730, false),
		SPLATTER_65(3731, false),
		SHIFTER_38(3732, true),
		SHIFTER_57(3734, true),
		SHIFTER_76(3736, true),
		SHIFTER_90(3738, true),
		SHIFTER_104(3740, true),
		RAVAGER_36(3742, false),
		RAVAGER_53(3743, false),
		RAVAGER_71(3744, false),
		RAVAGER_89(3745, false),
		RAVAGER_106(3746, false),
		SPINNER_36(3747, false),
		SPINNER_55(3748, false),
		SPINNER_74(3749, false),
		SPINNER_88(3751, false),
		SPINNER_92(3750, false),
		TORCHER_33(3752, true),
		TORCHER_49(3754, true),
		TORCHER_66(3756, true),
		TORCHER_79(3758, true),
		TORCHER_91(3759, true),
		DEFILER_33(3762, true),
		DEFILER_50(3764, true),
		DEFILER_66(3766, true),
		DEFILER_80(3768, true),
		DEFILER_97(3770, true),
		BRAWLER_51(3772, false),
		BRAWLER_76(3773, false),
		BRAWLER_101(3774, false),
		BRAWLER_129(3775, false),
		BRAWLER_158(3776, false);
	
		private int npcId;
		private boolean attackKnight;
	
		private GruntData(int npcId, boolean attackKnight) {
		    this.npcId = npcId;
		    this.attackKnight = attackKnight;
		}

		public static GruntData forId(int npcId) {
		    for (GruntData gruntData : GruntData.values()) {
		    	if (npcId == gruntData.npcId) {
		    		return gruntData;
				}
		    }
		    return null;
		}
    }
    
    public static void lobbyInterface(Player player) {
		try {
		    int minutes, seconds;
		    if (gameActive) {
		    	minutes = (gameTime + lobbyTime) / 60;
				seconds = (gameTime + lobbyTime) % 60;
		    } else {
		    	minutes = lobbyTime / 60;
		    	seconds = lobbyTime % 60;
		    }
		    if (seconds > 9) {
		    	player.getActionSender().sendString("Next Departure: " + minutes + ":" + seconds, 24127);
		    } else {
		    	player.getActionSender().sendString("Next Departure: " + minutes + ":0" + seconds, 24127);
		    }
		    player.getActionSender().sendString("Players Ready: " + playersInLobby() + "", 24128);
		    player.getActionSender().sendString("Players Required: " + PLAYERS_REQUIRED + " minimum", 24129);
		    player.getActionSender().sendString("Commendation Points: " + player.getPcPoints(), 24130);
		} catch (Exception ex) { }
    }
    
    public static void gameInterface(Player player) {
		try {
		    int minutes, seconds;
		    minutes = gameTime / 60;
		    seconds = gameTime % 60;
		    String timeLeft;
	
		    player.getActionSender().sendString("" + knightHealth, 24138);
		    player.getActionSender().sendString("" + player.getPcDamage(), 24139);
		    for (int i = 0; i < PORTAL_SIZE; i++) {
				if (PORTAL_HEALTH[i] > 0) {
				    player.getActionSender().sendString("" + PORTAL_HEALTH[i], 24140 + i);
				} else {
				    player.getActionSender().sendString("Dead", 24140 + i);
				}
		    }
		    if (seconds > 9) {
		    	timeLeft = "Time Remaining: " + minutes + ":" + seconds;
		    } else {
		    	timeLeft = "Time Remaining: " + minutes + ":0" + seconds;
		    }
		    if (gameTime <= 20) {
		    	player.getActionSender().sendString("@red@" + timeLeft, 24144);
		    } else if (gameTime > 20 && gameTime < 100) {
		    	player.getActionSender().sendString("@or1@" + timeLeft, 24144);
		    } else {
		    	player.getActionSender().sendString("@gre@" + timeLeft, 24144);
		    }
		} catch (Exception e) { }
    }
    
	private static void ProcessLogic(){
		World.submit(new Tick(8) {
		    @Override 
		    public void execute() {
				try {
					if (playersInLobby() <= 0 && playersInGame() <= 0) {
						resetGame();
						resetLobby();
						this.stop();
						return;
					}
					if (!gameActive) {
						if (lobbyTime > 0) {
							lobbyTime -= 5;
						}
						if (lobbyTime <= 0) {
							if (playersInLobby() < PLAYERS_REQUIRED) {
								lobbyTime = LOBBY_TIME;
								this.stop();
								ProcessLogic();
								return;
							}else{
								this.stop();
								startGame();
								ProcessLogic();
								return;
							}
						}
					}else{
						if (gameTime > 0) {
							gameTime -= 5;
							if (!allPortalsUnShielded()) {
								shieldTime += 5;
								if (shieldTime >= 30 && portalsUnshielded() == 0) {
									removePortalShield();
								} else if (shieldTime >= 60 && portalsUnshielded() == 1) {
									removePortalShield();
								} else if (shieldTime >= 90 && portalsUnshielded() == 2) {
									removePortalShield();
								} else if (shieldTime >= 120 && portalsUnshielded() == 3) {
									removePortalShield();
								}
							}
							if (!allPortalsDead()) {
								gruntTime += 5;
								if (gruntTime >= GRUNT_TIME) {
									gruntTime = 0;
									spawnGrunts();
									if (playersInGame() >= 5) {
										spawnGrunts();
									} else if (playersInGame() >= 10) {
										spawnGrunts();
									} else if (picklesTime) {
										spawnGrunts();
									}
									handleNpcBehavior();
								} 
								npcLogicTime += 5;
								if(npcLogicTime >= NPC_LOGIC_TIME)
								{
									npcLogicTime = 0;
									handleNpcBehavior();
								}
								if (getKnightHealth() != knight.getCurrentHp()) {
									setKnightHealth(knight.getCurrentHp());
								}
								updatePortalHealth();
							}
							if (getKnightHealth() == 0 || knight.isDead()) {
								if (playersInLobby() <= 0) {
									this.stop();
								}
								endGame(false);
								return;
							}
							if (allPortalsDead()) {
								if (playersInLobby() <= 0) {
									this.stop();
								}
								endGame(true);
								return;
							}
						}
						if (gameTime <= 0){
							if(allPortalsDead())
							{
								if (playersInLobby() <= 0) {
									this.stop();
								}
								endGame(true);
								return;
							} else {
								if (playersInLobby() <= 0) {
									this.stop();
								}
								endGame(false);
								return;
							}
						}
					}
				} catch (Exception ex){
					System.out.println("Error processing Pest Control Logic");
					ex.printStackTrace();
				}
		    }
		});
	}

    public static void handleHit(final Entity attacker, final Entity victim, final int damage) {
		if (attacker.isPlayer() && victim.isNpc()) {
		    Player player = (Player) attacker;
		    Npc npc = (Npc) victim;
		    if(player == null || npc == null)
		    	return;
		    switch (npc.getNpcId()) {
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
		if (attacker.isNpc() && victim.isNpc()) {
		    Npc npc1 = (Npc) attacker;
		    Npc npc2 = (Npc) victim;
		    if(npc1 == null || npc2 == null)
		    	return;
		    if (shouldAttackKnight(npc1) || isSplatter(npc1)) {
		    	setKnightHealth(npc2.getCurrentHp());
		    }
		}
    }

    public static void handleDeath(final Player player) {
    	if(player == null)
    		return;
		if (player.inPestControlGameArea() && gameActive) {
		    player.teleport(MinigameAreas.randomPosition(LANDING_AREA));
		}
		if (player.inPestControlGameArea() && !gameActive) {
		    player.teleport(LOBBY_EXIT);
		}
    }
    
    private static void spawnMainNpcs() {
    	for (int i = 0; i < PORTAL_SIZE; i++) {
    	    PortalData data = PortalData.values()[i];
    	    setPortalHealth(i, 250);
    	    NpcLoader.spawnNpc(data.shieldId, data.x, data.y, 0, true, true, true);
    	}
    	if (knight == null) {
    		spawnKnight();
    	} else if (knight != null) {
    	    knight.setDead(true);
    	    knight.setVisible(false);
    	    World.unregister(knight);
    	    spawnKnight();
    	}
    }
    
    private static void spawnKnight(){
		NpcLoader.spawnNpc(KNIGHT_DATA[0], KNIGHT_DATA[1], KNIGHT_DATA[2], 0, true, true);
		setKnightHealth(200);
	    for (Npc npc : World.getNpcs()) {
			if (npc == null) {
			    continue;
			}
			if (npc.getNpcId() == KNIGHT_DATA[0]) {
			    if (npc.inPestControlGameArea()) {
			    	knight = npc;
			    	break;
			    }
			}
	    }
    }
    
	private static void spawnGrunts() {
		for (int i = 0; i < PORTAL_SIZE; i++) {
			if (!isPortalDead(i) && isPortalUnshielded(i)) {
				PortalData portalData = PortalData.values()[i];
				GruntData gruntData = GruntData.values()[Misc.randomMinusOne(GruntData.values().length)];
				Npc grunt = new Npc(!picklesTime ? gruntData.npcId : 1828);
				//gameGrunts.add(grunt);
				if(gruntData.attackKnight && !picklesTime) {
				    grunt.setPosition(new Position(portalData.x + Misc.randomMinusOne(2), portalData.y + Misc.randomMinusOne(2), 0));
				    grunt.setSpawnPosition(knight.getPosition());
				    World.register(grunt);
				    grunt.walkTo(knight.getPosition(), true);
				} else {
				    grunt.setPosition(new Position(portalData.x + Misc.randomMinusOne(3), portalData.y + Misc.randomMinusOne(3), 0));
				    grunt.setSpawnPosition(new Position(portalData.x + Misc.randomMinusOne(3), portalData.y + Misc.randomMinusOne(3), 0));
				    grunt.setMinWalk(new Position(portalData.x - 5, portalData.y - 5));
				    grunt.setMaxWalk(new Position(portalData.x + 5, portalData.y + 5));
				    grunt.setWalkType(Npc.WalkType.WALK);
				    World.register(grunt);
				}
			}
		}
	}
	
	public static void handleNpcBehavior() {
		for (Npc npc : World.getNpcs()) {
		    if (npc == null) {
		    	continue;
		    }
		    if (!npc.inPestControlGameArea() || npc.isDead()) {
		    	continue;
		    }
		    npc.setSpawnPosition(npc.getPosition());
		    if (isShifter(npc) && knight != null && !Misc.goodDistance(npc.getPosition(), knight.getPosition(), 3)) {
		    	teleportShifter(npc);
		    	continue;
		    }
		    if (isSpinner(npc) && !npc.isAttacking()) {
		    	healPortal(npc);
		    	continue;
		    }
		    if (shouldAttackKnight(npc)) {
		    	attackKnight(npc);
		    }
		}
	}
	
    public static void teleportShifter(Npc npc) {
    	if(npc == null)
    		return;
		int hp = npc.getCurrentHp();
		if(hp == 0 || !npc.isVisible()) {
		    return;
		}
		if(knight == null)
			return;
		npc.setVisible(false);
		npc.setDead(true);
		World.unregister(npc);
		Npc newNpc = new Npc(npc.getNpcId());
		newNpc.setPosition(MinigameAreas.randomPosition(INSIDE_FORT));
		newNpc.setSpawnPosition(knight.getPosition());
		World.register(newNpc);
		newNpc.walkTo(knight.getPosition(), gameActive);
		newNpc.setCurrentHp(hp);
		attackKnight(newNpc);
    }
    
	public static void healPortal(Npc grunt) {
		System.out.println("attempting heal");
		if(grunt == null)
			return;
		if(grunt.isDead())
			return;
		System.out.println("attempting heal 2");
		try {
			for (Npc npc : World.getNpcs()) {
				if(npc == null)
					continue;
				if(!npc.inPestControlGameArea())
					continue;
			    if (isPortal(npc) && !npc.isDead()) {
					PortalData portaldata = PortalData.forNormal(npc.getNpcId());
					if(portaldata != null){
					    if (npc.getNpcId() == portaldata.getNormalId() && Misc.goodDistance(grunt.getPosition(), npc.getPosition(), 4)) {
					    	if(npc.getCurrentHp() >= npc.getMaxHp())
					    		continue;
					    	npc.getUpdateFlags().sendHighGraphic(606);
					    	npc.heal(50);
					    	switch (portaldata)
					    	{
								case WEST:
									setPortalHealth(0, npc.getCurrentHp());
									break;
								case EAST:
									setPortalHealth(1, npc.getCurrentHp());
									break;
								case SOUTHEAST:
									setPortalHealth(2, npc.getCurrentHp());
									break;
								case SOUTHWEST:
									setPortalHealth(3, npc.getCurrentHp());
									break;
					    	}
					    	grunt.getUpdateFlags().faceEntity(npc.getUpdateFlags().getEntityFaceIndex());
					    	grunt.getUpdateFlags().sendAnimation(3911);
					    }
					}
			    }
			}
		} catch (Exception ex){
			System.out.println("Error processing Pest Control portal heal");
			ex.printStackTrace();
		}
	}
	
    public static void attackKnight(Npc grunt) {
    	if(knight == null)
    		return;
    	if(knight.isDead() || getKnightHealth() <= 0)
    		return;
		if(Misc.goodDistance(grunt.getPosition(), knight.getPosition(), 15)) {
		    CombatCycleEvent.startCombat(grunt, knight);
		} else {
		    grunt.walkTo(knight.getPosition(), true);
		}
		grunt.getUpdateFlags().sendFaceToDirection(knight.getPosition());
		grunt.getUpdateFlags().setFace(knight.getPosition());
		grunt.getUpdateFlags().setFaceToDirection(true);
    }
    
    public static boolean shouldAttackKnight(Npc npc) {
    	if(npc == null)
    		return false;
    	GruntData gruntData = GruntData.forId(npc.getNpcId());
    	if(gruntData != null)
    	{
    	    if (npc.getNpcId() == gruntData.npcId && gruntData.attackKnight && npc.inPestControlGameArea()) {
    	    	return true;
    	    }
    	}
    	return false;
    }
    
    public static boolean isRavager(Npc npc) {
    	return npc.getNpcId() >= 3742 && npc.getNpcId() <= 3746;
    }
    
    public static boolean isSplatter(Npc npc) {
    	return npc.getNpcId() >= 3727 && npc.getNpcId() <= 3731;
    }

    public static boolean isPortal(Npc npc) {
    	return npc.getNpcId() >= 6142 && npc.getNpcId() <= 6149;
    }

    public static boolean isShieldedPortal(Npc npc) {
    	return npc.getNpcId() >= 6146 && npc.getNpcId() <= 6149;
    }

    public static boolean isSpinner(Npc npc) {
    	return npc.getNpcId() >= 3747 && npc.getNpcId() <= 3751;
    }
    
    public static boolean isShifter(Npc npc) {
    	return npc.getNpcId() >= 3732 && npc.getNpcId() <= 3740;
    }
	
    private static void destroyAllNpcs() {
		for (Npc npc : World.getNpcs()) {
		    if (npc == null) {
		    	continue;
		    }
		    if (npc.inPestControlGameArea()) {
		    	if(npc.getNpcId() == 1828 || npc == knight || isPortal(npc) || isShieldedPortal(npc) || GruntData.forId(npc.getNpcId()) != null){
		    		NpcLoader.destroyNpc(npc);
		    	}
		    }
		}
    }

    private static void setKnightHealth(int amount) {
		if (amount <= 0) {
		    amount = 0;
		}
		if (amount >= 200) {
		    amount = 200;
		}
		knightHealth = amount;
    }

    private static int getKnightHealth() {
    	return knightHealth;
    }
    
    private static void removePortalShield() {
		shieldTime = 0;
		int unShielded = Misc.randomMinusOne(PORTAL_SIZE);
		if (isPortalUnshielded(unShielded)) {
		    removePortalShield();
		    return;
		}
		for (Npc npc : World.getNpcs()) {
		    if (npc == null) {
		    	continue;
		    }
		    PortalData portalData = PortalData.forShield(PORTAL_IDS[unShielded]);
		    if(portalData == null)
		    	continue;
		    if (npc.inPestControlGameArea() && portalData != null && npc.getNpcId() == PORTAL_IDS[unShielded]) {
		    	npc.sendTransform(portalData.normalId, 999999);
		    	PORTAL_SHIELD[unShielded] = false;
		    	sendGameMessage("@dbl@The Void Knight has disabled the " + portalData.name + " Shield!");
		    }
		}
    }
    
    private static void resetShields() {
		for (int i = 0; i < PORTAL_SIZE; i++) {
		    PORTAL_SHIELD[i] = true;
		}
    }
    
	private static boolean isPortalUnshielded(int index)
	{
		return !PORTAL_SHIELD[index];
	}
	
    public static int portalsUnshielded() {
		int x = 0;
		for (int i = 0; i < PORTAL_SIZE; i++) {
		    if (isPortalUnshielded(i)) {
		    	x++;
		    }
		}
		return x;
    }

    private static boolean allPortalsUnShielded() {
		int count = 0;
		for (int i = 0; i < PORTAL_SIZE; i++) {
		    if (isPortalUnshielded(i)) {
		    	count++;
		    }
		}
		return count >= PORTAL_SIZE;
    }
    
    private static void setPortalHealth(int index, int amount) {
		if (amount <= 0) {
		    amount = 0;
		}
		if (amount >= 250) {
		    amount = 250;
		}
		PORTAL_HEALTH[index] = amount;
    }
    
    private static int getPortalHealth(int index) {
    	return PORTAL_HEALTH[index];
    }
    
    private static boolean isPortalDead(int index) {
    	return getPortalHealth(index) <= 0;
    }
    
    public static boolean allPortalsDead() {
		int count = 0;
		for (int i = 0; i < PORTAL_SIZE; i++) {
		    if (isPortalDead(i)) {
		    	count++;
		    }
		}
		return count >= PORTAL_SIZE;
    }
    
    private static void updatePortalHealth(){
		for (Npc npc : World.getNpcs()) {
			if(npc == null)
				continue;
			if(!npc.inPestControlGameArea())
				continue;
		    if (isPortal(npc) && !npc.isDead()) {
				PortalData portaldata = PortalData.forNormal(npc.getNpcId());
				if(portaldata != null){
				    if (npc.getNpcId() == portaldata.getNormalId()) {
				    	if(npc.getCurrentHp() >= npc.getMaxHp())
				    		continue;
				    	switch (portaldata)
				    	{
							case WEST:
								if (!isPortalDead(0))
									setPortalHealth(0, npc.getCurrentHp());
								break;
							case EAST:
								if (!isPortalDead(1))
									setPortalHealth(1, npc.getCurrentHp());
								break;
							case SOUTHEAST:
								if (!isPortalDead(2))
									setPortalHealth(2, npc.getCurrentHp());
								break;
							case SOUTHWEST:
								if (!isPortalDead(3))
									setPortalHealth(3, npc.getCurrentHp());
								break;
				    	}
				    }
				}
		    }
		}
    }
	
    public static void sendGameMessage(String msg) {
		for (Player player : new ArrayList<Player>(gamePlayers)) {
		    if (player != null) {
		    	player.getActionSender().sendMessage(msg);
		    }
		}
    }
	
	private static void startGame() {
		try {
			spawnMainNpcs();
		    shieldTime = 0;
		    gruntTime = 0;
		    npcLogicTime = 0;
		    gameTime = GAME_TIME;
		    lobbyTime = LOBBY_TIME;
		    gameActive = true;
		    picklesTime = (2 >= new Random().nextDouble() * 100);
		    for (Player player : new ArrayList<Player>(lobbyPlayers)) {
				if(player == null) {
				    continue; 
				}
				if (player.inPestControlLobbyArea() && isInLobby(player)) {
				    lobbyPlayers.remove(player);
				}
				player.teleport(MinigameAreas.randomPosition(LANDING_AREA));
				gamePlayers.add(player);
				if (!picklesTime) {
				    player.getActionSender().sendMessage("@blu@The Pest Control Game has begun!");
				} else {
				    player.getActionSender().sendMessage("@blu@The Pest Control Game has begun?");
				}
		    }
		} catch (Exception ex) {
			System.out.println("Error Starting Pest Control Game");
		    ex.printStackTrace();
		}
	}
	
	private static void endGame(boolean gameWon) {
		for (Player player : new ArrayList<Player>(gamePlayers)) {
			if(player == null)
				continue;
			if (player.inPestControlGameArea()) {
				if (gameWon) {
					player.getActionSender().sendMessage("@blu@Game won!");
					if (player.getPcDamage() >= 50) {
						player.addPcPoints(5, player);
					}
					else if (player.getPcDamage() >= 150) {
					    player.addPcPoints((int) Math.floor((player.getPcDamage() - 50) / 100d), player);
					}
				    int reward = player.getCombatLevel() * 10;
				    player.getInventory().addItem(new Item(995, reward));
				}else{
					 player.getActionSender().sendMessage("@red@Game lost.");
				}
				leaveGame(player, false);
			}
		}
		resetGame();
	}
	
    public static void leaveGame(Player player, boolean DC) {
    	if(player == null)
    		return;
		if (isInGame(player) && !player.isDead()) {
			gamePlayers.remove(player);
			if(!DC) {
			    player.teleport(LOBBY_EXIT);
				player.setPcDamage(0);
			    player.getInventory().removeItem(new Item(1511, player.getInventory().getItemAmount(1511)));
				player.resetEffects();
				player.removeAllEffects();
				player.heal(100);
				player.getPrayer().resetAll();
				player.getSkill().refresh();
				player.getPestControlBarricades().clear();
			}
		}
    }
	
    private static void resetGame() {
		gameActive = false;
		shieldTime = 0;
		gruntTime = 0;
		npcLogicTime = 0;
		gameTime = 0;
		lobbyTime = LOBBY_TIME;
		gamePlayers.clear();
		//gameGrunts.clear();
		//ravagers.clear();
		destroyAllNpcs();
		resetShields();
		setKnightHealth(200);
	//	resetBarricades();
    }
    
    private static void resetLobby() {
		lobbyTime = LOBBY_TIME;
		lobbyPlayers.clear();
    }
    
    private static void joinLobby(Player player) {
		if (player != null) {
		    if (!isInLobby(player)) {
		    	if (playersInLobby() <= 0 && playersInGame() <= 0) {
		    		ProcessLogic();
		    	}
		    	lobbyPlayers.add(player);
		    	player.getActionSender().sendMessage("You have joined the Pest Control lobby.");
		    	player.teleport(MinigameAreas.randomPosition(LOBBY_AREA));
		    } else {
		    	player.getActionSender().sendMessage("@dre@You're already in the Pest Control lobby.");
		    }
		}
    }
    
    public static void leaveLobby(Player player, boolean DC) {
		if (isInLobby(player)) {
			lobbyPlayers.remove(player);
			if(!DC){
				player.teleport(LOBBY_EXIT);
			}
		}
    }
    
	public static boolean handleObjectClicking(Player player, int objectId, int x, int y) {
		switch (objectId) {
		    case 14315: //gangplank
				if (x == 2658 && y == 2639) {
				    if(player.getInventory().playerHasItem(1511)) {
				    	player.getActionSender().sendMessage("You cannot take your own logs into the game area.");
				    	return true;
				    }
				    if (!player.inPestControlLobbyArea()) {
				    	joinLobby(player);
				    }
				}
			return true;
		    case 14314: //ladder
				if (x == 2660 && y == 2639) {
				    if (player.inPestControlLobbyArea()) {
				    	leaveLobby(player, false);
				    }
				}
			return true;
		}
		return false;
	}
    
	private static int playersInLobby() {
		if (lobbyPlayers != null) {
		    return lobbyPlayers.size();
		}
		return 0;
	}
	
    public static int playersInGame() {
		if (gamePlayers != null) {
		    return gamePlayers.size();
		}
		return 0;
    }
	
    private static boolean isInLobby(Player player) {
    	if(player != null && lobbyPlayers != null)
    	{
    		return lobbyPlayers.contains(player);
    	}
    	return false;
    }

    private static boolean isInGame(Player player) {
    	if(player != null && gamePlayers != null)
    	{
    		return gamePlayers.contains(player);
    	}
    	return false;
    }
    
    public static boolean gameActive() {
    	return gameActive;
    }
    
 /*   private static ArrayList<Position> getBrokenBarricades() {
    	return brokenBarricades;
    }*/
    
    private static ArrayList<Player> lobbyPlayers = new ArrayList<Player>();
    private static ArrayList<Player> gamePlayers = new ArrayList<Player>();
    //private static ArrayList<Npc> gameGrunts = new ArrayList<Npc>();
    //private static ArrayList<Npc> ravagers = new ArrayList<Npc>();
    //private static ArrayList<Position> brokenBarricades = new ArrayList<Position>();
    
    private static int lobbyTime = LOBBY_TIME;
    private static int gameTime = 0;
    private static int shieldTime = 0;
    private static int gruntTime = 0;
    private static int npcLogicTime = 0;
    private static int knightHealth = 200;
    private static boolean picklesTime;
    private static boolean gameActive = false;
    private static Npc knight;
}
