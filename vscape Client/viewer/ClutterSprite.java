package viewer;
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   D

public class ClutterSprite extends Graphics2D
{

    public ClutterSprite(E e, String s, int i)  //loads tree and rock sprites?
    {
        IoBuffer j = new IoBuffer(e.I((new StringBuilder()).append(s).append(IResourceLoader.I(1251)).toString(), null));
        IoBuffer k = new IoBuffer(e.I(IResourceLoader.I(1256), null));
        k.length = j.readShort();
        C = k.readShort();
        B = k.readShort();
        int l = k.readUnsigned();
        out = new int[l];
        for(int i1 = 0; i1 < l - 1; i1++)
            out[i1 + 1] = k.F();

        for(int j1 = 0; j1 < i; j1++)
        {
            k.length += 2;
            j.length += k.readShort() * k.readShort();
            k.length++;
        }

        I = k.readUnsigned();
        Z = k.readUnsigned();
        println = k.readShort();
        toString = k.readShort();
        int k1 = k.readUnsigned();
        int l1 = println * toString;
        append = new byte[l1];
        if(k1 == 0)
        {
            for(int i2 = 0; i2 < l1; i2++)
                append[i2] = j.read();

        } else
        if(k1 == 1)
        {
            for(int j2 = 0; j2 < println; j2++)
            {
                for(int k2 = 0; k2 < toString; k2++)
                    append[j2 + k2 * println] = j.read();

            }

        }
    }

    public final void I(int i, int j, int k, int l) //draws trees and rocks and shit
    {
        try
        {
            int i1 = println;
            int j1 = toString;
            int k1 = 0;
            int l1 = 0;
            int i2 = (i1 << 16) / k;
            int j2 = (j1 << 16) / l;
            int k2 = C;
            int l2 = B;
            i2 = (k2 << 16) / k;
            j2 = (l2 << 16) / l;
            i += ((I * k + k2) - 1) / k2;
            j += ((Z * l + l2) - 1) / l2;
            if((I * k) % k2 != 0)
                k1 = (k2 - (I * k) % k2 << 16) / k;
            if((Z * l) % l2 != 0)
                l1 = (l2 - (Z * l) % l2 << 16) / l;
            k = (k * (println - (k1 >> 16))) / k2;
            l = (l * (toString - (l1 >> 16))) / l2;
            int i3 = i + j * width;
            int j3 = width - k;
            if(j < topY)
            {
                int k3 = topY - j;
                l -= k3;
                j = 0;
                i3 += k3 * width;
                l1 += j2 * k3;
            }
            if(j + l > bottomY)
                l -= (j + l) - bottomY;
            if(i < topX)
            {
                int l3 = topX - i;
                k -= l3;
                i = 0;
                i3 += l3;
                k1 += i2 * l3;
                j3 += l3;
            }
            if(i + k > bottomX)
            {
                int i4 = (i + k) - bottomX;
                k -= i4;
                j3 += i4;
            }
            append(pixels, append, out, k1, l1, i3, j3, k, l, i2, j2, i1);  //draws trees and rocks and shit
        }
        catch(Exception exception)
        {
            System.out.println(IResourceLoader.I(997));
        }
    }

    public final void append(int ai[], byte abyte0[], int ai1[], int i, int j, int k, int l, 
            int i1, int j1, int k1, int l1, int i2) //also has some part in drawing trees and rocks and shit
    {
        try
        {
            int j2 = i;
            for(int k2 = -j1; k2 < 0; k2++)
            {
                int l2 = (j >> 16) * i2;
                for(int i3 = -i1; i3 < 0; i3++)
                {
                    byte byte0 = abyte0[(i >> 16) + l2];
                    if(byte0 != 0)
                        ai[k++] = ai1[byte0 & 0xff];
                    else
                        k++;
                    i += k1;
                }

                j += l1;
                i = j2;
                k += l;
            }

        }
        catch(Exception exception)
        {
            System.out.println(IResourceLoader.I(1030));
        }
    }

    public byte append[];
    public int out[];
    public int println;
    public int toString;
    public int I;
    public int Z;
    public int C;
    public int B;
}
