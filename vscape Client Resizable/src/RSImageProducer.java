import java.util.*;
import java.awt.*;
import java.awt.image.*;

public final class RSImageProducer {

	public RSImageProducer(int anInt316, int anInt317, Component component) {
		this.anInt316 = anInt316;
		this.anInt317 = anInt317;
		this.component = component;
		int count = anInt316 * anInt317;
		anIntArray315 = new int[count];
		image = new BufferedImage(COLOR_MODEL, Raster.createWritableRaster(COLOR_MODEL.createCompatibleSampleModel(anInt316, anInt317), new DataBufferInt(anIntArray315, count), null), false, new Hashtable<Object, Object>());
		initDrawingArea();
	}

	public void drawGraphics(int y, Graphics gfx, int x) {
		draw(gfx, x, y);
	}

	public void draw(Graphics gfx, int x, int y) {
		gfx.drawImage(image, x, y, component);
	}

	public void draw(Graphics gfx, int x, int y, int clipX, int clipY, int clipWidth, int clipHeight) {
		Shape tmp = gfx.getClip();
		try {
			clip.x = clipX;
			clip.y = clipY;
			clip.width = clipWidth;
			clip.height = clipHeight;
			gfx.setClip(clip);
			gfx.drawImage(image, x, y, component);
		} finally {
			gfx.setClip(tmp);
		}
	}

	public void initDrawingArea() {
		DrawingArea.initDrawingArea(anInt317, anInt316, anIntArray315);
	}

	public final int[] anIntArray315;
	public final int anInt316;
	public final int anInt317;
	public final BufferedImage image;
	public final Component component;
	private final Rectangle clip = new Rectangle();
	private static final ColorModel COLOR_MODEL = new DirectColorModel(32, 0xff0000, 0xff00, 0xff);
}