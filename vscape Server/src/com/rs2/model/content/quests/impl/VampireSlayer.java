package com.rs2.model.content.quests;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_2;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.objects.GameObject;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class VampireSlayer implements Quest {

	public static final int QUEST_STARTED = 1;
	public static final int SLAY_VAMPIRE = 2;
	public static final int QUEST_COMPLETE = 3;

	public static final int HAMMER = 2347;
	public static final int GARLIC = 1550;
	public static final int BEER = 1917;
	public static final int STAKE = 1549;
	public static final int STEAK_MEAT = 2142;

	public static final int STAIRS_UP = 2617;
	public static final int STAIRS_DOWN = 2616;
	public static final int COFFIN = 2614;

	public static final int MORGAN = 755;
	public static final int DR_HARLOW = 756;
	public static final int COUNT_DRAYNOR = 757;
	public static final int WYDIN = 557;

	public static final Position MORGAN_POS = new Position(3099, 3269, 0);
	public static final Position HARLOW_POS = new Position(3223, 3395, 0);
	public static final Position DOWN_TO_VAMPIRE = new Position(3077, 9771, 0);
	public static final Position UP_FROM_VAMPIRE = new Position(3116, 3356, 0);

	public int dialogueStage = 0;
	private int reward[][] = {};
	private int expReward[][] = {
		{Skill.ATTACK, 4825}
	};

	private static final int questPointReward = 3;

	public int getQuestID() {
		return 21;
	}

	public String getQuestName() {
		return "Vampire Slayer";
	}

	public String getQuestSaveName() {
		return "vampire-slayer";
	}

	public boolean canDoQuest(Player player) {
		return true;
	}

	public void getReward(Player player) {
		for (int[] rewards : reward) {
			player.getInventory().addItem(new Item(rewards[0], rewards[1]));
		}
		for (int[] expRewards : expReward) {
			player.getSkill().addExp(expRewards[0], (expRewards[1]));
		}
		player.addQuestPoints(questPointReward);
		player.getActionSender().QPEdit(player.getQuestPoints());
	}

	public void completeQuest(Player player) {
		getReward(player);
		player.getActionSender().sendInterface(12140);
		player.getActionSender().sendItemOnInterface(995, 200, 12142);
		player.getActionSender().sendString("You have completed: " + getQuestName(), 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("3 Quest Points", 12150);
		player.getActionSender().sendString("10856 Attack XP", 12151); //manually did the 2.25x calculation - don't touch
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), 7347);
	}

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if (questStage == QUEST_STARTED) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Morgan in Draynor Village to start this quest.", 8147);

			player.getActionSender().sendString("Morgan told me about the vampire in Draynor Manor.", 8149);
			player.getActionSender().sendString("He said to find his old friend Dr. Harlow in the Blue Moon Inn.", 8150);
		} else if (questStage == SLAY_VAMPIRE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Morgan in Draynor Village to start this quest.", 8147);
			player.getActionSender().sendString("@str@" + "Morgan told me about the vampire in Draynor Manor.", 8149);
			player.getActionSender().sendString("@str@" + "He said to find his old friend Dr. Harlow in the Blue Moon Inn.", 8150);

			player.getActionSender().sendString("Dr. Harlow told me I need a stake and a hammer.", 8152);
			player.getActionSender().sendString("Driving the stake into the vampire's heart will kill it.", 8153);
			player.getActionSender().sendString("He also said garlic will weaken the vampire greatly.", 8154);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Morgan in Draynor Village to start this quest.", 8147);
			player.getActionSender().sendString("@str@" + "Morgan told me about the vampire in Draynor Manor.", 8149);
			player.getActionSender().sendString("@str@" + "He said to find his old friend Dr. Harlow in the Blue Moon Inn.", 8150);
			player.getActionSender().sendString("@str@" + "Dr. Harlow told me I need a stake and a hammer.", 8152);
			player.getActionSender().sendString("@str@" + "Driving the stake into the vampire's heart will kill it.", 8153);
			player.getActionSender().sendString("@str@" + "He also said garlic will weaken the vampire greatly.", 8154);
			player.getActionSender().sendString("@str@" + "I slayed the vampire and saved Draynor!", 8156);

			player.getActionSender().sendString("@red@" + "You have completed this quest!", 8158);
		} else {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("Talk to Morgan in Draynor Village to start this quest.", 8147);
			player.getActionSender().sendString("Requirements: Ability to kill a strong level 32 vampire.", 8148);
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 7347);
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
			player.getActionSender().sendString("@yel@" + getQuestName(), 7347);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 7347);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 7347);
		}

	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString("Talk to Morgan in Draynor Village to start this quest.", 8147);
		player.getActionSender().sendString("Requirements: Ability to kill a strong level 32 vampire.", 8148);
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public static void healCount(Player player) {
		Npc count = null;
		for (Npc npc : World.getNpcs()) {
			if (npc == null) {
				continue;
			}
			if (npc.getNpcId() == COUNT_DRAYNOR) {
				count = npc;
			}
		}
		if (count != null) {
			count.heal(100);
			CombatManager.attack(count, player);
			count.getUpdateFlags().sendForceMessage("I am eternal! Get that garlic out of my sight!");
			player.getActionSender().sendMessage("Without a stake and hammer, Count Draynor regenerates stronger!");
			for (Item item : player.getInventory().getItemContainer().getItems()) {
				if (item == null) {
					continue;
				}
				if (item.getId() == GARLIC) {
					player.getInventory().removeItem(item);
				}
			}
		}
	}

	public static boolean handleCountDeath(final Player player, final Npc npc) {
		if (npc.getNpcId() == COUNT_DRAYNOR) {
			if (!player.getInventory().playerHasItem(STAKE)) {
				healCount(player);
				return true;
			} else if (!player.getInventory().playerHasItem(HAMMER)) {
				healCount(player);
				return true;
			}
		} else {
			return false;
		}
		return false;
	}

	public boolean itemHandling(final Player player, int itemId) {
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		return false;
	}

	public boolean doItemOnObject(final Player player, int object, int item) {
		return false;
	}

	public boolean doObjectClicking(final Player player, int object, int x, int y) {
		switch (object) {
			case STAIRS_DOWN:
				if (player.getQuestStage(21) == 2) {
					player.fadeTeleport(DOWN_TO_VAMPIRE);
					return true;
				} else if (player.getQuestStage(21) < 2) {
					player.getDialogue().sendPlayerChat("Ew. It looks creepy down there.", "I'm not going down there without a reason.", CONTENT);
					return true;
				} else if (player.getQuestStage(21) > 2) {
					player.getDialogue().sendPlayerChat("Once was enough for me with vampires.", CONTENT);
					return true;
				}
			case STAIRS_UP:
				player.teleport(UP_FROM_VAMPIRE);
				return true;
			case COFFIN:
				if (player.getQuestStage(21) == SLAY_VAMPIRE) {
					for (Npc npc : World.getNpcs()) {
						if (npc == null) {
							continue;
						}
						if (npc.getNpcId() == COUNT_DRAYNOR) {
							return false;
						}
					}
					if (Misc.random(50) == 1) {
						NpcLoader.spawnNpc(player, new Npc(COUNT_DRAYNOR), player.getPosition().clone(), true, "Ooga booga where da wite wimmen at");
					} else {
						NpcLoader.spawnNpc(player, new Npc(COUNT_DRAYNOR), player.getPosition().clone(), true, "You woke me from my slumber! Prepare to die!");
					}
					return true;
				} else if (player.getQuestStage(21) > SLAY_VAMPIRE) {
					player.getDialogue().sendPlayerChat("I already slayed this creepy vampire.", CONTENT);
					return true;
				} else {
					player.getDialogue().sendPlayerChat("I better not touch this...", CONTENT);
					return true;
				}

		}
		return false;
	}

	public boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
		switch (object) {

		}
		return false;
	}

	public void handleDeath(final Player player, final Npc died) {

	}

	public static boolean doMiscObjectClicking(final Player player, int object, int x, int y) {
		switch (object) {
			case 2612: //cupboard
				if (x == 3096) {
					new GameObject(2612, 3096, 3269, 1, 0, 10, 2613, 0);
					return true;
				}
				return true;
			case 2613: //open cupboard
				player.getActionSender().sendMessage("You search the cupboard...");
				player.getUpdateFlags().sendAnimation(832);
				player.setStopPacket(true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						player.getActionSender().sendMessage("You find a clove of garlic.");
						player.getInventory().addItem(new Item(GARLIC));
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
	}

	public boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
		switch (id) {
			case MORGAN:
				switch (player.getQuestStage(21)) {
					case 0: //start
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Please, please help us, bold adventurer!", SAD);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("What's the problem?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Our little village has been ravaged by an evil vampire!", "He lives in the basement of the manor to the north,", "we need someone to get rid of him once and for all!", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendOption("No, vampires are scary!", "Ok, I'm up for an adventure.");
								return true;
							case 5:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("No, vampires are scary!", CONTENT);
										player.getDialogue().endDialogue();
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("Ok, I'm up for an adventure.", CONTENT);
										return true;
								}
							case 6:
								player.getDialogue().sendNpcChat("I think first you should seek help. I have a friend", "who is a retired vampire hunter, his name is Dr. Harlow.", "He may be able to give you some tips.", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("He can normally be found in the Blue Moon Inn", "in Varrock, he's a bit of an old soak these days. Mention his old", "pal' Morgan, I'm sure he wouldn't want me killed.", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendPlayerChat("I'll look him up then, thanks.", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(21, 1);
								QuestHandler.getQuests()[21].startQuest(player);
								return true;
						}
						return false;
				}
				return false;
			case DR_HARLOW:
				switch (player.getQuestStage(21)) {
					case 1:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Buy me a dr-rink pleassh...", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendOption("No, you've had enough.", "Morgan needs your help!");
								return true;
							case 3:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("No, you've had enough.", ANGRY_1);
										player.getDialogue().endDialogue();
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("Morgan needs your help!", CONTENT);
										return true;
								}
							case 4:
								player.getDialogue().sendNpcChat("Morgan you shhay...? *Hic*", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("His village is being terrorized by a vampire!", "He told me to ask you about how to stop it.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("Buy me a beer... Then I'll teash you", "what you need to know...", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("But this is your friend Morgan we're talking about!", ANGRY_1);
								return true;
							case 8:
								player.getDialogue().sendOption("Give Dr. Harlow a beer.", "Refuse.");
								return true;
							case 9:
								switch (optionId) {
									case 1:
										if (player.getInventory().playerHasItem(BEER)) {
											player.getDialogue().sendGiveItemNpc("You hand Dr. Harlow a cold pint.", new Item(BEER));
											player.getInventory().removeItem(new Item(BEER));
											return true;
										} else {
											player.getDialogue().sendPlayerChat("I'm afraid I dont have a beer for you.", SAD);
											player.getDialogue().endDialogue();
											return true;
										}
									case 2:
										player.getDialogue().sendPlayerChat("No, I refuse. You've had far too many.", ANGRY_2);
										player.getDialogue().endDialogue();
										return true;
								}
							case 10:
								player.getDialogue().sendNpcChat("Cheersy matey!", HAPPY);
								return true;
							case 11:
								player.getDialogue().sendPlayerChat("So tell me how to kill vampires then.", CONTENT);
								return true;
							case 12:
								player.getDialogue().sendNpcChat("Yesh vampires, I was 'ery good at killing 'em once...", CONTENT);
								return true;
							case 13:
								player.getDialogue().sendStatement("Dr. Harlow appears to sober up slightly.");
								return true;
							case 14:
								player.getDialogue().sendNpcChat("Well, you're gonna need a stake, otherwise he'll just", "regenerate. Yes, you must have a stake to finish it off...", "I just happen to have one with me.", CONTENT);
								return true;
							case 15:
								player.getDialogue().sendGiveItemNpc("Dr. Harlow hands you a piece of meat?", new Item(STEAK_MEAT));
								player.getInventory().addItem(new Item(STEAK_MEAT));
								return true;
							case 16:
								player.getDialogue().sendPlayerChat("Erm, this is a piece of meat.", CONTENT);
								return true;
							case 17:
								player.getDialogue().sendStatement("Dr. Harlow begins to roar with drunken laughter.");
								return true;
							case 18:
								player.getDialogue().sendNpcChat("AH HA HA! Oh my...", "You're right my dear boy, silly me. Wrong 'stake'.", LAUGHING);
								return true;
							case 19:
								player.getDialogue().sendGiveItemNpc("Dr. Harlow hands you a wooden stake.", new Item(STAKE));
								player.getInventory().addItem(new Item(STAKE));
								return true;
							case 20:
								player.getDialogue().sendNpcChat("You'll need a hammer as well, to drive it in properly.", "Your everyday general store hammer will do. One last", "thing... It's wise to carry garlic with you, vampires", "are somewhat weakened near garlic.", CONTENT);
								return true;
							case 21:
								player.getDialogue().sendNpcChat("Morgan always liked garlic, you should try his house.", "But remember, a vampire is still a dangerous foe!", CONTENT);
								return true;
							case 22:
								player.getDialogue().sendPlayerChat("Thank you very much!", HAPPY);
								player.getDialogue().endDialogue();
								player.setQuestStage(21, 2);
								return true;
						}
						return false;
					case 2: //lost stake?
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (!player.getInventory().ownsItem(STAKE)) {
									player.getDialogue().sendPlayerChat("I lost my stake Dr. Harlow.", SAD);
									return true;
								} else {
									player.getDialogue().sendNpcChat("You're not the bartender! SshooO!", ANGRY_1);
									player.getDialogue().endDialogue();
									return true;
								}
							case 2:
								player.getDialogue().sendNpcChat("Well, luckily for yous, *hic*, I have another...", "right here!", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendGiveItemNpc("Dr. Harlow hands you a wooden stake.", new Item(STAKE));
								player.getDialogue().endDialogue();
								player.getInventory().addItem(new Item(STAKE));
								return true;
						}
						return false;
				}
				return false;
		}
		return false;
	}

	@Override
	public boolean doNpcClicking(Player player, Npc npc) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doItemOnNpc(Player player, int itemId, Npc npc) {
		// TODO Auto-generated method stub
		return false;
	}

}
