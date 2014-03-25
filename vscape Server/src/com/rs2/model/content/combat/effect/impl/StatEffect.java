package com.rs2.model.content.combat.effect.impl;

import com.rs2.model.Entity;
import com.rs2.model.content.combat.effect.Effect;
import com.rs2.model.content.combat.effect.EffectTick;
import com.rs2.model.content.combat.hit.Hit;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;

/**
 *
 */
@SuppressWarnings("rawtypes")
public class StatEffect extends Effect<EffectTick> {

	private int skillId, decrease;
	private double rate;

	public StatEffect(int skillId, double rate) {
		this.skillId = skillId;
		this.rate = rate;
	}

	public StatEffect(int skillId, int decrease) {
		this.skillId = skillId;
		this.decrease = decrease;
	}

	@Override
	public void onExecution(Hit hit, EffectTick tick) {
	}

	@Override
	public boolean isCompatible(Entity entity) {
		return true;
	}

	@Override
	public void onInit(Hit hit, EffectTick tick) {
		Entity victim = hit.getVictim();
		int statDecrease = calculateStatDecrease(calculateBaseStat(victim));
		if (victim.isPlayer()) {
			Player player = (Player) victim;
			player.getSkill().getLevel()[skillId] -= statDecrease;
			if (player.getSkill().getLevel()[skillId] < 0) {
				player.getSkill().getLevel()[skillId] = 0;
			}
			/*int currentLevel = player.getSkill().getPlayerLevel(skillId) - statDecrease;
			if (currentLevel < 0)
				currentLevel = 0;
			if (currentLevel >= player.getSkill().getLevel()[skillId])
				return;
			player.getSkill().setSkillLevel(skillId, currentLevel);*/
			player.getSkill().refresh(skillId);
		} else {
			// Npc npc = (Npc) victim;
			// TODO: decrease npc accuracy/defence/max hit
		}
	}

	@Override
	public EffectTick generateTick(Entity attacker, Entity victim) {
		return new EffectTick<StatEffect>((calculateStatDecrease(calculateBaseStat(attacker)) * 50), this, attacker, victim) {
			@Override
			public void execute() {
				stop();
			}
		};
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof StatEffect))
			return false;
		StatEffect o = (StatEffect) other;
		return skillId == o.skillId;
	}

	public int calculateBaseStat(Entity entity) {
		if (entity.isPlayer()) {
			Player player = (Player) entity;
			return player.getSkill().getPlayerLevel(skillId);
		} else {
			Npc npc = (Npc) entity;
			/*NpcCombatDef combatDef = npc.getCombatDef();
			if (skillId == Skill.ATTACK)
				return combatDef.getAttack();
			else if (skillId == Skill.DEFENCE)
				return combatDef.getDefence();
			else if (skillId == Skill.STRENGTH)*/
			return npc.getDefinition().getCombat();
		}
		//return 0;
	}

	public int calculateStatDecrease(int baseLevel) {
		if (decrease > 0) {
			return decrease;
		}
		return (int) Math.ceil(baseLevel * rate);
	}
}
