package com.rs2.util;

import java.util.Scanner;

import com.rs2.model.content.skills.SkillHandler;

/**
 *
 */
public class MiningTimeChecker {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        final int trials = 1000;
        while (true) {
            System.out.println("Mining level: ");
            int level = scanner.nextInt();
            System.out.println("Required level: ");
            int required = scanner.nextInt();
            System.out.println("Pickaxe Bonus: ");
            int pick = scanner.nextInt();
            int totalAttempts = 0;
            for (int i = 0; i < trials; i++) {
                int attempts = 0;
                while (true) {
                    attempts += 1;
                    if (SkillHandler.skillCheck(level, required, pick*20)) {
                        totalAttempts += attempts;
                        break;
                    }
                }
            }
            double averageAttempts = totalAttempts/(double)trials;
            System.out.println("Averaged at "+averageAttempts+" attempts which is "+Misc.ticksToSeconds((long)Math.ceil(averageAttempts*3d))+" seconds.");
        }
    }
}
