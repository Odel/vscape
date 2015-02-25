package com.rs2.util;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.rs2.GlobalVariables;
import com.rs2.model.content.quests.Quest;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.skills.magic.Spell;
import com.rs2.model.content.skills.magic.SpellBook;
import com.rs2.model.players.Player;
import com.rs2.model.players.Player.BankOptions;
import com.rs2.model.players.item.Item;

public class PlayerSaveSerialize implements JsonSerializer<Player> {

    @Override
    public JsonElement serialize(final Player player, final Type typeOfSrc, final JsonSerializationContext context) {
		JsonObject characterObj = new JsonObject();
		characterObj.addProperty("username", player.getUsername());
		characterObj.addProperty("password", player.getPassword());
		characterObj.addProperty("rights", player.getStaffRights());
		characterObj.addProperty("mac", player.getMacAddress());
		characterObj.addProperty("host", player.getHost());
		characterObj.addProperty("lastseen", GlobalVariables.datetime.format(new Date()));
		characterObj.addProperty("muteExpire", player.getMuteExpire());
		characterObj.addProperty("banExpire", player.getBanExpire());
		characterObj.addProperty("inJail", player.getInJail());
		characterObj.addProperty("isIronman", player.isIronman());
		characterObj.addProperty("clanChat", player.getClanChat() != null ? player.getClanChat().owner : 0);
		JsonObject positionObj = new JsonObject();
		positionObj.addProperty("x", player.getPosition().getX());
		positionObj.addProperty("y", player.getPosition().getY());
		positionObj.addProperty("z", player.getPosition().getZ());
		characterObj.add("position", positionObj);
		JsonObject positionLastObj = new JsonObject();
		positionLastObj.addProperty("x", player.getPosition().getLastX());
		positionLastObj.addProperty("y", player.getPosition().getLastY());
		positionLastObj.addProperty("z", player.getPosition().getLastZ());
		characterObj.add("lastPosition", positionLastObj);
		JsonObject appearanceObj = new JsonObject();
		appearanceObj.addProperty("gender", player.getGender());
		JsonArray appearanceArray = new JsonArray();
		for (int i = 0; i < player.getAppearance().length; i++) {
			appearanceArray.add(new JsonPrimitive(player.getAppearance()[i]));
		}
		appearanceObj.add("appearanceData", appearanceArray);
		JsonArray appearanceColorArray = new JsonArray();
		for (int i = 0; i < player.getColors().length; i++) {
			appearanceColorArray.add(new JsonPrimitive(player.getColors()[i]));
		}
		appearanceObj.add("colorData", appearanceColorArray);
		characterObj.add("appearance", appearanceObj);
		JsonObject optionsObj = new JsonObject();
		optionsObj.addProperty("yellColor", player.getHideColors());
		optionsObj.addProperty("runenergy", player.getEnergy());
		optionsObj.addProperty("runtoggled", player.getMovementHandler().isRunToggled());
		optionsObj.addProperty("brightness", player.getScreenBrightness());
		optionsObj.addProperty("mousebuttons", player.getMouseButtons());
		optionsObj.addProperty("chateffects", player.getChatEffects());
		optionsObj.addProperty("splitprivate", player.getSplitPrivateChat());
		optionsObj.addProperty("acceptaid", player.getAcceptAid());
		optionsObj.addProperty("musicvolume", player.getMusicVolume());
		optionsObj.addProperty("effectvolume", player.getEffectVolume());
		characterObj.add("options", optionsObj);
		characterObj.addProperty("tutorialstage", player.getNewComersSide().getTutorialIslandStage());
		characterObj.addProperty("tutorialprogress", player.getNewComersSide().getProgressValue());
		
		JsonObject combatObj = new JsonObject();
		combatObj.addProperty("autoretaliate", player.shouldAutoRetaliate());
		combatObj.addProperty("fightmode", player.getFightMode());
		combatObj.addProperty("specialamount", player.getSpecialAmount());
		combatObj.addProperty("magicBook", player.getMagicBookType() == SpellBook.ANCIENT);
		combatObj.addProperty("skulltimer", player.getSkullTimer());
		combatObj.addProperty("poisonImmunity", player.getPoisonImmunity().ticksRemaining());
		combatObj.addProperty("fireImmunity", player.getFireImmunity().ticksRemaining());
		combatObj.addProperty("teleBlockTimer", player.getTeleblockTimer().ticksRemaining());
		combatObj.addProperty("poisonDamage", player.getPoisonDamage());
		JsonArray degradeArray = new JsonArray();
		for (int i = 0; i < player.getDegradeableHits().length; i++) {
			degradeArray.add(new JsonPrimitive(player.getDegradeableHits()[i]));
		}
		combatObj.add("degradeableHits", degradeArray);
		characterObj.add("combat", combatObj);
		
		JsonObject itemDataObj = new JsonObject();
		itemDataObj.addProperty("bindingcharge", player.getBindingNeckCharge());
		itemDataObj.addProperty("forginglife", player.getRingOfForgingLife());
		itemDataObj.addProperty("recoillife", player.getRingOfRecoilLife());
		itemDataObj.addProperty("claybracelet", player.getClayBraceletLife());
		itemDataObj.addProperty("godbook", player.getGodBook());
		itemDataObj.addProperty("lostgodbook", player.getLostGodBook());
		itemDataObj.addProperty("usedFreeGauntletsCharge", player.getQuestVars().usedFreeGauntletsCharge());
		itemDataObj.addProperty("defender", player.getDefender());
		itemDataObj.addProperty("dfsCharges", player.getDfsCharges());
		JsonArray pouchArray = new JsonArray();
		for (int i = 0; i < 4; i++) {
			pouchArray.add(new JsonPrimitive(player.getPouchData(i)));
		}
		itemDataObj.add("pouchData", pouchArray);
		JsonArray puzzleArray = new JsonArray();
        for(Item item : player.getPuzzle().puzzleStoredItems) {
                int itemId = -1;
                if(item != null){
                        itemId = item.getId();
                }
                puzzleArray.add(new JsonPrimitive(itemId));
        }
        itemDataObj.add("puzzleStoredItems", puzzleArray);
        itemDataObj.addProperty("recieveMasks", player.getReceivedMasks());
        itemDataObj.addProperty("hasZombieHead", player.getHasZombieHead());
		characterObj.add("itemData", itemDataObj);
		
		JsonObject worldDataObj = new JsonObject();
		worldDataObj.addProperty("coaltrucks", player.getCoalTruckAmount());
		worldDataObj.addProperty("ectoGrinderBoneType", player.getEctofuntus().boneType != null ? player.getEctofuntus().boneType.boneId : -1);
		worldDataObj.addProperty("bonesInLoader", player.getEctofuntus().getBonesInLoader().size());
		worldDataObj.addProperty("bonemealInBin", player.getEctofuntus().getBonemealInBin().size());
		worldDataObj.addProperty("brimhavenOpen", player.isBrimhavenDungeonOpen());
		characterObj.add("worldData", worldDataObj);
		
		JsonObject npcDataObj = new JsonObject();
		npcDataObj.addProperty("runecraftNpc", player.getRunecraftNpc());
		npcDataObj.addProperty("killedClueAttacker", player.hasKilledClueAttacker());	
		npcDataObj.addProperty("killedTreeSpirit", player.hasKilledTreeSpirit());
		npcDataObj.addProperty("killedJungleDemon", player.hasKilledJungleDemon());
		JsonObject catObj = new JsonObject();
		catObj.addProperty("catItemId", player.getCat().getCatItem());
		catObj.addProperty("growthStage", player.getCat().getGrowthStage());
		catObj.addProperty("growthTime", player.getCat().getGrowthTime());
		catObj.addProperty("hungerTime", player.getCat().getHungerTime());
		catObj.addProperty("attentionTime", player.getCat().getAttentionTime());
		catObj.addProperty("ratsCaught", player.getCat().getRatsCaught());
		npcDataObj.add("catData", catObj);
		characterObj.add("npcData", npcDataObj);
		
		JsonObject slayerDataObj = new JsonObject();
		slayerDataObj.addProperty("slayerMaster", player.getSlayer().slayerMaster);
		slayerDataObj.addProperty("slayerTask", player.getSlayer().slayerTask);
		slayerDataObj.addProperty("taskAmount", player.getSlayer().taskAmount);
		characterObj.add("slayerData", slayerDataObj);
		
		JsonObject dehydrationObj = new JsonObject();
		dehydrationObj.addProperty("curDehydrateTime", player.getDesertHeat().getDehydrateTime());
		dehydrationObj.addProperty("curDehydrateDmgTime", player.getDesertHeat().getDehydrateDmgTime());
		dehydrationObj.addProperty("dehydrated", player.getDesertHeat().getDehydrated());
		characterObj.add("dehydrationData", dehydrationObj);
		
		JsonArray friendsArray = new JsonArray();
		for (int i = 0; i < player.getFriends().length; i++) {
			if(player.getFriends()[i] < 0L || player.getFriends()[i] >= 0x7dcff8986ea31000L)
				continue;
			friendsArray.add(new JsonPrimitive(player.getFriends()[i]));
		}
		JsonArray ignoresArray = new JsonArray();
		for (int i = 0; i < player.getIgnores().length; i++) {
			if(player.getIgnores()[i] < 0L || player.getIgnores()[i] >= 0x7dcff8986ea31000L)
				continue;
			ignoresArray.add(new JsonPrimitive(player.getIgnores()[i]));
		}
		
		JsonArray skillArray = new JsonArray();
		for (int i = 0; i < 22; i++) {
			JsonObject levelObj = new JsonObject();
			levelObj.addProperty("lvl", player.getSkill().getLevel()[i]);
			levelObj.addProperty("xp", (int) player.getSkill().getExp()[i]);
			skillArray.add(levelObj);
		}
		
		JsonArray invArray = new JsonArray();
		for (int i = 0; i < 28; i++) {
        	JsonObject invItemObj = new JsonObject();
        	Item item = player.getInventory().getItemContainer().get(i);
        	if (item == null) {
				invItemObj.addProperty("id", 65535);
			} else {
				invItemObj.addProperty("id", item.getId());
	        	invItemObj.addProperty("count", item.getCount());
	        	invItemObj.addProperty("timer", item.getTimer());
			}
        	invArray.add(invItemObj);
		}
		
		JsonArray equipArray = new JsonArray();
		for (int i = 0; i < 14; i++) {
        	JsonObject equipItemObj = new JsonObject();
        	Item item = player.getEquipment().getItemContainer().get(i);
        	if (item == null) {
        		equipItemObj.addProperty("id", 65535);
			} else {
				equipItemObj.addProperty("id", item.getId());
				equipItemObj.addProperty("count", item.getCount());
			}
        	equipArray.add(equipItemObj);
		}
		
		JsonObject bankObj = new JsonObject();
		bankObj.addProperty("changingpin", player.getBankPin().isChangingBankPin());
		bankObj.addProperty("deletingpin", player.getBankPin().isDeletingBankPin());
		bankObj.addProperty("appendYear", player.getBankPin().getPinAppendYear());
		bankObj.addProperty("appendDay", player.getBankPin().getPinAppendDay());
		JsonArray bankPinArray = new JsonArray();
		for (int i = 0; i < player.getBankPin().getBankPin().length; i++) {
			bankPinArray.add(new JsonPrimitive(player.getBankPin().getBankPin()[i]));
		}
		bankObj.add("pin", bankPinArray);
		JsonArray bankPinPendingArray = new JsonArray();
		for (int i = 0; i < player.getBankPin().getPendingBankPin().length; i++) {
			bankPinArray.add(new JsonPrimitive(player.getBankPin().getPendingBankPin()[i]));
		}
		bankObj.add("pinPending", bankPinPendingArray);
		bankObj.addProperty("hasReset", player.hasReset());
		bankObj.addProperty("swapMode", player.getBankOptions().equals(BankOptions.SWAP_ITEM));
		bankObj.addProperty("withdrawAsNote", player.isWithdrawAsNote());
		bankObj.addProperty("usedTabs", player.getBankManager().getUsedTabs());
		for (int i = 0; i < player.getBankManager().getUsedTabs(); i++) {
			JsonArray bankArray = new JsonArray();
			for (int j = 0; j < player.getBankManager().tabContainer(i).size(); j++) {
				JsonObject bankItemObj = new JsonObject();
	        	Item item = player.getBankManager().tabContainer(i).get(j);
	        	if (item == null) {
	        		bankItemObj.addProperty("id", 65535);
				} else {
					bankItemObj.addProperty("id", item.getId());
					bankItemObj.addProperty("count", item.getCount());
		        	bankItemObj.addProperty("timer", item.getTimer());
				}
	        	bankArray.add(bankItemObj);
			}
			if(i == 0) { 
				bankObj.add("items", bankArray);
			}else{
				bankObj.add("items"+i, bankArray);
			}
		}
		
		JsonArray pendingItemsArray = new JsonArray();
		for (int i = 0; i < player.getPendingItems().length; i++) {
			JsonObject pendingItemObj = new JsonObject();
			pendingItemObj.addProperty("id", player.getPendingItems()[i]);
			pendingItemObj.addProperty("count", player.getPendingItemsAmount()[i]);
			pendingItemsArray.add(pendingItemObj);
		}
		
		JsonObject questObj = new JsonObject();
		questObj.addProperty("questpoints", player.getQuestPoints());
		JsonObject questVarsObj = new JsonObject();
		questVarsObj.addProperty("phoenixGang", player.getQuestVars().isPhoenixGang());
		questVarsObj.addProperty("blackArmGang", player.getQuestVars().isBlackArmGang());
		questVarsObj.addProperty("melzarsDoorUnlock", player.getQuestVars().getMelzarsDoorUnlock());
		questVarsObj.addProperty("bananaCrate", player.getQuestVars().getBananaCrate());
		questVarsObj.addProperty("bananaCrateCount", player.getQuestVars().getBananaCrateCount());
		questVarsObj.addProperty("ectoWorshipCount", player.getEctoWorshipCount());	
		questVarsObj.addProperty("topHalfFlag", player.getQuestVars().getTopHalfFlag());
		questVarsObj.addProperty("bottomHalfFlag", player.getQuestVars().getBottomHalfFlag());
		questVarsObj.addProperty("skullFlag", player.getQuestVars().getSkullFlag());
		questVarsObj.addProperty("desiredTopHalfFlag", player.getQuestVars().getDesiredTopHalfFlag());
		questVarsObj.addProperty("desiredBottomHalfFlag", player.getQuestVars().getDesiredBottomHalfFlag());
		questVarsObj.addProperty("desiredSkullFlag", player.getQuestVars().getDesiredSkullFlag());
		questVarsObj.addProperty("petitionSigned", player.getQuestVars().petitionSigned());
		questVarsObj.addProperty("snailSlime", player.getQuestVars().givenSnailSlime());
		questVarsObj.addProperty("idPapers", player.getQuestVars().givenIdPapers());
		questVarsObj.addProperty("hasShotGrip", player.getQuestVars().hasShotGrip());
		questVarsObj.addProperty("ballistaIndex", player.getQuestVars().getBallistaIndex());
		questVarsObj.addProperty("gazeOfSaradomin", player.getQuestVars().isGazeOfSaradomin());
		questVarsObj.addProperty("1stMortMyreBridge", player.getQuestVars().getMortMyreBridgeFixed(1));
		questVarsObj.addProperty("2ndMortMyreBridge", player.getQuestVars().getMortMyreBridgeFixed(2));
		questVarsObj.addProperty("3rdMortMyreBridge", player.getQuestVars().getMortMyreBridgeFixed(3));
		questObj.add("questVars", questVarsObj);
		JsonObject monkeyMadnessVarsObj = new JsonObject();
		if(player.getQuestStage(36) > 0) {
		    monkeyMadnessVarsObj.addProperty("spokenMonkeyChild", player.getMMVars().spokenToMonkeyChild());
		    monkeyMadnessVarsObj.addProperty("monkeyChildBananas", player.getMMVars().givenMonkeyChildBananas());
		    monkeyMadnessVarsObj.addProperty("monkeyChildToy", player.getMMVars().monkeyChildHasToy());
		    monkeyMadnessVarsObj.addProperty("openGate", player.getMMVars().openGate());
		    monkeyMadnessVarsObj.addProperty("firstTimeJail", player.getMMVars().firstTimeJail());
		    monkeyMadnessVarsObj.addProperty("canHideInGrass", player.getMMVars().canHideInGrass());
		    monkeyMadnessVarsObj.addProperty("gotAmulet", player.getMMVars().gotAmulet());
		    monkeyMadnessVarsObj.addProperty("gotTalisman", player.getMMVars().gotTalisman());
		    monkeyMadnessVarsObj.addProperty("monkeyPetDeleted", player.getMMVars().monkeyPetDeleted);
		    monkeyMadnessVarsObj.addProperty("receivedClue", player.getMMVars().receivedClueFromMonkey());
		    monkeyMadnessVarsObj.addProperty("trainingComplete", player.getMMVars().trainingComplete());
		}
		questObj.add("monkeyMadnessVars", monkeyMadnessVarsObj);
		JsonArray questArray = new JsonArray();
		for(Quest q : QuestHandler.getQuests())
		{
			JsonObject questDataObj = new JsonObject();
			questDataObj.addProperty("name", q.getQuestSaveName());
			questDataObj.addProperty("stage", player.getQuestStage(q.getQuestID()));
			questArray.add(questDataObj);
		}
		questObj.add("questData", questArray);
		
		JsonObject minigameObj = new JsonObject();
		JsonObject barrowsObj = new JsonObject();
		barrowsObj.addProperty("grave", player.getRandomGrave());
		barrowsObj.addProperty("killCount", player.getKillCount());
		JsonArray barrowsKilledArray = new JsonArray();
		for (int i = 0; i < 6; i++) {
			barrowsKilledArray.add(new JsonPrimitive(player.getBarrowsNpcDead(i)));
		}
		barrowsObj.add("brothersKilled", barrowsKilledArray);
		minigameObj.add("barrows", barrowsObj);
		JsonObject pestControlObj = new JsonObject();
		pestControlObj.addProperty("pcPoints", player.getPcPoints());
		minigameObj.add("pestControl", pestControlObj);
		JsonObject mageArenaObj = new JsonObject();
		mageArenaObj.addProperty("zamorakCasts", player.getMageArenaCasts(Spell.FLAMES_OF_ZAMORAK));
		mageArenaObj.addProperty("saradominCasts", player.getMageArenaCasts(Spell.SARADOMIN_STRIKE));
		mageArenaObj.addProperty("guthixCasts", player.getMageArenaCasts(Spell.CLAWS_OF_GUTHIX));
		mageArenaObj.addProperty("arenaStage", player.getMageArenaStage());
		minigameObj.add("mageArena", mageArenaObj);
		JsonObject fightCavesObj = new JsonObject();
		fightCavesObj.addProperty("wave", player.getFightCavesWave());
		minigameObj.add("fightCaves", fightCavesObj);
		JsonObject mageTrainingArenaObj = new JsonObject();
		JsonObject enchantingChamberObj = new JsonObject();
		JsonObject alchemistPlaygroundObj = new JsonObject();
		JsonObject creatureGraveyardObj = new JsonObject();
		JsonObject telekineticTheatreObj = new JsonObject();
		enchantingChamberObj.addProperty("pizazzPoints", player.getEnchantingPizazz());
		enchantingChamberObj.addProperty("enchantCount", player.getEnchantingEnchantCount());
		enchantingChamberObj.addProperty("orbCount", player.getEnchantingOrbCount());
		alchemistPlaygroundObj.addProperty("pizazzPoints", player.getAlchemistPizazz());
		creatureGraveyardObj.addProperty("pizazzPoints", player.getGraveyardPizazz());
		creatureGraveyardObj.addProperty("graveyardFruitDeposited", player.getGraveyardFruitDeposited());
		creatureGraveyardObj.addProperty("bonesToPeachesEnabled", player.bonesToPeachesEnabled());
		telekineticTheatreObj.addProperty("pizazzPoints", player.getTelekineticPizazz());
		telekineticTheatreObj.addProperty("mazesCompleted", player.getTelekineticMazesCompleted());
		mageTrainingArenaObj.add("enchantingChamber", enchantingChamberObj);
		mageTrainingArenaObj.add("alchemistPlayground", alchemistPlaygroundObj);
		mageTrainingArenaObj.add("creatureGraveyard", creatureGraveyardObj);
		mageTrainingArenaObj.add("telekineticTheatre", telekineticTheatreObj);
		minigameObj.add("mageTrainingArena", mageTrainingArenaObj);
		
		JsonObject farmingObj = new JsonObject();
		JsonObject allotmentsObj = new JsonObject();
		JsonArray allotmentStageArray = new JsonArray();
		for (int i = 0; i < player.getAllotment().getFarmingStages().length; i++) {
			allotmentStageArray.add(new JsonPrimitive(player.getAllotment().getFarmingStages()[i]));
		}
		allotmentsObj.add("stages", allotmentStageArray);
		JsonArray allotmentSeedsArray = new JsonArray();
		for (int i = 0; i < player.getAllotment().getFarmingSeeds().length; i++) {
			allotmentSeedsArray.add(new JsonPrimitive(player.getAllotment().getFarmingSeeds()[i]));
		}
		allotmentsObj.add("seeds", allotmentSeedsArray);
		JsonArray allotmentHarvestArray = new JsonArray();
		for (int i = 0; i < player.getAllotment().getFarmingHarvest().length; i++) {
			allotmentHarvestArray.add(new JsonPrimitive(player.getAllotment().getFarmingHarvest()[i]));
		}
		allotmentsObj.add("harvest", allotmentHarvestArray);
		JsonArray allotmentStateArray = new JsonArray();
		for (int i = 0; i < player.getAllotment().getFarmingState().length; i++) {
			allotmentStateArray.add(new JsonPrimitive(player.getAllotment().getFarmingState()[i]));
		}
		allotmentsObj.add("state", allotmentStateArray);
		JsonArray allotmentTimerArray = new JsonArray();
		for (int i = 0; i < player.getAllotment().getFarmingTimer().length; i++) {
			allotmentTimerArray.add(new JsonPrimitive(player.getAllotment().getFarmingTimer()[i]));
		}
		allotmentsObj.add("timer", allotmentTimerArray);
		JsonArray allotmentDiseaseArray = new JsonArray();
		for (int i = 0; i < player.getAllotment().getDiseaseChance().length; i++) {
			allotmentDiseaseArray.add(new JsonPrimitive(player.getAllotment().getDiseaseChance()[i]));
		}
		allotmentsObj.add("diseaseChance", allotmentDiseaseArray);
		JsonArray allotmentWatchedArray = new JsonArray();
		for (int i = 0; i < player.getAllotment().getFarmingWatched().length; i++) {
			allotmentWatchedArray.add(new JsonPrimitive(player.getAllotment().getFarmingWatched()[i]));
		}
		allotmentsObj.add("watched", allotmentWatchedArray);
		
		JsonObject bushesObj = new JsonObject();
		JsonArray bushesStageArray = new JsonArray();
		for (int i = 0; i < player.getBushes().getFarmingStages().length; i++) {
			bushesStageArray.add(new JsonPrimitive(player.getBushes().getFarmingStages()[i]));
		}
		bushesObj.add("stages", bushesStageArray);
		JsonArray bushesSeedsArray = new JsonArray();
		for (int i = 0; i < player.getBushes().getFarmingSeeds().length; i++) {
			bushesSeedsArray.add(new JsonPrimitive(player.getBushes().getFarmingSeeds()[i]));
		}
		bushesObj.add("seeds", bushesSeedsArray);
		JsonArray bushesStateArray = new JsonArray();
		for (int i = 0; i < player.getBushes().getFarmingState().length; i++) {
			bushesStateArray.add(new JsonPrimitive(player.getBushes().getFarmingState()[i]));
		}
		bushesObj.add("state", bushesStateArray);
		JsonArray bushesTimerArray = new JsonArray();
		for (int i = 0; i < player.getBushes().getFarmingTimer().length; i++) {
			bushesTimerArray.add(new JsonPrimitive(player.getBushes().getFarmingTimer()[i]));
		}
		bushesObj.add("timer", bushesTimerArray);
		JsonArray bushesDiseaseArray = new JsonArray();
		for (int i = 0; i < player.getBushes().getDiseaseChance().length; i++) {
			bushesDiseaseArray.add(new JsonPrimitive(player.getBushes().getDiseaseChance()[i]));
		}
		bushesObj.add("diseaseChance", bushesDiseaseArray);
		JsonArray bushesWatchedArray = new JsonArray();
		for (int i = 0; i < player.getBushes().getFarmingWatched().length; i++) {
			bushesWatchedArray.add(new JsonPrimitive(player.getBushes().getFarmingWatched()[i]));
		}
		bushesObj.add("watched", bushesWatchedArray);
		
		JsonObject flowersObj = new JsonObject();
		JsonArray flowersStageArray = new JsonArray();
		for (int i = 0; i < player.getFlowers().getFarmingStages().length; i++) {
			flowersStageArray.add(new JsonPrimitive(player.getFlowers().getFarmingStages()[i]));
		}
		flowersObj.add("stages", flowersStageArray);
		JsonArray flowersSeedsArray = new JsonArray();
		for (int i = 0; i < player.getFlowers().getFarmingSeeds().length; i++) {
			flowersSeedsArray.add(new JsonPrimitive(player.getFlowers().getFarmingSeeds()[i]));
		}
		flowersObj.add("seeds", flowersSeedsArray);
		JsonArray flowersStateArray = new JsonArray();
		for (int i = 0; i < player.getFlowers().getFarmingState().length; i++) {
			flowersStateArray.add(new JsonPrimitive(player.getFlowers().getFarmingState()[i]));
		}
		flowersObj.add("state", flowersStateArray);
		JsonArray flowersTimerArray = new JsonArray();
		for (int i = 0; i < player.getFlowers().getFarmingTimer().length; i++) {
			flowersTimerArray.add(new JsonPrimitive(player.getFlowers().getFarmingTimer()[i]));
		}
		flowersObj.add("timer", flowersTimerArray);
		JsonArray flowersDiseaseArray = new JsonArray();
		for (int i = 0; i < player.getFlowers().getDiseaseChance().length; i++) {
			flowersDiseaseArray.add(new JsonPrimitive(player.getFlowers().getDiseaseChance()[i]));
		}
		flowersObj.add("diseaseChance", flowersDiseaseArray);
		
		JsonObject fruitTreesObj = new JsonObject();
		JsonArray fruitTreesStageArray = new JsonArray();
		for (int i = 0; i < player.getFruitTrees().getFarmingStages().length; i++) {
			fruitTreesStageArray.add(new JsonPrimitive(player.getFruitTrees().getFarmingStages()[i]));
		}
		fruitTreesObj.add("stages", fruitTreesStageArray);
		JsonArray fruitTreesSeedsArray = new JsonArray();
		for (int i = 0; i < player.getFruitTrees().getFarmingSeeds().length; i++) {
			fruitTreesSeedsArray.add(new JsonPrimitive(player.getFruitTrees().getFarmingSeeds()[i]));
		}
		fruitTreesObj.add("seeds", fruitTreesSeedsArray);
		JsonArray fruitTreesStateArray = new JsonArray();
		for (int i = 0; i < player.getFruitTrees().getFarmingState().length; i++) {
			fruitTreesStateArray.add(new JsonPrimitive(player.getFruitTrees().getFarmingState()[i]));
		}
		fruitTreesObj.add("state", fruitTreesStateArray);
		JsonArray fruitTreesTimerArray = new JsonArray();
		for (int i = 0; i < player.getFruitTrees().getFarmingTimer().length; i++) {
			fruitTreesTimerArray.add(new JsonPrimitive(player.getFruitTrees().getFarmingTimer()[i]));
		}
		fruitTreesObj.add("timer", fruitTreesTimerArray);
		JsonArray fruitTreesDiseaseArray = new JsonArray();
		for (int i = 0; i < player.getFruitTrees().getDiseaseChance().length; i++) {
			fruitTreesDiseaseArray.add(new JsonPrimitive(player.getFruitTrees().getDiseaseChance()[i]));
		}
		fruitTreesObj.add("diseaseChance", fruitTreesDiseaseArray);
		JsonArray fruitTreesWatchedArray = new JsonArray();
		for (int i = 0; i < player.getFruitTrees().getFarmingWatched().length; i++) {
			fruitTreesWatchedArray.add(new JsonPrimitive(player.getFruitTrees().getFarmingWatched()[i]));
		}
		fruitTreesObj.add("watched", fruitTreesWatchedArray);
		
		JsonObject herbsObj = new JsonObject();
		JsonArray herbsStageArray = new JsonArray();
		for (int i = 0; i < player.getHerbs().getFarmingStages().length; i++) {
			herbsStageArray.add(new JsonPrimitive(player.getHerbs().getFarmingStages()[i]));
		}
		herbsObj.add("stages", herbsStageArray);
		JsonArray herbseedsArray = new JsonArray();
		for (int i = 0; i < player.getHerbs().getFarmingSeeds().length; i++) {
			herbseedsArray.add(new JsonPrimitive(player.getHerbs().getFarmingSeeds()[i]));
		}
		herbsObj.add("seeds", herbseedsArray);
		JsonArray herbsHarvestArray = new JsonArray();
		for (int i = 0; i < player.getHerbs().getFarmingHarvest().length; i++) {
			herbsHarvestArray.add(new JsonPrimitive(player.getHerbs().getFarmingHarvest()[i]));
		}
		herbsObj.add("harvest", herbsHarvestArray);
		JsonArray herbstateArray = new JsonArray();
		for (int i = 0; i < player.getHerbs().getFarmingState().length; i++) {
			herbstateArray.add(new JsonPrimitive(player.getHerbs().getFarmingState()[i]));
		}
		herbsObj.add("state", herbstateArray);
		JsonArray herbsTimerArray = new JsonArray();
		for (int i = 0; i < player.getHerbs().getFarmingTimer().length; i++) {
			herbsTimerArray.add(new JsonPrimitive(player.getHerbs().getFarmingTimer()[i]));
		}
		herbsObj.add("timer", herbsTimerArray);
		JsonArray herbsDiseaseArray = new JsonArray();
		for (int i = 0; i < player.getHerbs().getDiseaseChance().length; i++) {
			herbsDiseaseArray.add(new JsonPrimitive(player.getHerbs().getDiseaseChance()[i]));
		}
		herbsObj.add("diseaseChance", herbsDiseaseArray);
		
		JsonObject hopsObj = new JsonObject();
		JsonArray hopsStageArray = new JsonArray();
		for (int i = 0; i < player.getHops().getFarmingStages().length; i++) {
			hopsStageArray.add(new JsonPrimitive(player.getHops().getFarmingStages()[i]));
		}
		hopsObj.add("stages", hopsStageArray);
		JsonArray hopsSeedsArray = new JsonArray();
		for (int i = 0; i < player.getHops().getFarmingSeeds().length; i++) {
			hopsSeedsArray.add(new JsonPrimitive(player.getHops().getFarmingSeeds()[i]));
		}
		hopsObj.add("seeds", hopsSeedsArray);
		JsonArray hopsHarvestArray = new JsonArray();
		for (int i = 0; i < player.getHops().getFarmingHarvest().length; i++) {
			hopsHarvestArray.add(new JsonPrimitive(player.getHops().getFarmingHarvest()[i]));
		}
		hopsObj.add("harvest", hopsHarvestArray);
		JsonArray hopsStateArray = new JsonArray();
		for (int i = 0; i < player.getHops().getFarmingState().length; i++) {
			hopsStateArray.add(new JsonPrimitive(player.getHops().getFarmingState()[i]));
		}
		hopsObj.add("state", hopsStateArray);
		JsonArray hopsTimerArray = new JsonArray();
		for (int i = 0; i < player.getHops().getFarmingTimer().length; i++) {
			hopsTimerArray.add(new JsonPrimitive(player.getHops().getFarmingTimer()[i]));
		}
		hopsObj.add("timer", hopsTimerArray);
		JsonArray hopsDiseaseArray = new JsonArray();
		for (int i = 0; i < player.getHops().getDiseaseChance().length; i++) {
			hopsDiseaseArray.add(new JsonPrimitive(player.getHops().getDiseaseChance()[i]));
		}
		hopsObj.add("diseaseChance", hopsDiseaseArray);
		JsonArray hopsWatchedArray = new JsonArray();
		for (int i = 0; i < player.getHops().getFarmingWatched().length; i++) {
			hopsWatchedArray.add(new JsonPrimitive(player.getHops().getFarmingWatched()[i]));
		}
		hopsObj.add("watched", hopsWatchedArray);
		
		JsonObject specialPlantOneObj = new JsonObject();
		JsonArray specialPlantOneStageArray = new JsonArray();
		for (int i = 0; i < player.getSpecialPlantOne().getFarmingStages().length; i++) {
			specialPlantOneStageArray.add(new JsonPrimitive(player.getSpecialPlantOne().getFarmingStages()[i]));
		}
		specialPlantOneObj.add("stages", specialPlantOneStageArray);
		JsonArray specialPlantOneSeedsArray = new JsonArray();
		for (int i = 0; i < player.getSpecialPlantOne().getFarmingSeeds().length; i++) {
			specialPlantOneSeedsArray.add(new JsonPrimitive(player.getSpecialPlantOne().getFarmingSeeds()[i]));
		}
		specialPlantOneObj.add("seeds", specialPlantOneSeedsArray);
		JsonArray specialPlantOneStateArray = new JsonArray();
		for (int i = 0; i < player.getSpecialPlantOne().getFarmingState().length; i++) {
			specialPlantOneStateArray.add(new JsonPrimitive(player.getSpecialPlantOne().getFarmingState()[i]));
		}
		specialPlantOneObj.add("state", specialPlantOneStateArray);
		JsonArray specialPlantOneTimerArray = new JsonArray();
		for (int i = 0; i < player.getSpecialPlantOne().getFarmingTimer().length; i++) {
			specialPlantOneTimerArray.add(new JsonPrimitive(player.getSpecialPlantOne().getFarmingTimer()[i]));
		}
		specialPlantOneObj.add("timer", specialPlantOneTimerArray);
		JsonArray specialPlantOneDiseaseArray = new JsonArray();
		for (int i = 0; i < player.getSpecialPlantOne().getDiseaseChance().length; i++) {
			specialPlantOneDiseaseArray.add(new JsonPrimitive(player.getSpecialPlantOne().getDiseaseChance()[i]));
		}
		specialPlantOneObj.add("diseaseChance", specialPlantOneDiseaseArray);
		
		JsonObject specialPlantTwoObj = new JsonObject();
		JsonArray specialPlantTwoStageArray = new JsonArray();
		for (int i = 0; i < player.getSpecialPlantTwo().getFarmingStages().length; i++) {
			specialPlantTwoStageArray.add(new JsonPrimitive(player.getSpecialPlantTwo().getFarmingStages()[i]));
		}
		specialPlantTwoObj.add("stages", specialPlantTwoStageArray);
		JsonArray specialPlantTwoSeedsArray = new JsonArray();
		for (int i = 0; i < player.getSpecialPlantTwo().getFarmingSeeds().length; i++) {
			specialPlantTwoSeedsArray.add(new JsonPrimitive(player.getSpecialPlantTwo().getFarmingSeeds()[i]));
		}
		specialPlantTwoObj.add("seeds", specialPlantTwoSeedsArray);
		JsonArray specialPlantTwoStateArray = new JsonArray();
		for (int i = 0; i < player.getSpecialPlantTwo().getFarmingState().length; i++) {
			specialPlantTwoStateArray.add(new JsonPrimitive(player.getSpecialPlantTwo().getFarmingState()[i]));
		}
		specialPlantTwoObj.add("state", specialPlantTwoStateArray);
		JsonArray specialPlantTwoTimerArray = new JsonArray();
		for (int i = 0; i < player.getSpecialPlantTwo().getFarmingTimer().length; i++) {
			specialPlantTwoTimerArray.add(new JsonPrimitive(player.getSpecialPlantTwo().getFarmingTimer()[i]));
		}
		specialPlantTwoObj.add("timer", specialPlantTwoTimerArray);
		JsonArray specialPlantTwoDiseaseArray = new JsonArray();
		for (int i = 0; i < player.getSpecialPlantTwo().getDiseaseChance().length; i++) {
			specialPlantTwoDiseaseArray.add(new JsonPrimitive(player.getSpecialPlantTwo().getDiseaseChance()[i]));
		}
		specialPlantTwoObj.add("diseaseChance", specialPlantTwoDiseaseArray);
		
		JsonObject treesObj = new JsonObject();
		JsonArray treesStageArray = new JsonArray();
		for (int i = 0; i < player.getTrees().getFarmingStages().length; i++) {
			treesStageArray.add(new JsonPrimitive(player.getTrees().getFarmingStages()[i]));
		}
		treesObj.add("stages", treesStageArray);
		JsonArray treesSeedsArray = new JsonArray();
		for (int i = 0; i < player.getTrees().getFarmingSeeds().length; i++) {
			treesSeedsArray.add(new JsonPrimitive(player.getTrees().getFarmingSeeds()[i]));
		}
		treesObj.add("seeds", treesSeedsArray);
		JsonArray treesHarvestArray = new JsonArray();
		for (int i = 0; i < player.getTrees().getFarmingHarvest().length; i++) {
			treesHarvestArray.add(new JsonPrimitive(player.getTrees().getFarmingHarvest()[i]));
		}
		treesObj.add("harvest", treesHarvestArray);
		JsonArray treesStateArray = new JsonArray();
		for (int i = 0; i < player.getTrees().getFarmingState().length; i++) {
			treesStateArray.add(new JsonPrimitive(player.getTrees().getFarmingState()[i]));
		}
		treesObj.add("state", treesStateArray);
		JsonArray treesTimerArray = new JsonArray();
		for (int i = 0; i < player.getTrees().getFarmingTimer().length; i++) {
			treesTimerArray.add(new JsonPrimitive(player.getTrees().getFarmingTimer()[i]));
		}
		treesObj.add("timer", treesTimerArray);
		JsonArray treesDiseaseArray = new JsonArray();
		for (int i = 0; i < player.getTrees().getDiseaseChance().length; i++) {
			treesDiseaseArray.add(new JsonPrimitive(player.getTrees().getDiseaseChance()[i]));
		}
		treesObj.add("diseaseChance", treesDiseaseArray);
		JsonArray treesWatchedArray = new JsonArray();
		for (int i = 0; i < player.getTrees().getFarmingWatched().length; i++) {
			treesWatchedArray.add(new JsonPrimitive(player.getTrees().getFarmingWatched()[i]));
		}
		treesObj.add("watched", treesWatchedArray);
		
		JsonObject compostObj = new JsonObject();
		JsonArray compostBinsArray = new JsonArray();
		for (int i = 0; i < player.getCompost().getCompostBins().length; i++) {
			compostBinsArray.add(new JsonPrimitive(player.getCompost().getCompostBins()[i]));
		}
		compostObj.add("bins", compostBinsArray);
		JsonArray compostBinsTimerArray = new JsonArray();
		for (int i = 0; i < player.getCompost().getCompostBinsTimer().length; i++) {
			compostBinsTimerArray.add(new JsonPrimitive(player.getCompost().getCompostBinsTimer()[i]));
		}
		compostObj.add("timer", compostBinsTimerArray);
		JsonArray compostBinsOrganicArray = new JsonArray();
		for (int i = 0; i < player.getCompost().getOrganicItemAdded().length; i++) {
			compostBinsOrganicArray.add(new JsonPrimitive(player.getCompost().getOrganicItemAdded()[i]));
		}
		compostObj.add("organicItems", compostBinsOrganicArray);
		JsonArray toolsArray = new JsonArray();
		for (int i = 0; i < player.getFarmingTools().getTools().length; i++) {
			toolsArray.add(new JsonPrimitive(player.getFarmingTools().getTools()[i]));
		}
		farmingObj.add("allotments", allotmentsObj);
		farmingObj.add("bushes", bushesObj);
		farmingObj.add("flowers", flowersObj);
		farmingObj.add("fruitTrees", fruitTreesObj);
		farmingObj.add("herbs", herbsObj);
		farmingObj.add("hops", hopsObj);
		farmingObj.add("specialPlantOne", specialPlantOneObj);
		farmingObj.add("specialPlantTwo", specialPlantTwoObj);
		farmingObj.add("trees", treesObj);
		farmingObj.add("compost", compostObj);
		farmingObj.add("tools", toolsArray);
		
		JsonObject mainObj = new JsonObject();
		mainObj.add("character", characterObj);
		mainObj.addProperty("friendsConverted", player.getFriendsConverted());
		mainObj.add("friends", friendsArray);
		mainObj.addProperty("ignoresConverted", player.getIgnoresConverted());
		mainObj.add("ignores", ignoresArray);
		mainObj.add("skills", skillArray);
		mainObj.add("inventory", invArray);
		mainObj.add("equipment", equipArray);
		mainObj.add("bank", bankObj);
		mainObj.add("pendingItems", pendingItemsArray);
		mainObj.add("quests", questObj);
		mainObj.add("minigames", minigameObj);
		mainObj.add("farming", farmingObj);
		
		return mainObj;
    }

}
