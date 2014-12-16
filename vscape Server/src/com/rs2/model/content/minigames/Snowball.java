package com.rs2.model.content.minigames;

import com.rs2.model.Entity;
import com.rs2.model.Graphic;
import com.rs2.model.World;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.combat.effect.impl.BindingEffect;
import com.rs2.model.content.combat.hit.Hit;
import com.rs2.model.content.combat.hit.HitDef;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.quests.ChristmasEvent;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import java.util.Random;

public class Snowball {

    public static final int SNOWBALL = 10501;
    public static final int frozenTime = 6;

    private static boolean isSanta(final Entity victim) {
	return victim.isNpc() && ((Npc) victim).getNpcId() == ChristmasEvent.SANTA;
    }

    public static void throwSnowball(final Player player, final Entity victim) {
	if (!player.getInventory().getItemContainer().contains(SNOWBALL)) {
	    return;
	}
	CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
	    @Override
	    public void execute(CycleEventContainer container) {
		int attackerX = player.getPosition().getX(), attackerY = player.getPosition().getY();
		int victimX = victim.getPosition().getX(), victimY = victim.getPosition().getY();
		final int offsetX = (attackerY - victimY) * -1;
		final int offsetY = (attackerX - victimX) * -1;
		//player.getActionSender().walkTo(0, 0, true);

		if (!player.getInCombatTick().completed()) {
		    player.getActionSender().sendMessage("You cannot throw a snowball in combat!");
		    container.stop();
		    this.stop();
		    return;
		}
		if (player.inWild()) {
		    player.getActionSender().sendMessage("You cannot throw a snowball in the Wilderness!");
		    container.stop();
		    this.stop();
		    return;
		} else {
		    player.getUpdateFlags().sendGraphic(860);
		    player.getUpdateFlags().sendAnimation(5063);
		    player.getActionSender().sendMessage("You throw your snowball...");
		    CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
			    b.stop();
			}

			@Override
			public void stop() {
			     World.sendProjectile(player.getPosition(), offsetX, offsetY, 861, 33, 10, 70, victim.getIndex() + (victim.isPlayer() ? -1 : 1), false);
			}
		    }, 1);
		    player.getInventory().removeItem(new Item(SNOWBALL));
		    player.getUpdateFlags().faceEntity(victim.getFaceIndex());
		    player.getUpdateFlags().setUpdateRequired(true);
		    CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
			    if (75 > (new Random().nextDouble() * 100) && victim.getFrozenImmunity().completed()) {
				player.getActionSender().sendMessage("...it freezes your target in place!");
				/*victim.getFrozenTimer().setWaitDuration(isSanta(victim) ? frozenTime + 30 : frozenTime);
				victim.getFrozenTimer().reset();
				*/
				HitDef hitDefFreeze = new HitDef(null, HitType.NORMAL, isSanta(victim) ? 5 : 0);
				Hit freeze = new Hit(player, victim, hitDefFreeze);
				BindingEffect bind = new BindingEffect(isSanta(victim) ? frozenTime + 30 : frozenTime);
				bind.initialize(freeze);
				if(isSanta(victim) && !player.Area(3175, 3235, 3410, 3470)) {
				    if(victim.getCurrentHp() - 5 <= 0) {
					CombatManager.startDeath(victim);
				    } else {
					freeze.display();
					victim.setCurrentHp(victim.getCurrentHp() - 5);
				    }
				}
				victim.getFrozenImmunity().setWaitDuration(isSanta(victim) ? frozenTime + 36 : frozenTime + 6);
				victim.getFrozenImmunity().reset();
				victim.getUpdateFlags().sendGraphic(363);
			    } else {
				player.getActionSender().sendMessage("...but it fails to freeze your target.");
				//victim.getUpdateFlags().sendGraphic(new Graphic(85, 100)); splash
				victim.getUpdateFlags().sendGraphic(862);
			    }
			    if (victim.isPlayer()) {
				((Player) victim).getActionSender().sendMessage("You get covered in snow by a thrown snowball!");
			    }
			    container.stop();
			    this.stop();
			}

			@Override
			public void stop() {
			    player.getMovementHandler().reset();
			}
		    }, 4);
		    container.stop();
		}
	    }

	    @Override
	    public void stop() {
		player.getMovementHandler().reset();
	    }
	}, 3);
    }
}
