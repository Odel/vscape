package com.rs2;

import com.rs2.cache.Cache;
import com.rs2.cache.interfaces.RSInterface;
import com.rs2.cache.object.GameObjectData;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.World;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.consumables.Food;
import com.rs2.model.content.minigames.magetrainingarena.*;
import com.rs2.model.content.quests.MonkeyMadness.ApeAtollNpcs;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.skills.fishing.FishingSpots;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcDefinition;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.npcs.drop.NpcDropController;
import com.rs2.model.players.GlobalGroundItem;
import com.rs2.model.players.HighscoresManager;
import com.rs2.model.players.Player;
import com.rs2.model.players.ShopManager;
import com.rs2.model.players.Player.LoginStages;
import com.rs2.model.players.clanchat.ClanChatHandler;
import com.rs2.model.players.item.ItemDefinition;
import com.rs2.model.players.item.ItemManager;
import com.rs2.model.tick.Tick;
import com.rs2.net.DedicatedReactor;
import com.rs2.net.packet.PacketManager;
import com.rs2.task.Task;
import com.rs2.task.TaskScheduler;
import com.rs2.util.*;
import com.rs2.util.clip.ObjectDef;
import com.rs2.util.clip.Rangable;
import com.rs2.util.clip.Region;
import com.rs2.util.sql.SQL;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The main core of RuneSource.
 *
 * @author blakeman8192
 */
public class Server implements Runnable {

	private static Server singleton;
	private final String host;
	private final int port;

	private final int cycleRate;
	private long cycle;
	private int infoDisplayCounter = 10;
	private static long minutesCounter;
	
	private Selector selector;
	private InetSocketAddress address;
	private ServerSocketChannel serverChannel;
	private Misc.Stopwatch cycleTimer;
	private final Queue<Player> loginQueue = new ConcurrentLinkedQueue<Player>();
    
    private static final Queue<Player> disconnectedPlayers = new LinkedList<Player>();
    private static final TaskScheduler scheduler = new TaskScheduler();

	/**
	 * Creates a new Server.
	 *
	 * @param host
	 *            the host
	 * @param port
	 *            the port
	 * @param cycleRate
	 *            the cycle rate
	 */
	private Server(String host, int port, int cycleRate) {
		this.host = host;
		this.port = port;
		this.cycleRate = cycleRate;
	}

	/**
	 * The main method.
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		String host = "0.0.0.0";//args[0];
		int port = 43594;
		int cycleRate = 600;
		Constants.DEVELOPER_MODE = false;
		Constants.SERVER_DEBUG = false;
		Constants.UNLIMITED_RUN = true;
		Constants.SQL_ENABLED = true;

        //PlayerCleaner.start();
        //System.exit(0);
		
        if (host.equals("127.0.0.1")) {
            System.out.println("Starting live server!");
            Constants.DEVELOPER_MODE = false;
            Constants.SQL_ENABLED = false;
            Constants.SERVER_DEBUG = false;
            Constants.HIGHSCORES_ENABLED = false;
            Constants.ADMINS_CAN_INTERACT = true;
            Constants.RSA_CHECK = true;
            Constants.CLIENT_VERSION = 317;
        }
        
        if(Constants.SQL_ENABLED)
        {
        
        	if(Constants.SQL_TYPE == 2)
        	{
        		File f = new File("./test.db"); 
        		if(!f.exists())
        		{
        			SQL.initHighScores();
        		}
        	}
        	else
        	{
            	SQL.createConnection();
        	}
        }

        //PlayerCleaner.start();
        //System.exit(0);

		setSingleton(new Server(host, port, cycleRate));

		new Thread(getSingleton()).start();
	}

    public static Queue<Player> getDisconnectedPlayers() {
        return disconnectedPlayers;
    }

    public void queueLogin(Player player) {
		loginQueue.add(player);
	}

	@Override
	public void run() {
		try {
			Thread.currentThread().setName("ServerEngine");
			System.setOut(new Misc.TimestampLogger(System.out));
			System.setErr(new Misc.TimestampLogger(System.err, "./data/err.log"));

			address = new InetSocketAddress(host, port);
			System.out.println("Starting " + Constants.SERVER_NAME + " on " + address + "...");

			// load shutdown hook
			Thread shutdownhook = new ShutDownHook();
			Runtime.getRuntime().addShutdownHook(shutdownhook);

			PacketManager.loadPackets();

            Cache.load();


            // load scripts
            Misc.loadScripts(new File("./data/ruby/"));
            
            GlobalVariables.patchNotes = Misc.loadPatchNotes();
            GlobalVariables.info = Misc.loadInfo();
            GlobalVariables.rules = Misc.loadRules();
            GlobalVariables.degradeInfo = Misc.loadDegradeInfo();
            GlobalVariables.npcDump = Misc.getNpcDump();
            GlobalVariables.itemDump = Misc.getItemDump();
            Misc.initAlphabet();


    		//AreaDefinition.init();
            ItemDefinition.init();
    		NpcDropController.init();
    		NpcDefinition.init();
    		ShopManager.loadShops();
    		Food.init();
			// load all xstream related files.
			XStreamUtil.loadAllFiles();

			// item weights
			ItemDefinition.loadWeight();

            //interfaces
            RSInterface.load();

			// Load plugins
			//PluginManager.loadPlugins();

			// Load regions
			ObjectDef.loadConfig();
			Region.load();
			Rangable.load();

			// Load objects
			ObjectLoader objectLoader = new ObjectLoader();
			objectLoader.load();

			GameObjectData.init();

			// load combat manager
			CombatManager.init();

			// Load minute timer
			startMinutesCounter();

			// global drops
			GlobalGroundItem.initialize();

			// load npc ls
			Npc.loadNpcDrops();
			
			// mage arena timers
			 AlchemistPlayground.loadAlchemistPlayGround();
			 EnchantingChamber.loadEnchantingChamber();
			 CreatureGraveyard.loadCreatureGraveyard();
			 TelekineticTheatre.loadTelekineticTheatre();
			// spawning world fishing spots
			FishingSpots.spawnFishingSpots();
			
	        QuestHandler.init();

			NpcLoader.loadAutoSpawn("./data/npcs/spawn-config.cfg");
			ApeAtollNpcs.init();

            HighscoresManager.load();
            
            PlayerSave.saveCycle();
            
            GlobalVariables.loadBans();
            
            ClanChatHandler.loadClans();

			// Start up and get a'rollin!
			startup();
			System.out.println("Online!");
		/*	while (!Thread.interrupted()) {
				try {
					cycle();
					sleep();
				} catch (Exception ex) {
					PlayerSave.saveAllPlayers();
					ex.printStackTrace();
				}
			}*/
			scheduler.schedule(new Task() {
				@Override
				protected void execute() {
					try {
						cycle();
					} catch (Exception ex) {
						PlayerSave.saveAllPlayers();
						ex.printStackTrace();
						stop();
					}
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//PluginManager.close();
	}

	/**
	 * Starts the server up.
	 *
	 * @throws java.io.IOException
	 */
	private void startup() throws Exception {
		// Initialize the networking objects.
		selector = Selector.open();
		serverChannel = ServerSocketChannel.open();
		DedicatedReactor.setInstance(new DedicatedReactor(Selector.open()));
		DedicatedReactor.getInstance().start();

		// ... and configure them!
		serverChannel.configureBlocking(false);
		serverChannel.socket().bind(address);

		synchronized (DedicatedReactor.getInstance()) {
			DedicatedReactor.getInstance().getSelector().wakeup();
			serverChannel.register(DedicatedReactor.getInstance().getSelector(), SelectionKey.OP_ACCEPT);
		}

		// Finally, initialize whatever else we need.
		//cycleTimer = new Misc.Stopwatch();
	}

	/**
	 * Accepts any incoming connections.
	 *
	 * @throws java.io.IOException
	 */
	public static void accept(SelectionKey key) throws IOException {
		ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();

		// Accept the socket channel.
		SocketChannel channel = serverChannel.accept();
		if (channel == null) {
			return;
		}

		// Make sure we can allow this connection.
		if (!HostGateway.enter(channel.socket().getInetAddress().getHostAddress())) {
			channel.close();
			return;
		}

		// Set up the new connection.
		channel.configureBlocking(false);
		SelectionKey newKey = channel.register(key.selector(), SelectionKey.OP_READ);
		Player player = new Player(newKey);
		newKey.attach(player);
	}

	/**
	 * Performs a server cycle.
	 */
	private void cycle() {
		int loggedIn = 0;
        Benchmark b = Benchmarks.getBenchmark("loginQueue");
        b.start();
		while (!loginQueue.isEmpty() && loggedIn++ < 50) {
			Player player = loginQueue.poll();
			try {
				player.finishLogin();
				player.setLoginStage(LoginStages.LOGGED_IN);
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("Error, infinite DC loop for this player");
				player.disconnect();
			}
		}
        b.stop();

        b = Benchmarks.getBenchmark("handleNetworkPackets");
        b.start();
		// Handle all network events.
		try {
			selector.selectNow();
			for (SelectionKey selectionKey : selector.selectedKeys()) {
				if (selectionKey.isValid()) {
					if (selectionKey.isReadable()) {
						// Tell the client to handle the packet.
						PacketManager.handleIncomingData((Player) selectionKey.attachment());
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        b.stop();

		// Next, perform game processing.
		try {
			//PluginManager.tick();
			World.process();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        b = Benchmarks.getBenchmark("disconnectingPlayers");
        b.start();
        synchronized (disconnectedPlayers) {
            for (Iterator<Player> players = disconnectedPlayers.iterator(); players.hasNext();) {
                Player player = players.next();
                if (player.logoutDisabled())
                    continue;
                player.logout();
                players.remove();
            }
        }
        b.stop();
        Benchmarks.resetAll();
      /*  if(infoDisplayCounter == 0){
        	System.out.println("[ENGINE]: Server load: " + cycle + "% with " + World.playerAmount() + " players");
        	infoDisplayCounter = 300;
    	}else{
        	infoDisplayCounter--;
    	}*/
	}

	/**
	 * Sleeps for the cycle delay.
	 *
	 * @throws InterruptedException
	 */
	private void sleep() {
		try {
			long sleepTime = cycleRate - cycleTimer.elapsed();
            boolean sleep = sleepTime > 0 && sleepTime < 600;
            for (int i = 0; i < PacketManager.SIZE; i++) {
                Benchmark b = PacketManager.packetBenchmarks[i];
                if (!sleep && b.getTime() > 0)
                    System.out.println("Packet "+i+"["+PacketManager.packets[i].getClass().getSimpleName()+"] took "+b.getTime()+" ms.");
                b.reset();
            }
			if (sleep) {
				cycle = 0;
                Benchmarks.resetAll();
				//System.out.println("[ENGINE]: Sleeping for " + sleepTime + "ms");
				Thread.sleep(sleepTime);
			} else {
				// The server has reached maximum load, players may now lag.
				cycle = (100 + Math.abs(sleepTime) / (cycleRate / 100));
				/*if (cycle > 999) {
					initiateRestart();
				}*/
				System.out.println("[WARNING]: Server Overload: " + cycle + "%!");
                Benchmarks.printAll();
                Benchmarks.resetAll();
              /*  for (int i = 0; i < 5; i++)
                    System.out.println("");*/
			}
		} catch (Exception ex) {
            ex.printStackTrace();

		} finally {
			cycleTimer.reset();
		}
	}

	@SuppressWarnings("unused")
	private void initiateRestart() {
		for (Player player : World.getPlayers()) {
            if (player == null || player.getIndex() == -1)
                continue;
            player.getActionSender().sendUpdateServer(30);
        }
		new ShutdownWorldProcess(30).start();
	}

	/**
	 * Starts the minute counter
	 */
	private void startMinutesCounter() {
		try {
			BufferedReader minuteFile = new BufferedReader(new FileReader("./data/minutes.log"));
			Server.minutesCounter = Integer.parseInt(minuteFile.readLine());
			minuteFile.close();// CLOSEFILE
		} catch (Exception e) {
			e.printStackTrace();
		}

		//originaly 25 1/4 15 secs - changed to 50 1/2 30 secs - (100 ticks =  1 minute)
		World.submit(new Tick(50) {
		    @Override public void execute() {
		        setMinutesCounter(getMinutesCounter() + 1);
                for (Player player : World.getPlayers()) {
                  if (player == null) {
                      continue;
                  }
                  player.getAllotment().processGrowth();
                  player.getFlowers().processGrowth();
                  player.getHerbs().processGrowth();
                  player.getHops().processGrowth();
                  player.getBushes().processGrowth();
                  player.getTrees().processGrowth();
                  player.getFruitTrees().processGrowth();
                  player.getSpecialPlantOne().doCalculations();
                  player.getSpecialPlantTwo().doCalculations();
                  //lowering all player items timer
	              ItemManager.getInstance().lowerAllItemsTimers(player);
                }
		    }
        });

	}

	public static void setMinutesCounter(long minutesCounter) {
		Server.minutesCounter = minutesCounter;
		try {
			BufferedWriter minuteCounter = new BufferedWriter(new FileWriter("./data/minutes.log"));
			minuteCounter.write(Long.toString(getMinutesCounter()));
			minuteCounter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static long getMinutesCounter() {
		return minutesCounter;
	}

	/**
	 * Sets the server singleton object.
	 * 
	 * @param singleton
	 *            the singleton
	 */
	public static void setSingleton(Server singleton) {
		if (Server.singleton != null) {
			throw new IllegalStateException("Singleton already set!");
		}
		Server.singleton = singleton;
	}

	/**
	 * Gets the server singleton object.
	 * 
	 * @return the singleton
	 */
	public static Server getSingleton() {
		return singleton;
	}

	/**
	 * Gets the selector.
	 * 
	 * @return The selector
	 */
	public Selector getSelector() {
		return selector;
	}

	public int getPort() {
		return port;
	}

}
