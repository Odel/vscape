package com.rs2.model.content;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

/**
 * By Mikey` of Rune-Server
 */
public class Pets {

	private Player player;
	private Npc pet;
	private int itemId;

	public Pets(Player player) {
		this.player = player;
	}

	public static final int[][] PET_IDS = {// itemId, petId
	{1561, 768}, {7585, 3507}, {5608, 1319}, {10721, 2473 }, {9975, 4447}, {5609, 1401} };

	/**
	 * Registers a pet for the player, and drops it.
	 */
	public void registerPet(int itemId, int petId) {
		if (player.getInventory().getItemContainer().getCount(itemId) == 0 && itemId != -1) {
			return;
		}
		if (pet != null) {
			player.getActionSender().sendMessage("You already have a pet following you!");
			return;
		}
		player.getInventory().removeItem(new Item(itemId, 1));
		this.itemId = itemId;
		this.pet = new Npc(petId);
		pet.setPosition(new Position(player.getPosition().getX() - 1, player.getPosition().getY(), player.getPosition().getZ()));
		pet.setSpawnPosition(new Position(player.getPosition().getX() - 1, player.getPosition().getY(), player.getPosition().getZ()));
		pet.setCurrentX(player.getPosition().getX() - 1);
		pet.setCurrentY(player.getPosition().getX() - 1);
		World.register(pet);      
		pet.setFollowingEntity(player);
                pet.setPlayerOwner(player.getIndex());
                pet.setDontAttack(true);
                pet.setCombatDelay(100000000);
                if(pet.getNpcId() == 1319)
                    pet.getUpdateFlags().sendForceMessage("Yiff!");
                else if(pet.getNpcId() == 2473)
                    pet.getUpdateFlags().sendForceMessage("Ribbit.");
	}
	/**
	 * Unregisters a pet for the player, and picks it up it.
	 */
	public void unregisterPet() {
		if (pet == null) {
			return;
		}
                    player.getActionSender().sendMessage("You pick up your pet.");
                    player.getInventory().addItem(new Item(itemId, 1));
                    this.itemId = -1;
                    pet.teleport(new Position(pet.getPosition().getX(), 10000, pet.getPosition().getZ()-1));
                    pet.setVisible(false);
                    World.unregister(pet);
                    Following.resetFollow(pet);
                    pet = null;
	}

	/**
	 * Returns the pet instance of the Npc class.
	 */
	public Npc getPet() {
		return pet;
	}

}
