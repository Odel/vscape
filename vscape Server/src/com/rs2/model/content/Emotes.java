package com.rs2.model.content;

import com.rs2.model.content.skills.Woodcutting.Canoe.CanoeTravelData;
import com.rs2.model.players.Player;

/**
 * By Mikey` of Rune-Server
 */
public class Emotes {

	private Player player;

	public Emotes(Player player) {
		this.player = player;
	}

	public enum EMOTE
	{
		YES(855, 168),
		NO(856, 169),
		THINK(857, 162),
		BOW(858, 164),
		ANGRY(859, 165),
		CRY(860, 161),
		LAUGTH(861, 170),
		CHEER(862, 171),
		WAVE(863, 163),
		BECKON(864, 167),
		CLAP(865, 172),
		DANCE(866, 166),
		PANIC(2105, 52050),
		JIG(2106, 52051),
		SPIN(2107, 52052),
		HEADBANG(2108, 52053),
		JOYJUMP(2109, 52054),
		RASPBERRY(2110, 52055),
		YAWN(2111, 52056),
		SALUTE(2112, 52057),
		SHRUG(2113, 52058),
		BLOWKISS(0x558, 43092, 574),
		GLASSBOX(1131, 2155),
		CLIMBROPE(1130, 25103),
		LEAN(1129, 25106),
		GLASSWALL(1128, 2154),
		GOBLINBOW(2127, 52071),
		GOBLINDANCE(2128, 52072),
		SCARED(2836, 59062),
		ZOMBIEWALK(3544, 72032),
		ZOMBIEDANCE(3543, 72033),
		RABBITHOP(3866, 72254);
		
		private int emoteId;
		private int buttonId;
		private int graphicId;
		
		EMOTE(int emoteId, int buttonId) {
			this.emoteId = emoteId;
			this.buttonId = buttonId;
		}
		
		EMOTE(int emoteId, int buttonId, int graphicId) {
			this.emoteId = emoteId;
			this.buttonId = buttonId;
			this.graphicId = graphicId;
		}
		
		public static EMOTE forButtonId(int button) {
			for (EMOTE emote : EMOTE.values())
					if (button == emote.buttonId)
						return emote;
			return null;
		}
	}

	public void performEmote(int emoteId) {
		player.getUpdateFlags().sendAnimation(emoteId, 0);
	}
	
	public void performEmote(int emoteId, int graphicId) {
		player.getMovementHandler().reset();
		player.getUpdateFlags().sendAnimation(emoteId, 0);
		if(graphicId > 0)
		{
			player.getUpdateFlags().sendGraphic(graphicId);
		}
	}

	public boolean activateEmoteButton(int buttonId) {
		EMOTE emote = EMOTE.forButtonId(buttonId);
		if(emote != null)
		{
			performEmote(emote.emoteId,emote.graphicId);
			return true;
		}
		return false;
	}
	
	public boolean isEmote(int buttonId)
	{
		EMOTE emote = EMOTE.forButtonId(buttonId);
		if(emote != null)
		{
			return true;
		}
		return false;
	}

}
