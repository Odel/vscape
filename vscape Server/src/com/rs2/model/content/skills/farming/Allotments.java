package com.rs2.model.content.skills.farming;

import java.util.ArrayList;
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
public class Allotments {

	private Player player;

	// set of global constants for Farming

	private static final int START_HARVEST_AMOUNT = 3;
	private static final int END_HARVEST_AMOUNT = 56;

	private static final double WATERING_CHANCE = 0.5;
	private static final double COMPOST_CHANCE = 0.9;
	private static final double SUPERCOMPOST_CHANCE = 0.7;
	private static final double CLEARING_EXPERIENCE = 4;

	public Allotments(Player player) {
		this.player = player;
	}

	// Farming data
	public int[] farmingStages = new int[8];
	public int[] farmingSeeds = new int[8];
	public int[] farmingHarvest = new int[8];
	public int[] farmingState = new int[8];
	public long[] farmingTimer = new long[8];
	public double[] diseaseChance = {1, 1, 1, 1, 1, 1, 1, 1};
	public boolean[] farmingWatched = {false, false, false, false, false, false, false, false};
	public boolean[] hasFullyGrown = {false, false, false, false, false, false, false, false};

	/* set of the constants for the patch */

	// states - 2 bits plant - 6 bits
	public static final int GROWING = 0x00;
	public static final int WATERED = 0x01;
	public static final int DISEASED = 0x02;
	public static final int DEAD = 0x03;

	public static final int FALADOR_AND_CATHERBY_CONFIG = 504;
	public static final int ARDOUGNE_AND_PHASMATYS_CONFIG = 505;

	/* This is the enum holding the seeds info */

	public enum AllotmentData {

		POTATO(5318, 1942, 5096, 3, 1, new int[]{6032, 2}, 40, 0.30, 8, 9.5, 0x06, 0x0c),
		ONION(5319, 1957, 5096, 3, 5, new int[]{5438, 1}, 40, 0.30, 9.5, 10.5, 0x0d, 0x13),
		CABBAGE(5324, 1965, 5097, 3, 7, new int[]{5458, 1}, 40, 0.25, 10, 11.5, 0x14, 0x1a), 
		TOMATO(5322, 1982, 5096, 3, 12, new int[]{5478, 2}, 40, 0.25, 12.5, 14, 0x1b, 0x21),
		SWEETCORN(5320, 5986, 6059, 3, 20, new int[]{5931, 10}, 60, 0.20, 17, 19, 0x22, 0x2a), 
		STRAWBERRY(5323, 5504, -1, 3, 31, new int[]{5386, 1}, 60, 0.20, 26, 29, 0x2b, 0x33),
		WATERMELON(5321, 5982, 5098, 3, 47, new int[]{5970, 10}, 80, 0.20, 48.5, 54.5, 0x34, 0x3e);

		private int seedId;
		private int harvestId;
		private int flowerProtect;
		private int seedAmount;
		private int levelRequired;
		private int[] paymentToWatch;
		private int growthTime;
		private double diseaseChance;
		private double plantingXp;
		private double harvestXp;
		private int startingState;
		private int endingState;

		private static Map<Integer, AllotmentData> seeds = new HashMap<Integer, AllotmentData>();

		static {
			for (AllotmentData data : AllotmentData.values()) {
				seeds.put(data.seedId, data);
			}
		}

		AllotmentData(int seedId, int harvestId, int flowerProtect, int seedAmount, int levelRequired, int[] paymentToWatch, int growthTime, double diseaseChance, double plantingXp, double harvestXp, int startingState, int endingState) {
			this.seedId = seedId;
			this.harvestId = harvestId;
			this.flowerProtect = flowerProtect;
			this.seedAmount = seedAmount;
			this.levelRequired = levelRequired;
			this.paymentToWatch = paymentToWatch;
			this.growthTime = growthTime;
			this.diseaseChance = diseaseChance;
			this.plantingXp = plantingXp;
			this.harvestXp = harvestXp;
			this.startingState = startingState;
			this.endingState = endingState;
		}

		public static AllotmentData forId(int seedId) {
			return seeds.get(seedId);
		}

		public int getSeedId() {
			return seedId;
		}

		public int getHarvestId() {
			return harvestId;
		}

		public int getFlowerProtect() {
			return flowerProtect;
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
	}

	/* This is the enum data about the different patches */

	public enum AllotmentFieldsData {

		CATHERBY_NORTH(0, new Position[]{new Position(2805, 3466), new Position(2806, 3468), new Position(2805, 3467), new Position(2814, 3468)}, 2324),

		CATHERBY_SOUTH(1, new Position[]{new Position(2805, 3459), new Position(2806, 3461), new Position(2802, 3459), new Position(2814, 3460)}, 2324),

		FALADOR_NORTH_WEST(2, new Position[]{new Position(3050, 3307), new Position(3051, 3312), new Position(3050, 3311), new Position(3054, 3312)}, 2323),

		FALADOR_SOUTH_EAST(3, new Position[]{new Position(3055, 3303), new Position(3059, 3304), new Position(3058, 3303), new Position(3059, 3308)}, 2323),

		PHASMATYS_NORTH_WEST(4, new Position[]{new Position(3597, 3525), new Position(3598, 3530), new Position(3597, 3529), new Position(3601, 3530)}, 2326),

		PHASMATYS_SOUTH_EAST(5, new Position[]{new Position(3602, 3521), new Position(3606, 3522), new Position(3605, 3521), new Position(3606, 3526)}, 2326),

		ARDOUGNE_NORTH(6, new Position[]{new Position(2662, 3377), new Position(2663, 3379), new Position(2662, 3378), new Position(2671, 3379)}, 2325),

		ARDOUGNE_SOUTH(7, new Position[]{new Position(2662, 3370), new Position(2663, 3372), new Position(2662, 3370), new Position(2671, 3371)}, 2325);

		private int allotmentIndex;
		private Position[] allotmentPosition;
		private int farmerBelonging;

		AllotmentFieldsData(int allotmentIndex, Position[] allotmentPosition, int farmerBelonging) {
			this.allotmentIndex = allotmentIndex;
			this.allotmentPosition = allotmentPosition;
			this.farmerBelonging = farmerBelonging;
		}

		public static AllotmentFieldsData forIdPosition(Position position) {
			for (AllotmentFieldsData allotmentFieldsData : AllotmentFieldsData.values()) {
				if (FarmingConstants.inRangeArea(allotmentFieldsData.getAllotmentPosition()[0], allotmentFieldsData.getAllotmentPosition()[1], position) || FarmingConstants.inRangeArea(allotmentFieldsData.getAllotmentPosition()[2], allotmentFieldsData.getAllotmentPosition()[3], position)) {
					return allotmentFieldsData;
				}
			}
			return null;
		}

		public static ArrayList<Integer> listIndexProtected(int npcId) {
			ArrayList<Integer> array = new ArrayList<Integer>();
			for (AllotmentFieldsData allotmentFieldsData : AllotmentFieldsData.values()) {
				if (allotmentFieldsData.getFarmerBelonging() == npcId)
					array.add(allotmentFieldsData.allotmentIndex);
			}
			return array;

		}

		public int getAllotmentIndex() {
			return allotmentIndex;
		}

		public Position[] getAllotmentPosition() {
			return allotmentPosition;
		}

		public int getFarmerBelonging() {
			return farmerBelonging;
		}
	}

	/* This is the enum that hold the different data for inspecting the plant */

	public enum InspectData {
		POTATOES(5318, new String[][]{{"The potato seeds have only just been planted."}, {"The potato plants have grown to double their", "previous height."}, {"The potato plants now are the same height as the", "surrounding weeds."}, {"The potato plants now spread their branches wider,", "not growing as much as before."}, {"The potato plants are ready to harvest. A white", "flower at the top of each plant opens up."}}), ONIONS(5319, new String[][]{{"The onion seeds have only just been planted."}, {"The onions are partially visible and the stems", "have grown."}, {"The top of the onion of the onion plant is clear", "above the ground and the onion is white."}, {"The onion plant is slightly larger than before and", "the onion is cream coloured."}, {"The onion stalks are larger than before and the", "onion is now light and brown coloured."}}), CABBAGES(5324, new String[][]{{"The cabbage seeds have only just been planted,", "the cabbages are small and bright green."},
				{"The cabbages are much larger, with more leaves", "surrounding the head."}, {"The cabbages are larger than before, and textures", "of leaves are now easily observable."}, {"The cabbage head has swollen larger, and the", "surrounding leaves are more close to the ground."}, {"The cabbage plants are ready to harvest. The", "centre of each cabbage head is light green coloured."}}), TOMATOES(5322, new String[][]{{"The tomato seeds have only just been planted."}, {"The tomato plants grow twice as large as before."}, {"The tomato plants grow larger, and small green", "tomatoes are now observable."}, {"The tomato plants grow thicker to hold up the", "weight of the tomatoes. The tomatoes are now light", "orange and slightly larger on the plant."}, {"The tomato plants are ready to harvest. The tomato", "plants leaves are larger and the tomatoes are", "ripe red."}}), SWEETCORNS(5320, new String[][]{{"The sweetcorn plants have only just been planted."},
				{"The sweetcorn plants are waist tall now and are", "leafy."}, {"The sweetcorn plants are slightly taller than", "before and slightly thicker."}, {"The sweetcorn leaves are larger at the base, and", "the plants are slightly taller."}, {"Closed corn cobs are now observable on the", "sweetcorn plants."}, {"The sweetcorn plants are ready to harvest. The", "corn cobs are open and visibly yellow."}}), STRAWBERRIES(5323, new String[][]{{"The strawberry seeds have only just been planted."}, {"The strawberry plants have more leaves than before."}, {"The strawberry plants have even more leaves and is", "slightly taller than before."}, {"Each strawberry plant has opened one white", "flower each."}, {"The strawberry plants are slightly larger, and", "have small strawberries visible at their bases."}, {"The strawberry plants are slightly larger, opened", "a second flower each, and have more strawberries."},
				{"The strawberry plants are ready to harvest. The", "strawberries are almost as large as the flowers."}}), WATERMELONS(5321, new String[][]{{"The watermelon seeds have only just been planted."}, {"The watermelon stems grow larger."}, {"The watermelon stems have even more leaves and is", "slightly larger than before."}, {"The watermelon stems have even more leaves and is", "slightly larger than before."}, {"The watermelon stems are slightly larger."}, {"The watermelon stems are slightly larger, and", "have small watermelons visible at their bases."}, {"The watermelons are ready to harvest. The", "watermelons are almost as large as the flowers."}});

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
					updateAllotmentsStates();
					continue;
				}
			}
			AllotmentData allotmentData = AllotmentData.forId(farmingSeeds[i]);
			if (allotmentData == null) {
				continue;
			}
			long growthTimeTotal = allotmentData.getGrowthTime();
			int totalStages = (allotmentData.getEndingState() - allotmentData.getStartingState()) + 2;
			long growthTimePerStage = (growthTimeTotal / (totalStages - 4));
			if(difference >= growthTimePerStage) //in growth stage time (10 minutes for allotments)
			{
				int nextStage = farmingStages[i] + 1;
				//if timer is 0 or if the plant is dead or fully grown go to next Allotment index insted
				if (farmingTimer[i] == 0 || farmingState[i] == 3 || (nextStage > totalStages)) {
					continue;
				}
				if (nextStage != farmingStages[i]) {
					farmingStages[i] = nextStage;
					if (farmingStages[i] <= nextStage){
						for (int j = farmingStages[i]; j <= nextStage; j++){
							processState(i);
						}
					}
					farmingTimer[i] = Server.getMinutesCounter();
					updateAllotmentsStates();
				}
			}
		}
	}
	
	public void processState(int index)
	{
		//plant is dead, So we return to stop processing
		if (farmingState[index] == 3) {
			return;
		}
		// if the patch is diseased, it dies, if its watched by a farmer, it goes back to normal
		if (farmingState[index] == 2) {
			//if watched grow
			if (farmingWatched[index]) {
				farmingState[index] = 0;
				AllotmentData allotmentData = AllotmentData.forId(farmingSeeds[index]);
				if (allotmentData == null)
					return;
				farmingTimer[index] = Server.getMinutesCounter();
				modifyStage(index);
			} else { // if not watched set state to dead
				farmingState[index] = 3;
			}
		}
		
		//if watered disease * 2
		if (farmingState[index] == 1) {
			diseaseChance[index] *= 2;
			farmingState[index] = 0;
		}

		//if state is compost and stage does not equal empty set state to growing
		if (farmingState[index] == 5 && farmingStages[index] != 3) {
			farmingState[index] = 0;
		}

		//if state growing, and stage is not a seed but a growing plant do disease
		if (farmingState[index] == 0 && farmingStages[index] >= 5 && !hasFullyGrown[index]) {
			processDisease(index);
		}
	}
	
	public void processDisease(int index)
	{
		AllotmentData allotmentData = AllotmentData.forId(farmingSeeds[index]);
		if (allotmentData == null) {
			return;
		}
		double chance = diseaseChance[index] * allotmentData.getDiseaseChance();
		int maxChance = (int)(chance * 100);
		int indexGiven = 0;
		if (!farmingWatched[index] && Misc.random(100) <= maxChance) {
			switch (index) {
				case 0 :
				case 1 :
					indexGiven = 3;
					break;
				case 2 :
				case 3 :
					indexGiven = 2;
					break;
				case 4 :
				case 5 :
					indexGiven = 1;
					break;
				case 6 :
				case 7 :
					indexGiven = 0;
					break;

			}
			if (player.getFlowers().farmingSeeds[indexGiven] >= 0x21 && player.getFlowers().farmingSeeds[indexGiven] <= 0x24) {
				if (allotmentData.getFlowerProtect() == Flowers.SCARECROW) {
					return;
				}
			}
			if (player.getFlowers().farmingState[indexGiven] != 3 && player.getFlowers().hasFullyGrown[indexGiven] && player.getFlowers().farmingSeeds[indexGiven] == allotmentData.getFlowerProtect()) {
				player.getFlowers().farmingState[indexGiven] = 3;
				player.getFlowers().updateFlowerStates();
			} else {
				if (Misc.random(100) <= maxChance) {
					farmingState[index] = 2;
				}
			}
		}
	}
	
	public void modifyStage(int i) {
		AllotmentData allotmentData = AllotmentData.forId(farmingSeeds[i]);
		if (allotmentData == null)
			return;
		int totalStages = (allotmentData.getEndingState() - allotmentData.getStartingState()) + 2;
		int nextStage = farmingStages[i] + 1;
		if(nextStage > totalStages)
			return;
		farmingStages[i] = nextStage;
		updateAllotmentsStates();
	}

	/* update all the patch states */
	public void updateAllotmentsStates() {
		// catherby north - catherby south - falador north west - falador south
		// east - phasmatys north west - phasmatys south east - ardougne north -
		// ardougne south
		int[] configValues = new int[farmingStages.length];

		int configValue;
		for (int i = 0; i < farmingStages.length; i++) {
			configValues[i] = getConfigValue(farmingStages[i], farmingSeeds[i], farmingState[i], i);
		}

		configValue = (configValues[0] << 16) + (configValues[1] << 8 << 16) + configValues[2] + (configValues[3] << 8);
		player.getActionSender().sendConfig(FALADOR_AND_CATHERBY_CONFIG, configValue);

		configValue = configValues[4] << 16 | configValues[5] << 8 << 16 | configValues[6] | configValues[7] << 8;
		player.getActionSender().sendConfig(ARDOUGNE_AND_PHASMATYS_CONFIG, configValue);

	}

	/* getting the different config values */

	public int getConfigValue(int allotmentStage, int seedId, int plantState, int index) {
		AllotmentData allotmentData = AllotmentData.forId(seedId);
		switch (allotmentStage) {
			case 0 :// weed
				return (GROWING << 6) + 0x00;
			case 1 :// weed cleared
				return (GROWING << 6) + 0x01;
			case 2 :
				return (GROWING << 6) + 0x02;
			case 3 :
				return (GROWING << 6) + 0x03;
		}
		if (allotmentData == null) {
			return -1;
		}
		if (allotmentData.getEndingState() == allotmentData.getStartingState() + allotmentStage - 1) {
			hasFullyGrown[index] = true;
		}

		return (getPlantState(plantState) << 6) + allotmentData.getStartingState() + allotmentStage - 4;
	}

	/* getting the plant states */

	public int getPlantState(int plantState) {
		switch (plantState) {
			case 0 :
				return GROWING;
			case 1 :
				return WATERED;
			case 2 :
				return DISEASED;
			case 3 :
				return DEAD;
		}
		return -1;
	}
	
	/* watering the patch */
	public boolean waterPatch(int objectX, int objectY, int itemId) {
		final AllotmentFieldsData allotmentFieldsData = AllotmentFieldsData.forIdPosition(new Position(objectX, objectY));
		if (allotmentFieldsData == null) {
			return false;
		}
		AllotmentData allotmentData = AllotmentData.forId(farmingSeeds[allotmentFieldsData.getAllotmentIndex()]);
		if (allotmentData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
        if (farmingState[allotmentFieldsData.getAllotmentIndex()] != 0 || farmingStages[allotmentFieldsData.getAllotmentIndex()] <= 1 || farmingStages[allotmentFieldsData.getAllotmentIndex()] == allotmentData.getEndingState() - allotmentData.getStartingState() + 4) {
			player.getActionSender().sendMessage("This patch doesn't need watering.");
			return true;
		}
		player.getInventory().removeItem(new Item(itemId));
		player.getInventory().addItem(new Item(itemId == 5333 ? itemId - 2 : itemId - 1));

		if (!player.getInventory().getItemContainer().contains(FarmingConstants.RAKE)) {
			player.getDialogue().sendStatement("You need a seed dibber to plant seed here.");
			return true;
		}
		player.getActionSender().sendMessage("You water the patch.");
		player.getUpdateFlags().sendAnimation(FarmingConstants.WATERING_CAN_ANIM);

		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {

			public void execute(CycleEventContainer container) {
				diseaseChance[allotmentFieldsData.getAllotmentIndex()] *= WATERING_CHANCE;
				farmingState[allotmentFieldsData.getAllotmentIndex()] = 1;
				container.stop();
			}

			@Override
			public void stop() {
				updateAllotmentsStates();
				player.setStopPacket(false);
				player.resetAnimation();
			}
		}, 5);
		return true;
	}

	/* clearing the patch with a rake of a spade */
	public boolean clearPatch(int objectX, int objectY, int itemId) {
		final AllotmentFieldsData allotmentFieldsData = AllotmentFieldsData.forIdPosition(new Position(objectX, objectY));
		int finalAnimation;
		int finalDelay;
		if (allotmentFieldsData == null || (itemId != FarmingConstants.RAKE && itemId != FarmingConstants.SPADE)) {
			return false;
		}
		if (farmingStages[allotmentFieldsData.getAllotmentIndex()] == 3) {
			return true;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (farmingStages[allotmentFieldsData.getAllotmentIndex()] <= 3) {
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
				if (farmingStages[allotmentFieldsData.getAllotmentIndex()] <= 2) {
					farmingStages[allotmentFieldsData.getAllotmentIndex()]++;
					player.getInventory().addItemOrDrop(new Item(6055));
				} else {
					farmingStages[allotmentFieldsData.getAllotmentIndex()] = 3;
					container.stop();
				}
				player.getSkill().addExp(Skill.FARMING, CLEARING_EXPERIENCE);
				farmingTimer[allotmentFieldsData.getAllotmentIndex()] = Server.getMinutesCounter();
				updateAllotmentsStates();
				if (farmingStages[allotmentFieldsData.getAllotmentIndex()] == 3) {
					container.stop();
					return;
				}
			}

			@Override
			public void stop() {
				resetAllotments(allotmentFieldsData.getAllotmentIndex());
				player.getActionSender().sendMessage("You clear the patch.");
				player.setStopPacket(false);
				player.resetAnimation();
			}
		}, finalDelay);
		return true;

	}

	/* planting the seeds */
	public boolean plantSeed(int objectX, int objectY, final int seedId) {
		final AllotmentFieldsData allotmentFieldsData = AllotmentFieldsData.forIdPosition(new Position(objectX, objectY));
		final AllotmentData allotmentData = AllotmentData.forId(seedId);
		if (allotmentFieldsData == null || allotmentData == null) {
			return false;
		}
		if (farmingStages[allotmentFieldsData.getAllotmentIndex()] != 3) {
			player.getActionSender().sendMessage("You can't plant a seed here.");
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (allotmentData.getLevelRequired() > player.getSkill().getLevel()[Skill.FARMING]) {
			player.getDialogue().sendStatement("You need a farming level of " + allotmentData.getLevelRequired() + " to plant this seed.");
			return true;
		}
		if (!player.getInventory().getItemContainer().contains(FarmingConstants.SEED_DIBBER)) {
			player.getDialogue().sendStatement("You need a seed dibber to plant seed here.");
			return true;
		}
		if (player.getInventory().getItemAmount(allotmentData.getSeedId()) < allotmentData.getSeedAmount()) {
			player.getDialogue().sendStatement("You need atleast " + allotmentData.getSeedAmount() + " seeds to plant here.");
			return true;
		}
		player.getUpdateFlags().sendAnimation(FarmingConstants.SEED_DIBBING);
		farmingStages[allotmentFieldsData.getAllotmentIndex()] = 4;
		player.getInventory().removeItem(new Item(seedId, allotmentData.getSeedAmount()));

		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {

			public void execute(CycleEventContainer container) {
				farmingState[allotmentFieldsData.getAllotmentIndex()] = 0;
				farmingSeeds[allotmentFieldsData.getAllotmentIndex()] = seedId;
				farmingTimer[allotmentFieldsData.getAllotmentIndex()] = Server.getMinutesCounter();
				player.getSkill().addExp(Skill.FARMING, allotmentData.getPlantingXp());
				container.stop();
			}

			@Override
			public void stop() {
				updateAllotmentsStates();
				player.setStopPacket(false);
			}
		}, 3);
		return true;
	}

	/* harvesting the plant resulted */
	public boolean harvest(int objectX, int objectY) {
		final AllotmentFieldsData allotmentFieldsData = AllotmentFieldsData.forIdPosition(new Position(objectX, objectY));
		if (allotmentFieldsData == null) {
			return false;
		}
		final AllotmentData allotmentData = AllotmentData.forId(farmingSeeds[allotmentFieldsData.getAllotmentIndex()]);
		if (allotmentData == null) {
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
		player.getUpdateFlags().sendAnimation(FarmingConstants.SPADE_ANIM);
		player.setSkilling(new CycleEvent() {

			public void execute(CycleEventContainer container) {
				if (farmingHarvest[allotmentFieldsData.getAllotmentIndex()] == 0) {
					farmingHarvest[allotmentFieldsData.getAllotmentIndex()] = (int) (1 + (START_HARVEST_AMOUNT + Misc.random(END_HARVEST_AMOUNT - START_HARVEST_AMOUNT)) * (player.getEquipment().getItemContainer().contains(7409) ? 1.10 : 1));
				}
				if (farmingHarvest[allotmentFieldsData.getAllotmentIndex()] == 1) {
					resetAllotments(allotmentFieldsData.getAllotmentIndex());
					farmingStages[allotmentFieldsData.getAllotmentIndex()] = 3;
					farmingTimer[allotmentFieldsData.getAllotmentIndex()] = Server.getMinutesCounter();
					container.stop();
					return;
				}
				if (!player.checkTask(task) || player.getInventory().getItemContainer().freeSlots() <= 0) {
					container.stop();
					return;
				}
				farmingHarvest[allotmentFieldsData.getAllotmentIndex()]--;
				player.getUpdateFlags().sendAnimation(FarmingConstants.SPADE_ANIM);
				player.getActionSender().sendMessage("You harvest the crop, and get some vegetables.");
				player.getInventory().addItem(new Item(allotmentData.getHarvestId()));
				player.getSkill().addExp(Skill.FARMING, allotmentData.getHarvestXp());
			}

			@Override
			public void stop() {
				updateAllotmentsStates();
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
		final AllotmentFieldsData allotmentFieldsData = AllotmentFieldsData.forIdPosition(new Position(objectX, objectY));
		if (allotmentFieldsData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (farmingStages[allotmentFieldsData.getAllotmentIndex()] != 3 || farmingState[allotmentFieldsData.getAllotmentIndex()] == 5) {
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
				diseaseChance[allotmentFieldsData.getAllotmentIndex()] *= itemId == 6032 ? COMPOST_CHANCE : SUPERCOMPOST_CHANCE;
				farmingState[allotmentFieldsData.getAllotmentIndex()] = 5;
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
		final AllotmentFieldsData allotmentFieldsData = AllotmentFieldsData.forIdPosition(new Position(objectX, objectY));
		if (allotmentFieldsData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		final InspectData inspectData = InspectData.forId(farmingSeeds[allotmentFieldsData.getAllotmentIndex()]);
		final AllotmentData allotmentData = AllotmentData.forId(farmingSeeds[allotmentFieldsData.getAllotmentIndex()]);
		if (farmingState[allotmentFieldsData.getAllotmentIndex()] == 2) {
			player.getDialogue().sendStatement("This plant is diseased. Use a plant cure on it to cure it, ", "or clear the patch with a spade.");
			return true;
		} else if (farmingState[allotmentFieldsData.getAllotmentIndex()] == 3) {
			player.getDialogue().sendStatement("This plant is dead. You did not cure it while it was diseased.", "Clear the patch with a spade.");
			return true;
		}
		if (farmingStages[allotmentFieldsData.getAllotmentIndex()] == 0) {
			player.getDialogue().sendStatement("This is an allotment patch. The soil has not been treated.", "The patch needs weeding.");
		} else if (farmingStages[allotmentFieldsData.getAllotmentIndex()] == 3) {
			player.getDialogue().sendStatement("This is an allotment patch. The soil has not been treated.", "The patch is empty and weeded.");
		} else if (inspectData != null && allotmentData != null) {
			player.getActionSender().sendMessage("You bend down and start to inspect the patch...");

			player.getUpdateFlags().sendAnimation(1331);
			player.setStopPacket(true);
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {

				public void execute(CycleEventContainer container) {
					if (farmingStages[allotmentFieldsData.getAllotmentIndex()] - 4 < inspectData.getMessages().length - 2) {
						player.getDialogue().sendStatement(inspectData.getMessages()[farmingStages[allotmentFieldsData.getAllotmentIndex()] - 4]);
					} else if (farmingStages[allotmentFieldsData.getAllotmentIndex()] < allotmentData.getEndingState() - allotmentData.getStartingState() + 2) {
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
		final AllotmentFieldsData allotmentFieldsData = AllotmentFieldsData.forIdPosition(new Position(objectX, objectY));
		if (allotmentFieldsData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		player.getSkillGuide().farmingComplex(1);
		player.getSkillGuide().selected = 20;
		return true;
	}

	/* protects the patch with the flowers */

	public void handleFlowerProtection(int index) {
		AllotmentData allotmentData = AllotmentData.forId(farmingSeeds[index]);
		if (allotmentData == null) {
			return;
		}
		double chance = diseaseChance[index] * allotmentData.getDiseaseChance();
		int maxChance = (int)(chance * 100);
		int indexGiven = 0;
		if (!farmingWatched[index] && Misc.random(100) <= maxChance) {
			switch (index) {
				case 0 :
				case 1 :
					indexGiven = 3;
					break;
				case 2 :
				case 3 :
					indexGiven = 2;
					break;
				case 4 :
				case 5 :
					indexGiven = 1;
					break;
				case 6 :
				case 7 :
					indexGiven = 0;
					break;

			}
			if (player.getFlowers().farmingSeeds[indexGiven] >= 0x21 && player.getFlowers().farmingSeeds[indexGiven] <= 0x24) {
				if (allotmentData.getFlowerProtect() == Flowers.SCARECROW) {
					return;
				}
			}
			if (player.getFlowers().farmingState[indexGiven] != 3 && player.getFlowers().hasFullyGrown[indexGiven] && player.getFlowers().farmingSeeds[indexGiven] == allotmentData.getFlowerProtect()) {
				player.getFlowers().farmingState[indexGiven] = 3;
				player.getFlowers().updateFlowerStates();
			} else {
				farmingState[index] = 2;
			}
		}

	}

	/* Curing the plant */

	public boolean curePlant(int objectX, int objectY, int itemId) {
		final AllotmentFieldsData allotmentFieldsData = AllotmentFieldsData.forIdPosition(new Position(objectX, objectY));
		if (allotmentFieldsData == null || itemId != 6036) {
			return false;
		}
		final AllotmentData allotmentData = AllotmentData.forId(farmingSeeds[allotmentFieldsData.getAllotmentIndex()]);
		if (allotmentData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (farmingState[allotmentFieldsData.getAllotmentIndex()] != 2) {
			player.getActionSender().sendMessage("This plant doesn't need to be cured.");
			return true;
		}
		player.getInventory().removeItem(new Item(itemId));
		player.getInventory().addItem(new Item(229));
		player.getUpdateFlags().sendAnimation(FarmingConstants.CURING_ANIM);
		player.setStopPacket(true);
		farmingState[allotmentFieldsData.getAllotmentIndex()] = 0;
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {

			public void execute(CycleEventContainer container) {
				player.getActionSender().sendMessage("You cure the plant with a plant cure.");
				container.stop();
			}

			@Override
			public void stop() {
				updateAllotmentsStates();
				player.setStopPacket(false);
				player.resetAnimation();
			}
		}, 7);

		return true;

	}

	@SuppressWarnings("unused")
	private void resetAllotments() {
		for (int i = 0; i < farmingStages.length; i++) {
			farmingSeeds[i] = 0;
			farmingState[i] = 0;
			diseaseChance[i] = 0;
			farmingHarvest[i] = 0;
		}
	}

	/* reseting the patches */

	private void resetAllotments(int index) {
		farmingSeeds[index] = 0;
		farmingState[index] = 0;
		diseaseChance[index] = 1;
		farmingHarvest[index] = 0;
		farmingWatched[index] = false;
		hasFullyGrown[index] = false;
	}

	/* checking if the patch is raked */

	public boolean checkIfRaked(int objectX, int objectY) {
		final AllotmentFieldsData allotmentFieldsData = AllotmentFieldsData.forIdPosition(new Position(objectX, objectY));
		if (allotmentFieldsData == null)
			return false;
		if (farmingStages[allotmentFieldsData.getAllotmentIndex()] == 3)
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

	public boolean[] getFarmingWatched() {
		return farmingWatched;
	}

	public void setFarmingWatched(int i, boolean allotmentWatched) {
		this.farmingWatched[i] = allotmentWatched;
	}

}
