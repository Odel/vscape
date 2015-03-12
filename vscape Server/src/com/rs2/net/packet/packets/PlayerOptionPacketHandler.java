package com.rs2.net.packet.packets;

import com.rs2.Constants;
import com.rs2.model.World;
import com.rs2.model.content.Following;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.minigames.Snowball;
import com.rs2.model.content.skills.magic.MagicSkill;
import com.rs2.model.content.minigames.gnomeball.GnomeBall;
import com.rs2.model.content.quests.impl.ChristmasEvent.ChristmasEvent;
import com.rs2.model.content.skills.magic.Spell;
import com.rs2.model.content.skills.magic.SpellBook;
import com.rs2.model.players.Player;
import com.rs2.model.players.TradeManager;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.Tick;
import com.rs2.net.StreamBuffer;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;
import com.rs2.util.Misc;

public class PlayerOptionPacketHandler implements PacketHandler {

	public static final int TRADE = 73;
	public static final int FOLLOW = 153;
	public static final int ATTACK = 128;
	public static final int TRADE_ANSWER = 139;
	public static final int TRADE_ANSWER2 = 136;
	public static final int MAGIC_ON_PLAYER = 249;
	public static final int USE_ITEM_ON_PLAYER = 14;
	public static final int WHACK_PLAYER = 39;

	@Override
	public void handlePacket(Player player, Packet packet) {
		if (player.stopPlayerPacket()) {
			return;
		}
		player.getActionSender().removeInterfaces();
		player.resetAllActions();
		switch (packet.getOpcode()) {
		case TRADE :
		case TRADE_ANSWER2 :
		case TRADE_ANSWER :
			handleTrade(player, packet);
			break;
		case FOLLOW :
			handleFollow(player, packet);
			break;
		case ATTACK :
			handleAttack(player, packet);
			break;
		case MAGIC_ON_PLAYER :
			handleMagicOnPlayer(player, packet);
			break;
		case USE_ITEM_ON_PLAYER :
			useItemOnPlayers(player, packet);
			break;
		case WHACK_PLAYER :
		    if (player.getEquipment().getId(Constants.WEAPON) == ChristmasEvent.SNOWBALL_ITEM) {
		    	handlePeltSnowball(player, packet);
		    } else {
		    	handleRubberChicken(player, packet);
		    }
			break;
		}
	}

	private void useItemOnPlayers(final Player player, Packet packet) {
		int playerId = packet.getIn().readShort();
		if (playerId < 0 || playerId > World.getPlayers().length) {
			return;
		}
		final Player otherPlayer = World.getPlayers()[playerId];
		if (otherPlayer == null || !Misc.goodDistance(player.getPosition(), otherPlayer.getPosition(), 15)) {
			return;
		}
		final int itemSlot = packet.getIn().readShort(StreamBuffer.ByteOrder.LITTLE);
		final Item item = player.getInventory().getItemContainer().get(itemSlot);
		if(item == null) {
			return;
		}
		final int taskId = player.getTask();
		if (otherPlayer.getInterface() > 0) {
			player.getActionSender().sendMessage("This player is busy.");
			return;
		}
		//player.setClickId(otherPlayerId);
		player.setInteractingEntity(otherPlayer);
		if(item.getId() != 751 && item.getId() != 10501) {
		    player.setFollowDistance(1);
		    player.setFollowingEntity(otherPlayer);
		}
		World.submit(new Tick(1) {
			@Override
			public void execute() {
				if (otherPlayer == null || otherPlayer.isDead() || !player.checkTask(taskId)) {
					Following.resetFollow(player);
					player.setInteractingEntity(null);
					player.getMovementHandler().reset();
					this.stop();
					return;
				}
				if(item.getId() == 751) {
				    GnomeBall.throwGnomeBall(player, otherPlayer);
				    player.getMovementHandler().reset();
				    this.stop();
				    return;
				}
				if(item.getId() == 10501) {
				    Snowball.throwSnowball(player, otherPlayer);
				    player.getMovementHandler().reset();
				    this.stop();
				    return;
				}
				if (player.goodDistanceEntity(otherPlayer, 1) && !player.inEntity(otherPlayer) && !Following.standingDiagonal(player.getPosition(), otherPlayer.getPosition())) {
					switch(item.getId()) {
					case 962:
						player.getInventory().removeItem(item);
						player.getActionSender().sendMessage("You pull the cracker with " + otherPlayer.getUsername() + "...");
						otherPlayer.getActionSender().sendMessage(player.getUsername() + " pulls a Christmas cracker with you...");
						if(Misc.random(1) == 1) {
							player.getActionSender().sendMessage("  ... and get a partyhat! Merry Christmas!");
							otherPlayer.getActionSender().sendMessage("  ... and they get a partyhat! But have some coins anyways, Merry Christmas!");
							otherPlayer.getInventory().addItem(new Item(995, 5 + Misc.random(100)));
							player.getInventory().addItemToSlot(new Item(1038 + (Misc.random(5)*2)), itemSlot);
						} else {
							otherPlayer.getActionSender().sendMessage("  ... and you get a partyhat! Merry Christmas!");
							player.getActionSender().sendMessage("  ... and they get a partyhat! But have some coins anyways, Merry Christmas!");
							player.getInventory().addItemToSlot(new Item(995, 5 + Misc.random(100)), itemSlot);
							otherPlayer.getInventory().addItem(new Item(1038 + (Misc.random(5)*2)));
						}
						break;
					default:
						player.getActionSender().sendMessage("Nothing interesting happens.");
						break;
					}
					Following.resetFollow(player);
					player.getUpdateFlags().sendFaceToDirection(otherPlayer.getPosition());
					player.setInteractingEntity(null);
					player.getMovementHandler().reset();
					this.stop();
				}
			}
		});

	}

	private void handleTrade(final Player player, final Packet packet) {
		final int otherPlayerId = packet.getIn().readShort(true, StreamBuffer.ByteOrder.LITTLE);
		if (otherPlayerId < 0 || otherPlayerId > World.getPlayers().length) {
			return;
		}
		final Player otherPlayer = World.getPlayers()[otherPlayerId];
		if (otherPlayer == null || !Misc.goodDistance(player.getPosition(), otherPlayer.getPosition(), 15)) {
			return;
		}
		final int taskId = player.getTask();
		if(player.isIronman())
		{
			player.getActionSender().sendMessage("You cannot trade with other players as an ironman.", true);
			return;
		}
		if(otherPlayer.isIronman())
		{
			player.getActionSender().sendMessage("This player is an ironman and cannot trade.", true);
			return;
		}
		if (otherPlayer.getTradingEntity() == player) {
			TradeManager.declineTrade(player);
		} else if (otherPlayer.getInterface() > 0) {
			player.getActionSender().sendMessage("This player is busy.", true);
			return;
		}
		if (Constants.SYSTEM_UPDATE) {
			player.getActionSender().sendMessage("You can't trade during a system update.", true);
			return;
		}
		//player.setClickId(otherPlayerId);
		player.setInteractingEntity(otherPlayer);
		player.setFollowDistance(1);
		player.setFollowingEntity(otherPlayer);
		World.submit(new Tick(1) {
			@Override
			public void execute() {
				if (otherPlayer == null || otherPlayer.isDead() || !player.checkTask(taskId)) {
					Following.resetFollow(player);
					player.setInteractingEntity(null);
					player.getMovementHandler().reset();
					this.stop();
					return;
				}
				if (player.goodDistanceEntity(otherPlayer, 1) && !player.inEntity(otherPlayer) && !Following.standingDiagonal(player.getPosition(), otherPlayer.getPosition())) {
					TradeManager.handleTradeRequest(player, otherPlayer);
					Following.resetFollow(player);
					player.getUpdateFlags().sendFaceToDirection(otherPlayer.getPosition());
					player.setInteractingEntity(null);
					player.getMovementHandler().reset();
					this.stop();
				}
			}
		});

	}

	private void handleChallenge(final Player player, final Player otherPlayer, final int taskId) {
		if (Constants.DUELING_DISABLED) {
			player.getActionSender().sendMessage("This feature is currently disabled.");
			return;
		}
		if (otherPlayer.getInterface() > 0) {
			player.getActionSender().sendMessage("This player is busy.");
			return;
		}
		if (Constants.SYSTEM_UPDATE) {
			player.getActionSender().sendMessage("You can't duel during a system update.");
			return;
		}
		player.setInteractingEntity(otherPlayer);
		player.setFollowDistance(1);
		player.setFollowingEntity(otherPlayer);
		World.submit(new Tick(1) {
			@Override
			public void execute() {
				if (otherPlayer == null || otherPlayer.isDead() || !player.checkTask(taskId)) {
					Following.resetFollow(player);
					player.setInteractingEntity(null);
					player.getMovementHandler().reset();
					this.stop();
					return;
				}
				if (player.goodDistanceEntity(otherPlayer, 1) && !player.inEntity(otherPlayer) && !Following.standingDiagonal(player.getPosition(), otherPlayer.getPosition())) {
					if (player.isInDuelArea()) {
						player.getDuelInteraction().requestDuel(otherPlayer);
						player.getUpdateFlags().faceEntity(-1);
					}
					Following.resetFollow(player);
					player.getUpdateFlags().sendFaceToDirection(otherPlayer.getPosition());
					player.setInteractingEntity(null);
					player.getMovementHandler().reset();
					this.stop();
				}
			}
		});
	}

	private void handleRubberChicken(final Player player, Packet packet) {
		final int otherPlayerId = packet.getIn().readShort(true, StreamBuffer.ByteOrder.LITTLE);
		if(player.getEquipment().getId(Constants.WEAPON) != 4566)
		{
			return;
		}
		if (otherPlayerId < 0 || otherPlayerId > World.getPlayers().length) {
			return;
		}
		final Player otherPlayer = World.getPlayers()[otherPlayerId];
		if (otherPlayer == null || !Misc.goodDistance(player.getPosition(), otherPlayer.getPosition(), 15)) {
			return;
		}
		player.setInteractingEntity(otherPlayer);
		player.setFollowDistance(1);
		player.setFollowingEntity(otherPlayer);
		final int taskId = player.getTask();
		World.submit(new Tick(1) {
			@Override
			public void execute() {
				if (otherPlayer == null || otherPlayer.isDead() || !player.checkTask(taskId)) {
					Following.resetFollow(player);
					player.setInteractingEntity(null);
					player.getMovementHandler().reset();
					this.stop();
					return;
				}
				if (player.goodDistanceEntity(otherPlayer, 1) && !player.inEntity(otherPlayer)) {
					player.getUpdateFlags().sendAnimation(1833);
					Following.resetFollow(player);
					player.getUpdateFlags().sendFaceToDirection(otherPlayer.getPosition());
					player.setInteractingEntity(null);
					player.getMovementHandler().reset();
					this.stop();
				}
			}
		});
	}
	
	private void handlePeltSnowball(final Player player, Packet packet) {
		final int otherPlayerId = packet.getIn().readShort(true, StreamBuffer.ByteOrder.LITTLE);
		if (otherPlayerId < 0 || otherPlayerId > World.getPlayers().length) {
			return;
		}
		final Player otherPlayer = World.getPlayers()[otherPlayerId];
		if (otherPlayer == null || !Misc.goodDistance(player.getPosition(), otherPlayer.getPosition(), 15)) {
			return;
		}
		Snowball.throwSnowball(player, otherPlayer);
	}
	

	private void handleFollow(Player player, Packet packet) {
		int playerToFollow = packet.getIn().readShort(true, StreamBuffer.ByteOrder.LITTLE);
		if (playerToFollow < 0 || playerToFollow > World.getPlayers().length) {
			return;
		}
		Player leader = World.getPlayers()[playerToFollow];
		if (leader == null || !Misc.goodDistance(player.getPosition(), leader.getPosition(), 15)) {
			return;
		}
		player.getUpdateFlags().faceEntity(leader.getFaceIndex());
		player.setFollowDistance(1);
		player.setFollowingEntity(leader);
	}

	private void handleAttack(final Player player, Packet packet) {
		int otherPlayerId = packet.getIn().readShort();
		if (otherPlayerId < 0 || otherPlayerId > World.getPlayers().length) {
			return;
		}
		final Player otherPlayer = World.getPlayers()[otherPlayerId];
		if (otherPlayer == null || !Misc.goodDistance(player.getPosition(), otherPlayer.getPosition(), 15)) {
			return;
		}
		final int taskId = player.getTask();
		player.setCastedSpell(null);
		player.getUpdateFlags().faceEntity(otherPlayer.getFaceIndex());
		if (!player.inDuelArena() && !player.inWild() && !player.inCwGame()) {
			handleChallenge(player, otherPlayer, taskId);
		} else {
			CombatManager.attack(player, otherPlayer);
		}
	}

	private void handleMagicOnPlayer(Player player, Packet packet) {
		int id = packet.getIn().readShort(true, StreamBuffer.ValueType.A);
		if (id < 0 || id > World.getPlayers().length) {
			return;
		}
		Player otherPlayer = World.getPlayers()[id];
		if (otherPlayer == null || !Misc.goodDistance(player.getPosition(), otherPlayer.getPosition(), 15)) {
			return;
		}
		int magicId = packet.getIn().readShort(true, StreamBuffer.ByteOrder.LITTLE);
		Spell spell = SpellBook.getSpell(player, magicId);
		if (spell != null) {
			player.setCastedSpell(spell);
			if (spell == Spell.TELEOTHER_CAMELOT || spell == Spell.TELEOTHER_FALADOR || spell == Spell.TELEOTHER_LUMBRIDGE) {
				MagicSkill.spellOnPlayer(player, otherPlayer, spell);
			} else {
				CombatManager.attack(player, otherPlayer);
			}
		} else if (Constants.SERVER_DEBUG) {
			System.out.println("Magic ID: " + magicId);
		}
	}
}
