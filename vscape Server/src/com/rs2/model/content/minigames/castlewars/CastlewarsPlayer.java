package com.rs2.model.content.minigames.castlewars;

public class CastlewarsPlayer{
	private int team; //1 is sara, 2 is zammy
	private boolean isInLobby;

	public CastlewarsPlayer(int t){
		team = t;
	}

	public CastlewarsPlayer(int t, boolean l){
		team = t;
		isInLobby = l;
		//System.out.println("Player entered cw, cwplayer object created");
	}

	public int getTeamNumber(){
		return team;
	}

	public boolean getIsInLobby(){
		return isInLobby;
	}

	public void setIsInLobby(boolean q){
		isInLobby = q;
	}


}