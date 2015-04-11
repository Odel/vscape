package com.rs2.model.content.quests.impl.UndergroundPass;

import com.rs2.model.Graphic;
import com.rs2.model.Position;
import com.rs2.model.content.combat.CombatCycleEvent;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;
import com.rs2.util.clip.ClippedPathFinder;
import java.util.ArrayList;

public class PassNpcHandling {
	public static Npc WITCHS_CAT;
	public static Npc KAMEN;
	public static Npc IBAN;
	public static final MinigameAreas.Area SPELL_AREA = new MinigameAreas.Area(2135, 2142, 4641, 4654, 1);
	private static final String[] IBAN_MESSAGES = new String[] {"An imposter dares desecrate this sacred place!","...Home to the only true child of Zamorak.", "Join the damned, mortal!", "Begone from my temple!", "You will die frail mortal."};
	public static boolean ibanEncounterRunning = false;
	
	public static void assignNpcs(Npc npc) {
		switch(npc.getNpcId()) {
			case UndergroundPass.WITCHS_CAT:
				WITCHS_CAT = npc;
				break;
			case UndergroundPass.KAMEN:
				KAMEN = npc;
				break;
			case UndergroundPass.LORD_IBAN:
				IBAN = npc;
				break;		
		}
	}
	
	public static void handlePushBoulder(final Player player, final Npc npc) {
		if (player.stopPlayerPacket()) {
			return;
		}
		player.setStopPacket(true);
		player.getUpdateFlags().sendAnimation(827);
		player.getActionSender().sendMessage("You use the piece of railing as leverage...", true);
		player.getActionSender().sendMessage("...and tip the boulder onto it's side...", true);
		player.getInventory().removeItem(new Item(UndergroundPass.PIECE_OF_RAILING));
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int count = 0;
			@Override
			public void execute(CycleEventContainer b) {
				count++;
				if (count == 1) {
					player.getActionSender().sendMessage("...It tumbles down the slope...", true);
					player.getActionSender().shakeScreen(1, 10, 10, 4);
					npc.walkTo(new Position(npc.getPosition().getX(), npc.getPosition().getY() + 8), false);
				}
				if (count >= 3) {
					player.getActionSender().resetCamera();
					player.getActionSender().shakeScreen(3, 25, 25, 100);
					b.stop();
				}
			}
			@Override
			public void stop() {
				player.setStopPacket(false);
				NpcLoader.destroyNpc(npc);
				NpcLoader.newNPC(986, 2396, 9595, 0, 2);
				player.movePlayer(new Position(player.getPosition().getX() - 25, player.getPosition().getY(), 0));
				player.getDialogue().sendPlayerChat("I heard something breaking.", Dialogues.SAD);
				player.getActionSender().resetCamera();
				if(player.getQuestStage(44) == UndergroundPass.CAN_USE_WELL) {
					player.setQuestStage(44, 4);
				}
			}
		}, 4);
	}
	
	public static void takeWitchsCat(final Player player) {
		player.getUpdateFlags().sendAnimation(827);
		player.getInventory().addItem(new Item(UndergroundPass.WITCHS_CAT_ITEM));
		player.getQuestVars().takenWitchCat = true;
		Npc cat = PassNpcHandling.WITCHS_CAT;
		player.getNpcs().remove(cat);
		cat.setVisible(false);
		cat.getUpdateFlags().setUpdateRequired(true);
		cat.setVisible(true);
	}
	
	public static void handleClickKamen(final Player player) {
		player.setStopPacket(true);
		player.getActionSender().sendMessage("He looks a little drunk.");
		KAMEN.getUpdateFlags().setForceChatMessage("Hic!");
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				b.stop();
			}

			@Override
			public void stop() {
				player.setStopPacket(false);
				if (Misc.goodDistance(player.getPosition(), KAMEN.getPosition(), 1)) {
					Dialogues.sendDialogue(player, KAMEN.getNpcId(), 2, 0);
				}
			}
		}, 3);
	}
	
	public static void spawnDerangedKoftik(final Player player) {
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				b.stop();
			}

			@Override
			public void stop() {
				player.setStopPacket(false);
				NpcLoader.spawnNpc(player, new Npc(UndergroundPass.KOFTIK_4), false, false);
				Dialogues.startDialogue(player, UndergroundPass.KOFTIK_4 + 20000);
			}
		}, 6);
	}
	
	public static void handleKalragDeath(final Player player) {
		if (player.getQuestStage(44) == UndergroundPass.IBANS_DEMISE && !player.getQuestVars().getIbanDollElements()[2]) {
			player.setStopPacket(true);
			player.getActionSender().sendMessage("Kalrag slumps to the floor...");
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				int count = 0;
				@Override
				public void execute(CycleEventContainer b) {
					switch (++count) {
						case 1:
							player.getActionSender().sendMessage("...poison flows from the corpse over the soil.");
							break;
						case 3:
							player.getActionSender().sendMessage("You smear the doll of Iban in the poisoned blood. It smells horrific.");
							player.getQuestVars().setIbanDollElements(true, 2);
							boolean dollComplete = true;
							for (boolean bool : player.getQuestVars().getIbanDollElements()) {
								dollComplete = bool;
							}
							if (dollComplete && player.getQuestStage(44) == UndergroundPass.IBANS_DEMISE) {
								player.setQuestStage(44, UndergroundPass.DOLL_COMPLETE);
							}
							player.getQuestVars().killedKalrag = true;
							for (Npc npc : player.getNpcs()) {
								if (npc != null && npc.getNpcId() == 977) {
									CombatCycleEvent.startCombat(npc, player);
								}
							}
							b.stop();
							break;
					}
				}

				@Override
				public void stop() {
					player.setStopPacket(false);
				}
			}, 3);
		}
	}
	
	public static void startIbanEncounter(final Player player) {
		ibanEncounterRunning = true;
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				b.stop();
			}
			@Override
			public void stop() {
				player.setInCutscene(true);
				player.setStopPacket(true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					int count = 0;
					int sayingIndex = 0;
					boolean doSpells = false;
					boolean justHit = false;
					@Override
					public void execute(CycleEventContainer b) {
						if (player == null || IBAN == null || !player.Area(2129, 2143, 4639, 4656) || player.getQuestVars().threwDollIntoWell) {
							b.stop();
							return;
						}
						if(count%3 == 0) {
							justHit = false;
						}
						ArrayList<Position> spells = new ArrayList<>();
						count++;
						if (doSpells) {
							player.setStopPacket(false);
							IBAN.getUpdateFlags().sendAnimation(347);
							for (int i = 0; i < 10 + Misc.random(15); i++) {
								Position p = MinigameAreas.randomPosition(SPELL_AREA);
								if (!spells.contains(p)) {
									player.getActionSender().sendStillGraphic(new Graphic(83, 0), p);
									spells.add(p);
								}
							}
						} else {
							player.setStopPacket(true);
						}
						for (Position p : spells) {
							if (player.getPosition().equals(p) && !justHit) {
								justHit = true;
								player.getUpdateFlags().sendAnimation(848); //headspin
								player.getUpdateFlags().sendGraphic(254, 100 << 16);
								player.hit(Misc.random(3) + 8, HitType.NORMAL);
								Position knockback = new Position(player.getPosition().getX() + 2, player.getPosition().getY(), 1);
								Position knockback2 = new Position(player.getPosition().getX() + 1, player.getPosition().getY(), 1);
								if(Misc.checkClip(player.getPosition(), knockback, true)) {
									player.movePlayer(knockback);
								} else if(Misc.checkClip(player.getPosition(), knockback2, true)) {
									player.movePlayer(knockback2);
								} else {
									player.movePlayer(new Position(2143, 4648, 1));
								}
							}
						}
						switch (count) {
							case 4:
								player.getActionSender().sendMessage("Iban seems to sense danger.", true);
								IBAN.getUpdateFlags().setForceChatMessage(IBAN_MESSAGES[sayingIndex]);
								player.getActionSender().sendMessage("Iban: @blu@Who dares bring the witch's magic into my temple?", true);
								break;
							case 8:
								player.getActionSender().sendMessage("His eyes fixate on you as he raises his arm...", true);
								doSpells = true;
								break;
						}
						if (sayingIndex >= IBAN_MESSAGES.length) {
							b.stop();
							sendIbanSaying(player, "You waste my time! Begone!");
							player.getUpdateFlags().sendGraphic(83);
							player.getUpdateFlags().sendAnimation(2304);
							player.fadeTeleport(new Position(2146, 4648, 1));
						} else if(count > 8 && count%8 == 0) {
							sendIbanSaying(player, IBAN_MESSAGES[sayingIndex]);
							sayingIndex++;
						}
					}

					@Override
					public void stop() {
						if (player.getQuestVars().threwDollIntoWell) {
							endIbanEncounter(player);
						} else {
							player.setStopPacket(false);
							player.setInCutscene(false);
							ibanEncounterRunning = false;
						}
					}
				}, 1);
			}
		}, 1);
	}
	
	public static void endIbanEncounter(final Player player) {
		sendIbanSaying(player, "What's happening? It's dark here... so dark!");
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int count = 1;
			int sayingIndex = 0;
			boolean doSpells = false;
			
			@Override
			public void execute(CycleEventContainer b) {
				player.setStopPacket(true);
				if (player == null) {
					b.stop();
					return;
				}
				count++;
				switch (count) {
					case 2:
						sendIbanSaying(player, "I'm falling into the dark, what have you done?");
						break;
					case 3:
						player.getActionSender().sendMessage("Iban begins clutching at his throat...", true);
						break;
					case 4:
						sendIbanSaying(player, "NOOOOOO!");
						break;
					case 5:
						startIbanDeath();
						player.getActionSender().sendMessage("Iban slumps motionless in his throne...", true);
						player.getActionSender().sendMessage("A roar comes from the pit of the damned.", true);
						break;
					case 6:
						player.getActionSender().sendMessage("The infamous Iban has finally gone to rest.", true);
						ClippedPathFinder.getPathFinder().findRoute(player, 2135, 4648, true, 0, 0);
						break;
					case 7:
						player.getUpdateFlags().sendFaceToDirection(new Position(2134, 4648, 1));
						player.getUpdateFlags().setFaceToDirection(true);
						player.getUpdateFlags().sendAnimation(832);
						player.getActionSender().sendMessage("Beside Iban's corpse you find his staff and some runes.", true);
						player.getInventory().replaceItemWithItem(new Item(UndergroundPass.DOLL_OF_IBAN), new Item(UndergroundPass.IBANS_STAFF));
						player.getInventory().addItem(new Item(560, 150)); //death runes
						player.getInventory().addItem(new Item(554, 300)); //fire runes
						break;
					case 8:
						fallingRocks(player);
						player.getActionSender().sendMessage("Suddenly around you rocks crash to the floor as the ground begins to shake...", true);
						break;
					case 9:
						player.getActionSender().sendMessage("...The temple walls begin to collapse in...", true);
						break;
					case 10:
						player.getActionSender().sendMessage("...And you're thrown from the temple platform.", true);
						b.stop();
						break;
				}
			}
			@Override
			public void stop() {
				ibanEncounterRunning = false;
				if (player != null) {
					player.setStopPacket(false);
					player.setInCutscene(false);
					player.fadeTeleport(UndergroundPass.FALL_OFF_TEMPLE);
					player.setQuestStage(44, UndergroundPass.IBAN_DEAD);
				}
			}
		}, 4);
	}
	
	public static void startIbanDeath() {
		CycleEventHandler.getInstance().addEvent(IBAN, new CycleEvent() {
			int count = 0;

			@Override
			public void execute(CycleEventContainer b) {
				count++;
				IBAN.getUpdateFlags().sendAnimation(1655);
				if(count >= 20) {
					b.stop();
				}
			}

			@Override
			public void stop() {
			}
		}, 1);
	}
	
	public static void fallingRocks(final Player player) {
		player.getActionSender().shakeScreen(1, 10, 10, 4);
		CycleEventHandler.getInstance().addEvent(IBAN, new CycleEvent() {
			int count = 0;

			@Override
			public void execute(CycleEventContainer b) {
				count++;
				IBAN.getUpdateFlags().sendAnimation(1655);
				if(count >= 9) {
					b.stop();
				} else {
					for (int i = 0; i < 2 + Misc.random(2); i++) {
						Position p = MinigameAreas.randomPosition(SPELL_AREA);
						player.getActionSender().sendStillGraphic(new Graphic(60, 0), p);
					}
				}
			}

			@Override
			public void stop() {
				player.getActionSender().resetCamera();
			}
		}, 1);
	}
	
	public static void sendIbanSaying(final Player player, String message) {
		IBAN.getUpdateFlags().setForceChatMessage("" + message);
		player.getActionSender().sendMessage("Iban: @blu@" + message, true);
	}
}
