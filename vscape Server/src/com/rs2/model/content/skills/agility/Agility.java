package com.rs2.model.content.skills.agility;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.content.quests.impl.MonkeyMadness.ApeAtoll.GreeGreeData;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class Agility {

	public static void crossObstacle(final Player player,final int x,final int y,final int anim,final int time, final double xp){
		player.isCrossingObstacle = true;
		player.setStopPacket(true);
		final boolean wasRunning = player.getMovementHandler().isRunToggled();
		player.getMovementHandler().setRunToggled(false);
		player.getMovementHandler().reset();
		player.resetAnimation();
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (anim > 0) {
					player.setRunAnim(anim);
					player.setWalkAnim(anim);
					player.setAppearanceUpdateRequired(true);
				}
				player.getActionSender().walkTo2(x,y,time,true);
				container.stop();
			}
			@Override
			public void stop() {
			}
		}, 1);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.setStopPacket(false);
				GreeGreeData g = GreeGreeData.forItemId(player.getEquipment().getId(Constants.WEAPON));
				if(g != null)
				{
					player.setRunAnim(g.getWalkAnim());
					player.setWalkAnim(g.getWalkAnim());
				}else{
					player.setRunAnim(-1);
					player.setWalkAnim(-1);
				}
				player.setAppearanceUpdateRequired(true);
				if(xp > 0){
					player.getSkill().addExp(Skill.AGILITY, xp);
				}
				if(wasRunning)
				{
					player.getMovementHandler().setRunToggled(true);
				}
				player.resetAnimation();
				player.isCrossingObstacle = false;
				container.stop();
			}
			@Override
			public void stop() {
			}
		}, time+1);
	}

	public static void crossObstacle(final Player player,final int x,final int y,final int startAnim, final int time, final int level, final double xp){
		if (!Constants.AGILITY_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if (player.getSkill().getLevel()[Skill.AGILITY] < level) {
			player.getDialogue().sendStatement("You need an agility level of "+level+" to use this shortcut.");
			return;
		}
		player.isCrossingObstacle = true;
		player.setStopPacket(true);
		final boolean wasRunning = player.getMovementHandler().isRunToggled();
		player.getMovementHandler().setRunToggled(false);
		player.getMovementHandler().reset();
		player.resetAnimation();
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (startAnim > 0) {
					player.setRunAnim(startAnim);
					player.setWalkAnim(startAnim);
					player.setAppearanceUpdateRequired(true);
				}
				player.getActionSender().walkTo2(x,y,time,true);
				player.getUpdateFlags().sendFaceToDirection(new Position(player.getPosition().getX(),player.getPosition().getY()+1));
				container.stop();
			}
			@Override
			public void stop() {
			}
		}, 1);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.setStopPacket(false);
				GreeGreeData g = GreeGreeData.forItemId(player.getEquipment().getId(Constants.WEAPON));
				if(g != null)
				{
					player.setRunAnim(g.getWalkAnim());
					player.setWalkAnim(g.getWalkAnim());
				}else{
					player.setRunAnim(-1);
					player.setWalkAnim(-1);
				}
				player.setAppearanceUpdateRequired(true);
				if(xp > 0){
					player.getSkill().addExp(Skill.AGILITY, xp);
				}
				if(wasRunning)
				{
					player.getMovementHandler().setRunToggled(true);
				}
				player.resetAnimation();
				player.isCrossingObstacle = false;
				container.stop();
			}
			@Override
			public void stop() {
			}
		}, time+1);
	}
	
	public static void jumpRock(final Player player,final int x,final int y,final int startAnim, final int time, final int level, final double xp){
		if (!Constants.AGILITY_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if (player.getSkill().getLevel()[Skill.AGILITY] < level) {
			player.getDialogue().sendStatement("You need an agility level of "+level+" to use this shortcut.");
			return;
		}
		player.isCrossingObstacle = true;
		player.setStopPacket(true);
		player.getMovementHandler().setRunToggled(true);
		player.getMovementHandler().reset();
		player.resetAnimation();
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (startAnim > 0) {
					player.setRunAnim(startAnim);
					player.setAppearanceUpdateRequired(true);
				}
				player.getActionSender().walkTo2(x,y,time,true);
				player.getUpdateFlags().sendFaceToDirection(new Position(player.getPosition().getX(),player.getPosition().getY()+1));
				container.stop();
			}
			@Override
			public void stop() {
			}
		}, 1);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.setStopPacket(false);
				player.setRunAnim(-1);
				player.setWalkAnim(-1);
				player.setAppearanceUpdateRequired(true);
				if(xp > 0){
					player.getSkill().addExp(Skill.AGILITY, xp);
				}
				player.resetAnimation();
				player.isCrossingObstacle = false;
				container.stop();
			}
			@Override
			public void stop() {
			}
		}, time+1);
	}
	
	public static void climbObstacle(final Player player,final int x,final int y,final int z,final int anim,final int time, final double xp){
		player.setStopPacket(true);
		player.getMovementHandler().reset();
		player.resetAnimation();
		if(anim > 0){
			player.getUpdateFlags().sendAnimation(anim);
		}
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.teleport(new Position(x,y,z));
				player.setStopPacket(false);
				if(xp > 0){
					player.getSkill().addExp(Skill.AGILITY, xp);
				}
				player.getMovementHandler().reset();
				player.resetAnimation();
				container.stop();
			}
			@Override
			public void stop() {
			}
		}, time+1);
	}

	public static void crossLog(Player player, int x, int y, int time, int level, double xp)
	{
		if (!Constants.AGILITY_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if (player.getSkill().getLevel()[Skill.AGILITY] < level) {
			player.getDialogue().sendStatement("You need an agility level of "+level+" to cross this log.");
			return;
		}
		crossObstacle(player, x, y, 762, time, xp);
	}
	
	public static void crossSkippingStones(Player player, int x, int y, int time, int level, double xp)
	{
		if (!Constants.AGILITY_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if (player.getSkill().getLevel()[Skill.AGILITY] < level) {
			player.getDialogue().sendStatement("You need an agility level of "+level+" to cross these stones.");
			return;
		}
		crossObstacle(player, x, y, 769, time, xp);
	}
	
	public static void crossRope(Player player, int x, int y, int level, double xp)
	{
		if (!Constants.AGILITY_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if (player.getSkill().getLevel()[Skill.AGILITY] < level) {
			player.getDialogue().sendStatement("You need an agility level of "+level+" to cross this log.");
			return;
		}
		crossObstacle(player, x, y, 762, 7, xp);
	}
	
	public static void crawlPipe(Player player, int x, int y, int time, int level, double xp)
	{
		if (!Constants.AGILITY_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if (player.getSkill().getLevel()[Skill.AGILITY] < level) {
			player.getDialogue().sendStatement("You need an agility level of "+level+" to crawl through this pipe.");
			return;
		}
		crossObstacle(player, x, y, 749, time, xp);
	}
	
	public static void climbTree(Player player, int x, int y, int z, int anim, int time, int level, double xp)
	{
		if (!Constants.AGILITY_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if (player.getSkill().getLevel()[Skill.AGILITY] < level) {
			player.getDialogue().sendStatement("You need an agility level of "+level+" to climb this tree.");
			return;
		}
		climbObstacle(player, x, y, z, anim, time, xp);
	}
	
	public static void climbNet(Player player, int x, int y, int z, int level, double xp)
	{
		if (!Constants.AGILITY_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if (player.getSkill().getLevel()[Skill.AGILITY] < level) {
			player.getDialogue().sendStatement("You need an agility level of "+level+" to climb this net.");
			return;
		}
		climbObstacle(player, x, y, z, 828, 2, xp);
	}
	
	public static void climbBranch(Player player, int x, int y, int z, int level, double xp)
	{
		if (!Constants.AGILITY_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if (player.getSkill().getLevel()[Skill.AGILITY] < level) {
			player.getDialogue().sendStatement("You need an agility level of "+level+" to climb this.");
			return;
		}
		climbObstacle(player, x, y, z, 828, 2, xp);
	}
	
	public static void crossLedge(Player player, int x, int y, int ledgeFace, int time, int level, double xp) {
		if (!Constants.AGILITY_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if (player.getSkill().getLevel()[Skill.AGILITY] < level) {
			player.getDialogue().sendStatement("You need an agility level of "+level+" to cross this ledge.");
			return;
		}
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
		crossObstacle(player, x, y, anim, time, xp);
	}
	
	public static void crossMonkeyBars(Player player, int x, int y, int z, int anim, int time, int level, double xp) {
		if (!Constants.AGILITY_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if (player.getSkill().getLevel()[Skill.AGILITY] < level) {
			player.getDialogue().sendStatement("You need an agility level of "+level+" to cross these bars.");
			return;
		}
		crossObstacle(player, x, y, anim, time, xp);
		climbObstacle(player, x, y, z, anim, time, 0);
	}
	
	public static void crossMonkeyBars(Player player, int x, int y, int level, double xp) {
		if (!Constants.AGILITY_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if (player.getSkill().getLevel()[Skill.AGILITY] < level) {
			player.getDialogue().sendStatement("You need an agility level of "+level+" to cross these bars.");
			return;
		}
		crossObstacle(player, x, y, 744, 7, xp);
	}
	
	public static void crossTreeRope(Player player, int x, int y, int anim, int time, int level, double xp) {
		if (!Constants.AGILITY_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if (player.getSkill().getLevel()[Skill.AGILITY] < level) {
			player.getDialogue().sendStatement("You need an agility level of "+level+" to climb down this.");
			return;
		}
		crossObstacle(player, x, y, anim, time, xp);
	}
	
	public static void swingRope(final Player player,final int x,final int y, final int anim, final int level,final double xp) {
		if (!Constants.AGILITY_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if (player.getSkill().getLevel()[Skill.AGILITY] < level) {
			player.getDialogue().sendStatement("You need an agility level of "+level+" to swing across this.");
			return;
		}
		player.setStopPacket(true);
		player.getMovementHandler().reset();
		player.resetAnimation();
		player.getUpdateFlags().sendAnimation(anim);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.teleport(new Position(x,y,0));
				player.setStopPacket(false);
				if(xp > 0){
					player.getSkill().addExp(Skill.AGILITY, xp);
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
	
	public static void swingRope(final Player player,final int x,final int y,final int level,final double xp) {
		if (!Constants.AGILITY_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if (player.getSkill().getLevel()[Skill.AGILITY] < level) {
			player.getDialogue().sendStatement("You need an agility level of "+level+" to swing across this.");
			return;
		}
		player.setStopPacket(true);
		player.getMovementHandler().reset();
		player.resetAnimation();
		player.getUpdateFlags().sendAnimation(751);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.teleport(new Position(x,y,0));
				player.setStopPacket(false);
				if(xp > 0){
					player.getSkill().addExp(Skill.AGILITY, xp);
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
	
	public static void climbOver(Player player, int x, int y, int level, double xp) {
		if (!Constants.AGILITY_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if (player.getSkill().getLevel()[Skill.AGILITY] < level) {
			player.getDialogue().sendStatement("You need an agility level of "+level+" to climb over this.");
			return;
		}
		crossObstacle(player, x, y, 839, 2, xp);
	}
}
