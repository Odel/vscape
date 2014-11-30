package com.rs2.model.content.skills.magic;

import com.rs2.Constants;
import com.rs2.model.content.skills.Skill;
import static com.rs2.model.content.skills.runecrafting.TabHandler.checkStaffs;
import com.rs2.model.players.Player;
import com.rs2.model.players.container.inventory.Inventory;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;


public class BoltEnchanting {
    public static final int ENCHANT_ANIM = 4462;
    public static final int ENCHANT_GFX = 759;
    
    public static final int AIR_RUNE = 556;
    public static final int EARTH_RUNE = 557;
    public static final int FIRE_RUNE = 554;
    public static final int WATER_RUNE = 555;
    public static final int LAW_RUNE = 563;
    public static final int NATURE_RUNE = 561;
    public static final int COSMIC_RUNE = 564;
    public static final int BLOOD_RUNE = 565;
    public static final int DEATH_RUNE = 560;
    public static final int SOUL_RUNE = 566;
    public static final int MIND_RUNE = 558;
    
    public enum BoltData {
	OPAL(879, 9236, 4, 9d, new int[][]{{AIR_RUNE, 2}, {COSMIC_RUNE, 1}}, -1),
	SAPPHIRE(9337, 9240, 7, 17d, new int[][]{{WATER_RUNE, 1}, {MIND_RUNE, 1}, {COSMIC_RUNE, 1}}, -1), 
	JADE(9335, 9237, 14, 19d, new int[][]{{EARTH_RUNE, 2}, {COSMIC_RUNE, 1}}, -1),
	PEARL(880, 9238, 24, 29d, new int[][]{{WATER_RUNE, 2},{COSMIC_RUNE, 1}}, -1),
	EMERALD(9338, 9241, 27, 37d, new int[][]{{AIR_RUNE, 3}, {NATURE_RUNE, 1}, {COSMIC_RUNE, 1}}, -1),
	TOPAZ(9336, 9239, 29, 33d, new int[][]{{FIRE_RUNE, 2}, {COSMIC_RUNE, 1}}, -1),
	RUBY(9339, 9242, 49, 59d, new int[][]{{FIRE_RUNE, 5}, {BLOOD_RUNE, 1}, {COSMIC_RUNE, 1}}, -1),
	DIAMOND(9340, 9243, 57, 67d, new int[][]{{EARTH_RUNE, 10}, {LAW_RUNE, 2}, {COSMIC_RUNE, 1}}, -1),
	DRAGONSTONE(9341, 9244, 68, 78d, new int[][]{{EARTH_RUNE, 15}, {SOUL_RUNE, 1}, {COSMIC_RUNE, 1}}, -1),
	ONYX(9342, 9245, 87, 97d, new int[][]{{FIRE_RUNE, 20}, {DEATH_RUNE, 1}, {COSMIC_RUNE, 1}}, -1);
	
	private int originalId;
	private int enchantedId;
	private int levelReq;
	private double exp;
	private int[][] runesRequired;
	private int buttonId;
	
	BoltData(int original, int enchanted, int req, double exp, int[][] runeReq, int button) {
	    this.originalId = original;
	    this.enchantedId = enchanted;
	    this.levelReq = req;
	    this.exp = exp;
	    this.runesRequired = runeReq;
	    this.buttonId = button;
	}
	
	public static BoltData forOriginalId(int id) {
	    for(BoltData b : BoltData.values()) {
		if(b.originalId == id) {
		    return b;
		}
	    }
	    return null;
	}
	
	public static BoltData forButtonId(int id) {
	    for(BoltData b : BoltData.values()) {
		if(b.buttonId == id) {
		    return b;
		}
	    }
	    return null;
	}
	
	public int[][] getRunes() {
	    return this.runesRequired;
	}
	
	public int getOriginalId() {
	    return this.originalId;
	}
	
	public int getLevelReq() {
	    return this.levelReq;
	}
    }
    
    public static void enchantBolts(final Player player, final int boltId, final int count) {
	player.getActionSender().removeInterfaces();
	final BoltData b = BoltData.forOriginalId(boltId);
	if(b == null) {
	    return;
	}
	if(player.getSkill().getLevel()[Skill.MAGIC] < b.getLevelReq()) {
	    player.getDialogue().sendStatement("You need a Magic level of " + b.getLevelReq() + " to enchant these bolts.");
	    return;
	}
	final int task = player.getTask();
	player.setSkilling(new CycleEvent() {
	int amnt = count;

	    @Override
	    public void execute(CycleEventContainer container) {
		int weaponId = player.getEquipment().getId(Constants.WEAPON);
		if (!player.checkTask(task) || !checkRunes(b, player) || amnt == 0) {
		    container.stop();
		    return;
		}
		if (!player.getInventory().playerHasItem(boltId, 10)) {
		    player.getActionSender().sendMessage("You have run out of bolts to enchant!");
		    container.stop();
		    return;
		}
		player.getUpdateFlags().sendAnimation(ENCHANT_ANIM);
		player.getUpdateFlags().sendGraphic(ENCHANT_GFX);
		player.getActionSender().sendMessage("You enchant the " + new Item(boltId).getDefinition().getName().toLowerCase() + ".");
		if(checkStaffs(b.getRunes()[0][0], weaponId)) {
		    player.getInventory().removeItem(new Item(b.getRunes()[0][0], b.getRunes()[0][1]));
		}
		if(checkStaffs(b.getRunes()[1][0], weaponId)) {
		    player.getInventory().removeItem(new Item(b.getRunes()[1][0], b.getRunes()[1][1]));
		}
		if(b.getRunes().length > 2) {
		    player.getInventory().removeItem(new Item(b.getRunes()[2][0], b.getRunes()[2][1]));
		}
		player.getInventory().removeItem(new Item(b.originalId, 10));
		player.getInventory().addItem(new Item(b.enchantedId, 10));
		amnt--;
	    }
	    @Override
	    public void stop() {
		player.resetAnimation();
	    }
	});
	CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 3);
    }
    
    public static boolean checkRunes(BoltData b, Player player) {
	Inventory i = player.getInventory();
	int id = player.getEquipment().getId(Constants.WEAPON);
	if(b.getRunes().length == 3) {
	    if (i.playerHasItem(b.getRunes()[0][0], b.getRunes()[0][1]) && i.playerHasItem(b.getRunes()[1][0], b.getRunes()[1][1]) && i.playerHasItem(b.getRunes()[2][0], b.getRunes()[2][1])) {
		return true;
	    } else if (checkStaffs(b.getRunes()[0][0], id) && i.playerHasItem(b.getRunes()[1][0], b.getRunes()[1][1]) && i.playerHasItem(b.getRunes()[2][0], b.getRunes()[2][1])) {
		return true;
	    }
	    return i.playerHasItem(b.getRunes()[0][0], b.getRunes()[0][1]) && checkStaffs(b.getRunes()[1][0], id) && i.playerHasItem(b.getRunes()[2][0], b.getRunes()[2][1]);
	}
	else if(b.getRunes().length == 2) {
	    if (i.playerHasItem(b.getRunes()[0][0], b.getRunes()[0][1]) && i.playerHasItem(b.getRunes()[1][0], b.getRunes()[1][1])) {
		return true;
	    } else if (checkStaffs(b.getRunes()[0][0], id) && i.playerHasItem(b.getRunes()[1][0], b.getRunes()[1][1])) {
		return true;
	    }
	    return i.playerHasItem(b.getRunes()[1][0], b.getRunes()[1][1]) && checkStaffs(b.getRunes()[0][0], id);
	}
	    return false;
    }
}
