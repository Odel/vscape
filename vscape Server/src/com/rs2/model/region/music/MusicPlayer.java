package com.rs2.model.region.music;

import com.rs2.model.players.Player;

public class MusicPlayer {
	
	private Player player;

	public MusicPlayer(Player player) {
		this.player = player;
	}
	
	public enum SongData {
		ADVENTURE(16191, 177),
		ALKHARID(16192, 50),
		ALONE(16193, 102),
		AMBIENTJUNGLE(16194, 90),
		//ANYWHERE(16194, 90),
		ARABIAN(16195, 36),
		ARABIAN2(16196, 123),
		//ARABIAN3(16197, 123),
		//ARABIQUE(16198, 123),
		ARMYOFDARKNESS(16199, 160),
		ARRIVAL(16200, 186);
	//	ARTISTRY(34321, 247),
		
		private int buttonId;
		private int songId;

		private SongData(int buttonId, int songId) {
			this.buttonId = buttonId;
			this.songId = songId;
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
    	return true;
    }
}
