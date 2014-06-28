package com.rs2.model.content.randomevents;

import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class TalkToEvent {

	public static final int[][] HERB_REWARD = { { 249, 113 }, { 251, 2446 }, { 253, 2428 }, { 255, 2430 }, { 257, 3008 }, { 2998, 2432 }, { 259, 3032 }, { 261, 2440 }, { 263, 3016 }, { 3000, 2440 }, { 265, 3024 }, { 2481, 2442 }, { 267, 3040 } };

	public static Item randomHerb() {
		return new Item(HERB_REWARD[Misc.randomMinusOne(HERB_REWARD.length)][0]);
	}

	public static Item rewardForHerb(int herb) {
		if (herb == 1) {
			return new Item(117);
		}
		for (int[] i : HERB_REWARD) {
			if (i[0] == herb) {
				return new Item(i[1]);
			}
		}
		return null;
	}

	public enum TalkToNpc {
		DRUNKEN_DWARF(956, 2429, new String[] { "'Ere, matey, 'ave some 'o the good stuff.", "Dun ignore your matey!", "I hates you 1!", "Aww comeon, talk to ikle me 1!" }), // 2429
		GENIE(409, 0, new String[] { "Greetings 1!", "I need to talk to you 1.", "1 please speak with me.", "1 I'm in a hurry, please talk to me!" }),
		//RICK(2476, 2476, new String[] { "Hello 1!", "I need to talk to you 1.", "1 please speak with me.", "1 I'm in a hurry, please talk to me!" }),
		JEKYLL(2540, 2541, new String[] { "Hey 1!", "I need to talk to you 1.", "1 please speak with me.", "1 I'm in a hurry, please talk to me!" });

		int npcId, transformId;
		String[] chat;

		private TalkToNpc(int npcId, int transformId, String[] chat) {
			this.npcId = npcId;
			this.transformId = transformId;
			this.chat = chat;
		}

		public int getId() {
			return npcId;
		}

		public int getTransformId() {
			return transformId;
		}

		public String[] getChat() {
			return chat;
		}
	}

	public static boolean isTalkToNpc(int id) {
		for (TalkToNpc npc : TalkToNpc.values()) {
			if (npc.getTransformId() == id) {
				return true;
			}
		}
		return false;
	}

	public static void spawnNpc(Player player, TalkToNpc n) {
		if (player.getSpawnedNpc() != null)
			return;
		Npc npc = new Npc(n.getId());
		player.setRandomEventNpc(npc);
		NpcLoader.spawnNpc(player, npc, false, false);
		player.getActionSender().sendStillGraphic(EventsConstants.RANDOM_EVENT_GRAPHIC, npc.getPosition(), 100 << 16);
		cycleEvent(npc, player, n.getChat(), n.getTransformId());
	}

	public static void cycleEvent(final Npc npc, final Player player, final String[] chat, final int transformId) {
		CycleEventHandler.getInstance().addEvent(npc, new CycleEvent() {
			int cycle = 120;
			int destructCycle = -1;
			String name = Misc.formatPlayerName(player.getUsername());
			@Override
			public void execute(CycleEventContainer container) {
				if (npc.getInteractingEntity() != null && npc.getInteractingEntity() == npc.getPlayerOwner() && destructCycle < 0) {
					destructCycle = 240;
				}
				if (destructCycle > 0) {
					destructCycle--;
					return;
				}
				if (cycle > 0 && destructCycle < 0) {
					switch (cycle) {
					case 120:
						npc.getUpdateFlags().sendForceMessage(chat[0].replaceAll("1", name));
						break;
					case 90:
						npc.getUpdateFlags().sendForceMessage(chat[1].replaceAll("1", name));
						break;
					case 60:
						npc.getUpdateFlags().sendForceMessage(chat[2].replaceAll("1", name));
						break;
					case 30:
						npc.getUpdateFlags().sendForceMessage(chat[3].replaceAll("1", name));
						break;
					case 2 :
						if (npc.getNpcId() == 409) {
							npc.getUpdateFlags().sendAnimation(EventsConstants.GOOD_BYE_EMOTE);
						}
						break;
					}
					cycle--;
				} else {
					NpcLoader.destroyNpc(npc);
					if (npc.getPlayerOwner() != null && destructCycle != 0) {
						if (transformId == 2541) {
							player.getPjTimer().setWaitDuration(0);
							player.getPjTimer().reset();
							NpcLoader.spawnNpc(player, new Npc(transformId + SpawnEvent.addValue(player.getCombatLevel())), true, false);
						} else if (transformId > 0) {
							player.getPjTimer().setWaitDuration(0);
							player.getPjTimer().reset();
							NpcLoader.spawnNpc(player, new Npc(transformId), true, false);
						} else {
							player.getActionSender().sendStillGraphic(EventsConstants.RANDOM_EVENT_GRAPHIC, npc.getPosition(), 100 << 16);
						}
					}
					container.stop();
					return;
				}
			}
			@Override
			public void stop() {
			}
		}, 1);
	}

	public static boolean isGenieLampButton(Player player, int actionButtonId) {
		switch (actionButtonId) {
		case 10252:
			player.getActionSender().sendConfig(261, 1);
			player.setGenieSelect(Skill.ATTACK);
			return true;
		case 10253:
			player.getActionSender().sendConfig(261, 2);
			player.setGenieSelect(Skill.STRENGTH);
			return true;
		case 10254:
			player.getActionSender().sendConfig(261, 3);
			player.setGenieSelect(Skill.RANGED);
			return true;
		case 10255:
			player.getActionSender().sendConfig(261, 4);
			player.setGenieSelect(Skill.MAGIC);
			return true;
		case 11000:
			player.getActionSender().sendConfig(261, 5);
			player.setGenieSelect(Skill.DEFENCE);
			return true;
		case 11001:
			player.getActionSender().sendConfig(261, 6);
			player.setGenieSelect(Skill.HITPOINTS);
			return true;
		case 11002:
			player.getActionSender().sendConfig(261, 7);
			player.setGenieSelect(Skill.PRAYER);
			return true;
		case 11003:
			player.getActionSender().sendConfig(261, 8);
			player.setGenieSelect(Skill.AGILITY);
			return true;
		case 11004:
			player.getActionSender().sendConfig(261, 9);
			player.setGenieSelect(Skill.HERBLORE);
			return true;
		case 11005:
			player.getActionSender().sendConfig(261, 10);
			player.setGenieSelect(Skill.THIEVING);
			return true;
		case 11006:
			player.getActionSender().sendConfig(261, 11);
			player.setGenieSelect(Skill.CRAFTING);
			return true;
		case 11007:
			player.getActionSender().sendConfig(261, 12);
			player.setGenieSelect(Skill.RUNECRAFTING);
			return true;
		case 11008:
			player.getActionSender().sendConfig(261, 13);
			player.setGenieSelect(Skill.MINING);
			return true;
		case 11009:
			player.getActionSender().sendConfig(261, 14);
			player.setGenieSelect(Skill.SMITHING);
			return true;
		case 11010:
			player.getActionSender().sendConfig(261, 15);
			player.setGenieSelect(Skill.FISHING);
			return true;
		case 11011:
			player.getActionSender().sendConfig(261, 16);
			player.setGenieSelect(Skill.COOKING);
			return true;
		case 11012:
			player.getActionSender().sendConfig(261, 17);
			player.setGenieSelect(Skill.FIREMAKING);
			return true;
		case 11013:
			player.getActionSender().sendConfig(261, 18);
			player.setGenieSelect(Skill.WOODCUTTING);
			return true;
		case 11014:
			player.getActionSender().sendConfig(261, 19);
			player.setGenieSelect(Skill.FLETCHING);
			return true;
		case 47002:
			player.getActionSender().sendConfig(261, 20);
			player.setGenieSelect(Skill.SLAYER);
			return true;
		case 54090:
			player.getActionSender().sendConfig(261, 21);
			player.setGenieSelect(Skill.FARMING);
			return true;
		case 11015:
			player.getActionSender().removeInterfaces();
			if (player.getGenieSelect() == -1) {
				player.getActionSender().sendMessage("You need to select a skill that you wish to level.");
			} else if (player.getInventory().removeItem(new Item(2528))) {
				int xp = player.getSkill().getPlayerLevel(player.getGenieSelect()) * 10;
				player.getSkill().addExp(player.getGenieSelect(), xp);
				player.getActionSender().sendMessage("Congratulations, " + xp * 2.25 + " xp was added to your " + Skill.SKILL_NAME[player.getGenieSelect()] + " skill.");
				player.setGenieSelect(-1);
				player.getActionSender().sendConfig(261, 0);
			}
			return true;
		}
		return false;
	}
}
