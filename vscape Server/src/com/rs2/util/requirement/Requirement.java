package com.rs2.util.requirement;

import com.rs2.model.Entity;
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
		if (!meets && failMessage != null && entity.isPlayer())
			((Player) entity).getActionSender().sendMessage(failMessage);
		return meets;

	}

}
