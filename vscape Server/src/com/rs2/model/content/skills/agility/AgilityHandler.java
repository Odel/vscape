package com.rs2.model.content.skills.agility;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.players.MovementLock;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class AgilityHandler {

	public static void crossLog(final Player player, final Position targetPos, final Position failPos, final double EXP, final int levelReq, boolean canFail, final int failTime){
        DoAgilityEvent(player, new AgilityEvent(levelReq, canFail, failTime){
				@Override
				public void success() {
			        player.walkTo(targetPos, false);
			        player.getMovementHandler().lock(getLock(player, 762, EXP));
				}
	
				@Override
				public void failure() {
					killMovementLock(player);
					player.setStopPacket(true);
					player.getUpdateFlags().sendAnimation(770);
	    			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
	    				@Override
	    				public void execute(CycleEventContainer container) {
	    					player.setStopPacket(false);
	    					player.resetAnimation();
	    					player.teleport(failPos);
	    					player.hit(Misc.random(2, 6), HitType.NORMAL);
	    					container.stop();
	    				}
	    				@Override
	    				public void stop() {
	    				}
	    			}, 2);
				}
	
				@Override
				public void levelRequirement() {
					player.getDialogue().sendStatement("You need an agility level of "+levelReq+" to cross this log.");
				}
        });
	}
	
	public static void killMovementLock(final Player player){
	/*	if (player.getMovementHandler().movementLock == null)
			return;
		player.getMovementHandler().movementLock = null;*/
  	  	player.isCrossingObstacle = false;
  	  	player.setStopPacket(false);
        player.setStandAnim(-1);
        player.setRunAnim(-1);
        player.setWalkAnim(-1);
        player.setAppearanceUpdateRequired(true);
        player.getMovementPaused().reset();
	}
	
	private static MovementLock getLock(final Player player, final int anim, final double EXP){
		return new MovementLock() {
            @Override
            public boolean forcesRun() {
                return false;
            }

            @Override
            public void start(Entity entity) {
            	player.isCrossingObstacle = true;
          	  	player.setStopPacket(true);
                player.setRunAnim(anim);
                player.setWalkAnim(anim);
                player.setAppearanceUpdateRequired(true);
            }

            @Override
            public void end(Entity entity) {
          	  	player.isCrossingObstacle = false;
          	  	player.setStopPacket(false);
                player.setStandAnim(-1);
                player.setRunAnim(-1);
                player.setWalkAnim(-1);
                player.setAppearanceUpdateRequired(true);
                player.getMovementPaused().reset();
				if(EXP > 0){
					player.getSkill().addExp(Skill.AGILITY, EXP);
				}
            }
        };
	}
	
	public static void DoAgilityEvent(final Player player, final AgilityEvent event)
	{
		int agilityLevel = player.getSkill().getLevel()[Constants.SKILL_AGILITY];
		int requiredLevel = event.requiredLevel;
		if(agilityLevel < requiredLevel)
		{
			event.levelRequirement();
			return;
		}
		boolean canFail = event.canFail && event.failTime > 0;
		if(canFail)
		{
			event.success();
			
			int weight = (int)player.getWeight();
			int weightModifier = (100 / (weight+1));
			if(!SkillHandler.skillCheck(agilityLevel, requiredLevel, weightModifier))
			{
    			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
    				@Override
    				public void execute(CycleEventContainer container) {
    					container.stop();
    				}
    				@Override
    				public void stop() {
    					event.failure();
    				}
    			}, event.failTime);
			}
		}else{
			event.success();
		}
	}
}
