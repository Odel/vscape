package com.rs2.model.content.quests;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.Following;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.dialogue.DialogueManager;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.net.ActionSender;

public class Biohazard implements Quest {

	public static final int questIndex = 7352;
	//Quest stages
	public static final int QUEST_STARTED = 1;
	public static final int TALKED_TO_JERICO = 2;
	public static final int DISTRACT_WATCHTOWER = 3;
	public static final int OVER_WALL = 4;
	public static final int STEW_POISONED = 5;
	public static final int QUEST_COMPLETE = 20;

	//Items
	public static final int ETHENEA = 415;
	public static final int LIQUID_HONEY = 416;
	public static final int SULFURIC_BROLINE = 417;
	public static final int PLAGUE_SAMPLE = 418;
	public static final int TOUCH_PAPER = 419;
	public static final int DISTILLATOR = 420;
	public static final int LATHAS_AMULET = 421;
	public static final int BIRD_FEED = 422;
	public static final int KEY = 423;
	public static final int PIGEON_CAGE_FULL = 424;
	public static final int PIGEON_CAGE_EMPTY = 425;
	public static final int PRIEST_GOWN_TOP = 426;
	public static final int PRIEST_GOWN_BOTTOM = 428;
	public static final int DOCTORS_GOWN = 430;

	//Positions
	public static final Position POSITION = new Position(0, 0, 0);

	//Interfaces
	public static final int INTERFACE = -1;

	//Npcs
	public static final int ELENA = 3209;
	public static final int KING_LATHAS = 364;
	public static final int JERICO = 366;
	public static final int CHEMIST = 367;
	public static final int GUARD = 368;
	public static final int NURSE_SARAH = 373;
	public static final int DA_VINCI = 336;
	public static final int CHANCY = 338;
	public static final int HOPS_DRINKING = 340;
	public static final int HOPS = 341;
	public static final int GUIDORS_WIFE = 342;
	public static final int GUIDOR = 343;
	public static final int GUARD_1 = 344;
	public static final int GUARD_2 = 345;
	public static final int GUARD_3 = 346;
	public static final int KILRON = 349;
	public static final int OMART = 350;

	//Objects
	public static final int ELENAS_DOOR = 2054;
	public static final int JERICO_CUPBOARD = 2056;
	public static final int JERICO_CUPBOARD_OPEN = 2057;
	public static final int WATCHTOWER = 2067;
	public static final int ROPE_LADDER = 2065;
	public static final int MOURNER_DOOR = 2036;

	private int reward[][] = { //{itemId, count},
	};

	private int expReward[][] = { //{skillId, exp},
		{Skill.THIEVING, 1250}
	};

	private static final int questPointReward = 3; //Change

	public int getQuestID() { //Change
		return 40;
	}

	public String getQuestName() { //Change
		return "Biohazard";
	}

	public String getQuestSaveName() { //Change
		return "biohazard";
	}

	public boolean canDoQuest(final Player player) {
		return false;
	}

	public void getReward(final Player player) {
		for (int[] rewards : reward) {
			player.getInventory().addItemOrDrop(new Item(rewards[0], rewards[1]));
		}
		for (final int[] expRewards : expReward) {
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer b) {
					b.stop();
				}

				@Override
				public void stop() {
					player.getSkill().addExp(expRewards[0], (expRewards[1]));
				}
			}, 4);
		}
		player.addQuestPoints(questPointReward);
		player.getActionSender().QPEdit(player.getQuestPoints());
	}

	public void completeQuest(Player player) {
		getReward(player);
		player.getActionSender().sendInterface(12140);
		player.getActionSender().sendItemOnInterface(12145, 250, DISTILLATOR); //zoom, then itemId
		player.getActionSender().sendString("You have completed " + getQuestName() + "!", 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("3 Quest Points", 12150);
		player.getActionSender().sendString("2812.5 Thieving XP", 12151);
		player.getActionSender().sendString("", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), questIndex);
	}

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		int lastIndex = 0;
		switch(questStage) { 
			case QUEST_STARTED:
				lastIndex = 5;
				break;
			case TALKED_TO_JERICO:
				lastIndex = 8;
				break;
			case DISTRACT_WATCHTOWER:
				lastIndex = 11;
				break;
			case OVER_WALL:
				lastIndex = 14;
				break;
			case QUEST_COMPLETE:
				lastIndex = 26;
				break;
		}
		lastIndex++;
		
		ActionSender a = player.getActionSender();
		a.sendQuestLogString("Talk to Elena in East Ardougne to begin.", 1, this.getQuestID(), 0);
		a.sendQuestLogString("Elena told me to talk to her father's old friend", 3, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("Jerico in order to try and gain access to West", 4, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("Ardougne again.", 5, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("I talked to Jerico, he said to seek out his", 7, this.getQuestID(), TALKED_TO_JERICO);
		a.sendQuestLogString("assistant Omart on the south end of the city wall.", 8, this.getQuestID(), TALKED_TO_JERICO);
		a.sendQuestLogString("Omart can get me over the wall, but only with the", 10, this.getQuestID(), DISTRACT_WATCHTOWER);
		a.sendQuestLogString("mourners at the nearby watchtower distracted.", 11, this.getQuestID(), DISTRACT_WATCHTOWER);
		a.sendQuestLogString("I managed to get over the West Ardougne wall with", 13, this.getQuestID(), OVER_WALL);
		a.sendQuestLogString("Omart's help.", 14, this.getQuestID(), OVER_WALL);
		switch (questStage) {
			default:
				break;
			case 0:
				a.sendQuestLogString("Talk to @dre@Elena @bla@in @dre@East Ardougne @bla@to begin.", 1);
				a.sendQuestLogString("@dre@Requirements:", 3);
				a.sendQuestLogString((QuestHandler.questCompleted(player, 39) ? "@str@" : "@dbl@") + "Plague City", 4);
				break;
			case DISTRACT_WATCHTOWER:
				a.sendQuestLogString("I should figure this out for myself, or ask", lastIndex + 1);
				a.sendQuestLogString("Jerico for ideas on how to accomplish this.", lastIndex + 2);
				break;
			case OVER_WALL:
				a.sendQuestLogString("I should investigate and find where", lastIndex + 1);
				a.sendQuestLogString("the mourners took Elena's distillator. I imagine it's", lastIndex + 2);
				a.sendQuestLogString("in the locked building where they all gather.", lastIndex + 3);
				break;
			case QUEST_COMPLETE:
				a.sendQuestLogString("@red@" + "You have completed this quest!", lastIndex + 1);
				break;
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), questIndex);
	}

	public boolean questCompleted(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if (questStage >= QUEST_COMPLETE) {
			return true;
		}
		return false;
	}

	public void sendQuestTabStatus(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if ((questStage >= QUEST_STARTED) && (questStage < QUEST_COMPLETE)) {
			player.getActionSender().sendString("@yel@" + getQuestName(), questIndex);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), questIndex);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), questIndex);
		}
	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
		player.getActionSender().sendString(getQuestName(), 8144);
	}
	
	public static boolean releasePigeons(final Player player) {
		if (player.getQuestStage(40) != DISTRACT_WATCHTOWER) {
			player.getDialogue().sendStatement("The pigeons refuse to leave the cage.");
			return false;
		}
		int x = player.getPosition().getX(), y = player.getPosition().getY();
		if (((x >= 2559 && x <= 2563 && y == 3300) || (x == 2564 && y >= 3301 && y <= 3306) || player.getPosition().equals(new Position(2563, 3301, 0))) && player.getQuestVars().birdSeedThrown) {
			player.setStopPacket(true);
			player.getActionSender().sendMessage("The pigeons fly towards the watchtower.");
			int attackerX = player.getPosition().getX(), attackerY = player.getPosition().getY();
			Position[] positions = {new Position(2561, 3305, 0), new Position(2561, 3303, 0), new Position(2559, 3303, 0)};
			for (Position p : positions) {
				int victimX = p.getX(), victimY = p.getY();
				int offsetX = (attackerY - victimY) * -1;
				int offsetY = (attackerX - victimX) * -1;
				World.sendProjectile(player.getPosition(), offsetX, offsetY, 72, 10, 43, 100, 0, false);
			}
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer b) {
					b.stop();
				}

				@Override
				public void stop() {
					player.setStopPacket(false);
					for (Npc npc : player.getNpcs()) {
						if (npc != null && npc.getNpcId() == 348) {
							npc.getUpdateFlags().sendAnimation(npc.getDefinition().getBlockAnim());
						}
					}
					player.getActionSender().sendMessage("The mourners are frantically trying to scare the pigeons away.");
					player.getQuestVars().watchtowerDistracted = true;
				}
			}, 3);
			return true;
		} else {
			if (player.getQuestVars().birdSeedThrown) {
				player.getActionSender().sendMessage("You need to get closer to the watchtower to release the birds.");
			} else {
				player.getDialogue().sendStatement("The pigeons refuse to leave the cage.");
			}
			return false;
		}

	}
	
	public static void climbRopeLadder(final Player player) {
		player.getActionSender().removeInterfaces();
		Following.resetFollow(World.getNpcs()[World.getNpcIndex(KILRON)]);
		Following.resetFollow(World.getNpcs()[World.getNpcIndex(OMART)]);
		player.getActionSender().sendMessage("You climb up the rope ladder...");
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				b.stop();
			}

			@Override
			public void stop() {
				player.setStopPacket(false);
				player.getActionSender().sendMessage("...and drop down on the other side.");
				player.teleport(new Position(player.getPosition().getX() > 2558 ? 2556 : 2559, 3267, 0));
				ObjectHandler.getInstance().removeObject(2557, 3267, 0, 10);
				ObjectHandler.getInstance().removeObject(2558, 3267, 0, 10);
				new GameObject(ROPE_LADDER + 1, 2557, 3267, 0, 3, 10, ROPE_LADDER, 999999);
				new GameObject(ROPE_LADDER + 1, 2558, 3267, 0, 1, 10, ROPE_LADDER, 999999);
				ObjectHandler.getInstance().removeClip(2559, 3267, 0, 10, 0);
				ObjectHandler.getInstance().removeClip(2559, 3268, 0, 10, 0);
				ObjectHandler.getInstance().removeClip(2559, 3269, 0, 10, 0);
			}
		}, 3);
	}

	public boolean itemHandling(final Player player, int itemId) {
		switch (itemId) {
			case PIGEON_CAGE_FULL:
				if(releasePigeons(player)) {
					player.getInventory().replaceItemWithItem(new Item(PIGEON_CAGE_FULL), new Item(PIGEON_CAGE_EMPTY));
				}
				return true;
		}
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		return false;
	}

	public boolean doItemOnObject(final Player player, int object, int item) {
		switch(object) {
			case WATCHTOWER:
				if(item == BIRD_FEED && player.getQuestStage(this.getQuestID()) == DISTRACT_WATCHTOWER) {
					player.getActionSender().sendMessage("You steathily throw a handful of feed onto the watchtower.");
					player.getInventory().removeItem(new Item(BIRD_FEED));
					player.getQuestVars().birdSeedThrown = true;
					return true;
				}
			return false;
		}
		return false;
	}

	public boolean doItemOnNpc(Player player, int itemId, Npc npc) {
		return false;
	}

	public boolean doNpcClicking(Player player, Npc npc) {
		return false;
	}

	public boolean doObjectClicking(final Player player, final int object, final int x, final int y) {
		int pX = player.getPosition().getX();
		int pY = player.getPosition().getY();
		switch (object) {
			case MOURNER_DOOR:
				if (x == 2551 && y == 3320) {
					if (player.getQuestStage(40) < STEW_POISONED) {
						player.getDialogue().setLastNpcTalk(369);
						player.getDialogue().sendNpcChat("Sorry, no one is allowed in at this point in time.");
						player.getDialogue().endDialogue();
						
					} else {

					}
					return true;
				}
			return false;
			case ROPE_LADDER:
				if(player.getQuestStage(40) >= OVER_WALL) {
					climbRopeLadder(player);
					return true;
				}
				return false;
			case WATCHTOWER:
				if(player.getQuestVars().watchtowerDistracted) {
					player.getDialogue().sendStatement("The mourner guards look terribly distracted by pigeons.");
				} else {
					player.getDialogue().sendStatement("The mourner guards look ever vigilant.");
				}
				return true;
			case JERICO_CUPBOARD_OPEN:
				if(x == 2611 && y == 3326) {
					Dialogues.startDialogue(player, JERICO_CUPBOARD_OPEN + 10000);
					return true;
				}
			return true;
			case JERICO_CUPBOARD:
				if(x == 2611 && y == 3326) {
					player.getUpdateFlags().sendAnimation(TheGrandTree.PLACE_ANIM);
					ObjectHandler.getInstance().removeObject(x, y, 0, 10);
					new GameObject(JERICO_CUPBOARD_OPEN, x, y, 0, 1, 10, object, 999999);
					return true;
				}
			return false;
			case ELENAS_DOOR:
				player.getActionSender().walkThroughDoor(object, x, y, 0);
				player.getActionSender().walkTo(pX == x ? 0 : pX < x ? 1 : -1, pY < 3339 ? 1 : -1, true);
				player.reloadRegion();
				return true;
		}
		return false;
	}

	public boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
		switch (object) {
			case JERICO_CUPBOARD_OPEN:
				player.getUpdateFlags().sendAnimation(TheGrandTree.PLACE_ANIM);
				ObjectHandler.getInstance().removeObject(x, y, 0, 10);
				new GameObject(JERICO_CUPBOARD, x, y, 0, 1, 10, object, 999999);
				return true;
		}
		return false;
	}

	public void handleDeath(final Player player, final Npc died) {

	}

	public boolean sendDialogue(final Player player, final int id, int chatId, int optionId, int npcChatId) {
		DialogueManager d = player.getDialogue();
		switch (id) { //Npc ID
			case KILRON:
				switch (player.getQuestStage(this.getQuestID())) {
					case OVER_WALL:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Do you wish to climb back over?");
								return true;
							case 2:
								d.sendOption("Yes.", "Not right now.");
								return true;
							case 3:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if(optionId == 2)
									d.endDialogue();
								return true;
							case 4:
								player.getActionSender().removeInterfaces();
								player.getActionSender().sendMessage("Kilron calls to his associate.");
								World.getNpcs()[World.getNpcIndex(KILRON)].getUpdateFlags().sendFaceToDirection(new Position(2559, 3266, 0));
								player.setStopPacket(true);
								CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
									@Override
									public void execute(CycleEventContainer b) {
										b.stop();
									}

									@Override
									public void stop() {
										player.setStopPacket(false);
										Dialogues.sendDialogue(player, KILRON, 5, 0);
									}
								}, 5);
								return true;
							case 5:
								d.sendNpcChat("Omart!");
								World.getNpcs()[World.getNpcIndex(OMART)].setFollowingEntity(World.getNpcs()[World.getNpcIndex(KILRON)]);
								return true;
							case 6:
								player.getActionSender().removeInterfaces();
								player.getActionSender().sendMessage("He throws one end of the rope ladder over the wall.");
								ObjectHandler.getInstance().removeObject(2557, 3267, 0, 10);
								ObjectHandler.getInstance().removeObject(2558, 3267, 0, 10);
								new GameObject(ROPE_LADDER, 2557, 3267, 0, 3, 10, 2066, 999999);
								new GameObject(ROPE_LADDER, 2558, 3267, 0, 1, 10, 2066, 999999);
								ObjectHandler.getInstance().removeClip(2559, 3267, 0, 10, 0);
								ObjectHandler.getInstance().removeClip(2559, 3268, 0, 10, 0);
								ObjectHandler.getInstance().removeClip(2559, 3269, 0, 10, 0);
								player.setStopPacket(true);
								CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
									@Override
									public void execute(CycleEventContainer b) {
										b.stop();
									}

									@Override
									public void stop() {
										Biohazard.climbRopeLadder(player);
									}
								}, 5);
								return true;
						}
					return false;
				}
			case OMART:
				switch (player.getQuestStage(this.getQuestID())) {
					case OVER_WALL:
						switch(d.getChatId()) {
							case 1:
								d.sendNpcChat("Do you wish to climb back over?");
								return true;
							case 2:
								d.sendOption("Yes.", "Not right now.");
								return true;
							case 3:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if(optionId == 2)
									d.endDialogue();
								return true;
							case 4:
								player.getActionSender().removeInterfaces();
								player.getActionSender().sendMessage("Omart calls to his associate.");
								World.getNpcs()[World.getNpcIndex(OMART)].getUpdateFlags().sendFaceToDirection(new Position(2556, 3266, 0));
								player.setStopPacket(true);
								CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
									@Override
									public void execute(CycleEventContainer b) {
										b.stop();
									}

									@Override
									public void stop() {
										player.setStopPacket(false);
										Dialogues.sendDialogue(player, OMART, 5, 0);
									}
								}, 5);
								return true;
							case 5:
								d.sendNpcChat("Kilron!");
								World.getNpcs()[World.getNpcIndex(KILRON)].setFollowingEntity(World.getNpcs()[World.getNpcIndex(OMART)]);
								return true;
							case 6:
								player.getActionSender().removeInterfaces();
								player.getActionSender().sendMessage("He throws one end of the rope ladder over the wall.");
								ObjectHandler.getInstance().removeObject(2557, 3267, 0, 10);
								ObjectHandler.getInstance().removeObject(2558, 3267, 0, 10);
								new GameObject(ROPE_LADDER, 2557, 3267, 0, 3, 10, 2066, 999999);
								new GameObject(ROPE_LADDER, 2558, 3267, 0, 1, 10, 2066, 999999);
								ObjectHandler.getInstance().removeClip(2559, 3267, 0, 10, 0);
								ObjectHandler.getInstance().removeClip(2559, 3268, 0, 10, 0);
								ObjectHandler.getInstance().removeClip(2559, 3269, 0, 10, 0);
								player.setStopPacket(true);
								CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
									@Override
									public void execute(CycleEventContainer b) {
										b.stop();
									}

									@Override
									public void stop() {
										Biohazard.climbRopeLadder(player);
									}
								}, 5);
								return true;
							case 8:
								d.sendNpcChat("You must go now traveller.");
								return true;
							case 9:
								d.sendOption("Ok, lets do it.", "I'll be back soon.");
								return true;
							case 10:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if(optionId == 2) {
									d.endDialogue();
								}
								return true;
							case 11:
								d.endDialogue();
								climbRopeLadder(player);
								return true;
						}
					return false;
					case DISTRACT_WATCHTOWER:
						switch (d.getChatId()) {
							case 1:
								if(player.getQuestVars().watchtowerDistracted) {
									d.sendNpcChat("Well done, the guards are having real trouble with those", "birds. You must go now, it's your only choice.");
								} else {
									d.sendNpcChat("We still need to distract the guards in the", "watchtower.");
									d.setNextChatId(10);
								}
								return true;
							case 2:
								player.getActionSender().removeInterfaces();
								player.getActionSender().sendMessage("Omart calls to his associate.");
								World.getNpcs()[World.getNpcIndex(OMART)].getUpdateFlags().sendFaceToDirection(new Position(2556, 3266, 0));
								player.setStopPacket(true);
								CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
									@Override
									public void execute(CycleEventContainer b) {
										b.stop();
									}

									@Override
									public void stop() {
										player.setStopPacket(false);
										Dialogues.sendDialogue(player, OMART, 3, 0);
									}
								}, 5);
								return true;
							case 3:
								d.sendNpcChat("Kilron!");
								World.getNpcs()[World.getNpcIndex(KILRON)].setFollowingEntity(World.getNpcs()[World.getNpcIndex(OMART)]);
								return true;
							case 4:
								player.getActionSender().removeInterfaces();
								player.getActionSender().sendMessage("He throws one end of the rope ladder over the wall.");
								player.setQuestStage(40, OVER_WALL);
								ObjectHandler.getInstance().removeObject(2557, 3267, 0, 10);
								ObjectHandler.getInstance().removeObject(2558, 3267, 0, 10);
								new GameObject(ROPE_LADDER, 2557, 3267, 0, 3, 10, 2066, 999999);
								new GameObject(ROPE_LADDER, 2558, 3267, 0, 1, 10, 2066, 999999);
								ObjectHandler.getInstance().removeClip(2559, 3267, 0, 10, 0);
								ObjectHandler.getInstance().removeClip(2559, 3268, 0, 10, 0);
								ObjectHandler.getInstance().removeClip(2559, 3269, 0, 10, 0);
								player.setStopPacket(true);
								CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
									@Override
									public void execute(CycleEventContainer b) {
										b.stop();
									}

									@Override
									public void stop() {
										player.setStopPacket(false);
										World.getNpcs()[World.getNpcIndex(KILRON)].setFollowingEntity(null);
										Dialogues.sendDialogue(player, OMART, 8, 0);
									}
								}, 5);
								return true;
							case 10:
								d.sendPlayerChat("How?");
								return true;
							case 11:
								d.sendNpcChat("Try asking Jerico, if he's not too busy with his pigeons.", "I'll be waiting here for you.");
								d.endDialogue();
								return true;
						}
					return false;
					case TALKED_TO_JERICO:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Omart, Jerico said you might be able to help me.");
								return true;
							case 2:
								d.sendNpcChat("He informed me of your problem traveller. I would be", "glad to help, I have a rope ladder and my associate,", "Kilron, is waiting on the other side.");
								return true;
							case 3:
								d.sendPlayerChat("Good stuff.");
								return true;
							case 4:
								d.sendNpcChat("Unfortunately, we can't risk it with the watch tower so", "close. So first we need to distract the guards in the", "tower.");
								return true;
							case 5:
								d.sendPlayerChat("How?");
								return true;
							case 6:
								d.sendNpcChat("Try asking Jerico, if he's not too busy with his pigeons.", "I'll be waiting here for you.");
								d.endDialogue();
								player.setQuestStage(this.getQuestID(), DISTRACT_WATCHTOWER);
								return true;
						}
					return false;
				}
			return false;
			case JERICO_CUPBOARD_OPEN + 10000:
				switch(d.getChatId()) {
					case 1:
						d.sendGiveItemNpc("The cupboard is full of birdfeed.", new Item(BIRD_FEED));
						player.getInventory().addItem(new Item(BIRD_FEED));
						return true;
					case 2:
						d.sendPlayerChat("Mmm, birdfeed! Now what could I do with that?");
						d.endDialogue();
						return true;
				}
			return false;
			case JERICO:
				switch (player.getQuestStage(this.getQuestID())) {
					case DISTRACT_WATCHTOWER:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Jerico, I need help distracted the mourners", "on the watchtower so that Omart can throw the", "rope ladder over the wall without getting caught.");
								return true;
							case 2:
								d.sendNpcChat("Hmm. Tricky, tricky...", "Let me think for a moment.");
								return true;
							case 3:
								d.sendStatement("Jerico stares off into the distance.");
								return true;
							case 4:
								d.sendStatement("Jerico starts to count on his fingers, mumbling", "softly to himself.");
								return true;
							case 5:
								d.sendNpcChat("Ah! I've got it. It's rather simple,", "really. Grab a pigeon cage from around the rear", "of my house. Then go into my cupboard inside and", "grab a pack of bird feed. Once you have");
								return true;
							case 6:
								d.sendNpcChat("both of these, head back to the watchtower.", "Once there, throw the feed onto the tower itself. Do", "it carefully! You don't want the mourners to notice. After", "you throw the feed, release the pigeons near");
								return true;
							case 7:
								d.sendNpcChat("the tower. Knowing my birds, they'll go crazy for", "the feed, and swarm the tower. The guards will be", "too busy fending them off to notice anything but", "how majestic my birds are!", LAUGHING);
								return true;
							case 8:
								d.sendPlayerChat("Bird feed, pigeons... got it. Thanks Jerico.");
								d.endDialogue();
								return true;
						}
					return false;
					case QUEST_STARTED:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello Jerico.");
								return true;
							case 2:
								d.sendNpcChat("Hello, I've been expecting you. Elena tells me you need", "to cross the wall.");
								return true;
							case 3:
								d.sendPlayerChat("That's right.");
								return true;
							case 4:
								d.sendNpcChat("My messenger pigeons help me communicate with", "friends over the wall.");
								return true;
							case 5:
								d.sendNpcChat("I have arranged for two friends to aid you with a rope", "ladder. Omart is waiting for you at the south end of the", "wall.");
								return true;
							case 6:
								d.sendNpcChat("But be careful, if the mourners catch you the", "punishment will be severe.");
								return true;
							case 7:
								d.sendPlayerChat("Thanks Jerico.");
								d.endDialogue();
								player.setQuestStage(this.getQuestID(), TALKED_TO_JERICO);
								return true;
						}
					return false;
				}
			return false;
			case ELENA:
				switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stagec
					case QUEST_STARTED:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Have you found Jerico yet?");
								return true;
							case 2:
								d.sendOption("Where can I find him at again?", "No, not yet.");
								return true;
							case 3:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if(optionId == 2)
									d.endDialogue();
								return true;
							case 4:
								d.sendNpcChat("My father's friend is in communication with", "West Ardougne. He might be able to help us, he lives", "next to the chapel.");
								d.endDialogue();
								return true;
						}
					return false;
					case 0:
						switch (d.getChatId()) {
							case 1:
								if(!QuestHandler.questCompleted(player, 39)) {
									return false;
								} else {
									d.sendPlayerChat("Good day to you, Elena.");
									return true;
								}
							case 2:
								d.sendNpcChat("You too, thanks for freeing me.", HAPPY);
								return true;
							case 3:
								d.sendNpcChat("It's just a shame the mourners confiscated my", "equipment.", SAD);
								return true;
							case 4:
								d.sendPlayerChat("What did they take?");
								return true;
							case 5:
								d.sendNpcChat("My distillator, I can't test any plague samples without it.", "They're holding it in the mourner quarters", "in West Ardougne.", SAD);
								return true;
							case 6:
								d.sendNpcChat("I must somehow retrieve that distillator if I am to find", "a cure for this awful affliction.");
								return true;
							case 7:
								d.sendOption("I'll try to retrieve it for you.", "Well, good luck.");
								return true;
							case 8:
								d.sendPlayerChat(d.tempStrings[optionId - 1], CONTENT);
								if(optionId == 2)
									d.endDialogue();
								return true;
							case 9:
								d.sendNpcChat("I was hoping you would say that. Unfortunately they", "discovered the tunnel and filled it in. We need another", "way over the wall.");
								return true;
							case 10:
								d.sendPlayerChat("Any ideas?");
								return true;
							case 11:
								d.sendNpcChat("My father's friend Jerico is in communication with", "West Ardougne. He might be able to help us, he lives", "next to the chapel.");
								return true;
							case 12:
								d.sendPlayerChat("I'll see what I can do.");
								d.endDialogue();
								QuestHandler.startQuest(player, this.getQuestID());
								return true;
						}
					return false;
					case QUEST_COMPLETE:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Thank you again!", Dialogues.HAPPY);
								d.endDialogue();
								return true;
						}
						return false;
				}
				return false;
		}
		return false;
	}

}
