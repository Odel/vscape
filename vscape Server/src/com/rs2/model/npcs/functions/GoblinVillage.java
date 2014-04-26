package com.rs2.model.npcs.functions;

import java.util.ArrayList;

import com.rs2.model.World;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;

/**
 * Created by IntelliJ IDEA.
 * User: vayken
 * Date: 6/22/12
 * Time: 9:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class GoblinVillage {
    public static final int RED_GOBLIN = 299;
    public static final int GREEN_GOBLIN = 298;

    public static final String[] GREEN_TEAM_MESSAGES = {"Green!", "Stupid reddie!", "Green not red!", "Red armour stupid!", "Green armour best!"};

    public static final String[] RED_TEAM_MESSAGES = {"Red!", "Stupid greenie!", "Red not green!", "Green armour stupid!", "Red armour best!"};

    public static ArrayList<Npc> getGoblins(String type){
        ArrayList<Npc> goblins = new ArrayList<Npc>();
        for(Npc npc : World.getNpcs()){
            if(npc == null) continue;
            if(!npc.isAttacking()){
                if(type.equals("red") && npc.getDefinition().getId() == RED_GOBLIN)
                    goblins.add(npc);
                else if(type.equals("green") && npc.getDefinition().getId() == GREEN_GOBLIN)
                    goblins.add(npc);
            }
        }
        return goblins;
    }

    public static ArrayList<Npc> getAttackableGoblin(Npc npc){
        ArrayList<Npc> attackable = new ArrayList<Npc>();
        ArrayList<Npc> ennemies = getGoblins(npc.getDefinition().getId() == RED_GOBLIN ? "green" : "red");
        for(int i = 0; i < ennemies.size(); i++){
            if(Misc.getDistance(ennemies.get(i).getPosition(), npc.getPosition()) <= 4)
                attackable.add(ennemies.get(i));
        }
        //System.out.println(attackable.size());
        return attackable;
    }

    public static Npc getRandomGoblin(Npc npc){
        ArrayList<Npc> array = getAttackableGoblin(npc);
        return array.size() == 0 ? null : array.get(Misc.random(array.size() - 1));
    }

    public static void updateGoblinsShouts(){
        ArrayList<Npc> redGoblins = getGoblins("red");
        ArrayList<Npc> greenGoblins = getGoblins("green");
        for(int i = 0; i < redGoblins.size(); i++)
            redGoblins.get(i).getUpdateFlags().sendForceMessage(RED_TEAM_MESSAGES[Misc.random(RED_TEAM_MESSAGES.length - 1)]);
        for(int i = 0; i < greenGoblins.size(); i++)
            greenGoblins.get(i).getUpdateFlags().sendForceMessage(GREEN_TEAM_MESSAGES[Misc.random(GREEN_TEAM_MESSAGES.length - 1)]);

    }

    public static void updateGoblinFights(){
        ArrayList<Npc> redGoblins = getGoblins("red");
        for(int i = 0; i < redGoblins.size(); i++){
            if(Misc.random(0) == 0){
                Npc randomEnemy = getRandomGoblin(redGoblins.get(i));
                if(randomEnemy == null) return;
                CombatManager.attack(redGoblins.get(i), randomEnemy);
                redGoblins.get(i).setFollowDistance(1);
                redGoblins.get(i).setFollowingEntity(randomEnemy);
            }
        }
    }

    public static void sendGoblinAttackTicks(){
        World.submit(new Tick(10) {
		    @Override
            public void execute() {
                updateGoblinsShouts();
                updateGoblinFights();
		    }
        });
    }
}
