package com.rs2.util.requirement;

import com.rs2.model.Entity;

/**
 *
 */
public abstract class ExecutableRequirement extends Requirement {

	public abstract void execute(Entity entity);

}
