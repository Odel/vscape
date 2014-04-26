package com.rs2.model.content.randomevents;

import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;

public class SpawnEvent {

	public enum RandomNpc {
		ZOMBIE(419),
		SHADE(425),
		TREE_SPIRIT(438),
		ROCK_GOLEM(413),
		RIVER_TROLL(391),
		EVIL_CHICKEN(2463);
		
		int npcId;

		private RandomNpc(int npcId) {
			this.npcId = npcId;
		}
		
		public int getId() {
			return npcId;
		}
	}

	public static int addValue(int combat) {
		return combat < 10 ? 0 : combat < 20 ? 1 : combat < 40 ? 2 : combat < 70 ? 3 : combat < 110 ? 4 : 5;
	}

	public static void spawnNpc(Player player, RandomNpc npc) {
        if (player.getSpawnedNpc() != null)
            return;
		NpcLoader.spawnNpc(player, new Npc(npc.getId() + addValue(player.getCombatLevel())), true, true);
	}
	
}
