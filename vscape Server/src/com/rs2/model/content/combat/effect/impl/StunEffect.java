package com.rs2.model.content.combat.effect.impl;

import com.rs2.model.Graphic;
import com.rs2.model.content.combat.effect.EffectTick;
import com.rs2.model.content.combat.hit.Hit;

/**
 *
 */
public class StunEffect extends BindingEffect {

    private int graphicId;

    public StunEffect(int frozenTime) {
        super(frozenTime);
        graphicId = frozenTime > 3 ? 80 : 254;
    }

    @SuppressWarnings("rawtypes")
	@Override
    public void onExecution(Hit hit, EffectTick tick) {
    	hit.getVictim().getStunTimer().setWaitDuration(getFrozenTime());
    	hit.getVictim().getStunTimer().reset();
        hit.getVictim().getUpdateFlags().sendGraphic(Graphic.highGraphic(graphicId));
    }
}
