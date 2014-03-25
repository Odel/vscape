package com.rs2.model.content.skills.fishing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.npcs.Npc;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;

public class FishingSpots {

	/**
	 * A map of all the FishingSpots we have in the game.
	 */
	static final Map<Position, Npc> FISHING_SPOTS = new HashMap<Position, Npc>();

	/**
	 * This is basically a Position set for each "fishing place" (Barbarian,
	 * Draynor, two spots in Camelot, Karamja, etc)
	 */
	static final Position[][] FISHING_SPOT_Position_SETS = {{new Position(3101, 3092, 0), new Position(3103, 3092)}, // tutorial
			// island
	
			{new Position(3104, 3424, 0), new Position(3104, 3425, 0), new Position(3110, 3432, 0), new Position(3110, 3433, 0)}, // Barbarian
																																	// Village
			{new Position(3085, 3230, 0), new Position(3085, 3231, 0), new Position(3086, 3227, 0)}, // Draynor
																										// Village
			{new Position(3239, 3244, 0), new Position(3238, 3252, 0)},// lumbridge
			{new Position(2921, 3178, 0), new Position(2923, 3179, 0), new Position(2923, 3180, 0), new Position(2924, 3181, 0), new Position(2926, 3180, 0), new Position(2926, 3179, 0), new Position(2926, 3176, 0)}, // Karamja
			{new Position(2838, 3431, 0), new Position(2837, 3431, 0), new Position(2836, 3431, 0), new Position(2846, 3429, 0), new Position(2844, 3429, 0), new Position(2845, 3429, 0)}, // CatherbyPart1
			{new Position(2853, 3423, 0), new Position(2855, 3423, 0), new Position(2859, 3426, 0)}, // CatherbyPart2
			{new Position(2726, 3524, 0), new Position(2727, 3524, 0), new Position(2714, 3532, 0)}, // Camelot
			{new Position(2990, 3169, 0), new Position(2986, 3176, 0)}, // Rimmington
			{new Position(2461, 3151, 0), new Position(2461, 3150, 0), new Position(2462, 3145, 0), new Position(2472, 3156, 0), new Position(2474, 3153, 0)}, // Castle
																																								// Wars
			{new Position(2393, 3419, 0), new Position(2391, 3421, 0), new Position(2389, 3423, 0), new Position(2388, 3423, 0), new Position(2385, 3422, 0), new Position(2384, 3419, 0), new Position(2383, 3417, 0), new Position(2382, 3415, 0), new Position(2382, 3414, 0), new Position(2384, 3411, 0)}, // Grand
																																																																												// Tree
			{new Position(2527, 3412, 0), new Position(2530, 3412, 0), new Position(2533, 3410, 0), new Position(2537, 3406, 0), new Position(2507, 3421, 0)}, // Baxtorian
																																								// Falls
			{new Position(2561, 3374, 0), new Position(2562, 3374, 0), new Position(2568, 3365, 0), new Position(2566, 3370, 0)}, // Ardougne
			{new Position(2855, 2974, 0), new Position(2865, 2972, 0), new Position(2860, 2972, 0), new Position(2835, 2974, 0), new Position(2859, 2976, 0), new Position(2866, 2976, 0), new Position(2836, 2971, 0), new Position(2841, 2971, 0)}, // Shilo
																																																														// Village
			{new Position(2791, 3279, 0), new Position(2795, 3279, 0), new Position(2790, 3273, 0), new Position(2793, 3283, 0)}, // Fishing
																																	// Platform
			{new Position(2843, 3359, 0), new Position(2842, 3359, 0), new Position(2847, 3361, 0), new Position(2848, 3361, 0), new Position(2840, 3356, 0), new Position(2845, 3356, 0), new Position(2875, 3342, 0), new Position(2876, 3342, 0), new Position(2877, 3342, 0), new Position(2879, 3338, 0), new Position(2879, 3335, 0), new Position(2877, 3331, 0), new Position(2876, 3331, 0), new Position(2875, 3331, 0)}, // Entrana
			{new Position(3497, 3175, 0), new Position(3496, 3178, 0), new Position(3499, 3178, 0), new Position(3489, 3184, 0), new Position(3496, 3176, 0), new Position(3486, 3184, 0), new Position(3479, 3189, 0), new Position(3476, 3191, 0), new Position(3472, 3196, 0), new Position(3496, 3180, 0), new Position(3512, 3178, 0), new Position(3515, 3180, 0), new Position(3518, 3177, 0), new Position(3528, 3172, 0), new Position(3531, 3169, 0), new Position(3531, 3172, 0), new Position(3531, 3167, 0), new Position(3529, 3165, 0), new Position(3528, 3167, 0), new Position(3527, 3169, 0), new Position(3529, 3165, 0), new Position(3527, 3171, 0)}, // Burgh
																																																																																																																																																																			// The
																																																																																																																																																																			// Rott
			{new Position(3047, 3703, 0), new Position(3045, 3702, 0)}, // Bandit
																		// Camp(WILDY)
			{new Position(2612, 3411, 0), new Position(2607, 3410, 0), new Position(2612, 3414, 0), new Position(2612, 3415, 0), new Position(2609, 3416, 0), new Position(2604, 3417, 0), new Position(2605, 3416, 0), new Position(2602, 3411, 0), new Position(2602, 3412, 0), new Position(2602, 3414, 0), new Position(2603, 3417, 0), new Position(2599, 3419, 0), new Position(2601, 3422, 0), new Position(2605, 3421, 0), new Position(2602, 3426, 0), new Position(2604, 3426, 0), new Position(2605, 3425, 0), new Position(2605, 3420, 0), new Position(2603, 3419, 0), new Position(2598, 3422, 0)}, // Fishing
																																																																																																																																																					// Guild
			{new Position(2210, 3243, 0), new Position(2216, 3236, 0), new Position(2222, 3241, 0), new Position(2223, 3238, 0)}, // Elf
																																	// Camp
			{new Position(2266, 3253, 0), new Position(2265, 3258, 0), new Position(2264, 3258, 0)}, // Tirannwn
			{new Position(2633, 3691, 0), new Position(2633, 3689, 0), new Position(2639, 3698, 0), new Position(2639, 3697, 0), new Position(2639, 3695, 0), new Position(2642, 3694, 0), new Position(2642, 3697, 0), new Position(2644, 3709, 0), new Position(2645, 3708, 0), new Position(2648, 3708, 0), new Position(2648, 3711, 0)}, // Rellekka
			{new Position(2580, 3851, 0), new Position(2581, 3851, 0), new Position(2582, 3851, 0), new Position(2583, 3852, 0), new Position(2583, 3853, 0), new Position(2572, 3860, 0), new Position(2573, 3860, 0)}, // Miscellenia
			{new Position(2694, 2706, 0), new Position(2707, 2698, 0)}, // Ape
																		// Atoll
			{new Position(3267, 3148, 0), new Position(3268, 3147, 0), new Position(3277, 3139, 0), new Position(3275, 3140, 0)}, // AlKharid
																																	// /*{},
			{new Position(3350, 3817, 0), new Position(3347, 3814, 0), new Position(3363, 3816, 0), new Position(3368, 3811, 0)}, // Wildy
																																	// north
			{new Position(2342, 3702, 0), new Position(2345, 3702, 0), new Position(2348, 3702, 0), new Position(2326, 3700, 0), new Position(2321, 3702, 0), new Position(2311, 3704, 0), new Position(2307, 3700, 0)}, // Piscatoris
																																																							// Fishing
																																																							// Colony
	};
	/**
	 * This basically contains all the IDs to spawn - in the spots above.
	 */
	static final int[][] FISHING_SPOT_IDS = {{316, 316}, // tutorial
																	// island
			{309, 309, 309}, // Barbarian village.
			{316, 316, 316}, // Draynor village.
			{309, 316},// lumbridge
			{316, 312, 312, 316, 316, 312}, // Karamja
			{316, 312, 312, 316, 316, 312}, // CatherbyPart1
			{313, 313, 313}, // CatherbyPart2
			{309, 309}, // Camelot
			{316, 316}, // Rimmington
			{309, 309, 309, 309}, // Castle Wars
			{309, 309, 309, 309, 309, 309, 309}, // Grand Tree
			{309, 309, 309}, // Baxtorian Falls
			{309, 309, 309}, // Ardougne
			{309, 309, 309, 309, 309}, // Shilo Village
			{316, 316, 316}, // Fishing Platform
			{316, 316, 316, 316, 316, 316, 316, 316, 316}, // Entrana
			{313, 313, 313, 313, 313, 313, 313, 313, 313, 313, 313, 313, 313, 313, 313, 313, 313}, // Burgh
																									// The
																									// Rott
			{316, 316}, // Bandit Camp(WILDY)
			{312, 312, 313, 313, 313, 312, 313, 313, 313, 312, 313, 313, 313, 313, 312, 313, 312}, // Fishing
																									// Guild
			{309, 309, 309}, // Elf Camp
			{309, 309, 309}, // Tirannwn
			{316, 316, 312, 312, 312, 313, 313, 313}, // Rellekka
			{312, 312, 312, 312, 312}, // Miscellenia
			{313, 313}, // Ape Atoll
			{316, 316, 316}, // AlKharid
			{312, 312, 312, 312}, // Wildy
			{1174, 1174, 1174, 1174, 1174, 1174, 1174}, // Piscatoris
	
	};
	/**
	 * Fishing spots will be the only NPC spawns that we "hardcode" - which we
	 * do in this method.
	 */
	public static void spawnFishingSpots() {
		for (int index = 0; index < FISHING_SPOT_IDS.length; index++) {
			for (int index2 = 0; index2 < FISHING_SPOT_IDS[index].length; index2++) {
				Npc npc = new Npc(FISHING_SPOT_IDS[index][index2]);
				npc.setPosition(FISHING_SPOT_Position_SETS[index][index2]);
				npc.setWalkType(Npc.WalkType.STAND);
				World.register(npc);
				FishingSpots.FISHING_SPOTS.put(FISHING_SPOT_Position_SETS[index][index2], npc);
			}
		}
		//FishingSpots.fishingEvent();
	}
	/**
	 * Checks if a specific Position contains a given FishingSpot.
	 * 
	 * @param loc
	 *            The Position we're checking.
	 * @param spot
	 *            The fishing spot we want to check for.
	 * @return <code>true</code> if, <code>false</code> if not.
	 */
	static boolean spotExists(Position loc, Fishing.FishingSpot spot) {
		Npc npc = FishingSpots.FISHING_SPOTS.get(loc);
		if (npc != null) {
			for (int id : spot.getNPCIds()) {
				if (npc.getDefinition().getId() == id) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * Takes care of the fishing spots moving around.
	 */
	public static void fishingEvent() {
	
		World.submit(new Tick(2) {
			@Override
			public void execute() {
				if (Fishing.r.nextInt(20) == 0) {
					/*
					 * For each "place" of fishing spots..
					 */
					for (Position[] array : FISHING_SPOT_Position_SETS) {
						/*
						 * We create a list of the Positions we actually have
						 * spots at.
						 */
						List<Position> spots = new ArrayList<Position>();
						/*
						 * As well as one without any spots in currently.
						 */
						List<Position> free = new ArrayList<Position>();
						/*
						 * By looping through all the Positions at this "place".
						 */
						for (Position loc : array) {
							/*
							 * And check if the map contains them.
							 */
							if (FishingSpots.FISHING_SPOTS.containsKey(loc)) {
								spots.add(loc);
							} else {
								free.add(loc);
							}
						}
						if (free.isEmpty())
							continue;
	
						/*
						 * Great, now we have our lists, and we pick a random
						 * fishing spot to move..
						 */
						if (spots.size() == 0) { // This will never be the case
													// when we're done adding
													// everything.
							return;
						}
						final Position currentLoc = spots.get(Fishing.r.nextInt(spots.size()));
						final Npc spot = FishingSpots.FISHING_SPOTS.get(currentLoc);
	
						FishingSpots.FISHING_SPOTS.remove(currentLoc);
						final Position newLoc = free.get(Misc.random(free.size() - 1));
						spot.teleport(newLoc);
						FishingSpots.FISHING_SPOTS.put(newLoc, spot);
					}
	
				}
			}
		});
	
	}
}
