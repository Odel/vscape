package com.rs2.model.region.music;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MusicLoader {
	
	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(MusicLoader.class.getName());

	private static Music[] music;
	
	public static void Load() throws IOException {
        try(FileReader reader = new FileReader("./datajson/content/music.json"))
        {
        	List<Music> musicList = new Gson().fromJson(reader, new TypeToken<List<Music>>(){}.getType());
        	music = new Music[musicList.size()];
	        for (int i = 0; i < music.length; i++) {// ItemDefinition def : defs) {
	        	music[i] = musicList.get(i);
	        }
	        musicList.clear();
        	reader.close();
        	System.out.println("Loaded "+ music.length +" music definitions!");
        } catch (IOException e) {
			logger.warning("Failed to initialize the music: " + e);
		}
	}

	public static Music forRegion(int region) {
		for (Music m : music) {
			if (m == null)
				continue;
			if(m.getRegions() == null)
				continue;
			for (int reg : m.getRegions()) {
				if (reg == region)
					return m;
			}
		}
		return null;
	}
	
	public static Music forButton(int button) {
		for (Music m : music) {
			if (m == null)
				continue;
			if (m.getButton() == button)
				return m;
		}
		return null;
	}
	
	public static Music forSong(int song) {
		for (Music m : music) {
			if (m == null)
				continue;
			if (m.getSong() == song)
				return m;
		}
		return null;
	}
	
	public static Music[] getMusic() {
		return music;
	}
}
