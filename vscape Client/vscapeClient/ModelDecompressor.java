package vscapeClient;
import java.io.DataInputStream;
import java.io.FileInputStream;
import vscapeClient.sign.signlink;

/**
 *Loads models from a data file
 *
 *@author Ben(songoty)
 */

public class ModelDecompressor {

	public static void loadModels() {
		try {
			DataInputStream indexFile = new DataInputStream(new FileInputStream(signlink.findcachedir() +"/model.idx"));
			DataInputStream dataFile = new DataInputStream(new FileInputStream(signlink.findcachedir() +"/model.dat"));
			int length = indexFile.readInt();
			for(int i = 0; i < length; i++) {
				int id = indexFile.readInt();
				int invlength = indexFile.readInt();
				byte[] data = new byte[invlength];
				dataFile.readFully(data);
			//	System.out.println("ID: "+ id +" Length: "+ invlength +" Data: "+ data);
				Model.method460(data, id);
			}
			indexFile.close();
			dataFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}