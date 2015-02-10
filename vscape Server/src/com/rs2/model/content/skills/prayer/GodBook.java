package com.rs2.model.content.skills.prayer;

import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class GodBook {
	
    public static final int SARA_BOOK = 3840;
    public static final int ZAMORAK_BOOK = 3842;
    public static final int GUTHIX_BOOK = 3844;
    
    public static final int UNBLESSED_HOLY_SYMBOL = 1716;
    public static final int HOLY_SYMBOL = 1718;
    public static final int UNPOWERED_UNHOLY_SYMBOL = 1722;
    public static final int UNHOLY_SYMBOL = 1724;
	
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
						player.setGodBook(comB);
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
	
	public static boolean blessSymbol(final Player player, int firstItem, int secondItem) {
	    if (firstItem == GUTHIX_BOOK || firstItem == SARA_BOOK) {
		if (secondItem == UNBLESSED_HOLY_SYMBOL) {
		    if (player.getSkill().getPlayerLevel(Skill.PRAYER) >= 50) {
			player.setStopPacket(true);
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			    @Override
			    public void execute(CycleEventContainer b) {
				b.stop();
			    }
			    @Override
			    public void stop() {
				player.getInventory().replaceItemWithItem(new Item(UNBLESSED_HOLY_SYMBOL), new Item(HOLY_SYMBOL));
				player.getActionSender().sendMessage("You bless the holy symbol.");
				player.setStopPacket(false);
			    }
			}, 1);
			return true;
		    } else {
			player.getDialogue().sendStatement("You need level 50 Prayer to bless holy symbols.");
			return true;
		    }
		}
	    }
	    if (firstItem == GUTHIX_BOOK || firstItem == ZAMORAK_BOOK) {
		if (secondItem == UNPOWERED_UNHOLY_SYMBOL) {
		    if (player.getSkill().getPlayerLevel(Skill.PRAYER) >= 50) {
			player.setStopPacket(true);
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			    @Override
			    public void execute(CycleEventContainer b) {
				b.stop();
			    }
			    @Override
			    public void stop() {
				player.getInventory().replaceItemWithItem(new Item(UNPOWERED_UNHOLY_SYMBOL), new Item(UNHOLY_SYMBOL));
				player.getActionSender().sendMessage("You power the unholy symbol.");
				player.setStopPacket(false);
			    }
			}, 1);
			return true;
		    } else {
			player.getDialogue().sendStatement("You need level 50 Prayer to power unholy symbols.");
			return true;
		    }
		}
	    }
	    return false;
	}
	
	public static void preachGodBook(final Player player, final int book) {
		player.setStopPacket(true);
		player.getAttributes().put("canTakeDamage", Boolean.FALSE);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int preachStage = 0;
			@Override
			public void execute(CycleEventContainer container) {
				player.getUpdateFlags().sendAnimation(1670);
				switch (book) {
				    case ZAMORAK_BOOK:
				    	switch (preachStage) {
				    		case 0:
				    			player.getUpdateFlags().setForceChatMessage("The weak deserve to die,");
				    		break;
				    		case 1:
				    			player.getUpdateFlags().setForceChatMessage("So that the strong may flourish.");
				    		break;
				    		case 2:
				    			player.getUpdateFlags().setForceChatMessage("This is the creed of Zamorak.");
				    			container.stop();
				    		break;
				    	}
					break;
				    case SARA_BOOK:
				    	switch (preachStage) {
				    		case 0:
				    			player.getUpdateFlags().setForceChatMessage("The currency of goodness is honour,");
				    		break;
				    		case 1:
				    			player.getUpdateFlags().setForceChatMessage("It retains its value through scarcity.");
				    		break;
				    		case 2:
				    			player.getUpdateFlags().setForceChatMessage("This is Saradomin's wisdom.");
				    			container.stop();
				    		break;
				    	}
					break;
				    case GUTHIX_BOOK:
				    	switch (preachStage) {
				    		case 0:
				    			player.getUpdateFlags().setForceChatMessage("The trees, the earth, the sky, the waters,");
				    		break;
				    		case 1:
				    			player.getUpdateFlags().setForceChatMessage("All play their part upon this land.");
				    		break;
				    		case 2:
				    			player.getUpdateFlags().setForceChatMessage("May Guthix bring thee balance.");
				    			container.stop();
				    		break;
				    	}
					break;
				}
				preachStage++;
			}
			@Override
			public void stop() {
				player.setStopPacket(false);
				player.getAttributes().put("canTakeDamage", Boolean.TRUE);
				preachStage = 0;
			}
		}, 2);
	}
}
