package com.rs2.model.content.skills.prayer;

import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import java.util.ArrayList;

public class Ectofuntus {
    private final Player player;
    private ArrayList<BonemealData> bonesInLoader = new ArrayList<>();
    private ArrayList<BonemealData> bonemealInBin = new ArrayList<>();
    public BonemealData boneType = null;

    public Ectofuntus(Player player) {
	this.player = player;
    }

    public enum BonemealData {
	BONEMEAL_BONES(4255, 526, 18.0),
	BONEMEAL_BURNT_BONES(4258, 528, 24.0),
	BONEMEAL_WOLF_BONES(4262, 2859, 28.0),
	BONEMEAL_MONKEY_BONES(4267, 3183, 20.0),
	BONEMEAL_BAT_BONES(4256, 530, 21.2),
	BONEMEAL_BIG_BONES(4257, 532, 60.0),
	//BONEMEAL_SHAIKAHAN(0, 0, 0),
	BONEMEAL_BABY_DRAGON(4260, 534, 120.0),
	BONEMEAL_DRAGON_BONES(4261, 536, 288.0),
	BONEMEAL_WYVERN_BONES(6810, 6812, 288.0),
	BONEMEAL_JOGRE_BONES(4271, 3125, 90.0),
	//BONEMEAL_BURNT_JOGRE(4259, 3127, 0),
	BONEMEAL_SMALL_NINJA(4263, 3179, 64.0),
	BONEMEAL_MEDIUM_NINJA(4264, 3180, 72.0),
	BONEMEAL_GORILLA_BONES(4265, 3181, 72.0),
	BONEMEAL_BEARDED_GORILLA(4266, 3182, 72.0),
	BONEMEAL_SMALL_ZOMBIE_MONKEY(4268, 3185, 20.0),
	BONEMEAL_LARGE_ZOMBIE_MONKEY(4269, 3186, 20.0),
	//BONEMEAL_SKELETON_BONES(4270, 3187), //may not be right.
	BONEMEAL_DAG_BONES(6728, 6729, 500.0);

	public int bonemealId;
	public int boneId;
	public double exp;

	BonemealData(int bonemealId, int boneId, double exp) {
	    this.bonemealId = bonemealId;
	    this.boneId = boneId;
	    this.exp = exp;
	}

	public static BonemealData forBoneId(int id) {
	    for (BonemealData b : BonemealData.values()) {
		if (b.boneId == id) {
		    return b;
		}
	    }
	    return null;
	}
	
	public static BonemealData forBonemealId(int id) {
	    for (BonemealData b : BonemealData.values()) {
		if (b.bonemealId == id) {
		    return b;
		}
	    }
	    return null;
	}
    }

    public static final int BUCKET = 1925;
    public static final int BUCKET_OF_SLIME = 4286;
    public static final int POT = 1931;
    public static final int ECTOTOKEN = 4278;

    public static double getExp(int bone) {
	return BoneBurying.getBone(bone).getXp() * 4;
    }

    public int getFirstBonemealInInventory() {
	for (BonemealData b : BonemealData.values()) {
	    if (player.getInventory().playerHasItem(b.bonemealId)) {
		return b.bonemealId;
	    }
	}
	return 0;
    }

    public int getFirstBonesInInventory() {
	for (BonemealData b : BonemealData.values()) {
	    if (player.getInventory().playerHasItem(b.boneId)) {
		return b.boneId;
	    }
	}
	return 0;
    }

    public String getBonesAdded() {
	String toReturn = "You have no bones in the loader and no bonemeal in the bin.";
	if(!bonesInLoader.isEmpty() && !bonemealInBin.isEmpty()) {
	    toReturn = "You have " + bonesInLoader.size() + " " + new Item(boneType.boneId).getDefinition().getName().toLowerCase() + " in the loader and " + bonemealInBin.size() + " bonemeal in the bin.";
	} else if (!bonesInLoader.isEmpty() && bonemealInBin.isEmpty()) {
	    if (bonesInLoader.size() == 1) {
		toReturn = "You have one set of " + new Item(boneType.boneId).getDefinition().getName().toLowerCase() + " in the loader.";
	    } else {
		toReturn = "You have " + bonesInLoader.size() + " " + new Item(boneType.boneId).getDefinition().getName().toLowerCase() + " in the loader.";
	    }
	} else if (!bonemealInBin.isEmpty() && bonesInLoader.isEmpty()) {
	    toReturn = "You have " + bonemealInBin.size() + " bonemeal in the bin.";
	}
	return toReturn;
    }

    public static boolean handleButtons(Player player, int buttonId) {
	switch (buttonId) {
	    case 10239:
		if (player.getStatedInterface().equals("Ectoplasm")) {
		    handleFillTick(player, 1);
		    return true;
		} else {
		    return false;
		}
	    case 10238:
		if (player.getStatedInterface().equals("Ectoplasm")) {
		    handleFillTick(player, 5);
		    return true;
		} else {
		    return false;
		}
	    case 6212:
		return player.getStatedInterface().equals("Ectoplasm");
	    case 6211:
		if (player.getStatedInterface().equals("Ectoplasm")) {
		    handleFillTick(player, 28);
		    return true;
		} else {
		    return false;
		}
	}
	return false;
    }

    public static void handleFillTick(final Player player, final int amount) {
	final int task = player.getTask();
	player.getMovementHandler().reset();
	player.setNewSkillTask();
	player.getActionSender().removeInterfaces();
	player.setSkilling(new CycleEvent() {
	    int fillAmount = amount;

	    @Override
	    public void execute(CycleEventContainer container) {
		if (!player.checkNewSkillTask() || !player.checkTask(task) || !player.getInventory().getItemContainer().contains(BUCKET) || fillAmount == 0) {
		    container.stop();
		    return;
		}
		handleFill(player);
		fillAmount--;
		container.setTick(2);
	    }

	    @Override
	    public void stop() {
		player.resetAnimation();
	    }
	});
	CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 1);
    }

    public static void handleFill(final Player player) {
	if (!player.getInventory().getItemContainer().contains(BUCKET)) {
	    return;
	}
	player.getActionSender().removeInterfaces();
	player.getUpdateFlags().sendAnimation(827);
	player.getInventory().replaceItemWithItem(new Item(BUCKET), new Item(BUCKET_OF_SLIME));
	player.getActionSender().sendMessage("You fill the bucket with Ectoplasm.");
    }

    public static boolean hasBonemeal(final Player player) {
	for (BonemealData b : BonemealData.values()) {
	    if (player.getInventory().playerHasItem(b.bonemealId)) {
		return true;
	    }
	}
	return false;
    }

    public ArrayList<BonemealData> getBonesInLoader() {
	return this.bonesInLoader;
    }
    
    public ArrayList<BonemealData> getBonemealInBin() {
	return this.bonemealInBin;
    }

    public void worship() {
	if (!player.getInventory().playerHasItem(BUCKET_OF_SLIME)) {
	    player.getActionSender().sendMessage("You need a bucket of slime to worship the Ectofuntus!");
	    return;
	}
	if (!hasBonemeal(player)) {
	    player.getActionSender().sendMessage("You do not have any ground bones to worship the Ectofuntus with!");
	    return;
	}
	if (player.getEctoWorshipCount() >= 12) {
	    player.getActionSender().sendMessage("The Ectofuntus is full of power! Talk to a ghost disciple to claim your tokens.");
	    return;
	} else {
	    final BonemealData bone = BonemealData.forBonemealId(getFirstBonemealInInventory());
	    if (bone != null) {
		player.setStopPacket(true);
		player.getUpdateFlags().sendAnimation(832);
		player.getActionSender().sendMessage("You place your slime and ground bones into the Ectofuntus.");
		player.getInventory().replaceItemWithItem(new Item(bone.bonemealId), new Item(POT));
		player.getInventory().replaceItemWithItem(new Item(BUCKET_OF_SLIME), new Item(BUCKET));
		if (player.getEctoWorshipCount() > 12) {
		    player.setEctoWorshipCount(12);
		} else {
		    player.setEctoWorshipCount(player.getEctoWorshipCount() + 1);
		}
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		@Override
		public void execute(CycleEventContainer b) {
		    player.getActionSender().sendMessage("You worship the Ectofuntus.");
		    player.getUpdateFlags().sendAnimation(1651);
		    player.getSkill().addExp(Skill.PRAYER, bone.exp);
		    b.stop();
		}

		@Override
		public void stop() {
		    player.setStopPacket(false);
		}
	    }, 4);
	    }  
	}
    }
    
}
