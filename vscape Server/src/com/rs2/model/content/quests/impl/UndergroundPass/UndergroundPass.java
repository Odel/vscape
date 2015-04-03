package com.rs2.model.content.quests.impl.UndergroundPass;

import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.GameObjectData;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.Following;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.combat.weapon.RangedAmmo;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.dialogue.DialogueManager;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.quests.impl.Quest;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.objects.GameObjectDef;
import com.rs2.model.objects.functions.Doors;
import com.rs2.model.players.Player;
import com.rs2.model.players.container.inventory.Inventory;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.net.ActionSender;
import com.rs2.util.Misc;
import com.rs2.util.clip.ClippedPathFinder;

public class UndergroundPass implements Quest {
	public static boolean UNDERGROUND_PASS_ENABLED = false;

	public static final int questIndex = 9927;
	//Quest stages
	public static final int QUEST_STARTED = 1;
	public static final int ENTER_CAVES = 2;
	public static final int CAN_USE_WELL = 3;
	public static final int UNICORN_KILLED = 4;
	public static final int IBANS_LAIR_OPEN = 5;
	public static final int TALK_TO_WITCH = 6;
	public static final int CAT_RETURNED = 7;
	public static final int IBANS_DEMISE = 8;
	public static final int QUEST_COMPLETE = 20;

	//Items
	public static final int ROCK = 1480;
	public static final int ORB_OF_LIGHT = 1481;
	public static final int ORB_OF_LIGHT_2 = 1482;
	public static final int ORB_OF_LIGHT_3 = 1483;
	public static final int ORB_OF_LIGHT_4 = 1484;
	public static final int DAMP_CLOTH = 1485;
	public static final int PIECE_OF_RAILING = 1486;
	public static final int UNICORN_HORN = 1487;
	public static final int PALADINS_BADGE = 1488;
	public static final int PALADINS_BADGE_2 = 1489;
	public static final int PALADINS_BADGE_3 = 1490;
	public static final int WITCHS_CAT_ITEM = 1491;
	public static final int DOLL_OF_IBAN = 1492;
	public static final int OLD_JOURNAL = 1493;
	public static final int HISTORY_OF_IBAN = 1494;
	public static final int KLANKS_GAUNTLETS = 1495;
	public static final int IBANS_DOVE = 1496;
	public static final int AMULET_OF_OTHAINIAN = 1497;
	public static final int AMULET_OF_DOOMION = 1498;
	public static final int AMULET_OF_HOLTHION = 1499;
	public static final int IBANS_SHADOW = 1500;
	public static final int DWARF_BREW = 1501;
	public static final int IBANS_ASHES = 1502;

	//Positions
	//KALRAG new Position(2357, 9911, 0)
	public static final Position PASS_ENTRANCE_POS = new Position(2495, 9716, 0);
	public static final Position PASS_EXIT_POS = new Position(2435, 3314, 0);
	public static final Position DOWN_TO_DWARVES = new Position(2336, 9794, 0);
	public static final Position UP_FROM_DWARVES = new Position(2150, 4546, 1);
	public static final Position DOWN_TO_TOMB = new Position(2305, 9915, 0);
	public static final Position UP_FROM_TOMB = new Position(2113, 4729, 1);

	//Interfaces
	public static final int INTERFACE = -1;

	//Npcs
	public static final int KING_LATHAS = 364;
	public static final int KOFTIK_NO_HOOD = 972;
	public static final int KOFTIK = 973;
	public static final int KOFTIK_2 = 974;
	public static final int KOFTIK_3 = 975;
	public static final int KOFTIK_4 = 976;
	public static final int BOULDER = 986;
	public static final int SIR_JERRO = 988;
	public static final int SIR_CARL = 989;
	public static final int SIR_HARRY = 990;
	public static final int HALF_SOULLESS = 991;
	public static final int KARDIA = 992;
	public static final int WITCHS_CAT = 993;
	public static final int NILOOF = 994;
	public static final int KLANK = 995;
	public static final int KAMEN = 996;
	public static final int KALRAG = 997;
	public static final int OTHAINIAN = 998;
	public static final int DOOMION = 999;
	public static final int HOLTHION = 1000;
	public static final int DARK_MAGE = 1001;
	public static final int DISCIPLE_OF_IBAN = 1002;
	public static final int LORD_IBAN = 1003;
	/*
	979 Slave combat lvl: 0
980 Slave combat lvl: 0
981 Slave combat lvl: 0
982 Slave combat lvl: 0
983 Slave combat lvl: 0
984 Slave combat lvl: 0
985 Slave combat lvl: 0
	*/

	//Objects
	public static final int PASS_ENTRANCE = 3213;
	public static final int PASS_EXIT = 3214;
	public static final int GUIDE_ROPE = 3340;
	
	public static final String[] ibanWhispers = {"Blood, pain and hate.", "Death is only the beginning.", "Kill, maim... murder.", "I'll swallow your soul.", "The power of the gods could be yours.", "Hear me...", "Iban will save you... He'll save us all.", "I will release you...", "Make them all pay!", "Join us!", "I see you adventurer... you can't hide."};
	public static final String[] oldJournalStrings = {"I came to cleanse these", "mountain passes of the", "dark forces that dwell", "here. I knew my journey", "would be treacherous, so", "I deposited Spheres of", "Light in some of the", "tunnels. These spheres", "are a beacon of safety", "for all who come. The", "spheres were created by", "Saradominist mages.", "When held they boost our", "faith and courage. I still", "feel...", "", "Iban relentlessly", "tugging...", "", "at my weak soul.......", "", "bringing out any innate", "goodness to one's heart,", "illuminating the dark", "caverns with the light of", "Saradomin, bringing fear", "and pain to all who", "embrace the dark side.", "My men are still repelled", "by 'Iban's will' - it seems", "as if their pure hearts bar", "them from entering", "Iban's realm. My turn", "has come. I dare not", "admit it to my loyal men,", "but I fear for the welfare", "of my soul." };
	public static final String[] diaryOfRandasStrings = {"It began as a whisper in", "my ears. Dismissing the", "sounds as the whistling of", "the wind I steeled myself", "against these forces, and", "continued on my way.", "", "But then the whispers became", "moans...", "", "", "At once fearsome and", "enticing like the call of", "some beautiful siren.", "", "", "Join us!", "", "Our greatness lies within", "you, but only Zamorak", "can unlock your", "potential..."};
	//History of Iban strings
	public static final String[] introductionStrings = {"Introduction:", "", "Gather round, all ye", "followers of the dark arts.", "Read carefully the words", "that I hearby inscribe, as", "I detail the heady brew", "that is responsible for my", "greatest creation in all", "my time on this world. I", "am Kardia, the most", "wretched witch in the", "land; scorned by beauty,", "the world and its", "inhabitants, see what I", "created: The most", "fearsome and powerful", "force of darkness the like", "of which has never before", "been seen in this world,", "in human form..."};
	public static final String[] ibanStrings = {"The History of Iban:", "", "Iban was a Black Knight", "who had learned to fight", "under the great", "Daquarius, Lord of the", "Black Knights. Together", "they had taken on the", "might of the White and", "the blood of a hunded", "soldiers had been wiped", "from the sword of Iban.", "Iban was not quite so", "different from those who", "tasted his blade: noble and", "educated, with a taste for", "the finer things available", "in life. But there was", "something that made him", "different: Ambition. This", "hunger for more went", "far past the realm of",
						"mere mortals, into the", "shadowy places of", "darkness and evil. Iban's", "ambition was almost", "godlike in its insatiability,", "but therein lay the", "essence of his darkess:", "at its most base Iban's", "fundamental desire was to", "control the hearts and", "minds of his fellow men,", "to take them beyond the", "pale of mere allegiance", "and corrupt them into a", "force for evil. A whole", "legion of these Soul-less", "beings, their minds", "demented from the sheer", "power of darkness that", "channelled through them...", "Zombies, void of emotions,", "without feelings or cares,",
						"servants to their wicked", "master even unto death...", "But dreams were all they", "ever were. As a mere", "mortal, heroic though he", "was, this ambition Iban", "was unable to achieve.", "Meeting his demise in the", "White Knights' now", "famous Dawn Ascent,", "Iban died with the bitter", "taste of failure in his", "mouth. Little did he know", "that death was only just", "the beginning..."};
	public static final String[] resurrectionStrings = {"Iban's resurrection:", "I knew of Iban's life,", "though of course I had", "not met him. Using the", "power of my dark", "practices, I vowed to", "resurrect this greatest of", "warriors. I would raise", "him again to fulfill the", "promise of his human life.", "", "To be a master...", "...of the undead..."};
	public static final String[] fleshStrings = {"Flesh:", "Taking a small doll with", "the likeness of Iban I", "smeared my effigy with", "the four elements that", "together bring existence", "into being. Essence of his", "darkness. At the battlefield", "where Iban lay, I had", "been able to steal a piece", "of Iban's cold flesh.", "Clasping some in my", "hand, I smeared it over", "the figure of Iban, and", "chanted his name with", "mighty incantation."};
	public static final String[] bloodStrings = {"Blood:", "I also needed blood, the", "giver of life force. By", "now Iban's body was but", "a hardened vessel, the", "blood drained empty. But", "these caverns are home", "to the giant spider, a", "venomous creature", "known to feed on the", "warm blood of humans. I", "found and killed one of", "these foul beasts, and", "wiped the blood from its", "vile body onto the effigy", "of Iban that I had", "fashioned."};
	public static final String[] shadowStrings = {"Shadow:", "Then came the hard part,", "recreating the parts of a", "man that cannot be seen", "or touched: Those", "intagible things that are", "the essence of life itself.", "Using mystical forces and", "under terrible strain, I", "performed the ancient", "ritual of Incantia, an", "undertaking so dark, and", "so powerful, that the life", "was nearly stolen from", "my body. When I", "recovered, I saw three", "Demons summoned,", "standing in a triangle,", "their energy focused on", "the doll of Iban. These", "Demons were the keepers", "of Iban's shadow, forever", "bound to him..."};
	public static final String[] conscienceStrings = {"Conscience:", "Finally, I had to make", "the most unique thing,", "the one element that", "separates man from all", "other beasts - his", "Conscience. A Zombie has", "no mind: a creature", "borne of bloodlust,", "destruction. But for all", "Iban's life he chose to", "take the path of darkness,", "the road to evil. Driven", "by this unholy ambition,", "his potential grew and", "now I could harness the", "residue of his existence,", "that remained trapped in", "the dark places, to the", "fullest.", "Locked inside an old", "wooden cage sat a",
							"beautiful white dove. A", "symbol of peace, freedom", "and hope, but also", "oblivious to the darkness", "of the world, like a", "newborn child. Taking the", "dove with me, I cradled", "the thing in my arms,", "stroking its soft downy", "feathers. I looked into the", "eyes of the bird, and", "gently placing a kiss upon", "its fragile head, I then", "strangled the bird, taking", "its life between my callous", "fingers. Truly this bird", "would be the conscience", "of Iban: innocence", "corrupted by evil...", "Taking crushed bones", "from the dove's body, I", "cast my mind's eye onto",
							"the body of Iban. My", "ritual was complete, soon", "he would come again", "renewed with life. I,", "Kardia, had done the", "unimagineable: Iban was", "resurrected, the most", "powerful evil to take", "human form. I alone", "knew that the same", "process that I had used", "to resurrect the soul of", "Iban could be used to", "destroy that very same", "evil. But now I was tired,", "as I closed my eyes, I", "was contented by the", "thought of the evil to be", "unleashed..."};
	private int reward[][] = { //{itemId, count},
	};

	private int expReward[][] = {
		{Skill.AGILITY, 3000},
		{Skill.ATTACK, 3000}
	};

	private static final int questPointReward = 5;

	public int getQuestID() { //Change
		return 44;
	}

	public String getQuestName() { //Change
		return "Underground Pass";
	}

	public String getQuestSaveName() { //Change
		return "undergroundpass";
	}

	public boolean canDoQuest(final Player player) {
		return false;
	}

	public void getReward(final Player player) {
		for (int[] rewards : reward) {
			player.getInventory().addItemOrDrop(new Item(rewards[0], rewards[1]));
		}
		for (final int[] expRewards : expReward) {
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer b) {
					b.stop();
				}

				@Override
				public void stop() {
					player.getSkill().addExp(expRewards[0], (expRewards[1]));
				}
			}, 4);
		}
		player.addQuestPoints(questPointReward);
		player.getActionSender().QPEdit(player.getQuestPoints());
	}

	public void completeQuest(Player player) {
		getReward(player);
		player.getActionSender().sendInterface(12140);
		player.getActionSender().sendItemOnInterface(12145, 250, DOLL_OF_IBAN); //zoom, then itemId
		player.getActionSender().sendString("You have completed " + getQuestName() + "!", 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("5 Quest Points", 12150);
		player.getActionSender().sendString("6750 Agility XP", 12151);
		player.getActionSender().sendString("6750 Attack XP", 12152);
		player.getActionSender().sendString("Iban Staff", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), questIndex);
	}

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		int lastIndex = 0;
		switch(questStage) { 
			case QUEST_STARTED:
				lastIndex = 4;
				break;
			case TALK_TO_WITCH:
				lastIndex = 19;
				break;
			case CAT_RETURNED:
				lastIndex = 22;
				break;
			case IBANS_DEMISE:
				lastIndex = 25;
				break;
			case QUEST_COMPLETE:
				lastIndex = 30;
				break;
		}
		lastIndex++;

		ActionSender a = player.getActionSender();
		a.sendQuestLogString("Talk to King Lathas in the Ardougne Castle to begin.", 1, this.getQuestID(), 0);
		a.sendQuestLogString("King Lathas has sent me on a mission to help explore", 3, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("this underground pass to the west.", 4, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("I met Koftik. He fears these caves, but he agreed to", 6, this.getQuestID(), ENTER_CAVES);
		a.sendQuestLogString("help. He said to meet him at the bridge inside the caves.", 7, this.getQuestID(), ENTER_CAVES);
		a.sendQuestLogString("I made it farther into the caverns. I climbed down a well", 9, this.getQuestID(), CAN_USE_WELL);
		a.sendQuestLogString("that seemed to 'accept' me only after I destroyed some", 10, this.getQuestID(), CAN_USE_WELL);
		a.sendQuestLogString("orbs of light that were nearby.", 11, this.getQuestID(), CAN_USE_WELL);
		a.sendQuestLogString("I squashed a unicorn with a giant boulder.", 13, this.getQuestID(), UNICORN_KILLED);
		a.sendQuestLogString("I managed to get access to Iban's inner lair. I only", 15, this.getQuestID(), IBANS_LAIR_OPEN);
		a.sendQuestLogString("had to murder a unicorn and 3 paladins to gain access.", 16, this.getQuestID(), IBANS_LAIR_OPEN);
		a.sendQuestLogString("I found some dwarves in the pass, namely Niloof. He told", 18, this.getQuestID(), TALK_TO_WITCH);
		a.sendQuestLogString("me that the only way through the pass is to defeat Iban.", 19, this.getQuestID(), TALK_TO_WITCH);
		a.sendQuestLogString("The witch didn't want to talk to me, but I was able to", 21, this.getQuestID(), CAT_RETURNED);
		a.sendQuestLogString("find and return her cat which kept her occupied.", 22, this.getQuestID(), CAT_RETURNED);
		a.sendQuestLogString("I found an effigy in the likeness of Iban and an old", 24, this.getQuestID(), IBANS_DEMISE);
		a.sendQuestLogString("journal written by the witch.", 25, this.getQuestID(), IBANS_DEMISE);
		switch (questStage) {
			default:
				break;
			case 0:
				a.sendQuestLogString("Talk to @dre@King Lathas @bla@in the @dre@Ardougne Castle @bla@to begin.", 1);
				a.sendQuestLogString("@dre@Requirements:", 3);
				a.sendQuestLogString((QuestHandler.questCompleted(player, QuestHandler.getQuestId("Biohazard")) ? "@str@" : "@dbl@") + "-Biohazard", 5);
				a.sendQuestLogString((player.getSkill().getPlayerLevel(Skill.RANGED) >= 25 ? "@str@" : "@dbl@") + "-25 Range", 6);
				break;
			case QUEST_STARTED:
				a.sendQuestLogString("He told me to meet  his tracker Koftik, who is waiting", lastIndex + 1);
				a.sendQuestLogString("for me in far West Ardougne.", lastIndex + 2);
				break;
			case TALK_TO_WITCH:
				a.sendQuestLogString("He said the only person who would know how to do that", lastIndex + 1);
				a.sendQuestLogString("is Iban's confidante, a witch who lives on the platforms", lastIndex + 2);
				a.sendQuestLogString("above this area.", lastIndex + 3);
				break;
			case CAT_RETURNED:
				a.sendQuestLogString("I should search for something that will help me defeat", lastIndex + 1);
				a.sendQuestLogString("Iban.", lastIndex + 2);
				break;
			case IBANS_DEMISE:
				a.sendQuestLogString("I need to figure out how I can render Iban able to", lastIndex + 1);
				a.sendQuestLogString("be defeated.", lastIndex + 2);
				break;
			case QUEST_COMPLETE:
				a.sendQuestLogString("@red@" + "You have completed this quest!", lastIndex + 1);
				break;
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), questIndex);
	}

	public boolean questCompleted(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if (questStage >= QUEST_COMPLETE) {
			return true;
		}
		return false;
	}

	public void sendQuestTabStatus(Player player) {
		if (UNDERGROUND_PASS_ENABLED) {
			int questStage = player.getQuestStage(getQuestID());
			if ((questStage >= QUEST_STARTED) && (questStage < QUEST_COMPLETE)) {
				player.getActionSender().sendString("@yel@" + getQuestName(), questIndex);
			} else if (questStage == QUEST_COMPLETE) {
				player.getActionSender().sendString("@gre@" + getQuestName(), questIndex);
			} else {
				player.getActionSender().sendString("@red@" + getQuestName(), questIndex);
			}
		}
	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
		player.getActionSender().sendString(getQuestName(), 8144);
	}
	
	
	
	public static void doLoginChecks(final Player player) {
		if(player.Area(2364, 2414, 9586, 9613)) {
			player.getActionSender().sendMapState(2);
		}
		if(player.Area(2465, 2482, 9671, 9688)) {
			GridMazeHandler.startGridCheck(player);
		}
		if(player.Area(2378, 2465, 9665, 9700)) {
			PassTrapHandling.startTrapCycle(player, 0);
		}
		if(player.Area(2390, 2430, 9697, 9729) || player.Area(2367, 2390, 9663, 9728)) {
			PassTrapHandling.startTrapCycle(player, 2);
		}
		if(player.inUndergroundPass()) {
			startIbanWhispers(player);
		}
	}
	
	public static void startIbanWhispers(final Player player) {
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				if(!player.inUndergroundPass())
					b.stop();
				else
					player.getActionSender().sendMessage(ibanWhispers[Misc.randomMinusOne(ibanWhispers.length)]);
			}
			@Override
			public void stop() {
			}
		}, 120);
	}

	public boolean itemHandling(final Player player, int itemId) {
		switch (itemId) {
			case HISTORY_OF_IBAN:
				Dialogues.startDialogue(player, HISTORY_OF_IBAN + 100000);
				return true;
			case OLD_JOURNAL:
				player.getActionSender().sendMessage("The journal is old and worn.");
				player.getBookHandler().initBook(oldJournalStrings, "The Journal of Randas");
				return true;
		}
		return false;
	}

	public boolean itemOnItemHandling(final Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		if(firstItem == IBANS_SHADOW && secondItem == DOLL_OF_IBAN) {
			player.getActionSender().sendMessage("You pour the strange liquid over the doll...");
			player.setStopPacket(true);
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer b) {
					b.stop();
				}

				@Override
				public void stop() {
					player.getActionSender().sendMessage("...it seeps into the doll.");
					player.getQuestVars().setUsedIbansShadow(true);
					player.getInventory().removeItem(new Item(IBANS_SHADOW));
					player.setStopPacket(false);
				}
			}, 2);
			return true;
		}
		if(firstItem == DAMP_CLOTH && RangedAmmo.FireArrowData.dampIdForOriginalId(secondItem) != -1) {
			player.getActionSender().sendMessage("You wrap the damp cloth around the arrow head...");
			player.getInventory().replaceItemWithItem(new Item(DAMP_CLOTH), new Item(RangedAmmo.FireArrowData.dampIdForOriginalId(secondItem), 1));
			player.getInventory().removeItem(new Item(secondItem, 1));
			return true;
		} else if (secondItem == DAMP_CLOTH && RangedAmmo.FireArrowData.dampIdForOriginalId(firstItem) != -1) {
			player.getActionSender().sendMessage("You wrap the damp cloth around the arrow head...");
			player.getInventory().replaceItemWithItem(new Item(DAMP_CLOTH), new Item(RangedAmmo.FireArrowData.dampIdForOriginalId(firstItem), 1));
			player.getInventory().removeItem(new Item(firstItem, 1));
			return true;
		}
		return false;
	}

	public boolean doItemOnObject(final Player player, final int object, final int item) {
		if(RangedAmmo.FireArrowData.litIdForDampId(item) != -1) {
			final CacheObject obj = ObjectLoader.object(object, player.getClickX(), player.getClickY(), player.getPosition().getZ());
			final GameObjectDef def = SkillHandler.getObject(object, player.getClickX(),player.getClickY(), player.getPosition().getZ());
			if (obj != null || def != null) {
				String name = GameObjectData.forId(obj != null ? obj.getDef().getId() : def.getId()).getName().toLowerCase();
				if (name.equalsIgnoreCase("fire") || name.equalsIgnoreCase("fireplace")) {
					int amount = player.getInventory().getItemAmount(item);
					if (player.getInventory().playerHasItem(item, amount)) {
						player.getActionSender().sendMessage("You light the cloth wrapped arrow head" + (amount > 1 ? "s." : "."));
						player.getInventory().replaceItemWithItem(new Item(item, amount), new Item(RangedAmmo.FireArrowData.litIdForDampId(item), amount));
						return true;
					}
				}
			}
		}
		switch(object) {
			case 3270:
				if(item == WITCHS_CAT_ITEM && player.inUndergroundPass() && player.getQuestStage(this.getQuestID()) == TALK_TO_WITCH) {
					player.getUpdateFlags().sendAnimation(827);
					player.getActionSender().sendMessage("...You place the cat by the door.");
					player.getInventory().removeItem(new Item(WITCHS_CAT_ITEM));
					player.setStopPacket(true);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						int count = 0;
						@Override
						public void execute(CycleEventContainer b) {
							count++;
							switch(count) {
								case 1:
									player.getActionSender().sendMessage("You knock on the door and hide around the corner...");
									ClippedPathFinder.getPathFinder().findRoute(player, 2157, 4568, true, 0, 0);
									break;
								case 2:
									player.getActionSender().sendMessage("The Witch takes the cat inside.");
									player.setQuestStage(getQuestID(), CAT_RETURNED);
									b.stop();
									break;
							}
						}
						@Override
						public void stop() {
							player.setStopPacket(false);
						}
					}, 3);
					return true;
				}
			return false;
			case 3305:
				if(item >= UNICORN_HORN && item <= PALADINS_BADGE_3) {
					player.setStopPacket(true);
					player.getActionSender().sendMessage("You throw the " + (item == UNICORN_HORN ? "unicorn horn" : "coat of arms") + " into the flames...");
					player.getInventory().removeItem(new Item(item));
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							player.setStopPacket(false);
							player.getQuestVars().wellItemsDestroyed[item - UNICORN_HORN] = true;
							player.getActionSender().sendMessage("You hear a howl in the distance.");
							boolean canContinue = true;
							for (boolean b : player.getQuestVars().wellItemsDestroyed)
								if (!b) {
									canContinue = false;
								}
							if (canContinue) {
								player.getActionSender().sendMessage("You hear a click from nearby...");
								player.getActionSender().sendMessage("It sounds like it came from the skull above the door.");
								if(player.getQuestStage(getQuestID()) == UNICORN_KILLED)
									player.setQuestStage(getQuestID(), IBANS_LAIR_OPEN);
							}
							
						}
					}, 3);
					return true;
				}
			return false;
			case 3216: //cage mud
				if(item == 952 && player.inUndergroundPass()) { //spade
					PassObjectHandling.handleDigMud(player);
					return true;
				}
				return false;
			case 3294: //furnace
				if(item >= ORB_OF_LIGHT && item <= ORB_OF_LIGHT_4) {
					player.setStopPacket(true);
					player.getActionSender().sendMessage("You throw the glowing orb into the furnace...");
					player.getActionSender().sendMessage("Its light quickly dims and then dies.");
					player.getInventory().removeItem(new Item(item));
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							player.getActionSender().sendMessage("You feel a cold shudder run down your spine.");
							player.getQuestVars().wellItemsDestroyed[item - ORB_OF_LIGHT] = true;
							player.setStopPacket(false);
						}
					}, 3);
					return true;
				}
			return false;
			case 3230: //flat rock trap
				if(item == 960) { //plank
					PassTrapHandling.handleDisarmTrap(player, 3231, 827, null);
					return true;
				}
			return false;
			case 2275: //rock swing
			case 2276:
				if(item == 954) { //rope
					PassObjectHandling.handleRopeSwing(player, object);
					return true;
				}
			return false;
		}
		return false;
	}

	public boolean doItemOnNpc(Player player, int itemId, Npc npc) {
		if(itemId == PIECE_OF_RAILING && npc.getNpcId() == BOULDER) {
			PassObjectHandling.handlePushBoulder(player, npc);
			return true;
		}
		return false;
	}

	public boolean doNpcClicking(Player player, Npc npc) {
		int id = npc.getNpcId();
		switch(id) {
			case WITCHS_CAT:
				Dialogues.startDialogue(player, WITCHS_CAT + 100000);
				return true;
			case BOULDER:
				player.getDialogue().sendPlayerChat("It's too heavy to move with just my hands.", DISTRESSED);
				player.getDialogue().endDialogue();
				return true;
		}
		return false;
	}
	
	public boolean doNpcSecondClicking(Player player, Npc npc) {
		switch(npc.getNpcId()) {
			case WITCHS_CAT:
				Dialogues.startDialogue(player, WITCHS_CAT);
				return true;
		}
		return false;
        }
	
	public boolean doObjectClicking(final Player player, int object, final int x, final int y) {
		if(player.stopPlayerPacket()) {
			return false;
		}
		switch (object) {
			case 3274:
				if(player.inUndergroundPass() && x == 2136 && y == 4578 && player.getQuestStage(44) >= CAT_RETURNED && player.getQuestStage(44) < QUEST_COMPLETE) {
					player.getActionSender().sendMessage("You attempt to open the chest...");
					final Inventory i = player.getInventory();
					player.setStopPacket(true);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						int count = 0;
						@Override
						public void execute(CycleEventContainer b) {
							switch(++count) {
								case 1:
									if (i.playerHasItem(AMULET_OF_OTHAINIAN) && i.playerHasItem(AMULET_OF_DOOMION) && i.playerHasItem(AMULET_OF_HOLTHION)) {
										player.getActionSender().sendMessage("The three amulets glow red in your backpack...");
										player.getInventory().removeItem(new Item(AMULET_OF_OTHAINIAN));
										player.getInventory().removeItem(new Item(AMULET_OF_DOOMION));
										player.getInventory().removeItem(new Item(AMULET_OF_HOLTHION));
									} else {
										player.getActionSender().sendMessage("...it seems to sit dormant and unresponsive.");
										b.stop();
									}
									break;
								case 2:
									player.getActionSender().sendMessage("...You place them on the chest and it opens.");
									player.getActionSender().sendObject(3273, x, y, 0, 2, 10);
									break;
								case 3:
									player.getActionSender().sendMessage("Inside you find a strange dark liquid.");
									player.getInventory().addItemOrDrop(new Item(IBANS_SHADOW));
									b.stop();
									break;
							}
						}

						@Override
						public void stop() {
							player.getActionSender().sendObject(3274, x, y, 0, 2, 10);
							player.setStopPacket(false);
							
						}
					}, 3);
					
					return true;
				
				}
			return false;
			case 3272:
				if(player.inUndergroundPass() && x == 2157 && y == 4564 && player.getQuestStage(44) >= CAT_RETURNED && player.getQuestStage(44) < QUEST_COMPLETE) {
					Dialogues.startDialogue(player, 32720);
					player.getActionSender().sendObject(3273, x, y, 0, 0, 10);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							player.getActionSender().sendObject(3272, x, y, 0, 0, 10);
						}
					}, 15);
					return true;
				}
			return false;
			case 3270:
				if(player.inUndergroundPass()) {
					if(player.getQuestStage(this.getQuestID()) < CAT_RETURNED) {
						Dialogues.startDialogue(player, 32700);
					} else {
						player.getActionSender().sendMessage("You open the door...");
						boolean condition = player.getPosition().getX() >= x;
						Doors.passThroughDialogueDoor(player, 3270, x, y, condition ? -1 : 1, new Position(condition ? 2158 : 2157, 4566, 1), false);
						if(condition)
							player.getActionSender().sendMessage("The Witch is busy talking to the cat.");
					}
					return true;
				}
			return false;
			case 3223:
				if(player.inUndergroundPass()) {
					player.fadeTeleport(player.getPosition().getY() < 9800 ? UP_FROM_DWARVES : UP_FROM_TOMB);
					return true;
				}
			return false;
			case 3222:
				if(player.inUndergroundPass()) {
					player.fadeTeleport(player.getPosition().getY() < 4700 ? DOWN_TO_DWARVES : DOWN_TO_TOMB);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
							@Override
							public void execute(CycleEventContainer b) {
								b.stop();
							}

							@Override
							public void stop() {
								player.setStopPacket(false);
								if(player.getQuestStage(getQuestID()) < TALK_TO_WITCH) {
									NpcLoader.spawnNpc(player, new Npc(KOFTIK_4), false, false);
									Dialogues.startDialogue(player, KOFTIK_4);
								}
							}
						}, 6);
					return true;
				}
			return false;
			case 3305:
				player.getActionSender().sendMessage("The well appears to have flames at the bottom, instead of water...");
				return true;
			case 3308: //broken cage
				if(player.inUndergroundPass()) {
					if(!player.getInventory().playerHasItem(UNICORN_HORN) && player.getQuestStage(this.getQuestID()) == UNICORN_KILLED)
						Dialogues.startDialogue(player, 33080);
					else
						player.getDialogue().sendPlayerChat("There's nothing here but a dead unicorn...", "Poor guy...", SAD);
					return true;
				}
			return false;
			case 3217: //"cave" back to cage mud
				if(player.inUndergroundPass()) {
					player.getActionSender().sendMessage("You push your way through the tunnel...");
					player.fadeTeleport(new Position(2393, 9651, 0));
					return true;
				}
				return false;
			case 3234:
				if(player.inUndergroundPass()) {
					Dialogues.startDialogue(player, 32340);
					return true;
				}
			return false;
			case 3339:
				if(x == 2382 && y == 9668) {
					Dialogues.startDialogue(player, ORB_OF_LIGHT + 10000);
					return true;
				}
			return false;
			case GUIDE_ROPE:
				if (player.getPosition().getY() < 9718) {
					player.getActionSender().sendMessage("You can't get a clear shot from here.");
				} else {
					PassObjectHandling.shootBridgeRope(player, x, y);
				}
				return true;
			case PASS_ENTRANCE:
				if(x == 2433 && y == 3313) {
					if (UNDERGROUND_PASS_ENABLED && player.getQuestStage(this.getQuestID()) >= ENTER_CAVES) {
						player.getUpdateFlags().sendAnimation(844);
						player.fadeTeleport(PASS_ENTRANCE_POS);
						startIbanWhispers(player);
					} else {
						player.getDialogue().setLastNpcTalk(KOFTIK);
						player.getDialogue().sendNpcChat("Hey, get away from there! You'll kill yourself", "in those caves!", ANGRY_1);
						player.getDialogue().endDialogue();
					}
					return true;
				}
				return false;
			case PASS_EXIT:
				if(x == 2496 && y == 9713) {
					player.getUpdateFlags().sendAnimation(844);
					player.fadeTeleport(PASS_EXIT_POS);
					return true;
				}
				return false;
		}
		return false;
	}

	public boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
		if(player.stopPlayerPacket()) {
			return false;
		}
		switch (object) {
			case 3362:
				if(player.inUndergroundPass()) {
					player.getActionSender().sendMessage("Inside you see a witch, she appears to be looking for something.");
					return true;
				}
			return false;
			case 3270:
				if(player.inUndergroundPass()) {
					player.getActionSender().sendMessage("You knock on the door...");
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							player.getActionSender().sendMessage("There is no reply.");
						}
					}, 3);

					return true;
				}
			return false;
			case 3267:
				if (player.inUndergroundPass()) {
					player.setStopPacket(true);
					player.getActionSender().sendMessage("You search the cage...");
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							player.setStopPacket(false);
							if (!player.getInventory().ownsItem(PIECE_OF_RAILING)) {
								player.getActionSender().sendMessage("You find a loose railing on the floor.");
								player.getInventory().addItem(new Item(PIECE_OF_RAILING));
							} else {
								player.getActionSender().sendMessage("You don't find anything interesting.");
							}
						}
					}, 3);
					return true;
				}
				break;
		}
		return false;
	}

	public void handleDeath(final Player player, final Npc died) {
		switch(died.getNpcId()) {
			case OTHAINIAN:
			case DOOMION:
			case HOLTHION:
				if(player.getQuestStage(44) == IBANS_DEMISE) {
					GroundItemManager.getManager().dropItem(new GroundItem(new Item(AMULET_OF_OTHAINIAN + (died.getNpcId() - OTHAINIAN)), player, died.getPosition()));
				}
				break;
			case SIR_JERRO:
			case SIR_CARL:
			case SIR_HARRY:
				if (player.getQuestStage(this.getQuestID()) < IBANS_LAIR_OPEN) {
					int itemId = died.getNpcId() == SIR_JERRO ? PALADINS_BADGE : died.getNpcId() == SIR_CARL ? PALADINS_BADGE_2 : PALADINS_BADGE_3;
					GroundItemManager.getManager().dropItem(new GroundItem(new Item(itemId), player, player, died.getPosition()));
				}
				break;
		}
	}

	public boolean sendDialogue(final Player player, final int id, int chatId, int optionId, int npcChatId) {
		DialogueManager d = player.getDialogue();
		switch (id) { //Npc ID
			case 32720:
				switch (d.getChatId()) {
					case 1:
						d.sendStatement("You search the chest...");
						return true;
					case 2:
						d.sendStatement("Inside you find a book, a wooden doll and two potions.");
						return true;
					case 3:
						d.endDialogue();
						player.getActionSender().removeInterfaces();
						if(!player.getInventory().ownsItem(HISTORY_OF_IBAN)) {
							player.getInventory().addItemOrDrop(new Item(HISTORY_OF_IBAN));
						}
						if(!player.getInventory().ownsItem(DOLL_OF_IBAN)) {
							player.getInventory().addItemOrDrop(new Item(DOLL_OF_IBAN));
						}
						if(player.getQuestStage(44) == CAT_RETURNED) {
							player.setQuestStage(44, IBANS_DEMISE);
							player.getInventory().addItemOrDrop(new Item(145)); //super attack
							player.getInventory().addItemOrDrop(new Item(127)); //restore pot
						}
						return true;
						
				}
			return false;
			case WITCHS_CAT:
				switch (d.getChatId()) {
					case 1:
						d.sendNpcChat("Mrowww!");
						d.endDialogue();
						return true;
				}
			return false;
			case HISTORY_OF_IBAN + 100000:
				switch (d.getChatId()) {
					case 1:
						d.sendStatement("There are many chapters here, which one would you", "like to read?");
						return true;
					case 2:
						d.sendOption("Introduction.", "Iban.", "The Resurrection.", "The Four Elements.");
						return true;
					case 3:
						//book here
						switch(optionId) {
							case 1:
								player.getBookHandler().initBook(introductionStrings, "Introduction");
								break;
							case 2:
								player.getBookHandler().initBook(ibanStrings, "The History of Iban");
								break;
							case 3:
								player.getBookHandler().initBook(resurrectionStrings, "Iban's Resurrection");
								break;
							case 4:
								d.sendOption("Flesh.", "Blood.", "Shadow.", "Conscience.");
								break;
						}
						if(optionId != 4)
							d.endDialogue();
						return true;
					case 4:
						switch(optionId) {
							case 1:
								player.getBookHandler().initBook(fleshStrings, "The Four Elements: Flesh");
								break;
							case 2:
								player.getBookHandler().initBook(bloodStrings, "The Four Elements: Blood");
								break;
							case 3:
								player.getBookHandler().initBook(shadowStrings, "The Four Elements: Shadow");
								break;
							case 4:
								player.getBookHandler().initBook(conscienceStrings, "The Four Elements: Conscience");
								break;
						}
						d.endDialogue();
						return true;
				}
			return false;
			case WITCHS_CAT + 100000:
				switch (d.getChatId()) {
					case 1:
						if(player.getQuestStage(this.getQuestID()) == TALK_TO_WITCH && !player.getInventory().playerHasItem(WITCHS_CAT_ITEM)) {
							Item witchCat = new Item(WITCHS_CAT_ITEM);
							if(player.getInventory().canAddItem(witchCat)) {
								player.getUpdateFlags().sendAnimation(827);
								player.getInventory().addItem(witchCat);
								player.getQuestVars().takenWitchCat = true;
								Npc cat = null;
								for(Npc npc : World.getNpcs()) {
									if(npc != null && npc.getNpcId() == WITCHS_CAT)
										cat = npc;
								}
								if(cat != null) {
									player.getNpcs().remove(cat);
									cat.setVisible(false);
									cat.getUpdateFlags().setUpdateRequired(true);
									cat.setVisible(true);
								}
							} else {
								player.getActionSender().sendMessage("You don't have enough inventory space to hold that item.");
								d.endDialogue();
							}
						} else {
							player.getActionSender().sendMessage("You have no reason to do that at the moment.");
						}
						d.endDialogue();
						return true;
				}
			return false;
			case 32700:
				d.setLastNpcTalk(KARDIA);
				switch (d.getChatId()) {
					case 1:
						d.sendNpcChat("Get away... Far away from here!", ANGRY_1);
						return true;
					case 2:
						d.endDialogue();
						player.getActionSender().removeInterfaces();
						player.hit(player.getCurrentHp()/4, HitType.NORMAL);
						return true;
				}
			return false;
			case KAMEN:
				switch (d.getChatId()) {
					case 1:
						player.setStopPacket(true);
						player.getActionSender().sendMessage("He looks a little drunk.");
						final Npc kamen = World.getNpcs()[player.getNpcClickIndex()];
						kamen.getUpdateFlags().setForceChatMessage("Hic!");
						CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
							@Override
							public void execute(CycleEventContainer b) {
								b.stop();
							}

							@Override
							public void stop() {
								player.setStopPacket(false);
								if(Misc.goodDistance(player.getPosition(), kamen.getPosition(), 1))
									Dialogues.sendDialogue(player, KAMEN, 2, 0);
							}
						}, 3);
						return true;
					case 2:
						d.sendPlayerChat("Hi there, you okay?");
						return true;
					case 3:
						d.sendNpcChat("Ooooh, my head ...I'm fried.");
						return true;
					case 4:
						d.sendPlayerChat("What's wrong?");
						return true;
					case 5:
						d.sendNpcChat("Too much of this home brew my friend. We make it", "from plant roots, but it blows your head off.");
						return true;
					case 6:
						d.sendNpcChat("You don't wanna put it near any naked flames.", "Want some?", ANGRY_1);
						return true;
					case 7:
						d.sendOption("Okay then.", "No thanks.");
						return true;
					case 8:
						d.sendPlayerChat(d.tempStrings[optionId - 1]);
						if (optionId == 2) {
							d.setNextChatId(15);
						}
						return true;
					case 9:
						d.sendNpcChat("Here you go... Hic!");
						return true;
					case 10:
						d.endDialogue();
						player.getActionSender().removeInterfaces();
						player.getActionSender().sendMessage("It tastes horrific and burns your throat.");
						player.getUpdateFlags().setForceChatMessage("Aaarrgghh!");
						player.hit(5, HitType.NORMAL);
						player.getActionSender().statEdit(Skill.AGILITY, -3, false);
						player.getInventory().addItemOrDrop(new Item(2327)); //meat pie
						player.getInventory().addItemOrDrop(new Item(2003)); //stew
						player.getInventory().addItem(new Item(2309)); //bread
						return true;
					case 15:
						d.sendNpcChat("Well come back any time.", HAPPY);
						d.endDialogue();
						return true;
				}
				return false;
			case KLANK:
				switch (d.getChatId()) {
					case 1:
						d.sendPlayerChat("Hello my good man.");
						return true;
					case 2:
						d.sendNpcChat("Good day to you outsider. I'm Klank, I'm the only", "blacksmith still alive down here. In fact we're the only", "ones that haven't yet turned.");
						return true;
					case 3:
						d.sendNpcChat("If you're not careful you'll become one of them too!", DISTRESSED);
						return true;
					case 4:
						d.sendPlayerChat("Who?... Iban's followers?");
						return true;
					case 5:
						d.sendNpcChat("They're not followers, they're slaves, they're the", "Soulless...");
						return true;
					case 6:
						d.sendPlayerChat("What happened to them?");
						return true;
					case 7:
						d.sendNpcChat("They were normal once, adventurers, treasure hunters.", "But men are weak, they couldn't ignore the voices.");
						return true;
					case 8:
						d.sendNpcChat("Now they all seem to think with one conscience... As if", "they're being controlled by one being...", SAD);
						return true;
					case 9:
						d.sendPlayerChat("Iban?");
						return true;
					case 10:
						d.sendNpcChat("Maybe... maybe Zamorak himself. Those who try and", "fight it Iban locks in cages, until their minds are too", "weak to resist.");
						return true;
					case 11:
						d.sendNpcChat("Eventually they all fall to his control...", SAD);
						if (player.getInventory().playerHasItem(590)) {
							d.endDialogue();
						}
						return true;
					case 12:
						d.sendNpcChat("Here, take this, I don't need it.");
						return true;
					case 13:
						d.endDialogue();
						player.getActionSender().removeInterfaces();
						player.getActionSender().sendMessage("Klank gives you a tinderbox.");
						player.getInventory().addItemOrDrop(new Item(590));
						return true;
				}
				return false;
			case NILOOF:
				switch(player.getQuestStage(this.getQuestID())) {
					case TALK_TO_WITCH:
						switch(d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello Niloof.");
								return true;
							case 2:
								d.sendNpcChat("So you still live, not many survive down here.");
								return true;
							case 3:
								d.sendPlayerChat("As I can see.");
								return true;
							case 4:
								d.sendNpcChat("Don't stay too long traveller. Iban's calls will soon", "penetrate your delicate human mind.");
								return true;
							case 5:
								d.sendNpcChat("You'll also become one of his minions... You must go", "above and find the witch Kardia. She holds the secret to", "Iban's destruction.");
								d.endDialogue();
								return true;
							case 10:
								d.sendPlayerChat("Thanks Niloof, take care.");
								return true;
							case 11:
								d.sendNpcChat("You too.");
								return true;
						}
					case IBANS_LAIR_OPEN:
						switch(d.getChatId()) {
							case 1:
								d.sendNpcChat("Back away! Back away! ...Wait. ...You're human!", DISTRESSED);
								return true;
							case 2:
								d.sendPlayerChat("That's right, I'm on a quest for King Lathas. We need", "to find a way through these caverns.");
								return true;
							case 3:
								d.sendNpcChat("Ha ha, listen up. We came here as miners decades ago,", "completely unware of the evil that lurked here. There's", "no way through, not while Iban still rules. He controls", "the gateway, the only way to the other side.");
								return true;
							case 4:
								d.sendPlayerChat("What gateway?");
								return true;
							case 5:
								d.sendNpcChat("It once stood as the 'Well of Voyage'. A gateway to the", "West. Now Iban's moulded it into a pit of the damned,", "a portal to Zamorak's darkest realms.");
								return true;
							case 6:
								d.sendNpcChat("He sends his followers there, never to return. Only", "once Iban is destroyed can the well be restored.");
								return true;
							case 7:
								d.sendPlayerChat("But how?");
								return true;
							case 8:
								d.sendNpcChat("If I knew, I would have slain him already. Seek out the", "Witch, his guide, his only confidante. Only she knows", "how to rid us of Iban.");
								return true;
							case 9:
								d.sendNpcChat("She lives on the platforms above, we dare not go there.", "Here, take some food to aid your journey.");
								return true;
							case 10:
								d.endDialogue();
								player.getActionSender().removeInterfaces();
								player.setStopPacket(true);
								player.setQuestStage(this.getQuestID(), TALK_TO_WITCH);
								player.getActionSender().sendMessage("Niloof gives you some food...");
								player.getInventory().addItemOrDrop(new Item(2327, 2)); //meat pie
								player.getInventory().addItemOrDrop(new Item(2289)); //pizza
								CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
									@Override
									public void execute(CycleEventContainer b) {
										b.stop();
									}
									@Override
									public void stop() {
										player.setStopPacket(false);
										Dialogues.sendDialogue(player, NILOOF, 10, 0);
									}
								}, 5);
								return true;
						}
					return false;
				}
			return false;
			case SIR_CARL:
			case SIR_HARRY:
				switch (d.getChatId()) {
					case 1:
						d.sendPlayerChat("Hello paladin.");
						return true;
					case 2:
						d.sendNpcChat("Hrumph. Talk to Jerro if you need anything.");
						d.endDialogue();
						return true;
				}
			return false;
			case SIR_JERRO:
				switch (d.getChatId()) {
					case 1:
						d.sendPlayerChat("Hello paladin.");
						return true;
					case 2:
						d.sendNpcChat("Traveller, what are you doing in this most unholy", "place?");
						return true;
					case 3:
						d.sendPlayerChat("I'm looking for safe route through the caverns, under", "order of King Lathas.");
						return true;
					case 4:
						if(!player.getQuestVars().receivedPaladinFood) {
							d.sendNpcChat("You've done well to get this far traveller, here, eat...");
						} else {
							d.sendNpcChat("You've done well to get this far traveller.");
							d.endDialogue();
						}
						return true;
					case 5:
						d.sendPlayerChat("Great, thanks a lot.");
						d.endDialogue();
						player.getActionSender().sendMessage("The Paladin gives you some food.");
						player.getQuestVars().receivedPaladinFood = true;
						player.getInventory().addItemOrDrop(new Item(2327)); //meat pie
						player.getInventory().addItemOrDrop(new Item(2003)); //stew
						player.getInventory().addItem(new Item(2309, 2)); //bread
						return true;
						
				}
			return false;
			case 33080:
				switch (d.getChatId()) {
					case 1:
						d.sendPlayerChat("All that remains is a damaged horn.", DISTRESSED);
						return true;
					case 2:
						d.endDialogue();
						player.getActionSender().removeInterfaces();
						player.getActionSender().sendMessage("The unicorn was killed by the boulder.");
						if(!player.getInventory().playerHasItem(UNICORN_HORN))
							player.getInventory().addItem(new Item(UNICORN_HORN));
						return true;
				}
			return false;
			case 32340:
				switch (d.getChatId()) {
					case 1:
						d.sendStatement("The markings appear to be holes in the wall... It's a trap!");
						return true;
					case 2:
						d.sendOption("Try and disarm the trap.", "Leave it alone.");
						return true;
					case 3:
						player.getActionSender().removeInterfaces();
						d.endDialogue();
						if (optionId == 1) {
							PassTrapHandling.handleDisarmTrap(player, 3234, 2246, null);
						}
						return true;	
				}
			return false;
			case ORB_OF_LIGHT + 10000:
				switch (d.getChatId()) {
					case 1:
						d.sendStatement("The rock appears to move slightly as you touch it... It's a trap!");
						return true;
					case 2:
						d.sendOption("Try and disarm the trap.", "Leave it alone.");
						return true;
					case 3:
						player.getActionSender().removeInterfaces();
						d.endDialogue();
						if (optionId == 1) {
							PassTrapHandling.handleDisarmTrap(player, 3361, 2244, null);
						}
						return true;	
				}
			return false;
			case KOFTIK_4:
				switch (d.getChatId()) {
					case 1:
						d.sendNpcChat("Traveller is that you?.. my friend on a mission!", DISTRESSED);
						return true;
					case 2:
						d.sendPlayerChat("Koftik, you're still here, you should leave.");
						return true;
					case 3:
						d.sendNpcChat("Leave?... leave?.. this is my home now. Home with", "my lord, he talks to me, he's my friend.", Dialogues.CALM);
						return true;
					case 4:
						d.sendStatement("Koftik seems to be in a weak state of mind.");
						return true;
					case 5:
						d.sendPlayerChat("Koftik you really should leave these caverns.");
						return true;
					case 6:
						d.sendNpcChat("Now now, we're all the same down here. There's just", "you and those Dwarves left to be converted.", ANGRY_1);
						return true;
					case 7:
						d.sendPlayerChat("Dwarves?");
						return true;
					case 8:
						d.sendNpcChat("Foolish Dwarves, still believing that they can resist. No", "one resists Iban, go traveller.", ANGRY_1);
						return true;
					case 9:
						d.sendNpcChat("The Dwarves to the south, they're not safe in the south!", DISTRESSED);
						return true;
					case 10:
						d.sendNpcChat("We'll show them, go slay them m'lord. He'll be so proud,", "that's all I want.", ANGRY_1);
						return true;
					case 11:
						d.sendPlayerChat("I'll pray for you.");
						d.endDialogue();
						Npc koftik = player.getSpawnedNpc();
						Following.resetFollow(koftik);
						koftik.setFollowDistance(20);
						koftik.setFollowingEntity(player);
						return true;		
				}
			return false;
			case KOFTIK_3:
				switch (d.getChatId()) {
					case 1:
						d.sendPlayerChat("Hello Koftik.");
						return true;
					case 2:
						d.sendNpcChat("Do you hear them? The voices tell me things.");
						return true;
					case 3:
						d.sendPlayerChat("Are you okay?");
						return true;
					case 4:
						d.sendNpcChat("The path of the righteous man is beset on all sides by", "the iniquities of the selfish and the tyranny of evil men.");
						return true;
					case 5:
						d.sendPlayerChat("Tyranny of the righteous? What?");
						return true;
					case 6:
						d.sendNpcChat("So many paths to choose... here we must all take our", "own path.");
						d.endDialogue();
						return true;
				}
			return false;
			case KOFTIK_2:
				switch (d.getChatId()) {
					case 1:
						d.sendPlayerChat("Koftik, how can we cross the bridge?");
						return true;
					case 2:
						d.sendNpcChat("I'm not sure, seems as if others were here before us", "though...");
						return true;
					case 3:
						d.sendNpcChat("I found this cloth amongst the charred remains", "of some arrows.");
						player.getInventory().addItem(new Item(DAMP_CLOTH));
						return true;
					case 4:
						d.sendPlayerChat("Charred arrows? They must have been trying to burn", "something. Or someone!");
						return true;
					case 5:
						d.sendPlayerChat("Interesting... we better keep our eyes open.");
						return true;
					case 6:
						d.sendNpcChat("I have also found the remains of a book...", SAD);
						return true;
					case 7:
						d.sendPlayerChat("What does it say?");
						return true;
					case 8:
						d.sendNpcChat("It seems to be written by the adventurer Randas. It", "reads...");
						return true;
					case 9:
						player.getActionSender().removeInterfaces();
						player.getActionSender().sendMessage("Koftik shows you the diary.");
						d.dontCloseInterface();
						player.getBookHandler().initBook(diaryOfRandasStrings, "The Diary of Randas");
						return true;
				}
				return false;
			case KOFTIK:
				switch (player.getQuestStage(this.getQuestID())) {
					case QUEST_STARTED:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello there, are you the King's scout?");
								return true;
							case 2:
								d.sendNpcChat("That I am, brave adventurer. King Lathas informed me", "that you need to cross these mountains.");
								return true;
							case 3:
								d.sendNpcChat("I'm afraid you'll have to go through the ancient", "underground pass...");
								return true;
							case 4:
								d.sendPlayerChat("That's ok, I've travelled through many a cave", "in my time.");
								return true;
							case 5:
								d.sendNpcChat("These caves are different... They're filled with the spirit", "of Zamorak!");
								return true;
							case 6:
								d.sendNpcChat("You can feel it as you wind your way round the", "stalagmites... an icy chill that penetrates the very fabric", "of your being...");
								return true;
							case 7:
								d.sendNpcChat("Not so many travellers come down here these days,", "...but there are some who are still foolhardy enough.");
								return true;
							case 8:
								d.sendOption("I'll take my chances.", "Tell me more...");
								return true;
							case 9:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if(optionId == 1)
									d.setNextChatId(15);
								return true;
								
							case 10:
								d.sendNpcChat("I remember seeing one such warrior. Going by the", "name of Randas... ...he stood tall and proud like an", "Elven King...");
								return true;
							case 11:
								d.sendNpcChat("...That same pride made him vulnerable to Zamorak's", "calls. Randas' worthy desire to be a great and mighty", "warrior also made him corruptible to Zamorak's promises", "of glory.");
								return true;
							case 12:
								d.sendNpcChat("...Zamorak showed him a way to achieve his goals by", "appealing to that most base and dark nature ...that", "resides in all of us.");
								return true;
							case 13:
								d.sendPlayerChat("What happened to him?", DISTRESSED);
								return true;
							case 14:
								d.sendNpcChat("No one knows...", SAD);
								return true;
							case 15:
								d.sendNpcChat("If you're willing, you'll need to meet me by", "the bridge just inside the caves. Be careful.");
								d.endDialogue();
								player.setQuestStage(this.getQuestID(), ENTER_CAVES);
								return true;	
						}
					return false;
				}
			return false;
			case KING_LATHAS:
				switch (player.getQuestStage(this.getQuestID())) {
					case QUEST_STARTED:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello King Lathas.");
								return true;
							case 2:
								d.sendNpcChat("Go meet my main tracker, Koftik. He will help you. He", "waits for you at the west side of West Ardougne.");
								return true;
							case 3:
								d.sendNpcChat("We must find a way through these caverns if we are to", "stop my brother Tyras.");
								return true;
							case 4:
								d.sendPlayerChat("I'll do my best Lathas.");
								return true;
							case 5:
								d.sendNpcChat("A warning traveller, the underground pass is lethal. We", "lost many men exploring those caverns. Go prepared", "with food and armor or you won't last long.");
								d.endDialogue();
								return true;		
						}
					return false;
					case 0:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello King Lathas.");
								return true;
							case 2:
								if(QuestHandler.questCompleted(player, 40) && UNDERGROUND_PASS_ENABLED) {
									d.sendNpcChat("Adventurer, thank Saradomin for your arrival!", HAPPY);
								} else {
									d.sendNpcChat("Shoo, peasant.", ANGRY_1);
									d.endDialogue();
								}
								return true;
							case 3:
								d.sendPlayerChat("Have your scouts found a way through the mountains?");
								return true;
							case 4:
								d.sendNpcChat("Not quite, we found a path to where we expected", "to find the Well of Voyage, an ancient portal to", "the far west.");
								return true;
							case 5:
								d.sendNpcChat("However during recent times a cluster of cultists have", "settled there, run by a madman named Iban.");
								return true;
							case 6:
								d.sendPlayerChat("Iban?");
								return true;
							case 7:
								d.sendNpcChat("A crazy loon who claims to be the son of Zamorak.");
								return true;
							case 8:
								d.sendNpcChat("Go meet my main tracker, Koftik. He will help you. He", "waits for you at the west side of West Ardougne.");
								return true;
							case 9:
								d.sendNpcChat("We must find a way through these caverns if we are to", "stop my brother Tyras.");
								return true;
							case 10:
								d.sendPlayerChat("I'll do my best Lathas.");
								return true;
							case 11:
								d.sendNpcChat("A warning traveller, the underground pass is lethal. We", "lost many men exploring those caverns. Go prepared", "with food and armor or you won't last long.");
								d.endDialogue();
								QuestHandler.startQuest(player, this.getQuestID());
								return true;	
						}
					return false;
					case QUEST_COMPLETE:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Thank you again!", Dialogues.HAPPY);
								d.endDialogue();
								return true;
						}
						return false;
				}
				return false;
		}
		return false;
	}

}
