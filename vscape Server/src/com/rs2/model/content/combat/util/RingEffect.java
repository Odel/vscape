package com.rs2.model.content.combat.util;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.skills.magic.Teleportation;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;



public class RingEffect {
    
    public static final int[] normalGems = {1623, 1621, 1621, 1619, 1619, 1617};
    public static final int[] specialGems = {1625, 1625, 1627, 1629};

	public static void ringOfRecoil(Entity attacker, Player player, int damage) {
        Item ring = player.getEquipment().getItemContainer().get(Constants.RING);
        if (ring != null && ring.getId() == 2550 && player != null && damage > 0) {
            int recoilDamage = (int) Math.ceil(damage * 0.1);
            attacker.hit(recoilDamage, HitType.NORMAL);
            player.setRingOfRecoilLife(player.getRingOfRecoilLife() - recoilDamage);
            if (player.getRingOfRecoilLife() <= 0) {
            	player.getActionSender().sendMessage("Your ring shatters!");
                player.getEquipment().removeAmount(Constants.RING, 1);
                player.setRingOfRecoilLife(40);
            }
		}
	}
	
	public static void ringOfLife(Player player) {
        Item ring = player.getEquipment().getItemContainer().get(Constants.RING);
        if (ring != null && ring.getId() == 2570) { //Ring of life
            if (player.getTeleportation().attemptTeleportJewellery(Teleportation.HOME) && !player.getMMVars().inProcessOfBeingJailed) {
                player.getActionSender().sendMessage("Your ring shatters!");
                player.getEquipment().removeAmount(Constants.RING, 1);
            }
        }
	}
	
	public static boolean ringOfWealth(Player player) {
	    return player.getEquipment().getId(Constants.RING) == 2572;
	}

}
