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
public class SpecialPlantTwo {

	private Player player;

	// set of global constants for Farming

	private static final double COMPOST_CHANCE = 0.9;
	private static final double SUPERCOMPOST_CHANCE = 0.7;
	private static final double CLEARING_EXPERIENCE = 4;

	public SpecialPlantTwo(Player player) {
		this.player = player;
	}

	// Farming data
	public int[] farmingStages = new int[4];
	public int[] farmingSeeds = new int[4];
	public int[] farmingState = new int[4];
	public long[] farmingTimer = new long[4];
	public double[] diseaseChance = {1, 1, 1, 1};
	public boolean[] hasFullyGrown = {false, false, false, false};

	public static final int MAIN_SPECIAL_PLANT_CONFIG = 512;

	/* This is the enum holding the seeds info */

	public enum SpecialPlantData {
		BELLADONNA(5281, 2398, 1, 63, 280, 0.15, 91, 512, 0x04, 0x08, -1, 0, 5, 8), CACTUS(5280, 6016, 1, 55, 550, 0.15, 66.5, 25, 0x08, 0x12, 0x1f, 374, 11, 17), BITTERCAP(5282, 6004, 1, 53, 220, 0.15, 61.5, 57.7, 0x04, 0x0f, -1, 0, 12, 17);

		private int seedId;
		private int harvestId;
		private int seedAmount;
		private int levelRequired;
		private int growthTime;
		private double diseaseChance;
		private double plantingXp;
		private double harvestXp;
		private int startingState;
		private int endingState;
		private int checkHealthState;
		private double checkHealthExperience;
		private int diseaseDiffValue;
		private int deathDiffValue;

		private static Map<Integer, SpecialPlantData> seeds = new HashMap<Integer, SpecialPlantData>();

		static {
			for (SpecialPlantData data : SpecialPlantData.values()) {
				seeds.put(data.seedId, data);
			}
		}

		SpecialPlantData(int seedId, int harvestId, int seedAmount, int levelRequired, int growthTime, double diseaseChance, double plantingXp, double harvestXp, int startingState, int endingState, int checkHealthState, double checkHealthExperience, int diseaseDiffValue, int deathDiffValue) {
			this.seedId = seedId;
			this.harvestId = harvestId;
			this.seedAmount = seedAmount;
			this.levelRequired = levelRequired;
			this.growthTime = growthTime;
			this.diseaseChance = diseaseChance;
			this.plantingXp = plantingXp;
			this.harvestXp = harvestXp;
			this.startingState = startingState;
			this.endingState = endingState;
			this.checkHealthState = checkHealthState;
			this.checkHealthExperience = checkHealthExperience;
			this.diseaseDiffValue = diseaseDiffValue;
			this.deathDiffValue = deathDiffValue;
		}

		public static SpecialPlantData forId(int seedId) {
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

		public int getCheckHealthState() {
			return checkHealthState;
		}

		public double getCheckHealthXp() {
			return checkHealthExperience;
		}

		public int getDiseaseDiffValue() {
			return diseaseDiffValue;
		}

		public int getDeathDiffValue() {
			return deathDiffValue;
		}
	}

	/* This is the enum data about the different patches */

	public enum SpecialPlantFieldsData {
		DRAYNOR_MANOR(0, new Position[]{new Position(3086, 3354), new Position(3087, 3355)}, 5281), AL_KARID(2, new Position[]{new Position(3315, 3202), new Position(3316, 3203)}, 5280), CANIFIS(3, new Position[]{new Position(3451, 3472), new Position(3452, 3473)}, 5282);

		private int specialPlantsIndex;
		private Position[] specialPlantPosition;
		private int seedId;

		SpecialPlantFieldsData(int specialPlantsIndex, Position[] specialPlantPosition, int seedId) {
			this.specialPlantsIndex = specialPlantsIndex;
			this.specialPlantPosition = specialPlantPosition;
			this.seedId = seedId;
		}

		public static SpecialPlantFieldsData forIdPosition(Position position) {
			for (SpecialPlantFieldsData specialPlantFieldsData : SpecialPlantFieldsData.values()) {
				if (FarmingConstants.inRangeArea(specialPlantFieldsData.getSpecialPlantPosition()[0], specialPlantFieldsData.getSpecialPlantPosition()[1], position)) {
					return specialPlantFieldsData;
				}
			}
			return null;
		}

		public int getSpecialPlantsIndex() {
			return specialPlantsIndex;
		}

		public Position[] getSpecialPlantPosition() {
			return specialPlantPosition;
		}

		public int getSeedId() {
			return seedId;
		}
	}

	/* This is the enum that hold the different data for inspecting the plant */

	public enum InspectData {
		BELLADONNA(5281, new String[][]{{"The belladonna seed has only just been planted."}, {"The belladonna plant grows a little taller."}, {"The belladonna plant grows taller and leafier."}, {"The belladonna plant grows some flower buds."}, {"The belladonna plant is ready to harvest."}}), CACTUS(5280, new String[][]{{"The cactus seed has only just been planted."}, {"The cactus grows taller."}, {"The cactus grows two small stumps."}, {"The cactus grows its stumps longer."}, {"The cactus grows larger."}, {"The cactus curves its arms upwards and grows another stump."}, {"The cactus grows all three of its arms upwards."}, {"The cactus is ready to be harvested."}}), BITTERCAP(5282, new String[][]{{"The mushroom spore has only just been planted."}, {"The mushrooms grow a little taller."}, {"The mushrooms grow a little taller."}, {"The mushrooms grow a little larger."}, {"The mushrooms grow a little larger."}, {"The mushrooms tops grow a little wider."},
				{"The mushrooms are ready to harvest."}});

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

	/* update all the patch states */

	public void updateSpecialPlants() {
		// draynor manor - none - al karid - canifis
		int[] configValues = new int[farmingStages.length];

		int configValue;
		for (int i = 0; i < farmingStages.length; i++) {
			configValues[i] = getConfigValue(farmingStages[i], farmingSeeds[i], farmingState[i], i);
		}

		configValue = (configValues[0] << 16) + (configValues[1] << 8 << 16) + configValues[2] + (configValues[3] << 8);
		player.getActionSender().sendConfig(MAIN_SPECIAL_PLANT_CONFIG, configValue);
	}

	/* getting the different config values */

	public int getConfigValue(int specialStage, int seedId, int plantState, int index) {
		SpecialPlantData specialPlantData = SpecialPlantData.forId(seedId);
		switch (specialStage) {
			case 0 :// weed
				return 0x00;
			case 1 :// weed cleared
				return 0x01;
			case 2 :
				return 0x02;
			case 3 :
				return 0x03;
		}
		if (specialPlantData == null) {
			return -1;
		}
		if (specialStage > specialPlantData.getEndingState() - specialPlantData.getStartingState() - 1) {
			hasFullyGrown[index] = true;
		}
		if (getPlantState(plantState, specialPlantData, specialStage) == 3)
			return specialPlantData.getCheckHealthState();

		return getPlantState(plantState, specialPlantData, specialStage);
	}

	/* getting the plant states */

	public int getPlantState(int plantState, SpecialPlantData specialPlantData, int specialStage) {
		int value = specialPlantData.getStartingState() + specialStage - 4;
		switch (plantState) {
			case 0 :
				return value;
			case 1 :
				return value + specialPlantData.getDiseaseDiffValue();
			case 2 :
				return value + specialPlantData.getDeathDiffValue();
			case 3 :
				return specialPlantData.getCheckHealthState();
		}
		return -1;
	}

	/* calculating the disease chance and making the plant grow */

	public void doCalculations() {
		for (int i = 0; i < farmingSeeds.length; i++) {
			if (farmingStages[i] > 0 && farmingStages[i] <= 3 && Server.getMinutesCounter() - farmingTimer[i] >= 5) {
				farmingStages[i]--;
				farmingTimer[i] = Server.getMinutesCounter();
				updateSpecialPlants();
				continue;
			}
			SpecialPlantData specialPlantData = SpecialPlantData.forId(farmingSeeds[i]);
			if (specialPlantData == null) {
				continue;
			}

			long difference = Server.getMinutesCounter() - farmingTimer[i];
			long growth = specialPlantData.getGrowthTime();
			int nbStates = specialPlantData.getEndingState() - specialPlantData.getStartingState();
			int state = (int) (difference * nbStates / growth);
			if (farmingTimer[i] == 0 || farmingState[i] == 2 || farmingState[i] == 3 || (state > nbStates)) {
				continue;
			}
			if (4 + state != farmingStages[i] && farmingStages[i] <= specialPlantData.getEndingState() - specialPlantData.getStartingState() + (specialPlantData == SpecialPlantData.BELLADONNA ? 5 : -2)) {
				if (farmingStages[i] == specialPlantData.getEndingState() - specialPlantData.getStartingState() - 2 && specialPlantData.getCheckHealthState() != -1) {
					farmingStages[i] = specialPlantData.getEndingState() - specialPlantData.getStartingState() + 4;
					farmingState[i] = 3;
					updateSpecialPlants();
					return;
				}
				farmingStages[i] = 4 + state;
				doStateCalculation(i);
				updateSpecialPlants();
			}
		}
	}

	public void modifyStage(int i) {
		SpecialPlantData specialPlantData = SpecialPlantData.forId(farmingSeeds[i]);
		if (specialPlantData == null)
			return;
		long difference = Server.getMinutesCounter() - farmingTimer[i];
		long growth = specialPlantData.getGrowthTime();
		int nbStates = specialPlantData.getEndingState() - specialPlantData.getStartingState();
		int state = (int) (difference * nbStates / growth);
		farmingStages[i] = 4 + state;
		updateSpecialPlants();

	}

	/* calculations about the diseasing chance */

	public void doStateCalculation(int index) {
		if (farmingState[index] == 2) {
			return;
		}
		// if the patch is diseased, it dies, if its watched by a farmer, it
		// goes back to normal
		if (farmingState[index] == 1) {
			farmingState[index] = 2;
		}

		if (farmingState[index] == 5 && farmingStages[index] != 2) {
			farmingState[index] = 0;
		}

		if (farmingState[index] == 0 && farmingStages[index] >= 5 && !hasFullyGrown[index]) {
			SpecialPlantData specialPlantData = SpecialPlantData.forId(farmingSeeds[index]);
			if (specialPlantData == null) {
				return;
			}

			double chance = diseaseChance[index] * specialPlantData.getDiseaseChance();
			int maxChance = (int) chance * 100;
			if (Misc.random(100) <= maxChance) {
				farmingState[index] = 1;
			}
		}
	}

	/* clearing the patch with a rake of a spade */

	public boolean clearPatch(int objectX, int objectY, int itemId) {
		final SpecialPlantFieldsData hopsFieldsData = SpecialPlantFieldsData.forIdPosition(new Position(objectX, objectY));
		int finalAnimation;
		int finalDelay;
		if (hopsFieldsData == null || (itemId != FarmingConstants.RAKE && itemId != FarmingConstants.SPADE)) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (farmingStages[hopsFieldsData.getSpecialPlantsIndex()] == 3) {
			return true;
		}
		if (farmingStages[hopsFieldsData.getSpecialPlantsIndex()] <= 3) {
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
				if (farmingStages[hopsFieldsData.getSpecialPlantsIndex()] <= 2) {
					farmingStages[hopsFieldsData.getSpecialPlantsIndex()]++;
					player.getInventory().addItemOrDrop(new Item(6055));
				} else {
					farmingStages[hopsFieldsData.getSpecialPlantsIndex()] = 3;
					container.stop();
				}
				player.getSkill().addExp(Skill.FARMING, CLEARING_EXPERIENCE);
				farmingTimer[hopsFieldsData.getSpecialPlantsIndex()] = Server.getMinutesCounter();
				updateSpecialPlants();
				if (farmingStages[hopsFieldsData.getSpecialPlantsIndex()] == 3) {
					container.stop();
					return;
				}
			}

			@Override
			public void stop() {
				resetSpecialPlants(hopsFieldsData.getSpecialPlantsIndex());
				player.getActionSender().sendMessage("You clear the patch.");
				player.setStopPacket(false);
				player.resetAnimation();
			}
		}, finalDelay);
		return true;

	}

	/* planting the seeds */

	public boolean plantSeeds(int objectX, int objectY, final int seedId) {
		final SpecialPlantFieldsData specialPlantFieldsData = SpecialPlantFieldsData.forIdPosition(new Position(objectX, objectY));
		final SpecialPlantData specialPlantData = SpecialPlantData.forId(seedId);
		if (specialPlantFieldsData == null || specialPlantData == null || specialPlantFieldsData.getSeedId() != seedId) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (farmingStages[specialPlantFieldsData.getSpecialPlantsIndex()] != 3) {
			player.getActionSender().sendMessage("You can't plant a seed here.");
			return false;
		}
		if (specialPlantData.getLevelRequired() > player.getSkill().getLevel()[Skill.FARMING]) {
			player.getDialogue().sendStatement("You need a farming level of " + specialPlantData.getLevelRequired() + " to plant this seed.");
			return true;
		}
		if (!player.getInventory().getItemContainer().contains(FarmingConstants.SEED_DIBBER)) {
			player.getDialogue().sendStatement("You need a seed dibber to plant seed here.");
			return true;
		}
		if (player.getInventory().getItemAmount(specialPlantData.getSeedId()) < specialPlantData.getSeedAmount()) {
			player.getDialogue().sendStatement("You need atleast " + specialPlantData.getSeedAmount() + " seeds to plant here.");
			return true;
		}
		player.getUpdateFlags().sendAnimation(FarmingConstants.SEED_DIBBING);
		farmingStages[specialPlantFieldsData.getSpecialPlantsIndex()] = 4;
		player.getInventory().removeItem(new Item(seedId, specialPlantData.getSeedAmount()));

		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				farmingState[specialPlantFieldsData.getSpecialPlantsIndex()] = 0;
				farmingSeeds[specialPlantFieldsData.getSpecialPlantsIndex()] = seedId;
				farmingTimer[specialPlantFieldsData.getSpecialPlantsIndex()] = Server.getMinutesCounter();
				player.getSkill().addExp(Skill.FARMING, specialPlantData.getPlantingXp());
				container.stop();
			}

			@Override
			public void stop() {
				updateSpecialPlants();
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

	public boolean harvestOrCheckHealth(int objectX, int objectY) {
		final SpecialPlantFieldsData specialPlantFieldsData = SpecialPlantFieldsData.forIdPosition(new Position(objectX, objectY));
		if (specialPlantFieldsData == null) {
			return false;
		}
		final SpecialPlantData specialPlantData = SpecialPlantData.forId(farmingSeeds[specialPlantFieldsData.getSpecialPlantsIndex()]);
		if (specialPlantData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (player.getInventory().getItemContainer().freeSlots() <= 0) {
			player.getActionSender().sendMessage("Not enough space in your inventory.");
			return true;
		}
		player.setStopPacket(true);
		player.getUpdateFlags().sendAnimation(832);
		final int task = player.getTask();
		player.setSkilling(new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task) || player.getInventory().getItemContainer().freeSlots() <= 0) {
					container.stop();
					return;
				}

				if (farmingState[specialPlantFieldsData.getSpecialPlantsIndex()] == 3) {
					player.getActionSender().sendMessage("You examine the plant for signs of disease and find that it's in perfect health.");
					player.getSkill().addExp(Skill.FARMING, specialPlantData.getCheckHealthXp());
					farmingState[specialPlantFieldsData.getSpecialPlantsIndex()] = 0;
					hasFullyGrown[specialPlantFieldsData.getSpecialPlantsIndex()] = false;
					farmingTimer[specialPlantFieldsData.getSpecialPlantsIndex()] = Server.getMinutesCounter() - specialPlantData.getGrowthTime();
					modifyStage(specialPlantFieldsData.getSpecialPlantsIndex());
					container.stop();
					return;
				}
				player.getActionSender().sendMessage("You harvest the crop, and pick some " + specialPlantData.getHarvestId() + ".");
				player.getInventory().addItem(new Item(specialPlantData.getHarvestId()));
				player.getSkill().addExp(Skill.FARMING, specialPlantData.getHarvestXp());

				switch (specialPlantData) {
					case BELLADONNA :
						resetSpecialPlants(specialPlantFieldsData.getSpecialPlantsIndex());
						farmingStages[specialPlantFieldsData.getSpecialPlantsIndex()] = 3;
						farmingTimer[specialPlantFieldsData.getSpecialPlantsIndex()] = Server.getMinutesCounter();
						break;
					case CACTUS :
						farmingStages[specialPlantFieldsData.getSpecialPlantsIndex()]--;
						break;
					case BITTERCAP :
						farmingStages[specialPlantFieldsData.getSpecialPlantsIndex()]++;
						if (farmingStages[specialPlantFieldsData.getSpecialPlantsIndex()] == 16) {
							resetSpecialPlants(specialPlantFieldsData.getSpecialPlantsIndex());
							farmingStages[specialPlantFieldsData.getSpecialPlantsIndex()] = 3;
							farmingTimer[specialPlantFieldsData.getSpecialPlantsIndex()] = Server.getMinutesCounter();
						}
						break;
				}
				updateSpecialPlants();
				container.stop();
			}

			@Override
			public void stop() {
				player.setStopPacket(false);
				player.resetAnimation();
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 2);
		return true;
	}

	/* lowering the stage */

	public void lowerStage(int index, int timer) {
		hasFullyGrown[index] = false;
		farmingTimer[index] -= timer;
	}

	/* putting compost onto the plant */

	public boolean putCompost(int objectX, int objectY, final int itemId) {
		if (itemId != 6032 && itemId != 6034) {
			return false;
		}
		final SpecialPlantFieldsData specialPlantFieldsData = SpecialPlantFieldsData.forIdPosition(new Position(objectX, objectY));
		if (specialPlantFieldsData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (farmingStages[specialPlantFieldsData.getSpecialPlantsIndex()] != 3 || farmingState[specialPlantFieldsData.getSpecialPlantsIndex()] == 5) {
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
				diseaseChance[specialPlantFieldsData.getSpecialPlantsIndex()] *= itemId == 6032 ? COMPOST_CHANCE : SUPERCOMPOST_CHANCE;
				farmingState[specialPlantFieldsData.getSpecialPlantsIndex()] = 5;
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
		final SpecialPlantFieldsData specialPlantFieldsData = SpecialPlantFieldsData.forIdPosition(new Position(objectX, objectY));
		if (specialPlantFieldsData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		final InspectData inspectData = InspectData.forId(farmingSeeds[specialPlantFieldsData.getSpecialPlantsIndex()]);
		final SpecialPlantData specialPlantData = SpecialPlantData.forId(farmingSeeds[specialPlantFieldsData.getSpecialPlantsIndex()]);
		if (farmingState[specialPlantFieldsData.getSpecialPlantsIndex()] == 1) {
			player.getDialogue().sendStatement("This plant is diseased. Use a plant cure on it to cure it, ", "or clear the patch with a spade.");
			return true;
		} else if (farmingState[specialPlantFieldsData.getSpecialPlantsIndex()] == 2) {
			player.getDialogue().sendStatement("This plant is dead. You did not cure it while it was diseased.", "Clear the patch with a spade.");
			return true;
		} else if (farmingState[specialPlantFieldsData.getSpecialPlantsIndex()] == 3) {
			player.getDialogue().sendStatement("This plant has fully grown. You can check it's health", "to gain some farming experiences.");
			return true;
		}
		if (farmingStages[specialPlantFieldsData.getSpecialPlantsIndex()] == 0) {
			player.getDialogue().sendStatement("This is one of the special patches. The soil has not been treated.", "The patch needs weeding.");
		} else if (farmingStages[specialPlantFieldsData.getSpecialPlantsIndex()] == 3) {
			player.getDialogue().sendStatement("This is one of the special patches. The soil has not been treated.", "The patch is empty and weeded.");
		} else if (inspectData != null && specialPlantData != null) {
			player.getActionSender().sendMessage("You bend down and start to inspect the patch...");

			player.getUpdateFlags().sendAnimation(1331);
			player.setStopPacket(true);
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {

				@Override
				public void execute(CycleEventContainer container) {
					if (farmingStages[specialPlantFieldsData.getSpecialPlantsIndex()] - 4 < inspectData.getMessages().length - 2) {
						player.getDialogue().sendStatement(inspectData.getMessages()[farmingStages[specialPlantFieldsData.getSpecialPlantsIndex()] - 4]);
					} else if (farmingStages[specialPlantFieldsData.getSpecialPlantsIndex()] < specialPlantData.getEndingState() - specialPlantData.getStartingState() + 2) {
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
		final SpecialPlantFieldsData specialPlantFieldsData = SpecialPlantFieldsData.forIdPosition(new Position(objectX, objectY));
		if (specialPlantFieldsData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		player.getSkillGuide().farmingComplex(8);
		player.getSkillGuide().selected = 20;
		return true;
	}

	/* Curing the plant */

	public boolean curePlant(int objectX, int objectY, int itemId) {
		final SpecialPlantFieldsData specialPlantFieldsData = SpecialPlantFieldsData.forIdPosition(new Position(objectX, objectY));
		if (specialPlantFieldsData == null || itemId != 6036) {
			return false;
		}
		final SpecialPlantData specialPlantData = SpecialPlantData.forId(farmingSeeds[specialPlantFieldsData.getSpecialPlantsIndex()]);
		if (specialPlantData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (farmingState[specialPlantFieldsData.getSpecialPlantsIndex()] != 1) {
			player.getActionSender().sendMessage("This plant doesn't need to be cured.");
			return true;
		}
		player.getInventory().removeItem(new Item(itemId));
		player.getInventory().addItem(new Item(229));
		player.getUpdateFlags().sendAnimation(FarmingConstants.CURING_ANIM);
		player.setStopPacket(true);
		farmingState[specialPlantFieldsData.getSpecialPlantsIndex()] = 0;
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				player.getActionSender().sendMessage("You cure the plant with a plant cure.");
				container.stop();
			}

			@Override
			public void stop() {
				updateSpecialPlants();
				player.setStopPacket(false);
				player.resetAnimation();
			}
		}, 7);

		return true;

	}

	private void resetSpecialPlants(int index) {
		farmingSeeds[index] = 0;
		farmingState[index] = 0;
		diseaseChance[index] = 1;
		hasFullyGrown[index] = false;
	}

	/* checking if the patch is raked */

	public boolean checkIfRaked(int objectX, int objectY) {
		final SpecialPlantFieldsData specialPlantFieldData = SpecialPlantFieldsData.forIdPosition(new Position(objectX, objectY));
		if (specialPlantFieldData == null)
			return false;
		if (farmingStages[specialPlantFieldData.getSpecialPlantsIndex()] == 3)
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
