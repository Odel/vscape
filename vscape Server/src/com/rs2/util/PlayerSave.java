package com.rs2.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rs2.Constants;
import com.rs2.Server;
import com.rs2.model.World;
import com.rs2.model.content.combat.effect.impl.PoisonEffect;
import com.rs2.model.content.combat.hit.Hit;
import com.rs2.model.content.combat.hit.HitDef;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.minigames.warriorsguild.WarriorsGuild;
import com.rs2.model.content.skills.magic.SpellBook;
import com.rs2.model.players.bank.BankManager;
import com.rs2.model.players.Player;
import com.rs2.model.players.Player.LoginStages;
import com.rs2.model.players.item.Item;
import com.rs2.net.packet.packets.AppearancePacketHandler;
import com.rs2.task.Task;
import com.rs2.task.TaskScheduler;
import com.rs2.model.content.quests.Quest;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.skills.magic.Spell;
/**
 * Static utility methods for saving and loading players.
 * 
 * @author blakeman8192
 */
public class PlayerSave {

	/** The directory where players are saved. */
	public static final String directoryOld = "./data/characters/";
	public static final String directoryNew = "./data/characters/json/";
	public static final boolean useNewFormat = true;
	
	public static boolean hasOldFormat (final Player player){
		return new File(directoryOld + player.getUsername() + ".dat").exists();
	}
	
	public static boolean hasNewFormat (final Player player){
		return new File(directoryNew + player.getUsername() + ".gz").exists();
	}
	
	public static final int SAVE_INTERVAL = 30; // in minutes
	
	public static void saveCycle() {
		new TaskScheduler().schedule(new Task(SAVE_INTERVAL * 100) {
		    @Override public void execute() {
		    	if(Constants.SYSTEM_UPDATE)
		    	{
		    		stop();
		    		return;
		    	}
                if(World.getPlayers() == null || World.getPlayers().length <= 0)
                {
                	return;
                }
	            synchronized (World.getPlayers()) {
	                final Player[] players = World.getPlayers();
	                for (Player p : players) {
	                    if (p != null && p.getIndex() != -1 && !p.getLoginStage().equals(LoginStages.LOGGING_OUT)) {
	                        try {
	                            PlayerSave.save(p);
	                        } catch (Exception e) {
	                            e.printStackTrace();
	                        }
	                    }
	                }
	            }
	            World.messageToStaff("@dre@Auto-Saved Players.");
				System.out.println("Auto-Saved Players.");
		    }
	    });
	
	}
	
	public static void saveJson(final Player player) {
		try {
			File file = new File(directoryNew + player.getUsername() + ".gz");
			if (!file.exists()) {
				file.createNewFile();
			}
		    final GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
		    gsonBuilder.registerTypeAdapter(Player.class, new PlayerSaveSerialize());
		    final Gson gson = gsonBuilder.create();
		    byte[] playerBytes = gson.toJson(player).getBytes();
			try(GZIPOutputStream compress = new GZIPOutputStream(new FileOutputStream(file))){
				compress.write(playerBytes);
				compress.flush();
				compress.close();
			} catch(IOException e) {
				System.out.println("Failed to compress "+ player.getUsername());
			}
		} catch(IOException e) {
			System.out.println("Failed to save "+ player.getUsername());
	    }
	}
	
	public static int loadJson(final Player player) {
		File file = new File(directoryNew + player.getUsername() + ".gz");
		return new PlayerSaveParser().parse(player, file);
	}
	
	/**
	 * Saves the player.
	 * 
	 * @param player
	 *            the player to save
	 * @return
	 */
	public static void save(final Player player) {
		if(useNewFormat)
		{
            saveJson(player);
		}
		else
		{
		try {
			Misc.Stopwatch stopwatch = new Misc.Stopwatch();
			File file = new File(directoryOld + player.getUsername() + ".dat");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream outFile = new FileOutputStream(file);
			DataOutputStream write = new DataOutputStream(outFile);
			write.writeUTF(player.getUsername());
			write.writeUTF(player.getPassword());
			write.writeInt(player.getStaffRights());
			write.writeInt(player.getPosition().getX());
			write.writeInt(player.getPosition().getY());
			write.writeInt(player.getPosition().getZ());
			write.writeInt(player.getGender());
			write.writeBoolean(player.shouldAutoRetaliate());
			write.writeInt(player.getFightMode());
			write.writeInt(player.getScreenBrightness());
			write.writeInt(player.getMouseButtons());
			write.writeInt(player.getChatEffects());
			write.writeInt(player.getSplitPrivateChat());
			write.writeInt(player.getAcceptAid());
			write.writeInt(player.getMusicVolume());
			write.writeInt(player.getEffectVolume());
			write.writeInt(player.getQuestPoints());
			write.writeDouble(player.getSpecialAmount());
			write.writeBoolean(player.getBankPin().isChangingBankPin());
			write.writeBoolean(player.getBankPin().isDeletingBankPin());
			write.writeInt(player.getBankPin().getPinAppendYear());
			write.writeInt(player.getBankPin().getPinAppendDay());
			write.writeInt(player.getBindingNeckCharge());
			write.writeInt(player.getRingOfForgingLife());
			write.writeInt(player.getRingOfRecoilLife());
			write.writeInt(player.getSkullTimer());
			write.writeInt(player.getNewComersSide().getTutorialIslandStage());
			write.writeInt(player.getNewComersSide().getProgressValue());
			write.writeDouble(player.getEnergy());
			write.writeBoolean(player.getMovementHandler().isRunToggled());

			for (int i = 0; i < player.getBankPin().getBankPin().length; i++) {
				write.writeInt(player.getBankPin().getBankPin()[i]);
			}
			for (int i = 0; i < player.getBankPin().getPendingBankPin().length; i++) {
				write.writeInt(player.getBankPin().getPendingBankPin()[i]);
			}
			for (Object[] element : player.questData) {
				write.writeInt((Integer) element[1]);
			}
			for (int i = 0; i < 4; i++) {
				write.writeInt(player.getPouchData(i));
			}
			for (int i = 0; i < player.getAppearance().length; i++) {
				write.writeInt(player.getAppearance()[i]);
			}
			for (int i = 0; i < player.getColors().length; i++) {
				write.writeInt(player.getColors()[i]);
			}
			for (int i = 0; i < player.getSkill().getLevel().length; i++) {
				write.writeInt(player.getSkill().getLevel()[i]);
			}
			for (int i = 0; i < player.getSkill().getExp().length; i++) {
				write.writeInt((int) player.getSkill().getExp()[i]);
			}
			for (int i = 0; i < 28; i++) {
				Item item = player.getInventory().getItemContainer().get(i);
				if (item == null) {
					write.writeInt(65535);
				} else {
					write.writeInt(item.getId());
					write.writeInt(item.getCount());
					write.writeInt(item.getTimer());
				}
			}
			for (int i = 0; i < 14; i++) {
				Item item = player.getEquipment().getItemContainer().get(i);
				if (item == null) {
					write.writeInt(65535);
				} else {
					write.writeInt(item.getId());
					write.writeInt(item.getCount());
				}
			}
			for (int i = 0; i < BankManager.SIZE; i++) {
				Item item = player.getBankManager().tabContainer(0).get(i);
				if (item == null) {
					write.writeInt(65535);
				} else {
					write.writeInt(item.getId());
					write.writeInt(item.getCount());
					write.writeInt(item.getTimer());
				}
			}
			for (int i = 0; i < player.getFriends().length; i++) {
				write.writeLong(player.getFriends()[i]);
			}
			for (int i = 0; i < player.getIgnores().length; i++) {
				write.writeLong(player.getIgnores()[i]);
			}
			for (int i = 0; i < player.getPendingItems().length; i++) {
				write.writeInt(player.getPendingItems()[i]);
				write.writeInt(player.getPendingItemsAmount()[i]);
			}
			write.writeInt(player.getRunecraftNpc());
			write.writeLong(player.getMuteExpire());
			write.writeLong(player.getBanExpire());
			write.writeBoolean(player.hasKilledTreeSpirit());
			write.writeBoolean(player.hasReset());
			write.writeBoolean(player.hasKilledJungleDemon());
			for (int i = 0; i < 6; i++) {
				write.writeBoolean(player.getBarrowsNpcDead(i));
			}
			write.writeInt(player.getKillCount());
			write.writeInt(player.getRandomGrave());
			write.writeInt(player.getPoisonImmunity().ticksRemaining());
			write.writeInt(player.getFireImmunity().ticksRemaining());
			write.writeInt(player.getTeleblockTimer().ticksRemaining());
			write.writeDouble(player.getPoisonDamage());
			for (int i = 0; i < player.getAllotment().getFarmingStages().length; i++) {
				write.writeInt(player.getAllotment().getFarmingStages()[i]);
			}
			for (int i = 0; i < player.getAllotment().getFarmingSeeds().length; i++) {
				write.writeInt(player.getAllotment().getFarmingSeeds()[i]);
			}
			for (int i = 0; i < player.getAllotment().getFarmingHarvest().length; i++) {
				write.writeInt(player.getAllotment().getFarmingHarvest()[i]);
			}
			for (int i = 0; i < player.getAllotment().getFarmingState().length; i++) {
				write.writeInt(player.getAllotment().getFarmingState()[i]);
			}
			for (int i = 0; i < player.getAllotment().getFarmingTimer().length; i++) {
				write.writeLong(player.getAllotment().getFarmingTimer()[i]);
			}
			for (int i = 0; i < player.getAllotment().getDiseaseChance().length; i++) {
				write.writeDouble(player.getAllotment().getDiseaseChance()[i]);
			}
			for (int i = 0; i < player.getAllotment().getFarmingWatched().length; i++) {
				write.writeBoolean(player.getAllotment().getFarmingWatched()[i]);
			}
			for (int i = 0; i < player.getBushes().getFarmingStages().length; i++) {
				write.writeInt(player.getBushes().getFarmingStages()[i]);
			}
			for (int i = 0; i < player.getBushes().getFarmingSeeds().length; i++) {
				write.writeInt(player.getBushes().getFarmingSeeds()[i]);
			}
			for (int i = 0; i < player.getBushes().getFarmingState().length; i++) {
				write.writeInt(player.getBushes().getFarmingState()[i]);
			}
			for (int i = 0; i < player.getBushes().getFarmingTimer().length; i++) {
				write.writeLong(player.getBushes().getFarmingTimer()[i]);
			}
			for (int i = 0; i < player.getBushes().getDiseaseChance().length; i++) {
				write.writeDouble(player.getBushes().getDiseaseChance()[i]);
			}
			for (int i = 0; i < player.getBushes().getFarmingWatched().length; i++) {
				write.writeBoolean(player.getBushes().getFarmingWatched()[i]);
			}
			for (int i = 0; i < player.getFlowers().getFarmingStages().length; i++) {
				write.writeInt(player.getFlowers().getFarmingStages()[i]);
			}
			for (int i = 0; i < player.getFlowers().getFarmingSeeds().length; i++) {
				write.writeInt(player.getFlowers().getFarmingSeeds()[i]);
			}
			for (int i = 0; i < player.getFlowers().getFarmingState().length; i++) {
				write.writeInt(player.getFlowers().getFarmingState()[i]);
			}
			for (int i = 0; i < player.getFlowers().getFarmingTimer().length; i++) {
				write.writeLong(player.getFlowers().getFarmingTimer()[i]);
			}
			for (int i = 0; i < player.getFlowers().getDiseaseChance().length; i++) {
				write.writeDouble(player.getFlowers().getDiseaseChance()[i]);
			}
			for (int i = 0; i < player.getFruitTrees().getFarmingStages().length; i++) {
				write.writeInt(player.getFruitTrees().getFarmingStages()[i]);
			}
			for (int i = 0; i < player.getFruitTrees().getFarmingSeeds().length; i++) {
				write.writeInt(player.getFruitTrees().getFarmingSeeds()[i]);
			}
			for (int i = 0; i < player.getFruitTrees().getFarmingState().length; i++) {
				write.writeInt(player.getFruitTrees().getFarmingState()[i]);
			}
			for (int i = 0; i < player.getFruitTrees().getFarmingTimer().length; i++) {
				write.writeLong(player.getFruitTrees().getFarmingTimer()[i]);
			}
			for (int i = 0; i < player.getFruitTrees().getDiseaseChance().length; i++) {
				write.writeDouble(player.getFruitTrees().getDiseaseChance()[i]);
			}
			for (int i = 0; i < player.getFruitTrees().getFarmingWatched().length; i++) {
				write.writeBoolean(player.getFruitTrees().getFarmingWatched()[i]);
			}
			for (int i = 0; i < player.getHerbs().getFarmingStages().length; i++) {
				write.writeInt(player.getHerbs().getFarmingStages()[i]);
			}
			for (int i = 0; i < player.getHerbs().getFarmingSeeds().length; i++) {
				write.writeInt(player.getHerbs().getFarmingSeeds()[i]);
			}
			for (int i = 0; i < player.getHerbs().getFarmingHarvest().length; i++) {
				write.writeInt(player.getHerbs().getFarmingHarvest()[i]);
			}
			for (int i = 0; i < player.getHerbs().getFarmingState().length; i++) {
				write.writeInt(player.getHerbs().getFarmingState()[i]);
			}
			for (int i = 0; i < player.getHerbs().getFarmingTimer().length; i++) {
				write.writeLong(player.getHerbs().getFarmingTimer()[i]);
			}
			for (int i = 0; i < player.getHerbs().getDiseaseChance().length; i++) {
				write.writeDouble(player.getHerbs().getDiseaseChance()[i]);
			}
			for (int i = 0; i < player.getHops().getFarmingStages().length; i++) {
				write.writeInt(player.getHops().getFarmingStages()[i]);
			}
			for (int i = 0; i < player.getHops().getFarmingSeeds().length; i++) {
				write.writeInt(player.getHops().getFarmingSeeds()[i]);
			}
			for (int i = 0; i < player.getHops().getFarmingHarvest().length; i++) {
				write.writeInt(player.getHops().getFarmingHarvest()[i]);
			}
			for (int i = 0; i < player.getHops().getFarmingState().length; i++) {
				write.writeInt(player.getHops().getFarmingState()[i]);
			}
			for (int i = 0; i < player.getHops().getFarmingTimer().length; i++) {
				write.writeLong(player.getHops().getFarmingTimer()[i]);
			}
			for (int i = 0; i < player.getHops().getDiseaseChance().length; i++) {
				write.writeDouble(player.getHops().getDiseaseChance()[i]);
			}
			for (int i = 0; i < player.getHops().getFarmingWatched().length; i++) {
				write.writeBoolean(player.getHops().getFarmingWatched()[i]);
			}
			for (int i = 0; i < player.getSpecialPlantOne().getFarmingStages().length; i++) {
				write.writeInt(player.getSpecialPlantOne().getFarmingStages()[i]);
			}
			for (int i = 0; i < player.getSpecialPlantOne().getFarmingSeeds().length; i++) {
				write.writeInt(player.getSpecialPlantOne().getFarmingSeeds()[i]);
			}
			for (int i = 0; i < player.getSpecialPlantOne().getFarmingState().length; i++) {
				write.writeInt(player.getSpecialPlantOne().getFarmingState()[i]);
			}
			for (int i = 0; i < player.getSpecialPlantOne().getFarmingTimer().length; i++) {
				write.writeLong(player.getSpecialPlantOne().getFarmingTimer()[i]);
			}
			for (int i = 0; i < player.getSpecialPlantOne().getDiseaseChance().length; i++) {
				write.writeDouble(player.getSpecialPlantOne().getDiseaseChance()[i]);
			}
			for (int i = 0; i < player.getSpecialPlantTwo().getFarmingStages().length; i++) {
				write.writeInt(player.getSpecialPlantTwo().getFarmingStages()[i]);
			}
			for (int i = 0; i < player.getSpecialPlantTwo().getFarmingSeeds().length; i++) {
				write.writeInt(player.getSpecialPlantTwo().getFarmingSeeds()[i]);
			}
			for (int i = 0; i < player.getSpecialPlantTwo().getFarmingState().length; i++) {
				write.writeInt(player.getSpecialPlantTwo().getFarmingState()[i]);
			}
			for (int i = 0; i < player.getSpecialPlantTwo().getFarmingTimer().length; i++) {
				write.writeLong(player.getSpecialPlantTwo().getFarmingTimer()[i]);
			}
			for (int i = 0; i < player.getSpecialPlantTwo().getDiseaseChance().length; i++) {
				write.writeDouble(player.getSpecialPlantTwo().getDiseaseChance()[i]);
			}
			for (int i = 0; i < player.getTrees().getFarmingStages().length; i++) {
				write.writeInt(player.getTrees().getFarmingStages()[i]);
			}
			for (int i = 0; i < player.getTrees().getFarmingSeeds().length; i++) {
				write.writeInt(player.getTrees().getFarmingSeeds()[i]);
			}
			for (int i = 0; i < player.getTrees().getFarmingHarvest().length; i++) {
				write.writeInt(player.getTrees().getFarmingHarvest()[i]);
			}
			for (int i = 0; i < player.getTrees().getFarmingState().length; i++) {
				write.writeInt(player.getTrees().getFarmingState()[i]);
			}
			for (int i = 0; i < player.getTrees().getFarmingTimer().length; i++) {
				write.writeLong(player.getTrees().getFarmingTimer()[i]);
			}
			for (int i = 0; i < player.getTrees().getDiseaseChance().length; i++) {
				write.writeDouble(player.getTrees().getDiseaseChance()[i]);
			}
			for (int i = 0; i < player.getTrees().getFarmingWatched().length; i++) {
				write.writeBoolean(player.getTrees().getFarmingWatched()[i]);
			}
			for (int i = 0; i < player.getCompost().getCompostBins().length; i++) {
				write.writeInt(player.getCompost().getCompostBins()[i]);
			}
			for (int i = 0; i < player.getCompost().getCompostBinsTimer().length; i++) {
				write.writeLong(player.getCompost().getCompostBinsTimer()[i]);
			}
			for (int i = 0; i < player.getCompost().getOrganicItemAdded().length; i++) {
				write.writeInt(player.getCompost().getOrganicItemAdded()[i]);
			}
			for (int i = 0; i < player.getFarmingTools().getTools().length; i++) {
				write.writeInt(player.getFarmingTools().getTools()[i]);
			}
			for (int i = 0; i < player.getDegradeableHits().length; i++) {
			    try {
				write.writeInt(player.getDegradeableHits()[i]);
			    }
			    catch(IOException e) { System.out.println("err");
				write.writeInt(0);
			    }
			}
			/*for (int i = 0; i < player.getBonesGround().length; i++) {
			    try {
				write.writeInt(player.getBonesGround()[i]);
			    }
			    catch(IOException e) {
				write.writeInt(0);
			    }
			}*/
			try {
			    write.writeInt(player.getSlayer().slayerMaster);
			} catch (IOException e) {
			    player.getSlayer().resetSlayerTask();
			}
			write.writeUTF(player.getSlayer().slayerTask);
			write.writeInt(player.getSlayer().taskAmount);
			write.writeBoolean(player.getMagicBookType() == SpellBook.ANCIENT);
			write.writeBoolean(player.isBrimhavenDungeonOpen());
			write.writeBoolean(player.hasKilledClueAttacker());
			write.writeInt(player.getClayBraceletLife());
			write.writeInt(player.getPcPoints());
			write.writeInt(player.getMageArenaCasts(Spell.FLAMES_OF_ZAMORAK));
			write.writeInt(player.getMageArenaCasts(Spell.SARADOMIN_STRIKE));
			write.writeInt(player.getMageArenaCasts(Spell.CLAWS_OF_GUTHIX));
			write.writeInt(player.getMageArenaStage());
			write.writeInt(player.getDefender());
			    write.writeBoolean(player.getQuestVars().isPhoenixGang());
			    write.writeBoolean(player.getQuestVars().isBlackArmGang());
			    write.writeBoolean(player.getQuestVars().getMelzarsDoorUnlock());
			    write.writeInt(player.getFightCavesWave());
			    write.writeBoolean(player.getQuestVars().getBananaCrate());
			    write.writeInt(player.getQuestVars().getBananaCrateCount());
			    write.writeInt(player.getEctoWorshipCount());
			    try {
				write.writeUTF(player.getQuestVars().getTopHalfFlag());
			    } catch (IOException e) {
				write.writeUTF("undyed");
			    }
			    try {
				write.writeUTF(player.getQuestVars().getBottomHalfFlag());
			    } catch (IOException e) {
				write.writeUTF("undyed");
			    }
			    try {
				write.writeUTF(player.getQuestVars().getSkullFlag());
			    } catch (IOException e) {
				write.writeUTF("undyed");
			    }
			    try {
				write.writeUTF(player.getQuestVars().getDesiredTopHalfFlag());
			    } catch (IOException e) {
				write.writeUTF("black");
			    }
			    try {
				write.writeUTF(player.getQuestVars().getDesiredBottomHalfFlag());
			    } catch (IOException e) {
				write.writeUTF("black");
			    }
			    try {
				write.writeUTF(player.getQuestVars().getDesiredSkullFlag());
			    } catch (IOException e) {
				write.writeUTF("black");
			    }
			    write.writeBoolean(player.getQuestVars().petitionSigned());
			    write.writeInt(player.getGodBook());
			    write.writeBoolean(player.getQuestVars().givenSnailSlime());
			    write.writeBoolean(player.getQuestVars().givenIdPapers());
			    write.writeBoolean(player.getQuestVars().usedFreeGauntletsCharge());
			    write.writeInt(player.getCoalTruckAmount());
			    write.writeInt(player.getDfsCharges());
			write.flush();
			write.close();
			PlayerSave.saveQuests(player);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("here");
		}
		}
	}
	
	public static void saveQuests(Player player) {
		BufferedWriter characterfile = null;
		try {
			characterfile = new BufferedWriter(new FileWriter(directoryOld+player.getUsername()+".txt"));
			
			characterfile.write("quest-points = ", 0, 15);
			characterfile.write(Integer.toString(player.getQuestPoints()), 0, Integer.toString(player.getQuestPoints()).length());
			characterfile.newLine();
			for(Quest q : QuestHandler.getQuests())
			{
				characterfile.write(q.getQuestSaveName() + " = ", 0, q.getQuestSaveName().length()+3);
				try {
					int questStage = player.getQuestStage(q.getQuestID());
					characterfile.write(Integer.toString(questStage), 0, Integer.toString(questStage).length());
				}
				catch(IOException e)
				{
					characterfile.write("0", 0, 1);
				}
				characterfile.newLine();
			}
		/*	characterfile.write("cooks-assistant = ", 0, 18);
			characterfile.write(Integer.toString(player.getQuestStage(0)), 0, Integer.toString(player.getQuestStage(0)).length());
			characterfile.newLine();
			characterfile.write("knights-sword = ", 0, 16);
			characterfile.write(Integer.toString(player.getQuestStage(1)), 0, Integer.toString(player.getQuestStage(1)).length());
			characterfile.newLine();*/
			
			characterfile.write("hide-yell = ", 0, 12);
			characterfile.write(Boolean.toString(player.getHideYell()), 0, Boolean.toString(player.getHideYell()).length());
			characterfile.newLine();
			characterfile.write("hide-colors = ", 0, 14);
			characterfile.write(Boolean.toString(player.getHideColors()), 0, Boolean.toString(player.getHideColors()).length());
			characterfile.newLine();
			characterfile.write("Has GodCape = ", 0, 14);	//cadillac
			characterfile.write(Boolean.toString(player.getCanHaveGodCape()), 0, Boolean.toString(player.getCanHaveGodCape()).length());
			characterfile.newLine();
			characterfile.write("Mage Arena stage = ", 0, 19);
			characterfile.write(Integer.toString(player.getMageArenaStage()), 0, Integer.toString(player.getMageArenaStage()).length());
			characterfile.newLine();
			characterfile.write("PC points = ", 0, 12);
			characterfile.write(Integer.toString(player.getPcPoints()), 0, Integer.toString(player.getPcPoints()).length());
			characterfile.newLine();
			characterfile.write("Latest defender = ", 0, 18);
			characterfile.write(Integer.toString(player.getDefender()), 0, Integer.toString(player.getDefender()).length());
			characterfile.newLine();
			characterfile.write("Ecto Count = ", 0, 13);
			characterfile.write(Integer.toString(player.getEctoWorshipCount()), 0, Integer.toString(player.getEctoWorshipCount()).length());
			characterfile.newLine();
			characterfile.write("MAC = ", 0, 6);
			characterfile.write(player.getMacAddress(), 0, player.getMacAddress().length());
			characterfile.newLine();
			
			characterfile.write("[EOF]", 0, 5);
			characterfile.newLine();
			characterfile.newLine();
			characterfile.close();
			
		} catch(IOException ioexception) {
			System.out.println(player.getUsername()+": error writing file.");
		}
		
	}
	
	@SuppressWarnings("unused")
	public static int loadQuests(Player player) throws IOException {
    	if(!useNewFormat || (useNewFormat && !hasNewFormat(player)))
    	{
		String line = "";
		String token = "";
		String token2 = "";
		String[] token3 = new String[3];
		boolean EndOfFile = false;
		int ReadMode = 0;
		BufferedReader characterfile = null;
		boolean File1 = false;
		
		try {
			characterfile = new BufferedReader(new FileReader(directoryOld+player.getUsername()+".txt"));
			File1 = true;
		} catch(FileNotFoundException fileex1) {
		    return 1;
		}
		
		if (File1) {
		} else {
			System.out.println(player.getUsername()+": unexisting user.");
			characterfile.close();
			return 0;
		}
		
		try {
			line = characterfile.readLine();
		} catch(IOException ioexception) {
			System.out.println(player.getUsername()+": error loading file.");
			characterfile.close();
			return 3;
		}
		if(line.contains("EOF")) {
		    EndOfFile = true;
		}
		while(EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token3 = token2.split("\t");
				switch(token) {
				    case "quest-points":
					player.setQuestPoints(Integer.parseInt(token2));
				    case "hide-yell":
					boolean yellhide = Boolean.parseBoolean(token2);
					if(yellhide)
					{
						player.setHideYell(true,false);
					}else{
						player.setHideYell(false,false);
					}
				    case "hide-colors":
					boolean colorhide = Boolean.parseBoolean(token2);
					if(colorhide)
					{
						player.setHideColors(colorhide,false);
					}else{
						player.setHideColors(false,false);
					}
					
				}
				for(Quest q : QuestHandler.getQuests())
				{
					if(q == null) continue;
					if (token != null && token.equals(q.getQuestSaveName())) 
					{
					    if(q == QuestHandler.getQuests()[7]) {
						player.setQuestStage(7, 0);
					    }
					    else {
						if(token2 != null) {
						    player.setQuestStage(q.getQuestID(), Integer.parseInt(token2));
						  //  q.sendQuestTabStatus(player);
						}
					    }
					}
					else { continue; }
				}
			}
		
		
		try {
			line = characterfile.readLine();
		} catch(IOException ioexception1) { EndOfFile = true; }
		
		}
		try { characterfile.close(); } catch(IOException ioexception) { }
		return 13;
    	}
    	return 0;
	}

    public static void load(Player player) {
    	
    	if(useNewFormat)
    	{
        	if(hasNewFormat(player))
        	{
        		loadJson(player);
        	}else{
        		readFile(player);
        	}
    	}else{
    		readFile(player);
    	}
	}//try now kk

    
	public static void saveAllPlayers() {
        synchronized (World.getPlayers()) {
            final Player[] players = World.getPlayers();
            for (Player p : players) {
                if (p != null && p.getIndex() != -1) {
                    try {
                        PlayerSave.save(p);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
	}
    
    static int readFile(Player player) {
       File file = new File(directoryOld + player.getUsername()
                + ".dat");
        if (!file.exists()) {
            if (Server.getSingleton() != null)
                Server.getSingleton().queueLogin(player);
            return 0;
        }
        try {
            FileInputStream inFile = new FileInputStream(file);
            DataInputStream load = new DataInputStream(inFile);
            player.setUsername(load.readUTF());
            String password = load.readUTF();
            if (!password.equalsIgnoreCase(player.getPassword())) {
            	System.out.println("Wrong login- "+password+"");
            	player.setReturnCode(Constants.LOGIN_RESPONSE_INVALID_CREDENTIALS);
            	load.close();
            	inFile.close();
            	return 3;
            } else {
            	System.out.println("correct login- "+password+"");//try bthat
            }
            
            player.setPassword(password);
            player.setStaffRights(load.readInt());
            player.getPosition().setX(load.readInt());
            player.getPosition().setLastX(player.getPosition().getX());
            player.getPosition().setY(load.readInt());
            player.getPosition().setLastY(player.getPosition().getY() + 1);
            player.getPosition().setZ(load.readInt());
            player.setGender(load.readInt());
            player.setAutoRetaliate(load.readBoolean());
            player.setFightMode(load.readInt());
            player.setScreenBrightness(load.readInt());
            player.setMouseButtons(load.readInt());
            player.setChatEffects(load.readInt());
            player.setSplitPrivateChat(load.readInt());
            player.setAcceptAid(load.readInt());
            player.setMusicVolume(load.readInt());
            player.setEffectVolume(load.readInt());
            player.setQuestPoints(load.readInt());
            player.setSpecialAmount((int) load.readDouble());
            player.getBankPin().setChangingBankPin(
                    load.readBoolean());
            player.getBankPin().setDeletingBankPin(
                    load.readBoolean());
            player.getBankPin()
                    .setPinAppendYear(load.readInt());
            player.getBankPin().setPinAppendDay(load.readInt());
            player.setBindingNeckCharge(load.readInt());
            player.setRingOfForgingLife(load.readInt());
            player.setRingOfRecoilLife(load.readInt());
        	int skullTimer = load.readInt();
        	if (skullTimer > 0)
        		player.addSkull(player, skullTimer);
            player.getNewComersSide().setTutorialIslandStage(
                    load.readInt(), false);
            player.getNewComersSide().setProgressValue(
                    load.readInt());
            player.setEnergy(load.readDouble());
            player.getMovementHandler().setRunToggled(load.readBoolean());
            for (int i = 0; i < player.getBankPin().getBankPin().length; i++) {
                player.getBankPin().getBankPin()[i] = load.readInt();
            }
            for (int i = 0; i < player.getBankPin()
                    .getPendingBankPin().length; i++) {
                player.getBankPin().getPendingBankPin()[i] = load
                        .readInt();
            }
            for (int i = 0; i < player.questData.length; i++) {
                player.questData[i][1] = load
                        .readInt();
            }
            for (int i = 0; i < 4; i++) {
                player.setPouchData(i, load.readInt());
            }
            for (int i = 0; i < player.getAppearance().length; i++) {
                player.getAppearance()[i] = load.readInt();
            }
            for (int i = 0; i < player.getColors().length; i++) {
                player.getColors()[i] = load.readInt();
            }
            AppearancePacketHandler.checkOutfitRanges(player);
            for (int i = 0; i < player.getSkill().getLevel().length; i++) {
                player.getSkill().getLevel()[i] = load
                        .readInt();
            }
            for (int i = 0; i < player.getSkill().getExp().length; i++) {
                player.getSkill().getExp()[i] = load.readInt();
            }
            for (int i = 0; i < 28; i++) {
                int id = load.readInt();
                if (id != 65535) {
                    int amount = load.readInt();
                    int timer = load.readInt();
                    if (id < Constants.MAX_ITEMS && amount > 0)  {
                        Item item = new Item(id, amount, timer);
                        if (item.getId() == 2696 || item.getId() == 2699 || item.getId() == 3510) {
                        	item = new Item(id - 1, amount, timer);
                        }
                        player.getInventory().getItemContainer().set(i, item);
                    }
                }
            }
            for (int i = 0; i < 14; i++) {
                int id = load.readInt();
                if (id != 65535) {
                    int amount = load.readInt();
                    if (id < Constants.MAX_ITEMS && amount > 0)  {
                        Item item = new Item(id, amount);
                        player.getEquipment().getItemContainer().set(i, item);
                    }
                }
            }
            try {
                for (int i = 0; i < BankManager.SIZE; i++) {
                    int id = load.readInt();
                    if (id != 65535) {
                        int amount = load.readInt();
                        int timer = load.readInt();
                        if (id < Constants.MAX_ITEMS && amount > 0)  {
                            Item item = new Item(id, amount, timer);
                            if (item.getId() == 2696 || item.getId() == 2699 || item.getId() == 3510) {
                            	item = new Item(id - 1, amount, timer);
                            }
                           // player.getBank().set(i, item);
                            player.getBankManager().tabContainer(0).set(i, item);
                        }
                    }
                }
	            for (int i = 0; i < player.getFriends().length; i++) {
					long friendLong = load.readLong();
					if(friendLong < 0L || friendLong >= 0x7dcff8986ea31000L)
						continue;
					if(!player.getFriendsConverted()){
						friendLong = NameUtil.nameToLong(NameUtil.longToNameOld(friendLong));
					}
	                player.getFriends()[i] = friendLong;
	            }
	            player.setFriendsConverted(true);
	            for (int i = 0; i < player.getIgnores().length; i++) {
					long ignoreLong = load.readLong();
					if(ignoreLong < 0L || ignoreLong >= 0x7dcff8986ea31000L)
						continue;
					if(!player.getIgnoresConverted()){
						ignoreLong = NameUtil.nameToLong(NameUtil.longToNameOld(ignoreLong));
					}
	                player.getIgnores()[i] = ignoreLong;
	            }
	            player.setIgnoresConverted(true);
	            for (int i = 0; i < player.getPendingItems().length; i++) {
	                player.getPendingItems()[i] = load.readInt();
	                player.getPendingItemsAmount()[i] = load.readInt();
	            }
                player.setRunecraftNpc(load.readInt());
                player.setMuteExpire(load.readLong());
                player.setBanExpire(load.readLong());
                player.setKilledTreeSpirit(load.readBoolean());
                player.setKilledJungleDemon(load.readBoolean());
                player.setResetBank(load.readBoolean());
                for (int i = 0; i < 6; i++) {
                	player.setBarrowsNpcDead(i, load.readBoolean());
                }
                player.setKillCount(load.readInt());
                player.setRandomGrave(load.readInt());
                player.getPoisonImmunity().setWaitDuration(load.readInt());
                player.getPoisonImmunity().reset();
                player.getFireImmunity().setWaitDuration(load.readInt());
                player.getFireImmunity().reset();
                player.getTeleblockTimer().setWaitDuration(load.readInt());
                player.getTeleblockTimer().reset();
            	double poison = load.readDouble();
                if (poison > 0) {
                	HitDef hitDef = new HitDef(null, HitType.POISON, Math.ceil(poison)).setStartingHitDelay(30);
    				Hit hit = new Hit(player, player, hitDef);
    				PoisonEffect p = new PoisonEffect(poison, false);
    				p.initialize(hit);
                }
    			for (int i = 0; i < player.getAllotment().getFarmingStages().length; i++) {
    				player.getAllotment().setFarmingStages(i, load.readInt());
    			}
    			for (int i = 0; i < player.getAllotment().getFarmingSeeds().length; i++) {
    				player.getAllotment().setFarmingSeeds(i, load.readInt());
    			}
    			for (int i = 0; i < player.getAllotment().getFarmingHarvest().length; i++) {
    				player.getAllotment().setFarmingHarvest(i, load.readInt());
    			}
    			for (int i = 0; i < player.getAllotment().getFarmingState().length; i++) {
    				player.getAllotment().setFarmingState(i, load.readInt());
    			}
    			for (int i = 0; i < player.getAllotment().getFarmingTimer().length; i++) {
    				player.getAllotment().setFarmingTimer(i, load.readLong());
    			}
    			for (int i = 0; i < player.getAllotment().getDiseaseChance().length; i++) {
    				player.getAllotment().setDiseaseChance(i, load.readDouble());
    			}
    			for (int i = 0; i < player.getAllotment().getFarmingWatched().length; i++) {
    				player.getAllotment().setFarmingWatched(i, load.readBoolean());
    			}
    			for (int i = 0; i < player.getBushes().getFarmingStages().length; i++) {
    				player.getBushes().setFarmingStages(i, load.readInt());
    			}
    			for (int i = 0; i < player.getBushes().getFarmingSeeds().length; i++) {
    				player.getBushes().setFarmingSeeds(i, load.readInt());
    			}
    			for (int i = 0; i < player.getBushes().getFarmingState().length; i++) {
    				player.getBushes().setFarmingState(i, load.readInt());
    			}
    			for (int i = 0; i < player.getBushes().getFarmingTimer().length; i++) {
    				player.getBushes().setFarmingTimer(i, load.readLong());
    			}
    			for (int i = 0; i < player.getBushes().getDiseaseChance().length; i++) {
    				player.getBushes().setDiseaseChance(i, load.readDouble());
    			}
    			for (int i = 0; i < player.getBushes().getFarmingWatched().length; i++) {
    				player.getBushes().setFarmingWatched(i, load.readBoolean());
    			}
    			for (int i = 0; i < player.getFlowers().getFarmingStages().length; i++) {
    				player.getFlowers().setFarmingStages(i, load.readInt());
    			}
    			for (int i = 0; i < player.getFlowers().getFarmingSeeds().length; i++) {
    				player.getFlowers().setFarmingSeeds(i, load.readInt());
    			}
    			for (int i = 0; i < player.getFlowers().getFarmingState().length; i++) {
    				player.getFlowers().setFarmingState(i, load.readInt());
    			}
    			for (int i = 0; i < player.getFlowers().getFarmingTimer().length; i++) {
    				player.getFlowers().setFarmingTimer(i, load.readLong());
    			}
    			for (int i = 0; i < player.getFlowers().getDiseaseChance().length; i++) {
    				player.getFlowers().setDiseaseChance(i, load.readDouble());
    			}
    			for (int i = 0; i < player.getFruitTrees().getFarmingStages().length; i++) {
    				player.getFruitTrees().setFarmingStages(i, load.readInt());
    			}
    			for (int i = 0; i < player.getFruitTrees().getFarmingSeeds().length; i++) {
    				player.getFruitTrees().setFarmingSeeds(i, load.readInt());
    			}
    			for (int i = 0; i < player.getFruitTrees().getFarmingState().length; i++) {
    				player.getFruitTrees().setFarmingState(i, load.readInt());
    			}
    			for (int i = 0; i < player.getFruitTrees().getFarmingTimer().length; i++) {
    				player.getFruitTrees().setFarmingTimer(i, load.readLong());
    			}
    			for (int i = 0; i < player.getFruitTrees().getDiseaseChance().length; i++) {
    				player.getFruitTrees().setDiseaseChance(i, load.readDouble());
    			}
    			for (int i = 0; i < player.getFruitTrees().getFarmingWatched().length; i++) {
    				player.getFruitTrees().setFarmingWatched(i, load.readBoolean());
    			}
    			for (int i = 0; i < player.getHerbs().getFarmingStages().length; i++) {
    				player.getHerbs().setFarmingStages(i, load.readInt());
    			}
    			for (int i = 0; i < player.getHerbs().getFarmingSeeds().length; i++) {
    				player.getHerbs().setFarmingSeeds(i, load.readInt());
    			}
    			for (int i = 0; i < player.getHerbs().getFarmingHarvest().length; i++) {
    				player.getHerbs().setFarmingHarvest(i, load.readInt());
    			}
    			for (int i = 0; i < player.getHerbs().getFarmingState().length; i++) {
    				player.getHerbs().setFarmingState(i, load.readInt());
    			}
    			for (int i = 0; i < player.getHerbs().getFarmingTimer().length; i++) {
    				player.getHerbs().setFarmingTimer(i, load.readLong());
    			}
    			for (int i = 0; i < player.getHerbs().getDiseaseChance().length; i++) {
    				player.getHerbs().setDiseaseChance(i, load.readDouble());
    			}
    			for (int i = 0; i < player.getHops().getFarmingStages().length; i++) {
    				player.getHops().setFarmingStages(i, load.readInt());
    			}
    			for (int i = 0; i < player.getHops().getFarmingSeeds().length; i++) {
    				player.getHops().setFarmingSeeds(i, load.readInt());
    			}
    			for (int i = 0; i < player.getHops().getFarmingHarvest().length; i++) {
    				player.getHops().setFarmingHarvest(i, load.readInt());
    			}
    			for (int i = 0; i < player.getHops().getFarmingState().length; i++) {
    				player.getHops().setFarmingState(i, load.readInt());
    			}
    			for (int i = 0; i < player.getHops().getFarmingTimer().length; i++) {
    				player.getHops().setFarmingTimer(i, load.readLong());
    			}
    			for (int i = 0; i < player.getHops().getDiseaseChance().length; i++) {
    				player.getHops().setDiseaseChance(i, load.readDouble());
    			}
    			for (int i = 0; i < player.getHops().getFarmingWatched().length; i++) {
    				player.getHops().setFarmingWatched(i, load.readBoolean());
    			}
    			for (int i = 0; i < player.getSpecialPlantOne().getFarmingStages().length; i++) {
    				player.getSpecialPlantOne().setFarmingStages(i, load.readInt());
    			}
    			for (int i = 0; i < player.getSpecialPlantOne().getFarmingSeeds().length; i++) {
    				player.getSpecialPlantOne().setFarmingSeeds(i, load.readInt());
    			}
    			for (int i = 0; i < player.getSpecialPlantOne().getFarmingState().length; i++) {
    				player.getSpecialPlantOne().setFarmingState(i, load.readInt());
    			}
    			for (int i = 0; i < player.getSpecialPlantOne().getFarmingTimer().length; i++) {
    				player.getSpecialPlantOne().setFarmingTimer(i, load.readLong());
    			}
    			for (int i = 0; i < player.getSpecialPlantOne().getDiseaseChance().length; i++) {
    				player.getSpecialPlantOne().setDiseaseChance(i, load.readDouble());
    			}
    			for (int i = 0; i < player.getSpecialPlantTwo().getFarmingStages().length; i++) {
    				player.getSpecialPlantTwo().setFarmingStages(i, load.readInt());
    			}
    			for (int i = 0; i < player.getSpecialPlantTwo().getFarmingSeeds().length; i++) {
    				player.getSpecialPlantTwo().setFarmingSeeds(i, load.readInt());
    			}
    			for (int i = 0; i < player.getSpecialPlantTwo().getFarmingState().length; i++) {
    				player.getSpecialPlantTwo().setFarmingState(i, load.readInt());
    			}
    			for (int i = 0; i < player.getSpecialPlantTwo().getFarmingTimer().length; i++) {
    				player.getSpecialPlantTwo().setFarmingTimer(i, load.readLong());
    			}
    			for (int i = 0; i < player.getSpecialPlantTwo().getDiseaseChance().length; i++) {
    				player.getSpecialPlantTwo().setDiseaseChance(i, load.readDouble());
    			}
    			for (int i = 0; i < player.getTrees().getFarmingStages().length; i++) {
    				player.getTrees().setFarmingStages(i, load.readInt());
    			}
    			for (int i = 0; i < player.getTrees().getFarmingSeeds().length; i++) {
    				player.getTrees().setFarmingSeeds(i, load.readInt());
    			}
    			for (int i = 0; i < player.getTrees().getFarmingHarvest().length; i++) {
    				player.getTrees().setFarmingHarvest(i, load.readInt());
    			}
    			for (int i = 0; i < player.getTrees().getFarmingState().length; i++) {
    				player.getTrees().setFarmingState(i, load.readInt());
    			}
    			for (int i = 0; i < player.getTrees().getFarmingTimer().length; i++) {
    				player.getTrees().setFarmingTimer(i, load.readLong());
    			}
    			for (int i = 0; i < player.getTrees().getDiseaseChance().length; i++) {
    				player.getTrees().setDiseaseChance(i, load.readDouble());
    			}
    			for (int i = 0; i < player.getTrees().getFarmingWatched().length; i++) {
    				player.getTrees().setFarmingWatched(i, load.readBoolean());
    			}
    			for (int i = 0; i < player.getCompost().getCompostBins().length; i++) {
    				player.getCompost().setCompostBins(i, load.readInt());
    			}
    			for (int i = 0; i < player.getCompost().getCompostBinsTimer().length; i++) {
    				player.getCompost().setCompostBinsTimer(i, load.readLong());
    			}
    			for (int i = 0; i < player.getCompost().getOrganicItemAdded().length; i++) {
    				player.getCompost().setOrganicItemAdded(i, load.readInt());
    			}
    			for (int i = 0; i < player.getFarmingTools().getTools().length; i++) {
    				player.getFarmingTools().setTools(i, load.readInt());
    			}
			for (int i = 0; i < player.getDegradeableHits().length; i++) {
			    try {
				load.readInt();
				player.setDegradeableHits(i, 0);
			    } catch (Exception e) {
			    }
			}
			/*for (int i = 0; i < player.getBonesGround().length; i++) {
			    try {
    				player.setBonesGround(i, load.readInt());
			    }
			    catch (IOException e) {
				player.setBonesGround(i, 0);
			    }
    			}*/
            } catch (IOException e) {
            }
            try {
            	player.getSlayer().slayerMaster = load.readInt();
            } catch (IOException e) { System.out.println("here1");
            	player.getSlayer().resetSlayerTask();
            }
            try {
            	player.getSlayer().slayerTask = load.readUTF();
            	player.getSlayer().taskAmount = load.readInt();
            } catch (IOException e) { System.out.println("here2");
            }
            try {
            	boolean ancient = load.readBoolean();
            	player.setMagicBookType(ancient ? SpellBook.ANCIENT : SpellBook.MODERN);
            } catch (IOException e) { System.out.println("here3");
            }
            try {
            	player.setBrimhavenDungeonOpen(load.readBoolean());
            } catch (IOException e) { System.out.println("here4");
            }
            try {
            	player.setKilledClueAttacker(load.readBoolean());
            } catch (IOException e) { System.out.println("here5");
            }
            try {
		    player.setClayBraceletLife(load.readInt());
            } catch (IOException e) { System.out.println("here6");
		player.setClayBraceletLife(0);
            }
            try {
		    player.setPcPoints(load.readInt(), player);
            } catch (IOException e) { System.out.println("here7");
		player.setPcPoints(0, player);
	    }
	    try {
            	player.saveZamorakCasts(load.readInt());
            } catch (IOException e) { System.out.println("here8");
		player.saveZamorakCasts(0);
            }
	    try {
            	player.saveSaradominCasts(load.readInt());
            } catch (IOException e) { System.out.println("here9");
		player.saveSaradominCasts(0);
            }
	    try {
            	player.saveGuthixCasts(load.readInt());
            } catch (IOException e) { System.out.println("here10");
		player.saveGuthixCasts(0);
            }
	    try {
            	player.setMageArenaStage(load.readInt());
            } catch (IOException e) { System.out.println("here11");
		player.setMageArenaStage(0);
            }
	    try {
            	player.setDefender(load.readInt());
            } catch (IOException e) { System.out.println("here12");
		WarriorsGuild.findDefender(player);
            }
	    try {
            	player.getQuestVars().joinPhoenixGang(load.readBoolean());
            } catch (IOException e) { System.out.println("here13");
            }
	    try {
            	player.getQuestVars().joinBlackArmGang(load.readBoolean());
            } catch (IOException e) { System.out.println("here14");
            }
	    try {
            	player.getQuestVars().setMelzarsDoorUnlock(load.readBoolean());
            } catch (IOException e) { System.out.println("here15");
            }
	    try {
            	player.setFightCavesWave(load.readInt());
            } catch (IOException e) { System.out.println("here16");
		player.setFightCavesWave(0);
            }
	    try {
            	player.getQuestVars().setBananaCrate(load.readBoolean());
            } catch (IOException e) { System.out.println("here17");
            }
	    try {
		    player.getQuestVars().setBananaCrateCount(load.readInt());
            } catch (IOException e) { System.out.println("here18");
		player.getQuestVars().setBananaCrateCount(0);
            }
	    try {
		player.setEctoWorshipCount(load.readInt());
            } catch (IOException e) { System.out.println("here19");
		player.setEctoWorshipCount(0);
            }
	    try {
		player.getQuestVars().dyeGhostsAhoyFlag("top", load.readUTF());
            } catch (IOException e) { System.out.println("20");
		
		player.getQuestVars().dyeGhostsAhoyFlag("top", "undyed");
            }
	    try {
		    player.getQuestVars().dyeGhostsAhoyFlag("bottom", load.readUTF());
            } catch (IOException e) { System.out.println("21"); 
		player.getQuestVars().dyeGhostsAhoyFlag("bottom", "undyed");
            }
	    try {
		    player.getQuestVars().dyeGhostsAhoyFlag("skull", load.readUTF());
            } catch (IOException e) { System.out.println("22"); 
		player.getQuestVars().dyeGhostsAhoyFlag("skull", "undyed");
            }
	    try {
		    player.getQuestVars().setDesiredGhostsAhoyFlag("top", load.readUTF());
            } catch (IOException e) { System.out.println("23");
		player.getQuestVars().setDesiredGhostsAhoyFlag("top", "black");
            }
	    try {
		    player.getQuestVars().setDesiredGhostsAhoyFlag("bottom", load.readUTF());
            } catch (IOException e) { System.out.println("24");
		player.getQuestVars().setDesiredGhostsAhoyFlag("bottom", "black");
            }
	    try {
		    player.getQuestVars().setDesiredGhostsAhoyFlag("top", load.readUTF());
            } catch (IOException e) { System.out.println("25");
		player.getQuestVars().setDesiredGhostsAhoyFlag("skull", "black");
            }
	    try {
            	player.getQuestVars().setPetitionSigned(load.readBoolean());
            } catch (IOException e) { System.out.println("26");
		player.getQuestVars().setPetitionSigned(false);
            }
	    try {
            	player.setGodBook(load.readInt());
            } catch (IOException e) { System.out.println("27");
		player.setGodBook(0);
            }
	    try {
            	player.getQuestVars().setGivenSnailSlime(load.readBoolean());
            } catch (IOException e) { System.out.println("28");
		player.getQuestVars().setGivenSnailSlime(false);
            }
	    try {
            	player.getQuestVars().setGivenIdPapers(load.readBoolean());
            } catch (IOException e) { System.out.println("29");
		player.getQuestVars().setGivenIdPapers(false);
            }
	    try {
            	player.getQuestVars().setHasUsedFreeGauntletsCharge(load.readBoolean());
            } catch (IOException e) { System.out.println("30");
		player.getQuestVars().setHasUsedFreeGauntletsCharge(false);
            }
	    try {
            	player.setCoalTruckAmount(load.readInt());
            } catch (IOException e) { System.out.println("31");
		player.setCoalTruckAmount(0);
            }
	    try {
            	player.setDfsCharges(load.readInt());
            } catch (IOException e) { System.out.println("32");
		player.setDfsCharges(0);
            }
            load.close();
            if (Server.getSingleton() != null)
                Server.getSingleton().queueLogin(player);
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
        	System.out.println("Account not loading: " + player);
            //corrupted save file
            if (Server.getSingleton() != null)
                Server.getSingleton().queueLogin(player);
            return 0;
        }
    }

    public static class ConnectionAttempt {
        private static final long TIMEOUT = 5 * 60 * 1000;
        private static final int MAX_ATTEMPTS = 7;
        Misc.Stopwatch timer;
        int attempts;

        public ConnectionAttempt() {
            timer = new Misc.Stopwatch();
        }

        public void addAttempt() {
            if (attempts < MAX_ATTEMPTS)
                attempts += 1;
            else timer.reset();
        }
        
        public void reset() {
            attempts = 0;
        }

        public boolean canConnect() {
            return attempts != MAX_ATTEMPTS || timer.elapsed() >= TIMEOUT;
        }
    }
}
