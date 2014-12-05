package com.rs2.model.content.combat.util;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.minigames.fightcaves.WavesHandling;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.magic.BoltEnchanting;
import com.rs2.model.content.skills.runecrafting.TabHandler;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class EnchantedBolts {

    public enum BoltSpecials {

	OPAL(9236, 749, 15, 1.2),
	SAPPHIRE(9240, 751, 25, 1d),
	JADE(9237, 755, 12, 1d),
	PEARL(9238, 750, 10, 1.25),
	EMERALD(9241, 752, 30, 1d),
	TOPAZ(9239, 757, 15, 1d),
	RUBY(9242, 754, 10, 1d),
	DIAMOND(9243, 758, 15, 1.1),
	DRAGONSTONE(9244, 756, 8, 1d),
	ONYX(9245, 753, 25, 1d);

	private int boltId;
	private int gfxId;
	private int procChance;
	private double increasedDamage;

	BoltSpecials(int boltId, int gfxId, int procChance, double incDmg) {
	    this.boltId = boltId;
	    this.gfxId = gfxId;
	    this.procChance = procChance;
	    this.increasedDamage = incDmg;
	}
	
	public static BoltSpecials forBoltId(int id) {
	    for(BoltSpecials b : BoltSpecials.values()) {
		if(b.boltId == id) {
		    return b;
		}
	    }
	    return null;
	}
	
	public int getGfxId() {
	    return this.gfxId;
	}
	
	public int getProcChance() {
	    return this.procChance;
	}
	
	public double getIncreasedDamage() {
	    return this.increasedDamage;
	}
	
	public void setIncreasedDamage(double set) {
	    this.increasedDamage = set;
	}

    }

    public static boolean activateSpecial(BoltSpecials b, final Player attacker, final Entity victim, int damage) {
	Player pVictim = null;
	Npc nVictim = null;
	if (victim.isPlayer()) {
	    pVictim = (Player) victim;
	}
	if (victim.isNpc()) {
	    nVictim = (Npc) victim;
	}
	switch (b) {
	    case OPAL:
		//Lucky lightning, deal extra damage
		victim.getUpdateFlags().sendGraphic(b.getGfxId());
		return true;
	    case SAPPHIRE:
		//Clear mind, Drains opponent's prayer by 5% and gives it to you (PVP only)
		if (pVictim == null) {
		    return false;
		} else {
		    victim.getUpdateFlags().sendGraphic(b.getGfxId());
		    int drainAmount = (int)Math.floor(pVictim.getSkill().getLevel()[Skill.PRAYER] * 0.05);
		    pVictim.getPrayer().drainPrayer(drainAmount);
		    if((attacker.getSkill().getPlayerLevel(Skill.PRAYER) - attacker.getSkill().getLevel()[Skill.PRAYER]) > drainAmount) {
			attacker.getActionSender().statEdit(Skill.PRAYER, drainAmount, false);
		    } else {
			drainAmount = (attacker.getSkill().getPlayerLevel(Skill.PRAYER) - attacker.getSkill().getLevel()[Skill.PRAYER]);
			attacker.getActionSender().statEdit(Skill.PRAYER, drainAmount, false);
		    }
		    attacker.getSkill().refresh(Skill.PRAYER);
		    pVictim.getSkill().refresh(Skill.PRAYER);
		    return false;
		}
	    case JADE:
		//Earth's fury, Chance of knocking opponent to the ground... Stunning in this scenario :^)
		victim.getUpdateFlags().sendGraphic(b.getGfxId());
		victim.getStunTimer().setWaitDuration(8);
		victim.getStunTimer().reset();
		return false;
	    case PEARL:
		victim.getUpdateFlags().sendGraphic(b.getGfxId());
		if(pVictim != null && TabHandler.checkStaffs(BoltEnchanting.WATER_RUNE, pVictim.getEquipment().getId(Constants.WEAPON))) {
		    return false; //If they've got some form of water stave, don't increase damage
		}
	    return true;
	    case EMERALD:
		//Magical poison, Poisons the opponent for 5 damage. 
		if(nVictim != null && nVictim.getDefinition().isPoisonImmune()) {
		    return false;
		} else {
		    if (victim.getPoisonDamage() == 0.0 && !victim.isPoisonImmune()) {
			victim.setPoisonDamage(5.0);
			victim.getUpdateFlags().sendGraphic(b.getGfxId());
			victim.hit(5, HitType.POISON);
			CycleEventHandler.getInstance().addEvent(attacker, new CycleEvent() {
			    @Override
			    public void execute(CycleEventContainer b) {
				if (victim == null || victim.isDead() || victim.isPoisonImmune()) {
				    b.stop();
				}
				if (Misc.random(3) == 1 && victim != null) {
				    victim.hit(5, HitType.POISON);
				}
			    }

			    @Override
			    public void stop() {
			    }
			}, 5);
		    }
		}
	    return false;
	    case TOPAZ:
		//Down to earth, Lowers Opponent's magic level (PVP only) 
		if (pVictim == null) {
		    return false;
		} else {
		    victim.getUpdateFlags().sendGraphic(b.getGfxId());
		    int drainAmount = (int)Math.floor(pVictim.getSkill().getLevel()[Skill.MAGIC] * 0.1);
		    pVictim.getActionSender().statEdit(Skill.MAGIC, -drainAmount, false);
		    return false;
		}
	    case RUBY:
		//Blood forfeit, Extracts 20% of your opponents HP in exchange for a 10% HP of your own.
		victim.getUpdateFlags().sendGraphic(b.getGfxId());
		attacker.hit((int)Math.ceil(attacker.getCurrentHp() * 0.1), HitType.NORMAL);
		return true;
	    case DIAMOND:
		//Armour piercing, Ignores the opponent's armour and hit slightly higher.
		if(attacker.getArmorPiercedEntity() == null || attacker.getArmorPiercedEntity() != victim) {
		    victim.getUpdateFlags().sendGraphic(b.getGfxId());
		    attacker.setArmorPiercedEntity(victim);
		    return true;
		}
	    return false;
	    case DRAGONSTONE:
		//Dragon's breath, Deals dragonfire damage (resisted through anti-dragonfire equipment)
		if(pVictim != null && pVictim.antiFire() > 0) {
		    return false;
		} else {
		    victim.getUpdateFlags().sendGraphic(b.getGfxId());
		    return true;
		}
	    case ONYX:
		victim.getUpdateFlags().sendGraphic(b.getGfxId());
		attacker.heal((int)Math.ceil(damage * 0.25));
		return false;
	}
	return false;
    }
    
    public static boolean fireNpc(Npc npc) {
	String name = npc.getDefinition().getName().toLowerCase();
	if(name.contains("tzhaar") || name.contains("tz-") || name.contains("fire") || name.contains("pyrefiend")) {
	    return true;
	}
	for(int i : WavesHandling.npcIds) {
	    if(i == npc.getNpcId()) {
		return true;
	    }
	}
	return false;
    }
}
