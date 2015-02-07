package com.rs2.model.content.skills.thieving;

import java.util.Random;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class ThieveOther {// todo hit method for poison chest and chest and doors

	private static final Random r = new Random();

	public static void pickLock(final Player player, final Position position, final int objectId, int level, final double xp, final int destX, final int destY) {
		if(player.stopPlayerPacket()) {
		    return;
		}
		player.setStopPacket(true);
		if (!Constants.THIEVING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if (player.getSkill().getLevel()[Skill.THIEVING] < level) {
			player.getActionSender().sendMessage("Your thieving level is not high enough to pick this lock.");
			return;
		}
		if (!player.getInventory().getItemContainer().contains(1523) && !player.onApeAtoll()) {
			player.getActionSender().sendMessage("You need a lockpick to do that.");
			return;
		}
		player.getUpdateFlags().sendAnimation(2246);
		player.getActionSender().sendMessage("You attempt to pick the lock...");
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (r.nextInt(30) < 5) {
					player.getActionSender().sendMessage("But fail to pick it.");
					player.setStopPacket(false);
					container.stop();
					return;
				}
				player.getActionSender().sendMessage("And manage to open the door.");
				player.getSkill().addExp(Skill.THIEVING, xp);
				player.getActionSender().walkTo(destX, destY, true);
				player.getActionSender().walkThroughDoor(objectId, position.getX(), position.getY(), player.getPosition().getZ());
				//Doors.handlePassThroughDoor(player, objectId, position.getX(), position.getY(), position.getZ(), destX, destY);
				container.stop();
			}

			@Override
			public void stop() {
				player.setStopPacket(false);
				player.resetAnimation();
			}
		}, 4);
	}

	public static void pickTrap(final Player player, final int objectId, final int objectX, final int objectY, int level, final double xp, final Item[] rewards, final int respawn) {
		if(player.stopPlayerPacket()) {
		    return;
		}
		player.setStopPacket(true);
		if (!Constants.THIEVING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if (player.getSkill().getLevel()[Skill.THIEVING] < level) {
			player.getActionSender().sendMessage("Your thieving level is not high enough to disarm traps.");
			return;
		}
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		    @Override
		    public void execute(CycleEventContainer b) {
			player.getUpdateFlags().sendAnimation(2246);
			player.getActionSender().sendMessage("You attempt to disarm the traps...");
			b.stop();
		    }

		    @Override
		    public void stop() {
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			    @Override
			    public void execute(CycleEventContainer container) {
				if (r.nextInt(30) < 5) {
				    player.getActionSender().sendMessage("But fail to disarm it, and get hit by the traps.");
				    int numb = r.nextInt(1);
				    player.hit(Misc.random(10), numb == 1 ? HitType.POISON : HitType.NORMAL);
				    if (numb == 1) {
					player.getUpdateFlags().sendGraphic(184);
				    }
				    container.stop();
				    return;
				}
				player.getActionSender().sendMessage("And manage to disarm it.");
				player.getSkill().addExp(Skill.THIEVING, xp);
				for (Item item : rewards) {
				    player.getInventory().addItem(item);
				}
				new GameObject(2588, objectX, objectY, player.getPosition().getZ(), SkillHandler.getFace(objectId, objectX, objectY, player.getPosition().getZ()), 10, objectId, respawn);
				container.stop();
			    }

			    @Override
			    public void stop() {
				player.setStopPacket(false);
				player.resetAnimation();
			    }
			}, 3);
		    }
		}, 1);
	}

	public static boolean handleObjectClick2(final Player player, int objectId, int objectX, int objectY) {
		int x = player.getPosition().getX();
		int y = player.getPosition().getY();
		switch (objectId) {
		case 2550:
			if (x == 2674 && y == 3305)
				pickLock(player, new Position(objectX, objectY), objectId, 1, 3.5, 0, 1);
			else
				pickLock(player, new Position(objectX, objectY), objectId, 16, 15, 0, -1);
			return true;
		case 2556:
			if (x == 2610 && y == 3316)
				pickLock(player, new Position(objectX, objectY), objectId, 14, 15, 1, 0);
			else
				pickLock(player, new Position(objectX, objectY), objectId, 13, 15, -1, 0);
			return true;
                case 2554:
			if (x == 2565 && y == 3356)
				pickLock(player, new Position(objectX, objectY), objectId, 45, 35, -1, 0);
			else
				pickLock(player, new Position(objectX, objectY), objectId, 45, 35, 1, 0);
			return true;
		case 2551:
			if (x == 2674 && y == 3304)
				pickLock(player, new Position(objectX, objectY), objectId, 16, 15, 0, -1);
			else
				pickLock(player, new Position(objectX, objectY), objectId, 16, 15, 0, 1);
			return true;
		case 2558:
			if (x == 3037 && y == 3956)
				pickLock(player, new Position(objectX, objectY), objectId, 39, 35, 1, 0);
			else if (x == 3041 && y == 3960)
				pickLock(player, new Position(objectX, objectY), objectId, 39, 35, 0, -1);
			else if (x == 3041 && y == 3959)
				pickLock(player, new Position(objectX, objectY), objectId, 39, 35, 0, 1);
			else if (x == 3045 && y == 3956)
				pickLock(player, new Position(objectX, objectY), objectId, 39, 35, -1, 0);
			else if (x == 3044 && y == 3956)
				pickLock(player, new Position(objectX, objectY), objectId, 39, 35, 1, 0);
			else
				pickLock(player, new Position(objectX, objectY), objectId, 39, 35, -1, 0);
			return true;
		case 2557:
			if (x == 3190 && y == 3957)
				pickLock(player, new Position(objectX, objectY), objectId, 42, 23, 0, 1);
			else if (x == 3190 && y == 3958)
				pickLock(player, new Position(objectX, objectY), objectId, 42, 23, 0, -1);
			else if (x == 3191 && y == 3962)
				pickLock(player, new Position(objectX, objectY), objectId, 42, 23, 0, 1);
			else if (x == 3191 && y == 3963)
				pickLock(player, new Position(objectX, objectY), objectId, 42, 23, 0, -1);
			return true;
		case 2566: //level 13 chest
			if (objectX == 2673 && objectY == 3307) {
			    pickTrap(player, objectId, objectX, objectY, 13, 8, new Item[]{new Item(995, 10)}, 11);
			} else if (objectX == 2630 && objectY == 3655) {
			    pickTrap(player, objectId, objectX, objectY, 13, 8, new Item[]{new Item(995, 10)}, 11);
			}
			return true;
		    case 2567: //level 28 nat chest
			if (objectX == 2671 && objectY == 3301) {
			    pickTrap(player, objectId, objectX, objectY, 28, 25, new Item[]{new Item(995, 3), new Item(561)}, 13);
			}
			return true;
		    case 2568: //level 43 chest
			if (objectX == 2671 && objectY == 3299) {
			    pickTrap(player, objectId, objectX, objectY, 43, 125, new Item[]{new Item(995, 50)}, 88);
			}
			return true;
		    case 2573: //level 47 hemenster chest & relleka
			if (objectX == 2639 && objectY == 3424) {
			    pickTrap(player, objectId, objectX, objectY, 47, 150, new Item[]{new Item(41, 5)}, 100);
			} else if (objectX == 2650 && objectY == 3659) {
			    
			    pickTrap(player, objectId, objectX, objectY, 47, 150, new Item[]{new Item(995, 75)}, 100);
			}
			return true;
		    case 2569: //level 59 chaos druid tower chests
			if (objectX == 2586 && objectY == 9737) {
			    pickTrap(player, objectId, objectX, objectY, 59, 250, new Item[]{new Item(995, 500), new Item(565, 2)}, 216);
			} else if (objectX == 2586 && objectY == 9734) {
			    pickTrap(player, objectId, objectX, objectY, 59, 250, new Item[]{new Item(995, 500), new Item(565, 2)}, 216);
			}
			return true;
		    case 2570: //level 72 king lathas chest
			if (objectX == 2588 && objectY == 3291) {
			    pickTrap(player, objectId, objectX, objectY, 72, 500, new Item[]{new Item(995, 1000), new Item(383), new Item(449), new Item(1623)}, 192);
			}
			return true;
                }
		return false;
	}
}
