package com.rs2.model.content.skills.magic;

import com.rs2.model.content.combat.attacks.SpellAttack;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemManager;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.requirement.ExecutableRequirement;
import com.rs2.util.requirement.Requirement;
import com.rs2.util.requirement.RuneRequirement;
import com.rs2.util.requirement.SkillLevelRequirement;


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
	OPAL(879, 9236, 4, 9d, new Item[]{new Item(COSMIC_RUNE,1), new Item(AIR_RUNE,2)}, 95022, 24180),
	SAPPHIRE(9337, 9240, 7, 17d, new Item[]{new Item(COSMIC_RUNE,1), new Item(WATER_RUNE,1), new Item(MIND_RUNE,1)}, 95023, 24188), 
	JADE(9335, 9237, 14, 19d, new Item[]{new Item(COSMIC_RUNE,1), new Item(EARTH_RUNE,2)}, 95024, 24205),
	PEARL(880, 9238, 24, 29d, new Item[]{new Item(COSMIC_RUNE,1), new Item(WATER_RUNE,2)}, 95025, 24220),
	EMERALD(9338, 9241, 27, 37d, new Item[]{new Item(COSMIC_RUNE,1), new Item(AIR_RUNE,3), new Item(NATURE_RUNE,1)}, 95026, 24235),
	TOPAZ(9336, 9239, 29, 33d, new Item[]{new Item(COSMIC_RUNE,1), new Item(FIRE_RUNE,2)}, 95027, 24254),
	RUBY(9339, 9242, 49, 59d, new Item[]{new Item(COSMIC_RUNE,1), new Item(FIRE_RUNE,5), new Item(BLOOD_RUNE,1)}, 95028, 24269),
	DIAMOND(9340, 9243, 57, 67d, new Item[]{new Item(COSMIC_RUNE,1), new Item(EARTH_RUNE,10), new Item(LAW_RUNE,2)}, 95029, 24288),
	DRAGONSTONE(9341, 9244, 68, 78d, new Item[]{new Item(COSMIC_RUNE,1), new Item(EARTH_RUNE,15), new Item(SOUL_RUNE,1)}, 95030, 24307),
	ONYX(9342, 9245, 87, 97d, new Item[]{new Item(COSMIC_RUNE,1), new Item(FIRE_RUNE,20), new Item(DEATH_RUNE,1)}, 95031, 24326);
	
	private int originalId;
	private int enchantedId;
	private int levelReq;
	private double exp;
	private Item[] runesRequired;
	private int buttonId;
	private int modelId;
	
	BoltData(int original, int enchanted, int req, double exp, Item[] runeReq, int button, int modelId) {
	    this.originalId = original;
	    this.enchantedId = enchanted;
	    this.levelReq = req;
	    this.exp = exp;
	    this.runesRequired = runeReq;
	    this.buttonId = button;
	    this.modelId = modelId;
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
	
	public Item[] getRunes() {
	    return this.runesRequired;
	}
	
	public int getOriginalId() {
	    return this.originalId;
	}
	
	public int getLevelReq() {
	    return this.levelReq;
	}
    }
    
    public static boolean handleButtons(final Player player, int buttonId){
    	
    	switch(buttonId){
    		case 75007:
    			openInterface(player);
			return true;
    	}
    	if(BoltData.forButtonId(buttonId) != null) {
    		enchantBolts(player, buttonId);
    		return true;
    	}
    	return false;
    }
    
    public static void openInterface(Player player){
    	if(player.getSkill().getLevel()[Skill.MAGIC] < 4) {
    	    player.getDialogue().sendStatement("You need a Magic level of atleast 4 to use this spell.");
    	    return;
    	}
		player.getActionSender().sendInterface(24173);
		player.setStatedInterface("boltEnchant");
		for(BoltData b : BoltData.values()) {
			player.getActionSender().sendItemOnInterface(b.modelId, 150, b.originalId);
		}
    }
    
    private static Requirement[] requirements;
	private static boolean canUseEnchant(final Player player, BoltData bd) {
		for (Requirement requirement : requirements)
			if (!requirement.meets(player))
				return false;
		return true;
	}

	public static void enchantBolts(final Player player, final int buttonId) {
		if(!player.getStatedInterface().contentEquals("boltEnchant"))
		{
			return;
		}
		final BoltData bd = BoltData.forButtonId(buttonId);
		if(bd == null) {
		    return;
		}
		final int boltId = bd.getOriginalId();
		if(!player.getInventory().playerHasItem(boltId))
		{
			player.getActionSender().sendMessage("You don't have any " + ItemManager.getInstance().getItemName(boltId) + ".");
			return;
		}
		if(player.getInventory().getItemAmount(boltId) < 10)
		{
			player.getActionSender().sendMessage("You need atleast 10 " + ItemManager.getInstance().getItemName(boltId) + " to perform this spell.");
			return;
		}
		requirements = new Requirement[bd.getRunes().length + 1];
		int i = 0;
		for (Item rune : bd.getRunes()) {
			requirements[i++] = new RuneRequirement(rune.getId(), rune.getCount()) {
				@Override
				public String getFailMessage() {
					return SpellAttack.FAILED_REQUIRED_RUNES;
				}
			};
		}
		requirements[i] = new SkillLevelRequirement(Skill.MAGIC, bd.getLevelReq()) {
			@Override
			public String getFailMessage() {
				return SpellAttack.FAILED_LEVEL_REQUIREMENT;
			}
		};
		if(!canUseEnchant(player, bd))
		{
			return;
		}
		player.getActionSender().removeInterfaces();
		player.setStatedInterface("");
		player.getUpdateFlags().sendAnimation(ENCHANT_ANIM);
		final int boltCount = 10;
		final int task = player.getTask();
		player.setSkilling(new CycleEvent() {
			final int amnt = boltCount;
		    @Override
		    public void execute(CycleEventContainer container) {
				if (!player.checkTask(task) || !canUseEnchant(player, bd) || !player.getInventory().playerHasItem(boltId) || amnt == 0) {
				    container.stop();
				    return;
				}
				player.getActionSender().sendMessage("The magic of the runes coaxes out the true nature of the gem tips.");
				for (Requirement requirement : requirements) {
					if (requirement instanceof ExecutableRequirement)
						((ExecutableRequirement) requirement).execute(player);
				}
				player.getInventory().removeItem(new Item(bd.originalId, amnt));
				player.getInventory().addItem(new Item(bd.enchantedId, amnt));
				container.stop();
		    }
			
		    @Override
		    public void stop() {
		    	player.resetAnimation();
		    }
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 3);
	}
}
