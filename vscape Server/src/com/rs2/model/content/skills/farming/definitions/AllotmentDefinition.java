package com.rs2.model.content.skills.farming.definitions;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AllotmentDefinition {

	private static ArrayList<AllotmentDefinition> definitions = new ArrayList<AllotmentDefinition>();
	public static void init() throws IOException {
		definitions.clear();
		FileReader reader = new FileReader("./datajson/farming/allotments.json");
		try
		{
			definitions = new Gson().fromJson(reader, new TypeToken<List<AllotmentDefinition>>(){}.getType());
			reader.close();
			System.out.println("Loaded " + definitions.size() + " Allotment definitions json.");
		} catch (IOException e) {
			reader.close();
			System.out.println("failed to load Allotment definitions json.");
		}
	}
	
	public static AllotmentDefinition forSeedId(int id) {
		for (AllotmentDefinition def : definitions) {
			if(def.seedId == id)
			{
				return def;
			}
		}
		return null;
	}
	
	public static AllotmentDefinition forHarvestId(int id) {
		for (AllotmentDefinition def : definitions) {
			if(def.harvestId == id)
			{
				return def;
			}
		}
		return null;
	}
	
	public AllotmentDefinition(int seedId, int harvestId, int protectionId, int seedsRequired, int levelRequired,
			int[] watchPayment, int totalGrowthTime, double diseaseChance, double plantExp, double harvestExp,
			int startConfig, int endConfig, String[][] stateMessages) {
			this.seedId = seedId;
			this.harvestId = harvestId;
			this.protectionId = protectionId;
			this.seedsRequired = seedsRequired;
			this.levelRequired = levelRequired;
			this.watchPayment = watchPayment;
			this.totalGrowthTime = totalGrowthTime;
			this.diseaseChance = diseaseChance;
			this.plantExp = plantExp;
			this.harvestExp = harvestExp;
			this.startConfig = startConfig; 
			this.endConfig = endConfig;
			this.stateMessages = stateMessages;
	}
	
	private String name;
	private int seedId = -1, harvestId = -1, protectionId = -1, seedsRequired = 1, levelRequired = 1;
	private int[] watchPayment = null;
	private int totalGrowthTime = 5;
	private double diseaseChance = 0.3, plantExp = 0, harvestExp = 0;
	private int startConfig = 0, endConfig = 0;
	public String[][] stateMessages = null;

	public String getName() {
		return name;
	}

	public int getSeedId() {
		return seedId;
	}

	public int getHarvestId() {
		return harvestId;
	}

	public int getProtectionId() {
		return protectionId;
	}

	public int getSeedsRequired() {
		return seedsRequired;
	}

	public int getLevelRequired() {
		return levelRequired;
	}

	public int[] getWatchPayment() {
		return watchPayment;
	}

	public int getTotalGrowthTime() {
		return totalGrowthTime;
	}

	public double getDiseaseChance() {
		return diseaseChance;
	}

	public double getPlantExp() {
		return plantExp;
	}

	public double getHarvestExp() {
		return harvestExp;
	}

	public int getStartConfig() {
		return startConfig;
	}

	public int getEndConfig() {
		return endConfig;
	}

	public String[][] getStateMessages() {
		return stateMessages;
	}
}
