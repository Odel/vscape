package com.rs2.model.npcs;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.World;
import com.rs2.model.content.combat.AttackScript;
import com.rs2.model.content.combat.attacks.BasicAttack;
import com.rs2.model.content.combat.weapon.AttackStyle;
import com.rs2.model.players.item.ItemDefinition;
//import com.rs2.util.XStreamUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 07/04/12 Time: 23:11 To change
 * this template use File | Settings | File Templates.
 */
public class NpcDefinition {

	@SuppressWarnings("unchecked")
	public static void init() throws IOException {
		//List<NpcDefinition> defs = (List<NpcDefinition>) XStreamUtil.getxStream().fromXML(new FileInputStream("data/npcs/npcDefinitions.xml"));
		FileReader reader = new FileReader("./data/npcs/npcDefinitions.json");
		List<NpcDefinition> defs = new Gson().fromJson(reader, new TypeToken<List<NpcDefinition>>(){}.getType());
		for (final NpcDefinition def : defs) {
			if (def.getId() >= Constants.MAX_NPC_ID) {
				break;
			}
            World.getDefinitions()[def.getId()] = def;
            //if (def.attackBonus < def.combat) {
            def.attackBonus = (int) (def.combat);
        	def.defenceMelee = (int) (def.combat / 2);
        	def.defenceMage = (int) (def.combat);
        	def.defenceRange = (int) (def.combat / 2);
            /*}
            if (def.defenceMage == 0) {
            	def.defenceMage = def.defenceRange;
            }*/
            boolean defExists = NpcCombatDef.defExists(def.getId());
            NpcCombatDef combatDef = NpcCombatDef.getDef(def.getId());
            if (defExists) {
            	if (!combatDef.isAttBonusSet() && !combatDef.isDefBonusSet()) {
            		combatDef = combatDef.bonusAtt(def.attackBonus, def.attackBonus, def.attackBonus, def.attackBonus, def.attackBonus).bonusDef(def.defenceMelee, def.defenceMelee, def.defenceMelee, def.defenceMage, def.defenceRange);
            	} else if (!combatDef.isAttBonusSet()) {
               		combatDef = combatDef.bonusAtt(def.attackBonus, def.attackBonus, def.attackBonus, def.attackBonus, def.attackBonus);
                } else if (!combatDef.isDefBonusSet()) {
            		combatDef = combatDef.bonusDef(def.defenceMelee, def.defenceMelee, def.defenceMelee, def.defenceMage, def.defenceRange);
            	}
             } else {
    			combatDef = new NpcCombatDef() {
                    @Override
                    public AttackScript[] attackScripts(Entity attacker, Entity victim) {
                        // Entity attacker, Entity victim, final AttackStyle.Mode
                        // mode, final AttackStyle.Bonus bonus, final int damage,
                        // final int delay, final int animation
                        return new AttackScript[]{BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.STAB, def.maxHit, def.attackSpeed / 600, def.attackAnim)};
                    }
                }.respawnSeconds(def.respawn).bonusAtt(def.attackBonus, def.attackBonus, def.attackBonus, def.attackBonus, def.attackBonus).bonusDef(def.defenceMelee, def.defenceMelee, def.defenceMelee, def.defenceMage, def.defenceRange);
            }
			NpcCombatDef.add(new int[]{def.getId()}, combatDef);
        }
		reader.close();
		System.out.println("Loaded " + defs.size() + " npc definitions.");
	}

	public static class NPCINFO {
		private final String name;
		private final int level;
		private final boolean aggressive;
		private final boolean retreats;
		private final boolean poisonous;

		public NPCINFO(String name, int level, boolean aggressive, boolean retreats, boolean poisonous) {
			this.name = name;
			this.level = level;
			this.aggressive = aggressive;
			this.retreats = retreats;
			this.poisonous = poisonous;
		}

		public String getName() {
			return name;
		}

		public int getLevel() {
			return level;
		}

		public boolean isAggressive() {
			return aggressive;
		}

		public boolean isRetreats() {
			return retreats;
		}

		public boolean isPoisonous() {
			return poisonous;
		}

	}

	public static NpcDefinition forName(String name) {
		for (NpcDefinition d : World.getDefinitions()) {
			if (d.getName().toLowerCase().equalsIgnoreCase(name.toLowerCase())) {
				return d;
			}
		}
		return null;
	}

	public static NpcDefinition forId(int id) {
		NpcDefinition d = World.getDefinitions()[id];
		if (d == null) {
			d = produceDefinition(id);
		}
		return d;
	}

	private int id;
	private String name, examine;
	private int respawn = 0, combat = 0, hitpoints = 1, maxHit = 0, size = 1, attackSpeed = 4000, attackAnim = 422, defenceAnim = 404, deathAnim = 2304, attackBonus = 20, defenceMelee = 20, defenceRange = 20, defenceMage = 20;

	private boolean attackable = false;
	private boolean aggressive = false;
	private boolean retreats = false;
	private boolean poisonous = false;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getExamine() {
		return examine;
	}

	public int getDeathAnim() {
		return deathAnim;
	}

	public int getBlockAnim() {
		return defenceAnim;
	}

	public int getAttackAnim() {
		return attackAnim;
	}

	public int getCombat() {
		return combat;
	}

	public int getSize() {
		return size;
	}

	public boolean isAggressive() {
		return aggressive;
	}

	public boolean retreats() {
		return retreats;
	}

	public boolean isPoisonous() {
		return poisonous;
	}

	public int getHitpoints() {
		return hitpoints;
	}

	public int getMaxhit() {
		return maxHit;
	}

	public static NpcDefinition produceDefinition(int id) {
		final NpcDefinition def = new NpcDefinition();
		def.id = id;
		def.name = "NPC #" + def.id;
		def.examine = "It's an NPC.";
		return def;
	}

	public boolean isAttackable() {
		return attackable;
	}

}
