import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;


public class SpriteLoader {
	
	public static class SpriteArchive {
		public String name;
        public int totalSprites;
        public Sprite[] sprites = null;
		public SpriteArchive(String name){
			this.name = name;
		}
		
		public void readValues(DataInputStream index, DataInputStream data) throws IOException {
			totalSprites = index.readInt();
			sprites = new Sprite[totalSprites];
			for (int i = 0; i < totalSprites; i++) {
				int bufferLength = data.readInt();
				byte[] spriteData = new byte[bufferLength];
				data.readFully(spriteData);
				sprites[i] = new Sprite(spriteData);
			}
		}
		
		public Sprite getSprite(int spriteIndex){
			if(spriteIndex >= 0 && spriteIndex <= totalSprites)
			{
				return sprites[spriteIndex];
			}
			return null;
		}
	}

	public static SpriteArchive[] cache;
	
	public static void loadSprites(StreamLoader archive) {
		try {
			Stream index = new Stream(DataUtils.readFile(Signlink.findcachedir() + "sprites.idx"));
			Stream data = new Stream(DataUtils.readFile(Signlink.findcachedir() + "sprites.dat"));
			DataInputStream indexFile = new DataInputStream(new GZIPInputStream(new ByteArrayInputStream(index.buffer)));
			DataInputStream dataFile = new DataInputStream(new GZIPInputStream(new ByteArrayInputStream(data.buffer)));

			int totalArchives = indexFile.readInt();
			if (cache == null) {
				cache = new SpriteArchive[totalArchives];
			}
			for (int i = 0; i < totalArchives; i++) {
				int namelen = indexFile.readInt();
				byte[] nameBytes = new byte[namelen];
				indexFile.readFully(nameBytes);
				if (cache[i] == null) {
					cache[i] = new SpriteArchive(new String(nameBytes));
				}
				cache[i].readValues(indexFile, dataFile);
			}
			indexFile.close();
			dataFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static SpriteArchive getArchive(String archive){
		for(SpriteArchive sa : cache)
		{
			if(sa.name.equalsIgnoreCase(archive))
			{
				return sa;
			}
		}
		return null;
	}
	
	public static Sprite getSprite(String archive, int spriteIndex){
		SpriteArchive sa = getArchive(archive);
		if(sa != null)
		{
			if(spriteIndex >= 0 && spriteIndex <= sa.totalSprites)
			{
				return sa.sprites[spriteIndex];
			}
		}
		return null;
	}
}
