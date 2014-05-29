package com.rs2.model.content.combat.hit;

import java.util.List;

import com.rs2.model.Entity;
import com.rs2.model.Entity.AttackTypes;
import com.rs2.model.Graphic;
import com.rs2.model.UpdateFlags;
import com.rs2.model.content.combat.AttackType;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.combat.effect.Effect;
import com.rs2.model.content.combat.effect.impl.StatEffect;
import com.rs2.model.content.combat.effect.impl.StunEffect;
import com.rs2.model.content.combat.projectile.Projectile;
import com.rs2.model.content.combat.special.SpecialType;
import com.rs2.model.content.combat.util.RingEffect;
import com.rs2.model.content.combat.weapon.AttackStyle;
import com.rs2.model.content.minigames.pestcontrol.PestControl;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.prayer.Prayer;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;

/**
 *
 */
public class Hit {

	private HitDef hitDef;
	private int hitDelay, damage;
	private Entity attacker, victim;
	private boolean isDelayedDamaged, hit;

	public Hit(Entity attacker, Entity victim, HitDef hitDef) {
		this.attacker = attacker;
		this.victim = victim;
		this.hitDef = hitDef;
		this.damage = hitDef.getDamage();
	}

	public void tick() {
		hitDelay--;
	}

    public void initialize() {
        initialize(true);
    }

	public void initialize(boolean queue) {
		if (hitDef.shouldRandomizeDamage())
			this.damage = Misc.random(damage);
		if (attacker != null && !hitDef.isUnblockable() && hitDef.shouldCheckAccuracy()) {
			executeAccuracy();
		}
		if (hitDef.getProjectileDef() != null)
			new Projectile(attacker, victim, hitDef.getProjectileDef()).show();
		hitDelay = hitDef.calculateHitDelay(attacker != null ? attacker.getPosition() : null, victim.getPosition());
        if (queue)
            CombatManager.getManager().submit(this);

		if (hit && victim.isPlayer()) {
			RingEffect.ringOfRecoil(attacker, (Player) victim, damage);
		}
	}

	public void display() {
		if (!canDamageEnemy() || damage < 0)
			return;
		UpdateFlags flags = victim.getUpdateFlags();
		HitType hitType = damage == 0 ? HitType.MISS : hitDef.getHitType();
		if (!flags.isDamage1Set()) {
			flags.setHitUpdate(true);
			flags.setDamage(damage);
			flags.setHitType(hitType.toInteger());
		} else if (!flags.isDamage2Set()) {
			flags.setHitUpdate2(true);
			flags.setDamage2(damage);
			flags.setHitType2(hitType.toInteger());
		} else flags.queueDamage(damage, hitType.toInteger());
		
        if (getAttacker() != null && getAttacker().isPlayer() && getVictim().isNpc()){
       	 Player player = (Player) getAttacker();
       	 if(hitType == HitType.MISS){
       		 player.getCombatSounds().npcBlockSound(((Npc) getVictim()));
       	 }else{
       		 player.getCombatSounds().npcDamageSound(((Npc) getVictim()));
       	 }
	}
       if (getAttacker() != null && getAttacker().isPlayer() && getVictim() != null && getVictim().isPlayer()){
      	 Player att = (Player) getAttacker();
      	 Player vic = (Player) getVictim();
      	 if(hitType == HitType.MISS){
      		att.getCombatSounds().blockSound(vic);
      	 }else{
      		att.getCombatSounds().damageSound();
      	 }
       }
       if (getVictim().isPlayer()) {
           Player player = (Player) getVictim();
	       	 if(hitType == HitType.MISS){
	    		 player.getCombatSounds().blockSound(player);
	    	 }else{
	    		 player.getCombatSounds().damageSound();
	    	 }
       }
    }

    @SuppressWarnings("rawtypes")
    public void execute(List<Hit> recoilList) {
        if (hitDef.getDropItem() != null && attacker != null && attacker.isPlayer()) {
            Player player = (Player) attacker;
            if (player.isDropArrow() && Misc.getRandom().nextInt(10) < 6) {
            	/*if (player.inDuelArena()) {
            		player.getActionSender().sendMessage(""+hitDef.getDropItem().getId());
            		player.getDuelMainData().getAmmoUsed().add(new Item(hitDef.getDropItem().getId(), hitDef.getDropItem().getCount()));
            	} else {*/
                    GroundItem dropItem = new GroundItem(new Item(hitDef.getDropItem().getId(), hitDef.getDropItem().getCount()), player, victim.getPosition().clone());
                    GroundItemManager.getManager().dropItem(dropItem);
            	//}
            }
        }

        if (!canDamageEnemy())
            return;

        /*if (hitDef.getEffects().size() > 0) {
              if (damage > 0 || (hitDef.getAttackStyle() != null && hitDef.getAttackStyle().getAttackType() == AttackType.MELEE)) {
                  for (Effect effect : hitDef.getEffects())
                      effect.execute(this);
              }
          } */
        // retaliate
        if (attacker != null && !isDelayedDamaged && !hitDef.isUnblockable() && hitDef.getHitType() != HitType.POISON && hitDef.getHitType() != HitType.BURN) {
            if (victim.isNpc() && !victim.isDontAttack()) {
            	CombatManager.attack(victim, attacker);
            } else if (victim.isPlayer() && !victim.isMoving()) {
                Player player = (Player) victim;
                if (player.shouldAutoRetaliate()) {
                    CombatManager.attack(victim, attacker);
                }
            }
        }
        if (hitDef.doBlockAnim() && !victim.getUpdateFlags().isAnimationUpdateRequired()) {
        	if (hitDef.getVictimAnimation() > 0) {
        		 victim.getUpdateFlags().sendAnimation(hitDef.getVictimAnimation());
        	} else if (victim.isPlayer()) {
    			Player player = (Player) victim;
    			if (player.transformNpc > 1) {
    	            victim.getUpdateFlags().sendAnimation(new Npc(player.transformNpc).getDefinition().getBlockAnim());
    			} else {
    	            victim.getUpdateFlags().sendAnimation(victim.getBlockAnimation());
    			}
    		} else {
                victim.getUpdateFlags().sendAnimation(victim.getBlockAnimation());
    		}
        }
        if (victim.isPlayer()) {
            Player player = (Player) victim;
            player.getActionSender().removeInterfaces();
        }
        if (hitDef.getHitGraphic() != null)
            victim.getUpdateFlags().sendGraphic(hitDef.getHitGraphic());
        if (!hit && !hitDef.isUnblockable()) {
        	if (damage > 0) {
            	damage = 0;
        	}
        	if (hitDef.getAttackStyle() != null && hitDef.getAttackStyle().getAttackType() != AttackType.MAGIC) {
                display();
        	}
            return;
        }
        if (attacker != null && attacker.isPlayer() && victim.isNpc()){
             Player player = (Player) attacker;
             if (player.getSlayer().needToFinishOffMonster(((Npc)victim), true)) {
            	 if (damage >= victim.getCurrentHp()) {
            		 damage = victim.getCurrentHp() - 1;
            	 }
             }
        }
        if (!isDelayedDamaged) {
            if (hitDef.getAttackStyle() != null) {
            	if (hitDef.getAttackStyle().getMode() == AttackStyle.Mode.DRAGONFIRE || hitDef.getAttackStyle().getMode() == AttackStyle.Mode.DRAGONFIRE_FAR && victim.isPlayer()) {
            		Player player = (Player) victim;
					switch (player.antiFire()) {
						case 2 :
							damage = 0;
							break;
						case 1 :
							player.getActionSender().sendMessage("You manage to resist some of the dragonfire.");
							damage = Misc.random(hitDef.getAttackStyle().getMode() == AttackStyle.Mode.DRAGONFIRE_FAR ? 8 : 4);
							break;
						default :
							player.getActionSender().sendMessage("You are horribly burned by the dragonfire!");
							damage = 30 + Misc.random(20);
							break;
					}
					if (damage > 10 && hitDef.getAttackStyle().getMode() == AttackStyle.Mode.DRAGONFIRE && victim.isProtectingFromCombat(hitDef.getAttackStyle().getAttackType(), attacker)) {
            			player.getActionSender().sendMessage("Your prayers manage to resist some of the dragonfire.");
            			damage = Misc.random(10);
            		}
            	} else if (victim.isProtectingFromCombat(hitDef.getAttackStyle().getAttackType(), attacker) && hitDef.getSpecialEffect() != 11 ) {
                    if (attacker != null && attacker.isPlayer())
                        damage = (int) Math.ceil(damage * .6);
                    else
                        damage = 0;
                }
		else if (getAttacker() != null && getAttacker().isPlayer() && getVictim().isNpc() && ((Npc)victim).getNpcId() == 1158 ) {
		    if( hitDef.getAttackStyle().getAttackType() == AttackType.RANGED || hitDef.getAttackStyle().getAttackType() == AttackType.MAGIC)
			damage = 0;
		}
		else if (getAttacker() != null && getAttacker().isPlayer() && ((Player)attacker).getStaffRights() < 1 && getVictim().isNpc() && ((Npc)victim).getNpcId() == 1160 ) {
		    if( hitDef.getAttackStyle().getAttackType() == AttackType.MELEE)
			damage = 0;
		}
            }
        }
        SpecialType.doSpecEffect(attacker, victim, hitDef, damage);

        int currentHp = victim.getCurrentHp();

        if (damage > currentHp)
            damage = currentHp;

        if (hitDef.getEffects() != null && hitDef.getEffects().size() > 0) {
            for (Effect effect : hitDef.getEffects()) {
            	if (effect != null) {
                    effect.execute(this);
            	}
            }
        }

        //if (hitDef.getDamageDelay() == 0) {
        if (attacker != null && hitDef != null && attacker.isPlayer()) {
            addCombatExp((Player) attacker, damage, hitDef.getAttackStyle());
        }
        currentHp -= damage;
        
        if (hitDef.getHitAnimation() != -1)
            victim.getUpdateFlags().sendAnimation(hitDef.getHitAnimation());
        if (attacker != null) {
            HitRecord hitRecord = victim.getHitRecord(attacker);
            if (hitRecord == null)
                hitRecord = new HitRecord(attacker);
            else
                victim.getHitRecordQueue().remove(hitRecord);
            hitRecord.addDamage(damage);
            victim.getHitRecordQueue().add(hitRecord);
            if (attacker.isPlayer()) {
                Player player = (Player) attacker;
                if (player.getIsUsingPrayer()[Prayer.SMITE] && victim.isPlayer()) {
                    Prayer.applySmite(player, ((Player) victim), damage);
                }
            }
        }
        victim.setCurrentHp(currentHp);
        if (victim.isPlayer()) {
            Player player = (Player) victim;
            if (currentHp > 0) {
                int saveHp = (int) Math.ceil(victim.getMaxHp() * .1);
                if (currentHp < saveHp) {
                    if (player.getIsUsingPrayer()[Prayer.REDEMPTION]) {
                        Prayer.applyRedemption(player, victim, currentHp);
                    }
                    RingEffect.ringOfLife(player);
                }
            }
        }
        if(attacker != null && attacker.inPestControlGameArea() && victim != null && victim.inPestControlGameArea())
        {
    		PestControl.handleHit(attacker, victim, damage);
        }
        if (hitDef.getSpecialEffect() != 5) { // d spear
            display();
        }
        /*} else {
              Hit hit = new Hit(attacker, victim, new HitDef(hitDef.getAttackStyle(), hitDef.getHitType(), damage).setStartingHitDelay(hitDef.getDamageDelay() - 1));
              hit.isDelayedDamaged = true;
              hit.initialize();
          } */
    }

    public void addCombatExp(Player player, int damage, AttackStyle attackStyle) {
        if (attackStyle == null) {
            return;
        }
        final double totalExp = damage * 4d;
        final double expPerSkill = totalExp / attackStyle.getMode().getSkillsTrained().length;
        for (int i : attackStyle.getMode().getSkillsTrained())
            player.getSkill().addExp(i, expPerSkill);
        player.getSkill().addExp(Skill.HITPOINTS, (totalExp / 3d));
    }

    public boolean shouldExecute() {
        return hitDelay <= 0;
    }

    public boolean canDamageEnemy() {
        return (Boolean) victim.getAttributes().get("canTakeDamage");
    }

    public Entity getVictim() {
        return victim;
    }

    public Entity getAttacker() {
        return attacker;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    @SuppressWarnings("rawtypes")
    private void executeAccuracy() {
    	hit = true;
        if (attacker != null && Misc.random(4) == 0) { //barrows effect
        	if (((attacker.isPlayer() && ((Player) attacker).hasFullGuthan()) || (attacker.isNpc() && ((Npc) attacker).getDefinition().getId() == 2027))) {
	    		hitDef.setSpecialEffect(7);
        	} else if (((attacker.isPlayer() && ((Player) attacker).hasFullTorag()) || (attacker.isNpc() && ((Npc) attacker).getDefinition().getId() == 2029))) {
        		hitDef.setSpecialEffect(8);
        	} else if (((attacker.isPlayer() && ((Player) attacker).hasFullAhrim()) || (attacker.isNpc() && ((Npc) attacker).getDefinition().getId() == 2025))) {
        		hitDef.setSpecialEffect(9);
        	} else if (((attacker.isPlayer() && ((Player) attacker).hasFullKaril()) || (attacker.isNpc() && ((Npc) attacker).getDefinition().getId() == 2028))) {
        		hitDef.setSpecialEffect(10);
        	} else if (((attacker.isPlayer() && ((Player) attacker).hasFullVerac()) || (attacker.isNpc() && ((Npc) attacker).getDefinition().getId() == 2030))) {
        		hitDef.setSpecialEffect(11);
        	} else if (((attacker.isNpc() && ((Npc) attacker).getDefinition().getId() == 2026))) {
        		double hpLost = attacker.getMaxHp() - attacker.getCurrentHp();
        		this.damage += damage * hpLost * 0.01; 
        	} else if((attacker.isPlayer() && ((Player) attacker).hasFullVoidMage()) ) {
			((Player) attacker).setBonuses(3, 1000);
		}
        }
        if (getAttacker().isNpc() && getVictim().isPlayer()) { // slayer npc effects
            if (!((Player) getVictim()).getSlayer().hasSlayerRequirement((Npc) getAttacker())) {
                String name = ((Npc) getAttacker()).getDefinition().getName().toLowerCase();
                if (name.equalsIgnoreCase("banshee")) {
                	damage = 8;
                	hitDef.addEffects(new Effect[]{new StatEffect(Skill.ATTACK, 1), new StatEffect(Skill.STRENGTH, 1), new StatEffect(Skill.DEFENCE, 1), new StatEffect(Skill.RANGED, 1), new StatEffect(Skill.MAGIC, 1)});
                } else if (name.equalsIgnoreCase("cockatrice")) {
                	damage = 11;
                	hitDef.addEffects(new Effect[]{new StatEffect(Skill.ATTACK, 3), new StatEffect(Skill.STRENGTH, 3), new StatEffect(Skill.DEFENCE, 3), new StatEffect(Skill.RANGED, 3), new StatEffect(Skill.MAGIC, 3), new StatEffect(Skill.AGILITY, 3)});
                } else if (name.equalsIgnoreCase("basilik")) {
                	damage = 12;
                	hitDef.addEffects(new Effect[]{new StatEffect(Skill.ATTACK, 3), new StatEffect(Skill.STRENGTH, 3), new StatEffect(Skill.DEFENCE, 3), new StatEffect(Skill.RANGED, 3), new StatEffect(Skill.MAGIC, 3)});
                } else if (name.equalsIgnoreCase("wall beast")) {
                	damage = 18;
                	hitDef.addEffect(new StunEffect(5)).setVictimAnimation(734);
                } else if (name.equalsIgnoreCase("aberrant spectre")) {
                	damage = 14;
                	hitDef.addEffects(new Effect[]{new StatEffect(Skill.ATTACK, 5), new StatEffect(Skill.STRENGTH, 5), new StatEffect(Skill.DEFENCE, 5), new StatEffect(Skill.RANGED, 5), new StatEffect(Skill.MAGIC, 5)});
                } else if (name.equalsIgnoreCase("dust devil")) {
                	damage = 14;
                }
            }
        }
        if (getVictim().isPlayer() && ((Player) getVictim()).getNewComersSide().isInTutorialIslandStage()) {
            if (((Player) getVictim()).getSkill().getLevel()[Skill.HITPOINTS] == 1)
            	hit = false;
        }
        if (getAttacker().isPlayer() && getVictim().isNpc()) {
            if (!((Player) getAttacker()).getSlayer().hasSlayerRequirement((Npc) getVictim())) {
            	hit = false;
            }
        }
        if ((attacker.isPlayer() && ((Player) attacker).getNewComersSide().getTutorialIslandStage() == 63)) {
            ((Player) attacker).getNewComersSide().setTutorialIslandStage(((Player) attacker).getNewComersSide().getTutorialIslandStage() + 1, true);
            hit = false;
        }
        if (hit) {
            double defence = CombatManager.getDefenceRoll(victim, hitDef);
			double accuracy = CombatManager.getAttackRoll(attacker, hitDef);
            double chance = CombatManager.getChance(accuracy, defence);
            boolean accurate = CombatManager.isAccurateHit(chance);
            if (getAttacker().isPlayer() && ((Player) getAttacker()).isDebugCombat()) {
            	((Player) getAttacker()).getActionSender().sendMessage("Chance to hit: "+(int) (chance * 100)+"% (Rounded)");
            }
            if (getVictim().isPlayer() && ((Player) getVictim()).isDebugCombat() && getAttacker().isNpc()) {
            	((Player) getVictim()).getActionSender().sendMessage("Chance of npc hitting u: "+(int) (chance * 100)+"% (Rounded)");
            }
            hit = accurate;
        }
        if (!hit && !hitDef.isUnblockable()) {
            if (hitDef.getAttackStyle() != null && hitDef.getAttackStyle().getAttackType() == AttackType.MAGIC) {
                hitDef.setHitGraphic(new Graphic(85, 100));
            }
            hitDef.removeEffects();
        } else {
            if (hitDef.getEffects() != null && hitDef.getEffects().size() > 0) {
                for (Effect effect : hitDef.getEffects()) {
                    if (victim.canAddEffect(effect)) {
                        effect.initialize(this);
                    }
                }
            }
        }
    }

}
