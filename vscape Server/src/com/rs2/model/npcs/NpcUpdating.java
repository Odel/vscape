package com.rs2.model.npcs;

import java.util.Iterator;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.players.Player;
import com.rs2.net.StreamBuffer;
import com.rs2.util.Misc;

/**
 * Deals with the NpcUpdating packet
 * 
 * @author BFMV
 * @author blakeman8192
 */
public class NpcUpdating {

	public static void update(Player player) {
		// XXX: The buffer sizes may need to be tuned.
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(8192);
		StreamBuffer.OutBuffer block = StreamBuffer.newOutBuffer(4096);

		// Initialize the update packet.
		out.writeVariableShortPacketHeader(player.getEncryptor(), 65);
		out.setAccessType(StreamBuffer.AccessType.BIT_ACCESS);

		// Update the NPCs in the local list.
		synchronized (player.getNpcs()) {
			out.writeBits(8, player.getNpcs().size());
			for (Iterator<Npc> i = player.getNpcs().iterator(); i.hasNext();) {
				Npc npc = i.next();
				if (npc.isVisible() && npc.getPosition().isViewableFrom(player.getPosition())) {
					updateNpcMovement(out, npc);
					if (npc.getUpdateFlags().isUpdateRequired()) {
						appendState(player, block, npc);
					}
				} else {
					out.writeBit(true);
					out.writeBits(2, 3);
					i.remove();
				}
			}
		}

		// Update the local NPC list itself.
		int npcsAdded = 0;
		for (int i = 0; i < World.getNpcs().length; i++) {
			if (npcsAdded > 15)
				// too many npcs added in cycle
				break;
			Npc npc = World.getNpcs()[i];
			if (npc == null || !npc.isVisible()) {
				continue;
			}
			synchronized (player.getNpcs()) {
				if (!player.getNpcs().contains(npc) && npc.getPosition().isViewableFrom(player.getPosition())) {
					npcsAdded++;
					player.getNpcs().add(npc);
					addNewNpc(out, player, npc);
					if (npc.getUpdateFlags().isUpdateRequired()) {
						appendState(player, block, npc);
					}
				}
			}
		}
		// Append the update block to the packet if need be.
		if (block.getBuffer().position() > 0) {
			out.writeBits(14, 16383);
			out.setAccessType(StreamBuffer.AccessType.BYTE_ACCESS);
			out.writeBytes(block.getBuffer());
		} else {
			out.setAccessType(StreamBuffer.AccessType.BYTE_ACCESS);
		}

		// Ship the packet out to the client.
		out.finishVariableShortPacketHeader();
		player.send(out.getBuffer());
	}

	public static void addNewNpc(StreamBuffer.OutBuffer out, Player player, Npc npc) {
		out.writeBits(14, npc.getIndex());
		Position delta = Misc.delta(player.getPosition(), npc.getPosition());
		out.writeBits(5, delta.getY());
		out.writeBits(5, delta.getX());
		out.writeBits(1, 0);
		if(npc.getNpcId() == 1577 && player.getQuestStage(38) > 6) {
			out.writeBits(14, 1576); //Vanstrom Clause -> Stranger
		} else {
			out.writeBits(14, npc.getNpcId());
		}
		out.writeBit(true);
	}

	public static void updateNpcMovement(StreamBuffer.OutBuffer out, Npc npc) {
		if (npc.getPrimaryDirection() == -1) {
			if (npc.getUpdateFlags().isUpdateRequired()) {
				out.writeBits(1, 1);
				out.writeBits(2, 0);
			} else {
				out.writeBits(1, 0);
			}
		} else {
			out.writeBits(1, 1);
			out.writeBits(2, 1);
			out.writeBits(3, npc.getPrimaryDirection());
			out.writeBit(true);
		}
	}

	public static void appendState(Player player, StreamBuffer.OutBuffer block, Npc npc) {
		int mask = 0x0;
		if (npc.getUpdateFlags().isAnimationUpdateRequired()) {
			mask |= 0x10;
		}
		if (npc.getUpdateFlags().isHitUpdate()) {
			mask |= 0x8;
		}
		if (npc.getUpdateFlags().isGraphicsUpdateRequired()) {
			mask |= 0x80;
		}
		if (npc.getUpdateFlags().isEntityFaceUpdate()) {
			mask |= 0x20;
		}
		if (npc.getUpdateFlags().isForceChatUpdate()) {
			mask |= 0x1;
		}
		if (npc.getUpdateFlags().isHitUpdate2()) {
			mask |= 0x40;
		}
		if (npc.isTransformUpdate()) {
			mask |= 0x2;
		}
		if (npc.getUpdateFlags().isFaceToDirection()) {
			mask |= 0x4;
		}
		block.writeByte(mask);
		if (npc.getUpdateFlags().isAnimationUpdateRequired()) {
			block.writeShort(npc.getUpdateFlags().getAnimationId(), StreamBuffer.ByteOrder.LITTLE);
			block.writeByte(npc.getUpdateFlags().getAnimationDelay());
		}
		if (npc.getUpdateFlags().isHitUpdate()) {
			int damage = npc.getUpdateFlags().getDamage();
			block.writeByte(damage, StreamBuffer.ValueType.A);
			block.writeByte(npc.getUpdateFlags().getHitType(), StreamBuffer.ValueType.C);
			block.writeByte(getCurrentHP(npc.getCurrentHp(), npc.getMaxHp(), 100), StreamBuffer.ValueType.A);
			block.writeByte(100);
		}
		if (npc.getUpdateFlags().isGraphicsUpdateRequired()) {
			block.writeShort(npc.getUpdateFlags().getGraphicsId());
			block.writeInt(npc.getUpdateFlags().getGraphicsDelay());
		}
		if (npc.getUpdateFlags().isEntityFaceUpdate()) {
			block.writeShort(npc.getUpdateFlags().getEntityFaceIndex());
		}
		if (npc.getUpdateFlags().isForceChatUpdate()) {
			block.writeString(npc.getUpdateFlags().getForceChatMessage());
		}
		if (npc.getUpdateFlags().isHitUpdate2()) {
			int damage = npc.getUpdateFlags().getDamage2();
			block.writeByte(damage, StreamBuffer.ValueType.C);
			block.writeByte(npc.getUpdateFlags().getHitType2(), StreamBuffer.ValueType.S);
			block.writeByte(getCurrentHP(npc.getCurrentHp(), npc.getMaxHp(), 100), StreamBuffer.ValueType.S);
			block.writeByte(100, StreamBuffer.ValueType.C);
		}
		if (npc.isTransformUpdate()) {
			if (npc.getTransformId() != -1) {
				block.writeShort(npc.getTransformId(), StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
			}
		}
		if (npc.getUpdateFlags().isFaceToDirection()) {
			Position pos = npc.getUpdateFlags().getFace();
			if (pos == null) {
				block.writeShort(0, StreamBuffer.ByteOrder.LITTLE);
				block.writeShort(0, StreamBuffer.ByteOrder.LITTLE);
			} else {
				block.writeShort(pos.getX() * 2 + 1, StreamBuffer.ByteOrder.LITTLE);
				block.writeShort(pos.getY() * 2 + 1, StreamBuffer.ByteOrder.LITTLE);
			}
		}
	}

	public static int getCurrentHP(int i, int i1, int i2) {
		double x = (double) i / (double) i1;
		return (int) Math.round(x * i2);
	}

}
