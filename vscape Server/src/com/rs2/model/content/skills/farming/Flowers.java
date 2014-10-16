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
public class Flowers { // todo scarecrow 6059

	private Player player;

	// set of global constants for Farming

	private static final double WATERING_CHANCE = 0.5;
	private static final double COMPOST_CHANCE = 0.9;
	private static final double SUPERCOMPOST_CHANCE = 0.7;
	private static final double CLEARING_EXPERIENCE = 4;
	public static final int SCARECROW = 6059;

	public Flowers(Player player) {
		this.player = player;
	}

	// Farming data
	public int[] farmingStages = new int[4];
	public int[] farmingSeeds = new int[4];
	public int[] farmingState = new int[4];
	public long[] farmingTimer = new long[4];
	public double[] diseaseChance = {1, 1, 1, 1};
	public boolean[] hasFullyGrown = {false, false, false, false};

	/* set of the constants for the patch */

	// states - 2 bits plant - 6 bits
	public static final int GROWING = 0x00;
	public static final int WATERED = 0x01;
	public static final int DISEASED = 0x02;
	public static final int DEAD = 0x03;

	public static final int FLOWER_PATCH_CONFIGS = 508;

	/* This is the enum holding the seeds info */

	public enum FlowerData {

		MARIGOLD(5096, 6010, 2, 20, 0.35, 8.5, 47, 0x08, 0x0c), 
		ROSEMARY(5097, 6014, 11, 20, 0.32, 12, 66.5, 0x0d, 0x11),
		NASTURTIUM(5098, 6012, 24, 20, 0.30, 19.5, 111, 0x12, 0x16),
		WOAD(5099, 1793, 25, 20, 0.27, 20.5, 115.5, 0x17, 0x1b), 
		LIMPWURT(5100, 225, 26, 20, 21.5, 8.5, 120, 0x1c, 0x20);

		private int seedId;
		private int harvestId;
		private int levelRequired;
		private int growthTime;
		private double diseaseChance;
		private double plantingXp;
		private double harvestXp;
		private int startingState;
		private int endingState;

		private static Map<Integer, FlowerData> seeds = new HashMap<Integer, FlowerData>();

		static {
			for (FlowerData data : FlowerData.values()) {
				seeds.put(data.seedId, data);
			}
		}

		FlowerData(int seedId, int harvestId, int levelRequired, int growthTime, double diseaseChance, double plantingXp, double harvestXp, int startingState, int endingState) {
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

		public static FlowerData forId(int seedId) {
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

	public enum FlowerFieldsData {
		ARDOUGNE(0, new Position[]{new Position(2666, 3374), new Position(2667, 3375)}), PHASMATYS(1, new Position[]{new Position(3601, 3525), new Position(3602, 3526)}), FALADOR(2, new Position[]{new Position(3054, 3307), new Position(3055, 3308)}), CATHERBY(3, new Position[]{new Position(2809, 3463), new Position(2810, 3464)});

		private int flowerIndex;
		private Position[] flowerPosition;

		FlowerFieldsData(int flowerIndex, Position[] flowerPosition) {
			this.flowerIndex = flowerIndex;
			this.flowerPosition = flowerPosition;
		}

		public static FlowerFieldsData forIdPosition(Position position) {
			for (FlowerFieldsData flowerFieldsData : FlowerFieldsData.values()) {
				if (FarmingConstants.inRangeArea(flowerFieldsData.getFlowerPosition()[0], flowerFieldsData.getFlowerPosition()[1], position)) {
					return flowerFieldsData;
				}
			}
			return null;
		}

		public int getFlowerIndex() {
			return flowerIndex;
		}

		public Position[] getFlowerPosition() {
			return flowerPosition;
		}
	}

	/* This is the enum that hold the different data for inspecting the plant */

	public enum InspectData {

		MARIGOLD(5096, new String[][]{{"The seeds have only just been planted."}, {"The marigold plants have developed leaves."}, {"The marigold plants have begun to grow their", "flowers. The new flowers are orange and small at", "first."}, {"The marigold plants are larger, and more", "developed in their petals."}, {"The marigold plants are ready to harvest. Their", "flowers are fully matured."}}), ROSEMARY(5097, new String[][]{{"The seeds have only just been planted."}, {"The rosemary plant is taller than before."}, {"The rosemary plant is bushier and taller than", "before."}, {"The rosemary plant is developing a flower bud at", "its top."}, {"The plant is ready to harvest. The rosemary", "plant's flower has opened."}}), NASTURTIUM(5098, new String[][]{{"The nasturtium seed has only just been planted."}, {"The nasturtium plants have started to develop", "leaves."}, {"The nasturtium plants have grown more leaves,", "and nine flower buds."},
				{"The nasturtium plants open their flower buds."}, {"The plants are ready to harvest. The nasturtium", "plants grow larger than before and the flowers", "fully open."}}), WOAD(5099, new String[][]{{"The woad seed has only just been planted."}, {"The woad plant produces more stalks, that split", "in tow near the top."}, {"The woad plant grows more segments from its", "intitial stalks."}, {"The woad plant develops flower buds on the end", "of each of its stalks."}, {"The woad plant is ready to harvest. The plant has", "all of its stalks pointing directly up, with", "all flowers open."}}), LIMPWURT(5100, new String[][]{{"The seed has only just been planted."}, {"The limpwurt plant produces more roots."}, {"The limpwurt plant produces an unopened pink", "flower bud and continues to grow larger."}, {"The limpwurt plant grows larger, with more loops", "in its roots. The flower bud is still unopened."},
				{"The limpwurt plant is ready to harvest. The", "flower finally opens wide, with a spike in the", "middle."}});
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
					updateFlowerStates();
				}
				if (farmingSeeds[i] > 0x21 && farmingSeeds[i] <= 0x24) {
					farmingSeeds[i]--;
					updateFlowerStates();
					return;
				}
			}
			FlowerData flowerData = FlowerData.forId(farmingSeeds[i]);
			if (flowerData == null) {
				continue;
			}
			long growthTimeTotal = flowerData.getGrowthTime();
			int totalStages = (flowerData.getEndingState() - flowerData.getStartingState());
			long growthTimePerStage = (growthTimeTotal / totalStages);
			int nextStage = farmingStages[i] + 1;
			//if timer is 0 or if the plant is dead or fully grown go to next Herb patch index insted
			if (farmingState[i] == 3 || farmingSeeds[i] == 0x21 || farmingTimer[i] == 0 || (nextStage > totalStages + 4)) {
				continue;
			}
			if(difference >= growthTimePerStage) //in growth stage time (5 minutes for flowers)
			{
				if (nextStage != farmingStages[i]) {
					farmingStages[i] = nextStage;
					farmingTimer[i] = Server.getMinutesCounter();
					processState(i);
					updateFlowerStates();
				}
			}
		}
	}
	
	public void processState(int index)
	{
		if (farmingState[index] == 3) {
			return;
		}
		// if the patch is diseased, it dies, if its watched by a farmer, it
		// goes back to normal
		if (farmingState[index] == 2) {
			farmingState[index] = 3;
		}

		if (farmingState[index] == 1 || farmingState[index] == 5 && farmingStages[index] != 3) {
			diseaseChance[index] *= 2;
			farmingState[index] = 0;
		}
		if (farmingState[index] == 0 && farmingStages[index] >= 5 && !hasFullyGrown[index]) {
			FlowerData flowerData = FlowerData.forId(farmingSeeds[index]);
			if (flowerData == null) {
				return;
			}
			double chance = diseaseChance[index] * flowerData.getDiseaseChance();
			int maxChance = (int) (chance * 100);

			if (Misc.random(100) <= maxChance) {
				farmingState[index] = 2;
			}
		}
	}
	
	/* update all the patch states */
	public void updateFlowerStates() {
		// ardougne - phasmatys - falador - catherby
		int[] configValues = new int[farmingStages.length];

		int configValue;
		for (int i = 0; i < farmingStages.length; i++) {
			configValues[i] = getConfigValue(farmingStages[i], farmingSeeds[i], farmingState[i], i);
		}

		configValue = (configValues[0] << 16) + (configValues[1] << 8 << 16) + configValues[2] + (configValues[3] << 8);
		player.getActionSender().sendConfig(FLOWER_PATCH_CONFIGS, configValue);

	}

	/* getting the different config values */

	public int getConfigValue(int flowerStage, int seedId, int plantState, int index) {
		if (farmingSeeds[index] >= 0x21 && farmingSeeds[index] <= 0x24 && farmingStages[index] > 3) {
			return (GROWING << 6) + farmingSeeds[index];
		}
		FlowerData flowerData = FlowerData.forId(seedId);
		switch (flowerStage) {
			case 0 :// weed
				return (GROWING << 6) + 0x00;
			case 1 :// weed cleared
				return (GROWING << 6) + 0x01;
			case 2 :
				return (GROWING << 6) + 0x02;
			case 3 :
				return (GROWING << 6) + 0x03;
		}
		if (flowerData == null) {
			return -1;
		}
		if (flowerData.getEndingState() == flowerData.getStartingState() + flowerStage - 2) {
			hasFullyGrown[index] = true;
		}
		return (getPlantState(plantState) << 6) + flowerData.getStartingState() + flowerStage - 4;
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
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Position(objectX, objectY));
		if (flowerFieldsData == null) {
			return false;
		}
		FlowerData flowerData = FlowerData.forId(farmingSeeds[flowerFieldsData.getFlowerIndex()]);
		if (flowerData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (farmingState[flowerFieldsData.getFlowerIndex()] == 1 || farmingStages[flowerFieldsData.getFlowerIndex()] <= 1 || farmingStages[flowerFieldsData.getFlowerIndex()] == flowerData.getEndingState() - flowerData.getStartingState() + 4) {
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

			@Override
			public void execute(CycleEventContainer container) {
				diseaseChance[flowerFieldsData.getFlowerIndex()] *= WATERING_CHANCE;
				farmingState[flowerFieldsData.getFlowerIndex()] = 1;
				container.stop();
			}

			@Override
			public void stop() {
				updateFlowerStates();
				player.setStopPacket(false);
				player.resetAnimation();
			}
		}, 5);
		return true;
	}

	/* clearing the patch with a rake of a spade */

	public boolean clearPatch(int objectX, int objectY, int itemId) {
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Position(objectX, objectY));
		int finalAnimation;
		int finalDelay;
		if (flowerFieldsData == null || (itemId != FarmingConstants.RAKE && itemId != FarmingConstants.SPADE)) {
			return false;
		}
		if (farmingStages[flowerFieldsData.getFlowerIndex()] == 3) {
			return true;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (farmingStages[flowerFieldsData.getFlowerIndex()] <= 3) {
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
				if (farmingStages[flowerFieldsData.getFlowerIndex()] <= 2) {
					farmingStages[flowerFieldsData.getFlowerIndex()]++;
					player.getInventory().addItemOrDrop(new Item(6055));
				} else {
					farmingStages[flowerFieldsData.getFlowerIndex()] = 3;
					container.stop();
				}
				player.getSkill().addExp(Skill.FARMING, CLEARING_EXPERIENCE);
				farmingTimer[flowerFieldsData.getFlowerIndex()] = Server.getMinutesCounter();
				updateFlowerStates();
				if (farmingStages[flowerFieldsData.getFlowerIndex()] == 3) {
					container.stop();
					return;
				}
			}

			@Override
			public void stop() {
				resetFlowers(flowerFieldsData.getFlowerIndex());
				player.getActionSender().sendMessage("You clear the patch.");
				player.setStopPacket(false);
				player.resetAnimation();
			}
		}, finalDelay);
		return true;

	}

	/* planting the seeds */

	public boolean plantSeed(int objectX, int objectY, final int seedId) {
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Position(objectX, objectY));
		final FlowerData flowerData = FlowerData.forId(seedId);
		if (flowerFieldsData == null || flowerData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (farmingStages[flowerFieldsData.getFlowerIndex()] != 3) {
			player.getDialogue().sendStatement("You can't plant a seed here.");
			return false;
		}
		if (flowerData.getLevelRequired() > player.getSkill().getLevel()[Skill.FARMING]) {
			player.getDialogue().sendStatement("You need a farming level of " + flowerData.getLevelRequired() + " to plant this seed.");
			return true;
		}
		if (!player.getInventory().getItemContainer().contains(FarmingConstants.SEED_DIBBER)) {
			player.getDialogue().sendStatement("You need a seed dibber to plant seed here.");
			return true;
		}

		player.getUpdateFlags().sendAnimation(FarmingConstants.SEED_DIBBING);
		farmingStages[flowerFieldsData.getFlowerIndex()] = 4;
		player.getInventory().removeItem(new Item(seedId));

		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				farmingState[flowerFieldsData.getFlowerIndex()] = 0;
				farmingSeeds[flowerFieldsData.getFlowerIndex()] = seedId;
				farmingTimer[flowerFieldsData.getFlowerIndex()] = Server.getMinutesCounter();
				player.getSkill().addExp(Skill.FARMING, flowerData.getPlantingXp());
				container.stop();
			}

			@Override
			public void stop() {
				updateFlowerStates();
				player.setStopPacket(false);
			}
		}, 3);
		return true;
	}

	@SuppressWarnings("unused")
	private void displayAll() {
		for (int i = 0; i < farmingStages.length; i++) {
			System.out.println("index : " + i);
			System.out.println("state : " + farmingState[i]);
			System.out.println("seeds : " + farmingSeeds[i]);
			System.out.println("level : " + farmingStages[i]);
			System.out.println("timer : " + farmingTimer[i]);
			System.out.println("disease chance : " + diseaseChance[i]);
			System.out.println("-----------------------------------------------------------------");
		}
	}

	/* harvesting the plant resulted */

	public boolean harvest(int objectX, int objectY) {
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Position(objectX, objectY));
		if (flowerFieldsData == null) {
			return false;
		}
		final FlowerData flowerData = FlowerData.forId(farmingSeeds[flowerFieldsData.getFlowerIndex()]);
		if (flowerData == null) {
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

		player.getUpdateFlags().sendAnimation(FarmingConstants.SPADE_ANIM);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				resetFlowers(flowerFieldsData.getFlowerIndex());
				farmingStages[flowerFieldsData.getFlowerIndex()] = 3;
				farmingTimer[flowerFieldsData.getFlowerIndex()] = Server.getMinutesCounter();
				player.getUpdateFlags().sendAnimation(FarmingConstants.SPADE_ANIM);
				player.getActionSender().sendMessage("You harvest the crop, and get some vegetables.");
				player.getInventory().addItem(new Item(flowerData.getHarvestId(), flowerData.getHarvestId() == 5099 || flowerData.getHarvestId() == 5100 ? 3 : 1));
				player.getSkill().addExp(Skill.FARMING, flowerData.getHarvestXp());
				container.stop();
			}

			@Override
			public void stop() {
				updateFlowerStates();
				player.resetAnimation();
			}
		}, 2);
		return true;
	}

	/* putting compost onto the plant */

	public boolean putCompost(int objectX, int objectY, final int itemId) {
		if (itemId != 6032 && itemId != 6034) {
			return false;
		}
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Position(objectX, objectY));
		if (flowerFieldsData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (farmingStages[flowerFieldsData.getFlowerIndex()] != 3 || farmingState[flowerFieldsData.getFlowerIndex()] == 5) {
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
				diseaseChance[flowerFieldsData.getFlowerIndex()] *= itemId == 6032 ? COMPOST_CHANCE : SUPERCOMPOST_CHANCE;
				farmingState[flowerFieldsData.getFlowerIndex()] = 5;
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
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Position(objectX, objectY));
		if (flowerFieldsData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		final InspectData inspectData = InspectData.forId(farmingSeeds[flowerFieldsData.getFlowerIndex()]);
		final FlowerData flowerData = FlowerData.forId(farmingSeeds[flowerFieldsData.getFlowerIndex()]);
		if (farmingState[flowerFieldsData.getFlowerIndex()] == 2) {
			player.getDialogue().sendStatement("This plant is diseased. Use a plant cure on it to cure it, ", "or clear the patch with a spade.");
			return true;
		} else if (farmingState[flowerFieldsData.getFlowerIndex()] == 3) {
			player.getDialogue().sendStatement("This plant is dead. You did not cure it while it was diseased.", "Clear the patch with a spade.");
			return true;
		}
		if (farmingStages[flowerFieldsData.getFlowerIndex()] == 0) {
			player.getDialogue().sendStatement("This is an flower patch. The soil has not been treated.", "The patch needs weeding.");
		} else if (farmingStages[flowerFieldsData.getFlowerIndex()] == 3) {
			player.getDialogue().sendStatement("This is an flower patch. The soil has not been treated.", "The patch is empty and weeded.");
		} else if (inspectData != null && flowerData != null) {
			player.getActionSender().sendMessage("You bend down and start to inspect the patch...");

			player.getUpdateFlags().sendAnimation(1331);
			player.setStopPacket(true);
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {

				@Override
				public void execute(CycleEventContainer container) {
					if (farmingStages[flowerFieldsData.getFlowerIndex()] - 4 < inspectData.getMessages().length - 2) {
						player.getDialogue().sendStatement(inspectData.getMessages()[farmingStages[flowerFieldsData.getFlowerIndex()] - 4]);
					} else if (farmingStages[flowerFieldsData.getFlowerIndex()] < flowerData.getEndingState() - flowerData.getStartingState() + 4) {
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
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Position(objectX, objectY));
		if (flowerFieldsData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		player.getSkillGuide().farmingComplex(6);
		player.getSkillGuide().selected = 20;
		return true;
	}

	/* Curing the plant */

	public boolean curePlant(int objectX, int objectY, int itemId) {
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Position(objectX, objectY));
		if (flowerFieldsData == null || itemId != 6036) {
			return false;
		}
		final FlowerData flowerData = FlowerData.forId(farmingSeeds[flowerFieldsData.getFlowerIndex()]);
		if (flowerData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (farmingState[flowerFieldsData.getFlowerIndex()] != 2) {
			player.getActionSender().sendMessage("This plant doesn't need to be cured.");
			return true;
		}
		player.getInventory().removeItem(new Item(itemId));
		player.getInventory().addItem(new Item(229));
		player.getUpdateFlags().sendAnimation(FarmingConstants.CURING_ANIM);
		farmingState[flowerFieldsData.getFlowerIndex()] = 0;
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				player.getActionSender().sendMessage("You cure the plant with a plant cure.");
				container.stop();
			}

			@Override
			public void stop() {
				updateFlowerStates();
				player.setStopPacket(false);
				player.resetAnimation();
			}
		}, 7);

		return true;

	}

	/* Planting scarecrow to push off the birds */

	public boolean plantScareCrow(int objectX, int objectY, int itemId) {
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Position(objectX, objectY));
		if (flowerFieldsData == null || itemId != SCARECROW) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (farmingStages[flowerFieldsData.getFlowerIndex()] != 3) {
			player.getActionSender().sendMessage("You need to clear the patch before planting a scarecrow");
			return false;
		}
		player.getInventory().removeItem(new Item(SCARECROW));
		player.getUpdateFlags().sendAnimation(832);
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				player.getActionSender().sendMessage("You put a scarecrow on the flower patch, and some weeds start to grow around it.");
				farmingSeeds[flowerFieldsData.getFlowerIndex()] = 0x24;
				farmingStages[flowerFieldsData.getFlowerIndex()] = 4;
				farmingTimer[flowerFieldsData.getFlowerIndex()] = Server.getMinutesCounter();
				container.stop();
			}

			@Override
			public void stop() {
				updateFlowerStates();
				player.setStopPacket(false);
				player.resetAnimation();
			}
		}, 2);
		return true;
	}

	@SuppressWarnings("unused")
	private void resetFlowers() {
		for (int i = 0; i < farmingStages.length; i++) {
			farmingSeeds[i] = 0;
			farmingState[i] = 0;
			diseaseChance[i] = 0;
		}
	}

	/* reseting the patches */

	private void resetFlowers(int index) {
		farmingSeeds[index] = 0;
		farmingState[index] = 0;
		diseaseChance[index] = 1;
	}

	/* checking if the patch is raked */

	public boolean checkIfRaked(int objectX, int objectY) {
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Position(objectX, objectY));
		if (flowerFieldsData == null)
			return false;
		if (farmingStages[flowerFieldsData.getFlowerIndex()] == 3)
			return true;
		return false;
	}

	public int[] getFarmingStages() {
		return farmingStages;
	}

	public void setFarmingStages(int i, int flowerStages) {
		this.farmingStages[i] = flowerStages;
	}

	public int[] getFarmingSeeds() {
		return farmingSeeds;
	}

	public void setFarmingSeeds(int i, int flowerSeeds) {
		this.farmingSeeds[i] = flowerSeeds;
	}

	public int[] getFarmingState() {
		return farmingState;
	}

	public void setFarmingState(int i, int flowerState) {
		this.farmingState[i] = flowerState;
	}

	public long[] getFarmingTimer() {
		return farmingTimer;
	}

	public void setFarmingTimer(int i, long flowerTimer) {
		this.farmingTimer[i] = flowerTimer;
	}

	public double[] getDiseaseChance() {
		return diseaseChance;
	}

	public void setDiseaseChance(int i, double diseaseChance) {
		this.diseaseChance[i] = diseaseChance;
	}

}
