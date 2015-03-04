package com.rs2.model.content.quests;

import com.rs2.model.Position;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import static com.rs2.model.content.quests.ErnestTheChicken.PULL_LEVER_ANIM;
import com.rs2.model.content.skills.Menus;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.players.container.inventory.Inventory;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class FamilyCrest implements Quest {

	//Quest stages

	public static final int QUEST_STARTED = 1;
	public static final int CALEB_NEEDS_INGREDIENTS = 2;
	public static final int FIND_AVAN = 3;
	public static final int FIND_AVAN_2 = 4;
	public static final int AVAN_NEEDS_GOLD = 5;
	public static final int FIND_JOHNATHON = 6;
	public static final int JOHNATHON_CURED = 7;
	public static final int SLAY_CHRONOZON = 8;
	public static final int CREST_GET = 9;
	public static final int QUEST_COMPLETE = 10;
	//Items
	public static final int SWORDFISH = 373;
	public static final int BASS = 365;
	public static final int TUNA = 361;
	public static final int SALMON = 329;
	public static final int SHRIMP = 315;
	public static final int CREST_PART_1 = 779;
	public static final int CREST_PART_2 = 780;
	public static final int CREST_PART_3 = 781;
	public static final int CREST = 782;
	public static final int PERFECT_RING = 773;
	public static final int PERFECT_GOLD_ORE = 446;
	public static final int PERFECT_GOLD_BAR = 2365;
	public static final int PERFECT_NECKLACE = 774;
	public static final int COOKING_GAUNTLETS = 775;
	public static final int GOLDSMITH_GAUNTLETS = 776;
	public static final int CHAOS_GAUNTLETS = 777;
	public static final int STEEL_GAUNTLETS = 778;
	//Positions
	public static final Position UP_AT_LIGHTHOUSE = new Position(2510, 3644, 0);
	//Interfaces
	public static final int DOOR_INTERFACE = 10116;
	//Npcs
	public static final int GEM_TRADER = 540;
	public static final int AVAN = 663;
	public static final int DIMINTHEIS = 664;
	public static final int BOOT = 665;
	public static final int CALEB = 666;
	public static final int CHRONOZON = 667;
	public static final int JOHNATHON = 668;
	//Objects
	public static final int NORTH_LEVER = 2421;
	public static final int NORTH_ROOM_LEVER = 2425;
	public static final int NORTH_ROOM_DOOR = 2431;
	public static final int SOUTH_LEVER = 2423;
	public static final int SOUTH_ROOM_DOOR_2 = 2427;
	public static final int SOUTH_ROOM_DOOR_1 = 2429;
	public static final int GOLD_DOOR = 2430;
	//Buttons
	public static final int MAKE_1_NECKLACE = 34174;
	public static final int MAKE_5_NECKLACE = 34173;
	public static final int MAKE_10_NECKLACE = 34172;
	public static final int MAKE_1_RING = 34170;
	public static final int MAKE_5_RING = 34169;
	public static final int MAKE_10_RING = 34168;
	public int dialogueStage = 0;

	private int reward[][] = {
		{STEEL_GAUNTLETS, 1}
	};
	private int expReward[][] = {};

	private static final int questPointReward = 1;

	public int getQuestID() {
		return 28;
	}

	public String getQuestName() {
		return "Family Crest";
	}

	public String getQuestSaveName() {
		return "familycrest";
	}

	public boolean canDoQuest(Player player) {
		return true;
	}

	public void getReward(Player player) {
		for (int[] rewards : reward) {
			player.getInventory().addItemOrDrop(new Item(rewards[0], rewards[1]));
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
		player.getActionSender().sendString("You have completed " + getQuestName() + "!", 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("Steel Gauntlets", 12151);
		player.getActionSender().sendString("A skill imbue for the gauntlets", 12152);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), 7357);
	}

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		switch (questStage) {
			case QUEST_STARTED:
				player.getActionSender().sendString("@str@" + "Talk to Dimintheis, in south-east Varrock to begin.", 8147);

				player.getActionSender().sendString("I need to find Dimintheis' son Caleb. The last he heard of", 8149);
				player.getActionSender().sendString("him he was a great chef beyond White Wolf Mountain.", 8150);
				break;
			case CALEB_NEEDS_INGREDIENTS:
				player.getActionSender().sendString("@str@" + "Talk to Dimintheis, in south-east Varrock to begin.", 8147);
				player.getActionSender().sendString("@str@" + "I need to find Dimintheis' son Caleb. The last he heard of", 8149);
				player.getActionSender().sendString("@str@" + "him he was a great chef beyond White Wolf Mountain.", 8150);

				player.getActionSender().sendString("I found Caleb, and now need to get him cooked Swordfish,", 8152);
				player.getActionSender().sendString("Bass, Tuna, Salmon & Shrimp in return for his crest piece.", 8153);
				break;
			case FIND_AVAN:
				player.getActionSender().sendString("@str@" + "Talk to Dimintheis, in south-east Varrock to begin.", 8147);
				player.getActionSender().sendString("@str@" + "I need to find Dimintheis' son Caleb. The last he heard of", 8149);
				player.getActionSender().sendString("@str@" + "him he was a great chef beyond White Wolf Mountain.", 8150);
				player.getActionSender().sendString("@str@" + "I found Caleb, and now need to get him cooked Swordfish,", 8152);
				player.getActionSender().sendString("@str@" + "Bass, Tuna, Salmon & Shrimp in return for his crest piece.", 8153);

				player.getActionSender().sendString("I got Caleb's piece of the crest, I just need to find his", 8155);
				player.getActionSender().sendString("brother Avan next. He'll have another piece, naturally.", 8156);
				break;
			case FIND_AVAN_2:
				player.getActionSender().sendString("@str@" + "Talk to Dimintheis, in south-east Varrock to begin.", 8147);
				player.getActionSender().sendString("@str@" + "I need to find Dimintheis' son Caleb. The last he heard of", 8149);
				player.getActionSender().sendString("@str@" + "him he was a great chef beyond White Wolf Mountain.", 8150);
				player.getActionSender().sendString("@str@" + "I found Caleb, and now need to get him cooked Swordfish,", 8152);
				player.getActionSender().sendString("@str@" + "Bass, Tuna, Salmon & Shrimp in return for his crest piece.", 8153);
				player.getActionSender().sendString("@str@" + "I got Caleb's piece of the crest.", 8155);

				player.getActionSender().sendString("The gem trader in Al Kharid told me that he told Avan", 8157);
				player.getActionSender().sendString("to go look for 'perfect gold' amongst the scorpions.", 8158);
				break;
			case AVAN_NEEDS_GOLD:
				player.getActionSender().sendString("@str@" + "Talk to Dimintheis, in south-east Varrock to begin.", 8147);
				player.getActionSender().sendString("@str@" + "I need to find Dimintheis' son Caleb. The last he heard of", 8149);
				player.getActionSender().sendString("@str@" + "him he was a great chef beyond White Wolf Mountain.", 8150);
				player.getActionSender().sendString("@str@" + "I found Caleb, and now need to get him cooked Swordfish,", 8152);
				player.getActionSender().sendString("@str@" + "Bass, Tuna, Salmon & Shrimp in return for his crest piece.", 8153);
				player.getActionSender().sendString("@str@" + "I got Caleb's piece of the crest.", 8155);

				player.getActionSender().sendString("I found Avan. He says he needs 'perfect' gold to make a", 8157);
				player.getActionSender().sendString("ruby necklace and ring for a woman. He said he will give me", 8158);
				player.getActionSender().sendString("his shard of the family crest once I have done so.", 8159);
				player.getActionSender().sendString("He said a dwarf named Boot may know where this gold is.", 8160);
				player.getActionSender().sendString("Apparently Boot returned to his home, in the mountains.", 8161);
				break;
			case FIND_JOHNATHON:
				player.getActionSender().sendString("@str@" + "Talk to Dimintheis, in south-east Varrock to begin.", 8147);
				player.getActionSender().sendString("@str@" + "I need to find Dimintheis' son Caleb. The last he heard of", 8149);
				player.getActionSender().sendString("@str@" + "him he was a great chef beyond White Wolf Mountain.", 8150);
				player.getActionSender().sendString("@str@" + "I found Caleb, and now need to get him cooked Swordfish,", 8152);
				player.getActionSender().sendString("@str@" + "Bass, Tuna, Salmon & Shrimp in return for his crest piece.", 8153);
				player.getActionSender().sendString("@str@" + "I got Caleb's piece of the crest.", 8155);
				player.getActionSender().sendString("@str@" + "I found Avan. He said he needs 'perfect' gold to make a", 8157);
				player.getActionSender().sendString("@str@" + "ruby necklace and ring for a woman. I then found the gold", 8158);
				player.getActionSender().sendString("@str@" + "and made him his 'perfect' jewelry.", 8159);

				player.getActionSender().sendString("Avan said his brother Johnathon hangs out in a tavern", 8161);
				player.getActionSender().sendString("on the edge of the Wilderness. I should try to find him.", 8162);
				break;
			case JOHNATHON_CURED:
				player.getActionSender().sendString("@str@" + "Talk to Dimintheis, in south-east Varrock to begin.", 8147);
				player.getActionSender().sendString("@str@" + "I need to find Dimintheis' son Caleb. The last he heard of", 8149);
				player.getActionSender().sendString("@str@" + "him he was a great chef beyond White Wolf Mountain.", 8150);
				player.getActionSender().sendString("@str@" + "I found Caleb, and now need to get him cooked Swordfish,", 8152);
				player.getActionSender().sendString("@str@" + "Bass, Tuna, Salmon & Shrimp in return for his crest piece.", 8153);
				player.getActionSender().sendString("@str@" + "I got Caleb's piece of the crest.", 8155);
				player.getActionSender().sendString("@str@" + "I found Avan. He said he needs 'perfect' gold to make a", 8157);
				player.getActionSender().sendString("@str@" + "ruby necklace and ring for a woman. I then found the gold", 8158);
				player.getActionSender().sendString("@str@" + "and made him his 'perfect' jewelry.", 8159);

				player.getActionSender().sendString("I found Johnathon and cured him with an antipoison.", 8161);
				player.getActionSender().sendString("He must have some task for me to do for the crest part.", 8162);
				break;
			case SLAY_CHRONOZON:
				player.getActionSender().sendString("@str@" + "Talk to Dimintheis, in south-east Varrock to begin.", 8147);
				player.getActionSender().sendString("@str@" + "I need to find Dimintheis' son Caleb. The last he heard of", 8149);
				player.getActionSender().sendString("@str@" + "him he was a great chef beyond White Wolf Mountain.", 8150);
				player.getActionSender().sendString("@str@" + "I found Caleb, and now need to get him cooked Swordfish,", 8152);
				player.getActionSender().sendString("@str@" + "Bass, Tuna, Salmon & Shrimp in return for his crest piece.", 8153);
				player.getActionSender().sendString("@str@" + "I got Caleb's piece of the crest.", 8155);
				player.getActionSender().sendString("@str@" + "I found Avan. He said he needs 'perfect' gold to make a", 8157);
				player.getActionSender().sendString("@str@" + "ruby necklace and ring for a woman. I then found the gold", 8158);
				player.getActionSender().sendString("@str@" + "and made him his 'perfect' jewelry.", 8159);

				player.getActionSender().sendString("Johnathon has been trying to defeat Chronozon, a nasty", 8161);
				player.getActionSender().sendString("demon in the Wilderness part of Edgeville dungeon, by the", 8162);
				player.getActionSender().sendString("earth obelisk. He said I need to cast all four elemental", 8163);
				player.getActionSender().sendString("blast spells on the demon in order for it to die.", 8164);
				player.getActionSender().sendString("Chronozon took his crest piece, so I'll get it back for him.", 8165);
				break;
			case CREST_GET:
				player.getActionSender().sendString("@str@" + "Talk to Dimintheis, in south-east Varrock to begin.", 8147);
				player.getActionSender().sendString("@str@" + "I need to find Dimintheis' son Caleb. The last he heard of", 8149);
				player.getActionSender().sendString("@str@" + "him he was a great chef beyond White Wolf Mountain.", 8150);
				player.getActionSender().sendString("@str@" + "I found Caleb, and now need to get him cooked Swordfish,", 8152);
				player.getActionSender().sendString("@str@" + "Bass, Tuna, Salmon & Shrimp in return for his crest piece.", 8153);
				player.getActionSender().sendString("@str@" + "I got Caleb's piece of the crest.", 8155);
				player.getActionSender().sendString("@str@" + "I found Avan. He said he needs 'perfect' gold to make a", 8157);
				player.getActionSender().sendString("@str@" + "ruby necklace and ring for a woman. I then found the gold", 8158);
				player.getActionSender().sendString("@str@" + "and made him his 'perfect' jewelry.", 8159);
				player.getActionSender().sendString("@str@" + "Johnathon has been trying to defeat Chronozon, a nasty", 8161);
				player.getActionSender().sendString("@str@" + "demon in the Wilderness part of Edgeville dungeon.", 8162);

				player.getActionSender().sendString("I defeated Chronozon and got the final piece.", 8164);
				player.getActionSender().sendString("I should return to Dimintheis with the complete crest.", 8165);
				break;
			case QUEST_COMPLETE:
				player.getActionSender().sendString("@str@" + "Talk to Dimintheis, in south-east Varrock to begin.", 8147);
				player.getActionSender().sendString("@str@" + "I need to find Dimintheis' son Caleb. The last he heard of", 8149);
				player.getActionSender().sendString("@str@" + "him he was a great chef beyond White Wolf Mountain.", 8150);
				player.getActionSender().sendString("@str@" + "I found Caleb, and now need to get him cooked Swordfish,", 8152);
				player.getActionSender().sendString("@str@" + "Bass, Tuna, Salmon & Shrimp in return for his crest piece.", 8153);
				player.getActionSender().sendString("@str@" + "I got Caleb's piece of the crest.", 8155);
				player.getActionSender().sendString("@str@" + "I found Avan. He said he needs 'perfect' gold to make a", 8157);
				player.getActionSender().sendString("@str@" + "ruby necklace and ring for a woman. I then found the gold", 8158);
				player.getActionSender().sendString("@str@" + "and made him his 'perfect' jewelry.", 8159);
				player.getActionSender().sendString("@str@" + "Johnathon has been trying to defeat Chronozon, a nasty", 8161);
				player.getActionSender().sendString("@str@" + "demon in the Wilderness part of Edgeville dungeon.", 8162);
				player.getActionSender().sendString("@str@" + "I defeated Chronozon and got the final piece.", 8164);
				player.getActionSender().sendString("@str@" + "I should return to Dimintheis with the complete crest.", 8165);

				player.getActionSender().sendString("@red@" + "You have completed this quest!", 8167);
				break;
			default:
				player.getActionSender().sendString("Talk to @dre@Dimintheis @bla@in south-east @dre@Varrock @bla@ to begin.", 8147);
				player.getActionSender().sendString("@dre@Requirements:", 8148);
				if (player.getSkill().getLevel()[Skill.MINING] < 40) {
					player.getActionSender().sendString("-40 Mining.", 8149);
				} else {
					player.getActionSender().sendString("@str@-40 Mining.", 8149);
				}
				if (player.getSkill().getLevel()[Skill.SMITHING] < 40) {
					player.getActionSender().sendString("-40 Smithing.", 8150);
				} else {
					player.getActionSender().sendString("@str@-40 Smithing.", 8150);
				}
				if (player.getSkill().getLevel()[Skill.MAGIC] < 59) {
					player.getActionSender().sendString("-Ability to cast all the elemental blast spells. (Boostable)", 8151);
				} else {
					player.getActionSender().sendString("@str@-Ability to cast all the elemental blast spells.", 8151);
				}
				if (player.getSkill().getLevel()[Skill.CRAFTING] < 40) {
					player.getActionSender().sendString("-40 Crafting.", 8152);
				} else {
					player.getActionSender().sendString("@str@-40 Crafting.", 8152);
				}
				player.getActionSender().sendString("@dre@-I must be able to defeat a strong level 170 demon.", 8153);
				break;
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 7357);
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
			player.getActionSender().sendString("@yel@" + getQuestName(), 7357);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 7357);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 7357);
		}

	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
		player.getActionSender().sendString(getQuestName(), 8144);
	}

	public static void healChronozon(Player player, Npc npc) {
		if (npc.getNpcId() == CHRONOZON) {
			npc.heal(100);
			CombatManager.attack(npc, player);
			npc.getUpdateFlags().sendForceMessage("Ha Ha Ha Ha Ha Ha...");
			player.getQuestVars().setHitChronozonWind(false);
			player.getQuestVars().setHitChronozonWater(false);
			player.getQuestVars().setHitChronozonEarth(false);
			player.getQuestVars().setHitChronozonFire(false);
		}
	}

	public static boolean handleChronozonDeath(final Player player, final Npc npc) {
		if (npc.getNpcId() == CHRONOZON) {
			if (!player.getQuestVars().hasHitChronozonWind() || !player.getQuestVars().hasHitChronozonWater() || !player.getQuestVars().hasHitChronozonEarth() || !player.getQuestVars().hasHitChronozonFire()) {
				player.getActionSender().sendMessage("Chronozon regenerates!");
				healChronozon(player, npc);
				return true;
			} else {
				npc.getUpdateFlags().setForceChatMessage("WHAT?! NOOOOOO!");
				return false;
			}
		} else {
			return false;
		}
	}
	
	public void handleDeath(final Player player, final Npc died) {

	}

	public static boolean hasFish(final Player player) {
		Inventory i = player.getInventory();
		return i.playerHasItem(SWORDFISH) && i.playerHasItem(BASS) && i.playerHasItem(TUNA) && i.playerHasItem(SALMON) && i.playerHasItem(SHRIMP);
	}

	public static boolean handleDrops(final Player player, final Npc npc) {
		if (npc.getNpcId() == CHRONOZON && player.getQuestStage(28) == SLAY_CHRONOZON && !player.getInventory().ownsItem(CREST_PART_3)) {
			GroundItem drop = new GroundItem(new Item(CREST_PART_3), player, npc.getPosition().clone());
			GroundItemManager.getManager().dropItem(drop);
			return true;
		}
		return false;
	}

	public boolean doItemOnNpc(Player player, final int itemId, final Npc npc) {
		Item used = new Item(itemId);
		if (used.getDefinition().getName().toLowerCase().contains("antipoison") && !used.getDefinition().isNoted()) {
			if (player.getInventory().playerHasItem(used)) {
				player.getInventory().replaceItemWithItem(used, new Item(229));
			}
			player.setQuestStage(28, JOHNATHON_CURED);
			Dialogues.sendDialogue(player, JOHNATHON, 20, 0);
			return true;
		}
		return false;
	}

	public static boolean buttonHandling(final Player player, final int buttonId) {
		final int amountNecklace = buttonId == MAKE_1_NECKLACE ? 1 : buttonId == MAKE_5_NECKLACE ? 5 : 10;
		final int amountRing = buttonId == MAKE_1_RING ? 1 : buttonId == MAKE_5_RING ? 5 : 10;
		switch (buttonId) {
			case MAKE_1_RING:
			case MAKE_5_RING:
			case MAKE_10_RING:
				if (player.getStatedInterface().equals("perfect gold")) {
					if (player.getSkill().getLevel()[Skill.CRAFTING] >= 34) {
						if (player.getInventory().playerHasItem(PERFECT_GOLD_BAR) && player.getInventory().playerHasItem(1592) && player.getInventory().playerHasItem(1603)) {
							player.setStopPacket(true);
							player.getActionSender().removeInterfaces();
							CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
								@Override
								public void execute(CycleEventContainer b) {
									int count = amountRing;
									if (!player.getInventory().playerHasItem(PERFECT_GOLD_BAR) || !player.getInventory().playerHasItem(1603) || count == 0) {
										b.stop();
										return;
									} else {
										player.getInventory().replaceItemWithItem(new Item(PERFECT_GOLD_BAR), new Item(PERFECT_RING));
										player.getInventory().removeItem(new Item(1603));
										player.getUpdateFlags().sendAnimation(899);
										player.getActionSender().sendMessage("You craft a 'perfect' ruby ring.");
										player.getSkill().addExp(Skill.CRAFTING, 70);
										player.getActionSender().sendSound(469, 0, 0);
										if (count - 1 == 0) {
											b.stop();
										}
										count--;
									}
								}

								@Override
								public void stop() {
									player.setStopPacket(false);
									return;
								}
							}, 4);
							return true;
						} else {
							player.getActionSender().removeInterfaces();
							player.getActionSender().sendMessage("You do not have the materials required.");
							return true;
						}
					} else {
						player.getDialogue().sendStatement("You need a Crafting level of 34 to make this.");
						return true;
					}
				} else {
					return false;
				}
			case MAKE_1_NECKLACE:
			case MAKE_5_NECKLACE:
			case MAKE_10_NECKLACE:
				if (player.getStatedInterface().equals("perfect gold")) {
					if (player.getSkill().getLevel()[Skill.CRAFTING] >= 40) {
						if (player.getInventory().playerHasItem(PERFECT_GOLD_BAR) && player.getInventory().playerHasItem(1597) && player.getInventory().playerHasItem(1603) && player.getStatedInterface().equals("perfect gold")) {
							player.setStopPacket(true);
							player.getActionSender().removeInterfaces();
							CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
								@Override
								public void execute(CycleEventContainer b) {
									int count = amountNecklace;
									if (!player.getInventory().playerHasItem(PERFECT_GOLD_BAR) || !player.getInventory().playerHasItem(1603) || count == 0) {
										b.stop();
										return;
									} else {
										player.getInventory().replaceItemWithItem(new Item(PERFECT_GOLD_BAR), new Item(PERFECT_NECKLACE));
										player.getInventory().removeItem(new Item(1603));
										player.getActionSender().sendMessage("You craft a 'perfect' ruby necklace.");
										player.getSkill().addExp(Skill.CRAFTING, 75);
										player.getUpdateFlags().sendAnimation(899);
										player.getActionSender().sendSound(469, 0, 0);
										if (count - 1 == 0) {
											b.stop();
										}
										count--;
									}
								}

								@Override
								public void stop() {
									player.setStopPacket(false);
								}
							}, 4);
							return true;
						} else {
							player.getActionSender().removeInterfaces();
							player.getActionSender().sendMessage("You do not have the materials required.");
							return true;
						}
					} else {
						player.getDialogue().sendStatement("You need a Crafting level of 40 to make this.");
						return true;
					}
				} else {
					return false;
				}
		}
		return false;
	}

	public boolean itemHandling(final Player player, int itemId) {
		switch (itemId) {

		}
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		Inventory i = player.getInventory();
		switch (firstItem) {
			case CREST_PART_1:
				switch (secondItem) {
					case CREST_PART_2:
					case CREST_PART_3:
						if (i.playerHasItem(CREST_PART_1) && i.playerHasItem(CREST_PART_2) && (i.playerHasItem(CREST_PART_3))) {
							player.getActionSender().sendMessage("You put the Family Crest back together.");
							player.getInventory().replaceItemWithItem(new Item(CREST_PART_1), new Item(CREST));
							player.getInventory().removeItem(new Item(CREST_PART_2));
							player.getInventory().removeItem(new Item(CREST_PART_3));
							if (player.getQuestStage(28) == SLAY_CHRONOZON) {
								player.setQuestStage(28, CREST_GET);
							}
							return true;
						}
				}
				return false;
			case CREST_PART_2:
				switch (secondItem) {
					case CREST_PART_1:
					case CREST_PART_3:
						if (i.playerHasItem(CREST_PART_1) && i.playerHasItem(CREST_PART_2) && (i.playerHasItem(CREST_PART_3))) {
							player.getActionSender().sendMessage("You put the Family Crest back together.");
							player.getInventory().removeItem(new Item(CREST_PART_1));
							player.getInventory().replaceItemWithItem(new Item(CREST_PART_2), new Item(CREST));
							player.getInventory().removeItem(new Item(CREST_PART_3));
							if (player.getQuestStage(28) == SLAY_CHRONOZON) {
								player.setQuestStage(28, CREST_GET);
							}
							return true;
						}
				}
				return false;
			case CREST_PART_3:
				switch (secondItem) {
					case CREST_PART_2:
					case CREST_PART_1:
						if (i.playerHasItem(CREST_PART_1) && i.playerHasItem(CREST_PART_2) && (i.playerHasItem(CREST_PART_3))) {
							player.getActionSender().sendMessage("You put the Family Crest back together.");
							player.getInventory().removeItem(new Item(CREST_PART_1));
							player.getInventory().removeItem(new Item(CREST_PART_2));
							player.getInventory().replaceItemWithItem(new Item(CREST_PART_3), new Item(CREST));
							if (player.getQuestStage(28) == SLAY_CHRONOZON) {
								player.setQuestStage(28, CREST_GET);
							}
							return true;
						}
				}
				return false;

		}
		return false;
	}

	public boolean doItemOnObject(final Player player, int object, int item) {
		switch (object) {
			case 14921:
			case 9390:
			case 2781:
			case 2785:
			case 2966:
			case 3044:
			case 3294:
			case 4304:
			case 4305:
			case 6189:
			case 6190:
			case 11009:
			case 11010:
			case 11666:
			case 12100:
			case 12809: //furnaces
				if (item == PERFECT_GOLD_ORE) {
					if (player.getSkill().getLevel()[Skill.SMITHING] >= 40) {
						player.getActionSender().sendMessage("You carefully smelt the 'perfect' gold ore in the furnace.");
						player.getUpdateFlags().sendAnimation(899);
						player.getActionSender().sendSound(469, 0, 0);
						final int task = player.getTask();
						player.setSkilling(new CycleEvent() {
							@Override
							public void execute(CycleEventContainer b) {
								if (!player.checkTask(task) && !player.getInventory().playerHasItem(PERFECT_GOLD_ORE)) {
									b.stop();
								} else {
									if (player.getInventory().playerHasItem(PERFECT_GOLD_ORE)) {
										player.getInventory().removeItem(new Item(PERFECT_GOLD_ORE));
										player.getInventory().addItem(new Item(PERFECT_GOLD_BAR));
										player.getSkill().addExp(Skill.SMITHING, 23);
										player.getActionSender().sendMessage("You retrieve a 'perfect' gold bar from the furnace.");
										player.getUpdateFlags().sendAnimation(899);
										player.getActionSender().sendSound(469, 0, 0);
									}
								}
							}

							@Override
							public void stop() {
								player.getTask();
								player.resetAnimation();
							}
						});
						CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 4);
						return true;
					} else {
						player.getDialogue().sendStatement("You need level 40 Smithing to smelt this ore.");
						return true;
					}
				} else if (item == PERFECT_GOLD_BAR) {
					player.setStatedInterface("perfect gold");
					Menus.display2Item(player, PERFECT_RING, PERFECT_NECKLACE, "'Perfect' ruby ring", "'Perfect' ruby necklace");
					return true;
				} else {
					return false;
				}
		}
		return false;
	}

	public boolean doObjectClicking(final Player player, int object, int x, int y) {
		switch (object) {
			case GOLD_DOOR:
				if (player.getQuestVars().northPerfectGoldMineLever() && !player.getQuestVars().southPerfectGoldMineLever() && player.getQuestVars().northRoomPerfectGoldMineLever()) {
					player.getActionSender().walkTo(player.getPosition().getX() < 2728 ? 1 : -1, 0, true);
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					return true;
				} else {
					if (player.getPosition().getX() >= 2728) {
						player.getActionSender().walkTo(player.getPosition().getX() < 2728 ? 1 : -1, 0, true);
						player.getActionSender().walkThroughDoor(object, x, y, 0);
						return true;
					} else {
						player.getActionSender().sendMessage("This door is locked.");
						return true;
					}
				}
			case NORTH_ROOM_DOOR:
				if (!player.getQuestVars().northPerfectGoldMineLever() && player.getQuestVars().southPerfectGoldMineLever()) {
					player.getActionSender().walkTo(0, player.getPosition().getY() < 9711 ? 1 : -1, true);
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					return true;
				} else {
					if (player.getPosition().getY() >= 9711) {
						player.getActionSender().walkTo(0, player.getPosition().getY() < 9711 ? 1 : -1, true);
						player.getActionSender().walkThroughDoor(object, x, y, 0);
						return true;
					} else {
						player.getActionSender().sendMessage("This door is locked.");
						return true;
					}
				}
			case SOUTH_ROOM_DOOR_1:
			case SOUTH_ROOM_DOOR_2:
				if (player.getQuestVars().northPerfectGoldMineLever()) {
					player.getActionSender().walkTo(0, player.getPosition().getY() < 9672 ? 1 : -1, true);
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					return true;
				} else {
					if (player.getPosition().getY() <= 9671) {
						player.getActionSender().walkTo(0, player.getPosition().getY() < 9672 ? 1 : -1, true);
						player.getActionSender().walkThroughDoor(object, x, y, 0);
						return true;
					} else {
						player.getActionSender().sendMessage("This door is locked.");
						return true;
					}
				}
			case NORTH_LEVER:
				player.getActionSender().sendMessage("You pull the lever.");
				player.getUpdateFlags().sendAnimation(PULL_LEVER_ANIM);
				player.getQuestVars().setNorthPerfectGoldMineLever(!player.getQuestVars().northPerfectGoldMineLever());
				return true;
			case NORTH_ROOM_LEVER:
				player.getActionSender().sendMessage("You pull the lever.");
				player.getUpdateFlags().sendAnimation(PULL_LEVER_ANIM);
				player.getQuestVars().setNorthRoomPerfectGoldMineLever(!player.getQuestVars().northRoomPerfectGoldMineLever());
				return true;
			case SOUTH_LEVER:
				if (player.getQuestVars().northPerfectGoldMineLever() && player.getQuestVars().northRoomPerfectGoldMineLever() && player.getQuestVars().southPerfectGoldMineLever()) {
					player.getActionSender().sendMessage("You pull the lever. You hear something click into place.");
				} else {
					player.getActionSender().sendMessage("You pull the lever.");
				}
				player.getUpdateFlags().sendAnimation(PULL_LEVER_ANIM);
				player.getQuestVars().setSouthPerfectGoldMineLever(!player.getQuestVars().southPerfectGoldMineLever());
				return true;
		}
		return false;
	}

	public boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
		switch (object) {

		}
		return false;
	}

	public boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
		switch (id) {
			case JOHNATHON:
				switch (player.getQuestStage(28)) {
					case QUEST_COMPLETE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendStatement("Johnathon casts an unknown spell...");
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Err...Johnathon?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Oh! It's you! How can I help you adventurer?", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendOption("I want you to enchant my gauntlets.", "You can't");
								return true;
							case 5:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("I want you to enchant my gauntlets.", CONTENT);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("You can't, I'm sorry.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
								}
							case 6:
								player.getDialogue().sendNpcChat("Ah, a powerful choice! My enchantment will greatly", "increase the amount of damage you do with bolt spells!", HAPPY);
								return true;
							case 7:
								if (player.getQuestVars().usedFreeGauntletsCharge()) {
									player.getDialogue().sendNpcChat("It'll cost you 25,000 coins for me to do this", "enchantment however, It's not particularly easy!", CONTENT);
									player.getDialogue().setNextChatId(15);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Since you've done my father and our family such,", "a great honor, I'll enchant them for free this time.", CONTENT);
									return true;
								}
							case 8:
								player.getDialogue().sendPlayerChat("Great, thank you Johnathon.", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("Alright, here we go....", CONTENT);
								return true;
							case 10:
								if (player.getInventory().playerHasItem(STEEL_GAUNTLETS)) {
									player.getDialogue().sendGiveItemNpc("After some motions, Johnathon hands you your gauntlets.", new Item(CHAOS_GAUNTLETS));
									player.getDialogue().endDialogue();
									player.getInventory().replaceItemWithItem(new Item(STEEL_GAUNTLETS), new Item(CHAOS_GAUNTLETS));
									player.getQuestVars().setHasUsedFreeGauntletsCharge(true);
									return true;
								} else {
									player.getDialogue().sendNpcChat("You don't have the gauntlets with you!", "Go get them and come back!", Dialogues.ANNOYED);
									player.getDialogue().endDialogue();
									return true;
								}
							case 15:
								player.getDialogue().sendNpcChat("Is this alright?", CONTENT);
								return true;
							case 16:
								player.getDialogue().sendOption("Yes. (25,000 gold)", "No.");
								return true;
							case 17:
								switch (optionId) {
									case 1:
										if ((player.getInventory().playerHasItem(STEEL_GAUNTLETS) || player.getInventory().playerHasItem(COOKING_GAUNTLETS) || player.getInventory().playerHasItem(GOLDSMITH_GAUNTLETS)) && player.getInventory().playerHasItem(995, 25000) && !player.getInventory().playerHasItem(CHAOS_GAUNTLETS)) {
											player.getDialogue().sendGiveItemNpc("After some motions, Johnathon hands you your pair of gauntlets.", new Item(GOLDSMITH_GAUNTLETS));
											player.getDialogue().endDialogue();
											if (player.getInventory().playerHasItem(STEEL_GAUNTLETS)) {
												player.getInventory().replaceItemWithItem(new Item(STEEL_GAUNTLETS), new Item(CHAOS_GAUNTLETS));
											} else if (player.getInventory().playerHasItem(COOKING_GAUNTLETS)) {
												player.getInventory().replaceItemWithItem(new Item(COOKING_GAUNTLETS), new Item(CHAOS_GAUNTLETS));
											} else {
												player.getInventory().replaceItemWithItem(new Item(GOLDSMITH_GAUNTLETS), new Item(CHAOS_GAUNTLETS));
											}
											player.getInventory().removeItem(new Item(995, 25000));
											return true;
										} else if (!player.getInventory().playerHasItem(995, 25000)) {
											player.getDialogue().sendNpcChat("You don't have the money with you!", "Go get the coin and come back!", Dialogues.ANNOYED);
											player.getDialogue().endDialogue();
											return true;
										} else if (player.getInventory().playerHasItem(CHAOS_GAUNTLETS)) {
											player.getDialogue().sendNpcChat("You already have my enchantment, silly.", CONTENT);
											player.getDialogue().endDialogue();
											return true;
										} else {
											player.getDialogue().sendNpcChat("You don't have the gauntlets with you!", "Go get them and come back!", Dialogues.ANNOYED);
											player.getDialogue().endDialogue();
											return true;
										}
									case 2:
										player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
								}
						}
						return false;
					case CREST_GET:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Thank you again for ridding the world", "of that evil adventurer.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case SLAY_CHRONOZON:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Any luck killing Chronozon?", CONTENT);
								return true;
							case 2:
								if (player.getInventory().playerHasItem(CREST_PART_3)) {
									player.getDialogue().sendPlayerChat("Piece of cake. I have your crest piece right here.", CONTENT);
									player.getDialogue().setNextChatId(5);
									return true;
								} else {
									player.getDialogue().sendPlayerChat("Not yet, I'm afraid. I'll have to keep trying.", CONTENT);
									return true;
								}
							case 3:
								player.getDialogue().sendNpcChat("That's the spirit!", HAPPY);
								player.getDialogue().endDialogue();
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Ahh, finally the world is rid of his evil.", "Thank you so much adventurer. Keep the crest part, you", "deserve it, and my father deserves it.", HAPPY);
								player.getDialogue().endDialogue();
								player.setQuestStage(28, CREST_GET);
								return true;
						}
						return false;
					case JOHNATHON_CURED:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Ooooh... thank you... Wow! I'm feeling a lot better now!", "That potion really seems to have done the trick!", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("How can I reward you?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("I am here to retrieve your fragment of the Fitzharmon", "family crest.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("You have? Unfortunately I don't have it any more... in", "my attempts to slay the fiendish Chronozon, the blood", "demon, I lost a lot of equipment in", SAD);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("our last battle when he bested me and forced me away", "from his den. He probably still has it now.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("So this Chronozon is hard to defeat?", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("Well...you will have to be a skilled Mage to defeat him,", "and my powers are not good enough yet. You will need", "to hit him once with each of the four elemental blast spells", "in order for him to be defeated.", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendPlayerChat("So, how did you end up getting poisoned?", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("Those accursed poison spiders that surround the", "entrance to Chronozon's lair... I must have taken a nip", "from one of them as I attempted to make my escape.", SAD);
								return true;
							case 10:
								player.getDialogue().sendPlayerChat("Where is Chronozon's lair?", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendNpcChat("The fiend has made his lair in the Wilderness by", "the Obelisk of Earth, in Edgeville's dungeon.", CONTENT);
								return true;
							case 12:
								player.getDialogue().sendPlayerChat("Alright, I'll work on defeating him.", CONTENT);
								return true;
							case 13:
								player.getDialogue().sendNpcChat("Thank you adventurer, the best of luck to you.", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(28, SLAY_CHRONOZON);
								return true;
							case 20:
								player.getDialogue().sendStatement("You use the potion on Johnathon.");
								player.getDialogue().setNextChatId(1);
								return true;
						}
						return false;
					case FIND_JOHNATHON:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Greetings. Would you happen to be Johnathon", "Fitzharmon?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("That... I am...", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("I am here to retrieve your fragment of the Fitzharmon", "family crest.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("The... poison... it is all... too much... My head...", "will not... stop spinning...", Dialogues.DISORIENTED_LEFT);
								return true;
							case 5:
								player.getDialogue().sendStatement("Sweat is pouring down Johnathon's face.");
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					default:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Urggh... leave me alone...", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
				}
			case BOOT:
				switch (player.getQuestStage(28)) {
					case AVAN_NEEDS_GOLD:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Hello tall person.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Hello. I'm in search of very high quality gold.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("High quality gold eh? Hmmm... Well, the very best", "quality gold that I know of, is just east of the large", "city Ardougne, underground somewhere.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("I don't believe it's exactly easy to get though...", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					default:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Hello tall person", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Hey there, little fella.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendStatement("Boot gets visibly mad and begins to mutter to himself.");
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
				}
			case AVAN:
				switch (player.getQuestStage(28)) {
					case QUEST_COMPLETE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Please go away, I'm very busy.", "Blasted scorpions...", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Hello again, Avan.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Oh! It's you! How can I help you adventurer?", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendOption("I want you to enchant my gauntlets.", "You can't");
								return true;
							case 5:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("I want you to enchant my gauntlets.", CONTENT);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("You can't, I'm sorry.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
								}
							case 6:
								player.getDialogue().sendNpcChat("Ah, a wise choice! My enchantment will greatly", "boost the experience rewarded for smelting gold!", HAPPY);
								return true;
							case 7:
								if (player.getQuestVars().usedFreeGauntletsCharge()) {
									player.getDialogue().sendNpcChat("It'll cost you 25,000 coins for me to do this", "enchantment however, It's not particularly easy!", CONTENT);
									player.getDialogue().setNextChatId(15);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Since you've done my father and our family such,", "a great honor, I'll enchant them for free this time.", CONTENT);
									return true;
								}
							case 8:
								player.getDialogue().sendPlayerChat("Great, thank you Avan.", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("Alright, here we go....", CONTENT);
								return true;
							case 10:
								if (player.getInventory().playerHasItem(STEEL_GAUNTLETS)) {
									player.getDialogue().sendGiveItemNpc("After some motions, Avan hands you your pair of gauntlets.", new Item(GOLDSMITH_GAUNTLETS));
									player.getDialogue().endDialogue();
									player.getInventory().replaceItemWithItem(new Item(STEEL_GAUNTLETS), new Item(GOLDSMITH_GAUNTLETS));
									player.getQuestVars().setHasUsedFreeGauntletsCharge(true);
									return true;
								} else {
									player.getDialogue().sendNpcChat("You don't have the gauntlets with you!", "Go get them and come back!", Dialogues.ANNOYED);
									player.getDialogue().endDialogue();
									return true;
								}
							case 15:
								player.getDialogue().sendNpcChat("Is this alright?", CONTENT);
								return true;
							case 16:
								player.getDialogue().sendOption("Yes. (25,000 gold)", "No.");
								return true;
							case 17:
								switch (optionId) {
									case 1:
										if ((player.getInventory().playerHasItem(STEEL_GAUNTLETS) || player.getInventory().playerHasItem(CHAOS_GAUNTLETS) || player.getInventory().playerHasItem(COOKING_GAUNTLETS)) && player.getInventory().playerHasItem(995, 25000) && !player.getInventory().playerHasItem(GOLDSMITH_GAUNTLETS)) {
											player.getDialogue().sendGiveItemNpc("After some motions, Avan hands you your pair of gauntlets.", new Item(GOLDSMITH_GAUNTLETS));
											player.getDialogue().endDialogue();
											if (player.getInventory().playerHasItem(STEEL_GAUNTLETS)) {
												player.getInventory().replaceItemWithItem(new Item(STEEL_GAUNTLETS), new Item(GOLDSMITH_GAUNTLETS));
											} else if (player.getInventory().playerHasItem(CHAOS_GAUNTLETS)) {
												player.getInventory().replaceItemWithItem(new Item(CHAOS_GAUNTLETS), new Item(GOLDSMITH_GAUNTLETS));
											} else {
												player.getInventory().replaceItemWithItem(new Item(COOKING_GAUNTLETS), new Item(GOLDSMITH_GAUNTLETS));
											}
											player.getInventory().removeItem(new Item(995, 25000));
											return true;
										} else if (!player.getInventory().playerHasItem(995, 25000)) {
											player.getDialogue().sendNpcChat("You don't have the money with you!", "Go get the coin and come back!", Dialogues.ANNOYED);
											player.getDialogue().endDialogue();
											return true;
										} else if (player.getInventory().playerHasItem(GOLDSMITH_GAUNTLETS)) {
											player.getDialogue().sendNpcChat("You already have my enchantment, silly.", CONTENT);
											player.getDialogue().endDialogue();
											return true;
										} else {
											player.getDialogue().sendNpcChat("You don't have the gauntlets with you!", "Go get them and come back!", Dialogues.ANNOYED);
											player.getDialogue().endDialogue();
											return true;
										}
									case 2:
										player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
								}
						}
						return false;
					case FIND_JOHNATHON:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("I suppose you want to find my other brother Johnathon,", "who is in possession of the last piece of the family's crest?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("That's correct.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Well, the last I heard of my brother Johnathon, he was", "studying the magical arts, and trying to hunt some", "demon or something out in the Wilderness.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Unsuprisingly, I do not believe he is doing a", "particularly good job of things, and spends most of his", "time recovering from his injuries in", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("some tavern or other near the edge of the Wilderness.", "You'll probably find him there.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("Thanks Avan.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case AVAN_NEEDS_GOLD:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("So, how are you doing getting me my perfect gold", "jewelry?", CONTENT);
								return true;
							case 2:
								if (player.getInventory().playerHasItem(PERFECT_NECKLACE) && player.getInventory().playerHasItem(PERFECT_RING)) {
									player.getDialogue().sendPlayerChat("I found the gold and crafted your ruby necklace", "and ring for you with it.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendPlayerChat("I'm afraid I'm still looking... Where did you", "say I might find the gold at again?", CONTENT);
									player.getDialogue().setNextChatId(15);
									return true;
								}
							case 3:
								player.getDialogue().sendNpcChat("These... these are exquisite! EXACTLY what I was", "searching for all of this time! Please, take my crest", "fragment!", HAPPY);
								return true;
							case 4:
								player.getDialogue().sendGiveItemNpc("You give Avan the 'perfect' jewelry...", "...and recieve Avan's crest shard in return.", new Item(PERFECT_RING), new Item(CREST_PART_2));
								player.getDialogue().setNextChatId(1);
								player.setQuestStage(28, FIND_JOHNATHON);
								player.getInventory().removeItem(new Item(PERFECT_RING));
								player.getInventory().replaceItemWithItem(new Item(PERFECT_NECKLACE), new Item(CREST_PART_2));
								return true;
							case 15:
								player.getDialogue().sendNpcChat("I thought I had found a solid lead on its whereabouts", "when I heard of a dwarf who is an expert on gold who", "goes by the name of 'Boot'.", CONTENT);
								return true;
							case 16:
								player.getDialogue().sendNpcChat("Unfortunately he has apparently returned to his home,", "somewhere in the mountains, and I have no idea how to", "find him.", SAD);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case FIND_AVAN_2:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Please go away, I'm very busy.", "Blasted scorpions...", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Are you Avan Fitzharmon?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("My name is indeed Avan Fitzharmon.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("You have a part of your family crest. I am on", "a quest to retrieve all the fragmented pieces and", "restore the crest.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Ha! I suppose one of my worthless brothers has sent", "you on this quest then?", ANGRY_1);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("No, it was your father who has asked me to do", "this for him.", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("My... my father wishes this? Then that is a different", "matter! I will let you have my crest shard, adventurer,", "but you must first do something for me.", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("There is a certain lady I am trying to impress. As a", "man of noble birth, I can not give her just any old", "trinket to show my devotion. What I intend to give her,", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("is a golden ring, embedded with the finest precious red", "stone available, and a necklace to match this ring. The", "problem however for me, is that", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("not just any old gold will be suitable. I seek only the", "purest, the most high quality of gold - what I seek, if", "you will, is perfect gold.", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendNpcChat("None of the gold around here is even remotely suitable", "in terms of quality. I have searched far and wide for", "the perfect gold I desire, but have had no success", CONTENT);
								return true;
							case 12:
								player.getDialogue().sendNpcChat("in finding it I am afraid. If you can find me my", "perfect gold, make a ring and necklace from it, and add", "rubies to them, I will", CONTENT);
								return true;
							case 13:
								player.getDialogue().sendNpcChat("gladly hand over my fragment of the family crest", "to you.", CONTENT);
								return true;
							case 14:
								player.getDialogue().sendPlayerChat("Can you give me any help on finding this 'perfect gold'?", CONTENT);
								return true;
							case 15:
								player.getDialogue().sendNpcChat("I thought I had found a solid lead on its whereabouts", "when I heard of a dwarf who is an expert on gold who", "goes by the name of 'Boot'.", CONTENT);
								return true;
							case 16:
								player.getDialogue().sendNpcChat("Unfortunately he has apparently returned to his home,", "somewhere in the mountains, and I have no idea how to", "find him.", SAD);
								return true;
							case 17:
								player.getDialogue().sendPlayerChat("Well, I'll see what I can do.", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(28, AVAN_NEEDS_GOLD);
								return true;
						}
						return false;
					default:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Please go away, I'm very busy.", "Blasted scorpions...", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendStatement("The man continues to mutter to himself about scorpions.");
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
				}
			case GEM_TRADER:
				switch (player.getQuestStage(28)) {
					case FIND_AVAN:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("I'm in search of a man named Avan Fitzharmon.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Fitzharmon eh? Hmmm.... If I'm not mistaken that's the", "family name of a member of the Varrockian nobility.", "You know, I HAVE seen someone of that", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("persuasion around here recently... Wearing a poncey", "yellow cape he was! Came in here all la-di-dah, high and", "mighty, asking for jewelry made", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("from 'perfect gold' - whatever that is - like 'normal'", "gold's just not good enough for little lord fancy pants", "there! I told him to head to the desert", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("'Cos I know there's gold out there, in them there sand", "dunes... and if it's not up to his lordship's high", "standards of 'gold perfection'...", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("Maybe we'll all get lucky and the scorpions will", "deal with him!", LAUGHING);
								player.getDialogue().endDialogue();
								player.setQuestStage(28, FIND_AVAN_2);
								return true;
						}
						return false;
				}
				return false;
			case CALEB:
				switch (player.getQuestStage(28)) {
					case QUEST_COMPLETE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Cooking and cleaning, and cooking and cleaning...", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Hello again, Caleb.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Oh! It's you! How can I help you adventurer?", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendOption("I want you to enchant my gauntlets.", "You can't");
								return true;
							case 5:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("I want you to enchant my gauntlets.", CONTENT);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("You can't, I'm sorry.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
								}
							case 6:
								player.getDialogue().sendNpcChat("Ah, a noble choice! My enchantment will decrease", "your chance to burn higher level fish while cooking!", HAPPY);
								return true;
							case 7:
								if (player.getQuestVars().usedFreeGauntletsCharge()) {
									player.getDialogue().sendNpcChat("It'll cost you 25,000 coins for me to do this", "enchantment however, It's not particularly easy!", CONTENT);
									player.getDialogue().setNextChatId(15);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Since you've done my father and our family such,", "a great honor, I'll enchant them for free this time.", CONTENT);
									return true;
								}
							case 8:
								player.getDialogue().sendPlayerChat("Great, thank you Caleb.", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("Alright, here we go....", CONTENT);
								return true;
							case 10:
								if (player.getInventory().playerHasItem(STEEL_GAUNTLETS)) {
									player.getDialogue().sendGiveItemNpc("After some motions, Caleb hands you your pair of gauntlets.", new Item(COOKING_GAUNTLETS));
									player.getDialogue().endDialogue();
									player.getInventory().replaceItemWithItem(new Item(STEEL_GAUNTLETS), new Item(COOKING_GAUNTLETS));
									player.getQuestVars().setHasUsedFreeGauntletsCharge(true);
									return true;
								} else {
									player.getDialogue().sendNpcChat("You don't have the gauntlets with you!", "Go get them and come back!", Dialogues.ANNOYED);
									player.getDialogue().endDialogue();
									return true;
								}
							case 15:
								player.getDialogue().sendNpcChat("Is this alright?", CONTENT);
								return true;
							case 16:
								player.getDialogue().sendOption("Yes. (25,000 gold)", "No.");
								return true;
							case 17:
								switch (optionId) {
									case 1:
										if ((player.getInventory().playerHasItem(STEEL_GAUNTLETS) || player.getInventory().playerHasItem(CHAOS_GAUNTLETS) || player.getInventory().playerHasItem(GOLDSMITH_GAUNTLETS)) && player.getInventory().playerHasItem(995, 25000) && !player.getInventory().playerHasItem(COOKING_GAUNTLETS)) {
											player.getDialogue().sendGiveItemNpc("After some motions, Caleb hands you your pair of gauntlets.", new Item(GOLDSMITH_GAUNTLETS));
											player.getDialogue().endDialogue();
											if (player.getInventory().playerHasItem(STEEL_GAUNTLETS)) {
												player.getInventory().replaceItemWithItem(new Item(STEEL_GAUNTLETS), new Item(COOKING_GAUNTLETS));
											} else if (player.getInventory().playerHasItem(CHAOS_GAUNTLETS)) {
												player.getInventory().replaceItemWithItem(new Item(CHAOS_GAUNTLETS), new Item(COOKING_GAUNTLETS));
											} else {
												player.getInventory().replaceItemWithItem(new Item(GOLDSMITH_GAUNTLETS), new Item(COOKING_GAUNTLETS));
											}
											player.getInventory().removeItem(new Item(995, 25000));
											return true;
										} else if (!player.getInventory().playerHasItem(995, 25000)) {
											player.getDialogue().sendNpcChat("You don't have the money with you!", "Go get the coin and come back!", Dialogues.ANNOYED);
											player.getDialogue().endDialogue();
											return true;
										} else if (player.getInventory().playerHasItem(COOKING_GAUNTLETS)) {
											player.getDialogue().sendNpcChat("You already have my enchantment, silly.", CONTENT);
											player.getDialogue().endDialogue();
											return true;
										} else {
											player.getDialogue().sendNpcChat("You don't have the gauntlets with you!", "Go get them and come back!", Dialogues.ANNOYED);
											player.getDialogue().endDialogue();
											return true;
										}
									case 2:
										player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
								}
						}
						return false;
					case FIND_AVAN:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("So, do you know where I could find any of your", "other brothers?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Well, we haven't really kept in touch... what with the", "dispute over the crest and all... I did hear from my", "brother Avan a while ago though...", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("He said he was on some kind of search for treasure, or", "gold, or something out in the desert. You might want to", "ask someone there who deals with those sort of shiny things.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Avan always did have expensive tastes however. You", "may find he is not prepared to hand over his crest", "piece to you as easily as I have.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case CALEB_NEEDS_INGREDIENTS:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("How is the fish collecting going?", CONTENT);
								return true;
							case 2:
								if (hasFish(player)) {
									player.getDialogue().sendPlayerChat("Got them all with me.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendPlayerChat("I'm afraid I haven't gotten all of them yet...", SAD);
									player.getDialogue().endDialogue();
									return true;
								}
							case 3:
								player.getDialogue().sendGiveItemNpc("You exchange the fish for Caleb's crest piece.", new Item(CREST_PART_1));
								player.getDialogue().endDialogue();
								player.getInventory().removeItem(new Item(SWORDFISH));
								player.getInventory().removeItem(new Item(BASS));
								player.getInventory().removeItem(new Item(TUNA));
								player.getInventory().removeItem(new Item(SALMON));
								player.getInventory().replaceItemWithItem(new Item(SHRIMP), new Item(CREST_PART_1));
								player.setQuestStage(28, FIND_AVAN);
								return true;

						}
						return false;
					case QUEST_STARTED:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Who are you? What are you after?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Are you Caleb Fitzharmon?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Why... yes I am, but I don't believe I know you... how", "did you know my name?", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("I have been sent by your father. He wishes the", "Fitzharmon Crest to be restored.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("I still love you.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("What?", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("Your father told me to tell you that,", "that he still loves you.", "Anyways, the crest piece?", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("Oh. How sweet, poor father...", "And well... hmmm.... yes... I erm, do have a piece of it", "anyway...", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendPlayerChat("Uh, what happened to the rest of it?", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("Well... my brothers and I had a slight disagreement", "about it... we all wanted to be heir to my fathers' lands,", "and we each ended up with a piece of the crest.", DISTRESSED);
								return true;
							case 11:
								player.getDialogue().sendNpcChat("None of us wanted to give up our rights to our", "brothers, so we didn't want to give up our pieces of the", "crest, but none of us wanted to face our father by", SAD);
								return true;
							case 12:
								player.getDialogue().sendNpcChat("returning to him with an incomplete crest... We each", "went our seperate ways many years past, none of us", "seeing our father or willing to give up our fragments.", CONTENT);
								return true;
							case 13:
								player.getDialogue().sendPlayerChat("So, can I have your bit?", CONTENT);
								return true;
							case 14:
								player.getDialogue().sendNpcChat("Well, I am the oldest son, so by the rules of chivalry, I", "am most entitled to be rightful bearer of the crest.", CONTENT);
								return true;
							case 15:
								player.getDialogue().sendPlayerChat("It's not really much use without the other fragments", "is it though?", CONTENT);
								return true;
							case 16:
								player.getDialogue().sendNpcChat("Well, that is true... perhaps it is time to put my pride", "aside... I'll tell you what: I'm struggling to complete this", "fish salad of mine,", CONTENT);
								return true;
							case 17:
								player.getDialogue().sendNpcChat("if you will assist me in my search for the", "ingredients, then I will let you take my piece as reward", "for your assistance.", CONTENT);
								return true;
							case 18:
								player.getDialogue().sendPlayerChat("What ingredients are you missing?", CONTENT);
								return true;
							case 19:
								player.getDialogue().sendNpcChat("I require the following cooked fish: Swordfish, Bass,", "Tuna, Salmon and Shrimp.", CONTENT);
								return true;
							case 20:
								player.getDialogue().sendPlayerChat("Okay, I will get those then.", CONTENT);
								return true;
							case 21:
								player.getDialogue().sendNpcChat("You will? It would help me a lot!", CONTENT);
								return true;
							case 22:
								player.getDialogue().sendPlayerChat("It's not as if I have much choice...", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(28, CALEB_NEEDS_INGREDIENTS);
								return true;

						}
						return false;
					default:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Who are you? What are you after?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Nothing, I'll be on my way.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;

				}
			case DIMINTHEIS:
				switch (player.getQuestStage(28)) {
					case CREST_GET:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("I have retrieved your crest.", CONTENT);
								return true;
							case 2:
								if (player.getInventory().playerHasItem(CREST)) {
									player.getDialogue().sendNpcChat("Adventurer... I can only thank you for your kindness,", "although the words are insufficient to express the", "gratitude I feel!", HAPPY);
									return true;
								} else {
									player.getDialogue().sendPlayerChat("But... apparently I don't have it on me.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								}
							case 3:
								player.getDialogue().sendNpcChat("You are truly a hero in every sense, and perhaps your", "efforts can begin to patch the wounds that have torn", "this family apart...", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("I know not how I can adequately reward you for your", "efforts... although...", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("I do have these mystical gauntlets, a family heirloom", "that through some power unknown to me, have always", "returned to the head of the family", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("whenever lost, or if the owner has died. I will pledge", "these to you, and if you should lose them return to me,", "and they will be here.", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("They can also be granted extra powers. Take them to", "one of my sons, they should be able to imbue them with", "a skill for you.", CONTENT);
								return true;
							case 8:
								player.getInventory().removeItem(new Item(CREST));
								QuestHandler.completeQuest(player, 28);
								player.getDialogue().dontCloseInterface();
								return true;
						}
						return false;
					case 0:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Hello. My name is Dimintheis, of the noble family", "Fitzharmon.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Hi, I am a bold adventurer.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("An adventurer hmmm? How lucky. I may have an", "adventure for you. I desperately need my family crest", "returned to me. It is of utmost importance.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("Why are you so desperate for it?", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Well, there is a long standing rule of chivalry amongst", "the Varrockian aristocracy, where each noble family is", "in", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("possession of a unique crest, which signifies the honor", "and lineage of the family. More than this however, it", "also represents the lawful rights of each family", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("to prove their ownership of their wealth and lands. If", "the family crest is lost, then the family's estate is handed", "over to the current monarch until the crest is restored.", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("This dates back to the times when there was much in-", "fighting amongst the noble families and their clans, and", "was introduced as a way of reducing the", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("bloodshed that was devastating the ranks of the ruling", "classes at that time. When you captured a rival family's", "clan, you also captured their lands and wealth.", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendPlayerChat("So, where is this crest?", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendNpcChat("Well, my three sons took it with them many years ago", "when they rode out to fight in the way against the", "undead necromancer and his army in", CONTENT);
								return true;
							case 12:
								player.getDialogue().sendNpcChat("the battle to save Varrock. For many years I had", "assumed them all dead, as I had heard no word from", "them, but recently I heard word that my son", CONTENT);
								return true;
							case 13:
								player.getDialogue().sendNpcChat("Caleb is alive and well, and trying to earn his fortune", "as a great chef in the lands beyond White Wolf", "Mountain, although I know not where the others are...", CONTENT);
								return true;
							case 14:
								player.getDialogue().sendOption("Ok, I will help you.", "Woah, sounds like too much of an adventure for me.");
								return true;
							case 15:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("Ok, I will help you.", CONTENT);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("Woah, man. I'm not trying to get tangled", "up in something political.", DISTRESSED);
										player.getDialogue().endDialogue();
										return true;
								}
							case 16:
								player.getDialogue().sendNpcChat("I thank you greatly adventurer! Also... if you find", "Caleb... please... let him know his father still loves him...", HAPPY);
								player.getDialogue().endDialogue();
								QuestHandler.startQuest(player, 28);
								return true;
						}
						return false;
					case QUEST_COMPLETE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (!player.getInventory().ownsItem(STEEL_GAUNTLETS) && !player.getInventory().ownsItem(CHAOS_GAUNTLETS) && !player.getInventory().ownsItem(GOLDSMITH_GAUNTLETS) && !player.getInventory().ownsItem(COOKING_GAUNTLETS)) {
									player.getDialogue().sendPlayerChat("I seem to have lost my gauntlets.", SAD);
									return true;
								} else {
									player.getDialogue().sendNpcChat("I will be eternally grateful to you adventurer. Thank", "you again for restoring honor to the Fitzharmon name.", HAPPY);
									player.getDialogue().endDialogue();
									return true;
								}
							case 2:
								player.getDialogue().sendNpcChat("Ah, yes, I know. They returned to me, just as I said.", "Here you are.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendGiveItemNpc("Dimintheis hands you your pair of Steel Gauntlets.", new Item(STEEL_GAUNTLETS));
								player.getDialogue().endDialogue();
								player.getInventory().addItemOrDrop(new Item(STEEL_GAUNTLETS));
								return true;
						}
						return false;
					case QUEST_STARTED:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("How goes thy quest adventurer?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("It's going alright. Where can I find Caleb", "again?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Caleb is trying to earn his fortune as a great chef", "in the lands beyond White Wolf Mountain.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("Thanks.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case FIND_AVAN:
					case FIND_AVAN_2:
					case AVAN_NEEDS_GOLD:
					case FIND_JOHNATHON:
					case JOHNATHON_CURED:
					case SLAY_CHRONOZON:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("How goes thy quest adventurer?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("It's going alright. I'm still working on getting that", "crest return to you whole.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Wonderful, you're so kind adventurer.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("Thanks.", CONTENT);
								player.getDialogue().endDialogue();
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

}
