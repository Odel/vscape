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
public class Hops {

	private Player player;

	// set of global constants for Farming

	private static final int START_HARVEST_AMOUNT = 3;
	private static final int END_HARVEST_AMOUNT = 41;

	private static final double WATERING_CHANCE = 0.5;
	private static final double COMPOST_CHANCE = 0.9;
	private static final double SUPERCOMPOST_CHANCE = 0.7;
	private static final double CLEARING_EXPERIENCE = 4;

	public Hops(Player player) {
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
	public boolean[] farmingWatched = {false, false, false, false};

	/* set of the constants for the patch */

	// states - 2 bits plant - 6 bits
	public static final int GROWING = 0x00;
	public static final int WATERED = 0x01;
	public static final int DISEASED = 0x02;
	public static final int DEAD = 0x03;

	public static final int MAIN_HOPS_CONFIG = 506;

	/* This is the enum holding the seeds info */

	public enum HopsData {
		BARLEY(5305, 6006, 4, 3, new int[]{6032, 3}, 40, 0.35, 8.5, 9.5, 0x31, 0x35), 
		HAMMERSTONE(5307, 5994, 4, 4, new int[]{6010, 1}, 40, 0.35, 9, 10, 0x04, 0x08), 
		ASGARNIAN(5308, 5996, 4, 8, new int[]{5458, 1}, 50, 0.30, 10.5, 12, 0x0b, 0x10), 
		JUTE(5306, 5931, 3, 13, new int[]{6008, 6}, 50, 0.30, 13, 14.5, 0x38, 0x3d),
		YANILLIAN(5309, 5998, 4, 16, new int[]{5968, 1}, 60, 0.25, 14.5, 16, 0x13, 0x19),
		KRANDORIAN(5310, 6000, 4, 21, new int[]{5478}, 70, 0.25, 17.5, 19.5, 0x1c, 0x23), 
		WILDBLOOD(5311, 6002, 4, 28, new int[]{6012, 1}, 80, 0.20, 23, 26, 0x26, 0x2e);

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

		private static Map<Integer, HopsData> seeds = new HashMap<Integer, HopsData>();

		static {
			for (HopsData data : HopsData.values()) {
				seeds.put(data.seedId, data);
			}
		}

		HopsData(int seedId, int harvestId, int seedAmount, int levelRequired, int[] paymentToWatch, int growthTime, double diseaseChance, double plantingXp, double harvestXp, int startingState, int endingState) {
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
		}

		public static HopsData forId(int seedId) {
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
	}

	/* This is the enum data about the different patches */

	public enum HopsFieldsData {

		LUMBRIDGE(0, new Position[]{new Position(3227, 3313, 0), new Position(3231, 3317, 0)}, 2333), MCGRUBOR(1, new Position[]{new Position(2664, 3523, 0), new Position(2669, 3528, 0)}, 2334), YANILLE(2, new Position[]{new Position(2574, 3103, 0), new Position(2577, 3106, 0)}, 2332), ENTRANA(3, new Position[]{new Position(2809, 3335, 0), new Position(2812, 3338, 0)}, 2327);

		private int hopsIndex;
		private Position[] hopsPosition;
		private int npcId;

		private static Map<Integer, HopsFieldsData> npcsProtecting = new HashMap<Integer, HopsFieldsData>();

		static {
			for (HopsFieldsData data : HopsFieldsData.values()) {
				npcsProtecting.put(data.npcId, data);

			}
		}

		public static HopsFieldsData forId(int npcId) {
			return npcsProtecting.get(npcId);
		}

		HopsFieldsData(int hopsIndex, Position[] hopsPosition, int npcId) {
			this.hopsIndex = hopsIndex;
			this.hopsPosition = hopsPosition;
			this.npcId = npcId;
		}

		public static HopsFieldsData forIdPosition(Position position) {
			for (HopsFieldsData hopsFieldsData : HopsFieldsData.values()) {
				if (FarmingConstants.inRangeArea(hopsFieldsData.getHopsPosition()[0], hopsFieldsData.getHopsPosition()[1], position)) {
					return hopsFieldsData;
				}
			}
			return null;
		}

		public int getHopsIndex() {
			return hopsIndex;
		}

		public Position[] getHopsPosition() {
			return hopsPosition;
		}

		public int getNpcId() {
			return npcId;
		}
	}

	/* This is the enum that hold the different data for inspecting the plant */

	public enum InspectData {

		BARLEY(5305, new String[][]{{"The barley seeds have only just been planted."}, {"Grain heads develop at the upper part of the stalks,", "as the barley grows taller."}, {"The barley grows taller, the heads weighing", "slightly on the stalks."}, {"The barley grows taller."}, {"The barley is ready to harvest. The heads of grain", "are weighing down heavily on the stalks!"}}), HAMMERSTONE(5307, new String[][]{{"The Hammerstone seeds have only just been planted."}, {"The Hammerstone hops plant grows a little bit taller."}, {"The Hammerstone hops plant grows a bit taller."}, {"The Hammerstone hops plant grows a bit taller."}, {"The Hammerstone hops plant is ready to harvest."}}), ASGARNIAN(5308, new String[][]{{"The Asgarnian seeds have only just been planted."}, {"The Asgarnian hops plant grows a bit taller."}, {"The Asgarnian hops plant grows a bit taller."}, {"The Asgarnian hops plant grows a bit taller."}, {"The upper new leaves appear dark green to the", "rest of the plant."},
				{"The Asgarnian hops plant is ready to harvest."}}), JUTE(5306, new String[][]{{"The Jute seeds have only just been planted."}, {"The jute plants grow taller."}, {"The jute plants grow taller."}, {"The jute plants grow taller."}, {"The jute plant grows taller. They are as high", "as the player."}, {"The jute plants are ready to harvest."}}), YANILLIAN(5309, new String[][]{{"The Yanillian seeds have only just been planted."}, {"The Yanillian hops plant grows a bit taller."}, {"The Yanillian hops plant grows a bit taller."}, {"The Yanillian hops plant grows a bit taller."}, {"The new leaves on the top of the Yanillian hops", "plant are dark green."}, {"The new leaves on the top of the Yanillian hops", "plant are dark green."}, {"The Yanillian hops plant is ready to harvest."}}), KRANDORIAN(5310, new String[][]{{"The Krandorian seeds have only just been planted."}, {"The Krandorian plant grows a bit taller."}, {"The Krandorian plant grows a bit taller."},
				{"The Krandorian plant grows a bit taller."}, {"The new leaves on top of the Krandorian plant are", "dark green."}, {"The Krandorian plant grows a bit taller."}, {"The new leaves on top of the Krandorian plant", "are dark green."}, {"The Krandorian plant is ready for harvesting."}}), WILDBLOOD(5311, new String[][]{{"The wildblood seeds have only just been planted."}, {"The wildblood hops plant grows a bit taller."}, {"The wildblood hops plant grows a bit taller."}, {"The wildblood hops plant grows a bit taller."}, {"The wildblood hops plant grows a bit taller."}, {"The wildblood hops plant grows a bit taller."}, {"The wildblood hops plant grows a bit taller."}, {"The new leaves at the top of the wildblood hops plant", "are dark green."}, {"The wildblood hops plant is ready to harvest."}});
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
					updateHopsStates();
					continue;
				}
			}
			HopsData hopsData = HopsData.forId(farmingSeeds[i]);
			if (hopsData == null) {
				continue;
			}
			long growthTimeTotal = hopsData.getGrowthTime();
			int totalStages = (hopsData.getEndingState() - hopsData.getStartingState());
			long growthTimePerStage = (growthTimeTotal / totalStages);
			if(difference >= growthTimePerStage) //in growth stage time (10 minutes for hops)
			{
				int nextStage = farmingStages[i] + 1;
				//if timer is 0 or if the plant is dead or fully grown go to next Hops index insted
				if (farmingTimer[i] == 0 || farmingState[i] == 3 || (nextStage > totalStages + 4)) {
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
					updateHopsStates();
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
			if (farmingWatched[index]) {
				farmingState[index] = 0;
				HopsData hopsData = HopsData.forId(farmingSeeds[index]);
				if (hopsData == null)
					return;
				farmingTimer[index] = Server.getMinutesCounter();
				modifyStage(index);
			} else {
				farmingState[index] = 3;
			}
		}

		if (farmingState[index] == 1) {
			diseaseChance[index] *= 2;
			farmingState[index] = 0;
		}

		if (farmingState[index] == 5 && farmingStages[index] != 3) {
			farmingState[index] = 0;
		}

		if (farmingState[index] == 0 && farmingStages[index] >= 5 && !hasFullyGrown[index]) {
			HopsData hopsData = HopsData.forId(farmingSeeds[index]);
			if (hopsData == null) {
				return;
			}

			double chance = diseaseChance[index] * hopsData.getDiseaseChance();
			int maxChance = (int)(chance * 100);
			if (Misc.random(100) <= maxChance) {
				farmingState[index] = 2;
			}
		}
	}
	
	public void modifyStage(int i) {
		HopsData hopsData = HopsData.forId(farmingSeeds[i]);
		if (hopsData == null)
			return;
		int totalStages = (hopsData.getEndingState() - hopsData.getStartingState());
		int nextStage = farmingStages[i] + 1;
		if(nextStage > totalStages)
			return;
		farmingStages[i] = nextStage;
		updateHopsStates();
	}
	
	/* update all the patch states */
	public void updateHopsStates() {
		// lumbridge - mc grubor - yanille - entrana
		int[] configValues = new int[farmingStages.length];

		int configValue;
		for (int i = 0; i < farmingStages.length; i++) {
			configValues[i] = getConfigValue(farmingStages[i], farmingSeeds[i], farmingState[i], i);
		}

		configValue = (configValues[0] << 16) + (configValues[1] << 8 << 16) + configValues[2] + (configValues[3] << 8);
		player.getActionSender().sendConfig(MAIN_HOPS_CONFIG, configValue);

	}

	/* getting the different config values */

	public int getConfigValue(int hopsStage, int seedId, int plantState, int index) {
		HopsData hopsData = HopsData.forId(seedId);
		switch (hopsStage) {
			case 0 :// weed
				return (GROWING << 6) + 0x00;
			case 1 :// weed cleared
				return (GROWING << 6) + 0x01;
			case 2 :
				return (GROWING << 6) + 0x02;
			case 3 :
				return (GROWING << 6) + 0x03;
		}
		if (hopsData == null) {
			return -1;
		}
		if (hopsData.getEndingState() == hopsData.getStartingState() + hopsStage - 1) {
			hasFullyGrown[index] = true;
		}

		return (getPlantState(plantState) << 6) + hopsData.getStartingState() + hopsStage - 4;
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
		final HopsFieldsData hopsFieldsData = HopsFieldsData.forIdPosition(new Position(objectX, objectY));
		if (hopsFieldsData == null) {
			return false;
		}
		HopsData hopsData = HopsData.forId(farmingSeeds[hopsFieldsData.getHopsIndex()]);
		if (hopsData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (farmingState[hopsFieldsData.getHopsIndex()] == 1 || farmingStages[hopsFieldsData.getHopsIndex()] <= 1 || farmingStages[hopsFieldsData.getHopsIndex()] == hopsData.getEndingState() - hopsData.getStartingState() + 4) {
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
				diseaseChance[hopsFieldsData.getHopsIndex()] *= WATERING_CHANCE;
				farmingState[hopsFieldsData.getHopsIndex()] = 1;
				container.stop();
			}

			@Override
			public void stop() {
				updateHopsStates();
				player.setStopPacket(false);
				player.resetAnimation();
			}
		}, 5);
		return true;
	}

	/* clearing the patch with a rake of a spade */

	public boolean clearPatch(int objectX, int objectY, int itemId) {
		final HopsFieldsData hopsFieldsData = HopsFieldsData.forIdPosition(new Position(objectX, objectY));
		int finalAnimation;
		int finalDelay;
		if (hopsFieldsData == null || (itemId != FarmingConstants.RAKE && itemId != FarmingConstants.SPADE)) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (farmingStages[hopsFieldsData.getHopsIndex()] == 3) {
			return true;
		}
		if (farmingStages[hopsFieldsData.getHopsIndex()] <= 3) {
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
				if (farmingStages[hopsFieldsData.getHopsIndex()] <= 2) {
					farmingStages[hopsFieldsData.getHopsIndex()]++;
					player.getInventory().addItemOrDrop(new Item(6055));
				} else {
					farmingStages[hopsFieldsData.getHopsIndex()] = 3;
					container.stop();
				}
				player.getSkill().addExp(Skill.FARMING, CLEARING_EXPERIENCE);
				farmingTimer[hopsFieldsData.getHopsIndex()] = Server.getMinutesCounter();
				updateHopsStates();
				if (farmingStages[hopsFieldsData.getHopsIndex()] == 3) {
					container.stop();
					return;
				}
			}

			@Override
			public void stop() {
				resetHops(hopsFieldsData.getHopsIndex());
				player.getActionSender().sendMessage("You clear the patch.");
				player.setStopPacket(false);
				player.resetAnimation();
			}
		}, finalDelay);
		return true;

	}

	/* planting the seeds */

	public boolean plantSeed(int objectX, int objectY, final int seedId) {
		final HopsFieldsData hopsFieldsData = HopsFieldsData.forIdPosition(new Position(objectX, objectY));
		final HopsData hopsData = HopsData.forId(seedId);
		if (hopsFieldsData == null || hopsData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (farmingStages[hopsFieldsData.getHopsIndex()] != 3) {
			player.getActionSender().sendMessage("You can't plant a seed here.");
			return false;
		}
		if (hopsData.getLevelRequired() > player.getSkill().getLevel()[Skill.FARMING]) {
			player.getDialogue().sendStatement("You need a farming level of " + hopsData.getLevelRequired() + " to plant this seed.");
			return true;
		}
		if (!player.getInventory().getItemContainer().contains(FarmingConstants.SEED_DIBBER)) {
			player.getDialogue().sendStatement("You need a seed dibber to plant seed here.");
			return true;
		}
		if (player.getInventory().getItemAmount(hopsData.getSeedId()) < hopsData.getSeedAmount()) {
			player.getDialogue().sendStatement("You need atleast " + hopsData.getSeedAmount() + " seeds to plant here.");
			return true;
		}
		player.getUpdateFlags().sendAnimation(FarmingConstants.SEED_DIBBING);
		farmingStages[hopsFieldsData.getHopsIndex()] = 4;
		player.getInventory().removeItem(new Item(seedId, hopsData.getSeedAmount()));

		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				farmingState[hopsFieldsData.getHopsIndex()] = 0;
				farmingSeeds[hopsFieldsData.getHopsIndex()] = seedId;
				farmingTimer[hopsFieldsData.getHopsIndex()] = Server.getMinutesCounter();
				player.getSkill().addExp(Skill.FARMING, hopsData.getPlantingXp());
				container.stop();
			}

			@Override
			public void stop() {
				updateHopsStates();
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
		final HopsFieldsData hopsFieldsData = HopsFieldsData.forIdPosition(new Position(objectX, objectY));
		if (hopsFieldsData == null) {
			return false;
		}
		final HopsData hopsData = HopsData.forId(farmingSeeds[hopsFieldsData.getHopsIndex()]);
		if (hopsData == null) {
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

			@Override
			public void execute(CycleEventContainer container) {
				if (farmingHarvest[hopsFieldsData.getHopsIndex()] == 0) {
					farmingHarvest[hopsFieldsData.getHopsIndex()] = (int) (1 + (START_HARVEST_AMOUNT + Misc.random(END_HARVEST_AMOUNT - START_HARVEST_AMOUNT)) * (player.getEquipment().getItemContainer().contains(7409) ? 1.10 : 1));
				}
				if (farmingHarvest[hopsFieldsData.getHopsIndex()] == 1) {
					resetHops(hopsFieldsData.getHopsIndex());
					farmingStages[hopsFieldsData.getHopsIndex()] = 3;
					farmingTimer[hopsFieldsData.getHopsIndex()] = Server.getMinutesCounter();
					container.stop();
					return;
				}
				if (!player.checkTask(task) || player.getInventory().getItemContainer().freeSlots() <= 0) {
					container.stop();
					return;
				}
				farmingHarvest[hopsFieldsData.getHopsIndex()]--;
				player.getUpdateFlags().sendAnimation(FarmingConstants.SPADE_ANIM);
				player.getActionSender().sendMessage("You harvest the crop, and get some vegetables.");
				player.getInventory().addItem(new Item(hopsData.getHarvestId()));
				player.getSkill().addExp(Skill.FARMING, hopsData.getHarvestXp());
			}

			@Override
			public void stop() {
				updateHopsStates();
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
		final HopsFieldsData hopsFieldsData = HopsFieldsData.forIdPosition(new Position(objectX, objectY));
		if (hopsFieldsData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (farmingStages[hopsFieldsData.getHopsIndex()] != 3 || farmingState[hopsFieldsData.getHopsIndex()] == 5) {
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
				diseaseChance[hopsFieldsData.getHopsIndex()] *= itemId == 6032 ? COMPOST_CHANCE : SUPERCOMPOST_CHANCE;
				farmingState[hopsFieldsData.getHopsIndex()] = 5;
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
		final HopsFieldsData hopsFieldsData = HopsFieldsData.forIdPosition(new Position(objectX, objectY));
		if (hopsFieldsData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		final InspectData inspectData = InspectData.forId(farmingSeeds[hopsFieldsData.getHopsIndex()]);
		final HopsData hopsData = HopsData.forId(farmingSeeds[hopsFieldsData.getHopsIndex()]);
		if (farmingState[hopsFieldsData.getHopsIndex()] == 2) {
			player.getDialogue().sendStatement("This plant is diseased. Use a plant cure on it to cure it, ", "or clear the patch with a spade.");
			return true;
		} else if (farmingState[hopsFieldsData.getHopsIndex()] == 3) {
			player.getDialogue().sendStatement("This plant is dead. You did not cure it while it was diseased.", "Clear the patch with a spade.");
			return true;
		}
		if (farmingStages[hopsFieldsData.getHopsIndex()] == 0) {
			player.getDialogue().sendStatement("This is a hops patch. The soil has not been treated.", "The patch needs weeding.");
		} else if (farmingStages[hopsFieldsData.getHopsIndex()] == 3) {
			player.getDialogue().sendStatement("This is a hops patch. The soil has not been treated.", "The patch is empty and weeded.");
		} else if (inspectData != null && hopsData != null) {
			player.getActionSender().sendMessage("You bend down and start to inspect the patch...");

			player.getUpdateFlags().sendAnimation(1331);
			player.setStopPacket(true);
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {

				@Override
				public void execute(CycleEventContainer container) {
					if (farmingStages[hopsFieldsData.getHopsIndex()] - 4 < inspectData.getMessages().length - 2) {
						player.getDialogue().sendStatement(inspectData.getMessages()[farmingStages[hopsFieldsData.getHopsIndex()] - 4]);
					} else if (farmingStages[hopsFieldsData.getHopsIndex()] < hopsData.getEndingState() - hopsData.getStartingState() + 2) {
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
		final HopsFieldsData hopsFieldsData = HopsFieldsData.forIdPosition(new Position(objectX, objectY));
		if (hopsFieldsData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		player.getSkillGuide().farmingComplex(2);
		player.getSkillGuide().selected = 20;
		return true;
	}

	/* Curing the plant */

	public boolean curePlant(int objectX, int objectY, int itemId) {
		final HopsFieldsData hopsFieldsData = HopsFieldsData.forIdPosition(new Position(objectX, objectY));
		if (hopsFieldsData == null || itemId != 6036) {
			return false;
		}
		final HopsData hopsData = HopsData.forId(farmingSeeds[hopsFieldsData.getHopsIndex()]);
		if (hopsData == null) {
			return false;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (farmingState[hopsFieldsData.getHopsIndex()] != 2) {
			player.getActionSender().sendMessage("This plant doesn't need to be cured.");
			return true;
		}
		player.getInventory().removeItem(new Item(itemId));
		player.getInventory().addItem(new Item(229));
		player.getUpdateFlags().sendAnimation(FarmingConstants.CURING_ANIM);
		player.setStopPacket(true);
		farmingState[hopsFieldsData.getHopsIndex()] = 0;
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				player.getActionSender().sendMessage("You cure the plant with a plant cure.");
				container.stop();
			}

			@Override
			public void stop() {
				updateHopsStates();
				player.setStopPacket(false);
				player.resetAnimation();
			}
		}, 7);

		return true;

	}

	private void resetHops(int index) {
		farmingSeeds[index] = 0;
		farmingState[index] = 0;
		diseaseChance[index] = 1;
		farmingHarvest[index] = 0;
		hasFullyGrown[index] = false;
		farmingWatched[index] = false;
	}

	/* checking if the patch is raked */

	public boolean checkIfRaked(int objectX, int objectY) {
		final HopsFieldsData hopsFieldsData = HopsFieldsData.forIdPosition(new Position(objectX, objectY));
		if (hopsFieldsData == null)
			return false;
		if (farmingStages[hopsFieldsData.getHopsIndex()] == 3)
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
