package com.rs2.model.content.minigames.magetrainingarena;

import java.util.Random;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.Player.LoginStages;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;

import java.util.ArrayList;

public class EnchantingChamber {

    private Player player;

    private int tempPizazzPoint;
    private int[] enchantSpellsUsed = new int[6];
    private ArrayList<GroundItem> DRAGONSTONES = new ArrayList<>();
    private int enchantCount = 0;
    private int orbDepositCount = 0;
    
    private static final String[] color = {"red", "yellow", "green", "blue"};
    private static final int[] REWARD_RUNES = {565, 560, 564};
    private static final int RED = 6901;
    private static final int YELLOW = 6899;
    private static final int GREEN = 6898;
    private static final int BLUE = 6900;
    private static final int ORB = 6902;
    private static final int DRAGONSTONE = 6903;
    
    private static final MinigameAreas.Area CHAMBER = new MinigameAreas.Area(3341, 3386, 9616, 9662, 0);
    private static final Position[] ENTERING_POSITION = {new Position(3373, 9640, 0), new Position(3374, 9639, 0), new Position(3375, 9640, 0), new Position(3374, 9641, 0), new Position(3362, 9650, 0), new Position(3363, 9651, 0), new Position(3364, 9650, 0), new Position(3363, 9649, 0), new Position(3363, 9628, 0), new Position(3364, 9629, 0), new Position(3363, 9630, 0), new Position(3362, 9629, 0), new Position(3354, 9640, 0), new Position(3353, 9639, 0), new Position(3352, 9640, 0), new Position(3353, 9641, 0)};

    static Random random = new Random();
    
    public static Npc guardian() {
	for (Npc npc : World.getNpcs()) {
	    if (npc == null) {
		continue;
	    }
	    if (npc.getDefinition().getId() == 3100) {
		return npc;
	    }
	}
	return null;
    }

    public EnchantingChamber(Player player) {
	this.player = player;
    }

    /* loading the enchanting chamber part */
    public static void loadEnchantingChamber() {
    	loadEnchantingEvent();
		World.submit(new Tick(40) { // 24 seconds
		    @Override
		    public void execute() {
		    	loadEnchantingEvent();
		    }
		});
    }
    
    public static boolean isEnchantingChamberItem(int itemId) {
    	return itemId == RED || itemId == YELLOW || itemId == GREEN || itemId == BLUE || itemId == DRAGONSTONE;
    }

    /* loading the enchanting chamber event */
    private static void loadEnchantingEvent() {
    	MageGameConstants.bonusItemEnchantingChamber = color[random.nextInt(color.length)];
		for (Player player : World.getPlayers()) {
		    if (player == null) {
		    	continue;
		    }
		    if (player.getEnchantingChamber().isInEnchantingChamber()) {
		    	player.getEnchantingChamber().showInterfaceComponent(MageGameConstants.bonusItemEnchantingChamber);
		    }
		}
		if (guardian() != null) {
		    guardian().getUpdateFlags().sendForceMessage("The color shape is now " + MageGameConstants.bonusItemEnchantingChamber + "!");
		}
    }

    public boolean isInEnchantingChamber() {
    	return player.inEnchantingChamber();
    }

    public void loadInterfacePlayer() {
		player.getActionSender().sendWalkableInterface(15917);
		player.getActionSender().sendString("" + (player.getEnchantingPizazz() + tempPizazzPoint), 15921);
    }

    public void showInterfaceComponent(String bonusItem) {
		removeInterfaceComponent();
		if (bonusItem.equals("red")) {
		    player.getActionSender().sendInterfaceHidden(0, 15924);
		} else if (bonusItem.equals("yellow")) {
		    player.getActionSender().sendInterfaceHidden(0, 15922);
		} else if (bonusItem.equals("green")) {
		    player.getActionSender().sendInterfaceHidden(0, 15923);
		} else if (bonusItem.equals("blue")) {
		    player.getActionSender().sendInterfaceHidden(0, 15925);
		}
    }

    public void removeInterfaceComponent() {
		player.getActionSender().sendInterfaceHidden(1, 15924);
		player.getActionSender().sendInterfaceHidden(1, 15922);
		player.getActionSender().sendInterfaceHidden(1, 15923);
		player.getActionSender().sendInterfaceHidden(1, 15925);
    }

    public boolean handleObjectClicking(int objectId) {
	switch (objectId) {
	    case 10802:// red pile
	    case 10799:// yellow pile
	    case 10800:// green pile
	    case 10801:// blue pile
	    	addCorrespondingItem(objectId);
		return true;
	    case 10782:// exiting object
		if (isInEnchantingChamber()) {
		    exit();
		    return true;
		}
		return false;
	    case 10779:// entering object
	    	enter();
		return true;
	    case 10803:// deposit orb object
	    	depositOrbs();
		return true;
	}
	return false;
    }

    /* adding the corresponding item for the object id */
    private void addCorrespondingItem(final int objectId) {
	if (player.getInventory().getItemContainer().freeSlots() <= 0) {
	    player.getActionSender().sendMessage("Not enough space in your inventory.");
	    return;
	}
	player.getUpdateFlags().sendAnimation(832, 0);
	player.setStopPacket(true);
	CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
	    @Override
	    public void execute(CycleEventContainer b) {
		b.stop();
	    }

	    @Override
	    public void stop() {
		player.getInventory().addItem(new Item(correspondingItem(objectId)));
		player.setStopPacket(false);
	    }
	}, 2);
    }

    /* gets the correspondint item for an object id */
    private static int correspondingItem(int objectId) {
	switch (objectId) {
	    case 10802:// red pile
		return RED;
	    case 10799:// yellow pile
		return YELLOW;
	    case 10800:// green pile
		return GREEN;
	    case 10801:// blue pile
		return BLUE;
	}
	return 0;
    }

    private static String getStringById(int i) {
	if (i == RED) {
	    return "red";
	}
	if (i == YELLOW) {
	    return "yellow";
	}
	if (i == GREEN) {
	    return "green";
	}
	if (i == BLUE) {
	    return "blue";
	}
	return "";
    }

    private void enter() {
		if (player.getSkill().getLevel()[Skill.MAGIC] < MageGameConstants.ENCHANTING_LEVEL) {
		    player.getActionSender().sendMessage("You need a magic level of " + MageGameConstants.ENCHANTING_LEVEL + " to enter here.");
		    return;
		}
		try
		{
			orbDepositCount = player.getEnchantingOrbCount();
			if(MageGameConstants.bonusItemEnchantingChamber != null)
			{
				showInterfaceComponent(MageGameConstants.bonusItemEnchantingChamber);
			}
			int number = random.nextInt(ENTERING_POSITION.length);
			player.teleport(ENTERING_POSITION[number]);
			player.getActionSender().sendMessage("You've entered the Enchanting Chamber.");
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			    @Override
			    public void execute(CycleEventContainer b) {
				if(!isInEnchantingChamber()) {
				    b.stop();
				} else {
				    player.getActionSender().sendMessage("You hear the tinkle of gems hitting the floor.");
				    for(int i = 0; i < 6; i++) {
					GroundItem drop = new GroundItem(new Item(DRAGONSTONE), player, player, MinigameAreas.randomPosition(CHAMBER));
					GroundItemManager.getManager().dropItem(drop);
					DRAGONSTONES.add(drop);
				    }
				}
			    }
		
			    @Override
			    public void stop() {
				if (!DRAGONSTONES.isEmpty()) {
				    for (GroundItem g : DRAGONSTONES) {
					if (g != null) {
					    GroundItemManager.getManager().destroyItem(g);
					}
				    }
				}
			    }
			}, 300);
		} catch (Exception ex){
			ex.printStackTrace();
		}
    }
    
    public void saveVariables() {
	player.setEnchantingPizazz(player.getEnchantingPizazz() + tempPizazzPoint);
	player.setEnchantingEnchantCount(enchantCount);
	player.setEnchantingOrbCount(orbDepositCount);
	tempPizazzPoint = 0;
	if (!DRAGONSTONES.isEmpty() && player != null && player.getLoginStage().equals(LoginStages.LOGGED_IN)) {
	    for (GroundItem g : DRAGONSTONES) {
		if (g != null) {
		    try {
			GroundItemManager.getManager().destroyItem(g);
		    } catch(Exception e) {
			
		    }
		}
	    }
	}
	DRAGONSTONES.clear();
	tempPizazzPoint = 0;
    }
    
    public void exit() {
	player.getActionSender().sendMessage("You've left the Enchanting Chamber.");
	player.getActionSender().sendWalkableInterface(-1);
	player.teleport(MageGameConstants.LEAVING_POSITION);
	removeItems();
	player.getActionSender().sendWalkableInterface(-1);
	saveVariables();
    }

    public void removeItems() {
    	player.getInventory().removeItem(new Item(RED, player.getInventory().getItemAmount(RED)));
    	player.getInventory().removeItem(new Item(YELLOW, player.getInventory().getItemAmount(YELLOW)));
    	player.getInventory().removeItem(new Item(GREEN, player.getInventory().getItemAmount(GREEN)));
		player.getInventory().removeItem(new Item(BLUE, player.getInventory().getItemAmount(BLUE)));
		player.getInventory().removeItem(new Item(ORB, player.getInventory().getItemAmount(ORB)));
		player.getInventory().removeItem(new Item(DRAGONSTONE, player.getInventory().getItemAmount(DRAGONSTONE)));
    }

    public void enchantItem(int spellIndex, int itemId) {
	if (itemId != RED && itemId != YELLOW && itemId != GREEN && itemId != BLUE && itemId != DRAGONSTONE) {
	    return;
	}
	if(spellIndex < 0) {
	    return;
	}
	if (itemId == DRAGONSTONE) {
	    if ((player.getEnchantingPizazz() + tempPizazzPoint) <= MageGameConstants.MAX_ENCHANTING_POINT) {
		tempPizazzPoint += (spellIndex + 1) * 2;
	    } else {
		tempPizazzPoint = MageGameConstants.MAX_ENCHANTING_POINT - player.getEnchantingPizazz();
	    }
	} else {
	    if (MageGameConstants.bonusItemEnchantingChamber.equals(getStringById(itemId)) && (player.getEnchantingPizazz() + tempPizazzPoint) <= MageGameConstants.MAX_ENCHANTING_POINT) {
		player.getActionSender().sendMessage("You recieve 1 bonus point!");
		tempPizazzPoint++;
	    }
	    enchantSpellsUsed[spellIndex] += 1;
	    if ((player.getEnchantingPizazz() + tempPizazzPoint) <= MageGameConstants.MAX_ENCHANTING_POINT) {
		if(enchantCount == 9) {
		    tempPizazzPoint += (spellIndex + 1);
		    resetEnchantingSpells();
		} else {
		    enchantCount++;
		}
	    } else {
		tempPizazzPoint = MageGameConstants.MAX_ENCHANTING_POINT - player.getEnchantingPizazz();
	    }
	}
	player.getInventory().replaceItemWithItem(new Item(itemId), new Item(ORB));
    }

    private void depositOrbs() {
	if (!player.getInventory().getItemContainer().contains(ORB)) {
	    player.getActionSender().sendMessage("You don't have any orbs to deposit.");
	    return;
	}
	int count = player.getInventory().getItemAmount(ORB);
	if((orbDepositCount + count) >= 20) {
	    int overflow = (orbDepositCount + count) - 20;
	    player.getInventory().addItemOrDrop(new Item(REWARD_RUNES[Misc.randomMinusOne(3)], 3));
	    orbDepositCount = overflow;
	} else {
	    orbDepositCount += count;
	}
	player.getInventory().removeItem(new Item(ORB, count));
	player.getUpdateFlags().sendAnimation(832, 0);
    }

    private void resetEnchantingSpells() {
	for (int i = 0; i < enchantSpellsUsed.length; i++) {
	    enchantSpellsUsed[i] = 0;
	}
	enchantCount = 0;
    }

}
