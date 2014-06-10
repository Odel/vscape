package com.rs2.model.content.skills.magic;

import java.util.HashMap;
import java.util.Map;

import com.rs2.model.players.Player;

/**
 *
 */
public enum SpellBook {

	MODERN(modernSpells(), modernAutoSpells()), ANCIENT(ancientSpells(), ancientAutoSpells());

	private Map<Integer, Spell> spells, autoSpells;

	SpellBook(Map<Integer, Spell> spells, Map<Integer, Spell> autoSpells) {
		this.spells = spells;
		this.autoSpells = autoSpells;
	}

	public static Spell getSpell(Player player, int id) {
		return player.getMagicBookType().spells.get(id);
	}

	public static Spell getAutoSpell(Player player, int id) {
		return player.getMagicBookType().autoSpells.get(id);
	}

	private static Map<Integer, Spell> modernAutoSpells() {
		Map<Integer, Spell> spells = new HashMap<Integer, Spell>();
		spells.put(7038, Spell.WIND_STRIKE);
		spells.put(7039, Spell.WATER_STRIKE);
		spells.put(7040, Spell.EARTH_STRIKE);
		spells.put(7041, Spell.FIRE_STRIKE);
		spells.put(7042, Spell.AIR_BOLT);
		spells.put(7043, Spell.WATER_BOLT);
		spells.put(7044, Spell.EARTH_BOLT);
		spells.put(7045, Spell.FIRE_BOLT);
		spells.put(7046, Spell.AIR_BLAST);
		spells.put(7047, Spell.WATER_BLAST);
		spells.put(7048, Spell.EARTH_BLAST);
		spells.put(7049, Spell.FIRE_BLAST);
		spells.put(7050, Spell.AIR_WAVE);
		spells.put(7051, Spell.WATER_WAVE);
		spells.put(7052, Spell.EARTH_WAVE);
		spells.put(7053, Spell.FIRE_WAVE);
		//slayer staff
		spells.put(47019, Spell.MAGIC_DART);
		spells.put(47020, Spell.CRUMBLE_UNDEAD);
		spells.put(47021, Spell.AIR_WAVE);
		spells.put(47022, Spell.WATER_WAVE);
		spells.put(47023, Spell.EARTH_WAVE);
		spells.put(47024, Spell.FIRE_WAVE);
		return spells;
	}

	private static Map<Integer, Spell> ancientAutoSpells() {
		Map<Integer, Spell> spells = new HashMap<Integer, Spell>();
		spells.put(51133, Spell.SMOKE_RUSH);
		spells.put(51185, Spell.SHADOW_RUSH);
		spells.put(51091, Spell.BLOOD_RUSH);
		spells.put(24018, Spell.ICE_RUSH);
		spells.put(51159, Spell.SMOKE_BURST);
		spells.put(51211, Spell.SHADOW_BURST);
		spells.put(51111, Spell.BLOOD_BURST);
		spells.put(51069, Spell.ICE_BURST);
		spells.put(51146, Spell.SMOKE_BLITZ);
		spells.put(51198, Spell.SHADOW_BLITZ);
		spells.put(51102, Spell.BLOOD_BLITZ);
		spells.put(51058, Spell.ICE_BLITZ);
		spells.put(51172, Spell.SMOKE_BARRAGE);
		spells.put(51224, Spell.SHADOW_BARRAGE);
		spells.put(51122, Spell.BLOOD_BARRAGE);
		spells.put(51080, Spell.ICE_BARRAGE);
		return spells;
	}

	private static Map<Integer, Spell> modernSpells() {
		Map<Integer, Spell> spells = new HashMap<Integer, Spell>();
		spells.put(1168, Spell.TELEGRAB);
		spells.put(1152, Spell.WIND_STRIKE);
		spells.put(1154, Spell.WATER_STRIKE);
		spells.put(1156, Spell.EARTH_STRIKE);
		spells.put(1158, Spell.FIRE_STRIKE);
		spells.put(1160, Spell.AIR_BOLT);
		spells.put(1163, Spell.WATER_BOLT);
		spells.put(1166, Spell.EARTH_BOLT);
		spells.put(1169, Spell.FIRE_BOLT);
		spells.put(1172, Spell.AIR_BLAST);
		spells.put(1175, Spell.WATER_BLAST);
		spells.put(1177, Spell.EARTH_BLAST);
		spells.put(1181, Spell.FIRE_BLAST);
		spells.put(1183, Spell.AIR_WAVE);
		spells.put(1185, Spell.WATER_WAVE);
		spells.put(1188, Spell.EARTH_WAVE);
		spells.put(1189, Spell.FIRE_WAVE);
		spells.put(1153, Spell.CONFUSE);
		spells.put(1157, Spell.WEAKEN);
		spells.put(1161, Spell.CURSE);
		spells.put(1542, Spell.VULNERABILITY);
		spells.put(1543, Spell.ENFEEBLE);
		spells.put(1562, Spell.STUN);
		spells.put(1572, Spell.BIND);
		spells.put(1582, Spell.SNARE);
		spells.put(1592, Spell.ENTANGLE);
		spells.put(1171, Spell.CRUMBLE_UNDEAD);
		spells.put(1539, Spell.IBAN_BLAST);
		spells.put(12037, Spell.MAGIC_DART);
		spells.put(1190, Spell.SARADOMIN_STRIKE);
		spells.put(1191, Spell.CLAWS_OF_GUTHIX);
		spells.put(1192, Spell.FLAMES_OF_ZAMORAK);
		spells.put(12445, Spell.TELEBLOCK);
		spells.put(4135, Spell.BONES_TO_BANANA);
		spells.put(62005, Spell.BONES_TO_PEACH);
		spells.put(1162, Spell.LOW_ALCH);
		spells.put(1178, Spell.HIGH_ALCH);
		spells.put(1179, Spell.CHARGE_WATER);
		spells.put(1182, Spell.CHARGE_EARTH);
		spells.put(1184, Spell.CHARGE_FIRE);
		spells.put(1186, Spell.CHARGE_AIR);
		spells.put(1173, Spell.SUPERHEAT);
		spells.put(1155, Spell.ENCHANT_LV_1);
		spells.put(1165, Spell.ENCHANT_LV_2);
		spells.put(1176, Spell.ENCHANT_LV_3);
		spells.put(1180, Spell.ENCHANT_LV_4);
		spells.put(1187, Spell.ENCHANT_LV_5);
		spells.put(6003, Spell.ENCHANT_LV_6);
		spells.put(4169, Spell.CHARGE);
		spells.put(12425, Spell.TELEOTHER_LUMBRIDGE);
		spells.put(12435, Spell.TELEOTHER_FALADOR);
		spells.put(12455, Spell.TELEOTHER_CAMELOT);
		spells.put(4140, Spell.VARROCK);
		spells.put(4143, Spell.LUMBRIDGE);
		spells.put(4146, Spell.FALADOR);
		spells.put(4150, Spell.CAMELOT);
		spells.put(6004, Spell.ARDOUGNE);
		spells.put(6005, Spell.WATCHTOWER);
		spells.put(29031, Spell.TROLLHEIM);
		spells.put(72038, Spell.APE_ATOLL);
		return spells;
	}

	private static Map<Integer, Spell> ancientSpells() {
		Map<Integer, Spell> spells = new HashMap<Integer, Spell>();
		spells.put(12939, Spell.SMOKE_RUSH);
		spells.put(12987, Spell.SHADOW_RUSH);
		spells.put(12901, Spell.BLOOD_RUSH);
		spells.put(12861, Spell.ICE_RUSH);
		spells.put(12963, Spell.SMOKE_BURST);
		spells.put(13011, Spell.SHADOW_BURST);
		spells.put(12919, Spell.BLOOD_BURST);
		spells.put(12881, Spell.ICE_BURST);
		spells.put(12951, Spell.SMOKE_BLITZ);
		spells.put(12999, Spell.SHADOW_BLITZ);
		spells.put(12911, Spell.BLOOD_BLITZ);
		spells.put(12871, Spell.ICE_BLITZ);
		spells.put(12975, Spell.SMOKE_BARRAGE);
		spells.put(13023, Spell.SHADOW_BARRAGE);
		spells.put(12929, Spell.BLOOD_BARRAGE);
		spells.put(12891, Spell.ICE_BARRAGE);
		spells.put(50235, Spell.PADDEWWA);
		spells.put(50245, Spell.SENNTISTEN);
		spells.put(50253, Spell.KHARYRLL);
		spells.put(51005, Spell.LASSAR);
		spells.put(51013, Spell.DAREEYAK);
		spells.put(51023, Spell.CARRALLANGAR);
		spells.put(51031, Spell.ANNAKARL);
		spells.put(51039, Spell.GHORROCK);
		return spells;
	}

	public Map<Integer, Spell> getSpells() {
		return spells;
	}
}
