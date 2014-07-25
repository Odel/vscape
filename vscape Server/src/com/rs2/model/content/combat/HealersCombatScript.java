package com.rs2.model.content.combat;

import com.rs2.model.Entity;
import com.rs2.model.Graphic;
import com.rs2.model.content.combat.attacks.BasicAttack;
import com.rs2.model.content.combat.attacks.HealingScript;
import com.rs2.model.content.combat.weapon.AttackStyle;
import com.rs2.model.npcs.Npc;

/**
 *
 */
public class HealersCombatScript implements CombatScript {
    
    private int[] npcIdsToHeal;
    private int hpToHeal, animation, attackDelay;
	private Graphic startGraphic, endGraphic;
    
    public HealersCombatScript(int hpToHeal, int animation, int attackDelay, Graphic startGraphic, Graphic endGraphic, int... npcIdsToHeal) {
        this.hpToHeal = hpToHeal;
        this.animation = animation;
        this.attackDelay = attackDelay;
        this.startGraphic = startGraphic;
        this.endGraphic = endGraphic;
        this.npcIdsToHeal = npcIdsToHeal;
    }
    
    @Override
    public AttackScript[] generateAttacks(Entity attacker, Entity victim, int distance) {
        if (attacker.isPlayer()) {
            return new AttackScript[] {BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 14, 8, 2637)};
	}

        if (victim.isNpc()) {
            int npcId = ((Npc)victim).getNpcId();
            for (int i = 0; i < npcIdsToHeal.length; i++) {
                if (npcIdsToHeal[i] == npcId)
                    return new AttackScript[] {new HealingScript(attacker, victim, hpToHeal, animation, attackDelay, startGraphic, endGraphic)};
            }
        }
        return new AttackScript[] {BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.SLASH, 14, 8, 2637)};
    }
}