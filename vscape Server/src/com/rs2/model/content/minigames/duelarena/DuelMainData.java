package com.rs2.model.content.minigames.duelarena;

import java.util.ArrayList;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.content.combat.AttackType;
import com.rs2.model.content.combat.attacks.WeaponAttack;
import com.rs2.model.content.combat.projectile.Projectile;
import com.rs2.model.content.combat.projectile.ProjectileDef;
import com.rs2.model.content.combat.projectile.ProjectileTrajectory;
import com.rs2.model.content.combat.weapon.Weapon;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 4/22/12 Time: 5:56 PM To change
 * this template use File | Settings | File Templates.
 */
public class DuelMainData {

	private Player player;
	private Player opponent;
	private boolean startedDuel = false;

	private ArrayList<Item> ammoUsed = new ArrayList<Item>();
	private ArrayList<Item> itemToRemove = new ArrayList<Item>();
	private ArrayList<String> rulesWorking = new ArrayList<String>();

	public static final int DUEL_ARENA_INTERFACE_1 = 6575;
	public static final int DUEL_ARENA_INTERFACE_2 = 6412;
	public static final int DUEL_ARENA_SCOREBOARD = 6308;
	public static final int DUEL_ARENA_VICTORY = 6733;

	public DuelMainData(Player player) {
		this.player = player;
	}
	private boolean[] ruleStates = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};

	private ArrayList<Item> itemStaked = new ArrayList<Item>();

	public void clearArrays() {
		itemStaked.clear();
	    rulesWorking.clear();
	    itemToRemove.clear();
	    ammoUsed.clear();
	}

	public static Item[] listIntoArray(ArrayList<Item> list) {
		Item[] items = new Item[list.size()];
		for (int i = 0; i < list.size(); i++)
			items[i] = list.get(i);
		return items;
	}

	public AttackType getPlayerAttackType() {
		return new WeaponAttack(player, player.getDuelMainData().opponent, Weapon.getWeapon(new Item(player.getEquipment().getId(Constants.WEAPON)))).getAttackStyle().getAttackType();
	}

	public boolean handleButton(int buttonId) {
		if (buttonId == 26018 || buttonId == 25120) {
			if (!player.getDuelMainData().startedDuel) {
				player.getDuelInteraction().acceptDuel();
			}
			return true;
			
		}
		RulesData rulesData = RulesData.forId(buttonId);
		if (rulesData == null)
			return false;
		if (player.getDuelMainData().getOpponent() == null || !player.getDuelMainData().getOpponent().isLoggedIn()) {
			player.getDuelInteraction().endDuelInteraction(true);
			return true;
		}
		rulesData.activateRule(player, true);
		rulesData.activateRule(player.getDuelMainData().getOpponent(), false);
		return true;
	}

	public static void handleVictory(final Player winner, final Player loser) {
		if (winner == null || loser == null)
			return;
		GlobalDuelRecorder.addDuelToList(winner, loser);
		final String userName = loser.getUsername();
		final String level = "" + loser.getCombatLevel();
		final Item[] rewardItems = listIntoArray(loser.getDuelMainData().getItemStaked());
		//final ArrayList<Item> winnerAmmo = winner.getDuelMainData().getAmmoUsed();
		//final ArrayList<Item> loserAmmo = loser.getDuelMainData().getAmmoUsed();
		winner.getDuelInteraction().endDuelInteraction(true);
		winner.getDuelMainData().clearArrays();
		loser.getDuelMainData().clearArrays();
		winner.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(winner, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (winner != null) {
                    winner.teleport(winner.getDuelAreas().getRandomDuelPosition());
                    winner.getActionSender().sendString(userName, 6840);
                    winner.getActionSender().sendString(level, 6839);
                    winner.getActionSender().sendUpdateItems(6822, rewardItems);
                    winner.getActionSender().createPlayerHints(10, -1);
            		for (Item reward : rewardItems) {
            			winner.getInventory().addItemOrDrop(reward);
            		}
                    winner.resetEffects();
            		/*while (winnerAmmo.iterator().hasNext()) {
        				winner.getInventory().addItemOrDrop(winnerAmmo.iterator().next());
            		}
            		while (loserAmmo.iterator().hasNext()) {
            			loser.getInventory().addItemOrDrop(loserAmmo.iterator().next());
            		}*/
                }
                container.stop();
            }
            @Override
            public void stop() {
                if (winner != null) {
                    winner.setStopPacket(false);
                    winner.getActionSender().sendInterface(DUEL_ARENA_VICTORY);
                }
            }
        }, 2);
	}

	public void handleDeath(boolean forfeit) {
		if (opponent != null && opponent.getDuelMainData() != null) {
	        opponent.getAttributes().put("canTakeDamage", true);
			handleVictory(opponent, player);
		}
		if (forfeit) {
			player.getActionSender().removeInterfaces();
			player.getActionSender().sendMessage("You forfeited the duel.");
		} else {
	        player.getActionSender().sendMessage("You have been defeated!");
		}
		player.resetEffects();
		player.getActionSender().createPlayerHints(10, -1);
		player.getDuelInteraction().endDuelInteraction(false);
		player.teleport(player.getDuelAreas().getRandomDuelPosition());
	}

	public void handleLogin() {
		if (player.inDuelArena())
			player.teleport(player.getDuelAreas().getRandomDuelPosition());
	}

	public void sendIntoDuel() {
		final int randomNumber = Misc.random(2);
		player.resetEffects();
		opponent.resetEffects();
		player.getActionSender().removeInterfaces();
		opponent.getActionSender().removeInterfaces();
		player.getActionSender().createPlayerHints(10, opponent.getIndex());
		opponent.getActionSender().createPlayerHints(10, player.getIndex());
		for (int i = 0; i < itemToRemove.size(); i++)
			player.getEquipment().unequip(player.getEquipment().getItemContainer().getSlotById(itemToRemove.get(i).getId()));
		for (int i = 0; i < opponent.getDuelMainData().getItemToRemove().size(); i++)
			opponent.getEquipment().unequip(opponent.getEquipment().getItemContainer().getSlotById(opponent.getDuelMainData().getItemToRemove().get(i).getId()));
		Position position = player.getDuelAreas().getRandomArenaPosition(RulesData.OBSTACLES.activated(player), randomNumber);
		player.teleport(position);
		opponent.teleport(RulesData.NO_MOVEMENT.activated(opponent) ? player.getDuelAreas().getNextToPlayerPosition(position) : player.getDuelAreas().getRandomArenaPosition(RulesData.OBSTACLES.activated(opponent), randomNumber));

		startCountDown();
		opponent.getDuelMainData().startCountDown();

		player.getDuelInteraction().setAccepted(false);
	}

	public void startCountDown() {
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int counter = 3;

			@Override
			public void execute(CycleEventContainer container) {
				player.getUpdateFlags().sendForceMessage("" + counter);
				counter--;
				if (counter < 0)
					container.stop();
			}

			@Override
			public void stop() {
				player.getUpdateFlags().sendForceMessage("FIGHT!");
				setStartedDuel(true);
			}
		}, 2);
	}

	public void stakeItem(Item item, int slot) {
		if (player.getStatedInterface() != "duel" || item == null || !player.getInventory().getItemContainer().contains(item.getId()) || opponent == null)
			return;
		if (item.getDefinition().isUntradable()) {
			player.getActionSender().sendMessage("You can't stake this item.");
			return;
		}
        if(itemStaked.size() >= opponent.getInventory().getItemContainer().freeSlots() && (!item.getDefinition().isStackable() || !containsItem(item))){
            player.getActionSender().sendMessage("The opponent has no free spaces left for that.");
            return;
        }
		if (!Constants.ADMINS_CAN_INTERACT && player.getStaffRights() >= 2) {
            player.getActionSender().sendMessage("This action is not allowed.");
            return;
        }
        if(!player.getInventory().playerHasItem(item))
            return;
		int amount = player.getInventory().getItemAmount(item.getId());
        if (!player.getInventory().removeItemSlot(item, slot)) {
        	return;
        }
		if ((!item.getDefinition().isStackable() && !item.getDefinition().isNoted())) {
			for (int i = 0; i < item.getCount(); i++) {
				if (amount > 0) {
					itemStaked.add(new Item(item.getId(), 1));
					amount--;
				}
			}

		} else {
			boolean found1 = false;
			for (int i = 0; i < itemStaked.size(); i++) {
				if (itemStaked.get(i).getId() == item.getId()) {
					itemStaked.get(i).setCount(itemStaked.get(i).getCount() + item.getCount());
					found1 = true;
				}
			}
			if (!found1) {
				itemStaked.add(new Item(item.getId(), item.getCount() > amount ? amount : item.getCount()));
			}
		}
		player.getDuelInteraction().setAccepted(false);
		opponent.getDuelInteraction().setAccepted(false);
		player.getDuelInterfaces().updateAcceptString();
		opponent.getDuelInterfaces().updateAcceptString();
		player.getDuelInterfaces().sendStakedItems();
		opponent.getDuelInterfaces().sendStakedItems();
	}

	public void removeStakedItem(Item item) {
		if (player.getStatedInterface() != "duel" || item == null || itemStaked.size() <= 0 || !containsItem(item) || opponent == null)
			return;
		if (!item.getDefinition().isNoted() && !item.getDefinition().isStackable()) {
			for (int i = 0; i < item.getCount(); i++) {
				boolean found = false;
				for (int j = 0; j < itemStaked.size(); j++) {
					if (itemStaked.get(j).getId() == item.getId() && !found) {
						itemStaked.remove(j);
						player.getInventory().addItem(new Item(item.getId()));
						found = true;
					}
				}
			}
		} else {
			for (int i = 0; i < itemStaked.size(); i++) {
				if (itemStaked.get(i).getId() == item.getId()) {
					if (item.getCount() >= itemStaked.get(i).getCount()) {
						int amount = itemStaked.get(i).getCount();
						itemStaked.remove(i);
						player.getInventory().addItem(new Item(item.getId(), amount));
					} else {
						itemStaked.get(i).setCount(itemStaked.get(i).getCount() - item.getCount());
						player.getInventory().addItem(new Item(item.getId(), item.getCount()));
					}
				}
			}
		}
		player.getDuelInteraction().setAccepted(false);
		opponent.getDuelInteraction().setAccepted(false);
		player.getDuelInterfaces().updateAcceptString();
		opponent.getDuelInterfaces().updateAcceptString();
		player.getDuelInterfaces().sendStakedItems();
		opponent.getDuelInterfaces().sendStakedItems();
	}

    public boolean containsItem(Item item){
        boolean found = false;
			for (int i = 0; i < itemStaked.size(); i++) {
				if (itemStaked.get(i).getId() == item.getId()) {
					found = true;
				}
			}
        return found;
    }

    public boolean startedDuel() {
    	return startedDuel;
    }
   
	public boolean canStartDuel() {
		if (!player.inDuelArena() || !startedDuel())
			player.getActionSender().sendMessage("The duel hasn't started yet!");
		return player.inDuelArena() && startedDuel();
	}

	public void healPlayer() {
		if (player.getSkill().getLevel()[Skill.HITPOINTS] < player.getSkill().getPlayerLevel(Skill.HITPOINTS)) {
			player.getUpdateFlags().sendGraphic(84);
			player.getUpdateFlags().sendAnimation(866);
			player.getActionSender().sendMessage("You have been healed.");
			player.getSkill().setSkillLevel(Skill.HITPOINTS, player.getSkill().getPlayerLevel(Skill.HITPOINTS));
			player.getSkill().refresh(Skill.HITPOINTS);
		} else {
			player.getActionSender().sendMessage("You are already very healthy.");
		}
	}

	public void sendRottenTomato(final Player other) {
		player.getInventory().removeItem(new Item(2518));
		player.getUpdateFlags().sendAnimation(806);
		player.setStopPacket(true);
		player.getMovementHandler().reset();
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				new Projectile(player, other, new ProjectileDef(29, ProjectileTrajectory.SPELL)).show();
				container.stop();
			}
			@Override
			public void stop() {
				player.setStopPacket(false);
			}
		}, 1);

		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				other.getUpdateFlags().sendGraphic(31, 100 << 16);
				container.stop();
			}
			@Override
			public void stop() {
			}
		}, 3);

	}

	public ArrayList<Item> getItemStaked() {
		return itemStaked;
	}

    public Player getOpponent() {
		return opponent;
	}

	public void setOpponent(Player opponent) {
		this.opponent = opponent;
	}

	public ArrayList<Item> getItemToRemove() {
		return itemToRemove;
	}

	public boolean[] getRuleStates() {
		return ruleStates;
	}

	public ArrayList<String> getRulesWorking() {
		return rulesWorking;
	}

	public void setStartedDuel(boolean startedDuel) {
		this.startedDuel = startedDuel;
	}
	
	public ArrayList<Item> getAmmoUsed() {
		return ammoUsed;
	}

}
