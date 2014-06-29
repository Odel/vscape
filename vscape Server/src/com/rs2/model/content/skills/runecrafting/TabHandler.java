package com.rs2.model.content.skills.runecrafting;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_2;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.NEAR_TEARS_2;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import static com.rs2.model.content.quests.DragonSlayer.nedSpawnedOnCrandor;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.players.Player;
import com.rs2.model.players.container.inventory.Inventory;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.model.transport.Sailing;
import com.rs2.util.Misc;

public class TabHandler {
    private static final int AIR_RUNE = 556;
    private static final int EARTH_RUNE = 557;
    private static final int FIRE_RUNE = 554;
    private static final int WATER_RUNE = 555;
    private static final int LAW_RUNE = 563;
    private static final int SOFT_CLAY = 1761;
    
    private static final int VARROCK = 1;
    private static final int LUMBRIDGE = 2;
    private static final int FALADOR = 3;
    private static final int CAMELOT = 4;
    private static final int ARDOUGNE = 5;
    private static final int WATCHTOWER = 6;
    
    private static final int[] TABS = {0, 8007, 8008, 8009, 8010, 8011, 8012};
    
    private static final int[][] VARROCK_RUNES = { {FIRE_RUNE, 1}, {AIR_RUNE, 3}, {LAW_RUNE, 1} };
    private static final int[][] LUMBRIDGE_RUNES = { {EARTH_RUNE, 1}, {AIR_RUNE, 3}, {LAW_RUNE, 1} };
    private static final int[][] FALADOR_RUNES = { {WATER_RUNE, 1}, {AIR_RUNE, 3}, {LAW_RUNE, 1} };
    private static final int[][] CAMELOT_RUNES = { {AIR_RUNE, 5}, {LAW_RUNE, 1}, {0, 0} };
    private static final int[][] ARDOUGNE_RUNES = { {WATER_RUNE, 2}, {LAW_RUNE, 2}, {0, 0} };
    private static final int[][] WATCHTOWER_RUNES = { {EARTH_RUNE, 2}, {LAW_RUNE, 2}, {0, 0} };
    
    public static boolean itemOnItemHandling(Player player, int firstItem, int secondItem) {
	if(firstItem == LAW_RUNE && secondItem == SOFT_CLAY) {
	    Dialogues.startDialogue(player, 10565);
	    return true;
	}
	return false;
    }
    
    public static boolean varrockRunes(Player player) {
	Inventory i = player.getInventory();
	if(i.playerHasItem(AIR_RUNE, 3) && i.playerHasItem(FIRE_RUNE) && i.playerHasItem(LAW_RUNE)) {
	    return true;
	}
	else {
	    return false;
	}
    }
    public static boolean lumbridgeRunes(Player player) {
	Inventory i = player.getInventory();
	if(i.playerHasItem(AIR_RUNE, 3) && i.playerHasItem(EARTH_RUNE) && i.playerHasItem(LAW_RUNE)) {
	    return true;
	}
	else {
	    return false;
	}
    }
    public static boolean faladorRunes(Player player) {
	Inventory i = player.getInventory();
	if(i.playerHasItem(AIR_RUNE, 3) && i.playerHasItem(WATER_RUNE) && i.playerHasItem(LAW_RUNE)) {
	    return true;
	}
	else {
	    return false;
	}
    }
    public static boolean camelotRunes(Player player) {
	Inventory i = player.getInventory();
	if(i.playerHasItem(AIR_RUNE, 5) && i.playerHasItem(LAW_RUNE)) {
	    return true;
	}
	else {
	    return false;
	}
    }
    public static boolean ardougneRunes(Player player) {
	Inventory i = player.getInventory();
	if(i.playerHasItem(WATER_RUNE, 2) && i.playerHasItem(LAW_RUNE, 2)) {
	    return true;
	}
	else {
	    return false;
	}
    }
    public static boolean watchtowerRunes(Player player) {
	Inventory i = player.getInventory();
	if(i.playerHasItem(EARTH_RUNE, 2) && i.playerHasItem(LAW_RUNE, 2)) {
	    return true;
	}
	else {
	    return false;
	}
    }
    public static boolean checkRunes(Player player, int spell) {
	if(spell == VARROCK) return varrockRunes(player);
	else if(spell == LUMBRIDGE) return lumbridgeRunes(player);
	else if(spell == FALADOR) return faladorRunes(player);
	else if(spell == CAMELOT) return camelotRunes(player);
	else if(spell == ARDOUGNE) return ardougneRunes(player);
	else if(spell == WATCHTOWER) return watchtowerRunes(player);
	else return false;
    }
    
    public static int[][] getRunes(int spell) {
	if(spell == VARROCK) return VARROCK_RUNES;
	else if(spell == LUMBRIDGE) return LUMBRIDGE_RUNES;
	else if(spell == FALADOR) return FALADOR_RUNES;
	else if(spell == CAMELOT) return CAMELOT_RUNES;
	else if(spell == ARDOUGNE) return ARDOUGNE_RUNES;
	else if(spell == WATCHTOWER) return WATCHTOWER_RUNES;
	else return null;
    }
    public static void craftTab(final Player player,final int spell, final int count) {
	final int task = player.getTask();
	player.setSkilling(new CycleEvent() {
	int amnt = count;

	    @Override
	    public void execute(CycleEventContainer container) {
		if (!player.checkTask(task) || !checkRunes(player, spell) || amnt == 0) {
		    container.stop();
		    return;
		}
		if (!player.getInventory().playerHasItem(SOFT_CLAY)) {
		    player.getActionSender().sendMessage("You have run out of soft clay!");
		    container.stop();
		    return;
		}
		player.getUpdateFlags().sendAnimation(791);
		player.getActionSender().sendMessage("You craft the tab.");
		player.getInventory().removeItem(new Item(getRunes(spell)[0][0], getRunes(spell)[0][1]));
		player.getInventory().removeItem(new Item(getRunes(spell)[1][0], getRunes(spell)[1][1]));
		player.getInventory().removeItem(new Item(getRunes(spell)[2][0], getRunes(spell)[2][1]));
		player.getInventory().removeItem(new Item(SOFT_CLAY));
		player.getInventory().addItem(new Item(TABS[spell]));
		amnt--;

	    }

	    @Override
	    public void stop() {
		player.resetAnimation();
	    }
	});
	CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 3);
	return;
    }
    public static boolean doItemOnObject(Player player, int object, int item) {
	switch(object) {

	}
	return false;
    }
    
    public static boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
	switch(id) {
	    case 10565:
		switch (player.getDialogue().getChatId()) {
		    case 1:
			player.getDialogue().sendOption("Varrock", "Lumbridge", "Falador", "Camelot", "More...");
			return true;
		    case 2:
			switch(optionId) {
			    case 1:
				if(varrockRunes(player) && player.getSkill().getLevel()[Skill.MAGIC] >= 25) {
				    player.getDialogue().sendOption("1", "5", "10", "25");
				    return true;
				}
				else if(varrockRunes(player) && player.getSkill().getLevel()[Skill.MAGIC] < 25) {
				    player.getDialogue().sendStatement("You need 25 magic to craft this tab.");
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!varrockRunes(player) && player.getSkill().getLevel()[Skill.MAGIC] >= 25) {
				    player.getDialogue().sendStatement("You do not have the runes required.");
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 2:
				if(lumbridgeRunes(player) && player.getSkill().getLevel()[Skill.MAGIC] >= 31) {
				    player.getDialogue().sendOption("1", "5", "10", "25");
				    player.getDialogue().setNextChatId(4);
				    return true;
				}
				else if(lumbridgeRunes(player) && player.getSkill().getLevel()[Skill.MAGIC] < 31) {
				    player.getDialogue().sendStatement("You need 25 magic to craft this tab.");
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!lumbridgeRunes(player) && player.getSkill().getLevel()[Skill.MAGIC] >= 31) {
				    player.getDialogue().sendStatement("You do not have the runes required.");
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 3:
				if(faladorRunes(player) && player.getSkill().getLevel()[Skill.MAGIC] >= 37) {
				    player.getDialogue().sendOption("1", "5", "10", "25");
				    player.getDialogue().setNextChatId(5);
				    return true;
				}
				else if(faladorRunes(player) && player.getSkill().getLevel()[Skill.MAGIC] < 37) {
				    player.getDialogue().sendStatement("You need 25 magic to craft this tab.");
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!faladorRunes(player) && player.getSkill().getLevel()[Skill.MAGIC] >= 37) {
				    player.getDialogue().sendStatement("You do not have the runes required.");
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 4:
				if(camelotRunes(player) && player.getSkill().getLevel()[Skill.MAGIC] >= 45) {
				    player.getDialogue().sendOption("1", "5", "10", "25");
				    player.getDialogue().setNextChatId(6);
				    return true;
				}
				else if(camelotRunes(player) && player.getSkill().getLevel()[Skill.MAGIC] < 45) {
				    player.getDialogue().sendStatement("You need 25 magic to craft this tab.");
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!camelotRunes(player) && player.getSkill().getLevel()[Skill.MAGIC] >= 45) {
				    player.getDialogue().sendStatement("You do not have the runes required.");
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 5:
				player.getDialogue().sendOption("Ardougne", "Watchtower", "Back");
				player.getDialogue().setNextChatId(15);
				return true;
			}
		    case 3:
			switch(optionId) {
			    case 1:
				craftTab(player, VARROCK, 1);
				break;
			    case 2:
				craftTab(player, VARROCK, 5);
				break;
			    case 3:
				craftTab(player, VARROCK, 10);
				break;
			    case 4:
				craftTab(player, VARROCK, 25);
				break;
			}
		    break;
		    case 4:
			switch(optionId) {
			    case 1:
				craftTab(player, LUMBRIDGE, 1);
				break;
			    case 2:
				craftTab(player, LUMBRIDGE, 5);
				break;
			    case 3:
				craftTab(player, LUMBRIDGE, 10);
				break;
			    case 4:
				craftTab(player, LUMBRIDGE, 25);
				break;
			}
		    break;
		    case 5:
			switch(optionId) {
			    case 1:
				craftTab(player, FALADOR, 1);
				break;
			    case 2:
				craftTab(player, FALADOR, 5);
				break;
			    case 3:
				craftTab(player, FALADOR, 10);
				break;
			    case 4:
				craftTab(player, FALADOR, 25);
				break;
			}
		    break;
		    case 6:
			switch(optionId) {
			    case 1:
				craftTab(player, CAMELOT, 1);
				break;
			    case 2:
				craftTab(player, CAMELOT, 5);
				break;
			    case 3:
				craftTab(player, CAMELOT, 10);
				break;
			    case 4:
				craftTab(player, CAMELOT, 25);
				break;
			}
		    break;
		    case 7:
			switch(optionId) {
			    case 1:
				craftTab(player, ARDOUGNE, 1);
				break;
			    case 2:
				craftTab(player, ARDOUGNE, 5);
				break;
			    case 3:
				craftTab(player, ARDOUGNE, 10);
				break;
			    case 4:
				craftTab(player, ARDOUGNE, 25);
				break;
			}
		    break;
		    case 8:
			switch(optionId) {
			    case 1:
				craftTab(player, WATCHTOWER, 1);
				break;
			    case 2:
				craftTab(player, WATCHTOWER, 5);
				break;
			    case 3:
				craftTab(player, WATCHTOWER, 10);
				break;
			    case 4:
				craftTab(player, WATCHTOWER, 25);
				break;
			}
		    break;
		    case 15:
			switch(optionId) {
			    case 1:
				if(ardougneRunes(player) && player.getSkill().getLevel()[Skill.MAGIC] >= 51) {
				    player.getDialogue().sendOption("1", "5", "10", "25");
				    player.getDialogue().setNextChatId(7);
				    return true;
				}
				else if(ardougneRunes(player) && player.getSkill().getLevel()[Skill.MAGIC] < 51) {
				    player.getDialogue().sendStatement("You need 25 magic to craft this tab.");
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!ardougneRunes(player) && player.getSkill().getLevel()[Skill.MAGIC] >= 51) {
				    player.getDialogue().sendStatement("You do not have the runes required.");
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 2:
				if(watchtowerRunes(player) && player.getSkill().getLevel()[Skill.MAGIC] >= 58) {
				    player.getDialogue().sendOption("1", "5", "10", "25");
				    player.getDialogue().setNextChatId(8);
				    return true;
				}
				else if(watchtowerRunes(player) && player.getSkill().getLevel()[Skill.MAGIC] < 58) {
				    player.getDialogue().sendStatement("You need 25 magic to craft this tab.");
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!watchtowerRunes(player) && player.getSkill().getLevel()[Skill.MAGIC] >= 58) {
				    player.getDialogue().sendStatement("You do not have the runes required.");
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 3:
				player.getDialogue().setNextChatId(1);
				return true;
			}
		}
	break;
	}
    return false;
    }
    
}