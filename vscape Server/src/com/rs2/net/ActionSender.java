package com.rs2.net;

import com.rs2.Constants;
import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.Graphic;
import com.rs2.model.Palette;
import com.rs2.model.Palette.PaletteTile;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.skills.magic.Spell;
import com.rs2.model.content.skills.magic.SpellBook;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.Player.BankOptions;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.net.StreamBuffer.ValueType;
import com.rs2.util.Misc;
import com.rs2.util.NameUtil;

public class ActionSender {

	private Player player;

	public ActionSender(Player player) {
		this.player = player;
	}

	public ActionSender sendMapState(int state) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2);
		out.writeHeader(player.getEncryptor(), 99);
		out.writeByte(state);
		player.send(out.getBuffer());
		return this;
	}

	public void stopPlayerPacket(int ticks) {
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.setStopPacket(false);
				container.stop();
			}

			@Override
			public void stop() {
			}
		}, ticks);
	}

	public ActionSender sendUpdateServer(int timer) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(4);
		out.writeHeader(player.getEncryptor(), 114);
		out.writeShort(timer, StreamBuffer.ByteOrder.LITTLE);
		player.send(out.getBuffer());
		return this;
	}

	public void sendSideBarInterfaces() {
		int[] sidebars = { 2423, 3917, 638, 3213, 1644, 5608, 0, 25000, 5065,
				5715, 2449, 904, 147, 962 };
		for (int i = 0; i < sidebars.length; i++) {
			sendSidebarInterface(i, sidebars[i]);
			if (i == 6) {
				sendSidebarInterface(i, player.getMagicBookType() == SpellBook.MODERN ? 1151 : 12855);
			}
		}
	}

	public void enableSideBarInterfaces(int[] listSideBar) {
		int[] sidebars = { 2423, 3917, 638, 3213, 1644, 5608, 1151, 25000, 5065,
				5715, 2449, 904, 147, 962 };
		for (int i = 0; i < listSideBar.length; i++) {
			sendSidebarInterface(listSideBar[i], sidebars[listSideBar[i]]);

		}
	}

	public ActionSender sendLogin() {
		sendDetails();
		sendPacket107();
		sendMapRegion();
		// sendSideBarInterfaces();
		sendEnergy();
		player.getEquipment().updateWeight();
		/* farming */
		player.getAllotment().updateAllotmentsStates();
		player.getFlowers().updateFlowerStates();
		player.getHerbs().updateHerbsStates();
		player.getHops().updateHopsStates();
		player.getBushes().updateBushesStates();
		player.getAllotment().updateAllotmentsStates();
		player.getTrees().updateTreeStates();
		for (int i = 0; i < player.getTrees().getFarmingState().length; i++)
		{
			if(player.getTrees().getFarmingState()[i] == 7)
				player.getTrees().respawnStumpTimer(i);
		}
		player.getFruitTrees().updateFruitTreeStates();
		player.getSpecialPlantOne().updateSpecialPlants();
		player.getSpecialPlantTwo().updateSpecialPlants();
        for(int i = 0; i < player.getPendingItems().length; i++){
            player.getInventory().addItem(new Item(player.getPendingItems()[i], player.getPendingItemsAmount()[i]));
        }
		player.getPrivateMessaging().sendPMOnLogin();
		sendIgnoreList(player.getIgnores());
		//QPEdit(player.getQuestPoints());
		return this;
	}
	
	public ActionSender QPEdit(int qp){
		sendString("QP: @gre@"+qp+" ", 3985);
		sendString("Quest Points: "+qp, 640);
		return this;
	}

	public ActionSender sendConfigsOnLogin() {
		resetAutoCastInterface();
		sendConfig(18, player.getMusicAuto() ? 1 : 0);// music auto
		sendConfig(166, player.getScreenBrightness());// screenBrightness
		sendConfig(168, player.getMusicVolume());// musicVolume
		sendConfig(169, player.getEffectVolume());// effectVolume
		sendConfig(170, player.getMouseButtons());// mouseButtons
		sendConfig(171, player.getChatEffects());// chatEffects
		sendConfig(172, player.shouldAutoRetaliate() ? 1 : 0); // auto retaliate
		sendConfig(173, player.getMovementHandler().isRunToggled() ? 1 : 0); // runOption
		sendConfig(287, player.getSplitPrivateChat());// splitPrivateChat
		sendConfig(427, player.isAcceptingAid() ? 1 : 0);// acceptAid
		sendConfig(115, player.isWithdrawAsNote() ? 1 : 0); // withdrawItemAsNote
		sendConfig(304,player.getBankOptions().equals(BankOptions.SWAP_ITEM) ? 0 : 1);// swapItem
		return this;
	}

	public ActionSender openXInterface(int XInterfaceId) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(1);
		out.writeHeader(player.getEncryptor(), 27);
		player.setEnterXInterfaceId(XInterfaceId);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender statEdit(int stat, int edit, boolean hasLimit) {
		int lvl = player.getSkill().getPlayerLevel(stat) + edit;
		if (player.getStaffRights() > 1 && Constants.SERVER_DEBUG)
			System.out.println("current: " + player.getSkill().getPlayerLevel(stat) + " max: " + lvl);
		if (!hasLimit) {
			player.getSkill().getLevel()[stat] += edit;
			if (player.getSkill().getLevel()[stat] < 0) {
				player.getSkill().getLevel()[stat] = 0;
			}
			player.getSkill().refresh(stat);
			return this;
		}
		if (edit < 0) {
			if (player.getSkill().getLevel()[stat] < lvl) {
				return this;
			}
			if (player.getSkill().getLevel()[stat] + edit < lvl) {
				player.getSkill().getLevel()[stat] = lvl;
			} else {
				player.getSkill().getLevel()[stat] += edit;
			}
		} else {
			if (player.getSkill().getLevel()[stat] > lvl) {
				return this;
			}
			if (player.getSkill().getLevel()[stat] + edit > lvl) {
				player.getSkill().getLevel()[stat] = lvl;
			} else {
				player.getSkill().getLevel()[stat] += edit;
			}
		}
		player.getSkill().refresh(stat);
		return this;
	}
	public ActionSender walkToNoPacket(int x, int y, final boolean crossing) {
		if (player.isStunned() || player.isFrozen()) {
			return this;
		}
		if (crossing)
			player.isCrossingObstacle = true;
		player.getMovementHandler().reset();
		player.getMovementHandler().addToPath(
				new Position(player.getPosition().getX() + x, player
						.getPosition().getY() + y));
		player.getMovementHandler().finish();
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			boolean walked = false;

			@Override
			public void execute(CycleEventContainer b) {
				if (walked) { // && !player.isMoving
					b.stop();
				}
				walked = true;
			}

			@Override
			public void stop() {
				if (crossing)
					player.isCrossingObstacle = false;
			}
		}, 1);
		return this;
	}
	public ActionSender walkTo(int x, int y, final boolean crossing) {
		if (player.isStunned() || player.isFrozen()) {
			return this;
		}
		player.setStopPacket(true);
		if (crossing)
			player.isCrossingObstacle = true;
		player.getMovementHandler().reset();
		player.getMovementHandler().addToPath(
				new Position(player.getPosition().getX() + x, player
						.getPosition().getY() + y));
		player.getMovementHandler().finish();
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			boolean walked = false;

			@Override
			public void execute(CycleEventContainer b) {
				if (walked) { // && !player.isMoving
					player.setStopPacket(false);
					b.stop();
				}
				walked = true;
			}

			@Override
			public void stop() {
				if (crossing)
					player.isCrossingObstacle = false;
			}
		}, 1);
		return this;
	}
	
	public ActionSender walkTo2(int x, int y, int time, final boolean crossing) {
		time = time/2;
		if(time <= 1)
			time = 1;
		if (player.isStunned() || player.isFrozen()) {
			return this;
		}
		player.setStopPacket(true);
		if (crossing)
			player.isCrossingObstacle = true;
		player.getMovementHandler().reset();
		player.getMovementHandler().addToPath(new Position(x, y));
		player.getMovementHandler().finish();
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			boolean walked = false;

			@Override
			public void execute(CycleEventContainer b) {
				if (walked) { // && !player.isMoving
					player.setStopPacket(false);
					b.stop();
				}
				walked = true;
			}

			@Override
			public void stop() {
				if (crossing)
					player.isCrossingObstacle = false;
			}
		}, time);
		return this;
	}

	public ActionSender walkToNoForce(int x, int y) {
		if (player.isStunned() || player.isFrozen()) {
			return this;
		}
		player.getMovementHandler().reset();
		player.getMovementHandler().addToPath(
				new Position(player.getPosition().getX() + x, player
						.getPosition().getY() + y));
		player.getMovementHandler().finish();
		return this;
	}

	public ActionSender createStillGfx(int id, int x, int y, int height,
			int time) {
		for (Player players : World.getPlayers()) {
			if (players == null) {
				continue;
			}
            if (players.getPosition().getZ() != height)
                continue;
			if (Misc.goodDistance(x, y, players.getPosition().getX(), players
					.getPosition().getY(), 25)) {
				players.getActionSender().sendStillGraphic(id,
						new Position(x, y, height), time);
			}
		}
		return this;
	}

	public ActionSender sendSkill(int skillID, int level, double exp) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(player.getEncryptor(), 134);
		out.writeByte(skillID);
		out.writeInt((int) exp, StreamBuffer.ByteOrder.MIDDLE);
		out.writeByte(level);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendFrame230(int interfaceId, int rotation1,
			int rotation2, int zoom) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(10);
		out.writeHeader(player.getEncryptor(), 230);
		out.writeShort(zoom, StreamBuffer.ValueType.A);
		out.writeShort(interfaceId);
		out.writeShort(rotation1);
		out.writeShort(rotation2, StreamBuffer.ValueType.A,
				StreamBuffer.ByteOrder.LITTLE);
		player.send(out.getBuffer());
		return this;

	}

	public ActionSender sendFrame106(int id) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2);
		out.writeHeader(player.getEncryptor(), 106);
		out.writeByte(id, ValueType.C);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender moveInterface(int x, int y, int id) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(player.getEncryptor(), 70);
		out.writeShort(x);
		out.writeShort(y, StreamBuffer.ByteOrder.LITTLE);
		out.writeShort(id, StreamBuffer.ByteOrder.LITTLE);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender updateFlashingSideIcon(int tabId) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(player.getEncryptor(), 152);
		out.writeByte(tabId);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender createPlayerHints(int type, int id) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(player.getEncryptor(), 254);
		out.writeByte(type);
		out.writeShort(id);
		out.writeByte(0 >> 16);
		out.writeByte(0 >> 8);
		out.writeByte(0);
		player.send(out.getBuffer());
		player.setHintIndex(id);
		return this;
	}

	/**
	 * Orient : 2 Middle ? 3 West ? 4 East ? 5 South ? 6 North.
	 */
	public ActionSender createObjectHints(int x, int y, int height, int pos) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(player.getEncryptor(), 254);
		out.writeByte(pos);
		out.writeShort(x);
		out.writeShort(y);
		out.writeByte(height);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendUpdateItem(Item item, int slot, int column,
			int amount) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(32);
		out.writeVariableShortPacketHeader(player.getEncryptor(), 34);
		out.writeShort(column); // Column Across Smith Screen
		// out.writeByte(4); // Total Rows?
		out.writeByte(slot); // Row Down The Smith Screen
		out.writeShort(item.getId() + 1); // item
		out.writeByte(amount); // how many there are?
		out.finishVariableShortPacketHeader();
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendUpdateItem(int slot, int inventoryId, Item item) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(32);
		out.writeVariableShortPacketHeader(player.getEncryptor(), 34);
		out.writeShort(inventoryId);
		out.writeByte(slot);
        if(item.getId() == 0){
            out.writeShort(0);
            out.writeByte(0);
        } else {
            out.writeShort(item.getId() + 1);
            if (item.getCount() > 254) {
                out.writeByte(255);
                out.writeShort(item.getCount());
            } else {
                out.writeByte(item.getCount());
            }
        }
		out.finishVariableShortPacketHeader();
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendUpdateItems(int inventoryId, Item[] items) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2048);
		out.writeVariableShortPacketHeader(player.getEncryptor(), 53);
		out.writeShort(inventoryId);
		out.writeShort(items.length);
		for (Item item : items) {
			if (item != null) {
				if (item.getCount() > 254) {
					out.writeByte(255);
					out.writeInt(item.getCount(),
							StreamBuffer.ByteOrder.INVERSE_MIDDLE);
				} else {
					out.writeByte(item.getCount());
				}
				out.writeShort(item.getId() + 1, StreamBuffer.ValueType.A,
						StreamBuffer.ByteOrder.LITTLE);
			} else {
				out.writeByte(0);
				out.writeShort(0, StreamBuffer.ValueType.A,
						StreamBuffer.ByteOrder.LITTLE);
			}
		}
		out.finishVariableShortPacketHeader();
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendObjectType(int face, int type) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(player.getEncryptor(), 101);
		out.writeByte(((type << 2) + (face & 3)), StreamBuffer.ValueType.C);
		out.writeByte(0);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendObject(int id, int x, int y, int h, int face,
			int type) {
		sendCoords(new Position(x, y, h));
		sendObjectType(face, type);
		if (id != -1) {
			StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
			out.writeHeader(player.getEncryptor(), 151);
			out.writeByte(0, StreamBuffer.ValueType.S);
			out.writeShort(id, StreamBuffer.ByteOrder.LITTLE);
			out.writeByte(((type << 2) + (face & 3)), StreamBuffer.ValueType.S);
			player.send(out.getBuffer());
		}
		return this;
	}

	public ActionSender sendChatboxOverlay(int interfaceId) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(4);
		out.writeHeader(player.getEncryptor(), 218);
		out.writeShort(interfaceId, StreamBuffer.ValueType.A,
				StreamBuffer.ByteOrder.LITTLE);
		player.send(out.getBuffer());
		return this;
	}
	
	public ActionSender sendMessage(String message, boolean isFiltered) {
		if (player.getNewComersSide().isInTutorialIslandStage()) {
			player.getDialogue().sendStatement(message);
			player.setClickId(0);
		}
		StreamBuffer.OutBuffer out = StreamBuffer
				.newOutBuffer(message.length() + 4);
		out.writeVariablePacketHeader(player.getEncryptor(), 253);
		out.writeString(message);
		out.writeByte(isFiltered == true ? 1 : 0);
		out.finishVariablePacketHeader();
		player.send(out.getBuffer());
		return this;
	}

	public void sendMessage(String message) {
		sendMessage(message, false);
	}

	public void hideAllSideBars() {
		for (int i = 0; i < 14; i++)
			sendSidebarInterface(i, -1);
	}

	public ActionSender sendSidebarInterface(int menuId, int form) {
        player.setSideBarInterfaceId(menuId, form);
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(4);
		out.writeHeader(player.getEncryptor(), 71);
		out.writeShort(form);
		out.writeByte(menuId, StreamBuffer.ValueType.A);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendProjectile(Position position, int offsetX,
			int offsetY, int id, int startHeight, int endHeight, int speed,
			int lockon, boolean mage) {
		sendCoordinates2(position);
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(16);
		out.writeHeader(player.getEncryptor(), 117);
		out.writeByte(50);
		out.writeByte(offsetY);
		out.writeByte(offsetX);
		out.writeShort(lockon);
		out.writeShort(id);
		out.writeByte(startHeight);
		out.writeByte(endHeight);
		out.writeShort(mage ? 50 : 41);
		out.writeShort(speed);
		out.writeByte(16);
		out.writeByte(64);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendProjectile(Position start, int size, int lockOn,
			byte offsetX, byte offsetY, int projectileId, int delay,
			int duration, int startHeight, int endHeight, int curve) {
		if (size > 1) {
			sendCoordinates2(new Position(start.getX() + (size / 2), start.getY() + (size / 2)));
		} else {
			sendCoordinates2(start);
		}
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(16);
		out.writeHeader(player.getEncryptor(), 117);
		out.writeByte(50);
		out.writeByte(offsetX);
		out.writeByte(offsetY);
		out.writeShort(lockOn);
		out.writeShort(projectileId);
		out.writeByte(startHeight);
		out.writeByte(endHeight);
		out.writeShort(delay);
		out.writeShort(duration);
		out.writeByte(curve);
		out.writeByte(64);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendCoordinates2(Position position) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(player.getEncryptor(), 85);
		int y = position.getY() - player.getCurrentRegion().getRegionY() * 8
				- 2;
		int x = position.getX() - player.getCurrentRegion().getRegionX() * 8
				- 3;
		out.writeByte(y, StreamBuffer.ValueType.C);
		out.writeByte(x, StreamBuffer.ValueType.C);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendMapRegion() {
		player.getCurrentRegion().setAs(player.getPosition());
		player.calculateLoadedLandscape();
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 73);
		out.writeShort(player.getPosition().getRegionX() + 6,
				StreamBuffer.ValueType.A);
		out.writeShort(player.getPosition().getRegionY() + 6);
		player.send(out.getBuffer());
		return this;
	}
	
	public ActionSender sendCopyMapRegion(Palette palette) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2048);
		out.writeVariableShortPacketHeader(player.getEncryptor(), 241);
		out.writeShort(player.getPosition().getRegionY() + 6, StreamBuffer.ValueType.A);
		out.setAccessType(StreamBuffer.AccessType.BIT_ACCESS);
		for(int z = 0; z < 4; z++) {
			for(int x = 0; x < 13; x++) {
				for(int y = 0; y < 13; y++) {
					PaletteTile tile = palette.getTile(x, y, z);
					out.writeBits(1, tile != null ? 1 : 0);
					if(tile != null) {
						out.writeBits(26, tile.getX() << 14 | tile.getY() << 3 | tile.getZ() << 24 | tile.getRotation() << 1);
					}
				}
			}
		}
		out.setAccessType(StreamBuffer.AccessType.BYTE_ACCESS);
		out.writeShort(player.getPosition().getRegionX() + 6);
		out.finishVariableShortPacketHeader();
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendLogout() {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(1);
		out.writeHeader(player.getEncryptor(), 109);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendInterface(int id) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(player.getEncryptor(), 97);
		out.writeShort(id);
		player.send(out.getBuffer());
		player.setInterface(id);
		return this;
	}

	public ActionSender sendWalkableInterface(int id) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(player.getEncryptor(), 208);
		out.writeShort(id, StreamBuffer.ByteOrder.LITTLE);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendScrollInterface(int interfaceId, int scrollPosition) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(6);
		out.writeHeader(player.getEncryptor(), 79);
		out.writeShort(interfaceId, StreamBuffer.ByteOrder.LITTLE);
		out.writeShort(scrollPosition, StreamBuffer.ValueType.A);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendMultiInterface(boolean inMulti) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 61);
		out.writeByte(inMulti ? 1 : 0);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendInterface(int interfaceId, int inventoryId) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 248);
		out.writeShort(interfaceId, StreamBuffer.ValueType.A);
		out.writeShort(inventoryId);
		player.send(out.getBuffer());
        player.setInterface(interfaceId);
        player.setSideBarOpen(inventoryId);
		return this;
	}

	public ActionSender flashSideBarIcon(int barId) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(player.getEncryptor(), 24);
		out.writeByte(-barId, StreamBuffer.ValueType.A);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender removeInterfaces() {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2);
		out.writeHeader(player.getEncryptor(), 219);
		player.send(out.getBuffer());
		player.setInterface(0);
        player.setSideBarOpen(0);
		return this;
	}

	public ActionSender sendCoords(Position position) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(player.getEncryptor(), 85);
		int y = position.getY() - 8 * player.getCurrentRegion().getRegionY();
		int x = position.getX() - 8 * player.getCurrentRegion().getRegionX();
		out.writeByte(y, StreamBuffer.ValueType.C);
		out.writeByte(x, StreamBuffer.ValueType.C);
		player.send(out.getBuffer());
		return this;
	}

    public ActionSender sendGroundItem(com.rs2.model.ground.GroundItem groundItem) {
        sendCoords(groundItem.getPosition());
        StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(6);
        out.writeHeader(player.getEncryptor(), 44);
        out.writeShort(groundItem.getItem().getId(), StreamBuffer.ValueType.A,
                StreamBuffer.ByteOrder.LITTLE);
        out.writeShort(groundItem.getItem().getCount());
        out.writeByte(0);
        player.send(out.getBuffer());
        return this;
    }


    public ActionSender removeGroundItem(com.rs2.model.ground.GroundItem groundItem) {
        sendCoords(groundItem.getPosition());
        StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(4);
        out.writeHeader(player.getEncryptor(), 156);
        out.writeByte(0, StreamBuffer.ValueType.S);
        out.writeShort(groundItem.getItem().getId());
        player.send(out.getBuffer());
        return this;

    }

	public ActionSender sendConfig(int id, int value) {
		if (value >= -128 && value < 128) {
			StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(4);
			out.writeHeader(player.getEncryptor(), 36);
			out.writeShort(id, StreamBuffer.ByteOrder.LITTLE);
			out.writeByte(value);
			player.send(out.getBuffer());
		} else {
			StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
			out.writeHeader(player.getEncryptor(), 87);
			out.writeShort(id, StreamBuffer.ByteOrder.LITTLE);
			out.writeInt(value, StreamBuffer.ByteOrder.MIDDLE);
			player.send(out.getBuffer());
		}
		return this;
	}

	public ActionSender sendString(String message, int interfaceId) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(message.length() + 6);
		out.writeVariableShortPacketHeader(player.getEncryptor(), 126);
		out.writeString(message);
		out.writeShort(interfaceId, StreamBuffer.ValueType.A);
		out.finishVariableShortPacketHeader();
		player.send(out.getBuffer());
		return this;
	}
	
	public void sendQuestLogString(String message, int logIndex, int questIndex, int strikeStage) {
		if(player.getQuestStage(questIndex) < strikeStage) {
		    return;
		} else if(player.getQuestStage(questIndex) > strikeStage) {
		    message = "@str@" + message;
		}
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(message.length() + 6);
		out.writeVariableShortPacketHeader(player.getEncryptor(), 126);
		out.writeString(message);
		out.writeShort(logIndex + 8146, StreamBuffer.ValueType.A);
		out.finishVariableShortPacketHeader();
		player.send(out.getBuffer());
	}
	
	public void sendQuestLogString(String message, int logIndex) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(message.length() + 6);
		out.writeVariableShortPacketHeader(player.getEncryptor(), 126);
		out.writeString(message);
		out.writeShort(logIndex + 8146, StreamBuffer.ValueType.A);
		out.finishVariableShortPacketHeader();
		player.send(out.getBuffer());
	}

	public ActionSender sendFriendList(long name, int world) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(10);
		out.writeHeader(player.getEncryptor(), 50);
		if (world != 0) {
			world += 9;
		}
		out.writeLong(name);
		out.writeByte(world);
		player.send(out.getBuffer());
		return this;
	}
	
	public ActionSender sendIgnoreList(long[] ignores) {
		if(ignores.length > 0){
			StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2048);
			out.writeVariableShortPacketHeader(player.getEncryptor(), 214);
			for (long ignore : ignores) {
				if(ignore > 0)
					out.writeLong(ignore);
			}
			out.finishVariableShortPacketHeader();
			player.send(out.getBuffer());
		}
		return this;
	}

	public ActionSender sendPMServer(int state) { // IMPROVED && CONVERTED
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2);
		out.writeHeader(player.getEncryptor(), 221);
		out.writeByte(state);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendPrivateMessage(long name, int rights,
			byte[] message, int messageSize) {
		// TODO: FIXME
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2048);
		out.writeVariablePacketHeader(player.getEncryptor(), 196);
		out.writeLong(name);
		out.writeInt(player.getPrivateMessaging().getLastPrivateMessageId());
		out.writeByte(rights);
		out.writeBytes(message, messageSize);
		out.finishVariablePacketHeader();
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendItemOnInterface(int id, int zoom, int model) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(player.getEncryptor(), 246);
		out.writeShort(id == 0 ? -1 : id, StreamBuffer.ByteOrder.LITTLE);
		out.writeShort(zoom);
		out.writeShort(model);
		player.send(out.getBuffer());
		return this;
	}
    
    public ActionSender sendStillGraphic(Graphic graphic, Position position) {
        return sendStillGraphic(graphic.getId(), position, graphic.getValue());
    }

	public ActionSender sendStillGraphic(int graphicId, Position pos, int delay) {
		sendCoords(pos);
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(player.getEncryptor(), 4);
		out.writeByte(0);
		out.writeShort(graphicId);
		out.writeByte(pos.getZ());
		out.writeShort(delay);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendChatInterface(int id) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(player.getEncryptor(), 164);
		out.writeShort(id, StreamBuffer.ByteOrder.LITTLE);
		player.send(out.getBuffer());
        player.setInterface(id);
		return this;
	}
	
	public ActionSender sendGlobalChat(String prefix, String name, String message, int rights) {
		int packetLength = prefix.length() + name.length() + message.length();
		StreamBuffer.OutBuffer out = StreamBuffer
				.newOutBuffer(packetLength + 6);
		out.writeVariablePacketHeader(player.getEncryptor(), 217);
		out.writeString(prefix);
		out.writeString(name);
		out.writeString(message);
		out.writeByte(rights);
		out.finishVariablePacketHeader();
		player.send(out.getBuffer());
		return this;
	}
	
	public ActionSender sendClanChat(String clanname, String name, String message, int rights) {
		int packetLength = clanname.length() + name.length() + message.length();
		StreamBuffer.OutBuffer out = StreamBuffer
				.newOutBuffer(packetLength + 6);
		out.writeVariablePacketHeader(player.getEncryptor(), 216);
		out.writeString(clanname);
		out.writeString(name);
		out.writeString(message);
		out.writeByte(rights);
		out.finishVariablePacketHeader();
		player.send(out.getBuffer());
		return this;
	}
	

	public ActionSender sendInterfaceAnimation(int intefaceChildId, int animId) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 200);
		out.writeShort(intefaceChildId);
		out.writeShort(animId);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender animateObject(int objx, int objy, int objz,
			int animationID) {
		CacheObject object = ObjectLoader.object(objx, objy, objz);
		if (object == null)
			return this;
		sendCoords(new Position(objx, objy, objz));
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 160);
		out.writeByte(0, StreamBuffer.ValueType.S);
		out.writeByte((object.getType() << 2) + (object.getRotation() & 3),
				StreamBuffer.ValueType.S);
		out.writeShort(animationID, StreamBuffer.ValueType.A);
		player.send(out.getBuffer());
		return this;

	}
	
    public void animateObjectRadius(int X, int Y, int Z, int animationID) {
        for (Player p : World.getPlayers()) {
            if (p == null)
            	continue;
            
            if (p.getPosition().isViewableFrom(new Position(X,Y,Z)) && Z == p.getPosition().getZ()) {
            	p.getActionSender().animateObject(X,Y,Z,animationID);
            }
        }
    }
    
	public ActionSender animateObject2(int objx, int objy, int objz,
			int animationID) {
		GameObject object = ObjectHandler.getInstance().getObject(objx, objy, objz);
		if (object == null)
			return this;
		sendCoords(new Position(objx, objy, objz));
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 160);
		out.writeByte(0, StreamBuffer.ValueType.S);
		out.writeByte((object.getDef().getType() << 2) + (object.getDef().getFace() & 3),
				StreamBuffer.ValueType.S);
		out.writeShort(animationID, StreamBuffer.ValueType.A);
		player.send(out.getBuffer());
		return this;
	}
	
    public void animateObjectRadius2(int X, int Y, int Z, int animationID) {
        for (Player p : World.getPlayers()) {
            if (p == null)
            	continue;
            
            if (p.getPosition().isViewableFrom(new Position(X,Y,Z)) && Z == p.getPosition().getZ()) {
            	p.getActionSender().animateObject2(X,Y,Z,animationID);
            }
        }
    }
    
	public ActionSender sendPlayerDialogueHead(int interfaceId) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(player.getEncryptor(), 185);
		out.writeShort(interfaceId, StreamBuffer.ValueType.A,
				StreamBuffer.ByteOrder.LITTLE);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendNPCDialogueHead(int npcId, int interfaceId) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 75);
		out.writeShort(npcId, StreamBuffer.ValueType.A,
				StreamBuffer.ByteOrder.LITTLE);
		out.writeShort(interfaceId, StreamBuffer.ValueType.A,
				StreamBuffer.ByteOrder.LITTLE);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendSound(int id, int type, int delay) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(player.getEncryptor(), 174);
        out.writeShort(id);
		out.writeByte(type); out.writeShort(delay);
		player.send(out.getBuffer());
		return this;
	}
	
    public void sendSoundRadius(int id, int type, int delay, Position pos, int radius) {
        for (Player p : World.getPlayers()) {
            if (p == null)
            	continue;
            
            if (pos.isViewableFrom(p.getPosition()) && pos.getZ() == p.getPosition().getZ()) {
            	if(Misc.getDistance(pos, p.getPosition()) <= radius){
            		p.getActionSender().sendSound(id, type, delay);
            	}
            }
        }
    }

	public ActionSender sendSong(int id) {
		if (player.currentSong == id)
			return this;
		player.currentSong = id;
		if (id != -1) {
			StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
			out.writeHeader(player.getEncryptor(), 74);
			out.writeShort(id, StreamBuffer.ByteOrder.LITTLE);
			player.send(out.getBuffer());
		}
		return this;
	}

	public ActionSender sendQuickSong(int id, int songDelay) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 121);
		out.writeShort(id, StreamBuffer.ByteOrder.LITTLE);
		out.writeShort(songDelay, StreamBuffer.ByteOrder.LITTLE);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendPlayerOption(String option, int slot, boolean top) {
		StreamBuffer.OutBuffer out = StreamBuffer
				.newOutBuffer(option.length() + 5);
		out.writeVariablePacketHeader(player.getEncryptor(), 104);
		out.writeByte(slot, StreamBuffer.ValueType.C);
		out.writeByte(top ? 1 : 0, StreamBuffer.ValueType.A);
		out.writeString(option);
		out.finishVariablePacketHeader();
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendDetails() {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(4);
		out.writeHeader(player.getEncryptor(), 249);
		out.writeByte(1, StreamBuffer.ValueType.A);
		out.writeShort(player.getIndex(), StreamBuffer.ValueType.A,
				StreamBuffer.ByteOrder.LITTLE);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendEnergy() {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2);
		out.writeHeader(player.getEncryptor(), 110);
		out.writeByte((int) player.getEnergy());
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendWeight() {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(player.getEncryptor(), 240);
		out.writeShort((int) Math.floor(player.getWeight()));
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendPacket107() {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(1);
		out.writeHeader(player.getEncryptor(), 107);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendDuelEquipment(int itemId, int amount, int slot) {
		if (itemId != 0) {
			StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(32);
			out.writeHeader(player.getEncryptor(), 34);
			out.writeShort(13824);
			out.writeByte(slot);
			out.writeShort(itemId + 1);
			if (amount > 254) {
				out.writeByte(255);
				out.writeInt(amount, StreamBuffer.ValueType.STANDARD,
						StreamBuffer.ByteOrder.BIG);
			} else {
				out.writeByte(amount);

			}
			out.finishVariableShortPacketHeader();
			player.send(out.getBuffer());
			return this;
		}
		return this;

	}

	public void updateAutoCastInterface(Spell spell) {
		String spellName = NameUtil.uppercaseFirstLetter(spell.name()
				.toLowerCase().replaceAll("_", " "));
		sendString(spellName, 352);
		sendConfig(108, 3);
		sendConfig(43, 3);
	}

	public void resetAutoCastInterface() {
		if (player.getAutoSpell() == null) {
			sendConfig(108, 0);
			sendConfig(43, player.getFightMode());
			sendString("", 352);
			return;
		}
		sendConfig(43, player.getFightMode());
		sendConfig(108, 2);
	}

	public ActionSender sendInterfaceHidden(int boolstate, int interfaceId) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(4);
		out.writeHeader(player.getEncryptor(), 171);
		out.writeByte(boolstate);
		out.writeShort(interfaceId);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendSpecialBar(int mainFrame, int subFrame) {
		return this;
	}

	public ActionSender sendComponentInterface(int interfaceId, int modelId) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(6);
		out.writeHeader(player.getEncryptor(), 8);
		out.writeShort(interfaceId, StreamBuffer.ValueType.A,
				StreamBuffer.ByteOrder.LITTLE);
		out.writeShort(modelId);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender updateSpecialBar(int amount, int barId) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(player.getEncryptor(), 70);
		out.writeShort(amount);
		out.writeShort(0, StreamBuffer.ByteOrder.LITTLE);
		out.writeShort(barId, StreamBuffer.ByteOrder.LITTLE);
		player.send(out.getBuffer());
		return this;
	}

	public void updateSpecialBarText(int specialBarId) {
		if (player.isSpecialAttackActive()) {
			sendConfig(301, 1);
		} else {
			sendConfig(301, 0);
		}
	}

	public void updateSpecialAmount(int barId) {
		int specialCheck = 10;
		byte specialAmount = (byte) (player.getSpecialAmount() / 10);
		for (int i = 0; i < 10; i++) {
			updateSpecialBar(specialAmount >= specialCheck ? 500 : 0, --barId);
			specialCheck--;
		}
	}

	public void sendFullScreenInterface(int interfaceId, int secondVar) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(player.getEncryptor(), 69);
		out.writeShort(interfaceId);
		out.writeShort(secondVar);
		// player.send(out.getBuffer());
	}
	
    /**
     * @param direction The direction this data relates to:
     *  0: X-direction
     *  1: Z-direction
     *  2: Y-direction
     *  3: XY-Angle
     *  4: Z-Angle
     * @param magnitude 0 - 255; The magnitude. This is the difference between the average and maximum/minimum Magnitude.
     * Because of this, the magnitude is a measure for smoothness of the final shake. The lower, the smoother.
     * @param amplitude 0 - 255; The maximum amplitude of the average wave.
     * @param fourPiOverPeriod 0 - 255; The number which is a result of 4 pi over period.
     */
    public void shakeScreen(int direction, int magnitude, int amplitude, int fourPiOverPeriod){
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(6);
		out.writeHeader(player.getEncryptor(), 35);
		out.writeByte(direction);
		out.writeByte(magnitude);
		out.writeByte(amplitude);
		out.writeByte(fourPiOverPeriod);
        player.send(out.getBuffer());
    }

	
	public void stillCamera(int x, int y, int height, int speed, int angle) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(player.getEncryptor(), 177);
		out.writeByte(x / 64);
		out.writeByte(y / 64);
		out.writeShort(height);
		out.writeByte(speed);
		out.writeByte(angle);
		player.send(out.getBuffer());
	}

	public void spinCamera(int x, int y, int height, int speed, int angle) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(player.getEncryptor(), 166);
		out.writeByte(x);
		out.writeByte(y);
		out.writeShort(height);
		out.writeByte(speed);
		out.writeByte(angle);
		player.send(out.getBuffer());
	}

	public void resetCamera() {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(1);
		out.writeHeader(player.getEncryptor(), 107);
		player.send(out.getBuffer());
		player.getUpdateFlags().setUpdateRequired(true);
	}

	public void sendWelcomeScreen() {
		sendFullScreenInterface(5993, 15244);
		//sendString("Welcome to RuneScape", 15257);
		//sendString("", 15258);
		//sendString("Make sure to set up a bank pin.", 15270);
	}

	public void walkThroughTutIsGate(final int id1, final int id2,
			final int x1, final int y1, final int x2, final int y2, final int h) {
		final CacheObject g = ObjectLoader.object(id1, x1, y1, h);
		final CacheObject g2 = ObjectLoader.object(id2, x2, y2, h);
		final int z = player.getPosition().getZ();
		new GameObject(id1, x1 + 1, y1, z, g.getRotation() - 4, 0,
				Constants.EMPTY_OBJECT, 3, false);
		new GameObject(id2, x2 + 2, y2 + 1, z, g2.getRotation() - 1, 0,
				Constants.EMPTY_OBJECT, 3, false);
		new GameObject(Constants.EMPTY_OBJECT, x1, y1, z, g2.getRotation(), 0,
				id1, 3, false);
		new GameObject(Constants.EMPTY_OBJECT, x2, y2, z, g2.getRotation(), 0,
				id2, 3, false);
	}

	public void walkThroughDoor2(final int id1, final int id2, final int x1,
			final int y1, final int x2, final int y2, final int h) {
		final CacheObject g1 = ObjectLoader.object(id1, x1, y1, h);
		final CacheObject g2 = ObjectLoader.object(id2, x2, y2, h);
		final int z = player.getPosition().getZ();
		new GameObject(Constants.EMPTY_OBJECT, x1, y1, z, g1.getRotation(), 0,
				id1, 3, false);
		new GameObject(Constants.EMPTY_OBJECT, x2, y2, z, g2.getRotation() + 1,
				0, id2, 3, false);
		new GameObject(id1, x1 + 1, y1, z, g1.getRotation() + 1, 0,
				Constants.EMPTY_OBJECT, 3, false);
		new GameObject(id2, x2 + 1, y2, z, g2.getRotation(), 0,
				Constants.EMPTY_OBJECT, 3, false);
	}

	public void walkThroughDoor3(final int id1, final int id2, final int x1,
			final int y1, final int x2, final int y2, final int h) {
		final CacheObject g1 = ObjectLoader.object(id1, x1, y1, h);
		final CacheObject g2 = ObjectLoader.object(id2, x2, y2, h);
		final int z = player.getPosition().getZ();
		new GameObject(Constants.EMPTY_OBJECT, x1, y1, z, g1.getRotation(), 0,
				id1, 3, false);
		new GameObject(Constants.EMPTY_OBJECT, x2, y2, z, g2.getRotation(), 0,
				id2, 3, false);
		new GameObject(id1, x1 - 1, y1, z, g1.getRotation() + 3, 0,
				Constants.EMPTY_OBJECT, 3, false);
		new GameObject(id2, x2 - 1, y2, z, g2.getRotation() + 1, 0,
				Constants.EMPTY_OBJECT, 3, false);
	}

	public void walkThroughDoor2(final int id1, final int x1, final int y1,
			final int h) {
		final CacheObject g = ObjectLoader.object(id1, x1, y1, h);
		new GameObject(id1, x1, y1, h, g.getRotation() - 1, 0, id1, 2,
				g.getRotation() - 2, x1, y1, false);
	}

	public void walkThroughDoor3(final int id1, final int x1, final int y1,
			final int h) {
		final CacheObject g = ObjectLoader.object(id1, x1, y1, h);
		new GameObject(id1, x1, y1, h, g.getRotation() - 2, 0, id1, 3,
				g.getRotation() - 3, x1, y1, false);
	}

	public void walkThroughDoor(final int id1, final int x1, final int y1,
			final int h) {
		final CacheObject g = ObjectLoader.object(id1, x1, y1, h);
		new GameObject(id1, x1, y1, h, g.getRotation() - 1, 0, id1, 2,
				g.getRotation(), x1, y1, false);
	}
	public void walkThroughDoor(final int id1, final int x1, final int y1,
			final int h, final int type, final int face) {
		final CacheObject g = ObjectLoader.object(id1, x1, y1, h);
		new GameObject(id1, x1, y1, h, face, type, id1, 2,
				g.getRotation(), x1, y1, false);
	}

	public void walkThroughDoubleDoor(final int id1, final int id2,
			final int x1, final int y1, final int x2, final int y2, final int h) {
		final CacheObject g1 = ObjectLoader.object(id1, x1, y1, h);
		final CacheObject g2 = ObjectLoader.object(id2, x2, y2, h);
		new GameObject(id1, x1, y1, h, g1.getRotation() - 1, 0, id1, 1,
				g1.getRotation(), x1, y1, false);
		new GameObject(id2, x2, y2, h, g2.getRotation() + 1, 0, id2, 1,
				g2.getRotation(), x2, y2, false);
	}
	
	public void walkThroughDoubleDoor2(final int id1, final int id2,
			final int x1, final int y1, final int x2, final int y2, final int h, int xOff, int yOff) {
		final CacheObject g1 = ObjectLoader.object(id1, x1, y1, h);
		final CacheObject g2 = ObjectLoader.object(id2, x2, y2, h);
		new GameObject(Constants.EMPTY_OBJECT, x1, y1, h, g1.getRotation(), 0, id1, 1, false);
		new GameObject(Constants.EMPTY_OBJECT, x2, y2, h, g2.getRotation(), 0, id2, 1, false);
		new GameObject(id1, x1+xOff, y1+yOff, h, g1.getRotation() + 1, 0, Constants.EMPTY_OBJECT, 1,
				g1.getRotation(), x1, y1, false);
		new GameObject(id2, x2+xOff, y2+yOff, h, g2.getRotation() - 1, 0, Constants.EMPTY_OBJECT, 1,
				g2.getRotation(), x2, y2, false);
	}

}
