package com.rs2.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.rs2.Constants;
import com.rs2.Server;
import com.rs2.model.content.combat.effect.impl.PoisonEffect;
import com.rs2.model.content.combat.hit.Hit;
import com.rs2.model.content.combat.hit.HitDef;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.quests.Quest;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.skills.magic.SpellBook;
import com.rs2.model.players.BankManager;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class PlayerSaveParser {
	
	// CHECK FOR NULLS AND ADD FALL
	public int parse(final Player player, File file)
	{
		try {
	        if (!file.exists()) {
	            if (Server.getSingleton() != null)
	                Server.getSingleton().queueLogin(player);
	            return 0;
	        }
	        try(GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(file))){
		        JsonElement mainElement = new JsonParser().parse(new BufferedReader(new InputStreamReader(gzip)));
		        gzip.close();
		        JsonObject mainObj = mainElement.getAsJsonObject();
		        JsonObject characterObj = mainObj.getAsJsonObject("character");
	            if(mainElement == null || mainObj == null || characterObj == null){
	            	player.setReturnCode(Constants.LOGIN_RESPONSE_COULD_NOT_COMPLETE_LOGIN);
	            	return 3;
	            }
		        player.setUsername(characterObj.get("username").getAsString());
	            String password = characterObj.get("password").getAsString();
	            if (!password.equalsIgnoreCase(player.getPassword())) {
	            	System.out.println("Wrong login- "+password+"");
	            	player.setReturnCode(Constants.LOGIN_RESPONSE_INVALID_CREDENTIALS);
	            	return 3;
	            } else {
	            	System.out.println("correct login - "+password+"");
	            }
	            player.setPassword(password);
	            player.setStaffRights(characterObj.get("rights").getAsInt());
                player.setMuteExpire(characterObj.get("muteExpire").getAsInt());
                player.setBanExpire(characterObj.get("banExpire").getAsInt());
	            JsonObject position = characterObj.getAsJsonObject("position");
	            if(position != null){
	            player.getPosition().setX(position.get("x") != null ? position.get("x").getAsInt() : Constants.START_X);
	            player.getPosition().setLastX(player.getPosition().getX());
	            player.getPosition().setY(position.get("y") != null ? position.get("y").getAsInt() : Constants.START_Y);
	            player.getPosition().setLastY(player.getPosition().getY() + 1);
	            player.getPosition().setZ(position.get("z") != null ? position.get("z").getAsInt() : Constants.START_Z);
	            }
	            JsonObject appearance = characterObj.getAsJsonObject("appearance");
	            if(appearance != null) {
		            player.setGender(appearance.get("gender").getAsInt());
		            JsonArray appearanceData = appearance.getAsJsonArray("appearanceData");
		            if(appearanceData != null && appearanceData.size() > 0){
			            for (int i = 0; i < player.getAppearance().length; i++) {
			            	if(appearanceData.get(i) != null)
			            		player.getAppearance()[i] = appearanceData.get(i).getAsInt();
			            }
		            }
		            JsonArray colorData = appearance.getAsJsonArray("colorData");
		            if(colorData != null && colorData.size() > 0){
			            for (int i = 0; i < player.getColors().length; i++) {
			            	if(colorData.get(i) != null)
			            		player.getColors()[i] = colorData.get(i).getAsInt();
			            }
		            }
	            }
	            JsonObject options = characterObj.getAsJsonObject("options");
	            if(options != null) {
		            boolean yellColor = options.get("yellColor").getAsBoolean();
		            player.setHideColors(yellColor, yellColor);
		            player.setEnergy(options.get("runenergy").getAsDouble());
		            player.getMovementHandler().setRunToggled(options.get("runtoggled").getAsBoolean());
		            player.setScreenBrightness(options.get("brightness").getAsInt());
		            player.setMouseButtons(options.get("mousebuttons").getAsInt());
		            player.setChatEffects(options.get("chateffects").getAsInt());
		            player.setSplitPrivateChat(options.get("splitprivate").getAsInt());
		            player.setAcceptAid(options.get("acceptaid").getAsInt());
		            player.setMusicVolume(options.get("musicvolume").getAsInt());
		            player.setEffectVolume(options.get("effectvolume").getAsInt());
	            }
	            player.getNewComersSide().setTutorialIslandStage(characterObj.get("tutorialstage").getAsInt(), false);
	            player.getNewComersSide().setProgressValue(characterObj.get("tutorialprogress").getAsInt());
	            JsonObject combat = characterObj.getAsJsonObject("combat");
	            if(combat != null){
		            player.setAutoRetaliate(combat.get("autoretaliate").getAsBoolean());
		            player.setFightMode(combat.get("fightmode").getAsInt());
		            player.setSpecialAmount(combat.get("specialamount").getAsInt());
	            	player.setMagicBookType(combat.get("magicBook").getAsBoolean() ? SpellBook.ANCIENT : SpellBook.MODERN);
	            	int skullTimer = combat.get("skulltimer").getAsInt();
	            	if (skullTimer > 0)
	            		player.addSkull(player, skullTimer);
	                player.getPoisonImmunity().setWaitDuration(combat.get("poisonImmunity").getAsInt());
	                player.getPoisonImmunity().reset();
	                player.getFireImmunity().setWaitDuration(combat.get("fireImmunity").getAsInt());
	                player.getFireImmunity().reset();
	                player.getTeleblockTimer().setWaitDuration(combat.get("teleBlockTimer").getAsInt());
	                player.getTeleblockTimer().reset();
	            	double poison = combat.get("poisonDamage").getAsDouble();
	                if (poison > 0) {
	                	HitDef hitDef = new HitDef(null, HitType.POISON, Math.ceil(poison)).setStartingHitDelay(30);
	    				Hit hit = new Hit(player, player, hitDef);
	    				PoisonEffect p = new PoisonEffect(poison, false);
	    				p.initialize(hit);
	                }
	                JsonArray degradeableHits = combat.getAsJsonArray("degradeableHits");
	                if(degradeableHits != null && degradeableHits.size() > 0){
		    			for (int i = 0; i < player.getDegradeableHits().length; i++) {
		    				player.setDegradeableHits(i, degradeableHits.get(i).getAsInt());
		    			}
	                }
	            }
    			JsonObject itemData = characterObj.getAsJsonObject("itemData");
    			if(itemData != null){
	                player.setBindingNeckCharge(itemData.get("bindingcharge").getAsInt());
	                player.setRingOfForgingLife(itemData.get("forginglife").getAsInt());
	                player.setRingOfRecoilLife(itemData.get("recoillife").getAsInt());
	                player.setClayBraceletLife(itemData.get("claybracelet").getAsInt());
	                player.setGodBook(itemData.get("godbook").getAsInt());
	                player.setHasUsedFreeGauntletsCharge(itemData.get("usedFreeGauntletsCharge").getAsBoolean());
	                player.setDefender(itemData.get("defender").getAsInt());
	                player.setDfsCharges(itemData.get("dfsCharges").getAsInt());
	                JsonArray pouchData = itemData.getAsJsonArray("pouchData");
	                if(pouchData != null && pouchData.size() > 0){
		                for (int i = 0; i < 4; i++) {
		                    player.setPouchData(i, pouchData.get(i).getAsInt());
		                }
	                }
    			}
                JsonObject worldData = characterObj.getAsJsonObject("worldData");
                if(worldData != null){
	                player.setCoalTruckAmount(worldData.get("coaltrucks").getAsInt());
	                player.setBrimhavenDungeonOpen(worldData.get("brimhavenOpen").getAsBoolean());
                }
                JsonObject npcData = characterObj.getAsJsonObject("npcData");
                if(npcData != null){
	                player.setRunecraftNpc(npcData.get("runecraftNpc").getAsInt());
	                player.setKilledClueAttacker(npcData.get("killedClueAttacker").getAsBoolean());
	                player.setKilledTreeSpirit(npcData.get("killedTreeSpirit").getAsBoolean());
	                player.setKilledJungleDemon(npcData.get("killedJungleDemon").getAsBoolean());
                }
                JsonObject slayerData = characterObj.getAsJsonObject("slayerData");
                if(slayerData != null){
	                player.getSlayer().slayerMaster = slayerData.get("slayerMaster").getAsInt();
	            	player.getSlayer().slayerTask = slayerData.get("slayerTask").getAsString();
	            	player.getSlayer().taskAmount = slayerData.get("taskAmount").getAsInt();
                }else{
                	player.getSlayer().resetSlayerTask();
                }
            	
            	JsonArray friends = mainObj.getAsJsonArray("friends");
            	if(friends != null && friends.size() > 0){
		            for (int i = 0; i < player.getFriends().length; i++) {
		                player.getFriends()[i] = friends.get(i).getAsLong();
		            }
            	}
            	JsonArray ignores = mainObj.getAsJsonArray("ignores");
            	if(ignores != null && ignores.size() > 0){
		            for (int i = 0; i < player.getIgnores().length; i++) {
		                player.getIgnores()[i] = ignores.get(i).getAsLong();
		            }
            	}
	            JsonArray skills = mainObj.getAsJsonArray("skills");
	            if(skills != null && skills.size() > 0){
		    		for (int i = 0; i < 22; i++) {
		    			JsonObject levelObj = skills.get(i).getAsJsonObject();
		    			int level = levelObj.get("lvl").getAsInt();
		    			double xp = levelObj.get("xp").getAsDouble();
		                player.getSkill().getLevel()[i] = level;
		                player.getSkill().getExp()[i] = xp;
		    		}
	            }
	    		JsonArray inventory = mainObj.getAsJsonArray("inventory");
	    		if(inventory != null && inventory.size() > 0){
		            for (int i = 0; i < 28; i++) {
		            	JsonObject itemObj = inventory.get(i).getAsJsonObject();
		            	int id = itemObj.get("id").getAsInt();
		                if (id != 65535) {
			            	int amount = itemObj.get("count").getAsInt();
			            	int timer = itemObj.get("timer").getAsInt();
		                    if (id < Constants.MAX_ITEMS && amount > 0)  {
		                        Item item = new Item(id, amount, timer);
		                        if (item.getId() == 2696 || item.getId() == 2699 || item.getId() == 3510) {
		                        	item = new Item(id - 1, amount, timer);
		                        }
		                        player.getInventory().getItemContainer().set(i, item);
		                    }
		                }
		            }
	    		}
	            JsonArray equipment = mainObj.getAsJsonArray("equipment");
	            if(equipment != null && equipment.size() > 0){
		            for (int i = 0; i < 14; i++) {
		            	JsonObject itemObj = equipment.get(i).getAsJsonObject();
		            	int id = itemObj.get("id").getAsInt();
		                if (id != 65535) {
		                	int amount = itemObj.get("count").getAsInt();
		                    if (id < Constants.MAX_ITEMS && amount > 0)  {
		                        Item item = new Item(id, amount);
		                        player.getEquipment().getItemContainer().set(i, item);
		                    }
		                }
		            }
	            }
	            JsonObject bank = mainObj.getAsJsonObject("bank");
	            if(bank != null){
		            player.getBankPin().setChangingBankPin(bank.get("changingpin").getAsBoolean());
		            player.getBankPin().setDeletingBankPin(bank.get("deletingpin").getAsBoolean());
		            player.getBankPin().setPinAppendYear(bank.get("appendYear").getAsInt());
		            player.getBankPin().setPinAppendDay(bank.get("appendDay").getAsInt());
		            JsonArray bankPin = bank.getAsJsonArray("pin");
		            if(bankPin != null && bankPin.size() > 0){
			            for (int i = 0; i < player.getBankPin().getBankPin().length; i++) {
			                player.getBankPin().getBankPin()[i] = bankPin.get(i).getAsInt();
			            }
		            }
		            JsonArray bankPinPending = bank.getAsJsonArray("pinPending");
		            if(bankPinPending != null && bankPinPending.size() > 0){
		            	for (int i = 0; i < player.getBankPin().getPendingBankPin().length; i++) {
			                player.getBankPin().getPendingBankPin()[i] = bankPinPending.get(i).getAsInt();
			            }
		            }
		            player.setResetBank(bank.get("hasReset").getAsBoolean());
		            JsonArray bankItems = bank.getAsJsonArray("items");
		            if(bankItems != null && bankItems.size() > 0){
		                for (int i = 0; i < BankManager.SIZE; i++) {
			            	JsonObject itemObj = bankItems.get(i).getAsJsonObject();
			            	int id = itemObj.get("id").getAsInt();
		                    if (id != 65535) {
				            	int amount = itemObj.get("count").getAsInt();
				            	int timer = itemObj.get("timer").getAsInt();
		                        if (id < Constants.MAX_ITEMS && amount > 0)  {
		                            Item item = new Item(id, amount, timer);
		                            if (item.getId() == 2696 || item.getId() == 2699 || item.getId() == 3510) {
		                            	item = new Item(id - 1, amount, timer);
		                            }
		                            player.getBank().set(i, item);
		                        }
		                    }
		                }
		            }
	            }
	            JsonArray pendingItems = mainObj.getAsJsonArray("pendingItems");
	            if(pendingItems != null && pendingItems.size() > 0){
		            for (int i = 0; i < player.getPendingItems().length; i++) {
		            	JsonObject itemObj = pendingItems.get(i).getAsJsonObject();
		                player.getPendingItems()[i] = itemObj.get("id").getAsInt();
		                player.getPendingItemsAmount()[i] = itemObj.get("count").getAsInt();
		            }
	            }
	            JsonObject quests = mainObj.getAsJsonObject("quests");
	            if(quests != null){
		            player.setQuestPoints(quests.get("questpoints").getAsInt());
		            JsonObject questVars = quests.getAsJsonObject("questVars");
		            if(questVars != null){
			            player.joinPhoenixGang(questVars.get("phoenixGang").getAsBoolean());
			            player.joinBlackArmGang(questVars.get("blackArmGang").getAsBoolean());
			            player.setMelzarsDoorUnlock(questVars.get("melzarsDoorUnlock").getAsBoolean());
		            	player.setBananaCrate(questVars.get("bananaCrate").getAsBoolean());
		            	player.setBananaCrateCount(questVars.get("bananaCrateCount").getAsInt());
		            	player.setEctoWorshipCount(questVars.get("ectoWorshipCount").getAsInt());
		            	player.dyeGhostsAhoyFlag("top", questVars.get("topHalfFlag").getAsString());
		            	player.dyeGhostsAhoyFlag("bottom", questVars.get("bottomHalfFlag").getAsString());
		            	player.dyeGhostsAhoyFlag("skull", questVars.get("skullFlag").getAsString());
			    		player.setDesiredGhostsAhoyFlag("top", questVars.get("desiredTopHalfFlag").getAsString());
			    		player.setDesiredGhostsAhoyFlag("bottom", questVars.get("desiredBottomHalfFlag").getAsString());
			    		player.setDesiredGhostsAhoyFlag("top", questVars.get("desiredSkullFlag").getAsString());
		            	player.setPetitionSigned(questVars.get("petitionSigned").getAsBoolean());
		            	player.setGivenSnailSlime(questVars.get("snailSlime").getAsBoolean());
		            	player.setGivenIdPapers(questVars.get("idPapers").getAsBoolean());
		            }
	            	JsonArray questData = quests.getAsJsonArray("questData");
	            	if(questData != null && questData.size() > 0){
	            		for (int i = 0; i < QuestHandler.getQuests().length; i++)
						{
							Quest q = QuestHandler.getQuests()[i];
							if(q == null) continue;
							
							JsonObject questDataObj = questData.get(i).getAsJsonObject();
							String questName = questDataObj.get("name").getAsString();
							if (questName != null && questName.equals(q.getQuestSaveName())) 
							{
								int questStage = questDataObj.get("stage").getAsInt();
							    if(q == QuestHandler.getQuests()[7]) {
							    	player.setQuestStage(7, 0);
							    } else {
									if(questStage >= 0) {
									    player.setQuestStage(q.getQuestID(), questStage);
									}
							    }
							} else { continue; }
						}
	            	}
	            }
	            JsonObject minigames = mainObj.getAsJsonObject("minigames");
	            if(minigames != null){
		            JsonObject barrows = minigames.getAsJsonObject("barrows");
		            if(barrows != null){
		                player.setRandomGrave(barrows.get("grave").getAsInt());
		                player.setKillCount(barrows.get("killCount").getAsInt());
		                JsonArray brothersKilled = barrows.getAsJsonArray("brothersKilled");
		                if(brothersKilled != null && brothersKilled.size() > 0){
			                for (int i = 0; i < 6; i++) {
			                	player.setBarrowsNpcDead(i, brothersKilled.get(i).getAsBoolean());
			                }
		                }
		            }
		            JsonObject pestControl = minigames.getAsJsonObject("pestControl");
		            if(pestControl != null){
		            	player.setPcPoints(pestControl.get("pcPoints").getAsInt(), player);
		            }
		            JsonObject mageArena = minigames.getAsJsonObject("mageArena");
		            if(mageArena != null){
		            	player.saveZamorakCasts(mageArena.get("zamorakCasts").getAsInt());
		            	player.saveSaradominCasts(mageArena.get("saradominCasts").getAsInt());
		            	player.saveGuthixCasts(mageArena.get("guthixCasts").getAsInt());
		            	player.setMageArenaStage(mageArena.get("arenaStage").getAsInt());
		            }
		            JsonObject fightCaves = minigames.getAsJsonObject("fightCaves");
		            if(fightCaves != null){
		            	player.setFightCavesWave(fightCaves.get("wave").getAsInt());
		            }
	            }
	            JsonObject farming = mainObj.getAsJsonObject("farming");
		        if(farming != null){
		            JsonObject allotments = farming.getAsJsonObject("allotments");
		            if(allotments != null){
		                JsonArray stages = allotments.getAsJsonArray("stages");
		                if(stages != null && stages.size() > 0){
			    			for (int i = 0; i < player.getAllotment().getFarmingStages().length; i++) {
			    				player.getAllotment().setFarmingStages(i, stages.get(i).getAsInt());
			    			}
		                }
		                JsonArray seeds = allotments.getAsJsonArray("seeds");
		                if(seeds != null && seeds.size() > 0){
			    			for (int i = 0; i < player.getAllotment().getFarmingSeeds().length; i++) {
			    				player.getAllotment().setFarmingSeeds(i, seeds.get(i).getAsInt());
			    			}
		                }
		                JsonArray harvest = allotments.getAsJsonArray("harvest");
		                if(harvest != null && harvest.size() > 0){
			    			for (int i = 0; i < player.getAllotment().getFarmingHarvest().length; i++) {
			    				player.getAllotment().setFarmingHarvest(i, harvest.get(i).getAsInt());
			    			}
		                }
		                JsonArray state = allotments.getAsJsonArray("state");
		                if(state != null && state.size() > 0){
			    			for (int i = 0; i < player.getAllotment().getFarmingState().length; i++) {
			    				player.getAllotment().setFarmingState(i, state.get(i).getAsInt());
			    			}
		                }
		                JsonArray timer = allotments.getAsJsonArray("timer");
		                if(timer != null && timer.size() > 0){
			    			for (int i = 0; i < player.getAllotment().getFarmingTimer().length; i++) {
			    				player.getAllotment().setFarmingTimer(i, timer.get(i).getAsLong());
			    			}
		                }
		                JsonArray diseaseChance = allotments.getAsJsonArray("diseaseChance");
		                if(diseaseChance != null && diseaseChance.size() > 0){
			    			for (int i = 0; i < player.getAllotment().getDiseaseChance().length; i++) {
			    				player.getAllotment().setDiseaseChance(i, diseaseChance.get(i).getAsDouble());
			    			}
		                }
		                JsonArray watched = allotments.getAsJsonArray("watched");
		                if(watched != null && watched.size() > 0){
			    			for (int i = 0; i < player.getAllotment().getFarmingWatched().length; i++) {
			    				player.getAllotment().setFarmingWatched(i, watched.get(i).getAsBoolean());
			    			}
		                }
		            }
		            JsonObject bushes = farming.getAsJsonObject("bushes");
		            if(bushes != null){
		                JsonArray stages = bushes.getAsJsonArray("stages");
		                if(stages != null && stages.size() > 0){
			    			for (int i = 0; i < player.getBushes().getFarmingStages().length; i++) {
			    				player.getBushes().setFarmingStages(i, stages.get(i).getAsInt());
			    			}
		                }
		                JsonArray seeds = bushes.getAsJsonArray("seeds");
		                if(seeds != null && seeds.size() > 0){
			    			for (int i = 0; i < player.getBushes().getFarmingSeeds().length; i++) {
			    				player.getBushes().setFarmingSeeds(i, seeds.get(i).getAsInt());
			    			}
		                }
		                JsonArray state = bushes.getAsJsonArray("state");
		                if(state != null && state.size() > 0){
			    			for (int i = 0; i < player.getBushes().getFarmingState().length; i++) {
			    				player.getBushes().setFarmingState(i, state.get(i).getAsInt());
			    			}
		                }
		                JsonArray timer = bushes.getAsJsonArray("timer");
		                if(timer != null && timer.size() > 0){
			    			for (int i = 0; i < player.getBushes().getFarmingTimer().length; i++) {
			    				player.getBushes().setFarmingTimer(i, timer.get(i).getAsLong());
			    			}
		                }
		                JsonArray diseaseChance = bushes.getAsJsonArray("diseaseChance");
		                if(diseaseChance != null && diseaseChance.size() > 0){
			    			for (int i = 0; i < player.getBushes().getDiseaseChance().length; i++) {
			    				player.getBushes().setDiseaseChance(i, diseaseChance.get(i).getAsDouble());
			    			}
		                }
		                JsonArray watched = bushes.getAsJsonArray("watched");
		                if(watched != null && watched.size() > 0){
			    			for (int i = 0; i < player.getBushes().getFarmingWatched().length; i++) {
			    				player.getBushes().setFarmingWatched(i, watched.get(i).getAsBoolean());
			    			}
		                }
		            }
		            JsonObject flowers = farming.getAsJsonObject("flowers");
		            if(flowers != null){
		                JsonArray stages = flowers.getAsJsonArray("stages");
		                if(stages != null && stages.size() > 0){
			    			for (int i = 0; i < player.getFlowers().getFarmingStages().length; i++) {
			    				player.getFlowers().setFarmingStages(i, stages.get(i).getAsInt());
			    			}
		                }
		                JsonArray seeds = flowers.getAsJsonArray("seeds");
		                if(seeds != null && seeds.size() > 0){
			    			for (int i = 0; i < player.getFlowers().getFarmingSeeds().length; i++) {
			    				player.getFlowers().setFarmingSeeds(i, seeds.get(i).getAsInt());
			    			}
		                }
		                JsonArray state = flowers.getAsJsonArray("state");
		                if(state != null && state.size() > 0){
			    			for (int i = 0; i < player.getFlowers().getFarmingState().length; i++) {
			    				player.getFlowers().setFarmingState(i, state.get(i).getAsInt());
			    			}
		                }
		                JsonArray timer = flowers.getAsJsonArray("timer");
		                if(timer != null && timer.size() > 0){
			    			for (int i = 0; i < player.getFlowers().getFarmingTimer().length; i++) {
			    				player.getFlowers().setFarmingTimer(i, timer.get(i).getAsLong());
			    			}
		                }
		                JsonArray diseaseChance = flowers.getAsJsonArray("diseaseChance");
		                if(diseaseChance != null && diseaseChance.size() > 0){
			    			for (int i = 0; i < player.getFlowers().getDiseaseChance().length; i++) {
			    				player.getFlowers().setDiseaseChance(i, diseaseChance.get(i).getAsDouble());
			    			}
		                }
		            }
		            JsonObject fruitTrees = farming.getAsJsonObject("fruitTrees");
		            if(fruitTrees != null){
		                JsonArray stages = fruitTrees.getAsJsonArray("stages");
		                if(stages != null && stages.size() > 0){
			    			for (int i = 0; i < player.getFruitTrees().getFarmingStages().length; i++) {
			    				player.getFruitTrees().setFarmingStages(i, stages.get(i).getAsInt());
			    			}
		                }
		                JsonArray seeds = fruitTrees.getAsJsonArray("seeds");
		                if(seeds != null && seeds.size() > 0){
			    			for (int i = 0; i < player.getFruitTrees().getFarmingSeeds().length; i++) {
			    				player.getFruitTrees().setFarmingSeeds(i, seeds.get(i).getAsInt());
			    			}
		                }
		                JsonArray state = fruitTrees.getAsJsonArray("state");
		                if(state != null && state.size() > 0){
			    			for (int i = 0; i < player.getFruitTrees().getFarmingState().length; i++) {
			    				player.getFruitTrees().setFarmingState(i, state.get(i).getAsInt());
			    			}
		                }
		                JsonArray timer = fruitTrees.getAsJsonArray("timer");
		                if(timer != null && timer.size() > 0){
			    			for (int i = 0; i < player.getFruitTrees().getFarmingTimer().length; i++) {
			    				player.getFruitTrees().setFarmingTimer(i, timer.get(i).getAsLong());
			    			}
		                }
		                JsonArray diseaseChance = fruitTrees.getAsJsonArray("diseaseChance");
		                if(diseaseChance != null && diseaseChance.size() > 0){
			    			for (int i = 0; i < player.getFruitTrees().getDiseaseChance().length; i++) {
			    				player.getFruitTrees().setDiseaseChance(i, diseaseChance.get(i).getAsDouble());
			    			}
		                }
		                JsonArray watched = fruitTrees.getAsJsonArray("watched");
		                if(watched != null && watched.size() > 0){
			    			for (int i = 0; i < player.getFruitTrees().getFarmingWatched().length; i++) {
			    				player.getFruitTrees().setFarmingWatched(i, watched.get(i).getAsBoolean());
			    			}
		                }
		            }
		            JsonObject herbs = farming.getAsJsonObject("herbs");
		            if(herbs != null){
		                JsonArray stages = herbs.getAsJsonArray("stages");
		                if(stages != null && stages.size() > 0){
			    			for (int i = 0; i < player.getHerbs().getFarmingStages().length; i++) {
			    				player.getHerbs().setFarmingStages(i, stages.get(i).getAsInt());
			    			}
		                }
		                JsonArray seeds = herbs.getAsJsonArray("seeds");
		                if(seeds != null && seeds.size() > 0){
			    			for (int i = 0; i < player.getHerbs().getFarmingSeeds().length; i++) {
			    				player.getHerbs().setFarmingSeeds(i, seeds.get(i).getAsInt());
			    			}
		                }
		                JsonArray harvest = herbs.getAsJsonArray("harvest");
		                if(harvest != null && harvest.size() > 0){
			    			for (int i = 0; i < player.getHerbs().getFarmingHarvest().length; i++) {
			    				player.getHerbs().setFarmingHarvest(i, harvest.get(i).getAsInt());
			    			}
		                }
		                JsonArray state = herbs.getAsJsonArray("state");
		                if(state != null && state.size() > 0){
			    			for (int i = 0; i < player.getHerbs().getFarmingState().length; i++) {
			    				player.getHerbs().setFarmingState(i, state.get(i).getAsInt());
			    			}
		                }
		                JsonArray timer = herbs.getAsJsonArray("timer");
		                if(timer != null && timer.size() > 0){
			    			for (int i = 0; i < player.getHerbs().getFarmingTimer().length; i++) {
			    				player.getHerbs().setFarmingTimer(i, timer.get(i).getAsLong());
			    			}
		                }
		                JsonArray diseaseChance = herbs.getAsJsonArray("diseaseChance");
		                if(diseaseChance != null && diseaseChance.size() > 0){
			    			for (int i = 0; i < player.getHerbs().getDiseaseChance().length; i++) {
			    				player.getHerbs().setDiseaseChance(i, diseaseChance.get(i).getAsDouble());
			    			}
		                }
		            }
		            JsonObject hops = farming.getAsJsonObject("hops");
		            if(hops != null){
		                JsonArray stages = hops.getAsJsonArray("stages");
		                if(stages != null && stages.size() > 0){
			    			for (int i = 0; i < player.getHops().getFarmingStages().length; i++) {
			    				player.getHops().setFarmingStages(i, stages.get(i).getAsInt());
			    			}
		                }
		                JsonArray seeds = hops.getAsJsonArray("seeds");
		                if(seeds != null && seeds.size() > 0){
			    			for (int i = 0; i < player.getHops().getFarmingSeeds().length; i++) {
			    				player.getHops().setFarmingSeeds(i, seeds.get(i).getAsInt());
			    			}
		                }
		                JsonArray harvest = hops.getAsJsonArray("harvest");
		                if(harvest != null && harvest.size() > 0){
			    			for (int i = 0; i < player.getHops().getFarmingHarvest().length; i++) {
			    				player.getHops().setFarmingHarvest(i, harvest.get(i).getAsInt());
			    			}
		                }
		                JsonArray state = hops.getAsJsonArray("state");
		                if(state != null && state.size() > 0){
			    			for (int i = 0; i < player.getHops().getFarmingState().length; i++) {
			    				player.getHops().setFarmingState(i, state.get(i).getAsInt());
			    			}
		                }
		                JsonArray timer = hops.getAsJsonArray("timer");
		                if(timer != null && timer.size() > 0){
			    			for (int i = 0; i < player.getHops().getFarmingTimer().length; i++) {
			    				player.getHops().setFarmingTimer(i, timer.get(i).getAsLong());
			    			}
		                }
		                JsonArray diseaseChance = hops.getAsJsonArray("diseaseChance");
		                if(diseaseChance != null && diseaseChance.size() > 0){
			    			for (int i = 0; i < player.getHops().getDiseaseChance().length; i++) {
			    				player.getHops().setDiseaseChance(i, diseaseChance.get(i).getAsDouble());
			    			}
		                }
		                JsonArray watched = hops.getAsJsonArray("watched");
		                if(watched != null && watched.size() > 0){
			    			for (int i = 0; i < player.getHops().getFarmingWatched().length; i++) {
			    				player.getHops().setFarmingWatched(i, watched.get(i).getAsBoolean());
			    			}
		                }
		            }
		            JsonObject specialPlantOne = farming.getAsJsonObject("specialPlantOne");
		            if(specialPlantOne != null){
		                JsonArray stages = specialPlantOne.getAsJsonArray("stages");
		                if(stages != null && stages.size() > 0){
			    			for (int i = 0; i < player.getSpecialPlantOne().getFarmingStages().length; i++) {
			    				player.getSpecialPlantOne().setFarmingStages(i, stages.get(i).getAsInt());
			    			}
		                }
		                JsonArray seeds = specialPlantOne.getAsJsonArray("seeds");
		                if(seeds != null && seeds.size() > 0){
			    			for (int i = 0; i < player.getSpecialPlantOne().getFarmingSeeds().length; i++) {
			    				player.getSpecialPlantOne().setFarmingSeeds(i, seeds.get(i).getAsInt());
			    			}
		                }
		                JsonArray state = specialPlantOne.getAsJsonArray("state");
		                if(state != null && state.size() > 0){
			    			for (int i = 0; i < player.getSpecialPlantOne().getFarmingState().length; i++) {
			    				player.getSpecialPlantOne().setFarmingState(i, state.get(i).getAsInt());
			    			}
		                }
		                JsonArray timer = specialPlantOne.getAsJsonArray("timer");
		                if(timer != null && timer.size() > 0){
			    			for (int i = 0; i < player.getSpecialPlantOne().getFarmingTimer().length; i++) {
			    				player.getSpecialPlantOne().setFarmingTimer(i, timer.get(i).getAsLong());
			    			}
		                }
		                JsonArray diseaseChance = specialPlantOne.getAsJsonArray("diseaseChance");
		                if(diseaseChance != null && diseaseChance.size() > 0){
			    			for (int i = 0; i < player.getSpecialPlantOne().getDiseaseChance().length; i++) {
			    				player.getSpecialPlantOne().setDiseaseChance(i, diseaseChance.get(i).getAsDouble());
			    			}
		                }
		            }
		            JsonObject specialPlantTwo = farming.getAsJsonObject("specialPlantTwo");
		            if(specialPlantTwo != null){
		                JsonArray stages = specialPlantTwo.getAsJsonArray("stages");
		                if(stages != null && stages.size() > 0){
			    			for (int i = 0; i < player.getSpecialPlantTwo().getFarmingStages().length; i++) {
			    				player.getSpecialPlantTwo().setFarmingStages(i, stages.get(i).getAsInt());
			    			}
		                }
		                JsonArray seeds = specialPlantTwo.getAsJsonArray("seeds");
		                if(seeds != null && seeds.size() > 0){
			    			for (int i = 0; i < player.getSpecialPlantTwo().getFarmingSeeds().length; i++) {
			    				player.getSpecialPlantTwo().setFarmingSeeds(i, seeds.get(i).getAsInt());
			    			}
		                }
		                JsonArray state = specialPlantTwo.getAsJsonArray("state");
		                if(state != null && state.size() > 0){
			    			for (int i = 0; i < player.getSpecialPlantTwo().getFarmingState().length; i++) {
			    				player.getSpecialPlantTwo().setFarmingState(i, state.get(i).getAsInt());
			    			}
		                }
		                JsonArray timer = specialPlantTwo.getAsJsonArray("timer");
		                if(timer != null && timer.size() > 0){
			    			for (int i = 0; i < player.getSpecialPlantTwo().getFarmingTimer().length; i++) {
			    				player.getSpecialPlantTwo().setFarmingTimer(i, timer.get(i).getAsLong());
			    			}
		                }
		                JsonArray diseaseChance = specialPlantTwo.getAsJsonArray("diseaseChance");
		                if(diseaseChance != null && diseaseChance.size() > 0){
			    			for (int i = 0; i < player.getSpecialPlantTwo().getDiseaseChance().length; i++) {
			    				player.getSpecialPlantTwo().setDiseaseChance(i, diseaseChance.get(i).getAsDouble());
			    			}
		                }
		            }
		            JsonObject trees = farming.getAsJsonObject("trees");
		            if(trees != null){
		                JsonArray stages = trees.getAsJsonArray("stages");
		                if(stages != null && stages.size() > 0){
			    			for (int i = 0; i < player.getTrees().getFarmingStages().length; i++) {
			    				player.getTrees().setFarmingStages(i, stages.get(i).getAsInt());
			    			}
		                }
		                JsonArray seeds = trees.getAsJsonArray("seeds");
		                if(seeds != null && seeds.size() > 0){
			    			for (int i = 0; i < player.getTrees().getFarmingSeeds().length; i++) {
			    				player.getTrees().setFarmingSeeds(i, seeds.get(i).getAsInt());
			    			}
		                }
		                JsonArray harvest = trees.getAsJsonArray("harvest");
		                if(harvest != null && harvest.size() > 0){
			    			for (int i = 0; i < player.getTrees().getFarmingHarvest().length; i++) {
			    				player.getTrees().setFarmingHarvest(i, harvest.get(i).getAsInt());
			    			}
		                }
		                JsonArray state = trees.getAsJsonArray("state");
		                if(state != null && state.size() > 0){
			    			for (int i = 0; i < player.getTrees().getFarmingState().length; i++) {
			    				player.getTrees().setFarmingState(i, state.get(i).getAsInt());
			    			}
		                }
		                JsonArray timer = trees.getAsJsonArray("timer");
		                if(timer != null && timer.size() > 0){
			    			for (int i = 0; i < player.getTrees().getFarmingTimer().length; i++) {
			    				player.getTrees().setFarmingTimer(i, timer.get(i).getAsLong());
			    			}
		                }
		                JsonArray diseaseChance = trees.getAsJsonArray("diseaseChance");
		                if(diseaseChance != null && diseaseChance.size() > 0){
			    			for (int i = 0; i < player.getTrees().getDiseaseChance().length; i++) {
			    				player.getTrees().setDiseaseChance(i, diseaseChance.get(i).getAsDouble());
			    			}
		                }
		                JsonArray watched = trees.getAsJsonArray("watched");
		                if(watched != null && watched.size() > 0){
			    			for (int i = 0; i < player.getTrees().getFarmingWatched().length; i++) {
			    				player.getTrees().setFarmingWatched(i, watched.get(i).getAsBoolean());
			    			}
		                }
		            }
		            JsonObject compost = farming.getAsJsonObject("compost");
		            if(compost != null){
		                JsonArray bins = compost.getAsJsonArray("bins");
		                if(bins != null && bins.size() > 0){
		        			for (int i = 0; i < player.getCompost().getCompostBins().length; i++) {
		        				player.getCompost().setCompostBins(i, bins.get(i).getAsInt());
		        			}	
		                }
		                JsonArray timer = trees.getAsJsonArray("timer");
		                if(timer != null && timer.size() > 0){
		        			for (int i = 0; i < player.getCompost().getCompostBinsTimer().length; i++) {
		        				player.getCompost().setCompostBinsTimer(i, timer.get(i).getAsLong());
		        			}
		                }
		                JsonArray organicItems = trees.getAsJsonArray("organicItems");
		                if(organicItems != null && organicItems.size() > 0){
		        			for (int i = 0; i < player.getCompost().getOrganicItemAdded().length; i++) {
		        				player.getCompost().setOrganicItemAdded(i, organicItems.get(i).getAsInt());
		        			}
		                }
		            }
		            JsonArray farmingTools = farming.getAsJsonArray("tools");
		            if(farmingTools != null && farmingTools.size() > 0){
		    			for (int i = 0; i < player.getFarmingTools().getTools().length; i++) {
		    				player.getFarmingTools().setTools(i,  farmingTools.get(i).getAsInt());
		    			}
		            }
	            }
	        }
            if (Server.getSingleton() != null)
                Server.getSingleton().queueLogin(player);
            return 0;
		} catch(IOException e) {
			//corrupted save file or missing file
            if (Server.getSingleton() != null)
                Server.getSingleton().queueLogin(player);
            return 0;
	    }
	}
}
