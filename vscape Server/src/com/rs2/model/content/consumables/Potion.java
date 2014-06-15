package com.rs2.model.content.consumables;

import com.rs2.model.content.combat.effect.impl.PoisonEffect;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.minigames.duelarena.RulesData;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class Potion {

	Player player;

	public Potion(Player player) {
		this.player = player;
	}

	/**
	 * All the potion definitions.
	 */
	private static PotionLoader.PotionDefinition[] potionDefinitions = new PotionLoader.PotionDefinition[50];

	/**
	 * Potion definition count.
	 */
	public static int potionCount = 0;

	public int potionIndex = 0, potionIdIndex = 0;
	private static final int EMPTY_VIAL = 229;

	public boolean isPotion(int itemId) {
		for (int i = 0; i < potionCount; i++) {
			for (int i2 = 0; i2 < potionDefinitions[i].getPotionIds().length; i2++) {
				if (potionDefinitions[i].getPotionIds()[i2] == itemId) {
					potionIndex = i;
					potionIdIndex = i2;
					return true;
				}
			}
		}
		return false;
	}

	public void drinkPotion(int itemId, int slot) {
		if (RulesData.NO_DRINKS.activated(player)) {
			player.getActionSender().sendMessage("Usage of drinks have been disabled during this fight!");
			return;
		}
			if (player.getSkill().canDoAction2(600) && !player.isDead()) {
				player.setInstigatingAttack(false);
				int[] potionIds = potionDefinitions[potionIndex].getPotionIds();
				int[] affectedStats = potionDefinitions[potionIndex].getAffectedStats();
				int[] statAddons = potionDefinitions[potionIndex].getStatAddons();
				double[] statModifiers = potionDefinitions[potionIndex].getStatModifiers();
				for (int i = 0; i < affectedStats.length; i++) {
					if (potionDefinitions[potionIndex].getPotionType() == PotionLoader.PotionDefinition.PotionTypes.BOOST) {
						int index = affectedStats[i];
						int currentLevel = player.getSkill().getLevel()[index];
						int actualLevel = player.getSkill().getPlayerLevel(index);
						int levelAfterDrink = actualLevel;
						levelAfterDrink += actualLevel * statModifiers[i];
						levelAfterDrink += statAddons[i];
						if (currentLevel < actualLevel) {
							player.getSkill().getLevel()[index] += levelAfterDrink - actualLevel;
							player.getSkill().refresh(index);
						} else if (currentLevel < levelAfterDrink) {
							player.getSkill().getLevel()[index] = levelAfterDrink;
							player.getSkill().refresh(index);
						}
					} else if (potionDefinitions[potionIndex].getPotionType() == PotionLoader.PotionDefinition.PotionTypes.RESTORE) {
						int index = affectedStats[i];
						int currentLevel = player.getSkill().getLevel()[index];
						int actualLevel = player.getSkill().getPlayerLevel(index);
						int levelAfterDrink = currentLevel;
						levelAfterDrink += actualLevel * statModifiers[i];
						levelAfterDrink += statAddons[i];
						if (currentLevel > actualLevel) {
							continue;
						}
						if (levelAfterDrink <= actualLevel) {
							player.getSkill().getLevel()[index] = levelAfterDrink;
							player.getSkill().refresh(index);
						} else {
							player.getSkill().getLevel()[index] = player.getSkill().getPlayerLevel(index);
							player.getSkill().refresh(index);
						}
					}
				}
			doOtherPotionEffects(itemId);
			player.getUpdateFlags().sendAnimation(itemId == 3801 ? 1330 : 829, 0);
			player.getActionSender().sendSound(334, 0, 0);
			player.getTask();
			player.getCombatDelayTick().setWaitDuration(player.getCombatDelayTick().getWaitDuration() + 2);
				if (potionIdIndex < 18) { //booze
						if (player.getInventory().removeItemSlot(new Item(itemId, 1), slot)) {
							if(potionIdIndex + 1 < potionIds.length)
							player.getInventory().addItemToSlot(new Item(potionIds[potionIdIndex + 1], 1), slot);
						} else if (player.getInventory().removeItem(new Item(itemId, 1))) {
							if(potionIdIndex + 1 < potionIds.length)
							player.getInventory().addItem(new Item(potionIds[potionIdIndex + 1], 1));
						}
					if(isDose(itemId))
					{
						player.getActionSender().sendMessage("You drink"+(isDose(itemId) ? " a dose of" :"")+" your " + potionDefinitions[potionIndex].getPotionName() + ".");
					}
				} else {
					if (player.getInventory().removeItemSlot(new Item(itemId, 1), slot)) {
						player.getInventory().addItemToSlot(new Item(EMPTY_VIAL), slot);
					} else if (player.getInventory().removeItem(new Item(itemId, 1))) {
						player.getInventory().addItem(new Item(EMPTY_VIAL));
					}
					player.getActionSender().sendMessage("You drink the last dose of your " + potionDefinitions[potionIndex].getPotionName() + ".");
				}	
		}
	}

	private boolean isDose(int itemId) {
		return itemId != 1993 && itemId != 1978 && itemId != 1917 && itemId != 7919 && itemId != 3801
			&& itemId != 1905 && itemId != 5751 && itemId != 4627 && itemId != 5755 && itemId != 5763
			 && itemId != 1911 && itemId != 1913 && itemId != 1909 && itemId != 1915 && itemId != 2955
			 && itemId != 5759 && itemId != 1907 && itemId != 2015 && itemId != 2017;
	}

	private void doOtherPotionEffects(int itemId) {
		switch (itemId) {
			case 7919 : // Bottle of wine
				player.heal(14);
				player.getActionSender().sendMessage("You drink your " + potionDefinitions[potionIndex].getPotionName() + ".");
				break;
			case 3801 : // Keg of beer
				player.getActionSender().statEdit(Skill.ATTACK, -40, false);
				player.getActionSender().statEdit(Skill.STRENGTH, 10, true);
				player.heal(1);
				player.getActionSender().sendMessage("You chug the keg. You feel reinvigorated... ...but extremely drunk, too.");
				player.setDrunkState(true, 600);
				break;
			case 1993 : // Jug of wine
				player.getActionSender().statEdit(Skill.ATTACK, -2, false);
				player.getActionSender().sendMessage("You drink your " + potionDefinitions[potionIndex].getPotionName() + ".");
				player.heal(11);
				break;
			case 1978 : // Cup of tea
				player.getUpdateFlags().sendForceMessage("Aaah, nothing like a nice cuppa tea!");
				player.getActionSender().sendMessage("You drink your " + potionDefinitions[potionIndex].getPotionName() + ".");
				player.heal(3);
				break;
			case 1917 : // Beer
				player.getActionSender().statEdit(Skill.ATTACK, (int) (player.getSkill().getPlayerLevel(Skill.ATTACK) * 0.07) * -1, false);
				player.getActionSender().statEdit(Skill.STRENGTH, (int) (player.getSkill().getPlayerLevel(Skill.STRENGTH) * 0.04) * 1, true);
				player.getActionSender().sendMessage("You drink your " + potionDefinitions[potionIndex].getPotionName() + ".");
				player.heal(1);
				player.setDrunkState(true, 60);
				break;
			case 1905 : // asgarnian ale
				player.getActionSender().statEdit(Skill.ATTACK, -4, false);
				player.getActionSender().statEdit(Skill.STRENGTH, 2, true);
				player.getActionSender().sendMessage("You drink your " + potionDefinitions[potionIndex].getPotionName() + ".");
				player.heal(2);
				player.setDrunkState(true, 60);
				break;
			case 5751 : // axeman's folly
				player.getActionSender().statEdit(Skill.ATTACK, -3, false);
				player.getActionSender().statEdit(Skill.STRENGTH, -3, false);
				player.getActionSender().statEdit(Skill.WOODCUTTING, 1, true);
				player.getActionSender().sendMessage("You drink your " + potionDefinitions[potionIndex].getPotionName() + ".");
				player.heal(1);
				player.setDrunkState(true, 60);
				break;
			case 4627 : //bandit's brew
				player.getActionSender().statEdit(Skill.ATTACK, 1, true);
				player.getActionSender().statEdit(Skill.STRENGTH, -1, false);
				player.getActionSender().statEdit(Skill.DEFENCE, -6, false);
				player.getActionSender().statEdit(Skill.THIEVING, 1, true);
				player.getActionSender().sendMessage("You drink your " + potionDefinitions[potionIndex].getPotionName() + ".");
				player.setDrunkState(true, 60);
				break;
			case 5755 : //chef's delight
				player.getActionSender().statEdit(Skill.COOKING, (int) (player.getSkill().getPlayerLevel(Skill.COOKING) * 0.05) + 1, true);
				player.getActionSender().statEdit(Skill.STRENGTH, -2, false);
				player.getActionSender().statEdit(Skill.ATTACK, -2, false);
				player.heal(1);
				player.getActionSender().sendMessage("You drink your " + potionDefinitions[potionIndex].getPotionName() + ".");
				player.setDrunkState(true, 60);
				break;
			case 5763 : //cider
				player.getActionSender().statEdit(Skill.FARMING, 1, true);
				player.getActionSender().statEdit(Skill.STRENGTH, -2, false);
				player.getActionSender().statEdit(Skill.ATTACK, -2, false);
				player.heal(2);
				player.getActionSender().sendMessage("You drink your " + potionDefinitions[potionIndex].getPotionName() + ".");
				player.setDrunkState(true, 60);
				break;
			case 1911 : //dragon bitter
				player.getActionSender().statEdit(Skill.STRENGTH, 2, true);
				player.getActionSender().statEdit(Skill.ATTACK, -1, false);
				player.heal(1);
				player.getActionSender().sendMessage("You drink your " + potionDefinitions[potionIndex].getPotionName() + ".");
				player.setDrunkState(true, 60);
				break;
			case 1913 : //dwarven stout
				player.getActionSender().statEdit(Skill.MINING, 1, true);
				player.getActionSender().statEdit(Skill.SMITHING, 1, true);
				player.heal(1);
				player.getActionSender().sendMessage("You drink your " + potionDefinitions[potionIndex].getPotionName() + ".");
				player.setDrunkState(true, 60);
				break;
			case 1909 : //greenman's ale
				player.getActionSender().statEdit(Skill.HERBLORE, 1, true);
				player.heal(1);
				player.getActionSender().sendMessage("You drink your " + potionDefinitions[potionIndex].getPotionName() + ".");
				player.setDrunkState(true, 60);
				break;
			case 1915 : //grog
				player.getActionSender().statEdit(Skill.STRENGTH, 3, true);
				player.getActionSender().statEdit(Skill.ATTACK, -6, false);
				player.heal(3);
				player.getActionSender().sendMessage("You drink your " + potionDefinitions[potionIndex].getPotionName() + ".");
				player.setDrunkState(true, 60);
				break;
			case 2955 : //moonlight mead
				player.heal(4);
				player.getActionSender().sendMessage("It tastes like something just died in your mouth.");
				player.setDrunkState(true, 120);
				break;
			case 5759 : //slayer's respite
				player.getActionSender().statEdit(Skill.SLAYER, 2, true);
				player.getActionSender().statEdit(Skill.ATTACK, -2, false);
				player.getActionSender().statEdit(Skill.STRENGTH, -2, false);
				player.heal(1);
				player.getActionSender().sendMessage("You drink your " + potionDefinitions[potionIndex].getPotionName() + ".");
				player.setDrunkState(true, 60);
				break;
			case 1907 : //wizard's mind bomb
				player.getActionSender().statEdit(Skill.MAGIC,player.getSkill().getPlayerLevel(Skill.MAGIC) >= 50 ? 3 : 2, true);
				player.getActionSender().statEdit(Skill.ATTACK, -4, false);
				player.getActionSender().statEdit(Skill.STRENGTH, -3, false);
				player.getActionSender().statEdit(Skill.DEFENCE, -3, false);
				player.heal(1);
				player.getActionSender().sendMessage("You drink your " + potionDefinitions[potionIndex].getPotionName() + ".");
				player.setDrunkState(true, 60);
				break;
			case 2015 : //vodka
				player.getActionSender().statEdit(Skill.ATTACK, -3, false);
				player.getActionSender().statEdit(Skill.STRENGTH, 3, true);
				player.getActionSender().sendMessage("You drink your vodka and smash the bottle on your head.");
				player.setDrunkState(true, 300);
				break;
			case 2017 : //whisky
				player.getActionSender().statEdit(Skill.ATTACK, -4, false);
				player.getActionSender().statEdit(Skill.STRENGTH, 3, true);
				player.heal(5);
				player.getActionSender().sendMessage("You drink your whisky and drop the bottle, whoops!");
				player.setDrunkState(true, 300);
				break;
			case 2446 : // Antipoison
			case 175 :
			case 177 :
			case 179 :
				player.removeAllEffects(PoisonEffect.class);
				player.getPoisonImmunity().setWaitDuration(300);
				player.getPoisonImmunity().reset();
				break;
			case 3008 : // Energy
			case 3010 :
			case 3012 :
			case 3014 :
				if (player.getEnergy() + 20 < 100) {
					player.setEnergy(player.getEnergy() + 20);
				} else {
					player.setEnergy(100);
				}
				break;
			case 2448 : // Super antipoison
			case 181 :
			case 183 :
			case 185 :
				player.removeAllEffects(PoisonEffect.class);
				player.getPoisonImmunity().setWaitDuration(600);
				player.getPoisonImmunity().reset();
				break;
			case 3016 : // Super energy
			case 3018 :
			case 3020 :
			case 3022 :
				if (player.getEnergy() + 40 < 100) {
					player.setEnergy(player.getEnergy() + 40);
				} else {
					player.setEnergy(100);
				}
				break;
			case 5943 : // Antipoison+
			case 5945 :
			case 5947 :
			case 5949 :
				player.removeAllEffects(PoisonEffect.class);
				player.getPoisonImmunity().setWaitDuration(900);
				player.getPoisonImmunity().reset();
				break;
			case 2452 : // Antifire
			case 2454 :
			case 2456 :
			case 2458 :
				player.getFireImmunity().setWaitDuration(600);
				player.getFireImmunity().reset();
				break;
			case 5952 : // Antipoison++
			case 5954 :
			case 5956 :
			case 5958 :
				player.removeAllEffects(PoisonEffect.class);
				player.getPoisonImmunity().setWaitDuration(1200);
				player.getPoisonImmunity().reset();
				break;
			case 6685 : // Saradomin brew
			case 6687 :
			case 6689 :
			case 6691 :
				player.heal(2);
				player.getActionSender().statEdit(Skill.HITPOINTS, (int) (player.getSkill().getPlayerLevel(Skill.HITPOINTS) * 0.15), true);
				player.getActionSender().statEdit(Skill.DEFENCE, (int) (player.getSkill().getPlayerLevel(Skill.DEFENCE) * 0.25), true);
				player.getActionSender().statEdit(Skill.ATTACK, (int) ((player.getSkill().getPlayerLevel(Skill.ATTACK) * 0.10) * -1), false);
				player.getActionSender().statEdit(Skill.STRENGTH, (int) ((player.getSkill().getPlayerLevel(Skill.STRENGTH) * 0.10) * -1), false);
				player.getActionSender().statEdit(Skill.MAGIC, (int) ((player.getSkill().getPlayerLevel(Skill.MAGIC) * 0.10) * -1), false);
				player.getActionSender().statEdit(Skill.RANGED, (int) ((player.getSkill().getPlayerLevel(Skill.RANGED) * 0.10) * -1), false);
				break;
			case 2450 : // Zamorak brew
			case 189 :
			case 191 :
			case 193 :
				player.getActionSender().statEdit(Skill.ATTACK, (int) (player.getSkill().getPlayerLevel(Skill.ATTACK) * 0.20) + 2, true);
				player.getActionSender().statEdit(Skill.STRENGTH, (int) (player.getSkill().getPlayerLevel(Skill.STRENGTH) * 0.12) + 2, true);
				player.getActionSender().statEdit(Skill.PRAYER, (int) (player.getSkill().getPlayerLevel(Skill.PRAYER) * 0.10), true);
				player.getActionSender().statEdit(Skill.DEFENCE, ((int) (player.getSkill().getPlayerLevel(Skill.DEFENCE) * 0.10) + 2) * -1, false);
				player.hit((int) (player.getSkill().getLevel()[Skill.HITPOINTS] * 0.1) + 2, HitType.NORMAL);
				break;
			case 151: //fishing
			case 153:
			case 155:
			case 2438:
				player.getActionSender().statEdit(Skill.FISHING, 3, true);
				break;
			case 169: //ranging
			case 171:
			case 173:
			case 2444:
				player.getActionSender().statEdit(Skill.RANGED, ((int) Math.floor(player.getSkill().getPlayerLevel(Skill.RANGED) * 0.10)) + 4, true);
				break;
			default :
				break;
		}
	}

	public static PotionLoader.PotionDefinition[] getPotionDefinitions() {
		return potionDefinitions;
	}

}