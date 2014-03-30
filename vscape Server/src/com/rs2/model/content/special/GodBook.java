package com.rs2.model.content.special;

import com.rs2.Constants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

import java.util.HashMap;
import java.util.Map;

import java.util.List;

public class GodBook {
	
	public static final double[][] Book = {
		{3839, 3827, 3828, 3829, 3830, 3840},//Saradomin
		{3841, 3831, 3832, 3833, 3834, 3842}, //Zamarok
		{3843, 3835, 3836, 3837, 3838, 3844} //Guthix
	};
	
	public static boolean hasPage(Player player, int id)
	{
		return player.getInventory().playerHasItem(id);
	}
	
	public static boolean hasPages(Player player, int p1, int p2, int p3, int p4)
	{
		if(hasPage(player, p1) && hasPage(player, p2) && hasPage(player, p3) && hasPage(player, p4))
		{
			return true;
		}
		return false;
	}
	
	public static boolean addPageToBook(Player player, Item useItem, Item withItem, int slot, int slotUsed)
	{
		int item = useItem.getId(), usedItem = withItem.getId();
		for (double[] bk : Book) {
			int dmgB = (int)bk[0];
			int p1 = (int)bk[1];
			int p2 = (int)bk[2];
			int p3 = (int)bk[3];
			int p4 = (int)bk[4];
			int comB = (int)bk[5];
			if ((item == p1 || item == p2 || item == p3 || item == p4)) {
				if(usedItem == dmgB)
				{
					if(hasPages(player, p1,p2,p3,p4))
					{
						player.getInventory().removeItem(new Item(p1));
						player.getInventory().removeItem(new Item(p2));
						player.getInventory().removeItem(new Item(p3));
						player.getInventory().removeItem(new Item(p4));
						player.getInventory().removeItem(new Item(dmgB));
						player.getInventory().addItem(new Item(comB));
						player.getActionSender().sendMessage("The book is now complete!");
						return true;
					}
					else
					{
						player.getActionSender().sendMessage("You don't have the required pages!");
						return true;
					}
				}
				else 
				{
					player.getActionSender().sendMessage("This page doesn't seem to be for this book...");
					return true;
				}
			}
		}
		return false;
	}
}
