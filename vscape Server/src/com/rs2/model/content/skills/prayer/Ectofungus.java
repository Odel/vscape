package com.rs2.model.content.skills.prayer;

import com.rs2.model.Position;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import com.rs2.model.content.quests.GhostsAhoy;
import com.rs2.model.content.skills.Menus;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;


public class Ectofungus {
    public static final Position UP_TO_BONEGRINDER = new Position(3666, 3522, 1);
    public static final Position DOWN_FROM_BONEGRINDER = new Position(3666, 3517, 0);
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
    
    
    public static final int ECTOFUNGUS = 5282;
    public static final int TRAPDOOR = 5267;
    public static final int STAIRS_UP = 5280;
    public static final int STAIRS_DOWN = 5281;
    public static final int STAIRS_UP_SLIME = 5262;
    public static final int STAIRS_DOWN_SLIME = 5263;
    public static final int LADDER_UP = 5264;
    public static final int JUMP_DOWN = 9308;
    public static final int LOADER = 11162;
    public static final int BONEGRINDER = 11163;
    public static final int BONEGRINDER_BIN = 11164;
    public static final int SLIME = 5461;
    public static final int SLIME_2 = 5462;
    
    public static final int[] BONEMEAL_BONES = {4255, 526};
    public static final int[] BONEMEAL_BAT_BONES = {4256, 530};
    public static final int[] BONEMEAL_BIG_BONES = {4257, 532};
    public static final int[] BONEMEAL_BURNT_BONES = {4258, 528};
    //public static final int[] BONEMEAL_BURNT_JOGRE = {4259, 3127};
    public static final int[] BONEMEAL_BABY_DRAGON = {4260, 534};
    public static final int[] BONEMEAL_DRAGON_BONES = {4261, 536};
    public static final int[] BONEMEAL_WYVERN_BONES = {6810, 6812};
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
    public static final int[] BONEMEAL_DAG_BONES = {6728, 6729};
    public static final int[][] BONE_ITERATOR = {BONEMEAL_BONES, BONEMEAL_BAT_BONES, BONEMEAL_BIG_BONES, BONEMEAL_BURNT_BONES, BONEMEAL_BABY_DRAGON, BONEMEAL_DRAGON_BONES, BONEMEAL_WYVERN_BONES, BONEMEAL_WOLF_BONES, BONEMEAL_JOGRE_BONES, BONEMEAL_DAG_BONES };
    
    public static final int BUCKET = 1925;
    public static final int BUCKET_OF_SLIME = 4286;
    public static final int POT = 1931;
    public static final int ECTOTOKEN = 4278;
    
    public static double getExp(int bone) {
	return BoneBurying.getBone(bone).getXp() * 4;
    }
    
    public static int getGroundBones(final Player player) {
	for(int i[] : BONE_ITERATOR) {
	    if(player.getInventory().playerHasItem(i[0])) {
		return i[0];
	    }
	}
	return 0;
    }
    
    public static int getBones(final Player player) {
	for(int i[] : BONE_ITERATOR) {
	    if(player.getInventory().playerHasItem(i[1])) {
		return i[1];
	    }
	}
	return 0;
    }
    
    public static boolean doObjectFirstClick(final Player player, int object, int x, int y) {
	switch(object) {
	    case STAIRS_UP:
		if(x == STAIRS_UP_OBJ[X] && y == STAIRS_UP_OBJ[Y]) {
		    player.teleport(UP_TO_BONEGRINDER);
		    return true;
		}
	    case STAIRS_DOWN:
		if(x == STAIRS_DOWN_OBJ[X] && y == STAIRS_DOWN_OBJ[Y]) {
		    player.teleport(DOWN_FROM_BONEGRINDER);
		    return true;
		}
 	    case ECTOFUNGUS:
		if(x == ECTOFUNGUS_OBJ[X] && y == ECTOFUNGUS_OBJ[Y]) {
		    worship(player);
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
	    case BONEGRINDER:
		if(player.getBonesGround().size() > 0 && !player.bonesGrinded()) {
		    player.getUpdateFlags().sendAnimation(1648);
		    player.getActionSender().sendMessage("You grind the bones.");
		    for(BoneBurying.Bone bone : player.getBonesGround()) {
			player.addBonesInBin(bone);
		    }
		    player.setBonesGrinded(true);
		    return true;
		}
		else if(player.getBonesGround().size() > 0 && player.bonesGrinded()) {
		    player.getActionSender().sendMessage("You already ground these bones. Collect the bonemeal in the bin.");
		    return true;
		}
		else if(player.getBonesGround().isEmpty()) {
		    player.getActionSender().sendMessage("You haven't added any bones to the grinder!");
		    return true;
		}
		else {
		    return false;
		}
	    case BONEGRINDER_BIN:
		if(player.getBonesGround().isEmpty() || !player.bonesGrinded()) {
		    player.getActionSender().sendMessage("The bin is empty.");
		    return true;
		}
		else if(!player.secondTryAtBin() && !player.getBonesGround().isEmpty()) {
		    for(BoneBurying.Bone bone : player.getBonesGround()) {
			for(int i : bone.getBoneIds()) {
			    int count = player.getBonesInBin().size();
			    if(getGroundBoneForNormal(i) != 0) {
				if(count == 0) {
				    break;
				}
				if(player.getInventory().playerHasItem(POT)) {
				    player.getInventory().replaceItemWithItem(new Item(POT), new Item(getGroundBoneForNormal(i)));
				    player.getBonesInBin().remove(bone);
				    count --;
				}
				else if(!player.getInventory().playerHasItem(POT)) {
				    player.getActionSender().sendMessage("You don't have an empty pot with which to collect this bonemeal!");
				    player.setSecondTryAtBin(true);
				    return true;
				}
			    }
			}
		    }
		    player.setBonesGrinded(false);
		    player.setSecondTryAtBin(false);
		    player.getBonesGround().clear();
		    player.getUpdateFlags().sendAnimation(827);
		    player.getActionSender().sendMessage("You magically sort the bonemeal into pots.");
		    return true;
		}
		else {
		    player.getBonesGround().clear();
		    for(BoneBurying.Bone bone : player.getBonesInBin()) {
			player.getBonesGround().add(bone);
		    }
		    player.getActionSender().sendMessage("You examine the bin and find there is more bonemeal to be collected.");
		    player.setSecondTryAtBin(false);
		    return true;
		}
	}
	return false;
    }
    
    public static String getBonesAdded(final Player player) {
	String toReturn = "You have no bones in the grinder.";
	if(player.getBonesGround().size() > 0  &&  !player.bonesGrinded()) {
	    if(player.getBonesGround().size() == 1) {
		toReturn = "You have one bone in the grinder.";
	    }
	    else {
		toReturn = "You have " + player.getBonesGround().size() + " bones in the grinder.";
	    }
	}
	else if(player.getBonesGround().size() > 0  &&  player.bonesGrinded()) {
		toReturn = "You have some bonemeal in the bin. Collect it with an empty pot.";
	}
	/*
	int bones = 0;
	int batbones = 0, bigbones = 0, burntbones = 0, babydragon = 0, dragonbones = 0, wolfbones = 0, jogrebones = 0;
	String BONES = "", BATBONES = "", BIGBONES = "", BURNTBONES = "", BABYDRAGON = "", DRAGONBONES = "", WOLFBONES = "", JOGREBONES = "";
	for(int i = 0; i < player.getBonesGround().size(); i++) {
		switch(player.getBonesGround().get(i)) {
		    case BONES:
			bones++;
		    case BAT_BONES:
			batbones++;
		    case BIG_BONES:
			bigbones++;
		    case BABYDRAGON_BONES:
			babydragon++;
		    case DRAGON_BONES:
			dragonbones++;
		    case WOLF_BONES:
			wolfbones++;
		    case JOGRE_BONES:
			jogrebones++;
	    }
	}
	if(bones > 0) BONES = "Bones X" + bones + " ";
	if(batbones > 0) BATBONES = "Bat Bones X" + batbones + " ";
	if(bigbones > 0) BIGBONES = "Big Bones X" + bigbones + " ";
	if(burntbones > 0) BURNTBONES = "Burnt Bones X" + burntbones + " ";
	if(babydragon > 0) BABYDRAGON = "Baby Dragon X" + babydragon + " ";
	if(dragonbones > 0) DRAGONBONES = "Dragon Bones X" + bigbones + " ";
	if(wolfbones > 0) WOLFBONES = "Wolf Bones X" + bigbones + " ";
	if(jogrebones > 0) JOGREBONES = "Jogre Bones X" + bigbones + " ";
	return toReturn + BONES + BATBONES + BIGBONES + BURNTBONES + BABYDRAGON + DRAGONBONES + WOLFBONES + JOGREBONES;
		*/
	return toReturn;
     }
    
    public static boolean doObjectSecondClick(final Player player, int object, int x, int y) {
	switch(object) {
	    case BONEGRINDER:
		if(x == BONEGRINDER_OBJ[X] && y == BONEGRINDER_OBJ[Y]) {
		    player.getActionSender().sendMessage("" + getBonesAdded(player));
		    return true;
		}
	}
	return false;
    }
    
    public static boolean doItemOnObject(final Player player, int object, int item) {
	switch(object) {
	    case SLIME:
	    case SLIME_2:
		if(item == GhostsAhoy.BEDSHEET) {
		    player.getUpdateFlags().sendAnimation(827);
		    player.getActionSender().sendMessage("You dip the bedsheet into the ectoplasm, and it comes out dripping with green slime.");
		    player.getInventory().replaceItemWithItem(new Item(GhostsAhoy.BEDSHEET), new Item(GhostsAhoy.ECTOPLASM_BEDSHEET));
		    return true;
		}
		if(item == BUCKET) {
		    player.setStatedInterface("Ectoplasm");
		    Menus.display1Item(player, BUCKET_OF_SLIME, "");
		    return true;
		    /*
		    player.getUpdateFlags().sendAnimation(827);
		    player.getActionSender().sendMessage("You fill your bucket with some slime.");
		    player.getInventory().replaceItemWithItem(new Item(BUCKET), new Item(BUCKET_OF_SLIME));
		    return true;
		    */
		}
	    return false;
	    case LOADER:
		if(BoneBurying.getBone(item) != null) {
		    final int BONES = getBones(player);
		    if(BONES == 0) {
			player.getActionSender().sendMessage("Those aren't bones!");
			return true;
		    }
		    CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
			    player.getActionSender().sendMessage("You add some " + new Item(BONES).getDefinition().getName() + " to the grinder.");
			    player.getInventory().removeItem(new Item(BONES));
			    player.addBonesGround(BoneBurying.getBone(BONES));
			    player.getUpdateFlags().sendAnimation(1649);
			    b.stop();
			}

			@Override
			public void stop() {
			    player.setStopPacket(false);
			}
		    }, 1);
		return true;
		} else {
		    return false;
		}
	}
	return false;
    }
    
    public static boolean handleButtons(Player player, int buttonId) {
		switch (buttonId) {
			case 10239:
			    if(player.getStatedInterface().equals("Ectoplasm")) {
				handleFillTick(player, 1);
				return true;
			    }
			    else {
				return false;
			    }
			case 10238:
				if(player.getStatedInterface().equals("Ectoplasm")) {
				handleFillTick(player, 5);
				return true;
			    }
			    else {
				return false;
			    }
			case 6212:
			    return player.getStatedInterface().equals("Ectoplasm");
			case 6211:
			    if(player.getStatedInterface().equals("Ectoplasm")) {
				handleFillTick(player, 28);
				return true;
			    }
			    else {
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
		if (!player.getInventory().getItemContainer().contains(BUCKET))
		{
			return;
		}
		player.getActionSender().removeInterfaces();
		player.getUpdateFlags().sendAnimation(827);
		player.getInventory().replaceItemWithItem(new Item(BUCKET), new Item(BUCKET_OF_SLIME));
		player.getActionSender().sendMessage("You fill the bucket with Ectoplasm.");
	}
	
    public static boolean hasGroundBones(final Player player) {
	for(int i[] : BONE_ITERATOR) {
	    if(player.getInventory().playerHasItem(i[0])) {
		return true;
	    }
	}
	return false;
    }
    
    public static int getNormalBoneForGround(int ground) {
	for(int i[] : BONE_ITERATOR) {
	    if(i[0] == ground) {
		return i[1];
	    }
	}
	return 0;
    }
    
    public static int getGroundBoneForNormal(int normal) {
	for(int i[] : BONE_ITERATOR) {
	    if(i[1] == normal) {
		return i[0];
	    }
	}
	return 0;
    }
    
    public static void worship(final Player player) {
	if(!player.getInventory().playerHasItem(BUCKET_OF_SLIME)) {
	    player.getActionSender().sendMessage("You need a bucket of slime to worship the Ectofuntus!");
	    return;
	}
	if(!hasGroundBones(player)) {
	    player.getActionSender().sendMessage("You do not have any ground bones to worship the Ectofuntus with!");
	    return;
	}
	if(player.getEctoWorshipCount() >= 12) {
	   player.getActionSender().sendMessage("The Ectofuntus is full of power! Talk to a ghost disciple to claim your tokens.");
	   return; 
	}
	else {
	    final int BONES = getGroundBones(player);
	    if(BONES != 0) {
		player.setStopPacket(true);
		player.getUpdateFlags().sendAnimation(832);
		player.getActionSender().sendMessage("You place your slime and ground bones into the Ectofuntus.");
		player.getInventory().replaceItemWithItem(new Item(BONES), new Item(POT));
		player.getInventory().replaceItemWithItem(new Item(BUCKET_OF_SLIME), new Item(BUCKET));
		if(player.getEctoWorshipCount() > 20) {
		    player.setEctoWorshipCount(12);
		} else {
		    player.setEctoWorshipCount(player.getEctoWorshipCount() + 1);
		}
	    }
	    CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		    @Override
		    public void execute(CycleEventContainer b) {
			player.getActionSender().sendMessage("You worship the Ectofuntus.");
			player.getUpdateFlags().sendAnimation(1651);
			player.getSkill().addExp(Skill.PRAYER, getExp(getNormalBoneForGround(BONES)));
			b.stop();
		    }

		    @Override
		    public void stop() {
			player.setStopPacket(false);
		    }
		}, 4);
	}
    }
    
    public static boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
	switch(id) {
	    case 1686: //ghost disciple
		switch (player.getDialogue().getChatId()) {
		    case 1:
			if(player.getEctoWorshipCount() > 0) {
			    player.getDialogue().sendPlayerChat("I've worshipped the Ectofuntus, I'd like", "tokens in exchange for the power it has recieved.", CONTENT);
			    return true;
			}
			else {
			    player.getDialogue().sendPlayerChat("What is this strange fountain?", CONTENT);
			    player.getDialogue().setNextChatId(5);
			    return true;
			}
		    case 2:
			player.getDialogue().sendNpcChat("Yes, good work adventurer. The Ectofuntus is mighty!", "Here are some Ectotokens in reward.", CONTENT);
			return true;
		    case 3:
			player.getDialogue().sendGiveItemNpc("The disciple hands you some Ectotokens.", new Item(4278));
			player.getDialogue().endDialogue();
			player.getInventory().addItemOrDrop(new Item(ECTOTOKEN, player.getEctoWorshipCount() > 12 ? 60 : player.getEctoWorshipCount() * 5));
			player.setEctoWorshipCount(0);
			return true;
		    case 5:
			player.getDialogue().sendNpcChat("This is the Ectofuntus, the most marvellous", "creation of Necrovarus, our glorious leader.", HAPPY);
			return true;
		    case 6:
			player.getDialogue().sendOption("What is the Ectofuntus for?", "Where do I get ectoplasm from?", "How do I grind bones?", "How do I receive Ecto-tokens?", "Thank you for your time.");
			return true;
		    case 7:
			switch(optionId) {
			    case 1:
				player.getDialogue().sendPlayerChat("What is the Ectofuntus for?", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("Where do I get ectoplasm from?", CONTENT);
				player.getDialogue().setNextChatId(17);
				return true;
			    case 3:
				player.getDialogue().sendPlayerChat("How do I grind bones?", CONTENT);
				player.getDialogue().setNextChatId(20);
				return true;
			    case 4:
				player.getDialogue().sendPlayerChat("How do I recieve Ecto-tokens?", CONTENT);
				player.getDialogue().setNextChatId(24);
				return true;
			    case 5:
				player.getDialogue().sendPlayerChat("Thank you for your time.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		    case 8:
			player.getDialogue().sendNpcChat("It provides the power to keep us ghosts", "from passing over into the next plane of", "existence.", CONTENT);
			return true;
		    case 9:
			player.getDialogue().sendPlayerChat("And how does it work?", CONTENT);
			return true;
		    case 10:
			player.getDialogue().sendNpcChat("You have to pour a bucket of ectoplasm and", "a pot of ground bones into the Ectofuntus,", " and then worship at it. A unit of unholy", "power will then be created.", CONTENT);
			return true;
		    case 11:
			player.getDialogue().sendPlayerChat("Can you do it yourself?", CONTENT);
			return true;
		    case 12:
			player.getDialogue().sendNpcChat("No, we must rely upon the living, as the", "worship of the undead no longer holds any", "inherent power.", CONTENT);
			return true;
		    case 13:
			player.getDialogue().sendPlayerChat("Why would people waste their time helping you out?", CONTENT);
			return true;
		    case 14:
			player.getDialogue().sendNpcChat("For every unit of power produced, we will", "give you five Ecto-tokens. These tokens can be used", "in Port Phasmatys to purchase various services,", "not least of which includes access through the main gates.", CONTENT);
			return true;
		    case 15:
			player.getDialogue().sendPlayerChat("Thanks.", CONTENT);
			player.getDialogue().endDialogue();
			return true;
		    case 17:
			player.getDialogue().sendNpcChat("Necrovarus sensed the power bubbling beneath our feet", "and we delved long and deep beneath Port Phasmatys,", "until we found a pool of natural ectoplasm. You may", "find it by using the trapdoor over there.", CONTENT);
			return true;
		    case 18:
			player.getDialogue().sendPlayerChat("Thanks.", CONTENT);
			player.getDialogue().endDialogue();
			return true;
		    case 20:
			player.getDialogue().sendNpcChat("There is a bone grinding machine upstairs. Put bones", "of any type into the machine's hopper, and then turn", "the handle when you have loaded all your bones.", CONTENT);
			return true;
		    case 21:
			player.getDialogue().sendNpcChat("Necrovarus, in his mighty power, enchanted the bin to allow", " you to seperate all the bonemeal into pots instantaneously.", CONTENT);
			return true;
		    case 22:
			player.getDialogue().sendPlayerChat("Thanks.", CONTENT);
			player.getDialogue().endDialogue();
			return true;
		    case 24:
			player.getDialogue().sendNpcChat("We disciples keep track of how many units", "of power have been produced. Just talk to us", "once you have generated some and we will reimburse you.", CONTENT);
			return true;
		    case 25:
			player.getDialogue().sendPlayerChat("How do I produce units of power?", CONTENT);
			return true;
		    case 26:
			player.getDialogue().sendNpcChat("You have to pour a bucket of ectoplasm and", "a pot of ground bones into the Ectofuntus,", " and then worship at it. A unit of unholy", "power will then be created.", CONTENT);
			return true;
		    case 27:
			player.getDialogue().sendPlayerChat("Thanks.", CONTENT);
			player.getDialogue().endDialogue();
			return true;
		}
		return false;
	}
	return false;
    }
}
