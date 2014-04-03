package vscapeClient;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Sounds.java


final class Sounds
{

    private Sounds()
    {
    }

    public static void unpack(Stream stream)
    {
        aByteArray327 = new byte[0x6baa8];
        aStream_328 = new Stream(aByteArray327);
        Class6.method166();
        do
        {
            int i = stream.readUnsignedWord();
            if(i == 65535)
                return;
            aSoundsArray325s[i] = new Sounds();
            aSoundsArray325s[i].method242(stream);
            anIntArray326[i] = aSoundsArray325s[i].method243();
        } while(true);
    }

    public static Stream method241(int i, int j)
    {
        if(aSoundsArray325s[j] != null)
        {
            Sounds sounds = aSoundsArray325s[j];
            return sounds.method244(i);
        } else
        {
            return null;
        }
    }

    private void method242(Stream stream)
    {
        for(int i = 0; i < 10; i++)
        {
            int j = stream.readUnsignedByte();
            if(j != 0)
            {
                stream.currentOffset--;
                aClass6Array329[i] = new Class6();
                aClass6Array329[i].method169(stream);
            }
        }

        anInt330 = stream.readUnsignedWord();
        anInt331 = stream.readUnsignedWord();
    }

    private int method243()
    {
        int i = 0x98967f;
        for(int j = 0; j < 10; j++)
            if(aClass6Array329[j] != null && aClass6Array329[j].anInt114 / 20 < i)
                i = aClass6Array329[j].anInt114 / 20;

        if(anInt330 < anInt331 && anInt330 / 20 < i)
            i = anInt330 / 20;
        if(i == 0x98967f || i == 0)
            return 0;
        for(int k = 0; k < 10; k++)
            if(aClass6Array329[k] != null)
                aClass6Array329[k].anInt114 -= i * 20;

        if(anInt330 < anInt331)
        {
            anInt330 -= i * 20;
            anInt331 -= i * 20;
        }
        return i;
    }

    private Stream method244(int i)
    {
        int j = method245(i);
        aStream_328.currentOffset = 0;
        aStream_328.writeDWord(0x52494646);
        aStream_328.method403(36 + j);
        aStream_328.writeDWord(0x57415645);
        aStream_328.writeDWord(0x666d7420);
        aStream_328.method403(16);
        aStream_328.method400(1);
        aStream_328.method400(1);
        aStream_328.method403(22050);
        aStream_328.method403(22050);
        aStream_328.method400(1);
        aStream_328.method400(8);
        aStream_328.writeDWord(0x64617461);
        aStream_328.method403(j);
        aStream_328.currentOffset += j;
        return aStream_328;
    }

    private int method245(int i)
    {
        int j = 0;
        for(int k = 0; k < 10; k++)
            if(aClass6Array329[k] != null && aClass6Array329[k].anInt113 + aClass6Array329[k].anInt114 > j)
                j = aClass6Array329[k].anInt113 + aClass6Array329[k].anInt114;

        if(j == 0)
            return 0;
        int l = (22050 * j) / 1000;
        int i1 = (22050 * anInt330) / 1000;
        int j1 = (22050 * anInt331) / 1000;
        if(i1 < 0 || i1 > l || j1 < 0 || j1 > l || i1 >= j1)
            i = 0;
        int k1 = l + (j1 - i1) * (i - 1);
        for(int l1 = 44; l1 < k1 + 44; l1++)
            aByteArray327[l1] = -128;

        for(int i2 = 0; i2 < 10; i2++)
        {
            if(aClass6Array329[i2] == null)
                continue;
            int k2 = (aClass6Array329[i2].anInt113 * 22050) / 1000;
            int j3 = (aClass6Array329[i2].anInt114 * 22050) / 1000;
            int ai[] = aClass6Array329[i2].method167(k2, aClass6Array329[i2].anInt113);
            for(int l3 = 0; l3 < k2; l3++)
                aByteArray327[l3 + j3 + 44] += (byte)(ai[l3] >> 8);

        }

        if(i > 1)
        {
            i1 += 44;
            j1 += 44;
            l += 44;
            int j2 = (k1 += 44) - l;
            for(int l2 = l - 1; l2 >= j1; l2--)
                aByteArray327[l2 + j2] = aByteArray327[l2];

            for(int i3 = 1; i3 < i; i3++)
            {
                int k3 = (j1 - i1) * i3;
                System.arraycopy(aByteArray327, i1, aByteArray327, i1 + k3, j1 - i1);
            }

            k1 -= 44;
        }
        return k1;
    }

    private static final Sounds aSoundsArray325s[] = new Sounds[5000];
    public static final int anIntArray326[] = new int[5000];
    private static byte aByteArray327[];
    private static Stream aStream_328;
    private final Class6 aClass6Array329[] = new Class6[10];
    private int anInt330;
    private int anInt331;

}
