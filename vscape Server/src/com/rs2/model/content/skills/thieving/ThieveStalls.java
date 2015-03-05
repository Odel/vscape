package com.rs2.model.content.skills.thieving;

import java.util.Random;

import com.rs2.Constants;
import com.rs2.model.World;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.npcs.Npc;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class ThieveStalls {

	public enum Stall {
		VEGETABLE_STALL(new int[] { 4706, 4708 }, new Item[] { new Item(1956), new Item(1965), new Item(1942), new Item(1982), new Item(1550) }, 2, 10, 3),
		BREAD_STALL(new int[] { 2561, 6163, 630, 6569 }, new Item[] { new Item(2309), new Item(1891), new Item(1893), new Item(1895), new Item(1897), new Item(1899), new Item(1901) }, 5, 16, 6),
		CRAFTING_STALL(new int[] { 4874, 6166 }, new Item[] { new Item(1755), new Item(1621), new Item(1592), new Item(1597) }, 5, 10, 11),
		BANANA_STALL(new int[] { 4875 }, new Item[] { new Item(1963) }, 5, 16, 11),
		GENERAL_STALL(new int[] { 4876 }, new Item[] { new Item(1931), new Item(2347), new Item(590) }, 5, 10, 11),
		TEA_STALL(new int[] { 635, 6574 }, new Item[] { new Item(1978) }, 5, 16, 8),
		ROCK_CAKE_STALL(new int[] { 2793 }, new Item[] { new Item(2379) }, 15, 10, 10),
		SILK_STALL(new int[] { 2560, 6568, 629 }, new Item[] { new Item(950) }, 20, 24, 10),
		WINE_STALL(new int[] { 14011 }, new Item[] { new Item(1937), new Item(1993), new Item(1987), new Item(1935), new Item(7919)}, 22, 27, 23),
		SEED_STALL(new int[] { 7053 }, new Item[] { new Item(5291), new Item(5292), new Item(5293), new Item(5294), new Item(5295), new Item(5296), new Item(5297), new Item(5298), new Item(5299), new Item(5300), new Item(5301), new Item(5302), new Item(5303), new Item(5304), new Item(5318), new Item(5319), new Item(5320), new Item(5321), new Item(5322), new Item(5323), new Item(5324), new Item(5096), new Item(5097), new Item(5098), new Item(5099), new Item(5100), new Item(5101), new Item(5102), new Item(5103), new Item(5104), new Item(5105), new Item(5106), new Item(5305), new Item(5306), new Item(5307), new Item(5308), new Item(5309), new Item(5310), new Item(5311) }, 27, 10, 18),
		FUR_STALL(new int[] { 2563, 4278, 632, 6571 }, new Item[] { new Item(958), new Item(948) }, 35, 36, 20),
		FISH_STALL(new int[] { 4277, 4705, 4707 }, new Item[] { new Item(331), new Item(359), new Item(377) }, 42, 42, 16),
		ORE_STALL(new int[] { 6164, 2565, 628 },  new Item[] { new Item(442) }, 50, 54, 50),
		SPICE_STALL(new int[] { 633, 2564, 6572 }, new Item[] { new Item(2007) }, 65, 82, 133),
		RUNE_STALL(new int[] { 4877 }, new Item[] { new Item(557), new Item(556), new Item(554), new Item(555), new Item(563)}, 65, 100, 133),
		SCIMITAR_STALL(new int[] { 4878 }, new Item[] { new Item(1325)}, 65, 160, 133),
		GEM_STALL(new int[] { 6162, 631, 2562, 6570 }, new Item[] { new Item(1617), new Item(1619), new Item(1621), new Item(1623) }, 75, 160, 300);

		private int[] id;
		private Item[] loot;
		private int level;
		private int xp;
		private int respawn;

		private Stall(int[] id, Item[] loot, int level, int xp, int respawn) {
			this.id = id;
			this.loot = loot;
			this.level = level;
			this.xp = xp;
			this.respawn = respawn;
		}

		public int[] getId() {
			return id;
		}

		public Item[] getLoot() {
			return loot;
		}

		public int getLevel() {
			return level;
		}

		public int getXp() {
			return xp;
		}

		public int getRespawnTime() {
			return respawn;
		}
	}

	public static Stall getStall(final int i) {
		for (Stall stall : Stall.values()) {
			for (int id : stall.getId()) {
				if (i == id) {
					return stall;
				}
			}
		}
		return null;
	}

	public static int emptyId(final int objectId) {
	    return objectId >= 4874 && objectId <= 4878 ? 4797 : objectId == 6163 ? 6573 : 634; //objectId > 6000 ? 6569
	}

	private static final Random r = new Random();

	public static boolean handleThievingStall(final Player player, final int objectId, final int objectX, final int objectY) {
		final Stall stall = getStall(objectId);
		if (stall == null) {
			return false;
		}
		if (player.getInventory().getItemContainer().freeSlots() < 1 && !(objectId == 7053)) {
			player.getActionSender().sendMessage("Looks like you don't have enough room in your inventory.");
			return true;
		}
		if (!Constants.THIEVING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (ObjectHandler.getInstance().getObject(objectX, objectY, player.getPosition().getZ()) != null) {
			player.getActionSender().sendMessage("Too late, the items are gone.");
			return true;
		}
		if (!SkillHandler.hasRequiredLevel(player, Skill.THIEVING, stall.getLevel(), "steal from this stall")) {
			return true;
		}
		final Item loot = stall.getLoot()[r.nextInt(stall.getLoot().length)];
		if (!player.getInventory().canAddItem(loot)) {
			return true;
		}
		player.getActionSender().sendMessage("You attempt to steal from the stall..");
		for (final Npc n : World.getNpcs()) {
			if (n == null)
				continue;
			if (!n.isDead() && n.getMaxHp() > 0 && !n.isAttacking() && Misc.goodDistance(n.getPosition().getX(), n.getPosition().getY(), player.getPosition().getX(), player.getPosition().getY(), 4)) {
				String NpcName = n.getDefinition().getName().toLowerCase();
				if(NpcName.contains("guard") || NpcName.contains("knight") || NpcName.contains("paladin")){
					n.getUpdateFlags().sendForceMessage("Hey! Get away from there!");
					if (n.getDefinition().isAttackable()) {
						CombatManager.attack(n, player);
					}
					return true;
				}
			}
		}
		final int task = player.getTask();
		player.setStopPacket(false);
		player.setHideWeapons(true);
		player.getUpdateFlags().sendAnimation(832);
		player.setSkilling(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task)) {
					container.stop();
					return;
				}
				if (!SkillHandler.checkObject(objectId, objectX, objectY, player.getPosition().getZ())) {
					player.getActionSender().sendMessage("Too late, the items are gone.");
					return;
				}
				player.getInventory().addItem(loot);
				player.getActionSender().sendMessage("You successfully stole a " + loot.getDefinition().getName().toLowerCase() + ".");
				player.getSkill().addExp(Skill.THIEVING, stall.getXp());
				int face = SkillHandler.getFace(objectId, objectX, objectY, player.getPosition().getZ());
				new GameObject(emptyId(objectId), objectX, objectY, player.getPosition().getZ(), face, 10, objectId, stall.getRespawnTime());
				if (player.getRandomHandler().getPillory().doJailEvent()) {
					player.getRandomHandler().getPillory().JailPlayer();
				}
				container.stop();
			}

			@Override
			public void stop() {
				player.setHideWeapons(false);
				player.setStopPacket(false);
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 2);
		return true;
	}

}
