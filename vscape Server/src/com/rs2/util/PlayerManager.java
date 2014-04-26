package com.rs2.util;

import java.util.Scanner;

import com.rs2.Constants;
import com.rs2.model.players.Player;

/**
 * Boomer
 */
public class PlayerManager {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Constants.MYSQL_ENABLED = false;
        Constants.DEVELOPER_MODE = true;
        boolean next = true;
        do {
            System.out.println("Username: ");
            String username = NameUtil.uppercaseFirstLetter(scanner.nextLine());
            Player player = new Player(null);
            player.setUsername(username);
            try {
                PlayerSave.load(player);
                player.setBanExpire(0);
                PlayerSave.save(player);
                System.out.println("Successfully Unbanned "+player.getUsername());
            } catch (Exception e) {
                e.printStackTrace();  
                System.out.println("Failed to save "+username);
            }
            System.out.println("Type 'q' to quit, anything else to continue");
            if (scanner.next().equals("q"))
                next = false;
            System.out.println("");
        }
        while (next);
        scanner.close();// CLOSEFILE
    }
}
