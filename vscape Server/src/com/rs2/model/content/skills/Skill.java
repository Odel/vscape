package com.rs2.model.content.skills;

import com.rs2.Constants;
import com.rs2.model.World;
import com.rs2.model.content.skills.prayer.Prayer.PrayerData;
import com.rs2.model.players.Player;
import com.rs2.util.NameUtil;
import com.rs2.util.PlayerSave;

public class Skill {

	private Player player;

	public static final int SKILL_COUNT = 22;
	public static final double MAXIMUM_EXP = 200000000;

	private int[] level = new int[SKILL_COUNT];
	private double[] exp = new double[SKILL_COUNT];

	public int skillRenewalTimer = 100;
	public int hitpointRenewalTimer = 100;
    
    private static final int[] EXPERIENCE_BY_LEVEL;
    
    static {
        EXPERIENCE_BY_LEVEL = new int[100];
        int points = 0, output = 0;
        for (int lvl = 1; lvl <= 99; lvl++) {
            points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
            output = (int) Math.floor(points / 4);
            EXPERIENCE_BY_LEVEL[lvl] = output;
        }
    }

	public static final String[] SKILL_NAME = {"Attack", "Defence", "Strength", "Hitpoints", "Ranged", "Prayer", "Magic", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer", "Farming", "Runecrafting"};

	public static final int ATTACK = 0, DEFENCE = 1, STRENGTH = 2, HITPOINTS = 3, RANGED = 4, PRAYER = 5, MAGIC = 6, COOKING = 7, WOODCUTTING = 8, FLETCHING = 9, FISHING = 10, FIREMAKING = 11, CRAFTING = 12, SMITHING = 13, MINING = 14, HERBLORE = 15, AGILITY = 16, THIEVING = 17, SLAYER = 18, FARMING = 19, RUNECRAFTING = 20;

	private long lastAction = -10000;
	private long lastAction2 = -10000;

	public Skill(Player player) {
		this.player = player;
		for (int i = 0; i < level.length; i++) {
			if (i == 3) {
				level[i] = 10;
				exp[i] = 1154;
			} else {
				level[i] = 1;
				exp[i] = 0;
			}
		}
	}
        
        //Takes a skillname and converts it into the integer counterpart
        public static int skillnameConversion(String s){
            String skill = s.toLowerCase();
            switch(skill){
                case "attack":
                case "atk":
                case "att":
                    return 0;
                case "defence":
                case "def":
                    return 1;
                case "strength":
                case "str":
                    return 2;
                case "hitpoints":
                case "hp":
                    return 3;
                case "ranged":
                case "range":
                    return 4;
                case "prayer":
                    return 5;
                case "magic":
                case "mage":
                case "magicks":
                    return 6;
                case "cooking":
                case "cook":
                    return 7;
                case "woodcutting":
                case "wcing":
                case "wc":
                    return 8;
                case "fletching":
                case "fletch":
                    return 9;
                case "fishing":
                case "fish":
                    return 10;
                case "firemaking":
                case "fming":
                case "fm":
                    return 11;
                case "crafting":
                case "craft":
                    return 12;
                case "smithing":
                case "smith":
                    return 13;
                case "mining":
                case "mine":
                    return 14;
                case "herblore":
                case "herb":
                    return 15;
                case "agility":
                    return 16;
                case "thieving":
                case "thief":
                    return 17;
                case "slayer":
                case "slay":
                    return 18;
                case "farming":
                case "farm":
                    return 19;
                case "runecrafting":
                case "rcing":
                case "rc":
                    return 20;
                default:
                    return -1;
            }
        }

	public void skillTick() {
		if (skillRenewalTimer <= 0) {
			for (int i = 0; i < SKILL_COUNT; i++) {
				if (level[i] != getLevelForXP(getExp()[i])) {
					if (level[i] > getLevelForXP(getExp()[i])) {
						level[i] -= 1;
					} else if (i != 5) {
						level[i] += 1;
					}
					refresh(i);
				}
			}
			skillRenewalTimer = 100;
			return;
		}
		if (skillRenewalTimer == 100 || skillRenewalTimer == 50) {
		    if (player.getSpecialAmount() < 100) {
				player.setSpecialAmount(player.getSpecialAmount() + 10);
				player.updateSpecialBar();
			}
		}
		if (skillRenewalTimer == 50) {
			if (player.getIsUsingPrayer()[PrayerData.RAPID_RESTORE.getIndex()]) {
				for (int i = 0; i < SKILL_COUNT; i++) {
					if (level[i] != getLevelForXP(getExp()[i])) {
						if (level[i] > getLevelForXP(getExp()[i])) {
							level[i] -= 1;
						} else if (i != 5) {
							level[i] += 1;
						}
						refresh(i);
					}
				}
			}
			if (player.getIsUsingPrayer()[PrayerData.RAPID_HEAL.getIndex()] || (player.getEquipment().getItemContainer().get(Constants.HANDS) != null && player.getEquipment().getItemContainer().get(Constants.HANDS).getId() == 11133)) {
				if (level[3] != getLevelForXP(getExp()[3])) {
					if (level[3] < getLevelForXP(getExp()[3])) {
						level[3] += 1;
					}
					refresh(3);
				}
			}
		}
		skillRenewalTimer--;
	}

	public int[][] CHAT_INTERFACES = {{ATTACK, 6247, 0, 0}, {DEFENCE, 6253, 0, 0}, {STRENGTH, 6206, 0, 0}, {HITPOINTS, 6216, 0, 0}, {RANGED, 4443, 5453, 6114}, {PRAYER, 6242, 0, 0}, {MAGIC, 6211, 0, 0}, {COOKING, 6226, 0, 0}, {WOODCUTTING, 4272, 0, 0}, {FLETCHING, 6231, 0, 0}, {FISHING, 6258, 0, 0}, {FIREMAKING, 4282, 0, 0}, {CRAFTING, 6263, 0, 0}, {SMITHING, 6221, 0, 0}, {MINING, 4416, 4417, 4438}, {HERBLORE, 6237, 0, 0}, {AGILITY, 4277, 0, 0}, {THIEVING, 4261, 4263, 4264}, {SLAYER, 12122, 0, 0}, {FARMING, 4887, 4889, 4890},
			{RUNECRAFTING, 4267, 0, 0},};

	public void sendSkillsOnLogin() {
		refresh();
	}

	public void refresh() {
		for (int i = 0; i < level.length; i++) {
			player.getActionSender().sendSkill(i, level[i], exp[i]);
		}
		player.setCombatLevel(calculateCombatLevel());
		player.setAppearanceUpdateRequired(true);
	}

	public void refresh(int skill) {
		player.getActionSender().sendSkill(skill, level[skill], exp[skill]);
		player.setCombatLevel(calculateCombatLevel());
		player.setAppearanceUpdateRequired(true);
	}

	public int getPlayerLevel(int skill) {
		return getLevelForXP(exp[skill]);
	}

	public int getLevelForXP(double exp) {
		for (int lvl = 1; lvl <= 99; lvl++) {
			if (EXPERIENCE_BY_LEVEL[lvl] > exp) {
				return lvl;
			}
		}
		return 99;
	}

	public int getXPForLevel(int level) {    
        if (level >= EXPERIENCE_BY_LEVEL.length)
            return Integer.MAX_VALUE;
		return EXPERIENCE_BY_LEVEL[level];
	}

	public int getTotalLevel() {
		int total = 0;
		for (int i = 0; i < SKILL_NAME.length; i++) {
			total += getPlayerLevel(i);
		}
		return total;
	}

	public long getTotalXp() {
		long totalxp = 0;
		for (double xp : getExp()) {
			totalxp += xp;
		}
		return totalxp;
	}

	public void addExp(int skill, double xp) {
		if (getLevel()[skill] >= 3 && player.getNewComersSide().isInTutorialIslandStage())
			return;
		if (xp <= 0) {
			return;
		}
		int oldLevel = getLevelForXP(exp[skill]);
        xp *= Constants.EXP_RATE;
		if (player.getEnchantingChamber().isInEnchantingChamber() || player.getAlchemistPlayground().isInAlchemistPlayGround() || player.getCreatureGraveyard().isInCreatureGraveyard() || player.getTelekineticTheatre().isInTelekineticTheatre())
			exp[skill] += xp * 0.75;
		else
			exp[skill] += xp;
		if (exp[skill] > MAXIMUM_EXP) {
			exp[skill] = MAXIMUM_EXP;
		}
		int newLevel = getLevelForXP(exp[skill]);
		int levelDiff = newLevel - oldLevel;
		if (levelDiff > 0) {
			level[skill] += levelDiff;
            if (skill > Skill.ATTACK && skill <= Skill.MAGIC) {
                player.setCombatLevel(calculateCombatLevel());
            }
            player.getUpdateFlags().sendGraphic(199);
			sendLevelUpMessage(skill);
		    player.setAppearanceUpdateRequired(true);
		}
		refresh(skill);
	}
	
	public void subtractExp(int skill, double xp) {
		if (getLevel()[skill] >= 3 && player.getNewComersSide().isInTutorialIslandStage())
			return;
		if(xp <= 0) {
		    return;
		}
		int oldLevel = getLevelForXP(exp[skill]);
		xp *= Constants.EXP_RATE;
		if (player.getEnchantingChamber().isInEnchantingChamber() || player.getAlchemistPlayground().isInAlchemistPlayGround() || player.getCreatureGraveyard().isInCreatureGraveyard() || player.getTelekineticTheatre().isInTelekineticTheatre())
			exp[skill] -= xp * 0.75;
		else
			exp[skill] -= xp;
		if (exp[skill] > MAXIMUM_EXP) {
			exp[skill] = MAXIMUM_EXP;
		}
		int newLevel = getLevelForXP(exp[skill]);
		int levelDiff = newLevel - oldLevel;
		if (levelDiff > 0) {
		    level[skill] += levelDiff;
		    if (skill > Skill.ATTACK && skill <= Skill.MAGIC) {
			player.setCombatLevel(calculateCombatLevel());
		    }
		    player.getUpdateFlags().sendGraphic(199);
		    sendLevelUpMessage(skill);
		    player.setAppearanceUpdateRequired(true);
		}
		refresh(skill);
	}
	private void sendLevelUpMessage(int skill) {
		int[][] data = {{0, 6248, 6249, 6247}, // ATTACK
				{1, 6254, 6255, 6253}, // DEFENCE
				{2, 6207, 6208, 6206}, // STRENGTH
				{3, 6217, 6218, 6216}, // HITPOINTS
				{4, 5453, 6114, 4443}, // RANGED
				{5, 6243, 6244, 6242}, // PRAYER
				{6, 6212, 6213, 6211}, // MAGIC
				{7, 6227, 6228, 6226}, // COOKING
				{8, 4273, 4274, 4272}, // WOODCUTTING
				{9, 6232, 6233, 6231}, // FLETCHING
				{10, 6259, 6260, 6258}, // FISHING
				{11, 4283, 4284, 4282}, // FIREMAKING
				{12, 6264, 6265, 6263}, // CRAFTING
				{13, 6222, 6223, 6221}, // SMITHING
				{14, 4417, 4438, 4416}, // MINING
				{15, 6238, 6239, 6237}, // HERBLORE
				{16, 4278, 4279, 4277}, // AGILITY
				{17, 4263, 4264, 4261}, // THIEVING
				{18, 12123, 12124, 12122}, // SLAYER
				{19, 12123, 12124, 12122}, // FARMING
				{20, 4268, 4269, 4267}, // RUNECRAFTING
		};
		String[] name = {"Attack", "Defence", "Strength", "Hitpoints", "Ranged", "Prayer", "Magic", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer", "Farming", "Runecrafting",};
		if (skill == data[skill][0]) {
						player.getActionSender().sendQuickSong(67, 18);
			if(name[skill].charAt(0) == 'A')
			{
				player.getActionSender().sendString("@dbl@Congratulations, you just advanced an " + name[skill] + " level!", data[skill][1]);
			}
			else
			{
				player.getActionSender().sendString("@dbl@Congratulations, you just advanced a " + name[skill] + " level!", data[skill][1]);
			}
			player.getActionSender().sendString("Your " + name[skill] + " level is now " + getPlayerLevel(skill) + ".", data[skill][2]);
			if(name[skill].charAt(0) == 'A')
			{
				player.getActionSender().sendMessage("You've just advanced an " + name[skill] + " level! You have reached level " + getPlayerLevel(skill) + ".", true);
			}
			else
			{
				player.getActionSender().sendMessage("You've just advanced a " + name[skill] + " level! You have reached level " + getPlayerLevel(skill) + ".", true);
			}
			if (skill == 9) {
				player.getActionSender().sendFrame230(6235, 254, 1257, 408);
				player.getActionSender().sendComponentInterface(6235, 2711);
				player.getActionSender().sendFrame230(6236, 223, 177, 444);
				player.getActionSender().sendComponentInterface(6236, 2711);
			}else if (skill == 18) {
				player.getActionSender().sendFrame230(12173, 221, 1944, 649);
				player.getActionSender().sendComponentInterface(12173, 1733);
			}else if (skill == 19) {
				player.getActionSender().sendItemOnInterface(12173, 200, 5340);
			}
			if (getLevelForXP(getExp()[skill]) == 99) {
				player.getActionSender().sendMessage("Well done! You've achieved the highest possible level in this skill!", true);
				for (Player p : World.getPlayers()) 
				{
					if (p == null)
					{
						continue;
					}
						p.getActionSender().sendMessage("@red@"+NameUtil.uppercaseFirstLetter(player.getUsername() + " just hit level 99 in " + name[skill] + "!"), true);
					}
				}
			
			player.getActionSender().sendChatInterface(data[skill][3]);
			player.getDialogue().endDialogue();
		}
		PlayerSave.save(player);
		// c.getCombat().resetPlayerAttack();
		player.setAppearanceUpdateRequired(true);
		player.getActionSender().sendString("Total Lvl: " + getTotalLevel(), 3984);
	}

	public int calculateCombatLevel() {
		final int attack = getLevelForXP(exp[ATTACK]);
		final int defence = getLevelForXP(exp[DEFENCE]);
		final int strength = getLevelForXP(exp[STRENGTH]);
		final int hp = getLevelForXP(exp[HITPOINTS]);
		final int prayer = getLevelForXP(exp[PRAYER]);
		final int ranged = getLevelForXP(exp[RANGED]);
		final int magic = getLevelForXP(exp[MAGIC]);
		double level = defence + hp + (prayer / 2);
		double magiclvl = (level + (1.3 * (1.5 * magic))) / 4;
		double rangelvl = (level + (1.3 * (1.5 * ranged))) / 4;
		double meleelvl = (level + (1.3 * (attack + strength))) / 4;
		if (meleelvl >= rangelvl && meleelvl >= magiclvl) {
			return (int) meleelvl;
		} else if (rangelvl >= meleelvl && rangelvl >= magiclvl) {
			return (int) rangelvl;
		} else {
			return (int) magiclvl;
		}
	}

	public void setLevel(int[] level) {
		this.level = level;
	}

	public int[] getLevel() {
		return level;
	}

	public void setExp(double[] exp) {
		this.exp = exp;
	}

	public double[] getExp() {
		return exp;
	}

	public void setSkillLevel(int skillId, int skillLevel) {
		level[skillId] = skillLevel;
	}

	public boolean canDoAction(int timer) {
		if (System.currentTimeMillis() >= lastAction) {
			lastAction = System.currentTimeMillis() + timer;
			return true;
		}
		return false;
	}

	public boolean canDoAction2(int timer) {
		if (System.currentTimeMillis() >= lastAction2) {
			lastAction2 = System.currentTimeMillis() + timer;
			return true;
		}
		return false;
	}
        
        

}
