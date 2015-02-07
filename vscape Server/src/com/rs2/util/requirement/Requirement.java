package com.rs2.util.requirement;

import com.rs2.model.Entity;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.combat.attacks.SpellAttack;
import com.rs2.model.players.Player;

/**
 *
 */
public abstract class Requirement {

	public abstract String getFailMessage();

	abstract boolean meetsRequirement(Entity entity);

	public boolean meets(Entity entity) {
		boolean meets = meetsRequirement(entity);
		String failMessage = getFailMessage();
		if (!meets && failMessage != null && entity.isPlayer()) {
		    ((Player) entity).getActionSender().sendMessage(failMessage);
		    if (failMessage.equalsIgnoreCase(CombatManager.NO_AMMO_MESSAGE) || failMessage.equalsIgnoreCase(SpellAttack.FAILED_REQUIRED_RUNES)) {
			entity.setFailedCriticalRequirement(true);
		    }
		}
		return meets;

	}

}
