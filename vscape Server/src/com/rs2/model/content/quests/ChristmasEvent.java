package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.dialogue.DialogueManager;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.content.minigames.Snowball;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.GameObjectDef;
import com.rs2.model.objects.functions.TrapDoor;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;
import java.util.ArrayList;

public class ChristmasEvent implements Quest {

	public static boolean CHRISTMAS_ENABLED = false;
	//Quest stages
	public static final int QUEST_STARTED = 1;
	public static final int GATHER_COAL = 2;
	public static final int RETURN_TO_SANTA = 3;
	public static final int SNOW_JAIL = 4;
	public static final int INVESTIGATE = 5;
	public static final int TRAPDOOR = 6;
	public static final int CONFRONT_SANTA = 7;
	public static final int QUEST_COMPLETE = 8;
	//Items
	public static final int SNOWBALL_ITEM = 10501;
	//Positions
	public static final Position SNOWY_JAIL = new Position(2855, 3783, 0);
	public static final Position ENCOUNTER = new Position(2775, 3840, 0);
    //Interfaces

	//Npcs
	public static final int SANTA = 1552;
	public static final int COAL_SLAVE = 979;
	public static final int DJANGO = 970;
	public static final int WISE_OLD_MAN = 2253;
	public static final int ICE_FIEND = 3406;
	public static final int ICE_GIANT = 111;
	public static final int ICE_SPIDER = 64;
	public static final int ICE_WOLF = 1558;
	public static final int ICE_TROLL = 1936;
	public static final int LIGHT_CREATURE = 2021;
	//Objects
	public static final int ICE_COLUMN = 516;
	public static final int ICE_LIGHT = 210;
	public static final int TRAPDOOR_OBJ = 6434;
	public static final int SLOPE = 5015;

	public static final int PLACE_ANIM = 832;

	public int dialogueStage = 0; //Ignore

	private int reward[][] = { //Items in the form of {Id, #},
	};

	private int expReward[][] = { //Exp in the form of {Skill.AGILITY, x},
	}; //The 2.25 multiplier is added later, use vanilla values

	private static final int questPointReward = 0;

	public int getQuestID() {
		return 34;
	}

	public String getQuestName() {
		return "Christmas Event";
	}

	public String getQuestSaveName() {
		return "christmasevent";
	}

	public boolean canDoQuest(Player player) { //Use to check for strict auxiliary quest requirements
		return true;
	}

	public void getReward(Player player) { //Don't change
		for (int[] rewards : reward) {
			player.getInventory().addItemOrDrop(new Item(rewards[0], rewards[1]));
		}
		for (int[] expRewards : expReward) {
			player.getSkill().addExp(expRewards[0], (expRewards[1]));
		}
		player.addQuestPoints(questPointReward);
		player.getActionSender().QPEdit(player.getQuestPoints());
	}

	//End of quest reward scroll interface, tweak what's necessary
	public void completeQuest(Player player) {
		//If writing in exp, be sure to express it manually as 2.25 the vanilla reward
		getReward(player);
		player.getActionSender().sendInterface(12140);
		player.getActionSender().sendItemOnInterface(995, 200, 12142);
		player.getActionSender().sendString("You have completed " + getQuestName() + "!", 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("x", 12150);
		player.getActionSender().sendString("x", 12151);
		player.getActionSender().sendString("x", 12152);
		player.getActionSender().sendString("x", 12153);
		player.getActionSender().sendString("x", 12154);
		player.getActionSender().sendString("x", 12155);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		//Updates the players quest list to show the green complete quest
		player.getActionSender().sendString("@gre@" + getQuestName(), 7361);
	}

    //Here we send the quest log, with the text and then the line number in sendString(string, line number)
	//The line number is according to the interface, just add to it for the next line
	public void sendQuestRequirements(Player player) {
		return;
	}

	public void sendQuestInterface(Player player) { //Don't change
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) { //Don't change
		player.setQuestStage(getQuestID(), QUEST_STARTED);
	}

	public boolean questCompleted(Player player) //Don't change
	{
		int questStage = player.getQuestStage(getQuestID());
		if (questStage >= QUEST_COMPLETE) {
			return true;
		}
		return false;
	}

	public void sendQuestTabStatus(Player player) { //Don't change
		return;
	}

	public int getQuestPoints() { //Don't change
		return questPointReward;
	}

	public void showInterface(Player player) { //Don't change
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
		player.getActionSender().sendString(getQuestName(), 8144);
	}

	public enum LightCreatureEnum {

		ONE(0, new Position(2782, 3868, 0), new Position(2784, 3868, 0)),
		TWO(1, new Position(2781, 3867, 0), new Position(2785, 3867, 0)),
		THREE(2, new Position(2780, 3866, 0), new Position(2786, 3866, 0)),
		FOUR(3, new Position(2765, 3846, 0), new Position(2775, 3846, 0)),
		FIVE(4, new Position(2769, 3852, 0), new Position(2775, 3847, 0)),
		SIX(5, new Position(2775, 3855, 0), new Position(2779, 3855, 0)),
		SEVEN(6, new Position(2762, 3856, 0), new Position(2771, 3856, 0)),
		EIGHT(7, new Position(2765, 3851, 0), new Position(2762, 3854, 0)),
		NINE(8, new Position(2766, 3849, 0), new Position(2765, 3846, 0)),
		TEN(9, new Position(2758, 3848, 0), new Position(2758, 3861, 0)),
		ELEVEN(10, new Position(2781, 3843, 0), new Position(2787, 3843, 0)),
		TWELVE(11, new Position(2797, 3837, 0), new Position(2782, 3837, 0)),
		THIRTEEN(12, new Position(2787, 3844, 0), new Position(2779, 3847, 0)),
		FOURTEEN(13, new Position(2778, 3849, 0), new Position(2781, 3852, 0)),
		FIFTEEN(14, new Position(2783, 3854, 0), new Position(2789, 3846, 0)),
		SIXTEEN(15, new Position(2791, 3854, 0), new Position(2792, 3851, 0)),
		SEVENTEEN(16, new Position(2792, 3856, 0), new Position(2799, 3856, 0)),
		EIGHTEEN(17, new Position(2801, 3853, 0), new Position(2804, 3848, 0)),
		NINETEEN(18, new Position(2802, 3846, 0), new Position(2799, 3846, 0)),
		TWENTY(19, new Position(2805, 3848, 0), new Position(2808, 3852, 0)),
		TWENTY1(20, new Position(2808, 3857, 0), new Position(2803, 3855, 0)),
		TWENTY2(21, new Position(2793, 3840, 0), new Position(2797, 3843, 0)),
		TWENTY3(22, new Position(2791, 3845, 0), new Position(2795, 3845, 0)),
		TWENTY4(22, new Position(2805, 3844, 0), new Position(2807, 3838, 0)),
		TWENTY5(23, new Position(2782, 3870, 0), new Position(2782, 3871, 0)),
		TWENTY6(24, new Position(2784, 3870, 0), new Position(2784, 3871, 0));
		public int id;
		public Position startPos;
		public Position endPos;

		LightCreatureEnum(int id, Position startPos, Position endPos) {
			this.id = id;
			this.startPos = startPos;
			this.endPos = endPos;
		}

		public static LightCreatureEnum forIndex(int index) {
			for (LightCreatureEnum l : LightCreatureEnum.values()) {
				if (index == l.id) {
					return l;
				}
			}
			return null;
		}

		public Position getStartPosition() {
			return this.startPos;
		}

		public Position getEndPosition() {
			return this.endPos;
		}
	}

	public static void sendDelaySantaChat(final Player player, final String[] chat) {
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				b.stop();
			}

			@Override
			public void stop() {
				player.getDialogue().setLastNpcTalk(SANTA);
				player.getDialogue().sendNpcChat(chat, LAUGHING);
				player.getDialogue().endDialogue();
			}
		}, 3);
	}

	public static void spawnEncounterNpc(Npc npc, Position spawningPosition) {
		npc.setPosition(spawningPosition);
		npc.setSpawnPosition(spawningPosition);
		npc.setWalkType(Npc.WalkType.STAND);
		npc.setCurrentX(spawningPosition.getX());
		npc.setCurrentY(spawningPosition.getY());
		npc.setNeedsRespawn(false);
		World.register(npc);
	}

	public void startEncounterLogic(final Player player) {
		final Npc santa = new Npc(SANTA);
		final ArrayList<Npc> lightCreatures = new ArrayList<>();
		for (int i = 0; i < LightCreatureEnum.values().length; i++) {
			lightCreatures.add(new Npc(LIGHT_CREATURE));
		}
		final int heightLevel = player.getPosition().getZ();
		final int combat = player.getCombatLevel();
		final int spawnId = combat < 10 ? ICE_FIEND : combat < 20 ? ICE_FIEND : combat < 40 ? ICE_GIANT : combat < 70 ? ICE_SPIDER : combat < 110 ? ICE_WOLF : ICE_TROLL;
		spawnEncounterNpc(santa, new Position(2783, 3869, heightLevel));
		for (int i = 0; i < lightCreatures.size(); i++) {
			LightCreatureEnum l = LightCreatureEnum.forIndex(i);
			Npc n = lightCreatures.get(i);
			if (l != null) {
				spawnEncounterNpc(n, new Position(l.getStartPosition().getX(), l.getStartPosition().getY(), heightLevel));
			}
		}
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			boolean enemySpawned = false;

			@Override
			public void execute(CycleEventContainer b) {
				if (player == null || santa == null || !santa.isVisible() || santa.isDead()) {
					b.stop();
					return;
				} else if (!player.Area(2754, 2814, 3833, 3873) || !player.encounterRunning || player.getPosition().getZ() != heightLevel) {
					b.stop();
					return;
				} else {
					if (Misc.goodDistance(player.getPosition(), lightCreatures.get(2).getPosition(), 1) || Misc.goodDistance(player.getPosition(), lightCreatures.get(1).getPosition(), 1) || player.getPosition().equals(lightCreatures.get(0).getPosition())) {
						player.teleport(new Position(ENCOUNTER.getX(), ENCOUNTER.getY(), heightLevel));
						sendDelaySantaChat(player, new String[]{"HO HO! You thought you could stop me?!"});
					}
					if (Misc.random(20) == 1 && !enemySpawned && !Misc.goodDistance(player.getPosition(), santa.getPosition(), 4)) {
						enemySpawned = true;
						Npc spawn = new Npc(spawnId);
						spawn.setCombatDelay(3);
						NpcLoader.spawnNpc(player, spawn, true, false);
						player.getDialogue().setLastNpcTalk(SANTA);
						player.getDialogue().sendNpcChat("Let's see how you like this!", LAUGHING);
						player.getDialogue().endDialogue();
						CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
							@Override
							public void execute(CycleEventContainer b) {
								b.stop();
							}

							@Override
							public void stop() {
								enemySpawned = false;
							}
						}, 15);
					}
					for (int i = 0; i < lightCreatures.size(); i++) {
						LightCreatureEnum l = LightCreatureEnum.forIndex(i);
						Npc n = lightCreatures.get(i);
						if (l != null) {
							if (lightCreatures.get(i).getPosition().equals(new Position(l.getStartPosition().getX(), l.getStartPosition().getY(), heightLevel)) && n.getFrozenImmunity().completed()) {
								n.walkTo(new Position(l.getEndPosition().getX(), l.getEndPosition().getY(), heightLevel), true);
							}
							if ((lightCreatures.get(i).getPosition().equals(new Position(l.getEndPosition().getX(), l.getEndPosition().getY(), heightLevel)) && n.getFrozenImmunity().completed())) {
								n.walkTo(new Position(l.getStartPosition().getX(), l.getStartPosition().getY(), heightLevel), true);
							}
							if (!lightCreatures.get(i).getPosition().equals(new Position(l.getStartPosition().getX(), l.getStartPosition().getY(), heightLevel)) && !lightCreatures.get(i).getPosition().equals(new Position(l.getEndPosition().getX(), l.getEndPosition().getY(), heightLevel)) && n.getFrozenImmunity().completed() && !n.isMoving()) {
								n.walkTo(new Position(l.getStartPosition().getX(), l.getStartPosition().getY(), heightLevel), true);
							}
							if (i > 2 && Misc.goodDistance(player.getPosition(), n.getPosition(), 1)) {
								player.teleport(new Position(ENCOUNTER.getX(), ENCOUNTER.getY(), heightLevel));
								sendDelaySantaChat(player, new String[]{"Ho ho ho! Feel the power of my magic!"});
							}
						}
					}
				}
			}

			@Override
			public void stop() {
				if (player != null) {
					player.encounterRunning = false;
					if (!player.getPosition().equals(new Position(2755, 3649, 0)) && !Misc.goodDistance(player.getPosition(), santa.getPosition(), 1)) {
						player.teleport(SNOWY_JAIL);
					}
				}

				for (Npc npc : World.getNpcs()) {
					if (npc != null && npc.Area(2754, 2814, 3833, 3873) && npc.getPosition().getZ() == heightLevel) {
						NpcLoader.destroyNpc(npc);
					}
				}
			}
		}, 1);
	}

	public void startEncounter(final Player player) {
		player.fadeTeleport(new Position(ENCOUNTER.getX(), ENCOUNTER.getY(), player.getIndex() * 4));
		player.setStopPacket(true);
		new GameObject(5015, 2773, 3835, 0, 0, 10, 0, 999999, true);
		new GameObject(5015, 2772, 3835, 0, 0, 10, 0, 999999, true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				sendDelaySantaChat(player, new String[]{"HO HO! You think you can best me?", "Just try and reach me! Ho ho ho!"});
				b.stop();
			}

			@Override
			public void stop() {
				player.setStopPacket(false);
				player.encounterRunning = true;
				startEncounterLogic(player);
			}
		}, 5);

	}

	public void startSnowballTimer(final Player player) {
		if (!player.snowballsTimerRunning) {
			player.snowballsTimerRunning = true;
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer b) {
					b.stop();
				}

				@Override
				public void stop() {
					player.snowballsReady = true;
					player.snowballsTimerRunning = false;
				}
			}, 300);
		}
	}

	public boolean itemHandling(final Player player, int itemId) { //Inherited, will work without a call to it
		switch (itemId) {

		}
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) { //Inherited, will work without a call to it
		return false;
	}

	public boolean doItemOnObject(final Player player, int object, int item) { //Inherited, will work without a call to it
		switch (object) {

		}
		return false;
	}

	public boolean doObjectClicking(final Player player, int object, int x, int y) { //Inherited, will work without a call to it
		if (CHRISTMAS_ENABLED) {
			switch (object) {
				case SLOPE:
					player.fadeTeleport(new Position(2755, 3649, 0));
					player.getActionSender().sendMessage("You find yourself at the foot of the mountain after a rough trip down.");
					return true;
				case TRAPDOOR_OBJ:
					if (player.getQuestStage(34) >= TRAPDOOR) {
						GameObjectDef def = SkillHandler.getObject(object, x, y, 0);
						TrapDoor.handleTrapdoor(player, object, 6435, def);
						return true;
					} else {
						player.getDialogue().sendPlayerChat("I'm not quite sure I should go down", "this, it looks fairly scary.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					}

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

	public boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) { //Inherited
		DialogueManager d = player.getDialogue();
		if (!CHRISTMAS_ENABLED) {
			return false;
		}
		switch (id) { //Npc ID
			case WISE_OLD_MAN:
				switch (player.getQuestStage(34)) {
					case QUEST_COMPLETE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendPlayerChat("Can I have some more of those snowballs?", CONTENT);
								return true;
							case 2:
								if (player.snowballsReady) {
									d.sendNpcChat("You're in luck! I managed to make a few more.", "Here they are!", HAPPY);
								} else {
									d.sendNpcChat("I don't have any new snowballs ready for you,", "sorry. Come back a bit later.", CONTENT);
									d.endDialogue();
									startSnowballTimer(player);
								}
								return true;
							case 3:
								if (player.getInventory().canAddItem(new Item(SNOWBALL_ITEM))) {
									d.sendGiveItemNpc("The Wise Old Man hands you some snowballs.", new Item(SNOWBALL_ITEM, 10));
									d.setNextChatId(5);
									player.getInventory().addItem(new Item(SNOWBALL_ITEM, 6 + Misc.random(4)));
									return true;
								} else {
									d.sendNpcChat("Oh, you don't have enough space in your", "inventory. Talk to me again when you do.", SAD);
									d.endDialogue();
									return true;
								}
							case 5:
								d.sendPlayerChat("So, what exactly do they do again?", CONTENT);
								return true;
							case 6:
								d.sendNpcChat("Well, if you throw them at a monster or a player...", "They should freeze in place! However, my magic isn't", "so good these days, so the effect may fail.", CONTENT);
								return true;
							case 7:
								d.sendPlayerChat("Thank you! When can I get some more?", CONTENT);
								return true;
							case 8:
								d.sendNpcChat("Hmm, it usually takes me several minutes to collect", "snow that has fallen from the top and then enchant it.", "Come back in a little while.", CONTENT);
								d.endDialogue();
								return true;
						}
						return false;
					case CONFRONT_SANTA:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getInventory().playerHasItem(new Item(SNOWBALL_ITEM))) {
									d.sendNpcChat("Those are hand made by me! Some of the snow", "from above falls down here from time to time.", "I gather it up and use what little of my magic I have", "left to enchant them.", CONTENT);
									d.setNextChatId(5);
								} else {
									d.sendPlayerChat("Can I have some more of those snowballs?", CONTENT);
								}
								return true;
							case 2:
								if (player.snowballsReady) {
									d.sendNpcChat("You're in luck! I managed to make a few more.", "Here they are!", HAPPY);
								} else {
									d.sendNpcChat("I don't have any new snowballs ready for you,", "sorry. Come back a bit later.", CONTENT);
									d.endDialogue();
									startSnowballTimer(player);
								}
								return true;
							case 3:
								if (player.getInventory().canAddItem(new Item(SNOWBALL_ITEM))) {
									d.sendGiveItemNpc("The Wise Old Man hands you some snowballs.", new Item(SNOWBALL_ITEM, 10));
									d.setNextChatId(5);
									player.getInventory().addItem(new Item(SNOWBALL_ITEM, 6 + Misc.random(4)));
									return true;
								} else {
									d.sendNpcChat("Oh, you don't have enough space in your", "inventory. Talk to me again when you do.", SAD);
									d.endDialogue();
									return true;
								}
							case 5:
								d.sendPlayerChat("So, what exactly do they do?", CONTENT);
								return true;
							case 6:
								d.sendNpcChat("Well, if you throw them at a monster or a player...", "They should freeze in place! However, my magic isn't", "so good these days, so the effect may fail.", CONTENT);
								return true;
							case 7:
								d.sendPlayerChat("Excellent. This should help me in case things go", "sour with Santa Claus.", CONTENT);
								return true;
							case 8:
								d.sendNpcChat("I'm sure he won't take too kindly to", "a confrontation, be wary adventurer. Be prepared", "for anything.", CONTENT);
								return true;
							case 9:
								d.sendPlayerChat("I'll be careful. Thank you!", CONTENT);
								d.endDialogue();
								return true;
						}
						return false;
					case TRAPDOOR:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendPlayerChat("Wait, you're behind all of this?", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Not quite. Django forced me down here after", "I learned of Santa's plot. The slaves at the top", "of the tunnel prevent me from leaving.", SAD);
								return true;
							case 3:
								d.sendPlayerChat("Where's all the coal then? Isn't he", "storing it all down here?", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("Where did you ever hear that from? If only", "he were storing coal down here. It'd make", "for nice fuel to keep this fire going.", SAD);
								return true;
							case 5:
								d.sendPlayerChat("So, where is all the coal? What is Santa's", "plot?", CONTENT);
								return true;
							case 6:
								d.sendNpcChat("Well, I imagine all the coal is being stored", "nearby in the Draynor bank... As for the", "plot, I'll start at the beginning...", CONTENT);
								return true;
							case 7:
								d.sendNpcChat("I was making my rounds around Draynor one", "morning, per my usual routine. It was then when I", "heard Diango shouting at someone or something.", CONTENT);
								return true;
							case 8:
								d.sendNpcChat("I slowly got closer in order to hear what he was", "saying. He was yelling at one of those slaves", "over not working fast enough and the pressure he is", "under.", CONTENT);
								return true;
							case 9:
								d.sendNpcChat("He kept saying that the 'quota' had to be met,", "or it would be the end of him. Santa is", "most likely using and abusing him too, just like", "he is doing to all of us...", SAD);
								return true;
							case 10:
								d.sendPlayerChat("That's awful, I'm going to go confront Santa.", CONTENT);
								return true;
							case 11:
								d.sendNpcChat("Oh, be very careful if you do, Santa is a powerful", "elder. No one knows how old he is, or what age he", "comes from. I may have something for you, however.", CONTENT);
								return true;
							case 12:
								d.sendNpcChat("Ah! Yes! Of course!", "Here, take these enchanted snowballs.", HAPPY);
								return true;
							case 13:
								if (player.getInventory().canAddItem(new Item(SNOWBALL_ITEM))) {
									d.sendGiveItemNpc("The Wise Old Man hands you some snowballs.", new Item(SNOWBALL_ITEM, 20));
									d.setNextChatId(1);
									player.getInventory().addItem(new Item(SNOWBALL_ITEM, 20));
									player.setQuestStage(34, CONFRONT_SANTA);
									player.snowballsReady = true;
									return true;
								} else {
									d.sendNpcChat("Oh, you don't have enough space in your", "inventory. Talk to me again when you do.", SAD);
									d.endDialogue();
									return true;
								}
						}
						return false;
				}
				return false;
			case COAL_SLAVE:
				switch (player.getQuestStage(34)) { //Dialogue per stage
					case QUEST_COMPLETE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendNpcChat("Thank you so much for defeating Santa!", HAPPY);
								d.endDialogue();
								return true;
						}
						return false;
					default:
						d.sendNpcChat("Go away. Don't talk to me.", "You'll make me look bad.", ANGRY_1);
						d.endDialogue();
						return true;
					case TRAPDOOR:
						d.sendStatement("The slave wordlessly motions towards the trapdoor.");
						d.endDialogue();
						return true;
					case INVESTIGATE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendPlayerChat("What exactly are you here for?", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Go away. Don't talk to me.", "You'll make me look bad.", ANGRY_1);
								return true;
							case 3:
								d.sendPlayerChat("I don't care, Django can't see over here.", "I know about the coal.", "...I'm here to help.", CONTENT);
								return true;
							case 4:
								d.sendStatement("The slave wordlessly motions towards the trapdoor.");
								return true;
							case 5:
								d.sendPlayerChat("Thank you, I'll set this straight.", CONTENT);
								d.endDialogue();
								player.setQuestStage(34, TRAPDOOR);
								return true;
						}
						return false;
				}
			case 980:
			case 981:
			case 982:
			case 983:
			case 984:
			case 985: //slaves
				switch (player.getQuestStage(34)) { //Dialogue per stage
					case QUEST_COMPLETE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendNpcChat("Thank you so much for defeating Santa!", HAPPY);
								d.endDialogue();
								return true;
						}
						return false;
					default:
						d.sendStatement("The slave looks very busy.");
						d.endDialogue();
						return true;
					case INVESTIGATE:
					case TRAPDOOR:
					case CONFRONT_SANTA:
						d.sendNpcChat("Please help us... Santa is a tyrant...", SAD);
						d.endDialogue();
						return true;
					case SNOW_JAIL:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getPosition().getY() > 3700) {
									d.sendPlayerChat("Where am I?", CONTENT);
									d.setNextChatId(10);
								} else {
									d.sendPlayerChat("Excuse me... Who or what are you?", CONTENT);
								}
								return true;
							case 2:
								d.sendNpcChat("I'm really not sure anymore...", "I work long days and nights...", SAD);
								return true;
							case 3:
								d.sendPlayerChat("But why? Who is making you do this?", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("The big man...", SAD);
								return true;
							case 5:
								d.sendStatement("The slave looks visibly shaken and refuses to speak.");
								d.endDialogue();
								return true;
							case 10:
								d.sendNpcChat("I don't know...", "It's so cold... If only I had some of", "that coal to keep me warm now...", SAD);
								return true;
							case 11:
								d.sendPlayerChat("Wait, did you say coal?", CONTENT);
								return true;
							case 12:
								d.sendNpcChat("Yes, I mined so much of it.", SAD);
								return true;
							case 13:
								d.sendPlayerChat("For Santa?", CONTENT);
								return true;
							case 14:
								d.sendNpcChat("Maybe... there was a fat bloke with a big", "white beard...", SAD);
								return true;
							case 15:
								d.sendPlayerChat("Interesting. How did you get sent here?", CONTENT);
								return true;
							case 16:
								d.sendNpcChat("I guess I got too nosy... I started to", "question why we were mining coal...", SAD);
								return true;
							case 17:
								d.sendPlayerChat("Who? Who did you question?", CONTENT);
								return true;
							case 18:
								d.sendNpcChat("That funny looking man in Draynor Village...", "The one always playing with toy horses...", SAD);
								return true;
							case 19:
								d.sendPlayerChat("Well, this is getting quite strange.", "Why Draynor and Django?", CONTENT);
								return true;
							case 20:
								d.sendStatement("The slave shakes from the cold.");
								return true;
							case 21:
								d.sendNpcChat("The toy horse man said it was in order to", "'optimize efficiency'... Said the coal would be", "safe nearby...", SAD);
								return true;
							case 22:
								d.sendPlayerChat("Hm, well, alright. I'm just going to teleport", "out of here in that case, and get to the bottom", "of this.", CONTENT);
								return true;
							case 23:
								d.sendPlayerChat("I guess that old fool Santa isn't aware", "all adventurers can teleport merely at whim!", LAUGHING);
								d.endDialogue();
								player.setQuestStage(34, INVESTIGATE);
								return true;
						}
						return false;
					case GATHER_COAL:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendPlayerChat("Excuse me... Who or what are you?", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("I'm really not sure anymore...", "I work long days and nights...", SAD);
								return true;
							case 3:
								d.sendPlayerChat("But why? Who is making you do this?", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("The big man...", SAD);
								return true;
							case 5:
								d.sendStatement("The slave looks visibly shaken and refuses to speak.");
								d.endDialogue();
								return true;
						}
						return false;
				}
			case DJANGO:
				switch (player.getQuestStage(34)) { //Dialogue per stage
					case QUEST_COMPLETE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendNpcChat("Well, you defeated my business partner...", "I'll have to re-evaluate my motives for future profit.", CONTENT);
								d.endDialogue();
								return true;
						}
						return false;
					case INVESTIGATE:
					case TRAPDOOR:
					case SNOW_JAIL:
					case RETURN_TO_SANTA:
					case CONFRONT_SANTA:
						d.sendNpcChat("Can't you see I'm busy?!", "Go see Santa for any of your troubles!", ANGRY_1);
						d.endDialogue();
						return true;
					case GATHER_COAL:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getInventory().playerHasItem(new Item(453, 10))) {
									d.sendPlayerChat("I have that coal you asked for.", CONTENT);
								} else {
									d.sendNpcChat("Hurry, go get 10 lumps of coal!", CONTENT);
									d.endDialogue();
								}
								return true;
							case 2:
								d.sendNpcChat("Ah, excellent! I'll take these off your hands...", "You can return to Santa and he'll probably", "have a reward for you.", CONTENT);
								return true;
							case 3:
								d.sendGiveItemNpc("You hand Django the 10 lumps of coal.", new Item(453));
								d.endDialogue();
								player.getInventory().removeItem(new Item(453, 10));
								player.setQuestStage(34, RETURN_TO_SANTA);
								return true;
						}
						return false;
					case QUEST_STARTED:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendPlayerChat("Hello. I was sent by the big man.", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Who?", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("Santa told me to come see you in", "order to help him meet his 'quota'...", "...Or something like that.", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("Ah, right, right. The quota. You see,", "this year has found an astounding number of", "people on Santa's naughty list.", CONTENT);
								return true;
							case 5:
								d.sendNpcChat("Normally Santa's naughty list is fairly large,", "but this year it's grown beyond what I thought", "possible!", CONTENT);
								return true;
							case 6:
								d.sendPlayerChat("So, how does this affect me?", CONTENT);
								return true;
							case 7:
								d.sendNpcChat("Well, you see, it's only tradition to serve up", "a lump of coal to every naughty person on Santa's", "list. However...", CONTENT);
								return true;
							case 8:
								d.sendNpcChat("With the list being so large, we can't seem to", "gather enough coal!", CONTENT);
								return true;
							case 9:
								d.sendPlayerChat("Wait, you want me to mine some coal for you?", CONTENT);
								return true;
							case 10:
								d.sendNpcChat("Precisely.", CONTENT);
								return true;
							case 11:
								d.sendPlayerChat("What if I'm not skilled enough to mine coal?", CONTENT);
								return true;
							case 12:
								d.sendNpcChat("Well then you better find some way to fill", "the quota! 10 lumps of coal should do the trick. It's", "important we get them as quick as possible!", CONTENT);
								return true;
							case 13:
								d.sendPlayerChat("Alright, I'll see what I can do.", CONTENT);
								d.endDialogue();
								player.setQuestStage(34, GATHER_COAL);
								return true;
						}
						return false;
				}
				return false;
			case SANTA:
				switch (player.getQuestStage(34)) { //Dialogue per stage
					case QUEST_COMPLETE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendNpcChat("Hmrph. Go away.", "I have to plan for next year.", Dialogues.ANGRY_2);
								d.endDialogue();
								return true;
						}
						return false;
					case CONFRONT_SANTA:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.Area(2754, 2814, 3833, 3873)) {
									d.sendPlayerChat("Hah! Here I am! Not so mighty after all.", CONTENT);
									d.setNextChatId(5);
									return true;
								}
								d.sendPlayerChat("Santa! I know what you're up to!", "I demand you stop this, or I will stop you myself!", Dialogues.ANGRY_2);
								return true;
							case 2:
								d.sendNpcChat("HO HO! So be it. I'll end this outrage", "before you can even say the words", "'permanent naughty list'", ANGRY_1);
								return true;
							case 3:
								startEncounter(player);
								d.endDialogue();
								return true;
							case 5:
								d.sendNpcChat("Wha?! It's not possible!", ANGRY_1);
								return true;
							case 6:
								if (player.getInventory().playerHasItem(SNOWBALL_ITEM) || player.getEquipment().getId(Constants.WEAPON) == SNOWBALL_ITEM) {
									d.sendStatement("You pelt Santa with an enchanted Snowball.");
									d.setNextChatId(9);
									for (Npc n : World.getNpcs()) {
										if (n != null && n.getNpcId() == SANTA && Misc.goodDistance(player.getPosition(), n.getPosition(), 1)) {
											Snowball.throwSnowball(player, n);
										}
									}
									return true;
								} else {
									d.sendStatement("If only you had something to hurt Santa with...", "...perhaps an enchanted snowball would do the trick.");
									return true;
								}

							case 7:
								d.sendNpcChat("Nevermind! You fool! Away with you!", Dialogues.ANGRY_2);
								return true;
							case 8:
								player.fadeTeleport(new Position(ENCOUNTER.getX(), ENCOUNTER.getY(), player.getPosition().getZ()));
								sendDelaySantaChat(player, new String[]{"I won't let this happen again! Hmmrph!"});
								d.endDialogue();
								return true;
							case 9:
								d.sendNpcChat("AAARGH! NO!", Dialogues.ANGRY_2);
								for (Npc npc : World.getNpcs()) {
									if (npc != null && npc.Area(2754, 2814, 3833, 3873) && npc.getPosition().getZ() == player.getPosition().getZ()) {
										NpcLoader.destroyNpc(npc);
									}
								}
								return true;
							case 10:
								GroundItem drop = new GroundItem(new Item(1050), player, new Position(2783, 3869, player.getPosition().getZ()));
								GroundItemManager.getManager().dropItem(drop);
								d.sendPlayerChat("Phew, glad that is over. I suppose I should", "get out of here...", CONTENT);
								d.endDialogue();
								player.setQuestStage(34, QUEST_COMPLETE);
								return true;
						}
						return false;
					case INVESTIGATE:
					case TRAPDOOR:
					case SNOW_JAIL:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendNpcChat("What?! How did you escape?! Back you go!!", ANGRY_1);
								return true;
							case 2:
								player.fadeTeleport(SNOWY_JAIL);
								d.endDialogue();
								return true;
						}
						return false;
					case RETURN_TO_SANTA:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendPlayerChat("Hey Santa. I gathered some coal for you and", "gave it to Django.", HAPPY);
								return true;
							case 2:
								d.sendNpcChat("Ah, yes. Good work adventurer.", LAUGHING);
								return true;
							case 3:
								d.sendPlayerChat("Django said there may be a, erm...", "...reward involved?", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("Hm? Why should I reward you? You're on the", "naughty list! Ho ho!", LAUGHING);
								return true;
							case 5:
								d.sendPlayerChat("Wait, what did I do wrong? I'm not naughty...", "How do I get onto the 'nice' list?", SAD);
								return true;
							case 6:
								d.sendNpcChat("The nice list? HO HO HO!", "There is no nice list this year sonny.", LAUGHING);
								return true;
							case 7:
								d.sendPlayerChat("You monster! You're supposed to bring cheer", "and joy to everyone!", ANGRY_1);
								return true;
							case 8:
								d.sendNpcChat("How dare you call ol' Santa a monster!", "You need to learn your place.", Dialogues.ANGRY_2);
								return true;
							case 9:
								player.fadeTeleport(SNOWY_JAIL);
								d.endDialogue();
								player.setQuestStage(34, SNOW_JAIL);
								return true;
						}
						return false;
					case QUEST_STARTED:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendNpcChat("Hurry up and go see Django, I have", "a deadline to meet!", ANGRY_1);
								d.endDialogue();
								return true;
						}
						return false;
					case 0:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendPlayerChat("Hello!", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Ho ho ho! Hello there adventurer. How are you?", LAUGHING);
								return true;
							case 3:
								d.sendPlayerChat("I'm doing alright. Say... Do you, erm...", "Have anything for me?", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("Hm? Don't you mean...", "'What can I do to help dear ol' Santa?'", "Ho ho ho!", LAUGHING);
								return true;
							case 5:
								d.sendOption("Yes?", "Err, no. I want a present.");
								return true;
							case 6:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("Yes?", CONTENT);
										d.setNextChatId(10);
										return true;
									case 2:
										d.sendPlayerChat("Err, no. I want a present.", CONTENT);
										return true;
								}
							case 7:
								d.sendNpcChat("Ho ho! You're a fiesty one. How about this...", "I give you a big fat lump of coal for", "being on my naughty list!", LAUGHING);
								return true;
							case 8:
								d.sendPlayerChat("Santa, what has gotten into you? You're", "supposed to bring holiday cheer and free items!", SAD);
								return true;
							case 9:
								d.sendNpcChat("Oh? Free items? Where are my free items?", "Hmmm?? Run on off to somewhere else,", "I don't have the time to deal with you ungrateful", "types.", ANGRY_1);
								d.endDialogue();
								return true;
							case 10:
								d.sendNpcChat("Most excellent! I knew you wanted to help ol'", "Santa. Here's what I need you to do...", CONTENT);
								return true;
							case 11:
								d.sendNpcChat("Go meet with my business partner, Django in Draynor.", "He'll fill you in on the details of the quota", "I need to meet this year, and how you can help.", CONTENT);
								return true;
							case 12:
								d.sendNpcChat("And don't forget to be jolly about it! Ho ho ho!", LAUGHING);
								d.endDialogue();
								QuestHandler.startQuest(player, 34);
								return true;
						}
						return false;
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
		if (itemId == SNOWBALL_ITEM) {
			Snowball.throwSnowball(player, npc);
			player.getMovementHandler().reset();
			return true;
		}
		return false;
	}
}
