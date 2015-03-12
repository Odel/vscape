package com.rs2.model.content.minigames;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.World;
import com.rs2.model.content.WalkInterfaces;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.combat.effect.impl.BindingEffect;
import com.rs2.model.content.combat.hit.Hit;
import com.rs2.model.content.combat.hit.HitDef;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.quests.impl.ChristmasEvent.ChristmasEvent;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

import java.util.Random;

public class Snowball {

    public static final int SNOWBALL = 10501;
    public static final int frozenTime = 6;

    private static boolean isSanta(final Entity victim) {
	return victim.isNpc() && ((Npc) victim).getNpcId() == ChristmasEvent.SANTA;
    }
    
    private static boolean isLightCreature(final Entity victim) {
	return victim.isNpc() && ((Npc) victim).getNpcId() == ChristmasEvent.LIGHT_CREATURE;
    }

    public static void throwSnowball(final Player player, final Entity victim) {
	if (!player.getInventory().getItemContainer().contains(SNOWBALL) && player.getEquipment().getId(Constants.WEAPON) != ChristmasEvent.SNOWBALL_ITEM) {
	    return;
	}
	CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
	    @Override
	    public void execute(CycleEventContainer container) {
		int attackerX = player.getPosition().getX(), attackerY = player.getPosition().getY();
		int victimX = victim.getPosition().getX(), victimY = victim.getPosition().getY();
		final int offsetX = (attackerY - victimY) * -1;
		final int offsetY = (attackerX - victimX) * -1;
		if(isSanta(victim) && !player.Area(2754, 2814, 3833, 3873)) {
		    player.getDialogue().sendPlayerChat("I don't think that would be a good idea...", Dialogues.CONTENT);
		    player.getDialogue().endDialogue();
		    container.stop();
		    this.stop();
		    return;
		}
		if (!player.getInCombatTick().completed()) {
		    player.getActionSender().sendMessage("You cannot throw a snowball in combat!");
		    container.stop();
		    this.stop();
		    return;
		}
		if(player.Area(2754, 2814, 3833, 3873) && isSanta(victim) && !Misc.goodDistance(player.getPosition(), victim.getPosition(), 1)) {
		    player.getDialogue().sendPlayerChat("I need to get closer for a guaranteed hit.", Dialogues.CONTENT);
		    player.getDialogue().endDialogue();
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
		    player.getMovementHandler().reset();
		    player.getUpdateFlags().sendGraphic(860);
		    player.getUpdateFlags().sendAnimation(5063);
		    player.getActionSender().sendMessage("You throw your snowball...");
		    if(player.getInventory().playerHasItem(SNOWBALL)) {
			player.getInventory().removeItem(new Item(SNOWBALL, 1));
		    } else if (player.getEquipment().getId(Constants.WEAPON) == SNOWBALL) {
			player.getEquipment().removeAmount(Constants.WEAPON, 1);
			WalkInterfaces.checkChickenOption(player);
			player.getEquipment().refresh();
		    }
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
				HitDef hitDefFreeze = new HitDef(null, HitType.NORMAL, isSanta(victim) ? 15 : 0);
				Hit freeze = new Hit(player, victim, hitDefFreeze);
				BindingEffect bind = new BindingEffect(isLightCreature(victim) ? frozenTime + 5 : frozenTime);
				bind.initialize(freeze);
				if(isSanta(victim) && !player.Area(3175, 3235, 3410, 3470)) {
				    freeze.display();
				    CombatManager.startDeath(victim);
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
