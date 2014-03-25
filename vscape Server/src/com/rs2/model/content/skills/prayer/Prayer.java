package com.rs2.model.content.skills.prayer;

import com.rs2.model.Entity;
import com.rs2.model.World;
import com.rs2.model.content.combat.CombatCycleEvent;
import com.rs2.model.content.combat.CombatCycleEvent.CanAttackResponse;
import com.rs2.model.content.combat.hit.Hit;
import com.rs2.model.content.combat.hit.HitDef;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.minigames.duelarena.RulesData;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;

public class Prayer {

	private Player player;

	public Prayer(Player player) {
		this.player = player;
	}

	public int[] prayerTimers = new int[18];

	public static final int THICK_SKIN = 0, BURST_OF_STRENGTH = 1, CLARITY_OF_THOUGHT = 2, ROCK_SKIN = 3, SUPERHUMAN_STRENGTH = 4, IMPROVED_REFLEXES = 5, RAPID_RESTORE = 6, RAPID_HEAL = 7, PROTECT_ITEM = 8, STEEL_SKIN = 9, ULTIMATE_STRENGTH = 10, INCREDIBLE_REFLEXES = 11, PROTECT_FROM_MAGIC = 12, PROTECT_FROM_RANGED = 13, PROTECT_FROM_MELEE = 14, RETRIBUTION = 15, REDEMPTION = 16, SMITE = 17;

	private final static Object[][] PRAYER_DATA = {
			// id, configId, name, levelRequired, drainAmount, drainRate
			{THICK_SKIN, 83, "Thick Skin", 1, 12}, {BURST_OF_STRENGTH, 84, "Burst of Strength", 4, 12}, {CLARITY_OF_THOUGHT, 85, "Clarity of Thought", 7, 12}, {ROCK_SKIN, 86, "Rock Skin", 10, 12}, {SUPERHUMAN_STRENGTH, 87, "Superhuman Strength", 13, 6}, {IMPROVED_REFLEXES, 88, "Improved Reflexes", 16, 6}, {RAPID_RESTORE, 89, "Rapid Restore", 19, 36}, {RAPID_HEAL, 90, "Rapid Heal", 22, 18}, {PROTECT_ITEM, 91, "Protect Item", 25, 18}, {STEEL_SKIN, 92, "Steel Skin", 28, 3}, {ULTIMATE_STRENGTH, 93, "Ultimate Strength", 31, 3}, {INCREDIBLE_REFLEXES, 94, "Incredible Reflexes", 34, 3}, {PROTECT_FROM_MAGIC, 95, "Protect from Magic", 37, 3}, {PROTECT_FROM_RANGED, 96, "Protect from Range", 40, 3}, {PROTECT_FROM_MELEE, 97, "Protect from Melee", 43, 3}, {RETRIBUTION, 98, "Retribution", 46, 12}, {REDEMPTION, 99, "Redemption", 49, 6}, {SMITE, 100, "Smite", 52, 18}};

	private double amountToDrain = 0.0;

	public void prayerTick() {
		for (int i = 0; i < player.getIsUsingPrayer().length; i++) {
			try{
			prayerTimers[i]--;
			if (prayerTimers[i] <= 0) {
				prayerTimers[i] = ((int) (((Integer) PRAYER_DATA[i][4]) * (1 + ((double) player.getBonuses().get(11) / 30))) * 2);
				if (player.getIsUsingPrayer()[i]) {
					amountToDrain++;
				}
			}
            }catch(Exception e){
                continue;
            }
		}
		if (amountToDrain > 0) {
			drainPrayer((int) amountToDrain * (player.inBarrows() ? 2 : 1));
			amountToDrain = 0.0;
		}
	}

	public static int getPrayerData(Integer index, int dataSlot) {
		int data = 0;
		for (Object[] i : PRAYER_DATA) {
			if (i[0] == index) {
				data = (Integer) i[dataSlot];
			}
		}
		return data;
	}

	public void activatePrayer(Integer id) {
		if (player.isDead()) {
			return;
		}
		int config = 0;
		String name = null;
		int level = 0;
		for (Object[] data : PRAYER_DATA) {
			if (data[0] == id) {
				config = (Integer) data[1];
				name = (String) data[2];
				level = (Integer) data[3];
			}
		}
		if (player.getSkill().getPlayerLevel(Skill.PRAYER) < level) {
			player.getActionSender().sendConfig(config, 0);
			player.getDialogue().endDialogue();
			player.getDialogue().sendStatement("You need a prayer level of aleast " + level + " to use " + name + ".");
			player.getActionSender().sendMessage("You need a prayer level of at least " + level + " to use " + name + ".");
			return;
		}
		if (RulesData.NO_PRAYER.activated(player)) {
			player.getActionSender().sendMessage("Usage of prayers have been disabled during this fight!");
			player.getActionSender().sendConfig(config, 0);
			return;
		}
		if (player.getSkill().getLevel()[Skill.PRAYER] <= 0) {
			player.getActionSender().sendMessage("You have run out of prayer points; recharge your prayer points at an altar");
			resetAll();
			return;
		}
		int headIcon = -1;
		boolean hasHeadIcon = false;
		if (id == PROTECT_FROM_MAGIC || id == PROTECT_FROM_RANGED || id == PROTECT_FROM_MELEE) {
			if (player.getStopProtectPrayer() > System.currentTimeMillis()) {
				player.getActionSender().sendMessage("Your protection prayers are temporarily disabled.");
				player.getActionSender().sendConfig(getPrayerData(id, 1), 0);
				return;
			}
		}
		switch (id) {
			case PROTECT_FROM_MAGIC :
				headIcon = 2;
				hasHeadIcon = true;
				break;
			case PROTECT_FROM_RANGED :
				headIcon = 1;
				hasHeadIcon = true;
				break;
			case PROTECT_FROM_MELEE :
				headIcon = 0;
				hasHeadIcon = true;
				break;
			case RETRIBUTION :
				headIcon = 3;
				hasHeadIcon = true;
				break;
			case REDEMPTION :
				headIcon = 5;
				hasHeadIcon = true;
				break;
			case SMITE :
				headIcon = 4;
				hasHeadIcon = true;
				break;
		}
		if (hasHeadIcon) {
			player.setPrayerIcon(!player.getIsUsingPrayer()[id] ? headIcon : -1);
		}
		player.getIsUsingPrayer()[id] = !player.getIsUsingPrayer()[id];
		player.getActionSender().sendConfig(config, player.getIsUsingPrayer()[id] ? 1 : 0);
		switchPrayer(id);
		player.setPrayerDrainTimer(player.getIsUsingPrayer()[id] ? 0 : 1);
		player.setAppearanceUpdateRequired(true);
	}

	private void switchPrayer(int id) {
		int[] turnOff = new int[0];
		switch (id) {
			case THICK_SKIN :
				turnOff = new int[]{ROCK_SKIN, STEEL_SKIN};
				break;
			case ROCK_SKIN :
				turnOff = new int[]{THICK_SKIN, STEEL_SKIN};
				break;
			case STEEL_SKIN :
				turnOff = new int[]{THICK_SKIN, ROCK_SKIN};
				break;
			case CLARITY_OF_THOUGHT :
				turnOff = new int[]{IMPROVED_REFLEXES, INCREDIBLE_REFLEXES};
				break;
			case IMPROVED_REFLEXES :
				turnOff = new int[]{CLARITY_OF_THOUGHT, INCREDIBLE_REFLEXES};
				break;
			case INCREDIBLE_REFLEXES :
				turnOff = new int[]{IMPROVED_REFLEXES, CLARITY_OF_THOUGHT};
				break;
			case BURST_OF_STRENGTH :
				turnOff = new int[]{SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH};
				break;
			case SUPERHUMAN_STRENGTH :
				turnOff = new int[]{BURST_OF_STRENGTH, ULTIMATE_STRENGTH};
				break;
			case ULTIMATE_STRENGTH :
				turnOff = new int[]{SUPERHUMAN_STRENGTH, BURST_OF_STRENGTH};
				break;
			case PROTECT_FROM_MAGIC :
				turnOff = new int[]{REDEMPTION, SMITE, RETRIBUTION, PROTECT_FROM_RANGED, PROTECT_FROM_MELEE};
				break;
			case PROTECT_FROM_RANGED :
				turnOff = new int[]{REDEMPTION, SMITE, RETRIBUTION, PROTECT_FROM_MAGIC, PROTECT_FROM_MELEE};
				break;
			case PROTECT_FROM_MELEE :
				turnOff = new int[]{REDEMPTION, SMITE, RETRIBUTION, PROTECT_FROM_RANGED, PROTECT_FROM_MAGIC};
				break;
			case RETRIBUTION :
				turnOff = new int[]{REDEMPTION, SMITE, PROTECT_FROM_MELEE, PROTECT_FROM_RANGED, PROTECT_FROM_MAGIC};
				break;
			case REDEMPTION :
				turnOff = new int[]{RETRIBUTION, SMITE, PROTECT_FROM_MELEE, PROTECT_FROM_RANGED, PROTECT_FROM_MAGIC};
				break;
			case SMITE :
				turnOff = new int[]{REDEMPTION, RETRIBUTION, PROTECT_FROM_MELEE, PROTECT_FROM_RANGED, PROTECT_FROM_MAGIC};
				break;
		}
		for (int i : turnOff) {
			if (i != id) {
				player.getIsUsingPrayer()[i] = false;
				player.getActionSender().sendConfig(getPrayerData(i, 1), 0);
			}
		}
	}

	public void unactivatePrayer(int id) {
		if (player.getIsUsingPrayer()[id]) {
			player.getIsUsingPrayer()[id] = false;
			player.getActionSender().sendConfig(getPrayerData(id, 1), 0);
			if (id == PROTECT_FROM_MAGIC || id == PROTECT_FROM_RANGED || id == PROTECT_FROM_MELEE || id == RETRIBUTION || id == REDEMPTION || id == SMITE) {
				player.setPrayerIcon(-1);
				player.setAppearanceUpdateRequired(true);
			}
		}
	}

	public void drainPrayer(int drainAmount) {
		player.getSkill().getLevel()[Skill.PRAYER] -= drainAmount;
		if (player.getSkill().getLevel()[Skill.PRAYER] <= 0) {
			player.getSkill().getLevel()[Skill.PRAYER] = 0;
			player.getSkill().refresh(Skill.PRAYER);
			resetAll();
			player.getActionSender().sendMessage("You have ran out of prayer points;" + " you must recharge at an altar.");
			return;
		}
		player.getSkill().refresh(Skill.PRAYER);
	}

	public void applySmiteEffect(Player victim, int hit) {
		if (player.getIsUsingPrayer()[SMITE]) {
			if ((victim.getSkill().getLevel()[Skill.PRAYER] -= hit / 4) < 0) {
				victim.getSkill().getLevel()[Skill.PRAYER] = 0;
			} else {
				victim.getSkill().getLevel()[Skill.PRAYER] -= hit / 4;
			}
			victim.getSkill().refresh(Skill.PRAYER);
		}
	}

	public void applyRedemptionPrayer(Player player) {
		if (player.getSkill().getLevel()[Skill.HITPOINTS] <= (int) (player.getSkill().getPlayerLevel(Skill.HITPOINTS) * 0.1)) {
			player.getSkill().getLevel()[Skill.HITPOINTS] += (int) (player.getSkill().getPlayerLevel(Skill.PRAYER) * 0.25);
			player.getUpdateFlags().sendGraphic(436, 0);
			player.getSkill().refresh(Skill.PRAYER);
			player.getSkill().setSkillLevel(Skill.PRAYER, 0);
			player.getSkill().refresh(Skill.HITPOINTS);
		}
	}

	public static int prayerHitModifiers(Entity attacker, Entity victim, int hit) {
		if (victim.isPlayer()) {
			Player victimPlayer = (Player) victim;
			if (attacker.isPlayer()) {
				Player attackingPlayer = (Player) attacker;
				if (victimPlayer.getIsUsingPrayer()[PROTECT_FROM_MELEE] && attackingPlayer.getAttackType() == Entity.AttackTypes.MELEE && !attackingPlayer.hasFullVerac()) {
					hit = hit / 4;
				} else if (victimPlayer.getIsUsingPrayer()[PROTECT_FROM_RANGED] && attackingPlayer.getAttackType() == Entity.AttackTypes.RANGED) {
					hit = hit / 4;
				} else if (victimPlayer.getIsUsingPrayer()[PROTECT_FROM_MAGIC] && attackingPlayer.getAttackType() == Entity.AttackTypes.MAGIC) {
					hit = hit / 4;
				}
			} else if (attacker.isNpc()) {
				Npc attackingNpc = (Npc) attacker;
				if (victimPlayer.getIsUsingPrayer()[PROTECT_FROM_MELEE] && attackingNpc.getAttackType() == Entity.AttackTypes.MELEE) {
					hit = 0;
				} else if (victimPlayer.getIsUsingPrayer()[PROTECT_FROM_RANGED] && attackingNpc.getAttackType() == Entity.AttackTypes.RANGED) {
					hit = 0;
				} else if (victimPlayer.getIsUsingPrayer()[PROTECT_FROM_MAGIC] && attackingNpc.getAttackType() == Entity.AttackTypes.MAGIC) {
					hit = 0;
				}
			}
		}
		return hit;
	}

	public void resetAll() {
		for (int i = 0; i < PRAYER_DATA.length; i++) {
			player.getIsUsingPrayer()[i] = false;
			player.getActionSender().sendConfig(getPrayerData(i, 1), 0);
		}
		player.setPrayerIcon(-1);
		player.setAppearanceUpdateRequired(true);
	}

	public boolean setPrayers(int buttonId) {
		switch (buttonId) {
			case 21233 :
				activatePrayer(THICK_SKIN);
				return true;
			case 21234 :
				activatePrayer(BURST_OF_STRENGTH);
				return true;
			case 21235 :
				activatePrayer(CLARITY_OF_THOUGHT);
				return true;
			case 21236 :
				activatePrayer(ROCK_SKIN);
				return true;
			case 21237 :
				activatePrayer(SUPERHUMAN_STRENGTH);
				return true;
			case 21238 :
				activatePrayer(IMPROVED_REFLEXES);
				return true;
			case 21239 :
				activatePrayer(RAPID_RESTORE);
				return true;
			case 21240 :
				activatePrayer(RAPID_HEAL);
				return true;
			case 21241 :
				activatePrayer(PROTECT_ITEM);
				return true;
			case 21242 :
				activatePrayer(STEEL_SKIN);
				return true;
			case 21243 :
				activatePrayer(ULTIMATE_STRENGTH);
				return true;
			case 21244 :
				activatePrayer(INCREDIBLE_REFLEXES);
				return true;
			case 21245 :
				activatePrayer(PROTECT_FROM_MAGIC);
				return true;
			case 21246 :
				activatePrayer(PROTECT_FROM_RANGED);
				return true;
			case 21247 :
				activatePrayer(PROTECT_FROM_MELEE);
				return true;
			case 2171 :
				activatePrayer(RETRIBUTION);
				return true;
			case 2172 :
				activatePrayer(REDEMPTION);
				applyRedemptionPrayer(player);
				return true;
			case 2173 :
				activatePrayer(SMITE);
				return true;
		}
		return false;
	}

	public static void rechargePrayer(final Player player) {
		if (player.getSkill().getLevel()[Skill.PRAYER] < player.getSkill().getPlayerLevel(Skill.PRAYER)) {
			player.getUpdateFlags().sendAnimation(645);
			player.getSkill().setSkillLevel(Skill.PRAYER, player.getSkill().getPlayerLevel(Skill.PRAYER));
			player.getSkill().refresh(Skill.PRAYER);
			player.getActionSender().sendMessage("You recharge your prayer at the altar.");
		} else {
			player.getActionSender().sendMessage("You already have full prayer!");
		}
	}

	public static void rechargePrayerGuild(final Player player) {
		if (player.getSkill().getLevel()[Skill.PRAYER] < player.getSkill().getPlayerLevel(Skill.PRAYER) + 2) {
			player.getUpdateFlags().sendAnimation(645);
			player.getSkill().setSkillLevel(Skill.PRAYER, player.getSkill().getPlayerLevel(Skill.PRAYER) + 2);
			player.getSkill().refresh(Skill.PRAYER);
			player.getActionSender().sendMessage("You recharge your prayer at the altar.");
			player.getActionSender().sendMessage("You recieve a temporary prayer boost.");
		} else {
			player.getActionSender().sendMessage("You already have full prayer!");
		}
	}

	public static void applyRetribution(final Entity died, final Entity killer) {
		final Player player = (Player) died;
		final boolean inMulti = died.inMulti();
		final int damage = player.getSkill().getPlayerLevel(Skill.PRAYER) / 4;
		final HitDef hitDef = new HitDef(null, HitType.NORMAL, damage).randomizeDamage().setUnblockable(true);
		Tick tick = new Tick(3) {
			@Override
			public void execute() {
				player.getUpdateFlags().sendGraphic(437, 0);
				if (!inMulti) {
					new Hit(died, killer, hitDef).initialize();
					stop();
					return;
				}
				for (Player players : World.getPlayers()) {
					if (players != null && players != died) {
						CombatCycleEvent.CanAttackResponse canAttackResponse = CombatCycleEvent.canAttack(died, players);
						if (Misc.getDistance(died.getPosition(), players.getPosition()) <= 1 && canAttackResponse == CanAttackResponse.SUCCESS) {
							new Hit(died, players, hitDef).initialize();
							if (!inMulti) {
								stop();
								return;
							}
						}
					}
				}
				for (Npc npcs : World.getNpcs()) {
					if (npcs != null) {
						CombatCycleEvent.CanAttackResponse canAttackResponse = CombatCycleEvent.canAttack(died, npcs);
						if (died.goodDistanceEntity(npcs, 1) && canAttackResponse == CanAttackResponse.SUCCESS) {
							new Hit(died, npcs, hitDef).initialize();
							if (!inMulti) {
								stop();
								return;
							}
						}
					}
				}
				stop();
			}
		};
		World.getTickManager().submit(tick);
	}

	public static void applyRedemption(Player player, Entity victim, int currentHp) {
		int lifeBonus = (int) Math.floor(player.getSkill().getPlayerLevel(Skill.PRAYER) / 4.0);
		currentHp += lifeBonus;
		if (currentHp > victim.getMaxHp())
			currentHp = victim.getMaxHp();
		player.getUpdateFlags().sendGraphic(436, 0);
		player.getSkill().setSkillLevel(Skill.PRAYER, 0);
		player.getPrayer().resetAll();
	}

	public static void applySmite(Player player, Player other, int damage) {
		int prayerLevel = other.getSkill().getLevel()[Skill.PRAYER] - (int) Math.floor(damage / 4);
		if (prayerLevel < 0)
			prayerLevel = 0;
		other.getSkill().setSkillLevel(Skill.PRAYER, prayerLevel);
		other.getSkill().refresh(Skill.PRAYER);
	}
}
