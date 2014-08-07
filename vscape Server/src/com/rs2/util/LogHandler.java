package com.rs2.util;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class LogHandler {
	
	private static String LogDirectory = "./data/logs/";
	private static String ShopDirectory = "shops/";
	private static String TradeDirectory = "trades/";
	
	private static DateFormat df = new SimpleDateFormat();

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
}