package com.rs2.model.content.quests.impl.UndergroundPass;

import com.rs2.Constants;
import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.Graphic;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.combat.weapon.RangedAmmo;
import com.rs2.model.content.combat.weapon.Weapon;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.content.skills.agility.Agility;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;
import com.rs2.util.clip.ClippedPathFinder;

public class PassObjectHandling {
	
	public static void handleRopeSwing(final Player player, int object) {
		if (object == 2275 || object == 2276) {
			player.setStopPacket(true);
			player.getUpdateFlags().sendAnimation(775);
			player.getActionSender().sendMessage("You tie your rope to the rock...");
			new GameObject(2273, 2460, 9699, 0, 3, 10, -1, 5, false);
			player.walkTo(new Position(2462, 9699, 0), true);
			final Position toLand = new Position(2466, 9699, 0);
			player.getUpdateFlags().sendFaceToDirection(toLand);
			player.getUpdateFlags().setFaceToDirection(true);
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				int count = 0;
				@Override
				public void execute(CycleEventContainer b) {
					count++;
					if (count == 1) {
						int level = player.getSkill().getPlayerLevel(Skill.AGILITY), levelReq = level - 90;
						if (Misc.random(2) == 1 || SkillHandler.skillCheck(level, levelReq < 1 ? 1 : levelReq, 0)) {
							player.getActionSender().sendMessage("You skillfully swing across.");
							player.getActionSender().animateObject2(2460, 9699, 0, 751);
							Agility.swingRopeTimed(player, toLand.getX(), toLand.getY(), 1, 0, 2);
						} else {
							PassObjectHandling.handleObstacleFailure(player, 2273, 2463, 9635);
							b.stop();
						}
					} else {
						player.getActionSender().sendMessage("The rope gets tangled in some stalagmites.");
						player.getInventory().removeItem(new Item(954));
						new GameObject(2274, 2460, 9699, 0, 3, 10, -1, 999999, false);
						b.stop();
					}
				}
				@Override
				public void stop() {
					player.setStopPacket(false);
				}
			}, 2);
		} else {
			if(player.getPosition().getX() < 2463) {
				player.getActionSender().sendMessage("I can't reach that!");
				return;
			}
			player.setStopPacket(true);
			final Position toLand = new Position(2460, 9692, 0);
			player.getUpdateFlags().sendFaceToDirection(toLand);
			player.getUpdateFlags().setFaceToDirection(true);
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				int count = 0;
				@Override
				public void execute(CycleEventContainer b) {
					count++;
					if (count == 1) {
						int level = player.getSkill().getPlayerLevel(Skill.AGILITY), levelReq = level - 90;
						if (Misc.random(2) == 1 || SkillHandler.skillCheck(level, levelReq < 1 ? 1 : levelReq, 0)) {
							player.getActionSender().sendMessage("You skillfully swing across.");
							player.getActionSender().animateObject(2461, 9692, 0, 751);
							Agility.swingRopeTimed(player, toLand.getX(), toLand.getY(), 1, 0, 2);
						} else {
							PassObjectHandling.handleObstacleFailure(player, 2274, 2461, 9692);
							b.stop();
						}
					} else {
						b.stop();
					}
				}
				@Override
				public void stop() {
					player.setStopPacket(false);
				}
			}, 2);
		}
	}
	
	public static void crossOverBridge(final Player player) {
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				b.stop();
			}

			@Override
			public void stop() {
				player.movePlayer(new Position(player.getPosition().getX() < 2444 ? 2447 : 2442, 9716, 0));
				player.setAppearanceUpdateRequired(true);
				player.getMovementHandler().reset();
				player.getActionSender().sendMessage("You rush across the bridge.");
				player.setStopPacket(false);
			}
		}, 3);
	}
	
	public static void sinkInSwamp(final Player player) {
		player.setStopPacket(true);
		player.getMovementHandler().reset();
		player.getActionSender().sendMessage("You feel yourself being dragged below...");
		player.setStandAnim(772);
		player.setAppearanceUpdateRequired(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				b.stop();
			}

			@Override
			public void stop() {
				player.setStopPacket(false);
				/*
				if (player.Area(2463, 2474, 9708, 9719)) {
					player.fadeTeleport(new Position(2470, 9646, 0));
				} else {
					player.fadeTeleport(new Position(2450, 9634, 0));
				}
				*/
				doFallAndDamage(player);
			}
		}, 5);
	}
	
	public static void returnOverBridge(final Player player) {
		player.setStopPacket(true);
		player.getUpdateFlags().sendAnimation(832);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int count = 0;
			@Override
			public void execute(CycleEventContainer b) {
				count++;
				if(count == 1) {
					player.getActionSender().sendMessage("The bridge falls.");
					player.getActionSender().sendObject(3240, 2443, 9716, 0, 3, 10);
					ClippedPathFinder.getPathFinder().findRoute(player, 2442, 9716, true, 0, 0);
				}
				if(player.getPosition().equals(new Position(2442, 9716, 0)))
					b.stop();
			}

			@Override
			public void stop() {
				crossOverBridge(player);
			}
		}, 2);
	}
	
	public static void shootBridgeRope(final Player player, int x, int y) {
		Weapon weapon = player.getEquippedWeapon();
		if (weapon != null) {
			if (weapon.getAmmoType() == null) {
				player.getActionSender().sendMessage("You need a bow to shoot at the rope.");
				return;
			}
			RangedAmmo ammo = RangedAmmo.getRangedAmmo(player, weapon, false);
			if (player.getEquipment().getItemContainer().get(Constants.ARROWS) == null) {
				player.getActionSender().sendMessage("You have no arrows equipped!");
				return;
			}
			if (ammo == null) {
				return;
			} else {
				if (ammo.getProjectileId() != 17) {
					player.getDialogue().sendPlayerChat("I don't think these arrows will burn the rope", "seeing as they aren't lit on fire.");
					return;
				}
			}
			int animation = weapon.getAttackAnimations()[0];
			Graphic startGfx = ammo.getGraphicId() != -1 ? new Graphic(ammo.getGraphicId(), weapon.getAmmoType().getGraphicHeight()) : null;
			player.getUpdateFlags().sendFaceToDirection(new Position(x, y, 0));
			player.getUpdateFlags().setFaceToDirection(true);
			player.getUpdateFlags().sendAnimation(animation);
			player.getUpdateFlags().sendGraphic(startGfx);
			int attackerX = player.getPosition().getX(), attackerY = player.getPosition().getY();
			final int offsetX = (attackerY - y) * -1;
			final int offsetY = (attackerX - x) * -1;
			World.sendProjectile(player.getPosition(), offsetX, offsetY, ammo.getProjectileId(), 43, 40, 70, 0, false);
			player.getActionSender().sendMessage("You fire your arrow at the rope supporting the bridge...");
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer b) {
					b.stop();
				}

				@Override
				public void stop() {
					player.getActionSender().sendMessage("The arrow impales the rope support.");
					new GameObject(3342, 2443, 9718, 0, 3, 10, 3340, 30, false);
					ClippedPathFinder.getPathFinder().findRoute(player, 2447, 9716, true, 0, 0);
					player.setStopPacket(true);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							if (player.getPosition().equals(new Position(2447, 9716, 0))) {
								player.getActionSender().sendMessage("The bridge falls.");
								player.getActionSender().sendObject(3240, 2443, 9716, 0, 3, 10);
								b.stop();
							} else if (!player.isMoving()) {
								ClippedPathFinder.getPathFinder().findRoute(player, 2447, 9716, true, 0, 0);
							}
						}

						@Override
						public void stop() {
							crossOverBridge(player);
						}
					}, 1);
				}
			}, 3);
		}
	}

	public static boolean doObstacleClicking(final Player player, final int object, final int x, final int y) {
		int level = player.getSkill().getPlayerLevel(Skill.AGILITY);
		final int pX = player.getPosition().getX(), pY = player.getPosition().getY();
		final CacheObject o = ObjectLoader.object(object, x, y, player.getPosition().getZ());
		switch(object) {
			case 3263:
				sinkInSwamp(player);
				return true;
			case 3265:
				if(x == 2443 && y == 9651) {
					player.fadeTeleport(new Position(2475, 9715, 0));
					player.getUpdateFlags().sendAnimation(828);
					player.getActionSender().sendMessage("You climb up the pile of mud and rocks...");
					player.getActionSender().sendMessage("It leads into darkness, the stench is unbearable.");
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}
						@Override
						public void stop() {
							player.getActionSender().sendMessage("You surface by the swamp, covered in muck.");
						}
					}, 5);
					return true;
				}
			return false;
			case 3365:
				player.getActionSender().sendMessage("You crawl out of the pit.");
				Ladders.climbLadder(player, new Position(2478, 9678, 0));
				GridMazeHandler.startGridCheck(player);
				return true;
			case 3309: //rockslide
					int levelReq = level - 90;
					if(SkillHandler.skillCheck(level, levelReq < 1 ? 1 : levelReq, 0)) {
						if(o != null) {
							if(x == 2482 && y == 9679 && player.getPosition().getX() > x) {
								GridMazeHandler.startGridCheck(player);
							}
							player.getMovementHandler().resetOnWalkPacket();
							if(Misc.checkClip(new Position(x, y, 0), new Position(x + 1, y, 0), true) && Misc.checkClip(new Position(x, y, 0), new Position(x - 1, y, 0), true)) {
								Agility.climbOver(player, pX > x ? x - 1 : x + 1, y, 1, 0);
							} else {
								Agility.climbOver(player, x, pY > y ? y - 1 : y + 1, 1, 0);
							}
							/*
							if(x != 2491 && y != 6961 && (o.getRotation() == 1 || o.getRotation() == 3 || (x == 2467 && y == 9466) || (x == 2455 && y == 9647))) {
								Agility.climbOver(player, pX > x ? x - 1 : x + 1, y, 1, 0);
							} else {
								Agility.climbOver(player, x, pY > y ? y - 1 : y + 1, 1, 0);
							}
								*/
						}
					} else {
						handleObstacleFailure(player, object, x, y);
					}
					return true;
		}
		return false;
	}
	
	public static void handleObstacleFailure(final Player player, final int object, final int x, final int y) { 
		switch(object) {
			case 2274: //swamp rope swing
				player.movePlayer(new Position(2462, 9692, 0));
				sinkInSwamp(player);
				break;
			case 2273: //rock rope swing
				doFallAndDamage(player);
				break;
			case 3309: //rockslide
				player.getActionSender().sendMessage("You slip on the rocks!");
				player.getUpdateFlags().sendAnimation(846);
				player.hit(Misc.random(3), HitType.NORMAL);
				break;
		}
	}
	
	public static void doFallAndDamage(final Player player) {
		player.setStandAnim(-1);
		player.fadeTeleport(new Position(2485, 9649, 0));
		player.getActionSender().sendMessage("You tumble deep into the crevasse.");
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				b.stop();
			}

			@Override
			public void stop() {
				player.setStopPacket(false);
				player.getActionSender().sendMessage("You land battered and bruised at the base.");
				player.hit(Misc.random(13), HitType.NORMAL);
			}
		}, 5);
	}
}
