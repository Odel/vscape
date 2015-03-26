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
import com.rs2.net.ActionSender;
import com.rs2.util.Misc;
import com.rs2.util.clip.ClippedPathFinder;

public class PassObjectHandling {

	public static void handlePortcullis(final Player player) {
		if(player.stopPlayerPacket()) {
			return;
		}
		player.setStopPacket(true);
		player.getActionSender().sendMessage("The portcullis opens.");
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
			}
		}, 3);
	}
	
	
	public static void handleRopeSwing(final Player player, int object) {
		if(player.stopPlayerPacket()) {
			return;
		}
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
		if(player.stopPlayerPacket()) {
			return;
		}
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
		if(player.stopPlayerPacket()) {
			return;
		}
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
		if(player.stopPlayerPacket()) {
			return false;
		}
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
	
	public static void startTrapCycle(final Player player) {
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int pX, pY;
			int trapReset = 0;
			int count = 0;
			@Override
			public void execute(CycleEventContainer b) {
				pX = player.getPosition().getX();
				pY = player.getPosition().getY();
				if(trapReset < count) {
					count = 0;
					trapReset = 0;
				}
				if(count > 0)
					count++;
				if(!player.Area(2378, 2465, 9665, 9700)) {
					b.stop();
				} else {
					if (count == 0 && !player.getQuestVars().immuneToTraps) {
						if ((((pX == 2443 || pX == 2440) && pY == 9677) || ((pX == 2435 || pX == 2432 || pX == 2430) && pY == 9676)) && count == 0) {
							if (pY == 9676)
								pY = 9675;
							player.getActionSender().animateObject(pX, pY, 0, pY == 9675 ? 1 : 3, 10, 459);
							player.getUpdateFlags().setForceChatMessage("Ouch!");
							player.getUpdateFlags().sendAnimation(player.getBlockAnimation());
							player.hit(Misc.random(3) + 5, HitType.NORMAL);
							trapReset = 3;
							count++;
						} else if ((pX == 2418 && (pY == 9681 || pY == 9685)) || (pX == 2416 && pY == 9689) || (pX == 2408 && pY == 9674) || ((pX == 2404 || pX == 2401) && pY == 9675) || (pX == 2396 && pY == 9677) || (pX == 2393 && pY == 9676)) {
							final int x = pX, y = pY;
							if (ObjectLoader.object("Flat rock", x, y, 0) != null) {
								int faceCheck = 2;
								if (ObjectLoader.object("Stalagmites", x, y - 1, 0) != null)
									faceCheck = 1;
								final int face = faceCheck;
								player.getUpdateFlags().setForceChatMessage("Ouch!");
								player.getActionSender().animateObject(face == 2 ? x - 1 : x, face == 2 ? y : y - 1, 0, 462);
								player.hit(Misc.random(3) + 5, HitType.NORMAL);
								player.getUpdateFlags().sendAnimation(player.getBlockAnimation());
								trapReset = 3;
								count++;
								CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
									@Override
									public void execute(CycleEventContainer b) {
										b.stop();
									}
									@Override
									public void stop() {
										player.getActionSender().animateObject(face == 2 ? x - 1 : x, face == 2 ? y : y - 1, 0, 463);
									}
								}, 2);
							}
						}
					}
				}
			}

			@Override
			public void stop() {
			}
		}, 1);
	}
	
	public static void handleDisarmTrap(final Player player, final int object, final int anim, final Position toTravel) {
		final int x = player.getClickX(), y = player.getClickY();
		if(player.stopPlayerPacket()) {
			return;
		}
		player.getQuestVars().immuneToTraps = true;
		switch(object) {
			case 3234: //wall spikes
				player.getUpdateFlags().sendAnimation(anim);
				player.setStopPacket(true);
				player.getUpdateFlags().sendFaceToDirection(new Position(x, y, 0));
				player.getUpdateFlags().setFaceToDirection(true);
				player.getActionSender().sendMessage("You try to disarm the trap...");
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					int count = 0;
					@Override
					public void execute(CycleEventContainer b) {
						count++;
						if (count >= 2) {
							int level = player.getSkill().getPlayerLevel(Skill.THIEVING);
							if (SkillHandler.skillCheck((level + 40) > 99 ? 99 : (level + 40), 20, 0)) {
								player.getActionSender().sendMessage("...and succeed, you quickly walk past.");
								player.getActionSender().walkTo(player.getPosition().getX() < x ? 2 : -2, 0, true);
							} else {
								int newY = y;
								if (y == 9676)
									newY = 9675;
								player.getActionSender().animateObject(x, newY, 0, newY == 9675 ? 1 : 3, 10, 459);
								player.getActionSender().sendMessage("...and fail, activating the trap!");
								player.getUpdateFlags().setForceChatMessage("Ouch!");
								player.getUpdateFlags().sendAnimation(player.getBlockAnimation());
								player.hit(Misc.random(3) + 5, HitType.NORMAL);
							}
							b.stop();
						}
					}
					@Override
					public void stop() {
						player.setStopPacket(false);
						player.getQuestVars().immuneToTraps = false;
					}
				}, 2);
				break;
			case 3361: //log trap
				player.getUpdateFlags().sendAnimation(anim);
				player.setStopPacket(true);
				player.getActionSender().sendMessage("You try to disarm the trap...");
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					int count = 0;
					boolean failed = false;
					@Override
					public void execute(CycleEventContainer b) {
						count++;
						if (count == 2) {
							int level = player.getSkill().getPlayerLevel(Skill.THIEVING);
							if (SkillHandler.skillCheck((level + 40) > 99 ? 99 : (level + 40), 20, 0)) {
								player.getActionSender().sendMessage("...and succeed long enough to take the Orb.");
								player.getInventory().addItemOrDrop(new Item(UndergroundPass.ORB_OF_LIGHT_4));
								player.getActionSender().sendObject(-1, 2382, 9668, 0, 0, 10);
							} else {
								player.getActionSender().sendMessage("...and fail, activating the trap!");
								player.getActionSender().animateObject(2380, 9667, 0, 458); //swing
								player.getUpdateFlags().sendAnimation(848); //headspin
								player.getUpdateFlags().sendGraphic(254, 100 << 16);
								player.hit(Misc.random(5) + 10, HitType.NORMAL);
								//player.getActionSender().animateObject(2380, 9667, 0, 457); //dismantle
								failed = true;
							}
						} else if (count > 4 - (failed ? 1 : 0)) {
							b.stop();
						}
					}
					@Override
					public void stop() {
						player.setStopPacket(false);
						player.getQuestVars().immuneToTraps = false;
						if (failed) {
							
						}
					}
				}, 2);
				break;
			case 3230:
				player.getUpdateFlags().sendAnimation(anim);
				if(ObjectLoader.object("Flat rock", x, y, 0) != null) {
					int faceCheck = 2;
					if(ObjectLoader.object("Stalagmites", x, y - 1, 0) != null)
						faceCheck = 1;
					player.setStopPacket(true);
					player.getActionSender().sendMessage("You search the flat rock...");
					final int face = faceCheck;
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						int count = 0;
						boolean failed = false;
						@Override
						public void execute(CycleEventContainer b) {
							count++;
							if(count == 3) {
								int level = player.getSkill().getPlayerLevel(Skill.THIEVING);
								if (SkillHandler.skillCheck((level + 30) > 99 ? 99 : (level + 30), 30, 0)) {
									player.getDialogue().sendStatement("You notice the rock is a pressure plate rigged as a trap.");
								} else {
									player.getActionSender().sendMessage("You spring the trap!");
									player.getActionSender().animateObject(face == 2 ? x - 1 : x, face == 2 ? y : y - 1, 0, 462);
									player.getUpdateFlags().setUpdateRequired(true);
									player.getUpdateFlags().sendAnimation(846);
									player.hit(Misc.random(3) + 5, HitType.NORMAL);
									failed = true;
								}
							} else if (count > 4 - (failed ? 1 : 0)) {
								b.stop();
							}
						}

						@Override
						public void stop() {
							player.setStopPacket(false);
							player.getQuestVars().immuneToTraps = false;
							if(failed) {
								player.getActionSender().animateObject(face == 2 ? x - 1 : x, face == 2 ? y : y - 1, 0, 463);
							} else {
								player.getActionSender().removeInterfaces();
								player.getActionSender().sendMessage("You quickly and carefully walk over the rock.");
								player.getActionSender().walkTo(face == 2 ? 0 : (player.getPosition().getX() <= x ? 2 : -2), face == 2 ? (player.getPosition().getY() < y ? 2 : -2) : 0, true);
							}
						}
					}, 2);
				}
				break;
			case 3231: //flat rock w/ plank
				player.getUpdateFlags().sendAnimation(anim);
				if(ObjectLoader.object("Flat rock", x, y, 0) != null) {
					int face = 2;
					if(ObjectLoader.object("Stalagmites", x, y - 1, 0)!= null)
						face = 1;
					player.setStopPacket(true);
					player.getActionSender().sendObject(-1, x, y, 0, 0, 10);
					new GameObject(3231, player.getClickX(), player.getClickY(), 0, face, 22, -1, 3, true);
					player.getActionSender().sendMessage("You place the plank across the flat rock...");
					final int faceCheck = face;
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						int count = 0;
						@Override
						public void execute(CycleEventContainer b) {
							count++;
							if(count == 1) {
								player.getActionSender().sendMessage("...and quickly walk over.");
								player.getActionSender().walkTo(faceCheck == 2 ? 0 : (player.getPosition().getX() <= x ? 2 : -2), faceCheck == 2 ? (player.getPosition().getY() < y ? 2 : -2) : 0, true);
							} else {
								b.stop();
							}
						}

						@Override
						public void stop() {
							player.setStopPacket(false);
							player.getQuestVars().immuneToTraps = false;
							player.getActionSender().sendObject(3230, x, y, 0, 0, 10);
						}
					}, 2);
				}
			break;
		}
	
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
				a.sendString("from the ravages of the cold", 3030);
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
		}
	}
	
}
