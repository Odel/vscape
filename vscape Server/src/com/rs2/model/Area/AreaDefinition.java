package com.rs2.model.Area;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rs2.model.Entity;
import com.rs2.model.Position;

public class AreaDefinition {
	
	class Area {
		class Coordinates {
			private int x;
			private int y;
			private int z;
			
			public Coordinates(int x, int y, int z)
			{
				this.x = x;
				this.y = y;
				this.z = z;
			}
			
			public int getX() {
				return x;
			}
			
			public int getY() {
				return y;
			}
			
			public int getZ() {
				return z;
			}
		}
		
		private Coordinates sw;
		private Coordinates ne;
		
		private Area(Coordinates sw, Coordinates ne) {
			this.sw = sw;
			this.ne = ne;
		}
		
		public Coordinates getSw() {
			return sw;
		}
		
		public Coordinates getNe() {
			return ne;
		}
	}
	
	private static ArrayList<AreaDefinition> areaDefinitions = new ArrayList<AreaDefinition>();
	
	public static void init() throws IOException {
		FileReader reader = new FileReader("./datajson/world/areas.json");
		try
		{
			List<AreaDefinition> defs = new Gson().fromJson(reader, new TypeToken<List<AreaDefinition>>(){}.getType());
			if(defs.size() > 0)
			{
				for (final AreaDefinition def : defs) {
					if(def != null && !areaDefinitions.contains(def))
					{
						areaDefinitions.add(def);
					}
				}
			}
			reader.close();
			System.out.println("Loaded " + defs.size() + " area definitions json.");
		} catch (IOException e) {
			reader.close();
			System.out.println("failed to load area definitions json.");
		}
	}
	
	private String name;
	private Area[] areas;
	
	private AreaDefinition(String name, Area[] areas) {
		this.name = name;
		this.areas = areas;
	}
	
	public static AreaDefinition forName(String name) {
		for (AreaDefinition d : areaDefinitions) {
			if (d.getName().toLowerCase().equalsIgnoreCase(name.toLowerCase())) {
				return d;
			}
		}
		return null;
	}
	
	public String getName() {
		return name;
	}
	
	public Area[] getAreas() {
		return areas;
	}
	
	public boolean inArea(final Entity ent){
		return inArea(ent.getPosition());
	}
	
	public boolean inArea(final Position pos){
		if(getAreas() != null) {
			for (Area a : getAreas()) {
				if(a == null)
				{
					continue;
				}
				if(a.getSw().z == -1 || a.getNe().z == -1) { // Check all heights
					return pos.getX() >= a.getSw().getX() && pos.getX() <= a.getNe().getX() && pos.getY() >= a.getSw().getY() && pos.getY() <= a.getNe().getY();
				} else if(a.getSw().z >= 0 && a.getNe().z >= 0) { // check specific heights
					return pos.getX() >= a.getSw().getX() && pos.getX() <= a.getNe().getX() && pos.getY() >= a.getSw().getY() && pos.getY() <= a.getNe().getY() && pos.getZ() >= a.getSw().getZ() && pos.getZ() <= a.getNe().getZ();
				}
			}
		}
		return false;
	}
}
