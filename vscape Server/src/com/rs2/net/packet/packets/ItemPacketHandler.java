package com.rs2.net.packet.packets;

import com.rs2.Constants;
import com.rs2.cache.interfaces.RSInterface;
import com.rs2.model.Position;
import com.rs2.model.content.Pets;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.minigames.barrows.Barrows;
import com.rs2.model.content.skills.Menus;
import com.rs2.model.content.skills.Tools;
import com.rs2.model.content.skills.Crafting.BasicCraft;
import com.rs2.model.content.skills.Crafting.GemCrafting;
import com.rs2.model.content.skills.Crafting.GemCutting;
import com.rs2.model.content.skills.Crafting.GemData;
import com.rs2.model.content.skills.Crafting.GlassMaking;
import com.rs2.model.content.skills.Crafting.LeatherMakingHandler;
import com.rs2.model.content.skills.Fletching.ArrowMaking;
import com.rs2.model.content.skills.Fletching.BowStringing;
import com.rs2.model.content.skills.Fletching.LogCuttingInterfaces;
import com.rs2.model.content.skills.cooking.OneIngredients;
import com.rs2.model.content.skills.cooking.ThreeIngredients;
import com.rs2.model.content.skills.cooking.TwoIngredients;
import com.rs2.model.content.skills.farming.MithrilSeeds;
import com.rs2.model.content.skills.herblore.Cleaning;
import com.rs2.model.content.skills.herblore.Coconut;
import com.rs2.model.content.skills.herblore.Grinding;
import com.rs2.model.content.skills.herblore.PoisoningWeapon;
import com.rs2.model.content.skills.herblore.PotionMaking;
import com.rs2.model.content.skills.magic.MagicSkill;
import com.rs2.model.content.skills.magic.Spell;
import com.rs2.model.content.skills.prayer.GodBook;
import com.rs2.model.content.skills.runecrafting.Pouches;
import com.rs2.model.content.skills.runecrafting.Runecrafting;
import com.rs2.model.content.skills.slayer.Slayer;
import com.rs2.model.content.skills.smithing.SmithBars;
import com.rs2.model.content.treasuretrails.AnagramsScrolls;
import com.rs2.model.content.treasuretrails.ChallengeScrolls;
import com.rs2.model.content.treasuretrails.ClueScroll;
import com.rs2.model.content.treasuretrails.CoordinateScrolls;
import com.rs2.model.content.treasuretrails.DiggingScrolls;
import com.rs2.model.content.treasuretrails.MapScrolls;
import com.rs2.model.content.treasuretrails.Puzzle;
import com.rs2.model.content.treasuretrails.SearchScrolls;
import com.rs2.model.content.treasuretrails.Sextant;
import com.rs2.model.content.treasuretrails.SpeakToScrolls;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.players.BankManager;
import com.rs2.model.players.Player;
import com.rs2.model.players.ShopManager;
import com.rs2.model.players.TradeManager;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemManager;
import com.rs2.model.players.item.functions.Casket;
import com.rs2.model.players.item.functions.Nests;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.net.StreamBuffer;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;


public class ItemPacketHandler implements PacketHandler {

	public static final int DROP_ITEM = 87;
	public static final int PICKUP_ITEM = 236;
	public static final int SECOND_GROUND_OPTION_ITEM = 253;
	public static final int HANDLE_OPTIONS = 214;
	public static final int CLICK_1 = 145;
	public static final int CLICK_5 = 117;
	public static final int CLICK_10 = 43;
	public static final int CLICK_ALL = 129;
	public static final int EQUIP_ITEM = 41;
	public static final int USE_ITEM_ON_ITEM = 53;
	public static final int USE_ITEM_ON_GROUND_ITEM = 25;
	public static final int CASTED_SPELL_ON_ITEM = 237;
	public static final int CASTED_SPELL_ON_GROUND_ITEM = 181;

	public static final int FIRST_CLICK_ITEM = 122;
	public static final int SECOND_CLICK_ITEM = 16;
	public static final int THIRD_CLICK_ITEM = 75;

	@Override
	public void handlePacket(Player player, Packet packet) {
		if (player.stopPlayerPacket()) {
			return;
		}
		switch (packet.getOpcode()) {
			case HANDLE_OPTIONS :
				handleOptions(player, packet);
				return;
		}
		player.resetAllActions();
		switch (packet.getOpcode()) {
			case USE_ITEM_ON_GROUND_ITEM :
				useItemOnGroundItem(player, packet);
				break;
			case USE_ITEM_ON_ITEM :
				useItemOnItem(player, packet);
				break;
			case DROP_ITEM :
				handleDropItem(player, packet);
				break;
			case PICKUP_ITEM :
				handlePickupItem(player, packet);
				break;
			case SECOND_GROUND_OPTION_ITEM :
				handlePickupSecondItem(player, packet);
				break;
			case CLICK_1 :
				handleClick1(player, packet);
				break;
			case CLICK_5 :
				handleClick5(player, packet);
				break;
			case CLICK_10 :
				handleClick10(player, packet);
				break;
			case CLICK_ALL :
				handleClickAll(player, packet);
				break;
			case EQUIP_ITEM :
				handleEquipItem(player, packet);
				break;
			case FIRST_CLICK_ITEM :
				handleFirstClickItem(player, packet);
				break;
			case SECOND_CLICK_ITEM :
				handleSecondClickItem(player, packet);
				break;
			case THIRD_CLICK_ITEM :
				handleThirdClickItem(player, packet);
				break;
			case CASTED_SPELL_ON_ITEM :
				handleCastedSpellOnItem(player, packet);
				break;
			case CASTED_SPELL_ON_GROUND_ITEM :
				handleCastedSpellOnGroundItem(player, packet);
				break;
		}
	}

	private void handleDropItem(Player player, Packet packet) {
		int itemId = packet.getIn().readShort(StreamBuffer.ValueType.A);
		packet.getIn().readShort();
		player.setSlot(packet.getIn().readShort(StreamBuffer.ValueType.A));
		Item item = player.getInventory().getItemContainer().get(player.getSlot());
		if(Puzzle.moveSlidingPiece(player, itemId))
            return;
		if (item == null || item.getId() != itemId || !item.validItem())
			return;
		if (item.getDefinition().isStackable()) {
			item.setCount(player.getInventory().getItemContainer().getCount(item.getId()));
		} else {
			item.setCount(1);
		}
		if (!player.getInventory().getItemContainer().contains(item.getId())) {
			return;
		}
		for (int[] element : Pets.PET_IDS) {
			if (item.getDefinition().getId() == element[0]) {
				player.getPets().registerPet(element[0], element[1]);
				return;
			}
		}
		if (item.getDefinition().isUntradable()) {
			String[][] info = {{"Are you sure you want to drop this item?", "14174"}, {"Yes.", "14175"}, {"No.", "14176"}, {"", "14177"}, {"Dropping this item will make you lose it forever.", "14182"}, {"", "14183"}, {item.getDefinition().getName(), "14184"}};
			player.getActionSender().sendUpdateItem(item, 0, 14171, 1);
			for (String[] element : info) {
				player.getActionSender().sendString(element[0], Integer.parseInt(element[1]));
			}
			player.setDestroyItem(item);
			player.getActionSender().sendChatInterface(14170);
			return;
		}
		if (player.getInventory().getItemContainer().contains(item.getId())) {
			player.getActionSender().sendSound(376, 1, 0);
	        if (!Constants.ADMINS_CAN_INTERACT && player.getStaffRights() >= 2) {
	            player.getActionSender().sendMessage("Your item disappears because you're an administrator.");
	        } else {
                GroundItemManager.getManager().dropItem(new GroundItem(new Item(item.getId(), item.getCount()), player));
	        }
			if (!player.getInventory().removeItemSlot(item, player.getSlot())) {
				player.getInventory().removeItem(item);
			}
		}
		player.getEquipment().updateWeight();
	}

	private void useItemOnItem(Player player, Packet packet) {
		int itemSecondClickSlot = packet.getIn().readShort();
		int itemFirstClickSlot = packet.getIn().readShort(StreamBuffer.ValueType.A);
		packet.getIn().readShort();
		packet.getIn().readShort();
		if (itemFirstClickSlot > 28 || itemSecondClickSlot > 28) {
			return;
		}
		Item firstClickItem = player.getInventory().getItemContainer().get(itemFirstClickSlot);
		Item secondClickItem = player.getInventory().getItemContainer().get(itemSecondClickSlot);
		if (firstClickItem == null || secondClickItem == null || !firstClickItem.validItem() || !secondClickItem.validItem())
			return;
		int firstItem = firstClickItem.getId();
		int secondItem = secondClickItem.getId();
		if (player.getDuelMainData().getOpponent() != null){
			player.getDuelInteraction().endDuelInteraction(true);
            return;
        }
		if(OneIngredients.mixItems(player, firstItem, secondItem, itemFirstClickSlot, itemSecondClickSlot))
            return;
		if(TwoIngredients.mixItems(player, firstItem, secondItem, itemFirstClickSlot, itemSecondClickSlot))
            return;
		if(ThreeIngredients.mixItems(player, firstItem, secondItem, itemFirstClickSlot, itemSecondClickSlot))
            return;
		/* Fletching */
		if(ArrowMaking.perform(player, firstItem, secondItem))
            return;
		if(BowStringing.perform(player, firstItem, secondItem))
            return;
		if (LogCuttingInterfaces.handleItemOnItem(player, firstItem, secondItem))
			return;
		/* Crafting */
		if(GemCutting.handleCutting(player, firstItem, secondItem, firstItem != GemCutting.CHISEL ? itemFirstClickSlot : itemSecondClickSlot))
            return;
		if(LeatherMakingHandler.handleItemOnItem(player, firstItem, secondItem, itemFirstClickSlot, itemSecondClickSlot))
            return;
		if(BasicCraft.handleItemOnItem(player, firstItem, secondItem))
            return;
		if ((firstItem == GlassMaking.GLASSBLOWING_PIPE && secondItem == GlassMaking.MOLTEN_GLASS) || (secondItem == GlassMaking.GLASSBLOWING_PIPE && firstItem == GlassMaking.MOLTEN_GLASS)) {
			Menus.sendSkillMenu(player, "glassMaking");
			return;
		}
		/* STRINGING AMULETS */
		for (int i = 0; i < GemData.stringItems.length; i++) {
			if (GemData.stringItems[i][0] == firstItem || GemData.stringItems[i][0] == secondItem) {
				GemCrafting.string(player, i);
				return;
			}
		}
		/* Farming */
		if (player.getSeedling().placeSeedInPot(firstClickItem.getId(), secondClickItem.getId(), itemFirstClickSlot, itemSecondClickSlot)) {
			return;
		}
		if (player.getSeedling().waterSeedling(firstClickItem.getId(), secondClickItem.getId(), itemFirstClickSlot, itemSecondClickSlot)) {
			return;
		}
		if (player.getItemOnItem().handleItemOnItem(firstClickItem, secondClickItem, itemFirstClickSlot, itemSecondClickSlot)) {
			return;
		}
		if (Tools.attachTool(player, firstItem, secondItem)) {
			player.getActionSender().sendMessage("You put together the head and handle.");
    		return;
		}
        /*Slayer*/
        if(player.getSlayer().handleItemOnItem(firstItem, secondItem)){
            return;
        }
		/* Herblore */
		if (PotionMaking.createPotion(player, firstClickItem, secondClickItem, itemFirstClickSlot, itemSecondClickSlot)) {
			return;
		}
		if (Grinding.createProduct(player, firstClickItem, secondClickItem, itemFirstClickSlot, itemSecondClickSlot)) {
			return;
		}
		if (PoisoningWeapon.handlePoison(player, firstClickItem, secondClickItem)) {
			return;
		}
		if (Coconut.handleCoconut(player, firstClickItem, secondClickItem)) {
			return;
		}
		if (PotionMaking.combineDose(player, firstItem, secondItem, itemFirstClickSlot, itemSecondClickSlot)) {
			return;
		}
		/* Firemaking */
		if (firstItem == 590 || secondItem == 590) {
			if (firstItem == 596 && secondItem == 596) {
				if (player.getInventory().removeItem(new Item(596))) {
					player.getInventory().addItem(new Item(594));
					player.getActionSender().sendMessage("You light the torch.");
					return;
				}
			}
			if (firstItem == 36 && secondItem == 36) {
				if (player.getInventory().removeItem(new Item(36))) {
					player.getInventory().addItem(new Item(33));
					player.getActionSender().sendMessage("You light the candle.");
					return;
				}
			}
			player.getFiremaking().attemptFire(firstItem, secondItem, false, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
			return;
		}
		/* Cooking */
		if ((firstItem == 1929 && secondItem == 1933) || (firstItem == 1933 && secondItem == 1929)) {
			if (player.getNewComersSide().isInTutorialIslandStage()) {
				player.getInventory().removeItem(new Item(1933));
				player.getInventory().removeItem(new Item(1929));
				player.getInventory().addItem(new Item(2307));
				player.getInventory().addItem(new Item(1925));
				player.getInventory().addItem(new Item(1931));
				if (player.getNewComersSide().getTutorialIslandStage() == 17)
					player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);
				return;
			}
			player.setStatedInterface("flour");
			player.getActionSender().sendString("Bread dough", 13770);
			player.getActionSender().sendString("Pastry dough", 13771);
			player.getActionSender().sendString("Pizza base", 13772);
			player.getActionSender().sendChatInterface(13768);
			return;
		}
		
		if (GodBook.addPageToBook(player, firstClickItem, secondClickItem, itemFirstClickSlot, itemSecondClickSlot)) {
			return;
		}
        player.getActionSender().sendMessage("Nothing interesting happens.");

	}

	private void useItemOnGroundItem(final Player player, Packet packet) {
		packet.getIn().readShort();
		int itemInInven = packet.getIn().readShort(StreamBuffer.ValueType.A);
		player.setClickId(packet.getIn().readShort());
		player.setClickY(packet.getIn().readShort(StreamBuffer.ValueType.A));
		player.setClickZ(player.getPosition().getZ());
		packet.getIn().readShort();
		player.setClickX(packet.getIn().readShort());
		if (itemInInven != 590) {
			return;
		}
		final int task = player.getTask();
		player.setSkilling(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task)) {
					container.stop();
					return;
				}
				if (player.getPosition().getX() == player.getClickX() && player.getPosition().getY() == player.getClickY()) {
					player.getFiremaking().attemptFire(player.getClickId(), 0, true, player.getClickX(), player.getClickY(), player.getPosition().getZ());
					container.stop();
				}
			}
			@Override
			public void stop() {
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 1);
	}

	private void handlePickupItem(Player player, Packet packet) {
		player.setClickY(packet.getIn().readShort(StreamBuffer.ByteOrder.LITTLE));
		player.setClickId(packet.getIn().readShort());
		player.setClickX(packet.getIn().readShort(StreamBuffer.ByteOrder.LITTLE));
		player.setClickZ(player.getPosition().getZ());
		if (!player.getInventory().canAddItem(new Item(player.getClickId()))) {
			return;
		}
		if (ClueScroll.hasClue(player) && new Item(player.getClickId()).getDefinition().getName().toLowerCase().contains("clue scroll")) {
			player.getActionSender().sendMessage("You can only pick up one scroll at a time.");
			return;
		}
		if ((Boolean) player.getAttributes().get("canPickup")) {
			ItemManager.getInstance().pickupItem(player, player.getClickId(), new Position(player.getClickX(), player.getClickY(), player.getPosition().getZ()));
		}
	}

	private void handlePickupSecondItem(final Player player, Packet packet) {
		player.setClickX(packet.getIn().readShort(StreamBuffer.ByteOrder.LITTLE));
		player.setClickY(packet.getIn().readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE));
		player.setClickId(packet.getIn().readShort(StreamBuffer.ValueType.A));
		player.setClickZ(player.getPosition().getZ());
        if (player.getStaffRights() > 1 && Constants.SERVER_DEBUG)
        	System.out.println(player.getClickX() + " " + player.getClickY());
		if (!player.getInventory().canAddItem(new Item(player.getClickId()))) {
			return;
		}
		final int task = player.getTask();
		player.setSkilling(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task)) {
					container.stop();
					return;
				}
				if (player.getPosition().getX() == player.getClickX() && player.getPosition().getY() == player.getClickY()) {
					player.getFiremaking().attemptFire(player.getClickId(), 0, true, player.getClickX(), player.getClickY(), player.getPosition().getZ());
					container.stop();
				}
			}
			@Override
			public void stop() {
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 1);

	}

	private void handleOptions(Player player, Packet packet) {
		player.setInterfaceId(packet.getIn().readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE));
		packet.getIn().readByte(StreamBuffer.ValueType.C);
		int fromSlot = packet.getIn().readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		int toSlot = packet.getIn().readShort(StreamBuffer.ByteOrder.LITTLE);
        RSInterface inter = RSInterface.forId(player.getInterfaceId());
        if (!player.hasInterfaceOpen(inter)) {
            //player.getActionSender().removeInterfaces();
            return;
        }
		switch (player.getInterfaceId()) {
			case 5382 :
				BankManager.handleBankOptions(player, fromSlot, toSlot);
				break;
			case 3214 :
                Item item = player.getInventory().getItemContainer().get(fromSlot);
                if (item == null)
                    return;
                if(player.getInventory().playerHasItem(item)){
					player.getInventory().swap(fromSlot, toSlot);
					player.getInventory().refresh();
                }
				break;
		}
	}

	private void handleClick1(Player player, Packet packet) {
		int interfaceID = packet.getIn().readShort(StreamBuffer.ValueType.A);
		player.setSlot(packet.getIn().readShort(StreamBuffer.ValueType.A));
		int itemId = packet.getIn().readShort(StreamBuffer.ValueType.A);
        RSInterface inter = RSInterface.forId(interfaceID);
        if (!player.hasInterfaceOpen(inter)) {
            //player.getActionSender().removeInterfaces();
            return;
        }
		if (interfaceID == 1119 || interfaceID == 1120 || interfaceID == 1121 || interfaceID == 1122 || interfaceID == 1123) {
			//player.getSmithing().smithItem(itemId, 1);
			SmithBars.startSmithing(player, itemId, 1);
        }
		if (interfaceID == 1688) {
			player.getEquipment().unequip(player.getSlot());
		} else if (interfaceID == 5064 || interfaceID == 7423) {
			BankManager.bankItem(player, player.getSlot(), itemId, 1);
		} else if (interfaceID == 5382) {
			BankManager.withdrawItem(player, player.getSlot(), itemId, 1);
		} else if (interfaceID == 3900) {
			ShopManager.getBuyValue(player, itemId);
		} else if (interfaceID == 3823) {
			ShopManager.getSellValue(player, itemId);
		} else if (interfaceID == 3322) {
			if (player.getStatedInterface() == "duel")
				player.getDuelMainData().stakeItem(new Item(itemId, 1), player.getSlot());
			else
				TradeManager.offerItem(player, player.getSlot(), itemId, 1);
		} else if (interfaceID == 3415) {
			TradeManager.removeTradeItem(player, player.getSlot(), itemId, 1);
		} else if (interfaceID == 15682 || interfaceID == 15683) {
			player.getFarmingTools().withdrawItems(itemId, 1);
		} else if (interfaceID == 15594 || interfaceID == 15595) {
			player.getFarmingTools().storeItems(itemId, 1);
		} else if (interfaceID == 6669) {
			player.getDuelMainData().removeStakedItem(new Item(itemId, 1));
		}
		switch (interfaceID) {
			case 4233 : // make 1 ring crafting
				GemCrafting.startCrafter(player, GemData.getGemSlot()[player.getSlot()], 1, 0);
				break;
			case 4239 : // make 1 neckalce crafting
				GemCrafting.startCrafter(player, GemData.getGemSlot()[player.getSlot()], 1, 1);
				break;
			case 4245 : // make 1 amulet crafting
				GemCrafting.startCrafter(player, GemData.getGemSlot()[player.getSlot()], 1, 2);
				break;
		}

	}

	private void handleClick5(Player player, Packet packet) {
		int interfaceID = packet.getIn().readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		int itemId = packet.getIn().readShort(true, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		player.setSlot(packet.getIn().readShort(true, StreamBuffer.ByteOrder.LITTLE));
        RSInterface inter = RSInterface.forId(interfaceID);
        if (!player.hasInterfaceOpen(inter)) {
            //player.getActionSender().removeInterfaces();
            return;
        }
		if (interfaceID == 5064 || interfaceID == 7423) {
			BankManager.bankItem(player, player.getSlot(), itemId, 5);
		} else if (interfaceID == 5382) {
			BankManager.withdrawItem(player, player.getSlot(), itemId, 5);
		} else if (interfaceID == 3900) {
			ShopManager.buyItem(player, player.getSlot(), itemId, 1);
		} else if (interfaceID == 3823) {
			ShopManager.sellItem(player, player.getSlot(), itemId, 1);
		} else if (interfaceID == 3322) {
			if (player.getStatedInterface() == "duel")
				player.getDuelMainData().stakeItem(new Item(itemId, 5), player.getSlot());
			else
				TradeManager.offerItem(player, player.getSlot(), itemId, 5);
		} else if (interfaceID == 3415) {
			TradeManager.removeTradeItem(player, player.getSlot(), itemId, 5);
		} else if (interfaceID == 15682 || interfaceID == 15683) {
			player.getFarmingTools().withdrawItems(itemId, 5);
		} else if (interfaceID == 15594 || interfaceID == 15595) {
			player.getFarmingTools().storeItems(itemId, 5);
		} else if (interfaceID == 1119 || interfaceID == 1120 || interfaceID == 1121 || interfaceID == 1122 || interfaceID == 1123) {
			//player.getSmithing().smithItem(itemId, 5);
			SmithBars.startSmithing(player, itemId, 5);
		} else if (interfaceID == 6669) {
			player.getDuelMainData().removeStakedItem(new Item(itemId, 5));
		}
		switch (interfaceID) {
			case 4233 : // make 1 ring crafting
				GemCrafting.startCrafter(player, GemData.getGemSlot()[player.getSlot()], 5, 0);
				break;
			case 4239 : // make 1 neckalce crafting
				GemCrafting.startCrafter(player, GemData.getGemSlot()[player.getSlot()], 5, 1);
				break;
			case 4245 : // make 1 amulet crafting
				GemCrafting.startCrafter(player, GemData.getGemSlot()[player.getSlot()], 5, 2);
				break;
		}
	}

	private void handleClick10(Player player, Packet packet) {
		int interfaceID = packet.getIn().readShort(StreamBuffer.ByteOrder.LITTLE);
		int itemId = packet.getIn().readShort(StreamBuffer.ValueType.A);
		player.setSlot(packet.getIn().readShort(StreamBuffer.ValueType.A));
        RSInterface inter = RSInterface.forId(interfaceID);
        if (!player.hasInterfaceOpen(inter)) {
            //player.getActionSender().removeInterfaces();
            return;
        }
		if (interfaceID == 5064 || interfaceID == 7423) {
			BankManager.bankItem(player, player.getSlot(), itemId, 10);
		} else if (interfaceID == 5382) {
			BankManager.withdrawItem(player, player.getSlot(), itemId, 10);
		} else if (interfaceID == 3900) {
			ShopManager.buyItem(player, player.getSlot(), itemId, 5);
		} else if (interfaceID == 3823) {
			ShopManager.sellItem(player, player.getSlot(), itemId, 5);
		} else if (interfaceID == 3322) {
			if (player.getStatedInterface() == "duel")
				player.getDuelMainData().stakeItem(new Item(itemId, 10), player.getSlot());
			else
				TradeManager.offerItem(player, player.getSlot(), itemId, 10);
		} else if (interfaceID == 3415) {
			TradeManager.removeTradeItem(player, player.getSlot(), itemId, 10);
		} else if (interfaceID == 15682 || interfaceID == 15683) {
			player.getFarmingTools().withdrawItems(itemId, 255);
		} else if (interfaceID == 15594 || interfaceID == 15595) {
			player.getFarmingTools().storeItems(itemId, player.getInventory().getItemAmount(itemId));
		} else if (interfaceID == 1119 || interfaceID == 1120 || interfaceID == 1121 || interfaceID == 1122 || interfaceID == 1123) {
			//player.getSmithing().smithItem(itemId, 10);
			SmithBars.startSmithing(player, itemId, 10);
		} else if (interfaceID == 6669) {
			player.getDuelMainData().removeStakedItem(new Item(itemId, 10));
		}
		switch (interfaceID) {
			case 4233 : // make 1 ring crafting
				GemCrafting.startCrafter(player, GemData.getGemSlot()[player.getSlot()], 10, 0);
				break;
			case 4239 : // make 1 neckalce crafting
				GemCrafting.startCrafter(player, GemData.getGemSlot()[player.getSlot()], 10, 1);
				break;
			case 4245 : // make 1 amulet crafting
				GemCrafting.startCrafter(player, GemData.getGemSlot()[player.getSlot()], 10, 2);
				break;
		}
	}

	private void handleClickAll(Player player, Packet packet) {
		player.setSlot(packet.getIn().readShort(StreamBuffer.ValueType.A));
		int interfaceID = packet.getIn().readShort();
		int itemId = packet.getIn().readShort(StreamBuffer.ValueType.A);
        RSInterface inter = RSInterface.forId(interfaceID);
        if (!player.hasInterfaceOpen(inter)) {
            //player.getActionSender().removeInterfaces();
            return;
        }
		if (interfaceID == 5064 || interfaceID == 7423) {
			BankManager.bankItem(player, player.getSlot(), itemId, player.getInventory().getItemContainer().getCount(itemId));
		} else if (interfaceID == 5382) {
            BankManager.withdrawItem(player, player.getSlot(), itemId, player.getBank().getCount(itemId));
		} else if (interfaceID == 3900) {
			ShopManager.buyItem(player, player.getSlot(), itemId, 10);
		} else if (interfaceID == 3823) {
			ShopManager.sellItem(player, player.getSlot(), itemId, 10);
		} else if (interfaceID == 3322) {
			if (player.getStatedInterface() == "duel")
				player.getDuelMainData().stakeItem(new Item(itemId, player.getInventory().getItemContainer().getCount(itemId)), player.getSlot());
			else
				TradeManager.offerItem(player, player.getSlot(), itemId, player.getInventory().getItemContainer().getCount(itemId));
		} else if (interfaceID == 15594 || interfaceID == 15595) {
			player.getActionSender().openXInterface(interfaceID);
			player.setClickItem(itemId);
		} else if (interfaceID == 15682 || interfaceID == 15683) {
			player.getActionSender().openXInterface(interfaceID);
			player.setClickItem(itemId);
		} else if (interfaceID == 3415) {
			TradeManager.removeTradeItem(player, player.getSlot(), itemId, Integer.MAX_VALUE);
		} else if (interfaceID == 6669) {
			player.getDuelMainData().removeStakedItem(new Item(itemId, Integer.MAX_VALUE));
		}
	}

	private void handleFirstClickItem(final Player player, Packet packet) {
		int interfaceID = packet.getIn().readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		player.setSlot(packet.getIn().readShort(StreamBuffer.ValueType.A));
		int itemId = packet.getIn().readShort(StreamBuffer.ByteOrder.LITTLE);
        RSInterface inter = RSInterface.forId(interfaceID);
        if (!player.hasInterfaceOpen(inter)) {
            //player.getActionSender().removeInterfaces();
            return;
        }
		Item item = player.getInventory().getItemContainer().get(player.getSlot());

		if (item == null || item.getId() != itemId)
			return;
		/*
		 * if (player.getRunecrafting().fillEssencePouch(item)) { return; } if
		 * (player.getHerblore().cleanHerb(item)) { return; }
		 */
        if(Nests.handleNest(player, itemId)){
            return;
        }
        if (itemId == Slayer.ENCHANTED_GEM) {
        	Dialogues.startDialogue(player, 10012);
        	return;
        }
		if (Cleaning.handleCleaning(player, itemId, player.getSlot()))
			return;

		if (player.getBoneBurying().buryBone(itemId, player.getSlot())) {
			return;
		}
		if (itemId >= 5509 && itemId <= 5514) {
			Pouches.fillEssencePouch(player, itemId);
			return;
		}

		if (player.getPotion().isPotion(itemId)) {
			player.getPotion().drinkPotion(itemId, player.getSlot());
			return;
		}
		if (player.getFood().eatFood(itemId, player.getSlot())){
            return;
        }
		ClueScroll.handleCasket(player, itemId);
		if (new Item(itemId).getDefinition().getName().toLowerCase().contains("clue scroll") || new Item(itemId).getDefinition().getName().toLowerCase().contains("challenge scroll")) {
			ClueScroll.cleanClueInterface(player);
		}
		if (Puzzle.loadClueInterface(player, itemId)) {
			player.getActionSender().sendMessage("clue id: "+itemId);
			return;
		}
		if (CoordinateScrolls.loadClueInterface(player, itemId)) {
			player.getActionSender().sendMessage("clue id: "+itemId);
			return;
		}
		if (DiggingScrolls.loadClueInterface(player, itemId)) {
			player.getActionSender().sendMessage("clue id: "+itemId);
			return;
		}
		if (ChallengeScrolls.loadClueInterface(player, itemId)) {
			player.getActionSender().sendMessage("clue id: "+itemId);
			return;
		}
		if (SpeakToScrolls.loadClueInterface(player, itemId)) {
			player.getActionSender().sendMessage("clue id: "+itemId);
			return;
		}
		if (AnagramsScrolls.loadClueInterface(player, itemId)) {
			player.getActionSender().sendMessage("clue id: "+itemId);
			return;
		}
		if (MapScrolls.loadClueInterface(player, itemId)) {
			player.getActionSender().sendMessage("clue id: "+itemId);
			return;
		}
		if (SearchScrolls.loadClueInterface(player, itemId)) {
			player.getActionSender().sendMessage("clue id: "+itemId);
			return;
		}
		switch (itemId) {
			case 2528 : // genie lamp
				player.setGenieSelect(-1);
	            player.getActionSender().sendConfig(261, 0);
	            player.getActionSender().sendInterface(2808);
				return;
			case 550 : // newcomers map
				player.getActionSender().sendInterface(5392);
				return;
			case 33 : // candle
				if (player.getInventory().removeItemSlot(item, player.getSlot())) {
					player.getActionSender().sendMessage("You extinguish the candle.");
					player.getInventory().addItemToSlot(new Item(36), player.getSlot());
				}
				return;
			case 594 : // torch
				if (player.getInventory().removeItemSlot(item, player.getSlot())) {
					player.getActionSender().sendMessage("You extinguish the torch.");
					player.getInventory().addItemToSlot(new Item(596), player.getSlot());
				}
				return;
			case 4531 : // candle lantern
				if (player.getInventory().removeItemSlot(item, player.getSlot())) {
					player.getActionSender().sendMessage("You extinguish the candle lantern.");
					player.getInventory().addItemToSlot(new Item(4529), player.getSlot());
				}
				return;
			case 4534 : // black candle lantern
				if (player.getInventory().removeItemSlot(item, player.getSlot())) {
					player.getActionSender().sendMessage("You extinguish the black candle lantern.");
					player.getInventory().addItemToSlot(new Item(4532), player.getSlot());
				}
				return;
			case 4539 : // oil lamp
				if (player.getInventory().removeItemSlot(item, player.getSlot())) {
					player.getActionSender().sendMessage("You extinguish the oil lamp.");
					player.getInventory().addItemToSlot(new Item(4537), player.getSlot());
				}
				return;
			case 4550 : // bullseye lantern
				if (player.getInventory().removeItemSlot(item, player.getSlot())) {
					player.getActionSender().sendMessage("You extinguish the bullseye lantern.");
					player.getInventory().addItemToSlot(new Item(4548), player.getSlot());
				}
				return;
			case 405 : // casket
				if (player.getInventory().removeItemSlot(item, player.getSlot())) {
					Casket.openCasket(player);
				}
				return;
			case 2150 : // swamp toad
				if (player.getInventory().removeItemSlot(item, player.getSlot())) {
					player.getActionSender().sendMessage("You pull the legs off the toad. Poor toad. At least they'll grow back.");
					player.getInventory().addItemToSlot(new Item(2152), player.getSlot());
				}
				return;
			case 407 : //oyster
				if (player.getInventory().removeItemSlot(item, player.getSlot())) {
					player.getActionSender().sendMessage("You open the oyster.");
					player.getInventory().addItemToSlot(new Item(411), player.getSlot());
				}
				return;
			case 4033:
			  //  ShopManager.openShop(player, 39);
			  player.getActionSender().sendMessage("You poke the monkey");
			    return;
			case 952 : //spade
				player.getUpdateFlags().sendAnimation(830);
				player.getActionSender().sendMessage("You dig into the ground...");

				final int task = player.getTask();
				player.setSkilling(new CycleEvent() {

					@Override
					public void execute(CycleEventContainer container) {
						if (!player.checkTask(task)) {
							container.stop();
							return;
						}
						if (player.getPosition().getX() == 2566 && (player.getPosition().getY() == 3331 || player.getPosition().getY() == 3333)) {
							player.teleport(new Position(2530, 3303));
							player.getActionSender().sendMessage("and find yourself in plague city.");
							container.stop();
							return;
						}
						if (!MapScrolls.digClue(player) && !DiggingScrolls.digClue(player) && !CoordinateScrolls.digClue(player) && !Barrows.digCrypt(player)) {
							player.getActionSender().sendMessage("but do not find anything.");
							container.stop();
						}

					}

					@Override
					public void stop() {
						player.resetAnimation();
					}
				});
				CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 2);
				return;
			case 2574 : // sextant
				Sextant.initializeRandomSextantInterface(player);
				return;
			case 299 :
				//MithrilSeeds.plantMithrilSeed(player);
				return;
		}

        player.getActionSender().sendMessage("Nothing interesting happens.");
		/*
		 * if (item == 4155) { String slayerNpc = (String)
		 * player.getSlayerTask()[0]; if (!slayerNpc.equalsIgnoreCase("")) {
		 * player.getDialogue().sendStatement("Your existing task is to kill " +
		 * player.getSlayerTask()[1] + " " + slayerNpc + "s.");
		 * player.getDialogue().setNextDialogue(0); } else {
		 * player.getDialogue().sendStatement("You don't have a slaver task.");
		 * } return; }
		 */
	}

	private void handleSecondClickItem(Player player, Packet packet) {
		int itemId = packet.getIn().readShort(StreamBuffer.ValueType.A);
		player.setSlot(packet.getIn().readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE));
		int interfaceID = packet.getIn().readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
        RSInterface inter = RSInterface.forId(interfaceID);
        if (!player.hasInterfaceOpen(inter)) {
            //player.getActionSender().removeInterfaces();
            return;
        }
		Item item = player.getInventory().getItemContainer().get(player.getSlot());
		if (item == null || item.getId() != itemId)
			return;
		Pouches.checkEssencePouch(player, itemId);
		switch(itemId) {
			case 4566 : // rubber chicken
				player.getUpdateFlags().sendAnimation(1835);
				return;
		}
	}

	private void handleThirdClickItem(Player player, Packet packet) {
		int interfaceID = packet.getIn().readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		player.setSlot(packet.getIn().readShort(StreamBuffer.ByteOrder.LITTLE));
		player.setClickItem(packet.getIn().readShort(true, StreamBuffer.ValueType.A));
        RSInterface inter = RSInterface.forId(interfaceID);
        if (!player.hasInterfaceOpen(inter)) {
            //player.getActionSender().removeInterfaces();
            return;
        }
		Item item = player.getInventory().getItemContainer().get(player.getSlot());
		if (item == null || item.getId() != player.getClickItem())
			return;
		if (PotionMaking.emptyPotion(player, new Item(player.getClickItem()), player.getSlot()))
			return;
		if (Runecrafting.clickTalisman(player, player.getClickItem())) {
			return;
		}
        switch(item.getId()){
	        case 2552 : // ring of duelling
	        case 2554 :
	        case 2556 :
	        case 2558 :
	        case 2560 :
	        case 2562 :
	        case 2564 :
	        case 2566 :
        		Dialogues.startDialogue(player, 10004);
	        	break;
            case 1712 : // glory
            case 1710 :
            case 1708 :
            case 1706 :
        		Dialogues.startDialogue(player, 10003);
            	break;
            case 3853 :
        	case 3855 :
        	case 3857 :
        	case 3859 :
        	case 3861 :
        	case 3863 :
        	case 3865 :
        	case 3867 :
        		Dialogues.startDialogue(player, 10002);
            	break;
        }
	}

	private void handleEquipItem(Player player, Packet packet) {
		int itemId = packet.getIn().readShort(); // Item ID.
		player.setSlot(packet.getIn().readShort(StreamBuffer.ValueType.A));
		player.setInterfaceId(packet.getIn().readShort(StreamBuffer.ValueType.A)); // Interface ID.
        RSInterface inter = RSInterface.forId(player.getInterfaceId());
        if (!player.hasInterfaceOpen(inter)) {
            return;
        }
		if (new Item(itemId).getDefinition().getSlot() == -1)
		{
			return;
		}
		if (player.getDuelMainData().getOpponent() != null && !player.inDuelArena()){
			player.getDuelInteraction().endDuelInteraction(true);
            return;
        }
        //player.getActionSender().removeInterfaces();
		Item item = player.getInventory().getItemContainer().get(player.getSlot());
		if (item == null || item.getId() != itemId || !item.validItem())
		{
			return;
		}
		for (int[] element : Pouches.POUCHES) {
			if (itemId == element[0]) {
				Pouches.emptyEssencePouch(player, itemId);
				return;
			}
		}
		player.getEquipment().equip(player.getSlot());
	}

	private void handleCastedSpellOnItem(Player player, Packet packet) {
		StreamBuffer.InBuffer in = packet.getIn();
		player.setSlot(in.readShort());
		int itemId = in.readShort(StreamBuffer.ValueType.A);
		player.setInterfaceId(in.readShort());
		int magicId = in.readShort(StreamBuffer.ValueType.A);
		Spell spell = player.getMagicBookType().getSpells().get(magicId);
		Item item = player.getInventory().getItemContainer().get(player.getSlot());
		if (item == null || item.getId() != itemId || !item.validItem())
			return;
		if (spell != null) {
			MagicSkill.spellOnItem(player, spell, itemId, player.getSlot());
		}
		if (player.getEnchantingChamber().isInEnchantingChamber()) {
			player.getEnchantingChamber().enchantItem(magicId, itemId);
			return;
		}
		if (player.getAlchemistPlayground().isInAlchemistPlayGround()) {
			player.getAlchemistPlayground().alchemyItem(itemId);
			return;
		} else if (player.getStaffRights() > 1 && Constants.SERVER_DEBUG)
			System.out.println("Slot: " + player.getSlot() + " Item id: " + itemId + " Interface ID: " + player.getInterfaceId() + " magic id: " + magicId);
	}

	private void handleCastedSpellOnGroundItem(Player player, Packet packet) {
		StreamBuffer.InBuffer in = packet.getIn();
		int y = in.readShort(StreamBuffer.ByteOrder.LITTLE);
		int itemId = in.readShort();
		int x = in.readShort(StreamBuffer.ByteOrder.LITTLE);
		int magicId = in.readShort(StreamBuffer.ValueType.A);
		Spell spell = player.getMagicBookType().getSpells().get(magicId);
		if (spell != null) {
			MagicSkill.spellOnGroundItem(player, spell, itemId, new Position(x, y, player.getPosition().getZ()));
		} else if (player.getStaffRights() > 1 && Constants.SERVER_DEBUG)
			System.out.println("Magic ID: " + magicId + " Item ID: " + itemId + " X: " + x + " Y: " + y);
	}

}
