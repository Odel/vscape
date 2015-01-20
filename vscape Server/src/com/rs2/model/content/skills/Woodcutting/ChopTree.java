package com.rs2.model.content.skills.Woodcutting;

import com.rs2.Constants;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.randomevents.SpawnEvent;
import com.rs2.model.content.randomevents.SpawnEvent.RandomNpc;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.content.skills.Tools;
import com.rs2.model.content.skills.Tools.Tool;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.functions.ChopVines;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class ChopTree {

	public enum Tree {
		ACHEY_TREE(new int[]{2023}, 1, 25, 2862, 3371, 75, 100),
		NORMAL_TREE(new int[]{1276, 1277, 1278, 1279, 1280, 1282, 1283, 1284, 1285, 1286, 1289, 1290, 1291, 1315, 1316, 1318, 1319, 1330, 1331, 1332, 1333, 1365, 1383, 1384, 2409, 3033, 3034, 3035, 3036, 3881, 3882, 3883, 5902, 5903, 5904}, 1, 25, 1511, 1342, 75, 100),
		JUNGLE_TREE(new int[]{4818}, 1, 25, 1511, 4819, 75, 100),
		JUNGLE_TREE_2(new int[]{4820}, 1, 25, 1511, 4821, 75, 100),
		OAK_TREE(new int[]{1281, 2037}, 15, 37.5, 1521, 1356, 14, 25),
		WILLOW_TREE(new int[]{1308, 5551, 5552, 5553}, 30, 67.5, 1519, 7399, 14, 15),
		TEAK_TREE(new int[]{9036}, 35, 85, 6333, 9037, 14, 20),
		MAPLE_TREE(new int[]{1307, 4677}, 45, 100, 1517, 1343, 59, 15),
		HOLLOW_TREE(new int[]{2289, 4060}, 45, 83, 3239, 2310, 59, 15),
		MAHOGANY_TREE(new int[]{9034}, 50, 125, 6332, 9035, 80, 10),
		YEW_TREE(new int[]{1309}, 60, 175, 1515, 7402, 100, 5),
		MAGIC_TREE(new int[]{1306}, 75, 250, 1513, 7401, 200, 5),
		DRAMEN_TREE(new int[]{1292}, 36, 0, 771, 1513, 59, 100),
		VINES(new int[]{5103, 5104, 5105, 5106, 5107}, 34, 0, -1, 1513, 2, 100);
		
		private int[] id;
		private int level;
		private double xp;
		private int log;
		private int stump;
		private int respawnTime;
		private int decayChance;

		public static Tree getTree(int id) {
            for(Tree tree : Tree.values()){
            	for (int ids : tree.getId()) {
	            	if (ids == id) {
	            		return tree;
	            	}
            	}
            }
            return null;
		}

		private Tree(int[] id, int level, double xp, int log, int stump, int respawnTime, int decayChance) {
			this.id = id;
			this.level = level;
			this.xp = xp;
			this.log = log;
			this.stump = stump;
			this.respawnTime = respawnTime;
            this.decayChance = decayChance;
		}

		public int[] getId() {
			return id;
		}

		public int getLevel() 	{
			return level;
		}

		public double getXP() {
			return xp;
		}

		public int getLog() {
			return log;
		}

		public int getStump() {
			return stump;
		}

		public int getRespawnTime() {
			return respawnTime;
		}

        public int getDecayChance() {
            return decayChance;
        }
	}
	
	public static void handle(final Player player, final int id, final int x, final int y) {
		final GameObject p = ObjectHandler.getInstance().getObject(x, y, player.getPosition().getZ());
		if (p != null && p.getDef().getId() != id) {
			return;
		}
		final Tree tree = Tree.getTree(id);
		if(tree == null) {
			return;
		}
		if (!Constants.WOODCUTTING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		final Tool axe = Tools.getTool(player, Skill.WOODCUTTING);
		if(axe == null) {
			player.getActionSender().sendMessage("You do not have an axe which you have the woodcutting level to use.");
			return;
		}
		if(player.getSkill().getLevel()[Skill.WOODCUTTING] < tree.getLevel()) {
			player.getActionSender().sendMessage("You need a Woodcutting level of " + tree.getLevel() + " to cut this tree.", true);
			return;
		}
		final Item log = new Item(tree.getLog(), 1);
		if(player.getInventory().getItemContainer().freeSlot() == -1) {
			player.getActionSender().sendMessage("Your inventory is too full to hold any more " + log.getDefinition().getName().toLowerCase() + ".");
			return;
		}
		  if (tree == Tree.DRAMEN_TREE) {
			   if (player.getQuestStage(14) < 2) {
				player.getDialogue().sendPlayerChat("I don't know anything about this tree...", Dialogues.SAD);
				return;
			   }
			   if (NpcLoader.checkSpawn(player, 655) && player.getQuestStage(14) == 2) {
			    player.getActionSender().sendMessage("You need to kill the tree spirit first!", true);
			    return;
			   }
			   if (!player.hasKilledTreeSpirit() && player.getQuestStage(14) == 2) {
			   NpcLoader.spawnNpc(player, new Npc(655), true, true);
			   return;
			  }
		  }
		if (player.getNewComersSide().isInTutorialIslandStage()) {
			player.getDialogue().sendTutorialIslandWaitingInfo("", "Your character is now attempting to cut down the tree. Sit back", "for a moment whilst he does all the hard work.", "", "Please wait...");
		} else {
			player.getActionSender().sendMessage("You swing your axe at the "+(tree == Tree.VINES ? "vines" : "tree")+".", true);
		}
		player.getActionSender().sendSound(472, 0, 0);
		player.getUpdateFlags().sendAnimation(axe.getAnimation(), 0);
		final int task = player.getTask();
		player.setSkilling(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task)) {
					container.stop();
					return;
				}
				final GameObject p = ObjectHandler.getInstance().getObject(x, y, player.getPosition().getZ());
				if (p != null && p.getDef().getId() != id) {
					player.getActionSender().sendMessage("The tree has run out of logs.", true);
					container.stop();
					return;
				}
				final Item log = new Item(tree.getLog(), 1);
				if(player.getInventory().getItemContainer().freeSlot() == -1) {
					player.getActionSender().sendMessage("Your inventory is too full to hold any more " + log.getDefinition().getName().toLowerCase() + ".", true);
					container.stop();
					return;
				}
				if (SkillHandler.doSpawnEvent(player)) {
					SpawnEvent.spawnNpc(player, RandomNpc.TREE_SPIRIT);
				}
				if (Misc.random(900) == 0) { // Birds nest (chance = 281)
                    GroundItem item = new GroundItem(new Item(5070 + Misc.random(4)), player);
                    GroundItemManager.getManager().dropItem(item);
				}
				if (SkillHandler.skillCheck(player.getSkill().getLevel()[Skill.WOODCUTTING], tree.getLevel(), axe.getBonus())) {
					player.getSkill().addExp(Skill.WOODCUTTING, tree.getXP());
					if (log.getId() > 0) {
						player.getInventory().addItem(log);
						if (player.getNewComersSide().isInTutorialIslandStage()) {
							player.getDialogue().sendGiveItemNpc("You get some logs.", new Item(1511));
							if (player.getNewComersSide().getTutorialIslandStage() == 7)
								player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);
							player.setClickId(0);
						} else {
							player.getActionSender().sendMessage("You get some " + log.getDefinition().getName().toLowerCase() + ".");
						}
					}
					if (tree != Tree.DRAMEN_TREE && Misc.random(100) <= tree.decayChance) {
						if (tree != Tree.VINES) {
							player.getActionSender().sendMessage("The tree has run out of logs.", true);
						}
						int face = SkillHandler.getFace(id, x, y, player.getPosition().getZ());
						new GameObject(tree.getStump(), x, y, player.getPosition().getZ(), face, 10, id, tree.getRespawnTime(), tree != Tree.VINES);
						container.stop();
						if (tree == Tree.VINES) {
							ChopVines.walkThru(player, x, y);
						}
						return;
					}
				}
				player.getActionSender().sendSound(472, 0, 0);
				player.getUpdateFlags().sendAnimation(axe.getAnimation(), 0);
			}
			@Override
			public void stop() {
				player.getMovementHandler().reset();
				player.getUpdateFlags().sendAnimation(-1, 0);
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 3);
	}

}
