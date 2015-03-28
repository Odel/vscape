package viewer;
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   topY


public class S
{

    public S()
    {
    }

    public static final byte B(A a)
    {
        return (byte)I(8, a);
    }

    public static final void I(A a)
    {
        int i = 0;
        int ai[] = null;
        int ai1[] = null;
        int ai2[] = null;
        a.Y = 1;
        if(A.a == null)
            A.a = new int[a.Y * 0x186a0];
        for(boolean flag = true; flag;)
        {
            byte byte0 = B(a);
            if(byte0 == 23)
                return;
            byte0 = B(a);
            byte0 = B(a);
            byte0 = B(a);
            byte0 = B(a);
            byte0 = B(a);
            a.i++;
            byte0 = B(a);
            byte0 = B(a);
            byte0 = B(a);
            byte0 = B(a);
            byte0 = println(a);
            if(byte0 != 0)
                a.V = true;
            else
                a.V = false;
            if(a.V)
                System.out.println(IResourceLoader.I(1266));  //PANIC! RANDOMISED BLOCK!  lol i dunno what this means
            a.z = 0;
            byte0 = B(a);
            a.z = a.z << 8 | byte0 & 0xff;
            byte0 = B(a);
            a.z = a.z << 8 | byte0 & 0xff;
            byte0 = B(a);
            a.z = a.z << 8 | byte0 & 0xff;
            for(int j = 0; j < 16; j++)
            {
                byte byte1 = println(a);
                if(byte1 == 1)
                    a.h[j] = true;
                else
                    a.h[j] = false;
            }

            for(int k = 0; k < 256; k++)
                a.g[k] = false;

            for(int l = 0; l < 16; l++)
            {
                if(!a.h[l])
                    continue;
                for(int j1 = 0; j1 < 16; j1++)
                {
                    byte byte2 = println(a);
                    if(byte2 == 1)
                        a.g[l * 16 + j1] = true;
                }

            }

            out(a);
            int i1 = a.e + 2;
            int k1 = I(3, a);
            int l1 = I(15, a);
            for(int i2 = 0; i2 < l1; i2++)
            {
                int j2 = 0;
                do
                {
                    byte byte4 = println(a);
                    if(byte4 == 0)
                        break;
                    j2++;
                } while(true);
                a.o[i2] = (byte)j2;
            }

            byte abyte0[] = new byte[6];
            for(byte byte3 = 0; byte3 < k1; byte3++)
                abyte0[byte3] = byte3;

            for(int k2 = 0; k2 < l1; k2++)
            {
                byte byte5 = a.o[k2];
                byte byte7 = abyte0[byte5];
                for(; byte5 > 0; byte5--)
                    abyte0[byte5] = abyte0[byte5 - 1];

                abyte0[0] = byte7;
                a.n[k2] = byte7;
            }

            for(int l2 = 0; l2 < k1; l2++)
            {
                int k3 = I(5, a);
                for(int i4 = 0; i4 < i1; i4++)
                {
                    do
                    {
                        byte byte8 = println(a);
                        if(byte8 == 0)
                            break;
                        byte8 = println(a);
                        if(byte8 == 0)
                            k3++;
                        else
                            k3--;
                    } while(true);
                    a.p[l2][i4] = (byte)k3;
                }

            }

            for(int i3 = 0; i3 < k1; i3++)
            {
                byte byte6 = 32;
                int j4 = 0;
                for(int l4 = 0; l4 < i1; l4++)
                {
                    if(a.p[i3][l4] > j4)
                        j4 = a.p[i3][l4];
                    if(a.p[i3][l4] < byte6)
                        byte6 = a.p[i3][l4];
                }

                B(a.q[i3], a.r[i3], a.t[i3], a.p[i3], byte6, j4, i1);
                a.u[i3] = byte6;
            }

            int j3 = a.e + 1;
            int l3 = -1;
            int k4 = 0;
            for(int i5 = 0; i5 <= 255; i5++)
                a.d[i5] = 0;

            int j5 = 4095;
            for(int k5 = 15; k5 >= 0; k5--)
            {
                for(int i6 = 15; i6 >= 0; i6--)
                {
                    a.l[j5] = (byte)(k5 * 16 + i6);
                    j5--;
                }

                a.m[k5] = j5 + 1;
            }

            int l5 = 0;
            if(k4 == 0)
            {
                l3++;
                k4 = 50;
                byte byte9 = a.n[l3];
                i = a.u[byte9];
                ai = a.q[byte9];
                ai2 = a.t[byte9];
                ai1 = a.r[byte9];
            }
            k4--;
            int j6 = i;
            int k6;
            byte byte10;
            for(k6 = I(j6, a); k6 > ai[j6]; k6 = k6 << 1 | byte10)
            {
                j6++;
                byte10 = println(a);
            }

            for(int l6 = ai2[k6 - ai1[j6]]; l6 != j3;)
                if(l6 == 0 || l6 == 1)
                {
                    int l7 = -1;
                    int j8 = 1;
                    do
                    {
                        if(l6 == 0)
                            l7 += 1 * j8;
                        else
                        if(l6 == 1)
                            l7 += 2 * j8;
                        j8 *= 2;
                        if(k4 == 0)
                        {
                            l3++;
                            k4 = 50;
                            byte byte13 = a.n[l3];
                            i = a.u[byte13];
                            ai = a.q[byte13];
                            ai2 = a.t[byte13];
                            ai1 = a.r[byte13];
                        }
                        k4--;
                        int k8 = i;
                        int k9;
                        byte byte16;
                        for(k9 = I(k8, a); k9 > ai[k8]; k9 = k9 << 1 | byte16)
                        {
                            k8++;
                            byte16 = println(a);
                        }

                        l6 = ai2[k9 - ai1[k8]];
                    } while(l6 == 0 || l6 == 1);
                    l7++;
                    byte byte14 = a.k[a.l[a.m[0]] & 0xff];
                    a.d[byte14 & 0xff] += l7;
                    while(l7 > 0) 
                    {
                        A.a[l5] = byte14 & 0xff;
                        l5++;
                        l7--;
                    }
                } else
                {
                    int i8 = l6 - 1;
                    byte byte12;
                    if(i8 < 16)
                    {
                        int l8 = a.m[0];
                        byte12 = a.l[l8 + i8];
                        for(; i8 > 3; i8 -= 4)
                        {
                            int l9 = l8 + i8;
                            a.l[l9] = a.l[l9 - 1];
                            a.l[l9 - 1] = a.l[l9 - 2];
                            a.l[l9 - 2] = a.l[l9 - 3];
                            a.l[l9 - 3] = a.l[l9 - 4];
                        }

                        for(; i8 > 0; i8--)
                            a.l[l8 + i8] = a.l[(l8 + i8) - 1];

                        a.l[l8] = byte12;
                    } else
                    {
                        int i9 = i8 / 16;
                        int i10 = i8 % 16;
                        int k10 = a.m[i9] + i10;
                        byte12 = a.l[k10];
                        for(; k10 > a.m[i9]; k10--)
                            a.l[k10] = a.l[k10 - 1];

                        a.m[i9]++;
                        for(; i9 > 0; i9--)
                        {
                            a.m[i9]--;
                            a.l[a.m[i9]] = a.l[(a.m[i9 - 1] + 16) - 1];
                        }

                        a.m[0]--;
                        a.l[a.m[0]] = byte12;
                        if(a.m[0] == 0)
                        {
                            int l10 = 4095;
                            for(int i11 = 15; i11 >= 0; i11--)
                            {
                                for(int j11 = 15; j11 >= 0; j11--)
                                {
                                    a.l[l10] = a.l[a.m[i11] + j11];
                                    l10--;
                                }

                                a.m[i11] = l10 + 1;
                            }

                        }
                    }
                    a.d[a.k[byte12 & 0xff] & 0xff]++;
                    A.a[l5] = a.k[byte12 & 0xff] & 0xff;
                    l5++;
                    if(k4 == 0)
                    {
                        l3++;
                        k4 = 50;
                        byte byte15 = a.n[l3];
                        i = a.u[byte15];
                        ai = a.q[byte15];
                        ai2 = a.t[byte15];
                        ai1 = a.r[byte15];
                    }
                    k4--;
                    int j9 = i;
                    int j10;
                    byte byte17;
                    for(j10 = I(j9, a); j10 > ai[j9]; j10 = j10 << 1 | byte17)
                    {
                        j9++;
                        byte17 = println(a);
                    }

                    l6 = ai2[j10 - ai1[j9]];
                }

            a.U = 0;
            a.T = 0;
            a.j[0] = 0;
            for(int i7 = 1; i7 <= 256; i7++)
                a.j[i7] = a.d[i7 - 1];

            for(int j7 = 1; j7 <= 256; j7++)
                a.j[j7] += a.j[j7 - 1];

            for(int k7 = 0; k7 < l5; k7++)
            {
                byte byte11 = (byte)(A.a[k7] & 0xff);
                A.a[a.j[byte11 & 0xff]] |= k7 << 8;
                a.j[byte11 & 0xff]++;
            }

            a.c = A.a[a.z] >> 8;
            a.f = 0;
            a.c = A.a[a.c];
            a.b = (byte)(a.c & 0xff);
            a.c >>= 8;
            a.f++;
            a.v = l5;
            Z(a);
            if(a.f == a.v + 1 && a.U == 0)
                flag = true;
            else
                flag = false;
        }

    }

    public static final void Z(A a)
    {
        byte byte0 = a.T;
        int i = a.U;
        int j = a.f;
        int k = a.b;
        int ai[] = A.a;
        int l = a.c;
        byte abyte0[] = a.N;
        int i1 = a.O;
        int j1 = a.P;
        int k1 = j1;
        int l1 = a.v + 1;
label0:
        do
        {
            if(i > 0)
            {
                do
                {
                    if(j1 == 0)
                        break label0;
                    if(i == 1)
                        break;
                    abyte0[i1] = byte0;
                    i--;
                    i1++;
                    j1--;
                } while(true);
                if(j1 == 0)
                {
                    i = 1;
                    break;
                }
                abyte0[i1] = byte0;
                i1++;
                j1--;
            }
            boolean flag = true;
            do
            {
                if(!flag)
                    break;
                flag = false;
                if(j == l1)
                {
                    i = 0;
                    break label0;
                }
                byte0 = (byte)k;
                l = ai[l];
                byte byte1 = (byte)(l & 0xff);
                l >>= 8;
                j++;
                if(byte1 != k)
                {
                    k = byte1;
                    if(j1 == 0)
                    {
                        i = 1;
                    } else
                    {
                        abyte0[i1] = byte0;
                        i1++;
                        j1--;
                        flag = true;
                        continue;
                    }
                    break label0;
                }
                if(j != l1)
                    continue;
                if(j1 == 0)
                {
                    i = 1;
                    break label0;
                }
                abyte0[i1] = byte0;
                i1++;
                j1--;
                flag = true;
            } while(true);
            i = 2;
            l = ai[l];
            byte byte2 = (byte)(l & 0xff);
            l >>= 8;
            if(++j != l1)
                if(byte2 != k)
                {
                    k = byte2;
                } else
                {
                    i = 3;
                    l = ai[l];
                    byte byte3 = (byte)(l & 0xff);
                    l >>= 8;
                    if(++j != l1)
                        if(byte3 != k)
                        {
                            k = byte3;
                        } else
                        {
                            l = ai[l];
                            byte byte4 = (byte)(l & 0xff);
                            l >>= 8;
                            j++;
                            i = (byte4 & 0xff) + 4;
                            l = ai[l];
                            k = (byte)(l & 0xff);
                            l >>= 8;
                            j++;
                        }
                }
        } while(true);
        int i2 = a.Q;
        a.Q += k1 - j1;
        if(a.Q < i2)
            a.R++;
        a.T = byte0;
        a.U = i;
        a.f = j;
        a.b = k;
        A.a = ai;
        a.c = l;
        a.N = abyte0;
        a.O = i1;
        a.P = j1;
    }

    public static final void out(A a)
    {
        a.e = 0;
        for(int i = 0; i < 256; i++)
            if(a.g[i])
            {
                a.k[a.e] = (byte)i;
                a.e++;
            }

    }

    public static final byte println(A a)
    {
        return (byte)I(1, a);
    }

    public static final void B(int ai[], int ai1[], int ai2[], byte abyte0[], int i, int j, int k)
    {
        int l = 0;
        for(int i1 = i; i1 <= j; i1++)
        {
            for(int k2 = 0; k2 < k; k2++)
                if(abyte0[k2] == i1)
                {
                    ai2[l] = k2;
                    l++;
                }

        }

        for(int j1 = 0; j1 < 23; j1++)
            ai1[j1] = 0;

        for(int k1 = 0; k1 < k; k1++)
            ai1[abyte0[k1] + 1]++;

        for(int l1 = 1; l1 < 23; l1++)
            ai1[l1] += ai1[l1 - 1];

        for(int i2 = 0; i2 < 23; i2++)
            ai[i2] = 0;

        int j2 = 0;
        for(int l2 = i; l2 <= j; l2++)
        {
            j2 += ai1[l2 + 1] - ai1[l2];
            ai[l2] = j2 - 1;
            j2 <<= 1;
        }

        for(int i3 = i + 1; i3 <= j; i3++)
            ai1[i3] = (ai[i3 - 1] + 1 << 1) - ai1[i3];

    }

    public static final int I(byte abyte0[], int i, byte abyte1[], int j, int k)
    {
        synchronized (B) {
            B.G = abyte1;
            B.H = k;
            B.N = abyte0;
            B.O = 0;
            B.K = j;
            B.P = i;
            B.X = 0;
            B.W = 0;
            B.L = 0;
            B.M = 0;
            B.Q = 0;
            B.R = 0;
            B.i = 0;
            I(B);
            i -= B.P;
            return i;
        }
    }

    public static final int I(int i, A a)
    {
        int j;
        do
        {
            if(a.X >= i)
            {
                int k = a.W >> a.X - i & (1 << i) - 1;
                a.X -= i;
                j = k;
                break;
            }
            a.W = a.W << 8 | a.G[a.H] & 0xff;
            a.X += 8;
            a.H++;
            a.K--;
            a.L++;
            if(a.L == 0)
                a.M++;
        } while(true);
        return j;
    }

    public static A B = new A();

}
