package com.rs2.model.content.skills.agility;

import com.rs2.model.Position;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.players.Player;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

import java.util.Random;

public class ShortcutHandler {

    public static boolean handleShortcut(final Player player, int id, int x, int y) {
	int targetX = 0;
	int targetY = 0;
	int objectFace = SkillHandler.getFace(id, x, y, player.getPosition().getZ());
	switch (id) {
	    case 2296: //coal trucks log
		if (x == 2599) {
		    Agility.crossLog(player, 2603, 3477, 8, 20, 10);
		} else if (x == 2602) {
		    Agility.crossLog(player, 2598, 3477, 8, 20, 10);
		}
		return true;
	    case 2332: //karamja log
		if (x == 2909) {
		    Agility.crossLog(player, 2906, 3049, 8, 20, 5);
		} else if (x == 2907) {
		    Agility.crossLog(player, 2910, 3049, 6, 8, 5);
		}
		return true;
	    case 9311:
	    case 9312: //varrock underwall tunnel
		if (x == 3189) {
		    Agility.crossObstacle(player, 3193, 3493, 844, 6, 21, 8);
		} else if (x == 3192) {
		    Agility.crossObstacle(player, 3188, 3493, 844, 6, 21, 8);
		}
		return true;
	    case 9309:
	    case 9310: //fally underwall tunnel
		if (y == 3312) {
		    Agility.crossObstacle(player, 2948, 3309, 844, 6, 26, 8);
		} else if (y == 3310) {
		    Agility.crossObstacle(player, 2948, 3313, 844, 6, 26, 8);
		}
		return true;
	    case 9315: //jumping rocks to champs guild from draynor manor
		if (player.getPosition().getX() == 3149) {
		    Agility.crossObstacle(player, 3150, 3363, 769, 5, 29, 9);
		} else if (player.getPosition().getX() == 3150) {
		    Agility.crossObstacle(player, 3151, 3363, 769, 5, 29, 9);
		} else if (player.getPosition().getX() == 3151 && x == 3152) {
		    Agility.crossObstacle(player, 3152, 3363, 769, 5, 29, 9);
		} else if (player.getPosition().getX() == 3151 && x == 3150) {
		    Agility.crossObstacle(player, 3150, 3363, 769, 5, 29, 9);
		} else if (player.getPosition().getX() == 3152 && x == 3153) {
		    Agility.crossObstacle(player, 3153, 3363, 769, 5, 29, 9);
		} else if (player.getPosition().getX() == 3152 && x == 3151) {
		    Agility.crossObstacle(player, 3151, 3363, 769, 5, 29, 9);
		} else if (player.getPosition().getX() == 3153) {
		    Agility.crossObstacle(player, 3152, 3363, 769, 5, 29, 9);
		} else if (player.getPosition().getX() == 3154) {
		    Agility.crossObstacle(player, 3153, 3363, 769, 5, 29, 9);
		}
		return true;
	    case 9316:
	    case 9317: //grand tree nw rocks
		if (x == 2487) {
		    Agility.crossObstacle(player, 2489, 3521, 844, 10, 36, 13);
		} else if (x == 2489) {
		    Agility.crossObstacle(player, 2486, 3515, 844, 10, 36, 13);
		}
		return true;
	    case 9331:
	    case 9332: //ak mine rockclimb
		if (x == 3303) {
		    Agility.crossObstacle(player, 3306, 3315, 844, 6, 37, 13);
		} else if (x == 3305) {
		    Agility.crossObstacle(player, 3302, 3315, 820, 6, 37, 13);
		}
		return true;
	    case 9325: //dwarf mine crevice
		if (x == 3029) {
		    Agility.crossObstacle(player, 3035, 9806, 844, 8, 41, 15);
		} else if (x == 3034) {
		    Agility.crossObstacle(player, 3028, 9806, 844, 8, 41, 15);
		}
		return true;
	    case 9322:
	    case 9324: //frem log cross
		if (y == 3595) {
		    Agility.crossLog(player, 2722, 3592, 6, 47, 20);
		} else if (y == 3593) {
		    Agility.crossLog(player, 2722, 3596, 6, 47, 20);
		}
		return true;
	    case 9295: //moss giant pipe
		if (x == 3150) {
		    Agility.crawlPipe(player, 3155, 9906, 6, 48, 20);
		} else if (x == 3153) {
		    Agility.crawlPipe(player, 3149, 9906, 6, 48, 20);
		}
		return true;
	    case 9319:
	    case 9320: //slayer tower spike chains
		if (x == 3422 && player.getPosition().getZ() == 0) {
		    Agility.climbBranch(player, 3423, 3550, 1, 59, 25);
		} else if (x == 3422 && player.getPosition().getZ() == 1) {
		    Agility.climbBranch(player, 3423, 3550, 0, 59, 25);
		} else if (x == 3447 && player.getPosition().getZ() == 1) {
		    Agility.climbBranch(player, 3448, 3576, 2, 71, 47);
		} else if (x == 3447 && player.getPosition().getZ() == 2) {
		    Agility.climbBranch(player, 3448, 3576, 1, 71, 47);
		}
		return true;
	    case 9321: //slayer dungeon crevice 61
		if (x == 2734) {
		    Agility.crossObstacle(player, 2730, 10008, 844, 6, 61, 35);
		} else if (x == 2731) {
		    Agility.crossObstacle(player, 2735, 10008, 844, 6, 61, 35);
		}
		return true;
	    case 9334:
	    case 9335:
	    case 9336:
	    case 9337: //canifis/morytania shortcut 64
		if (x == 3424 && player.getPosition().getX() == 3423) {
		    Agility.crossObstacle(player, 3424, 3476, 756, 4, 64, 35);
		} else if (x == 3424 && player.getPosition().getX() == 3424) {
		    Agility.crossObstacle(player, 3423, 3476, 756, 4, 64, 35);
		} else if (x == 3425 && y == 3476) {
		    Agility.crossObstacle(player, 3427, 3476, 820, 6, 64, 35);
		} else if (x == 3426) {
		    Agility.crossObstacle(player, 3424, 3476, 820, 6, 64, 35);
		} else if (x == 3425 && player.getPosition().getY() == 3483) {
		    Agility.crossObstacle(player, 3425, 3484, 756, 4, 64, 35);
		} else if (x == 3425 && player.getPosition().getY() == 3484) {
		    Agility.crossObstacle(player, 3425, 3483, 756, 4, 64, 35);
		}
		return true;
	    case 9293: //taverly dung blue drag pipe
		if (x == 2887) {
		    Agility.crawlPipe(player, 2892, 9799, 6, 70, 47);
		} else if (player.getPosition().getX() == 2891) {
		    Agility.crawlPipe(player, 2886, 9799, 6, 70, 47);
		}
		return true;
	    case 9294: //taverly spike hop
		if (player.getPosition().getX() == 2880) {
		    Agility.climbOver(player, 2878, 9813, 80, 0);
		} else if (player.getPosition().getX() == 2878) {
		    Agility.climbOver(player, 2880, 9813, 80, 0);
		}
		return true;
	    case 2333:
	    case 2334:
	    case 2335: //shilo rock jump 74
		if (player.getPosition().getY() == 2948) {
		    Agility.crossObstacle(player, 2925, 2949, 769, 5, 74, 50);
		} else if (player.getPosition().getY() == 2950) {
		    Agility.crossObstacle(player, 2925, 2949, 769, 5, 74, 50);
		} else if (player.getPosition().getY() == 2947) {
		    Agility.crossObstacle(player, 2925, 2948, 769, 5, 74, 50);
		} else if (player.getPosition().getY() == 2949 && y == 2948) {
		    Agility.crossObstacle(player, 2925, 2948, 769, 5, 74, 50);
		} else if (player.getPosition().getY() == 2949 && y == 2950) {
		    Agility.crossObstacle(player, 2925, 2950, 769, 5, 74, 50);
		} else if (player.getPosition().getY() == 2951) {
		    Agility.crossObstacle(player, 2925, 2950, 769, 5, 74, 50);
		}
		return true;
	    case 9326: //slayer dungeon 81 floor
		if (x == 2774 && player.getPosition().getX() == 2775) {
		    Agility.crossObstacle(player, 2773, 10003, 769, 5, 81, 56);
		} else if (x == 2774 && player.getPosition().getX() == 2773) {
		    Agility.crossObstacle(player, 2775, 10003, 769, 5, 81, 56);
		} else if (x == 2769 && player.getPosition().getX() == 2770) {
		    Agility.crossObstacle(player, 2768, 10002, 769, 5, 81, 56);
		} else if (x == 2769 && player.getPosition().getX() == 2768) {
		    Agility.crossObstacle(player, 2770, 10002, 769, 5, 81, 56);
		}
		return true;

	    case 2618: // lumberyard fence (russian fence)
		targetY = player.getPosition().getY() < y ? 1 : -1;
		Agility.crossObstacle(player, x, y + targetY, 839, 2, 0);
		return true;
	    case 9300: // lumby to varrock fence (the castle wall one)
		targetY = player.getPosition().getY() < y ? 1 : -1;
		Agility.crossObstacle(player, x, y + targetY, 2750, 2, 0);
		return true;
	    case 12982: // stile
		targetY = player.getPosition().getY() < y ? 2 : -1;
		Agility.crossObstacle(player, x, y + targetY, 839, 3, 0);
		return true;
	    case 2324: //ogre rope swing
		player.getActionSender().sendMessage("You attempt to swing on the rope swing...");
		player.fadeTeleport(new Position(2511, player.getPosition().getY() < 3095 ? 3096 : 3091, 0));
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		    @Override
		    public void execute(CycleEventContainer b) {
			b.stop();
		    }

		    @Override
		    public void stop() {
			player.getActionSender().sendMessage("...you make it safely across.");
		    }
		}, 4);
		return true;
	    case 3790:
	    case 3791: //death plateau rocks
	    case 3748:
	    case 3723:
	    case 3722:
		if (x == 2879) {
		    Agility.crossObstacle(player, 2877, 3623, 844, 6, 25, 1);
		} else if (x == 2878) {
		    Agility.crossObstacle(player, 2880, 3622, 844, 6, 25, 1);
		} else if (x == 2860) {
		    Agility.crossObstacle(player, 2858, 3626, 844, 6, 25, 1);
		} else if (x == 2859) {
		    Agility.crossObstacle(player, 2861, 3626, 844, 6, 25, 1);
		} else if (y == 3612 && player.getPosition().getY() == 3613) {
		    Agility.crossObstacle(player, 2856, 3611, 844, 3, 25, 1);
		} else if (y == 3612 && player.getPosition().getY() == 3611) {
		    Agility.crossObstacle(player, 2856, 3613, 844, 3, 25, 1);
		} else if (y == 3628 && player.getPosition().getY() == 3629) {
		    Agility.crossObstacle(player, 2833, 3627, 844, 3, 25, 1);
		} else if (y == 3628 && player.getPosition().getY() == 3627) {
		    Agility.crossObstacle(player, 2833, 3629, 844, 3, 25, 1);
		} else if (x == 2821) {
		    if (player.getPosition().getX() == 2822) {
			Agility.crossObstacle(player, 2820, 3635, 844, 3, 25, 1);
		    } else if (player.getPosition().getX() == 2820) {
			Agility.crossObstacle(player, 2822, 3635, 844, 3, 25, 1);
		    }
		} else if (y == 3595) {
		    Agility.crossObstacle(player, 2880, 3593, 844, 6, 25, 1);
		} else if (y == 3594) {
		    Agility.crossObstacle(player, 2880, 3596, 844, 6, 25, 1);
		}
		return true;
	    case 11844: //falador wall shortcut
		if (player.getPosition().getX() == 2934) {
		    Agility.climbOver(player, 2936, 3355, 11, 5);
		} else if (player.getPosition().getX() == 2936) {
		    Agility.climbOver(player, 2934, 3355, 11, 5);
		}
		return true;
	    case 9301:
	    case 9302: //watchtower tunnel
		if (y == 3111) {
		    Agility.crossObstacle(player, 2575, 3107, 844, 6, 16, 7);
		} else if (y == 3108) {
		    Agility.crossObstacle(player, 2575, 3112, 844, 6, 16, 7);
		}
		return true;
	    case 12127: //Zanaris / cosmic altar shortcuts
		if (x == 2400) {
		    if (75 >= new Random().nextDouble() * 100) {
			Agility.crossObstacle(player, 2400, player.getPosition().getY() > 4402 ? 4402 : 4404, player.getPosition().getY() > 4401 ? 756 : 754, 4, 46, 12.5);
		    } else {
			Agility.crossObstacle(player, 2400, player.getPosition().getY() > 4402 ? 4402 : 4404, player.getPosition().getY() > 4401 ? 756 : 754, 4, 46, 12.5);
			if (player.getSkill().getLevel()[Skill.AGILITY] >= 46) {
			    player.hit(4, HitType.NORMAL);
			    player.getActionSender().sendMessage("You fail to cross the obstacle safely!");
			}
		    }
		} else {
		    if (player.getPosition().getY() > 4396) {
			if (75 >= new Random().nextDouble() * 100) {
			    Agility.crossObstacle(player, 2415, player.getPosition().getY() > 4401 ? 4401 : 4403, player.getPosition().getY() > 4401 ? 756 : 754, 4, 66, 22.5);
			} else {
			    Agility.crossObstacle(player, 2415, player.getPosition().getY() > 4401 ? 4401 : 4403, player.getPosition().getY() > 4401 ? 756 : 754, 4, 66, 22.5);
			    if (player.getSkill().getLevel()[Skill.AGILITY] >= 66) {
				player.hit(4, HitType.NORMAL);
				player.getActionSender().sendMessage("You fail to cross the obstacle safely!");
			    }
			}
		    } else {
			if (75 >= new Random().nextDouble() * 100) {
			    Agility.crossObstacle(player, 2408, player.getPosition().getY() > 4394 ? 4394 : 4396, player.getPosition().getY() > 4394 ? 756 : 754, 4, 66, 22.5);
			} else {
			    Agility.crossObstacle(player, 2408, player.getPosition().getY() > 4394 ? 4394 : 4396, player.getPosition().getY() > 4394 ? 756 : 754, 4, 66, 22.5);
			    if (player.getSkill().getLevel()[Skill.AGILITY] >= 66) {
				player.hit(4, HitType.NORMAL);
				player.getActionSender().sendMessage("You fail to cross the obstacle safely!");
			    }
			}
		    }
		}
		return true;
	    case 2231: // rockslide
		if (x >= 2791 && x <= 2795 && y >= 2978 && y <= 2980) {
		    targetX = player.getPosition().getX() < x ? 2795 : 2791;
		    Agility.crossObstacle(player, targetX, y, 844, 4, 0);
		}
		return true;
	    //Arandar
	    case 9296:
		if (y == 3299)//first rocks down
		{
		    Agility.crossObstacle(player, 2345, 3294, 844, 7, 59, 0);
		}
		if (y == 3288)//second rocks down
		{
		    Agility.crossObstacle(player, 2338, 3286, 844, 7, 68, 0);
		}
		if (y == 3252) //third rocks down
		{
		    Agility.crossObstacle(player, 2338, 3253, 844, 7, 85, 0);
		}
		return true;
	    case 9297:
		if (y == 3295) //first rocks up
		{
		    Agility.crossObstacle(player, 2345, 3300, 844, 7, 59, 0);
		}
		if (y == 3285) //second rocks up
		{
		    Agility.crossObstacle(player, 2338, 3281, 844, 7, 68, 0);
		}
		if (y == 3253) //third rocks up
		{
		    Agility.crossObstacle(player, 2332, 3252, 844, 7, 85, 0);
		}
		return true;
	    case 9307: //ectopool shortcut
		if (x == 3670 && y == 9888 && player.getSkill().getLevel()[Skill.AGILITY] >= 53) //from top
		{
		    player.teleport(new Position(3670, 9888, 3));
		} else {
		    player.getActionSender().sendMessage("This shortcut requires level 53 Agility.");
		}
		return true;
	    case 9308: //ectopool shortcut
		if (x == 3670 && y == 9888 && player.getSkill().getLevel()[Skill.AGILITY] >= 53) //from bottom
		{
		    player.teleport(new Position(3671, 9888, 2));
		} else {
		    player.getActionSender().sendMessage("This shortcut requires level 53 Agility.");
		}
		return true;
	    case 2320: //edgeville dungeon monkey bars
		if (x == 3119 || x == 3120) {
		    if (y == 9964) {
			Agility.crossMonkeyBars(player, player.getPosition().getX(), 9969, 15, 0);
		    }
		    if (y == 9969) {
			Agility.crossMonkeyBars(player, player.getPosition().getX(), 9964, 0, 0);
		    }
		}
		return true;
	    case 2287: //barbarian agility pipe entrance
		if (x == 2552 && y == 3559) {
		    targetY = player.getPosition().getY() > y ? 3558 : 3561;
		    Agility.crawlPipe(player, x, targetY, 3, 35, 0);
		}
		return true;	
	    //Tirannwn
		//Isafdar
	    case 3921: //tripwire
	 //   	System.out.println(objectFace);
	    	switch(objectFace)
	    	{
	    		case 0 :
	    		case 2 :
				    targetY = player.getPosition().getY() > y ? -2 : 2;
				    Agility.crossObstacle(player, x, y + targetY, 2750, 3, 1, 0);
	    		break;
	    		case 1 :
	    		case 3 :
				    targetX = player.getPosition().getX() > x ? -2 : 2;
				    Agility.crossObstacle(player, x + targetX, y, 2750, 3, 1, 0);
	    		break;
	    	}
		return true;
	    case 3931: //log
		if (x == 2201 && y == 3237) {
		    Agility.crossObstacle(player, 2196, y, 762, 8, 45, 0);
		}
		if (x == 2197 && y == 3237) {
		    Agility.crossObstacle(player, 2202, y, 762, 8, 45, 0);
		}
		return true;
	    case 3932: //log
		if (x == 2259 && y == 3250) {
		    Agility.crossObstacle(player, 2264, y, 762, 8, 45, 0);
		}
		if (x == 2263 && y == 3250) {
		    Agility.crossObstacle(player, 2258, y, 762, 8, 45, 0);
		}
		return true;
	    case 3933: //log
		if (x == 2290 && y == 3238) {
		    Agility.crossObstacle(player, x, 3232, 762, 8, 45, 0);
		}
		if (x == 2290 && y == 3233) {
		    Agility.crossObstacle(player, x, 3239, 762, 8, 45, 0);
		}
		return true;
	    case 3937: //Dense Forest
	    case 3938:
	    case 3939:
	    case 3998:
	    case 3999:
	    	switch(objectFace)
	    	{
	    		case 0 :
	    		case 2 :
	    			if(id == 3937 || id == 3999)
	    			{
		    			targetY = player.getPosition().getY() < y ? 2 : -1;
		    			Agility.crossObstacle(player, x + 1, y + targetY, 2750, 4, 1, 0);
	    			}
	    			if(id == 3938 || id == 3939 || id == 3998)
	    			{
		    			targetY = player.getPosition().getY() < y ? 2 : -1;
		    			Agility.crossObstacle(player, x + 1, y + targetY, 844, 4, 1, 0);
	    			}
	    		break;
	    		case 1 :
	    		case 3 :
	    			if(id == 3937 || id == 3999)
	    			{
	    				targetX = player.getPosition().getX() < x ? 2 : -1;
		    			Agility.crossObstacle(player, x + targetX, y + 1, 2750, 4, 1, 0);
	    			}
	    			if(id == 3938 || id == 3939 || id == 3998)
	    			{
		    			targetX = player.getPosition().getX() < x ? 2 : -1;
		    			Agility.crossObstacle(player, x + targetX, y + 1, 844, 4, 1, 0);
	    			}
	    		break;
	    	}
		return true;
	    case 8742 :
			targetX = player.getPosition().getX() < x ? 1 : -1;
			Agility.crossObstacle(player, x + targetX, 3194, 844, 4, 1, 0);
	    return true;
	    //leaves
	    case 3925 :
	    	switch(objectFace)
	    	{
	    		case 0 :
	    		case 2 :
	    			targetX = player.getPosition().getX() < x ? 3 : -3;
	    			Agility.climbObstacle(player, x + targetX, y, player.getPosition().getZ(), 2750, 2, 0);
	    		break;
	    		case 1 :
	    		case 3 :
	    			targetY = player.getPosition().getY() < y ? 3 : -3;
	    			Agility.climbObstacle(player, x, y + targetY, player.getPosition().getZ(), 2750, 2, 0);
	    		break;
	    	}
	    return true;
	}
	return false;
    }

}
