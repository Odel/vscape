package com.rs2.model.content.combat.hit;

import com.rs2.Constants;
import java.util.List;

import com.rs2.model.Entity;
import com.rs2.model.Graphic;
import com.rs2.model.UpdateFlags;
import com.rs2.model.World;
import com.rs2.model.content.combat.AttackType;
import com.rs2.model.content.combat.CombatCycleEvent;
import com.rs2.model.content.combat.CombatCycleEvent.CanAttackResponse;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.combat.attacks.SpellAttack;
import static com.rs2.model.content.combat.attacks.SpellAttack.getMultiAncients;
import com.rs2.model.content.combat.effect.Effect;
import com.rs2.model.content.combat.effect.EffectTick;
import com.rs2.model.content.combat.effect.impl.StatEffect;
import com.rs2.model.content.combat.effect.impl.StunEffect;
import com.rs2.model.content.combat.projectile.Projectile;
import com.rs2.model.content.combat.special.SpecialType;
import com.rs2.model.content.combat.util.RingEffect;
import com.rs2.model.content.combat.util.WeaponDegrading;
import com.rs2.model.content.combat.weapon.AttackStyle;
import com.rs2.model.content.minigames.fightcaves.FightCaves;
import com.rs2.model.content.minigames.pestcontrol.PestControl;
import com.rs2.model.content.quests.AnimalMagnetism;
import com.rs2.model.content.quests.DemonSlayer;
import com.rs2.model.content.quests.FamilyCrest;
import com.rs2.model.content.quests.GoblinDiplomacy;
import com.rs2.model.content.quests.HorrorFromTheDeep;
import com.rs2.model.content.quests.VampireSlayer;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.magic.Spell;
import com.rs2.model.content.skills.prayer.Prayer;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;
import java.util.LinkedList;
import java.util.Random;

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
		if (hitDef.shouldRandomizeDamage()) {
		    if(hitDef.isDarkBowSpec()) 
			this.damage = Misc.random(5, damage);
		    else if(hitDef.isDarkBowDragonSpec())
			this.damage = Misc.random(8, damage);
		    else
			this.damage = Misc.random(damage);
		}
		if (attacker != null && !hitDef.isUnblockable() && hitDef.shouldCheckAccuracy()) {
			executeAccuracy();
		}
		if(attacker != null && victim != null && victim.isNpc() && ((Npc)victim).getNpcId() == 2745 && attacker.isPlayer()) { //jad
		    Player player = (Player)attacker;
		    if(victim.getCurrentHp() < (victim.getMaxHp() / 2) && !player.hurkotsSpawned()) {
			FightCaves.spawnYtHurkots(player);
			player.setHurkotsSpawned(true);
		    }
		}
		if(attacker != null && victim != null && victim.isNpc() && ((Npc)victim).getNpcId() == 879) {
		    DemonSlayer.sendDelrithMessages();
		}
		if(attacker != null && victim != null && victim.isNpc() && attacker.isNpc()
			&& ((Npc)victim).getNpcId() == 4479 && ((Npc)attacker).getNpcId() == 4486) {
		    GoblinDiplomacy.sendGreenMessages((Npc)attacker);
		}
		if(victim != null && attacker != null && victim.isPlayer() && attacker.isNpc() && ((Npc)attacker).getNpcId() == 3200) {
		    int chance = Misc.random(2);
		    Player player = (Player)victim;
		    switch(chance) {
			case 0: //magic
			    if(player.isProtectingFromCombat(AttackType.MAGIC, attacker)) {
				damage = 0;
			    }
			case 1:
			    if(player.isProtectingFromCombat(AttackType.RANGED, attacker)) {
				damage = 0;
			    }
			case 2:
			    if(player.isProtectingFromCombat(AttackType.MELEE, attacker)) {
				damage = 0;
			}
		    }
		}
		if(attacker != null && victim != null && victim.isNpc() && attacker.isNpc()
			&& ((Npc)attacker).getNpcId() == 4479 && ((Npc)victim).getNpcId() == 4486) {
		    GoblinDiplomacy.sendRedMessages((Npc)attacker);
		}
		if(attacker != null && victim != null && attacker.isNpc() && victim.isPlayer()
			&& ((Player)victim).getInventory().playerHasItem(VampireSlayer.GARLIC) 
			&& ((Npc)attacker).getNpcId() == VampireSlayer.COUNT_DRAYNOR) {
		    damage = damage/6;
		}
		if(attacker != null && victim != null && attacker.isPlayer() 
		&& victim.isNpc() && ((Npc)victim).getNpcId() == FamilyCrest.CHRONOZON ) {
		    Player player = (Player)attacker;
		    if(hitDef.getHitGraphic() != null) {
			if(hitDef.getHitGraphic().getId() == 134) {
			    player.getActionSender().sendMessage("Chronozon weakens slightly.");
			    player.setHitChronozonWind(true);
			} else if(hitDef.getHitGraphic().getId() == 137) {
			    player.getActionSender().sendMessage("Chronozon weakens slightly.");
			    player.setHitChronozonWater(true);
			} else if(hitDef.getHitGraphic().getId() == 140) {
			    player.getActionSender().sendMessage("Chronozon weakens slightly.");
			    player.setHitChronozonEarth(true);
			} else if(hitDef.getHitGraphic().getId() == 131) {
			    player.getActionSender().sendMessage("Chronozon weakens slightly.");
			    player.setHitChronozonFire(true);
			}
		    }
		}
		if(attacker != null && victim != null && attacker.isPlayer()) {
		    Player player = (Player)attacker;
		    if(hitDef.getHitGraphic() != null && player.getEquipment().getId(Constants.HANDS) == FamilyCrest.CHAOS_GAUNTLETS) {
			if(hitDef.getHitGraphic().getId() == 119) {
			    damage += 4;
			} else if(hitDef.getHitGraphic().getId() == 122) {
			    damage += 4;
			} else if(hitDef.getHitGraphic().getId() == 125) {
			    damage += 4;
			} else if(hitDef.getHitGraphic().getId() == 128) {
			    damage += 4;
			}
		    }
		}
		if(attacker != null && victim != null && attacker.isPlayer() 
		&& victim.isNpc() && ((Npc)victim).getNpcId() >= 1351 && ((Npc)victim).getNpcId() < 1357 ) {
		    Player player = (Player)attacker;
		    switch(((Npc)victim).getTransformId()) {
			case HorrorFromTheDeep.WHITE_MOTHER:
			    if (hitDef.getAttackStyle().getAttackType() == AttackType.MAGIC && HorrorFromTheDeep.isAirSpell(hitDef.getHitGraphic().getId())) {
				break;
			    } else {
				damage = 0;
				break;
			    }
			case HorrorFromTheDeep.BLUE_MOTHER:
			    if (hitDef.getAttackStyle().getAttackType() == AttackType.MAGIC && HorrorFromTheDeep.isWaterSpell(hitDef.getHitGraphic().getId())) {
				break;
			    } else {
				damage = 0;
				break;
			    }
			case HorrorFromTheDeep.BROWN_MOTHER:
			    if (hitDef.getAttackStyle().getAttackType() == AttackType.MAGIC && HorrorFromTheDeep.isEarthSpell(hitDef.getHitGraphic().getId())) {
				break;
			    } else {
				damage = 0;
				break;
			    }
			case HorrorFromTheDeep.RED_MOTHER:
			    if (hitDef.getAttackStyle().getAttackType() == AttackType.MAGIC && HorrorFromTheDeep.isFireSpell(hitDef.getHitGraphic().getId())) {
				break;
			    } else {
				damage = 0;
				break;
			    }
			case HorrorFromTheDeep.GREEN_MOTHER:
			    if (hitDef.getAttackStyle().getAttackType() == AttackType.RANGED) {
				break;
			    } else {
				damage = 0;
				break;
			    }
			case HorrorFromTheDeep.ORANGE_MOTHER:
			    if (hitDef.getAttackStyle().getAttackType() == AttackType.MELEE) {
				break;
			    } else {
				damage = 0;
				break;
			    }
		    }
		}
		if(attacker != null && victim != null && attacker.isPlayer() && victim.isNpc()) {
		    Player player = (Player)attacker;
		    Npc npc = (Npc)victim;
		    if(player.getEquipment().getId(Constants.AMULET) == 11128 && player.getEquipment().getItemContainer().get(Constants.WEAPON) != null) {
			int id = player.getEquipment().getId(Constants.WEAPON);
			if(id >= 6522 && id < 6529) {
			    damage += damage/4;
			}
		    }
		}
		if(attacker != null && attacker.isNpc()) {
		    Npc npc = (Npc)attacker;
		    if(npc.getNpcId() >= 1338 && npc.getNpcId() < 1344) { //weak dagannoths under lighthouse
			if(80 >= (new Random().nextDouble() * 100.0)) {
			    damage = 0;
			}
		    }
		}
		if(attacker != null && victim != null && victim.isNpc() && ((Npc)victim).getNpcId() != 2745) {
		    FightCaves.handlePlayerHit(attacker, (Npc)victim, damage);
		}
		if (attacker != null && victim != null && attacker.isPlayer() && hitDef.getAttackStyle() != null && hitDef.getAttackStyle().getAttackType() == AttackType.MAGIC && getMultiAncients(hitDef.getHitGraphic() == null ? new Graphic(0, 0) : hitDef.getHitGraphic())  && attacker.inMulti() && victim.inMulti()) {
		    final Spell spell = SpellAttack.getMultiAncientSpellForGfx(hitDef.getHitGraphic());
		    for (final Npc npcs : World.getNpcs()) {
			if (npcs == null || npcs.getNpcId() == 3782) {
			    continue;
			}
			if (getVictim().isNpc() && ((Npc) getVictim()) != npcs) {
			    CombatCycleEvent.CanAttackResponse canAttackResponse = CombatCycleEvent.canAttack(getAttacker(), npcs);
			    final HitDef hitDefMulti = hitDef.clone().randomizeDamage();
			    if (getVictim().goodDistanceEntity(npcs, 1) && canAttackResponse == CombatCycleEvent.CanAttackResponse.SUCCESS) {
				CycleEventHandler.getInstance().addEvent((Player) attacker, new CycleEvent() {
				    @Override
				    public void execute(CycleEventContainer b) {
					Hit hit = new Hit(getAttacker(), npcs, hitDefMulti);
					List hitList = new LinkedList();
					hitList.add(hit);
					if (spell.getRequiredEffect() != null) {
					    EffectTick t = spell.getRequiredEffect().generateTick(attacker, npcs);
					    if (t != null) {
						npcs.addEffect(t);
						World.getTickManager().submit(t);
					    }
					    spell.getRequiredEffect().onInit(hit, t);
					}

					if (spell.getAdditionalEffect() != null) {
					    EffectTick t2 = spell.getAdditionalEffect().generateTick(attacker, npcs);
					    if (t2 != null) {
						npcs.addEffect(t2);
						World.getTickManager().submit(t2);
					    }
					    spell.getAdditionalEffect().onInit(hit, t2);
					}
					hit.execute(hitList);
					npcs.hit(Misc.random(hitDefMulti.getDamage()), HitType.NORMAL);
					hitList.clear();
					b.stop();
				    }

				    @Override
				    public void stop() {
				    }
				}, spell.getHitDef().calculateHitDelay(attacker.getPosition(), victim.getPosition()));
			    }
			}
		    }
		    if (attacker.inWild() && attacker.isPlayer() && CombatCycleEvent.canAttack(attacker, victim) != CanAttackResponse.WILD_LEVEL) {
			for (final Player players : World.getPlayers()) {
			    if (players == null) {
				continue;
			    }
			    if (players == (Player)attacker) {
				continue;
			    }
			    if (players != getVictim() && players != getAttacker().getCombatingEntity()) {
				CombatCycleEvent.CanAttackResponse canAttackResponse = CombatCycleEvent.canAttack(getAttacker(), players);
				final HitDef hitDefMulti = hitDef.clone().randomizeDamage().addEffects(new Effect[]{spell.getRequiredEffect(), spell.getAdditionalEffect()});
				if (getVictim().goodDistanceEntity(players, 1) && canAttackResponse == CombatCycleEvent.CanAttackResponse.SUCCESS) {
				    CycleEventHandler.getInstance().addEvent((Player) attacker, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
					    Hit hit = new Hit(getAttacker(), players, hitDefMulti);
					    List hitList = new LinkedList();
					    hitList.add(hit);
					    if (spell.getRequiredEffect() != null) {
						EffectTick t = spell.getRequiredEffect().generateTick(attacker, players);
						if (t != null) {
						    players.addEffect(t);
						    World.getTickManager().submit(t);
						}
						spell.getRequiredEffect().onInit(hit, t);
					    }

					    if (spell.getAdditionalEffect() != null) {
						EffectTick t2 = spell.getAdditionalEffect().generateTick(attacker, players);
						if (t2 != null) {
						    players.addEffect(t2);
						    World.getTickManager().submit(t2);
						}
						spell.getAdditionalEffect().onInit(hit, t2);
					    }
					    hit.execute(hitList);
					    players.hit(Misc.random(hitDefMulti.getDamage()), HitType.NORMAL);
					    hitList.clear();
					    b.stop();
					}

					@Override
					public void stop() {
					}
				    }, spell.getHitDef().calculateHitDelay(attacker.getPosition(), victim.getPosition()));
				}
			    }
			}
		    }
		}
		if(Constants.DEGRADING_ENABLED) {
		    if(attacker != null && attacker.isPlayer() && victim != null && victim.isNpc()) {
			WeaponDegrading.handlePlayerHit((Player)attacker);
		    }
		    if(attacker != null && attacker.isNpc() && victim != null && victim.isPlayer()) {
			WeaponDegrading.handleNpcHit((Player)victim);
		    }
		}
		if (hitDef.getProjectileDef() != null)
			new Projectile(attacker, victim, hitDef.getProjectileDef()).show();
		hitDelay = hitDef.calculateHitDelay(attacker != null ? attacker.getPosition() : null, victim.getPosition());
        if (queue)
            CombatManager.getManager().submit(this);

		if (hit && victim.isPlayer()) {
			RingEffect.ringOfRecoil(attacker, (Player) victim, damage);
			FightCaves.handleNpcHit(attacker, (Player)victim, damage);
		}
	}

	public void display() {
		if (!canDamageEnemy() || damage < 0)
			return;
		UpdateFlags flags = victim.getUpdateFlags();
		HitType hitType = damage == 0 ? HitType.MISS : hitDef.getHitType();
		if(hitDef.isDarkBowSpec() && hitType == HitType.MISS) {
		    hitType = hitDef.getHitType();
		    damage = 5;
		}
		if(hitDef.isDarkBowDragonSpec() && hitType == HitType.MISS) {
		    hitType = hitDef.getHitType();
		    damage = 8;
		}
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
    	if (victim.isPlayer()){
    		Player player = (Player) victim;
			if(player.isHomeTeleporting())
			{
				player.setHomeTeleporting(false);
			}
    	}
        if (hitDef.getDropItem() != null && attacker != null && attacker.isPlayer()) {
            Player player = (Player) attacker;
            if (player.isDropArrow() && Misc.getRandom().nextInt(10) < 6) {
		if(player.getEquipment().getId(Constants.CAPE) == AnimalMagnetism.AVAS_ATTRACTOR || player.getEquipment().getId(Constants.CAPE) == AnimalMagnetism.AVAS_ACCUMULATOR) {
		    Item arrow = new Item(hitDef.getDropItem().getId(), hitDef.getDropItem().getCount());
		    player.getEquipment().getItemContainer().add(arrow, hitDef.getDropItem().getDefinition().getSlot());
		}
            	/*if (player.inDuelArena()) {
            		player.getActionSender().sendMessage(""+hitDef.getDropItem().getId());
            		player.getDuelMainData().getAmmoUsed().add(new Item(hitDef.getDropItem().getId(), hitDef.getDropItem().getCount()));
            	} else {*/
		else {
                    GroundItem dropItem = new GroundItem(new Item(hitDef.getDropItem().getId(), hitDef.getDropItem().getCount()), player, victim.getPosition().clone());
                    GroundItemManager.getManager().dropItem(dropItem);
		}
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
                if (player.shouldAutoRetaliate() && player.getCombatingEntity() == null) {
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
			if(attacker.isNpc() && ((Npc)attacker).getNpcId() == 3200) {
			    
			} else {
			    damage = 0;
			}
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
                    if(!player.inMiniGameArea()){
                    	RingEffect.ringOfLife(player);
                    }
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
	    if(getAttacker().isPlayer() && ((Player)attacker).hasFullVoidMage()) {
		if(chance * 1.3 >= 1.0) { chance = 1.0; }
		else { chance *= 1.3; }
	    }
            if (getAttacker().isPlayer() && ((Player) getAttacker()).isDebugCombat()) {
            	((Player) getAttacker()).getActionSender().sendMessage("Chance to hit: "+(int) (chance * 100)+"% (Rounded)");
            }
            if (getVictim().isPlayer() && ((Player) getVictim()).isDebugCombat() && getAttacker().isNpc()) {
            	((Player) getVictim()).getActionSender().sendMessage("Chance of npc hitting you: "+(int) (chance * 100)+"% (Rounded)");
            }
	    if (getVictim().isPlayer() && ((Player) getVictim()).isDebugCombat() && getAttacker().isPlayer()) {
            	((Player) getVictim()).getActionSender().sendMessage("Chance of player hitting you: "+(int) (chance * 100)+"% (Rounded)");
            }
	    if (getAttacker().isPlayer() && ((Player) attacker).getBonus(3) <= -20 && hitDef.getAttackStyle().getAttackType() == AttackType.MAGIC) {
		hit = false;
	    } else {
		hit = accurate;
	    }
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
