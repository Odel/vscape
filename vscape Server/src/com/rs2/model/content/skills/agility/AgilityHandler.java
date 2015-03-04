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

	public static boolean crossLog(final Player player, final Position targetPos, final Position failPos, final double EXP, final int levelReq, boolean canFail, final int failTime, final int DMG){
		return DoAgilityEvent(player, new AgilityEvent(levelReq, canFail, failTime){
				@Override
				public void success() {
			        player.walkTo(targetPos, false);
			        player.getMovementHandler().lock(getLock(player, 762, EXP));
				}
	
				@Override
				public void failure() {
					super.failure();
					removeMovementLock(player);
					player.setStopPacket(true);
					player.getUpdateFlags().sendAnimation(770);
	    			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
	    				@Override
	    				public void execute(CycleEventContainer container) {
	    					player.setStopPacket(false);
	    					player.resetAnimation();
	    					player.teleport(failPos);
	    					player.hit(Misc.random(1, DMG), HitType.NORMAL);
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
	
	public static boolean crossLedge(final Player player, final int ledgeFace, final Position targetPos, final Position finalPos, final Position failPos, final double EXP, final int levelReq, boolean canFail, final int failTime, final int DMG){
		return DoAgilityEvent(player, new AgilityEvent(levelReq, canFail, failTime){
				@Override
				public void success() {
			        player.walkTo(targetPos, false);
					int anim = 754;
					switch(ledgeFace)
					{
						case 0:
							anim = 0; //east
							break;
						case 1:
							anim = 754; //south
							break;
						case 2:
							anim = 0; //west
							break;
						case 3:
							anim = 756; //north
							break;
					}
			        player.getMovementHandler().lock(getLock(player, anim, EXP));
			        if(finalPos != null){
						CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
							@Override
							public void execute(CycleEventContainer container) {
								if(failCalled)
								{
									container.stop();
									return;
								}
								player.walkTo(finalPos, false);
								player.getMovementHandler().lock(getLock(player, -1, 0));
								container.stop();
							}
							@Override
							public void stop() {
							}
						}, 6);
			        }
				}
	
				@Override
				public void failure() {
					super.failure();
					removeMovementLock(player);
					player.setStopPacket(true);
					player.getUpdateFlags().sendAnimation(770);
	    			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
	    				@Override
	    				public void execute(CycleEventContainer container) {
	    					player.setStopPacket(false);
	    					player.resetAnimation();
	    					player.teleport(failPos);
	    					player.hit(Misc.random(1, DMG), HitType.NORMAL);
	    					container.stop();
	    				}
	    				@Override
	    				public void stop() {
	    				}
	    			}, 2);
				}
	
				@Override
				public void levelRequirement() {
					player.getDialogue().sendStatement("You need an agility level of "+levelReq+" to cross this ledge.");
				}
        });
	}
	
	public static boolean swingRope(final Player player, final Position targetPos, final Position failPos, final double EXP, final int levelReq, boolean canFail, final int failTime, final int DMG){
		return DoAgilityEvent(player, new AgilityEvent(levelReq, canFail, failTime){
				@Override
				public void success() {
					player.setStopPacket(true);
					player.getMovementHandler().reset();
					player.resetAnimation();
					player.getUpdateFlags().sendAnimation(751);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {
							if(failCalled)
							{
								container.stop();
								return;
							}
							player.teleport(targetPos);
							player.setStopPacket(false);
							if(EXP > 0){
								player.getSkill().addExp(Skill.AGILITY, EXP);
							}
							player.getMovementHandler().reset();
							player.resetAnimation();
							container.stop();
						}
						@Override
						public void stop() {
						}
					}, 3);
				}
	
				@Override
				public void failure() {
					super.failure();
					player.setStopPacket(true);
	    			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
	    				@Override
	    				public void execute(CycleEventContainer container) {
	    					player.setStopPacket(false);
	    					player.resetAnimation();
	    					player.teleport(failPos);
	    					player.hit(Misc.random(1, DMG), HitType.NORMAL);
	    					container.stop();
	    				}
	    				@Override
	    				public void stop() {
	    				}
	    			}, 2);
				}
	
				@Override
				public void levelRequirement() {
					player.getDialogue().sendStatement("You need an agility level of "+levelReq+" to use this rope swing.");
				}
        });
	}
	
	public static void removeMovementLock(final Player player){
		if (player.getMovementHandler().getMovementLock() == null)
			return;
		player.getMovementHandler().setMovementLock(null);
  	  	player.isCrossingObstacle = false;
  	  	player.setStopPacket(false);
        player.setStandAnim(-1);
        player.setRunAnim(-1);
        player.setWalkAnim(-1);
        player.setAppearanceUpdateRequired(true);
        player.getMovementHandler().reset();
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
                player.getMovementHandler().reset();
				if(EXP > 0){
					player.getSkill().addExp(Skill.AGILITY, EXP);
				}
            }
        };
	}
	
	public static boolean DoAgilityEvent(final Player player, final AgilityEvent event)
	{
		if (!Constants.AGILITY_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return false;
		}
		int agilityLevel = player.getSkill().getLevel()[Constants.SKILL_AGILITY];
		int requiredLevel = event.requiredLevel;
		if(agilityLevel < requiredLevel)
		{
			event.levelRequirement();
			return false;
		}
		boolean successful = true;
		boolean canFail = event.canFail && event.failTime > 0;
		if(canFail)
		{
			event.success();
			
			int weight = (int)player.getWeight();
			int weightModifier = (weight <= 2 ? 1000 : (1000 / (weight / 2)));  //(500 / (weight+1));
			if(!SkillHandler.skillCheck(agilityLevel, requiredLevel, weightModifier))
			{
				successful = false;
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
			successful = true;
			event.success();
		}
		return successful;
	}
}
