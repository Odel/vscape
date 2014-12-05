package com.rs2.model.content.minigames.castlewars.impl;

import com.rs2.model.content.minigames.castlewars.Castlewars;
import com.rs2.model.players.Player;

public class CatapultInterface {
	
	private static int[] numberModels = {
		4863, //0
		4864, //1
		4865, //2
		4866, //3
		4867, //4
		4868, //5
		4869, //6
		4870, //7
		4871, //8
		4872 //9
	};
	
	private static int[][] baseXCoords = {
		{0,0}, //zammy
		{90,90} //sara
	};
	private static int[] getBaseXCoords(int team)
	{
		return baseXCoords[team];
	}
	
	private static int[][] fireCoords = {
		{0,0}, //zammy
		{0,0} //sara
	};
	private static void setCoords(int team, int x, int y)
	{
		fireCoords[team] = new int[]{x,y};
	}
	private static int[] getCoords(int team)
	{
		return fireCoords[team];
	}

	public static void resetCoords(int team){
		setCoords(team, 0, 0);
	}
	
	public static void openInterface(final Player player, int team){
		resetCoords(team);
		player.getActionSender().sendInterface(11169);
		player.setStatedInterface("cwCatapult");
		setCoordinatesInterface(player, team);
	}
	
	private static void setCoordinatesInterface(final Player player, int team){
		int[] coords = getCoords(team);
		int x = coords[0];
		int y = coords[1];
		if(y < 10)
		{
			player.getActionSender().sendComponentInterface(11317, numberModels[0]);
			player.getActionSender().sendComponentInterface(11318, numberModels[y]);
		}else{
			int first = y / 10;
			int second = y % 10;
			player.getActionSender().sendComponentInterface(11317, numberModels[first]);
			player.getActionSender().sendComponentInterface(11318, numberModels[second]);
		}
		if(x < 10)
		{
			player.getActionSender().sendComponentInterface(11319, numberModels[0]);
			player.getActionSender().sendComponentInterface(11320, numberModels[x]);
		}else{
			int first = x / 10;
			int second = x % 10;
			player.getActionSender().sendComponentInterface(11319, numberModels[first]);
			player.getActionSender().sendComponentInterface(11320, numberModels[second]);
		}
		int[] base = getBaseXCoords(team);
		int baseX = base[0];
		int baseY = base[1];
		player.getActionSender().moveInterface(baseX + (team == 0 ? x * 2 : -x * 2), baseY + (team == 0 ? y * 2 : -y * 2), 11332);
	}
	
	private static void incrementCoord(int team, int x, int y)
	{
		int[] coords = getCoords(team);
		int xC = coords[0];
		int yC = coords[1];
		if(xC >= 0 && xC < 30 && x == 1)
		{
			xC += x;
		}
		if(xC > 0 && xC <= 30 && x == -1)
		{
			xC += x;
		}
		if(yC >= 0 && yC < 30 && y == 1)
		{
			yC += y;
		}
		if(yC > 0 && yC <= 30 && y == -1)
		{
			yC += y;
		}
		setCoords(team, xC, yC);
	}
	
	public static boolean HandleButtons(final Player player, int buttonId)
	{
		if(!Castlewars.isInGame(player) || !player.getStatedInterface().contentEquals("cwCatapult"))
		{
			return false;
		}
		int team = player.getCastlewarsTeam();
		switch(buttonId)
		{
			case 44057 : // y up
				incrementCoord(team, 0, 1);
				setCoordinatesInterface(player, team);
			return true;
			case 44058 : // y down
				incrementCoord(team, 0, -1);
				setCoordinatesInterface(player, team);
			return true;
			case 44060 : // x left
				incrementCoord(team, -1, 0);
				setCoordinatesInterface(player, team);
			return true;
			case 44059 : // x right
				incrementCoord(team, 1, 0);
				setCoordinatesInterface(player, team);
			return true;
			case 44065 : // fire
				int[] coords = getCoords(team);
				int x = coords[0];
				int y = coords[1];
				player.setStatedInterface("");
				player.getActionSender().removeInterfaces();
				CastlewarsCatapults.fireCatapult(player, team, x, y);
			return true;
		}
		return false;
	}
	
}
