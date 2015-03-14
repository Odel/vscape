package com.rs2.model.content.skills.cooking;

import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.container.Container;
import com.rs2.model.players.item.Item;

public class GnomeCooking {
	private Player player;
	private Container ingredientsInShaker = new Container(Container.Type.ALWAYS_STACK, 6);
	
	public GnomeCooking(Player player) {
		this.player = player;
	}
	
	public static final int COCKTAIL_SHAKER = 2025;
	public static final int COCKTAIL_GLASS = 2026;
	public static final int ODD_COCKTAIL = 2094;
	
	public static final int VODKA = 2015;
	public static final int WHISKY = 2017;
	public static final int GIN = 2019;
	public static final int BRANDY = 2021;
	
	public static final int LEMON = 2102;
	public static final int LEMON_CHUNKS = 2104;
	public static final int LEMON_SLICES = 2106;
	public static final int ORANGE = 2108;
	public static final int ORANGE_CHUNKS = 2110;
	public static final int ORANGE_SLICES = 2112;
	public static final int PINEAPPLE = 2114;
	public static final int PINEAPPLE_CHUNKS = 2116;
	public static final int PINEAPPLE_RING = 2118;
	public static final int LIME = 2120;
	public static final int LIME_CHUNKS = 2122;
	public static final int LIME_SLICES = 2124;
	public static final int DWELLBERRIES = 2126;
	public static final int EQUA_LEAVES = 2128;
	public static final int POT_OF_CREAM = 2130;
	
	public static final int MIXED_BLIZZARD = 9566;
	public static final int MIXED_SHORTGUY = 9567;
	public static final int MIXED_BLAST = 9568;
	public static final int MIXED_PUNCH = 9569;
	public static final int MIXED_SPECIAL = 9570;
	public static final int MIXED_SATURDAY = 9571;
	public static final int MIXED_DRAGON = 9574;
	
	public String[][][] giannePages = { { { "Knead a ball of Gianne\n dough into a gnomebowl mould. Bake this briefly Decadently add four bars of chocolate to the bowl and top with one sprig of equa leaves. Bake the bowl in the oven to melt the chocolate. Then mix in two big dollops of cream" }, {} }

	};
	
	
	public enum CocktailData {
		FRUIT_BLAST(2084, MIXED_BLAST, 6, 50, new int[][] { {PINEAPPLE, 1}, {LEMON, 1}, {ORANGE, 1} }, new int[][] { {LEMON_SLICES, 1} } ),
		PINEAPPLE_PUNCH(2048, MIXED_PUNCH, 8, 70, new int[][] { {PINEAPPLE, 2}, {LEMON, 1}, {ORANGE, 1} }, new int[][] { {LIME_CHUNKS, 1}, {PINEAPPLE_CHUNKS, 1}, {ORANGE_SLICES, 1} } ),
		WIZARD_BLIZZARD(2054, MIXED_BLIZZARD, 18, 110, new int[][] { {VODKA, 2}, {GIN, 1}, {LIME, 1}, {LEMON, 1}, {ORANGE, 1} }, new int[][] { {PINEAPPLE_CHUNKS, 1}, {LIME_SLICES, 1} } ),
		SHORT_GREEN_GUY(2080, MIXED_SHORTGUY, 20, 120, new int[][] { {VODKA, 1}, {LIME, 3} }, new int[][] { {LIME_SLICES, 1}, {EQUA_LEAVES, 1} } ),
		DRUNK_DRAGON(9576, MIXED_DRAGON, 32, 160, new int[][] { {VODKA, 1}, {GIN, 1}, {DWELLBERRIES, 1} }, new int[][] { {PINEAPPLE_CHUNKS, 1}, {POT_OF_CREAM, 1} } ),
		CHOC_SATURDAY(9572, MIXED_SATURDAY, 33, 170, new int[][] { {WHISKY, 1}, {1973, 1}, {EQUA_LEAVES, 1}, {1927, 1} }, new int[][] { {1975, 1}, {POT_OF_CREAM, 1} } ),
		BLURBERRY_SPECIAL(2064, MIXED_SPECIAL, 37, 180, new int[][] { {VODKA, 1}, {BRANDY, 1}, {GIN, 1}, {LEMON, 2}, {ORANGE, 1} }, new int[][] { {LEMON_CHUNKS, 1}, {ORANGE_CHUNKS, 1}, {EQUA_LEAVES, 1}, {LIME_SLICES, 1} } );
		
		private int finalId;
		private int mixedId;
		private int levelReq;
		private int totalExp;
		private int[][] basicIngredients;
		private int[][] secondaryIngredients;
		
		CocktailData(int finalId, int mixedId, int levelReq, int totalExp, int[][] basicIngredients, int[][] secondaryIngredients) {
			this.finalId = finalId;
			this.mixedId = mixedId;
			this.levelReq = levelReq;
			this.totalExp = totalExp;
			this.basicIngredients = basicIngredients;
			this.secondaryIngredients = secondaryIngredients;
		}
		
		public static CocktailData getCocktailByMixed(int id) {
			for (CocktailData c : CocktailData.values()) {
				if (c.getMixedId() == id) {
					return c;
				}
			}
			return null;
		}

		public int[][] getBasicIngredients() {
			return this.basicIngredients;
		}
		
		public int[][] getSecondaryIngredients() {
			return this.secondaryIngredients;
		}
		
		public int getFinalId() {
			return this.finalId;
		}
		
		public int getMixedId() {
			return this.mixedId;
		}
		
		public int getLevelReq() {
			return this.levelReq;
		}
		
		public int getTotalExp() {
			return this.totalExp;
		}
		
	}
	
	public CocktailData findPossibleCocktail() {
		CocktailData drink = null;
		for (CocktailData c : CocktailData.values()) {
			boolean[] toSat = new boolean[c.getBasicIngredients().length];
			boolean failed = false;
			int count = 0;
			for (int[] i : c.getBasicIngredients()) {
				for (Item item : ingredientsInShaker.toArray()) {
					if (item != null && item.getId() == i[0] && item.getCount() == i[1]) {
						toSat[count] = true;
					}
				}
				count++;
			}
			for (boolean b : toSat) {
				if (!b) {
					failed = true;
				}
			}
			if (!failed) {
				drink = c;
				break;
			}
		}
		return drink;
	}
	
	public boolean hasSecondaryIngredients(CocktailData c) {
		boolean satisfied = true;
		for(int[] i : c.getSecondaryIngredients()) {
			if(!player.getInventory().playerHasItem(new Item(i[0], i[1])))
				satisfied = false;
		}
		return satisfied;
	}
	
	public void removeSecondaryIngredients(CocktailData c) {
		for(int[] i : c.getSecondaryIngredients()) {
			player.getInventory().removeItem(new Item(i[0], i[1]));
		}
	}
	
	public void mixCocktail() {
		if (ingredientsInShaker.size() == 0) {
			player.getActionSender().sendMessage("Your shaker is empty, you have nothing to mix or pour.");
			return;
		}
		CocktailData c = findPossibleCocktail();
		if (c != null) {
			if(player.getSkill().getPlayerLevel(Skill.COOKING) < c.getLevelReq()) {
				player.getDialogue().sendStatement("You need a Cooking level of " + c.getLevelReq() + " to make this cocktail.");
				return;
			}
			player.getSkill().addExp(Skill.COOKING, 30);
			ingredientsInShaker.clear();
			if (hasSecondaryIngredients(c) && player.getInventory().playerHasItem(COCKTAIL_GLASS)) {
				player.getActionSender().sendMessage("You mix and pour the cocktail and finish it with the rest of the ingredients.");
				player.getInventory().replaceItemWithItem(new Item(COCKTAIL_GLASS), new Item(c.getFinalId()));
				removeSecondaryIngredients(c);
				player.getSkill().addExp(Skill.COOKING, c.getTotalExp() - 30);
			} else if (hasSecondaryIngredients(c) && !player.getInventory().playerHasItem(COCKTAIL_GLASS)) {
				player.getActionSender().sendMessage("You mix the cocktail ingredients, but can't finish the drink without a glass.");
				player.getInventory().replaceItemWithItem(new Item(COCKTAIL_SHAKER), new Item(c.getMixedId()));
			} else {
				player.getActionSender().sendMessage("You mix the cocktail ingredients.");
				player.getInventory().replaceItemWithItem(new Item(COCKTAIL_SHAKER), new Item(c.getMixedId()));
			}
		} else {
			if (player.getInventory().playerHasItem(COCKTAIL_GLASS)) {
				player.getActionSender().sendMessage("You mix and pour the cocktail?");
				player.getInventory().replaceItemWithItem(new Item(COCKTAIL_GLASS), new Item(ODD_COCKTAIL));
				ingredientsInShaker.clear();
			} else {
				player.getDialogue().sendStatement("You have nothing to pour these ingredients into, and they", "don't seem to mix together into anything.");
			}
		}
	}
	
	public boolean itemOnItemHandling(int firstItem, int secondItem, int firstSlot, int secondSlot) {
		switch(firstItem) {
			case VODKA:
			case BRANDY:
			case GIN:
			case WHISKY:
			case LEMON:
			case ORANGE:
			case LIME:
			case LIME_SLICES:
			case PINEAPPLE:
			case EQUA_LEAVES:
			case DWELLBERRIES:
			case 1973: //choco bar
			case 1927: //bucket of milk
				if(secondItem == COCKTAIL_SHAKER) {
						if(ingredientsInShaker.size() == 6) {
							player.getActionSender().sendMessage("The shaker is full. Mix and pour it into a cocktail glass.");
							return true;
						}
						if(firstItem != 1927 && firstItem != 1973) {
							player.getActionSender().sendMessage("You add " + new Item(firstItem).getDefinition().getName().toLowerCase() + " to the shaker.");
						} else {
							if(firstItem == 1927)
								player.getActionSender().sendMessage("You add milk to the shaker.");
							else
								player.getActionSender().sendMessage("You add chocolate to the shaker.");
						}
						if(firstItem == 1927) {
							player.getInventory().replaceItemWithItem(new Item(1927), new Item(1925));
						} else {
							player.getInventory().removeItem(new Item(firstItem));
						}
						if (ingredientsInShaker.contains(firstItem)) {
							Item existing = ingredientsInShaker.getById(firstItem);
							ingredientsInShaker.remove(existing);
							Item toAdd = new Item(existing.getId(), existing.getCount() + 1);
							ingredientsInShaker.add(toAdd);
						} else {
							ingredientsInShaker.add(new Item(firstItem));
						}
						return true;
				}
			return false;
			case COCKTAIL_SHAKER:
				if(secondItem == COCKTAIL_GLASS) {
					mixCocktail();
					return true;
				}
		}
		return false;
	}
	
	public boolean itemHandling(int itemId) {
		switch (itemId) {
			case ODD_COCKTAIL:
				player.getActionSender().sendMessage("You drink the odd cocktail... Disgusting!");
				player.getInventory().replaceItemWithItem(new Item(itemId), new Item(2026));
				return true;
			case 2084:
			case 2034: //Fruit blast
				player.getUpdateFlags().sendAnimation(829);
				player.heal(3);
				player.getActionSender().sendMessage("You drink the fruit blast.");
				player.getInventory().replaceItemWithItem(new Item(itemId), new Item(2026));
				return true;
			case 2048:
			case 2036: //Pineapple punch
				player.getUpdateFlags().sendAnimation(829);
				player.heal(3);
				player.getActionSender().sendMessage("You drink the pineapple punch.");
				player.getInventory().replaceItemWithItem(new Item(itemId), new Item(2026));
				return true;
			case 2064:
			case 2028: //Blurberry special
				player.getActionSender().statEdit(Skill.ATTACK, -4, false);
				player.getActionSender().statEdit(Skill.STRENGTH, 7, true);
				player.heal(5);
				player.getActionSender().sendMessage("You drink the Blurberry special.");
				player.getInventory().replaceItemWithItem(new Item(itemId), new Item(2026));
				player.setDrunkState(true, 100);
				return true;
			case 2054:
			case 2040: //Wizard blizzard
				player.getActionSender().statEdit(Skill.ATTACK, -4, false);
				player.getActionSender().statEdit(Skill.STRENGTH, 6, true);
				player.heal(4);
				player.getActionSender().sendMessage("You drink the wizard blizzard.");
				player.getInventory().replaceItemWithItem(new Item(itemId), new Item(2026));
				player.setDrunkState(true, 100);
				return true;
			case 2080:
			case 2038: //Short green guy
				player.getActionSender().statEdit(Skill.ATTACK, -3, false);
				player.getActionSender().statEdit(Skill.STRENGTH, 4, true);
				player.heal(4);
				player.getActionSender().sendMessage("You drink the short green guy.");
				player.getInventory().replaceItemWithItem(new Item(itemId), new Item(2026));
				player.setDrunkState(true, 100);
				return true;
			case 2092:
			case 2032: //Drunk dragon
				player.getActionSender().statEdit(Skill.ATTACK, -4, false);
				player.getActionSender().statEdit(Skill.STRENGTH, 7, true);
				player.heal(5);
				player.getActionSender().sendMessage("You drink the drunk dragon.");
				player.getInventory().replaceItemWithItem(new Item(itemId), new Item(2026));
				player.setDrunkState(true, 100);
				return true;
			case 2074:
			case 2030: //Choc saturday
				player.getActionSender().statEdit(Skill.ATTACK, -4, false);
				player.getActionSender().statEdit(Skill.STRENGTH, 6, true);
				player.heal(5);
				player.getActionSender().sendMessage("You drink the choc' saturday.");
				player.getInventory().replaceItemWithItem(new Item(itemId), new Item(2026));
				player.setDrunkState(true, 100);
				return true;
			case MIXED_SPECIAL:
			case MIXED_BLIZZARD:
			case MIXED_SHORTGUY:
			case MIXED_BLAST:
			case MIXED_PUNCH:
			case MIXED_DRAGON:
			case MIXED_SATURDAY:
				CocktailData c = CocktailData.getCocktailByMixed(itemId);
				if (c != null) {
					if (hasSecondaryIngredients(c)) {
						if (player.getInventory().playerHasItem(COCKTAIL_GLASS)) {
							player.getActionSender().sendMessage("You mix and pour the rest of the ingredients into the cocktail.");
							removeSecondaryIngredients(c);
							player.getInventory().replaceItemWithItem(new Item(itemId), new Item(COCKTAIL_SHAKER));
							player.getInventory().replaceItemWithItem(new Item(COCKTAIL_GLASS), new Item(c.getFinalId()));
							player.getSkill().addExp(Skill.COOKING, c.getTotalExp() - 30);
						} else {
							player.getDialogue().sendStatement("You need an empty cocktail glass to do this.");
						}
					} else {
						player.getDialogue().sendStatement("You do not have the secondary ingredients required to", "finish this cocktail mix.");
					}
				}
				return true;
			case COCKTAIL_SHAKER:
				if (ingredientsInShaker.size() == 0) {
					player.getActionSender().sendMessage("Your shaker is empty, you have nothing to mix or pour.");
				} else {
					mixCocktail();
				}
				return true;
			
		}
		return false;
	}
}
