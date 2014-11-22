package com.rs2.model.content.quests.MonkeyMadness;

import com.rs2.model.players.Player;

public class ApeAtoll {
    private static final int[] MAIN_COORDS = {2755, 2784};
    
    public enum GreeGreeData {
	SMALL_NINJA_GREEGREE(4024, 3179, 1480, 1386, 1380),
	MEDIUM_NINJA_GREEGREE(4025, 3180, 1481, 1386, 1380),
	GORILLA_GREEGREE (4026, 3181, 1482, 1401, 1399),
	BEARED_GORILLA_GREEGREE (4027, 3182, 1483, 1401, 1399),
	MYSTERIOUS_GREEGREE (4028, -1, 1484, 1401, 1399), //bonesId?
	SMALL_ZOMBIE_GREEGREE (4029, 3185, 1485, 1386, 1382),
	LARGE_ZOMBIE_GREEGREE (4030, 3186, 1486, 1386, 1382),
	MONKEY_GREEGREE (4031, 3183, 1487, 222, 219);
    
	private int itemId;
	private int bonesId;
	private int transformId;
	private int standAnim;
	private int walkAnim;
	
	GreeGreeData(int itemId, int bonesId, int transformId, int standAnim, int walkAnim) {
	    this.itemId = itemId;
	    this.bonesId = bonesId;
	    this.transformId = transformId;
	    this.standAnim = standAnim;
	    this.walkAnim = walkAnim;
	}
	
	public static GreeGreeData forItemId(int itemId) {
	    for(GreeGreeData g : GreeGreeData.values()) {
		if(g.itemId == itemId) {
		    return g;
		}
	    }
	    return null;
	}
	
	public static int talismanForBones(int itemId) {
	    for(GreeGreeData g : GreeGreeData.values()) {
		if(g.bonesId == itemId) {
		    return g.itemId;
		}
	    }
	    return -1;
	}
	
	public int getItemId() {
	    return this.itemId;
	}
	
	public int getBonesId() {
	    return this.bonesId;
	}
	
	public int getTransformId() {
	    return this.transformId;
	}
	
	public int getStandAnim() {
	    return this.standAnim;
	}
	
	public int getWalkAnim() {
	    return this.walkAnim;
	}

    }
    
    public static boolean handleGreeGreeEquip(final Player player, int itemId) {
	if(GreeGreeData.forItemId(itemId) != null) {
	    if(player.onApeAtoll()) {
		player.getEquipment().equip(player.getSlot());
		GreeGreeData g = GreeGreeData.forItemId(itemId);
		player.transformNpc = g.getTransformId();
		player.setStandAnim(g.getStandAnim());
		player.setWalkAnim(g.getWalkAnim());
		player.setRunAnim(g.getWalkAnim());
		player.getUpdateFlags().setUpdateRequired(true);
		return true;
	    } else {
		player.getActionSender().sendMessage("You must be on Ape Atoll to feel the effects of the greegree.");
		return true;
	    }
	}

	return false;
    }
}
