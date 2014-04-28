package com.rs2.model.content.skills;

import com.rs2.Constants;
import com.rs2.model.players.Player;

public class SkillCapeHandler {
	private Player player;

	public SkillCapeHandler(Player player) {
		this.player = player;
	}
	
	public enum SkillCape
	{
		ATTACK(Skill.ATTACK, 9747, 9748, 0),
		STRENGTH(Skill.STRENGTH, 9750, 9751, 0),
		DEFENCE(Skill.DEFENCE, 9753, 9754, 0),
		RANGE(Skill.RANGED, 9756, 9757, 0),
		PRAYER(Skill.PRAYER, 9759, 9760, 0),
		MAGIC(Skill.MAGIC, 9762, 9763, 0),
		RUNECRAFTING(Skill.RUNECRAFTING, 9765, 9766, 0),
		HITPOINT(Skill.HITPOINTS, 9768, 9769, 0),
		AGILITY(Skill.AGILITY, 9771, 9772, 0),
		HERBLORE(Skill.HERBLORE, 9774, 9775, 0),
		THIEVING(Skill.THIEVING, 9777, 9778, 0),
		CRAFTING(Skill.CRAFTING, 9780, 9781, 0),
		FLETCHING(Skill.FLETCHING, 9783, 9784, 0),
		SLAYER(Skill.SLAYER, 9786, 9787, 0),
		CONSTRUCTION(-1, 9789, 9790, 0),
		MINING(Skill.MINING, 9792, 9793, 0),
		SMITHING(Skill.SMITHING, 9795, 9796, 0),
		FISHING(Skill.FISHING, 9798, 9799, 0),
		COOKING(Skill.COOKING, 9801, 9802, 0),
		FIREMAKING(Skill.FIREMAKING, 9804, 9805, 0),
		WOODCUTTING(Skill.WOODCUTTING, 9807, 9808, 0),
		FARMING(Skill.FARMING, 9810, 9811, 0);
		
		private int skillId;
		private int skillcapeId;
		private int skillcapeTrimmedId;
		private int hoodId;
		
		SkillCape(int skillId, int skillcapeId, int skillcapeTrimmedId, int hoodId) {
			this.skillId = skillId;
			this.skillcapeId = skillcapeId;
			this.skillcapeTrimmedId = skillcapeTrimmedId;
			this.hoodId = hoodId;
		}
		
		public static SkillCape forId(int skillId) {
			for (SkillCape cape : SkillCape.values())
					if (skillId == cape.skillId)
						return cape;
			return null;
		}
	}
	
	public int getCapeId(int skillId, int type)
	{
		SkillCape cape = SkillCape.forId(skillId);
		if(cape == null)
			return -1;
		switch(type)
		{
			case 0:
			return cape.skillcapeId;
			case 1:
			return cape.skillcapeTrimmedId;
		}
		return -1;
	}
	
	public int getHoodId(int skillId)
	{
		SkillCape cape = SkillCape.forId(skillId);
		if(cape == null)
			return -1;
		
		return cape.hoodId;
	}
	
	public boolean hasCape(int skillId, int type)
	{
		SkillCape cape = SkillCape.forId(skillId);
		if(cape == null)
			return false;
		switch(type)
		{
			case 0:
				if(player.hasItem(cape.skillcapeId))
				{
					return true;
				}
			return false;
			case 1:
				if(player.hasItem(cape.skillcapeTrimmedId))
				{
					return true;
				}
			return false;
		}
		return false;
	}
	
	public boolean wearingCape()
	{
		for (SkillCape cape : SkillCape.values())
		{
			if(player.getEquipment().getId(Constants.CAPE) == cape.skillcapeId || player.getEquipment().getId(Constants.CAPE) == cape.skillcapeTrimmedId)
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean wearingCape(int skillId)
	{
		SkillCape cape = SkillCape.forId(skillId);
		if(cape == null)
			return false;
		if(player.getEquipment().getId(Constants.CAPE) == cape.skillcapeId || player.getEquipment().getId(Constants.CAPE) == cape.skillcapeTrimmedId)
		{
			return true;
		}
		return false;
	}
}
