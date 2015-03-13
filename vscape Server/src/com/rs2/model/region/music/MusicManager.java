package com.rs2.model.region.music;

import com.rs2.model.players.Player;

public class MusicManager {

	private Player player;

	public MusicManager(Player player) {
		this.player = player;
	}

	public void Init(){
		player.getActionSender().sendConfig(18, player.getMusicAuto() ? 1 : 0);
		player.getActionSender().sendString("AUTO", 4439);
		
		for (Music music : MusicLoader.getMusic()) {
			if (music == null)
				continue;
			if (music.getFrame() <= 0)
				continue;
			player.getActionSender().sendStringColor(music.getFrame(), 0x3366);
		}
	}
	
	public void playRegionMusic() {
		if(!player.getMusicAuto())
			return;
		int regionId = player.getPosition().getRegionId();
		Music music = MusicLoader.forRegion(regionId);
		if(music == null)
		{
			//System.out.println("Music isn't added into this region yet! Region = "+ player.getPosition().getRegionId());
			return;
		}
		if(music.getSong() == player.currentSong)
			return;
		player.getActionSender().sendSong(music.getSong());
		player.getActionSender().sendString(music.getName(), 4439);
	}
	
    public boolean handleButton(int buttonId)
    {
    	Music music = MusicLoader.forButton(buttonId);
    	if(music != null)
    	{
        	if(player.getMusicAuto())
        	{
        		player.setMusicAuto(false);
        		player.getActionSender().sendConfig(18, player.getMusicAuto() ? 1 : 0);
        	}
    		player.getActionSender().sendSong(music.getSong());
    		player.getActionSender().sendString(music.getName(), 4439);
    		return true;
    	}
    	return false;
    }
}
