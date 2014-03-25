package com.rs2.model.content.skills;

import com.rs2.model.players.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 22/12/11 Time: 17:15 To change
 * this template use File | Settings | File Templates.
 */
public class SkillsX {
	public static boolean handleXButtons(Player player, int buttonId) {
		switch (buttonId) {
			case 9110 : // bronze x
			case 15148 : // iron x
			case 15152 : // silver x
			case 15156 : // steel x
			case 15160 : // gold x
			case 16062 : // mith x
			case 29018 : // addy x
			case 29023 : // rune x
			case 34182 :
			case 34242 :
			case 34246 :
			case 34250 :
			case 34202 :
			case 34206 :
			case 34210 :
			case 44207 :
			case 44211 :
			case 48109 :
			case 48113 :
			case 48117 :
			case 24056 :
			case 48121 :
			case 34214 :
			case 34254 :
			case 35002 :
			case 6212 :
			case 34186 :
			case 34190 :
			case 34167 :
			case 34171 :
			case 53150 :
            case 57209 :
            case 57210 :
            case 57211 :
            case 57212 :
            case 57213 :
            case 57214 :
            case 57215 :
            case 57216 :
				player.getActionSender().openXInterface(buttonId);
				return true;
		}
		return false;
	}
}
