package viewer;
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   F

import java.util.Random;

public class F extends Graphics2D
{

    public final int append(String s)
    {
        if(s == null)
            return 0;
        int i = 0;
        for(int j = 0; j < s.length(); j++)
            i += Z[s.charAt(j)];

        return i;
    }

    public final void I(String s, int i, int j, int k)
    {
        if(s == null)
            return;
        j -= C;
        for(int l = 0; l < s.length(); l++)
        {
            char c = s.charAt(l);
            if(c != ' ')
                charAt(append[c], i + toString[c], j + anIntArray01[c], charAt[c], length[c], k);
            i += Z[c];
        }

    }

    public F(E e, String s, boolean flag)
    {
        append = new byte[256][];
        charAt = new int[256];
        length = new int[256];
        toString = new int[256];
        anIntArray01 = new int[256];
        Z = new int[256];
        C = 0;
        B = new Random();
        D = false;
        IoBuffer buffer = new IoBuffer(e.I((new StringBuilder()).append(s).append(IResourceLoader.I(1251)).toString(), null));
        IoBuffer j = new IoBuffer(e.I(IResourceLoader.I(1256), null));
        j.length = buffer.readShort() + 4;
        int k = j.readUnsigned();
        if(k > 0)
            j.length += 3 * (k - 1);
        for(int l = 0; l < 256; l++)
        {
            toString[l] = j.readUnsigned();
            anIntArray01[l] = j.readUnsigned();
            int i1 = charAt[l] = j.readShort();
            int j1 = length[l] = j.readShort();
            int k1 = j.readUnsigned();
            int l1 = i1 * j1;
            append[l] = new byte[l1];
            if(k1 == 0)
            {
                for(int i2 = 0; i2 < l1; i2++)
                    append[l][i2] = buffer.read();

            } else
            if(k1 == 1)
            {
                for(int j2 = 0; j2 < i1; j2++)
                {
                    for(int l2 = 0; l2 < j1; l2++)
                        append[l][j2 + l2 * i1] = buffer.read();

                }

            }
            if(j1 > C && l < 128)
                C = j1;
            toString[l] = 1;
            Z[l] = i1 + 2;
            int k2 = 0;
            for(int i3 = j1 / 7; i3 < j1; i3++)
                k2 += append[l][i3 * i1];

            if(k2 <= j1 / 7)
            {
                Z[l]--;
                toString[l] = 0;
            }
            k2 = 0;
            for(int j3 = j1 / 7; j3 < j1; j3++)
                k2 += append[l][(i1 - 1) + j3 * i1];

            if(k2 <= j1 / 7)
                Z[l]--;
        }

        if(flag)
            Z[32] = Z[73];
        else
            Z[32] = Z[105];
    }

    public final void Z(String s, int i, int j, int k)
    {
        I(s, i - append(s), j, k);
    }

    public final void C(String s, int i, int j, int k)
    {
        I(s, i - append(s) / 2, j, k);
    }

    public final void charAt(byte abyte0[], int i, int j, int k, int l, int i1)
    {
        int j1 = i + j * width;
        int k1 = width - k;
        int l1 = 0;
        int i2 = 0;
        if(j < topY)
        {
            int j2 = topY - j;
            l -= j2;
            j = topY;
            i2 += j2 * k;
            j1 += j2 * width;
        }
        if(j + l >= bottomY)
            l -= ((j + l) - bottomY) + 1;
        if(i < topX)
        {
            int k2 = topX - i;
            k -= k2;
            i = topX;
            i2 += k2;
            j1 += k2;
            l1 += k2;
            k1 += k2;
        }
        if(i + k >= bottomX)
        {
            int l2 = ((i + k) - bottomX) + 1;
            k -= l2;
            l1 += l2;
            k1 += l2;
        }
        if(k <= 0 || l <= 0)
        {
            return;
        } else
        {
            length(pixels, abyte0, i1, i2, j1, k, l, k1, l1);
            return;
        }
    }

    public final void length(int ai[], byte abyte0[], int i, int j, int k, int l, int i1, 
            int j1, int k1)
    {
        int l1 = -(l >> 2);
        l = -(l & 3);
        for(int i2 = -i1; i2 < 0; i2++)
        {
            for(int j2 = l1; j2 < 0; j2++)
            {
                if(abyte0[j++] != 0)
                    ai[k++] = i;
                else
                    k++;
                if(abyte0[j++] != 0)
                    ai[k++] = i;
                else
                    k++;
                if(abyte0[j++] != 0)
                    ai[k++] = i;
                else
                    k++;
                if(abyte0[j++] != 0)
                    ai[k++] = i;
                else
                    k++;
            }

            for(int k2 = l; k2 < 0; k2++)
                if(abyte0[j++] != 0)
                    ai[k++] = i;
                else
                    k++;

            k += j1;
            j += k1;
        }

    }

    public byte append[][];
    public int charAt[];
    public int length[];
    public int toString[];
    public int anIntArray01[];
    public int Z[];
    public int C;
    public Random B;
    public boolean D;
}
