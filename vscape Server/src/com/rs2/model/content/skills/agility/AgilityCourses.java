package com.rs2.model.content.skills.agility;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.content.quests.MonkeyMadness.ApeAtoll.GreeGreeData;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class AgilityCourses {
	
	private Player player;
	
	public AgilityCourses(Player player)
	{
		this.player = player;
	}
	
	private int[] courseStage = {
		0, // gnome
		0, // barb
		0, // wild
		0 // ape
	};
	
	public boolean handleCourse(int id, int x, int y)
	{
		if(gnomeCourse(id, x, y)){
			return true;
		}
		if(barbOutpost(id, x, y)){
			return true;
		}
		if(wildyCourse(id,x,y)){
			return true;
		}
		if(apeCourse(id,x,y)){
			return true;
		}
		return false;
	}
	
	private boolean gnomeCourse(int id, int x, int y)
	{
		switch(id)
		{
			case 2295: // gnome log
				Agility.crossLog(player, x, 3429, 8, 1, 7.5);
				setCourseStage(0,1);
			return true;
			case 2285: // gnome net
				Agility.climbNet(player, x, y-1, 1, 1, 7.5);
				if(getCourseStage(0) == 1)
					setCourseStage(0,2);
			return true;
			case 2313: // gnome treebranch
				Agility.climbBranch(player, x, y-1, 2, 1, 5);
				if(getCourseStage(0) == 2)
					setCourseStage(0,3);
			return true;
			case 2312: // gnome rope
				Agility.crossRope(player, 2483, y, 1, 7.5);
				if(getCourseStage(0) == 3)
					setCourseStage(0,4);
			return true;
			case 2314: // gnome branch down
			case 2315: // gnome branch down
				Agility.climbBranch(player, player.getPosition().getX(), player.getPosition().getY(), 0, 1, 5);
				if(getCourseStage(0) == 4)
					setCourseStage(0,5);
			return true;
			case 2286: // gnome net
				if(player.getPosition().getY() < y){
					Agility.climbNet(player, x, y+1, 0, 1, 7.5);
					if(getCourseStage(0) == 5)
						setCourseStage(0,6);
				}
			return true;
			case 154: // gnome pipe
			case 4058:
				if(x >= 2484 && x <= 2487 && y == 3431){
					if(getCourseStage(0) == 6) 
						setCourseStage(0,7);

					if(getCourseStage(0) != 7) {
						Agility.crawlPipe(player, x, 3437, 7, 1, 7.5);
						player.getDialogue().sendStatement("You have completed the course.");
						setCourseStage(0,0);
					}else if(getCourseStage(0) == 7) {
						double bonusXp = (39d);
						Agility.crawlPipe(player, x, 3437, 7, 1, (7.5+bonusXp));
						player.getDialogue().sendStatement("You have completed the course.","You were rewarded "+(bonusXp * Constants.EXP_RATE)+" bonus experience!" );
						setCourseStage(0,0);
					}
				}
			return true;
		}
		return false;
	}
	
	private boolean barbOutpost(int id, int x, int y)
	{
		switch(id)
		{
			case 2282: // barb swing 1
				if(player.getPosition().getY() > y) {
					player.getActionSender().animateObject(x, y, 0, 751);
					if(AgilityHandler.swingRope(player, new Position(x,3549,0), new Position(2550,9950,0), 22, 35, true, 2, 5)){
						setCourseStage(1,1);
					}
				}
			return true;
			case 2294: // barb log 1
				if(player.getPosition().getY() == y) {
					if(AgilityHandler.crossLog(player, new Position(2541,y,0), new Position(2550,3547,0), 13.7, 35, true, 7, 3)){
						if(getCourseStage(1) == 1) 
							setCourseStage(1,2);
					}
				}
			return true;
			case 2284: // barb net 1
				if(player.getPosition().getX() > x) {
					Agility.climbNet(player, x-1, y, 1, 1, 8.2);
					if(getCourseStage(1) == 2) 
						setCourseStage(1,3);
				}
			return true;
			case 2302: // barb ledge 1
				if(player.getPosition().getY() == y && player.getPosition().getX() > x) {
					if(AgilityHandler.crossLedge(player, 3, new Position(2532, y, 1), new Position(2532, 3546, 1), new Position(2534,3545,0), 22, 35, true, 4, 5)){
						if(getCourseStage(1) == 3) 
							setCourseStage(1,4);
					}
				}
			return true;
			case 3205: // barb ladder 1
			    if(player.getPosition().getX() == 2532) {
				Ladders.climbLadder(player, new Position(2532, 3546, 0));
				if(getCourseStage(1) == 4) 
					setCourseStage(1,5);
				return true;
			    }
			return false;
			case 1948: // walls
				//1
				if(player.getPosition().getX() < x)
				{
					if(x == 2536){
						Agility.climbOver(player, 2537, y, 1, 13.7);
						if(getCourseStage(1) == 5) 
							setCourseStage(1,6);
					}
					if(x == 2539){
						Agility.climbOver(player, 2540, y, 1, 13.7);
						if(getCourseStage(1) == 6) 
							setCourseStage(1,7);
					}
					if(x == 2542){
						if(getCourseStage(1) == 7) {
							setCourseStage(1,8);
						}
						if(getCourseStage(1) != 8) {
							Agility.climbOver(player, 2543, y, 1, 13.7);
							player.getDialogue().sendStatement("You have completed the course.");
							setCourseStage(1,0);
						}else if(getCourseStage(1) == 8) {
							double bonusXp = (139.5d);
							Agility.climbOver(player, 2543, y, 1, (13.7+bonusXp));
							player.getDialogue().sendStatement("You have completed the course.","You were rewarded "+(bonusXp * Constants.EXP_RATE)+" bonus experience!" );
							setCourseStage(1,0);
						}
					}
				}
			return true;
		}
		return false;
	}
	
	private boolean wildyCourse(int id, int x, int y)
	{
		switch(id)
		{
			case 2288:
				if(x == 3004 && y == 3938){
					Agility.crawlPipe(player, x, 3950, 14, 49, 12.5);
					setCourseStage(2,1);
				}
				return true;
			case 2283:
				if(x == 3005 && y == 3952 && player.getPosition().getY() < 3954){
					Agility.swingRope(player, x, 3958, 49, 20);
					if(getCourseStage(2) == 1) 
						setCourseStage(2,2);
				}
				return true;
			case 2311:
				if(x == 3001 && y == 3960){
					Agility.crossSkippingStones(player, 2996, y, 8, 49, 20);
					if(getCourseStage(2) == 2) 
						setCourseStage(2,3);
				}
				return true;
			case 2297:
				if(x == 3001 && y == 3945){
					Agility.crossLog(player, 2994, y, 11, 49, 20);
					if(getCourseStage(2) == 3) 
						setCourseStage(2,4);
				}
				return true;
			case 2328:
				if(x >= 2993 && x <= 2995 && y == 3936){
					if(getCourseStage(2) == 4) {
						setCourseStage(2,5);
					}
					if(getCourseStage(2) != 5) {
						Agility.crossObstacle(player, x, 3933, 844, 7, 1, 0);
						player.getDialogue().sendStatement("You have completed the course.");
						setCourseStage(2,0);
					}else if(getCourseStage(2) == 5) {
						double bonusXp = (499d);
						Agility.crossObstacle(player, x, 3933, 844, 7, 1, bonusXp);
						player.getDialogue().sendStatement("You have completed the course.","You were rewarded "+(bonusXp * Constants.EXP_RATE)+" bonus experience!" );
						setCourseStage(2,0);
					}
				}
				return true;
		}
		return false;
	}
	
	private boolean apeCourse(int id, int x, final int y)
	{
		if(canDoApe(id)){
			switch(id)
			{
				case 12568 : // jump stone
				    player.setStopPacket(true);
					if(x == 2754 && y == 2742){
					    if(player.transformNpc != 1480 && player.transformNpc != 1481) {
						player.getDialogue().sendStatement("You are not in the agile monkey form needed for this course.");
						player.getDialogue().endDialogue();
						return true;
					    }
					    final boolean westCrossing = player.getPosition().getX() > x;
					    player.getUpdateFlags().sendFaceToDirection(new Position(westCrossing ? 2753 : 2755, y, 0));
					    player.getUpdateFlags().setFaceToDirection(true);
					    CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						int count = 0;
						@Override
						public void execute(CycleEventContainer b) {
						    if (count == 1) {
							b.stop();
						    } else {
							Agility.crossObstacle(player, 2754, y, 3481, 3, 65, 20);
							count++;
						    }
						}

						@Override
						public void stop() {
						    Agility.crossObstacle(player, westCrossing ? 2753 : 2755, y, 3481, 3, 65, 20);
						    setCourseStage(3, 1);
						    player.setStopPacket(false);
						}
					    }, 4);
					}
					return true;
				case 12570 : // tropical tree climb
					if(x == 2753 && y == 2741) {
					    if(player.transformNpc != 1480 && player.transformNpc != 1481) {
						player.getDialogue().sendStatement("You are not in the agile monkey form needed for this course.");
						player.getDialogue().endDialogue();
						return true;
					    }
					    if (player.getSkill().getLevel()[Skill.AGILITY] < 65) {
						player.getDialogue().sendStatement("You need an agility level of " + 65 + " to climb this tree.");
						return true;
					    }
					    player.setStopPacket(true);
					    player.getUpdateFlags().setFace(new Position(x, y, 0));
					    player.getUpdateFlags().setUpdateRequired(true);
					    player.getUpdateFlags().sendAnimation(3487, 0);
					    CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
						    b.stop();
						}

						@Override
						public void stop() {
						    Agility.climbTree(player, 2753, 2742, 2, 3487, 0, 65, 40);
						    if(getCourseStage(3) == 1)
							setCourseStage(3, 2);
						    player.setStopPacket(false);
						}
					    }, 3);
					}
					return true;
				case 12573 : // monkey bars
					if(x == 2752 && y == 2741){
					    if(player.transformNpc != 1480 && player.transformNpc != 1481) {
						player.getDialogue().sendStatement("You are not in the agile monkey form needed for this course.");
						player.getDialogue().endDialogue();
						return true;
					    }
						Agility.crossMonkeyBars(player, 2747, y, 0, 3483, 6, 65, 40);
						if(getCourseStage(3) == 2)
							setCourseStage(3, 3);
					}
					return true;
				case 12576 : // rock climb
					if(x == 2746 && y == 2741){
					    if(player.transformNpc != 1480 && player.transformNpc != 1481) {
						player.getDialogue().sendStatement("You are not in the agile monkey form needed for this course.");
						player.getDialogue().endDialogue();
						return true;
					    }
						if(player.getPosition().getX() >= x)
						{
							Agility.crossObstacle(player, 2743, y, 3485, 5, 65, 60);
							if(getCourseStage(3) == 3)
								setCourseStage(3, 4);
						}
					}
					return true;
				case 12578 : // swing
					if(x == 2752 && y == 2731){
					    if(player.transformNpc != 1480 && player.transformNpc != 1481) {
						player.getDialogue().sendStatement("You are not in the agile monkey form needed for this course.");
						player.getDialogue().endDialogue();
						return true;
					    }
						if(player.getPosition().getX() <= x)
						{
							Agility.swingRope(player, 2756, y, 3488, 65, 100);
							if(getCourseStage(3) == 4)
								setCourseStage(3, 5);
						}
					}
					return true;	
				case 12618 : // rope down (couldnt find right anim)
					if(x == 2757 && y == 2734){
					    if(player.transformNpc != 1480 && player.transformNpc != 1481) {
						player.getDialogue().sendStatement("You are not in the agile monkey form needed for this course.");
						player.getDialogue().endDialogue();
						return true;
					    }
						player.teleport(new Position(2758, 2735, 1));
						player.setStopPacket(true);
						CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						    @Override
						    public void execute(CycleEventContainer b) {
							b.stop();
						    }

						    @Override
						    public void stop() {
							Agility.crossTreeRope(player, 2770, 2747, 3494, 14, 65, 100);
							CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
							    @Override
							    public void execute(CycleEventContainer b) {
								b.stop();
							    }

							    @Override
							    public void stop() {
								player.teleport(new Position(2770, 2747, 0));
								player.setStopPacket(false);
								if(getCourseStage(3) == 5)
									setCourseStage(3, 6);
								if(getCourseStage(3) == 6) {
									double bonusXp = (200d);
									player.getSkill().addExp(Skill.AGILITY, bonusXp);
									player.getDialogue().sendStatement("You have completed the course.","You were rewarded "+(bonusXp * Constants.EXP_RATE)+" bonus experience!" );
									setCourseStage(3,0);
								} else {
									player.getDialogue().sendStatement("You have completed the course.");
									setCourseStage(3,0);
								}
							    }
							}, 14);
						    }
						}, 1);
					}
					return true;	
			}
		}
		return false;
	}
	private boolean canDoApe(int object){
		switch(object)
		{
			case 12568 : // jump stone
			case 12570 : // tropical tree climb
			case 12573 : // monkey bars
			case 12576 : // rock climb
			case 12578 : // swing
			case 12618 : // rope down
				GreeGreeData g = GreeGreeData.forItemId(player.getEquipment().getId(Constants.WEAPON));
				if(g == null)
				{
					player.getActionSender().sendMessage("You need to be wielding a monkey GreeGree to do this course.");
					return false;
				}else{
					if(g.getItemId() != 4024 && g.getItemId() != 4025)
					{
						player.getActionSender().sendMessage("This Monkey is not agile enough to do this course.");
						return false;
					}
					if(g.getItemId() == 4024 || g.getItemId() == 4025)
					{
						if(player.transformNpc != g.getTransformId()){
							player.getActionSender().sendMessage("Requip the GreeGree to transform into a monkey.");
							return false;
						}
					}
				}
			return true;
		}
		return false;
	}
	
	
	public void setCourseStage(int course, int stage){
		courseStage[course] = stage;
	}
	
	public int getCourseStage(int course){
		return courseStage[course];
	}
}
