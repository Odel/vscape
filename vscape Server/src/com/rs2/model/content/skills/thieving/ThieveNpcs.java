package com.rs2.model.content.skills.thieving;

import com.rs2.Constants;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.content.treasuretrails.ClueScroll;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class ThieveNpcs {

	private static final int THIEVING_ANIMATION = 881;
	private static final int BIRDS_WHEN_STUNNED = 254; //80

	public enum ThieveNpc {
		CITIZEN(new String[] { "man", "woman" }, 1, 8, new Item[] { new Item(995, 3) }, 5, 1),
		FARMER(new String[] {"farmer"}, 10, 14.5, new Item[] { new Item(995, 9), new Item(5318, 1) }, 5, 1),
                HAM(new String[] {"h.a.m. member"}, 15, 18, new Item[] { new Item(995, 12), new Item(882, 15), new Item(1349), new Item(688), new Item(1203), new Item(314, 8)}, new Item[] {new Item(4298), new Item(4300), new Item(4302), new Item(4304), new Item(4306), new Item(4308), new Item(4310) }, 6, 1),
		WARRIOR(new String[] { "warrior woman", "al-kharid warrior" }, 25, 26, new Item[] { new Item(995, 18) }, 5, 2),
		ROGUE(new String[] { "rogue" }, 32, 36.5, new Item[] { new Item(995, 25), new Item(995, 40), new Item(7919, 1), new Item(556, 6), new Item(5686, 1), new Item(1523, 1), new Item(1944, 1) }, 5, 2),
		MASTER_FARMER(new String[] { "master farmer", "master gardener" }, 38, 43, new Item[] { new Item(5318), new Item(5319), new Item(5324), new Item(5322), new Item(5320), new Item(5323), new Item(5305), new Item(5307),
				new Item(5308), new Item(5306), new Item(5309), new Item(5310), new Item(5104), new Item(5105), new Item(5106), new Item(5096), new Item(5097), new Item(5098), new Item(5099), new Item(5100),
				new Item(5291), new Item(5292), new Item(5293), new Item(5294)}, new Item[]{new Item(5321), new Item(5311), new Item(5101), new Item(5102), new Item(5103), new Item(5295), new Item(5296),
				new Item(5297), new Item(5298), new Item(5299), new Item(5300), new Item(5301), new Item(5302), new Item(5303), new Item(5304), new Item(5280), new Item(5281)}, 5, 3),
		GUARD(new String[] { "guard" }, 40, 46.5, new Item[] { new Item(995, 30) }, 5, 2),
		FREMENNIK_CITIZIN(new String[] { "inga", "freidir", "jennella", "sassilik", "agnar", "lanzig", "pontak", "lensa" }, 45, 65, new Item[] { new Item(995, 40) }, 5, 2),
		BEARDED_POLLNIVIAN_BANDIT(new String[] { "bearded pollnivnian bandit" }, 45, 65, new Item[] { new Item(995, 40) }, 4, 5),
		DESERT_BANDIT(new String[] { "bandit" }, 53, 79.5, new Item[] { new Item(995, 30), new Item(2446), new Item(1523) }, 5, 3),
		KNIGHT(new String[] { "knight of ardougne" }, 55, 84.3, new Item[] { new Item(995, 50) }, 5, 3),
		POLLNIVIAN_BANDIT(new String[] { "pollnivian bandit" }, 55, 84.3, new Item[] { new Item(995, 30) }, 5, 5),
		WATCHMAN(new String[] { "watchman" }, 65, 137.5, new Item[] { new Item(995, 60), new Item(2309)}, 5, 3),
		MENAPHITE_THUG(new String[] { "menaphite thug" }, 65, 137.5, new Item[] { new Item(995, 60) }, 5, 5),
		PALADIN(new String[] { "paladin" }, 70, 151.75, new Item[] { new Item(995, 80), new Item(562, 2) }, 5, 3),
		GNOME(new String[] { "gnome", "gnome woman", "gnome child" }, 75, 198.5, new Item[] { new Item(995, 300), new Item(557), new Item(444), new Item(569), new Item(2150), new Item(2162) }, 5, 1),
		HERO(new String[] { "hero" }, 80, 273.3, new Item[] { new Item(995, 300), new Item(560, 2), new Item(565), new Item(569), new Item(1617), new Item(444), new Item(1993) }, 6, 4),
		ELF(new String[] { "elf" }, 85, 353, new Item[] { new Item(995, 300), new Item(569), new Item(444), new Item(1601), new Item(560, 2), new Item(1993), new Item(237), new Item(581), new Item(561, 3) }, 6, 5);

		private String[] npcId;
		private int levelReq;
		private double experience;
		private Item[] loot;
		private Item[] rareLoot;
		private int stunTime;
		private int stunDamage;

		private ThieveNpc(String[] npcId, int levelReq, double experience, Item[] loot, int stunTime, int stunDamage) {
			this.npcId = npcId;
			this.levelReq = levelReq;
			this.experience = experience;
			this.loot = loot;
			this.stunTime = stunTime;
			this.stunDamage = stunDamage;
		}

		private ThieveNpc(String[] npcId, int levelReq, double experience, Item[] loot, Item[] rareLoot, int stunTime, int stunDamage) {
			this.npcId = npcId;
			this.levelReq = levelReq;
			this.experience = experience;
			this.loot = loot;
			this.rareLoot = rareLoot;
			this.stunTime = stunTime;
			this.stunDamage = stunDamage;
		}

		public String[] getNpcName() {
			return npcId;
		}

		public int getLevelReq() {
			return levelReq;
		}

		public double getExperience() {
			return experience;
		}

		public Item[] getLoot() {
			return loot;
		}

		public Item[] getRareLoot() {
			return rareLoot;
		}

		public int getStunTime() {
			return stunTime;
		}

		public int getStunDamage() {
			return stunDamage;
		}
	}

	public static ThieveNpc getNpc(final String npcName) {
		for (ThieveNpc npc : ThieveNpc.values()) {
			for (String name : npc.getNpcName()) {
				if (npcName.equalsIgnoreCase(name)) {
					return npc;
				}

                         }
		}
		return null;
	}
                    

	public static boolean handleThieveNpc(final Player player, final Npc npc) {
		if (player == null || player.isStunned() || !player.getSkill().canDoAction(2200)) {
			return true;
		}
                
		final String npcName = npc.getDefinition().getName().toLowerCase();
		final ThieveNpc thieveNpc = getNpc(npcName.toLowerCase());
                
		if (thieveNpc == null) {
			return false;
		}
		if (!Constants.THIEVING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (!SkillHandler.hasRequiredLevel(player, Skill.THIEVING, thieveNpc.getLevelReq(), "pickpocket this npc")) {
			return true;
		}
		final boolean successful = Misc.random(player.getSkill().getLevel()[Skill.THIEVING]) > Misc.random(thieveNpc.getLevelReq());
		final Item randomLoot = thieveNpc.getRareLoot() != null && Misc.random(1) == 0 ? thieveNpc.getRareLoot()[Misc.randomMinusOne(thieveNpc.getRareLoot().length)] : thieveNpc.getLoot()[Misc.randomMinusOne(thieveNpc.getLoot().length)];
		final Item loot = new Item(randomLoot.getId(), randomLoot.getCount() > 1 ? Misc.random(randomLoot.getCount() + 1) : 1);
                final Item specialLoot = new Item(randomLoot.getId(), randomLoot.getCount());
		final int stunnedHit = thieveNpc.getStunDamage();
		player.setStopPacket(true);
		player.getUpdateFlags().sendAnimation(THIEVING_ANIMATION);
		player.getActionSender().sendMessage("You attempt to pick the " + npcName + "'s pocket.");
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (successful) {
					player.getActionSender().sendMessage("You manage to pick the " + npc.getDefinition().getName().toLowerCase() + "'s pocket.");
					if(specialLoot.getId() == 995 && !ClueScroll.hasClue(player) && thieveNpc.getNpcName()[0].equals("h.a.m. member") && Misc.random(3) == 0) {
					    player.getInventory().addItemOrDrop(new Item(ClueScroll.getRandomClue(1)));
					    player.getActionSender().sendMessage("You steal a clue scroll!");
					}
					else if(specialLoot.getId() == 995) {
                                            player.getInventory().addItem(new Item(995, specialLoot.getCount()));
					    player.getActionSender().sendMessage("You steal some " + ItemDefinition.forId(specialLoot.getId()).getName().toLowerCase() + ".");
					}
					else {
					    int count = loot.getCount();
					    if(count == 0) {
						count = 1;
					    }
                                            player.getInventory().addItem(new Item(loot.getId(), count * multiple(player, thieveNpc.getLevelReq())));
					    player.getActionSender().sendMessage("You steal some " + ItemDefinition.forId(loot.getId()).getName().toLowerCase() + ".");
					}
					player.getSkill().addExp(Skill.THIEVING, thieveNpc.getExperience());
				} else {
					npc.getUpdateFlags().sendForceMessage("What do you think you're doing?");
					npc.getUpdateFlags().sendAnimation(401);
					npc.setInteractingEntity(player);
					player.getUpdateFlags().sendAnimation(player.getBlockAnimation());
					player.getActionSender().sendMessage("You fail to pick the " + npcName + "'s pocket.");
					player.getUpdateFlags().sendGraphic(BIRDS_WHEN_STUNNED, 100 << 16);
					player.hit(stunnedHit < 1 ? 1 : stunnedHit, HitType.NORMAL);
					player.getStunTimer().setWaitDuration(thieveNpc.getStunTime());
					player.getStunTimer().reset();
				}
				if (player.getRandomHandler().getPillory().doJailEvent()) {
					player.getRandomHandler().getPillory().JailPlayer();
				}
				container.stop();
			}

			@Override
			public void stop() {
				player.setStopPacket(false);
			}
		}, 3);
		return true;
	}

	public static int multiple(Player player, int reqLevel) {
		/**
		 * The chance of a x4 loot is 1/25
		 */
		if (Misc.random(25) == 0) {
			if (player.getSkill().getPlayerLevel(Skill.THIEVING) >= reqLevel + 30 && player.getSkill().getPlayerLevel(Skill.AGILITY) >= reqLevel + 20) {
				player.getActionSender().sendMessage("You recieve a quadruple loot!");
				return 4;
			}
		}
		/**
		 * The chance of a 3x loot is 1/20
		 */
		if (Misc.random(20) == 0) {
			if (player.getSkill().getPlayerLevel(Skill.THIEVING) >= reqLevel + 20 && player.getSkill().getPlayerLevel(Skill.AGILITY) >= reqLevel + 10) {
				player.getActionSender().sendMessage("You recieve a triple loot!");
				return 3;
			}
		}
		/**
		 * The chance of a 2x loot is 1/15
		 */
		if (Misc.random(15) == 0) {
			if (player.getSkill().getPlayerLevel(Skill.THIEVING) >= reqLevel + 10 && player.getSkill().getPlayerLevel(Skill.AGILITY) >= reqLevel) {
				player.getActionSender().sendMessage("You recieve a double loot!");
				return 2;
			}
		}
		/**
		 * We cannot multiply by 0 because anything that is, results 0
		 */
		return 1;
	}

}
