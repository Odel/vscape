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
public class Bushes {

	private Player player;

	// set of global constants for Farming

	private static final double COMPOST_CHANCE = 0.9;
	private static final double SUPERCOMPOST_CHANCE = 0.7;
	private static final double CLEARING_EXPERIENCE = 4;

	public Bushes(Player player) {
		this.player = player;
	}

	// Farming data
	public int[] farmingStages = new int[4];
	public int[] farmingSeeds = new int[4];
	public int[] farmingHarvest = new int[4];
	public int[] farmingState = new int[4];
	public long[] farmingTimer = new long[4];
	public double[] diseaseChance = {1, 1, 1, 1};
	public boolean[] hasFullyGrown = {false, false, false, false};
	public boolean[] bushesWatched = {false, false, false, false};

	/* set of the constants for the patch */

	// states - 2 bits plant - 6 bits
	public static final int GROWING = 0x00;
	public static final int DISEASED = 0x01;
	public static final int DEAD = 0x02;
	public static final int CHECK = 0x03;

	public static final int MAIN_BUSHES_CONFIG = 509;

	/* This is the enum holding the seeds info */

	public enum BushesData {
		REDBERRY(5101, 1951, 1, 10, new int[]{5478, 4}, 100, 0.20, 11.5, 4.5, 0x05, 0x0e, 0x09, 0x3a, 64), 
		CADAVABERRY(5102, 753, 1, 22, new int[]{5968, 3}, 120, 0.20, 18, 7, 0x0f, 0x19, 0x14, 0x3b, 102.5), 
		DWELLBERRY(5103, 2126, 1, 36, new int[]{5406, 3}, 140, 0.20, 31.5, 12, 0x1a, 0x25, 0x20, 0x3c, 177.5), 
		JANGERBERRY(5104, 247, 1, 48, new int[]{5982, 6}, 160, 0.20, 50.5, 19, 0x26, 0x32, 0x2d, 0x3d, 284.5), 
		WHITEBERRY(5105, 239, 1, 59, new int[]{6004, 8}, 160, 0.20, 78, 29, 0x33, 0x3f, 0x3a, 0x3e, 437.5),
		POISONIVYBERRY(5106, 6018, 1, 70, null, 160, 0.20, 160, 45, 0xc5, 0xd1, 0xcc, 0x3f, 674);

		private int seedId;
		private int harvestId;
		private int seedAmount;
		private int levelRequired;
		private int[] paymentToWatch;
		private int growthTime;
		private double diseaseChance;
		private double plantingXp;
		private double harvestXp;
		private int startingState;
		private int endingState;
		private int limitState;
		private int checkHealthState;
		private double checkHealthExperience;

		private static Map<Integer, BushesData> seeds = new HashMap<Integer, BushesData>();

		static {
			for (BushesData data : BushesData.values()) {
				seeds.put(data.seedId, data);
			}
		}

		BushesData(int seedId, int harvestId, int seedAmount, int levelRequired, int[] paymentToWatch, int growthTime, double diseaseChance, double plantingXp, double harvestXp, int startingState, int endingState, int limitState, int checkHealthState, double checkHealthExperience) {
			this.seedId = seedId;
			this.harvestId = harvestId;
			this.seedAmount = seedAmount;
			this.levelRequired = levelRequired;
			this.paymentToWatch = paymentToWatch;
			this.growthTime = growthTime;
			this.diseaseChance = diseaseChance;
			this.plantingXp = plantingXp;
			this.harvestXp = harvestXp;
			this.startingState = startingState;
			this.endingState = endingState;
			this.limitState = limitState;
			this.checkHealthState = checkHealthState;
			this.checkHealthExperience = checkHealthExperience;
		}

		public static BushesData forId(int seedId) {
			return seeds.get(seedId);
		}

		public int getSeedId() {
			return seedId;
		}

		public int getHarvestId() {
			return harvestId;
		}

		public int getSeedAmount() {
			return seedAmount;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public int[] getPaymentToWatch() {
			return paymentToWatch;
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

		public int getLimitState() {
			return limitState;
		}

		public int getCheckHealthState() {
			return checkHealthState;
		}

		public double getCheckHealthXp() {
			return checkHealthExperience;
		}
	}

	/* This is the enum data about the different patches */

	public enum BushesFieldsData {
		ETCETERIA(0, new Position[]{new Position(2591, 3863), new Position(2592, 3864)}, 2337), 
		SOUTH_ARDOUGNE(1, new Position[]{new Position(2617, 3225), new Position(2618, 3226)}, 2338), 
		CHAMPION_GUILD(2, new Position[]{new Position(3181, 3357), new Position(3182, 3358)}, 2335),
		RIMMINGTON(3, new Position[]{new Position(2940, 3221), new Position(2941, 3222)}, 2336);

		private int bushesIndex;
		private Position[] bushesPosition;
		private int npcId;

		private static Map<Integer, BushesFieldsData> npcsProtecting = new HashMap<Integer, BushesFieldsData>();

		static {
			for (BushesFieldsData data : BushesFieldsData.values()) {
				npcsProtecting.put(data.npcId, data);

			}
		}

		public static BushesFieldsData forId(int npcId) {
			return npcsProtecting.get(npcId);
		}

		BushesFieldsData(int bushesIndex, Position[] bushesPosition, int npcId) {
			this.bushesIndex = bushesIndex;
			this.bushesPosition = bushesPosition;
			this.npcId = npcId;
		}

		public static BushesFieldsData forIdPosition(Position position) {
			for (BushesFieldsData bushesFieldsData : BushesFieldsData.values()) {
				if (FarmingConstants.inRangeArea(bushesFieldsData.getBushesPosition()[0], bushesFieldsData.getBushesPosition()[1], position)) {
					return bushesFieldsData;
				}
			}
			return null;
		}

		public int getBushesIndex() {
			return bushesIndex;
		}

		public Position[] getBushesPosition() {
			return bushesPosition;
		}

		public int getNpcId() {
			return npcId;
		}
	}

	/* This is the enum that hold the different data for inspecting the plant */

	public enum InspectData {

		REDBERRY(5101, new String[][]{{"The Redberry seeds have only just been planted."}, {"The Redberry bush grows larger."}, {"The Redberry bush grows larger."}, {"The Redberry bush grows small, unripe,", "green berries."}, {"The berries grow larger, and pink."}, {"The Redberry bush is ready to harvest.", "The berries on the bush are red."},}), CADAVABERRY(5102, new String[][]{{"The Cadavaberry seeds have only just been planted."}, {"The Cadavaberry bush grows larger."}, {"The Cadavaberry bush grows larger."}, {"The Cadavaberry bush grows larger."}, {"The Cadavaberry bush grows small, unripe,", "green berries."}, {"The berries grow larger, and pink."}, {"The Cadavaberry bush is ready to harvest.", "The berries on the bush are purple."}}), DWELLBERRY(5103, new String[][]{{"The Dwellbery seeds have only just been planted."}, {"The Dwellbery bush grows larger."}, {"The Dwellbery bush grows larger."}, {"The Dwellbery bush grows larger."}, {"The Dwellbery bush grows larger."},
				{"The Dwellbery bush grows small, unripe,", "green berries."}, {"The berries grow larger, and light blue."}, {"The Dwellbery bush is ready to harvest.", "The berries on the bush are blue."},}), JANGERBERRY(5104, new String[][]{{"The Jangerberry seeds have only just been planted."}, {"The Jangerberry bush grows larger."}, {"The Jangerberry bush grows larger."}, {"The Jangerberry bush grows larger."}, {"The Jangerberry bush grows larger."}, {"The Jangerberry bush grows small, unripe,", "green berries."}, {"The berries grow larger."}, {"The berries grow larger, and light green."}, {"The Jangerberry bush is ready to harvest.", "The berries on the bush are green."}}), WHITEBERRY(5105, new String[][]{{"The Whiteberry seeds have only just been planted."}, {"The Whiteberry bush grows larger."}, {"The Whiteberry bush grows larger."}, {"The Whiteberry bush grows larger."}, {"The Whiteberry bush grows larger."}, {"The Whiteberry bush grows larger."},
				{"The Whiteberry bush grows small, unripe,", "green berries."}, {"The berries grow larger."}, {"The Whiteberry bush is ready to harvest.", "The berries on the bush are white."},}), POISONIVYBERRY(5106, new String[][]{{"The Poison ivy seeds have only just been planted."}, {"The Poison ivy bush grows larger."}, {"The Poison ivy bush grows larger."}, {"The Poison ivy bush grows larger."}, {"The Poison ivy bush grows larger."}, {"The Poison ivy bush grows small, unripe,", "green berries."}, {"The berries grow larger."}, {"The berries grow larger, and light green."}, {"The Poison ivy bush is ready to harvest.", "The berries on the bush are pale yellow."}});
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
					updateBushesStates();
					continue;
				}
			}
			BushesData bushesData = BushesData.forId(farmingSeeds[i]);
			if (bushesData == null) {
				continue;
			}
			long growthTimeTotal = bushesData.getGrowthTime();
			int totalStages = (bushesData.getEndingState() - bushesData.getStartingState());
			long growthTimePerStage = (growthTimeTotal / (totalStages - 4));
			int nextStage = farmingStages[i] + 1;
			//if timer is 0 or if the plant is dead or fully grown go to next Bush index insted
			if (farmingTimer[i] == 0 || farmingState[i] == 2 || farmingState[i] == 3 || (nextStage > totalStages)) {
				continue;
			}
			if(difference >= growthTimePerStage) //in growth stage time (20 minutes for bushes)
			{
				if (nextStage != farmingStages[i]) {
					if (farmingStages[i] == (totalStages - 1)) {
						farmingStages[i] = totalStages + 4;
						farmingState[i] = 3;
						farmingTimer[i] = Server.getMinutesCounter();
						updateBushesStates();
						return;
					}
					farmingStages[i] = nextStage;
					if (farmingStages[i] <= nextStage)
					{
						for (int j = farmingStages[i]; j <=nextStage; j++){
							processState(i);
						}
					}
					farmingTimer[i] = Server.getMinutesCounter();
					updateBushesStates();
				}
			}
		}
	}
	
	public void processState(int index)
	{
		if (farmingState[index] == 2) {
			return;
		}
		// if the patch is diseased, it dies, if its watched by a farmer, it
		// goes back to normal
		if (farmingState[index] == 1) {
			if (bushesWatched[index]) {
				farmingState[index] = 0;
				BushesData bushesData = BushesData.forId(farmingSeeds[index]);
				if (bushesData == null)
					return;
				farmingTimer[index] = Server.getMinutesCounter();
				modifyStage(index);
			} else {
				farmingState[index] = 2;
			}
		}

		if (farmingState[index] == 5 && farmingStages[index] != 2) {
			farmingState[index] = 0;
		}

		if (farmingState[index] == 0 && farmingStages[index] >= 5 && !hasFullyGrown[index]) {
			BushesData bushesData = BushesData.forId(farmingSeeds[index]);
			if (bushesData == null) {
				return;
			}

			double chance = diseaseChance[index] * bushesData.getDiseaseChance();
			int maxChance = (int)(chance * 100);
			if (Misc.random(100) <= maxChance) {
				farmingState[index] = 1;
			}
		}
	}
	
	public void modifyStage(int i) {
		BushesData bushesData = BushesData.forId(farmingSeeds[i]);
		if (bushesData == null)
			return;
		int totalStages = (bushesData.getEndingState() - bushesData.getStartingState());
		int nextStage = farmingStages[i] + 1;
		if(nextStage > totalStages)
			return;
		farmingStages[i] = nextStage;
		updateBushesStates();
	}

	public void lowerStage(int index) {
		BushesData bushesData = BushesData.forId(farmingSeeds[index]);
		if (bushesData == null)
			return;
		int totalStages = (bushesData.getEndingState() - bushesData.getStartingState());
		hasFullyGrown[index] = false;
		farmingTimer[index] = Server.getMinutesCounter();
		int lowerBy = 1;
		if(farmingStages[index] == totalStages + 1)
		{
			lowerBy = 4;
		}
		farmingStages[index] -= lowerBy;
		updateBushesStates();
	}
	
	/* update all the patch states */
	public void updateBushesStates() {
		// etceteria - south ardougne - champion guild - rimmington
		int[] configValues = new int[farmingStages.length];

		int configValue;
		for (int i = 0; i < farmingStages.length; i++) {
			configValues[i] = getConfigValue(farmingStages[i], farmingSeeds[i], farmingState[i], i);
		}

		configValue = (configValues[0] << 16) + (configValues[1] << 8 << 16) + configValues[2] + (configValues[3] << 8);
		player.getActionSender().sendConfig(MAIN_BUSHES_CONFIG, configValue);
	}

	/* getting the different config values */

	public int getConfigValue(int bushesStage, int seedId, int plantState, int index) {
		BushesData bushesData = BushesData.forId(seedId);
		switch (bushesStage) {
			case 0 :// weed
				return (GROWING << 6) + 0x00;
			case 1 :// weed cleared
				return (GROWING << 6) + 0x01;
			case 2 :
				return (GROWING << 6) + 0x02;
			case 3 :
				return (GROWING << 6) + 0x03;
		}
		if (bushesData == null) {
			return -1;
		}
		if (bushesStage > bushesData.getEndingState() - bushesData.getStartingState() - 1) {
			hasFullyGrown[index] = true;
		}
		if (getPlantState(plantState) == 3)
			return (getPlantState(plantState) << 6) + bushesData.getCheckHealthState();
		if (seedId == 5106) {
			if (getPlantState(plantState) == 1) {
				return bushesData.getStartingState() + bushesStage - 4 + 12;
			} else if (getPlantState(plantState) == 2) {
				return bushesData.getStartingState() + bushesStage - 4 + 20;
			}
		}
		return (getPlantState(plantState) << 6) + bushesData.getStartingState() + bushesStage - 4 + (getPlantState(plantState) == 2 ? -1 : 0);
	}

	/* getting the plant states */

	public int getPlantState(int plantState) {
		switch (plantState) {
			case 0 :
				return GROWING;
			case 1 :
				return DISEASED;
			case 2 :
				return DEAD;
			case 3 :
				return CHECK;
		}
		return -1;
	}

	/* clearing the patch with a rake of a spade */
	public boolean clearPatch(int objectX, int objectY, int itemId) {
		final BushesFieldsData bushesFieldsData = BushesFieldsData.forIdPosition(new Position(objectX, objectY));
		int finalAnimation;
		int finalDelay;
		if (bushesFieldsData == null || (itemId != FarmingConstants.RAKE && itemId != FarmingConstants.SPADE)) {
			return false;
		}
		if (farmingStages[bushesFieldsData.getBushesIndex()] == 3) {
			return true;
		}
		if (!Constants.FARMING_ENABLED || !Constants.FARMING_BUSHES_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (farmingStages[bushesFieldsData.getBushesIndex()] <= 3) {
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
				if (farmingStages[bushesFieldsData.getBushesIndex()] <= 2) {
					farmingStages[bushesFieldsData.getBushesIndex()]++;
					player.getInventory().addItemOrDrop(new Item(6055));
				} else {
					farmingStages[bushesFieldsData.getBushesIndex()] = 3;
					container.stop();
				}
				player.getSkill().addExp(Skill.FARMING, CLEARING_EXPERIENCE);
				farmingTimer[bushesFieldsData.getBushesIndex()] = Server.getMinutesCounter();
				updateBushesStates();
				if (farmingStages[bushesFieldsData.getBushesIndex()] == 3) {
					container.stop();
					return;
				}
			}

			@Override
			public void stop() {
				resetBushes(bushesFieldsData.getBushesIndex());
				player.getActionSender().sendMessage("You clear the patch.");
				player.setStopPacket(false);
				player.resetAnimation();
			}
		}, finalDelay);
		return true;

	}

	/* planting the seeds */

	public boolean plantSeed(int objectX, int objectY, final int seedId) {
		final BushesFieldsData bushesFieldsData = BushesFieldsData.forIdPosition(new Position(objectX, objectY));
		final BushesData bushesData = BushesData.forId(seedId);
		if (bushesFieldsData == null || bushesData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED || !Constants.FARMING_BUSHES_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (farmingStages[bushesFieldsData.getBushesIndex()] != 3) {
			player.getActionSender().sendMessage("You can't plant a seed here.");
			return false;
		}
		if (bushesData.getLevelRequired() > player.getSkill().getLevel()[Skill.FARMING]) {
			player.getDialogue().sendStatement("You need a farming level of " + bushesData.getLevelRequired() + " to plant this seed.");
			return true;
		}
		if (!player.getInventory().getItemContainer().contains(FarmingConstants.SEED_DIBBER)) {
			player.getDialogue().sendStatement("You need a seed dibber to plant seed here.");
			return true;
		}
		if (player.getInventory().getItemAmount(bushesData.getSeedId()) < bushesData.getSeedAmount()) {
			player.getDialogue().sendStatement("You need atleast " + bushesData.getSeedAmount() + " seeds to plant here.");
			return true;
		}
		player.getUpdateFlags().sendAnimation(FarmingConstants.SEED_DIBBING);
		farmingStages[bushesFieldsData.getBushesIndex()] = 4;
		player.getInventory().removeItem(new Item(seedId, bushesData.getSeedAmount()));

		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				farmingState[bushesFieldsData.getBushesIndex()] = 0;
				farmingSeeds[bushesFieldsData.getBushesIndex()] = seedId;
				farmingTimer[bushesFieldsData.getBushesIndex()] = Server.getMinutesCounter();
				player.getSkill().addExp(Skill.FARMING, bushesData.getPlantingXp());
				container.stop();
			}

			@Override
			public void stop() {
				updateBushesStates();
				player.setStopPacket(false);
			}
		}, 3);
		return true;
	}

	/* harvesting the plant resulted */
	public boolean harvestOrCheckHealth(int objectX, int objectY) {
		final BushesFieldsData bushesFieldsData = BushesFieldsData.forIdPosition(new Position(objectX, objectY));
		if (bushesFieldsData == null) {
			return false;
		}
		final BushesData bushesData = BushesData.forId(farmingSeeds[bushesFieldsData.getBushesIndex()]);
		if (bushesData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED || !Constants.FARMING_BUSHES_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (player.getInventory().getItemContainer().freeSlots() <= 0) {
			player.getActionSender().sendMessage("Not enough space in your inventory.");
			return true;
		}
		player.getUpdateFlags().sendAnimation(832);
		final int task = player.getTask();
		player.setSkilling(new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task) || player.getInventory().getItemContainer().freeSlots() <= 0) {
					container.stop();
					return;
				}

				if (farmingState[bushesFieldsData.getBushesIndex()] == 3) {
					player.getActionSender().sendMessage("You examine the bush for signs of disease and find that it's in perfect health.");
					player.getSkill().addExp(Skill.FARMING, bushesData.getCheckHealthXp());
					farmingState[bushesFieldsData.getBushesIndex()] = 0;
					hasFullyGrown[bushesFieldsData.getBushesIndex()] = false;
					farmingTimer[bushesFieldsData.getBushesIndex()] = Server.getMinutesCounter();
					modifyStage(bushesFieldsData.getBushesIndex());
					container.stop();
					return;
				}
				player.getActionSender().sendMessage("You harvest the crop, and pick some berries.");
				player.getInventory().addItem(new Item(bushesData.getHarvestId()));
				player.getSkill().addExp(Skill.FARMING, bushesData.getHarvestXp());
		    	lowerStage(bushesFieldsData.getBushesIndex());
				container.stop();
			}

			@Override
			public void stop() {
				player.resetAnimation();
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 2);
		return true;
	}

	/* putting compost onto the plant */
	public boolean putCompost(int objectX, int objectY, final int itemId) {
		if (itemId != 6032 && itemId != 6034) {
			return false;
		}
		final BushesFieldsData bushesFieldsData = BushesFieldsData.forIdPosition(new Position(objectX, objectY));
		if (bushesFieldsData == null) {
			return false;
		}

		if (farmingStages[bushesFieldsData.getBushesIndex()] != 3 || farmingState[bushesFieldsData.getBushesIndex()] == 5) {
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

			@Override
			public void execute(CycleEventContainer container) {
				diseaseChance[bushesFieldsData.getBushesIndex()] *= itemId == 6032 ? COMPOST_CHANCE : SUPERCOMPOST_CHANCE;
				farmingState[bushesFieldsData.getBushesIndex()] = 5;
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
		final BushesFieldsData bushesFieldsData = BushesFieldsData.forIdPosition(new Position(objectX, objectY));
		if (bushesFieldsData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED || !Constants.FARMING_BUSHES_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		final InspectData inspectData = InspectData.forId(farmingSeeds[bushesFieldsData.getBushesIndex()]);
		final BushesData bushesData = BushesData.forId(farmingSeeds[bushesFieldsData.getBushesIndex()]);
		if (farmingState[bushesFieldsData.getBushesIndex()] == 1) {
			player.getDialogue().sendStatement("This plant is diseased. Use a plant cure on it to cure it, ", "or clear the patch with a spade.");
			return true;
		} else if (farmingState[bushesFieldsData.getBushesIndex()] == 2) {
			player.getDialogue().sendStatement("This plant is dead. You did not cure it while it was diseased.", "Clear the patch with a spade.");
			return true;
		} else if (farmingState[bushesFieldsData.getBushesIndex()] == 3) {
			player.getDialogue().sendStatement("This plant has fully grown. You can check it's health", "to gain some farming experiences.");
			return true;
		}
		if (farmingStages[bushesFieldsData.getBushesIndex()] == 0) {
			player.getDialogue().sendStatement("This is a bush patch. The soil has not been treated.", "The patch needs weeding.");
		} else if (farmingStages[bushesFieldsData.getBushesIndex()] == 3) {
			player.getDialogue().sendStatement("This is a bush patch. The soil has not been treated.", "The patch is empty and weeded.");
		} else if (inspectData != null && bushesData != null) {
			player.getActionSender().sendMessage("You bend down and start to inspect the patch...");

			player.getUpdateFlags().sendAnimation(1331);
			player.setStopPacket(true);
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {

				@Override
				public void execute(CycleEventContainer container) {
					if (farmingStages[bushesFieldsData.getBushesIndex()] - 4 < inspectData.getMessages().length - 2) {
						player.getDialogue().sendStatement(inspectData.getMessages()[farmingStages[bushesFieldsData.getBushesIndex()] - 4]);
					} else if (farmingStages[bushesFieldsData.getBushesIndex()] < bushesData.getEndingState() - bushesData.getStartingState() + 2) {
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
					// player.reset();
				}
			}, 5);
		}
		return true;
	}

	/* opening the corresponding guide about the patch */

	public boolean guide(int objectX, int objectY) {
		final BushesFieldsData bushesFieldsData = BushesFieldsData.forIdPosition(new Position(objectX, objectY));
		if (bushesFieldsData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED || !Constants.FARMING_BUSHES_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		player.getSkillGuide().farmingComplex(5);
		player.getSkillGuide().selected = 20;
		return true;
	}

	/* Curing the plant */

	public boolean curePlant(int objectX, int objectY, int itemId) {
		final BushesFieldsData bushesFieldsData = BushesFieldsData.forIdPosition(new Position(objectX, objectY));
		if (bushesFieldsData == null || itemId != 6036) {
			return false;
		}
		final BushesData bushesData = BushesData.forId(farmingSeeds[bushesFieldsData.getBushesIndex()]);
		if (bushesData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED || !Constants.FARMING_BUSHES_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (farmingState[bushesFieldsData.getBushesIndex()] != 1) {
			player.getActionSender().sendMessage("This plant doesn't need to be cured.");
			return true;
		}
		player.getInventory().removeItem(new Item(itemId));
		player.getInventory().addItem(new Item(229));
		player.getUpdateFlags().sendAnimation(FarmingConstants.CURING_ANIM);
		player.setStopPacket(true);
		farmingState[bushesFieldsData.getBushesIndex()] = 0;
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				player.getActionSender().sendMessage("You cure the plant with a plant cure.");
				container.stop();
			}

			@Override
			public void stop() {
				updateBushesStates();
				player.setStopPacket(false);
				player.resetAnimation();
			}
		}, 7);

		return true;

	}

	private void resetBushes(int index) {
		farmingSeeds[index] = 0;
		farmingState[index] = 0;
		diseaseChance[index] = 1;
		hasFullyGrown[index] = false;
		bushesWatched[index] = false;
	}

	/* checking if the patch is raked */

	public boolean checkIfRaked(int objectX, int objectY) {
		final BushesFieldsData bushesFieldData = BushesFieldsData.forIdPosition(new Position(objectX, objectY));
		if (bushesFieldData == null)
			return false;
		if (farmingStages[bushesFieldData.getBushesIndex()] == 3)
			return true;
		return false;
	}

	public int[] getFarmingStages() {
		return farmingStages;
	}

	public void setFarmingStages(int i, int bushesStages) {
		this.farmingStages[i] = bushesStages;
	}

	public int[] getFarmingSeeds() {
		return farmingSeeds;
	}

	public void setFarmingSeeds(int i, int bushesSeeds) {
		this.farmingSeeds[i] = bushesSeeds;
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

	public void setFarmingState(int i, int bushesState) {
		this.farmingState[i] = bushesState;
	}

	public long[] getFarmingTimer() {
		return farmingTimer;
	}

	public void setFarmingTimer(int i, long bushesTimer) {
		this.farmingTimer[i] = bushesTimer;
	}

	public double[] getDiseaseChance() {
		return diseaseChance;
	}

	public void setDiseaseChance(int i, double diseaseChance) {
		this.diseaseChance[i] = diseaseChance;
	}

	public boolean[] getFarmingWatched() {
		return bushesWatched;
	}

	public void setFarmingWatched(int i, boolean bushesWatched) {
		this.bushesWatched[i] = bushesWatched;
	}

}
