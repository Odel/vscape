package com.rs2.model;

import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;

public class UpdateFlags {

	private boolean isUpdateRequired;
	private boolean chatUpdateRequired;
	private boolean isForceChatUpdate;
	private String forceChatMessage;
	private boolean graphicsUpdateRequired;
	private int graphicsId;
	private int graphicsDelay;
	private boolean animationUpdateRequired;
	private int animationId;
	private int animationDelay;
	private boolean entityFaceUpdate;
	private int entityFaceIndex = -1;
	private boolean faceToDirection;
	private Position face;
	private boolean hitUpdate;
	private boolean hitUpdate2;
    private int queueDamage1 = -1, queueType1;
    private int queueDamage2 = -1, queueType2;
	private boolean forceMovementUpdateRequired;
	private int damage;
	private int damage2;
	private int hitType;
	private int hitType2;
	private int startX, startY, endX, endY, speed1, speed2, direction;
    private boolean damage1Set, damage2Set;
    private Entity owner;

    public UpdateFlags(Entity entity){
        this.owner = entity;
    }

	public void sendForceMovement(Player player, final int x, final int y, final int speed1, final int speed2, final int direction) {
		this.startX = player.getPosition().getLocalX();
		this.startY = player.getPosition().getLocalY();
		this.endX = player.getPosition().getLocalX() + x;
		this.endY = player.getPosition().getLocalY() + y;
		this.speed1 = speed1;
		this.speed2 = speed2;
		this.direction = direction;
		forceMovementUpdateRequired = true;
		isUpdateRequired = true;
	}

	public void sendForceMovementDoubleSwing(Player player, final int x, final int y, final int startY, final int speed1, final int speed2, final int direction) {
		this.startX = player.getPosition().getLocalX();
		this.startY = player.getPosition().getLocalY();
		this.endX = player.getPosition().getLocalX() + x;
		this.endY = player.getPosition().getLocalY() + y;
		this.speed1 = speed1;
		this.speed2 = speed2;
		this.direction = direction;
		forceMovementUpdateRequired = true;
		isUpdateRequired = true;
	}

	public void resetForceMovement() {
		startX = startY = endX = endY = speed1 = speed2 = direction = 0;
	}

	public void sendGraphic(Graphic graphic) {
		sendGraphic(graphic.getId(), graphic.getValue());
	}
	public void sendGraphic(int graphicsId) {
		this.graphicsId = graphicsId;
		this.graphicsDelay = 0;
		graphicsUpdateRequired = true;
		isUpdateRequired = true;
	}

	public void sendGraphic(int graphicsId, int graphicsDelay) {
        if(owner.isPlayer()){
        }
		this.graphicsId = graphicsId;
		this.graphicsDelay = graphicsDelay;
		graphicsUpdateRequired = true;
		isUpdateRequired = true;
	}

	public void sendHighGraphic(int graphicsId) {
        if(owner.isPlayer()){
        }
		this.graphicsId = graphicsId;
		this.graphicsDelay = 100 << 16 + 0;
		graphicsUpdateRequired = true;
		isUpdateRequired = true;
	}

	public void sendHighGraphic(int graphicsId, int graphicsDelay) {
		this.graphicsId = graphicsId;
		this.graphicsDelay = 100 << 16 + graphicsDelay;
		graphicsUpdateRequired = true;
		isUpdateRequired = true;
	}

	public void sendAnimation(int animationId) {
	    if (owner.isPlayer() && (((Player)owner).transformNpc == 1707 || ((Player)owner).transformNpc == 1708 || (((Player)owner).transformNpc >= 1480 && ((Player)owner).transformNpc <= 1487)) ) {
		return;
	    }
	    if (owner.isNpc() && animationId == 6969) {
		return;
	    }
		this.animationId = animationId;
		this.animationDelay = 0;
		animationUpdateRequired = true;
		isUpdateRequired = true;
	}

	public void sendAnimation(int animationId, int animationDelay) {
        if(owner.isPlayer()){
        }
	    if( owner.isPlayer() && (((Player)owner).transformNpc == 1707 || ((Player)owner).transformNpc == 1708) ) {
		return;
	    }
		this.animationId = animationId;
		this.animationDelay = animationDelay;
		animationUpdateRequired = true;
		isUpdateRequired = true;
	}

	public void faceEntity(int entityFaceIndex) {
		this.entityFaceIndex = entityFaceIndex;
		entityFaceUpdate = true;
		isUpdateRequired = true;
	}

	public void sendFaceToDirection(Position face) {
		this.face = face;
		faceToDirection = true;
		isUpdateRequired = true;
	}

	public void sendForceMessage(String forceChatMessage) {
		this.forceChatMessage = forceChatMessage;
		isForceChatUpdate = true;
		isUpdateRequired = true;
	}

	public void reset() {
		isForceChatUpdate = false;
		chatUpdateRequired = false;
		graphicsUpdateRequired = false;
		animationUpdateRequired = false;
		entityFaceUpdate = false;
		faceToDirection = false;
        damage = queueDamage1;
        damage2 = queueDamage2;
        hitType = queueType1;
        hitType2 = queueType2;
        queueDamage1 = -1;
        queueDamage2 = -1;
		hitUpdate = damage != -1;
		hitUpdate2 = damage2 != -1;
        damage1Set = false;
        damage2Set = false;
		forceMovementUpdateRequired = false;
	}

	public void setUpdateRequired(boolean isUpdateRequired) {
		this.isUpdateRequired = isUpdateRequired;
	}

	public boolean isUpdateRequired() {
		return isUpdateRequired;
	}

	public void setChatUpdateRequired(boolean chatUpdateRequired) {
		this.chatUpdateRequired = chatUpdateRequired;
	}

	public boolean isChatUpdateRequired() {
		return chatUpdateRequired;
	}

	public void setForceChatUpdate(boolean isForceChatUpdate) {
		this.isForceChatUpdate = isForceChatUpdate;
	}

	public boolean isForceChatUpdate() {
		return isForceChatUpdate;
	}

	public void setForceChatMessage(String forceChatMessage) {
		this.forceChatMessage = forceChatMessage;
		setForceChatUpdate(true);
	}

	public String getForceChatMessage() {
		return forceChatMessage;
	}

	public void setGraphicsUpdateRequired(boolean graphicsUpdateRequired) {
		this.graphicsUpdateRequired = graphicsUpdateRequired;
	}
    

	public boolean isGraphicsUpdateRequired() {
		return graphicsUpdateRequired;
	}

	public void setGraphicsId(int graphicsId) {
		this.graphicsId = graphicsId;
	}

	public int getGraphicsId() {
		return graphicsId;
	}

	public void setGraphicsDelay(int graphicsDelay) {
		this.graphicsDelay = graphicsDelay;
	}

	public int getGraphicsDelay() {
		return graphicsDelay;
	}

	public void setAnimationUpdateRequired(boolean animationUpdateRequired) {
		this.animationUpdateRequired = animationUpdateRequired;
	}

	public boolean isAnimationUpdateRequired() {
		return animationUpdateRequired;
	}

	public int getAnimationId() {
		return animationId;
	}

	public void setAnimationDelay(int animationDelay) {
		this.animationDelay = animationDelay;
	}

	public int getAnimationDelay() {
		return animationDelay;
	}

	public void setEntityFaceUpdate(boolean entityFaceUpdate) {
		this.entityFaceUpdate = entityFaceUpdate;
	}

	public boolean isEntityFaceUpdate() {
		return entityFaceUpdate;
	}

	public void setEntityFaceIndex(int entityFaceIndex) {
		this.entityFaceIndex = entityFaceIndex;
	}

	public int getEntityFaceIndex() {
		return entityFaceIndex;
	}

	public void setFaceToDirection(boolean faceToDirection) {
		this.faceToDirection = faceToDirection;
	}

	public boolean isFaceToDirection() {
		return faceToDirection;
	}

	public void setFace(Position face) {
		this.face = face;
	}

	public Position getFace() {
		return face;
	}

	public void setHitUpdate(boolean hitUpdate) {
		this.hitUpdate = hitUpdate;
	}

	public boolean isHitUpdate() {
		return hitUpdate;
	}

	public void setHitUpdate2(boolean hitUpdate2) {
		this.hitUpdate2 = hitUpdate2;
	}

	public boolean isHitUpdate2() {
		return hitUpdate2;
	}

	public void setDamage(int damage) {
		this.damage = damage;
        this.damage1Set = true;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage2(int damage2) {
		this.damage2 = damage2;   
        this.damage2Set = true;
	}

	public int getDamage2() {
		return damage2;
	}

	public void setHitType(int hitType) {
		this.hitType = hitType;
	}

	public int getHitType() {
		return hitType;
	}

	public void setHitType2(int hitType2) {
		this.hitType2 = hitType2;
	}

	public int getHitType2() {
		return hitType2;
	}

	public boolean isForceMovementUpdateRequired() {
		return forceMovementUpdateRequired;
	}

	public void setForceMovementUpdateRequired(boolean forceMovementUpdateRequired) {
		this.forceMovementUpdateRequired = forceMovementUpdateRequired;
	}

	public int getStartX() {
		return startX;
	}

	public int getStartY() {
		return startY;
	}

	public int getEndX() {
		return endX;
	}

	public int getEndY() {
		return endY;
	}

	public int getSpeed1() {
		return speed1;
	}

	public int getSpeed2() {
		return speed2;
	}

	public int getDirection() {
		return direction;
	}

    public boolean isDamage1Set() {
        return damage1Set;
    }

    public boolean isDamage2Set() {
        return damage2Set;
    }


    public void queueDamage(int damage, int hitType) {
        if (queueDamage1 == -1) {
            queueDamage1 = damage;
            queueType1 = hitType;
        }
        else if (queueDamage2 == -1) {
            queueDamage2 = damage;
            queueType2 = hitType;
        }
    }
}
