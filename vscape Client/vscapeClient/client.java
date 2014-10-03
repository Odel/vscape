package vscapeClient;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import java.applet.AppletContext;
import java.awt.*;
import java.io.*;
import java.math.BigInteger;

import javax.sound.midi.*;
import javax.sound.sampled.*;

import java.net.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

import vscapeClient.sign.signlink;

public class client extends RSApplet {
	
	private static boolean DevMode = false;
	private static boolean MusicEnabled = true;

	public static String getHost() {
		return "127.0.0.1";
	}
	
	public static int getPort() {
		return 43594;
	}

	private static String intToKOrMilLongName(int i)
	{
		String s = String.valueOf(i);
		for(int k = s.length() - 3; k > 0; k -= 3)
			s = s.substring(0, k) + "," + s.substring(k);
		if(s.length() > 8)
			s = "@gre@" + s.substring(0, s.length() - 8) + " million @whi@(" + s + ")";
		else
		if(s.length() > 4)
			s = "@cya@" + s.substring(0, s.length() - 4) + "K @whi@(" + s + ")";
		return " " + s;
	}

	private void stopMidi()
	{
	    if(MusicEnabled) {
		MidiPlayer.getSingleton().stop();
		signlink.midifade = 0;
		signlink.midi = "stop";
	    }
	}
	
	private boolean menuHasAddFriend(int j)
	{
		if(j < 0)
			return false;
		int k = menuActionID[j];
		if(k >= 2000)
			k -= 2000;
		return k == 337;
	}
	
	public void drawChannelButtons() { 
		String gameText[] = { "On", "Off" };
		int gameTextColor[] = { 65280, 0xff0000 };
		String globalText[] = { "On", "Hidden", "Off" };
		int globalTextColor[] = { 65280, 65535, 0xff0000 };
		String text[] = { "On", "Friends", "Off", "Hide" };
		int textColor[] = { 65280, 0xffff00, 0xff0000, 65535 };
		/* Draws main buttons sprite */
		chatButtons.drawSprite(5, 143);
		/* Button hovering */
		if(cButtonHPos == 7) {
			reportH.drawSprite(404, 143);
		}
		if(cButtonCPos == 0)
				chatButtonC.drawSprite(5, 143);
			else if(cButtonCPos == 1)
				chatButtonC.drawSprite(62, 143);
			else if(cButtonCPos == 2)
				chatButtonC.drawSprite(119, 143);
			else if(cButtonCPos == 3)
				chatButtonC.drawSprite(176, 143);
			else if(cButtonCPos == 4)
				chatButtonC.drawSprite(233, 143);
			else if(cButtonCPos == 5)
				chatButtonC.drawSprite(290, 143);
			else if(cButtonCPos == 6)
				chatButtonC.drawSprite(347, 143);
		if(cButtonHPos == cButtonCPos) {
			if(cButtonHPos == 0)
				chatButtonHC.drawSprite(5, 143);
			else if(cButtonHPos == 1)
				chatButtonHC.drawSprite(62, 143);
			else if(cButtonHPos == 2)
				chatButtonHC.drawSprite(119, 143);
			else if(cButtonHPos == 3)
				chatButtonHC.drawSprite(176, 143);
			else if(cButtonHPos == 4)
				chatButtonHC.drawSprite(233, 143);
			else if(cButtonHPos == 5)
				chatButtonHC.drawSprite(290, 143);
			else if(cButtonHPos == 6)
				chatButtonHC.drawSprite(347, 143);
		} else {
			if(cButtonHPos == 0)
				chatButtonH.drawSprite(5, 143);
			else if(cButtonHPos == 1)
				chatButtonH.drawSprite(62, 143);
			else if(cButtonHPos == 2)
				chatButtonH.drawSprite(119, 143);
			else if(cButtonHPos == 3)
				chatButtonH.drawSprite(176, 143);
			else if(cButtonHPos == 4)
				chatButtonH.drawSprite(233, 143);
			else if(cButtonHPos == 5)
				chatButtonH.drawSprite(290, 143);
			else if(cButtonHPos == 6)
				chatButtonH.drawSprite(347, 143);
		}
		smallText.method389(true, 26, 0xffffff, "All", 158);
		smallText.method389(true, 77, 0xffffff, "Game", 153);
		smallText.method389(true, 131, 0xffffff, "Public", 153);
		smallText.method389(true, 185, 0xffffff, "Private", 153);
		smallText.method389(true, 247, 0xffffff, "Trade", 153);
		smallText.method389(true, 304, 0xffffff, "Duel", 153);
		smallText.method389(true, 360, 0xffffff, "Global", 153); // GLOBAL CHAT
	//	smallText.method389(true, 427, 0xffffff, "Report Abuse", 158);
		smallText.method382(gameTextColor[gameMode], 90, gameText[gameMode], 164, true);
		smallText.method382(textColor[publicChatMode], 146, text[publicChatMode], 164, true);
		smallText.method382(textColor[privateChatMode], 203, text[privateChatMode], 164, true);
		smallText.method382(textColor[tradeMode], 260, text[tradeMode], 164, true);
		smallText.method382(textColor[duelMode], 315, text[duelMode], 164, true);
		smallText.method382(globalTextColor[globalMode], 374, globalText[globalMode], 164, true);
	}
	
	private void drawChatArea() /* This draws/displays the chat area */
	{
		aRSImageProducer_1166.initDrawingArea();
		Texture.anIntArray1472 = anIntArray1180;
		chatArea.drawSprite(0, 0);
		drawChannelButtons();
		TextDrawingArea textDrawingArea = aTextDrawingArea_1271;
		if(messagePromptRaised) {
			chatTextDrawingArea.drawText(0, aString1121, 60, 259);
			chatTextDrawingArea.drawText(128, promptInput + "*", 80, 259);
		} else
		if(inputDialogState == 1) {
			chatTextDrawingArea.drawText(0, "Enter amount:", 60, 259);
			chatTextDrawingArea.drawText(128, amountOrNameInput + "*", 80, 259);
		} else
		if(inputDialogState == 2) {
			chatTextDrawingArea.drawText(0, "Enter name:", 60, 259);
			chatTextDrawingArea.drawText(128, amountOrNameInput + "*", 80, 259);
		} else
		if(aString844 != null) {
			chatTextDrawingArea.drawText(0, aString844, 60, 259);
			chatTextDrawingArea.drawText(128, "Click to continue", 80, 259);
		} else
		if(backDialogID != -1)
			drawInterface(0, 20, RSInterface.interfaceCache[backDialogID], 20);
		else
		if(dialogID != -1) {
			drawInterface(0, 20, RSInterface.interfaceCache[dialogID], 20);
		} else {
			int j77 = -3;
			int j = 0;
			DrawingArea.setDrawingArea(121, 8, 497, 7);
			for(int k = 0; k < 500; k++)
			if(chatMessages[k] != null) {
				int l = chatTypes[k];
				int i1 = (70 - j77 * 14) + anInt1089 + 5;
				String s1 = chatNames[k];
				byte byte0 = 0;
				if(s1 != null && s1.startsWith("@cr1@")) {
					s1 = s1.substring(5);
					byte0 = 1;
				}
				if(s1 != null && s1.startsWith("@cr2@")) {
					s1 = s1.substring(5);
					byte0 = 2;
				}
				if(s1 != null && s1.startsWith("@cr3@")) {
					s1 = s1.substring(5);
					byte0 = 3;
				}
				if(l == 0) {
					if ((chatTypeView == 5 || chatTypeView == 0) && gameMode == 0) {
						if(i1 > 0 && i1 < 210)
							textDrawingArea.method389(false, 9, 0, chatMessages[k], i1 - 1);//chat color enabled
						j++;
						j77++;
					}
				}
				if((l == 1 || l == 2) && (l == 1 || publicChatMode == 0 || publicChatMode == 1 && isFriendOrSelf(s1))) {
					if (chatTypeView == 1 || chatTypeView == 0) {
						if(i1 > 0 && i1 < 210) {
							int j1 = 8;
							if(byte0 == 1) {
								modIcons[0].method361(j1, i1 - 12);
								j1 += 12;
							}
							if(byte0 == 2) {
								modIcons[1].method361(j1, i1 - 12);
								j1 += 12;
							}
							if(byte0 == 3) {
								modIcons[2].method361(j1, i1 - 12);
								j1 += 12;
							}
							/* draws playername in chatbox[history] */
							textDrawingArea.method385(0, s1 + ":", i1 - 1, j1 + 1);
							j1 += textDrawingArea.getTextWidth(s1) + 8;
							/* draws chat in chatbox[history] */
							textDrawingArea.method389(false, j1 + 1, 255, chatMessages[k], i1 - 1);//chat color enabled;
						}
						j++;
						j77++;
					}
				}
				if((l == 3 || l == 7) && (splitPrivateChat == 0 || chatTypeView == 2) && (l == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(s1)))
				{
					if (chatTypeView == 2 || chatTypeView == 0) {
						if(i1 > 0 && i1 < 210) {
							int k1 = 8;
							textDrawingArea.method385(0, "From", i1, k1);
							k1 += textDrawingArea.getTextWidth("From ");
							if(byte0 == 1) {
								modIcons[0].method361(k1, i1 - 12);
								k1 += 12;
							}
							if(byte0 == 2) {
								modIcons[1].method361(k1, i1 - 12);
								k1 += 12;
							}
							if(byte0 == 3) {
								modIcons[2].method361(k1, i1 - 12);
								k1 += 12;
								k1 += 12;
							}
							textDrawingArea.method385(0, s1 + ":", i1 - 1, k1 + 1);
							k1 += textDrawingArea.getTextWidth(s1) + 8;
							textDrawingArea.method385(0x800000, chatMessages[k], i1 - 1, k1 + 1);
						}
						j++;
						j77++;
					}
				}
				if(l == 4 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s1))) {
					if (chatTypeView == 3 || chatTypeView == 0) {
						if(i1 > 0 && i1 < 210)
							textDrawingArea.method385(0x800080, s1 + " " + chatMessages[k], i1, 8);
						j++;
						j77++;
					}
				}
				if(l == 5 && splitPrivateChat == 0 && privateChatMode < 2) {
					if (chatTypeView == 2 || chatTypeView == 0) {
						if(i1 > 0 && i1 < 210)
							textDrawingArea.method385(0x800000, chatMessages[k], i1, 8);
						j++;
						j77++;
					}
				}
				if(l == 6 && (splitPrivateChat == 0 || chatTypeView == 2) && privateChatMode < 2)
				{
					if (chatTypeView == 2 || chatTypeView == 0) {
						if(i1 > 0 && i1 < 210) {
							textDrawingArea.method385(0, "To " + s1 + ":", i1, 8);
							textDrawingArea.method385(0x800000, chatMessages[k], i1, 12 + textDrawingArea.getTextWidth("To :" + s1));
						}
					j++;
					j77++;
					}
				}
				if(l == 8 && (duelMode == 0 || duelMode == 1 && isFriendOrSelf(s1)))
				{
					if (chatTypeView == 4 || chatTypeView == 0) {
						if(i1 > 0 && i1 < 210)
							textDrawingArea.method385(0x7e3200, s1 + " " + chatMessages[k], i1, 8);
						j++;
						j77++;
					}
					if(l == 11 && (clanChatMode == 0))
					{
						if (chatTypeView == 11) {
						if(i1 > 0 && i1 < 210)
							textDrawingArea.method385(0x7e3200, s1 + " " + chatMessages[k], i1, 8);
						j++;
						j77++;
					}
					if(l == 12)//link
					{
						if(i1 > 0 && i1 < 110)
							textDrawingArea.method385(0x7e3200, chatMessages[k] + " @blu@" + s1, i1, 8);
							j++;
						}
					}
				}
				if(l == 9 && (globalMode == 0 || globalMode == 1)) // GLOBAL CHAT
				{
					if (chatTypeView == 9 || (chatTypeView == 0 && globalMode == 0)) {
						if(i1 > 0 && i1 < 210) {
							int j1 = 8;
							textDrawingArea.method385(255, globalPrefix, i1, j1);
							j1 += textDrawingArea.getTextWidth(globalPrefix);
							if(byte0 == 1) {
								modIcons[0].method361(j1, i1 - 12);
								j1 += 12;
							}
							if(byte0 == 2) {
								modIcons[1].method361(j1, i1 - 12);
								j1 += 12;
							}
							if(byte0 == 3) {
								modIcons[2].method361(j1, i1 - 12);
								j1 += 12;
							}
							textDrawingArea.method385(0, s1 + ":", i1 - 1, j1 + 1);
							j1 += textDrawingArea.getTextWidth(s1) + 8;
							textDrawingArea.method389(false, j1 + 1, 0x7e3200, chatMessages[k], i1 - 1);//chat color enabled;
						}
						j++;
						j77++;
					}
				}
			}
			DrawingArea.defaultDrawingAreaSize();
			anInt1211 = j * 14 + 7 + 5;
			if(anInt1211 < 111)
				anInt1211 = 111;
			drawScrollbar(113, anInt1211 - anInt1089 - 113, 7, 496, anInt1211);
			String s;
			if(myPlayer != null && myPlayer.name != null)
				s = myPlayer.name;
			else
				s = TextClass.fixName(myUsername);
			textDrawingArea.method385(0, s + ":", 132, 9);
			textDrawingArea.drawChatInput(255, 10 + textDrawingArea.getTextWidth(s + ": "), inputString + "*", 132, false);
			DrawingArea.method339(120, 0x807660, 504, 7);
		}
		if(menuOpen && menuScreenArea == 2) {
			drawMenu();
		}
		aRSImageProducer_1166.drawGraphics(338, super.graphics, 0);
		aRSImageProducer_1165.initDrawingArea();
		Texture.anIntArray1472 = anIntArray1182;
	}

	public void init()
    {
        try
        {
            nodeID = 10;
            portOff = 0;
            setHighMem();
            isMembers = true;
            signlink.storeid = 32;
            signlink.startpriv(InetAddress.getLocalHost());
            initClientFrame(505, 767);
            instance = this;
        }
        catch(Exception exception)
        {
            return;
        }
    }

	public void startRunnable(Runnable runnable, int i)
	{
		if(i > 10)
			i = 10;
		if(signlink.mainapp != null)
		{
			signlink.startthread(runnable, i);
		} else
		{
			super.startRunnable(runnable, i);
		}
	}

	public Socket openSocket(int port)
		throws IOException
	{
			return new Socket(InetAddress.getByName(server), port);
	}

	public String getMac()
	{

		
		String command = "";

	    String sOsName = System.getProperty("os.name");
	    if ((sOsName.startsWith("Linux")) || (sOsName.startsWith("Mac")) || (sOsName.startsWith("HP-UX"))) {
	            command = "/sbin/ifconfig";
	    

	    Pattern p = Pattern.compile("([a-fA-F0-9]{1,2}(-|:)){5}[a-fA-F0-9]{1,2}");
	    try {
	        Process pa = null;
			try {
				pa = Runtime.getRuntime().exec(command);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(pa == null)
			{
				System.out.println("the command 'ifconfig' failed to run on your system");
			}
	        pa.waitFor();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(pa.getInputStream()));

	        String line;
	        Matcher m;
	        while ((line = reader.readLine()) != null) {

	            m = p.matcher(line);

	            if (!m.find())
	                continue;
	            line = m.group();
	            break;

	        }
	        return line;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    }
	    else
	    {
		    String firstInterface = null;        
		    Map<String, String> addressByNetwork = new HashMap<>();
		    Enumeration<NetworkInterface> networkInterfaces = null;
			try {
				networkInterfaces = NetworkInterface.getNetworkInterfaces();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		    while(networkInterfaces.hasMoreElements()){
		        NetworkInterface network = networkInterfaces.nextElement();
	
		        byte[] bmac = null;
				try {
					bmac = network.getHardwareAddress();
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        if(bmac != null){
		            StringBuilder sb = new StringBuilder();
		            for (int i = 0; i < bmac.length; i++){
		                sb.append(String.format("%02X%s", bmac[i], (i < bmac.length - 1) ? "-" : ""));        
		            }
	
		            if(sb.toString().isEmpty()==false){
		                addressByNetwork.put(network.getName(), sb.toString());
		                //System.out.println("MAC = "+sb.toString()+" @ ["+network.getName()+"] "+network.getDisplayName());
		            }
	
		            if(sb.toString().isEmpty()==false && firstInterface == null){
		                firstInterface = network.getName();
		            }
		        }
		    }
	
		    if(firstInterface != null){
		        return addressByNetwork.get(firstInterface);
		    }
	
		    return null;
	    }
		return null;
	}
	private void processMenuClick()
	{
		if(activeInterfaceType != 0)
			return;
		int j = super.clickMode3;
		if(spellSelected == 1 && super.saveClickX >= 516 && super.saveClickY >= 160 && super.saveClickX <= 765 && super.saveClickY <= 205)
			j = 0;
		if(menuOpen) {
			if(j != 1) {
				int k = super.mouseX;
				int j1 = super.mouseY;
				if(menuScreenArea == 0) {
					k -= 4;
					j1 -= 4;
				}
				if(menuScreenArea == 1) {
					k -= 519;
					j1 -= 168;
				}
				if(menuScreenArea == 2) {
					k -= 17;
					j1 -= 338;
				}
				if(menuScreenArea == 3) {
					k -= 519;
					j1 -= 0;
				}
				if(k < menuOffsetX - 10 || k > menuOffsetX + menuWidth + 10 || j1 < menuOffsetY - 10 || j1 > menuOffsetY + menuHeight + 10)
				{
					menuOpen = false;
					if(menuScreenArea == 1)
						needDrawTabArea = true;
					if(menuScreenArea == 2)
						inputTaken = true;
				}
			}
			if(j == 1) {
				int l = menuOffsetX;
				int k1 = menuOffsetY;
				int i2 = menuWidth;
				int k2 = super.saveClickX;
				int l2 = super.saveClickY;
				if(menuScreenArea == 0) {
					k2 -= 4;
					l2 -= 4;
				}
				if(menuScreenArea == 1) {
					k2 -= 519;
					l2 -= 168;
				}
				if(menuScreenArea == 2) {
					k2 -= 17;
					l2 -= 338;
				}
				if(menuScreenArea == 3) {
					k2 -= 519;
					l2 -= 0;
				}
				int i3 = -1;
				for(int j3 = 0; j3 < menuActionRow; j3++)
				{
					int k3 = k1 + 31 + (menuActionRow - 1 - j3) * 15;
					if(k2 > l && k2 < l + i2 && l2 > k3 - 13 && l2 < k3 + 3)
						i3 = j3;
				}

				if(i3 != -1)
					doAction(i3);
				menuOpen = false;
				if(menuScreenArea == 1)
					needDrawTabArea = true;
				if(menuScreenArea == 2)
				{
					inputTaken = true;
				}
			}
		} else
		{
			if(j == 1 && menuActionRow > 0)
			{
				int i1 = menuActionID[menuActionRow - 1];
				if(i1 == 632 || i1 == 78 || i1 == 867 || i1 == 431 || i1 == 53 || i1 == 74 || i1 == 454 || i1 == 539 || i1 == 493 || i1 == 847 || i1 == 447 || i1 == 1125)
				{
					int l1 = menuActionCmd2[menuActionRow - 1];
					int j2 = menuActionCmd3[menuActionRow - 1];
					RSInterface class9 = RSInterface.interfaceCache[j2];
					if(class9.aBoolean259 || class9.aBoolean235)
					{
						aBoolean1242 = false;
						anInt989 = 0;
						anInt1084 = j2;
						anInt1085 = l1;
						activeInterfaceType = 2;
						anInt1087 = super.saveClickX;
						anInt1088 = super.saveClickY;
						if(RSInterface.interfaceCache[j2].parentID == openInterfaceID)
							activeInterfaceType = 1;
						if(RSInterface.interfaceCache[j2].parentID == backDialogID)
							activeInterfaceType = 3;
						return;
					}
				}
			}
			if(j == 1 && (anInt1253 == 1 || menuHasAddFriend(menuActionRow - 1)) && menuActionRow > 2)
				j = 2;
			if(j == 1 && menuActionRow > 0)
				doAction(menuActionRow - 1);
			if(j == 2 && menuActionRow > 0)
				determineMenuSize();
		}
	}

	public static int totalRead = 0;

	public static String getFileNameWithoutExtension(String fileName) {
		File tmpFile = new File(fileName);
		tmpFile.getName();
		int whereDot = tmpFile.getName().lastIndexOf('.');
		if (0 < whereDot && whereDot <= tmpFile.getName().length() - 2 ) {
			return tmpFile.getName().substring(0, whereDot);
		}
		return "";
	}

	public void preloadModels() {
		File file = new File (signlink.findcachedir() + ("/Raw/")); 
		File[] fileArray = file.listFiles();
		for(int y = 0; y < fileArray.length; y++) {
			String s = fileArray[y].getName();
			byte[] buffer = ReadFile("./Raw/"+s);
			Model.method460(buffer,Integer.parseInt(getFileNameWithoutExtension(s)));
		}
	}

	public static final byte[] ReadFile(String s) {
		try {
			byte abyte0[];
			File file = new File(s);
			int i = (int)file.length();
			abyte0 = new byte[i];
			DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(new FileInputStream(s)));
			datainputstream.readFully(abyte0, 0, i);
			datainputstream.close();
			totalRead++;
			return abyte0;
		} catch(Exception e) {
			System.out.println((new StringBuilder()).append("Read Error: ").append(s).toString());
			return null;
		}
	}

	public void models() {
		for(int ModelIndex = 0; ModelIndex < 50000; ModelIndex++) {
			byte[] abyte0 = getModel(ModelIndex);
			if(abyte0 != null && abyte0.length > 0) {
			decompressors[1].method234(abyte0.length, abyte0, ModelIndex);
			pushMessage("Model added successfully!", 0, "");
			}
		}
	}

	private void saveMidi(boolean flag, byte abyte0[])
	{
		signlink.midifade = flag ? 1 : 0;
		signlink.midisave(abyte0, abyte0.length);
	}

	private void method22()
	{
		try
		{
			anInt985 = -1;
			aClass19_1056.removeAll();
			aClass19_1013.removeAll();
			Texture.method366();
			unlinkMRUNodes();
			worldController.initToNull();
			System.gc();
			for(int i = 0; i < 4; i++)
				aClass11Array1230[i].method210();

			for(int l = 0; l < 4; l++)
			{
				for(int k1 = 0; k1 < 104; k1++)
				{
					for(int j2 = 0; j2 < 104; j2++)
						byteGroundArray[l][k1][j2] = 0;

				}

			}

			ObjectManager objectManager = new ObjectManager(byteGroundArray, intGroundArray);
			int k2 = aByteArrayArray1183.length;
			stream.createFrame(0);
			if(!aBoolean1159)
			{
				for(int i3 = 0; i3 < k2; i3++)
				{
					int i4 = (anIntArray1234[i3] >> 8) * 64 - baseX;
					int k5 = (anIntArray1234[i3] & 0xff) * 64 - baseY;
					byte abyte0[] = aByteArrayArray1183[i3];
					if(abyte0 != null)
						objectManager.method180(abyte0, k5, i4, (anInt1069 - 6) * 8, (anInt1070 - 6) * 8, aClass11Array1230);
				}

				for(int j4 = 0; j4 < k2; j4++)
				{
					int l5 = (anIntArray1234[j4] >> 8) * 64 - baseX;
					int k7 = (anIntArray1234[j4] & 0xff) * 64 - baseY;
					byte abyte2[] = aByteArrayArray1183[j4];
					if(abyte2 == null && anInt1070 < 800)
						objectManager.method174(k7, 64, 64, l5);
				}

				anInt1097++;
				if(anInt1097 > 160)
				{
					anInt1097 = 0;
					stream.createFrame(238);
					stream.writeWordBigEndian(96);
				}
				stream.createFrame(0);
				for(int i6 = 0; i6 < k2; i6++)
				{
					byte abyte1[] = aByteArrayArray1247[i6];
					if(abyte1 != null)
					{
						int l8 = (anIntArray1234[i6] >> 8) * 64 - baseX;
						int k9 = (anIntArray1234[i6] & 0xff) * 64 - baseY;
						objectManager.method190(l8, aClass11Array1230, k9, worldController, abyte1);
					}
				}

			}
			if(aBoolean1159)
			{
				for(int j3 = 0; j3 < 4; j3++)
				{
					for(int k4 = 0; k4 < 13; k4++)
					{
						for(int j6 = 0; j6 < 13; j6++)
						{
							int l7 = anIntArrayArrayArray1129[j3][k4][j6];
							if(l7 != -1)
							{
								int i9 = l7 >> 24 & 3;
								int l9 = l7 >> 1 & 3;
								int j10 = l7 >> 14 & 0x3ff;
								int l10 = l7 >> 3 & 0x7ff;
								int j11 = (j10 / 8 << 8) + l10 / 8;
								for(int l11 = 0; l11 < anIntArray1234.length; l11++)
								{
									if(anIntArray1234[l11] != j11 || aByteArrayArray1183[l11] == null)
										continue;
									objectManager.method179(i9, l9, aClass11Array1230, k4 * 8, (j10 & 7) * 8, aByteArrayArray1183[l11], (l10 & 7) * 8, j3, j6 * 8);
									break;
								}

							}
						}

					}

				}

				for(int l4 = 0; l4 < 13; l4++)
				{
					for(int k6 = 0; k6 < 13; k6++)
					{
						int i8 = anIntArrayArrayArray1129[0][l4][k6];
						if(i8 == -1)
							objectManager.method174(k6 * 8, 8, 8, l4 * 8);
					}

				}

				stream.createFrame(0);
				for(int l6 = 0; l6 < 4; l6++)
				{
					for(int j8 = 0; j8 < 13; j8++)
					{
						for(int j9 = 0; j9 < 13; j9++)
						{
							int i10 = anIntArrayArrayArray1129[l6][j8][j9];
							if(i10 != -1)
							{
								int k10 = i10 >> 24 & 3;
								int i11 = i10 >> 1 & 3;
								int k11 = i10 >> 14 & 0x3ff;
								int i12 = i10 >> 3 & 0x7ff;
								int j12 = (k11 / 8 << 8) + i12 / 8;
								for(int k12 = 0; k12 < anIntArray1234.length; k12++)
								{
									if(anIntArray1234[k12] != j12 || aByteArrayArray1247[k12] == null)
										continue;
									objectManager.method183(aClass11Array1230, worldController, k10, j8 * 8, (i12 & 7) * 8, l6, aByteArrayArray1247[k12], (k11 & 7) * 8, i11, j9 * 8);
									break;
								}

							}
						}

					}

				}

			}
			stream.createFrame(0);
			objectManager.method171(aClass11Array1230, worldController);
			aRSImageProducer_1165.initDrawingArea();
			stream.createFrame(0);
			int k3 = ObjectManager.anInt145;
			if(k3 > plane)
				k3 = plane;
			if(k3 < plane - 1)
				k3 = plane - 1;
			if(lowMem)
				worldController.method275(ObjectManager.anInt145);
			else
				worldController.method275(0);
			for(int i5 = 0; i5 < 104; i5++)
			{
				for(int i7 = 0; i7 < 104; i7++)
					spawnGroundItem(i5, i7);

			}

			anInt1051++;
			if(anInt1051 > 98)
			{
				anInt1051 = 0;
				stream.createFrame(150);
			}
			method63();
		}
		catch(Exception exception) { }
		ObjectDef.mruNodes1.unlinkAll();
		if(super.gameFrame != null)
		{
			stream.createFrame(210);
			stream.writeDWord(0x3f008edd);
		}
		if(lowMem && signlink.cache_dat != null)
		{
			int j = onDemandFetcher.getVersionCount(0);
			for(int i1 = 0; i1 < j; i1++)
			{
				int l1 = onDemandFetcher.getModelIndex(i1);
				if((l1 & 0x79) == 0)
					Model.method461(i1);
			}

		}
		System.gc();
		Texture.method367();
		onDemandFetcher.method566();
		int k = (anInt1069 - 6) / 8 - 1;
		int j1 = (anInt1069 + 6) / 8 + 1;
		int i2 = (anInt1070 - 6) / 8 - 1;
		int l2 = (anInt1070 + 6) / 8 + 1;
		if(aBoolean1141)
		{
			k = 49;
			j1 = 50;
			i2 = 49;
			l2 = 50;
		}
		for(int l3 = k; l3 <= j1; l3++)
		{
			for(int j5 = i2; j5 <= l2; j5++)
				if(l3 == k || l3 == j1 || j5 == i2 || j5 == l2)
				{
					int j7 = onDemandFetcher.method562(0, j5, l3);
					if(j7 != -1)
						onDemandFetcher.method560(j7, 3);
					int k8 = onDemandFetcher.method562(1, j5, l3);
					if(k8 != -1)
						onDemandFetcher.method560(k8, 3);
				}

		}

	}

	private void unlinkMRUNodes()
	{
		ObjectDef.mruNodes1.unlinkAll();
		ObjectDef.mruNodes2.unlinkAll();
		EntityDef.mruNodes.unlinkAll();
		ItemDef.mruNodes2.unlinkAll();
		ItemDef.mruNodes1.unlinkAll();
		Player.mruNodes.unlinkAll();
		SpotAnim.aMRUNodes_415.unlinkAll();
	}

	private void method24(int i)
	{
		int ai[] = aClass30_Sub2_Sub1_Sub1_1263.myPixels;
		int j = ai.length;
		for(int k = 0; k < j; k++)
			ai[k] = 0;

		for(int l = 1; l < 103; l++)
		{
			int i1 = 24628 + (103 - l) * 512 * 4;
			for(int k1 = 1; k1 < 103; k1++)
			{
				if((byteGroundArray[i][k1][l] & 0x18) == 0)
					worldController.method309(ai, i1, i, k1, l);
				if(i < 3 && (byteGroundArray[i + 1][k1][l] & 8) != 0)
					worldController.method309(ai, i1, i + 1, k1, l);
				i1 += 4;
			}

		}

		int j1 = ((238 + (int)(Math.random() * 20D)) - 10 << 16) + ((238 + (int)(Math.random() * 20D)) - 10 << 8) + ((238 + (int)(Math.random() * 20D)) - 10);
		int l1 = (238 + (int)(Math.random() * 20D)) - 10 << 16;
		aClass30_Sub2_Sub1_Sub1_1263.method343();
		for(int i2 = 1; i2 < 103; i2++)
		{
			for(int j2 = 1; j2 < 103; j2++)
			{
				if((byteGroundArray[i][j2][i2] & 0x18) == 0)
					method50(i2, j1, j2, l1, i);
				if(i < 3 && (byteGroundArray[i + 1][j2][i2] & 8) != 0)
					method50(i2, j1, j2, l1, i + 1);
			}

		}

		aRSImageProducer_1165.initDrawingArea();
		anInt1071 = 0;
		for(int k2 = 0; k2 < 104; k2++)
		{
			for(int l2 = 0; l2 < 104; l2++)
			{
				int i3 = worldController.method303(plane, k2, l2);
				if(i3 != 0)
				{
					i3 = i3 >> 14 & 0x7fff;
					int j3 = ObjectDef.forID(i3).anInt746;
					if(j3 >= 0)
					{
						int k3 = k2;
						int l3 = l2;
						if(j3 != 22 && j3 != 29 && j3 != 34 && j3 != 36 && j3 != 46 && j3 != 47 && j3 != 48)
						{
							byte byte0 = 104;
							byte byte1 = 104;
							int ai1[][] = aClass11Array1230[plane].anIntArrayArray294;
							for(int i4 = 0; i4 < 10; i4++)
							{
								int j4 = (int)(Math.random() * 4D);
								if(j4 == 0 && k3 > 0 && k3 > k2 - 3 && (ai1[k3 - 1][l3] & 0x1280108) == 0)
									k3--;
								if(j4 == 1 && k3 < byte0 - 1 && k3 < k2 + 3 && (ai1[k3 + 1][l3] & 0x1280180) == 0)
									k3++;
								if(j4 == 2 && l3 > 0 && l3 > l2 - 3 && (ai1[k3][l3 - 1] & 0x1280102) == 0)
									l3--;
								if(j4 == 3 && l3 < byte1 - 1 && l3 < l2 + 3 && (ai1[k3][l3 + 1] & 0x1280120) == 0)
									l3++;
							}

						}
						aClass30_Sub2_Sub1_Sub1Array1140[anInt1071] = mapFunctions[j3];
						anIntArray1072[anInt1071] = k3;
						anIntArray1073[anInt1071] = l3;
						anInt1071++;
					}
				}
			}

		}

	}

	private void spawnGroundItem(int i, int j)
	{
		NodeList class19 = groundArray[plane][i][j];
		if(class19 == null)
		{
			worldController.method295(plane, i, j);
			return;
		}
		int k = 0xfa0a1f01;
		Object obj = null;
		for(Item item = (Item)class19.reverseGetFirst(); item != null; item = (Item)class19.reverseGetNext())
		{
			ItemDef itemDef = ItemDef.forID(item.ID);
			int l = itemDef.value;
			if(itemDef.stackable)
				l *= item.anInt1559 + 1;
//	notifyItemSpawn(item, i + baseX, j + baseY);
	
			if(l > k)
			{
				k = l;
				obj = item;
			}
		}

		class19.insertTail(((Node) (obj)));
		Object obj1 = null;
		Object obj2 = null;
		for(Item class30_sub2_sub4_sub2_1 = (Item)class19.reverseGetFirst(); class30_sub2_sub4_sub2_1 != null; class30_sub2_sub4_sub2_1 = (Item)class19.reverseGetNext())
		{
			if(class30_sub2_sub4_sub2_1.ID != ((Item) (obj)).ID && obj1 == null)
				obj1 = class30_sub2_sub4_sub2_1;
			if(class30_sub2_sub4_sub2_1.ID != ((Item) (obj)).ID && class30_sub2_sub4_sub2_1.ID != ((Item) (obj1)).ID && obj2 == null)
				obj2 = class30_sub2_sub4_sub2_1;
		}

		int i1 = i + (j << 7) + 0x60000000;
		worldController.method281(i, i1, ((Animable) (obj1)), method42(plane, j * 128 + 64, i * 128 + 64), ((Animable) (obj2)), ((Animable) (obj)), plane, j);
	}

	private void method26(boolean flag)
	{
		for(int j = 0; j < npcCount; j++)
		{
			NPC npc = npcArray[npcIndices[j]];
			int k = 0x20000000 + (npcIndices[j] << 14);
			if(npc == null || !npc.isVisible() || npc.desc.aBoolean93 != flag)
				continue;
			int l = npc.x >> 7;
			int i1 = npc.y >> 7;
			if(l < 0 || l >= 104 || i1 < 0 || i1 >= 104)
				continue;
			if(npc.anInt1540 == 1 && (npc.x & 0x7f) == 64 && (npc.y & 0x7f) == 64)
			{
				if(anIntArrayArray929[l][i1] == anInt1265)
					continue;
				anIntArrayArray929[l][i1] = anInt1265;
			}
			if(!npc.desc.aBoolean84)
				k += 0x80000000;
			worldController.method285(plane, npc.anInt1552, method42(plane, npc.y, npc.x), k, npc.y, (npc.anInt1540 - 1) * 64 + 60, npc.x, npc, npc.aBoolean1541);
		}
	}

	private boolean replayWave()
	{
			return signlink.wavereplay();
	}

	private void loadError()
	{
		String s = "ondemand";//was a constant parameter
		System.out.println(s);
		try
		{
			getAppletContext().showDocument(new URL(getCodeBase(), "loaderror_" + s + ".html"));
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		do
			try
			{
				Thread.sleep(1000L);
			}
			catch(Exception _ex) { }
		while(true);
	}

	private void buildInterfaceMenu(int i, RSInterface class9, int k, int l, int i1, int j1)
	{
		if(class9.type != 0 || class9.children == null || class9.isMouseoverTriggered)
			return;
		if(k < i || i1 < l || k > i + class9.width || i1 > l + class9.height)
			return;
		int k1 = class9.children.length;
		for(int l1 = 0; l1 < k1; l1++)
		{
			int i2 = class9.childX[l1] + i;
			int j2 = (class9.childY[l1] + l) - j1;
			RSInterface class9_1 = RSInterface.interfaceCache[class9.children[l1]];
			i2 += class9_1.anInt263;
			j2 += class9_1.anInt265;
			if((class9_1.mOverInterToTrigger >= 0 || class9_1.anInt216 != 0) && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height)
				if(class9_1.mOverInterToTrigger >= 0)
					anInt886 = class9_1.mOverInterToTrigger;
				else
					anInt886 = class9_1.id;
			if(class9_1.type == 0)
			{
				buildInterfaceMenu(i2, class9_1, k, j2, i1, class9_1.scrollPosition);
				if(class9_1.scrollMax > class9_1.height)
					method65(i2 + class9_1.width, class9_1.height, k, i1, class9_1, j2, true, class9_1.scrollMax);
			} else
			{
				if(class9_1.atActionType == 1 && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height)
				{
					boolean flag = false;
					if(class9_1.contentType != 0)
						flag = buildFriendsListMenu(class9_1);
					if(!flag)
					{
						//System.out.println("1"+class9_1.tooltip + ", " + class9_1.interfaceID);
						//menuActionName[menuActionRow] = class9_1.tooltip + ", " + class9_1.id; <- BACKUP
						menuActionName[menuActionRow] = class9_1.tooltip;
						menuActionID[menuActionRow] = 315;
						menuActionCmd3[menuActionRow] = class9_1.id;
						menuActionRow++;
					}
				}
				if(class9_1.atActionType == 2 && spellSelected == 0 && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height)
				{
					String s = class9_1.selectedActionName;
					if(s.indexOf(" ") != -1)
						s = s.substring(0, s.indexOf(" "));
					menuActionName[menuActionRow] = s + " @gre@" + class9_1.spellName;
					menuActionID[menuActionRow] = 626;
					menuActionCmd3[menuActionRow] = class9_1.id;
					menuActionRow++;
				}
				if(class9_1.atActionType == 3 && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height)
				{
					menuActionName[menuActionRow] = "Close";
					menuActionID[menuActionRow] = 200;
					menuActionCmd3[menuActionRow] = class9_1.id;
					menuActionRow++;
				}
				if(class9_1.atActionType == 4 && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height)
				{
					//System.out.println("2"+class9_1.tooltip + ", " + class9_1.interfaceID);
					//menuActionName[menuActionRow] = class9_1.tooltip + ", " + class9_1.id; <- BACKUP
					menuActionName[menuActionRow] = class9_1.tooltip;
					menuActionID[menuActionRow] = 169;
					menuActionCmd3[menuActionRow] = class9_1.id;
					menuActionRow++;
				}
				if(class9_1.atActionType == 5 && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height)
				{
					//System.out.println("3"+class9_1.tooltip + ", " + class9_1.interfaceID);
					//menuActionName[menuActionRow] = class9_1.tooltip + ", " + class9_1.id; <- BACKUP
					menuActionName[menuActionRow] = class9_1.tooltip;
					menuActionID[menuActionRow] = 646;
					menuActionCmd3[menuActionRow] = class9_1.id;
					menuActionRow++;
				}
				if(class9_1.atActionType == 6 && !aBoolean1149 && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height)
				{
					//System.out.println("4"+class9_1.tooltip + ", " + class9_1.interfaceID);
					//menuActionName[menuActionRow] = class9_1.tooltip + ", " + class9_1.id; <- BACKUP
					menuActionName[menuActionRow] = class9_1.tooltip;
					menuActionID[menuActionRow] = 679;
					menuActionCmd3[menuActionRow] = class9_1.id;
					menuActionRow++;
				}
				if(class9_1.type == 2)
				{
					int k2 = 0;
					for(int l2 = 0; l2 < class9_1.height; l2++)
					{
						for(int i3 = 0; i3 < class9_1.width; i3++)
						{
							int j3 = i2 + i3 * (32 + class9_1.invSpritePadX);
							int k3 = j2 + l2 * (32 + class9_1.invSpritePadY);
							if(k2 < 20)
							{
								j3 += class9_1.spritesX[k2];
								k3 += class9_1.spritesY[k2];
							}
							if(k >= j3 && i1 >= k3 && k < j3 + 32 && i1 < k3 + 32)
							{
								mouseInvInterfaceIndex = k2;
								lastActiveInvInterface = class9_1.id;
								if(class9_1.inv[k2] > 0)
								{
									ItemDef itemDef = ItemDef.forID(class9_1.inv[k2] - 1);
									if(itemSelected == 1 && class9_1.isInventoryInterface)
									{
										if(class9_1.id != anInt1284 || k2 != anInt1283)
										{
											menuActionName[menuActionRow] = "Use " + selectedItemName + " with @lre@" + itemDef.name;
											menuActionID[menuActionRow] = 870;
											menuActionCmd1[menuActionRow] = itemDef.id;
											menuActionCmd2[menuActionRow] = k2;
											menuActionCmd3[menuActionRow] = class9_1.id;
											menuActionRow++;
										}
									} else
									if(spellSelected == 1 && class9_1.isInventoryInterface)
									{
										if((spellUsableOn & 0x10) == 16)
										{
											menuActionName[menuActionRow] = spellTooltip + " @lre@" + itemDef.name;
											menuActionID[menuActionRow] = 543;
											menuActionCmd1[menuActionRow] = itemDef.id;
											menuActionCmd2[menuActionRow] = k2;
											menuActionCmd3[menuActionRow] = class9_1.id;
											menuActionRow++;
										}
									} else
									{
										if(class9_1.isInventoryInterface)
										{
											for(int l3 = 4; l3 >= 3; l3--)
												if(itemDef.itemActions != null && itemDef.itemActions[l3] != null)
												{
													menuActionName[menuActionRow] = itemDef.itemActions[l3] + " @lre@" + itemDef.name;
													if(l3 == 3)
														menuActionID[menuActionRow] = 493;
													if(l3 == 4)
														menuActionID[menuActionRow] = 847;
													menuActionCmd1[menuActionRow] = itemDef.id;
													menuActionCmd2[menuActionRow] = k2;
													menuActionCmd3[menuActionRow] = class9_1.id;
													menuActionRow++;
												} else
												if(l3 == 4)
												{
													menuActionName[menuActionRow] = "Drop @lre@" + itemDef.name;
													menuActionID[menuActionRow] = 847;
													menuActionCmd1[menuActionRow] = itemDef.id;
													menuActionCmd2[menuActionRow] = k2;
													menuActionCmd3[menuActionRow] = class9_1.id;
													menuActionRow++;
												}

										}
										if(class9_1.usableItemInterface)
										{
											menuActionName[menuActionRow] = "Use @lre@" + itemDef.name;
											menuActionID[menuActionRow] = 447;
											menuActionCmd1[menuActionRow] = itemDef.id;
											menuActionCmd2[menuActionRow] = k2;
											menuActionCmd3[menuActionRow] = class9_1.id;
											menuActionRow++;
										}
										if(class9_1.isInventoryInterface && itemDef.itemActions != null)
										{
											for(int i4 = 2; i4 >= 0; i4--)
												if(itemDef.itemActions[i4] != null)
												{
													menuActionName[menuActionRow] = itemDef.itemActions[i4] + " @lre@" + itemDef.name;
													if(i4 == 0)
														menuActionID[menuActionRow] = 74;
													if(i4 == 1)
														menuActionID[menuActionRow] = 454;
													if(i4 == 2)
														menuActionID[menuActionRow] = 539;
													menuActionCmd1[menuActionRow] = itemDef.id;
													menuActionCmd2[menuActionRow] = k2;
													menuActionCmd3[menuActionRow] = class9_1.id;
													menuActionRow++;
												}

										}
										if(class9_1.actions != null)
										{
											for(int j4 = 4; j4 >= 0; j4--)
												if(class9_1.actions[j4] != null)
												{
													menuActionName[menuActionRow] = class9_1.actions[j4] + " @lre@" + itemDef.name;
													if(j4 == 0)
														menuActionID[menuActionRow] = 632;
													if(j4 == 1)
														menuActionID[menuActionRow] = 78;
													if(j4 == 2)
														menuActionID[menuActionRow] = 867;
													if(j4 == 3)
														menuActionID[menuActionRow] = 431;
													if(j4 == 4)
														menuActionID[menuActionRow] = 53;
													menuActionCmd1[menuActionRow] = itemDef.id;
													menuActionCmd2[menuActionRow] = k2;
													menuActionCmd3[menuActionRow] = class9_1.id;
													menuActionRow++;
												}

										}
										//menuActionName[menuActionRow] = "Examine @lre@" + itemDef.name + " @gre@(@whi@" + (class9_1.inv[k2] - 1) + "@gre@)";
										menuActionName[menuActionRow] = "Examine @lre@" + itemDef.name;
										menuActionID[menuActionRow] = 1125;
										menuActionCmd1[menuActionRow] = itemDef.id;
										menuActionCmd2[menuActionRow] = k2;
										menuActionCmd3[menuActionRow] = class9_1.id;
										menuActionRow++;
									}
								}
							}
							k2++;
						}

					}

				}
			}
		}

	}
	public void drawScrollbar(int j, int k, int l, int i1, int j1) {
		scrollBar1.method361(i1, l);
		scrollBar2.method361(i1, (l + j) - 16);
		DrawingArea.drawPixels(j - 32, l + 16, i1, 0x000001, 16);
		DrawingArea.drawPixels(j - 32, l + 16, i1, 0x3d3426, 15);
		DrawingArea.drawPixels(j - 32, l + 16, i1, 0x342d21, 13);
		DrawingArea.drawPixels(j - 32, l + 16, i1, 0x2e281d, 11);
		DrawingArea.drawPixels(j - 32, l + 16, i1, 0x29241b, 10);
		DrawingArea.drawPixels(j - 32, l + 16, i1, 0x252019, 9);
		DrawingArea.drawPixels(j - 32, l + 16, i1, 0x000001, 1);
		int k1 = ((j - 32) * j) / j1;
		if(k1 < 8)
			k1 = 8;
		int l1 = ((j - 32 - k1) * k) / (j1 - j);
		DrawingArea.drawPixels(k1, l + 16 + l1, i1, barFillColor, 16);
		DrawingArea.method341(l + 16 + l1, 0x000001, k1, i1);
		DrawingArea.method341(l + 16 + l1, 0x8d8269, k1, i1 + 1);
		DrawingArea.method341(l + 16 + l1, 0x6a614d, k1, i1 + 2);
		DrawingArea.method341(l + 16 + l1, 0x706651, k1, i1 + 3);
		DrawingArea.method341(l + 16 + l1, 0x706651, k1, i1 + 4);
		DrawingArea.method341(l + 16 + l1, 0x6a614d, k1, i1 + 5);
		DrawingArea.method341(l + 16 + l1, 0x6a614d, k1, i1 + 6);
		DrawingArea.method341(l + 16 + l1, 0x685e4b, k1, i1 + 7);
		DrawingArea.method341(l + 16 + l1, 0x685e4b, k1, i1 + 8);
		DrawingArea.method341(l + 16 + l1, 0x665d4a, k1, i1 + 9);
		DrawingArea.method341(l + 16 + l1, 0x665d4a, k1, i1 + 10);
		DrawingArea.method341(l + 16 + l1, 0x58503f, k1, i1 + 11);
		DrawingArea.method341(l + 16 + l1, 0x58503f, k1, i1 + 12);
		DrawingArea.method341(l + 16 + l1, 0x544c3c, k1, i1 + 13);
		DrawingArea.method341(l + 16 + l1, 0x4b4535, k1, i1 + 14);
		DrawingArea.method339(l + 16 + l1, 0x000001, 15, i1);
		DrawingArea.method339(l + 17 + l1, 0x000001, 15, i1);
		DrawingArea.method339(l + 17 + l1, 0x6a614d, 14, i1);
		DrawingArea.method339(l + 17 + l1, 0x706651, 13, i1);
		DrawingArea.method339(l + 17 + l1, 0x6d5f48, 11, i1);
		DrawingArea.method339(l + 17 + l1, 0x6a614d, 10, i1);
		DrawingArea.method339(l + 17 + l1, 0x76684b, 7, i1);
		DrawingArea.method339(l + 17 + l1, 0x7d715b, 5, i1);
		DrawingArea.method339(l + 17 + l1, 0x7e6e50, 4, i1);
		DrawingArea.method339(l + 17 + l1, 0x8d8269, 3, i1);
		DrawingArea.method339(l + 17 + l1, 0x000001, 2, i1);
		DrawingArea.method339(l + 18 + l1, 0x000001, 16, i1);
		DrawingArea.method339(l + 18 + l1, 0x625946, 15, i1);
		DrawingArea.method339(l + 18 + l1, 0x665d4a, 14, i1);
		DrawingArea.method339(l + 18 + l1, 0x625640, 11, i1);
		DrawingArea.method339(l + 18 + l1, 0x6a614d, 10, i1);
		DrawingArea.method339(l + 18 + l1, 0x706651, 7, i1);
		DrawingArea.method339(l + 18 + l1, 0x726954, 5, i1);
		DrawingArea.method339(l + 18 + l1, 0x716247, 4, i1);
		DrawingArea.method339(l + 18 + l1, 0x7d715b, 3, i1);
		DrawingArea.method339(l + 18 + l1, 0x8d8269, 2, i1);
		DrawingArea.method339(l + 18 + l1, 0x000001, 1, i1);
		DrawingArea.method339(l + 19 + l1, 0x000001, 16, i1);
		DrawingArea.method339(l + 19 + l1, 0x544c3c, 15, i1);
		DrawingArea.method339(l + 19 + l1, 0x625946, 14, i1);
		DrawingArea.method339(l + 19 + l1, 0x665d4a, 11, i1);
		DrawingArea.method339(l + 19 + l1, 0x685e4b, 9, i1);
		DrawingArea.method339(l + 19 + l1, 0x6a614d, 7, i1);
		DrawingArea.method339(l + 19 + l1, 0x706651, 5, i1);
		DrawingArea.method339(l + 19 + l1, 0x726954, 4, i1);
		DrawingArea.method339(l + 19 + l1, 0x6a614d, 3, i1);
		DrawingArea.method339(l + 19 + l1, 0x8d8269, 2, i1);
		DrawingArea.method339(l + 19 + l1, 0x000001, 1, i1);
		DrawingArea.method339(l + 20 + l1, 0x000001, 16, i1);
		DrawingArea.method339(l + 20 + l1, 0x4b4535, 15, i1);
		DrawingArea.method339(l + 20 + l1, 0x4f4839, 14, i1);
		DrawingArea.method339(l + 20 + l1, 0x58503f, 13, i1);
		DrawingArea.method339(l + 20 + l1, 0x665d4a, 10, i1);
		DrawingArea.method339(l + 20 + l1, 0x685e4b, 8, i1);
		DrawingArea.method339(l + 20 + l1, 0x6a614d, 6, i1);
		DrawingArea.method339(l + 20 + l1, 0x706651, 4, i1);
		DrawingArea.method339(l + 20 + l1, 0x6a614d, 3, i1);
		DrawingArea.method339(l + 20 + l1, 0x8d8269, 2, i1);
		DrawingArea.method339(l + 20 + l1, 0x000001, 1, i1);
		DrawingArea.method341(l + 16 + l1, 0x000001, k1, i1 + 15);
		DrawingArea.method339(l + 15 + l1 + k1, 0x000001, 16, i1);
		DrawingArea.method339(l + 14 + l1 + k1, 0x000001, 15, i1);
		DrawingArea.method339(l + 14 + l1 + k1, 0x413a2e, 14, i1);
		DrawingArea.method339(l + 14 + l1 + k1, 0x453d31, 10, i1);
		DrawingArea.method339(l + 14 + l1 + k1, 0x4f4839, 9, i1);
		DrawingArea.method339(l + 14 + l1 + k1, 0x4a402f, 7, i1);
		DrawingArea.method339(l + 14 + l1 + k1, 0x4b4535, 4, i1);
		DrawingArea.method339(l + 14 + l1 + k1, 0x625946, 3, i1);
		DrawingArea.method339(l + 14 + l1 + k1, 0x000001, 2, i1);
		DrawingArea.method339(l + 13 + l1 + k1, 0x000001, 16, i1);
		DrawingArea.method339(l + 13 + l1 + k1, 0x453d31, 15, i1);
		DrawingArea.method339(l + 13 + l1 + k1, 0x4b4535, 11, i1);
		DrawingArea.method339(l + 13 + l1 + k1, 0x544c3c, 9, i1);
		DrawingArea.method339(l + 13 + l1 + k1, 0x4f4839, 7, i1);
		DrawingArea.method339(l + 13 + l1 + k1, 0x625946, 6, i1);
		DrawingArea.method339(l + 13 + l1 + k1, 0x58503f, 4, i1);
		DrawingArea.method339(l + 13 + l1 + k1, 0x625640, 3, i1);
		DrawingArea.method339(l + 13 + l1 + k1, 0x706651, 2, i1);
		DrawingArea.method339(l + 13 + l1 + k1, 0x000001, 1, i1);
		DrawingArea.method339(l + 12 + l1 + k1, 0x000001, 16, i1);
		DrawingArea.method339(l + 12 + l1 + k1, 0x453d31, 15, i1);
		DrawingArea.method339(l + 12 + l1 + k1, 0x4b4535, 14, i1);
		DrawingArea.method339(l + 12 + l1 + k1, 0x4f4839, 12, i1);
		DrawingArea.method339(l + 12 + l1 + k1, 0x625946, 11, i1);
		DrawingArea.method339(l + 12 + l1 + k1, 0x58503f, 10, i1);
		DrawingArea.method339(l + 12 + l1 + k1, 0x665d4a, 7, i1);
		DrawingArea.method339(l + 12 + l1 + k1, 0x685e4b, 4, i1);
		DrawingArea.method339(l + 12 + l1 + k1, 0x726954, 3, i1);
		DrawingArea.method339(l + 12 + l1 + k1, 0x7d715b, 2, i1);
		DrawingArea.method339(l + 12 + l1 + k1, 0x000001, 1, i1);
		DrawingArea.method339(l + 11 + l1 + k1, 0x000001, 16, i1);
		DrawingArea.method339(l + 11 + l1 + k1, 0x4b4535, 15, i1);
		DrawingArea.method339(l + 11 + l1 + k1, 0x544c3c, 14, i1);
		DrawingArea.method339(l + 11 + l1 + k1, 0x625946, 13, i1);
		DrawingArea.method339(l + 11 + l1 + k1, 0x58503f, 11, i1);
		DrawingArea.method339(l + 11 + l1 + k1, 0x665d4a, 9, i1);
		DrawingArea.method339(l + 11 + l1 + k1, 0x685e4b, 7, i1);
		DrawingArea.method339(l + 11 + l1 + k1, 0x6a614d, 5, i1);
		DrawingArea.method339(l + 11 + l1 + k1, 0x706651, 4, i1);
		DrawingArea.method339(l + 11 + l1 + k1, 0x6a614d, 3, i1);
		DrawingArea.method339(l + 11 + l1 + k1, 0x7d715b, 2, i1);
		DrawingArea.method339(l + 11 + l1 + k1, 0x000001, 1, i1);
	}

		public void updateNPCs(Stream stream, int i) {
			anInt839 = 0;
			anInt893 = 0;
			method139(stream);
			method46(i, stream);
			method86(stream);
			for (int k = 0; k < anInt839; k++) {
				int l = anIntArray840[k];
				if (npcArray[l].anInt1537 != loopCycle) {
					npcArray[l].desc = null;
					npcArray[l] = null;
				}
			}

			if (stream.currentOffset != i) {
				signlink.reporterror(myUsername
						+ " size mismatch in getnpcpos - pos:"
						+ stream.currentOffset + " psize:" + i);
				throw new RuntimeException("eek");
			}
			for (int i1 = 0; i1 < npcCount; i1++)
				if (npcArray[npcIndices[i1]] == null) {
					signlink.reporterror(myUsername
							+ " null entry in npc list - pos:" + i1 + " size:"
							+ npcCount);
					throw new RuntimeException("eek");
				}

		}
		
	private int cButtonHPos;
	private int cButtonHCPos;
	private int cButtonCPos;

	private void processChatModeClick() {
		if(super.mouseX >= 6 && super.mouseX <= 62 && super.mouseY >= 482 && super.mouseY <= 505) {
			cButtonHPos = 0;
			aBoolean1233 = true;
			inputTaken = true;
		}
		else if(super.mouseX >= 63 && super.mouseX <= 118 && super.mouseY >= 482 && super.mouseY <= 505) {
			cButtonHPos = 1;
			aBoolean1233 = true;
			inputTaken = true;
		}
		else if(super.mouseX >= 119 && super.mouseX <= 175 && super.mouseY >= 482 && super.mouseY <= 505) {
			cButtonHPos = 2;
			aBoolean1233 = true;
			inputTaken = true;
		}
		else if(super.mouseX >= 177 && super.mouseX <= 232 && super.mouseY >= 482 && super.mouseY <= 505) {
			cButtonHPos = 3;
			aBoolean1233 = true;
			inputTaken = true;
		}
		else if(super.mouseX >= 233 && super.mouseX <= 289 && super.mouseY >= 482 && super.mouseY <= 505) {
			cButtonHPos = 4;
			aBoolean1233 = true;
			inputTaken = true;
		}
		else if(super.mouseX >= 290 && super.mouseX <= 346 && super.mouseY >= 482 && super.mouseY <= 505) {
			cButtonHPos = 5;
			aBoolean1233 = true;
			inputTaken = true;
		}
		else if(super.mouseX >= 346 && super.mouseX <= 400 && super.mouseY >= 482 && super.mouseY <= 505) {
			cButtonHPos = 6;
			aBoolean1233 = true;
			inputTaken = true;
	/*	else if(super.mouseX >= 404 && super.mouseX <= 515 && super.mouseY >= 482 && super.mouseY <= 505) {
			cButtonHPos = 7;
			aBoolean1233 = true;
			inputTaken = true;*/
		} else {
			cButtonHPos = -1;
			aBoolean1233 = true;
			inputTaken = true;
		}	
	
		if(super.clickMode3 == 1) {
			if(super.saveClickX >= 6 && super.saveClickX <= 62 && super.saveClickY >= 482 && super.saveClickY <= 505) {
				cButtonCPos = 0;
				chatTypeView = 0;
				aBoolean1233 = true;
				inputTaken = true;
			}
			if(super.saveClickX >= 63 && super.saveClickX <= 118 && super.saveClickY >= 482 && super.saveClickY <= 505) {
				cButtonCPos = 1;
				chatTypeView = 5;
				aBoolean1233 = true;
				inputTaken = true;
			}
			if(super.saveClickX >= 119 && super.saveClickX <= 175 && super.saveClickY >= 482 && super.saveClickY <= 505) {
				cButtonCPos = 2;
				chatTypeView = 1;
				aBoolean1233 = true;
				inputTaken = true;
			}
			if(super.saveClickX >= 177 && super.saveClickX <= 232 && super.saveClickY >= 482 && super.saveClickY <= 505) {
				cButtonCPos = 3;
				chatTypeView = 2;
				aBoolean1233 = true;
				inputTaken = true;
			}
			if(super.saveClickX >= 233 && super.saveClickX <= 289 && super.saveClickY >= 482 && super.saveClickY <= 505) {
				cButtonCPos = 4;
				chatTypeView = 3;
				aBoolean1233 = true;
				inputTaken = true;
			}
			if(super.saveClickX >= 290 && super.saveClickX <= 346 && super.saveClickY >= 482 && super.saveClickY <= 505) {
				cButtonCPos = 5;
				chatTypeView = 4;
				aBoolean1233 = true;
				inputTaken = true;
			}
			// GLOBAL CHAT
			if(super.saveClickX >= 347 && super.saveClickX <= 400 && super.saveClickY >= 482 && super.saveClickY <= 505) {
				cButtonCPos = 6;
				chatTypeView = 9;
				aBoolean1233 = true;
				inputTaken = true;
			}
		/*	if(super.saveClickX >= 404 && super.saveClickX <= 515 && super.saveClickY >= 482 && super.saveClickY <= 505) {
				if(openInterfaceID == -1) {
					clearTopInterfaces();
					reportAbuseInput = "";
					canMute = false;
					for(int i = 0; i < RSInterface.interfaceCache.length; i++) {
						if(RSInterface.interfaceCache[i] == null || RSInterface.interfaceCache[i].contentType != 600)
							continue;
						reportAbuseInterfaceID = openInterfaceID = RSInterface.interfaceCache[i].parentID;
						break;
					}

				} else {
					pushMessage("Please close the interface you have open before using 'report abuse'", 0, "");
				}
			}*/
		}
	}
		
/*	private int cButtonHPos;
	private int cButtonHCPos;
	private int cButtonCPos;

	private void processChatModeClick()
	{
		if(super.clickMode3 == 1)
		{
			if(super.saveClickX >= 6 && super.saveClickX <= 106 && super.saveClickY >= 467 && super.saveClickY <= 499)
			{
				publicChatMode = (publicChatMode + 1) % 4;
				aBoolean1233 = true;
				inputTaken = true;
				stream.createFrame(95);
				stream.writeWordBigEndian(publicChatMode);
				stream.writeWordBigEndian(privateChatMode);
				stream.writeWordBigEndian(tradeMode);
			}
			if(super.saveClickX >= 135 && super.saveClickX <= 235 && super.saveClickY >= 467 && super.saveClickY <= 499)
			{
				privateChatMode = (privateChatMode + 1) % 3;
				aBoolean1233 = true;
				inputTaken = true;
				stream.createFrame(95);
				stream.writeWordBigEndian(publicChatMode);
				stream.writeWordBigEndian(privateChatMode);
				stream.writeWordBigEndian(tradeMode);
			}
			if(super.saveClickX >= 273 && super.saveClickX <= 373 && super.saveClickY >= 467 && super.saveClickY <= 499)
			{
				tradeMode = (tradeMode + 1) % 3;
				aBoolean1233 = true;
				inputTaken = true;
				stream.createFrame(95);
				stream.writeWordBigEndian(publicChatMode);
				stream.writeWordBigEndian(privateChatMode);
				stream.writeWordBigEndian(tradeMode);
			}
			if(super.saveClickX >= 404 && super.saveClickX <= 515 && super.saveClickY >= 467 && super.saveClickY <= 499) {
				if(openInterfaceID == -1) {
					clearTopInterfaces();
					reportAbuseInput = "";
					canMute = false;
					for(int i = 0; i < RSInterface.interfaceCache.length; i++) {
						if(RSInterface.interfaceCache[i] == null || RSInterface.interfaceCache[i].contentType != 600)
							continue;
						reportAbuseInterfaceID = openInterfaceID = RSInterface.interfaceCache[i].parentID;
						break;
					}
				} else {
					pushMessage("Please close the interface you have open before using 'report abuse'", 0, "");
				}
			}
		}
	}
*/
	 private void method33(int i)
	    {
	        int j = Varp.cache[i].anInt709;
	        if(j == 0)
	            return;
	        int k = variousSettings[i];
	        if(j == 1)
	        {
	            if(k == 1)
	                Texture.method372(0.90000000000000002D);
	            if(k == 2)
	                Texture.method372(0.80000000000000004D);
	            if(k == 3)
	                Texture.method372(0.69999999999999996D);
	            if(k == 4)
	                Texture.method372(0.59999999999999998D);
	            ItemDef.mruNodes1.unlinkAll();
	            welcomeScreenRaised = true;
	        }
	        if(j == 3)
	        {
		    if(MusicEnabled) {
	            boolean flag = musicEnabled;
	            if(k == 0)
	            {
	            	musicVolume = 200;
	                musicEnabled = true;
	            }
	            if(k == 1)
	            {
	            	musicVolume = 150;
	                musicEnabled = true;
	            }
	            if(k == 2)
	            {
	                musicVolume = 100;
	                musicEnabled = true;
	            }
	            if(k == 3)
	            {
	                musicVolume = 50;
	                musicEnabled = true;
	            }
	            if(k == 4)
	                musicEnabled = false;
	            
	            adjustVolume(musicEnabled, musicVolume);
	            if(musicEnabled != flag && !lowMem)
	            {
	                if(musicEnabled)
	                {
	                    nextSong = currentSong;
	                    songChanging = true;
	                    onDemandFetcher.method558(2, nextSong);
	                } else
	                {
	                    stopMidi();
	                }
	                prevSong = 0;
	            }
	        }
		}
	        if(j == 4)
	        {
	            if(k == 0)
	            {
	                aBoolean848 = true;
	                setWaveVolume(0);
	            }
	            if(k == 1)
	            {
	                aBoolean848 = true;
	                setWaveVolume(-400);
	            }
	            if(k == 2)
	            {
	                aBoolean848 = true;
	                setWaveVolume(-800);
	            }
	            if(k == 3)
	            {
	                aBoolean848 = true;
	                setWaveVolume(-1200);
	            }
	            if(k == 4)
	                aBoolean848 = false;
	        }
	        if(j == 5)
	            anInt1253 = k;
	        if(j == 6)
	            anInt1249 = k;
	        if(j == 8)
	        {
	            splitPrivateChat = k;
	            inputTaken = true;
	        }
	        if(j == 9)
	            anInt913 = k;
	    }


	private void updateEntities() {
		try{
			int anInt974 = 0;
			for(int j = -1; j < playerCount + npcCount; j++) {
			Object obj;
			if(j == -1)
				obj = myPlayer;
			else
			if(j < playerCount)
				obj = playerArray[playerIndices[j]];
			else
				obj = npcArray[npcIndices[j - playerCount]];
			if(obj == null || !((Entity) (obj)).isVisible())
				continue;
			if(obj instanceof NPC) {
				EntityDef entityDef = ((NPC)obj).desc;
				if(entityDef.childrenIDs != null)
					entityDef = entityDef.method161();
				if(entityDef == null)
					continue;
			}
			if(j < playerCount) {
				int l = 30;
				Player player = (Player)obj;
				if(player.headIcon >= 0) {
					npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height + 15);
					if(spriteDrawX > -1) {
						if (player.skullIcon < 2) {
							skullIcons[player.skullIcon].drawSprite(spriteDrawX - 12, spriteDrawY - l);
							l += 25;
						}
						if (player.headIcon < 7) {
							headIcons[player.headIcon].drawSprite(spriteDrawX - 12, spriteDrawY - l);
							l += 18;
						}
                    }
                }
				if(j >= 0 && anInt855 == 10 && anInt933 == playerIndices[j]) {
					npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height + 15);
					if(spriteDrawX > -1)
						headIconsHint[player.hintIcon].drawSprite(spriteDrawX - 12, spriteDrawY - l);
				}
			} else {
				EntityDef entityDef_1 = ((NPC)obj).desc;
				if(entityDef_1.anInt75 >= 0 && entityDef_1.anInt75 < headIcons.length) {
					npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height + 15);
					if(spriteDrawX > -1)
						headIcons[entityDef_1.anInt75].drawSprite(spriteDrawX - 12, spriteDrawY - 30);
				}
				if(anInt855 == 1 && anInt1222 == npcIndices[j - playerCount] && loopCycle % 20 < 10) {
					npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height + 15);
					if(spriteDrawX > -1)
						headIconsHint[0].drawSprite(spriteDrawX - 12, spriteDrawY - 28);
				}
			}
			if(((Entity) (obj)).textSpoken != null && (j >= playerCount || publicChatMode == 0 || publicChatMode == 3 || publicChatMode == 1 && isFriendOrSelf(((Player)obj).name)))
			{
				npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height);
				if(spriteDrawX > -1 && anInt974 < anInt975)
				{
					anIntArray979[anInt974] = chatTextDrawingArea.method384(((Entity) (obj)).textSpoken) / 2;
					anIntArray978[anInt974] = chatTextDrawingArea.anInt1497;
					anIntArray976[anInt974] = spriteDrawX;
					anIntArray977[anInt974] = spriteDrawY;
					anIntArray980[anInt974] = ((Entity) (obj)).anInt1513;
					anIntArray981[anInt974] = ((Entity) (obj)).anInt1531;
					anIntArray982[anInt974] = ((Entity) (obj)).textCycle;
					aStringArray983[anInt974++] = ((Entity) (obj)).textSpoken;
					if(anInt1249 == 0 && ((Entity) (obj)).anInt1531 >= 1 && ((Entity) (obj)).anInt1531 <= 3)
					{
						anIntArray978[anInt974] += 10;
						anIntArray977[anInt974] += 5;
					}
					if(anInt1249 == 0 && ((Entity) (obj)).anInt1531 == 4)
						anIntArray979[anInt974] = 60;
					if(anInt1249 == 0 && ((Entity) (obj)).anInt1531 == 5)
						anIntArray978[anInt974] += 5;
				}
			}
			if(((Entity) (obj)).loopCycleStatus > loopCycle)
			{
				try{
					npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height + 15);
					if(spriteDrawX > -1)
					{
						int i1 = (((Entity) (obj)).currentHealth * 30) / ((Entity) (obj)).maxHealth;
						if(i1 > 30)
							i1 = 30;
						DrawingArea.drawPixels(5, spriteDrawY - 3, spriteDrawX - 15, 65280, i1);
						DrawingArea.drawPixels(5, spriteDrawY - 3, (spriteDrawX - 15) + i1, 0xff0000, 30 - i1);
					}
				}catch(Exception e){ }
				}
				for(int j1 = 0; j1 < 4; j1++)
					if(((Entity) (obj)).hitsLoopCycle[j1] > loopCycle)
					{
						npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height / 2);
						if(spriteDrawX > -1)
						{
							if(j1 == 1)
								spriteDrawY -= 20;
							if(j1 == 2)
							{
								spriteDrawX -= 15;
								spriteDrawY -= 10;
							}
							if(j1 == 3)
							{
								spriteDrawX += 15;
								spriteDrawY -= 10;
							}
							hitMarks[((Entity) (obj)).hitMarkTypes[j1]].drawSprite(spriteDrawX - 12, spriteDrawY - 12);
							smallText.drawText(0, String.valueOf(((Entity) (obj)).hitArray[j1]), spriteDrawY + 4, spriteDrawX);
							smallText.drawText(0xffffff, String.valueOf(((Entity) (obj)).hitArray[j1]), spriteDrawY + 3, spriteDrawX - 1);
						}
					}
			}
			for(int k = 0; k < anInt974; k++) {
				int k1 = anIntArray976[k];
				int l1 = anIntArray977[k];
				int j2 = anIntArray979[k];
				int k2 = anIntArray978[k];
				boolean flag = true;
				while(flag) 
				{
					flag = false;
					for(int l2 = 0; l2 < k; l2++)
						if(l1 + 2 > anIntArray977[l2] - anIntArray978[l2] && l1 - k2 < anIntArray977[l2] + 2 && k1 - j2 < anIntArray976[l2] + anIntArray979[l2] && k1 + j2 > anIntArray976[l2] - anIntArray979[l2] && anIntArray977[l2] - anIntArray978[l2] < l1)
						{
							l1 = anIntArray977[l2] - anIntArray978[l2];
							flag = true;
						}

				}
				spriteDrawX = anIntArray976[k];
				spriteDrawY = anIntArray977[k] = l1;
				String s = aStringArray983[k];
				if(anInt1249 == 0)
				{
					int i3 = 0xffff00;
					if(anIntArray980[k] < 6)
						i3 = anIntArray965[anIntArray980[k]];
					if(anIntArray980[k] == 6)
						i3 = anInt1265 % 20 >= 10 ? 0xffff00 : 0xff0000;
					if(anIntArray980[k] == 7)
						i3 = anInt1265 % 20 >= 10 ? 65535 : 255;
					if(anIntArray980[k] == 8)
						i3 = anInt1265 % 20 >= 10 ? 0x80ff80 : 45056;
					if(anIntArray980[k] == 9) {
						int j3 = 150 - anIntArray982[k];
						if(j3 < 50)
							i3 = 0xff0000 + 1280 * j3;
						else
						if(j3 < 100)
							i3 = 0xffff00 - 0x50000 * (j3 - 50);
						else
						if(j3 < 150)
							i3 = 65280 + 5 * (j3 - 100);
					}
					if(anIntArray980[k] == 10) {
						int k3 = 150 - anIntArray982[k];
						if(k3 < 50)
							i3 = 0xff0000 + 5 * k3;
						else
						if(k3 < 100)
							i3 = 0xff00ff - 0x50000 * (k3 - 50);
						else
						if(k3 < 150)
							i3 = (255 + 0x50000 * (k3 - 100)) - 5 * (k3 - 100);
					}
					if(anIntArray980[k] == 11) {
						int l3 = 150 - anIntArray982[k];
						if(l3 < 50)
							i3 = 0xffffff - 0x50005 * l3;
						else
						if(l3 < 100)
							i3 = 65280 + 0x50005 * (l3 - 50);
						else
						if(l3 < 150)
							i3 = 0xffffff - 0x50000 * (l3 - 100);
					}
					if(anIntArray981[k] == 0) {
						chatTextDrawingArea.drawText(0, s, spriteDrawY + 1, spriteDrawX);
						chatTextDrawingArea.drawText(i3, s, spriteDrawY, spriteDrawX);
					}
					if(anIntArray981[k] == 1) {
						chatTextDrawingArea.method386(0, s, spriteDrawX, anInt1265, spriteDrawY + 1);
						chatTextDrawingArea.method386(i3, s, spriteDrawX, anInt1265, spriteDrawY);
					}
					if(anIntArray981[k] == 2) {
						chatTextDrawingArea.method387(spriteDrawX, s, anInt1265, spriteDrawY + 1, 0);
						chatTextDrawingArea.method387(spriteDrawX, s, anInt1265, spriteDrawY, i3);
					}
					if(anIntArray981[k] == 3) {
						chatTextDrawingArea.method388(150 - anIntArray982[k], s, anInt1265, spriteDrawY + 1, spriteDrawX, 0);
						chatTextDrawingArea.method388(150 - anIntArray982[k], s, anInt1265, spriteDrawY, spriteDrawX, i3);
					}
					if(anIntArray981[k] == 4) {
						int i4 = chatTextDrawingArea.method384(s);
						int k4 = ((150 - anIntArray982[k]) * (i4 + 100)) / 150;
						DrawingArea.setDrawingArea(334, spriteDrawX - 50, spriteDrawX + 50, 0);
						chatTextDrawingArea.method385(0, s, spriteDrawY + 1, (spriteDrawX + 50) - k4);
						chatTextDrawingArea.method385(i3, s, spriteDrawY, (spriteDrawX + 50) - k4);
						DrawingArea.defaultDrawingAreaSize();
					}
					if(anIntArray981[k] == 5) {
						int j4 = 150 - anIntArray982[k];
						int l4 = 0;
						if(j4 < 25)
							l4 = j4 - 25;
						else
						if(j4 > 125)
							l4 = j4 - 125;
						DrawingArea.setDrawingArea(spriteDrawY + 5, 0, 512, spriteDrawY - chatTextDrawingArea.anInt1497 - 1);
						chatTextDrawingArea.drawText(0, s, spriteDrawY + 1 + l4, spriteDrawX);
						chatTextDrawingArea.drawText(i3, s, spriteDrawY + l4, spriteDrawX);
						DrawingArea.defaultDrawingAreaSize();
					}
				} else {
					chatTextDrawingArea.drawText(0, s, spriteDrawY + 1, spriteDrawX);
					chatTextDrawingArea.drawText(0xffff00, s, spriteDrawY, spriteDrawX);
				}
			}
		} catch(Exception e){ }
	}

	private void delFriend(long l)
	{
		try
		{
			if(l == 0L)
				return;
			for(int i = 0; i < friendsCount; i++)
			{
				if(friendsListAsLongs[i] != l)
					continue;
				friendsCount--;
				needDrawTabArea = true;
				for(int j = i; j < friendsCount; j++)
				{
					friendsList[j] = friendsList[j + 1];
					friendsNodeIDs[j] = friendsNodeIDs[j + 1];
					friendsListAsLongs[j] = friendsListAsLongs[j + 1];
				}

				stream.createFrame(215);
				stream.writeQWord(l);
				break;
			}
		}
		catch(RuntimeException runtimeexception)
		{
			signlink.reporterror("18622, " + false + ", " + l + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public void drawSideIcons(){
		/* Top sideIcons */
		if(tabInterfaceIDs[0] != -1)//attack
			newSideIcons[0].drawSprite(30, 10);
		if(tabInterfaceIDs[1] != -1)//stat
			newSideIcons[1].drawSprite(53, 6);
		if(tabInterfaceIDs[2] != -1)//quest
			newSideIcons[2].drawSprite(83, 7);
		if(tabInterfaceIDs[3] != -1)//inventory
			newSideIcons[3].drawSprite(112, 4);
		if(tabInterfaceIDs[4] != -1)//equipment
			newSideIcons[4].drawSprite(153, 6);
		if(tabInterfaceIDs[5] != -1)//prayer
			newSideIcons[5].drawSprite(180, 5);
		if(tabInterfaceIDs[6] != -1)//magic
			newSideIcons[6].drawSprite(208, 8);
		/* Bottom sideIcons */
	//	if(tabInterfaceIDs[7] != -1)//clan
	//		sideIcons[7].drawSprite(53, 302);
		if(tabInterfaceIDs[8] != -1)//friends
			newSideIcons[7].drawSprite(54, 303);
		if(tabInterfaceIDs[9] != -1)//ignore
			newSideIcons[8].drawSprite(82, 304);
		if(tabInterfaceIDs[10] != -1)//logout
			newSideIcons[9].drawSprite(116, 305);
		if(tabInterfaceIDs[11] != -1)//options
			newSideIcons[10].drawSprite(153, 302);
		if(tabInterfaceIDs[12] != -1)//emotes
			newSideIcons[11].drawSprite(185, 303);
		if(tabInterfaceIDs[13] != -1)//music
			newSideIcons[12].drawSprite(209, 303);
	}

	public void drawRedStones() {
		if(tabInterfaceIDs[tabID] != -1) {
			switch(tabID) {
				case 0:
					redStones[0].drawSprite(19, 0);
					break;
				case 1:
					redStones[5].drawSprite(51, 1);
					break;
				case 2:
					redStones[5].drawSprite(79, 1);
					break;
				case 3:
					redStones[2].drawSprite(107, 0);
					break;
				case 4:
					redStones[7].drawSprite(150, 1);
					break;
				case 5:
					redStones[7].drawSprite(178, 1);
					break;
				case 6:
					redStones[1].drawSprite(206, 2);
					break;
				case 7:
					redStones[3].drawSprite(19, 297);
					break;
				case 8:
					redStones[8].drawSprite(51, 297);
					break;
				case 9:
					redStones[8].drawSprite(79, 297);
					break;
				case 10:
					redStones[6].drawSprite(107, 299);
					break;
				case 11:
					redStones[9].drawSprite(150, 297);
					break;
				case 12:
					redStones[9].drawSprite(178, 297);
					break;
				case 13:
					redStones[4].drawSprite(206, 297);
					break;
			}
		}
	}
	
	private void drawTabArea()
	{
		aRSImageProducer_1163.initDrawingArea();
		Texture.anIntArray1472 = anIntArray1181;
		/* Draws new tab area sprite */
		tabArea.drawSprite(0, 0);
		if(invOverlayInterfaceID == -1)
		{
			drawRedStones();
			drawSideIcons();
		}
		if(invOverlayInterfaceID != -1)
			drawInterface(0, 38, RSInterface.interfaceCache[invOverlayInterfaceID], 37);
		else
		if(tabInterfaceIDs[tabID] != -1)
			drawInterface(0, 38, RSInterface.interfaceCache[tabInterfaceIDs[tabID]], 37);
		if(menuOpen && menuScreenArea == 1)
			drawMenu();
		aRSImageProducer_1163.drawGraphics(168, super.graphics, 519);
		aRSImageProducer_1165.initDrawingArea();
		Texture.anIntArray1472 = anIntArray1182;
	}

	private void method37(int j)
	{
		if(!lowMem)
		{
			if(Texture.anIntArray1480[17] >= j)
			{
				Background background = Texture.aBackgroundArray1474s[17];
				int k = background.anInt1452 * background.anInt1453 - 1;
				int j1 = background.anInt1452 * anInt945 * 2;
				byte abyte0[] = background.aByteArray1450;
				byte abyte3[] = aByteArray912;
				for(int i2 = 0; i2 <= k; i2++)
					abyte3[i2] = abyte0[i2 - j1 & k];

				background.aByteArray1450 = abyte3;
				aByteArray912 = abyte0;
				Texture.method370(17);
				anInt854++;
				if(anInt854 > 1235)
				{
					anInt854 = 0;
					stream.createFrame(226);
					stream.writeWordBigEndian(0);
					int l2 = stream.currentOffset;
					stream.writeWord(58722);
					stream.writeWordBigEndian(240);
					stream.writeWord((int)(Math.random() * 65536D));
					stream.writeWordBigEndian((int)(Math.random() * 256D));
					if((int)(Math.random() * 2D) == 0)
						stream.writeWord(51825);
					stream.writeWordBigEndian((int)(Math.random() * 256D));
					stream.writeWord((int)(Math.random() * 65536D));
					stream.writeWord(7130);
					stream.writeWord((int)(Math.random() * 65536D));
					stream.writeWord(61657);
					stream.writeBytes(stream.currentOffset - l2);
				}
			}
			if(Texture.anIntArray1480[24] >= j)
			{
				Background background_1 = Texture.aBackgroundArray1474s[24];
				int l = background_1.anInt1452 * background_1.anInt1453 - 1;
				int k1 = background_1.anInt1452 * anInt945 * 2;
				byte abyte1[] = background_1.aByteArray1450;
				byte abyte4[] = aByteArray912;
				for(int j2 = 0; j2 <= l; j2++)
					abyte4[j2] = abyte1[j2 - k1 & l];

				background_1.aByteArray1450 = abyte4;
				aByteArray912 = abyte1;
				Texture.method370(24);
			}
			if(Texture.anIntArray1480[40] >= j)
			{
				Background background_2 = Texture.aBackgroundArray1474s[40];
				int i1 = background_2.anInt1452 * background_2.anInt1453 - 1;
				int l1 = background_2.anInt1452 * anInt945 * 2;
				byte abyte2[] = background_2.aByteArray1450;
				byte abyte5[] = aByteArray912;
				for(int k2 = 0; k2 <= i1; k2++)
					abyte5[k2] = abyte2[k2 - l1 & i1];

				background_2.aByteArray1450 = abyte5;
				aByteArray912 = abyte2;
				Texture.method370(40);
			}
		}
	}

	private void method38()
	{
		for(int i = -1; i < playerCount; i++)
		{
			int j;
			if(i == -1)
				j = myPlayerIndex;
			else
				j = playerIndices[i];
			Player player = playerArray[j];
			if(player != null && player.textCycle > 0)
			{
				player.textCycle--;
				if(player.textCycle == 0)
					player.textSpoken = null;
			}
		}

		for(int k = 0; k < npcCount; k++)
		{
			int l = npcIndices[k];
			NPC npc = npcArray[l];
			if(npc != null && npc.textCycle > 0)
			{
				npc.textCycle--;
				if(npc.textCycle == 0)
					npc.textSpoken = null;
			}
		}

	}

	private void calcCameraPos()
	{
		int i = anInt1098 * 128 + 64;
		int j = anInt1099 * 128 + 64;
		int k = method42(plane, j, i) - anInt1100;
		if(xCameraPos < i)
		{
			xCameraPos += anInt1101 + ((i - xCameraPos) * anInt1102) / 1000;
			if(xCameraPos > i)
				xCameraPos = i;
		}
		if(xCameraPos > i)
		{
			xCameraPos -= anInt1101 + ((xCameraPos - i) * anInt1102) / 1000;
			if(xCameraPos < i)
				xCameraPos = i;
		}
		if(zCameraPos < k)
		{
			zCameraPos += anInt1101 + ((k - zCameraPos) * anInt1102) / 1000;
			if(zCameraPos > k)
				zCameraPos = k;
		}
		if(zCameraPos > k)
		{
			zCameraPos -= anInt1101 + ((zCameraPos - k) * anInt1102) / 1000;
			if(zCameraPos < k)
				zCameraPos = k;
		}
		if(yCameraPos < j)
		{
			yCameraPos += anInt1101 + ((j - yCameraPos) * anInt1102) / 1000;
			if(yCameraPos > j)
				yCameraPos = j;
		}
		if(yCameraPos > j)
		{
			yCameraPos -= anInt1101 + ((yCameraPos - j) * anInt1102) / 1000;
			if(yCameraPos < j)
				yCameraPos = j;
		}
		i = anInt995 * 128 + 64;
		j = anInt996 * 128 + 64;
		k = method42(plane, j, i) - anInt997;
		int l = i - xCameraPos;
		int i1 = k - zCameraPos;
		int j1 = j - yCameraPos;
		int k1 = (int)Math.sqrt(l * l + j1 * j1);
		int l1 = (int)(Math.atan2(i1, k1) * 325.94900000000001D) & 0x7ff;
		int i2 = (int)(Math.atan2(l, j1) * -325.94900000000001D) & 0x7ff;
		if(l1 < 128)
			l1 = 128;
		if(l1 > 383)
			l1 = 383;
		if(yCameraCurve < l1)
		{
			yCameraCurve += anInt998 + ((l1 - yCameraCurve) * anInt999) / 1000;
			if(yCameraCurve > l1)
				yCameraCurve = l1;
		}
		if(yCameraCurve > l1)
		{
			yCameraCurve -= anInt998 + ((yCameraCurve - l1) * anInt999) / 1000;
			if(yCameraCurve < l1)
				yCameraCurve = l1;
		}
		int j2 = i2 - xCameraCurve;
		if(j2 > 1024)
			j2 -= 2048;
		if(j2 < -1024)
			j2 += 2048;
		if(j2 > 0)
		{
			xCameraCurve += anInt998 + (j2 * anInt999) / 1000;
			xCameraCurve &= 0x7ff;
		}
		if(j2 < 0)
		{
			xCameraCurve -= anInt998 + (-j2 * anInt999) / 1000;
			xCameraCurve &= 0x7ff;
		}
		int k2 = i2 - xCameraCurve;
		if(k2 > 1024)
			k2 -= 2048;
		if(k2 < -1024)
			k2 += 2048;
		if(k2 < 0 && j2 > 0 || k2 > 0 && j2 < 0)
			xCameraCurve = i2;
	}

	/*private void drawMenu()
	{
		int i = menuOffsetX;
		int j = menuOffsetY;
		int k = menuWidth;
		int l = anInt952;
		int i1 = 0x5d5447;
		DrawingArea.drawPixels(l, j, i, i1, k);
		DrawingArea.drawPixels(16, j + 1, i + 1, 0, k - 2);
		DrawingArea.fillPixels(i + 1, k - 2, l - 19, 0, j + 18);
		chatTextDrawingArea.method385(i1, "Choose Option", j + 14, i + 3);
		int j1 = super.mouseX;
		int k1 = super.mouseY;
		if(menuScreenArea == 0) {
			j1 -= 4;
			k1 -= 4;
		}
		if(menuScreenArea == 1) {
			j1 -= 519;
			k1 -= 168;
		}
		if(menuScreenArea == 2) {
			j1 -= 17;
			k1 -= 338;
		}
		if(menuScreenArea == 3) {
			j1 -= 519;
			k1 -= 0;
		}
		for(int l1 = 0; l1 < menuActionRow; l1++)
		{
			int i2 = j + 31 + (menuActionRow - 1 - l1) * 15;
			int j2 = 0xffffff;
			if(j1 > i && j1 < i + k && k1 > i2 - 13 && k1 < i2 + 3)
				j2 = 0xffff00;
			chatTextDrawingArea.method389(true, i + 3, j2, menuActionName[l1], i2);
		}

	}*/
	
	private void drawMenu() {
		int i = menuOffsetX;
		int j = menuOffsetY;
		int k = menuWidth;
		int l = menuHeight + 1;//anInt952
		//DrawingArea.drawPixels(height, yPos, xPos, color, width);
		//DrawingArea.fillPixels(xPos, width, height, color, yPos);
		DrawingArea.drawPixels(l - 4, j + 2, i, 0x706a5e, k);
		DrawingArea.drawPixels(l - 2, j + 1, i + 1, 0x706a5e, k - 2);
		DrawingArea.drawPixels(l, j, i + 2, 0x706a5e, k - 4);
		DrawingArea.drawPixels(l - 2, j + 1, i + 3, 0x2d2822, k - 6);
		DrawingArea.drawPixels(l - 4, j + 2, i + 2, 0x2d2822, k - 4);
		DrawingArea.drawPixels(l - 6, j + 3, i + 1, 0x2d2822, k - 2);
		DrawingArea.drawPixels(l - 22, j + 19, i + 2, 0x524a3d, k - 4);
		DrawingArea.drawPixels(l - 22, j + 20, i + 3, 0x524a3d, k - 6);
		DrawingArea.drawPixels(l - 23, j + 20, i + 3, 0x2b271c, k - 6);
		DrawingArea.fillPixels(i + 3, k - 6, 1, 0x2a291b, j + 2);
		DrawingArea.fillPixels(i + 2, k - 4, 1, 0x2a261b, j + 3);
		DrawingArea.fillPixels(i + 2, k - 4, 1, 0x252116, j + 4);
		DrawingArea.fillPixels(i + 2, k - 4, 1, 0x211e15, j + 5);
		DrawingArea.fillPixels(i + 2, k - 4, 1, 0x1e1b12, j + 6);
		DrawingArea.fillPixels(i + 2, k - 4, 1, 0x1a170e, j + 7);
		DrawingArea.fillPixels(i + 2, k - 4, 2, 0x15120b, j + 8);
		DrawingArea.fillPixels(i + 2, k - 4, 1, 0x100d08, j + 10);
		DrawingArea.fillPixels(i + 2, k - 4, 1, 0x090a04, j + 11);
		DrawingArea.fillPixels(i + 2, k - 4, 1, 0x080703, j + 12);
		DrawingArea.fillPixels(i + 2, k - 4, 1, 0x090a04, j + 13);
		DrawingArea.fillPixels(i + 2, k - 4, 1, 0x070802, j + 14);
		DrawingArea.fillPixels(i + 2, k - 4, 1, 0x090a04, j + 15);
		DrawingArea.fillPixels(i + 2, k - 4, 1, 0x070802, j + 16);
		DrawingArea.fillPixels(i + 2, k - 4, 1, 0x090a04, j + 17);
		DrawingArea.fillPixels(i + 2, k - 4, 1, 0x2a291b, j + 18);
		DrawingArea.fillPixels(i + 3, k - 6, 1, 0x564943, j + 19);
		chatTextDrawingArea.method385(0xc6b895, "Choose Option", j + 14, i + 3);
		int j1 = super.mouseX;
		int k1 = super.mouseY;
		if(menuScreenArea == 0) {
			j1 -= 4;
			k1 -= 4;
		}
		if(menuScreenArea == 1) {
			j1 -= 519;
			k1 -= 168;
		}
		if(menuScreenArea == 2) {
			j1 -= 17;
			k1 -= 338;
		}
		if(menuScreenArea == 3) {
			j1 -= 516;
			k1 -= 0;
		}
		for(int l1 = 0; l1 < menuActionRow; l1++) {
			int i2 = j + 31 + (menuActionRow - 1 - l1) * 15;
			int j2 = 0xc6b895;
			if (j1 > i && j1 < i + k && k1 > i2 - 13 && k1 < i2 + 3) {
				DrawingArea.drawPixels(15, i2 - 11, i + 3, 0x6f695d, menuWidth - 6);
				j2 = 0xeee5c6;
			}
			chatTextDrawingArea.method389(true, i + 4, j2, menuActionName[l1], i2 + 1);
		}
	}

	private void addFriend(long l)
	{
		try
		{
			if(l == 0L)
				return;
			if(friendsCount >= 100 && anInt1046 != 1)
			{
				pushMessage("Your friendlist is full. Max of 100 for free users, and 200 for members", 0, "");
				return;
			}
			if(friendsCount >= 200)
			{
				pushMessage("Your friendlist is full. Max of 100 for free users, and 200 for members", 0, "");
				return;
			}
			String s = TextClass.fixName(TextClass.nameForLong(l));
			for(int i = 0; i < friendsCount; i++)
				if(friendsListAsLongs[i] == l)
				{
					pushMessage(s + " is already on your friend list", 0, "");
					return;
				}
			for(int j = 0; j < ignoreCount; j++)
				if(ignoreListAsLongs[j] == l)
				{
					pushMessage("Please remove " + s + " from your ignore list first", 0, "");
					return;
				}

			if(s.equals(myPlayer.name))
			{
				return;
			} else
			{
				friendsList[friendsCount] = s;
				friendsListAsLongs[friendsCount] = l;
				friendsNodeIDs[friendsCount] = 0;
				friendsCount++;
				needDrawTabArea = true;
				stream.createFrame(188);
				stream.writeQWord(l);
				return;
			}
		}
		catch(RuntimeException runtimeexception)
		{
			signlink.reporterror("15283, " + (byte)68 + ", " + l + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	private int method42(int i, int j, int k)
	{
		int l = k >> 7;
		int i1 = j >> 7;
		if(l < 0 || i1 < 0 || l > 103 || i1 > 103)
			return 0;
		int j1 = i;
		if(j1 < 3 && (byteGroundArray[1][l][i1] & 2) == 2)
			j1++;
		int k1 = k & 0x7f;
		int l1 = j & 0x7f;
		int i2 = intGroundArray[j1][l][i1] * (128 - k1) + intGroundArray[j1][l + 1][i1] * k1 >> 7;
		int j2 = intGroundArray[j1][l][i1 + 1] * (128 - k1) + intGroundArray[j1][l + 1][i1 + 1] * k1 >> 7;
		return i2 * (128 - l1) + j2 * l1 >> 7;
	}

	private static String intToKOrMil(int j)
	{
		if(j < 0x186a0)
			return String.valueOf(j);
		if(j < 0x989680)
			return j / 1000 + "K";
		else
			return j / 0xf4240 + "M";
	}

	private void resetLogout()
	{
		try
		{
			if(socketStream != null)
				socketStream.close();
		}
		catch(Exception _ex) { }
		socketStream = null;
		loggedIn = false;
		loginScreenState = 0;
 	    //myUsername = "";
 	    //myPassword = "";
		unlinkMRUNodes();
		worldController.initToNull();
		for(int i = 0; i < 4; i++)
			aClass11Array1230[i].method210();

		System.gc();
		stopMidi();
		currentSong = -1;
		nextSong = 0;
		prevSong = 0;
		musicVolume = 200;
		onDemandFetcher.method558(2, nextSong);
	}

	private void method45()
	{
		aBoolean1031 = true;
		for(int j = 0; j < 7; j++)
		{
			anIntArray1065[j] = -1;
			for(int k = 0; k < IDK.length; k++)
			{
				if(IDK.cache[k].aBoolean662 || IDK.cache[k].anInt657 != j + (aBoolean1047 ? 0 : 7))
					continue;
				anIntArray1065[j] = k;
				break;
			}

		}

	}

	private void method46(int i, Stream stream)
	{
		while(stream.bitPosition + 21 < i * 8)
		{
			int k = stream.readBits(14);
			if(k == 16383)
				break;
			if(npcArray[k] == null)
				npcArray[k] = new NPC();
			NPC npc = npcArray[k];
			npcIndices[npcCount++] = k;
			npc.anInt1537 = loopCycle;
			int l = stream.readBits(5);
			if(l > 15)
				l -= 32;
			int i1 = stream.readBits(5);
			if(i1 > 15)
				i1 -= 32;
			int j1 = stream.readBits(1);
			npc.desc = EntityDef.forID(stream.readBits(14));
			int k1 = stream.readBits(1);
			if(k1 == 1)
				anIntArray894[anInt893++] = k;
			npc.anInt1540 = npc.desc.aByte68;
			npc.anInt1504 = npc.desc.anInt79;
			npc.anInt1554 = npc.desc.walkAnim;
			npc.anInt1555 = npc.desc.anInt58;
			npc.anInt1556 = npc.desc.anInt83;
			npc.anInt1557 = npc.desc.anInt55;
			npc.anInt1511 = npc.desc.standAnim;
			npc.setPos(myPlayer.smallX[0] + i1, myPlayer.smallY[0] + l, j1 == 1);
		}
		stream.finishBitAccess();
	}

	public void processGameLoop()
	{
		if(rsAlreadyLoaded || loadingError || genericLoadingError)
			return;
		loopCycle++;
		if(!loggedIn)
			processLoginScreenInput();
		else
			mainGameProcessor();
		processOnDemandQueue();
	}

	private void method47(boolean flag)
	{
		if(myPlayer.x >> 7 == destX && myPlayer.y >> 7 == destY)
			destX = 0;
		int j = playerCount;
		if(flag)
			j = 1;
		for(int l = 0; l < j; l++)
		{
			Player player;
			int i1;
			if(flag)
			{
				player = myPlayer;
				i1 = myPlayerIndex << 14;
			} else
			{
				player = playerArray[playerIndices[l]];
				i1 = playerIndices[l] << 14;
			}
			if(player == null || !player.isVisible())
				continue;
			player.aBoolean1699 = (lowMem && playerCount > 50 || playerCount > 200) && !flag && player.anInt1517 == player.anInt1511;
			int j1 = player.x >> 7;
			int k1 = player.y >> 7;
			if(j1 < 0 || j1 >= 104 || k1 < 0 || k1 >= 104)
				continue;
			if(player.aModel_1714 != null && loopCycle >= player.anInt1707 && loopCycle < player.anInt1708)
			{
				player.aBoolean1699 = false;
				player.anInt1709 = method42(plane, player.y, player.x);
				worldController.method286(plane, player.y, player, player.anInt1552, player.anInt1722, player.x, player.anInt1709, player.anInt1719, player.anInt1721, i1, player.anInt1720);
				continue;
			}
			if((player.x & 0x7f) == 64 && (player.y & 0x7f) == 64)
			{
				if(anIntArrayArray929[j1][k1] == anInt1265)
					continue;
				anIntArrayArray929[j1][k1] = anInt1265;
			}
			player.anInt1709 = method42(plane, player.y, player.x);
			worldController.method285(plane, player.anInt1552, player.anInt1709, i1, player.y, 60, player.x, player, player.aBoolean1541);
		}

	}

	private boolean promptUserForInput(RSInterface class9)
	{
		int j = class9.contentType;
		if(anInt900 == 2)
		{
			if(j == 201)
			{
				inputTaken = true;
				inputDialogState = 0;
				messagePromptRaised = true;
				promptInput = "";
				friendsListAction = 1;
				aString1121 = "Enter name of friend to add to list";
			}
			if(j == 202)
			{
				inputTaken = true;
				inputDialogState = 0;
				messagePromptRaised = true;
				promptInput = "";
				friendsListAction = 2;
				aString1121 = "Enter name of friend to delete from list";
			}
		}
		if(j == 205)
		{
			anInt1011 = 250;
			return true;
		}
		if(j == 501)
		{
			inputTaken = true;
			inputDialogState = 0;
			messagePromptRaised = true;
			promptInput = "";
			friendsListAction = 4;
			aString1121 = "Enter name of player to add to list";
		}
		if(j == 502)
		{
			inputTaken = true;
			inputDialogState = 0;
			messagePromptRaised = true;
			promptInput = "";
			friendsListAction = 5;
			aString1121 = "Enter name of player to delete from list";
		}
		if(j >= 300 && j <= 313)
		{
			int k = (j - 300) / 2;
			int j1 = j & 1;
			int i2 = anIntArray1065[k];
			if(i2 != -1)
			{
				do
				{
					if(j1 == 0 && --i2 < 0)
						i2 = IDK.length - 1;
					if(j1 == 1 && ++i2 >= IDK.length)
						i2 = 0;
				} while(IDK.cache[i2].aBoolean662 || IDK.cache[i2].anInt657 != k + (aBoolean1047 ? 0 : 7));
				anIntArray1065[k] = i2;
				aBoolean1031 = true;
			}
		}
		if(j >= 314 && j <= 323)
		{
			int l = (j - 314) / 2;
			int k1 = j & 1;
			int j2 = anIntArray990[l];
			if(k1 == 0 && --j2 < 0)
				j2 = anIntArrayArray1003[l].length - 1;
			if(k1 == 1 && ++j2 >= anIntArrayArray1003[l].length)
				j2 = 0;
			anIntArray990[l] = j2;
			aBoolean1031 = true;
		}
		if(j == 324 && !aBoolean1047)
		{
			aBoolean1047 = true;
			method45();
		}
		if(j == 325 && aBoolean1047)
		{
			aBoolean1047 = false;
			method45();
		}
		if(j == 326)
		{
			stream.createFrame(101);
			stream.writeWordBigEndian(aBoolean1047 ? 0 : 1);
			for(int i1 = 0; i1 < 7; i1++)
				stream.writeWordBigEndian(anIntArray1065[i1]);

			for(int l1 = 0; l1 < 5; l1++)
				stream.writeWordBigEndian(anIntArray990[l1]);

			return true;
		}
		if(j == 613)
			canMute = !canMute;
		if(j >= 601 && j <= 612)
		{
			clearTopInterfaces();
			if(reportAbuseInput.length() > 0)
			{
				stream.createFrame(218);
				stream.writeQWord(TextClass.longForName(reportAbuseInput));
				stream.writeWordBigEndian(j - 601);
				stream.writeWordBigEndian(canMute ? 1 : 0);
			}
		}
		return false;
	}

	private void method49(Stream stream)
	{
		for(int j = 0; j < anInt893; j++)
		{
			int k = anIntArray894[j];
			Player player = playerArray[k];
			int l = stream.readUnsignedByte();
			if((l & 0x40) != 0)
				l += stream.readUnsignedByte() << 8;
			method107(l, k, stream, player);
		}

	}

	private void method50(int i, int k, int l, int i1, int j1)
	{
		int k1 = worldController.method300(j1, l, i);
		if(k1 != 0)
		{
			int l1 = worldController.method304(j1, l, i, k1);
			int k2 = l1 >> 6 & 3;
			int i3 = l1 & 0x1f;
			int k3 = k;
			if(k1 > 0)
				k3 = i1;
			int ai[] = aClass30_Sub2_Sub1_Sub1_1263.myPixels;
			int k4 = 24624 + l * 4 + (103 - i) * 512 * 4;
			int i5 = k1 >> 14 & 0x7fff;
			ObjectDef class46_2 = ObjectDef.forID(i5);
			if(class46_2.anInt758 != -1)
			{
				Background background_2 = mapScenes[class46_2.anInt758];
				if(background_2 != null)
				{
					int i6 = (class46_2.anInt744 * 4 - background_2.anInt1452) / 2;
					int j6 = (class46_2.anInt761 * 4 - background_2.anInt1453) / 2;
					background_2.method361(48 + l * 4 + i6, 48 + (104 - i - class46_2.anInt761) * 4 + j6);
				}
			} else
			{
				if(i3 == 0 || i3 == 2)
					if(k2 == 0)
					{
						ai[k4] = k3;
						ai[k4 + 512] = k3;
						ai[k4 + 1024] = k3;
						ai[k4 + 1536] = k3;
					} else
					if(k2 == 1)
					{
						ai[k4] = k3;
						ai[k4 + 1] = k3;
						ai[k4 + 2] = k3;
						ai[k4 + 3] = k3;
					} else
					if(k2 == 2)
					{
						ai[k4 + 3] = k3;
						ai[k4 + 3 + 512] = k3;
						ai[k4 + 3 + 1024] = k3;
						ai[k4 + 3 + 1536] = k3;
					} else
					if(k2 == 3)
					{
						ai[k4 + 1536] = k3;
						ai[k4 + 1536 + 1] = k3;
						ai[k4 + 1536 + 2] = k3;
						ai[k4 + 1536 + 3] = k3;
					}
				if(i3 == 3)
					if(k2 == 0)
						ai[k4] = k3;
					else
					if(k2 == 1)
						ai[k4 + 3] = k3;
					else
					if(k2 == 2)
						ai[k4 + 3 + 1536] = k3;
					else
					if(k2 == 3)
						ai[k4 + 1536] = k3;
				if(i3 == 2)
					if(k2 == 3)
					{
						ai[k4] = k3;
						ai[k4 + 512] = k3;
						ai[k4 + 1024] = k3;
						ai[k4 + 1536] = k3;
					} else
					if(k2 == 0)
					{
						ai[k4] = k3;
						ai[k4 + 1] = k3;
						ai[k4 + 2] = k3;
						ai[k4 + 3] = k3;
					} else
					if(k2 == 1)
					{
						ai[k4 + 3] = k3;
						ai[k4 + 3 + 512] = k3;
						ai[k4 + 3 + 1024] = k3;
						ai[k4 + 3 + 1536] = k3;
					} else
					if(k2 == 2)
					{
						ai[k4 + 1536] = k3;
						ai[k4 + 1536 + 1] = k3;
						ai[k4 + 1536 + 2] = k3;
						ai[k4 + 1536 + 3] = k3;
					}
			}
		}
		k1 = worldController.method302(j1, l, i);
		if(k1 != 0)
		{
			int i2 = worldController.method304(j1, l, i, k1);
			int l2 = i2 >> 6 & 3;
			int j3 = i2 & 0x1f;
			int l3 = k1 >> 14 & 0x7fff;
			ObjectDef class46_1 = ObjectDef.forID(l3);
			if(class46_1.anInt758 != -1)
			{
				Background background_1 = mapScenes[class46_1.anInt758];
				if(background_1 != null)
				{
					int j5 = (class46_1.anInt744 * 4 - background_1.anInt1452) / 2;
					int k5 = (class46_1.anInt761 * 4 - background_1.anInt1453) / 2;
					background_1.method361(48 + l * 4 + j5, 48 + (104 - i - class46_1.anInt761) * 4 + k5);
				}
			} else
			if(j3 == 9)
			{
				int l4 = 0xeeeeee;
				if(k1 > 0)
					l4 = 0xee0000;
				int ai1[] = aClass30_Sub2_Sub1_Sub1_1263.myPixels;
				int l5 = 24624 + l * 4 + (103 - i) * 512 * 4;
				if(l2 == 0 || l2 == 2)
				{
					ai1[l5 + 1536] = l4;
					ai1[l5 + 1024 + 1] = l4;
					ai1[l5 + 512 + 2] = l4;
					ai1[l5 + 3] = l4;
				} else
				{
					ai1[l5] = l4;
					ai1[l5 + 512 + 1] = l4;
					ai1[l5 + 1024 + 2] = l4;
					ai1[l5 + 1536 + 3] = l4;
				}
			}
		}
		k1 = worldController.method303(j1, l, i);
		if(k1 != 0)
		{
			int j2 = k1 >> 14 & 0x7fff;
			ObjectDef class46 = ObjectDef.forID(j2);
			if(class46.anInt758 != -1)
			{
				Background background = mapScenes[class46.anInt758];
				if(background != null)
				{
					int i4 = (class46.anInt744 * 4 - background.anInt1452) / 2;
					int j4 = (class46.anInt761 * 4 - background.anInt1453) / 2;
					background.method361(48 + l * 4 + i4, 48 + (104 - i - class46.anInt761) * 4 + j4);
				}
			}
		}
	}

	private void loadTitleScreen()
	{
		aBackground_966 = new Background(titleStreamLoader, "titlebox", 0);
		aBackground_967 = new Background(titleStreamLoader, "titlebutton", 0);
		aBackgroundArray1152s = new Background[12];
		int j = 0;
		try
		{
			j = Integer.parseInt(getParameter("fl_icon"));
		}
		catch(Exception _ex) { }
		if(j == 0)
		{
			for(int k = 0; k < 12; k++)
				aBackgroundArray1152s[k] = new Background(titleStreamLoader, "runes", k);

		} else
		{
			for(int l = 0; l < 12; l++)
				aBackgroundArray1152s[l] = new Background(titleStreamLoader, "runes", 12 + (l & 3));

		}
		aClass30_Sub2_Sub1_Sub1_1201 = new Sprite(128, 265);
		aClass30_Sub2_Sub1_Sub1_1202 = new Sprite(128, 265);
		System.arraycopy(aRSImageProducer_1110.anIntArray315, 0, aClass30_Sub2_Sub1_Sub1_1201.myPixels, 0, 33920);

		System.arraycopy(aRSImageProducer_1111.anIntArray315, 0, aClass30_Sub2_Sub1_Sub1_1202.myPixels, 0, 33920);

		anIntArray851 = new int[256];
		for(int k1 = 0; k1 < 64; k1++)
			anIntArray851[k1] = k1 * 0x40000;

		for(int l1 = 0; l1 < 64; l1++)
			anIntArray851[l1 + 64] = 0xff0000 + 1024 * l1;

		for(int i2 = 0; i2 < 64; i2++)
			anIntArray851[i2 + 128] = 0xffff00 + 4 * i2;

		for(int j2 = 0; j2 < 64; j2++)
			anIntArray851[j2 + 192] = 0xffffff;

		anIntArray852 = new int[256];
		for(int k2 = 0; k2 < 64; k2++)
			anIntArray852[k2] = k2 * 1024;

		for(int l2 = 0; l2 < 64; l2++)
			anIntArray852[l2 + 64] = 65280 + 4 * l2;

		for(int i3 = 0; i3 < 64; i3++)
			anIntArray852[i3 + 128] = 65535 + 0x40000 * i3;

		for(int j3 = 0; j3 < 64; j3++)
			anIntArray852[j3 + 192] = 0xffffff;

		anIntArray853 = new int[256];
		for(int k3 = 0; k3 < 64; k3++)
			anIntArray853[k3] = k3 * 4;

		for(int l3 = 0; l3 < 64; l3++)
			anIntArray853[l3 + 64] = 255 + 0x40000 * l3;

		for(int i4 = 0; i4 < 64; i4++)
			anIntArray853[i4 + 128] = 0xff00ff + 1024 * i4;

		for(int j4 = 0; j4 < 64; j4++)
			anIntArray853[j4 + 192] = 0xffffff;

		anIntArray850 = new int[256];
		anIntArray1190 = new int[32768];
		anIntArray1191 = new int[32768];
		randomizeBackground(null);
		anIntArray828 = new int[32768];
		anIntArray829 = new int[32768];
		drawLoadingText(10, "Connecting to fileserver");
		if(!aBoolean831)
		{
			drawFlames = true;
			aBoolean831 = true;
			startRunnable(this, 2);
		}
	}

	private static void setHighMem()
	{
		WorldController.lowMem = false;
		Texture.lowMem = false;
		lowMem = false;
		ObjectManager.lowMem = false;
		ObjectDef.lowMem = false;
	}

	 public static void main(String args[])
    {
        try
        {
            nodeID = 10;
            portOff = 0;
            setHighMem();
            isMembers = true;
            signlink.storeid = 32;
            signlink.startpriv(InetAddress.getLocalHost());
            instance = new Jframe(args);
        }
        catch(Exception exception) { }
    }
	 
	public static client instance;

	private void loadingStages()
	{
		if(lowMem && loadingStage == 2 && ObjectManager.anInt131 != plane)
		{
			aRSImageProducer_1165.initDrawingArea();
			aTextDrawingArea_1271.drawText(0, "Loading - please wait.", 151, 257);
			aTextDrawingArea_1271.drawText(0xffffff, "Loading - please wait.", 150, 256);
			aRSImageProducer_1165.drawGraphics(4, super.graphics, 4);
			loadingStage = 1;
			aLong824 = System.currentTimeMillis();
		}
		if(loadingStage == 1)
		{
			int j = method54();
			if(j != 0 && System.currentTimeMillis() - aLong824 > 0x57e40L)
			{
				signlink.reporterror(myUsername + " glcfb " + aLong1215 + "," + j + "," + lowMem + "," + decompressors[0] + "," + onDemandFetcher.getNodeCount() + "," + plane + "," + anInt1069 + "," + anInt1070);
				aLong824 = System.currentTimeMillis();
			}
		}
		if(loadingStage == 2 && plane != anInt985)
		{
			anInt985 = plane;
			method24(plane);
		}
	}

	private int method54()
	{
		for(int i = 0; i < aByteArrayArray1183.length; i++)
		{
			if(aByteArrayArray1183[i] == null && anIntArray1235[i] != -1)
				return -1;
			if(aByteArrayArray1247[i] == null && anIntArray1236[i] != -1)
				return -2;
		}

		boolean flag = true;
		for(int j = 0; j < aByteArrayArray1183.length; j++)
		{
			byte abyte0[] = aByteArrayArray1247[j];
			if(abyte0 != null)
			{
				int k = (anIntArray1234[j] >> 8) * 64 - baseX;
				int l = (anIntArray1234[j] & 0xff) * 64 - baseY;
				if(aBoolean1159)
				{
					k = 10;
					l = 10;
				}
				flag &= ObjectManager.method189(k, abyte0, l);
			}
		}

		if(!flag)
			return -3;
		if(aBoolean1080)
		{
			return -4;
		} else
		{
			loadingStage = 2;
			ObjectManager.anInt131 = plane;
			method22();
			stream.createFrame(121);
			return 0;
		}
	}

	private void method55()
	{
		for(Animable_Sub4 class30_sub2_sub4_sub4 = (Animable_Sub4)aClass19_1013.reverseGetFirst(); class30_sub2_sub4_sub4 != null; class30_sub2_sub4_sub4 = (Animable_Sub4)aClass19_1013.reverseGetNext())
			if(class30_sub2_sub4_sub4.anInt1597 != plane || loopCycle > class30_sub2_sub4_sub4.anInt1572)
				class30_sub2_sub4_sub4.unlink();
			else
			if(loopCycle >= class30_sub2_sub4_sub4.anInt1571)
			{
				if(class30_sub2_sub4_sub4.anInt1590 > 0)
				{
					NPC npc = npcArray[class30_sub2_sub4_sub4.anInt1590 - 1];
					if(npc != null && npc.x >= 0 && npc.x < 13312 && npc.y >= 0 && npc.y < 13312)
						class30_sub2_sub4_sub4.method455(loopCycle, npc.y, method42(class30_sub2_sub4_sub4.anInt1597, npc.y, npc.x) - class30_sub2_sub4_sub4.anInt1583, npc.x);
				}
				if(class30_sub2_sub4_sub4.anInt1590 < 0)
				{
					int j = -class30_sub2_sub4_sub4.anInt1590 - 1;
					Player player;
					if(j == unknownInt10)
						player = myPlayer;
					else
						player = playerArray[j];
					if(player != null && player.x >= 0 && player.x < 13312 && player.y >= 0 && player.y < 13312)
						class30_sub2_sub4_sub4.method455(loopCycle, player.y, method42(class30_sub2_sub4_sub4.anInt1597, player.y, player.x) - class30_sub2_sub4_sub4.anInt1583, player.x);
				}
				class30_sub2_sub4_sub4.method456(anInt945);
				worldController.method285(plane, class30_sub2_sub4_sub4.anInt1595, (int)class30_sub2_sub4_sub4.aDouble1587, -1, (int)class30_sub2_sub4_sub4.aDouble1586, 60, (int)class30_sub2_sub4_sub4.aDouble1585, class30_sub2_sub4_sub4, false);
			}

	}

	public AppletContext getAppletContext()
	{
		if(signlink.mainapp != null)
			return signlink.mainapp.getAppletContext();
		else
			return super.getAppletContext();
	}

	private void drawLogo()
	{
		byte abyte0[] = titleStreamLoader.getDataForName("title.dat");
		Sprite sprite = new Sprite(abyte0, this);
		aRSImageProducer_1110.initDrawingArea();
		sprite.method346(0, 0);
		aRSImageProducer_1111.initDrawingArea();
		sprite.method346(-637, 0);
		aRSImageProducer_1107.initDrawingArea();
		sprite.method346(-128, 0);
		aRSImageProducer_1108.initDrawingArea();
		sprite.method346(-202, -371);
		aRSImageProducer_1109.initDrawingArea();
		sprite.method346(-202, -171);
		aRSImageProducer_1112.initDrawingArea();
		sprite.method346(0, -265);
		aRSImageProducer_1113.initDrawingArea();
		sprite.method346(-562, -265);
		aRSImageProducer_1114.initDrawingArea();
		sprite.method346(-128, -171);
		aRSImageProducer_1115.initDrawingArea();
		sprite.method346(-562, -171);
		int ai[] = new int[sprite.myWidth];
		for(int j = 0; j < sprite.myHeight; j++)
		{
			for(int k = 0; k < sprite.myWidth; k++)
				ai[k] = sprite.myPixels[(sprite.myWidth - k - 1) + sprite.myWidth * j];

			System.arraycopy(ai, 0, sprite.myPixels, sprite.myWidth * j, sprite.myWidth);

		}

		aRSImageProducer_1110.initDrawingArea();
		sprite.method346(382, 0);
		aRSImageProducer_1111.initDrawingArea();
		sprite.method346(-255, 0);
		aRSImageProducer_1107.initDrawingArea();
		sprite.method346(254, 0);
		aRSImageProducer_1108.initDrawingArea();
		sprite.method346(180, -371);
		aRSImageProducer_1109.initDrawingArea();
		sprite.method346(180, -171);
		aRSImageProducer_1112.initDrawingArea();
		sprite.method346(382, -265);
		aRSImageProducer_1113.initDrawingArea();
		sprite.method346(-180, -265);
		aRSImageProducer_1114.initDrawingArea();
		sprite.method346(254, -171);
		aRSImageProducer_1115.initDrawingArea();
			sprite.method346(-180, -171);
			aRSImageProducer_1107.initDrawingArea();
			sprite = null;
			Object obj = null;
			Object obj1 = null;
			System.gc();

	}

	private void processOnDemandQueue()
	{
		do
		{
			OnDemandData onDemandData;
			do
			{
				onDemandData = onDemandFetcher.getNextNode();
				if(onDemandData == null)
					return;
				if(onDemandData.dataType == 0)
				{
					Model.method460(onDemandData.buffer, onDemandData.ID);
					needDrawTabArea = true;
					if (backDialogID != -1)
						inputTaken = true;
				}
				if(onDemandData.dataType == 1 && onDemandData.buffer != null)
					Class36.method529(onDemandData.buffer,onDemandData.ID);
				if (onDemandData.dataType == 2 && onDemandData.ID == nextSong && onDemandData.buffer != null && MusicEnabled)
					MidiPlayer.getSingleton().play(onDemandData.buffer,true,musicVolume);
				if(onDemandData.dataType == 3 && loadingStage == 1)
				{
					for(int i = 0; i < aByteArrayArray1183.length; i++)
					{
						if(anIntArray1235[i] == onDemandData.ID)
						{
							aByteArrayArray1183[i] = onDemandData.buffer;
							if(onDemandData.buffer == null)
								anIntArray1235[i] = -1;
							break;
						}
						if(anIntArray1236[i] != onDemandData.ID)
							continue;
						aByteArrayArray1247[i] = onDemandData.buffer;
						if(onDemandData.buffer == null)
							anIntArray1236[i] = -1;
						break;
					}

				}
			} while(onDemandData.dataType != 93 || !onDemandFetcher.method564(onDemandData.ID));
			ObjectManager.method173(new Stream(onDemandData.buffer), onDemandFetcher);
		} while(true);
	}

	private void calcFlamesPosition()
	{
		char c = '\u0100';
		for(int j = 10; j < 117; j++)
		{
			int k = (int)(Math.random() * 100D);
			if(k < 50)
				anIntArray828[j + (c - 2 << 7)] = 255;
		}
		for(int l = 0; l < 100; l++)
		{
			int i1 = (int)(Math.random() * 124D) + 2;
			int k1 = (int)(Math.random() * 128D) + 128;
			int k2 = i1 + (k1 << 7);
			anIntArray828[k2] = 192;
		}

		for(int j1 = 1; j1 < c - 1; j1++)
		{
			for(int l1 = 1; l1 < 127; l1++)
			{
				int l2 = l1 + (j1 << 7);
				anIntArray829[l2] = (anIntArray828[l2 - 1] + anIntArray828[l2 + 1] + anIntArray828[l2 - 128] + anIntArray828[l2 + 128]) / 4;
			}

		}

		anInt1275 += 128;
		if(anInt1275 > anIntArray1190.length)
		{
			anInt1275 -= anIntArray1190.length;
			int i2 = (int)(Math.random() * 12D);
			randomizeBackground(aBackgroundArray1152s[i2]);
		}
		for(int j2 = 1; j2 < c - 1; j2++)
		{
			for(int i3 = 1; i3 < 127; i3++)
			{
				int k3 = i3 + (j2 << 7);
				int i4 = anIntArray829[k3 + 128] - anIntArray1190[k3 + anInt1275 & anIntArray1190.length - 1] / 5;
				if(i4 < 0)
					i4 = 0;
				anIntArray828[k3] = i4;
			}

		}

		System.arraycopy(anIntArray969, 1, anIntArray969, 0, c - 1);

		anIntArray969[c - 1] = (int)(Math.sin((double)loopCycle / 14D) * 16D + Math.sin((double)loopCycle / 15D) * 14D + Math.sin((double)loopCycle / 16D) * 12D);
		if(anInt1040 > 0)
			anInt1040 -= 4;
		if(anInt1041 > 0)
			anInt1041 -= 4;
		if(anInt1040 == 0 && anInt1041 == 0)
		{
			int l3 = (int)(Math.random() * 2000D);
			if(l3 == 0)
				anInt1040 = 1024;
			if(l3 == 1)
				anInt1041 = 1024;
		}
	}

	private boolean saveWave(byte abyte0[], int i)
	{
		SoundProvider.getSingleton().playWAV(abyte0);
		return abyte0 == null;
	}

	private void method60(int i)
	{
		RSInterface class9 = RSInterface.interfaceCache[i];
		for(int j = 0; j < class9.children.length; j++)
		{
			if(class9.children[j] == -1)
				break;
			RSInterface class9_1 = RSInterface.interfaceCache[class9.children[j]];
			if(class9_1.type == 1)
				method60(class9_1.id);
			class9_1.anInt246 = 0;
			class9_1.anInt208 = 0;
		}
	}

	private void drawHeadIcon()
    {
        if(anInt855 != 2)
            return;
        calcEntityScreenPos((anInt934 - baseX << 7) + anInt937, anInt936 * 2, (anInt935 - baseY << 7) + anInt938);
        if(spriteDrawX > -1 && loopCycle % 20 < 10)
            headIconsHint[0].drawSprite(spriteDrawX - 12, spriteDrawY - 28);
    }

	private void mainGameProcessor()
	{
		if(anInt1104 > 1)
			anInt1104--;
		if(anInt1011 > 0)
			anInt1011--;
		for(int j = 0; j < 5; j++)
			if(!parsePacket())
				break;

		if(!loggedIn)
			return;
		synchronized(mouseDetection.syncObject)
		{
			if(flagged)
			{
				if(super.clickMode3 != 0 || mouseDetection.coordsIndex >= 40)
				{
					stream.createFrame(45);
					stream.writeWordBigEndian(0);
					int j2 = stream.currentOffset;
					int j3 = 0;
					for(int j4 = 0; j4 < mouseDetection.coordsIndex; j4++)
					{
						if(j2 - stream.currentOffset >= 240)
							break;
						j3++;
						int l4 = mouseDetection.coordsY[j4];
						if(l4 < 0)
							l4 = 0;
						else
						if(l4 > 502)
							l4 = 502;
						int k5 = mouseDetection.coordsX[j4];
						if(k5 < 0)
							k5 = 0;
						else
						if(k5 > 764)
							k5 = 764;
						int i6 = l4 * 765 + k5;
						if(mouseDetection.coordsY[j4] == -1 && mouseDetection.coordsX[j4] == -1)
						{
							k5 = -1;
							l4 = -1;
							i6 = 0x7ffff;
						}
						if(k5 == anInt1237 && l4 == anInt1238)
						{
							if(anInt1022 < 2047)
								anInt1022++;
						} else
						{
							int j6 = k5 - anInt1237;
							anInt1237 = k5;
							int k6 = l4 - anInt1238;
							anInt1238 = l4;
							if(anInt1022 < 8 && j6 >= -32 && j6 <= 31 && k6 >= -32 && k6 <= 31)
							{
								j6 += 32;
								k6 += 32;
								stream.writeWord((anInt1022 << 12) + (j6 << 6) + k6);
								anInt1022 = 0;
							} else
							if(anInt1022 < 8)
							{
								stream.writeDWordBigEndian(0x800000 + (anInt1022 << 19) + i6);
								anInt1022 = 0;
							} else
							{
								stream.writeDWord(0xc0000000 + (anInt1022 << 19) + i6);
								anInt1022 = 0;
							}
						}
					}

					stream.writeBytes(stream.currentOffset - j2);
					if(j3 >= mouseDetection.coordsIndex)
					{
						mouseDetection.coordsIndex = 0;
					} else
					{
						mouseDetection.coordsIndex -= j3;
						for(int i5 = 0; i5 < mouseDetection.coordsIndex; i5++)
						{
							mouseDetection.coordsX[i5] = mouseDetection.coordsX[i5 + j3];
							mouseDetection.coordsY[i5] = mouseDetection.coordsY[i5 + j3];
						}

					}
				}
			} else
			{
				mouseDetection.coordsIndex = 0;
			}
		}
		if(super.clickMode3 != 0)
		{
			long l = (super.aLong29 - aLong1220) / 50L;
			if(l > 4095L)
				l = 4095L;
			aLong1220 = super.aLong29;
			int k2 = super.saveClickY;
			if(k2 < 0)
				k2 = 0;
			else
			if(k2 > 502)
				k2 = 502;
			int k3 = super.saveClickX;
			if(k3 < 0)
				k3 = 0;
			else
			if(k3 > 764)
				k3 = 764;
			int k4 = k2 * 765 + k3;
			int j5 = 0;
			if(super.clickMode3 == 2)
				j5 = 1;
			int l5 = (int)l;
			stream.createFrame(241);
			stream.writeDWord((l5 << 20) + (j5 << 19) + k4);
		}
		if(anInt1016 > 0)
			anInt1016--;
		if(super.keyArray[1] == 1 || super.keyArray[2] == 1 || super.keyArray[3] == 1 || super.keyArray[4] == 1)
			aBoolean1017 = true;
		if(aBoolean1017 && anInt1016 <= 0)
		{
			anInt1016 = 20;
			aBoolean1017 = false;
			stream.createFrame(86);
			stream.writeWord(anInt1184);
			stream.method432(minimapInt1);
		}
		if(super.awtFocus && !aBoolean954)
		{
			aBoolean954 = true;
			stream.createFrame(3);
			stream.writeWordBigEndian(1);
		}
		if(!super.awtFocus && aBoolean954)
		{
			aBoolean954 = false;
			stream.createFrame(3);
			stream.writeWordBigEndian(0);
		}
		loadingStages();
		method115();
		method90();
		anInt1009++;
		if(anInt1009 > 750)
			dropClient();
		method114();
		method95();
		method38();
		anInt945++;
		if(crossType != 0)
		{
			crossIndex += 20;
			if(crossIndex >= 400)
				crossType = 0;
		}
		if(atInventoryInterfaceType != 0)
		{
			atInventoryLoopCycle++;
			if(atInventoryLoopCycle >= 15)
			{
				if(atInventoryInterfaceType == 2)
					needDrawTabArea = true;
				if(atInventoryInterfaceType == 3)
					inputTaken = true;
				atInventoryInterfaceType = 0;
			}
		}
		if(activeInterfaceType != 0)
		{
			anInt989++;
			if(super.mouseX > anInt1087 + 5 || super.mouseX < anInt1087 - 5 || super.mouseY > anInt1088 + 5 || super.mouseY < anInt1088 - 5)
				aBoolean1242 = true;
			if(super.clickMode2 == 0)
			{
				if(activeInterfaceType == 2)
					needDrawTabArea = true;
				if(activeInterfaceType == 3)
					inputTaken = true;
				activeInterfaceType = 0;
				if(aBoolean1242 && anInt989 >= 5)
				{
					lastActiveInvInterface = -1;
					processRightClick();
					if(lastActiveInvInterface == anInt1084 && mouseInvInterfaceIndex != anInt1085)
					{
						RSInterface class9 = RSInterface.interfaceCache[anInt1084];
						int j1 = 0;
						if(anInt913 == 1 && class9.contentType == 206)
							j1 = 1;
						if(class9.inv[mouseInvInterfaceIndex] <= 0)
							j1 = 0;
						if(class9.aBoolean235)
						{
							int l2 = anInt1085;
							int l3 = mouseInvInterfaceIndex;
							class9.inv[l3] = class9.inv[l2];
							class9.invStackSizes[l3] = class9.invStackSizes[l2];
							class9.inv[l2] = -1;
							class9.invStackSizes[l2] = 0;
						} else
						if(j1 == 1)
						{
							int i3 = anInt1085;
							for(int i4 = mouseInvInterfaceIndex; i3 != i4;)
								if(i3 > i4)
								{
									class9.swapInventoryItems(i3, i3 - 1);
									i3--;
								} else
								if(i3 < i4)
								{
									class9.swapInventoryItems(i3, i3 + 1);
									i3++;
								}

						} else
						{
							class9.swapInventoryItems(anInt1085, mouseInvInterfaceIndex);
						}
						stream.createFrame(214);
						stream.method433(anInt1084);
						stream.method424(j1);
						stream.method433(anInt1085);
						stream.method431(mouseInvInterfaceIndex);
					}
				} else
				if((anInt1253 == 1 || menuHasAddFriend(menuActionRow - 1)) && menuActionRow > 2)
					determineMenuSize();
				else
				if(menuActionRow > 0)
					doAction(menuActionRow - 1);
				atInventoryLoopCycle = 10;
				super.clickMode3 = 0;
			}
		}
		if(WorldController.anInt470 != -1)
		{
			int k = WorldController.anInt470;
			int k1 = WorldController.anInt471;
			boolean flag = doWalkTo(0, 0, 0, 0, myPlayer.smallY[0], 0, 0, k1, myPlayer.smallX[0], true, k);
			WorldController.anInt470 = -1;
			if(flag)
			{
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 1;
				crossIndex = 0;
			}
		}
		if(super.clickMode3 == 1 && aString844 != null)
		{
			aString844 = null;
			inputTaken = true;
			super.clickMode3 = 0;
		}
		processMenuClick();
		processMainScreenClick();
		processTabClick();
		processMapAreaClick();
		processChatModeClick();
		if(super.clickMode2 == 1 || super.clickMode3 == 1)
			anInt1213++;
		if(loadingStage == 2)
			method108();
		if(loadingStage == 2 && aBoolean1160)
			calcCameraPos();
		for(int i1 = 0; i1 < 5; i1++)
			anIntArray1030[i1]++;

		method73();
		super.idleTime++;
		if(super.idleTime > 20000)
		{
			anInt1011 = 250;
			super.idleTime -= 2000;
			stream.createFrame(202);
		}
		anInt988++;
		if(anInt988 > 500)
		{
			anInt988 = 0;
			int l1 = (int)(Math.random() * 8D);
			if((l1 & 1) == 1)
				anInt1278 += anInt1279;
			if((l1 & 2) == 2)
				anInt1131 += anInt1132;
			if((l1 & 4) == 4)
				anInt896 += anInt897;
		}
		if(anInt1278 < -50)
			anInt1279 = 2;
		if(anInt1278 > 50)
			anInt1279 = -2;
		if(anInt1131 < -55)
			anInt1132 = 2;
		if(anInt1131 > 55)
			anInt1132 = -2;
		if(anInt896 < -40)
			anInt897 = 1;
		if(anInt896 > 40)
			anInt897 = -1;
		anInt1254++;
		if(anInt1254 > 500)
		{
			anInt1254 = 0;
			int i2 = (int)(Math.random() * 8D);
			if((i2 & 1) == 1)
				minimapInt2 += anInt1210;
			if((i2 & 2) == 2)
				minimapInt3 += anInt1171;
		}
		if(minimapInt2 < -60)
			anInt1210 = 2;
		if(minimapInt2 > 60)
			anInt1210 = -2;
		if(minimapInt3 < -20)
			anInt1171 = 1;
		if(minimapInt3 > 10)
			anInt1171 = -1;
		anInt1010++;
		if(anInt1010 > 50)
			stream.createFrame(0);
		try
		{
			if(socketStream != null && stream.currentOffset > 0)
			{
				socketStream.queueBytes(stream.currentOffset, stream.buffer);
				stream.currentOffset = 0;
				anInt1010 = 0;
			}
		}
		catch(IOException _ex)
		{
			dropClient();
		}
		catch(Exception exception)
		{
			resetLogout();
		}
	}

	private void method63()
	{
		Class30_Sub1 class30_sub1 = (Class30_Sub1)aClass19_1179.reverseGetFirst();
		for(; class30_sub1 != null; class30_sub1 = (Class30_Sub1)aClass19_1179.reverseGetNext())
			if(class30_sub1.anInt1294 == -1)
			{
				class30_sub1.anInt1302 = 0;
				method89(class30_sub1);
			} else
			{
				class30_sub1.unlink();
			}

	}

	private void resetImageProducers()
	{
		if(aRSImageProducer_1107 != null)
			return;
		super.fullGameScreen = null;
		aRSImageProducer_1166 = null;
		aRSImageProducer_1164 = null;
		aRSImageProducer_1163 = null;
		aRSImageProducer_1165 = null;
		aRSImageProducer_1123 = null;
		aRSImageProducer_1124 = null;
		aRSImageProducer_1125 = null;
		aRSImageProducer_1110 = new RSImageProducer(128, 265, getGameComponent());
		DrawingArea.setAllPixelsToZero();
		aRSImageProducer_1111 = new RSImageProducer(128, 265, getGameComponent());
		DrawingArea.setAllPixelsToZero();
		aRSImageProducer_1107 = new RSImageProducer(509, 171, getGameComponent());
		DrawingArea.setAllPixelsToZero();
		aRSImageProducer_1108 = new RSImageProducer(360, 132, getGameComponent());
		DrawingArea.setAllPixelsToZero();
		aRSImageProducer_1109 = new RSImageProducer(360, 200, getGameComponent());
		DrawingArea.setAllPixelsToZero();
		aRSImageProducer_1112 = new RSImageProducer(202, 238, getGameComponent());
		DrawingArea.setAllPixelsToZero();
		aRSImageProducer_1113 = new RSImageProducer(203, 238, getGameComponent());
		DrawingArea.setAllPixelsToZero();
		aRSImageProducer_1114 = new RSImageProducer(74, 94, getGameComponent());
		DrawingArea.setAllPixelsToZero();
		aRSImageProducer_1115 = new RSImageProducer(75, 94, getGameComponent());
		DrawingArea.setAllPixelsToZero();
		if(titleStreamLoader != null)
		{
			drawLogo();
			loadTitleScreen();
		}
		welcomeScreenRaised = true;
	}

	void drawLoadingText(int i, String s)
	{
		anInt1079 = i;
		aString1049 = s;
		resetImageProducers();
		if(titleStreamLoader == null)
		{
			super.drawLoadingText(i, s);
			return;
		}
		aRSImageProducer_1109.initDrawingArea();
		char c = '\u0168';
		char c1 = '\310';
		byte byte1 = 20;
		chatTextDrawingArea.drawText(0xffffff, "Vscape is loading - please wait...", c1 / 2 - 26 - byte1, c / 2);
		int j = c1 / 2 - 18 - byte1;
		DrawingArea.fillPixels(c / 2 - 152, 304, 34, 0x8c1111, j);
		DrawingArea.fillPixels(c / 2 - 151, 302, 32, 0, j + 1);
		DrawingArea.drawPixels(30, j + 2, c / 2 - 150, 0x8c1111, i * 3);
		DrawingArea.drawPixels(30, j + 2, (c / 2 - 150) + i * 3, 0, 300 - i * 3);
		chatTextDrawingArea.drawText(0xffffff, s, (c1 / 2 + 5) - byte1, c / 2);
		aRSImageProducer_1109.drawGraphics(171, super.graphics, 202);
		if(welcomeScreenRaised)
		{
			welcomeScreenRaised = false;
			if(!aBoolean831)
			{
				aRSImageProducer_1110.drawGraphics(0, super.graphics, 0);
				aRSImageProducer_1111.drawGraphics(0, super.graphics, 637);
			}
			aRSImageProducer_1107.drawGraphics(0, super.graphics, 128);
			aRSImageProducer_1108.drawGraphics(371, super.graphics, 202);
			aRSImageProducer_1112.drawGraphics(265, super.graphics, 0);
			aRSImageProducer_1113.drawGraphics(265, super.graphics, 562);
			aRSImageProducer_1114.drawGraphics(171, super.graphics, 128);
			aRSImageProducer_1115.drawGraphics(171, super.graphics, 562);
		}
	}

	private void method65(int i, int j, int k, int l, RSInterface class9, int i1, boolean flag,
						  int j1)
	{
		int anInt992;
		if(aBoolean972)
			anInt992 = 32;
		else
			anInt992 = 0;
		aBoolean972 = false;
		if(k >= i && k < i + 16 && l >= i1 && l < i1 + 16)
		{
			class9.scrollPosition -= anInt1213 * 4;
			if(flag)
			{
				needDrawTabArea = true;
			}
		} else
		if(k >= i && k < i + 16 && l >= (i1 + j) - 16 && l < i1 + j)
		{
			class9.scrollPosition += anInt1213 * 4;
			if(flag)
			{
				needDrawTabArea = true;
			}
		} else
		if(k >= i - anInt992 && k < i + 16 + anInt992 && l >= i1 + 16 && l < (i1 + j) - 16 && anInt1213 > 0)
		{
			int l1 = ((j - 32) * j) / j1;
			if(l1 < 8)
				l1 = 8;
			int i2 = l - i1 - 16 - l1 / 2;
			int j2 = j - 32 - l1;
			class9.scrollPosition = ((j1 - j) * i2) / j2;
			if(flag)
				needDrawTabArea = true;
			aBoolean972 = true;
		}
	}

	private boolean method66(int i, int j, int k)
	{
		int i1 = i >> 14 & 0x7fff;
		int j1 = worldController.method304(plane, k, j, i);
		if(j1 == -1)
			return false;
		int k1 = j1 & 0x1f;
		int l1 = j1 >> 6 & 3;
		if(k1 == 10 || k1 == 11 || k1 == 22)
		{
			ObjectDef class46 = ObjectDef.forID(i1);
			int i2;
			int j2;
			if(l1 == 0 || l1 == 2)
			{
				i2 = class46.anInt744;
				j2 = class46.anInt761;
			} else
			{
				i2 = class46.anInt761;
				j2 = class46.anInt744;
			}
			int k2 = class46.anInt768;
			if(l1 != 0)
				k2 = (k2 << l1 & 0xf) + (k2 >> 4 - l1);
			doWalkTo(2, 0, j2, 0, myPlayer.smallY[0], i2, k2, j, myPlayer.smallX[0], false, k);
		} else
		{
			doWalkTo(2, l1, 0, k1 + 1, myPlayer.smallY[0], 0, 0, j, myPlayer.smallX[0], false, k);
		}
		crossX = super.saveClickX;
		crossY = super.saveClickY;
		crossType = 2;
		crossIndex = 0;
		return true;
	}

	private StreamLoader streamLoaderForName(int i, String s, String s1, int j, int k)
	{
		byte abyte0[] = null;
		int l = 5;
		try
		{
			if(decompressors[0] != null)
				abyte0 = decompressors[0].decompress(i);
		}
		catch(Exception _ex) { }
		if(abyte0 != null)
		{
	//		aCRC32_930.reset();
	//		aCRC32_930.update(abyte0);
	//		int i1 = (int)aCRC32_930.getValue();
	//		if(i1 != j)
		}
		if(abyte0 != null)
		{
			StreamLoader streamLoader = new StreamLoader(abyte0);
			return streamLoader;
		}
		int j1 = 0;
		while(abyte0 == null)
		{
			String s2 = "Unknown error";
			drawLoadingText(k, "Requesting " + s);
			Object obj = null;
			try
			{
				int k1 = 0;
				DataInputStream datainputstream = openJagGrabInputStream(s1 + j);
				byte abyte1[] = new byte[6];
				datainputstream.readFully(abyte1, 0, 6);
				Stream stream = new Stream(abyte1);
				stream.currentOffset = 3;
				int i2 = stream.read3Bytes() + 6;
				int j2 = 6;
				abyte0 = new byte[i2];
				System.arraycopy(abyte1, 0, abyte0, 0, 6);

				while(j2 < i2) 
				{
					int l2 = i2 - j2;
					if(l2 > 1000)
						l2 = 1000;
					int j3 = datainputstream.read(abyte0, j2, l2);
					if(j3 < 0)
					{
						s2 = "Length error: " + j2 + "/" + i2;
						throw new IOException("EOF");
					}
					j2 += j3;
					int k3 = (j2 * 100) / i2;
					if(k3 != k1)
						drawLoadingText(k, "Loading " + s + " - " + k3 + "%");
					k1 = k3;
				}
				datainputstream.close();
				try
				{
					if(decompressors[0] != null)
						decompressors[0].method234(abyte0.length, abyte0, i);
				}
				catch(Exception _ex)
				{
					decompressors[0] = null;
				}
   /*			 if(abyte0 != null)
				{
					aCRC32_930.reset();
					aCRC32_930.update(abyte0);
					int i3 = (int)aCRC32_930.getValue();
					if(i3 != j)
					{
						abyte0 = null;
						j1++;
						s2 = "Checksum error: " + i3;
					}
				}
  */
			}
			catch(IOException ioexception)
			{
				if(s2.equals("Unknown error"))
					s2 = "Connection error";
				abyte0 = null;
			}
			catch(NullPointerException _ex)
			{
				s2 = "Null error";
				abyte0 = null;
				if(!signlink.reporterror)
					return null;
			}
			catch(ArrayIndexOutOfBoundsException _ex)
			{
				s2 = "Bounds error";
				abyte0 = null;
				if(!signlink.reporterror)
					return null;
			}
			catch(Exception _ex)
			{
				s2 = "Unexpected error";
				abyte0 = null;
				if(!signlink.reporterror)
					return null;
			}
			if(abyte0 == null)
			{
				for(int l1 = l; l1 > 0; l1--)
				{
					if(j1 >= 3)
					{
						drawLoadingText(k, "Game updated - please reload page");
						l1 = 10;
					} else
					{
						drawLoadingText(k, s2 + " - Retrying in " + l1);
					}
					try
					{
						Thread.sleep(1000L);
					}
					catch(Exception _ex) { }
				}

				l *= 2;
				if(l > 60)
					l = 60;
				aBoolean872 = !aBoolean872;
			}

		}

		StreamLoader streamLoader_1 = new StreamLoader(abyte0);
			return streamLoader_1;
	}

	private void dropClient()
	{
		if(anInt1011 > 0)
		{
			resetLogout();
			return;
		}
		aRSImageProducer_1165.initDrawingArea();
		aTextDrawingArea_1271.drawText(0, "Connection lost", 144, 257);
		aTextDrawingArea_1271.drawText(0xffffff, "Connection lost", 143, 256);
		aTextDrawingArea_1271.drawText(0, "Please wait - attempting to reestablish", 159, 257);
		aTextDrawingArea_1271.drawText(0xffffff, "Please wait - attempting to reestablish", 158, 256);
		aRSImageProducer_1165.drawGraphics(4, super.graphics, 4);
		anInt1021 = 0;
		destX = 0;
		RSSocket rsSocket = socketStream;
		loggedIn = false;
		loginFailures = 0;
		login(myUsername, myPassword, true);
		if(!loggedIn)
			resetLogout();
		try
		{
			rsSocket.close();
		}
		catch(Exception _ex)
		{
		}
	}

	private void doAction(int i)
	{
		if(i < 0)
			return;
		if(inputDialogState != 0)
		{
			inputDialogState = 0;
			inputTaken = true;
		}
		int j = menuActionCmd2[i];
		int k = menuActionCmd3[i];
		int l = menuActionID[i];
		int i1 = menuActionCmd1[i];
		if(l >= 2000)
			l -= 2000;
		if(l == 582)
		{
			NPC npc = npcArray[i1];
			if(npc != null)
			{
				doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, npc.smallY[0], myPlayer.smallX[0], false, npc.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(57);
				stream.method432(anInt1285);
				stream.method432(i1);
				stream.method431(anInt1283);
				stream.method432(anInt1284);
			}
		}
		if(l == 234)
		{
			boolean flag1 = doWalkTo(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, k, myPlayer.smallX[0], false, j);
			if(!flag1)
				flag1 = doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, k, myPlayer.smallX[0], false, j);
			crossX = super.saveClickX;
			crossY = super.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(236);
			stream.method431(k + baseY);
			stream.writeWord(i1);
			stream.method431(j + baseX);
		}
		if(l == 62 && method66(i1, k, j))
		{
			stream.createFrame(192);
			stream.writeWord(anInt1284);
			stream.method431(i1 >> 14 & 0x7fff);
			stream.method433(k + baseY);
			stream.method431(anInt1283);
			stream.method433(j + baseX);
			stream.writeWord(anInt1285);
		}
		if(l == 511)
		{
			boolean flag2 = doWalkTo(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, k, myPlayer.smallX[0], false, j);
			if(!flag2)
				flag2 = doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, k, myPlayer.smallX[0], false, j);
			crossX = super.saveClickX;
			crossY = super.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(25);
			stream.method431(anInt1284);
			stream.method432(anInt1285);
			stream.writeWord(i1);
			stream.method432(k + baseY);
			stream.method433(anInt1283);
			stream.writeWord(j + baseX);
		}
		if(l == 74)
		{
			stream.createFrame(122);
			stream.method433(k);
			stream.method432(j);
			stream.method431(i1);
			atInventoryLoopCycle = 0;
			atInventoryInterface = k;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if(RSInterface.interfaceCache[k].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if(RSInterface.interfaceCache[k].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if(l == 315)
		{
			RSInterface class9 = RSInterface.interfaceCache[k];
			boolean flag8 = true;
			if(class9.contentType > 0)
				flag8 = promptUserForInput(class9);
			if(flag8)
			{
				stream.createFrame(185);
				stream.writeWord(k);
			}
		}
		if(l == 561)
		{
			Player player = playerArray[i1];
			if(player != null)
			{
				doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, player.smallY[0], myPlayer.smallX[0], false, player.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				anInt1188 += i1;
				if(anInt1188 >= 90)
				{
					stream.createFrame(136);
					anInt1188 = 0;
				}
				stream.createFrame(128);
				stream.writeWord(i1);
			}
		}
		if(l == 20)
		{
			NPC class30_sub2_sub4_sub1_sub1_1 = npcArray[i1];
			if(class30_sub2_sub4_sub1_sub1_1 != null)
			{
				doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub1_1.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub1_1.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(155);
				stream.method431(i1);
			}
		}
		if(l == 779)
		{
			Player class30_sub2_sub4_sub1_sub2_1 = playerArray[i1];
			if(class30_sub2_sub4_sub1_sub2_1 != null)
			{
				doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub2_1.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub2_1.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(153);
				stream.method431(i1);
			}
		}
		if(l == 516)
			if(!menuOpen)
				worldController.method312(super.saveClickY - 4, super.saveClickX - 4);
			else
				worldController.method312(k - 4, j - 4);
		if(l == 1062)
		{
			anInt924 += baseX;
			if(anInt924 >= 113)
			{
				stream.createFrame(183);
				stream.writeDWordBigEndian(0xe63271);
				anInt924 = 0;
			}
			method66(i1, k, j);
			stream.createFrame(228);
			stream.method432(i1 >> 14 & 0x7fff);
			stream.method432(k + baseY);
			stream.writeWord(j + baseX);
		}
		if(l == 679 && !aBoolean1149)
		{
			stream.createFrame(40);
			stream.writeWord(k);
			aBoolean1149 = true;
		}
		if(l == 431)
		{
			stream.createFrame(129);
			stream.method432(j);
			stream.writeWord(k);
			stream.method432(i1);
			atInventoryLoopCycle = 0;
			atInventoryInterface = k;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if(RSInterface.interfaceCache[k].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if(RSInterface.interfaceCache[k].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if(l == 337 || l == 42 || l == 792 || l == 322)
		{
			String s = menuActionName[i];
			int k1 = s.indexOf("@whi@");
			if(k1 != -1)
			{
				long l3 = TextClass.longForName(s.substring(k1 + 5).trim());
				if(l == 337)
					addFriend(l3);
				if(l == 42)
					addIgnore(l3);
				if(l == 792)
					delFriend(l3);
				if(l == 322)
					delIgnore(l3);
			}
		}
		if(l == 53)
		{
			stream.createFrame(135);
			stream.method431(j);
			stream.method432(k);
			stream.method431(i1);
			atInventoryLoopCycle = 0;
			atInventoryInterface = k;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if(RSInterface.interfaceCache[k].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if(RSInterface.interfaceCache[k].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if(l == 539)
		{
			stream.createFrame(16);
			stream.method432(i1);
			stream.method433(j);
			stream.method433(k);
			atInventoryLoopCycle = 0;
			atInventoryInterface = k;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if(RSInterface.interfaceCache[k].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if(RSInterface.interfaceCache[k].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if(l == 484 || l == 6)
		{
			String s1 = menuActionName[i];
			int l1 = s1.indexOf("@whi@");
			if(l1 != -1)
			{
				s1 = s1.substring(l1 + 5).trim();
				String s7 = TextClass.fixName(TextClass.nameForLong(TextClass.longForName(s1)));
				boolean flag9 = false;
				for(int j3 = 0; j3 < playerCount; j3++)
				{
					Player class30_sub2_sub4_sub1_sub2_7 = playerArray[playerIndices[j3]];
					if(class30_sub2_sub4_sub1_sub2_7 == null || class30_sub2_sub4_sub1_sub2_7.name == null || !class30_sub2_sub4_sub1_sub2_7.name.equalsIgnoreCase(s7))
						continue;
					doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub2_7.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub2_7.smallX[0]);
					if(l == 484)
					{
						stream.createFrame(139);
						stream.method431(playerIndices[j3]);
					}
					if(l == 6)
					{
						anInt1188 += i1;
						if(anInt1188 >= 90)
						{
							stream.createFrame(136);
							anInt1188 = 0;
						}
						stream.createFrame(128);
						stream.writeWord(playerIndices[j3]);
					}
					flag9 = true;
					break;
				}

				if(!flag9)
					pushMessage("Unable to find " + s7, 0, "");
			}
		}
		if(l == 870)
		{
			stream.createFrame(53);
			stream.writeWord(j);
			stream.method432(anInt1283);
			stream.method433(i1);
			stream.writeWord(anInt1284);
			stream.method431(anInt1285);
			stream.writeWord(k);
			atInventoryLoopCycle = 0;
			atInventoryInterface = k;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if(RSInterface.interfaceCache[k].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if(RSInterface.interfaceCache[k].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if(l == 847)
		{
			stream.createFrame(87);
			stream.method432(i1);
			stream.writeWord(k);
			stream.method432(j);
			atInventoryLoopCycle = 0;
			atInventoryInterface = k;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if(RSInterface.interfaceCache[k].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if(RSInterface.interfaceCache[k].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if(l == 626)
		{
			RSInterface class9_1 = RSInterface.interfaceCache[k];
			spellSelected = 1;
			anInt1137 = k;
			spellUsableOn = class9_1.spellUsableOn;
			itemSelected = 0;
			needDrawTabArea = true;
			String s4 = class9_1.selectedActionName;
			if(s4.indexOf(" ") != -1)
				s4 = s4.substring(0, s4.indexOf(" "));
			String s8 = class9_1.selectedActionName;
			if(s8.indexOf(" ") != -1)
				s8 = s8.substring(s8.indexOf(" ") + 1);
			spellTooltip = s4 + " " + class9_1.spellName + " " + s8;
			if(spellUsableOn == 16)
			{
				needDrawTabArea = true;
				tabID = 3;
				tabAreaAltered = true;
			}
			return;
		}
		if(l == 78)
		{
			stream.createFrame(117);
			stream.method433(k);
			stream.method433(i1);
			stream.method431(j);
			atInventoryLoopCycle = 0;
			atInventoryInterface = k;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if(RSInterface.interfaceCache[k].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if(RSInterface.interfaceCache[k].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if(l == 27)
		{
			Player class30_sub2_sub4_sub1_sub2_2 = playerArray[i1];
			if(class30_sub2_sub4_sub1_sub2_2 != null)
			{
				doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub2_2.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub2_2.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				anInt986 += i1;
				if(anInt986 >= 54)
				{
					stream.createFrame(189);
					stream.writeWordBigEndian(234);
					anInt986 = 0;
				}
				stream.createFrame(73);
				stream.method431(i1);
			}
		}
		if(l == 213)
		{
			boolean flag3 = doWalkTo(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, k, myPlayer.smallX[0], false, j);
			if(!flag3)
				flag3 = doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, k, myPlayer.smallX[0], false, j);
			crossX = super.saveClickX;
			crossY = super.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(79);
			stream.method431(k + baseY);
			stream.writeWord(i1);
			stream.method432(j + baseX);
		}
		if(l == 632)
		{
			stream.createFrame(145);
			stream.method432(k);
			stream.method432(j);
			stream.method432(i1);
			atInventoryLoopCycle = 0;
			atInventoryInterface = k;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if(RSInterface.interfaceCache[k].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if(RSInterface.interfaceCache[k].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if(l == 1005) {
			globalMode = 2;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if(l == 1004) {
			globalMode = 1;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if(l == 1003) {
			globalMode = 0;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if(l == 1002) {
			cButtonCPos = 0;
			chatTypeView = 9;
			aBoolean1233 = true;
			inputTaken = true;
		}		
		if(l == 1001) {
			gameMode = 1;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if(l == 1000) {
			gameMode = 0;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if(l == 999) {
			cButtonCPos = 0;
			chatTypeView = 0;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if(l == 998) {
			cButtonCPos = 1;
			chatTypeView = 5;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if(l == 997) {
			publicChatMode = 3;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if(l == 996) {
			publicChatMode = 2;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if(l == 995) {
			publicChatMode = 1;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if(l == 994) {
			publicChatMode = 0;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if(l == 993) {
			cButtonCPos = 2;
			chatTypeView = 1;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if(l == 992) {
			privateChatMode = 2;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if(l == 991) {
			privateChatMode = 1;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if(l == 990) {
			privateChatMode = 0;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if(l == 989) {
			cButtonCPos = 3;
			chatTypeView = 2;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if(l == 987) {
			tradeMode = 2;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if(l == 986) {
			tradeMode = 1;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if(l == 985) {
			tradeMode = 0;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if(l == 984) {
			cButtonCPos = 5;
			chatTypeView = 3;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if(l == 983) {
			duelMode = 2;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if(l == 982) {
			duelMode = 1;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if(l == 981) {
			duelMode = 0;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if(l == 980) {
			cButtonCPos = 6;
			chatTypeView = 4;
			aBoolean1233 = true;
			inputTaken = true;
		}
		if(l == 493)
		{
			stream.createFrame(75);
			stream.method433(k);
			stream.method431(j);
			stream.method432(i1);
			atInventoryLoopCycle = 0;
			atInventoryInterface = k;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if(RSInterface.interfaceCache[k].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if(RSInterface.interfaceCache[k].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if(l == 652)
		{
			boolean flag4 = doWalkTo(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, k, myPlayer.smallX[0], false, j);
			if(!flag4)
				flag4 = doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, k, myPlayer.smallX[0], false, j);
			crossX = super.saveClickX;
			crossY = super.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(156);
			stream.method432(j + baseX);
			stream.method431(k + baseY);
			stream.method433(i1);
		}
		if(l == 94)
		{
			boolean flag5 = doWalkTo(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, k, myPlayer.smallX[0], false, j);
			if(!flag5)
				flag5 = doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, k, myPlayer.smallX[0], false, j);
			crossX = super.saveClickX;
			crossY = super.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(181);
			stream.method431(k + baseY);
			stream.writeWord(i1);
			stream.method431(j + baseX);
			stream.method432(anInt1137);
		}
		if(l == 646)
		{
			stream.createFrame(185);
			stream.writeWord(k);
			RSInterface class9_2 = RSInterface.interfaceCache[k];
			if(class9_2.valueIndexArray != null && class9_2.valueIndexArray[0][0] == 5)
			{
				int i2 = class9_2.valueIndexArray[0][1];
				if(variousSettings[i2] != class9_2.anIntArray212[0])
				{
					variousSettings[i2] = class9_2.anIntArray212[0];
					method33(i2);
					needDrawTabArea = true;
				}
			}
		}
		if(l == 225)
		{
			NPC class30_sub2_sub4_sub1_sub1_2 = npcArray[i1];
			if(class30_sub2_sub4_sub1_sub1_2 != null)
			{
				doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub1_2.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub1_2.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				anInt1226 += i1;
				if(anInt1226 >= 85)
				{
					stream.createFrame(230);
					stream.writeWordBigEndian(239);
					anInt1226 = 0;
				}
				stream.createFrame(17);
				stream.method433(i1);
			}
		}
		if(l == 965)
		{
			NPC class30_sub2_sub4_sub1_sub1_3 = npcArray[i1];
			if(class30_sub2_sub4_sub1_sub1_3 != null)
			{
				doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub1_3.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub1_3.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				anInt1134++;
				if(anInt1134 >= 96)
				{
					stream.createFrame(152);
					stream.writeWordBigEndian(88);
					anInt1134 = 0;
				}
				stream.createFrame(21);
				stream.writeWord(i1);
			}
		}
		if(l == 413)
		{
			NPC class30_sub2_sub4_sub1_sub1_4 = npcArray[i1];
			if(class30_sub2_sub4_sub1_sub1_4 != null)
			{
				doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub1_4.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub1_4.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(131);
				stream.method433(i1);
				stream.method432(anInt1137);
			}
		}
		if(l == 200)
			clearTopInterfaces();
		if(l == 1025)
		{
			/*NPC class30_sub2_sub4_sub1_sub1_5 = npcArray[i1];
			if(class30_sub2_sub4_sub1_sub1_5 != null)
			{
				EntityDef entityDef = class30_sub2_sub4_sub1_sub1_5.desc;
				if(entityDef.childrenIDs != null)
					entityDef = entityDef.method161();
				if(entityDef != null)
				{
					String s9;
					if(entityDef.description != null)
						s9 = new String(entityDef.description);
					else
						s9 = "It's a " + entityDef.name + ".";
					pushMessage(s9, 0, "");
				}
			}*/
			//EXAMINE NPC
			NPC class30_sub2_sub4_sub1_sub1_5 = npcArray[i1];
			if (class30_sub2_sub4_sub1_sub1_5 != null) {
				EntityDef entityDef = class30_sub2_sub4_sub1_sub1_5.desc;
				if (entityDef.childrenIDs != null)
					entityDef = entityDef.method161();
				if (entityDef != null) {
					stream.createFrame(222);
					stream.writeWord(entityDef.npcID);
				}
			}
		}
		if(l == 900)
		{
			method66(i1, k, j);
			stream.createFrame(252);
			stream.method433(i1 >> 14 & 0x7fff);
			stream.method431(k + baseY);
			stream.method432(j + baseX);
		}
		if(l == 412)
		{
			NPC class30_sub2_sub4_sub1_sub1_6 = npcArray[i1];
			if(class30_sub2_sub4_sub1_sub1_6 != null)
			{
				doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub1_6.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub1_6.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(72);
				stream.method432(i1);
			}
		}
		if(l == 365)
		{
			Player class30_sub2_sub4_sub1_sub2_3 = playerArray[i1];
			if(class30_sub2_sub4_sub1_sub2_3 != null)
			{
				doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub2_3.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub2_3.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(249);
				stream.method432(i1);
				stream.method431(anInt1137);
			}
		}
		if(l == 729)
		{
			Player class30_sub2_sub4_sub1_sub2_4 = playerArray[i1];
			if(class30_sub2_sub4_sub1_sub2_4 != null)
			{
				doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub2_4.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub2_4.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(39);
				stream.method431(i1);
			}
		}
		if(l == 577)
		{
			Player class30_sub2_sub4_sub1_sub2_5 = playerArray[i1];
			if(class30_sub2_sub4_sub1_sub2_5 != null)
			{
				doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub2_5.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub2_5.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(139);
				stream.method431(i1);
			}
		}
		if(l == 956 && method66(i1, k, j))
		{
			stream.createFrame(35);
			stream.method431(j + baseX);
			stream.method432(anInt1137);
			stream.method432(k + baseY);
			stream.method431(i1 >> 14 & 0x7fff);
		}
		if(l == 567)
		{
			boolean flag6 = doWalkTo(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, k, myPlayer.smallX[0], false, j);
			if(!flag6)
				flag6 = doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, k, myPlayer.smallX[0], false, j);
			crossX = super.saveClickX;
			crossY = super.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(23);
			stream.method431(k + baseY);
			stream.method431(i1);
			stream.method431(j + baseX);
		}
		if(l == 867)
		{
			if((i1 & 3) == 0)
				anInt1175++;
			if(anInt1175 >= 59)
			{
				stream.createFrame(200);
				stream.writeWord(25501);
				anInt1175 = 0;
			}
			stream.createFrame(43);
			stream.method431(k);
			stream.method432(i1);
			stream.method432(j);
			atInventoryLoopCycle = 0;
			atInventoryInterface = k;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if(RSInterface.interfaceCache[k].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if(RSInterface.interfaceCache[k].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if(l == 543)
		{
			stream.createFrame(237);
			stream.writeWord(j);
			stream.method432(i1);
			stream.writeWord(k);
			stream.method432(anInt1137);
			atInventoryLoopCycle = 0;
			atInventoryInterface = k;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if(RSInterface.interfaceCache[k].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if(RSInterface.interfaceCache[k].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if(l == 606)
		{
			String s2 = menuActionName[i];
			int j2 = s2.indexOf("@whi@");
			if(j2 != -1)
				if(openInterfaceID == -1)
				{
					clearTopInterfaces();
					reportAbuseInput = s2.substring(j2 + 5).trim();
					canMute = false;
					for(int i3 = 0; i3 < RSInterface.interfaceCache.length; i3++)
					{
						if(RSInterface.interfaceCache[i3] == null || RSInterface.interfaceCache[i3].contentType != 600)
							continue;
						reportAbuseInterfaceID = openInterfaceID = RSInterface.interfaceCache[i3].parentID;
						break;
					}

				} else
				{
					pushMessage("Please close the interface you have open before using 'report abuse'", 0, "");
				}
		}
		if(l == 491)
		{
			Player class30_sub2_sub4_sub1_sub2_6 = playerArray[i1];
			if(class30_sub2_sub4_sub1_sub2_6 != null)
			{
				doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub2_6.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub2_6.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(14);
				//stream.method432(anInt1284);
				stream.writeWord(i1); //Player id
				//stream.writeWord(anInt1285);
				stream.method431(anInt1283); //Item slot
			}
		}
		if(l == 639)
		{
			String s3 = menuActionName[i];
			int k2 = s3.indexOf("@whi@");
			if(k2 != -1)
			{
				long l4 = TextClass.longForName(s3.substring(k2 + 5).trim());
				int k3 = -1;
				for(int i4 = 0; i4 < friendsCount; i4++)
				{
					if(friendsListAsLongs[i4] != l4)
						continue;
					k3 = i4;
					break;
				}

				if(k3 != -1 && friendsNodeIDs[k3] > 0)
				{
					inputTaken = true;
					inputDialogState = 0;
					messagePromptRaised = true;
					promptInput = "";
					friendsListAction = 3;
					aLong953 = friendsListAsLongs[k3];
					aString1121 = "Enter message to send to " + friendsList[k3];
				}
			}
		}
		if(l == 454)
		{
			stream.createFrame(41);
			stream.writeWord(i1);
			stream.method432(j);
			stream.method432(k);
			atInventoryLoopCycle = 0;
			atInventoryInterface = k;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if(RSInterface.interfaceCache[k].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if(RSInterface.interfaceCache[k].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if(l == 478)
		{
			NPC class30_sub2_sub4_sub1_sub1_7 = npcArray[i1];
			if(class30_sub2_sub4_sub1_sub1_7 != null)
			{
				doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub1_7.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub1_7.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				if((i1 & 3) == 0)
					anInt1155++;
				if(anInt1155 >= 53)
				{
					stream.createFrame(85);
					stream.writeWordBigEndian(66);
					anInt1155 = 0;
				}
				stream.createFrame(18);
				stream.method431(i1);
			}
		}
		if(l == 113)
		{
			method66(i1, k, j);
			stream.createFrame(70);
			stream.method431(j + baseX);
			stream.writeWord(k + baseY);
			stream.method433(i1 >> 14 & 0x7fff);
		}
		if(l == 872)
		{
			method66(i1, k, j);
			stream.createFrame(234);
			stream.method433(j + baseX);
			stream.method432(i1 >> 14 & 0x7fff);
			stream.method433(k + baseY);
		}
		if(l == 502)
		{
			method66(i1, k, j);
			stream.createFrame(132);
			stream.method433(j + baseX);
			stream.writeWord(i1 >> 14 & 0x7fff);
			stream.method432(k + baseY);
		}
		if(l == 1125)
		{
		/*	ItemDef itemDef = ItemDef.forID(i1);
			RSInterface class9_4 = RSInterface.interfaceCache[k];
			String s5;
			if(class9_4 != null && class9_4.invStackSizes[j] >= 0x186a0)
				s5 = class9_4.invStackSizes[j] + " x " + itemDef.name;
			else
			if(itemDef.description != null)
				s5 = new String(itemDef.description);
			else
				s5 = "It's a " + itemDef.name + ".";
			pushMessage(s5, 0, "");*/
			//examine item
			ItemDef itemDef = ItemDef.forID(i1);
			RSInterface class9_4 = RSInterface.interfaceCache[k];
			String s5;
			if(itemDef != null){
				if (class9_4 != null && class9_4.invStackSizes[j] >= 0x186a0){
					s5 = class9_4.invStackSizes[j] + " x " + itemDef.name;
					pushMessage(s5, 0, "");
				}else{
					stream.createFrame(220);
					stream.writeWord(i1); // ID
				}
			}
		}
		if(l == 169)
		{
			stream.createFrame(185);
			stream.writeWord(k);
			RSInterface class9_3 = RSInterface.interfaceCache[k];
			if(class9_3.valueIndexArray != null && class9_3.valueIndexArray[0][0] == 5)
			{
				int l2 = class9_3.valueIndexArray[0][1];
				variousSettings[l2] = 1 - variousSettings[l2];
				method33(l2);
				needDrawTabArea = true;
			}
		}
		if(l == 447)
		{
			itemSelected = 1;
			anInt1283 = j;
			anInt1284 = k;
			anInt1285 = i1;
			selectedItemName = ItemDef.forID(i1).name;
			spellSelected = 0;
			needDrawTabArea = true;
			return;
		}
		if(l == 1226)
		{
			int j1 = i1 >> 14 & 0x7fff;
			ObjectDef class46 = ObjectDef.forID(j1);
			String s10;
			if(class46.description != null)
				s10 = new String(class46.description);
			else
				s10 = "It's a " + class46.name + ".";
			pushMessage(s10, 0, "");
		}
		if(l == 244)
		{
			boolean flag7 = doWalkTo(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, k, myPlayer.smallX[0], false, j);
			if(!flag7)
				flag7 = doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, k, myPlayer.smallX[0], false, j);
			crossX = super.saveClickX;
			crossY = super.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(253);
			stream.method431(j + baseX);
			stream.method433(k + baseY);
			stream.method432(i1);
		}
		if(l == 1448)
		{
		/*	ItemDef itemDef_1 = ItemDef.forID(i1);
			String s6;
			if(itemDef_1.description != null)
				s6 = new String(itemDef_1.description);
			else
				s6 = "It's a " + itemDef_1.name + ".";
			pushMessage(s6, 0, "");*/
			//EXAMINE ITEM
			ItemDef itemDef_1 = ItemDef.forID(i1);
			if(itemDef_1 != null)
			{
				stream.createFrame(220);
				stream.writeWord(i1); // ID
			}
		}
		itemSelected = 0;
			spellSelected = 0;
			needDrawTabArea = true;

	}

	private void locationCheck()
	{
		anInt1251 = 0;
		int j = (myPlayer.x >> 7) + baseX;
		int k = (myPlayer.y >> 7) + baseY;
		if(j >= 3053 && j <= 3156 && k >= 3056 && k <= 3136)
			anInt1251 = 1;
		if(j >= 3072 && j <= 3118 && k >= 9492 && k <= 9535)
			anInt1251 = 1;
		if(anInt1251 == 1 && j >= 3139 && j <= 3199 && k >= 3008 && k <= 3062)
			anInt1251 = 0;
	}

	public void run()
	{
		if(drawFlames)
		{
			drawFlames();
		} else
		{
			super.run();
		}
	}

	private void build3dScreenMenu()
	{
		if(itemSelected == 0 && spellSelected == 0)
		{
			menuActionName[menuActionRow] = "Walk here";
			menuActionID[menuActionRow] = 516;
			menuActionCmd2[menuActionRow] = super.mouseX;
			menuActionCmd3[menuActionRow] = super.mouseY;
			menuActionRow++;
		}
		int j = -1;
		for(int k = 0; k < Model.anInt1687; k++)
		{
			int l = Model.anIntArray1688[k];
			int i1 = l & 0x7f;
			int j1 = l >> 7 & 0x7f;
			int k1 = l >> 29 & 3;
			int l1 = l >> 14 & 0x7fff;
			if(l == j)
				continue;
			j = l;
			if(k1 == 2 && worldController.method304(plane, i1, j1, l) >= 0)
			{
				ObjectDef class46 = ObjectDef.forID(l1);
				if(class46.childrenIDs != null)
					class46 = class46.method580();
				if(class46 == null)
					continue;
				if(itemSelected == 1)
				{
					menuActionName[menuActionRow] = "Use " + selectedItemName + " with @cya@" + class46.name;
					menuActionID[menuActionRow] = 62;
					menuActionCmd1[menuActionRow] = l;
					menuActionCmd2[menuActionRow] = i1;
					menuActionCmd3[menuActionRow] = j1;
					menuActionRow++;
				} else
				if(spellSelected == 1)
				{
					if((spellUsableOn & 4) == 4)
					{
						menuActionName[menuActionRow] = spellTooltip + " @cya@" + class46.name;
						menuActionID[menuActionRow] = 956;
						menuActionCmd1[menuActionRow] = l;
						menuActionCmd2[menuActionRow] = i1;
						menuActionCmd3[menuActionRow] = j1;
						menuActionRow++;
					}
				} else
				{
					if(class46.actions != null)
					{
						for(int i2 = 4; i2 >= 0; i2--)
							if(class46.actions[i2] != null)
							{
								menuActionName[menuActionRow] = class46.actions[i2] + " @cya@" + class46.name;
								if(i2 == 0)
									menuActionID[menuActionRow] = 502;
								if(i2 == 1)
									menuActionID[menuActionRow] = 900;
								if(i2 == 2)
									menuActionID[menuActionRow] = 113;
								if(i2 == 3)
									menuActionID[menuActionRow] = 872;
								if(i2 == 4)
									menuActionID[menuActionRow] = 1062;
								menuActionCmd1[menuActionRow] = l;
								menuActionCmd2[menuActionRow] = i1;
								menuActionCmd3[menuActionRow] = j1;
								menuActionRow++;
							}

					}
					//menuActionName[menuActionRow] = "Examine @cya@" + class46.name + " @gre@(@whi@" + l1 + "@gre@) (@whi@" + (i1 + baseX) + "," + (j1 + baseY) + "@gre@)";
					menuActionName[menuActionRow] = "Examine @cya@" + class46.name;
					menuActionID[menuActionRow] = 1226;
					menuActionCmd1[menuActionRow] = class46.type << 14;
					menuActionCmd2[menuActionRow] = i1;
					menuActionCmd3[menuActionRow] = j1;
					menuActionRow++;
				}
			}
			if(k1 == 1)
			{
				NPC npc = npcArray[l1];
				if(npc.desc.aByte68 == 1 && (npc.x & 0x7f) == 64 && (npc.y & 0x7f) == 64)
				{
					for(int j2 = 0; j2 < npcCount; j2++)
					{
						NPC npc2 = npcArray[npcIndices[j2]];
						if(npc2 != null && npc2 != npc && npc2.desc.aByte68 == 1 && npc2.x == npc.x && npc2.y == npc.y)
							buildAtNPCMenu(npc2.desc, npcIndices[j2], j1, i1);
					}

					for(int l2 = 0; l2 < playerCount; l2++)
					{
						Player player = playerArray[playerIndices[l2]];
						if(player != null && player.x == npc.x && player.y == npc.y)
							buildAtPlayerMenu(i1, playerIndices[l2], player, j1);
					}

				}
				buildAtNPCMenu(npc.desc, l1, j1, i1);
			}
			if(k1 == 0)
			{
				Player player = playerArray[l1];
				if((player.x & 0x7f) == 64 && (player.y & 0x7f) == 64)
				{
					for(int k2 = 0; k2 < npcCount; k2++)
					{
						NPC class30_sub2_sub4_sub1_sub1_2 = npcArray[npcIndices[k2]];
						if(class30_sub2_sub4_sub1_sub1_2 != null && class30_sub2_sub4_sub1_sub1_2.desc.aByte68 == 1 && class30_sub2_sub4_sub1_sub1_2.x == player.x && class30_sub2_sub4_sub1_sub1_2.y == player.y)
							buildAtNPCMenu(class30_sub2_sub4_sub1_sub1_2.desc, npcIndices[k2], j1, i1);
					}

					for(int i3 = 0; i3 < playerCount; i3++)
					{
						Player class30_sub2_sub4_sub1_sub2_2 = playerArray[playerIndices[i3]];
						if(class30_sub2_sub4_sub1_sub2_2 != null && class30_sub2_sub4_sub1_sub2_2 != player && class30_sub2_sub4_sub1_sub2_2.x == player.x && class30_sub2_sub4_sub1_sub2_2.y == player.y)
							buildAtPlayerMenu(i1, playerIndices[i3], class30_sub2_sub4_sub1_sub2_2, j1);
					}

				}
				buildAtPlayerMenu(i1, l1, player, j1);
			}
			if(k1 == 3)
			{
				NodeList class19 = groundArray[plane][i1][j1];
				if(class19 != null)
				{
					for(Item item = (Item)class19.getFirst(); item != null; item = (Item)class19.getNext())
					{
						ItemDef itemDef = ItemDef.forID(item.ID);
						if(itemSelected == 1)
						{
							menuActionName[menuActionRow] = "Use " + selectedItemName + " with @lre@" + itemDef.name;
							menuActionID[menuActionRow] = 511;
							menuActionCmd1[menuActionRow] = item.ID;
							menuActionCmd2[menuActionRow] = i1;
							menuActionCmd3[menuActionRow] = j1;
							menuActionRow++;
						} else
						if(spellSelected == 1)
						{
							if((spellUsableOn & 1) == 1)
							{
								menuActionName[menuActionRow] = spellTooltip + " @lre@" + itemDef.name;
								menuActionID[menuActionRow] = 94;
								menuActionCmd1[menuActionRow] = item.ID;
								menuActionCmd2[menuActionRow] = i1;
								menuActionCmd3[menuActionRow] = j1;
								menuActionRow++;
							}
						} else
						{
							for(int j3 = 4; j3 >= 0; j3--)
								if(itemDef.groundActions != null && itemDef.groundActions[j3] != null)
								{
									menuActionName[menuActionRow] = itemDef.groundActions[j3] + " @lre@" + itemDef.name;
									if(j3 == 0)
										menuActionID[menuActionRow] = 652;
									if(j3 == 1)
										menuActionID[menuActionRow] = 567;
									if(j3 == 2)
										menuActionID[menuActionRow] = 234;
									if(j3 == 3)
										menuActionID[menuActionRow] = 244;
									if(j3 == 4)
										menuActionID[menuActionRow] = 213;
									menuActionCmd1[menuActionRow] = item.ID;
									menuActionCmd2[menuActionRow] = i1;
									menuActionCmd3[menuActionRow] = j1;
									menuActionRow++;
								} else
								if(j3 == 2)
								{
									menuActionName[menuActionRow] = "Take @lre@" + itemDef.name;
									menuActionID[menuActionRow] = 234;
									menuActionCmd1[menuActionRow] = item.ID;
									menuActionCmd2[menuActionRow] = i1;
									menuActionCmd3[menuActionRow] = j1;
									menuActionRow++;
								}

							//menuActionName[menuActionRow] = "Examine @lre@" + itemDef.name + " @gre@(@whi@" + item.ID + "@gre@)";
							menuActionName[menuActionRow] = "Examine @lre@" + itemDef.name;
							menuActionID[menuActionRow] = 1448;
							menuActionCmd1[menuActionRow] = item.ID;
							menuActionCmd2[menuActionRow] = i1;
							menuActionCmd3[menuActionRow] = j1;
							menuActionRow++;
						}
					}

				}
			}
		}
	}

	public void cleanUpForQuit()
	{
		signlink.reporterror = false;
		try
		{
			if(socketStream != null)
				socketStream.close();
		}
		catch(Exception _ex) { }
		socketStream = null;
		stopMidi();
		if(mouseDetection != null)
			mouseDetection.running = false;
		mouseDetection = null;
		onDemandFetcher.disable();
		onDemandFetcher = null;
		aStream_834 = null;
		stream = null;
		aStream_847 = null;
		inStream = null;
		anIntArray1234 = null;
		aByteArrayArray1183 = null;
		aByteArrayArray1247 = null;
		anIntArray1235 = null;
		anIntArray1236 = null;
		intGroundArray = null;
		byteGroundArray = null;
		worldController = null;
		aClass11Array1230 = null;
		anIntArrayArray901 = null;
		anIntArrayArray825 = null;
		bigX = null;
		bigY = null;
		aByteArray912 = null;
		aRSImageProducer_1163 = null;
		aRSImageProducer_1164 = null;
		aRSImageProducer_1165 = null;
		aRSImageProducer_1166 = null;
		aRSImageProducer_1123 = null;
		aRSImageProducer_1124 = null;
		aRSImageProducer_1125 = null;
		/* Null pointers for custom sprites */
		chatArea = null;
		chatButtons = null;
		chatButtonH = null;
		chatButtonHC = null;
		chatButtonC = null;
		reportH = null;
		tabArea = null;
		mapArea = null;
		frame = null;
		/**/
		mapBack = null;
		backBase1 = null;
		sideIcons = null;
		redStones = null;
		compass = null;
		hitMarks = null;
		headIcons = null;
		headIconsHint = null;
		crosses = null;
		mapDotItem = null;
		mapDotNPC = null;
		mapDotPlayer = null;
		mapDotFriend = null;
		mapDotTeam = null;
		mapScenes = null;
		mapFunctions = null;
		anIntArrayArray929 = null;
		playerArray = null;
		playerIndices = null;
		anIntArray894 = null;
		aStreamArray895s = null;
		anIntArray840 = null;
		npcArray = null;
		npcIndices = null;
		groundArray = null;
		aClass19_1179 = null;
		aClass19_1013 = null;
		aClass19_1056 = null;
		menuActionCmd2 = null;
		menuActionCmd3 = null;
		menuActionID = null;
		menuActionCmd1 = null;
		menuActionName = null;
		variousSettings = null;
		anIntArray1072 = null;
		anIntArray1073 = null;
		aClass30_Sub2_Sub1_Sub1Array1140 = null;
		aClass30_Sub2_Sub1_Sub1_1263 = null;
		friendsList = null;
		friendsListAsLongs = null;
		friendsNodeIDs = null;
		aRSImageProducer_1110 = null;
		aRSImageProducer_1111 = null;
		aRSImageProducer_1107 = null;
		aRSImageProducer_1108 = null;
		aRSImageProducer_1109 = null;
		aRSImageProducer_1112 = null;
		aRSImageProducer_1113 = null;
		aRSImageProducer_1114 = null;
		aRSImageProducer_1115 = null;
		nullLoader();
		ObjectDef.nullLoader();
		EntityDef.nullLoader();
		ItemDef.nullLoader();
		Flo.cache = null;
		IDK.cache = null;
		RSInterface.interfaceCache = null;
		DummyClass.cache = null;
		Animation.anims = null;
		SpotAnim.cache = null;
		SpotAnim.aMRUNodes_415 = null;
		Varp.cache = null;
		super.fullGameScreen = null;
		Player.mruNodes = null;
		Texture.nullLoader();
		WorldController.nullLoader();
		Model.nullLoader();
		Class36.nullLoader();
		System.gc();
	}

	private void printDebug()
	{
		System.out.println("============");
		System.out.println("flame-cycle:" + anInt1208);
		if(onDemandFetcher != null)
			System.out.println("Od-cycle:" + onDemandFetcher.onDemandCycle);
		System.out.println("loop-cycle:" + loopCycle);
		System.out.println("draw-cycle:" + anInt1061);
		System.out.println("ptype:" + pktType);
		System.out.println("psize:" + pktSize);
		if(socketStream != null)
			socketStream.printDebug();
		super.shouldDebug = false;
	}

	Component getGameComponent()
	{
		if(signlink.mainapp != null)
			return signlink.mainapp;
		if(super.gameFrame != null)
			return super.gameFrame;
		else
			return this;
	}
	
	public String indexLocation(int cacheIndex, int index) {
		return signlink.findcachedir() + "index" + cacheIndex + "/" + (index != -1 ? index + ".gz" : "");
	}

	public void repackCacheIndex(int cacheIndex) {
		System.out.println("Started repacking index " + cacheIndex + ".");
		int indexLength = new File(indexLocation(cacheIndex, -1)).listFiles().length;
		File[] file = new File(indexLocation(cacheIndex, -1)).listFiles();
		try {
			for (int index = 0; index < indexLength; index++) {
				int fileIndex = Integer.parseInt(getFileNameWithoutExtension(file[index].toString()));
				byte[] data = fileToByteArray(cacheIndex, fileIndex);
				if(data != null && data.length > 0) {
					decompressors[cacheIndex].method234(data.length, data, fileIndex);
					System.out.println("Repacked " + fileIndex + ".");
				} else {
					System.out.println("Unable to locate index " + fileIndex + ".");
				}
			}
		} catch(Exception e) {
			System.out.println("Error packing cache index " + cacheIndex + ".");
		}
		System.out.println("Finished repacking " + cacheIndex + ".");
	}

	public byte[] fileToByteArray(int cacheIndex, int index) {
		try {
			if (indexLocation(cacheIndex, index).length() <= 0 || indexLocation(cacheIndex, index) == null) {
				return null;
			}
			File file = new File(indexLocation(cacheIndex, index));
			byte[] fileData = new byte[(int)file.length()];
			FileInputStream fis = new FileInputStream(file);
			fis.read(fileData);
			fis.close();
			return fileData;
		} catch(Exception e) {
			return null;
		}
	}

	public byte[] getModel(int Index) {
		try {
			File Model = new File(signlink.findcachedir() + "/Models/"+Index+".gz");
			byte[] aByte = new byte[(int)Model.length()];
			FileInputStream fis = new FileInputStream(Model);
			fis.read(aByte);
			pushMessage("aByte = ["+aByte+"]!", 0, "");
			fis.close();
			return aByte;
		}
		catch(Exception e)
		{return null;}
	}

	private void method73() {
		do {
			int j = readChar(-796);
			if(j == -1)
				break;
			if(openInterfaceID != -1 && openInterfaceID == reportAbuseInterfaceID) {
				if(j == 8 && reportAbuseInput.length() > 0)
					reportAbuseInput = reportAbuseInput.substring(0, reportAbuseInput.length() - 1);
				if((j >= 97 && j <= 122 || j >= 65 && j <= 90 || j >= 48 && j <= 57 || j == 32) && reportAbuseInput.length() < 12)
					reportAbuseInput += (char)j;
			} else if(messagePromptRaised) {
				if(j >= 32 && j <= 122 && promptInput.length() < 80) {
					promptInput += (char)j;
					inputTaken = true;
				}
				if(j == 8 && promptInput.length() > 0) {
					promptInput = promptInput.substring(0, promptInput.length() - 1);
					inputTaken = true;
				}
				if(j == 13 || j == 10) {
					messagePromptRaised = false;
					inputTaken = true;
					if(friendsListAction == 1)
					{
						long l = TextClass.longForName(promptInput);
						addFriend(l);
					}
					if(friendsListAction == 2 && friendsCount > 0)
					{
						long l1 = TextClass.longForName(promptInput);
						delFriend(l1);
					}
					if(friendsListAction == 3 && promptInput.length() > 0)
					{
						stream.createFrame(126);
						stream.writeWordBigEndian(0);
						int k = stream.currentOffset;
						stream.writeQWord(aLong953);
						TextInput.method526(promptInput, stream);
						stream.writeBytes(stream.currentOffset - k);
						promptInput = TextInput.processText(promptInput);
					//	promptInput = Censor.doCensor(promptInput);
						pushMessage(promptInput, 6, TextClass.fixName(TextClass.nameForLong(aLong953)));
						if(privateChatMode == 2)
						{
							privateChatMode = 1;
							aBoolean1233 = true;
							stream.createFrame(95);
							stream.writeWordBigEndian(publicChatMode);
							stream.writeWordBigEndian(privateChatMode);
							stream.writeWordBigEndian(tradeMode);
						}
					}
					if(friendsListAction == 4 && ignoreCount < 100)
					{
						long l2 = TextClass.longForName(promptInput);
						addIgnore(l2);
					}
					if(friendsListAction == 5 && ignoreCount > 0)
					{
						long l3 = TextClass.longForName(promptInput);
						delIgnore(l3);
					}
				}
			} else if (inputDialogState == 1) {
				if (j >= 48 && j <= 57 && amountOrNameInput.length() < 10) {
					amountOrNameInput += (char) j;
					inputTaken = true;
				}
				if ((!amountOrNameInput.toLowerCase().contains("k") && !amountOrNameInput.toLowerCase().contains("m") && !amountOrNameInput.toLowerCase().contains("b")) && (j == 107 || j == 109) || j == 98) {
					amountOrNameInput += (char) j;
					inputTaken = true;
				}
				if (j == 8 && amountOrNameInput.length() > 0) {
					amountOrNameInput = amountOrNameInput.substring(0, amountOrNameInput.length() - 1);
					inputTaken = true;
				}
				if (j == 13 || j == 10) {
					if (amountOrNameInput.length() > 0) {
						if (amountOrNameInput.toLowerCase().contains("k")) {
							amountOrNameInput = amountOrNameInput.replaceAll("k", "000");
						} else if (amountOrNameInput.toLowerCase().contains("m")) {
							amountOrNameInput = amountOrNameInput.replaceAll("m", "000000");
						} else if (amountOrNameInput.toLowerCase().contains("b")) {
							amountOrNameInput = amountOrNameInput.replaceAll("b", "000000000");
						}
						int amount = 0;
						amount = Integer.parseInt(amountOrNameInput);
						stream.createFrame(208);
						stream.writeDWord(amount);
					}
					inputDialogState = 0;
					inputTaken = true;
				}
			} else if(inputDialogState == 2) {
				if(j >= 32 && j <= 122 && amountOrNameInput.length() < 12)
				{
					amountOrNameInput += (char)j;
					inputTaken = true;
				}
				if(j == 8 && amountOrNameInput.length() > 0)
				{
					amountOrNameInput = amountOrNameInput.substring(0, amountOrNameInput.length() - 1);
					inputTaken = true;
				}
				if(j == 13 || j == 10)
				{
					if(amountOrNameInput.length() > 0)
					{
						stream.createFrame(60);
						stream.writeQWord(TextClass.longForName(amountOrNameInput));
					}
					inputDialogState = 0;
					inputTaken = true;
				}
			} else
			if(backDialogID == -1)
			{
				if(j >= 32 && j <= 122 && inputString.length() < 80)
				{
					inputString += (char)j;
					inputTaken = true;
				}
				if(j == 8 && inputString.length() > 0)
				{
					inputString = inputString.substring(0, inputString.length() - 1);
					inputTaken = true;
				}
				if((j == 13 || j == 10) && inputString.length() > 0)
				{
					if(myPrivilege == 2)
					{
					  if(inputString.equals("::clientdrop"))
							dropClient();
						if(inputString.equals("::lag"))
							printDebug();
						if(inputString.equals("::prefetchmusic"))
						{
							for(int j1 = 0; j1 < onDemandFetcher.getVersionCount(2); j1++)
								onDemandFetcher.method563((byte)1, 2, j1);

						}
						if(inputString.equals("::fpson"))
							fpsOn = true;
						if(inputString.equals("::fpsoff"))
							fpsOn = false;
						if(inputString.equals("::dataon"))
							clientData = true;
						if(inputString.equals("::dataoff"))
							clientData = false;
						if(inputString.equals("::noclip"))
						{
							for(int k1 = 0; k1 < 4; k1++)
							{
								for(int i2 = 1; i2 < 103; i2++)
								{
									for(int k2 = 1; k2 < 103; k2++)
										aClass11Array1230[k1].anIntArrayArray294[i2][k2] = 0;

								}

							}

						}
					}
					if(inputString.startsWith("::"))
					{
						stream.createFrame(103);
						stream.writeWordBigEndian(inputString.length() - 1);
						stream.writeString(inputString.substring(2));
					} else
					{
						String s = inputString.toLowerCase();	
						int j2 = 0;
						if(s.startsWith("yellow:"))
						{
							j2 = 0;
							inputString = inputString.substring(7);
						} else
						if(s.startsWith("red:"))
						{
							j2 = 1;
							inputString = inputString.substring(4);
						} else
						if(s.startsWith("green:"))
						{
							j2 = 2;
							inputString = inputString.substring(6);
						} else
						if(s.startsWith("cyan:"))
						{
							j2 = 3;
							inputString = inputString.substring(5);
						} else
						if(s.startsWith("purple:"))
						{
							j2 = 4;
							inputString = inputString.substring(7);
						} else
						if(s.startsWith("white:"))
						{
							j2 = 5;
							inputString = inputString.substring(6);
						} else
						if(s.startsWith("flash1:"))
						{
							j2 = 6;
							inputString = inputString.substring(7);
						} else
						if(s.startsWith("flash2:"))
						{
							j2 = 7;
							inputString = inputString.substring(7);
						} else
						if(s.startsWith("flash3:"))
						{
							j2 = 8;
							inputString = inputString.substring(7);
						} else
						if(s.startsWith("glow1:"))
						{
							j2 = 9;
							inputString = inputString.substring(6);
						} else
						if(s.startsWith("glow2:"))
						{
							j2 = 10;
							inputString = inputString.substring(6);
						} else
						if(s.startsWith("glow3:"))
						{
							j2 = 11;
							inputString = inputString.substring(6);
						}
						s = inputString.toLowerCase();
						int i3 = 0;
						if(s.startsWith("wave:"))
						{
							i3 = 1;
							inputString = inputString.substring(5);
						} else
						if(s.startsWith("wave2:"))
						{
							i3 = 2;
							inputString = inputString.substring(6);
						} else
						if(s.startsWith("shake:"))
						{
							i3 = 3;
							inputString = inputString.substring(6);
						} else
						if(s.startsWith("scroll:"))
						{
							i3 = 4;
							inputString = inputString.substring(7);
						} else
						if(s.startsWith("slide:"))
						{
							i3 = 5;
							inputString = inputString.substring(6);
						}
						stream.createFrame(4);
						stream.writeWordBigEndian(0);
						int j3 = stream.currentOffset;
						stream.method425(i3);
						stream.method425(j2);
						aStream_834.currentOffset = 0;
						TextInput.method526(inputString, aStream_834);
						stream.method441(0, aStream_834.buffer, aStream_834.currentOffset);
						stream.writeBytes(stream.currentOffset - j3);
						inputString = TextInput.processText(inputString);
					//	inputString = Censor.doCensor(inputString);
						myPlayer.textSpoken = inputString;
						myPlayer.anInt1513 = j2;
						myPlayer.anInt1531 = i3;
						myPlayer.textCycle = 150;
						if(myPrivilege == 2)
							pushMessage(myPlayer.textSpoken, 2, "@cr2@" + myPlayer.name);
						else
						if(myPrivilege == 1)
							pushMessage(myPlayer.textSpoken, 2, "@cr1@" + myPlayer.name);
						else
							pushMessage(myPlayer.textSpoken, 2, myPlayer.name);
						if(publicChatMode == 2)
						{
							publicChatMode = 3;
							aBoolean1233 = true;
							stream.createFrame(95);
							stream.writeWordBigEndian(publicChatMode);
							stream.writeWordBigEndian(privateChatMode);
							stream.writeWordBigEndian(tradeMode);
						}
					}
					inputString = "";
					inputTaken = true;
				}
			}
		} while(true);
	}

	private void buildPublicChat(int j)
	{
		int l = 0;
		for(int i1 = 0; i1 < 500; i1++)
		{
			if(chatMessages[i1] == null)
				continue;
			if(chatTypeView != 1)
				continue;
			int j1 = chatTypes[i1];
			String s = chatNames[i1];
			String ct = chatMessages[i1];
			int k1 = (70 - l * 14 + 42) + anInt1089 + 4 + 5;
			if(k1 < -23)
				break;
			if(s != null && s.startsWith("@cr1@"))
				s = s.substring(5);
			if(s != null && s.startsWith("@cr2@"))
				s = s.substring(5);
			if(s != null && s.startsWith("@cr3@"))
				s = s.substring(5);
			if((j1 == 1 || j1 == 2) && (j1 == 1 || publicChatMode == 0 || publicChatMode == 1 && isFriendOrSelf(s))) {
			if(j > k1 - 14 && j <= k1 && !s.equals(myPlayer.name)) {
				if(myPrivilege >= 1) {
					menuActionName[menuActionRow] = "Report abuse @whi@" + s;
					menuActionID[menuActionRow] = 606;
					menuActionRow++;
				}
				menuActionName[menuActionRow] = "Add ignore @whi@" + s;
				menuActionID[menuActionRow] = 42;
				menuActionRow++;
				menuActionName[menuActionRow] = "Add friend @whi@" + s;
				menuActionID[menuActionRow] = 337;
				menuActionRow++;
			}
			l++;
			}
		}
	}

	private void buildFriendChat(int j)
	{
		int l = 0;
		for(int i1 = 0; i1 < 500; i1++) {
			if(chatMessages[i1] == null)
				continue;
			if(chatTypeView != 2)
				continue;
			int j1 = chatTypes[i1];
			String s = chatNames[i1];
			String ct = chatMessages[i1];
			int k1 = (70 - l * 14 + 42) + anInt1089 + 4 + 5;
			if(k1 < -23)
				break;
			if(s != null && s.startsWith("@cr1@"))
				s = s.substring(5);
			if(s != null && s.startsWith("@cr2@"))
				s = s.substring(5);
			if(s != null && s.startsWith("@cr3@"))
				s = s.substring(5);
			if((j1 == 5 || j1 == 6) && (splitPrivateChat == 0 || chatTypeView == 2) && (j1 == 6 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(s)))
				l++;
			if((j1 == 3 || j1 == 7) && (splitPrivateChat == 0 || chatTypeView == 2) && (j1 == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(s)))
			{
				if(j > k1 - 14 && j <= k1) {
					if(myPrivilege >= 1) {
						menuActionName[menuActionRow] = "Report abuse @whi@" + s;
						menuActionID[menuActionRow] = 606;
						menuActionRow++;
					}
					menuActionName[menuActionRow] = "Add ignore @whi@" + s;
					menuActionID[menuActionRow] = 42;
					menuActionRow++;
					menuActionName[menuActionRow] = "Add friend @whi@" + s;
					menuActionID[menuActionRow] = 337;
					menuActionRow++;
				}
			l++;
			}
		}
	}

	private void buildDuelorTrade(int j)
	{
		int l = 0;
		for(int i1 = 0; i1 < 500; i1++)
		{
			if(chatMessages[i1] == null)
				continue;
			if(chatTypeView != 3 && chatTypeView != 4)
				continue;
			int j1 = chatTypes[i1];
			String s = chatNames[i1];
			int k1 = (70 - l * 14 + 42) + anInt1089 + 4 + 5;
			if(k1 < -23)
				break;
			if(s != null && s.startsWith("@cr1@"))
				s = s.substring(5);
			if(s != null && s.startsWith("@cr2@"))
				s = s.substring(5);
			if(s != null && s.startsWith("@cr3@"))
				s = s.substring(5);
			if(chatTypeView == 3 && j1 == 4 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s))) {
				if(j > k1 - 14 && j <= k1) {
					menuActionName[menuActionRow] = "Accept trade @whi@" + s;
					menuActionID[menuActionRow] = 484;
					menuActionRow++;
				}
			l++;
			}
			if(chatTypeView == 4 && j1 == 8 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s))) {
				if(j > k1 - 14 && j <= k1) {
					menuActionName[menuActionRow] = "Accept challenge @whi@" + s;
					menuActionID[menuActionRow] = 6;
					menuActionRow++;
				}
			l++;
			}
			if(j1 == 12) {
				if(j > k1 - 14 && j <= k1) {
					menuActionName[menuActionRow] = "Go-to @blu@" + s;
					menuActionID[menuActionRow] = 915;
					menuActionRow++;
				}
				l++;
			}
		}
	}

	private void buildChatAreaMenu(int j)
	{
		int l = 0;
		int test = 0;
		for(int i1 = 0; i1 < 500; i1++)
		{
			if(chatMessages[i1] == null)
			continue;
			int j1 = chatTypes[i1];
			int k1 = (70 - l * 14 + 42) + anInt1089 + 4 + 5;
			if(k1 < -23)
			break;
			String s = chatNames[i1];
			String ct = chatMessages[i1];
			boolean flag = false;
			if(chatTypeView == 1) {
				buildPublicChat(j);
				break;
			}
			if(chatTypeView == 2) {
				buildFriendChat(j);
				break;
			}
			if(chatTypeView == 3 || chatTypeView == 4) {
				buildDuelorTrade(j);
				break;
			}
			if(chatTypeView == 5) {
				break;
			}
			if(s != null && s.startsWith("@cr1@")) {
				s = s.substring(5);
				boolean flag1 = true;
				byte byte0 = 1;
			}
			if(s != null && s.startsWith("@cr2@")) {
				s = s.substring(5);
				byte byte0 = 2;
			}
			if(s != null && s.startsWith("@cr3@")) {
				s = s.substring(5);
				byte byte0 = 3;
			}
			if(j1 == 0)
				l++;
			if((j1 == 1 || j1 == 2) && (j1 == 1 || publicChatMode == 0 || publicChatMode == 1 && isFriendOrSelf(s)))
			{
				if(j > k1 - 14 && j <= k1 && !s.equals(myPlayer.name))
				{
					if(myPrivilege >= 1)
					{
						menuActionName[menuActionRow] = "Report abuse @whi@" + s;
						menuActionID[menuActionRow] = 606;
						menuActionRow++;
					}
					menuActionName[menuActionRow] = "Add ignore @whi@" + s;
					menuActionID[menuActionRow] = 42;
					menuActionRow++;
					menuActionName[menuActionRow] = "Add friend @whi@" + s;
					menuActionID[menuActionRow] = 337;
					menuActionRow++;
				}
			l++;
			}
			if((j1 == 3 || j1 == 7) && splitPrivateChat == 0 && (j1 == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(s)))
			{
				if(j > k1 - 14 && j <= k1)
				{
					if(myPrivilege >= 1)
					{
						menuActionName[menuActionRow] = "Report abuse @whi@" + s;
						menuActionID[menuActionRow] = 606;
						menuActionRow++;
					}
					menuActionName[menuActionRow] = "Add ignore @whi@" + s;
					menuActionID[menuActionRow] = 42;
					menuActionRow++;
					menuActionName[menuActionRow] = "Add friend @whi@" + s;
					menuActionID[menuActionRow] = 337;
					menuActionRow++;
				}
			l++;
			}
			if(j1 == 4 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s)))
			{
				if(j > k1 - 14 && j <= k1)
				{
					menuActionName[menuActionRow] = "Accept trade @whi@" + s;
					menuActionID[menuActionRow] = 484;
					menuActionRow++;
				}
			l++;
			}
			if((j1 == 5 || j1 == 6) && splitPrivateChat == 0 && privateChatMode < 2)
				l++;
			if(j1 == 8 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s)))
			{
				if(j > k1 - 14 && j <= k1)
				{
					menuActionName[menuActionRow] = "Accept challenge @whi@" + s;
					menuActionID[menuActionRow] = 6;
					menuActionRow++;
				}
			l++;
			}
		}
	}

	private void drawFriendsListOrWelcomeScreen(RSInterface class9)
	{
		int j = class9.contentType;
		if(j >= 1 && j <= 100 || j >= 701 && j <= 800)
		{
			if(j == 1 && anInt900 == 0)
			{
				class9.message = "Loading friend list";
				class9.atActionType = 0;
				return;
			}
			if(j == 1 && anInt900 == 1)
			{
				class9.message = "Connecting to friendserver";
				class9.atActionType = 0;
				return;
			}
			if(j == 2 && anInt900 != 2)
			{
				class9.message = "Please wait...";
				class9.atActionType = 0;
				return;
			}
			int k = friendsCount;
			if(anInt900 != 2)
				k = 0;
			if(j > 700)
				j -= 601;
			else
				j--;
			if(j >= k)
			{
				class9.message = "";
				class9.atActionType = 0;
				return;
			} else
			{
				class9.message = friendsList[j];
				class9.atActionType = 1;
				return;
			}
		}
		if(j >= 101 && j <= 200 || j >= 801 && j <= 900)
		{
			int l = friendsCount;
			if(anInt900 != 2)
				l = 0;
			if(j > 800)
				j -= 701;
			else
				j -= 101;
			if(j >= l)
			{
				class9.message = "";
				class9.atActionType = 0;
				return;
			}
			if(friendsNodeIDs[j] == 0)
				class9.message = "@red@Offline";
			else if(friendsNodeIDs[j] == nodeID)
				class9.message = "@gre@Online"/* + (friendsNodeIDs[j] - 9)*/;
			else
				class9.message = "@red@Offline"/* + (friendsNodeIDs[j] - 9)*/;
			class9.atActionType = 1;
			return;
		}
		if(j == 203)
		{
			int i1 = friendsCount;
			if(anInt900 != 2)
				i1 = 0;
			class9.scrollMax = i1 * 15 + 20;
			if(class9.scrollMax <= class9.height)
				class9.scrollMax = class9.height + 1;
			return;
		}
		if(j >= 401 && j <= 500)
		{
			if((j -= 401) == 0 && anInt900 == 0)
			{
				class9.message = "Loading ignore list";
				class9.atActionType = 0;
				return;
			}
			if(j == 1 && anInt900 == 0)
			{
				class9.message = "Please wait...";
				class9.atActionType = 0;
				return;
			}
			int j1 = ignoreCount;
			if(anInt900 == 0)
				j1 = 0;
			if(j >= j1)
			{
				class9.message = "";
				class9.atActionType = 0;
				return;
			} else
			{
				class9.message = TextClass.fixName(TextClass.nameForLong(ignoreListAsLongs[j]));
				class9.atActionType = 1;
				return;
			}
		}
		if(j == 503)
		{
			class9.scrollMax = ignoreCount * 15 + 20;
			if(class9.scrollMax <= class9.height)
				class9.scrollMax = class9.height + 1;
			return;
		}
		if(j == 327)
		{
			class9.modelRotation1 = 150;
			class9.modelRotation2 = (int)(Math.sin((double)loopCycle / 40D) * 256D) & 0x7ff;
			if(aBoolean1031)
			{
				for(int k1 = 0; k1 < 7; k1++)
				{
					int l1 = anIntArray1065[k1];
					if(l1 >= 0 && !IDK.cache[l1].method537())
						return;
				}

				aBoolean1031 = false;
				Model aclass30_sub2_sub4_sub6s[] = new Model[7];
				int i2 = 0;
				for(int j2 = 0; j2 < 7; j2++)
				{
					int k2 = anIntArray1065[j2];
					if(k2 >= 0)
						aclass30_sub2_sub4_sub6s[i2++] = IDK.cache[k2].method538();
				}

				Model model = new Model(i2, aclass30_sub2_sub4_sub6s);
				for(int l2 = 0; l2 < 5; l2++)
					if(anIntArray990[l2] != 0)
					{
						model.method476(anIntArrayArray1003[l2][0], anIntArrayArray1003[l2][anIntArray990[l2]]);
						if(l2 == 1)
							model.method476(anIntArray1204[0], anIntArray1204[anIntArray990[l2]]);
					}

				model.method469();
				model.method470(Animation.anims[myPlayer.anInt1511].anIntArray353[0]);
				model.method479(64, 850, -30, -50, -30, true);
				class9.anInt233 = 5;
				class9.mediaID = 0;
				RSInterface.method208(aBoolean994, model);
			}
			return;
		}
		if(j == 324)
		{
			if(aClass30_Sub2_Sub1_Sub1_931 == null)
			{
				aClass30_Sub2_Sub1_Sub1_931 = class9.sprite1;
				aClass30_Sub2_Sub1_Sub1_932 = class9.sprite2;
			}
			if(aBoolean1047)
			{
				class9.sprite1 = aClass30_Sub2_Sub1_Sub1_932;
				return;
			} else
			{
				class9.sprite1 = aClass30_Sub2_Sub1_Sub1_931;
				return;
			}
		}
		if(j == 325)
		{
			if(aClass30_Sub2_Sub1_Sub1_931 == null)
			{
				aClass30_Sub2_Sub1_Sub1_931 = class9.sprite1;
				aClass30_Sub2_Sub1_Sub1_932 = class9.sprite2;
			}
			if(aBoolean1047)
			{
				class9.sprite1 = aClass30_Sub2_Sub1_Sub1_931;
				return;
			} else
			{
				class9.sprite1 = aClass30_Sub2_Sub1_Sub1_932;
				return;
			}
		}
		if(j == 600)
		{
			class9.message = reportAbuseInput;
			if(loopCycle % 20 < 10)
			{
				class9.message += "|";
				return;
			} else
			{
				class9.message += " ";
				return;
			}
		}
		if(j == 613)
			if(myPrivilege >= 1)
			{
				if(canMute)
				{
					class9.textColor = 0xff0000;
					class9.message = "Moderator option: Mute player for 48 hours: <ON>";
				} else
				{
					class9.textColor = 0xffffff;
					class9.message = "Moderator option: Mute player for 48 hours: <OFF>";
				}
			} else
			{
				class9.message = "";
			}
		if(j == 650 || j == 655)
			if(anInt1193 != 0)
			{
				String s;
				if(daysSinceLastLogin == 0)
					s = "earlier today";
				else
				if(daysSinceLastLogin == 1)
					s = "yesterday";
				else
					s = daysSinceLastLogin + " days ago";
				class9.message = "You last logged in " + s + " from: " + signlink.dns;
			} else
			{
				class9.message = "";
			}
		if(j == 651)
		{
			if(unreadMessages == 0)
			{
				class9.message = "0 unread messages";
				class9.textColor = 0xffff00;
			}
			if(unreadMessages == 1)
			{
				class9.message = "1 unread message";
				class9.textColor = 65280;
			}
			if(unreadMessages > 1)
			{
				class9.message = unreadMessages + " unread messages";
				class9.textColor = 65280;
			}
		}
		if(j == 652)
			if(daysSinceRecovChange == 201)
			{
				if(membersInt == 1)
					class9.message = "@yel@This is a non-members world: @whi@Since you are a member we";
				else
					class9.message = "";
			} else
			if(daysSinceRecovChange == 200)
			{
				class9.message = "You have not yet set any password recovery questions.";
			} else
			{
				String s1;
				if(daysSinceRecovChange == 0)
					s1 = "Earlier today";
				else
				if(daysSinceRecovChange == 1)
					s1 = "Yesterday";
				else
					s1 = daysSinceRecovChange + " days ago";
				class9.message = s1 + " you changed your recovery questions";
			}
		if(j == 653)
			if(daysSinceRecovChange == 201)
			{
				if(membersInt == 1)
					class9.message = "@whi@recommend you use a members world instead. You may use";
				else
					class9.message = "";
			} else
			if(daysSinceRecovChange == 200)
				class9.message = "We strongly recommend you do so now to secure your account.";
			else
				class9.message = "If you do not remember making this change then cancel it immediately";
		if(j == 654)
		{
			if(daysSinceRecovChange == 201)
				if(membersInt == 1)
				{
					class9.message = "@whi@this world but member benefits are unavailable whilst here.";
					return;
				} else
				{
					class9.message = "";
					return;
				}
			if(daysSinceRecovChange == 200)
			{
				class9.message = "Do this from the 'account management' area on our front webpage";
				return;
			}
			class9.message = "Do this from the 'account management' area on our front webpage";
		}
	}

	private void drawSplitPrivateChat()
	{
		if(splitPrivateChat == 0)
			return;
		TextDrawingArea textDrawingArea = aTextDrawingArea_1271;
		int i = 0;
		if(anInt1104 != 0)
			i = 1;
		for(int j = 0; j < 100; j++)
			if(chatMessages[j] != null)
			{
				int k = chatTypes[j];
				String s = chatNames[j];
				byte byte1 = 0;
				if(s != null && s.startsWith("@cr1@"))
				{
					s = s.substring(5);
					byte1 = 1;
				}
				if(s != null && s.startsWith("@cr2@"))
				{
					s = s.substring(5);
					byte1 = 2;
				}
				if((k == 3 || k == 7) && (k == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(s)))
				{
					int l = 329 - i * 13;
					int k1 = 4;
					textDrawingArea.method385(0, "From", l, k1);
					textDrawingArea.method385(65535, "From", l - 1, k1);
					k1 += textDrawingArea.getTextWidth("From ");
					if(byte1 == 1)
					{
						modIcons[0].method361(k1, l - 12);
						k1 += 12;
					}
					if(byte1 == 2)
					{
						modIcons[1].method361(k1, l - 12);
						k1 += 12;
					}
					textDrawingArea.method385(0, s + ": " + chatMessages[j], l, k1);
					textDrawingArea.method385(65535, s + ": " + chatMessages[j], l - 1, k1);
					if(++i >= 5)
						return;
				}
				if(k == 5 && privateChatMode < 2)
				{
					int i1 = 329 - i * 13;
					textDrawingArea.method385(0, chatMessages[j], i1, 4);
					textDrawingArea.method385(65535, chatMessages[j], i1 - 1, 4);
					if(++i >= 5)
						return;
				}
				if(k == 6 && privateChatMode < 2)
				{
					int j1 = 329 - i * 13;
					textDrawingArea.method385(0, "To " + s + ": " + chatMessages[j], j1, 4);
					textDrawingArea.method385(65535, "To " + s + ": " + chatMessages[j], j1 - 1, 4);
					if(++i >= 5)
						return;
				}
			}

	}

	public void pushMessage(String s, int i, String s1) {
		if(i == 0 && dialogID != -1) {
			aString844 = s;
			super.clickMode3 = 0;
		}
		if(backDialogID == -1)
			inputTaken = true;
		for(int j = 499; j > 0; j--) {
			chatTypes[j] = chatTypes[j - 1];
			chatNames[j] = chatNames[j - 1];
			chatMessages[j] = chatMessages[j - 1];
		}
		chatTypes[0] = i;
		chatNames[0] = s1;
		chatMessages[0] = s;
	}

	public void processMapAreaClick() {
	}

	private void processTabClick() {
		if(super.clickMode3 == 1) {
			if(super.saveClickX >= 524 && super.saveClickX <= 561 && super.saveClickY >= 169 && super.saveClickY < 205 && tabInterfaceIDs[0] != -1)
			{
				needDrawTabArea = true;
				tabID = 0;
				tabAreaAltered = true;
			}
			if(super.saveClickX >= 562 && super.saveClickX <= 594 && super.saveClickY >= 168 && super.saveClickY < 205 && tabInterfaceIDs[1] != -1)
			{
				needDrawTabArea = true;
				tabID = 1;
				tabAreaAltered = true;
			}
			if(super.saveClickX >= 595 && super.saveClickX <= 626 && super.saveClickY >= 168 && super.saveClickY < 205 && tabInterfaceIDs[2] != -1)
			{
				needDrawTabArea = true;
				tabID = 2;
				tabAreaAltered = true;
			}
			if(super.saveClickX >= 627 && super.saveClickX <= 660 && super.saveClickY >= 168 && super.saveClickY < 203 && tabInterfaceIDs[3] != -1)
			{
				needDrawTabArea = true;
				tabID = 3;
				tabAreaAltered = true;
			}
			if(super.saveClickX >= 661 && super.saveClickX <= 693 && super.saveClickY >= 168 && super.saveClickY < 205 && tabInterfaceIDs[4] != -1)
			{
				needDrawTabArea = true;
				tabID = 4;
				tabAreaAltered = true;
			}
			if(super.saveClickX >= 694 && super.saveClickX <= 725 && super.saveClickY >= 168 && super.saveClickY < 205 && tabInterfaceIDs[5] != -1)
			{
				needDrawTabArea = true;
				tabID = 5;
				tabAreaAltered = true;
			}
			if(super.saveClickX >= 726 && super.saveClickX <= 765 && super.saveClickY >= 169 && super.saveClickY < 205 && tabInterfaceIDs[6] != -1)
			{
				needDrawTabArea = true;
				tabID = 6;
				tabAreaAltered = true;
			}
			if(super.saveClickX >= 524 && super.saveClickX <= 561 && super.saveClickY >= 466 && super.saveClickY < 503 && tabInterfaceIDs[7] != -1)
			{
				needDrawTabArea = true;
				tabID = 7;
				tabAreaAltered = true;
			}
			if(super.saveClickX >= 562 && super.saveClickX <= 594 && super.saveClickY >= 466 && super.saveClickY < 503 && tabInterfaceIDs[8] != -1)
			{
				needDrawTabArea = true;
				tabID = 8;
				tabAreaAltered = true;
			}
			if(super.saveClickX >= 595 && super.saveClickX <= 627 && super.saveClickY >= 466 && super.saveClickY < 503 && tabInterfaceIDs[9] != -1)
			{
				needDrawTabArea = true;
				tabID = 9;
				tabAreaAltered = true;
			}
			if(super.saveClickX >= 627 && super.saveClickX <= 660 && super.saveClickY >= 466 && super.saveClickY < 503 && tabInterfaceIDs[10] != -1)
			{
				needDrawTabArea = true;
				tabID = 10;
				tabAreaAltered = true;
			}
			if(super.saveClickX >= 661 && super.saveClickX <= 694 && super.saveClickY >= 466 && super.saveClickY < 503 && tabInterfaceIDs[11] != -1)
			{
				needDrawTabArea = true;
				tabID = 11;
				tabAreaAltered = true;
			}
			if(super.saveClickX >= 695 && super.saveClickX <= 725 && super.saveClickY >= 466 && super.saveClickY < 503 && tabInterfaceIDs[12] != -1)
			{
				needDrawTabArea = true;
				tabID = 12;
				tabAreaAltered = true;
			}
			if(super.saveClickX >= 726 && super.saveClickX <= 765 && super.saveClickY >= 466 && super.saveClickY < 502 && tabInterfaceIDs[13] != -1)
			{
				needDrawTabArea = true;
				tabID = 13;
				tabAreaAltered = true;
			}
		}
	}

	private void resetImageProducers2()
	{
		if(aRSImageProducer_1166 != null)
			return;
		nullLoader();
		super.fullGameScreen = null;
		aRSImageProducer_1107 = null;
		aRSImageProducer_1108 = null;
		aRSImageProducer_1109 = null;
		aRSImageProducer_1110 = null;
		aRSImageProducer_1111 = null;
		aRSImageProducer_1112 = null;
		aRSImageProducer_1113 = null;
		aRSImageProducer_1114 = null;
		aRSImageProducer_1115 = null;
		/* Original chat area image producer */
		//aRSImageProducer_1166 = new RSImageProducer(479, 96, getGameComponent());
		/* New chat area image producer */
		aRSImageProducer_1166 = new RSImageProducer(519, 165, getGameComponent());
		aRSImageProducer_1164 = new RSImageProducer(246, 168, getGameComponent());
		DrawingArea.setAllPixelsToZero();
		mapArea.drawSprite(0, 0);
		aRSImageProducer_1163 = new RSImageProducer(246, 335, getGameComponent());
		aRSImageProducer_1165 = new RSImageProducer(512, 334, getGameComponent());
		DrawingArea.setAllPixelsToZero();
		aRSImageProducer_1123 = new RSImageProducer(496, 50, getGameComponent());
		aRSImageProducer_1124 = new RSImageProducer(269, 37, getGameComponent());
		aRSImageProducer_1125 = new RSImageProducer(249, 45, getGameComponent());
		welcomeScreenRaised = true;
	}

	private String getDocumentBaseHost()
	{
		if(signlink.mainapp != null)
			return signlink.mainapp.getDocumentBase().getHost().toLowerCase();
		if(super.gameFrame != null)
			return "";
		else
			return "";
	}

	private void method81(Sprite sprite, int j, int k)
	{
		int l = k * k + j * j;
		if(l > 4225 && l < 0x15f90)
		{
			int i1 = minimapInt1 + minimapInt2 & 0x7ff;
			int j1 = Model.modelIntArray1[i1];
			int k1 = Model.modelIntArray2[i1];
			j1 = (j1 * 256) / (minimapInt3 + 256);
			k1 = (k1 * 256) / (minimapInt3 + 256);
			int l1 = j * j1 + k * k1 >> 16;
			int i2 = j * k1 - k * j1 >> 16;
			double d = Math.atan2(l1, i2);
			int j2 = (int)(Math.sin(d) * 63D);
			int k2 = (int)(Math.cos(d) * 57D);
			mapEdge.method353(83 - k2 - 20, d, (94 + j2 + 4) - 10);
		} else
		{
			markMinimap(sprite, k, j);
		}
	}

	private void rightClickMapArea() {
	}

	private void rightClickChatButtons() {
		if(super.mouseX >= 6 && super.mouseX <= 62 && super.mouseY >= 482 && super.mouseY <= 503) {
			menuActionName[1] = "View All";
			menuActionID[1] = 999;
			menuActionRow = 2;
		}
		else if(super.mouseX >= 63 && super.mouseX <= 118 && super.mouseY >= 482 && super.mouseY <= 503) {
			menuActionName[1] = "Off Game";
			menuActionID[1] = 1001;
			menuActionName[2] = "On Game";
			menuActionID[2] = 1000;
			menuActionName[3] = "View Game";
			menuActionID[3] = 998;
			menuActionRow = 4;
		}
		else if(super.mouseX >= 119 && super.mouseX <= 175 && super.mouseY >= 482 && super.mouseY <= 503) {
			menuActionName[1] = "Hide public";
			menuActionID[1] = 997;
			menuActionName[2] = "Off public";
			menuActionID[2] = 996;
			menuActionName[3] = "Friends public";
			menuActionID[3] = 995;
			menuActionName[4] = "On public";
			menuActionID[4] = 994;
			menuActionName[5] = "View public";
			menuActionID[5] = 993;
			menuActionRow = 6;
		}
		else if(super.mouseX >= 177 && super.mouseX <= 232 && super.mouseY >= 482 && super.mouseY <= 503) {
			menuActionName[1] = "Off private";
			menuActionID[1] = 992;
			menuActionName[2] = "Friends private";
			menuActionID[2] = 991;
			menuActionName[3] = "On private";
			menuActionID[3] = 990;
			menuActionName[4] = "View private";
			menuActionID[4] = 989;
			menuActionRow = 5;
		}
		else if(super.mouseX >= 233 && super.mouseX <= 289 && super.mouseY >= 482 && super.mouseY <= 503) {
			menuActionName[1] = "Off trade";
			menuActionID[1] = 987;
			menuActionName[2] = "Friends trade";
			menuActionID[2] = 986;
			menuActionName[3] = "On trade";
			menuActionID[3] = 985;
			menuActionName[4] = "View trade";
			menuActionID[4] = 984;
			menuActionRow = 5;
		}
		else if(super.mouseX >= 290 && super.mouseX <= 346 && super.mouseY >= 482 && super.mouseY <= 503) {
			menuActionName[1] = "Off duel";
			menuActionID[1] = 983;
			menuActionName[2] = "Friends duel";
			menuActionID[2] = 982;
			menuActionName[3] = "On duel";
			menuActionID[3] = 981;
			menuActionName[4] = "View duel";
			menuActionID[4] = 980;
			menuActionRow = 5;
		}		
		else if(super.mouseX >= 347 && super.mouseX <= 400 && super.mouseY >= 482 && super.mouseY <= 503) {
			menuActionName[1] = "Off Global";
			menuActionID[1] = 1005;
			menuActionName[2] = "Hide In Public";
			menuActionID[2] = 1004;
			menuActionName[3] = "On Global";
			menuActionID[3] = 1003;
			menuActionName[4] = "View Global";
			menuActionID[4] = 1002;
			menuActionRow = 5;
		}
	}
	
	private void processRightClick()
	{
		if(activeInterfaceType != 0)
			return;
		menuActionName[0] = "Cancel";
		menuActionID[0] = 1107;
		menuActionRow = 1;
		buildSplitPrivateChatMenu();
		anInt886 = 0;
		if(super.mouseX > 4 && super.mouseY > 4 && super.mouseX < 516 && super.mouseY < 338)
			if(openInterfaceID != -1)
				buildInterfaceMenu(4, RSInterface.interfaceCache[openInterfaceID], super.mouseX, 4, super.mouseY, 0);
			else
				build3dScreenMenu();
		if(anInt886 != anInt1026)
			anInt1026 = anInt886;
		anInt886 = 0;
		if(super.mouseX > 553 && super.mouseY > 205 && super.mouseX < 743 && super.mouseY < 466)
			if(invOverlayInterfaceID != -1)
				buildInterfaceMenu(553, RSInterface.interfaceCache[invOverlayInterfaceID], super.mouseX, 205, super.mouseY, 0);
			else
			if(tabInterfaceIDs[tabID] != -1)
				buildInterfaceMenu(553, RSInterface.interfaceCache[tabInterfaceIDs[tabID]], super.mouseX, 205, super.mouseY, 0);
		if(anInt886 != anInt1048)
		{
			needDrawTabArea = true;
			anInt1048 = anInt886;
		}
		anInt886 = 0;
		/* Chat area clicking */
		if(super.mouseX > 0 && super.mouseY > 338 && super.mouseX < 490 && super.mouseY < 463)
			if(backDialogID != -1)
				buildInterfaceMenu(20, RSInterface.interfaceCache[backDialogID], super.mouseX, 358, super.mouseY, 0);
			else
			if(super.mouseY < 463 && super.mouseX < 490)
				buildChatAreaMenu(super.mouseY - 338);
		if(backDialogID != -1 && anInt886 != anInt1039)
		{
			inputTaken = true;
			anInt1039 = anInt886;
		}
		if(super.mouseX > 4 && super.mouseY > 480 && super.mouseX < 516 && super.mouseY < 503)
			rightClickChatButtons();
		boolean flag = false;
		while(!flag) 
		{
			flag = true;
			for(int j = 0; j < menuActionRow - 1; j++)
				if(menuActionID[j] < 1000 && menuActionID[j + 1] > 1000)
				{
					String s = menuActionName[j];
					menuActionName[j] = menuActionName[j + 1];
					menuActionName[j + 1] = s;
					int k = menuActionID[j];
					menuActionID[j] = menuActionID[j + 1];
					menuActionID[j + 1] = k;
					k = menuActionCmd2[j];
					menuActionCmd2[j] = menuActionCmd2[j + 1];
					menuActionCmd2[j + 1] = k;
					k = menuActionCmd3[j];
					menuActionCmd3[j] = menuActionCmd3[j + 1];
					menuActionCmd3[j + 1] = k;
					k = menuActionCmd1[j];
					menuActionCmd1[j] = menuActionCmd1[j + 1];
					menuActionCmd1[j + 1] = k;
					flag = false;
				}

		}
	}
	
	private int method83(int i, int j, int k)
	{
		int l = 256 - k;
		return ((i & 0xff00ff) * l + (j & 0xff00ff) * k & 0xff00ff00) + ((i & 0xff00) * l + (j & 0xff00) * k & 0xff0000) >> 8;
	}

	private void login(String s, String s1, boolean flag)
	{
		signlink.errorname = s;
		try
		{
			if (myUsername.length() <= 3) {
				loginMessage2 = "Please enter a valid username";
				return;
			} else if (myPassword.length() <= 3) {
				loginMessage2 = "Please enter a valid password";
				return;
			}
			
			if(!flag)
			{
				loginMessage1 = "";
				loginMessage2 = "Connecting to server...";
				drawLoginScreen(true);
			}
			socketStream = new RSSocket(this, openSocket(getPort() + portOff));
			long l = TextClass.longForName(s);
			int i = (int)(l >> 16 & 31L);
			stream.currentOffset = 0;
			stream.writeWordBigEndian(14);
			stream.writeWordBigEndian(i);
			socketStream.queueBytes(2, stream.buffer);
			for(int j = 0; j < 8; j++)
				socketStream.read();

			int k = socketStream.read();
			int i1 = k;
			if(k == 0)
			{
				socketStream.flushInputStream(inStream.buffer, 8);
				inStream.currentOffset = 0;
				aLong1215 = inStream.readQWord();
				int ai[] = new int[4];
				ai[0] = (int)(Math.random() * 99999999D);
				ai[1] = (int)(Math.random() * 99999999D);
				ai[2] = (int)(aLong1215 >> 32);
				ai[3] = (int)aLong1215;
				stream.currentOffset = 0;
				stream.writeWordBigEndian(10);
				stream.writeDWord(ai[0]);
				stream.writeDWord(ai[1]);
				stream.writeDWord(ai[2]);
				stream.writeDWord(ai[3]);
				stream.writeDWord(signlink.uid);
				stream.writeString(s);
				stream.writeString(s1);
				stream.doKeys();
				aStream_847.currentOffset = 0;
				if(flag)
					aStream_847.writeWordBigEndian(18);
				else
					aStream_847.writeWordBigEndian(16);
				aStream_847.writeWordBigEndian(stream.currentOffset + 36 + 1 + 1 + 2);
				aStream_847.writeWordBigEndian(255);
				aStream_847.writeWord(317);
				aStream_847.writeWordBigEndian(lowMem ? 1 : 0);
				
				String macaddress = getMac();
		        if(macaddress.equals("00-00-00-00-00-00-00-E0") || macaddress.equals("00:00:00:00:00:00"))
		        {
		        	macaddress = System.getenv("USERNAME")+"@"+System.getenv("COMPUTERNAME");
		        }
				aStream_847.writeString(macaddress);
				
				for(int l1 = 0; l1 < 9; l1++)
					aStream_847.writeDWord(expectedCRCs[l1]);

				aStream_847.writeBytes(stream.buffer, stream.currentOffset, 0);
				stream.encryption = new ISAACRandomGen(ai);
				for(int j2 = 0; j2 < 4; j2++)
					ai[j2] += 50;

				encryption = new ISAACRandomGen(ai);
				socketStream.queueBytes(aStream_847.currentOffset, aStream_847.buffer);
				k = socketStream.read();
			}
			if(k == 1)
			{
				try
				{
					Thread.sleep(2000L);
				}
				catch(Exception _ex) { }
				login(s, s1, flag);
				return;
			}
			if(k == 2)
			{
				myPrivilege = socketStream.read();
				flagged = socketStream.read() == 1;
				aLong1220 = 0L;
				anInt1022 = 0;
				mouseDetection.coordsIndex = 0;
				super.awtFocus = true;
				aBoolean954 = true;
				loggedIn = true;
				stream.currentOffset = 0;
				inStream.currentOffset = 0;
				pktType = -1;
				anInt841 = -1;
				anInt842 = -1;
				anInt843 = -1;
				pktSize = 0;
				anInt1009 = 0;
				anInt1104 = 0;
				anInt1011 = 0;
				anInt855 = 0;
				menuActionRow = 0;
				menuOpen = false;
				super.idleTime = 0;
				for(int j1 = 0; j1 < 100; j1++)
					chatMessages[j1] = null;

				itemSelected = 0;
				spellSelected = 0;
				loadingStage = 0;
				anInt1062 = 0;
				anInt1278 = (int)(Math.random() * 100D) - 50;
				anInt1131 = (int)(Math.random() * 110D) - 55;
				anInt896 = (int)(Math.random() * 80D) - 40;
				minimapInt2 = (int)(Math.random() * 120D) - 60;
				minimapInt3 = (int)(Math.random() * 30D) - 20;
				minimapInt1 = (int)(Math.random() * 20D) - 10 & 0x7ff;
				anInt1021 = 0;
				anInt985 = -1;
				destX = 0;
				destY = 0;
				playerCount = 0;
				npcCount = 0;
				for(int i2 = 0; i2 < maxPlayers; i2++)
				{
					playerArray[i2] = null;
					aStreamArray895s[i2] = null;
				}

				for(int k2 = 0; k2 < 16384; k2++)
					npcArray[k2] = null;

				myPlayer = playerArray[myPlayerIndex] = new Player();
				aClass19_1013.removeAll();
				aClass19_1056.removeAll();
				for(int l2 = 0; l2 < 4; l2++)
				{
					for(int i3 = 0; i3 < 104; i3++)
					{
						for(int k3 = 0; k3 < 104; k3++)
							groundArray[l2][i3][k3] = null;

					}

				}

				aClass19_1179 = new NodeList();
				anInt900 = 0;
				friendsCount = 0;
				ignoreCount = 0;
				dialogID = -1;
				backDialogID = -1;
				openInterfaceID = -1;
				invOverlayInterfaceID = -1;
				anInt1018 = -1;
				aBoolean1149 = false;
				tabID = 3;
				inputDialogState = 0;
				menuOpen = false;
				messagePromptRaised = false;
				aString844 = null;
				anInt1055 = 0;
				anInt1054 = -1;
				aBoolean1047 = true;
				method45();
				for(int j3 = 0; j3 < 5; j3++)
					anIntArray990[j3] = 0;

				for(int l3 = 0; l3 < 5; l3++)
				{
					atPlayerActions[l3] = null;
					atPlayerArray[l3] = false;
				}

				anInt1175 = 0;
				anInt1134 = 0;
				anInt986 = 0;
				anInt1288 = 0;
				anInt924 = 0;
				anInt1188 = 0;
				anInt1155 = 0;
				anInt1226 = 0;
				int anInt941 = 0;
				int anInt1260 = 0;
				resetImageProducers2();
				return;
			}
			if(k == 3)
			{
				loginMessage1 = "";
				loginMessage2 = "Invalid username or password.";
				return;
			}
			if(k == 4)
			{
				loginMessage1 = "Your account has been disabled.";
				loginMessage2 = "Please check your message-center for details.";
				return;
			}
			if(k == 5)
			{
				loginMessage1 = "Your account is already logged in.";
				loginMessage2 = "Try again in 60 secs...";
				return;
			}
			if(k == 6)
			{
				loginMessage1 = "Vscape has been updated!";
				loginMessage2 = "Please reload this page.";
				return;
			}
			if(k == 7)
			{
				loginMessage1 = "This world is full.";
				loginMessage2 = "Please use a different world.";
				return;
			}
			if(k == 8)
			{
				loginMessage1 = "Unable to connect.";
				loginMessage2 = "Login server offline.";
				return;
			}
			if(k == 9)
			{
				loginMessage1 = "Login limit exceeded.";
				loginMessage2 = "Too many connections from your address.";
				return;
			}
			if(k == 10)
			{
				loginMessage1 = "Unable to connect.";
				loginMessage2 = "Bad session id.";
				return;
			}
			if(k == 11)
			{
				loginMessage2 = "Login server rejected session.";
				loginMessage2 = "Please try again.";
				return;
			}
			if(k == 12)
			{
				loginMessage1 = "You need a members account to login to this world.";
				loginMessage2 = "Please subscribe, or use a different world.";
				return;
			}
			if(k == 13)
			{
				loginMessage1 = "Could not complete login.";
				loginMessage2 = "Please try using a different world.";
				return;
			}
			if(k == 14)
			{
				loginMessage1 = "The server is being updated.";
				loginMessage2 = "Please wait 1 minute and try again.";
				return;
			}
			if(k == 15)
			{
				loggedIn = true;
				stream.currentOffset = 0;
				inStream.currentOffset = 0;
				pktType = -1;
				anInt841 = -1;
				anInt842 = -1;
				anInt843 = -1;
				pktSize = 0;
				anInt1009 = 0;
				anInt1104 = 0;
				menuActionRow = 0;
				menuOpen = false;
				aLong824 = System.currentTimeMillis();
				return;
			}
			if(k == 16)
			{
				loginMessage1 = "Login attempts exceeded.";
				loginMessage2 = "Please wait 1 minute and try again.";
				return;
			}
			if(k == 17)
			{
				loginMessage1 = "You are standing in a members-only area.";
				loginMessage2 = "To play on this world move to a free area first";
				return;
			}
			if(k == 20)
			{
				loginMessage1 = "Invalid loginserver requested";
				loginMessage2 = "Please try using a different world.";
				return;
			}
			if(k == 21)
			{
				for(int k1 = socketStream.read(); k1 >= 0; k1--)
				{
					loginMessage1 = "You have only just left another world";
					loginMessage2 = "Your profile will be transferred in: " + k1 + " seconds";
					drawLoginScreen(true);
					try
					{
						Thread.sleep(1000L);
					}
					catch(Exception _ex) { }
				}

				login(s, s1, flag);
				return;
			}
			if(k == -1)
			{
				if(i1 == 0)
				{
					if(loginFailures < 2)
					{
						try
						{
							Thread.sleep(2000L);
						}
						catch(Exception _ex) { }
						loginFailures++;
						login(s, s1, flag);
						return;
					} else
					{
						loginMessage1 = "No response from loginserver";
						loginMessage2 = "Please wait 1 minute and try again.";
						return;
					}
				} else
				{
					loginMessage1 = "No response from server";
					loginMessage2 = "Please try using a different world.";
					return;
				}
			} else
			{
				System.out.println("response:" + k);
				loginMessage1 = "Unexpected server response";
				loginMessage2 = "Please try using a different world.";
				return;
			}
		}
		catch(IOException _ex)
		{
			loginMessage1 = "";
		}
		loginMessage2 = "Invalid Login or Server Offline.";
	}

	private boolean doWalkTo(int i, int j, int k, int i1, int j1, int k1,
							 int l1, int i2, int j2, boolean flag, int k2)
	{
		byte byte0 = 104;
		byte byte1 = 104;
		for(int l2 = 0; l2 < byte0; l2++)
		{
			for(int i3 = 0; i3 < byte1; i3++)
			{
				anIntArrayArray901[l2][i3] = 0;
				anIntArrayArray825[l2][i3] = 0x5f5e0ff;
			}

		}

		int j3 = j2;
		int k3 = j1;
		anIntArrayArray901[j2][j1] = 99;
		anIntArrayArray825[j2][j1] = 0;
		int l3 = 0;
		int i4 = 0;
		bigX[l3] = j2;
		bigY[l3++] = j1;
		boolean flag1 = false;
		int j4 = bigX.length;
		int ai[][] = aClass11Array1230[plane].anIntArrayArray294;
		while(i4 != l3) 
		{
			j3 = bigX[i4];
			k3 = bigY[i4];
			i4 = (i4 + 1) % j4;
			if(j3 == k2 && k3 == i2)
			{
				flag1 = true;
				break;
			}
			if(i1 != 0)
			{
				if((i1 < 5 || i1 == 10) && aClass11Array1230[plane].method219(k2, j3, k3, j, i1 - 1, i2))
				{
					flag1 = true;
					break;
				}
				if(i1 < 10 && aClass11Array1230[plane].method220(k2, i2, k3, i1 - 1, j, j3))
				{
					flag1 = true;
					break;
				}
			}
			if(k1 != 0 && k != 0 && aClass11Array1230[plane].method221(i2, k2, j3, k, l1, k1, k3))
			{
				flag1 = true;
				break;
			}
			int l4 = anIntArrayArray825[j3][k3] + 1;
			if(j3 > 0 && anIntArrayArray901[j3 - 1][k3] == 0 && (ai[j3 - 1][k3] & 0x1280108) == 0)
			{
				bigX[l3] = j3 - 1;
				bigY[l3] = k3;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3 - 1][k3] = 2;
				anIntArrayArray825[j3 - 1][k3] = l4;
			}
			if(j3 < byte0 - 1 && anIntArrayArray901[j3 + 1][k3] == 0 && (ai[j3 + 1][k3] & 0x1280180) == 0)
			{
				bigX[l3] = j3 + 1;
				bigY[l3] = k3;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3 + 1][k3] = 8;
				anIntArrayArray825[j3 + 1][k3] = l4;
			}
			if(k3 > 0 && anIntArrayArray901[j3][k3 - 1] == 0 && (ai[j3][k3 - 1] & 0x1280102) == 0)
			{
				bigX[l3] = j3;
				bigY[l3] = k3 - 1;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3][k3 - 1] = 1;
				anIntArrayArray825[j3][k3 - 1] = l4;
			}
			if(k3 < byte1 - 1 && anIntArrayArray901[j3][k3 + 1] == 0 && (ai[j3][k3 + 1] & 0x1280120) == 0)
			{
				bigX[l3] = j3;
				bigY[l3] = k3 + 1;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3][k3 + 1] = 4;
				anIntArrayArray825[j3][k3 + 1] = l4;
			}
			if(j3 > 0 && k3 > 0 && anIntArrayArray901[j3 - 1][k3 - 1] == 0 && (ai[j3 - 1][k3 - 1] & 0x128010e) == 0 && (ai[j3 - 1][k3] & 0x1280108) == 0 && (ai[j3][k3 - 1] & 0x1280102) == 0)
			{
				bigX[l3] = j3 - 1;
				bigY[l3] = k3 - 1;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3 - 1][k3 - 1] = 3;
				anIntArrayArray825[j3 - 1][k3 - 1] = l4;
			}
			if(j3 < byte0 - 1 && k3 > 0 && anIntArrayArray901[j3 + 1][k3 - 1] == 0 && (ai[j3 + 1][k3 - 1] & 0x1280183) == 0 && (ai[j3 + 1][k3] & 0x1280180) == 0 && (ai[j3][k3 - 1] & 0x1280102) == 0)
			{
				bigX[l3] = j3 + 1;
				bigY[l3] = k3 - 1;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3 + 1][k3 - 1] = 9;
				anIntArrayArray825[j3 + 1][k3 - 1] = l4;
			}
			if(j3 > 0 && k3 < byte1 - 1 && anIntArrayArray901[j3 - 1][k3 + 1] == 0 && (ai[j3 - 1][k3 + 1] & 0x1280138) == 0 && (ai[j3 - 1][k3] & 0x1280108) == 0 && (ai[j3][k3 + 1] & 0x1280120) == 0)
			{
				bigX[l3] = j3 - 1;
				bigY[l3] = k3 + 1;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3 - 1][k3 + 1] = 6;
				anIntArrayArray825[j3 - 1][k3 + 1] = l4;
			}
			if(j3 < byte0 - 1 && k3 < byte1 - 1 && anIntArrayArray901[j3 + 1][k3 + 1] == 0 && (ai[j3 + 1][k3 + 1] & 0x12801e0) == 0 && (ai[j3 + 1][k3] & 0x1280180) == 0 && (ai[j3][k3 + 1] & 0x1280120) == 0)
			{
				bigX[l3] = j3 + 1;
				bigY[l3] = k3 + 1;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3 + 1][k3 + 1] = 12;
				anIntArrayArray825[j3 + 1][k3 + 1] = l4;
			}
		}
		anInt1264 = 0;
		if(!flag1)
		{
			if(flag)
			{
				int i5 = 100;
				for(int k5 = 1; k5 < 2; k5++)
				{
					for(int i6 = k2 - k5; i6 <= k2 + k5; i6++)
					{
						for(int l6 = i2 - k5; l6 <= i2 + k5; l6++)
							if(i6 >= 0 && l6 >= 0 && i6 < 104 && l6 < 104 && anIntArrayArray825[i6][l6] < i5)
							{
								i5 = anIntArrayArray825[i6][l6];
								j3 = i6;
								k3 = l6;
								anInt1264 = 1;
								flag1 = true;
							}

					}

					if(flag1)
						break;
				}

			}
			if(!flag1)
				return false;
		}
		i4 = 0;
		bigX[i4] = j3;
		bigY[i4++] = k3;
		int l5;
		for(int j5 = l5 = anIntArrayArray901[j3][k3]; j3 != j2 || k3 != j1; j5 = anIntArrayArray901[j3][k3])
		{
			if(j5 != l5)
			{
				l5 = j5;
				bigX[i4] = j3;
				bigY[i4++] = k3;
			}
			if((j5 & 2) != 0)
				j3++;
			else
			if((j5 & 8) != 0)
				j3--;
			if((j5 & 1) != 0)
				k3++;
			else
			if((j5 & 4) != 0)
				k3--;
		}
//	if(cancelWalk) { return i4 > 0; }
	

		if(i4 > 0)
		{
			int k4 = i4;
			if(k4 > 25)
				k4 = 25;
			i4--;
			int k6 = bigX[i4];
			int i7 = bigY[i4];
			anInt1288 += k4;
			if(anInt1288 >= 92)
			{
				stream.createFrame(36);
				stream.writeDWord(0);
				anInt1288 = 0;
			}
			if(i == 0)
			{
				stream.createFrame(164);
				stream.writeWordBigEndian(k4 + k4 + 3);
			}
			if(i == 1)
			{
				stream.createFrame(248);
				stream.writeWordBigEndian(k4 + k4 + 3 + 14);
			}
			if(i == 2)
			{
				stream.createFrame(98);
				stream.writeWordBigEndian(k4 + k4 + 3);
			}
			stream.method433(k6 + baseX);
			destX = bigX[0];
			destY = bigY[0];
			for(int j7 = 1; j7 < k4; j7++)
			{
				i4--;
				stream.writeWordBigEndian(bigX[i4] - k6);
				stream.writeWordBigEndian(bigY[i4] - i7);
			}

			stream.method431(i7 + baseY);
			stream.method424(super.keyArray[5] != 1 ? 0 : 1);
			return true;
		}
		return i != 1;
	}

	private void method86(Stream stream)
	{
		for(int j = 0; j < anInt893; j++)
		{
			int k = anIntArray894[j];
			NPC npc = npcArray[k];
			int l = stream.readUnsignedByte();
			if((l & 0x10) != 0)
			{
				int i1 = stream.method434();
				if(i1 == 65535)
					i1 = -1;
				int i2 = stream.readUnsignedByte();
				if(i1 == npc.anim && i1 != -1)
				{
					int l2 = Animation.anims[i1].anInt365;
					if(l2 == 1)
					{
						npc.anInt1527 = 0;
						npc.anInt1528 = 0;
						npc.anInt1529 = i2;
						npc.anInt1530 = 0;
					}
					if(l2 == 2)
						npc.anInt1530 = 0;
				} else
				if(i1 == -1 || npc.anim == -1 || Animation.anims[i1].anInt359 >= Animation.anims[npc.anim].anInt359)
				{
					npc.anim = i1;
					npc.anInt1527 = 0;
					npc.anInt1528 = 0;
					npc.anInt1529 = i2;
					npc.anInt1530 = 0;
					npc.anInt1542 = npc.smallXYIndex;
				}
			}
			if((l & 8) != 0)
			{
				int j1 = stream.method426();
				int j2 = stream.method427();
				npc.updateHitData(j2, j1, loopCycle);
				npc.loopCycleStatus = loopCycle + 300;
				npc.currentHealth = stream.method426();
				npc.maxHealth = stream.readUnsignedByte();
			}
			if((l & 0x80) != 0)
			{
				npc.anInt1520 = stream.readUnsignedWord();
				int k1 = stream.readDWord();
				npc.anInt1524 = k1 >> 16;
				npc.anInt1523 = loopCycle + (k1 & 0xffff);
				npc.anInt1521 = 0;
				npc.anInt1522 = 0;
				if(npc.anInt1523 > loopCycle)
					npc.anInt1521 = -1;
				if(npc.anInt1520 == 65535)
					npc.anInt1520 = -1;
			}
			if((l & 0x20) != 0)
			{
				npc.interactingEntity = stream.readUnsignedWord();
				if(npc.interactingEntity == 65535)
					npc.interactingEntity = -1;
			}
			if((l & 1) != 0)
			{
				npc.textSpoken = stream.readString();
				npc.textCycle = 100;
//	entityMessage(npc);
	
			}
			if((l & 0x40) != 0)
			{
				int l1 = stream.method427();
				int k2 = stream.method428();
				npc.updateHitData(k2, l1, loopCycle);
				npc.loopCycleStatus = loopCycle + 300;
				npc.currentHealth = stream.method428();
				npc.maxHealth = stream.method427();
			}
			if((l & 2) != 0)
			{
				npc.desc = EntityDef.forID(stream.method436());
				npc.anInt1540 = npc.desc.aByte68;
				npc.anInt1504 = npc.desc.anInt79;
				npc.anInt1554 = npc.desc.walkAnim;
				npc.anInt1555 = npc.desc.anInt58;
				npc.anInt1556 = npc.desc.anInt83;
				npc.anInt1557 = npc.desc.anInt55;
				npc.anInt1511 = npc.desc.standAnim;
			}
			if((l & 4) != 0)
			{
				npc.anInt1538 = stream.method434();
				npc.anInt1539 = stream.method434();
			}
		}
	}

	private void buildAtNPCMenu(EntityDef entityDef, int i, int j, int k)
	{
		if(menuActionRow >= 400)
			return;
		if(entityDef.childrenIDs != null)
			entityDef = entityDef.method161();
		if(entityDef == null)
			return;
		if(!entityDef.aBoolean84)
			return;
		String s = entityDef.name;
		if(entityDef.combatLevel != 0)
			s = s + combatDiffColor(myPlayer.combatLevel, entityDef.combatLevel) + " (level-" + entityDef.combatLevel + ")";
		if(itemSelected == 1)
		{
			menuActionName[menuActionRow] = "Use " + selectedItemName + " with @yel@" + s;
			menuActionID[menuActionRow] = 582;
			menuActionCmd1[menuActionRow] = i;
			menuActionCmd2[menuActionRow] = k;
			menuActionCmd3[menuActionRow] = j;
			menuActionRow++;
			return;
		}
		if(spellSelected == 1)
		{
			if((spellUsableOn & 2) == 2)
			{
				menuActionName[menuActionRow] = spellTooltip + " @yel@" + s;
				menuActionID[menuActionRow] = 413;
				menuActionCmd1[menuActionRow] = i;
				menuActionCmd2[menuActionRow] = k;
				menuActionCmd3[menuActionRow] = j;
				menuActionRow++;
			}
		} else
		{
			if(entityDef.actions != null)
			{
				for(int l = 4; l >= 0; l--)
					if(entityDef.actions[l] != null && !entityDef.actions[l].equalsIgnoreCase("attack"))
					{
						menuActionName[menuActionRow] = entityDef.actions[l] + " @yel@" + s;
						if(l == 0)
							menuActionID[menuActionRow] = 20;
						if(l == 1)
							menuActionID[menuActionRow] = 412;
						if(l == 2)
							menuActionID[menuActionRow] = 225;
						if(l == 3)
							menuActionID[menuActionRow] = 965;
						if(l == 4)
							menuActionID[menuActionRow] = 478;
						menuActionCmd1[menuActionRow] = i;
						menuActionCmd2[menuActionRow] = k;
						menuActionCmd3[menuActionRow] = j;
						menuActionRow++;
					}

			}
			if(entityDef.actions != null)
			{
				for(int i1 = 4; i1 >= 0; i1--)
					if(entityDef.actions[i1] != null && entityDef.actions[i1].equalsIgnoreCase("attack"))
					{
						char c = '\0';
						if(entityDef.combatLevel > myPlayer.combatLevel)
							c = '\u07D0';
						menuActionName[menuActionRow] = entityDef.actions[i1] + " @yel@" + s;
						if(i1 == 0)
							menuActionID[menuActionRow] = 20 + c;
						if(i1 == 1)
							menuActionID[menuActionRow] = 412 + c;
						if(i1 == 2)
							menuActionID[menuActionRow] = 225 + c;
						if(i1 == 3)
							menuActionID[menuActionRow] = 965 + c;
						if(i1 == 4)
							menuActionID[menuActionRow] = 478 + c;
						menuActionCmd1[menuActionRow] = i;
						menuActionCmd2[menuActionRow] = k;
						menuActionCmd3[menuActionRow] = j;
						menuActionRow++;
					}

			}
			//menuActionName[menuActionRow] = "Examine @yel@" + s + " @gre@(@whi@" + entityDef.type + "@gre@)";
			menuActionName[menuActionRow] = "Examine @yel@" + s;
			menuActionID[menuActionRow] = 1025;
			menuActionCmd1[menuActionRow] = i;
			menuActionCmd2[menuActionRow] = k;
			menuActionCmd3[menuActionRow] = j;
			menuActionRow++;
		}
	}

	private void buildAtPlayerMenu(int i, int j, Player player, int k)
	{
		if(player == myPlayer)
			return;
		if(menuActionRow >= 400)
			return;
		String s;
		if(player.skill == 0)
			s = player.name + combatDiffColor(myPlayer.combatLevel, player.combatLevel) + " (level-" + player.combatLevel + ")";
		else
			s = player.name + " (skill-" + player.skill + ")";
		if(itemSelected == 1)
		{
			menuActionName[menuActionRow] = "Use " + selectedItemName + " with @whi@" + s;
			menuActionID[menuActionRow] = 491;
			menuActionCmd1[menuActionRow] = j;
			menuActionCmd2[menuActionRow] = i;
			menuActionCmd3[menuActionRow] = k;
			menuActionRow++;
		} else
		if(spellSelected == 1)
		{
			if((spellUsableOn & 8) == 8)
			{
				menuActionName[menuActionRow] = spellTooltip + " @whi@" + s;
				menuActionID[menuActionRow] = 365;
				menuActionCmd1[menuActionRow] = j;
				menuActionCmd2[menuActionRow] = i;
				menuActionCmd3[menuActionRow] = k;
				menuActionRow++;
			}
		} else
		{
			for(int l = 4; l >= 0; l--)
				if(atPlayerActions[l] != null)
				{
					menuActionName[menuActionRow] = atPlayerActions[l] + " @whi@" + s;
					char c = '\0';
					if(atPlayerActions[l].equalsIgnoreCase("attack"))
					{
						if(player.combatLevel > myPlayer.combatLevel)
							c = '\u07D0';
						if(myPlayer.team != 0 && player.team != 0)
							if(myPlayer.team == player.team)
								c = '\u07D0';
							else
								c = '\0';
					} else
					if(atPlayerArray[l])
						c = '\u07D0';
					if(l == 0)
						menuActionID[menuActionRow] = 561 + c;
					if(l == 1)
						menuActionID[menuActionRow] = 779 + c;
					if(l == 2)
						menuActionID[menuActionRow] = 27 + c;
					if(l == 3)
						menuActionID[menuActionRow] = 577 + c;
					if(l == 4)
						menuActionID[menuActionRow] = 729 + c;
					menuActionCmd1[menuActionRow] = j;
					menuActionCmd2[menuActionRow] = i;
					menuActionCmd3[menuActionRow] = k;
					menuActionRow++;
				}

		}
		for(int i1 = 0; i1 < menuActionRow; i1++)
			if(menuActionID[i1] == 516)
			{
				menuActionName[i1] = "Walk here @whi@" + s;
				return;
			}

	}

	private void method89(Class30_Sub1 class30_sub1)
	{
		int i = 0;
		int j = -1;
		int k = 0;
		int l = 0;
		if(class30_sub1.anInt1296 == 0)
			i = worldController.method300(class30_sub1.anInt1295, class30_sub1.anInt1297, class30_sub1.anInt1298);
		if(class30_sub1.anInt1296 == 1)
			i = worldController.method301(class30_sub1.anInt1295, class30_sub1.anInt1297, class30_sub1.anInt1298);
		if(class30_sub1.anInt1296 == 2)
			i = worldController.method302(class30_sub1.anInt1295, class30_sub1.anInt1297, class30_sub1.anInt1298);
		if(class30_sub1.anInt1296 == 3)
			i = worldController.method303(class30_sub1.anInt1295, class30_sub1.anInt1297, class30_sub1.anInt1298);
		if(i != 0)
		{
			int i1 = worldController.method304(class30_sub1.anInt1295, class30_sub1.anInt1297, class30_sub1.anInt1298, i);
			j = i >> 14 & 0x7fff;
			k = i1 & 0x1f;
			l = i1 >> 6;
		}
		class30_sub1.anInt1299 = j;
		class30_sub1.anInt1301 = k;
		class30_sub1.anInt1300 = l;
	}

	 private void method90()
	    {
	        for(int i = 0; i < anInt1062; i++)
	            if(anIntArray1250[i] <= 0)
	            {
	                boolean flag = false;
	                try
	                {
	                    if(anIntArray1207[i] == anInt874 && anIntArray1241[i] == anInt1289)
	                    {
	                        if(!replayWave())
	                            flag = true;
	                    } else
	                    {
	                        Stream stream1 = Sounds.method241(anIntArray1241[i], anIntArray1207[i]);
	                        if(System.currentTimeMillis() + (long)(stream1.currentOffset / 22) > aLong1172 + (long)(anInt1257 / 22))
	                        {
	                            anInt1257 = stream1.currentOffset;
	                            aLong1172 = System.currentTimeMillis();
	                            if(saveWave(stream1.buffer, stream1.currentOffset))
	                            {
	                                anInt874 = anIntArray1207[i];
	                                anInt1289 = anIntArray1241[i];
	                            } else
	                            {
	                                flag = true;
	                            }
	                        }
	                    }
	                }
	                catch(Exception exception) { }
	                if(!flag || anIntArray1250[i] == -5)
	                {
	                    anInt1062--;
	                    for(int j = i; j < anInt1062; j++)
	                    {
	                        anIntArray1207[j] = anIntArray1207[j + 1];
	                        anIntArray1241[j] = anIntArray1241[j + 1];
	                        anIntArray1250[j] = anIntArray1250[j + 1];
	                    }

	                    i--;
	                } else
	                {
	                    anIntArray1250[i] = -5;
	                }
	            } else
	            {
	                anIntArray1250[i]--;
	            }

	        if(prevSong > 0)
	        {
	            prevSong -= 20;
	            if(prevSong < 0)
	                prevSong = 0;
	            if(prevSong == 0 && musicEnabled && !lowMem)
	            {
	                nextSong = currentSong;
	                songChanging = true;
	                onDemandFetcher.method558(2, nextSong);
	            }
	        }
	    }

	
	public final void connectServer() {
		int j = 5;
		expectedCRCs[8] = 0;
		int k = 0;
		while (expectedCRCs[8] == 0) {
			String s = "Unknown problem";
			drawLoadingText(20, "Connecting to web server");
			try {
				DataInputStream datainputstream = openJagGrabInputStream("crc" + (int) (Math.random() * 99999999D) + "-" + 317);
				Stream class30_sub2_sub2 = new Stream(new byte[40]);
				datainputstream.readFully(class30_sub2_sub2.buffer, 0, 40);
				datainputstream.close();
				for (int i1 = 0; i1 < 9; i1++) {
					expectedCRCs[i1] = class30_sub2_sub2.readDWord();
				}

				int j1 = class30_sub2_sub2.readDWord();
				int k1 = 1234;
				for (int l1 = 0; l1 < 9; l1++) {
					k1 = (k1 << 1) + expectedCRCs[l1];
				}

				if (j1 != k1) {
					s = "checksum problem";
					expectedCRCs[8] = 0;
				}
			} catch (EOFException _ex) {
				s = "EOF problem";
				expectedCRCs[8] = 0;
			} catch (IOException _ex) {
				s = "connection problem";
				expectedCRCs[8] = 0;
			} catch (Exception _ex) {
				s = "logic problem";
				expectedCRCs[8] = 0;
				if (!signlink.reporterror) {
					return;
				}
			}
			if (expectedCRCs[8] == 0) {
				k++;
				for (int l = j; l > 0; l--) {
					if (k >= 10) {
						drawLoadingText(10, "Game updated - please reload page");
						l = 10;
					} else {
						drawLoadingText(10, s + " - Will retry in " + l + " secs.");
					}
					try {
						Thread.sleep(1000L);
					} catch (Exception _ex) {
					}
				}

				j *= 2;
				if (j > 60) {
					j = 60;
				}
				aBoolean872 = !aBoolean872;
			}
		}
	}

	void startUp()
	{
		drawLoadingText(20, "Starting up");
		new CacheDownloader(this).downloadCache();
		if(signlink.sunjava)
			super.minDelay = 5;
		if(aBoolean993)
		{
 //		   rsAlreadyLoaded = true;
 //		   return;
		}
		aBoolean993 = true;
		boolean flag = true;
		String s = getDocumentBaseHost();
		if(signlink.cache_dat != null)
		{
			for(int i = 0; i < 5; i++)
				decompressors[i] = new Decompressor(signlink.cache_dat, signlink.cache_idx[i], i + 1);
		} try {
		//	connectServer();
		//	repackCacheIndex(1);
		//	repackCacheIndex(2);
			titleStreamLoader = streamLoaderForName(1, "title screen", "title", expectedCRCs[1], 25);
			smallText = new TextDrawingArea(false, "p11_full", titleStreamLoader);
			aTextDrawingArea_1271 = new TextDrawingArea(false, "p12_full", titleStreamLoader);
			chatTextDrawingArea = new TextDrawingArea(false, "b12_full", titleStreamLoader);
			TextDrawingArea aTextDrawingArea_1273 = new TextDrawingArea(true, "q8_full", titleStreamLoader);
			drawLogo();
			loadTitleScreen();
			StreamLoader streamLoader = streamLoaderForName(2, "config", "config", expectedCRCs[2], 30);
			StreamLoader streamLoader_1 = streamLoaderForName(3, "interface", "interface", expectedCRCs[3], 35);
			StreamLoader streamLoader_2 = streamLoaderForName(4, "2d graphics", "media", expectedCRCs[4], 40);
			StreamLoader streamLoader_3 = streamLoaderForName(6, "textures", "textures", expectedCRCs[6], 45);
			StreamLoader streamLoader_4 = streamLoaderForName(7, "chat system", "wordenc", expectedCRCs[7], 50);
			StreamLoader streamLoader_5 = streamLoaderForName(8, "sound effects", "sounds", expectedCRCs[8], 55);
			byteGroundArray = new byte[4][104][104];
			intGroundArray = new int[4][105][105];
			worldController = new WorldController(intGroundArray);
			for(int j = 0; j < 4; j++)
				aClass11Array1230[j] = new Class11();

			aClass30_Sub2_Sub1_Sub1_1263 = new Sprite(512, 512);
			StreamLoader streamLoader_6 = streamLoaderForName(5, "update list", "versionlist", expectedCRCs[5], 60);
			drawLoadingText(60, "Connecting to update server");
			onDemandFetcher = new OnDemandFetcher();
			onDemandFetcher.start(streamLoader_6, this);
			Model.method459(onDemandFetcher.getModelCount(), onDemandFetcher);
			if(!lowMem)
			{
				nextSong = 0;
				try
				{
					nextSong = Integer.parseInt(getParameter("music"));
				}
				catch(Exception _ex) { }
				songChanging = true;
				onDemandFetcher.method558(2, nextSong);
				while(onDemandFetcher.getNodeCount() > 0)
				{
					processOnDemandQueue();
					try
					{
						Thread.sleep(100L);
					}
					catch(Exception _ex) { }
					if(onDemandFetcher.anInt1349 > 3)
					{
						loadError();
						return;
					}
				}
			}
	
			drawLoadingText(80, "Unpacking media");
			
			/* Custom sprite unpacking */
			chatArea = new Sprite("chat/","chatarea");
			chatButtons = new Sprite("chat/","chatbuttons");
			chatButtonH = new Sprite("chat/","chathover");
			chatButtonC = new Sprite("chat/","chatclicked");
			chatButtonHC = new Sprite("chat/","chatclickedh");
			reportH = new Sprite("chat/","reporthover");
			tabArea = new Sprite("frame/","tabarea");
			mapArea = new Sprite("frame/","maparea");
			compass = new Sprite("frame/","compass");
            for(int nSI = 0; nSI < 14; nSI++)
                newSideIcons[nSI] = new Sprite("icons/","sideicon_"+nSI);
			
			/**/
			mapBack = new Background(streamLoader_2, "mapback", 0);
			/*for(int j3 = 0; j3 < 14; j3++)
				sideIcons[j3] = new Sprite(streamLoader_2, "sideicons", j3);*/
			for(int r1 = 0; r1 < 10; r1++)
				redStones[r1] = new Sprite(streamLoader_2, "redstones", r1);

//			compass = new Sprite(streamLoader_2, "compass", 0);
			mapEdge = new Sprite(streamLoader_2, "mapedge", 0);
			mapEdge.method345();
			try
			{
				for(int k3 = 0; k3 < 100; k3++)
					mapScenes[k3] = new Background(streamLoader_2, "mapscene", k3);
			}
			catch(Exception _ex) { }
			try
			{
				for(int l3 = 0; l3 < 100; l3++)
					mapFunctions[l3] = new Sprite(streamLoader_2, "mapfunction", l3);
			}
			catch(Exception _ex) { }
			try
			{
				for(int i4 = 0; i4 < 20; i4++)
					hitMarks[i4] = new Sprite(streamLoader_2, "hitmarks", i4);
			}
			catch(Exception _ex) { }
			try
            {
				for(int h1 = 0; h1 < 6; h1++)
					headIconsHint[h1] = new Sprite(streamLoader_2, "headicons_hint", h1);
            }
            catch(Exception _ex) { }
			try
			{
			for(int j4 = 0; j4 < 8; j4++)
                    headIcons[j4] = new Sprite(streamLoader_2, "headicons_prayer", j4);
				for(int j45 = 0; j45 < 3; j45++)
                    skullIcons[j45] = new Sprite(streamLoader_2, "headicons_pk", j45 );
			}
			catch(Exception _ex) { }
			mapFlag = new Sprite(streamLoader_2, "mapmarker", 0);
			mapMarker = new Sprite(streamLoader_2, "mapmarker", 1);
			for(int k4 = 0; k4 < 8; k4++)
				crosses[k4] = new Sprite(streamLoader_2, "cross", k4);

			mapDotItem = new Sprite(streamLoader_2, "mapdots", 0);
			mapDotNPC = new Sprite(streamLoader_2, "mapdots", 1);
			mapDotPlayer = new Sprite(streamLoader_2, "mapdots", 2);
			mapDotFriend = new Sprite(streamLoader_2, "mapdots", 3);
			mapDotTeam = new Sprite(streamLoader_2, "mapdots", 4);
			backBase1 = new Background(streamLoader_2, "backbase", 0);
			scrollBar1 = new Background(streamLoader_2, "scrollbar", 0);
			scrollBar2 = new Background(streamLoader_2, "scrollbar", 1);

			for(int l4 = 0; l4 < 2; l4++)
				modIcons[l4] = new Background(streamLoader_2, "mod_icons", l4);

			Sprite sprite = new Sprite("frame/","frame");
			frame = new RSImageProducer(sprite.myWidth, sprite.myHeight, getGameComponent());
			sprite.method346(0, 0);

			int i5 = (int)(Math.random() * 21D) - 10;
			int j5 = (int)(Math.random() * 21D) - 10;
			int k5 = (int)(Math.random() * 21D) - 10;
			int l5 = (int)(Math.random() * 41D) - 20;
			for(int i6 = 0; i6 < 100; i6++)
			{
				if(mapFunctions[i6] != null)
					mapFunctions[i6].method344(i5 + l5, j5 + l5, k5 + l5);
				if(mapScenes[i6] != null)
					mapScenes[i6].method360(i5 + l5, j5 + l5, k5 + l5);
			}

			drawLoadingText(83, "Unpacking textures");
			Texture.method368(streamLoader_3);
			Texture.method372(0.80000000000000004D);
			Texture.method367();
			drawLoadingText(86, "Unpacking config");
			Animation.unpackConfig(streamLoader);
			ObjectDef.unpackConfig(streamLoader);
			Flo.unpackConfig(streamLoader);
			ItemDef.unpackConfig(streamLoader);
			EntityDef.unpackConfig(streamLoader);
			IDK.unpackConfig(streamLoader);
			SpotAnim.unpackConfig(streamLoader);
			Varp.unpackConfig(streamLoader);
			VarBit.unpackConfig(streamLoader);
			ItemDef.isMembers = isMembers;
			if(!lowMem)
			{
				drawLoadingText(90, "Unpacking sounds");
				byte abyte0[] = streamLoader_5.getDataForName("sounds.dat");
				Stream stream = new Stream(abyte0);
				Sounds.unpack(stream);
			}
			drawLoadingText(95, "Unpacking interfaces");
			TextDrawingArea aclass30_sub2_sub1_sub4s[] = {
					smallText, aTextDrawingArea_1271, chatTextDrawingArea, aTextDrawingArea_1273
			};
			RSInterface.unpack(streamLoader_1, aclass30_sub2_sub1_sub4s, streamLoader_2);
			drawLoadingText(100, "Preparing game engine");
			for(int j6 = 0; j6 < 33; j6++)
			{
				int k6 = 999;
				int i7 = 0;
				for(int k7 = 0; k7 < 34; k7++)
				{
					if(mapBack.aByteArray1450[k7 + j6 * mapBack.anInt1452] == 0)
					{
						if(k6 == 999)
							k6 = k7;
						continue;
					}
					if(k6 == 999)
						continue;
					i7 = k7;
					break;
				}

				anIntArray968[j6] = k6;
				anIntArray1057[j6] = i7 - k6;
			}

			for(int l6 = 5; l6 < 156; l6++)
			{
				int j7 = 999;
				int l7 = 0;
				for(int j8 = 25; j8 < 172; j8++)
				{
					if(mapBack.aByteArray1450[j8 + l6 * mapBack.anInt1452] == 0 && (j8 > 34 || l6 > 34))
					{
						if(j7 == 999)
							j7 = j8;
						continue;
					}
					if(j7 == 999)
						continue;
					l7 = j8;
					break;
				}

				anIntArray1052[l6 - 5] = j7 - 25;
				anIntArray1229[l6 - 5] = l7 - j7;
			}

			Texture.method365(519, 165);
			anIntArray1180 = Texture.anIntArray1472;
			Texture.method365(246, 335);
			anIntArray1181 = Texture.anIntArray1472;
			Texture.method365(512, 334);
			anIntArray1182 = Texture.anIntArray1472;
			int ai[] = new int[9];
			for(int i8 = 0; i8 < 9; i8++)
			{
				int k8 = 128 + i8 * 32 + 15;
				int l8 = 600 + k8 * 3;
				int i9 = Texture.anIntArray1470[k8];
				ai[i8] = l8 * i9 >> 16;
			}

			WorldController.method310(500, 800, 512, 334, ai);
			Censor.loadConfig(streamLoader_4);
			mouseDetection = new MouseDetection(this);
			startRunnable(mouseDetection, 10);
			Animable_Sub5.clientInstance = this;
			ObjectDef.clientInstance = this;
			EntityDef.clientInstance = this;
			return;
		}
		catch(Exception exception)
		{
			signlink.reporterror("loaderror " + aString1049 + " " + anInt1079);
		}
		loadingError = true;
	}

	private void method91(Stream stream, int i)
	{
		while(stream.bitPosition + 10 < i * 8)
		{
			int j = stream.readBits(11);
			if(j == 2047)
				break;
			if(playerArray[j] == null)
			{
				playerArray[j] = new Player();
				if(aStreamArray895s[j] != null)
					playerArray[j].updatePlayer(aStreamArray895s[j]);
			}
			playerIndices[playerCount++] = j;
			Player player = playerArray[j];
			player.anInt1537 = loopCycle;
			int k = stream.readBits(1);
			if(k == 1)
				anIntArray894[anInt893++] = j;
			int l = stream.readBits(1);
			int i1 = stream.readBits(5);
			if(i1 > 15)
				i1 -= 32;
			int j1 = stream.readBits(5);
			if(j1 > 15)
				j1 -= 32;
			player.setPos(myPlayer.smallX[0] + j1, myPlayer.smallY[0] + i1, l == 1);
		}
		stream.finishBitAccess();
	}

	private void processMainScreenClick()
	{
		if(anInt1021 != 0)
			return;
		if(super.clickMode3 == 1)
		{
			int i = super.saveClickX - 25 - 550;
			int j = super.saveClickY - 5 - 4;
			if(i >= 0 && j >= 0 && i < 146 && j < 151)
			{
				i -= 73;
				j -= 75;
				int k = minimapInt1 + minimapInt2 & 0x7ff;
				int i1 = Texture.anIntArray1470[k];
				int j1 = Texture.anIntArray1471[k];
				i1 = i1 * (minimapInt3 + 256) >> 8;
				j1 = j1 * (minimapInt3 + 256) >> 8;
				int k1 = j * i1 + i * j1 >> 11;
				int l1 = j * j1 - i * i1 >> 11;
				int i2 = myPlayer.x + k1 >> 7;
				int j2 = myPlayer.y - l1 >> 7;
				boolean flag1 = doWalkTo(1, 0, 0, 0, myPlayer.smallY[0], 0, 0, j2, myPlayer.smallX[0], true, i2);
				if(flag1)
				{
					stream.writeWordBigEndian(i);
					stream.writeWordBigEndian(j);
					stream.writeWord(minimapInt1);
					stream.writeWordBigEndian(57);
					stream.writeWordBigEndian(minimapInt2);
					stream.writeWordBigEndian(minimapInt3);
					stream.writeWordBigEndian(89);
					stream.writeWord(myPlayer.x);
					stream.writeWord(myPlayer.y);
					stream.writeWordBigEndian(anInt1264);
					stream.writeWordBigEndian(63);
				}
			}
			anInt1117++;
			if(anInt1117 > 1151)
			{
				anInt1117 = 0;
				stream.createFrame(246);
				stream.writeWordBigEndian(0);
				int l = stream.currentOffset;
				if((int)(Math.random() * 2D) == 0)
					stream.writeWordBigEndian(101);
				stream.writeWordBigEndian(197);
				stream.writeWord((int)(Math.random() * 65536D));
				stream.writeWordBigEndian((int)(Math.random() * 256D));
				stream.writeWordBigEndian(67);
				stream.writeWord(14214);
				if((int)(Math.random() * 2D) == 0)
					stream.writeWord(29487);
				stream.writeWord((int)(Math.random() * 65536D));
				if((int)(Math.random() * 2D) == 0)
					stream.writeWordBigEndian(220);
				stream.writeWordBigEndian(180);
				stream.writeBytes(stream.currentOffset - l);
			}
		}
	}

	private String interfaceIntToString(int j)
	{
		if(j < 0x3b9ac9ff)
			return String.valueOf(j);
		else
			return "*";
	}

	private void showErrorScreen()
	{
		Graphics g = getGameComponent().getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, 765, 503);
		method4(1);
		if(loadingError)
		{
			aBoolean831 = false;
			g.setFont(new Font("Helvetica", 1, 16));
			g.setColor(Color.yellow);
			int k = 35;
			g.drawString("Sorry, an error has occured whilst loading Vscape", 30, k);
			k += 50;
			g.setColor(Color.white);
			g.drawString("To fix this try the following (in order):", 30, k);
			k += 50;
			g.setColor(Color.white);
			g.setFont(new Font("Helvetica", 1, 12));
			g.drawString("1: Try closing ALL open web-browser windows, and reloading", 30, k);
			k += 30;
			g.drawString("2: Try clearing your web-browsers cache from tools->internet options", 30, k);
			k += 30;
			g.drawString("3: Try using a different game-world", 30, k);
			k += 30;
			g.drawString("4: Try rebooting your computer", 30, k);
			k += 30;
			g.drawString("5: Try selecting a different version of Java from the play-game menu", 30, k);
		}
		if(genericLoadingError)
		{
			aBoolean831 = false;
			g.setFont(new Font("Helvetica", 1, 20));
			g.setColor(Color.white);
			g.drawString("Error - unable to load game!", 50, 50);
			g.drawString("To play Vscape make sure you play from", 50, 100);
			g.drawString("http://www.runescape.com", 50, 150);
		}
		if(rsAlreadyLoaded)
		{
			aBoolean831 = false;
			g.setColor(Color.yellow);
			int l = 35;
			g.drawString("Error a copy of Vscape already appears to be loaded", 30, l);
			l += 50;
			g.setColor(Color.white);
			g.drawString("To fix this try the following (in order):", 30, l);
			l += 50;
			g.setColor(Color.white);
			g.setFont(new Font("Helvetica", 1, 12));
			g.drawString("1: Try closing ALL open web-browser windows, and reloading", 30, l);
			l += 30;
			g.drawString("2: Try rebooting your computer, and reloading", 30, l);
			l += 30;
		}
	}

	public URL getCodeBase()
	{		
		try
		{
		return new URL(server +":" + (80 + portOff));
		}
		catch(Exception _ex) { }
		return null;
	}

	private void method95()
	{
		for(int j = 0; j < npcCount; j++)
		{
			int k = npcIndices[j];
			NPC npc = npcArray[k];
			if(npc != null)
				method96(npc);
		}
	}

	private void method96(Entity entity) {
		if(entity.x < 128 || entity.y < 128 || entity.x >= 13184 || entity.y >= 13184) {
			entity.anim = -1;
			entity.anInt1520 = -1;
			entity.anInt1547 = 0;
			entity.anInt1548 = 0;
			entity.x = entity.smallX[0] * 128 + entity.anInt1540 * 64;
			entity.y = entity.smallY[0] * 128 + entity.anInt1540 * 64;
			entity.method446();
		}
		if(entity == myPlayer && (entity.x < 1536 || entity.y < 1536 || entity.x >= 11776 || entity.y >= 11776)) {
			entity.anim = -1;
			entity.anInt1520 = -1;
			entity.anInt1547 = 0;
			entity.anInt1548 = 0;
			entity.x = entity.smallX[0] * 128 + entity.anInt1540 * 64;
			entity.y = entity.smallY[0] * 128 + entity.anInt1540 * 64;
			entity.method446();
		}
		if(entity.anInt1547 > loopCycle)
			method97(entity);
		else
		if(entity.anInt1548 >= loopCycle)
			method98(entity);
		else
			method99(entity);
		method100(entity);
		method101(entity);
	}

	private void method97(Entity entity)
	{
		int i = entity.anInt1547 - loopCycle;
		int j = entity.anInt1543 * 128 + entity.anInt1540 * 64;
		int k = entity.anInt1545 * 128 + entity.anInt1540 * 64;
		entity.x += (j - entity.x) / i;
		entity.y += (k - entity.y) / i;
		entity.anInt1503 = 0;
		if(entity.anInt1549 == 0)
			entity.turnDirection = 1024;
		if(entity.anInt1549 == 1)
			entity.turnDirection = 1536;
		if(entity.anInt1549 == 2)
			entity.turnDirection = 0;
		if(entity.anInt1549 == 3)
			entity.turnDirection = 512;
	}

	private void method98(Entity entity)
	{
		if(entity.anInt1548 == loopCycle || entity.anim == -1 || entity.anInt1529 != 0 || entity.anInt1528 + 1 > Animation.anims[entity.anim].method258(entity.anInt1527))
		{
			int i = entity.anInt1548 - entity.anInt1547;
			int j = loopCycle - entity.anInt1547;
			int k = entity.anInt1543 * 128 + entity.anInt1540 * 64;
			int l = entity.anInt1545 * 128 + entity.anInt1540 * 64;
			int i1 = entity.anInt1544 * 128 + entity.anInt1540 * 64;
			int j1 = entity.anInt1546 * 128 + entity.anInt1540 * 64;
			entity.x = (k * (i - j) + i1 * j) / i;
			entity.y = (l * (i - j) + j1 * j) / i;
		}
		entity.anInt1503 = 0;
		if(entity.anInt1549 == 0)
			entity.turnDirection = 1024;
		if(entity.anInt1549 == 1)
			entity.turnDirection = 1536;
		if(entity.anInt1549 == 2)
			entity.turnDirection = 0;
		if(entity.anInt1549 == 3)
			entity.turnDirection = 512;
		entity.anInt1552 = entity.turnDirection;
	}

	private void method99(Entity entity)
	{
		entity.anInt1517 = entity.anInt1511;
		if(entity.smallXYIndex == 0)
		{
			entity.anInt1503 = 0;
			return;
		}
		if(entity.anim != -1 && entity.anInt1529 == 0)
		{
			Animation animation = Animation.anims[entity.anim];
			if(entity.anInt1542 > 0 && animation.anInt363 == 0)
			{
				entity.anInt1503++;
				return;
			}
			if(entity.anInt1542 <= 0 && animation.anInt364 == 0)
			{
				entity.anInt1503++;
				return;
			}
		}
		int i = entity.x;
		int j = entity.y;
		int k = entity.smallX[entity.smallXYIndex - 1] * 128 + entity.anInt1540 * 64;
		int l = entity.smallY[entity.smallXYIndex - 1] * 128 + entity.anInt1540 * 64;
		if(k - i > 256 || k - i < -256 || l - j > 256 || l - j < -256)
		{
			entity.x = k;
			entity.y = l;
			return;
		}
		if(i < k)
		{
			if(j < l)
				entity.turnDirection = 1280;
			else
			if(j > l)
				entity.turnDirection = 1792;
			else
				entity.turnDirection = 1536;
		} else
		if(i > k)
		{
			if(j < l)
				entity.turnDirection = 768;
			else
			if(j > l)
				entity.turnDirection = 256;
			else
				entity.turnDirection = 512;
		} else
		if(j < l)
			entity.turnDirection = 1024;
		else
			entity.turnDirection = 0;
		int i1 = entity.turnDirection - entity.anInt1552 & 0x7ff;
		if(i1 > 1024)
			i1 -= 2048;
		int j1 = entity.anInt1555;
		if(i1 >= -256 && i1 <= 256)
			j1 = entity.anInt1554;
		else
		if(i1 >= 256 && i1 < 768)
			j1 = entity.anInt1557;
		else
		if(i1 >= -768 && i1 <= -256)
			j1 = entity.anInt1556;
		if(j1 == -1)
			j1 = entity.anInt1554;
		entity.anInt1517 = j1;
		int k1 = 4;
		if(entity.anInt1552 != entity.turnDirection && entity.interactingEntity == -1 && entity.anInt1504 != 0)
			k1 = 2;
		if(entity.smallXYIndex > 2)
			k1 = 6;
		if(entity.smallXYIndex > 3)
			k1 = 8;
		if(entity.anInt1503 > 0 && entity.smallXYIndex > 1)
		{
			k1 = 8;
			entity.anInt1503--;
		}
		if(entity.aBooleanArray1553[entity.smallXYIndex - 1])
			k1 <<= 1;
		if(k1 >= 8 && entity.anInt1517 == entity.anInt1554 && entity.anInt1505 != -1)
			entity.anInt1517 = entity.anInt1505;
		if(i < k)
		{
			entity.x += k1;
			if(entity.x > k)
				entity.x = k;
		} else
		if(i > k)
		{
			entity.x -= k1;
			if(entity.x < k)
				entity.x = k;
		}
		if(j < l)
		{
			entity.y += k1;
			if(entity.y > l)
				entity.y = l;
		} else
		if(j > l)
		{
			entity.y -= k1;
			if(entity.y < l)
				entity.y = l;
		}
		if(entity.x == k && entity.y == l)
		{
			entity.smallXYIndex--;
			if(entity.anInt1542 > 0)
				entity.anInt1542--;
		}
	}

	private void method100(Entity entity)
	{
		if(entity.anInt1504 == 0)
			return;
		if(entity.interactingEntity != -1 && entity.interactingEntity < 32768)
		{
			NPC npc = npcArray[entity.interactingEntity];
			if(npc != null)
			{
				int i1 = entity.x - npc.x;
				int k1 = entity.y - npc.y;
				if(i1 != 0 || k1 != 0)
					entity.turnDirection = (int)(Math.atan2(i1, k1) * 325.94900000000001D) & 0x7ff;
			}
		}
		if(entity.interactingEntity >= 32768)
		{
			int j = entity.interactingEntity - 32768;
			if(j == unknownInt10)
				j = myPlayerIndex;
			Player player = playerArray[j];
			if(player != null)
			{
				int l1 = entity.x - player.x;
				int i2 = entity.y - player.y;
				if(l1 != 0 || i2 != 0)
					entity.turnDirection = (int)(Math.atan2(l1, i2) * 325.94900000000001D) & 0x7ff;
			}
		}
		if((entity.anInt1538 != 0 || entity.anInt1539 != 0) && (entity.smallXYIndex == 0 || entity.anInt1503 > 0))
		{
			int k = entity.x - (entity.anInt1538 - baseX - baseX) * 64;
			int j1 = entity.y - (entity.anInt1539 - baseY - baseY) * 64;
			if(k != 0 || j1 != 0)
				entity.turnDirection = (int)(Math.atan2(k, j1) * 325.94900000000001D) & 0x7ff;
			entity.anInt1538 = 0;
			entity.anInt1539 = 0;
		}
		int l = entity.turnDirection - entity.anInt1552 & 0x7ff;
		if(l != 0)
		{
			if(l < entity.anInt1504 || l > 2048 - entity.anInt1504)
				entity.anInt1552 = entity.turnDirection;
			else
			if(l > 1024)
				entity.anInt1552 -= entity.anInt1504;
			else
				entity.anInt1552 += entity.anInt1504;
			entity.anInt1552 &= 0x7ff;
			if(entity.anInt1517 == entity.anInt1511 && entity.anInt1552 != entity.turnDirection)
			{
				if(entity.anInt1512 != -1)
				{
					entity.anInt1517 = entity.anInt1512;
					return;
				}
				entity.anInt1517 = entity.anInt1554;
			}
		}
	}

	private void method101(Entity entity)
	{
		entity.aBoolean1541 = false;
		if(entity.anInt1517 != -1)
		{
			Animation animation = Animation.anims[entity.anInt1517];
			entity.anInt1519++;
			if(entity.anInt1518 < animation.anInt352 && entity.anInt1519 > animation.method258(entity.anInt1518))
			{
				entity.anInt1519 = 0;
				entity.anInt1518++;
			}
			if(entity.anInt1518 >= animation.anInt352)
			{
				entity.anInt1519 = 0;
				entity.anInt1518 = 0;
			}
		}
		if(entity.anInt1520 != -1 && loopCycle >= entity.anInt1523)
		{
			if(entity.anInt1521 < 0)
				entity.anInt1521 = 0;
			Animation animation_1 = SpotAnim.cache[entity.anInt1520].aAnimation_407;
			for(entity.anInt1522++; entity.anInt1521 < animation_1.anInt352 && entity.anInt1522 > animation_1.method258(entity.anInt1521); entity.anInt1521++)
				entity.anInt1522 -= animation_1.method258(entity.anInt1521);

			if(entity.anInt1521 >= animation_1.anInt352 && (entity.anInt1521 < 0 || entity.anInt1521 >= animation_1.anInt352))
				entity.anInt1520 = -1;
		}
		if(entity.anim != -1 && entity.anInt1529 <= 1)
		{
			Animation animation_2 = Animation.anims[entity.anim];
			if(animation_2.anInt363 == 1 && entity.anInt1542 > 0 && entity.anInt1547 <= loopCycle && entity.anInt1548 < loopCycle)
			{
				entity.anInt1529 = 1;
				return;
			}
		}
		if(entity.anim != -1 && entity.anInt1529 == 0)
		{
			Animation animation_3 = Animation.anims[entity.anim];
			for(entity.anInt1528++; entity.anInt1527 < animation_3.anInt352 && entity.anInt1528 > animation_3.method258(entity.anInt1527); entity.anInt1527++)
				entity.anInt1528 -= animation_3.method258(entity.anInt1527);

			if(entity.anInt1527 >= animation_3.anInt352)
			{
				entity.anInt1527 -= animation_3.anInt356;
				entity.anInt1530++;
				if(entity.anInt1530 >= animation_3.anInt362)
					entity.anim = -1;
				if(entity.anInt1527 < 0 || entity.anInt1527 >= animation_3.anInt352)
					entity.anim = -1;
			}
			entity.aBoolean1541 = animation_3.aBoolean358;
		}
		if(entity.anInt1529 > 0)
			entity.anInt1529--;
	}

	private void drawGameScreen()
	{
		if(welcomeScreenRaised)
		{
			welcomeScreenRaised = false;
			frame.drawGraphics(0, super.graphics, 0);
			needDrawTabArea = true;
			inputTaken = true;
			tabAreaAltered = true;
			aBoolean1233 = true;
			if(loadingStage != 2)
			{
				aRSImageProducer_1165.drawGraphics(4, super.graphics, 4);
				aRSImageProducer_1164.drawGraphics(0, super.graphics, 519);
			}
		}
		if(loadingStage == 2)
			method146();
		if(menuOpen && menuScreenArea == 1)
			needDrawTabArea = true;
		if(invOverlayInterfaceID != -1)
		{
			boolean flag1 = method119(anInt945, invOverlayInterfaceID);
			if(flag1)
				needDrawTabArea = true;
		}
		if(atInventoryInterfaceType == 2)
			needDrawTabArea = true;
		if(activeInterfaceType == 2)
			needDrawTabArea = true;
		if(needDrawTabArea)
		{
			drawTabArea();
			needDrawTabArea = false;
		}
		if(backDialogID == -1)
		{
			aClass9_1059.scrollPosition = anInt1211 - anInt1089 - 110;
			if(super.mouseX > 478 && super.mouseX < 580 && super.mouseY > 342)
				method65(494, 110, super.mouseX - 0, super.mouseY - 348, aClass9_1059, 0, false, anInt1211);
			int i = anInt1211 - 110 - aClass9_1059.scrollPosition;
			if(i < 0)
				i = 0;
			if(i > anInt1211 - 110)
				i = anInt1211 - 110;
			if(anInt1089 != i)
			{
				anInt1089 = i;
				inputTaken = true;
			}
		}
		if(backDialogID != -1)
		{
			boolean flag2 = method119(anInt945, backDialogID);
			if(flag2)
				inputTaken = true;
		}
		if(atInventoryInterfaceType == 3)
			inputTaken = true;
		if(activeInterfaceType == 3)
			inputTaken = true;
		if(aString844 != null)
			inputTaken = true;
		if(menuOpen && menuScreenArea == 2)
			inputTaken = true;
		if(inputTaken)
		{
			drawChatArea();
			inputTaken = false;
		}
		if(loadingStage == 2)
		{
			drawMinimap();
			aRSImageProducer_1164.drawGraphics(0, super.graphics, 519);
		}
		if(anInt1054 != -1)
			tabAreaAltered = true;
		if(tabAreaAltered)
		{
			if(anInt1054 != -1 && anInt1054 == tabID)
			{
				anInt1054 = -1;
				stream.createFrame(120);
				stream.writeWordBigEndian(tabID);
			}
			tabAreaAltered = false;
			aRSImageProducer_1125.initDrawingArea();
			aRSImageProducer_1165.initDrawingArea();
		}
		anInt945 = 0;
	}

	private boolean buildFriendsListMenu(RSInterface class9)
	{
		int i = class9.contentType;
		if(i >= 1 && i <= 200 || i >= 701 && i <= 900)
		{
			if(i >= 801)
				i -= 701;
			else
			if(i >= 701)
				i -= 601;
			else
			if(i >= 101)
				i -= 101;
			else
				i--;
			menuActionName[menuActionRow] = "Remove @whi@" + friendsList[i];
			menuActionID[menuActionRow] = 792;
			menuActionRow++;
			menuActionName[menuActionRow] = "Message @whi@" + friendsList[i];
			menuActionID[menuActionRow] = 639;
			menuActionRow++;
			return true;
		}
		if(i >= 401 && i <= 500)
		{
			menuActionName[menuActionRow] = "Remove @whi@" + class9.message;
			menuActionID[menuActionRow] = 322;
			menuActionRow++;
			return true;
		} else
		{
			return false;
		}
	}

	private void method104()
	{
		Animable_Sub3 class30_sub2_sub4_sub3 = (Animable_Sub3)aClass19_1056.reverseGetFirst();
		for(; class30_sub2_sub4_sub3 != null; class30_sub2_sub4_sub3 = (Animable_Sub3)aClass19_1056.reverseGetNext())
			if(class30_sub2_sub4_sub3.anInt1560 != plane || class30_sub2_sub4_sub3.aBoolean1567)
				class30_sub2_sub4_sub3.unlink();
			else
			if(loopCycle >= class30_sub2_sub4_sub3.anInt1564)
			{
				class30_sub2_sub4_sub3.method454(anInt945);
				if(class30_sub2_sub4_sub3.aBoolean1567)
					class30_sub2_sub4_sub3.unlink();
				else
					worldController.method285(class30_sub2_sub4_sub3.anInt1560, 0, class30_sub2_sub4_sub3.anInt1563, -1, class30_sub2_sub4_sub3.anInt1562, 60, class30_sub2_sub4_sub3.anInt1561, class30_sub2_sub4_sub3, false);
			}

	}

	private void drawInterface(int j, int k, RSInterface class9, int l)
	{
		if(class9.type != 0 || class9.children == null)
			return;
		if(class9.isMouseoverTriggered && anInt1026 != class9.id && anInt1048 != class9.id && anInt1039 != class9.id)
			return;
		int i1 = DrawingArea.topX;
		int j1 = DrawingArea.topY;
		int k1 = DrawingArea.bottomX;
		int l1 = DrawingArea.bottomY;
		DrawingArea.setDrawingArea(l + class9.height, k, k + class9.width, l);
		int i2 = class9.children.length;
		for(int j2 = 0; j2 < i2; j2++)
		{
			int k2 = class9.childX[j2] + k;
			int l2 = (class9.childY[j2] + l) - j;
			RSInterface class9_1 = RSInterface.interfaceCache[class9.children[j2]];
			k2 += class9_1.anInt263;
			l2 += class9_1.anInt265;
			if(class9_1.contentType > 0)
				drawFriendsListOrWelcomeScreen(class9_1);
			if(class9_1.type == 0)
			{
				if(class9_1.scrollPosition > class9_1.scrollMax - class9_1.height)
					class9_1.scrollPosition = class9_1.scrollMax - class9_1.height;
				if(class9_1.scrollPosition < 0)
					class9_1.scrollPosition = 0;
				drawInterface(class9_1.scrollPosition, k2, class9_1, l2);
				if(class9_1.scrollMax > class9_1.height)
					drawScrollbar(class9_1.height, class9_1.scrollPosition, l2, k2 + class9_1.width, class9_1.scrollMax);
			} else
			if(class9_1.type != 1)
				if(class9_1.type == 2)
				{
					int i3 = 0;
					for(int l3 = 0; l3 < class9_1.height; l3++)
					{
						for(int l4 = 0; l4 < class9_1.width; l4++)
						{
							int k5 = k2 + l4 * (32 + class9_1.invSpritePadX);
							int j6 = l2 + l3 * (32 + class9_1.invSpritePadY);
							if(i3 < 20)
							{
								k5 += class9_1.spritesX[i3];
								j6 += class9_1.spritesY[i3];
							}
							if(class9_1.inv[i3] > 0)
							{
								int k6 = 0;
								int j7 = 0;
								int j9 = class9_1.inv[i3] - 1;
								if(k5 > DrawingArea.topX - 32 && k5 < DrawingArea.bottomX && j6 > DrawingArea.topY - 32 && j6 < DrawingArea.bottomY || activeInterfaceType != 0 && anInt1085 == i3)
								{
									int l9 = 0;
									if(itemSelected == 1 && anInt1283 == i3 && anInt1284 == class9_1.id)
										l9 = 0xffffff;
									Sprite class30_sub2_sub1_sub1_2 = ItemDef.getSprite(j9, class9_1.invStackSizes[i3], l9);
									if(class30_sub2_sub1_sub1_2 != null)
									{
										if(activeInterfaceType != 0 && anInt1085 == i3 && anInt1084 == class9_1.id)
										{
											k6 = super.mouseX - anInt1087;
											j7 = super.mouseY - anInt1088;
											if(k6 < 5 && k6 > -5)
												k6 = 0;
											if(j7 < 5 && j7 > -5)
												j7 = 0;
											if(anInt989 < 5)
											{
												k6 = 0;
												j7 = 0;
											}
											class30_sub2_sub1_sub1_2.drawSprite1(k5 + k6, j6 + j7);
											if(j6 + j7 < DrawingArea.topY && class9.scrollPosition > 0)
											{
												int i10 = (anInt945 * (DrawingArea.topY - j6 - j7)) / 3;
												if(i10 > anInt945 * 10)
													i10 = anInt945 * 10;
												if(i10 > class9.scrollPosition)
													i10 = class9.scrollPosition;
												class9.scrollPosition -= i10;
												anInt1088 += i10;
											}
											if(j6 + j7 + 32 > DrawingArea.bottomY && class9.scrollPosition < class9.scrollMax - class9.height)
											{
												int j10 = (anInt945 * ((j6 + j7 + 32) - DrawingArea.bottomY)) / 3;
												if(j10 > anInt945 * 10)
													j10 = anInt945 * 10;
												if(j10 > class9.scrollMax - class9.height - class9.scrollPosition)
													j10 = class9.scrollMax - class9.height - class9.scrollPosition;
												class9.scrollPosition += j10;
												anInt1088 -= j10;
											}
										} else
										if(atInventoryInterfaceType != 0 && atInventoryIndex == i3 && atInventoryInterface == class9_1.id)
											class30_sub2_sub1_sub1_2.drawSprite1(k5, j6);
										else
											class30_sub2_sub1_sub1_2.drawSprite(k5, j6);
										if(class30_sub2_sub1_sub1_2.anInt1444 == 33 || class9_1.invStackSizes[i3] != 1)
										{
											int k10 = class9_1.invStackSizes[i3];
											smallText.method385(0, intToKOrMil(k10), j6 + 10 + j7, k5 + 1 + k6);
											smallText.method385(0xffff00, intToKOrMil(k10), j6 + 9 + j7, k5 + k6);
										}
									}
								}
							} else
							if(class9_1.sprites != null && i3 < 20)
							{
								Sprite class30_sub2_sub1_sub1_1 = class9_1.sprites[i3];
								if(class30_sub2_sub1_sub1_1 != null)
									class30_sub2_sub1_sub1_1.drawSprite(k5, j6);
							}
							i3++;
						}

					}

				} else
				if(class9_1.type == 3)
				{
					boolean flag = false;
					if(anInt1039 == class9_1.id || anInt1048 == class9_1.id || anInt1026 == class9_1.id)
						flag = true;
					int j3;
					if(interfaceIsSelected(class9_1))
					{
						j3 = class9_1.anInt219;
						if(flag && class9_1.anInt239 != 0)
							j3 = class9_1.anInt239;
					} else
					{
						j3 = class9_1.textColor;
						if(flag && class9_1.anInt216 != 0)
							j3 = class9_1.anInt216;
					}
					if(class9_1.aByte254 == 0)
					{
						if(class9_1.aBoolean227)
							DrawingArea.drawPixels(class9_1.height, l2, k2, j3, class9_1.width);
						else
							DrawingArea.fillPixels(k2, class9_1.width, class9_1.height, j3, l2);
					} else
					if(class9_1.aBoolean227)
						DrawingArea.method335(j3, l2, class9_1.width, class9_1.height, 256 - (class9_1.aByte254 & 0xff), k2);
					else
						DrawingArea.method338(l2, class9_1.height, 256 - (class9_1.aByte254 & 0xff), j3, class9_1.width, k2);
				} else
				if(class9_1.type == 4)
				{
					TextDrawingArea textDrawingArea = class9_1.textDrawingAreas;
					String s = class9_1.message;
					boolean flag1 = false;
					if(anInt1039 == class9_1.id || anInt1048 == class9_1.id || anInt1026 == class9_1.id)
						flag1 = true;
					int i4;
					if(interfaceIsSelected(class9_1))
					{
						i4 = class9_1.anInt219;
						if(flag1 && class9_1.anInt239 != 0)
							i4 = class9_1.anInt239;
						if(class9_1.aString228.length() > 0)
							s = class9_1.aString228;
					} else
					{
						i4 = class9_1.textColor;
						if(flag1 && class9_1.anInt216 != 0)
							i4 = class9_1.anInt216;
					}
					if(class9_1.atActionType == 6 && aBoolean1149)
					{
						s = "Please wait...";
						i4 = class9_1.textColor;
					}
					if(DrawingArea.width == 519)
					{
						if(i4 == 0xffff00)
							i4 = 255;
						if(i4 == 49152)
							i4 = 0xffffff;
					}
					for(int l6 = l2 + textDrawingArea.anInt1497; s.length() > 0; l6 += textDrawingArea.anInt1497)
					{
						if(s.indexOf("%") != -1)
						{
							do
							{
								int k7 = s.indexOf("%1");
								if(k7 == -1)
									break;
								s = s.substring(0, k7) + interfaceIntToString(extractInterfaceValues(class9_1, 0)) + s.substring(k7 + 2);
							} while(true);
							do
							{
								int l7 = s.indexOf("%2");
								if(l7 == -1)
									break;
								s = s.substring(0, l7) + interfaceIntToString(extractInterfaceValues(class9_1, 1)) + s.substring(l7 + 2);
							} while(true);
							do
							{
								int i8 = s.indexOf("%3");
								if(i8 == -1)
									break;
								s = s.substring(0, i8) + interfaceIntToString(extractInterfaceValues(class9_1, 2)) + s.substring(i8 + 2);
							} while(true);
							do
							{
								int j8 = s.indexOf("%4");
								if(j8 == -1)
									break;
								s = s.substring(0, j8) + interfaceIntToString(extractInterfaceValues(class9_1, 3)) + s.substring(j8 + 2);
							} while(true);
							do
							{
								int k8 = s.indexOf("%5");
								if(k8 == -1)
									break;
								s = s.substring(0, k8) + interfaceIntToString(extractInterfaceValues(class9_1, 4)) + s.substring(k8 + 2);
							} while(true);
						}
						int l8 = s.indexOf("\\n");
						String s1;
						if(l8 != -1)
						{
							s1 = s.substring(0, l8);
							s = s.substring(l8 + 2);
						} else
						{
							s1 = s;
							s = "";
						}
						if(class9_1.centerText)
							textDrawingArea.method382(i4, k2 + class9_1.width / 2, s1, l6, class9_1.textShadow);
						else
							textDrawingArea.method389(class9_1.textShadow, k2, i4, s1, l6);
					}
				} else if(class9_1.type == 5) {
					Sprite sprite;
					if(interfaceIsSelected(class9_1))
						sprite = class9_1.sprite2;
					else
						sprite = class9_1.sprite1;
					if(sprite != null)
						if(class9_1.drawsTransparent == true)
							sprite.drawSprite2(k2, l2);
					else
						sprite.drawSprite(k2, l2);
				} else if(class9_1.type == 6) {
					int k3 = Texture.textureInt1;
					int j4 = Texture.textureInt2;
					Texture.textureInt1 = k2 + class9_1.width / 2;
					Texture.textureInt2 = l2 + class9_1.height / 2;
					int i5 = Texture.anIntArray1470[class9_1.modelRotation1] * class9_1.anInt269 >> 16;
					int l5 = Texture.anIntArray1471[class9_1.modelRotation1] * class9_1.anInt269 >> 16;
					boolean flag2 = interfaceIsSelected(class9_1);
					int i7;
					if(flag2)
						i7 = class9_1.anInt258;
					else
						i7 = class9_1.anInt257;
					Model model;
					if(i7 == -1)
					{
						model = class9_1.method209(-1, -1, flag2);
					} else
					{
						Animation animation = Animation.anims[i7];
						model = class9_1.method209(animation.anIntArray354[class9_1.anInt246], animation.anIntArray353[class9_1.anInt246], flag2);
					}
					if(model != null)
						model.method482(class9_1.modelRotation2, 0, class9_1.modelRotation1, 0, i5, l5);
					Texture.textureInt1 = k3;
					Texture.textureInt2 = j4;
				} else
				if(class9_1.type == 7)
				{
					TextDrawingArea textDrawingArea_1 = class9_1.textDrawingAreas;
					int k4 = 0;
					for(int j5 = 0; j5 < class9_1.height; j5++)
					{
						for(int i6 = 0; i6 < class9_1.width; i6++)
						{
							if(class9_1.inv[k4] > 0)
							{
								ItemDef itemDef = ItemDef.forID(class9_1.inv[k4] - 1);
								String s2 = itemDef.name;
								if(itemDef.stackable || class9_1.invStackSizes[k4] != 1)
									s2 = s2 + " x" + intToKOrMilLongName(class9_1.invStackSizes[k4]);
								int i9 = k2 + i6 * (115 + class9_1.invSpritePadX);
								int k9 = l2 + j5 * (12 + class9_1.invSpritePadY);
								if(class9_1.centerText)
									textDrawingArea_1.method382(class9_1.textColor, i9 + class9_1.width / 2, s2, k9, class9_1.textShadow);
								else
									textDrawingArea_1.method389(class9_1.textShadow, i9, class9_1.textColor, s2, k9);
							}
							k4++;
						}

					}

				}
		}

		DrawingArea.setDrawingArea(l1, i1, k1, j1);
	}
	
	private void randomizeBackground(Background background)
	{
		int j = 256;
		for(int k = 0; k < anIntArray1190.length; k++)
			anIntArray1190[k] = 0;

		for(int l = 0; l < 5000; l++)
		{
			int i1 = (int)(Math.random() * 128D * (double)j);
			anIntArray1190[i1] = (int)(Math.random() * 256D);
		}

		for(int j1 = 0; j1 < 20; j1++)
		{
			for(int k1 = 1; k1 < j - 1; k1++)
			{
				for(int i2 = 1; i2 < 127; i2++)
				{
					int k2 = i2 + (k1 << 7);
					anIntArray1191[k2] = (anIntArray1190[k2 - 1] + anIntArray1190[k2 + 1] + anIntArray1190[k2 - 128] + anIntArray1190[k2 + 128]) / 4;
				}

			}

			int ai[] = anIntArray1190;
			anIntArray1190 = anIntArray1191;
			anIntArray1191 = ai;
		}

		if(background != null)
		{
			int l1 = 0;
			for(int j2 = 0; j2 < background.anInt1453; j2++)
			{
				for(int l2 = 0; l2 < background.anInt1452; l2++)
					if(background.aByteArray1450[l1++] != 0)
					{
						int i3 = l2 + 16 + background.anInt1454;
						int j3 = j2 + 16 + background.anInt1455;
						int k3 = i3 + (j3 << 7);
						anIntArray1190[k3] = 0;
					}

			}

		}
	}

	private void method107(int i, int j, Stream stream, Player player)
	{
		if((i & 0x400) != 0)
		{
			player.anInt1543 = stream.method428();
			player.anInt1545 = stream.method428();
			player.anInt1544 = stream.method428();
			player.anInt1546 = stream.method428();
			player.anInt1547 = stream.method436() + loopCycle;
			player.anInt1548 = stream.method435() + loopCycle;
			player.anInt1549 = stream.method428();
			player.method446();
		}
		if((i & 0x100) != 0)
		{
			player.anInt1520 = stream.method434();
			int k = stream.readDWord();
			player.anInt1524 = k >> 16;
			player.anInt1523 = loopCycle + (k & 0xffff);
			player.anInt1521 = 0;
			player.anInt1522 = 0;
			if(player.anInt1523 > loopCycle)
				player.anInt1521 = -1;
			if(player.anInt1520 == 65535)
				player.anInt1520 = -1;
		}
		if((i & 8) != 0)
		{
			int l = stream.method434();
			if(l == 65535)
				l = -1;
			int i2 = stream.method427();
			if(l == player.anim && l != -1)
			{
				int i3 = Animation.anims[l].anInt365;
				if(i3 == 1)
				{
					player.anInt1527 = 0;
					player.anInt1528 = 0;
					player.anInt1529 = i2;
					player.anInt1530 = 0;
				}
				if(i3 == 2)
					player.anInt1530 = 0;
			} else
			if(l == -1 || player.anim == -1 || Animation.anims[l].anInt359 >= Animation.anims[player.anim].anInt359)
			{
				player.anim = l;
				player.anInt1527 = 0;
				player.anInt1528 = 0;
				player.anInt1529 = i2;
				player.anInt1530 = 0;
				player.anInt1542 = player.smallXYIndex;
			}
		}
		if((i & 4) != 0)
		{
			player.textSpoken = stream.readString();
			if(player.textSpoken.charAt(0) == '~')
			{
				player.textSpoken = player.textSpoken.substring(1);
				pushMessage(player.textSpoken, 2, player.name);
			} else
			if(player == myPlayer)
				pushMessage(player.textSpoken, 2, player.name);
			player.anInt1513 = 0;
			player.anInt1531 = 0;
			player.textCycle = 150;
		}
		if((i & 0x80) != 0)
		{
			int i1 = stream.method434();
			int j2 = stream.readUnsignedByte();
			int j3 = stream.method427();
			int k3 = stream.currentOffset;
			if(player.name != null && player.visible)
			{
				long l3 = TextClass.longForName(player.name);
				boolean flag = false;
				if(j2 <= 1)
				{
					for(int i4 = 0; i4 < ignoreCount; i4++)
					{
						if(ignoreListAsLongs[i4] != l3)
							continue;
						flag = true;
						break;
					}

				}
				if(!flag && anInt1251 == 0)
					try
					{
						aStream_834.currentOffset = 0;
						stream.method442(j3, 0, aStream_834.buffer);
						aStream_834.currentOffset = 0;
						String s = TextInput.method525(j3, aStream_834);
						//s = Censor.doCensor(s);
						player.textSpoken = s;
						player.anInt1513 = i1 >> 8;
						player.privelage = j2;

						//entityMessage(player);
	
						player.anInt1531 = i1 & 0xff;
						player.textCycle = 150;
						if(j2 == 2 || j2 == 3)
							pushMessage(s, 1, "@cr2@" + player.name);
						else
						if(j2 == 1)
							pushMessage(s, 1, "@cr1@" + player.name);
						else
							pushMessage(s, 2, player.name);
					}
					catch(Exception exception)
					{
						signlink.reporterror("cde2");
					}
			}
			stream.currentOffset = k3 + j3;
		}
		if((i & 1) != 0)
		{
			player.interactingEntity = stream.method434();
			if(player.interactingEntity == 65535)
				player.interactingEntity = -1;
		}
		if((i & 0x10) != 0)
		{
			int j1 = stream.method427();
			byte abyte0[] = new byte[j1];
			Stream stream_1 = new Stream(abyte0);
			stream.readBytes(j1, 0, abyte0);
			aStreamArray895s[j] = stream_1;
			player.updatePlayer(stream_1);
		}
		if((i & 2) != 0)
		{
			player.anInt1538 = stream.method436();
			player.anInt1539 = stream.method434();
		}
		if((i & 0x20) != 0)
		{
			int k1 = stream.readUnsignedByte();
			int k2 = stream.method426();
			player.updateHitData(k2, k1, loopCycle);
			player.loopCycleStatus = loopCycle + 300;
			player.currentHealth = stream.method427();
			player.maxHealth = stream.readUnsignedByte();
		}
		if((i & 0x200) != 0)
		{
			int l1 = stream.readUnsignedByte();
			int l2 = stream.method428();
			player.updateHitData(l2, l1, loopCycle);
			player.loopCycleStatus = loopCycle + 300;
			player.currentHealth = stream.readUnsignedByte();
			player.maxHealth = stream.method427();
		}
	}

	private void method108()
	{
		try
		{
			int j = myPlayer.x + anInt1278;
			int k = myPlayer.y + anInt1131;
			if(anInt1014 - j < -500 || anInt1014 - j > 500 || anInt1015 - k < -500 || anInt1015 - k > 500)
			{
				anInt1014 = j;
				anInt1015 = k;
			}
			if(anInt1014 != j)
				anInt1014 += (j - anInt1014) / 16;
			if(anInt1015 != k)
				anInt1015 += (k - anInt1015) / 16;
			if(super.keyArray[1] == 1)
				anInt1186 += (-24 - anInt1186) / 2;
			else
			if(super.keyArray[2] == 1)
				anInt1186 += (24 - anInt1186) / 2;
			else
				anInt1186 /= 2;
			if(super.keyArray[3] == 1)
				anInt1187 += (12 - anInt1187) / 2;
			else
			if(super.keyArray[4] == 1)
				anInt1187 += (-12 - anInt1187) / 2;
			else
				anInt1187 /= 2;
			  minimapInt1 = minimapInt1 + anInt1186 / 2 & 0x7ff;
			  anInt1184 += anInt1187 / 2;
			if(anInt1184 < 128)
				anInt1184 = 128;
			if(anInt1184 > 383)
				anInt1184 = 383;
			int l = anInt1014 >> 7;
			int i1 = anInt1015 >> 7;
			int j1 = method42(plane, anInt1015, anInt1014);
			int k1 = 0;
			if(l > 3 && i1 > 3 && l < 100 && i1 < 100)
			{
				for(int l1 = l - 4; l1 <= l + 4; l1++)
				{
					for(int k2 = i1 - 4; k2 <= i1 + 4; k2++)
					{
						int l2 = plane;
						if(l2 < 3 && (byteGroundArray[1][l1][k2] & 2) == 2)
							l2++;
						int i3 = j1 - intGroundArray[l2][l1][k2];
						if(i3 > k1)
							k1 = i3;
					}

				}

			}
			anInt1005++;
			if(anInt1005 > 1512)
			{
				anInt1005 = 0;
				stream.createFrame(77);
				stream.writeWordBigEndian(0);
				int i2 = stream.currentOffset;
				stream.writeWordBigEndian((int)(Math.random() * 256D));
				stream.writeWordBigEndian(101);
				stream.writeWordBigEndian(233);
				stream.writeWord(45092);
				if((int)(Math.random() * 2D) == 0)
					stream.writeWord(35784);
				stream.writeWordBigEndian((int)(Math.random() * 256D));
				stream.writeWordBigEndian(64);
				stream.writeWordBigEndian(38);
				stream.writeWord((int)(Math.random() * 65536D));
				stream.writeWord((int)(Math.random() * 65536D));
				stream.writeBytes(stream.currentOffset - i2);
			}
			int j2 = k1 * 192;
			if(j2 > 0x17f00)
				j2 = 0x17f00;
			if(j2 < 32768)
				j2 = 32768;
			if(j2 > anInt984)
			{
				anInt984 += (j2 - anInt984) / 24;
				return;
			}
			if(j2 < anInt984)
			{
				anInt984 += (j2 - anInt984) / 80;
			}
		}
		catch(Exception _ex)
		{
			signlink.reporterror("glfc_ex " + myPlayer.x + "," + myPlayer.y + "," + anInt1014 + "," + anInt1015 + "," + anInt1069 + "," + anInt1070 + "," + baseX + "," + baseY);
			throw new RuntimeException("eek");
		}
	}

	public void processDrawing()
	{
		if(rsAlreadyLoaded || loadingError || genericLoadingError)
		{
			showErrorScreen();
			return;
		}
		anInt1061++;
		if(!loggedIn)
			drawLoginScreen(false);
		else
			drawGameScreen();
		anInt1213 = 0;
	}

	private boolean isFriendOrSelf(String s)
	{
		if(s == null)
			return false;
		for(int i = 0; i < friendsCount; i++)
			if(s.equalsIgnoreCase(friendsList[i]))
				return true;
		return s.equalsIgnoreCase(myPlayer.name);
	}

	private static String combatDiffColor(int i, int j)
	{
		int k = i - j;
		if(k < -9)
			return "@red@";
		if(k < -6)
			return "@or3@";
		if(k < -3)
			return "@or2@";
		if(k < 0)
			return "@or1@";
		if(k > 9)
			return "@gre@";
		if(k > 6)
			return "@gr3@";
		if(k > 3)
			return "@gr2@";
		if(k > 0)
			return "@gr1@";
		else
			return "@yel@";
	}

	private void setWaveVolume(int i)
	{
		signlink.wavevol = i;
	}

	private void draw3dScreen()
	{
		drawSplitPrivateChat();
		if(crossType == 1)
		{
			crosses[crossIndex / 100].drawSprite(crossX - 8 - 4, crossY - 8 - 4);
			anInt1142++;
			if(anInt1142 > 67)
			{
				anInt1142 = 0;
				stream.createFrame(78);
			}
		}
		if(crossType == 2)
			crosses[4 + crossIndex / 100].drawSprite(crossX - 8 - 4, crossY - 8 - 4);
		if(anInt1018 != -1)
		{
			method119(anInt945, anInt1018);
			drawInterface(0, 0, RSInterface.interfaceCache[anInt1018], 0);
		}
		if(openInterfaceID != -1)
		{
			method119(anInt945, openInterfaceID);
			drawInterface(0, 0, RSInterface.interfaceCache[openInterfaceID], 0);
		}
		locationCheck();
		if(!menuOpen)
		{
			processRightClick();
			drawTooltip();
		} else
		if(menuScreenArea == 0)
			drawMenu();
		if(anInt1055 == 1)
			headIcons[1].drawSprite(472, 296);
		if(fpsOn)
		{
			char c = '\u01FB';
			int k = 20;
			int i1 = 0xffff00;
			if(super.fps < 15)
				i1 = 0xff0000;
			aTextDrawingArea_1271.method380("Fps:" + super.fps, c, i1, k);
			k += 15;
			Runtime runtime = Runtime.getRuntime();
			int j1 = (int)((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
			i1 = 0xffff00;
			if(j1 > 0x2000000 && lowMem)
				i1 = 0xff0000;
			aTextDrawingArea_1271.method380("Mem:" + j1 + "k", c, 0xffff00, k);
			k += 15;
		}
		int i1 = 0xffff00;
		int x = baseX + (myPlayer.x - 6 >> 7);
		int y = baseY + (myPlayer.y - 6 >> 7);
		if (clientData)
		{
			char c = '\u01FB';
			int k = 20;
			if(super.fps < 15)
			i1 = 0xff0000;
			aTextDrawingArea_1271.method385(0xffff00, "Fps: " + super.fps, 285, 5);
			Runtime runtime = Runtime.getRuntime();
			int j1 = (int)((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
			i1 = 0xffff00;
			if(j1 > 0x2000000 && lowMem)
			i1 = 0xff0000;
			k += 15;
			aTextDrawingArea_1271.method385(0xffff00, "Mem: " + j1 + "k", 299, 5);
			aTextDrawingArea_1271.method385(0xffff00, "Mouse X: " + super.mouseX + " , Mouse Y: " + super.mouseY, 314, 5);
			aTextDrawingArea_1271.method385(0xffff00, "Coords: " + x + ", " + y, 329, 5);
		}
		if(anInt1104 != 0)
		{
			int j = anInt1104 / 50;
			int l = j / 60;
			j %= 60;
			if(j < 10)
				aTextDrawingArea_1271.method385(0xffff00, "The mods have smiled upon you - system update in: " + l + ":0" + j, 329, 4);
			else
				aTextDrawingArea_1271.method385(0xffff00, "The mods have smiled upon you - system update in: " + l + ":" + j, 329, 4);
			anInt849++;
			if(anInt849 > 75)
			{
				anInt849 = 0;
				stream.createFrame(148);
			}
		}
	}

	private void addIgnore(long l)
	{
		try
		{
			if(l == 0L)
				return;
			if(ignoreCount >= 100)
			{
				pushMessage("Your ignore list is full. Max of 100 hit", 0, "");
				return;
			}
			String s = TextClass.fixName(TextClass.nameForLong(l));
			for(int j = 0; j < ignoreCount; j++)
				if(ignoreListAsLongs[j] == l)
				{
					pushMessage(s + " is already on your ignore list", 0, "");
					return;
				}
			for(int k = 0; k < friendsCount; k++)
				if(friendsListAsLongs[k] == l)
				{
					pushMessage("Please remove " + s + " from your friend list first", 0, "");
					return;
				}

			ignoreListAsLongs[ignoreCount++] = l;
			needDrawTabArea = true;
			stream.createFrame(133);
			stream.writeQWord(l);
			return;
		}
		catch(RuntimeException runtimeexception)
		{
			signlink.reporterror("45688, " + l + ", " + 4 + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	private void method114()
	{
		for(int i = -1; i < playerCount; i++)
		{
			int j;
			if(i == -1)
				j = myPlayerIndex;
			else
				j = playerIndices[i];
			Player player = playerArray[j];
			if(player != null)
				method96(player);
		}

	}

	private void method115()
	{
		if(loadingStage == 2)
		{
			for(Class30_Sub1 class30_sub1 = (Class30_Sub1)aClass19_1179.reverseGetFirst(); class30_sub1 != null; class30_sub1 = (Class30_Sub1)aClass19_1179.reverseGetNext())
			{
				if(class30_sub1.anInt1294 > 0)
					class30_sub1.anInt1294--;
				if(class30_sub1.anInt1294 == 0)
				{
					if(class30_sub1.anInt1299 < 0 || ObjectManager.method178(class30_sub1.anInt1299, class30_sub1.anInt1301))
					{
						method142(class30_sub1.anInt1298, class30_sub1.anInt1295, class30_sub1.anInt1300, class30_sub1.anInt1301, class30_sub1.anInt1297, class30_sub1.anInt1296, class30_sub1.anInt1299);
						class30_sub1.unlink();
					}
				} else
				{
					if(class30_sub1.anInt1302 > 0)
						class30_sub1.anInt1302--;
					if(class30_sub1.anInt1302 == 0 && class30_sub1.anInt1297 >= 1 && class30_sub1.anInt1298 >= 1 && class30_sub1.anInt1297 <= 102 && class30_sub1.anInt1298 <= 102 && (class30_sub1.anInt1291 < 0 || ObjectManager.method178(class30_sub1.anInt1291, class30_sub1.anInt1293)))
					{
						method142(class30_sub1.anInt1298, class30_sub1.anInt1295, class30_sub1.anInt1292, class30_sub1.anInt1293, class30_sub1.anInt1297, class30_sub1.anInt1296, class30_sub1.anInt1291);
						class30_sub1.anInt1302 = -1;
						if(class30_sub1.anInt1291 == class30_sub1.anInt1299 && class30_sub1.anInt1299 == -1)
							class30_sub1.unlink();
						else
						if(class30_sub1.anInt1291 == class30_sub1.anInt1299 && class30_sub1.anInt1292 == class30_sub1.anInt1300 && class30_sub1.anInt1293 == class30_sub1.anInt1301)
							class30_sub1.unlink();
					}
				}
			}

		}
	}

	private void determineMenuSize()
	{
		int i = chatTextDrawingArea.getTextWidth("Choose Option");
		for(int j = 0; j < menuActionRow; j++)
		{
			int k = chatTextDrawingArea.getTextWidth(menuActionName[j]);
			if(k > i)
				i = k;
		}

		i += 8;
		int l = 15 * menuActionRow + 21;
		if(super.saveClickX > 4 && super.saveClickY > 4 && super.saveClickX < 516 && super.saveClickY < 338)
		{
			int i1 = super.saveClickX - 4 - i / 2;
			if(i1 + i > 512)
				i1 = 512 - i;
			if(i1 < 0)
				i1 = 0;
			int l1 = super.saveClickY - 4;
			if(l1 + l > 334)
				l1 = 334 - l;
			if(l1 < 0)
				l1 = 0;
			menuOpen = true;
			menuScreenArea = 0;
			menuOffsetX = i1;
			menuOffsetY = l1;
			menuWidth = i;
			menuHeight = 15 * menuActionRow + 22;
		}
		if(super.saveClickX > 519 && super.saveClickY > 168 && super.saveClickX < 765 && super.saveClickY < 503)
		{
			int j1 = super.saveClickX - 519 - i / 2;
			if(j1 < 0)
				j1 = 0;
			else
			if(j1 + i > 245)
				j1 = 245 - i;
			int i2 = super.saveClickY - 168;
			if(i2 < 0)
				i2 = 0;
			else
			if(i2 + l > 333)
				i2 = 333 - l;
			menuOpen = true;
			menuScreenArea = 1;
			menuOffsetX = j1;
			menuOffsetY = i2;
			menuWidth = i;
			menuHeight = 15 * menuActionRow + 22;
		}
		if(super.saveClickX > 0 && super.saveClickY > 338 && super.saveClickX < 516 && super.saveClickY < 503)
		{
			int k1 = super.saveClickX - 0 - i / 2;
			if(k1 < 0)
				k1 = 0;
			else
			if(k1 + i > 516)
				k1 = 516 - i;
			int j2 = super.saveClickY - 338;
			if(j2 < 0)
				j2 = 0;
			else
			if(j2 + l > 165)
				j2 = 165 - l;
			menuOpen = true;
			menuScreenArea = 2;
			menuOffsetX = k1;
			menuOffsetY = j2;
			menuWidth = i;
			menuHeight = 15 * menuActionRow + 22;
		}
	}

	private void method117(Stream stream)
	{
		stream.initBitAccess();
		int j = stream.readBits(1);
		if(j == 0)
			return;
		int k = stream.readBits(2);
		if(k == 0)
		{
			anIntArray894[anInt893++] = myPlayerIndex;
			return;
		}
		if(k == 1)
		{
			int l = stream.readBits(3);
			myPlayer.moveInDir(false, l);
			int k1 = stream.readBits(1);
			if(k1 == 1)
				anIntArray894[anInt893++] = myPlayerIndex;
			return;
		}
		if(k == 2)
		{
			int i1 = stream.readBits(3);
			myPlayer.moveInDir(true, i1);
			int l1 = stream.readBits(3);
			myPlayer.moveInDir(true, l1);
			int j2 = stream.readBits(1);
			if(j2 == 1)
				anIntArray894[anInt893++] = myPlayerIndex;
			return;
		}
		if(k == 3)
		{
			plane = stream.readBits(2);
			int j1 = stream.readBits(1);
			int i2 = stream.readBits(1);
			if(i2 == 1)
				anIntArray894[anInt893++] = myPlayerIndex;
			int k2 = stream.readBits(7);
			int l2 = stream.readBits(7);
			myPlayer.setPos(l2, k2, j1 == 1);
		}
	}

	private void nullLoader()
	{
		aBoolean831 = false;
		while(drawingFlames)
		{
			aBoolean831 = false;
			try
			{
				Thread.sleep(50L);
			}
			catch(Exception _ex) { }
		}
		aBackground_966 = null;
		aBackground_967 = null;
		aBackgroundArray1152s = null;
		anIntArray850 = null;
		anIntArray851 = null;
		anIntArray852 = null;
		anIntArray853 = null;
		anIntArray1190 = null;
		anIntArray1191 = null;
		anIntArray828 = null;
		anIntArray829 = null;
		aClass30_Sub2_Sub1_Sub1_1201 = null;
		aClass30_Sub2_Sub1_Sub1_1202 = null;
	}

	private boolean method119(int i, int j)
	{
		boolean flag1 = false;
		RSInterface class9 = RSInterface.interfaceCache[j];
		for(int k = 0; k < class9.children.length; k++)
		{
			if(class9.children[k] == -1)
				break;
			RSInterface class9_1 = RSInterface.interfaceCache[class9.children[k]];
			if(class9_1.type == 1)
				flag1 |= method119(i, class9_1.id);
			if(class9_1.type == 6 && (class9_1.anInt257 != -1 || class9_1.anInt258 != -1))
			{
				boolean flag2 = interfaceIsSelected(class9_1);
				int l;
				if(flag2)
					l = class9_1.anInt258;
				else
					l = class9_1.anInt257;
				if(l != -1)
				{
					Animation animation = Animation.anims[l];
					for(class9_1.anInt208 += i; class9_1.anInt208 > animation.method258(class9_1.anInt246);)
					{
						class9_1.anInt208 -= animation.method258(class9_1.anInt246) + 1;
						class9_1.anInt246++;
						if(class9_1.anInt246 >= animation.anInt352)
						{
							class9_1.anInt246 -= animation.anInt356;
							if(class9_1.anInt246 < 0 || class9_1.anInt246 >= animation.anInt352)
								class9_1.anInt246 = 0;
						}
						flag1 = true;
					}

				}
			}
		}

		return flag1;
	}

	private int method120()
	{
		int j = 3;
		if(yCameraCurve < 310)
		{
			int k = xCameraPos >> 7;
			int l = yCameraPos >> 7;
			int i1 = myPlayer.x >> 7;
			int j1 = myPlayer.y >> 7;
			if((byteGroundArray[plane][k][l] & 4) != 0)
				j = plane;
			int k1;
			if(i1 > k)
				k1 = i1 - k;
			else
				k1 = k - i1;
			int l1;
			if(j1 > l)
				l1 = j1 - l;
			else
				l1 = l - j1;
			if(k1 > l1)
			{
				int i2 = (l1 * 0x10000) / k1;
				int k2 = 32768;
				while(k != i1) 
				{
					if(k < i1)
						k++;
					else
					if(k > i1)
						k--;
					if((byteGroundArray[plane][k][l] & 4) != 0)
						j = plane;
					k2 += i2;
					if(k2 >= 0x10000)
					{
						k2 -= 0x10000;
						if(l < j1)
							l++;
						else
						if(l > j1)
							l--;
						if((byteGroundArray[plane][k][l] & 4) != 0)
							j = plane;
					}
				}
			} else
			{
				int j2 = (k1 * 0x10000) / l1;
				int l2 = 32768;
				while(l != j1) 
				{
					if(l < j1)
						l++;
					else
					if(l > j1)
						l--;
					if((byteGroundArray[plane][k][l] & 4) != 0)
						j = plane;
					l2 += j2;
					if(l2 >= 0x10000)
					{
						l2 -= 0x10000;
						if(k < i1)
							k++;
						else
						if(k > i1)
							k--;
						if((byteGroundArray[plane][k][l] & 4) != 0)
							j = plane;
					}
				}
			}
		}
		if((byteGroundArray[plane][myPlayer.x >> 7][myPlayer.y >> 7] & 4) != 0)
			j = plane;
		return j;
	}

	private int method121()
	{
		int j = method42(plane, yCameraPos, xCameraPos);
		if(j - zCameraPos < 800 && (byteGroundArray[plane][xCameraPos >> 7][yCameraPos >> 7] & 4) != 0)
			return plane;
		else
			return 3;
	}

	private void delIgnore(long l)
	{
		try
		{
			if(l == 0L)
				return;
			for(int j = 0; j < ignoreCount; j++)
				if(ignoreListAsLongs[j] == l)
				{
					ignoreCount--;
					needDrawTabArea = true;
					System.arraycopy(ignoreListAsLongs, j + 1, ignoreListAsLongs, j, ignoreCount - j);

					stream.createFrame(74);
					stream.writeQWord(l);
					return;
				}

			return;
		}
		catch(RuntimeException runtimeexception)
		{
			signlink.reporterror("47229, " + 3 + ", " + l + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	public String getParameter(String s)
	{
		if(signlink.mainapp != null)
			return signlink.mainapp.getParameter(s);
		else
			return super.getParameter(s);
	}

	private void adjustVolume(boolean flag, int i)
	{
	    if(MusicEnabled) {
		MidiPlayer.getSingleton().setVolume(0, i);
		signlink.midivol = i;
		if(flag)
			signlink.midi = "voladjust";
	    }
	}

	private int extractInterfaceValues(RSInterface class9, int j)
	{
		if(class9.valueIndexArray == null || j >= class9.valueIndexArray.length)
			return -2;
		try
		{
			int ai[] = class9.valueIndexArray[j];
			int k = 0;
			int l = 0;
			int i1 = 0;
			do
			{
				int j1 = ai[l++];
				int k1 = 0;
				byte byte0 = 0;
				if(j1 == 0)
					return k;
				if(j1 == 1)
					k1 = currentStats[ai[l++]];
				if(j1 == 2)
					k1 = maxStats[ai[l++]];
				if(j1 == 3)
					k1 = currentExp[ai[l++]];
				if(j1 == 4)
				{
					RSInterface class9_1 = RSInterface.interfaceCache[ai[l++]];
					int k2 = ai[l++];
					if(k2 >= 0 && k2 < ItemDef.totalItems && (!ItemDef.forID(k2).membersObject || isMembers))
					{
						for(int j3 = 0; j3 < class9_1.inv.length; j3++)
							if(class9_1.inv[j3] == k2 + 1)
								k1 += class9_1.invStackSizes[j3];

					}
				}
				if(j1 == 5)
					k1 = variousSettings[ai[l++]];
				if(j1 == 6)
					k1 = anIntArray1019[maxStats[ai[l++]] - 1];
				if(j1 == 7)
					k1 = (variousSettings[ai[l++]] * 100) / 46875;
				if(j1 == 8)
					k1 = myPlayer.combatLevel;
				if(j1 == 9)
				{
					for(int l1 = 0; l1 < Skills.skillsCount; l1++)
						if(Skills.skillEnabled[l1])
							k1 += maxStats[l1];

				}
				if(j1 == 10)
				{
					RSInterface class9_2 = RSInterface.interfaceCache[ai[l++]];
					int l2 = ai[l++] + 1;
					if(l2 >= 0 && l2 < ItemDef.totalItems && (!ItemDef.forID(l2).membersObject || isMembers))
					{
						for(int k3 = 0; k3 < class9_2.inv.length; k3++)
						{
							if(class9_2.inv[k3] != l2)
								continue;
							k1 = 0x3b9ac9ff;
							break;
						}

					}
				}
				if(j1 == 11)
					k1 = energy;
				if(j1 == 12)
					k1 = weight;
				if(j1 == 13)
				{
					int i2 = variousSettings[ai[l++]];
					int i3 = ai[l++];
					k1 = (i2 & 1 << i3) == 0 ? 0 : 1;
				}
				if(j1 == 14)
				{
					int j2 = ai[l++];
					VarBit varBit = VarBit.cache[j2];
					int l3 = varBit.anInt648;
					int i4 = varBit.anInt649;
					int j4 = varBit.anInt650;
					int k4 = anIntArray1232[j4 - i4];
					k1 = variousSettings[l3] >> i4 & k4;
				}
				if(j1 == 15)
					byte0 = 1;
				if(j1 == 16)
					byte0 = 2;
				if(j1 == 17)
					byte0 = 3;
				if(j1 == 18)
					k1 = (myPlayer.x >> 7) + baseX;
				if(j1 == 19)
					k1 = (myPlayer.y >> 7) + baseY;
				if(j1 == 20)
					k1 = ai[l++];
				if(byte0 == 0)
				{
					if(i1 == 0)
						k += k1;
					if(i1 == 1)
						k -= k1;
					if(i1 == 2 && k1 != 0)
						k /= k1;
					if(i1 == 3)
						k *= k1;
					i1 = 0;
				} else
				{
					i1 = byte0;
				}
			} while(true);
		}
		catch(Exception _ex)
		{
			return -1;
		}
	}

	public void drawTooltip() {
		if (menuActionRow < 2 && itemSelected == 0 && spellSelected == 0)
			return;
		String s;
		if (itemSelected == 1 && menuActionRow < 2)
			s = "Use " + selectedItemName + " with...";
		else if (spellSelected == 1 && menuActionRow < 2)
			s = spellTooltip + "...";
		else
			s = menuActionName[menuActionRow - 1];
		if (menuActionRow > 2)
			s = s + "@whi@ / " + (menuActionRow - 2) + " more options";
		chatTextDrawingArea.method390(4, 0xffffff, s, loopCycle / 1000, 15);
	}
		
	private void drawMinimap() {
		aRSImageProducer_1164.initDrawingArea();
		if(anInt1021 == 2) {
			byte abyte0[] = mapBack.aByteArray1450;
			int ai[] = DrawingArea.pixels;
			int k2 = abyte0.length;
			for(int i5 = 0; i5 < k2; i5++)
			if(abyte0[i5] == 0)
				ai[i5] = 0;
			compass.method352(33, minimapInt1, anIntArray1057, 256, anIntArray968, 25, 4, 31, 33, 25);
			aRSImageProducer_1165.initDrawingArea();
			return;
		}
		int i = minimapInt1 + minimapInt2 & 0x7ff;
		int j = 48 + myPlayer.x / 32;
		int l2 = 464 - myPlayer.y / 32;
		for (int x = 0; x < anIntArray1229.length; x++){
			anIntArray1229[x] = 170;
			anIntArray1052[x] = -23;
		}
		aClass30_Sub2_Sub1_Sub1_1263.method352(151, i, anIntArray1229, 256 + minimapInt3, anIntArray1052, l2, 9, 54, 146, j);
		compass.method352(33, minimapInt1, anIntArray1057, 256, anIntArray968, 25, 4, 31, 33, 25);
		for(int j5 = 0; j5 < anInt1071; j5++) {
			int k = (anIntArray1072[j5] * 4 + 2) - myPlayer.x / 32;
			int i3 = (anIntArray1073[j5] * 4 + 2) - myPlayer.y / 32;
			markMinimap(aClass30_Sub2_Sub1_Sub1Array1140[j5], k, i3);
		}

		for(int k5 = 0; k5 < 104; k5++) {
			for(int l5 = 0; l5 < 104; l5++) {
				NodeList class19 = groundArray[plane][k5][l5];
				if(class19 != null) {
					int l = (k5 * 4 + 2) - myPlayer.x / 32;
					int j3 = (l5 * 4 + 2) - myPlayer.y / 32;
					markMinimap(mapDotItem, l, j3);
				}
			}
		}

		for(int i6 = 0; i6 < npcCount; i6++) {
			NPC npc = npcArray[npcIndices[i6]];
			if(npc != null && npc.isVisible()) {
				EntityDef entityDef = npc.desc;
				if(entityDef.childrenIDs != null)
					entityDef = entityDef.method161();
				if(entityDef != null && entityDef.aBoolean87 && entityDef.aBoolean84) {
					int i1 = npc.x / 32 - myPlayer.x / 32;
					int k3 = npc.y / 32 - myPlayer.y / 32;
					markMinimap(mapDotNPC, i1, k3);
				}
			}
		}

		for(int j6 = 0; j6 < playerCount; j6++) {
			Player player = playerArray[playerIndices[j6]];
			if(player != null && player.isVisible()) {
				int j1 = player.x / 32 - myPlayer.x / 32;
				int l3 = player.y / 32 - myPlayer.y / 32;
				boolean flag1 = false;
				long l6 = TextClass.longForName(player.name);
				for(int k6 = 0; k6 < friendsCount; k6++) {
					if(l6 != friendsListAsLongs[k6] || friendsNodeIDs[k6] == 0)
					continue;
					flag1 = true;
					break;
				}
				boolean flag2 = false;
				if(myPlayer.team != 0 && player.team != 0 && myPlayer.team == player.team)
					flag2 = true;
				if(flag1)
					markMinimap(mapDotFriend, j1, l3);
				else
					markMinimap(mapDotPlayer, j1, l3);
			}
		}

		if(anInt855 != 0 && loopCycle % 20 < 10) {
			if(anInt855 == 1 && anInt1222 >= 0 && anInt1222 < npcArray.length) {
					NPC class30_sub2_sub4_sub1_sub1_1 = npcArray[anInt1222];
					if(class30_sub2_sub4_sub1_sub1_1 != null)
					{
						int k1 = class30_sub2_sub4_sub1_sub1_1.x / 32 - myPlayer.x / 32;
						int i4 = class30_sub2_sub4_sub1_sub1_1.y / 32 - myPlayer.y / 32;
						method81(mapMarker, i4, k1);
					}
			}
			if(anInt855 == 2) {
				int l1 = ((anInt934 - baseX) * 4 + 2) - myPlayer.x / 32;
				int j4 = ((anInt935 - baseY) * 4 + 2) - myPlayer.y / 32;
				method81(mapMarker, j4, l1);
			}
			if(anInt855 == 10 && anInt933 >= 0 && anInt933 < playerArray.length)
			{
				Player class30_sub2_sub4_sub1_sub2_1 = playerArray[anInt933];
				if(class30_sub2_sub4_sub1_sub2_1 != null) {
					int i2 = class30_sub2_sub4_sub1_sub2_1.x / 32 - myPlayer.x / 32;
					int k4 = class30_sub2_sub4_sub1_sub2_1.y / 32 - myPlayer.y / 32;
					method81(mapMarker, k4, i2);
				}
			}
		}
		if(destX != 0) {
			int j2 = (destX * 4 + 2) - myPlayer.x / 32;
			int l4 = (destY * 4 + 2) - myPlayer.y / 32;
			markMinimap(mapFlag, j2, l4);
		}
		DrawingArea.drawPixels(3, 78+5, 97+30, 0xffffff, 3);
		mapArea.drawSprite(0, 0);
		aRSImageProducer_1165.initDrawingArea();
	}

	private void npcScreenPos(Entity entity, int i) {
		calcEntityScreenPos(entity.x, i, entity.y);
	}

	private void calcEntityScreenPos(int i, int j, int l) {
		if(i < 128 || l < 128 || i > 13056 || l > 13056) {
			spriteDrawX = -1;
			spriteDrawY = -1;
			return;
		}
		int i1 = method42(plane, l, i) - j;
		i -= xCameraPos;
		i1 -= zCameraPos;
		l -= yCameraPos;
		int j1 = Model.modelIntArray1[yCameraCurve];
		int k1 = Model.modelIntArray2[yCameraCurve];
		int l1 = Model.modelIntArray1[xCameraCurve];
		int i2 = Model.modelIntArray2[xCameraCurve];
		int j2 = l * l1 + i * i2 >> 16;
		l = l * i2 - i * l1 >> 16;
		i = j2;
		j2 = i1 * k1 - l * j1 >> 16;
		l = i1 * j1 + l * k1 >> 16;
		i1 = j2;
		if(l >= 50) {
			spriteDrawX = Texture.textureInt1 + (i << 9) / l;
			spriteDrawY = Texture.textureInt2 + (i1 << 9) / l;
		} else {
			spriteDrawX = -1;
			spriteDrawY = -1;
		}
	}

	private void buildSplitPrivateChatMenu()
	{
		if(splitPrivateChat == 0)
			return;
		int i = 0;
		if(anInt1104 != 0)
			i = 1;
		for(int j = 0; j < 100; j++)
			if(chatMessages[j] != null)
			{
				int k = chatTypes[j];
				String s = chatNames[j];
				boolean flag1 = false;
				if(s != null && s.startsWith("@cr1@"))
				{
					s = s.substring(5);
					boolean flag2 = true;
				}
				if(s != null && s.startsWith("@cr2@"))
				{
					s = s.substring(5);
					byte byte0 = 2;
				}
				if((k == 3 || k == 7) && (k == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(s)))
				{
					int l = 329 - i * 13;
					if(super.mouseX > 4 && super.mouseY - 4 > l - 10 && super.mouseY - 4 <= l + 3)
					{
						int i1 = aTextDrawingArea_1271.getTextWidth("From:  " + s + chatMessages[j]) + 25;
						if(i1 > 450)
							i1 = 450;
						if(super.mouseX < 4 + i1)
						{
							if(myPrivilege >= 1)
							{
								menuActionName[menuActionRow] = "Report abuse @whi@" + s;
								menuActionID[menuActionRow] = 2606;
								menuActionRow++;
							}
							menuActionName[menuActionRow] = "Add ignore @whi@" + s;
							menuActionID[menuActionRow] = 2042;
							menuActionRow++;
							menuActionName[menuActionRow] = "Add friend @whi@" + s;
							menuActionID[menuActionRow] = 2337;
							menuActionRow++;
						}
					}
					if(++i >= 5)
						return;
				}
				if((k == 5 || k == 6) && privateChatMode < 2 && ++i >= 5)
					return;
			}

	}

	private void method130(int j, int k, int l, int i1, int j1, int k1,
						   int l1, int i2, int j2)
	{
		Class30_Sub1 class30_sub1 = null;
		for(Class30_Sub1 class30_sub1_1 = (Class30_Sub1)aClass19_1179.reverseGetFirst(); class30_sub1_1 != null; class30_sub1_1 = (Class30_Sub1)aClass19_1179.reverseGetNext())
		{
			if(class30_sub1_1.anInt1295 != l1 || class30_sub1_1.anInt1297 != i2 || class30_sub1_1.anInt1298 != j1 || class30_sub1_1.anInt1296 != i1)
				continue;
			class30_sub1 = class30_sub1_1;
			break;
		}

		if(class30_sub1 == null)
		{
			class30_sub1 = new Class30_Sub1();
			class30_sub1.anInt1295 = l1;
			class30_sub1.anInt1296 = i1;
			class30_sub1.anInt1297 = i2;
			class30_sub1.anInt1298 = j1;
			method89(class30_sub1);
			aClass19_1179.insertHead(class30_sub1);
		}
		class30_sub1.anInt1291 = k;
		class30_sub1.anInt1293 = k1;
		class30_sub1.anInt1292 = l;
		class30_sub1.anInt1302 = j2;
		class30_sub1.anInt1294 = j;
	}

	private boolean interfaceIsSelected(RSInterface class9)
	{
		if(class9.anIntArray245 == null)
			return false;
		for(int i = 0; i < class9.anIntArray245.length; i++)
		{
			int j = extractInterfaceValues(class9, i);
			int k = class9.anIntArray212[i];
			if(class9.anIntArray245[i] == 2)
			{
				if(j >= k)
					return false;
			} else
			if(class9.anIntArray245[i] == 3)
			{
				if(j <= k)
					return false;
			} else
			if(class9.anIntArray245[i] == 4)
			{
				if(j == k)
					return false;
			} else
			if(j != k)
				return false;
		}

		return true;
	}

	private DataInputStream openJagGrabInputStream(String s)
		throws IOException
	{
 //	   if(!aBoolean872)
 //		   if(signlink.mainapp != null)
 //			   return signlink.openurl(s);
 //		   else
 //			   return new DataInputStream((new URL(getCodeBase(), s)).openStream());
		if(aSocket832 != null)
		{
			try
			{
				aSocket832.close();
			}
			catch(Exception _ex) { }
			aSocket832 = null;
		}
		aSocket832 = openSocket(43594);
		aSocket832.setSoTimeout(10000);
		java.io.InputStream inputstream = aSocket832.getInputStream();
		OutputStream outputstream = aSocket832.getOutputStream();
		outputstream.write(("JAGGRAB /" + s + "\n\n").getBytes());
		return new DataInputStream(inputstream);
	}

	private void doFlamesDrawing()
	{
		char c = '\u0100';
		if(anInt1040 > 0)
		{
			for(int i = 0; i < 256; i++)
				if(anInt1040 > 768)
					anIntArray850[i] = method83(anIntArray851[i], anIntArray852[i], 1024 - anInt1040);
				else
				if(anInt1040 > 256)
					anIntArray850[i] = anIntArray852[i];
				else
					anIntArray850[i] = method83(anIntArray852[i], anIntArray851[i], 256 - anInt1040);

		} else
		if(anInt1041 > 0)
		{
			for(int j = 0; j < 256; j++)
				if(anInt1041 > 768)
					anIntArray850[j] = method83(anIntArray851[j], anIntArray853[j], 1024 - anInt1041);
				else
				if(anInt1041 > 256)
					anIntArray850[j] = anIntArray853[j];
				else
					anIntArray850[j] = method83(anIntArray853[j], anIntArray851[j], 256 - anInt1041);

		} else
		{
			System.arraycopy(anIntArray851, 0, anIntArray850, 0, 256);

		}
		System.arraycopy(aClass30_Sub2_Sub1_Sub1_1201.myPixels, 0, aRSImageProducer_1110.anIntArray315, 0, 33920);

		int i1 = 0;
		int j1 = 1152;
		for(int k1 = 1; k1 < c - 1; k1++)
		{
			int l1 = (anIntArray969[k1] * (c - k1)) / c;
			int j2 = 22 + l1;
			if(j2 < 0)
				j2 = 0;
			i1 += j2;
			for(int l2 = j2; l2 < 128; l2++)
			{
				int j3 = anIntArray828[i1++];
				if(j3 != 0)
				{
					int l3 = j3;
					int j4 = 256 - j3;
					j3 = anIntArray850[j3];
					int l4 = aRSImageProducer_1110.anIntArray315[j1];
					aRSImageProducer_1110.anIntArray315[j1++] = ((j3 & 0xff00ff) * l3 + (l4 & 0xff00ff) * j4 & 0xff00ff00) + ((j3 & 0xff00) * l3 + (l4 & 0xff00) * j4 & 0xff0000) >> 8;
				} else
				{
					j1++;
				}
			}

			j1 += j2;
		}

		aRSImageProducer_1110.drawGraphics(0, super.graphics, 0);
		System.arraycopy(aClass30_Sub2_Sub1_Sub1_1202.myPixels, 0, aRSImageProducer_1111.anIntArray315, 0, 33920);

		i1 = 0;
		j1 = 1176;
		for(int k2 = 1; k2 < c - 1; k2++)
		{
			int i3 = (anIntArray969[k2] * (c - k2)) / c;
			int k3 = 103 - i3;
			j1 += i3;
			for(int i4 = 0; i4 < k3; i4++)
			{
				int k4 = anIntArray828[i1++];
				if(k4 != 0)
				{
					int i5 = k4;
					int j5 = 256 - k4;
					k4 = anIntArray850[k4];
					int k5 = aRSImageProducer_1111.anIntArray315[j1];
					aRSImageProducer_1111.anIntArray315[j1++] = ((k4 & 0xff00ff) * i5 + (k5 & 0xff00ff) * j5 & 0xff00ff00) + ((k4 & 0xff00) * i5 + (k5 & 0xff00) * j5 & 0xff0000) >> 8;
				} else
				{
					j1++;
				}
			}

			i1 += 128 - k3;
			j1 += 128 - k3 - i3;
		}

		aRSImageProducer_1111.drawGraphics(0, super.graphics, 637);
	}

	private void method134(Stream stream)
	{
		int j = stream.readBits(8);
		if(j < playerCount)
		{
			for(int k = j; k < playerCount; k++)
				anIntArray840[anInt839++] = playerIndices[k];

		}
		if(j > playerCount)
		{
			signlink.reporterror(myUsername + " Too many players");
			throw new RuntimeException("eek");
		}
		playerCount = 0;
		for(int l = 0; l < j; l++)
		{
			int i1 = playerIndices[l];
			Player player = playerArray[i1];
			int j1 = stream.readBits(1);
			if(j1 == 0)
			{
				playerIndices[playerCount++] = i1;
				player.anInt1537 = loopCycle;
			} else
			{
				int k1 = stream.readBits(2);
				if(k1 == 0)
				{
					playerIndices[playerCount++] = i1;
					player.anInt1537 = loopCycle;
					anIntArray894[anInt893++] = i1;
				} else
				if(k1 == 1)
				{
					playerIndices[playerCount++] = i1;
					player.anInt1537 = loopCycle;
					int l1 = stream.readBits(3);
					player.moveInDir(false, l1);
					int j2 = stream.readBits(1);
					if(j2 == 1)
						anIntArray894[anInt893++] = i1;
				} else
				if(k1 == 2)
				{
					playerIndices[playerCount++] = i1;
					player.anInt1537 = loopCycle;
					int i2 = stream.readBits(3);
					player.moveInDir(true, i2);
					int k2 = stream.readBits(3);
					player.moveInDir(true, k2);
					int l2 = stream.readBits(1);
					if(l2 == 1)
						anIntArray894[anInt893++] = i1;
				} else
				if(k1 == 3)
					anIntArray840[anInt839++] = i1;
			}
		}
	}

	private void drawLoginScreen(boolean flag)
	{
		resetImageProducers();
		aRSImageProducer_1109.initDrawingArea();
		aBackground_966.method361(0, 0);
		char c = '\u0168';
		char c1 = '\310';
		if(loginScreenState == 0)
		{
			int i = c1 / 2 + 80;
			smallText.method382(0x75a9a9, c / 2, onDemandFetcher.statusString, i, true);
			i = c1 / 2 - 20;
			chatTextDrawingArea.method382(0xffff00, c / 2, "Welcome to Vscape", i, true);
			i += 30;
			int l = c / 2 - 80;
			int k1 = c1 / 2 + 20;
			aBackground_967.method361(l - 73, k1 - 20);
			chatTextDrawingArea.method382(0xffffff, l, "New User", k1 + 5, true);
			l = c / 2 + 80;
			aBackground_967.method361(l - 73, k1 - 20);
			chatTextDrawingArea.method382(0xffffff, l, "Existing User", k1 + 5, true);
		}
		if(loginScreenState == 2)
		{
			int j = c1 / 2 - 40;
			if(loginMessage1.length() > 0)
			{
				chatTextDrawingArea.method382(0xffff00, c / 2, loginMessage1, j - 15, true);
				chatTextDrawingArea.method382(0xffff00, c / 2, loginMessage2, j, true);
				j += 30;
			} else
			{
				chatTextDrawingArea.method382(0xffff00, c / 2, loginMessage2, j - 7, true);
				j += 30;
			}
			chatTextDrawingArea.method389(true, c / 2 - 90, 0xffffff, "Username: " + myUsername + ((loginScreenCursorPos == 0) & (loopCycle % 40 < 20) ? "@yel@|" : ""), j);
			j += 15;
			chatTextDrawingArea.method389(true, c / 2 - 88, 0xffffff, "Password: " + TextClass.passwordAsterisks(myPassword) + ((loginScreenCursorPos == 1) & (loopCycle % 40 < 20) ? "@yel@|" : ""), j);
			j += 15;
			if(!flag)
			{
				int i1 = c / 2 - 80;
				int l1 = c1 / 2 + 50;
				aBackground_967.method361(i1 - 73, l1 - 20);
				chatTextDrawingArea.method382(0xffffff, i1, "Login", l1 + 5, true);
				i1 = c / 2 + 80;
				aBackground_967.method361(i1 - 73, l1 - 20);
				chatTextDrawingArea.method382(0xffffff, i1, "Cancel", l1 + 5, true);
			}
		}
		if(loginScreenState == 3)
		{
						chatTextDrawingArea.method382(0xffff00, c / 2, "Create a free account", c1 / 2 - 60, true);
			int k = c1 / 2 - 35;
			chatTextDrawingArea.method382(0xffffff, c / 2, "To create a new account you need to", k, true);
			k += 15;
			chatTextDrawingArea.method382(0xffffff, c / 2, "click on cancel, and press the existing", k, true);
			k += 15;
			chatTextDrawingArea.method382(0xffffff, c / 2, "user button, and just log in with any username", k, true);
			k += 15;
			chatTextDrawingArea.method382(0xffffff, c / 2, "and password that is not already being used!", k, true);
			k += 15;
			int j1 = c / 2;
			int i2 = c1 / 2 + 50;
			aBackground_967.method361(j1 - 73, i2 - 20);
			chatTextDrawingArea.method382(0xffffff, j1, "Cancel", i2 + 5, true);
		}
		aRSImageProducer_1109.drawGraphics(171, super.graphics, 202);
		if(welcomeScreenRaised)
		{
			welcomeScreenRaised = false;
			aRSImageProducer_1107.drawGraphics(0, super.graphics, 128);
			aRSImageProducer_1108.drawGraphics(371, super.graphics, 202);
			aRSImageProducer_1112.drawGraphics(265, super.graphics, 0);
			aRSImageProducer_1113.drawGraphics(265, super.graphics, 562);
			aRSImageProducer_1114.drawGraphics(171, super.graphics, 128);
			aRSImageProducer_1115.drawGraphics(171, super.graphics, 562);
		}
	}

	private void drawFlames()
	{
		drawingFlames = true;
		try
		{
			long l = System.currentTimeMillis();
			int i = 0;
			int j = 20;
			while(aBoolean831) 
			{
				anInt1208++;
				calcFlamesPosition();
				calcFlamesPosition();
				doFlamesDrawing();
				if(++i > 10)
				{
					long l1 = System.currentTimeMillis();
					int k = (int)(l1 - l) / 10 - j;
					j = 40 - k;
					if(j < 5)
						j = 5;
					i = 0;
					l = l1;
				}
				try
				{
					Thread.sleep(j);
				}
				catch(Exception _ex) { }
			}
		}
		catch(Exception _ex) { }
		drawingFlames = false;
	}

	public void raiseWelcomeScreen()
	{
		welcomeScreenRaised = true;
	}

	private void method137(Stream stream1, int i)
    {
        if(i == 84)
        {
            int j = stream1.readUnsignedByte();
            int i3 = anInt1268 + (j >> 4 & 7);
            int l5 = anInt1269 + (j & 7);
            int k8 = stream1.readUnsignedWord();
            int j11 = stream1.readUnsignedWord();
            int k13 = stream1.readUnsignedWord();
            if(i3 >= 0 && l5 >= 0 && i3 < 104 && l5 < 104)
            {
                NodeList nodelist1 = groundArray[plane][i3][l5];
                if(nodelist1 != null)
                {
                    Item item3 = (Item)nodelist1.reverseGetFirst();
                    do
                    {
                        if(item3 == null)
                            break;
                        if(item3.ID == (k8 & 0x7fff) && item3.anInt1559 == j11)
                        {
                            item3.anInt1559 = k13;
                            break;
                        }
                        item3 = (Item)nodelist1.reverseGetNext();
                    } while(true);
                    spawnGroundItem(i3, l5);
                }
            }
            return;
        }
        if(i == 105)
        {
            int k = stream1.readUnsignedByte();
            int j3 = anInt1268 + (k >> 4 & 7);
            int i6 = anInt1269 + (k & 7);
            int l8 = stream1.readUnsignedWord();
            int k11 = stream1.readUnsignedByte();
            int l13 = k11 >> 4 & 0xf;
            int l15 = k11 & 7;
            if(myPlayer.smallX[0] >= j3 - l13 && myPlayer.smallX[0] <= j3 + l13 && myPlayer.smallY[0] >= i6 - l13 && myPlayer.smallY[0] <= i6 + l13 && aBoolean848 && !lowMem && anInt1062 < 50)
            {
                anIntArray1207[anInt1062] = l8;
                anIntArray1241[anInt1062] = l15;
                anIntArray1250[anInt1062] = Sounds.anIntArray326[l8];
                anInt1062++;
            }
        }
        if(i == 215)
        {
            int l = stream1.method435();
            int k3 = stream1.method428();
            int j6 = anInt1268 + (k3 >> 4 & 7);
            int i9 = anInt1269 + (k3 & 7);
            int l11 = stream1.method435();
            int i14 = stream1.readUnsignedWord();
            if(j6 >= 0 && i9 >= 0 && j6 < 104 && i9 < 104 && l11 != unknownInt10)
            {
                Item item2 = new Item();
                item2.ID = l;
                item2.anInt1559 = i14;
                if(groundArray[plane][j6][i9] == null)
                    groundArray[plane][j6][i9] = new NodeList();
                groundArray[plane][j6][i9].insertHead(item2);
                spawnGroundItem(j6, i9);
            }
            return;
        }
        if(i == 156)
        {
            int i1 = stream1.method426();
            int l3 = anInt1268 + (i1 >> 4 & 7);
            int k6 = anInt1269 + (i1 & 7);
            int j9 = stream1.readUnsignedWord();
            if(l3 >= 0 && k6 >= 0 && l3 < 104 && k6 < 104)
            {
                NodeList nodelist = groundArray[plane][l3][k6];
                if(nodelist != null)
                {
                    Item item = (Item)nodelist.reverseGetFirst();
                    do
                    {
                        if(item == null)
                            break;
                        if(item.ID == (j9 & 0x7fff))
                        {
                            item.unlink();
                            break;
                        }
                        item = (Item)nodelist.reverseGetNext();
                    } while(true);
                    if(nodelist.reverseGetFirst() == null)
                        groundArray[plane][l3][k6] = null;
                    spawnGroundItem(l3, k6);
                }
            }
            return;
        }
        if(i == 160)
        {
            int j1 = stream1.method428();
            int i4 = anInt1268 + (j1 >> 4 & 7);
            int l6 = anInt1269 + (j1 & 7);
            int k9 = stream1.method428();
            int i12 = k9 >> 2;
            int j14 = k9 & 3;
            int i16 = anIntArray1177[i12];
            int i17 = stream1.method435();
            if(i4 >= 0 && l6 >= 0 && i4 < 103 && l6 < 103)
            {
                int i18 = intGroundArray[plane][i4][l6];
                int l18 = intGroundArray[plane][i4 + 1][l6];
                int k19 = intGroundArray[plane][i4 + 1][l6 + 1];
                int j20 = intGroundArray[plane][i4][l6 + 1];
                if(i16 == 0)
                {
                    Object1 object1 = worldController.method296(plane, i4, l6);
                    if(object1 != null)
                    {
                        int j21 = object1.uid >> 14 & 0x7fff;
                        if(i12 == 2)
                        {
                            object1.aClass30_Sub2_Sub4_278 = new Animable_Sub5(j21, 4 + j14, 2, l18, k19, i18, j20, i17, false);
                            object1.aClass30_Sub2_Sub4_279 = new Animable_Sub5(j21, j14 + 1 & 3, 2, l18, k19, i18, j20, i17, false);
                        } else
                        {
                            object1.aClass30_Sub2_Sub4_278 = new Animable_Sub5(j21, j14, i12, l18, k19, i18, j20, i17, false);
                        }
                    }
                }
                if(i16 == 1)
                {
                    Object2 object2 = worldController.method297(i4, l6, plane);
                    if(object2 != null)
                        object2.aClass30_Sub2_Sub4_504 = new Animable_Sub5(object2.uid >> 14 & 0x7fff, 0, 4, l18, k19, i18, j20, i17, false);
                }
                if(i16 == 2)
                {
                    Object5 object5 = worldController.method298(i4, l6, plane);
                    if(i12 == 11)
                        i12 = 10;
                    if(object5 != null)
                        object5.aClass30_Sub2_Sub4_521 = new Animable_Sub5(object5.uid >> 14 & 0x7fff, j14, i12, l18, k19, i18, j20, i17, false);
                }
                if(i16 == 3)
                {
                    Object3 object3 = worldController.method299(l6, i4, plane);
                    if(object3 != null)
                        object3.aClass30_Sub2_Sub4_814 = new Animable_Sub5(object3.uid >> 14 & 0x7fff, j14, 22, l18, k19, i18, j20, i17, false);
                }
            }
            return;
        }
        if(i == 147)
        {
            int k1 = stream1.method428();
            int j4 = anInt1268 + (k1 >> 4 & 7);
            int i7 = anInt1269 + (k1 & 7);
            int l9 = stream1.readUnsignedWord();
            byte byte0 = stream1.method430();
            int k14 = stream1.method434();
            byte byte1 = stream1.method429();
            int j17 = stream1.readUnsignedWord();
            int j18 = stream1.method428();
            int i19 = j18 >> 2;
            int l19 = j18 & 3;
            int k20 = anIntArray1177[i19];
            byte byte2 = stream1.readSignedByte();
            int k21 = stream1.readUnsignedWord();
            byte byte3 = stream1.method429();
            Player player;
            if(l9 == unknownInt10)
                player = myPlayer;
            else
                player = playerArray[l9];
            if(player != null)
            {
                ObjectDef objectdef = ObjectDef.forID(k21);
                int l21 = intGroundArray[plane][j4][i7];
                int i22 = intGroundArray[plane][j4 + 1][i7];
                int j22 = intGroundArray[plane][j4 + 1][i7 + 1];
                int k22 = intGroundArray[plane][j4][i7 + 1];
                Model model = objectdef.method578(i19, l19, l21, i22, j22, k22, -1);
                if(model != null)
                {
                    method130(j17 + 1, -1, 0, k20, i7, 0, plane, j4, k14 + 1);
                    player.anInt1707 = k14 + loopCycle;
                    player.anInt1708 = j17 + loopCycle;
                    player.aModel_1714 = model;
                    int l22 = objectdef.anInt744;
                    int i23 = objectdef.anInt761;
                    if(l19 == 1 || l19 == 3)
                    {
                        l22 = objectdef.anInt761;
                        i23 = objectdef.anInt744;
                    }
                    player.anInt1711 = j4 * 128 + l22 * 64;
                    player.anInt1713 = i7 * 128 + i23 * 64;
                    player.anInt1712 = method42(plane, player.anInt1713, player.anInt1711);
                    if(byte2 > byte0)
                    {
                        byte byte4 = byte2;
                        byte2 = byte0;
                        byte0 = byte4;
                    }
                    if(byte3 > byte1)
                    {
                        byte byte5 = byte3;
                        byte3 = byte1;
                        byte1 = byte5;
                    }
                    player.anInt1719 = j4 + byte2;
                    player.anInt1721 = j4 + byte0;
                    player.anInt1720 = i7 + byte3;
                    player.anInt1722 = i7 + byte1;
                }
            }
        }
        if(i == 151)
        {
            int l1 = stream1.method426();
            int k4 = anInt1268 + (l1 >> 4 & 7);
            int j7 = anInt1269 + (l1 & 7);
            int i10 = stream1.method434();
            int j12 = stream1.method428();
            int l14 = j12 >> 2;
            int j16 = j12 & 3;
            int k17 = anIntArray1177[l14];
            if(k4 >= 0 && j7 >= 0 && k4 < 104 && j7 < 104)
                method130(-1, i10, j16, k17, j7, l14, plane, k4, 0);
            return;
        }
        if(i == 4)
        {
            int i2 = stream1.readUnsignedByte();
            int l4 = anInt1268 + (i2 >> 4 & 7);
            int k7 = anInt1269 + (i2 & 7);
            int j10 = stream1.readUnsignedWord();
            int k12 = stream1.readUnsignedByte();
            int i15 = stream1.readUnsignedWord();
            if(l4 >= 0 && k7 >= 0 && l4 < 104 && k7 < 104)
            {
                l4 = l4 * 128 + 64;
                k7 = k7 * 128 + 64;
                Animable_Sub3 animable_sub3 = new Animable_Sub3(plane, loopCycle, i15, j10, method42(plane, k7, l4) - k12, k7, l4);
                aClass19_1056.insertHead(animable_sub3);
            }
            return;
        }
        if(i == 44)
        {
            int j2 = stream1.method436();
            int i5 = stream1.readUnsignedWord();
            int l7 = stream1.readUnsignedByte();
            int k10 = anInt1268 + (l7 >> 4 & 7);
            int l12 = anInt1269 + (l7 & 7);
            if(k10 >= 0 && l12 >= 0 && k10 < 104 && l12 < 104)
            {
                Item item1 = new Item();
                item1.ID = j2;
                item1.anInt1559 = i5;
                if(groundArray[plane][k10][l12] == null)
                    groundArray[plane][k10][l12] = new NodeList();
                groundArray[plane][k10][l12].insertHead(item1);
                spawnGroundItem(k10, l12);
            }
            return;
        }
        if(i == 101)
        {
            int k2 = stream1.method427();
            int j5 = k2 >> 2;
            int i8 = k2 & 3;
            int l10 = anIntArray1177[j5];
            int i13 = stream1.readUnsignedByte();
            int j15 = anInt1268 + (i13 >> 4 & 7);
            int k16 = anInt1269 + (i13 & 7);
            if(j15 >= 0 && k16 >= 0 && j15 < 104 && k16 < 104)
                method130(-1, -1, i8, l10, k16, j5, plane, j15, 0);
            return;
        }
        if(i == 117)
        {
            int l2 = stream1.readUnsignedByte();
            int k5 = anInt1268 + (l2 >> 4 & 7);
            int j8 = anInt1269 + (l2 & 7);
            int i11 = k5 + stream1.readSignedByte();
            int j13 = j8 + stream1.readSignedByte();
            int k15 = stream1.readSignedWord();
            int l16 = stream1.readUnsignedWord();
            int l17 = stream1.readUnsignedByte() * 4;
            int k18 = stream1.readUnsignedByte() * 4;
            int j19 = stream1.readUnsignedWord();
            int i20 = stream1.readUnsignedWord();
            int l20 = stream1.readUnsignedByte();
            int i21 = stream1.readUnsignedByte();
            if(k5 >= 0 && j8 >= 0 && k5 < 104 && j8 < 104 && i11 >= 0 && j13 >= 0 && i11 < 104 && j13 < 104 && l16 != 65535)
            {
                k5 = k5 * 128 + 64;
                j8 = j8 * 128 + 64;
                i11 = i11 * 128 + 64;
                j13 = j13 * 128 + 64;
                Animable_Sub4 animable_sub4 = new Animable_Sub4(l20, k18, j19 + loopCycle, i20 + loopCycle, i21, plane, method42(plane, j8, k5) - l17, j8, k5, k15, l16);
                animable_sub4.method455(j19 + loopCycle, j13, method42(plane, j13, i11) - k18, i11);
                aClass19_1013.insertHead(animable_sub4);
            }
        }
    }


	private static void setLowMem()
	{
		WorldController.lowMem = true;
		Texture.lowMem = true;
		lowMem = true;
		ObjectManager.lowMem = true;
		ObjectDef.lowMem = true;
	}

	private void method139(Stream stream)
	{
		stream.initBitAccess();
		int k = stream.readBits(8);
		if(k < npcCount)
		{
			for(int l = k; l < npcCount; l++)
				anIntArray840[anInt839++] = npcIndices[l];

		}
		if(k > npcCount)
		{
			signlink.reporterror(myUsername + " Too many npcs");
			throw new RuntimeException("eek");
		}
		npcCount = 0;
		for(int i1 = 0; i1 < k; i1++)
		{
			int j1 = npcIndices[i1];
			NPC npc = npcArray[j1];
			int k1 = stream.readBits(1);
			if(k1 == 0)
			{
				npcIndices[npcCount++] = j1;
				npc.anInt1537 = loopCycle;
			} else
			{
				int l1 = stream.readBits(2);
				if(l1 == 0)
				{
					npcIndices[npcCount++] = j1;
					npc.anInt1537 = loopCycle;
					anIntArray894[anInt893++] = j1;
				} else
				if(l1 == 1)
				{
					npcIndices[npcCount++] = j1;
					npc.anInt1537 = loopCycle;
					int i2 = stream.readBits(3);
					npc.moveInDir(false, i2);
					int k2 = stream.readBits(1);
					if(k2 == 1)
						anIntArray894[anInt893++] = j1;
				} else
				if(l1 == 2)
				{
					npcIndices[npcCount++] = j1;
					npc.anInt1537 = loopCycle;
					int j2 = stream.readBits(3);
					npc.moveInDir(true, j2);
					int l2 = stream.readBits(3);
					npc.moveInDir(true, l2);
					int i3 = stream.readBits(1);
					if(i3 == 1)
						anIntArray894[anInt893++] = j1;
				} else
				if(l1 == 3)
					anIntArray840[anInt839++] = j1;
			}
		}

	}

	private void processLoginScreenInput()
	{
		if(loginScreenState == 0)
		{
			int i = super.myWidth / 2 - 80;
			int l = super.myHeight / 2 + 20;
			l += 20;
			if(super.clickMode3 == 1 && super.saveClickX >= i - 75 && super.saveClickX <= i + 75 && super.saveClickY >= l - 20 && super.saveClickY <= l + 20)
			{
				loginScreenState = 3;
				loginScreenCursorPos = 0;
			}
			i = super.myWidth / 2 + 80;
			if(super.clickMode3 == 1 && super.saveClickX >= i - 75 && super.saveClickX <= i + 75 && super.saveClickY >= l - 20 && super.saveClickY <= l + 20)
			{
				loginMessage1 = "";
				loginMessage2 = "Enter your username & password.";
				loginScreenState = 2;
				loginScreenCursorPos = 0;
			}
		} else
		{
			if(loginScreenState == 2)
			{
				int j = super.myHeight / 2 - 40;
				j += 30;
				j += 25;
				if(super.clickMode3 == 1 && super.saveClickY >= j - 15 && super.saveClickY < j)
					loginScreenCursorPos = 0;
				j += 15;
				if(super.clickMode3 == 1 && super.saveClickY >= j - 15 && super.saveClickY < j)
					loginScreenCursorPos = 1;
				j += 15;
				int i1 = super.myWidth / 2 - 80;
				int k1 = super.myHeight / 2 + 50;
				k1 += 20;
				if(super.clickMode3 == 1 && super.saveClickX >= i1 - 75 && super.saveClickX <= i1 + 75 && super.saveClickY >= k1 - 20 && super.saveClickY <= k1 + 20)
				{
					loginFailures = 0;
					login(myUsername, myPassword, false);
					if(loggedIn)
						return;
				}
				i1 = super.myWidth / 2 + 80;
				if(super.clickMode3 == 1 && super.saveClickX >= i1 - 75 && super.saveClickX <= i1 + 75 && super.saveClickY >= k1 - 20 && super.saveClickY <= k1 + 20)
				{
					loginScreenState = 0;
 				//	myUsername = "Dipshit";
 				//	myPassword = "Dipshit";
				}
				do
				{
					int l1 = readChar(-796);
					if(l1 == -1)
						break;
					boolean flag1 = false;
					for(int i2 = 0; i2 < validUserPassChars.length(); i2++)
					{
						if(l1 != validUserPassChars.charAt(i2))
							continue;
						flag1 = true;
						break;
					}

					if(loginScreenCursorPos == 0)
					{
						if(l1 == 8 && myUsername.length() > 0)
							myUsername = myUsername.substring(0, myUsername.length() - 1);
						if(l1 == 9 || l1 == 10 || l1 == 13)
							loginScreenCursorPos = 1;
						if(flag1)
							myUsername += (char)l1;
						if(myUsername.length() > 12)
							myUsername = myUsername.substring(0, 12);
					} else
					if(loginScreenCursorPos == 1)
					{
						if(l1 == 8 && myPassword.length() > 0)
							myPassword = myPassword.substring(0, myPassword.length() - 1);
						if(l1 == 9 || l1 == 10 || l1 == 13)
							loginScreenCursorPos = 0;
						if(flag1)
							myPassword += (char)l1;
						if(myPassword.length() > 20)
							myPassword = myPassword.substring(0, 20);
					}
				} while(true);
				return;
			}
			if(loginScreenState == 3)
			{
				int k = super.myWidth / 2;
				int j1 = super.myHeight / 2 + 50;
				j1 += 20;
				if(super.clickMode3 == 1 && super.saveClickX >= k - 75 && super.saveClickX <= k + 75 && super.saveClickY >= j1 - 20 && super.saveClickY <= j1 + 20)
					loginScreenState = 0;
			}
		}
	}

	private void markMinimap(Sprite sprite, int i, int j)
	{
		int k = minimapInt1 + minimapInt2 & 0x7ff;
		int l = i * i + j * j;
		if(l > 6400)
			return;
		int i1 = Model.modelIntArray1[k];
		int j1 = Model.modelIntArray2[k];
		i1 = (i1 * 256) / (minimapInt3 + 256);
		j1 = (j1 * 256) / (minimapInt3 + 256);
		int k1 = j * i1 + i * j1 >> 16;
		int l1 = j * j1 - i * i1 >> 16;
		if(l > 2500) {
			sprite.drawSprite(((94 + k1) - sprite.anInt1444 / 2) + 4 + 30, 83 - l1 - sprite.anInt1445 / 2 - 4 + 5);
		} else {
			sprite.drawSprite(((94 + k1) - sprite.anInt1444 / 2) + 4 + 30, 83 - l1 - sprite.anInt1445 / 2 - 4 + 5);
		}
	}

	private void method142(int i, int j, int k, int l, int i1, int j1, int k1
	)
	{
		if(i1 >= 1 && i >= 1 && i1 <= 102 && i <= 102)
		{
			if(lowMem && j != plane)
				return;
			int i2 = 0;
			if(j1 == 0)
				i2 = worldController.method300(j, i1, i);
			if(j1 == 1)
				i2 = worldController.method301(j, i1, i);
			if(j1 == 2)
				i2 = worldController.method302(j, i1, i);
			if(j1 == 3)
				i2 = worldController.method303(j, i1, i);
			if(i2 != 0)
			{
				int i3 = worldController.method304(j, i1, i, i2);
				int j2 = i2 >> 14 & 0x7fff;
				int k2 = i3 & 0x1f;
				int l2 = i3 >> 6;
				if(j1 == 0)
				{
					worldController.method291(i1, j, i, (byte)-119);
					ObjectDef class46 = ObjectDef.forID(j2);
					if(class46.aBoolean767)
						aClass11Array1230[j].method215(l2, k2, class46.aBoolean757, i1, i);
				}
				if(j1 == 1)
					worldController.method292(i, j, i1);
				if(j1 == 2)
				{
					worldController.method293(j, i1, i);
					ObjectDef class46_1 = ObjectDef.forID(j2);
					if(i1 + class46_1.anInt744 > 103 || i + class46_1.anInt744 > 103 || i1 + class46_1.anInt761 > 103 || i + class46_1.anInt761 > 103)
						return;
					if(class46_1.aBoolean767)
						aClass11Array1230[j].method216(l2, class46_1.anInt744, i1, i, class46_1.anInt761, class46_1.aBoolean757);
				}
				if(j1 == 3)
				{
					worldController.method294(j, i, i1);
					ObjectDef class46_2 = ObjectDef.forID(j2);
					if(class46_2.aBoolean767 && class46_2.hasActions)
						aClass11Array1230[j].method218(i, i1);
				}
			}
			if(k1 >= 0)
			{
				int j3 = j;
				if(j3 < 3 && (byteGroundArray[1][i1][i] & 2) == 2)
					j3++;
				ObjectManager.method188(worldController, k, i, l, j3, aClass11Array1230[j], intGroundArray, i1, k1, j);
			}
		}
	}

	private void updatePlayers(int i, Stream stream)
	{
		anInt839 = 0;
		anInt893 = 0;
		method117(stream);
		method134(stream);
		method91(stream, i);
		method49(stream);
		for(int k = 0; k < anInt839; k++)
		{
			int l = anIntArray840[k];
			if(playerArray[l].anInt1537 != loopCycle)
				playerArray[l] = null;
		}

		if(stream.currentOffset != i)
		{
			signlink.reporterror("Error packet size mismatch in getplayer pos:" + stream.currentOffset + " psize:" + i);
			throw new RuntimeException("eek");
		}
		for(int i1 = 0; i1 < playerCount; i1++)
			if(playerArray[playerIndices[i1]] == null)
			{
				signlink.reporterror(myUsername + " null entry in pl list - pos:" + i1 + " size:" + playerCount);
				throw new RuntimeException("eek");
			}

	}

	private void setCameraPos(int j, int k, int l, int i1, int j1, int k1)
	{
		int l1 = 2048 - k & 0x7ff;
		int i2 = 2048 - j1 & 0x7ff;
		int j2 = 0;
		int k2 = 0;
		int l2 = j;
		if(l1 != 0)
		{
			int i3 = Model.modelIntArray1[l1];
			int k3 = Model.modelIntArray2[l1];
			int i4 = k2 * k3 - l2 * i3 >> 16;
			l2 = k2 * i3 + l2 * k3 >> 16;
			k2 = i4;
		}
		if(i2 != 0)
		{
/* xxx			if(cameratoggle){
				if(zoom == 0)
				zoom = k2;
			  if(lftrit == 0)
				lftrit = j2;
			  if(fwdbwd == 0)
				fwdbwd = l2;
			  k2 = zoom;
			  j2 = lftrit;
			  l2 = fwdbwd;
			}
*/
			int j3 = Model.modelIntArray1[i2];
			int l3 = Model.modelIntArray2[i2];
			int j4 = l2 * j3 + j2 * l3 >> 16;
			l2 = l2 * l3 - j2 * j3 >> 16;
			j2 = j4;
		}
		xCameraPos = l - j2;
		zCameraPos = i1 - k2;
		yCameraPos = k1 - l2;
		yCameraCurve = k;
		xCameraCurve = j1;
	}

	private boolean parsePacket()
	{
		if(socketStream == null)
			return false;
		try
		{
			int i = socketStream.available();
			if(i == 0)
				return false;
			if(pktType == -1)
			{
				socketStream.flushInputStream(inStream.buffer, 1);
				pktType = inStream.buffer[0] & 0xff;
				if(encryption != null)
					pktType = pktType - encryption.getNextKey() & 0xff;
				pktSize = SizeConstants.packetSizes[pktType];
				i--;
			}
			if(pktSize == -1)
				if(i > 0)
				{
					socketStream.flushInputStream(inStream.buffer, 1);
					pktSize = inStream.buffer[0] & 0xff;
					i--;
				} else
				{
					return false;
				}
			if(pktSize == -2)
				if(i > 1)
				{
					socketStream.flushInputStream(inStream.buffer, 2);
					inStream.currentOffset = 0;
					pktSize = inStream.readUnsignedWord();
					i -= 2;
				} else
				{
					return false;
				}
			if(i < pktSize)
				return false;
			inStream.currentOffset = 0;
			socketStream.flushInputStream(inStream.buffer, pktSize);
			anInt1009 = 0;
			anInt843 = anInt842;
			anInt842 = anInt841;
			anInt841 = pktType;
			if(pktType == 81)
			{
				updatePlayers(pktSize, inStream);
				aBoolean1080 = false;
				pktType = -1;
				return true;
			}
			if(pktType == 176)
			{
				daysSinceRecovChange = inStream.method427();
				unreadMessages = inStream.method435();
				membersInt = inStream.readUnsignedByte();
				anInt1193 = inStream.method440();
				daysSinceLastLogin = inStream.readUnsignedWord();
				if(anInt1193 != 0 && openInterfaceID == -1)
				{
					signlink.dnslookup(TextClass.method586(anInt1193));
					clearTopInterfaces();
					char c = '\u028A';
					if(daysSinceRecovChange != 201 || membersInt == 1)
						c = '\u028F';
					reportAbuseInput = "";
					canMute = false;
					for(int k9 = 0; k9 < RSInterface.interfaceCache.length; k9++)
					{
						if(RSInterface.interfaceCache[k9] == null || RSInterface.interfaceCache[k9].contentType != c)
							continue;
						openInterfaceID = RSInterface.interfaceCache[k9].parentID;
						break;
					}

				}
				pktType = -1;
				return true;
			}
			if(pktType == 64)
			{
				anInt1268 = inStream.method427();
				anInt1269 = inStream.method428();
				for(int j = anInt1268; j < anInt1268 + 8; j++)
				{
					for(int l9 = anInt1269; l9 < anInt1269 + 8; l9++)
						if(groundArray[plane][j][l9] != null)
						{
							groundArray[plane][j][l9] = null;
							spawnGroundItem(j, l9);
						}

				}

				for(Class30_Sub1 class30_sub1 = (Class30_Sub1)aClass19_1179.reverseGetFirst(); class30_sub1 != null; class30_sub1 = (Class30_Sub1)aClass19_1179.reverseGetNext())
					if(class30_sub1.anInt1297 >= anInt1268 && class30_sub1.anInt1297 < anInt1268 + 8 && class30_sub1.anInt1298 >= anInt1269 && class30_sub1.anInt1298 < anInt1269 + 8 && class30_sub1.anInt1295 == plane)
						class30_sub1.anInt1294 = 0;

				pktType = -1;
				return true;
			}
			if(pktType == 185)
			{
				int k = inStream.method436();
				RSInterface.interfaceCache[k].anInt233 = 3;
				if(myPlayer.desc == null)
					RSInterface.interfaceCache[k].mediaID = (myPlayer.anIntArray1700[0] << 25) + (myPlayer.anIntArray1700[4] << 20) + (myPlayer.equipment[0] << 15) + (myPlayer.equipment[8] << 10) + (myPlayer.equipment[11] << 5) + myPlayer.equipment[1];
				else
					RSInterface.interfaceCache[k].mediaID = (int)(0x12345678L + myPlayer.desc.interfaceType);
				pktType = -1;
				return true;
			}
			if(pktType == 107)
			{
				aBoolean1160 = false;
				for(int l = 0; l < 5; l++)
					aBooleanArray876[l] = false;

				pktType = -1;
				return true;
			}
			if(pktType == 72)
			{
				int i1 = inStream.method434();
				RSInterface class9 = RSInterface.interfaceCache[i1];
				for(int k15 = 0; k15 < class9.inv.length; k15++)
				{
					class9.inv[k15] = -1;
					class9.inv[k15] = 0;
				}

				pktType = -1;
				return true;
			}
			if(pktType == 214)
			{
			/*	ignoreCount = pktSize / 8;
				for(int j1 = 0; j1 < ignoreCount; j1++)
					ignoreListAsLongs[j1] = inStream.readQWord();
*/
				long ignoreLong = inStream.readQWord();
				String ignoreName = TextClass.fixName(TextClass.nameForLong(ignoreLong));
				for (int k24 = 0; k24 < ignoreCount; k24++) {
					if (ignoreLong != ignoreListAsLongs[k24])
						continue;
					ignoreName = null;

				}
				if (ignoreName != null && ignoreCount < 100) {
					ignoreListAsLongs[ignoreCount] = ignoreLong;
					ignoreCount++;
					needDrawTabArea = true;
				}
				pktType = -1;
				return true;
			}
			if(pktType == 166)
			{
				aBoolean1160 = true;
				anInt1098 = inStream.readUnsignedByte();
				anInt1099 = inStream.readUnsignedByte();
				anInt1100 = inStream.readUnsignedWord();
				anInt1101 = inStream.readUnsignedByte();
				anInt1102 = inStream.readUnsignedByte();
				if(anInt1102 >= 100)
				{
					xCameraPos = anInt1098 * 128 + 64;
					yCameraPos = anInt1099 * 128 + 64;
					zCameraPos = method42(plane, yCameraPos, xCameraPos) - anInt1100;
				}
				pktType = -1;
				return true;
			}
			if(pktType == 134)
			{
				needDrawTabArea = true;
				int k1 = inStream.readUnsignedByte();
				int i10 = inStream.method439();
				int l15 = inStream.readUnsignedByte();
				currentExp[k1] = i10;
				currentStats[k1] = l15;
				maxStats[k1] = 1;
				for(int k20 = 0; k20 < 98; k20++)
					if(i10 >= anIntArray1019[k20])
						maxStats[k1] = k20 + 2;

				pktType = -1;
				return true;
			}
			if(pktType == 71)
			{
				int l1 = inStream.readUnsignedWord();
				int j10 = inStream.method426();
				if(l1 == 65535)
					l1 = -1;
				tabInterfaceIDs[j10] = l1;
				needDrawTabArea = true;
				tabAreaAltered = true;
				pktType = -1;
				return true;
			}
			if(pktType == 74)
			{
				int i2 = inStream.method434();
				if(i2 == 65535)
					i2 = -1;
				if(i2 != currentSong && musicEnabled && !lowMem && prevSong == 0)
				{
					nextSong = i2;
					songChanging = true;
					onDemandFetcher.method558(2, nextSong);
				}
				currentSong = i2;
				pktType = -1;
				return true;
			}
			if(pktType == 121)
			{
				int j2 = inStream.method436();
				int k10 = inStream.method435();
				if(musicEnabled && !lowMem)
				{
					nextSong = j2;
					songChanging = false;
					onDemandFetcher.method558(2, nextSong);
					prevSong = k10;
				}
				pktType = -1;
				return true;
			}
			if(pktType == 109)
			{
				resetLogout();
				pktType = -1;
				return false;
			}
			if(pktType == 70)
			{
				int k2 = inStream.readSignedWord();
				int l10 = inStream.method437();
				int i16 = inStream.method434();
				RSInterface class9_5 = RSInterface.interfaceCache[i16];
				class9_5.anInt263 = k2;
				class9_5.anInt265 = l10;
				pktType = -1;
				return true;
			}
			if(pktType == 73 || pktType == 241)
			{
				
				//mapReset();
				int l2 = anInt1069;
				int i11 = anInt1070;
				if(pktType == 73)
				{
					l2 = inStream.method435();
					i11 = inStream.readUnsignedWord();
					aBoolean1159 = false;
				}
				if(pktType == 241)
				{
					i11 = inStream.method435();
					inStream.initBitAccess();
					for(int j16 = 0; j16 < 4; j16++)
					{
						for(int l20 = 0; l20 < 13; l20++)
						{
							for(int j23 = 0; j23 < 13; j23++)
							{
								int i26 = inStream.readBits(1);
								if(i26 == 1)
									anIntArrayArrayArray1129[j16][l20][j23] = inStream.readBits(26);
								else
									anIntArrayArrayArray1129[j16][l20][j23] = -1;
							}

						}

					}
					inStream.finishBitAccess();
					l2 = inStream.readUnsignedWord();
					aBoolean1159 = true;
				}
				if(anInt1069 == l2 && anInt1070 == i11 && loadingStage == 2)
				{
					pktType = -1;
					return true;
				}
				anInt1069 = l2;
				anInt1070 = i11;
				baseX = (anInt1069 - 6) * 8;
				baseY = (anInt1070 - 6) * 8;
				aBoolean1141 = (anInt1069 / 8 == 48 || anInt1069 / 8 == 49) && anInt1070 / 8 == 48;
				if(anInt1069 / 8 == 48 && anInt1070 / 8 == 148)
					aBoolean1141 = true;
				loadingStage = 1;
				aLong824 = System.currentTimeMillis();
				aRSImageProducer_1165.initDrawingArea();
				aTextDrawingArea_1271.drawText(0, "Loading - please wait.", 151, 257);
				aTextDrawingArea_1271.drawText(0xffffff, "Loading - please wait.", 150, 256);
				aRSImageProducer_1165.drawGraphics(4, super.graphics, 4);
				if(pktType == 73)
				{
					int k16 = 0;
					for(int i21 = (anInt1069 - 6) / 8; i21 <= (anInt1069 + 6) / 8; i21++)
					{
						for(int k23 = (anInt1070 - 6) / 8; k23 <= (anInt1070 + 6) / 8; k23++)
							k16++;

					}

					aByteArrayArray1183 = new byte[k16][];
					aByteArrayArray1247 = new byte[k16][];
					anIntArray1234 = new int[k16];
					anIntArray1235 = new int[k16];
					anIntArray1236 = new int[k16];
					k16 = 0;
					for(int l23 = (anInt1069 - 6) / 8; l23 <= (anInt1069 + 6) / 8; l23++)
					{
						for(int j26 = (anInt1070 - 6) / 8; j26 <= (anInt1070 + 6) / 8; j26++)
						{
							anIntArray1234[k16] = (l23 << 8) + j26;
							if(aBoolean1141 && (j26 == 49 || j26 == 149 || j26 == 147 || l23 == 50 || l23 == 49 && j26 == 47))
							{
								anIntArray1235[k16] = -1;
								anIntArray1236[k16] = -1;
								k16++;
							} else
							{
								int k28 = anIntArray1235[k16] = onDemandFetcher.method562(0, j26, l23);
								if(k28 != -1)
									onDemandFetcher.method558(3, k28);
								int j30 = anIntArray1236[k16] = onDemandFetcher.method562(1, j26, l23);
								if(j30 != -1)
									onDemandFetcher.method558(3, j30);
								k16++;
							}
						}

					}

				}
				if(pktType == 241)
				{
					int l16 = 0;
					int ai[] = new int[676];
					for(int i24 = 0; i24 < 4; i24++)
					{
						for(int k26 = 0; k26 < 13; k26++)
						{
							for(int l28 = 0; l28 < 13; l28++)
							{
								int k30 = anIntArrayArrayArray1129[i24][k26][l28];
								if(k30 != -1)
								{
									int k31 = k30 >> 14 & 0x3ff;
									int i32 = k30 >> 3 & 0x7ff;
									int k32 = (k31 / 8 << 8) + i32 / 8;
									for(int j33 = 0; j33 < l16; j33++)
									{
										if(ai[j33] != k32)
											continue;
										k32 = -1;
										break;
									}

									if(k32 != -1)
										ai[l16++] = k32;
								}
							}

						}

					}

					aByteArrayArray1183 = new byte[l16][];
					aByteArrayArray1247 = new byte[l16][];
					anIntArray1234 = new int[l16];
					anIntArray1235 = new int[l16];
					anIntArray1236 = new int[l16];
					for(int l26 = 0; l26 < l16; l26++)
					{
						int i29 = anIntArray1234[l26] = ai[l26];
						int l30 = i29 >> 8 & 0xff;
						int l31 = i29 & 0xff;
						int j32 = anIntArray1235[l26] = onDemandFetcher.method562(0, l31, l30);
						if(j32 != -1)
							onDemandFetcher.method558(3, j32);
						int i33 = anIntArray1236[l26] = onDemandFetcher.method562(1, l31, l30);
						if(i33 != -1)
							onDemandFetcher.method558(3, i33);
					}

				}
				int i17 = baseX - anInt1036;
				int j21 = baseY - anInt1037;
				anInt1036 = baseX;
				anInt1037 = baseY;
				for(int j24 = 0; j24 < 16384; j24++)
				{
					NPC npc = npcArray[j24];
					if(npc != null)
					{
						for(int j29 = 0; j29 < 10; j29++)
						{
							npc.smallX[j29] -= i17;
							npc.smallY[j29] -= j21;
						}

						npc.x -= i17 * 128;
						npc.y -= j21 * 128;
					}
				}

				for(int i27 = 0; i27 < maxPlayers; i27++)
				{
					Player player = playerArray[i27];
					if(player != null)
					{
						for(int i31 = 0; i31 < 10; i31++)
						{
							player.smallX[i31] -= i17;
							player.smallY[i31] -= j21;
						}

						player.x -= i17 * 128;
						player.y -= j21 * 128;
					}
				}

				aBoolean1080 = true;
				byte byte1 = 0;
				byte byte2 = 104;
				byte byte3 = 1;
				if(i17 < 0)
				{
					byte1 = 103;
					byte2 = -1;
					byte3 = -1;
				}
				byte byte4 = 0;
				byte byte5 = 104;
				byte byte6 = 1;
				if(j21 < 0)
				{
					byte4 = 103;
					byte5 = -1;
					byte6 = -1;
				}
				for(int k33 = byte1; k33 != byte2; k33 += byte3)
				{
					for(int l33 = byte4; l33 != byte5; l33 += byte6)
					{
						int i34 = k33 + i17;
						int j34 = l33 + j21;
						for(int k34 = 0; k34 < 4; k34++)
							if(i34 >= 0 && j34 >= 0 && i34 < 104 && j34 < 104)
								groundArray[k34][k33][l33] = groundArray[k34][i34][j34];
							else
								groundArray[k34][k33][l33] = null;

					}

				}

				for(Class30_Sub1 class30_sub1_1 = (Class30_Sub1)aClass19_1179.reverseGetFirst(); class30_sub1_1 != null; class30_sub1_1 = (Class30_Sub1)aClass19_1179.reverseGetNext())
				{
					class30_sub1_1.anInt1297 -= i17;
					class30_sub1_1.anInt1298 -= j21;
					if(class30_sub1_1.anInt1297 < 0 || class30_sub1_1.anInt1298 < 0 || class30_sub1_1.anInt1297 >= 104 || class30_sub1_1.anInt1298 >= 104)
						class30_sub1_1.unlink();
				}

				if(destX != 0)
				{
					destX -= i17;
					destY -= j21;
				}
				aBoolean1160 = false;
				pktType = -1;
				return true;
			}
			if(pktType == 208)
			{
				int i3 = inStream.method437();
				if(i3 >= 0)
					method60(i3);
				anInt1018 = i3;
				pktType = -1;
				return true;
			}
			if(pktType == 99)
			{
				anInt1021 = inStream.readUnsignedByte();
				pktType = -1;
				return true;
			}
			if(pktType == 75)
			{
				int j3 = inStream.method436();
				int j11 = inStream.method436();
				RSInterface.interfaceCache[j11].anInt233 = 2;
				RSInterface.interfaceCache[j11].mediaID = j3;
				pktType = -1;
				return true;
			}
			if(pktType == 114)
			{
				anInt1104 = inStream.method434() * 30;
				pktType = -1;
				return true;
			}
			if(pktType == 60)
			{
				anInt1269 = inStream.readUnsignedByte();
				anInt1268 = inStream.method427();
				while(inStream.currentOffset < pktSize)
				{
					int k3 = inStream.readUnsignedByte();
					method137(inStream, k3);
				}
				pktType = -1;
				return true;
			}
			if(pktType == 35)
			{
				int l3 = inStream.readUnsignedByte();
				int k11 = inStream.readUnsignedByte();
				int j17 = inStream.readUnsignedByte();
				int k21 = inStream.readUnsignedByte();
				aBooleanArray876[l3] = true;
				anIntArray873[l3] = k11;
				anIntArray1203[l3] = j17;
				anIntArray928[l3] = k21;
				anIntArray1030[l3] = 0;
				pktType = -1;
				return true;
			}
			if(pktType == 174)
			{
				int i4 = inStream.readUnsignedWord();
				int l11 = inStream.readUnsignedByte();
				int k17 = inStream.readUnsignedWord();
				if(aBoolean848 && !lowMem && anInt1062 < 50)
				{
					anIntArray1207[anInt1062] = i4;
					anIntArray1241[anInt1062] = l11;
					anIntArray1250[anInt1062] = k17 + Sounds.anIntArray326[i4];
					anInt1062++;
				}
				pktType = -1;
				return true;
			}
			if(pktType == 104)
			{
				int j4 = inStream.method427();
				int i12 = inStream.method426();
				String s6 = inStream.readString();
				if(j4 >= 1 && j4 <= 5)
				{
					if(s6.equalsIgnoreCase("null"))
						s6 = null;
					atPlayerActions[j4 - 1] = s6;
					atPlayerArray[j4 - 1] = i12 == 0;
				}
				pktType = -1;
				return true;
			}
			if(pktType == 78)
			{
				destX = 0;
				pktType = -1;
				return true;
			}
			if(pktType == 253)
			{
				String s = inStream.readString();
				if(s.endsWith(":STOP:")) {
					try {
						// SoundProvider.playerPool.stop();
					} catch (Exception ex) { }
				}
				else if(s.endsWith(":yell:")) // GLOBAL CHAT
				{
					String yellName = s.substring(0, s.indexOf(":"));
					String yellNameString = yellName;
					if(yellNameString != null && (yellNameString.startsWith("@cr1@") || yellNameString.startsWith("@cr2@")
							|| yellNameString.startsWith("@cr3@"))) {
						yellNameString = yellNameString.substring(5);
					}
					long yellNameLong = TextClass.longForName(yellNameString);
					boolean yellflag = false;
					for(int j27 = 0; j27 < ignoreCount; j27++)
					{
						if(ignoreListAsLongs[j27] != yellNameLong)
							continue;
						yellflag = true;
						break;
					}
					if(!yellflag && anInt1251 == 0){
						String yellMessage = s.substring(s.indexOf(":") + 1, s.length() - 6);
						pushMessage(yellMessage, 9, yellName);
					}
				}
				else if(s.endsWith(":tradereq:"))
				{
					String s3 = s.substring(0, s.indexOf(":"));
					long l17 = TextClass.longForName(s3);
					boolean flag2 = false;
					for(int j27 = 0; j27 < ignoreCount; j27++)
					{
						if(ignoreListAsLongs[j27] != l17)
							continue;
						flag2 = true;
						break;
					}

					if(!flag2 && anInt1251 == 0)
						pushMessage("wishes to trade with you.", 4, s3);
				} else
				if(s.endsWith(":duelreq:"))
				{
					String s4 = s.substring(0, s.indexOf(":"));
					long l18 = TextClass.longForName(s4);
					boolean flag3 = false;
					for(int k27 = 0; k27 < ignoreCount; k27++)
					{
						if(ignoreListAsLongs[k27] != l18)
							continue;
						flag3 = true;
						break;
					}

					if(!flag3 && anInt1251 == 0)
						pushMessage("wishes to duel with you.", 8, s4);
				} else
				if(s.endsWith(":chalreq:"))
				{
					String s5 = s.substring(0, s.indexOf(":"));
					long l19 = TextClass.longForName(s5);
					boolean flag4 = false;
					for(int l27 = 0; l27 < ignoreCount; l27++)
					{
						if(ignoreListAsLongs[l27] != l19)
							continue;
						flag4 = true;
						break;
					}

					if(!flag4 && anInt1251 == 0)
					{
						String s8 = s.substring(s.indexOf(":") + 1, s.length() - 9);
						pushMessage(s8, 8, s5);
					}
				} else
				{
					pushMessage(s, 0, "");
				}
				pktType = -1;
				//serverMessage(s);
	
				return true;
			}
			if(pktType == 1)
			{
				for(int k4 = 0; k4 < playerArray.length; k4++)
					if(playerArray[k4] != null)
						playerArray[k4].anim = -1;

				for(int j12 = 0; j12 < npcArray.length; j12++)
					if(npcArray[j12] != null)
						npcArray[j12].anim = -1;

				pktType = -1;
				return true;
			}
			if(pktType == 50)
			{
				long l4 = inStream.readQWord();
				int i18 = inStream.readUnsignedByte();
				String s7 = TextClass.fixName(TextClass.nameForLong(l4));
				for(int k24 = 0; k24 < friendsCount; k24++)
				{
					if(l4 != friendsListAsLongs[k24])
						continue;
					if(friendsNodeIDs[k24] != i18)
					{
						friendsNodeIDs[k24] = i18;
						needDrawTabArea = true;
						if(i18 >= 2) {
							pushMessage("     " + s7 + " has logged in.", 5, "");
						}
						if(i18 <= 1) {
							pushMessage("     "  + s7 + " has logged out.", 5, "");
						}
					}
					s7 = null;
					break;
				}

				if(s7 != null && friendsCount < 200)
				{
					friendsListAsLongs[friendsCount] = l4;
					friendsList[friendsCount] = s7;
					friendsNodeIDs[friendsCount] = i18;
					friendsCount++;
					needDrawTabArea = true;
				}
				for(boolean flag6 = false; !flag6;)
				{
					flag6 = true;
					for(int k29 = 0; k29 < friendsCount - 1; k29++)
						if(friendsNodeIDs[k29] != nodeID && friendsNodeIDs[k29 + 1] == nodeID || friendsNodeIDs[k29] == 0 && friendsNodeIDs[k29 + 1] != 0)
						{
							int j31 = friendsNodeIDs[k29];
							friendsNodeIDs[k29] = friendsNodeIDs[k29 + 1];
							friendsNodeIDs[k29 + 1] = j31;
							String s10 = friendsList[k29];
							friendsList[k29] = friendsList[k29 + 1];
							friendsList[k29 + 1] = s10;
							long l32 = friendsListAsLongs[k29];
							friendsListAsLongs[k29] = friendsListAsLongs[k29 + 1];
							friendsListAsLongs[k29 + 1] = l32;
							needDrawTabArea = true;
							flag6 = false;
						}

				}

				pktType = -1;
				return true;
			}
			if(pktType == 110)
			{
				if(tabID == 12)
					needDrawTabArea = true;
				energy = inStream.readUnsignedByte();
				pktType = -1;
				return true;
			}
			if(pktType == 254)
			{
				anInt855 = inStream.readUnsignedByte();
				if(anInt855 == 1)
					anInt1222 = inStream.readUnsignedWord();
				if(anInt855 >= 2 && anInt855 <= 6)
				{
					if(anInt855 == 2)
					{
						anInt937 = 64;
						anInt938 = 64;
					}
					if(anInt855 == 3)
					{
						anInt937 = 0;
						anInt938 = 64;
					}
					if(anInt855 == 4)
					{
						anInt937 = 128;
						anInt938 = 64;
					}
					if(anInt855 == 5)
					{
						anInt937 = 64;
						anInt938 = 0;
					}
					if(anInt855 == 6)
					{
						anInt937 = 64;
						anInt938 = 128;
					}
					anInt855 = 2;
					anInt934 = inStream.readUnsignedWord();
					anInt935 = inStream.readUnsignedWord();
					anInt936 = inStream.readUnsignedByte();
				}
				if(anInt855 == 10)
					anInt933 = inStream.readUnsignedWord();
				pktType = -1;
				return true;
			}
			if(pktType == 248)
			{
				int i5 = inStream.method435();
				int k12 = inStream.readUnsignedWord();
				if(backDialogID != -1)
				{
					backDialogID = -1;
					inputTaken = true;
				}
				if(inputDialogState != 0)
				{
					inputDialogState = 0;
					inputTaken = true;
				}
				openInterfaceID = i5;
				invOverlayInterfaceID = k12;
				needDrawTabArea = true;
				tabAreaAltered = true;
				aBoolean1149 = false;
				pktType = -1;
				return true;
			}
			if(pktType == 79)
			{
				int j5 = inStream.method434();
				int l12 = inStream.method435();
				RSInterface class9_3 = RSInterface.interfaceCache[j5];
				if(class9_3 != null && class9_3.type == 0)
				{
					if(l12 < 0)
						l12 = 0;
					if(l12 > class9_3.scrollMax - class9_3.height)
						l12 = class9_3.scrollMax - class9_3.height;
					class9_3.scrollPosition = l12;
				}
				pktType = -1;
				return true;
			}
			if(pktType == 68)
			{
				for(int k5 = 0; k5 < variousSettings.length; k5++)
					if(variousSettings[k5] != anIntArray1045[k5])
					{
						variousSettings[k5] = anIntArray1045[k5];
						method33(k5);
						needDrawTabArea = true;
					}

				pktType = -1;
				return true;
			}
			if(pktType == 217)
			{
				try {
					globalPrefix = inStream.readString();
					String name = inStream.readString();
					String message = inStream.readString();
					int rights = inStream.readUnsignedByte();
					boolean ignore = false;
					long nameeLong = TextClass.longForName(name);
					for (int l29 = 0; l29 < ignoreCount; l29++) {
						if (ignoreListAsLongs[l29] != nameeLong)
							continue;
						ignore = true;

					}
					if(!ignore){
						if (rights == 2 || rights == 3)
							pushMessage(message, 9, "@cr2@" + TextClass.fixName(name));
						else if (rights == 1)
							pushMessage(message, 9, "@cr1@" + TextClass.fixName(name));
						else
							pushMessage(message, 9, TextClass.fixName(name));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				pktType = -1;
				return true;
			}
			if(pktType == 196)
			{
				long l5 = inStream.readQWord();
					int j18 = inStream.readDWord();
					int l21 = inStream.readUnsignedByte();
					boolean flag5 = false;
					if(l21 <= 1) {
						for(int l29 = 0; l29 < ignoreCount; l29++) {
							if(ignoreListAsLongs[l29] != l5)
								continue;
							flag5 = true;
							
						}
					}
					if(!flag5 && anInt1251 == 0)
						try {
							String s9 = TextInput.method525(pktSize - 13, inStream);
							//if(l21 != 3)
								//s9 = Censor.doCensor(s9);
							if(l21 == 2 || l21 == 3)
								pushMessage(s9, 7, "@cr2@" + TextClass.fixName(TextClass.nameForLong(l5)));
							else
							if(l21 == 1)
								pushMessage(s9, 7, "@cr1@" + TextClass.fixName(TextClass.nameForLong(l5)));
							else
								pushMessage(s9, 3, TextClass.fixName(TextClass.nameForLong(l5)));
						} catch(Exception exception1) {
							signlink.reporterror("cde1");
						}
					pktType = -1;
					return true;
			}
			if(pktType == 85)
			{
				anInt1269 = inStream.method427();
				anInt1268 = inStream.method427();
				pktType = -1;
				return true;
			}
			if(pktType == 24)
			{
				anInt1054 = inStream.method428();
				if(anInt1054 == tabID)
				{
					if(anInt1054 == 3)
						tabID = 1;
					else
						tabID = 3;
					needDrawTabArea = true;
				}
				pktType = -1;
				return true;
			}
			if(pktType == 246)
			{
				int i6 = inStream.method434();
				int i13 = inStream.readUnsignedWord();
				int k18 = inStream.readUnsignedWord();
				if(k18 == 65535)
				{
					RSInterface.interfaceCache[i6].anInt233 = 0;
					pktType = -1;
					return true;
				} else
				{
					ItemDef itemDef = ItemDef.forID(k18);
					RSInterface.interfaceCache[i6].anInt233 = 4;
					RSInterface.interfaceCache[i6].mediaID = k18;
					RSInterface.interfaceCache[i6].modelRotation1 = itemDef.modelRotation1;
					RSInterface.interfaceCache[i6].modelRotation2 = itemDef.modelRotation2;
					RSInterface.interfaceCache[i6].anInt269 = (itemDef.modelZoom * 100) / i13;
					pktType = -1;
					return true;
				}
			}
			if(pktType == 171)
			{
				boolean flag1 = inStream.readUnsignedByte() == 1;
				int j13 = inStream.readUnsignedWord();
				RSInterface.interfaceCache[j13].isMouseoverTriggered = flag1;
				pktType = -1;
				return true;
			}
			if(pktType == 142)
			{
				int j6 = inStream.method434();
				method60(j6);
				if(backDialogID != -1)
				{
					backDialogID = -1;
					inputTaken = true;
				}
				if(inputDialogState != 0)
				{
					inputDialogState = 0;
					inputTaken = true;
				}
				invOverlayInterfaceID = j6;
				needDrawTabArea = true;
				tabAreaAltered = true;
				openInterfaceID = -1;
				aBoolean1149 = false;
				pktType = -1;
				return true;
			}
			if(pktType == 126)
			{
				String s1 = inStream.readString();
				int k13 = inStream.method435();
				RSInterface.interfaceCache[k13].message = s1;
				if(RSInterface.interfaceCache[k13].parentID == tabInterfaceIDs[tabID])
					needDrawTabArea = true;
				pktType = -1;
				return true;
			}
			if(pktType == 206)
			{
				publicChatMode = inStream.readUnsignedByte();
				privateChatMode = inStream.readUnsignedByte();
				tradeMode = inStream.readUnsignedByte();
				aBoolean1233 = true;
				inputTaken = true;
				pktType = -1;
				return true;
			}
			if(pktType == 240)
			{
				if(tabID == 12)
					needDrawTabArea = true;
				weight = inStream.readSignedWord();
				pktType = -1;
				return true;
			}
			if(pktType == 8)
			{
				int k6 = inStream.method436();
				int l13 = inStream.readUnsignedWord();
				RSInterface.interfaceCache[k6].anInt233 = 1;
				RSInterface.interfaceCache[k6].mediaID = l13;
				pktType = -1;
				return true;
			}
			if(pktType == 122)
			{
				int l6 = inStream.method436();
				int i14 = inStream.method436();
				int i19 = i14 >> 10 & 0x1f;
				int i22 = i14 >> 5 & 0x1f;
				int l24 = i14 & 0x1f;
				RSInterface.interfaceCache[l6].textColor = (i19 << 19) + (i22 << 11) + (l24 << 3);
				pktType = -1;
				return true;
			}
			if(pktType == 53)
			{
				needDrawTabArea = true;
				int i7 = inStream.readUnsignedWord();
				RSInterface class9_1 = RSInterface.interfaceCache[i7];
				int j19 = inStream.readUnsignedWord();
				for(int j22 = 0; j22 < j19; j22++)
				{
					int i25 = inStream.readUnsignedByte();
					if(i25 == 255)
						i25 = inStream.method440();
					class9_1.inv[j22] = inStream.method436();
					class9_1.invStackSizes[j22] = i25;
				}

				for(int j25 = j19; j25 < class9_1.inv.length; j25++)
				{
					class9_1.inv[j25] = 0;
					class9_1.invStackSizes[j25] = 0;
				}

				pktType = -1;
				return true;
			}
			if(pktType == 230)
			{
				int j7 = inStream.method435();
				int j14 = inStream.readUnsignedWord();
				int k19 = inStream.readUnsignedWord();
				int k22 = inStream.method436();
				RSInterface.interfaceCache[j14].modelRotation1 = k19;
				RSInterface.interfaceCache[j14].modelRotation2 = k22;
				RSInterface.interfaceCache[j14].anInt269 = j7;
				pktType = -1;
				return true;
			}
			if(pktType == 221)
			{
				anInt900 = inStream.readUnsignedByte();
				needDrawTabArea = true;
				pktType = -1;
				return true;
			}
			if(pktType == 177)
			{
				aBoolean1160 = true;
				anInt995 = inStream.readUnsignedByte();
				anInt996 = inStream.readUnsignedByte();
				anInt997 = inStream.readUnsignedWord();
				anInt998 = inStream.readUnsignedByte();
				anInt999 = inStream.readUnsignedByte();
				if(anInt999 >= 100)
				{
					int k7 = anInt995 * 128 + 64;
					int k14 = anInt996 * 128 + 64;
					int i20 = method42(plane, k14, k7) - anInt997;
					int l22 = k7 - xCameraPos;
					int k25 = i20 - zCameraPos;
					int j28 = k14 - yCameraPos;
					int i30 = (int)Math.sqrt(l22 * l22 + j28 * j28);
					yCameraCurve = (int)(Math.atan2(k25, i30) * 325.94900000000001D) & 0x7ff;
					xCameraCurve = (int)(Math.atan2(l22, j28) * -325.94900000000001D) & 0x7ff;
					if(yCameraCurve < 128)
						yCameraCurve = 128;
					if(yCameraCurve > 383)
						yCameraCurve = 383;
				}
				pktType = -1;
				return true;
			}
			if(pktType == 249)
			{
				anInt1046 = inStream.method426();
				unknownInt10 = inStream.method436();
				pktType = -1;
				return true;
			}
			if(pktType == 65)
			{
				updateNPCs(inStream, pktSize);
				pktType = -1;
				return true;
			}
			if(pktType == 27)
			{
				messagePromptRaised = false;
				inputDialogState = 1;
				amountOrNameInput = "";
				inputTaken = true;
				pktType = -1;
				return true;
			}
			if(pktType == 187)
			{
				messagePromptRaised = false;
				inputDialogState = 2;
				amountOrNameInput = "";
				inputTaken = true;
				pktType = -1;
				return true;
			}
			if(pktType == 97)
			{
				int l7 = inStream.readUnsignedWord();
				method60(l7);
				if(invOverlayInterfaceID != -1)
				{
					invOverlayInterfaceID = -1;
					needDrawTabArea = true;
					tabAreaAltered = true;
				}
				if(backDialogID != -1)
				{
					backDialogID = -1;
					inputTaken = true;
				}
				if(inputDialogState != 0)
				{
					inputDialogState = 0;
					inputTaken = true;
				}
				openInterfaceID = l7;
				aBoolean1149 = false;
				pktType = -1;
				return true;
			}
			if(pktType == 218)
			{
				int i8 = inStream.method438();
				dialogID = i8;
				inputTaken = true;
				pktType = -1;
				return true;
			}
			if(pktType == 87)
			{
				int j8 = inStream.method434();
				int l14 = inStream.method439();
				anIntArray1045[j8] = l14;
				if(variousSettings[j8] != l14)
				{
					variousSettings[j8] = l14;
					method33(j8);
					needDrawTabArea = true;
					if(dialogID != -1)
						inputTaken = true;
				}
				pktType = -1;
				return true;
			}
			if(pktType == 36)
			{
				int k8 = inStream.method434();
				byte byte0 = inStream.readSignedByte();
				anIntArray1045[k8] = byte0;
				if(variousSettings[k8] != byte0)
				{
					variousSettings[k8] = byte0;
					method33(k8);
					needDrawTabArea = true;
					if(dialogID != -1)
						inputTaken = true;
				}
				pktType = -1;
				return true;
			}
			if(pktType == 61)
			{
				anInt1055 = inStream.readUnsignedByte();
				pktType = -1;
				return true;
			}
			if(pktType == 200)
			{
				int l8 = inStream.readUnsignedWord();
				int i15 = inStream.readSignedWord();
				RSInterface class9_4 = RSInterface.interfaceCache[l8];
				class9_4.anInt257 = i15;
				if(i15 == -1)
				{
					class9_4.anInt246 = 0;
					class9_4.anInt208 = 0;
				}
				pktType = -1;
				return true;
			}
			if(pktType == 219)
			{
				if(invOverlayInterfaceID != -1)
				{
					invOverlayInterfaceID = -1;
					needDrawTabArea = true;
					tabAreaAltered = true;
				}
				if(backDialogID != -1)
				{
					backDialogID = -1;
					inputTaken = true;
				}
				if(inputDialogState != 0)
				{
					inputDialogState = 0;
					inputTaken = true;
				}
				openInterfaceID = -1;
				aBoolean1149 = false;
				pktType = -1;
				return true;
			}
			if(pktType == 34)
			{
				needDrawTabArea = true;
				int i9 = inStream.readUnsignedWord();
				RSInterface class9_2 = RSInterface.interfaceCache[i9];
				while(inStream.currentOffset < pktSize)
				{
					int j20 = inStream.method422();
					int i23 = inStream.readUnsignedWord();
					int l25 = inStream.readUnsignedByte();
					if(l25 == 255)
						l25 = inStream.readDWord();
					if(j20 >= 0 && j20 < class9_2.inv.length)
					{
						class9_2.inv[j20] = i23;
						class9_2.invStackSizes[j20] = l25;
					}
				}
				pktType = -1;
				return true;
			}
			if(pktType == 105 || pktType == 84 || pktType == 147 || pktType == 215 || pktType == 4 || pktType == 117 || pktType == 156 || pktType == 44 || pktType == 160 || pktType == 101 || pktType == 151)
			{
				method137(inStream, pktType);
				pktType = -1;
				return true;
			}
			if(pktType == 106)
			{
				tabID = inStream.method427();
				needDrawTabArea = true;
				tabAreaAltered = true;
				pktType = -1;
				return true;
			}
			if(pktType == 164)
			{
				int j9 = inStream.method434();
				method60(j9);
				if(invOverlayInterfaceID != -1)
				{
					invOverlayInterfaceID = -1;
					needDrawTabArea = true;
					tabAreaAltered = true;
				}
				backDialogID = j9;
				inputTaken = true;
				openInterfaceID = -1;
				aBoolean1149 = false;
				pktType = -1;
				return true;
			}
			signlink.reporterror("T1 - " + pktType + "," + pktSize + " - " + anInt842 + "," + anInt843);
			resetLogout();
		}
		catch(IOException _ex)
		{
			dropClient();
		}
		catch(Exception exception)
		{
			String s2 = "T2 - " + pktType + "," + anInt842 + "," + anInt843 + " - " + pktSize + "," + (baseX + myPlayer.smallX[0]) + "," + (baseY + myPlayer.smallY[0]) + " - ";
			for(int j15 = 0; j15 < pktSize && j15 < 50; j15++)
				s2 = s2 + inStream.buffer[j15] + ",";

			signlink.reporterror(s2);
			resetLogout();
		}
		return true;
	}

	public void method146() {
		anInt1265++;
		method47(true);
		method26(true);
		method47(false);
		method26(false);
		method55();
		method104();
		if (!aBoolean1160) {
			int i = anInt1184;
			if (anInt984 / 256 > i)
				i = anInt984 / 256;
			if (aBooleanArray876[4] && anIntArray1203[4] + 128 > i)
				i = anIntArray1203[4] + 128;
			int k = minimapInt1 + anInt896 & 0x7ff;
			setCameraPos(600 + i * 3, i, anInt1014,
					method42(plane, myPlayer.y, myPlayer.x) - 50, k, anInt1015);
		}
		int j;
		if (!aBoolean1160)
			j = method120();
		else
			j = method121();
		int l = xCameraPos;
		int i1 = zCameraPos;
		int j1 = yCameraPos;
		int k1 = yCameraCurve;
		int l1 = xCameraCurve;
		for (int i2 = 0; i2 < 5; i2++)
			if (aBooleanArray876[i2]) {
				int j2 = (int) ((Math.random()
						* (double) (anIntArray873[i2] * 2 + 1) - (double) anIntArray873[i2]) + Math
						.sin((double) anIntArray1030[i2]
								* ((double) anIntArray928[i2] / 100D))
						* (double) anIntArray1203[i2]);
				if (i2 == 0)
					xCameraPos += j2;
				if (i2 == 1)
					zCameraPos += j2;
				if (i2 == 2)
					yCameraPos += j2;
				if (i2 == 3)
					xCameraCurve = xCameraCurve + j2 & 0x7ff;
				if (i2 == 4) {
					yCameraCurve += j2;
					if (yCameraCurve < 128)
						yCameraCurve = 128;
					if (yCameraCurve > 383)
						yCameraCurve = 383;
				}
			}
		int k2 = Texture.anInt1481;
		Model.aBoolean1684 = true;
		Model.anInt1687 = 0;
		Model.anInt1685 = super.mouseX - 4;
		Model.anInt1686 = super.mouseY - 4;
		DrawingArea.setAllPixelsToZero();
		worldController.method313(xCameraPos, yCameraPos, xCameraCurve,
				zCameraPos, j, yCameraCurve);
		worldController.clearObj5Cache();
		updateEntities();
		drawHeadIcon();
		method37(k2);
		draw3dScreen();
		aRSImageProducer_1165.drawGraphics(4, super.graphics, 4);
		xCameraPos = l;
		zCameraPos = i1;
		yCameraPos = j1;
		yCameraCurve = k1;
		xCameraCurve = l1;
	}

	private void clearTopInterfaces()
	{
		stream.createFrame(130);
		if(invOverlayInterfaceID != -1)
		{
			invOverlayInterfaceID = -1;
			needDrawTabArea = true;
			aBoolean1149 = false;
			tabAreaAltered = true;
		}
		if(backDialogID != -1)
		{
			backDialogID = -1;
			inputTaken = true;
			aBoolean1149 = false;
		}
		openInterfaceID = -1;
	}

	public client()
	{
		chatTypeView = 0;
		duelMode = 0;
		gameMode = 0;
		globalMode = 0;
		clanChatMode = 0;
		cButtonCPos = 0;
		if(DevMode)
		{
			server = "127.0.0.1";
		}
		else
		{
			server = "vidyascape.no-ip.org";
		}
		anIntArrayArray825 = new int[104][104];
		friendsNodeIDs = new int[200];
		groundArray = new NodeList[4][104][104];
		aBoolean831 = false;
		aStream_834 = new Stream(new byte[5000]);
		npcArray = new NPC[16384];
		npcIndices = new int[16384];
		anIntArray840 = new int[1000];
		aStream_847 = Stream.create();
		aBoolean848 = true;
		openInterfaceID = -1;
		currentExp = new int[Skills.skillsCount];
		aBoolean872 = false;
		anIntArray873 = new int[5];
		anInt874 = -1;
		aBooleanArray876 = new boolean[5];
		drawFlames = false;
		reportAbuseInput = "";
		unknownInt10 = -1;
		menuOpen = false;
		inputString = "";
		maxPlayers = 2048;
		myPlayerIndex = 2047;
		playerArray = new Player[maxPlayers];
		playerIndices = new int[maxPlayers];
		anIntArray894 = new int[maxPlayers];
		aStreamArray895s = new Stream[maxPlayers];
		anInt897 = 1;
		anIntArrayArray901 = new int[104][104];
		anInt902 = 0x766654;
		aByteArray912 = new byte[16384];
		currentStats = new int[Skills.skillsCount];
		ignoreListAsLongs = new long[100];
		loadingError = false;
		anInt927 = 0x332d25;
		anIntArray928 = new int[5];
		anIntArrayArray929 = new int[104][104];
		chatTypes = new int[500];
		chatNames = new String[500];
		chatMessages = new String[500];
		sideIcons = new Sprite[14];
		newSideIcons = new Sprite[14];
		redStones = new Sprite[10];
		aBoolean954 = true;
		friendsListAsLongs = new long[200];
		currentSong = -1;
		drawingFlames = false;
		spriteDrawX = -1;
		spriteDrawY = -1;
		anIntArray968 = new int[33];
		anIntArray969 = new int[256];
		decompressors = new Decompressor[5];
		variousSettings = new int[2000];
		aBoolean972 = false;
		anInt975 = 50;
		anIntArray976 = new int[anInt975];
		anIntArray977 = new int[anInt975];
		anIntArray978 = new int[anInt975];
		anIntArray979 = new int[anInt975];
		anIntArray980 = new int[anInt975];
		anIntArray981 = new int[anInt975];
		anIntArray982 = new int[anInt975];
		aStringArray983 = new String[anInt975];
		anInt985 = -1;
		hitMarks = new Sprite[20];
		anIntArray990 = new int[5];
		aBoolean994 = false;
		anInt1002 = 0x23201b;
		amountOrNameInput = "";
		aClass19_1013 = new NodeList();
		aBoolean1017 = false;
		anInt1018 = -1;
		anIntArray1030 = new int[5];
		aBoolean1031 = false;
		mapFunctions = new Sprite[100];
		dialogID = -1;
		maxStats = new int[Skills.skillsCount];
		anIntArray1045 = new int[2000];
		aBoolean1047 = true;
		anIntArray1052 = new int[151];
		anInt1054 = -1;
		aClass19_1056 = new NodeList();
		anIntArray1057 = new int[33];
		aClass9_1059 = new RSInterface();
		mapScenes = new Background[100];
		barFillColor = 0x4d4233;
		anIntArray1065 = new int[7];
		anIntArray1072 = new int[1000];
		anIntArray1073 = new int[1000];
		aBoolean1080 = false;
		friendsList = new String[200];
		inStream = Stream.create();
		expectedCRCs = new int[9];
		menuActionCmd2 = new int[500];
		menuActionCmd3 = new int[500];
		menuActionID = new int[500];
		menuActionCmd1 = new int[500];
		headIcons = new Sprite[20];
		headIconsHint = new Sprite[20];
		skullIcons = new Sprite[20];
		tabAreaAltered = false;
		aString1121 = "";
		atPlayerActions = new String[5];
		atPlayerArray = new boolean[5];
		anIntArrayArrayArray1129 = new int[4][13][13];
		anInt1132 = 2;
		aClass30_Sub2_Sub1_Sub1Array1140 = new Sprite[1000];
		aBoolean1141 = false;
		aBoolean1149 = false;
		crosses = new Sprite[8];
		musicEnabled = MusicEnabled;
		needDrawTabArea = false;
		loggedIn = false;
		canMute = false;
		aBoolean1159 = false;
		aBoolean1160 = false;
		anInt1171 = 1;
		myUsername = "";
		myPassword = "";
		genericLoadingError = false;
		reportAbuseInterfaceID = -1;
		aClass19_1179 = new NodeList();
		anInt1184 = 128;
		invOverlayInterfaceID = -1;
		stream = Stream.create();
		menuActionName = new String[500];
		anIntArray1203 = new int[5];
		anIntArray1207 = new int[50];
		anInt1210 = 2;
		anInt1211 = 78;
		promptInput = "";
		modIcons = new Background[2];
		tabID = 3;
		inputTaken = false;
		songChanging = true;
		anIntArray1229 = new int[151];
		aClass11Array1230 = new Class11[4];
		aBoolean1233 = false;
		anIntArray1240 = new int[100];
		anIntArray1241 = new int[50];
		aBoolean1242 = false;
		
		anIntArray1250 = new int[50];
		
		rsAlreadyLoaded = false;
		welcomeScreenRaised = false;
		messagePromptRaised = false;
		loginMessage1 = "";
		loginMessage2 = "";
		backDialogID = -1;
		anInt1279 = 2;
		bigX = new int[4000];
		bigY = new int[4000];
		anInt1289 = -1;
	}

	public String globalPrefix = "";
	public int chatTypeView;
	public int clanChatMode;
	public int duelMode;
	public int gameMode;
	public int globalMode;
	/* Declare custom sprites */
	private Sprite chatArea;
	private Sprite chatButtons;
	private Sprite chatButtonH;
	private Sprite chatButtonHC;
	private Sprite chatButtonC;
	private Sprite reportH;
	private Sprite tabArea;
	private Sprite mapArea;
	private Sprite[] newSideIcons;
	/**/
	private RSImageProducer frame;
	private int ignoreCount;
	private long aLong824;
	private int[][] anIntArrayArray825;
	private int[] friendsNodeIDs;
	private NodeList[][][] groundArray;
	private int[] anIntArray828;
	private int[] anIntArray829;
	private volatile boolean aBoolean831;
	private Socket aSocket832;
	private int loginScreenState;
	private Stream aStream_834;
	private NPC[] npcArray;
	private int npcCount;
	private int[] npcIndices;
	private int anInt839;
	private int[] anIntArray840;
	private int anInt841;
	private int anInt842;
	private int anInt843;
	private String aString844;
	private int privateChatMode;
	private Stream aStream_847;
	private boolean aBoolean848;
	private static int anInt849;
	private int[] anIntArray850;
	private int[] anIntArray851;
	private int[] anIntArray852;
	private int[] anIntArray853;
	private static int anInt854;
	private int anInt855;
	public static int openInterfaceID;
	private int xCameraPos;
	private int zCameraPos;
	private int yCameraPos;
	private int yCameraCurve;
	private int xCameraCurve;
	private int myPrivilege;
	private final int[] currentExp;
	private Sprite[] redStones;
	private Sprite mapFlag;
	private Sprite mapMarker;
	private boolean aBoolean872;
	private final int[] anIntArray873;
	private int anInt874;
	private final boolean[] aBooleanArray876;
	private int weight;
	private MouseDetection mouseDetection;
	private volatile boolean drawFlames;
	private String reportAbuseInput;
	private int unknownInt10;
	private boolean menuOpen;
	private int anInt886;
	private String inputString;
	private final int maxPlayers;
	private final int myPlayerIndex;
	private Player[] playerArray;
	private int playerCount;
	private int[] playerIndices;
	private int anInt893;
	private int[] anIntArray894;
	private Stream[] aStreamArray895s;
	private int anInt896;
	private int anInt897;
	private int friendsCount;
	private int anInt900;
	private int[][] anIntArrayArray901;
	private final int anInt902;
	private byte[] aByteArray912;
	private int anInt913;
	private int crossX;
	private int crossY;
	private int crossIndex;
	private int crossType;
	private int plane;
	private final int[] currentStats;
	private static int anInt924;
	private final long[] ignoreListAsLongs;
	private boolean loadingError;
	private final int anInt927;
	private final int[] anIntArray928;
	private int[][] anIntArrayArray929;
	private Sprite aClass30_Sub2_Sub1_Sub1_931;
	private Sprite aClass30_Sub2_Sub1_Sub1_932;
	private int anInt933;
	private int anInt934;
	private int anInt935;
	private int anInt936;
	private int anInt937;
	private int anInt938;
	private static int anInt940;
	private final int[] chatTypes;
	private final String[] chatNames;
	private final String[] chatMessages;
	private int anInt945;
	private WorldController worldController;
	private Sprite[] sideIcons;
	private int menuScreenArea;
	private int menuOffsetX;
	private int menuOffsetY;
	private int menuWidth;
	private int menuHeight;
	private long aLong953;
	private boolean aBoolean954;
	private long[] friendsListAsLongs;
	private int currentSong;
	private static int nodeID = 10;
	static int portOff;
	static boolean clientData;
	private static boolean isMembers = true;
	private static boolean lowMem;
	private volatile boolean drawingFlames;
	private int spriteDrawX;
	private int spriteDrawY;
	private final int[] anIntArray965 = {
		0xffff00, 0xff0000, 65280, 65535, 0xff00ff, 0xffffff
	};
	private Background aBackground_966;
	private Background aBackground_967;
	private final int[] anIntArray968;
	private final int[] anIntArray969;
	final Decompressor[] decompressors;
	public int variousSettings[];
	private boolean aBoolean972;
	private final int anInt975;
	private final int[] anIntArray976;
	private final int[] anIntArray977;
	private final int[] anIntArray978;
	private final int[] anIntArray979;
	private final int[] anIntArray980;
	private final int[] anIntArray981;
	private final int[] anIntArray982;
	private final String[] aStringArray983;
	private int anInt984;
	private int anInt985;
	private static int anInt986;
	private Sprite[] hitMarks;
	private int anInt988;
	private int anInt989;
	private final int[] anIntArray990;
	private static boolean aBoolean993;
	private final boolean aBoolean994;
	private int anInt995;
	private int anInt996;
	private int anInt997;
	private int anInt998;
	private int anInt999;
	private ISAACRandomGen encryption;
	private Sprite mapEdge;
	private final int anInt1002;
	static final int[][] anIntArrayArray1003 = {
		{
			6798, 107, 10283, 16, 4797, 7744, 5799, 4634, 33697, 22433, 
			2983, 54193
		}, {
			8741, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153, 
			56621, 4783, 1341, 16578, 35003, 25239
		}, {
			25238, 8742, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 
			10153, 56621, 4783, 1341, 16578, 35003
		}, {
			4626, 11146, 6439, 12, 4758, 10270
		}, {
			4550, 4537, 5681, 5673, 5790, 6806, 8076, 4574
		}
	};
	private String amountOrNameInput;
	private static int anInt1005;
	private int daysSinceLastLogin;
	private int pktSize;
	private int pktType;
	private int anInt1009;
	private int anInt1010;
	private int anInt1011;
	private NodeList aClass19_1013;
	private int anInt1014;
	private int anInt1015;
	private int anInt1016;
	private boolean aBoolean1017;
	private int anInt1018;
	private static final int[] anIntArray1019;
	private int anInt1021;
	private int anInt1022;
	private int loadingStage;
	private Background scrollBar1;
	private Background scrollBar2;
	private int anInt1026;
	private Background backBase1;
	private Background backBase2;
	private Background backHmid1;
	private final int[] anIntArray1030;
	private boolean aBoolean1031;
	private Sprite[] mapFunctions;
	private int baseX;
	private int baseY;
	private int anInt1036;
	private int anInt1037;
	private int loginFailures;
	private int anInt1039;
	private int anInt1040;
	private int anInt1041;
	private int dialogID;
	private final int[] maxStats;
	private final int[] anIntArray1045;
	private int anInt1046;
	private boolean aBoolean1047;
	private int anInt1048;
	private String aString1049;
	private static int anInt1051;
	private final int[] anIntArray1052;
	private StreamLoader titleStreamLoader;
	private int anInt1054;
	private int anInt1055;
	private NodeList aClass19_1056;
	private final int[] anIntArray1057;
	public final RSInterface aClass9_1059;
	private Background[] mapScenes;
	private static int anInt1061;
	private int anInt1062;
	private final int barFillColor;
	private int friendsListAction;
	private final int[] anIntArray1065;
	private int mouseInvInterfaceIndex;
	private int lastActiveInvInterface;
	public static OnDemandFetcher onDemandFetcher;
	private int anInt1069;
	private int anInt1070;
	private int anInt1071;
	private int[] anIntArray1072;
	private int[] anIntArray1073;
	private Sprite mapDotItem;
	private Sprite mapDotNPC;
	private Sprite mapDotPlayer;
	private Sprite mapDotFriend;
	private Sprite mapDotTeam;
	private int anInt1079;
	private boolean aBoolean1080;
	private String[] friendsList;
	private Stream inStream;
	private int anInt1084;
	private int anInt1085;
	private int activeInterfaceType;
	private int anInt1087;
	private int anInt1088;
	public static int anInt1089;
	private final int[] expectedCRCs;
	private int[] menuActionCmd2;
	private int[] menuActionCmd3;
	private int[] menuActionID;
	private int[] menuActionCmd1;
	private Sprite[] headIcons;
	private Sprite[] skullIcons;
	private Sprite[] headIconsHint;
	private static int anInt1097;
	private int anInt1098;
	private int anInt1099;
	private int anInt1100;
	private int anInt1101;
	private int anInt1102;
	public static boolean tabAreaAltered;
	private int anInt1104;
	private RSImageProducer aRSImageProducer_1107;
	private RSImageProducer aRSImageProducer_1108;
	private RSImageProducer aRSImageProducer_1109;
	private RSImageProducer aRSImageProducer_1110;
	private RSImageProducer aRSImageProducer_1111;
	private RSImageProducer aRSImageProducer_1112;
	private RSImageProducer aRSImageProducer_1113;
	private RSImageProducer aRSImageProducer_1114;
	private RSImageProducer aRSImageProducer_1115;
	private static int anInt1117;
	private int membersInt;
	private String aString1121;
	private Sprite compass;
	private RSImageProducer aRSImageProducer_1123;
	private RSImageProducer aRSImageProducer_1124;
	private RSImageProducer aRSImageProducer_1125;
	public static Player myPlayer;
	private final String[] atPlayerActions;
	private final boolean[] atPlayerArray;
	private final int[][][] anIntArrayArrayArray1129;
	public final static int[] tabInterfaceIDs = {
		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
		-1, -1, -1, -1
	};
	private int anInt1131;
	private int anInt1132;
	private int menuActionRow;
	private static int anInt1134;
	private int spellSelected;
	private int anInt1137;
	private int spellUsableOn;
	private String spellTooltip;
	private Sprite[] aClass30_Sub2_Sub1_Sub1Array1140;
	private boolean aBoolean1141;
	private static int anInt1142;
	private int energy;
	private boolean aBoolean1149;
	private Sprite[] crosses;
	private boolean musicEnabled;
	private Background[] aBackgroundArray1152s;
	public static boolean needDrawTabArea;
	private int unreadMessages;
	private static int anInt1155;
	private static boolean fpsOn;
	public boolean loggedIn;
	private boolean canMute;
	private boolean aBoolean1159;
	private boolean aBoolean1160;
	static int loopCycle;
	private static final String validUserPassChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"\243$%^&*()-_=+[{]};:'@#~,<.>/?\\| ";
	private RSImageProducer aRSImageProducer_1163;
	private RSImageProducer aRSImageProducer_1164;
	private RSImageProducer aRSImageProducer_1165;
	private RSImageProducer aRSImageProducer_1166;
	private int daysSinceRecovChange;
	private RSSocket socketStream;
	private int anInt1169;
	private int minimapInt3;
	private int anInt1171;
	private long aLong1172;
	private String myUsername;
	private String myPassword;
	private static int anInt1175;
	private boolean genericLoadingError;
	private final int[] anIntArray1177 = {
		0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 
		2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
		2, 2, 3
	};
	private int reportAbuseInterfaceID;
	private NodeList aClass19_1179;
	private int[] anIntArray1180;
	private int[] anIntArray1181;
	private int[] anIntArray1182;
	private byte[][] aByteArrayArray1183;
	private int anInt1184;
	private int minimapInt1;
	private int anInt1186;
	private int anInt1187;
	private static int anInt1188;
	private int invOverlayInterfaceID;
	private int[] anIntArray1190;
	private int[] anIntArray1191;
	private Stream stream;
	private int anInt1193;
	private int splitPrivateChat;
	private Background mapBack;
	private String[] menuActionName;
	private Sprite aClass30_Sub2_Sub1_Sub1_1201;
	private Sprite aClass30_Sub2_Sub1_Sub1_1202;
	private final int[] anIntArray1203;
	static final int[] anIntArray1204 = {
		9104, 10275, 7595, 3610, 7975, 8526, 918, 38802, 24466, 10145, 
		58654, 5027, 1457, 16565, 34991, 25486
	};
	private static boolean flagged;
	private final int[] anIntArray1207;
	private int anInt1208;
	private int minimapInt2;
	private int anInt1210;
	public static int anInt1211;
	private String promptInput;
	private int anInt1213;
	private int[][][] intGroundArray;
	private long aLong1215;
	private int loginScreenCursorPos;
	private final Background[] modIcons;
	private long aLong1220;
	public static int tabID;
	private int anInt1222;
	public static boolean inputTaken;
	private int inputDialogState;
	private static int anInt1226;
	private int nextSong;
	private boolean songChanging;
	private int musicVolume = 200;
	private final int[] anIntArray1229;
	private Class11[] aClass11Array1230;
	public static int anIntArray1232[];
	private boolean aBoolean1233;
	private int[] anIntArray1234;
	private int[] anIntArray1235;
	private int[] anIntArray1236;
	private int anInt1237;
	private int anInt1238;
	public final int anInt1239 = 100;
	private final int[] anIntArray1240;
	private final int[] anIntArray1241;
	private boolean aBoolean1242;
	private int atInventoryLoopCycle;
	private int atInventoryInterface;
	private int atInventoryIndex;
	private int atInventoryInterfaceType;
	private byte[][] aByteArrayArray1247;
	private int tradeMode;
	private int anInt1249;
	private final int[] anIntArray1250;
	private int anInt1251;
	private final boolean rsAlreadyLoaded;
	private int anInt1253;
	private int anInt1254;
	private boolean welcomeScreenRaised;
	private boolean messagePromptRaised;
	private int anInt1257;
	private byte[][][] byteGroundArray;
	private int prevSong;
	private int destX;
	private int destY;
	private Sprite aClass30_Sub2_Sub1_Sub1_1263;
	private int anInt1264;
	private int anInt1265;
	private String loginMessage1;
	private String loginMessage2;
	private int anInt1268;
	private int anInt1269;
	private TextDrawingArea smallText;
	private TextDrawingArea aTextDrawingArea_1271;
	private TextDrawingArea chatTextDrawingArea;
	private int anInt1275;
	private int backDialogID;
	private int anInt1278;
	private int anInt1279;
	private int[] bigX;
	private int[] bigY;
	private int itemSelected;
	private int anInt1283;
	private int anInt1284;
	private int anInt1285;
	private String selectedItemName;
	private int publicChatMode;
	private static int anInt1288;
	private int anInt1289;
	public static int anInt1290;
	public static String server = getHost();

	static 
	{
		anIntArray1019 = new int[99];
		int i = 0;
		for(int j = 0; j < 99; j++)
		{
			int l = j + 1;
			int i1 = (int)((double)l + 300D * Math.pow(2D, (double)l / 7D));
			i += i1;
			anIntArray1019[j] = i / 4;
		}

		anIntArray1232 = new int[32];
		i = 2;
		for(int k = 0; k < 32; k++)
		{
			anIntArray1232[k] = i - 1;
			i += i;
		}

	}
}
