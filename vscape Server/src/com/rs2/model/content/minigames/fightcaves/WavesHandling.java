package com.rs2.model.content.minigames.fightcaves;

import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;

//import com.rs2.model.content.combat.HealersCombatScript;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 5/17/12 Time: 8:27 PM To change
 * this template use File | Settings | File Templates.
 */
public class WavesHandling {
    public static final Npc TZ_KIH = new Npc(2627);
    public static final Npc TZ_KEK_SPAWN = new Npc(2629);
    public static final Npc TZ_KEK = new Npc(2630);
    public static final Npc TOK_XIL = new Npc(2631);
    public static final Npc YT_MEJ_KOT = new Npc(2741);
    public static final Npc KET_ZEK = new Npc(2743);
    public static final Npc TZTOK_JAD = new Npc(2745);
    //public static final Npc YT_HURKO = new Npc(World.getDefinitions()[2746], 2746, new HealersCombatScript(10, 2637, 5, new Graphic(-1, 100), new Graphic(444, 100)));


    public static final Npc[][] waves = {
            {TZ_KIH},
            {TZ_KIH, TZ_KIH},
            {TZ_KEK},
            {TZ_KEK, TZ_KIH},
            {TZ_KEK, TZ_KIH, TZ_KIH},
            {TZ_KEK, TZ_KEK},
            {TOK_XIL},
            {TOK_XIL, TZ_KIH},
            {TOK_XIL, TZ_KIH, TZ_KIH},
            {TOK_XIL, TZ_KEK},
            {TOK_XIL, TZ_KEK, TZ_KIH},
            {TOK_XIL, TZ_KEK, TZ_KIH, TZ_KIH},
            {TOK_XIL, TZ_KEK, TZ_KEK},
            {TOK_XIL, TOK_XIL},
            {YT_MEJ_KOT},
            {YT_MEJ_KOT, TZ_KIH},
            {YT_MEJ_KOT, TZ_KIH, TZ_KIH},
            {YT_MEJ_KOT, TZ_KEK},
            {YT_MEJ_KOT, TZ_KEK, TZ_KIH},
            {YT_MEJ_KOT, TZ_KEK, TZ_KIH, TZ_KIH},
            {YT_MEJ_KOT, TZ_KEK, TZ_KEK},
            {YT_MEJ_KOT, TOK_XIL},
            {YT_MEJ_KOT, TOK_XIL, TZ_KIH},
            {YT_MEJ_KOT, TOK_XIL, TZ_KIH, TZ_KIH},
            {YT_MEJ_KOT, TOK_XIL, TZ_KEK},
            {YT_MEJ_KOT, TOK_XIL, TZ_KEK, TZ_KIH},
            {YT_MEJ_KOT, TOK_XIL, TZ_KEK, TZ_KIH, TZ_KIH},
            {YT_MEJ_KOT, TOK_XIL, TZ_KEK, TZ_KEK},
            {YT_MEJ_KOT, TOK_XIL, TOK_XIL},
            {YT_MEJ_KOT, YT_MEJ_KOT},
            {KET_ZEK},
            {KET_ZEK, TZ_KIH},
            {KET_ZEK, TZ_KIH, TZ_KIH},
            {KET_ZEK, TZ_KEK},
            {KET_ZEK, TZ_KEK, TZ_KIH},
            {KET_ZEK, TZ_KEK, TZ_KIH, TZ_KIH},
            {KET_ZEK, TZ_KEK, TZ_KEK},
            {KET_ZEK, TOK_XIL},
            {KET_ZEK, TOK_XIL, TZ_KIH},
            {KET_ZEK, TOK_XIL, TZ_KIH, TZ_KIH},
            {KET_ZEK, TOK_XIL, TZ_KEK},
            {KET_ZEK, TOK_XIL, TZ_KEK, TZ_KIH},
            {KET_ZEK, TOK_XIL, TZ_KEK, TZ_KIH, TZ_KIH},
            {KET_ZEK, TOK_XIL, TZ_KEK, TZ_KEK},
            {KET_ZEK, TOK_XIL, TOK_XIL},
            {KET_ZEK, YT_MEJ_KOT},
            {KET_ZEK, YT_MEJ_KOT, TZ_KIH},
            {KET_ZEK, YT_MEJ_KOT, TZ_KIH, TZ_KIH},
            {KET_ZEK, YT_MEJ_KOT, TZ_KEK},
            {KET_ZEK, YT_MEJ_KOT, TZ_KEK, TZ_KIH},
            {KET_ZEK, YT_MEJ_KOT, TZ_KEK, TZ_KIH, TZ_KIH},
            {KET_ZEK, YT_MEJ_KOT, TZ_KEK, TZ_KEK},
            {KET_ZEK, YT_MEJ_KOT, TOK_XIL},
            {KET_ZEK, YT_MEJ_KOT, TOK_XIL, TZ_KIH},
            {KET_ZEK, YT_MEJ_KOT, TOK_XIL, TZ_KIH, TZ_KIH},
            {KET_ZEK, YT_MEJ_KOT, TOK_XIL, TZ_KEK},
            {KET_ZEK, YT_MEJ_KOT, TOK_XIL, TZ_KEK, TZ_KIH},
            {KET_ZEK, YT_MEJ_KOT, TOK_XIL, TZ_KEK, TZ_KIH, TZ_KIH},
            {KET_ZEK, YT_MEJ_KOT, TOK_XIL, TZ_KEK, TZ_KEK},
            {KET_ZEK, YT_MEJ_KOT, TOK_XIL, TOK_XIL},
            {KET_ZEK, YT_MEJ_KOT, YT_MEJ_KOT},
            {KET_ZEK, KET_ZEK},
            {TZTOK_JAD}
            };

    public static void spawnWave(Player player, int wave){
        for(Npc npc : waves[wave]){
            NpcLoader.spawnNpc(player, npc, MinigameAreas.randomPosition(FightCaveAreas.getRandomSpawningArea(player.getPosition())), false, null);
            //CombatManager.attack(npc, player);

            //System.out.println("spawned " + npc.getPosition());
        }
    }


    public static void spawnFinalWave(Player player){
        NpcLoader.spawnNpc(player, TZTOK_JAD, true, false);
        //NpcLoader.spawnNpc(TZTOK_JAD, YT_HURKO, TZTOK_JAD.getPosition(), false);
    }
}
