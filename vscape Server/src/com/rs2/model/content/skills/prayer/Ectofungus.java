package com.rs2.model.content.skills.prayer;

import com.rs2.model.Position;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.objects.functions.TrapDoor;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;


public class Ectofungus {
    public static final Position DOWN_AT_SLIME = new Position(3683, 9888, 0);
    public static final Position UP_FROM_SLIME = new Position(3687, 9888, 1);
    public static final Position DOWN_FROM_FIRST_LEVEL = new Position(3675, 9888, 1);
    public static final Position UP_FROM_FIRST_LEVEL = new Position(3671, 9888, 2);
    public static final Position DOWN_FROM_SECOND_LEVEL = new Position(3688, 9888, 2);
    public static final Position UP_FROM_SECOND_LEVEL = new Position(3692, 9888, 3);
    public static final Position UP_FROM_FIRST_LEVEL_SHORTCUT = new Position(3670, 9888, 3);
    public static final Position DOWN_FROM_TRAPDOOR = new Position(3669, 9888, 3);
    public static final Position UP_FROM_LADDER = new Position(3654, 3519, 0);
    
    public static final int X = 0, Y = 1;
    public static final int[] ECTOFUNGUS_OBJ = {3658, 3518};
    public static final int[] TRAPDOOR_OBJ = {3653, 3519};
    public static final int[] STAIRS_UP_OBJ = {3666, 3518};
    public static final int[] STAIRS_DOWN_OBJ = {3666, 3520};
    public static final int[] STAIRS_UP_TO_SECOND_OBJ = {3689, 9887};
    public static final int[] STAIRS_UP_TO_FIRST_OBJ = {3672, 9887};
    public static final int[] STAIRS_UP_FROM_SLIME_OBJ = {3684, 9887};
    public static final int[] STAIRS_DOWN_TO_SECOND_OBJ = {3689, 9887};
    public static final int[] STAIRS_DOWN_TO_FIRST_OBJ = {3672, 9887};
    public static final int[] STAIRS_DOWN_TO_SLIME_OBJ = {3684, 9887};
    public static final int[] LADDER_UP_OBJ = {3668, 9888};
    public static final int[] BONEGRINDER_OBJ = {3659, 3525};
    public static final int[] BONEGRINDER_BIN_OBJ = {3658, 3525};
    public static final int[] BARRIER_NORTH_OBJ = {3659, 3508};
    
    public static final int ECTOFUNGUS = 5282;
    public static final int TRAPDOOR = 5267;
    public static final int STAIRS_UP = 5280;
    public static final int STAIRS_DOWN = 5281;
    public static final int STAIRS_UP_SLIME = 5262;
    public static final int STAIRS_DOWN_SLIME = 5263;
    public static final int LADDER_UP = 5264;
    public static final int JUMP_DOWN = 9308;
    public static final int BONEGRINDER = 11163;
    public static final int BONEGRINDER_BIN = 11164;
    public static final int BARRIER_NORTH = 5259;
    public static final int SLIME = 5461;
    public static final int SLIME_2 = 5462;
    
    public static final int[] BONEMEAL_BONES = {4255, 526};
    public static final int[] BONEMEAL_BAT_BONES = {4256, 530};
    public static final int[] BONEMEAL_BIG_BONES = {4257, 532};
    public static final int[] BONEMEAL_BURNT_BONES = {4258, 538};
    //public static final int[] BONEMEAL_BURNT_JOGRE = {4259, 3127};
    public static final int[] BONEMEAL_BABY_DRAGON = {4260, 534};
    public static final int[] BONEMEAL_DRAGON_BONES = {4261, 536};
    public static final int[] BONEMEAL_WOLF_BONES = {4262, 2859};
    //public static final int[] BONEMEAL_SMALL_NINJA= {4263, 3179};
    //public static final int[] BONEMEAL_MEDIUM_NINJA = {4264, 3180};
    //public static final int[] BONEMEAL_GORILLA_BONES = {4265, 3181};
    //public static final int[] BONEMEAL_BEARDED_GORILLA = {4266, 3182};
    //public static final int[] BONEMEAL_MONKEY_BONES = {4267, 3183};
    //public static final int[] BONEMEAL_SMALL_ZOMBIE_MONKEY = {4268, 3185};
    //public static final int[] BONEMEAL_LARGE_ZOMBIE_MONKEY = {4269, 3186};
    //public static final int[] BONEMEAL_SKELETON_BONES = {4270, 3187}; //may not be right.
    public static final int[] BONEMEAL_JOGRE_BONES = {4271, 3125};
    
    public static final int BUCKET = 1925;
    public static final int BUCKET_OF_SLIME = 4286;
    
    public static double getExp(int bone) {
	return BoneBurying.getBone(bone).getXp() * 4;
    }
    
    public static boolean doObjectFirstClick(final Player player, int object, int x, int y) {
	switch(object) {
	    case ECTOFUNGUS:
		if(x == ECTOFUNGUS_OBJ[X] && y == ECTOFUNGUS_OBJ[Y]) {
		    return true;
		}
	    case STAIRS_UP_SLIME:
		if(x == STAIRS_UP_FROM_SLIME_OBJ[X]) {
		    player.teleport(UP_FROM_SLIME);
		    return true;
		}
		else if(x == STAIRS_UP_TO_FIRST_OBJ[X]) {
		    player.teleport(UP_FROM_FIRST_LEVEL);
		    return true;
		}
		else if(x == STAIRS_UP_TO_SECOND_OBJ[X]) {
		    player.teleport(UP_FROM_SECOND_LEVEL);
		    return true;
		}
	    case STAIRS_DOWN_SLIME:
		if(x == STAIRS_DOWN_TO_SLIME_OBJ[X]) {
		    player.teleport(DOWN_AT_SLIME);
		    return true;
		}
		else if(x == STAIRS_DOWN_TO_FIRST_OBJ[X]) {
		    player.teleport(DOWN_FROM_FIRST_LEVEL);
		    return true;
		}
		else if(x == STAIRS_DOWN_TO_SECOND_OBJ[X]) {
		    player.teleport(DOWN_FROM_SECOND_LEVEL);
		    return true;
		}
	    case LADDER_UP:
		if(x == LADDER_UP_OBJ[X]) {
		    Ladders.climbLadder(player, UP_FROM_LADDER);
		    return true;
		}
	    case TRAPDOOR:
		player.getUpdateFlags().sendAnimation(827);
		
		GameObject o = new GameObject(1754, TRAPDOOR_OBJ[X], TRAPDOOR_OBJ[Y], 0, 1, 10, TRAPDOOR, 999999);
		ObjectHandler.getInstance().removeObject(TRAPDOOR, TRAPDOOR_OBJ[X], TRAPDOOR_OBJ[Y], 22);
		ObjectHandler.getInstance().addObject(o, true);
		return true;
	}
	return false;
    }
    
    public static boolean doItemOnObject(Player player, int object, int item) {
	switch(object) {
	    case SLIME:
	    case SLIME_2:
		if(item == BUCKET) {
		    player.getUpdateFlags().sendAnimation(827);
		    player.getActionSender().sendMessage("You fill your bucket with some slime.");
		    player.getInventory().replaceItemWithItem(new Item(BUCKET), new Item(BUCKET_OF_SLIME));
		    return true;
		}
	}
	return false;
    }
}
