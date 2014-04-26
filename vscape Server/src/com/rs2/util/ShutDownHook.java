package com.rs2.util;


public class ShutDownHook extends Thread {

	@Override
	public void run() {
		PlayerSave.saveAllPlayers();
		System.out.println("Saved all players.");
	}
}