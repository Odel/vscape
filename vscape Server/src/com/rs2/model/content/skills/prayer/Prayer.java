package com.rs2.model.content.skills.prayer;

import com.rs2.Constants;
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

	public final int prayerSize = 24;
	public int[] prayerTimers = new int[prayerSize];

	//public static final int THICK_SKIN = 0, BURST_OF_STRENGTH = 1, CLARITY_OF_THOUGHT = 2, ROCK_SKIN = 3, SUPERHUMAN_STRENGTH = 4, IMPROVED_REFLEXES = 5, RAPID_RESTORE = 6, RAPID_HEAL = 7, PROTECT_ITEM = 8, STEEL_SKIN = 9, ULTIMATE_STRENGTH = 10, INCREDIBLE_REFLEXES = 11, PROTECT_FROM_MAGIC = 12, PROTECT_FROM_RANGED = 13, PROTECT_FROM_MELEE = 14, RETRIBUTION = 15, REDEMPTION = 16, SMITE = 17;

	public enum PrayerData {
		THICK_SKIN(0, 83, "Thick Skin", 1, 12),
		BURST_OF_STRENGTH(1, 84, "Burst of Strength", 4, 12), 
		CLARITY_OF_THOUGHT(2, 85, "Clarity of Thought", 7, 12), 
		SHARP_EYE(3, 700, "Sharp Eye", 8, 12), 
		MYSTIC_WILL(4, 701, "Mystic Will", 9, 12), 
		ROCK_SKIN(5, 86, "Rock Skin", 10, 12),
		SUPERHUMAN_STRENGTH(6, 87, "Superhuman Strength", 13, 6),
		IMPROVED_REFLEXES(7, 88, "Improved Reflexes", 16, 6),
		RAPID_RESTORE(8, 89, "Rapid Restore", 19, 36), 
		RAPID_HEAL(9, 90, "Rapid Heal", 22, 18),
		PROTECT_ITEM(10, 91, "Protect Item", 25, 18),
		HAWK_EYE(11, 702, "Hawk Eye", 26, 6), 
		MYSTIC_LORE(12, 703, "Mystic Lore", 27, 6), 
		STEEL_SKIN(13, 92, "Steel Skin", 28, 3),
		ULTIMATE_STRENGTH(14, 93, "Ultimate Strength", 31, 3), 
		INCREDIBLE_REFLEXES(15, 94, "Incredible Reflexes", 34, 3),
		PROTECT_FROM_MAGIC(16, 95, "Protect from Magic", 37, 3),
		PROTECT_FROM_RANGED(17, 96, "Protect from Range", 40, 3), 
		PROTECT_FROM_MELEE(18, 97, "Protect from Melee", 43, 3), 
		EAGLE_EYE(19, 704, "Eagle Eye", 44, 3), 
		MYSTIC_MIGHT(20, 705, "Mystic Might", 45, 3), 
		RETRIBUTION(21, 98, "Retribution", 46, 12),
		REDEMPTION(22, 99, "Redemption", 49, 6),
		SMITE(23, 100, "Smite", 52, 18);
		
		private int id;
		private int configId;
		private String name;
		private int levelRequired;
		private int drainRate;
		
		private PrayerData(int id, int configId, String name, int levelRequired, int drainRate)
		{
			this.id = id;
			this.configId = configId;
			this.name = name;
			this.levelRequired = levelRequired;
			this.drainRate = drainRate;
		}
		
		public static PrayerData forId(int index) {
			for (PrayerData prayerData : PrayerData.values()) {
				if (prayerData.getIndex() == index)
					return prayerData;
			}
			return null;
		}
		
		public int getIndex(){
			return id;
		}
		public int getConfigId(){
			return configId;
		}
		public String getName(){
			return name;
		}
		public int getLevelRequired(){
			return levelRequired;
		}
		public int getDrainRate(){
			return drainRate;
		}
	}
	
	private double amountToDrain = 0.0;
	
	public void prayerTick() {
		if(Constants.DDOS_PROTECT_MODE) {
			return;
		}
		for (int i = 0; i < player.getIsUsingPrayer().length; i++) {
			try{
			prayerTimers[i]--;
			if (prayerTimers[i] <= 0) {
				prayerTimers[i] = ((int) (PrayerData.forId(i).getDrainRate() * (1 + ((double) player.getBonuses().get(11) / 30))) * 2);
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
	
	public void resetAll() {
		for (int i = 0; i < PrayerData.values().length; i++) {
			player.getIsUsingPrayer()[i] = false;
			player.getActionSender().sendConfig(PrayerData.forId(i).getConfigId(), 0);
		}
		player.setPrayerIcon(-1);
		player.setAppearanceUpdateRequired(true);
	}
	
	@SuppressWarnings("incomplete-switch")
	public void activatePrayer(int id) {
		if (player.isDead()) {
			return;
		}
		PrayerData data = PrayerData.forId(id);
		if(data == null)
			return;
		int config = data.getConfigId();
		String name = data.getName();
		int level = data.getLevelRequired();
		
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
		if (data == PrayerData.PROTECT_FROM_MAGIC || data == PrayerData.PROTECT_FROM_RANGED || data == PrayerData.PROTECT_FROM_MELEE) {
			if (player.getStopProtectPrayer() > System.currentTimeMillis()) {
				player.getActionSender().sendMessage("Your protection prayers are temporarily disabled.");
				player.getActionSender().sendConfig(config, 0);
				return;
			}
		}
		switch (data) {
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
		switchPrayer(data);
		player.setPrayerDrainTimer(player.getIsUsingPrayer()[id] ? 0 : 1);
		player.setAppearanceUpdateRequired(true);
	}
	
	@SuppressWarnings("incomplete-switch")
	private void switchPrayer(PrayerData data) {
		PrayerData[] turnOff = new PrayerData[0];
		switch (data) {
			case THICK_SKIN :
				turnOff = new PrayerData[]{PrayerData.ROCK_SKIN, PrayerData.STEEL_SKIN};
				break;
			case ROCK_SKIN :
				turnOff = new PrayerData[]{PrayerData.THICK_SKIN, PrayerData.STEEL_SKIN};
				break;
			case STEEL_SKIN :
				turnOff = new PrayerData[]{PrayerData.THICK_SKIN, PrayerData.ROCK_SKIN};
				break;
			case CLARITY_OF_THOUGHT :
				turnOff = new PrayerData[]{PrayerData.IMPROVED_REFLEXES, PrayerData.INCREDIBLE_REFLEXES,PrayerData.MYSTIC_WILL, PrayerData.MYSTIC_LORE, PrayerData.MYSTIC_MIGHT};
				break;
			case IMPROVED_REFLEXES :
				turnOff = new PrayerData[]{PrayerData.CLARITY_OF_THOUGHT, PrayerData.INCREDIBLE_REFLEXES,PrayerData.MYSTIC_WILL, PrayerData.MYSTIC_LORE, PrayerData.MYSTIC_MIGHT};
				break;
			case INCREDIBLE_REFLEXES :
				turnOff = new PrayerData[]{PrayerData.IMPROVED_REFLEXES, PrayerData.CLARITY_OF_THOUGHT,PrayerData.MYSTIC_WILL, PrayerData.MYSTIC_LORE, PrayerData.MYSTIC_MIGHT};
				break;
			case BURST_OF_STRENGTH :
				turnOff = new PrayerData[]{PrayerData.SUPERHUMAN_STRENGTH, PrayerData.ULTIMATE_STRENGTH,PrayerData.MYSTIC_WILL, PrayerData.MYSTIC_LORE, PrayerData.MYSTIC_MIGHT};
				break;
			case SUPERHUMAN_STRENGTH :
				turnOff = new PrayerData[]{PrayerData.BURST_OF_STRENGTH, PrayerData.ULTIMATE_STRENGTH,PrayerData.MYSTIC_WILL, PrayerData.MYSTIC_LORE, PrayerData.MYSTIC_MIGHT};
				break;
			case ULTIMATE_STRENGTH :
				turnOff = new PrayerData[]{PrayerData.SUPERHUMAN_STRENGTH, PrayerData.BURST_OF_STRENGTH,PrayerData.MYSTIC_WILL, PrayerData.MYSTIC_LORE, PrayerData.MYSTIC_MIGHT};
				break;
			case SHARP_EYE :
				turnOff = new PrayerData[]{PrayerData.HAWK_EYE, PrayerData.EAGLE_EYE,PrayerData.MYSTIC_WILL, PrayerData.MYSTIC_LORE, PrayerData.MYSTIC_MIGHT};
				break;
			case HAWK_EYE :
				turnOff = new PrayerData[]{PrayerData.SHARP_EYE, PrayerData.EAGLE_EYE,PrayerData.MYSTIC_WILL, PrayerData.MYSTIC_LORE, PrayerData.MYSTIC_MIGHT};
				break;
			case EAGLE_EYE :
				turnOff = new PrayerData[]{PrayerData.SHARP_EYE, PrayerData.HAWK_EYE,PrayerData.MYSTIC_WILL, PrayerData.MYSTIC_LORE, PrayerData.MYSTIC_MIGHT};
				break;
			case PROTECT_FROM_MAGIC :
				turnOff = new PrayerData[]{PrayerData.REDEMPTION, PrayerData.SMITE, PrayerData.RETRIBUTION, PrayerData.PROTECT_FROM_RANGED, PrayerData.PROTECT_FROM_MELEE};
				break;
			case PROTECT_FROM_RANGED :
				turnOff = new PrayerData[]{PrayerData.REDEMPTION, PrayerData.SMITE, PrayerData.RETRIBUTION, PrayerData.PROTECT_FROM_MAGIC, PrayerData.PROTECT_FROM_MELEE};
				break;
			case PROTECT_FROM_MELEE :
				turnOff = new PrayerData[]{PrayerData.REDEMPTION, PrayerData.SMITE, PrayerData.RETRIBUTION, PrayerData.PROTECT_FROM_RANGED, PrayerData.PROTECT_FROM_MAGIC};
				break;
			case RETRIBUTION :
				turnOff = new PrayerData[]{PrayerData.REDEMPTION, PrayerData.SMITE, PrayerData.PROTECT_FROM_MELEE, PrayerData.PROTECT_FROM_RANGED, PrayerData.PROTECT_FROM_MAGIC};
				break;
			case REDEMPTION :
				turnOff = new PrayerData[]{PrayerData.RETRIBUTION, PrayerData.SMITE, PrayerData.PROTECT_FROM_MELEE, PrayerData.PROTECT_FROM_RANGED, PrayerData.PROTECT_FROM_MAGIC};
				break;
			case SMITE :
				turnOff = new PrayerData[]{PrayerData.REDEMPTION, PrayerData.RETRIBUTION, PrayerData.PROTECT_FROM_MELEE, PrayerData.PROTECT_FROM_RANGED, PrayerData.PROTECT_FROM_MAGIC};
				break;
			case MYSTIC_WILL:
				turnOff = new PrayerData[]{
						PrayerData.CLARITY_OF_THOUGHT,PrayerData.IMPROVED_REFLEXES, PrayerData.INCREDIBLE_REFLEXES,
						PrayerData.BURST_OF_STRENGTH,PrayerData.SUPERHUMAN_STRENGTH, PrayerData.ULTIMATE_STRENGTH,
						PrayerData.SHARP_EYE,PrayerData.HAWK_EYE, PrayerData.EAGLE_EYE,
						PrayerData.MYSTIC_LORE,PrayerData.MYSTIC_MIGHT, 
					};
				break;
			case MYSTIC_LORE:
				turnOff = new PrayerData[]{
					PrayerData.CLARITY_OF_THOUGHT,PrayerData.IMPROVED_REFLEXES, PrayerData.INCREDIBLE_REFLEXES,
					PrayerData.BURST_OF_STRENGTH,PrayerData.SUPERHUMAN_STRENGTH, PrayerData.ULTIMATE_STRENGTH,
					PrayerData.SHARP_EYE,PrayerData.HAWK_EYE, PrayerData.EAGLE_EYE,
					PrayerData.MYSTIC_WILL,PrayerData.MYSTIC_MIGHT, 
				};
				break;
			case MYSTIC_MIGHT:
				turnOff = new PrayerData[]{
						PrayerData.CLARITY_OF_THOUGHT,PrayerData.IMPROVED_REFLEXES, PrayerData.INCREDIBLE_REFLEXES,
						PrayerData.BURST_OF_STRENGTH,PrayerData.SUPERHUMAN_STRENGTH, PrayerData.ULTIMATE_STRENGTH,
						PrayerData.SHARP_EYE,PrayerData.HAWK_EYE, PrayerData.EAGLE_EYE,
						PrayerData.MYSTIC_LORE,PrayerData.MYSTIC_WILL, 
					};
				break;
		}
		for (PrayerData i : turnOff) {
			if (i != data) {
				player.getIsUsingPrayer()[i.getIndex()] = false;
				player.getActionSender().sendConfig(i.getConfigId(), 0);
			}
		}
	}

	public void unactivatePrayer(PrayerData data) {
		if (player.getIsUsingPrayer()[data.getIndex()]) {
			player.getIsUsingPrayer()[data.getIndex()] = false;
			player.getActionSender().sendConfig(data.getConfigId(), 0);
			if (data == PrayerData.PROTECT_FROM_MAGIC || data == PrayerData.PROTECT_FROM_RANGED || data == PrayerData.PROTECT_FROM_MELEE || data == PrayerData.RETRIBUTION || data == PrayerData.REDEMPTION || data == PrayerData.SMITE) {
				player.setPrayerIcon(-1);
				player.setAppearanceUpdateRequired(true);
			}
		}
	}
	
	public void applySmiteEffect(Player victim, int hit) {
		if (player.getSkill().getLevel()[Skill.PRAYER] <= 0) {
			return;
		}
		if (player.getIsUsingPrayer()[PrayerData.SMITE.getIndex()]) {
			if ((victim.getSkill().getLevel()[Skill.PRAYER] -= hit / 4) < 0) {
				victim.getSkill().getLevel()[Skill.PRAYER] = 0;
			} else {
				victim.getSkill().getLevel()[Skill.PRAYER] -= hit / 4;
			}
			victim.getSkill().refresh(Skill.PRAYER);
		}
	}

	public void applyRedemptionPrayer(Player player) {
		if (player.getSkill().getLevel()[Skill.PRAYER] <= 0) {
			return;
		}
		if (player.getSkill().getLevel()[Skill.HITPOINTS] <= (int) (player.getSkill().getPlayerLevel(Skill.HITPOINTS) * 0.1)) {
			player.getSkill().getLevel()[Skill.HITPOINTS] += (int) (player.getSkill().getPlayerLevel(Skill.PRAYER) * 0.25);
			player.getUpdateFlags().sendGraphic(436, 0);
			player.getSkill().setSkillLevel(Skill.PRAYER, 0);
			player.getSkill().refresh(Skill.PRAYER);
			player.getSkill().refresh(Skill.HITPOINTS);
			player.getPrayer().resetAll();
		}
	}
	
	public static int prayerHitModifiers(Entity attacker, Entity victim, int hit) {
		if (victim.isPlayer()) {
			Player victimPlayer = (Player) victim;
			if (attacker.isPlayer()) {
				Player attackingPlayer = (Player) attacker;
				if (victimPlayer.getIsUsingPrayer()[PrayerData.PROTECT_FROM_MELEE.getIndex()] && attackingPlayer.getAttackType() == Entity.AttackTypes.MELEE && !attackingPlayer.hasFullVerac()) {
					hit = hit / 4;
				} else if (victimPlayer.getIsUsingPrayer()[PrayerData.PROTECT_FROM_RANGED.getIndex()] && attackingPlayer.getAttackType() == Entity.AttackTypes.RANGED) {
					hit = hit / 4;
				} else if (victimPlayer.getIsUsingPrayer()[PrayerData.PROTECT_FROM_MAGIC.getIndex()] && attackingPlayer.getAttackType() == Entity.AttackTypes.MAGIC) {
					hit = hit / 4;
				}
			} else if (attacker.isNpc()) {
				Npc attackingNpc = (Npc) attacker;
				if (victimPlayer.getIsUsingPrayer()[PrayerData.PROTECT_FROM_MELEE.getIndex()] && attackingNpc.getAttackType() == Entity.AttackTypes.MELEE) {
					hit = 0;
				} else if (victimPlayer.getIsUsingPrayer()[PrayerData.PROTECT_FROM_RANGED.getIndex()] && attackingNpc.getAttackType() == Entity.AttackTypes.RANGED) {
					hit = 0;
				} else if (victimPlayer.getIsUsingPrayer()[PrayerData.PROTECT_FROM_MAGIC.getIndex()] && attackingNpc.getAttackType() == Entity.AttackTypes.MAGIC) {
					hit = 0;
				}
			}
		}
		return hit;
	}
	public boolean setPrayers(int buttonId) {
		switch (buttonId) {
			case 21233 :
				activatePrayer(PrayerData.THICK_SKIN.getIndex());
				return true;
			case 21234 :
				activatePrayer(PrayerData.BURST_OF_STRENGTH.getIndex());
				return true;
			case 21235 :
				activatePrayer(PrayerData.CLARITY_OF_THOUGHT.getIndex());
				return true;
			case 77100 :
				activatePrayer(PrayerData.SHARP_EYE.getIndex());
				return true;
			case 77102 :
				activatePrayer(PrayerData.MYSTIC_WILL.getIndex());
				return true;
			case 21236 :
				activatePrayer(PrayerData.ROCK_SKIN.getIndex());
				return true;
			case 21237 :
				activatePrayer(PrayerData.SUPERHUMAN_STRENGTH.getIndex());
				return true;
			case 21238 :
				activatePrayer(PrayerData.IMPROVED_REFLEXES.getIndex());
				return true;
			case 21239 :
				activatePrayer(PrayerData.RAPID_RESTORE.getIndex());
				return true;
			case 21240 :
				activatePrayer(PrayerData.RAPID_HEAL.getIndex());
				return true;
			case 21241 :
				activatePrayer(PrayerData.PROTECT_ITEM.getIndex());
				return true;
			case 77104 :
				activatePrayer(PrayerData.HAWK_EYE.getIndex());
				return true;
			case 77106 :
				activatePrayer(PrayerData.MYSTIC_LORE.getIndex());
				return true;
			case 21242 :
				activatePrayer(PrayerData.STEEL_SKIN.getIndex());
				return true;
			case 21243 :
				activatePrayer(PrayerData.ULTIMATE_STRENGTH.getIndex());
				return true;
			case 21244 :
				activatePrayer(PrayerData.INCREDIBLE_REFLEXES.getIndex());
				return true;
			case 21245 :
				activatePrayer(PrayerData.PROTECT_FROM_MAGIC.getIndex());
				return true;
			case 21246 :
				activatePrayer(PrayerData.PROTECT_FROM_RANGED.getIndex());
				return true;
			case 21247 :
				activatePrayer(PrayerData.PROTECT_FROM_MELEE.getIndex());
				return true;
			case 77109 :
				activatePrayer(PrayerData.EAGLE_EYE.getIndex());
				return true;
			case 77111 :
				activatePrayer(PrayerData.MYSTIC_MIGHT.getIndex());
				return true;
			case 2171 :
				activatePrayer(PrayerData.RETRIBUTION.getIndex());
				return true;
			case 2172 :
				activatePrayer(PrayerData.REDEMPTION.getIndex());
				applyRedemptionPrayer(player);
				return true;
			case 2173 :
				activatePrayer(PrayerData.SMITE.getIndex());
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
		if (player.getSkill().getLevel()[Skill.PRAYER] <= 0) {
			return;
		}
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
		if (player.getSkill().getLevel()[Skill.PRAYER] <= 0) {
			return;
		}
		int lifeBonus = (int) Math.floor(player.getSkill().getPlayerLevel(Skill.PRAYER) / 4.0);
		currentHp += lifeBonus;
		if (currentHp > victim.getMaxHp())
			currentHp = victim.getMaxHp();
		player.getUpdateFlags().sendGraphic(436, 0);
		player.getSkill().getLevel()[Skill.HITPOINTS] = currentHp;
		player.getSkill().setSkillLevel(Skill.PRAYER, 0);
		player.getSkill().refresh(Skill.PRAYER);
		player.getSkill().refresh(Skill.HITPOINTS);
		player.getPrayer().resetAll();
	}

	public static void applySmite(Player player, Player other, int damage) {
		if (player.getSkill().getLevel()[Skill.PRAYER] <= 0) {
			return;
		}
		int prayerLevel = other.getSkill().getLevel()[Skill.PRAYER] - (int) Math.floor(damage / 4);
		if (prayerLevel < 0)
			prayerLevel = 0;
		other.getSkill().setSkillLevel(Skill.PRAYER, prayerLevel);
		other.getSkill().refresh(Skill.PRAYER);
	}
}
