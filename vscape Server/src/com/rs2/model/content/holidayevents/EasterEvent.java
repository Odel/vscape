package com.rs2.model.content.holidayevents;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.consumables.Food.FoodData;
import com.rs2.model.content.randomevents.EventsConstants;
import com.rs2.model.content.treasuretrails.ClueScroll;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.Npc.WalkType;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.npcs.drop.NpcDropController;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;

//not a great class but it shall do

public class EasterEvent extends Tick {

	private static EasterEvent easterEvent;
	
	public static final int[] EASTEREGGS = { 7928, 7929, 7930, 7931, 7932, 7933 };
	private static final int EGG_CHANCE = 5;
	
	private static final int[] RABBIT_IDS = { 1192, 1193, 1194 };
	private static final int RABBIT_HOLE_ID = 12202;
	private static final Position[] HOLE_POSITIONS = {
		new Position(2951,3265),
		new Position(2955,3265),
		new Position(2958,3268),
		new Position(2954,3270),
		new Position(2958,3273),
		new Position(2962,3272),
		new Position(2966,3270),
		new Position(2963,3266),
		new Position(2963,3264),
		new Position(2967,3272),
		new Position(2955,3276),
		new Position(2956,3280),
		new Position(2961,3279),
		new Position(2965,3279),
		new Position(2972,3277),
		new Position(2970,3269)
	};
	
	private static final int EASTER_MINX = 2946, EASTER_MAXX = 2978;
	private static final int EASTER_MINY = 3263, EASTER_MAXY = 3287;
	
	private static final int MAX_RABBIT = 10;
	private static List<Npc> rabbits = new ArrayList();
	
	private static final Position[] ChildPositions = {
		new Position(2972,3271),
		new Position(2971,3269),
		new Position(2972,3267),
		new Position(2972,3265),
		new Position(2971,3262),
		new Position(2968,3260)
	};
	private static final int[] CHILDNPCID = { 2872, 2873, 2874, 2875, 2876, 2877 };
	
	public EasterEvent() {
		super(1);
		setTickDelay(4);
		World.getTickManager().submit(this);
	}
	
	public static void init() {
		easterEvent = new EasterEvent();
		for(Position ChildPos : ChildPositions)
		{
			Npc child = new Npc(CHILDNPCID[Misc.randomMinusOne(CHILDNPCID.length)]);
			child.setPosition(ChildPos);
			child.setSpawnPosition(ChildPos);
			child.setCurrentX(ChildPos.getX());
			child.setCurrentY(ChildPos.getY());
			child.setWalkType(WalkType.STAND);
			World.register(child);
		}
	}

	@Override
	public void execute() 
	{
		spawnRabbitHole();
	}
	
	private void spawnRabbitHole()
	{
		if(rabbits.size() >= MAX_RABBIT)
		{
			NpcLoader.destroyNpc(rabbits.get(0));
			rabbits.remove(0);
		}
		Position newPos = HOLE_POSITIONS[Misc.randomMinusOne(HOLE_POSITIONS.length)];
		new GameObject(RABBIT_HOLE_ID, newPos.getX(), newPos.getY(), 0, 0, 22, 6951, 15);
		Npc rabbit = new Npc(RABBIT_IDS[Misc.randomMinusOne(RABBIT_IDS.length)]);
		rabbit.setPosition(new Position(newPos.getX(), newPos.getY(), 0));
		rabbit.setSpawnPosition(new Position(newPos.getX(), newPos.getY(), 0));
		rabbit.setCurrentX(newPos.getX());
		rabbit.setCurrentY(newPos.getY());
		rabbit.setMinWalk(HOLE_POSITIONS[0]);
		rabbit.setMaxWalk(HOLE_POSITIONS[HOLE_POSITIONS.length-1]);
		rabbit.setWalkType(WalkType.WALK);
		World.register(rabbit);
		rabbits.add(rabbit);
	}
	
	public static boolean onRabbitHole(Player player)
	{
		if(player.getPosition().getX() >= EASTER_MINX && player.getPosition().getX() <= EASTER_MAXX 
		&& player.getPosition().getY() >= EASTER_MINY && player.getPosition().getY() <= EASTER_MAXY)
		{
			GameObject p = ObjectHandler.getInstance().getObject(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
			if (p != null && p.getDef().getId() == RABBIT_HOLE_ID) 
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		return false;
	}
	
	public static boolean digRabbitHole(Player player)
	{
		if(onRabbitHole(player))
		{
			return giveEgg(player);
		}
		else
		{
			return false;
		}
	}
	
	private static boolean giveEgg(Player player)
	{
		if (Misc.random(EGG_CHANCE) == 0) {
			int eggId = EASTEREGGS[Misc.randomMinusOne(EASTEREGGS.length)];
			Item eggItem = new Item(eggId,1);
			player.getInventory().addItem(eggItem);
			player.getDialogue().sendStatement("You've found an Easter Egg!", eggId);
			return true;
		} else {
			return false;
		}
	}
}
