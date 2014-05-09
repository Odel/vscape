package com.rs2.model.content.skills.agility;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class Agility {

	public static void crossObstacle(final Player player,final int x,final int y,final int anim,final int time, final double xp){
		player.getMovementHandler().reset();
		final boolean wasRunning = player.getMovementHandler().isRunToggled();
		if(wasRunning)
		{
			player.getMovementHandler().setRunToggled(false);
		}
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
				player.setRunAnim(-1);
				player.setWalkAnim(-1);
				player.setAppearanceUpdateRequired(true);
				if(xp > 0){
					player.getSkill().addExp(Skill.AGILITY, xp);
				}
				if(wasRunning)
				{
					player.getMovementHandler().setRunToggled(true);
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
	
	public static void crossObstacle(final Player player,final int x,final int y,final int startAnim, final int endAnim,final int time, final double xp){
		player.getMovementHandler().reset();
		final boolean wasRunning = player.getMovementHandler().isRunToggled();
		if(wasRunning)
		{
			player.getMovementHandler().setRunToggled(false);
		}
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
				container.stop();
			}
			@Override
			public void stop() {
			}
		}, 1);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.setRunAnim(-1);
				player.setWalkAnim(-1);
				player.setAppearanceUpdateRequired(true);
				if(xp > 0){
					player.getSkill().addExp(Skill.AGILITY, xp);
				}
				if(wasRunning)
				{
					player.getMovementHandler().setRunToggled(true);
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
	
	public static void climbObstacle(final Player player,final int x,final int y,final int z,final int anim,final int time, final double xp){
		player.setStopPacket(true);
		player.getMovementHandler().reset();
		player.resetAnimation();
		player.getUpdateFlags().sendAnimation(anim);
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
	
	public static void crossLog(Player player, int x, int y, int level, double xp)
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
	
	public static void crawlPipe(Player player, int x, int y, int level, double xp)
	{
		if (!Constants.AGILITY_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if (player.getSkill().getLevel()[Skill.AGILITY] < level) {
			player.getDialogue().sendStatement("You need an agility level of "+level+" to crawl through this pipe.");
			return;
		}
		crossObstacle(player, x, y, 746, 748, 7, xp);
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
			player.getDialogue().sendStatement("You need an agility level of "+level+" to climb this branch.");
			return;
		}
		climbObstacle(player, x, y, z, 828, 2, xp);
	}
}
