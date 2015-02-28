package com.rs2.model.content.skills.slayer;

import java.util.HashMap;
import java.util.Map;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.combat.projectile.Projectile;
import com.rs2.model.content.combat.projectile.ProjectileDef;
import com.rs2.model.content.combat.projectile.ProjectileTrajectory;
import com.rs2.model.content.combat.weapon.WeaponInterface;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.magic.Spell;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class Slayer { // todo fungicide

	public static final Npc mushRoom = new Npc(3346);
	public static final Npc mushRoom2 = new Npc(3347);
	public static final Npc troll = new Npc(2801);

	public static final int TURAEL = 70;
	public static final int MAZCHNA = 1596;
	public static final int VANNAKA = 1597;
	public static final int CHAELDAR = 1598;
	public static final int DURADEL = 1599;
	
	private Player player;

	public Slayer(Player player) {
		this.player = player;
	}

	public int slayerMaster = 0;
	public String slayerTask = "";
	public int taskAmount;
	public boolean spawnedSlayerNpc = false;

	public enum SlayerTipsData {
		BANSHEE("banshee", new String[] { "Banshees are fearsome creatures with a mighty", "scream. You need something to cover your ears", "to protect from their scream. Banshees are", "are found in the Slayer Tower." }),
		BASILISK("basilisk", new String[] { "Basilisks are found where Fremmenik Slayers delve", "deep. Its stare is dangerous to any slayer. You", "will need something to deflect its stare", "along with a weapon that busts its armour." }), 
		BAT("bat", new String[] {"Bats are typically weak creatures", "but are immune to magic attacks.", "However, they are weak to stabs. They", "are usually found outside of dungeons." }), 
		BLOODVELD("bloodveld", new String[] { "Bloodvelds are somewhat weak creatures with a magical", "melee attack. These strange, demonic creatures", "are weak to various attack styles. They are", "found in the Slayer Tower north of Canifis." }), 
		BLUEDRAGON("blue dragon", new String[] { "These are the stronger cousin of the green dragon,", "and both require protection from their ferocious breath.", "Blue dragons are found in a few different dungeons", "across vscape. They are immune to magiplayer." }), 
		BRONZEDRAGON("bronze dragon", new String[] { "They may be the weakest in their family, but do not", "take them lightly. Like all dragons you need protection", "against their breath. They are weak to attacks that melt", "their metal and are found in Brimhaven Dungeon." }),
		CAVEBUG("cave bug", new String[] { "Cave bugs can be quite dangerous and can be quite", "weak as well. They're weak to magic attacks and are", "sought for their excellent herb drops. You may find", "them under many swamps." }),
		CAVECRAWLER("cave crawler", new String[] { "These are nasty critters with a weakness", "to many weapons, especially those of Dorgeshuun", "descent.  Cave crawlers are found where Fremmenik", "Slayers go and also are found under swamps." }),
		CAVESLIME("cave slime", new String[] { "Cave slimes are weak overall but are", "poisonous if you are attacked. They are", "weak to magiplayer.  They are found under swamps." }),
		COCKATRICE("cockatrice", new String[] { "Cockatrices are weak monsters, especially when using", "a slash weapon.  You will need something to deflect", "their deadly beams from their eyes. They are found", "where many Fremmenik Slayers are." }),
		CRAWLINGHAND("crawling hand", new String[] { "Crawling hands are very weak creatures that derived", "from giant skeleton hands and were enchanted by Dark", "wizards. I would avoid high fiving these residents", "of the Slayer Tower." }), 
		DAGANNOTH("dagannoth", new String[] { "These spiny creatures have either a", "range or melee attack. They are weak to", "weapons such as daggers. They fight in packs", "in island dungeons." }),
		DESERTLIZARD("desert lizard", new String[] {"These small creatures obviously live in", "the desert.  Their habitat, however, allows", "for them to stay alive unless they're cooled", "down somehow." }),
		DOG("dog", new String[] { "Dogs may be a pet, but are in fact", "vicious to all Slayers alike. Ones that", "need to be disposed of are found lurking", "in various dungeons." }),
		GHOUL("ghoul", new String[] { "Ghouls are vicious undead creatures", "found outside lurking in cemetaries. Their", "transparent skin is weak to stab attacks." }),
		GREENDRAGON("green dragon", new String[] { "Cousins of Elvarg, these dragons are not", "to be messed with.  Their ferocious breath", "means that protection will be needed. They", "are found mostly lurking in the Wilderness." }),
		EARTHWARRIOR("earth warrior", new String[] { "These warriors of the Earth are weak to", "crush attacks.  They are only found in the", "dungeon of Edgeville, which is a tad bit","in the Wilderness. So beware!" }),
		ELF("elf", new String[] { "Elves are quick, fierce warriors who are", "weak to melee attacks.  They are found guarding", "their homeland, which lies far West." }),
		HELLHOUND("hellhound", new String[] { "Their name says it all. These hounds of hell are natural", "devil spawn. They attack quickly and accurately, but", "high defense negates these attacks. Stab attacks weaken", "them. They are found closest to hell as they can get." }),
		HOBGOBLIN("hobgoblin", new String[] { "Not to be confused by their weaker cousin, Hobgoblins", "are tough goblins that are weak to all combat types.", "Their accuracy makes up for their weaknesses, They", "lurk all throughout vscape." }),
		ICEGIANT("ice giant", new String[] { "These giants have a strong ice casing", "that is exceptionally weak to fire. They", "are found in below-freezing habitats." }),
		INFERNALMAGE("infernal mage", new String[] {"Infernal mages are masters of combat magiplayer.", "Range easily defeats these residents of the", "Slayer Tower." }),
		JELLY("jelly", new String[] { "Jellies are weak for their level and any", "melee weapon weakens them more. They're", "sought out for their clue scroll drops", "and are found in the Slayer Tower." }),
		KALPHITE("kalphite", new String[] { "Kalphites are nasty offspring of the", "Kalphite Queen.  Weaker ones are not", "poisonous but stronger ones are.  It is best", "to kill these with a crush weapon." }),
		LESSERDEMON("lesser demon", new String[] { "Lesser demons are distant cousins to", "Greater demons, and as any demon are weak to", "Silverlight or Darklight. They can be found", "in many dungeons." }),
		MOSSGIANT("moss giant", new String[] { "Moss Giants are cousins to the Ice Giant", "and are weak to fire spells. These are found", "in many dungeons and areas across vscape." }),
		MOGRE("mogre", new String[] {"Mogres are nasty river trolls who", "can be found at Mudskipper Point.", "Since trolls are so slow, range is", "their weakness." }),
		OGRE("ogre", new String[] { "Ogres are found far west in vscape.", "They are known to be slow and therefore", "range is their weakness." }),
		PYREFIEND("pyrefiend", new String[] { "Pyrefiends were born out of fire,", "and therefore water is their natural weakness.", "Pyrefiends are found where fire is most prevalent." }),
		ROCKSLUG("rockslug", new String[] { "Rockslugs are weak monsters which", "in order to defeat them you need some salt.", "They're found in under swamps and in some dungeons." }),
		SHADOWWARRIOR("shadow warrior", new String[] { "Shadow warriors are masters of concealment.", "They're not seen on the mini map, and are weak", "to crush attacks. They exist in one dungeon only." }),
		SKELETON("skeleton", new String[] { "Skeletons are undead monsters and are", "weak to the spell Crumble Undead and crush","attacks. They're found in dungeons all over", "vscape." }),
		TROLL("troll", new String[] { "Trolls are strong giants found throughout", "vscape, but mainly in Trollheim. They are", "weak to crush attacks and it you may want to", "have good defensive armour when fighting these." }),
		VAMPIRE("vampire", new String[] { "Vampires are unholy beings and are therefore", "weakened by a holy symbol. They do bite occasionally,", "but are overall weak creatures. They are found", "in themost haunted spots in vscape." }), 
		WEREWOLF("werewolf", new String[] { "Werewolves are only found in Canifis", "and are weak to the Wolfbane dagger.", "They're very strong in werewolf form,", "but not in human form." }),
		ABERRANTSPECTER("aberrant spectre", new String[] { "Aberrant specters are odorous creatures", "and as a result require some sort of protection", "against their stench.  They are weak to slash", "attacks and are found in the Slayer Tower." }),
		FIREGIANT("fire giant", new String[] { "Fire giants were born from lava, and weak to", "ice attacks. They hit exceptionally well", "so it's advised to wear armour when fighting", "these. They are found in many dungeons." }),
		CROCODILE("crocodile", new String[] { "Crocodiles are desert creatures who", "reside near river banks to cool off.", "Their scaly armour proves weak to stab", "attacks." }),
		DUSTDEVIL("dust devil", new String[] {"Dust devils use clouds of dust, sand, ash and whatever", "else they can inhale to blind and disorientate their", "victims. Good luck on obtaining a dragon chainmail", "from their dusty remains." }),
		GOBLIN("goblin", new String[] { "Goblins are weak creatures found throughout", "vscape. They're weak to all combat styles,", "mainly melee." }),
		WALLBEAST("wall beast", new String[] { "Wall beasts are vicious creatures who live within", "the walls of swampy caves. You will need some", "sort of helmet protection when fighting them", "or they will bash your head against the wall." }),
		TUROTH("turoth", new String[] { "Turoths are weak creatures that are only", "defeated by the Leaf-bladed weapon. They are", "found deep in the Fremmenik Slayer Dungeon." }),
		ABYSSALDEMON("abyssal demon", new String[] { "Abyssal demons are accurate monsters with the ability", "to teleport themselves and you. They're difficult", " to kill but are very rewarding,", "as they drop the Abyssal whip. These are found in", "the Slayer Tower." }),
		KURASK("kurask", new String[] { "Kurasks are similar to Turoths, but are", "much stronger. They are defeated by a Leaf-bladed", "weapon.  These are found in the Fremmenik Slayer", "dungeon." }),
		GREATERDEMON("greater demon", new String[] { "Greater demons are accurate demons", "weak to Silverlight or Darklight.  It is", "recommended to wear heavy armour when fighting these." }),
		IRONDRAGON("iron dragon", new String[] { "Iron dragons are the stronger version of", "the Bronze dragon, and are found near them too", "They are weak to fire spells, as this melts their", "armour." }),
		BLACKDEMON("black demon", new String[] { "Black demons are the strongest demons", "in vscape. They are weak to Silverlight and", "Darklight but you will still need heavy armour", "to fend off their attacks." }),
		STEELDRAGON("steel dragon", new String[] { "Steel dragons are found where Bronze and Iron", "dragons are found.  They are stronger but are also", "weak to fire spells." }),
		MITHRILDRAGON("mithril dragon", new String[] { "Mithril dragons are found in Brimhaven dungeon.", "A team of archaeologists uncovered a cavern", "containing them and other ancient monsters.", "Good luck." }),
		GARGOYLE("gargoyle", new String[] { "Gargoyles are stone figures that are weak to", "crush attacks.  To defeat them, you need a rock", "hammer.  These are found in the Slayer Tower and", "are known for their Granite maul drops." }),
		SHADE("shade", new String[] { "Shades are weak to crush attacks and are", "of all levels.  They are accurate monsters", "and are found in the darkest parts of vscape." }),
		SPIRITUALMAGE("spiritual mage", new String[] { "There are two ancient spiritual mages on", "the Frozen Waste Plateau in the deep Wilderness.", "Legend has it they may still have a pair of dragon boots", "from the old God Wars." }),
		NECHRYAEL("nechryael", new String[] { "These demons are among the strongest demons", "in vscape. They are sought after for their", "rune boot drops.  Nechryaels are found in the", "Slayer Tower." });

		private String monsterName;
		private String monsterTips[];

		private static Map<String, SlayerTipsData> monsterNames = new HashMap<String, SlayerTipsData>();

		static {
			for (SlayerTipsData data : SlayerTipsData.values()) {
				monsterNames.put(data.monsterName, data);

			}
		}

		SlayerTipsData(String monsterName, String[] monsterTips) {
			this.monsterName = monsterName;
			this.monsterTips = monsterTips;
		}

		public static SlayerTipsData forName(String slayerTask) {
			return monsterNames.get(slayerTask);
		}

		public String getMonsterName() {
			return monsterName;
		}

		public String[] getMonsterTips() {
			return monsterTips;
		}

	}

	public enum SlayerMasterData {
		TURAEL(70, 3, 300, "Burthorpe", Slayer.BURTHORPE_ASSIGNMENTS),
		MAZCHNA(1596, 20, 200, "Canifis", Slayer.CANIFIS_ASSIGNMENTS),
		VANNAKA(1597, 40, 200, "Edgeville dungeon", Slayer.EDGEVILLE_DUNGEON_ASSIGNMENTS),
		CHAELDAR(1598, 70, 200, "Zanaris", Slayer.ZANARIS_ASSIGNMENTS),
		DURADEL(1599, 100, 200, "Shilo village", Slayer.SHILO_VILLAGE_ASSIGNMENTS);

		private int masterId;
		private int maxAmountTask;
		private int combatLevelReq;
		private String masterLocation;
		private String masterTasks[];

		private static Map<Integer, SlayerMasterData> masterIds = new HashMap<Integer, SlayerMasterData>();

		static {
			for (SlayerMasterData data : SlayerMasterData.values()) {
				masterIds.put(data.masterId, data);
			}
		}

		SlayerMasterData(int masterId, int combatLevelReq, int maxAmountTask, String masterLocation, String[] masterTasks) {
			this.masterId = masterId;
			this.combatLevelReq = combatLevelReq;
			this.maxAmountTask = maxAmountTask;
			this.masterLocation = masterLocation;
			this.masterTasks = masterTasks;
		}

		public static SlayerMasterData forId(int masterId) {
			return masterIds.get(masterId);
		}

		public int getMasterId() {
			return masterId;
		}

		public int getCombatLevelReq() {
			return combatLevelReq;
		}

		public int getMaxAmountTask() {
			return maxAmountTask;
		}

		public String getMasterLocation() {
			return masterLocation;
		}

		public String[] getMasterTasks() {
			return masterTasks;
		}
	}

	public enum SlayerTaskData {
		CRAWLING_HANDS("crawling hand", 5, null, "none"),
		CAVE_BUGS("cave bug", 7, null, "none"),
		CAVE_CRAWLER("cave crawler", 10, null, "none"),
		BANSHEE("banshee", 15, new int[] { EARMUFF }, "equipment"),
		CAVE_SLIME("cave slime", 17, null, "none"),
		ROCKSLUG("rockslug", 20, new int[] { BAG_OF_SALT }, "use"),
		DESERT_LIZARD("desert lizard", 22, new int[] { ICE_COOLER }, "use"),
		COCKATRICE("cockatrice", 25, new int[] { MIRROR_SHIELD }, "equipment"),
		PYREFIEND("pyrefiend", 30, null, "none"),
		MOGRE("mogre", 32, null, "none"),
		HARPIE_BUG_SWARM("harpie bug swarm", 33, new int[] { LANTERN }, "equipment"),
		WALL_BEAST("wall beast", 35, new int[] { SPINY_HELMET }, "equipment"),
		KILLERWATT("killerwatt", 37, new int[] { SLAYER_BOOTS }, "equipment"),
		BASILISK("basilisk", 40, new int[] { MIRROR_SHIELD }, "equipment"),
		FEVER_SPIDER("fever spider", 42, new int[] { SLAYER_GLOVES }, "equipment"),
		INFERNAL_MAGE("infernal mage", 45, null, "none"),
		BLOODVELD("bloodveld", 50, null, "none"),
		JELLY("jelly", 52, null, "none"),
		TUROTH("turoth", 55, new int[] { LEAF_BLADED_SPEAR }, "equipment"),
		ZYGOMITE("zygomite", 57, FUNGICIDE, "use"),
		ABERRANT_SPECTRE("aberrant spectre", 60, new int[] { NOSE_PEG }, "equipment"),
		DUST_DEVIL("dust devil", 65, new int[] { FACE_MASK }, "equipment"),
		KURASK("kurask", 70, new int[] { LEAF_BLADED_SPEAR }, "equipment"),
		SKELETAL_WYVERNS("skeletal wyvern", 72, new int[] { ELEMENTAL_SHIELD, 11283, 11284 }, "equipment"),
		GARGOYLE("gargoyle", 75, new int[] { ROCK_HAMMER }, "use"),
		NECHRYAEL("nechryael", 80, null, "none"),
		SPIRITUAL_MAGE("spiritual mage", 83, null, "none"),
		ABYSSAL_DEMON("abyssal demon", 85, null, "none"),
		DARK_BEAST("dark beast", 90, null, "none");

		private String monsterName;
		private int levelRequired;
		private int equipmentNeeded[];
		private String equipmentType;

		private static Map<String, SlayerTaskData> monsterNames = new HashMap<String, SlayerTaskData>();

		static {
			for (SlayerTaskData data : SlayerTaskData.values()) {
				monsterNames.put(data.monsterName, data);

			}
		}

		SlayerTaskData(String monsterName, int levelRequired, int[] equipmentNeeded, String equipmentType) {
			this.monsterName = monsterName;
			this.levelRequired = levelRequired;
			this.equipmentNeeded = equipmentNeeded;
			this.equipmentType = equipmentType;
		}

		public static SlayerTaskData forName(String monsterName) {
			if (monsterName.equalsIgnoreCase("")) {
				return null;
			}
			return monsterNames.get(monsterName);
		}

		public String getMonsterName() {
			return monsterName;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public int[] getEquipmentNeeded() {
			return equipmentNeeded;
		}

		public String getEquipmentType() {
			return equipmentType;
		}
	}

	public static final int ENCHANTED_GEM = 4155;
	public static final int EARMUFF = 4166;
	public static final int BAG_OF_SALT = 4161;
	public static final int ICE_COOLER = 6696;
	public static final int MIRROR_SHIELD = 4156;
	public static final int FISHING_EXPLOSION = 6664;
	public static final int LANTERN = 7053;
	public static final int SPINY_HELMET = 4551;
	public static final int SLAYER_BOOTS = 7159;
	public static final int SLAYER_GLOVES = 6720;
	public static final int NOSE_PEG = 4168;
	public static final int FACE_MASK = 4164;
	public static final int ELEMENTAL_SHIELD = 2890;
	public static final int ROCK_HAMMER = 4162;
	public static final int LEAF_BLADED_SPEAR = 4158;
	public static final int BROAD_ARROWS = 4160;
	public static final int SLAYER_STAFF = 4170;
	public static final int FUNGICIDE[] = { 7421, 7422, 7423, 7424, 7425, 7426, 7427, 7428, 7429, 7430 };

	
	public static final String BURTHORPE_ASSIGNMENTS[] = {"banshee", "bat", "kalphite", "bear", "crawling hand", "ghost", "goblin", "skeleton", "spider", "wolf"};
	public static final String CANIFIS_ASSIGNMENTS[] = {"basilisk", "kalphite", "cave crawler", "crawling hand", "skeleton", "earth warrior","bat", "bear","bloodveld","cockatrice","ghost", "hobgoblin","ice giant", "ice warrior", "infernal mage", "jelly","lesser demon", "ogre", "pyrefiend", "shadow warrior", "zombie", "moss giant", "rockslug", "banshee"};
	public static final String EDGEVILLE_DUNGEON_ASSIGNMENTS[] = {"aberrant spectre", "kalphite", "ice warrior", "iron dragon", "jelly", "pyrefiend", "shadow warrior", "dagannoth", "goblin", "hill giant", "banshee", "basilisk", "bloodveld", "blue dragon", "bronze dragon", "cockatrice", "dust devil", "crawling hand", "earth warrior", "fire giant", "green dragon", "hellhound", "ice giant", "lesser demon", "moss giant", "rockslug", "turoth"};
	public static final String ZANARIS_ASSIGNMENTS[] = {"basilisk", "abyssal demon", "kalphite", "black demon", "dust devil", "gargoyle", "greater demon", "nechryael", "skeletal wyvern", "kurask", "dagannoth", "turoth", "aberrant spectre", "gargoyle", "bronze dragon", "cockatrice", "infernal mage", "fire giant", "hellhound", "iron dragon", "jelly", "lesser demon", "pyrefiend", "steel dragon"};
	public static final String SHILO_VILLAGE_ASSIGNMENTS[] = {"bronze dragon", "abyssal demon", "kalphite", "black demon", "black dragon", "dust devil", "gargoyle", "kurask", "hellhound", "dagannoth", "skeletal wyvern", "mithril dragon", "spiritual mage", "steel dragon", "nechryael", "iron dragon", "aberrant spectre", "turoth", "fire giant", "greater demon", "dark beast"};

	public void assignNewTask(int id) {
		SlayerMasterData master = SlayerMasterData.forId(id);
		if (master == null) {
			return;
		} else {
			if (!Constants.SLAYER_ENABLED) {
				player.getActionSender().sendMessage("This skill is currently disabled.");
				player.getActionSender().removeInterfaces();
				return;
			}
			if (player.getCombatLevel() < master.getCombatLevelReq()) {
				player.getDialogue().sendNpcChat("You need a combat level of "+master.getCombatLevelReq()+" to recieve a task from me.", Dialogues.HAPPY);
				return;
			}
			if (master.getMasterId() == 1599 && player.getSkill().getLevel()[Skill.SLAYER] < 50) {
				player.getDialogue().sendNpcChat("You need a slayer level of 50 to recieve a task from me.", Dialogues.HAPPY);
				return;
			}
			slayerMaster = id;
			slayerTask = master.getMasterTasks()[Misc.random(master.getMasterTasks().length - 1)];
			taskAmount = 10 + Misc.random(master.getMaxAmountTask() - 10);
			handleSpecialMonsterTask();
			player.getDialogue().sendNpcChat((new StringBuilder()).append("Your new task is to kill ").append(taskAmount).append(" ").append(slayerTask).append("s.").toString(), Dialogues.HAPPY);
			return;
		}
	}

	public void handleSpecialMonsterTask() {
		SlayerTaskData slayerTaskData = SlayerTaskData.forName(slayerTask);
		if (slayerTaskData == null)
			return;
		if (slayerTaskData.getLevelRequired() > player.getSkill().getPlayerLevel(Skill.SLAYER)) {
			assignNewTask(slayerMaster);
		}

	}

	public void resetSlayerTask() {
		slayerMaster = 0;
		slayerTask = "none";
		taskAmount = 0;
		player.getActionSender().sendMessage("You have finished your slayer task! Speak to a slayer master to get a new one.");
	}

	public void handleNpcDeath(Npc npc) {
		if (slayerMaster < 1 || slayerTask.equalsIgnoreCase("")) {
			return;
		}
		String npcName = npc.getDefinition().getName().toLowerCase();
		if (slayerTask.equalsIgnoreCase("werewolf") && (npcName.contains("wolfman") || npcName.contains("wolfwoman"))) {
			//count werewolves
		} else if (!npcName.contains(slayerTask)) {
			return;
		}
		if (taskAmount == 0) {
			resetSlayerTask();
			return;
		} else {
			taskAmount--;
			if(taskAmount%10 == 0 && player.getEquipment().getId(Constants.HANDS) >= 11118 && player.getEquipment().getId(Constants.HANDS) <= 11126) {
				player.getActionSender().sendMessage("You still need to kill " + taskAmount + " monsters to complete your current Slayer assignment.");
			}
			player.getSkill().addExp(Skill.SLAYER, npc.getDefinition().getHitpoints());
			return;
		}
	}

	public boolean hasSlayerRequirement(Npc npc) {
		SlayerTaskData slayerTaskData = SlayerTaskData.forName(npc.getDefinition().getName().toLowerCase());
		if (slayerTaskData == null)
			return true;
		if (player.getSkill().getLevel()[Skill.SLAYER] < slayerTaskData.getLevelRequired()) {
			player.getActionSender().sendMessage((new StringBuilder()).append("You need a slayer level of ").append(slayerTaskData.getLevelRequired()).append(" to attack this monster.").toString());
			return false;
		}
		if (slayerTaskData.getMonsterName().equalsIgnoreCase("kurask") || slayerTaskData.getMonsterName().equalsIgnoreCase("turoth")) {
			if (player.getEquippedWeapon() != null && (player.getEquippedWeapon().getWeaponInterface() == WeaponInterface.LONG_BOW || player.getEquippedWeapon().getWeaponInterface() == WeaponInterface.SHORT_BOW) && player.getEquipment().getItemContainer().get(Constants.ARROWS).getId() == BROAD_ARROWS)
				return true;
			if (player.getCastedSpell() == Spell.MAGIC_DART || player.getAutoSpell() == Spell.MAGIC_DART)
				return true;
		}
		if (slayerTaskData.getEquipmentNeeded() == null)
			return true;
		if (slayerTaskData.getEquipmentType().equals("use") || slayerTaskData.getEquipmentType().equals("none"))
			return true;
		for (Item equipment : player.getEquipment().getItemContainer().toArray()) {
			if (equipment == null)
				continue;
			for (int needed : slayerTaskData.getEquipmentNeeded()) {
				if (equipment.getId() == needed)
					return true;
			}

		}
		player.getActionSender().sendMessage("You don't have the required protection against this kind of monster!");
		return false;

	}

	public boolean needToFinishOffMonster(Npc npc, boolean message) {
		SlayerTaskData slayerTaskData = SlayerTaskData.forName(npc.getDefinition().getName().toLowerCase());
		if (slayerTaskData == null)
			return false;
		if (slayerTaskData.getEquipmentType().equals("use") && npc.getCurrentHp() > 1) {
			return true;
		}
		int tenPercent = npc.getMaxHp() / 10;
		if (slayerTaskData.getEquipmentType().equals("use") && npc.getCurrentHp() <= tenPercent) {
			if (message)
				player.getActionSender().sendMessage((new StringBuilder()).append("The ").append(Misc.ucFirst(slayerTaskData.getMonsterName())).append(" is on its last legs! Finish it quickly!").toString());
			return true;
		}
		return false;
	}

	public boolean doNpcSpecialEffect(final Npc npc) {
		final int x = npc.getPosition().getX();
		final int y = npc.getPosition().getY();
		if (spawnedSlayerNpc)
			return false;
		switch (npc.getDefinition().getId()) {
		case 3344:
		case 3345:
			player.getUpdateFlags().sendAnimation(827);
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					npc.setVisible(false);
					NpcLoader.spawnNpc(player, npc.getDefinition().getId() == 3344 ? mushRoom : mushRoom2, new Position(x, y), false, null);
					container.stop();
				}

				@Override
				public void stop() {
				}
			}, 2);
			return true;
		}
		return false;
	}

	public boolean handleObjects(final int objectId, final int objectX, final int objectY) {
		if (!Misc.goodDistance(player.getPosition().getX(), player.getPosition().getY(), objectX, objectY, 7))
			return false;
		if (spawnedSlayerNpc) {
			player.getActionSender().sendMessage("I think i should kill Mogre if i want to try this again.");
			return false;
		}
		switch (objectId) {
		case 10087:
		case 10088:
		case 10089:
			if (!player.getInventory().playerHasItem(FISHING_EXPLOSION)) {
				player.getActionSender().sendMessage("You don't have anything to lure with.");
				return false;
			} else {
				player.getMovementHandler().reset();
				player.getUpdateFlags().sendFaceToDirection(new Position(objectX, objectY));
				player.getUpdateFlags().sendAnimation(806);
				player.getActionSender().sendMessage("You throw the shuddering vial into the water...");
				player.getInventory().removeItem(new Item(FISHING_EXPLOSION));
				new Projectile(player.getPosition(), 3, new Position(objectX, objectY), 0, new ProjectileDef(29, ProjectileTrajectory.SPELL)).show();
				player.setStopPacket(true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						NpcLoader.spawnNpc(player, troll, new Position(player.getPosition().getX(), player.getPosition().getY()), false, null);
						container.stop();
					}

					@Override
					public void stop() {
						player.setStopPacket(false);
					}
				}, 5);
				return true;
			}
		}
		return false;
	}

	public void finishOffMonster(Npc npc, int itemId) {
		SlayerTaskData slayerTaskData = SlayerTaskData.forName(npc.getDefinition().getName().toLowerCase());
		if (slayerTaskData == null)
			return;
		int tenPercent = npc.getMaxHp() / 10;
		if (!slayerTaskData.getEquipmentType().equals("use") || npc.getCurrentHp() > tenPercent)
			return;
		int arr$[] = slayerTaskData.getEquipmentNeeded();
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++) {
			int item = arr$[i$];
			if (item == itemId) {
				player.getUpdateFlags().sendAnimation(832);
				npc.setDead(true);
				CombatManager.startDeath(npc);
				if (itemId != ROCK_HAMMER) {
					player.getInventory().removeItem(new Item(itemId));
					player.getInventory().addItem(new Item(itemId != 7432 ? itemId < 7421 || itemId > 7430 ? -1 : itemId + 1 : 7421));
				}
				return;
			}
		}

	}

	public boolean handleItemOnItem(int itemUsed, int usedWith) {
		if ((itemUsed == 7432 && usedWith == 7431) || (itemUsed == 7431 && usedWith == 7432)) {
			player.getInventory().removeItem(new Item(7432));
			player.getInventory().removeItem(new Item(7431));
			player.getInventory().addItem(new Item(7421));
			return true;
		}
		return false;
	}

}
