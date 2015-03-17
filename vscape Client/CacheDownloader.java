import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.net.URL;
//import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CacheDownloader {

        private Client client;

        private final int BUFFER = 1024;

        /*
         * Only things you need to change
         *
         */
        private String versionURL = "https://dl.dropboxusercontent.com/u/31306161/vscape/cacheVersion.dat";
        private String cacheLink = "https://dl.dropboxusercontent.com/u/31306161/vscape/vscape2.zip";
        private int localCacheVersion = 0;
        
        private String fileToExtract = getCacheDir() + getArchivedName();

        public CacheDownloader(Client client) {
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
                return Signlink.findcachedir();
        }

        private String getCacheLink() {
            if(Client.CACHE_DEV_BRANCH)
            {
            	cacheLink = "https://dl.dropboxusercontent.com/u/31306161/vscape/dev/cache.zip";
            }
            return cacheLink;
        }
        
        private String getVersionLink() {
            if(Client.CACHE_DEV_BRANCH)
            {
            	versionURL = "https://dl.dropboxusercontent.com/u/31306161/vscape/dev/cacheVersion_dev.dat";
            }
            return versionURL;
        }

        private int getCacheRemoteVersion() throws IOException {
        	BufferedReader cacheVerReader = new BufferedReader(new InputStreamReader(new URL(getVersionLink()).openStream()));    
    		String line;
    		try {
    			if((line = cacheVerReader.readLine()) != null) {
        			cacheVerReader.close();
    				return Integer.parseInt(line);
    			}else{
        			cacheVerReader.close();
                	return 0;
    			}
    		} catch(IOException e) {
    			System.out.println("problem reading remote cache version");
    			cacheVerReader.close();
    			return 0;
    		}
        }
        
        private int getCacheLocalVersion() throws IOException {
        	File versionFile = new File(getCacheDir() + "cacheVersion.dat");
			BufferedReader cacheVerReader = new BufferedReader(new FileReader(versionFile));
    		String line;
			if(!versionFile.exists()) {
				cacheVerReader.close();
			    return 0;
			}
    		try {
    			if((line = cacheVerReader.readLine()) != null) {
        			cacheVerReader.close();
                	return Integer.parseInt(line);
    			}else{
        			cacheVerReader.close();
                	return 0;
    			}
    		} catch(IOException e) {
    			cacheVerReader.close();
    			return 0;
    		}
        }

        public CacheDownloader downloadCache() {
            try {
	            File versionFile = new File(getCacheDir() + "cacheVersion.dat");

	            int remoteVer = getCacheRemoteVersion();
	            if(!versionFile.exists())
	            {	
		            downloadFile(getCacheLink(), getArchivedName());
		            unZip();
		                
	                localCacheVersion = getCacheRemoteVersion();
		            BufferedWriter versionFileWriter = new BufferedWriter(new FileWriter(versionFile));
	                versionFileWriter.write(localCacheVersion+"");
	                versionFileWriter.flush();
	                versionFileWriter.close();
	            }
	            else
	            {
	            	localCacheVersion = getCacheLocalVersion();
	            	if(remoteVer != localCacheVersion)
	            	{
		                downloadFile(getCacheLink(), getArchivedName());
		                unZip();
		                
		                localCacheVersion = getCacheRemoteVersion();
			            BufferedWriter versionFileWriter = new BufferedWriter(new FileWriter(versionFile));
		                versionFileWriter.write(localCacheVersion+"");
		                versionFileWriter.flush();
		                versionFileWriter.close();
	            	}
	            	else
	            	{
	            		return null;
	            	}
	            }
            } catch(Exception e) {
		e.printStackTrace();
            }
            return null;
        }
        
        private void downloadFile(String adress, String localFileName) {
                OutputStream out = null;
                URLConnection conn = null;
                //conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
                InputStream in = null;
                int lastpercent = 0;
                
                try {

                        URL url = new URL(adress);
                        out = new BufferedOutputStream(
                                new FileOutputStream(getCacheDir() + localFileName)); 

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
                                if(percentage > lastpercent)
                                {
                                	drawLoadingText(percentage, "Downloading Cache " + percentage + "%");
                                	lastpercent = percentage;
                                }

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

                deleteZIP(new File(fileToExtract));
                
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
        
    	private void deleteZIP(File f) {
    		String fileName = f.getName();
    		if (!f.exists())
    			throw new IllegalArgumentException("Cache Delete: no such file or directory: " + fileName);

    		if (!f.canWrite())
    			throw new IllegalArgumentException("Cache Delete: write protected: " + fileName);

    		if (f.isDirectory()) {
    			String[] files = f.list();
    			if (files.length > 0)
    				throw new IllegalArgumentException("Cache Delete: directory not empty: " + fileName);
    		}
    		
    		boolean success = f.delete();
    		if (!success)
    			throw new IllegalArgumentException("Cache Delete: deletion failed");

    	}
}
