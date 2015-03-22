package com.rs2.model;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.rs2.Constants;
import com.rs2.Server;
import com.rs2.cache.region.RegionManager;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcDefinition;
import com.rs2.model.npcs.NpcUpdating;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.PlayerUpdating;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.model.tick.Tick;
import com.rs2.model.tick.TickManager;
import com.rs2.util.Benchmark;
import com.rs2.util.Benchmarks;
import com.rs2.util.Misc;
import com.rs2.util.NameUtil;
import com.rs2.util.PlayerSave;

/**
 * Handles all logged in players.
 * 
 * @author blakeman8192
 */
public class World {

	/** All registered players. */
	private final static Player[] players = new Player[Constants.MAX_PLAYERS_AMOUNT];

	/** All registered NPCs. */
	private static Npc[] npcs = new Npc[Constants.MAX_NPCS];
	
	public static HashMap<Long, GameObject> cannons = new HashMap<Long, GameObject>();

	private static TickManager tickManager = new TickManager();

	private static NpcDefinition[] definitions = new NpcDefinition[Constants.MAX_NPC_ID];

	public static int SERVER_TICKS = 0;
	private static long saveTimer;

	/**
	 * Performs the processing of all players.
	 * 
	 * @throws Exception
	 */
	public static synchronized void process() throws Exception {
		World.SERVER_TICKS++;
        Benchmark b = Benchmarks.getBenchmark("executeTicks");
		List<Tick> ticksToExecute = new LinkedList<Tick>();
		ticksToExecute.addAll(tickManager.getTickables());
		Iterator<Tick> tickIt$ = ticksToExecute.iterator();
		tickManager.getTickables().clear();
        b.start();
		while (tickIt$.hasNext()) {
			Tick t = tickIt$.next();
			try {
				t.cycle();
				if (t.isRunning()) {
					tickManager.getTickables().add(t);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
        b.stop();

		try {
            b = Benchmarks.getBenchmark("groundItemsUpdate");
            b.start();
            GroundItemManager.getManager().run();
            b.stop();
            b = Benchmarks.getBenchmark("groundObjectsUpdate");
            b.start();
            ObjectHandler.getInstance().tick();
            b.stop();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// Perform any logic processing for players.
		b = Benchmarks.getBenchmark("processPlayerLogic");
		b.start();
		for (Player player : players) {
			if (player == null) {
				continue;
			}
			try {
				player.process();
			} catch (Exception ex) {
				ex.printStackTrace();
				player.disconnect();
			}
		}
		b.stop();

		// Perform any logic processing for NPCs.
		b = Benchmarks.getBenchmark("processNPCLogic");
		b.start();
		for (Npc npc : npcs) {
			if (npc == null) {
				continue;
			}
			try {
				npc.process();
			} catch (Exception ex) {
				ex.printStackTrace();
				unregister(npc);
			}
		}
		b.stop();

		CycleEventHandler.getInstance().tick();

		// Perform movement process
		b = Benchmarks.getBenchmark("processMovement");
		b.start();
		for (Player player : players) {
			if (player == null) {
				continue;
			}
			player.getMovementHandler().process();
		}
		for (Npc npc : npcs) {
			if (npc == null) {
				continue;
			}
			npc.getMovementHandler().process();
		}
		b.stop();

		// Update all players.
		b = Benchmarks.getBenchmark("updatePlayers");
		b.start();
		for (final Player player : players) {
			if (player == null) {
				continue;
			}
			try {
				PlayerUpdating.update(player);
				NpcUpdating.update(player);
			} catch (Exception ex) {
				ex.printStackTrace();
				player.disconnect();
			}
		}
        /*final CountDownLatch latch = new CountDownLatch(playerAmount());
        synchronized (getPlayers()) {
            for (final Player player : getPlayers()) {
                if (player == null) {
                    continue;
                }
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            PlayerUpdating.update(player);
                            NpcUpdating.update(player);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            player.disconnect();
                        } finally {
                            latch.countDown();
                        }
                    }
                });
            }
            try {
                latch.await();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }  */
		b.stop();

		// Reset all players after cycle.
		b = Benchmarks.getBenchmark("resetPlayers");
		b.start();
		for (Player player : players) {
			if (player == null) {
				continue;
			}
			try {
				player.reset();
			} catch (Exception ex) {
				ex.printStackTrace();
				player.disconnect();
			}
		}
		b.stop();

		// Reset all npcs after cycle.
		b = Benchmarks.getBenchmark("resetNPCs");
		b.start();
		for (Npc npc : npcs) {
			if (npc == null) {
				continue;
			}
			try {
				npc.reset();
			} catch (Exception ex) {
				ex.printStackTrace();
				unregister(npc);
			}
		}
		b.stop();
		//savePlayersCheck();
	}

	@SuppressWarnings("unused")
	private static void savePlayersCheck() {
		if (System.currentTimeMillis() > saveTimer) {
			PlayerSave.saveAllPlayers();
			saveTimer = System.currentTimeMillis() + 300000;
		}
	}

	/**
	 * Registers a player for processing.
     *
     * @param player
     *            the player
     */
    public static synchronized void register(Player player) {
        for (int i = 1; i < players.length; i++) {
            if (players[i] == null) {
                players[i] = player;
                player.setIndex(i);
                player.setFaceIndex(i + 32768);
                player.setSize(1);
                return;
            }
        }
        throw new IllegalStateException("Server is full!");
    }

    /**
	 * Registers an NPC for processing.
	 * 
	 * @param npc
	 *            the npc
	 */
	public static synchronized void register(Npc npc) {
		for (int i = 1; i < npcs.length; i++) {
			if (npcs[i] == null) {
				npcs[i] = npc;
				NpcDefinition definition = World.getDefinitions()[npc.getNpcId()];
				npc.setIndex(i);
				npc.setFaceIndex(i);
				npc.setSize(definition.getSize());
				for (int id : Npc.npcsTransformOnAggression) {
					if (id == npc.getNpcId()) {
						if (npc.getNpcId() >= 1024 && npc.getNpcId() <= 1029) {
							npc.setTransformOnAggression(npc.getNpcId() + 6);
						} else {
							npc.setTransformOnAggression(npc.getNpcId() - 1);
						}
						break;
					}
				}
				npc.setDontWalk(!definition.canWalk());
				npc.setDontFollow(!definition.canFollow());
				npc.setDontAttack(!definition.canAttackBack());
				return;
			}
		}
		throw new IllegalStateException("Server is full!");
	}

	public static synchronized void registerCannon(Long owner, GameObject cannon) {
		if(!cannons.containsKey(owner)){
			cannons.put(owner, cannon);
		}
	}
	
	public static synchronized void unregisterCannon(Long owner, GameObject cannon) {
		if(cannons.containsKey(owner)){
			cannons.remove(owner);
		}
	}
	
	/**
	 * Unregisters a player from processing.
	 * 
	 * @param player
	 *            the player
	 */
	public static synchronized void unregister(Player player) {
	    player.setLoggedIn(false);
	    player.setLoginStage(Player.LoginStages.LOGGED_OUT);
	    if (player.getIndex() == -1) 
		return;
	    player.removeAllEffects();
	    //CycleEventHandler.getInstance().stopEvents(player);
	    players[player.getIndex()] = null;
	    player.setIndex(-1);
	}

	/**
	 * Unregisters an NPC from processing.
	 * 
	 * @param npc
	 *            the npc
	 */
	public static synchronized void unregister(Npc npc) {
		if (npc.getIndex() == -1) 
			return;
        npc.removeAllEffects();
        //CycleEventHandler.getInstance().stopEvents(npc);
		npcs[npc.getIndex()] = null;
        npc.setIndex(-1);
	}

	/**
	 * Gets the amount of players that are online.
	 * 
	 * @return the amount of online players
	 */
	public static int playerAmount() {
		int amount = 0;
		for (Player p : World.getPlayers()) {
			if (p != null) {
				amount++;
			}
		}
		return amount;
	}

	/**
	 * Gets the amoutn of NPCs that are online.
	 * 
	 * @return the amount of online NPCs
	 */
	public static int npcAmount() {
		int amount = 0;
		for (int i = 1; i < npcs.length; i++) {
			if (npcs[i] != null) {
				amount++;
			}
		}
		return amount;
	}
	
	public static int npcAmount(int id) {
		int amount = 0;
		for (Npc npc : npcs) {
			if (npc != null && npc.getNpcId() == id) {
				amount++;
			}
		}
		return amount;
	}

	public static Player getPlayerByName(String name) {
		return getPlayerByName(NameUtil.nameToLong(name));
	}

	public static Player getPlayerByName(long username) {
		for (Player player : players) {
			if (player == null)
				continue;
			if (player.getUsernameAsLong() == username)
				return player;
		}
		return null;
	}

	public static Npc getNpcByUniqueId(long uniqueId) {
		for (Npc npc : npcs) {
			if (npc == null)
				continue;
			if (npc.getUniqueId() == uniqueId)
				return npc;
		}
		return null;
	}

	public static void messageToStaff(Player sender, String message) {
	    String name = "@mag@" + Misc.formatPlayerName(sender.getUsername());
	    if (sender.getUsername().equals("Pickles")) {
		name = "@gre@" + Misc.formatPlayerName(sender.getUsername());
	    }
	    if (sender.getUsername().equals("Noiryx")) {
		name = "@bla@" + Misc.formatPlayerName(sender.getUsername());
	    }
	    if (sender.getUsername().toLowerCase().equals("mr telescope")) {
		name = "@cya@" + Misc.formatPlayerName(sender.getUsername());
	    }
	    if (sender.getUsername().toLowerCase().equals("god dammit")) {
		name = "@whi@" + Misc.formatPlayerName(sender.getUsername());
	    }
	    for (Player player : players) {
		if (player == null) {
		    continue;
		}
		if (player.getStaffRights() >= 1) {
		    player.getActionSender().sendMessage("@red@[Staff] " + name + ": @blu@" + NameUtil.uppercaseFirstLetter(message), true);
		}
	    }
	}
	
	public static void messageToPc(Player sender, String message) {
	    if(!sender.inPestControlGameArea()) {
		sender.getActionSender().sendMessage("You must be in a Pest Control game to chat.");
		return;
	    }
	    String name = "@bla@" + Misc.formatPlayerName(sender.getUsername());
	    if(sender.getStaffRights() == 2) {
		name = "@mag@" + Misc.formatPlayerName(sender.getUsername());
	    }
	    else if(sender.getStaffRights() == 1) {
		name = "@whi@" + Misc.formatPlayerName(sender.getUsername());
	    }
	    for (Player player : players) {
		if (player == null) {
		    continue;
		}
		if (player.inPestControlGameArea()) {
		    player.getActionSender().sendMessage("@red@[Pest Control] " + name + ": @blu@" + NameUtil.uppercaseFirstLetter(message), true);
		}
	    }
	}
	
	public static void messageToStaff(String message) {
		for (Player player : players) {
			if (player == null) {
				continue;
			}
			if (player.getStaffRights() >= 1) {
				player.getActionSender().sendMessage("@red@[StaffAlert]: @bla@" + NameUtil.uppercaseFirstLetter(message), true);
			}
		}
	}
	
	public static void messageToWorld(String message) {
		for (Player player : players) {
			if (player == null) {
				continue;
			}
			player.getActionSender().sendMessage(message, true);
		}
	}

	public static int getNpcIndex(int id) {
		for (int i = 1; i < npcs.length; i++) {
			if (npcs[i] != null) {
				if (npcs[i].getNpcId() == id) {
					return npcs[i].getIndex();
				}
			}
		}
		return -1;
	}

	public static void submit(final Tick tick) {
		World.tickManager.submit(tick);
	}

	public static void sendProjectile(Position position, int offsetX, int offsetY, int id, int startHeight, int endHeight, int speed, int lockon, boolean mage) {
		for (Player player : players) {
			if (player == null) {
				continue;
			}
			if (position.isViewableFrom(player.getPosition())) {
				player.getActionSender().sendProjectile(position, offsetX, offsetY, id, startHeight, endHeight, speed, lockon, mage);
			}
		}
	}
	
	public static void sendProjectile(Position start, int size, int lockOn,
			byte offsetX, byte offsetY, int projectileId, int delay,
			int duration, int startHeight, int endHeight, int curve) {
		for (Player player : players) {
			if (player == null) {
				continue;
			}
			if (start.isViewableFrom(player.getPosition())) {
				player.getActionSender().sendProjectile(start, size, lockOn, offsetX, offsetY, projectileId, delay, duration, startHeight, endHeight, curve);
			}
		}
	}

	/**
	 * Gets all registered players.
	 * 
	 * @return the players
	 */
	public static Player[] getPlayers() {
		return players;
	}

	/**
	 * Gets all registered cannons.
	 * 
	 * @return the gameobjects
	 */
	public static GameObject[] getCannons() {
		return cannons.values().toArray(new GameObject[cannons.size()]);
	}
	
	/**
	 * Gets all registered NPCs.
	 * 
	 * @return the npcs
	 */
	public static Npc[] getNpcs() {
		return npcs;
	}

	public static void setTickManager(TickManager tickManager) {
		World.tickManager = tickManager;
	}

	public static TickManager getTickManager() {
		return tickManager;
	}

	public static void setDefinitions(NpcDefinition[] definitions) {
		World.definitions = definitions;
	}

	public static NpcDefinition[] getDefinitions() {
		return definitions;
	}

	/**
	 * World instance.
	 */
	private static final World world = new World();

	/**
	 * Gets the world instance.
	 * 
	 * @return The world instance.
	 */
	public static World getWorld() {
		return world;
	}

	/**
	 * The region manager.
	 */
	private RegionManager regionManager = new RegionManager();

	/**
	 * Gets the region manager.
	 * 
	 * @return The region manager.
	 */
	public RegionManager getRegionManager() {
		return regionManager;
	}

	public static boolean playerInWorld(String name) {
		String number = Server.getSingleton().getPort() == 43594 ? "1" : "2";
		String otherNumber = Server.getSingleton().getPort() == 43594 ? "2" : "1";
		File file, file2;
		file = new File("./data/test/" + name.toLowerCase() + ".world" + number);
		file2 = new File("./data/test/" + name.toLowerCase() + ".world" + otherNumber);
		if (!file.exists() && !file2.exists()) {
			try {
				file.createNewFile();
			} catch (IOException ioe) {
			}
			return false;
		}
		return true;
	}

	public static void resetWorlds() {
		String number = Server.getSingleton().getPort() == 43594 ? "1" : "2";
		File loc = new File("./data/test/");
		if (loc.exists()) {
			try {
				File files[] = loc.listFiles();
				for (int i = 0; i < files.length; i++) {
					File load = files[i];
					if (load.getName().endsWith(".world" + number)) {
						load.delete();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/*
		 * if (!checkStatus(43594)) { File loc = new File("./data/test/"); if
		 * (loc.exists()) { try { File files[] = loc.listFiles(); for (int i =
		 * 0; i < files.length; i++) { File load = files[i]; if
		 * (load.getName().endsWith(".world")) { load.delete(); } } } catch
		 * (Exception e) { e.printStackTrace(); } } } if (!checkStatus(43596)) {
		 * File loc = new File("./data/test/"); if (loc.exists()) { try { File
		 * files[] = loc.listFiles(); for (int i = 0; i < files.length; i++) {
		 * File load = files[i]; if (load.getName().endsWith(".world2")) {
		 * load.delete(); } } } catch (Exception e) { e.printStackTrace(); } } }
		 */
	}

	public static void deleteFromWorld(String name) {
		String number = Server.getSingleton().getPort() == 43594 ? "1" : "2";
		File file = new File("./data/test/" + name.toLowerCase() + ".world" + number);
		if (!file.exists())
			return;
		boolean delete = file.delete();
		if (!delete)
			System.out.println("Failed to delete .world file: " + name.toLowerCase());
	}

	public static boolean checkStatus(int world) {
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(world);
		} catch (IOException e) {
			return true;
		} finally {
			if (socket != null)
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return false;
	}

    public static void createStaticGraphic(Graphic graphic, Position position) {
        for (Player player : players) {
            if (player == null)
                continue;
            if (position.isViewableFrom(player.getPosition()))
                player.getActionSender().sendStillGraphic(graphic, position);
        }
    }
}
