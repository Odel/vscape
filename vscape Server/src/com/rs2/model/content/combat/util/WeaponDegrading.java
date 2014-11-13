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
import com.rs2.model.players.container.equipment.Equipment;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 24/03/12 Time: 12:17 To change
 * this template use File | Settings | File Templates.
 */
public class WeaponDegrading {
	
	/*public static void degradeEquipment(Player player) {
		for (int i = 0; i < Equipment.SIZE; i++)
			degradeSpecificEquipment(player, i, player.getEquipment().getItemContainer().get(i));
			player.getEquipment().refresh();
	}

	public static void degradeSpecificEquipment(Player player, int slot, Item equipment) {
		if (equipment == null)
			return;
		updateDegradingEquipment(player, slot, equipment);
		if (player.getEquipment().getItemContainer().get(slot).getTimer() >= 0) {
		}	 //if time to degrade
	}*/
	
	public static void handlePlayerHit(final Player player) {
	    Item equipped = player.getEquipment().getItemContainer().get(Constants.WEAPON);
	    Degradeables item = Degradeables.getDegradeableItem(equipped);
	    if (item == null || !Constants.DEGRADING_ENABLED) {
		return;
	    }
	    if (item.getEquipSlot() == Constants.WEAPON) {
		if (equipped.getId() == item.getOriginalId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == 0) {
		    player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
		    player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getFirstDegradeId()));
		    player.getEquipment().refresh();
		    player.setDegradeableHits(item.getPlayerArraySlot(), player.getDegradeableHits()[item.getPlayerArraySlot()] + 1);
		} else if (equipped.getId() == item.getFirstDegradeId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS) {
		    player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
		    player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getSecondDegradeId()));
		    player.getEquipment().refresh();
		} else if (equipped.getId() == item.getSecondDegradeId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS * 2) {
		    player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
		    player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getThirdDegradeId()));
		    player.getEquipment().refresh();
		} else if (equipped.getId() == item.getThirdDegradeId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS * 3) {
		    player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
		    player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getFourthDegradeId()));
		    player.getEquipment().refresh();
		} else if (equipped.getId() == item.getFourthDegradeId() && item.getFifthDegradeId() == -1 && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS * 4) {
		    player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has broken.");
		    player.setDegradeableHits(item.getPlayerArraySlot(), 0);
		    if (player.getInventory().canAddItem(new Item(item.getBrokenId()))) {
			player.getInventory().addItem(new Item(item.getBrokenId()));
		    } else {
			GroundItem dropItem = new GroundItem(new Item(item.getBrokenId()), player, player.getPosition().clone());
			GroundItemManager.getManager().dropItem(dropItem);
		    }
		    player.getEquipment().unequip(item.getEquipSlot());
		    player.getInventory().removeItem(equipped);
		    player.getEquipment().refresh();
		    player.setAppearanceUpdateRequired(true);
		} else if (equipped.getId() == item.getFourthDegradeId() && item.getFifthDegradeId() != -1 && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS * 4) {
		    player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
		    player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getFifthDegradeId()));
		    player.getEquipment().refresh();
		} else if (equipped.getId() == item.getFifthDegradeId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS * 5) {
		    player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
		    player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getSixthDegradeId()));
		    player.getEquipment().refresh();
		} else if (equipped.getId() == item.getSixthDegradeId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS * 6) {
		    player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
		    player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getSeventhDegradeId()));
		    player.getEquipment().refresh();
		} else if (equipped.getId() == item.getSeventhDegradeId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS * 7) {
		    player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
		    player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getEigthDegradeId()));
		    player.getEquipment().refresh();
		} else if (equipped.getId() == item.getEigthDegradeId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS * 8) {
		    player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
		    player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getNinthDegradeId()));
		    player.getEquipment().refresh();
		} else if (equipped.getId() == item.getNinthDegradeId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS * 9) {
		    player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
		    player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getTenthDegradeId()));
		    player.getEquipment().refresh();
		} else if (equipped.getId() == item.getTenthDegradeId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS * 10) {
		    player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has broken.");
		    player.setDegradeableHits(item.getPlayerArraySlot(), 0);
		    if (player.getInventory().canAddItem(new Item(item.getBrokenId()))) {
			player.getInventory().addItem(new Item(item.getBrokenId()));
		    } else {
			GroundItem dropItem = new GroundItem(new Item(item.getBrokenId()), player, player.getPosition().clone());
			GroundItemManager.getManager().dropItem(dropItem);
		    }
		    player.getEquipment().unequip(item.getEquipSlot());
		    player.getInventory().removeItem(equipped);
		    player.getEquipment().refresh();
		    player.setAppearanceUpdateRequired(true);
		} else {
		    player.setDegradeableHits(item.getPlayerArraySlot(), player.getDegradeableHits()[item.getPlayerArraySlot()] + 1);
		}
	    } 
	}
	public static void handleNpcHit(final Player player) {
	    Degradeables item = getRandomEquipped(player);
	    if (item == null || !Constants.DEGRADING_ENABLED) {
		return;
	    }
	    Item equipped = player.getEquipment().getItemContainer().get(item.getEquipSlot());
	    if (equipped.getId() == item.getOriginalId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == 0) {
		player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
		player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getFirstDegradeId()));
		player.getEquipment().refresh();
		player.setDegradeableHits(item.getPlayerArraySlot(), player.getDegradeableHits()[item.getPlayerArraySlot()] + 1);
	    } else if (equipped.getId() == item.getFirstDegradeId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS) {
		player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
		player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getSecondDegradeId()));
		player.getEquipment().refresh();
	    } else if (equipped.getId() == item.getSecondDegradeId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS * 2) {
		player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
		player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getThirdDegradeId()));
		player.getEquipment().refresh();
	    } else if (equipped.getId() == item.getThirdDegradeId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS * 3) {
		player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
		player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getFourthDegradeId()));
		player.getEquipment().refresh();
	    } else if (equipped.getId() == item.getFourthDegradeId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS * 4) {
		player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has broken.");
		if (player.getInventory().canAddItem(new Item(item.getBrokenId()))) {
		    player.getInventory().addItem(new Item(item.getBrokenId()));
		} else {
		    GroundItem dropItem = new GroundItem(new Item(item.getBrokenId()), player, player.getPosition().clone());
		    GroundItemManager.getManager().dropItem(dropItem);
		}
		player.getEquipment().getItemContainer().set(item.getEquipSlot(), null);
		player.getEquipment().refresh();
	    } else {
		player.setDegradeableHits(item.getPlayerArraySlot(), player.getDegradeableHits()[item.getPlayerArraySlot()] + 1);
	    }
	}
	
	public static String[] optionBuilder(final ArrayList<Item> items, int options, int offset) {
	    if(options > 5) {
		return new String[]{"Nevermind."};
	    }
	    String[] toReturn = new String[options];
	    for(int i = 0; i < options; i++) {
		toReturn[i] = "" + new Item(Degradeables.getDegradeableItem(items.get(i + offset)).getOriginalId()).getDefinition().getName();
	    }
	    if(options > 4) {
		if(offset == 20) {
		    toReturn[4] = "Nevermind.";
		} else {
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
		if(i.getDefinition().getName().contains(" 100") || i.getDefinition().getName().contains(" 75")
		|| i.getDefinition().getName().contains(" 25") || i.getDefinition().getName().contains(" 0")) {
		    if(!toReturn.contains(i)) {
			toReturn.add(i);
			//player.getActionSender().sendMessage("" + i.getDefinition().getName());
		    }
		    
		}
		
	    }
	    return toReturn;
	}
	
	private static Degradeables getRandomEquipped(final Player player) {
	    ArrayList<Degradeables> equippedDegradeables = new ArrayList<Degradeables>();
	    for(int x = 0; x < 14; x++) {
		Item equipped = player.getEquipment().getItemContainer().get(x);
		Degradeables item = Degradeables.getDegradeableItem(equipped);
		if(item == null) continue;
		if(item.getEquipSlot() == Constants.WEAPON) continue;
		else {
		    equippedDegradeables.add(item);
		}
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
	private static void updateDegradingEquipment(Player player, int slot, Item equipment) {
		/*for (String s : barrowsEquipments) {
			if (equipment.getDefinition().getName().toLowerCase().contains(s) && !(equipment.getId() >= 4856 && equipment.getId() <= 4999)) {
				player.getEquipment().getItemContainer().set(slot, new Item(getFirstDegraded(equipment.getId())));
				player.getEquipment().getItemContainer().get(slot).setTimer(5625);// lasting
																					// 15
																					// hours
			}
		}
		if (equipment.getId() >= 4856 && equipment.getId() <= 4999 && !equipment.getDefinition().getName().toLowerCase().contains(" 0")) {
			if (player.getEquipment().getItemContainer().get(slot).getTimer() == 0) {
				player.getEquipment().getItemContainer().set(slot, new Item(equipment.getId() + 1));
				if (!equipment.getDefinition().getName().toLowerCase().contains(" 25"))
					player.getEquipment().getItemContainer().get(slot).setTimer(5625);
				else
					player.getActionSender().sendMessage("Your barrows equipment has broken.");
			}
		} */
	    /*
		if (equipment.getDefinition().getName().toLowerCase().contains("crystal")) {
			if (equipment.getDefinition().getId() == 4212 || equipment.getDefinition().getId() == 4224) {
				player.getEquipment().getItemContainer().set(slot, new Item(equipment.getDefinition().getId() == 4212 ? 4214 : 4225));
				player.getEquipment().getItemContainer().get(slot).setTimer(250);// lasting
																					// 2500
																					// shots
			} else {
				if (player.getEquipment().getItemContainer().get(slot).getTimer() == 0) {
					if (equipment.getDefinition().getId() == 4223 || equipment.getDefinition().getId() == 4234)
						player.getEquipment().getItemContainer().remove(equipment);

					player.getEquipment().getItemContainer().set(slot, new Item(equipment.getId() + 1));
					player.getEquipment().getItemContainer().get(slot).setTimer(250);
				}
			}
		}
		    */
	}
	
    public static boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
	DialogueManager d = player.getDialogue();
	ArrayList<Item> toRepair = getDegradedBarrowsInInventory(player);
	switch (id) {
	    case 519:
		switch (player.getDialogue().getChatId()) {
		    case 1:
			d.sendNpcChat("Hello " + player.getUsername() + ".", "Would you like to see my shop?", CONTENT);
			return true;
		    case 2:
			if(!toRepair.isEmpty()) {
			    d.sendOption("Yes!", "No, thank you.", "Actually, I have some broken gear here.");
			    return true;
			} else {
			    d.sendOption("Yes!", "No, thank you.");
			    return true;
			}
		    case 3:
			switch(optionId) {
			    case 1:
				d.sendPlayerChat("Yes!", CONTENT);
				return true;
			    case 2:
				d.sendPlayerChat("No, thank you.", CONTENT);
				d.endDialogue();
				return true;
			    case 3:
				d.sendPlayerChat("Actually, I have some broken gear here.", CONTENT);
				d.setNextChatId(6);
				return true;
			}
		    case 4:
			if (player.getInteractingEntity() != null && player.getInteractingEntity().isNpc()) {
			    Shops.openShop(player, ((Npc) player.getInteractingEntity()).getNpcId());
			}
			d.dontCloseInterface();
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
			if(toRepair.size() > 4) {
			    d.sendOption(optionBuilder(toRepair, 5, 0));
			    return true;
			} else {
			    int size = (int) (4 - (Math.floor(4d / (toRepair.size()%4))));
			    d.sendOption(optionBuilder(toRepair, size, 0));
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
				d.setNextChatId(20);
				if (toRepair.size() > 8) {
				    d.sendOption(optionBuilder(toRepair, 5, 4));
				    return true;
				} else {
				    int size = (int) (4 - (Math.floor(4d / (toRepair.size() % 4))));
				    d.sendOption(optionBuilder(toRepair, size, 4));
				    return true;
				}
			}
		    case 11:
			int hits = player.getDegradeableHits()[Degradeables.getDegradeableItem(toRepair.get(player.getTempInteger())).getPlayerArraySlot()];
			String itemName = new Item(Degradeables.getDegradeableItem(toRepair.get(player.getTempInteger())).getOriginalId()).getDefinition().getName();
			if(hits > 0) {
			    d.sendNpcChat("That will be " + (10 * hits) + " gold to fix your", "" + itemName + ". Is this alright?", CONTENT);
			    return true;
			} else {
			    d.sendNpcChat("Hmm, I'm not sure how you managed to damage this...", "But I'll repair it anyways.", "It will be " + (10 * hits) + " gold to fix,", "Is this alright?", CONTENT);
			    return true;
			}
		    case 12:
			d.sendOption("Yes, that's fine.", "No, thank you.");
			return true;
		    case 13:
			switch(optionId) {
			    case 1:
				int hitsToPay = player.getDegradeableHits()[Degradeables.getDegradeableItem(toRepair.get(player.getTempInteger())).getPlayerArraySlot()];
				if(player.getInventory().playerHasItem(new Item(995, hitsToPay * 10))) {
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
			int amountToPay = player.getDegradeableHits()[Degradeables.getDegradeableItem(toRepair.get(player.getTempInteger())).getPlayerArraySlot()] * 10;
			d.sendGiveItemNpc("You exchange the money...", "...for the repaired piece of equipment.", new Item(995, amountToPay), toRepair.get(player.getTempInteger()));
			player.getInventory().replaceItemWithItem(toRepair.get(player.getTempInteger()), new Item(Degradeables.getDegradeableItem(toRepair.get(player.getTempInteger())).getOriginalId()));
			player.getInventory().removeItem(new Item(995, amountToPay));
			player.setDegradeableHits(Degradeables.getDegradeableItem(toRepair.get(player.getTempInteger())).getPlayerArraySlot(), 0);
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
				if (toRepair.size() > 12) {
				    d.sendOption(optionBuilder(toRepair, 5, 8));
				    return true;
				} else {
				    int size = (int) (4 - (Math.floor(4d / (toRepair.size() % 4))));
				    d.sendOption(optionBuilder(toRepair, size, 8));
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
				if (toRepair.size() > 16) {
				    d.sendOption(optionBuilder(toRepair, 5, 12));
				    return true;
				} else {
				    int size = (int) (4 - (Math.floor(4d / (toRepair.size() % 4))));
				    d.sendOption(optionBuilder(toRepair, size, 12));
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
				if (toRepair.size() > 20) {
				    d.sendOption(optionBuilder(toRepair, 5, 16));
				    return true;
				} else {
				    int size = (int) (4 - (Math.floor(4d / (toRepair.size() % 4))));
				    d.sendOption(optionBuilder(toRepair, size, 16));
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
				if (toRepair.size() > 24) {
				    d.sendOption(optionBuilder(toRepair, 5, 20));
				    return true;
				} else {
				    int size = (int) (4 - (Math.floor(4d / (toRepair.size() % 4))));
				    d.sendOption(optionBuilder(toRepair, size, 20));
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
			    hitsAll += player.getDegradeableHits()[Degradeables.getDegradeableItem(i).getPlayerArraySlot()];
			}
			int amountToPayAll = hitsAll * 10;
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
			    player.setDegradeableHits(Degradeables.getDegradeableItem(i).getPlayerArraySlot(), 0);
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
