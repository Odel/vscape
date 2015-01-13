package com.rs2.model.content.skills.mining;

import com.rs2.Constants;
import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.combat.util.RingEffect;
import com.rs2.model.content.randomevents.SpawnEvent;
import com.rs2.model.content.randomevents.SpawnEvent.RandomNpc;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.content.skills.Tools;
import com.rs2.model.content.skills.Tools.Tool;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemManager;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

/**
 * Mining class
 */
// TODO: Add rune ess mining
public class MineOre {

	private Player player;

	public MineOre(Player player) {
		this.player = player;
	}

	public static final int[] granites = {6981, 6979, 6983};
	public static final int[] sandstone = {6977, 6971, 6975, 6973};

	public static final int[] normalGems = {1623, 1623, 1623, 1623, 1621, 1621, 1621, 1619, 1619, 1617};
	public static final int[] specialGems = {1625, 1625, 1627, 1627, 1629};

	// THINGS THAT STILL NEED ADDING HERE (LIMESTONE AND ELEMENTAL) THATS IT I THINK
	public enum MiningData {
		COPPER(new int[]{2090, 2091, 3042, 9708, 9709, 9710, 11936, 11937, 11938, 11960, 11961, 11962,14906,14907}, 436, 1, 18, 7), TIN(new int[]{2094, 2095, 3043, 9714, 9715, 9716, 11933, 11934, 11935, 11957, 11958, 11959,14902,14903}, 438, 1, 18, 7), BLURITE(new int[]{2110, 10583, 10584}, 668, 10, 17, 25), IRON(new int[]{2092, 2093, 9717, 9718, 9719, 11954, 11955, 11956,14856,14857,14858,14913,14914}, 440, 15, 35, 16), COAL(new int[]{2096, 2097, 10948, 11930, 11931, 11932, 11963, 11964, 11965,14850,14851,14852}, 453, 30, 50, 83), GEM(new int[]{2111}, -1, 40, 65, 50), SANDSTONE(new int[]{10946}, -1, 35, 0, 15), GRANITE(new int[]{10947}, 444, 45, 65, 15), GOLD(new int[]{2098, 9720, 9721, 9722, 11183, 11184, 11185, 11951, 11952, 11953}, 444, 40, 65, 150), PERFECT_GOLD(new int[]{2099}, 446, 40, 65, 150), SILVER(new int[]{2100, 2101, 11186, 11187, 11188, 11948, 11949, 11950}, 442, 20, 40, 100), MITHRIL(new int[]{2102, 2103, 11942, 11943, 11944, 11945, 11946, 11947,14853,14854,14855}, 447, 55, 80, 300), ADAMANTITE(new int[]{2104, 2105, 11939, 11940, 11941,14862,14863,14864}, 449, 70, 95, 600), RUNITE(new int[]{2106, 2107, 14859, 14860, 14861}, 451, 85, 125, 2000), CLAY(new int[]{2108, 2109, 9711, 9712, 9713, 10949, 11189, 11190, 11191,14904,14905}, 434, 1, 5, 3), EMPTY(new int[]{10944,
				9723, 9724, 9725, 11555, 11552, 11553, 11554, 11557, 11556, 450, 451}, -1, 0, 0, 0), ELEMENTAL(new int[]{3403}, 2892, 20, 45, 20);
		private int[] objectIDs;
		private int oreReceived;
		private int levelRequired;
		private int expReceived;
		private int respawnTimer;

		MiningData(int[] objectIDs, int oreReceived, int levelRequired, int expReceived, int respawnTimer) {
			this.objectIDs = objectIDs;
			this.oreReceived = oreReceived;
			this.levelRequired = levelRequired;
			this.expReceived = expReceived;
			this.respawnTimer = respawnTimer;
		}

		public static MiningData forId(int objectId) {
			for (MiningData miningData : MiningData.values())
				for (int object : miningData.objectIDs)
					if (objectId == object)
						return miningData;
			return null;
		}
	}

	public boolean canMine(final int object) {
		/*
		 * if (c.tutorialDia < 28) { DialogueHandler.sendStatement(c,
		 * "You are not ready to mine yet. Please follow the tutorial and you'll"
		 * , "be mining in no time."); return false; }
		 */
		if (!miningRocks(object)) {
			return false;
		}
		if (!Constants.MINING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return false;
		}
		if (player.getInventory().getItemContainer().freeSlots() <= 0) {
			player.getActionSender().sendMessage("Not enough space in your inventory.", true);
			if (player.getNewComersSide().isInTutorialIslandStage()) {
				player.getDialogue().sendStatement("Not enough space in your inventory.");
				player.setClickId(0);
			}
			return false;
		}
		if (Tools.getTool(player, Skill.MINING) == null) {
			player.getActionSender().sendMessage("You do not have a pickaxe that you can use.", true);
			if (player.getNewComersSide().isInTutorialIslandStage()) {
				player.getDialogue().sendStatement("You do not have a pickaxe that you can use.");
				player.setClickId(0);
			}
			return false;
		}
		if (!SkillHandler.hasRequiredLevel(player, Skill.MINING, getLevelReq(object), "mine here")) {
			return false;
		}
		return true;
	}

	public void startMining(final int object, final int obX, final int obY) {
		if (!SkillHandler.checkObject(object, obX, obY, player.getPosition().getZ())) {
			return;
		}
		final GameObject p = ObjectHandler.getInstance().getObject(obX, obY, player.getPosition().getZ());
		if (p != null) {
			player.getActionSender().sendMessage("There is currently no ores remaining in this rock.", true);
			if (player.getNewComersSide().isInTutorialIslandStage()) {
				player.getDialogue().sendStatement("There is currently no ores remaining in this rock.");
				player.setClickId(0);
			}
			return;
		}
		if (player.getInventory().getItemContainer().freeSlots() <= 0) {
			player.getActionSender().sendMessage("Not enough space in your inventory.", true);
			return;
		}

		player.getActionSender().sendMessage("You swing your pick at the rock.", true);
		if (player.getNewComersSide().isInTutorialIslandStage()) {
			player.getDialogue().sendTutorialIslandWaitingInfo("", "Your character is now attempting to mine the rock.", "This should take only a few seconds.", "", "Please wait...");
		}
		final int task = player.getTask();
		final MiningData miningData = MiningData.forId(object);
		if (miningData == null || miningData == MiningData.EMPTY)
			return;
		final Tool pickaxe = Tools.getTool(player, Skill.MINING);
		if (pickaxe == null) {
			player.getActionSender().sendMessage("You do not have a pickaxe that you can use.", true);
			return;
		}
		player.resetAnimation();
		player.getUpdateFlags().sendAnimation(pickaxe.getAnimation());
		player.getActionSender().sendSound(432, 0, 0);
		player.setSkilling(new CycleEvent() {
		    int itemReceived = miningData.oreReceived;
			final int getXp = miningData.expReceived;
			final int respawnTimer = getRespawnTimerFormula(miningData.respawnTimer);
			final int levelReq = miningData.levelRequired;
			
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task)) {
					container.stop();
					return;
				}
				final GameObject p = ObjectHandler.getInstance().getObject(obX, obY, player.getPosition().getZ());
				if (p != null && p.getDef().getId() != object) {
					if (player.getNewComersSide().isInTutorialIslandStage()) {
						player.getDialogue().sendStatement("There is no more ore in this rock.");
						player.setClickId(0);
					}
					player.getActionSender().sendMessage("There is no more ore in this rock.", true);
					container.stop();
					return;
				}
				if (SkillHandler.doSpawnEvent(player)) {
					SpawnEvent.spawnNpc(player, RandomNpc.ROCK_GOLEM);
				}
                container.setTick(3);
			    player.getUpdateFlags().sendAnimation(pickaxe.getAnimation());
				player.getActionSender().sendSound(432, 0, 0);
				if (SkillHandler.skillCheck(player.getSkill().getLevel()[Skill.MINING], levelReq, pickaxe.getBonus())) {
					if (itemReceived == 434 && player.getEquipment().getId(Constants.HANDS) == 11074) {
						player.setClayBraceletLife(player.getClayBraceletLife() - 1);
						itemReceived = 1761;
						if (player.getClayBraceletLife() < 1 && player.getEquipment().getId(Constants.HANDS) == 11074) {
							player.getEquipment().removeAmount(Constants.HANDS, 1);
							player.getActionSender().sendMessage("Your Bracelet of Clay shatters!", true);
							player.setClayBraceletLife(28);
						}
					}
					if(getItemRecieved(object, itemReceived) == 446 && !Entity.inArea(player, 2726, 2746, 9680, 9700)) {
					    itemReceived = 444;
					    player.getInventory().addItem(new Item(itemReceived, 1));
					} else {
					    player.getInventory().addItem(new Item(getItemRecieved(object, itemReceived), 1));
					}
					player.getActionSender().sendMessage("You manage to mine some " + (object == 2111 ? "gem" : object == 10946 ? "sandstone" : object == 10947 ? "granite" : ItemManager.getInstance().getItemName(itemReceived).toLowerCase() + "."));
					if (player.getNewComersSide().isInTutorialIslandStage()) {
						player.getDialogue().sendStatement("You manage to mine some " + (object == 2111 ? "gem" : object == 10946 ? "sandstone" : object == 10947 ? "granite" : ItemManager.getInstance().getItemName(itemReceived).toLowerCase() + "."));
						player.setClickId(0);
						if (player.getNewComersSide().getTutorialIslandStage() == 32 && object == 3043)
							player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);
						else if (player.getNewComersSide().getTutorialIslandStage() == 33 && object == 3042)
							player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);

					}
					player.getSkill().addExp(Skill.MINING, getExp(itemReceived, getXp));
					try {
						int face = SkillHandler.getFace(object, obX, obY, player.getPosition().getZ());
						int type = SkillHandler.getType(object, obX, obY, player.getPosition().getZ());
						new GameObject(emptyOre(object), obX, obY, player.getPosition().getZ(), type == 22 ? getFace(face) : face, type == 11 ? 11 : 10, object, respawnTimer);
					} catch (Exception e) {
						e.printStackTrace();
					}
					container.stop();
				}
				if (!player.getInventory().canAddItem(new Item(getItemRecieved(object, itemReceived), 1))) {
					container.stop();
					return;
				}
			}
			@Override
			public void stop() {
				player.getUpdateFlags().sendAnimation(-1);
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 3);
	}

	public static int getFace(int face) {
		switch (face) {
			case 1 :
				return 2;
			case 2 :
				return 4;
			case 3 :
				return 1;
			case 4 :
				return 0;

		}
		return face;
	}

	private static double getExp(int itemReceived, int getXp) {
		switch (itemReceived) {
			// granite
			case 6979 :// 2kg
				return 50;
			case 6981 :// 5kg
				return 60;
			case 6983 : // 5kg
				return 75;
				// sandstone
			case 6971 :// 1kg
				return 30;
			case 6973 :// 2kg
				return 40;
			case 6975 :// 5kg
				return 50;
			case 6977 :// 10kg
				return 60;
		}
		return getXp;
	}

	public static int getItemRecieved(int object, int itemReceived) {
		switch (object) {
			case 0 : // random gem event
				return RingEffect.normalGems[Misc.randomMinusOne(RingEffect.normalGems.length)];
			case 2111 :
				while (true) {
					if (Misc.random(2) == 0) {
						return normalGems[Misc.randomMinusOne(normalGems.length)];
					} else {
						return specialGems[Misc.randomMinusOne(specialGems.length)];
					}
				}
			case 10947 :
				return granites[Misc.random(granites.length - 1)];
			case 10946 :
				return sandstone[Misc.random(sandstone.length - 1)];
		}
		return itemReceived;
	}

	public static boolean miningRocks(int object) {
		return MiningData.forId(object) != null;
	}

	private static int emptyOre(int object) {
		int[] ore1 = {9708, 9711, 9714, 9717, 9720};
		int[] ore2 = {9709, 9712, 9715, 9718, 9721};
		int[] ore3 = {9710, 9713, 9716, 9719, 9722};

		int[] ore4 = {11183, 11186, 11189, 11930, 11933, 11936, 11939, 11942, 11945, 11948, 11951, 11954, 11957, 11960, 11963};
		int[] ore5 = {11184, 11187, 11190, 11931, 11934, 11937, 11940, 11943, 11946, 11949, 11952, 11955, 11958, 11961, 11964};
		int[] ore6 = {11185, 11188, 11191, 11932, 11935, 11938, 11941, 11944, 11947, 11950, 11953, 11956, 11959, 11962, 11965};
		
		int[] ore7 = {14859};
		int[] ore8 = {14860};
		int[] ore9 = {14861};
		if (object == 10946 || object == 10948) {
			return 10944;
		}
		if (object == 10947 || object == 10949) {
			return 10945;
		}
		if(object == 10583 || object == 10584) {
			return 10585;
		}
		for (int i : ore1) {
			if (object == i) {
				return 9723;
			}
		}
		for (int i : ore2) {
			if (object == i) {
				return 9724;
			}
		}
		for (int i : ore3) {
			if (object == i) {
				return 9725;
			}
		}
		for (int i : ore4) {
			if (object == i) {
				return object >= 11945 ? 11555 : 11552;
			}
		}
		for (int i : ore5) {
			if (object == i) {
				return object >= 11945 ? 11556 : 11553;
			}
		}
		for (int i : ore6) {
			if (object == i) {
				return object >= 11945 ? 11557 : 11554;
			}
		}
		for (int i : ore7) {
			if (object == i) {
				return 14832;
			}
		}
		for (int i : ore8) {
			if (object == i) {
				return 14833;
			}
		}
		for (int i : ore9) {
			if (object == i) {
				return 14834;
			}
		}
		if (object % 2 == 0 || object == 3043) {
			return 450;
		} else {
			return 451;
		}
	}

	private static int getLevelReq(int object) {
		if (MiningData.forId(object) != null)
			return MiningData.forId(object).levelRequired;
		return 0;
	}

	public boolean prospect(final int objectId) {
		int[] empty = {14832, 14833, 14834, 10944, 9723, 9724, 9725, 11555, 11552, 11553, 11554, 11557, 11556, 450, 451};
		for (int i : empty)
			if (objectId == i) {
				player.getActionSender().sendMessage("There is currently no ores remaining in this rock.", true);
				return true;
			}
		MiningData miningData = MiningData.forId(objectId);
		if (miningData == null)
			return false;
		if (player.getNewComersSide().isInTutorialIslandStage())
			player.getDialogue().sendTutorialIslandWaitingInfo("", "Your character is now attempting to prospect the rock. This", "should take only a few seconds.", "", "Please wait...");
		player.getActionSender().sendMessage("You examine the rock for ores...");
		player.setStopPacket(true);
		int oreId = getItemRecieved(objectId, miningData.oreReceived);
		final String oreName = ItemManager.getInstance().getItemName(oreId).toLowerCase().replaceAll("ore", "").trim();
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				player.getActionSender().sendMessage("This rock contains " + (objectId == 2111 ? "gems" : objectId == 2491 ? "unbound Rune Stone essence" : oreName + "."));
				if (player.getNewComersSide().isInTutorialIslandStage()) {
					if (player.getNewComersSide().getTutorialIslandStage() == 29 && oreName.contains("tin"))
						player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);

					else if (player.getNewComersSide().getTutorialIslandStage() == 30 && oreName.contains("copper"))
						player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);

					player.getDialogue().sendStatement("This rock contains " + (objectId == 2111 ? "gems" : objectId == 2491 ? "unbound Rune Stone essence" : oreName + "."));
					player.setClickId(0);
				}
				player.setStopPacket(false);
				b.stop();
			}
			@Override
			public void stop() {

			}
		}, 5);
		return true;
	}

	public static int getSmokingRock(Position objectClickingLocation) {
		if (objectClickingLocation == null) {
			return -1;
		}
		CacheObject g = ObjectLoader.object(objectClickingLocation.getX(), objectClickingLocation.getY(), 0);
		if (g.getDef().getId() >= 2090 && g.getDef().getId() <= 2111) {
			return g.getDef().getId() + 29;
		}
		if (g.getDef().getId() >= 10944 && g.getDef().getId() <= 10949) {
			return g.getDef().getId() + 246;
		}

		return -1;
	}

	public boolean handleRockSmokeClick(final Position loc) {
		final Tool pickaxe = Tools.getTool(player, Skill.MINING);
		if (pickaxe != null) {
			final int objectId = player.getClickId();
			player.getActionSender().sendMessage("You swing your pick at the rock.");
			final int task = player.getTask();
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer p) {
					if (!player.checkTask(task)) {
						p.stop();
						return;
					}
					player.getUpdateFlags().sendAnimation(pickaxe.getAnimation());
					p.stop();
				}
				@Override
				public void stop() {
				}
			}, 1);
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer p) {
					GameObject o = ObjectHandler.getInstance().getObject(loc.getX(), loc.getY(), loc.getZ());
					if (o == null || o.getDef().getId() == objectId) {
						p.stop();
						return;
					}
					if (player.checkTask(task)) {
						Tools.breakTool(player, Skill.MINING);
						player.getActionSender().createStillGfx(157, loc.getX(), loc.getY(), 0, 1);
						ObjectHandler.getInstance().removeObject(loc.getX(), loc.getY(), loc.getZ(), 10);
						player.getActionSender().sendMessage("Your pickaxe has been broken by the rock!", true);
						player.hit(Misc.random(5) + 5, HitType.NORMAL);
						player.getUpdateFlags().sendAnimation(-1);
					}
					p.stop();
				}
				@Override
				public void stop() {
				}
			}, 5);
			return true;
		} else {
			player.getActionSender().sendMessage("You do not have a pickaxe that you can use.", true);
		}
		return false;
	}

	public static int getRespawnTimerFormula(int timer) {
		//int timerInSeconds = (timer * 6) / 10;
		//return timerInSeconds - ((timerInSeconds / 10) * (2000 % World.playerAmount()));
		return timer;
	}

}