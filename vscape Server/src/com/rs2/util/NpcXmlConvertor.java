package com.rs2.util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.rs2.Constants;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.drop.NpcDropController;
import com.rs2.model.npcs.drop.NpcDropItem;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;

public class NpcXmlConvertor {

    public static void main(String[] args) throws IOException {
    	XStreamUtil.loadAllFiles();
    	//makeXmlFile("./data/npcDrops2.cfg");
    	//makeCfgFile("./data/npc-drops.cfg");
    	loadAutoSpawn("./data/drops.txt");
    	//makeXml();
    }
    
    public static void makeXml() {
    	for (int i = 3521; i < Constants.MAX_NPC_ID; i++) {
			NpcDropController drops = NpcDropController.forId(i);
			if (drops == null) {
				continue;
			}
			System.out.println("npc: "+i);
			writeToXml("	  </drops>");
			writeToXml("	</dropController>");
			writeToXml("	<dropController>");
			writeToXml("	  <npcIds>");
			writeToXml("		<int>"+i+"</int> <!-- "+new Npc(i).getDefinition().getName()+" -->");
			writeToXml("	  </npcIds>");
			writeToXml("	  <drops>");
			for (NpcDropItem drop : drops.getDropList()) {
				writeToXml("	     <npcDrop>");
				writeToXml("	       <id>"+drop.getId()+"</id> <!-- "+new Item(drop.getId()).getDefinition().getName()+" -->");
				writeToXml("	       <count>");
				for (int count : drop.getCount()) {
					writeToXml("	         <int>"+count+"</int>");
				}
				writeToXml("	       </count>");
				writeToXml("	       <chance>"+drop.getChance()+"</chance>");
				writeToXml("	     </npcDrop>");
			}
    	}
    }
	/**
	 * Loads auto-spawn file
	 **/
	public static boolean makeXmlFile(String FileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		BufferedReader characterfile = null;
		try {
			characterfile = new BufferedReader(new FileReader("./" + FileName));
		} catch (FileNotFoundException fileex) {
			System.out.println(FileName + ": file not found.");
			return false;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			System.out.println(FileName + ": error loading file.");
			return false;
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("npc")) {
					int id = Integer.parseInt(token3[0]);
					//NpcDefinition def = NpcDefinition.forId(id);
					System.out.println("npc: "+id);
					writeToXml("	  </drops>");
					writeToXml("	</dropController>");
					writeToXml("	<dropController>");
					writeToXml("	  <npcIds>");
					writeToXml("		<int>"+id+"</int>");
					writeToXml("	  </npcIds>");
					writeToXml("	  <drops>");
				}
				if (token.equals("amount")) {
					writeToXml("	       <count>");
					writeToXml("	         <int>"+(token3.length > 0 ? Integer.parseInt(token3[0].equals("") ? "1" : token3[0]) : "")+"</int>");
					if (token3.length > 1)
						writeToXml("	         <int>"+(Integer.parseInt(token3[1].equals("") ? "1" : token3[1]))+"</int>");
					if (token3.length > 2)
						writeToXml("	         <int>"+(Integer.parseInt(token3[2].equals("") ? "1" : token3[2]))+"</int>");
					if (token3.length > 3)
						writeToXml("	         <int>"+(Integer.parseInt(token3[3].equals("") ? "1" : token3[3]))+"</int>");
					if (token3.length > 4)
						writeToXml("	         <int>"+(Integer.parseInt(token3[4].equals("") ? "1" : token3[4]))+"</int>");
					if (token3.length > 5)
						writeToXml("	         <int>"+(Integer.parseInt(token3[5].equals("") ? "1" : token3[5]))+"</int>");
					if (token3.length > 6)
						writeToXml("	         <int>"+(Integer.parseInt(token3[6].equals("") ? "1" : token3[6]))+"</int>");
					if (token3.length > 7)
						writeToXml("	         <int>"+(Integer.parseInt(token3[7].equals("") ? "1" : token3[7]))+"</int>");
					if (token3.length > 8)
						writeToXml("	         <int>"+(Integer.parseInt(token3[8].equals("") ? "1" : token3[8]))+"</int>");
					if (token3.length > 9)
						writeToXml("	         <int>"+(Integer.parseInt(token3[9].equals("") ? "1" : token3[9]))+"</int>");
					writeToXml("	       </count>");
				}
				if (token.equals("loot")) {
					int itemId = Integer.parseInt(token3[0]);
					//ItemDefinition itemDef = ItemDefinition.forId(itemId);
					writeToXml("	     <npcDrop>");
					writeToXml("	       <id>"+itemId+"</id>");
				}
				if (token.equals("chance")) {
					writeToXml("	       <chance>"+Integer.parseInt(token3[0])+"</chance>");
					writeToXml("	     </npcDrop>");
				}
				/*if (token.equals("spawn")) {
					newNPC(Integer.parseInt(token3[0]), Integer.parseInt(token3[1]), Integer.parseInt(token3[2]), Integer.parseInt(token3[3]), Integer.parseInt(token3[4]));
				}*/
			} else if (line.equals("[ENDOFSPAWNLIST]")) {
				try {
					characterfile.close();
				} catch (IOException ioexception) {
				}
				return true;
			}
			try {
				line = characterfile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
				System.out.println("Loaded all npc spawns.");
			}
		}
		try {
			characterfile.close();
		} catch (IOException ioexception) {
		}
		return false;
	}

	/**
	 * Loads cfg files
	 **/
	public static boolean makeCfgFile(String FileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		boolean writeFiles = false;
		BufferedReader characterfile = null;
		try {
			characterfile = new BufferedReader(new FileReader("./" + FileName));
		} catch (FileNotFoundException fileex) {
			System.out.println(FileName + ": file not found.");
			return false;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			System.out.println(FileName + ": error loading file.");
			return false;
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("npc")) {
					int id = Integer.parseInt(token3[0]);
					System.out.println("npc: "+id);
					writeToCfg("npc = "+id);
				}
				int id = ItemDefinition.getItemId(token3[0].toLowerCase().replaceAll("_", " "));
				id = id == 617 ? 995 : id;
				if (token.equals("loot")) {
					writeFiles = id > 0 && !token3[0].toLowerCase().contains("clue");
					if (writeFiles) {
						writeToCfg("loot = "+id);
					}
				}
				if (writeFiles) {
					if (token.equals("amount")) {
						String a = "amount = ";
						for (int i = 0; i < token3.length; i++) {
							int number = Integer.parseInt(token3[i].equals("") ? "1" : token3[i]);
							a = a + "	" + number;
						}
						writeToCfg(a);
					}
					if (token.equals("chance")) {
						writeToCfg("chance = "+Integer.parseInt(token3[0]));
					}
				}
			} else if (line.equals("[ENDOFSPAWNLIST]")) {
				try {
					characterfile.close();
				} catch (IOException ioexception) {
				}
				return true;
			}
			try {
				line = characterfile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
				System.out.println("Loaded all npc spawns.");
			}
		}
		try {
			characterfile.close();
		} catch (IOException ioexception) {
		}
		return false;
	}

	/**
	 *  Write xml file
	 **/
	public static void writeToXml(String line) {
		String filePath = "./data/npcDrops3.xml";
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filePath, true));
			try {
				out.newLine();
				out.write(line);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  Write cfg file
	 **/
	public static void writeToCfg(String line) {
		String filePath = "./data/npcDrops2.cfg";
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filePath, true));
			try {
				out.newLine();
				out.write(line);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  Write cfg file
	 **/
	public static void invalidId(long line) {
		String filePath = "./data/wrongId.cfg";
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filePath, true));
			try {
				out.newLine();
				out.write(""+line);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads auto-spawn file
	 **/
	public static boolean loadAutoSpawn(String FileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		BufferedReader characterfile = null;
		try {
			characterfile = new BufferedReader(new FileReader("./" + FileName));
		} catch (FileNotFoundException fileex) {
			System.out.println(FileName + ": file not found.");
			return false;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			System.out.println(FileName + ": error loading file.");
			return false;
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("drop")) {
					int id = Integer.parseInt(token3[0]);
					String amount = token3[1];
					int chance = Integer.parseInt(token3[2]);
					writeToXml("	     <npcDrop>");
					writeToXml("	       <id>"+id+"</id> <!--"+new Item(id).getDefinition().getName()+"-->");
					writeToXml("	       <count>");
					writeToXml("	         <int>"+amount+"</int>");
					writeToXml("	       </count>");
					writeToXml("	       <chance>"+chance+"</chance>");
					writeToXml("	     </npcDrop>");
				}
			} else if (line.equals("[ENDOFSPAWNLIST]")) {
				try {
					characterfile.close();
				} catch (IOException ioexception) {
				}
				return true;
			}
			try {
				line = characterfile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
				System.out.println("Loaded all npc spawns.");
			}
		}
		try {
			characterfile.close();
		} catch (IOException ioexception) {
		}
		return false;
	}

}
