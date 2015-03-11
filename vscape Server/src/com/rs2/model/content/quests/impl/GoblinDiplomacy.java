package com.rs2.model.content.quests;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.effect.impl.BindingEffect;
import com.rs2.model.content.combat.hit.Hit;
import com.rs2.model.content.combat.hit.HitDef;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_2;
import static com.rs2.model.content.dialogue.Dialogues.ANNOYED;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.Skill;
import com.rs2.util.Misc;
import java.util.ArrayList;

public class GoblinDiplomacy implements Quest {

	public static final int QUEST_STARTED = 1;
	public static final int BLUE_ARMOR = 2;
	public static final int BROWN_ARMOR = 3;
	public static final int QUEST_COMPLETE = 4;

	public static final int ORANGE_GOBLIN_MAIL = 286;
	public static final int BLUE_GOBLIN_MAIL = 287;
	public static final int GOBLIN_MAIL = 288;
	public static final int YELLOW_DYE = 1765;
	public static final int RED_DYE = 1763;
	public static final int BLUE_DYE = 1767;
	public static final int ORANGE_DYE = 1769;
	public static final int GREEN_DYE = 1771;
	public static final int PURPLE_DYE = 1773;
	public static final int WOAD_LEAF = 1793;
	public static final int ONION = 1957;
	public static final int REDBERRIES = 1951;
	public static final int GOLD_BAR = 2357;

	public static final int GENERAL_WARTFACE = 4494;
	public static final int GENERAL_BENTNOZE = 4493;
	public static final int GRUBFOOT = 4496;
	public static final int WYSON_THE_GARDENER = 36;
	public static final int RED_GOBLIN = 4479;
	public static final int GREEN_GOBLIN = 4486; //peetah paarker i will get you
	public static final Position GRUBFOOT_SPAWN = new Position(2961, 3506, 0);
	public static final Position GRUBFOOT_STEP_AWAY = new Position(2961, 3505, 0);
	public static final String[] RED_MESSAGES = {"Die greeny!", "Green no good!", "You die now!", "Stoopid green!", "Red da best!", "Red strong like rum!", "Greeny is weak!", "Green wif envy!", "O deer, yous are red! Wif blood!", "Yousa got red blod on you!"};
	public static final String[] GREEN_MESSAGES = {"Die red!", "Red is ded!", "Red like your blod!", "Stoopid red!", "Bettah dead than red!", "Green da best!", "Green strong than red!", "Red weak color!", "You wont like me angree!", "Ah aha ha aha!"};
	public int dialogueStage = 0;
	private int reward[][] = {
		{GOLD_BAR, 1}
	};
	private int expReward[][] = {
		{Skill.CRAFTING, 200}
	};

	private static final int questPointReward = 5;

	public int getQuestID() {
		return 19;
	}

	public String getQuestName() {
		return "Goblin Diplomacy";
	}

	public String getQuestSaveName() {
		return "goblin-diplomacy";
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
		player.getActionSender().sendString("5 Quest Points,", 12150);
		player.getActionSender().sendString("450 Crafting XP", 12151); //manually did the 2.25x calculation - don't touch
		player.getActionSender().sendString("A gold bar", 12152);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), 7338);
	}

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if (questStage == QUEST_STARTED) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to the Generals of Goblin Village.", 8147);

			player.getActionSender().sendString("The two warring goblin generals are fighting over a color.", 8149);
			player.getActionSender().sendString("They demanded I bring them orange goblin mail to try.", 8150);
			player.getActionSender().sendString("I should be able to dye some regular goblin mail.", 8151);
		} else if (questStage == BLUE_ARMOR) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to the Generals of Goblin Village.", 8147);
			player.getActionSender().sendString("@str@" + "The two warring goblin generals are fighting over a color.", 8149);
			player.getActionSender().sendString("@str@" + "They demanded I bring them orange goblin mail to try.", 8150);
			player.getActionSender().sendString("@str@" + "I should be able to dye some regular goblin mail.", 8151);

			player.getActionSender().sendString("The generals didn't like the orange mail.", 8153);
			player.getActionSender().sendString("They now demand blue goblin mail.", 8154);
		} else if (questStage == BROWN_ARMOR) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to the Generals of Goblin Village.", 8147);
			player.getActionSender().sendString("@str@" + "The two warring goblin generals are fighting over a color.", 8149);
			player.getActionSender().sendString("@str@" + "They demanded I bring them orange goblin mail to try.", 8150);
			player.getActionSender().sendString("@str@" + "I should be able to dye some regular goblin mail.", 8151);
			player.getActionSender().sendString("@str@" + "The generals demanded blue goblin mail.", 8153);

			player.getActionSender().sendString("The generals didn't like blue either.", 8155);
			player.getActionSender().sendString("They now demand their wretched original goblin mail.", 8156);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to the Generals of Goblin Village.", 8147);
			player.getActionSender().sendString("@str@" + "The two warring goblin generals are fighting over a color.", 8149);
			player.getActionSender().sendString("@str@" + "They demanded I bring them orange goblin mail to try.", 8150);
			player.getActionSender().sendString("@str@" + "I should be able to dye some regular goblin mail.", 8151);
			player.getActionSender().sendString("@str@" + "The generals demanded blue goblin mail.", 8153);
			player.getActionSender().sendString("@str@" + "The generals demanded the original goblin mail.", 8155);

			player.getActionSender().sendString("@red@" + "You have completed this quest!", 8157);
		} else {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("Talk to the Generals of Goblin Village.", 8147);
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 7338);
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
			player.getActionSender().sendString("@yel@" + getQuestName(), 7338);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 7338);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 7338);
		}

	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString("Talk to the Generals of Goblin Village.", 8147);
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public static ArrayList<Npc> getGreenGoblin() {
		ArrayList<Npc> goblins = new ArrayList<Npc>();
		for (Npc npc : World.getNpcs()) {
			if (npc == null) {
				continue;
			}
			if (npc.getNpcId() == GREEN_GOBLIN) {
				goblins.add(npc);
			}
		}
		return goblins;
	}

	public static void sendRedMessages(Npc goblin) {
		if (Misc.random(3) == 1) {
			goblin.getUpdateFlags().sendForceMessage(RED_MESSAGES[Misc.randomMinusOne(RED_MESSAGES.length)]);
		}
	}

	public static void sendGreenMessages(Npc goblin) {
		if (Misc.random(3) == 1) {
			goblin.getUpdateFlags().sendForceMessage(GREEN_MESSAGES[Misc.randomMinusOne(GREEN_MESSAGES.length)]);
		}
	}

	public boolean itemHandling(Player player, int itemId) {
		switch (itemId) {
			case GOBLIN_MAIL:
			case BLUE_GOBLIN_MAIL:
			case ORANGE_GOBLIN_MAIL:
				player.getDialogue().sendPlayerChat("This looks a bit too small for me.", SAD);
				return true;
		}
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		if ((firstItem == YELLOW_DYE && secondItem == RED_DYE) || (firstItem == RED_DYE && secondItem == YELLOW_DYE)) {
			player.getActionSender().sendMessage("You mix the two dyes together.");
			player.getInventory().removeItem(new Item(RED_DYE));
			player.getInventory().replaceItemWithItem(new Item(YELLOW_DYE), new Item(ORANGE_DYE));
			return true;
		} else if ((firstItem == YELLOW_DYE && secondItem == BLUE_DYE) || (firstItem == BLUE_DYE && secondItem == YELLOW_DYE)) {
			player.getActionSender().sendMessage("You mix the two dyes together.");
			player.getInventory().removeItem(new Item(BLUE_DYE));
			player.getInventory().replaceItemWithItem(new Item(YELLOW_DYE), new Item(GREEN_DYE));
			return true;
		} else if ((firstItem == RED_DYE && secondItem == BLUE_DYE) || (firstItem == BLUE_DYE && secondItem == RED_DYE)) {
			player.getActionSender().sendMessage("You mix the two dyes together.");
			player.getInventory().removeItem(new Item(BLUE_DYE));
			player.getInventory().replaceItemWithItem(new Item(RED_DYE), new Item(PURPLE_DYE));
			return true;
		} else if (firstItem == ORANGE_DYE && secondItem == GOBLIN_MAIL) {
			player.getActionSender().sendMessage("You dye the goblin mail orange.");
			player.getInventory().removeItem(new Item(GOBLIN_MAIL));
			player.getInventory().replaceItemWithItem(new Item(ORANGE_DYE), new Item(ORANGE_GOBLIN_MAIL));
			return true;
		} else if (firstItem == BLUE_DYE && secondItem == GOBLIN_MAIL) {
			player.getActionSender().sendMessage("You dye the goblin mail blue.");
			player.getInventory().removeItem(new Item(GOBLIN_MAIL));
			player.getInventory().replaceItemWithItem(new Item(BLUE_DYE), new Item(BLUE_GOBLIN_MAIL));
			return true;
		} else {
			return false;
		}
	}

	public boolean doItemOnObject(final Player player, int object, int item) {
		return false;
	}

	public boolean doObjectClicking(Player player, int object, int x, int y) {
		switch (object) {
			case 23:
				if (x == 2962 && y == 3506 && player.getQuestStage(19) == 1) {
					for (Npc npc : World.getNpcs()) {
						if (npc == null) {
							continue;
						}
						if (npc.getNpcId() == GRUBFOOT) {
							return false;
						}
					}
					NpcLoader.spawnNpc(null, new Npc(GRUBFOOT), GRUBFOOT_SPAWN, false, "Ooph!");
					player.walkTo(GRUBFOOT_STEP_AWAY, false);
					return true;
				}
				return false;
			case 365:
				if (x == 2951 && y == 3507 && (player.getQuestStage(19) > 0 && player.getQuestStage(19) < 4)) {
					if (player.getInventory().playerHasExactItem(GOBLIN_MAIL, 1) || player.getInventory().playerHasExactItem(GOBLIN_MAIL, 2)) {
						player.getInventory().addItem(new Item(GOBLIN_MAIL));
						player.getDialogue().sendGiveItemNpc("You find an old, dusty goblin mail.", new Item(GOBLIN_MAIL));
					} else {
						return false;
					}
				}
				return false;
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
		switch (id) {
			case GENERAL_WARTFACE:
			case GENERAL_BENTNOZE:
				switch (player.getQuestStage(19)) {
					case 0: //start
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().setLastNpcTalk(GENERAL_WARTFACE);
								player.getDialogue().sendNpcChat("Green armor da best.", ANGRY_1);
								return true;
							case 2:
								player.getDialogue().setLastNpcTalk(GENERAL_BENTNOZE);
								player.getDialogue().sendNpcChat("No, no, red every time.", ANGRY_2);
								return true;
							case 3:
								player.getDialogue().setLastNpcTalk(GENERAL_WARTFACE);
								player.getDialogue().sendNpcChat("Go away human, we busy.", ANGRY_1);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("Why are you arguing about the colors", "of your armor?", CONTENT);
								return true;
							case 5:
								player.getDialogue().setLastNpcTalk(GENERAL_BENTNOZE);
								player.getDialogue().sendNpcChat("We decide to celebrate new century by changing", "color of our armor, brown boring. Want change.", ANGRY_2);
								return true;
							case 6:
								player.getDialogue().setLastNpcTalk(GENERAL_WARTFACE);
								player.getDialogue().sendNpcChat("Problem is they want different change to us.", ANGRY_2);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("Wouldn't you prefer peace?", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("Yeah, peace is good as long as it peace", "wearing green armor!", ANGRY_1);
								return true;
							case 9:
								player.getDialogue().setLastNpcTalk(GENERAL_BENTNOZE);
								player.getDialogue().sendNpcChat("But green too much like skin, make you feel naked!", ANGRY_2);
								return true;
							case 10:
								player.getDialogue().sendPlayerChat("How about a different armor color?", "Not green or red.", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendNpcChat("That would mean me wrong! But at least Wartface", "not right!", ANGRY_1);
								return true;
							case 12:
								player.getDialogue().setLastNpcTalk(GENERAL_WARTFACE);
								player.getDialogue().sendNpcChat("Me dunno what that look like. Have to see", "armor before we decide.", CONTENT);
								return true;
							case 13:
								player.getDialogue().setLastNpcTalk(GENERAL_BENTNOZE);
								player.getDialogue().sendNpcChat("Human! Bring us armor in new color!", CONTENT);
								return true;
							case 14:
								player.getDialogue().setLastNpcTalk(GENERAL_WARTFACE);
								player.getDialogue().sendNpcChat("What color we try?", CONTENT);
								return true;
							case 15:
								player.getDialogue().setLastNpcTalk(GENERAL_BENTNOZE);
								player.getDialogue().sendNpcChat("Orange may be good.", CONTENT);
								return true;
							case 16:
								player.getDialogue().setLastNpcTalk(GENERAL_WARTFACE);
								player.getDialogue().sendNpcChat("Yes... Human! Bring us orange armor!", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(19, 1);
								QuestHandler.getQuests()[19].startQuest(player);
								return true;
						}
						return false;
					case 1: //orange armor
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getInventory().playerHasItem(ORANGE_GOBLIN_MAIL)) {
									player.getDialogue().sendPlayerChat("I have some orange armor here.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Fetch us orange armor human!", ANGRY_1);
									player.getDialogue().endDialogue();
									return true;
								}
							case 2:
								player.getDialogue().sendGiveItemNpc("You hand the generals the orange mail.", new Item(ORANGE_GOBLIN_MAIL));
								player.getInventory().removeItem(new Item(ORANGE_GOBLIN_MAIL));
								HitDef hitDef = new HitDef(null, HitType.NORMAL, 0).setStartingHitDelay(100000);
								Hit hit = new Hit(player, player, hitDef);
								BindingEffect bind = new BindingEffect(1000000);
								bind.initialize(hit); //try and step away during dialogue now :-)
								return true;
							case 3:
								player.getDialogue().setLastNpcTalk(GENERAL_WARTFACE);
								player.getDialogue().sendNpcChat("No, I don't like that much.", CONTENT);
								return true;
							case 4:
								player.getDialogue().setLastNpcTalk(GENERAL_BENTNOZE);
								player.getDialogue().sendNpcChat("It clashes with skin color.", CONTENT);
								return true;
							case 5:
								player.getDialogue().setLastNpcTalk(GENERAL_WARTFACE);
								player.getDialogue().sendNpcChat("We need darker color, like blue!", ANGRY_2);
								return true;
							case 6:
								player.getDialogue().setLastNpcTalk(GENERAL_BENTNOZE);
								player.getDialogue().sendNpcChat("Yeah, blue might be good.", CONTENT);
								return true;
							case 7:
								player.getDialogue().setLastNpcTalk(GENERAL_WARTFACE);
								player.getDialogue().sendNpcChat("Human! Get us blue armor!", ANGRY_1);
								player.setQuestStage(19, 2);
								player.resetEffects();
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case 2: //blue armor
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getInventory().playerHasItem(BLUE_GOBLIN_MAIL)) {
									player.getDialogue().sendPlayerChat("I have some blue armor here.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Fetch us blue armor human!", ANGRY_1);
									player.getDialogue().endDialogue();
									return true;
								}
							case 2:
								player.getDialogue().sendGiveItemNpc("You hand the generals the blue mail.", new Item(BLUE_GOBLIN_MAIL));
								player.getInventory().removeItem(new Item(BLUE_GOBLIN_MAIL));
								HitDef hitDef = new HitDef(null, HitType.NORMAL, 0).setStartingHitDelay(100000);
								Hit hit = new Hit(player, player, hitDef);
								BindingEffect bind = new BindingEffect(1000000);
								bind.initialize(hit); //try and step away during dialogue now :-)
								return true;
							case 3:
								player.getDialogue().setLastNpcTalk(GENERAL_BENTNOZE);
								player.getDialogue().sendNpcChat("That not right. Not goblin color at all.", ANGRY_1);
								return true;
							case 4:
								player.getDialogue().setLastNpcTalk(GENERAL_WARTFACE);
								player.getDialogue().sendNpcChat("Goblins wear dark earth color, like brown!", ANGRY_2);
								return true;
							case 5:
								player.getDialogue().setLastNpcTalk(GENERAL_BENTNOZE);
								player.getDialogue().sendNpcChat("Yeah, brown might be good.", CONTENT);
								return true;
							case 6:
								player.getDialogue().setLastNpcTalk(GENERAL_WARTFACE);
								player.getDialogue().sendNpcChat("Human! Get us brown armor!", ANGRY_1);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("Isn't that what you used to...", "Nevermind. I'll get the armor.", ANNOYED);
								player.getDialogue().endDialogue();
								player.resetEffects();
								player.setQuestStage(19, 3);
								return true;
						}
						return false;
					case 3: //brown armor
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getInventory().playerHasItem(GOBLIN_MAIL)) {
									player.getDialogue().sendPlayerChat("I have some 'brown' armor here.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Fetch us brown armor human!", ANGRY_1);
									player.getDialogue().endDialogue();
									return true;
								}
							case 2:
								player.getDialogue().sendGiveItemNpc("You hand them the unaltered goblin mail.", new Item(GOBLIN_MAIL));
								player.getInventory().removeItem(new Item(GOBLIN_MAIL));
								HitDef hitDef = new HitDef(null, HitType.NORMAL, 0).setStartingHitDelay(100000);
								Hit hit = new Hit(player, player, hitDef);
								BindingEffect bind = new BindingEffect(1000000);
								bind.initialize(hit); //try and step away during dialogue now :-)
								return true;
							case 3:
								player.getDialogue().setLastNpcTalk(GENERAL_WARTFACE);
								player.getDialogue().sendNpcChat("That color quite nice. Me can see me-self wearing that.", HAPPY);
								return true;
							case 4:
								player.getDialogue().setLastNpcTalk(GENERAL_BENTNOZE);
								player.getDialogue().sendNpcChat("It a deal then. Brown armor it is.", HAPPY);
								return true;
							case 5:
								player.getDialogue().setLastNpcTalk(GENERAL_WARTFACE);
								player.getDialogue().sendNpcChat("Thank you for sorting out argument human.", "Take this as reward.", CONTENT);
								return true;
							case 6:
								player.resetEffects();
								player.setQuestStage(19, 4);
								QuestHandler.completeQuest(player, 19);
								player.getDialogue().dontCloseInterface();
								return true;
						}
						return false;
				}
				return false;
			case RED_GOBLIN:
				switch (player.getDialogue().getChatId()) {
					case 1:
						if (player.getQuestStage(19) < 4) {
							player.getDialogue().sendNpcChat("Red is da best! Red da color of blood!", CONTENT);
							player.getDialogue().endDialogue();
							return true;
						} else {
							player.getDialogue().sendNpcChat("Brown maybee best color...", CONTENT);
							player.getDialogue().endDialogue();
							return true;
						}
				}
				return false;
			case GREEN_GOBLIN:
				switch (player.getDialogue().getChatId()) {
					case 1:
						if (player.getQuestStage(19) < 4) {
							player.getDialogue().sendNpcChat("Green is da best! Green da color of skin!", CONTENT);
							player.getDialogue().endDialogue();
							return true;
						} else {
							player.getDialogue().sendNpcChat("Brown maybee best color...", CONTENT);
							player.getDialogue().endDialogue();
							return true;
						}
				}
				return false;
			case GRUBFOOT:
				switch (player.getQuestStage(19)) {
					case 1:
					case 2:
					case 3: //tell them playas where to get goblin mail
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Who are you?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Please sir no hurt me!", SAD);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("Oh, I assume you're the general's servant then.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Me help masters very well!", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("Do you know where I can find some goblin mail?", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("It's none of me business but...", "human can always kill outsider goblins for mail!", HAPPY);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("Anywhere else?", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("Me not so sure, me have one...", "...maybee sack in other house has one too!", HAPPY);
								return true;
							case 9:
								player.getDialogue().sendPlayerChat("Can I have yours?", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("As long as human promise to get generals peace!", HAPPY);
								return true;
							case 11:
								player.getDialogue().sendPlayerChat("I promise.", CONTENT);
								return true;
							case 12:
								if (!player.getInventory().playerHasItem(GOBLIN_MAIL)) {
									player.getDialogue().sendGiveItemNpc("Grubfoot hands you a goblin mail from his sack.", new Item(GOBLIN_MAIL));
									player.getInventory().addItem(new Item(GOBLIN_MAIL));
									return true;
								} else {
									player.getDialogue().sendNpcChat("Yousa already have goblin mail!", ANGRY_1);
									player.getDialogue().endDialogue();
									for (Npc npc : World.getNpcs()) {
										if (npc == null) {
											continue;
										}
										if (npc.getNpcId() == GRUBFOOT) {
											NpcLoader.destroyNpc(npc);
										}
									}
									return true;
								}
							case 13:
								player.getDialogue().sendPlayerChat("Thanks, I guess.", CONTENT);
								return true;
							case 14:
								player.getDialogue().sendStatement("Grubfoot crawls back into his sack.");
								player.getDialogue().endDialogue();
								for (Npc npc : World.getNpcs()) {
									if (npc == null) {
										continue;
									}
									if (npc.getNpcId() == GRUBFOOT) {
										NpcLoader.destroyNpc(npc);
									}
								}
								return true;

						}
				}
			case WYSON_THE_GARDENER:
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendNpcChat("Hello there!", CONTENT);
						return true;
					case 2:
						player.getDialogue().sendPlayerChat("I'm in need of some Woad leaves.", CONTENT);
						return true;
					case 3:
						player.getDialogue().sendNpcChat("I may have some, how much are you willing", "to spend each?", CONTENT);
						return true;
					case 4:
						player.getDialogue().sendOption("1 gold", "10 gold", "15 gold", "20 gold");
						return true;
					case 5:
						switch (optionId) {
							case 1:
								player.getDialogue().sendNpcChat("Don't make me laugh!", "I can't afford to sell them that cheap!", LAUGHING);
								player.getDialogue().endDialogue();
								return true;
							case 2:
								player.getDialogue().sendNpcChat("A decent offer, but still too low for me. Sorry.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 3:
								player.getDialogue().sendNpcChat("That's a fair price.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("You're very generous, I respect that.", "I'll give you two leaves for that price.", CONTENT);
								player.getDialogue().setNextChatId(7);
								return true;
						}
					case 6:
						if (player.getInventory().playerHasItem(995, 15)) {
							player.getDialogue().sendGiveItemNpc("You exchange your money for a woad leaf.", new Item(WOAD_LEAF));
							player.getDialogue().endDialogue();
							player.getInventory().replaceItemWithItem(new Item(995, 15), new Item(WOAD_LEAF));
							return true;
						} else {
							player.getDialogue().sendPlayerChat("I'm afraid I was bluffing...", "I don't have the coin...", SAD);
							player.getDialogue().endDialogue();
							return true;
						}
					case 7:
						if (player.getInventory().playerHasItem(995, 20)) {
							player.getDialogue().sendGiveItemNpc("You exchange your money for 2 woad leaves.", new Item(WOAD_LEAF));
							player.getDialogue().endDialogue();
							player.getInventory().replaceItemWithItem(new Item(995, 20), new Item(WOAD_LEAF, 2));
							return true;
						} else {
							player.getDialogue().sendPlayerChat("I'm afraid I was bluffing...", "I don't have the coin...", SAD);
							player.getDialogue().endDialogue();
							return true;
						}
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
