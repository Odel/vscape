package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.NEAR_TEARS_2;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.players.Player;
import com.rs2.model.players.container.inventory.Inventory;
import com.rs2.model.players.item.Item;
import com.rs2.model.transport.Sailing;
import com.rs2.util.Misc;

public class DragonSlayer implements Quest {

	public static final int QUEST_STARTED = 1;
	public static final int BACK_TO_GUILDMASTER = 2;
	public static final int CRANDOR_PREP = 3;
	public static final int BOAT_BOUGHT = 4;
	public static final int BOAT_READY = 5;
	public static final int TO_CRANDOR = 6;
	public static final int ON_CRANDOR = 7;
	public static final int BACK_TO_OZIACH = 8;
	public static final int QUEST_COMPLETE = 9;

	public static final int UNFIRED_BOWL = 1791;
	public static final int WIZARD_MIND_BOMB = 1907;
	public static final int LOBSTER_POT = 301;
	public static final int SILK = 950;
	public static final int MAZE_KEY = 1542;
	public static final int RED_KEY = 1543;
	public static final int ORANGE_KEY = 1544;
	public static final int YELLOW_KEY = 1545;
	public static final int BLUE_KEY = 1546;
	public static final int MAGENTA_KEY = 1547;
	public static final int GREEN_KEY = 1548;

	public static MinigameAreas.Area BASEMENT = new MinigameAreas.Area(2921, 2941, 9639, 9661, 0);

	public int dialogueStage = 0;
	private int reward[][] = {};
	private int expReward[][] = {
		{Skill.STRENGTH, 18650},
		{Skill.DEFENCE, 18650}
	};

	private static final int questPointReward = 2;

	public int getQuestID() {
		return 15;
	}

	public String getQuestName() {
		return "Dragon Slayer";
	}

	public String getQuestSaveName() {
		return "dragon-slayer";
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
		player.getActionSender().sendString("You have completed: " + getQuestName(), 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("2 Quest Points,", 12150);
		player.getActionSender().sendString("Ability to wear Rune Platebody", 12151);
		player.getActionSender().sendString((int) (expReward[0][1] * Constants.EXP_RATE) + " Strength XP,", 12152);
		player.getActionSender().sendString((int) (expReward[0][1] * Constants.EXP_RATE) + " Defence XP,", 12153);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), 7383);
	}

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if (questStage == QUEST_STARTED) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Speak with the Guildmaster in the Champion's Guild.", 8147);

			player.getActionSender().sendString("The guildmaster said to speak with Oziach.", 8149);
			player.getActionSender().sendString("His house is near Edgeville.", 8150);
		} else if (questStage == BACK_TO_GUILDMASTER) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Speak with the Guildmaster in the Champion's Guild.", 8147);
			player.getActionSender().sendString("@str@" + "The guildmaster said to speak with Oziach.", 8149);
			player.getActionSender().sendString("@str@" + "His house is near Edgeville.", 8150);

			player.getActionSender().sendString("I need to kill the dragon Elvarg, beneath Crandor.", 8152);
			player.getActionSender().sendString("The guildmaster should have more information.", 8153);
		} else if (questStage == CRANDOR_PREP) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Speak with the Guildmaster in the Champion's Guild.", 8147);
			player.getActionSender().sendString("@str@" + "The guildmaster said to speak with Oziach.", 8149);
			player.getActionSender().sendString("@str@" + "His house is near Edgeville.", 8150);
			player.getActionSender().sendString("@str@" + "I need to kill the dragon Elvarg, beneath Crandor.", 8152);
			player.getActionSender().sendString("@str@" + "The guildmaster should have more information.", 8153);

			player.getActionSender().sendString("The guildmaster told me I need 3 core things:", 8155);
			if (player.getInventory().ownsItem(1540) || player.getEquipment().getId(Constants.SHIELD) == 1540) {
				player.getActionSender().sendString("@str@" + "-An anti-dragon fire shield.", 8156);
			} else {
				player.getActionSender().sendString("@dre@" + "-An anti-dragon fire shield.", 8156);
				player.getActionSender().sendString("The anti-dragon fire shield should be easy.", 8160);
			}
			if (player.getInventory().ownsItem(1538)) {
				player.getActionSender().sendString("@str@" + "-A map to Crandor.", 8158);
			} else {
				player.getActionSender().sendString("@dre@" + "-A map to Crandor.", 8158);
				player.getActionSender().sendString("The map has been split in 3 by the wizards:", 8165);
				if (player.getInventory().ownsItem(1535)) {
					player.getActionSender().sendString("@str@" + "-Melzar's maze contains Melzar's piece.", 8166);
				} else {
					player.getActionSender().sendString("@dre@" + "-Melzar's maze contains Melzar's piece.", 8166);
				}
				if (player.getInventory().ownsItem(1536)) {
					player.getActionSender().sendString("@str@" + "-The Oracle on Ice Mountain knows about Thalzar.", 8167);
				} else {
					player.getActionSender().sendString("@dre@" + "-The Oracle on Ice Mountain knows about Thalzar.", 8167);
				}
				if (player.getInventory().ownsItem(1537)) {
					player.getActionSender().sendString("@str@" + "-Wormbrain in Sarim's jail led the raid on Lozar's home.", 8168);
				} else {
					player.getActionSender().sendString("@dre@" + "-Wormbrain in Sarim's jail led the raid on Lozar's home.", 8168);
				}
			}
			player.getActionSender().sendString("@dre@" + "-A boat to sail to Crandor.", 8157);
			player.getActionSender().sendString("The guildmaster said to look around Port Sarim,", 8162);
			player.getActionSender().sendString("for a ship and a captain.", 8163);

		} else if (questStage == BOAT_BOUGHT) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Speak with the Guildmaster in the Champion's Guild.", 8147);
			player.getActionSender().sendString("@str@" + "The guildmaster said to speak with Oziach.", 8149);
			player.getActionSender().sendString("@str@" + "His house is near Edgeville.", 8150);
			player.getActionSender().sendString("@str@" + "I need to kill the dragon Elvarg, beneath Crandor.", 8152);
			player.getActionSender().sendString("@str@" + "The guildmaster should have more information.", 8153);

			player.getActionSender().sendString("The guildmaster told me I need 3 core things:", 8155);
			if (player.getInventory().ownsItem(1540) || player.getEquipment().getId(Constants.SHIELD) == 1540) {
				player.getActionSender().sendString("@str@" + "-An anti-dragon fire shield.", 8156);
			} else {
				player.getActionSender().sendString("@dre@" + "-An anti-dragon fire shield.", 8156);
				player.getActionSender().sendString("The anti-dragon fire shield should be easy.", 8160);
			}
			if (player.getInventory().ownsItem(1538)) {
				player.getActionSender().sendString("@str@" + "-A map to Crandor.", 8158);
			} else {
				player.getActionSender().sendString("@dre@" + "-A map to Crandor.", 8158);
				player.getActionSender().sendString("The map has been split in 3 by the wizards:", 8165);
				if (player.getInventory().ownsItem(1535)) {
					player.getActionSender().sendString("@str@" + "-Melzar's maze contains Melzar's piece.", 8166);
				} else {
					player.getActionSender().sendString("@dre@" + "-Melzar's maze contains Melzar's piece.", 8166);
				}
				if (player.getInventory().ownsItem(1536)) {
					player.getActionSender().sendString("@str@" + "-The Oracle on Ice Mountain knows about Thalzar.", 8167);
				} else {
					player.getActionSender().sendString("@dre@" + "-The Oracle on Ice Mountain knows about Thalzar.", 8167);
				}
				if (player.getInventory().ownsItem(1537)) {
					player.getActionSender().sendString("@str@" + "-Wormbrain in Sarim's jail led the raid on Lozar's home.", 8168);
				} else {
					player.getActionSender().sendString("@dre@" + "-Wormbrain in Sarim's jail led the raid on Lozar's home.", 8168);
				}
			}
			player.getActionSender().sendString("@dre@" + "-I found a boat, it just needs some repair.", 8157);
			if (player.getInventory().playerHasItem(1538) && (player.getInventory().ownsItem(1540) || player.getEquipment().getId(Constants.SHIELD) == 1540)) {
				player.getActionSender().sendString("I need to find a captain.", 8159);
			}
		} else if (questStage == BOAT_READY) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Speak with the Guildmaster in the Champion's Guild.", 8147);
			player.getActionSender().sendString("@str@" + "The guildmaster said to speak with Oziach.", 8149);
			player.getActionSender().sendString("@str@" + "His house is near Edgeville.", 8150);
			player.getActionSender().sendString("@str@" + "I need to kill the dragon Elvarg, beneath Crandor.", 8152);
			player.getActionSender().sendString("@str@" + "The guildmaster should have more information.", 8153);

			player.getActionSender().sendString("The guildmaster told me I need 3 core things:", 8155);
			if (player.getInventory().ownsItem(1540) || player.getEquipment().getItemContainer().get(Constants.SHIELD).getId() == 1540) {
				player.getActionSender().sendString("@str@" + "-An anti-dragon fire shield.", 8156);
			} else {
				player.getActionSender().sendString("@dre@" + "-An anti-dragon fire shield.", 8156);
				player.getActionSender().sendString("The anti-dragon fire shield should be easy.", 8160);
			}
			player.getActionSender().sendString("@str@" + "-A boat to sail to Crandor.", 8157);
			if (player.getInventory().ownsItem(1538)) {
				player.getActionSender().sendString("@str@" + "-A map to Crandor.", 8158);
			} else {
				player.getActionSender().sendString("@dre@" + "-A map to Crandor.", 8158);
				player.getActionSender().sendString("The map has been split in 3 by the wizards:", 8165);
				if (player.getInventory().ownsItem(1535)) {
					player.getActionSender().sendString("@str@" + "-Melzar's maze contains Melzar's piece.", 8166);
				} else {
					player.getActionSender().sendString("@dre@" + "-Melzar's maze contains Melzar's piece.", 8166);
				}
				if (player.getInventory().ownsItem(1536)) {
					player.getActionSender().sendString("@str@" + "-The Oracle on Ice Mountain knows about Thalzar.", 8167);
				} else {
					player.getActionSender().sendString("@dre@" + "-The Oracle on Ice Mountain knows about Thalzar.", 8167);
				}
				if (player.getInventory().ownsItem(1537)) {
					player.getActionSender().sendString("@str@" + "-Wormbrain in Sarim's jail led the raid on Lozar's home.", 8168);
				} else {
					player.getActionSender().sendString("@dre@" + "-Wormbrain in Sarim's jail led the raid on Lozar's home.", 8168);
				}
			}
			if (player.getInventory().playerHasItem(1538) && (player.getInventory().ownsItem(1540) || player.getEquipment().getId(Constants.SHIELD) == 1540)) {
				player.getActionSender().sendString("I need to find a captain.", 8159);
			}
		} else if (questStage == TO_CRANDOR) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Speak with the Guildmaster in the Champion's Guild.", 8147);
			player.getActionSender().sendString("@str@" + "The guildmaster said to speak with Oziach.", 8149);
			player.getActionSender().sendString("@str@" + "His house is near Edgeville.", 8150);
			player.getActionSender().sendString("@str@" + "I need to kill the dragon Elvarg, beneath Crandor.", 8152);
			player.getActionSender().sendString("@str@" + "The guildmaster should have more information.", 8153);
			player.getActionSender().sendString("@str@" + "The guildmaster told me I need 3 core things:", 8155);
			player.getActionSender().sendString("@str@" + "-An anti-dragon fire shield.", 8156);
			player.getActionSender().sendString("@str@" + "-A boat to sail to Crandor.", 8157);
			player.getActionSender().sendString("@str@" + "-A map to Crandor.", 8158);
			player.getActionSender().sendString("I have everything. I need to go to the ship.", 8160);
			player.getActionSender().sendString("There I can set sail with Ned.", 8161);
		} else if (questStage == ON_CRANDOR) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Speak with the Guildmaster in the Champion's Guild.", 8147);
			player.getActionSender().sendString("@str@" + "The guildmaster said to speak with Oziach.", 8149);
			player.getActionSender().sendString("@str@" + "His house is near Edgeville.", 8150);
			player.getActionSender().sendString("@str@" + "I need to kill the dragon Elvarg, beneath Crandor.", 8152);
			player.getActionSender().sendString("@str@" + "The guildmaster should have more information.", 8153);
			player.getActionSender().sendString("@str@" + "The guildmaster told me I need 3 core things:", 8155);
			player.getActionSender().sendString("@str@" + "-An anti-dragon fire shield.", 8156);
			player.getActionSender().sendString("@str@" + "-A boat to sail to Crandor.", 8157);
			player.getActionSender().sendString("@str@" + "-A map to Crandor.", 8158);
			player.getActionSender().sendString("@str@" + "I have everything. I need to go to the ship.", 8160);
			player.getActionSender().sendString("@str@" + "There I can set sail with Ned.", 8161);

			player.getActionSender().sendString("We barely made it to Crandor. Elvarg should be near.", 8163);
		} else if (questStage == BACK_TO_OZIACH) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Speak with the Guildmaster in the Champion's Guild.", 8147);
			player.getActionSender().sendString("@str@" + "The guildmaster said to speak with Oziach.", 8149);
			player.getActionSender().sendString("@str@" + "His house is near Edgeville.", 8150);
			player.getActionSender().sendString("@str@" + "I need to kill the dragon Elvarg, beneath Crandor.", 8152);
			player.getActionSender().sendString("@str@" + "The guildmaster should have more information.", 8153);
			player.getActionSender().sendString("@str@" + "The guildmaster told me I need 3 core things:", 8155);
			player.getActionSender().sendString("@str@" + "-An anti-dragon fire shield.", 8156);
			player.getActionSender().sendString("@str@" + "-A boat to sail to Crandor.", 8157);
			player.getActionSender().sendString("@str@" + "-A map to Crandor.", 8158);
			player.getActionSender().sendString("@str@" + "I have everything. I need to go to the ship.", 8160);
			player.getActionSender().sendString("@str@" + "There I can set sail with Ned.", 8161);
			player.getActionSender().sendString("@str@" + "We barely made it to Crandor. Elvarg should be near.", 8163);

			player.getActionSender().sendString("Elvarg has been slain, I have proved myself.", 8163);
			player.getActionSender().sendString("I should return to Oziach.", 8164);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Speak with the Guildmaster in the Champion's Guild.", 8147);
			player.getActionSender().sendString("@str@" + "The guildmaster said to speak with Oziach.", 8149);
			player.getActionSender().sendString("@str@" + "His house is near Edgeville.", 8150);
			player.getActionSender().sendString("@str@" + "I need to kill the dragon Elvarg, beneath Crandor.", 8152);
			player.getActionSender().sendString("@str@" + "The guildmaster should have more information.", 8153);
			player.getActionSender().sendString("@str@" + "The guildmaster told me I need 3 core things:", 8155);
			player.getActionSender().sendString("@str@" + "-An anti-dragon fire shield.", 8156);
			player.getActionSender().sendString("@str@" + "-A boat to sail to Crandor.", 8157);
			player.getActionSender().sendString("@str@" + "-A map to Crandor.", 8158);
			player.getActionSender().sendString("@str@" + "I have everything. I need to go to the ship.", 8160);
			player.getActionSender().sendString("@str@" + "There I can set sail with Ned.", 8161);
			player.getActionSender().sendString("@str@" + "We barely made it to Crandor. Elvarg should be near.", 8163);
			player.getActionSender().sendString("@str@" + "Elvarg has been slain, I have proved myself.", 8163);

			player.getActionSender().sendString("@red@" + "You have completed this quest!", 8165);
		} else {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("Speak with the @dre@Guildmaster in the @dre@Champion's Guild.", 8147);
			player.getActionSender().sendString("@dre@Requirements:", 8148);
			if (player.getQuestPoints() >= 32) {
				player.getActionSender().sendString("@str@" + "-32 Quest Points.", 8149);
			} else {
				player.getActionSender().sendString("@dbl@" + "-32 Quest Points.", 8149);
			}
			player.getActionSender().sendString("@dbl@" + "-Ability to defeat a level 83 dragon.", 8150);
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 7383);
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
			player.getActionSender().sendString("@yel@" + getQuestName(), 7383);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 7383);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 7383);
		}

	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString("Speak with the @dre@Guildmaster in the @dre@Champion's Guild.", 8147);
		player.getActionSender().sendString("@dre@Requirements:", 8148);
		if (player.getQuestPoints() >= 32) {
			player.getActionSender().sendString("@str@" + "-32 Quest Points.", 8149);
		} else {
			player.getActionSender().sendString("@dbl@" + "-32 Quest Points.", 8149);
		}
		player.getActionSender().sendString("@dbl@" + "-Ability to defeat a level 83 dragon.", 8150);
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public static boolean nedSpawnedOnCrandor() {
		for (Npc npc : World.getNpcs()) {
			if (npc == null) {
				continue;
			}
			if (npc.getNpcId() == 918 && npc.getPosition().getY() == 3238) {
				return false;
			}
		}
		return true;
	}

	public static boolean thalzarDoorItems(Player player) {
		Inventory inventory = player.getInventory();
		if (inventory.playerHasItem(UNFIRED_BOWL) && inventory.playerHasItem(WIZARD_MIND_BOMB)
			&& inventory.playerHasItem(SILK) && inventory.playerHasItem(LOBSTER_POT)) {
			player.getInventory().removeItem(new Item(UNFIRED_BOWL));
			player.getInventory().removeItem(new Item(SILK));
			player.getInventory().removeItem(new Item(LOBSTER_POT));
			player.getInventory().removeItem(new Item(WIZARD_MIND_BOMB));
			return true;
		} else {
			return false;
		}
	}

	public static boolean mapPieces(Player player) {
		Inventory inventory = player.getInventory();
		if (inventory.playerHasItem(1535) && inventory.playerHasItem(1536) && inventory.playerHasItem(1537)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean itemHandling(Player player, int itemId) {
		if (itemId == 1535 || itemId == 1536 || itemId == 1537) {
			player.getDialogue().sendPlayerChat("Part of a map to Crandor...", "It doesn't make much sense ripped like this.", CONTENT);
			return true;
		} else if (itemId == 1538) {
			player.getDialogue().sendPlayerChat("A complete map to Crandor, now it makes sense.", CONTENT);
			return true;
		}
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		if (firstItem == 1535 && (secondItem == 1536 || secondItem == 1537)) {
			if (mapPieces(player)) {
				player.getDialogue().sendStatement("You re-attach the Crandor map.");
				player.getInventory().removeItem(new Item(1535));
				player.getInventory().removeItem(new Item(1536));
				player.getInventory().removeItem(new Item(1537));
				player.getInventory().addItem(new Item(1538));
			} else {
				player.getDialogue().sendPlayerChat("It seems I don't have all the pieces yet...", SAD);
				return true;
			}
		} else if (firstItem == 1536 && (secondItem == 1535 || secondItem == 1537)) {
			if (mapPieces(player)) {
				player.getDialogue().sendStatement("You re-attach the Crandor map.");
				player.getInventory().removeItem(new Item(1535));
				player.getInventory().removeItem(new Item(1536));
				player.getInventory().removeItem(new Item(1537));
				player.getInventory().addItem(new Item(1538));
			} else {
				player.getDialogue().sendPlayerChat("It seems I don't have all the pieces yet...", SAD);
				return true;
			}
		} else if (firstItem == 1537 && (secondItem == 1535 || secondItem == 1536)) {
			if (mapPieces(player)) {
				player.getDialogue().sendStatement("You re-attach the Crandor map.");
				player.getInventory().removeItem(new Item(1535));
				player.getInventory().removeItem(new Item(1536));
				player.getInventory().removeItem(new Item(1537));
				player.getInventory().addItem(new Item(1538));
			} else {
				player.getDialogue().sendPlayerChat("It seems I don't have all the pieces yet...", SAD);
				return true;
			}
		}
		return false;
	}

	public boolean doItemOnObject(final Player player, int object, int item) {
		return false;
	}

	public boolean doObjectClicking(Player player, int object, int x, int y) {
		switch (object) {
			case 2605: // down ladders
				Ladders.climbLadder(player, new Position(2932, 9640, 0));
				return true;
			case 2598: //melzar's yellow door
				if (player.getPosition().getY() < 3255) {
					if (player.getPosition().getY() > 3249) {
						if (player.getInventory().playerHasItem(YELLOW_KEY)) {
							player.getActionSender().walkThroughDoor(object, x, y, 2);
							player.getActionSender().walkTo(0, player.getPosition().getY() < 3250 ? 1 : -1, true);
							player.getActionSender().sendMessage("The key disintegrates as it unlocks the door.");
							player.getInventory().removeItem(new Item(YELLOW_KEY));
							return true;
						} else {
							player.getDialogue().sendStatement("This door is locked.");
							return true;
						}
					} else if (player.getPosition().getY() < 3250) {
						player.getActionSender().walkThroughDoor(object, x, y, 2);
						player.getActionSender().walkTo(0, player.getPosition().getY() < 3250 ? 1 : -1, true);
						return true;
					}
				} else {
					if (player.getPosition().getX() < 2936) {
						if (player.getInventory().playerHasItem(YELLOW_KEY)) {
							player.getActionSender().walkThroughDoor(object, x, y, 2);
							player.getActionSender().walkTo(player.getPosition().getX() < 2936 ? 1 : -1, 0, true);
							player.getActionSender().sendMessage("The key disintegrates as it unlocks the door.");
							player.getInventory().removeItem(new Item(YELLOW_KEY));
							return true;
						} else {
							player.getDialogue().sendStatement("This door is locked.");
							return true;
						}
					} else if (player.getPosition().getY() > 2935) {
						player.getActionSender().walkThroughDoor(object, x, y, 2);
						player.getActionSender().walkTo(player.getPosition().getX() < 2936 ? 1 : -1, 0, true);
						return true;
					}
				}
			case 2601: //melzar's green door
				if (player.getPosition().getY() < 9656) {
					if (player.getInventory().playerHasItem(GREEN_KEY)) {
						player.getActionSender().walkThroughDoor(object, x, y, 0);
						player.getActionSender().walkTo(0, player.getPosition().getY() < 9656 ? 1 : -1, true);
						player.getActionSender().sendMessage("The key disintegrates as it unlocks the door.");
						player.getInventory().removeItem(new Item(GREEN_KEY));
						return true;
					} else {
						player.getDialogue().sendStatement("This door is locked.");
						return true;
					}
				} else if (player.getPosition().getX() > 9655) {
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					player.getActionSender().walkTo(0, player.getPosition().getY() < 9656 ? 1 : -1, true);
					return true;
				}
			case 2600: //melzar's magenta door
				if (player.getPosition().getY() < 9652) {
					if (player.getInventory().playerHasItem(MAGENTA_KEY)) {
						player.getActionSender().walkThroughDoor(object, x, y, 0);
						player.getActionSender().walkTo(0, player.getPosition().getY() < 9652 ? 1 : -1, true);
						player.getActionSender().sendMessage("The key disintegrates as it unlocks the door.");
						player.getInventory().removeItem(new Item(MAGENTA_KEY));
						return true;
					} else {
						player.getDialogue().sendStatement("This door is locked.");
						return true;
					}
				} else if (player.getPosition().getY() > 9651) {
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					player.getActionSender().walkTo(0, player.getPosition().getY() < 9652 ? 1 : -1, true);
					return true;
				}
			case 2599: //melzar's blue door
				if (player.getPosition().getX() > 2930) {
					if (player.getInventory().playerHasItem(BLUE_KEY)) {
						player.getActionSender().walkThroughDoor(object, x, y, 0);
						player.getActionSender().walkTo(player.getPosition().getX() > 2930 ? -1 : 1, player.getPosition().getY() < 9643 ? 1 : 0, true);
						player.getActionSender().sendMessage("The key disintegrates as it unlocks the door.");
						player.getInventory().removeItem(new Item(BLUE_KEY));
						return true;
					} else {
						player.getDialogue().sendStatement("This door is locked.");
						return true;
					}
				} else if (player.getPosition().getX() < 2931) {
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					player.getActionSender().walkTo(player.getPosition().getX() > 2930 ? -1 : 1, player.getPosition().getY() < 9643 ? 1 : 0, true);
					return true;
				}
			case 2597: //melzar's orange door
				if (player.getPosition().getX() < 2931) {
					if (player.getInventory().playerHasItem(ORANGE_KEY)) {
						player.getActionSender().walkThroughDoor(object, x, y, 1);
						player.getActionSender().walkTo(player.getPosition().getX() < 2931 ? 1 : -1, 0, true);
						player.getActionSender().sendMessage("The key disintegrates as it unlocks the door.");
						player.getInventory().removeItem(new Item(ORANGE_KEY));
						return true;
					} else {
						player.getDialogue().sendStatement("This door is locked.");
						return true;
					}
				} else if (player.getPosition().getX() > 2929) {
					player.getActionSender().walkThroughDoor(object, x, y, 1);
					player.getActionSender().walkTo(player.getPosition().getX() < 2931 ? 1 : -1, 0, true);
					return true;
				}
			case 2596: //melzar's red door
				if (player.getPosition().getX() > 2925) {
					if (player.getInventory().playerHasItem(RED_KEY)) {
						player.getActionSender().walkThroughDoor(object, x, y, 0);
						player.getActionSender().walkTo(player.getPosition().getX() > 2925 ? -1 : 1, 0, true);
						player.getActionSender().sendMessage("The key disintegrates as it unlocks the door.");
						player.getInventory().removeItem(new Item(RED_KEY));
						return true;
					} else {
						player.getDialogue().sendStatement("This door is locked.");
						return true;
					}
				} else if (player.getPosition().getX() < 2925) {
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					player.getActionSender().walkTo(player.getPosition().getX() > 2925 ? -1 : 1, 0, true);
					return true;
				}
			case 2602: //melzar's maze exit door
				if (player.getPosition().getX() > 2937) {
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					player.getActionSender().walkTo(-1, 0, true);
					player.getActionSender().sendMessage("The door slams shut behind you.");
					return true;
				} else {
					player.getDialogue().sendStatement("This door is locked.");
					return true;
				}
			case 2595: //melzar's front door
				if (player.getInventory().playerHasItem(MAZE_KEY) || player.getQuestVars().getMelzarsDoorUnlock()) {
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					player.getActionSender().walkTo(player.getPosition().getX() > 2940 ? -1 : 1, 0, true);
					player.getQuestVars().setMelzarsDoorUnlock(true);
					player.getActionSender().sendMessage("The door slams shut behind you.");
					return true;
				} else {
					player.getDialogue().sendStatement("This door is locked.");
					return true;
				}
			case 2586: //thalzar's door
				if (player.getPosition().getX() < x) {
					if (thalzarDoorItems(player)) {
						player.getActionSender().walkThroughDoor(object, x, y, 0);
						player.getActionSender().walkTo(player.getPosition().getX() < 3051 ? 1 : -1, 0, true);
						player.getActionSender().sendMessage("You feel a magical force remove the items from your pack.");
						return true;
					}
				} else {
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					player.getActionSender().walkTo(player.getPosition().getX() < 3051 ? 1 : -1, 0, true);
					return true;
				}
				return false;
			case 2603: //melzar's chest
				if (player.getQuestStage(15) >= 3 && player.getQuestStage(15) < 6) {
					if (player.getInventory().ownsItem(1535)) {
						return true;
					} else {
						player.getInventory().addItem(new Item(1535));
						player.getDialogue().sendStatement("You find an old, tattered piece of map.");
						return true;
					}
				}
				return false;
			case 2587: //thalzar's chest
				if (player.getQuestStage(15) >= 3 && player.getQuestStage(15) < 6) {
					if (player.getInventory().ownsItem(1536)) {
						return true;
					} else {
						player.getInventory().addItem(new Item(1536));
						player.getDialogue().sendStatement("You find an old, tattered piece of map.");
						return true;
					}
				}
				return false;
			case 2607:
			case 2608:
				if (player.getQuestStage(15) == 7) {
					player.getActionSender().walkThroughDoubleDoor(2607, 2608, 2847, 9636, 2847, 9637, 0);
					player.getActionSender().walkTo(player.getPosition().getX() < 2847 ? 1 : -1, 0, true);
					return true;
				} else {
					if (player.getQuestStage(15) == 8 && player.getPosition().getX() > 2846) {
						player.getActionSender().walkThroughDoubleDoor(2607, 2608, 2847, 9636, 2847, 9637, 0);
						player.getActionSender().walkTo(-1, 0, true);
						return true;
					}
					player.getDialogue().sendStatement("You have no need to enter here anymore.");
					return true;
				}
			case 2606: // dragon slayer wall
				if (player.getQuestStage(15) >= 7) {
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					player.getActionSender().walkTo(0, player.getPosition().getY() < 9600 ? 1 : -1, true);
					return true;
				} else {
					player.getDialogue().sendStatement("This door is locked from the other side.", "You hear the roaring of a mighty dragon.");
					return true;
				}
			case 2609: // crandor tunnel
				player.fadeTeleport(new Position(2834, 9657, 0));
				return true;
			case 1805: // champions guild
				if (player.getQuestPoints() >= 32) {
					player.getActionSender().walkTo(0, player.getPosition().getY() > 3362 ? -1 : 1, true);
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					return true;
				} else {
					player.getDialogue().sendStatement("You need a qp total of 32 to access the Champion's Guild.");
					return true;
				}
			case 2593:
			case 2594:
				if (x == 3047) {
					if (player.getQuestStage(15) == 4 || player.getQuestStage(15) == 5) {
						player.teleport(new Position(3047, player.getPosition().getY() < 3206 ? 3207 : 3204, player.getPosition().getZ() == 0 ? 1 : 0));
						return true;
					} else if (player.getQuestStage(15) == 6 && player.getPosition().getY() < 3206) {
						player.teleport(new Position(3047, 3207, 1));
						Dialogues.startDialogue(player, 918);
						return true;
					} else if (player.getQuestStage(15) == 6 && player.getPosition().getY() > 3206) {
						player.teleport(new Position(3047, 3204, 0));
						return true;
					} else {
						player.getDialogue().sendStatement("Perhaps I shouldn't board this ship right now.");
						return true;
					}
				}
			case 2590:
			case 272: //ship ladder
				if (player.getPosition().getZ() == 1 && x == 3049) {
					if (player.getQuestStage(15) < 5) {
						player.getDialogue().sendPlayerChat("It looks awfully messy down there. I probably", "shouldn't go down until I talk to Jenkins.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					} else {
						Ladders.climbLadder(player, new Position(3048, 3208, 0));
						return true;
					}
				} else if (player.getPosition().getZ() == 0 && x == 3049) {
					Ladders.climbLadder(player, new Position(3048, 3208, 1));
					return true;
				}
				return true;
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

	public static void handleDrops(Player player, Npc npc) {
		if (npc.getNpcId() == 82 && MinigameAreas.isInArea(player.getPosition(), BASEMENT)) { //lesser demon
			if (!player.getInventory().playerHasItem(GREEN_KEY)) {
				GroundItem drop = new GroundItem(new Item(GREEN_KEY), player, new Position(npc.getPosition().getX(), npc.getPosition().getY()));
				GroundItemManager.getManager().dropItem(drop);
			}
		}
		if (npc.getNpcId() == 753) { //melzar
			if (!player.getInventory().playerHasItem(MAGENTA_KEY)) {
				GroundItem drop = new GroundItem(new Item(MAGENTA_KEY), player, new Position(npc.getPosition().getX(), npc.getPosition().getY()));
				GroundItemManager.getManager().dropItem(drop);
			}
		}
		if (npc.getNpcId() == 6099 || npc.getNpcId() == 6100) { //skeleton
			if (!player.getInventory().playerHasItem(BLUE_KEY) && Misc.random(1) == 1) {
				GroundItem drop = new GroundItem(new Item(BLUE_KEY), player, new Position(npc.getPosition().getX(), npc.getPosition().getY()));
				GroundItemManager.getManager().dropItem(drop);
			}
		}
		if (npc.getNpcId() == 6091) { //skeleton
			if (!player.getInventory().playerHasItem(YELLOW_KEY)) {
				GroundItem drop = new GroundItem(new Item(YELLOW_KEY), player, new Position(npc.getPosition().getX(), npc.getPosition().getY(), 2));
				GroundItemManager.getManager().dropItem(drop);
			}
		}
		if (npc.getNpcId() == 6094) { //ghost
			if (!player.getInventory().playerHasItem(ORANGE_KEY)) {
				GroundItem drop = new GroundItem(new Item(ORANGE_KEY), player, new Position(npc.getPosition().getX(), npc.getPosition().getY(), 1));
				GroundItemManager.getManager().dropItem(drop);
			}
		}
		if (npc.getNpcId() == 6088) { //zombie rat
			if (!player.getInventory().playerHasItem(RED_KEY)) {
				GroundItem drop = new GroundItem(new Item(RED_KEY), player, new Position(npc.getPosition().getX(), npc.getPosition().getY()));
				GroundItemManager.getManager().dropItem(drop);
			}
		}
		if (npc.getNpcId() == 745) { //wormbrain
			if ((player.getQuestStage(15) >= 3 && player.getQuestStage(15) < 6) && !player.getInventory().ownsItem(1537)) {
				GroundItem drop = new GroundItem(new Item(1537), player, new Position(npc.getPosition().getX(), npc.getPosition().getY()));
				GroundItemManager.getManager().dropItem(drop);
			}
		}
		if (npc.getNpcId() == 742) { //elvarg
			GroundItem drop = new GroundItem(new Item(1753), player, new Position(npc.getPosition().getX(), npc.getPosition().getY()));
			GroundItemManager.getManager().dropItem(drop);
			GroundItemManager.getManager().dropItem(drop);
			GroundItemManager.getManager().dropItem(drop);
			GroundItemManager.getManager().dropItem(drop);
			GroundItemManager.getManager().dropItem(drop);
			GroundItem drop2 = new GroundItem(new Item(536), player, new Position(npc.getPosition().getX(), npc.getPosition().getY()));
			GroundItemManager.getManager().dropItem(drop2);
		}
	}

	public static boolean isArmor(int id) {
		switch (id) {
			case 1135: //green dhide body
			case 1127: //rune plate
			case 2661: //saradomin plate
			case 2653: //zamorak plate
			case 2669: //guthix plate
			case 2615: //rune plate (g)
			case 2623: //rune plate(t)
			case 3481: //gilded plate
			case 7372: //green dhide body (t)
			case 7370: //green dhide body (g)
				return true;
		}
		return false;
	}

	public boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
		switch (id) {
			case 918: // fuck you ned
				switch (player.getQuestStage(15)) {
					case 3:
					case 4:
					case 5:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Hello there!", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("You used to be a sailor, right?", "Can you take me to Crandor?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Well, I was a sailor. I've not been able to get work at", "sea these days, though. They say I'm too old.", SAD);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Sorry, where was it you said you wanted to go?", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("To the island of Crandor.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("Crandor?", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("Hmm... It would be a chance to sail a ship once more.", "I'd sail anywhere if it was a chance to sail again.", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("Then again, no captain in his right mind would sail to", "that island.", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("Where's your ship boy?", CONTENT);
								return true;
							case 10:
								if (player.getQuestStage(15) == 3) {
									player.getDialogue().sendPlayerChat("I'm afraid I don't have one...", SAD);
									return true;
								} else if (player.getQuestStage(15) == 4) {
									player.getDialogue().sendPlayerChat("The Lady Lumbridge, in Port Sarim.", "She does need some repairs though...", CONTENT);
									player.getDialogue().setNextChatId(12);
									return true;
								} else if (player.getQuestStage(15) == 5) {
									player.getDialogue().sendPlayerChat("The Lady Lumbridge, in Port Sarim.", HAPPY);
									player.getDialogue().setNextChatId(13);
									return true;
								}
							case 11:
								player.getDialogue().sendNpcChat("How can you expect us to sail without a boat!", "Come back to me when you find a worthy vessel.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 12:
								player.getDialogue().sendNpcChat("How can you expect us to sail with a broken boat!", "Come back to me when the vessel is repaired.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 13:
								player.getDialogue().sendNpcChat("That old pile of junk? Last I heard, she wasn't", "seaworthy.", CONTENT);
								return true;
							case 14:
								player.getDialogue().sendPlayerChat("I had the cabin boy Jenkins fix 'er up!", HAPPY);
								return true;
							case 15:
								player.getDialogue().sendNpcChat("You did? Great! Now it's just a matter of getting there.", "Did you manage to find a map to Crandor?", CONTENT);
								return true;
							case 16:
								if (player.getInventory().playerHasItem(1538)) {
									player.getDialogue().sendPlayerChat("Yes, I did! It's right here.", HAPPY);
									player.getDialogue().setNextChatId(18);
									return true;
								} else {
									player.getDialogue().sendPlayerChat("No, not yet...", SAD);
									return true;
								}
							case 17:
								player.getDialogue().sendNpcChat("Well, go find one lad! I'll be waiting here!", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 18:
								player.getDialogue().sendStatement("You hand Ned the map.");
								player.getInventory().removeItem(new Item(1538));
								if (player.getQuestStage(15) == 5) {
									player.setQuestStage(15, 6);
								}
								return true;
							case 19:
								player.getDialogue().sendNpcChat("Fantastic! I'll meet you on the ship laddy!", HAPPY);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case 6: //sailing time
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getPosition().getX() < 3050 && player.getPosition().getY() > 3200) {
									player.getDialogue().sendNpcChat("I'M DOWN HERE LADDY!", "ARE YOU READY TO SAIL!?", HAPPY);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Meet me on the ship boy!", HAPPY);
									player.getDialogue().endDialogue();
									return true;
								}
							case 2:
								player.getDialogue().sendOption("Yes, let's go!", "No, I'm not quite ready yet.");
								return true;
							case 3:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("Yes, let's go!", HAPPY);
										if (!nedSpawnedOnCrandor()) {
											NpcLoader.spawnNpc(918, 2852, 3238, 0, false, true);
										}
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("No, I'm not quite ready yet.", SAD);
										player.getDialogue().endDialogue();
										return true;
								}
							case 4:
								Sailing.sailShip(player, Sailing.ShipRoute.PORT_SARIM_TO_CRANDOR, id);
								player.getDialogue().dontCloseInterface();
								if (player.getQuestStage(15) == 6) {
									player.setQuestStage(15, 7);
								}
								return true;
						}
						return false;
					case 7: //crashed on crandor
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getPosition().getX() < 2860 && player.getPosition().getY() < 3250) {
									player.getDialogue().sendNpcChat("Oomph. That was a rough trip.", SAD);
									return true;
								} else {
									return false;
								}
							case 2:
								player.getDialogue().sendPlayerChat("W-where's Jenkins?", SAD);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("I'm not sure laddy. It looks like he didn't make it.", SAD);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("Poor Jenkins... May his soul rest in peace.", NEAR_TEARS_2);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Aye. He was a brave one.", "Use the memory of Jenkins to fight Elvarg.", "Go now laddy, rid the world of this evil.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
				}
				return false;
			case 6085: //cabin boy jenkins rip
				switch (player.getDialogue().getChatId()) {
					case 1:
						if (player.getQuestStage(15) == 4) {
							player.getDialogue().sendPlayerChat("How can we fix the ship?", CONTENT);
							return true;
						} else {
							player.getDialogue().sendNpcChat("Ship's ready to sail mister.", CONTENT);
							player.getDialogue().setNextChatId(15);
							return true;
						}
					case 2:
						player.getDialogue().sendNpcChat("Well there's a hole underneath, on the hull, mister.", CONTENT);
						return true;
					case 3:
						player.getDialogue().sendPlayerChat("What do I need to fix that?", CONTENT);
						return true;
					case 4:
						player.getDialogue().sendNpcChat("I reckon I can fix it with 90 steel nails...", "and three planks of wood.", CONTENT);
						return true;
					case 5:
						player.getDialogue().sendNpcChat("Do you have those?", CONTENT);
						return true;
					case 6:
						player.getDialogue().sendOption("Yes, here they are.", "No, sorry.");
						return true;
					case 7:
						switch (optionId) {
							case 1:
								if (player.getInventory().playerHasItem(1539, 90) && player.getInventory().playerHasItem(960, 3)) {
									player.getDialogue().sendStatement("You hand Jenkins the repair supplies.");
									player.getInventory().removeItem(new Item(1539, 90));
									player.getInventory().removeItem(new Item(960, 3));
									if (player.getQuestStage(15) == 4) {
										player.setQuestStage(15, 5);
									}
									return true;
								} else {
									player.getDialogue().sendPlayerChat("Actually I was lying. I don't have the supplies.", SAD);
									player.getDialogue().endDialogue();
									return true;
								}
							case 2:
								player.getDialogue().sendPlayerChat("No, sorry.", SAD);
								player.getDialogue().endDialogue();
								return true;
						}
					case 8:
						player.getDialogue().sendNpcChat("Well, great! I'll use my crafting skill,", "and get to work on that hole.", HAPPY);
						return true;
					case 9:
						if (player.getPosition().getZ() == 1) {
							player.getDialogue().sendStatement("Jenkins heads for the underneath of the ship.");
							for (Npc jenkins : World.getNpcs()) {
								if (jenkins == null) {
									continue;
								}
								if (jenkins.getNpcId() == 6085) {
									jenkins.walkTo(new Position(3048, 3208, 1), true);
								}
							}
						} else {
							player.getDialogue().sendStatement("Jenkins heads for the broken part of the ship.");
							for (Npc jenkins : World.getNpcs()) {
								if (jenkins == null) {
									continue;
								}
								if (jenkins.getNpcId() == 6085) {
									jenkins.setFollowingEntity(null);
									jenkins.walkTo(new Position(3046, 3208, 0), false);
								}
							}
						}
						return true;
					case 10:
						if (player.getPosition().getZ() == 1) {
							player.getDialogue().sendStatement("Jenkins descends below.");
							for (Npc jenkins : World.getNpcs()) {
								if (jenkins == null) {
									continue;
								}
								if (jenkins.getNpcId() == 6085) {
									jenkins.teleport(new Position(3048, 3208, 0));
								}
							}
						} else {
							player.getDialogue().sendNpcChat("Here we go!", CONTENT);
							return true;
						}
						return true;
					case 11:
						player.getDialogue().sendStatement("You hear lots of banging and sighing.");
						return true;
					case 12:
						if (player.getPosition().getZ() == 1) {
							player.getDialogue().sendStatement("Jenkins emerges from the belly of the ship.");
							for (Npc jenkins : World.getNpcs()) {
								if (jenkins == null) {
									continue;
								}
								if (jenkins.getNpcId() == 6085) {
									jenkins.teleport(new Position(3048, 3208, 1));
									jenkins.setFollowingEntity(player);
								}
							}
						} else {
							player.getDialogue().sendStatement("Jenkins goes to the deck of the boat.");
							for (Npc jenkins : World.getNpcs()) {
								if (jenkins == null) {
									continue;
								}
								if (jenkins.getNpcId() == 6085) {
									jenkins.teleport(new Position(3048, 3208, 1));
								}
							}
						}
						return true;
					case 13:
						player.getDialogue().sendNpcChat("The hole is all fixed up mister, ready to go.", HAPPY);
						return true;
					case 14:
						player.getDialogue().sendPlayerChat("Thank you very much Jenkins.", HAPPY);
						player.getDialogue().endDialogue();
						return true;
					case 15:
						player.getDialogue().sendPlayerChat("You wouldn't happen to know a captain,", "would you?", CONTENT);
						return true;
					case 16:
						player.getDialogue().sendNpcChat("No mister, but I reckon someone who works", "with rope all day has seen many days at sea.", CONTENT);
						return true;
					case 17:
						player.getDialogue().sendPlayerChat("Hmm...", CONTENT);
						player.getDialogue().endDialogue();
						return true;
				}
				return false;
			case 744: //klarense
				switch (player.getDialogue().getChatId()) {
					case 1:
						if (player.getQuestStage(15) < 3) {
							player.getDialogue().sendPlayerChat("Can I use this boat?", CONTENT);
							return true;
						} else if (player.getQuestStage(15) == 3) {
							player.getDialogue().sendPlayerChat("Can I buy this boat?", CONTENT);
							player.getDialogue().setNextChatId(3);
							return true;
						} else if (player.getQuestStage(15) == 4) {
							player.getDialogue().sendNpcChat("Didja get 'er fixed up yet?", CONTENT);
							player.getDialogue().endDialogue();
							return true;
						} else if (player.getQuestStage(15) == 5) {
							player.getDialogue().sendNpcChat("Good luck kid, you'll need it.", CONTENT);
							player.getDialogue().endDialogue();
							return true;
						} else {
							player.getDialogue().sendNpcChat("Nice to see you again on this lovely day!", CONTENT);
							player.getDialogue().endDialogue();
							return true;
						}
					case 2:
						player.getDialogue().sendNpcChat("The ship is not ready yet and Port Sarim's", "workers are slow, there's no telling how long", "the ship will take to fix.", SAD);
						player.getDialogue().endDialogue();
						return true;
					case 3:
						player.getDialogue().sendNpcChat("Interested in buying the Lady Lumbridge eh? Now, I'll be", "straight with you: she's not quite seaworth right now,", "but if you purchase her I'll give her some work and", "she'll be the nippiest ship this side of Port Khazard.", CONTENT);
						return true;
					case 4:
						player.getDialogue().sendOption("I'd like to buy her.", "Ah well, nevermind.");
						return true;
					case 5:
						switch (optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("I'd like to buy her.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Ah well, nevermind.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
					case 6:
						player.getDialogue().sendNpcChat("How does 5,000 gold sound? I'll even throw in my", "cabin boy, Jenkins, for free! He'll swab the decks and", "splice the mainsails for you!", CONTENT);
						return true;
					case 7:
						player.getDialogue().sendOption("Yep, sounds good (5000 gp).", "I'm not paying that much for a boat!");
						return true;
					case 8:
						switch (optionId) {
							case 1:
								if (player.getInventory().playerHasItem(995, 5000)) {
									player.getDialogue().sendStatement("You hand Klarense 5000 gold.");
									player.getInventory().removeItem(new Item(995, 5000));
									return true;
								} else {
									player.getDialogue().sendPlayerChat("It appears I don't have the coin...", SAD);
									player.getDialogue().endDialogue();
									return true;
								}
							case 2:
								player.getDialogue().sendPlayerChat("I'm not paying that much for a boat!", CONTENT);
								player.getDialogue().setNextChatId(25);
								return true;
						}
					case 9:
						player.getDialogue().sendNpcChat("Okey dokey, she's all yours!", HAPPY);
						if (player.getQuestStage(15) == 3) {
							player.setQuestStage(15, 4);
						}
						return true;
					case 10:
						player.getDialogue().sendPlayerChat("Would you take me to Crandor when she's ready?", CONTENT);
						return true;
					case 11:
						player.getDialogue().sendNpcChat("Crandor? You're joking right?", LAUGHING);
						return true;
					case 12:
						player.getDialogue().sendPlayerChat("No, I want to go to Crandor.", ANGRY_1);
						return true;
					case 13:
						player.getDialogue().sendNpcChat("Then you must be crazy.", "That island is surrounded by reefs that would rip this", "ship to shreds. You'd need an experienced captain to", "stand a chance of getting through.", CONTENT);
						return true;
					case 14:
						player.getDialogue().sendNpcChat("And, even if I could get to it, there's no way I'm going", "any closer to that dragon than I have to. They say it", "can destroy whole ships on one bite.", CONTENT);
						return true;
					case 15:
						player.getDialogue().sendPlayerChat("Alright, well thank you again!", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					case 25:
						player.getDialogue().sendNpcChat("Well bugger off then! The Lady Lumbridge is a fine vessel!", "Worthy of many gold piececs!", ANGRY_1);
						player.getDialogue().endDialogue();
						return true;
				}
				return false;
			case 745: //wormbrain
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendNpcChat("What does human want? Human free me?", CONTENT);
						return true;
					case 2:
						player.getDialogue().sendPlayerChat("No. I've come for Lozar's piece of the", "Crandor map.", CONTENT);
						return true;
					case 3:
						player.getDialogue().sendNpcChat("A map piece hmm...", "Me not sure if I have it!", "Perhaps 10,000 shiny gold could refresh memory!", CONTENT);
						return true;
					case 4:
						player.getDialogue().sendOption("Bribe Wormbrain (10000 gold).", "Nevermind.");
						return true;
					case 5:
						switch (optionId) {
							case 1:
								if (player.getInventory().playerHasItem(995, 10000)) {
									player.getDialogue().sendStatement("You hand Wormbrain 10000 coins through the bars.");
									player.getInventory().removeItem(new Item(995, 10000));
									return true;
								} else {
									player.getDialogue().sendNpcChat("Hrmph, no coin no memory of map!", ANGRY_1);
									player.getDialogue().endDialogue();
									return true;
								}
							case 2:
								player.getDialogue().sendPlayerChat("Nevermind, I better get going.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
					case 6:
						player.getDialogue().sendNpcChat("Ah yes, me remember now - torn piece of map!", CONTENT);
						return true;
					case 7:
						player.getDialogue().sendPlayerChat("Yes, now be a good goblin and give it here.", CONTENT);
						return true;
					case 8:
						player.getDialogue().sendNpcChat("Yes'sah here is your precious torn paper.", CONTENT);
						return true;
					case 9:
						player.getDialogue().sendStatement("Wormbrain hands you a torn piece of a map through the bars.");
						player.getInventory().addItem(new Item(1537));
						return true;
					case 10:
						player.getDialogue().sendPlayerChat("Thank you Wormbrain, now I must be off.", CONTENT);
						return true;
					case 11:
						player.getDialogue().sendNpcChat("So long friendly human!", CONTENT);
						player.getDialogue().endDialogue();
						return true;
				}
				return false;
			case 747: //oziach
				switch (player.getDialogue().getChatId()) {
					case 1:
						if (player.getQuestStage(15) == 8) {
							player.getDialogue().sendPlayerChat("Oziach, I slayed Elvarg.", HAPPY);
							player.getDialogue().setNextChatId(15);
							return true;
						} else if (player.getQuestStage(15) == 1) {
							player.getDialogue().sendNpcChat("Hrumph, what do you want?", CONTENT);
							return true;
						} else {
							return false;
						}
					case 2:
						player.getDialogue().sendPlayerChat("I would like to buy a rune platebody.", CONTENT);
						return true;
					case 3:
						player.getDialogue().sendNpcChat("HA HA! And who are you?", LAUGHING);
						return true;
					case 4:
						player.getDialogue().sendPlayerChat("A member of the Champion's Guild!", "The guildmaster send me.", ANGRY_1);
						return true;
					case 5:
						player.getDialogue().sendNpcChat("Champion my arse, you're puny.", "I'll tell you what real champions do.", "They slay mighty dragons!", CONTENT);
						return true;
					case 6:
						player.getDialogue().sendPlayerChat("What if I've already killed dragons?", CONTENT);
						return true;
					case 7:
						player.getDialogue().sendNpcChat("Can you prove it?", CONTENT);
						return true;
					case 8:
						player.getDialogue().sendPlayerChat("Not really, no...", SAD);
						return true;
					case 9:
						player.getDialogue().sendNpcChat("Tell you what kid, I need a favor.", "This favor could also prove your might.", CONTENT);
						return true;
					case 10:
						player.getDialogue().sendPlayerChat("Go on...", CONTENT);
						return true;
					case 11:
						player.getDialogue().sendNpcChat("I need you to slay the dragon Elvarg for me.", "The dragon lives under Crandor Isle...", "...perhaps if you manage to kill it,", "I'll sell you a rune platebody!", CONTENT);
						if (player.getQuestStage(15) == 1) {
							player.setQuestStage(15, 2);
						}
						return true;
					case 12:
						player.getDialogue().sendPlayerChat("I'll prove you wrong Oziach, you'll see!", CONTENT);
						return true;
					case 13:
						player.getDialogue().sendNpcChat("Scurry back to your guild now,", "tell the guildmaster I send my greetings.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					case 15:
						player.getDialogue().sendNpcChat("You really did, didn't you?", CONTENT);
						return true;
					case 16:
						if (player.getInventory().playerHasItem(11279)) {
							player.getDialogue().sendStatement("You give Oziach Elvarg's mighty head.");
							player.getInventory().removeItem(new Item(11279));
							return true;
						} else {
							player.getDialogue().sendPlayerChat("I seem to have misplaced the creature's head,", "but I assure you the monster is slain.", CONTENT);
							return true;
						}
					case 17:
						player.getDialogue().sendNpcChat("What a marvelous day indeed! Thank you friend.", "I guess you are worthy!", LAUGHING);
						return true;
					case 18:
						player.setQuestStage(15, 9);
						QuestHandler.completeQuest(player, 15);
						player.getDialogue().dontCloseInterface();
						return true;
				}
				return false;
			case 746: //oracle
				switch (player.getDialogue().getChatId()) {
					case 1:
						if (player.getQuestStage(15) >= 3 && player.getQuestStage(15) < 6) {
							player.getDialogue().sendPlayerChat("I seek a piece of the map to the isle of Crandor.", CONTENT);
							return true;
						} else {
							player.getDialogue().sendStatement("The Oracle is busy meditating. Best to not bother her.");
							player.getDialogue().endDialogue();
							return true;
						}
					case 2:
						player.getDialogue().sendNpcChat("The map's behind a door below,", "but entering is rather tough.", "This is what you need to know:", "You must use the following stuff.", CONTENT);
						return true;
					case 3:
						player.getDialogue().sendNpcChat("First, a beer used by a mage.", "Next, some worm string, changed to sheet.", "Then, a small crustacean cage.", "Last, a bowl that has not seen heat.", CONTENT);
						return true;
					case 4:
						player.getDialogue().sendPlayerChat("Erm, okay...", "I suppose I'll be going now.", CONTENT);
						return true;
					case 5:
						player.getDialogue().sendNpcChat("Yes, go now you must!", CONTENT);
						player.getDialogue().endDialogue();
						return true;
				}
				return false;
			case 198: //guildmaster
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendNpcChat("Hello, welcome to the Champion's Guild!", CONTENT);
						return true;
					case 2:
						if (player.getQuestStage(15) < 1) {
							player.getDialogue().sendPlayerChat("I'm looking for a rune platebody", CONTENT);
							return true;
						} else if (player.getQuestStage(15) == 2) {
							player.getDialogue().sendPlayerChat("Oziach said I need to prove myself first.", CONTENT);
							player.getDialogue().setNextChatId(5);
							return true;
						} else {
							player.getDialogue().sendPlayerChat("Thank you, for everything!", CONTENT);
							player.getDialogue().endDialogue();
							return true;
						}
					case 3:
						player.getDialogue().sendNpcChat("Hmmm, there's a man named Oziach...", "He lives in Edgeville and sells rune platebodies.", CONTENT);
						return true;
					case 4:
						player.getDialogue().sendNpcChat("Go speak with him.", CONTENT);
						player.getDialogue().endDialogue();
						player.setQuestStage(15, 1);
						QuestHandler.getQuests()[15].startQuest(player);
						return true;
					case 5:
						player.getDialogue().sendNpcChat("Oh? What do you have to do?", CONTENT);
						return true;
					case 6:
						player.getDialogue().sendPlayerChat("He told me to slay Elvarg.", "A mighty dragon beneath Crandor Isle.", CONTENT);
						return true;
					case 7:
						player.getDialogue().sendPlayerChat("What's wrong?", CONTENT);
						return true;
					case 8:
						player.getDialogue().sendNpcChat("Thiry years ago, Crandor was a thriving community", "with a great tradition of mages and adventurers.", "Crandorians even earned the right to be part of", "the Champion's Guild!", CONTENT);
						return true;
					case 9:
						player.getDialogue().sendNpcChat("One of their adventurers went too far, however. He", "descended into the volcano in the center of Crandor", "and woke Elvarg.", CONTENT);
						return true;
					case 10:
						player.getDialogue().sendNpcChat("He must have fought valiantly against the dragon", "because they say that, to this day, she has a scar down", "her side,", CONTENT);
						return true;
					case 11:
						player.getDialogue().sendNpcChat("but the dragon still won the fight. She emerged and laid", "waste to the whole of Crandor with her fire breath!", CONTENT);
						return true;
					case 12:
						player.getDialogue().sendNpcChat("Some refugees managed to escape in fishing boats.", "They landed on the coast, north of Rimmington, and", "set up camp but the dragon followed them and burned", "the camp to the ground.", CONTENT);
						return true;
					case 13:
						player.getDialogue().sendNpcChat("Out of all the people of Crandor there were only three", "survivors: a trio of wizards who used magic to escape.", "Their names were Thalzar, Lozar and Melzar.", CONTENT);
						return true;
					case 14:
						player.getDialogue().sendNpcChat("If you're serious about taking on Elvarg, first you'll", "need to get to Crandor. The island is surrounded by", "dangerous reefs, so you'll need a ship capable of getting", "through them and a map to show you the way.", CONTENT);
						return true;
					case 15:
						player.getDialogue().sendNpcChat("When you get to Crandor, you'll also need", "an anti-dragon shield.", CONTENT);
						return true;
					case 16:
						player.getDialogue().sendPlayerChat("Where can I find a captain that will sail to Crandor?", CONTENT);
						return true;
					case 17:
						player.getDialogue().sendNpcChat("I would start at Port Sarim.", "There are plenty of captains you could convince there.", CONTENT);
						return true;
					case 18:
						player.getDialogue().sendPlayerChat("And what about a ship?", CONTENT);
						return true;
					case 19:
						player.getDialogue().sendNpcChat("Only a ship made to the old Crandorian design", "would be able to navigate the reefs.", CONTENT);
						return true;
					case 20:
						player.getDialogue().sendNpcChat("If there is one still in existence,", "it's probably in Port Sarim as well.", CONTENT);
						return true;
					case 21:
						player.getDialogue().sendPlayerChat("This doesn't sound too difficult,", "what's the catch here?", CONTENT);
						return true;
					case 22:
						player.getDialogue().sendNpcChat("The catch is that the map has been torn between", "the 3 wizards that escaped Crandor.", CONTENT);
						return true;
					case 23:
						player.getDialogue().sendOption("Where can I find Melzar's piece?", "Where can I find Thalzar's piece?", "Where can I find Lozar's piece?");
						return true;
					case 24:
						switch (optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Where can I find Melzar's piece?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Where can I find Thalzar's piece?", CONTENT);
								player.getDialogue().setNextChatId(63);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("Where can I find Lozar's piece?", CONTENT);
								player.getDialogue().setNextChatId(43);
								return true;
						}
					case 25:
						player.getDialogue().sendNpcChat("Melzar built a castle on the site of the Crandorian", "refugee camp, north of Rimmington. He's locked himself", "in there and no one's seen him for years.", CONTENT);
						return true;
					case 26:
						player.getDialogue().sendNpcChat("The inside of his castle is like a maze, and is populated", "by undead monsters. Maybe, if you could get all the", "way through the maze, you could find his piece of the", "map.", CONTENT);
						return true;
					case 27:
						player.getDialogue().sendNpcChat("Adventurers sometimes go in there to prove themselves,", "so I can give you this key to Melzar's Maze.", CONTENT);
						return true;
					case 28:
						if (player.getInventory().canAddItem(new Item(MAZE_KEY))) {
							player.getDialogue().sendGiveItemNpc("The Guildmaster hands you a key.", new Item(MAZE_KEY));
							player.getInventory().addItem(new Item(MAZE_KEY));
						} else {
							player.getDialogue().sendStatement("Your inventory is full!");
							player.getDialogue().endDialogue();
						}
						return true;
					case 29:
						player.getDialogue().sendOption("Where can I find Thalzar's piece?", "Where can I find Lozar's piece?");
						return true;
					case 30:
						switch (optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Where can I find Thalzar's piece?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Where can I find Lozar's piece?", CONTENT);
								player.getDialogue().setNextChatId(37);
								return true;
						}
					case 31:
						player.getDialogue().sendNpcChat("Thalzar was the most paranoid of the three wizards. He", "hid his map piece and took the location to", "his grave.", CONTENT);
						return true;
					case 32:
						player.getDialogue().sendNpcChat("I don't think you'd be able to find out where it is by", "ordinary means. You'll need to talk to the Oracle on", "Ice Mountain.", CONTENT);
						return true;
					case 33:
						player.getDialogue().sendPlayerChat("And where can I find Lozar's piece?", CONTENT);
						return true;
					case 34:
						player.getDialogue().sendNpcChat("A few weeks ago, I'd have told you to speak to Lozar", "herself, in her house across the river from Lumbridge.", CONTENT);
						return true;
					case 35:
						player.getDialogue().sendNpcChat("Unfortunately, goblin raiders killed her and stole", "everything. The leader of the raid, Wormbrain", "probably has the map piece now.", "He is in prison at Port Sarim.", CONTENT);
						return true;
					case 36:
						player.getDialogue().sendPlayerChat("Thank you, I'll get to work.", CONTENT);
						player.getDialogue().endDialogue();
						player.setQuestStage(15, 3);
						return true;
					case 37:
						player.getDialogue().sendNpcChat("A few weeks ago, I'd have told you to speak to Lozar", "herself, in her house across the river from Lumbridge.", CONTENT);
						return true;
					case 38:
						player.getDialogue().sendNpcChat("Unfortunately, goblin raiders killed her and stole", "everything. The leader of the raid, Wormbrain", "probably has the map piece now.", "He is in prison at Port Sarim.", CONTENT);
						return true;
					case 39:
						player.getDialogue().sendPlayerChat("And where can I find Thalzar's piece?", CONTENT);
						return true;
					case 40:
						player.getDialogue().sendNpcChat("Thalzar was the most paranoid of the three wizards. He", "hid his map piece and took the location to", "his grave.", CONTENT);
						return true;
					case 41:
						player.getDialogue().sendNpcChat("I don't think you'd be able to find out where it is by", "ordinary means. You'll need to talk to the Oracle on", "Ice Mountain.", CONTENT);
						return true;
					case 42:
						player.getDialogue().sendPlayerChat("Thank you, I'll get to work.", CONTENT);
						player.getDialogue().endDialogue();
						player.setQuestStage(15, 3);
						return true;
					case 43:
						player.getDialogue().sendNpcChat("A few weeks ago, I'd have told you to speak to Lozar", "herself, in her house across the river from Lumbridge.", CONTENT);
						return true;
					case 44:
						player.getDialogue().sendNpcChat("Unfortunately, goblin raiders killed her and stole", "everything. The leader of the raid, Wormbrain", "probably has the map piece now.", "He is in prison at Port Sarim.", CONTENT);
						return true;
					case 45:
						player.getDialogue().sendOption("Where can I find Melzar's piece?", "Where can I find Thalzar's piece?");
						return true;
					case 46:
						switch (optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Where can I find Melzar's piece?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Where can I find Thalzar's piece?", CONTENT);
								player.getDialogue().setNextChatId(55);
								return true;
						}
					case 47:
						player.getDialogue().sendNpcChat("Melzar built a castle on the site of the Crandorian", "refugee camp, north of Rimmington. He's locked himself", "in there and no one's seen him for years.", CONTENT);
						return true;
					case 48:
						player.getDialogue().sendNpcChat("The inside of his castle is like a maze, and is populated", "by undead monsters. Maybe, if you could get all the", "way through the maze, you could find his piece of the", "map.", CONTENT);
						return true;
					case 49:
						player.getDialogue().sendNpcChat("Adventurers sometimes go in there to prove themselves,", "so I can give you this key to Melzar's Maze.", CONTENT);
						return true;
					case 50:
						if (player.getInventory().canAddItem(new Item(MAZE_KEY))) {
							player.getDialogue().sendGiveItemNpc("The Guildmaster hands you a key.", new Item(MAZE_KEY));
							player.getInventory().addItem(new Item(MAZE_KEY));
						} else {
							player.getDialogue().sendStatement("Your inventory is full!");
							player.getDialogue().endDialogue();
						}
						return true;
					case 51:
						player.getDialogue().sendPlayerChat("And where can I find Thalzar's piece?", CONTENT);
						return true;
					case 52:
						player.getDialogue().sendNpcChat("Thalzar was the most paranoid of the three wizards. He", "hid his map piece and took the location to", "his grave.", CONTENT);
						return true;
					case 53:
						player.getDialogue().sendNpcChat("I don't think you'd be able to find out where it is by", "ordinary means. You'll need to talk to the Oracle on", "Ice Mountain.", CONTENT);
						return true;
					case 54:
						player.getDialogue().sendPlayerChat("Thank you, I'll get to work.", CONTENT);
						player.getDialogue().endDialogue();
						player.setQuestStage(15, 3);
						return true;
					case 55:
						player.getDialogue().sendNpcChat("Thalzar was the most paranoid of the three wizards. He", "hid his map piece and took the location to", "his grave.", CONTENT);
						return true;
					case 56:
						player.getDialogue().sendNpcChat("I don't think you'd be able to find out where it is by", "ordinary means. You'll need to talk to the Oracle on", "Ice Mountain.", CONTENT);
						return true;
					case 57:
						player.getDialogue().sendPlayerChat("And where can I find Melzar's piece?", CONTENT);
						return true;
					case 58:
						player.getDialogue().sendNpcChat("Melzar built a castle on the site of the Crandorian", "refugee camp, north of Rimmington. He's locked himself", "in there and no one's seen him for years.", CONTENT);
						return true;
					case 59:
						player.getDialogue().sendNpcChat("The inside of his castle is like a maze, and is populated", "by undead monsters. Maybe, if you could get all the", "way through the maze, you could find his piece of the", "map.", CONTENT);
						return true;
					case 60:
						player.getDialogue().sendNpcChat("Adventurers sometimes go in there to prove themselves,", "so I can give you this key to Melzar's Maze.", CONTENT);
						return true;
					case 61:
						if (player.getInventory().canAddItem(new Item(MAZE_KEY))) {
							player.getDialogue().sendGiveItemNpc("The Guildmaster hands you a key.", new Item(MAZE_KEY));
							player.getInventory().addItem(new Item(MAZE_KEY));
						} else {
							player.getDialogue().sendStatement("Your inventory is full!");
							player.getDialogue().endDialogue();
						}
						return true;
					case 62:
						player.getDialogue().sendPlayerChat("Thank you, I'll get to work.", CONTENT);
						player.getDialogue().endDialogue();
						player.setQuestStage(15, 3);
						return true;
					case 63:
						player.getDialogue().sendNpcChat("Thalzar was the most paranoid of the three wizards. He", "hid his map piece and took the location to", "his grave.", CONTENT);
						return true;
					case 64:
						player.getDialogue().sendNpcChat("I don't think you'd be able to find out where it is by", "ordinary means. You'll need to talk to the Oracle on", "Ice Mountain.", CONTENT);
						return true;
					case 65:
						player.getDialogue().sendOption("Where can I find Melzar's piece?", "Where can I find Lozar's piece?");
						return true;
					case 66:
						switch (optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Where can I find Melzar's piece?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Where can I find Lozar's piece?", CONTENT);
								player.getDialogue().setNextChatId(75);
								return true;
						}
					case 67:
						player.getDialogue().sendNpcChat("Melzar built a castle on the site of the Crandorian", "refugee camp, north of Rimmington. He's locked himself", "in there and no one's seen him for years.", CONTENT);
						return true;
					case 68:
						player.getDialogue().sendNpcChat("The inside of his castle is like a maze, and is populated", "by undead monsters. Maybe, if you could get all the", "way through the maze, you could find his piece of the", "map.", CONTENT);
						return true;
					case 69:
						player.getDialogue().sendNpcChat("Adventurers sometimes go in there to prove themselves,", "so I can give you this key to Melzar's Maze.", CONTENT);
						return true;
					case 70:
						if (player.getInventory().canAddItem(new Item(MAZE_KEY))) {
							player.getDialogue().sendGiveItemNpc("The Guildmaster hands you a key.", new Item(MAZE_KEY));
							player.getInventory().addItem(new Item(MAZE_KEY));
						} else {
							player.getDialogue().sendStatement("Your inventory is full!");
							player.getDialogue().endDialogue();
						}
						return true;
					case 71:
						player.getDialogue().sendPlayerChat("And where can I find Lozar's piece?", CONTENT);
						return true;
					case 72:
						player.getDialogue().sendNpcChat("A few weeks ago, I'd have told you to speak to Lozar", "herself, in her house across the river from Lumbridge.", CONTENT);
						return true;
					case 73:
						player.getDialogue().sendNpcChat("Unfortunately, goblin raiders killed her and stole", "everything. The leader of the raid, Wormbrain", "probably has the map piece now.", "He is in prison at Port Sarim.", CONTENT);
						return true;
					case 74:
						player.getDialogue().sendPlayerChat("Thank you, I'll get to work.", CONTENT);
						player.getDialogue().endDialogue();
						player.setQuestStage(15, 3);
						return true;
					case 75:
						player.getDialogue().sendNpcChat("A few weeks ago, I'd have told you to speak to Lozar", "herself, in her house across the river from Lumbridge.", CONTENT);
						return true;
					case 76:
						player.getDialogue().sendNpcChat("Unfortunately, goblin raiders killed her and stole", "everything. The leader of the raid, Wormbrain", "probably has the map piece now.", "He is in prison at Port Sarim.", CONTENT);
						return true;
					case 77:
						player.getDialogue().sendPlayerChat("And where can I find Melzar's piece?", CONTENT);
						return true;
					case 78:
						player.getDialogue().sendNpcChat("Melzar built a castle on the site of the Crandorian", "refugee camp, north of Rimmington. He's locked himself", "in there and no one's seen him for years.", CONTENT);
						return true;
					case 79:
						player.getDialogue().sendNpcChat("The inside of his castle is like a maze, and is populated", "by undead monsters. Maybe, if you could get all the", "way through the maze, you could find his piece of the", "map.", CONTENT);
						return true;
					case 80:
						player.getDialogue().sendNpcChat("Adventurers sometimes go in there to prove themselves,", "so I can give you this key to Melzar's Maze.", CONTENT);
						return true;
					case 81:
						if (player.getInventory().canAddItem(new Item(MAZE_KEY))) {
							player.getDialogue().sendGiveItemNpc("The Guildmaster hands you a key.", new Item(MAZE_KEY));
							player.getInventory().addItem(new Item(MAZE_KEY));
						} else {
							player.getDialogue().sendStatement("Your inventory is full!");
							player.getDialogue().endDialogue();
						}
						return true;
					case 82:
						player.getDialogue().sendPlayerChat("Thank you, I'll get to work.", CONTENT);
						player.getDialogue().endDialogue();
						player.setQuestStage(15, 3);
						return true;
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
