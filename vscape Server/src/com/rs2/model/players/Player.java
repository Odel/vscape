package com.rs2.model.players;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Timer;
import java.util.TimerTask;

import com.rs2.Constants;
import com.rs2.GlobalVariables;
import com.rs2.HostGateway;
import com.rs2.Server;
import com.rs2.cache.interfaces.RSInterface;
import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.BankPin;
import com.rs2.model.content.Emotes;
import com.rs2.model.content.Following;
import com.rs2.model.content.Pets;
import com.rs2.model.content.SkillGuides;
import com.rs2.model.content.WalkInterfaces;
import com.rs2.model.content.combat.AttackType;
import com.rs2.model.content.combat.CombatCycleEvent;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.combat.SkullRecord;
import com.rs2.model.content.combat.special.SpecialType;
import com.rs2.model.content.combat.util.Degradeables;
import com.rs2.model.content.combat.weapon.Weapon;
import com.rs2.model.content.combat.weapon.CombatSounds;
import com.rs2.model.content.combat.weapon.RangedAmmo;
import com.rs2.model.content.consumables.Food;
import com.rs2.model.content.consumables.Potion;
import com.rs2.model.content.dialogue.DialogueManager;
import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.content.minigames.RuneDraw;
import com.rs2.model.content.minigames.barrows.Barrows;
import com.rs2.model.content.minigames.castlewars.Castlewars;
import com.rs2.model.content.minigames.warriorsguild.WarriorsGuild;
import com.rs2.model.content.minigames.duelarena.DuelAreas;
import com.rs2.model.content.minigames.duelarena.DuelInterfaces;
import com.rs2.model.content.minigames.duelarena.DuelMainData;
import com.rs2.model.content.minigames.duelarena.PlayerInteraction;
import com.rs2.model.content.minigames.fightcaves.FightCaves;
import com.rs2.model.content.minigames.magetrainingarena.AlchemistPlayground;
import com.rs2.model.content.minigames.magetrainingarena.CreatureGraveyard;
import com.rs2.model.content.minigames.magetrainingarena.EnchantingChamber;
import com.rs2.model.content.minigames.magetrainingarena.TelekineticTheatre;
import com.rs2.model.content.minigames.pestcontrol.*;
import com.rs2.model.content.quests.ChristmasEvent;
import com.rs2.model.content.quests.GhostsAhoyPetition;
import com.rs2.model.content.quests.MonkeyMadness.ApeAtoll;
import com.rs2.model.content.quests.MonkeyMadness.ApeAtollNpcs;
import com.rs2.model.content.quests.MonkeyMadness.MonkeyMadness;
import com.rs2.model.content.quests.MonkeyMadness.MonkeyMadnessVars;
import com.rs2.model.content.quests.NatureSpirit;
import com.rs2.model.content.quests.PiratesTreasure;
import com.rs2.model.content.quests.PlagueCity;
import com.rs2.model.content.randomevents.EventsConstants;
import com.rs2.model.content.randomevents.RandomHandler;
import com.rs2.model.content.skills.ItemOnItemHandling;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillResources;
import com.rs2.model.content.skills.SkillcapeEmotes;
import com.rs2.model.content.skills.SkillCapeHandler;
import com.rs2.model.content.skills.Woodcutting.Canoe;
import com.rs2.model.content.skills.agility.AgilityCourses;
import com.rs2.model.content.skills.cooking.BrewData;
import com.rs2.model.content.skills.cooking.Cooking;
import com.rs2.model.content.skills.cooking.FillHandler;
import com.rs2.model.content.skills.cooking.Wine;
import com.rs2.model.content.skills.farming.Allotments;
import com.rs2.model.content.skills.farming.Bushes;
import com.rs2.model.content.skills.farming.Compost;
import com.rs2.model.content.skills.farming.Flowers;
import com.rs2.model.content.skills.farming.FruitTree;
import com.rs2.model.content.skills.farming.Herbs;
import com.rs2.model.content.skills.farming.Hops;
import com.rs2.model.content.skills.farming.Seedling;
import com.rs2.model.content.skills.farming.SpecialPlantOne;
import com.rs2.model.content.skills.farming.SpecialPlantTwo;
import com.rs2.model.content.skills.farming.ToolLeprechaun;
import com.rs2.model.content.skills.farming.WoodTrees;
import com.rs2.model.content.skills.firemaking.Firemaking;
import com.rs2.model.content.skills.fishing.Fishing;
import com.rs2.model.content.skills.magic.Spell;
import com.rs2.model.content.skills.magic.SpellBook;
import com.rs2.model.content.skills.magic.Teleportation;
import com.rs2.model.content.skills.mining.MineOre;
import com.rs2.model.content.skills.prayer.BoneBurying;
import com.rs2.model.content.skills.prayer.Prayer;
import com.rs2.model.content.skills.prayer.Prayer.PrayerData;
import com.rs2.model.content.skills.runecrafting.Pouches;
import com.rs2.model.content.skills.runecrafting.Tiaras;
import com.rs2.model.content.skills.slayer.Slayer;
import com.rs2.model.content.tutorialisland.NewComersSide;
import com.rs2.model.content.tutorialisland.StagesLoader;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcDefinition;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.npcs.functions.Cat;
import com.rs2.model.players.bank.BankManager;
import com.rs2.model.players.clanchat.ClanChat;
import com.rs2.model.players.clanchat.ClanChatHandler;
import com.rs2.model.players.container.Container;
import com.rs2.model.players.container.Container.Type;
import com.rs2.model.players.container.equipment.Equipment;
import com.rs2.model.players.container.inventory.Inventory;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;
import com.rs2.model.players.item.ItemManager;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.model.tick.Tick;
import com.rs2.model.region.music.RegionMusic;
import com.rs2.model.region.music.MusicPlayer;
import com.rs2.net.ActionSender;
import com.rs2.net.DedicatedReactor;
import com.rs2.net.ISAACCipher;
import com.rs2.net.Login;
import com.rs2.net.StreamBuffer;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager;
import com.rs2.util.Area;
import com.rs2.util.Benchmark;
import com.rs2.util.Benchmarks;
import com.rs2.util.Misc;
import com.rs2.util.PlayerSave;
import com.rs2.util.plugin.LocalPlugin;
import com.rs2.util.plugin.PluginManager;
import com.rs2.util.sql.SQL;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.quests.QuestVariables;
import com.rs2.model.content.quests.RecruitmentDrive;
import com.rs2.model.content.quests.SantaEncounter;
import com.rs2.model.content.skills.ranging.DwarfMultiCannon;
import com.rs2.model.content.skills.farming.MithrilSeeds;
import com.rs2.model.content.skills.firemaking.BarbarianSpirits;
import com.rs2.model.content.skills.prayer.Ectofuntus;
import com.rs2.model.content.treasuretrails.Puzzle;

/**
 * Represents a logged-in player.
 * 
 * @author blakeman8192
 * @author BFMV
 */
public class Player extends Entity {
    private int combatLevel;
	private final SelectionKey key;
	private final ByteBuffer inData;
	private final ByteBuffer outData;
	private SocketChannel socketChannel;
	private LoginStages loginStage = LoginStages.CONNECTED;
	private ISAACCipher encryptor;
	private ISAACCipher decryptor;
	private int opcode = -1;
	private int packetLength = -1;
	private String username;
	private String password;
	private String macAddress = "NONE";
	private int clientVersion;
	private int magicId;
	private int lastNpc = -1;
	private int inter = -1;
	private final Misc.Stopwatch timeoutStopwatch = new Misc.Stopwatch();
	private final List<Player> players = new LinkedList<Player>();
	private final List<Npc> npcs = new LinkedList<Npc>();
	public Inventory inventory = new Inventory(this);
	private Equipment equipment = new Equipment(this);
	private BankManager bankmanager = new BankManager(this);
	private PrivateMessaging privateMessaging = new PrivateMessaging(this);
	private Prayer prayer = new Prayer(this);
	private Teleportation teleportation = new Teleportation(this);
	private Emotes emotes = new Emotes(this);
	private SkillcapeEmotes skillcapeEmotes = new SkillcapeEmotes(this);
	private SkillCapeHandler skillcapeHandler = new SkillCapeHandler(this);
	private Skill skill = new Skill(this);
	private ActionSender actionSender = new ActionSender(this);
	private RuneDraw runeDraw = new RuneDraw(this);
	private Puzzle puzzle = new Puzzle(this);
	private MithrilSeeds seeds = new MithrilSeeds(this);
	private Barrows barrows = new Barrows(this);
	private BarbarianSpirits barbarianSpirits = new BarbarianSpirits(this);
	private GhostsAhoyPetition petition = new GhostsAhoyPetition(this);
	private RandomHandler randomHandler = new RandomHandler(this);
	private boolean wyvernWarned = false;
	private Slayer slayer = new Slayer(this);
	private NewComersSide newComersSide = new NewComersSide(this);
	private PlayerInteraction playerInteraction = new PlayerInteraction(this);
	private AgilityCourses agilityCourse = new AgilityCourses(this);
	private RegionMusic regionMusic = new RegionMusic(this);
	private MusicPlayer musicPlayer = new MusicPlayer(this);
	private CombatSounds combatSounds = new CombatSounds(this);
	private DuelMainData duelMainData = new DuelMainData(this);
	private AlchemistPlayground alchemistPlayground = new AlchemistPlayground(this);
	private CreatureGraveyard creatureGraveyard = new CreatureGraveyard(this);
	private TelekineticTheatre telekineticTheatre = new TelekineticTheatre(this);
	private EnchantingChamber enchantingChamber = new EnchantingChamber(this);
	private Ectofuntus ectofuntus = new Ectofuntus(this);
	private int enchantingPizazz = 0;
	private int enchantingOrbCount = 0;
	private int enchantingEnchantCount = 0;
	private int telekineticPizazz = 0;
	private int telekineticMazesCompleted = 0;
	private int alchemistPizazz = 0;
	private int graveyardPizazz = 0;
	private int graveyardFruitDeposited = 0;
	private boolean bonesToPeachesEnabled = false;
	private ArrayList<Position> pestControlBarricades = new ArrayList<Position>();
	private DuelInterfaces duelInterfaces = new DuelInterfaces(this);
	private DuelAreas duelAreas = new DuelAreas(this);
	private Wine wine = new Wine(this);
	private Fishing fishing = new Fishing(this);
	private ItemOnItemHandling itemOnItem = new ItemOnItemHandling(this);
	private SkillGuides skillGuides = new SkillGuides(this);
	private Food food = new Food(this);
	private Potion potion = new Potion(this);
	private MineOre mining = new MineOre(this);
	private Cooking cooking = new Cooking(this);
	private FillHandler fillHandler = new FillHandler(this);
	private Compost compost = new Compost(this);
	private Allotments allotment = new Allotments(this);
	private Flowers flower = new Flowers(this);
	private Herbs herb = new Herbs(this);
	private Hops hops = new Hops(this);
	private Bushes bushes = new Bushes(this);
	private Seedling seedling = new Seedling(this);
	private WoodTrees trees = new WoodTrees(this);
	private FruitTree fruitTrees = new FruitTree((this));
	private SpecialPlantOne specialPlantOne = new SpecialPlantOne(this);
	private SpecialPlantTwo specialPlantTwo = new SpecialPlantTwo(this);
	private ToolLeprechaun toolLeprechaun = new ToolLeprechaun(this);
	private Firemaking firemaking = new Firemaking(this);
	private BoneBurying boneBurying = new BoneBurying(this);
	private SkillResources skillResources = new SkillResources(this);
	private BrewData brewData = new BrewData(this);
	private Pets pets = new Pets(this);
	private Cat cat = new Cat(this);
	private MonkeyMadnessVars MMVars = new MonkeyMadnessVars(this);
	private QuestVariables questVars = new QuestVariables(this);
	private DialogueManager dialogue = new DialogueManager(this);
	private BankPin bankPin = new BankPin(this);
	private Login login = new Login();
	private Position currentRegion = new Position(0, 0, 0);
	private int staffRights = 0;
	private boolean member;
	private int chatColor;
	private byte[] chatText;
	private int pouchData[] = { 0, 0, 0, 0 };
	private int gender = Constants.GENDER_MALE;
	private final int[] appearance = new int[7];
	private final int[] colors = new int[5];
	private Container trade = new Container(Type.STANDARD, Inventory.SIZE);
	private boolean pickupItem;
	public boolean trackerGnome1 = false;
	public boolean trackerGnome2 = false;
	public boolean trackerGnome3 = false;
	private int clickX = -1;
	private int clickY = -1;
	private int clickZ = -1;
	private int clickId = -1;
	private int clickItemId = -1;
	private int interfaceId = -1;
	private int slot = -1;
	private boolean equipmentOperate = false;
	private int npcClickIndex;
	private boolean withdrawAsNote;
	private int enterXId;
	private int enterXSlot;
	private int enterXInterfaceId;
	private BankOptions bankOptions = BankOptions.SWAP_ITEM;
	private int shopId;
	private boolean isLoggedIn;
	private Map<Integer, Integer> bonuses = new HashMap<Integer, Integer>();
	private boolean friendsConverted = false;
	private long[] friends = new long[200];
	private boolean ignoresConverted = false;
	private long[] ignores = new long[100];
	private int currentDialogueId;
	private int currentOptionId;
	private int optionClickId;
	private int currentGloryId;
	private int returnCode = Constants.LOGIN_RESPONSE_OK;
	private TradeStage tradeStage = TradeStage.WAITING;
	private int[] pendingItems = new int[Inventory.SIZE];
	private int[] pendingItemsAmount = new int[Inventory.SIZE];
	private boolean usingShop = false;
	private double energy = 100;
	private boolean needsPlacement;
	private boolean resetMovementQueue;
	private boolean appearanceUpdateRequired;
	private boolean killedTreeSpirit;
	private boolean resetbank;
	public SantaEncounter santaEncounter = new SantaEncounter(this);
	public boolean snowballsReady = false;
	public boolean snowballsTimerRunning = false;
	public boolean encounterRunning = false;
	public boolean skillCapeBoost = false;
	private boolean killedJungleDemon;
	private int prayerIcon = -1;
	private int skullIcon = -1;
	private int serverPoints = 0;
	private boolean[] isUsingPrayer = new boolean[24];
	private boolean[] ernestLevers = new boolean[6];
	private boolean hurkotsSpawned = false;
	private int prayerDrainTimer = 6;
	private SpellBook magicBookType = SpellBook.MODERN;
	private boolean autoRetaliate = false;
	private boolean isSkulled;
	private boolean stopPlayerPacket;
	private int screenBrightness = 2;
	private int mouseButtons = 0;
	private int chatEffects = 1;
	private int splitPrivateChat = 0;
	private int acceptAid = 0;
	private int musicVolume = 0;
	private int musicLoop = 0;
	private boolean musicAuto = true;
	private int effectVolume = 0;
	private int questPoints = 0;
	private boolean canHaveGodCape = true;
	private boolean specialAttackActive = false;
	
	private int godBook = 0;
	private int lostGodBook = 0;
	private double specialDamage = 1, specialAccuracy = 1;
	private int specialAmount = 100;
	private int ringOfRecoilLife = 40;
	private int ringOfForgingLife = 140;
	private int bindingNeckCharge = 15;
	private int clayBraceletLife = 28;
	private int defender = 8843;
	private int innoculationBraceletLife = 275;
	private int fightMode, fightType, fightStyle, fightXp;
	private boolean usingBow, usingCross, usingCrystalBow, usingArrows, usingBolts, usingOtherRangedWeapon, dropArrow;
	private boolean fullDharok, fullAhrim, fullKaril, fullTorag, fullGuthan, fullVerac, fullVoidMelee, fullVoidRange, fullVoidMage, voidMace;
	private List<LocalPlugin> plugins = new ArrayList<LocalPlugin>();
	private long lastFire;
	private long stopProtectPrayer;
	private int currentWalkableInterface;
	private int oldItem;
	private int oldObject;
	private int smithInterface;
	private int runecraftNpc = 553;
	private int hintIndex = -1;
	public ArrayList<Position> followPath = new ArrayList<Position>();
	public String statedInterface = "";
	public Npc spawnedNpc;
	private long usernameAsLong;
	private boolean hamTrapDoor;
	private boolean inCutscene = false;
	private Weapon equippedWeapon = Weapon.FISTS;
	private SpecialType specialType;
	private List<SkullRecord> skullRecords;
	private Spell castedSpell, autoSpell;
	private boolean autoCasting = false;
	private boolean warriorsGuildGameActive;
	private boolean warriorsGuildFirstTime;
	private boolean spokeToGarv;
	private boolean spokeToGrubor;
	private boolean spokeToAlfonse;
	private boolean spokeToCharlie;
	public boolean dfsTimer = false;
	private int tokenTime;
	private int fightCavesWave;
	private int fightCavesKillCount;
	public int currentX, currentY;
	private boolean hideWeapons;
    String[] badNames = {"mod", "Mod", "admin", "Admin", "owner", "Owner"};
    private Area loadedLandscape;
    private List<GroundItem> groundItems = new LinkedList<GroundItem>();
    private long muteExpire, banExpire;
    private boolean barrowsNpcDead[] = new boolean[6];
    private int killCount;
    private int randomGrave;
    private boolean loggingOut;
    private boolean brimhavenDungeonOpen;
    private Position teleotherPosition;
    private Item destroyItem;
    private boolean hearMessage = true;
    private boolean bankWarning;
    public boolean debugCombat;
    private Npc randomEventNpc;
    private Item randomHerb;
    private int genieSelect;
    private boolean hideYell = false;
    private boolean hideColors = false;
    public boolean showHp = false;
    private Entity armorPiercedEntity = null;
	public Object[][] questData = {
	// questName, currentStage, finishedStage, questPoints
	{"Getting Started", 0, 2, 1}};
	public int[] questStage = new int[0];
	
	private int[] sidebarInterfaceId = { 2423, 3917, 638, 3213, 1644, 5608, 0, 25000, 5065,
            5715, 2449, 904, 147, 962 };

	// Public ints
	public int moveToX, moveToY, moveToH, objectWalkX, objectWalkY, objectX, objectY, objectXOffset, objectYOffset, objectXSize, objectYSize;

	public Position npcClickingLocation, objectClickingLocation;

	public boolean visible = true;
	public boolean tempBoolean = false;
	public boolean wildyWarned = false;
	public int transformNpc = -1;
	public double totalWeight;
	public double sextantBarDegree;
	public int sextantSunCoords;
	public int rotationFactor;
	public int sextantGlobalPiece;
	public int sextantLandScapeCoords;
	public int clue1Amount;
	public int clue2Amount;
	public int clue3Amount;
	public boolean killedClueAttacker;
	public int clueLevel;
	public int challengeScroll;
	public int skillAnswer;
	public int pcSkillPoints;
	private int tempInteger;
	public boolean isCrossingObstacle = false;
	public int currentSong;
	private int runAnim = -1, standAnim = -1, walkAnim = -1;
	private String host;
    private int sideBarOpen;
    private int[] pinAttempt = new int[4];
    private long logoutTimer;
    private int coalTruckAmount;
    private int dfsCharges = 0;
    private boolean receivedMasks = false;
    private boolean hasZombieHead = false;

	private Player lastPersonTraded;
	private Player lastPersonChallenged;

	private int drunkTimer = 0;
	private boolean isDrunk;
	
	private int pcDamage = 0;
	private int pcPoints = 0;
	private int zamorakCasts = 0;
	private int saradominCasts = 0;
	private int guthixCasts = 0;
	private int mageArenaStage = 0;
	private int[] degradeableHits = new int[26];
	//private ArrayList<BoneBurying.Bone> bonesGround = new ArrayList<BoneBurying.Bone>();
	private ArrayList<BoneBurying.Bone> bonesInBin = new ArrayList<BoneBurying.Bone>();
	//private boolean bonesGrinded = false;
//	private boolean secondTryAtBin = false;
	private int ectoWorshipCount = 0;
	private Canoe canoe = new Canoe(this);
    private boolean homeTeleporting = false;
    private DwarfMultiCannon dwarfMultiCannon = new DwarfMultiCannon(this);
    private boolean inJail = false;
    private DesertHeat desertHeat = new DesertHeat(this);
    private int castleWarsTeam = -1;
    private boolean ironman = false;
    private int minloggedout = 0;
    
    private ClanChat currentClanChat;
    private boolean movementDisabled = false;
    
	public void resetAnimation() {
		getUpdateFlags().sendAnimation(-1);
	}
	
	public void resetTransform() {         
		transformNpc = 0;
		setStandAnim(-1);
		setWalkAnim(-1);
		setRunAnim(-1);
		setAppearanceUpdateRequired(true);
	}

	@Override
	public void reset() {         
		getUpdateFlags().reset();
		setPrimaryDirection(-1);
		setSecondaryDirection(-1);
		setAppearanceUpdateRequired(false);
		setResetMovementQueue(false);
		setNeedsPlacement(false);
		setUsingShop(false);
		setChatText(null);
	}

	@Override
	public void initAttributes() {
		getAttributes().put("smithing", Boolean.FALSE);
		getAttributes().put("smelting", Boolean.FALSE);
		getAttributes().put("isBanking", Boolean.FALSE);
		getAttributes().put("isShopping", Boolean.FALSE);
		getAttributes().put("canPickup", Boolean.FALSE);
		getAttributes().put("canTakeDamage", Boolean.TRUE);
	}

	public Player(SelectionKey key) {
		this.key = key;
		inData = ByteBuffer.allocateDirect(512);
		outData = ByteBuffer.allocateDirect(8192);
		if (key != null) {
			socketChannel = (SocketChannel) key.channel();
			host = socketChannel.socket().getInetAddress().getHostAddress();
		}
		setPosition(new Position(Constants.START_X, Constants.START_Y, Constants.START_Z));
		initAttributes();

		// Set the default appearance.
		setDefaultAppearance();

		// Set the default colors.
		getColors()[0] = 7;
		getColors()[1] = 0;
		getColors()[2] = 9;
		getColors()[3] = 5;
		getColors()[4] = 0;
		for (int i = 0; i < pendingItems.length; i++) {
			pendingItems[i] = -1;
			pendingItemsAmount[i] = 0;
		}
		this.skullRecords = new LinkedList<SkullRecord>();
	}

	public void setDefaultAppearance() {
		setGender(0);
        int[] defaultAppearances = Constants.APPEARANCE_RANGES[gender][0];
        System.arraycopy(defaultAppearances, 0, appearance, 0, appearance.length);
        int[] defaultColors = Constants.COLOR_RANGES[gender][0];
        System.arraycopy(defaultColors, 0, colors, 0, colors.length);
	}

	public void handlePacket() {
		StreamBuffer.InBuffer in = StreamBuffer.newInBuffer(inData);
		Packet p = new Packet(opcode, packetLength, in);
		boolean dispatch = true;
        PacketManager.packetBenchmarks[p.getOpcode()].start();
		for (LocalPlugin lp : plugins) {
			if (!lp.onPacketArrival(p)) {
				dispatch = false;
			}
		}
		if (dispatch && !this.stopPlayerPacket) {
			PacketManager.handlePacket(this, p);
		}
        PacketManager.packetBenchmarks[p.getOpcode()].stop();

	}

	public boolean timeOutCheck() {
		// If no packet for more than x * 1000 seconds, disconnect.
		if (isLoggedIn() && getTimeoutStopwatch().elapsed() > 300000 && this.getStaffRights() < 2) { //5 minutes
			return true;
		}
		return false;
	}

	public void send(ByteBuffer buffer) {
		if(buffer == null)
			return;
        if (socketChannel == null)
            return;
        if (!socketChannel.isOpen())
        	return;
		// Prepare the buffer for writing.
		buffer.flip();
		try {
			// ...and write it!
			socketChannel.write(buffer);
			// If not all the data was sent
			if (buffer.hasRemaining()) {
				// Queue it
				synchronized (getOutData()) {
					getOutData().put(buffer);
				}

				// And flag write interest
				synchronized (DedicatedReactor.getInstance()) {
					DedicatedReactor.getInstance().getSelector().wakeup();
					key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
				}
			}
		} catch (Exception ex) {
			System.out.println("Failed to write packet to client");
			disconnect();
		}
	}

	/**
	 * Here we can allow updating of methods who write data to the buffer This
	 * updates all the time and prevents buffer writing bug
	 */
    public void sendTickUpdatesOnLogin() {
        final Player player = this;
        World.submit(new Tick(3) {
            @Override
            public void execute() {
                this.stop();
                getAttributes().put("canPickup", Boolean.TRUE);
                getPrivateMessaging().refresh(false);
                WalkInterfaces.addWalkableInterfaces(player);
                getActionSender().sendEnergy();
            }
        });
    }
    
	public void logout() {
		if(Constants.SQL_ENABLED)
		{
			SQL.saveHighScore(this);
		}
        if(getClanChat() != null)
        {
        	getClanChat().leaveChat(this, true);
        }
        if(inPestControlLobbyArea())
        {
        	PestControl.leaveLobby(this, true);
        }
        else if(inPestControlGameArea())
        {
        	PestControl.leaveGame(this, true);
        }
        else if(inFightCaves()) {
        	FightCaves.destroyNpcs(this);
        }
        else if(inCwLobby())
        {
        	Castlewars.LeaveLobby(this, true);
        }
        else if(inCwGame())
        {
        	Castlewars.LeaveGame(this, true, 0);
        }
	else if(this.inEnchantingChamber()) {
		this.getEnchantingChamber().saveVariables();
	}
	else if(this.inAlchemistPlayground()) {
		this.getAlchemistPlayground().saveVariables(true);
	}
	else if(this.inCreatureGraveyard()) {
		this.getCreatureGraveyard().saveVariables();
	}
	else if(this.inTelekineticTheatre()) {
		this.getTelekineticTheatre().saveVariables();
	} else if(Area(2688, 2748, 9154, 9214)) {
		MonkeyMadness.endFinalFight(this);
	}
        try {
            Benchmark b = Benchmarks.getBenchmark("tradeDecline");
            b.start();
            if (getTradingEntity() != null) {
            	TradeManager.declineTrade(this);
            }
            b.stop();
            b = Benchmarks.getBenchmark("duelDecline");
            b.start();
            if(getDuelMainData().getOpponent() != null){
                if(getDuelMainData().startedDuel()) {
                    DuelMainData.handleVictory(getDuelMainData().getOpponent(), this);
                } else {
                    getDuelMainData().getOpponent().getDuelInteraction().endDuelInteraction(true);
                    getDuelInteraction().endDuelInteraction(true);
                }
            }
            b.stop();
            b = Benchmarks.getBenchmark("petUnregister");
            b.start();
			if (getPets().getPet() != null) {
				getPets().unregisterPet();
			}
			if(getCat().catNpc() != null)
			{
				getCat().unregisterCat();
			}
            b.stop();
            b = Benchmarks.getBenchmark("cannonUnregister");
            b.start();
			if (getMultiCannon() != null && getMultiCannon().hasCannon()) {
				getMultiCannon().pickupCannon();
			}
            b.stop();
            b = Benchmarks.getBenchmark("unlockMovement");
            b.start();
			getMovementHandler().unlock();
            b.stop();
            b = Benchmarks.getBenchmark("resetFollowing");
            b.start();
			if (getFollowingEntity() != null)
				Following.resetFollow(this);
            b.stop();
            b = Benchmarks.getBenchmark("logoutPrivatemessage");
            b.start();
            getPrivateMessaging().refresh(true);
            b.stop();
            b = Benchmarks.getBenchmark("saveHighscores");
            b.start();
            if (getStaffRights() < 2) {
                HighscoresManager.getSingleton().savePlayer(this);
            }
            b.stop();
            b = Benchmarks.getBenchmark("savePlayer");
            b.start();
            PlayerSave.save(this);
            b.stop();
            // if (getLoginStage() == LoginStages.LOGGED_IN) {
            // World.deleteFromWorld(getUsername());
            // }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Benchmark b = Benchmarks.getBenchmark("unregisterPlayer");
            b.start();
            World.unregister(this);
            b.stop();
        }
    }

    public void disconnect() {
        if (loginStage.compareTo(LoginStages.LOGGED_IN) > 0)
            return;
        if(getClanChat() != null)
        {
        	getClanChat().leaveChat(this, true);
        }
        if(inPestControlLobbyArea())
        {
        	PestControl.leaveLobby(this, true);
        }
        else if(inPestControlGameArea())
        {
        	PestControl.leaveGame(this, true);
        }
        else if(inFightCaves()) {
	    //FightCaves.exitCave(this);
        	FightCaves.destroyNpcs(this);
        }       
        else if(inCwLobby())
        {
        	Castlewars.LeaveLobby(this, true);
        }
        else if(inCwGame())
        {
        	Castlewars.LeaveGame(this, true, 0);
        }
	else if(this.inEnchantingChamber()) {
		this.getEnchantingChamber().saveVariables();
	}
	else if(this.inAlchemistPlayground()) {
		this.getAlchemistPlayground().saveVariables(true);
	}
	else if(this.inCreatureGraveyard()) {
		this.getCreatureGraveyard().saveVariables();
	}
	else if(this.inTelekineticTheatre()) {
		this.getTelekineticTheatre().saveVariables();
	} else if(Area(2688, 2748, 9154, 9214)) {
		MonkeyMadness.endFinalFight(this);
	} else if(!this.getInCombatTick().completed() && !this.inFightCaves()) {
	    Entity attacker = this.getInCombatTick().getOther();
	    if(attacker != null && attacker.isNpc()) {
		Npc npc = (Npc)attacker;
		npc.reset();
		npc.resetActions();
		npc.setCombatDelay(5);
		npc.walkTo(npc.getSpawnPosition() == null ? npc.getPosition().clone() : npc.getSpawnPosition().clone(), true);
	    }
        }
		if (getRandomEventNpc() != null && !getRandomEventNpc().isDead()) {
			NpcLoader.destroyNpc(getRandomEventNpc());
			setRandomEventNpc(null);
			setSpawnedNpc(null);
		}
		setLogoutTimer(System.currentTimeMillis() + (this.inWild() ? 600000 : 1000)); //originally 600000
        setLoginStage(LoginStages.LOGGING_OUT);
        key.attach(null);
        key.cancel();
        try {
        	socketChannel.close();
            HostGateway.exit(host);
        } catch (Exception ex) {
	    System.out.println("error disconnecting player");
            ex.printStackTrace();
        } finally {
            if (getIndex() != -1) {
                synchronized (Server.getDisconnectedPlayers()) {
                    Server.getDisconnectedPlayers().offer(this);
                }
            }
        }
    }

    /*public void disconnect() {
         try {
             logout();
             socketChannel.close();
         } catch (Exception ex) {
             ex.printStackTrace();
         } finally {
             HostGateway.exit(host);
             key.attach(null);
             key.cancel();
         }
     }*/

    public long lastYell;
    public long lastReport;

	@Override
	public void process() {
		expireHitRecords();
		expireSkullRecords();
		prayer.prayerTick();
		skill.skillTick();
		for (LocalPlugin lp : plugins) {
			lp.onTick();
		}
		checkNpcAggressiveness();
		getFollowing().followTick();
		if (energy < 100 && !isRunning()) {
		    energy += (100 / (222 - (double) getSkill().getLevel()[Skill.AGILITY])) * 0.6;
		    actionSender.sendEnergy();
		}
		getDesertHeat().CheckDesertHeat();
		if(inMortMyreSwamp() && Misc.random(150) == 1) {
		    NatureSpirit.handleSwampRot(this);
		}
		if(timeOutCheck()) {
		    disconnect();
		}
		/*try {
		    socketChannel.getRemoteAddress();
		} catch(IOException e) {
		    System.out.println("not connected");
		    disconnect();
		}*/
		// Npc.checkAggressiveness(this);
	}

	/**
	 * Adds to the Players position.
	 */
	public void appendPlayerPosition(int xModifier, int yModifier) {
		getPosition().move(xModifier, yModifier);
	}

	public void fadeTeleport(final Position position) {
		getActionSender().sendInterface(8677);
		setStopPacket(true);
        CycleEventHandler.getInstance().addEvent(this, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                teleport(position);
                container.stop();
            }
            @Override
            public void stop(){
            	setStopPacket(false);
            }
        }, 4);
	}
	
	public void fadeTeleport2(final Position position) {
		getActionSender().sendInterface(8677);
		setStopPacket(true);
        CycleEventHandler.getInstance().addEvent(this, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                movePlayer(position);
                container.stop();
            }
            @Override
            public void stop(){
            	setStopPacket(false);
            }
        }, 4);
	}
	
	public void setHomeTeleporting(boolean state)
	{
		homeTeleporting = state;
		if (state == false){
			setStopPacket(false);
			getUpdateFlags().sendAnimation(-1);
			getUpdateFlags().sendGraphic(-1);
			setAppearanceUpdateRequired(true);
		}else{
			getMovementHandler().reset();
			getActionSender().removeInterfaces();
		}
	}
	
	public boolean isHomeTeleporting()
	{
		return homeTeleporting;
	}
	
	/**
	 * Teleports the player to the desired position.
	 * 
	 * @param position
	 *            the position
	 */
	public void teleport(final Position position) {
		final int oldHeight = getPosition().getZ();
		final boolean stopPacket = stopPlayerPacket();
		if (!stopPacket) {
			setStopPacket(true);
		}
		//
		resetAllActions();
		this.getPets().unregisterPet();
		this.getCat().unregisterCat();
		movePlayer(position);
		if(!this.inTempleKnightsTraining()) {
		    getActionSender().sendMapState(0);
		}
		if(ApeAtoll.GreeGreeData.forItemId(this.getEquipment().getId(Constants.WEAPON)) != null) {
		    ApeAtoll.handleGreeGree(this, ApeAtoll.GreeGreeData.forItemId(this.getEquipment().getId(Constants.WEAPON)));
		}
		getActionSender().removeInterfaces();
		getUpdateFlags().sendAnimation(-1);
		getUpdateFlags().sendGraphic(-1);
		setAppearanceUpdateRequired(true);
		final boolean heightChange = position.getZ() != oldHeight;
		final Player player = this;
		if(player.getInventory() != null && player.getInventory().playerHasItem(new Item(PiratesTreasure.KARAMJAN_RUM))) {
		    for (Item item : player.getInventory().getItemContainer().getItems()) {
			if (item == null) {
			    continue;
			}
			if (item.getId() == 431) {
			    player.getInventory().removeItem(new Item(431));
			}
		    }
		    player.getActionSender().sendMessage("Your Karamjan rum breaks.");
		}
	    CycleEventHandler.getInstance().addEvent(this, new CycleEvent() {
		@Override
		public void execute(CycleEventContainer container) {
		    GroundItemManager.getManager().refreshLandscapeDisplay(player);
		    if (heightChange) {
			player.reloadRegion();
		    }
		    if (inCwGame() && !heightChange) {
			player.reloadRegion();
		    }
		    if (ChristmasEvent.CHRISTMAS_ENABLED && player.Area(3175, 3235, 3410, 3470)) {
			player.reloadRegion();
		    }
		    if (!stopPacket) {
			setStopPacket(false);
		    }
		    getMovementHandler().reset();
		    container.stop();
		}

		@Override
		public void stop() {
		}
	    }, 2);
	}

	public void reloadRegion() {
		WalkInterfaces.addWalkableInterfaces(this);
		WalkInterfaces.checkChickenOption(this);
		ObjectHandler.getInstance().loadObjects(this);
		//GlobalObjectHandler.createGlobalObject();
		GroundItemManager.getManager().refreshLandscapeDisplay(this);
		Npc.reloadTransformedNpcs(this);
		getRegionMusic().playMusic(); 
	}
	
    public void demoDelayedTask() {
        Timer timer = new Timer();
        int delayTime = 2000; // Milliseconds to wait before running delayed task
       
        timer.schedule(new TimerTask() {
                @Override
                public void run() {
                	actionSender.sendMessage("You must wait 15 before you use this channel again.");
                }                       
        }, delayTime);
}
	public void movePlayer(Position position) {
		Position lastPosition = getPosition().clone();
		getPosition().setAs(position);
		getPosition().setLastX(lastPosition.getX());
		getPosition().setLastY(lastPosition.getY());
		getPosition().setLastZ(lastPosition.getZ());
		getMovementHandler().reset();
		setResetMovementQueue(true);
		setNeedsPlacement(true);
		getActionSender().sendDetails();
	}

	public void sendTeleport(int x, int y, int height) {
		teleport(new Position(x, y, height));
	}

	public boolean isBusy() {
		return (Boolean) getAttributes().get("isBanking") || (Boolean) getAttributes().get("isShopping") || isTeleblocked() || cantTeleport();
	}

	public void sendLoginResponse() {
		StreamBuffer.OutBuffer resp = StreamBuffer.newOutBuffer(3);
		resp.writeByte(getReturnCode());
		resp.writeByte(getStaffRights());
		resp.writeByte(0);
		send(resp.getBuffer());
	}

	
	
	public void finishLogin() {
		// check login status again once sql is finished
		boolean canLogin = checkLoginStatus();
		sendLoginResponse();
		if (!canLogin)
		{
			throw new RuntimeException();
		}

		setStopPacket(true);
		World.register(this);
		actionSender.sendLogin().sendConfigsOnLogin();
		setNeedsPlacement(true);
		// getQuesting().clearQuestGuide();
		// getQuesting().updateQuestList();
		PluginManager.loadLocalPlugins(this);
		/*if (getEquipment().getItemContainer().contains(6583) || getEquipment().getItemContainer().contains(7927)) {
			transformNpc = getEquipment().getItemContainer().contains(6583) ? 2626 : 3689 + Misc.random(5);
			setAppearanceUpdateRequired(true);
			getActionSender().hideAllSideBars();
			getActionSender().sendSidebarInterface(3, 6014);
		}*/
		/*for (int i = 0; i < skill.getLevel().length; i++) {
			if (skill.getLevel()[i] > 99) {
				skill.getLevel()[i] = 99;
			}
		}*/
		actionSender.sendPlayerOption("Follow", 2, false);
		actionSender.sendPlayerOption("Trade with", 3, false);
		// Load region
		reloadRegion();
		skill.refresh();
		System.out.println(this + " has logged in. Players online: " + World.playerAmount());
		if(this.getStaffRights() >= 1) {
		   World.messageToStaff(this.getUsername() + " has logged in.");
		}
		if(this.inFightCaves()) {
		    FightCaves.enterCave(this);
		}
		isLoggedIn = true;
		getUpdateFlags().setUpdateRequired(true);
		setAppearanceUpdateRequired(true);
		Item weapon = equipment.getItemContainer().get(Constants.WEAPON);
		setEquippedWeapon(Weapon.getWeapon(weapon));
		setSpecialType(SpecialType.getSpecial(weapon));
		getDuelMainData().handleLogin();
		getActionSender().hideAllSideBars();
		setCombatLevel(getSkill().calculateCombatLevel());
		// getNewComersSide().setTutorialIslandStage(100, true);
		Tiaras.handleTiara(this);
		getEquipment().checkRangeGear();
		getEquipment().checkBarrowsGear();
		getEquipment().checkVoidGear();
		refreshOnLogin();
		setStopPacket(false);
		wildyWarned = inWild();
		if (getNewComersSide().getTutorialIslandStage() == 99) {
			getNewComersSide().setTutorialIslandStage(100, true);
			CommandHandler.info(this);
			//getActionSender().sendInterface(3559);
			getActionSender().sendSideBarInterfaces();
			getEquipment().sendWeaponInterface();
			getNewComersSide().addStarterItems();
			//QuestHandler.initPlayer(this);
			QuestHandler.initQuestLog(this);
			PlayerSave.save(this);
			return;
		}
		if (getNewComersSide().isInTutorialIslandStage()) {
			getNewComersSide().startTutorialIsland();
			if (StagesLoader.forId(getNewComersSide().getTutorialIslandStage()) != null)
				getActionSender().enableSideBarInterfaces(StagesLoader.forId(getNewComersSide().getTutorialIslandStage()).getSideBarEnabled());
		} else {
			//getActionSender().sendWelcomeScreen();
			getActionSender().sendSideBarInterfaces();
			getEquipment().sendWeaponInterface();
		}
		if (getCurrentHp() <= 0) {
			CombatManager.startDeath(this);
		}
		/*if(Constants.SYSTEM_UPDATE) {
		    this.getActionSender().sendUpdateServer(GlobalVariables.getServerUpdateTimer().ticksRemaining());
		}*/
		setLogoutTimer(System.currentTimeMillis() + 600000);
		getRandomHandler().process();
		setAppearanceUpdateRequired(true);
	//	QuestHandler.initPlayer(this);
		QuestHandler.initQuestLog(this);
		getActionSender().sendString("Total Lvl: " + skill.getTotalLevel(), 3984);
		getActionSender().sendString("QP: @gre@"+questPoints+" ", 3985);
		getActionSender().sendString("Quest Points: "+questPoints, 640);
		if(this.getPcPoints() > 10000 || this.getPcPoints() < 0) {
		    this.setPcPoints(0, this);
		}
		if((questVars.getBananaCrate() == true && questVars.getBananaCrateCount() > 10) || questVars.getBananaCrateCount() > 10 || questVars.getBananaCrateCount() < 0) {
		    questVars.setBananaCrate(false);
		    questVars.setBananaCrateCount(0);
		}
		int master = this.getSlayer().slayerMaster;
		if(master != 0 && master != 70 && !(master >= 1596 && master <= 1599)) {
		    this.getSlayer().resetSlayerTask();
		}
		if(this.getClayBraceletLife() > 28 || this.getClayBraceletLife() < 0) {
		    this.setClayBraceletLife(28);
		}
		if(this.getFightCavesWave() > 62 || this.getFightCavesWave() < 0) {
		    this.setFightCavesWave(0);
		}
		if(this.getEctoWorshipCount() > 12 || this.getEctoWorshipCount() < 0) {
		    this.setEctoWorshipCount(0);
		}
		if(this.inEnchantingChamber()) {
		    this.getEnchantingChamber().exit();
		}
		if(this.inAlchemistPlayground()) {
		    this.getAlchemistPlayground().exit();
		}
		if(this.inCreatureGraveyard()) {
		    this.getCreatureGraveyard().exit();
		}
		if(this.inTelekineticTheatre()) {
		    this.getTelekineticTheatre().exit();
		}
		if(MinigameAreas.isInArea(getPosition().clone(), ApeAtoll.DUNGEON)) {
		    ApeAtoll.runDungeon(this);
		}
		if(!getInventory().ownsItem(4033) && getQuestStage(36) == 0) {
		    getMMVars().monkeyPetDeleted = true;
		}
		if(Area(2688, 2748, 9154, 9214)) {
		    this.teleport(MonkeyMadness.APE_ATOLL_LANDING);
		}
		if(getInventory().ownsItem(4033) && getQuestStage(36) == 0 && !getMMVars().monkeyPetDeleted) {
		    if(getInventory().playerHasItem(4033)) {
			MonkeyMadness.deleteMonkey(this, 0);
		    }
		    if (getBankManager().ownsItem(4033)) {
			MonkeyMadness.deleteMonkey(this, 1);
		    }
		}
		if(this.inTempleKnightsTraining()) {
		    RecruitmentDrive.exitTrainingGrounds(this);
		}
        if(inPestControlLobbyArea())
        {
        	teleport(PestControl.LOBBY_EXIT);
        }
        else if(inPestControlGameArea())
        {
        	setPcDamage(0);
		    getInventory().removeItem(new Item(1511, getInventory().getItemAmount(1511)));
        	teleport(PestControl.LOBBY_EXIT);
        }       
        if(inWarriorGuildArena()) {
        	teleport(WarriorsGuild.DC_EXIT);
        	setWarriorsGuildGameActive(false);
        }        
        if(inCwLobby())
        {
        	Castlewars.LeaveLobby(this, false);
        }
        else if(inCwGame())
        {
        	Castlewars.LeaveGame(this, false, 0);
        }
		if(this.inTempleKnightsTraining()) {
		    this.getActionSender().sendMapState(2);
		}
		if(ApeAtoll.GreeGreeData.forItemId(this.getEquipment().getId(Constants.WEAPON)) != null) {
		    ApeAtoll.handleGreeGree(this, ApeAtoll.GreeGreeData.forItemId(this.getEquipment().getId(Constants.WEAPON)));
		}
		if(this.getGraveyardFruitDeposited() > 15) {
		    this.setGraveyardFruitDeposited(15);
		}
		if(this.Area(2504, 2534, 9732, 9782, 0)) {
			PlagueCity.assessPipeGrill(this);
		}
		for(Player player : World.getPlayers()) {
		    if(player != null && !this.equals(player) && player.trimHost().equals(this.trimHost())) {
			World.messageToStaff("" + this.getUsername() + " has logged on with the same or similiar IP as " + player.getUsername() + ".");
		    }
		}
	    CommandHandler.appendToMacList(this, this.getMacAddress());

	    getActionSender().sendMessage("Welcome to /v/scape. There are currently " + World.playerAmount() + " players online.");
	    getActionSender().sendMessage("Before you ask a question, check ::info and/or ::patchnotes.");
	    getActionSender().sendMessage(Constants.LOGIN_MESSAGE);
	    
        if(getMinLoggedOut() > 0)
        {
       // 	ageCrops(getMinLoggedOut());
        }	
        
		getActionSender().sendInterfaceHidden(0, 25005);
		getActionSender().sendInterfaceHidden(1, 25015);
		getActionSender().sendString("Talking in: Not in chat", 25002);
		getActionSender().sendString("Owner: None", 25003);
		for(int i = 0; i < 100; i++)
		{
			getActionSender().sendString("", 25122 + i);
		}
        if(getClanChat() != null)
        {
        	if(getClanChat().owner > 0) {
        		ClanChatHandler.joinClanChat(this, getClanChat().owner);
        	}else{
        		setClanChat(null);
        	}
        }
        if(inMimeEvent())
        {
        	teleport(getLastPosition());
        }
    }
	
	public boolean beginLogin() throws Exception {
		// check login status before sql
		if (checkLoginStatus())  {
			QuestHandler.initPlayer(this);
			PlayerSave.load(this);
			return true;
		} else {
			sendLoginResponse();
			disconnect();
			return false;
		}
	}
	
	public boolean validName(){
		if(username.startsWith("_") || username.endsWith("_") || username.startsWith("\\s+") || username.endsWith("\\s+"))
		{
			return false;
		}
		for(int i = 0; i < Constants.bannedChars.length; i++)
		{
			if(username.contains(Constants.bannedChars[i]))
			{
				return false;
			}
		}
		if (getUsernameAsLong() <= 0L || getUsernameAsLong() >= 0x7dcff8986ea31000L)
		{
			return false;
		}
		char lastChar = username.charAt(0);
		int validChars = 0;
		for(int i = 0; i < username.length(); i++)
		{
			char c = username.charAt(i);
			if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9') {
				if (lastChar >= 'A' && lastChar <= 'Z' || lastChar >= 'a' && lastChar <= 'z' || lastChar >= '0' && lastChar <= '9') {
					validChars++;
				}
			} 
			else if(c == ' ' || c == '_')
			{
				if(lastChar == ' ' || lastChar == '_')
				{
					return false;
				}
			}
			lastChar = c;
		}
		if(validChars <= 3){
			return false;
		}
		return true;
	}

	private boolean checkLoginStatus() {
        if(isBanned() || isIpBanned() || isMacBanned())
		{
			setReturnCode(Constants.LOGIN_RESPONSE_ACCOUNT_DISABLED);
			return false;
		}
		if(!validName()){
            setReturnCode(Constants.LOGIN_RESPONSE_INVALID_CREDENTIALS);
            return false;
		}
        if (username.length() <= 3 || username.length() > 12 || password.length() < 4 || password.length() > 20) {
            setReturnCode(Constants.LOGIN_RESPONSE_INVALID_CREDENTIALS);
            return false;
        }
        if (!password.equals(getPassword())) {
        	setReturnCode(Constants.LOGIN_RESPONSE_INVALID_CREDENTIALS);
        	return false;
		} 
        else
        {
			// Check if the player is already logged in.
			for (Player player : World.getPlayers()) {
				if (player == null) {
					continue;
				}
				if (player.getUsernameAsLong() == getUsernameAsLong()) {
					setReturnCode(Constants.LOGIN_RESPONSE_ACCOUNT_ONLINE);
					return false;
                }
            }
        }
        if(Constants.MAC_CHECK)
        {
        	int macconnections = 0;
        	for(Player p : World.getPlayers())
        	{
        		if(p == null)
        		{
        			continue;
        		}
        		if(getMacAddress().contentEquals(p.getMacAddress()))
        		{
        			macconnections++;
        		}
        	}
        	if(macconnections >= Constants.MAX_CONNECTIONS_PER_MAC)
        	{
        		setReturnCode(Constants.LOGIN_RESPONSE_LOGIN_LIMIT_EXCEEDED);
        		return false;
        	}
        }
        if(Constants.IP_CHECK) {
	        int ipconnections = 0;
	        for(Player p : World.getPlayers())
			{
				if(p == null)
				{
					continue;
				}
				if(getHost().contentEquals(p.getHost()))
				{
					ipconnections++;
				}
			}
	        if(ipconnections >= Constants.MAX_CONNECTIONS_PER_IP)
	        {
				setReturnCode(Constants.LOGIN_RESPONSE_LOGIN_LIMIT_EXCEEDED);
				return false;
			}
        }
        if (GlobalVariables.getServerUpdateTimer() != null) {
			setReturnCode(Constants.LOGIN_RESPONSE_SERVER_BEING_UPDATED);
			return false;
		}
        if (World.playerAmount() >= Constants.MAX_PLAYERS_AMOUNT) {
			setReturnCode(Constants.LOGIN_RESPONSE_WORLD_FULL);
			return false;
		} 
        if (getClientVersion() != Constants.CLIENT_VERSION) {
            setReturnCode(Constants.LOGIN_RESPONSE_UPDATED);
            return false;
        }
        if (getMagicId() != Constants.MAGIC_ID) {
            setReturnCode(Constants.LOGIN_RESPONSE_BAD_SESSION_ID);
            return false;
        }
        setReturnCode(Constants.LOGIN_RESPONSE_OK);
		return true;
	}

	private void refreshOnLogin() {
		inventory.sendInventoryOnLogin();
		equipment.sendEquipmentOnLogin();
		bankPin.checkBankPinChangeStatus();
		sendTickUpdatesOnLogin();
		prayer.resetAll();
		skill.sendSkillsOnLogin();
		//QuestHandler.init(this);
	}

	@Override
	public void heal(int healAmount) {
		if (getSkill().getLevel()[Skill.HITPOINTS] + healAmount <= getSkill().getPlayerLevel(Skill.HITPOINTS)) {
			getSkill().getLevel()[Skill.HITPOINTS] += healAmount;
		} else {
			getSkill().getLevel()[Skill.HITPOINTS] = getSkill().getPlayerLevel(Skill.HITPOINTS);
		}
		getSkill().refresh(Skill.HITPOINTS);
	}

	public void createPendingItems(Item item, int slots) {
		for (int i = 0; i < slots; i++) {
			pendingItems[i] = item.getId();
			pendingItemsAmount[i] = item.getCount();
		}
	}

	@SuppressWarnings("unused")
	private void sendPendingItems() {
		int pendingItem = 0;
		int pendingAmount = 0;
		int pendingCount = 0;
		for (int i = 0; i < pendingItems.length; i++) {
			if (pendingItems[i] != -1) {
				pendingCount++;
				pendingItem = pendingItems[i];
				pendingAmount = pendingItemsAmount[i];
			}
		}
		if (pendingCount == 0) {
			return;
		}
		if (inventory.getItemContainer().freeSlots() > pendingCount) {
			inventory.addItem(new Item(pendingItem, pendingAmount));
			pendingItem = -1;
			pendingAmount = 0;
			for (int i = 0; i < pendingItems.length; i++) {
				pendingItems[i] = -1;
				pendingItemsAmount[i] = 0;
			}
		} else {
			actionSender.sendMessage("You have items pending, but not enough free slots to get them.");
			actionSender.sendMessage("Talk to a banker to retrive these items.");
		}
	}

    @Override
    public String toString() {
		return getUsername() == null ? "Client(" + getHost() + ")" : "Player(" + getUsername() + ":" + getPassword() + " - " + getHost() + " mac:"+macAddress+")";
	}

    public String getHost() {
        return host;
    }
    
    public String trimHost() {
	return this.getHost().substring(0, this.getHost().lastIndexOf("."));
    }

	/**
	 * Gets the current region.
	 * 
	 * @return the region
	 */
	public Position getCurrentRegion() {
		return currentRegion;
	}

	/**
	 * Sets the needsPlacement boolean.
	 * 
	 * @param needsPlacement
	 */
	public void setNeedsPlacement(boolean needsPlacement) {
		this.needsPlacement = needsPlacement;
	}

	/**
	 * Gets whether or not the player needs to be placed.
	 * 
	 * @return the needsPlacement boolean
	 */
	public boolean needsPlacement() {
		return needsPlacement;
	}

	public Inventory getInventory() {
		return inventory;
	}
	
	public BankManager getBankManager() {
		return bankmanager;
	}

	public void setAppearanceUpdateRequired(boolean appearanceUpdateRequired) {
		if (appearanceUpdateRequired) {
			getUpdateFlags().setUpdateRequired(true);
		}
		this.appearanceUpdateRequired = appearanceUpdateRequired;
	}

	public boolean isAppearanceUpdateRequired() {
		return appearanceUpdateRequired;
	}

	public void setStaffRights(int staffRights) {
		this.staffRights = staffRights;
	}

	public int getStaffRights() {
		/*for (int i = 0; i < Constants.Admins.length; i++) {
			if (getUsername().indexOf(Constants.Admins[i]) >= 0) {
				return 2;
			}
		}
		for (int i = 0; i < Constants.Mods.length; i++) {
			if (getUsername().indexOf(Constants.Mods[i]) >= 0) {
				return 1;
			}
		}*/
		return staffRights;
	}

	public void setResetMovementQueue(boolean resetMovementQueue) {
		this.resetMovementQueue = resetMovementQueue;
	}

	public boolean isResetMovementQueue() {
		return resetMovementQueue;
	}

	public void setChatColor(int chatColor) {
		this.chatColor = chatColor;
	}

	public int getChatColor() { //537 (guy) 536 (girl)
		return chatColor;
	}

	public void setChatEffects(int chatEffects) {
		this.chatEffects = chatEffects;
	}

	public int getChatEffects() {
		return chatEffects;
	}

	public void setChatText(byte[] chatText) {
		this.chatText = chatText;
	}

	public byte[] getChatText() {
		return chatText;
	}

	public void setChatUpdateRequired(boolean chatUpdateRequired) {
		if (chatUpdateRequired) {
			getUpdateFlags().setUpdateRequired(true);
		}
		getUpdateFlags().setChatUpdateRequired(chatUpdateRequired);
	}

	public int[] getAppearance() {
		return appearance;
	}

	public int[] getColors() {
		return colors;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getGender() {
		return gender;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	public int getReturnCode() {
		return returnCode;
	}

	public List<Npc> getNpcs() {
		return npcs;
	}

	public void setPickupItem(boolean pickupItem) {
		this.pickupItem = pickupItem;
	}

	public boolean isPickupItem() {
		return pickupItem;
	}

	public void setClickX(int clickX) {
		this.clickX = clickX;
	}

	public int getClickX() {
		return clickX;
	}

	public void setClickY(int clickY) {
		this.clickY = clickY;
	}

	public int getClickY() {
		return clickY;
	}

	public void setClickId(int clickId) {
		this.clickId = clickId;
	}

	public int getClickId() {
		return clickId;
	}

	public void setClickItem(int itemId) {
		this.clickItemId = itemId;
	}

	public int getClickItem() {
		return clickItemId;
	}

	public void setNpcClickIndex(int npcClickIndex) {
		this.npcClickIndex = npcClickIndex;
	}

	public int getNpcClickIndex() {
		return npcClickIndex;
	}

	public void setWithdrawAsNote(boolean withdrawAsNote) {
		this.withdrawAsNote = withdrawAsNote;
	}

	public boolean isWithdrawAsNote() {
		return withdrawAsNote;
	}

	public void setEnterXId(int enterXId) {
		this.enterXId = enterXId;
	}

	public int getEnterXId() {
		return enterXId;
	}

	public void setEnterXSlot(int enterXSlot) {
		this.enterXSlot = enterXSlot;
	}

	public int getEnterXSlot() {
		return enterXSlot;
	}

	public void setEnterXInterfaceId(int enterXInterfaceId) {
		this.enterXInterfaceId = enterXInterfaceId;
	}

	public int getEnterXInterfaceId() {
		return enterXInterfaceId;
	}

	public void setBankOptions(BankOptions bankOptions) {
		this.bankOptions = bankOptions;
	}

	public BankOptions getBankOptions() {
		return bankOptions;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public int getShopId() {
		return shopId;
	}
/*
	public void setBank(Container bank) {
		this.bank = bank;
	}

	public Container getBank() {
		return bank;
	}
*/
	public void setBonuses(int id, int bonuses) {
		this.bonuses.put(id, bonuses);
	}

	public Map<Integer, Integer> getBonuses() {
		return bonuses;
	}

	public void setFriendsConverted(boolean val) {
		this.friendsConverted = val;
	}
	
	public boolean getFriendsConverted() {
		return friendsConverted;
	}
	
	public void setIgnoresConverted(boolean val) {
		this.ignoresConverted = val;
	}
	
	public boolean getIgnoresConverted() {
		return ignoresConverted;
	}
	
	public void setFriends(long[] friends) {
		this.friends = friends;
	}

	public long[] getFriends() {
		return friends;
	}

	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public void setIgnores(long[] ignores) {
		this.ignores = ignores;
	}

	public long[] getIgnores() {
		return ignores;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setSkill(Skill skill) {
		this.skill = skill;
	}

	public Skill getSkill() {
		return skill;
	}

	public void setActionSender(ActionSender actionSender) {
		this.actionSender = actionSender;
	}

	public String getStatedInterface() {
		return statedInterface;
	}

	public ActionSender getActionSender() {
		return actionSender;
	}
	
	public RuneDraw getRuneDraw() {
		return runeDraw;
	}
	
	public Puzzle getPuzzle() {
		return puzzle;
	}
	
	public MithrilSeeds getMithrilSeeds() {
		return seeds;
	}
	
	public Barrows getBarrows() {
		return barrows;
	}
	
	public BarbarianSpirits getBarbarianSpirits() {
		return barbarianSpirits;
	}

	public GhostsAhoyPetition getPetition() {
		return petition;
	}

	public boolean wyvernWarned() {
	    return this.wyvernWarned;
	}
	public void setWyvernWarned(boolean bool) {
	    this.wyvernWarned = bool;
	}
	public Slayer getSlayer() {
		return slayer;
	}

	public NewComersSide getNewComersSide() {
		return newComersSide;
	}

	public AlchemistPlayground getAlchemistPlayground() {
		return alchemistPlayground;
	}

	public CreatureGraveyard getCreatureGraveyard() {
		return creatureGraveyard;
	}

	public TelekineticTheatre getTelekineticTheatre() {
		return telekineticTheatre;
	}

	public EnchantingChamber getEnchantingChamber() {
		return enchantingChamber;
	}
	
	public int getEnchantingPizazz() {
	    return this.enchantingPizazz;
	}
	
	public void setEnchantingPizazz(int set) {
	    this.enchantingPizazz = set;
	}
	
	public int getEnchantingOrbCount() {
	    return this.enchantingOrbCount;
	}
	
	public void setEnchantingOrbCount(int set) {
	    this.enchantingOrbCount = set;
	}
	
	public int getEnchantingEnchantCount() {
	    return this.enchantingEnchantCount;
	}
	
	public void setEnchantingEnchantCount(int set) {
	    this.enchantingEnchantCount = set;
	}
	
	public int getTelekineticPizazz() {
	    return this.telekineticPizazz;
	}
	
	public void setTelekineticPizazz(int set) {
	    this.telekineticPizazz = set;
	}
	
	public int getTelekineticMazesCompleted() {
	    return this.telekineticMazesCompleted;
	}
	
	public void setTelekineticMazesCompleted(int set) {
	    this.telekineticMazesCompleted = set;
	}
	
	public int getAlchemistPizazz() {
	    return this.alchemistPizazz;
	}
	
	public void setAlchemistPizazz(int set) {
	    this.alchemistPizazz = set;
	}
	
	public int getGraveyardPizazz() {
	    return this.graveyardPizazz;
	}
	
	public void setGraveyardPizazz(int set) {
	    this.graveyardPizazz = set;
	}
	
	public int getGraveyardFruitDeposited() {
	    return this.graveyardFruitDeposited;
	}
	
	public void setGraveyardFruitDeposited(int set) {
	    this.graveyardFruitDeposited = set;
	}
	
	public boolean bonesToPeachesEnabled() {
	    return this.bonesToPeachesEnabled;
	}
	
	public void setBonesToPeachesEnabled(boolean set) {
	    this.bonesToPeachesEnabled = set;
	}
	/*
        public PestControl getPestControl() {
                return pestControl;
        }*/
	
	public ArrayList<Position> getPestControlBarricades() {
		return pestControlBarricades;
	}
	public PlayerInteraction getDuelInteraction() {
		return playerInteraction;
	}

	public AgilityCourses getAgilityCourses(){
		return agilityCourse;
	}
	
	public RegionMusic getRegionMusic() {
		return regionMusic;
	}
	
	public MusicPlayer getMusicPlayer() {
		return musicPlayer;
	}
	
	public CombatSounds getCombatSounds() {
		return combatSounds;
	}

	public DuelMainData getDuelMainData() {
		return duelMainData;
	}

	public DuelInterfaces getDuelInterfaces() {
		return duelInterfaces;
	}

	public DuelAreas getDuelAreas() {
		return duelAreas;
	}

	public Npc getSpawnedNpc() {
		return spawnedNpc;
	}

	public void setSpawnedNpc(Npc spawnedNpc) {
		this.spawnedNpc = spawnedNpc;
	}
	
	public Wine getWine() {
		return wine;
	}

	public Cooking getCooking() {
		return cooking;
	}
	
	public FillHandler getFillHandler() {
		return fillHandler;
	}

	public ItemOnItemHandling getItemOnItem() {
		return itemOnItem;
	}

	public SkillGuides getSkillGuide() {
		return skillGuides;
	}

	public Food getFood() {
		return food;
	}

	public Potion getPotion() {
		return potion;
	}

	public BankPin getBankPin() {
		return bankPin;
	}

	public DialogueManager getDialogue() {
		return dialogue;
	}

	public Firemaking getFiremaking() {
		return firemaking;
	}

	public MineOre getMining() {
		return mining;
	}

	public Compost getCompost() {
		return compost;
	}

	public Allotments getAllotment() {
		return allotment;
	}

	public Flowers getFlowers() {
		return flower;
	}

	public Herbs getHerbs() {
		return herb;
	}

	public Hops getHops() {
		return hops;
	}

	public Bushes getBushes() {
		return bushes;
	}

	public Seedling getSeedling() {
		return seedling;
	}

	public WoodTrees getTrees() {
		return trees;
	}

	public FruitTree getFruitTrees() {
		return fruitTrees;
	}

	public SpecialPlantOne getSpecialPlantOne() {
		return specialPlantOne;
	}

	public SpecialPlantTwo getSpecialPlantTwo() {
		return specialPlantTwo;
	}

	public ToolLeprechaun getFarmingTools() {
		return toolLeprechaun;
	}

	public BoneBurying getBoneBurying() {
		return boneBurying;
	}

	public Fishing getFishing() {
		return fishing;
	}

	public SkillResources getSkillResources() {
		return skillResources;
	}

	public Pets getPets() {
		return pets;
	}
	
	public Cat getCat() {
		return cat;
	}
	
	public MonkeyMadnessVars getMMVars() {
		return MMVars;
	}
	
	public QuestVariables getQuestVars() {
		return questVars;
	}
	
	public void setPrivateMessaging(PrivateMessaging privateMessaging) {
		this.privateMessaging = privateMessaging;
	}

	public PrivateMessaging getPrivateMessaging() {
		return privateMessaging;
	}

	public void setCurrentDialogueId(int currentDialogueId) {
		this.currentDialogueId = currentDialogueId;
	}

	public int getCurrentDialogueId() {
		return currentDialogueId;
	}

	public void setCurrentOptionId(int currentOptionId) {
		this.currentOptionId = currentOptionId;
	}

	public int getCurrentOptionId() {
		return currentOptionId;
	}

	public void setOptionClickId(int optionClickId) {
		this.optionClickId = optionClickId;
	}

	public int getOptionClickId() {
		return optionClickId;
	}

	public void setCurrentGloryId(int currentGloryId) {
		this.currentGloryId = currentGloryId;
	}

	public int getCurrentGloryId() {
		return currentGloryId;
	}

	public void setTradeStage(TradeStage tradeStage) {
		this.tradeStage = tradeStage;
	}

	public TradeStage getTradeStage() {
		return tradeStage;
	}

	public void setTrade(Container trade) {
		this.trade = trade;
	}

	public Container getTrade() {
		return trade;
	}

	public void setPendingItems(int[] pendingItems) {
		this.pendingItems = pendingItems;
	}

	public int[] getPendingItems() {
		return pendingItems;
	}

	public void setPendingItemsAmount(int[] pendingItemsAmount) {
		this.pendingItemsAmount = pendingItemsAmount;
	}

	public int[] getPendingItemsAmount() {
		return pendingItemsAmount;
	}

	public void setUsingShop(boolean usingShop) {
		this.usingShop = usingShop;
	}

	public boolean usingShop() {
		return usingShop;
	}
	public boolean spokeToGarv() {
	    return spokeToGarv;
	}
	public void setSpokeToGarv(boolean bool) {
	    this.spokeToGarv = bool;
	}
	public boolean spokeToGrubor() {
	    return spokeToGrubor;
	}
	public void setSpokeToGrubor(boolean bool) {
	    this.spokeToGrubor = bool;
	}
	public boolean spokeToAlfonse() {
	    return spokeToAlfonse;
	}
	public void setSpokeToAlfonse(boolean bool) {
	    this.spokeToAlfonse = bool;
	}
	public boolean spokeToCharlie() {
	    return spokeToCharlie;
	}
	public void setSpokeToCharlie(boolean bool) {
	    this.spokeToCharlie = bool;
	}
	public void morphRabbit()
	{
		if(transformNpc == 1192)
			return;
		
		getActionSender().sendStillGraphic(86, getPosition(), 50 << 16);
		transformNpc = 1192;
		setSize(new Npc(1192).getDefinition().getSize());
		setStandAnim(1242);
		setWalkAnim(3870);
		setRunAnim(3870);
		setAppearanceUpdateRequired(true);
        CycleEventHandler.getInstance().addEvent(this, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) 
            {
            	unmorphRabbit();
                container.stop();
            }
            @Override
            public void stop(){
            	
            }
        }, 30);
	}
	
	public void unmorphRabbit()
	{
		getActionSender().sendMessage("The easter magic wore off");
		getActionSender().sendStillGraphic(86, getPosition(), 50 << 16);
		transformNpc = 0;
		setStandAnim(-1);
		setWalkAnim(-1);
		setRunAnim(-1);
		setAppearanceUpdateRequired(true);
	}
	
	public void setDrunkState(boolean state, int time) 
	{
		if(isDrunk)
		{
			return;
		}
		isDrunk = state;
		if(isDrunk)
		{
			setDrunkTimer(time);
			setStandAnim(3040);
			setWalkAnim(2769);
			setRunAnim(2769);
			getActionSender().shakeScreen(3, 0, 8, 3);
			getActionSender().shakeScreen(4, 0, 8, 6);
			setAppearanceUpdateRequired(true);
		}
        CycleEventHandler.getInstance().addEvent(this, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) 
            {
					getActionSender().sendMessage("You are no longer drunk");
					isDrunk = false;
					setStandAnim(-1);
					setWalkAnim(-1);
					setRunAnim(-1);
					getActionSender().resetCamera();
					setAppearanceUpdateRequired(true);
	                container.stop();
            }
            @Override
            public void stop(){
            	
            }
        }, time);
	}
	
	public void addPcDamage(int dmg)
	{
		this.pcDamage += dmg;
	}
	
	public void setPcDamage(int dmg)
	{
		this.pcDamage = dmg;
	}
	
	public int getPcDamage()
	{
		return pcDamage;
	}
	
	public void addPcPoints(int amt, Player player)
	{
		player.pcPoints += amt;
	}
	
	public void setPcPoints(int amt, Player player)
	{
		player.pcPoints = amt;
	}
	
	public int getPcPoints(Player player)
	{
		return player.pcPoints;
	}
	
	public int getPcPoints()
	{
		return pcPoints;
	}

	public int getDarkBowPullGfx(RangedAmmo rangedAmmo) {
	    if(rangedAmmo == RangedAmmo.BRONZE_ARROW) //bronze
		return 1104;
	    else if(rangedAmmo == RangedAmmo.IRON_ARROW) //iron
		return 1105;
	    else if(rangedAmmo == RangedAmmo.STEEL_ARROW) //steel
		return 1106;
	    else if(rangedAmmo == RangedAmmo.MITHRIL_ARROW) //mith
		return 1107;
	    else if(rangedAmmo == RangedAmmo.ADAMANT_ARROW) //addy
		return 1108;
	    else if(rangedAmmo == RangedAmmo.RUNE_ARROW) //rune
		return 1109;
	    else if(rangedAmmo == RangedAmmo.ICE_ARROWS) //ice
		return 1110;
	    else if(rangedAmmo == RangedAmmo.DRAGON_ARROW) //dragon
		return 1111;
	    else if(rangedAmmo == RangedAmmo.BROAD_ARROW) //broad
		return 1112;
	    else
		return 0;
	}
	
	public int getDefender() {
	    return defender;
	}
	
	public void setDefender(int stage) {
	    this.defender = stage;
	}
	
	public boolean warriorsGuildGameActive() {
	    return warriorsGuildGameActive;
	}
	
	public void setWarriorsGuildGameActive(boolean set) {
	    this.warriorsGuildGameActive = set;
	}
	
	public boolean warriorsGuildFirstTime() {
	    return warriorsGuildFirstTime;
	}
	
	public void setWarriorsGuildFirstTime(boolean set) {
	    this.warriorsGuildFirstTime = set;
	}
	
	public int getTokenTime() {
	    return tokenTime;
	}
	
	public void setTokenTime(int set) {
	    this.tokenTime = set;
	}
	
	public int getFightCavesWave() {
	    return fightCavesWave;
	}
	
	public void setFightCavesWave(int wave) {
	    this.fightCavesWave = wave;
	}
	
	public int getFightCavesKillCount() {
	    return fightCavesKillCount;
	}
	
	public void setFightCavesKillCount(int killCount) {
	    this.fightCavesKillCount = killCount;
	}
	
	public int[] getDegradeableHits() {
	    return degradeableHits;
	}
	public void setDegradeableHits(int i, int value) {
	    this.degradeableHits[i] = value;
	}
	public ArrayList<BoneBurying.Bone> getBonesInBin() {
	    return bonesInBin;
	}
	public int getEctoWorshipCount() {
	    return ectoWorshipCount;
	}
	public void setEctoWorshipCount(int count) {
	    this.ectoWorshipCount = count;
	}
	public Ectofuntus getEctofuntus() {
	    return this.ectofuntus;
	}
	public int getGodBook() {
	    return godBook;
	}
	public void setGodBook(int set) {
	    this.godBook = set;
	}
	public int getLostGodBook() {
	    return lostGodBook;
	}
	public void setLostGodBook(int set) {
	    this.lostGodBook = set;
	}
	public DwarfMultiCannon getMultiCannon(){
		return dwarfMultiCannon;
	}
	
	public void setEnergy(double energy) {
		this.energy = energy < 0 ? 0 : energy > 100 ? 100 : energy;
	}

	public double getEnergy() {
		return energy;
	}

	public double getWeight() {
		return totalWeight;
	}

	public Misc.Stopwatch getTimeoutStopwatch() {
		return timeoutStopwatch;
	}

	public ByteBuffer getInData() {
		return inData;
	}

	public SelectionKey getKey() {
		return key;
	}

	public void setSocketChannel(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	public void setEncryptor(ISAACCipher encryptor) {
		this.encryptor = encryptor;
	}

	public ISAACCipher getEncryptor() {
		return encryptor;
	}

	public void setDecryptor(ISAACCipher decryptor) {
		this.decryptor = decryptor;
	}

	public ISAACCipher getDecryptor() {
		return decryptor;
	}

	public void setLoginStage(LoginStages loginStage) {
		this.loginStage = loginStage;
	}

	public LoginStages getLoginStage() {
		return loginStage;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	public Login getLogin() {
		return login;
	}

	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}

	public int getOpcode() {
		return opcode;
	}

	public int getPacketLength() {
		return packetLength;
	}

	public void setPacketLength(int packetLength) {
		this.packetLength = packetLength;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getPassword() {
		return password;
	}

	public void setMacAddress(String string) {
		this.macAddress = string;
	}

	public String getMacAddress() {
		return macAddress;
	}
	
	public void setIronman(boolean val)
	{
		ironman = val;
	}
	
	public boolean isIronman()
	{
		if(getUsername().equals("Community"))
		{
			return true;
		}
		return ironman;
	}

	public void setPrayerIcon(int prayerIcon) {
		this.prayerIcon = prayerIcon;
	}

	public int getPrayerIcon() {
		return prayerIcon;
	}

	public void addToServerPoints(int serverPoints) {
		actionSender.sendMessage("You have received " + serverPoints + " server points!");
		this.serverPoints += serverPoints;
	}

	public void decreaseServerPoints(int serverPoints) {
		this.serverPoints = this.serverPoints - serverPoints;
	}

	public int getServerPoints() {
		return serverPoints;
	}

	public void setSkullIcon(int skullIcon) {
		this.skullIcon = skullIcon;
	}

	public int getSkullIcon() {
		return skullIcon;
	}

	public void setIsUsingPrayer(boolean[] isUsingPrayer) {
		this.isUsingPrayer = isUsingPrayer;
	}

	public boolean[] getIsUsingPrayer() {
		return isUsingPrayer;
	}
	
	public void setErnestLevers(int slot, boolean bool) {
		this.ernestLevers[slot] = bool;
	}

	public boolean[] getErnestLevers() {
		return ernestLevers;
	}
	
	public void setHurkotsSpawned(boolean bool) {
		this.hurkotsSpawned = bool;
	}

	public boolean hurkotsSpawned() {
		return hurkotsSpawned;
	}

	public void setPrayerDrainTimer(int prayerDrainTimer) {
		this.prayerDrainTimer = prayerDrainTimer;
	}

	public int getPrayerDrainTimer() {
		return prayerDrainTimer;
	}

	public void setPrayer(Prayer prayer) {
		this.prayer = prayer;
	}

	public Prayer getPrayer() {
		return prayer;
	}

	public Teleportation getTeleportation() {
		return teleportation;
	}

	public Emotes getEmotes() {
		return emotes;
	}
	
	public SkillcapeEmotes getSkillcapeEmotes() {
		return skillcapeEmotes;
	}
	
	public SkillCapeHandler getSkillCapeHandler() {
		return skillcapeHandler;
	}

	public boolean shouldAutoRetaliate() {
		return autoRetaliate;
	}

	public void setAutoRetaliate(boolean autoRetaliate) {
		this.autoRetaliate = autoRetaliate;
	}

	public int getScreenBrightness() {
		return screenBrightness;
	}

	public void setScreenBrightness(int screenBrightness) {
		this.screenBrightness = screenBrightness;
	}

	public int getMouseButtons() {
		return mouseButtons;
	}

	public void setMouseButtons(int mouseButtons) {
		this.mouseButtons = mouseButtons;
	}

	public int getSplitPrivateChat() {
		return splitPrivateChat;
	}

	public void setSplitPrivateChat(int splitPrivateChat) {
		this.splitPrivateChat = splitPrivateChat;
	}

	public boolean isAcceptingAid() {
		return acceptAid == 0;
	}

	public int getAcceptAid() {
		return acceptAid;
	}

	public void setAcceptAid(int acceptAid) {
		this.acceptAid = acceptAid;
	}

	public int getMusicVolume() {
		return musicVolume;
	}

	public void setMusicVolume(int musicVolume) {
		this.musicVolume = musicVolume;
	}
	
	public int getMusicLooping() {
		return musicLoop;
	}
	
	public void setMusicLooping(int musicLoop) {
		this.musicLoop = musicLoop;
	}
	
	public void toggleMusicLooping() {
		if(this.musicLoop == 0)
		{
			this.musicLoop = 1;
		}
		else
		{
			this.musicLoop = 0;
		}
	}
	
	public void setMusicAuto(boolean musicAuto) {
		this.musicAuto = musicAuto;
	}
	
	public void toggleMusicAuto() {
		musicAuto = !musicAuto;
	}
	
	public boolean getMusicAuto() {
		return musicAuto;
	}
	
	public int getEffectVolume() {
		return effectVolume;
	}

	public void setEffectVolume(int effectVolume) {
		this.effectVolume = effectVolume;
	}
	
	public boolean getCanHaveGodCape() {
	    return canHaveGodCape;
	}
	
	public void setCanHaveGodCape(boolean bool){
	    this.canHaveGodCape = bool;
	}
	
	public int getMageArenaCasts(Spell spell) {
	    if(spell == Spell.FLAMES_OF_ZAMORAK)
		return zamorakCasts;
	    else if(spell == Spell.SARADOMIN_STRIKE)
		return saradominCasts;
	    else if(spell == Spell.CLAWS_OF_GUTHIX)
		return guthixCasts;
	    else return 100;
	}
	
	public void setMageArenaCasts(Spell spell, int casts) {
	    if(spell == Spell.FLAMES_OF_ZAMORAK)
		this.zamorakCasts = casts;
	    else if(spell == Spell.SARADOMIN_STRIKE)
		this.saradominCasts = casts;
	    else if(spell == Spell.CLAWS_OF_GUTHIX)
		this.guthixCasts = casts;
	}
	
	public void saveZamorakCasts(int casts) {
	    this.zamorakCasts = casts;
	}
	public void saveSaradominCasts(int casts) {
	    this.saradominCasts = casts;
	}
	public void saveGuthixCasts(int casts) {
	    this.guthixCasts = casts;
	}
	
	public int getMageArenaStage() {
	    return mageArenaStage;
	}
		
	public void setMageArenaStage(int stage) {
	    this.mageArenaStage = stage;
	}
	
	public Entity getArmorPiercedEntity() {
	    return this.armorPiercedEntity;
	}
	
	public void setArmorPiercedEntity(Entity set) {
	    this.armorPiercedEntity = set;
	}
	
	public int getQuestPoints() {
		return questPoints;
	}

	public void setQuestPoints(int qP) {
		this.questPoints = qP;
	}
	
	public void addQuestPoints(int points) {
        this.questPoints += points;
        getActionSender().sendString("QP: @gre@"+questPoints+" ", 3985);
        getActionSender().sendString("Quest Points: "+questPoints, 640);
    }
	
    public void setQuestsLength(int length) {
        this.questStage = new int[length];
    }

    public void setQuestStage(int index, int questStage) {
        this.questStage[index] = questStage;
    }

	public void setMagicBookType(SpellBook magicBookType) {
		this.magicBookType = magicBookType;
	}

	public SpellBook getMagicBookType() {
		return magicBookType;
	}

	public void addPlugin(LocalPlugin lp) {
		plugins.add(lp);
	}

	public List<LocalPlugin> getPlugins() {
		return plugins;
	}

	public void removePlugin(LocalPlugin lp) {
		plugins.remove(lp);
	}

	public void setSpecialAmount(int specialAmount) {
		this.specialAmount = specialAmount;
		if (this.specialAmount > 100) {
			this.specialAmount = 100;
		}
	}

	public int getSpecialAmount() {
		return specialAmount;
	}

	public void setSpecialDamage(double specialDamage) {
		this.specialDamage = specialDamage;
	}

	public double getSpecialDamage() {
		return specialDamage;
	}

	public void setSpecialAccuracy(double specialAccuracy) {
		this.specialAccuracy = specialAccuracy;
	}

	public double getSpecialAccuracy() {
		return specialAccuracy;
	}

	public boolean isSpecialAttackActive() {
		return specialAttackActive;
	}

	public void setSpecialAttackActive(boolean specialAttackActive) {
		this.specialAttackActive = specialAttackActive;
	}

	public void setRingOfRecoilLife(int ringOfRecoilLife) {
		this.ringOfRecoilLife = ringOfRecoilLife;
	}

	public int getRingOfRecoilLife() {
		return ringOfRecoilLife;
	}

	public void setRingOfForgingLife(int ringOfForgingLife) {
		this.ringOfForgingLife = ringOfForgingLife;
	}

	public int getRingOfForgingLife() {
		return ringOfForgingLife;
	}

	public int getBindingNeckCharge() {
		return bindingNeckCharge;
	}

	public void setBindingNeckCharge(int bindingNeckCharge) {
		this.bindingNeckCharge = bindingNeckCharge;
	}
	
	public void setClayBraceletLife(int clayBraceletLife) {
		this.clayBraceletLife = clayBraceletLife;
	}

	public int getClayBraceletLife() {
		return clayBraceletLife;
	}

	public void setFightMode(int fightMode) {
		this.fightMode = fightMode;
	}

	public int getFightMode() {
		return fightMode;
	}

	public void setFightStyle(int fightStyle) {
		this.fightStyle = fightStyle;
	}

	public int getFightStyle() {
		return fightStyle;
	}

	public void setFightType(int fightType) {
		this.fightType = fightType;
	}

	public int getFightType() {
		return fightType;
	}

	public void setFightXp(int fightXp) {
		this.fightXp = fightXp;
	}

	public int getFightXp() {
		return fightXp;
	}

	public void setUsingBow(boolean usingBow) {
		this.usingBow = usingBow;
	}

	public boolean isUsingBow() {
		return usingBow;
	}

	public void setUsingCross(boolean usingCross) {
		this.usingCross = usingCross;
	}

	public boolean isUsingCross() {
		return usingCross;
	}

	public void setUsingCrystalBow(boolean usingCrystalBow) {
		this.usingCrystalBow = usingCrystalBow;
	}

	public boolean isUsingCrystalBow() {
		return usingCrystalBow;
	}

	public void setStopPacket(boolean stopPlayerPacket) {
		this.stopPlayerPacket = stopPlayerPacket;
	}

	public boolean stopPlayerPacket() {
		if (transformNpc == 2626 || (transformNpc >= 3689 && transformNpc <= 3694)) {
			return true;
		}
		return this.stopPlayerPacket;
	}

	public void setUsingArrows(boolean usingArrows) {
		this.usingArrows = usingArrows;
	}

	public boolean isUsingArrows() {
		return usingArrows;
	}

	public void setUsingBolts(boolean usingBolts) {
		this.usingBolts = usingBolts;
	}

	public boolean isUsingBolts() {
		return usingBolts;
	}

	public void setUsingOtherRangedWeapon(boolean usingOtherRangedWeapon) {
		this.usingOtherRangedWeapon = usingOtherRangedWeapon;
	}

	public boolean isUsingOtherRangedWeapon() {
		return usingOtherRangedWeapon;
	}

	public void setDropArrow(boolean dropArrow) {
		this.dropArrow = dropArrow;
	}

	public boolean isDropArrow() {
		return dropArrow;
	}

	public void setFullDharok(boolean fullDharok) {
		this.fullDharok = fullDharok;
	}

	public boolean hasFullDharok() {
		return fullDharok;
	}

	public void setFullAhrim(boolean fullAhrim) {
		this.fullAhrim = fullAhrim;
	}

	public boolean hasFullAhrim() {
		return fullAhrim;
	}

	public void setFullKaril(boolean fullKaril) {
		this.fullKaril = fullKaril;
	}

	public boolean hasFullKaril() {
		return fullKaril;
	}

	public void setFullTorag(boolean fullTorag) {
		this.fullTorag = fullTorag;
	}

	public boolean hasFullTorag() {
		return fullTorag;
	}
	
	public boolean hasFullVoidMelee() {
		return fullVoidMelee;
	}
	public void setFullVoidMelee(boolean fullVoidMelee) {
		this.fullVoidMelee = fullVoidMelee;
	}
	public boolean hasFullVoidRange() {
		return fullVoidRange;
	}
	public void setFullVoidRange(boolean fullVoidRange) {
		this.fullVoidRange = fullVoidRange;
	}
	public boolean hasFullVoidMage() {
		return fullVoidMage;
	}
	public void setFullVoidMage(boolean fullVoidMage) {
		this.fullVoidMage = fullVoidMage;
	}
	public boolean hasVoidMace() {
		return voidMace;
	}
	public void setVoidMace(boolean voidMace) {
		this.voidMace = voidMace;
	}
	
	public boolean wearingCwBracelet(){
		int brace = getEquipment().getId(Constants.HANDS);
		return brace == 11079 || brace == 11081 || brace == 11083;
	}
	
	public void damageCwBracelet(){
		int brace = getEquipment().getId(Constants.HANDS);
		String msg = "Your " + ItemManager.getInstance().getItemName(brace) + " has degraded.";
		if(brace == 11079){
			getEquipment().replaceEquipment(11081, Constants.HANDS);
		} else if(brace == 11081){
			getEquipment().replaceEquipment(11083, Constants.HANDS);
		} else if(brace == 11083){
			getEquipment().replaceEquipment(-1, Constants.HANDS);
			msg = "Your " + ItemManager.getInstance().getItemName(brace) + " crumbles.";
		}
		getActionSender().sendMessage(msg);
	}
	
	public boolean carryingCwBanner(){
		return getEquipment().getId(Constants.WEAPON) == Castlewars.SARA_BANNER || getEquipment().getId(Constants.WEAPON) == Castlewars.ZAMMY_BANNER;
	}
	
	public int getSkillAnswer() {
	    return this.skillAnswer;
	}
	
	public void setSkillAnswer(int answer){
	    this.skillAnswer = answer;
	}
	    
	public int getPcSkillPoints() {
	    return this.pcSkillPoints;
	}
	
	public void setPcSkillPoints(int answer){
	    this.pcSkillPoints = answer;
	}
	public void setFullGuthan(boolean fullGuthan) {
		this.fullGuthan = fullGuthan;
	}

	public boolean hasFullGuthan() {
		return fullGuthan;
	}

	public void setFullVerac(boolean fullVerac) {
		this.fullVerac = fullVerac;
	}

	public boolean hasFullVerac() {
		return fullVerac;
	}

	public long getLastFire() {
		return lastFire;
	}

	public void setLastFire(long lastFire) {
		this.lastFire = lastFire;
	}

	public void setStopProtectPrayer(long stopProtectPrayer) {
		this.stopProtectPrayer = stopProtectPrayer;
	}

	public long getStopProtectPrayer() {
		return stopProtectPrayer;
	}

	/*public boolean isMoving() {
		return getPrimaryDirection() != -1 || getSecondaryDirection() != -1;
	}*/

	@Override
	public int getCurrentHp() {
		return skill.getLevel()[Skill.HITPOINTS];
	}

	@Override
	public int getMaxHp() {
		return skill.getPlayerLevel(Skill.HITPOINTS);
	}

	@Override
	public int getDeathAnimation() {
		return 2304;
	}

	@Override
	public int getBlockAnimation() {
		Item shield = equipment.getItemContainer().get(Constants.SHIELD);
		if (shield != null) {
			String name = ItemDefinition.forId(shield.getId()).getName().toLowerCase();
			if (name.contains("shield"))
				return 1156;
			else if(name.contains("defender"))
				return 4177;
			else if(name.contains("2h") || name.contains("godsword"))
				return 7050;
		}
		return equippedWeapon.getBlockAnimation();
	}

	@Override
	public int getDeathAnimationLength() {
		return 6;
	}
	
	@Override
	public void setDeathAnimationLength(int length) {
	}

	@Override
	public int getBaseAttackLevel(AttackType attackType) {
		if (attackType == AttackType.RANGED)
			return skill.getLevel()[Skill.RANGED];
		else if (attackType == AttackType.MAGIC)
			return skill.getLevel()[Skill.MAGIC];
		return skill.getLevel()[Skill.ATTACK];
	}

	@Override
	public int getBaseDefenceLevel(AttackType attackType) {
		if (attackType == AttackType.MAGIC)
			return skill.getLevel()[Skill.MAGIC];
		return skill.getLevel()[Skill.DEFENCE];
	}

	@Override
	public boolean isProtectingFromCombat(AttackType attackType, Entity attacker) {
		if (attackType == AttackType.MELEE)
			return isUsingPrayer[PrayerData.PROTECT_FROM_MELEE.getIndex()];
		else if (attackType == AttackType.RANGED)
			return isUsingPrayer[PrayerData.PROTECT_FROM_RANGED.getIndex()];
		else if (attackType == AttackType.MAGIC)
			return isUsingPrayer[PrayerData.PROTECT_FROM_MAGIC.getIndex()];
		return false;
	}

	@Override
	public void setCurrentHp(int hp) {
		skill.setSkillLevel(Skill.HITPOINTS, hp);
		skill.refresh(Skill.HITPOINTS);
	}

	public long getUsernameAsLong() {
		return usernameAsLong;
	}

	public void setUsernameAsLong(long usernameAsLong) {
		this.usernameAsLong = usernameAsLong;
	}

	public void setCurrentWalkableInterface(int currentWalkableInterface) {
		this.currentWalkableInterface = currentWalkableInterface;
	}

	public int getCurrentWalkableInterface() {
		return currentWalkableInterface;
	}

	public void setSpecialType(SpecialType specialType) {
		this.specialType = specialType;
	}

	public SpecialType getSpecialType() {
		return specialType;
	}

	public void setEquippedWeapon(Weapon weapon) {
		if (weapon == null)
			weapon = Weapon.FISTS;
		this.equippedWeapon = weapon;
	}

	public Weapon getEquippedWeapon() {
		return equippedWeapon;
	}

	public boolean clickSpecialBar(int buttonId) {
		switch (buttonId) { // Instant specs
		case 29074: // new client
			if (getEquipment().getId(Constants.WEAPON) == 1377) {
				SpecialType.dbaxeSpec(this);
				return true;
			}
			break;
		case 29163:
			if (getEquipment().getId(Constants.WEAPON) == 35) {
				SpecialType.excaliburSpec(this);
				return true;
			}
			break;
		case 29049: // new client
			if (getEquipment().getId(Constants.WEAPON) == 4153) {
				if (getCombatingEntity() == null) {
					getActionSender().sendMessage("You can only use this special when attacking something.");
					return true;
				}
				SpecialType.gmaulSpec(this);
				return true;
			}
			break;
		case 1010101: //dfs
		    if (getEquipment().getId(Constants.SHIELD) == 11284 || getEquipment().getId(Constants.SHIELD) == 11283) {
				if (getCombatingEntity() == null) {
					getActionSender().sendMessage("You can only use this when attacking something.");
					return true;
				}
				SpecialType.dfsUncharge(this);
				this.dfsTimer = true;
				final Player player = this;
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				    @Override
				    public void execute(CycleEventContainer b) {
					b.stop();
				    }
				    @Override
				    public void stop() {
					player.dfsTimer = false;
				    }
				}, 120);
				return true;
			}
			break;
		}
		boolean before = specialAttackActive;
		if (equippedWeapon.getWeaponInterface().getSpecialBarButtonId() != buttonId){
			if(buttonId != 1010101) //new client temporary
				return false;
		}
		setSpecialAttackActive(!specialAttackActive);
		if (specialAttackActive && specialType == null)
			setSpecialAttackActive(false);
		if (specialAttackActive != before)
			updateSpecialBar();
		return true;
	}

	public void updateSpecialBar() {
		if (equippedWeapon.getWeaponInterface().getSpecialBarId() < 1) {
			return;
		}
		actionSender.updateSpecialAmount(equippedWeapon.getWeaponInterface().getSpecialTextId());
		actionSender.updateSpecialBarText(equippedWeapon.getWeaponInterface().getSpecialTextId());
	}

	public int getSkullTimer() {
		int size = skullRecords.size();
        if (size == 0)
            return 0;
        int timer = 0;
		for (Iterator<SkullRecord> skullRecordIterator = skullRecords.iterator(); skullRecordIterator.hasNext();) {
			int nextTime = skullRecordIterator.next().ticksRemaining();
			if (nextTime > timer) {
				timer = nextTime;
			}
		}
		return timer;
	}

	public void expireSkullRecords() {
		int size = skullRecords.size();
        if (size == 0)
            return;
		for (Iterator<SkullRecord> skullRecordIterator = skullRecords.iterator(); skullRecordIterator.hasNext();) {
			if (skullRecordIterator.next().expired())
				skullRecordIterator.remove();
		}
		size = skullRecords.size();
		if (size == 0) {
			setSkulled(false);
		}
	}

	public boolean containsSkullRecord(Player other) {
		for (SkullRecord skullRecord : skullRecords)
			if (skullRecord.getEntity() == other)
				return true;
		return false;
	}

	public void addSkull(Player victim, int timer) {
		SkullRecord skullRecord = new SkullRecord(victim, timer);
		skullRecords.add(skullRecord);
		setSkulled(true);
	}

	public void addPossibleSkull(Player victim) {
		if (!victim.isPlayer())
			return;
		Player other = (Player) victim;
		if (inDuelArena())
			return;
		if (inCwGame())
			return;
		if (other.containsSkullRecord(this))
			return;
		for (SkullRecord skullRecord : skullRecords)
			if (skullRecord.getEntity() == victim) {
				skullRecord.refresh();
				return;
			}
		addSkull(victim, SkullRecord.PK_EXPIRE_TIME);
	}

	public void setSkulled(boolean isSkulled) {
		this.isSkulled = isSkulled;
		setSkullIcon(isSkulled ? 0 : -1);
		setAppearanceUpdateRequired(true);
	}

	public ArrayList<Item> getItemsKeptOnDeath(Item[] items) {
		PriorityQueue<Item> allItems = new PriorityQueue<Item>(1, new Comparator<Item>() {
			@Override
			public int compare(Item a, Item b) {
			    if(Degradeables.getDegradeableItem(a) != null) {
				a = new Item(Degradeables.getDegradeableItem(a).getOriginalId());
			    }
			    if(Degradeables.getDegradeableItem(b) != null) {
				b = new Item(Degradeables.getDegradeableItem(b).getOriginalId());
			    }
				return ItemDefinition.forId(b.getId()).getHighAlcValue() - ItemDefinition.forId(a.getId()).getHighAlcValue();
			}
		});

		for (Item item : items) {
			if (item == null)
				continue;
			if (!item.getDefinition().isUntradable())
				allItems.add(new Item(item.getId()));
		}
		ArrayList<Item> keptItems = new ArrayList<Item>();
		int itemsKept = isSkulled ? 0 : 3;
		if (isUsingPrayer[PrayerData.PROTECT_ITEM.getIndex()])
			itemsKept += 1;
		while (keptItems.size() < itemsKept && allItems.size() > 0) {
			keptItems.add(allItems.poll());
		}
		return keptItems;
	}

	@Override
	public void dropItems(Entity killer) {
		if(Constants.DDOS_PROTECT_MODE) {
			return;
		}
		if (inDuelArena() || creatureGraveyard.isInCreatureGraveyard() || inPestControlLobbyArea() || inPestControlGameArea() || onPestControlIsland() || inFightCaves()) {
			return; //prevents the dropping of items
		}
		if (inCwGame() || this.Area(2754, 2814, 3833, 3873)) {
			return; //prevents the dropping of items
		}
		if (killer == null) {
			killer = this;
		}
		if (killer.isNpc() ) {
			killer = this;
		}
	/*	if (!killer.isPlayer()) {
			killer = this;
		}*/
		if(getLoginStage() != LoginStages.LOGGED_IN)
		{
			return;
		}
		Item[] items = new Item[equipment.getItemContainer().capacity() + inventory.getItemContainer().capacity()];
		System.arraycopy(equipment.getItemContainer().getItems(), 0, items, 0, equipment.getItemContainer().getItems().length);
		System.arraycopy(inventory.getItemContainer().getItems(), 0, items, equipment.getItemContainer().getItems().length, inventory.getItemContainer().getItems().length);
		ArrayList<Item> keptItems = getItemsKeptOnDeath(items);
		ArrayList<Integer> alwaysProtect = new ArrayList<>();
		List<Item> allItems = new ArrayList<Item>(Arrays.asList(items));
		for(int i = 0; i < Pets.PET_IDS.length; i++) {
		    if(inventory.playerHasItem(new Item(Pets.PET_IDS[i][0])) )
				alwaysProtect.add(Pets.PET_IDS[i][0]);
		}
		if(inventory.playerHasItem(FightCaves.FIRE_CAPE) || equipment.getId(Constants.CAPE) == FightCaves.FIRE_CAPE) {
		    alwaysProtect.add(FightCaves.FIRE_CAPE);
		}
		if(inventory.playerHasItem(7462) || equipment.getId(Constants.HANDS) == 7462) {
		    alwaysProtect.add(7462);
		}
		for (Item kept : keptItems) {
			if (kept == null)
				continue;
			for (Iterator<Item> droppedItems = allItems.iterator(); droppedItems.hasNext();) {
				Item dropped = droppedItems.next();
				if (dropped != null) {
					if (dropped.getId() == kept.getId()) {
						dropped.setCount(dropped.getCount() - 1);
						if (dropped.getCount() <= 0)
							droppedItems.remove();
						break;
					}
				}
			}
		}
		equipment.getItemContainer().clear();
		inventory.getItemContainer().clear();
		GroundItemManager.getManager().dropItem(new GroundItem(new Item(526, 1), this, getDeathPosition()));
		for (Item kept : keptItems)
			inventory.addItem(kept);
		for (int i : alwaysProtect) {
		    boolean alreadyKept = false;
		    for(Item kept : keptItems) {
			if(kept.getId() == i) {
			    alreadyKept = true;
			}
		    }
		    if(!alreadyKept) {
			inventory.addItem(new Item(i));
		    }
			
		}
		for (Item dropped : allItems) {
			if (dropped == null)
				continue;
			if (alwaysProtect.contains(dropped.getId()))
				continue;
			if(dropped.getId() >= 3839 && dropped.getId() <= 3844) {
			    this.setLostGodBook(dropped.getId());
			}
			if(Degradeables.notDroppable(Degradeables.getDegradeableItem(dropped), dropped) && Constants.DEGRADING_ENABLED) {
				GroundItemManager.getManager().dropItem(new GroundItem(new Item(Degradeables.getDegradeableItem(dropped).getBrokenId()), killer));
				setDegradeableHits(Degradeables.getDegradeableItem(dropped).getPlayerArraySlot(), 0);
			}
			else if (!dropped.getDefinition().isUntradable()) {
				GroundItem item = new GroundItem(new Item(dropped.getId(), dropped.getCount()), this, killer, getDeathPosition());
				GroundItemManager.getManager().dropItem(item);
			} else {
			    if(dropped.getId() == 11283) {
				GroundItem item = new GroundItem(new Item(11284, dropped.getCount()), this, getDeathPosition());
				GroundItemManager.getManager().dropItem(item);
			    } else {
				GroundItem item = new GroundItem(new Item(dropped.getId(), dropped.getCount()), this, getDeathPosition());
				GroundItemManager.getManager().dropItem(item);
			    }
			}
		}
		equipment.refresh();
		inventory.refresh();
	}

	public Spell getCastedSpell() {
		return castedSpell;
	}

	public Spell getAutoSpell() {
		return autoSpell;
	}

	public void setCastedSpell(Spell spell) {
		this.castedSpell = spell;
	}

	public boolean isAutoCasting() {
		return autoCasting;
	}

	public void setAutoCasting(boolean autoCasting) {
		if (autoCasting) {
			getActionSender().sendConfig(108, 3);
			getActionSender().sendConfig(43, 3);
		} else {
			getActionSender().resetAutoCastInterface();
		}
		this.autoCasting = autoCasting;
	}

	public void setAutoSpell(Spell spell) {
		if (spell == null) {
			getActionSender().resetAutoCastInterface();
			this.autoCasting = false;
		} else if (hasVoidMace() && spell == Spell.CLAWS_OF_GUTHIX) {
		    	getActionSender().sendSidebarInterface(0, 3796);
			getActionSender().updateAutoCastInterface(spell);
			this.autoCasting = true;
		} else {
			getActionSender().sendSidebarInterface(0, 328);
			getActionSender().updateAutoCastInterface(spell);
			this.autoCasting = true;
		}
		this.autoSpell = spell;
	}

	public void disableAutoCast() { // ONLY for fight mode switch
		this.getActionSender().sendConfig(108, 2);
		this.autoCasting = false;
	}

	public void setMember(boolean member) {
		this.member = member;
	}

	public boolean isMember() {
		return member;
	}

	/**
	 * Gets the
	 * 
	 * @return The outData
	 */
	public ByteBuffer getOutData() {
		return outData;
	}

	public void setStatedInterface(String statedInterface) {
		this.statedInterface = statedInterface;
	}

	public void setTempInteger(int tempInteger) {
		this.tempInteger = tempInteger;
	}

	public int getTempInteger() {
		return tempInteger; // To change body of created methods use File |
							// Settings | File Templates.
	}

	public List<SkullRecord> getSkullRecords() {
		return skullRecords;
	}

	public void setOldItem(int oldItem) {
		this.oldItem = oldItem;
	}

	public int getOldItem() {
		return oldItem;
	}

	public void setSmithInterface(int smithInterface) {
		this.smithInterface = smithInterface;
	}

	public int getSmithInterface() {
		return smithInterface;
	}
	
	public void setHintIndex(int index) {
	    this.hintIndex = index;
	}
	
	public int getHintIndex() {
	    return this.hintIndex;
	}
	
	public Canoe getCanoe()
	{
		return canoe;
	}
	
	public DesertHeat getDesertHeat()
	{
		return desertHeat;
	}
	
	public void setCastlewarsTeam(int cwteam)
	{
		castleWarsTeam = cwteam;
	}
	public int getCastlewarsTeam()
	{
		return castleWarsTeam;
	}
	
	public int getStandAnim() {
		if (standAnim == -1)
			return getEquipment().getStandAnim();
		return standAnim;
	}

	public int getWalkAnim() {
		if (walkAnim == -1)
			return getEquipment().getWalkAnim();
		return walkAnim;
	}

	public int getRunAnim() {
		if (runAnim == -1)
			return getEquipment().getRunAnim();
		return runAnim;
	}

	public void setRunAnim(int runAnim) {
		this.runAnim = runAnim;
	}

	public void setWalkAnim(int walkAnim) {
		this.walkAnim = walkAnim;
	}

	public void setStandAnim(int standAnim) {
		this.standAnim = standAnim;
	}

	public int getStandTurn() {
		int curWalkAnim = getWalkAnim();
		if (curWalkAnim != Weapon.FISTS.getMovementAnimations()[1])
			return curWalkAnim;
		else
			return 0x337;
	}

	public int get180Turn() {
		int curWalkAnim = getWalkAnim();
		if (curWalkAnim != Weapon.FISTS.getMovementAnimations()[1])
			return curWalkAnim;
		return 0x334;
	}

	public int get90TurnCW() {
		int curWalkAnim = getWalkAnim();
		if (curWalkAnim != Weapon.FISTS.getMovementAnimations()[1])
			return curWalkAnim;
		return 0x335;
	}

	public int get90TurnCCW() {
		int curWalkAnim = getWalkAnim();
		if (curWalkAnim != Weapon.FISTS.getMovementAnimations()[1])
			return curWalkAnim;
		return 0x336;
	}

    public void setMuteExpire(long l) {
        this.muteExpire = l;
    }
    
    public void setBanExpire(long l) {
        this.banExpire = l;
    }

    public long getMuteExpire() {
        return muteExpire;
    }
    
    public long getBanExpire() {
        return banExpire;
    }

    public void resetEffects() {
		getSkullRecords().clear();
		setSkulled(false);
		getPrayer().resetAll();
		setEnergy(100);
		setSpecialAmount(100);
		updateSpecialBar();
		resetEffectTimers();
		resetImmuneTimers();
		effects.clear();
		removeAllEffects();
		int skills[] = getSkill().getLevel();
		for (int i = 0; i < skills.length; i++)
			getSkill().setSkillLevel(i, getSkill().getPlayerLevel(i));
		getSkill().refresh();
    }
   
    public boolean logoutDisabled() {
    	if (getLoginStage() == LoginStages.LOGGING_OUT && getLogoutTimer() < System.currentTimeMillis()) {
        	return false;
    	}
        return !getInCombatTick().completed();
    }

    public boolean hasInterfaceOpen(RSInterface inter) {  
        if (inter == null)
            return false;
        if (getInterface() == inter.getParentId())
            return true;
        if (sideBarOpen == inter.getParentId())
            return true;
        for (int i : sidebarInterfaceId)
            if (i == inter.getParentId())
                return true;
        return false;
    }

    public enum BankOptions {
		SWAP_ITEM, INSERT_ITEM, ITEM_WITHDRAW, NOTE_WITHDRAW
	}

	public enum TradeStage {
		WAITING, SEND_REQUEST, ACCEPT, SEND_REQUEST_ACCEPT, SECOND_TRADE_WINDOW
	}

	public enum LoginStages {
		CONNECTED, LOGGING_IN, AWAITING_LOGIN_COMPLETE, LOGGED_IN, LOGGING_OUT, LOGGED_OUT
	}

	public void appendToAutoSpawn(NpcDefinition npc) {
		//int randNum = Misc.random(4) + 2;
		/*
		String filePath = "./data/spawns.txt";
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filePath, true));
			try {
				out.write("spawn = " + npc.getId() + "\t    " + getPosition().getX() + "	" + getPosition().getY() + "	0	" + randNum + "	" + npc.getName());
				out.newLine();
                out.flush();
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		NpcLoader.newWanderNPC(npc.getId(), getPosition().getX(), getPosition().getY(), getPosition().getZ());
	}

	public void resetAllActions() {
		setCurrentSkillTask();
		getTask();
		resetPacketVariables();
		Following.resetFollow(this);
		setInteractingEntity(null);
		CombatManager.resetCombat(this);
		getDialogue().endDialogue();
		if (getUpdateFlags().getEntityFaceIndex() != 65535) {
			getUpdateFlags().faceEntity(65535);
		}
	}
	
	public void resetAllActions(boolean resetCombat) {
		setCurrentSkillTask();
		getTask();
		resetPacketVariables();
		Following.resetFollow(this);
		setInteractingEntity(null);
		if(resetCombat) {
			resetCombat();
		}
		getDialogue().endDialogue();
		if (getUpdateFlags().getEntityFaceIndex() != 65535) {
			getUpdateFlags().faceEntity(65535);
		}
	}
	
	public void resetCombat()
	{
		CombatManager.resetCombat(this);
	}

	public void resetSkillActions() {
		getTask();
		setCurrentSkillTask();
		resetPacketVariables();
	}

	public void resetPacketVariables() {
		setInterfaceId(-1);
		setClickId(-1);
		setClickY(-1);
		setSlot(-1);
		setClickX(-1);
		setClickZ(-1);
		setClickItem(-1);
	}

	public boolean shouldHideWeapons() {
		return hideWeapons;
	}

	public void setHideWeapons(boolean hideWeapons) {
		this.hideWeapons = hideWeapons;
	}

	public boolean isVisible() {
		return visible;
	}

	public int antiFire() {
		int fire = 0;
		if (isFireImmune()) {
			fire += 1;
		}
		if (getEquipment().getId(Constants.SHIELD) == 1540) {
			fire += 1;
		}
		if (getEquipment().getId(Constants.SHIELD) == 11284 || getEquipment().getId(Constants.SHIELD) == 11283) {
			fire += 1;
		}
		return fire;
	}

	public boolean npcCanAttack(Npc npc) {
		if (npc.getNpcId() == 1052) {
		    return npc.getCombatingEntity() == null;
		}
		if (npc.isAttacking() || !npc.getDefinition().isAttackable()) {
			return false;
		}
		if(npc.onApeAtoll() && ApeAtollNpcs.isAggressiveNpc(npc.getNpcId())) {
		    if(npc.getNpcId() == ApeAtollNpcs.SCORPION && !Misc.goodDistance(getPosition(), npc.getPosition(), 2)) {
			return false;
		    }
		    return !getMMVars().isMonkey();
		}
		if (npc.getNpcId() == 99 || npc.getNpcId() == 2429 || npc.getNpcId() == 1827 || npc.getNpcId() == 1266 || npc.getNpcId() == 1268 || npc.getNpcId() == 2453 || npc.getNpcId() == 2890) {
			return true;
		}
		if (npc.getNpcId() == 18) {
			return getCombatingEntity() != null;
		}
		if (npc.getNpcId() == 1931) {
			return getCombatingEntity() != null;
		}
		if (npc.inWild()) {
			return true;
		}
		if (!npc.getDefinition().isAggressive()) {
			return false;
		}
		if (getCombatLevel() <= npc.getDefinition().getCombat() * 2) {
			return true;
		}
		return false;
	}

	public void checkNpcAggressiveness() {
		if(Constants.DDOS_PROTECT_MODE || getStaffRights() >= 3) {
			return;
		}
		if (!getInCombatTick().completed() && !inMulti()) {
			return;
		}
		for (Npc npc : getNpcs()) {
			if (npc == null) {
				continue;
			}
			if (npc.isDead()) {
				continue;
			}
			if (npc.getPlayerOwner() != null) {
				continue;
			}
			if(onApeAtoll() && npc.onApeAtoll()) {
			    if ((npc.getNpcId() == MonkeyMadness.MONKEYS_AUNT || npc.getNpcId() == MonkeyMadness.AWOWOGEI) && Misc.goodDistance(npc.getPosition(), getPosition(), 5) && Misc.checkClip(npc.getPosition(), getPosition(), true) && !getMMVars().inProcessOfBeingJailed && !getMMVars().isMonkey()) {
				npc.getUpdateFlags().setForceChatMessage("OOH! OOH! AAH!");
				ApeAtoll.jail(this, true);
			    }
			    if (npc.getNpcId() == 1457 && Misc.goodDistance(npc.getPosition(), getPosition(), 5) && Misc.checkClip(npc.getPosition(), getPosition(), true) && ApeAtoll.hiddenInGrass(this) && npc.isAttacking()) {
				CombatManager.resetCombat(npc);
			    }
			    if (npc.getNpcId() == 1458 && Misc.goodDistance(npc.getPosition(), getPosition(), 5) && !Area(2762, 2767, 2767, 2772) && npc.isAttacking()) {
				CombatManager.resetCombat(npc);
			    }
			    if (npc.getNpcId() == 1456 && Misc.goodDistance(npc.getPosition(), getPosition(), 10) && !getMMVars().inProcessOfBeingJailed && !getMMVars().isMonkey()) {
				CombatManager.attack(npc, this);
				if (getPosition().getY() > 2758) {
				    ApeAtoll.jail(this, false);
				}
			    }
			    if (npc.getNpcId() == 1457 && Misc.goodDistance(npc.getPosition(), getPosition(), 5) && Misc.checkClip(npc.getPosition(), getPosition(), false) && !getMMVars().inProcessOfBeingJailed && !getMMVars().isMonkey() && !ApeAtoll.hiddenInGrass(this)) {
				if (!npc.isAttacking()) {
				    CombatManager.attack(npc, this);
				}
				if (Misc.random(4) == 1 && !this.isAttacking()) {
				    CombatManager.attack(npc, this);
				    ApeAtoll.jail(this, true);
				}
			    }
			    if (npc.getNpcId() == 1458 && Misc.goodDistance(npc.getPosition(), getPosition(), 5) && Misc.checkClip(npc.getPosition(), getPosition(), false) && !getMMVars().inProcessOfBeingJailed && !getMMVars().isMonkey() && Area(2762, 2767, 2767, 2772) && !npc.isAttacking()) {
				CombatManager.attack(npc, this);
				npc.getUpdateFlags().setForceChatMessage("AAH! AAH!");
				ApeAtoll.jail(this, true);
			    }
			}
			if (!npcCanAttack(npc)) {
				continue;
			}
			if (Misc.goodDistance(npc.getPosition(), getPosition(), 7) && npc.getNpcId() >= 2881 && npc.getNpcId() <= 2883 && npc.getCombatingEntity() == null) {
			    CombatManager.attack(npc, this);
			}
			if (Misc.goodDistance(npc.getSpawnPosition(), getPosition(), npc.getNpcId() == 1266 || npc.getNpcId() == 1268 || npc.getNpcId() == 2453 || npc.getNpcId() == 2890 ? 1 : 4)) {
			//if (npc.getWalkableArea().contains(getPosition().getX(), getPosition().getY())) {
				CombatCycleEvent.CanAttackResponse response = CombatCycleEvent.canAttack(npc, this);
				if (response != CombatCycleEvent.CanAttackResponse.SUCCESS)
					continue;
				if (npc.getNpcId() == 99) {
					npc.getUpdateFlags().setForceChatMessage("Grrr!");
				}
				if (npc.getNpcId() == 180) {
					npc.getUpdateFlags().setForceChatMessage("Stand and deliver!");
				}
				if (npc.getNpcId() == 18) {
					npc.getUpdateFlags().setForceChatMessage("Brother, I will help thee with this infidel!");
				}
				if (npc.getNpcId() == 1931) {
					npc.getUpdateFlags().setForceChatMessage("You chose the wrong place to start trouble!");
				}
				if (npc.getTransformTimer() < 1 && npc.isTransformOnAggression() > 0) {
					npc.sendTransform(npc.isTransformOnAggression(), 999999);
				}
				if (npc.getNpcId() == 1052) {
					npc.getUpdateFlags().setForceChatMessage("OOooooohhh");
				}
				CombatManager.attack(npc, this);
				return;
			}
		}
	}

	public int getPouchData(int i) {
		return pouchData[i];
	}

	public void setPouchData(int i, int amount) {
		pouchData[i] = amount;
	}
	
	public boolean hasPouchDrop(int itemId) {
		for (int i = 0; i < Pouches.POUCHES.length; i++) {
			if (itemId == Pouches.POUCHES[i][0]) {
				if(hasItem(itemId))
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param hamTrapDoor
	 *            the hamTrapDoor to set
	 */
	public void setHamTrapDoor(boolean hamTrapDoor) {
		this.hamTrapDoor = hamTrapDoor;
	}

	/**
	 * @return the hamTrapDoor
	 */
	public boolean isHamTrapDoor() {
		return hamTrapDoor;
	}
	
	/*
	@param inCutscene
	    BOOLEAN TO SET IF U R IN CUTSCEEN
	*/
	public void setInCutscene(boolean set) {
		this.inCutscene = set;
	}
	
	/*
	@return inCutscene
	    RETURNS IF PLAYER IN CUTSCEEN OR NO?
	*/
	public boolean isInCutscene() {
		return inCutscene;
	}

	public int getClientVersion() {
		return clientVersion;
	}

	public void setClientVersion(int clientVersion) {
		this.clientVersion = clientVersion;
	}

	public int getMagicId() {
		return magicId;
	}

	public void setMagicId(int magicId) {
		this.magicId = magicId;
	}

	/**
	 * @param runecraftNpc the runecraftNpc to set
	 */
	public void setRunecraftNpc(int runecraftNpc) {
		this.runecraftNpc = runecraftNpc;
	}

	/**
	 * @return the runecraftNpc
	 */
	public int getRunecraftNpc() {
		return runecraftNpc;
	}

	public BrewData getBrewData() {
		return brewData;
	}
	
	public boolean getInJail() {
		return inJail;
	}

	public void setInJail(boolean state) {
		inJail = state;
	}

	public boolean hasCombatEquipment() {
		for (Item item : getInventory().getItemContainer().getItems()) {
            if (item == null)
                continue;
            for (int b : item.getDefinition().getBonuses())
                if (b > 0) {
                    return true;
                }
        }
        for (Item item : getEquipment().getItemContainer().getItems()) {
            if (item == null)
                continue;
            for (int b : item.getDefinition().getBonuses())
                if (b > 0) {
                    return true;
                }
        }
        return false;
    }
    public boolean hasRunes() {
	for (Item item : getInventory().getItemContainer().getItems()) {
	    if (item == null) {
		continue;
	    }
	    if (item.getDefinition().getName().toLowerCase().contains("rune") 
		&& ((item.getId() > 550 && item.getId() < 570) || (item.getId() > 4693 && item.getId() < 4700))) {
		return true;
	    }
	}
	return false;
    }
    private Position getLoadedLandscapeCorner() {
        int x = getPosition().getLocalX(getCurrentRegion());
        int y = getPosition().getLocalY(getCurrentRegion());
        int cornerX = (getPosition().getX() - x);
        int cornerY = (getPosition().getY() - y);
        return new Position(cornerX, cornerY, getPosition().getZ());
    }

    public Area getLoadedLandscape() {
        return loadedLandscape;
    }

    public void calculateLoadedLandscape() {
        Position corner = getLoadedLandscapeCorner();
        this.loadedLandscape = Area.areaFromCorner(corner, 104, 104);
    }

	public boolean carryingItem(int id) {
		for (Item item : getInventory().getItemContainer().getItems()) {
			if (item != null && item.getId() == id) {
				return true;
			}
		}
		return false;
	}
	
	public boolean itemInBank(int id) {
		return getBankManager().ownsItem(id);
	}
	
	public boolean hasItem(int id) {
		for (Item item : getInventory().getItemContainer().getItems()) {
			if (item != null && item.getId() == id) {
				return true;
			}
		}
		if(getBankManager().ownsItem(id)){
			return true;
		}
		return false;
	}

	public boolean hasClueScroll() {
		for (Item item : getInventory().getItemContainer().getItems()) {
			if (item != null && item.getDefinition().getName().toLowerCase().contains("clue")) {
				return true;
			}
		}
		if(getBankManager().ownsItem("clue"))
		{
			return true;
		}
		return false;
	}

	public boolean hasPuzzle() {
		for (Item item : getInventory().getItemContainer().getItems()) {
			if (item != null && (item.getId() == 2800 || item.getId() == 3565 || item.getId() == 3571)) {
				return true;
			}
		}
		if(getBankManager().ownsItem(2800) || getBankManager().ownsItem(3565) || getBankManager().ownsItem(3571))
		{
			return true;
		}
		return false;
	}

    public List<GroundItem> getGroundItems() {
        return groundItems;
    }

	/**
	 * @param oldObject the oldObject to set
	 */
	public void setOldObject(int oldObject) {
		this.oldObject = oldObject;
	}

	/**
	 * @return the oldObject
	 */
	public int getOldObject() {
		return oldObject;
	}
    
    public boolean isMuted() {
        return muteExpire != 0 && muteExpire > System.currentTimeMillis();
    }
    
    public boolean isBanned() {
        return banExpire != 0 && banExpire > System.currentTimeMillis();
    }
    
    public boolean isIpBanned() 
    {
        if(getHost().length() <= 0)
        {
        	return false;
        }
    	return GlobalVariables.isIpBanned(getHost());
    }
    
    public boolean isMacBanned() 
    {
        if(getMacAddress().length() <= 0)
        {
        	return false;
        }
    	return GlobalVariables.isMacBanned(getMacAddress());
    }

	/**
	 * @param lastNpc the lastNpc to set
	 */
	public void setLastNpc(int lastNpc) {
		this.lastNpc = lastNpc;
	}

	/**
	 * @return the lastNpc
	 */
	public int getLastNpc() {
		return lastNpc;
	}

	public void setKilledTreeSpirit(boolean killedTreeSpirit) {
		this.killedTreeSpirit = killedTreeSpirit;
	}
	
	public void setResetBank(boolean resetbank) {
		this.resetbank = resetbank;
	}

	public boolean hasReset() {
		return resetbank;
	}
	
	/**
	 * @return the killedEvilSpirit
	 */
	public boolean hasKilledTreeSpirit() {
		return killedTreeSpirit;
	}
	
	public void setKilledJungleDemon(boolean killedJungleDemon) {
		this.killedJungleDemon = killedJungleDemon;
	}
	
	public SantaEncounter getSantaEncounter() {
	    return this.santaEncounter;
	}

	/**
	 * @return the killedJungleDemon
	 */
	public boolean hasKilledJungleDemon() {
		return killedJungleDemon;
	}

	public int getInterface() {
		return inter;
	}

	public void setInterface(int inter) {
		this.inter = inter;
	}

	public void setBarrowsNpcDead(int index, boolean dead) {
		this.barrowsNpcDead[index] = dead;
	}

	/**
	 * @return the barrowsNpcDead
	 */
	public boolean[] getBarrowsNpcDead() {
		return barrowsNpcDead;
	}

	/**
	 * @return the barrowsNpcDead
	 */
	public boolean getBarrowsNpcDead(int id) {
		return barrowsNpcDead[id];
	}

	/**
	 * @param killCount the killCount to set
	 */
	public void setKillCount(int killCount) {
		this.killCount = killCount;
	}

	/**
	 * @return the killCount
	 */
	public int getKillCount() {
		return killCount;
	}

	/**
	 * @param randomGrave the randomGrave to set
	 */
	public void setRandomGrave(int randomGrave) {
		this.randomGrave = randomGrave;
	}

	/**
	 * @return the randomGrave
	 */
	public int getRandomGrave() {
		return randomGrave;
	}
    
    public int getCombatLevel() {
        return combatLevel;
    }
    
    public void setCombatLevel(int combatLevel) {
        this.combatLevel = combatLevel;
    }

	/**
	 * @param loggingOut the loggingOut to set
	 */
	public void setLoggingOut(boolean loggingOut) {
		this.loggingOut = loggingOut;
	}

	/**
	 * @return the loggingOut
	 */
	public boolean isLoggingOut() {
		return loggingOut;
	}

	/**
	 * @param lastPersonTraded the lastPersonTraded to set
	 */
	public void setLastPersonTraded(Player lastPersonTraded) {
		this.lastPersonTraded = lastPersonTraded;
	}

	/**
	 * @return the lastPersonTraded
	 */
	public Player getLastPersonTraded() {
		return lastPersonTraded;
	}

	/**
	 * @param clickZ the clickZ to set
	 */
	public void setClickZ(int clickZ) {
		this.clickZ = clickZ;
	}

	/**
	 * @return the clickZ
	 */
	public int getClickZ() {
		return clickZ;
	}
    
    public void setSideBarInterfaceId(int slot, int id) {
        sidebarInterfaceId[slot] = id;
    }
    
    public void setSideBarOpen(int id) {
        this.sideBarOpen = id;
    }

	/**
	 * @param interfaceId the interfaceId to set
	 */
	public void setInterfaceId(int interfaceId) {
		this.interfaceId = interfaceId;
	}

	/**
	 * @return the interfaceId
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

	/**
	 * @param slot the slot to set
	 */
	public void setSlot(int slot) {
		this.slot = slot;
	}
	
	/**
	 * @return the slot
	 */
	public int getSlot() {
		return slot;
	}
	
	public void setEquipmentOperate(boolean val) {
		equipmentOperate = val;
	}
	
	public boolean getEquipmentOperate() {
		return equipmentOperate;
	}
	
	/**
	 * @param brimhavenDungeonOpen the brimhavenDungeonOpen to set
	 */
	public void setBrimhavenDungeonOpen(boolean brimhavenDungeonOpen) {
		this.brimhavenDungeonOpen = brimhavenDungeonOpen;
	}

	/**
	 * @return the brimhavenDungeonOpen
	 */
	public boolean isBrimhavenDungeonOpen() {
		return brimhavenDungeonOpen;
	}
	
	/**
	 * @param teleotherPosition the teleotherPosition to set
	 */
	public void setTeleotherPosition(Position teleotherPosition) {
		this.teleotherPosition = teleotherPosition;
	}

	/**
	 * @return the teleotherPosition
	 */
	public Position getTeleotherPosition() {
		return teleotherPosition;
	}

	/**
	 * @param destroyItem the destroyItem to set
	 */
	public void setDestroyItem(Item destroyItem) {
		this.destroyItem = destroyItem;
	}

	/**
	 * @return the destroyItem
	 */
	public Item getDestroyItem() {
		return destroyItem;
	}

    public boolean isHearMessage() {
		return hearMessage;
	}

	public void setHearMessage(boolean hearMessage) {
		this.hearMessage = hearMessage;
	}

	/**
	 * @param bankWarning the bankWarning to set
	 */
	public void setBankWarning(boolean bankWarning) {
		this.bankWarning = bankWarning;
	}

	/**
	 * @return the bankWarning
	 */
	public boolean isBankWarning() {
		return bankWarning;
	}

	/**
	 * @param pinAttempt the pinAttempt to set
	 */
	public void setPinAttempt(int pinAttempt, int index) {
		this.pinAttempt[index] = pinAttempt;
	}

	/**
	 * @param pinAttempt the pinAttempt to set
	 */
	public void resetPinAttempt() {
		this.pinAttempt = new int[4];
	}

	/**
	 * @return the pinAttempt
	 */
	public int[] getPinAttempt() {
		return pinAttempt;
	}

	public boolean isDebugCombat() {
		return debugCombat;
	}

	/**
	 * @param randomEventNpc the randomEventNpc to set
	 */
	public void setRandomEventNpc(Npc randomEventNpc) {
		this.randomEventNpc = randomEventNpc;
	}

	/**
	 * @return the randomEventNpc
	 */
	public Npc getRandomEventNpc() {
		return randomEventNpc;
	}

	/**
	 * @param randomHerb the randomHerb to set
	 */
	public void setRandomHerb(Item randomHerb) {
		this.randomHerb = randomHerb;
	}

	/**
	 * @return the randomHerb
	 */
	public Item getRandomHerb() {
		return randomHerb;
	}

	/**
	 * @param genieSelect the genieSelect to set
	 */
	public void setGenieSelect(int genieSelect) {
		this.genieSelect = genieSelect;
	}

	/**
	 * @return the genieSelect
	 */
	public int getGenieSelect() {
		return genieSelect;
	}

	public boolean hasKilledClueAttacker() {
		return killedClueAttacker;
	}

	public void setKilledClueAttacker(boolean killedClueAttacker) {
		this.killedClueAttacker = killedClueAttacker;
	}

	/**
	 * @param logoutTimer the logoutTimer to set
	 */
	public void setLogoutTimer(long logoutTimer) {
		this.logoutTimer = logoutTimer;
	}

	/**
	 * @return the logoutTimer
	 */
	public long getLogoutTimer() {
		return logoutTimer;
	}

	/**
	 * @param coalTruckAmount the coalTruckAmount to set
	 */
	public void setCoalTruckAmount(int coalTruckAmount) {
		this.coalTruckAmount = coalTruckAmount;
	}

	/**
	 * @return the coalTruckAmount
	 */
	public int getCoalTruckAmount() {
		return coalTruckAmount;
	}
	public void setDfsCharges(int set) {
	    this.dfsCharges = set;
	}
	public int getDfsCharges() {
	    return this.dfsCharges;
	}
	
	public void setReceivedMasks(boolean val)
	{
		receivedMasks = val;
	}
	
	public boolean getReceivedMasks()
	{
		return receivedMasks;
	}
	
	public void setHasZombieHead(boolean val)
	{
		hasZombieHead = val;
	}
	
	public boolean getHasZombieHead()
	{
		return hasZombieHead;
	}
	
	/**
	 * @param lastPersonChallenged the lastPersonChallenged to set
	 */
	public void setLastPersonChallenged(Player lastPersonChallenged) {
		this.lastPersonChallenged = lastPersonChallenged;
	}

	/**
	 * @return the lastPersonChallenged
	 */
	public Player getLastPersonChallenged() {
		return lastPersonChallenged;
	}

	public int getQuestStage(int questId) {
		return questStage[questId];
	}
	
	public void sendQuestTab(){
		getActionSender().sendString("Player Quests", 663);
		getActionSender().sendString("@red@Black Knight's Fortress", 7332); // black knight's fortress
		getActionSender().sendString("@red@Cook's Assistant", 7333);
		getActionSender().sendString("@red@Demon Slayer", 7334); //demon slayer
		getActionSender().sendString("@red@Doric's Quest", 7336);
		getActionSender().sendString("@red@Ernest the Chicken", 7339); // ernest the chicken
		getActionSender().sendString("@red@Goblin Diplomacy", 7338); //goblin diplomacy
		getActionSender().sendString("@red@The Restless Ghost", 7337);
		getActionSender().sendString("", 7383); //Dragon Slayer
		getActionSender().sendString("@red@The Imp Catcher", 7340);
		getActionSender().sendString("@red@Pirate's Treasure", 7341); //pirate's treasure
		getActionSender().sendString("@red@Prince Ali Rescue", 7342); //ali rescue
		getActionSender().sendString("@red@Romeo and Juliet", 7343); //romeo & juliet
		getActionSender().sendString("@red@Rune Mysteries", 7335);
		getActionSender().sendString("@red@Sheep Shearer", 7344);
		getActionSender().sendString("@red@Shield of Arrav", 7345); //shield of arrav
		getActionSender().sendString("@red@The Knight's Sword", 7346);
		getActionSender().sendString("@red@Vampire Slayer", 7347); //vampire slayer
		getActionSender().sendString("@red@Witch's Potion", 7348);
		getActionSender().sendString("", 8438); //death plateau
		getActionSender().sendString("", 12852); //desert treasure
		getActionSender().sendString("", 7354); //dig site
		getActionSender().sendString("@red@Druidic Ritual", 7355);
		getActionSender().sendString("@red@Dwarf Cannon", 7356); //dwarf cannon
		getActionSender().sendString("", 8679);//Eadgar's ruse
		getActionSender().sendString("@red@Elemental Workshop", 7459); //elemental workshop
		getActionSender().sendString("@red@Family Crest", 7357); //Family crest
		getActionSender().sendString("", 12836); //the feud
		getActionSender().sendString("", 7358); //fight arena
		getActionSender().sendString("", 7359); //Fishing Content
		getActionSender().sendString("", 14169); //forgettable tale
		getActionSender().sendString("", 10115); //the fremennik trials
		getActionSender().sendString("", 14604); //garden of tranquility
		getActionSender().sendString("@red@Gertrudes Cat", 7360); //gertrude's cat
		getActionSender().sendString("@red@Ghosts Ahoy", 12282); //ghosts ahoy
		getActionSender().sendString("", 13577); //the giant dwarf
		getActionSender().sendString("", 12839); //the golem
		getActionSender().sendString("@red@The Grand Tree", 7361); //the grand tree
		getActionSender().sendString("", 11857); //haunted mine
		getActionSender().sendString("", 7362); //hazeel cult
		getActionSender().sendString("@red@Heroes Quest", 7363); //heroes quest
		getActionSender().sendString("", 7364); //holy grail
		getActionSender().sendString("@red@Horror From The Deep", 10135); //horror from the deep
		getActionSender().sendString("", 4508); //Icthlarin's Little Helper 
		getActionSender().sendString("", 11907); //in search of the myreque
		getActionSender().sendString("", 7365); //jungle potion
		getActionSender().sendString("", 7366);  //legends quest
		getActionSender().sendString("@red@Lost City", 7367); //lost city
		getActionSender().sendString("", 13389); //the lost tribe
		getActionSender().sendString("@red@Merlin's Crystal", 7368); //merlin's crystal
		getActionSender().sendString("", 11132); //monkey madness
		getActionSender().sendString("", 7369); //monk's friend
		getActionSender().sendString("", 12389); //mountain daughter
		getActionSender().sendString("", 13974); //mourning's end pt 1
		getActionSender().sendString("", 7370); //murder mystery
		getActionSender().sendString("@red@Nature Spirit", 8137); //nature spirit
		getActionSender().sendString("", 7371); //observatory quest
		getActionSender().sendString("", 12345); //one small favour
		getActionSender().sendString("@red@Plague City", 7372); //plague city
		getActionSender().sendString("@red@Priest in Peril", 8115); //priest in peril
		// unknown id
		getActionSender().sendString("", 8576); //regicide
		getActionSender().sendString("", 12139); //roving elves
		getActionSender().sendString("", 7373); //scorpion catcher
		getActionSender().sendString("", 7374); //sea slug quest
		getActionSender().sendString("", 8969); //shades of mort'ton
		getActionSender().sendString("", 7375); //sheep herder
		getActionSender().sendString("", 7376); //shilo village
		getActionSender().sendString("", 1740); // tai bwo wannai trio
		getActionSender().sendString("", 3278); //tears of guthix
		getActionSender().sendString("", 7378); //temple of ikov
		getActionSender().sendString("", 6518); //throne of miscellania
		getActionSender().sendString("", 7379); //the tourist trap
		getActionSender().sendString("@red@Tree Gnome Village", 7380); //tree gnome village
		getActionSender().sendString("", 7381); //tribal totem
		getActionSender().sendString("", 11858); //troll romance
		// unknown id
		getActionSender().sendString("", 9927); //underground pass
		getActionSender().sendString("", 7349); //watchtower
		getActionSender().sendString("@red@Waterfall Quest", 7350); //waterfall quest
		getActionSender().sendString("", 7351); //witch's house
		getActionSender().sendString("", 13356); //zogre flesh eaters
		// more
		getActionSender().sendString("", 6024); //wanted!
		getActionSender().sendString("", 191); //troll stronghold
		getActionSender().sendString("", 15235); //a tail of two cats
		getActionSender().sendString("", 249); //swan song
		getActionSender().sendString("", 15592); //spirits of the elid
		getActionSender().sendString("", 15098); //a soul's bane
		getActionSender().sendString("", 15352); //shadow of the storm
		getActionSender().sendString("", 14912); //rum deal
		getActionSender().sendString("@red@Recruitment Drive", 668); //recruitment drive
		getActionSender().sendString("", 18306); //recipe for disaster
		getActionSender().sendString("", 15499); //rat catchers
		getActionSender().sendString("", 18684); //rag and bone man
		getActionSender().sendString("", 6027); //mournings end pt 2
		getActionSender().sendString("", 15487); //making history
		getActionSender().sendString("", 18517); //in aid of the myreque
		getActionSender().sendString("", 16128);//the hand in the sand
		getActionSender().sendString("", 6987);//a fairy tale pt 1
		getActionSender().sendString("", 16149); //enakhra's lament
		getActionSender().sendString("", 15841); //devious minds
		getActionSender().sendString("", 7353); //clock tower
		getActionSender().sendString("", 682); //members quests lel
		getActionSender().sendString("Animal Magnetism", 12772); //the quest formerly known as Between a rock...
		getActionSender().sendString("", 673); //big chompy bird hunting
		getActionSender().sendString("", 17510); //cabin fever
		// unknown id
		getActionSender().sendString("", 7352); //biohazard
		getActionSender().sendString("", 12129); //creature of fenkenstrain
	}

    public boolean getHideYell(){
    	return hideYell;
    }

    public boolean getHideColors()
    {
    	return hideColors;
    }

	public void setHideYell(boolean hide, boolean msg) {
		hideYell = hide;
		if (msg) {
			if (hideYell) {
				getActionSender().sendMessage(
						"You have toggled your yell channel off");
			} else {
				getActionSender().sendMessage(
						"You have toggled your yell channel on");
			}
		}
	}
        
	public void setHideColors(boolean hide, boolean msg) {
		hideColors = hide;
		if (msg) {
			if (hideColors) {
				getActionSender().sendMessage(
						"You have toggled your yell colors off");
			} else {
				getActionSender().sendMessage(
						"You have toggled your yell colors on");
			}
		}
	}

	public void setTimeLoggedOut(String asString) {
		String dateStop = GlobalVariables.datetime.format(new Date());
	    Date d1 = null;
	    Date d2 = null;
	    try {
	        d1 = GlobalVariables.datetime.parse(asString);
	        d2 = GlobalVariables.datetime.parse(dateStop);
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	    long diff = d2.getTime() - d1.getTime();
	    long diffMinutes = diff / (60 * 1000) % 60;
	  //  long diffHours = diff / (60 * 60 * 1000);
	    
	    //System.out.println("Time in minutes: " + diffMinutes + " minutes.");
	    //System.out.println("Time in hours: " + diffHours + " hours.");
	    
	    minloggedout = (int) diffMinutes;
	}
	
	public int getMinLoggedOut()
	{
		return minloggedout;
	}
	
	public void ageCrops(int mintoage)
	{
		System.out.println("Aging crops " + mintoage + "min");
		if(mintoage > 960)
		{
			mintoage = 960;
		}
		for(int k = 0; k <= (mintoage*2); k++)
		{
			getAllotment().processGrowth();
			getFlowers().processGrowth();
			getHerbs().processGrowth();
			getHops().processGrowth();
			getBushes().processGrowth();
			getTrees().processGrowth();
			getFruitTrees().processGrowth();
			getSpecialPlantOne().doCalculations();
			getSpecialPlantTwo().doCalculations();
		}
	}

	public int getDrunkTimer() {
		return drunkTimer;
	}

	public void setDrunkTimer(int drunkTimer) {
		this.drunkTimer = drunkTimer;
	}

	public int getInnoculationBraceletLife() {
		return innoculationBraceletLife;
	}

	public void setInnoculationBraceletLife(int innoculationBraceletLife) {
		this.innoculationBraceletLife = innoculationBraceletLife;
	}

	public ClanChat getClanChat() {
		return currentClanChat;
	}

	public void setClanChat(ClanChat currentClanChat) {
		this.currentClanChat = currentClanChat;
	}

	public RandomHandler getRandomHandler() {
		return randomHandler;
	}
	
	public void setMovementDisabled(boolean state){
		movementDisabled = state;
	}
	
	public boolean getMovementDisabled(){
		return movementDisabled;
	}

}
