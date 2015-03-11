package com.rs2.model.content.quests.impl.DeathPlateau;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.Npc.WalkType;

public class BurthorpeCampHandler {
	private static Npc commandSergeant;
	private static Npc trainingSoldier_1;
	private static Npc trainingSoldier_2;
	
	
	public static void init() {
		commandSergeant = newNpc(1061, 2893, 3540, 0, 3);
		trainingSoldier_1 = newNpc(1064, 2900, 3533, 0, 4);
		trainingSoldier_2 = newNpc(1064, 2900, 3531, 0, 4);
	}
	
	/*
	1064	2900	3533	0	4	Soldier
spawn = 1064	2900	3531	0	4	Soldier
	*/
	
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
}
