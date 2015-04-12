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
import com.rs2.model.content.skills.thieving.ThieveOther;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.model.tick.Tick;
import com.rs2.net.ActionSender;
import com.rs2.util.Misc;
import com.rs2.util.clip.ClippedPathFinder;

public class PassObjectHandling {
	
	public static void handleIbansDoors(final Player player, final int x, final int y) {
		if (player.getPosition().getX() > 2100) {
			if (player.getPosition().getX() <= x) {
				player.getActionSender().sendMessage("You pull open the large doors...", true);
				new GameObject(3335, 2143, 4648, 1, 3, 10, 3333, 4, true);
				new GameObject(3335, 2143, 4647, 1, 1, 10, 3334, 4, true);
				player.getActionSender().walkTo(2, 0, true);
				return;
			}
			if (PassNpcHandling.ibanEncounterRunning) {
				player.getActionSender().sendMessage("A magical force has sealed the door. Iban appears to be busy.");
				return;
			}
			if (player.getQuestStage(44) < UndergroundPass.DOLL_COMPLETE) {
				if (player.getQuestStage(44) == UndergroundPass.IBANS_DEMISE) {
					player.getDialogue().sendPlayerChat("Perhaps I don't want to open this without", "having infused the doll with all four elements.");
				} else {
					player.getDialogue().sendPlayerChat("I don't want to get killed by Iban. I should do", "some more research first before charging in.");
				}
				return;
			}
			if (player.getEquipment().wearingOnlySpecifics(new int[]{1035, 1033})) {
				boolean dollComplete = true;
				for (boolean b : player.getQuestVars().getIbanDollElements()) {
					dollComplete = b;
				}
				if (dollComplete && player.getInventory().playerHasItem(UndergroundPass.DOLL_OF_IBAN)) {
					player.getActionSender().sendMessage("You pull open the large doors...", true);
					new GameObject(3335, 2143, 4648, 1, 3, 10, 3333, 4, true);
					new GameObject(3335, 2143, 4647, 1, 1, 10, 3334, 4, true);
					player.getMovementHandler().setRunToggled(true);
					player.getActionSender().walkTo(-2, 0, true);
					PassNpcHandling.startIbanEncounter(player);
				} else {
					player.getDialogue().sendPlayerChat("Perhaps I don't want to open this without", "the doll. It is the only thing that can kill", "Lord Iban.");
				}
			} else {
				player.getActionSender().sendMessage("The doors refuse to open...", true);
				player.getActionSender().sendTimedMessage("Only followers of Zamorak may enter.", true, 3);
			}
		} else {
			player.getActionSender().sendMessage("You pull open the large doors...", true);
			new GameObject(3335, 2015, 4712, 1, 3, 10, 3333, 4, true);
			new GameObject(3335, 2015, 4711, 1, 1, 10, 3334, 4, true);
			player.getActionSender().walkTo(player.getPosition().getX() > x ? -2 : 2, 0, true);
		}
	}
	
	public static void igniteIbansTomb(final Player player) {
		new GameObject(3357, 2356, 9800, 0, 3, 2, -1, 6);
		new GameObject(3357, 2356, 9801, 0, 0, 0, -1, 6);
		new GameObject(3357, 2356, 9802, 0, 0, 0, -1, 6);
		new GameObject(3357, 2356, 9803, 0, 0, 2, -1, 6);
		new GameObject(3357, 2357, 9803, 0, 1, 0, -1, 6);
		new GameObject(3357, 2358, 9803, 0, 1, 0, -1, 6);
		new GameObject(3357, 2359, 9803, 0, 1, 2, -1, 6);
		new GameObject(3357, 2359, 9802, 0, 2, 0, -1, 6);
		new GameObject(3357, 2359, 9801, 0, 2, 0, -1, 6);
		new GameObject(3357, 2359, 9800, 0, 2, 2, -1, 6);
		new GameObject(3357, 2357, 9800, 0, 3, 0, -1, 6);
		new GameObject(3357, 2358, 9800, 0, 3, 0, -1, 6);
	}
	
	public static void handleBridgeJump(final Player player, final int object, final int x, final int y) {
		if (player.stopPlayerPacket()) {
			return;
		}
		CacheObject o = ObjectLoader.object(object, x, y, 1);
		if (o != null) {
			player.setStopPacket(true);
			int face = o.getDef().getFace(), modifier = face == 1 || face == 3 ? (player.getPosition().getX() > x ? -4 : 4) : (player.getPosition().getY() > y ? -4 : 4), 
				toFace = face == 1 || face == 3 ? (modifier < 0 ? 3 : 1) : (modifier < 0 ? 2 : 0);
			player.getActionSender().sendMessage("You attempt to jump over the remaining bridge...");
			final Position toBe = face == 1 || face == 3 ? new Position(player.getPosition().getX() + modifier, y, 1) : new Position(x, player.getPosition().getY() + modifier, 1);
			player.getUpdateFlags().setFace(toBe);
			final boolean success = SkillHandler.skillCheck((player.getSkill().getPlayerLevel(Skill.AGILITY) + 40), 10, 0);
			player.getUpdateFlags().sendAnimation(2750);
			if(face == 1 || face == 3) {
				player.getUpdateFlags().sendForceMovement(player, modifier, !success ? -1 : 0, 15, 50, toFace, success ? 3 : 2, true);
			} else {
				player.getUpdateFlags().sendForceMovement(player, !success ? -1 : 0, modifier, 15, 50, toFace, success ? 3 : 2, true);
			}
			if(!success) {
				player.getActionSender().sendWalkableInterface(8677);
			}
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				int count = 0;
				@Override
				public void execute(CycleEventContainer b) {
					player.setStopPacket(true);
					if (count == 1 && !success) {
						b.stop();
					} else {
						if (!success) {
							player.getActionSender().sendMessage("...you plunge into darkness...");
							player.transformNpc = 2370;
							count++;
						} else {
							count++;
							if(count == 2) {
								b.stop();
							} else if(count == 1) {
								player.getActionSender().sendMessage("...you manage to cross safely.");
							}
						}
					}
				}
				@Override
				public void stop() {
					player.setStopPacket(false);
					if(!success) {
						player.teleport(new Position(2331, 9855, 0));
						player.getActionSender().sendWalkableInterface(-1);
						player.transformNpc = -1;
						player.setAppearanceUpdateRequired(true);
						player.hit(Misc.random(5) + 13, HitType.NORMAL);
						player.getUpdateFlags().setForceChatMessage("Ouch!");
					} else {
						if (player.getQuestStage(44) >= 10) {
							if ((toBe.getX() == 2034 || toBe.getX() == 2032) && (toBe.getY() == 4730 || toBe.getY() == 4688)) {
								player.movePlayer(new Position(toBe.getX() + 128, toBe.getY() - 64, 1));
							} else if ((toBe.getX() == 2162 || toBe.getX() == 2160) && (toBe.getY() == 4662 || toBe.getY() == 4628)) {
								player.movePlayer(new Position(toBe.getX() - 128, toBe.getY() + 64, 1));
							}
						}
					}
				}
			}, 3);
		}
	}
	
	public static void handlePipeCrawl(final Player player, final int object, final int x, final int y) {
		if (player.stopPlayerPacket()) {
			return;
		}
		player.setStopPacket(false);
		final CacheObject o = ObjectLoader.object(x, y, 0);
		if (o != null) {
			final int face = o.getRotation();
			if (object == 3235) {
				player.getActionSender().sendMessage("You remove the grill from the pipe and crawl in...", true);
				player.getActionSender().sendObject(3237, x, y, 0, 3, 10);
			} else {
				player.getActionSender().sendMessage("You crawl into the pipe...", true);
			}
			if (face == 2) {
				player.getActionSender().sendMapState(2);
			}
			final Position toBe = new Position(face == 2 ? x + 2 : face == 3 ? x - 1 : x, face == 0 ? y - 1 : y, 0);
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				int count = 0;
				boolean stop = false;

				@Override
				public void execute(CycleEventContainer b) {
					count++;
					if (count >= 5 || stop) {
						b.stop();
					}
					player.getUpdateFlags().sendFaceToDirection(new Position(x, y, 0));
					player.getUpdateFlags().setFaceToDirection(true);
					player.getUpdateFlags().setUpdateRequired(true);
					if (player.getPosition().equals(toBe) && !stop) {
						player.getUpdateFlags().sendAnimation(749);
						stop = true;
					} else {
						player.walkTo(toBe, true);
					}
				}

				@Override
				public void stop() {
					final boolean wasRunning = player.getMovementHandler().isRunToggled();
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						int count = 0;

						@Override
						public void execute(CycleEventContainer b) {
							count++;
							switch (count) {
								case 1:
									if (face == 2) {
										if (player.getQuestStage(44) >= UndergroundPass.UNICORN_KILLED) {
											player.movePlayer(new Position(x - 25, y, 0));
										} else {
											player.movePlayer(new Position(x, y, 0));
										}
									} else if (face == 3) {
										player.movePlayer(new Position(2415, 9605, 0));
									} else if (face == 0) {
										player.movePlayer(new Position(2451, 9690, 0));
									}
									player.transformNpc = 2370;
									player.getMovementHandler().setRunToggled(false);
									player.getMovementHandler().reset();
									//player.setWalkAnim(748);
									player.setAppearanceUpdateRequired(true);
									player.getActionSender().walkTo(face == 2 ? -3 : face == 3 ? 3 : 0, face == 0 ? 3 : 0, true);
									break;
								case 6:
									player.getActionSender().walkTo(face == 2 ? -2 : face == 3 ? 2 : 0, face == 0 ? 2 : 0, true);
									break;
								case 8:
									player.transformNpc = -1;
									break;
								case 10:
									b.stop();
									break;
							}
						}

						@Override
						public void stop() {
							player.setStopPacket(false);
							player.getUpdateFlags().sendAnimation(748);
							player.setAppearanceUpdateRequired(true);
							player.getActionSender().sendMessage("...you emerge on the other side.", true);
							if ((face == 2 || face == 3) && player.getPosition().getX() > 2417) {
								player.getActionSender().sendMapState(0);
							} else if (face == 0) {
								player.getActionSender().sendObject(3235, x, y, 0, 3, 10);
							}
							if (wasRunning) {
								player.getMovementHandler().setRunToggled(true);
							}
						}

					}, 1);
				}
			}, 1);
		}
	}
	
	public static void handleDigMud(final Player player) {
		if(player.stopPlayerPacket()) {
			return;
		}
		player.setStopPacket(true);
		player.getUpdateFlags().sendAnimation(830);
		player.getActionSender().sendMessage("You dig into the pile of mud...", true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int count = 0;
			@Override
			public void execute(CycleEventContainer b) {
				count++;
				switch(count) {
					case 1:
						player.getActionSender().sendMessage("...and find it's a filled in tunnel!", true);
						player.getActionSender().sendObject(-1, 2393, 9650, 0, 3, 10);
						player.getActionSender().sendObject(3215, 2393, 9650, 0, 3, 22);
						break;
					case 2:
						player.getActionSender().sendMessage("You push your way through the tunnel...", true);
						player.timedFadeTeleport(new Position(2392, 9646, 0), 1);
						break;
					case 4:
						b.stop();
						break;
				}
			}
			@Override
			public void stop() {
				player.getActionSender().sendObject(3216, 2393, 9650, 0, 3, 10);
			}

		}, 3);
	}
	
	public static void pickCageLock(final Player player, final int x, final int y) {
		if (player.stopPlayerPacket()) {
			return;
		}
		CacheObject o = ObjectLoader.object(x, y, 0);
		if (o != null) {
			final int face = o.getRotation();
			final Position p = new Position(x, y, 0);
			final boolean outsideCage = Misc.checkClip(player.getPosition(), p, true);
			final Position toBe = outsideCage ? p : new Position(x, face == 3 ? (y - 1) : (y + 1), 0);
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				int pX, pY;
				@Override
				public void execute(CycleEventContainer b) {
					if (!Misc.goodDistance(player.getPosition(), p, 3))
						b.stop();
					pX = player.getPosition().getX();
					pY = player.getPosition().getY();
					if (player.getPosition().equals(toBe) && (face == 3 || face == 1)) {
						if (face == 3) {
							ThieveOther.pickLock(player, p, 3266, 0, 0, pX == x ? 0 : pX >= x ? -1 : 1, pY >= y ? -1 : 1);
						} else if (face == 1) {
							ThieveOther.pickLock(player, p, 3266, 0, 0, pX == x ? 0 : pX >= x ? -1 : 1, pY <= y ? 1 : -1);
						}
						b.stop();
					} else if (face == 2 || face == 0) {
						int pX = player.getPosition().getX();
						ThieveOther.pickLock(player, p, 3268, 50, 15, face == 2 ? (pX <= x ? 1 : -1) : pX >= x ? -1 : 1, 0);
						b.stop();
					} else {
						if(face == 3 || face == 1)
							player.walkTo(toBe, true);
					}
				}
				@Override
				public void stop() {
				}

			}, 1);
		}
	}

	public static void handlePortcullis(final Player player) {
		if(player.stopPlayerPacket()) {
			return;
		}
		player.setStopPacket(true);
		player.getActionSender().sendMessage("The portcullis opens.");
		player.getUpdateFlags().sendFaceToDirection(new Position(2465, 9674, 0));
		player.getUpdateFlags().setFaceToDirection(true);
		for(int i = 0; i < 4; i++)
			player.getActionSender().animateObject(2465, 9674 + (i*2), 0, 1, 10, 455);
		player.walkTo(new Position(2466, 9677, 0), true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int count = 0;
			@Override
			public void execute(CycleEventContainer b) {
				count++;
				if(count == 1) {
					player.getActionSender().walkTo(-2, 0, true);
				} else {
					b.stop();
				}
			}

			@Override
			public void stop() {
				player.setStopPacket(false);
				player.getActionSender().sendMessage("The portcullis closes.");
				for(int i = 0; i < 4; i++)
					player.getActionSender().animateObject(2465, 9674 + (i*2), 0, 1, 10, 454);
				PassTrapHandling.startTrapCycle(player, 0);
				player.getUpdateFlags().setFaceToDirection(false);
			}
		}, 6);
	}
	
	
	public static void handleRopeSwing(final Player player, int object) {
		if(player.stopPlayerPacket()) {
			return;
		}
		if (object == 2275 || object == 2276) {
			player.setStopPacket(true);
			player.getUpdateFlags().sendAnimation(775);
			player.getActionSender().sendMessage("You tie your rope to the rock...", true);
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
							player.getActionSender().sendMessage("You skillfully swing across.", true);
							player.getActionSender().animateObject2(2460, 9699, 0, 751);
							Agility.swingRopeTimed(player, toLand.getX(), toLand.getY(), 1, 0, 2);
						} else {
							PassObjectHandling.handleObstacleFailure(player, 2273, 2463, 9635);
							b.stop();
						}
					} else {
						player.getActionSender().sendMessage("The rope gets tangled in some stalagmites.", true);
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
				player.getActionSender().sendMessage("I can't reach that!", true);
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
							player.getActionSender().sendMessage("You skillfully swing across.", true);
							player.getActionSender().animateObject(2461, 9692, 0, 751);
							Agility.swingRopeTimed(player, toLand.getX(), toLand.getY(), 1, 0, 2);
						} else {
							player.setStopPacket(false);
							player.getUpdateFlags().sendAnimation(751);
							player.getActionSender().sendMessage("You lose your grip on the rope!", true);
							player.getActionSender().animateObject(2461, 9692, 0, 751);
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
				player.getActionSender().sendMessage("You rush across the bridge.", true);
				player.setStopPacket(false);
			}
		}, 3);
	}
	
	public static void sinkInSwamp(final Player player) {
		if(player.stopPlayerPacket()) {
			return;
		}
		player.setStopPacket(true);
		player.getMovementHandler().reset();
		player.getActionSender().sendMessage("You feel yourself being dragged below...", true);
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
				doFallAndDamage(player);
			}
		}, 5);
	}
	
	public static void returnOverBridge(final Player player) {
		if(player.stopPlayerPacket()) {
			return;
		}
		player.setStopPacket(true);
		player.getUpdateFlags().sendAnimation(832);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int count = 0;
			@Override
			public void execute(CycleEventContainer b) {
				count++;
				if(count == 1) {
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
		if(player.stopPlayerPacket()) {
			return;
		}
		if(player.getSkill().getPlayerLevel(Skill.RANGED) < 25) {
			player.getDialogue().sendStatement("You need 25 Ranged to hit the guide rope from here.");
			return;
		}
		Weapon weapon = player.getEquippedWeapon();
		if (weapon != null) {
			if (weapon.getAmmoType() == null) {
				player.getActionSender().sendMessage("You need a bow to shoot at the rope.", true);
				return;
			}
			RangedAmmo ammo = RangedAmmo.getRangedAmmo(player, weapon, false);
			if (player.getEquipment().getItemContainer().get(Constants.ARROWS) == null) {
				player.getActionSender().sendMessage("You have no arrows equipped!", true);
				return;
			}
			if (ammo == null) {
				return;
			} else {
				if (ammo.getProjectileId() != 17 && ammo.getProjectileId() != 1115) {
					player.getDialogue().sendPlayerChat("I don't think these arrows will burn the rope", "seeing as they aren't lit on fire.");
					return;
				}
			}
			player.setStopPacket(true);
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
			player.getActionSender().sendMessage("You fire your arrow at the rope supporting the bridge...", true);
			player.getEquipment().removeAmount(Constants.ARROWS, 1);
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer b) {
					b.stop();
				}

				@Override
				public void stop() {
					player.getActionSender().sendMessage("The arrow impales the rope support.", true);
					new GameObject(3342, 2443, 9718, 0, 3, 10, 3340, 30, false);
					ClippedPathFinder.getPathFinder().findRoute(player, 2447, 9716, true, 0, 0);
					player.setStopPacket(true);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							if (player.getPosition().equals(new Position(2447, 9716, 0))) {
								player.getActionSender().sendMessage("The bridge falls.", true);
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
		if(player.stopPlayerPacket() || !player.inUndergroundPass()) {
			return false;
		}
		int level = player.getSkill().getPlayerLevel(Skill.AGILITY);
		final int pX = player.getPosition().getX(), pY = player.getPosition().getY();
		final CacheObject o = ObjectLoader.object(object, x, y, player.getPosition().getZ());
		switch(object) {
			case 3235:
			case 3237: //pipes
				handlePipeCrawl(player, object, x, y);
				return true;
			case 3268:
			case 3266: //cell doors
				pickCageLock(player, x, y);
				return true;
			case 3218:
			case 3219:
				if (player.getPosition().getY() > 9662) {
					player.fadeTeleport(player.getQuestStage(44) >= UndergroundPass.UNICORN_KILLED ? new Position(2375, 9609, 0) : new Position(2400, 9609, 0));
				} else {
					player.fadeTeleport(new Position(2371, 9667, 0));
				}
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						b.stop();
					}
					@Override
					public void stop() {
						if (player.getPosition().getY() > 9662) {
							player.getActionSender().sendMapState(0);
							PassTrapHandling.startTrapCycle(player, 2);
						} else {
							player.getActionSender().sendMapState(2);
						}
					}
				}, player.getPosition().getY() > 9662 ? 4 : 6);
				return true;
			case 3264: //well
				player.getActionSender().sendMessage("You feel the grip of icy hands all around you...", true);
				boolean canContinue = true;
				if (player.getQuestStage(44) < UndergroundPass.CAN_USE_WELL) {
					for (boolean b : player.getQuestVars().wellItemsDestroyed) {
						if (!b) {
							canContinue = false;
						}
					}
				}
				if (canContinue) {
					for (int i = 0; i < 4; i++) {
						player.getQuestVars().wellItemsDestroyed[i] = false;
					}
					Ladders.climbLadder(player, new Position(2423, 9660, 0));
					if (player.getQuestStage(44) == UndergroundPass.ENTER_CAVES) {
						player.setQuestStage(44, UndergroundPass.CAN_USE_WELL);
					}
				}
				final boolean teleported = canContinue;
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						b.stop();
					}

					@Override
					public void stop() {
						player.setStopPacket(false);
						if (teleported) {
							player.getActionSender().sendMessage("...slowly dragging you further down into the caverns.", true);
						} else {
							player.getActionSender().sendMessage("...the hands try to strangle you!", true);
							player.hit(10, HitType.NORMAL);
						}
					}
				}, 3);
				return true;
			case 3307:
				Ladders.climbLadder(player, new Position(2418, 9674, 0));
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						b.stop();
					}

					@Override
					public void stop() {
						PassTrapHandling.startTrapCycle(player, 0);
					}
				}, 5);
				return true;
			case 3220:
			case 3221:
				if (player.getPosition().getX() > 2180) {
					if (player.getQuestStage(44) >= UndergroundPass.IBANS_LAIR_OPEN) {
						for (int i = 0; i < 4; i++) {
							player.getQuestVars().wellItemsDestroyed[i] = false;
						}
						player.fadeTeleport(new Position(2173, 4725, 1));
					} else {
						player.getActionSender().sendMessage("The doors won't budge. You hear a faint cackling.", true);
					}
					return true;
				} else {
					player.fadeTeleport(new Position(2370, 9719, 0));
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							PassTrapHandling.startTrapCycle(player, 2);
						}
					}, 6);
					return true;
				}
			case 3276:
				player.setStopPacket(true);
				player.isCrossingObstacle = true;
				player.getActionSender().sendMessage("You start to cross the stone bridge...", true);
				final boolean wasRunning = player.getMovementHandler().isRunToggled();
				player.getMovementHandler().setRunToggled(false);
				player.getMovementHandler().reset();
				player.setWalkAnim(762);
				player.setAppearanceUpdateRequired(true);
				int modifier = player.getPosition().getX() < x ? 2 : -2;
				player.getUpdateFlags().setFace(new Position(player.getPosition().getX() + modifier, y, 0));
				player.getActionSender().walkTo(modifier, 0, true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					int count = 0;
					@Override
					public void execute(CycleEventContainer b) {
						player.setStopPacket(true);
						count++;
						int level = player.getSkill().getPlayerLevel(Skill.AGILITY);	
						if (count == 1 && !SkillHandler.skillCheck((level + 40) > 99 ? 99 : (level + 40), 20, 0)) {
							player.getActionSender().sendMessage("...and slip and fall halfway across!", true);
							handleObstacleFailure(player, 3276, x, y);
							b.stop();
						} else if (count == 2) {
							player.getActionSender().sendMessage("...and make it.", true);
						} else if (count >= 3) {
							b.stop();
						}
					}

					@Override
					public void stop() {
						player.setWalkAnim(-1);
						player.setAppearanceUpdateRequired(true);
						player.setStopPacket(false);
						if (wasRunning)
							player.getMovementHandler().setRunToggled(true);
						player.getMovementHandler().reset();
					}
				}, 2);
				return true;
			case 3238:
				if(player.getPosition().getX() < 2374) {
					player.getActionSender().sendMessage("I can't reach that from here!", true);
					return true;
				}
				player.getActionSender().sendMessage("You put your foot on the ledge and try to edge across.", true);
				player.isCrossingObstacle = true;
				player.setStopPacket(true);
				if(x == 2374 && (y == 9644 || y == 9643)) {
					Agility.crossLedge(player, 2374, 9638, 1, 8, 0, 0);
				} else {
					Agility.crossLedge(player, 2374, 9644, 3, 8, 0, 0);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						int count = 0;
						@Override
						public void execute(CycleEventContainer b) {
							count++;
							if(player.getPosition().getX() == 2374 && player.getPosition().getY() == 9644) {
								player.getActionSender().walkTo(1, 0, true);
								b.stop();
							} else if (count > 10) {
								b.stop();
							}
						}

						@Override
						public void stop() {
							player.setStopPacket(false);
							player.isCrossingObstacle = false;
						}
					}, 1);
				}
				return true;
			case 3360:
				if(x == 2417 && y == 9658) {
					player.setStopPacket(true);
					player.getActionSender().sendMessage("You search the crate...");
					player.getUpdateFlags().sendAnimation(832);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							if(!player.getQuestVars().receivedCrateFood) {
								player.getQuestVars().receivedCrateFood = true;
								player.getActionSender().sendMessage("You find some food.");
								player.getInventory().addItemOrDrop(new Item(2327, 2)); //meat pie
								player.getInventory().addItemOrDrop(new Item(329, 2)); //salmon
							} else {
								player.getActionSender().sendMessage("You find nothing of interest.");
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
				return false;
			case 3263:
				sinkInSwamp(player);
				return true;
			case 3265:
				if(x == 2443 && y == 9651) {
					player.fadeTeleport(new Position(2475, 9715, 0));
					player.getUpdateFlags().sendAnimation(828);
					player.getActionSender().sendMessage("You climb up the pile of mud and rocks...", true);
					player.getActionSender().sendMessage("It leads into darkness, the stench is unbearable.", true);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}
						@Override
						public void stop() {
							player.getActionSender().sendMessage("You surface by the swamp, covered in muck.", true);
						}
					}, 5);
					return true;
				}
			return false;
			case 3365:
				player.getActionSender().sendMessage("You crawl out of the pit.", true);
				Ladders.climbLadder(player, new Position(2478, 9678, 0));
				GridMazeHandler.startGridCheck(player);
				return true;
			case 3309: //rockslide
					if(Misc.random(3) == 1 || SkillHandler.skillCheck(level, 1, 0)) {
						if(o != null) {
							if(x == 2482 && y == 9679 && player.getPosition().getX() > x) {
								GridMazeHandler.startGridCheck(player);
							}
							player.getMovementHandler().resetOnWalkPacket();
							player.getUpdateFlags().sendAnimation(839);
							if(Misc.checkClip(new Position(x, y, 0), new Position(x + 1, y, 0), true) && Misc.checkClip(new Position(x, y, 0), new Position(x - 1, y, 0), true)) {
								boolean condition = pX > x;
								player.getUpdateFlags().sendForceMovement(player, condition ? -2 : 2, 0, 30, 60, condition ? 3 : 1, 3, true);
							} else {
								boolean condition = pY > y;
								player.getUpdateFlags().sendForceMovement(player, 0, condition ? -2 : 2, 30, 60, condition ? 2 : 0, 3, true);
							}
						}
					} else {
						handleObstacleFailure(player, object, x, y);
					}
					return true;
			case 3230:
				PassTrapHandling.handleDisarmTrap(player, object, 2244, null);
				return true;
			case 2274: //rope swing
				handleRopeSwing(player, object);
				return true;
			case 3337:
				if(x == 2466 && y == 9672) {
					handlePortcullis(player);
					return true;
				}
				return false;
			case 3241: //lever
				if (x == 2436 && y == 9716) {
					returnOverBridge(player);
					return true;
				}
				return false;
		}
		return false;
	}
	
	public static void handleObstacleFailure(final Player player, final int object, final int x, final int y) {
		switch(object) {
			case 3276:
				player.setStopPacket(true);
				if(y == 9632) {
					player.getUpdateFlags().sendAnimation(player.getUpdateFlags().getFace().getX() < x ? 771 : 770);
					player.timedMovePlayer(new Position(x, y + 2, 0), 1);
				} else {
					player.getUpdateFlags().sendAnimation(player.getUpdateFlags().getFace().getX() < x ? 770 : 771);
					player.timedMovePlayer(new Position(x, y - 2, 0), 1);
				}
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					int count = 0;
					@Override
					public void execute(CycleEventContainer b) {
						count++;
						if (count == 1) {
							player.getUpdateFlags().sendAnimation(-1);
						}
						if (count >= 2) {
							player.getActionSender().sendMessage("You are injured by the spikes.", true);
							player.hit(Misc.random(5) + 8, HitType.NORMAL);
							b.stop();
						}
					}

					@Override
					public void stop() {
						player.setStopPacket(false);
						player.isCrossingObstacle = false;
					}
				}, 1);
				break;
			case 2274: //swamp rope swing
				player.movePlayer(new Position(2462, 9692, 0));
				sinkInSwamp(player);
				break;
			case 2273: //rock rope swing
				doFallAndDamage(player);
				break;
			case 3309: //rockslide
				player.getActionSender().sendMessage("You slip on the rocks!", true);
				player.getUpdateFlags().sendAnimation(846);
				player.hit(Misc.random(3), HitType.NORMAL);
				break;
		}
	}
	
	public static void doFallAndDamage(final Player player) {
		player.setStandAnim(-1);
		player.fadeTeleport(new Position(2485, 9649, 0));
		player.getActionSender().sendMessage("You tumble deep into the crevasse.", true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				b.stop();
			}

			@Override
			public void stop() {
				player.setStopPacket(false);
				player.getActionSender().sendMessage("You land battered and bruised at the base.", true);
				player.hit(Misc.random(13), HitType.NORMAL);
			}
		}, 5);
	}
	
	public static void readTablets(final Player player, int id) {
		ActionSender a = player.getActionSender();
		a.sendInterface(3023);
		for (int i = 0; i < 11; i++) {
			a.sendString("", 3026 + i);
		}
		switch(id) {
			case 3295:
				a.sendString("All those who thirst for knowledge,", 3026);
				a.sendString("Bow down to the lord.", 3027);
				a.sendString("All you that crave eternal life,", 3028);
				a.sendString("Come and meet your god.", 3029);
				a.sendString("For no man nor beast", 3030);
				a.sendString("can cast a spell", 3031);
				a.sendString("against the wake of eternal hell.", 3032);
				break;
			case 3296:
				a.sendString("Most men do live in fear of death", 3026);
				a.sendString("that it might steal their soul.", 3027);
				a.sendString("Some work and pray,", 3028);
				a.sendString("to shield their life", 3029);
				a.sendString("from the ravages of the cold.", 3030);
				a.sendString("But only those who embrace the end", 3031);
				a.sendString("can truly make their life extend.", 3032);
				a.sendString("And when all hope begins to fade", 3033);
				a.sendString("look above and use nature", 3034);
				a.sendString("as your aid.", 3035);
				break;
			case 3297:
				a.sendString("And now our god has given us", 3026);
				a.sendString("one who is from our own.", 3027);
				a.sendString("A saviour who once sat upon", 3028);
				a.sendString("his father's glorious throne.", 3029);
				a.sendString("It is in your name that we", 3030);
				a.sendString("will lead the attack,", 3031);
				a.sendString("Iban, Son of Zamorak!", 3032);
				break;
			case 3298:
				a.sendString("Here lies the sacred well,", 3026);
				a.sendString("entrance to Iban's hell.", 3027);
				a.sendString("He blesses all his disciples keen.", 3028);
				a.sendString("'We bathe in pure evil' they yell.", 3029);
				a.sendString("The force of darkness is strong,", 3030);
				a.sendString("the wait for morning forever long.", 3031);
				a.sendString("If a light should break the night", 3032);
				a.sendString("the dark will rise to win the fight.", 3033);
				break;
			case 3299:
				a.sendString("Leave this battered corpse be,", 3026);
				a.sendString("for now he lives as spirit alone.", 3027);
				a.sendString("Turn and leave, run and flee", 3028);
				a.sendString("before the Soulless writhe and moan.", 3029);
				a.sendString("It is the soil that shall rise", 3030);
				a.sendString("to turn away the mites and flies.", 3031);
				a.sendString("Only as flesh becomes ash,", 3032);
				a.sendString("and wood becomes dust,", 3033);
				a.sendString("...will Iban's corpse embrace", 3034);
				a.sendString("nature's eternal lust.", 3035);
				break;
		}
	}
	
}
