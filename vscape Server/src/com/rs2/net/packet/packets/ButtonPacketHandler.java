package com.rs2.net.packet.packets;

import com.rs2.Constants;
import com.rs2.cache.interfaces.RSInterface;
import com.rs2.model.content.Emotes.EMOTE;
import com.rs2.model.content.quests.QuestHandler;
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
import com.rs2.model.content.skills.Woodcutting.Canoe;
import com.rs2.model.content.skills.cooking.Cooking;
import com.rs2.model.content.skills.cooking.DairyChurn;
import com.rs2.model.content.skills.cooking.FlourRelated;
import com.rs2.model.content.skills.magic.MagicSkill;
import com.rs2.model.content.skills.magic.Spell;
import com.rs2.model.content.skills.smithing.Smelting;
import com.rs2.model.content.treasuretrails.Sextant;
import com.rs2.model.players.Player;
import com.rs2.model.players.Player.BankOptions;
import com.rs2.model.players.TradeManager;
import com.rs2.model.transport.GnomeGlider;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;
import com.rs2.util.Misc;
import com.rs2.model.players.BankManager;
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
        if (!player.hasInterfaceOpen(inter) && !player.getEmotes().isEmote(buttonId)) {
            //player.getActionSender().removeInterfaces();
            return;
        }
        //reenabled because im curious.
		handleButton(player, Misc.hexToInt(data));
	}

	private void handleButton(Player player, int buttonId) {
		if (Constants.SERVER_DEBUG) {
			System.out.println("button id: "+buttonId);
		}
		if (player.getDuelMainData().handleButton(buttonId))
			return;
		// Buttons that can be used while packets stopped
		switch (buttonId) {
            // smithing
			/*case 15147 :// bronze bar
				player.getSmithing().startSmelting(1, 0);
				break;
			case 15151 :// iron
				player.getSmithing().startSmelting(1, 1);
				break;
			case 15155 :// silver
				player.getSmithing().startSmelting(1, 2);
				break;
			case 15159 :// steel
				player.getSmithing().startSmelting(1, 3);
				break;
			case 15163 :// gold
				player.getSmithing().startSmelting(1, 4);
				break;
			case 29017 :// mith
				player.getSmithing().startSmelting(1, 5);
				break;
			case 29022 :// addy
				player.getSmithing().startSmelting(1, 6);
				break;
			case 29026 :// rune
				player.getSmithing().startSmelting(1, 7);
				break;
			case 15146 :
				player.getSmithing().startSmelting(5, 0);
				break;
			case 15150 :
				player.getSmithing().startSmelting(5, 1);
				break;
			case 15154 :
				player.getSmithing().startSmelting(5, 2);
				break;
			case 15158 :
				player.getSmithing().startSmelting(5, 3);
				break;
			case 15162 :
				player.getSmithing().startSmelting(5, 4);
				break;
			case 29016 :
				player.getSmithing().startSmelting(5, 5);
				break;
			case 29020 :
				player.getSmithing().startSmelting(5, 6);
				break;
			case 29025 :
				player.getSmithing().startSmelting(5, 7);
				break;
			case 10247 :// bronze bar 10
				player.getSmithing().startSmelting(10, 0);
				break;
			case 15149 :// iron bar 10
				player.getSmithing().startSmelting(10, 1);
				break;
			case 15153 :// silver bar 10
				player.getSmithing().startSmelting(10, 2);
				break;
			case 15157 :// steel bar 10
				player.getSmithing().startSmelting(10, 3);
				break;
			case 15161 :// gold bar 10
				player.getSmithing().startSmelting(10, 4);
				break;
			case 24253 :// mith bar 10
				player.getSmithing().startSmelting(10, 5);
				break;
			case 29019 :// addy bar 10
				player.getSmithing().startSmelting(10, 6);
				break;
			case 29024 :// rune bar 10
				player.getSmithing().startSmelting(10, 7);
				break;*/
			case 38197:  //music loop button
				player.toggleMusicLooping();
				break;
			case 23132 : // unmorph
				player.getActionSender().removeInterfaces();
				player.transformNpc = -1;
				player.getActionSender().sendSideBarInterfaces();
				player.setAppearanceUpdateRequired(true);
				//player.getEquipment().unequip(Constants.RING);
				break;
            case 9118:
                player.getActionSender().removeInterfaces();
                break;
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
				player.setMouseButtons(1);
				return;
			case 3147 :
				player.setChatEffects(0);
				return;
			case 3148 :
				player.setChatEffects(1);
				return;
			case 3189 :
				player.setSplitPrivateChat(1);
				return;
			case 3190 :
				player.setSplitPrivateChat(0);
				return;
			case 48176 :
				player.setAcceptAid(0);
				return;
			case 48177 :
				player.setAcceptAid(1);
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
				player.setWithdrawAsNote(true);
				return;
			case 21011 :
				player.setWithdrawAsNote(false);
				return;
			case 31194 :
				player.setBankOptions(BankOptions.SWAP_ITEM);
				return;
			case 31195 :
				player.setBankOptions(BankOptions.INSERT_ITEM);
				return;
				// STOP ADDING QUEST BUTTONS HERE 
				/*
			case 28165: //quest tab entry for cook's assistant
				QuestHandler.handleQuestButtons(player, buttonId);
				return;
			case 28178: //the knight's sword
				QuestHandler.handleQuestButtons(player, buttonId);
				return;
			case 28169: //the restless ghost
				QuestHandler.handleQuestButtons(player, buttonId);
				return;*/
		}
		// YOU ONLY NEED THIS ONE LIKE HERE
		if (QuestHandler.handleQuestButtons(player, buttonId))
		{
			return;
		}
		if (MagicSkill.clickingToAutoCast(player, buttonId))
			return;
		if (player.getEquipment().setFightMode(buttonId)) {
			player.getEquipment().sendWeaponInterface();
			return;
		}
		if (player.getPrayer().setPrayers(buttonId)) {
			return;
		}
		/**
		 * All buttons after this part cannot be used while player's packets are
		 * disabled
		 */
		if (player.stopPlayerPacket()) {
			return;
		}
		switch (buttonId) {
		case 73099: //Deposit all Inventory Items
			BankManager.bankAll(player);
			return;
			/** Destroy item **/
			case 55095 :
				if (player.getDestroyItem() != null) {
					player.getInventory().removeItem(player.getDestroyItem());
				}
			case 55096 :
				player.setDestroyItem(null);
				player.getActionSender().removeInterfaces();
				break;
			/** Teleother **/
			case 49022 :
				player.getTeleportation().teleportObelisk(player.getTeleotherPosition());
			case 49024 :
				player.getActionSender().removeInterfaces();
				return;
			/** Emotes **/
		/*	case 168 :
				player.getUpdateFlags().sendAnimation(855);
				return;
			case 169 :
				player.getUpdateFlags().sendAnimation(856);
				return;
			case 162 :
				player.getUpdateFlags().sendAnimation(857);
				return;
			case 164 :
				player.getUpdateFlags().sendAnimation(858);
				return;
			case 165 :
				player.getUpdateFlags().sendAnimation(859);
				return;
			case 161 :
				player.getUpdateFlags().sendAnimation(860);
				return;
			case 170 :
				player.getUpdateFlags().sendAnimation(861);
				return;
			case 171 :
				player.getUpdateFlags().sendAnimation(862);
				return;
			case 163 :
				player.getUpdateFlags().sendAnimation(863);
				return;
			case 167 :
				player.getUpdateFlags().sendAnimation(864);
				return;
			case 172 :
				player.getUpdateFlags().sendAnimation(865);
				return;
			case 166 :
				player.getUpdateFlags().sendAnimation(866);
				return;
			case 52050 :
				player.getUpdateFlags().sendAnimation(2105);
				return;
			case 52051 :
				player.getUpdateFlags().sendAnimation(2106);
				return;
			case 52052 :
				player.getUpdateFlags().sendAnimation(2107);
				return;
			case 52053 :
				player.getUpdateFlags().sendAnimation(2108);
				return;
			case 52054 :
				player.getUpdateFlags().sendAnimation(2109);
				return;
			case 52055 :
				player.getUpdateFlags().sendAnimation(2110);
				return;
			case 52056 :
				player.getUpdateFlags().sendAnimation(2111);
				return;
			case 52057 :
				player.getUpdateFlags().sendAnimation(2112);
				return;
			case 52058 :
				player.getUpdateFlags().sendAnimation(2113);
				return;
			case 43092 :
				player.getActionSender().stopPlayerPacket(4);
				player.getUpdateFlags().sendAnimation(0x558);
				player.getUpdateFlags().sendGraphic(574);
				return;
			case 2155 :
				player.getUpdateFlags().sendAnimation(0x46B);
				return;
			case 25103 :
				player.getUpdateFlags().sendAnimation(0x46A);
				return;
			case 25106 :
				player.getUpdateFlags().sendAnimation(0x469);
				return;
			case 2154 :
				player.getUpdateFlags().sendAnimation(0x468);
				return;
			case 52071 :
				player.getUpdateFlags().sendAnimation(0x84F);
				return;
			case 52072 :
				player.getUpdateFlags().sendAnimation(0x850);
				return;
			case 73003 :
				player.getUpdateFlags().sendAnimation(2836);
				return;
			case 73001 :
				player.getUpdateFlags().sendAnimation(3544);
				return;
			case 73000 :
				player.getUpdateFlags().sendAnimation(3543);
				return;
			case 72032 :
				player.getUpdateFlags().sendAnimation(3544);
				return;
			case 72033 :
				player.getUpdateFlags().sendAnimation(3543);
				return;
			case 59062 :
				player.getUpdateFlags().sendAnimation(2836);
				return;*/
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
                    player.getActionSender().sendMessage("You can't logout during a duel fight!");
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
		if (player.getRandomInterfaceClick().handleButtonClicking(buttonId)) {
			return;
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
		if (SkillsX.handleXButtons(player, buttonId)) {
			return;
		}
		if (DairyChurn.churnItem(player, buttonId)) {
			return;
		}
		if (PotteryMaking.makePottery(player, buttonId, 0)) {
			return;
		}
		if (SilverCrafting.makeSilver(player, buttonId, 0)) {
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
		if (FlourRelated.handleButton(player, buttonId)) {
			return;
		}
		if (Smelting.handleSmelting(player, buttonId, 0)) {
			return;
		}
		if(Canoe.craftCanoe(player, buttonId))
		{
			return;
		}
		if(Canoe.travelCanoe(player, buttonId))
		{
			return;
		}
        if (player.getStaffRights() > 1 && Constants.SERVER_DEBUG)
			System.out.println("button "+buttonId+" doesn't do anything");
	}
}
