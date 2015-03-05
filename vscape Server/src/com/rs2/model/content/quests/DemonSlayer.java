package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.ANNOYED;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.players.container.inventory.Inventory;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class DemonSlayer implements Quest {

	public static final int QUEST_STARTED = 1;
	public static final int KEYS = 2;
	public static final int BONES_TO_TRAIBORN = 3;
	public static final int KEYS_TO_PRYSIN = 4;
	public static final int FIGHT_DELRITH = 5;
	public static final int QUEST_COMPLETE = 6;

	public static final int BUCKET = 1925;
	public static final int BUCKET_OF_WATER = 1929;
	public static final int BONES = 526;
	public static final int SILVERLIGHT = 2402;
	public static final int KEY_1 = 2399;
	public static final int KEY_2 = 2400;
	public static final int KEY_3 = 2401;
	public static Position DELRITH_SPAWN = new Position(3229, 3369, 0);
	public static String[] delrithMessages = {"DIE, PUNY MORTAL!", "FEEL THE POWER OF MY WRATH!", "YOU THINK YOU CAN SAVE YOUR CITY?", "HA HA HA HA", "SURRENDER NOW AND I'LL KILL YOU QUICKLY!", "AH HA HA", "THAT PUNY STICK DOESN'T HURT ME!", "SILVERLIGHT?! MORE LIKE, HITS-LIGHT! AH AH AH", "MWA HA HA HA", "MY REIGN IS ETERNAL!"};

	public int dialogueStage = 0;
	private int reward[][] = {};
	private int expReward[][] = {};

	private static final int questPointReward = 3;

	public int getQuestID() {
		return 17;
	}

	public String getQuestName() {
		return "Demon Slayer";
	}

	public String getQuestSaveName() {
		return "demon-slayer";
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
		player.getActionSender().sendString("3 Quest Points,", 12150);
		player.getActionSender().sendString("Silverlight", 12151);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), 7334);
	}

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if (questStage == QUEST_STARTED) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Speak with the Gypsy in Varrock.", 8147);

			player.getActionSender().sendString("Gypsy Aris told me about Delrith's impending invasion.", 8149);
			player.getActionSender().sendString("I need to speak with Sir Prysin in the palace.", 8150);
		} else if (questStage == KEYS) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Speak with the Gypsy in Varrock.", 8147);
			player.getActionSender().sendString("@str@" + "Gypsy Aris told me about Delrith's impending invasion.", 8149);

			player.getActionSender().sendString("I need to get the 3 keys for Silverlight:", 8151);
			if (player.getInventory().playerHasItem(2399) || player.getInventory().ownsItem(2399)) {
				player.getActionSender().sendString("@str@" + "Sir Prysin's key he dropped down the drain.", 8152);
			} else {
				player.getActionSender().sendString("@dre@" + "Sir Prysin's key he dropped down the drain.", 8152);
			}
			if (player.getInventory().playerHasItem(2400) || player.getInventory().ownsItem(2400)) {
				player.getActionSender().sendString("@str@" + "Captain Rovin's key.", 8153);
			} else {
				player.getActionSender().sendString("@dre@" + "Captain Rovin's key.", 8153);
			}
			if (player.getInventory().playerHasItem(2401) || player.getInventory().ownsItem(2401)) {
				player.getActionSender().sendString("@str@" + "Wizard Traiborn's key.", 8154);
			} else {
				player.getActionSender().sendString("@dre@" + "Wizard Traiborn's key.", 8154);
			}
			if (playerHasKeys(player)) {
				player.getActionSender().sendString("I should return to Sir Prysin with these keys.", 8156);
			}
		} else if (questStage == BONES_TO_TRAIBORN) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Speak with the Gypsy in Varrock.", 8147);
			player.getActionSender().sendString("@str@" + "Gypsy Aris told me about Delrith's impending invasion.", 8149);

			player.getActionSender().sendString("I need to get the 3 keys for Silverlight:", 8151);
			if (player.getInventory().playerHasItem(2399) || player.getInventory().ownsItem(2399)) {
				player.getActionSender().sendString("@str@" + "Sir Prysin's key he dropped down the drain.", 8152);
			} else {
				player.getActionSender().sendString("@dre@" + "Sir Prysin's key he dropped down the drain.", 8152);
			}
			if (player.getInventory().playerHasItem(2400) || player.getInventory().ownsItem(2400)) {
				player.getActionSender().sendString("@str@" + "Captain Rovin's key.", 8153);
			} else {
				player.getActionSender().sendString("@dre@" + "Captain Rovin's key.", 8153);
			}
			if (player.getInventory().playerHasItem(2401) || player.getInventory().ownsItem(2401)) {
				player.getActionSender().sendString("@str@" + "Wizard Traiborn's key.", 8154);
			} else {
				player.getActionSender().sendString("@dre@" + "Wizard Traiborn's key, I need 25 bones.", 8154);
			}
			if (playerHasKeys(player)) {
				player.getActionSender().sendString("I should return to Sir Prysin with these keys.", 8156);
			}
		} else if (questStage == KEYS_TO_PRYSIN) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Speak with the Gypsy in Varrock.", 8147);
			player.getActionSender().sendString("@str@" + "Gypsy Aris told me about Delrith's impending invasion.", 8149);

			player.getActionSender().sendString("I need to get the 3 keys for Silverlight:", 8151);
			player.getActionSender().sendString("@str@" + "Sir Prysin's key he dropped down the drain.", 8152);
			player.getActionSender().sendString("@str@" + "Captain Rovin's key.", 8153);
			player.getActionSender().sendString("@str@" + "Wizard Traiborn's key.", 8154);
			player.getActionSender().sendString("I should return to Sir Prysin with these keys.", 8156);
		} else if (questStage == FIGHT_DELRITH) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Speak with the Gypsy in Varrock.", 8147);
			player.getActionSender().sendString("@str@" + "Gypsy Aris told me about Delrith's impending invasion.", 8148);

			player.getActionSender().sendString("Sir Prysin has given me Silverlight.", 8150);
			player.getActionSender().sendString("All I need to do now is slay Delrith.", 8151);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Speak with the Gypsy in Varrock.", 8147);
			player.getActionSender().sendString("@str@" + "Gypsy Aris told me about Delrith's impending invasion.", 8148);
			player.getActionSender().sendString("@str@" + "Sir Prysin has given me Silverlight.", 8149);
			player.getActionSender().sendString("@str@" + "All I need to do now is slay Delrith.", 8150);

			player.getActionSender().sendString("@red@" + "You have completed this quest!", 8152);
		} else {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("Speak with the Gypsy in Varrock.", 8147);
			player.getActionSender().sendString("You must be able to defeat a level 27 demon.", 8149);
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 7334);
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
			player.getActionSender().sendString("@yel@" + getQuestName(), 7334);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 7334);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 7334);
		}

	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString("Speak with the Gypsy in Varrock.", 8147);
		player.getActionSender().sendString("You must be able to defeat a level 27 demon.", 8149);
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public static boolean playerHasKeys(Player player) {
		Inventory i = player.getInventory();
		return i.playerHasItem(KEY_1) && i.playerHasItem(KEY_2) && i.playerHasItem(KEY_3);
	}

	public static boolean delrithSpawned() {
		for (Npc npc : World.getNpcs()) {
			if (npc == null) {
				continue;
			}
			if (npc.getNpcId() == 879 || npc.getNpcId() == 880) {
				return true;
			}
		}
		return false;
	}

	public static void spawnDelrith(Player player) {
		Npc delrith = new Npc(879);
		delrith.setSpawnPosition(DELRITH_SPAWN.clone());
		delrith.setPosition(DELRITH_SPAWN.clone());
		World.register(delrith);
		delrith.setNeedsRespawn(false);
		delrith.setPlayerOwner(player.getIndex());
		CombatManager.attack(delrith, player);
		delrith.getUpdateFlags().sendForceMessage("FREE AT LAST!");
	}

	public static void respawnDelrith(Player player) {
		Npc oldDelrith = null;
		for (Npc npc : World.getNpcs()) {
			if (npc == null) {
				continue;
			}
			if (npc.getNpcId() == 880) {
				oldDelrith = npc;
			}
		}
		if (oldDelrith != null) {
			Position SPAWN = oldDelrith.getPosition().clone();
			oldDelrith.setVisible(false);
			World.unregister(oldDelrith);
			Npc delrith = new Npc(879);
			delrith.setSpawnPosition(SPAWN);
			delrith.setPosition(SPAWN);
			World.register(delrith);
			delrith.setPlayerOwner(player.getIndex());
			CombatManager.attack(delrith, player);
			delrith.getUpdateFlags().sendForceMessage("FOOL!");
		}
	}

	public static void sendDelrithMessages() {
		Npc delrith = null;
		for (Npc npc : World.getNpcs()) {
			if (npc == null) {
				continue;
			}
			if (npc.getNpcId() == 879) {
				delrith = npc;
			}
		}
		if (delrith != null) {
			delrith.getUpdateFlags().sendForceMessage(delrithMessages[Misc.random(9)]);
		}

	}

	public static boolean fightingDemon(Player player) {
		if (player.getEquipment().getId(Constants.WEAPON) == 2402 && player.getCombatingEntity() != null
			&& player.getCombatingEntity().isNpc()) {
			Npc npc = (Npc) player.getCombatingEntity();
			int id = npc.getNpcId();
			switch (id) {
				case 879: //delrith
					return true;
			}
			if (npc.getDefinition().getName().toLowerCase().contains("demon")) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public static boolean handleNpcClick(Player player, int npcId) {
		if (npcId == 880 && NpcLoader.checkSpawn(player, 880)) {
			Dialogues.startDialogue(player, 10666);
			return true;
		}
		return false;
	}

	public boolean itemHandling(final Player player, int itemId) {
		return false;
	}

	public static boolean itemPickupHandling(Player player, int itemId) {
		if (player.getQuestStage(17) != 2 && itemId == KEY_1) {
			if (player.getQuestStage(17) < 2) {
				player.getDialogue().sendPlayerChat("I'd better not touch this.", "I don't know who it belongs to!", CONTENT);
				return true;
			} else if (player.getQuestStage(17) > 3) {
				player.getDialogue().sendStatement("You have no need for this key.");
				return true;
			}
		}
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		return false;
	}

	public boolean doItemOnObject(Player player, int object, int item) {
		switch (object) {
			case 2843:
				if (player.getQuestStage(17) == 2 && item == BUCKET_OF_WATER) {
					player.getDialogue().sendStatement("You flush the drain with your bucket of water...", "You hear the key clatter down the sewage drain.", "Better go wait at the end of the sewer pipe.");
					player.getInventory().replaceItemWithItem(new Item(BUCKET_OF_WATER), new Item(BUCKET));
					player.getUpdateFlags().sendAnimation(827);
					GroundItem drop = new GroundItem(new Item(KEY_1), player, new Position(3225, 9897));
					GroundItemManager.getManager().dropItem(drop);
					return true;
				}
		}
		return false;
	}

	public boolean doObjectClicking(Player player, int object, int x, int y) {
		switch (object) {
			case 2843:
				if (player.getQuestStage(17) == 2) {
					player.getDialogue().sendPlayerChat("I can see the key, but there's no way to reach it...", SAD);
					return true;
				} else if (player.getQuestStage(17) > 2) {
					player.getDialogue().sendPlayerChat("I don't want to touch this nasty thing again.", CONTENT);
					return true;
				} else {
					player.getDialogue().sendStatement("You have no reason to touch this drain.");
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
		switch (id) {
			case 882: //gypsy aris
				switch (player.getQuestStage(17)) {
					case 0: //start
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Greetings adventurer.", "Cross my palm with gold and the future", "will be revealed to you.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendOption("Okay, here you go. (1 gold)", "No, I don't believe in that stuff.");
								return true;
							case 3:
								switch (optionId) {
									case 1:
										if (player.getInventory().playerHasItem(995, 1)) {
											player.getDialogue().sendPlayerChat("Okay, here you go.", CONTENT);
											player.getInventory().removeItem(new Item(995, 1));
											return true;
										} else {
											player.getDialogue().sendPlayerChat("Oh, I don't have the coin...", SAD);
											player.getDialogue().endDialogue();
											return true;
										}
									case 2:
										player.getDialogue().sendPlayerChat("No, I don't believe in that stuff.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
								}
							case 4:
								player.getDialogue().sendNpcChat("Come closer, and listen carefully to what the future", "holds for you, as I peer into the swirling", "mists of the crystal ball...", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("I can see images forming. I can see you.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("You are holding a very impressive sword,", "I'm sure I recognize that sword...", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("There's a big dark shadow appearing...", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("AARGH!", DISTRESSED);
								return true;
							case 9:
								player.getDialogue().sendPlayerChat("Are you all right?", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("It's Delrith! Delrith is coming!", SAD);
								return true;
							case 11:
								player.getDialogue().sendPlayerChat("But who is Delrith?", CONTENT);
								return true;
							case 12:
								player.getDialogue().sendNpcChat("Delrith...", CONTENT);
								return true;
							case 13:
								player.getDialogue().sendNpcChat("Delrith is a powerful demon.", CONTENT);
								return true;
							case 14:
								player.getDialogue().sendNpcChat("Oh! I really hope he didn't see me", "looking at him through my crystal ball!", DISTRESSED);
								return true;
							case 15:
								player.getDialogue().sendNpcChat("*Sigh* ...He tired to destroy this city 150 years ago.", "He was stopped just in time by the great hero Wally.", CONTENT);
								return true;
							case 16:
								player.getDialogue().sendNpcChat("Using his magic sword Silverlight, Wally managed to trap", "the demon in the dark wizard's stone circle.", CONTENT);
								return true;
							case 17:
								player.getDialogue().sendNpcChat("That's it! Silverlight was the sword you were holding", "in my vision!", "You are destined to stop the demon this time.", HAPPY);
								return true;
							case 18:
								player.getDialogue().sendOption("How am I meant to fight a demon who can destroy cities?", "Wally doesn't sound like a heroic name.");
								return true;
							case 19:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("How am I meant to fight a demon who can destroy cities?", DISTRESSED);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("Wally doesn't sound like a very heroic name.", CONTENT);
										player.getDialogue().setNextChatId(24);
										return true;
								}
							case 20:
								player.getDialogue().sendNpcChat("If you face Delrith while he is still weak", "from being summoned, and use the correct weapon...", "You will not find the task too arduous.", CONTENT);
								return true;
							case 21:
								player.getDialogue().sendNpcChat("Do not fear. If you follow the path of the", "great hero Wally, then you are sure to defeat him.", CONTENT);
								return true;
							case 22:
								player.getDialogue().sendOption("How am I meant to fight a demon who can destroy cities?", "Wally doesn't sound like a heroic name.");
								return true;
							case 23:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("How am I meant to fight a demon who can destroy cities?", DISTRESSED);
										player.getDialogue().setNextChatId(20);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("Wally doesn't sound like a very heroic name.", CONTENT);
										return true;
								}
							case 24:
								player.getDialogue().sendNpcChat("Yes I know. Maybe that's why history doesn't", "remember him.", "However, he was a great hero.", CONTENT);
								return true;
							case 25:
								player.getDialogue().sendNpcChat("Who knows how much pain and suffering Delrith would", "have brought forth without Wally to stop him!", CONTENT);
								return true;
							case 26:
								player.getDialogue().sendNpcChat("It looks like you are going to need to perform", "similar heroics.", CONTENT);
								return true;
							case 27:
								player.getDialogue().sendPlayerChat("So how did Wally kill Delrith?", CONTENT);
								return true;
							case 28:
								player.getDialogue().sendNpcChat("Wally managed to arrive at the stone circle just", "as Delright was summoned by a cult of dark wizards...", CONTENT);
								return true;
							case 29:
								player.getDialogue().sendNpcChat("By reciting the correct magical incantation, and thrusting", "Silverlight into Delrith while he was newly summoned,", "Wally was able to imprison Delrith.", CONTENT);
								return true;
							case 30:
								player.getDialogue().sendNpcChat("I would imagine an evil sorceror is already starting", "on the rituals to summon Delrith as we speak.", CONTENT);
								return true;
							case 31:
								player.getDialogue().sendOption("What is the magical incantation?", "Where can I find Silverlight?");
								return true;
							case 32:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("What is the magical incantation?", CONTENT);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("Where can I find Silverlight?", CONTENT);
										player.getDialogue().setNextChatId(38);
										return true;
								}
							case 33:
								player.getDialogue().sendNpcChat("Oh yes, let me think a second...", CONTENT);
								return true;
							case 34:
								player.getDialogue().sendNpcChat("Alright, I think I've got it now, it goes...", "Purchai...Gabindo...Camerinthum...Aber...", "Carlem! Have you got that?", CONTENT);
								return true;
							case 35:
								player.getDialogue().sendPlayerChat("I think so, yes.", CONTENT);
								return true;
							case 36:
								player.getDialogue().sendOption("What is the magical incantation?", "Where can I find Silverlight?");
								return true;
							case 37:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("What is the magical incantation?", CONTENT);
										player.getDialogue().setNextChatId(33);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("Where can I find Silverlight?", CONTENT);
										return true;
								}
							case 38:
								player.getDialogue().sendNpcChat("Silverlight has been passed down through Wally's", "descendants. I believe it's current in the care of", "one of the King's knights, Sir Prysin.", CONTENT);
								return true;
							case 39:
								player.getDialogue().sendNpcChat("He shouldn't be too hard to find. He lives", "in the royal palace in this city.", "Tell him Gypsy Aris sent you.", CONTENT);
								return true;
							case 40:
								player.getDialogue().sendPlayerChat("Okay thanks, I'll do my best to stop the demon.", HAPPY);
								return true;
							case 41:
								player.getDialogue().sendNpcChat("Good luck, and may Guthix be with you!", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(17, 1);
								QuestHandler.getQuests()[17].startQuest(player);
								return true;
						}
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("What is the magical incantation again?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("It goes...", "Purchai...Gabindo...Camerinthum...Aber...Carlem.", "Have you got it this time?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("Yes, thank you Gyspsy.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
				}
				return false;
			case 883: //sir prysin
				switch (player.getQuestStage(17)) {
					case 0:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Hello, who are you?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("I'm a mighty adventurer. Who are you?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("I am Sir Prysin.", "A bold and famous knight of the realm.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
					case 1: //talk to prysin
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Hello, who are you?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendOption("I'm a mighty adventurer. Who are you?", "Gypsy Aris said I should come and talk to you.");
								return true;
							case 3:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("I'm a mighty adventurer. Who are you?", CONTENT);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("Gypsy Aris said I should come and talk to you.", CONTENT);
										player.getDialogue().setNextChatId(5);
										return true;
								}
							case 4:
								player.getDialogue().sendNpcChat("I am Sir Prysin.", "A bold and famous knight of the realm.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Gyspy Aris? Is she still alive?", "I remember her from when I was young.", "Well what do we need to speak about?", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("I need to find Silverlight.", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("What do you need to find that for?", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendPlayerChat("I need it to fight Delrith.", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("Delrith? I thought the world was rid of him,", "thanks to my great-grandfather.", DISTRESSED);
								return true;
							case 10:
								player.getDialogue().sendPlayerChat("Well, Aris' crystal ball seems to think otherwise.", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendNpcChat("Oh I do have it, but it's so powerful that", "the King made me put it in a special box with requires", "3 different keys to open it.", CONTENT);
								return true;
							case 12:
								player.getDialogue().sendPlayerChat("And why is this a problem?", CONTENT);
								return true;
							case 13:
								player.getDialogue().sendNpcChat("I kept one of the keys. The gave the other two", "to other people for safe keeping.", CONTENT);
								return true;
							case 14:
								player.getDialogue().sendNpcChat("One I gave to Rovin, the captain of this palace's guard.", CONTENT);
								return true;
							case 15:
								player.getDialogue().sendNpcChat("The other I gave to the wizard Traiborn,", "of the Wizard's Tower.", CONTENT);
								return true;
							case 16:
								player.getDialogue().sendPlayerChat("Can you give me your key?", CONTENT);
								return true;
							case 17:
								player.getDialogue().sendNpcChat("Um...", "Ah...", CONTENT);
								return true;
							case 18:
								player.getDialogue().sendNpcChat("Well there's a problem there as well.", SAD);
								return true;
							case 19:
								player.getDialogue().sendNpcChat("I managed to drop the key in the drain just outside", "the palace's kitchen...", CONTENT);
								return true;
							case 20:
								player.getDialogue().sendPlayerChat("And where does the drain lead to?", CONTENT);
								return true;
							case 21:
								player.getDialogue().sendNpcChat("It's the drain for the drainpipe running from the sink.", "It empties into the city's sewers.", CONTENT);
								return true;
							case 22:
								player.getDialogue().sendOption("Where can I find Captain Rovin?", "Where is Traiborn currently?");
								return true;
							case 23:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("Where can I find Captain Rovin?", CONTENT);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("Where is Traiborn currently?", CONTENT);
										player.getDialogue().setNextChatId(28);
										return true;
								}
							case 24:
								player.getDialogue().sendNpcChat("Captain Rovin lives at the top of the guards'", "quarters in the north-west wing of this palace.", CONTENT);
								return true;
							case 25:
								player.getDialogue().sendPlayerChat("And where is Traiborn currently?", CONTENT);
								return true;
							case 26:
								player.getDialogue().sendNpcChat("I believe he still lives on the second floor of Wizard Tower.", "The one south of Draynor.", CONTENT);
								return true;
							case 27:
								player.getDialogue().sendPlayerChat("Ah yes, I know the one.", CONTENT);
								player.getDialogue().setNextChatId(31);
								return true;
							case 28:
								player.getDialogue().sendNpcChat("I believe he still lives on the second floor of Wizard Tower.", "The one south of Draynor.", CONTENT);
								return true;
							case 29:
								player.getDialogue().sendPlayerChat("And where can I find Captain Rovin?", CONTENT);
								return true;
							case 30:
								player.getDialogue().sendNpcChat("Captain Rovin lives at the top of the guards'", "quarters in the north-west wing of this palace.", CONTENT);
								return true;
							case 31:
								player.getDialogue().sendPlayerChat("Well, I'd better get key hunting.", CONTENT);
								player.setQuestStage(17, 2);
								return true;
							case 32:
								player.getDialogue().sendNpcChat("Good luck then.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
					case 2: //where 2 get keys
					case 3:
					case 4:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (playerHasKeys(player)) {
									player.getDialogue().sendPlayerChat("I found all the keys!", HAPPY);
									player.getDialogue().setNextChatId(7);
									player.setQuestStage(17, 4);
									return true;
								} else {
									player.getDialogue().sendOption("Where is Captain Rovin again?", "Where is Traiborn again?");
									return true;
								}
							case 2:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("Where can I find Captain Rovin again?", CONTENT);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("Where is Traiborn again?", CONTENT);
										player.getDialogue().setNextChatId(5);
										return true;
								}
							case 3:
								player.getDialogue().sendNpcChat("Captain Rovin lives at the top of the guards'", "quarters in the north-west wing of this palace.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("Thank you.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 5:
								player.getDialogue().sendNpcChat("I believe he still lives on the second floor of Wizard Tower.", "The one south of Draynor.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("Ah yes, I know the one, thank you.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 7:
								player.getDialogue().sendNpcChat("Excellent, now I can give you Silverlight!", HAPPY);
								return true;
							case 8:
								player.getDialogue().sendGiveItemNpc("Prysin hands you a gleaming sword for the keys.", new Item(SILVERLIGHT));
								player.getInventory().removeItem(new Item(KEY_1));
								player.getInventory().removeItem(new Item(KEY_2));
								player.getInventory().removeItem(new Item(KEY_3));
								player.getInventory().addItem(new Item(SILVERLIGHT));
								player.setQuestStage(17, 5);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("That sword belonged to my great-grandfather.", "Make sure you treat it with respect!", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("Now go kill that demon!", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
					case 5:
					case 6: //re-buying silverlight
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("I lost Silverlight...", SAD);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Lucky for you, I had another forged.", "It will cost 800 gp for this one.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendOption("Yes, that is fine. (800 gold)", "No, thank you.");
								return true;
							case 4:
								switch (optionId) {
									case 1:
										if (player.getInventory().playerHasItem(995, 800)) {
											player.getDialogue().sendPlayerChat("Here you are.", CONTENT);
											player.getInventory().removeItem(new Item(995, 800));
											return true;
										} else {
											player.getDialogue().sendPlayerChat("I don't have the coin...", SAD);
											player.getDialogue().endDialogue();
											return true;
										}
									case 2:
										player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
								}
							case 5:
								player.getDialogue().sendGiveItemNpc("Sir Prysin hands you a new Silverlight.", new Item(SILVERLIGHT));
								player.getInventory().addItem(new Item(SILVERLIGHT));
								player.getDialogue().endDialogue();
								return true;
						}
				}
				return false;
			case 884: //captain rovin
				switch (player.getQuestStage(17)) {
					case 0: //else
					case 1:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("What are you doing up here?! Only", "palace guards are allowed up here!", ANGRY_1);
								player.getDialogue().endDialogue();
								return true;
						}
					case 2: //keys
					case 3:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("What are you doing up here?! Only", "palace guards are allowed up here!", ANGRY_1);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Yes, I know, but this is important.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Ok, I'm listening. Tell me what is so important.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("There's a demon who wants to invade this city.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Is it a powerful demon?", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("As good as the palace guards are,", "I don't think they are up to taking on a powerful demon.", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("It's not them who are going to fight the demon,", "it's me.", ANNOYED);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("What, all by yourself?", "How are you going to do that?", LAUGHING);
								return true;
							case 9:
								player.getDialogue().sendPlayerChat("I'm going to use the sword Silverlight,", "which I believe you have one of the keys for.", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("Yes, I do. But why should I trust you?", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendPlayerChat("Sir Prysin said you would give me the key.", CONTENT);
								return true;
							case 12:
								player.getDialogue().sendNpcChat("Oh, he did, did he? Well, I don't report", "to Sir Prysin, I report directly to the King!", ANNOYED);
								return true;
							case 13:
								player.getDialogue().sendNpcChat("I didn't work my way up through the ranks of", "the palace guards so I could take orders from", "an in-bred moron who only has his job because his", "ancestor was a hero with a silly name!!", ANGRY_1);
								return true;
							case 14:
								player.getDialogue().sendPlayerChat("Why did he give you one of the keys then?", CONTENT);
								return true;
							case 15:
								player.getDialogue().sendNpcChat("Only because the King ordered him to!", "The King couldn't get Sir Prysin to part with his precious", "ancestral sword, but he made him lock it up", "so he couldn't lose it.", ANNOYED);
								return true;
							case 16:
								player.getDialogue().sendNpcChat("I got one key, and I think some wizard got another.", "What happened to the third key?", CONTENT);
								return true;
							case 17:
								player.getDialogue().sendPlayerChat("Sir Prysin dropped it down a drain.", CONTENT);
								return true;
							case 18:
								player.getDialogue().sendNpcChat("HA HA! That idiot!", LAUGHING);
								return true;
							case 19:
								player.getDialogue().sendNpcChat("Okay, I'll give you the key, just so that", "it's you that kills the demon and not Prysin!", CONTENT);
								return true;
							case 20:
								if (!player.getInventory().playerHasItem(KEY_2)) {
									player.getDialogue().sendGiveItemNpc("Captain Rovin hands you a key.", new Item(KEY_2));
									player.getInventory().addItem(new Item(KEY_2));
									if (playerHasKeys(player)) {
										player.setQuestStage(17, 4);
									}
									player.getDialogue().endDialogue();
									return true;
								} else {
									player.getDialogue().sendStatement("You already have Rovin's key.");
									player.getDialogue().endDialogue();
									return true;
								}
						}
				}
				return false;
			case 881: //traiborn
				switch (player.getQuestStage(17)) {
					case 2:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (!player.getInventory().playerHasItem(KEY_3)) {
									player.getDialogue().sendPlayerChat("Hello, I was told you have a key that was", "given to you by Sir Prysin.", CONTENT);
									return true;
								} else {
									return false;
								}
							case 2:
								player.getDialogue().sendNpcChat("Sir Prysin? Who's that?", "What would I want his key for?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("He told me you were looking after it for him.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("That wasn't very clever of him. I'd lost my head", "if it wasn't screwed on. Go and tell him to", "find someone else to look after his valuables.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("Well, have you got any keys knocking around?", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("Now you come to mention it, yes, I do.", "It's in my special closet of valuable items.", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("Now how do I get into that?", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("I sealed it using one of my magic rituals.", "So, it would make sense that another ritual", "would open it again.", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendPlayerChat("Do you know which ritual to use?", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("Let me think a second...", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendNpcChat("I reckon a simple drazier style ritual will work.", "Hmm, the main problem with that is that I", "would need 25 sets of bones... where will I get that?", CONTENT);
								return true;
							case 12:
								if (player.getInventory().playerHasItem(BONES, 25)) {
									player.getDialogue().sendPlayerChat("I happen to have 25 bones on me.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendPlayerChat("I'll go fetch some bones for you.", CONTENT);
									player.getDialogue().endDialogue();
									player.setQuestStage(17, 3);
									return true;
								}
							case 13:
								player.getDialogue().sendNpcChat("Wow! That's...convenient.", CONTENT);
								return true;
							case 14:
								player.getDialogue().sendGiveItemNpc("You give Traiborn all the bones.", new Item(BONES));
								player.getInventory().removeItem(new Item(BONES, 25));
								return true;
							case 15:
								player.getDialogue().sendNpcChat("Chicken wing of dark and color too...", "hidden near the grass with dew...", "locked away I have a key...", "return it now, please, unto me!", CONTENT);
								return true;
							case 16:
								player.getDialogue().sendNpcChat("Hah! It worked!", LAUGHING);
								return true;
							case 17:
								player.getDialogue().sendGiveItemNpc("Traiborn hands you a key.", new Item(KEY_3));
								player.getInventory().addItem(new Item(KEY_3));
								if (playerHasKeys(player)) {
									player.setQuestStage(17, 4);
								}
								return true;
							case 18:
								player.getDialogue().sendPlayerChat("Thank you Traiborn, see you later!", HAPPY);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case 3: //needs bonners
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (!player.getInventory().playerHasItem(KEY_3)) {
									player.getDialogue().sendNpcChat("Did you get those 25 bones?", CONTENT);
									return true;
								} else {
									return false;
								}
							case 2:
								if (player.getInventory().playerHasItem(BONES, 25)) {
									player.getDialogue().sendPlayerChat("I did, I got you your bones.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendPlayerChat("No, I guess I don't have them still.", SAD);
									player.getDialogue().endDialogue();
									return true;
								}
							case 3:
								player.getDialogue().sendGiveItemNpc("You give Traiborn all the bones.", new Item(BONES));
								player.getInventory().removeItem(new Item(BONES, 25));
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Chick wing of dark and color too...", "hidden near the grass with dew...", "locked away I have a key...", "return it now, please, unto me!", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Hah! It worked!", LAUGHING);
								return true;
							case 6:
								player.getDialogue().sendGiveItemNpc("Traiborn hands you a key.", new Item(KEY_3));
								player.getInventory().addItem(new Item(KEY_3));
								if (playerHasKeys(player)) {
									player.setQuestStage(17, 4);
								}
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("Thank you Traiborn, see you later!", HAPPY);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
				}
				return false;
			case 10666: //weak delrith
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendOption("Carlem Ader Camarinthium Purchase Bagindo!", "Carlem Aber Camerinthum Purchai Gabindo!", "Carmem Aber Caberinthum Purchae Gabindo!", "Carlem Aben Saberithum Purchai Stabindo!");
						return true;
					case 2:
						switch (optionId) {
							case 1:
							case 3:
							case 4:
								respawnDelrith(player);
								player.getDialogue().endDialogue();
								return true;
							case 2:
								if (player.getQuestStage(17) == 5) {
									player.setQuestStage(17, 6);
								}
								for (Npc delrith : World.getNpcs()) {
									if (delrith == null) {
										continue;
									}
									if (delrith.getNpcId() == 880) {
										player.getUpdateFlags().faceEntity(delrith.getFaceIndex());
										player.getUpdateFlags().sendAnimation(2890);
										delrith.setNeedsRespawn(false);
										CombatManager.startDeath(delrith);
									}
								}
								player.getDialogue().sendPlayerChat("Carlem Aber Camerinthum Purchai Gabindo!", CONTENT);
								final Player attacker = player;
								attacker.setStopPacket(true);
								CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
									@Override
									public void execute(CycleEventContainer b) {
										QuestHandler.completeQuest(attacker, 17);
										b.stop();
									}

									@Override
									public void stop() {
										attacker.setStopPacket(false);
									}
								}, 6);
								return true;
						}
					case 3:

						player.getDialogue().dontCloseInterface();
						return true;
				}
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
