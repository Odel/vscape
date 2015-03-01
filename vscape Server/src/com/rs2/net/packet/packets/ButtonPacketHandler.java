package com.rs2.net.packet.packets;

import com.rs2.Constants;
import com.rs2.cache.interfaces.RSInterface;
import com.rs2.model.Position;
import com.rs2.model.content.combat.util.Degradeables;
import com.rs2.model.content.combat.util.Degrading;
import com.rs2.model.content.minigames.castlewars.impl.CatapultInterface;
import com.rs2.model.content.minigames.pestcontrol.PestControlRewardHandler;
import com.rs2.model.content.quests.DwarfCannon;
import com.rs2.model.content.quests.FamilyCrest;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.quests.RecruitmentDrive;
import com.rs2.model.content.randomevents.TalkToEvent;
import com.rs2.model.content.skills.SkillsX;
import com.rs2.model.content.skills.Crafting.DramenBranch;
import com.rs2.model.content.skills.Crafting.GlassMaking;
import com.rs2.model.content.skills.Crafting.LeatherMakingHandler;
import com.rs2.model.content.skills.Crafting.PotteryMaking;
import com.rs2.model.content.skills.Crafting.SilverCrafting;
import com.rs2.model.content.skills.Crafting.Spinning;
import com.rs2.model.content.skills.Crafting.Tanning;
import com.rs2.model.content.skills.Crafting.Weaving;
import com.rs2.model.content.skills.Fletching.HandleLogCutting;
import com.rs2.model.content.skills.cooking.Cooking;
import com.rs2.model.content.skills.cooking.DairyChurn;
import com.rs2.model.content.skills.cooking.DoughHandler;
import com.rs2.model.content.skills.cooking.FillHandler;
import com.rs2.model.content.skills.cooking.SliceDiceHandler;
import com.rs2.model.content.skills.magic.BoltEnchanting;
import com.rs2.model.content.skills.magic.MagicSkill;
import com.rs2.model.content.skills.magic.Spell;
import com.rs2.model.content.skills.prayer.Ectofuntus;
import com.rs2.model.content.skills.smithing.Smelting;
import com.rs2.model.content.treasuretrails.Sextant;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.players.Player;
import com.rs2.model.players.Player.BankOptions;
import com.rs2.model.players.TradeManager;
import com.rs2.model.transport.GnomeGlider;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;
import com.rs2.util.Misc;
import com.rs2.model.players.item.Item;

public class ButtonPacketHandler implements PacketHandler {

	public static final int BUTTON = 185;

	@Override
	public void handlePacket(Player player, Packet packet) {
        byte[] data = packet.getIn().readBytes(2);
        int interfaceId = 0;
        interfaceId |= (data[0] & 0xff) << 8;
        interfaceId |= (data[1] & 0xff);
        RSInterface inter = RSInterface.forId(interfaceId);
        int buttonId = Misc.hexToInt(data);
        if (!player.hasInterfaceOpen(inter)) {
            //player.getActionSender().removeInterfaces();
           // return;
        }
		handleButton(player, buttonId);
	}

	private void handleButton(Player player, int buttonId) {
		if (Constants.SERVER_DEBUG) {
			System.out.println(player.getUsername() + "button id: "+buttonId);
		}
		if (player.getDuelMainData().handleButton(buttonId))
			return;
		// Buttons that can be used while packets stopped
		switch (buttonId) {
			case 23132 : // unmorph
				player.getActionSender().removeInterfaces();
				player.transformNpc = -1;
				player.getActionSender().sendSideBarInterfaces();
				player.setAppearanceUpdateRequired(true);
				//player.getEquipment().unequip(Constants.RING);
				return;
            case 9118:
                player.getActionSender().removeInterfaces();
                return;
			/* Skill menus */
			case 33206 : // attack
				player.getSkillGuide().attackComplex(1);
				player.getSkillGuide().selected = 0;
				return;
			case 33209 : // strength
				player.getSkillGuide().strengthComplex(1);
				player.getSkillGuide().selected = 1;
				return;
			case 33212 : // Defence
				player.getSkillGuide().defenceComplex(1);
				player.getSkillGuide().selected = 2;
				return;
			case 33215 : // range
				player.getSkillGuide().rangedComplex(1);
				player.getSkillGuide().selected = 3;
				return;
			case 33218 : // prayer
				player.getSkillGuide().prayerComplex(1);
				player.getSkillGuide().selected = 4;
				return;
			case 33221 : // mage
				player.getSkillGuide().magicComplex(1);
				player.getSkillGuide().selected = 5;
				return;
			case 33224 : // runecrafting
				player.getSkillGuide().runecraftingComplex(1);
				player.getSkillGuide().selected = 6;
				return;
			case 33207 : // hp
				player.getSkillGuide().hitpointsComplex(1);
				player.getSkillGuide().selected = 7;
				return;
			case 33210 : // agility
				player.getSkillGuide().agilityComplex(1);
				player.getSkillGuide().selected = 8;
				return;
			case 33213 : // herblore
				player.getSkillGuide().herbloreComplex(1);
				player.getSkillGuide().selected = 9;
				return;
			case 33216 : // theiving
				player.getSkillGuide().thievingComplex(1);
				player.getSkillGuide().selected = 10;
				return;
			case 33219 : // crafting
				player.getSkillGuide().craftingComplex(1);
				player.getSkillGuide().selected = 11;
				return;
			case 33222 : // fletching
				player.getSkillGuide().fletchingComplex(1);
				player.getSkillGuide().selected = 12;
				return;
			case 47130 :// slayer
				player.getSkillGuide().slayerComplex(1);
				player.getSkillGuide().selected = 13;
				return;
			case 33208 :// mining
				player.getSkillGuide().miningComplex(1);
				player.getSkillGuide().selected = 14;
				return;
			case 33211 : // smithing
				player.getSkillGuide().smithingComplex(1);
				player.getSkillGuide().selected = 15;
				return;
			case 33214 : // fishing
				player.getSkillGuide().fishingComplex(1);
				player.getSkillGuide().selected = 16;
				return;
			case 33217 : // cooking
				player.getSkillGuide().cookingComplex(1);
				player.getSkillGuide().selected = 17;
				return;
			case 33220 : // firemaking
				player.getSkillGuide().firemakingComplex(1);
				player.getSkillGuide().selected = 18;
				return;
			case 33223 : // woodcut
				player.getSkillGuide().woodcuttingComplex(1);
				player.getSkillGuide().selected = 19;
				return;
			case 54104 : // farming
				player.getSkillGuide().farmingComplex(1);
				player.getSkillGuide().selected = 20;
				return;

			case 34142 : // tab 1
				player.getSkillGuide().menuCompilation(1);
				return;

			case 34119 : // tab 2
				player.getSkillGuide().menuCompilation(2);
				return;

			case 34120 : // tab 3
				player.getSkillGuide().menuCompilation(3);
				return;

			case 34123 : // tab 4
				player.getSkillGuide().menuCompilation(4);
				return;

			case 34133 : // tab 5
				player.getSkillGuide().menuCompilation(5);
				return;

			case 34136 : // tab 6
				player.getSkillGuide().menuCompilation(6);
				return;

			case 34139 : // tab 7
				player.getSkillGuide().menuCompilation(7);
				return;

			case 34155 : // tab 8
				player.getSkillGuide().menuCompilation(8);
				return;
			    
			case 34158 : // tab 9
				player.getSkillGuide().menuCompilation(9);
				return;

			case 34161 : // tab 10
				player.getSkillGuide().menuCompilation(10);
				return;

			case 59199 : // tab 11
				player.getSkillGuide().menuCompilation(11);
				return;

			case 59202 : // tab 12
				player.getSkillGuide().menuCompilation(12);
				return;

			case 59205 : // tab 13
				player.getSkillGuide().menuCompilation(13);
				return;
			case 152 :
				player.getMovementHandler().setRunToggled(false);
				return;
			case 153 :
				if (player.getNewComersSide().getTutorialIslandStage() == 22)
					player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);
				player.getMovementHandler().setRunToggled(true);
				return;
			case 74214 :	
				if(player.getMovementHandler().isRunToggled())
				{
					player.getMovementHandler().setRunToggled(false);
				}else{
					player.getMovementHandler().setRunToggled(true);
				}
				return;
			case 89061 :	
			case 93209 :
			case 93217 :
			case 93225 :
			case 93233 :
			case 93240 :
			case 93202 :
			case 94051 :
				if(player.shouldAutoRetaliate())
				{
					player.setAutoRetaliate(false);
				}else{
					player.setAutoRetaliate(true);
				}
				return;
			case 150 :
				player.setAutoRetaliate(true);
				return;
			case 151 :
				player.setAutoRetaliate(false);
				return;
			case 3138 :
				player.setScreenBrightness(1);
				return;
			case 3140 :
				player.setScreenBrightness(2);
				return;
			case 3142 :
				//player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() - 1, true);
				player.setScreenBrightness(3);
				return;
			case 3144 :
				//player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);
				player.setScreenBrightness(4);
				return;
			case 3145 :
				player.setMouseButtons(0);
				return;
			case 3146 :
				//player.setMouseButtons(1);
				if(player.getMouseButtons() == 0) { player.setMouseButtons(1); }
				else if(player.getMouseButtons() == 1) { player.setMouseButtons(0); }
				return;
			case 3147 :
				if(player.getChatEffects() == 0) { player.setChatEffects(1); }
				else if(player.getChatEffects() == 1) { player.setChatEffects(0); }
				//player.setChatEffects(0);
				return;
			case 3148 :
				player.setChatEffects(1);
				return;
			case 49169:
			case 49167:
				player.getActionSender().sendMessage("There are no more pages in this book!");
				return;
			case 39178: //book interface close
				player.getActionSender().removeInterfaces();
				player.setStatedInterface("");
				player.setInterface(0);
				return;
			case 3189 :
				if(player.getSplitPrivateChat() == 0) { player.setSplitPrivateChat(1); }
				else if(player.getSplitPrivateChat() == 1) { player.setSplitPrivateChat(0); }
			//	player.setSplitPrivateChat(1);
				return;
			case 3190 :
				player.setSplitPrivateChat(0);
				return;
			case 48176 :
				if(player.getAcceptAid() == 0) { player.setAcceptAid(1); }
				else if(player.getAcceptAid() == 1) { player.setAcceptAid(0); }
			//	player.setAcceptAid(0);
				return;
			case 48177 :
				player.setAcceptAid(1);
				return;
			case 38197 :
				player.toggleMusicLooping();
				return;
			case 24125 :
				player.toggleMusicAuto();
				return;
			case 24126 :
				player.setMusicAuto(false);
				return;
			case 3162 :// setMusicVolume (0/4)
				player.setMusicVolume(4);
				//player.getActionSender().sendConfig(168, player.getMusicVolume());
				return;
			case 3163 :// setMusicVolume (1/4)
				player.setMusicVolume(3);
				//player.getActionSender().sendConfig(168, player.getMusicVolume());
				return;
			case 3164 :// setMusicVolume (2/4)
				player.setMusicVolume(2);
				//player.getActionSender().sendConfig(168, player.getMusicVolume());
				return;
			case 3165 :// setMusicVolume (3/4)
				player.setMusicVolume(1);
				//player.getActionSender().sendConfig(168, player.getMusicVolume());
				return;
			case 3166 :// setMusicVolume (4/4)
				player.setMusicVolume(0);
				//player.getActionSender().sendConfig(168, player.getMusicVolume());
				return;
			case 3173 :// setEffectVolume (0/4)
				player.setEffectVolume(4);
				return;
			case 3174 :// setEffectVolume (1/4)
				player.setEffectVolume(3);
				return;
			case 3175 :// setEffectVolume (2/4)
				player.setEffectVolume(2);
				return;
			case 3176 :// setEffectVolume (3/4)
				player.setEffectVolume(1);
				return;
			case 3177 :// setEffectVolume (4/4)
				player.setEffectVolume(0);
				return;
			case 21010 :
				if(player.isWithdrawAsNote())
				{
					player.setWithdrawAsNote(false);
				}else{
					player.setWithdrawAsNote(true);
				}
				return;
			case 77115: //piety
			case 77113: //chivalry
			    player.getActionSender().sendMessage("This prayer is disabled.");
			    player.getActionSender().sendConfig(buttonId == 77113 ? 706 : 707, 0);
			    return;
			/*case 21011 :
				player.setWithdrawAsNote(false);
				return;*/
			/*case 31194 :
				player.setBankOptions(BankOptions.SWAP_ITEM);
				return;*/
			case 31195 :
				if (player.getBankOptions().equals(BankOptions.SWAP_ITEM)) {
					player.setBankOptions(BankOptions.INSERT_ITEM);
				} else if (player.getBankOptions().equals(BankOptions.INSERT_ITEM)) {
					player.setBankOptions(BankOptions.SWAP_ITEM);
				}
				//player.setBankOptions(BankOptions.INSERT_ITEM);
				return;
			case 97184 :
				if(player.getClanChat() != null)
				{
					player.getClanChat().leaveChat(player, false);
				}
				return;
		}
		if (QuestHandler.handleQuestButtons(player, buttonId))
		{
			return;
		}
		if(player.getMusicPlayer().handleButton(buttonId))
			return;
		if (MagicSkill.clickingToAutoCast(player, buttonId))
			return;
		if (player.getEquipment().setFightMode(buttonId)) {
			player.getEquipment().sendWeaponInterface();
			return;
		}
		if (player.getPrayer().setPrayers(buttonId)) {
			return;
		}
		if (player.getRandomHandler().getPillory().handleButton(buttonId)) {
			return;
		}
		/**
		 * All buttons after this part cannot be used while player's packets are
		 * disabled
		 */
		if (player.stopPlayerPacket()) {
			return;
		}
		if(player.getBankManager().tabButtons(buttonId))
		{
			return;
		}
		switch (buttonId) {
			case 195080: //Deposit all Inventory Items (new client)
				player.getBankManager().bankAll();
			return;
			case 83093: //equipment interface 474
				player.getActionSender().sendInterface(21172, 3213);
			return;
			case 83051: //close equipment interface 474
				player.getActionSender().removeInterfaces();
			return;
			case 75010: // HOME TELEPORT
				player.getTeleportation().attemptHomeTeleport(new Position(Constants.LUMBRIDGE_X, Constants.LUMBRIDGE_Y, 0));
			return;
			/** Destroy item **/
			case 55095 :
				if (player.getDestroyItem() != null) {
				    Item item = player.getDestroyItem();
				    if (item.getId() == 2412) {
					player.getActionSender().sendMessage("You can now obtain another God Cape.");
					player.setCanHaveGodCape(true);
				    }
				    if (item.getId() == 2413) {
					player.getActionSender().sendMessage("You can now obtain another God Cape.");
					player.setCanHaveGodCape(true);
				    }
				    if (item.getId() == 2414) {
					player.getActionSender().sendMessage("You can now obtain another God Cape.");
					player.setCanHaveGodCape(true);
				    }
				    if (Degradeables.notDroppable(Degradeables.getDegradeableItem(item), item)) {
					GroundItemManager.getManager().dropItem(new GroundItem(new Item(Degradeables.getDegradeableItem(item).getBrokenId()), player));
					if(!Degrading.ownsDegradedVersion(player, item.getId())) {
					    player.setDegradeableHits(Degradeables.getDegradeableItem(item).getPlayerArraySlot(), 0);
					}
				    }
					player.getInventory().removeItem(player.getDestroyItem());
				}
			case 55096 :
				player.setDestroyItem(null);
				player.getActionSender().removeInterfaces();
				return;
			/** Teleother **/
			case 49022 :
				player.getTeleportation().teleportObelisk(player.getTeleotherPosition());
			case 49024 :
				player.getActionSender().removeInterfaces();
				return;
			case 23132 :
				player.getActionSender().removeInterfaces();
				player.getEquipment().unequip(Constants.RING);
				return;
			case 13218 :
				TradeManager.acceptStageTwo(player);
				return;
			case 13092 :
				TradeManager.acceptStageOne(player);
				return;
			case 14067 :
				player.setAppearanceUpdateRequired(true);
				player.getUpdateFlags().setUpdateRequired(true);
				player.getActionSender().removeInterfaces();
				return;
			case 9154 :
				if (!player.getInCombatTick().completed()) {
					player.getActionSender().sendMessage("You have to wait 10 seconds after combat in order to logout.");
					return;
				}
                if (player.inDuelArena()) {
                    player.getActionSender().sendMessage("You can't logout during a duel!");
                    return;
                }
                if (player.inPestControlGameArea()) {
                    player.getActionSender().sendMessage("You can't logout while in Pest control!");
                    return;
                }
                if (player.inCwGame() || player.inCwLobby()) {
                    player.getActionSender().sendMessage("You can't logout while in Castle wars!");
                    return;
                }
                if (player.inRandomEvent()) {
                    player.getActionSender().sendMessage("You can't logout in a random event area!");
                    return;
                }
				if(player.isInCutscene()) {
				    player.getActionSender().sendMessage("You can't logout during a cutscene!");
				    return;
				}
				if(player.inMageTrainingArena())
                {
                    player.getActionSender().sendMessage("You can't logout while in the Mage Training Arena!");
                    return;
                }
				player.getActionSender().sendLogout();
				return;
		}
		
		if (TalkToEvent.isGenieLampButton(player, buttonId)) {
			return;
		}
		if (player.clickSpecialBar(buttonId)) {
			return;
		}
		if (GnomeGlider.flightButtons(player, buttonId)) {
			return;
		}
		if (player.getSkillGuide().skillGuidesButton(buttonId)) {
			return;
		}
		if (player.getRandomHandler().getCurrentEvent() != null) {
			if(player.getRandomHandler().getCurrentEvent().handleButtons(buttonId))
			{
				return;
			}
		}
		if (Sextant.handleSextantButtons(player, buttonId)) {
			return;
		}
		Spell spell = player.getMagicBookType().getSpells().get(buttonId);
		if (spell != null) {
			MagicSkill.spellButtonClicked(player, spell);
			//if (player.getCreatureGraveyard().isInCreatureGraveyard() && spell == Spell.BONES_TO_BANANA || spell == Spell.BONES_TO_PEACH)
			//	player.getCreatureGraveyard().applyBonesToFruit(spell == Spell.BONES_TO_PEACH ? true : false);
			return;
		}
		if (player.getDialogue().optionButtons(buttonId)) {
			return;
		}
		if (player.getBankPin().clickPinButton(buttonId)) {
			return;
		}
		if (player.getEmotes().activateEmoteButton(buttonId)) {
			return;
		}
		if (player.getSkillcapeEmotes().doEmote(buttonId)) {
			return;
		}
		if (player.getRuneDraw().handleButtons(player, buttonId)) {
		    return;
		}
		if (SkillsX.handleXButtons(player, buttonId)) {
			return;
		}
		if (DairyChurn.handleButtons(player, buttonId, 0)) {
			return;
		}
		if (PotteryMaking.makePottery(player, buttonId, 0)) {
			return;
		}
		if (SilverCrafting.makeSilver(player, buttonId, 0)) {
			return;
		}
		if (Ectofuntus.handleButtons(player, buttonId)) {
			return;
		}
		if (FamilyCrest.buttonHandling(player, buttonId)) {
			return;
		}
		if (DwarfCannon.buttonHandling(player, buttonId)) {
			return;
		}
		if(RecruitmentDrive.buttonHandling(player, buttonId)) {
			return;
		}
		if (Spinning.spin(player, buttonId, 0)) {
			return;
		}
		if (GlassMaking.makeSilver(player, buttonId, 0)) {
			return;
		}
		if (LeatherMakingHandler.handleButtons(player, buttonId, 0)) {
			return;
		}
		if (Tanning.handleButtons(player, buttonId)) {
			return;
		}
		if (Weaving.weave(player, buttonId, 0)) {
			return;
		}
		if (DramenBranch.cutDramen(player, buttonId, 0)) {
			return;
		}
		if (HandleLogCutting.handleButtons(player, buttonId, 0)) {
			return;
		}
		if (Cooking.handleButtons(player, buttonId)) {
			return;
		}
		if (FillHandler.handleButtons(player, buttonId)) {
			return;
		}
		if (DoughHandler.handleButtons(player, buttonId, 0)) {
			return;
		}
		if (SliceDiceHandler.handleButtons(player, buttonId, 0)) {
			return;
		}
		if (PestControlRewardHandler.handleButtons(player, buttonId)) {
			return;
		}
		if (Smelting.handleSmelting(player, buttonId, 0)) {
			return;
		}
		if(player.getCanoe().craftCanoe(buttonId) || player.getCanoe().travelCanoe(buttonId))
		{
			return;
		}
		if (BoltEnchanting.handleButtons(player, buttonId)) {
			return;
		}
		if (CatapultInterface.HandleButtons(player, buttonId)) {
			return;
		}
        if (player.getStaffRights() > 1 && Constants.SERVER_DEBUG)
			System.out.println("button "+buttonId+" doesn't do anything");
	}
}
