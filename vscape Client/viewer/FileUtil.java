package viewer;
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   K

import java.io.*;

public class FileUtil {

    public FileUtil() {
    }

    public static final byte[] readFile(String fileName) {
        File file = new File(fileName);
        int length = (int) file.length();
        byte buffer[] = new byte[length];
        try (DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(new FileInputStream(fileName)))) {
            datainputstream.readFully(buffer, 0, length);
            return buffer;
        } catch (IOException ex) {
            System.out.println((new StringBuilder()).append(IResourceLoader.I(1385)).append(fileName).toString());
        }
        return null;
    }
}
