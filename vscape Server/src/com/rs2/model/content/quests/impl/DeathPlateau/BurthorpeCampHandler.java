package com.rs2.model.content.quests.impl.DeathPlateau;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.Npc.WalkType;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class BurthorpeCampHandler {
	private static Npc commandSergeant;
	private static Npc trainingSoldier_1;
	private static Npc trainingSoldier_2;
	private static Npc[] trainees = new Npc[10];
	private static final int[] POSSIBLE_ANIMS = {1067, 393, 422, 423};
	private static final int[] POSSIBLE_ANIMS_SERG = {1067, 424, 393, 423, 422};
	public static String[] LATIN_PHRASES = new String[23];
	
	
	public static void init() {
		commandSergeant = newNpc(1061, 2893, 3541, 0, 3);
		trainingSoldier_1 = newNpc(1064, 2900, 3533, 0, 4);
		trainingSoldier_2 = newNpc(1064, 2900, 3531, 0, 4);
		for(int i = 2891; i < 2896; i++) {
			trainees[i - 2891] = newNpc(1063, i, 3539, 0, 2);
			trainees[(i - 2891) + 5] = newNpc(1063, i, 3537, 0, 2);
		}
		beginCycles();
		try {
			loadPhrases();
		} catch (IOException e) {
			System.out.println("Error loading Burthorpe soldier latin phrases.");
		}
	}
	
	public static void loadPhrases() throws IOException {
		String[] read = new String[23];
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("data/npcs/LatinPhrases.txt")));
			String line = null;
			int q = 0;
			while (((line = br.readLine()) != null) && (q < 23)) {
				read[q] = line;
				q++;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		LATIN_PHRASES = read;
	}
	
	public static Npc getTrainingSoldier(int soldier) {
		return soldier == 1 ? trainingSoldier_1 : trainingSoldier_2;
	}
	
	public static Npc getCommandSergeant() {
		return commandSergeant;
	}
	
	public static Npc newNpc(int id, int x, int y, int heightLevel, int face) {
		Npc npc = new Npc(id);
		npc.setPosition(new Position(x, y, heightLevel));
		npc.setSpawnPosition(new Position(x, y, heightLevel));
		npc.setNeedsRespawn(true);
		npc.setMinWalk(new Position(x - Constants.NPC_WALK_DISTANCE, y - Constants.NPC_WALK_DISTANCE));
		npc.setMaxWalk(new Position(x + Constants.NPC_WALK_DISTANCE, y + Constants.NPC_WALK_DISTANCE));
		npc.setWalkType(face == 1 || face > 5 ? WalkType.WALK : WalkType.STAND);
		npc.setFace(face);
		npc.setCurrentX(x);
		npc.setCurrentY(y);
		npc.setNeedsRespawn(true);
		World.register(npc);
		return npc;
	}
	
	public static void beginCycles() {
		CycleEventHandler.getInstance().addEvent(trainingSoldier_1, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				trainingSoldier_1.getUpdateFlags().sendAnimation(POSSIBLE_ANIMS[Misc.randomMinusOne(POSSIBLE_ANIMS.length)], Misc.random(5));
			}
			@Override
			public void stop() {}
		}, 10);
		CycleEventHandler.getInstance().addEvent(trainingSoldier_2, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				trainingSoldier_2.getUpdateFlags().sendAnimation(POSSIBLE_ANIMS[Misc.randomMinusOne(POSSIBLE_ANIMS.length)], Misc.random(5));
			}
			@Override
			public void stop() {}
		}, 11);
		CycleEventHandler.getInstance().addEvent(commandSergeant, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				final int anim = POSSIBLE_ANIMS_SERG[Misc.randomMinusOne(POSSIBLE_ANIMS_SERG.length)];
				commandSergeant.getUpdateFlags().sendAnimation(anim, Misc.random(5));
				if (commandSergeant.playerNearby()) {
					for (final Npc trainee : trainees) {
						if(trainee != null) {
							trainee.getUpdateFlags().sendAnimation(anim, 100);
						}
					}
				}
			}
			@Override
			public void stop() {}
		}, 12);
	}
	
}
