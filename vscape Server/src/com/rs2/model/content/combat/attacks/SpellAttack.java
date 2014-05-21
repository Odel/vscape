package com.rs2.model.content.combat.attacks;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.World;
import com.rs2.model.content.combat.AttackUsableResponse;
import com.rs2.model.content.combat.effect.Effect;
import com.rs2.model.content.combat.hit.HitDef;
import com.rs2.model.content.combat.projectile.Projectile;
import com.rs2.model.content.combat.projectile.ProjectileDef;
import com.rs2.model.content.combat.projectile.ProjectileTrajectory;

import com.rs2.model.content.minigames.duelarena.RulesData;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.magic.Spell;
import com.rs2.model.content.skills.magic.SpellBook;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.Tick;
import com.rs2.util.requirement.EquipmentRequirement;
import com.rs2.util.requirement.Requirement;
import com.rs2.util.requirement.RuneRequirement;
import com.rs2.util.requirement.SkillLevelRequirement;

/**
 *
 */
public class SpellAttack extends BasicAttack {
	private Spell spell;
	public static final String FAILED_REQUIRED_RUNES = "You do not have the runes required!";
	public static final String FAILED_LEVEL_REQUIREMENT = "Your level in magic is not high enough!";

	public SpellAttack(Entity attacker, Entity victim, Spell spell) {
		super(attacker, victim);
		this.spell = spell;
	}

	@Override
	public final AttackUsableResponse.Type isUsable() {
		if (!spell.isCombatSpell() || spell.getHitDef() == null)
			return AttackUsableResponse.Type.FAIL;
		if (getAttacker().isPlayer()) {
			if (getVictim().isNpc() && ((Npc) getVictim()).getMaxHp() <= 0) {
				((Player) getAttacker()).getActionSender().sendMessage("You cannot cast a spell on this npc.");
				((Player) getAttacker()).setCastedSpell(null);
				return AttackUsableResponse.Type.FAIL;
			}
			if (RulesData.NO_MAGIC.activated((Player) getAttacker())) {
				if (getAttacker().isPlayer()) {
					((Player) getAttacker()).getActionSender().sendMessage("Magic attacks have been disabled during this fight!");
					((Player) getAttacker()).setCastedSpell(null);
					((Player) getAttacker()).setAutoSpell(null);
				}
				return AttackUsableResponse.Type.FAIL;
			}
		}
		if (spell.equals(Spell.CRUMBLE_UNDEAD)) {
			if (!Npc.isUndeadNpc(getVictim())) {
				((Player) getAttacker()).getActionSender().sendMessage("You can only cast this spell on undead npc.");
				((Player) getAttacker()).setCastedSpell(null);
				return AttackUsableResponse.Type.FAIL;
			}
		}
		if (spell.getRequiredEffect() != null) {
			if (!getVictim().canAddEffect(spell.getRequiredEffect())) {
				if (getAttacker().isPlayer()) {
					((Player) getAttacker()).getActionSender().sendMessage("The target is immune to that spell right now!");
					((Player) getAttacker()).setCastedSpell(null);
				}
				return AttackUsableResponse.Type.FAIL;
			}
		}
		return super.isUsable();
	}

	@Override
	public int distanceRequired() {
		return 10;
	}

	@Override
	public void initialize() {
		if (!spell.isCombatSpell() || spell.getHitDef() == null)
			return;
		if (spell.getAnimation() != -1) {
			boolean setAnimation = false;
			if (getAttacker().isPlayer()) {
				Player player = (Player) getAttacker();
				if (player.isAutoCasting() && player.getAutoSpell() == spell && player.getMagicBookType() == SpellBook.MODERN) {
					setAnimation(1162);
					setAnimation = true;
				}
			}
			if (!setAnimation)
				setAnimation(spell.getAnimation());
		}
		HitDef hitDef = spell.getHitDef().clone();
		if (getAttacker().hasGodChargeEffect() && (spell == Spell.FLAMES_OF_ZAMORAK || spell == Spell.CLAWS_OF_GUTHIX || spell == Spell.SARADOMIN_STRIKE))
			hitDef.setDamage(hitDef.getDamage() + 10);
        //if(getAttacker().isNpc() && ((Npc)getAttacker()).getDefinition().getId() == 2025 && Misc.random(30) == 0){
        //     setHits(new HitDef[]{hitDef.randomizeDamage().applyAccuracy().addEffects(new Effect[]{new StatEffect(Skill.STRENGTH, .1)})});
        //     getVictim().getUpdateFlags().sendGraphic(400, 100 << 16);
        //}else
	if(getAttacker().isPlayer() ) {
	    Player player = (Player) getAttacker();
	    if(player.hasFullVoidMage())
		 setHits(new HitDef[]{hitDef.randomizeDamage().applyAccuracy(1.3).addEffects(new Effect[]{spell.getRequiredEffect(), spell.getAdditionalEffect()})});
	}
	    
        setHits(new HitDef[]{hitDef.randomizeDamage().applyAccuracy().addEffects(new Effect[]{spell.getRequiredEffect(), spell.getAdditionalEffect()})});
		setGraphic(spell.getGraphic());
		setAttackDelay(5);
		Item[] runesRequired = spell.getRunesRequired();
		int staffRequired = -1;
		
		if(getAttacker().isPlayer()) {
		    Player player = (Player) getAttacker();
		    if(player.getEquipment().voidMace() && spell == Spell.CLAWS_OF_GUTHIX)
			staffRequired = 8841;
		    else if (spell == Spell.FLAMES_OF_ZAMORAK)
			staffRequired = 2417;
		    else if (spell == Spell.CLAWS_OF_GUTHIX)
			staffRequired = 2416;
		    else if (spell == Spell.SARADOMIN_STRIKE)
			staffRequired = 2415;
		    else if (spell == Spell.IBAN_BLAST)
			staffRequired = 1409;
		    else if (spell == Spell.MAGIC_DART)
			staffRequired = 4170;
		}
		    
		int reqs = (staffRequired != -1 ? 1 : 0) + (runesRequired != null ? runesRequired.length : 0) + 1;
		Requirement[] requirements = new Requirement[reqs];
		int i = 0;
		if (runesRequired != null) {
			for (; i < runesRequired.length; i++) {
				requirements[i] = new RuneRequirement(runesRequired[i].getId(), runesRequired[i].getCount()) {
					@Override
					public String getFailMessage() {
						return "You do not have the runes required!";
					}
				};
			}
		}
		requirements[i++] = new SkillLevelRequirement(Skill.MAGIC, spell.getLevelRequired()) {
			@Override
			public String getFailMessage() {
				return "Your level in magic is not high enough!";
			}
		};
		if (staffRequired != -1) {
			requirements[i] = new EquipmentRequirement(Constants.WEAPON, staffRequired, 1, false) {
				@Override
				public String getFailMessage() {
					return "You must equip the proper staff to use this spell!";
				}
			};
		}
		setRequirements(requirements);
	}

	@Override
	public int execute(CycleEventContainer container) {
        //getAttacker().getMovementPaused().setWaitDuration(2);
		if (getAttacker().isPlayer()) {
			Player player = (Player) getAttacker();
			if (!player.isAutoCasting()) {
				getAttacker().getMovementHandler().reset();
			}
			if (player.getCastedSpell() == spell) {
                if (player.getNewComersSide().getTutorialIslandStage() == 64)
                    player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);
                player.setCastedSpell(null);
                container.stop();
            }
        }
        if (spell == Spell.ICE_BARRAGE) {
            if (getVictim().getMovementHandler().getWaypoints().size() > 0) {
                Tick t = new Tick(1) {
                    @Override
                    public void execute() {
                        ProjectileTrajectory trajectory = new ProjectileTrajectory(0, 6, 50, 50, 0);
                        Projectile projectile = new Projectile(getVictim(), getVictim(), getVictim().getMovementHandler().getWaypoints().peekLast(), new ProjectileDef(368, trajectory));
                        projectile.show();
                        stop();
                    }
                };
                World.getTickManager().submit(t);
            }
        }
        if (getAttacker().isPlayer()) {
            Player player = (Player)getAttacker();
            player.getSkill().addExp(Skill.MAGIC, spell.getExpEarned());
        }
        int delay = super.execute(container);
		// if (delay > 0 && spell == Spell.ICE_BLITZ)
		// delay -= 1;
		return delay;
	}

	@Override
	public void failedRequirement() {
		if (getAttacker().isPlayer()) {
			Player player = (Player) getAttacker();
			if (player.getCastedSpell() == spell)
				player.setCastedSpell(null);
			else if (player.getAutoSpell() == spell)
				player.setAutoSpell(null);
		}
	}

	public Spell getSpell() {
		return spell;
	}
}
