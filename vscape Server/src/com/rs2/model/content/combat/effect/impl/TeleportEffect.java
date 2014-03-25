package com.rs2.model.content.combat.effect.impl;

import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.combat.effect.Effect;
import com.rs2.model.content.combat.effect.EffectTick;
import com.rs2.model.content.combat.hit.Hit;
import com.rs2.model.npcs.Npc;
import com.rs2.util.Misc;

@SuppressWarnings("rawtypes")
public class TeleportEffect extends Effect<EffectTick> {

	@Override
	public void onExecution(Hit hit, EffectTick tick) {
	}

	@Override
	public boolean isCompatible(Entity entity) {
		return true;
	}

	@Override
	public void onInit(Hit hit, EffectTick tick) {
		Entity attacker = hit.getAttacker();
		Entity victim = hit.getVictim();
		int x = 0, y = 0;
		if (attacker.isNpc()) {
			x = ((Npc)attacker).getSpawnPosition().getX() - 10 + Misc.random(20);
			y = ((Npc)attacker).getSpawnPosition().getY() - 10 + Misc.random(20);
		} else {
			x = attacker.getPosition().getX() - 10 + Misc.random(20);
			y = attacker.getPosition().getY() - 10 + Misc.random(20);
		}
		if (x > 0) {
			victim.teleport(new Position(x, y));
		}
		CombatManager.resetCombat(victim);
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
