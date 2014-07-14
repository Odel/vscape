package com.rs2.model.content.combat.attacks;

import com.rs2.model.Entity;
import com.rs2.model.Graphic;
import com.rs2.model.content.Following;
import com.rs2.model.content.combat.AttackScript;
import com.rs2.model.content.combat.AttackUsableResponse;
import com.rs2.model.tick.CycleEventContainer;

/**
 * Created by IntelliJ IDEA.
 * User: vayken
 * Date: 5/19/12
 * Time: 10:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class HealingScript extends AttackScript {

	private Graphic startGraphic, endGraphic;
	private int animation, attackDelay, hitpoints;

	public HealingScript(Entity attacker, Entity victim, int hitpoints, int animation, int attackDelay, Graphic startGraphic, Graphic endGraphic) {
		super(attacker, victim);
		this.animation = animation;
		this.attackDelay = attackDelay;
        this.hitpoints = hitpoints;
        this.startGraphic = startGraphic;
        this.endGraphic = endGraphic;
	}

    @Override
    public void initialize(){}

    @Override
    public int distanceRequired(){
        return 10;
    }


	@Override
	public int execute(CycleEventContainer container) {
		if (startGraphic != null)
			getAttacker().getUpdateFlags().sendGraphic(startGraphic.getId(), startGraphic.getValue());

		if (endGraphic!= null)
			getVictim().getUpdateFlags().sendGraphic(endGraphic.getId(), endGraphic.getValue());

		if (animation != -1)
			getAttacker().getUpdateFlags().sendAnimation(animation);

		getVictim().heal(hitpoints);

		return attackDelay;
	}

	@Override
	public AttackUsableResponse.Type isUsable() {
		Entity attacker = getAttacker();
		Entity victim = getVictim();
		if (!Following.withinRange(attacker, victim, distanceRequired())) {
			return AttackUsableResponse.Type.WAIT;
		}
		return AttackUsableResponse.Type.SUCCESS;
		/*int distanceRequired = distanceRequired();
		if (attacker.isPlayer()) {
			int diffX = attacker.getPosition().getX() - victim.getPosition().getX();
			int diffY = attacker.getPosition().getY() - victim.getPosition().getY();
			// check if diagonal
			if (Math.abs(diffX) == Math.abs(diffY))
				return AttackUsableResponse.Type.WAIT;
		}
		return AttackUsableResponse.Type.SUCCESS;
		// if attack is far away, make sure path is clear
		if (!Misc.checkClip(attacker.getPosition(), victim.getPosition(), distanceRequired == 1))
			return AttackUsableResponse.Type.WAIT;
		return AttackUsableResponse.Type.SUCCESS;*/
	}




}
