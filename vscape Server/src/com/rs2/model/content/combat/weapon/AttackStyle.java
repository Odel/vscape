package com.rs2.model.content.combat.weapon;

import com.rs2.model.content.combat.AttackType;
import com.rs2.model.content.skills.Skill;

/**
 *
 */
public class AttackStyle {

	public enum Mode {
		MELEE_ACCURATE(Skill.ATTACK), AGGRESSIVE(Skill.STRENGTH), DEFENSIVE(Skill.DEFENCE), CONTROLLED(Skill.ATTACK, Skill.DEFENCE, Skill.STRENGTH), RANGED_ACCURATE(Skill.RANGED), LONGRANGE(Skill.RANGED, Skill.DEFENCE), RAPID(Skill.RANGED), MAGIC(Skill.MAGIC), DRAGONFIRE_FAR(Skill.MAGIC), DRAGONFIRE(Skill.MAGIC), MELEE_FAR(Skill.ATTACK);
        private int[] skillsTrained;
        Mode(int... skillsTrained) {
            this.skillsTrained = skillsTrained;
        }
        
        public int[] getSkillsTrained() {
            return skillsTrained;
        }
	}
	public enum Bonus {
		STAB(0), SLASH(1), CRUSH(2), MAGIC(3), RANGED(4);

		private int index;
		Bonus(int index) {
			this.index = index;
		}

		public int toInteger() {
			return index;
		}
	}

	private Mode mode;
	private Bonus bonus;
	private int buttonId;
	private AttackType attackType;

	public AttackStyle(AttackType attackType, Mode mode, Bonus bonus) {
		this(attackType, -1, mode, bonus);
	}

	public AttackStyle(AttackType attackType, int buttonId, Mode mode, Bonus bonus) {
		this.attackType = attackType;
		this.buttonId = buttonId;
		this.mode = mode;
		this.bonus = bonus;
	}

	public AttackType getAttackType() {
		return attackType;
	}

	public int getButtonId() {
		return buttonId;
	}

	public Mode getMode() {
		return mode;
	}

	public Bonus getBonus() {
		return bonus;
	}
}
