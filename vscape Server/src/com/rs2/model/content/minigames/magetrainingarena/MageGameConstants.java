package com.rs2.model.content.minigames.magetrainingarena;

import com.rs2.model.Position;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 01/01/12 Time: 02:52 To change
 * this template use File | Settings | File Templates.
 */
public class MageGameConstants {
    
	public static final Position LEAVING_POSITION = new Position(3363, 3318, 0);
	public static final int ALCHEMIST_LEVEL = 21;
	public static final int ENCHANTING_LEVEL = 7;
	public static final int TELEKINETIC_LEVEL = 33;
	public static final int GRAVEYARD_LEVEL = 15;

	public static final int MAX_ALCHEMY_POINT = 8000;
	public static final int MAX_ENCHANTING_POINT = 16000;
	public static final int MAX_TELEKINETIC_POINT = 4000;
	public static final int MAX_GRAVEYARD_POINT = 4000;
	
	public static String bonusItemEnchantingChamber;
	
	public static int RUNE_LONGSWORD_PRICE;
	public static int EMERALD_PRICE;
	public static int ADAMANT_MED_HELM_PRICE;
	public static int ADAMANT_KITE_PRICE;
	public static int LEATHER_BOOTS_PRICE;
	
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

}
