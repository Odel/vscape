package com.rs2.net.packet.packets;

import com.rs2.Constants;
import com.rs2.cache.interfaces.RSInterface;
import com.rs2.model.Position;
import com.rs2.model.content.Pets;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.combat.util.Degradeables;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.minigames.warriorsguild.WarriorsGuild;
import com.rs2.model.content.minigames.barrows.Barrows;
import com.rs2.model.content.minigames.castlewars.CastlewarsExchange;
import com.rs2.model.content.minigames.castlewars.impl.CastlewarsBarricades;
import com.rs2.model.content.minigames.magetrainingarena.MageRewardHandling;
import com.rs2.model.content.minigames.magetrainingarena.TelekineticTheatre;
import com.rs2.model.content.quests.DemonSlayer;
import com.rs2.model.content.quests.DwarfCannon;
import com.rs2.model.content.quests.GhostsAhoy;
import com.rs2.model.content.quests.HeroesQuest;
import com.rs2.model.content.quests.MerlinsCrystal;
import com.rs2.model.content.quests.MonkeyMadness.ApeAtoll;
import com.rs2.model.content.quests.MonkeyMadness.MonkeyMadness;
import com.rs2.model.content.quests.NatureSpirit;
import com.rs2.model.content.quests.PiratesTreasure;
import com.rs2.model.content.quests.Quest;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.quests.RecruitmentDrive;
import com.rs2.model.content.quests.TheGrandTree;
import com.rs2.model.content.skills.Menus;
import com.rs2.model.content.skills.Tools;
import com.rs2.model.content.skills.Crafting.BasicCraft;
import com.rs2.model.content.skills.Crafting.GemCrafting;
import com.rs2.model.content.skills.Crafting.GemCutting;
import com.rs2.model.content.skills.Crafting.GemData;
import com.rs2.model.content.skills.Crafting.MacheteCrafting;
import com.rs2.model.content.skills.Crafting.GlassMaking;
import com.rs2.model.content.skills.Crafting.LeatherMakingHandler;
import com.rs2.model.content.skills.Fletching.ArrowMaking;
import com.rs2.model.content.skills.Fletching.BowStringing;
import com.rs2.model.content.skills.Fletching.GemTips;
import com.rs2.model.content.skills.Fletching.LogCuttingInterfaces;
import com.rs2.model.content.skills.Fletching.CbowFletching;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillCapeHandler;
import com.rs2.model.content.skills.cooking.OneIngredients;
import com.rs2.model.content.skills.cooking.SliceDiceHandler;
import com.rs2.model.content.skills.cooking.ThreeIngredients;
import com.rs2.model.content.skills.cooking.TwoIngredients;
import com.rs2.model.content.skills.cooking.DoughHandler;
import com.rs2.model.content.skills.cooking.wetClayHandler;
import com.rs2.model.content.skills.herblore.Cleaning;
import com.rs2.model.content.skills.herblore.Coconut;
import com.rs2.model.content.skills.herblore.Grinding;
import com.rs2.model.content.skills.herblore.PoisoningWeapon;
import com.rs2.model.content.skills.herblore.PotionMaking;
import com.rs2.model.content.skills.magic.MagicSkill;
import com.rs2.model.content.skills.magic.Spell;
import com.rs2.model.content.skills.magic.TeleTabs;
import com.rs2.model.content.skills.prayer.GodBook;
import com.rs2.model.content.skills.runecrafting.Pouches;
import com.rs2.model.content.skills.runecrafting.Runecrafting;
import com.rs2.model.content.skills.runecrafting.TabHandler;
import com.rs2.model.content.skills.slayer.Slayer;
import com.rs2.model.content.skills.smithing.SmithBars;
import com.rs2.model.content.treasuretrails.AnagramsScrolls;
import com.rs2.model.content.treasuretrails.ChallengeScrolls;
import com.rs2.model.content.treasuretrails.ClueScroll;
import com.rs2.model.content.treasuretrails.CoordinateScrolls;
import com.rs2.model.content.treasuretrails.DiggingScrolls;
import com.rs2.model.content.treasuretrails.MapScrolls;
import com.rs2.model.content.treasuretrails.SearchScrolls;
import com.rs2.model.content.treasuretrails.Sextant;
import com.rs2.model.content.treasuretrails.SpeakToScrolls;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.players.Player;
import com.rs2.model.players.ShopManager;
import com.rs2.model.players.TradeManager;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;
import com.rs2.model.players.item.ItemManager;
import com.rs2.model.players.item.functions.Casket;
import com.rs2.model.players.item.functions.Nests;
import com.rs2.model.players.item.functions.Toys;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.net.StreamBuffer;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;
import com.rs2.util.Misc;

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

    public static final int EXAMINE_ITEM = 220;

    @Override
    public void handlePacket(Player player, Packet packet) {
	if (player.stopPlayerPacket()) {
	    return;
	}
	switch (packet.getOpcode()) {
	    case HANDLE_OPTIONS:
		handleOptions(player, packet);
		return;
	    case CLICK_5:
	    	player.resetAllActions(false);
		handleClick5(player, packet);
		return;
	}
	    player.resetAllActions();
	switch (packet.getOpcode()) {
	    case USE_ITEM_ON_GROUND_ITEM:
		useItemOnGroundItem(player, packet);
		break;
	    case USE_ITEM_ON_ITEM:
		useItemOnItem(player, packet);
		break;
	    case DROP_ITEM:
		handleDropItem(player, packet);
		break;
	    case PICKUP_ITEM:
		handlePickupItem(player, packet);
		break;
	    case SECOND_GROUND_OPTION_ITEM:
		handlePickupSecondItem(player, packet);
		break;
	    case CLICK_1:
		handleClick1(player, packet);
		break;
	    case CLICK_10:
		handleClick10(player, packet);
		break;
	    case CLICK_ALL:
		handleClickAll(player, packet);
		break;
	    case EQUIP_ITEM:
		handleEquipItem(player, packet);
		break;
	    case FIRST_CLICK_ITEM:
		handleFirstClickItem(player, packet);
		break;
	    case SECOND_CLICK_ITEM:
		handleSecondClickItem(player, packet);
		break;
	    case THIRD_CLICK_ITEM:
		handleThirdClickItem(player, packet);
		break;
	    case CASTED_SPELL_ON_ITEM:
		handleCastedSpellOnItem(player, packet);
		break;
	    case CASTED_SPELL_ON_GROUND_ITEM:
		handleCastedSpellOnGroundItem(player, packet);
		break;
	    /*case EXAMINE_ITEM :
	     handleExamineItem(player, packet);
	     break;*/
	}
    }

    @SuppressWarnings("unused")
	private void handleExamineItem(Player player, Packet packet) {
	int itemId = packet.getIn().readShort(); // Item ID.
	ItemDefinition itemDef = new Item(itemId).getDefinition();
	if (itemDef != null) {
	    if (itemDef.getDescription() == null || itemDef.getDescription() == "null") {
		player.getActionSender().sendMessage("It's a " + itemDef.getName() + ".");
	    } else {
		player.getActionSender().sendMessage(itemDef.getDescription());
	    }
	} else {
	    player.getActionSender().sendMessage("It's an Item.");
	}
    }

    private void handleDropItem(Player player, Packet packet) {
	int itemId = packet.getIn().readShort(StreamBuffer.ValueType.A);
	packet.getIn().readShort();
	player.setSlot(packet.getIn().readShort(StreamBuffer.ValueType.A));
	Item item = player.getInventory().getItemContainer().get(player.getSlot());
	if (player.getPuzzle().moveSlidingPiece(itemId, true)) {
	    return;
	}
	if(player.getQuestStage(36) == 4 && player.getMMVars().getPuzzle().moveSlidingPiece(itemId, true)) {
	    return;
	}
	if (item == null || item.getId() != itemId || !item.validItem()) {
	    return;
	}
	if (item.getDefinition().isStackable()) {
	    item.setCount(player.getInventory().getItemContainer().getCount(item.getId()));
	} else {
	    item.setCount(1);
	}
	/*if (player.getInterface() == 3900) {
	    ShopManager.buyItem(player, player.getSlot(), itemId, 1);
	} else if (player.getInterface() == 3823) {
	    ShopManager.sellItem(player, player.getSlot(), itemId, 1);
	}*/
	if (!player.getInventory().getItemContainer().contains(item.getId())) {
	    return;
	}
	if (player.inTempleKnightsTraining()) {
	    if(item.getId() == RecruitmentDrive.FOX || item.getId() == RecruitmentDrive.GRAIN || item.getId() == RecruitmentDrive.CHICKEN) {
		player.getActionSender().sendSound(376, 1, 0);
		player.getInventory().removeItem(item);
		player.getEquipment().updateWeight();
		RecruitmentDrive.handleDropItem(player, item);
		return;
	    }
	}
	if (item.getId() == 530 && player.getPosition().getX() == 2780 && player.getPosition().getY() == 3515) {
	    if (player.getInventory().playerHasItem(32)) {
		player.getInventory().removeItem(new Item(530));
		MerlinsCrystal.summon(player);
		return;
	    } else {
		player.getDialogue().sendStatement("Nothing happens. Perhaps you should light the candle first.");
		return;
	    }
	}
	if (WarriorsGuild.itemHandling(player, itemId)) {
	    return;
	}
	if (item.getId() == 4045) {
		player.getActionSender().sendSoundRadius(97, 0, 0, player.getPosition(), 5);
		player.getUpdateFlags().sendGraphic(287);
		player.hit(15, HitType.NORMAL);
	    if (!player.getInventory().removeItemSlot(item, player.getSlot())) {
	    	player.getInventory().removeItem(item);
	    }
	    player.getUpdateFlags().sendForceMessage("Ow!");
	    player.getActionSender().sendMessage("You were injured by the exploding potion.");
	    return;
	}
	if (item.getId() == 10592) {
	    player.getActionSender().sendMessage("Play the bongos instead.");
	    return;
	}
	if (item.getId() == 6541) {
	    player.getActionSender().sendMessage("You don't want to destroy your pet!");
	    return;
	}
	if (item.getId() == 4033 && player.getQuestStage(36) >= 14) {
	    player.getDialogue().setLastNpcTalk(1463);
	    if(player.getEquipment().getId(Constants.AMULET) == MonkeyMadness.MONKEYSPEAK_AMULET) {
		player.getDialogue().sendNpcChat("Don't drop me! You said you'd rescue me!", Dialogues.SAD);
	    } else {
		player.getDialogue().sendNpcChat("OOK! OOK!", Dialogues.DISTRESSED);
	    }
	    player.getDialogue().endDialogue();
	    return;
	}
	if(player.getCat().registerCat(item.getId()))
	{
		return;
	}
	for (int[] element : Pets.PET_IDS) {
	    if (item.getDefinition().getId() == element[0]) {
		player.getPets().registerPet(element[0], element[1]);
		return;
	    }
	}
	
	if (item.getDefinition().isUntradable() || item.getId() == 763 || item.getId() == 765 || item.getId() == 769 || item.getId() == 288 || item.getId() == 10498 || item.getId() == 10499) {
	    if (Degradeables.notDroppable(Degradeables.getDegradeableItem(item), item)) {
		String[][] info = {{"Are you sure you want to drop this item?", "14174"}, {"Yes.", "14175"}, {"No.", "14176"}, {"", "14177"}, {"Dropping this item will make it break completely.", "14182"}, {"", "14183"}, {item.getDefinition().getName(), "14184"}};
		for (String[] element : info) {
		    player.getActionSender().sendString(element[0], Integer.parseInt(element[1]));
		}
	    } else {
		String[][] info = {{"Are you sure you want to drop this item?", "14174"}, {"Yes.", "14175"}, {"No.", "14176"}, {"", "14177"}, {"Dropping this item will make you lose it forever.", "14182"}, {"", "14183"}, {item.getDefinition().getName(), "14184"}};
		for (String[] element : info) {
		    player.getActionSender().sendString(element[0], Integer.parseInt(element[1]));
		}
	    }
	    player.getActionSender().sendUpdateItem(item, 0, 14171, 1);
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
	    player.getActionSender().removeInterfaces();
	    player.getDialogue().resetDialogue();
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
	if (firstClickItem == null || secondClickItem == null || !firstClickItem.validItem() || !secondClickItem.validItem()) {
	    return;
	}
	int firstItem = firstClickItem.getId();
	int secondItem = secondClickItem.getId();
	if (player.getDuelMainData().getOpponent() != null) {
	    player.getDuelInteraction().endDuelInteraction(true);
	    return;
	}
	if (OneIngredients.mixItems(player, firstItem, secondItem, itemFirstClickSlot, itemSecondClickSlot)) {
	    return;
	}
	if (TwoIngredients.mixItems(player, firstItem, secondItem, itemFirstClickSlot, itemSecondClickSlot)) {
	    return;
	}
	if (ThreeIngredients.mixItems(player, firstItem, secondItem, itemFirstClickSlot, itemSecondClickSlot)) {
	    return;
	}
	if (DoughHandler.handleInterface(player, firstItem, secondItem, itemFirstClickSlot, itemSecondClickSlot)) {
	    return;
	}
	if (SliceDiceHandler.handleInterface(player, firstItem, secondItem, itemFirstClickSlot)) {
	    return;
	}
	/* Fletching */
	if (ArrowMaking.perform(player, firstItem, secondItem)) {
	    return;
	}
	if (BowStringing.perform(player, firstItem, secondItem)) {
	    return;
	}
	if (LogCuttingInterfaces.handleItemOnItem(player, firstItem, secondItem)) {
	    return;
	}
	if (CbowFletching.attachLimbs(player, firstItem, secondItem) || CbowFletching.stringCbow(player, firstItem, secondItem)) {
	    return;
	}
	if (GemTips.handleCutting(player, firstItem, secondItem, firstItem != GemTips.CHISEL ? itemFirstClickSlot : itemSecondClickSlot)) {
	    return;
	}
	/* Crafting */
	if (GemCutting.handleCutting(player, firstItem, secondItem, firstItem != GemCutting.CHISEL ? itemFirstClickSlot : itemSecondClickSlot)) {
	    return;
	}
	if (MacheteCrafting.handleMacheteCraft(player, firstItem, secondItem, firstItem != MacheteCrafting.MACHETE ? itemFirstClickSlot : itemSecondClickSlot)) {
	    return;
	}
	if (LeatherMakingHandler.handleItemOnItem(player, firstItem, secondItem, itemFirstClickSlot, itemSecondClickSlot)) {
	    return;
	}
	if (BasicCraft.handleItemOnItem(player, firstItem, secondItem)) {
	    return;
	}
	if (GodBook.blessSymbol(player, firstItem, secondItem)) {
	    return;
	}
	if ((firstItem == GlassMaking.GLASSBLOWING_PIPE && secondItem == GlassMaking.MOLTEN_GLASS) || (secondItem == GlassMaking.GLASSBLOWING_PIPE && firstItem == GlassMaking.MOLTEN_GLASS)) {
	    Menus.sendSkillMenu(player, "glassMaking");
	    return;
	}
	if(firstItem == 1963 && secondItem == MonkeyMadness.MONKEY_ITEM) {
	    if(Misc.random(10) == 1 && !player.hasClueScroll()) {
		player.getActionSender().sendMessage("The monkey chews on the banana and spits out a clue!");
		player.getInventory().replaceItemWithItem(new Item(1963), new Item(ClueScroll.getRandomClue(2)));
		player.getMMVars().setRecievedClueFromMonkey(true);
		return;
	    } else {
		player.getActionSender().sendMessage("The monkey chews on the banana, it makes some happy chatter.");
		player.getInventory().removeItem(new Item(1963));
		return;
	    }
	}
	/* STRINGING AMULETS */
	for (int i = 0; i < GemData.stringItems.length; i++) {
	    if (GemData.stringItems[i][0] == firstItem || GemData.stringItems[i][0] == secondItem) {
		GemCrafting.string(player, i);
		return;
	    }
	}
	for (Quest q : QuestHandler.getQuests()) {
	    if (q.itemOnItemHandling(player, firstItem, secondItem, itemFirstClickSlot, itemSecondClickSlot)) {
	    	return;
	    }
	}
	if (TabHandler.itemOnItemHandling(player, firstItem, secondItem)) {
	    return;
	}
	if (wetClayHandler.itemOnItemHandling(player, firstItem, secondItem)) {
	    return;
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
	if (player.getSlayer().handleItemOnItem(firstItem, secondItem)) {
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

	if (GodBook.addPageToBook(player, firstClickItem, secondClickItem, itemFirstClickSlot, itemSecondClickSlot)) {
	    return;
	}
	if (firstClickItem.getId() == 1765 && secondClickItem.getId() == 2421) {
	    player.getActionSender().sendMessage("You carefully dye the wig blonde.");
	    player.getInventory().removeItem(new Item(2421));
	    player.getInventory().removeItem(new Item(1765));
	    player.getInventory().addItem(new Item(2419));
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
	switch(itemInInven) {
	    default:
		    return;
	    case 590: //Tinderbox
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
		    break;
		
	    case 1929: //Watchtower
		    break;
	}
	
    }

    private void handlePickupItem(Player player, Packet packet) {
	player.setClickY(packet.getIn().readShort(StreamBuffer.ByteOrder.LITTLE));
	player.setClickId(packet.getIn().readShort());
	player.setClickX(packet.getIn().readShort(StreamBuffer.ByteOrder.LITTLE));
	player.setClickZ(player.getPosition().getZ());
	if(player.getClickId() == TelekineticTheatre.STATUE) {
	    player.getTelekineticTheatre().loadCamera();
	    return;
	}
	if (!player.getInventory().canAddItem(new Item(player.getClickId()))) {
	    return;
	}
	if (ClueScroll.hasClue(player) && new Item(player.getClickId()).getDefinition().getName().toLowerCase().contains("clue scroll")) {
	    player.getActionSender().sendMessage("You can only pick up one scroll at a time.");
	    return;
	}
	if (DemonSlayer.itemPickupHandling(player, player.getClickId())) {
	    return;
	}
	if (HeroesQuest.itemPickupHandling(player, player.getClickId())) {
	    return;
	}
	if (TheGrandTree.itemPickupHandling(player, player.getClickId())) {
	    return;
	}
	if (DwarfCannon.itemPickupHandling(player, player.getClickId())) {
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
	if(player.getClickId() == TelekineticTheatre.STATUE) {
	    player.getTelekineticTheatre().resetStatue();
	    return;
	}
	if (player.getStaffRights() > 1 && Constants.SERVER_DEBUG) {
	    System.out.println(player.getClickX() + " " + player.getClickY());
	}
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
		switch (player.getInterfaceId()) {
			case 50025:
		    	player.getBankManager().toTab(0, fromSlot);
			break;
			case 50026:
				player.getBankManager().toTab(1, fromSlot);
			break;
			case 50027:
				player.getBankManager().toTab(2, fromSlot);
				break;
			case 50028:
				player.getBankManager().toTab(3, fromSlot);
				break;
			case 50029:
				player.getBankManager().toTab(4, fromSlot);
				break;
			case 50030:
				player.getBankManager().toTab(5, fromSlot);
				break;
			case 50031:
				player.getBankManager().toTab(6, fromSlot);
				break;
			case 50032:
				player.getBankManager().toTab(7, fromSlot);
				break;
			case 50033:
				player.getBankManager().toTab(8, fromSlot);
				break;
		}
	    //player.getActionSender().removeInterfaces();
	    return;
	}
	switch (player.getInterfaceId()) {
	    case 5382:
		player.getBankManager().handleBankOptions(fromSlot, toSlot);
		break;
	    case 3214:
		Item item = player.getInventory().getItemContainer().get(fromSlot);
		if (item == null) {
		    return;
		}
		if (player.getInventory().playerHasItem(item)) {
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
	switch (interfaceID) {
	    case 4233: // make 1 ring crafting
		GemCrafting.startCrafter(player, GemData.getGemSlot()[player.getSlot()], 1, 0);
		break;
	    case 4239: // make 1 neckalce crafting
		GemCrafting.startCrafter(player, GemData.getGemSlot()[player.getSlot()], 1, 1);
		break;
	    case 4245: // make 1 amulet crafting
		GemCrafting.startCrafter(player, GemData.getGemSlot()[player.getSlot()], 1, 2);
		break;
	    case 24159: // make 1 bracelet crafting
		GemCrafting.startCrafter(player, GemData.getGemSlot()[player.getSlot()], 1, 3);
		break;
	}
	
	if (!player.hasInterfaceOpen(inter)) {
            //player.getActionSender().removeInterfaces();
		if (interfaceID == 12392) {
		    CastlewarsExchange.getBuyValue(player, itemId);
		}
	    return;
	}
	if (interfaceID == 15948) {
	    MageRewardHandling.getBuyValue(player, itemId);
	}
	if (interfaceID == 1119 || interfaceID == 1120 || interfaceID == 1121 || interfaceID == 1122 || interfaceID == 1123) {
	    //player.getSmithing().smithItem(itemId, 1);
	    SmithBars.startSmithing(player, itemId, 1);
	}
	if (interfaceID == 1688) {
	    player.getEquipment().unequip(player.getSlot());
	    player.setEquipmentOperate(false);
	} else if (interfaceID == 5064 || interfaceID == 7423) {
		player.getBankManager().bankItem(player.getSlot(), itemId, 1);
	} else if (interfaceID == 5382) {
		player.getBankManager().withdrawItem(player.getSlot(), itemId, 1);
	} else if (interfaceID == 3900) {
	    ShopManager.getBuyValue(player, itemId);
	} else if (interfaceID == 3823) {
	    ShopManager.getSellValue(player, itemId);
	} else if (interfaceID == 3322) {
	    if (player.getStatedInterface() == "duel") {
		player.getDuelMainData().stakeItem(new Item(itemId, 1), player.getSlot());
	    } else {
		TradeManager.offerItem(player, player.getSlot(), itemId, 1);
	    }
	} else if (interfaceID == 3415) {
	    TradeManager.removeTradeItem(player, player.getSlot(), itemId, 1);
	} else if (interfaceID == 15682 || interfaceID == 15683) {
	    player.getFarmingTools().withdrawItems(itemId, 1);
	} else if (interfaceID == 15594 || interfaceID == 15595) {
	    player.getFarmingTools().storeItems(itemId, 1);
	} else if (interfaceID == 6669) {
	    player.getDuelMainData().removeStakedItem(new Item(itemId, 1));
	}
    }

    private void handleClick5(Player player, Packet packet) {
	int interfaceID = packet.getIn().readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
	int itemId = packet.getIn().readShort(true, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
	player.setSlot(packet.getIn().readShort(true, StreamBuffer.ByteOrder.LITTLE));
	RSInterface inter = RSInterface.forId(interfaceID);
	if(itemId != 11284 && itemId != 11283) {
	    player.resetCombat();
	}
	switch (interfaceID) {
	    case 4233: // make 5 ring crafting
		GemCrafting.startCrafter(player, GemData.getGemSlot()[player.getSlot()], 5, 0);
		break;
	    case 4239: // make 5 necklace crafting
		GemCrafting.startCrafter(player, GemData.getGemSlot()[player.getSlot()], 5, 1);
		break;
	    case 4245: // make 5 amulet crafting
		GemCrafting.startCrafter(player, GemData.getGemSlot()[player.getSlot()], 5, 2);
		break;
	    case 24159: // make 5 bracelet crafting
		GemCrafting.startCrafter(player, GemData.getGemSlot()[player.getSlot()], 5, 3);
		break;
	}
	if (!player.hasInterfaceOpen(inter)) {
	    //player.getActionSender().removeInterfaces();
		if (interfaceID == 12392) {
			CastlewarsExchange.BuyItem(player, player.getSlot(), itemId);
		}
		
	    return;
	}
	if (interfaceID == 15948) {
	    MageRewardHandling.buyItem(player, player.getSlot(), itemId);
	}
	if (interfaceID == 5064 || interfaceID == 7423) {
		player.getBankManager().bankItem(player.getSlot(), itemId, 5);
	} else if (interfaceID == 5382) {
		player.getBankManager().withdrawItem(player.getSlot(), itemId, 5);
	} else if (interfaceID == 3900) {
		ShopManager.buyItem(player, player.getSlot(), itemId, 1);
	} else if (interfaceID == 3823) {
		ShopManager.sellItem(player, player.getSlot(), itemId, 1);
	} else if (interfaceID == 3322) {
	    if (player.getStatedInterface() == "duel") {
		player.getDuelMainData().stakeItem(new Item(itemId, 5), player.getSlot());
	    } else {
		TradeManager.offerItem(player, player.getSlot(), itemId, 5);
	    }
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
	if (interfaceID == 1688) {
	    player.setClickItem(itemId);
	    operateItem(player, itemId);
	}
    }

    private void handleClick10(Player player, Packet packet) {
	int interfaceID = packet.getIn().readShort(StreamBuffer.ByteOrder.LITTLE);
	int itemId = packet.getIn().readShort(StreamBuffer.ValueType.A);
	player.setSlot(packet.getIn().readShort(StreamBuffer.ValueType.A));
	RSInterface inter = RSInterface.forId(interfaceID);
	switch (interfaceID) {
	    case 4233: // make 10 ring crafting
		GemCrafting.startCrafter(player, GemData.getGemSlot()[player.getSlot()], 10, 0);
		break;
	    case 4239: // make 10 neckalce crafting
		GemCrafting.startCrafter(player, GemData.getGemSlot()[player.getSlot()], 10, 1);
		break;
	    case 4245: // make 10 amulet crafting
		GemCrafting.startCrafter(player, GemData.getGemSlot()[player.getSlot()], 10, 2);
		break;
	    case 24159: // make 10 bracelet crafting
		GemCrafting.startCrafter(player, GemData.getGemSlot()[player.getSlot()], 10, 3);
		break;
	}
	if (!player.hasInterfaceOpen(inter)) {
	    //player.getActionSender().removeInterfaces();
	    return;
	}
	if (interfaceID == 5064 || interfaceID == 7423) {
		player.getBankManager().bankItem(player.getSlot(), itemId, 10);
	} else if (interfaceID == 5382) {
		player.getBankManager().withdrawItem(player.getSlot(), itemId, 10);
	} else if (interfaceID == 3900) {
	    ShopManager.buyItem(player, player.getSlot(), itemId, 5);
	} else if (interfaceID == 3823) {
	    ShopManager.sellItem(player, player.getSlot(), itemId, 5);
	} else if (interfaceID == 3322) {
	    if (player.getStatedInterface() == "duel") {
		player.getDuelMainData().stakeItem(new Item(itemId, 10), player.getSlot());
	    } else {
		TradeManager.offerItem(player, player.getSlot(), itemId, 10);
	    }
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
		player.getBankManager().bankItem(player.getSlot(), itemId, player.getInventory().getItemContainer().getCount(itemId));
	} else if (interfaceID == 5382) {
		player.getBankManager().withdrawItemAll(player.getSlot(), itemId);
	} else if (interfaceID == 3900) {
	    ShopManager.buyItem(player, player.getSlot(), itemId, 10);
	} else if (interfaceID == 3823) {
	    ShopManager.sellItem(player, player.getSlot(), itemId, 10);
	} else if (interfaceID == 3322) {
	    if (player.getStatedInterface() == "duel") {
		player.getDuelMainData().stakeItem(new Item(itemId, player.getInventory().getItemContainer().getCount(itemId)), player.getSlot());
	    } else {
		TradeManager.offerItem(player, player.getSlot(), itemId, player.getInventory().getItemContainer().getCount(itemId));
	    }
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

	if (item == null || item.getId() != itemId) {
	    return;
	}
	/*
	 * if (player.getRunecrafting().fillEssencePouch(item)) { return; } if
	 * (player.getHerblore().cleanHerb(item)) { return; }
	 */
	if (itemId == 2329) {
	    player.getInventory().replaceItemWithItem(new Item(2329), new Item(2313));
	    player.getActionSender().sendMessage("You empty your burnt pie onto the ground.");
	    return;
	}
	if (Nests.handleNest(player, itemId)) {
	    return;
	}
	if (QuestHandler.getQuests()[24].itemHandling(player, itemId)) { //Ghosts Ahoy
	    return;
	}
	if (QuestHandler.getQuests()[26].itemHandling(player, itemId)) { //Horror from the deep
	    return;
	}
	if (itemId == 6885) { //Mage Training hat
	    Dialogues.startDialogue(player, 3096);
	    return;
	}
	if (itemId == Slayer.ENCHANTED_GEM) {
	    Dialogues.startDialogue(player, 10012);
	    return;
	}
	if (Cleaning.handleCleaning(player, itemId, player.getSlot())) {
	    return;
	}

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
	if (player.getFood().eatFood(itemId, player.getSlot())) {
	    return;
	}
	if (ClueScroll.handleCasket(player, itemId)) {
	    return;
	}
	if (new Item(itemId).getDefinition().getName().toLowerCase().contains("clue scroll") || new Item(itemId).getDefinition().getName().toLowerCase().contains("challenge scroll")) {
	    ClueScroll.cleanClueInterface(player);
	}
	if (player.getPuzzle().loadClueInterface(itemId)) {
	    return;
	}
	if (CoordinateScrolls.loadClueInterface(player, itemId)) {
	    player.getActionSender().sendMessage("Clue ID: " + itemId);
	    return;
	}
	if (DiggingScrolls.loadClueInterface(player, itemId)) {
	    player.getActionSender().sendMessage("Clue ID: " + itemId);
	    return;
	}
	if (ChallengeScrolls.loadClueInterface(player, itemId)) {
	    player.getActionSender().sendMessage("Clue ID: " + itemId);
	    return;
	}
	if (SpeakToScrolls.loadClueInterface(player, itemId)) {
	    player.getActionSender().sendMessage("Clue ID: " + itemId);
	    return;
	}
	if (AnagramsScrolls.loadClueInterface(player, itemId)) {
	    player.getActionSender().sendMessage("Clue ID: " + itemId);
	    return;
	}
	if (MapScrolls.loadClueInterface(player, itemId)) {
	    player.getActionSender().sendMessage("Clue ID: " + itemId);
	    return;
	}
	if (SearchScrolls.loadClueInterface(player, itemId)) {
	    player.getActionSender().sendMessage("Clue ID: " + itemId);
	    return;
	}
	if (TeleTabs.breakTab(player, itemId, player.getSlot())) {
	    return;
	}
	if (TabHandler.breakEnchantTab(player, itemId, player.getSlot())) {
	    return;
	}
	if (TabHandler.breakBonesTab(player, itemId, player.getSlot())) {
	    return;
	}
	if (player.getMultiCannon().itemFirstClick(itemId, player.getSlot())) {
	    return;
	}
	if (Toys.itemFirstClick(player, itemId, player.getSlot())) {
	    return;
	}
	for (Quest q : QuestHandler.getQuests()) {
	    if (q.itemHandling(player, itemId)) {
		return;
	    }
	}
	switch (itemId) {
		case 4049 :
			player.getUpdateFlags().sendAnimation(829);
			if(player.wearingCwBracelet()){
				player.heal((int)Misc.fromPercent(15, player.getMaxHp()));
			}else{
				player.heal((int)Misc.fromPercent(10, player.getMaxHp()));
			}
			player.removeAllEffects();
			if (!player.getInventory().removeItemSlot(item, player.getSlot())) {
				player.getInventory().removeItem(item);
			}
			player.getTask();
			player.getCombatDelayTick().setWaitDuration(player.getCombatDelayTick().getWaitDuration() + 2);
		return;
		case 4053 :
			CastlewarsBarricades.PlaceBarricade(player, itemId, player.getSlot());
		return;
	    case 2528: // genie lamp
		player.setGenieSelect(-1);
		player.getActionSender().sendConfig(261, 0);
		player.getActionSender().sendInterface(2808);
		return;
	    case 550: // newcomers map
		player.getActionSender().sendInterface(5392);
		return;
	    case 33: // candle
		if (player.getInventory().removeItemSlot(item, player.getSlot())) {
		    player.getActionSender().sendMessage("You extinguish the candle.");
		    player.getInventory().addItemToSlot(new Item(36), player.getSlot());
		}
		return;
	    case 594: // torch
		if (player.getInventory().removeItemSlot(item, player.getSlot())) {
		    player.getActionSender().sendMessage("You extinguish the torch.");
		    player.getInventory().addItemToSlot(new Item(596), player.getSlot());
		}
		return;
	    case 4531: // candle lantern
		if (player.getInventory().removeItemSlot(item, player.getSlot())) {
		    player.getActionSender().sendMessage("You extinguish the candle lantern.");
		    player.getInventory().addItemToSlot(new Item(4529), player.getSlot());
		}
		return;
	    case 4534: // black candle lantern
		if (player.getInventory().removeItemSlot(item, player.getSlot())) {
		    player.getActionSender().sendMessage("You extinguish the black candle lantern.");
		    player.getInventory().addItemToSlot(new Item(4532), player.getSlot());
		}
		return;
	    case 4539: // oil lamp
		if (player.getInventory().removeItemSlot(item, player.getSlot())) {
		    player.getActionSender().sendMessage("You extinguish the oil lamp.");
		    player.getInventory().addItemToSlot(new Item(4537), player.getSlot());
		}
		return;
	    case 4550: // bullseye lantern
		if (player.getInventory().removeItemSlot(item, player.getSlot())) {
		    player.getActionSender().sendMessage("You extinguish the bullseye lantern.");
		    player.getInventory().addItemToSlot(new Item(4548), player.getSlot());
		}
		return;
	    case 405: // casket
		if (player.getInventory().removeItemSlot(item, player.getSlot())) {
		    Casket.openCasket(player);
		}
		return;
	    case 2150: // swamp toad
		if (player.getInventory().removeItemSlot(item, player.getSlot())) {
		    player.getActionSender().sendMessage("You pull the legs off the toad. Poor toad. At least they'll grow back.");
		    player.getInventory().addItemToSlot(new Item(2152), player.getSlot());
		}
		return;
	    case 407: //oyster
		if (player.getInventory().removeItemSlot(item, player.getSlot())) {
		    player.getActionSender().sendMessage("You open the oyster.");
		    player.getInventory().addItemToSlot(new Item(411), player.getSlot());
		}
		return;
	    case 4033:
		//  ShopManager.openShop(player, 39);
		player.getActionSender().sendMessage("You poke the monkey.");
		return;
	    case 10592:
		player.getPets().registerPet(10592, 5428);
		return;
	    case 2005: //Burnt Stew
		 if (player.getInventory().removeItemSlot(item, player.getSlot()))  {
			player.getActionSender().sendMessage("You empty the burnt stew");
			player.getInventory().addItemToSlot(new Item(1923), player.getSlot());
		}
		return;
	    case 2013: //Burnt Curry
		if (player.getInventory().removeItemSlot(item, player.getSlot()))  {
                        player.getActionSender().sendMessage("You empty the burnt curry");
                        player.getInventory().addItemToSlot(new Item(1923), player.getSlot());
                }
                return;

	    case 952: //spade
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
			/*if (player.getPosition().getX() == 2566 && (player.getPosition().getY() == 3331 || player.getPosition().getY() == 3333)) {
			    player.fadeTeleport(new Position(2530, 3303));
			    player.getActionSender().sendMessage("and find yourself in plague city.");
			    container.stop();
			    return;
			}*/
			if (player.getPosition().getX() == 2999 && player.getPosition().getY() == 3383 && (player.getQuestStage(20) == 6 || player.getQuestStage(20) == 7)) {
			    player.setQuestStage(20, 8);
			    QuestHandler.completeQuest(player, 20);
			    player.getInventory().addItem(new Item(PiratesTreasure.CHEST));
			    container.stop();
			    return;
			}
			if (player.getPosition().equals(GhostsAhoy.TREASURE_LOCATION) && !player.getInventory().playerHasItem(GhostsAhoy.BOOK_OF_HARICANTO)) {
			    player.getDialogue().sendGiveItemNpc("You unearth the Book of Haricanto.", new Item(GhostsAhoy.BOOK_OF_HARICANTO));
			    player.getInventory().addItem(new Item(GhostsAhoy.BOOK_OF_HARICANTO));
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
	    case 2574: // sextant
		Sextant.initializeRandomSextantInterface(player);
		return;
	    case 299:
		player.getMithrilSeeds().plantMithrilSeed(player);
		return;
	    case 6722:
	    	player.getUpdateFlags().sendAnimation(2840);
			player.getUpdateFlags().setForceChatMessage("Alas!");
		return;
	    case 6040:// ammy of nature
		Dialogues.startDialogue(player, 10016);
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
	if (item == null || item.getId() != itemId) {
	    return;
	}
	Pouches.checkEssencePouch(player, item.getId());
	switch (itemId) {
	    case NatureSpirit.SILVER_SICKLE_B:
		NatureSpirit.handleDruidicSpell(player, true);
		return;
	    case 4566: // rubber chicken
	    	player.getUpdateFlags().sendAnimation(1835);
		return;
	    case 6722:
	    	Dialogues.startDialogue(player, 2868);
		return;
	    case 11284: //dfs charge check
	    case 11283:
		player.getActionSender().sendMessage("You have " + player.getDfsCharges() + " dragonbreath charge(s).");
		return;
	    case 3840:
	    case 3842:
	    case 3844: //god books
		GodBook.preachGodBook(player, itemId);
		return;
	    case 2568: // RING OF FORGING CHARGE CHECK
		player.getActionSender().sendMessage("You have " + player.getRingOfForgingLife() + " Ring of Forging charge(s) remaining.");
		return;
	    case 11074: // BRACELET OF CLAY CHARGE CHECK
		player.getActionSender().sendMessage("You have " + player.getClayBraceletLife() + " Bracelet of Clay charge(s) remaining.");
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
	if (item == null || item.getId() != player.getClickItem()) {
	    return;
	}
	if (PotionMaking.emptyPotion(player, new Item(player.getClickItem()), player.getSlot())) {
	    return;
	}
	if (Runecrafting.clickTalisman(player, player.getClickItem())) {
	    return;
	}
	if (item.getId() == 11283) {
	    player.getActionSender().sendMessage("You empty the charges from your Dragonfire shield.");
	    player.getInventory().replaceItemWithItem(new Item(11283), new Item(11284));
	    player.getUpdateFlags().sendAnimation(6700);
	    player.setDfsCharges(0);
	    return;
	}
	for (int[] element : Pouches.POUCHES) {
	    if (item.getId() == element[0]) {
		Pouches.emptyEssencePouch(player, item.getId());
		return;
	    }
	}
	switch (item.getId()) {
	    case 4006: //monkey dentures
		player.getActionSender().sendMessage("You hear a quiet chattering coming from the dentures.");
		return;
	    case 4007: //enchanted bar
		player.getActionSender().sendMessage("You hear a very faint chattering coming from the metal.");
		return;
	    case 2552: // ring of duelling
	    case 2554:
	    case 2556:
	    case 2558:
	    case 2560:
	    case 2562:
	    case 2564:
	    case 2566:
		Dialogues.startDialogue(player, 10004);
		break;
	    case 431: //karamjan rum
		player.getActionSender().statEdit(Skill.ATTACK, -3, false);
		player.getActionSender().statEdit(Skill.STRENGTH, 2, true);
		player.getActionSender().sendMessage("You drink your Karamjan Rum and drop the bottle, whoops!.");
		player.getInventory().removeItem(new Item(431));
		player.setDrunkState(true, 300);
		break;
	    case 1712: // glory
	    case 1710:
	    case 1708:
	    case 1706:
		Dialogues.startDialogue(player, 10003);
		break;
	    case 11105: // glory
	    case 11107:
	    case 11109:
	    case 11111:
		Dialogues.startDialogue(player, 10015);
		break;
	    case 3853:
	    case 3855:
	    case 3857:
	    case 3859:
	    case 3861:
	    case 3863:
	    case 3865:
	    case 3867:
		Dialogues.startDialogue(player, 10002);
		break;
	    case 11118: // glory
	    case 11120:
	    case 11122:
	    case 11124:
		Dialogues.startDialogue(player, 10014);
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
	if (player.inTempleKnightsTraining()) {
	    if(itemId == 5607 || itemId == 5608 || itemId == 5609) {
		player.getDialogue().sendPlayerChat("I had better just carry this across in my backpack.", Dialogues.CONTENT);
		player.getDialogue().endDialogue();
		return;
	    }
	}
	if(itemId == 4035 && player.getQuestStage(36) == 19 && !player.Area(2688, 2748, 9154, 9214)) {
	    Dialogues.startDialogue(player, 885789);
	    return;
	}
	if (itemId == 6893 || itemId == 6894 || itemId == 6895 || itemId == 6897 || itemId == 5607 || itemId == 5608 || itemId == 5609) {
	    player.getActionSender().sendMessage("You cannot equip this item.");
	    return;
	}
	if (itemId == 6722){
		player.getUpdateFlags().sendAnimation(2844);
		player.getUpdateFlags().setForceChatMessage("Muahahahah!");
	}
	if (itemId == 6541) {
	    player.getPets().registerPet(6541, 901);
	    return;
	}
	if ((itemId >= 3327 && itemId < 3344)) {
	    player.getEquipment().equip(player.getSlot());
	    return;
	}
	if (QuestHandler.getQuests()[19].itemHandling(player, itemId)) { //Goblin Diplomacy
	    return;
	}
	if (QuestHandler.getQuests()[25].itemHandling(player, itemId)) { //Animal Magnetism
	    return;
	}
	if (ApeAtoll.handleGreeGreeEquip(player, itemId)) {
	    return;
	}
	if (itemId == 11664) {
	    if (player.hasFullVoidRange() && player.getSkill().getLevel()[4] <= player.getSkill().getPlayerLevel(4)) {
		player.getSkill().getLevel()[4] = (int) (player.getSkill().getPlayerLevel(4) * 1.1);
		return;
	    }
	}
	if (new Item(itemId).getDefinition().getSlot() == -1) {
	    return;
	}
	if (player.getDuelMainData().getOpponent() != null && !player.inDuelArena()) {
	    player.getDuelInteraction().endDuelInteraction(true);
	    return;
	}
	//player.getActionSender().removeInterfaces();
	Item item = player.getInventory().getItemContainer().get(player.getSlot());
	if (item == null || item.getId() != itemId || !item.validItem()) {
	    return;
	}
	player.getEquipment().equip(player.getSlot());
	player.setEquipmentOperate(false);
    }

    private void handleCastedSpellOnItem(Player player, Packet packet) {
	StreamBuffer.InBuffer in = packet.getIn();
	player.setSlot(in.readShort());
	int itemId = in.readShort(StreamBuffer.ValueType.A);
	player.setInterfaceId(in.readShort());
	int magicId = in.readShort(StreamBuffer.ValueType.A);
	Spell spell = player.getMagicBookType().getSpells().get(magicId);
	Item item = player.getInventory().getItemContainer().get(player.getSlot());
	if (item == null || item.getId() != itemId || !item.validItem()) {
	    return;
	}
	if (spell != null) {
	    MagicSkill.spellOnItem(player, spell, itemId, player.getSlot());
	} else if (player.getStaffRights() > 1 && Constants.SERVER_DEBUG) {
	    System.out.println("Slot: " + player.getSlot() + " Item id: " + itemId + " Interface ID: " + player.getInterfaceId() + " magic id: " + magicId);
	}
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
	} else if (player.getStaffRights() > 1 && Constants.SERVER_DEBUG) {
	    System.out.println("Magic ID: " + magicId + " Item ID: " + itemId + " X: " + x + " Y: " + y);
	}
    }

    private void operateItem(final Player player, int itemId) {
	if ((SkillCapeHandler.SkillCape.forItemId(itemId) != null)) {
	    if (!player.skillCapeBoost) {
		player.skillCapeBoost = true;
		player.getActionSender().statEdit(SkillCapeHandler.SkillCape.forItemId(itemId).getSkillId(), 1, true);
		player.getActionSender().sendMessage("You feel a slight boost in your abilities.");
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		    @Override
		    public void execute(CycleEventContainer b) {
			b.stop();
		    }

		    @Override
		    public void stop() {
			player.skillCapeBoost = false;
		    }
		}, 60);
		return;
	    } else {
		player.getActionSender().sendMessage("You must wait a minute in between stat boosts!");
		return;
	    }
	}
	if(Constants.DEGRADING_ENABLED) {
		    Item item = new Item(itemId);
		    Degradeables d = Degradeables.getDegradeableItem(item);
		    if (d != null) {
			if (d.getOriginalId() == itemId) {
			    if (player.getDegradeableHits()[d.getPlayerArraySlot()] <= 0) {
				player.setDegradeableHits(d.getPlayerArraySlot(), 0);
				player.getActionSender().sendMessage("Your " + item.getDefinition().getName().toLowerCase() + " will degrade and become untradeable upon combat.", true);
			    }
			}
			int count = 1;
			for (int i : d.getIterableDegradedIds(!item.getDefinition().getName().toLowerCase().contains("crystal"))) {
			    if (item.getId() == i) {
				int hitCount = player.getDegradeableHits()[Degradeables.getDegradeableItem(item).getPlayerArraySlot()];
				player.getActionSender().sendMessage("You have " + ((Degradeables.DEGRADE_HITS * count) - hitCount) + " hits on your " + item.getDefinition().getName().toLowerCase() + " until the next degrade.", true);
			    }
			    count++;
			}
		}
	}
	switch (itemId) {
	    case 11283:
		if(player.dfsTimer) {
		    player.getActionSender().sendMessage("You must wait 2 minutes in between operation for this item.");
		    return;
		}
		if(!player.getInCombatTick().completed()) {
		    player.getActionSender().sendMessage("Your Dragonfire shield has become partially depleted.");
		    player.getEquipment().replaceEquipment(11284, Constants.SHIELD);
		    player.clickSpecialBar(1010101);
		}
		return;
	    case 11284:
		if(player.dfsTimer) {
		    player.getActionSender().sendMessage("You must wait 2 minutes in between operation for this item.");
		    return;
		}
		if (player.getDfsCharges() > 0) {
		    player.clickSpecialBar(1010101);
		} else {
		    player.getActionSender().sendMessage("You have no charges left!");
		}
		return;
	    case 6885: //Mage Training hat
		Dialogues.startDialogue(player, 3096);
		return;
	    case 4566: // rubber chicken
		player.getUpdateFlags().sendAnimation(1835);
		return;
	    case NatureSpirit.SILVER_SICKLE_B:
	    	NatureSpirit.handleDruidicSpell(player, true);
		return;
	    case 3840:
	    case 3842:
	    case 3844: //god books
	    	GodBook.preachGodBook(player, itemId);
		return;
	    case 2568: // RING OF FORGING CHARGE CHECK
	    	player.getActionSender().sendMessage("You have " + player.getRingOfForgingLife() + " Ring of Forging charge(s) remaining.");
    	return;
	    case 11074: // BRACELET OF CLAY CHARGE CHECK
	    	player.getActionSender().sendMessage("You have " + player.getClayBraceletLife() + " Bracelet of Clay charge(s) remaining.");
    	return;
	    case 2552: // ring of duelling
	    case 2554:
	    case 2556:
	    case 2558:
	    case 2560:
	    case 2562:
	    case 2564:
	    case 2566:
	    	player.setEquipmentOperate(true);
	    	Dialogues.startDialogue(player, 10004);
		break;
	    case 1712: // glory
	    case 1710:
	    case 1708:
	    case 1706:
	    	player.setEquipmentOperate(true);
	    	Dialogues.startDialogue(player, 10003);
		break;
	    case 11105: // glory
	    case 11107:
	    case 11109:
	    case 11111:
	    	player.setEquipmentOperate(true);
	    	Dialogues.startDialogue(player, 10015);
		break;
	    case 3853:
	    case 3855:
	    case 3857:
	    case 3859:
	    case 3861:
	    case 3863:
	    case 3865:
	    case 3867:
	    	player.setEquipmentOperate(true);
	    	Dialogues.startDialogue(player, 10002);
		break;
	    case 11118: // glory
	    case 11120:
	    case 11122:
	    case 11124:
	    	player.setEquipmentOperate(true);
	    	Dialogues.startDialogue(player, 10014);
		break;
	}
    }
}
