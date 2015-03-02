package com.rs2.model.content.combat.util;

import com.rs2.Constants;
import com.rs2.model.content.Shops;
import com.rs2.model.content.dialogue.DialogueManager;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 24/03/12 Time: 12:17 To change
 * this template use File | Settings | File Templates.
 */
public class Degrading {
    public static final int REPAIR_COST_BARROWS = 10;
    public static final int REPAIR_COST_CRYSTAL = 50;
	
	public static void handleHit(final Player player, boolean weaponHit) {
	    Item equipped = new Item(player.getEquipment().getId(Constants.WEAPON));
	    Degradeables item = Degradeables.getDegradeableItem(equipped);
	    if(!weaponHit) {
		item = getRandomEquipped(player);
		if(item != null) {
		    equipped = player.getEquipment().getItemContainer().get(item.getEquipSlot());
		}
	    }
	    if (item == null || !Constants.DEGRADING_ENABLED) {
		return;
	    }
	    if(!ownsDegradedVersion(player, equipped.getId())) {
		if ((player.getDegradeableHits()[Degradeables.getDegradeableItem(equipped).getPlayerArraySlot()] < 0 && item.getOriginalId() == equipped.getId())
		    || (player.getDegradeableHits()[Degradeables.getDegradeableItem(equipped).getPlayerArraySlot()] > 0 && item.getOriginalId() == equipped.getId())) {
		    player.setDegradeableHits(Degradeables.getDegradeableItem(equipped).getPlayerArraySlot(), 0);
		}
	    }
	    if (equipped.getId() == item.getOriginalId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == 0) {
		player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
		player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getFirstDegradeId()));
		player.getEquipment().refresh();
		player.setDegradeableHits(item.getPlayerArraySlot(), player.getDegradeableHits()[item.getPlayerArraySlot()] + 1);
		return;
	    }
	    player.setDegradeableHits(item.getPlayerArraySlot(), player.getDegradeableHits()[item.getPlayerArraySlot()] + 1);
	    int count = 1;
	    for(int i : item.getIterableDegradedIds(!equipped.getDefinition().getName().toLowerCase().contains("crystal"))) {
		if(equipped.getId() == i && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS * count) {
		    if (equipped.getId() == item.getFourthDegradeId() && item.getFifthDegradeId() == -1 && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS * 4
			|| equipped.getId() == item.getTenthDegradeId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS * 10) {
			player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has broken.");
			player.setDegradeableHits(item.getPlayerArraySlot(), 0);
			if (player.getInventory().canAddItem(new Item(item.getBrokenId()))) {
			    player.getInventory().addItem(new Item(item.getBrokenId()));
			} else {
			    GroundItem dropItem = new GroundItem(new Item(item.getBrokenId()), player, player.getPosition().clone());
			    GroundItemManager.getManager().dropItem(dropItem);
			}
			if (player.getInventory().canAddItem(equipped)) {
				player.getEquipment().unequip(item.getEquipSlot());
				player.getInventory().removeItem(equipped);
			} else {
				player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(-1));
			}
			player.getEquipment().refresh();
			player.setAppearanceUpdateRequired(true);
			return;
		    } else {
			player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
			player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getDegradeIdForInt(count + 1)));
			player.getEquipment().refresh();
			return;
		    }
		}
		count++;
	    }
	}
	
	public static boolean ownsDegradedVersion(final Player player, int itemId) {
	    boolean crystal = new Item(itemId).getDefinition().getName().toLowerCase().contains("crystal");
	    Degradeables d = Degradeables.getDegradeableItem(new Item(itemId));
	    if(d != null) {
		for(int i : d.getIterableDegradedIds(!crystal)) {
		    if(player.getInventory().ownsItem(i))
			return true;
		}
	    }
	    return false;
	}
	
	public static String[] optionBuilder(final ArrayList<Item> items, int options, int offset) {
	    String[] toReturn = new String[options];
	    boolean isLastOptions = (items.size() - offset) <= 5;
	    for(int i = 0; i < options; i++) {
		if(i == 4 && items.size() % 4 == 0) continue;
		toReturn[i] = "" + new Item(Degradeables.getDegradeableItem(items.get(i + offset)).getOriginalId()).getDefinition().getName();
	    }
	    if(options == 5) {
		if(items.size() % 4 == 0 && (items.size() - offset) == 4) {
		    toReturn[4] = "Nevermind.";
		} else if((items.size() - 1) % 4 != 0 || (items.size() > 5 && (items.size() - 1) % 4 == 0 && !isLastOptions)){
		    toReturn[4] = "More...";
		}
	    }
	    return toReturn;
	}
	
	public static ArrayList<Item> getDegradedBarrowsInInventory(final Player player) {
	    ArrayList<Item> toReturn = new ArrayList<>();
	    for(Item i : player.getInventory().getItemContainer().toArray()) {
		if(i == null) {
		    continue;
		}
		if(i.getDefinition().getName().contains(" 100") || i.getDefinition().getName().contains(" 75") || i.getDefinition().getName().contains(" 50")
		|| i.getDefinition().getName().contains(" 25") || i.getDefinition().getName().contains(" 0")) {
		    if(!toReturn.contains(i)) {
			toReturn.add(i);
		    }
		    
		}
	    }
	    return toReturn;
	}
	
	public static ArrayList<Item> getDegradedCrystalInInventory(final Player player) {
	    ArrayList<Item> toReturn = new ArrayList<>();
	    for(Item i : player.getInventory().getItemContainer().toArray()) {
		if(i == null) {
		    continue;
		}
		Degradeables d = Degradeables.getDegradeableItem(i);
		if(d != null) {
		    for(int compare : d.getIterableDegradedIds(false)) {
			if(i.getId() == compare && !toReturn.contains(i) && i.getDefinition().getName().toLowerCase().contains("crystal")) {
			    toReturn.add(i);
			}
		    }
		}
	    }
	    return toReturn;
	}
	
	private static Degradeables getRandomEquipped(final Player player) {
	    ArrayList<Degradeables> equippedDegradeables = new ArrayList<>();
	    for(int x = 0; x < 14; x++) {
		Item equipped = player.getEquipment().getItemContainer().get(x);
		if(equipped == null) continue;
		Degradeables item = Degradeables.getDegradeableItem(equipped);
		if(item == null || x == Constants.WEAPON) continue;
		equippedDegradeables.add(item);
	    }
	    if(equippedDegradeables.isEmpty()) {
		return null;
	    }
	    else if(equippedDegradeables.size() == 1) {
		return equippedDegradeables.get(0);
	    }
	    else {
		return equippedDegradeables.get(Misc.randomMinusOne(equippedDegradeables.size()));
	    }
	}
	
    public static boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
	DialogueManager d = player.getDialogue();
	ArrayList<Item> toRepair = getDegradedBarrowsInInventory(player);
	ArrayList<Item> toRepairCrystal = getDegradedCrystalInInventory(player);
	switch (id) {
	    case 1777: //Ilfeen crystal repairs
		switch (player.getDialogue().getChatId()) {
		    case 1:
			d.sendNpcChat("Hello " + player.getUsername() + ".", "How are you doing?", CONTENT);
			return true;
		    case 2:
			if (!toRepairCrystal.isEmpty()) {
			    d.sendOption("Who are you?", "I'm doing fine, thank you.", "I have some degraded crystal equipment here.");
			    return true;
			} else {
			    d.sendOption("Who are you?", "I'm doing fine, thank you.");
			    return true;
			}
		    case 3:
			switch(optionId) {
			    case 1:
				d.sendPlayerChat("Who are you?", CONTENT);
				return true;
			    case 2:
				    d.sendPlayerChat("I'm doing fine, thank you.", CONTENT);
				d.endDialogue();
				return true;
			    case 3:
				d.sendPlayerChat("I have some degraded crystal equipment here.", CONTENT);
				d.setNextChatId(6);
				return true;
			}
		    case 4:
			d.sendNpcChat("My name is Ilfeen, enchanter and life-giver of the", "crystal seed. I have studied it extensively, enough", "to give charge back to equipment created from seed.", CONTENT);
			return true;
		    case 5:
			d.sendPlayerChat("Very interesting, thank you.", CONTENT);
			d.endDialogue();
			return true;
		    case 6:
			String name = new Item(Degradeables.getDegradeableItem(toRepairCrystal.get(0)).getOriginalId()).getDefinition().getName();
			name = name.substring(name.indexOf("c"));
			if(toRepairCrystal.size() == 1) {
			    d.sendNpcChat("Ah, I see you do! Let me see...", "Your " + name + " needs repairing, no?", CONTENT);
			    return true;
			} else {
			    d.sendNpcChat("Ah, I see you do! Let me see...", "Which would you like repaired?", CONTENT);
			    return true;
			}
		    case 7:
			if(toRepairCrystal.size() == 1) {
			    d.sendOption("Yes.", "No.");
			} else {
			    String repairName_1 = new Item(Degradeables.getDegradeableItem(toRepairCrystal.get(0)).getOriginalId()).getDefinition().getName();
			    String repairName_2 = new Item(Degradeables.getDegradeableItem(toRepairCrystal.get(1)).getOriginalId()).getDefinition().getName();
			    d.sendOption(repairName_1 + ".", repairName_2 + ".");
			    d.setNextChatId(17);
			}
			return true;
		    case 8:
			switch(optionId) {
			    case 1:
				d.sendPlayerChat("Yes.", CONTENT);
				player.setTempInteger(0);
				return true;
			    case 2:
				d.sendPlayerChat("No, thank you.", CONTENT);
				d.endDialogue();
				return true;
			}
		    case 9:
			int hits = player.getDegradeableHits()[Degradeables.getDegradeableItem(toRepairCrystal.get(player.getTempInteger())).getPlayerArraySlot()];
			String itemName = new Item(Degradeables.getDegradeableItem(toRepairCrystal.get(player.getTempInteger())).getOriginalId()).getDefinition().getName();
			itemName = itemName.substring(itemName.indexOf("c"));
			if(toRepairCrystal.get(player.getTempInteger()).getId() == Degradeables.getDegradeableItem(toRepairCrystal.get(player.getTempInteger())).getBrokenId()) {
			    hits = Degradeables.DEGRADE_HITS * 10;
			}
			if(hits > 0) {
			    d.sendNpcChat("That will be " + (REPAIR_COST_CRYSTAL * hits) + " gold to charge your", "" + itemName + ". Is this alright?", CONTENT);
			    return true;
			} else {
			    d.sendNpcChat("Hmm, I'm not sure how you managed to damage this...", "But I'll charge it anyways.", "It will be " + (REPAIR_COST_CRYSTAL * hits) + " gold to fix,", "Is this alright?", CONTENT);
			    return true;
			}
		    case 10:
			d.sendOption("Yes, that's fine.", "No, thank you.");
			return true;
		    case 11:
			switch(optionId) {
			    case 1:
				int hitsToPay = player.getDegradeableHits()[Degradeables.getDegradeableItem(toRepairCrystal.get(player.getTempInteger())).getPlayerArraySlot()];
				if(toRepairCrystal.get(player.getTempInteger()).getId() == Degradeables.getDegradeableItem(toRepairCrystal.get(player.getTempInteger())).getBrokenId()) {
				    hitsToPay = Degradeables.DEGRADE_HITS * 10;
				}
				if(player.getInventory().playerHasItem(new Item(995, hitsToPay * REPAIR_COST_CRYSTAL))) {
				    d.sendPlayerChat("Yes, that's fine.", CONTENT);
				    return true;
				} else {
				    d.sendPlayerChat("I'm sorry, I don't have that much money.", Dialogues.SAD);
				    d.endDialogue();
				    return true;
				}
			    case 2:
				d.sendPlayerChat("No, thank you.", CONTENT);
				d.endDialogue();
				return true;
			}
		    case 12:
			int amountToPay = player.getDegradeableHits()[Degradeables.getDegradeableItem(toRepairCrystal.get(player.getTempInteger())).getPlayerArraySlot()] * REPAIR_COST_CRYSTAL;
			if(toRepairCrystal.get(player.getTempInteger()).getId() == Degradeables.getDegradeableItem(toRepairCrystal.get(player.getTempInteger())).getBrokenId()) {
			    amountToPay = Degradeables.DEGRADE_HITS * 10 * REPAIR_COST_CRYSTAL;
			}
			d.sendGiveItemNpc("You exchange the money...", "...for the charged crystal equipment.", new Item(995, amountToPay), new Item(Degradeables.getDegradeableItem(toRepairCrystal.get(player.getTempInteger())).getOriginalId()));
			player.getInventory().replaceItemWithItem(toRepairCrystal.get(player.getTempInteger()), new Item(Degradeables.getDegradeableItem(toRepairCrystal.get(player.getTempInteger())).getOriginalId()));
			player.getInventory().removeItem(new Item(995, amountToPay));
			if(player.getDegradeableHits()[Degradeables.getDegradeableItem(toRepairCrystal.get(player.getTempInteger())).getPlayerArraySlot()] > 0 && !ownsDegradedVersion(player, toRepairCrystal.get(player.getTempInteger()).getId())) {
			    player.setDegradeableHits(Degradeables.getDegradeableItem(toRepairCrystal.get(player.getTempInteger())).getPlayerArraySlot(), 0);
			}
			player.setTempInteger(-1);
			return true;
		    case 15:
			d.sendPlayerChat("Thank you!", CONTENT);
			return true;
		    case 16:
			d.sendNpcChat("Sure thing.", CONTENT);
			d.endDialogue();
			return true;
		    case 17:
			String repairName1 = new Item(Degradeables.getDegradeableItem(toRepairCrystal.get(0)).getOriginalId()).getDefinition().getName();
			String repairName2 = new Item(Degradeables.getDegradeableItem(toRepairCrystal.get(1)).getOriginalId()).getDefinition().getName();
			switch(optionId) {
			    case 1:
				d.sendPlayerChat("My " + repairName1 + ", please.", CONTENT);
				player.setTempInteger(0);
				d.setNextChatId(9);
				return true;
			    case 2:
				d.sendPlayerChat("My " + repairName2 + ", please.", CONTENT);
				player.setTempInteger(1);
				d.setNextChatId(9);
				return true;
			}
		}
	    return false;
		
	    case 3790: //pc repair squire
	    case 519: //bob
		switch (player.getDialogue().getChatId()) {
		    case 1:
			if(id == 519) {
			    d.sendNpcChat("Hello " + player.getUsername() + ".", "Would you like to see my shop?", CONTENT);
			    return true;
			} else {
			    d.sendNpcChat("Hello " + player.getUsername() + ".", "How are you doing?", CONTENT);
			    return true;
			}
		    case 2:
			if(id == 519) {
			    if (!toRepair.isEmpty()) {
				d.sendOption("Yes!", "No, thank you.", "Actually, I have some broken gear here.");
				return true;
			    } else {
				d.sendOption("Yes!", "No, thank you.");
				return true;
			    }
			} else {
			    if (!toRepair.isEmpty()) {
				d.sendOption("Feeling a bit under the weather with all these pests.", "I'm doing fine, thank you.", "I have some broken gear here.");
				return true;
			    } else {
				d.sendOption("Feeling a bit under the weather with all these pests.", "I'm doing fine, thank you.");
				return true;
			    }
			}
		    case 3:
			switch(optionId) {
			    case 1:
				if(id == 519) {
				    d.sendPlayerChat("Yes!", CONTENT);
				} else {
				    d.sendPlayerChat("Feeling a bit under the weather with", "all these pests.", Dialogues.SAD);
				}
				return true;
			    case 2:
				if(id == 519) {
				    d.sendPlayerChat("No, thank you.", CONTENT);
				} else {
				    d.sendPlayerChat("I'm doing fine, thank you.", CONTENT);
				}
				d.endDialogue();
				return true;
			    case 3:
				d.sendPlayerChat("Actually, I have some broken gear here.", CONTENT);
				d.setNextChatId(6);
				return true;
			}
		    case 4:
			if(id == 519) {
			    if (player.getInteractingEntity() != null && player.getInteractingEntity().isNpc()) {
				Shops.openShop(player, ((Npc) player.getInteractingEntity()).getNpcId());
			    }
			    d.dontCloseInterface();
			} else {
			    d.sendNpcChat("Well, I hope you feel better soon.", CONTENT);
			    d.endDialogue();
			}
			return true;
		    case 6:
			d.sendNpcChat("Ah, I see you do! Let me see...", "Do you want to repair pieces individually?", "Or repair all your broken items at once?", CONTENT);
			return true;
		    case 7:
			d.sendOption("Piece by piece.", "All at once, please.");
			return true;
		    case 8:
			switch(optionId) {
			    case 1:
				d.sendPlayerChat("Piece by piece. I'd like to repair my...", CONTENT);
				return true;
			    case 2:
				d.sendPlayerChat("All at once, please. Time is money!", CONTENT);
				d.setNextChatId(30);
				return true;
			}
		    case 9:
			if(toRepair.size() >= 4) {
			    d.sendOption(optionBuilder(toRepair, 5, 0));
			    return true;
			} else if(toRepair.size() > 1) {
			    int size = (int) (4 - (Math.floor(4d / (toRepair.size()%4))));
			    d.sendOption(optionBuilder(toRepair, size, 0));
			    return true;
			} else {
			    String itemName = new Item(Degradeables.getDegradeableItem(toRepair.get(0)).getOriginalId()).getDefinition().getName();
			    d.sendPlayerChat(itemName + " please.", CONTENT);
			    d.setNextChatId(11);
			    player.setTempInteger(0);
			    return true;
			}
		    case 10:
			switch(optionId) {
			    case 1:
			    case 2:
			    case 3:
			    case 4:
				String itemName = new Item(Degradeables.getDegradeableItem(toRepair.get(optionId - 1)).getOriginalId()).getDefinition().getName();
				d.sendPlayerChat("I'd like my " + itemName + " repaired.", CONTENT);
				player.setTempInteger(optionId - 1);
				return true;
			    case 5:
				if (toRepair.size() > 5) {
				    d.setNextChatId(20);
				    if (toRepair.size() >= 8) {
					d.sendOption(optionBuilder(toRepair, 5, 4));
					return true;
				    } else {
					int size = (int) (4 - (Math.floor(4d / (toRepair.size() % 4))));
					d.sendOption(optionBuilder(toRepair, size, 4));
					return true;
				    }
				} else if((toRepair.size() - 1)%4 == 0) {
				    String itemName2 = new Item(Degradeables.getDegradeableItem(toRepair.get(optionId - 1)).getOriginalId()).getDefinition().getName();
				    d.sendPlayerChat("I'd like my " + itemName2 + " repaired.", CONTENT);
				    player.setTempInteger(optionId - 1);
				    return true;
				} else {
				    d.sendPlayerChat("Nevermind.", CONTENT);
				    d.endDialogue();
				    return true;
				}
			}
		    case 11:
			int hits = player.getDegradeableHits()[Degradeables.getDegradeableItem(toRepair.get(player.getTempInteger())).getPlayerArraySlot()];
			String itemName = new Item(Degradeables.getDegradeableItem(toRepair.get(player.getTempInteger())).getOriginalId()).getDefinition().getName();
			if(toRepair.get(player.getTempInteger()).getId() == Degradeables.getDegradeableItem(toRepair.get(player.getTempInteger())).getBrokenId()) {
			    hits = Degradeables.DEGRADE_HITS * 4;
			}
			if(hits > 0) {
			    d.sendNpcChat("That will be " + (REPAIR_COST_BARROWS * hits) + " gold to fix your", "" + itemName + ". Is this alright?", CONTENT);
			    return true;
			} else {
			    d.sendNpcChat("Hmm, I'm not sure how you managed to damage this...", "But I'll repair it anyways.", "It will be " + (REPAIR_COST_BARROWS * hits) + " gold to fix,", "Is this alright?", CONTENT);
			    return true;
			}
		    case 12:
			d.sendOption("Yes, that's fine.", "No, thank you.");
			return true;
		    case 13:
			switch(optionId) {
			    case 1:
				int hitsToPay = player.getDegradeableHits()[Degradeables.getDegradeableItem(toRepair.get(player.getTempInteger())).getPlayerArraySlot()];
				if(toRepair.get(player.getTempInteger()).getId() == Degradeables.getDegradeableItem(toRepair.get(player.getTempInteger())).getBrokenId()) {
				    hitsToPay = Degradeables.DEGRADE_HITS * 4;
				}
				if(player.getInventory().playerHasItem(new Item(995, hitsToPay * REPAIR_COST_BARROWS))) {
				    d.sendPlayerChat("Yes, that's fine.", CONTENT);
				    return true;
				} else {
				    d.sendPlayerChat("I'm sorry, I don't have that much money.", Dialogues.SAD);
				    d.endDialogue();
				    return true;
				}
			    case 2:
				d.sendPlayerChat("No, thank you.", CONTENT);
				d.endDialogue();
				return true;
			}
		    case 14:
			int amountToPay = player.getDegradeableHits()[Degradeables.getDegradeableItem(toRepair.get(player.getTempInteger())).getPlayerArraySlot()] * REPAIR_COST_BARROWS;
			if(toRepair.get(player.getTempInteger()).getId() == Degradeables.getDegradeableItem(toRepair.get(player.getTempInteger())).getBrokenId()) {
			    amountToPay = Degradeables.DEGRADE_HITS * 4 * REPAIR_COST_BARROWS;
			}
			d.sendGiveItemNpc("You exchange the money...", "...for the repaired piece of equipment.", new Item(995, amountToPay), new Item(Degradeables.getDegradeableItem(toRepair.get(player.getTempInteger())).getOriginalId()));
			player.getInventory().replaceItemWithItem(toRepair.get(player.getTempInteger()), new Item(Degradeables.getDegradeableItem(toRepair.get(player.getTempInteger())).getOriginalId()));
			player.getInventory().removeItem(new Item(995, amountToPay));
			if(player.getDegradeableHits()[Degradeables.getDegradeableItem(toRepair.get(player.getTempInteger())).getPlayerArraySlot()] > 0 && !ownsDegradedVersion(player, toRepair.get(player.getTempInteger()).getId())) {
			    player.setDegradeableHits(Degradeables.getDegradeableItem(toRepair.get(player.getTempInteger())).getPlayerArraySlot(), 0);
			}
			player.setTempInteger(-1);
			return true;
		    case 15:
			d.sendPlayerChat("Thank you!", CONTENT);
			return true;
		    case 16:
			if(toRepair.size() > 1) {
			    d.sendNpcChat("Sure thing, would you like to repair another piece?", CONTENT);
			    return true;
			} else {
			    d.sendNpcChat("Sure thing.", CONTENT);
			    d.endDialogue();
			    return true;
			}
		    case 17:
			d.sendOption("Yes, please.", "No, thank you.");
			return true;
		    case 18:
			switch(optionId) {
			    case 1:
				d.sendPlayerChat("Yes, please. I'd like to repair my...", CONTENT);
				d.setNextChatId(9);
				return true;
			    case 2:
				d.sendPlayerChat("No, thank you.", CONTENT);
				d.endDialogue();
				return true;
			}
		    case 20:
			switch(optionId) {
			    case 1:
			    case 2:
			    case 3:
			    case 4:
				String itemName2 = new Item(Degradeables.getDegradeableItem(toRepair.get(optionId + 3)).getOriginalId()).getDefinition().getName();
				d.sendPlayerChat("I'd like my " + itemName2 + " repaired.", CONTENT);
				d.setNextChatId(11);
				player.setTempInteger(optionId + 3);
				return true;
			    case 5:
				if (toRepair.size() > 9) {
				    if (toRepair.size() >= 12) {
					d.sendOption(optionBuilder(toRepair, 5, 8));
					return true;
				    } else {
					int size = (int) (4 - (Math.floor(4d / (toRepair.size() % 4))));
					d.sendOption(optionBuilder(toRepair, size, 8));
					return true;
				    }
				} else if((toRepair.size() - 1)%4 == 0) {
				    String itemName3 = new Item(Degradeables.getDegradeableItem(toRepair.get(optionId + 3)).getOriginalId()).getDefinition().getName();
				    d.sendPlayerChat("I'd like my " + itemName3 + " repaired.", CONTENT);
				    d.setNextChatId(11);
				    player.setTempInteger(optionId + 3);
				    return true;
				} else {
				    d.sendPlayerChat("Nevermind.", CONTENT);
				    d.endDialogue();
				    return true;
				}
			}
		    case 21:
			switch(optionId) {
			    case 1:
			    case 2:
			    case 3:
			    case 4:
				String itemName3 = new Item(Degradeables.getDegradeableItem(toRepair.get(optionId + 7)).getOriginalId()).getDefinition().getName();
				d.sendPlayerChat("I'd like my " + itemName3 + " repaired.", CONTENT);
				d.setNextChatId(11);
				player.setTempInteger(optionId + 7);
				return true;
			    case 5:
				if (toRepair.size() > 13) {
				    if (toRepair.size() >= 16) {
					d.sendOption(optionBuilder(toRepair, 5, 12));
					return true;
				    } else {
					int size = (int) (4 - (Math.floor(4d / (toRepair.size() % 4))));
					d.sendOption(optionBuilder(toRepair, size, 12));
					return true;
				    }
				} else if((toRepair.size() - 1)%4 == 0) {
				    String itemName4 = new Item(Degradeables.getDegradeableItem(toRepair.get(optionId + 7)).getOriginalId()).getDefinition().getName();
				    d.sendPlayerChat("I'd like my " + itemName4 + " repaired.", CONTENT);
				    d.setNextChatId(11);
				    player.setTempInteger(optionId + 7);
				    return true;
				} else {
				    d.sendPlayerChat("Nevermind.", CONTENT);
				    d.endDialogue();
				    return true;
				}
			}
		    case 22:
			switch(optionId) {
			    case 1:
			    case 2:
			    case 3:
			    case 4:
				String itemName3 = new Item(Degradeables.getDegradeableItem(toRepair.get(optionId + 11)).getOriginalId()).getDefinition().getName();
				d.sendPlayerChat("I'd like my " + itemName3 + " repaired.", CONTENT);
				d.setNextChatId(11);
				player.setTempInteger(optionId + 11);
				return true;
			    case 5:
				if (toRepair.size() > 17) {
				    if (toRepair.size() >= 20) {
					d.sendOption(optionBuilder(toRepair, 5, 16));
					return true;
				    } else {
					int size = (int) (4 - (Math.floor(4d / (toRepair.size() % 4))));
					d.sendOption(optionBuilder(toRepair, size, 16));
					return true;
				    }
				} else if((toRepair.size() - 1)%4 == 0) {
				    String itemName4 = new Item(Degradeables.getDegradeableItem(toRepair.get(optionId + 11)).getOriginalId()).getDefinition().getName();
				    d.sendPlayerChat("I'd like my " + itemName4 + " repaired.", CONTENT);
				    d.setNextChatId(11);
				    player.setTempInteger(optionId + 11);
				    return true;
				} else {
				    d.sendPlayerChat("Nevermind.", CONTENT);
				    d.endDialogue();
				    return true;
				}
			}
		    case 23:
			switch(optionId) {
			    case 1:
			    case 2:
			    case 3:
			    case 4:
				String itemName3 = new Item(Degradeables.getDegradeableItem(toRepair.get(optionId + 15)).getOriginalId()).getDefinition().getName();
				d.sendPlayerChat("I'd like my " + itemName3 + " repaired.", CONTENT);
				d.setNextChatId(11);
				player.setTempInteger(optionId + 15);
				return true;
			    case 5:
				if (toRepair.size() > 21) {
				    if (toRepair.size() >= 24) {
					d.sendOption(optionBuilder(toRepair, 5, 20));
					return true;
				    } else {
					int size = (int) (4 - (Math.floor(4d / (toRepair.size() % 4))));
					d.sendOption(optionBuilder(toRepair, size, 20));
					return true;
				    }
				} else if((toRepair.size() - 1)%4 == 0) {
				    String itemName4 = new Item(Degradeables.getDegradeableItem(toRepair.get(optionId + 15)).getOriginalId()).getDefinition().getName();
				    d.sendPlayerChat("I'd like my " + itemName4 + " repaired.", CONTENT);
				    d.setNextChatId(11);
				    player.setTempInteger(optionId + 15);
				    return true;
				} else {
				    d.sendPlayerChat("Nevermind.", CONTENT);
				    d.endDialogue();
				    return true;
				}
			}
		    case 24:
			switch(optionId) {
			    case 1:
			    case 2:
			    case 3:
			    case 4:
				String itemName3 = new Item(Degradeables.getDegradeableItem(toRepair.get(optionId + 19)).getOriginalId()).getDefinition().getName();
				d.sendPlayerChat("I'd like my " + itemName3 + " repaired.", CONTENT);
				d.setNextChatId(11);
				player.setTempInteger(optionId + 19);
				return true;
			    case 5:
				d.sendPlayerChat("Nevermind.", CONTENT);
				d.endDialogue();
				return true;
			}
		    case 30:
			int hitsAll = 0;
			for(Item i : toRepair) {
			    if(i.getId() == Degradeables.getDegradeableItem(i).getBrokenId()) {
				hitsAll += Degradeables.DEGRADE_HITS * 4;
			    } else {
				hitsAll += player.getDegradeableHits()[Degradeables.getDegradeableItem(i).getPlayerArraySlot()];
			    }
			}
			int amountToPayAll = hitsAll * REPAIR_COST_BARROWS;
			player.setTempInteger(amountToPayAll);
			if(hitsAll > 0) {
			    d.sendNpcChat("That will be " + (amountToPayAll) + " gold to fix all your items.", "Is this alright?", CONTENT);
			    return true;
			} else {
			    d.sendNpcChat("Hmm, I'm not sure how you managed this...", "...Panic! I'll need to see the items individually.", CONTENT);
			    d.endDialogue();
			    return true;
			}
		    case 31:
			d.sendOption("Yes, that's fine.", "No, thank you.");
			return true;
		    case 32:
			switch(optionId) {
			    case 1:
				if(player.getInventory().playerHasItem(new Item(995, player.getTempInteger()))) {
				    d.sendPlayerChat("Yes, that's fine.", CONTENT);
				    return true;
				} else {
				    d.sendPlayerChat("I'm sorry, I don't have that much money.", Dialogues.SAD);
				    d.endDialogue();
				    return true;
				}
			    case 2:
				d.sendPlayerChat("No, thank you.", CONTENT);
				d.endDialogue();
				return true;
			}
		    case 33:
			d.sendGiveItemNpc("You exchange the money for the repairs.", new Item(995, player.getTempInteger()));
			for(Item i : toRepair) {
			    player.getInventory().replaceItemWithItem(i, new Item(Degradeables.getDegradeableItem(i).getOriginalId()));
			    if(player.getDegradeableHits()[Degradeables.getDegradeableItem(i).getPlayerArraySlot()] > 0 && !ownsDegradedVersion(player, i.getId())) {
				player.setDegradeableHits(Degradeables.getDegradeableItem(i).getPlayerArraySlot(), 0);
			    }
			}
			player.getInventory().removeItem(new Item(995, player.getTempInteger()));
			player.setTempInteger(-1);
			return true;
		    case 34:
			d.sendPlayerChat("Thank you!", CONTENT);
			return true;   
		}
	    return false;
	}
	return false;
    }
}

