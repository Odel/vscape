package viewer;
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   I

public class IoBuffer /*extends CacheableNode*/ {

    public byte buffer[];
    public int length;

    public IoBuffer(byte buffer[]) {
        this.buffer = buffer;
        length = 0;
    }

    public final int readInt() {
        length += 4;
        return ((buffer[length - 4] & 0xff) << 24) + ((buffer[length - 3] & 0xff) << 16) + ((buffer[length - 2] & 0xff) << 8) + (buffer[length - 1] & 0xff);
    }

    public final int readUnsigned() {
        return buffer[length++] & 0xff;
    }

    public final byte read() {
        return buffer[length++];
    }

    public final int readShort() {
        length += 2;
        return ((buffer[length - 2] & 0xff) << 8) + (buffer[length - 1] & 0xff);
    }

    public final String readString() {
        int i = length;
        while (buffer[length++] != 10) ;
        return new String(buffer, i, length - i - 1);
    }

    public final int F() {
        length += 3;
        return ((buffer[length - 3] & 0xff) << 16) + ((buffer[length - 2] & 0xff) << 8) + (buffer[length - 1] & 0xff);
    }
}
