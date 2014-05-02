package com.rs2.model.region.music;

import com.rs2.model.players.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 17/04/12 Time: 23:56 To change
 * this template use File | Settings | File Templates.
 */
public class RegionMusic {
	public void playMusic(Player player) {
		Music song = getMusicId(player);
		if (song == null) {
			//player.getActionSender().sendSong(2);
			return;
		}
		player.getActionSender().sendSong(song.music);
	}

	private Music getMusicId(Player player) {
		int x = player.getPosition().getX(), y = player.getPosition().getY();
		for (int i = 0; i < songs.length; i++) {
			if ((x >= songs[i].swX && x <= songs[i].neX) && (y >= songs[i].swY && y <= songs[i].neY)) {
				return songs[i];
			}
			else
			{
				//System.out.println("no song");
				//return null;
			}
		}
		return null;
	}

	// Music id, swX, swY, neX, neY
	private final Music[] songs = {new Music(76, 3200, 3199, 3273, 3302), // Harmony
			new Music(2, 3200, 3303, 3273, 3353), // Autumn Voyage
			new Music(111, 3274, 3328, 3315, 3394), // Still Night
			new Music(123, 3274, 3266, 3323, 3327), // Arabian2
			new Music(36, 3274, 3200, 3323, 3265), // Arabian
			new Music(50, 3257, 3112, 3333, 3199), // Alkharid
			new Music(47, 3324, 3200, 3408, 3262), // Duel Arena
			new Music(122, 3324, 3263, 3408, 3285), // Shine
			new Music(541, 3324, 3286, 3408, 3327), // The Enchanter
			// new Music(Id, 3194, 3137, 3256, 3199), // Yesteryear MISSING
			new Music(64, 3136, 3136, 3193, 3199), // Book of Spells
			// new Music(Id, 3074, 3136, 3135, 3199), // Vision MISSING
			new Music(3, 3066, 3200, 3120, 3272), // Unknown Land
			new Music(327, 3121, 3200, 3199, 3268), // Dream
			new Music(163, 3121, 3269, 3199, 3314), // Flute Salad
			new Music(151, 3066, 3273, 3120, 3314), // Start
			new Music(333, 3066, 3315, 3147, 3394), // Spooky
			new Music(116, 3148, 3315, 3199, 3394), // Greatness
			new Music(106, 3200, 3354, 3273, 3394), // Expanse
			new Music(157, 3248, 3395, 3328, 3468), // Medieval
			new Music(125, 3166, 3395, 3247, 3468), // Garden
			new Music(175, 3111, 3395, 3165, 3468), // Spirit
			new Music(177, 3111, 3469, 3264, 3524), // Adventure
			new Music(93, 3265, 3469, 3328, 3524), // Parade
			new Music(48, 3329, 3447, 3418, 3524), // Morytania
			// new Music(Id, 3054, 3058, 3156, 3135), // Newbie Melody MISSING
			// new Music(Id, 2972, 3103, 3014, 3136), // Mudskipper Melody
			// MISSING
			// new Music(Id, 2972, 3137, 3037, 3185), // Tomorrow MISSING
			new Music(35, 2993, 3186, 3065, 3260), // Sea Shanty 2
			// new Music(Id, 2941, 3186, 2992, 3260), // Long Way Home MISSING
			// new Music(Id, 2911, 3196, 2940, 3264), // Emperor MISSING
			new Music(107, 2889, 3265, 2940, 3324), // Miles Away
			new Music(127, 2941, 3261, 3013, 3324), // Nightfall
			new Music(49, 3014, 3261, 3065, 3324), // Wander
			new Music(186, 2880, 3325, 2935, 3394), // Arrival
			new Music(72, 2936, 3325, 3065, 3394), // Fanfare
			new Music(54, 2944, 3395, 3008, 3458), // Scape Soft
			new Music(54, 2944, 3459, 2987, 3474), // Scape Soft
			new Music(150, 3009, 3395, 3065, 3458), // Gnome Theme
			new Music(141, 3066, 3395, 3110, 3450), // Barbarianism
			new Music(23, 2937, 3475, 2987, 3524), // Goblin Village
			new Music(102, 2988, 3459, 3065, 3524), // Alone
			new Music(98, 3066, 3451, 3110, 3524), // Forever
			new Music(34, 2944, 3525, 2991, 3591), // Wonder
			new Music(96, 2992, 3525, 3034, 3555), // Witching
			new Music(96, 2992, 3556, 3124, 3605), // Inspiration
			new Music(182, 3035, 3525, 3124, 3555), // Dangerous
			new Music(169, 3125, 3525, 3264, 3579), // Crystal Sword
			// new Music(Id, 3265, 3525, 3392, 3562), // Shining MISSING
			new Music(121, 3265, 3563, 3392, 3619), // Forbidden
			new Music(113, 2944, 3592, 2991, 3655), // Lightness
			new Music(160, 2992, 3606, 3055, 3655), // Army of Darkness
			new Music(176, 3056, 3606, 3124, 3655), // Undercurrent
			new Music(10, 3125, 3581, 3205, 3655), // Moody
			new Music(179, 3206, 3580, 3264, 3655), // Underground
			new Music(183, 2944, 3656, 3003, 3722), // Troubled
			new Music(66, 3004, 3656, 3064, 3722), // Legion
			new Music(476, 3065, 3656, 3126, 3722), // Dead Can Dance
			new Music(43, 3127, 3656, 3197, 3714), // Wilderness3
			new Music(8, 3198, 3656, 3264, 3702), // Wildwood
			new Music(337, 3265, 3621, 3392, 3716), // Faithless
			// new Music(Id, 2944, 3723, 3047, 3799), // Wilderness4 UNKNOWN
			new Music(435, 3048, 3723, 3126, 3799), // Wilderness
			new Music(68, 3127, 3715, 3197, 3758), // Cavern
			new Music(332, 3198, 3704, 3264, 3758), // Scape Wild
			new Music(182, 3265, 3717, 3392, 3842), // Dangerous
			// new Music(Id, 3127, 3759, 3264, 3842), // Wolf Mountain UNKNOWN
			new Music(37, 2944, 3800, 3003, 3903), // Deep Wildy
			// new Music(Id, 3004, 3800, 3126, 3903), // Wilderness2 MISSING
			// new Music(Id, 3127, 3843, 3211, 3903), // Close Quarters MISSING
			new Music(331, 3212, 3843, 3392, 3903), // Scape Sad
			new Music(52, 2944, 3904, 3009, 3969), // Serene
			// new Music(Id, 3010, 3904, 3076, 3969), // Pirates of Peril
			// MISSING
			new Music(13, 3077, 3905, 3139, 3969), // Mage Arena
			// new Music(Id, 3140, 3905, 3211, 3969), // Expecting MISSING
			// new Music(Id, 3212, 3905, 3392, 3967), // Regal MISSING
			new Music(92, 2891, 3133, 2940, 3195), // Sea Shanty (PART 1)
			new Music(92, 2941, 3133, 2971, 3185), // Sea Shanty (PART 2)
			new Music(172, 2817, 3133, 2890, 3208), // Jungle Island
			new Music(314, 2366, 3069, 2446, 3136) // Castlewars
	};
}