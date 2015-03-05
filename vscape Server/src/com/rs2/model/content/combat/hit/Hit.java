package com.rs2.model.content.combat.hit;

import com.rs2.Constants;

import java.util.List;

import com.rs2.model.Entity;
import com.rs2.model.Graphic;
import com.rs2.model.Position;
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
import com.rs2.model.content.combat.effect.impl.BindingEffect;
import com.rs2.model.content.combat.effect.impl.StatEffect;
import com.rs2.model.content.combat.effect.impl.StunEffect;
import com.rs2.model.content.combat.projectile.Projectile;
import com.rs2.model.content.combat.special.SpecialType;
import com.rs2.model.content.combat.util.Degrading;
import com.rs2.model.content.combat.util.EnchantedBolts;
import com.rs2.model.content.combat.util.RingEffect;
import com.rs2.model.content.combat.weapon.AttackStyle;
import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.content.minigames.fightcaves.FightCaves;
import com.rs2.model.content.minigames.pestcontrol.PestControl;
import com.rs2.model.content.quests.AnimalMagnetism;
import com.rs2.model.content.quests.DemonSlayer;
import com.rs2.model.content.quests.FamilyCrest;
import com.rs2.model.content.quests.GoblinDiplomacy;
import com.rs2.model.content.quests.HorrorFromTheDeep;
import com.rs2.model.content.quests.MonkeyMadness.ApeAtoll;
import com.rs2.model.content.quests.MonkeyMadness.MonkeyMadness;
import com.rs2.model.content.quests.NatureSpirit;
import com.rs2.model.content.quests.RecruitmentDrive;
import com.rs2.model.content.quests.VampireSlayer;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.magic.Spell;
import com.rs2.model.content.skills.prayer.Prayer;
import com.rs2.model.content.skills.prayer.Prayer.PrayerData;
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
		if(Constants.DDOS_PROTECT_MODE) {
			if(victim != null && victim.isPlayer()) {
			    damage = 0;
			    return;
			}
		}
		if(victim != null && victim.isPlayer() && ((Player)victim).getStaffRights() >= 3) { //dev mode just in case
		    damage = 0;
		    return;
		}
		if(victim != null && victim.isPlayer() && attacker != null && attacker.isNpc() && ((Player)victim).getMMVars().inProcessOfBeingJailed && ((Player)victim).onApeAtoll() && ((Npc)attacker).getNpcId() != 1457) {
		    return;
		}
		if(victim != null && victim.isPlayer() && MinigameAreas.isInArea(victim.getPosition(), ApeAtoll.JAIL) && (damage > 8 || (victim.getCurrentHp() - damage) <= 0)) {
		    return;
		}
		if(victim.isNpc() && hitDef.getHitType() == HitType.POISON)
		{
			if(((Npc)victim).getDefinition() != null && ((Npc)victim).getDefinition().isPoisonImmune())
				return;
		}
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
			    player.getQuestVars().setHitChronozonWind(true);
			} else if(hitDef.getHitGraphic().getId() == 137) {
			    player.getActionSender().sendMessage("Chronozon weakens slightly.");
			    player.getQuestVars().setHitChronozonWater(true);
			} else if(hitDef.getHitGraphic().getId() == 140) {
			    player.getActionSender().sendMessage("Chronozon weakens slightly.");
			    player.getQuestVars().setHitChronozonEarth(true);
			} else if(hitDef.getHitGraphic().getId() == 131) {
			    player.getActionSender().sendMessage("Chronozon weakens slightly.");
			    player.getQuestVars().setHitChronozonFire(true);
			}
		    }
		}
		if(attacker != null && victim != null && victim.isNpc() && hitDef != null && attacker.isPlayer() && ((Npc)victim).getNpcId() == 205) { //Salarin
		    if(hitDef.getAttackStyle() != null && (hitDef.getAttackStyle().getAttackType().equals(AttackType.RANGED) || hitDef.getAttackStyle().getAttackType().equals(AttackType.MELEE))) {
			damage = 0;
			((Player)attacker).getActionSender().sendMessage("Salarin the Twisted resists your attack!", true);
		    } else {
			if(hitDef.getHitGraphic() != null) {
			    int gfxId = hitDef.getHitGraphic().getId();
			    if(gfxId != 92 && gfxId != 95 && gfxId != 98 && gfxId != 101 && gfxId != 76) {
				damage = 0;
				((Player)attacker).getActionSender().sendMessage("Salarin the Twisted resists your attack!", true);
			    }
			}
		    }
		}
		if(attacker != null && victim != null && attacker.isPlayer() && hitDef != null && hitDef.getAttackStyle() != null && hitDef.getAttackStyle().getAttackType().equals(AttackType.RANGED)) {
		    Player player = (Player)attacker;
		    boolean noWeapon = player.getEquipment().getItemContainer().get(Constants.WEAPON) == null;
		    boolean noCrossbow = player.getEquipment().getItemContainer().get(Constants.WEAPON) != null && !player.getEquipment().getItemContainer().get(Constants.WEAPON).getDefinition().getName().contains("c'bow") && !player.getEquipment().getItemContainer().get(Constants.WEAPON).getDefinition().getName().contains("crossbow");
		    EnchantedBolts.BoltSpecials b = EnchantedBolts.BoltSpecials.forBoltId(player.getEquipment().getId(Constants.ARROWS));
		    if (b != null && !noWeapon && !noCrossbow) {
			if ((b.getProcChance() >= new Random().nextDouble() * 100) && damage != 0 && hitDef.getHitType() != HitType.MISS) {
			    if (EnchantedBolts.activateSpecial(b, player, victim, damage)) {
				if (!b.equals(EnchantedBolts.BoltSpecials.RUBY)) {
				    damage *= b.getIncreasedDamage();
				}
				if (b.equals(EnchantedBolts.BoltSpecials.PEARL) && victim.isNpc() && EnchantedBolts.fireNpc((Npc) victim)) {
				    damage *= 1.1;
				} else if (b.equals(EnchantedBolts.BoltSpecials.RUBY)) {
				    damage = (int) Math.ceil(victim.getCurrentHp() / 5);
				} else if (b.equals(EnchantedBolts.BoltSpecials.DRAGONSTONE)) {
				    damage += 20;
				}
			    }
			}
		    }
		}
		if(attacker != null && victim != null && attacker.isPlayer() && victim.isNpc() && victim.onApeAtoll()) {
		    if(((Npc)victim).getNpcId() == 1459 || ((Npc)victim).getNpcId() == 1460) {
			if(victim.getCurrentHp() < (victim.getMaxHp() / 6) && Misc.random(2) == 1) {
			    victim.getUpdateFlags().sendAnimation(1405);
			    hitDef.setVictimAnimation(6969);
			    ((Npc)victim).setCurrentHp(((Npc)victim).getCurrentHp() + 25);
			}
		    }
		}
		if(attacker != null && victim != null && attacker.isPlayer() && ((Player)attacker).getArmorPiercedEntity() != null && ((Player)attacker).getArmorPiercedEntity().equals(victim)) {
		    damage *= 1.1;
		   // ((Player)attacker).getActionSender().sendMessage("Armor piercing...");
		}
		if(attacker != null && victim != null && attacker.isPlayer() && victim.isPlayer())
		{
			if(attacker.inCwGame() && victim.inCwGame()){
				Player playerA = (Player)attacker;
				Player playerV = (Player)victim;
				if(playerA.wearingCwBracelet() && playerV.carryingCwBanner()){
					 damage *= 1.2;
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
		if(attacker != null && victim != null && attacker.isPlayer() && victim.isNpc() && ((Npc)victim).getNpcId() == RecruitmentDrive.SIR_LEYE) {
		    if((victim.getCurrentHp() - damage) <= 0 && ((Player)attacker).getGender() != 1) {
			victim.getUpdateFlags().sendForceMessage("Fool! No man can defeat me!");
			damage = 0;
		    }
		}
		if(attacker != null && victim != null && attacker.isPlayer() 
		&& victim.isNpc() && ((Npc)victim).getNpcId() >= 1351 && ((Npc)victim).getNpcId() < 1357 ) {
		 //   Player player = (Player)attacker;
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
		   // Npc npc = (Npc)victim;
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
		if(attacker != null && victim != null && victim.isNpc() && ((Npc)victim).getNpcId() != 2745 && hitDef != null && hitDef.getAttackStyle() != null && hitDef.getAttackStyle().getAttackType().equals(AttackType.MELEE)) {
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
				    @SuppressWarnings({ "rawtypes", "unchecked" })
					@Override
				    public void execute(CycleEventContainer b) {
					Hit hit = new Hit(getAttacker(), npcs, hitDefMulti);
					List<Hit> hitList = new LinkedList<Hit>();
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
					int damage = Misc.random(hitDefMulti.getDamage());
					npcs.hit(damage, HitType.NORMAL);
					((Player) attacker).getSkill().addExp(Skill.MAGIC, damage * 4d);
					if(((Player) attacker).inPestControlGameArea()) {
					    PestControl.handleHit(attacker, victim, damage);
					}
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
					@SuppressWarnings({ "rawtypes", "unchecked" })
					@Override
					public void execute(CycleEventContainer b) {
					    Hit hit = new Hit(getAttacker(), players, hitDefMulti);
					    List<Hit> hitList = new LinkedList<Hit>();
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
			Degrading.handleHit((Player)attacker, true);
		    }
		    if(attacker != null && attacker.isNpc() && victim != null && victim.isPlayer()) {
			Degrading.handleHit((Player)victim, false);
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
		if(attacker != null && attacker.isNpc() && ((Npc)attacker).getNpcId() == 1052 && hitType == HitType.MISS) {
		    return;
		}
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
		
		if(hitDef.getAttackStyle() != null && hitDef.getAttackStyle().getAttackType() == AttackType.MAGIC)
		{
			Spell spell = SpellAttack.getSpellForHitGfx(hitDef.getHitGraphic());
			if(spell != null)
			{
				if (getAttacker() != null && getAttacker().isPlayer()){
					Player player = (Player) getAttacker();
					player.getCombatSounds().spellSound(spell, false);
				}
				if (getVictim() != null && getVictim().isPlayer()) {
					Player player = (Player) getVictim();
					player.getCombatSounds().spellSound(spell, false);
				}
			}
		}else{
	        if (getAttacker() != null && getAttacker().isPlayer() && getVictim().isNpc()){
	       	 Player player = (Player) getAttacker();
	       	 Npc npc = (Npc) getVictim();
	       	 if (npc.getPosition().isViewableFrom(player.getPosition())) {
		       	 if(hitType == HitType.MISS){
		       		 player.getCombatSounds().npcBlockSound(((Npc) getVictim()));
		       	 }else{
		       		 player.getCombatSounds().npcDamageSound(((Npc) getVictim()));
		       	 }
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
	       if (getVictim() != null && getVictim().isPlayer()) {
	           Player player = (Player) getVictim();
		       	 if(hitType == HitType.MISS){
		    		 player.getCombatSounds().blockSound(player);
		    	 }else{
		    		 player.getCombatSounds().damageSound();
		    	 }
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
            	Item arrow = new Item(hitDef.getDropItem().getId(), hitDef.getDropItem().getCount());
            	if(player.getEquipment().getId(Constants.CAPE) == AnimalMagnetism.AVAS_ATTRACTOR || player.getEquipment().getId(Constants.CAPE) == AnimalMagnetism.AVAS_ACCUMULATOR) {
            		player.getEquipment().addEquipment(arrow, arrow.getDefinition().getSlot());
            	}
            	/*if (player.inDuelArena()) {
            		player.getActionSender().sendMessage(""+hitDef.getDropItem().getId());
            		player.getDuelMainData().getAmmoUsed().add(new Item(hitDef.getDropItem().getId(), hitDef.getDropItem().getCount()));
            	} else {*/
            	 else {
                    GroundItem dropItem = new GroundItem(arrow, player, victim.getPosition().clone());
                    GroundItemManager.getManager().dropItem(dropItem);
				}
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
            if (victim.isNpc() && !victim.isDontAttack() && !((Npc)victim).walkingBackToSpawn) {
            	CombatManager.attack(victim, attacker);
            } else if (victim.isPlayer() && !victim.isMoving()) {
                Player player = (Player) victim;
                if (player.shouldAutoRetaliate() && player.getCombatingEntity() == null) {
                    CombatManager.attack(victim, attacker);
                }
            }
        }
	if (attacker != null && victim != null && attacker.isNpc() && victim.isNpc()) {
	    Npc attacker = (Npc) this.attacker;
	    Npc victim = (Npc) this.victim;
	    if (attacker.getNpcId() >= 1412 && attacker.getNpcId() <= 1426) {
		hitDef.setVictimAnimation(6969);
		if(victim.getNpcId() == 1472) {
		    if (damage > 1) {
			damage = 1;
		    }
		    if (Misc.random(1) == 1) {
			damage = 0;
		    }
		}
		if(victim.getNpcId() != 1472) {
		    if(Misc.random(3) == 1) {
			damage = 1;
		    }
		}
		if (victim.getNpcId() == MonkeyMadness.JUNGLE_DEMON && (victim.getCurrentHp() - damage) <= 0) {
		    victim.setCurrentHp(victim.getMaxHp());
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
			if(victim.getBlockAnimation() > 0) {
			    victim.getUpdateFlags().sendAnimation(victim.getBlockAnimation());
			}
    		}
        }
	if(victim != null && attacker != null && attacker.isNpc() && victim.isPlayer() && ((Npc)attacker).getDefinition().getName().equals("Spinolyp") && hitDef.getEffects() != null && !hitDef.getEffects().isEmpty() && hitDef.getEffects().get(0) != null && hitDef.getEffects().get(0).equals(new StatEffect(5, 0)) && damage > 0) {
	    ((Player)victim).getActionSender().statEdit(5, -1, false);
	}
        if (victim != null && victim.isPlayer() && !((Player)victim).getMMVars().inProcessOfBeingJailed) {
            Player player = (Player) victim;
            player.getActionSender().removeInterfaces();
        }
	if (attacker != null && victim.isPlayer() && attacker.isNpc() && ((Npc) attacker).getNpcId() == 2882) {
	    Position p = victim.getPosition();
	    for (Player player : World.getPlayers()) {
		if (player != null && Misc.goodDistance(p, player.getPosition(), 1) && !player.isProtectingFromCombat(AttackType.MAGIC, attacker)) {
		    player.hit(Misc.random(61), HitType.NORMAL);
		}
	    }
	}
	if (attacker != null && attacker.isNpc() && victim != null && victim.isPlayer() && ((Npc)attacker).getNpcId() == NatureSpirit.GHAST) {
	    NatureSpirit.handleSpoilFood((Npc)attacker, (Player)victim);
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
            	if (hitDef.getAttackStyle().getMode() == AttackStyle.Mode.DRAGONFIRE || hitDef.getAttackStyle().getMode() == AttackStyle.Mode.DRAGONFIRE_FAR || (attacker.isNpc() && ((Npc)attacker).getNpcId() == 50 && hitDef.getAttackStyle().getMode() != AttackStyle.Mode.MELEE_ACCURATE) && victim.isPlayer()) {
		    Player player = (Player) victim;
		    switch (player.antiFire()) {
			case 2:
			    if (attacker.isNpc() && ((Npc)attacker).getNpcId() == 50 && hitDef.getAttackStyle().getMode() != AttackStyle.Mode.MELEE_ACCURATE && hitDef.getAttackStyle().getMode() != AttackStyle.Mode.DRAGONFIRE && hitDef.getAttackStyle().getMode() != AttackStyle.Mode.DRAGONFIRE_FAR) {
				damage = Misc.random(10); //KBD's 3 special breaths
			    } else {
				damage = 0;
			    }
			    break;
			case 1:
			    if (attacker != null && attacker.isNpc() && !((Npc) attacker).getDefinition().getName().toLowerCase().contains("wyvern")) {
				player.getActionSender().sendMessage("You manage to resist some of the dragonfire.");
				if (player.getEquipment().getId(Constants.SHIELD) == 11284) {
				    player.getUpdateFlags().sendAnimation(6695); //6700 removing charges, 6696 special
				    player.getUpdateFlags().sendGraphic(new Graphic(1164, 0));
				    if (player.getDfsCharges() < 49) {
					player.setDfsCharges(player.getDfsCharges() + 1);
				    } else if (player.getDfsCharges() == 49) {
					player.setDfsCharges(50);
					player.getActionSender().sendMessage("Your Dragonfire shield is fully charged.");
					player.getEquipment().replaceEquipment(11283, Constants.SHIELD);
				    }
				    player.getEquipment().sendBonus(player);
				}
				if(((Npc) attacker).getNpcId() == 50) {
				    if (attacker.isNpc() && ((Npc)attacker).getNpcId() == 50 && hitDef.getAttackStyle().getMode() != AttackStyle.Mode.MELEE_ACCURATE && hitDef.getAttackStyle().getMode() != AttackStyle.Mode.DRAGONFIRE && hitDef.getAttackStyle().getMode() != AttackStyle.Mode.DRAGONFIRE_FAR) {
					damage = Misc.random(10); //KBD's 3 special breaths
				    } else {
					damage = Misc.random(15);
				    }
				} else {
				    damage = Misc.random(hitDef.getAttackStyle().getMode() == AttackStyle.Mode.DRAGONFIRE_FAR ? 8 : 4);
				}
				break;
			    } else {
				HitDef hitDefFreeze = new HitDef(null, HitType.NORMAL, 0);
				Hit freeze = new Hit(player, player, hitDefFreeze);
				BindingEffect bind = new BindingEffect(12);
				bind.initialize(freeze);
				player.getActionSender().sendMessage("You manage to resist most of the icy breath, but are frozen in place.");
				damage = Misc.random(10);
				break;
			    }
			default:
			    if (attacker != null && attacker.isNpc() && !((Npc) attacker).getDefinition().getName().toLowerCase().contains("wyvern")) {
				player.getActionSender().sendMessage("You are horribly burned by the dragonfire!");
				damage = 30 + Misc.random(20);
				break;
			    } else if (attacker != null && attacker.isNpc() && ((Npc) attacker).getNpcId() == 50) {
				player.getActionSender().sendMessage("You are horribly burned by the dragonfire!");
				damage = 45 + Misc.random(20);
				break;
			    } else {
				if (player.getEquipment().getId(Constants.SHIELD) != 2890) {
				    player.getActionSender().sendMessage("You are horribly burned by the ice breath!");
				    damage = 30 + Misc.random(20);
				    break;
				} else {
				    HitDef hitDefFreeze = new HitDef(null, HitType.NORMAL, 0);
				    Hit freeze = new Hit(player, player, hitDefFreeze);
				    BindingEffect bind = new BindingEffect(12);
				    bind.initialize(freeze);
				    player.getActionSender().sendMessage("You magically resist most of the icy breath, but are frozen in place.");
				    damage = Misc.random(10);
				    break;
				}
			    }
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
		else if (getAttacker() != null && getAttacker().isPlayer() && getVictim().isNpc() && ((Npc)victim).getNpcId() == 2881) { //Supreme
		    if(hitDef.getAttackStyle().getAttackType() == AttackType.RANGED || hitDef.getAttackStyle().getAttackType() == AttackType.MAGIC) {
			if(Misc.random(2) == 1) {
			    damage = 0;
			} else {
			    damage /= 10;
			}
		    }
		}
		else if (getAttacker() != null && getAttacker().isPlayer() && getVictim().isNpc() && ((Npc)victim).getNpcId() == 2882) { //Prime
		    if(hitDef.getAttackStyle().getAttackType() == AttackType.MELEE || hitDef.getAttackStyle().getAttackType() == AttackType.MAGIC) {
			if(Misc.random(2) == 1) {
			    damage = 0;
			} else {
			    damage /= 10;
			}
		    }
		}
		else if (getAttacker() != null && getAttacker().isPlayer() && getVictim().isNpc() && ((Npc)victim).getNpcId() == 2883) { //Rex
		    if(hitDef.getAttackStyle().getAttackType() == AttackType.MELEE || hitDef.getAttackStyle().getAttackType() == AttackType.RANGED) {
			if(Misc.random(2) == 1) {
			    damage = 0;
			} else {
			    damage /= 10;
			}
		    }
		}
		else if (getAttacker() != null && getAttacker().isPlayer() && getVictim().isNpc() && ((Npc)victim).getNpcId() == 1158) {
		    if(hitDef.getAttackStyle().getAttackType() == AttackType.RANGED || hitDef.getAttackStyle().getAttackType() == AttackType.MAGIC)
			damage = 0;
		}
		else if (getAttacker() != null && getAttacker().isPlayer() && ((Player)attacker).getStaffRights() < 1 && getVictim().isNpc() && ((Npc)victim).getNpcId() == 1160) {
		    if(hitDef.getAttackStyle().getAttackType() == AttackType.MELEE) {
			if(((Player)attacker).getEquipment().fullVerac() && Misc.random(100) > 50) {
			    //do nothing what a madman
			} else {
			    damage = 0;
			}
		    }
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
        	if(canAddXp()){
        		addCombatExp((Player) attacker, damage, hitDef.getAttackStyle());
        	}
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
                if (player.getIsUsingPrayer()[PrayerData.SMITE.getIndex()] && victim.isPlayer()) {
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
                    if (player.getIsUsingPrayer()[PrayerData.REDEMPTION.getIndex()]) {
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
    
    public boolean canAddXp(){
    	if(getVictim().isNpc()){
    		if(victim.isBarricade()){
    			return false;
    		}
    	}	
    	return true;
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
	    } else if(attacker != null && attacker.isNpc() && victim != null && victim.isNpc() && ((Npc)victim).getNpcId() == 1472) {
		hit = true;
	    } else if (attacker != null && attacker.isNpc() && victim != null && victim.isNpc() && attacker.onApeAtoll()) {
		hit = true;
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