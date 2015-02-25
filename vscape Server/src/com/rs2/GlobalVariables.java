package com.rs2;

import com.rs2.model.tick.TickTimer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class GlobalVariables {

    public static boolean ACCEPT_CONNECTIONS = true;

	public static int serverGlobalTimer = 0;
	public static TickTimer serverUpdateTimer = null;
	
	public static String[] patchNotes = null;
	public static String[] info = null;
	public static String[] rules = null;
	public static String[] degradeInfo = null;
	public static String[] npcDump = null;
	public static String[] itemDump = null;
	
    public static SimpleDateFormat datetime = new SimpleDateFormat("MM/dd/yy HH:mm");
	
	public static HashMap<Integer, String> ALPHABET = new HashMap<>();
	// public static long leverTimer[] = new long[LeverHandler.lever.length];
	public static int grainHopper = 0, grainBin = 0;
	
	public static int getServerGlobalTimer() {
		return serverGlobalTimer;
	}
	public static void setServerGlobalTimer(int serverGlobalTimer) {
		GlobalVariables.serverGlobalTimer = serverGlobalTimer;
	}
	public static TickTimer getServerUpdateTimer() {
		return serverUpdateTimer;
	}
	public static void setServerUpdateTimer(TickTimer serverUpdateTimer) {
		GlobalVariables.serverUpdateTimer = serverUpdateTimer;
	}
	public static int getGrainHopper() {
		return grainHopper;
	}
	public static void setGrainHopper(int grainHopper) {
		GlobalVariables.grainHopper = grainHopper;
	}
	public static int getGrainBin() {
		return grainBin;
	}
	public static void setGrainBin(int grainBin) {
		GlobalVariables.grainBin = grainBin;
	}

	private static ArrayList<String> bannedIps = new ArrayList<String>();
	private static ArrayList<String> bannedMacs = new ArrayList<String>();
	
	public static void loadBans(){
		bannedIps.clear();
		bannedMacs.clear();
		try(BufferedReader br = new BufferedReader(new FileReader(new File("data/bannedips.txt")))){
			String line = null;
			while ((line = br.readLine()) != null) {
				if(!bannedIps.contains(line.trim()))
					bannedIps.add(line.trim());
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try(BufferedReader br = new BufferedReader(new FileReader(new File("data/bannedmacs.txt")))){
			String line = null;
			while ((line = br.readLine()) != null) {
				if(!bannedMacs.contains(line.trim()))
					bannedMacs.add(line.trim());
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Loaded " + bannedIps.size() + " Banned IP addresses");
		System.out.println("Loaded " + bannedMacs.size() + " Banned MAC addresses");
	}
	
	public static void banIp(String ip)
	{
		if(!bannedIps.contains(ip.trim()))
		{
			bannedIps.add(ip.trim());
			try(BufferedWriter bw = new BufferedWriter(new FileWriter("data/bannedips.txt", true))){
				bw.write(ip.trim());
				bw.newLine();
				bw.flush();
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void unbanIp(String ip)
	{
		if(bannedIps.contains(ip.trim()))
		{
			bannedIps.remove(ip.trim());
			try(BufferedWriter bw = new BufferedWriter(new FileWriter("data/bannedips.txt", false))){
				for(String ipA : bannedMacs)
				{
					bw.write(ipA.trim());
					bw.newLine();
				}
				bw.flush();
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static boolean isIpBanned(String ip){
		if(bannedIps == null || bannedIps.size() <= 0)
		{
			return false;
		}
		return bannedIps.contains(ip.trim());
	}
	
	public static void banMac(String mac)
	{
		if(!bannedMacs.contains(mac.trim()))
		{
			bannedMacs.add(mac.trim());
			try(BufferedWriter bw = new BufferedWriter(new FileWriter("data/bannedmacs.txt", true))){
				bw.write(mac.trim());
				bw.newLine();
				bw.flush();
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void unbanMac(String mac)
	{
		if(bannedMacs.contains(mac.trim()))
		{
			bannedMacs.remove(mac.trim());
			try(BufferedWriter bw = new BufferedWriter(new FileWriter("data/bannedmacs.txt", false))){
				for(String macA : bannedMacs)
				{
					bw.write(macA.trim());
					bw.newLine();
				}
				bw.flush();
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static boolean isMacBanned(String mac){
		if(bannedMacs == null || bannedMacs.size() <= 0)
		{
			return false;
		}
		return bannedMacs.contains(mac.trim());
	}
}
