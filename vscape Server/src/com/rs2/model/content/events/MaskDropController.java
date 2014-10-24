package com.rs2.model.content.events;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.content.minigames.MinigameAreas.Area;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemManager;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;

public class MaskDropController {
	
	private static ArrayList<Position> possiblePositions = new ArrayList<Position>();
	private static Area[] areas = {
		new Area(2713, 2759, 3149, 3170, 0),
		new Area(2816, 2897, 3011, 3075, 0),
		new Area(3395, 3452, 2883, 2941, 0),
		new Area(3521, 3641, 3459, 3509, 0),
		new Area(3298, 3328, 3386, 3446, 0),
		new Area(2372, 2452, 3360, 3370, 0),
		new Area(2575, 2625, 3115, 3135, 0),
		new Area(3122, 3162, 3763, 3838, 0),
		new Area(3199, 3319, 3692, 3755, 0),
		new Area(3169, 3319, 3545, 3580, 0)
	};
	
	public static Position currentPosition = new Position(0,0,0);
	private static Position lastPosition = new Position(0,0,0);
	
	private final static int[] masks = { 1053, 1055, 1057};
	private static GroundItem currentMask;
	
	public static int spawnTime = 30; // in minutes
	private static int curSpawnTime = 0;
	public static int lifeTime = 5; // in minutes
	private static int curLifeTime = 0;
	private final static int minDistance = 100;
	
	public static void initController(){
		BufferedReader Checker = null;
		int posCount = 0;
		try {
			String Data;
			Checker = new BufferedReader(new FileReader("./data/world/hweenspawns.txt"));
			while ((Data = Checker.readLine()) != null) {
				if (Data.startsWith("#")) {
					continue;
				}
				String[] args = Data.split(",");
                possiblePositions.add(new Position(Integer.parseInt(args[0]),Integer.parseInt(args[1]),Integer.parseInt(args[2])));
                posCount++;
			}
			Checker.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Loaded " + posCount + " mask drop positions.");
		if(possiblePositions.size() <= 0)
		{
			System.out.println("MaskDropController failed to load");
			return;
		}
		process();
	}
	
	public static void spawnMask(){
		if(currentMask != null)
			return;
		while(currentPosition.withinDistance(lastPosition, minDistance))
		{
			currentPosition = getRandomPosition();
			if(!currentPosition.withinDistance(lastPosition, minDistance)){
				break;
			}
		}
		if(currentPosition != null)
			currentMask = new GroundItem(getRandomMask(), currentPosition, ((lifeTime+2) * 100), false);  
		if(currentMask != null){
			GroundItemManager.getManager().dropItem(currentMask);
			lastPosition = currentPosition;
			curSpawnTime = 0;
			curLifeTime = 0;
	    	for(Player p : World.getPlayers())
	    	{
	    		if(p == null)
	    		{
	    			continue;
	    		}
	    		p.getActionSender().sendMessage("@red@A h'ween mask spawned in the world, try to find it!");
	    	}
		}
	}
	
	public static void removeMask() {
		if(currentMask == null)
			return;
		GroundItemManager.getManager().destroyItem(currentMask);
		currentMask = null;
		curSpawnTime = 0;
		curLifeTime = 0;
    	for(Player p : World.getPlayers())
    	{
    		if(p == null)
    		{
    			continue;
    		}
    		p.getActionSender().sendMessage("@red@No one found the h'ween mask, Maybe next time!");
    	}
	}
	
	public static boolean itemPickupHandling(final Player player, final int id, final Position pos) {
		switch(id)
		{
			case 1053 :
			case 1055 :
			case 1057 :
			if(currentMask != null && comparePosition(pos))
			{
		    	for(Player p : World.getPlayers())
		    	{
		    		if(p == null)
		    		{
		    			continue;
		    		}
		    		p.getActionSender().sendMessage("@red@Someone has found the h'ween mask!");
		    	}
				ItemManager.getInstance().pickupItem(player, player.getClickId(), new Position(player.getClickX(), player.getClickY(), player.getPosition().getZ()));
				curSpawnTime = 0;
				curLifeTime = 0;
				currentMask = null;
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	private static boolean comparePosition(Position pos){
		return pos.getX() == currentMask.getPosition().getX() && pos.getY() == currentMask.getPosition().getY() && pos.getZ() == currentMask.getPosition().getZ();
	}
	
	private static Item getRandomMask(){
		return new Item(masks[Misc.random(masks.length-1)], 1);
	}
	
	private static Position getRandomPosition(){
		if (50 >= (new Random().nextDouble() * 100))
		{
			return possiblePositions.get(Misc.random(possiblePositions.size()-1));
		}else{
			return MinigameAreas.randomPosition(areas[Misc.random(areas.length-1)]);
		}
	}
	
	private static void process(){
		World.submit(new Tick(100) {
		    @Override public void execute() {
		    	if(currentMask == null){
		    		curSpawnTime++;
		    		if(curSpawnTime >= spawnTime)
		    		{
		    			spawnMask();
		    			process();
		    			this.stop();
		    			return;
		    		}
		    	}else{
		    		curLifeTime++;
		    		if(curLifeTime >= lifeTime)
		    		{
		    			removeMask();
		    			process();
		    			this.stop();
		    			return;
		    		}
		    	}
		    }
	    });
	}
}
