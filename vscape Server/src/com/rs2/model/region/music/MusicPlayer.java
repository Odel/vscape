package com.rs2.model.region.music;

import com.rs2.model.players.Player;

public class MusicPlayer {
	
	private Player player;

	public MusicPlayer(Player player) {
		this.player = player;
	}
	
	public enum SongData {
		ADVENTURE(16191, 177, "Adventure"),
		ALKHARID(16192, 50, "Al Kharid"),
		ALONE(16193, 102, "Alone"),
		AMBIENTJUNGLE(16194, 90, "Ambient Jungle"),
		//ANYWHERE(16194, 90, "Anywhere"),
		ARABIAN(16195, 36, "Arabian"),
		ARABIAN2(16196, 123, "Arabian2"),
		//ARABIAN3(16197, 123, "Arabian3"),
		//ARABIQUE(16198, 123, "Arabique"),
		ARMYOFDARKNESS(16199, 160, "Army Of Darkness"),
		ARRIVAL(16200, 186, "Arrival");
	//	ARTISTRY(34321, 247),
		
		private int buttonId;
		private int songId;
		private String name;

		private SongData(int buttonId, int songId, String name) {
			this.buttonId = buttonId;
			this.songId = songId;
			this.name = name;
		}

		public int getButton() {
			return buttonId;
		}
		
		public static SongData forId(int buttonId) {
			for (SongData songData : SongData.values()) {
					if (songData.buttonId == buttonId)
						return songData;
			}
			return null;
		}
	}
    
    public boolean handleButton(int buttonId)
    {
    	SongData songData = SongData.forId(buttonId);
    	if(songData == null)
    		return false;
    	
    	if(player.getMusicAuto())
    	{
    		player.setMusicAuto(false);
    		player.getActionSender().sendConfig(18, player.getMusicAuto() ? 1 : 0);
    	}
    	player.getActionSender().sendSong(songData.songId);
    	player.getActionSender().sendMessage("Now playing song: "+songData.name);
    	return true;
    }
}
