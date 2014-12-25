package com.rs2.model.content.minigames.magetrainingarena;

import java.util.Random;

import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 01/01/12 Time: 22:55 To change
 * this template use File | Settings | File Templates.
 */
public class CreatureGraveyard {// todo handle death, logout, login

	public static final Position[] ENTERING_POSITION = {new Position(3364, 9640, 1), new Position(3363, 9641, 1), new Position(3362, 9640, 1), new Position(3363, 9639, 1)};
	public static final int BANANA = 1963;
	public static final int PEACH = 6883;

	private Player player;

	public CreatureGraveyard(Player player) {
		this.player = player;
	}

	private int pizzazPoints;

	private static int getCorrespondingItem(int objectId) {
		switch (objectId) {
			case 10725 :
				return 6904;
			case 10726 :
				return 6905;
			case 10727 :
				return 6906;
			case 10728 :
				return 6907;
		}
		return 0;
	}
	private static int getFruitAmount(int itemId) {
		switch (itemId) {
			case 6904 :
				return 1;
			case 6905 :
				return 2;
			case 6906 :
				return 3;
			case 6907 :
				return 4;
		}
		return 0;
	}
	public static void lowerObjectLife(int objectId, int x, int y, int h) {
		CacheObject object = ObjectLoader.object(objectId, x, y, h);
		if (objectId != 10725) {
			ObjectHandler.getInstance().removeObject(x, y, h, 10);
			new GameObject(objectId - 1, x, y, h, object.getRotation(), object.getType(), objectId, 20);
		}

	}

	public boolean isInCreatureGraveyard() {
	    return player.inCreatureGraveyard();
	}
	
	public void enter() {
		Random random = new Random();
		int number = random.nextInt(ENTERING_POSITION.length);
		if (player.getSkill().getLevel()[Skill.MAGIC] < MageGameConstants.GRAVEYARD_LEVEL) {
			player.getActionSender().sendMessage("You need a magic level of " + MageGameConstants.ALCHEMIST_LEVEL + " to enter here.");
			return;
		}
		player.teleport(ENTERING_POSITION[number]);
		player.getActionSender().sendMessage("You've entered the Creature Graveyard.");
	}

	public void exit() {
		player.teleport(MageGameConstants.LEAVING_POSITION);
		player.getActionSender().sendMessage("You've left the Creature Graveyard.");
		removeItems();
	}

	public void handleDeath() {
		pizzazPoints -= 10;
		if (pizzazPoints < 0)
			pizzazPoints = 0;
		player.teleport(MageGameConstants.LEAVING_POSITION);
		removeItems();
	}

	public void loadInterfacePlayer() {
		player.getActionSender().sendWalkableInterface(15931);
		player.getActionSender().sendString("" + pizzazPoints, 15935);
	}

	public static void loadCreatureGraveyard() {
		World.submit(new Tick(20) {
			@Override
			public void execute() {
				for (Player p : World.getPlayers()) {
					if (p == null)
						continue;
					if (p.getCreatureGraveyard().isInCreatureGraveyard()) {
						for (int i = 0; i < 20; i++)
							p.getActionSender().sendStillGraphic(520, new Position(3350 + Misc.random(40), 9610 + Misc.random(50)), 0);
						if (Misc.random(2) == 0) {
							p.hit(2, HitType.NORMAL);
						}
					}
				}
			}
		});
	}

	public boolean applyBonesToFruit(boolean peaches) {
		int fruit = peaches ? PEACH : BANANA;
		int[] count = {player.getInventory().getItemAmount(6904), player.getInventory().getItemAmount(6905), player.getInventory().getItemAmount(6906), player.getInventory().getItemAmount(6907)};
		if (count[0] == 0 && count[1] == 0 && count[2] == 0 && count[3] == 0) {
			player.getActionSender().sendMessage("You don't have any bones to convert into fruits.");
			return false;
		}
		player.getInventory().removeItem(new Item(6904, count[0]));
		player.getInventory().removeItem(new Item(6905, count[1]));
		player.getInventory().removeItem(new Item(6906, count[2]));
		player.getInventory().removeItem(new Item(6907, count[3]));
		player.getInventory().addItem(new Item(fruit, count[0] * getFruitAmount(6904)));
		player.getInventory().addItem(new Item(fruit, count[1] * getFruitAmount(6905)));
		player.getInventory().addItem(new Item(fruit, count[2] * getFruitAmount(6906)));
		player.getInventory().addItem(new Item(fruit, count[3] * getFruitAmount(6907)));
		return true;
	}

	public void removeItems() {
		player.getInventory().removeItem(new Item(PEACH, 28));
		player.getInventory().removeItem(new Item(BANANA, 28));
		player.getInventory().removeItem(new Item(6904, 28));
		player.getInventory().removeItem(new Item(6905, 28));
		player.getInventory().removeItem(new Item(6906, 28));
		player.getInventory().removeItem(new Item(6907, 28));

	}

	public void putFruitInFoodChute() {
		int bananaCount = player.getInventory().getItemAmount(BANANA);
		int peachCount = player.getInventory().getItemAmount(PEACH);
		if (bananaCount + peachCount == 0) {
			player.getActionSender().sendMessage("You have no fruit to put in the fruit chute.");
			return;
		}
		int pizzazWon = (bananaCount + peachCount) / 16;
		pizzazPoints += pizzazWon;
		if (pizzazPoints > MageGameConstants.MAX_GRAVEYARD_POINT)
			pizzazPoints = MageGameConstants.MAX_GRAVEYARD_POINT;
		player.getActionSender().sendMessage("You've put " + (bananaCount + peachCount) + " in the food chute and receive " + pizzazWon + " points");
		player.getUpdateFlags().sendAnimation(832);
		removeItems();
	}

	public boolean handleObjectClicking(int objectId, int x, int y, int h) {
		if (objectId >= 10725 && objectId <= 10728) {
			if (player.getInventory().getItemContainer().freeSlots() <= 0) {
				player.getActionSender().sendMessage("Not enough space in your inventory.");
				return true;
			}
			if (Misc.random(5) == 0)
				lowerObjectLife(objectId, x, y, h);
			if (Misc.random(3) == 0)
				player.hit(2, HitType.NORMAL);
			player.getInventory().addItem(new Item(getCorrespondingItem(objectId)));
			player.getUpdateFlags().sendAnimation(832);
			return true;
		}
		if (objectId == 10782) {
			if (isInCreatureGraveyard())
				exit();
			return false;
		} else if (objectId == 10781) {
			enter();
			return true;
		} else if (objectId == 10735) {
			putFruitInFoodChute();
			return true;
		}

		return false;
	}

	public static boolean handleBonesRestore(GameObject object) {
		final GameObject o = object;
		switch (object.getDef().getId()) {
			case 10725 :
				ObjectHandler.getInstance().removeObject(o.getDef().getPosition().getX(), o.getDef().getPosition().getY(), o.getDef().getPosition().getZ(), o.getDef().getType());
				new GameObject(o.getDef().getId() + 1, o.getDef().getPosition().getX(), o.getDef().getPosition().getY(), o.getDef().getPosition().getZ(), o.getDef().getFace(), o.getDef().getType(), o.getDef().getId(), 20);
				return true;
			case 10726 :
				ObjectHandler.getInstance().removeObject(o.getDef().getPosition().getX(), o.getDef().getPosition().getY(), o.getDef().getPosition().getZ(), o.getDef().getType());
				new GameObject(o.getDef().getId() + 1, o.getDef().getPosition().getX(), o.getDef().getPosition().getY(), o.getDef().getPosition().getZ(), o.getDef().getFace(), o.getDef().getType(), o.getDef().getId(), 20);
				return true;
			case 10727 :
				ObjectHandler.getInstance().removeObject(o.getDef().getPosition().getX(), o.getDef().getPosition().getY(), o.getDef().getPosition().getZ(), o.getDef().getType());
				new GameObject(o.getDef().getId() + 1, o.getDef().getPosition().getX(), o.getDef().getPosition().getY(), o.getDef().getPosition().getZ(), o.getDef().getFace(), o.getDef().getType(), o.getDef().getId(), 500000);
				return true;

		}
		return false;
	}
}
