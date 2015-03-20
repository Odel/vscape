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
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;
import com.rs2.util.clip.ClippedPathFinder;

public class PassObjectHandling {
	
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
					player.getActionSender().sendObject(3342, 2443, 9718, 0, 3, 10);
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
			case 3309: //rockslide
				if(pY > 9700) {
					int levelReq = level - 90;
					if(SkillHandler.skillCheck(level, levelReq < 1 ? 1 : levelReq, 0)) {
						if(o != null) {
							if(o.getRotation() == 1 || o.getRotation() == 3) {
								Agility.climbOver(player, pX > x ? x - 1 : x + 1, y, 1, 0);
							} else {
								Agility.climbOver(player, x, pY > y ? y - 1 : y + 1, 1, 0);
							}
						}
					} else {
						handleObstacleFailure(player, object, x, y);
					}
					return true;
				}
				return false;
		}
		return false;
	}
	
	private static void handleObstacleFailure(final Player player, final int object, final int x, final int y) { 
		switch(object) {
			case 3309: //rockslide
				player.getUpdateFlags().sendAnimation(846);
				player.hit(Misc.random(3), HitType.NORMAL);
				return;
		}
	}
}
