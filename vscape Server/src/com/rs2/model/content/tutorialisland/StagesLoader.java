package com.rs2.model.content.tutorialisland;

import java.util.HashMap;
import java.util.Map;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 4/30/12 Time: 8:44 PM To change
 * this template use File | Settings | File Templates.
 */
public enum StagesLoader {
	STAGE_1(1, 1, new int[]{TutorialConstants.LOGOUT_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().sendSidebarInterface(TutorialConstants.LOGOUT_TAB[0], TutorialConstants.LOGOUT_TAB[1]);
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(945).getIndex());
			player.getDialogue().sendStartInfo("To start the tutorial use your left mouse-button to click on the", "'2006Scape Guide' in this room. He is indicated by a flashing", "yellow arrow above his head. If you can't find him, use your", "keyboard's arrow keys to rotate the view.", "Getting Started", send);
		}
	},

	STAGE_2(2, new int[]{TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().sendSidebarInterface(TutorialConstants.OPTION_TAB[0], TutorialConstants.OPTION_TAB[1]);
			player.getActionSender().flashSideBarIcon(TutorialConstants.OPTION_TAB[0]);
			player.getDialogue().sendStartInfo("Player controls", "Please click on the flashing spanner icon found at the bottom", "right of your screen. This will display your player controls.", "", "", send);
		}
	},

	STAGE_3(3, 7, new int[]{TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(945).getIndex());
			player.getDialogue().sendScrollDownInfo("Player controls", new String[]{"On the side panel, you can now see a variety of options from", "changing the brightness of the screen and the volume of", "music, to selecting whether your player should accept help", "from other players. Don't worry about these too much for now,", "they will become clearer as you explore the game. Talk to the", "2006Scape Guide to continue."}, send);
		}
	},

	STAGE_4(4, new int[]{TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createObjectHints(3098, 3107, 130, 3);
			player.getDialogue().sendScrollDownInfo("Interacting with scenery", new String[]{"You can interact with many items of the scenery by simply ", "clicking on them. Right clicking will also give more options. Feel", "free to try it with the things in this room. then click on the door", "indicated with the yellow arrow to go through to the next", "instructor."}, send);
		}
	},

	STAGE_5(5, 1, new int[]{TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			if (send)
				player.getNewComersSide().setProgressValue(player.getNewComersSide().getProgressValue() + 1);
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(943).getIndex());
			player.getDialogue().sendStartInfo("Follow the path to find the next instructor. Clicking on the", "ground will walk you to that point. Talk to the survival expert by", "the pond to continue the tutorial. Remember you can rotate", "the view by pressing the arrow keys.", "Moving around", send);
		}
	},

	STAGE_6(6, new int[]{TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0]}, new Item[]{new Item(590), new Item(1351)}, "The Survival Guide") {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createPlayerHints(-1, Npc.getNpcById(943).getIndex());
			player.getActionSender().sendSidebarInterface(TutorialConstants.INVENTORY_TAB[0], TutorialConstants.INVENTORY_TAB[1]);
			player.getActionSender().flashSideBarIcon(TutorialConstants.INVENTORY_TAB[0]);
			player.getDialogue().sendStartInfo("Click on the flashing backpack icon to the right side of the", "main window to view your inventory. Your inventory is a list of", "everything you have in your backpack.", "", "Viewing the items that you were given.", send);
		}
	},

	STAGE_7(7, new int[]{TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0]}, new Item[]{new Item(590), new Item(1351)}, "The Survival Guide") {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getDialogue().sendStartInfo("You can click on the backpack icon at any time to view the", "items that you currently have in your inventory. You will see", "that you now have an axe in your inventory. Use this to get", "some logs by clicking on the indicated tree.", "Cut down a tree", send);
			player.getActionSender().createObjectHints(3100, 3095, 170, 3);
		}
	},

	STAGE_8(8, new int[]{TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createObjectHints(3100, 3094, 170, -1);
			player.getDialogue().sendStartInfo("Well done - you managed to cut some logs from the tree! Next", "use the tinderbox in your inventory to light the logs.", "a) First click on the tinderbox to 'use it'.", "b) Then click on the logs in your inventory to light them.", "Building a fire", send);
		}
	},

	STAGE_9(9, new int[]{TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().sendSidebarInterface(TutorialConstants.STATS_TAB[0], TutorialConstants.STATS_TAB[1]);
			player.getActionSender().flashSideBarIcon(TutorialConstants.STATS_TAB[0]);
			player.getDialogue().sendStartInfo("You gained some experience.", "Click on the flashing bar graph icon near the inventory button", "to see your skill abilities.", "", "", send);
		}
	},

	STAGE_10(10, 2, new int[]{TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(943).getIndex());
			player.getDialogue().sendStartInfo("Here you will see how good your skills are. As you move your", "mouse over any of the icons in this panel, the small yellow", "popup box will show you the exact amount of experience you", "have and you much you need. Speak to Brynna to continue.", "These are your stats.", send);
		}
	},

	STAGE_11(11, new int[]{TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0]}, new Item[]{new Item(303)}, "The Survival Guide") {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			if (send)
				player.getNewComersSide().setProgressValue(player.getNewComersSide().getProgressValue() + 1);
			player.getActionSender().createObjectHints(3101, 3092, 70, 2);
			player.getDialogue().sendStartInfo("Click on the fishing spot indicated by the flashing arrow.", "Remember you can check your inventory by clicking the", "backpack icon.", "", "Catch some Shrimp.", send);
		}
	},

	STAGE_12(12, new int[]{TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createObjectHints(3101, 3092, 70, -1);
			player.getDialogue().sendStartInfo("Now you have caught some shrimp let's cook it. First light a", "fire: Chop down a tree and then use the tinderbox on the logs.", "If you've lose your axe or tinderbox Brynna will give you", "another.", "Cooking your shrimp.", send);
		}
	},

	STAGE_13(13, new int[]{TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getDialogue().sendStartInfo("You have just burnt your first shrimp. This is normal. As you", "get more experience in cooking you will burn stuff less. Let's", "try cooking, without burning it this time. First catch some", "shrimp, then use them on a fire.", "Burning your shrimp.", send);
		}
	},

	STAGE_14(14, new int[]{TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createObjectHints(3089, 3091, 120, 4);
			player.getDialogue().sendStartInfo("If you'd like a recap on anything you've learned so far speak", "to Brynna. You can now move on to the next instructor. Click on", "the gate shown and follow the path. Remember you can move", "the camera with the arrow keys.", "Well done, you've just cooked your first 2006Scape meal.", send);
		}
	},

	STAGE_15(15, new int[]{TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			if (send)
				player.getNewComersSide().setProgressValue(player.getNewComersSide().getProgressValue() + 1);
			player.getActionSender().createObjectHints(3079, 3084, 130, 3);
			player.getDialogue().sendStartInfo("Follow the path until you get to the door with the yellow arrow", "above it. Click on the door to open it. Notice the mini-map in", "the top right, this shows a top down view of the area around", "you. This can also be used for navigation.", "Find your next instructor.", send);
		}
	},

	STAGE_16(16, 1, new int[]{TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(942).getIndex());
			player.getDialogue().sendStartInfo("Talk to the chief indicated. He will teach you the more advanced", "aspects of cooking such as combining ingredients. He will also", "teach you about your music player menu as well.", "", "Find your next instructor.", send);
		}
	},

	STAGE_17(17, new int[]{TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0]}, new Item[]{new Item(1929), new Item(1933)}, "Leo") {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createPlayerHints(-1, Npc.getNpcById(942).getIndex());
			player.getDialogue().sendStartInfo("This is the base for many of the meals. To make dough we must", "mix flour and water. So first right click the bucket of water and", "select use then left click on the pot of flour.", "", "Making dough.", send);
		}
	},

	STAGE_18(18, new int[]{TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0]}, new Item[]{new Item(1929), new Item(1933)}, "Leo") {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createObjectHints(3076, 3081, 100, 2);
			player.getDialogue().sendStartInfo("Cooking dough. Now you have dough made you can cook it. To", "cook the dough use it with the range shown by the arrow. If you", "lose your dough talk to Lev, he will give you more ingredients.", "", "", send);
		}
	},

	STAGE_19(19, new int[]{TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createObjectHints(3076, 3081, 100, -1);
			if (send)
				player.getNewComersSide().setProgressValue(player.getNewComersSide().getProgressValue() + 1);
			player.getActionSender().sendSidebarInterface(TutorialConstants.MUSIC_TAB[0], TutorialConstants.MUSIC_TAB[1]);
			player.getActionSender().flashSideBarIcon(TutorialConstants.MUSIC_TAB[0]);
			player.getDialogue().sendScrollDownInfo("Cooking dough.", new String[]{"Well done! Your first loaf of bread. As you gain experience in", "Cooking, you will be able to make other things like pies, cakes", "and even kebabs. Now you've got the hang on cooking, let's", "move on. Click on the flashing icon in the bottom right to see", "the jukebox."}, send);
		}
	},

	STAGE_20(20, new int[]{TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createObjectHints(3073, 3090, 130, 3);
			player.getDialogue().sendScrollDownInfo("The Music Player.", new String[]{"From this interface you can control the music that is played.", "As you explore the world, more of the tuner will become", "unlocked. Once you've examined this menu use the next door", "to continue. If you need a recap on anything covered here,", "talk to Lev."}, send);
		}
	},

	STAGE_21(21, new int[]{TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createObjectHints(3073, 3090, 130, -1);
			if (send)
				player.getNewComersSide().setProgressValue(player.getNewComersSide().getProgressValue() + 1);
			player.getActionSender().sendSidebarInterface(TutorialConstants.EMOTE_TAB[0], TutorialConstants.EMOTE_TAB[1]);
			player.getActionSender().flashSideBarIcon(TutorialConstants.EMOTE_TAB[0]);
			player.getDialogue().sendStartInfo("", "Why not try running there. Start by opening the player", "controls, that's the flashing icon of a running man.", "", "It's only a short distance to the next guide", send);
		}
	},

	STAGE_22(22, new int[]{TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getDialogue().sendStartInfo("In this menu you will see many options from waving to walking.", "At the top of the panel there are two buttons. One is walk the", "other one is run. Click the run button.", "", "Running.", send);
		}
	},

	STAGE_23(23, new int[]{TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			// todo red arrow
			player.getActionSender().createObjectHints(3086, 3126, 130, 5);
			player.getDialogue().sendStartInfo("Now that you have run turned on follow the path, until you", "come to the end. You may notice that your energy left goes", "down. If this reaches zero you'll stop running. Click on the door", "to pass through it.", "Run to the next guide.", send);
		}
	},

	STAGE_24(24, 1, new int[]{TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			if (send)
				player.getNewComersSide().setProgressValue(player.getNewComersSide().getProgressValue() + 1);
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(949).getIndex());
			player.getDialogue().sendStartInfo("Talk with the Quest Guide Instructor.", "", "He'll tell you all about quests.", "", "", send);
		}
	},

	STAGE_25(25, new int[]{TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createPlayerHints(-1, Npc.getNpcById(949).getIndex());
			player.getActionSender().sendSidebarInterface(TutorialConstants.QUEST_TAB[0], TutorialConstants.QUEST_TAB[1]);
			player.getActionSender().flashSideBarIcon(TutorialConstants.QUEST_TAB[0]);
			player.getDialogue().sendStartInfo("Open the Quest journal.", "", "Click on the flashing icon next to your inventory.", "", "", send);
		}
	},

	STAGE_26(26, 2, new int[]{TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(949).getIndex());
			player.getDialogue().sendStartInfo("Your Quest journal", "This is your quest journal, a list of all the quests in the game.", "Talk to the instructor again for an explanation.", "", "", send);
		}
	},

	STAGE_27(27, new int[]{TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createObjectHints(3088, 3119, 100, 2);
			player.getDialogue().sendStartInfo("Moving on.", "It's time to enter some caves. Click on the ladder to go down to", "the next area.", "", "", send);
		}
	},

	STAGE_28(28, 1, new int[]{TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			if (send)
				player.getNewComersSide().setProgressValue(player.getNewComersSide().getProgressValue() + 1);
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(948).getIndex());
			player.getDialogue().sendStartInfo("Next let's get you a weapon, or more to the point, you can", "make your first weapon yourself. Don't panic, the mining", "instructor will help you. Talk to him and he'll tell you all about it.", "", "Mining and smithing.", send);
		}
	},

	STAGE_29(29, new int[]{TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createObjectHints(3076, 9504, 70, 2);
			player.getDialogue().sendStartInfo("To prospect a mineable rock just right click it and select the", "prospect rock option. This will tell you the type of ore you can", "mine from it. Try it now on one of the rocks indicated.", "", "Prospecting", send);
		}
	},

	STAGE_30(30, new int[]{TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createObjectHints(3086, 9501, 70, 2);
			player.getDialogue().sendStartInfo("", "So now you know there's tin in the grey rocks, try prospecting", "the brown ones next.", "", "It's tin.", send);
		}
	},

	STAGE_31(31, 4, new int[]{TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(948).getIndex());
			player.getDialogue().sendStartInfo("Talk to the mining instructor to find out about these types of", "ore and how you can mine them. He'll give you the needed tools", "too.", "", "It's copper.", send);
		}
	},

	STAGE_32(32, new int[]{TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}, new Item[]{new Item(1265)}, "Dezzick") {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createObjectHints(3076, 9504, 70, 2);
			player.getDialogue().sendStartInfo("It's quite simple really, all you need to do is right click on the", "rock and select mine. You can only mine when you have a", "pickaxe. So give it a try, first mine one tin ore.", "", "Mining.", send);
		}
	},

	STAGE_33(33, new int[]{TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createObjectHints(3086, 9501, 70, 2);
			player.getDialogue().sendStartInfo("Now you have some tin ore you just need some copper, then", "you'll have all you need to create a bronze bar. As you did", "before right click on the copper rock and select mine.", "", "Mining.", send);
		}
	},

	STAGE_34(34, new int[]{TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			if (send)
				player.getNewComersSide().setProgressValue(player.getNewComersSide().getProgressValue() + 1);
			player.getActionSender().createObjectHints(3079, 9496, 120, 2);
			player.getDialogue().sendStartInfo("You should now have both copper and tin ore. So let's smelt", "them to make a bronze bar. To do this right click on either tin or", "copper ore and select use then left click on the furnace. Try", "it now.", "Smelting.", send);
		}
	},

	STAGE_35(35, 7, new int[]{TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(948).getIndex());
			player.getDialogue().sendStartInfo("", "Speak to Dezzick and he'll show you how to make it into a", "weapon.", "", "You've made a bronze bar!", send);
		}
	},

	STAGE_36(36, new int[]{TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}, new Item[]{new Item(2347)}, "Dezzick") {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createObjectHints(3083, 9499, 70, 2);
			player.getDialogue().sendStartInfo("To smith you'll need a hammer like the one you were given by", "Dezzick, access to an anvil like the one with the arrow over it", "and enough metal bars to make what you are trying to smith.", "To start the process use the bar on one of the anvils.", "Smithing a dagger.", send);
		}
	},

	STAGE_37(37, new int[]{TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createObjectHints(3094, 9502, 120, 4);
			player.getDialogue().sendStartInfo("So let's move on. Go through the gates shown by the arrow.", "Remember you may need to move the camera to see your", "surrowndings. Speak to the guide for a recap at any time.", "", "You've finished in this area.", send);
		}
	},

	STAGE_38(38, 1, new int[]{TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			if (send)
				player.getNewComersSide().setProgressValue(player.getNewComersSide().getProgressValue() + 1);
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(944).getIndex());
			player.getDialogue().sendStartInfo("Combat.", "In this area you will find out about combat, both melee and", "ranged. Speak to the guide and he'll tell you about it.", "", "", send);
		}
	},

	STAGE_39(39, new int[]{TutorialConstants.EQUIPMENT_TAB[0], TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createPlayerHints(-1, Npc.getNpcById(944).getIndex());
			player.getActionSender().sendSidebarInterface(TutorialConstants.EQUIPMENT_TAB[0], TutorialConstants.EQUIPMENT_TAB[1]);
			player.getActionSender().flashSideBarIcon(TutorialConstants.EQUIPMENT_TAB[0]);
			player.getDialogue().sendStartInfo("Wielding weapons.", "You now have access to a new interface. Click on the flashing", "icon of a man, the one to the right of your backpack icon.", "", "", send);
		}
	},

	STAGE_40(40, new int[]{TutorialConstants.EQUIPMENT_TAB[0], TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getDialogue().sendStartInfo("From here you can see what items you have equipped. Let's", "get one of those slots filled, go back to your inventory and", "right click your dagger, select wield from the menu.", "", "This is your worn inventory.", send);
		}
	},

	STAGE_41(41, 5, new int[]{TutorialConstants.EQUIPMENT_TAB[0], TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(944).getIndex());
			player.getDialogue().sendScrollDownInfo("You're now holding your dagger.", new String[]{"Clothes, armour, weapons and many other items are equipped", "like this. You can unequip items by clicking on the item int the", "worn inventory. You can close this window by clicking on the", "small 'x' in the top right hand corner. Speak to the Combat", "Instructor to continue."}, send);
		}
	},

	STAGE_42(42, new int[]{TutorialConstants.EQUIPMENT_TAB[0], TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}, new Item[]{new Item(1171), new Item(1277)}, "The Combat Guide") {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createPlayerHints(-1, Npc.getNpcById(944).getIndex());
			player.getDialogue().sendScrollDownInfo("Unequipping items.", new String[]{"In your worn inventory panel, right click on the dagger and", "select the remove option from the drop down list. After you've", "unequipped the dagger, wield the sword and shield. As you", "pass the mouse over an item you will see its name appear at", "the top left of the screen."}, send);
		}
	},

	STAGE_43(43, new int[]{TutorialConstants.ATTACK_TAB[0], TutorialConstants.EQUIPMENT_TAB[0], TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().sendSidebarInterface(TutorialConstants.ATTACK_TAB[0], TutorialConstants.ATTACK_TAB[1]);
			player.getActionSender().flashSideBarIcon(TutorialConstants.ATTACK_TAB[0]);
			player.getDialogue().sendStartInfo("", "Click on the flashing crossed swords icon to see the combat", "interface.", "", "Combat Interface.", send);
		}
	},

	STAGE_44(44, new int[]{TutorialConstants.ATTACK_TAB[0], TutorialConstants.EQUIPMENT_TAB[0], TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			if (send)
				player.getNewComersSide().setProgressValue(player.getNewComersSide().getProgressValue() + 1);
			player.getActionSender().createObjectHints(3110, 9518, 120, 4);
			player.getDialogue().sendStartInfo("From this interface you can select the type of attack your", "character will use. Different mosters have different", "weaknesses. Now you have the tools needed for battle why", "not slay some rats. Click on the gate indicated to continue.", "This is your Combat interface.", send);
		}
	},

	STAGE_45(45, new int[]{TutorialConstants.ATTACK_TAB[0], TutorialConstants.EQUIPMENT_TAB[0], TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(950).getIndex());
			player.getDialogue().sendStartInfo("", "To attack the rat, click it and select the attack option. You", "will then walk over to it and start hitting it.", "", "Attacking.", send);
		}
	},

	STAGE_46(46, 6, new int[]{TutorialConstants.ATTACK_TAB[0], TutorialConstants.EQUIPMENT_TAB[0], TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(944).getIndex());
			player.getDialogue().sendStartInfo("", "Pass through the gate and talk to Vannaka, he will give you", "your next task.", "", "Well done, you've made your first kill!", send);
		}
	},

	STAGE_47(47, new int[]{TutorialConstants.ATTACK_TAB[0], TutorialConstants.EQUIPMENT_TAB[0], TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}, new Item[]{new Item(841), new Item(882, 50)}, "The Combat Guide") {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			if (send)
				player.getNewComersSide().setProgressValue(player.getNewComersSide().getProgressValue() + 1);
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(950).getIndex());
			player.getDialogue().sendStartInfo("Now you have a bow and some arrows. Before you can use", "them you'll need to equip them. Once equipped with the", "ranging gear try killing another rat. Remember, to attack right", "click on the monster and select attack.", "Rat ranging.", send);
		}
	},

	STAGE_48(48, new int[]{TutorialConstants.ATTACK_TAB[0], TutorialConstants.EQUIPMENT_TAB[0], TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createObjectHints(3111, 9526, 100, 2);
			player.getDialogue().sendStartInfo("You have completed the tasks here, to move on click on the", "ladder shown. If you need to go over any of what you learned", "here just talk to Vannaka and he'll tell you what he can.", "", "Moving on.", send);
		}
	},

	STAGE_49(49, 1, new int[]{TutorialConstants.ATTACK_TAB[0], TutorialConstants.EQUIPMENT_TAB[0], TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createObjectHints(3122, 3124, 100, 2);
			if (send)
				player.getNewComersSide().setProgressValue(player.getNewComersSide().getProgressValue() + 2);
			player.getDialogue().sendStartInfo("Follow the path and you will come to the front of a building.", "This is the 'Bank of 2006Scape' where you can store all your", "most valued items. To open your bank box just right click on an", "open booth indicated and select use.", "Banking.", send);
		}
	},

	STAGE_50(50, new int[]{TutorialConstants.ATTACK_TAB[0], TutorialConstants.EQUIPMENT_TAB[0], TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createObjectHints(3125, 3124, 130, 3);
			player.getDialogue().sendStartInfo("You can store more stuff here for safe keeping. If you die,", "anything in your bank will be saved. To deposit something right", "click it and select store. Once you've had a good look close the", "window and move on through the door indicated.", "This is your bank box.", send);
		}
	},

	STAGE_51(51, 1, new int[]{TutorialConstants.ATTACK_TAB[0], TutorialConstants.EQUIPMENT_TAB[0], TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			if (send)
				player.getNewComersSide().setProgressValue(player.getNewComersSide().getProgressValue() + 1);
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(947).getIndex());
			player.getDialogue().sendStartInfo("", "The guide here will tell you about making cash. Just click on", "him to hear what he's got to say.", "", "Financial advice.", send);
		}
	},

	STAGE_52(52, new int[]{TutorialConstants.ATTACK_TAB[0], TutorialConstants.EQUIPMENT_TAB[0], TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createObjectHints(3130, 3124, 130, 3);
			player.getDialogue().sendStartInfo("", "Continue through the next door.", "", "", "", send);
		}
	},

	STAGE_53(53, 1, new int[]{TutorialConstants.ATTACK_TAB[0], TutorialConstants.EQUIPMENT_TAB[0], TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			if (send)
				player.getNewComersSide().setProgressValue(player.getNewComersSide().getProgressValue() + 1);
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(954).getIndex());
			player.getDialogue().sendStartInfo("Follow the path to the chapel and enter it.", "Once inside talk to the monk. He'll tell you all about prayer.", "", "", "", send);
		}
	},

	STAGE_54(54, new int[]{TutorialConstants.PRAYER_TAB[0], TutorialConstants.ATTACK_TAB[0], TutorialConstants.EQUIPMENT_TAB[0], TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().sendSidebarInterface(TutorialConstants.PRAYER_TAB[0], TutorialConstants.PRAYER_TAB[1]);
			player.getActionSender().flashSideBarIcon(TutorialConstants.PRAYER_TAB[0]);
			player.getActionSender().createPlayerHints(-1, Npc.getNpcById(954).getIndex());
			player.getDialogue().sendStartInfo("Your prayer menu.", "", "Click on the flashing icon to open the prayer menu.", "", "", send);
		}
	},

	STAGE_55(55, 3, new int[]{TutorialConstants.PRAYER_TAB[0], TutorialConstants.ATTACK_TAB[0], TutorialConstants.EQUIPMENT_TAB[0], TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(954).getIndex());
			player.getDialogue().sendStartInfo("Your prayer menu.", "", "Talk with Brother Brace and he'll tell you about prayers.", "", "", send);
		}
	},

	STAGE_56(56, new int[]{TutorialConstants.FRIEND_TAB[0], TutorialConstants.PRAYER_TAB[0], TutorialConstants.ATTACK_TAB[0], TutorialConstants.EQUIPMENT_TAB[0], TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().sendSidebarInterface(TutorialConstants.FRIEND_TAB[0], TutorialConstants.FRIEND_TAB[1]);
			player.getActionSender().flashSideBarIcon(TutorialConstants.FRIEND_TAB[0]);
			player.getActionSender().createPlayerHints(-1, Npc.getNpcById(954).getIndex());
			player.getDialogue().sendStartInfo("You should see another new icon. Click on the flashing", "smily to open your friends list.", "", "", "", send);
		}
	},

	STAGE_57(57, new int[]{TutorialConstants.IGNORE_TAB[0], TutorialConstants.FRIEND_TAB[0], TutorialConstants.PRAYER_TAB[0], TutorialConstants.ATTACK_TAB[0], TutorialConstants.EQUIPMENT_TAB[0], TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().sendSidebarInterface(TutorialConstants.IGNORE_TAB[0], TutorialConstants.IGNORE_TAB[1]);
			player.getActionSender().flashSideBarIcon(TutorialConstants.IGNORE_TAB[0]);
			player.getDialogue().sendStartInfo("", "This will be explained by Brother Brace shortly, but first click", "the other flashing icon next to the friends list one.", "", "This is your friends list.", send);
		}
	},

	STAGE_58(58, 7, new int[]{TutorialConstants.IGNORE_TAB[0], TutorialConstants.FRIEND_TAB[0], TutorialConstants.PRAYER_TAB[0], TutorialConstants.ATTACK_TAB[0], TutorialConstants.EQUIPMENT_TAB[0], TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(954).getIndex());
			player.getDialogue().sendStartInfo("The two lists, friends and ignore, can be very helpful for", "keeping track of when your friends are online or for blocking", "messages from people you simply don't like. Speak with", "Brother Brace and he will tell you more.", "This is your ignore list.", send);
		}
	},

	STAGE_59(59, new int[]{TutorialConstants.IGNORE_TAB[0], TutorialConstants.FRIEND_TAB[0], TutorialConstants.PRAYER_TAB[0], TutorialConstants.ATTACK_TAB[0], TutorialConstants.EQUIPMENT_TAB[0], TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			if (send)
				player.getNewComersSide().setProgressValue(player.getNewComersSide().getProgressValue() + 1);
			player.getActionSender().createObjectHints(3122, 3102, 130, 6);
			player.getDialogue().sendStartInfo("Your final instructor!", "You're almost finished on tutorial island. Pass through the", "door to find the path leading to your last instructor.", "", "", send);
		}
	},

	STAGE_60(60, 1, new int[]{TutorialConstants.IGNORE_TAB[0], TutorialConstants.FRIEND_TAB[0], TutorialConstants.PRAYER_TAB[0], TutorialConstants.ATTACK_TAB[0], TutorialConstants.EQUIPMENT_TAB[0], TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			if (send)
				player.getNewComersSide().setProgressValue(player.getNewComersSide().getProgressValue() + 1);
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(946).getIndex());
			player.getDialogue().sendStartInfo("Just follow the path to the wizards house, where you will be", "shown how to cast spells. Just talk with the mage indicated to", "find out more.", "", "Your final instructor!", send);
		}
	},

	STAGE_61(61, new int[]{TutorialConstants.MAGIC_TAB[0], TutorialConstants.IGNORE_TAB[0], TutorialConstants.FRIEND_TAB[0], TutorialConstants.PRAYER_TAB[0], TutorialConstants.ATTACK_TAB[0], TutorialConstants.EQUIPMENT_TAB[0], TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createPlayerHints(-1, Npc.getNpcById(946).getIndex());
			player.getActionSender().sendSidebarInterface(TutorialConstants.MAGIC_TAB[0], TutorialConstants.MAGIC_TAB[1]);
			player.getActionSender().flashSideBarIcon(TutorialConstants.MAGIC_TAB[0]);
			player.getDialogue().sendStartInfo("", "Open up the Magic Spellbook tab by clicking on the flashing spellbook", "icon next to the Prayer List tab you just learned about.", "", "Open up your final tab", send);
		}
	},

	STAGE_62(62, 3, new int[]{TutorialConstants.MAGIC_TAB[0], TutorialConstants.IGNORE_TAB[0], TutorialConstants.FRIEND_TAB[0], TutorialConstants.PRAYER_TAB[0], TutorialConstants.ATTACK_TAB[0], TutorialConstants.EQUIPMENT_TAB[0], TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(946).getIndex());
			player.getDialogue().sendStartInfo("This is your where all of your magic spells are.", "Talk to Terrova to learn more.", "", "", "", send);
		}
	},

	STAGE_63(63, new int[]{TutorialConstants.MAGIC_TAB[0], TutorialConstants.IGNORE_TAB[0], TutorialConstants.FRIEND_TAB[0], TutorialConstants.PRAYER_TAB[0], TutorialConstants.ATTACK_TAB[0], TutorialConstants.EQUIPMENT_TAB[0], TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}, new Item[]{new Item(556, 5), new Item(558, 5)}, "Terrova") {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(951).getIndex());
			if (send)
				player.getNewComersSide().setProgressValue(player.getNewComersSide().getProgressValue() + 2);
			player.getDialogue().sendScrollDownInfo("Cast Wind Strike at a chicken.", new String[]{"Now you have the runes you should see the Wind Strike icon", "at the top-left of your spellbook, second from the left. Walk over to the", "caged chickens, click the Wind Strike icon and then  ", "select one of the chickens to cast it on. It may take", "several tries. If you need more runes ask Terrova"}, send);
		}
	},

	STAGE_64(64, new int[]{TutorialConstants.MAGIC_TAB[0], TutorialConstants.IGNORE_TAB[0], TutorialConstants.FRIEND_TAB[0], TutorialConstants.PRAYER_TAB[0], TutorialConstants.ATTACK_TAB[0], TutorialConstants.EQUIPMENT_TAB[0], TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getDialogue().sendStartInfo("That's it, you cast a spell! Sadly it didn't have any effect this time, but", "the more you practice, the better you'll get. Repeat this process until", "you successfully cast the spell. Click the Wind Strike icon again and", "then select one of the chickens.", "Cast Wind Strike on a chicken.", send);
		}
	},

	STAGE_65(65, 4, new int[]{TutorialConstants.MAGIC_TAB[0], TutorialConstants.IGNORE_TAB[0], TutorialConstants.FRIEND_TAB[0], TutorialConstants.PRAYER_TAB[0], TutorialConstants.ATTACK_TAB[0], TutorialConstants.EQUIPMENT_TAB[0], TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(946).getIndex());
			player.getDialogue().sendStartInfo("", "All you need to do now is teleport to the mainland. Just speak with", "Terrova and he'll how to do that.", "", "You have almost completed the tutorial!", send);
		}
	},

	STAGE_66(66, new int[]{TutorialConstants.MAGIC_TAB[0], TutorialConstants.IGNORE_TAB[0], TutorialConstants.FRIEND_TAB[0], TutorialConstants.PRAYER_TAB[0], TutorialConstants.ATTACK_TAB[0], TutorialConstants.EQUIPMENT_TAB[0], TutorialConstants.QUEST_TAB[0], TutorialConstants.MUSIC_TAB[0], TutorialConstants.LOGOUT_TAB[0], TutorialConstants.OPTION_TAB[0], TutorialConstants.INVENTORY_TAB[0], TutorialConstants.STATS_TAB[0], TutorialConstants.EMOTE_TAB[0]}) {
		@Override
		public void sendInterfaces(Player player, boolean send) {
			player.getActionSender().createPlayerHints(-1, Npc.getNpcById(946).getIndex());
			player.getActionSender().sendChatboxOverlay(-1);
			player.getActionSender().removeInterfaces();
			player.teleport(new Position(3233, 3229, 0));
			player.getNewComersSide().setTutorialIslandStage(100, true);
			player.getActionSender().sendWalkableInterface(-1);
			player.getActionSender().sendMessage("Welcome to " + Constants.SERVER_NAME + " - currently in Pre-Alpha stage v" + Constants.TEST_VERSION + ".");
			player.getDialogue().sendStatement("Welcome to Lumbridge! To get more help, simply click on the ", "Lumbridge Guide and he will give you some tips", "There can be found by looking for the question mark icon on", "your minimap. If you find that you are lost any time, look for", "other players, they might help you to make your way back.");
			player.getNewComersSide().addStarterItems();
		}
	},

	;

	private int stageIndex;
	private int[] sideBarEnabled;
	private Item[] tutItemsInvolved;
	private String tutorName;
	private int dialogueId;

	static Map<Integer, StagesLoader> stages = new HashMap<Integer, StagesLoader>();

	static {
		for (StagesLoader data : values()) {
			stages.put(data.stageIndex, data);
		}
	}

	public static StagesLoader forId(int stageId) {
		return stages.get(stageId);
	}

	StagesLoader(int stageIndex, int[] sideBarEnabled, Item[] tutItemsInvolved, String tutorName) {
		this.stageIndex = stageIndex;
		this.sideBarEnabled = sideBarEnabled;
		this.tutItemsInvolved = tutItemsInvolved;
		this.tutorName = tutorName;
	}

	StagesLoader(int stageIndex, int dialogueId, int[] sideBarEnabled) {
		this.stageIndex = stageIndex;
		this.sideBarEnabled = sideBarEnabled;
		this.dialogueId = dialogueId;
	}

	StagesLoader(int stageIndex, int[] sideBarEnabled) {
		this.stageIndex = stageIndex;
		this.sideBarEnabled = sideBarEnabled;
	}

	public abstract void sendInterfaces(Player player, boolean send);

	public int[] getSideBarEnabled() {
		return sideBarEnabled;
	}

	public Item[] getTutItemsInvolved() {
		return tutItemsInvolved;
	}

	public String getTutorName() {
		return tutorName;
	}

	public int getDialogueId() {
		return dialogueId;
	}
}
