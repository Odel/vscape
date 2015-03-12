package com.rs2.model.npcs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.Graphic;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.Following;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.minigames.magetrainingarena.CreatureGraveyard;
import com.rs2.model.content.quests.impl.MonkeyMadness.ApeAtollNpcs;
import com.rs2.model.npcs.Npc.WalkType;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

/**
 * Having anything to do with any type of npc data loading.
 * 
 * @author BFMV
 */
public class NpcLoader {

	/**
	 * Loads auto-spawn file
	 * @throws IOException 
	 **/
	public static boolean loadAutoSpawn(String FileName) throws IOException {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		BufferedReader characterfile = null;
		try {
			characterfile = new BufferedReader(new FileReader("./" + FileName));
		} catch (FileNotFoundException fileex) {
			System.out.println(FileName + ": file not found.");
			return false;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			System.out.println(FileName + ": error loading file.");
			characterfile.close();
			return false;
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("spawn")) {
					newNPC(Integer.parseInt(token3[0]), Integer.parseInt(token3[1]), Integer.parseInt(token3[2]), Integer.parseInt(token3[3]), Integer.parseInt(token3[4]));
				}
			} else if (line.equals("[ENDOFSPAWNLIST]")) {
				try {
					characterfile.close();
				} catch (IOException ioexception) {
				}
				characterfile.close();
				return true;
			}
			try {
				line = characterfile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
				System.out.println("Loaded all npc spawns.");
			}
		}
		try {
			characterfile.close();
		} catch (IOException ioexception) {
		}
		return false;
	}

	public static void newNPC(int id, int x, int y, int heightLevel, int face) {
		Npc npc = new Npc(id);
		if(id == 3101) {
		    CreatureGraveyard.GUARDIAN = npc;
		}
		if(id == 1424) {
		    ApeAtollNpcs.WAYMOTTIN = npc;
		}
		if(id == 1423) {
		    ApeAtollNpcs.BUNKWICKET = npc;
		}
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
	}
	
	public static void newWanderNPC(int id, int x, int y, int heightLevel) {
		Npc npc = new Npc(id);
		npc.setPosition(new Position(x, y, heightLevel));
		npc.setSpawnPosition(new Position(x, y, heightLevel));
		npc.setNeedsRespawn(true);
		npc.setMinWalk(new Position(x - Constants.NPC_WALK_DISTANCE, y - Constants.NPC_WALK_DISTANCE));
		npc.setMaxWalk(new Position(x + Constants.NPC_WALK_DISTANCE, y + Constants.NPC_WALK_DISTANCE));
		npc.setWalkType(WalkType.WALK);
		npc.setCurrentX(x);
		npc.setCurrentY(y);
		npc.setNeedsRespawn(true);
		World.register(npc);
	}

	public static void spawnNpc(Player player, Npc npc, boolean attack, boolean hintIcon) {
		int x = 0, y = 0;
		if (player.canMove(1, 0)) {
			x = 1;
			y = 0;
		} else if (player.canMove(-1, 0)) {
			x = -1;
			y = 0;
		} else if (player.canMove(0, 1)) {
			x = 0;
			y = 1;
		} else if (player.canMove(0, -1)) {
			x = 0;
			y = -1;
		}
		x = player.getPosition().getX() + x;
		y = player.getPosition().getY() + y;
		npc.setPosition(new Position(x, y, player.getPosition().getZ()));
		npc.setSpawnPosition(new Position(x, y, player.getPosition().getZ()));
		npc.setWalkType(Npc.WalkType.STAND);
		npc.setCurrentX(x);
		npc.setCurrentY(y);
		World.register(npc);
		player.setSpawnedNpc(npc);
		npc.setPlayerOwner(player.getIndex());
		npc.getUpdateFlags().sendFaceToDirection(player.getPosition());
		if (attack)
			CombatManager.attack(npc, player);
        else {
            npc.setFollowDistance(1);
            npc.setFollowingEntity(player);
        }
        if(hintIcon)
            player.getActionSender().createPlayerHints(1, (npc).getIndex());
	    if (npc.getNpcId() == 77) {
	    	npc.getUpdateFlags().sendGraphic(Graphic.lowGraphic(78));
	    }
	}

	public static boolean checkSpawn(Player player, int id) {
		return player.getSpawnedNpc() != null && !player.getSpawnedNpc().isDead() && player.getSpawnedNpc().getNpcId() == id;
	}
	public static void spawnStepAwayNpc(Entity entityToAttack, Npc npc, Position spawningPosition) {
		npc.setPosition(spawningPosition);
		npc.setSpawnPosition(spawningPosition);
		npc.setWalkType(Npc.WalkType.STAND);
		npc.setCurrentX(spawningPosition.getX());
		npc.setCurrentY(spawningPosition.getY());
		npc.setNeedsRespawn(false);
		World.register(npc);
		npc.getFollowing().stepAway();
		if (entityToAttack != null) {
		    CombatManager.attack(npc, entityToAttack);
		    npc.getUpdateFlags().sendFaceToDirection(entityToAttack.getPosition());
		}
    }
	public static void spawnNpc(Entity entityToAttack, Npc npc, Position spawningPosition, boolean hintIcon, String message) {
		npc.setPosition(spawningPosition);
		npc.setSpawnPosition(spawningPosition);
		npc.setWalkType(Npc.WalkType.STAND);
		npc.setCurrentX(spawningPosition.getX());
		npc.setCurrentY(spawningPosition.getY());
		npc.setNeedsRespawn(false);
		World.register(npc);
		if (entityToAttack != null) {
		    npc.setFollowingEntity(entityToAttack);
		    CombatManager.attack(npc, entityToAttack);
		    npc.getUpdateFlags().sendFaceToDirection(entityToAttack.getPosition());
		}
		if(entityToAttack != null && entityToAttack.isPlayer() && hintIcon)
		    ((Player)entityToAttack).getActionSender().createPlayerHints(1, (npc).getIndex());
		if(message != null)
		    npc.getUpdateFlags().sendForceMessage(message);
    }
	
	public static void spawnPlayerOwnedSpecificLocationNpc(Player player, Npc npc, Position spawningPosition, boolean hintIcon, String message) {
		npc.setPosition(spawningPosition);
		npc.setSpawnPosition(spawningPosition);
		npc.setWalkType(Npc.WalkType.STAND);
		npc.setCurrentX(spawningPosition.getX());
		npc.setCurrentY(spawningPosition.getY());
		npc.setNeedsRespawn(false);
		World.register(npc);
		player.setSpawnedNpc(npc);
		if(Misc.goodDistance(player.getPosition(), spawningPosition, 10)) {
		    npc.setPlayerOwner(player.getIndex());
		}
		if(hintIcon)
		    player.getActionSender().createPlayerHints(1, (npc).getIndex());
		if(message != null)
		    npc.getUpdateFlags().sendForceMessage(message);
	}
	
	public static void spawnPlayerOwnedAttackNpc(final Player player, final Npc npc, Position spawningPosition, boolean hintIcon, String message) {
		npc.setPosition(spawningPosition);
		npc.setSpawnPosition(spawningPosition);
		npc.setWalkType(Npc.WalkType.STAND);
		npc.setCurrentX(spawningPosition.getX());
		npc.setCurrentY(spawningPosition.getY());
		npc.setNeedsRespawn(false);
		World.register(npc);
		npc.setFollowingEntity(player);
		npc.getUpdateFlags().sendFaceToDirection(player.getPosition());
		player.setSpawnedNpc(npc);
		if(Misc.goodDistance(player.getPosition(), spawningPosition, 10)) {
		    npc.setPlayerOwner(player.getIndex());
		}
		if(hintIcon)
		    player.getActionSender().createPlayerHints(1, (npc).getIndex());
		if(message != null)
		    npc.getUpdateFlags().sendForceMessage(message);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		    @Override
		    public void execute(CycleEventContainer b) {
			b.stop();
		    }

		    @Override
		    public void stop() {
			CombatManager.attack(npc, player);
		    }
		}, 2);
		
	}
	
	public static void spawnNpc(int id, int x, int y, int heightLevel, boolean DontFollow, boolean DontWalk) {
		Npc npc = new Npc(id);
		npc.setPosition(new Position(x, y, heightLevel));
		npc.setSpawnPosition(new Position(x, y, heightLevel));
        npc.setDontFollow(DontFollow);
        npc.setDontWalk(DontWalk);
        if(!DontWalk)
        {
    		npc.setMinWalk(new Position(x - Constants.NPC_WALK_DISTANCE, y - Constants.NPC_WALK_DISTANCE));
    		npc.setMaxWalk(new Position(x + Constants.NPC_WALK_DISTANCE, y + Constants.NPC_WALK_DISTANCE));
            npc.setWalkType(WalkType.WALK);
        }else{
        	 npc.setWalkType(WalkType.STAND);
        }
		npc.setCurrentX(x);
		npc.setCurrentY(y);
        npc.setNeedsRespawn(false);
		World.register(npc);
	}
	
	public static void spawnNpc(int id, int x, int y, int heightLevel, boolean DontFollow, boolean DontWalk, boolean DontAttack) {
	    Npc npc = new Npc(id);
	    npc.setPosition(new Position(x, y, heightLevel));
	    npc.setSpawnPosition(new Position(x, y, heightLevel));
	    npc.setDontFollow(DontFollow);
	    npc.setDontWalk(DontWalk);
	    npc.setDontAttack(DontAttack);
	    if(!DontWalk) {
    		npc.setMinWalk(new Position(x - Constants.NPC_WALK_DISTANCE, y - Constants.NPC_WALK_DISTANCE));
    		npc.setMaxWalk(new Position(x + Constants.NPC_WALK_DISTANCE, y + Constants.NPC_WALK_DISTANCE));
		npc.setWalkType(WalkType.WALK);
	    }else{
        	npc.setWalkType(WalkType.STAND);
	    }
	    npc.setCurrentX(x);
	    npc.setCurrentY(y);
	    npc.setNeedsRespawn(false);
	    World.register(npc);
	}
	public static Npc spawnBasicNpc(Player player, int npcId, Position position, boolean attack) {
	    Npc npc = new Npc(npcId);
	    npc.setPosition(position);
	    npc.setSpawnPosition(position);
	    npc.setCurrentX(position.getX());
	    npc.setCurrentY(position.getY());
	    World.register(npc);
	    if(attack) {
		CombatManager.attack(npc, player);
	    } 
	    return npc;
	}
	public static void destroyNpc(Npc npc) {
		if(npc == null) {
		    return;
		}
		if (npc.getPlayerOwner() != null) {
			npc.getPlayerOwner().setSpawnedNpc(null);
		}
		npc.setVisible(false);
		Following.resetFollow(npc);
		World.unregister(npc);
	}

}
