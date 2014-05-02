package com.rs2.model.content.minigames.castlewars;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class CastlewarsLogging {
	Format formatter = new SimpleDateFormat("YYYY-MM-dd_hh-mm");
	Date date = new Date();
	private Logger logger;
	
	public CastlewarsLogging(){
	logger = Logger.getLogger("CwLog");
	try { 
		String outFile = "/data/content/cw/cw" + formatter.format(date) + ".log";
        FileHandler fh = new FileHandler(outFile);  
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();  
        fh.setFormatter(formatter);  
        logger.info("Castlewars starting");  

    } catch (SecurityException e0) {  
        e0.printStackTrace();  
    } catch (IOException e1) {  
        e1.printStackTrace();  
    }  
}
	
	public void log(String text){
		logger.info(text);
	}

}