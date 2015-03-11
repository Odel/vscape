package com.rs2.net.packet.packets;

import com.rs2.Constants;
import com.rs2.model.World;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.quests.impl.AnimalMagnetism;
import com.rs2.model.content.quests.impl.DemonSlayer;
import com.rs2.model.content.quests.impl.GhostsAhoy.GhostsAhoy;
import com.rs2.model.content.quests.impl.GhostsAhoy.GhostsAhoyPetition;
import com.rs2.model.content.quests.impl.HeroesQuest;
import com.rs2.model.content.quests.impl.TheGrandTree;
import com.rs2.model.content.quests.impl.TreeGnomeVillage;
import com.rs2.model.content.quests.impl.WaterfallQuest;
import com.rs2.model.content.skills.magic.Spell;
import com.rs2.model.content.skills.magic.SpellBook;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcDefinition;
import com.rs2.model.players.Player;
import com.rs2.model.players.WalkToActionHandler;
import com.rs2.model.players.WalkToActionHandler.Actions;
import com.rs2.net.StreamBuffer;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;
import com.rs2.util.Misc;

public class NpcPacketHandler implements PacketHandler {

	public static final int FIRST_CLICK = 155;
	public static final int SECOND_CLICK = 17;
	public static final int THIRD_CLICK = 21;
	public static final int FOURTH_CLICK = 230;
	public static final int ATTACK = 72;
	public static final int ATTACK_CHOMPY = 18;
	public static final int MAGIC_ON_NPC = 131;
	public static final int ITEM_ON_NPC = 57;
	
	public static final int EXAMINE_NPC = 222;

	@Override
	public void handlePacket(Player player, Packet packet) {
		if (player.stopPlayerPacket()) {
			return;
		}
		switch (packet.getOpcode()) {
			case EXAMINE_NPC :
				handleNpcExamine(player, packet);
				return;
		}
		player.getActionSender().removeInterfaces();
		player.resetAllActions();
		switch (packet.getOpcode()) {
			case FIRST_CLICK :
				handleFirstClick(player, packet);
				break;
			case SECOND_CLICK :
				handleSecondClick(player, packet);
				break;
			case THIRD_CLICK :
				handleThirdClick(player, packet);
				break;
			case FOURTH_CLICK :
				handleFourthClick(player, packet);
				break;
			case ATTACK_CHOMPY:
			case ATTACK :
				handleAttack(player, packet);
				break;
			case MAGIC_ON_NPC :
				handleMagicOnNpc(player, packet);
				break;
			case ITEM_ON_NPC :
				handleItemOnNpc(player, packet);
				break;
		}
	}
	
	private void handleNpcExamine(Player player, Packet packet) {
		int npcId = packet.getIn().readShort(); // Npc ID.
		NpcDefinition npcDef = new Npc(npcId).getDefinition();
		if(npcDef != null){
			if(npcDef.getExamine() == null || npcDef.getExamine() == "null"){
				player.getActionSender().sendMessage("It's an NPC.", true);
			}else{
				player.getActionSender().sendMessage(npcDef.getExamine(), true);
			}
		}else{
			player.getActionSender().sendMessage("It's an NPC.", true);
		}
	}

	private void handleFirstClick(Player player, Packet packet) {
		int npcSlot = packet.getIn().readShort(true, StreamBuffer.ByteOrder.LITTLE);
		if (npcSlot < 0 || npcSlot > World.getNpcs().length) {
			return;
		}
		Npc npc = World.getNpcs()[npcSlot];
		if (npc == null || !npc.isRealNpc()) {
			return;
		}
		player.setClickId(npc.getNpcId());
		player.setClickX(npc.getPosition().getX());
		player.setClickY(npc.getPosition().getY());
		player.setClickZ(player.getPosition().getZ());
		player.setNpcClickIndex(npcSlot);
		player.getUpdateFlags().faceEntity(npcSlot);
		if(!npc.isBoothBanker()) {
		    player.setFollowDistance(1);
		    player.setFollowingEntity(npc);
		}
		if(npc.isBoothBanker()) {
		    if(!Misc.checkClip(player.getPosition(), npc.getPosition(), true)) {
		    player.setClickX(npc.getUpdateFlags().getFace().getX() == npc.getPosition().getX() ? npc.getPosition().getX() : npc.getUpdateFlags().getFace().getX() < npc.getPosition().getX() ? npc.getUpdateFlags().getFace().getX() - 1 : npc.getUpdateFlags().getFace().getX() + 1);
		    player.setClickY(npc.getUpdateFlags().getFace().getY() == npc.getPosition().getY() ? npc.getPosition().getY() : npc.getUpdateFlags().getFace().getY() < npc.getPosition().getY() ? npc.getUpdateFlags().getFace().getY() - 1 : npc.getUpdateFlags().getFace().getY() + 1);
		    } else {
			player.setClickX(npc.getUpdateFlags().getFace().getX());
			player.setClickY(npc.getUpdateFlags().getFace().getY());
		    }
		}
		if (Constants.SERVER_DEBUG) {
			player.getActionSender().sendMessage("First click npc: "+player.getClickId());
		}
		if(DemonSlayer.handleNpcClick(player, npc.getNpcId())) {
		    return;
		}
		if(AnimalMagnetism.handleNpcClick(player, npc.getNpcId())) {
		    return;
		}
		if(npc.getNpcId() == TreeGnomeVillage.TRACKER_GNOME_2 || npc.getNpcId() == TheGrandTree.CHARLIE || npc.getNpcId() == WaterfallQuest.HUDON) {
		    player.walkTo(npc.getPosition(), true);
		    Dialogues.startDialogue(player, npc.getNpcId());
		    return;
		}
		if (npc.getNpcId() == GhostsAhoyPetition.GHOST_VILLAGER && player.getInventory().playerHasItem(GhostsAhoy.PETITION)) {
		    player.getPetition().setLastNpcTalked(npc);
		    if(player.getPetition().sendDialogue(player, npc.getNpcId(), 1, 0)) {
			player.getPetition().setChatId(1);
			return;
		    }
		}
		if (npc.getNpcId() == 3102) {
		    Dialogues.startDialogue(player, 3102);
		    return;
		}
		if (npc.getNpcId() == 1469 && player.getPosition().getX() <= 2606 && player.getQuestStage(36) >= 14) {
		    Dialogues.startDialogue(player, 1469);
		    return;
		}
		WalkToActionHandler.setActions(Actions.NPC_FIRST_CLICK);
		WalkToActionHandler.doActions(player);
	}

	private void handleSecondClick(Player player, Packet packet) {
		int npcSlot = packet.getIn().readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE) & 0xFFFF;
		if (npcSlot < 0 || npcSlot > World.getNpcs().length) {
			return;
		}
		Npc npc = World.getNpcs()[npcSlot];
		if (npc == null || !npc.isRealNpc()) {
			return;
		}
		player.setClickId(npc.getNpcId());
		player.setClickX(npc.getPosition().getX());
		player.setClickY(npc.getPosition().getY());
		player.setClickZ(player.getPosition().getZ());
		player.setNpcClickIndex(npcSlot);
		player.getUpdateFlags().faceEntity(npcSlot);
		if(!npc.isBoothBanker()) {
		    player.setFollowDistance(1);
		    player.setFollowingEntity(npc);
		}
		if(npc.isBoothBanker()) {
		    if(!Misc.checkClip(player.getPosition(), npc.getPosition(), true)) {
		    player.setClickX(npc.getUpdateFlags().getFace().getX() == npc.getPosition().getX() ? npc.getPosition().getX() : npc.getUpdateFlags().getFace().getX() < npc.getPosition().getX() ? npc.getUpdateFlags().getFace().getX() - 1 : npc.getUpdateFlags().getFace().getX() + 1);
		    player.setClickY(npc.getUpdateFlags().getFace().getY() == npc.getPosition().getY() ? npc.getPosition().getY() : npc.getUpdateFlags().getFace().getY() < npc.getPosition().getY() ? npc.getUpdateFlags().getFace().getY() - 1 : npc.getUpdateFlags().getFace().getY() + 1);
		    } else {
			player.setClickX(npc.getUpdateFlags().getFace().getX());
			player.setClickY(npc.getUpdateFlags().getFace().getY());
		    }
		}
		if (Constants.SERVER_DEBUG) {
			player.getActionSender().sendMessage("Second click npc: "+player.getClickId());
		}
		WalkToActionHandler.setActions(Actions.NPC_SECOND_CLICK);
		WalkToActionHandler.doActions(player);
	}

	private void handleThirdClick(Player player, Packet packet) {
		int npcSlot = packet.getIn().readShort(true);
		if (npcSlot < 0 || npcSlot > World.getNpcs().length) {
			return;
		}
		Npc npc = World.getNpcs()[npcSlot];
		if (npc == null || !npc.isRealNpc()) {
			return;
		}
		player.setClickId(npc.getNpcId());
		player.setClickX(npc.getPosition().getX());
		player.setClickY(npc.getPosition().getY());
		player.setClickZ(player.getPosition().getZ());
		player.setNpcClickIndex(npcSlot);
		player.getUpdateFlags().faceEntity(npcSlot);
		player.setFollowDistance(1);
		player.setFollowingEntity(npc);
		if (Constants.SERVER_DEBUG) {
			player.getActionSender().sendMessage("Third click npc: "+player.getClickId());
		}
		WalkToActionHandler.setActions(Actions.NPC_THIRD_CLICK);
		WalkToActionHandler.doActions(player);
	}

	private void handleFourthClick(Player player, Packet packet) {
	}

	private void handleAttack(final Player player, Packet packet) {
		int npcSlot = packet.getIn().readShort(StreamBuffer.ValueType.A);
		if (npcSlot < 0 || npcSlot > World.getNpcs().length) {
			return;
		}
		final Npc npc = World.getNpcs()[npcSlot];
		if (npc == null || !npc.isRealNpc()) {
			return;
		}
		player.setCastedSpell(null);
		if (npc.getPlayerOwner() != null && npc.getPlayerOwner() != player) {
			player.getActionSender().sendMessage(npc.getDefinition().getName() + " is not interested in interacting with you right now.");
			return;
		}
		if (npc.getDefinition().isAttackable()) {
		    if(npc.getNpcId() == HeroesQuest.GRIP) {
			HeroesQuest.handleShootGrip(player, npc);
			return;
		    }
			CombatManager.attack(player, npc);
		}
		else
			player.getActionSender().sendMessage("You cannot attack that npc!");
		/*
		 * AttackType.determineAttackType(player);
		 * player.setFollowDistance(player
		 * .getCombat().getDistanceForCombatType());
		 * player.setClickId(npc.getNpcId());
		 * player.setClickX(npc.getPosition().getX());
		 * player.setClickY(npc.getPosition().getY()); player.setTarget(npc);
		 * player.setInstigatingAttack(true); player.setFollowingEntity(npc);
		 */
	}

	private void handleMagicOnNpc(final Player player, Packet packet) {
		player.getMovementHandler().reset();
		int npcSlot = packet.getIn().readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		if (npcSlot < 0 || npcSlot > World.getNpcs().length) {
			return;
		}
		int magicId = packet.getIn().readShort(StreamBuffer.ValueType.A);
		final Npc npc = World.getNpcs()[npcSlot];
		if (npc == null || !npc.isRealNpc()) {
			return;
		}
		if (npc.getPlayerOwner() != null && npc.getPlayerOwner() != player) {
			player.getActionSender().sendMessage(npc.getDefinition().getName() + " is not interested in interacting with you right now.");
			return;
		}
		if(npc.getNpcId() == HeroesQuest.GRIP) {
		    player.getActionSender().sendMessage("You cannot attack this npc this way.");
		    return;
		}
		Spell spell = SpellBook.getSpell(player, magicId);
		if (spell != null) {
			player.setCastedSpell(spell);
			CombatManager.attack(player, npc);
		} else if (player.getStaffRights() > 1 && Constants.SERVER_DEBUG)
			System.out.println("Magic id: " + magicId);
	}

	private void handleItemOnNpc(final Player player, Packet packet) {
		final int itemId = packet.getIn().readShort(StreamBuffer.ValueType.A);
		final int npcSlot = packet.getIn().readShort(StreamBuffer.ValueType.A);
		final int itemSlot = packet.getIn().readShort(StreamBuffer.ByteOrder.LITTLE);
        if (player.getStaffRights() > 1 && Constants.SERVER_DEBUG)
        	System.out.println(itemId + " " + npcSlot + " " + itemSlot);
        if (npcSlot < 0 || npcSlot > World.getNpcs().length) {
			return;
		}
		final Npc npc = World.getNpcs()[npcSlot];
		if (npc == null || !npc.isRealNpc()) {
			return;
		}
		player.setNpcClickIndex(npcSlot);
		player.setInteractingEntity(npc);
		player.setClickItem(itemId);
		player.setSlot(itemSlot);
		player.setClickId(npc.getNpcId());
		player.setClickX(npc.getPosition().getX());
		player.setClickY(npc.getPosition().getY());
		player.setClickZ(player.getPosition().getZ());
		player.getUpdateFlags().faceEntity(npcSlot);
		WalkToActionHandler.setActions(Actions.ITEM_ON_NPC);
		WalkToActionHandler.doActions(player);
	}

}
