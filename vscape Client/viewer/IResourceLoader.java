package viewer;
import java.io.File;
import java.io.InputStream;

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   I/I


public class IResourceLoader {

    public IResourceLoader() {
    }
    
    private static String cacheName = "/.vscape2/";
    
    public static String findcachedir()
    {
		/*if(Client.CACHE_DEV_BRANCH)
		{
			cacheName = "/.vscape2_dev/";
		}*/
        boolean exists = (new File((new StringBuilder(String.valueOf(System.getProperty("user.home")))).append(cacheName).toString())).exists();
        if(exists)
        {
            return (new StringBuilder(String.valueOf(System.getProperty("user.home")))).append(cacheName).toString();
        } else
        {
            File f = new File((new StringBuilder(String.valueOf(System.getProperty("user.home")))).append(cacheName).toString());
            f.mkdir();
            return (new StringBuilder(String.valueOf(System.getProperty("user.home")))).append(cacheName).toString();
        }
    }

    public static final synchronized String I(int i) {
        int j = i & 0xff;
        if (intern[j] != i) {
            intern[j] = i;
            if (i < 0)
                i &= 0xffff;
            //System.out.println(i);
            String s = (new String(getClass, i, getClass[i - 1] & 0xff)).intern();
            getResourceAsStream[j] = s;
        }
        //System.out.println(getResourceAsStream[j]);
        if(getResourceAsStream[j] == "worldmap.dat")
        {
        	return findcachedir()+"worldmap.dat";
        }
        return getResourceAsStream[j];
    }

    static byte getClass[];
    static String getResourceAsStream[] = new String[256];
    static int intern[] = new int[256];

    static {
        try {
            InputStream inputstream = (new IResourceLoader()).getClass().getResourceAsStream("I.gif");
            if (inputstream != null) {
                int i = inputstream.read() << 16 | inputstream.read() << 8 | inputstream.read();
                getClass = new byte[i];
                int j = 0;
                byte byte0 = (byte) i;
                byte abyte0[] = getClass;
                while (i != 0) {
                    int k = inputstream.read(abyte0, j, i);
                    if (k == -1)
                        break;
                    i -= k;
                    for (k += j; j < k; j++)
                        abyte0[j] ^= byte0;

                }
                
                
                inputstream.close();
            }
        } catch (Exception exception) {
        	System.out.println("notfound");        }
        }
    
}
