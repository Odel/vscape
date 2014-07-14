package com.rs2.model.content.minigames.fightcaves;

import com.rs2.model.Entity;
import com.rs2.model.Graphic;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.combat.HealersCombatScript;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.minigames.barrows.Barrows.spawn;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class FightCaves {
    public static final int TZ_KIH = 2627;
    public static final int TZ_KEK_SPAWN = 2738;
    public static final int TZ_KEK = 2630;
    public static final int TOK_XIL = 2631;
    public static final int YT_MEJ_KOT = 2741;
    public static final int KET_ZEK = 2743;
    public static final int TZTOK_JAD = 2745;
    public static final int YT_HURKO = 2746;
    public static final int[] npcIds = {TZ_KIH, TZ_KEK_SPAWN, TZ_KEK, TOK_XIL, YT_MEJ_KOT, KET_ZEK, TZTOK_JAD, YT_HURKO};
    public static final Position EXIT = new Position(2439, 5168, 0);
    public static final int FIRE_CAPE = 6570;
    
    public static void enterCave(final Player player, final boolean firstTime) {
	player.teleport(new Position(2413, 5117, player.getIndex() * 4));
	CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
	    @Override
	    public void execute(CycleEventContainer container) {
		if(firstTime) {
		    dialogueCycle(player);
		}
		//player.setFightCavesWave(0);
		player.getActionSender().walkTo(-5, -35, true);
		WavesHandling.spawnWave(player, player.getFightCavesWave());
		container.stop();
	    }
	    @Override
	    public void stop() {
		
	    }
	}, 3);
    }
    public static void exitCave(final Player player) {
	destroyNpcs(player);
	player.fadeTeleport(FightCaves.EXIT);
	player.setFightCavesWave(0);
    }
    
    public static boolean inFightCaves(Entity npc) {
	return (npc.getPosition().getX() > 2365 && npc.getPosition().getX() < 2430 
		&& npc.getPosition().getY() < 5120 && npc.getPosition().getY() > 5060);
    }
    private static void dialogueCycle(final Player player) {
	player.getDialogue().setLastNpcTalk(2617);
	player.getDialogue().sendNpcChat("You're on your own now " +player.getUsername()+".", "Prepare to fight for your life!", Dialogues.CONTENT);
	CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
	    @Override
	    public void execute(CycleEventContainer container) {
		container.stop();
	    }
	    @Override
	    public void stop() {
		player.getActionSender().removeInterfaces();
		return;
	    }
	}, 6);
    }
    public static void spawnYtHurko(final Player player) {
	NpcLoader.spawnNpc(player, new Npc(World.getDefinitions()[YT_HURKO], YT_HURKO, new HealersCombatScript(10, 2639, 5, new Graphic(-1, 100), new Graphic(444, 1), TZTOK_JAD)), player.getPosition().clone(), false, null);
    }
    public static void hurkoTargetJad(Npc jad) {
	for(Npc npc : World.getNpcs()) {
	    if(npc == null) continue;
	    if(npc.getNpcId() == YT_HURKO) {
		CombatManager.attack(npc, jad);
	    }
	}
    }
    public static Npc getJad(final Player player) {
	for(Npc npc : World.getNpcs()) {
	    if(npc == null) continue;
	    if(npc.getNpcId() == TZTOK_JAD && npc.getPosition().getZ() == player.getPosition().getZ()) {
		return npc;
	    }
	}
	return null;
    }
    public static void destroyNpcs(final Player player) {
	for(Npc npc : World.getNpcs()) {
	    if(npc == null) continue;
	    if(npc.inFightCaves() && npc.getPosition().getZ() == player.getPosition().getZ()) {
		NpcLoader.destroyNpc(npc);
	    }
	}
    }
    public static void attack(final Player player) {
	for(Npc npc : World.getNpcs()) {
	    if(npc == null) continue;
	    if(npc.inFightCaves() && npc.getPosition().getZ() == player.getPosition().getZ()) {
		if(!npc.isAttacking() && npc.getNpcId() != YT_HURKO) {
		    npc.walkTo(player.getPosition().clone(), false);
		    CombatManager.attack(npc, player);
		}
	    }
	}
    }
    public static Npc belowHalfHealth(final Player player) {
	for(Npc npc : World.getNpcs()) {
	    if(npc == null) continue;
	    if(npc.inFightCaves() && npc.getPosition().getZ() == player.getPosition().getZ()) {
		if(npc.getCurrentHp() <= (npc.getMaxHp()/2) ) {
		    return npc;
		}
	    }
	}
	return null;
    }
    public static void healerTargeting(final Player player) {
	for(Npc npc : World.getNpcs()) {
	    if(npc == null) continue;
	    if((npc.getNpcId() == YT_MEJ_KOT || npc.getNpcId() == YT_HURKO) 
		&& npc.getPosition().getZ() == player.getPosition().getZ()) {
		Npc toHeal = belowHalfHealth(player);
		if(npc != belowHalfHealth(player) && toHeal != null && npc.getTarget() != toHeal
		   && toHeal.getCurrentHp() != toHeal.getMaxHp()) {
		    CombatManager.attack(npc, toHeal);
		}
		else {
		    CombatManager.attack(npc, player);
		}
	    }
	}
    }
    public static void handleNpcHit(Entity attacker, Player victim, int damage) {
	if(attacker.isNpc() && ((Npc)attacker).getNpcId() == TZ_KIH) {
	    if(damage >= 1) {
		victim.getSkill().getLevel()[Skill.PRAYER] = (victim.getSkill().getLevel()[Skill.PRAYER] - damage);
		victim.getSkill().refresh(Skill.PRAYER);
	    }
	}
    }
    public static void handlePlayerHit(Entity attacker, Npc victim, int damage) {
	if(victim.getNpcId() == TZ_KEK && attacker.isPlayer()) {
	    if(damage >= 1) {
		attacker.hit(1, HitType.NORMAL);
	    }
	}
    }
    public static void handleDeath(final Player player, Npc died) {
	for(int id : npcIds) {
	    if(id == died.getNpcId()) {
		player.setFightCavesKillCount(player.getFightCavesKillCount() - 1);
		if(player.getFightCavesKillCount() == 0 && died.getNpcId() != TZTOK_JAD) {
		    player.setFightCavesWave(player.getFightCavesWave() + 1);
		    WavesHandling.spawnWave(player, player.getFightCavesWave());
		    break;
		}
		else if(died.getNpcId() == TZ_KEK) {
		    Position spawn = died.getPosition();
		    Position spawn1 = new Position(spawn.getX() - 1, spawn.getY(), spawn.getZ());
		    Position spawn2 = new Position(spawn.getX() + 1, spawn.getY(), spawn.getZ());
		    NpcLoader.spawnNpc(player, new Npc(TZ_KEK_SPAWN), spawn1, false, null);
		    NpcLoader.spawnNpc(player, new Npc(TZ_KEK_SPAWN), spawn2, false, null);
		    break;
		}
		else if(died.getNpcId() == TZTOK_JAD) {
		    exitCave(player);
		    player.getDialogue().sendGiveItemNpc("Congratulations! You survived the Tzhaar Fight Caves!", new Item(FIRE_CAPE));
		    if(!player.getInventory().canAddItem(new Item(FIRE_CAPE))) {
			player.getActionSender().sendMessage("Your fire cape has been sent to your bank.");
			player.getBank().add(new Item(FIRE_CAPE));
			
		    }
		    else {
			player.getInventory().addItem(new Item(FIRE_CAPE));
		    }
		}
	    }
	}
	
    }
    
}
