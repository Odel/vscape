package com.rs2.util.requirement;

import com.rs2.model.Entity;
import com.rs2.model.players.Player;

/**
 *
 */
public abstract class SkillLevelRequirement extends Requirement {
	private int skillId, skillLevel;

	public SkillLevelRequirement(int skillId, int skillLevel) {
		this.skillId = skillId;
		this.skillLevel = skillLevel;
	}

	@Override
	boolean meetsRequirement(Entity entity) {
		if (!entity.isPlayer())
			return true;
		Player player = (Player) entity;
		return player.getSkill().getLevel()[skillId] >= skillLevel;
	}
}
