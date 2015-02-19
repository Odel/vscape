package com.rs2.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class LogHandler {
	
	private static String LogDirectory = "./data/logs/";
	private static String ShopDirectory = "shops/";
	private static String TradeDirectory = "trades/";
	
	static SimpleDateFormat Format = new SimpleDateFormat("MM-dd-yy hh:mm:ss a");
	
	private static DateFormat df = new SimpleDateFormat();
	
	public static void logShop(Player player, String shopName, String transactionType, Item item, int cost, String currency)
	{
		 try(BufferedWriter shopLogWriter = new BufferedWriter(new FileWriter(LogDirectory + ShopDirectory + shopName + ".txt", true))){
			int itemId = item.getDefinition().getId();
			String itemName = item.getDefinition().getName();
			int count = item.getCount();
			shopLogWriter.write("[" + df.format(new Date()) + "]["+player.getUsername()+"]"+transactionType+": ["+itemId+"]"+itemName+" Amount: "+count+" for "+cost+" "+currency);
			shopLogWriter.newLine();
			shopLogWriter.flush();
			shopLogWriter.close();
		 } catch(IOException ioexception) {
			System.out.println("error writing shop log file.");
		 }
	}
	
	public static void logTrade(Player player1, Player player2, Item items)
	{
		try {
			int compareNames = player1.getUsername().compareTo(player2.getUsername());
			BufferedWriter tradeLogWriter = null;
			if(compareNames < 0)
			{
				tradeLogWriter = new BufferedWriter(new FileWriter(LogDirectory + TradeDirectory + player1.getUsername() + "&" + player2.getUsername() + ".txt", true));
			}
			else
			{
				tradeLogWriter = new BufferedWriter(new FileWriter(LogDirectory + TradeDirectory + player2.getUsername() + "&" + player1.getUsername() + ".txt", true));
			}
			try {
				tradeLogWriter.write("[" + df.format(new Date()) + "] "+player1.getUsername()+ " traded " + player2.getUsername() + " " + items.getCount() + " of #" + items.getId() + "");
				tradeLogWriter.newLine();
			} catch(IOException ioexception) {
				tradeLogWriter.close();
			} finally {
				tradeLogWriter.close();
			}
		} catch(IOException ioexception) {
			System.out.println("error writing trade log file.");
		}
	}
	
	public static void logYell(String name, String msg)
	{
		 try(BufferedWriter yellLogWriter = new BufferedWriter(new FileWriter(LogDirectory  + "yell.txt", true))){
			String time = Format.format(new Date());
			yellLogWriter.write("["+time+"] "+name + ": " + msg);	
			yellLogWriter.newLine();
			yellLogWriter.flush();
			yellLogWriter.close();
		 } catch(IOException ioexception) {
			System.out.println("error writing yell log file.");
		 }
	}
	
	public static void LogCommand(String name, String msg)
	{
		 try(BufferedWriter commandLog = new BufferedWriter(new FileWriter(LogDirectory  + "commands.txt", true))){
			 commandLog.write("[" + df.format(new Date()) + "] "+ name + " used ::" + msg);
			 commandLog.newLine();
			 commandLog.flush();
			 commandLog.close();
		 } catch(IOException ioexception) {
			 System.out.println("error writing command log file.");
	     }
	}
}