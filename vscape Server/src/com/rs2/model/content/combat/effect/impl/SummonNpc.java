package com.rs2.model.content.combat.effect.impl;

import com.rs2.model.Entity;
import com.rs2.model.content.combat.effect.Effect;
import com.rs2.model.content.combat.effect.EffectTick;
import com.rs2.model.content.combat.hit.Hit;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;

/**
 *
 */
@SuppressWarnings("rawtypes")
public class SummonNpc extends Effect<EffectTick> {

	private int summonNpc;

	public SummonNpc(int summonNpc) {
		this.summonNpc = summonNpc;
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
		if (victim.isPlayer()) {
			Player player = (Player) victim;
			NpcLoader.spawnNpc(player, new Npc(summonNpc), true, false);
		}
	}

	@Override
	public EffectTick generateTick(Entity attacker, Entity victim) {
		return null;
	}

	@Override
	public boolean equals(Object other) {
		return false;
	}

}
