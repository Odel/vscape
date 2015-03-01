package com.rs2.model.content.combat.weapon;

import com.rs2.Constants;
import com.rs2.model.content.skills.magic.Spell;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcDefinition;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.ItemDefinition;
import com.rs2.util.Misc;

//Really basic weapon sounds class
public class CombatSounds {
	
	private Player player;

	public CombatSounds(Player player) {
		this.player = player;
	}
	
	public int getWeaponSound(int weaponId)
	{
		if(weaponId <= 0)
		{
			return 417;
		}
		String weaponName = ItemDefinition.forId(weaponId).getName().toLowerCase();
		if(weaponName.contains("whip")) {
			return 1080;
		} else if(weaponName.contains("scimitar") || weaponName.contains("sword") && !weaponName.contains("2h")) {
			return 396;
		} else if (weaponName.contains("dragon dagger") || weaponName.contains("drag dagger")) {
			return 793;
		} else if (weaponName.contains("2h sword") || weaponName.contains("2h")) {
			return 398;
		} else if (weaponName.contains("halberd")) {
			return 420;
		} else if (weaponName.contains("axe") || weaponName.contains("battleaxe") && !weaponName.contains("throw")) {
			return 399;
		} else if (weaponName.contains("staff")) {
			return 394;
		} else if (weaponName.contains("mace")) {
			return 387;
		} else if (weaponName.contains("dharok")) {
			return 1057;
		} else if (weaponName.contains("torag")) {
			return 1062;
		} else if (weaponName.contains("guthan")) {
			return 1061;
		} else if (weaponName.contains("verac")) {
			return 1059;
		} else if (weaponName.contains("granite maul")) {
			return 1079;
		} else if (weaponName.contains("longbow") || weaponName.contains("shortbow") || weaponName.contains("c'bow")) {
			return 370;
		} else if (weaponName.contains("crystal bow")) {
			return 386;
		}
		return 0;
	}
	
	public void weaponSound(final Player ply)
	{
		int soundId = getWeaponSound(ply.getEquipment().getId(Constants.WEAPON));
		if(soundId > 0)
		{
			player.getActionSender().sendSound(soundId,0,0);
		}
	}
	
	public void blockSound(final Player ply)
	{
		int soundId = 381;
		if(ply.getEquipment().getId(Constants.SHIELD) <= 0) {
			String weaponName = ItemDefinition.forId(ply.getEquipment().getId(Constants.WEAPON)).getName().toLowerCase();
			if (weaponName.contains("2h sword") || weaponName.contains("2h")) {
				soundId = Misc.random(410, 415);
			}
		} else {
			String shieldName = ItemDefinition.forId(ply.getEquipment().getId(Constants.SHIELD)).getName().toLowerCase();
			if (shieldName.contains("shield")) {
				soundId = Misc.random(410, 415);
			}
		}
		player.getActionSender().sendSound(soundId,0,0);
	}
	
	public void damageSound()
	{
		player.getActionSender().sendSound(69,0,0);
	}

	public void npcAttackSound(Npc npc)
	{
		int soundId = 417;
		String npcName = NpcDefinition.forId(npc.getNpcId()).getName().toLowerCase();
		if(npcName.contains("cow"))
		{
			soundId = 4;
		}
		else if(npcName.contains("dog"))
		{
			soundId = 36;
		}
		player.getActionSender().sendSound(soundId,0,0);
	}
	
	public void npcBlockSound(Npc npc)
	{
		int soundId = 381;
		String npcName = NpcDefinition.forId(npc.getNpcId()).getName().toLowerCase();
		if(npcName.contains("cow"))
		{
			soundId = 3;
		}
		else if(npcName.contains("dog"))
		{
			soundId = 34;
		}
		player.getActionSender().sendSound(soundId,0,0);
	}
	
	public void npcDamageSound(Npc npc)
	{
		int soundId = 69;
		String npcName = NpcDefinition.forId(npc.getNpcId()).getName().toLowerCase();
		if(npcName.contains("cow")){
			soundId = 5;
		} else if(npcName.contentEquals("man")) {
			soundId = 72;
		} else if(npcName.contentEquals("woman")) {
			soundId = 73;
		} else if(npcName.contains("dog"))
		{
			soundId = 37;
		}
		player.getActionSender().sendSound(soundId,0,0);
	}
	
	public void npcDeathSound(Npc npc)
	{
		int soundId = 70;
		String npcName = NpcDefinition.forId(npc.getNpcId()).getName().toLowerCase();
		if(npcName.contains("cow")){
			soundId = 5;
		} else if(npcName.contentEquals("man")) {
			soundId = 70;
		} else if(npcName.contentEquals("woman")) {
			soundId = 71;
		} else if(npcName.contains("dog")) {
			soundId = 35;
		}
		player.getActionSender().sendSound(soundId,0,0);
	}
	
	
	public void spellSound(Spell spell, boolean cast)
	{
		int soundId = 0;
		switch(spell)
		{
		case BIND:
			break;
		case BLOOD_BARRAGE:
			break;
		case BLOOD_BLITZ:
			break;
		case BLOOD_BURST:
			break;
		case BLOOD_RUSH:
			break;
		case CLAWS_OF_GUTHIX:
			break;
		case CLAWS_OF_GUTHIX_BATTLE:
			break;
		case CONFUSE:
			soundId = cast ? 99 : 100;
			break;
		case CRUMBLE_UNDEAD:
			break;
		case CURSE:
			break;
		case EARTH_BLAST:
			soundId = cast ? 1007 : 1005;
			break;
		case EARTH_BOLT:
			soundId = cast ? 1003 : 1006;
			break;
		case EARTH_STRIKE:
			soundId = cast ? 1002 : 1004;
			break;
		case EARTH_WAVE:
			soundId = cast ? 1009 : 1008;
			break;
		case ENFEEBLE:
			break;
		case ENTANGLE:
			soundId = cast ? 1012 : 1013;
			break;
		case FIRE_BLAST:
			soundId = cast ? 1020 : 1019;
			break;
		case FIRE_BOLT:
			soundId = cast ? 1015 : 1016;
			break;
		case FIRE_BOLT_ZAMORAK:
			break;
		case FIRE_STRIKE:
			soundId = cast ? 1017 : 1018;
			break;
		case FIRE_WAVE:
			soundId = cast ? 1014 : 1021;
			break;
		case FLAMES_OF_ZAMORAK:
			break;
		case GHORROCK:
			break;
		case IBAN_BLAST:
			break;
		case ICE_BARRAGE:
			soundId = cast ? 1111 : 1125;
			break;
		case ICE_BLITZ:
			soundId = cast ? 1111 : 1110;
			break;
		case ICE_BURST:
			soundId = cast ? 1111 : 1126;
			break;
		case ICE_RUSH:
			soundId = cast ? 1111 : 1110;
			break;
		case MAGIC_DART:
			break;
		case MITHRIL_SPELL:
			break;
		case NECROMANCER:
			break;
		case PADDEWWA:
			break;
		case SARADOMIN_STRIKE:
			break;
		case SENNTISTEN:
			break;
		case SHADOW_BARRAGE:
			break;
		case SHADOW_BLITZ:
			break;
		case SHADOW_BURST:
			break;
		case SHADOW_RUSH:
			break;
		case SMOKE_BARRAGE:
			break;
		case SMOKE_BLITZ:
			break;
		case SMOKE_BURST:
			break;
		case SMOKE_RUSH:
			break;
		case SNARE:
			soundId = cast ? 1010 : 1011;
			break;
		case STUN:
			break;
		case TELEBLOCK:
			break;
		case TELEGRAB:
			break;
		case VULNERABILITY:
			break;
		case WATER_BLAST:
			soundId = cast ? 1026 : 1027;
			break;
		case WATER_BOLT:
			soundId = cast ? 1024 : 1025;
			break;
		case WATER_STRIKE:
			soundId = cast ? 1023 : 1022;
			break;
		case WATER_WAVE:
			soundId = cast ? 1028 : 1029;
			break;
		case WEAKEN:
			break;
		case WIND_BLAST:
			soundId = cast ? 1034 : 1033;
			break;
		case WIND_BOLT:
			soundId = cast ? 1031 : 1033;
			break;
		case WIND_STRIKE:
			soundId = cast ? 1030 : 1032;
			break;
		case WIND_WAVE:
			soundId = cast ? 1031 : 1033;
			break;
		}
		if(soundId > 0)
		{
			player.getActionSender().sendSound(soundId,0,0);
		}
	}
}
