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
	
	public static final int PREMADE_BLUR = 2028;
	public static final int PREMADE_CHOC = 2030;
	public static final int PREMADE_DRAGON = 2032;
	public static final int PREMADE_FRUIT = 2034;
	public static final int PREMADE_PINEAPPLE = 2036;
	public static final int PREMADE_SHORTGUY = 2038;
	public static final int PREMADE_WIZARD = 2040;
	
	public String[][][] giannePages = { { { "Knead a ball of Gianne\n dough into a gnomebowl mould. Bake this briefly Decadently add four bars of chocolate to the bowl and top with one sprig of equa leaves. Bake the bowl in the oven to melt the chocolate. Then mix in two big dollops of cream" }, {} }

	};
	
	
	public enum CocktailData {
		BLURBERRY(2064, PREMADE_BLUR, 37, 180, new int[][] { {VODKA, 1}, {BRANDY, 1}, {GIN, 1}, {LEMON, 2}, {ORANGE, 1} }, new int[][] { {LEMON_CHUNKS, 1}, {ORANGE_CHUNKS, 1}, {EQUA_LEAVES, 1}, {LIME_SLICES, 1} } );
		
		private int finalId;
		private int premadeId;
		private int levelReq;
		private int totalExp;
		private int[][] basicIngredients;
		private int[][] secondaryIngredients;
		
		CocktailData(int finalId, int premadeId, int levelReq, int totalExp, int[][] basicIngredients, int[][] secondaryIngredients) {
			this.finalId = finalId;
			this.premadeId = premadeId;
			this.levelReq = levelReq;
			this.totalExp = totalExp;
			this.basicIngredients = basicIngredients;
			this.secondaryIngredients = secondaryIngredients;
		}
		
		public static CocktailData getCocktailByPremade(int premade) {
			for (CocktailData c : CocktailData.values()) {
				if (c.getPremadeId() == premade) {
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
		
		public int getPremadeId() {
			return this.premadeId;
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
			player.getActionSender().sendMessage("Your shaker is empty, you have nothing to pour.");
			return;
		}
		CocktailData c = findPossibleCocktail();
		ingredientsInShaker.clear();
		if (c != null) {
			if(player.getSkill().getPlayerLevel(Skill.COOKING) < c.getLevelReq()) {
				player.getDialogue().sendStatement("You need a Cooking level of " + c.getLevelReq() + " to make this cocktail.");
				return;
			}
			player.getSkill().addExp(Skill.COOKING, 30);
			if (hasSecondaryIngredients(c)) {
				player.getActionSender().sendMessage("You pour the cocktail and finish it with the rest of the ingredients.");
				player.getInventory().replaceItemWithItem(new Item(COCKTAIL_GLASS), new Item(c.getFinalId()));
				removeSecondaryIngredients(c);
				player.getSkill().addExp(Skill.COOKING, c.getTotalExp() - 30);
			} else {
				player.getActionSender().sendMessage("You pour the cocktail.");
				player.getInventory().replaceItemWithItem(new Item(COCKTAIL_GLASS), new Item(c.getPremadeId()));
			}
		} else {
			player.getActionSender().sendMessage("You pour the cocktail.");
			player.getInventory().replaceItemWithItem(new Item(COCKTAIL_GLASS), new Item(ODD_COCKTAIL));
		}
	}
	
	public boolean itemOnItemHandling(int firstItem, int secondItem, int firstSlot, int secondSlot) {
		switch(firstItem) {
			case VODKA:
			case BRANDY:
			case GIN:
			case WHISKY:
			case LEMON:
			case LEMON_CHUNKS:
			case LEMON_SLICES:
			case ORANGE:
			case ORANGE_CHUNKS:
			case ORANGE_SLICES:
			case LIME:
			case LIME_CHUNKS:
			case LIME_SLICES:
			case POT_OF_CREAM:
			case DWELLBERRIES:
			case 1973:
			case 1975: //choco bar and dust
			case 1927: //bucket of milk
				switch(secondItem) {
					case PREMADE_BLUR:
					case PREMADE_CHOC:
					case PREMADE_DRAGON:
					case PREMADE_FRUIT:
					case PREMADE_PINEAPPLE:
					case PREMADE_SHORTGUY:
					case PREMADE_WIZARD:
						CocktailData c = CocktailData.getCocktailByPremade(secondItem);
						if(c != null) {
							if(hasSecondaryIngredients(c)) {
								player.getActionSender().sendMessage("You mix the rest of the ingredients into the cocktail.");
								removeSecondaryIngredients(c);
								player.getInventory().replaceItemWithItem(new Item(secondItem), new Item(c.getFinalId()));
								player.getSkill().addExp(Skill.COOKING, c.getTotalExp() - 30);
								return true;
							}
						}
						return true;
					case COCKTAIL_SHAKER:
						player.getActionSender().sendMessage("You add " + new Item(firstItem).getDefinition().getName().toLowerCase() + " to the shaker.");
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
			case COCKTAIL_SHAKER:
				if(player.getInventory().playerHasItem(COCKTAIL_GLASS)) {
					mixCocktail();
				} else {
					player.getDialogue().sendStatement("You need a cocktail glass to pour the shaker mix into.");
				}
			return true;
			
		}
		return false;
	}
}
