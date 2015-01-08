package com.rs2.model.content.minigames.magetrainingarena;

import java.util.Random;

import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.content.minigames.magetrainingarena.MageGameConstants.BonePile;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;

public class CreatureGraveyard {
    
    public static final int BANANA = 1963;
    public static final int PEACH = 6883;
    public static final int BONES_1 = 6904;
    public static final int BONES_2 = 6905;
    public static final int BONES_3 = 6906;
    public static final int BONES_4 = 6907;
    
    public static final int BONEPILE_1 = 10725;
    public static final int BONEPILE_2 = 10726;
    public static final int BONEPILE_3 = 10727;
    public static final int BONEPILE_4 = 10728;
    
    public static final MinigameAreas.Area GRAVEYARD_AREA = new MinigameAreas.Area(3344, 3384, 9620, 9660, 1);
    public static final Position[] ENTERING_POSITION = {new Position(3364, 9640, 1), new Position(3363, 9641, 1), new Position(3362, 9640, 1), new Position(3363, 9639, 1)};
    public static final int[] RUNE_REWARDS = {565, 560, 557, 555, 561};
    
    public static Npc GUARDIAN;
    
    private Player player;
    private int pizazzPoints;
    private int fruitDeposited;
    
    public CreatureGraveyard(Player player) {
	this.player = player;
    }

    private static int getCorrespondingItem(int objectId) {
	switch (objectId) {
	    case BONEPILE_1:
		return BONES_1;
	    case BONEPILE_2:
		return BONES_2;
	    case BONEPILE_3:
		return BONES_3;
	    case BONEPILE_4:
		return BONES_4;
	}
	return 0;
    }

    private static int getFruitAmount(int itemId) {
	switch (itemId) {
	    case BONES_1:
		return 1;
	    case BONES_2:
		return 2;
	    case BONES_3:
		return 3;
	    case BONES_4:
		return 4;
	}
	return 0;
    }

    public boolean isInCreatureGraveyard() {
	return player.inCreatureGraveyard();
    }

    public void enter() {
	fruitDeposited = player.getGraveyardFruitDeposited();
	player.setGraveyardFruitDeposited(0);
	int number = new Random().nextInt(ENTERING_POSITION.length);
	if (player.getSkill().getLevel()[Skill.MAGIC] < MageGameConstants.GRAVEYARD_LEVEL) {
	    player.getActionSender().sendMessage("You need a magic level of " + MageGameConstants.GRAVEYARD_LEVEL + " to enter here.");
	    return;
	}
	player.teleport(ENTERING_POSITION[number]);
	player.getActionSender().sendMessage("You've entered the Creature Graveyard.");
    }
    
    public void saveVariables() {
	player.setGraveyardPizazz(player.getGraveyardPizazz() + pizazzPoints);
	player.setGraveyardFruitDeposited(fruitDeposited);
	pizazzPoints = 0;
	fruitDeposited = 0;
    }
    
    public void exit() {
	player.teleport(MageGameConstants.LEAVING_POSITION);
	player.getActionSender().sendMessage("You've left the Creature Graveyard.");
	player.getActionSender().sendWalkableInterface(-1);
	removeItems();
	saveVariables();
    }

    public void handleDeath() {
	pizazzPoints -= 10;
	if (pizazzPoints < 0) {
	    pizazzPoints = 0;
	    if(player.getGraveyardPizazz() > 10) {
		player.setGraveyardPizazz(player.getGraveyardPizazz() - 10);
	    } else {
		player.setGraveyardPizazz(0);
	    }
	}
	exit();
    }

    public void loadInterfacePlayer() {
	player.getActionSender().sendWalkableInterface(15931);
	player.getActionSender().sendString("" + (player.getGraveyardPizazz() + pizazzPoints), 15935);
    }

    public static void loadCreatureGraveyard() {
	loadBonePiles();
	World.submit(new Tick(20) {
	    @Override
	    public void execute() {
		for (Player p : World.getPlayers()) {
		    if (p == null) {
			continue;
		    }
		    if (p.getCreatureGraveyard().isInCreatureGraveyard()) {
			for (int i = 0; i < 20; i++) {
			    p.getActionSender().sendStillGraphic(520, MinigameAreas.randomPosition(GRAVEYARD_AREA).clone(), 0);
			}
			if ((75 >= (new Random().nextDouble() * 100)) && !Misc.goodDistance(p.getPosition(), GUARDIAN.getPosition(), 2)) {
			    p.hit(2, HitType.NORMAL);
			}
		    }
		}
	    }
	});
    }
    
    public static void loadBonePiles() {
	for(BonePile b : BonePile.values()) {
	    int x = b.getPosition().getX();
	    int y = b.getPosition().getY();
	    CacheObject g = ObjectLoader.object(x, y, 1);
	    GameObject o = new GameObject(BONEPILE_1, x, y, 1, g.getRotation(), g.getType(), g.getDef().getId(), 999999);
	    ObjectHandler.getInstance().addObject(o, true);
	}
    }
    
    public boolean applyBonesToFruit(boolean peaches) {
	if (!player.getSkill().canDoAction(1200)) {
	    return false;
	}
	if (peaches && !player.bonesToPeachesEnabled()) {
	    player.getActionSender().sendMessage("You must unlock this spell from the Mage Training Arena before using it.");
	    return false;
	}
	int fruit = peaches ? PEACH : BANANA;
	int[] count = {player.getInventory().getItemAmount(BONES_1), player.getInventory().getItemAmount(BONES_2), player.getInventory().getItemAmount(BONES_3), player.getInventory().getItemAmount(BONES_4)};
	if (count[0] == 0 && count[1] == 0 && count[2] == 0 && count[3] == 0) {
	    player.getActionSender().sendMessage("You don't have any bones to convert into fruits.");
	    return false;
	}
	player.getInventory().removeItem(new Item(BONES_1, count[0]));
	player.getInventory().removeItem(new Item(BONES_2, count[1]));
	player.getInventory().removeItem(new Item(BONES_3, count[2]));
	player.getInventory().removeItem(new Item(BONES_4, count[3]));
	int fruitCount = (count[0] * getFruitAmount(BONES_1)) + (count[1] * getFruitAmount(BONES_2)) + (count[2] * getFruitAmount(BONES_3)) + (count[3] * getFruitAmount(BONES_4));
	player.getInventory().addItem(new Item(fruit, fruitCount));
	return true;
    }

    public void removeItems() {
	player.getInventory().removeItem(new Item(PEACH, player.getInventory().getItemAmount(PEACH)));
	player.getInventory().removeItem(new Item(BANANA, player.getInventory().getItemAmount(BANANA)));
	player.getInventory().removeItem(new Item(BONES_1, player.getInventory().getItemAmount(BONES_1)));
	player.getInventory().removeItem(new Item(BONES_2, player.getInventory().getItemAmount(BONES_2)));
	player.getInventory().removeItem(new Item(BONES_3, player.getInventory().getItemAmount(BONES_3)));
	player.getInventory().removeItem(new Item(BONES_4, player.getInventory().getItemAmount(BONES_4)));

    }

    public void putFruitInFoodChute() {
	int bananaCount = player.getInventory().getItemAmount(BANANA);
	int peachCount = player.getInventory().getItemAmount(PEACH);
	if (bananaCount + peachCount == 0) {
	    player.getActionSender().sendMessage("You have no fruit to put in the fruit chute.");
	    return;
	}
	int pizazzWon = (bananaCount + peachCount + fruitDeposited) / 16;
	pizazzPoints += pizazzWon;
	if (pizazzPoints > MageGameConstants.MAX_GRAVEYARD_POINT) {
	    pizazzPoints = MageGameConstants.MAX_GRAVEYARD_POINT;
	}
	if(pizazzWon > 0) {
	    player.getActionSender().sendMessage("You put " + (bananaCount + peachCount) + " fruit in the food chute and receive " + pizazzWon + " point(s) and a rune reward!");
	    int newCount = (bananaCount + peachCount + fruitDeposited) - (pizazzWon * 16);
	    fruitDeposited = newCount > 0 ? newCount : 0;
	    player.getInventory().addItemOrDrop(new Item(RUNE_REWARDS[Misc.randomMinusOne(RUNE_REWARDS.length)], pizazzWon));
	} else {
	    player.getActionSender().sendMessage("You put " + (bananaCount + peachCount) + " fruit in the food chute.");
	    fruitDeposited = bananaCount + peachCount;
	}
	player.getUpdateFlags().sendAnimation(832);
	removeItems();
    }

    public boolean handleObjectClicking(final int objectId, final int x, final int y, final int h) {
	if (objectId >= BONEPILE_1 && objectId <= BONEPILE_4) {
	    if (player.getInventory().getItemContainer().freeSlots() <= 0) {
		player.getActionSender().sendMessage("Not enough space in your inventory.");
		return true;
	    }
	    player.setStopPacket(true);
	    CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		@Override
		public void execute(CycleEventContainer b) {

		    if (Misc.random(3) == 0) {
			player.hit(2, HitType.NORMAL);
		    }
		    player.getInventory().addItem(new Item(getCorrespondingItem(objectId)));
		    player.getUpdateFlags().sendAnimation(832);
		    BonePile pile = BonePile.forPosition(new Position(x, y, h));
		    if (pile != null) {
			pile.setPickCount(pile.getPickCount() + 1);
			if (pile.getPickCount() >= 4) {
			    int newId = pile.getCurrentId() == BONEPILE_4 ? BONEPILE_1 : pile.getCurrentId() + 1;
			    CacheObject g = ObjectLoader.object(x, y, 1);
			    ObjectHandler.getInstance().removeObject(x, y, h, g.getType());
			    GameObject o = new GameObject(newId, x, y, 1, g.getRotation(), g.getType(), pile.getCurrentId(), 999999);
			    ObjectHandler.getInstance().addObject(o, true);
			    pile.setCurrentId(newId);
			    pile.setPickCount(0);
			}
		    }
		    b.stop();
		}
		@Override
		public void stop() {
		    player.setStopPacket(false);
		}
	    }, 2);
	    return true;
	}
	if (objectId == 10782) {
	    if (isInCreatureGraveyard()) {
		exit();
		return true;
	    }
	} else if (objectId == 10781) {
	    if(player.getInventory().playerHasItem(new Item(BANANA)) || player.getInventory().playerHasItem(new Item(PEACH))) {
		player.getActionSender().sendMessage("You cannot bring fruit into the Creature Graveyard.");
		return true;
	    }
	    enter();
	    return true;
	} else if (objectId == 10735) {
	    putFruitInFoodChute();
	    return true;
	}

	return false;
    }

}
