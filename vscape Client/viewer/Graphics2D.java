package viewer;
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   H


public class Graphics2D /*extends CacheableNode*/ {

    public static int pixels[];
    public static int width;
    public static int height;
    public static int topY = 0;
    public static int bottomY = 0;
    public static int topX = 0;
    public static int bottomX = 0;
    public static int virtualBottomX;
    public static int centerX;
    public static int centerY;

    //draws white & red lines/boundries/walls
    public static final void Z(int i, int j, int k, int l) {
    	//System.out.println(i+","+j+","+k+","+l);
        if (j < topY || j >= bottomY)
            return;
        if (i < topX) {
            k -= topX - i;
            i = topX;
        }
        if (i + k > bottomX)
            k = bottomX - i;
        int i1 = i + j * width;
        for (int j1 = 0; j1 < k; j1++)
            pixels[i1 + j1] = l;

    }

    //draws white & red lines/boundries/walls
    public static final void C(int i, int j, int k, int l) {
    	//System.out.println(i+","+j+","+k+","+l);
        if (i < topX || i >= bottomX)
            return;
        if (j < topY) {
            k -= topY - j;
            j = topY;
        }
        if (j + k > bottomY)
            k = bottomY - j;
        int i1 = i + j * width;
        for (int j1 = 0; j1 < k; j1++)
            pixels[i1 + j1 * width] = l;

    }

    public static final void setCoordinates(int x, int y, int width, int height) {
    	//System.out.println(x+","+y+","+width+","+height);
        if (x < 0)
            x = 0;
        if (y < 0)
            y = 0;
        if (width > Graphics2D.width)
            width = Graphics2D.width;
        if (height > Graphics2D.height)
            height = Graphics2D.height;
        topX = x;
        topY = y;
        bottomX = width;
        bottomY = height;
        virtualBottomX = bottomX - 1;
        centerX = bottomX / 2;
        centerY = bottomY / 2;
    }

    public static final void drawFilledRectangleAlhpa(int x, int y, int width, int height, int color, int alpha) {
        if (x < topX) {
            width -= topX - x;
            x = topX;
        }
        if (y < topY) {
            height -= topY - y;
            y = topY;
        }
        if (x + width > bottomX)
            width = bottomX - x;
        if (y + height > bottomY)
            height = bottomY - y;
        int a = 256 - alpha;
        int r = (color >> 16 & 0xff) * alpha;
        int g = (color >> 8 & 0xff) * alpha;
        int b = (color & 0xff) * alpha;
        int widthOffset = Graphics2D.width - width;
        int pixel = x + y * Graphics2D.width;
        for (int heightCounter = 0; heightCounter < height; heightCounter++) {
            for (int widthCounter = -width; widthCounter < 0; widthCounter++) {
                int red = (pixels[pixel] >> 16 & 0xff) * a;
                int green = (pixels[pixel] >> 8 & 0xff) * a;
                int blue = (pixels[pixel] & 0xff) * a;
                int rgba = ((r + red >> 8) << 16) + ((g + green >> 8) << 8) + (b + blue >> 8);
                pixels[pixel++] = rgba;
            }

            pixel += widthOffset;
        }

    }

    //draws button "drop shadows" / boarders
    public static final void I(int i, int j, int k, int l, int i1) {
    	//System.out.println(i+","+j+","+k+","+l+","+i1);
        Z(i, j, k, i1);
        Z(i, (j + l) - 1, k, i1);
        C(i, j, l, i1);
        C((i + k) - 1, j, l, i1);
    }

    public static final void createRasterizer(int pixels[], int width, int height) {
        Graphics2D.pixels = pixels;
        Graphics2D.width = width;
        Graphics2D.height = height;
        setCoordinates(0, 0, width, height);
    }

    public static final void drawFilledRectangle(int x, int y, int width, int height, int color) {
        if (x < topX) {
            width -= topX - x;
            x = topX;
        }
        if (y < topY) {
            height -= topY - y;
            y = topY;
        }
        if (x + width > bottomX)
            width = bottomX - x;
        if (y + height > bottomY)
            height = bottomY - y;
        int pixelOffset = Graphics2D.width - width;
        int pixel = x + y * Graphics2D.width;
        for (int heightCounter = -height; heightCounter < 0; heightCounter++) {
            for (int widthCounter = -width; widthCounter < 0; widthCounter++)
                pixels[pixel++] = color;

            pixel += pixelOffset;
        }

    }

    public static final void resetPixels() {
        int pixelCount = width * height;
        for (int pixel = 0; pixel < pixelCount; pixel++) {
            pixels[pixel] = 0;
        }
    }

    //draws flashing circles when key items are clicked
    public static final void C(int i, int j, int k, int l, int i1) {
    	//System.out.println(i+","+j+","+k+","+l+","+i1);
        int j1 = 256 - i1;
        int k1 = (l >> 16 & 0xff) * i1;
        int l1 = (l >> 8 & 0xff) * i1;
        int i2 = (l & 0xff) * i1;
        int j2 = j - k;
        if (j2 < 0)
            j2 = 0;
        int k2 = j + k;
        if (k2 >= height)
            k2 = height - 1;
        for (int l2 = j2; l2 <= k2; l2++) {
            int i3 = l2 - j;
            int j3 = (int) Math.sqrt(k * k - i3 * i3);
            int k3 = i - j3;
            if (k3 < 0)
                k3 = 0;
            int l3 = i + j3;
            if (l3 >= width)
                l3 = width - 1;
            int i4 = k3 + l2 * width;
            for (int j4 = k3; j4 <= l3; j4++) {
                int k4 = (pixels[i4] >> 16 & 0xff) * j1;
                int l4 = (pixels[i4] >> 8 & 0xff) * j1;
                int i5 = (pixels[i4] & 0xff) * j1;
                int j5 = ((k1 + k4 >> 8) << 16) + ((l1 + l4 >> 8) << 8) + (i2 + i5 >> 8);
                pixels[i4++] = j5;
            }

        }
    }

    public Graphics2D() {
        // ...
    }
}
