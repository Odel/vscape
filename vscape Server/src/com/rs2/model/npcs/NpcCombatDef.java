package com.rs2.model.npcs;

import java.util.HashMap;
import java.util.Map;

import com.rs2.model.Entity;
import com.rs2.model.content.combat.AttackScript;
import com.rs2.model.content.combat.attacks.BasicAttack;
import com.rs2.model.content.combat.weapon.AttackStyle;
import com.rs2.model.content.combat.weapon.Weapon;
import com.rs2.model.players.item.ItemDefinition;

/**
 *
 */
public abstract class NpcCombatDef {

	private static final Map<Integer, NpcCombatDef> npcCombatDefs = new HashMap<Integer, NpcCombatDef>();

	private int deathAnimationLength, spawnDelay;
	private boolean attBonusSet, defBonusSet;
	private Map<Integer, Integer> bonuses = new HashMap<Integer, Integer>();
	private NpcWeakness weakness = null;

	public abstract AttackScript[] attackScripts(Entity attacker, Entity victim);

	public NpcCombatDef() {
		this.deathAnimationLength = 6;
		this.spawnDelay = 30;
		for (AttackStyle.Bonus bonus : AttackStyle.Bonus.values()) {
			this.bonuses.put(bonus.toInteger(), 0);
			this.bonuses.put(bonus.toInteger() + AttackStyle.Bonus.values().length, 0);
		}
	}

	public NpcCombatDef bonusAtt(int stab, int slash, int crush, int magic, int ranged) {
		int modifier = 0;
		this.bonuses.put(modifier, bonuses.get(modifier) + stab);
		this.bonuses.put(1 + modifier, bonuses.get(modifier + 1) + slash);
		this.bonuses.put(2 + modifier, bonuses.get(modifier + 2) + crush);
		this.bonuses.put(3 + modifier, bonuses.get(modifier + 3) + magic);
		this.bonuses.put(4 + modifier, bonuses.get(modifier + 4) + ranged);
		setAttBonusSet(true);
		return this;
	}

	public NpcCombatDef bonusDef(int stab, int slash, int crush, int magic, int ranged) {
		int modifier = 5;
		this.bonuses.put(modifier, bonuses.get(modifier) + stab);
		this.bonuses.put(1 + modifier, bonuses.get(modifier + 1) + slash);
		this.bonuses.put(2 + modifier, bonuses.get(modifier + 2) + crush);
		this.bonuses.put(3 + modifier, bonuses.get(modifier + 3) + magic);
		this.bonuses.put(4 + modifier, bonuses.get(modifier + 4) + ranged);
		setDefBonusSet(true);
		return this;
	}

	public NpcCombatDef weakness(NpcWeakness weakness) {
		if (this.weakness != null) {
			return this;
		}
		this.weakness = weakness;
		return this;
	}

	public NpcCombatDef equip(int itemId) {
		ItemDefinition def = ItemDefinition.forId(itemId);
		int[] ib = def.getBonuses();
		bonusAtt(ib[0], ib[1], ib[2], ib[3], ib[4]);
		return bonusDef(ib[5], ib[6], ib[7], ib[8], ib[9]);
	}

	public NpcCombatDef deathLength(int deathAnimationLength) {
		if (this.deathAnimationLength != -1) {
			return this;
		}
		this.deathAnimationLength = deathAnimationLength;
		return this;
	}

	public NpcCombatDef respawn(int spawnDelay) {
		if (this.spawnDelay != -1) {
			return this;
		}
		this.spawnDelay = spawnDelay;
		return this;
	}

	public NpcCombatDef respawnSeconds(int seconds) {
		return respawn((int) Math.ceil(seconds * 1000.0 / 600.0));
	}

	public NpcWeakness getWeakness() {
		return weakness;
	}

	public int getDeathAnimationLength() {
		return deathAnimationLength;
	}

	public int getSpawnDelay() {
		return spawnDelay;
	}

	public Map<Integer, Integer> getBonuses() {
		return bonuses;
	}

	public static void add(int[] ids, NpcCombatDef npcCombatDef) {
		for (int id : ids)
			npcCombatDefs.put(id, npcCombatDef);
	}

	public static boolean defExists(int npcId) {
		NpcCombatDef def = npcCombatDefs.get(npcId);
		return def != null;
	}

	public static NpcCombatDef getDef(int npcId) {
		NpcCombatDef def = npcCombatDefs.get(npcId);
		if (def == null) {
			def = new NpcCombatDef() {
				@Override
				public AttackScript[] attackScripts(Entity attacker, Entity victim) {
					return new AttackScript[]{BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.AGGRESSIVE, 0, Weapon.FISTS)};
				}
			};
			npcCombatDefs.put(npcId, def);
		}
		return def;
	}

	/**
	 * @param attBonusSet the attBonusSet to set
	 */
	public void setAttBonusSet(boolean attBonusSet) {
		this.attBonusSet = attBonusSet;
	}

	/**
	 * @return the attBonusSet
	 */
	public boolean isAttBonusSet() {
		return attBonusSet;
	}

	/**
	 * @param defBonusSet the defBonusSet to set
	 */
	public void setDefBonusSet(boolean defBonusSet) {
		this.defBonusSet = defBonusSet;
	}

	/**
	 * @return the defBonusSet
	 */
	public boolean isDefBonusSet() {
		return defBonusSet;
	}

}
