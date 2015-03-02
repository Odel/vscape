package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.*;
import com.rs2.model.content.skills.Crafting.GemCrafting;
import com.rs2.model.content.skills.Crafting.GlassMaking;
import com.rs2.model.content.skills.Crafting.SilverCrafting;
import com.rs2.model.content.skills.smithing.Smelting;
import com.rs2.model.players.item.ItemManager;

public class ElementalWorkshop implements Quest {

	public static final int QUEST_STARTED = 1;
	public static final int KEY_GET = 2;
	public static final int DISREPAIR = 3;
	public static final int WATER_LEVER = 4;
	public static final int WATER_FLOWING = 5;
	public static final int BELLOWS_PATCHED = 6;
	public static final int AIR_BLOWING = 7;
	public static final int FURNACE = 8;
	public static final int SMELT = 9;
	public static final int SMITH = 10;
	public static final int QUEST_COMPLETE = 11;

	public int dialogueStage = 0;
	private int reward[][] = {};
	private int expReward[][] = {
		{Skill.CRAFTING, 5000},
		{Skill.SMITHING, 5000},};

	private static final int questPointReward = 1;

	public int getQuestID() {
		return 12;
	}

	public String getQuestName() {
		return "Elemental Workshop";
	}

	public String getQuestSaveName() {
		return "elemental-workshop";
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
		player.getActionSender().sendString("1 Quest Point,", 12150);
		player.getActionSender().sendString((int) (expReward[0][1] * Constants.EXP_RATE) + " Crafting XP,", 12151);
		player.getActionSender().sendString((int) (expReward[0][1] * Constants.EXP_RATE) + " Smithing XP,", 12152);
		player.getActionSender().sendString("The ability to make", 12153);
		player.getActionSender().sendString("Elemental Shields.", 12154);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), 7459);
	}

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if (questStage == QUEST_STARTED) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Search the bookshelves around Seers' Village.", 8147);

			player.getActionSender().sendString("You found an old book about the Elemental Workshop.", 8149);
		} else if (questStage == KEY_GET) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Search the bookshelves around Seers' Village.", 8147);
			player.getActionSender().sendString("@str@" + "You found an old book about the Elemental Workshop.", 8149);

			player.getActionSender().sendString("You found an old key to an old workshop in the book.", 8151);
		} else if (questStage == DISREPAIR || questStage == WATER_LEVER) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Search the bookshelves around Seers' Village.", 8147);
			player.getActionSender().sendString("@str@" + "You found an old book about the Elemental Workshop.", 8149);
			player.getActionSender().sendString("@str@" + "You found an old key to an old workshop in the book.", 8151);

			player.getActionSender().sendString("You've found the Elemental Workshop, however...", 8153);
			player.getActionSender().sendString("...It seems to have fallen into disrepair.", 8154);
			player.getActionSender().sendString("Getting the water flowing should be priority.", 8155);
		} else if (questStage == WATER_FLOWING || questStage == BELLOWS_PATCHED) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Search the bookshelves around Seers' Village.", 8147);
			player.getActionSender().sendString("@str@" + "You found an old book about the Elemental Workshop.", 8149);
			player.getActionSender().sendString("@str@" + "You found an old key to an old workshop in the book.", 8151);
			player.getActionSender().sendString("@str@" + "You've found the Elemental Workshop, however...", 8153);
			player.getActionSender().sendString("@str@" + "...It seems to have fallen into disrepair.", 8154);
			player.getActionSender().sendString("@str@" + "Getting the water flowing should be priority.", 8155);

			player.getActionSender().sendString("The water is flowing and powering the machines.", 8157);
			player.getActionSender().sendString("But it seems the bellows for the furnace have a leak.", 8158);
		} else if (questStage == AIR_BLOWING) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Search the bookshelves around Seers' Village.", 8147);
			player.getActionSender().sendString("@str@" + "You found an old book about the Elemental Workshop.", 8149);
			player.getActionSender().sendString("@str@" + "You found an old key to an old workshop in the book.", 8151);
			player.getActionSender().sendString("@str@" + "You've found the Elemental Workshop, however...", 8153);
			player.getActionSender().sendString("@str@" + "...It seems to have fallen into disrepair.", 8154);
			player.getActionSender().sendString("@str@" + "Getting the water flowing should be priority.", 8155);
			player.getActionSender().sendString("@str@" + "The water is flowing and powering the machines.", 8157);
			player.getActionSender().sendString("@str@" + "But it seems the bellows for the furnace have a leak.", 8158);

			player.getActionSender().sendString("With the furnace hot, you can smelt normal ore.", 8160);
			player.getActionSender().sendString("However, you'll need a stone bowl for elemental ore.", 8161);
			player.getActionSender().sendString("There has to be one laying around somewhere.", 8162);
		} else if (questStage == FURNACE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Search the bookshelves around Seers' Village.", 8147);
			player.getActionSender().sendString("@str@" + "You found an old book about the Elemental Workshop.", 8149);
			player.getActionSender().sendString("@str@" + "You found an old key to an old workshop in the book.", 8151);
			player.getActionSender().sendString("@str@" + "You've found the Elemental Workshop, however...", 8153);
			player.getActionSender().sendString("@str@" + "...It seems to have fallen into disrepair.", 8154);
			player.getActionSender().sendString("@str@" + "Getting the water flowing should be priority.", 8155);
			player.getActionSender().sendString("@str@" + "The water is flowing and powering the machines.", 8157);
			player.getActionSender().sendString("@str@" + "But it seems the bellows for the furnace have a leak.", 8158);
			player.getActionSender().sendString("@str@" + "With the furnace hot enough, you can smelt ore.", 8160);
			player.getActionSender().sendString("@str@" + "You'll need the ore and a stone bowl.", 8161);
			player.getActionSender().sendString("@str@" + "There has to be one laying around somewhere.", 8162);

			player.getActionSender().sendString("Use your stone bowl to put some lava in the furnace.", 8164);
		} else if (questStage == SMELT) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Search the bookshelves around Seers' Village.", 8147);
			player.getActionSender().sendString("@str@" + "You found an old book about the Elemental Workshop.", 8149);
			player.getActionSender().sendString("@str@" + "You found an old key to an old workshop in the book.", 8151);
			player.getActionSender().sendString("@str@" + "You've found the Elemental Workshop, however...", 8153);
			player.getActionSender().sendString("@str@" + "...It seems to have fallen into disrepair.", 8154);
			player.getActionSender().sendString("@str@" + "Getting the water flowing should be priority.", 8155);
			player.getActionSender().sendString("@str@" + "The water is flowing and powering the machines.", 8157);
			player.getActionSender().sendString("@str@" + "But it seems the bellows for the furnace have a leak.", 8158);
			player.getActionSender().sendString("@str@" + "With the furnace hot enough, you can smelt ore.", 8160);
			player.getActionSender().sendString("@str@" + "You'll need the ore and a stone bowl.", 8161);
			player.getActionSender().sendString("@str@" + "There has to be one laying around somewhere.", 8162);
			player.getActionSender().sendString("@str@" + "Use your stone bowl to put some lava in the furnace.", 8164);

			player.getActionSender().sendString("Smelt the elemental ore in the furnace.", 8166);
		} else if (questStage == SMITH) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Search the bookshelves around Seers' Village.", 8147);
			player.getActionSender().sendString("@str@" + "You found an old book about the Elemental Workshop.", 8149);
			player.getActionSender().sendString("@str@" + "You found an old key to an old workshop in the book.", 8151);
			player.getActionSender().sendString("@str@" + "You've found the Elemental Workshop, however...", 8153);
			player.getActionSender().sendString("@str@" + "...It seems to have fallen into disrepair.", 8154);
			player.getActionSender().sendString("@str@" + "Getting the water flowing should be priority.", 8155);
			player.getActionSender().sendString("@str@" + "The water is flowing and powering the machines.", 8157);
			player.getActionSender().sendString("@str@" + "But it seems the bellows for the furnace have a leak.", 8158);
			player.getActionSender().sendString("@str@" + "With the furnace hot enough, you can smelt ore.", 8160);
			player.getActionSender().sendString("@str@" + "You'll need the ore and a stone bowl.", 8161);
			player.getActionSender().sendString("@str@" + "There has to be one laying around somewhere.", 8162);
			player.getActionSender().sendString("@str@" + "Use your stone bowl to put some lava in the furnace.", 8164);
			player.getActionSender().sendString("@str@" + "Smelt the elemental ore in the furnace.", 8166);

			player.getActionSender().sendString("Smith the elemental metal on a workbench.", 8168);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Search the bookshelves around Seers' Village.", 8147);
			player.getActionSender().sendString("@str@" + "You found an old book about the Elemental Workshop.", 8149);
			player.getActionSender().sendString("@str@" + "You found an old key to an old workshop in the book.", 8151);
			player.getActionSender().sendString("@str@" + "You've found the Elemental Workshop, however...", 8153);
			player.getActionSender().sendString("@str@" + "...It seems to have fallen into disrepair.", 8154);
			player.getActionSender().sendString("@str@" + "Getting the water flowing should be priority.", 8155);
			player.getActionSender().sendString("@str@" + "The water is flowing and powering the machines.", 8157);
			player.getActionSender().sendString("@str@" + "But it seems the bellows for the furnace have a leak.", 8158);
			player.getActionSender().sendString("@str@" + "With the furnace hot enough, you can smelt ore.", 8160);
			player.getActionSender().sendString("@str@" + "You'll need the ore and a stone bowl.", 8161);
			player.getActionSender().sendString("@str@" + "There has to be one laying around somewhere.", 8162);
			player.getActionSender().sendString("@str@" + "Use your stone bowl to put some lava in the furnace.", 8164);
			player.getActionSender().sendString("@str@" + "Smelt the elemental ore in the furnace.", 8166);
			player.getActionSender().sendString("@str@" + "Smith the elemental metal on a workbench.", 8168);

			player.getActionSender().sendString("@red@You have completed this quest!", 8170);
		} else {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("Search the bookshelves around Seers' Village.", 8147);
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 7459);
	}

	public boolean questCompleted(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if ((questStage >= QUEST_STARTED) && (questStage < QUEST_COMPLETE)) {
			return true;
		}
		return false;
	}

	public void sendQuestTabStatus(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if ((questStage >= QUEST_STARTED) && questStage != QUEST_COMPLETE) {
			player.getActionSender().sendString("@yel@" + getQuestName(), 7459);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 7459);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 7459);
		}

	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString("Search the bookshelves around Seers' Village.", 8147);
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public static void summon(Player player) {
		player.getActionSender().walkTo(-1, 0, true);
		Npc npc = new Npc(238);
		npc.setPosition(new Position(2780, 3515, 0));
		npc.setSpawnPosition(new Position(2780, 3515, 0));
		World.register(npc);
		npc.setPlayerOwner(player.getIndex());
		Dialogues.startDialogue(player, 238);
	}

	public boolean itemHandling(Player player, int itemId) {
		if (itemId == 2886) {
			player.getDialogue().sendPlayerChat("Hey, the spine of this book is all lumpy.", Dialogues.CONTENT);
			player.getActionSender().sendMessage("Perhaps you should use a knife on the spine of the book.");
			return true;
		}
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		if (firstItem == 946 && secondItem == 2886) {
			player.getInventory().removeItem(new Item(2886));
			player.getDialogue().sendStatement("You cut apart the book to find a key.");
			player.getInventory().addItem(new Item(2887));
			if (player.getQuestStage(12) == 1) {
				player.setQuestStage(12, 2);
			}
			return true;
		}
		return false;
	}

	public boolean doItemOnObject(Player player, int object, int item) {
		switch (object) {
			case 3402: //elemental workshop workbench
				if (item == 2893 && player.getQuestStage(12) >= 10 && player.getSkill().getLevel()[Skill.SMITHING] >= 20) {
					if (player.getInventory().playerHasItem(2347)) {
						player.getInventory().removeItem(new Item(2893));
						player.getInventory().addItem(new Item(2890));
						if (player.getQuestStage(12) == 10) {
							player.setQuestStage(12, 11);
							QuestHandler.completeQuest(player, 12);
							return true;
						}
						return true;
					} else {
						player.getDialogue().sendStatement("You need a hammer to smith!");
						return true;
					}
				} else if (player.getSkill().getLevel()[Skill.SMITHING] < 20) {
					player.getDialogue().sendStatement("You need 20 smithing to smith and elemental shield.");
					return true;
				}
				break;
			case 3413: //elemental workshop furnace
				if (item == 2889 && player.getQuestStage(12) == 8) {
					player.getDialogue().sendStatement("The furnace flares dramatically as you pour the lava in.");
					player.getInventory().replaceItemWithItem(new Item(2889), new Item(2888));
					player.setQuestStage(12, 9);
					return true;
				} else if (item == 2892 && player.getQuestStage(12) >= 9) {
					if (player.getInventory().playerHasItem(453, 4)) {
						player.getInventory().removeItem(new Item(2892));
						player.getInventory().removeItem(new Item(453, 4));
						player.getInventory().addItem(new Item(2893));
						player.getActionSender().sendMessage("You smelt the elemental ore in the furnace.");
						player.getActionSender().sendMessage("You retrieve elemental metal from the furnace.");
						player.getUpdateFlags().sendAnimation(899);
						player.getActionSender().sendSound(469, 0, 0);
						if (player.getQuestStage(12) == 9) {
							player.setQuestStage(12, 10);
						}
						return true;
					} else {
						player.getDialogue().sendStatement("You need 4 coal to smelt Elemental Ore.");
						return true;
					}
				} else if (player.getQuestStage(12) >= 7) {
					if (item == GlassMaking.BUCKET_OF_SAND) {
						GlassMaking.makeMoltenGlass(player);
					} else if (item == GemCrafting.GOLD_BAR) {
						GemCrafting.openInterface(player);
					} else if (item == SilverCrafting.SILVER_BAR) {
						Menus.sendSkillMenu(player, "silverCrafting");
					} else if (ItemManager.getInstance().getItemName(item).toLowerCase().endsWith("ore") && item != 668) {
						Smelting.smeltInterface(player);
					} else if (item == 668) {
						Dialogues.startDialogue(player, 10200);
					}
					return true;
				}
				break;
			case 3414: //elemental workshop lava trough
				if (item == 2888 && player.getQuestStage(12) == 8) {
					player.getDialogue().sendStatement("You carefully fill the bowl with lava.");
					player.getInventory().replaceItemWithItem(new Item(2888), new Item(2889));
					return true;
				}
				break;
		}
		return false;
	}

	public boolean doObjectClicking(Player player, int object, int x, int y) {
		switch (object) {
			case 3389: //elemental workshop bookshelf
				if (x == 2716 && y == 3481 && !player.getInventory().ownsItem(2886)) {
					if (player.getQuestStage(12) < 1) {
						player.setQuestStage(12, 1);
						QuestHandler.getQuests()[12].startQuest(player);
					}
					player.getDialogue().sendStatement("You find an old, beaten book.");
					player.getInventory().addItem(new Item(2886));
					return true;
				}
				break;
			case 3390:
			case 3391: // elemental workshop wall
				if (player.getInventory().playerHasItem(2887)) {
					player.fadeTeleport(new Position(2716, 9888));
					if (player.getQuestStage(12) == 2) {
						player.setQuestStage(12, 3);
					}
					return true;
				}
				break;
			case 3397: //elemental workshop boxes
				if (player.getQuestStage(12) == 5 && !player.getInventory().playerHasItem(1741)) {
					player.getDialogue().sendStatement("You find some leather after rummaging through the boxes.");
					player.getInventory().addItem(new Item(1741));
					return true;
				}
				break;
			case 3409: //elemental workshop bellows lever
				if (player.getQuestStage(12) == 6) {
					player.getDialogue().sendStatement("You hear the whoosh of air as the bellows come to life.");
					//player.getActionSender().animateObject(2734, 9882, 0, 469);
					player.setQuestStage(12, 7);
					return true;
				}
				break;
			case 3398: //elemental workshop crate for stone bowl
				if (player.getQuestStage(12) == 7 && !player.getInventory().playerHasItem(2888)) {
					player.getDialogue().sendStatement("You find an empty stone bowl.");
					player.getInventory().addItem(new Item(2888));
					player.setQuestStage(12, 8);
					return true;
				}
				break;
			case 3410: //elemental workshop bellows
				if (player.getQuestStage(12) == 5) {
					if (player.getSkill().getLevel()[Skill.CRAFTING] < 20) {
						player.getDialogue().sendStatement("You need a Crafting level of 20 to fix the bellows.");
						return true;
					} else if (player.getInventory().playerHasItem(1733) && player.getInventory().playerHasItem(1734) && player.getInventory().playerHasItem(1741)) {
						player.getInventory().removeItem(new Item(1734));
						player.getInventory().removeItem(new Item(1741));
						player.getDialogue().sendStatement("You patch up the bellows.", "You should be able to pull the lever now.");
						player.setQuestStage(12, 6);
						return true;
					} else {
						player.getDialogue().sendStatement("The hole looks rather large.", "You will need needle, thread, and a piece of regular leather.");
						return true;
					}
				}
				break;
			case 3404:
			case 3405: //elemental workshop water valve
				if (x == 2726 && y == 9908 && player.getQuestStage(12) == 3) {
					player.getDialogue().sendStatement("You turned the valve on. You hear water flowing.");
					return true;
				} else if (x == 2713 && y == 9908 && player.getQuestStage(12) == 3) {
					player.getDialogue().sendStatement("You turned the valve on. You hear lots of water flowing.");
					player.setQuestStage(12, 4);
					return true;
				}
				break;
			case 3406: //elemental workshop water wheel
				if (player.getQuestStage(12) == 4) {
					player.getDialogue().sendStatement("You hear a lurch and a squeel as the wheel turns.");
					player.getActionSender().animateObject(2719, 9907, 0, 472);
					player.setQuestStage(12, 5);
					return true;
				} else {
					player.getDialogue().sendStatement("The lever won't budge without flowing water.");
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

	public boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
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
