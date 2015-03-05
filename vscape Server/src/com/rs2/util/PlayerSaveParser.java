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
import com.rs2.model.content.skills.prayer.Ectofuntus;
import com.rs2.model.content.treasuretrails.ClueScroll;
import com.rs2.model.players.Player.BankOptions;
import com.rs2.model.players.bank.BankManager;
import com.rs2.model.players.clanchat.ClanChatHandler;
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
	            if(characterObj.get("lastseen") != null)
	            {
	            	player.setTimeLoggedOut(characterObj.get("lastseen").getAsString());
	            }
                player.setMuteExpire(characterObj.get("muteExpire").getAsLong());
                player.setBanExpire(characterObj.get("banExpire").getAsLong());
                if(characterObj.get("inJail") != null)
                {
                	player.setInJail(characterObj.get("inJail").getAsBoolean());
                }
				if(characterObj.get("isIronman") != null) {
					player.setIronman(characterObj.get("isIronman").getAsBoolean());
				}
				if(characterObj.get("clanChat") != null) {
					player.setClanChat(ClanChatHandler.getClanChat(characterObj.get("clanChat").getAsLong()));
				}
	            JsonObject position = characterObj.getAsJsonObject("position");
	            if(position != null){
	            player.getPosition().setX(position.get("x") != null ? position.get("x").getAsInt() : Constants.START_X);
	            player.getPosition().setLastX(player.getPosition().getX());
	            player.getPosition().setY(position.get("y") != null ? position.get("y").getAsInt() : Constants.START_Y);
	            player.getPosition().setLastY(player.getPosition().getY() + 1);
	            player.getPosition().setZ(position.get("z") != null ? position.get("z").getAsInt() : Constants.START_Z);
	            }
	            JsonObject positionLast = characterObj.getAsJsonObject("lastPosition");
	            if(positionLast != null){
	            player.getPosition().setLastX(positionLast.get("x") != null ? positionLast.get("x").getAsInt() : Constants.START_X);
	            player.getPosition().setLastY(positionLast.get("y") != null ? positionLast.get("y").getAsInt() : Constants.START_Y);
	            player.getPosition().setLastZ(positionLast.get("z") != null ? positionLast.get("z").getAsInt() : Constants.START_Z);
	            }
	            JsonObject appearance = characterObj.getAsJsonObject("appearance");
	            if(appearance != null) {
		            player.setGender(appearance.get("gender").getAsInt());
		            JsonArray appearanceData = appearance.getAsJsonArray("appearanceData");
		            if(appearanceData != null && appearanceData.size() > 0){
			            for (int i = 0; i < player.getAppearance().length; i++) {
							if(i >= appearanceData.size())
								break;
			            	if(appearanceData.get(i) != null)
			            		player.getAppearance()[i] = appearanceData.get(i).getAsInt();
			            }
		            }
		            JsonArray colorData = appearance.getAsJsonArray("colorData");
		            if(colorData != null && colorData.size() > 0){
			            for (int i = 0; i < player.getColors().length; i++) {
							if(i >= colorData.size())
								break;
			            	if(colorData.get(i) != null)
			            		player.getColors()[i] = colorData.get(i).getAsInt();
			            }
		            }
	            }
	            JsonObject options = characterObj.getAsJsonObject("options");
	            if(options != null) {
		            boolean yellColor = options.get("yellColor").getAsBoolean();
		            player.setHideColors(yellColor, false);
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
							if(i >= degradeableHits.size())
								break;
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
		        	if(itemData.get("lostgodbook") != null){
		        		player.setLostGodBook(itemData.get("lostgodbook").getAsInt());
		        	}
	                player.getQuestVars().setHasUsedFreeGauntletsCharge(itemData.get("usedFreeGauntletsCharge").getAsBoolean());
	                player.setDefender(itemData.get("defender").getAsInt());
	                player.setDfsCharges(itemData.get("dfsCharges").getAsInt());
	                JsonArray pouchData = itemData.getAsJsonArray("pouchData");
	                if(pouchData != null && pouchData.size() > 0){
		                for (int i = 0; i < 4; i++) {
							if(i >= pouchData.size())
								break;
		                    player.setPouchData(i, pouchData.get(i).getAsInt());
		                }
	                }
					JsonArray puzzleStoredItems = itemData.getAsJsonArray("puzzleStoredItems");
					if(puzzleStoredItems != null && puzzleStoredItems.size() > 0) {
					    for (int i = 0; i < ClueScroll.PUZZLE_LENGTH; i++) {
							if(i >= puzzleStoredItems.size())
								break;
					    	player.getPuzzle().puzzleStoredItems[i] = new Item(puzzleStoredItems.get(i).getAsInt());
					    }
					}
					if(itemData.get("recieveMasks") != null)
						player.setReceivedMasks(itemData.get("recieveMasks").getAsBoolean());
					if(itemData.get("hasZombieHead") != null)
						player.setHasZombieHead(itemData.get("hasZombieHead").getAsBoolean());
    			}
                JsonObject worldData = characterObj.getAsJsonObject("worldData");
                if(worldData != null){
	                player.setCoalTruckAmount(worldData.get("coaltrucks").getAsInt());
			if(worldData.get("ectoGrinderBoneType") != null) {
			    player.getEctofuntus().boneType = Ectofuntus.BonemealData.forBoneId(worldData.get("ectoGrinderBoneType").getAsInt());
			}
			if(worldData.get("bonesInLoader") != null) {
			    int amount = worldData.get("bonesInLoader").getAsInt();
			    for(int i = 0; i < amount; i++) {
				player.getEctofuntus().getBonesInLoader().add(i, Ectofuntus.BonemealData.forBoneId(player.getEctofuntus().boneType.boneId));
			    }
			}
			if(worldData.get("bonemealInBin") != null) {
			    int amount = worldData.get("bonemealInBin").getAsInt();
			    for(int i = 0; i < amount; i++) {
				player.getEctofuntus().getBonemealInBin().add(i, Ectofuntus.BonemealData.forBoneId(player.getEctofuntus().boneType.boneId));
			    }
			}
	                player.setBrimhavenDungeonOpen(worldData.get("brimhavenOpen").getAsBoolean());
                }
                JsonObject npcData = characterObj.getAsJsonObject("npcData");
                if(npcData != null){
	                player.setRunecraftNpc(npcData.get("runecraftNpc").getAsInt());
	                player.setKilledClueAttacker(npcData.get("killedClueAttacker").getAsBoolean());
	                player.setKilledTreeSpirit(npcData.get("killedTreeSpirit").getAsBoolean());
	                player.setKilledJungleDemon(npcData.get("killedJungleDemon").getAsBoolean());
	                JsonObject catData = npcData.getAsJsonObject("catData");
	                if(catData != null){
	                	player.getCat().setCatItem(catData.get("catItemId").getAsInt());
	                	player.getCat().setGrowthStage(catData.get("growthStage").getAsInt());
	                	player.getCat().setGrowthTime(catData.get("growthTime").getAsInt());
	                	player.getCat().setHungerTime(catData.get("hungerTime").getAsInt());
	                	player.getCat().setAttentionTime(catData.get("attentionTime").getAsInt());
	                	player.getCat().setRatsCaught(catData.get("ratsCaught").getAsInt());
	                }
                }
                JsonObject slayerData = characterObj.getAsJsonObject("slayerData");
                if(slayerData != null){
	                player.getSlayer().slayerMaster = slayerData.get("slayerMaster").getAsInt();
	            	player.getSlayer().slayerTask = slayerData.get("slayerTask").getAsString();
	            	player.getSlayer().taskAmount = slayerData.get("taskAmount").getAsInt();
                }else{
                	player.getSlayer().resetSlayerTask();
                }
                
                JsonObject dehydrationData = characterObj.getAsJsonObject("dehydrationData");
                if(dehydrationData != null){
	                player.getDesertHeat().setDehydrateTime(dehydrationData.get("curDehydrateTime").getAsInt());
	                player.getDesertHeat().setDehydrateDmgTime(dehydrationData.get("curDehydrateDmgTime").getAsInt());
	                player.getDesertHeat().setDehydrated(dehydrationData.get("dehydrated").getAsBoolean());
                }
            	
                if(mainObj.get("friendsConverted") != null)
                {
                	player.setFriendsConverted(mainObj.get("friendsConverted").getAsBoolean());
                }
            	JsonArray friends = mainObj.getAsJsonArray("friends");
            	if(friends != null && friends.size() > 0){
		            for (int i = 0; i < player.getFriends().length; i++) {
						if(i >= friends.size())
							break;
						long friendLong = friends.get(i).getAsLong();
						if(friendLong < 0L || friendLong >= 0x7dcff8986ea31000L)
							continue;
						if(!player.getFriendsConverted()){
							friendLong = NameUtil.nameToLong(NameUtil.longToNameOld(friends.get(i).getAsLong()));
						}
		                player.getFriends()[i] = friendLong;
		            }
            	}
            	if(!player.getFriendsConverted()){
            		player.setFriendsConverted(true);
            	}
                if(mainObj.get("ignoresConverted") != null)
                {
                	player.setIgnoresConverted(mainObj.get("ignoresConverted").getAsBoolean());
                }
            	JsonArray ignores = mainObj.getAsJsonArray("ignores");
            	if(ignores != null && ignores.size() > 0){
		            for (int i = 0; i < player.getIgnores().length; i++) {
						if(i >= ignores.size())
							break;
						long ignoreLong = ignores.get(i).getAsLong();
						if(ignoreLong < 0L || ignoreLong >= 0x7dcff8986ea31000L)
							continue;
						if(!player.getIgnoresConverted()){
							ignoreLong = NameUtil.nameToLong(NameUtil.longToNameOld(ignores.get(i).getAsLong()));
						}
		                player.getIgnores()[i] = ignoreLong;
		            }
            	}
            	if(!player.getIgnoresConverted()){
            		player.setIgnoresConverted(true);
            	}
	            JsonArray skills = mainObj.getAsJsonArray("skills");
	            if(skills != null && skills.size() > 0){
		    		for (int i = 0; i < 22; i++) {
						if(i >= skills.size())
							break;
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
						if(i >= inventory.size())
							break;
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
						if(i >= equipment.size())
							break;
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
							if(i >= bankPin.size())
								break;
			                player.getBankPin().getBankPin()[i] = bankPin.get(i).getAsInt();
			            }
		            }
		            JsonArray bankPinPending = bank.getAsJsonArray("pinPending");
		            if(bankPinPending != null && bankPinPending.size() > 0){
		            	for (int i = 0; i < player.getBankPin().getPendingBankPin().length; i++) {
							if(i >= bankPinPending.size())
								break;
			                player.getBankPin().getPendingBankPin()[i] = bankPinPending.get(i).getAsInt();
			            }
		            }
		            player.setResetBank(bank.get("hasReset").getAsBoolean());
		            if(bank.get("swapMode") != null) {
		            	player.setBankOptions(bank.get("swapMode").getAsBoolean() ? BankOptions.SWAP_ITEM : BankOptions.INSERT_ITEM);
		            }
		            if(bank.get("withdrawAsNote") != null) {
		            	player.setWithdrawAsNote(bank.get("withdrawAsNote").getAsBoolean());
		            }
		            if(bank.get("usedTabs") != null) {
		            	player.getBankManager().addTabs(bank.get("usedTabs").getAsInt());
		            }
		            for (int i = 0; i < player.getBankManager().getUsedTabs(); i++) {
		            	JsonArray bankItems;
		            	if(i == 0)
		            		bankItems = bank.getAsJsonArray("items");
		            	else
		            		bankItems = bank.getAsJsonArray("items"+i);
		            	
		            	if(bankItems != null && bankItems.size() > 0){
		            		for (int j = 0; j < bankItems.size(); j++) {
			                	if(j >= bankItems.size() || j > BankManager.SIZE)
									break;
				            	JsonObject itemObj = bankItems.get(j).getAsJsonObject();
				            	int id = itemObj.get("id").getAsInt();
				            	if (id != 65535) {
					            	int amount = itemObj.get("count").getAsInt();
					            	int timer = itemObj.get("timer").getAsInt();
			                        if (id < Constants.MAX_ITEMS && amount > 0)  {
			                            Item item = new Item(id, amount, timer);
			                            if (item.getId() == 2696 || item.getId() == 2699 || item.getId() == 3510) {
			                            	item = new Item(id - 1, amount, timer);
			                            }
			                            player.getBankManager().tabContainer(i).set(j, item);
			                        }
				            	}
		            		}
		            	}
		            }
	            }
	            JsonArray pendingItems = mainObj.getAsJsonArray("pendingItems");
	            if(pendingItems != null && pendingItems.size() > 0){
		            for (int i = 0; i < player.getPendingItems().length; i++) {
						if(i >= pendingItems.size())
							break;
		            	JsonObject itemObj = pendingItems.get(i).getAsJsonObject();
		                player.getPendingItems()[i] = itemObj.get("id").getAsInt();
		                player.getPendingItemsAmount()[i] = itemObj.get("count").getAsInt();
		            }
	            }
	            JsonObject quests = mainObj.getAsJsonObject("quests");
	            if(quests != null){
		            player.setQuestPoints(quests.get("questpoints").getAsInt());
		            JsonObject questVars = quests.getAsJsonObject("questVars");
		            if(questVars != null) {
			            player.getQuestVars().joinPhoenixGang(questVars.get("phoenixGang").getAsBoolean());
			            player.getQuestVars().joinBlackArmGang(questVars.get("blackArmGang").getAsBoolean());
			            player.getQuestVars().setMelzarsDoorUnlock(questVars.get("melzarsDoorUnlock").getAsBoolean());
				    player.getQuestVars().setBananaCrate(questVars.get("bananaCrate").getAsBoolean());
				    player.getQuestVars().setBananaCrateCount(questVars.get("bananaCrateCount").getAsInt());
				    player.setEctoWorshipCount(questVars.get("ectoWorshipCount").getAsInt());
				    player.getQuestVars().dyeGhostsAhoyFlag("top", questVars.get("topHalfFlag").getAsString());
				    player.getQuestVars().dyeGhostsAhoyFlag("bottom", questVars.get("bottomHalfFlag").getAsString());
				    player.getQuestVars().dyeGhostsAhoyFlag("skull", questVars.get("skullFlag").getAsString());
				    player.getQuestVars().setDesiredGhostsAhoyFlag("top", questVars.get("desiredTopHalfFlag").getAsString());
				    player.getQuestVars().setDesiredGhostsAhoyFlag("bottom", questVars.get("desiredBottomHalfFlag").getAsString());
				    player.getQuestVars().setDesiredGhostsAhoyFlag("top", questVars.get("desiredSkullFlag").getAsString());
				    player.getQuestVars().setPetitionSigned(questVars.get("petitionSigned").getAsBoolean());
				    player.getQuestVars().setGivenSnailSlime(questVars.get("snailSlime").getAsBoolean());
				    player.getQuestVars().setGivenIdPapers(questVars.get("idPapers").getAsBoolean());
		            	if(questVars.get("hasShotGrip") != null){
		            		player.getQuestVars().setShotGrip(questVars.get("hasShotGrip").getAsBoolean());
		            	}
				if(questVars.get("ballistaIndex") != null){
		            		player.getQuestVars().setBallistaIndex(questVars.get("ballistaIndex").getAsInt());
		            	}
				if(questVars.get("gazeOfSaradomin") != null) {
					player.getQuestVars().setGazeOfSaradomin(questVars.get("gazeOfSaradomin").getAsBoolean());
				}
				if(questVars.get("1stMortMyreBridge") != null) {
					player.getQuestVars().setMortMyreBridgeFixed(1, questVars.get("1stMortMyreBridge").getAsBoolean());
				}
				if(questVars.get("2ndMortMyreBridge") != null) {
					player.getQuestVars().setMortMyreBridgeFixed(2, questVars.get("2ndMortMyreBridge").getAsBoolean());
				}
				if(questVars.get("3rdMortMyreBridge") != null) {
					player.getQuestVars().setMortMyreBridgeFixed(3, questVars.get("3rdMortMyreBridge").getAsBoolean());
				}
				if(questVars.get("canTeleArdy") != null) {
					player.getQuestVars().setCanTeleportArdougne(questVars.get("canTeleArdy").getAsBoolean());
				}
		            }
			    JsonObject MMVars = quests.getAsJsonObject("monkeyMadnessVars");
				if(MMVars != null) {
				    if(MMVars.get("spokenMonkeyChild") != null) {
					player.getMMVars().setSpokenToMonkeyChild(MMVars.get("spokenMonkeyChild").getAsBoolean());
				    }
				    if(MMVars.get("monkeyChildBananas") != null) {
					player.getMMVars().setGivenMonkeyChildBananas(MMVars.get("monkeyChildBananas").getAsBoolean());
				    }
				    if(MMVars.get("monkeyChildToy") != null) {
					player.getMMVars().setMonkeyChildHasToy(MMVars.get("monkeyChildToy").getAsBoolean());
				    }
				    if(MMVars.get("openGate") != null) {
					player.getMMVars().setOpenGate(MMVars.get("openGate").getAsBoolean());
				    }
				    if(MMVars.get("canHideInGrass") != null) {
					player.getMMVars().setCanHideInGrass(MMVars.get("canHideInGrass").getAsBoolean());
				    }
				    if(MMVars.get("firstTimeJail") != null) {
					player.getMMVars().setFirstTimeJail(MMVars.get("firstTimeJail").getAsBoolean());
				    }
				    if(MMVars.get("gotAmulet") != null) {
					player.getMMVars().setGotAmulet(MMVars.get("gotAmulet").getAsBoolean());
				    }
				    if(MMVars.get("gotTalisman") != null) {
					player.getMMVars().setGotTalisman(MMVars.get("gotTalisman").getAsBoolean());
				    }
				    if(MMVars.get("monkeyPetDeleted") != null) {
					player.getMMVars().monkeyPetDeleted = MMVars.get("monkeyPetDeleted").getAsBoolean();
				    }
				    if(MMVars.get("trainingComplete") != null) {
					player.getMMVars().setTrainingComplete(MMVars.get("trainingComplete").getAsBoolean());
				    }
				    if(MMVars.get("receivedClue") != null) {
					player.getMMVars().setRecievedClueFromMonkey(MMVars.get("receivedClue").getAsBoolean());
				    }
				}
	            	JsonArray questData = quests.getAsJsonArray("questData");
	            	if(questData != null && questData.size() > 0){
	            		for (int i = 0; i < QuestHandler.getQuests().length; i++)
						{
							if(i >= questData.size())
								break;
							
							Quest q = QuestHandler.getQuests()[i];
							if(q == null) continue;
							
							JsonObject questDataObj = questData.get(i).getAsJsonObject();
							if(questDataObj == null)
								continue;
							
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
								if(i >= brothersKilled.size())
									break;
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
			    JsonObject mageTrainingArena = minigames.getAsJsonObject("mageTrainingArena");
		            if(mageTrainingArena != null){
				JsonObject enchantingChamber = mageTrainingArena.getAsJsonObject("enchantingChamber");
				if (enchantingChamber != null) {
				    if (enchantingChamber.get("pizazzPoints") != null) {
					player.setEnchantingPizazz(enchantingChamber.get("pizazzPoints").getAsInt());
				    }
				    if (enchantingChamber.get("enchantCount") != null) {
					player.setEnchantingEnchantCount(enchantingChamber.get("enchantCount").getAsInt());
				    }
				    if (enchantingChamber.get("orbCount") != null) {
					player.setEnchantingOrbCount(enchantingChamber.get("orbCount").getAsInt());
				    }
				}
				JsonObject alchemistPlayground = mageTrainingArena.getAsJsonObject("alchemistPlayground");
				if (alchemistPlayground != null) {
				    if(alchemistPlayground.get("pizazzPoints") != null) {
					player.setAlchemistPizazz(alchemistPlayground.get("pizazzPoints").getAsInt());
				    }
				}
				JsonObject creatureGraveyard = mageTrainingArena.getAsJsonObject("creatureGraveyard");
				if (creatureGraveyard != null) {
				    if (creatureGraveyard.get("pizazzPoints") != null) {
					player.setGraveyardPizazz(creatureGraveyard.get("pizazzPoints").getAsInt());
				    }
				    if (creatureGraveyard.get("graveyardFruitDeposited") != null) {
					player.setGraveyardFruitDeposited(creatureGraveyard.get("graveyardFruitDeposited").getAsInt());
				    }
				    if (creatureGraveyard.get("bonesToPeachesEnabled") != null) {
					player.setBonesToPeachesEnabled(creatureGraveyard.get("bonesToPeachesEnabled").getAsBoolean());
				    }
				}
				JsonObject telekineticTheatre = mageTrainingArena.getAsJsonObject("telekineticTheatre");
				if (telekineticTheatre != null) {
				    if(telekineticTheatre.get("pizazzPoints") != null) {
					player.setTelekineticPizazz(telekineticTheatre.get("pizazzPoints").getAsInt());
				    }
				    if(telekineticTheatre.get("mazesCompleted") != null) {
					player.setTelekineticMazesCompleted(telekineticTheatre.get("mazesCompleted").getAsInt());
				    }
				}
		            }
	            }
	            JsonObject farming = mainObj.getAsJsonObject("farming");
		        if(farming != null){
		            JsonObject allotments = farming.getAsJsonObject("allotments");
		            if(allotments != null){
		                JsonArray stages = allotments.getAsJsonArray("stages");
		                if(stages != null && stages.size() > 0){
			    			for (int i = 0; i < player.getAllotment().getFarmingStages().length; i++) {
								if(i >= stages.size())
									break;
			    				player.getAllotment().setFarmingStages(i, stages.get(i).getAsInt());
			    			}
		                }
		                JsonArray seeds = allotments.getAsJsonArray("seeds");
		                if(seeds != null && seeds.size() > 0){
			    			for (int i = 0; i < player.getAllotment().getFarmingSeeds().length; i++) {
								if(i >= seeds.size())
									break;
			    				player.getAllotment().setFarmingSeeds(i, seeds.get(i).getAsInt());
			    			}
		                }
		                JsonArray harvest = allotments.getAsJsonArray("harvest");
		                if(harvest != null && harvest.size() > 0){
			    			for (int i = 0; i < player.getAllotment().getFarmingHarvest().length; i++) {
								if(i >= harvest.size())
									break;
			    				player.getAllotment().setFarmingHarvest(i, harvest.get(i).getAsInt());
			    			}
		                }
		                JsonArray state = allotments.getAsJsonArray("state");
		                if(state != null && state.size() > 0){
			    			for (int i = 0; i < player.getAllotment().getFarmingState().length; i++) {
								if(i >= state.size())
									break;
			    				player.getAllotment().setFarmingState(i, state.get(i).getAsInt());
			    			}
		                }
		                JsonArray timer = allotments.getAsJsonArray("timer");
		                if(timer != null && timer.size() > 0){
			    			for (int i = 0; i < player.getAllotment().getFarmingTimer().length; i++) {
								if(i >= timer.size())
									break;
			    				player.getAllotment().setFarmingTimer(i, timer.get(i).getAsLong());
			    			}
		                }
		                JsonArray diseaseChance = allotments.getAsJsonArray("diseaseChance");
		                if(diseaseChance != null && diseaseChance.size() > 0){
			    			for (int i = 0; i < player.getAllotment().getDiseaseChance().length; i++) {
								if(i >= diseaseChance.size())
									break;
			    				player.getAllotment().setDiseaseChance(i, diseaseChance.get(i).getAsDouble());
			    			}
		                }
		                JsonArray watched = allotments.getAsJsonArray("watched");
		                if(watched != null && watched.size() > 0){
			    			for (int i = 0; i < player.getAllotment().getFarmingWatched().length; i++) {
								if(i >= watched.size())
									break;
			    				player.getAllotment().setFarmingWatched(i, watched.get(i).getAsBoolean());
			    			}
		                }
		            }
		            JsonObject bushes = farming.getAsJsonObject("bushes");
		            if(bushes != null){
		                JsonArray stages = bushes.getAsJsonArray("stages");
		                if(stages != null && stages.size() > 0){
			    			for (int i = 0; i < player.getBushes().getFarmingStages().length; i++) {
								if(i >= stages.size())
									break;
			    				player.getBushes().setFarmingStages(i, stages.get(i).getAsInt());
			    			}
		                }
		                JsonArray seeds = bushes.getAsJsonArray("seeds");
		                if(seeds != null && seeds.size() > 0){
			    			for (int i = 0; i < player.getBushes().getFarmingSeeds().length; i++) {
								if(i >= seeds.size())
									break;
			    				player.getBushes().setFarmingSeeds(i, seeds.get(i).getAsInt());
			    			}
		                }
		                JsonArray state = bushes.getAsJsonArray("state");
		                if(state != null && state.size() > 0){
			    			for (int i = 0; i < player.getBushes().getFarmingState().length; i++) {
								if(i >= state.size())
									break;
			    				player.getBushes().setFarmingState(i, state.get(i).getAsInt());
			    			}
		                }
		                JsonArray timer = bushes.getAsJsonArray("timer");
		                if(timer != null && timer.size() > 0){
			    			for (int i = 0; i < player.getBushes().getFarmingTimer().length; i++) {
								if(i >= timer.size())
									break;
			    				player.getBushes().setFarmingTimer(i, timer.get(i).getAsLong());
			    			}
		                }
		                JsonArray diseaseChance = bushes.getAsJsonArray("diseaseChance");
		                if(diseaseChance != null && diseaseChance.size() > 0){
			    			for (int i = 0; i < player.getBushes().getDiseaseChance().length; i++) {
								if(i >= diseaseChance.size())
									break;
			    				player.getBushes().setDiseaseChance(i, diseaseChance.get(i).getAsDouble());
			    			}
		                }
		                JsonArray watched = bushes.getAsJsonArray("watched");
		                if(watched != null && watched.size() > 0){
			    			for (int i = 0; i < player.getBushes().getFarmingWatched().length; i++) {
								if(i >= watched.size())
									break;
			    				player.getBushes().setFarmingWatched(i, watched.get(i).getAsBoolean());
			    			}
		                }
		            }
		            JsonObject flowers = farming.getAsJsonObject("flowers");
		            if(flowers != null){
		                JsonArray stages = flowers.getAsJsonArray("stages");
		                if(stages != null && stages.size() > 0){
			    			for (int i = 0; i < player.getFlowers().getFarmingStages().length; i++) {
								if(i >= stages.size())
									break;
			    				player.getFlowers().setFarmingStages(i, stages.get(i).getAsInt());
			    			}
		                }
		                JsonArray seeds = flowers.getAsJsonArray("seeds");
		                if(seeds != null && seeds.size() > 0){
			    			for (int i = 0; i < player.getFlowers().getFarmingSeeds().length; i++) {
								if(i >= seeds.size())
									break;
			    				player.getFlowers().setFarmingSeeds(i, seeds.get(i).getAsInt());
			    			}
		                }
		                JsonArray state = flowers.getAsJsonArray("state");
		                if(state != null && state.size() > 0){
			    			for (int i = 0; i < player.getFlowers().getFarmingState().length; i++) {
								if(i >= state.size())
									break;
			    				player.getFlowers().setFarmingState(i, state.get(i).getAsInt());
			    			}
		                }
		                JsonArray timer = flowers.getAsJsonArray("timer");
		                if(timer != null && timer.size() > 0){
			    			for (int i = 0; i < player.getFlowers().getFarmingTimer().length; i++) {
								if(i >= timer.size())
									break;
			    				player.getFlowers().setFarmingTimer(i, timer.get(i).getAsLong());
			    			}
		                }
		                JsonArray diseaseChance = flowers.getAsJsonArray("diseaseChance");
		                if(diseaseChance != null && diseaseChance.size() > 0){
			    			for (int i = 0; i < player.getFlowers().getDiseaseChance().length; i++) {
								if(i >= diseaseChance.size())
									break;
			    				player.getFlowers().setDiseaseChance(i, diseaseChance.get(i).getAsDouble());
			    			}
		                }
		            }
		            JsonObject fruitTrees = farming.getAsJsonObject("fruitTrees");
		            if(fruitTrees != null){
		                JsonArray stages = fruitTrees.getAsJsonArray("stages");
		                if(stages != null && stages.size() > 0){
			    			for (int i = 0; i < player.getFruitTrees().getFarmingStages().length; i++) {
								if(i >= stages.size())
									break;
			    				player.getFruitTrees().setFarmingStages(i, stages.get(i).getAsInt());
			    			}
		                }
		                JsonArray seeds = fruitTrees.getAsJsonArray("seeds");
		                if(seeds != null && seeds.size() > 0){
			    			for (int i = 0; i < player.getFruitTrees().getFarmingSeeds().length; i++) {
								if(i >= seeds.size())
									break;
			    				player.getFruitTrees().setFarmingSeeds(i, seeds.get(i).getAsInt());
			    			}
		                }
		                JsonArray state = fruitTrees.getAsJsonArray("state");
		                if(state != null && state.size() > 0){
			    			for (int i = 0; i < player.getFruitTrees().getFarmingState().length; i++) {
								if(i >= state.size())
									break;
			    				player.getFruitTrees().setFarmingState(i, state.get(i).getAsInt());
			    			}
		                }
		                JsonArray timer = fruitTrees.getAsJsonArray("timer");
		                if(timer != null && timer.size() > 0){
			    			for (int i = 0; i < player.getFruitTrees().getFarmingTimer().length; i++) {
								if(i >= timer.size())
									break;
			    				player.getFruitTrees().setFarmingTimer(i, timer.get(i).getAsLong());
			    			}
		                }
		                JsonArray diseaseChance = fruitTrees.getAsJsonArray("diseaseChance");
		                if(diseaseChance != null && diseaseChance.size() > 0){
			    			for (int i = 0; i < player.getFruitTrees().getDiseaseChance().length; i++) {
								if(i >= diseaseChance.size())
									break;
			    				player.getFruitTrees().setDiseaseChance(i, diseaseChance.get(i).getAsDouble());
			    			}
		                }
		                JsonArray watched = fruitTrees.getAsJsonArray("watched");
		                if(watched != null && watched.size() > 0){
			    			for (int i = 0; i < player.getFruitTrees().getFarmingWatched().length; i++) {
								if(i >= watched.size())
									break;
			    				player.getFruitTrees().setFarmingWatched(i, watched.get(i).getAsBoolean());
			    			}
		                }
		            }
		            JsonObject herbs = farming.getAsJsonObject("herbs");
		            if(herbs != null){
		                JsonArray stages = herbs.getAsJsonArray("stages");
		                if(stages != null && stages.size() > 0){
			    			for (int i = 0; i < player.getHerbs().getFarmingStages().length; i++) {
								if(i >= stages.size())
									break;
			    				player.getHerbs().setFarmingStages(i, stages.get(i).getAsInt());
			    			}
		                }
		                JsonArray seeds = herbs.getAsJsonArray("seeds");
		                if(seeds != null && seeds.size() > 0){
			    			for (int i = 0; i < player.getHerbs().getFarmingSeeds().length; i++) {
								if(i >= seeds.size())
									break;
			    				player.getHerbs().setFarmingSeeds(i, seeds.get(i).getAsInt());
			    			}
		                }
		                JsonArray harvest = herbs.getAsJsonArray("harvest");
		                if(harvest != null && harvest.size() > 0){
			    			for (int i = 0; i < player.getHerbs().getFarmingHarvest().length; i++) {
								if(i >= harvest.size())
									break;
			    				player.getHerbs().setFarmingHarvest(i, harvest.get(i).getAsInt());
			    			}
		                }
		                JsonArray state = herbs.getAsJsonArray("state");
		                if(state != null && state.size() > 0){
			    			for (int i = 0; i < player.getHerbs().getFarmingState().length; i++) {
								if(i >= state.size())
									break;
			    				player.getHerbs().setFarmingState(i, state.get(i).getAsInt());
			    			}
		                }
		                JsonArray timer = herbs.getAsJsonArray("timer");
		                if(timer != null && timer.size() > 0){
			    			for (int i = 0; i < player.getHerbs().getFarmingTimer().length; i++) {
								if(i >= timer.size())
									break;
			    				player.getHerbs().setFarmingTimer(i, timer.get(i).getAsLong());
			    			}
		                }
		                JsonArray diseaseChance = herbs.getAsJsonArray("diseaseChance");
		                if(diseaseChance != null && diseaseChance.size() > 0){
			    			for (int i = 0; i < player.getHerbs().getDiseaseChance().length; i++) {
								if(i >= diseaseChance.size())
									break;
			    				player.getHerbs().setDiseaseChance(i, diseaseChance.get(i).getAsDouble());
			    			}
		                }
		            }
		            JsonObject hops = farming.getAsJsonObject("hops");
		            if(hops != null){
		                JsonArray stages = hops.getAsJsonArray("stages");
		                if(stages != null && stages.size() > 0){
			    			for (int i = 0; i < player.getHops().getFarmingStages().length; i++) {
								if(i >= stages.size())
									break;
			    				player.getHops().setFarmingStages(i, stages.get(i).getAsInt());
			    			}
		                }
		                JsonArray seeds = hops.getAsJsonArray("seeds");
		                if(seeds != null && seeds.size() > 0){
			    			for (int i = 0; i < player.getHops().getFarmingSeeds().length; i++) {
								if(i >= seeds.size())
									break;
			    				player.getHops().setFarmingSeeds(i, seeds.get(i).getAsInt());
			    			}
		                }
		                JsonArray harvest = hops.getAsJsonArray("harvest");
		                if(harvest != null && harvest.size() > 0){
			    			for (int i = 0; i < player.getHops().getFarmingHarvest().length; i++) {
								if(i >= harvest.size())
									break;
			    				player.getHops().setFarmingHarvest(i, harvest.get(i).getAsInt());
			    			}
		                }
		                JsonArray state = hops.getAsJsonArray("state");
		                if(state != null && state.size() > 0){
			    			for (int i = 0; i < player.getHops().getFarmingState().length; i++) {
								if(i >= state.size())
									break;
			    				player.getHops().setFarmingState(i, state.get(i).getAsInt());
			    			}
		                }
		                JsonArray timer = hops.getAsJsonArray("timer");
		                if(timer != null && timer.size() > 0){
			    			for (int i = 0; i < player.getHops().getFarmingTimer().length; i++) {
								if(i >= timer.size())
									break;
			    				player.getHops().setFarmingTimer(i, timer.get(i).getAsLong());
			    			}
		                }
		                JsonArray diseaseChance = hops.getAsJsonArray("diseaseChance");
		                if(diseaseChance != null && diseaseChance.size() > 0){
			    			for (int i = 0; i < player.getHops().getDiseaseChance().length; i++) {
								if(i >= diseaseChance.size())
									break;
			    				player.getHops().setDiseaseChance(i, diseaseChance.get(i).getAsDouble());
			    			}
		                }
		                JsonArray watched = hops.getAsJsonArray("watched");
		                if(watched != null && watched.size() > 0){
			    			for (int i = 0; i < player.getHops().getFarmingWatched().length; i++) {
								if(i >= watched.size())
									break;
			    				player.getHops().setFarmingWatched(i, watched.get(i).getAsBoolean());
			    			}
		                }
		            }
		            JsonObject specialPlantOne = farming.getAsJsonObject("specialPlantOne");
		            if(specialPlantOne != null){
		                JsonArray stages = specialPlantOne.getAsJsonArray("stages");
		                if(stages != null && stages.size() > 0){
			    			for (int i = 0; i < player.getSpecialPlantOne().getFarmingStages().length; i++) {
								if(i >= stages.size())
									break;
			    				player.getSpecialPlantOne().setFarmingStages(i, stages.get(i).getAsInt());
			    			}
		                }
		                JsonArray seeds = specialPlantOne.getAsJsonArray("seeds");
		                if(seeds != null && seeds.size() > 0){
			    			for (int i = 0; i < player.getSpecialPlantOne().getFarmingSeeds().length; i++) {
								if(i >= seeds.size())
									break;
			    				player.getSpecialPlantOne().setFarmingSeeds(i, seeds.get(i).getAsInt());
			    			}
		                }
		                JsonArray state = specialPlantOne.getAsJsonArray("state");
		                if(state != null && state.size() > 0){
			    			for (int i = 0; i < player.getSpecialPlantOne().getFarmingState().length; i++) {
								if(i >= state.size())
									break;
			    				player.getSpecialPlantOne().setFarmingState(i, state.get(i).getAsInt());
			    			}
		                }
		                JsonArray timer = specialPlantOne.getAsJsonArray("timer");
		                if(timer != null && timer.size() > 0){
			    			for (int i = 0; i < player.getSpecialPlantOne().getFarmingTimer().length; i++) {
								if(i >= timer.size())
									break;
			    				player.getSpecialPlantOne().setFarmingTimer(i, timer.get(i).getAsLong());
			    			}
		                }
		                JsonArray diseaseChance = specialPlantOne.getAsJsonArray("diseaseChance");
		                if(diseaseChance != null && diseaseChance.size() > 0){
			    			for (int i = 0; i < player.getSpecialPlantOne().getDiseaseChance().length; i++) {
			    				if(i >= diseaseChance.size())
									break;
			    				player.getSpecialPlantOne().setDiseaseChance(i, diseaseChance.get(i).getAsDouble());
			    			}
		                }
		            }
		            JsonObject specialPlantTwo = farming.getAsJsonObject("specialPlantTwo");
		            if(specialPlantTwo != null){
		                JsonArray stages = specialPlantTwo.getAsJsonArray("stages");
		                if(stages != null && stages.size() > 0){
			    			for (int i = 0; i < player.getSpecialPlantTwo().getFarmingStages().length; i++) {
			    				if(i >= stages.size())
									break;
			    				player.getSpecialPlantTwo().setFarmingStages(i, stages.get(i).getAsInt());
			    			}
		                }
		                JsonArray seeds = specialPlantTwo.getAsJsonArray("seeds");
		                if(seeds != null && seeds.size() > 0){
			    			for (int i = 0; i < player.getSpecialPlantTwo().getFarmingSeeds().length; i++) {
			    				if(i >= seeds.size())
									break;
			    				player.getSpecialPlantTwo().setFarmingSeeds(i, seeds.get(i).getAsInt());
			    			}
		                }
		                JsonArray state = specialPlantTwo.getAsJsonArray("state");
		                if(state != null && state.size() > 0){
			    			for (int i = 0; i < player.getSpecialPlantTwo().getFarmingState().length; i++) {
			    				if(i >= state.size())
									break;
			    				player.getSpecialPlantTwo().setFarmingState(i, state.get(i).getAsInt());
			    			}
		                }
		                JsonArray timer = specialPlantTwo.getAsJsonArray("timer");
		                if(timer != null && timer.size() > 0){
			    			for (int i = 0; i < player.getSpecialPlantTwo().getFarmingTimer().length; i++) {
			    				if(i >= timer.size())
									break;
			    				player.getSpecialPlantTwo().setFarmingTimer(i, timer.get(i).getAsLong());
			    			}
		                }
		                JsonArray diseaseChance = specialPlantTwo.getAsJsonArray("diseaseChance");
		                if(diseaseChance != null && diseaseChance.size() > 0){
			    			for (int i = 0; i < player.getSpecialPlantTwo().getDiseaseChance().length; i++) {
			    				if(i >= diseaseChance.size())
									break;
			    				player.getSpecialPlantTwo().setDiseaseChance(i, diseaseChance.get(i).getAsDouble());
			    			}
		                }
		            }
		            JsonObject trees = farming.getAsJsonObject("trees");
		            if(trees != null){
		                JsonArray stages = trees.getAsJsonArray("stages");
		                if(stages != null && stages.size() > 0){
			    			for (int i = 0; i < player.getTrees().getFarmingStages().length; i++) {
			    				if(i >= stages.size())
									break;
			    				player.getTrees().setFarmingStages(i, stages.get(i).getAsInt());
			    			}
		                }
		                JsonArray seeds = trees.getAsJsonArray("seeds");
		                if(seeds != null && seeds.size() > 0){
			    			for (int i = 0; i < player.getTrees().getFarmingSeeds().length; i++) {
			    				if(i >= seeds.size())
									break;
			    				player.getTrees().setFarmingSeeds(i, seeds.get(i).getAsInt());
			    			}
		                }
		                JsonArray harvest = trees.getAsJsonArray("harvest");
		                if(harvest != null && harvest.size() > 0){
			    			for (int i = 0; i < player.getTrees().getFarmingHarvest().length; i++) {
			    				if(i >= harvest.size())
									break;
			    				player.getTrees().setFarmingHarvest(i, harvest.get(i).getAsInt());
			    			}
		                }
		                JsonArray state = trees.getAsJsonArray("state");
		                if(state != null && state.size() > 0){
			    			for (int i = 0; i < player.getTrees().getFarmingState().length; i++) {
			    				if(i >= state.size())
									break;
			    				player.getTrees().setFarmingState(i, state.get(i).getAsInt());
			    			}
		                }
		                JsonArray timer = trees.getAsJsonArray("timer");
		                if(timer != null && timer.size() > 0){
			    			for (int i = 0; i < player.getTrees().getFarmingTimer().length; i++) {
			    				if(i >= timer.size())
									break;
			    				player.getTrees().setFarmingTimer(i, timer.get(i).getAsLong());
			    			}
		                }
		                JsonArray diseaseChance = trees.getAsJsonArray("diseaseChance");
		                if(diseaseChance != null && diseaseChance.size() > 0){
			    			for (int i = 0; i < player.getTrees().getDiseaseChance().length; i++) {
			    				if(i >= diseaseChance.size())
									break;
			    				player.getTrees().setDiseaseChance(i, diseaseChance.get(i).getAsDouble());
			    			}
		                }
		                JsonArray watched = trees.getAsJsonArray("watched");
		                if(watched != null && watched.size() > 0){
			    			for (int i = 0; i < player.getTrees().getFarmingWatched().length; i++) {
			    				if(i >= watched.size())
									break;
			    				player.getTrees().setFarmingWatched(i, watched.get(i).getAsBoolean());
			    			}
		                }
		            }
		            JsonObject compost = farming.getAsJsonObject("compost");
		            if(compost != null){
		                JsonArray bins = compost.getAsJsonArray("bins");
		                if(bins != null && bins.size() > 0){
		        			for (int i = 0; i < player.getCompost().getCompostBins().length; i++) {
		        				if(i >= bins.size())
									break;
		        				player.getCompost().setCompostBins(i, bins.get(i).getAsInt());
		        			}	
		                }
		                JsonArray timer = trees.getAsJsonArray("timer");
		                if(timer != null && timer.size() > 0){
		        			for (int i = 0; i < player.getCompost().getCompostBinsTimer().length; i++) {
		        				if(i >= timer.size())
									break;
		        				player.getCompost().setCompostBinsTimer(i, timer.get(i).getAsLong());
		        			}
		                }
		                JsonArray organicItems = trees.getAsJsonArray("organicItems");
		                if(organicItems != null && organicItems.size() > 0){
		        			for (int i = 0; i < player.getCompost().getOrganicItemAdded().length; i++) {
		        				if(i >= organicItems.size())
									break;
		        				player.getCompost().setOrganicItemAdded(i, organicItems.get(i).getAsInt());
		        			}
		                }
		            }
		            JsonArray farmingTools = farming.getAsJsonArray("tools");
		            if(farmingTools != null && farmingTools.size() > 0){
		    			for (int i = 0; i < player.getFarmingTools().getTools().length; i++) {
		    				if(i >= farmingTools.size())
								break;
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
