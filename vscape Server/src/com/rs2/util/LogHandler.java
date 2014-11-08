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
	
	static BufferedWriter yellLogWriter = null;

	public static void logShop(Player player, String shopName, String transactionType, Item item, int cost, String currency)
	{
		try {
			BufferedWriter shopLogWriter = new BufferedWriter(new FileWriter(LogDirectory + ShopDirectory + shopName + ".txt", true));
			try {
				int itemId = item.getDefinition().getId();
				String itemName = item.getDefinition().getName();
				int count = item.getCount();
				shopLogWriter.write("[" + df.format(new Date()) + "]["+player.getUsername()+"]"+transactionType+": ["+itemId+"]"+itemName+" Amount: "+count+" for "+cost+" "+currency);
				shopLogWriter.newLine();
			} catch(IOException ioexception) {
				shopLogWriter.close();
			} finally {
				shopLogWriter.close();
			}
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
				//int itemId = item.getDefinition().getId();
				//String itemName = item.getDefinition().getName();
				//int count = item.getCount();
				tradeLogWriter.write("[" + df.format(new Date()) + "] "+player1.getUsername()+ " traded " + player2.getUsername() + " " + items.getCount() + " of #" + items.getId() + "");
				//System.out.println(items.toString());
				
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
		try {
			yellLogWriter = new BufferedWriter(new FileWriter(LogDirectory + "yell.txt", true));
			try {
				String time = Format.format(new Date());
				yellLogWriter.write("["+time+"] "+name + ": " + msg);	
				yellLogWriter.newLine();
			} catch(IOException ioexception) {
				yellLogWriter.close();
			} finally {
				yellLogWriter.close();
			}
		} catch(IOException ioexception) {
			System.out.println("error writing yell log file.");
		}
	}
	
	public static void LogCommand(String name, String msg)
	{
		try {
			BufferedWriter logWriter = new BufferedWriter(new FileWriter(LogDirectory + "commands.txt", true));
			try {
				logWriter.write("[" + df.format(new Date()) + "] "+ name + " used ::" + msg);
				logWriter.newLine();
			} catch(IOException ioexception) {
				logWriter.close();
			} finally {
				logWriter.close();
			}
		} catch(IOException ioexception) {
			System.out.println("error writing command log file.");
		}
	}
}