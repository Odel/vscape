package com.rs2.model.npcs;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.Following;
import com.rs2.model.content.combat.AttackType;
import com.rs2.model.content.combat.CombatScript;
import com.rs2.model.content.combat.util.RingEffect;
import com.rs2.model.content.dungeons.Abyss;
import com.rs2.model.content.quests.impl.HorrorFromTheDeep;
import com.rs2.model.content.quests.impl.MonkeyMadness.ApeAtollNpcs.ApeAtollNpcData;
import com.rs2.model.content.randomevents.EventsConstants;
import com.rs2.model.content.treasuretrails.ClueScroll;
import com.rs2.model.content.treasuretrails.KeyToClue;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.drop.NpcDropController;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;
import com.rs2.util.clip.ClippedPathFinder;

/**
 * A non-player-character.
 * 
 * @author blakeman8192
 */
public class Npc extends Entity {

	private int npcId;
	private int originalNpcId;
	private NpcDefinition definition;
	private Position minWalk = new Position(0, 0);
	private Position maxWalk = new Position(0, 0);
	private Position spawnPosition;
	private WalkType walkType = WalkType.STAND;
	private int currentX;
	private int currentY;
	private int hp;
	private int transformId;
	private int transformTimer;
	private int face;
	private boolean isVisible = true;
	private boolean transformUpdate;
	private boolean needsRespawn;
	private boolean realNpc;
	private boolean pet = false;
	private int ownerIndex;
	private int hpRenewalTimer;
	public boolean walkingBackToSpawn = false;

	private NpcCombatDef npcCombatDef;

	private static int AMOUNT_NPCS_ADDED = 0;

	public static int[] npcsTransformOnAggression = {1266, 1268, 2453, 2886, 2890, 1024, 1025, 1026, 1027, 1028, 1029};

	/**
	 * Creates a new Npc.
	 * 
	 * @param npcId
	 *            the NPC ID
	 */
	public Npc(int npcId) {
		NpcDefinition definition = World.getDefinitions()[npcId];
		setNpcId(npcId);
		setRealNpc(true);
		setOriginalNpcId(npcId);
		getUpdateFlags().setUpdateRequired(true);
		this.definition = definition == null ? NpcDefinition.produceDefinition(npcId) : definition;
		initAttributes();
		setAttackType(AttackTypes.MELEE);
		setUniqueId(AMOUNT_NPCS_ADDED++);
		this.npcCombatDef = NpcCombatDef.getDef(npcId);
		hp = getMaxHp();
	}

    /**
	 * Creates a new Npc.
	 *
	 * @param npcId
	 *            the NPC ID
	 */
	public Npc(NpcDefinition definition, int npcId, CombatScript npcCombatScript) {
		setNpcId(npcId);
		setRealNpc(true);
		setOriginalNpcId(npcId);
		getUpdateFlags().setUpdateRequired(true);
        this.definition = definition == null ? NpcDefinition.produceDefinition(npcId) : definition;
		initAttributes();
		setAttackType(AttackTypes.MELEE);
		setUniqueId(AMOUNT_NPCS_ADDED++);
		this.npcCombatDef = NpcCombatDef.getDef(npcId);
		hp = getMaxHp();
        setCombatScript(npcCombatScript);
	}

	@Override
	public void initAttributes() {
		getAttributes().put("doDamage", Boolean.FALSE);
		getAttributes().put("canTakeDamage", Boolean.TRUE);
	}

	@Override
	public void process() {
		handleTransformTick();
		expireHitRecords();
		npcRandomWalk();
		restoreHp();
        getFollowing().followTick();
        ownerCheck();
	}

	public void restoreHp() {
		if(getCurrentHp() < getMaxHp()) {
			if (hpRenewalTimer < 1 && !isDead()) {
				heal(1);
				hpRenewalTimer = 100;
			} else {
				hpRenewalTimer--;
			}
		}
	}

	public void teleportPlayer(final Player player, final int x, final int y, final int z, String shout) {
		player.getDialogue().endDialogue();
		player.getActionSender().removeInterfaces();
		getUpdateFlags().sendAnimation(1818);
		getUpdateFlags().sendGraphic(343);
		if (shout != null)
			getUpdateFlags().setForceChatMessage(shout);
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.getTeleportation().teleportObelisk(x, y, z);
				container.stop();
			}

			@Override
			public void stop() {
				player.setStopPacket(false);
			}
		}, 4);
	}

	public void teleportPlayerRunecraft(final Player player, final int x, final int y, final int z, String shout) {
		player.getActionSender().sendSound(1001, 0, 0);
		player.getDialogue().endDialogue();
		player.getActionSender().removeInterfaces();
		getUpdateFlags().sendAnimation(717);
		getUpdateFlags().sendGraphic(108);
		player.getActionSender().sendSound(1000, 0, 0);
		if (shout != null)
			getUpdateFlags().setForceChatMessage(shout);
		player.setStopPacket(true);
		player.getAttributes().put("canTakeDamage", Boolean.FALSE);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.getTeleportation().teleportRunecraft(x, y, z);
				container.stop();
			}
			@Override
			public void stop() {
				player.getActionSender().sendSound(1000, 0, 0);
			}
			
		}, 4);
	}

	public void sendPlayerAway(final Player player, int emoteId, int playerEmote, final int x, final int y, final int z, String shout, final boolean disappear) {
		player.getDialogue().endDialogue();
		player.getActionSender().removeInterfaces();
		getUpdateFlags().sendAnimation(emoteId);
		player.getUpdateFlags().sendAnimation(playerEmote);
		player.getActionSender().sendInterface(8677);
		final Npc npc = this;
		if (shout != null)
			getUpdateFlags().setForceChatMessage(shout);
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.teleport(new Position(x, y, z));
				player.getActionSender().removeInterfaces();
				player.getUpdateFlags().sendAnimation(65535, 0);
				if (disappear) {
					player.getActionSender().sendStillGraphic(EventsConstants.RANDOM_EVENT_GRAPHIC, player.getSpawnedNpc().getPosition(), 100 << 16);
					NpcLoader.destroyNpc(npc);
				}
				container.stop();
			}

			@Override
			public void stop() {
				player.setStopPacket(false);
			}
		}, 4);
	}

	@Override
	public void reset() {
		getUpdateFlags().sendAnimation(-1);
		getUpdateFlags().reset();
		transformUpdate = false;
		setPrimaryDirection(-1);
		getUpdateFlags().faceEntity(-1);
	}

	public void heal(int healAmount) {
		if (getCurrentHp() + healAmount >= getMaxHp()) {
			setCurrentHp(getMaxHp());
		} else {
			setCurrentHp(getCurrentHp() + healAmount);
		}
	}

	/**
	 * Makes walkable npcs walk, then updates it's position.
	 */
	public void npcRandomWalk() {
		if (this == null || !isVisible() || isAttacking() || isDead() || getFollowingEntity() != null || getInteractingEntity() != null || getCombatingEntity() != null || this.isMoving())
			return;
		if (isDontWalk() || ApeAtollNpcData.forNpcId(this.getNpcId()) != null) {
			return;
		}
		if(!playerNearby() && npcId != 1091)
		{
			return;
		}
		if (getWalkType() == WalkType.STAND) {
			getUpdateFlags().sendFaceToDirection(getFacingDirection(getPosition(), getFace()));
		} else if (!isFrozen() && !isStunned() && Misc.random(npcId == 1091 ? 5 : 9) == 0) {
			int x = minWalk.getX(), y = minWalk.getY(), width = maxWalk.getX()-minWalk.getX(), length = maxWalk.getY()-minWalk.getY();
			if(npcId == 1091) {
				x = getPosition().getX() - 8;
				y = getPosition().getY() - 8;
				width = 16;
				length = 16;
			}
			int x1 = Misc.getRandom().nextInt(width), y1 = Misc.getRandom().nextInt(length);
			Position position = new Position(x+x1, y+y1, getPosition().getZ());
			if(npcId == 1091) {
				this.setSpawnPosition(position);
			}
			if(npcId == 1091) {
				ClippedPathFinder.getPathFinder().findRoute(this, position.getX(), position.getY(), true, 0, 0);
			} else {
				walkTo(position, true);
			}
		}
	}
	
	public boolean canHaveInteractingEntity() {
		int id = this.npcId;
		return !this.isBoothBanker() && id != HorrorFromTheDeep.SITTING_JOSSIK && id != 1423 && id != 1424 && id != 1577
			&& id < 1066 && id > 1068 && id != 1063 && id != 1064 && id != 1061 && id != 1069;
	}

	public boolean playerNearby() {
        synchronized (World.getPlayers()) {
        	if(World.getPlayers().length <= 0)
        	{
        		return false;
        	}
        	final Player[] players = World.getPlayers();
            for (Player p : players) {
	            if (p == null)
	            	continue;
	            
	            if(isVisible() && getPosition().isViewableFrom(p.getPosition()) && getPosition().getZ() == p.getPosition().getZ()){
	            	return true;
	            }
	        }
        }
        return false;
	}

	public void ownerCheck() {
		if (getPlayerIndex() > 0) {
			if(this.getNpcId() == 1472) {
			    return;
			}
			if (!isDead() && (getPlayerOwner() == null || !Misc.goodDistance(getPosition(), getPlayerOwner().getPosition(), 15))) {
				NpcLoader.destroyNpc(this);
			}
			return;
		}
	}

	public static Position getFacingDirection(Position position, int facingType) {
		int x = position.getX();
		int y = position.getY();
		switch (facingType) {
			case 2 :
				return new Position(x, y + 1);
			case 3 :
				return new Position(x, y - 1);
			case 4 :
				return new Position(x + 1, y);
			case 5 :
				return new Position(x - 1, y);
		}
		return new Position(x, y - 1);
	}

	/**
	 * Adds to the NPCs position.
	 */
	public void appendNpcPosition(int xModifier, int yModifier) {
		currentX += xModifier;
		currentY += yModifier;
		getPosition().move(xModifier, yModifier);
	}

	public void sendTransform(int transformId, int transformTicks) {
		//NpcDefinition def = World.getDefinitions()[transformId];
		this.transformId = transformId;
		setTransformTimer(transformTicks);
		transformUpdate = true;
		setNpcId(transformId);
		getUpdateFlags().setUpdateRequired(true);
		this.definition = World.getDefinitions()[transformId];
		this.npcCombatDef = NpcCombatDef.getDef(transformId);
		hp = getMaxHp();
	}

	/**
	 * Sets the NPC ID.
	 * 
	 * @param npcId
	 *            the npcId
	 */
	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

	/**
	 * Gets the NPC ID.
	 * 
	 * @return the npcId
	 */
	public int getNpcId() {
		return npcId;
	}

	public int getOriginalNpcId() {
		return originalNpcId;
	}

	public void setOriginalNpcId(int originalNpcId) {
		this.originalNpcId = originalNpcId;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public int getPlayerIndex() {
		return ownerIndex;
	}

	public Player getPlayerOwner() {
		if(ownerIndex < 0 || ownerIndex > World.getPlayers().length)
		{
			return null;
		}
		return World.getPlayers()[ownerIndex];
	}

	public void setPlayerOwner(int playerIndex) {
		this.ownerIndex = playerIndex;
	}

	public void setMinWalk(Position minWalk) {
		this.minWalk = minWalk;
	}

	public Position getMinWalk() {
		return minWalk;
	}

	public void setMaxWalk(Position maxWalk) {
		this.maxWalk = maxWalk;
	}

	public Position getMaxWalk() {
		return maxWalk;
	}

	public void setWalkType(WalkType walkType) {
		this.walkType = walkType;
	}

	public WalkType getWalkType() {
		return walkType;
	}

    public void setTransformUpdate(boolean transformUpdate) {
		this.transformUpdate = transformUpdate;
	}

	public boolean isTransformUpdate() {
		return transformUpdate;
	}

	public void setTransformId(int transformId) {
		this.transformId = transformId;
	}

	public int getTransformId() {
		return transformId;
	}

	public int getRespawnTimer() {
		return npcCombatDef.getSpawnDelay();
	}

	public void setNeedsRespawn(boolean needsRespawn) {
		this.needsRespawn = needsRespawn;
	}

	public boolean needsRespawn() {
		return needsRespawn;
	}

	public void setSpawnPosition(Position spawnPosition) {
		this.spawnPosition = spawnPosition;
	}

	public Position getSpawnPosition() {
		return spawnPosition;
	}

	public void setCurrentX(int currentX) {
		this.currentX = currentX;
	}

	public int getCurrentX() {
		return currentX;
	}

	public void setCurrentY(int currentY) {
		this.currentY = currentY;
	}

	public int getCurrentY() {
		return currentY;
	}
	
	public boolean isPet() {
		return pet;
	}
	
	public void setAsPet(boolean set) {
		this.pet = set;
	}
	
	@Override
	public int getCurrentHp() {
		return hp;
	}

	@Override
	public int getMaxHp() {
		return definition.getHitpoints();
	}

	@Override
	public int getDeathAnimation() {
		return definition.getDeathAnim();
	}

	@Override
	public int getBlockAnimation() {
		return definition.getBlockAnim();
	}

	@Override
	public int getDeathAnimationLength() {
		return npcCombatDef.getDeathAnimationLength();
	}
	
	@Override
	public void setDeathAnimationLength(int length) {
	   npcCombatDef.setDeathAnimationLength(length);
	}

	@Override
	public int getBaseAttackLevel(AttackType attackType) {
		return (int) (definition.getCombat() / 2);
	}

	@Override
	public int getBaseDefenceLevel(AttackType attackType) {
		return (int) (definition.getCombat() / 2);
	}

	@Override
	public boolean isProtectingFromCombat(AttackType attackType, Entity attacker) {
		return false;
	}

    @Override
    public void teleport(Position position) {
    	setVisible(false);
        setPosition(position);
		getMovementHandler().reset();
		setVisible(true);
    }

    @Override
	public void setCurrentHp(int hp) {
		this.hp = hp;
	}


    public static void loadNpcDrops() {
    	for (int i = 0; i < Constants.MAX_NPC_ID; i++) {
    		NpcDropController drops = NpcDropController.forId(i);
    		if (drops != null) {
        		drops.setDrop();
    		}
    	}
    }

	@Override
	public void dropItems(Entity killer) {
	    NpcDropController drops = NpcDropController.forId(getNpcId());
	    if (killer != null && drops != null && !killer.inPestControlGameArea() && !killer.Area(2754, 2814, 3833, 3873)) {
		if (killer.isPlayer()) {
		    boolean bool = RingEffect.ringOfWealth((Player) killer);
		    drops.setRareTableChance(bool);
		}
		for (Item item : drops.getDrops()) {
		    if (item != null) {
				String itemName = item.getDefinition().getName().toLowerCase();
				if (killer.isPlayer() && (itemName.contains("clue") || ((Player) killer).hasPouchDrop(item.getId()))) {
				    return;
				}
				if(item.getCount() > 1 && !item.getDefinition().isNoted() && !item.getDefinition().isStackable())
				{
					for(int i = 0; i < item.getCount(); i++)
					{
						GroundItem drop = new GroundItem(new Item(item.getId(), 1), this, killer, getDeathPosition());
						GroundItemManager.getManager().dropItem(drop);
					}
				}else{
					GroundItem drop = new GroundItem(new Item(item.getId(), item.getCount() < 1 ? 1 : item.getCount()), this, killer, getDeathPosition());
					GroundItemManager.getManager().dropItem(drop);
				}
		    }
		}
		if (killer.isPlayer()) {
		    ((Player) killer).getActionSender().sendSound(376, 0, 0);
		    Abyss.dropPouches((Player) killer, this);
		    KeyToClue.dropKey((Player) killer, this);
		    ClueScroll.dropClue((Player) killer, this);
		    //WarriorsGuild.dropDefender((Player) killer, this);
		}
	    }
	}

	public NpcCombatDef getCombatDef() {
		return npcCombatDef;
	}

	public NpcDefinition getDefinition() {
		return definition;
    }

    public enum WalkType {
		STAND, WALK
	}

	public int getFace() {
		return face;
	}

	public void setFace(int face) {
		this.face = face;
	}

	public static Npc getNpcById(int npcId) {
		for (Npc npc : World.getNpcs()) {
			if (npc == null)
				continue;
			if (npc.getDefinition().getId() == npcId)
				return npc;
		}
		return null;
	}

	public static void reloadTransformedNpcs(Player player) {
		for (Npc npc : World.getNpcs()) {
			if (npc == null || !Misc.goodDistance(player.getPosition(), npc.getPosition(), 25) || player.getPosition().getZ() != npc.getPosition().getZ())
				continue;
			if (npc.getTransformTimer() > 0) {
				npc.getUpdateFlags().setUpdateRequired(true);
			}
		}
	}

	public int getTransformTimer() {
		return transformTimer;
	}

	public void setTransformTimer(int transformTimer) {
		this.transformTimer = transformTimer;
	}

	public void handleTransformTick() {
		if(getNpcId() != getOriginalNpcId())
		{
			if (getTransformTimer() > 0 && getTransformTimer() < 999999) {
				setTransformTimer(getTransformTimer() - 1);
				if (getTransformTimer() < 1) {
					sendTransform(getOriginalNpcId(), 0);
				}
			}
		}
	}

	public void setRealNpc(boolean realNpc) {
		this.realNpc = realNpc;
	}

	public boolean isRealNpc() {
		return realNpc;
	}

    public boolean checkWalk(int dirX, int dirY) {
    	if (!canMove(dirX, dirY)) {
    		return false;
    	}
    	/*if (walkIntoNpc(dirX, dirY)) {
    		return false;
    	}*/
    	return true;
    }

	public boolean walkIntoNpc(int dirX, int dirY) {
		for (Npc npc2 : World.getNpcs()) {
			if (npc2 != null && npc2 != this && !isDead() && npc2.getPosition().getZ() == getPosition().getZ() && !npc2.isPet()) {
				if (Misc.goodDistance(getPosition().getX(), getPosition().getY(), npc2.getPosition().getX(), npc2.getPosition().getY(), npc2.getSize() + getSize())) {
					if (npcInNpc(this, npc2, dirX, dirY)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean npcInNpc(Npc npc1, Npc npc2, int dirX, int dirY) {
		for (int x = npc1.getPosition().getX(); x < npc1.getPosition().getX() + npc1.getSize(); x++) {
			for (int y = npc1.getPosition().getY(); y < npc1.getPosition().getY() + npc1.getSize(); y++) {
				for (int x2 = npc2.getPosition().getX(); x2 < npc2.getPosition().getX() + npc2.getSize(); x2++) {
					for (int y2 = npc2.getPosition().getY(); y2 < npc2.getPosition().getY() + npc2.getSize(); y2++) {
						if (x2 == x + dirX && y2 == y + dirY) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public void resetActions() {
		getTask();
		setInteractingEntity(null);
		setCombatingEntity(null);
		setSkilling(null);
		getUpdateFlags().faceEntity(-1);
		Following.resetFollow(this);
	}

	public boolean getCorrectStandPosition(Position pos, int size) {
		int x = getPosition().getX();
		int y = getPosition().getY();
		int h = getPosition().getZ();
		switch(face) {
			case 2 :
				return pos.equals(new Position(x, y + size, h));
			case 3 :
				return pos.equals(new Position(x, y - size, h));
			case 4 :
				return pos.equals(new Position(x + size, y, h));
			case 5 :
				return pos.equals(new Position(x - size, y, h));
		}
		return Misc.goodDistance(getPosition(), pos, 1);
	}

	public Position getCorrectStandPosition(int size) {
		int x = getPosition().getX();
		int y = getPosition().getY();
		int h = getPosition().getZ();
		switch(face) {
			case 2 :
				return new Position(x, y + size, h);
			case 3 :
				return new Position(x, y - size, h);
			case 4 :
				return new Position(x + size, y, h);
			case 5 :
				return new Position(x - size, y, h);
		}
		return new Position(x, y + size, h);
	}

	public boolean isBoothBanker() {
		if (face == 1) {
			return false;
		}
		return getNpcId() == 166 || getNpcId() == 494 || getNpcId() == 495 || getNpcId() == 496 || getNpcId() == 499 || getNpcId() == 2619 || getNpcId() == 3046;
	}

	public static boolean isUndeadNpc(Entity victim) {
		if (victim.isPlayer()) {
			return false;
		}
		String name = ((Npc) victim).getDefinition().getName().toLowerCase();
		if (name.contains("spectre") || name.contains("banshee") || name.contains("shade") || name.contains("zombie") || name.contains("skeleton") || name.contains("ghost") || name.contains("crawling hand") || name.contains("skeletal hand") || name.contains("zombie hand") || name.contains("zogre") || name.contains("skorge") || name.contains("ankous")) {
			return true;
		}
		return false;
	}

}
