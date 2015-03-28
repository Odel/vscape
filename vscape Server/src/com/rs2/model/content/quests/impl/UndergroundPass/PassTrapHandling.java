package com.rs2.model.content.quests.impl.UndergroundPass;

import com.rs2.cache.object.ObjectLoader;
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

public class PassTrapHandling {
	
	public static void springOrbOfLightTrap(final Player player) {
		player.getActionSender().sendMessage("You activate the trap!");
		player.getActionSender().animateObject(2380, 9667, 0, 3, 10, 458); //swing
		player.getUpdateFlags().sendAnimation(846, 1);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				b.stop();
			}

			@Override
			public void stop() {
				player.getUpdateFlags().sendAnimation(848); //headspin
				player.getUpdateFlags().sendGraphic(254, 100 << 16);
				player.hit(Misc.random(5) + 10, HitType.NORMAL);
			}
		}, 2);
	}
	
	public static void startTrapCycle(final Player player, int floorLevel) {
		switch (floorLevel) {
			case 2: //zombie floor
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					int pX, pY;
					int trapReset = 0;
					int count = 0;

					@Override
					public void execute(CycleEventContainer b) {
						pX = player.getPosition().getX();
						pY = player.getPosition().getY();
						if (trapReset < count) {
							count = 0;
							trapReset = 0;
						}
						if (count > 0)
							count++;
						if (!player.Area(2389, 2430, 9697, 9729) && !player.Area(2367, 2390, 9663, 9728)) {
							b.stop();
						} else {
							if (count == 0 && !player.getQuestVars().immuneToTraps) {
								if ((pX == 2406 && (pY == 9719 || pY == 9725))) {
									final int x = pX, y = pY;
									if (ObjectLoader.object("Flat rock", x, y, 0) != null) {
										player.getUpdateFlags().setForceChatMessage("Ouch!");
										player.getActionSender().animateObject(x, y - 1, 0, 462);
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
												player.getActionSender().animateObject(x, y - 1, 0, 463);
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
				break;
			case 0: //Ground / entry level
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					int pX, pY;
					int trapReset = 0;
					int count = 0;

					@Override
					public void execute(CycleEventContainer b) {
						pX = player.getPosition().getX();
						pY = player.getPosition().getY();
						if (trapReset < count) {
							count = 0;
							trapReset = 0;
						}
						if (count > 0)
							count++;
						if (!player.Area(2378, 2465, 9665, 9700)) {
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
										if (ObjectLoader.object("Stalagmites", x, y - 1, 0) != null) {
											faceCheck = 1;
										}
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
				break;
		}
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
								if(!player.getInventory().ownsItem(UndergroundPass.ORB_OF_LIGHT_4)) {
									player.getActionSender().sendMessage("...and succeed long enough to take the Orb.");
									player.getInventory().addItemOrDrop(new Item(UndergroundPass.ORB_OF_LIGHT_4));
									player.getActionSender().sendObject(-1, 2382, 9668, 0, 0, 10);
								} else {
									player.getActionSender().sendMessage("...and succeed.");
									player.getDialogue().sendPlayerChat("I have no need for another one of these.");
								}
							} else {
								springOrbOfLightTrap(player);
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
}
