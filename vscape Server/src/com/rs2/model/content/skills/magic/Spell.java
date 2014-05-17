package com.rs2.model.content.skills.magic;

import com.rs2.Constants;
import com.rs2.model.Graphic;
import com.rs2.model.content.combat.effect.Effect;
import com.rs2.model.content.combat.effect.impl.BindingEffect;
import com.rs2.model.content.combat.effect.impl.LeechEffect;
import com.rs2.model.content.combat.effect.impl.PoisonEffect;
import com.rs2.model.content.combat.effect.impl.StatEffect;
import com.rs2.model.content.combat.effect.impl.UnequipEffect;
import com.rs2.model.content.combat.effect.impl.SummonNpc;
import com.rs2.model.content.combat.effect.impl.TbEffect;
import com.rs2.model.content.combat.hit.HitDef;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.combat.projectile.ProjectileDef;
import com.rs2.model.content.combat.projectile.ProjectileTrajectory;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.item.Item;

/**
 *
 */

public enum Spell {
	WIND_STRIKE(1, 711, Graphic.highGraphic(90), 5.5, new Item[]{new Item(556), new Item(558)}, magicHit(2, magicProjectile(91), Graphic.highGraphic(92)), true), WATER_STRIKE(5, 711, Graphic.highGraphic(93), 7.5, new Item[]{new Item(556), new Item(558), new Item(555)}, magicHit(4, magicProjectile(94), Graphic.highGraphic(95)), true), EARTH_STRIKE(9, 711, Graphic.highGraphic(96), 9.5, new Item[]{new Item(556), new Item(558), new Item(557, 2)}, magicHit(6, magicProjectile(97), Graphic.highGraphic(98)), true), FIRE_STRIKE(13, 711, Graphic.highGraphic(99), 11.5, new Item[]{new Item(556, 2), new Item(558), new Item(554, 3)}, magicHit(8, magicProjectile(100), Graphic.highGraphic(101)), true), AIR_BOLT(17, 711, Graphic.highGraphic(117), 13.5, new Item[]{new Item(556, 2), new Item(562)}, magicHit(9, magicProjectile(118), Graphic.highGraphic(119)), true), WATER_BOLT(23, 711, Graphic.highGraphic(120), 16.5, new Item[]{new Item(556, 2), new Item(562), new Item(555, 2)}, magicHit(10,
			magicProjectile(121), Graphic.highGraphic(122)), true), EARTH_BOLT(29, 711, Graphic.highGraphic(123), 19.5, new Item[]{new Item(556, 2), new Item(562), new Item(557, 3)}, magicHit(11, magicProjectile(124), Graphic.highGraphic(125)), true), FIRE_BOLT(35, 711, Graphic.highGraphic(126), 21.5, new Item[]{new Item(556, 3), new Item(562), new Item(554, 4)}, magicHit(12, magicProjectile(127), Graphic.highGraphic(128)), true), AIR_BLAST(41, 711, Graphic.highGraphic(132), 25.5, new Item[]{new Item(556, 3), new Item(560)}, magicHit(13, magicProjectile(133), Graphic.highGraphic(134)), true), WATER_BLAST(47, 711, Graphic.highGraphic(135), 28.5, new Item[]{new Item(556, 3), new Item(560), new Item(555, 3)}, magicHit(14, magicProjectile(136), Graphic.highGraphic(137)), true), EARTH_BLAST(53, 711, Graphic.highGraphic(138), 31.5, new Item[]{new Item(556, 3), new Item(560), new Item(557, 4)}, magicHit(15, magicProjectile(139), Graphic.highGraphic(140)), true), FIRE_BLAST(59, 711, Graphic
			.highGraphic(129), 34.5, new Item[]{new Item(556, 4), new Item(560), new Item(554, 4)}, magicHit(16, magicProjectile(130), Graphic.highGraphic(131)), true), AIR_WAVE(62, 711, Graphic.highGraphic(158), 36, new Item[]{new Item(556, 5), new Item(565)}, magicHit(17, magicProjectile(159), Graphic.highGraphic(160)), true), WATER_WAVE(65, 711, Graphic.highGraphic(161), 37.5, new Item[]{new Item(556, 5), new Item(565), new Item(555, 7)}, magicHit(18, magicProjectile(162), Graphic.highGraphic(163)), true), EARTH_WAVE(70, 711, Graphic.highGraphic(164), 40, new Item[]{new Item(556, 5), new Item(565), new Item(557, 7)}, magicHit(19, magicProjectile(165), Graphic.highGraphic(166)), true), FIRE_WAVE(75, 711, Graphic.highGraphic(155), 42.5, new Item[]{new Item(556, 5), new Item(565), new Item(554, 7)}, magicHit(20, magicProjectile(156), Graphic.highGraphic(157)), true),
	// Ancients
	SMOKE_RUSH(50, 1978, null, 31, new Item[]{new Item(562, 2), new Item(560, 2), new Item(554), new Item(556)}, magicHit(14, magicProjectile(386), Graphic.lowGraphic(387)), true, null, new PoisonEffect(2)), SHADOW_RUSH(52, 1978, null, 31, new Item[]{new Item(562, 2), new Item(560, 2), new Item(556), new Item(566)}, magicHit(14, magicProjectile(378), Graphic.lowGraphic(379)), true, null, new StatEffect(Skill.ATTACK, .1)), BLOOD_RUSH(56, 1978, null, 33, new Item[]{new Item(562, 2), new Item(560, 2), new Item(565)}, magicHit(15, magicProjectile(372), Graphic.lowGraphic(373)), true, null, new LeechEffect(.4)), ICE_RUSH(58, 1978, null, 34, new Item[]{new Item(562, 2), new Item(560, 2), new Item(555, 2)}, magicHit(16, magicProjectile(360), Graphic.lowGraphic(361)), true, null, new BindingEffect(8)), SMOKE_BURST(62, 1979, null, 36, new Item[]{new Item(562, 4), new Item(560, 2), new Item(554, 2), new Item(556, 2)}, magicHit(17, magicProjectile(-1), Graphic.lowGraphic(390)), true, null,
			new PoisonEffect(2)), SHADOW_BURST(64, 1979, null, 37, new Item[]{new Item(562, 4), new Item(560, 2), new Item(556, 2), new Item(566, 2)}, magicHit(17, magicProjectile(-1), Graphic.lowGraphic(382)), true, null, new StatEffect(Skill.ATTACK, .1)), BLOOD_BURST(68, 1979, null, 39, new Item[]{new Item(562, 4), new Item(560, 2), new Item(565, 2)}, magicHit(21, magicProjectile(-1), Graphic.lowGraphic(376)), true, null, new LeechEffect(.4)), ICE_BURST(70, 1979, null, 40, new Item[]{new Item(562, 4), new Item(560, 2), new Item(555, 4)}, magicHit(22, magicProjectile(-1), Graphic.lowGraphic(363)), true, null, new BindingEffect(17)), SMOKE_BLITZ(74, 1978, null, 42, new Item[]{new Item(560, 2), new Item(565, 2), new Item(554, 2), new Item(556, 2)}, magicHit(23, magicProjectile(386), Graphic.lowGraphic(387)), true, null, new PoisonEffect(4)), SHADOW_BLITZ(76, 1978, null, 43, new Item[]{new Item(560, 2), new Item(565, 2), new Item(556, 2), new Item(566, 2)}, magicHit(24,
			magicProjectile(380), Graphic.lowGraphic(381)), true, null, new StatEffect(Skill.ATTACK, .1)), BLOOD_BLITZ(80, 1978, null, 45, new Item[]{new Item(560, 2), new Item(565, 4)}, magicHit(25, magicProjectile(374), Graphic.lowGraphic(375)), true, null, new LeechEffect(.4)), ICE_BLITZ(82, 1978, Graphic.highGraphic(366), 46, new Item[]{new Item(560, 2), new Item(565, 2), new Item(555, 3)}, magicHit(26, magicProjectile(-1), Graphic.lowGraphic(367)).setStartingHitDelay(3)/*.setDamageDelay(1)*/, true, null, new BindingEffect(25)), SMOKE_BARRAGE(86, 1979, null, 48, new Item[]{new Item(560, 4), new Item(565, 2), new Item(554, 4), new Item(556, 4)}, magicHit(27, magicProjectile(-1), Graphic.lowGraphic(391)), true, null, new PoisonEffect(4)), SHADOW_BARRAGE(88, 1979, null, 48, new Item[]{new Item(560, 4), new Item(565, 2), new Item(556, 2), new Item(556, 3)}, magicHit(28, magicProjectile(-1), Graphic.lowGraphic(383)), true, null, new StatEffect(Skill.ATTACK, .15)), BLOOD_BARRAGE(92, 1979,
			null, 51, new Item[]{new Item(560, 4), new Item(565, 4), new Item(566)}, magicHit(29, magicProjectile(-1), Graphic.lowGraphic(377)), true, null, new LeechEffect(.4)), ICE_BARRAGE(94, 1979, null, 52, new Item[]{new Item(560, 4), new Item(565, 2), new Item(555, 6)}, magicHit(30, magicProjectile(-1), Graphic.lowGraphic(369)), true, null, new BindingEffect(33)),

	// ancient teleports
	PADDEWWA(54, 1979, Graphic.lowGraphic(392), 64, new Item[]{new Item(563, 2), new Item(554, 1), new Item(556, 1)}), SENNTISTEN(60, 1979, Graphic.lowGraphic(392), 70, new Item[]{new Item(563, 2), new Item(566, 1)}), KHARYRLL(66, 1979, Graphic.lowGraphic(392), 76, new Item[]{new Item(563, 2), new Item(565, 1)}), LASSAR(72, 1979, Graphic.lowGraphic(392), 82, new Item[]{new Item(563, 2), new Item(555, 4)}), DAREEYAK(78, 1979, Graphic.lowGraphic(392), 88, new Item[]{new Item(563, 2), new Item(554, 3), new Item(556, 2)}), CARRALLANGAR(84, 1979, Graphic.lowGraphic(392), 94, new Item[]{new Item(563, 2), new Item(566, 2)}), ANNAKARL(90, 1979, Graphic.lowGraphic(392), 100, new Item[]{new Item(563, 1), new Item(565, 1)}), GHORROCK(96, 1979, Graphic.lowGraphic(392), 106, new Item[]{new Item(563, 2), new Item(565, 8)}),

	// special spells
	IBAN_BLAST(50, 708, Graphic.highGraphic(87), 61, new Item[]{new Item(554, 5), new Item(560)}, magicHit(25, magicProjectile(88), Graphic.highGraphic(89)), true), CONFUSE(3, 716, Graphic.highGraphic(102), 13, new Item[]{new Item(555, 3), new Item(557, 2), new Item(559, 1)}, magicHit(-1, magicProjectile(103), Graphic.highGraphic(104)), true, new StatEffect(Skill.ATTACK, .05), null), WEAKEN(11, 716, Graphic.highGraphic(105), 21, new Item[]{new Item(555, 3), new Item(557, 2), new Item(559, 1)}, magicHit(-1, magicProjectile(106), Graphic.highGraphic(107)), true, new StatEffect(Skill.STRENGTH, .05), null), CURSE(19, 717, Graphic.highGraphic(108), 29, new Item[]{new Item(555, 2), new Item(557, 3), new Item(559, 1)}, magicHit(-1, magicProjectile(109), Graphic.highGraphic(110)), true, new StatEffect(Skill.DEFENCE, .05), null), VULNERABILITY(66, 729, Graphic.highGraphic(167), 76, new Item[]{new Item(557, 5), new Item(555, 5), new Item(566, 2)}, magicHit(-1, magicProjectile(168),
			Graphic.highGraphic(169)), true, new StatEffect(Skill.DEFENCE, .1), null), ENFEEBLE(73, 729, Graphic.highGraphic(170), 89, new Item[]{new Item(557, 8), new Item(555, 8), new Item(566, 2)}, magicHit(-1, magicProjectile(171), Graphic.highGraphic(172)), true, new StatEffect(Skill.STRENGTH, .1), null), STUN(80, 729, Graphic.highGraphic(173), 90, new Item[]{new Item(557, 12), new Item(555, 12), new Item(566, 2)}, magicHit(-1, magicProjectile(174), Graphic.highGraphic(107)), true, new StatEffect(Skill.ATTACK, .1), null), BIND(20, 711, Graphic.highGraphic(177), 30, new Item[]{new Item(557, 3), new Item(555, 3), new Item(561, 2)}, magicHit(-1, magicProjectile(178), Graphic.highGraphic(181)), true, new BindingEffect(8), null), SNARE(50, 711, Graphic.highGraphic(177), 60, new Item[]{new Item(557, 4), new Item(555, 4), new Item(561, 2)}, magicHit(2, magicProjectile(178), Graphic.highGraphic(180)), true, new BindingEffect(17), null), ENTANGLE(79, 711, Graphic.highGraphic(177), 90,
			new Item[]{new Item(557, 5), new Item(555, 5), new Item(561, 2)}, magicHit(5, magicProjectile(178), Graphic.highGraphic(179)), true, new BindingEffect(25), null), CRUMBLE_UNDEAD(39, 724, Graphic.highGraphic(146), 24.5, new Item[]{new Item(556, 2), new Item(557, 2), new Item(562, 1)}, magicHit(15, magicProjectile(146), Graphic.highGraphic(147)), true), MAGIC_DART(50, 1576, Graphic.highGraphic(327), 30, new Item[]{new Item(560, 1), new Item(558, 4)}, magicHit(10, magicProjectile(328), Graphic.highGraphic(329)), true), SARADOMIN_STRIKE(50, 811, null, 60, new Item[]{new Item(554, 2), new Item(565, 2), new Item(556, 4)}, magicHit(20, null, Graphic.highGraphic(76)), true), CLAWS_OF_GUTHIX(60, 811, null, 60, new Item[]{new Item(554, 1), new Item(565, 2), new Item(556, 4)}, magicHit(20, null, Graphic.highGraphic(77)), true), FLAMES_OF_ZAMORAK(60, 811, null, 60, new Item[]{new Item(554, 4), new Item(565, 2), new Item(556, 1)}, magicHit(20, null, Graphic.lowGraphic(78)), true), TELEBLOCK(
			85, 1819, null, 85, new Item[]{new Item(563, 1), new Item(562, 1), new Item(560, 1)}, magicHit(-1, magicProjectile(344), Graphic.lowGraphic(345)), true, new TbEffect(500), null),

	// misc spells
	CHARGE(80, 811, Graphic.highGraphic(301), 180, new Item[]{new Item(554, 3), new Item(565, 3), new Item(556, 3)}), LOW_ALCH(21, 712, Graphic.highGraphic(112), 31, new Item[]{new Item(554, 3), new Item(561, 1)}), HIGH_ALCH(55, 713, Graphic.highGraphic(113), 65, new Item[]{new Item(554, 5), new Item(561, 1)}), TELEGRAB(33, 728, Graphic.highGraphic(142), 43, new Item[]{new Item(556, 1), new Item(563, 1)}, magicHit(-1, magicProjectile(143), new Graphic(144, 0)), false), CHARGE_WATER(56, 726, Graphic.highGraphic(149), 56, new Item[]{new Item(555, 3), new Item(564, 3)}), CHARGE_EARTH(60, 726, Graphic.highGraphic(150), 70, new Item[]{new Item(557, 3), new Item(564, 3)}), CHARGE_FIRE(63, 726, Graphic.highGraphic(151), 73, new Item[]{new Item(554, 3), new Item(564, 3)}), CHARGE_AIR(66, 726, Graphic.highGraphic(152), 75, new Item[]{new Item(556, 3), new Item(564, 3)}), BONES_TO_BANANA(15, 722, Graphic.highGraphic(141), 25, new Item[]{new Item(557, 2), new Item(555, 2), new Item(561)}), SUPERHEAT(
			43, 725, Graphic.highGraphic(148), 53, new Item[]{new Item(554, 4), new Item(561, 1)}), BONES_TO_PEACH(60, 722, Graphic.highGraphic(311), 36, new Item[]{new Item(561, 2), new Item(555, 4), new Item(557, 4)}),

	ENCHANT_LV_1(7, 719, Graphic.highGraphic(114), 7, new Item[]{new Item(555, 1), new Item(564)}), ENCHANT_LV_2(27, 719, Graphic.highGraphic(114), 27, new Item[]{new Item(556, 3), new Item(564)}), ENCHANT_LV_3(49, 720, Graphic.highGraphic(115), 49, new Item[]{new Item(554, 5), new Item(564)}), ENCHANT_LV_4(57, 720, Graphic.highGraphic(115), 57, new Item[]{new Item(557, 10), new Item(564)}), ENCHANT_LV_5(68, 721, Graphic.highGraphic(116), 68, new Item[]{new Item(557, 15), new Item(555, 15), new Item(564)}), ENCHANT_LV_6(87, 721, Graphic.highGraphic(452), 87, new Item[]{new Item(557, 20), new Item(554, 20), new Item(564)}),

	TELEOTHER_LUMBRIDGE(74, 1818, Graphic.highGraphic(343), 84, new Item[]{new Item(566), new Item(563), new Item(557)}), TELEOTHER_FALADOR(82, 1818, Graphic.highGraphic(343), 92, new Item[]{new Item(566), new Item(563), new Item(555)}), TELEOTHER_CAMELOT(90, 1818, Graphic.highGraphic(343), 90, new Item[]{new Item(566, 2), new Item(563)}),

	// teleport spells
	VARROCK(25, 714, Graphic.highGraphic(301), 35, new Item[]{new Item(563), new Item(556, 3), new Item(554, 1)}), LUMBRIDGE(31, 714, Graphic.highGraphic(301), 41, new Item[]{new Item(563), new Item(556, 3), new Item(557, 1)}), FALADOR(37, 714, Graphic.highGraphic(301), 47, new Item[]{new Item(563), new Item(556, 3), new Item(555, 1)}), CAMELOT(45, 714, Graphic.highGraphic(301), 55, new Item[]{new Item(563), new Item(556, 5)}), ARDOUGNE(51, 714, Graphic.highGraphic(301), 61, new Item[]{new Item(563, 2), new Item(555, 2)}), WATCHTOWER(58, 714, Graphic.highGraphic(301), 68, new Item[]{new Item(563, 2), new Item(557, 2)}), TROLLHEIM(61, 714, Graphic.highGraphic(301), 68, new Item[]{new Item(563, 2), new Item(554, 2)}), APE_ATOLL(64, 714, Graphic.highGraphic(301), 74, new Item[]{new Item(563, 2), new Item(554, 2), new Item(555, 2), new Item(1963, 1)}),

	//npc spells
	SPINOLYP(2868, magicHit(10, magicProjectile(94), Graphic.highGraphic(95)), true, new StatEffect(5, 1)), 
	WALLASALKI(2365, magicHit(30, magicProjectile(162), Graphic.highGraphic(163)), true),
	PRIME(2853, magicHit(61, magicProjectile(162), Graphic.highGraphic(163)), true),
	NECROMANCER(811, magicHit(0, null, null), true, new SummonNpc(77)),
	TORCHER(3880, magicHit(5, magicProjectile(660), Graphic.highGraphic(101)), true),
	CHAOSMAIN(3146, magicHit(26, magicProjectile(557), Graphic.highGraphic(556)), true),
	CHAOSDISARM(3146, magicHit(0, magicProjectile(551), Graphic.highGraphic(550)), true, new UnequipEffect()),
	;

	private int levelRequired, animation;
	private Graphic graphic;
	private double expEarned;
	private Item[] runesRequired;
	private HitDef hitDef;
	private boolean combatSpell;

	@SuppressWarnings("rawtypes")
	private Effect requiredEffect, additionalEffect;

	Spell(int animation, HitDef hitDef, boolean combatSpell) {
		this(0, animation, null, 0, null, hitDef, combatSpell, null, null);
	}

	@SuppressWarnings("rawtypes")
	Spell(int animation, HitDef hitDef, boolean combatSpell, Effect effect) {
		this(0, animation, null, 0, null, hitDef, combatSpell, null, effect);
	}

	Spell(int levelRequired, int animation, Graphic graphic, double expEarned, Item[] runesRequired) {
		this(levelRequired, animation, graphic, expEarned, runesRequired, null, false);
	}

	Spell(int levelRequired, int animation, Graphic graphic, double expEarned, Item[] runesRequired, HitDef hitDef, boolean combatSpell) {
		this(levelRequired, animation, graphic, expEarned, runesRequired, hitDef, combatSpell, null, null);
	}

	@SuppressWarnings("rawtypes")
	Spell(int levelRequired, int animation, Graphic graphic, double expEarned, Item[] runesRequired, HitDef hitDef, boolean combatSpell, Effect requiredEffect, Effect additionalEffect) {
		this.levelRequired = levelRequired;
		this.animation = animation;
		this.graphic = graphic;
		this.expEarned = expEarned;
		this.runesRequired = runesRequired;
		this.hitDef = hitDef;
		this.combatSpell = combatSpell;
		this.requiredEffect = requiredEffect;
		this.additionalEffect = additionalEffect;
	}

	public boolean isCombatSpell() {
		return combatSpell;
	}

	public int getLevelRequired() {
		return levelRequired;
	}

	public int getAnimation() {
		return animation;
	}

	public double getExpEarned() {
		return expEarned;
	}

	public Item[] getRunesRequired() {
		return runesRequired;
	}

	public HitDef getHitDef() {
		return hitDef;
	}

	public Graphic getGraphic() {
		return graphic;
	}

	private static ProjectileDef magicProjectile(int projectileId) {
		return new ProjectileDef(projectileId, ProjectileTrajectory.SPELL);
	}

	private static HitDef magicHit(int maxDamage, ProjectileDef projectileDef, Graphic hitGraphic) {
		return magicHit(maxDamage, projectileDef, hitGraphic, 0);
	}

	private static HitDef magicHit(int maxDamage, ProjectileDef projectileDef, Graphic hitGraphic, int startingHitDelay) {
		return new HitDef(Constants.MAGIC_STYLE, HitType.NORMAL, maxDamage).setProjectile(projectileDef).setHitGraphic(hitGraphic).setStartingHitDelay(startingHitDelay);
	}

	@SuppressWarnings("rawtypes")
	public Effect getRequiredEffect() {
		return requiredEffect;
	}

	@SuppressWarnings("rawtypes")
	public Effect getAdditionalEffect() {
		return additionalEffect;
	}

}
