package com.rs2.util.sql;

import java.io.BufferedReader;
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
import com.rs2.model.content.skills.Skill;
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
				stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
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
	
	private static boolean ignoreUser(String name){
		if(name.equals("Stannis") || name.equals("Lord dio") || name.equals("Quietessdick") || name.equals("Bobsterdebug") || name.equals("Mod dammit") || name.equals("Noiryx") || name.equals("Pickles") || name.equals("Mrsmeg") || name.equals("Mr telescope") || name.equals("Shark") || name.equals("Mr foxter") || name.equals("Mr_foxter"))
		{
			return true;
		}
		return false;
	}
	
	public static boolean saveHighScore(Player player) {
		if(ignoreUser(player.getUsername()))
		{
			return false;
		}
		try {
			ResultSet rs = query("SELECT * FROM highscores WHERE username='"+player.getUsername()+"' LIMIT 1");
			if (!rs.next()) {
				rs.moveToInsertRow(); // Moves cursor to insert row.
				rs.updateString("username", player.getUsername());
				rs.updateInt("rights", player.getStaffRights());
				rs.updateLong("overall_xp", player.getSkill().getTotalXp());
				for (int i = 0; i < Skill.SKILL_NAME.length; i++) {
					rs.updateInt(""+Skill.SKILL_NAME[i].toLowerCase()+"_xp", (int)player.getSkill().getExp()[i]);
				}
				rs.insertRow(); // inserts the new row
			} else {
				rs.updateString("username", player.getUsername());
				rs.updateInt("rights", player.getStaffRights());
				rs.updateLong("overall_xp", player.getSkill().getTotalXp());
				for (int i = 0; i < Skill.SKILL_NAME.length; i++) {
					rs.updateInt(""+Skill.SKILL_NAME[i].toLowerCase()+"_xp", (int)player.getSkill().getExp()[i]);
				}
				rs.updateRow(); // updates an existing row
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void cleanHighScores(){
		System.out.println("Erasing highscore entries");
		try {
			query("TRUNCATE TABLE `highscores`");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void initHighScores(){
		createConnection();
		System.out.println("Updating all highscore entries from save files, this may take a while...");
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
				         //   int leveltotal = 0;
				            long xptotal = 0;
				            if(skills != null && skills.size() > 0){
					    		for (int i = 0; i < 22; i++) {
					    			JsonObject levelObj = skills.get(i).getAsJsonObject();
					    	//		leveltotal += levelObj.get("lvl").getAsInt();
					    			xptotal += levelObj.get("xp").getAsInt();
					    		}
				            }
				    		try {
				    			String sname = file.getName();
				    			sname = sname.substring(0, sname.lastIndexOf('.'));
				    			if(ignoreUser(sname))
				    			{
				    				continue;
				    			}
				    			if(Constants.SQL_TYPE == 2)
				    			{
				    				PreparedStatement del;
				    				del = con.prepareStatement("DELETE FROM `highscores` WHERE username = ?;");
				    				del.setString(1, sname);
				    				del.executeUpdate();
				    			}
				    			ResultSet rs = query("SELECT * FROM highscores WHERE username='"+sname+"' LIMIT 1");
				    			if (!rs.next()) {
				    				rs.moveToInsertRow(); // Moves cursor to insert row.
				    				rs.updateString("username", sname);
				    				rs.updateInt("rights", characterObj.get("rights").getAsInt());
				    				rs.updateLong("overall_xp", xptotal);
				    				for (int i = 0; i < skills.size()-1; i++) {
						    			JsonObject levelObj = skills.get(i).getAsJsonObject();
						    			int exp = levelObj.get("xp").getAsInt();
				    					rs.updateInt(""+Skill.SKILL_NAME[i].toLowerCase()+"_xp", exp);
				    				}
				    				rs.insertRow(); // inserts the new row
				    			} else {
				    				rs.updateString("username", sname);
				    				rs.updateInt("rights", characterObj.get("rights").getAsInt());
				    				rs.updateLong("overall_xp", xptotal);
				    				for (int i = 0; i < skills.size()-1; i++) {
						    			JsonObject levelObj = skills.get(i).getAsJsonObject();
						    			int exp = levelObj.get("xp").getAsInt();
				    					rs.updateInt(""+Skill.SKILL_NAME[i].toLowerCase()+"_xp", exp);
				    				}
				    				rs.updateRow(); // updates an existing row
				    			}	    			
				    		} catch (Exception e) {
				    			e.printStackTrace();
				    		}
				        }
					}
				}/*else{
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
			    				del = con.prepareStatement("DELETE FROM `highscores` WHERE username = ?;");
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
			}*/
			}
		}catch(Exception e) {
		}
			
	}
}