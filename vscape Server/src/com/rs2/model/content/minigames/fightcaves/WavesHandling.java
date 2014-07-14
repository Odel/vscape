package com.rs2.model.content.minigames.fightcaves;

import com.rs2.model.Graphic;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.combat.HealersCombatScript;
import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

//import com.rs2.model.content.combat.HealersCombatScript;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 5/17/12 Time: 8:27 PM To change
 * this template use File | Settings | File Templates.
 */
public class WavesHandling {
    public static final int TZ_KIH = 2627;
    public static final int TZ_KEK_SPAWN = 2738;
    public static final int TZ_KEK = 2630;
    public static final int TOK_XIL = 2631;
    public static final int YT_MEJ_KOT = 2741;
    public static final int KET_ZEK = 2743;
    public static final int TZTOK_JAD = 2745;
    public static final int YT_HURKO = 2746;
    public static final int[] npcIds = {TZ_KIH, TZ_KEK_SPAWN, TZ_KEK, TOK_XIL, YT_MEJ_KOT, KET_ZEK, TZTOK_JAD, YT_HURKO};
    public static final int[] idsLessMejKot = {TZ_KIH, TZ_KEK_SPAWN, TZ_KEK, TOK_XIL, KET_ZEK, TZTOK_JAD, YT_HURKO};

    public static final int[][] waves = { //0 offset for the kek_spawns
            {TZ_KIH}, //wave 0
            {TZ_KIH, TZ_KIH},
            {TZ_KEK, 0, 0},
            {TZ_KEK, 0, 0, TZ_KIH},
            {TZ_KEK, 0, 0, TZ_KIH, TZ_KIH},
            {TZ_KEK, TZ_KEK, 0, 0, 0, 0}, //wave 5
            {TOK_XIL},
            {TOK_XIL, TZ_KIH},
            {TOK_XIL, TZ_KIH, TZ_KIH},
            {TOK_XIL, TZ_KEK, 0, 0},
            {TOK_XIL, TZ_KEK, 0, 0, TZ_KIH}, //wave 10
            {TOK_XIL, TZ_KEK, 0, 0, TZ_KIH, TZ_KIH},
            {TOK_XIL, TZ_KEK, 0, 0, TZ_KEK, 0, 0},
            {TOK_XIL, TOK_XIL},
            {YT_MEJ_KOT},
            {YT_MEJ_KOT, TZ_KIH}, //wave 15
            {YT_MEJ_KOT, TZ_KIH, TZ_KIH},
            {YT_MEJ_KOT, TZ_KEK, 0, 0},
            {YT_MEJ_KOT, TZ_KEK, 0, 0, TZ_KIH},
            {YT_MEJ_KOT, TZ_KEK, 0, 0, TZ_KIH, TZ_KIH},
            {YT_MEJ_KOT, TZ_KEK, 0, 0, TZ_KEK, 0, 0}, //wave 20
            {YT_MEJ_KOT, TOK_XIL},
            {YT_MEJ_KOT, TOK_XIL, TZ_KIH},
            {YT_MEJ_KOT, TOK_XIL, TZ_KIH, TZ_KIH},
            {YT_MEJ_KOT, TOK_XIL, TZ_KEK, 0, 0},
            {YT_MEJ_KOT, TOK_XIL, TZ_KEK, 0, 0, TZ_KIH}, //wave 25
            {YT_MEJ_KOT, TOK_XIL, TZ_KEK, 0, 0, TZ_KIH, TZ_KIH},
            {YT_MEJ_KOT, TOK_XIL, TZ_KEK, 0, 0, TZ_KEK, 0, 0},
            {YT_MEJ_KOT, TOK_XIL, TOK_XIL},
            {YT_MEJ_KOT, YT_MEJ_KOT},
            {KET_ZEK}, //wave 30
            {KET_ZEK, TZ_KIH},
            {KET_ZEK, TZ_KIH, TZ_KIH},
            {KET_ZEK, TZ_KEK, 0, 0},
            {KET_ZEK, TZ_KEK, 0, 0, TZ_KIH},
            {KET_ZEK, TZ_KEK, 0, 0, TZ_KIH, TZ_KIH}, //wave 35
            {KET_ZEK, TZ_KEK, 0, 0, TZ_KEK, 0, 0},
            {KET_ZEK, TOK_XIL},
            {KET_ZEK, TOK_XIL, TZ_KIH},
            {KET_ZEK, TOK_XIL, TZ_KIH, TZ_KIH},
            {KET_ZEK, TOK_XIL, TZ_KEK, 0, 0}, //wave 40
            {KET_ZEK, TOK_XIL, TZ_KEK, 0, 0, TZ_KIH},
            {KET_ZEK, TOK_XIL, TZ_KEK, 0, 0, TZ_KIH, TZ_KIH},
            {KET_ZEK, TOK_XIL, TZ_KEK, 0, 0, TZ_KEK, 0, 0},
            {KET_ZEK, TOK_XIL, TOK_XIL},
            {KET_ZEK, YT_MEJ_KOT}, //wave 45
            {KET_ZEK, YT_MEJ_KOT, TZ_KIH},
            {KET_ZEK, YT_MEJ_KOT, TZ_KIH, TZ_KIH},
            {KET_ZEK, YT_MEJ_KOT, TZ_KEK, 0, 0},
            {KET_ZEK, YT_MEJ_KOT, TZ_KEK, 0, 0, TZ_KIH},
            {KET_ZEK, YT_MEJ_KOT, TZ_KEK, 0, 0, TZ_KIH, TZ_KIH}, //wave 50
            {KET_ZEK, YT_MEJ_KOT, TZ_KEK, 0, 0, TZ_KEK, 0, 0}, 
            {KET_ZEK, YT_MEJ_KOT, TOK_XIL},
            {KET_ZEK, YT_MEJ_KOT, TOK_XIL, TZ_KIH},
            {KET_ZEK, YT_MEJ_KOT, TOK_XIL, TZ_KIH, TZ_KIH},
            {KET_ZEK, YT_MEJ_KOT, TOK_XIL, TZ_KEK, 0, 0}, //wave 55
            {KET_ZEK, YT_MEJ_KOT, TOK_XIL, TZ_KEK, 0, 0, TZ_KIH},
            {KET_ZEK, YT_MEJ_KOT, TOK_XIL, TZ_KEK, 0, 0, TZ_KIH, TZ_KIH},
            {KET_ZEK, YT_MEJ_KOT, TOK_XIL, TZ_KEK, 0, 0, TZ_KEK, 0, 0},
            {KET_ZEK, YT_MEJ_KOT, TOK_XIL, TOK_XIL},
            {KET_ZEK, YT_MEJ_KOT, YT_MEJ_KOT}, //wave 60
            {KET_ZEK, KET_ZEK}, 
            {TZTOK_JAD} //wave 62
            };

    public static void spawnWave(final Player player, final int wave) {
	CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
	    @Override
	    public void execute(CycleEventContainer container) {
		for(int id : waves[wave]) {
		    if(id == 0) continue;
		    Position spawn = MinigameAreas.randomPosition(FightCaveAreas.getRandomSpawningArea(player.getPosition()));
		    spawn.setZ(player.getPosition().getZ());
		    if(id != YT_MEJ_KOT) {
			NpcLoader.spawnNpc(player, new Npc(id), spawn, false, null);
		    }
		    else {
			NpcLoader.spawnNpc(player, new Npc(World.getDefinitions()[YT_MEJ_KOT], YT_MEJ_KOT, new HealersCombatScript(10, 2639, 5, new Graphic(-1, 100), new Graphic(444, 1), idsLessMejKot)), spawn, false, null);
		    }
		}
		player.setFightCavesKillCount(waves[wave].length);
		if(wave == 62) { //jad
		    player.getActionSender().sendMessage("You hear thunderous footsteps... Something big is near.");
		}
		else {
		    player.getActionSender().sendMessage("Wave " + wave +"...");
		}
		container.stop();
	    }
	    @Override
	    public void stop() {
		return;
	    }
	}, 6);
        
    }


    public static void spawnFinalWave(Player player){
        NpcLoader.spawnNpc(player, new Npc(TZTOK_JAD), true, false);
        //NpcLoader.spawnNpc(TZTOK_JAD, YT_HURKO, TZTOK_JAD.getPosition(), false);
    }
}
