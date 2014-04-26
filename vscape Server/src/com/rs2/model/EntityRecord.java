package com.rs2.model;

public class EntityRecord {

	private int entityId;
	private boolean entityIsPlayer;
    private Entity entity;

	public EntityRecord(Entity entity) {
        this.entity = entity;
		this.entityId = entity.getUniqueId();
		this.entityIsPlayer = entity.isPlayer();
	}

	public Entity getEntity() {
        if (entity == null || entity.getIndex() == -1) {
            entity = null;
            Entity[] list = entityIsPlayer ? World.getPlayers() : World.getNpcs();
            for (Entity e: list) {
                if (e == null)
                    continue;
                if (e.getUniqueId() == entityId) {
                    entity = e;
                    break;
                }
            }
        }
        return entity;
	}
    
    @Override
    public boolean equals(Object o) {
        if (o == null || (!(o instanceof EntityRecord) && !(o instanceof Entity)))
            return false;
        if (o instanceof Entity) {
            Entity other = (Entity)o;
            if (entity == other)
                return true;
            else if (other.isPlayer() == entityIsPlayer && other.getUniqueId() == entityId) {
                entity = other;
                return true;
            } else return false;
        } else {
            EntityRecord other = (EntityRecord)o;
            return other.entityId == entityId && other.entityIsPlayer == entityIsPlayer;
        }
    }

}
