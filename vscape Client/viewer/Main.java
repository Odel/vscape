package viewer;
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Main

import java.io.*;
import java.net.URL;

public class Main extends MapShell {
	
	static int initwidth = 800;
	static int initheight = 700;
	
	int newwidth = initwidth;
	int newheight = initheight;
    public static final Main main(String args[]) {
        Main main1 = new Main();
        main1.I(initwidth, initheight);
        return main1;
    }

    public final void init() {
        Z(newwidth, newheight);
    }

    public final void I() {
        E e1 = mkdir();
        I(100, IResourceLoader.I(1116));
        IoBuffer i1 = new IoBuffer(e1.I(IResourceLoader.I(1145), null));
        openStream = i1.readShort();
        out = i1.readShort();
        printStackTrace = i1.readShort();
        println = i1.readShort();
        CI = 3200 - openStream;
        BI = (out + println) - 3200;
        s = 180;
        a = (printStackTrace * s) / println;
        e = newwidth - a - 5;
        g = newheight - s - 20;
        i1 = new IoBuffer(e1.I(IResourceLoader.I(1154), null));
        q = i1.readShort();
        for (int j1 = 0; j1 < q; j1++) {
            t[j1] = i1.readString();
            u[j1] = i1.readShort();
            v[j1] = i1.readShort();
            w[j1] = i1.readUnsigned();
        }

        i1 = new IoBuffer(e1.I(IResourceLoader.I(1165), null));
        int k1 = i1.readShort();
        read = new int[k1 + 1];
        substring = new int[k1 + 1];
        for (int l1 = 0; l1 < k1; l1++) {
            read[l1 + 1] = i1.readInt();
            substring[l1 + 1] = i1.readInt();
        }

        byte abyte0[] = e1.I(IResourceLoader.I(1178), null);
        byte abyte1[][] = new byte[printStackTrace][println];
        charAt(abyte0, abyte1);
        byte abyte2[] = e1.I(IResourceLoader.I(1191), null);
        write = new int[printStackTrace][println];
        I = new byte[printStackTrace][println];
        close(abyte2, write, I);
        byte abyte3[] = e1.I(IResourceLoader.I(1203), null);
        Z = new byte[printStackTrace][println];
        B = new byte[printStackTrace][println];
        C = new byte[printStackTrace][println];
        append(abyte3, Z, B, C);
        try {
            for (int i2 = 0; i2 < 100; i2++)
                D[i2] = new ClutterSprite(e1, IResourceLoader.I(1211), i2);

        } catch (Exception exception) {
        }
        try {
            for (int j2 = 0; j2 < 100; j2++)
                F[j2] = new B(e1, IResourceLoader.I(1220), j2);

        } catch (Exception exception1) {
        }
        J = new F(e1, IResourceLoader.I(1232), false);
        S = new O(11, true, this);
        A = new O(12, true, this);
        E = new O(14, true, this);
        o2 = new O(17, true, this);
        H = new O(19, true, this);
        o1 = new O(22, true, this);
        L = new O(26, true, this);
        M = new O(30, true, this);
        toString = new int[printStackTrace][println];
        equals(abyte1, toString);
        k = new B(a, s);
        k.I();
        indexOf(0, 0, printStackTrace, println, 0, 0, a, s);
        H.I(0, 0, a, s, 0);
        H.I(1, 1, a - 2, s - 2, charAt);
        super.JI.createRasterizer();
    }

    public final void append(byte abyte0[], byte abyte1[][], byte abyte2[][], byte abyte3[][]) {
        for (int i1 = 0; i1 < abyte0.length; ) {
            int j1 = (abyte0[i1++] & 0xff) * 64 - openStream;
            int k1 = (abyte0[i1++] & 0xff) * 64 - out;
            if (j1 > 0 && k1 > 0 && j1 + 64 < printStackTrace && k1 + 64 < println) {
                int l1 = 0;
                while (l1 < 64) {
                    byte abyte4[] = abyte1[l1 + j1];
                    byte abyte5[] = abyte2[l1 + j1];
                    byte abyte6[] = abyte3[l1 + j1];
                    int k2 = println - k1 - 1;
                    for (int l2 = -64; l2 < 0; l2++) {
                        do {
                            int i3 = abyte0[i1++] & 0xff;
                            if (i3 == 0)
                                break;
                            if (i3 < 29)
                                abyte4[k2] = (byte) i3;
                            else if (i3 < 160) {
                                abyte5[k2] = (byte) (i3 - 28);
                            } else {
                                abyte6[k2] = (byte) (i3 - 159);
                                R[Q] = l1 + j1;
                                T[Q] = k2;
                                U[Q] = i3 - 160;
                                Q++;
                            }
                        } while (true);
                        k2--;
                    }

                    l1++;
                }
            } else {
                int i2 = 0;
                while (i2 < 64) {
                    for (int j2 = -64; j2 < 0; j2++) {
                        byte byte0;
                        do
                            byte0 = abyte0[i1++];
                        while (byte0 != 0);
                    }

                    i2++;
                }
            }
        }

    }

    public final void charAt(byte abyte0[], byte abyte1[][]) {
        for (int i1 = 0; i1 < abyte0.length; ) {
            int j1 = (abyte0[i1++] & 0xff) * 64 - openStream;
            int k1 = (abyte0[i1++] & 0xff) * 64 - out;
            if (j1 > 0 && k1 > 0 && j1 + 64 < printStackTrace && k1 + 64 < println) {
                int l1 = 0;
                while (l1 < 64) {
                    byte abyte2[] = abyte1[l1 + j1];
                    int i2 = println - k1 - 1;
                    for (int j2 = -64; j2 < 0; j2++)
                        abyte2[i2--] = abyte0[i1++];

                    l1++;
                }
            } else {
                i1 += 4096;
            }
        }

    }

    public final void close(byte abyte0[], int ai[][], byte abyte1[][]) {
        for (int i1 = 0; i1 < abyte0.length; ) {
            int j1 = (abyte0[i1++] & 0xff) * 64 - openStream;
            int k1 = (abyte0[i1++] & 0xff) * 64 - out;
            if (j1 > 0 && k1 > 0 && j1 + 64 < printStackTrace && k1 + 64 < println) {
                int l1 = 0;
                while (l1 < 64) {
                    int ai1[] = ai[l1 + j1];
                    byte abyte2[] = abyte1[l1 + j1];
                    int j2 = println - k1 - 1;
                    for (int k2 = -64; k2 < 0; k2++) {
                        byte byte1 = abyte0[i1++];
                        if (byte1 != 0) {
                            abyte2[j2] = abyte0[i1++];
                            int l2 = 0;
                            if (byte1 > 0)
                                l2 = substring[byte1];
                            ai1[j2--] = l2;
                        } else {
                            ai1[j2--] = 0;
                        }
                    }

                    l1++;
                }
            } else {
                int i2 = -4096;
                while (i2 < 0) {
                    byte byte0 = abyte0[i1++];
                    if (byte0 != 0)
                        i1++;
                    i2++;
                }
            }
        }

    }

    public final void equals(byte abyte0[][], int ai[][]) {
        int i1 = printStackTrace;
        int j1 = println;
        int ai1[] = new int[j1];
        for (int k1 = 0; k1 < j1; k1++)
            ai1[k1] = 0;

        for (int l1 = 5; l1 < i1 - 5; l1++) {
            byte abyte1[] = abyte0[l1 + 5];
            byte abyte2[] = abyte0[l1 - 5];
            for (int i2 = 0; i2 < j1; i2++)
                ai1[i2] += read[abyte1[i2] & 0xff] - read[abyte2[i2] & 0xff];

            if (l1 <= 10 || l1 >= i1 - 10)
                continue;
            int j2 = 0;
            int k2 = 0;
            int l2 = 0;
            int ai2[] = ai[l1];
            for (int i3 = 5; i3 < j1 - 5; i3++) {
                int j3 = ai1[i3 - 5];
                int k3 = ai1[i3 + 5];
                j2 += (k3 >> 20) - (j3 >> 20);
                k2 += (k3 >> 10 & 0x3ff) - (j3 >> 10 & 0x3ff);
                l2 += (k3 & 0x3ff) - (j3 & 0x3ff);
                if (l2 > 0)
                    ai2[i3] = exists((double) j2 / 8533D, (double) k2 / 8533D, (double) l2 / 8533D);
            }

        }

    }

    public final int exists(double d1, double d2, double d3) {
        double d4 = d3;
        double d5 = d3;
        double d6 = d3;
        if (d2 != 0.0D) {
            double d7;
            if (d3 < 0.5D)
                d7 = d3 * (1.0D + d2);
            else
                d7 = (d3 + d2) - d3 * d2;
            double d8 = 2D * d3 - d7;
            double d9 = d1 + 0.33333333333333331D;
            if (d9 > 1.0D)
                d9--;
            double d10 = d1;
            double d11 = d1 - 0.33333333333333331D;
            if (d11 < 0.0D)
                d11++;
            if (6D * d9 < 1.0D)
                d4 = d8 + (d7 - d8) * 6D * d9;
            else if (2D * d9 < 1.0D)
                d4 = d7;
            else if (3D * d9 < 2D)
                d4 = d8 + (d7 - d8) * (0.66666666666666663D - d9) * 6D;
            else
                d4 = d8;
            if (6D * d10 < 1.0D)
                d5 = d8 + (d7 - d8) * 6D * d10;
            else if (2D * d10 < 1.0D)
                d5 = d7;
            else if (3D * d10 < 2D)
                d5 = d8 + (d7 - d8) * (0.66666666666666663D - d10) * 6D;
            else
                d5 = d8;
            if (6D * d11 < 1.0D)
                d6 = d8 + (d7 - d8) * 6D * d11;
            else if (2D * d11 < 1.0D)
                d6 = d7;
            else if (3D * d11 < 2D)
                d6 = d8 + (d7 - d8) * (0.66666666666666663D - d11) * 6D;
            else
                d6 = d8;
        }
        int i1 = (int) (d4 * 256D);
        int j1 = (int) (d5 * 256D);
        int k1 = (int) (d6 * 256D);
        int l1 = (i1 << 16) + (j1 << 8) + k1;
        return l1;
    }

    public final void Z() {
        try {
            read = null;
            substring = null;
            toString = (int[][]) null;
            write = (int[][]) null;
            I = (byte[][]) null;
            Z = (byte[][]) null;
            C = (byte[][]) null;
            B = (byte[][]) null;
            D = null;
            F = null;
            J = null;
            N = null;
            O = null;
            P = null;
            R = null;
            T = null;
            U = null;
            k = null;
            t = null;
            u = null;
            v = null;
            w = null;
            DI = null;
            System.gc();
            return;
        } catch (Throwable throwable) {
            return;
        }
    }

    public final void C() {
        if (super.MI[1] == 1) {
            CI = (int) ((double) CI - 16D / II);
            length = true;
        }
        if (super.MI[2] == 1) {
            CI = (int) ((double) CI + 16D / II);
            length = true;
        }
        if (super.MI[3] == 1) {
            BI = (int) ((double) BI - 16D / II);
            length = true;
        }
        if (super.MI[4] == 1) {
            BI = (int) ((double) BI + 16D / II);
            length = true;
        }
        int i1 = 1;
        do {
            if (i1 <= 0)
                break;
            i1 = F();
            if (i1 == 49) {
                ZI = 3D;
                length = true;
            }
            if (i1 == 50) {
                ZI = 4D;
                length = true;
            }
            if (i1 == 51) {
                ZI = 6D;
                length = true;
            }
            if (i1 == 52) {
                ZI = 8D;
                length = true;
            }
            if (i1 == 107 || i1 == 75) {
                drawkey = !drawkey;
                length = true;
            }
            if (i1 == 111 || i1 == 79) {
                drawoverview = !drawoverview;
                length = true;
            }
            if (super.SI != null && i1 == 101) {
                System.out.println(IResourceLoader.I(1050));
                B b1 = new B(printStackTrace * 2, println * 2);
                b1.I();
                indexOf(0, 0, printStackTrace, println, 0, 0, printStackTrace * 2, println * 2);
                super.JI.createRasterizer();
                int i2 = b1.I.length;
                byte abyte0[] = new byte[i2 * 3];
                int l2 = 0;
                for (int i3 = 0; i3 < i2; i3++) {
                    int j3 = b1.I[i3];
                    abyte0[l2++] = (byte) (j3 >> 16);
                    abyte0[l2++] = (byte) (j3 >> 8);
                    abyte0[l2++] = (byte) j3;
                }

                System.out.println(IResourceLoader.I(1069));
                try {
                    BufferedOutputStream bufferedoutputstream = new BufferedOutputStream(new FileOutputStream((new StringBuilder()).append(IResourceLoader.I(1084)).append(printStackTrace * 2).append(IResourceLoader.I(1089)).append(println * 2).append(IResourceLoader.I(1091)).toString()));
                    bufferedoutputstream.write(abyte0);
                    bufferedoutputstream.close();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                System.out.println((new StringBuilder()).append(IResourceLoader.I(1100)).append(printStackTrace * 2).append(IResourceLoader.I(1114)).append(println * 2).toString());
            }
        } while (true);
        if (super.HI == 1) {
            l = super.KI;
            m = super.LI;
            n = CI;
            o = BI;
            if (super.KI > 170 && super.KI < 220 && super.LI > (newheight-30) && super.LI < newheight) {
                ZI = 3D;
                l = -1;
            }
            if (super.KI > 230 && super.KI < 280 && super.LI > (newheight-30) && super.LI < newheight) {
                ZI = 4D;
                l = -1;
            }
            if (super.KI > 290 && super.KI < 340 && super.LI > (newheight-30) && super.LI < newheight) {
                ZI = 6D;
                l = -1;
            }
            if (super.KI > 350 && super.KI < 400 && super.LI > (newheight-30) && super.LI < newheight) {
                ZI = 8D;
                l = -1;
            }
            if (super.KI > V && super.LI > W + Y && super.KI < V + X && super.LI < newheight) {
                drawkey = !drawkey;
                l = -1;
            }
            if (super.KI > e && super.LI > g + s && super.KI < e + a && super.LI < newheight) {
                drawoverview = !drawoverview;
                l = -1;
            }
            if (drawkey) {
                if (super.KI > V && super.LI > W && super.KI < V + X && super.LI < W + Y)
                    l = -1;
                if (super.KI > V && super.LI > W && super.KI < V + X && super.LI < W + 18 && z > 0)
                    z -= 25;
                if (super.KI > V && super.LI > (W + Y) - 18 && super.KI < V + X && super.LI < W + Y && z < 50)
                    z += 25;
            }
            length = true;
        }
        if (drawkey) {
            b = -1;
            if (super.EI > V && super.EI < V + X) {
                i1 = W + 21 + 5;
                for (int j1 = 0; j1 < 25; j1++) {
                    if (j1 + i < DI.length && DI[j1 + i].equals(IResourceLoader.I(669)))
                        continue;
                    if (super.GI >= i1 && super.GI < i1 + 17) {
                        b = j1 + i;
                        if (super.HI == 1) {
                            f = j1 + i;
                            j = 50;
                        }
                    }
                    i1 += 17;
                }

            }
            if (b != d) {
                d = b;
                length = true;
            }
        }
        if ((super.AI == 1 || super.HI == 1) && drawoverview) {
            i1 = super.KI;
            int k1 = super.LI;
            if (super.AI == 1) {
                i1 = super.EI;
                k1 = super.GI;
            }
            if (i1 > e && k1 > g && i1 < e + a && k1 < g + s) {
                CI = ((i1 - e) * printStackTrace) / a;
                BI = ((k1 - g) * println) / s;
                l = -1;
                length = true;
            }
        }
        if (super.AI == 1 && l != -1) {
            CI = n + (int) (((double) (l - super.EI) * 2D) / ZI);
            BI = o + (int) (((double) (m - super.GI) * 2D) / ZI);
            length = true;
        }
        if (II < ZI) {
            length = true;
            II += II / 30D;
            if (II > ZI)
                II = ZI;
        }
        if (II > ZI) {
            length = true;
            II -= II / 30D;
            if (II < ZI)
                II = ZI;
        }
        if (i < z) {
            length = true;
            i++;
        }
        if (i > z) {
            length = true;
            i--;
        }
        if (j > 0) {
            length = true;
            j--;
        }
        i1 = CI - (int) (635D / II);
        int l1 = BI - (int) (503D / II);
        int j2 = CI + (int) (635D / II);
        int k2 = BI + (int) (503D / II);

        if (i1 < 48)
            CI = 48 + (int) (635D / II);
        if (l1 < 48)
            BI = 48 + (int) (503D / II);
        if (j2 > printStackTrace - 48)
            CI = printStackTrace - 48 - (int) (635D / II);
        if (k2 > println - 48)
            BI = println - 48 - (int) (503D / II);
    }

    public final void B() {
        if (length) {
            length = false;
            mkdir = 0;
            H.resetPixels();
            int i1 = CI - (int) (635D / II);
            int j1 = BI - (int) (503D / II);
            int k1 = CI + (int) (635D / II);
            int l1 = BI + (int) (503D / II);
            indexOf(i1, j1, k1, l1, 0, 0, newwidth, newheight);
            //System.out.println(i1 + "," + j1 + "," + k1 + "," + l1);
            //draws the overview and the red pan window
            if (drawoverview) {  //if Overview/minimap is on
            	e = newwidth - 217;
            	g = newheight - 200;
                k.Z(e, g);
                H.drawFilledRectangleAlhpa(e + (a * i1) / printStackTrace, g + (s * j1) / println, ((k1 - i1) * a) / printStackTrace, ((l1 - j1) * s) / println, 0xff0000, 128);
                H.I(e + (a * i1) / printStackTrace, g + (s * j1) / println, ((k1 - i1) * a) / printStackTrace, ((l1 - j1) * s) / println, 0xff0000);
                if (j > 0 && j % 10 < 5) {
                    for (int i2 = 0; i2 < Q; i2++)
                        if (U[i2] == f) {
                            int k2 = e + (a * R[i2]) / printStackTrace;
                            int i3 = g + (s * T[i2]) / println;
                            H.C(k2, i3, 2, 0xffff00, 256);
                        }

                }
            }
            if (drawkey) {  //if Key is on
            	W = newheight - 490;
                gc(V, W, X, 18, 0x999999, 0x777777, 0x555555, IResourceLoader.I(814));
                gc(V, W + 18, X, Y - 36, 0x999999, 0x777777, 0x555555, "");
                gc(V, (W + Y) - 18, X, 18, 0x999999, 0x777777, 0x555555, IResourceLoader.I(824));
                //System.out.println(V + "," + W + "," + X + "," + Y);
                int j2 = W + 3 + 18;
                for (int l2 = 0; l2 < 25; l2++) {
                    if (l2 + i < F.length && l2 + i < DI.length) {
                        if (DI[l2 + i].equals(IResourceLoader.I(669)))
                            continue;
                        F[l2 + i].I(V + 3, j2);
                        J.I(DI[l2 + i], V + 21, j2 + 14, 0);
                        int j3 = 0xffffff;
                        if (b == l2 + i)
                            j3 = 0xbbaaaa;
                        if (j > 0 && j % 10 < 5 && f == l2 + i)
                            j3 = 0xffff00;
                        J.I(DI[l2 + i], V + 20, j2 + 13, j3);
                    }
                    j2 += 17;
                }

            }
            gc(e, g + s, a, 18, charAt, close, equals, IResourceLoader.I(834)); //draws the "Overview" button
            gc(V, W + Y, X, 18, charAt, close, equals, IResourceLoader.I(843)); //draws the "Key" button
            
            //Lights up a zoom button depending on the zoom level, ZI seems to be the zoom level var
            if (ZI == 3D)
            {
                gc(170, newheight - 30, 50, 30, exists, gc, indexOf, IResourceLoader.I(847));
            }
            else
                gc(170, newheight - 30, 50, 30, charAt, close, equals, IResourceLoader.I(847));
            if (ZI == 4D)
            {
                gc(230, newheight - 30, 50, 30, exists, gc, indexOf, IResourceLoader.I(851));

        	}
            else
            {
                gc(230, newheight - 30, 50, 30, charAt, close, equals, IResourceLoader.I(851));
            }
            if (ZI == 6D)
            {
                gc(290, newheight - 30, 50, 30, exists, gc, indexOf, IResourceLoader.I(855));
            }
            else
            {
                gc(290, newheight - 30, 50, 30, charAt, close, equals, IResourceLoader.I(855));
            }
            if (ZI == 8D)
            {
                gc(350, newheight - 30, 50, 30, exists, gc, indexOf, IResourceLoader.I(859));
            }
            else
            {
                gc(350, newheight - 30, 50, 30, charAt, close, equals, IResourceLoader.I(859));
            }
        }
        mkdir--;
        if (mkdir <= 0) {
            super.JI.drawGraphics(super.FI, 0, 0);
            mkdir = 50;
        }
    }

    public final void D() {
        mkdir = 0;
    }

    
    //draws entire UI elements like the "Key" button and the text.
    //also seems to set up clickable space for these buttons, maybe not!!!
    
    //i1 is x pos
    //j1 is y pos
    //k1 is width
    //l1 is height
    public final void gc(int i1, int j1, int k1, int l1, int i2, int j2, int k2,
                         String s1) {
    	if(s1 == "Key")
    	{
    		j1 = newheight - 20;
    	}
    	/*else if(s1 == "37%" || s1 == "50%" || s1 == "75%" || s1 == "100%" )
    	{
    		j1 = newheight - 30;
    	}*/
    	/*else if(s1 == "Next Page")
    	{
    		j1 = newheight - 40;
    	}*/
    	//System.out.println(s1 + "," + i1 + "," + j1 + "," + k1 + "," + l1 + "," + i2 + "," + j2 + "," + k2);
    	//int heightdiff = j1 - 503;
    	//j1 += heightdiff;
        H.I(i1, j1, k1, l1, 0);
        i1++;
        j1++;
        k1 -= 2;
        l1 -= 2;
        H.drawFilledRectangle(i1, j1, k1, l1, j2);
        H.Z(i1, j1, k1, i2);
        H.C(i1, j1, l1, i2);
        H.Z(i1, (j1 + l1) - 1, k1, k2);
        H.C((i1 + k1) - 1, j1, l1, k2);
        J.C(s1, i1 + k1 / 2 + 1, j1 + l1 / 2 + 1 + 4, 0);
        J.C(s1, i1 + k1 / 2, j1 + l1 / 2 + 4, 0xffffff);
    }

    public final void indexOf(int i1, int j1, int k1, int l1, int i2, int j2, int k2,
                              int l2) {
        int i3 = k1 - i1;
        int j3 = l1 - j1;
        int k3 = (k2 - i2 << 16) / i3;
        int l3 = (l2 - j2 << 16) / j3;
        for (int i4 = 0; i4 < i3; i4++) {
            int k4 = k3 * i4 >> 16;
            int i6 = k3 * (i4 + 1) >> 16;
            int i7 = i6 - k4;
            if (i7 <= 0)
                continue;
            k4 += i2;
            i6 += i2;
            int ai[] = toString[i4 + i1];
            int ai1[] = write[i4 + i1];
            byte abyte1[] = I[i4 + i1];
            for (int l9 = 0; l9 < j3; l9++) {
                int k10 = l3 * l9 >> 16;
                int j11 = l3 * (l9 + 1) >> 16;
                int l11 = j11 - k10;
                if (l11 <= 0)
                    continue;
                k10 += j2;
                j11 += j2;
                int k12 = ai1[l9 + j1];
                if (k12 == 0) {
                    H.drawFilledRectangle(k4, k10, i6 - k4, j11 - k10, ai[l9 + j1]);
                    continue;
                }
                byte byte0 = abyte1[l9 + j1];
                int l13 = byte0 & 0xfc;
                if (l13 == 0 || i7 <= 1 || l11 <= 1)
                    H.drawFilledRectangle(k4, k10, i7, l11, k12);
                else
                    length(H.pixels, k10 * H.width + k4, ai[l9 + j1], k12, i7, l11, l13 >> 2, byte0 & 3);
            }

        }

        if (k1 - i1 > k2 - i2)
            return;
        int j4 = 0;
        for (int l4 = 0; l4 < i3; l4++) {
            int j6 = k3 * l4 >> 16;
            int j7 = k3 * (l4 + 1) >> 16;
            int i8 = j7 - j6;
            if (i8 <= 0)
                continue;
            byte abyte0[] = Z[l4 + i1];
            byte abyte2[] = B[l4 + i1];
            byte abyte3[] = C[l4 + i1];
            for (int l10 = 0; l10 < j3; l10++) {
                int k11 = l3 * l10 >> 16;
                int i12 = l3 * (l10 + 1) >> 16;
                int l12 = i12 - k11;
                if (l12 <= 0)
                    continue;
                int k13 = abyte0[l10 + j1] & 0xff;
                if (k13 != 0) {
                    int i14;
                    if (i8 == 1)
                        i14 = j6;
                    else
                        i14 = j7 - 1;
                    int k14;
                    if (l12 == 1)
                        k14 = k11;
                    else
                        k14 = i12 - 1;
                    int i15 = 0xcccccc;
                    if (k13 >= 5 && k13 <= 8 || k13 >= 13 && k13 <= 16 || k13 >= 21 && k13 <= 24 || k13 == 27 || k13 == 28) {
                        i15 = 0xcc0000;
                        k13 -= 4;
                    }
                    if (k13 == 1)
                        H.C(j6, k11, l12, i15);
                    else if (k13 == 2)
                        H.Z(j6, k11, i8, i15);
                    else if (k13 == 3)
                        H.C(i14, k11, l12, i15);
                    else if (k13 == 4)
                        H.Z(j6, k14, i8, i15);
                    else if (k13 == 9) {
                        H.C(j6, k11, l12, 0xffffff);
                        H.Z(j6, k11, i8, i15);
                    } else if (k13 == 10) {
                        H.C(i14, k11, l12, 0xffffff);
                        H.Z(j6, k11, i8, i15);
                    } else if (k13 == 11) {
                        H.C(i14, k11, l12, 0xffffff);
                        H.Z(j6, k14, i8, i15);
                    } else if (k13 == 12) {
                        H.C(j6, k11, l12, 0xffffff);
                        H.Z(j6, k14, i8, i15);
                    } else if (k13 == 17)
                        H.Z(j6, k11, 1, i15);
                    else if (k13 == 18)
                        H.Z(i14, k11, 1, i15);
                    else if (k13 == 19)
                        H.Z(i14, k14, 1, i15);
                    else if (k13 == 20)
                        H.Z(j6, k14, 1, i15);
                    else if (k13 == 25) {
                        for (int j15 = 0; j15 < l12; j15++)
                            H.Z(j6 + j15, k14 - j15, 1, i15);

                    } else if (k13 == 26) {
                        for (int k15 = 0; k15 < l12; k15++)
                            H.Z(j6 + k15, k11 + k15, 1, i15);

                    }
                }
                int j14 = abyte2[l10 + j1] & 0xff;
                if (j14 != 0)
                    D[j14 - 1].I(j6 - i8 / 2, k11 - l12 / 2, i8 * 2, l12 * 2);
                int l14 = abyte3[l10 + j1] & 0xff;
                if (l14 != 0) {
                    P[j4] = l14 - 1;
                    N[j4] = j6 + i8 / 2;
                    O[j4] = k11 + l12 / 2;
                    j4++;
                }
            }

        }

        for (int i5 = 0; i5 < j4; i5++)
            if (F[P[i5]] != null)
                F[P[i5]].I(N[i5] - 7, O[i5] - 7);

        if (j > 0) {
            for (int j5 = 0; j5 < j4; j5++) {
                if (P[j5] != f)
                    continue;
                F[P[j5]].I(N[j5] - 7, O[j5] - 7);
                if (j % 10 < 5) {
                    H.C(N[j5], O[j5], 15, 0xffff00, 128);
                    H.C(N[j5], O[j5], 7, 0xffffff, 256);
                }
            }

        }
        if (II == ZI && p) {
            label0:
            for (int k5 = 0; k5 < q; k5++) {
                int k6 = u[k5];
                int k7 = v[k5];
                k6 -= openStream;
                k7 = (out + println) - k7;
                int j8 = i2 + ((k2 - i2) * (k6 - i1)) / (k1 - i1);
                int l8 = j2 + ((l2 - j2) * (k7 - j1)) / (l1 - j1);
                int j9 = w[k5];
                int i10 = 0xffffff;
                O o1 = null;
                if (j9 == 0) {
                    if (II == 3D)
                        o1 = S;
                    if (II == 4D)
                        o1 = A;
                    if (II == 6D)
                        o1 = E;
                    if (II == 8D)
                        o1 = o2;
                }
                if (j9 == 1) {
                    if (II == 3D)
                        o1 = E;
                    if (II == 4D)
                        o1 = o2;
                    if (II == 6D)
                        o1 = H;
                    if (II == 8D)
                        o1 = this.o1;
                }
                if (j9 == 2) {
                    i10 = 0xffaa00;
                    if (II == 3D)
                        o1 = H;
                    if (II == 4D)
                        o1 = this.o1;
                    if (II == 6D)
                        o1 = L;
                    if (II == 8D)
                        o1 = M;
                }
                if (o1 == null)
                    continue;
                String s1 = t[k5];
                int j12 = 1;
                for (int i13 = 0; i13 < s1.length(); i13++)
                    if (s1.charAt(i13) == '/')
                        j12++;

                l8 -= (o1.I() * (j12 - 1)) / 2;
                l8 += o1.C() / 2;
                do {
                    int j13 = s1.indexOf(IResourceLoader.I(864));
                    if (j13 == -1) {
                        o1.I(s1, j8, l8, i10, true);
                        continue label0;
                    }
                    String s2 = s1.substring(0, j13);
                    o1.I(s2, j8, l8, i10, true);
                    l8 += o1.I();
                    s1 = s1.substring(j13 + 1);
                } while (true);
            }

        }
        if (append) {
            for (int l5 = openStream / 64; l5 < (openStream + printStackTrace) / 64; l5++) {
                for (int l6 = out / 64; l6 < (out + println) / 64; l6++) {
                    int l7 = l5 * 64;
                    int k8 = l6 * 64;
                    l7 -= openStream;
                    k8 = (out + println) - k8;
                    int i9 = i2 + ((k2 - i2) * (l7 - i1)) / (k1 - i1);
                    int k9 = j2 + ((l2 - j2) * (k8 - 64 - j1)) / (l1 - j1);
                    int j10 = i2 + ((k2 - i2) * ((l7 + 64) - i1)) / (k1 - i1);
                    int i11 = j2 + ((l2 - j2) * (k8 - j1)) / (l1 - j1);
                    H.I(i9, k9, j10 - i9, i11 - k9, 0xffffff);
                    J.Z((new StringBuilder()).append(l5).append(IResourceLoader.I(866)).append(l6).toString(), j10 - 5, i11 - 5, 0xffffff);
                    if (l5 == 33 && l6 >= 71 && l6 <= 73)
                        J.C(IResourceLoader.I(868), (j10 + i9) / 2, (i11 + k9) / 2, 0xff0000);
                }

            }

        }
    }

    public final void length(int ai[], int i1, int j1, int k1, int l1, int i2, int j2, int k2) {
        int l2 = H.width - l1;
        if (j2 == 9) {
            j2 = 1;
            k2 = k2 + 1 & 3;
        }
        if (j2 == 10) {
            j2 = 1;
            k2 = k2 + 3 & 3;
        }
        if (j2 == 11) {
            j2 = 8;
            k2 = k2 + 3 & 3;
        }
        if (j2 == 1) {
            if (k2 == 0) {
                for (int i3 = 0; i3 < i2; i3++) {
                    for (int i11 = 0; i11 < l1; i11++)
                        if (i11 <= i3)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            }
            if (k2 == 1) {
                for (int j3 = i2 - 1; j3 >= 0; j3--) {
                    for (int j11 = 0; j11 < l1; j11++)
                        if (j11 <= j3)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            }
            if (k2 == 2) {
                for (int k3 = 0; k3 < i2; k3++) {
                    for (int k11 = 0; k11 < l1; k11++)
                        if (k11 >= k3)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            }
            if (k2 == 3) {
                for (int l3 = i2 - 1; l3 >= 0; l3--) {
                    for (int l11 = 0; l11 < l1; l11++)
                        if (l11 >= l3)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            } else {
                return;
            }
        }
        if (j2 == 2) {
            if (k2 == 0) {
                for (int i4 = i2 - 1; i4 >= 0; i4--) {
                    for (int i12 = 0; i12 < l1; i12++)
                        if (i12 <= i4 >> 1)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            }
            if (k2 == 1) {
                for (int j4 = 0; j4 < i2; j4++) {
                    for (int j12 = 0; j12 < l1; j12++)
                        if (j12 >= j4 << 1)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            }
            if (k2 == 2) {
                for (int k4 = 0; k4 < i2; k4++) {
                    for (int k12 = l1 - 1; k12 >= 0; k12--)
                        if (k12 <= k4 >> 1)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            }
            if (k2 == 3) {
                for (int l4 = i2 - 1; l4 >= 0; l4--) {
                    for (int l12 = l1 - 1; l12 >= 0; l12--)
                        if (l12 >= l4 << 1)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            } else {
                return;
            }
        }
        if (j2 == 3) {
            if (k2 == 0) {
                for (int i5 = i2 - 1; i5 >= 0; i5--) {
                    for (int i13 = l1 - 1; i13 >= 0; i13--)
                        if (i13 <= i5 >> 1)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            }
            if (k2 == 1) {
                for (int j5 = i2 - 1; j5 >= 0; j5--) {
                    for (int j13 = 0; j13 < l1; j13++)
                        if (j13 >= j5 << 1)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            }
            if (k2 == 2) {
                for (int k5 = 0; k5 < i2; k5++) {
                    for (int k13 = 0; k13 < l1; k13++)
                        if (k13 <= k5 >> 1)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            }
            if (k2 == 3) {
                for (int l5 = 0; l5 < i2; l5++) {
                    for (int l13 = l1 - 1; l13 >= 0; l13--)
                        if (l13 >= l5 << 1)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            } else {
                return;
            }
        }
        if (j2 == 4) {
            if (k2 == 0) {
                for (int i6 = i2 - 1; i6 >= 0; i6--) {
                    for (int i14 = 0; i14 < l1; i14++)
                        if (i14 >= i6 >> 1)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            }
            if (k2 == 1) {
                for (int j6 = 0; j6 < i2; j6++) {
                    for (int j14 = 0; j14 < l1; j14++)
                        if (j14 <= j6 << 1)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            }
            if (k2 == 2) {
                for (int k6 = 0; k6 < i2; k6++) {
                    for (int k14 = l1 - 1; k14 >= 0; k14--)
                        if (k14 >= k6 >> 1)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            }
            if (k2 == 3) {
                for (int l6 = i2 - 1; l6 >= 0; l6--) {
                    for (int l14 = l1 - 1; l14 >= 0; l14--)
                        if (l14 <= l6 << 1)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            } else {
                return;
            }
        }
        if (j2 == 5) {
            if (k2 == 0) {
                for (int i7 = i2 - 1; i7 >= 0; i7--) {
                    for (int i15 = l1 - 1; i15 >= 0; i15--)
                        if (i15 >= i7 >> 1)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            }
            if (k2 == 1) {
                for (int j7 = i2 - 1; j7 >= 0; j7--) {
                    for (int j15 = 0; j15 < l1; j15++)
                        if (j15 <= j7 << 1)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            }
            if (k2 == 2) {
                for (int k7 = 0; k7 < i2; k7++) {
                    for (int k15 = 0; k15 < l1; k15++)
                        if (k15 >= k7 >> 1)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            }
            if (k2 == 3) {
                for (int l7 = 0; l7 < i2; l7++) {
                    for (int l15 = l1 - 1; l15 >= 0; l15--)
                        if (l15 <= l7 << 1)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            } else {
                return;
            }
        }
        if (j2 == 6) {
            if (k2 == 0) {
                for (int i8 = 0; i8 < i2; i8++) {
                    for (int i16 = 0; i16 < l1; i16++)
                        if (i16 <= l1 / 2)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            }
            if (k2 == 1) {
                for (int j8 = 0; j8 < i2; j8++) {
                    for (int j16 = 0; j16 < l1; j16++)
                        if (j8 <= i2 / 2)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            }
            if (k2 == 2) {
                for (int k8 = 0; k8 < i2; k8++) {
                    for (int k16 = 0; k16 < l1; k16++)
                        if (k16 >= l1 / 2)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            }
            if (k2 == 3) {
                for (int l8 = 0; l8 < i2; l8++) {
                    for (int l16 = 0; l16 < l1; l16++)
                        if (l8 >= i2 / 2)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            }
        }
        if (j2 == 7) {
            if (k2 == 0) {
                for (int i9 = 0; i9 < i2; i9++) {
                    for (int i17 = 0; i17 < l1; i17++)
                        if (i17 <= i9 - i2 / 2)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            }
            if (k2 == 1) {
                for (int j9 = i2 - 1; j9 >= 0; j9--) {
                    for (int j17 = 0; j17 < l1; j17++)
                        if (j17 <= j9 - i2 / 2)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            }
            if (k2 == 2) {
                for (int k9 = i2 - 1; k9 >= 0; k9--) {
                    for (int k17 = l1 - 1; k17 >= 0; k17--)
                        if (k17 <= k9 - i2 / 2)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            }
            if (k2 == 3) {
                for (int l9 = 0; l9 < i2; l9++) {
                    for (int l17 = l1 - 1; l17 >= 0; l17--)
                        if (l17 <= l9 - i2 / 2)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            }
        }
        if (j2 == 8) {
            if (k2 == 0) {
                for (int i10 = 0; i10 < i2; i10++) {
                    for (int i18 = 0; i18 < l1; i18++)
                        if (i18 >= i10 - i2 / 2)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            }
            if (k2 == 1) {
                for (int j10 = i2 - 1; j10 >= 0; j10--) {
                    for (int j18 = 0; j18 < l1; j18++)
                        if (j18 >= j10 - i2 / 2)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            }
            if (k2 == 2) {
                for (int k10 = i2 - 1; k10 >= 0; k10--) {
                    for (int k18 = l1 - 1; k18 >= 0; k18--)
                        if (k18 >= k10 - i2 / 2)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

                return;
            }
            if (k2 == 3) {
                for (int l10 = 0; l10 < i2; l10++) {
                    for (int l18 = l1 - 1; l18 >= 0; l18--)
                        if (l18 >= l10 - i2 / 2)
                            ai[i1++] = k1;
                        else
                            ai[i1++] = j1;

                    i1 += l2;
                }

            }
        }
    }

    public final E mkdir() {
        byte abyte0[] = null;
        String s1 = null;
        try {
            s1 = out();
            abyte0 = FileUtil.readFile(IResourceLoader.I(1291));
            return new E(abyte0);
        } catch (Throwable throwable) {
            abyte0 = openStream();
        }
        if (s1 != null && abyte0 != null)
            try {
                printStackTrace((new StringBuilder()).append(s1).append(IResourceLoader.I(1304)).toString(), abyte0);
            } catch (Throwable throwable1) {
            }
        return new E(abyte0);
    }

    public final byte[] openStream() {
        I(0, IResourceLoader.I(1318));
        try {
            String s1 = "";
            for (int i1 = 0; i1 < 10; i1++)
            {
                s1 = (new StringBuilder()).append(s1).append(G.I[i1]).toString();
            }
            DataInputStream datainputstream;
            if (super.SI != null)
                datainputstream = new DataInputStream(new FileInputStream(IResourceLoader.I(1291)));
            else
                datainputstream = new DataInputStream((new URL(getCodeBase(), (new StringBuilder()).append(IResourceLoader.I(1333)).append(s1).append(IResourceLoader.I(1251)).toString())).openStream());
            int j1 = 0;
            int k1 = 0;
            int l1 = 0x53901;
            byte abyte0[] = new byte[l1];
            while (k1 < l1) {
                int i2 = l1 - k1;
                if (i2 > newwidth)
                    i2 = newwidth;
                int j2 = datainputstream.read(abyte0, k1, i2);
                if (j2 < 0)
                    throw new IOException(IResourceLoader.I(1342));
                k1 += j2;
                int k2 = (k1 * 100) / l1;
                if (k2 != j1)
                    I(k2, (new StringBuilder()).append(IResourceLoader.I(1354)).append(k2).append(IResourceLoader.I(1369)).toString());
                j1 = k2;
            }
            datainputstream.close();
            return abyte0;
        } catch (IOException ioexception) {
            System.out.println(IResourceLoader.I(1371));
            ioexception.printStackTrace();
            return null;
        }
    }

    public final String out() {
        String[] arrayOfString = {IResourceLoader.I(1398), IResourceLoader.I(1410), IResourceLoader.I(1420), IResourceLoader.I(1432), IResourceLoader.I(1442), IResourceLoader.I(1454), IResourceLoader.I(1464), IResourceLoader.I(1476), IResourceLoader.I(1486), IResourceLoader.I(1490), IResourceLoader.I(1493), ""};
        String str1 = IResourceLoader.I(1499); //.file_store_32
        for (int i1 = 0; i1 < arrayOfString.length; i1++) {
            try {
                String str2 = arrayOfString[i1];
                File localFile;
                if (str2.length() > 0) {
                    localFile = new File(str2);
                    if (!localFile.exists()) {
                    }
                } else {
                    localFile = new File(str2 + str1);
                    if ((localFile.exists()) || (localFile.mkdir())) {
                        return str2 + str1 + IResourceLoader.I(864);
                    }
                }
            } catch (Exception localException) {
            }
        }
        return null;
    }

    public final void printStackTrace(String s1, byte abyte0[]) {
        try (FileOutputStream fileoutputstream = new FileOutputStream(s1)) {
            fileoutputstream.write(abyte0, 0, abyte0.length);
            fileoutputstream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Main() {
        charAt = 0x887755;
        close = 0x776644;
        equals = 0x665533;
        exists = 0xaa0000;
        gc = 0x990000;
        indexOf = 0x880000;
        length = true;
        D = new ClutterSprite[100];
        F = new B[100];
        N = new int[2000];
        O = new int[2000];
        P = new int[2000];
        R = new int[2000];
        T = new int[2000];
        U = new int[2000];
        V = 5;
        W = 13;
        X = 140;
        Y = 470;
        drawkey = false;
        b = -1;
        d = -1;
        f = -1;
        drawoverview = false;
		r = newwidth ;
        t = new String[r];
        u = new int[r];
        v = new int[r];
        w = new int[r];
        II = 4D;
        ZI = 4D;
    }

    public static boolean append;
    public int charAt;
    public int close;
    public int equals;
    public int exists;
    public int gc;
    public int indexOf;
    public boolean length;
    public int mkdir;
    public static int openStream;
    public static int out;
    public static int printStackTrace;
    public static int println;
    public int read[];
    public int substring[];
    public int toString[][];
    public int write[][];
    public byte I[][];
    public byte Z[][];
    public byte C[][];
    public byte B[][];
    public ClutterSprite D[];
    public B F[];
    public F J;
    public O S;
    public O A;
    public O E;
    public O o2;
    public O H; // there are like a million of these!
    public O o1;
    public O L;
    public O M;
    public int N[];
    public int O[];
    public int P[];
    public int Q;
    public int R[];
    public int T[];
    public int U[];
    public int V;
    public int W;
    public int X;
    public int Y;
    public int i;
    public int z;
    public boolean drawkey;
    public int b;
    public int d;
    public int f;
    public int j;
    public int s;
    public int a;
    public int e;
    public int g;
    public boolean drawoverview;
    public B k;
    public int l;
    public int m;
    public int n;
    public int o;
    public static boolean p = true;
    public int q;
    public int r;
    public String t[];
    public int u[];
    public int v[];
    public int w[];
    public double II;
    public double ZI;
    public static int CI;
    public static int BI;
    public String DI[] = {
    		IResourceLoader.I(1), IResourceLoader.I(15), IResourceLoader.I(26), IResourceLoader.I(37), IResourceLoader.I(46), IResourceLoader.I(58), IResourceLoader.I(63), IResourceLoader.I(75), IResourceLoader.I(87), IResourceLoader.I(99), IResourceLoader.I(107),
            IResourceLoader.I(113), IResourceLoader.I(129), IResourceLoader.I(137), IResourceLoader.I(148), IResourceLoader.I(163), IResourceLoader.I(178), IResourceLoader.I(192), IResourceLoader.I(205), IResourceLoader.I(217), IResourceLoader.I(223), IResourceLoader.I(233),
            IResourceLoader.I(242), IResourceLoader.I(251), IResourceLoader.I(265), IResourceLoader.I(277), IResourceLoader.I(290), IResourceLoader.I(303), IResourceLoader.I(316), IResourceLoader.I(327), IResourceLoader.I(339), IResourceLoader.I(352), IResourceLoader.I(360),
            IResourceLoader.I(370), IResourceLoader.I(378), IResourceLoader.I(389), IResourceLoader.I(404), IResourceLoader.I(414), IResourceLoader.I(427), IResourceLoader.I(437), IResourceLoader.I(450), IResourceLoader.I(464), IResourceLoader.I(475), IResourceLoader.I(489),
            IResourceLoader.I(498), IResourceLoader.I(510), IResourceLoader.I(525), IResourceLoader.I(537), IResourceLoader.I(548), IResourceLoader.I(559), IResourceLoader.I(576), IResourceLoader.I(592), IResourceLoader.I(606), IResourceLoader.I(620), IResourceLoader.I(634),
            IResourceLoader.I(648), IResourceLoader.I(654), IResourceLoader.I(669), IResourceLoader.I(673), IResourceLoader.I(686), IResourceLoader.I(691)
    };

}
