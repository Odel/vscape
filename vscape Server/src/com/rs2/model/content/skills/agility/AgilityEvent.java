package com.rs2.model.content.skills.agility;

public abstract class AgilityEvent {
	
	public int requiredLevel = 0;
	public boolean canFail = false;
	public int failTime = 0;
	public boolean failCalled = false;
	
	public AgilityEvent(final int requiredLevel, final boolean canFail, final int failTime)
	{
		this.requiredLevel = requiredLevel;
		this.canFail = canFail;
		this.failTime = failTime;
	}

	public abstract void success();

	public void failure()
	{
		failCalled = true;
	}
	
	public abstract void levelRequirement();
}
