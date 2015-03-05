package com.rs2.util;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.rs2.cache.object.GameObjectData;
import com.rs2.model.content.consumables.Food;
import com.rs2.model.content.consumables.PotionLoader;
import com.rs2.model.content.consumables.PotionLoader.PotionDefinition;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcDefinition;
import com.rs2.model.npcs.drop.NpcDropController;
import com.rs2.model.npcs.drop.NpcDropItem;
import com.rs2.model.players.GlobalObject;
import com.rs2.model.players.GlobalObjectHandler;
import com.rs2.model.players.ShopManager;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;
import com.thoughtworks.xstream.XStream;

/**
 * Class handling all XStream
 * 
 * @author BFMV
 * 
 */
public class XStreamUtil {

	private static XStreamUtil instance = new XStreamUtil();
	private static XStream xStream = new XStream();

	public static XStreamUtil getInstance() {
		return instance;
	}

	public static XStream getxStream() {
		return xStream;
	}

	static {
		xStream.alias("objectDefinition", GameObjectData.class);
		xStream.alias("potiondef", PotionDefinition.class);
		xStream.alias("object", GlobalObject.class);
	}

	public static void loadAllFiles() throws FileNotFoundException, IOException {
		//NpcLoader.loadSpawns();
		PotionLoader.loadPotionDefinitions();
		GlobalObjectHandler.loadObjects();
	}

}
