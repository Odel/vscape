package com.rs2.util.sql;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.zip.GZIPInputStream;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rs2.Constants;
//import java.security.MessageDigest;
import com.rs2.model.players.Player;
import com.rs2.util.PlayerSave;


public class SQL {


	public static Connection con;
	public static Statement stmt;
	int affected = 0;
	public static boolean connectionMade;
	
	public static void createConnection() {
		if(Constants.SQL_TYPE == 1)
		{
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				con = DriverManager.getConnection(Constants.GAME_DB_URL,Constants.GAME_DB_USER,Constants.GAME_DB_PASS);
				stmt = con.createStatement();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(Constants.SQL_TYPE == 2)
		{
			try {
				Class.forName("org.sqlite.JDBC").newInstance();
			    con = DriverManager.getConnection("jdbc:sqlite:test.db");
			    stmt = con.createStatement();
			    query("create table if not exists skillsoverall (username CHAR(50), totalxp INT, totallevel INT )");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		if(con == null)
		{
			System.out.println("Something went wrong setting up the SQL connection");
		}
		else
		{
			if(Constants.SQL_TYPE == 1)
			{
				System.out.println("Opened MySql database successfully");
			}
			else if(Constants.SQL_TYPE == 2)
			{
				System.out.println("Opened SQLite database successfully");
			}
		}
	}
	public static ResultSet query(String s) throws SQLException {
		try {
			if (s.toLowerCase().startsWith("select")) {
				ResultSet rs = stmt.executeQuery(s);
				return rs;
			} else {
				stmt.executeUpdate(s);
			}
			return null;
		} catch (Exception e) {
			destroyConnection();
			createConnection();
			e.printStackTrace();
		}
		return null;
	}

	public static void destroyConnection() {
		try {
			stmt.close();
			con.close();
			connectionMade = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean saveHighScore(Player player) {
		try {
			//query("DELETE FROM `skills` WHERE playerName = '"+player.getUsername()+"';");
			query("DELETE FROM `skillsoverall` WHERE username = '"+player.getUsername()+"';");
			//query("INSERT INTO `skills` (`playerName`,`Attacklvl`,`Attackxp`,`Defencelvl`,`Defencexp`,`Strengthlvl`,`Strengthxp`,`Hitpointslvl`,`Hitpointsxp`,`Rangelvl`,`Rangexp`,`Prayerlvl`,`Prayerxp`,`Magiclvl`,`Magicxp`,`Cookinglvl`,`Cookingxp`,`Woodcuttinglvl`,`Woodcuttingxp`,`Fletchinglvl`,`Fletchingxp`,`Fishinglvl`,`Fishingxp`,`Firemakinglvl`,`Firemakingxp`,`Craftinglvl`,`Craftingxp`,`Smithinglvl`,`Smithingxp`,`Mininglvl`,`Miningxp`,`Herblorelvl`,`Herblorexp`,`Agilitylvl`,`Agilityxp`,`Thievinglvl`,`Thievingxp`,`Slayerlvl`,`Slayerxp`,`Farminglvl`,`Farmingxp`,`Runecraftlvl`,`Runecraftxp`) VALUES ('"+player.getUsername()+"',"+player.getSkill().getLevel()[Skill.ATTACK]+","+player.getSkill().getExp()[Skill.ATTACK]+","+player.getSkill().getLevel()[Skill.DEFENCE]+","+player.getSkill().getExp()[Skill.DEFENCE]+","+player.getSkill().getLevel()[Skill.STRENGTH]+","+player.getSkill().getExp()[Skill.STRENGTH]+","+player.getSkill().getLevel()[Skill.HITPOINTS]+","+player.getSkill().getExp()[Skill.HITPOINTS]+","+player.getSkill().getLevel()[Skill.RANGED]+","+player.getSkill().getExp()[Skill.RANGED]+","+player.getSkill().getLevel()[Skill.PRAYER]+","+player.getSkill().getExp()[Skill.PRAYER]+","+player.getSkill().getLevel()[Skill.MAGIC]+","+player.getSkill().getExp()[Skill.MAGIC]+","+player.getSkill().getLevel()[Skill.COOKING]+","+player.getSkill().getExp()[Skill.COOKING]+","+player.getSkill().getLevel()[Skill.WOODCUTTING]+","+player.getSkill().getExp()[Skill.WOODCUTTING]+","+player.getSkill().getLevel()[Skill.FLETCHING]+","+player.getSkill().getExp()[Skill.FLETCHING]+","+player.getSkill().getLevel()[Skill.FISHING]+","+player.getSkill().getExp()[Skill.FISHING]+","+player.getSkill().getLevel()[Skill.FIREMAKING]+","+player.getSkill().getExp()[Skill.FIREMAKING]+","+player.getSkill().getLevel()[Skill.CRAFTING]+","+player.getSkill().getExp()[Skill.CRAFTING]+","+player.getSkill().getLevel()[Skill.SMITHING]+","+player.getSkill().getExp()[Skill.SMITHING]+","+player.getSkill().getLevel()[Skill.MINING]+","+player.getSkill().getExp()[Skill.MINING]+","+player.getSkill().getLevel()[Skill.HERBLORE]+","+player.getSkill().getExp()[Skill.HERBLORE]+","+player.getSkill().getLevel()[Skill.AGILITY]+","+player.getSkill().getExp()[Skill.AGILITY]+","+player.getSkill().getLevel()[Skill.THIEVING]+","+player.getSkill().getExp()[Skill.THIEVING]+","+player.getSkill().getLevel()[Skill.SLAYER]+","+player.getSkill().getExp()[Skill.SLAYER]+","+player.getSkill().getLevel()[Skill.FARMING]+","+player.getSkill().getExp()[Skill.FARMING]+","+player.getSkill().getLevel()[Skill.RUNECRAFTING]+","+player.getSkill().getExp()[Skill.RUNECRAFTING]+");");
			query("INSERT INTO `skillsoverall` (`username`,`totalxp`,`totallevel`) VALUES ('"+player.getUsername()+"',"+(player.getSkill().getTotalXp())+","+(player.getSkill().getTotalLevel())+");");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void cleanHighScores(){
		System.out.println("Erasing highscore entries");
		try {
			query("TRUNCATE TABLE `skillsoverall`");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void initHighScores(){
		createConnection();
		System.out.println("Updating all highscore entries from save files, this may take a while...");
		int count = 0;
		try {
			File folder = new File(PlayerSave.directoryOld);
			if(PlayerSave.useNewFormat){
				folder = new File(PlayerSave.directoryNew);
			}else{
				folder = new File(PlayerSave.directoryOld);
			}
			File[] listOfFiles = folder.listFiles();
			for (File file : listOfFiles) {
				if(PlayerSave.useNewFormat){
					if (file.getName().endsWith(".gz")) {
				        try(GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(file))){
					        JsonElement mainElement = new JsonParser().parse(new BufferedReader(new InputStreamReader(gzip)));
					        gzip.close();
					        JsonObject mainObj = mainElement.getAsJsonObject();
					        JsonObject characterObj = mainObj.getAsJsonObject("character");
				            if(mainElement == null || mainObj == null || characterObj == null){
				            	continue;
				            }
				            JsonArray skills = mainObj.getAsJsonArray("skills");
				            if(skills == null || skills.size() == 0)
				            {
				            	continue;
				            }
				            int leveltotal = 0;
				            int xptotal = 0;
				            if(skills != null && skills.size() > 0){
					    		for (int i = 0; i < 22; i++) {
					    			JsonObject levelObj = skills.get(i).getAsJsonObject();
					    			leveltotal += levelObj.get("lvl").getAsInt();
					    			xptotal += levelObj.get("xp").getAsInt();
					    		}
				            }
				    		try {
				    			String sname = file.getName();
				    			sname = sname.substring(0, sname.lastIndexOf('.'));
				    			
				    			if(Constants.SQL_TYPE == 2)
				    			{
				    				PreparedStatement del;
				    				del = con.prepareStatement("DELETE FROM `skillsoverall` WHERE username = ?;");
				    				del.setString(1, sname);
				    				del.executeUpdate();
				    			}
				    			PreparedStatement upd = con.prepareStatement("INSERT INTO `skillsoverall` (`username`,`totallevel`,`totalxp`) VALUES (?,?,?);");
				    			upd.setString(1, sname);
				    			upd.setInt(2, leveltotal);
				    			upd.setInt(3, xptotal);
				    			upd.executeUpdate();				    			
				    		} catch (Exception e) {
				    			e.printStackTrace();
				    		}
				        }
					}
				}else{
					if (file.getName().endsWith(".dat")) {
						count++;
						//System.out.println(file.getName());
						File openplayerfile = new File(folder + "/" + file.getName());
			            FileInputStream inFile = new FileInputStream(openplayerfile);
			            DataInputStream load = new DataInputStream(inFile);
			            
			            String name = load.readUTF();
			            String s = load.readUTF();
			            int i = load.readInt();
			            i = load.readInt();
			            i = load.readInt();
			            i = load.readInt();
			            i = load.readInt();
			            boolean b = load.readBoolean();
			            i = load.readInt();
			            i = load.readInt();
			            i = load.readInt();
			            i = load.readInt();
			            i = load.readInt();
			            i = load.readInt();
			            i = load.readInt();
			            i = load.readInt();
			            i = load.readInt();
			            i = (int) load.readDouble();
			            b = load.readBoolean();
			            b = load.readBoolean();
			            i = load.readInt();
			            i = load.readInt();
			            i = load.readInt();
			            i = load.readInt();
			            i = load.readInt();
			            i = load.readInt();
			            i = load.readInt();
			            i = load.readInt();
			            i = (int) load.readDouble();
			            b = load.readBoolean();
			            String stuff = "";
			            for (int k = 0; k < 25; k++) {
				            i = load.readInt();
				            stuff += "," + i;
			            }
			            int[] levels = new int[22];
			            int leveltotal = 0;
			            for (int k = 0; k <  22; k++) {
			                levels[k] = load.readInt();
			                leveltotal += levels[k];
			            }
			            
			            //System.out.println(name + ":" + stuff + ":" + levelstring + ":" + b);
			            int[] xp = new int[22];
			            int xptotal = 0;
			            for (int k = 0; k < 22; k++) {
			                xp[k] = load.readInt();
			                xptotal += xp[k];
			            }
			            
			            load.close();
			            if(count%100 == 0)
			            {
			            	System.out.println(count + " entries made");
			            }
			    		try {
			    			String sname = file.getName();
			    			sname = sname.substring(0, sname.lastIndexOf('.'));
			    			//System.out.println(sname);
			    			//query("DELETE FROM `skillsoverall` WHERE playerName = '"+sname+"';");
			    			//query("INSERT INTO `skillsoverall` (`playerName`,`xp`,`lvl`) VALUES ('"+sname+"',"+(xptotal)+","+(leveltotal)+");");
			    			if(Constants.SQL_TYPE == 2)
			    			{
			    				PreparedStatement del;
			    				del = con.prepareStatement("DELETE FROM `skillsoverall` WHERE username = ?;");
			    				del.setString(1, sname);
			    				del.executeUpdate();
			    			}
			    			
			    			PreparedStatement upd = con.prepareStatement("INSERT INTO `skillsoverall` (`username`,`totallevel`,`totalxp`) VALUES (?,?,?);");
			    			upd.setString(1, sname);
			    			upd.setInt(2, leveltotal);
			    			upd.setInt(3, xptotal);
			    			upd.executeUpdate();
			    			
			    			
			    		} catch (Exception e) {
			    			e.printStackTrace();
			    		}
	                } 
				}
			}
		}catch(Exception e) {
		}
			
	}
}