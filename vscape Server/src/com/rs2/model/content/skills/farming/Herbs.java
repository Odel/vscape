package com.rs2.model.content.skills.farming;

import java.util.HashMap;
import java.util.Map;

import com.rs2.Constants;
import com.rs2.Server;
import com.rs2.model.Position;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 24/02/12 Time: 20:34 To change
 * this template use File | Settings | File Templates.
 */
public class Herbs {

	private Player player;

	// set of global constants for Farming

	private static final int START_HARVEST_AMOUNT = 3;
	private static final int END_HARVEST_AMOUNT = 18;

	private static final double COMPOST_CHANCE = 0.9;
	private static final double SUPERCOMPOST_CHANCE = 0.7;
	private static final double CLEARING_EXPERIENCE = 4;

	public Herbs(Player player) {
		this.player = player;
	}

	// Farming data
	public int[] farmingStages = new int[4];
	public int[] farmingSeeds = new int[4];
	public int[] farmingHarvest = new int[4];
	public int[] farmingState = new int[4];
	public long[] farmingTimer = new long[4];
	public double[] diseaseChance = {1, 1, 1, 1, 1};

	/* set of the constants for the patch */

	// states - 2 bits plant - 6 bits
	public static final int GROWING = 0x00;

	public static final int MAIN_HERB_LOCATION_CONFIG = 515;

	/* This is the enum holding the seeds info */

	public enum HerbData {
		GUAM(5291, 199, 9, 80, 0.25, 11, 12.5, 0x04, 0x08),
		MARRENTILL(5292, 201, 14, 80, 0.25, 13.5, 15, 0x0b, 0x0f),
		TARROMIN(5293, 203, 19, 80, 0.25, 16, 18, 0x12, 0x16), 
		HARRALANDER(5294, 205, 26, 80, 0.25, 21.5, 24, 0x19, 0x1d), 
		GOUT_TUBER(6311, 3261, 29, 80, 0.25, 105, 45, 0xc0, 0xc4), 
		RANARR(5295, 207, 32, 80, 0.20, 27, 30.5, 0x20, 0x24),
		TOADFLAX(5296, 3049, 38, 80, 0.20, 34, 38.5, 0x27, 0x2b), 
		IRIT(5297, 209, 44, 80, 0.20, 43, 48.5, 0x2e, 0x32),
		AVANTOE(5298, 211, 50, 80, 0.20, 54.5, 61.5, 0x35, 0x39),
		KUARM(5299, 213, 56, 80, 0.20, 69, 78, 0x44, 0x48), 
		SNAPDRAGON(5300, 3051, 62, 80, 0.15, 87.5, 98.5, 0x4b, 0x4f), 
		CADANTINE(5301, 215, 67, 80, 0.15, 106.5, 120, 0x52, 0x56), 
		LANTADYME(5302, 2485, 73, 80, 0.15, 134.5, 151.5, 0x59, 0x5d), 
		DWARF(5303, 217, 79, 80, 0.15, 170.5, 192, 0x60, 0x64), 
		TORSOL(5304, 219, 85, 80, 0.15, 199.5, 224.5, 0x67, 0x6b);

		private int seedId;
		private int harvestId;
		private int levelRequired;
		private int growthTime;
		private double diseaseChance;
		private double plantingXp;
		private double harvestXp;
		private int startingState;
		private int endingState;

		private static Map<Integer, HerbData> seeds = new HashMap<Integer, HerbData>();

		static {
			for (HerbData data : HerbData.values()) {
				seeds.put(data.seedId, data);
			}
		}

		HerbData(int seedId, int harvestId, int levelRequired, int growthTime, double diseaseChance, double plantingXp, double harvestXp, int startingState, int endingState) {
			this.seedId = seedId;
			this.harvestId = harvestId;
			this.levelRequired = levelRequired;
			this.growthTime = growthTime;
			this.diseaseChance = diseaseChance;
			this.plantingXp = plantingXp;
			this.harvestXp = harvestXp;
			this.startingState = startingState;
			this.endingState = endingState;
		}

		public static HerbData forId(int seedId) {
			return seeds.get(seedId);
		}

		public int getSeedId() {
			return seedId;
		}

		public int getHarvestId() {
			return harvestId;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public int getGrowthTime() {
			return growthTime;
		}

		public double getDiseaseChance() {
			return diseaseChance;
		}

		public double getPlantingXp() {
			return plantingXp;
		}

		public double getHarvestXp() {
			return harvestXp;
		}

		public int getStartingState() {
			return startingState;
		}

		public int getEndingState() {
			return endingState;
		}
	}

	/* This is the enum data about the different patches */

	public enum HerbFieldsData {
		ARDOUGNE(0, new Position[]{new Position(2670, 3374), new Position(2671, 3375)}), PHASMATYS(1, new Position[]{new Position(3605, 3529), new Position(3606, 3530)}), FALADOR(2, new Position[]{new Position(3058, 3311), new Position(3059, 3312)}), CATHERBY(3, new Position[]{new Position(2813, 3463), new Position(2814, 3464)});
		private int herbIndex;
		private Position[] herbPosition;

		HerbFieldsData(int herbIndex, Position[] herbPosition) {
			this.herbIndex = herbIndex;
			this.herbPosition = herbPosition;
		}

		public static HerbFieldsData forIdPosition(Position position) {
			for (HerbFieldsData herbFieldsData : HerbFieldsData.values()) {
				if (FarmingConstants.inRangeArea(herbFieldsData.getHerbPosition()[0], herbFieldsData.getHerbPosition()[1], position)) {
					return herbFieldsData;
				}
			}
			return null;
		}

		public int getHerbIndex() {
			return herbIndex;
		}

		public Position[] getHerbPosition() {
			return herbPosition;
		}
	}

	/* This is the enum that hold the different data for inspecting the plant */

	public enum InspectData {

		GUAM(5291, new String[][]{{"The seed has only just been planted."}, {"The herb is now ankle height."}, {"The herb is now knee height."}, {"The herb is now mid-thigh height."}, {"The herb is fully grown and ready to harvest."}}), MARRENTILL(5292, new String[][]{{"The seed has only just been planted."}, {"The herb is now ankle height."}, {"The herb is now knee height."}, {"The herb is now mid-thigh height."}, {"The herb is fully grown and ready to harvest."}}), TARROMIN(5293, new String[][]{{"The seed has only just been planted."}, {"The herb is now ankle height."}, {"The herb is now knee height."}, {"The herb is now mid-thigh height."}, {"The herb is fully grown and ready to harvest."}}), HARRALANDER(5294, new String[][]{{"The seed has only just been planted."}, {"The herb is now ankle height."}, {"The herb is now knee height."}, {"The herb is now mid-thigh height."}, {"The herb is fully grown and ready to harvest."}}), GOUT_TUBER(6311, new String[][]{
				{"The seed has only just been planted."}, {"The herb is now ankle height."}, {"The herb is now knee height."}, {"The herb is now mid-thigh height."}, {"The herb is fully grown and ready to harvest."}}), RANARR(5295, new String[][]{{"The seed has only just been planted."}, {"The herb is now ankle height."}, {"The herb is now knee height."}, {"The herb is now mid-thigh height."}, {"The herb is fully grown and ready to harvest."}}), TOADFLAX(5296, new String[][]{{"The seed has only just been planted."}, {"The herb is now ankle height."}, {"The herb is now knee height."}, {"The herb is now mid-thigh height."}, {"The herb is fully grown and ready to harvest."}}), IRIT(5297, new String[][]{{"The seed has only just been planted."}, {"The herb is now ankle height."}, {"The herb is now knee height."}, {"The herb is now mid-thigh height."}, {"The herb is fully grown and ready to harvest."}}), AVANTOE(5298, new String[][]{{"The seed has only just been planted."},
				{"The herb is now ankle height."}, {"The herb is now knee height."}, {"The herb is now mid-thigh height."}, {"The herb is fully grown and ready to harvest."}}), KUARM(5299, new String[][]{{"The seed has only just been planted."}, {"The herb is now ankle height."}, {"The herb is now knee height."}, {"The herb is now mid-thigh height."}, {"The herb is fully grown and ready to harvest."}}), SNAPDRAGON(5300, new String[][]{{"The seed has only just been planted."}, {"The herb is now ankle height."}, {"The herb is now knee height."}, {"The herb is now mid-thigh height."}, {"The herb is fully grown and ready to harvest."}}), CADANTINE(5301, new String[][]{{"The seed has only just been planted."}, {"The herb is now ankle height."}, {"The herb is now knee height."}, {"The herb is now mid-thigh height."}, {"The herb is fully grown and ready to harvest."}}), LANTADYME(5302, new String[][]{{"The seed has only just been planted."}, {"The herb is now ankle height."},
				{"The herb is now knee height."}, {"The herb is now mid-thigh height."}, {"The herb is fully grown and ready to harvest."}}), DWARF(5303, new String[][]{{"The seed has only just been planted."}, {"The herb is now ankle height."}, {"The herb is now knee height."}, {"The herb is now mid-thigh height."}, {"The herb is fully grown and ready to harvest."}}), TORSOL(5304, new String[][]{{"The seed has only just been planted."}, {"The herb is now ankle height."}, {"The herb is now knee height."}, {"The herb is now mid-thigh height."}, {"The herb is fully grown and ready to harvest."}})

		;
		private int seedId;
		private String[][] messages;

		private static Map<Integer, InspectData> seeds = new HashMap<Integer, InspectData>();

		static {
			for (InspectData data : InspectData.values()) {
				seeds.put(data.seedId, data);
			}
		}

		InspectData(int seedId, String[][] messages) {
			this.seedId = seedId;
			this.messages = messages;
		}

		public static InspectData forId(int seedId) {
			return seeds.get(seedId);
		}

		public int getSeedId() {
			return seedId;
		}

		public String[][] getMessages() {
			return messages;
		}
	}

	public void processGrowth()
	{
		for (int i = 0; i < farmingSeeds.length; i++) {
			long difference = (Server.getMinutesCounter() - farmingTimer[i]);
			if(difference >= 5) //5 "minute" period 
			{
				// if weeds or clear patch, the patch needs to lower a stage
				if (farmingStages[i] > 0 && farmingStages[i] <= 3) 
				{
					farmingStages[i]--;
					farmingTimer[i] = Server.getMinutesCounter();
					updateHerbsStates();
					continue;
				}
			}
			HerbData herbData = HerbData.forId(farmingSeeds[i]);
			if (herbData == null) {
				continue;
			}
			long growthTimeTotal = herbData.getGrowthTime();
			int totalStages = (herbData.getEndingState() - herbData.getStartingState());
			long growthTimePerStage = (growthTimeTotal / totalStages);
			int nextStage = farmingStages[i] + 1;
			//if timer is 0 or if the plant is dead or fully grown go to next Herb patch index insted
			if (farmingTimer[i] == 0 || farmingState[i] == 2 || (nextStage > totalStages + 4)) {
				continue;
			}
			if(difference >= growthTimePerStage) //in growth stage time (20 minutes for herbs)
			{
				if (nextStage != farmingStages[i]) {
					farmingStages[i] = nextStage;
					farmingTimer[i] = Server.getMinutesCounter();
					processState(i);
					updateHerbsStates();
				}
			}
		}
	}
	
	public void processState(int index)
	{
		if (farmingState[index] == 2) {
			return;
		}
		if (farmingState[index] == 1) {
			farmingState[index] = 2;
		}
		if (farmingState[index] == 4 && farmingStages[index] != 3) {
			farmingState[index] = 0;
		}
		if (farmingState[index] == 0 && farmingStages[index] >= 4 && farmingStages[index] <= 7) {
			HerbData herbData = HerbData.forId(farmingSeeds[index]);
			if (herbData == null) {
				return;
			}
			double chance = diseaseChance[index] * herbData.getDiseaseChance();
			int maxChance = (int)(chance * 100);
			if (Misc.random(100) <= maxChance) {
				farmingState[index] = 1;
			}
		}
	}
	
	/* update all the patch states */
	public void updateHerbsStates() {
		// falador catherby ardougne phasmatys
		int[] configValues = new int[farmingStages.length];

		int configValue;
		for (int i = 0; i < farmingStages.length; i++) {
			configValues[i] = getConfigValue(farmingStages[i], farmingSeeds[i], farmingState[i], i);
		}

		configValue = (configValues[0] << 16) + (configValues[1] << 8 << 16) + configValues[2] + (configValues[3] << 8);
		player.getActionSender().sendConfig(MAIN_HERB_LOCATION_CONFIG, configValue);

	}

	/* getting the different config values */

	public int getConfigValue(int herbStage, int seedId, int plantState, int index) {
		HerbData herbData = HerbData.forId(seedId);
		switch (herbStage) {
			case 0 :// weed
				return (GROWING << 6) + 0x00;
			case 1 :// weed cleared
				return (GROWING << 6) + 0x01;
			case 2 :
				return (GROWING << 6) + 0x02;
			case 3 :
				return (GROWING << 6) + 0x03;
		}
		if (herbData == null) {
			return -1;
		}
		if (farmingSeeds[index] == 6311) {
			if (plantState == 1) {
				return farmingStages[index] + 0xc1;
			} else if (plantState == 2) {
				return farmingStages[index] + 0xc3;
			}
		}
		return (plantState == 2 ? farmingStages[index] + 0x9e : plantState == 1 ? farmingStages[index] + 0x9a : getPlantState(plantState) << 6) + herbData.getStartingState() + herbStage - 4;
	}

	/* getting the plant states */

	public int getPlantState(int plantState) {
		switch (plantState) {
			case 0 :
				return GROWING;
		}
		return -1;
	}

	/* clearing the patch with a rake of a spade */
	public boolean clearPatch(int objectX, int objectY, int itemId) {
		final HerbFieldsData herbFieldsData = HerbFieldsData.forIdPosition(new Position(objectX, objectY));
		int finalAnimation;
		int finalDelay;
		if (herbFieldsData == null || (itemId != FarmingConstants.RAKE && itemId != FarmingConstants.SPADE)) {
			return false;
		}
		if (farmingStages[herbFieldsData.getHerbIndex()] == 3) {
			return true;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (farmingStages[herbFieldsData.getHerbIndex()] <= 3) {
			if (!player.getInventory().getItemContainer().contains(FarmingConstants.RAKE)) {
				player.getDialogue().sendStatement("You need a rake to clear this path.");
				return true;
			} else {
				finalAnimation = FarmingConstants.RAKING_ANIM;
				finalDelay = 5;
			}
		} else {
			if (!player.getInventory().getItemContainer().contains(FarmingConstants.SPADE)) {
				player.getDialogue().sendStatement("You need a spade to clear this path.");
				return true;
			} else {
				finalAnimation = FarmingConstants.SPADE_ANIM;
				finalDelay = 3;
			}
		}
		final int animation = finalAnimation;
		player.setStopPacket(true);
		player.getUpdateFlags().sendAnimation(animation);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				player.getUpdateFlags().sendAnimation(animation);
				if (farmingStages[herbFieldsData.getHerbIndex()] <= 2) {
					farmingStages[herbFieldsData.getHerbIndex()]++;
					player.getInventory().addItemOrDrop(new Item(6055));
				} else {
					farmingStages[herbFieldsData.getHerbIndex()] = 3;
					container.stop();
				}
				player.getSkill().addExp(Skill.FARMING, CLEARING_EXPERIENCE);
				farmingTimer[herbFieldsData.getHerbIndex()] = Server.getMinutesCounter();
				updateHerbsStates();
				if (farmingStages[herbFieldsData.getHerbIndex()] == 3) {
					container.stop();
					return;
				}
			}

			@Override
			public void stop() {
				resetHerbs(herbFieldsData.getHerbIndex());
				player.getActionSender().sendMessage("You clear the patch.");
				player.setStopPacket(false);
				player.resetAnimation();
			}
		}, finalDelay);
		return true;

	}

	/* planting the seeds */

	public boolean plantSeed(int objectX, int objectY, final int seedId) {
		final HerbFieldsData herbFieldsData = HerbFieldsData.forIdPosition(new Position(objectX, objectY));
		final HerbData herbData = HerbData.forId(seedId);
		if (herbFieldsData == null || herbData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (farmingStages[herbFieldsData.getHerbIndex()] != 3) {
			player.getActionSender().sendMessage("You can't plant a seed here.");
			return false;
		}
		if (herbData.getLevelRequired() > player.getSkill().getLevel()[Skill.FARMING]) {
			player.getDialogue().sendStatement("You need a farming level of " + herbData.getLevelRequired() + " to plant this seed.");
			return true;
		}
		if (!player.getInventory().getItemContainer().contains(FarmingConstants.SEED_DIBBER)) {
			player.getDialogue().sendStatement("You need a seed dibber to plant seed here.");
			return true;
		}
		player.getUpdateFlags().sendAnimation(FarmingConstants.SEED_DIBBING);
		player.getInventory().removeItem(new Item(seedId));

		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {

			public void execute(CycleEventContainer container) {
				farmingState[herbFieldsData.getHerbIndex()] = 0;
				farmingStages[herbFieldsData.getHerbIndex()] = 4;
				farmingSeeds[herbFieldsData.getHerbIndex()] = seedId;
				farmingTimer[herbFieldsData.getHerbIndex()] = Server.getMinutesCounter();
				player.getSkill().addExp(Skill.FARMING, herbData.getPlantingXp());
				container.stop();
			}

			@Override
			public void stop() {
				updateHerbsStates();
				player.setStopPacket(false);
				player.resetAnimation();
			}
		}, 3);
		return true;
	}

	@SuppressWarnings("unused")
	private void displayAll() {
		for (int i = 0; i < farmingStages.length; i++) {
			System.out.println("index : " + i);
			System.out.println("state : " + farmingState[i]);
			System.out.println("harvest : " + farmingHarvest[i]);
			System.out.println("seeds : " + farmingSeeds[i]);
			System.out.println("level : " + farmingStages[i]);
			System.out.println("timer : " + farmingTimer[i]);
			System.out.println("disease chance : " + diseaseChance[i]);
			System.out.println("-----------------------------------------------------------------");
		}
	}

	/* harvesting the plant resulted */

	public boolean harvest(int objectX, int objectY) {
		final HerbFieldsData herbFieldsData = HerbFieldsData.forIdPosition(new Position(objectX, objectY));
		if (herbFieldsData == null) {
			return false;
		}
		final HerbData herbData = HerbData.forId(farmingSeeds[herbFieldsData.getHerbIndex()]);
		if (herbData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (!player.getInventory().getItemContainer().contains(FarmingConstants.SPADE)) {
			player.getDialogue().sendStatement("You need a spade to harvest here.");
			return true;
		}

		final int task = player.getTask();
		player.getUpdateFlags().sendAnimation(FarmingConstants.PICKING_VEGETABLE_ANIM);
		player.setSkilling(new CycleEvent() {

			public void execute(CycleEventContainer container) {
				if (farmingHarvest[herbFieldsData.getHerbIndex()] == 0) {
					farmingHarvest[herbFieldsData.getHerbIndex()] = (int) (1 + (START_HARVEST_AMOUNT + Misc.random(END_HARVEST_AMOUNT - START_HARVEST_AMOUNT)) * (player.getEquipment().getItemContainer().contains(7409) ? 1.10 : 1));
				}

				if (farmingHarvest[herbFieldsData.getHerbIndex()] == 1) {
					resetHerbs(herbFieldsData.getHerbIndex());
					farmingStages[herbFieldsData.getHerbIndex()] = 3;
					farmingTimer[herbFieldsData.getHerbIndex()] = Server.getMinutesCounter();
					container.stop();
					return;
				}
				if (!player.checkTask(task) || player.getInventory().getItemContainer().freeSlots() <= 0) {
					container.stop();
					return;
				}
				farmingHarvest[herbFieldsData.getHerbIndex()]--;
				player.getUpdateFlags().sendAnimation(FarmingConstants.PICKING_HERB_ANIM);
				player.getActionSender().sendMessage("You harvest the crop, and get some herbs.");
				player.getInventory().addItem(new Item(herbData.getHarvestId()));
				player.getSkill().addExp(Skill.FARMING, herbData.getHarvestXp());
			}

			@Override
			public void stop() {
				updateHerbsStates();
				player.resetAnimation();
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 3);
		return true;
	}

	/* putting compost onto the plant */

	public boolean putCompost(int objectX, int objectY, final int itemId) {
		if (itemId != 6032 && itemId != 6034) {
			return false;
		}
		final HerbFieldsData herbFieldsData = HerbFieldsData.forIdPosition(new Position(objectX, objectY));
		if (herbFieldsData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (farmingStages[herbFieldsData.getHerbIndex()] != 3 || farmingState[herbFieldsData.getHerbIndex()] == 4) {
			player.getActionSender().sendMessage("This patch doesn't need compost.");
			return true;
		}
		player.getInventory().removeItem(new Item(itemId));
		player.getInventory().addItem(new Item(1925));

		player.getActionSender().sendMessage("You pour some " + (itemId == 6034 ? "super" : "") + "compost on the patch.");
		player.getUpdateFlags().sendAnimation(FarmingConstants.PUTTING_COMPOST);
		player.getSkill().addExp(Skill.FARMING, itemId == 6034 ? Compost.SUPER_COMPOST_EXP_USE : Compost.COMPOST_EXP_USE);

		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {

			public void execute(CycleEventContainer container) {
				diseaseChance[herbFieldsData.getHerbIndex()] *= itemId == 6032 ? COMPOST_CHANCE : SUPERCOMPOST_CHANCE;
				farmingState[herbFieldsData.getHerbIndex()] = 4;
				container.stop();
			}

			@Override
			public void stop() {
				player.setStopPacket(false);
				player.resetAnimation();
			}
		}, 7);
		return true;
	}

	/* inspecting a plant */

	public boolean inspect(int objectX, int objectY) {
		final HerbFieldsData herbFieldsData = HerbFieldsData.forIdPosition(new Position(objectX, objectY));
		if (herbFieldsData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		final InspectData inspectData = InspectData.forId(farmingSeeds[herbFieldsData.getHerbIndex()]);
		final HerbData herbData = HerbData.forId(farmingSeeds[herbFieldsData.getHerbIndex()]);
		if (farmingState[herbFieldsData.getHerbIndex()] == 1) {
			player.getDialogue().sendStatement("This plant is diseased. Use a plant cure on it to cure it, ", "or clear the patch with a spade.");
			return true;
		} else if (farmingState[herbFieldsData.getHerbIndex()] == 2) {
			player.getDialogue().sendStatement("This plant is dead. You did not cure it while it was diseased.", "Clear the patch with a spade.");
			return true;
		}
		if (farmingStages[herbFieldsData.getHerbIndex()] == 0) {
			player.getDialogue().sendStatement("This is an herb patch. The soil has not been treated.", "The patch needs weeding.");
		} else if (farmingStages[herbFieldsData.getHerbIndex()] == 3) {
			player.getDialogue().sendStatement("This is an herb patch. The soil has not been treated.", "The patch is empty and weeded.");
		} else if (inspectData != null && herbData != null) {
			player.getActionSender().sendMessage("You bend down and start to inspect the patch...");

			player.getUpdateFlags().sendAnimation(1331);
			player.setStopPacket(true);
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {

				public void execute(CycleEventContainer container) {
					if (farmingStages[herbFieldsData.getHerbIndex()] - 4 < inspectData.getMessages().length - 2) {
						player.getDialogue().sendStatement(inspectData.getMessages()[farmingStages[herbFieldsData.getHerbIndex()] - 4]);
					} else if (farmingStages[herbFieldsData.getHerbIndex()] < herbData.getEndingState() - herbData.getStartingState() + 2) {
						player.getDialogue().sendStatement(inspectData.getMessages()[inspectData.getMessages().length - 2]);
					} else {
						player.getDialogue().sendStatement(inspectData.getMessages()[inspectData.getMessages().length - 1]);
					}
					container.stop();
				}

				@Override
				public void stop() {
					player.getUpdateFlags().sendAnimation(1332);
					player.setStopPacket(false);
					player.resetAnimation();
				}
			}, 5);
		}
		return true;
	}

	/* opening the corresponding guide about the patch */

	public boolean guide(int objectX, int objectY) {
		final HerbFieldsData herbFieldsData = HerbFieldsData.forIdPosition(new Position(objectX, objectY));
		if (herbFieldsData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		player.getSkillGuide().farmingComplex(7);
		player.getSkillGuide().selected = 20;
		return true;
	}

	/* Curing the plant */

	public boolean curePlant(int objectX, int objectY, int itemId) {
		final HerbFieldsData herbFieldsData = HerbFieldsData.forIdPosition(new Position(objectX, objectY));
		if (herbFieldsData == null || itemId != 6036) {
			return false;
		}
		final HerbData herbData = HerbData.forId(farmingSeeds[herbFieldsData.getHerbIndex()]);
		if (herbData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (farmingState[herbFieldsData.getHerbIndex()] != 1) {
			player.getActionSender().sendMessage("This plant doesn't need to be cured.");
			return true;
		}
		player.getInventory().removeItem(new Item(itemId));
		player.getInventory().addItem(new Item(229));
		player.getUpdateFlags().sendAnimation(FarmingConstants.CURING_ANIM);
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {

			public void execute(CycleEventContainer container) {
				player.getActionSender().sendMessage("You cure the plant with a plant cure.");
				farmingState[herbFieldsData.getHerbIndex()] = 0;
				container.stop();
			}

			@Override
			public void stop() {
				updateHerbsStates();
				player.setStopPacket(false);
				player.resetAnimation();
			}
		}, 7);

		return true;

	}

	@SuppressWarnings("unused")
	private void resetHerbs() {
		for (int i = 0; i < farmingStages.length; i++) {
			farmingSeeds[i] = 0;
			farmingState[i] = 0;
			diseaseChance[i] = 0;
			farmingHarvest[i] = 0;
		}
	}

	/* reseting the patches */

	private void resetHerbs(int index) {
		farmingSeeds[index] = 0;
		farmingState[index] = 0;
		diseaseChance[index] = 1;
		farmingHarvest[index] = 0;
	}

	/* checking if the patch is raked */

	public boolean checkIfRaked(int objectX, int objectY) {
		final HerbFieldsData herbFieldsData = HerbFieldsData.forIdPosition(new Position(objectX, objectY));
		if (herbFieldsData == null)
			return false;
		if (farmingStages[herbFieldsData.getHerbIndex()] == 3)
			return true;
		return false;
	}

	public int[] getFarmingStages() {
		return farmingStages;
	}

	public void setFarmingStages(int i, int allotmentStages) {
		this.farmingStages[i] = allotmentStages;
	}

	public int[] getFarmingSeeds() {
		return farmingSeeds;
	}

	public void setFarmingSeeds(int i, int allotmentSeeds) {
		this.farmingSeeds[i] = allotmentSeeds;
	}

	public int[] getFarmingHarvest() {
		return farmingHarvest;
	}

	public void setFarmingHarvest(int i, int allotmentHarvest) {
		this.farmingHarvest[i] = allotmentHarvest;
	}

	public int[] getFarmingState() {
		return farmingState;
	}

	public void setFarmingState(int i, int allotmentState) {
		this.farmingState[i] = allotmentState;
	}

	public long[] getFarmingTimer() {
		return farmingTimer;
	}

	public void setFarmingTimer(int i, long allotmentTimer) {
		this.farmingTimer[i] = allotmentTimer;
	}

	public double[] getDiseaseChance() {
		return diseaseChance;
	}

	public void setDiseaseChance(int i, double diseaseChance) {
		this.diseaseChance[i] = diseaseChance;
	}

}
