import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.net.URL;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.Enumeration;

import sign.signlink;

public class CacheDownloader {

        private client client;

        private final int BUFFER = 1024;

        /*
         * Only things you need to change
         *
         */
        private final int VERSION = 1; // Version of cache
        private String cacheLink = "https://dl.dropboxusercontent.com/u/162363240/2006-Memorys.zip"; // Link to cache

        private String fileToExtract = getCacheDir() + getArchivedName();

        public CacheDownloader(client client) {
                this.client = client;
        }

        private void drawLoadingText(String text) {
                client.drawLoadingText(35, text);
                System.out.println(text);
        }


        private void drawLoadingText(int amount, String text) {
                client.drawLoadingText(amount, text);
                System.out.println(text);
        }

        private String getCacheDir() {
                return signlink.findcachedir();
        }

        private String getCacheLink() {
                return cacheLink;
        }

        private int getCacheVersion() {
                return VERSION;
        }

        public CacheDownloader downloadCache() {
                try {
                File location = new File(getCacheDir());
                File version = new File(getCacheDir() + "/cacheVersion" + getCacheVersion() + ".dat");
                
                if(!location.exists()) {
                        //drawLoadingText("Downloading Cache Please wait...");
                        downloadFile(getCacheLink(), getArchivedName());

                        unZip();
                      //  System.out.println("UNZIP");

                        BufferedWriter versionFile = new BufferedWriter(new FileWriter(getCacheDir() + "/cacheVersion" + getCacheVersion() + ".dat"));
                        versionFile.close();
                } else {
                        if(!version.exists()) {
                                //drawLoadingText("Downloading Cache Please wait...");
                                downloadFile(getCacheLink(), getArchivedName());

                                unZip();
                               // System.out.println("UNZIP");

                                BufferedWriter versionFile = new BufferedWriter(new FileWriter(getCacheDir() + "/cacheVersion" + getCacheVersion() + ".dat"));
                                versionFile.close();

                        } else {
                                return null;
                        }
                }
                } catch(Exception e) {

                }
                return null;
        }
        
        private void downloadFile(String adress, String localFileName) {
                OutputStream out = null;
                URLConnection conn;
                InputStream in = null;
                
                try {

                        URL url = new URL(adress);
                        out = new BufferedOutputStream(
                                new FileOutputStream(getCacheDir() + "/" +localFileName)); 

                        conn = url.openConnection();
                        in = conn.getInputStream(); 
                
                        byte[] data = new byte[BUFFER]; 
        
                        int numRead;
                        long numWritten = 0;
                        int length = conn.getContentLength();

        
                        while((numRead = in.read(data)) != -1) {
                                out.write(data, 0, numRead);
                                numWritten += numRead;

                                    int percentage = (int)(((double)numWritten / (double)length) * 100D);
                                drawLoadingText(percentage, "Downloading Cache " + percentage + "%");

                        }

                       // System.out.println(localFileName + "\t" + numWritten);
                        drawLoadingText("Finished downloading "+getArchivedName()+"!");

                } catch (Exception exception) {
                        exception.printStackTrace();
                } finally {
                        try {
                                if (in != null) {
                                        in.close();
                                }
                                if (out != null) {
                                        out.close();
                                }
                        } catch (IOException ioe) {
                        }
                }

        }

        private String getArchivedName() {
                int lastSlashIndex = getCacheLink().lastIndexOf('/');
                if (lastSlashIndex >= 0 
                        && lastSlashIndex < getCacheLink().length() -1) { 
                        return getCacheLink().substring(lastSlashIndex + 1);
                } else {
                        System.err.println("error retreiving archivaed name.");
                }
                return "";
        }



        private void unZip() {

            try {
                    InputStream in = 
                    new BufferedInputStream(new FileInputStream(fileToExtract));
                ZipInputStream zin = new ZipInputStream(in);
                ZipEntry e;

                while((e=zin.getNextEntry()) != null) {

                               if(e.isDirectory()) {
                        (new File(getCacheDir() + e.getName())).mkdir();
                               } else {

                    if (e.getName().equals(fileToExtract)) {
                        unzip(zin, fileToExtract);
                        break;
                    }
                               unzip(zin, getCacheDir() + e.getName());
                    }
                 //   System.out.println("unzipping2 " + e.getName());
                }
                zin.close();

            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        private void unzip(ZipInputStream zin, String s) 
                throws IOException {

                FileOutputStream out = new FileOutputStream(s);
                //System.out.println("unzipping " + s);
                byte [] b = new byte[BUFFER];
                int len = 0;

                while ((len = zin.read(b)) != -1) {
                        out.write(b,0,len);
                }
                out.close();
        }
}