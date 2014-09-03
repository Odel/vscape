package com.rs2.model.players;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
import com.rs2.model.Graphic;
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
import com.rs2.model.content.combat.effect.impl.PoisonEffect;
import com.rs2.model.content.combat.hit.Hit;
import com.rs2.model.content.combat.hit.HitDef;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.combat.special.SpecialType;
import com.rs2.model.content.combat.util.BarrowsItems;
import com.rs2.model.content.combat.util.BarrowsItems;
import com.rs2.model.content.combat.util.WeaponDegrading;
import com.rs2.model.content.combat.weapon.Weapon;
import com.rs2.model.content.combat.weapon.CombatSounds;
import com.rs2.model.content.combat.weapon.RangedAmmo;
import com.rs2.model.content.consumables.Food;
import com.rs2.model.content.consumables.Potion;
import com.rs2.model.content.dialogue.DialogueManager;
import com.rs2.model.content.minigames.RuneDraw;
import com.rs2.model.content.minigames.barrows.Barrows;
import com.rs2.model.content.minigames.warriorsguild.WarriorsGuild;
import com.rs2.model.content.minigames.castlewars.CastlewarsPlayer;
import com.rs2.model.content.minigames.duelarena.DuelAreas;
import com.rs2.model.content.minigames.duelarena.DuelInterfaces;
import com.rs2.model.content.minigames.duelarena.DuelMainData;
import com.rs2.model.content.minigames.duelarena.PlayerInteraction;
import com.rs2.model.content.minigames.fightcaves.FightCaves;
import com.rs2.model.content.minigames.fightcaves.WavesHandling;
import com.rs2.model.content.minigames.magetrainingarena.AlchemistPlayground;
import com.rs2.model.content.minigames.magetrainingarena.CreatureGraveyard;
import com.rs2.model.content.minigames.magetrainingarena.EnchantingChamber;
import com.rs2.model.content.minigames.magetrainingarena.TelekineticTheatre;
import com.rs2.model.content.minigames.pestcontrol.*;
import com.rs2.model.content.quests.GhostsAhoy;
import com.rs2.model.content.quests.GhostsAhoyPetition;
import com.rs2.model.content.quests.PiratesTreasure;
import static com.rs2.model.content.quests.PiratesTreasure.BANANA;
import com.rs2.model.content.quests.Quest;
import com.rs2.model.content.randomevents.RandomEvent;
import com.rs2.model.content.randomevents.SpawnEvent;
import com.rs2.model.content.randomevents.SpawnEvent.RandomNpc;
import com.rs2.model.content.randomevents.InterfaceClicking.impl.InterfaceClickHandler;
import com.rs2.model.content.skills.ItemOnItemHandling;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillResources;
import com.rs2.model.content.skills.SkillcapeEmotes;
import com.rs2.model.content.skills.SkillCapeHandler;
import com.rs2.model.content.skills.Woodcutting.Canoe.CanoeStationData;
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
import com.rs2.model.content.skills.runecrafting.Pouches;
import com.rs2.model.content.skills.runecrafting.Tiaras;
import com.rs2.model.content.skills.slayer.Slayer;
import com.rs2.model.content.treasuretrails.ClueScroll;
import com.rs2.model.content.treasuretrails.CoordinateScrolls.CoordinateData;
import com.rs2.model.content.tutorialisland.NewComersSide;
import com.rs2.model.content.tutorialisland.StagesLoader;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcDefinition;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.npcs.drop.NpcDropController;
import com.rs2.model.npcs.drop.NpcDropItem;
import com.rs2.model.objects.GameObject;
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
import com.rs2.model.transport.MagicCarpet;
import com.rs2.model.transport.Sailing;
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
import com.rs2.util.NameUtil;
import com.rs2.util.PlayerSave;
import com.rs2.util.ShutdownWorldProcess;
import com.rs2.util.plugin.LocalPlugin;
import com.rs2.util.plugin.PluginManager;
import com.rs2.util.sql.SQL;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.randomevents.FreakyForester;


import com.rs2.model.content.randomevents.TalkToEvent;
import com.rs2.model.content.skills.runecrafting.TabHandler;
import com.rs2.model.content.treasuretrails.SearchScrolls;
import com.rs2.model.content.treasuretrails.SearchScrolls.SearchData;
import com.rs2.model.tick.TickStopWatch;
import com.rs2.model.tick.TickTimer;

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
	private BankManager bankmanager = new BankManager();
	private PrivateMessaging privateMessaging = new PrivateMessaging(this);
	private Prayer prayer = new Prayer(this);
	private Teleportation teleportation = new Teleportation(this);
	private Emotes emotes = new Emotes(this);
	private SkillcapeEmotes skillcapeEmotes = new SkillcapeEmotes(this);
	private SkillCapeHandler skillcapeHandler = new SkillCapeHandler(this);
	private Skill skill = new Skill(this);
	private ActionSender actionSender = new ActionSender(this);
	private RuneDraw runeDraw = new RuneDraw(this);
	private FreakyForester freakyForester = new FreakyForester(this);
	private GhostsAhoyPetition petition = new GhostsAhoyPetition(this);
	private boolean[] runeDrawWins = {false, false, false};
	private boolean justWonRuneDraw = false;
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
	private PestControl pestControl = new PestControl();
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
	private InterfaceClickHandler randomInterfaceClick = new InterfaceClickHandler(this);
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
	private Container bank = new Container(Type.ALWAYS_STACK, BankManager.SIZE);
	private Container trade = new Container(Type.STANDARD, Inventory.SIZE);
	private boolean pickupItem;
	private int clickX = -1;
	private int clickY = -1;
	private int clickZ = -1;
	private int clickId = -1;
	private int clickItemId = -1;
	private int interfaceId = -1;
	private int slot = -1;
	private int npcClickIndex;
	private boolean withdrawAsNote;
	private int enterXId;
	private int enterXSlot;
	private int enterXInterfaceId;
	private BankOptions bankOptions = BankOptions.SWAP_ITEM;
	private int shopId;
	private boolean isLoggedIn;
	private Map<Integer, Integer> bonuses = new HashMap<Integer, Integer>();
	private long[] friends = new long[200];
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
	private boolean killedJungleDemon;
	private int prayerIcon = -1;
	private int skullIcon = -1;
	private int serverPoints = 0;
	private boolean[] isUsingPrayer = new boolean[18];
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
	private boolean canHaveGodCape = true; //cadillac
	private boolean specialAttackActive = false;
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
	public ArrayList<Position> followPath = new ArrayList<Position>();
	public String statedInterface = "";
	public Npc spawnedNpc;
	private long usernameAsLong;
	private boolean hamTrapDoor;
	private Weapon equippedWeapon = Weapon.FISTS;
	private SpecialType specialType;
	private List<SkullRecord> skullRecords;
	private Spell castedSpell, autoSpell;
	private boolean autoCasting = false;
	private boolean warriorsGuildGameActive;
	private boolean warriorsGuildFirstTime;
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
    private boolean debugCombat;
    private Npc randomEventNpc;
    private Item randomHerb;
    private int genieSelect;
    private boolean hideYell = false;
    private boolean hideColors = false;
    public boolean showHp = false;
	public Object[][] questData = {
	// questName, currentStage, finishedStage, questPoints
	{"Getting Started", 0, 2, 1}};
	public int[] questStage = new int[0];
	
	private int[] sidebarInterfaceId = { 2423, 3917, 638, 3213, 1644, 5608, 0, -1, 5065,
            5715, 2449, 904, 147, 962 };

	// Public ints
	public int moveToX, moveToY, moveToH, objectWalkX, objectWalkY, objectX, objectY, objectXOffset, objectYOffset, objectXSize, objectYSize;

	public Position npcClickingLocation, objectClickingLocation;

	private boolean visible = true;
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
	public Item[] puzzleStoredItems = new Item[ClueScroll.PUZZLE_LENGTH];
	private int tempInteger;
	public boolean isCrossingObstacle = false;
	public int currentSong;
	private int runAnim = -1, standAnim = -1, walkAnim = -1;
	private String host;
    private int sideBarOpen;
    private int[] pinAttempt = new int[4];
    private long logoutTimer;
    private int coalTruckAmount;

	private Player lastPersonTraded;
	private Player lastPersonChallenged;

	public CastlewarsPlayer cwplayer;
	
	private int drunkTimer;
	private boolean isDrunk;
	
	private int pcDamage;
	private int pcPoints;
	private int zamorakCasts;
	private int saradominCasts;
	private int guthixCasts;
	private int mageArenaStage;
	private boolean phoenixGang;
	private boolean blackArmGang;
	private boolean melzarsDoor;
	private boolean bananaCrate;
	private int bananaCrateCount;
	private int[] barrowsHits = new int[24];
	private ArrayList<BoneBurying.Bone> bonesGround = new ArrayList<BoneBurying.Bone>();
	private ArrayList<BoneBurying.Bone> bonesInBin = new ArrayList<BoneBurying.Bone>();
	private boolean bonesGrinded = false;
	private boolean secondTryAtBin = false;
	private int ectoWorshipCount = 0;
	private String topHalfOfGhostsAhoyFlag = "undyed";
	private String bottomHalfOfGhostsAhoyFlag = "undyed";
	private String skullOfGhostsAhoyFlag = "undyed";
	private String desiredTopHalfOfGhostsAhoyFlag = "black";
	private String desiredBottomHalfOfGhostsAhoyFlag = "black";
	private String desiredSkullOfGhostsAhoyFlag = "black";
	private boolean lobsterSpawned = false;
	private boolean petitionSigned = false;
	public CanoeStationData curCanoeStation;
        private String currentChannel = null;
       
	public void resetAnimation() {
		getUpdateFlags().sendAnimation(-1);
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

	/*public Item destroyedBarrow(Item item) {
		if (item != null) {
			for (String s : WeaponDegrading.barrowsEquipments) {
				if (item.getDefinition().getName().toLowerCase().contains(s)) {
					if (item.getDefinition().getName().toLowerCase().contains("100"))
						return new Item(item.getId() + 4, item.getCount());
					if (item.getDefinition().getName().toLowerCase().contains("75"))
						return new Item(item.getId() + 3, item.getCount());
					if (item.getDefinition().getName().toLowerCase().contains("50"))
						return new Item(item.getId() + 2, item.getCount());
					if (item.getDefinition().getName().toLowerCase().contains("25"))
						return new Item(item.getId() + 1, item.getCount());

					return new Item(WeaponDegrading.getFirstDegraded(item.getId()) + 4, item.getCount());
				}
			}
			return item;
		}
		return null;
	}*/

	public void handleDropItems(int amountToDrop, Player player) {
		ArrayList<Item> items = new ArrayList<Item>();
		/*for (int i = 0; i < Inventory.SIZE; i++)
			if (getInventory().getItemContainer().get(i) != null && !ItemManager.getInstance().isUntradeable(getInventory().getItemContainer().get(i).getId()))
				items.add(destroyedBarrow(getInventory().getItemContainer().get(i)));

		for (int i = 0; i < Equipment.SIZE; i++)
			if (getEquipment().getItemContainer().get(i) != null && !ItemManager.getInstance().isUntradeable(getEquipment().getItemContainer().get(i).getId()))
				items.add(destroyedBarrow(getEquipment().getItemContainer().get(i)));
		*/
		if (items.size() == 0)
			return;
		// getting the highest price in the list
		items = getHighestPriceListed(items);
		getInventory().getItemContainer().clear();
		getInventory().refresh();
		getEquipment().getItemContainer().clear();
		getEquipment().refresh();
		if (amountToDrop != 0)
			for (int i = 0; i < amountToDrop; i++)
				getInventory().addItem(items.get(i));
		for (int i = amountToDrop; i < items.size(); i++) {
			ItemManager.getInstance().createGroundItem(player, items.get(i), new Position(getPosition().getX(), getPosition().getY(), getPosition().getZ()));
		}
		player.getActionSender().sendSound(376, 0, 0);
	} 

	public ArrayList<Item> getHighestPriceListed(ArrayList<Item> array) {
		ArrayList<Item> tempArray = array;
		ArrayList<Item> listedArray = new ArrayList<Item>();
		Item highest;
		for (int i = 0; i < tempArray.size(); i++) {
			highest = new Item(0);
			for (Item items : tempArray) {
				if (items == null)
					continue;
				if (items.getDefinition().getPrice() >= highest.getDefinition().getPrice())
					highest = items;
			}
			tempArray.remove(highest);
			listedArray.add(highest);
		}
		return listedArray;
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
		if (dispatch) {
			PacketManager.handlePacket(this, p);
		}
        PacketManager.packetBenchmarks[p.getOpcode()].stop();

	}

	public boolean timeOutCheck() {
		// If no packet for more than 5 seconds, disconnect.
		if (isLoggedIn() && getTimeoutStopwatch().elapsed() > 5000 && this.getStaffRights() < 2) {
			disconnect();
			return true; // true;
		}
		return false;
	}

	public void send(ByteBuffer buffer) {
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

    public void disconnect() {
        if (loginStage.compareTo(LoginStages.LOGGED_IN) > 0)
            return;
        if(inPestControlLobbyArea())
        {
        	PestControl.leaveLobby(this);
        }
	else if(inPestControlGameArea())
        {
        	PestControl.leaveGame(this);
        }
	else if(inWarriorGuildArena()) {
	    WarriorsGuild.exitArena(this, true);
	}
	else if(inFightCaves()) {
	    //FightCaves.exitCave(this);
	    FightCaves.destroyNpcs(this);
	}
	else if(!this.getInCombatTick().completed() && !this.inFightCaves()) {
	    Entity attacker = this.getInCombatTick().getOther();
	    if(attacker != null && attacker.isNpc()) {
		Npc npc = (Npc)attacker;
		npc.reset();
		npc.resetActions();
		npc.setCombatDelay(5);
		npc.walkTo(npc.getSpawnPosition() == null ? npc.getPosition().clone() : npc.getSpawnPosition().clone(), true);
	    }
	    else if(attacker != null && attacker.isPlayer()) {
		attacker.setCombatingEntity(null);
		attacker.getMovementHandler().reset();
	    }
	}
	RandomEvent.resetEvents(this);
	setLogoutTimer(System.currentTimeMillis() + 1000); //originally 600000
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

    private long lastYell;
    private long lastReport;

	@Override
	public void process() {
		/*if (timeOutCheck()) {
			return;
		}*/
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
		// Npc.checkAggressiveness(this);
	}

	/**
	 * Adds to the Players position.
	 */
	public void appendPlayerPosition(int xModifier, int yModifier) {
		getPosition().move(xModifier, yModifier);
	}
	
	/**
	 * Handles a player command.
	 * 
	 * @param keyword
	 *            the command keyword
	 * @param args
	 *            the arguments (separated by spaces)
	 */
	public void handleCommand(String keyword, String[] args, String fullString) {
		keyword = keyword.toLowerCase();

		if (getStaffRights() >= 0) {
			playerCommands(keyword, args, fullString);
		}
		if (getStaffRights() >= 1) {
			modCommands(keyword, args, fullString);
		}
		if (getStaffRights() >= 2) {
			adminCommands(keyword, args, fullString);
		}
	}

	public void playerCommands(String keyword, String[] args, String fullString) {
		if (keyword.equals("outfit")) {
			getActionSender().sendInterface(3559);
		}
		else if ( keyword.equals("highscores") || keyword.equals("highscore") || keyword.equals("hs") || keyword.equals("hiscores") )
		{
	        if(Constants.SQL_ENABLED)
	        {
			try {
				ResultSet rs = SQL.query("SELECT * FROM `skillsoverall` ORDER BY totallevel DESC LIMIT 50;");
				getActionSender().sendInterface(8134);
				ClearNotes();
				this.getActionSender().sendString("@dre@-=The /v/scape no-life elite=-", 8144);
				int line = 8147;
				while ( rs.next() ) {
					String  name = rs.getString("playerName");
					if( !name.equals("Quietessdick")  && !name.equals("Bobsterdebug") && !name.equals("Noiryx") && !name.equals("Pickles") && !name.equals("Mrsmeg")  && !name.equals("Mr telescope") && !name.equals("Shark") && !name.equals("Mr foxter") && !name.equals("Mr_foxter"))
					{
						int lv  = rs.getInt("totallevel");
						this.getActionSender().sendString(name + " - level " + lv, line);
						line++;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        }
		}
		else if (keyword.equals("panic") || keyword.equals("helpme")) {
			if (args.length < 1) {
				actionSender.sendMessage("Please use ::panic/::helpme (reason) (can be multiple words).");
				return;
			}
			if(System.currentTimeMillis() - lastReport < 1800000) {
				getActionSender().sendMessage("You can only report or ask for assistance once every 30 minutes!");
				return;
			}
			lastReport = System.currentTimeMillis();
			World.messageToStaff("@blu@"+ getUsername() + " paniced because:" + fullString + "!");
			getActionSender().sendMessage("A message for assistance has been sent to the staff.");
		}
		else if (keyword.equals("report")) {
			if (args.length < 2) {
				actionSender.sendMessage("Please use ::report username reason (reason can be multiple words).");
				return;
			}
			String name = args[0];
			Player player = World.getPlayerByName(name);
			if (player == null) {
                getActionSender().sendMessage("Cannot report an offline player.");
                return;
            }
			if( player.getUsername() == getUsername()){
				getActionSender().sendMessage("You can't report yourself, silly.");
				return;
			}
            if(System.currentTimeMillis() - lastReport < 1800000) {
				getActionSender().sendMessage("You can only report or ask for assistance once every 30 minutes!");
				return;
			}
            lastReport = System.currentTimeMillis();
			getActionSender().sendMessage("A message has been sent to staff about your report.");
			World.messageToStaff("@dre@"+getUsername() + " has reported " + player.getUsername() + " for "+ fullString);
		}
		else if (keyword.equals("yell") || keyword.equals("y")) {
			Yell(fullString);
		}
		else if (keyword.equals("hideyell") || keyword.equals("hy")) {
			setHideYell(!hideYell,true);
		}    
		else if (keyword.equals("hidecolor")  || keyword.equals("hc") ) {
			setHideColors(!hideColors,true);
		}
		else if (keyword.equals("bugreport") || keyword.equals("bug")){
			if (args.length < 2) {
				getActionSender().sendMessage("Please write more than two words.");
				return;
			}
			if(System.currentTimeMillis() - lastReport < 1800000) {
				getActionSender().sendMessage("You can only report, ask for assistance or report a bug once every 30 minutes!");
				return;
			}
			lastReport = System.currentTimeMillis();
			getActionSender().sendMessage("The bug has been reported. Thank you!");
			appendToBugList(fullString);
		}
		else if (keyword.equals("home")) {
            if (inWild() || isAttacking() || inDuelArena() || inPestControlLobbyArea() || inPestControlGameArea() || isDead() || !getInCombatTick().completed() || inFightCaves()) {
                getActionSender().sendMessage("You cannot do that here!");
            } else {
                teleport(new Position(Constants.START_X, Constants.START_Y, 0));
                getActionSender().sendMessage("You teleported home.");
            } 
		}
		else if (keyword.equals("showhp")) {
			setShowHp(!showHp,true);
                }
		else if (keyword.equals("players")) {
			getActionSender().sendMessage("There are currently "+World.playerAmount()+ " players online.");
			getActionSender().sendInterface(8134);
			ClearNotes();
			getActionSender().sendString(Constants.SERVER_NAME+" - Player List", 8144);
			getActionSender().sendString("@dbl@Online players(" +World.playerAmount()+ "):", 8145);
			int line = 8147;
			for (Player player : World.getPlayers()) {
				if(player != null)
				{	
					if(line > 8195 && line < 12174)
					{
						line = 12174;
					}
					getActionSender().sendString("@dre@"+player.getUsername(), line);
					line++;
				}
			}
		}
		else if (keyword.equals("changepass")) {
					String pass = fullString;
                        if(pass.length() > 20){ 
                            getActionSender().sendMessage("Your password is too long! 20 characters maximum."); 
                        } else if(pass.equals("changepass")){ 
                            getActionSender().sendMessage("Please input a password.");
                        } else if(pass.length() < 4){ 
                            getActionSender().sendMessage("Your password is too short! 4 characters required.");  
                        } else if (getUsername().equals("Community")) {
                            getActionSender().sendMessage("You cannot change the community account's password!");
						} else {
                            setPassword(pass);// setPassword
                            getActionSender().sendMessage("Your new password is " + pass + ".");
			}
		}
		else if (keyword.equals("removemypin")) {
			if (getUsername().equals("Community"){
		    getBankPin().deleteBankPin();
			getActionSender().sendMessage("Community bankpin deleted.");		
			}
		}
		else if (keyword.equals("patchnotes")) {
			getActionSender().sendInterface(8134);
			ClearNotes();	
			this.getActionSender().sendString("@dre@-=vscape patch notes=-", 8144);
			int line = 8147;
			for(String q: GlobalVariables.patchNotes)
			{
				if(q!=null)
				{	
					if(line > 8195 && line < 12174) {
					    line = 12174;
					}
					this.getActionSender().sendString(q, line);
					line++;
				}
			}
		}
		else if (keyword.equals("info")) {
			info();
		}
        else if (keyword.equals("fox")) {
        transformNpc = 1319;
        setStandAnim(6561);
        setWalkAnim(6560);
        setRunAnim(6560);
        setAppearanceUpdateRequired(true);
		getUpdateFlags().setForceChatMessage("Yiff!");
		}
		else if(keyword.equals("resetpet")) {
			this.getPets().unregisterPet();
		}
		else if(keyword.equals("pcpoints")) {
			getActionSender().sendMessage("You have " + this.getPcPoints() + " commendation points." );
		}
		else if(keyword.equals("pc")) {
			World.messageToPc(this, fullString);
		}
		else if(keyword.equals("pcactive")) {
		    if(PestControl.gameActive())
			getActionSender().sendMessage("There is an active Pest Control game with " +PestControl.playerCount() + " players playing.");
		    else {
			getActionSender().sendMessage("Pest Control is not running at the moment.");
		    }
		}
		else if(keyword.equals("resetcaves")) {
		    this.setFightCavesWave(0);
		}
		else if (keyword.equals("maxqp")) {
		    int x = 0;
		    for(int i = 0; i < QuestHandler.getQuests().length; i++) {
			x += QuestHandler.getQuests()[i].getQuestPoints();
		    }
		    this.getActionSender().sendMessage("The total possible quest points is: " + (x - 1) + "."); //Minus one for the easter event's class
		}
		
	
	}

	public void modCommands(String keyword, String[] args, String fullString) {
		String name = fullString;
		 if (keyword.equals("closeinterface")) {
            Player player = World.getPlayerByName(fullString);
            if (player == null)
                return;
            player.getActionSender().removeInterfaces();
        }
		else if (keyword.equals("clan") || keyword.equals("c")) {
			Clan(fullString);
		}
		else if (keyword.equals("joinclan") || keyword.equals("jc")) {
			setClanChannel(fullString);
		}
        else if (keyword.equals("leaveclan") || keyword.equals("lc")){
                        leaveClanChannel();
        }        
		else if (keyword.equals("modban")) {
        	ModBan(args);
        }  else if (keyword.equals("kick")) {
            Player player = World.getPlayerByName(fullString);
            if (player == null)
                return;
            player.disconnect();
            actionSender.sendMessage("You have kicked "+player.getUsername());
        }  	else if (keyword.equals("staff") || keyword.equals("s")) {
	    World.messageToStaff(this, fullString);
		}	else if (keyword.equals("mute")) {
            if (args.length < 2) {
                actionSender.sendMessage("::mute hours username");
                return;
            }
            int hours = Integer.parseInt(args[0]);
            int maxHours = getStaffRights() == 1 ? 24 : 100;
            if (hours <= 0 || hours > maxHours) {
                actionSender.sendMessage("Mute between 0 and "+maxHours+" hours");
                return;
            }
            name = "";
            for (int i = 1; i < args.length; i++) {
                name += args[i];
            }
            Player player = World.getPlayerByName(name);
            if (player == null) {
                actionSender.sendMessage("Could not find "+name);
                return;
            }
            if (player.isMuted()) {
                actionSender.sendMessage("Player is already muted");
                return;
            }
            try {
                OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("./data/modlog.out", true));
                out.write(player.getUsername()+" was muted by "+username+" for "+hours+" hours.");
                out.flush();
                out.close(); //CLOSEFILE
            } catch (FileNotFoundException e) {
                e.printStackTrace(); 
            } catch (IOException e) {
                e.printStackTrace();
            }
            actionSender.sendMessage("Muted "+player.getUsername()+" for "+hours+" hours.");
            player.actionSender.sendMessage("You have been muted for "+hours+" hours");
            player.setMuteExpire(System.currentTimeMillis()+(hours*60*60*1000));
        }
		if(keyword.equals("bankof")) {
			for (Player player : World.getPlayers()) {
				if (player == null)
					continue;
				if (player.getUsername().equalsIgnoreCase(fullString)) {
				    getActionSender().sendInterface(8134);
				    ClearNotes();
				    getActionSender().sendString("Bank of: " + player.getUsername(), 8144);
				    int line = 8145;
				    for (int counter = 0; counter < player.getBank().getItems().length; counter = counter + 2) {
					    if (line > 8195 && line < 12174) {
						line = 12174;
					    }
					    Item item1 = player.getBank().getItems()[counter];
					    Item item2 = player.getBank().getItems()[counter+1];
					    if(item1 == null) {
						counter++;
						continue;
					    }
					    getActionSender().sendString(item1.getDefinition().getName() + "[ " + item1.getCount() + " ]  " + item2.getDefinition().getName() + "[ " + item2.getCount() + " ]", line);
					    line++;
				    }
				    break;
				}
			}
		}
		if(keyword.equals("bank2of")) {
			for (Player player : World.getPlayers()) {
				if (player == null)
					continue;
				if (player.getUsername().equalsIgnoreCase(fullString)) {
				    getActionSender().sendInterface(8134);
				    ClearNotes();
				    getActionSender().sendString("Bank part two of: " + player.getUsername(), 8144);
				    int line = 8145;
				    for (int counter = 0; counter < player.getBank().getItems().length; counter = counter + 2) {
					if(counter < 200) continue;
					else {
					    if (line > 8195 && line < 12174) {
						line = 12174;
					    }
					    Item item1 = player.getBank().getItems()[counter];
					    Item item2 = player.getBank().getItems()[counter+1];
					    getActionSender().sendString(item1.getDefinition().getName() + "[ " + item1.getCount() + " ]  " + item2.getDefinition().getName() + "[ " + item2.getCount() + " ]", line);
					    line++;
					}
				    }
				    break;
				}
			}
		}
	}

	public void adminCommands(String keyword, String[] args, String fullString) {
		saveCommand(getUsername(), keyword+" "+fullString);
		if (keyword.equals("getrandom")) {
			switch(Misc.random(5)) {
				case 0 :
					SpawnEvent.spawnNpc(this, RandomNpc.EVIL_CHICKEN);
					break;
				case 1 :
					SpawnEvent.spawnNpc(this, RandomNpc.RIVER_TROLL);
					break;
				case 2 :
					SpawnEvent.spawnNpc(this, RandomNpc.ROCK_GOLEM);
					break;
				case 3 :
					SpawnEvent.spawnNpc(this, RandomNpc.SHADE);
					break;
				case 4 :
					SpawnEvent.spawnNpc(this, RandomNpc.TREE_SPIRIT);
					break;
				case 5 :
					SpawnEvent.spawnNpc(this, RandomNpc.ZOMBIE);
					break;
			}
		}
		/*if (keyword.equals("highscoresinit"))
		{
			SQL.initHighScores();
		}*/
		if (keyword.equals("resettask")) {
            Player player = World.getPlayerByName(fullString);
            if (player == null)
                return;
            player.slayer.resetSlayerTask();
            actionSender.sendMessage("You have reset the slayer task for "+player.getUsername());
		}
		if (keyword.equals("forcerandom")) {
			getActionSender().sendMessage("Sent random to " + args[0].toLowerCase());
			for (Player player : World.getPlayers()) {
				if (player == null)
					continue;
				if (player.getUsername().equalsIgnoreCase(fullString)) {
					    switch(Misc.random(3)) {
						case 0 :
						    TalkToEvent.spawnNpc(player, TalkToEvent.TalkToNpc.DRUNKEN_DWARF);
						    break;
						case 1 :
						    TalkToEvent.spawnNpc(player, TalkToEvent.TalkToNpc.GENIE);
						    break;
						case 2 :
						    TalkToEvent.spawnNpc(player, TalkToEvent.TalkToNpc.JEKYLL);
						    break;
						//case 3 :
						    //TalkToEvent.spawnNpc(this, TalkToEvent.TalkToNpc.RICK);
						    //break;
						case 3 :
						    player.getRandomInterfaceClick().sendEventRandomly();
						    break;
					    }
					return;
				}
			}
			getActionSender().sendMessage("The player is not online at the moment.");
		}
		if (keyword.equals("coordinate")) {
			final int id = Integer.parseInt(args[0]);
			CoordinateData clue = CoordinateData.forIdClue(id);
			actionSender.sendMessage(clue.getDiggingPosition().getX()+" "+clue.getDiggingPosition().getY());
		}
		else if (keyword.equals("usa")) { //4th of july command
			getUpdateFlags().sendAnimation(2106, 0); //animation
			Graphic graphic = new Graphic(199, 100); //gfx part
			getUpdateFlags().sendGraphic(graphic.getId(), graphic.getValue()); //gfx part2
			getUpdateFlags().setForceChatMessage("U S A! U S A! U S A!"); //Message
		}
		else if (keyword.equals("forcefox")) {
			String name = fullString;
			getActionSender().sendMessage("You have foxed " +
			args[0].toLowerCase() + ". Yiff!"); 
			for (Player player : World.getPlayers()) { 
					if (player == null) continue; 
					if(player.getUsername().equalsIgnoreCase(name)) {
						player.transformNpc = 1319;
						player.setStandAnim(6561);
						player.setWalkAnim(6560);
						player.setRunAnim(6560);
						player.setAppearanceUpdateRequired(true);
						player.getUpdateFlags().setForceChatMessage("Yiff!");
						player.getActionSender().sendMessage("You have been foxed. Good luck yiffing in hell!"); 
					return;
                  } 
               }
			getActionSender().sendMessage("Player offline or not found."); 
			}
		else if (keyword.equals("sayit")) {
		    String whattosay = fullString.substring(0, fullString.indexOf("-")-1);
		    String name = fullString.substring(fullString.indexOf("-")+1);
		    long nameLong = NameUtil.nameToLong(NameUtil.uppercaseFirstLetter(name));
		    Player player = World.getPlayerByName(nameLong);
			player.getUpdateFlags().setForceChatMessage(whattosay);
		    if(player != null) {
			this.getActionSender().sendMessage("Made " + player.getUsername() +" say something.");
		    }
		    else {
			this.getActionSender().sendMessage("Could not find player.");
		    }
		}
		else if (keyword.equals("teletoclue")) {
			try {
				final int id = Integer.parseInt(args[0]);
				CoordinateData clue = CoordinateData.forIdClue(id);
				int x = clue.getDiggingPosition().getX();
				int y = clue.getDiggingPosition().getY();
				teleport(new Position(x, y, getPosition().getZ()));
				getActionSender().sendMessage("You teleported to clue:"+id+" at " + x + ", " + y + ", " + getPosition().getZ());
			} catch (Exception e1) {
				final int id = Integer.parseInt(args[0]);
				if(SearchScrolls.SearchData.forIdClue(id).getClueId() == id) {
				    teleport(SearchScrolls.SearchData.forIdClue(id).getObjectPosition().clone());
				}
				else {
				    getActionSender().sendMessage("Please use ::teletoclue clueid. If you did, an error occured, sorry.");
				}
			}
		}
	
		//sound debug
		if (keyword.equals("sound")) {
			final int id = Integer.parseInt(args[0]);
			getActionSender().sendSound(id, 0, 0);
		}
		if (keyword.equals("quicksong")) {
			final int id = Integer.parseInt(args[0]);
			final int delay = Integer.parseInt(args[1]);
			getActionSender().sendQuickSong(id, delay);
		}
		if (keyword.equals("music")) {
			final int id = Integer.parseInt(args[0]);
			getActionSender().sendSong(id);
		}
		if(keyword.equals("testinterfaceitem")) {
		    final int item = Integer.parseInt(args[0]);
		    final int line = Integer.parseInt(args[1]);
		    getActionSender().sendItemOnInterface(item, 200, line);
		}
		if(keyword.equals("setqueststage") || keyword.equals("queststage")) {
		    final int quest = Integer.parseInt(args[0]);
		    final int stage = Integer.parseInt(args[1]);
		    String name = fullString.substring(fullString.indexOf("-")+1);
		    long nameLong = NameUtil.nameToLong(NameUtil.uppercaseFirstLetter(name));
		    Player player = World.getPlayerByName(nameLong);
		    if(!fullString.contains("-")) {
			this.setQuestStage(quest, stage);
			QuestHandler.getQuests()[quest].sendQuestTabStatus(this);
			getActionSender().sendMessage("Set " +QuestHandler.getQuests()[quest].getQuestName() + " to stage " + stage + ".");
		    }
		    else {
			try {
			    player.setQuestStage(quest, stage);
			    QuestHandler.getQuests()[quest].sendQuestTabStatus(player);
			    getActionSender().sendMessage("Set " + player.getUsername() + "'s " +QuestHandler.getQuests()[quest].getQuestName() + " to stage " + stage + ".");
			}
			catch(Exception e) {
			    this.getActionSender().sendMessage("Could not find player.");
			}
		    }
		}
		if(keyword.equals("getplayerquestpoints") || keyword.equals("getplayerqp") || keyword.equals("getqp")) {
		    String name = fullString;
		    long nameLong = NameUtil.nameToLong(NameUtil.uppercaseFirstLetter(name));
		    Player player = World.getPlayerByName(nameLong);
		    this.getActionSender().sendMessage("That player has " + player.getQuestPoints() + " quest points.");
		}
		else if (keyword.equals("playersquestpoints")) {
			getActionSender().sendMessage("There are currently "+World.playerAmount()+ " players online.");
			getActionSender().sendInterface(8134);
			ClearNotes();
			getActionSender().sendString(Constants.SERVER_NAME+" - Player List", 8144);
			getActionSender().sendString("@dbl@Online players(" +World.playerAmount()+ "):", 8145);
			int line = 8147;
			for (Player player : World.getPlayers()) {
				if(player != null)
				{	
					if(line > 8195 && line < 12174)
					{
						line = 12174;
					}
					getActionSender().sendString("@dre@"+player.getUsername()+ " - QP: " + player.getQuestPoints(), line);
					line++;
				}
			}
		}
		if(keyword.equals("setplayerquestpoints") || keyword.equals("setplayerqp")) {
		    final int qp = Integer.parseInt(args[0]);
		    String name = fullString.substring(fullString.indexOf("-")+1);
		    long nameLong = NameUtil.nameToLong(NameUtil.uppercaseFirstLetter(name));
		    Player player = World.getPlayerByName(nameLong);
		    player.setQuestPoints(qp);
		    this.getActionSender().sendMessage("That player now has " +qp+ " quest points.");
		}
		if(keyword.equals("getexp") || keyword.equals("getlevel") || keyword.equals("getstat")) {
		    final int skill = Integer.parseInt(args[0]);
		    String name = fullString.substring(fullString.indexOf("-")+1);
		    long nameLong = NameUtil.nameToLong(NameUtil.uppercaseFirstLetter(name));
		    Player player = World.getPlayerByName(nameLong);
		    final double exp = player.getSkill().getExp()[skill];
		    if(player != null) {
			this.getActionSender().sendMessage("That player has " + exp + " experience in " + Skill.SKILL_NAME[skill] + " and is level " + player.getSkill().getLevelForXP(exp) + ".");
		    }
		    else {
			this.getActionSender().sendMessage("Could not find player.");
		    }
		}
		
		if(keyword.equals("subtractexp")) {
		    final int skill = Integer.parseInt(args[0]);
		    final int exp = Integer.parseInt(args[1]);
		    String name = fullString.substring(fullString.indexOf("-")+1);
		    long nameLong = NameUtil.nameToLong(NameUtil.uppercaseFirstLetter(name));
		    Player player = World.getPlayerByName(nameLong);
		    player.getSkill().subtractExp(skill, exp/2.25);
		    if(player != null) {
			this.getActionSender().sendMessage("Subtracted " + exp + " from " + player.getUsername() +"'s " + Skill.SKILL_NAME[skill] + ".");
		    }
		    else {
			this.getActionSender().sendMessage("Could not find player.");
		    }
		}
		if(keyword.equals("addexp")) {
		    final int skill = Integer.parseInt(args[0]);
		    final int exp = Integer.parseInt(args[1]);
		    String name = fullString.substring(fullString.indexOf("-")+1);
		    long nameLong = NameUtil.nameToLong(NameUtil.uppercaseFirstLetter(name));
		    Player player = World.getPlayerByName(nameLong);
		    player.getSkill().addExp(skill, exp/2.25);
		    if(player != null) {
			this.getActionSender().sendMessage("Added " + exp + " to " + player.getUsername() +"'s " + Skill.SKILL_NAME[skill] + ".");
		    }
		    else {
			this.getActionSender().sendMessage("Could not find player.");
		    }
		}
		
		if (keyword.equals("run")) {
			final int id = Integer.parseInt(args[0]);
			setRunAnim(id);
			setAppearanceUpdateRequired(true);
		}
		if (keyword.equals("walk")) {
			final int id = Integer.parseInt(args[0]);
			setWalkAnim(id);
			setAppearanceUpdateRequired(true);
		}
		if (keyword.equals("stand")) {
			final int id = Integer.parseInt(args[0]);
			setStandAnim(id);
			setAppearanceUpdateRequired(true);
		}
		if (keyword.equals("poison")) {
			HitDef hitDef = new HitDef(null, HitType.POISON, Math.ceil(4.0)).setStartingHitDelay(1);
			Hit hit = new Hit(this, this, hitDef);
			PoisonEffect p = new PoisonEffect(4.0, false);
			p.initialize(hit);
		}
        else if (keyword.equals("dbhs")) {
            HighscoresManager.debug = !HighscoresManager.debug;
            getActionSender().sendMessage("Highscores debug mode: " + HighscoresManager.debug);
        }
		else if (keyword.equals("empty")) {
	    getInventory().getItemContainer().clear();
	    getInventory().refresh();
		}
		else if (keyword.equals("hsstatus")) {
            getActionSender().sendMessage("Highscores are "+(HighscoresManager.running ? "running" : "stopped")+" "+(HighscoresManager.debug ? "in debug mode" : ""));
        }
		else if (keyword.equals("rebooths")) {
            HighscoresManager.running = !HighscoresManager.running;
            getActionSender().sendMessage("Highscores are "+(HighscoresManager.running ? "running" : "stopped")+" "+(HighscoresManager.debug ? "in debug mode" : ""));
        }
        else if (keyword.equals("fox")) {
        transformNpc = 1319;
        setStandAnim(6561);
        setWalkAnim(6560);
        setRunAnim(6560);
        setAppearanceUpdateRequired(true);
	getUpdateFlags().setForceChatMessage("Yiff!");
		}
		else if (keyword.equals("uptime")) {
            getActionSender().sendMessage("Server has been online for: " + Misc.durationFromTicks(World.SERVER_TICKS, false));
        }
        else if (keyword.equals("save")) {
	    PlayerSave.saveAllPlayers();
	    actionSender.sendMessage("Saved players.");
        }
		else if (keyword.equals("forcespace")) { 
	   	 String name = fullString;
		 getActionSender().sendMessage("You have sent " +
		 args[0].toLowerCase() + " to space."); 
		 for (Player player : World.getPlayers()) { 
		 if (player == null) continue; 
		 if(player.getUsername().equalsIgnoreCase(name)) {
		 player.teleport(new Position(3108, 3954));
		 player.getActionSender().sendMessage
		 ("You have been sent to space. Good luck escaping!"); return;
			} 
		 }
		 getActionSender
		 ().sendMessage("Player offline or not found."); 
		}
		else if (keyword.equalsIgnoreCase("fillspec")) {
			setSpecialAmount(100);
			updateSpecialBar();
		}
		else if (keyword.equals("master")) {
			for (int i = 0; i < skill.getLevel().length; i++) {
				skill.getLevel()[i] = 99;
				skill.getExp()[i] = 200000000;
			}
			skill.refresh();
		}
		else if (keyword.equals("resetstats")) {
			for (int i = 0; i < skill.getLevel().length; i++) {
				if(i == 3)
				{
					skill.getLevel()[i] = 10;
					skill.getExp()[i] = skill.getXPForLevel(9);
				}
				else
				{
					skill.getLevel()[i] = 1;
					skill.getExp()[i] = skill.getXPForLevel(0);
				}
			}
			skill.refresh();
		}
		else if (keyword.equals("changeuser")) {
		    String name = args[0];
		    String newName = args[1];
		    Player player = World.getPlayerByName(name);
		    if(player == null ) {
			this.getActionSender().sendMessage("Could not find player.");
			return;
		    }
		    player.setUsername(newName);
		    this.getActionSender().sendMessage("Set " + name +"'s username to: " + newName + " .");
		}
		else if (keyword.equals("forester")) {
		    this.getFreakyForester().spawnForester();
		}
		else if (keyword.equals("playerdump") || keyword.equals("dump")) {
		    String name = fullString;
		    Player player = World.getPlayerByName(name);
			if (player == null) {
			    this.actionSender.sendMessage("Cannot find player: "+name);
			    return;
			}
			BufferedWriter file = null;
			try {
			    file = new BufferedWriter(new FileWriter("./data/characters/"+player.getUsername()+"dump.txt"));
			    for(int i = 0; i < 21; i++) {
				file.write(Skill.SKILL_NAME[i] + " lvl = ", 0, Skill.SKILL_NAME[i].length() + 7);
				file.write(Integer.toString(player.getSkill().getLevel()[i]), 0, Integer.toString(player.getSkill().getLevel()[i]).length());
				file.newLine();
				file.write("Exp = ", 0, 6);
				file.write(Integer.toString((int)player.getSkill().getExp()[i]), 0, Integer.toString((int)player.getSkill().getExp()[i]).length());
				file.newLine();
				file.newLine();
			    }
			    file.newLine();
			    file.close();
			    this.getActionSender().sendMessage("Dumping complete.");
			}
			catch (IOException e) {
			    this.getActionSender().sendMessage("Error dumping player information.");
			}
		}
		else if (keyword.equals("poisondump")) {
		    bank.add(new Item(PiratesTreasure.CLEANING_CLOTH), 25);
		    PiratesTreasure.dumpAllPoisonedItems(this);
		}
		else if (keyword.equals("dyedump")) {
		    inventory.addItem(new Item(GhostsAhoy.RED_DYE));
		    inventory.addItem(new Item(GhostsAhoy.YELLOW_DYE));
		    inventory.addItem(new Item(GhostsAhoy.BLUE_DYE));
		    inventory.addItem(new Item(GhostsAhoy.ORANGE_DYE));
		    inventory.addItem(new Item(GhostsAhoy.PURPLE_DYE));
		    inventory.addItem(new Item(GhostsAhoy.GREEN_DYE));
		}
		else if (keyword.equals("enchantdump")) {
		    bank.add(new Item(8016, 100));
		    bank.add(new Item(8017, 100));
		    bank.add(new Item(8018, 100));
		    bank.add(new Item(8019, 100));
		    bank.add(new Item(8020, 100));
		    bank.add(new Item(8021, 100));
		    for(int i = 0; i < 6; i++) {
			for(int x = 0; x < 4; x++) {
			    if(TabHandler.ENCHANTABLES[i][x] == -1) continue;
			    bank.add(new Item(TabHandler.ENCHANTABLES[i][x]));
			}
		    }
		}
		else if (keyword.equals("debugcombat")) {
		    this.debugCombat = true;
		    this.getActionSender().sendMessage("Starting accuracy messages for combat debugging.");
		}
		else if (keyword.equals("stopdebugcombat")) {
		    this.debugCombat = false;
		    this.getActionSender().sendMessage("Stopping accuracy messages for combat debugging.");
		}
		else if (keyword.equals("pnpc")) {
			final int npcId = Integer.parseInt(args[0]);
			transformNpc = npcId;
			setAppearanceUpdateRequired(true);
			setSize(new Npc(npcId).getDefinition().getSize());
			getActionSender().sendMessage("NPC #" + npcId);
		}
		else if (keyword.equals("rnpc")) {
			int npcId = (int)Misc.random(6390);
			if(GlobalVariables.npcDump[npcId].toLowerCase().contains("null")) {
			    getActionSender().sendMessage("Whoops! You landed on a null npc. Please try again.");
			    return;
			}
			transformNpc = npcId;
			setAppearanceUpdateRequired(true);
			setSize(new Npc(npcId).getDefinition().getSize());
			getActionSender().sendMessage("NPC #" + npcId);
		}
		else if (keyword.equals("pet")) {
                final int petId = Integer.parseInt(args[0]);
                this.getPets().registerPet(-1, petId);
                	if ( petId == 1319)
                            this.getPets().getPet().getUpdateFlags().setForceChatMessage("Yiff!");
		}
		else if (keyword.equals("talkpet") || keyword.equals("tp")) {
		    this.getPets().getPet().getUpdateFlags().sendForceMessage(fullString);
		}
		else if (keyword.equals("invisible") || keyword.equals("invis")) {
			visible = !visible;
			getActionSender().sendMessage("Invisible: " + !visible);
		}
		else if (keyword.equals("scan")) {
			for (int i = 0; i < Constants.MAX_NPC_ID; i++) {
				for (NpcDropItem item : NpcDropController.forId(i).getDropList()) {
					for (int c : item.getCount()) {
						if (c > 1000) {
							System.out.println(c);
						}
					}
				}
			}
		}
		else if (keyword.equals("bank")) {
			BankManager.openBank(this);
		}
		else if (keyword.equals("npc")) {
			int npcId = Integer.parseInt(args[0]);
			if(GlobalVariables.npcDump[npcId].toLowerCase().contains("null")) {
			    getActionSender().sendMessage("Whoops! You landed on a null npc. Please try again.");
			    return;
			}
			Npc npc = new Npc(npcId);
			npc.setPosition(new Position(getPosition().getX(), getPosition().getY(), getPosition().getZ()));
			npc.setSpawnPosition(new Position(getPosition().getX(), getPosition().getY(), getPosition().getZ()));
			npc.setCurrentX(getPosition().getX());
			npc.setCurrentY(getPosition().getY());
			World.register(npc);
			getActionSender().sendMessage("You spawn NPC #" + npcId);
			setLastNpc(npc.getNpcId());
		}
        else if (keyword.equals("tfra")) {
            actionSender.sendFrame106(Integer.parseInt(args[0]));

        }
		else if (keyword.equals("removenpc")) {
			for (Npc npc : World.getNpcs()) {
				if (npc != null) {
					if (npc.getPosition().equals(getPosition())) {
						getActionSender().sendMessage("You remove NPC #" + npc.getNpcId());
						npc.setVisible(false);
						World.unregister(npc);
						break;
					}
				}
			}
		}
		else if (keyword.equals("object")) {
			int objectId = Integer.parseInt(args[0]);
			int face = args.length > 1 ? Integer.parseInt(args[1]) : 0;
			int type = args.length > 2 ? Integer.parseInt(args[2]) : 10;
			new GameObject(objectId, getPosition().getX(), getPosition().getY(), getPosition().getZ(), face, type, 0, 999999, false);
		}
		else if (keyword.equals("item")) {
			int id = Integer.parseInt(args[0]);
			int amount = 1;
			if (args.length > 1) {
				amount = Integer.parseInt(args[1]);
				amount = amount > Constants.MAX_ITEM_COUNT ? Constants.MAX_ITEM_COUNT : amount;
			}
			inventory.addItem(new Item(id, amount));
			getActionSender().sendMessage("You spawn a " + new Item(id).getDefinition().getName().toLowerCase() + " (" + id + ").");
		}
		else if (keyword.equals("giveitem")) {
			int id = Integer.parseInt(args[0]);
			int amount = 1;
			String name = fullString.substring(fullString.indexOf("-")+1);
			long nameLong = NameUtil.nameToLong(NameUtil.uppercaseFirstLetter(name));
			Player player = World.getPlayerByName(nameLong);
			if (args.length > 1) {
				amount = Integer.parseInt(args[1]);
				amount = amount > Constants.MAX_ITEM_COUNT ? Constants.MAX_ITEM_COUNT : amount;
			}
			player.getInventory().addItem(new Item(id, amount));
			getActionSender().sendMessage("You give a " + new Item(id).getDefinition().getName().toLowerCase() + " (" + id + ") to " + name + "." );
		}
		else if (keyword.equals("runes")) {
			for (int i = 0; i < 566 - 554 + 1; i++) {
				inventory.addItem(new Item(554 + i, 1000));
			}
			inventory.addItem(new Item(1381, 1));
			inventory.addItem(new Item(4675, 1));
		}
		else if (keyword.equals("tabs") || keyword.equals("teleports")) {
		    for(int i = 0; i < 8012 - 8007 + 1; i++) {
			inventory.addItem(new Item(8007 + i, 100));
		    }
		    inventory.addItem(new Item(1712, 2)); //glories
		    inventory.addItem(new Item(3853)); //games necklace
		    inventory.addItem(new Item(11105)); //skills necklace
		    inventory.addItem(new Item(2552)); //duel ring
		    inventory.addItem(new Item(GhostsAhoy.ECTOPHIAL));
		}
		else if (keyword.equals("tele")) {
			try {
				int x = Integer.parseInt(args[0]);
				int y = Integer.parseInt(args[1]);
				int z = Integer.parseInt(args[2]);
				teleport(new Position(x, y, z));
				getActionSender().sendMessage("You teleported to: " + x + ", " + y + ", " + z);
			} catch (Exception e) {
				try {
					int x = Integer.parseInt(args[0]);
					int y = Integer.parseInt(args[1]);
					teleport(new Position(x, y, getPosition().getZ()));
					getActionSender().sendMessage("You teleported to: " + x + ", " + y + ", " + getPosition().getZ());
				} catch (Exception e1) {
					getActionSender().sendMessage("Please use the syntax ::tele x y (optional z)");
				}
			}
		}
		else if (keyword.equals("bump")) {
			try {
				int amount = Integer.parseInt(args[0]);
				String direction = args[1].toLowerCase();
				String name = fullString.substring(fullString.indexOf("-")+1);
				long nameLong = NameUtil.nameToLong(NameUtil.uppercaseFirstLetter(name));
				Player player = World.getPlayerByName(nameLong);
				if(player == null && fullString.toLowerCase().contains("-")) {
				    this.getActionSender().sendMessage("Could not find player.");
				    return;
				}
				switch(direction) {
				    case "up":
					if(fullString.toLowerCase().contains("-")) {
					    player.teleport(new Position(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ() + amount));
					    break;
					}
					else {
					    this.teleport(new Position(this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ() + amount));
					    break;
					}
				    case "down":
					if(fullString.toLowerCase().contains("-")) {
					    player.teleport(new Position(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ() - amount < 0 ? 0 : player.getPosition().getZ() - amount));
					    break;
					}
					else {
					    this.teleport(new Position(this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ() - amount < 0 ? 0 : this.getPosition().getZ() - amount));
					    break;
					}
				    case "e":
				    case "east":
					if(fullString.toLowerCase().contains("-")) {
					    player.teleport(new Position(player.getPosition().getX() + amount, player.getPosition().getY(), player.getPosition().getZ()));
					    break;
					}
					else {
					    this.teleport(new Position(this.getPosition().getX() + amount, this.getPosition().getY(), this.getPosition().getZ()));
					    break;
					}
				    case "w":
				    case "west":
					if(fullString.toLowerCase().contains("-")) {
					    player.teleport(new Position(player.getPosition().getX() - amount, player.getPosition().getY(), player.getPosition().getZ()));
					    break;
					}
					else {
					    this.teleport(new Position(this.getPosition().getX() - amount, this.getPosition().getY(), this.getPosition().getZ()));
					    break; 
					}
				    case "n":
				    case "north":
					if(fullString.toLowerCase().contains("-")) {
					    player.teleport(new Position(player.getPosition().getX(), player.getPosition().getY() + amount, player.getPosition().getZ()));
					    break;
					}
					else {
					    this.teleport(new Position(this.getPosition().getX(), this.getPosition().getY() + amount, this.getPosition().getZ()));
					    break;
					}
				    case "s":
				    case "south":
					if(fullString.toLowerCase().contains("-")) {
					    player.teleport(new Position(player.getPosition().getX(), player.getPosition().getY() - amount, player.getPosition().getZ()));
					    break;
					}
					else {
					    this.teleport(new Position(this.getPosition().getX(), this.getPosition().getY() - amount, this.getPosition().getZ()));
					    break;
					}
				}
			} catch (Exception e) {
			    getActionSender().sendMessage("Please use the syntax ::bump amount direction (east, e, west, w, etc.)");
			    }
		}
		else if (keyword.equals("removebankpin")) {
		    Player player = World.getPlayerByName(fullString);
		    if (player == null)
			return;
		    player.getBankPin().deleteBankPin();
		}
		else if (keyword.equals("melee")) {
			inventory.addItem(new Item(4151)); //whip
			inventory.addItem(new Item(11335)); //d full
			inventory.addItem(new Item(3140)); //d chain
			inventory.addItem(new Item(4087)); //d legs
			inventory.addItem(new Item(11732)); //dragon boots
			inventory.addItem(new Item(1187)); //d square
			inventory.addItem(new Item(6585)); //amulet of fury
			inventory.addItem(new Item(6570)); //fire cape
			inventory.addItem(new Item(7461)); //dragon gloves
		}
		else if (keyword.equals("purple")) {
			for(int i = 2934; i < 2944; i += 2) {
			    inventory.addItem(new Item(i));
			}
			inventory.addItem(new Item(3785)); //purple cloak
		}
		else if (keyword.equals("range")) {
			inventory.addItem(new Item(2581)); //Robin hood
			inventory.addItem(new Item(6585)); //fury
			inventory.addItem(new Item(4736)); //karil's top
			inventory.addItem(new Item(4738)); //karil's skirt
			inventory.addItem(new Item(2577)); //ranger boots
			inventory.addItem(new Item(10376)); //guthix bracers
			inventory.addItem(new Item(6570)); //fire cape
			inventory.addItem(new Item(1540)); //anti
			inventory.addItem(new Item(892, 10000)); //rune arrows
			inventory.addItem(new Item(11212, 10000)); //dragon arrows
			inventory.addItem(new Item(9342, 10000)); //onyx bolts
			inventory.addItem(new Item(861)); //msb
			inventory.addItem(new Item(4212)); //crystal bow
			inventory.addItem(new Item(11235)); //dark bow
			inventory.addItem(new Item(9185)); //rune cbow
		}
		else if (keyword.equals("mage")) {
			getActionSender().sendMessage("Use the ::runes command for runes.");
			inventory.addItem(new Item(3053)); //lava battlestaff
			inventory.addItem(new Item(1397)); //air battlestaff
			inventory.addItem(new Item(6563)); //mystic mud battlestaff
			inventory.addItem(new Item(1712)); //glory
			inventory.addItem(new Item(10342)); //3 age mage hat
			inventory.addItem(new Item(10338)); //3rd age mage top
			inventory.addItem(new Item(10340)); //3rd age mage bottoms
			inventory.addItem(new Item(2579)); //wizard boots
			inventory.addItem(new Item(4095)); //mystic gloves
			inventory.addItem(new Item(2890)); //elemental shield
			this.setQuestStage(12, 11);
		}
		else if (keyword.equals("potions")) {
			inventory.addItem(new Item(2437, 100));
			inventory.addItem(new Item(2441, 100));
			inventory.addItem(new Item(2443, 100));
			inventory.addItem(new Item(2435, 100));
		}
		else if (keyword.equals("food")) {
			inventory.addItem(new Item(391, 28));
		}
		else if (keyword.equals("spawn")) {
			int id = Integer.parseInt(args[0]);
			if(GlobalVariables.npcDump[id].toLowerCase().contains("null")) {
			    getActionSender().sendMessage("Whoops! You landed on a null npc. Please try again.");
			    return;
			}
			NpcDefinition npc = NpcDefinition.forId(id);
			appendToAutoSpawn(npc);
		}
		else if (keyword.equals("team")) {
			int cape = Integer.parseInt(args[0]);
			int count = 0;
			for (Player player : World.getPlayers()) {
				if (player == null)
					continue;
				if (player.getEquipment().getId(Constants.CAPE) == cape) {
					player.teleport(getPosition());
					count++;
				}
			}
			getActionSender().sendMessage("You have " + count + " teammates on your team.");
		}
		else if (keyword.equals("teleto")) {
			String name = fullString;
			for (Player player : World.getPlayers()) {
				if (player == null)
					continue;
				if (player.getUsername().equalsIgnoreCase(name)) {
				    if (player.inFightCaves()) {
					getActionSender().sendMessage("That player is in Fight Caves, best to not mess it up.");
					break;
				    }
				    else {
					teleport(player.getPosition().clone());
					break;
				    }
				}
			}
		}
		else if (keyword.equals("teletome") || keyword.equals("bring")) {
			String name = fullString;
           // if (inWild())  {
             //   actionSender.sendMessage("You can't teleport someone into the wild!");
               // return;
            //}
			Player player = World.getPlayerByName(name);
            if (player == null) {
                actionSender.sendMessage("Cannot find player: "+name);
                return;
            }
            if (player.inDuelArena()) {
                actionSender.sendMessage("That person is dueling right now.");
            }
            if(player.inPestControlLobbyArea() || player.inPestControlGameArea())
            {
            	actionSender.sendMessage("That person is in pest control right now.");
            }
	    if(player.inFightCaves()) {
		getActionSender().sendMessage("That player is in Fight Caves, best to not disturb them.");
		return;
	    }
		player.getPets().unregisterPet();
            player.teleport(getPosition().clone());
		}
		else if (keyword.equals("modern")) {
			getActionSender().sendSidebarInterface(6, 1151);
			magicBookType = SpellBook.MODERN;
		}
		else if (keyword.equals("ancient")) {
			getActionSender().sendSidebarInterface(6, 12855);
			magicBookType = SpellBook.ANCIENT;
		}
		else if (keyword.equals("hits")) {
		    int hits = Integer.parseInt(args[0]);
		    for (int i = 0; i < hits; i++) {
			hit(i, HitType.NORMAL);
		    }
		}
		else if(keyword.equals("hitme")) {
		    this.hit(Integer.parseInt(args[0]), HitType.NORMAL);
		    getActionSender().sendMessage("Your hp is at " + (getSkill().getLevel()[Skill.HITPOINTS] -  Integer.parseInt(args[0]))+ ".");
		    skill.refresh();
		}
		
		else if (keyword.equals("mypos")) {
			getActionSender().sendMessage("You are at: " + getPosition());
		}
		else if (keyword.equalsIgnoreCase("shiptest")) {
			Sailing.sailShip(this, Sailing.ShipRoute.values()[Integer.parseInt(args[0])]);
		}
		else if (keyword.equalsIgnoreCase("carpet")) {
			int xDiff = Integer.parseInt(args[0]);
			int yDiff = Integer.parseInt(args[1]);
			Position pos = new Position(getPosition().getX() + xDiff, getPosition().getY() + yDiff);
			MagicCarpet.ride(this, pos);
		}
		else if (keyword.equals("ranim")) {
			int animationId = (int)Misc.random(7200);
			getUpdateFlags().sendAnimation(animationId, 0);
			getActionSender().sendMessage("Animation #" + animationId);
		}
		else if (keyword.equals("anim")) {
			int animationId = Integer.parseInt(args[0]);
			getUpdateFlags().sendAnimation(animationId, 0);
			getActionSender().sendMessage("Animation #" + animationId);
		}
		else if (keyword.equals("gfx")) {
			int gfxId = Integer.parseInt(args[0]);
			Graphic graphic = new Graphic(gfxId, 100);
			getUpdateFlags().sendGraphic(graphic.getId(), graphic.getValue());
			getActionSender().sendMessage("GFX #" + gfxId);
		}
		else if (keyword.equals("barrowsreward")) {
		    int amount = Integer.parseInt(args[0]);
		    for(int i = 0; i < amount; i++) {
			this.setKillCount(14);
			for(int i2 = 0; i < this.getBarrowsNpcDead().length; i++) {
			    this.setBarrowsNpcDead(i, true);
			}
			Barrows.getReward(this);
		    }
		    this.getActionSender().sendMessage("Sending " +amount+ " rewards based on all brothers dead and 14 kc.");
		}
		else if (keyword.equals("addpcpoints")) {
		    int points = Integer.parseInt(args[0]);
		    String name = fullString.substring(fullString.indexOf("-")+1);
		    long nameLong = NameUtil.nameToLong(NameUtil.uppercaseFirstLetter(name));
		    Player player = World.getPlayerByName(nameLong);
		    addPcPoints(points, player);
		    this.getActionSender().sendMessage("You have added " +points+ " to " +name+"'s commendation points.");
		}
		else if (keyword.equals("setpcpoints")) {
		    int points = Integer.parseInt(args[0]);
		    String name = fullString.substring(fullString.indexOf("-")+1);
		    long nameLong = NameUtil.nameToLong(NameUtil.uppercaseFirstLetter(name));
		    Player player = World.getPlayerByName(nameLong);
		    setPcPoints(points, player);
		    this.getActionSender().sendMessage("You have set " +name+ "'s commendation points to " +points+ ".");
		}
		else if (keyword.equals("getpcpoints")) {
		    String name = fullString;
		    long nameLong = NameUtil.nameToLong(NameUtil.uppercaseFirstLetter(name));
		    Player player = World.getPlayerByName(nameLong);
		    this.getActionSender().sendMessage("That player has " + getPcPoints(player) + " commendation points.");
		}
		
		else if (keyword.equals("setwave") || keyword.equals("wave")) {
		    int wave = Integer.parseInt(args[0]);
		    this.setFightCavesWave(wave - 1);
		    this.getActionSender().sendMessage("Set Fight Caves wave to " + wave + ".");
		}
		else if (keyword.equals("setplayerwave") || keyword.equals("playerwave")) {
		    int wave = Integer.parseInt(args[0]);
		    String name = fullString.substring(fullString.indexOf("-")+1);
		    long nameLong = NameUtil.nameToLong(NameUtil.uppercaseFirstLetter(name));
		    Player player = World.getPlayerByName(nameLong);
		    player.setFightCavesWave(wave);
		    this.getActionSender().sendMessage("Set " + player.getUsername() + " Fight Caves wave to " + wave + ".");
		}
		else if (keyword.equals("forcefightcaves")) {
		    String name = fullString;
		    long nameLong = NameUtil.nameToLong(NameUtil.uppercaseFirstLetter(name));
		    Player player = World.getPlayerByName(nameLong);
		    FightCaves.enterCave(player);
		    this.getActionSender().sendMessage("Forced " + player.getUsername() + " into the Fight Caves.");
		}
		else if (keyword.equals("interface")) {
			this.getActionSender().sendInterface(Integer.parseInt(args[0]));
		} 
		else if (keyword.equals("teststring")) {
		    for(int i = 18500; i < 19000; i++) {
			this.getActionSender().sendString("" + i, i);
		    }
		}
		else if (keyword.equals("resetinterface")) {
		    this.getActionSender().removeInterfaces();
		}
		else if (keyword.equals("unmute")) {
            Player player = World.getPlayerByName(fullString);
            if (player == null) {
                actionSender.sendMessage("Could not find player "+fullString);
                return;
            }
            actionSender.sendMessage("Unmuted "+fullString);
            player.setMuteExpire(System.currentTimeMillis());
        } 
		else if (keyword.equals("ban")) {
        	Ban(args);
		} 
		else if (keyword.equals("unban")) {
		Player player = World.getPlayerByName(fullString);
		if(player == null) {
		    actionSender.sendMessage("Could not find player "+fullString);
		    return;
		}
		else {
		    player.setBanExpire(System.currentTimeMillis() + 1000);
		}
        } 
		else if (keyword.equals("banip")) {
        	BanIpAddress(args);
        } 
		else if (keyword.equals("banmac")) {
        	//BanMacAddress(args);
        } 
		else if (keyword.equals("checkips")) {
        //	checkHosts();
        } 
		else if (keyword.equals("update") ) {
        	final int seconds = Integer.parseInt(args[0]);
			SystemUpdate(seconds);
        }
        else if (keyword.equals("stat")) {
		int skillId = Integer.parseInt(args[0]);
                int lvl = Integer.parseInt(args[1]);
                if (!fullString.contains("-")) {
		    this.getSkill().getLevel()[skillId] = lvl > 99 ? 99 : lvl;
		    this.getSkill().getExp()[skillId] = getSkill().getXPForLevel(lvl) - (getSkill().getXPForLevel(lvl) - getSkill().getXPForLevel(lvl-1));
		    this.getSkill().refresh(skillId);
                }
                String name = fullString.substring(fullString.indexOf("-")+1);
                long nameLong = NameUtil.nameToLong(NameUtil.uppercaseFirstLetter(name));
                Player player = World.getPlayerByName(nameLong);
                if (player == null && fullString.contains("-")) {
                    getActionSender().sendMessage("Can't find player "+name);
                    return;
                }
		player.getSkill().getLevel()[skillId] = lvl > 99 ? 99 : lvl;
		player.getSkill().getExp()[skillId] = getSkill().getXPForLevel(lvl) - (getSkill().getXPForLevel(lvl) - getSkill().getXPForLevel(lvl-1));
                player.getSkill().refresh(skillId);
        }
        else if (keyword.equals("rights")) {
        	GiveRights(args);
        }
        else if(keyword.equals("staffyell")) {
        	Constants.STAFF_ONLY_YELL = !Constants.STAFF_ONLY_YELL;
            getActionSender().sendMessage("Staff only yell: "+(Constants.STAFF_ONLY_YELL ? "true" : "false"));
    		for (Player player : World.getPlayers()) 
    		{
    			if (player == null)
    				continue;
    			if(!player.hideYell)
    			{
    				player.getActionSender().sendMessage("@red@Yell has been set to "+(Constants.STAFF_ONLY_YELL ? "staff-only" : "all-users") + " by "+NameUtil.formatName(getUsername()));
    			}
    		}
        }
	}

	//clear note interface
	public void ClearNotes()
	{
		if(getInterface() == 8134)
		{
			int line = 8144;
			for (int i = 0; i < 120; i++) {
				if(line > 8195 && line < 12174)
				{
					line = 12174;
				}
				getActionSender().sendString("",line);
				line++;
			}
		}
	}
	
	
	/**
	 * get players by host
	 * 
	 * 
	 */
/*	public void checkHosts()
	{
    	List<String> Hosts = new ArrayList();
    	for (Player player : World.getPlayers()) {
    		if(player != null)
			{	
    			if(!Hosts.contains(player.getHost()))
    			{
    				Hosts.add(player.getHost());
    			}
			}
    	}
    	List<Player[]> playersWithHost = new ArrayList();
    	for (String validHost : Hosts) {
    		playersWithHost.add(getPlayersByIp(validHost));
    	}
		getActionSender().sendInterface(8134);
		ClearNotes();
		int line = 8145;
		for (String validHost : Hosts) {
			getActionSender().sendString("@dbl@"+validHost, line);
			line++;
			for (Player[] players : playersWithHost) {
				line++;
				for (Player player : players) {
					if(player != null)
					{	
						getActionSender().sendString(player.getUsername(), line);
						line++;
					}
				}
			}
			line++;
		}
	}
	
	public Player[] getPlayersByIp(String ip)
	{
    	List<Player> playerWithHost = new ArrayList(); 
    	for (Player player : World.getPlayers()) {
    		if(player != null)
			{	
    			if(player.getHost().equals(ip))
    			{
    				playerWithHost.add(player);
    			}
			}
    	}
    	Player[] playerHosts = new Player[playerWithHost.size()];
    	int index = 0;
    	for (Player player : playerWithHost) {
    		playerHosts[index] = player;
    		index++;
    	}
		return playerHosts;
	}
	*/
	private void info() {
	    getActionSender().sendInterface(8134);
	    ClearNotes();
	    this.getActionSender().sendString("@dre@-=vscape information=-", 8144);
	    int line = 8147;
	    for (String q : GlobalVariables.info) {
		if (q != null) {
		    if (line > 8195 && line < 12174) {
			line = 12174;
		    }
		    this.getActionSender().sendString(q, line);
		    line++;
		}
	    }
	}
	/**
	 * Yell Message
	 * 
	 * @param msg
	 *            the yell message
	 * 
	 */
	private void Yell(String Msg) {
		if(Constants.STAFF_ONLY_YELL & getStaffRights() < 1)
		{
			getActionSender().sendMessage("Yell is currently set to Staff only.");
			return;
		}
		if(hideYell)
		{
			getActionSender().sendMessage("Your yelling is currently disabled ::hideyell");
			return;
		}
		if(getStaffRights() < 1){
			 if(System.currentTimeMillis() - lastYell < 25000) {
				 long secBeforeNextYell = ((((System.currentTimeMillis() - lastYell)  / 1000 ) - 30) * -1);
				 getActionSender().sendMessage("You can yell again in " + secBeforeNextYell + " seconds!");
				 return;
			 }
		}
		String YellMsg = Msg;
		
		for(int i = 0; i < Constants.bad.length; i++)
		{
			if(YellMsg.toLowerCase().contains(Constants.bad[i]))
			{
				getActionSender().sendMessage("You are trying to say something that should not be said!");
				return;
			}
		}
		
		if (isMuted()) 
		{
			getActionSender().sendMessage("You are muted and cannot use yell.");
			return;
		}
		
		String NameColor = "@blu@";
        switch (getStaffRights()) {
            case 1:
            	NameColor = "@whi@";
                break;
            case 2:
            	NameColor = "@mag@";
                break;
            default:
            	NameColor = "@blu@";
        }
		String yeller = NameUtil.formatName(getUsername());
		
		lastYell = System.currentTimeMillis();
		
		for (Player player : World.getPlayers()) 
		{
			if (player == null)
				continue;
			
			if(!player.hideYell)
			{
				if(player.hideColors)
				{
					for(int k = 0; k < Constants.colorStrings.length;k++)
					{
						YellMsg = YellMsg.replace(Constants.colorStrings[k], "");
					}
				}
				player.getActionSender().sendMessage(NameColor+"["+yeller+"]@dre@ " + NameUtil.uppercaseFirstLetter(YellMsg));
			}
		}
	}
        
        //oh fuck it's clan chat dude idshabbeding
        	private void Clan(String Msg) {
                if(currentChannel == null){ 
			getActionSender().sendMessage("You're not in a clan.");
			return;
		}

		String ClanMsg = Msg;
		String chatter = NameUtil.formatName(getUsername());
                
                String clan = currentChannel;
                
                for(int i = 0; i < Constants.bad.length; i++)
		{
			if(ClanMsg.toLowerCase().contains(Constants.bad[i]))
			{
				getActionSender().sendMessage("You are trying to say something that should not be said!");
				return;
			}
		}
                
		for (Player player : World.getPlayers()) {
			if (player == null)
				continue;
                        
                        if(player.currentChannel.equals(clan)){
				if(player.hideColors){
					for(int k = 0; k < Constants.colorStrings.length;k++){
						ClanMsg = ClanMsg.replace(Constants.colorStrings[k], "");
					}
				}
				player.getActionSender().sendMessage("@gre@[" + NameUtil.uppercaseFirstLetter(clan) + "]@blu@["+ chatter +"]@bla@: " + NameUtil.uppercaseFirstLetter(ClanMsg));
                            
			}
		}
	}
             //glanzchat ends here
                

	/**
	 * Modify Stats
	 * 
	 * @param player
	 *            the player
	 * 
	 * @param skill
	 *            the skill
	 * 
	 * @param newLvl
	 *            the new level
	 */
	private void Stat(String player, int skill, int newLvl) {

	}

	/**
	 * Mute Player
	 * 
	 * @param player
	 *            the player to mute
	 * 
	 */
	private void Mute(String[] args) {

	}
	
	/**
	 * Change Player rights
	 * 
	 * @param player
	 *            the player
	 *
	 * @param rights
	 *            right access level
	 */
	private void GiveRights(String[] args) {
		if (args.length < 2) {
			actionSender.sendMessage("::rights username rightType");
			actionSender.sendMessage("rightType ex: player, mod, admin");
			return;
		}
		String name = args[0];
		String rightType = args[1];
		Player player = World.getPlayerByName(name);
		if (player == null) {
			actionSender.sendMessage("Could not find player " + name);
			return;
		}
		if (player.isBanned()) {
			actionSender.sendMessage("Player is banned");
			return;
		}
		int rightLevel = 0;
        switch (rightType) {
        case "player":
        	rightLevel = 0;
            break;
        case "mod":
        	rightLevel = 1;
            break;
        case "admin":
        	rightLevel = 2;
        }
        
		String playerName = NameUtil.formatName(player.getUsername());
        if(player.getStaffRights() != rightLevel)
        {
			player.setStaffRights(rightLevel);
			World.messageToStaff("@dre@"+getUsername()+" has made "+ playerName +" "+ rightType +".");
        }
        else
        {
        	actionSender.sendMessage(playerName + " is already a "+ rightType +".");
        }
    }
	
	/**
	 * Ban Player
	 * 
	 * @param player
	 *            the player to ban
	 * 
	 */
	private void Ban(String[] args) {
		if (args.length < 2) {
			actionSender.sendMessage("::ban username hours"); //use underscore instead of space if name is two words
			return;
		}
		String name = "";
		for (int i = 1; i < args.length; i++) {
			name += args[0];
		}
		int hours = Integer.parseInt(args[1]);
		if (hours <= 0 || hours > 1000000) {
			actionSender.sendMessage("Ban between 0 and 1000000 hours");
			return;
		}
		Player player = World.getPlayerByName(name);
		if (player == null) {
			actionSender.sendMessage("Could not find player " + name);
			return;
		}
		if (player.isBanned()) {
			actionSender.sendMessage("Player is already banned");
			return;
		}
		try {
			OutputStreamWriter out = new OutputStreamWriter(
					new FileOutputStream("./data/modlog.out", true));
			out.write(player.getUsername() + " was banned by " + username
					+ " for " + hours + " hours.");
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		actionSender.sendMessage("Banned " + player.getUsername() + " for "
				+ hours + " hours.");
		player.setBanExpire(System.currentTimeMillis()
				+ (hours * 60 * 60 * 1000));
		player.disconnect();
	}
	
	private void ModBan(String[] args) {
		if (args.length < 1) {
			actionSender.sendMessage("::ban username hours"); //use underscore instead of space if name is two words
			return;
		}
		String name = args[0];
		int hours = 2;
		Player player = World.getPlayerByName(name);
		if (player == null) {
			actionSender.sendMessage("Could not find player " + name);
			return;
		}
		if (player.getStaffRights() > 1) {
		    actionSender.sendMessage("You can't ban someone with more rights than you!");
		    return;
		}
		if (player.isBanned()) {
			actionSender.sendMessage("Player is already banned");
			return;
		}
		try {
			OutputStreamWriter out = new OutputStreamWriter(
					new FileOutputStream("./data/modlog.out", true));
			out.write(player.getUsername() + " was banned by " + username
					+ " for " + hours + " hours.");
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		actionSender.sendMessage("Banned " + player.getUsername() + " for "
				+ hours + " hours.");
		player.setBanExpire(System.currentTimeMillis()
				+ (hours * 60 * 60 * 1000));
		player.disconnect();
	}
	
	/**
	 * Ban Player mac address
	 * 
	 * @param player
	 *            the player to ban
	 * 
	 */
	private void BanIpAddress(String[] args) {
		Player player = World.getPlayerByName(args[0]);
		if (player == null) {
			actionSender.sendMessage("Could not find player " + args[0]);
			return;
		}
		if (player.isIpBanned()) {
			actionSender.sendMessage("Player is already ip banned");
			return;
		}
		try {
			OutputStreamWriter out = new OutputStreamWriter(
					new FileOutputStream("./data/bannedips.txt", true));
			out.write(player.getHost()+"\n");
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		actionSender.sendMessage("Banned " + player.getUsername() + "'s ip address.");
		player.disconnect();
	}

	private void SystemUpdate(int seconds) {
		if (seconds <= 0) {
			getActionSender().sendMessage("Invalid timer parameter provided.");
			return;
		}
		final int ticks = seconds * 1000 / 600;
		if (GlobalVariables.getServerUpdateTimer() != null) {
			getActionSender().sendMessage(
					"An update has already been executed.");
			return;
		}
		Constants.SYSTEM_UPDATE = true;
		for (Player player : World.getPlayers()) {
			if (player == null || player.getIndex() == -1)
				continue;
			player.getActionSender().sendUpdateServer(ticks);
			TradeManager.declineTrade(player);
		}

		new ShutdownWorldProcess(seconds).start();
	}

	private void writeNpc(Npc npc) {
		String filePath = "./data/npcs/spawn-config.cfg";
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filePath, true));
			try {
				out.newLine();
				out.write("spawn = "+npc.getNpcId()+"	"+npc.getPosition().getX()+"	"+npc.getPosition().getY()+"	"+npc.getPosition().getZ()+"	1	"+npc.getDefinition().getName());
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		movePlayer(position);
		getActionSender().sendMapState(0);
		getActionSender().removeInterfaces();
		getUpdateFlags().sendAnimation(-1);
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
                if (heightChange) {
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
		getPosition().setAs(position);
		getPosition().setLastX(position.getX());
		getPosition().setLastY(position.getY() + 1);
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
		Tiaras.handleTiara(this, getEquipment().getId(Constants.HAT));
		getEquipment().checkRangeGear();
		getEquipment().checkBarrowsGear();
		getEquipment().checkVoidGear();
		refreshOnLogin();
		setStopPacket(false);
		wildyWarned = inWild();
		if (getNewComersSide().getTutorialIslandStage() == 99) {
			getNewComersSide().setTutorialIslandStage(100, true);
			info();
			//getActionSender().sendInterface(3559);
			getActionSender().sendSideBarInterfaces();
			getEquipment().sendWeaponInterface();
			getNewComersSide().addStarterItems();
			QuestHandler.initPlayer(this);
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
		RandomEvent.startRandomEvent(this);
	    setAppearanceUpdateRequired(true);
	    QuestHandler.initPlayer(this);
	    getActionSender().sendString("Total Lvl: " + skill.getTotalLevel(), 3984);
	    getActionSender().sendString("QP: @gre@"+questPoints+" ", 3985);
	}

	public boolean beginLogin() throws Exception {
		// check login status before sql
		if (checkLoginStatus())  {
			PlayerSave.load(this);
			return true;
		} else {
			sendLoginResponse();
			System.out.println("17");
			disconnect();
			return false;
		}
	}

	private boolean checkLoginStatus() {
        if(isBanned() || isIpBanned())
		{
			setReturnCode(Constants.LOGIN_RESPONSE_ACCOUNT_DISABLED);
			return false;
		}
		for(int i = 0; i < Constants.bannedChars.length; i++)
		{
			if(username.contains(Constants.bannedChars[i]))
			{
	            setReturnCode(Constants.LOGIN_RESPONSE_INVALID_CREDENTIALS);
	            return false;
			}
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

	public void logout() {
		if(Constants.SQL_ENABLED)
		{
			SQL.saveHighScore(this);
		}
        if(inPestControlLobbyArea())
        {
        	PestControl.leaveLobby(this);
        }
	else if(inPestControlGameArea())
        {
        	PestControl.leaveGame(this);
        }
	else if(inWarriorGuildArena()) {
	    WarriorsGuild.exitArena(this, true);
	}
	else if(inFightCaves()) {
	    FightCaves.destroyNpcs(this);
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
/*public int destroyBarrowItemOnDeath(Item item) {	//cadillac
         String itemName = ItemDefinition.forId(item.getId()).getName().toLowerCase();
         if(itemName.contains("ahrim")) {
             if(itemName.contains("staff"))
                 return 4866;
             if(itemName.contains("skirt"))
                 return 4878;
             if(itemName.contains("top"))
                 return 4872;
             if(itemName.contains("hood"))
                 return 4860;
         }
         if(itemName.contains("guthan")) {
             if(itemName.contains("spear"))
                 return 4914;
             if(itemName.contains("skirt"))
                 return 4926;
             if(itemName.contains("body"))
                 return 4920;
             if(itemName.contains("helm"))
                 return 4908;
         }
         if(itemName.contains("torag")) {
             if(itemName.contains("hammers"))
                 return 4962;
             if(itemName.contains("legs"))
                 return 4974;
             if(itemName.contains("body"))
                 return 4968;
             if(itemName.contains("helm"))
                 return 4956;
         }
         if(itemName.contains("dharok")) {
             if(itemName.contains("axe"))
                 return 4890;
             if(itemName.contains("legs"))
                 return 4902;
             if(itemName.contains("body"))
                 return 4896;
             if(itemName.contains("helm"))
                 return 4884;
         }
         if(itemName.contains("verac")) {
             if(itemName.contains("flail"))
                 return 4986;
             if(itemName.contains("skirt"))
                 return 4998;
             if(itemName.contains("top") || itemName.contains("brassard"))
                 return 4992;
             if(itemName.contains("helm"))
                 return 4980;
         }
         if(itemName.contains("karil")) {
             if(itemName.contains("bow"))
                 return 4938;
             if(itemName.contains("skirt"))
                 return 4950;
             if(itemName.contains("top"))
                 return 4944;
             if(itemName.contains("coif"))
                 return 4932;
         }
         return -1;
     }*/
    @Override
    public String toString() {
		return getUsername() == null ? "Client(" + getHost() + ")" : "Player(" + getUsername() + ":" + getPassword() + " - " + getHost() + " mac:"+macAddress+")";
	}

    public String getHost() {
        return host;
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

	public void setBank(Container bank) {
		this.bank = bank;
	}

	public Container getBank() {
		return bank;
	}

	public void setBonuses(int id, int bonuses) {
		this.bonuses.put(id, bonuses);
	}

	public Map<Integer, Integer> getBonuses() {
		return bonuses;
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
	
	public FreakyForester getFreakyForester() {
		return freakyForester;
	}
	
	public GhostsAhoyPetition getPetition() {
		return petition;
	}
	
	public boolean[] getRuneDrawWins() {
		return runeDrawWins;
	}
	
	public void setRuneDrawWins(int slot, boolean bool) {
	    this.runeDrawWins[slot] = bool;
	}
	
	public boolean justWonRuneDraw() {
		return justWonRuneDraw;
	}
	
	public void setJustWonRuneDraw(boolean bool) {
	    this.justWonRuneDraw = bool;
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
        public PestControl getPestControl() {
                return pestControl;
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

	public InterfaceClickHandler getRandomInterfaceClick() {
		return randomInterfaceClick;
	}

	public SkillResources getSkillResources() {
		return skillResources;
	}

	public Pets getPets() {
		return pets;
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
			drunkTimer = time;
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
	
	public boolean isPhoenixGang() {
	    return phoenixGang;
	}
	public void joinPhoenixGang(boolean bool) {
	    if(this.isBlackArmGang()) this.phoenixGang = false;
	    else this.phoenixGang = bool;
	}
	
	public boolean isBlackArmGang() {
	    return blackArmGang;
	}
	public void joinBlackArmGang(boolean bool) {
	    if(this.isPhoenixGang()) this.blackArmGang = false;
	    else this.blackArmGang = bool;
	}
	
	public boolean getMelzarsDoorUnlock() {
	    return melzarsDoor;
	}
	
	public void setMelzarsDoorUnlock(boolean bool) {
	    this.melzarsDoor = bool;
	}
	
	public boolean getBananaCrate() {
	    return bananaCrate;
	}
	
	public void setBananaCrate(boolean bool) {
	    this.bananaCrate = bool;
	}
	
	public int getBananaCrateCount() {
	    return bananaCrateCount;
	}
	
	public void setBananaCrateCount(int count) {
	    this.bananaCrateCount = count;
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
	
	public int[] getBarrowsHits() {
	    return barrowsHits;
	}
	public void setBarrowsHits(int i, int value) {
	    this.barrowsHits[i] = value;
	}
	public ArrayList<BoneBurying.Bone> getBonesInBin() {
	    return bonesInBin;
	}
	public void addBonesInBin(BoneBurying.Bone bone) {
	    this.bonesInBin.add(bone);
	}
	public ArrayList<BoneBurying.Bone> getBonesGround() {
	    return bonesGround;
	}
	public void addBonesGround(BoneBurying.Bone bone) {
	    this.bonesGround.add(bone);
	}
	public boolean secondTryAtBin() {
	    return secondTryAtBin;
	}
	public void setSecondTryAtBin(boolean bool) {
	    this.secondTryAtBin = bool;
	}
	public boolean bonesGrinded() {
	    return bonesGrinded;
	}
	public void setBonesGrinded(boolean bool) {
	    this.bonesGrinded = bool;
	}
	public int getEctoWorshipCount() {
	    return ectoWorshipCount;
	}
	public void setEctoWorshipCount(int count) {
	    this.ectoWorshipCount = count;
	}
	public String getTopHalfFlag() {
	    return topHalfOfGhostsAhoyFlag;
	}
	public String getBottomHalfFlag() {
	    return bottomHalfOfGhostsAhoyFlag;
	}
	public String getSkullFlag() {
	    return skullOfGhostsAhoyFlag;
	}
	public String getDesiredTopHalfFlag() {
	    return desiredTopHalfOfGhostsAhoyFlag;
	}
	public String getDesiredBottomHalfFlag() {
	    return desiredBottomHalfOfGhostsAhoyFlag;
	}
	public String getDesiredSkullFlag() {
	    return desiredSkullOfGhostsAhoyFlag;
	}
	public boolean lobsterSpawnedAndDead() {
	    return lobsterSpawned;
	}
	public void setLobsterSpawnedAndDead(boolean bool) {
	    this.lobsterSpawned = bool;
	}
	public boolean petitionSigned() {
	    return petitionSigned;
	}
	public void setPetitionSigned(boolean bool) {
	    this.petitionSigned = bool;
	}
	public void dyeGhostsAhoyFlag(String part, String color) {
	    switch(part) {
		case "topHalf":
		case "top":
		    this.topHalfOfGhostsAhoyFlag = color;
		    return;
		case "bottomHalf":
		case "bottom":
		    this.bottomHalfOfGhostsAhoyFlag = color;
		    return;
		case "skull":
		    this.skullOfGhostsAhoyFlag = color;
		    return;	
	    }
	}
	public void setDesiredGhostsAhoyFlag(String part, String color) {
	    switch(part) {
		case "topHalf":
		case "top":
		    this.desiredTopHalfOfGhostsAhoyFlag = color;
		    return;
		case "bottomHalf":
		case "bottom":
		    this.desiredBottomHalfOfGhostsAhoyFlag = color;
		    return;
		case "skull":
		    this.desiredSkullOfGhostsAhoyFlag = color;
		    return;	
	    }
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

	public void setPrayerIcon(int prayerIcon) {
		this.prayerIcon = prayerIcon;
	}

	public int getPrayerIcon() {
		return prayerIcon;
	}

	public void addToServerPoints(int serverPoints) {
		actionSender.sendMessage("You have recieved " + serverPoints + " server points!");
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
	
	public boolean getCanHaveGodCape() {	//cadillac
	    return canHaveGodCape;
	}
	
	public void setCanHaveGodCape(boolean bool){ //cadillac-ack-ack-ack
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

	public int getQuestPoints() {
		return questPoints;
	}

	public void setQuestPoints(int qP) {
		this.questPoints = qP;
	}
	
	public void addQuestPoints(int points) {
        this.questPoints += points;
        getActionSender().sendString("QP: @gre@"+questPoints+" ", 3985);
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
		return stopPlayerPacket;
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
			return isUsingPrayer[Prayer.PROTECT_FROM_MELEE];
		else if (attackType == AttackType.RANGED)
			return isUsingPrayer[Prayer.PROTECT_FROM_RANGED];
		else if (attackType == AttackType.MAGIC)
			return isUsingPrayer[Prayer.PROTECT_FROM_MAGIC];
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
		case 29063:
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
		case 29038:
			if (getEquipment().getId(Constants.WEAPON) == 4153) {
				if (getCombatingEntity() == null) {
					getActionSender().sendMessage("You can only use this special when attacking something.");
					return true;
				}
				SpecialType.gmaulSpec(this);
				return true;
			}
			break;
		}
		boolean before = specialAttackActive;
		if (equippedWeapon.getWeaponInterface().getSpecialBarButtonId() != buttonId)
			return false;
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
		if (isUsingPrayer[Prayer.PROTECT_ITEM])
			itemsKept += 1;
		while (keptItems.size() < itemsKept && allItems.size() > 0) {
			keptItems.add(allItems.poll());
		}
		return keptItems;
	}

	@Override
	public void dropItems(Entity killer) {
		if (inDuelArena() || creatureGraveyard.isInCreatureGraveyard() || inPestControlLobbyArea() || inPestControlGameArea() || onPestControlIsland() || inFightCaves()) {
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
		Item[] items = new Item[equipment.getItemContainer().capacity() + inventory.getItemContainer().capacity()];
		System.arraycopy(equipment.getItemContainer().getItems(), 0, items, 0, equipment.getItemContainer().getItems().length);
		System.arraycopy(inventory.getItemContainer().getItems(), 0, items, equipment.getItemContainer().getItems().length, inventory.getItemContainer().getItems().length);
		ArrayList<Item> keptItems = getItemsKeptOnDeath(items);
		List<Item> allItems = new ArrayList<Item>(Arrays.asList(items));
		for(int i = 0; i < getPets().PET_IDS.length; i++) {
		    if(inventory.playerHasItem(new Item(getPets().PET_IDS[i][0])) )
				keptItems.add(new Item(getPets().PET_IDS[i][0]));
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
		for (Item kept : keptItems)
			inventory.addItem(kept);
		for (Item dropped : allItems) {
			if (dropped == null)
				continue;
			for(int i = 0; i < getPets().PET_IDS.length; i++) {
			    if(dropped.getId() == getPets().PET_IDS[i][0])
				inventory.addItem(dropped);
			}
			/*if(BarrowsItems.droppableForDeath(BarrowsItems.getBarrowsItem(dropped), dropped)) {
				GroundItemManager.getManager().dropItem(new GroundItem(new Item(BarrowsItems.getBarrowsItem(dropped).getBrokenId()), killer));
				setBarrowsHits(BarrowsItems.getBarrowsItem(dropped).getPlayerArraySlot(), 0);
			}*/
			if (!dropped.getDefinition().isUntradable()) {
				GroundItem item = new GroundItem(new Item(dropped.getId(), dropped.getCount()), this, killer, getDeathPosition());
				GroundItemManager.getManager().dropItem(item);
			}
			else {
				 GroundItem item = new GroundItem(new Item(dropped.getId(), dropped.getCount()), this, getDeathPosition());
				 GroundItemManager.getManager().dropItem(item);
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
		} 
		else if(hasVoidMace() && spell == Spell.CLAWS_OF_GUTHIX) {
		    	getActionSender().sendSidebarInterface(0, 3796);
			getActionSender().updateAutoCastInterface(spell);
			this.autoCasting = true;
		}else {
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
	
	public void setCanoeStation(CanoeStationData newStation)
	{
		curCanoeStation = newStation;
	}
	
	public CanoeStationData getCanoeStation()
	{
		return curCanoeStation;
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
		int skills[] = getSkill().getLevel();
		for (int i = 0; i < skills.length; i++)
			getSkill().setSkillLevel(i, getSkill().getPlayerLevel(i));
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

	public void appendToBugList(String bug) {
		String filePath = "./data/bugs.txt";
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filePath, true));
			try {
				out.write("Bug reported by " + getUsername() + " about : " + bug);
				out.newLine();
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void appendToAutoSpawn(NpcDefinition npc) {
		int randNum = Misc.random(4) + 2;
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
		NpcLoader.newNPC(npc.getId(), getPosition().getX(), getPosition().getY(), getPosition().getZ(), randNum);
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
		if (npc.isAttacking() || !npc.getDefinition().isAttackable()) {
			return false;
		}
		if (npc.getNpcId() == 2429 || npc.getNpcId() == 1827 || npc.getNpcId() == 1266 || npc.getNpcId() == 1268 || npc.getNpcId() == 2453 || npc.getNpcId() == 2890) {
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
		if (!getInCombatTick().completed() && !inMulti()) {
			return;
		}
		for (Npc npc : getNpcs()) {
			if (npc.getPlayerOwner() != null) {
				continue;
			}
			if (!npcCanAttack(npc)) {
				continue;
			}
			if (Misc.goodDistance(npc.getSpawnPosition(), getPosition(), npc.getNpcId() == 1266 || npc.getNpcId() == 1268 || npc.getNpcId() == 2453 || npc.getNpcId() == 2890 ? 1 : 4)) {
			//if (npc.getWalkableArea().contains(getPosition().getX(), getPosition().getY())) {
				CombatCycleEvent.CanAttackResponse response = CombatCycleEvent.canAttack(npc, this);
				if (response != CombatCycleEvent.CanAttackResponse.SUCCESS)
					continue;
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
		for (Item item : getBank().getItems()) {
			if (item != null && item.getId() == id) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasItem(int id) {
		for (Item item : getInventory().getItemContainer().getItems()) {
			if (item != null && item.getId() == id) {
				return true;
			}
		}
		for (Item item : getBank().getItems()) {
			if (item != null && item.getId() == id) {
				return true;
			}
		}
		return false;
	}

	public boolean hasClueScroll() {
		for (Item item : getInventory().getItemContainer().getItems()) {
			if (item != null && item.getDefinition().getName().toLowerCase().contains("clue")) {
				return true;
			}
		}
		for (Item item : getBank().getItems()) {
			if (item != null && item.getDefinition().getName().toLowerCase().contains("clue")) {
				return true;
			}
		}
		return false;
	}

	public boolean hasPuzzle() {
		for (Item item : getInventory().getItemContainer().getItems()) {
			if (item != null && (item.getId() == 2800 || item.getId() == 3565 || item.getId() == 3571)) {
				return true;
			}
		}
		for (Item item : getBank().getItems()) {
			if (item != null && (item.getId() == 2800 || item.getId() == 3565 || item.getId() == 3571)) {
				return true;
			}
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
        File file = new File("./data/bannedips.txt");
        if (!file.exists()) {
            return false;
        }
        try {
        	String CurrentLine;
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((CurrentLine = br.readLine()) != null) 
            {
            	if(CurrentLine.length() > 0)
            	{
					if(CurrentLine.startsWith(getHost()))
					{
						br.close();
						return true;
					}
            	}
			}
			br.close();
        } catch (IOException e) {
        	return false;
	    }
        return false;
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

	private void saveCommand(String user, String command) {
		String filePath = "./data/commands.txt";
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filePath, true));
			try {
				out.write(user+" used "+command);
				out.newLine();
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		getActionSender().sendString("", 7356); //dwarf cannon
		getActionSender().sendString("", 8679);//Eadgar's ruse
		getActionSender().sendString("@red@Elemental Workshop", 7459); //elemental workshop
		getActionSender().sendString("", 7357); //Family crest
		getActionSender().sendString("", 12836); //the feud
		getActionSender().sendString("", 7358); //fight arena
		getActionSender().sendString("", 7359); //Fishing Content
		getActionSender().sendString("", 14169); //forgettable tale
		getActionSender().sendString("", 10115); //the fremennik trials
		getActionSender().sendString("", 14604); //garden of tranquility
		getActionSender().sendString("", 7360); //gertrude's cat
		getActionSender().sendString("@red@Ghosts Ahoy", 12282); //ghosts ahoy
		getActionSender().sendString("", 13577); //the giant dwarf
		getActionSender().sendString("", 12839); //the golem
		getActionSender().sendString("", 7361); //the grand tree
		getActionSender().sendString("", 11857); //haunted mine
		getActionSender().sendString("", 7362); //hazeel cult
		getActionSender().sendString("", 7363); //heroes quest
		getActionSender().sendString("", 7364); //holy grail
		getActionSender().sendString("", 10135); //horror from the deep
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
		getActionSender().sendString("", 8137); //nature spirit
		getActionSender().sendString("", 7371); //observatory quest
		getActionSender().sendString("", 12345); //one small favour
		getActionSender().sendString("", 7372); //plague city
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
		getActionSender().sendString("", 7380); //tree gnome village
		getActionSender().sendString("", 7381); //tribal totem
		getActionSender().sendString("", 11858); //troll romance
		// unknown id
		getActionSender().sendString("", 9927); //underground pass
		getActionSender().sendString("", 7349); //watchtower
		getActionSender().sendString("", 7350); //waterfall quest
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
		getActionSender().sendString("", 668); //recruitment drive
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
    public String getCurrentchannel(){
    	return currentChannel;
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
    public boolean getShowHp(){
        return showHp;
    }
        public void setShowHp(boolean show, boolean msg) {
		showHp = show;
			if (showHp) {
				getActionSender().sendMessage(
						"You have turned on your HP and Prayer display.");
			} else {
				getActionSender().sendMessage(
						"You have turned off your HP and Prayer display.");
			}
		
	}
        public void setClanChannel(String channel) {
		String inChannel = channel;
                
                    for(int i = 0; i < Constants.bad.length; i++){
			if(channel.toLowerCase().contains(Constants.bad[i])){
				getActionSender().sendMessage("You are trying to make a channel you shouldn't make!");
				return;
			}
		}
                                
                                if(currentChannel == null){
				getActionSender().sendMessage(
						"You have joined " + channel +".");
                                currentChannel = channel;
                                }else{
                                getActionSender().sendMessage(
						"You are already in " + currentChannel + "!");
                                }
			}
	
        public void leaveClanChannel(){
           getActionSender().sendMessage("You have left " + currentChannel + ".");
           currentChannel = null;
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
}
