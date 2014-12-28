package com.rs2.model.content.minigames.magetrainingarena;

import com.rs2.model.Position;

public class MageGameConstants {
	//General constants
	public static final Position LEAVING_POSITION = new Position(3363, 3318, 0);
	public static final int ALCHEMIST_LEVEL = 21;
	public static final int ENCHANTING_LEVEL = 7;
	public static final int TELEKINETIC_LEVEL = 33;
	public static final int GRAVEYARD_LEVEL = 15;

	public static final int MAX_ALCHEMY_POINT = 8000;
	public static final int MAX_ENCHANTING_POINT = 16000;
	public static final int MAX_TELEKINETIC_POINT = 4000;
	public static final int MAX_GRAVEYARD_POINT = 4000;
	
	//Enchanting variables
	public static String bonusItemEnchantingChamber;
	
	//Alchemist variables
	public static int RUNE_LONGSWORD_PRICE;
	public static int EMERALD_PRICE;
	public static int ADAMANT_MED_HELM_PRICE;
	public static int ADAMANT_KITE_PRICE;
	public static int LEATHER_BOOTS_PRICE;
	public static int FREE_ALCH_ITEM;
	
	//Graveyard enum
	public enum BonePile {
	    B0(10725, 0, new Position(3346, 9647, 1)),
	    B1(10725, 0, new Position(3344, 9650, 1)),
	    B2(10725, 0, new Position(3347, 9650, 1)),
	    B3(10725, 0, new Position(3345, 9655, 1)),
	    B4(10725, 0, new Position(3355, 9658, 1)),
	    B5(10725, 0, new Position(3361, 9654, 1)),
	    B6(10725, 0, new Position(3361, 9651, 1)),
	    B7(10725, 0, new Position(3357, 9652, 1)),
	    B8(10725, 0, new Position(3355, 9649, 1)),
	    B9(10725, 0, new Position(3350, 9650, 1)),
	    B10(10725, 0, new Position(3353, 9646, 1)),
	    B11(10725, 0, new Position(3350, 9643, 1)),
	    B12(10725, 0, new Position(3374, 9650, 1)),
	    B13(10725, 0, new Position(3374, 9646, 1)),
	    B14(10725, 0, new Position(3377, 9645, 1)),
	    B15(10725, 0, new Position(3382, 9647, 1)),
	    B16(10725, 0, new Position(3381, 9653, 1)),
	    B17(10725, 0, new Position(3379, 9658, 1)),
	    B18(10725, 0, new Position(3375, 9658, 1)),
	    B19(10725, 0, new Position(3372, 9657, 1)),
	    B20(10725, 0, new Position(3375, 9633, 1)),
	    B21(10725, 0, new Position(3375, 9628, 1)),
	    B22(10725, 0, new Position(3371, 9627, 1)),
	    B23(10725, 0, new Position(3367, 9624, 1)),
	    B24(10725, 0, new Position(3375, 9620, 1)),
	    B25(10725, 0, new Position(3380, 9623, 1)),
	    B26(10725, 0, new Position(3380, 9630, 1)),
	    B27(10725, 0, new Position(3382, 9631, 1)),
	    B28(10725, 0, new Position(3357, 9628, 1)),
	    B29(10725, 0, new Position(3353, 9628, 1)),
	    B30(10725, 0, new Position(3352, 9637, 1)),
	    B31(10725, 0, new Position(3350, 9634, 1)),
	    B32(10725, 0, new Position(3347, 9633, 1)),
	    B33(10725, 0, new Position(3344, 9631, 1)),
	    B34(10725, 0, new Position(3346, 9626, 1)),
	    B35(10725, 0, new Position(3347, 9622, 1)),
	    B36(10725, 0, new Position(3354, 9622, 1)),
	    B37(10725, 0, new Position(3355, 9620, 1));
	    
	    public int currentId;
	    public int pickCount;
	    public Position pos;
	    
	    BonePile(int id, int count, Position pos) {
		this.currentId = id;
		this.pickCount = count;
		this.pos = pos;
	    }
	    
	    public static BonePile forPosition(Position pos) {
		for(BonePile b : BonePile.values()) {
		    if(b.pos.equals(pos)) {
			return b;
		    }
		}
		return null;
	    }
	    
	    public int getPickCount() {
		return this.pickCount;
	    }
	    public void setPickCount(int set) {
		this.pickCount = set;
	    }
	    public int getCurrentId() {
		return this.currentId;
	    }
	    public void setCurrentId(int set) {
		this.currentId = set;
	    }
	    public Position getPosition() {
		return this.pos;
	    }
	}
	
	//Telekinetic variables
	public static final Position[] MAZE_POSITIONS = {new Position(3336, 9718, 0), new Position(3379, 9716, 0), new Position(3374, 9696, 0), new Position(3354, 9690, 0), new Position(3362, 9713, 1), new Position(3378, 9706, 1), new Position(3382, 9698, 1), new Position(3355, 9693, 1), new Position(3368, 9680, 2), new Position(3359, 9701, 2)};
	public static final Position[] EXIT_POSITIONS = {new Position(3335, 9718, 0), new Position(3380, 9716, 0), new Position(3374, 9697, 0), new Position(3353, 9690, 0), new Position(3363, 9713, 1), new Position(3377, 9706, 1), new Position(3383, 9698, 1), new Position(3356, 9693, 1), new Position(3369, 9680, 2), new Position(3360, 9701, 2),};
	public static final Position[] STATUE_POSITIONS = {new Position(3343, 9705, 0), new Position(3368, 9712, 0), new Position(3373, 9678, 0), new Position(3343, 9680, 0), new Position(3350, 9717, 1), new Position(3374, 9713, 1), new Position(3376, 9686, 1), new Position(3351, 9684, 1), new Position(3348, 9674, 2), new Position(3346, 9718, 2)};
	public static final Position[] GUARDIAN_POSITIONS = {new Position(3350, 9718, 0),  new Position(3372, 9722, 0), new Position(3373, 9689, 0), new Position(3348, 9687, 0), new Position(3353, 9715, 1), new Position(3387, 9713, 1), new Position(3376, 9699, 1), new Position(3360, 9690, 1), new Position(3364, 9681, 2), new Position(3356, 9708, 2)};
	public static final Position[] STATUE_ARRIVAL_POSITIONS = {new Position(3347, 9714, 0), new Position(3367, 9720, 0), new Position(3375, 9682, 0), new Position(3342, 9684, 0), new Position(3341, 9708, 1), new Position(3383, 9713, 1), new Position(3385, 9686, 1), new Position(3353, 9680, 1), new Position(3339, 9683, 2), new Position(3345, 9718, 2)};

	public static final Position[] UPPER_LEFT_CORNER = {new Position(3337, 9715, 0), new Position(3365, 9721, 0), new Position(3368, 9683, 0), new Position(3337, 9685, 0), new Position(3340, 9718, 1), new Position(3373, 9723, 1), new Position(3375, 9687, 1), new Position(3345, 9690, 1), new Position(3338, 9684, 2), new Position(3342, 9719, 2)};
	public static final Position[] BOTTOM_LEFT_CORNER = {new Position(3337, 9704, 0), new Position(3365, 9710, 0), new Position(3368, 9672, 0), new Position(3337, 9674, 0), new Position(3340, 9707, 1), new Position(3373, 9712, 1), new Position(3375, 9676, 1), new Position(3345, 9679, 1), new Position(3338, 9673, 2), new Position(3342, 9708, 2)};
	public static final Position[] UPPER_RIGHT_CORNER = {new Position(3348, 9715, 0), new Position(3376, 9721, 0), new Position(3379, 9683, 0), new Position(3348, 9685, 0), new Position(3351, 9715, 1), new Position(3384, 9723, 1), new Position(3386, 9687, 1), new Position(3356, 9690, 1), new Position(3349, 9684, 2), new Position(3353, 9719, 2)};
	public static final Position[] BOTTOM_RIGHT_CORNER = {new Position(3348, 9704, 0), new Position(3376, 9710, 0), new Position(3379, 9672, 0), new Position(3348, 9674, 0), new Position(3351, 9707, 1), new Position(3384, 9712, 1), new Position(3386, 9676, 1), new Position(3356, 9679, 1), new Position(3349, 9673, 2), new Position(3353, 9708, 2)};

	public static Position[] CENTER_SQUARE = new Position[10];

}
