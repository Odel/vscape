package com.rs2.model.content.quests.MonkeyMadness;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.content.dialogue.DialogueManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.model.objects.GameObject;
import com.rs2.model.World;
import com.rs2.model.content.Following;
import com.rs2.model.content.combat.hit.HitType;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_2;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import com.rs2.model.content.quests.MonkeyMadness.ApeAtoll.GreeGreeData;
import com.rs2.model.content.quests.MonkeyMadness.ApeAtollNpcs.FinalFightNpcs;
import com.rs2.model.content.quests.Quest;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.randomevents.EventsConstants;
import com.rs2.model.content.treasuretrails.ClueScroll;
import com.rs2.util.Misc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.objects.functions.Ladders;

public class MonkeyMadness implements Quest {

	//Quest stages
	public static final int QUEST_STARTED = 1;
	public static final int SPOKE_TO_CARROTCOCK = 2;
	public static final int NEW_ORDERS = 3;
	public static final int ORDERS_FROM_DAERO = 4;
	public static final int CRACKED_THE_CODE = 5;
	public static final int LUMBDO_A_DICK = 6;
	public static final int MEANWHILE = 7;
	public static final int PRISON_DAY_1 = 8; //Unused
	public static final int GARKORS_ORDERS = 9;
	public static final int ZOOKNOCK_SPEECH = 10;
	public static final int MONKEY_MAN = 11;
	public static final int GARKORS_PLAN = 12;
	public static final int KRUKS_PERMISSION = 13;
	public static final int AWOWOGEIS_TEST = 14;
	public static final int INTO_THE_CAGE = 15;
	public static final int THE_GREAT_ESCAPE = 16;
	public static final int PASSED_THE_TEST = 17;
	public static final int GARKORS_RETELLING = 18;
	public static final int NEW_MEMEBER = 19;
	public static final int FUCK_THE_DEMON = 20;
	public static final int QUEST_COMPLETE = 21;

	//Items
	public static final int DRAGONSTONE = 1615;
	public static final int BANANA = 1963;
	public static final int GOLD_BAR = 2357;
	public static final int SPARE_CONTROLS = 4002;
	public static final int ROYAL_SEAL = 4004;
	public static final int NARNODES_ORDERS = 4005;
	public static final int MONKEY_DENTURES = 4006;
	public static final int ENCHANTED_BAR = 4007;
	public static final int MONKEY_NUTS = 4012;
	public static final int MONKEY_BAR = 4014;
	public static final int BANANA_STEW = 4016;
	public static final int MONKEYSPEAK_AMULET_MOULD = 4020;
	public static final int MONKEYSPEAK_AMULET = 4021;
	public static final int MONKEYSPEAK_AMULET_U = 4022;
	public static final int MONKEY_TALISMAN = 4023;
	public static final int MONKEY_ITEM = 4033;
	public static final int MONKEY_SKULL = 4034;
	public static final int TENTH_SQUAD_SIGIL = 4035;
	public static final int WOOL = 1759;
	public static final int KARAMJA_MONKEY_BONES = 3183;

	//Positions
	public static final Position INSIDE_VILLAGE = new Position(2515, 3162, 0);
	public static final Position APE_ATOLL_LANDING = new Position(2805, 2707, 0);
	public static final Position FINAL_FIGHT = new Position(2729, 9173, 1);
	public static final Position HANGAR = new Position(2585, 4518, 0);
	public static final Position HANGAR_INITIALIZED = new Position(2649, 4518, 0);
	public static final Position BELOW_PYRES = new Position(2807, 9201, 0);
	public static final Position UP_FROM_PYRES = new Position(2806, 2785, 0);
	public static final Position START_OF_DUNGEON = new Position(2764, 9103, 0);
	public static final Position END_OF_DUNGEON = new Position(2805, 9142, 0);
	public static final Position CRASH_ISLAND = new Position(2897, 2727, 0);
	public static final Position AWOWOGEIS_PALACE = new Position(2802, 2762, 0);
	public static final Position INSIDE_CAGE = new Position(2603, 3278, 0);
	public static final Position OUTSIDE_CAGE = new Position(2608, 3278, 0);

	//Interfaces
	public static final int[] GLIDER_PUZZLE = {3904, 3906, 3908, 3910, 3912, 3914, 3916, 3918, 3920, 3922, 3924, 3926, 3928, 3930, 3932, 3934, 3936, 3938, 3940, 3942, 3944, 3946, 3948, 3950, -1};
	public static Item[] GLIDER_PUZZLE_ITEMS = null;
	public static final int PUZZLE_INTERFACE = 11126;

	//Npcs
	public static final int HAZELMERE = 669;
	public static final int SHIPYARD_WORKER = 675;
	public static final int KING_NARNODE = 670;
	public static final int GLOUGH = 671;
	public static final int ANITA = 672;
	public static final int DAERO = 1407;
	public static final int WAYDAR = 1408;
	public static final int WAYDAR_2 = 1409;
	public static final int WAYDAR_3 = 1410;
	public static final int GARKOR = 1411;
	public static final int GARKOR_2 = 1412;
	public static final int LUMO = 1413;
	public static final int LUMO_2 = 1414;
	public static final int BUNKDO = 1415;
	public static final int BUNKDO_2 = 1416;
	public static final int CARADO = 1417;
	public static final int CARADO_2 = 1418;
	public static final int LUMBDO = 1419;
	public static final int KARAM = 1420;
	public static final int KARAM_2 = 1421;
	public static final int KARAM_3 = 1422;
	public static final int BUNKWICKET = 1423;
	public static final int WAYMOTTIN = 1424;
	public static final int ZOOKNOCK = 1425;
	public static final int ZOOKNOCK_2 = 1426;
	public static final int GLO_CARANOCK = 1427;
	public static final int GLO_CARANOCK_2 = 1428;
	public static final int DUGOPUL = 1429;
	public static final int SALENAB = 1430;
	public static final int TREFAJI = 1431;
	public static final int ABERAB = 1432;
	public static final int SOLIHIB = 1433;
	public static final int DAGA = 1434;
	public static final int TUTAB = 1435;
	public static final int IFABA = 1436;
	public static final int HAMAB = 1437;
	public static final int HAFUBA = 1438;
	public static final int DENADU = 1439;
	public static final int LOFU = 1440;
	public static final int KRUK = 1441;
	public static final int DUKE = 1442;
	public static final int OIPUIS = 1443;
	public static final int UYORO = 1444;
	public static final int OUHAI = 1445;
	public static final int UODAI = 1446;
	public static final int PADULAH = 1447;
	public static final int AWOWOGEI = 1448;
	public static final int UWOGO = 1449;
	public static final int MURUWOI = 1450;
	public static final int SLEEPING_MONKEY = 1451;
	public static final int MONKEY_CHILD = 1452;
	public static final int MONKEYS_UNCLE = 1453;
	public static final int MONKEYS_AUNT = 1454;
	public static final int ELDER_GUARD_1 = 1461;
	public static final int ELDER_GUARD_2 = 1462;
	public static final int MONKEY = 1463;
	public static final int BONZARA = 1468;
	public static final int MONKEY_MINDER = 1469;
	public static final int FOREMAN = 1470;
	public static final int JUNGLE_DEMON = 1472;

	//Objects
	public static final int SHIPYARD_GATE = 2439;
	public static final int SHIPYARD_GATE_2 = 2438;
	public static final int HANGAR_EXIT = 4868;
	public static final int HANGAR_EXIT_2 = 4869;
	public static final int REINITIALIZATION_PANEL = 4871;
	public static final int WALL_OF_FLAMES = 4766;
	public static final int WALL_OF_FLAMES_2 = 4765;
	public static final int CRATE_1 = 4714;
	public static final int CRATE_2 = 4717;
	public static final int CRATE_3 = 4716;
	public static final int CRATE_4 = 4715;
	public static final int CRATE_5 = 4718;
	public static final int CRATE_6 = 4719;
	public static final int CRATE_7 = 4721;
	public static final int CRATE_8 = 4722;
	public static final int CRATE_9 = 4723;
	public static final int AMULET_MOULD_CRATE = 4724;
	public static final int BAMBOO_GATE_LEFT = 4787;
	public static final int BAMBOO_GATE_RIGHT = 4788;

	public int dialogueStage = 0;

	private int reward[][] = {
		{DRAGONSTONE, 3},
		{995, 100000} //Lovely money!
	};

	private int expReward[][] = { //to do
	};

	private static final int questPointReward = 3;

	public int getQuestID() {
		return 36;
	}

	public String getQuestName() {
		return "Monkey Madness";
	}

	public String getQuestSaveName() {
		return "monkeymadness";
	}

	public boolean canDoQuest(final Player player) {
		return QuestHandler.questCompleted(player, 33) && QuestHandler.questCompleted(player, 29);
	}

	public void getReward(Player player) {
		for (int[] rewards : reward) {
			player.getInventory().addItemOrDrop(new Item(rewards[0], rewards[1]));
		}
		for (int[] expRewards : expReward) {
			player.getSkill().addExp(expRewards[0], (expRewards[1]));
		}
		player.addQuestPoints(questPointReward);
		player.getActionSender().QPEdit(player.getQuestPoints());
	}

	public void completeQuest(Player player) {
		getReward(player);
		player.getActionSender().sendInterface(12140);
		player.getActionSender().sendItemOnInterface(12145, 250, MONKEY_ITEM);
		player.getActionSender().sendString("You have completed " + getQuestName() + "!", 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("3 Quest Points", 12150);
		player.getActionSender().sendString("100,000 coins", 12151);
		player.getActionSender().sendString("3 Dragonstones", 12152);
		player.getActionSender().sendString("Ability to purchase and wield", 12153);
		player.getActionSender().sendString("the Dragon Scimitar.", 12154);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), 7361);
	}

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		switch (questStage) {
			case QUEST_STARTED:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen in the Grand Tree to begin.", 8147);

				player.getActionSender().sendString("King Narnode Shareen needs my help again. He told", 8149);
				player.getActionSender().sendString("me his 10th Squad has not reported anything back from", 8150);
				player.getActionSender().sendString("Glough's shipyard in Karamja. I need to investigate.", 8151);
				break;
			case SPOKE_TO_CARROTCOCK:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen in the Grand Tree to begin.", 8147);

				player.getActionSender().sendString("@str@" + "King Narnode Shareen needs my help again. He told", 8149);
				player.getActionSender().sendString("@str@" + "me his 10th Squad has not reported anything back from", 8150);
				player.getActionSender().sendString("@str@" + "Glough's shipyard in Karamja. I need to investigate.", 8151);

				player.getActionSender().sendString("I found a gnome named G.L.O Caranock who told me that", 8153);
				player.getActionSender().sendString("perhaps the 10th Squad was blown off course.", 8154);
				player.getActionSender().sendString("I should return to the King.", 8155);
				break;
			case NEW_ORDERS:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen in the Grand Tree to begin.", 8147);

				player.getActionSender().sendString("@str@" + "King Narnode Shareen needs my help again. He told", 8149);
				player.getActionSender().sendString("@str@" + "me his 10th Squad has not reported anything back from", 8150);
				player.getActionSender().sendString("@str@" + "Glough's shipyard in Karamja. I need to investigate.", 8151);

				player.getActionSender().sendString("King Narnode has given me some new orders to give to", 8153);
				player.getActionSender().sendString("his new Tree Guardian, Daero. I should give them to him.", 8154);
				break;
			case ORDERS_FROM_DAERO:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen in the Grand Tree to begin.", 8147);

				player.getActionSender().sendString("@str@" + "King Narnode Shareen needs my help again. He told", 8149);
				player.getActionSender().sendString("@str@" + "me his 10th Squad has not reported anything back from", 8150);
				player.getActionSender().sendString("@str@" + "Glough's shipyard in Karamja. I need to investigate.", 8151);

				player.getActionSender().sendString("Daero and the King need me to go far south of Karamja", 8153);
				player.getActionSender().sendString("to investigate Caranock's claim that Garkor's squad was", 8154);
				player.getActionSender().sendString("indeed blown off course.", 8155);
				break;
			case CRACKED_THE_CODE:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen in the Grand Tree to begin.", 8147);

				player.getActionSender().sendString("@str@" + "King Narnode Shareen needs my help again. He told", 8149);
				player.getActionSender().sendString("@str@" + "me his 10th Squad has not reported anything back from", 8150);
				player.getActionSender().sendString("@str@" + "Glough's shipyard in Karamja. I need to investigate.", 8151);

				player.getActionSender().sendString("I managed to get the hangar reinitialized, and should now", 8153);
				player.getActionSender().sendString("proceed to investigate Caranock's claim that Garkor's", 8154);
				player.getActionSender().sendString("squad was indeed blown off course.", 8155);
				break;
			case LUMBDO_A_DICK:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen in the Grand Tree to begin.", 8147);

				player.getActionSender().sendString("@str@" + "King Narnode Shareen needs my help again. He told", 8149);
				player.getActionSender().sendString("@str@" + "me his 10th Squad has not reported anything back from", 8150);
				player.getActionSender().sendString("@str@" + "Glough's shipyard in Karamja. I need to investigate.", 8151);

				player.getActionSender().sendString("Waydar and I have ended up on 'Crash Island'.", 8153);
				player.getActionSender().sendString("Lumbdo from the 10th Squad is here protecting the", 8154);
				player.getActionSender().sendString("intact gliders, but refuses to take me to the atoll", 8155);
				player.getActionSender().sendString("where Segeant Garkor and the rest of the Squad are.", 8156);
				break;
			case MEANWHILE:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen in the Grand Tree to begin.", 8147);
				player.getActionSender().sendString("@str@" + "King Narnode Shareen needs my help again. He told", 8149);
				player.getActionSender().sendString("@str@" + "me his 10th Squad has not reported anything back from", 8150);
				player.getActionSender().sendString("@str@" + "Glough's shipyard in Karamja. I need to investigate.", 8151);
				player.getActionSender().sendString("@str@" + "Waydar and I have ended up on 'Crash Island'.", 8153);

				player.getActionSender().sendString("It took some convincing, but Lumbdo has taken me", 8155);
				player.getActionSender().sendString("to the mysterious atoll. I need to find Sergeant", 8156);
				player.getActionSender().sendString("Garkor immediately.", 8157);
				break;
			case GARKORS_ORDERS:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen in the Grand Tree to begin.", 8147);
				player.getActionSender().sendString("@str@" + "King Narnode Shareen needs my help again. He told", 8149);
				player.getActionSender().sendString("@str@" + "me his 10th Squad has not reported anything back from", 8150);
				player.getActionSender().sendString("@str@" + "Glough's shipyard in Karamja. I need to investigate.", 8151);
				player.getActionSender().sendString("@str@" + "Waydar and I have ended up on 'Crash Island'.", 8153);
				player.getActionSender().sendString("@str@" + "It took some convincing, but Lumbdo took me", 8155);
				player.getActionSender().sendString("@str@" + "to the mysterious atoll. I need to find Sergeant", 8156);
				player.getActionSender().sendString("@str@" + "Garkor immediately.", 8157);

				player.getActionSender().sendString("I managed to find Sergeant Garkor in this hellish", 8159);
				player.getActionSender().sendString("place. He told me to find the 10th Squad mage, Zooknock,", 8160);
				player.getActionSender().sendString("in the tunnels beneath this Atoll.", 8161);
				break;
			case ZOOKNOCK_SPEECH:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen in the Grand Tree to begin.", 8147);
				player.getActionSender().sendString("@str@" + "King Narnode Shareen needs my help again. He told", 8149);
				player.getActionSender().sendString("@str@" + "me his 10th Squad has not reported anything back from", 8150);
				player.getActionSender().sendString("@str@" + "Glough's shipyard in Karamja. I need to investigate.", 8151);
				player.getActionSender().sendString("@str@" + "Waydar and I have ended up on 'Crash Island'.", 8153);
				player.getActionSender().sendString("@str@" + "It took some convincing, but Lumbdo took me to", 8155);
				player.getActionSender().sendString("@str@" + "the mysterious atoll. I managed to find Sergeant", 8156);
				player.getActionSender().sendString("@str@" + "Garkor and Squad mage Zooknock.", 8157);

				player.getActionSender().sendString("Zooknock said I need 2 items to help the 10th squad:", 8159);
				if (!player.getInventory().ownsItem(MONKEYSPEAK_AMULET) && !player.getInventory().ownsItem(MONKEYSPEAK_AMULET_U)) {
					player.getActionSender().sendString("@dre@1.) Monkeyspeak Amulet:", 8161);
				} else {
					player.getActionSender().sendString("@str@" + "@dre@1.) Monkeyspeak Amulet:", 8161);
				}
				if (!player.getInventory().playerHasItem(ENCHANTED_BAR) && !player.getInventory().ownsItem(MONKEYSPEAK_AMULET)) {
					if (!player.getInventory().playerHasItem(MONKEY_DENTURES)) {
						player.getActionSender().sendString("-Some sort of magical talking monkey trinket.", 8162);
					} else {
						player.getActionSender().sendString("@str@" + "-Some sort of magical talking monkey trinket.", 8162);
					}
					if (!player.getInventory().playerHasItem(GOLD_BAR)) {
						player.getActionSender().sendString("-A Gold bar.", 8163);
					} else {
						player.getActionSender().sendString("@str@" + "-A Gold bar.", 8163);
					}
					if (!player.getInventory().playerHasItem(MONKEYSPEAK_AMULET_MOULD)) {
						player.getActionSender().sendString("-A Monkeyspeak Amulet Mould.", 8164);
					} else {
						player.getActionSender().sendString("@str@" + "-A Monkeyspeak Amulet Mould.", 8164);
					}
				} else {
					if (player.getInventory().ownsItem(MONKEYSPEAK_AMULET) || player.getInventory().ownsItem(MONKEYSPEAK_AMULET_U)) {
						player.getActionSender().sendString("@str@" + "Zooknock told me I need to make the amulet and", 8162);
						player.getActionSender().sendString("@str@" + "to craft it in a place of religious significance to", 8163);
						player.getActionSender().sendString("@str@" + "the monkeys. The temple pyres nearby may work.", 8164);
					} else {
						player.getActionSender().sendString("Zooknock told me I need to make the amulet and", 8162);
						player.getActionSender().sendString("to craft it in a place of religious significance to", 8163);
						player.getActionSender().sendString("the monkeys. The temple pyres nearby may work.", 8164);
					}
				}

				player.getActionSender().sendString("@dre@2.) Monkey Greegree talisman:", 8166);
				if (!player.getInventory().playerHasItem(MONKEY_TALISMAN)) {
					player.getActionSender().sendString("@bla@-A talisman to bind the power of monkey into.", 8167);
				} else {
					player.getActionSender().sendString("@str@" + "@bla@-A talisman to bind the power of monkey into.", 8167);
				}
				if (!player.getInventory().playerHasItem(KARAMJA_MONKEY_BONES)) {
					player.getActionSender().sendString("@bla@-Bones from a Karamjan monkey to guide the talisman.", 8168);
				} else {
					player.getActionSender().sendString("@str@" + "@bla@-Bones from a Karamjan monkey to guide the talisman.", 8168);
				}
				break;
			case MONKEY_MAN:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen in the Grand Tree to begin.", 8147);
				player.getActionSender().sendString("@str@" + "King Narnode Shareen needs my help again. He told", 8149);
				player.getActionSender().sendString("@str@" + "me his 10th Squad has not reported anything back from", 8150);
				player.getActionSender().sendString("@str@" + "Glough's shipyard in Karamja. I need to investigate.", 8151);
				player.getActionSender().sendString("@str@" + "Waydar and I have ended up on 'Crash Island'.", 8153);
				player.getActionSender().sendString("@str@" + "It took some convincing, but Lumbdo took me to", 8155);
				player.getActionSender().sendString("@str@" + "the mysterious atoll. I managed to find Sergeant", 8156);
				player.getActionSender().sendString("@str@" + "Garkor and Squad mage Zooknock.", 8157);

				player.getActionSender().sendString("I managed to make the items Zooknock and Garkor", 8159);
				player.getActionSender().sendString("required and can now safely disguise as a monkey.", 8160);
				player.getActionSender().sendString("I should return to Garkor for further orders.", 8161);
				break;
			case GARKORS_PLAN:
			case KRUKS_PERMISSION:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen in the Grand Tree to begin.", 8147);
				player.getActionSender().sendString("@str@" + "King Narnode Shareen needs my help again. He told", 8149);
				player.getActionSender().sendString("@str@" + "me his 10th Squad has not reported anything back from", 8150);
				player.getActionSender().sendString("@str@" + "Glough's shipyard in Karamja. I need to investigate.", 8151);
				player.getActionSender().sendString("@str@" + "Waydar and I have ended up on 'Crash Island'.", 8153);
				player.getActionSender().sendString("@str@" + "It took some convincing, but Lumbdo took me to", 8155);
				player.getActionSender().sendString("@str@" + "the mysterious atoll. I managed to find Sergeant", 8156);
				player.getActionSender().sendString("@str@" + "Garkor and Squad mage Zooknock.", 8157);
				player.getActionSender().sendString("@str@" + "I managed to make the items Zooknock and Garkor", 8159);
				player.getActionSender().sendString("@str@" + "required and can now safely disguise as a monkey.", 8160);

				player.getActionSender().sendString("Garkor has plans for me involved King Awowogei.", 8162);
				player.getActionSender().sendString("I should gain entrance to his 'palace' and speak", 8163);
				player.getActionSender().sendString("with him.", 8164);
				break;
			case AWOWOGEIS_TEST:
			case INTO_THE_CAGE:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen in the Grand Tree to begin.", 8147);
				player.getActionSender().sendString("@str@" + "King Narnode Shareen needs my help again. He told", 8149);
				player.getActionSender().sendString("@str@" + "me his 10th Squad has not reported anything back from", 8150);
				player.getActionSender().sendString("@str@" + "Glough's shipyard in Karamja. I need to investigate.", 8151);
				player.getActionSender().sendString("@str@" + "Waydar and I have ended up on 'Crash Island'.", 8153);
				player.getActionSender().sendString("@str@" + "It took some convincing, but Lumbdo took me to", 8155);
				player.getActionSender().sendString("@str@" + "the mysterious atoll. I managed to find Sergeant", 8156);
				player.getActionSender().sendString("@str@" + "Garkor and Squad mage Zooknock.", 8157);
				player.getActionSender().sendString("@str@" + "I managed to make the items Zooknock and Garkor", 8159);
				player.getActionSender().sendString("@str@" + "required and can now safely disguise as a monkey.", 8160);
				player.getActionSender().sendString("@str@" + "I gained the favor to enter King Awowogei's 'palace'.", 8162);

				player.getActionSender().sendString("King Awowogei will only believe Garkor's idea of", 8164);
				player.getActionSender().sendString("an alliance with the Karamjan Monkeys if I travel", 8165);
				player.getActionSender().sendString("to Ardougne and free a monkey captive there and", 8166);
				player.getActionSender().sendString("return it to him.", 8167);
				break;
			case THE_GREAT_ESCAPE:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen in the Grand Tree to begin.", 8147);
				player.getActionSender().sendString("@str@" + "King Narnode Shareen needs my help again. He told", 8149);
				player.getActionSender().sendString("@str@" + "me his 10th Squad has not reported anything back from", 8150);
				player.getActionSender().sendString("@str@" + "Glough's shipyard in Karamja. I need to investigate.", 8151);
				player.getActionSender().sendString("@str@" + "Waydar and I have ended up on 'Crash Island'.", 8153);
				player.getActionSender().sendString("@str@" + "It took some convincing, but Lumbdo took me to", 8155);
				player.getActionSender().sendString("@str@" + "the mysterious atoll. I managed to find Sergeant", 8156);
				player.getActionSender().sendString("@str@" + "Garkor and Squad mage Zooknock.", 8157);
				player.getActionSender().sendString("@str@" + "I managed to make the items Zooknock and Garkor", 8159);
				player.getActionSender().sendString("@str@" + "required and can now safely disguise as a monkey.", 8160);
				player.getActionSender().sendString("@str@" + "I gained the favor to enter King Awowogei's 'palace'.", 8162);

				player.getActionSender().sendString("I managed to capture a monkey from the Ardougne", 8164);
				player.getActionSender().sendString("Zoo for King Awowogei. I should carefully take him", 8165);
				player.getActionSender().sendString("back to Awowogei without teleporting.", 8166);
				break;
			case PASSED_THE_TEST:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen in the Grand Tree to begin.", 8147);
				player.getActionSender().sendString("@str@" + "King Narnode Shareen needs my help again. He told", 8149);
				player.getActionSender().sendString("@str@" + "me his 10th Squad has not reported anything back from", 8150);
				player.getActionSender().sendString("@str@" + "Glough's shipyard in Karamja. I need to investigate.", 8151);
				player.getActionSender().sendString("@str@" + "Waydar and I have ended up on 'Crash Island'.", 8153);
				player.getActionSender().sendString("@str@" + "It took some convincing, but Lumbdo took me to", 8155);
				player.getActionSender().sendString("@str@" + "the mysterious atoll. I managed to find Sergeant", 8156);
				player.getActionSender().sendString("@str@" + "Garkor and Squad mage Zooknock.", 8157);
				player.getActionSender().sendString("@str@" + "I managed to make the items Zooknock and Garkor", 8159);
				player.getActionSender().sendString("@str@" + "required and can now safely disguise as a monkey.", 8160);
				player.getActionSender().sendString("@str@" + "I gained the favor to enter King Awowogei's 'palace'.", 8162);
				player.getActionSender().sendString("@str@" + "I managed to capture a monkey from the Ardougne", 8164);
				player.getActionSender().sendString("@str@" + "Zoo and proved myself to Awowogei.", 8165);

				player.getActionSender().sendString("I should talk to Garkor to find out what we", 8167);
				player.getActionSender().sendString("should do now that Awowogei trusts 'me'.", 8168);
				break;
			case GARKORS_RETELLING:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen in the Grand Tree to begin.", 8147);
				player.getActionSender().sendString("@str@" + "King Narnode Shareen needs my help again. He told", 8149);
				player.getActionSender().sendString("@str@" + "me his 10th Squad has not reported anything back from", 8150);
				player.getActionSender().sendString("@str@" + "Glough's shipyard in Karamja. I need to investigate.", 8151);
				player.getActionSender().sendString("@str@" + "Waydar and I have ended up on 'Crash Island'.", 8153);
				player.getActionSender().sendString("@str@" + "It took some convincing, but Lumbdo took me to", 8155);
				player.getActionSender().sendString("@str@" + "the mysterious atoll. I managed to find Sergeant", 8156);
				player.getActionSender().sendString("@str@" + "Garkor and Squad mage Zooknock.", 8157);
				player.getActionSender().sendString("@str@" + "I managed to make the items Zooknock and Garkor", 8159);
				player.getActionSender().sendString("@str@" + "required and can now safely disguise as a monkey.", 8160);
				player.getActionSender().sendString("@str@" + "I gained the favor to enter King Awowogei's 'palace'.", 8162);
				player.getActionSender().sendString("@str@" + "I managed to capture a monkey from the Ardougne", 8164);
				player.getActionSender().sendString("@str@" + "Zoo and proved myself to Awowogei.", 8165);

				player.getActionSender().sendString("Garkor told me of the terrible plans for the", 8167);
				player.getActionSender().sendString("10th Squad as well as myself! I need to speak", 8168);
				player.getActionSender().sendString("with him immediately. What am I going to do?!", 8169);
				break;
			case NEW_MEMEBER:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen in the Grand Tree to begin.", 8147);
				player.getActionSender().sendString("@str@" + "King Narnode Shareen needs my help again. He told", 8149);
				player.getActionSender().sendString("@str@" + "me his 10th Squad has not reported anything back from", 8150);
				player.getActionSender().sendString("@str@" + "Glough's shipyard in Karamja. I need to investigate.", 8151);
				player.getActionSender().sendString("@str@" + "Waydar and I have ended up on 'Crash Island'.", 8153);
				player.getActionSender().sendString("@str@" + "It took some convincing, but Lumbdo took me to", 8155);
				player.getActionSender().sendString("@str@" + "the mysterious atoll. I managed to find Sergeant", 8156);
				player.getActionSender().sendString("@str@" + "Garkor and Squad mage Zooknock.", 8157);
				player.getActionSender().sendString("@str@" + "I managed to make the items Zooknock and Garkor", 8159);
				player.getActionSender().sendString("@str@" + "required and can now safely disguise as a monkey.", 8160);
				player.getActionSender().sendString("@str@" + "I gained the favor to enter King Awowogei's 'palace'.", 8162);
				player.getActionSender().sendString("@str@" + "I managed to capture a monkey from the Ardougne", 8164);
				player.getActionSender().sendString("@str@" + "Zoo and proved myself to Awowogei.", 8165);
				player.getActionSender().sendString("@str@" + "Garkor told me of the terrible plans for the", 8167);
				player.getActionSender().sendString("@str@" + "10th Squad as well as myself.", 8168);

				player.getActionSender().sendString("Garkor gave me a sigil, it signifies I am a real", 8170);
				player.getActionSender().sendString("member of the 10th Squad in order to help in the", 8171);
				player.getActionSender().sendString("final battle between another beast of Glough's", 8172);
				player.getActionSender().sendString("and us. I need to only equip the sigil when I am", 8173);
				player.getActionSender().sendString("ready to begin the fight.", 8174);
				break;
			case FUCK_THE_DEMON:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen in the Grand Tree to begin.", 8147);
				player.getActionSender().sendString("@str@" + "King Narnode Shareen needs my help again. He told", 8149);
				player.getActionSender().sendString("@str@" + "me his 10th Squad has not reported anything back from", 8150);
				player.getActionSender().sendString("@str@" + "Glough's shipyard in Karamja. I need to investigate.", 8151);
				player.getActionSender().sendString("@str@" + "Waydar and I have ended up on 'Crash Island'.", 8153);
				player.getActionSender().sendString("@str@" + "It took some convincing, but Lumbdo took me to", 8155);
				player.getActionSender().sendString("@str@" + "the mysterious atoll. I managed to find Sergeant", 8156);
				player.getActionSender().sendString("@str@" + "Garkor and Squad mage Zooknock.", 8157);
				player.getActionSender().sendString("@str@" + "I managed to make the items Zooknock and Garkor", 8159);
				player.getActionSender().sendString("@str@" + "required and can now safely disguise as a monkey.", 8160);
				player.getActionSender().sendString("@str@" + "I gained the favor to enter King Awowogei's 'palace'.", 8162);
				player.getActionSender().sendString("@str@" + "I managed to capture a monkey from the Ardougne", 8164);
				player.getActionSender().sendString("@str@" + "Zoo and proved myself to Awowogei.", 8165);
				player.getActionSender().sendString("@str@" + "Garkor told me of the terrible plans for the", 8167);
				player.getActionSender().sendString("@str@" + "10th Squad as well as myself.", 8168);
				player.getActionSender().sendString("@str@" + "Garkor gave me a sigil, it signifies I am a real", 8170);
				player.getActionSender().sendString("@str@" + "member of the 10th Squad in order to help in the", 8171);
				player.getActionSender().sendString("@str@" + "final battle with another beast of Glough's.", 8172);

				if (!player.getMMVars().spokeToGarkorEndOfFight) {
					player.getActionSender().sendString("We did it! We defeated the nasty Jungle Demon!", 8174);
					player.getActionSender().sendString("I should talk to Garkor per usual for instructions.", 8175);
				} else {
					player.getActionSender().sendString("We did it! We defeated the nasty Jungle Demon!", 8174);
					player.getActionSender().sendString("I should talk to King Narnode Shareen and tell", 8175);
					player.getActionSender().sendString("him the good news.", 8176);
				}
				break;
			case QUEST_COMPLETE:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen in the Grand Tree to begin.", 8147);
				player.getActionSender().sendString("@str@" + "King Narnode Shareen needs my help again. He told", 8149);
				player.getActionSender().sendString("@str@" + "me his 10th Squad has not reported anything back from", 8150);
				player.getActionSender().sendString("@str@" + "Glough's shipyard in Karamja. I need to investigate.", 8151);
				player.getActionSender().sendString("@str@" + "Waydar and I have ended up on 'Crash Island'.", 8153);
				player.getActionSender().sendString("@str@" + "It took some convincing, but Lumbdo took me to", 8155);
				player.getActionSender().sendString("@str@" + "the mysterious atoll. I managed to find Sergeant", 8156);
				player.getActionSender().sendString("@str@" + "Garkor and Squad mage Zooknock.", 8157);
				player.getActionSender().sendString("@str@" + "I managed to make the items Zooknock and Garkor", 8159);
				player.getActionSender().sendString("@str@" + "required and can now safely disguise as a monkey.", 8160);
				player.getActionSender().sendString("@str@" + "I gained the favor to enter King Awowogei's 'palace'.", 8162);
				player.getActionSender().sendString("@str@" + "I managed to capture a monkey from the Ardougne", 8164);
				player.getActionSender().sendString("@str@" + "Zoo and proved myself to Awowogei.", 8165);
				player.getActionSender().sendString("@str@" + "Garkor told me of the terrible plans for the", 8167);
				player.getActionSender().sendString("@str@" + "10th Squad as well as myself.", 8168);
				player.getActionSender().sendString("@str@" + "Garkor gave me a sigil, it signifies I am a real", 8170);
				player.getActionSender().sendString("@str@" + "member of the 10th Squad in order to help in the", 8171);
				player.getActionSender().sendString("@str@" + "final battle with another beast of Glough's.", 8172);
				player.getActionSender().sendString("@str@" + "I defeated the nasty Jungle Demon with help from", 8174);
				player.getActionSender().sendString("@str@" + "the Royal 10th Squad of gnomes and saved the day.", 8175);

				player.getActionSender().sendString("@red@" + "You have completed this quest!", 8177);
				break;
			default:
				player.getActionSender().sendString("Talk to @dre@King Narnode Shareen @bla@in the @dre@Grand Tree @bla@to begin.", 8147);
				player.getActionSender().sendString("@dre@Requirements:", 8148);
				if (QuestHandler.questCompleted(player, 33)) {
					player.getActionSender().sendString("@str@-Tree Gnome Village.", 8150);
				} else {
					player.getActionSender().sendString("@dbl@-Tree Gnome Village.", 8150);
				}
				if (QuestHandler.questCompleted(player, 29)) {
					player.getActionSender().sendString("@str@-The Grand Tree.", 8151);
				} else {
					player.getActionSender().sendString("@dbl@-The Grand Tree.", 8151);
				}
				player.getActionSender().sendString("-Ability to defeat a level 195 Jungle Demon.", 8152);
				break;
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 11132);
	}

	public boolean questCompleted(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if (questStage >= QUEST_COMPLETE) {
			return true;
		}
		return false;
	}

	public void sendQuestTabStatus(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if ((questStage >= QUEST_STARTED) && (questStage < QUEST_COMPLETE)) {
			player.getActionSender().sendString("@yel@" + getQuestName(), 11132);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 11132);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 11132);
		}
	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
		player.getActionSender().sendString(getQuestName(), 8144);
	}

	public static void deleteMonkey(final Player player, int location) {
		switch (location) {
			case 0: //Inventory
				player.getInventory().removeItem(new Item(MONKEY_ITEM));
				break;
			case 1: //Bank
				player.getBankManager().remove(new Item(4033));
				break;
		}
		player.addPcPoints(225, player);
		player.getActionSender().sendMessage("Your pet monkey has fled due to Monkey Madness.");
		player.getActionSender().sendMessage("You have been restored 225 commendation points.");
	}

	public static void openGliderPuzzle(final Player player) {
		if (!player.getMMVars().startedGliderPuzzle) {
			player.getMMVars().getPuzzle().initPuzzle(6661234);
			player.getMMVars().startedGliderPuzzle = true;
		}
		player.getMMVars().getPuzzle().loadClueInterface(6661234);
	}

	public static void openChapterInterface(final Player player) {
		player.getActionSender().sendInterface(11104);
		for (int i = 0; i < 11; i++) {
			player.getActionSender().sendString("", 11112 + i);
		}
	}

	public static void reinitializeHangar(final Player player) {
		if (player.getQuestStage(36) == ORDERS_FROM_DAERO) {
			player.setStopPacket(true);
			player.getActionSender().sendMessage("You begin to hear a low rumbling sound...");
			player.getActionSender().shakeScreen(2, 10, 10, 8);
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer b) {
					b.stop();
				}

				@Override
				public void stop() {
					player.fadeTeleport(MonkeyMadness.HANGAR_INITIALIZED);
					player.getActionSender().resetCamera();
					player.getActionSender().sendMessage("The Military Gliders have been reinitialized!");
					player.setQuestStage(36, MonkeyMadness.CRACKED_THE_CODE);
					player.setStopPacket(false);
				}
			}, 8);
		}
	}

	public static void startFinalFight(final Player player) {
		player.setStopPacket(true);
		player.getActionSender().shakeScreen(2, 10, 10, 8);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				b.stop();
			}

			@Override
			public void stop() {
				player.fadeTeleport(FINAL_FIGHT.modifyZ((player.getIndex() * 4) + 1));
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						b.stop();
					}

					@Override
					public void stop() {
						player.getActionSender().resetCamera();
						player.getDialogue().setLastNpcTalk(GARKOR);
						player.getDialogue().sendNpcChat("Ready yourself, human: the final battle begins!", ANGRY_1);
						player.getDialogue().endDialogue();
						spawnFinalFightNpcs(player, player.getPosition().getZ());
					}
				}, 5);
				player.setStopPacket(false);
			}
		}, 8);
	}

	public static void spawnFinalFightNpcs(final Player player, final int z) {
		for (FinalFightNpcs n : FinalFightNpcs.values()) {
			player.getMMVars().getFinalFightNpcs().add(ApeAtollNpcs.spawnFinalFightNpc(n, z, player));
			player.getActionSender().sendStillGraphic(EventsConstants.RANDOM_EVENT_GRAPHIC, n.getPosition(), 0);
		}
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				b.stop();
			}

			@Override
			public void stop() {
				Position spawnPos = new Position(2728, 9170, z);
				Npc npc = new Npc(JUNGLE_DEMON);
				npc.setPosition(spawnPos);
				npc.setSpawnPosition(spawnPos);
				npc.setWalkType(Npc.WalkType.WALK);
				npc.setMaxWalk(new Position(spawnPos.getX() + 50, spawnPos.getY() + 50, z));
				npc.setMinWalk(new Position(spawnPos.getX() - 50, spawnPos.getY() - 50, z));
				npc.setNeedsRespawn(false);
				npc.setPlayerOwner(player.getIndex());
				World.register(npc);
				player.getMMVars().jungleDemon = npc;
				startFinalFightLogic(player, player.getPosition().getZ());
			}
		}, 3);
	}

	public static void startFinalFightLogic(final Player player, int z) {
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				if (!player.Area(2688, 2748, 9154, 9214)) {
					b.stop();
					return;
				}
				if (player.getMMVars().jungleDemon != null && !player.getMMVars().jungleDemon.isDead() && !player.getMMVars().jungleDemon.isAttacking()) {
					CombatManager.attack(player.getMMVars().jungleDemon, player);
				}
				for (Npc n : player.getMMVars().getFinalFightNpcs()) {
					if (!player.getMMVars().jungleDemon.isDead() && Misc.goodDistance(n.getPosition(), player.getMMVars().jungleDemon.getPosition(), 5) && !n.isAttacking()) {
						CombatManager.attack(n, player.getMMVars().jungleDemon);
					}
				}
			}

			@Override
			public void stop() {
				endFinalFight(player);
			}
		}, 2);
	}

	public static void endFinalFight(final Player player) {
		for (Npc n : player.getMMVars().getFinalFightNpcs()) {
			if (n != null) {
				NpcLoader.destroyNpc(n);
			}
		}
		if (player.getMMVars().jungleDemon != null && !player.getMMVars().jungleDemon.isDead()) {
			NpcLoader.destroyNpc(player.getMMVars().jungleDemon);
			player.getMMVars().jungleDemon = null;
		}
		player.getMMVars().getFinalFightNpcs().clear();
	}

	public void handleDeath(final Player player, Npc npc) {
		if (npc.getNpcId() == 1472 && player.getQuestStage(36) == NEW_MEMEBER) {
			player.setQuestStage(36, FUCK_THE_DEMON);
			for (Npc n : player.getMMVars().getFinalFightNpcs()) {
				if (n != null && n.getNpcId() == GARKOR_2) {
					player.getActionSender().createPlayerHints(1, n.getIndex());
				}
			}
		}
	}

	public boolean itemHandling(final Player player, int itemId) {
		switch (itemId) {
			case SPARE_CONTROLS:
				if (GLIDER_PUZZLE_ITEMS == null) {
					GLIDER_PUZZLE_ITEMS = new Item[ClueScroll.PUZZLE_LENGTH];
					for (int i = 0; i < ClueScroll.PUZZLE_LENGTH; i++) {
						GLIDER_PUZZLE_ITEMS[i] = new Item(GLIDER_PUZZLE[i]);
					}
				}
				player.setStatedInterface("GLIDER_PUZZLE_HINT");
				player.getActionSender().sendInterface(PUZZLE_INTERFACE);
				player.getActionSender().sendUpdateItems(11130, GLIDER_PUZZLE_ITEMS);
				return true;
		}
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		if ((firstItem == WOOL && secondItem == MONKEYSPEAK_AMULET_U) || (firstItem == MONKEYSPEAK_AMULET_U && secondItem == WOOL)) {
			player.getInventory().removeItem(new Item(WOOL));
			player.getInventory().replaceItemWithItem(new Item(MONKEYSPEAK_AMULET_U), new Item(MONKEYSPEAK_AMULET));
			player.getActionSender().sendMessage("You put some string on your amulet. It makes a slight 'Ook' sound.");
			return true;
		}
		return false;
	}

	public boolean doItemOnObject(final Player player, int object, int item) {
		if (object == WALL_OF_FLAMES || object == WALL_OF_FLAMES_2) {
			if (item == ENCHANTED_BAR) {
				if (player.getInventory().playerHasItem(MONKEYSPEAK_AMULET_MOULD)) {
					player.getUpdateFlags().sendAnimation(899);
					player.getActionSender().sendSound(469, 0, 0);
					player.getActionSender().sendMessage("You melt the enchanted gold into an unstrung amulet.");
					player.getInventory().replaceItemWithItem(new Item(ENCHANTED_BAR), new Item(MONKEYSPEAK_AMULET_U));
					return true;
				} else {
					player.getDialogue().sendStatement("You need a monkey amulet mould to do this.");
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}
		return false;
	}

	public boolean doItemOnNpc(Player player, int itemId, Npc npc) {
		return false;
	}

	public boolean doNpcClicking(Player player, Npc npc) {
		return false;
	}

	public boolean doObjectClicking(final Player player, int object, int x, int y) {
		switch (object) {
			case 366: //Spare controls crate
				if (x == 2588 && y == 4508) {
					player.getActionSender().sendMessage("You search the crate...");
					player.setStopPacket(true);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							if (!player.getInventory().playerHasItem(SPARE_CONTROLS) && player.getInventory().getItemContainer().freeSlots() > 0) {
								player.getActionSender().sendMessage("...you find a spare control panel.");
								player.getInventory().addItem(new Item(SPARE_CONTROLS));
							}
							player.setStopPacket(false);
						}
					}, 2);
				}
				return false;
			case 4771: //AWOWOGEI
				Dialogues.startDialogue(player, AWOWOGEI);
				return true;
			case 4774:
				Ladders.climbLadder(player, new Position(2728, 2766, 2));
				return true;
			case 4776:
				Ladders.climbLadder(player, new Position(2730, 2766, 0));
				return true;
			case 4777:
				Ladders.climbLadder(player, new Position(2712, 2766, 0));
				return true;
			case 4775:
				Ladders.climbLadder(player, new Position(2714, 2766, 2));
				return true;
			case BAMBOO_GATE_LEFT:
			case BAMBOO_GATE_RIGHT:
				player.setStopPacket(true);
				player.getActionSender().sendMessage("The monkey guards give you a wink and a nod as you pass through the gate.", true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						player.getActionSender().walkTo(0, player.getPosition().getY() < 2766 ? 2 : -2, true);
						/*GameObject o = new GameObject(4790, 2722, 2766, 0, 2, 10, -1, 1);
						 GameObject ob = new GameObject(Constants.EMPTY_OBJECT, 2721, 2766, 0, 2, 10, 4788, 1);
						 GameObject oc = new GameObject(4789, 2719, 2766, 0, 0, 10, 4787, 1); */
						new GameObject(4790, 2722, 2766, 0, 2, 10, -1, 1);
						new GameObject(Constants.EMPTY_OBJECT, 2721, 2766, 0, 2, 10, 4788, 1);
						new GameObject(4789, 2719, 2766, 0, 0, 10, 4787, 1);
						b.stop();
					}

					@Override
					public void stop() {
						player.setStopPacket(false);
					}
				}, 2);
				return true;
			case REINITIALIZATION_PANEL:
				if (player.getQuestStage(36) == ORDERS_FROM_DAERO) {
					openGliderPuzzle(player);
				} else {
					player.getActionSender().sendMessage("The hangar has been reinitialized.");
				}
				return true;
			case HANGAR_EXIT:
			case HANGAR_EXIT_2:
				player.fadeTeleport(new Position(2412, 3499, 0));
				return true;
			case SHIPYARD_GATE:
			case SHIPYARD_GATE_2:
				if (player.getQuestStage(36) > 0) {
					if (!player.getMMVars().openGate() && player.getPosition().getX() < 2945) {
						Dialogues.startDialogue(player, SHIPYARD_WORKER);
						return true;
					} else {
						player.getActionSender().walkThroughDoor(object, x, y, 0);
						player.getActionSender().walkTo(player.getPosition().getX() < 2945 ? 1 : -1, 0, true);
						return true;
					}
				}
			case CRATE_1:
				player.getActionSender().sendMessage("You search the crate...");
				player.getUpdateFlags().sendAnimation(832);
				player.setStopPacket(true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						Dialogues.startDialogue(player, 4714999);
						b.stop();
					}

					@Override
					public void stop() {
						player.setStopPacket(false);
					}
				}, 3);
				return true;
			case CRATE_2:
				player.getActionSender().sendMessage("You search the crate...");
				player.getUpdateFlags().sendAnimation(832);
				player.setStopPacket(true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						player.getActionSender().sendMessage("You find some wool.");
						player.getInventory().addItem(new Item(WOOL));
						b.stop();
					}

					@Override
					public void stop() {
						player.setStopPacket(false);
					}
				}, 3);
				return true;
			case CRATE_3:
				player.getActionSender().sendMessage("You search the crate...");
				player.getUpdateFlags().sendAnimation(832);
				player.setStopPacket(true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						player.getActionSender().sendMessage("You find some thread.");
						player.getInventory().addItem(new Item(1734));
						b.stop();
					}

					@Override
					public void stop() {
						player.setStopPacket(false);
					}
				}, 3);
				return true;
			case CRATE_4:
				player.getActionSender().sendMessage("You search the crate...");
				player.getUpdateFlags().sendAnimation(832);
				player.setStopPacket(true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						if (!player.getInventory().playerHasItem(MONKEY_DENTURES)) {
							player.getActionSender().sendMessage("You find a magical talking pair of Monkey Dentures.");
							player.getInventory().addItem(new Item(MONKEY_DENTURES));
						} else {
							player.getActionSender().sendMessage("You already have a pair of Monkey Dentures.");
						}
						b.stop();
					}

					@Override
					public void stop() {
						player.setStopPacket(false);
					}
				}, 3);
				return true;
			case CRATE_5:
				player.getActionSender().sendMessage("You search the crate...");
				player.getUpdateFlags().sendAnimation(832);
				player.setStopPacket(true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						player.getActionSender().sendMessage("You find a needle.");
						player.getInventory().addItem(new Item(1733));
						b.stop();
					}

					@Override
					public void stop() {
						player.setStopPacket(false);
					}
				}, 3);
				return true;
			case CRATE_6:
				player.getActionSender().sendMessage("You search the crate...");
				player.getUpdateFlags().sendAnimation(832);
				player.setStopPacket(true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						player.getActionSender().sendMessage("You find a tinderbox.");
						player.getInventory().addItem(new Item(590));
						b.stop();
					}

					@Override
					public void stop() {
						player.setStopPacket(false);
					}
				}, 3);
				return true;
			case CRATE_7:
				player.getActionSender().sendMessage("You search the crate...");
				player.getUpdateFlags().sendAnimation(832);
				player.setStopPacket(true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						player.getActionSender().sendMessage("You find a knife.");
						player.getInventory().addItem(new Item(946));
						b.stop();
					}

					@Override
					public void stop() {
						player.setStopPacket(false);
					}
				}, 3);
				return true;
			case CRATE_8:
				player.getActionSender().sendMessage("You search the crate...");
				player.getUpdateFlags().sendAnimation(832);
				player.setStopPacket(true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						player.getActionSender().sendMessage("You find a chisel.");
						player.getInventory().addItem(new Item(1755));
						b.stop();
					}

					@Override
					public void stop() {
						player.setStopPacket(false);
					}
				}, 3);
				return true;
			case CRATE_9:
				player.getActionSender().sendMessage("You search the crate...");
				player.getUpdateFlags().sendAnimation(832);
				player.setStopPacket(true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						player.getActionSender().sendMessage("You find it full of bananas.");
						player.getInventory().addItemOrDrop(new Item(1963));
						b.stop();
					}

					@Override
					public void stop() {
						player.setStopPacket(false);
					}
				}, 3);
				return true;
			case AMULET_MOULD_CRATE:
				player.getActionSender().sendMessage("You search the crate...");
				player.getUpdateFlags().sendAnimation(832);
				player.setStopPacket(true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						if (!player.getInventory().playerHasItem(MONKEYSPEAK_AMULET_MOULD)) {
							player.getActionSender().sendMessage("You find a monkey amulet mould!");
							player.getInventory().addItemOrDrop(new Item(MONKEYSPEAK_AMULET_MOULD));
						} else {
							player.getActionSender().sendMessage("You already have a monkey amulet mould.");
						}
						b.stop();
					}

					@Override
					public void stop() {
						player.setStopPacket(false);
					}
				}, 3);
				return true;

		}
		return false;
	}

	public boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
		switch (object) {

		}
		return false;
	}

	public static boolean amuletAndTalisman(final Player player) {
		return player.getMMVars().gotAmulet() && player.getMMVars().gotTalisman();
	}

	public static int getFirstBonesInInventory(final Player player) {
		for (Item i : player.getInventory().getItemContainer().toArray()) {
			if (i == null || i.getId() == -1) {
				continue;
			}
			if (GreeGreeData.talismanForBones(i.getId()) != -1) {
				return i.getId();
			}
		}
		return 0;
	}

	public static String getMonkeyTransformName(final Player player) {
		GreeGreeData g = GreeGreeData.forItemId(player.getEquipment().getId(Constants.WEAPON));
		if (g != null) {
			return g.name().substring(0, g.name().indexOf('_'));
		}
		return "null";
	}

	public static void meanwhile(final Player player) {
		player.setStopPacket(true);
		player.setInCutscene(true);
		switch (player.getQuestStage(36)) {
			case LUMBDO_A_DICK:
				player.setQuestStage(36, MEANWHILE);
				player.getActionSender().sendWalkableInterface(8677);
				player.getActionSender().sendMapState(2);
				player.setStopPacket(true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					int count = 1;

					@Override
					public void execute(CycleEventContainer b) {
						DialogueManager d = player.getDialogue();
						player.setStopPacket(true);
						switch (count) {
							case 1:
								d.setLastNpcTalk(FOREMAN);
								d.sendNpcChat("The workers are getting restless, Caranock.", CONTENT);
								break;
							case 2:
								d.setLastNpcTalk(GLO_CARANOCK);
								d.sendNpcChat("I know...", CONTENT);
								break;
							case 3:
								d.setLastNpcTalk(FOREMAN);
								d.sendNpcChat("All this talk of Glough being replaced doesn't bode well", "for... how shall I put this... their morale.", CONTENT);
								break;
							case 4:
								d.setLastNpcTalk(GLO_CARANOCK);
								d.sendNpcChat("Look, I know.", CONTENT);
								break;
							case 5:
								d.setLastNpcTalk(FOREMAN);
								d.sendNpcChat("Those are all men with children to feed. Famished", "families. Worried wives. All of us rely on this shipyard.", CONTENT);
								break;
							case 6:
								d.sendNpcChat("If something isn't done soon there'll be revolt. And I", "won't be able to stop it.", CONTENT);
								break;
							case 7:
								d.setLastNpcTalk(GLO_CARANOCK);
								d.sendNpcChat("Stop worrying. I'm working on something.", CONTENT);
								break;
							case 8:
								d.setLastNpcTalk(FOREMAN);
								d.sendNpcChat("What something? You're always working on something.", "all we ever hear is bad news.", CONTENT);
								break;
							case 9:
								d.sendNpcChat("First, Glough disappears. Then news of a missing squad", "of the Royal Guard in our area. And what about that", "human sent by the king?", CONTENT);
								break;
							case 10:
								d.setLastNpcTalk(GLO_CARANOCK);
								d.sendNpcChat("The human means nothing. If it becomes too much", "trouble, I will simply have it... removed. In the", "meantime, let it continue to search for that blasted 10th", "Squad.", CONTENT);
								break;
							case 11:
								d.setLastNpcTalk(FOREMAN);
								d.sendNpcChat("I am still worried. What am I meant to tell the men?", CONTENT);
								break;
							case 12:
								d.setLastNpcTalk(GLO_CARANOCK);
								d.sendNpcChat("Stop worrying. I'm working on something, Glough left", "a few of his agents in the Gnome airforce.", CONTENT);
								break;
							case 13:
								d.sendNpcChat("For now tell your men to continue work on the", "battleships. Give me some time.", CONTENT);
								break;
							case 14:
								d.setLastNpcTalk(FOREMAN);
								d.sendNpcChat("I hope you're right, Caranock, for your sake. My sake.", "For all of our sakes...", CONTENT);
								break;
							case 15:
								b.stop();
						}
						count++;
					}

					@Override
					public void stop() {
						player.setStopPacket(false);
						player.getActionSender().sendWalkableInterface(-1);
						player.teleport(APE_ATOLL_LANDING);
						CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
							@Override
							public void execute(CycleEventContainer b) {
								b.stop();
							}

							@Override
							public void stop() {
								openChapterInterface(player);
								player.getActionSender().sendString("Monkey Madness: Chapter 2", 11112);
								player.getActionSender().sendString("In which our hero finds himself engaging in severe quantities", 11115);
								player.getActionSender().sendString("of monkey business.", 11116);
							}
						}, 3);
						player.getDialogue().dontCloseInterface();
						player.getActionSender().sendMapState(0);
						player.setInCutscene(false);
					}
				}, 8);
				//Dialogues.startDialogue(player, 1223334444);
				return;
			case ZOOKNOCK_SPEECH:
				player.setQuestStage(36, MONKEY_MAN);
				player.getActionSender().sendWalkableInterface(8677);
				player.getActionSender().sendMapState(2);
				player.setStopPacket(true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					int count = 1;

					@Override
					public void execute(CycleEventContainer b) {
						DialogueManager d = player.getDialogue();
						player.setStopPacket(true);
						switch (count) {
							case 1:
								d.setLastNpcTalk(GLO_CARANOCK);
								d.sendNpcChat("It is good of you to meet with me, Waydar.", CONTENT);
								break;
							case 2:
								d.setLastNpcTalk(WAYDAR);
								d.sendNpcChat("It is good to see you again, Caranock. It is a strange", "island these monkeys inhabit.", CONTENT);
								break;
							case 3:
								d.setLastNpcTalk(GLO_CARANOCK);
								d.sendNpcChat("Well observed. How have you been keeping yourself", "occupied?", CONTENT);
								break;
							case 4:
								d.setLastNpcTalk(WAYDAR);
								d.sendNpcChat("I am now a Flight Commander. My duties include", "testing Glough's prototype military glider.", CONTENT);
								break;
							case 5:
								d.setLastNpcTalk(GLO_CARANOCK);
								d.sendNpcChat("My my. How things have changed somewhat since", "Glough's time... Now, what of the human?", CONTENT);
								break;
							case 6:
								d.setLastNpcTalk(WAYDAR);
								d.sendNpcChat("The human? Just somebody Narnode appears to have", "taken a fancy to. It is hard to tell why. I suspect the", "human was involved with Glough's fall from grace.", CONTENT);
								break;
							case 7:
								d.setLastNpcTalk(GLO_CARANOCK);
								d.sendNpcChat("You may be right. Never mind, there are greater", "matters afoot. With Glough gone, it falls to us to", "continue with his plans.", CONTENT);
								break;
							case 8:
								d.setLastNpcTalk(GLO_CARANOCK);
								d.sendNpcChat("Also, the shipyard workers are becoming restless.", CONTENT);
								break;
							case 9:
								d.setLastNpcTalk(WAYDAR);
								d.sendNpcChat("I see. What do you have in mind?", CONTENT);
								break;
							case 10:
								d.setLastNpcTalk(GLO_CARANOCK);
								d.sendNpcChat("Money for me, Waydar, and promotion for you. As", "you know the 10th Squad of the Royal Guard are", "slightly worse for wear on this island.", CONTENT);
								break;
							case 11:
								d.setLastNpcTalk(WAYDAR);
								d.sendNpcChat("This I know. But I don't see how it leads to money or", "a promotion.", CONTENT);
								break;
							case 12:
								d.setLastNpcTalk(GLO_CARANOCK);
								d.sendNpcChat("What if they were to die? An entire squad of the Royal", "Guard goes missing in the jungle of Karamja... We", "could blame it on the human.", CONTENT);
								break;
							case 13:
								d.setLastNpcTalk(WAYDAR);
								d.sendNpcChat("Narnode would be furious.", CONTENT);
								break;
							case 14:
								d.setLastNpcTalk(GLO_CARANOCK);
								d.sendNpcChat("Precisely. He might even order an invasion. At the", "very least he'll step up the defense. More orders for me,", "promotion for you.", CONTENT);
								break;
							case 15:
								d.setLastNpcTalk(WAYDAR);
								d.sendNpcChat("Very clever. It might also serve us well to remind", "Narnode of Bolren's situation.", CONTENT);
							case 16:
								d.setLastNpcTalk(GLO_CARANOCK);
								d.sendNpcChat("Ah yes, all that trouble with the Khazard. Last I heard,", "Bolren had retrieved the orbs of protection. Apparently", "some human lent their assistance.", CONTENT);
								break;
							case 17:
								d.setLastNpcTalk(WAYDAR);
								d.sendNpcChat("Really? Typical meddling human behavior.", "Nevertheless, it will stoke fires of worry. After all, the", "battle still continues.", CONTENT);
								break;
							case 18:
								d.setLastNpcTalk(GLO_CARANOCK);
								d.sendNpcChat("I agree. Anyhow, we don't want your human", "wondering as to your whereabouts. When the time is", "right, don't hesitate to... dispose of it.", CONTENT);
								break;
							case 19:
								d.setLastNpcTalk(WAYDAR);
								d.sendNpcChat("Understood. Military gliders are after all, an untested", "form of transport...", CONTENT);
								break;
							case 20:
								b.stop();
						}
						count++;
					}

					@Override
					public void stop() {
						player.setStopPacket(false);
						player.getActionSender().sendWalkableInterface(-1);
						player.getActionSender().removeInterfaces();
						CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
							@Override
							public void execute(CycleEventContainer b) {
								b.stop();
							}

							@Override
							public void stop() {
								openChapterInterface(player);
								player.getActionSender().sendString("Monkey Madness: Chapter 3", 11112);
								player.getActionSender().sendString("In which our hero finds himself contending with life as a", 11115);
								player.getActionSender().sendString("monkey.", 11116);
							}
						}, 3);
						player.getDialogue().dontCloseInterface();
						player.getActionSender().sendMapState(0);
						player.setInCutscene(false);
					}
				}, 8);
				//Dialogues.startDialogue(player, 444433322);
				return;
			case PASSED_THE_TEST:
				player.setQuestStage(36, GARKORS_RETELLING);
				player.getActionSender().sendWalkableInterface(8677);
				player.getActionSender().sendMapState(2);
				player.setStopPacket(true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					int count = 1;

					@Override
					public void execute(CycleEventContainer b) {
						DialogueManager d = player.getDialogue();
						player.setStopPacket(true);
						switch (count) {
							case 1:
								d.setLastNpcTalk(GLO_CARANOCK);
								d.sendNpcChat("Good evening, Awowogei.", CONTENT);
								break;
							case 2:
								d.setLastNpcTalk(AWOWOGEI);
								d.sendNpcChat("It is always dark here, Gnome. Why have you asked", "to see me in private?", CONTENT);
								break;
							case 3:
								d.setLastNpcTalk(WAYDAR);
								d.sendNpcChat("Caranock and I have a suggestion to make.", CONTENT);
								break;
							case 4:
								d.setLastNpcTalk(AWOWOGEI);
								d.sendNpcChat("Then be quick about it.", CONTENT);
								break;
							case 5:
								d.setLastNpcTalk(WAYDAR);
								d.sendNpcChat("The foot soldiers of the Royal Guard in your jail...", CONTENT);
								break;
							case 6:
								d.setLastNpcTalk(GLO_CARANOCK);
								d.sendNpcChat("Would it not be easier if they were somehow just to...", "die?", CONTENT);
								break;
							case 7:
								d.setLastNpcTalk(AWOWOGEI);
								d.sendNpcChat("Why would I want to do that? Your king would", "declare war on my island.", CONTENT);
								break;
							case 8:
								d.setLastNpcTalk(GLO_CARANOCK);
								d.sendNpcChat("I can assure you he will not. We will lay the blame", "at the humans' feet.", CONTENT);
								break;
							case 9:
								d.sendNpcChat("Narnode will indeed declare war, not against you,", "but against humankind.", CONTENT);
								break;
							case 10:
								d.setLastNpcTalk(WAYDAR);
								d.sendNpcChat("You are of course welcome to your share of the profits.", CONTENT);
								break;
							case 11:
								d.setLastNpcTalk(AWOWOGEI);
								d.sendNpcChat("Intriguing. I have recently secured an alliance with", "the northern monkeys, which may prove useful.", CONTENT);
								break;
							case 12:
								d.setLastNpcTalk(AWOWOGEI);
								d.sendNpcChat("What would you have me do?", CONTENT);
								break;
							case 13:
								d.setLastNpcTalk(GLO_CARANOCK);
								d.sendNpcChat("Kill the foot soldiers and the rest of the 10th Squad. My", "superior has sent you a few tricks which may prove", "useful.", CONTENT);
								break;
							case 14:
								d.setLastNpcTalk(AWOWOGEI);
								d.sendNpcChat("Such as?", CONTENT);
								break;
							case 15:
								d.setLastNpcTalk(GLO_CARANOCK);
								d.sendNpcChat("High magic: the ability to summon the entire 10th Squad", "to a single location... and-", CONTENT);
								break;
							case 16:
								d.setLastNpcTalk(AWOWOGEI);
								d.sendNpcChat("Even those who escaped?", CONTENT);
								break;
							case 17:
								d.setLastNpcTalk(GLO_CARANOCK);
								d.sendNpcChat("Yes. And of course you will also receive access to one", "of his 'pets'.", CONTENT);
								break;
							case 18:
								d.setLastNpcTalk(GLO_CARANOCK);
								d.sendNpcChat("You must be careful with these, as you have only one", "use of each. Ensure you set your trap well, none", "must survive lest they spread the truth.", CONTENT);
								break;
							case 19:
								d.setLastNpcTalk(WAYDAR);
								d.sendNpcChat("What of my human?", CONTENT);
								break;
							case 20:
								d.setLastNpcTalk(AWOWOGEI);
								d.sendNpcChat("What human?", CONTENT);
								break;
							case 21:
								d.setLastNpcTalk(GLO_CARANOCK);
								d.sendNpcChat("Ignore him. My colleague's official mission was to look", "after a human in the area, but don't worry, it is", "probably dead already.", CONTENT);
								break;
							case 22:
								d.setLastNpcTalk(AWOWOGEI);
								d.sendNpcChat("I should hope so, for both of your sakes.", CONTENT);
								break;
							case 23:
								d.setLastNpcTalk(AWOWOGEI);
								d.sendNpcChat("Very well. I shall let you know when I have dealt", "with the Royal Guard.", CONTENT);
								break;
							case 24:
								d.setLastNpcTalk(GLO_CARANOCK);
								d.sendNpcChat("Good luck, Awowogei.", CONTENT);
								break;
							case 25:
								d.setLastNpcTalk(AWOWOGEI);
								d.sendNpcChat("With access to one of Glough's 'pets' I don't think I'll", "need it...", CONTENT);
								break;
							case 26:
								b.stop();
						}
						count++;
					}

					@Override
					public void stop() {
						player.setStopPacket(false);
						player.getActionSender().sendWalkableInterface(-1);
						player.getActionSender().removeInterfaces();
						CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
							@Override
							public void execute(CycleEventContainer b) {
								b.stop();
							}

							@Override
							public void stop() {
								openChapterInterface(player);
								player.getActionSender().sendString("Monkey Madness: Chapter 4", 11112);
								player.getActionSender().sendString("The Final Battle", 11115);
								player.getDialogue().dontCloseInterface();
							}
						}, 3);
						player.getDialogue().dontCloseInterface();
						player.getActionSender().sendMapState(0);
						player.setInCutscene(false);
					}
				}, 8);
				return;
		}
		return;
	}

	public boolean sendDialogue(final Player player, final int id, int chatId, int optionId, int npcChatId) {
		DialogueManager d = player.getDialogue();
		switch (id) { //Npc ID
			case 885789: //Sigil
				if (player.getMMVars().isMonkey()) {
					d.sendStatement("You cannot be a monkey to be teleported to the final fight.");
					d.endDialogue();
					return true;
				}
				switch (d.getChatId()) {
					case 1:
						d.sendStatement("Let the sigil teleport you when worn?");
						return true;
					case 2:
						d.sendOption("Yes.", "No.");
						return true;
					case 3:
						switch (optionId) {
							case 1:
								player.getActionSender().removeInterfaces();
								player.getEquipment().equip(player.getInventory().getItemContainer().getSlotById(4035));
								startFinalFight(player);
								return true;
							case 2:
								player.getActionSender().removeInterfaces();
								d.endDialogue();
								return true;
						}
				}
				return false;
			case 4714999:
				switch (d.getChatId()) {
					case 1:
						d.sendStatement("You find a hole in the floor under the crate! All you can see is", "the faint glimmer of light from extremely far below.");
						return true;
					case 2:
						d.sendOption("Go down the hole.", "Do nothing.");
						return true;
					case 3:
						switch (optionId) {
							case 1:
								d.sendStatement("You being to lower yourself into the hole...");
								return true;
							case 2:
								d.endDialogue();
								player.getActionSender().removeInterfaces();
								return true;
						}
					case 4:
						d.endDialogue();
						player.fadeTeleport(new Position(2801, 9169, 0));
						CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
							@Override
							public void execute(CycleEventContainer b) {
								b.stop();
							}

							@Override
							public void stop() {
								if (player.getSkill().getLevel()[Skill.AGILITY] < 51) {
									player.getActionSender().sendMessage("You were not agile enough and fell down the hole!");
									player.hit(11, HitType.NORMAL);
								}
							}
						}, 5);
						return true;
				}
				return false;
			case KING_NARNODE:
				if (!QuestHandler.questCompleted(player, 29) || !QuestHandler.questCompleted(player, 33)) { //Grand Tree or Tree Gnome Village
					player.getDialogue().sendNpcChat("Thank you ever so much again Traveller, and", "welcome back to the Grand Tree!", HAPPY);
					player.getDialogue().endDialogue();
					return true;
				} else if (player.getQuestStage(36) >= QUEST_STARTED && player.getQuestStage(36) <= CRACKED_THE_CODE && !player.getInventory().playerHasItem(ROYAL_SEAL)) {
					switch (d.getChatId()) {
						case 1:
							d.sendPlayerChat("Narnode, I've lost the Royal Seal!", DISTRESSED);
							return true;
						case 2:
							d.sendNpcChat("Don't worry adventurer, it is just paper, I have", "another you can take.", CONTENT);
							return true;
						case 3:
							d.sendNpcChat("Be careful from now on, adventurer.", CONTENT);
							return true;
						case 4:
							d.sendGiveItemNpc("Narnode hands you a copy of the Royal Seal.", new Item(ROYAL_SEAL));
							d.endDialogue();
							player.getInventory().addItemOrDrop(new Item(ROYAL_SEAL));
							return true;
					}
				}
				switch (player.getQuestStage(36)) { //Dialogue per stage
					case QUEST_COMPLETE:
						switch (d.getChatId()) {
							case 1:
								if (!player.getMMVars().trainingComplete()) {
									d.sendNpcChat("Go talk to Daero and do your 10th Squad", "training! It's a good program and will allow", "you to return to Ape Atoll if need be.", CONTENT);
									d.endDialogue();
								} else {
									d.sendNpcChat("Welcome back " + player.getUsername() + "! I am forever", "indebted to your actions which helped save the", "Tree from corruption. Thank you.", HAPPY);
								}
								return true;
							case 2:
								d.sendPlayerChat("Of course Narnode. All parts of this vast", "world are worth saving.", HAPPY);
								d.endDialogue();
								return true;
						}
					case QUEST_STARTED: //Starting the quest
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Hurry adventurer! The 10th Squad could be in danger!", CONTENT);
								return true;
							case 2:
								d.sendPlayerChat("Err, where did you send them again?", CONTENT);
								return true;
							case 3:
								d.sendNpcChat("You don't remember? The shipyard Glough was", "using to build his 'army'! It's on Karamja, the", "east coast like you said yourself. Please hurry!", DISTRESSED);
								d.endDialogue();
								return true;
						}
						return false;
					case 0: //Starting the quest
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello.", CONTENT); //oh SHIT SON it is about to GO DOWN
								return true;
							case 2:
								d.sendNpcChat("Adventurer! It is good to see you again.", HAPPY);
								return true;
							case 3:
								d.sendPlayerChat("And you too, King. How fares the tree?", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("The tree? It is fine, as it has always been since we", "foiled Glough's plans.", CONTENT);
								return true;
							case 5:
								d.sendPlayerChat("Good. What ever did happen to Glough?", CONTENT);
								return true;
							case 6:
								d.sendNpcChat("Oh, I forced him to resign. I have now appointed a new", "head Tree Guardian, Daero. He is learning quickly", "and serves me well.", CONTENT);
								return true;
							case 7:
								d.sendPlayerChat("King you look worried. Is anything the matter?", CONTENT);
								return true;
							case 8:
								if (!QuestHandler.questCompleted(player, 33)) {
									d.sendNpcChat("No, nothing at all! Don't worry about me", "adventurer. Perhaps there are others things", "that need tending to.", CONTENT);
									d.setNextChatId(25);
								} else {
									d.sendNpcChat("Nothing in particular... Well... actually, yes, there is.", DISTRESSED);
								}
								return true;
							case 9:
								d.sendPlayerChat("What is it?", CONTENT);
								return true;
							case 10:
								d.sendNpcChat("Well, do you remember Glough's ship building facilities", "in Karamja?", CONTENT);
								return true;
							case 11:
								d.sendPlayerChat("Yes, they were on the eastern coast. What of them?", CONTENT);
								return true;
							case 12:
								d.sendNpcChat("After you defeated Glough's demon I sent an envoy of", "my Royal Guard, the 10th Squad, to oversee the", "decommission of the shipyard. They were ordered to", "use force if necessary.", CONTENT);
								return true;
							case 13:
								d.sendPlayerChat("I see... Were they successful?", CONTENT);
								return true;
							case 14:
								d.sendNpcChat("I... I don't know. I have heard nothing from them. I", "do not even know if they reached the shipyard!", DISTRESSED);
								return true;
							case 15:
								d.sendPlayerChat("It is a long way...", CONTENT);
								return true;
							case 16:
								d.sendNpcChat("But I need to know what happened. These are elite", "soldiers, their disappearance cannot simply be ignored.", "I cannot wait any longer!", DISTRESSED);
								return true;
							case 17:
								d.sendNpcChat("And so I ask you: would you visit Glough's old shipyard", "in Karamja and find out if the 10th Squad ever", "managed to reach it?", CONTENT);
								return true;
							case 18:
								d.sendOption("Yes.", "No.");
								return true;
							case 19:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("Yes, I will help you King.", CONTENT);
										return true;
									case 2:
										d.sendPlayerChat("I'm sorry, but I can't help.", CONTENT);
										d.endDialogue();
										return true;
								}
								return false;
							case 20:
								d.sendGiveItemNpc("Narnode hands you a copy of the Royal Seal.", new Item(ROYAL_SEAL));
								player.getInventory().addItemOrDrop(new Item(ROYAL_SEAL));
								return true;
							case 21:
								d.sendNpcChat("Thank you very much. You may need this Royal Seal", "to identify yourself as my envoy.", CONTENT);
								return true;
							case 22:
								d.sendNpcChat("Please report to me as soon as you have any", "information.", CONTENT);
								QuestHandler.startQuest(player, getQuestID());
								return true;
							case 23:
								openChapterInterface(player);
								player.getActionSender().sendString("Monkey Madness: Chapter 1", 11112);
								player.getActionSender().sendString("In which our hero finds himself drawn back into Glough's web", 11115);
								player.getActionSender().sendString("of deception and deceit.", 11116);
								d.dontCloseInterface();
								return true;
							case 25:
								d.sendStatement("You must complete Tree Gnome Village to start Monkey Madness.");
								d.endDialogue();
								return true;
						}
						return false;
					case NEW_ORDERS:
						if (!player.getInventory().playerHasItem(NARNODES_ORDERS)) {
							switch (d.getChatId()) {
								case 1:
									d.sendPlayerChat("I, erm... lost your orders...", CONTENT);
									return true;
								case 2:
									d.sendNpcChat("Boy, you adventurers sure like to lose things.", "I'll just write another copy I suppose.", CONTENT);
									return true;
								case 3:
									d.sendStatement("The King begins to write on some parchment...");
									return true;
								case 4:
									d.sendGiveItemNpc("Narnode hands you some handwritten orders.", new Item(NARNODES_ORDERS));
									d.endDialogue();
									player.getInventory().addItemOrDrop(new Item(NARNODES_ORDERS));
									return true;
							}
							return false;
						} else {
							switch (d.getChatId()) {
								case 1:
									d.sendPlayerChat("Where will I find Daero?", CONTENT);
									return true;
								case 2:
									d.sendNpcChat("You will find him attending to business somewhere on", "The Grand Tree.", CONTENT);
									d.endDialogue();
									return true;
							}
						}
						return false;
					case SPOKE_TO_CARROTCOCK:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Welcome back adventurer.", CONTENT);
								return true;
							case 2:
								d.sendPlayerChat("Hello, I investigated the shipyard.", CONTENT);
								return true;
							case 3:
								d.sendNpcChat("Thank you for doing this. What did you find?", CONTENT);
								return true;
							case 4:
								d.sendPlayerChat("I met a gnome who goes by the name of Caranock.", "Do you recognize it?", CONTENT);
								return true;
							case 5:
								d.sendNpcChat("The name sounds a little familiar, but it is nobody I", "know personally.", CONTENT);
								return true;
							case 6:
								d.sendPlayerChat("He calls himself a Gnome Liaison Officer. He seemed a", "little... odd.", CONTENT);
								return true;
							case 7:
								d.sendNpcChat("Nevermind that, did you find anything out about my", "10th Squad?", DISTRESSED);
								return true;
							case 8:
								d.sendPlayerChat("Caranock suggested they were blown off course by", "extreme southerly winds.", CONTENT);
								return true;
							case 9:
								d.sendNpcChat("Do you believe him?", CONTENT);
								return true;
							case 10:
								d.sendPlayerChat("I don't have any other information right now.", CONTENT);
								return true;
							case 11:
								d.sendNpcChat("Very well. I will now prepare some orders. You must", "convey them to my new High Tree Guardian, Daero.", CONTENT);
								return true;
							case 12:
								d.sendStatement("The King begins to write on some parchment...");
								return true;
							case 13:
								d.sendGiveItemNpc("Narnode hands you some handwritten orders.", new Item(NARNODES_ORDERS));
								player.getInventory().addItemOrDrop(new Item(NARNODES_ORDERS));
								return true;
							case 14:
								d.sendPlayerChat("Where will I find Daero?", CONTENT);
								return true;
							case 15:
								d.sendNpcChat("You will find him attending to business somewhere on", "The Grand Tree.", CONTENT);
								player.setQuestStage(getQuestID(), NEW_ORDERS);
								d.endDialogue();
								return true;
						}
						return false;
					case FUCK_THE_DEMON:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("King Narnode!", HAPPY);
								return true;
							case 2:
								d.sendNpcChat("Yes? How is the mission going... it has been quite some", "time since I sent you on your way.", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("It's over, it's finally over.", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("What do you mean 'over'?", CONTENT);
								return true;
							case 5:
								d.sendPlayerChat("I mean 'finished.'", CONTENT);
								return true;
							case 6:
								d.sendNpcChat("Yes, alright. Report on what happened.", CONTENT);
								return true;
							case 7:
								d.sendPlayerChat("With all due respect sir, if I told you, you would not", "believe me. I expect Sergeant Garkor will be sending", "you a full report soon enough.", CONTENT);
								return true;
							case 8:
								d.sendNpcChat("And what of my 10th Squad?", DISTRESSED);
								return true;
							case 9:
								d.sendPlayerChat("They all live, we suffered no casualties.", CONTENT);
								return true;
							case 10:
								d.sendNpcChat("'We', " + player.getUsername() + "?", CONTENT);
								return true;
							case 11:
								d.sendPlayerChat("I, uh, I'm part of the 10th Squad now. I even have", "the sigil.", CONTENT);
								return true;
							case 12:
								d.sendGiveItemNpc("You show King Narnode your sigil.", new Item(TENTH_SQUAD_SIGIL));
								return true;
							case 13:
								d.sendNpcChat("Well, now. It appears I cannot argue with that. Garkor", "obviously thinks highly of you, as do I.", CONTENT);
								return true;
							case 14:
								d.sendNpcChat("No service such as what you have done for me goes", "unrewarded in my kingdom. I personally made a visit", "to the Royal Treasury to withdraw your reward.", CONTENT);
								return true;
							case 15:
								if (player.getInventory().getItemContainer().freeSlots() < 4) {
									d.sendNpcChat("You don't have enough room in your inventory.", SAD);
									d.endDialogue();
									return true;
								} else {
									d.sendGiveItemNpc("King Narnode shows you a huge stack of gold!", "And several dragonstones!", new Item(995, 100000), new Item(1615));
									return true;
								}
							case 16:
								d.sendNpcChat("So you're officially a member of the 10th Squad then?", CONTENT);
								return true;
							case 17:
								d.sendPlayerChat("I suppose so...", CONTENT);
								return true;
							case 18:
								d.sendNpcChat("Well then you had better sign up for training.", CONTENT);
								return true;
							case 19:
								d.sendPlayerChat("Training?", CONTENT);
								return true;
							case 20:
								d.sendNpcChat("Yes. All members of the Royal Guard must complete a", "mandatory training program.", CONTENT);
								return true;
							case 21:
								d.sendPlayerChat("Where do I sign up for this?", CONTENT);
								return true;
							case 22:
								d.sendNpcChat("The High Tree Guardian Daero is in charge of the", "training program. You should know where to find", "him by now.", CONTENT);
								return true;
							case 23:
								QuestHandler.completeQuest(player, 36);
								d.dontCloseInterface();
								return true;
						}
						return false;
				}
				return false;
			case GLO_CARANOCK:
				switch (player.getQuestStage(36)) {
					case SPOKE_TO_CARROTCOCK:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("I shall see personally to the decommission. You should", "report to the King immediately.", CONTENT);
								d.endDialogue();
								return true;
						}
						return false;
					case QUEST_STARTED:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello!", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Who are you? Did Glough send you?", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("Glough? No. He has been forced", "to resign by the King.", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("Forced to resign??", DISTRESSED);
								return true;
							case 5:
								d.sendPlayerChat("He was plotting to start a war between the", "gnomes and humankind.", CONTENT);
								return true;
							case 6:
								d.sendPlayerChat("Anyway, I am here on a separate mission. I am", "investigating the mysterious disappearance of the 10th", "Squad of King Narnode's Royal Guard. They were to", "carry out some work in the area.", CONTENT);
								return true;
							case 7:
								d.sendNpcChat("Royal Guard? I know nothing about them. Absolutely", "nothing.", CONTENT);
								return true;
							case 8:
								d.sendPlayerChat("You have no idea why they mysteriously disappeared?", CONTENT);
								return true;
							case 9:
								d.sendNpcChat("None whatsoever. What were they here to do?", CONTENT);
								return true;
							case 10:
								d.sendPlayerChat("They were to oversee the decommission of the shipyard.", CONTENT);
								return true;
							case 11:
								d.sendNpcChat("Decommission of the shipyard... I see. Well, we have had", "some seriously strong southerly winds as of late. They", "may have been blown off course during flight.", CONTENT);
								return true;
							case 12:
								d.sendNpcChat("I shall see personally to the decommission. You should", "report to the King immediately.", CONTENT);
								player.setQuestStage(36, SPOKE_TO_CARROTCOCK);
								d.endDialogue();
								return true;
						}
						return false;
				}
				return false;
			case DAERO:
				if (player.getPosition().getZ() > 0 && player.getQuestStage(36) >= ORDERS_FROM_DAERO && player.getQuestStage(36) < QUEST_COMPLETE) {
					if (player.getQuestStage(36) == ORDERS_FROM_DAERO) {
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("I have a few questions...", CONTENT);
								d.setNextChatId(18);
								return true;
							case 18:
								d.sendOption("Talk about the journey...", "Talk about the 10th Squad...", "Talk about Caranock...", "Let's go then.");
								return true;
							case 19:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("I've got a few questions about the journey.", CONTENT);
										d.setNextChatId(20);
										return true;
									case 2:
										d.sendPlayerChat("I've got a few questions about the 10th Squad.", CONTENT);
										d.setNextChatId(22);
										return true;
									case 3:
										d.sendPlayerChat("I've got a few questions about Caranock.", CONTENT);
										d.setNextChatId(24);
										return true;
									case 4:
										d.sendPlayerChat("Let's go then.", CONTENT);
										d.setNextChatId(44);
										return true;
								}
								return false;
							case 20:
								d.sendOption("What lies to the south of Karamja?", "How will I travel?", "Are you coming with me?", "Return to previous options.");
								return true;
							case 21:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("What lies to the south of Karamja?", CONTENT);
										d.setNextChatId(26);
										return true;
									case 2:
										d.sendPlayerChat("How will I travel?", CONTENT);
										d.setNextChatId(29);
										return true;
									case 3:
										d.sendPlayerChat("Are you coming with me?", CONTENT);
										d.setNextChatId(30);
										return true;
									case 4:
										d.sendPlayerChat("Actually...", CONTENT);
										d.setNextChatId(18);
										return true;
								}
								return false;
							case 22:
								d.sendOption("Why did the King send a squad of the Royal Guard?", "Who is Garkor?", "Why are the 10th Squad so famous?", "Return to previous options.");
								return true;
							case 23:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("Why did the King send a squad of the Royal Guard?", CONTENT);
										d.setNextChatId(31);
										return true;
									case 2:
										d.sendPlayerChat("Who is Garkor?", CONTENT);
										d.setNextChatId(35);
										return true;
									case 3:
										d.sendPlayerChat("Why are the 10th Squad so famous?", CONTENT);
										d.setNextChatId(36);
										return true;
									case 4:
										d.sendPlayerChat("Actually...", CONTENT);
										d.setNextChatId(18);
										return true;
								}
								return false;
							case 24:
								d.sendOption("Who is Caranock?", "What is a Gnome Liaison Officer?", "I'm not so sure about Caranock...", "Return to previous options.");
								return true;
							case 25:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("Who is Caranock?", CONTENT);
										d.setNextChatId(39);
										return true;
									case 2:
										d.sendPlayerChat("What is a Gnome Liaison Officer?", CONTENT);
										d.setNextChatId(40);
										return true;
									case 3:
										d.sendPlayerChat("I'm not so sure about Caranock...", CONTENT);
										d.setNextChatId(41);
										return true;
									case 4:
										d.sendPlayerChat("Actually...", CONTENT);
										d.setNextChatId(18);
										return true;
								}
								return false;
							case 26:
								d.sendNpcChat("We do not know. Initial reports spoke of a large atoll", "populated by monkeys.", CONTENT);
								return true;
							case 27:
								d.sendPlayerChat("Monkeys? Like on Karamja?", CONTENT);
								return true;
							case 28:
								d.sendNpcChat("From what I have heard, not quite like those monkeys...", DISTRESSED);
								d.setNextChatId(20);
								return true;
							case 29:
								d.sendNpcChat("It is my responsibility to make arrangement for your", "mission. We will shortly visit a colleage of mine who", "will be accompanying you.", CONTENT);
								d.setNextChatId(20);
								return true;
							case 30:
								d.sendNpcChat("I am afraid not. I must remain here to safeguard the", "Grand Tree. I will assign a gnome agent to travel with", "you.", CONTENT);
								d.setNextChatId(20);
								return true;
							case 31:
								d.sendNpcChat("The Royal Guard is composed of particularly elite", "soldiers who have proven themselves in battle. They are", "duty bound to protect the Grand Tree, its King and", "his interests.", CONTENT);
								return true;
							case 32:
								d.sendNpcChat("In the face of danger, they can more than take care of", "themselves.", CONTENT);
								return true;
							case 33:
								d.sendPlayerChat("So the King worries we have come across an equally", "formidable foe?", CONTENT);
								return true;
							case 34:
								d.sendNpcChat("He worries about this, yes.", DISTRESSED);
								d.setNextChatId(22);
								return true;
							case 35:
								d.sendNpcChat("Sergeant Garkor holds the command of the 10th Squad.", "As a soldier, he is extremely able. If his men are in trouble,", "he will be tirelessly working to save them. You should aim", "to make contact with him.", CONTENT);
								d.setNextChatId(22);
								return true;
							case 36:
								d.sendNpcChat("They are, as you humans might say, the best of the", "best. As well as Sergeant Garkor, they have in their", "company a High Mage, two Sappers and battle hardened", "foot soldiers.", CONTENT);
								return true;
							case 37:
								d.sendPlayerChat("What is a Sapper?", CONTENT);
								return true;
							case 38:
								d.sendNpcChat("You might consider the role as that of an engineer. Or", "perhaps of a munitions expert.", CONTENT);
								d.setNextChatId(22);
								return true;
							case 39:
								d.sendNpcChat("I have never heard of him. According to the report you", "made to the King, he is the Gnome Liaison officer at", "the eastern Karamja shipyard.", CONTENT);
								d.setNextChatId(24);
								return true;
							case 40:
								d.sendNpcChat("It was a position Glough introduced. Gnome Liaison", "Officers are in general responsible for coordinating", "activities between Gnomes and other beings in our", "remote operations.", CONTENT);
								d.setNextChatId(24);
								return true;
							case 41:
								d.sendNpcChat("In what way?", CONTENT);
								return true;
							case 42:
								d.sendPlayerChat("I do not know. He just seemed a little suspicious. He", "was very keen to see me leave.", DISTRESSED);
								return true;
							case 43:
								d.sendNpcChat("I do not know him; he is from before my time. Glough", "would presumably have handpicked him.", CONTENT);
								d.setNextChatId(24);
								return true;
							case 44:
								d.sendNpcChat("I must first introduce you to a colleague of mine", "who will be accompanying you on your mission.", CONTENT);
								return true;
							case 45:
								d.sendPlayerChat("Who is it?", CONTENT);
								return true;
							case 46:
								d.sendNpcChat("His name is Flight Commander Waydar.", CONTENT);
								return true;
							case 47:
								d.sendNpcChat("We must go now and meet Waydar. For security", "reasons I must ask you to wear a blindfold.", CONTENT);
								return true;
							case 48:
								d.sendStatement("You wear the blindfold Daero hands you.");
								return true;
							case 49:
								player.fadeTeleport(HANGAR);
								player.setStopPacket(true);
								CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
									@Override
									public void execute(CycleEventContainer b) {
										b.stop();
									}

									@Override
									public void stop() {
										Dialogues.sendDialogue(player, DAERO, 50, 0, 0);
										player.setStopPacket(false);
									}
								}, 5);
								return true;
						}
						return false;
					} else {
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Would you like to go back to the hangar?", CONTENT);
								return true;
							case 2:
								d.sendOption("Yes.", "No.");
								return true;
							case 3:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("Yes I would.", CONTENT);
										return true;
									case 2:
										d.sendPlayerChat("Not right now, no.", CONTENT);
										d.endDialogue();
										return true;
								}
								return false;
							case 4:
								d.sendNpcChat("Alright, you know the drill, blindfold time.", CONTENT);
								return true;
							case 5:
								d.sendStatement("You put on the blindfold as Daero takes", "you back to the secret hangar.");
								player.fadeTeleport(player.getQuestStage(36) >= CRACKED_THE_CODE ? HANGAR_INITIALIZED : HANGAR);
								d.endDialogue();
								return true;
						}
						return false;
					}
				} else {
					switch (player.getQuestStage(36)) {
						case NEW_ORDERS:
							switch (d.getChatId()) {
								case 1:
									d.sendPlayerChat("Are you Daero?", CONTENT);
									return true;
								case 2:
									d.sendNpcChat("Indeed I am, and who are you?", CONTENT);
									return true;
								case 3:
									d.sendPlayerChat("I am an adventurer. I am currently in the service of", "your King.", CONTENT);
									return true;
								case 4:
									d.sendNpcChat("I see. You must be the individual who helped defeat my", "predecessor Glough. I hope you'll find me a more", "honest replacement.", CONTENT);
									return true;
								case 5:
									d.sendPlayerChat("I have been asked to give you orders from King", "Narnode.", CONTENT);
									return true;
								case 6:
									d.sendNpcChat("Well hand them over here then.", CONTENT);
									return true;
								case 7:
									if (!player.getInventory().playerHasItem(NARNODES_ORDERS)) {
										d.sendPlayerChat("Shit, I forgot them.", CONTENT);
										d.endDialogue();
										return true;
									} else {
										d.sendGiveItemNpc("You hand King Narnode's orders to Daero.", new Item(NARNODES_ORDERS));
										player.getInventory().removeItem(new Item(NARNODES_ORDERS));
										return true;
									}
								case 8:
									d.sendNpcChat("It is written in an old military code. Bear with me", "whilst I decode this.", CONTENT);
									return true;
								case 9:
									d.sendStatement("It takes Daero a moment to read the orders.");
									return true;
								case 10:
									d.sendNpcChat("I hope you feel like a quest adventurer.", CONTENT);
									return true;
								case 11:
									d.sendPlayerChat("Why is that?", CONTENT);
									return true;
								case 12:
									d.sendNpcChat("...Because you're going to get one.", CONTENT);
									return true;
								case 13:
									d.sendPlayerChat("Tell me what the orders say!", CONTENT);
									return true;
								case 14:
									d.sendNpcChat("Given your recent performance of uncovering and", "neutralizing a threat at the very extremes of the", "Gnome hierarchy, the King has decreed that you are to", "undertake a reconnaissance mission.", CONTENT);
									return true;
								case 15:
									d.sendPlayerChat("Where to?", CONTENT);
									return true;
								case 16:
									d.sendNpcChat("You are to be taken far to the south of Karamja,", "further than any gnome has purposely travelled before.", "You are to investigate Caranock's claim that Garkor's", "squad were blown off course.", CONTENT);
									return true;
								case 17:
									d.sendNpcChat("You must really have impressed the King in the past.", CONTENT);
									player.setQuestStage(36, ORDERS_FROM_DAERO);
									return true;
							}
							return false;
						case ORDERS_FROM_DAERO:
							switch (d.getChatId()) {
								case 1:
									d.sendOption("Where is this hangar?", "How do I leave and return?", "What is reinitialization?", "Nevermind.");
									return true;
								case 2:
									switch (optionId) {
										case 1:
											d.sendPlayerChat("Where is this hangar?", CONTENT);
											d.setNextChatId(3);
											return true;
										case 2:
											d.sendPlayerChat("How do I leave and return?", CONTENT);
											d.setNextChatId(4);
											return true;
										case 3:
											d.sendPlayerChat("What is reinitialization?", CONTENT);
											d.setNextChatId(6);
											return true;
										case 4:
											d.sendPlayerChat("Nevermind.", CONTENT);
											d.endDialogue();
											return true;
									}
									return false;
								case 3:
									d.sendNpcChat("We try to ensure the location of the hangar remains a", "secret. If you must know something, then know that it", "lies directly beneath the Gnomeball pitch. However, I", "cannot reveal the location of its entrance.", CONTENT);
									d.setNextChatId(2);
									return true;
								case 4:
									d.sendNpcChat("There is a teleporter around the corner. You can use", "this to leave.", CONTENT);
									return true;
								case 5:
									d.sendNpcChat("Come and find me on the Grand Tree to return. I will", "probably be at the bar.", CONTENT);
									d.setNextChatId(2);
									return true;
								case 6:
									d.sendNpcChat("Glough left a code on the facility whch periodically", "needs to be entered if we wish to maintain the supply of", "power.", CONTENT);
									return true;
								case 7:
									d.sendNpcChat("Without going through this process of reinitialization, we", "cannot power the mechanism which deploys the military", "gliders.", CONTENT);
									d.setNextChatId(2);
									return true;
								case 50:
									d.setLastNpcTalk(DAERO);
									d.sendNpcChat("Welcome, adventurer, to the Underground Military", "Glider Hangar.", CONTENT);
									return true;
								case 51:
									d.sendPlayerChat("Wow! Why would Gnomes need such a place?", CONTENT);
									return true;
								case 52:
									d.sendNpcChat("We do not, if the truth be told. This hangar was part", "of Glough's contingency planning. Had the attacks by", "land and sea failed, he would have turned to air.", CONTENT);
									return true;
								case 53:
									d.sendNpcChat("It is fortunate indeed that you managed to expose him.", "The military gliders in this hangar are a prototype for a", "much more refined version of the standard variety we", "currently use.", CONTENT);
									return true;
								case 54:
									d.sendNpcChat("Let me introduce you to Flight Commander Waydar.", CONTENT);
									return true;
								case 55:
									d.sendNpcChat("Flight Commmander Waydar, I would like", "you to meet " + player.getUsername() + ".", CONTENT);
									return true;
								case 56:
									d.setLastNpcTalk(WAYDAR);
									d.sendNpcChat("Greetings High Tree Guardian.", CONTENT);
									return true;
								case 57:
									d.setLastNpcTalk(WAYDAR);
									d.sendNpcChat("And greetings to you too, visitor.", CONTENT);
									return true;
								case 58:
									d.setLastNpcTalk(DAERO);
									d.sendNpcChat("Not just any old visitor Waydar; this is the person who", "exposed Glough and defeated his demon.", CONTENT);
									return true;
								case 59:
									d.setLastNpcTalk(WAYDAR);
									d.sendNpcChat("I see. Well, there are no more demons left here.", CONTENT);
									return true;
								case 60:
									d.setLastNpcTalk(DAERO);
									d.sendNpcChat("Quite. He is now on a secret mission for the King.", CONTENT);
									return true;
								case 61:
									d.sendNpcChat("As you know, the 10th Squad went missing during their", "mission to decommission the eastern shipyard of", "Karamja.", CONTENT);
									return true;
								case 62:
									d.sendNpcChat("We still do not know what happened, but evidence", "suggests they were blown far off course to the south.", CONTENT);
									return true;
								case 63:
									d.setLastNpcTalk(WAYDAR);
									d.sendNpcChat("Their standard gliders must have fallen prey to the", "tropical weather.", CONTENT);
									return true;
								case 64:
									d.setLastNpcTalk(DAERO);
									d.sendNpcChat("When reinitialization has been completed, you are to fly", "to the south of Karamja with " + player.getUsername() + " and", "accompany him on the mission.", CONTENT);
									return true;
								case 65:
									d.setLastNpcTalk(WAYDAR);
									d.sendNpcChat("We are no closer to reinitializing sir, the code is too", "hard. It is likely the only person who could do it is", "Glough.", CONTENT);
									return true;
								case 66:
									d.setLastNpcTalk(DAERO);
									d.sendNpcChat("That gnome is never stepping foot in this hangar again.", "He always was and still is a menace. Do you", "understand me?", CONTENT);
									return true;
								case 67:
									d.setLastNpcTalk(WAYDAR);
									d.sendNpcChat("Yes sir.", CONTENT);
									return true;
								case 68:
									d.setLastNpcTalk(DAERO);
									d.sendNpcChat("Very well. Notify me when you have managed to reinitialize.", CONTENT);
									return true;
								case 69:
									d.sendNpcChat(player.getUsername() + " you will have to wait until reinitialization is", "complete.", CONTENT);
									d.endDialogue();
									return true;
							}
							return false;
						case CRACKED_THE_CODE:
							switch (d.getChatId()) {
								case 1:
									d.sendNpcChat("Well done, adventurer. You have managed to break", "Glough's code. Now the process of reinitialization is", "complete, you can truly begin your journey into the", "unknown.", CONTENT);
									return true;
								case 2:
									d.sendPlayerChat("I've had some practice in the past.", CONTENT);
									return true;
								case 3:
									d.sendNpcChat("You are clearly a man of many talents.", CONTENT);
									return true;
								case 4:
									d.sendNpcChat("Flight Commander Waydar, now that reinitialization is", "complete, I order you to fly to the south of Karamja", "with " + player.getUsername() + ".", CONTENT);
									return true;
								case 5:
									d.setLastNpcTalk(WAYDAR);
									d.sendNpcChat("Yes sir.", CONTENT);
									return true;
								case 6:
									d.setLastNpcTalk(DAERO);
									d.sendNpcChat("You are to safeguard him on this potentially dangerous", "mission.", CONTENT);
									return true;
								case 7:
									d.setLastNpcTalk(WAYDAR);
									d.sendNpcChat("Understood.", CONTENT);
									return true;
								case 8:
									d.setLastNpcTalk(DAERO);
									d.sendNpcChat(player.getUsername() + ", speak to Waydar when you are ready to leave.", CONTENT);
									return true;
								case 9:
									d.sendPlayerChat("Ah, thank you.", CONTENT);
									d.endDialogue();
									return true;
							}
							return false;
						case FUCK_THE_DEMON:
							switch (d.getChatId()) {
								case 1:
									d.sendNpcChat("You look like you're in a hurry.", "Use the teleportation device to leave the hangar.", CONTENT);
									d.endDialogue();
									return true;
							}
							return false;
						case QUEST_COMPLETE:
							if (!player.getMMVars().trainingComplete()) {
								switch (d.getChatId()) {
									case 1:
										d.sendPlayerChat("Good day, High Tree Guardian.", CONTENT);
										return true;
									case 2:
										d.sendNpcChat("Hello there. I hear your mission is complete.", CONTENT);
										return true;
									case 3:
										d.sendPlayerChat("News travels quickly on this tree. I expect you also", "know that I am to be enrolled in the Royal Guard", "Training Program?", CONTENT);
										return true;
									case 4:
										d.sendNpcChat("Indeed I do. It's mandatory training now to", "visit the Ape Atoll by any means.", CONTENT);
										return true;
									case 5:
										d.sendPlayerChat("How long does it take?", CONTENT);
										return true;
									case 6:
										d.sendNpcChat("For you, it should hardly take any time at all.", CONTENT);
										return true;
									case 7:
										d.sendOption("Then let us begin.", "Can't you just lie and say I did the program?");
										d.setNextChatId(20);
										return true;
									case 8:
										d.sendNpcChat("Enthusiasm. I like that. We will first begin with a series", "of exercises designed to increase your strength and", "stamina.", CONTENT);
										return true;
									case 9:
										d.sendNpcChat("We then will follow these up by improving your attack", "and defense techniques.", CONTENT);
										return true;
									case 10:
										d.sendNpcChat("Let us begin. You must choose what you want to focus on.", CONTENT);
										return true;
									case 11:
										d.sendOption("Focus on increasing strength and stamina...", "Focus on improving attack and defence techniques...");
										return true;
									case 12:
										switch (optionId) {
											case 1:
												d.sendStatement("Several hours and a training montage later...");
												player.getActionSender().sendWalkableInterface(8677);
												player.getActionSender().sendMapState(2);
												return true;
											case 2:
												d.sendStatement("Several hours and a training montage later...");
												player.getActionSender().sendWalkableInterface(8677);
												player.getActionSender().sendMapState(2);
												d.setNextChatId(15);
												return true;
										}
										return true;
									case 13:
										d.endDialogue();
										player.getActionSender().removeInterfaces();
										player.getActionSender().sendWalkableInterface(-1);
										player.getActionSender().sendMapState(0);
										player.getMMVars().setTrainingComplete(true);
										player.getSkill().addExp(Skill.STRENGTH, 35000);
										player.getActionSender().sendMessage("You are awarded " + (35000 * 2.25) + " experience in Strength!");
										player.getSkill().addExp(Skill.HITPOINTS, 35000);
										player.getActionSender().sendMessage("You are awarded " + (35000 * 2.25) + " experience in Hitpoints!");
										player.getSkill().addExp(Skill.ATTACK, 20000);
										player.getActionSender().sendMessage("You are awarded " + (20000 * 2.25) + " experience in Attack!");
										player.getSkill().addExp(Skill.DEFENCE, 20000);
										player.getActionSender().sendMessage("You are awarded " + (20000 * 2.25) + " experience in Defence!");
										player.getActionSender().sendMessage("You may now return to Ape Atoll by means of Glider or Magic.");
										return true;
									case 15:
										d.endDialogue();
										player.getActionSender().removeInterfaces();
										player.getMMVars().setTrainingComplete(true);
										player.getSkill().addExp(Skill.ATTACK, 35000);
										player.getActionSender().sendMessage("You are awarded " + (35000 * 2.25) + " experience in Attack!");
										player.getSkill().addExp(Skill.DEFENCE, 35000);
										player.getActionSender().sendMessage("You are awarded " + (35000 * 2.25) + " experience in Defence!");
										player.getSkill().addExp(Skill.STRENGTH, 20000);
										player.getActionSender().sendMessage("You are awarded " + (20000 * 2.25) + " experience in Strength!");
										player.getSkill().addExp(Skill.HITPOINTS, 20000);
										player.getActionSender().sendMessage("You are awarded " + (20000 * 2.25) + " experience in Hitpoints!");
										player.getActionSender().sendMessage("You may now return to Ape Atoll by means of Glider or Magic.");
										return true;
									case 20:
										switch (optionId) {
											case 1:
												d.sendPlayerChat("Then let us begin.", CONTENT);
												d.setNextChatId(8);
												return true;
											case 2:
												d.sendPlayerChat("Can't you just lie and say I did the", "program?", CONTENT);
												return true;
										}
									case 21:
										d.sendNpcChat("Why would you want that? Don't you", "adventurers crave experience?", CONTENT);
										return true;
									case 22:
										d.sendPlayerChat("Yes, but only in specific skills!", "Why does no one understand that!?", ANGRY_1);
										return true;
									case 23:
										d.sendNpcChat("Because it sounds a bit... autistic.", CONTENT);
										return true;
									case 24:
										d.sendPlayerChat("Just tell Narnode I did the training!", ANGRY_2);
										return true;
									case 25:
										d.sendNpcChat("You won't be able to do it again if you", "make me do this, is that what you want?", CONTENT);
										return true;
									case 26:
										d.sendOption("Yes!", "Actually... Nevermind.");
										return true;
									case 27:
										switch (optionId) {
											case 1:
												d.sendPlayerChat("Yes!", ANGRY_1);
												return true;
											case 2:
												d.sendPlayerChat("Actually... Nevermind.", CONTENT);
												d.endDialogue();
												return true;
										}
									case 28:
										d.sendNpcChat("As you wish, I will tell Narnode you", "completed the program.", CONTENT);
										d.endDialogue();
										player.getMMVars().setTrainingComplete(true);
										player.getActionSender().sendMessage("You may now return to Ape Atoll by means of Glider or Magic.");
										return true;
								}
								return false;
							} else {
								if (player.getPosition().getZ() > 0) {
									switch (d.getChatId()) {
										case 1:
											d.sendNpcChat("Would you like to go back to the hangar?", CONTENT);
											return true;
										case 2:
											d.sendOption("Yes.", "No.");
											return true;
										case 3:
											switch (optionId) {
												case 1:
													d.sendPlayerChat("Yes I would.", CONTENT);
													return true;
												case 2:
													d.sendPlayerChat("Not right now, no.", CONTENT);
													d.endDialogue();
													return true;
											}
											return false;
										case 4:
											d.sendNpcChat("Alright, you know the drill, blindfold time.", CONTENT);
											return true;
										case 5:
											d.sendStatement("You put on the blindfold as Daero takes", "you back to the secret hangar.");
											player.fadeTeleport(player.getQuestStage(36) >= CRACKED_THE_CODE ? HANGAR_INITIALIZED : HANGAR);
											d.endDialogue();
											return true;
									}
									return false;
								} else {
									switch (d.getChatId()) {
										case 1:
											d.sendNpcChat("Hello again adventurer. Great work with", "the demon. Don't forget Waydar can take you to", "Crash Island and you can use the teleportation", "device to leave the hangar.", CONTENT);
											d.endDialogue();
											return true;
									}
									return false;
								}
							}
					}
					return false;
				}
			case GLOUGH:
				switch (player.getQuestStage(36)) {
					case ORDERS_FROM_DAERO:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Hrmph. What do you want?", ANGRY_1);
								return true;
							case 2:
								d.sendPlayerChat("King Narnode has set me on a mission. It involves", "using your old military glider hangar. I demand", "to know the reinitialization code.", CONTENT);
								return true;
							case 3:
								d.sendNpcChat("HA! Why would I EVER do that for you?", LAUGHING);
								return true;
							case 4:
								d.sendOption("I'll make it worth your while.", "You're right, nevermind.");
								return true;
							case 5:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("I'll make it worth your while.", CONTENT);
										return true;
									case 2:
										d.sendPlayerChat("Eh, you're right. Nevermind.", CONTENT);
										d.endDialogue();
										return true;
								}
							case 6:
								d.sendNpcChat("Make it worth my while eh?", "My 'while' is going to cost 200,000 gold.", ANGRY_1);
								return true;
							case 7:
								d.sendOption("Fine. (200,000 gold)", "That's far too much for me.");
								return true;
							case 8:
								switch (optionId) {
									case 1:
										if (player.getInventory().playerHasItem(995, 200000)) {
											d.sendPlayerChat("Fine. Here's the gold.", ANGRY_2);
											return true;
										} else {
											d.sendPlayerChat("I don't have that much on me...", ANGRY_2);
											d.endDialogue();
											return true;
										}
									case 2:
										d.sendPlayerChat("That's far too much for me.", SAD);
										d.endDialogue();
										return true;
								}
							case 9:
								d.sendGiveItemNpc("You hand Glough 200,000 gold.", new Item(995, 200000));
								return true;
							case 10:
								d.sendNpcChat("Thank you... adventurer.", "Now for the code...", CONTENT);
								return true;
							case 11:
								d.sendStatement("Glough begins to whisper into your ear...");
								return true;
							case 12:
								d.endDialogue();
								player.getActionSender().removeInterfaces();
								player.getInventory().removeItem(new Item(995, 200000));
								reinitializeHangar(player);
								return true;
						}
						return false;
				}
				return false;
			case WAYDAR:
				if (player.getQuestStage(36) > ORDERS_FROM_DAERO) {
					switch (d.getChatId()) {
						case 1:
							d.sendNpcChat("Are you stocked up on food? I can", "only carry enough provisions for myself.", CONTENT);
							return true;
						case 2:
							d.sendNpcChat("I'd be careful of the local fauna, I've heard their", "bite is far worse than any noise they make.", CONTENT);
							return true;
						case 3:
							d.sendNpcChat("Do you wish to fly now?", CONTENT);
							return true;
						case 4:
							d.sendOption("Yes.", "No.");
							return true;
						case 5:
							switch (optionId) {
								case 1:
									d.sendPlayerChat("Yes, let's go.", CONTENT);
									return true;
								case 2:
									d.sendPlayerChat("No, I'm not ready yet.", CONTENT);
									d.endDialogue();
									return true;
							}
						case 6:
							d.sendNpcChat("As you wish.", CONTENT);
							return true;
						case 7:
							player.fadeTeleport(CRASH_ISLAND);
							d.endDialogue();
							return true;
					}
					return false;
				}
				switch (player.getQuestStage(36)) {
					case ORDERS_FROM_DAERO:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Hello adventurer.", CONTENT);
								return true;
							case 2:
								d.sendOption("What are military gliders?", "Why are the gliders folded against the wall?", "When will reinitialization be completed?", "Where is the reinitialization code?");
								return true;
							case 3:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("What are military gliders?", CONTENT);
										d.setNextChatId(4);
										return true;
									case 2:
										d.sendPlayerChat("Why are the gliders folded against the wall?", CONTENT);
										d.setNextChatId(5);
										return true;
									case 3:
										d.sendPlayerChat("When will reinitialization be completed?", CONTENT);
										d.setNextChatId(6);
										return true;
									case 4:
										d.sendPlayerChat("Where is the reinitialization code?", CONTENT);
										d.setNextChatId(8);
										return true;
								}
								return false;
							case 4:
								d.sendNpcChat("As Daero said, they are a prototype for a new version", "of glider. They are reinforced yet are built out of", "lighter materials. They can fly much faster and for", "longer.", CONTENT);
								d.setNextChatId(3);
								return true;
							case 5:
								d.sendNpcChat("It is the most compact way to store them.", "Unfortunately, they will remain like that until they are", "powered. We can only fully power the hangar when", "reinitialization has been completed.", CONTENT);
								d.setNextChatId(4);
								return true;
							case 6:
								d.sendNpcChat("I cannot be sure. It is a difficult code; it may happen", "the next instant, then again it may not.", CONTENT);
								return true;
							case 7:
								d.sendNpcChat("You could even have a go at solving the code yourself.", CONTENT);
								d.setNextChatId(3);
								return true;
							case 8:
								d.sendNpcChat("The code must be entered at the red control panel just", "by the wall over there.", CONTENT);
								return true;
							case 9:
								d.sendPlayerChat("Thanks, I'll give it a shot.", CONTENT);
								return true;
							case 10:
								d.sendNpcChat("Good luck.", CONTENT);
								d.endDialogue();
								return true;
						}
						return false;
				}
				return false;
			case LUMBDO:
				if (player.getQuestStage(36) > LUMBDO_A_DICK) {
					switch (d.getChatId()) {
						case 1:
							if (player.getPosition().getX() < 2852) {
								d.sendNpcChat("I suppose you want to go back to Crash Island?", CONTENT);
							} else {
								d.sendNpcChat("I suppose you want to go back to the atoll?", CONTENT);
							}
							return true;
						case 2:
							d.sendOption("Yes.", "No.");
							return true;
						case 3:
							switch (optionId) {
								case 1:
									d.sendPlayerChat("Yes, I do.", CONTENT);
									return true;
								case 2:
									d.sendPlayerChat("No, not right now.", CONTENT);
									d.endDialogue();
									return true;
							}
							return false;
						case 4:
							d.sendNpcChat("Alright.", CONTENT);
							return true;
						case 5:
							player.fadeTeleport(player.getPosition().getX() < 2852 ? CRASH_ISLAND : APE_ATOLL_LANDING);
							d.endDialogue();
							return true;
					}
					return false;
				} else {
					switch (player.getQuestStage(36)) {
						case FUCK_THE_DEMON:
							switch (d.getChatId()) {
								case 1:
									d.sendNpcChat("Outstanding job adventurer.", HAPPY);
									d.endDialogue();
									return true;
							}
							return false;
						case CRACKED_THE_CODE:
							switch (d.getChatId()) {
								case 1:
									d.sendNpcChat("Who are you two?", CONTENT);
									return true;
								case 2:
									d.sendPlayerChat("We are on a mission for King Narnode Shareen. I am", player.getUsername() + " and this is Flight Commander Waydar.", CONTENT);
									return true;
								case 3:
									d.sendNpcChat("What business do you have here?", CONTENT);
									return true;
								case 4:
									d.sendPlayerChat("We are to investigate the disappearance of the 10th", "Squad of the Royal Guard. Am I right in suspecting", "that you, Lumbdo, are a member of the 10th Squad?", CONTENT);
									return true;
								case 5:
									d.sendNpcChat("I might be. Do you have any way to prove that you", "are who you say you are?", CONTENT);
									return true;
								case 6:
									if (!player.getInventory().playerHasItem(ROYAL_SEAL)) {
										d.sendPlayerChat("I don't have the royal seal with me...", SAD);
										d.endDialogue();
										return true;
									} else {
										d.sendGiveItemNpc("You show Lumbdo the Royal Seal.", new Item(ROYAL_SEAL));
										return true;
									}
								case 7:
									d.sendNpcChat("I see. Sorry for my mistrust.", CONTENT);
									return true;
								case 8:
									d.sendPlayerChat("So are you Lumbdo of the 10th Squad?", CONTENT);
									return true;
								case 9:
									d.sendNpcChat("I am indeed.", CONTENT);
									return true;
								case 10:
									d.sendPlayerChat("Where are the rest of your squad? Where is your", "Sergeant?", CONTENT);
									return true;
								case 11:
									d.sendNpcChat("Let me begin at the beginning human.", CONTENT);
									return true;
								case 12:
									d.sendNpcChat("We were on our way to decommission Glough's old ship", "building facilities in eastern Karamja, as you probably", "know. However, we obviously chose the wrong season to", "fly.", CONTENT);
									return true;
								case 13:
									d.sendNpcChat("We were one gnome to a glider, so each was extremely", "light. Like leaves in a wind, we were blown south before", "we even landed on that island.", CONTENT);
									return true;
								case 14:
									d.sendPlayerChat("Did you crash straight here?", CONTENT);
									return true;
								case 15:
									d.sendNpcChat("Yes. The winds drove us into the treetops, which", "destroyed the canvas of our gliders. We dragged what", "remained of the gliders out onto this beach.", CONTENT);
									return true;
								case 16:
									d.sendPlayerChat("What did you do then?", CONTENT);
									return true;
								case 17:
									d.sendNpcChat("Whilst we were falling, Sergeant Garkor noticed a large", "populated atoll to our west. You cannot see it from", "here, but it is within sailing distance.", CONTENT);
									return true;
								case 18:
									d.sendNpcChat("We spent time gathering enough wood to fashion two", "boats. The Sergeant took the rest of the 10th Squad in", "the larger of the boats to explore the island and to", "search for potential glider launch sites.", CONTENT);
									return true;
								case 19:
									d.sendPlayerChat("Presumably you are to guard the gliders until they", "return?", CONTENT);
									return true;
								case 20:
									d.sendNpcChat("Affirmative.", CONTENT);
									return true;
								case 21:
									d.sendPlayerChat("You must take us to the island. I have orders from the", "High Tree Guardian to make contact with your", "Sergeant.", CONTENT);
									return true;
								case 22:
									d.sendNpcChat("And I have orders from the Sergeant to stay here.", CONTENT);
									return true;
								case 23:
									d.sendPlayerChat("You will not take me?", CONTENT);
									return true;
								case 24:
									d.sendNpcChat("I'll tell you what I won't take...", "orders from you.", ANGRY_2);
									player.setQuestStage(36, LUMBDO_A_DICK);
									d.endDialogue();
									return true;
							}
							return false;
					}
					return false;
				}
			case WAYDAR_2:
				if (player.getQuestStage(36) > LUMBDO_A_DICK) {
					switch (d.getChatId()) {
						case 1:
							d.sendNpcChat("Would you like to go back to my kingdom?", CONTENT);
							return true;
						case 2:
							d.sendOption("Yes.", "No.");
							return true;
						case 3:
							switch (optionId) {
								case 1:
									d.sendPlayerChat("Yes, Commander.", CONTENT);
									return true;
								case 2:
									d.sendPlayerChat("No, not right now.", CONTENT);
									d.endDialogue();
									return true;
							}
							return false;
						case 4:
							d.sendNpcChat("As you wish.", CONTENT);
							return true;
						case 5:
							player.fadeTeleport(HANGAR_INITIALIZED);
							d.endDialogue();
							return true;
					}
					return false;
				} else {
					switch (player.getQuestStage(36)) {
						case CRACKED_THE_CODE:
							switch (d.getChatId()) {
								case 1:
									if (player.getMMVars().whereAreWeWaydar) {
										d.sendOption("What shall we do now?", "Do you recognize the gnome on the beach?", "Can you take me back to your kingdom?", "Nevermind...");
										d.setNextChatId(6);
									} else {
										d.sendPlayerChat("Where are we?", CONTENT);
									}
									return true;
								case 2:
									d.sendNpcChat("I am not sure. We appear to have landed where the", "10th Squad crashed. The number of gnome gliders is", "correct. Unfortunately for them, it appears that none of", "their gliders survived the collision.", CONTENT);
									return true;
								case 3:
									d.sendPlayerChat("Did our glider survive?", CONTENT);
									return true;
								case 4:
									d.sendNpcChat("Of course.", CONTENT);
									d.endDialogue();
									player.getMMVars().whereAreWeWaydar = true;
									return true;
								case 5:
									d.sendOption("What shall we do now?", "Do you recognize the gnome on the beach?", "Can you take me back to your kingdom?", "Nevermind...");
									return true;
								case 6:
									switch (optionId) {
										case 1:
											d.sendPlayerChat("What shall we do now?", CONTENT);
											return true;
										case 2:
											d.sendPlayerChat("Do you recognize the gnome on the beach?", CONTENT);
											d.setNextChatId(8);
											return true;
										case 3:
											d.sendPlayerChat("Can you take me back to your kingdom?", CONTENT);
											d.setNextChatId(9);
											return true;
										case 4:
											d.sendPlayerChat("Nevermind...", CONTENT);
											d.endDialogue();
											d.setNextChatId(11);
											return true;
									}
									return false;
								case 7:
									d.sendNpcChat("We need to find the 10th Squad, they probably", "left someone behind to guard their gliders.", CONTENT);
									d.setNextChatId(5);
									return true;
								case 8:
									d.sendNpcChat("I do not recognize him, though he may be a", "member of the 10th Squad if he is here.", CONTENT);
									d.setNextChatId(5);
									return true;
								case 9:
									d.sendNpcChat("Yes, I can.", CONTENT);
									return true;
								case 10:
									player.fadeTeleport(HANGAR_INITIALIZED);
									d.endDialogue();
									return true;
							}
							return false;
						case LUMBDO_A_DICK:
							switch (d.getChatId()) {
								case 1:
									if (player.getMMVars().whereAreWeWaydar) {
										d.sendOption("What shall we do now?", "Do you recognize the gnome on the beach?", "Can you take me back to your kingdom?", "I cannot convince Lumdo to take us to the island...");
										d.setNextChatId(5);
									} else {
										d.sendPlayerChat("Where are we?", CONTENT);
									}
									return true;
								case 2:
									d.sendNpcChat("I am not sure. We appear to have landed where the", "10th Squad crashed. The number of gnome gliders is", "correct. Unfortunately for them, it appears that none of", "their gliders survived the collision.", CONTENT);
									return true;
								case 3:
									d.sendPlayerChat("Did our glider survive?", CONTENT);
									d.setNextChatId(30);
									return true;
								case 4:
									d.sendOption("What shall we do now?", "Do you recognize the gnome on the beach?", "Can you take me back to your kingdom?", "I cannot convince Lumdo to take us to the island.");
									return true;
								case 5:
									switch (optionId) {
										case 1:
											d.sendPlayerChat("What shall we do now?", CONTENT);
											d.setNextChatId(6);
											return true;
										case 2:
											d.sendPlayerChat("Do you recognize the gnome on the beach?", CONTENT);
											d.setNextChatId(7);
											return true;
										case 3:
											d.sendPlayerChat("Can you take me back to your kingdom?", CONTENT);
											d.setNextChatId(8);
											return true;
										case 4:
											d.sendPlayerChat("I cannot convince Lumbdo to take us to the island...", SAD);
											d.setNextChatId(10);
											return true;
									}
									return false;
								case 6:
									d.sendNpcChat("We need to find the 10th Squad now, they probably", "left someone behind to guard their gliders.", CONTENT);
									d.setNextChatId(4);
									return true;
								case 7:
									d.sendNpcChat("I do not recognize him, though he may be a", "member of the 10th Squad if he is here.", CONTENT);
									d.setNextChatId(4);
									return true;
								case 8:
									d.sendNpcChat("Of course.", CONTENT);
									return true;
								case 9:
									player.fadeTeleport(HANGAR_INITIALIZED);
									d.endDialogue();
									return true;
								case 10:
									d.sendNpcChat("What is the problem?", CONTENT);
									return true;
								case 11:
									d.sendPlayerChat("He claims to be under direct orders from Garkor to", "guard their gliders until the rest return.", CONTENT);
									return true;
								case 12:
									d.sendNpcChat("His zeal in this matter is to be expected. The Royal", "Guard, in particular the 10th Squad, are renowned", "for their fierce loyalty.", CONTENT);
									return true;
								case 13:
									d.sendPlayerChat("Can you do anything?", CONTENT);
									return true;
								case 14:
									d.sendNpcChat("I would rather not get involved. My mission is to", "protect you.", CONTENT);
									return true;
								case 15:
									d.sendPlayerChat("You must do something!", DISTRESSED);
									return true;
								case 16:
									d.sendNpcChat("You are becoming tiresome, human. As you wish.", CONTENT);
									return true;
								case 17:
									d.sendNpcChat("Foot soldier Lumbdo of the 10th Squad.", CONTENT);
									return true;
								case 18:
									d.setLastNpcTalk(LUMBDO);
									d.sendNpcChat("Yes?", CONTENT);
									return true;
								case 19:
									d.setLastNpcTalk(WAYDAR_2);
									d.sendNpcChat("I am Flight Commander Waydar. I believe you are", "under direct orders from your Sergeant to guard these", "gliders?", CONTENT);
									return true;
								case 20:
									d.setLastNpcTalk(LUMBDO);
									d.sendNpcChat("That is correct, Commander.", CONTENT);
									return true;
								case 21:
									d.setLastNpcTalk(WAYDAR_2);
									d.sendNpcChat("I need not remind you that I outrank Garkor. As of", "this instant, your orders are to convey the human to", "the atoll and remain there until he needs to return.", CONTENT);
									return true;
								case 22:
									d.setLastNpcTalk(LUMBDO);
									d.sendNpcChat("Garkor will not be pleased!", ANGRY_1);
									return true;
								case 23:
									d.setLastNpcTalk(WAYDAR_2);
									d.sendNpcChat("The he can take up his issues with me personally.", CONTENT);
									return true;
								case 24:
									d.sendPlayerChat("Waydar! Will you not accompany me to the island?", DISTRESSED);
									return true;
								case 25:
									d.sendNpcChat("No, after all, somebody has to look after the gliders.", CONTENT);
									return true;
								case 26:
									d.sendPlayerChat("But it is your mission to protect me!", DISTRESSED);
									return true;
								case 27:
									d.sendNpcChat("Enough. Return here when you are done.", CONTENT);
									return true;
								case 28:
									openChapterInterface(player);
									player.getActionSender().sendString("Meanwhile, far away in Karamja...", 11115);
									d.dontCloseInterface();
									meanwhile(player);
									return true;
								case 30:
									d.sendNpcChat("Of course.", CONTENT);
									d.setNextChatId(4);
									player.getMMVars().whereAreWeWaydar = true;
									return true;
							}
							return false;
					}
				}
				return false;
			case CARADO:
				if (!player.getMMVars().canHideInGrass()) {
					switch (d.getChatId()) {
						case 1:
							d.sendNpcChat("Psst, if you haven't already - you should", "seek out our Assasin, Karam. He is skilled", "in the art of deception and can help you", "hide from those monkey archers.", CONTENT);
							return true;
						case 2:
							d.sendPlayerChat("How so?", CONTENT);
							return true;
						case 3:
							d.sendNpcChat("I'm not exactly sure, but he is always", "going on about surroundings, particularly the", "grass around here.", CONTENT);
							return true;
						case 4:
							d.sendPlayerChat("Alright, where is he?", CONTENT);
							return true;
						case 5:
							d.sendNpcChat("He's hiding all around, but you may", "find him just outside this cell to the east.", CONTENT);
							d.endDialogue();
							return true;
					}
				}
				return false;
			case WAYMOTTIN:
			case BUNKWICKET:
			case BUNKDO_2:
			case CARADO_2:
			case KARAM_3:
			case LUMO_2:
				if (player.getQuestStage(36) == FUCK_THE_DEMON) {
					switch (d.getChatId()) {
						case 1:
							d.sendNpcChat("Outstanding job adventurer.", HAPPY);
							d.endDialogue();
							return true;
					}
				}
				return false;
			case BUNKDO:
				switch (d.getChatId()) {
					case 1:
						d.sendNpcChat("You gotta get me out of here man.", DISTRESSED);
						return true;
					case 2:
						d.sendPlayerChat("I'm trying.", SAD);
						d.endDialogue();
						return true;
				}
				return false;
			case LUMO:
				if (player.getQuestStage(36) == MEANWHILE) {
					if (player.getMMVars().firstTimeJail()) {
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Look, a newcomer. I'd say let me be the first to", "welcome you to Ape Atoll, but I see you've already met", "the welcoming party...", CONTENT);
								return true;
							case 2:
								d.sendPlayerChat("Hello?", CONTENT);
								return true;
							case 3:
								d.sendNpcChat("Hello. Who are you?", CONTENT);
								return true;
							case 4:
								d.sendPlayerChat("I am in the service of King Narnode Shareen. I have", "been sent here to locate the missing 10th Squad of the", "Royal Guard.", CONTENT);
								return true;
							case 5:
								d.sendNpcChat("Well we're certainly missing home, I'll give you that.", CONTENT);
								return true;
							case 6:
								d.sendPlayerChat("How long have you been here?", CONTENT);
								return true;
							case 7:
								d.sendNpcChat("Oh, not for too long. The jail guards keep us", "entertained every now and then too.", CONTENT);
								return true;
							case 8:
								d.sendNpcChat("Isn't that right Bunkdo?", CONTENT);
								return true;
							case 9:
								d.setLastNpcTalk(BUNKDO);
								d.sendNpcChat("That's right Lumo. Remember the time when they got", "all confused when changing shifts?", CONTENT);
								return true;
							case 10:
								d.setLastNpcTalk(LUMO);
								d.sendNpcChat("Oh yeah. Trefaji and Aberab, they're as dense as", "bricks. They do give mighty punches though.", CONTENT);
								return true;
							case 11:
								d.setLastNpcTalk(BUNKDO);
								d.sendNpcChat("Indeed. My back side is still a little sore...", CONTENT);
								return true;
							case 12:
								d.setLastNpcTalk(LUMO);
								d.sendNpcChat("Your backside?", CONTENT);
								return true;
							case 13:
								d.setLastNpcTalk(BUNKDO);
								d.sendNpcChat("I turned around at the wrong time...", CONTENT);
								return true;
							case 14:
								d.setLastNpcTalk(CARADO);
								d.sendNpcChat("Serves you bloody right too.", CONTENT);
								return true;
							case 15:
								d.setLastNpcTalk(LUMO);
								d.sendNpcChat("Now, now lads, watch your language. You'll have to", "excuse them, human, they're only soldiers!", CONTENT);
								return true;
							case 16:
								d.setLastNpcTalk(CARADO);
								d.sendNpcChat("Excuse me...", CONTENT);
								return true;
							case 17:
								d.sendPlayerChat("Oh that's quite alright.", CONTENT);
								return true;
							case 18:
								d.setLastNpcTalk(LUMO);
								d.sendNpcChat("So human, what can we do for you?", CONTENT);
								return true;
							case 19:
								d.sendPlayerChat("Well I suppose I ought to help you escape.", CONTENT);
								return true;
							case 20:
								d.setLastNpcTalk(BUNKDO);
								d.sendNpcChat("No it's alright lad, we quite like it in here.", CONTENT);
								return true;
							case 21:
								d.setLastNpcTalk(LUMO);
								d.sendNpcChat("Do your best to ignore him...", CONTENT);
								return true;
							case 22:
								d.sendNpcChat("To be fair human, you're not much help stuck in here.", CONTENT);
								return true;
							case 23:
								d.sendPlayerChat("Well how do I get out?", CONTENT);
								return true;
							case 24:
								d.sendNpcChat("Well, you can try picking the lock on your door.", "Watch out for guards though.", CONTENT);
								return true;
							case 25:
								d.sendNpcChat("We've been trying to pick ours but we haven't been", "able to do so yet.", CONTENT);
								d.endDialogue();
								return true;
						}
						return false;
					} else {
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Any luck on getting us out yet?", CONTENT);
								return true;
							case 2:
								d.sendPlayerChat("I'm still working on it...", CONTENT);
								d.endDialogue();
								return true;
						}
					}
				} else if (player.getQuestStage(36) > MEANWHILE) {
					switch (player.getQuestStage(36)) {
						default:
							switch (d.getChatId()) {
								case 1:
									d.sendNpcChat("Any luck on getting us out yet?", CONTENT);
									return true;
								case 2:
									d.sendPlayerChat("I'm still working on it...", CONTENT);
									d.endDialogue();
									return true;
							}
							return false;
					}
				}
				return false;
			case KARAM:
			case KARAM_2:
				if (player.getQuestStage(36) >= MEANWHILE) {
					switch (d.getChatId()) {
						case 1:
							d.sendPlayerChat("Hello? Is anybody there?", CONTENT);
							return true;
						case 2:
							d.sendNpcChat("That depends on who is asking...", CONTENT);
							return true;
						case 3:
							d.sendPlayerChat("I am in the service of King Narnode Shareen. I have", "been sent here to locate the missing 10th Squad of the", "Royal Guard.", CONTENT);
							return true;
						case 4:
							d.sendNpcChat("I see. As you can see, this is a dangerous place to be.", CONTENT);
							return true;
						case 5:
							d.sendPlayerChat("It certainly is... who are you?", CONTENT);
							return true;
						case 6:
							d.sendNpcChat("Karam, 10th Squad, Royal Guard. High Assassin at", "your service.", CONTENT);
							return true;
						case 7:
							d.sendOption("What are you doing here?", "How do you remain unseen?", "How can you be everywhere?", "I'll be back later.");
							return true;
						case 8:
							switch (optionId) {
								case 1:
									d.sendPlayerChat("What are you doing here?", CONTENT);
									d.setNextChatId(9);
									return true;
								case 2:
									d.sendPlayerChat("How do you remain unseen?", CONTENT);
									d.setNextChatId(10);
									return true;
								case 3:
									d.sendPlayerChat("How can you be everywhere?", CONTENT);
									d.setNextChatId(13);
									return true;
								case 4:
									d.sendPlayerChat("I'll be back later.", CONTENT);
									d.endDialogue();
									return true;
							}
							return false;
						case 9:
							d.sendNpcChat("I am thinking of a way to free my friends. In the", "meantime, I am helping those who further my cause.", CONTENT);
							d.setNextChatId(7);
							return true;
						case 10:
							d.sendNpcChat("I have been gifted by Zooknock, our squad mage. I", "have saved his life on a number of occasions.", CONTENT);
							return true;
						case 11:
							d.sendPlayerChat("Is there any way I could be like that?", CONTENT);
							return true;
						case 12:
							d.sendNpcChat("Your best chance is to hide in this tall grass, human.", CONTENT);
							d.setNextChatId(7);
							player.getMMVars().setCanHideInGrass(true);
							return true;
						case 13:
							d.sendNpcChat("I move quickly.", CONTENT);
							d.setNextChatId(7);
							return true;
					}
				}
				return false;
			case GARKOR:
				switch (player.getQuestStage(36)) {
					case MEANWHILE:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello.", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("A fine day you have chosen to visit this hellish island,", "human.", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("Good day to you to Sergeant. I've been sent here", "by your King Narnode to-", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("Investigate the circumstances surrounding the", "mysterious disappearance of my squad. Yes, I know.", CONTENT);
								return true;
							case 5:
								d.sendPlayerChat("How did you know that?", CONTENT);
								return true;
							case 6:
								d.sendNpcChat("The King and I are still in communication, albeit", "sporadic. I decided I need a human of your caliber to", "assist me. It is pleasing to see you are still alive.", CONTENT);
								return true;
							case 7:
								d.sendPlayerChat("Why do you need a human?", CONTENT);
								return true;
							case 8:
								d.sendNpcChat("There is more going on than meets your eye, human.", "Did you not find it strange that an entire squad be", "sent to decommission a shipyard?", CONTENT);
								return true;
							case 9:
								d.sendPlayerChat("Well...", CONTENT);
								return true;
							case 10:
								d.sendNpcChat("Indeed. But there are more pressing matters at hand.", "Three of my squad have been captured and placed in", "the jail. They are watched over by somewhat", "overpowering guards.", CONTENT);
								return true;
							case 11:
								d.sendNpcChat("Before we can resume our original mission we must", "rescue them.", CONTENT);
								return true;
							case 12:
								d.sendPlayerChat("I know about the guards. I had to sneak out between", "the change of shifts.", CONTENT);
								return true;
							case 13:
								d.sendNpcChat("Trust me; we too have considered this, but whilst it is", "possible for one, it is near impossible for three.", CONTENT);
								return true;
							case 14:
								d.sendNpcChat("We have considered many things. I have my squad", "mage and sappers working below us right now. My", "Assassin Karam, is operating in the village itself.", CONTENT);
								return true;
							case 15:
								d.sendNpcChat("I remain here so that I may overhear Awowogei's plans.", CONTENT);
								return true;
							case 16:
								d.sendPlayerChat("Awowogei?", CONTENT);
								return true;
							case 17:
								d.sendNpcChat("The self-proclaimed ruler of the island. I have been", "listening to him for some time now. I believe we will", "incur a minimum of casualties if we have an insider - a", "monkey working for us.", CONTENT);
								return true;
							case 18:
								d.sendPlayerChat("Have you seen these monkeys? You could never", "convince them to work for you!", CONTENT);
								return true;
							case 19:
								d.sendNpcChat("I wasn't suggesting convincing them, human, but you.", CONTENT);
								return true;
							case 20:
								d.sendPlayerChat("Don't be ridiculous! I'm a human, not a monkey!", ANGRY_1);
								return true;
							case 21:
								d.sendNpcChat("Do not be so skeptical... you humans are considered to", "be quite closely related to monkeys.", CONTENT);
								return true;
							case 22:
								d.sendPlayerChat("Yes, but...", CONTENT);
								return true;
							case 23:
								d.sendNpcChat("Go and see my squad mage, Zooknock. Tell him I have", "asked you to be 'disguised' as a monkey so that you", "may infiltrate the village. As you will see, he is", "something of an expert on the subject.", CONTENT);
								return true;
							case 24:
								d.sendPlayerChat("I can't even communicate with the monkeys!", CONTENT);
								return true;
							case 25:
								d.sendNpcChat("Just go and find my squad mage, human.", CONTENT);
								player.setQuestStage(36, GARKORS_ORDERS);
								d.endDialogue();
								return true;
						}
						return false;
					case GARKORS_ORDERS:
					case ZOOKNOCK_SPEECH:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Have you obtained a disguise yet?", CONTENT);
								return true;
							case 2:
								d.sendPlayerChat("Not yet I'm afraid...", CONTENT);
								return true;
							case 3:
								d.sendNpcChat("Remember to talk to Zooknock in the underground", "caves if you need help, human.", CONTENT);
								d.endDialogue();
								return true;
						}
						return false;
					case MONKEY_MAN:
						switch (d.getChatId()) {
							case 1:
								if (player.getMMVars().isMonkey()) {
									d.sendNpcChat("My, my, Zooknock has outdone himself this time. You", "do look very much like a monkey with that.", CONTENT);
								} else {
									d.sendNpcChat("Hm, you don't quite look like a monkey yet.", "Come back when you're disguised and we'll talk then.", CONTENT);
									d.endDialogue();
								}
								return true;
							case 2:
								d.sendPlayerChat("I know.", CONTENT);
								return true;
							case 3:
								if (getMonkeyTransformName(player).equals("MONKEY")) {
									d.sendNpcChat("And by happy coincidence you appear to be just the", "right sort of monkey.", CONTENT);
								} else {
									d.sendNpcChat("Hm, you're not disguised as a simple Karamjan", "monkey. You'll need to look humble for this next task.", "Come back when you have the proper greegree.", CONTENT);
									d.endDialogue();
								}
								return true;
							case 4:
								d.sendNpcChat("I need you now to seek audience with Awowogei. Claim", "you are an envoy from the monkeys of Karamja and", "are seeking an alliance.", CONTENT);
								return true;
							case 5:
								d.sendNpcChat("You must win his trust if we are to succeed.", CONTENT);
								player.setQuestStage(36, GARKORS_PLAN);
								d.endDialogue();
								return true;
						}
						return true;
					case GARKORS_PLAN:
					case AWOWOGEIS_TEST:
					case INTO_THE_CAGE:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("I need you now to seek audience with Awowogei. Claim", "you are an envoy from the monkeys of Karamja and", "are seeking an alliance.", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("You must win his trust if we are to succeed.", CONTENT);
								d.endDialogue();
								return true;
						}
						return false;
					case PASSED_THE_TEST:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Well done on winning Awowogei's trust. I overheard", "everything from here.", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("However, your efforts may be in vain...", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("What do you mean?", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("Progress in the caves has been slow. Whilst you were", "in Ardougne, Bunkwicket and Waymottin overheard a", "slightly disturbing conversation.", CONTENT);
								return true;
							case 5:
								d.sendPlayerChat("Who was speaking? What was said?", CONTENT);
								return true;
							case 6:
								d.sendNpcChat("Listen closely whilst I narrate the details.", CONTENT);
								return true;
							case 7:
								openChapterInterface(player);
								player.getActionSender().sendString("Somewhere far below the Ape Atoll...", 11115);
								d.dontCloseInterface();
								meanwhile(player);
								return true;
						}
						return false;
					case GARKORS_RETELLING:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("What shall we do?", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Zooknock and I have come up with a plan.", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("What kind of plan?", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("I hope you were listening closely. The teleportation spell", "that was provided will teleport ALL of the 10th Squad, no", "matter where we may be.", CONTENT);
								return true;
							case 5:
								d.sendNpcChat("In effect, the spell will break Lumo, Bunkdo and Carado", "out of the jail for us.", CONTENT);
								return true;
							case 6:
								d.sendPlayerChat("But you will be teleported straight into whatever trap", "they have prepared!", CONTENT);
								return true;
							case 7:
								d.sendNpcChat("Indeed. This is where you come in. Do not forget", "we are the 10th Squad of the Royal Guard, and that we", "are more than capable of holding our own.", CONTENT);
								return true;
							case 8:
								d.sendNpcChat("with your assistance, we should be able to defeat", "whatever is thrown at us.", CONTENT);
								return true;
							case 9:
								d.sendPlayerChat("How will I join you?", CONTENT);
								return true;
							case 10:
								d.sendNpcChat("Simple. We fool the teleportation spell that you are in", "fact a member of our squad.", CONTENT);
								return true;
							case 11:
								d.sendPlayerChat("What?", CONTENT);
								return true;
							case 12:
								d.sendNpcChat("Zooknock knows Glough's grasp of magic well. He", "believes the spell is linked to the sigils that all of our", "squad carry.", CONTENT);
								return true;
							case 13:
								d.sendNpcChat("It is these sigils that identify us as a member of the", "squad.", CONTENT);
								return true;
							case 14:
								d.sendGiveItemNpc("Garkor hands you some kind of medallion.", new Item(TENTH_SQUAD_SIGIL));
								player.getInventory().addItemOrDrop(new Item(TENTH_SQUAD_SIGIL));
								return true;
							case 15:
								d.sendNpcChat("Welcome to the 10th Squad " + player.getUsername() + ".", CONTENT);
								return true;
							case 16:
								d.sendPlayerChat("What is it?", CONTENT);
								return true;
							case 17:
								d.sendNpcChat("It is a replica Waymottin has made of our squad sigils.", "If you wear that when the spell is cast, you will be", "summoned along with the rest of us.", CONTENT);
								return true;
							case 18:
								d.sendNpcChat("You should prepare. Collect your thoughts and", "belongings and then wear the sigil. Hurry, human, we", "do not wish to enter this fight without you.", CONTENT);
								return true;
							case 19:
								d.sendPlayerChat("All I have to do is wear the sigil?", CONTENT);
								return true;
							case 20:
								d.sendNpcChat("Yes, but do not do so until you are ready.", CONTENT);
								player.setQuestStage(36, NEW_MEMEBER);
								d.endDialogue();
								return true;
						}
						return false;
					case NEW_MEMEBER:
						switch (d.getChatId()) {
							case 1:
								d.sendOption("What should I do now?", "Perhaps I lost the 10th Squad sigil?");
								return true;
							case 2:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("What should I do now?", CONTENT);
										return true;
									case 2:
										d.sendPlayerChat("Perhaps I lost the 10th Squad sigil?", CONTENT);
										d.setNextChatId(10);
										return true;
								}
							case 3:
								d.sendNpcChat("You should prepare. Collect your thoughts and", "belongings and then wear the sigil. Hurry, human, we", "do not wish to enter this fight without you.", CONTENT);
								return true;
							case 4:
								d.sendPlayerChat("All I have to do is wear the sigil?", CONTENT);
								return true;
							case 5:
								d.sendNpcChat("Yes, but do not do so until you are ready.", CONTENT);
								d.endDialogue();
								return true;
							case 10:
								d.sendNpcChat("Here's another, but be careful, I only", "have so many of these.", CONTENT);
								player.getInventory().addItemOrDrop(new Item(TENTH_SQUAD_SIGIL));
								d.endDialogue();
								return true;
						}
						return false;
				}
				return false;
			case ZOOKNOCK:
				if (player.getQuestStage(36) >= GARKORS_ORDERS && player.getQuestStage(36) < MONKEY_MAN) {
					switch (player.getQuestStage(36)) {
						case GARKORS_ORDERS:
							switch (d.getChatId()) {
								case 1:
									d.sendPlayerChat("Hello?", CONTENT);
									return true;
								case 2:
									d.sendNpcChat("A human... here? What business have you on Ape", "Atoll?", CONTENT);
									return true;
								case 3:
									d.sendPlayerChat("I am on a mission for King Narnode Shareen of the", "gnomes.", CONTENT);
									return true;
								case 4:
									d.sendPlayerChat("I have been sent to investigate the whereabouts of the", "10th Squad of his Royal Guard, which went missing", "during a recent mission to Karamja.", CONTENT);
									return true;
								case 5:
									d.sendNpcChat("Well you've found us, what's left of us that is.", CONTENT);
									return true;
								case 6:
									d.sendNpcChat("I am Zooknock, the 10th Squad mage. These are", "Bunkwicket and Waymottin, two of our finest sappers.", "I assume you will want to know how we got here?", CONTENT);
									return true;
								case 7:
									d.sendPlayerChat("Of course!", CONTENT);
									return true;
								case 8:
									d.sendNpcChat("Your story first, human. What possessed you to travel", "to this forsaken island?", CONTENT);
									return true;
								case 9:
									d.sendPlayerChat("I am currently in the service of your King. I was the", "human who exposed Glough's warfaring plans and", "defeated his demon.", CONTENT);
									return true;
								case 10:
									d.sendPlayerChat("As far as I understand, the 10th Squad were sent to", "oversee the decommission of Glough's shipyard in east", "Karamja.", CONTENT);
									return true;
								case 11:
									d.sendPlayerChat("Rumour has it you were blown off course. The King", "worried as to your fate, and sent me to investigate.", CONTENT);
									return true;
								case 12:
									d.sendNpcChat("You were sent alone?", CONTENT);
									return true;
								case 13:
									d.sendPlayerChat("No, I have been accompanied by Flight Commander", "Waydar. We flew south on a special type of glider and", "landed on a small island to our east.", CONTENT);
									return true;
								case 14:
									d.sendNpcChat("The so called Crash Island. We left there one of our", "number, Lumdo, to guard our gliders until our return.", CONTENT);
									return true;
								case 15:
									d.sendPlayerChat("Yes, we have met. He ferried me across to the atoll.", CONTENT);
									return true;
								case 16:
									d.sendNpcChat("He did?! He was explicitly ordered to guard the gliders!", "How did this happen? Who is guarding the gliders now?", ANGRY_1);
									return true;
								case 17:
									d.sendPlayerChat("Waydar ordered him to leave his post. He is guarding", "the gliders himself.", CONTENT);
									return true;
								case 18:
									d.sendNpcChat("Flight Commander Waydar you said? For some reason", "that name is familiar...", CONTENT);
									return true;
								case 19:
									d.sendPlayerChat("So why are you here?", CONTENT);
									return true;
								case 20:
									d.sendNpcChat("The rumors are correct. We were indeed blown off", "course. Fortunately, we managed to find a small island", "to steer our gliders towards, else we surely would have", "drowned.", CONTENT);
									return true;
								case 21:
									d.sendPlayerChat("When you gathered enough wood to fashion two boats.", "your Sergeant and the rest of the 10th Squad took the", "larger boat to this island, leaving Lumdo to guard the", "gliders and the other boat.", CONTENT);
									return true;
								case 22:
									d.sendNpcChat("Correct. I assume Lumdo told you this?", CONTENT);
									return true;
								case 23:
									d.sendPlayerChat("Yes. What happened when you landed here?", CONTENT);
									return true;
								case 24:
									d.sendNpcChat("We split up into several small groups to search the", "island for potential gnome glider launch sites. Whilst we", "knew the island to be inhabited, we did not expect its", "occupants to be quite so... militant.", CONTENT);
									return true;
								case 25:
									d.sendPlayerChat("...", CONTENT);
									return true;
								case 26:
									d.sendNpcChat("Naggers. Lots of monkeys. They are unlike any other", "type of monkey we've come across. A far cry from the", "usual wild variety, these were armed with high quality", "weaponry and uncanny tactical ability.", CONTENT);
									return true;
								case 27:
									d.sendNpcChat("We were overwhelmed in numbers. Some of us", "managed to escape, but the rest were taken captive.", CONTENT);
									return true;
								case 28:
									d.sendPlayerChat("Who survived?", CONTENT);
									return true;
								case 29:
									d.sendNpcChat("Myself, the Sergeant, Bunkwicket and Waymottin here.", "Karam, our assassin, probably managed to escape, he", "usually does.", CONTENT);
									return true;
								case 30:
									d.sendPlayerChat("And of the rest?", CONTENT);
									return true;
								case 31:
									d.sendNpcChat("Lumo, Bunkdo and Carado were captured, as I said.", "We believe they are being held in the jail. We are", "working on a way to release them. I have sensed there", "lies a cavern to the north.", CONTENT);
									return true;
								case 32:
									d.sendNpcChat("We are attempting to tunnel to this northern cavern", "and then move upwards from there.", CONTENT);
									return true;
								case 33:
									d.sendPlayerChat("Why don't you just go overground?", CONTENT);
									return true;
								case 34:
									d.sendNpcChat("We have considered this, but every entrance seems to", "be excessively guarded.", CONTENT);
									return true;
								case 35:
									d.sendPlayerChat("I see...", CONTENT);
									return true;
								case 36:
									d.sendPlayerChat("I have spoken to your Sergeant. He believes that the", "best way to rescue the rest of your squad with the", "minimum of casualties is to have an insider, a monkey", "working for us.", CONTENT);
									return true;
								case 37:
									d.sendNpcChat("Aha. He wants me to turn you into a monkey.", CONTENT);
									return true;
								case 38:
									d.sendPlayerChat("Actually, it was more along the lines of a disguise...", CONTENT);
									return true;
								case 39:
									d.sendNpcChat("I think you misunderstand, human. Do you know why", "you were sent here?", CONTENT);
									return true;
								case 40:
									d.sendPlayerChat("King Narnode Shareen asked me to...", CONTENT);
									return true;
								case 41:
									d.sendNpcChat("Indeed. However, King Narnode Shareen is still in", "contact with Garkor! You were sent here because", "Garkor specifically asked Narnode for you!", CONTENT);
									return true;
								case 42:
									d.sendPlayerChat("Why wasn't I told?", CONTENT);
									return true;
								case 43:
									d.sendNpcChat("Before you arrived on this island, that information", "would have endangered both yourself and the mission.", CONTENT);
									return true;
								case 44:
									d.sendPlayerChat("But why a human? Why me?", CONTENT);
									return true;
								case 45:
									d.sendNpcChat("Garkor had long decided that we need a monkey", "insider. I have the necessary magic to perform the", "shapeshifting spell, but we needed a human to", "transform.", CONTENT);
									return true;
								case 46:
									d.sendPlayerChat("Why don't you just transform a gnome?", CONTENT);
									return true;
								case 47:
									d.sendNpcChat("It has been tried in the past, but the results were far", "from.... satisfactory. Although we, like you are related", "to the monkeys, the link is too weak for a successful", "transformation. That is why we need you.", CONTENT);
									return true;
								case 48:
									d.sendPlayerChat("Right. What do I have to do?", CONTENT);
									return true;
								case 49:
									d.sendNpcChat("There will be two aspects to your transformation. We", "must first arrange it so that you are able to", "understand and communicate with the monkeys.", CONTENT);
									return true;
								case 50:
									d.sendNpcChat("We must also transform your body so that you may", "pass amongst them unnoticed.", CONTENT);
									return true;
								case 51:
									d.sendNpcChat("So that the effects of my spells are not permanent, I", "will invest their power into magical items which you", "must find. You can then use them at your will.", CONTENT);
									return true;
								case 52:
									d.sendPlayerChat("What kind of items?", CONTENT);
									return true;
								case 53:
									d.sendNpcChat("For the spells to take full effect, they will have to be in", "some way related to the monkeys.", CONTENT);
									return true;
								case 54:
									d.sendNpcChat("I suggest that I invest the ability to communicate with", "the monkeys in an authentic monkey amulet.", CONTENT);
									return true;
								case 55:
									d.sendNpcChat("Similarly, the transformation spell should be stored in a", "monkey talisman of some kind.", CONTENT);
									d.endDialogue();
									player.setQuestStage(36, ZOOKNOCK_SPEECH);
									return true;
							}
							return false;
						case ZOOKNOCK_SPEECH:
							if (player.getInventory().playerHasItem(MONKEY_DENTURES) && player.getInventory().playerHasItem(MONKEYSPEAK_AMULET_MOULD) && player.getInventory().playerHasItem(GOLD_BAR)) {
								switch (d.getChatId()) {
									case 1:
										d.sendNpcChat("Good work.", CONTENT);
										return true;
									case 2:
										d.sendNpcChat("Now listen closely, human. I will case a spell to enchant", "this gold bar with the power contained in these monkey", "dentures.", CONTENT);
										return true;
									case 3:
										d.sendNpcChat("You must then smith the gold using the monkey amulet", "mould. However, unless you do this in a place of", "religious significance to the monkeys, the spirits", "contained within will likely depart.", CONTENT);
										return true;
									case 4:
										d.sendPlayerChat("Where do I find a place of religious significance to", "monkeys?", CONTENT);
										return true;
									case 5:
										d.sendNpcChat("Somewhere in the village. It ought to be obvious. Now,", "give me a moment.", CONTENT);
										return true;
									case 6:
										player.getInventory().replaceItemWithItem(new Item(GOLD_BAR), new Item(ENCHANTED_BAR));
										player.getInventory().removeItem(new Item(MONKEY_DENTURES));
										d.sendGiveItemNpc("Zooknock hands you back the gold bar and the", "monkey amulet mould.", new Item(ENCHANTED_BAR), new Item(MONKEYSPEAK_AMULET_MOULD));
										player.getMMVars().setGotAmulet(true);
										if (amuletAndTalisman(player)) {
											return true;
										} else {
											d.endDialogue();
											return true;
										}
									case 7:
										openChapterInterface(player);
										player.getActionSender().sendString("Meanwhile, somewhere far below the Ape Atoll...", 11115);
										d.dontCloseInterface();
										meanwhile(player);
										return true;
								}
							} else if (player.getInventory().playerHasItem(KARAMJA_MONKEY_BONES) && player.getInventory().playerHasItem(MONKEY_TALISMAN)) {
								switch (d.getChatId()) {
									case 1:
										d.sendNpcChat("Excellent.", CONTENT);
										return true;
									case 2:
										d.sendNpcChat("Bear with me human, I must now cast an extremely", "powerful spell. It is not often we are successful in", "investing shapeshifting powers within objects.", CONTENT);
										return true;
									case 3:
										d.sendStatement("Zooknock seems to focus intently.");
										return true;
									case 4:
										d.sendStatement("The air begins to hum.");
										return true;
									case 5:
										int talismanId = GreeGreeData.talismanForBones(getFirstBonesInInventory(player));
										d.sendGiveItemNpc("Zooknock hands you back the talisman. It seems to glow.", new Item(talismanId));
										return true;
									case 6:
										d.sendNpcChat("I am afraid I have not been able to fully invest my", "powers in that talisman. You may use it, but it will", "continue to draw its energy directly from me.", CONTENT);
										return true;
									case 7:
										d.sendNpcChat("The range at which I will be able to sustain it is limited.", "I cannot ensure it will be effective off the atoll.", CONTENT);
										return true;
									case 8:
										d.sendNpcChat("Furthermore, you will not be able to attack whilst using", "this, so be careful. perhaps when I refine my spells I", "could look into making this possible.", CONTENT);
										return true;
									case 9:
										player.getMMVars().setGotTalisman(true);
										int talisman = GreeGreeData.talismanForBones(getFirstBonesInInventory(player));
										int bonesId = getFirstBonesInInventory(player);
										player.getInventory().replaceItemWithItem(new Item(MONKEY_TALISMAN), new Item(talisman));
										player.getInventory().removeItem(new Item(bonesId));
										if (amuletAndTalisman(player)) {
											openChapterInterface(player);
											player.getActionSender().sendString("Meanwhile, somewhere far below the Ape Atoll...", 11115);
											d.dontCloseInterface();
											meanwhile(player);
										} else {
											player.getActionSender().removeInterfaces();
										}
										return true;
								}
							} else {
								switch (d.getChatId()) {
									case 1:
										d.sendOption("What do we need for the monkey amulet?", "What do we need for the monkey talisman?");
										d.setNextChatId(57);
										return true;
									case 56:
										d.sendOption("What do we need for the monkey amulet?", "What do we need for the monkey talisman?");
										return true;
									case 57:
										switch (optionId) {
											case 1:
												d.sendPlayerChat("What do we need for the monkey amulet?", CONTENT);
												return true;
											case 2:
												d.sendPlayerChat("What do we need for the monkey talisman?", CONTENT);
												d.setNextChatId(64);
												return true;
										}
										return false;
									case 58:
										d.sendNpcChat("We need a gold bar, a monkey amulet mould, and", "something to do monkey speech.", CONTENT);
										return true;
									case 59:
										d.sendOption("Where do I find a gold bar?", "Where do I find a monkey amulet mould?", "Where do I find something to do monkey speech?", "I'll be back later.");
										return true;
									case 60:
										switch (optionId) {
											case 1:
												d.sendPlayerChat("Where do I find a gold bar?", CONTENT);
												return true;
											case 2:
												d.sendPlayerChat("Where do I find a monkey amulet mold?", CONTENT);
												d.setNextChatId(62);
												return true;
											case 3:
												d.sendPlayerChat("Where do I find something to do monkey speech?", CONTENT);
												d.setNextChatId(63);
												return true;
											case 4:
												d.sendPlayerChat("I'll be back later.", CONTENT);
												d.endDialogue();
												return true;
										}
										return false;
									case 61:
										d.sendNpcChat("I'll leave that to you, there's no way you would have", "gotten this far and couldn't figure out how to make a", "bar of gold.", CONTENT);
										d.setNextChatId(59);
										return true;
									case 62:
										d.sendNpcChat("I'm sure you could find one in the village above.", "It might be where the thing to do monkey speech is too.", CONTENT);
										d.setNextChatId(59);
										return true;
									case 63:
										d.sendNpcChat("I'm sure there's something in the village above.", "It might be where the amulet mould is too.", CONTENT);
										d.setNextChatId(59);
										return true;
									case 64:
										d.sendNpcChat("We need some kind of monkey remains as well as an", "authentic magical monkey talisman.", CONTENT);
										return true;
									case 65:
										d.sendOption("Where do I find monkey remains?", "Where do I find a magical monkey talisman?", "I'll be back later.");
										return true;
									case 66:
										switch (optionId) {
											case 1:
												d.sendPlayerChat("Where do I find monkey remains?", CONTENT);
												return true;
											case 2:
												d.sendPlayerChat("Where do I find a magical monkey talisman?", CONTENT);
												d.setNextChatId(70);
												return true;
											case 3:
												d.sendPlayerChat("I'll be back later.", CONTENT);
												d.endDialogue();
												return true;
										}
										return false;
									case 67:
										d.sendNpcChat("I'll leave that to your better judgment... However, bear", "in mind the type of remain might affect the type of", "monkey you become...", CONTENT);
										return true;
									case 68:
										d.sendPlayerChat("What if I need to be another type of monkey?", CONTENT);
										return true;
									case 69:
										d.sendNpcChat("Then bring me different monkey remains and another", "talisman.", CONTENT);
										d.setNextChatId(65);
										return true;
									case 70:
										d.sendNpcChat("There ought to be something in the village. I cannot", "be sure, as I have not spent much time there.", CONTENT);
										d.setNextChatId(65);
										return true;
								}
							}
							return false;
					}
				} else {
					if (player.getQuestStage(36) >= MONKEY_MAN) {
						if (player.getInventory().playerHasItem(MONKEY_DENTURES) && player.getInventory().playerHasItem(MONKEYSPEAK_AMULET_MOULD) && player.getInventory().playerHasItem(GOLD_BAR)) {
							switch (d.getChatId()) {
								case 1:
									d.sendPlayerChat("Hello Zocknook. I have some supplies for a new", "monkey amulet.", CONTENT);
									return true;
								case 2:
									d.sendNpcChat("Cool, cool.", CONTENT);
									return true;
								case 3:
									player.getInventory().removeItem(new Item(MONKEY_DENTURES));
									player.getInventory().removeItem(new Item(GOLD_BAR));
									player.getInventory().removeItem(new Item(MONKEYSPEAK_AMULET_MOULD));
									d.sendGiveItemNpc("Zooknock hands you back the gold bar and the monkey", "amulet mould.", new Item(ENCHANTED_BAR), new Item(MONKEYSPEAK_AMULET_MOULD));
									player.getInventory().addItemOrDrop(new Item(ENCHANTED_BAR));
									player.getInventory().addItemOrDrop(new Item(MONKEYSPEAK_AMULET_MOULD));
									d.endDialogue();
									return true;
							}
						} else if (getFirstBonesInInventory(player) != 0 && player.getInventory().playerHasItem(MONKEY_TALISMAN)) {
							switch (d.getChatId()) {
								case 1:
									d.sendPlayerChat("Hello Zocknook. I have some supplies for a new", "enchanted talisman.", CONTENT);
									return true;
								case 2:
									d.sendNpcChat("Cool, cool.", CONTENT);
									return true;
								case 3:
									int bonesId = getFirstBonesInInventory(player);
									int talismanId = GreeGreeData.talismanForBones(bonesId);
									if (talismanId != -1) {
										player.getInventory().replaceItemWithItem(new Item(MONKEY_TALISMAN), new Item(talismanId));
										player.getInventory().removeItem(new Item(bonesId));
										d.sendGiveItemNpc("Zocknook hands you back the talisman. It seems to glow.", new Item(talismanId));
										d.endDialogue();
										return true;
									}
							}
						} else {
							switch (d.getChatId()) {
								case 1:
									if (player.getQuestStage(36) > MONKEY_MAN) {
										d.sendOption("What do we need for the monkey amulet?", "What do we need for the monkey talisman?");
										d.setNextChatId(57);
									} else {
										d.sendNpcChat("Well done with the items adventurer. You should go", "report to Garkor. Unless you have questions of course.", CONTENT);
										return true;
									}
									return true;
								case 2:
									d.sendOption("I do have some questions actually.", "Alright, thank you.");
									return true;
								case 3:
									switch (optionId) {
										case 1:
											d.sendPlayerChat("I do have some questions actually.", CONTENT);
											d.setNextChatId(56);
											return true;
										case 2:
											d.sendPlayerChat("Alright, thank you.", CONTENT);
											d.endDialogue();
											return true;
									}
								case 56:
									d.sendOption("What do I need for a monkey amulet again?", "What do I need for the monkey talisman again?");
									return true;
								case 57:
									switch (optionId) {
										case 1:
											d.sendPlayerChat("What do I need for a monkey amulet again?", CONTENT);
											return true;
										case 2:
											d.sendPlayerChat("What do I need for a monkey talisman again?", CONTENT);
											d.setNextChatId(64);
											return true;
									}
									return false;
								case 58:
									d.sendNpcChat("You need a gold bar, a monkey amulet mould, and", "those magical monkey dentures.", CONTENT);
									return true;
								case 59:
									d.sendOption("Where do I find a gold bar?", "Where do I find a monkey amulet mould?", "Where do I find magical monkey dentures?", "I'll be back later.");
									return true;
								case 60:
									switch (optionId) {
										case 1:
											d.sendPlayerChat("Where do I find a gold bar?", CONTENT);
											return true;
										case 2:
											d.sendPlayerChat("Where do I find a monkey amulet mold?", CONTENT);
											d.setNextChatId(62);
											return true;
										case 3:
											d.sendPlayerChat("Where do I find magical monkey dentures?", CONTENT);
											d.setNextChatId(63);
											return true;
										case 4:
											d.sendPlayerChat("I'll be back later.", CONTENT);
											d.endDialogue();
											return true;
									}
									return false;
								case 61:
									d.sendNpcChat("I'll leave that to you, there's no way you would have", "gotten this far and couldn't figure out how to make a", "bar of gold.", CONTENT);
									d.setNextChatId(59);
									return true;
								case 62:
									d.sendNpcChat("I'm sure you could find one in the village above.", "It might be where the thing to do monkey speech is too.", CONTENT);
									d.setNextChatId(59);
									return true;
								case 63:
									d.sendNpcChat("You tell me adventurer, you already found some!", LAUGHING);
									d.setNextChatId(59);
									return true;
								case 64:
									d.sendNpcChat("We need some kind of monkey remains as well as an", "authentic magical monkey talisman.", CONTENT);
									return true;
								case 65:
									d.sendOption("Where do I find monkey remains?", "Where do I find a magical monkey talisman?", "I'll be back later.");
									return true;
								case 66:
									switch (optionId) {
										case 1:
											d.sendPlayerChat("Where do I find monkey remains?", CONTENT);
											return true;
										case 2:
											d.sendPlayerChat("Where do I find a magical monkey talisman?", CONTENT);
											d.setNextChatId(70);
											return true;
										case 3:
											d.sendPlayerChat("I'll be back later.", CONTENT);
											d.endDialogue();
											return true;
									}
									return false;
								case 67:
									d.sendNpcChat("I'll leave that to your better judgment... However, bear", "in mind the type of remain might affect the type of", "monkey you become...", CONTENT);
									return true;
								case 68:
									d.sendPlayerChat("What if I need to be another type of monkey?", CONTENT);
									return true;
								case 69:
									d.sendNpcChat("Then bring me different monkey remains and another", "talisman.", CONTENT);
									d.setNextChatId(65);
									return true;
								case 70:
									d.sendNpcChat("You tell me adventurer, you already found one!", LAUGHING);
									d.setNextChatId(65);
									return true;
							}
						}
					}
				}
				return false;
			case AWOWOGEI:
				if (player.getEquipment().getId(Constants.AMULET) != MONKEYSPEAK_AMULET) {
					d.sendNpcChat("Ook! Ook!", ANGRY_1);
					d.endDialogue();
					return true;
				}
				switch (player.getQuestStage(36)) {
					case KRUKS_PERMISSION:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Greetings, Awowogei.", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Greetings, visitor. What brings you to my island?", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("I am an envoy from the monkeys of Karamja.", "We wish to propose an alliance.", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("I see. Ours is a strong and mighty lineage of", "monkey, visitor. We clearly do not need your offer", "of an alliance.", CONTENT);
								return true;
							case 5:
								d.sendPlayerChat("Awowogei, please consider my offer carefully.", CONTENT);
								return true;
							case 6:
								d.sendPlayerChat("We offer strength in numbers and our island would", "serve as a northern platform for defence. All that we", "ask for in return is peace.", CONTENT);
								return true;
							case 7:
								d.setLastNpcTalk(UWOGO);
								d.sendNpcChat("I don't believe him, Awowogei. Never trust a northern", "Monkey.", CONTENT);
								return true;
							case 8:
								d.setLastNpcTalk(AWOWOGEI);
								d.sendNpcChat("What is your opinion, Muruwoi?", CONTENT);
								return true;
							case 9:
								d.setLastNpcTalk(MURUWOI);
								d.sendNpcChat("I think he seems trustworthy, sir.", CONTENT);
								return true;
							case 10:
								d.setLastNpcTalk(AWOWOGEI);
								d.sendNpcChat("I must admit. I have always regared your kind", "as our inferior cousins, visitor.", CONTENT);
								return true;
							case 11:
								d.sendNpcChat("However, I am well aware that you may have a few", "things to offer.", CONTENT);
								return true;
							case 12:
								d.setLastNpcTalk(UWOGO);
								d.sendNpcChat("Don't listen to him Awowogei!", CONTENT);
								return true;
							case 13:
								d.setLastNpcTalk(AWOWOGEI);
								d.sendNpcChat("Be silent, Uwogo!", CONTENT);
								return true;
							case 14:
								d.sendNpcChat("I have heard your kind are exceptionally resourceful.", "I wish to put this reputation to the test.", CONTENT);
								return true;
							case 15:
								d.sendNpcChat("You must be well aware your kind are hunted and", "trapped almost everywhere.", CONTENT);
								return true;
							case 16:
								d.sendNpcChat("In particular you may have heard of such activities", "in a city known to the humans as Ardougne.", CONTENT);
								return true;
							case 17:
								d.sendNpcChat("There are several of your kind kept captive there. I", "challenge you to free one and return it to me.", CONTENT);
								return true;
							case 18:
								d.sendPlayerChat("How am I meant to free one of them?", CONTENT);
								return true;
							case 19:
								d.sendNpcChat("This is for you to decide, visitor.", CONTENT);
								return true;
							case 20:
								d.sendPlayerChat("Very well. I will be back later, with one of the captives.", CONTENT);
								player.setQuestStage(36, AWOWOGEIS_TEST);
								d.endDialogue();
								return true;
						}
						return false;
					case AWOWOGEIS_TEST:
					case INTO_THE_CAGE:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Have you gone to Ardougne and", "retrieved a captive yet?", CONTENT);
								return true;
							case 2:
								d.sendPlayerChat("Not yet.", CONTENT);
								d.endDialogue();
								return true;
						}
						return false;
					case THE_GREAT_ESCAPE:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Have you brought with you a captive?", CONTENT);
								return true;
							case 2:
								if (player.getInventory().playerHasItem(MONKEY_ITEM)) {
									d.sendPlayerChat("Yes, I have.", CONTENT);
								} else {
									d.sendPlayerChat("No, I haven't...", CONTENT);
									d.endDialogue();
								}
								return true;
							case 3:
								d.sendNpcChat("Well done!", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("You have shown yourself to be very resourceful. You", "have managed to complete an extremely long journey", "remarkably quickly.", CONTENT);
								return true;
							case 5:
								d.sendPlayerChat("Thank you!", CONTENT);
								return true;
							case 6:
								d.sendNpcChat("You are clearly well acquainted with the ways of this", "world. We will talk more on this later.", CONTENT);
								return true;
							case 7:
								d.sendNpcChat("In the meantime, feel free to remain as long as you", "like on my island.", CONTENT);
								return true;
							case 8:
								d.sendPlayerChat("What about the proposed alliance, Awowogei?", CONTENT);
								return true;
							case 9:
								d.sendNpcChat("I must think upon it some more and discuss the", "matter with my advisors. We will contact you", "when we are ready.", CONTENT);
								d.endDialogue();
								player.setQuestStage(36, PASSED_THE_TEST);
								player.getInventory().removeItem(new Item(MONKEY_ITEM));
								return true;
						}
						return false;
				}
				return false;
			case SHIPYARD_WORKER:
				if (player.getQuestStage(36) == 1 && !player.getMMVars().openGate()) {
					switch (d.getChatId()) {
						case 1:
							d.sendNpcChat("Hey you! What are you up to?", CONTENT);
							return true;
						case 2:
							d.sendPlayerChat("I'm trying to go through to the shipyard.", CONTENT);
							return true;
						case 3:
							d.sendNpcChat("I can see that! Why?", CONTENT);
							return true;
						case 4:
							d.sendPlayerChat("I am on a special mission for King Narnode of the", "Gnomes.", CONTENT);
							return true;
						case 5:
							d.sendNpcChat("Narnode? I don't believe you. He wouldn't send a", "human!", CONTENT);
							return true;
						case 6:
							d.sendPlayerChat("Well he did...", CONTENT);
							return true;
						case 7:
							d.sendNpcChat("Tough.", CONTENT);
							return true;
						case 8:
							if (player.getInventory().playerHasItem(ROYAL_SEAL)) {
								d.sendPlayerChat("Look, I have the Gnome Royal Seal.", CONTENT);
								return true;
							} else {
								d.sendPlayerChat("Fine then...", CONTENT);
								d.endDialogue();
								return true;
							}
						case 9:
							d.sendGiveItemNpc("You show the shipyard worker the Royal Seal.", new Item(ROYAL_SEAL));
							return true;
						case 10:
							d.sendNpcChat("Wow. I haven't seen one of these since...", CONTENT);
							return true;
						case 11:
							d.sendPlayerChat("Since when?", CONTENT);
							return true;
						case 12:
							d.sendNpcChat("Anyway. Please step inside, sir.", CONTENT);
							player.getMMVars().setOpenGate(true);
							d.endDialogue();
							return true;
					}
				}
				return false;
			case MONKEY_CHILD:
				if (player.getEquipment().getId(Constants.AMULET) != MONKEYSPEAK_AMULET) {
					d.sendNpcChat("Ook. Ook.", CONTENT);
					d.endDialogue();
					return true;
				} else if (!player.getMMVars().spokenToMonkeyChild() && !player.getMMVars().givenMonkeyChildBananas() && !player.getMMVars().monkeyChildHasToy()) {
					switch (d.getChatId()) {
						case 1:
							d.sendNpcChat("You look a lot bigger than last time", "I saw you, Uncle!", CONTENT);
							return true;
						case 2:
							d.sendPlayerChat("I've uh, been... eating more bananas... ", "just like you should be!", CONTENT);
							return true;
						case 3:
							d.sendNpcChat("I'm bored.", CONTENT);
							return true;
						case 4:
							d.sendPlayerChat("Why are you bored?", CONTENT);
							return true;
						case 5:
							d.sendNpcChat("Aunty told me to pick loads of bananas. She said if, I", "got bananas she'd give me a new toy!", CONTENT);
							return true;
						case 6:
							d.sendOption("What kind of toy did Aunty say she'd give you?", "How many bananas did Aunty want?");
							return true;
						case 7:
							switch (optionId) {
								case 1:
									d.sendPlayerChat("What kind of toy did Aunty say she'd give you?", CONTENT);
									return true;
								case 2:
									d.sendPlayerChat("How many bananas did Aunty want?", CONTENT);
									d.setNextChatId(9);
									return true;
							}
							return false;
						case 8:
							d.sendNpcChat("Aunty said it was some kind of Monkey Talisman!", "I wonder what it looks like...", CONTENT);
							d.setNextChatId(6);
							return true;
						case 9:
							d.sendNpcChat("Twenty! But I can't count! It's very mean of her, isn't", "it uncle?", CONTENT);
							return true;
						case 10:
							d.sendPlayerChat("Yes, very mean... do you want me to get the bananas", "for you?", CONTENT);
							return true;
						case 11:
							d.sendNpcChat("Ok! Ook Ook!", CONTENT);
							return true;
						case 12:
							d.sendPlayerChat("But only if you promise to let me borrow your toy...", CONTENT);
							return true;
						case 13:
							d.sendNpcChat("Ok Uncle!", CONTENT);
							player.getMMVars().setSpokenToMonkeyChild(true);
							d.endDialogue();
							return true;
					}
				} else if (player.getMMVars().spokenToMonkeyChild() && !player.getMMVars().givenMonkeyChildBananas() && !player.getMMVars().monkeyChildHasToy()) {
					switch (d.getChatId()) {
						case 1:
							d.sendNpcChat("Did you get any bananas, Uncle?", CONTENT);
							return true;
						case 2:
							int amount = player.getInventory().getItemAmount(BANANA);
							if (amount >= 5) {
								d.sendPlayerChat("Yes, I have some here.", CONTENT);
								player.getInventory().removeItem(new Item(BANANA, amount));
								return true;
							} else if (amount > 0 && amount < 5) {
								d.sendPlayerChat("I have some bananas here for you.", CONTENT);
								d.setNextChatId(6);
								return true;

							} else {
								d.sendPlayerChat("Not yet, I'm afraid.", CONTENT);
								d.endDialogue();
								return true;
							}
						case 3:
							d.sendNpcChat("Wow, that's a lot of bananas! Are there twenty?", CONTENT);
							return true;
						case 4:
							d.sendPlayerChat("Yes, of course there are.", CONTENT);
							return true;
						case 5:
							d.sendNpcChat("Aunty will be so happy!", CONTENT);
							player.getMMVars().setGivenMonkeyChildBananas(true);
							d.endDialogue();
							return true;
						case 6:
							d.sendNpcChat("That doesn't look like twenty bananas Uncle! I", "need more bananas!", DISTRESSED);
							return true;
						case 7:
							d.sendStatement("The child looks on the verge of tears.");
							return true;
						case 8:
							d.sendPlayerChat("N-no, don't cry! I'll get some more!", DISTRESSED);
							return true;
						case 9:
							d.sendNpcChat("Yay! Thanks Uncle!", HAPPY);
							d.endDialogue();
							return true;
					}
				} else if (player.getMMVars().spokenToMonkeyChild() && player.getMMVars().givenMonkeyChildBananas() && !player.getMMVars().monkeyChildHasToy()) {
					switch (d.getChatId()) {
						case 1:
							d.sendPlayerChat("Has Aunty given you the toy yet?", CONTENT);
							return true;
						case 2:
							d.sendNpcChat("No, not yet... she said she's too busy dealing with", "something called humans.", CONTENT);
							d.endDialogue();
							CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
								@Override
								public void execute(CycleEventContainer b) {
									b.stop();
								}

								@Override
								public void stop() {
									player.getMMVars().setMonkeyChildHasToy(true);
								}
							}, 120);
							return true;
					}
				} else if (player.getMMVars().givenMonkeyChildBananas() && player.getMMVars().monkeyChildHasToy() && !player.getInventory().ownsItem(MONKEY_TALISMAN)) {
					switch (d.getChatId()) {
						case 1:
							d.sendPlayerChat("Has Aunty given you the toy yet?", CONTENT);
							return true;
						case 2:
							d.sendNpcChat("Yeah, it's really neat!", CONTENT);
							return true;
						case 3:
							d.sendPlayerChat("Can I borrow it now then?", CONTENT);
							return true;
						case 4:
							d.sendNpcChat("But I only just got it!", CONTENT);
							return true;
						case 5:
							d.sendPlayerChat("Please?", CONTENT);
							return true;
						case 6:
							d.sendNpcChat("Ok then...", CONTENT);
							return true;
						case 7:
							d.sendGiveItemNpc("The monkey child gives you some kind of talisman.", new Item(MONKEY_TALISMAN));
							player.getInventory().addItemOrDrop(new Item(MONKEY_TALISMAN));
							d.endDialogue();
							return true;
					}
				}
				return false;
			case KRUK:
				if (!player.getMMVars().isMonkey()) {
					d.sendNpcChat("Ook.  Ook.", CONTENT);
					d.endDialogue();
					return true;
				}
				switch (player.getQuestStage(36)) {
					case GARKORS_PLAN:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello?", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("What brings you up here, monkey?", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("I have come to seek audience with Awowogei.", "I am told I need your permission to do so.", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("That's right, you do. What business have you on our", "island?", CONTENT);
								return true;
							case 5:
								d.sendPlayerChat("I am an envoy from the monkeys of Karamja.", "I have come to propose an alliance.", CONTENT);
								return true;
							case 6:
								d.sendNpcChat("I see. Very well, you look genuine enough. Follow me.", CONTENT);
								return true;
							case 7:
								player.fadeTeleport(AWOWOGEIS_PALACE);
								player.setQuestStage(36, KRUKS_PERMISSION);
								d.endDialogue();
								return true;
						}
						return false;
				}
				return false;
			case MONKEY_MINDER:
				if (player.getQuestStage(36) >= AWOWOGEIS_TEST) {
					if (!player.getInventory().playerHasItem(MONKEY_ITEM) && player.getMMVars().isMonkey() && player.getPosition().getX() >= 2607) {
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Ook. Ook.", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Why do you monkeys keep trying to escape? Good", "thing I've caught you before you got away, you little", "scoundrel.", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("Ook!", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("Let's put you back in your cage where you belong...", CONTENT);
								return true;
							case 5:
								d.sendPlayerChat("Ok!", CONTENT);
								return true;
							case 6:
								d.sendNpcChat("What??", CONTENT);
								return true;
							case 7:
								d.sendPlayerChat("Err... Ook?", CONTENT);
								return true;
							case 8:
								d.sendNpcChat("I must be imagining things... monkeys can't talk.", CONTENT);
								return true;
							case 9:
								player.fadeTeleport(INSIDE_CAGE);
								if (player.getQuestStage(36) == AWOWOGEIS_TEST) {
									player.setQuestStage(36, INTO_THE_CAGE);
								}
								d.endDialogue();
								return true;
						}
						return false;
					} else if (player.getInventory().playerHasItem(MONKEY_ITEM) && !player.getMMVars().isMonkey() && player.getPosition().getX() <= 2606) {
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("My word, what are you doing in there?", CONTENT);
								return true;
							case 2:
								d.sendPlayerChat("I... er... I don't know! One minute I was asleep, and", "the next minute I was here surrounded by monkeys!", CONTENT);
								return true;
							case 3:
								d.sendNpcChat("Well, don't worry. we'll have you out of there shortly.", CONTENT);
								return true;
							case 4:
								d.sendPlayerChat("Thank you.", CONTENT);
								return true;
							case 5:
								d.sendNpcChat("No problem.", CONTENT);
								return true;
							case 6:
								player.fadeTeleport(OUTSIDE_CAGE);
								if (player.getQuestStage(36) == INTO_THE_CAGE) {
									player.setQuestStage(36, THE_GREAT_ESCAPE);
								}
								d.endDialogue();
								return true;
						}
						return false;
					}
				}
				return false;
			case MONKEY:
				if (player.getEquipment().getId(Constants.AMULET) != MONKEYSPEAK_AMULET) {
					d.sendNpcChat("Ook. Ook.", CONTENT);
					d.endDialogue();
					return true;
				}
				switch (player.getQuestStage(36)) {
					case INTO_THE_CAGE:
					case THE_GREAT_ESCAPE:
						switch (d.getChatId()) {
							case 1:
								if (player.getInventory().ownsItem(MONKEY_ITEM)) {
									d.sendNpcChat("AAH AAH!", ANGRY_2);
									d.endDialogue();
								} else {
									d.sendPlayerChat("Hello there, little monkey.", CONTENT);
								}
								return true;
							case 2:
								d.sendNpcChat("Hello there!", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("How would you like to get out of here?", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("Escape!? It's all I ever think about!", HAPPY);
								return true;
							case 5:
								d.sendPlayerChat("That's convenient. When would you like to leave?", CONTENT);
								return true;
							case 6:
								d.sendNpcChat("Where will you be taking me?", CONTENT);
								return true;
							case 7:
								d.sendPlayerChat("Erm... to the happy, sunny jungle of Karamja.", CONTENT);
								return true;
							case 8:
								d.sendNpcChat("Wowee! I was born there you know!", HAPPY);
								return true;
							case 9:
								d.sendPlayerChat("That's nice. Are you ready to go?", CONTENT);
								return true;
							case 10:
								d.sendNpcChat("Yes. Actually, can I bring some of my friends?", CONTENT);
								return true;
							case 11:
								d.sendPlayerChat("No, I only have space for one.", CONTENT);
								return true;
							case 12:
								d.sendNpcChat("Pleeeeeeeease?", CONTENT);
								return true;
							case 13:
								d.sendPlayerChat("No!", CONTENT);
								return true;
							case 14:
								d.sendNpcChat("Pretty pleeeeeeeease?", CONTENT);
								return true;
							case 15:
								d.sendPlayerChat("NO!!", CONTENT);
								return true;
							case 16:
								d.sendNpcChat("Pretty please, with a banana on top?", CONTENT);
								return true;
							case 17:
								if (player.getInventory().getItemContainer().freeSlots() > 0) {
									d.sendPlayerChat("Look, I already said no. If you want to come then", "jump into my backpack.", CONTENT);
								} else {
									d.sendPlayerChat("Look, I already said no. Just let me make room", "in my backpack and then you can come.", CONTENT);
									d.endDialogue();
								}
								return true;
							case 18:
								d.sendNpcChat("Ook!", CONTENT);
								return true;
							case 19:
								d.endDialogue();
								player.getActionSender().removeInterfaces();
								for (Npc npc : World.getNpcs()) {
									if (npc != null && npc.getNpcId() == MONKEY) {
										CombatManager.startDeath(npc);
									}
								}
								player.setStopPacket(true);
								CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
									@Override
									public void execute(CycleEventContainer b) {

										b.stop();
									}

									@Override
									public void stop() {
										player.setStopPacket(false);
										player.getActionSender().sendMessage("The monkey hops into your backpack.");
										player.getInventory().addItem(new Item(MONKEY_ITEM));
									}
								}, 6);
						}
						return false;
				}
				return false;
			case GARKOR_2:
				if (player.getQuestStage(36) >= FUCK_THE_DEMON) {
					switch (d.getChatId()) {
						case 1:
							d.sendNpcChat("Well done, human! That was a most impressive display", "of skill.", CONTENT);
							return true;
						case 2:
							d.sendPlayerChat("Thank you.", CONTENT);
							return true;
						case 3:
							d.sendNpcChat("You should report to King Narnode immediately. Tell", "him that the 10th Squad still survives and has suffered", "no casualties.", CONTENT);
							return true;
						case 4:
							d.sendPlayerChat("Rest assured, I will.", CONTENT);
							return true;
						case 5:
							d.sendPlayerChat("How do I leave this place?", CONTENT);
							return true;
						case 6:
							d.sendNpcChat("Speak to Zooknock. He will arrange for you to leave.", CONTENT);
							d.endDialogue();
							player.getMMVars().spokeToGarkorEndOfFight = true;
							for (Npc n : player.getMMVars().getFinalFightNpcs()) {
								if (n != null && n.getNpcId() == ZOOKNOCK_2) {
									player.getActionSender().createPlayerHints(1, n.getIndex());
								}
							}
							return true;
					}
				}
				return false;
			case ZOOKNOCK_2:
				if (player.getQuestStage(36) >= FUCK_THE_DEMON) {
					if (player.getMMVars().spokeToGarkorEndOfFight) {
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Well done, human. Bear with me now.", CONTENT);
								return true;
							case 2:
								player.fadeTeleport(CRASH_ISLAND);
								player.getActionSender().createPlayerHints(1, -1);
								d.endDialogue();
								return true;
						}
					} else {
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Outstanding job adventurer.", HAPPY);
								d.endDialogue();
								return true;
						}
					}
				}
				return false;
			case ELDER_GUARD_1:
			case ELDER_GUARD_2:
				if (!player.getMMVars().isMonkey()) {
					ApeAtoll.jail(player, false);
					return false;
				}
				if (player.getEquipment().getId(Constants.AMULET) != MONKEYSPEAK_AMULET) {
					d.sendNpcChat("Ook... ook.", ANGRY_1);
					d.endDialogue();
					return true;
				}
				if (player.getQuestStage(36) >= KRUKS_PERMISSION) {
					switch (d.getChatId()) {
						case 1:
							d.sendNpcChat("Grr... What do you want?", ANGRY_1);
							return true;
						case 2:
							if (id == 1461) {
								if (player.getPosition().getY() < 2759) {
									d.sendPlayerChat("I'd like to speak with Awowogei, please.", CONTENT);
								} else {
									d.sendPlayerChat("I would like to leave now.", CONTENT);
								}
							} else {
								if (player.getPosition().getY() < 2762) {
									d.sendPlayerChat("I'd like to speak with Awowogei, please.", CONTENT);
								} else {
									d.sendPlayerChat("I would like to leave now.", CONTENT);
								}
							}
							return true;
						case 3:
							d.sendNpcChat("Very well.", CONTENT);
							return true;
						case 4:
							player.getActionSender().removeInterfaces();
							Following.resetFollow(player);
							player.setInteractingEntity(null);
							if (id == 1461) {
								player.getActionSender().walkTo(player.getPosition().getX() != 2802 ? 2802 - player.getPosition().getX() : 0, player.getPosition().getY() < 2759 ? 2759 - player.getPosition().getY() : 2756 - player.getPosition().getY(), true);
							} else {
								player.getActionSender().walkTo(player.getPosition().getX() != 2799 ? 2799 - player.getPosition().getX() : 0, player.getPosition().getY() < 2762 ? 2762 - player.getPosition().getY() : 2759 - player.getPosition().getY(), true);
							}
							d.endDialogue();
							return true;
					}
					return false;
				} else {
					switch (player.getQuestStage(36)) {
						case MONKEY_MAN:
							switch (d.getChatId()) {
								case 1:
									d.sendNpcChat("Grr... What do you want?", ANGRY_1);
									return true;
								case 2:
									d.sendPlayerChat("Nothing, sorry...", CONTENT);
									d.endDialogue();
									return true;
							}
							return false;
						case GARKORS_PLAN:
							switch (d.getChatId()) {
								case 1:
									d.sendNpcChat("Grrr... What do you want?", ANGRY_1);
									return true;
								case 2:
									d.sendPlayerChat("I'd like to speak with Awowogei, please.", CONTENT);
									return true;
								case 3:
									d.sendNpcChat("Only the Captain of the Monkey Guard or those he", "authorizes may enter this building. You will need his", "permission to enter.", CONTENT);
									return true;
								case 4:
									d.sendPlayerChat("Who is the Captain of the Monkey Guard?", CONTENT);
									return true;
								case 5:
									d.sendNpcChat("He goes by the name of Kruk.  You can find him", "atop the cliffs near the entrance to this village.", CONTENT);
									d.endDialogue();
									return true;
							}
							return false;
					}
				}
				return false;
		}
		return false;
	}

}
