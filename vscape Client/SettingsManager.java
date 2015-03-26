import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SettingsManager {

	public static final String settings_dir = Signlink.findcachedir() + "settings.ini";
	
	public static int sizeMode = 0;
	public static int resizableW = 900;
	public static int resizableH = 600;

	public static void write() {
		File file = new File(settings_dir);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch(IOException ioexception) {
			System.out.println("error writing settings file.");
		}
		if(file.exists())
		{
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(settings_dir))){
				writer.write("[CHARACTER]");
				writer.newLine();
				writer.write("remember = " + Client.rememberMe);
				writer.newLine();
				writer.write("username = " + (Client.rememberMe ? Client.myUsername : ""));
				writer.newLine();
				writer.write("password = " + (Client.rememberMe ? Client.myPassword : ""));
				writer.newLine(); 
				writer.newLine();
				writer.write("[CHAT]");
				writer.newLine();
				writer.write("game = " + Client.instance.gameMode);
				writer.newLine();
				writer.write("public = " + Client.instance.publicChatMode);
				writer.newLine();
				writer.write("private = " + Client.instance.privateChatMode);
				writer.newLine();
				writer.write("clan = " + Client.instance.clanChatMode);
				writer.newLine();
				writer.write("trade = " + Client.instance.tradeMode);
				writer.newLine();
				writer.write("global = " + Client.instance.globalMode);
				writer.newLine();
				writer.newLine();
				writer.write("[GRAPHICAL]");
				writer.newLine();
				writer.write("sizeMode = " + (Client.clientSize >= 1 ? "1" : "0"));
				writer.newLine();
				writer.write("resizableW = " + Client.clientWidth);
				writer.newLine();
				writer.write("resizableH = " + Client.clientHeight);
				writer.newLine();
				writer.write("roofs = " + !Client.instance.roofsToggled);
				writer.newLine();
				writer.newLine();
				writer.write("[MISC]");
				writer.newLine();
				writer.write("loginMusic = " + Client.loginMusicEnabled);
				writer.newLine();
				writer.write("xpDrop = " + Client.instance.xpDropEnabled);
				writer.newLine();
				writer.flush();
				writer.close();
			} catch(IOException ioexception) {
				System.out.println("error writing settings file.");
			}
		}
		sizeMode = (Client.clientSize >= 1 ? 1 : 0);
		resizableW = Client.clientWidth;
		resizableH = Client.clientHeight;
	}
	
	public static void load(){
		File file = new File(settings_dir);
		if (file.exists()) {
			String line = "";
			String token = "";
			String token2 = "";
			String[] token3 = new String[3];
			try(BufferedReader reader = new BufferedReader(new FileReader(settings_dir))){
				line = reader.readLine();
				while(line != null) {
					line = line.trim();
					int equalIndex = line.indexOf("=");
					if (equalIndex > -1) {
						token = line.substring(0, equalIndex);
						token = token.trim();
						token2 = line.substring(equalIndex + 1);
						token2 = token2.trim();
						token3 = token2.split("\t");
						if(!token2.isEmpty() && token2.length() > 0)
						{
							switch(token) {
								case "remember" :
									Client.rememberMe = Boolean.parseBoolean(token2);
								break;
								case "username" :
									Client.myUsername = token2;
								break;
								case "password" :
									Client.myPassword = token2;
								break;
								case "sizeMode" :
									sizeMode = Integer.parseInt(token2);
								break;
								case "resizableW" :
									resizableW = Integer.parseInt(token2);
								break;
								case "resizableH" :
									resizableH = Integer.parseInt(token2);
								break;
								case "roofs" :
									Client.instance.roofsToggled = !Boolean.parseBoolean(token2);
								break;
								case "game" :
									 Client.instance.gameMode = Integer.parseInt(token2);
								break;
								case "public" :
									 Client.instance.publicChatMode = Integer.parseInt(token2);
								break;
								case "private" :
									 Client.instance.privateChatMode = Integer.parseInt(token2);
								break;
								case "clan" :
									 Client.instance.clanChatMode = Integer.parseInt(token2);
								break;
								case "trade" :
									 Client.instance.tradeMode = Integer.parseInt(token2);
								break;
								case "global" :
									 Client.instance.globalMode = Integer.parseInt(token2);
								break;
								case "loginMusic" :
									Client.loginMusicEnabled = Boolean.parseBoolean(token2);
								break;
								case "xpDrop" :
									Client.instance.xpDropEnabled = Boolean.parseBoolean(token2);
								break;
							}
						}
					}
					line = reader.readLine();
				}
				reader.close();
			} catch(IOException ioexception) {
				System.out.println("error writing settings file.");
			}
		}
	}
}
