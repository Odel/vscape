package com.rs2.model.players;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player.LoginStages;
import com.rs2.model.players.container.equipment.Equipment;
import com.rs2.model.players.item.Item;
import com.rs2.net.StreamBuffer;
import com.rs2.util.Misc;

import java.util.Iterator;

/*
 * This file is part of RuneSource.
 *
 * RuneSource is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RuneSource is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RuneSource.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Provides static utility methods for updating players.
 * 
 * @author blakeman8192
 */
public final class PlayerUpdating {

	/**
	 * Updates the player.
	 * 
	 * @param player
	 *            the player
	 */
	public static void update(Player player) {
		// Check for region changes.
		int deltaX = player.getPosition().getX() - player.getCurrentRegion().getRegionX() * 8;
		int deltaY = player.getPosition().getY() - player.getCurrentRegion().getRegionY() * 8;
		player.currentX = deltaX;
		player.currentY = deltaY;
		if (deltaX < 16 || deltaX >= 88 || deltaY < 16 || deltaY > 88) {
			player.getActionSender().sendMapRegion();
		}

		// XXX: The buffer sizes may need to be tuned.
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(8192);
		StreamBuffer.OutBuffer block = StreamBuffer.newOutBuffer(4096);

		// Initialize the update packet.
		out.writeVariableShortPacketHeader(player.getEncryptor(), 81);
		out.setAccessType(StreamBuffer.AccessType.BIT_ACCESS);

		// Update this player.
		PlayerUpdating.updateLocalPlayerMovement(player, out);
		if (player.getUpdateFlags().isUpdateRequired()) {
			PlayerUpdating.updateState(player, block, false, true);
		}

		// Update other local players.
		out.writeBits(8, player.getPlayers().size());
		for (Iterator<Player> i = player.getPlayers().iterator(); i.hasNext();) {
			Player other = i.next();
            /*if (other.getIndex() == -1)
                System.out.println("Player ("+player.getUsername()+") has a player ("+other.getUsername()+") with index -1 in local list");
            else if (World.getPlayers()[other.getIndex()] != other) {
                System.out.println("Player ("+other.getUsername()+") is on "+player.getUsername()+"'s local list. He is not in world list.");
                System.out.println("His login stage is: "+other.getLoginStage().name());
            }*/
			if ((other.getPosition().isViewableFrom(player.getPosition()) && other.isVisible()) && other.getLoginStage() != LoginStages.LOGGED_OUT && !other.needsPlacement()) {
				PlayerUpdating.updateOtherPlayerMovement(other, out);
				if (other.getUpdateFlags().isUpdateRequired()) {
					PlayerUpdating.updateState(other, block, false, false);
				}
			} else {
				out.writeBit(true);
				out.writeBits(2, 3);
				i.remove();
			}
		}

		int playersAdded = 0;
		// Update the local player list.
		for (int i = 0; i < World.getPlayers().length; i++) {
			if (playersAdded > 15 || player.getPlayers().size() >= 255) {
				// Player limit has been reached.
				break;
			}
			Player other = World.getPlayers()[i];
			if (other == null || other == player || other.getLoginStage() == LoginStages.LOGGED_OUT) {
				continue;
			}
			if (!player.getPlayers().contains(other) && (other.getPosition().isViewableFrom(player.getPosition()) && other.isVisible())) {
				playersAdded++;
				player.getPlayers().add(other);
				PlayerUpdating.addPlayer(out, player, other);
				PlayerUpdating.updateState(other, block, true, false);
			}
		}

		// Append the attributes block to the main packet.
		if (block.getBuffer().position() > 0) {
			out.writeBits(11, 2047);
			out.setAccessType(StreamBuffer.AccessType.BYTE_ACCESS);
			out.writeBytes(block.getBuffer());
		} else {
			out.setAccessType(StreamBuffer.AccessType.BYTE_ACCESS);
		}

		// Finish the packet and send it.
		out.finishVariableShortPacketHeader();
		player.send(out.getBuffer());
	}

	/**
	 * Appends the state of a player's chat to a buffer.
	 * 
	 * @param player
	 *            the player
	 * @param out
	 *            the buffer
	 */
	public static void appendChat(Player player, StreamBuffer.OutBuffer out) {
		out.writeShort(((player.getChatColor() & 0xff) << 8) + (player.getChatEffects() & 0xff), StreamBuffer.ByteOrder.LITTLE);
		out.writeByte(player.getStaffRights());
		out.writeByte(player.getChatText().length, StreamBuffer.ValueType.C);
		out.writeBytesReverse(player.getChatText());
	}

	/**
	 * Appends the state of a player's appearance to a buffer.
	 * 
	 * @param player
	 *            the player
	 * @param out
	 *            the buffer
	 */
	public static void appendAppearance(Player player, StreamBuffer.OutBuffer out) {
		StreamBuffer.OutBuffer block = StreamBuffer.newOutBuffer(128);

		block.writeByte(player.getGender()); // Gender
		block.writeByte(player.getPrayerIcon());
		block.writeByte(player.getSkullIcon());
		// block.writeByte(-1);
		if (player.transformNpc < 1) {
			// Hat.
			if (player.getEquipment().getItemContainer().isSlotUsed(Constants.HAT) && player.getEquipment().getItemContainer().get(Constants.HAT).validEquipment()) {
				block.writeShort(0x200 + player.getEquipment().getItemContainer().get(Constants.HAT).getId());
			} else {
				block.writeByte(0);
			}
			// Cape.
			if (player.getEquipment().getItemContainer().isSlotUsed(Constants.CAPE) && player.getEquipment().getItemContainer().get(Constants.CAPE).validEquipment()) {
				block.writeShort(0x200 + player.getEquipment().getItemContainer().get(Constants.CAPE).getId());
			} else {
				block.writeByte(0);
			}
			// Amulet.
			if (player.getEquipment().getItemContainer().isSlotUsed(Constants.AMULET) && player.getEquipment().getItemContainer().get(Constants.AMULET).validEquipment()) {
				block.writeShort(0x200 + player.getEquipment().getItemContainer().get(Constants.AMULET).getId());
			} else {
				block.writeByte(0);
			}
			// Weapon.
			if (player.getEquipment().getItemContainer().isSlotUsed(Constants.WEAPON) && !player.shouldHideWeapons() && player.getEquipment().getItemContainer().get(Constants.WEAPON).validEquipment()) {
				block.writeShort(0x200 + player.getEquipment().getItemContainer().get(Constants.WEAPON).getId());
			} else {
				block.writeByte(0);
			}
			// Chest.
			if (player.getEquipment().getItemContainer().isSlotUsed(Constants.CHEST) && player.getEquipment().getItemContainer().get(Constants.CHEST).validEquipment()) {
				block.writeShort(0x200 + player.getEquipment().getItemContainer().get(Constants.CHEST).getId());
			} else {
				block.writeShort(0x100 + player.getAppearance()[Constants.APPEARANCE_SLOT_CHEST]);
			}
			// Shield.
			if (player.getEquipment().getItemContainer().isSlotUsed(Constants.SHIELD) && !player.shouldHideWeapons() && player.getEquipment().getItemContainer().get(Constants.SHIELD).validEquipment()) {
				block.writeShort(0x200 + player.getEquipment().getItemContainer().get(Constants.SHIELD).getId());
			} else {
				block.writeByte(0);
			}
			// Arms.
			Item chest = player.getEquipment().getItemContainer().get(Constants.CHEST);
			if (chest == null || (chest != null && !Equipment.hideArms(chest.getId())) && chest.validEquipment()) {
				block.writeShort(0x100 + player.getAppearance()[Constants.APPEARANCE_SLOT_ARMS]);
			} else {
				block.writeShort(0x200 + chest.getId());
			}
			// Legs.
			if (player.getEquipment().getItemContainer().isSlotUsed(Constants.LEGS) && player.getEquipment().getItemContainer().get(Constants.LEGS).validEquipment()) {
				block.writeShort(0x200 + player.getEquipment().getItemContainer().get(Constants.LEGS).getId());
			} else {
				block.writeShort(0x100 + player.getAppearance()[Constants.APPEARANCE_SLOT_LEGS]);
			}
			// Hair
			Item helm = player.getEquipment().getItemContainer().get(Constants.HAT);
			if (helm == null || (helm != null && !Equipment.hideHair(helm.getId()) && !Equipment.hideHairAndBeard(helm.getId())) && helm.validEquipment()) {
				block.writeShort(0x100 + player.getAppearance()[Constants.APPEARANCE_SLOT_HEAD]);
			} else {
				block.writeByte(0);
			}
			// Hands.
			if (player.getEquipment().getItemContainer().isSlotUsed(Constants.HANDS) && player.getEquipment().getItemContainer().get(Constants.HANDS).validEquipment()) {
				block.writeShort(0x200 + player.getEquipment().getItemContainer().get(Constants.HANDS).getId());
			} else {
				block.writeShort(0x100 + player.getAppearance()[Constants.APPEARANCE_SLOT_HANDS]);
			}
			// Feet.
			if (player.getEquipment().getItemContainer().isSlotUsed(Constants.FEET) && player.getEquipment().getItemContainer().get(Constants.FEET).validEquipment()) {
				block.writeShort(0x200 + player.getEquipment().getItemContainer().get(Constants.FEET).getId());
			} else {
				block.writeShort(0x100 + player.getAppearance()[Constants.APPEARANCE_SLOT_FEET]);
			}
			// Beard.
			if (player.getGender() == Constants.GENDER_MALE && (helm == null || (helm != null && !Equipment.hideBeard(helm.getId()) && !Equipment.hideHairAndBeard(helm.getId())))) {
				block.writeShort(0x100 + player.getAppearance()[Constants.APPEARANCE_SLOT_BEARD]);
			} else {
				block.writeByte(0);
			}
		} else {
			block.writeShort(-1);
			block.writeShort(player.transformNpc);
		}

		// Player colors
		block.writeByte(player.getColors()[0]);
		block.writeByte(player.getColors()[1]);
		block.writeByte(player.getColors()[2]);
		block.writeByte(player.getColors()[3]);
		block.writeByte(player.getColors()[4]);
		// Movement animations
		// Item item =
		// player.getEquipment().getItemContainer().get(Constants.WEAPON);
		block.writeShort(player.getStandAnim()); // stand
		block.writeShort(player.getStandTurn());
		block.writeShort(player.getWalkAnim()); // walk
		block.writeShort(player.get180Turn());
		block.writeShort(player.get90TurnCW());
		block.writeShort(player.get90TurnCCW());

		block.writeShort(player.getRunAnim()); // run

		block.writeLong(player.getUsernameAsLong());
		block.writeByte(player.getCombatLevel()); // Combat level.
		block.writeShort(0);

		// Append the block length and the block to the packet.
		out.writeByte(block.getBuffer().position(), StreamBuffer.ValueType.C);
		out.writeBytes(block.getBuffer());
	}

	/**
	 * Adds a player to the local player list of another player.
	 * 
	 * @param out
	 *            the packet to write to
	 * @param player
	 *            the host player
	 * @param other
	 *            the player being added
	 */
	public static void addPlayer(StreamBuffer.OutBuffer out, Player player, Player other) {
		out.writeBits(11, other.getIndex()); // Server slot.
		out.writeBit(true); // Yes, an update is required.
		out.writeBit(true); // Discard walking queue(?)

		// Write the relative position.
		Position delta = Misc.delta(player.getPosition(), other.getPosition());
		out.writeBits(5, delta.getY());
		out.writeBits(5, delta.getX());
	}

	/**
	 * Updates movement for this local player. The difference between this
	 * method and the other player method is that this will make use of sector
	 * 2,3 to place the player in a specific position while sector 2,3 is not
	 * present in updating of other players (it simply flags local list removal
	 * instead).
	 * 
	 * @param player
	 * @param out
	 */
	public static void updateLocalPlayerMovement(Player player, StreamBuffer.OutBuffer out) {
		boolean updateRequired = player.getUpdateFlags().isUpdateRequired();
		if (player.needsPlacement()) { // Do they need placement?
			out.writeBit(true); // Yes, there is an update.
			int posX = player.getPosition().getLocalX(player.getCurrentRegion());
			int posY = player.getPosition().getLocalY(player.getCurrentRegion());
			appendPlacement(out, posX, posY, player.getPosition().getZ(), player.isResetMovementQueue(), updateRequired);
		} else { // No placement update, check for movement.
			int pDir = player.getPrimaryDirection();
			int sDir = player.getSecondaryDirection();
			if (pDir != -1) { // If they moved.
				out.writeBit(true); // Yes, there is an update.
				if (sDir != -1) { // If they ran.
					appendRun(out, pDir, sDir, updateRequired);
				} else { // Movement but no running - they walked.
					appendWalk(out, pDir, updateRequired);
				}
			} else { // No movement.
				if (updateRequired) { // Does the state need to be updated?
					out.writeBit(true); // Yes, there is an update.
					appendStand(out);
				} else { // No update whatsoever.
					out.writeBit(false);
				}
			}
		}
	}

	/**
	 * Updates the movement of a player for another player (does not make use of
	 * sector 2,3).
	 * 
	 * @param player
	 *            the player
	 * @param out
	 *            the packet
	 */
	public static void updateOtherPlayerMovement(Player player, StreamBuffer.OutBuffer out) {
		boolean updateRequired = player.getUpdateFlags().isUpdateRequired();
		int pDir = player.getPrimaryDirection();
		int sDir = player.getSecondaryDirection();
		if (pDir != -1) { // If they moved.
			out.writeBit(true); // Yes, there is an update.
			if (sDir != -1) { // If they ran.
				appendRun(out, pDir, sDir, updateRequired);
			} else { // Movement but no running - they walked.
				appendWalk(out, pDir, updateRequired);
			}
		} else { // No movement.
			if (updateRequired) { // Does the state need to be updated?
				out.writeBit(true); // Yes, there is an update.
				appendStand(out);
			} else { // No update whatsoever.
				out.writeBit(false);
			}
		}
	}

	/**
	 * Updates the state of a player.
	 * 
	 * @param player
	 *            the player
	 * @param block
	 *            the block
	 */
	public static void updateState(Player player, StreamBuffer.OutBuffer block, boolean forceAppearance, boolean noChat) {
		int mask = 0x0;
		if (player.getUpdateFlags().isForceMovementUpdateRequired()) {
			mask |= 0x400;
		}
		if (player.getUpdateFlags().isGraphicsUpdateRequired()) {
			mask |= 0x100;
		}
		if (player.getUpdateFlags().isAnimationUpdateRequired()) {
			mask |= 0x8;
		}
		if (player.getUpdateFlags().isForceChatUpdate()) {
			mask |= 0x4;
		}
		if (player.getUpdateFlags().isChatUpdateRequired() && !noChat) {
			mask |= 0x80;
		}
		if (player.getUpdateFlags().isEntityFaceUpdate()) {
			mask |= 0x1;
		}
		if (player.isAppearanceUpdateRequired() || forceAppearance) {
			mask |= 0x10;
		}
		if (player.getUpdateFlags().isFaceToDirection()) {
			mask |= 0x2;
		}
		if (player.getUpdateFlags().isHitUpdate()) {
			mask |= 0x20;
		}
		if (player.getUpdateFlags().isHitUpdate2()) {
			mask |= 0x200;
		}
		if (mask >= 0x100) {
			mask |= 0x40;
			block.writeShort(mask, StreamBuffer.ByteOrder.LITTLE);
		} else {
			block.writeByte(mask);
		}
		if (player.getUpdateFlags().isForceMovementUpdateRequired()) {
			block.writeByte(player.getUpdateFlags().getStartX(), StreamBuffer.ValueType.S);
			block.writeByte(player.getUpdateFlags().getStartY(), StreamBuffer.ValueType.S);
			block.writeByte(player.getUpdateFlags().getEndX(), StreamBuffer.ValueType.S);
			block.writeByte(player.getUpdateFlags().getEndY(), StreamBuffer.ValueType.S);
			// block.writeShort(player.getUpdateFlags().getSpeed1(),
			// StreamBuffer.ByteOrder.LITTLE);
			block.writeShort(player.getUpdateFlags().getSpeed1(), StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
			block.writeShort(player.getUpdateFlags().getSpeed2(), StreamBuffer.ValueType.A);
			block.writeByte(player.getUpdateFlags().getDirection(), StreamBuffer.ValueType.S);
		}
		if (player.getUpdateFlags().isGraphicsUpdateRequired()) {
			block.writeShort(player.getUpdateFlags().getGraphicsId(), StreamBuffer.ByteOrder.LITTLE);
			block.writeInt(player.getUpdateFlags().getGraphicsDelay());
		}
		if (player.getUpdateFlags().isAnimationUpdateRequired()) {
			block.writeShort(player.getUpdateFlags().getAnimationId(), StreamBuffer.ByteOrder.LITTLE);
			block.writeByte(player.getUpdateFlags().getAnimationDelay(), StreamBuffer.ValueType.C);
		}
		if (player.getUpdateFlags().isForceChatUpdate()) {
			block.writeString(player.getUpdateFlags().getForceChatMessage());
		}
		if (player.getUpdateFlags().isChatUpdateRequired() && !noChat) {
			appendChat(player, block);
		}
		if (player.getUpdateFlags().isEntityFaceUpdate()) {
			block.writeShort(player.getUpdateFlags().getEntityFaceIndex(), StreamBuffer.ByteOrder.LITTLE);
		}
		if (player.isAppearanceUpdateRequired() || forceAppearance) {
			appendAppearance(player, block);
		}
		if (player.getUpdateFlags().isFaceToDirection()) {
			block.writeShort(player.getUpdateFlags().getFace().getX() * 2 + 1, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
			block.writeShort(player.getUpdateFlags().getFace().getY() * 2 + 1, StreamBuffer.ByteOrder.LITTLE);
		}
		if (player.getUpdateFlags().isHitUpdate()) {
			block.writeByte(player.getUpdateFlags().getDamage());
			block.writeByte(player.getUpdateFlags().getHitType(), StreamBuffer.ValueType.A);
			block.writeByte(player.getSkill().getLevel()[Skill.HITPOINTS], StreamBuffer.ValueType.C);
			block.writeByte(player.getSkill().getLevelForXP((int) player.getSkill().getExp()[Skill.HITPOINTS]));
		}
		if (player.getUpdateFlags().isHitUpdate2()) {
			block.writeByte(player.getUpdateFlags().getDamage2());
			block.writeByte(player.getUpdateFlags().getHitType2(), StreamBuffer.ValueType.S);
			block.writeByte(player.getSkill().getLevel()[Skill.HITPOINTS]);
			block.writeByte(player.getSkill().getLevelForXP((int) player.getSkill().getExp()[Skill.HITPOINTS]), StreamBuffer.ValueType.C);
		}
	}

	/**
	 * Appends the stand version of the movement section of the update packet
	 * (sector 2,0). Appending this (instead of just a zero bit) automatically
	 * assumes that there is a required attribute update afterwards.
	 * 
	 * @param out
	 *            the buffer to append to
	 */
	public static void appendStand(StreamBuffer.OutBuffer out) {
		out.writeBits(2, 0); // 0 - no movement.
	}

	/**
	 * Appends the walk version of the movement section of the update packet
	 * (sector 2,1).
	 * 
	 * @param out
	 *            the buffer to append to
	 * @param direction
	 *            the walking direction
	 * @param attributesUpdate
	 *            whether or not a player attributes update is required
	 */
	public static void appendWalk(StreamBuffer.OutBuffer out, int direction, boolean attributesUpdate) {
		out.writeBits(2, 1); // 1 - walking.

		// Append the actual sector.
		out.writeBits(3, direction);
		out.writeBit(attributesUpdate);
	}

	/**
	 * Appends the walk version of the movement section of the update packet
	 * (sector 2,2).
	 * 
	 * @param out
	 *            the buffer to append to
	 * @param direction
	 *            the walking direction
	 * @param direction2
	 *            the running direction
	 * @param attributesUpdate
	 *            whether or not a player attributes update is required
	 */
	public static void appendRun(StreamBuffer.OutBuffer out, int direction, int direction2, boolean attributesUpdate) {
		out.writeBits(2, 2); // 2 - running.

		// Append the actual sector.
		out.writeBits(3, direction);
		out.writeBits(3, direction2);
		out.writeBit(attributesUpdate);
	}

	/**
	 * Appends the player placement version of the movement section of the
	 * update packet (sector 2,3). Note that by others this was previously
	 * called the "teleport update".
	 * 
	 * @param out
	 *            the buffer to append to
	 * @param localX
	 *            the local X coordinate
	 * @param localY
	 *            the local Y coordinate
	 * @param z
	 *            the Z coordinate
	 * @param discardMovementQueue
	 *            whether or not the client should discard the movement queue
	 * @param attributesUpdate
	 *            whether or not a plater attributes update is required
	 */
	public static void appendPlacement(StreamBuffer.OutBuffer out, int localX, int localY, int z, boolean discardMovementQueue, boolean attributesUpdate) {
		out.writeBits(2, 3); // 3 - placement.
		// Append the actual sector.
		out.writeBits(2, z);
		out.writeBit(discardMovementQueue);
		out.writeBit(attributesUpdate);
		out.writeBits(7, localY);
		out.writeBits(7, localX);
	}

}
