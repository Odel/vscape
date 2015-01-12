package com.rs2.model.content.skills.prayer;

import com.rs2.Constants;
import com.rs2.model.content.randomevents.SpawnEvent;
import com.rs2.model.content.randomevents.SpawnEvent.RandomNpc;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemManager;
import com.rs2.util.Misc;

/**
 * By Mikey` of Rune-Server
 */
public class BoneBurying {

	private Player player;

	public BoneBurying(Player player) {
		this.player = player;
	}

	public enum Bone {
		BONES(new int[]{526, 2530}, 4.5),
		BURNT_BONES(new int[]{528}, 6),
		WOLF_BONES(new int[]{2859}, 7),
		MONKEY_BONES(new int[]{3183, 3185, 3186}, 5),
		MEDIUM_MONKEY_BONES(new int[]{3179}, 16),
		LARGE_MONKEY_BONES(new int[]{3180, 3181, 3182}, 18),
		BAT_BONES(new int[]{530}, 5.3),
		BIG_BONES(new int[]{532}, 15),
		SHAIKAHAN_BONES(new int[]{3123}, 25),
		BABYDRAGON_BONES(new int[]{534}, 30),
		DRAGON_BONES(new int[]{536}, 72),
		WYVERN_BONES(new int[]{6812}, 72),
		JOGRE_BONES(new int[]{3125, 3127}, 22.5),
		ZOGRE_BONES(new int[]{4812}, 22.5),
		OURG_BONES(new int[]{4834}, 50),
		FAYRG_BONES(new int[]{4830}, 84),
		RAURG_BONES(new int[]{4832}, 96),
		DAGANNOTH_BONES(new int[]{6729}, 125);
		
		
		int[] boneIds;
		double xp;
	
		private Bone(int[] boneIds, double xp) {
			this.boneIds = boneIds;
			this.xp = xp;
		}
		
		public int[] getBoneIds() {
			return boneIds;
		}

		public double getXp() {
			return xp;
		}
	}

	public static Bone getBone(int itemId) {
		for (Bone bone : Bone.values()) {
			for (int item : bone.getBoneIds()) {
				if (item == itemId) {
					return bone;
				}
			}
		}
		return null;
	}

	public boolean buryBone(int itemId, int slot) {
		Bone bone = getBone(itemId);
		if (bone == null) {
			return false;
		}
		if (!Constants.PRAYER_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (!player.getSkill().canDoAction(800)) {
			return true;
		}
		if (player.getInventory().removeItemSlot(new Item(itemId), slot)) {
			player.getTask();
			player.getSkill().addExp(5, bone.getXp());
			player.getUpdateFlags().sendAnimation(827);
			player.getActionSender().sendMessage("You bury the " + ItemManager.getInstance().getItemName(itemId).toLowerCase() + ".");
			if (SkillHandler.doSpawnEvent(player)) {
				SpawnEvent.spawnNpc(player, Misc.random(3) == 0 ? RandomNpc.SHADE : RandomNpc.ZOMBIE);
			}
		}
		return true;
	}

}
