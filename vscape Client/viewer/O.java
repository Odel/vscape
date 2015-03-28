package viewer;
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   O

import java.awt.*;
import java.awt.image.PixelGrabber;

public class O extends Graphics2D {

    public final void I(Font font, FontMetrics fontmetrics, char c, int i, boolean flag, MapShell mapShell) {
        int j = fontmetrics.charWidth(c);
        int k = j;
        if (flag)
            try {
                if (c == '/')
                    flag = false;
                if (c == 'f' || c == 't' || c == 'w' || c == 'v' || c == 'k' || c == 'x' || c == 'y' || c == 'A' || c == 'V' || c == 'W')
                    j++;
            } catch (Exception exception) {
            }
        int i1 = fontmetrics.getMaxAscent();
        int j1 = fontmetrics.getMaxAscent() + fontmetrics.getMaxDescent();
        int k1 = fontmetrics.getHeight();
        Image image = mapShell.J().createImage(j, j1);
        Graphics g = image.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, j, j1);
        g.setColor(Color.white);
        g.setFont(font);
        g.drawString((new StringBuilder()).append(c).append("").toString(), 0, i1);
        if (flag)
            g.drawString((new StringBuilder()).append(c).append("").toString(), 1, i1);
        int ai[] = new int[j * j1];
        PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, j, j1, ai, 0, j);
        try {
            pixelgrabber.grabPixels();
        } catch (Exception exception1) {
        }
        image.flush();
        int l1 = 0;
        int i2 = 0;
        int j2 = j;
        int k2 = j1;
        label0:
        for (int l2 = 0; l2 < j1; l2++) {
            int i4 = 0;
            do {
                if (i4 >= j)
                    continue label0;
                int j5 = ai[i4 + l2 * j];
                if ((j5 & 0xffffff) != 0) {
                    i2 = l2;
                    break label0;
                }
                i4++;
            } while (true);
        }

        label1:
        for (int i3 = 0; i3 < j; i3++) {
            int j4 = 0;
            do {
                if (j4 >= j1)
                    continue label1;
                int k5 = ai[i3 + j4 * j];
                if ((k5 & 0xffffff) != 0) {
                    l1 = i3;
                    break label1;
                }
                j4++;
            } while (true);
        }

        label2:
        for (int j3 = j1 - 1; j3 >= 0; j3--) {
            int k4 = 0;
            do {
                if (k4 >= j)
                    continue label2;
                int l5 = ai[k4 + j3 * j];
                if ((l5 & 0xffffff) != 0) {
                    k2 = j3 + 1;
                    break label2;
                }
                k4++;
            } while (true);
        }

        label3:
        for (int k3 = j - 1; k3 >= 0; k3--) {
            int l4 = 0;
            do {
                if (l4 >= j1)
                    continue label3;
                int i6 = ai[k3 + l4 * j];
                if ((i6 & 0xffffff) != 0) {
                    j2 = k3 + 1;
                    break label3;
                }
                l4++;
            } while (true);
        }

        black[i * 9] = (byte) (append / 16384);
        black[i * 9 + 1] = (byte) (append / 128 & 0x7f);
        black[i * 9 + 2] = (byte) (append & 0x7f);
        black[i * 9 + 3] = (byte) (j2 - l1);
        black[i * 9 + 4] = (byte) (k2 - i2);
        black[i * 9 + 5] = (byte) l1;
        black[i * 9 + 6] = (byte) (i1 - i2);
        black[i * 9 + 7] = (byte) k;
        black[i * 9 + 8] = (byte) k1;
        for (int l3 = i2; l3 < k2; l3++) {
            for (int i5 = l1; i5 < j2; i5++) {
                int j6 = ai[i5 + l3 * j] & 0xff;
                if (j6 > 30 && j6 < 230)
                    I = true;
                black[append++] = (byte) j6;
            }

        }

    }

    
    //blots the Map labels to the drawing surface at pos i,j color k? flag is weather to draw drop shadow or not
    public final void append(String s, int i, int j, int k, boolean flag) {
        try {
            if (I || k == 0)
                flag = false;
            for (int l = 0; l < s.length(); l++) {
                int i1 = charAt[s.charAt(l)];
                if (flag) {
                    charWidth(i1, i + 1, j, 0, black, I);
                    charWidth(i1, i, j + 1, 0, black, I);
                }
                charWidth(i1, i, j, k, black, I);
                i += black[i1 + 7];
            }

        } catch (Exception exception) {
            System.out.println((new StringBuilder()).append(IResourceLoader.I(875)).append(exception).toString());
            exception.printStackTrace();
        }
    }

    public O(int i, boolean flag, MapShell mapShell) {
        I = false;
        append = 0;
        black = new byte[0x186a0];
        append = 855;
        I = false;
        Font font = new Font(IResourceLoader.I(1241), flag ? 1 : 0, i);
        FontMetrics fontmetrics = mapShell.getFontMetrics(font);
        for (int j = 0; j < 95; j++)
            I(font, fontmetrics, IResourceLoader.I(901).charAt(j), j, false, mapShell);

        if (flag && I) {
            append = 855;
            I = false;
            Font font1 = new Font(IResourceLoader.I(1241), 0, i);
            FontMetrics fontmetrics1 = mapShell.getFontMetrics(font1);
            for (int i1 = 0; i1 < 95; i1++)
                I(font1, fontmetrics1, IResourceLoader.I(901).charAt(i1), i1, false, mapShell);

            if (!I) {
                append = 855;
                I = false;
                for (int j1 = 0; j1 < 95; j1++)
                    I(font1, fontmetrics1, IResourceLoader.I(901).charAt(j1), j1, true, mapShell);

            }
        }
        byte abyte0[] = new byte[append];
        for (int k = 0; k < append; k++)
            abyte0[k] = black[k];

        black = abyte0;
    }

    
    //draws map labels at pos i,j if they're on the screen  color k?  flag is weather to draw drop shadows or not
    public final void I(String s, int i, int j, int k, boolean flag) {
    	//System.out.println(s + "," + i  + ","+ j  + "," +k + "," + flag);
        int l = charAt(s) / 2;
        int i1 = C();
        if (i - l > bottomX)
            return;
        if (i + l < topX)
            return;
        if (j - i1 > bottomY)
            return;
        if (j < 0) {
            return;
        } else {
            append(s, i - l, j, k, flag);
            return;
        }
    }

    public final void black(int ai[], byte abyte0[], int i, int j, int k, int l, int i1,
                            int j1, int k1) {
        for (int l1 = -i1; l1 < 0; l1++) {
            for (int i2 = -l; i2 < 0; i2++) {
                int j2 = abyte0[j++] & 0xff;
                if (j2 > 30) {
                    if (j2 >= 230) {
                        ai[k++] = i;
                    } else {
                        int k2 = ai[k];
                        ai[k++] = ((i & 0xff00ff) * j2 + (k2 & 0xff00ff) * (256 - j2) & 0xff00ff00) + ((i & 0xff00) * j2 + (k2 & 0xff00) * (256 - j2) & 0xff0000) >> 8;
                    }
                } else {
                    k++;
                }
            }

            k += j1;
            j += k1;
        }

    }

    public final int I() {
        return black[8] - 1;
    }

    public final int charAt(String s) {
        int i = 0;
        for (int j = 0; j < s.length(); j++) {
            if (s.charAt(j) == '@' && j + 4 < s.length() && s.charAt(j + 4) == '@') {
                j += 4;
                continue;
            }
            if (s.charAt(j) == '~' && j + 4 < s.length() && s.charAt(j + 4) == '~')
                j += 4;
            else
                i += black[charAt[s.charAt(j)] + 7];
        }

        return i;
    }

    public final void charWidth(int i, int j, int k, int l, byte abyte0[], boolean flag) {
        int i1 = j + abyte0[i + 5];
        int j1 = k - abyte0[i + 6];
        int k1 = abyte0[i + 3];
        int l1 = abyte0[i + 4];
        int i2 = abyte0[i] * 16384 + abyte0[i + 1] * 128 + abyte0[i + 2];
        int j2 = i1 + j1 * width;
        int k2 = width - k1;
        int l2 = 0;
        if (j1 < topY) {
            int i3 = topY - j1;
            l1 -= i3;
            j1 = topY;
            i2 += i3 * k1;
            j2 += i3 * width;
        }
        if (j1 + l1 >= bottomY)
            l1 -= ((j1 + l1) - bottomY) + 1;
        if (i1 < topX) {
            int j3 = topX - i1;
            k1 -= j3;
            i1 = topX;
            i2 += j3;
            j2 += j3;
            l2 += j3;
            k2 += j3;
        }
        if (i1 + k1 >= bottomX) {
            int k3 = ((i1 + k1) - bottomX) + 1;
            k1 -= k3;
            l2 += k3;
            k2 += k3;
        }
        if (k1 > 0 && l1 > 0)
            if (flag)
                black(pixels, abyte0, l, i2, j2, k1, l1, k2, l2);
            else
                createImage(pixels, abyte0, l, i2, j2, k1, l1, k2, l2);
    }

    public final void createImage(int ai[], byte abyte0[], int i, int j, int k, int l, int i1, int j1, int k1) {
    	//System.out.println(i +","+ j +","+ k +","+ l +","+ i1 +","+ j1 +","+ k1);
        try {
            int l1 = -(l >> 2);
            l = -(l & 3);
            for (int i2 = -i1; i2 < 0; i2++) {
                for (int j2 = l1; j2 < 0; j2++) {
                    if (abyte0[j++] != 0)
                        ai[k++] = i;
                    else
                        k++;
                    if (abyte0[j++] != 0)
                        ai[k++] = i;
                    else
                        k++;
                    if (abyte0[j++] != 0)
                        ai[k++] = i;
                    else
                        k++;
                    if (abyte0[j++] != 0)
                        ai[k++] = i;
                    else
                        k++;
                }

                for (int k2 = l; k2 < 0; k2++)
                    if (abyte0[j++] != 0)
                        ai[k++] = i;
                    else
                        k++;

                k += j1;
                j += k1;
            }

        } catch (Exception exception) {
            System.out.println((new StringBuilder()).append(IResourceLoader.I(888)).append(exception).toString());
            exception.printStackTrace();
        }
    }

    public final int C() {
        return black[6];
    }

    public boolean I;
    public int append;
    public byte black[];
    public static int charAt[];

    static {
        charAt = new int[256];
        for (int i = 0; i < 256; i++) {
            int j = IResourceLoader.I(901).indexOf(i);
            if (j == -1)
                j = 74;
            charAt[i] = j * 9;
        }

    }
}
