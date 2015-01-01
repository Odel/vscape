import java.applet.AppletContext;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import java.util.zip.CRC32;

@SuppressWarnings("serial")
public class Client extends RSApplet {
	
	private final static String CLIENT_VERSION = "3.3";
	
	public final static boolean DevMode = true;
	public final static boolean MusicEnabled = true;
	
	public static int REGULAR_WIDTH = 765, REGULAR_HEIGHT = 503;
	
	public void checkSize() {
		if (clientSize == 1) {
			if (clientWidth != (isWebclient() ? getGameComponent().getWidth() : gameFrame.getFrameWidth())) {
				clientWidth = (isWebclient() ? getGameComponent().getWidth() : gameFrame.getFrameWidth());
				gameAreaWidth = clientWidth;
				updateGameArea();
			}
			if (clientHeight != (isWebclient() ? getGameComponent().getHeight() : gameFrame.getFrameHeight())) {
				clientHeight = (isWebclient() ? getGameComponent().getHeight() : gameFrame.getFrameHeight());
				gameAreaHeight = clientHeight;
				updateGameArea();
			}
		}
	}

	private void updateGameArea() {
		Texture.method365(clientSize == 0 ? REGULAR_WIDTH : clientWidth, clientSize == 0 ? REGULAR_HEIGHT : clientHeight);
		fullScreenTextureArray = Texture.anIntArray1472;
		Texture.method365(clientSize == 0 ? 516 : clientWidth, clientSize == 0 ? 165 : clientHeight);
		anIntArray1180 = Texture.anIntArray1472;
		Texture.method365(clientSize == 0 ? (aRSImageProducer_1163 != null ? aRSImageProducer_1163.anInt316 : 250) : clientWidth,clientSize == 0 ? (aRSImageProducer_1163 != null ? aRSImageProducer_1163.anInt317 : 335) : clientHeight);
		anIntArray1181 = Texture.anIntArray1472;
		Texture.method365(clientSize == 0 ? 512 : clientWidth, clientSize == 0 ? 334 : clientHeight);
		anIntArray1182 = Texture.anIntArray1472;
		
		int ai[] = new int[9];
		for (int i8 = 0; i8 < 9; i8++) {
			int k8 = 128 + i8 * 32 + 15;
			int l8 = 600 + k8 * 3;
			int i9 = Texture.anIntArray1470[k8];
			ai[i8] = l8 * i9 >> 16;
		}
		
		WorldController.method310(500, 800, clientSize == 0 ? 512 : clientWidth, clientSize == 0 ? 334 : clientHeight, ai);

		aRSImageProducer_1165 = new RSImageProducer(clientSize == 0 ? 512 : clientWidth, clientSize == 0 ? 334 : clientHeight, getGameComponent());
		DrawingArea.setAllPixelsToZero();
		resetImageProducers2();

		if (!loggedIn)
			resetAllImageProducers();
	}

	public void toggleSize(int size) {
		if (clientSize != size) {
			clientSize = size;
			int width = 765;
			int height = 503;
			if (size == 0) {
				log_view_dist = 9;
				size = 0;
				width = REGULAR_WIDTH;
				height = REGULAR_HEIGHT;
				showChat = true;
				showTab = true;
			} else if (size == 1) {
				log_view_dist = 10;
				size = 1;
				width = isWebclient() ? appletWidth : resizableWidth;
				height = isWebclient() ? appletHeight : resizableHeight;
			} else if (size == 2) {
				log_view_dist = 10;
				size = 2;
				width = getMaxWidth();
				height = getMaxHeight();
			}
			rebuildFrame(size, width, height);
			updateGameArea();
		}
	}


	public void rebuildFrame(int size, int width, int height) {
		try {
			gameAreaWidth = (size == 0) ? 512 : width;
			gameAreaHeight = (size == 0) ? 334 : height;
			clientWidth = width;
			clientHeight = height;
			instance.rebuildFrame(clientSize == 2, width, height, clientSize == 1, clientSize >= 1);
			updateGameArea();
			super.mouseX = super.mouseY = -1;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void drawUnfixedGame() {
		if (clientSize != 0) {
			drawChatArea();
			drawTabArea();
			drawMinimap();
		}
	}

	public boolean isFixed() {
		return clientSize == 0;
	}

	public boolean canClick() {
		if (mouseInRegion(clientWidth - (clientWidth <= 1000 ? 240 : 480), clientHeight - (clientWidth <= 1000 ? 90 : 37), clientWidth, clientHeight)) {
			return false;
		}
		if (showChat) {
			if (super.mouseX > 0 && super.mouseX < 519 && super.mouseY > clientHeight - 165 && super.mouseY < clientHeight || super.mouseX > clientWidth - 220 && super.mouseX < clientWidth && super.mouseY > 0 && super.mouseY < 165) {
				return false;
			}
		}
		if (mouseInRegion2(clientWidth - 216, clientWidth, 0, 172)) {
			return false;
		}
		if (showTab) {
			if (clientWidth > 1000) {
				if (super.mouseX >= clientWidth - 420 && super.mouseX <= clientWidth && super.mouseY >= clientHeight - 37 && super.mouseY <= clientHeight || super.mouseX > clientWidth - 225 && super.mouseX < clientWidth && super.mouseY > clientHeight - 37 - 274 && super.mouseY < clientHeight)
					return false;
			} else {
				if (super.mouseX >= clientWidth - 210 && super.mouseX <= clientWidth && super.mouseY >= clientHeight - 74 && super.mouseY <= clientHeight || super.mouseX > clientWidth - 225 && super.mouseX < clientWidth && super.mouseY > clientHeight - 74 - 274 && super.mouseY < clientHeight)
					return false;
			}
		}
		return true;
	}

	public boolean mouseInRegion(int x1, int y1, int x2, int y2) {
		if (super.mouseX >= x1 && super.mouseX <= x2 && super.mouseY >= y1 && super.mouseY <= y2)
			return true;
		return false;
	}

	public boolean mouseInRegion2(int x1, int x2, int y1, int y2) {
		if (super.mouseX >= x1 && super.mouseX <= x2 && super.mouseY >= y1 && super.mouseY <= y2) {
			return true;
		}
		return false;
	}

	public boolean clickInRegion(int x1, int y1, int x2, int y2) {
		if (super.saveClickX >= x1 && super.saveClickX <= x2 && super.saveClickY >= y1 && super.saveClickY <= y2)
			return true;
		return false;
	}

	public boolean canClickMap() {
		if (super.mouseX >= clientWidth - 21 && super.mouseX <= clientWidth && super.mouseY >= 0 && super.mouseY <= 21)
			return false;
		return true;
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

	private static String intToKOrMilLongName(int i) {
		String s = String.valueOf(i);
		for (int k = s.length() - 3; k > 0; k -= 3)
			s = s.substring(0, k) + "," + s.substring(k);
		if (s.length() > 8)
			s = "@gre@" + s.substring(0, s.length() - 8) + " million @whi@(" + s + ")";
		else if (s.length() > 4)
			s = "@cya@" + s.substring(0, s.length() - 4) + "K @whi@(" + s + ")";
		return " " + s;
	}

	public final String methodR(int j) {
		if (j >= 0 && j < 10000)
			return String.valueOf(j);
		if (j >= 10000 && j < 10000000)
			return j / 1000 + "K";
		if (j >= 10000000 && j < 999999999)
			return j / 1000000 + "M";
		if (j >= 999999999)
			return "*";
		else
			return "?";
	}

	public static final byte[] ReadFile(String s) {
		try {
			byte abyte0[];
			File file = new File(s);
			int i = (int) file.length();
			abyte0 = new byte[i];
			DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(new FileInputStream(s)));
			datainputstream.readFully(abyte0, 0, i);
			datainputstream.close();
			return abyte0;
		} catch (Exception e) {
			System.out.println((new StringBuilder()).append("Read Error: ").append(s).toString());
			return null;
		}
	}

	/*private void stopMidi() {
		Signlink.midifade = 0;
		Signlink.midi = "stop";
	}*/

	private boolean menuHasAddFriend(int j) {
		if (j < 0)
			return false;
		int k = menuActionID[j];
		if (k >= 2000)
			k -= 2000;
		return k == 337;
	}

	public void drawChannelButtons() {
		int y = 0;
		if(clientSize != 0) {
			y = clientHeight - 164;
		}
		String gameText[] = { "On", "Off" };
		int gameTextColor[] = { 65280, 0xff0000 };
		String globalText[] = { "On", "Hidden", "Off" };
		int globalTextColor[] = { 65280, 65535, 0xff0000 };
		String text[] = { "On", "Friends", "Off", "Hide" };
		int textColor[] = { 65280, 0xffff00, 0xff0000, 65535 };
		switch(cButtonCPos) {
		case 0:
			cacheSprite[1].drawSprite(5, y + 142);
			break;
		case 1:
			cacheSprite[1].drawSprite(71, y + 142);
			break;
		case 2:
			cacheSprite[1].drawSprite(137, y + 142);
			break;
		case 3:
			cacheSprite[1].drawSprite(203, y + 142);
			break;
		case 4:
			cacheSprite[1].drawSprite(269, y + 142);
			break;
		case 5:
			cacheSprite[1].drawSprite(335, y + 142);
			break;
		case 6:
			cacheSprite[1].drawSprite(401, y + 142);
			break;
		}
		if(cButtonHPos == cButtonCPos) {
			switch(cButtonHPos) {
			case 0:
				cacheSprite[2].drawSprite(5, y + 142);
				break;
			case 1:
				cacheSprite[2].drawSprite(71, y + 142);
				break;
			case 2:
				cacheSprite[2].drawSprite(137, y + 142);
				break;
			case 3:
				cacheSprite[2].drawSprite(203, y + 142);
				break;
			case 4:
				cacheSprite[2].drawSprite(269, y + 142);
				break;
			case 5:
				cacheSprite[2].drawSprite(335, y + 142);
				break;
			case 6:
				cacheSprite[2].drawSprite(401, y + 142);
				break;
			}
		} else {
			switch(cButtonHPos) {
			case 0:
				cacheSprite[0].drawSprite(5, y + 142);
				break;
			case 1:
				cacheSprite[0].drawSprite(71, y + 142);
				break;
			case 2:
				cacheSprite[0].drawSprite(137, y + 142);
				break;
			case 3:
				cacheSprite[0].drawSprite(203, y + 142);
				break;
			case 4:
				cacheSprite[0].drawSprite(269, y + 142);
				break;
			case 5:
				cacheSprite[0].drawSprite(335, y + 142);
				break;
			case 6:
				cacheSprite[0].drawSprite(401, y + 142);
				break;
			}
		}
		smallText.method389(true, 26, 0xffffff, "All", clientSize == 0 ? 157 : clientHeight - 8);
		smallText.method389(true, 86, 0xffffff, "Game", clientSize == 0 ? 152 : clientHeight - 12);
		smallText.method389(true, 150, 0xffffff, "Public", clientSize == 0 ? 152 : clientHeight - 12);
		smallText.method389(true, 212, 0xffffff, "Private", clientSize == 0 ? 152 : clientHeight - 12);
		smallText.method389(true, 286, 0xffffff, "Duel", clientSize == 0 ? 152 : clientHeight - 12);
		smallText.method389(true, 349, 0xffffff, "Trade", clientSize == 0 ? 152 : clientHeight - 12);
		smallText.method389(true, 411, 0xffffff, "Global", clientSize == 0 ? 152 : clientHeight - 12);
		smallText.method382(gameTextColor[gameMode], 100, gameText[gameMode], clientSize == 0 ? 163 : clientHeight - 1, true);
		smallText.method382(textColor[publicChatMode], 164, text[publicChatMode], clientSize == 0 ? 163 : clientHeight - 1, true);
		smallText.method382(textColor[privateChatMode], 230, text[privateChatMode], clientSize == 0 ? 163 : clientHeight - 1, true);
		smallText.method382(textColor[duelMode], 296, text[duelMode], clientSize == 0 ? 163 : clientHeight - 1, true);
		smallText.method382(textColor[tradeMode], 362, text[tradeMode], clientSize == 0 ? 163 : clientHeight - 1, true);
		smallText.method382(globalTextColor[globalMode], 428, globalText[globalMode], clientSize == 0 ? 163 : clientHeight - 1, true);
	}

	private void drawChatArea() {
		int y = 0;
		Texture.anIntArray1472 = anIntArray1180;
		if (clientSize == 0) 
			aRSImageProducer_1166.initDrawingArea();
		
		if (clientSize == 0){
			cacheSprite[25].drawSprite(0, y);
			mascotChat.draw24BitSprite(7, y+6);
		}
		
		if (clientSize != 0)
			y = clientHeight - 165;
		 
		if (clientSize != 0 && showChat) {
			cacheSprite[30].drawSprite(0, y - 1);
			DrawingArea.method335(0xccbb9a, y + 6, 506, 129, 200, 7);
			mascotChat.draw24BitSprite(7, y+6);
		}
		
		if(clientSize != 0) {
			cacheSprite[31].drawSprite(0, clientHeight - 22);
		}
		
		if (clientSize == 0) {
			aRSImageProducer_1166.initDrawingArea();
		}
		
		drawChannelButtons();
		TextDrawingArea textDrawingArea = aTextDrawingArea_1271;
		if (messagePromptRaised) {
			chatTextDrawingArea.drawText(0, aString1121, y + 60, 259);
			chatTextDrawingArea.drawText(128, promptInput + "*", y + 80, 259);
		} else if (inputDialogState == 1) {
			chatTextDrawingArea.drawText(0, "Enter amount:", y + 60, 259);
			chatTextDrawingArea.drawText(128, amountOrNameInput + "*", y + 80,
					259);
		} else if (inputDialogState == 2) {
			chatTextDrawingArea.drawText(0, "Enter name:", y + 60, 259);
			chatTextDrawingArea.drawText(128, amountOrNameInput + "*", y + 80,
					259);
		} else if (aString844 != null) {
			chatTextDrawingArea.drawText(0, aString844, y + 60, 259);
			chatTextDrawingArea.drawText(128, "Click to continue", y + 80, 259);
		} else if (backDialogID != -1) {
			drawInterface(0, 20, RSInterface.interfaceCache[backDialogID],
					y + 20);
		} else if (dialogID != -1) {
			drawInterface(0, 20, RSInterface.interfaceCache[dialogID], y + 20);
		} else if (showChat || clientSize == 0) {
			//int j77 = -3;
			int amountOfMessages = 0;
			DrawingArea.setDrawingArea(y + 122, 8, 497, y + 7);
			for (int k = 0; k < 500; k++)
				if (chatMessages[k] != null) {
					int chatType = chatTypes[k];
					int yPos = (114 - amountOfMessages * 14) + 3 + chatScrollPos;
					if (clientSize != 0)
						yPos += y;
					String s1 = chatNames[k];
					byte byte0 = 0;
					if (s1 != null && s1.startsWith("@cr1@")) {
						s1 = s1.substring(5);
						byte0 = 1;
					} else if (s1 != null && s1.startsWith("@cr2@")) {
						s1 = s1.substring(5);
						byte0 = 2;
					} else if (s1 != null && s1.startsWith("@cr3@")) {
						s1 = s1.substring(5);
						byte0 = 3;
					}
					if (chatType == 0) {
						if ((chatTypeView == 5 || chatTypeView == 0) && gameMode == 0) {
							if (yPos > y && yPos < y + 210)
								if (clientSize != 0)
									textDrawingArea.method389(false, 11, 0,
											chatMessages[k], yPos);// chat
							// color
							// enabled
								else
									textDrawingArea.method389(false, 11, 0,
											chatMessages[k], yPos);
							amountOfMessages++;
							//j77++;
						}
					}
					if ((chatType == 1 || chatType == 2)
							&& (chatType == 1 || publicChatMode == 0 || publicChatMode == 1
							&& isFriendOrSelf(s1))) {
						if (chatTypeView == 1 || chatTypeView == 0) {
							if (yPos > y && yPos < y + 210) {
								int xPos = 11;
								if (byte0 == 1) {
									modIcons[0].drawBackground(xPos + 1,
											yPos - 12);
									xPos += 14;
								} else if (byte0 == 2) {
									modIcons[1].drawBackground(xPos + 1,
											yPos - 12);
									xPos += 14;
								} else if (byte0 == 3) {
									modIcons[2].drawBackground(xPos + 1,
											yPos - 12);
									xPos += 14;
								}
								textDrawingArea.method385(0, s1 + ":", yPos,
										xPos);
								xPos += textDrawingArea.getTextWidth(s1) + 8;
								textDrawingArea.method389(false, xPos, 255,
										chatMessages[k], yPos);

							}
							amountOfMessages++;
							//j77++;
						}
					}
					if ((chatType == 3 || chatType == 7)
							&& (splitPrivateChat == 0 || chatTypeView == 2)
							&& (chatType == 7 || privateChatMode == 0 || privateChatMode == 1
							&& isFriendOrSelf(s1))) {
						if (chatTypeView == 2 || chatTypeView == 0) {
							if (yPos > y && yPos < y + 210) {
								int k1 = 11;
								if (clientSize == 0)
									textDrawingArea.method385(0, "From", yPos,
											k1);
								else
									textDrawingArea.method389(true, k1,
											0xff5256, "From", yPos);
								k1 += textDrawingArea.getTextWidth("From ");
								if (byte0 == 1) {
									modIcons[0].drawBackground(k1, yPos - 12);
									k1 += 12;
								} else if (byte0 == 2) {
									modIcons[1].drawBackground(k1, yPos - 12);
									k1 += 12;
								} else if (byte0 == 3) {
									modIcons[2].drawBackground(k1, yPos - 12);
									k1 += 12;
								}
								if (clientSize != 0) {
									textDrawingArea.method389(true, k1,
											0xff5256, s1 + ":", yPos);
									k1 += textDrawingArea.getTextWidth(s1) + 8;
									textDrawingArea.method389(true, k1,
											0xff5256, chatMessages[k], yPos);
								} else {
									textDrawingArea.method385(0, s1 + ":",
											yPos, k1);
									k1 += textDrawingArea.getTextWidth(s1) + 8;
									textDrawingArea.method385(0x800000,
											chatMessages[k], yPos, k1);
								}
							}
							amountOfMessages++;
							//j77++;
						}
					}
					if (chatType == 4
							&& (tradeMode == 0 || tradeMode == 1
							&& isFriendOrSelf(s1))) {
						if (chatTypeView == 3 || chatTypeView == 0) {
							if (yPos > y && yPos < y + 210)
								if (clientSize != 0)
									textDrawingArea.method389(true, 11,
											0xff5256, s1 + " "
													+ chatMessages[k], yPos);
								else
									textDrawingArea.method385(0x800080, s1
											+ " " + chatMessages[k], yPos, 11);
							amountOfMessages++;
							//j77++;
						}
					}
					if (chatType == 5 && splitPrivateChat == 0
							&& privateChatMode < 2) {
						if (chatTypeView == 2 || chatTypeView == 0) {
							if (yPos > y && yPos < y + 210)
								if (clientSize != 0)
									textDrawingArea.method389(true, 11,
											0xff5256, chatMessages[k], yPos);
								else
									textDrawingArea.method385(0x800000,
											chatMessages[k], yPos, 11);
							amountOfMessages++;
							//j77++;
						}
					}
					if (chatType == 6
							&& (splitPrivateChat == 0 || chatTypeView == 2)
							&& privateChatMode < 2) {
						if (chatTypeView == 2 || chatTypeView == 0) {
							if (yPos > y && yPos < y + 210) {
								if (clientSize != 0) {
									textDrawingArea.method389(true, 11,
											0xff5256, "To " + s1 + ":", yPos);
									textDrawingArea.method389(true,
											15 + textDrawingArea
											.getTextWidth("To :" + s1),
											0xff5256, chatMessages[k], yPos);
								} else {
									textDrawingArea.method385(0, "To " + s1
											+ ":", yPos, 11);
									textDrawingArea.method385(0x800000,
											chatMessages[k], yPos,
											15 + textDrawingArea
											.getTextWidth("To :" + s1));
								}
							}
							amountOfMessages++;
							//j77++;
						}
					}
					if (chatType == 8
							&& (duelMode == 0 || duelMode == 1
							&& isFriendOrSelf(s1))) {
						if (chatTypeView == 4 || chatTypeView == 0) {
							if (yPos > y && yPos < y + 210)
								if (clientSize != 0)
									textDrawingArea.method389(true, 11,
											0x7e3200, s1 + " "
													+ chatMessages[k], yPos);
								else
									textDrawingArea.method385(0x7e3200, s1
											+ " " + chatMessages[k], yPos, 11);
							amountOfMessages++;
							//j77++;
						}
						if (chatType == 11 && (clanChatMode == 0)) {
							if (chatTypeView == 11) {
								if (yPos > y && yPos < y + 210)
									if (clientSize != 0)
										textDrawingArea.method389(true,11,0x7e3200,s1+ " "+ chatMessages[k],yPos);
									else
										textDrawingArea.method385(0x7e3200, s1+ " " + chatMessages[k], yPos,11);
								amountOfMessages++;
								//j77++;
							}
							if (chatType == 12) {
								if (yPos > y && yPos < y + 210)
									if (clientSize != 0)
										textDrawingArea.method389(true, 11,0x7e3200, chatMessages[k]+ " @blu@" + s1, yPos);
									else
										textDrawingArea.method385(0x7e3200,chatMessages[k]+ " @blu@" + s1,yPos, 11);
								amountOfMessages++;
							}
						}
					}
					
					// CHANGE TO PACKET (CASE 216)
					if (chatType == 9 && (globalMode == 0 || globalMode == 1)) {
						if (chatTypeView == 9  || (chatTypeView == 0 && globalMode == 0)) {
							int xPos = 8;
							textDrawingArea.method385(255, globalPrefix, yPos, xPos);
							xPos +=  textDrawingArea.getTextWidth(globalPrefix);
							if (byte0 == 1) {
								modIcons[0].drawBackground(xPos + 1,
										yPos - 12);
								xPos += 14;
							} else if (byte0 == 2) {
								modIcons[1].drawBackground(xPos + 1,
										yPos - 12);
								xPos += 14;
							} else if (byte0 == 3) {
								modIcons[2].drawBackground(xPos + 1,
										yPos - 12);
								xPos += 14;
							}
							textDrawingArea.method385(0, s1 + ":", yPos,
									xPos);
							xPos += textDrawingArea.getTextWidth(s1) + 8;
							textDrawingArea.method389(false, xPos, 0x7e3200,
									chatMessages[k], yPos);
								amountOfMessages++;
						}
					}
					
					/*if (chatType == 16) {
						int j2 = 40;
						int clanNameWidth = textDrawingArea.getTextWidth(clanname);
						if (chatTypeView == 11 || chatTypeView == 0) {
							if (yPos > y && yPos < y + 210)
								switch (chatRights[k]) {
								case 1:
									j2 += clanNameWidth;
									modIcons[0].drawBackground(j2 - 18,yPos - 12);
									j2 += 14;
									break;

								case 2:
									j2 += clanNameWidth;
									modIcons[1].drawBackground(j2 - 18,yPos - 12);
									j2 += 14;
									break;

								case 3:
									j2 += clanNameWidth;
									modIcons[1].drawBackground(j2 - 18,yPos - 12);
									j2 += 14;
									break;

								default:
									j2 += clanNameWidth;
									break;
								}
							textDrawingArea.method385(0, "[", yPos, 8);
							textDrawingArea.method385(255, "" + clanname + "",yPos, 14);
							textDrawingArea.method385(0, "]", yPos,clanNameWidth + 14);

							textDrawingArea.method385(0, chatNames[k] + ":",yPos, j2 - 17); // j2
							j2 += textDrawingArea.getTextWidth(chatNames[k]) + 7;
							textDrawingArea.method385(0x800000,chatMessages[k], yPos, j2 - 16);// j2
							amountOfMessages++;
							//j77++;
						}
					}*/
				}
			DrawingArea.defaultDrawingAreaSize();
			chatScrollMax = amountOfMessages * 14 + 7 + 5;
			if(chatScrollMax < 111)
				chatScrollMax = 111;
			drawScrollbar(114, chatScrollMax - chatScrollPos - 114, 7 + (clientSize == 0 ? 0 : clientHeight - 165), 496, chatScrollMax);
			String s;
			if(myPlayer != null && myPlayer.name != null)
				s = myPlayer.name;
			else
				s = TextClass.fixName(myUsername);
			if (clientSize != 0) {
				textDrawingArea.drawChatInput(0, 11, s + ":", y + 133, false);
				textDrawingArea.drawChatInput(255,
						12 + textDrawingArea.getTextWidth(s + ": "),
						inputString + "*", y + 133, false);
				for (int i = 0; i < 505; i++) {
					int opacity = 100 - (int) (i / 5.05);
					DrawingArea.method340(0, 1, y + 6, opacity, 7 + i); 
					DrawingArea.method340(0, 1, y + 121, opacity, 7 + i);
				}
			} else {
				textDrawingArea.method385(0, s + ":", y + 133, 11);
				textDrawingArea.drawChatInput(255,
						12 + textDrawingArea.getTextWidth(s + ": "),
						inputString + "*", y + 133, false);
				DrawingArea.method339(y + 121, 0x807660, 506, 7);
			}
		}
		if(menuOpen && clientSize == 0) {
			drawMenu(0, 338);
		} else if (menuOpen && menuScreenArea == 2 && clientSize != 0) {
			drawMenu(0 ,338);
		}
		if (clientSize == 0)
			aRSImageProducer_1166.drawGraphics(338, super.graphics, 0);
		aRSImageProducer_1165.initDrawingArea();
		Texture.anIntArray1472 = anIntArray1182;
	}
/*
	public void init() {
		try {
			nodeID = 10;
			portOff = 0;
			setHighMem();
			isMembers = true;
			Signlink.storeid = 32;
			Signlink.startpriv(InetAddress.getLocalHost());
			initClientFrame(503, 765);
			instance = this;
		} catch (Exception exception) {
			return;
		}
	}*/

	public void startRunnable(Runnable runnable, int i) {
		if (i > 10)
			i = 10;
		if (Signlink.mainapp != null) {
			Signlink.startthread(runnable, i);
		} else {
			super.startRunnable(runnable, i);
		}
	}

	public Socket openSocket(int port) throws IOException {
		return new Socket(InetAddress.getByName(server), port);
	}

	private void processMenuClick() {
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
				if(k < menuOffsetX - 10 || k > menuOffsetX + menuWidth + 10 || j1 < menuOffsetY - 10 || j1 > menuOffsetY + menuHeight + 10) {
					menuOpen = false;
					if(menuScreenArea == 1) {
					}
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
				switch (menuScreenArea) {
				case 0:
					k2 -= 4;
					l2 -= 4;
					break;
				case 1:
					k2 -= 519;
					l2 -= 168;
					break;
				case 2:
					k2 -= 5;
					l2 -= 338;
					break;
				case 3:
					k2 -= 519;
					l2 -= 0;
					break;
				}
				int i3 = -1;
				for(int j3 = 0; j3 < menuActionRow; j3++) {
					int k3 = k1 + 31 + (menuActionRow - 1 - j3) * 15;
					if(k2 > l && k2 < l + i2 && l2 > k3 - 13 && l2 < k3 + 3)
						i3 = j3;
				}
				if(i3 != -1)
					doAction(i3);
				menuOpen = false;
				if(menuScreenArea == 1) {
				}
				if(menuScreenArea == 2) {
					inputTaken = true;
				}
			}
		} else {
			if(j == 1 && menuActionRow > 0) {
				int i1 = menuActionID[menuActionRow - 1];
				if(i1 == 632 || i1 == 78 || i1 == 867 || i1 == 431 || i1 == 53 || i1 == 74 || i1 == 454 || i1 == 539 || i1 == 493 || i1 == 847 || i1 == 447 || i1 == 1125) {
					int l1 = menuActionCmd2[menuActionRow - 1];
					int j2 = menuActionCmd3[menuActionRow - 1];
					RSInterface class9 = RSInterface.interfaceCache[j2];
					if(class9.aBoolean259 || class9.aBoolean235) {
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

			processMainScreenClick();
			processTabClick();
			processChatModeClick();

		}
	}

	public static String getFileNameWithoutExtension(String fileName) {
		File tmpFile = new File(fileName);
		tmpFile.getName();
		int whereDot = tmpFile.getName().lastIndexOf('.');
		if (0 < whereDot && whereDot <= tmpFile.getName().length() - 2) {
			return tmpFile.getName().substring(0, whereDot);
		}
		return "";
	}
	
	public byte[] getModel(int index) {
		try {
			File model = new File(Signlink.findcachedir()+"pModels/" + index + ".gz");
			byte[] aByte = new byte[(int)model.length()];
			FileInputStream Fis = new FileInputStream(model);
			Fis.read(aByte);
			pushMessage("aByte = [" + aByte + "]!", 0, "");
			Fis.close();
			return aByte;
		} catch(Exception e) {
			return null;
		}
	}

	public void packModels() {
		for(int modelIndex = 0; modelIndex < 100000; modelIndex++) {
			byte[] abyte0 = getModel(modelIndex);
			if(abyte0 != null && abyte0.length > 0) {
				decompressors[1].method234(abyte0.length, abyte0, modelIndex);
			}
		}
	}
	
	public byte[] getMap(int index) {
		try {
			File map = new File(Signlink.findcachedir()+"pMaps/" + index + ".gz");
			byte[] aByte = new byte[(int)map.length()];
			FileInputStream Fis = new FileInputStream(map);
			Fis.read(aByte);
			pushMessage("aByte = [" + aByte + "]!", 0, "");
			Fis.close();
			return aByte;
		} catch(Exception e) {
			return null;
		}
	}

	public void packMaps() {
		for(int mapIndex = 0; mapIndex < 100000; mapIndex++) {
			byte[] abyte0 = getMap(mapIndex);
			if(abyte0 != null && abyte0.length > 0) {
				decompressors[4].method234(abyte0.length, abyte0, mapIndex);
			}
		}
	}


	private void method22() {
		try {
			anInt985 = -1;
			aClass19_1056.removeAll();
			aClass19_1013.removeAll();
			Texture.method366();
			unlinkMRUNodes();
			worldController.initToNull();
			System.gc();
			for (int i = 0; i < 4; i++)
				aClass11Array1230[i].method210();
			for (int l = 0; l < 4; l++) {
				for (int k1 = 0; k1 < 104; k1++) {
					for (int j2 = 0; j2 < 104; j2++)
						byteGroundArray[l][k1][j2] = 0;
				}
			}

			ObjectManager objectManager = new ObjectManager(byteGroundArray, intGroundArray);
			int k2 = aByteArrayArray1183.length;
			stream.createFrame(0);
			if (!aBoolean1159) {
				for (int i3 = 0; i3 < k2; i3++) {
					int i4 = (anIntArray1234[i3] >> 8) * 64 - baseX;
					int k5 = (anIntArray1234[i3] & 0xff) * 64 - baseY;
					byte abyte0[] = aByteArrayArray1183[i3];
					if (FileOperations.FileExists(Signlink.findcachedir() + "maps/" + anIntArray1235[i3] + ".dat"))
						abyte0 = FileOperations.ReadFile(Signlink.findcachedir() + "maps/" + anIntArray1235[i3] + ".dat");
					if (abyte0 != null)
						objectManager.method180(abyte0, k5, i4, (anInt1069 - 6) * 8, (anInt1070 - 6) * 8, aClass11Array1230);
				}
				for (int j4 = 0; j4 < k2; j4++) {
					int l5 = (anIntArray1234[j4] >> 8) * 64 - baseX;
					int k7 = (anIntArray1234[j4] & 0xff) * 64 - baseY;
					byte abyte2[] = aByteArrayArray1183[j4];
					if (abyte2 == null && anInt1070 < 800)
						objectManager.method174(k7, 64, 64, l5);
				}
				anInt1097++;
				if (anInt1097 > 160) {
					anInt1097 = 0;
					stream.createFrame(238);
					stream.writeWordBigEndian(96);
				}
				stream.createFrame(0);
				for (int i6 = 0; i6 < k2; i6++) {
					byte abyte1[] = aByteArrayArray1247[i6];
					if (abyte1 != null) {
						int l8 = (anIntArray1234[i6] >> 8) * 64 - baseX;
						int k9 = (anIntArray1234[i6] & 0xff) * 64 - baseY;
						objectManager.method190(l8, aClass11Array1230, k9, worldController, abyte1);
					}
				}

			}
			if (aBoolean1159) {
				for (int j3 = 0; j3 < 4; j3++) {
					for (int k4 = 0; k4 < 13; k4++) {
						for (int j6 = 0; j6 < 13; j6++) {
							int l7 = anIntArrayArrayArray1129[j3][k4][j6];
							if (l7 != -1) {
								int i9 = l7 >> 24 & 3;
						int l9 = l7 >> 1 & 3;
				int j10 = l7 >> 14 & 0x3ff;
		int l10 = l7 >> 3 & 0x7ff;
		int j11 = (j10 / 8 << 8) + l10 / 8;
		for (int l11 = 0; l11 < anIntArray1234.length; l11++) {
			if (anIntArray1234[l11] != j11 || aByteArrayArray1183[l11] == null)
				continue;
			objectManager.method179(i9, l9, aClass11Array1230, k4 * 8, (j10 & 7) * 8, aByteArrayArray1183[l11], (l10 & 7) * 8, j3, j6 * 8);
			break;
		}

							}
						}
					}
				}
				for (int l4 = 0; l4 < 13; l4++) {
					for (int k6 = 0; k6 < 13; k6++) {
						int i8 = anIntArrayArrayArray1129[0][l4][k6];
						if (i8 == -1)
							objectManager.method174(k6 * 8, 8, 8, l4 * 8);
					}
				}

				stream.createFrame(0);
				for (int l6 = 0; l6 < 4; l6++) {
					for (int j8 = 0; j8 < 13; j8++) {
						for (int j9 = 0; j9 < 13; j9++) {
							int i10 = anIntArrayArrayArray1129[l6][j8][j9];
							if (i10 != -1) {
								int k10 = i10 >> 24 & 3;
						int i11 = i10 >> 1 & 3;
				int k11 = i10 >> 14 & 0x3ff;
				int i12 = i10 >> 3 & 0x7ff;
				int j12 = (k11 / 8 << 8) + i12 / 8;
				for (int k12 = 0; k12 < anIntArray1234.length; k12++) {
					if (anIntArray1234[k12] != j12 || aByteArrayArray1247[k12] == null)
						continue;
					//if (FileOperations.FileExists(Signlink.findcachedir() + "maps/" + anIntArray1235[k12] + ".dat"))
					//	FileOperations.ReadFile(Signlink.findcachedir() + "maps/" + anIntArray1235[k12] + ".dat");
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
			if (k3 > plane)
				k3 = plane;
			if (k3 < plane - 1)
				k3 = plane - 1;
			if (lowMem)
				worldController.method275(ObjectManager.anInt145);
			else
				worldController.method275(0);
			for (int i5 = 0; i5 < 104; i5++) {
				for (int i7 = 0; i7 < 104; i7++)
					spawnGroundItem(i5, i7);

			}

			anInt1051++;
			if (anInt1051 > 98) {
				anInt1051 = 0;
				stream.createFrame(150);
			}
			method63();
		} catch (Exception exception) {
		}
		ObjectDef.mruNodes1.unlinkAll();
		if (super.gameFrame != null) {
			stream.createFrame(210);
			stream.writeDWord(0x3f008edd);
		}
		if (lowMem && Signlink.cache_dat != null) {
			int j = onDemandFetcher.getVersionCount(0);
			for (int i1 = 0; i1 < j; i1++) {
				int l1 = onDemandFetcher.getModelIndex(i1);
				if ((l1 & 0x79) == 0)
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
		if (aBoolean1141) {
			k = 49;
			j1 = 50;
			i2 = 49;
			l2 = 50;
		}
		for (int l3 = k; l3 <= j1; l3++) {
			for (int j5 = i2; j5 <= l2; j5++)
				if (l3 == k || l3 == j1 || j5 == i2 || j5 == l2) {
					int j7 = onDemandFetcher.method562(0, j5, l3);
					if (j7 != -1)
						onDemandFetcher.method560(j7, 3);
					int k8 = onDemandFetcher.method562(1, j5, l3);
					if (k8 != -1)
						onDemandFetcher.method560(k8, 3);
				}

		}

	}

	private void unlinkMRUNodes() {
		ObjectDef.mruNodes1.unlinkAll();
		ObjectDef.mruNodes2.unlinkAll();
		EntityDef.mruNodes.unlinkAll();
		ItemDef.mruNodes2.unlinkAll();
		ItemDef.mruNodes1.unlinkAll();
		Player.mruNodes.unlinkAll();
		SpotAnim.aMRUNodes_415.unlinkAll();
	}

	private void method24(int i) {
		int ai[] = minimapImage.myPixels;
		int j = ai.length;
		for (int k = 0; k < j; k++)
			ai[k] = 0;

		for (int l = 1; l < 103; l++) {
			int i1 = 24628 + (103 - l) * 512 * 4;
			for (int k1 = 1; k1 < 103; k1++) {
				if ((byteGroundArray[i][k1][l] & 0x18) == 0)
					worldController.method309(ai, i1, i, k1, l);
				if (i < 3 && (byteGroundArray[i + 1][k1][l] & 8) != 0)
					worldController.method309(ai, i1, i + 1, k1, l);
				i1 += 4;
			}

		}

		int j1 = ((238 + (int) (Math.random() * 20D)) - 10 << 16) + ((238 + (int) (Math.random() * 20D)) - 10 << 8) + ((238 + (int) (Math.random() * 20D)) - 10);
		int l1 = (238 + (int) (Math.random() * 20D)) - 10 << 16;
		minimapImage.method343();
		for (int i2 = 1; i2 < 103; i2++) {
			for (int j2 = 1; j2 < 103; j2++) {
				if ((byteGroundArray[i][j2][i2] & 0x18) == 0)
					method50(i2, j1, j2, l1, i);
				if (i < 3 && (byteGroundArray[i + 1][j2][i2] & 8) != 0)
					method50(i2, j1, j2, l1, i + 1);
			}

		}

		aRSImageProducer_1165.initDrawingArea();
		anInt1071 = 0;
		for (int k2 = 0; k2 < 104; k2++) {
			for (int l2 = 0; l2 < 104; l2++) {
				int i3 = worldController.method303(plane, k2, l2);
				if (i3 != 0) {
					i3 = i3 >> 14 & 0x7fff;
			int j3 = ObjectDef.forID(i3).anInt746;
			if (j3 >= 0) {
				int k3 = k2;
				int l3 = l2;
				if (j3 != 22 && j3 != 29 && j3 != 34 && j3 != 36 && j3 != 46 && j3 != 47 && j3 != 48) {
					byte byte0 = 104;
					byte byte1 = 104;
					int ai1[][] = aClass11Array1230[plane].anIntArrayArray294;
					for (int i4 = 0; i4 < 10; i4++) {
						int j4 = (int) (Math.random() * 4D);
						if (j4 == 0 && k3 > 0 && k3 > k2 - 3 && (ai1[k3 - 1][l3] & 0x1280108) == 0)
							k3--;
						if (j4 == 1 && k3 < byte0 - 1 && k3 < k2 + 3 && (ai1[k3 + 1][l3] & 0x1280180) == 0)
							k3++;
						if (j4 == 2 && l3 > 0 && l3 > l2 - 3 && (ai1[k3][l3 - 1] & 0x1280102) == 0)
							l3--;
						if (j4 == 3 && l3 < byte1 - 1 && l3 < l2 + 3 && (ai1[k3][l3 + 1] & 0x1280120) == 0)
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

	private void spawnGroundItem(int i, int j) {
		NodeList class19 = groundArray[plane][i][j];
		if (class19 == null) {
			worldController.method295(plane, i, j);
			return;
		}
		int k = 0xfa0a1f01;
		Object obj = null;
		for (Item item = (Item) class19.reverseGetFirst(); item != null; item = (Item) class19.reverseGetNext()) {
			ItemDef itemDef = ItemDef.forID(item.ID);
			int l = itemDef.value;
			if (itemDef.stackable)
				l *= item.anInt1559 + 1;
			// notifyItemSpawn(item, i + baseX, j + baseY);

			if (l > k) {
				k = l;
				obj = item;
			}
		}

		class19.insertTail(((Node) (obj)));
		Object obj1 = null;
		Object obj2 = null;
		for (Item class30_sub2_sub4_sub2_1 = (Item) class19.reverseGetFirst(); class30_sub2_sub4_sub2_1 != null; class30_sub2_sub4_sub2_1 = (Item) class19.reverseGetNext()) {
			if (class30_sub2_sub4_sub2_1.ID != ((Item) (obj)).ID && obj1 == null)
				obj1 = class30_sub2_sub4_sub2_1;
			if (class30_sub2_sub4_sub2_1.ID != ((Item) (obj)).ID && class30_sub2_sub4_sub2_1.ID != ((Item) (obj1)).ID && obj2 == null)
				obj2 = class30_sub2_sub4_sub2_1;
		}

		int i1 = i + (j << 7) + 0x60000000;
		worldController.method281(i, i1, ((Animable) (obj1)), method42(plane, j * 128 + 64, i * 128 + 64), ((Animable) (obj2)), ((Animable) (obj)), plane, j);
	}

	private void method26(boolean flag) {
		for (int j = 0; j < npcCount; j++) {
			NPC npc = npcArray[npcIndices[j]];
			int k = 0x20000000 + (npcIndices[j] << 14);
			if (npc == null || !npc.isVisible() || npc.desc.aBoolean93 != flag)
				continue;
			int l = npc.x >> 7;
			int i1 = npc.y >> 7;
		if (l < 0 || l >= 104 || i1 < 0 || i1 >= 104)
			continue;
		if (npc.anInt1540 == 1 && (npc.x & 0x7f) == 64 && (npc.y & 0x7f) == 64) {
			if (anIntArrayArray929[l][i1] == anInt1265)
				continue;
			anIntArrayArray929[l][i1] = anInt1265;
		}
		if (!npc.desc.hasActions)
			k += 0x80000000;
		worldController.method285(plane, npc.anInt1552, method42(plane, npc.y, npc.x), k, npc.y, (npc.anInt1540 - 1) * 64 + 60, npc.x, npc, npc.aBoolean1541);
		}
	}

	public void drawHoverBox(int xPos, int yPos, String text) {
		String[] results = text.split("\n");
		int height = (results.length * 16) + 6;
		int width;
		width = smallText.getTextWidth(results[0]) + 6;
		for (int i = 1; i < results.length; i++)
			if (width <= smallText.getTextWidth(results[i]) + 6)
				width = smallText.getTextWidth(results[i]) + 6;
		DrawingArea.drawPixels(height, yPos, xPos, 0xFFFFA0, width);
		DrawingArea.fillPixels(xPos, width, height, 0, yPos);
		yPos += 14;
		for (int i = 0; i < results.length; i++) {
			smallText.method389(false, xPos + 3, 0, results[i], yPos);
			yPos += 16;
		}
	}

	private void buildInterfaceMenu(int i, RSInterface class9, int k, int l, int i1, int j1) {
		if (class9 == null)
			class9 = RSInterface.interfaceCache[21356];
		if (class9.type != 0 || class9.children == null || class9.isMouseoverTriggered)
			return;
		if (k < i || i1 < l || k > i + class9.width || i1 > l + class9.height)
			return;
		int k1 = class9.children.length;
		for (int l1 = 0; l1 < k1; l1++) {
			int i2 = class9.childX[l1] + i;
			int j2 = (class9.childY[l1] + l) - j1;
			RSInterface class9_1 = RSInterface.interfaceCache[class9.children[l1]];
			i2 += class9_1.anInt263;
			j2 += class9_1.anInt265;
			if ((class9_1.hoverType >= 0 || class9_1.anInt216 != 0) && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height)
				if (class9_1.hoverType >= 0)
					anInt886 = class9_1.hoverType;
				else
					anInt886 = class9_1.id;
			if (class9_1.type == 8 && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height) {
				anInt1315 = class9_1.id;
			}
			if (class9_1.type == 0) {
				buildInterfaceMenu(i2, class9_1, k, j2, i1, class9_1.scrollPosition);
				if (class9_1.scrollMax > class9_1.height)
					moveScroller(i2 + class9_1.width, class9_1.height, k, i1, class9_1, j2, true, class9_1.scrollMax);
			} else {
				if (class9_1.atActionType == 1 && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height) {
					boolean flag = false;
					if (class9_1.contentType != 0)
						flag = buildFriendsListMenu(class9_1);
					if (!flag) {
						menuActionName[menuActionRow] = class9_1.tooltip;
						menuActionID[menuActionRow] = 315;
						menuActionCmd3[menuActionRow] = class9_1.id;
						menuActionRow++;
					}
				}
				if (class9_1.atActionType == 2 && spellSelected == 0 && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height) {
					String s = class9_1.selectedActionName;
					if (s.indexOf(" ") != -1)
						s = s.substring(0, s.indexOf(" "));
					if (class9_1.spellName.endsWith("Rush") || class9_1.spellName.endsWith("Burst") || class9_1.spellName.endsWith("Blitz") || class9_1.spellName.endsWith("Barrage") || class9_1.spellName.endsWith("strike") || class9_1.spellName.endsWith("bolt") || class9_1.spellName.equals("Crumble undead") || class9_1.spellName.endsWith("blast") || class9_1.spellName.endsWith("wave") || class9_1.spellName.equals("Claws of Guthix") || class9_1.spellName.equals("Flames of Zamorak") || class9_1.spellName.equals("Magic Dart")) {
						menuActionName[menuActionRow] = "Autocast @gre@" + class9_1.spellName;
						menuActionID[menuActionRow] = 104;
						menuActionCmd3[menuActionRow] = class9_1.id;
						menuActionRow++;
					}
					menuActionName[menuActionRow] = s + " @gre@" + class9_1.spellName;
					menuActionID[menuActionRow] = 626;
					menuActionCmd3[menuActionRow] = class9_1.id;
					menuActionRow++;
				}
				if (class9_1.atActionType == 3 && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height) {
					menuActionName[menuActionRow] = "Close";
					menuActionID[menuActionRow] = 200;
					menuActionCmd3[menuActionRow] = class9_1.id;
					menuActionRow++;
				}
				if (class9_1.atActionType == 4 && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height) {
					// System.out.println("2"+class9_1.tooltip + ", " +
					// class9_1.interfaceID);
					// menuActionName[menuActionRow] = class9_1.tooltip + ", " +
					// class9_1.id;
					menuActionName[menuActionRow] = class9_1.tooltip;
					menuActionID[menuActionRow] = 169;
					menuActionCmd3[menuActionRow] = class9_1.id;
					menuActionRow++;
					if (class9_1.hoverText != null) {
						// drawHoverBox(k, l, class9_1.hoverText);
						// System.out.println("DRAWING INTERFACE: " +
						// class9_1.hoverText);
					}
				}
				if (class9_1.atActionType == 5 && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height) {
					// System.out.println("3"+class9_1.tooltip + ", " +
					// class9_1.interfaceID);
					// menuActionName[menuActionRow] = class9_1.tooltip + ", " +
					// class9_1.id;
					menuActionName[menuActionRow] = class9_1.tooltip;
					menuActionID[menuActionRow] = 646;
					menuActionCmd3[menuActionRow] = class9_1.id;
					menuActionRow++;
				}
				if (class9_1.atActionType == 6 && !aBoolean1149 && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height) {
					// System.out.println("4"+class9_1.tooltip + ", " +
					// class9_1.interfaceID);
					// menuActionName[menuActionRow] = class9_1.tooltip + ", " +
					// class9_1.id;
					menuActionName[menuActionRow] = class9_1.tooltip;
					menuActionID[menuActionRow] = 679;
					menuActionCmd3[menuActionRow] = class9_1.id;
					menuActionRow++;
				}
				if (class9_1.type == 2) {
					int k2 = 0;
					for (int l2 = 0; l2 < class9_1.height; l2++) {
						for (int i3 = 0; i3 < class9_1.width; i3++) {
							int j3 = i2 + i3 * (32 + class9_1.invSpritePadX);
							int k3 = j2 + l2 * (32 + class9_1.invSpritePadY);
							if (k2 < 20) {
								j3 += class9_1.spritesX[k2];
								k3 += class9_1.spritesY[k2];
							}
							if (k >= j3 && i1 >= k3 && k < j3 + 32 && i1 < k3 + 32) {
								mouseInvInterfaceIndex = k2;
								lastActiveInvInterface = class9_1.id;
								if (class9_1.inv[k2] > 0) {
									ItemDef itemDef = ItemDef.forID(class9_1.inv[k2] - 1);
									if (itemSelected == 1 && class9_1.isInventoryInterface) {
										if (class9_1.id != anInt1284 || k2 != anInt1283) {
											menuActionName[menuActionRow] = "Use " + selectedItemName + " with @lre@" + itemDef.name;
											menuActionID[menuActionRow] = 870;
											menuActionCmd1[menuActionRow] = itemDef.id;
											menuActionCmd2[menuActionRow] = k2;
											menuActionCmd3[menuActionRow] = class9_1.id;
											menuActionRow++;
										}
									} else if (spellSelected == 1 && class9_1.isInventoryInterface) {
										if ((spellUsableOn & 0x10) == 16) {
											menuActionName[menuActionRow] = spellTooltip + " @lre@" + itemDef.name;
											menuActionID[menuActionRow] = 543;
											menuActionCmd1[menuActionRow] = itemDef.id;
											menuActionCmd2[menuActionRow] = k2;
											menuActionCmd3[menuActionRow] = class9_1.id;
											menuActionRow++;
										}
									} else {
										if (class9_1.isInventoryInterface) {
											for (int l3 = 4; l3 >= 3; l3--)
												if (itemDef.itemActions != null && itemDef.itemActions[l3] != null) {
													menuActionName[menuActionRow] = itemDef.itemActions[l3] + " @lre@" + itemDef.name;
													if (l3 == 3)
														menuActionID[menuActionRow] = 493;
													if (l3 == 4)
														menuActionID[menuActionRow] = 847;
													menuActionCmd1[menuActionRow] = itemDef.id;
													menuActionCmd2[menuActionRow] = k2;
													menuActionCmd3[menuActionRow] = class9_1.id;
													menuActionRow++;
												} else if (l3 == 4) {
													menuActionName[menuActionRow] = "Drop @lre@" + itemDef.name;
													menuActionID[menuActionRow] = 847;
													menuActionCmd1[menuActionRow] = itemDef.id;
													menuActionCmd2[menuActionRow] = k2;
													menuActionCmd3[menuActionRow] = class9_1.id;
													menuActionRow++;
												}
										}
										if (class9_1.usableItemInterface) {
											menuActionName[menuActionRow] = "Use @lre@" + itemDef.name;
											menuActionID[menuActionRow] = 447;
											menuActionCmd1[menuActionRow] = itemDef.id;
											menuActionCmd2[menuActionRow] = k2;
											menuActionCmd3[menuActionRow] = class9_1.id;
											menuActionRow++;
										}
										if (class9_1.isInventoryInterface && itemDef.itemActions != null) {
											for (int i4 = 2; i4 >= 0; i4--)
												if (itemDef.itemActions[i4] != null) {
													menuActionName[menuActionRow] = itemDef.itemActions[i4] + " @lre@" + itemDef.name;
													if (i4 == 0)
														menuActionID[menuActionRow] = 74;
													if (i4 == 1)
														menuActionID[menuActionRow] = 454;
													if (i4 == 2)
														menuActionID[menuActionRow] = 539;
													menuActionCmd1[menuActionRow] = itemDef.id;
													menuActionCmd2[menuActionRow] = k2;
													menuActionCmd3[menuActionRow] = class9_1.id;
													menuActionRow++;
												}

										}
										if (class9_1.actions != null) {
											for (int j4 = 4; j4 >= 0; j4--)
												if (class9_1.actions[j4] != null) {
													menuActionName[menuActionRow] = class9_1.actions[j4] + " @lre@" + itemDef.name;
													if (j4 == 0)
														menuActionID[menuActionRow] = 632;
													if (j4 == 1)
														menuActionID[menuActionRow] = 78;
													if (j4 == 2)
														menuActionID[menuActionRow] = 867;
													if (j4 == 3)
														menuActionID[menuActionRow] = 431;
													if (j4 == 4)
														menuActionID[menuActionRow] = 53;
													menuActionCmd1[menuActionRow] = itemDef.id;
													menuActionCmd2[menuActionRow] = k2;
													menuActionCmd3[menuActionRow] = class9_1.id;
													menuActionRow++;
												}

										}
										// menuActionName[menuActionRow] =
										// "Examine @lre@" + itemDef.name +
										// " @gre@(@whi@" + (class9_1.inv[k2] -
										// 1) + "@gre@)";
										menuActionName[menuActionRow] = (myPrivilege < 2 ? "Examine @lre@" + itemDef.name : "Examine @lre@" + itemDef.name +
												" @gre@(@whi@" + (class9_1.inv[k2] - 1) + "@gre@)");
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
		scrollBar1.drawSprite(i1, l);
		scrollBar2.drawSprite(i1, (l + j) - 16);
		DrawingArea.drawPixels(j - 32, l + 16, i1, 0x000001, 16);
		DrawingArea.drawPixels(j - 32, l + 16, i1, 0x3d3426, 15);
		DrawingArea.drawPixels(j - 32, l + 16, i1, 0x342d21, 13);
		DrawingArea.drawPixels(j - 32, l + 16, i1, 0x2e281d, 11);
		DrawingArea.drawPixels(j - 32, l + 16, i1, 0x29241b, 10);
		DrawingArea.drawPixels(j - 32, l + 16, i1, 0x252019, 9);
		DrawingArea.drawPixels(j - 32, l + 16, i1, 0x000001, 1);
		int k1 = ((j - 32) * j) / j1;
		if (k1 < 8)
			k1 = 8;
		int l1 = ((j - 32 - k1) * k) / (j1 - j);
		DrawingArea.drawPixels(k1, l + 16 + l1, i1, barFillColor, 16);
		DrawingArea.method341(l + 16 + l1, 0x000001, k1, i1);
		DrawingArea.method341(l + 16 + l1, 0x817051, k1, i1 + 1);
		DrawingArea.method341(l + 16 + l1, 0x73654a, k1, i1 + 2);
		DrawingArea.method341(l + 16 + l1, 0x6a5c43, k1, i1 + 3);
		DrawingArea.method341(l + 16 + l1, 0x6a5c43, k1, i1 + 4);
		DrawingArea.method341(l + 16 + l1, 0x655841, k1, i1 + 5);
		DrawingArea.method341(l + 16 + l1, 0x655841, k1, i1 + 6);
		DrawingArea.method341(l + 16 + l1, 0x61553e, k1, i1 + 7);
		DrawingArea.method341(l + 16 + l1, 0x61553e, k1, i1 + 8);
		DrawingArea.method341(l + 16 + l1, 0x5d513c, k1, i1 + 9);
		DrawingArea.method341(l + 16 + l1, 0x5d513c, k1, i1 + 10);
		DrawingArea.method341(l + 16 + l1, 0x594e3a, k1, i1 + 11);
		DrawingArea.method341(l + 16 + l1, 0x594e3a, k1, i1 + 12);
		DrawingArea.method341(l + 16 + l1, 0x514635, k1, i1 + 13);
		DrawingArea.method341(l + 16 + l1, 0x4b4131, k1, i1 + 14);
		DrawingArea.method339(l + 16 + l1, 0x000001, 15, i1);
		DrawingArea.method339(l + 17 + l1, 0x000001, 15, i1);
		DrawingArea.method339(l + 17 + l1, 0x655841, 14, i1);
		DrawingArea.method339(l + 17 + l1, 0x6a5c43, 13, i1);
		DrawingArea.method339(l + 17 + l1, 0x6d5f48, 11, i1);
		DrawingArea.method339(l + 17 + l1, 0x73654a, 10, i1);
		DrawingArea.method339(l + 17 + l1, 0x76684b, 7, i1);
		DrawingArea.method339(l + 17 + l1, 0x7b6a4d, 5, i1);
		DrawingArea.method339(l + 17 + l1, 0x7e6e50, 4, i1);
		DrawingArea.method339(l + 17 + l1, 0x817051, 3, i1);
		DrawingArea.method339(l + 17 + l1, 0x000001, 2, i1);
		DrawingArea.method339(l + 18 + l1, 0x000001, 16, i1);
		DrawingArea.method339(l + 18 + l1, 0x564b38, 15, i1);
		DrawingArea.method339(l + 18 + l1, 0x5d513c, 14, i1);
		DrawingArea.method339(l + 18 + l1, 0x625640, 11, i1);
		DrawingArea.method339(l + 18 + l1, 0x655841, 10, i1);
		DrawingArea.method339(l + 18 + l1, 0x6a5c43, 7, i1);
		DrawingArea.method339(l + 18 + l1, 0x6e6046, 5, i1);
		DrawingArea.method339(l + 18 + l1, 0x716247, 4, i1);
		DrawingArea.method339(l + 18 + l1, 0x7b6a4d, 3, i1);
		DrawingArea.method339(l + 18 + l1, 0x817051, 2, i1);
		DrawingArea.method339(l + 18 + l1, 0x000001, 1, i1);
		DrawingArea.method339(l + 19 + l1, 0x000001, 16, i1);
		DrawingArea.method339(l + 19 + l1, 0x514635, 15, i1);
		DrawingArea.method339(l + 19 + l1, 0x564b38, 14, i1);
		DrawingArea.method339(l + 19 + l1, 0x5d513c, 11, i1);
		DrawingArea.method339(l + 19 + l1, 0x61553e, 9, i1);
		DrawingArea.method339(l + 19 + l1, 0x655841, 7, i1);
		DrawingArea.method339(l + 19 + l1, 0x6a5c43, 5, i1);
		DrawingArea.method339(l + 19 + l1, 0x6e6046, 4, i1);
		DrawingArea.method339(l + 19 + l1, 0x73654a, 3, i1);
		DrawingArea.method339(l + 19 + l1, 0x817051, 2, i1);
		DrawingArea.method339(l + 19 + l1, 0x000001, 1, i1);
		DrawingArea.method339(l + 20 + l1, 0x000001, 16, i1);
		DrawingArea.method339(l + 20 + l1, 0x4b4131, 15, i1);
		DrawingArea.method339(l + 20 + l1, 0x544936, 14, i1);
		DrawingArea.method339(l + 20 + l1, 0x594e3a, 13, i1);
		DrawingArea.method339(l + 20 + l1, 0x5d513c, 10, i1);
		DrawingArea.method339(l + 20 + l1, 0x61553e, 8, i1);
		DrawingArea.method339(l + 20 + l1, 0x655841, 6, i1);
		DrawingArea.method339(l + 20 + l1, 0x6a5c43, 4, i1);
		DrawingArea.method339(l + 20 + l1, 0x73654a, 3, i1);
		DrawingArea.method339(l + 20 + l1, 0x817051, 2, i1);
		DrawingArea.method339(l + 20 + l1, 0x000001, 1, i1);
		DrawingArea.method341(l + 16 + l1, 0x000001, k1, i1 + 15);
		DrawingArea.method339(l + 15 + l1 + k1, 0x000001, 16, i1);
		DrawingArea.method339(l + 14 + l1 + k1, 0x000001, 15, i1);
		DrawingArea.method339(l + 14 + l1 + k1, 0x3f372a, 14, i1);
		DrawingArea.method339(l + 14 + l1 + k1, 0x443c2d, 10, i1);
		DrawingArea.method339(l + 14 + l1 + k1, 0x483e2f, 9, i1);
		DrawingArea.method339(l + 14 + l1 + k1, 0x4a402f, 7, i1);
		DrawingArea.method339(l + 14 + l1 + k1, 0x4b4131, 4, i1);
		DrawingArea.method339(l + 14 + l1 + k1, 0x564b38, 3, i1);
		DrawingArea.method339(l + 14 + l1 + k1, 0x000001, 2, i1);
		DrawingArea.method339(l + 13 + l1 + k1, 0x000001, 16, i1);
		DrawingArea.method339(l + 13 + l1 + k1, 0x443c2d, 15, i1);
		DrawingArea.method339(l + 13 + l1 + k1, 0x4b4131, 11, i1);
		DrawingArea.method339(l + 13 + l1 + k1, 0x514635, 9, i1);
		DrawingArea.method339(l + 13 + l1 + k1, 0x544936, 7, i1);
		DrawingArea.method339(l + 13 + l1 + k1, 0x564b38, 6, i1);
		DrawingArea.method339(l + 13 + l1 + k1, 0x594e3a, 4, i1);
		DrawingArea.method339(l + 13 + l1 + k1, 0x625640, 3, i1);
		DrawingArea.method339(l + 13 + l1 + k1, 0x6a5c43, 2, i1);
		DrawingArea.method339(l + 13 + l1 + k1, 0x000001, 1, i1);
		DrawingArea.method339(l + 12 + l1 + k1, 0x000001, 16, i1);
		DrawingArea.method339(l + 12 + l1 + k1, 0x443c2d, 15, i1);
		DrawingArea.method339(l + 12 + l1 + k1, 0x4b4131, 14, i1);
		DrawingArea.method339(l + 12 + l1 + k1, 0x544936, 12, i1);
		DrawingArea.method339(l + 12 + l1 + k1, 0x564b38, 11, i1);
		DrawingArea.method339(l + 12 + l1 + k1, 0x594e3a, 10, i1);
		DrawingArea.method339(l + 12 + l1 + k1, 0x5d513c, 7, i1);
		DrawingArea.method339(l + 12 + l1 + k1, 0x61553e, 4, i1);
		DrawingArea.method339(l + 12 + l1 + k1, 0x6e6046, 3, i1);
		DrawingArea.method339(l + 12 + l1 + k1, 0x7b6a4d, 2, i1);
		DrawingArea.method339(l + 12 + l1 + k1, 0x000001, 1, i1);
		DrawingArea.method339(l + 11 + l1 + k1, 0x000001, 16, i1);
		DrawingArea.method339(l + 11 + l1 + k1, 0x4b4131, 15, i1);
		DrawingArea.method339(l + 11 + l1 + k1, 0x514635, 14, i1);
		DrawingArea.method339(l + 11 + l1 + k1, 0x564b38, 13, i1);
		DrawingArea.method339(l + 11 + l1 + k1, 0x594e3a, 11, i1);
		DrawingArea.method339(l + 11 + l1 + k1, 0x5d513c, 9, i1);
		DrawingArea.method339(l + 11 + l1 + k1, 0x61553e, 7, i1);
		DrawingArea.method339(l + 11 + l1 + k1, 0x655841, 5, i1);
		DrawingArea.method339(l + 11 + l1 + k1, 0x6a5c43, 4, i1);
		DrawingArea.method339(l + 11 + l1 + k1, 0x73654a, 3, i1);
		DrawingArea.method339(l + 11 + l1 + k1, 0x7b6a4d, 2, i1);
		DrawingArea.method339(l + 11 + l1 + k1, 0x000001, 1, i1);
	}

	private void updateNPCs(Stream stream, int i) {
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
			Signlink.reporterror(myUsername + " size mismatch in getnpcpos - pos:" + stream.currentOffset + " psize:" + i);
			throw new RuntimeException("eek");
		}
		for (int i1 = 0; i1 < npcCount; i1++)
			if (npcArray[npcIndices[i1]] == null) {
				Signlink.reporterror(myUsername + " null entry in npc list - pos:" + i1 + " size:" + npcCount);
				throw new RuntimeException("eek");
			}

	}

	private int cButtonHPos;
	private int cButtonCPos;
	public int channel;

	private void processChatModeClick() {
		int y = 0;
		if(clientSize != 0) {
			y = clientHeight - 503;
		}
		if(super.mouseX >= 5 && super.mouseX <= 61 && super.mouseY >= y + 482 && super.mouseY <= y + 503) {
			cButtonHPos = 0;
			inputTaken = true;
		} else if(super.mouseX >= 71 && super.mouseX <= 127 && super.mouseY >= y + 482 && super.mouseY <= y + 503) {
			cButtonHPos = 1;
			inputTaken = true;
		} else if(super.mouseX >= 137 && super.mouseX <= 193 && super.mouseY >= y + 482 && super.mouseY <= y + 503) {
			cButtonHPos = 2;
			inputTaken = true;
		} else if(super.mouseX >= 203 && super.mouseX <= 259 && super.mouseY >= y + 482 && super.mouseY <= y + 503) {
			cButtonHPos = 3;
			inputTaken = true;
		} else if(super.mouseX >= 269 && super.mouseX <= 325 && super.mouseY >= y + 482 && super.mouseY <= y + 503) {
			cButtonHPos = 4;
			inputTaken = true;
		} else if(super.mouseX >= 335 && super.mouseX <= 391 && super.mouseY >= y + 482 && super.mouseY <= y + 503) {
			cButtonHPos = 5;
			inputTaken = true;
		} else if(super.mouseX >= 401 && super.mouseX <= 457 && super.mouseY >= y + 482 && super.mouseY <= y + 503) {
			cButtonHPos = 6;
			inputTaken = true;
		} else {
			cButtonHPos = -1;
			inputTaken = true;
		}
		if(super.clickMode3 == 1) {
			if(super.saveClickX >= 5 && super.saveClickX <= 61 && super.saveClickY >= y + 482 && super.saveClickY <= y + 505) {
				if (clientSize != 0) {
					if (channel != 0) {
						cButtonCPos = 0;
						chatTypeView = 0;
						inputTaken = true;
						channel = 0;
					} else {
						showChat = showChat ? false : true;
					}
				} else {
					cButtonCPos = 0;
					chatTypeView = 0;
					inputTaken = true;
					channel = 0;
				}
			} else if(super.saveClickX >= 71 && super.saveClickX <= 127 && super.saveClickY >= y + 482 && super.saveClickY <= y + 505) {
				if (clientSize != 0) {
					if (channel != 1 && clientSize != 0) {
						cButtonCPos = 1;
						chatTypeView = 5;
						inputTaken = true;
						channel = 1;
					} else {
						showChat = showChat ? false : true;
					}
				} else {
					cButtonCPos = 1;
					chatTypeView = 5;
					inputTaken = true;
					channel = 1;
				}
			} else if(super.saveClickX >= 137 && super.saveClickX <= 193 && super.saveClickY >= y + 482 && super.saveClickY <= y + 505) {
				if (clientSize != 0) {
					if (channel != 2 && clientSize != 0) {
						cButtonCPos = 2;
						chatTypeView = 1;
						inputTaken = true;
						channel = 2;
					} else {
						showChat = showChat ? false : true;
					}
				} else {
					cButtonCPos = 2;
					chatTypeView = 1;
					inputTaken = true;
					channel = 2;
				}
			} else if(super.saveClickX >= 203 && super.saveClickX <= 259 && super.saveClickY >= y + 482 && super.saveClickY <= y + 505) {
				if (clientSize != 0) {
					if (channel != 3 && clientSize != 0) {
						cButtonCPos = 3;
						chatTypeView = 2;
						inputTaken = true;
						channel = 3;
					} else {
						showChat = showChat ? false : true;
					}
				} else {
					cButtonCPos = 3;
					chatTypeView = 2;
					inputTaken = true;
					channel = 3;
				}
			} else if(super.saveClickX >= 269 && super.saveClickX <= 325 && super.saveClickY >= y + 482 && super.saveClickY <= y + 505) {
				if (clientSize != 0) {
					if (channel != 4 && clientSize != 0) {
						cButtonCPos = 4;
						chatTypeView = 4;
						inputTaken = true;
						channel = 4;
					} else {
						showChat = showChat ? false : true;
					}
				} else {
					cButtonCPos = 4;
					chatTypeView = 4;
					inputTaken = true;
					channel = 4;
				}
			} else if(super.saveClickX >= 335 && super.saveClickX <= 391 && super.saveClickY >= y + 482 && super.saveClickY <= y + 505) {
				if (clientSize != 0) {
					if (channel != 5 && clientSize != 0) {
						cButtonCPos = 5;
						chatTypeView = 3;
						inputTaken = true;
						channel = 5;
					} else {
						showChat = showChat ? false : true;
					}
				} else {
					cButtonCPos = 5;
					chatTypeView = 3;
					inputTaken = true;
					channel = 5;
				}
			} else if(super.saveClickX >= 401 && super.saveClickX <= 457 && super.saveClickY >= y + 482 && super.saveClickY <= y + 505) {
				if (clientSize != 0) {
					if (channel != 9 && clientSize != 0) {
						cButtonCPos = 6;
						chatTypeView = 9;
						inputTaken = true;
						channel = 9;
					} else {
						showChat = showChat ? false : true;
					}
				} else {
					cButtonCPos = 6;
					chatTypeView = 9;
					inputTaken = true;
					channel = 9;
				}
			}
			
			/* else if(super.saveClickX >= 404 && super.saveClickX <= 515 && super.saveClickY >= y + 482 && super.saveClickY <= y + 505) {
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

	private void method33(int i) {
		try {
			int j = Varp.cache[i].anInt709;
			if (j == 0)
				return;
			int k = variousSettings[i];
			if (j == 1) {
				if (k == 1)
					chosenBrightness = 0.90000000000000002D;
				if (k == 2)
					chosenBrightness = 0.80000000000000004D;
				if (k == 3)
					chosenBrightness = 0.69999999999999996D;
				if (k == 4)
					chosenBrightness = 0.59999999999999998D;
				
				Texture.method372(chosenBrightness);
				ItemDef.mruNodes1.unlinkAll();
				welcomeScreenRaised = true;
			}
			if (j == 3) {
			    if (MusicEnabled) {
				boolean flag = musicEnabled;
				if (k == 0) {
				    musicVolume = 200;
				    musicEnabled = true;
				}
				if (k == 1) {
				    musicVolume = 150;
				    musicEnabled = true;
				}
				if (k == 2) {
				    musicVolume = 100;
				    musicEnabled = true;
				}
				if (k == 3) {
				    musicVolume = 50;
				    musicEnabled = true;
				}
				if (k == 4) {
				    musicVolume = 0;
				    musicEnabled = false;
				}
				adjustVolume(musicEnabled, musicVolume);
				if (musicEnabled != flag && !lowMem) {
				    if (musicEnabled) {
					nextSong = currentSong;
					songChanging = true;
					onDemandFetcher.method558(2, nextSong);
				    } else {
					stopMidi();
				    }
				    previousSong = 0;
				}
			    }
			}

			if (j == 4) {
			    if(MusicEnabled) {
				SoundPlayer.setVolume(k);
				if (k == 0) {
					aBoolean848 = true;
					setWaveVolume(0);
				}
				if (k == 1) {
					aBoolean848 = true;
					setWaveVolume(-400);
				}
				if (k == 2) {
					aBoolean848 = true;
					setWaveVolume(-800);
				}
				if (k == 3) {
					aBoolean848 = true;
					setWaveVolume(-1200);
				}
				if (k == 4) {
					aBoolean848 = false;
				}
				/*if (k == 0) {
					aBoolean848 = true;
					setWaveVolume(0);
				}
				if (k == 1) {
					aBoolean848 = true;
					setWaveVolume(-400);
				}
				if (k == 2) {
					aBoolean848 = true;
					setWaveVolume(-800);
				}
				if (k == 3) {
					aBoolean848 = true;
					setWaveVolume(-1200);
				}
				if (k == 4)
					aBoolean848 = false;*/
				}
			}
			if (j == 5)
				anInt1253 = k;
			if (j == 6)
				anInt1249 = k;
			if (j == 8) {
				splitPrivateChat = k;
				inputTaken = true;
			}
			if (j == 9)
				anInt913 = k;
		} catch (Exception e) {
		}
	}

	public StreamLoader mediaStreamLoader;

	public void updateEntities() {
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

	private void delFriend(long l) {
		try {
			if (l == 0L)
				return;
			for (int i = 0; i < friendsCount; i++) {
				if (friendsListAsLongs[i] != l)
					continue;
				friendsCount--;
				needDrawTabArea = true;
				for (int j = i; j < friendsCount; j++) {
					friendsList[j] = friendsList[j + 1];
					friendsNodeIDs[j] = friendsNodeIDs[j + 1];
					friendsListAsLongs[j] = friendsListAsLongs[j + 1];
				}

				stream.createFrame(215);
				stream.writeQWord(l);
				break;
			}
		} catch (RuntimeException runtimeexception) {
			Signlink.reporterror("18622, " + false + ", " + l + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public void drawSideIcons() {
		if (clientSize == 0) {
			int[] id = {
					9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22	
			};
			int[] tab = {
					0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13	
			};
			int[] positionX = {
					17, 49, 83, 114, 147, 180, 214, 14, 49, 82, 116, 148, 184, 216
			};
			int[] positionY = {
					9, 7, 7, 5, 2, 3, 7, 303, 306, 306, 302, 304, 302, 303	
			};
			for (int i = 0; i < 14; i++) {
				if (tabInterfaceIDs[tab[i]] != -1) {
					if (id[i] != -1) {
						cacheSprite[id[i]].drawSprite(positionX[i], positionY[i]);
					}
				}
			}
		} else if (clientSize != 0) {
			if(clientWidth < smallTabs) {
				int[] id = {
						9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22	
				};
				int[] tab = {
						0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13	
				};
				int [] positionX = {
						231, 198, 165, 134, 100, 67, 32,
						232, 198, 164, 131, 98, 64, 32,
				};
				int [] positionY = {
						65, 67, 66, 68, 71, 70, 65,
						32, 30, 30, 33, 31, 32, 32
				};
				for (int i = 0; i < 14; i++) {
					if (tabInterfaceIDs[tab[i]] != -1) {
						if (id[i] != -1) {
							cacheSprite[id[i]].drawSprite(clientWidth - positionX[i], clientHeight - positionY[i]);
						}
					}
				}
			} else if(clientWidth >= smallTabs) {
				int[] id = {
						9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22	
				};
				int[] tab = {
						0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13	
				};
				int [] positionX = {
						455, 423, 389, 359, 325, 292, 258,
						226, 193, 160, 126, 95, 61, 29,
				};
				int [] positionY = {
						29, 31, 29, 32, 34, 34, 31,
						32, 30, 30, 33, 31, 32, 32
				};
				for (int i = 0; i < 14; i++) {
					if (tabInterfaceIDs[tab[i]] != -1) {
						if (id[i] != -1) {
							cacheSprite[id[i]].drawSprite(clientWidth - positionX[i], clientHeight - positionY[i]);
						}
					}
				}
			}
		}
	}

	public void drawRedStones() {
		if(clientSize == 0) {
			if(tabInterfaceIDs[tabID] != -1) {
				switch(tabID) {
				case 0:
					cacheSprite[4].drawSprite(6, 0);
					break;
				case 1:
					cacheSprite[8].drawSprite(44, 0);
					break;
				case 2:
					cacheSprite[8].drawSprite(77, 0);
					break;
				case 3:
					cacheSprite[8].drawSprite(110, 0);
					break;
				case 4:
					cacheSprite[8].drawSprite(143, 0);
					break;
				case 5:
					cacheSprite[8].drawSprite(176, 0);
					break;
				case 6:
					cacheSprite[5].drawSprite(209, 0);
					break;
				case 7:
					cacheSprite[6].drawSprite(6, 298);
					break;
				case 8:
					cacheSprite[8].drawSprite(44, 298);
					break;
				case 9:
					cacheSprite[8].drawSprite(77, 298);
					break;
				case 10:
					cacheSprite[8].drawSprite(110, 298);
					break;
				case 11:
					cacheSprite[8].drawSprite(143, 298);
					break;
				case 12:
					cacheSprite[8].drawSprite(176, 298);
					break;
				case 13:
					cacheSprite[7].drawSprite(209, 298);
					break;
				}
			}
		} else if (clientSize != 0) {
			if(clientWidth < smallTabs) {
				if(tabInterfaceIDs[tabID] != -1) {
					if(!showTab){
						return;
					}
					switch(tabID) {
					case 0:
						cacheSprite[4].drawSprite(clientWidth - 241, clientHeight - 73);
						break;
					case 1:
						cacheSprite[8].drawSprite(clientWidth - 202, clientHeight - 73);
						break;
					case 2:
						cacheSprite[8].drawSprite(clientWidth - 170, clientHeight - 73);
						break;
					case 3:
						cacheSprite[8].drawSprite(clientWidth - 138, clientHeight - 73);
						break;
					case 4:
						cacheSprite[8].drawSprite(clientWidth - 104, clientHeight - 73);
						break;
					case 5:
						cacheSprite[8].drawSprite(clientWidth - 71, clientHeight - 73);
						break;
					case 6:
						cacheSprite[5].drawSprite(clientWidth - 38, clientHeight - 73);
						break;
					case 7:
						cacheSprite[6].drawSprite(clientWidth - 241, clientHeight - 37);
						break;
					case 8:
						cacheSprite[8].drawSprite(clientWidth - 202, clientHeight - 37);
						break;
					case 9:
						cacheSprite[8].drawSprite(clientWidth - 170, clientHeight - 37);
						break;
					case 10:
						cacheSprite[8].drawSprite(clientWidth - 138, clientHeight - 37);
						break;
					case 11:
						cacheSprite[8].drawSprite(clientWidth - 104, clientHeight - 37);
						break;
					case 12:
						cacheSprite[8].drawSprite(clientWidth - 71, clientHeight - 37);
						break;
					case 13:
						cacheSprite[7].drawSprite(clientWidth - 38, clientHeight - 37);
						break;
					}
				}
			} else if (clientWidth >= smallTabs) {
				if(tabInterfaceIDs[tabID] != -1) {
					if(!showTab){
						return;
					}
					switch(tabID) {
					case 0:
						cacheSprite[8].drawSprite(clientWidth - 462, clientHeight - 36);
						break;
					case 1:
						cacheSprite[8].drawSprite(clientWidth - 429, clientHeight - 36);
						break;
					case 2:
						cacheSprite[8].drawSprite(clientWidth - 396, clientHeight - 36);
						break;
					case 3:
						cacheSprite[8].drawSprite(clientWidth - 363, clientHeight - 36);
						break;
					case 4:
						cacheSprite[8].drawSprite(clientWidth - 330, clientHeight - 36);
						break;
					case 5:
						cacheSprite[8].drawSprite(clientWidth - 297, clientHeight - 36);
						break;
					case 6:
						cacheSprite[8].drawSprite(clientWidth - 264, clientHeight - 36);
						break;
					case 7:
						cacheSprite[8].drawSprite(clientWidth - 231, clientHeight - 36);
						break;
					case 8:
						cacheSprite[8].drawSprite(clientWidth - 198, clientHeight - 36);
						break;
					case 9:
						cacheSprite[8].drawSprite(clientWidth - 165, clientHeight - 36);
						break;
					case 10:
						cacheSprite[8].drawSprite(clientWidth - 132, clientHeight - 36);
						break;
					case 11:
						cacheSprite[8].drawSprite(clientWidth - 99, clientHeight - 36);
						break;
					case 12:
						cacheSprite[8].drawSprite(clientWidth - 66, clientHeight - 36);
						break;
					case 13:
						cacheSprite[8].drawSprite(clientWidth - 33, clientHeight - 36);
						break;
					}
				}
			}
		}
	}

	private void drawTabArea() {
		if (clientSize == 0) {
			aRSImageProducer_1163.initDrawingArea();
		}
		if(clientSize == 0) {
			cacheSprite[23].drawSprite(0, 0);
			mascotInv.draw24BitSprite(30, 37);
		}
		Texture.anIntArray1472 = anIntArray1181;
		int y = clientWidth >= smallTabs ? 37 : 74;
		if(clientSize != 0) {
			if(clientWidth >= smallTabs) {
				cacheSprite[27].drawSprite(clientWidth - 461, clientHeight - 36);
			} else {
				cacheSprite[28].drawSprite(clientWidth - 241, clientHeight - 73);
			}
		}	
		if (showTab) {
			if(clientWidth >= smallTabs) {
				cacheSprite[29].drawSprite(clientWidth - 204, clientHeight - 310);
				DrawingArea.method335(0x50463C, clientHeight - 303, 190, 260, 100, clientWidth - 197);
				mascotInv.draw24BitSprite(clientWidth - 198, clientHeight - 303);
			} else {
				cacheSprite[29].drawSprite(clientWidth - 222, clientHeight - 346);
				DrawingArea.method335(0x50463C, clientHeight - 339, 190, 260, 100, clientWidth - 216);
				mascotInv.draw24BitSprite(clientWidth - 216, clientHeight - 339);
			}
		}
		int x1 = 0, y1 = 0;
		if(clientSize != 0) {
			x1 = clientWidth - 225;
			y1 = clientHeight - 340;
		}
		if(clientSize == 0) {
			if (invOverlayInterfaceID != -1) {
				drawInterface(0, 32,
						RSInterface.interfaceCache[invOverlayInterfaceID], 36);
			} else if (tabInterfaceIDs[tabID] != -1) {
				drawInterface(0, 32,
						RSInterface.interfaceCache[tabInterfaceIDs[tabID]], 36);
			}
		} else if (clientSize != 0 && showTab) {
			if (invOverlayInterfaceID != -1) {
				drawInterface(0, (clientWidth >= smallTabs ? x1 + 28 : clientWidth - 214),
						RSInterface.interfaceCache[invOverlayInterfaceID], (clientWidth >= smallTabs ? y1 + 37 : clientHeight - y - 265));
			} else if (tabInterfaceIDs[tabID] != -1) {
				drawInterface(0, (clientWidth >= smallTabs ? x1 + 28 : clientWidth - 214),
						RSInterface.interfaceCache[tabInterfaceIDs[tabID]], (clientWidth >= smallTabs ? y1 + 37 : clientHeight - y - 265));
			}
		}

		if(invOverlayInterfaceID == -1) {
			drawRedStones();
			drawSideIcons();
		}
		if(menuOpen && clientSize == 0) {
			drawMenu(516, 168);
		} else if (menuOpen && menuScreenArea == 1 && clientSize != 0) {
			drawMenu(516, 168);
		}
		if (clientSize == 0)
			aRSImageProducer_1163.drawGraphics(168, super.graphics, 516);
		aRSImageProducer_1165.initDrawingArea();
		Texture.anIntArray1472 = anIntArray1182;
	}

	private void method37(int j) {
		if(!lowMem) {
			if(Texture.anIntArray1480[17] >= j) {
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
				if(anInt854 > 1235) {
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
			if(Texture.anIntArray1480[24] >= j) {
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
			if(Texture.anIntArray1480[34] >= j) {
				Background background_2 = Texture.aBackgroundArray1474s[34];
				int i1 = background_2.anInt1452 * background_2.anInt1453 - 1;
				int l1 = background_2.anInt1452 * anInt945 * 2;
				byte abyte2[] = background_2.aByteArray1450;
				byte abyte5[] = aByteArray912;
				for(int k2 = 0; k2 <= i1; k2++)
					abyte5[k2] = abyte2[k2 - l1 & i1];

				background_2.aByteArray1450 = abyte5;
				aByteArray912 = abyte2;
				Texture.method370(34);
			}
			if(Texture.anIntArray1480[40] >= j);
			{
				Background background_2 = Texture.aBackgroundArray1474s[40];
				int i1 = background_2.anInt1452 * background_2.anInt1453 - 1;
				int l1 = background_2.anInt1452 * anInt945 * 2;
				byte abyte2[] = background_2.aByteArray1450;
				byte abyte5[] = aByteArray912;
				for (int k2 = 0; k2 <= i1; k2++)
				abyte5[k2] = abyte2[k2 - l1 & i1];
	
				background_2.aByteArray1450 = abyte5;
				aByteArray912 = abyte2;
				Texture.method370(40);
			}
		}
	}

	private void method38() {
		for (int i = -1; i < playerCount; i++) {
			int j;
			if (i == -1)
				j = myPlayerIndex;
			else
				j = playerIndices[i];
			Player player = playerArray[j];
			if (player != null && player.textCycle > 0) {
				player.textCycle--;
				if (player.textCycle == 0)
					player.textSpoken = null;
			}
		}
		for (int k = 0; k < npcCount; k++) {
			int l = npcIndices[k];
			NPC npc = npcArray[l];
			if (npc != null && npc.textCycle > 0) {
				npc.textCycle--;
				if (npc.textCycle == 0)
					npc.textSpoken = null;
			}
		}
	}

	private void calcCameraPos() {
		int i = anInt1098 * 128 + 64;
		int j = anInt1099 * 128 + 64;
		int k = method42(plane, j, i) - anInt1100;
		if(xCameraPos < i) {
			xCameraPos += anInt1101 + ((i - xCameraPos) * anInt1102) / 1000;
			if(xCameraPos > i)
				xCameraPos = i;
		}
		if(xCameraPos > i) {
			xCameraPos -= anInt1101 + ((xCameraPos - i) * anInt1102) / 1000;
			if(xCameraPos < i)
				xCameraPos = i;
		}
		if(zCameraPos < k) {
			zCameraPos += anInt1101 + ((k - zCameraPos) * anInt1102) / 1000;
			if(zCameraPos > k)
				zCameraPos = k;
		}
		if(zCameraPos > k) {
			zCameraPos -= anInt1101 + ((zCameraPos - k) * anInt1102) / 1000;
			if(zCameraPos < k)
				zCameraPos = k;
		}
		if(yCameraPos < j) {
			yCameraPos += anInt1101 + ((j - yCameraPos) * anInt1102) / 1000;
			if(yCameraPos > j)
				yCameraPos = j;
		}
		if(yCameraPos > j) {
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
		if(yCameraCurve < l1) {
			yCameraCurve += anInt998 + ((l1 - yCameraCurve) * anInt999) / 1000;
			if(yCameraCurve > l1)
				yCameraCurve = l1;
		}
		if(yCameraCurve > l1) {
			yCameraCurve -= anInt998 + ((yCameraCurve - l1) * anInt999) / 1000;
			if(yCameraCurve < l1)
				yCameraCurve = l1;
		}
		int j2 = i2 - xCameraCurve;
		if(j2 > 1024)
			j2 -= 2048;
		if(j2 < -1024)
			j2 += 2048;
		if(j2 > 0) {
			xCameraCurve += anInt998 + (j2 * anInt999) / 1000;
			xCameraCurve &= 0x7ff;
		}
		if(j2 < 0) {
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

	private void drawMenu(int xOffSet, int yOffSet) {
		int xPos = menuOffsetX - (xOffSet - 4);
		int yPos = (-yOffSet + 4) + menuOffsetY;
		int menuW = menuWidth;
		int menuH = menuHeight + 1;
		inputTaken = true;
		tabAreaAltered = true;
		DrawingArea.drawPixels(menuH - 4, yPos + 2, xPos, 0x706a5e, menuW);
		DrawingArea.drawPixels(menuH - 2, yPos + 1, xPos + 1, 0x706a5e, menuW - 2);
		DrawingArea.drawPixels(menuH, yPos, xPos + 2, 0x706a5e, menuW - 4);
		DrawingArea.drawPixels(menuH - 2, yPos + 1, xPos + 3, 0x2d2822, menuW - 6);
		DrawingArea.drawPixels(menuH - 4, yPos + 2, xPos + 2, 0x2d2822, menuW - 4);
		DrawingArea.drawPixels(menuH - 6, yPos + 3, xPos + 1, 0x2d2822, menuW - 2);
		DrawingArea.drawPixels(menuH - 22, yPos + 19, xPos + 2, 0x524a3d, menuW - 4);
		DrawingArea.drawPixels(menuH - 22, yPos + 20, xPos + 3, 0x524a3d, menuW - 6);
		DrawingArea.drawPixels(menuH - 23, yPos + 20, xPos + 3, 0x2b271c, menuW - 6);
		DrawingArea.fillPixels(xPos + 3, menuW - 6, 1, 0x2a291b, yPos + 2);
		DrawingArea.fillPixels(xPos + 2, menuW - 4, 1, 0x2a261b, yPos + 3);
		DrawingArea.fillPixels(xPos + 2, menuW - 4, 1, 0x252116, yPos + 4);
		DrawingArea.fillPixels(xPos + 2, menuW - 4, 1, 0x211e15, yPos + 5);
		DrawingArea.fillPixels(xPos + 2, menuW - 4, 1, 0x1e1b12, yPos + 6);
		DrawingArea.fillPixels(xPos + 2, menuW - 4, 1, 0x1a170e, yPos + 7);
		DrawingArea.fillPixels(xPos + 2, menuW - 4, 2, 0x15120b, yPos + 8);
		DrawingArea.fillPixels(xPos + 2, menuW - 4, 1, 0x100d08, yPos + 10);
		DrawingArea.fillPixels(xPos + 2, menuW - 4, 1, 0x090a04, yPos + 11);
		DrawingArea.fillPixels(xPos + 2, menuW - 4, 1, 0x080703, yPos + 12);
		DrawingArea.fillPixels(xPos + 2, menuW - 4, 1, 0x090a04, yPos + 13);
		DrawingArea.fillPixels(xPos + 2, menuW - 4, 1, 0x070802, yPos + 14);
		DrawingArea.fillPixels(xPos + 2, menuW - 4, 1, 0x090a04, yPos + 15);
		DrawingArea.fillPixels(xPos + 2, menuW - 4, 1, 0x070802, yPos + 16);
		DrawingArea.fillPixels(xPos + 2, menuW - 4, 1, 0x090a04, yPos + 17);
		DrawingArea.fillPixels(xPos + 2, menuW - 4, 1, 0x2a291b, yPos + 18);
		DrawingArea.fillPixels(xPos + 3, menuW - 6, 1, 0x564943, yPos + 19);
		chatTextDrawingArea.method385(0xc6b895, "Choose Option", yPos + 14, xPos + 3);
		int mouseX = super.mouseX - (xOffSet);
		int mouseY = (-yOffSet) + super.mouseY;
		for (int l1 = 0; l1 < menuActionRow; l1++) {
			int textY = yPos + 31 + (menuActionRow - 1 - l1) * 15;
			int disColor = 0xc6b895;
			if (mouseX > xPos && mouseX < xPos + menuW && mouseY > textY - 13 && mouseY < textY + 3) {
				DrawingArea.drawPixels(15, textY - 11, xPos + 3, 0x6f695d, menuWidth - 6);
				disColor = 0xeee5c6;
			}
			chatTextDrawingArea.method389(true, xPos + 3, disColor, menuActionName[l1], textY);
		}
	}

	private void addFriend(long l) {
		try {
			if (l == 0L)
				return;
			if (friendsCount >= 100 && anInt1046 != 1) {
				pushMessage("Your friendlist is full. Max of 100 for free users, and 200 for members", 0, "");
				return;
			}
			if (friendsCount >= 200) {
				pushMessage("Your friendlist is full. Max of 100 for free users, and 200 for members", 0, "");
				return;
			}
			String s = TextClass.fixName(TextClass.nameForLong(l));
			for (int i = 0; i < friendsCount; i++)
				if (friendsListAsLongs[i] == l) {
					pushMessage(s + " is already on your friend list", 0, "");
					return;
				}
			for (int j = 0; j < ignoreCount; j++)
				if (ignoreListAsLongs[j] == l) {
					pushMessage("Please remove " + s + " from your ignore list first", 0, "");
					return;
				}

			if (s.equals(myPlayer.name)) {
				return;
			} else {
				friendsList[friendsCount] = s;
				friendsListAsLongs[friendsCount] = l;
				friendsNodeIDs[friendsCount] = 0;
				friendsCount++;
				needDrawTabArea = true;
				stream.createFrame(188);
				stream.writeQWord(l);
				return;
			}
		} catch (RuntimeException runtimeexception) {
			Signlink.reporterror("15283, " + (byte) 68 + ", " + l + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	private int method42(int i, int j, int k) {
		int l = k >> 7;
			int i1 = j >> 7;
									if (l < 0 || i1 < 0 || l > 103 || i1 > 103)
										return 0;
									int j1 = i;
									if (j1 < 3 && (byteGroundArray[1][l][i1] & 2) == 2)
										j1++;
									int k1 = k & 0x7f;
									int l1 = j & 0x7f;
									int i2 = intGroundArray[j1][l][i1] * (128 - k1) + intGroundArray[j1][l + 1][i1] * k1 >> 7;
									int j2 = intGroundArray[j1][l][i1 + 1] * (128 - k1) + intGroundArray[j1][l + 1][i1 + 1] * k1 >> 7;
									return i2 * (128 - l1) + j2 * l1 >> 7;
	}

	private static String intToKOrMil(int j) {
		if (j < 0x186a0)
			return String.valueOf(j);
		if (j < 0x989680)
			return j / 1000 + "K";
		else
			return j / 0xf4240 + "M";
	}

	private void resetLogout() {
		try {
			if (socketStream != null)
				socketStream.close();
		} catch (Exception _ex) {
		}
		toggleSize(0);
		resetFade();
		socketStream = null;
		loggedIn = false;
		loginScreenState = 2;
		loginMessage1 = "";
		loginMessage2 = "";
		// myUsername = "";
		// myPassword = "";
		ignoreCount = 0;
		unlinkMRUNodes();
		worldController.initToNull();
		for (int i = 0; i < 4; i++)
			aClass11Array1230[i].method210();
		System.gc();
		stopMidi();
		currentSong = -1;
		nextSong = 0;
		previousSong = 0;
		if(loginMusicEnabled && MusicEnabled){
			musicVolume = 200;
			onDemandFetcher.method558(2, nextSong);
		}
	}

	private void method45() {
		aBoolean1031 = true;
		for (int j = 0; j < 7; j++) {
			anIntArray1065[j] = -1;
			for (int k = 0; k < IDK.length; k++) {
				if (IDK.cache[k].aBoolean662 || IDK.cache[k].anInt657 != j + (aBoolean1047 ? 0 : 7))
					continue;
				anIntArray1065[j] = k;
				break;
			}
		}
	}

	private void method46(int i, Stream stream) {
		while (stream.bitPosition + 21 < i * 8) {
			int k = stream.readBits(14);
			if (k == 16383)
				break;
			if (npcArray[k] == null)
				npcArray[k] = new NPC();
			NPC npc = npcArray[k];
			npcIndices[npcCount++] = k;
			npc.anInt1537 = loopCycle;
			int l = stream.readBits(5);
			if (l > 15)
				l -= 32;
			int i1 = stream.readBits(5);
			if (i1 > 15)
				i1 -= 32;
			int j1 = stream.readBits(1);
			npc.desc = EntityDef.forID(stream.readBits(14));
			int k1 = stream.readBits(1);
			if (k1 == 1)
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

	public void processGameLoop() {
		if (rsAlreadyLoaded || loadingError || genericLoadingError)
			return;
		loopCycle++;
		if (!loggedIn)
			processLoginScreenInput();
		else
			mainGameProcessor();
		processOnDemandQueue();
	}

	/*private void method47(boolean flag) {
		if (myPlayer.x >> 7 == destX && myPlayer.y >> 7 == destY)
			destX = 0;
		int j = playerCount;
		if (flag)
			j = 1;
		for (int l = 0; l < j; l++) {
			Player player;
			int i1;
			if (flag) {
				player = myPlayer;
				i1 = myPlayerIndex << 14;
			} else {
				player = playerArray[playerIndices[l]];
				i1 = playerIndices[l] << 14;
			}
			if (player == null || !player.isVisible())
				continue;
			player.aBoolean1699 = (lowMem && playerCount > 50 || playerCount > 200) && !flag && player.anInt1517 == player.anInt1511;
			int j1 = player.x >> 7;
				int k1 = player.y >> 7;
		if (j1 < 0 || j1 >= 104 || k1 < 0 || k1 >= 104)
			continue;
		if (player.aModel_1714 != null && loopCycle >= player.anInt1707 && loopCycle < player.anInt1708) {
			player.aBoolean1699 = false;
			player.anInt1709 = method42(plane, player.y, player.x);
			worldController.method286(plane, player.y, player, player.anInt1552, player.anInt1722, player.x, player.anInt1709, player.anInt1719, player.anInt1721, i1, player.anInt1720);
			continue;
		}
		if ((player.x & 0x7f) == 64 && (player.y & 0x7f) == 64) {
			if (anIntArrayArray929[j1][k1] == anInt1265)
				continue;
			anIntArrayArray929[j1][k1] = anInt1265;
		}
		player.anInt1709 = method42(plane, player.y, player.x);
		worldController.method285(plane, player.anInt1552, player.anInt1709, i1, player.y, 60, player.x, player, player.aBoolean1541);
		}
	}*/
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
	private boolean promptUserForInput(RSInterface class9) {
		int j = class9.contentType;
		if (anInt900 == 2) {
			if (j == 201) {
				inputTaken = true;
				inputDialogState = 0;
				messagePromptRaised = true;
				promptInput = "";
				friendsListAction = 1;
				aString1121 = "Enter name of friend to add to list";
			}
			if (j == 202) {
				inputTaken = true;
				inputDialogState = 0;
				messagePromptRaised = true;
				promptInput = "";
				friendsListAction = 2;
				aString1121 = "Enter name of friend to delete from list";
			}
		}
		if (j == 205) {
			anInt1011 = 250;
			return true;
		}
		if (j == 501) {
			inputTaken = true;
			inputDialogState = 0;
			messagePromptRaised = true;
			promptInput = "";
			friendsListAction = 4;
			aString1121 = "Enter name of player to add to list";
		}
		if (j == 502) {
			inputTaken = true;
			inputDialogState = 0;
			messagePromptRaised = true;
			promptInput = "";
			friendsListAction = 5;
			aString1121 = "Enter name of player to delete from list";
		}
		if (j == 550) {
			inputTaken = true;
			inputDialogState = 0;
			messagePromptRaised = true;
			promptInput = "";
			friendsListAction = 6;
			aString1121 = "Enter the name of the chat you wish to join";
		}
		if (j >= 300 && j <= 313) {
			int k = (j - 300) / 2;
			int j1 = j & 1;
			int i2 = anIntArray1065[k];
			if (i2 != -1) {
				do {
					if (j1 == 0 && --i2 < 0)
						i2 = IDK.length - 1;
					if (j1 == 1 && ++i2 >= IDK.length)
						i2 = 0;
				} while (IDK.cache[i2].aBoolean662 || IDK.cache[i2].anInt657 != k + (aBoolean1047 ? 0 : 7));
				anIntArray1065[k] = i2;
				aBoolean1031 = true;
			}
		}
		if (j >= 314 && j <= 323) {
			int l = (j - 314) / 2;
			int k1 = j & 1;
			int j2 = anIntArray990[l];
			if (k1 == 0 && --j2 < 0)
				j2 = anIntArrayArray1003[l].length - 1;
			if (k1 == 1 && ++j2 >= anIntArrayArray1003[l].length)
				j2 = 0;
			anIntArray990[l] = j2;
			aBoolean1031 = true;
		}
		if (j == 324 && !aBoolean1047) {
			aBoolean1047 = true;
			method45();
		}
		if (j == 325 && aBoolean1047) {
			aBoolean1047 = false;
			method45();
		}
		if (j == 326) {
			stream.createFrame(101);
			stream.writeWordBigEndian(aBoolean1047 ? 0 : 1);
			for (int i1 = 0; i1 < 7; i1++)
				stream.writeWordBigEndian(anIntArray1065[i1]);

			for (int l1 = 0; l1 < 5; l1++)
				stream.writeWordBigEndian(anIntArray990[l1]);

			return true;
		}
		if (j == 613)
			canMute = !canMute;
		if (j >= 601 && j <= 612) {
			clearTopInterfaces();
			if (reportAbuseInput.length() > 0) {
				stream.createFrame(218);
				stream.writeQWord(TextClass.longForName(reportAbuseInput));
				stream.writeWordBigEndian(j - 601);
				stream.writeWordBigEndian(canMute ? 1 : 0);
			}
		}
		return false;
	}

	private void method49(Stream stream) {
		for (int j = 0; j < anInt893; j++) {
			int k = anIntArray894[j];
			Player player = playerArray[k];
			int l = stream.readUnsignedByte();
			if ((l & 0x40) != 0)
				l += stream.readUnsignedByte() << 8;
			method107(l, k, stream, player);
		}
	}

	private void method50(int i, int k, int l, int i1, int j1) {
		int k1 = worldController.method300(j1, l, i);
		if (k1 != 0) {
			int l1 = worldController.method304(j1, l, i, k1);
			int k2 = l1 >> 6 & 3;
			int i3 = l1 & 0x1f;
			int k3 = k;
			if (k1 > 0)
				k3 = i1;
			int ai[] = minimapImage.myPixels;
			int k4 = 24624 + l * 4 + (103 - i) * 512 * 4;
			int i5 = k1 >> 14 & 0x7fff;
			ObjectDef class46_2 = ObjectDef.forID(i5);
			if (class46_2.anInt758 != -1) {
				Background background_2 = mapScenes[class46_2.anInt758];
				if (background_2 != null) {
					int i6 = (class46_2.anInt744 * 4 - background_2.anInt1452) / 2;
					int j6 = (class46_2.anInt761 * 4 - background_2.anInt1453) / 2;
					background_2.drawBackground(48 + l * 4 + i6, 48 + (104 - i - class46_2.anInt761) * 4 + j6);
				}
			} else {
				if (i3 == 0 || i3 == 2)
					if (k2 == 0) {
						ai[k4] = k3;
						ai[k4 + 512] = k3;
						ai[k4 + 1024] = k3;
						ai[k4 + 1536] = k3;
					} else if (k2 == 1) {
						ai[k4] = k3;
						ai[k4 + 1] = k3;
						ai[k4 + 2] = k3;
						ai[k4 + 3] = k3;
					} else if (k2 == 2) {
						ai[k4 + 3] = k3;
						ai[k4 + 3 + 512] = k3;
						ai[k4 + 3 + 1024] = k3;
						ai[k4 + 3 + 1536] = k3;
					} else if (k2 == 3) {
						ai[k4 + 1536] = k3;
						ai[k4 + 1536 + 1] = k3;
						ai[k4 + 1536 + 2] = k3;
						ai[k4 + 1536 + 3] = k3;
					}
				if (i3 == 3)
					if (k2 == 0)
						ai[k4] = k3;
					else if (k2 == 1)
						ai[k4 + 3] = k3;
					else if (k2 == 2)
						ai[k4 + 3 + 1536] = k3;
					else if (k2 == 3)
						ai[k4 + 1536] = k3;
				if (i3 == 2)
					if (k2 == 3) {
						ai[k4] = k3;
						ai[k4 + 512] = k3;
						ai[k4 + 1024] = k3;
						ai[k4 + 1536] = k3;
					} else if (k2 == 0) {
						ai[k4] = k3;
						ai[k4 + 1] = k3;
						ai[k4 + 2] = k3;
						ai[k4 + 3] = k3;
					} else if (k2 == 1) {
						ai[k4 + 3] = k3;
						ai[k4 + 3 + 512] = k3;
						ai[k4 + 3 + 1024] = k3;
						ai[k4 + 3 + 1536] = k3;
					} else if (k2 == 2) {
						ai[k4 + 1536] = k3;
						ai[k4 + 1536 + 1] = k3;
						ai[k4 + 1536 + 2] = k3;
						ai[k4 + 1536 + 3] = k3;
					}
			}
		}
		k1 = worldController.method302(j1, l, i);
		if (k1 != 0) {
			int i2 = worldController.method304(j1, l, i, k1);
			int l2 = i2 >> 6 & 3;
		int j3 = i2 & 0x1f;
		int l3 = k1 >> 14 & 0x7fff;
		ObjectDef class46_1 = ObjectDef.forID(l3);
		if (class46_1.anInt758 != -1) {
			Background background_1 = mapScenes[class46_1.anInt758];
			if (background_1 != null) {
				int j5 = (class46_1.anInt744 * 4 - background_1.anInt1452) / 2;
				int k5 = (class46_1.anInt761 * 4 - background_1.anInt1453) / 2;
				background_1.drawBackground(48 + l * 4 + j5, 48 + (104 - i - class46_1.anInt761) * 4 + k5);
			}
		} else if (j3 == 9) {
			int l4 = 0xeeeeee;
			if (k1 > 0)
				l4 = 0xee0000;
			int ai1[] = minimapImage.myPixels;
			int l5 = 24624 + l * 4 + (103 - i) * 512 * 4;
			if (l2 == 0 || l2 == 2) {
				ai1[l5 + 1536] = l4;
				ai1[l5 + 1024 + 1] = l4;
				ai1[l5 + 512 + 2] = l4;
				ai1[l5 + 3] = l4;
			} else {
				ai1[l5] = l4;
				ai1[l5 + 512 + 1] = l4;
				ai1[l5 + 1024 + 2] = l4;
				ai1[l5 + 1536 + 3] = l4;
			}
		}
		}
		k1 = worldController.method303(j1, l, i);
		if (k1 != 0) {
			int j2 = k1 >> 14 & 0x7fff;
			ObjectDef class46 = ObjectDef.forID(j2);
			if (class46.anInt758 != -1) {
				Background background = mapScenes[class46.anInt758];
				if (background != null) {
					int i4 = (class46.anInt744 * 4 - background.anInt1452) / 2;
					int j4 = (class46.anInt761 * 4 - background.anInt1453) / 2;
					background.drawBackground(48 + l * 4 + i4, 48 + (104 - i - class46.anInt761) * 4 + j4);
				}
			}
		}
	}

	private void loadTitleScreen() {
		aBackground_966 = new Background(titleStreamLoader, "titlebox", 0);
		aBackground_967 = new Background(titleStreamLoader, "titlebutton", 0);
		aBackgroundArray1152s = new Background[12];
		int j = 0;
		try {
			j = Integer.parseInt(getParameter("fl_icon"));
		} catch (Exception _ex) {
		}
		if (j == 0) {
			for (int k = 0; k < 12; k++)
				aBackgroundArray1152s[k] = new Background(titleStreamLoader, "runes", k);

		} else {
			for (int l = 0; l < 12; l++)
				aBackgroundArray1152s[l] = new Background(titleStreamLoader, "runes", 12 + (l & 3));

		}
		aClass30_Sub2_Sub1_Sub1_1201 = new Sprite(128, 265);
		aClass30_Sub2_Sub1_Sub1_1202 = new Sprite(128, 265);
		System.arraycopy(aRSImageProducer_1110.anIntArray315, 0, aClass30_Sub2_Sub1_Sub1_1201.myPixels, 0, 33920);

		System.arraycopy(aRSImageProducer_1111.anIntArray315, 0, aClass30_Sub2_Sub1_Sub1_1202.myPixels, 0, 33920);

		anIntArray851 = new int[256];
		for (int k1 = 0; k1 < 64; k1++)
			anIntArray851[k1] = k1 * 0x40000;

		for (int l1 = 0; l1 < 64; l1++)
			anIntArray851[l1 + 64] = 0xff0000 + 1024 * l1;

		for (int i2 = 0; i2 < 64; i2++)
			anIntArray851[i2 + 128] = 0xffff00 + 4 * i2;

		for (int j2 = 0; j2 < 64; j2++)
			anIntArray851[j2 + 192] = 0xffffff;

		anIntArray852 = new int[256];
		for (int k2 = 0; k2 < 64; k2++)
			anIntArray852[k2] = k2 * 1024;

		for (int l2 = 0; l2 < 64; l2++)
			anIntArray852[l2 + 64] = 65280 + 4 * l2;

		for (int i3 = 0; i3 < 64; i3++)
			anIntArray852[i3 + 128] = 65535 + 0x40000 * i3;

		for (int j3 = 0; j3 < 64; j3++)
			anIntArray852[j3 + 192] = 0xffffff;

		anIntArray853 = new int[256];
		for (int k3 = 0; k3 < 64; k3++)
			anIntArray853[k3] = k3 * 4;

		for (int l3 = 0; l3 < 64; l3++)
			anIntArray853[l3 + 64] = 255 + 0x40000 * l3;

		for (int i4 = 0; i4 < 64; i4++)
			anIntArray853[i4 + 128] = 0xff00ff + 1024 * i4;

		for (int j4 = 0; j4 < 64; j4++)
			anIntArray853[j4 + 192] = 0xffffff;

		anIntArray850 = new int[256];
		anIntArray1190 = new int[32768];
		anIntArray1191 = new int[32768];
		randomizeBackground(null);
		anIntArray828 = new int[32768];
		anIntArray829 = new int[32768];
		drawLoadingText(10, "Connecting to fileserver");
		if (!aBoolean831) {
			drawFlames = true;
			aBoolean831 = true;
			startRunnable(this, 2);
		}
	}

	private static void setHighMem() {
		WorldController.lowMem = false;
		Texture.lowMem = false;
		lowMem = false;
		ObjectManager.lowMem = false;
		ObjectDef.lowMem = false;
	}
	
	public static Client getClient() {
		return instance;
	} 

	public static void main(String args[]) {
		try {
			nodeID = 10;
			portOff = 0;
			setHighMem();
			isMembers = true;
			Signlink.storeid = 32;
			Signlink.startpriv(InetAddress.getLocalHost());
			clientSize = 0;
			instance = new Client();
			instance.createClientFrame(clientWidth, clientHeight);
		} catch (Exception exception) { }
	}

	public static Client instance;

	private void loadingStages() {
		if (lowMem && loadingStage == 2 && ObjectManager.anInt131 != plane) {
			aRSImageProducer_1165.initDrawingArea();
			aTextDrawingArea_1271.drawText(0, "Loading - please wait.", 151, 257);
			aTextDrawingArea_1271.drawText(0xffffff, "Loading - please wait.", 150, 256);
			aRSImageProducer_1165.drawGraphics(clientSize == 0 ? 4 : 0, super.graphics, clientSize == 0 ? 4 : 0);
			loadingStage = 1;
			aLong824 = System.currentTimeMillis();
		}
		if (loadingStage == 1) {
			int j = method54();
			if (j != 0 && System.currentTimeMillis() - aLong824 > 0x57e40L) {
				Signlink.reporterror(myUsername + " glcfb " + aLong1215 + "," + j + "," + lowMem + "," + decompressors[0] + "," + onDemandFetcher.getNodeCount() + "," + plane + "," + anInt1069 + "," + anInt1070);
				aLong824 = System.currentTimeMillis();
			}
		}
		if (loadingStage == 2 && plane != anInt985) {
			anInt985 = plane;
			method24(plane);
		}
	}

	private int method54() {
		for (int i = 0; i < aByteArrayArray1183.length; i++) {
			if (aByteArrayArray1183[i] == null && anIntArray1235[i] != -1)
				return -1;
			if (aByteArrayArray1247[i] == null && anIntArray1236[i] != -1)
				return -2;
		}
		boolean flag = true;
		for (int j = 0; j < aByteArrayArray1183.length; j++) {
			byte abyte0[] = aByteArrayArray1247[j];
			if (abyte0 != null) {
				int k = (anIntArray1234[j] >> 8) * 64 - baseX;
				int l = (anIntArray1234[j] & 0xff) * 64 - baseY;
				if (aBoolean1159) {
					k = 10;
					l = 10;
				}
				flag &= ObjectManager.method189(k, abyte0, l);
			}
		}
		if (!flag)
			return -3;
		if (aBoolean1080) {
			return -4;
		} else {
			loadingStage = 2;
			ObjectManager.anInt131 = plane;
			method22();
			stream.createFrame(121);
			return 0;
		}
	}

	private void method55() {
		for (Animable_Sub4 class30_sub2_sub4_sub4 = (Animable_Sub4) aClass19_1013.reverseGetFirst(); class30_sub2_sub4_sub4 != null; class30_sub2_sub4_sub4 = (Animable_Sub4) aClass19_1013.reverseGetNext())
			if (class30_sub2_sub4_sub4.anInt1597 != plane || loopCycle > class30_sub2_sub4_sub4.anInt1572)
				class30_sub2_sub4_sub4.unlink();
			else if (loopCycle >= class30_sub2_sub4_sub4.anInt1571) {
				if (class30_sub2_sub4_sub4.anInt1590 > 0) {
					NPC npc = npcArray[class30_sub2_sub4_sub4.anInt1590 - 1];
					if (npc != null && npc.x >= 0 && npc.x < 13312 && npc.y >= 0 && npc.y < 13312)
						class30_sub2_sub4_sub4.method455(loopCycle, npc.y, method42(class30_sub2_sub4_sub4.anInt1597, npc.y, npc.x) - class30_sub2_sub4_sub4.anInt1583, npc.x);
				}
				if (class30_sub2_sub4_sub4.anInt1590 < 0) {
					int j = -class30_sub2_sub4_sub4.anInt1590 - 1;
					Player player;
					if (j == unknownInt10)
						player = myPlayer;
					else
						player = playerArray[j];
					if (player != null && player.x >= 0 && player.x < 13312 && player.y >= 0 && player.y < 13312)
						class30_sub2_sub4_sub4.method455(loopCycle, player.y, method42(class30_sub2_sub4_sub4.anInt1597, player.y, player.x) - class30_sub2_sub4_sub4.anInt1583, player.x);
				}
				class30_sub2_sub4_sub4.method456(anInt945);
				worldController.method285(plane, class30_sub2_sub4_sub4.anInt1595, (int) class30_sub2_sub4_sub4.aDouble1587, -1, (int) class30_sub2_sub4_sub4.aDouble1586, 60, (int) class30_sub2_sub4_sub4.aDouble1585, class30_sub2_sub4_sub4, false);
			}

	}

	public AppletContext getAppletContext() {
		if (Signlink.mainapp != null)
			return Signlink.mainapp.getAppletContext();
		else
			return super.getAppletContext();
	}

	private void drawLogo() {
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
		for (int j = 0; j < sprite.myHeight; j++) {
			for (int k = 0; k < sprite.myWidth; k++)
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
		sprite = new Sprite(titleStreamLoader, "logo", 0);
		aRSImageProducer_1107.initDrawingArea();
		sprite.drawSprite(382 - sprite.myWidth / 2 - 128, 18);
		sprite = null;
		System.gc();
	}

	private void saveMidi(boolean flag, byte abyte0[])
	{
		Signlink.midifade = flag ? 1 : 0;
		Signlink.saveMidi(abyte0, abyte0.length);
	}
	
	private void processOnDemandQueue() {
		do {
			OnDemandData onDemandData;
			do {
				onDemandData = onDemandFetcher.getNextNode();
				if (onDemandData == null)
					return;
				if (onDemandData.dataType == 0) {
					Model.method460(onDemandData.buffer, onDemandData.ID);
					if (backDialogID != -1)
						inputTaken = true;
				}
				if(onDemandData.dataType == 1 && onDemandData.buffer != null)
					Class36.load(onDemandData.buffer, onDemandData.ID);
				if (onDemandData.dataType == 2 && onDemandData.ID == nextSong && onDemandData.buffer != null && MusicEnabled){
					MidiPlayer.getSingleton().play(onDemandData.buffer,songChanging,musicVolume);
					//saveMidi(songChanging, onDemandData.buffer);
				}
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
/*
	private void calcFlamesPosition() {
		char c = '\u0100';
		for (int j = 10; j < 117; j++) {
			int k = (int) (Math.random() * 100D);
			if (k < 50)
				anIntArray828[j + (c - 2 << 7)] = 255;
		}
		for (int l = 0; l < 100; l++) {
			int i1 = (int) (Math.random() * 124D) + 2;
			int k1 = (int) (Math.random() * 128D) + 128;
			int k2 = i1 + (k1 << 7);
			anIntArray828[k2] = 192;
		}

		for (int j1 = 1; j1 < c - 1; j1++) {
			for (int l1 = 1; l1 < 127; l1++) {
				int l2 = l1 + (j1 << 7);
				anIntArray829[l2] = (anIntArray828[l2 - 1] + anIntArray828[l2 + 1] + anIntArray828[l2 - 128] + anIntArray828[l2 + 128]) / 4;
			}

		}

		anInt1275 += 128;
		if (anInt1275 > anIntArray1190.length) {
			anInt1275 -= anIntArray1190.length;
			int i2 = (int) (Math.random() * 12D);
			randomizeBackground(aBackgroundArray1152s[i2]);
		}
		for (int j2 = 1; j2 < c - 1; j2++) {
			for (int i3 = 1; i3 < 127; i3++) {
				int k3 = i3 + (j2 << 7);
				int i4 = anIntArray829[k3 + 128] - anIntArray1190[k3 + anInt1275 & anIntArray1190.length - 1] / 5;
				if (i4 < 0)
					i4 = 0;
				anIntArray828[k3] = i4;
			}

		}

		System.arraycopy(anIntArray969, 1, anIntArray969, 0, c - 1);

		anIntArray969[c - 1] = (int) (Math.sin((double) loopCycle / 14D) * 16D + Math.sin((double) loopCycle / 15D) * 14D + Math.sin((double) loopCycle / 16D) * 12D);
		if (anInt1040 > 0)
			anInt1040 -= 4;
		if (anInt1041 > 0)
			anInt1041 -= 4;
		if (anInt1040 == 0 && anInt1041 == 0) {
			int l3 = (int) (Math.random() * 2000D);
			if (l3 == 0)
				anInt1040 = 1024;
			if (l3 == 1)
				anInt1041 = 1024;
		}
	}
*/
	private void method60(int i) {
		RSInterface class9 = RSInterface.interfaceCache[i];
		for (int j = 0; j < class9.children.length; j++) {
			if (class9.children[j] == -1)
				break;
			RSInterface class9_1 = RSInterface.interfaceCache[class9.children[j]];
			if (class9_1.type == 1)
				method60(class9_1.id);
			class9_1.anInt246 = 0;
			class9_1.anInt208 = 0;
		}
	}

	private void drawHeadIcon() {
		if (anInt855 != 2)
			return;
		calcEntityScreenPos((anInt934 - baseX << 7) + anInt937, anInt936 * 2, (anInt935 - baseY << 7) + anInt938);
		if (spriteDrawX > -1 && loopCycle % 20 < 10)
			headIconsHint[0].drawSprite(spriteDrawX - 12, spriteDrawY - 28);
	}


	public void sendString(String str, int i)
	{
		RSInterface.interfaceCache[i].message = str;
		needDrawTabArea = true;
		inputTaken = true;
	}

	private void mainGameProcessor()
	{
		checkSize();
		if(anInt1104 > 1)
			anInt1104--;
		if(anInt1011 > 0)
			anInt1011--;
		for(int j = 0; j < 5; j++)
			if(!parsePacket())
				break;

		if(!loggedIn)
			return;
		
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
				if(atInventoryInterfaceType == 2) {
				}
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
				if(activeInterfaceType == 2) {
				}
				if(activeInterfaceType == 3)
					inputTaken = true;
				activeInterfaceType = 0;
				if(aBoolean1242 && anInt989 >= 15)
				{
					lastActiveInvInterface = -1;
					processRightClick();
					processBankClick();
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
		if(super.clickMode2 == 1 || super.clickMode3 == 1)
			anInt1213++;
		if (anInt1500 != 0 || anInt1044 != 0 || anInt1129 != 0) {
			if (anInt1501 < 0 && !menuOpen) {
				anInt1501++;
				if (anInt1501 == 0) {
					if (anInt1500 != 0) {
						inputTaken = true;
					}
					if (anInt1044 != 0) {
					}
				}
			}
		} else if (anInt1501 > 0) {
			anInt1501--;
		}
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

	private void method63() {
		Class30_Sub1 class30_sub1 = (Class30_Sub1) aClass19_1179.reverseGetFirst();
		for (; class30_sub1 != null; class30_sub1 = (Class30_Sub1) aClass19_1179.reverseGetNext())
			if (class30_sub1.anInt1294 == -1) {
				class30_sub1.anInt1302 = 0;
				method89(class30_sub1);
			} else {
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
        if (titleStreamLoader == null) {
            super.drawLoadingText(i, s);
            return;
        }
      //  aRSImageProducer_1109.initDrawingArea();
       // DrawingArea.drawFilledPixels(0, 0, clientWidth, clientHeight, 0);
        refreshClientScreen();
        int centerX = clientWidth / 2, centerY = clientHeight / 2;
        byte byte1 = 20;
        chatTextDrawingArea.method382(0xffffff, centerX, "vscape is loading - please wait...", centerY - 23 - byte1, true);
        int j = centerY - 18 - byte1;
        DrawingArea.fillPixels(centerX - 152, 304, 34, 0x8c1111, j);
        DrawingArea.fillPixels(centerX - 151, 302, 32, 0, j + 1);
        DrawingArea.drawPixels(30, j + 2, centerX - 150, 0x8c1111, i * 3);
        DrawingArea.drawPixels(30, j + 2, (centerX - 150) + i * 3, 0, 300 - i * 3);
        chatTextDrawingArea.drawText(0xffffff, s, (centerY + 4) - byte1, centerX);
        aRSImageProducer_1109.drawGraphics(0, super.graphics, 0);
        if (welcomeScreenRaised)
            welcomeScreenRaised = false;
	/*	anInt1079 = i;
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
		chatTextDrawingArea.drawText(0xffffff, "/v/scape is loading - please wait...", c1 / 2 - 26 - byte1, c / 2);
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
		}*/
	}

	public void moveScroller(int i, int j, int k, int l, RSInterface class9, int i1, boolean flag, int j1)
    {
        int anInt992;
        if (aBoolean972)
            anInt992 = 32;
        else
            anInt992 = 0;
        aBoolean972 = false;
        if (k >= i && k < i + 16 && l >= i1 && l < i1 + 16)
        {
            class9.scrollPosition -= anInt1213 * 4;
            if (flag)
            {
                needDrawTabArea = true;
            }
        }
        else if (k >= i && k < i + 16 && l >= (i1 + j) - 16 && l < i1 + j)
        {
            class9.scrollPosition += anInt1213 * 4;
            if (flag)
            {
                needDrawTabArea = true;
            }
            // free scrolling
        }
        else if (k >= i - anInt992 && k < i + 16 + anInt992 && l >= i1 + 16 && l < (i1 + j) - 16 && anInt1213 > 0)
        {
            int l1 = ((j - 32) * j) / j1;
            if (l1 < 8)
                l1 = 8;
            int i2 = l - i1 - 16 - l1 / 2;
            int j2 = j - 32 - l1;
            class9.scrollPosition = (short) (((j1 - j) * i2) / j2);
            if (flag)
                needDrawTabArea = true;
            aBoolean972 = true;
        }
    }

	private boolean method66(int i, int j, int k) {
		int i1 = i >> 14 & 0x7fff;
					int j1 = worldController.method304(plane, k, j, i);
					if (j1 == -1)
						return false;
					int k1 = j1 & 0x1f;
					int l1 = j1 >> 6 & 3;
								if (k1 == 10 || k1 == 11 || k1 == 22) {
									ObjectDef class46 = ObjectDef.forID(i1);
									int i2;
									int j2;
									if (l1 == 0 || l1 == 2) {
										i2 = class46.anInt744;
										j2 = class46.anInt761;
									} else {
										i2 = class46.anInt761;
										j2 = class46.anInt744;
									}
									int k2 = class46.anInt768;
									if (l1 != 0)
										k2 = (k2 << l1 & 0xf) + (k2 >> 4 - l1);
									doWalkTo(2, 0, j2, 0, myPlayer.smallY[0], i2, k2, j, myPlayer.smallX[0], false, k);
								} else {
									doWalkTo(2, l1, 0, k1 + 1, myPlayer.smallY[0], 0, 0, j, myPlayer.smallX[0], false, k);
								}
								crossX = super.saveClickX;
								crossY = super.saveClickY;
								crossType = 2;
								crossIndex = 0;
								return true;
	}

	private StreamLoader streamLoaderForName(int i, String s, String s1, int j, int k) {
		byte abyte0[] = null;
		int l = 5;
		try {
			if (decompressors[0] != null)
				abyte0 = decompressors[0].decompress(i);
		} catch (Exception _ex) {
		}
		if (abyte0 != null) {
			// aCRC32_930.reset();
			// aCRC32_930.update(abyte0);
			// int i1 = (int)aCRC32_930.getValue();
			// if(i1 != j)
		}
		if (abyte0 != null) {
			StreamLoader streamLoader = new StreamLoader(abyte0);
			return streamLoader;
		}
		int j1 = 0;
		while (abyte0 == null) {
			String s2 = "Unknown error";
			drawLoadingText(k, "Requesting " + s);
			try {
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

				while (j2 < i2) {
					int l2 = i2 - j2;
					if (l2 > 1000)
						l2 = 1000;
					int j3 = datainputstream.read(abyte0, j2, l2);
					if (j3 < 0) {
						s2 = "Length error: " + j2 + "/" + i2;
						throw new IOException("EOF");
					}
					j2 += j3;
					int k3 = (j2 * 100) / i2;
					if (k3 != k1)
						drawLoadingText(k, "Loading " + s + " - " + k3 + "%");
					k1 = k3;
				}
				datainputstream.close();
				try {
					if (decompressors[0] != null)
						decompressors[0].method234(abyte0.length, abyte0, i);
				} catch (Exception _ex) {
					decompressors[0] = null;
				}
				/*
				 * if(abyte0 != null) { aCRC32_930.reset();
				 * aCRC32_930.update(abyte0); int i3 =
				 * (int)aCRC32_930.getValue(); if(i3 != j) { abyte0 = null;
				 * j1++; s2 = "Checksum error: " + i3; } }
				 */
			} catch (IOException ioexception) {
				if (s2.equals("Unknown error"))
					s2 = "Connection error";
				abyte0 = null;
			} catch (NullPointerException _ex) {
				s2 = "Null error";
				abyte0 = null;
				if (!Signlink.reporterror)
					return null;
			} catch (ArrayIndexOutOfBoundsException _ex) {
				s2 = "Bounds error";
				abyte0 = null;
				if (!Signlink.reporterror)
					return null;
			} catch (Exception _ex) {
				s2 = "Unexpected error";
				abyte0 = null;
				if (!Signlink.reporterror)
					return null;
			}
			if (abyte0 == null) {
				for (int l1 = l; l1 > 0; l1--) {
					if (j1 >= 3) {
						drawLoadingText(k, "Game updated - please reload page");
						l1 = 10;
					} else {
						drawLoadingText(k, s2 + " - Retrying in " + l1);
					}
					try {
						Thread.sleep(1000L);
					} catch (Exception _ex) {
					}
				}

				l *= 2;
				if (l > 60)
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

	public void setNorth() {
		anInt1278 = 0;
		anInt1131 = 0;
		anInt896 = 0;
		minimapInt1 = 0;
		minimapInt2 = 0;
		minimapInt3 = 0;
	}

	private void doAction(int i) {
		if (i < 0)
			return;
		if (inputDialogState != 0) {
			inputDialogState = 0;
			inputTaken = true;
		}
		int j = menuActionCmd2[i];
		int k = menuActionCmd3[i];
		int l = menuActionID[i];
		int i1 = menuActionCmd1[i];
		if (l >= 2000)
			l -= 2000;
		if (l == 696) {
			setNorth();
		}
		if (l == 104) {
			RSInterface class9_1 = RSInterface.interfaceCache[k];
			spellID = class9_1.id;
			if (!autocast) {
				autocast = true;
				autoCastId = class9_1.id;
				stream.createFrame(185);
				stream.writeWord(class9_1.id);
			} else if (autoCastId == class9_1.id) {
				autocast = false;
				autoCastId = 0;
				stream.createFrame(185);
				stream.writeWord(class9_1.id);
			} else if (autoCastId != class9_1.id) {
				autocast = true;
				autoCastId = class9_1.id;
				stream.createFrame(185);
				stream.writeWord(class9_1.id);
			}
		}
		if (l == 582) {
			NPC npc = npcArray[i1];
			if (npc != null) {
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
		if (l == 234) {
			boolean flag1 = doWalkTo(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, k, myPlayer.smallX[0], false, j);
			if (!flag1)
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
		if (l == 62 && method66(i1, k, j)) {
			stream.createFrame(192);
			stream.writeWord(anInt1284);
			stream.method431(i1 >> 14 & 0x7fff);
			stream.method433(k + baseY);
			stream.method431(anInt1283);
			stream.method433(j + baseX);
			stream.writeWord(anInt1285);
		}
		if (l == 511) {
			boolean flag2 = doWalkTo(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, k, myPlayer.smallX[0], false, j);
			if (!flag2)
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
		if (l == 74) {
			stream.createFrame(122);
			stream.method433(k);
			stream.method432(j);
			stream.method431(i1);
			atInventoryLoopCycle = 0;
			atInventoryInterface = k;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[k].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[k].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (l == 315) {
			RSInterface class9 = RSInterface.interfaceCache[k];
			boolean flag8 = true;
			if (class9.contentType > 0)
				flag8 = promptUserForInput(class9);
			if (flag8) {

				switch (k) {
			/*	case 15201: //equipment interface
					openInterfaceID = 15106;
					break;*/
				case 36004:
					toggleSize(0);
					break;
				case 36005:
					toggleSize(1);
					break;
				case 36006: 
					toggleSize(2);
					break;
				/*case 21341:
					sendFrame248(21172, 3213);
					method60(21172);
					inputTaken = true;
					break;*/
				default:
					stream.createFrame(185);
					stream.writeWord(k);
					break;

				}
			}
		}
		if (l == 561) {
			Player player = playerArray[i1];
			if (player != null) {
				doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, player.smallY[0], myPlayer.smallX[0], false, player.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				anInt1188 += i1;
				if (anInt1188 >= 90) {
					stream.createFrame(136);
					anInt1188 = 0;
				}
				stream.createFrame(128);
				stream.writeWord(i1);
			}
		}
		if (l == 20) {
			NPC class30_sub2_sub4_sub1_sub1_1 = npcArray[i1];
			if (class30_sub2_sub4_sub1_sub1_1 != null) {
				doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub1_1.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub1_1.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(155);
				stream.method431(i1);
			}
		}
		if (l == 779) {
			Player class30_sub2_sub4_sub1_sub2_1 = playerArray[i1];
			if (class30_sub2_sub4_sub1_sub2_1 != null) {
				doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub2_1.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub2_1.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(153);
				stream.method431(i1);
			}
		}
		if (l == 519)
			if (!menuOpen)
				worldController.method312(super.saveClickY - 4, super.saveClickX - 4);
			else
				worldController.method312(k - 4, j - 4);
		if (l == 1062) {
			anInt924 += baseX;
			if (anInt924 >= 113) {
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
		if (l == 679 && !aBoolean1149) {
			stream.createFrame(40);
			stream.writeWord(k);
			aBoolean1149 = true;
		}
		if (l == 431) {
			stream.createFrame(129);
			stream.method432(j);
			stream.writeWord(k);
			stream.method432(i1);
			atInventoryLoopCycle = 0;
			atInventoryInterface = k;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[k].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[k].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (l == 337 || l == 42 || l == 792 || l == 322) {
			String s = menuActionName[i];
			int k1 = s.indexOf("@whi@");
			if (k1 != -1) {
				long l3 = TextClass.longForName(s.substring(k1 + 5).trim());
				if (l == 337)
					addFriend(l3);
				if (l == 42)
					addIgnore(l3);
				if (l == 792)
					delFriend(l3);
				if (l == 322)
					delIgnore(l3);
			}
		}
		if (l == 53) {
			stream.createFrame(135);
			stream.method431(j);
			stream.method432(k);
			stream.method431(i1);
			atInventoryLoopCycle = 0;
			atInventoryInterface = k;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[k].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[k].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (l == 539) {
			stream.createFrame(16);
			stream.method432(i1);
			stream.method433(j);
			stream.method433(k);
			atInventoryLoopCycle = 0;
			atInventoryInterface = k;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[k].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[k].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (l == 484 || l == 6) {
			String s1 = menuActionName[i];
			int l1 = s1.indexOf("@whi@");
			if (l1 != -1) {
				s1 = s1.substring(l1 + 5).trim();
				String s7 = TextClass.fixName(TextClass.nameForLong(TextClass.longForName(s1)));
				boolean flag9 = false;
				for (int j3 = 0; j3 < playerCount; j3++) {
					Player class30_sub2_sub4_sub1_sub2_7 = playerArray[playerIndices[j3]];
					if (class30_sub2_sub4_sub1_sub2_7 == null || class30_sub2_sub4_sub1_sub2_7.name == null || !class30_sub2_sub4_sub1_sub2_7.name.equalsIgnoreCase(s7))
						continue;
					doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub2_7.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub2_7.smallX[0]);
					if (l == 484) {
						stream.createFrame(139);
						stream.method431(playerIndices[j3]);
					}
					if (l == 6) {
						anInt1188 += i1;
						if (anInt1188 >= 90) {
							stream.createFrame(136);
							anInt1188 = 0;
						}
						stream.createFrame(128);
						stream.writeWord(playerIndices[j3]);
					}
					flag9 = true;
					break;
				}

				if (!flag9)
					pushMessage("Unable to find " + s7, 0, "");
			}
		}
		if (l == 870) {
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
			if (RSInterface.interfaceCache[k].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[k].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (l == 847) {
			stream.createFrame(87);
			stream.method432(i1);
			stream.writeWord(k);
			stream.method432(j);
			atInventoryLoopCycle = 0;
			atInventoryInterface = k;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[k].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[k].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (l == 626) {
			RSInterface class9_1 = RSInterface.interfaceCache[k];
			spellSelected = 1;
			spellID = class9_1.id;
			anInt1137 = k;
			spellUsableOn = class9_1.spellUsableOn;
			itemSelected = 0;
			needDrawTabArea = true;
			String s4 = class9_1.selectedActionName;
			if (s4.indexOf(" ") != -1)
				s4 = s4.substring(0, s4.indexOf(" "));
			String s8 = class9_1.selectedActionName;
			if (s8.indexOf(" ") != -1)
				s8 = s8.substring(s8.indexOf(" ") + 1);
			spellTooltip = s4 + " " + class9_1.spellName + " " + s8;
			if (spellUsableOn == 16) {
				tabID = 3;
				tabAreaAltered = true;
			}
			return;
		}
		if (l == 78) {
			stream.createFrame(117);
			stream.method433(k);
			stream.method433(i1);
			stream.method431(j);
			atInventoryLoopCycle = 0;
			atInventoryInterface = k;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[k].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[k].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (l == 27) {
			Player class30_sub2_sub4_sub1_sub2_2 = playerArray[i1];
			if (class30_sub2_sub4_sub1_sub2_2 != null) {
				doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub2_2.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub2_2.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				anInt986 += i1;
				if (anInt986 >= 54) {
					stream.createFrame(189);
					stream.writeWordBigEndian(234);
					anInt986 = 0;
				}
				stream.createFrame(73);
				stream.method431(i1);
			}
		}
		if (l == 213) {
			boolean flag3 = doWalkTo(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, k, myPlayer.smallX[0], false, j);
			if (!flag3)
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
		if (l == 632) {
			stream.createFrame(145);
			stream.method432(k);
			stream.method432(j);
			stream.method432(i1);
			atInventoryLoopCycle = 0;
			atInventoryInterface = k;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[k].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[k].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if(l == 1008) {
			globalMode = 2;
			inputTaken = true;
		}
		if(l == 1007) {
			globalMode = 1;
			inputTaken = true;
		}
		if(l == 1006) {
			globalMode = 0;
			inputTaken = true;
		}
		if(l == 1005) {
			cButtonCPos = 0;
			chatTypeView = 9;
			inputTaken = true;
		}		
		if (l == 1004) {
			if (tabInterfaceIDs[10] != -1) {
				needDrawTabArea = true;
				tabID = 10;
				tabAreaAltered = true;
			}
		}
		if (l == 1003) {
			clanChatMode = 2;
			inputTaken = true;
		}
		if (l == 1002) {
			clanChatMode = 1;
			inputTaken = true;
		}
		if (l == 1001) {
			gameMode = 1;
			inputTaken = true;
		}
		if (l == 1000) {
			gameMode = 0;
			inputTaken = true;
		}
		if (l == 999) {
			cButtonCPos = 0;
			chatTypeView = 0;
			inputTaken = true;
		}
		if (l == 998) {
			cButtonCPos = 1;
			chatTypeView = 5;
			inputTaken = true;
		}
		if (l == 997) {
			publicChatMode = 3;
			inputTaken = true;
		}
		if (l == 996) {
			publicChatMode = 2;
			inputTaken = true;
		}
		if (l == 995) {
			publicChatMode = 1;
			inputTaken = true;
		}
		if (l == 994) {
			publicChatMode = 0;
			inputTaken = true;
		}
		if (l == 993) {
			cButtonCPos = 2;
			chatTypeView = 1;
			inputTaken = true;
		}
		if (l == 992) {
			privateChatMode = 2;
			inputTaken = true;
		}
		if (l == 991) {
			privateChatMode = 1;
			inputTaken = true;
		}
		if (l == 990) {
			privateChatMode = 0;
			inputTaken = true;
		}
		if (l == 989) {
			cButtonCPos = 3;
			chatTypeView = 2;
			inputTaken = true;
		}
		if (l == 987) {
			tradeMode = 2;
			inputTaken = true;
		}
		if (l == 986) {
			tradeMode = 1;
			inputTaken = true;
		}
		if (l == 985) {
			tradeMode = 0;
			inputTaken = true;
		}
		if (l == 984) {
			cButtonCPos = 5;
			chatTypeView = 3;
			inputTaken = true;
		}
		if (l == 983) {
			duelMode = 2;
			inputTaken = true;
		}
		if (l == 982) {
			duelMode = 1;
			inputTaken = true;
		}
		if (l == 981) {
			duelMode = 0;
			inputTaken = true;
		}
		if (l == 980) {
			cButtonCPos = 6;
			chatTypeView = 4;
			inputTaken = true;
		}
		if (l == 493) {
			stream.createFrame(75);
			stream.method433(k);
			stream.method431(j);
			stream.method432(i1);
			atInventoryLoopCycle = 0;
			atInventoryInterface = k;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[k].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[k].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (l == 652) {
			boolean flag4 = doWalkTo(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, k, myPlayer.smallX[0], false, j);
			if (!flag4)
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
		if (l == 94) {
			boolean flag5 = doWalkTo(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, k, myPlayer.smallX[0], false, j);
			if (!flag5)
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
		if (l == 646) {
			stream.createFrame(185);
			stream.writeWord(k);
			RSInterface class9_2 = RSInterface.interfaceCache[k];
			if (class9_2.valueIndexArray != null && class9_2.valueIndexArray[0][0] == 5) {
				int i2 = class9_2.valueIndexArray[0][1];
				if (variousSettings[i2] != class9_2.anIntArray212[0]) {
					variousSettings[i2] = class9_2.anIntArray212[0];
					method33(i2);
					needDrawTabArea = true;
				}
			}
		}
		if (l == 225) {
			NPC class30_sub2_sub4_sub1_sub1_2 = npcArray[i1];
			if (class30_sub2_sub4_sub1_sub1_2 != null) {
				doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub1_2.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub1_2.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				anInt1226 += i1;
				if (anInt1226 >= 85) {
					stream.createFrame(230);
					stream.writeWordBigEndian(239);
					anInt1226 = 0;
				}
				stream.createFrame(17);
				stream.method433(i1);
			}
		}
		if (l == 965) {
			NPC class30_sub2_sub4_sub1_sub1_3 = npcArray[i1];
			if (class30_sub2_sub4_sub1_sub1_3 != null) {
				doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub1_3.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub1_3.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				anInt1134++;
				if (anInt1134 >= 96) {
					stream.createFrame(152);
					stream.writeWordBigEndian(88);
					anInt1134 = 0;
				}
				stream.createFrame(21);
				stream.writeWord(i1);
			}
		}
		if (l == 413) {
			NPC class30_sub2_sub4_sub1_sub1_4 = npcArray[i1];
			if (class30_sub2_sub4_sub1_sub1_4 != null) {
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
		if (l == 200)
			clearTopInterfaces();
		if (l == 1025) {
		/*	NPC class30_sub2_sub4_sub1_sub1_5 = npcArray[i1];
			if (class30_sub2_sub4_sub1_sub1_5 != null) {
				EntityDef entityDef = class30_sub2_sub4_sub1_sub1_5.desc;
				if (entityDef.childrenIDs != null)
					entityDef = entityDef.method161();
				if (entityDef != null) {
					String s9;
					if (entityDef.description != null)
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
		if (l == 900) {
			method66(i1, k, j);
			stream.createFrame(252);
			stream.method433(i1 >> 14 & 0x7fff);
			stream.method431(k + baseY);
			stream.method432(j + baseX);
		}
		if (l == 412) {
			NPC class30_sub2_sub4_sub1_sub1_6 = npcArray[i1];
			if (class30_sub2_sub4_sub1_sub1_6 != null) {
				doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub1_6.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub1_6.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(72);
				stream.method432(i1);
			}
		}
		if (l == 365) {
			Player class30_sub2_sub4_sub1_sub2_3 = playerArray[i1];
			if (class30_sub2_sub4_sub1_sub2_3 != null) {
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
		if (l == 729) {
			Player class30_sub2_sub4_sub1_sub2_4 = playerArray[i1];
			if (class30_sub2_sub4_sub1_sub2_4 != null) {
				doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub2_4.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub2_4.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(39);
				stream.method431(i1);
			}
		}
		if (l == 577) {
			Player class30_sub2_sub4_sub1_sub2_5 = playerArray[i1];
			if (class30_sub2_sub4_sub1_sub2_5 != null) {
				doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub2_5.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub2_5.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(139);
				stream.method431(i1);
			}
		}
		if (l == 956 && method66(i1, k, j)) {
			stream.createFrame(35);
			stream.method431(j + baseX);
			stream.method432(anInt1137);
			stream.method432(k + baseY);
			stream.method431(i1 >> 14 & 0x7fff);
		}
		if (l == 567) {
			boolean flag6 = doWalkTo(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, k, myPlayer.smallX[0], false, j);
			if (!flag6)
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
		if (l == 867) {
			if ((i1 & 3) == 0)
				anInt1175++;
			if (anInt1175 >= 59) {
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
			if (RSInterface.interfaceCache[k].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[k].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (l == 543) {
			stream.createFrame(237);
			stream.writeWord(j);
			stream.method432(i1);
			stream.writeWord(k);
			stream.method432(anInt1137);
			atInventoryLoopCycle = 0;
			atInventoryInterface = k;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[k].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[k].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (l == 606) {
			String s2 = menuActionName[i];
			int j2 = s2.indexOf("@whi@");
			if (j2 != -1)
				if (openInterfaceID == -1) {
					clearTopInterfaces();
					reportAbuseInput = s2.substring(j2 + 5).trim();
					canMute = false;
					for (int i3 = 0; i3 < RSInterface.interfaceCache.length; i3++) {
						if (RSInterface.interfaceCache[i3] == null || RSInterface.interfaceCache[i3].contentType != 600)
							continue;
						reportAbuseInterfaceID = openInterfaceID = RSInterface.interfaceCache[i3].parentID;
						break;
					}

				} else {
					pushMessage("Please close the interface you have open before using 'report abuse'", 0, "");
				}
		}
		if (l == 491) {
			Player class30_sub2_sub4_sub1_sub2_6 = playerArray[i1];
			if (class30_sub2_sub4_sub1_sub2_6 != null) {
				doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub2_6.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub2_6.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(14);
			//	stream.method432(anInt1284);
				stream.writeWord(i1); //Player id
			//	stream.writeWord(anInt1285);
				stream.method431(anInt1283); //Item slot
			}
		}
		if (l == 639) {
			String s3 = menuActionName[i];
			int k2 = s3.indexOf("@whi@");
			if (k2 != -1) {
				long l4 = TextClass.longForName(s3.substring(k2 + 5).trim());
				int k3 = -1;
				for (int i4 = 0; i4 < friendsCount; i4++) {
					if (friendsListAsLongs[i4] != l4)
						continue;
					k3 = i4;
					break;
				}

				if (k3 != -1 && friendsNodeIDs[k3] > 0) {
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
		if (l == 454) {
			stream.createFrame(41);
			stream.writeWord(i1);
			stream.method432(j);
			stream.method432(k);
			atInventoryLoopCycle = 0;
			atInventoryInterface = k;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[k].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[k].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (l == 478) {
			NPC class30_sub2_sub4_sub1_sub1_7 = npcArray[i1];
			if (class30_sub2_sub4_sub1_sub1_7 != null) {
				doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub1_7.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub1_7.smallX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				if ((i1 & 3) == 0)
					anInt1155++;
				if (anInt1155 >= 53) {
					stream.createFrame(85);
					stream.writeWordBigEndian(66);
					anInt1155 = 0;
				}
				stream.createFrame(18);
				stream.method431(i1);
			}
		}
		if (l == 113) {
			method66(i1, k, j);
			stream.createFrame(70);
			stream.method431(j + baseX);
			stream.writeWord(k + baseY);
			stream.method433(i1 >> 14 & 0x7fff);
		}
		if (l == 872) {
			method66(i1, k, j);
			stream.createFrame(234);
			stream.method433(j + baseX);
			stream.method432(i1 >> 14 & 0x7fff);
			stream.method433(k + baseY);
		}
		if (l == 502) {
			method66(i1, k, j);
			stream.createFrame(132);
			stream.method433(j + baseX);
			stream.writeWord(i1 >> 14 & 0x7fff);
			stream.method432(k + baseY);
		}
		if (l == 1125) {
			//EXAMINE ITEM PACKET
		/*	ItemDef itemDef = ItemDef.forID(i1);
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
			}*/
			
			ItemDef itemDef = ItemDef.forID(i1);
			RSInterface class9_4 = RSInterface.interfaceCache[k];
			String s5;
			if (class9_4 != null && class9_4.invStackSizes[j] >= 0x186a0)
				s5 = class9_4.invStackSizes[j] + " x " + itemDef.name;
			else if (itemDef.description != null)
				s5 = new String(itemDef.description);
			else
				s5 = "It's a " + itemDef.name + ".";
			pushMessage(s5, 0, "");
		}
		if (l == 169) {
			stream.createFrame(185);
			stream.writeWord(k);
			RSInterface class9_3 = RSInterface.interfaceCache[k];
			if (class9_3.valueIndexArray != null && class9_3.valueIndexArray[0][0] == 5) {
				int l2 = class9_3.valueIndexArray[0][1];
				variousSettings[l2] = 1 - variousSettings[l2];
				method33(l2);
				needDrawTabArea = true;
			}
		}
		if (l == 447) {
			itemSelected = 1;
			anInt1283 = j;
			anInt1284 = k;
			anInt1285 = i1;
			selectedItemName = ItemDef.forID(i1).name;
			spellSelected = 0;
			needDrawTabArea = true;
			return;
		}
		if (l == 1226) {
			int j1 = i1 >> 14 & 0x7fff;
		ObjectDef class46 = ObjectDef.forID(j1);
		String s10;
		if (class46.description != null)
			s10 = new String(class46.description);
		else
			s10 = "It's a " + class46.name + ".";
		pushMessage(s10, 0, "");
		}
		if (l == 244) {
			boolean flag7 = doWalkTo(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, k, myPlayer.smallX[0], false, j);
			if (!flag7)
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
		if (l == 1448) {
			ItemDef itemDef_1 = ItemDef.forID(i1);
			String s6;
			if (itemDef_1.description != null)
				s6 = new String(itemDef_1.description);
			else
				s6 = "It's a " + itemDef_1.name + ".";
			pushMessage(s6, 0, "");
			//EXAMINE ITEM PACKET
		/*	ItemDef itemDef_1 = ItemDef.forID(i1);
			if(itemDef_1 != null)
			{
				stream.createFrame(220);
				stream.writeWord(i1); // ID
			}*/
		}
		itemSelected = 0;
		spellSelected = 0;
		needDrawTabArea = true;

	}

	private void method70() {
		anInt1251 = 0;
		int j = (myPlayer.x >> 7) + baseX;
		int k = (myPlayer.y >> 7) + baseY;
		if (j >= 3053 && j <= 3156 && k >= 3056 && k <= 3136)
			anInt1251 = 1;
		if (j >= 3072 && j <= 3118 && k >= 9492 && k <= 9535)
			anInt1251 = 1;
		if (anInt1251 == 1 && j >= 3139 && j <= 3199 && k >= 3008 && k <= 3062)
			anInt1251 = 0;
	}

	public void run() {
		if (drawFlames) {
			drawFlames();
		} else {
			super.run();
		}
	}

	private void build3dScreenMenu() {
		if (itemSelected == 0 && spellSelected == 0) {
			menuActionName[menuActionRow] = "Walk here";
			menuActionID[menuActionRow] = 519;
			menuActionCmd2[menuActionRow] = super.mouseX;
			menuActionCmd3[menuActionRow] = super.mouseY;
			menuActionRow++;
		}
		int j = -1;
		for (int k = 0; k < Model.anInt1687; k++) {
			int l = Model.anIntArray1688[k];
			int i1 = l & 0x7f;
			int j1 = l >> 7 & 0x7f;
			int k1 = l >> 29 & 3;
			int l1 = l >> 14 & 0x7fff;
		if (l == j)
			continue;
		j = l;
		if (k1 == 2 && worldController.method304(plane, i1, j1, l) >= 0) {
			ObjectDef class46 = ObjectDef.forID(l1);
			if (class46.childrenIDs != null)
				class46 = class46.method580();
			if (class46 == null)
				continue;
			if (itemSelected == 1) {
				menuActionName[menuActionRow] = "Use " + selectedItemName + " with @cya@" + class46.name;
				menuActionID[menuActionRow] = 62;
				menuActionCmd1[menuActionRow] = l;
				menuActionCmd2[menuActionRow] = i1;
				menuActionCmd3[menuActionRow] = j1;
				menuActionRow++;
			} else if (spellSelected == 1) {
				if ((spellUsableOn & 4) == 4) {
					menuActionName[menuActionRow] = spellTooltip + " @cya@" + class46.name;
					menuActionID[menuActionRow] = 956;
					menuActionCmd1[menuActionRow] = l;
					menuActionCmd2[menuActionRow] = i1;
					menuActionCmd3[menuActionRow] = j1;
					menuActionRow++;
				}
			} else {
				if (class46.actions != null) {
					for (int i2 = 4; i2 >= 0; i2--)
						if (class46.actions[i2] != null) {
							menuActionName[menuActionRow] = class46.actions[i2] + " @cya@" + class46.name;
							if (i2 == 0)
								menuActionID[menuActionRow] = 502;
							if (i2 == 1)
								menuActionID[menuActionRow] = 900;
							if (i2 == 2)
								menuActionID[menuActionRow] = 113;
							if (i2 == 3)
								menuActionID[menuActionRow] = 872;
							if (i2 == 4)
								menuActionID[menuActionRow] = 1062;
							menuActionCmd1[menuActionRow] = l;
							menuActionCmd2[menuActionRow] = i1;
							menuActionCmd3[menuActionRow] = j1;
							menuActionRow++;
						}

				}
				// menuActionName[menuActionRow] = "Examine @cya@" +
				// class46.name + " @gre@(@whi@" + l1 + "@gre@) (@whi@" +
				// (i1 + baseX) + "," + (j1 + baseY) + "@gre@)";
				menuActionName[menuActionRow] = (myPrivilege < 2 ? "Examine @cya@" + class46.name : "Examine @cya@" +
						  class46.name + " @gre@(@whi@" + l1 + "@gre@) (@whi@" +
						 (i1 + baseX) + "," + (j1 + baseY) + "@gre@)");
				menuActionID[menuActionRow] = 1226;
				menuActionCmd1[menuActionRow] = class46.type << 14;
				menuActionCmd2[menuActionRow] = i1;
				menuActionCmd3[menuActionRow] = j1;
				menuActionRow++;
			}
		}
		if (k1 == 1) {
			NPC npc = npcArray[l1];
			try {
				if (npc.desc.aByte68 == 1 && (npc.x & 0x7f) == 64 && (npc.y & 0x7f) == 64) {
					for (int j2 = 0; j2 < npcCount; j2++) {
						NPC npc2 = npcArray[npcIndices[j2]];
						if (npc2 != null && npc2 != npc && npc2.desc.aByte68 == 1 && npc2.x == npc.x && npc2.y == npc.y)
							buildAtNPCMenu(npc2.desc, npcIndices[j2], j1, i1);
					}
					for (int l2 = 0; l2 < playerCount; l2++) {
						Player player = playerArray[playerIndices[l2]];
						if (player != null && player.x == npc.x && player.y == npc.y)
							buildAtPlayerMenu(i1, playerIndices[l2], player, j1);
					}
				}
				buildAtNPCMenu(npc.desc, l1, j1, i1);
			} catch (Exception e) {
			}
		}
		if (k1 == 0) {
			Player player = playerArray[l1];
			try {
				if((player.x & 0x7f) == 64 && (player.y & 0x7f) == 64)
				{
					for(int k2 = 0; k2 < npcCount; k2++)
					{
						NPC npc2 = npcArray[npcIndices[k2]];
						if(npc2 != null && npc2.desc.aByte68 == 1 && npc2.x == player.x && npc2.y == player.y)
							buildAtNPCMenu(npc2.desc, npcIndices[k2], j1, i1);
					}

					for(int i3 = 0; i3 < playerCount; i3++)
					{
						Player player2 = playerArray[playerIndices[i3]];
						if(player2 != null && player2 != player && player2.x == player.x && player2.y == player.y)
							buildAtPlayerMenu(i1, playerIndices[i3], player2, j1);
					}

				}
				buildAtPlayerMenu(i1, l1, player, j1);
			} catch (Exception e) {
				
			}
		}
		if (k1 == 3) {
			NodeList class19 = groundArray[plane][i1][j1];
			if (class19 != null) {
				for (Item item = (Item) class19.getFirst(); item != null; item = (Item) class19.getNext()) {
					ItemDef itemDef = ItemDef.forID(item.ID);
					if (itemSelected == 1) {
						menuActionName[menuActionRow] = "Use " + selectedItemName + " with @lre@" + itemDef.name;
						menuActionID[menuActionRow] = 511;
						menuActionCmd1[menuActionRow] = item.ID;
						menuActionCmd2[menuActionRow] = i1;
						menuActionCmd3[menuActionRow] = j1;
						menuActionRow++;
					} else if (spellSelected == 1) {
						if ((spellUsableOn & 1) == 1) {
							menuActionName[menuActionRow] = spellTooltip + " @lre@" + itemDef.name;
							menuActionID[menuActionRow] = 94;
							menuActionCmd1[menuActionRow] = item.ID;
							menuActionCmd2[menuActionRow] = i1;
							menuActionCmd3[menuActionRow] = j1;
							menuActionRow++;
						}
					} else {
						for (int j3 = 4; j3 >= 0; j3--)
							if (itemDef.groundActions != null && itemDef.groundActions[j3] != null) {
								menuActionName[menuActionRow] = itemDef.groundActions[j3] + " @lre@" + itemDef.name;
								if (j3 == 0)
									menuActionID[menuActionRow] = 652;
								if (j3 == 1)
									menuActionID[menuActionRow] = 567;
								if (j3 == 2)
									menuActionID[menuActionRow] = 234;
								if (j3 == 3)
									menuActionID[menuActionRow] = 244;
								if (j3 == 4)
									menuActionID[menuActionRow] = 213;
								menuActionCmd1[menuActionRow] = item.ID;
								menuActionCmd2[menuActionRow] = i1;
								menuActionCmd3[menuActionRow] = j1;
								menuActionRow++;
							} else if (j3 == 2) {
								menuActionName[menuActionRow] = "Take @lre@" + itemDef.name;
								menuActionID[menuActionRow] = 234;
								menuActionCmd1[menuActionRow] = item.ID;
								menuActionCmd2[menuActionRow] = i1;
								menuActionCmd3[menuActionRow] = j1;
								menuActionRow++;
							}
						menuActionName[menuActionRow] = (myPrivilege < 2 ? "Examine @lre@" + itemDef.name :
								"Examine @lre@" + itemDef.name +
								" @gre@(@whi@" + item.ID + "@gre@)");
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

	public void cleanUpForQuit() {
		Signlink.reporterror = false;
		try {
			if (socketStream != null) {
				socketStream.close();
			}
		} catch (Exception _ex) {
		}
		socketStream = null;
		stopMidi();
		resetFade();
		if (mouseDetection != null)
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
		mapEdgeIP = null;
		leftFrame = null;
		topFrame = null;
		aRSImageProducer_1164 = null;
		aRSImageProducer_1165 = null;
		aRSImageProducer_1166 = null;
		aRSImageProducer_1123 = null;
		aRSImageProducer_1124 = null;
		aRSImageProducer_1125 = null;
		/* Null pointers for custom sprites */
		background = null;
		mascotInv = null;
		mascotChat = null;
		cacheSprite = null;
		mapBack = null;
		sideIcons = null;
		compass = null;
		hitMarks = null;
		headIcons = null;
		skullIcons = null;
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
		minimapImage = null;
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
		multiOverlay = null;
		nullLoader();
		ObjectDef.nullLoader();
		EntityDef.nullLoader();
		ItemDef.nullLoader();
		Flo.cache = null;
		IDK.cache = null;
		RSInterface.interfaceCache = null;
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

	Component getGameComponent() {
		if (Signlink.mainapp != null)
			return Signlink.mainapp;
		if (super.gameFrame != null)
			return super.gameFrame;
		else
			return this;
	}

	private void method73() {
		do {
			int j = readChar(-796);
			if (j == -1)
				break;
			if (openInterfaceID != -1 && openInterfaceID == reportAbuseInterfaceID) {
				if (j == 8 && reportAbuseInput.length() > 0)
					reportAbuseInput = reportAbuseInput.substring(0, reportAbuseInput.length() - 1);
				if ((j >= 97 && j <= 122 || j >= 65 && j <= 90 || j >= 48 && j <= 57 || j == 32) && reportAbuseInput.length() < 12)
					reportAbuseInput += (char) j;
			} else if (messagePromptRaised) {
				if (j >= 32 && j <= 122 && promptInput.length() < 80) {
					promptInput += (char) j;
					inputTaken = true;
				}
				if (j == 8 && promptInput.length() > 0) {
					promptInput = promptInput.substring(0, promptInput.length() - 1);
					inputTaken = true;
				}
				if (j == 13 || j == 10) {
					messagePromptRaised = false;
					inputTaken = true;
					if (friendsListAction == 1) {
						long l = TextClass.longForName(promptInput);
						addFriend(l);
					}
					if (friendsListAction == 2 && friendsCount > 0) {
						long l1 = TextClass.longForName(promptInput);
						delFriend(l1);
					}
					if (friendsListAction == 3 && promptInput.length() > 0) {
						stream.createFrame(126);
						stream.writeWordBigEndian(0);
						int k = stream.currentOffset;
						stream.writeQWord(aLong953);
						TextInput.method526(promptInput, stream);
						stream.writeBytes(stream.currentOffset - k);
						promptInput = TextInput.processText(promptInput);
						// promptInput = Censor.doCensor(promptInput);
						pushMessage(promptInput, 6, TextClass.fixName(TextClass.nameForLong(aLong953)));
						if (privateChatMode == 2) {
							privateChatMode = 1;
							stream.createFrame(95);
							stream.writeWordBigEndian(publicChatMode);
							stream.writeWordBigEndian(privateChatMode);
							stream.writeWordBigEndian(tradeMode);
						}
					}
					if (friendsListAction == 4 && ignoreCount < 100) {
						long l2 = TextClass.longForName(promptInput);
						addIgnore(l2);
					}
					if (friendsListAction == 5 && ignoreCount > 0) {
						long l3 = TextClass.longForName(promptInput);
						delIgnore(l3);
					}
					if (friendsListAction == 6) {
						long l3 = TextClass.longForName(promptInput);
						chatJoin(l3);
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
						try {
							amount = Integer.parseInt(amountOrNameInput);
						} catch (NumberFormatException ex){
							amount = Integer.MAX_VALUE;
						}
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
			} else if (backDialogID == -1) {
				if (j >= 32 && j <= 122 && inputString.length() < 80) {
					inputString += (char) j;
					inputTaken = true;
				}
				if (j == 8 && inputString.length() > 0) {
					inputString = inputString.substring(0, inputString.length() - 1);
					inputTaken = true;
				}
				if ((j == 13 || j == 10) && inputString.length() > 0) {
					if (inputString.equals("::toggleroofs")){
						roofsToggled = !roofsToggled;
					}
					if (myPrivilege == 2) {
						
						if (inputString.equals("::dumpclip"))
							onDemandFetcher.DumpMapClipping();

						if (inputString.equals("::regular"))
							toggleSize(0);
						if (inputString.equals("::resize"))
							toggleSize(1);
						if (inputString.equals("::fullscreen"))
							toggleSize(2);
						
						if (inputString.equals("::fpson"))
							fpsOn = true;
						if (inputString.equals("::fpsoff"))
							fpsOn = false;
						if (inputString.equals("::dataon"))
							clientData = true;
						if (inputString.equals("::dataoff"))
							clientData = false;
						if (inputString.equals("::noclip")) {
							for (int k1 = 0; k1 < 4; k1++) {
								for (int i2 = 1; i2 < 103; i2++) {
									for (int k2 = 1; k2 < 103; k2++)
										aClass11Array1230[k1].anIntArrayArray294[i2][k2] = 0;

								}
							}
						}
					}
					if (inputString.startsWith("/"))
						inputString = "::" + inputString;
					if (inputString.startsWith("::")) {
						stream.createFrame(103);
						stream.writeWordBigEndian(inputString.length() - 1);
						stream.writeString(inputString.substring(2));
					} else {
						String s = inputString.toLowerCase();
						int j2 = 0;
						if (s.startsWith("yellow:")) {
							j2 = 0;
							inputString = inputString.substring(7);
						} else if (s.startsWith("red:")) {
							j2 = 1;
							inputString = inputString.substring(4);
						} else if (s.startsWith("green:")) {
							j2 = 2;
							inputString = inputString.substring(6);
						} else if (s.startsWith("cyan:")) {
							j2 = 3;
							inputString = inputString.substring(5);
						} else if (s.startsWith("purple:")) {
							j2 = 4;
							inputString = inputString.substring(7);
						} else if (s.startsWith("white:")) {
							j2 = 5;
							inputString = inputString.substring(6);
						} else if (s.startsWith("flash1:")) {
							j2 = 6;
							inputString = inputString.substring(7);
						} else if (s.startsWith("flash2:")) {
							j2 = 7;
							inputString = inputString.substring(7);
						} else if (s.startsWith("flash3:")) {
							j2 = 8;
							inputString = inputString.substring(7);
						} else if (s.startsWith("glow1:")) {
							j2 = 9;
							inputString = inputString.substring(6);
						} else if (s.startsWith("glow2:")) {
							j2 = 10;
							inputString = inputString.substring(6);
						} else if (s.startsWith("glow3:")) {
							j2 = 11;
							inputString = inputString.substring(6);
						}else if (s.startsWith(">")) {
							j2 = 2;
						}
						s = inputString.toLowerCase();
						int i3 = 0;
						if (s.startsWith("wave:")) {
							i3 = 1;
							inputString = inputString.substring(5);
						} else if (s.startsWith("wave2:")) {
							i3 = 2;
							inputString = inputString.substring(6);
						} else if (s.startsWith("shake:")) {
							i3 = 3;
							inputString = inputString.substring(6);
						} else if (s.startsWith("scroll:")) {
							i3 = 4;
							inputString = inputString.substring(7);
						} else if (s.startsWith("slide:")) {
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
						// inputString = Censor.doCensor(inputString);
						myPlayer.textSpoken = inputString;
						myPlayer.anInt1513 = j2;
						myPlayer.anInt1531 = i3;
						myPlayer.textCycle = 150;
						if (myPrivilege == 2)
							pushMessage(myPlayer.textSpoken, 2, "@cr2@" + myPlayer.name);
						else if (myPrivilege == 1)
							pushMessage(myPlayer.textSpoken, 2, "@cr1@" + myPlayer.name);
						else
							pushMessage(myPlayer.textSpoken, 2, myPlayer.name);
						if (publicChatMode == 2) {
							publicChatMode = 3;
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
		} while (true);
	}

	private void buildPublicChat(int j) {
		int l = 0;
		for (int i1 = 0; i1 < 500; i1++) {
			if (chatMessages[i1] == null)
				continue;
			if (chatTypeView != 1)
				continue;
			int j1 = chatTypes[i1];
			String s = chatNames[i1];
			int k1 = (70 - l * 14 + 42) + chatScrollPos + 4 + 5;
			if (k1 < -23)
				break;
			if (s != null && s.startsWith("@cr1@"))
				s = s.substring(5);
			if (s != null && s.startsWith("@cr2@"))
				s = s.substring(5);
			if (s != null && s.startsWith("@cr3@"))
				s = s.substring(5);
			if ((j1 == 1 || j1 == 2) && (j1 == 1 || publicChatMode == 0 || publicChatMode == 1 && isFriendOrSelf(s))) {
				if (j > k1 - 14 && j <= k1 && !s.equals(myPlayer.name)) {
					if (myPrivilege >= 1) {
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

	private void buildFriendChat(int j) {
		int l = 0;
		for (int i1 = 0; i1 < 500; i1++) {
			if (chatMessages[i1] == null)
				continue;
			if (chatTypeView != 2)
				continue;
			int j1 = chatTypes[i1];
			String s = chatNames[i1];
			int k1 = (70 - l * 14 + 42) + chatScrollPos + 4 + 5;
			if (k1 < -23)
				break;
			if (s != null && s.startsWith("@cr1@"))
				s = s.substring(5);
			if (s != null && s.startsWith("@cr2@"))
				s = s.substring(5);
			if (s != null && s.startsWith("@cr3@"))
				s = s.substring(5);
			if ((j1 == 5 || j1 == 6) && (splitPrivateChat == 0 || chatTypeView == 2) && (j1 == 6 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(s)))
				l++;
			if ((j1 == 3 || j1 == 7) && (splitPrivateChat == 0 || chatTypeView == 2) && (j1 == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(s))) {
				if (j > k1 - 14 && j <= k1) {
					if (myPrivilege >= 1) {
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

	private void buildDuelorTrade(int j) {
		int l = 0;
		for (int i1 = 0; i1 < 500; i1++) {
			if (chatMessages[i1] == null)
				continue;
			if (chatTypeView != 3 && chatTypeView != 4)
				continue;
			int j1 = chatTypes[i1];
			String s = chatNames[i1];
			int k1 = (70 - l * 14 + 42) + chatScrollPos + 4 + 5;
			if (k1 < -23)
				break;
			if (s != null && s.startsWith("@cr1@"))
				s = s.substring(5);
			if (s != null && s.startsWith("@cr2@"))
				s = s.substring(5);
			if (s != null && s.startsWith("@cr3@"))
				s = s.substring(5);
			if (chatTypeView == 3 && j1 == 4 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s))) {
				if (j > k1 - 14 && j <= k1) {
					menuActionName[menuActionRow] = "Accept trade @whi@" + s;
					menuActionID[menuActionRow] = 484;
					menuActionRow++;
				}
				l++;
			}
			if (chatTypeView == 4 && j1 == 8 && (duelMode == 0 || duelMode == 1 && isFriendOrSelf(s))) {
				if (j > k1 - 14 && j <= k1) {
					menuActionName[menuActionRow] = "Accept challenge @whi@" + s;
					menuActionID[menuActionRow] = 6;
					menuActionRow++;
				}
				l++;
			}
			if (j1 == 12) {
				if (j > k1 - 14 && j <= k1) {
					menuActionName[menuActionRow] = "Go-to @blu@" + s;
					menuActionID[menuActionRow] = 915;
					menuActionRow++;
				}
				l++;
			}
		}
	}

	private void buildChatAreaMenu(int j) {
		int l = 0;
		for (int i1 = 0; i1 < 500; i1++) {
			if (chatMessages[i1] == null)
				continue;
			int j1 = chatTypes[i1];
			int k1 = (70 - l * 14 + 42) + chatScrollPos + 4 + 5;
			if (k1 < -23)
				break;
			String s = chatNames[i1];
			if (chatTypeView == 1) {
				buildPublicChat(j);
				break;
			}
			if (chatTypeView == 2) {
				buildFriendChat(j);
				break;
			}
			if (chatTypeView == 3 || chatTypeView == 4) {
				buildDuelorTrade(j);
				break;
			}
			if (chatTypeView == 5) {
				break;
			}
			if (chatTypeView == 9) {
				break;
			}
			if (s != null && s.startsWith("@cr1@")) {
				s = s.substring(5);
			}
			if (s != null && s.startsWith("@cr2@")) {
				s = s.substring(5);
			}
			if (s != null && s.startsWith("@cr3@")) {
				s = s.substring(5);
			}
			if (j1 == 0)
				l++;
			if ((j1 == 1 || j1 == 2) && (j1 == 1 || publicChatMode == 0 || publicChatMode == 1 && isFriendOrSelf(s))) {
				if (j > k1 - 14 && j <= k1 && !s.equals(myPlayer.name)) {
					if (myPrivilege >= 1) {
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
			if ((j1 == 3 || j1 == 7) && splitPrivateChat == 0 && (j1 == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(s))) {
				if (j > k1 - 14 && j <= k1) {
					if (myPrivilege >= 1) {
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
			if (j1 == 4 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s))) {
				if (j > k1 - 14 && j <= k1) {
					menuActionName[menuActionRow] = "Accept trade @whi@" + s;
					menuActionID[menuActionRow] = 484;
					menuActionRow++;
				}
				l++;
			}
			if ((j1 == 5 || j1 == 6) && splitPrivateChat == 0 && privateChatMode < 2)
				l++;
			if (j1 == 8 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s))) {
				if (j > k1 - 14 && j <= k1) {
					menuActionName[menuActionRow] = "Accept challenge @whi@" + s;
					menuActionID[menuActionRow] = 6;
					menuActionRow++;
				}
				l++;
			}
		}
	}

	private void drawFriendsListOrWelcomeScreen(RSInterface class9) {
		int j = class9.contentType;
		if (j >= 1 && j <= 100 || j >= 701 && j <= 800) {
			if (j == 1 && anInt900 == 0) {
				class9.message = "Loading friend list";
				class9.atActionType = 0;
				return;
			}
			if (j == 1 && anInt900 == 1) {
				class9.message = "Connecting to friendserver";
				class9.atActionType = 0;
				return;
			}
			if (j == 2 && anInt900 != 2) {
				class9.message = "Please wait...";
				class9.atActionType = 0;
				return;
			}
			int k = friendsCount;
			if (anInt900 != 2)
				k = 0;
			if (j > 700)
				j -= 601;
			else
				j--;
			if (j >= k) {
				class9.message = "";
				class9.atActionType = 0;
				return;
			} else {
				class9.message = friendsList[j];
				class9.atActionType = 1;
				return;
			}
		}
		if (j >= 101 && j <= 200 || j >= 801 && j <= 900) {
			int l = friendsCount;
			if (anInt900 != 2)
				l = 0;
			if (j > 800)
				j -= 701;
			else
				j -= 101;
			if (j >= l) {
				class9.message = "";
				class9.atActionType = 0;
				return;
			}
			if (friendsNodeIDs[j] == 0)
				class9.message = "@red@Offline";
			else if (friendsNodeIDs[j] == nodeID)
				class9.message = "@gre@Online"/* + (friendsNodeIDs[j] - 9) */;
			else
				class9.message = "@red@Offline"/* + (friendsNodeIDs[j] - 9) */;
			class9.atActionType = 1;
			return;
		}
		if (j == 203) {
			int i1 = friendsCount;
			if (anInt900 != 2)
				i1 = 0;
			class9.scrollMax = i1 * 15 + 20;
			if (class9.scrollMax <= class9.height)
				class9.scrollMax = class9.height + 1;
			return;
		}
		if (j >= 401 && j <= 500) {
			if ((j -= 401) == 0 && anInt900 == 0) {
				class9.message = "Loading ignore list";
				class9.atActionType = 0;
				return;
			}
			if (j == 1 && anInt900 == 0) {
				class9.message = "Please wait...";
				class9.atActionType = 0;
				return;
			}
			int j1 = ignoreCount;
			if (anInt900 == 0)
				j1 = 0;
			if (j >= j1) {
				class9.message = "";
				class9.atActionType = 0;
				return;
			} else {
				class9.message = TextClass.fixName(TextClass.nameForLong(ignoreListAsLongs[j]));
				class9.atActionType = 1;
				return;
			}
		}
		if (j == 503) {
			class9.scrollMax = ignoreCount * 15 + 20;
			if (class9.scrollMax <= class9.height)
				class9.scrollMax = class9.height + 1;
			return;
		}
		if (j == 327) {
			class9.modelRotation1 = 150;
			class9.modelRotation2 = (int) (Math.sin((double) loopCycle / 40D) * 256D) & 0x7ff;
			if (aBoolean1031) {
				for (int k1 = 0; k1 < 7; k1++) {
					int l1 = anIntArray1065[k1];
					if (l1 >= 0 && !IDK.cache[l1].method537())
						return;
				}

				aBoolean1031 = false;
				Model aclass30_sub2_sub4_sub6s[] = new Model[7];
				int i2 = 0;
				for (int j2 = 0; j2 < 7; j2++) {
					int k2 = anIntArray1065[j2];
					if (k2 >= 0)
						aclass30_sub2_sub4_sub6s[i2++] = IDK.cache[k2].method538();
				}

				Model model = new Model(i2, aclass30_sub2_sub4_sub6s);
				for (int l2 = 0; l2 < 5; l2++)
					if (anIntArray990[l2] != 0) {
						model.method476(anIntArrayArray1003[l2][0], anIntArrayArray1003[l2][anIntArray990[l2]]);
						if (l2 == 1)
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
		if (j == 328) {
			RSInterface rsInterface = class9;
			int verticleTilt = 150;
			int animationSpeed = (int) (Math.sin((double) loopCycle / 40D) * 256D) & 0x7ff;
			rsInterface.modelRotation1 = verticleTilt;
			rsInterface.modelRotation2 = animationSpeed;
			if (aBoolean1031) {
				Model characterDisplay = myPlayer.method452();
				for (int l2 = 0; l2 < 5; l2++)
					if (anIntArray990[l2] != 0) {
						characterDisplay.method476(anIntArrayArray1003[l2][0], anIntArrayArray1003[l2][anIntArray990[l2]]);
						if (l2 == 1)
							characterDisplay.method476(anIntArray1204[0], anIntArray1204[anIntArray990[l2]]);
					}
				int staticFrame = myPlayer.anInt1511;
				characterDisplay.method469();
				characterDisplay.method470(Animation.anims[staticFrame].anIntArray353[0]);
				// characterDisplay.method479(64, 850, -30, -50, -30, true);
				rsInterface.anInt233 = 5;
				rsInterface.mediaID = 0;
				RSInterface.method208(aBoolean994, characterDisplay);
			}
			return;
		}
		if (j == 324) {
			if (aClass30_Sub2_Sub1_Sub1_931 == null) {
				aClass30_Sub2_Sub1_Sub1_931 = class9.sprite1;
				aClass30_Sub2_Sub1_Sub1_932 = class9.sprite2;
			}
			if (aBoolean1047) {
				class9.sprite1 = aClass30_Sub2_Sub1_Sub1_932;
				return;
			} else {
				class9.sprite1 = aClass30_Sub2_Sub1_Sub1_931;
				return;
			}
		}
		if (j == 325) {
			if (aClass30_Sub2_Sub1_Sub1_931 == null) {
				aClass30_Sub2_Sub1_Sub1_931 = class9.sprite1;
				aClass30_Sub2_Sub1_Sub1_932 = class9.sprite2;
			}
			if (aBoolean1047) {
				class9.sprite1 = aClass30_Sub2_Sub1_Sub1_931;
				return;
			} else {
				class9.sprite1 = aClass30_Sub2_Sub1_Sub1_932;
				return;
			}
		}
		if (j == 600) {
			class9.message = reportAbuseInput;
			if (loopCycle % 20 < 10) {
				class9.message += "|";
				return;
			} else {
				class9.message += " ";
				return;
			}
		}
		if (j == 613)
			if (myPrivilege >= 1) {
				if (canMute) {
					class9.textColor = 0xff0000;
					class9.message = "Moderator option: Mute player for 48 hours: <ON>";
				} else {
					class9.textColor = 0xffffff;
					class9.message = "Moderator option: Mute player for 48 hours: <OFF>";
				}
			} else {
				class9.message = "";
			}
		if (j == 650 || j == 655)
			if (anInt1193 != 0) {
				String s;
				if (daysSinceLastLogin == 0)
					s = "earlier today";
				else if (daysSinceLastLogin == 1)
					s = "yesterday";
				else
					s = daysSinceLastLogin + " days ago";
				class9.message = "You last logged in " + s + " from: " + Signlink.dns;
			} else {
				class9.message = "";
			}
		if (j == 651) {
			if (unreadMessages == 0) {
				class9.message = "0 unread messages";
				class9.textColor = 0xffff00;
			}
			if (unreadMessages == 1) {
				class9.message = "1 unread message";
				class9.textColor = 65280;
			}
			if (unreadMessages > 1) {
				class9.message = unreadMessages + " unread messages";
				class9.textColor = 65280;
			}
		}
		if (j == 652)
			if (daysSinceRecovChange == 201) {
				if (membersInt == 1)
					class9.message = "@yel@This is a non-members world: @whi@Since you are a member we";
				else
					class9.message = "";
			} else if (daysSinceRecovChange == 200) {
				class9.message = "You have not yet set any password recovery questions.";
			} else {
				String s1;
				if (daysSinceRecovChange == 0)
					s1 = "Earlier today";
				else if (daysSinceRecovChange == 1)
					s1 = "Yesterday";
				else
					s1 = daysSinceRecovChange + " days ago";
				class9.message = s1 + " you changed your recovery questions";
			}
		if (j == 653)
			if (daysSinceRecovChange == 201) {
				if (membersInt == 1)
					class9.message = "@whi@recommend you use a members world instead. You may use";
				else
					class9.message = "";
			} else if (daysSinceRecovChange == 200)
				class9.message = "We strongly recommend you do so now to secure your account.";
			else
				class9.message = "If you do not remember making this change then cancel it immediately";
		if (j == 654) {
			if (daysSinceRecovChange == 201)
				if (membersInt == 1) {
					class9.message = "@whi@this world but member benefits are unavailable whilst here.";
					return;
				} else {
					class9.message = "";
					return;
				}
			if (daysSinceRecovChange == 200) {
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
					if (clientSize != 0) {
						l = clientHeight - 170 - i * 13;
					}
					int k1 = 4;
					textDrawingArea.method385(0, "From", l, k1);
					textDrawingArea.method385(65535, "From", l - 1, k1);
					k1 += textDrawingArea.getTextWidth("From ");
					if(byte1 == 1)
					{
						modIcons[0].drawBackground(k1, l - 12);
						k1 += 12;
					}
					if(byte1 == 2)
					{
						modIcons[1].drawBackground(k1, l - 12);
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
					if (clientSize != 0) {
						i1 = clientHeight - 170 - i * 13;
					}
					textDrawingArea.method385(0, chatMessages[j], i1, 4);
					textDrawingArea.method385(65535, chatMessages[j], i1 - 1, 4);
					if(++i >= 5)
						return;
				}
				if(k == 6 && privateChatMode < 2)
				{
					int j1 = 329 - i * 13;
					if (clientSize != 0) {
						j1 = clientHeight - 170 - i * 13;
					}
					textDrawingArea.method385(0, "To " + s + ": " + chatMessages[j], j1, 4);
					textDrawingArea.method385(65535, "To " + s + ": " + chatMessages[j], j1 - 1, 4);
					if(++i >= 5)
						return;
				}
			}

	}

	public void pushMessage(String s, int i, String s1) {
		if (i == 0 && dialogID != -1) {
			aString844 = s;
			super.clickMode3 = 0;
		}
		if (backDialogID == -1)
			inputTaken = true;
		for (int j = 499; j > 0; j--) {
			chatTypes[j] = chatTypes[j - 1];
			chatNames[j] = chatNames[j - 1];
			chatMessages[j] = chatMessages[j - 1];
			chatRights[j] = chatRights[j - 1];
		}
		chatTypes[0] = i;
		chatNames[0] = s1;
		chatMessages[0] = s;
		chatRights[0] = rights;
	}

	public static void setTab(int id) {
		needDrawTabArea = true;
		tabID = id;
		tabAreaAltered = true;
	}

	private void processTabClick() {
		int[] tab = {
				0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
		};
		if(clientSize == 0) {
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
				if(super.saveClickX >= 627 && super.saveClickX <= 664 && super.saveClickY >= 466 && super.saveClickY < 503 && tabInterfaceIDs[10] != -1)
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
		} else if (clientSize != 0) {
			if(clientWidth < smallTabs) {
				if(super.clickMode3 == 1) {
					if (super.saveClickX >= clientWidth - 241
							&& super.saveClickX <= clientWidth - 204
							&& super.saveClickY >= clientHeight - 72
							&& super.saveClickY < clientHeight - 40
							&& tabInterfaceIDs[0] != -1) {
						if (tabID == tab[0]) {
							showTab = !showTab;
						} else {
							showTab = true;
						}
						tabID = tab[0];
						needDrawTabArea = true;
						tabAreaAltered = true;

					}
					if (super.saveClickX >= clientWidth - 202
							&& super.saveClickX <= clientWidth - 171
							&& super.saveClickY >= clientHeight - 72
							&& super.saveClickY < clientHeight - 40
							&& tabInterfaceIDs[1] != -1) {
						if (tabID == tab[1]) {
							showTab = !showTab;
						} else {
							showTab = true;
						}
						tabID = tab[1];
						needDrawTabArea = true;
						tabAreaAltered = true;

					}
					if (super.saveClickX >= clientWidth - 170
							&& super.saveClickX <= clientWidth - 139
							&& super.saveClickY >= clientHeight - 72
							&& super.saveClickY < clientHeight - 40
							&& tabInterfaceIDs[2] != -1) {
						if (tabID == tab[2]) {
							showTab = !showTab;
						} else {
							showTab = true;
						}
						tabID = tab[2];
						needDrawTabArea = true;
						tabAreaAltered = true;

					}
					if (super.saveClickX >= clientWidth - 138
							&& super.saveClickX <= clientWidth - 105
							&& super.saveClickY >= clientHeight - 72
							&& super.saveClickY < clientHeight - 40
							&& tabInterfaceIDs[3] != -1) {
						if (tabID == tab[3]) {
							showTab = !showTab;
						} else {
							showTab = true;
						}
						tabID = tab[3];
						needDrawTabArea = true;
						tabAreaAltered = true;

					}
					if (super.saveClickX >= clientWidth - 104
							&& super.saveClickX <= clientWidth - 72
							&& super.saveClickY >= clientHeight - 72
							&& super.saveClickY < clientHeight - 40
							&& tabInterfaceIDs[4] != -1) {
						if (tabID == tab[4]) {
							showTab = !showTab;
						} else {
							showTab = true;
						}
						tabID = tab[4];
						needDrawTabArea = true;
						tabAreaAltered = true;

					}
					if (super.saveClickX >= clientWidth - 71
							&& super.saveClickX <= clientWidth - 39
							&& super.saveClickY >= clientHeight - 72
							&& super.saveClickY < clientHeight - 40
							&& tabInterfaceIDs[5] != -1) {
						if (tabID == tab[5]) {
							showTab = !showTab;
						} else {
							showTab = true;
						}
						tabID = tab[5];
						needDrawTabArea = true;
						tabAreaAltered = true;

					}
					if (super.saveClickX >= clientWidth - 38
							&& super.saveClickX <= clientWidth - 0
							&& super.saveClickY >= clientHeight - 72
							&& super.saveClickY < clientHeight - 40
							&& tabInterfaceIDs[6] != -1) {
						if (tabID == tab[6]) {
							showTab = !showTab;
						} else {
							showTab = true;
						}
						tabID = tab[6];
						needDrawTabArea = true;
						tabAreaAltered = true;

					}
					if (super.saveClickX >= clientWidth - 202
							&& super.saveClickX <= clientWidth - 171
							&& super.saveClickY >= clientHeight - 37
							&& super.saveClickY < clientHeight - 0
							&& tabInterfaceIDs[8] != -1) {
						if (tabID == tab[8]) {
							showTab = !showTab;
						} else {
							showTab = true;
						}
						tabID = tab[8];
						needDrawTabArea = true;
						tabAreaAltered = true;

					}
					if (super.saveClickX >= clientWidth - 170
							&& super.saveClickX <= clientWidth - 139
							&& super.saveClickY >= clientHeight - 37
							&& super.saveClickY < clientHeight - 0
							&& tabInterfaceIDs[9] != -1) {
						if (tabID == tab[9]) {
							showTab = !showTab;
						} else {
							showTab = true;
						}
						tabID = tab[9];
						needDrawTabArea = true;
						tabAreaAltered = true;

					}
					if (super.saveClickX >= clientWidth - 138
							&& super.saveClickX <= clientWidth - 105
							&& super.saveClickY >= clientHeight - 37
							&& super.saveClickY < clientHeight - 0
							&& tabInterfaceIDs[10] != -1) {
						if (tabID == tab[10]) {
							showTab = !showTab;
						} else {
							showTab = true;
						}
						tabID = tab[10];
						needDrawTabArea = true;
						tabAreaAltered = true;

					}
					if (super.saveClickX >= clientWidth - 104
							&& super.saveClickX <= clientWidth - 72
							&& super.saveClickY >= clientHeight - 37
							&& super.saveClickY < clientHeight - 0
							&& tabInterfaceIDs[11] != -1) {
						if (tabID == tab[11]) {
							showTab = !showTab;
						} else {
							showTab = true;
						}
						tabID = tab[11];
						needDrawTabArea = true;
						tabAreaAltered = true;

					}
					if (super.saveClickX >= clientWidth - 71
							&& super.saveClickX <= clientWidth - 39
							&& super.saveClickY >= clientHeight - 37
							&& super.saveClickY < clientHeight - 0
							&& tabInterfaceIDs[12] != -1) {
						if (tabID == tab[12]) {
							showTab = !showTab;
						} else {
							showTab = true;
						}
						tabID = tab[12];
						needDrawTabArea = true;
						tabAreaAltered = true;

					}
					if (super.saveClickX >= clientWidth - 38
							&& super.saveClickX <= clientWidth - 0
							&& super.saveClickY >= clientHeight - 37
							&& super.saveClickY < clientHeight - 0
							&& tabInterfaceIDs[13] != -1) {
						if (tabID == tab[13]) {
							showTab = !showTab;
						} else {
							showTab = true;
						}
						tabID = tab[13];
						needDrawTabArea = true;
						tabAreaAltered = true;

					}
				}
			}
		} if(clientWidth >= smallTabs) {
			if(super.clickMode3 == 1) {
				if (super.saveClickX >= clientWidth - 462
						&& super.saveClickX <= clientWidth - 429
						&& super.saveClickY >= clientHeight - 37
						&& super.saveClickY < clientHeight - 0
						&& tabInterfaceIDs[0] != -1) {
					if (tabID == tab[0]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[0];
					needDrawTabArea = true;
					tabAreaAltered = true;

				}
				if (super.saveClickX >= clientWidth - 429
						&& super.saveClickX <= clientWidth - 396
						&& super.saveClickY >= clientHeight - 37
						&& super.saveClickY < clientHeight - 0
						&& tabInterfaceIDs[1] != -1) {
					if (tabID == tab[1]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[1];
					needDrawTabArea = true;
					tabAreaAltered = true;

				}
				if (super.saveClickX >= clientWidth - 396
						&& super.saveClickX <= clientWidth - 363
						&& super.saveClickY >= clientHeight - 37
						&& super.saveClickY < clientHeight - 0
						&& tabInterfaceIDs[2] != -1) {
					if (tabID == tab[2]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[2];
					needDrawTabArea = true;
					tabAreaAltered = true;

				}
				if (super.saveClickX >= clientWidth - 363
						&& super.saveClickX <= clientWidth - 330
						&& super.saveClickY >= clientHeight - 37
						&& super.saveClickY < clientHeight - 0
						&& tabInterfaceIDs[3] != -1) {
					if (tabID == tab[3]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[3];
					needDrawTabArea = true;
					tabAreaAltered = true;

				}
				if (super.saveClickX >= clientWidth - 330
						&& super.saveClickX <= clientWidth - 297
						&& super.saveClickY >= clientHeight - 37
						&& super.saveClickY < clientHeight - 0
						&& tabInterfaceIDs[4] != -1) {
					if (tabID == tab[4]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[4];
					needDrawTabArea = true;
					tabAreaAltered = true;

				}
				if (super.saveClickX >= clientWidth - 297
						&& super.saveClickX <= clientWidth - 264
						&& super.saveClickY >= clientHeight - 37
						&& super.saveClickY < clientHeight - 0
						&& tabInterfaceIDs[5] != -1) {
					if (tabID == tab[5]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[5];
					needDrawTabArea = true;
					tabAreaAltered = true;

				}
				if (super.saveClickX >= clientWidth - 264
						&& super.saveClickX <= clientWidth - 231
						&& super.saveClickY >= clientHeight - 37
						&& super.saveClickY < clientHeight - 0
						&& tabInterfaceIDs[6] != -1) {
					if (tabID == tab[6]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[6];
					needDrawTabArea = true;
					tabAreaAltered = true;

				}
				if (super.saveClickX >= clientWidth - 231
						&& super.saveClickX <= clientWidth - 198
						&& super.saveClickY >= clientHeight - 37
						&& super.saveClickY < clientHeight - 0
						&& tabInterfaceIDs[7] != -1) {
					if (tabID == tab[7]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[7];
					needDrawTabArea = true;
					tabAreaAltered = true;

				}
				if (super.saveClickX >= clientWidth - 198
						&& super.saveClickX <= clientWidth - 165
						&& super.saveClickY >= clientHeight - 37
						&& super.saveClickY < clientHeight - 0
						&& tabInterfaceIDs[8] != -1) {
					if (tabID == tab[8]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[8];
					needDrawTabArea = true;
					tabAreaAltered = true;

				}
				if (super.saveClickX >= clientWidth - 165
						&& super.saveClickX <= clientWidth - 132
						&& super.saveClickY >= clientHeight - 37
						&& super.saveClickY < clientHeight - 0
						&& tabInterfaceIDs[9] != -1) {
					if (tabID == tab[9]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[9];
					needDrawTabArea = true;
					tabAreaAltered = true;

				}
				if (super.saveClickX >= clientWidth - 132
						&& super.saveClickX <= clientWidth - 99
						&& super.saveClickY >= clientHeight - 37
						&& super.saveClickY < clientHeight - 0
						&& tabInterfaceIDs[10] != -1) {
					if (tabID == tab[10]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[10];
					needDrawTabArea = true;
					tabAreaAltered = true;

				}
				if (super.saveClickX >= clientWidth - 99
						&& super.saveClickX <= clientWidth - 66
						&& super.saveClickY >= clientHeight - 37
						&& super.saveClickY < clientHeight - 0
						&& tabInterfaceIDs[11] != -1) {
					if (tabID == tab[11]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[11];
					needDrawTabArea = true;
					tabAreaAltered = true;

				}
				if (super.saveClickX >= clientWidth - 66
						&& super.saveClickX <= clientWidth - 33
						&& super.saveClickY >= clientHeight - 37
						&& super.saveClickY < clientHeight - 0
						&& tabInterfaceIDs[12] != -1) {
					if (tabID == tab[12]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[12];
					needDrawTabArea = true;
					tabAreaAltered = true;

				}
				if (super.saveClickX >= clientWidth - 33
						&& super.saveClickX <= clientWidth - 0
						&& super.saveClickY >= clientHeight - 37
						&& super.saveClickY < clientHeight - 0
						&& tabInterfaceIDs[13] != -1) {
					if (tabID == tab[13]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[13];
					needDrawTabArea = true;
					tabAreaAltered = true;
				}
			}
		}
	}

	private void resetImageProducers2() {
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
		aRSImageProducer_1166 = new RSImageProducer(516, 165, getGameComponent());//chat back
		aRSImageProducer_1164 = new RSImageProducer(249, 168, getGameComponent());//mapback
		DrawingArea.setAllPixelsToZero();
		cacheSprite[24].drawSprite(0, 0);
		aRSImageProducer_1163 = new RSImageProducer(250, 335, getGameComponent());//inventory
		aRSImageProducer_1165 = new RSImageProducer(512, 334, getGameComponent());//gamescreen
		DrawingArea.setAllPixelsToZero();
		aRSImageProducer_1123 = new RSImageProducer(496, 50, getGameComponent());
		aRSImageProducer_1124 = new RSImageProducer(269, 37, getGameComponent());
		aRSImageProducer_1125 = new RSImageProducer(249, 45, getGameComponent());
		welcomeScreenRaised = true;
	}

	private void method81(Sprite sprite, int j, int k) {
		int l = k * k + j * j;
		if(l > 4225 && l < 0x15f90) {
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
			mapEdge.method353((clientSize == 0 ? 86 : 89) - k2 - 20, d, ((clientSize == 0 ? 131 : clientWidth + 131 - 238) + j2) - 10 - 5);
		} else {
			markMinimap(sprite, k, j);
		}
	}

	private void rightClickChatButtons() {
		int y = 0;
		if (clientSize != 0) {
			y = clientHeight - 503;
		}
		if(super.mouseX >= 5 && super.mouseX <= 61 && super.mouseY >= y + 482 && super.mouseY <= y + 503) {
			menuActionName[1] = "View All";
			menuActionID[1] = 999;
			menuActionRow = 2;
		} else if(super.mouseX >= 71 && super.mouseX <= 127 && super.mouseY >= y + 482 && super.mouseY <= y + 503) {
			menuActionName[1] = "Off Game";
			menuActionID[1] = 1001;
			menuActionName[2] = "On Game";
			menuActionID[2] = 1000;
			menuActionName[3] = "View Game";
			menuActionID[3] = 998;
			menuActionRow = 4;
		} else if(super.mouseX >= 137 && super.mouseX <= 193 && super.mouseY >= y + 482 && super.mouseY <= y + 503) {
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
		} else if(super.mouseX >= 203 && super.mouseX <= 259 && super.mouseY >= y + 482 && super.mouseY <= y + 503) {
			menuActionName[1] = "Off private";
			menuActionID[1] = 992;
			menuActionName[2] = "Friends private";
			menuActionID[2] = 991;
			menuActionName[3] = "On private";
			menuActionID[3] = 990;
			menuActionName[4] = "View private";
			menuActionID[4] = 989;
			menuActionRow = 5;
		} else if(super.mouseX >= 269 && super.mouseX <= 325 && super.mouseY >= y + 482 && super.mouseY <= y + 503) {
			menuActionName[1] = "Off duel";
			menuActionID[1] = 983;
			menuActionName[2] = "Friends duel";
			menuActionID[2] = 982;
			menuActionName[3] = "On duel";
			menuActionID[3] = 981;
			menuActionName[4] = "View duel";
			menuActionID[4] = 980;
			menuActionRow = 5;
		} else if(super.mouseX >= 335 && super.mouseX <= 391 && super.mouseY >= y + 482 && super.mouseY <= y + 503) {
			menuActionName[1] = "Off trade";
			menuActionID[1] = 987;
			menuActionName[2] = "Friends trade";
			menuActionID[2] = 986;
			menuActionName[3] = "On trade";
			menuActionID[3] = 985;
			menuActionName[4] = "View trade";
			menuActionID[4] = 984;
			menuActionRow = 5;
		} else if(super.mouseX >= 401 && super.mouseX <= 457 && super.mouseY >= y + 482 && super.mouseY <= y + 503) {
			menuActionName[1] = "Off Global";
			menuActionID[1] = 1008;
			menuActionName[2] = "Hide In Public";
			menuActionID[2] = 1007;
			menuActionName[3] = "On Global";
			menuActionID[3] = 1006;
			menuActionName[4] = "View Global";
			menuActionID[4] = 1005;
			menuActionRow = 5;
		}
	}

	public void processRightClick() {
		if (activeInterfaceType != 0) {
			return;
		}
		menuActionName[0] = "Cancel";
		menuActionID[0] = 1107;
		menuActionRow = 1;
		if (fullscreenInterfaceID != -1) {
			anInt886 = 0;
			anInt1315 = 0;
			buildInterfaceMenu(8, RSInterface.interfaceCache[fullscreenInterfaceID], super.mouseX, 8, super.mouseY, 0);
			if (anInt886 != anInt1026) {
				anInt1026 = anInt886;
			}
			if (anInt1315 != anInt1129) {
				anInt1129 = anInt1315;
			}
			return;
		}
		buildSplitPrivateChatMenu();
		anInt886 = 0;
		anInt1315 = 0;
		if (clientSize == 0) {
			if (super.mouseX > 4 && super.mouseY > 4 && super.mouseX < 516 && super.mouseY < 338)
				if (openInterfaceID != -1)
					buildInterfaceMenu(4, RSInterface.interfaceCache[openInterfaceID], super.mouseX, 4, super.mouseY, 0);
				else
					build3dScreenMenu();
		} else if (clientSize != 0) {
			if (canClick())
				if (super.mouseX > (clientWidth / 2) - 356 && super.mouseY > (clientHeight / 2) - 230 && super.mouseX < ((clientWidth / 2) + 356) && super.mouseY < (clientHeight / 2) + 230 && openInterfaceID != -1)
					buildInterfaceMenu((clientWidth / 2) - 356, RSInterface.interfaceCache[openInterfaceID], super.mouseX, (clientHeight / 2) - 230, super.mouseY, 0);
				else
					build3dScreenMenu();
		}
		if (anInt886 != anInt1026) {
			anInt1026 = anInt886;
		}
		if (anInt1315 != anInt1129) {
			anInt1129 = anInt1315;
		}
		anInt886 = 0;
		anInt1315 = 0;
		if (clientSize == 0) {
			if (super.mouseX > clientWidth - 218 && super.mouseY > clientHeight - 298 && super.mouseX < clientWidth - 25 && super.mouseY < clientHeight - 35) {
				if (invOverlayInterfaceID != -1)
					buildInterfaceMenu(clientWidth - 218, RSInterface.interfaceCache[invOverlayInterfaceID], super.mouseX, clientHeight - 298, super.mouseY, 0);
				else if (tabInterfaceIDs[tabID] != -1)
					buildInterfaceMenu(clientWidth - 218, RSInterface.interfaceCache[tabInterfaceIDs[tabID]], super.mouseX, clientHeight - 298, super.mouseY, 0);
			}
		} else if (clientSize != 0) {
			int y = clientWidth > 1000 ? 37 : 74;
			if(clientWidth > 1000) {
				if (super.mouseX > clientWidth - 197 && super.mouseY > clientHeight - y - 267 && super.mouseX < clientWidth - 7 && super.mouseY < clientHeight - y - 7 && showTab) {
					if (invOverlayInterfaceID != -1) { 
						buildInterfaceMenu(clientWidth - 197,
								RSInterface.interfaceCache[invOverlayInterfaceID],
								super.mouseX, clientHeight - 304, super.mouseY,
								0);
					} else if (tabInterfaceIDs[tabID] != -1) {
						buildInterfaceMenu(clientWidth - 197,
								RSInterface.interfaceCache[tabInterfaceIDs[tabID]],
								super.mouseX, clientHeight - 304, super.mouseY,
								0);
					}
				}
			} if(clientWidth <= 1000) {
				if (super.mouseX > clientWidth - 211 && super.mouseY > clientHeight - y - 267 && super.mouseX < clientWidth - 7 && super.mouseY < clientHeight - y - 7 && showTab) {
					if (invOverlayInterfaceID != -1) {
						buildInterfaceMenu(clientWidth - 211,
								RSInterface.interfaceCache[invOverlayInterfaceID],
								super.mouseX, clientHeight - y - 267, super.mouseY,
								0);
					} else if (tabInterfaceIDs[tabID] != -1) {
						buildInterfaceMenu(clientWidth - 211,
								RSInterface.interfaceCache[tabInterfaceIDs[tabID]],
								super.mouseX, clientHeight - y - 267, super.mouseY,
								0);
					}
				}
			}
		}
		if (anInt886 != anInt1048) {
			needDrawTabArea = true;
			tabAreaAltered = true;
			anInt1048 = anInt886;
		}
		if (anInt1315 != anInt1044) {
			needDrawTabArea = true;
			tabAreaAltered = true;
			anInt1044 = anInt1315;
		}
		anInt886 = 0;
		anInt1315 = 0;
		if (super.mouseX > 0 && super.mouseY > (clientSize == 0 ? 338 : clientHeight - 165) && super.mouseX < 490 && super.mouseY < (clientSize == 0 ? 463 : clientHeight - 40) && showChat) {
			if (backDialogID != -1) {
				buildInterfaceMenu(20,
						RSInterface.interfaceCache[backDialogID], super.mouseX, (clientSize == 0 ? 358 : clientHeight - 145),
						super.mouseY, 0);
			} else if (super.mouseY < (clientSize == 0 ? 463 : clientHeight - 40) && super.mouseX < 490) {
				buildChatAreaMenu(super.mouseY - (clientSize == 0 ? 338 : clientHeight - 165));
			}
		}
		if (backDialogID != -1 && anInt886 != anInt1039) {
			inputTaken = true;
			anInt1039 = anInt886;
		}
		if (backDialogID != -1 && anInt1315 != anInt1500) {
			inputTaken = true;
			anInt1500 = anInt1315;
		}
		if (super.mouseX > 0 && super.mouseY > clientHeight - 165 && super.mouseX < 519 && super.mouseY < clientHeight)
			rightClickChatButtons();
		if (super.mouseX > clientWidth - 249 && super.mouseY < 168)
			rightClickChatButtons();
		boolean flag = false;
		while (!flag) {
			flag = true;
			for (int j = 0; j < menuActionRow - 1; j++) {
				if (menuActionID[j] < 1000 && menuActionID[j + 1] > 1000) {
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
	}
/*
	private int method83(int i, int j, int k) {
		int l = 256 - k;
		return ((i & 0xff00ff) * l + (j & 0xff00ff) * k & 0xff00ff00) + ((i & 0xff00) * l + (j & 0xff00) * k & 0xff0000) >> 8;
	}*/

	private void login(String s, String s1, boolean flag)
	{
		Signlink.errorname = s;
		try
		{
			if(!flag)
			{
				loginMessage2 = "";
				loginMessage1 = "Connecting to server...";
				drawLoginScreen(true);
			}
			socketStream = new RSSocket(this, openSocket(43594 + portOff));
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
				stream.writeDWord(1092);
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
				aStream_847.writeWord(318);
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
				currentSound = 0;
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
				loginMessage2 = "";
				loginMessage1 = "Invalid username or password.";
				return;
			}
			if(k == 4)
			{
				loginMessage1 = "Your account has been disabled.";
				loginMessage2 = "Thank you, come again.";
				return;
			}
			if(k == 5)
			{
				loginMessage1 = "Your account is already logged in.";
				loginMessage2 = "Try again in 60 seconds...";
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
				loginMessage2 = "Too many connections.";
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
				loginMessage2 = "Please try again.";
				return;
			}
			if(k == 14)
			{
				loginMessage1 = "The server is being updated.";
				loginMessage2 = "Please wait and try again.";
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
				loginMessage2 = "Please try again.";
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
						loginMessage1 = "No response from loginserver.";
						loginMessage2 = "Please wait and try again.";
						return;
					}
				} else
				{
					loginMessage1 = "No response from server.";
					loginMessage2 = "Please try again.";
					return;
				}
			} else
			{
				System.out.println("response:" + k);
				loginMessage1 = "Unexpected server response.";
				loginMessage2 = "Please try again.";
				return;
			}
		}
		catch(IOException _ex)
		{
			loginMessage1 = "";
		}
		loginMessage1 = "Invalid login or server offline.";
	}

	private boolean doWalkTo(int i, int j, int k, int i1, int j1, int k1, int l1, int i2, int j2, boolean flag, int k2) {
		byte byte0 = 104;
		byte byte1 = 104;
		for (int l2 = 0; l2 < byte0; l2++) {
			for (int i3 = 0; i3 < byte1; i3++) {
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
		while (i4 != l3) {
			j3 = bigX[i4];
			k3 = bigY[i4];
			i4 = (i4 + 1) % j4;
			if (j3 == k2 && k3 == i2) {
				flag1 = true;
				break;
			}
			if (i1 != 0) {
				if ((i1 < 5 || i1 == 10) && aClass11Array1230[plane].method219(k2, j3, k3, j, i1 - 1, i2)) {
					flag1 = true;
					break;
				}
				if (i1 < 10 && aClass11Array1230[plane].method220(k2, i2, k3, i1 - 1, j, j3)) {
					flag1 = true;
					break;
				}
			}
			if (k1 != 0 && k != 0 && aClass11Array1230[plane].method221(i2, k2, j3, k, l1, k1, k3)) {
				flag1 = true;
				break;
			}
			int l4 = anIntArrayArray825[j3][k3] + 1;
			if (j3 > 0 && anIntArrayArray901[j3 - 1][k3] == 0 && (ai[j3 - 1][k3] & 0x1280108) == 0) {
				bigX[l3] = j3 - 1;
				bigY[l3] = k3;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3 - 1][k3] = 2;
				anIntArrayArray825[j3 - 1][k3] = l4;
			}
			if (j3 < byte0 - 1 && anIntArrayArray901[j3 + 1][k3] == 0 && (ai[j3 + 1][k3] & 0x1280180) == 0) {
				bigX[l3] = j3 + 1;
				bigY[l3] = k3;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3 + 1][k3] = 8;
				anIntArrayArray825[j3 + 1][k3] = l4;
			}
			if (k3 > 0 && anIntArrayArray901[j3][k3 - 1] == 0 && (ai[j3][k3 - 1] & 0x1280102) == 0) {
				bigX[l3] = j3;
				bigY[l3] = k3 - 1;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3][k3 - 1] = 1;
				anIntArrayArray825[j3][k3 - 1] = l4;
			}
			if (k3 < byte1 - 1 && anIntArrayArray901[j3][k3 + 1] == 0 && (ai[j3][k3 + 1] & 0x1280120) == 0) {
				bigX[l3] = j3;
				bigY[l3] = k3 + 1;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3][k3 + 1] = 4;
				anIntArrayArray825[j3][k3 + 1] = l4;
			}
			if (j3 > 0 && k3 > 0 && anIntArrayArray901[j3 - 1][k3 - 1] == 0 && (ai[j3 - 1][k3 - 1] & 0x128010e) == 0 && (ai[j3 - 1][k3] & 0x1280108) == 0 && (ai[j3][k3 - 1] & 0x1280102) == 0) {
				bigX[l3] = j3 - 1;
				bigY[l3] = k3 - 1;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3 - 1][k3 - 1] = 3;
				anIntArrayArray825[j3 - 1][k3 - 1] = l4;
			}
			if (j3 < byte0 - 1 && k3 > 0 && anIntArrayArray901[j3 + 1][k3 - 1] == 0 && (ai[j3 + 1][k3 - 1] & 0x1280183) == 0 && (ai[j3 + 1][k3] & 0x1280180) == 0 && (ai[j3][k3 - 1] & 0x1280102) == 0) {
				bigX[l3] = j3 + 1;
				bigY[l3] = k3 - 1;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3 + 1][k3 - 1] = 9;
				anIntArrayArray825[j3 + 1][k3 - 1] = l4;
			}
			if (j3 > 0 && k3 < byte1 - 1 && anIntArrayArray901[j3 - 1][k3 + 1] == 0 && (ai[j3 - 1][k3 + 1] & 0x1280138) == 0 && (ai[j3 - 1][k3] & 0x1280108) == 0 && (ai[j3][k3 + 1] & 0x1280120) == 0) {
				bigX[l3] = j3 - 1;
				bigY[l3] = k3 + 1;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3 - 1][k3 + 1] = 6;
				anIntArrayArray825[j3 - 1][k3 + 1] = l4;
			}
			if (j3 < byte0 - 1 && k3 < byte1 - 1 && anIntArrayArray901[j3 + 1][k3 + 1] == 0 && (ai[j3 + 1][k3 + 1] & 0x12801e0) == 0 && (ai[j3 + 1][k3] & 0x1280180) == 0 && (ai[j3][k3 + 1] & 0x1280120) == 0) {
				bigX[l3] = j3 + 1;
				bigY[l3] = k3 + 1;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3 + 1][k3 + 1] = 12;
				anIntArrayArray825[j3 + 1][k3 + 1] = l4;
			}
		}
		anInt1264 = 0;
		if (!flag1) {
			if (flag) {
				int i5 = 100;
				for (int k5 = 1; k5 < 2; k5++) {
					for (int i6 = k2 - k5; i6 <= k2 + k5; i6++) {
						for (int l6 = i2 - k5; l6 <= i2 + k5; l6++) {
							if (i6 >= 0 && l6 >= 0 && i6 < 104 && l6 < 104 && anIntArrayArray825[i6][l6] < i5) {
								i5 = anIntArrayArray825[i6][l6];
								j3 = i6;
								k3 = l6;
								anInt1264 = 1;
								flag1 = true;
							}
						}
					}
					if (flag1)
						break;
				}
			}
			if (!flag1)
				return false;
		}
		i4 = 0;
		bigX[i4] = j3;
		bigY[i4++] = k3;
		int l5;
		for (int j5 = l5 = anIntArrayArray901[j3][k3]; j3 != j2 || k3 != j1; j5 = anIntArrayArray901[j3][k3]) {
			if (j5 != l5) {
				l5 = j5;
				bigX[i4] = j3;
				bigY[i4++] = k3;
			}
			if ((j5 & 2) != 0)
				j3++;
			else if ((j5 & 8) != 0)
				j3--;
			if ((j5 & 1) != 0)
				k3++;
			else if ((j5 & 4) != 0)
				k3--;
		}
		if (i4 > 0) {
			int k4 = i4;
			if (k4 > 25)
				k4 = 25;
			i4--;
			int k6 = bigX[i4];
			int i7 = bigY[i4];
			anInt1288 += k4;
			if (anInt1288 >= 92) {
				stream.createFrame(36);
				stream.writeDWord(0);
				anInt1288 = 0;
			}
			if (i == 0) {
				stream.createFrame(164);
				stream.writeWordBigEndian(k4 + k4 + 3);
			}
			if (i == 1) {
				stream.createFrame(248);
				stream.writeWordBigEndian(k4 + k4 + 3 + 14);
			}
			if (i == 2) {
				stream.createFrame(98);
				stream.writeWordBigEndian(k4 + k4 + 3);
			}
			stream.method433(k6 + baseX);
			destX = bigX[0];
			destY = bigY[0];
			for (int j7 = 1; j7 < k4; j7++) {
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

	private void method86(Stream stream) {
		for (int j = 0; j < anInt893; j++) {
			int k = anIntArray894[j];
			NPC npc = npcArray[k];
			int l = stream.readUnsignedByte();
			if ((l & 0x10) != 0) {
				int i1 = stream.method434();
				if (i1 == 65535)
					i1 = -1;
				int i2 = stream.readUnsignedByte();
				if (i1 == npc.anim && i1 != -1) {
					int l2 = Animation.anims[i1].anInt365;
					if (l2 == 1) {
						npc.anInt1527 = 0;
						npc.anInt1528 = 0;
						npc.anInt1529 = i2;
						npc.anInt1530 = 0;
					}
					if (l2 == 2)
						npc.anInt1530 = 0;
				} else if (i1 == -1 || npc.anim == -1 || Animation.anims[i1].anInt359 >= Animation.anims[npc.anim].anInt359) {
					npc.anim = i1;
					npc.anInt1527 = 0;
					npc.anInt1528 = 0;
					npc.anInt1529 = i2;
					npc.anInt1530 = 0;
					npc.anInt1542 = npc.smallXYIndex;
				}
			}
			if ((l & 8) != 0) {
				int j1 = stream.method426();
				int j2 = stream.method427();
				npc.updateHitData(j2, j1, loopCycle);
				npc.loopCycleStatus = loopCycle + 300;
				npc.currentHealth = stream.method426();
				npc.maxHealth = stream.readUnsignedByte();
			}
			if ((l & 0x80) != 0) {
				npc.anInt1520 = stream.readUnsignedWord();
				int k1 = stream.readDWord();
				npc.anInt1524 = k1 >> 16;
			npc.anInt1523 = loopCycle + (k1 & 0xffff);
			npc.anInt1521 = 0;
			npc.anInt1522 = 0;
			if (npc.anInt1523 > loopCycle)
				npc.anInt1521 = -1;
			if (npc.anInt1520 == 65535)
				npc.anInt1520 = -1;
			}
			if ((l & 0x20) != 0) {
				npc.interactingEntity = stream.readUnsignedWord();
				if (npc.interactingEntity == 65535)
					npc.interactingEntity = -1;
			}
			if ((l & 1) != 0) {
				npc.textSpoken = stream.readString();
				npc.textCycle = 100;
			}
			if ((l & 0x40) != 0) {
				int l1 = stream.method427();
				int k2 = stream.method428();
				npc.updateHitData(k2, l1, loopCycle);
				npc.loopCycleStatus = loopCycle + 300;
				npc.currentHealth = stream.method428();
				npc.maxHealth = stream.method427();
			}
			if ((l & 2) != 0) {
				npc.desc = EntityDef.forID(stream.method436());
				npc.anInt1540 = npc.desc.aByte68;
				npc.anInt1504 = npc.desc.anInt79;
				npc.anInt1554 = npc.desc.walkAnim;
				npc.anInt1555 = npc.desc.anInt58;
				npc.anInt1556 = npc.desc.anInt83;
				npc.anInt1557 = npc.desc.anInt55;
				npc.anInt1511 = npc.desc.standAnim;
			}
			if ((l & 4) != 0) {
				npc.anInt1538 = stream.method434();
				npc.anInt1539 = stream.method434();
			}
		}
	}

	private void buildAtNPCMenu(EntityDef entityDef, int i, int j, int k) {
		if (menuActionRow >= 400)
			return;
		if (entityDef.childrenIDs != null)
			entityDef = entityDef.method161();
		if (entityDef == null)
			return;
		if (!entityDef.hasActions)
			return;
		String s = entityDef.name;
		if (entityDef.combatLevel != 0)
			s = s + combatDiffColor(myPlayer.combatLevel, entityDef.combatLevel) + " (level-" + entityDef.combatLevel + ")";
		if (itemSelected == 1) {
			menuActionName[menuActionRow] = "Use " + selectedItemName + " with @yel@" + s;
			menuActionID[menuActionRow] = 582;
			menuActionCmd1[menuActionRow] = i;
			menuActionCmd2[menuActionRow] = k;
			menuActionCmd3[menuActionRow] = j;
			menuActionRow++;
			return;
		}
		if (spellSelected == 1) {
			if ((spellUsableOn & 2) == 2) {
				menuActionName[menuActionRow] = spellTooltip + " @yel@" + s;
				menuActionID[menuActionRow] = 413;
				menuActionCmd1[menuActionRow] = i;
				menuActionCmd2[menuActionRow] = k;
				menuActionCmd3[menuActionRow] = j;
				menuActionRow++;
			}
		} else {
			if (entityDef.actions != null) {
				for (int l = 4; l >= 0; l--)
					if (entityDef.actions[l] != null && !entityDef.actions[l].equalsIgnoreCase("attack")) {
						menuActionName[menuActionRow] = entityDef.actions[l] + " @yel@" + s;
						if (l == 0)
							menuActionID[menuActionRow] = 20;
						if (l == 1)
							menuActionID[menuActionRow] = 412;
						if (l == 2)
							menuActionID[menuActionRow] = 225;
						if (l == 3)
							menuActionID[menuActionRow] = 965;
						if (l == 4)
							menuActionID[menuActionRow] = 478;
						menuActionCmd1[menuActionRow] = i;
						menuActionCmd2[menuActionRow] = k;
						menuActionCmd3[menuActionRow] = j;
						menuActionRow++;
					}

			}
			if (entityDef.actions != null) {
				for (int i1 = 4; i1 >= 0; i1--)
					if (entityDef.actions[i1] != null && entityDef.actions[i1].equalsIgnoreCase("attack")) {
						char c = '\0';
						if (entityDef.combatLevel > myPlayer.combatLevel)
							c = '\u07D0';
						menuActionName[menuActionRow] = entityDef.actions[i1] + " @yel@" + s;
						if (i1 == 0)
							menuActionID[menuActionRow] = 20 + c;
						if (i1 == 1)
							menuActionID[menuActionRow] = 412 + c;
						if (i1 == 2)
							menuActionID[menuActionRow] = 225 + c;
						if (i1 == 3)
							menuActionID[menuActionRow] = 965 + c;
						if (i1 == 4)
							menuActionID[menuActionRow] = 478 + c;
						menuActionCmd1[menuActionRow] = i;
						menuActionCmd2[menuActionRow] = k;
						menuActionCmd3[menuActionRow] = j;
						menuActionRow++;
					}

			}

			menuActionName[menuActionRow] = (myPrivilege < 2 ? "Examine @yel@" + s : "Examine @yel@" + s 
					+ " @gre@(@whi@" + entityDef.interfaceType + "@gre@)");
			menuActionID[menuActionRow] = 1025;
			menuActionCmd1[menuActionRow] = i;
			menuActionCmd2[menuActionRow] = k;
			menuActionCmd3[menuActionRow] = j;
			menuActionRow++;
		}
	}

	private void buildAtPlayerMenu(int i, int j, Player player, int k) {
		if (player == myPlayer)
			return;
		if (menuActionRow >= 400)
			return;
		String s;
		if (player.skill == 0)
			s = player.name + combatDiffColor(myPlayer.combatLevel, player.combatLevel) + " (level-" + player.combatLevel + ")";
		else
			s = player.name + " (skill-" + player.skill + ")";
		if (itemSelected == 1) {
			menuActionName[menuActionRow] = "Use " + selectedItemName + " with @whi@" + s;
			menuActionID[menuActionRow] = 491;
			menuActionCmd1[menuActionRow] = j;
			menuActionCmd2[menuActionRow] = i;
			menuActionCmd3[menuActionRow] = k;
			menuActionRow++;
		} else if (spellSelected == 1) {
			if ((spellUsableOn & 8) == 8) {
				menuActionName[menuActionRow] = spellTooltip + " @whi@" + s;
				menuActionID[menuActionRow] = 365;
				menuActionCmd1[menuActionRow] = j;
				menuActionCmd2[menuActionRow] = i;
				menuActionCmd3[menuActionRow] = k;
				menuActionRow++;
			}
		} else {
			for (int l = 4; l >= 0; l--)
				if (atPlayerActions[l] != null) {
					menuActionName[menuActionRow] = atPlayerActions[l] + " @whi@" + s;
					char c = '\0';
					if (atPlayerActions[l].equalsIgnoreCase("attack")) {
						if (player.combatLevel > myPlayer.combatLevel)
							c = '\u07D0';
						if (myPlayer.team != 0 && player.team != 0)
							if (myPlayer.team == player.team)
								c = '\u07D0';
							else
								c = '\0';
					} else if (atPlayerArray[l])
						c = '\u07D0';
					if (l == 0)
						menuActionID[menuActionRow] = 561 + c;
					if (l == 1)
						menuActionID[menuActionRow] = 779 + c;
					if (l == 2)
						menuActionID[menuActionRow] = 27 + c;
					if (l == 3)
						menuActionID[menuActionRow] = 577 + c;
					if (l == 4)
						menuActionID[menuActionRow] = 729 + c;
					menuActionCmd1[menuActionRow] = j;
					menuActionCmd2[menuActionRow] = i;
					menuActionCmd3[menuActionRow] = k;
					menuActionRow++;
				}

		}
		for (int i1 = 0; i1 < menuActionRow; i1++) {
			if (menuActionID[i1] == 519) {
				menuActionName[i1] = "Walk here @whi@" + s;
				return;
			}
		}
	}

	private void method89(Class30_Sub1 class30_sub1) {
		int i = 0;
		int j = -1;
		int k = 0;
		int l = 0;
		if (class30_sub1.anInt1296 == 0)
			i = worldController.method300(class30_sub1.anInt1295, class30_sub1.anInt1297, class30_sub1.anInt1298);
		if (class30_sub1.anInt1296 == 1)
			i = worldController.method301(class30_sub1.anInt1295, class30_sub1.anInt1297, class30_sub1.anInt1298);
		if (class30_sub1.anInt1296 == 2)
			i = worldController.method302(class30_sub1.anInt1295, class30_sub1.anInt1297, class30_sub1.anInt1298);
		if (class30_sub1.anInt1296 == 3)
			i = worldController.method303(class30_sub1.anInt1295, class30_sub1.anInt1297, class30_sub1.anInt1298);
		if (i != 0) {
			int i1 = worldController.method304(class30_sub1.anInt1295, class30_sub1.anInt1297, class30_sub1.anInt1298, i);
			j = i >> 14 & 0x7fff;
		k = i1 & 0x1f;
		l = i1 >> 6;
		}
		class30_sub1.anInt1299 = j;
		class30_sub1.anInt1301 = k;
		class30_sub1.anInt1300 = l;
	}
	
	private boolean replayWave()
	{
		return Signlink.replayWave();
	}
	
	private boolean saveWave(byte abyte0[], int i)
	{
		return false;
	}
	
	public final void method90() {
		for (int index = 0; index < currentSound; index++) {
			//if (soundDelay[index] <= 0) {
				boolean flag1 = false;
				try {
					Stream stream = Sounds.method241(soundType[index], sound[index]);
					new SoundPlayer((InputStream) new ByteArrayInputStream(stream.buffer, 0, stream.currentOffset), soundDelay[index]);
					if (System.currentTimeMillis() + (long) (stream.currentOffset / 22) > aLong1172 + (long) (anInt1257 / 22)) {
						anInt1257 = stream.currentOffset;
						aLong1172 = System.currentTimeMillis();
					}
				} catch (Exception exception) {
					exception.printStackTrace();
				}
				if (!flag1 || soundDelay[index] == -5) {
					currentSound--;
					for (int j = index; j < currentSound; j++) {
						sound[j] = sound[j + 1];
						soundType[j] = soundType[j + 1];
						soundDelay[j] = soundDelay[j + 1];
						soundVolume[j] = soundVolume[j + 1];
					}
					index--;
				} else {
					soundDelay[index] = -5;
				}
			/*} else {
				soundDelay[index]--;
			}*/
		}
		if(MusicEnabled) {
		    if (previousSong > 0) {
			previousSong -= 20;
			if (previousSong < 0) {
			    previousSong = 0;
			}
			if (previousSong == 0 && musicEnabled) {
			    nextSong = currentSong;
			    songChanging = true;
			    onDemandFetcher.method558(2, nextSong);
			}
		    }
		}
	}
	
	/*private void method90()
    {
		for(int i = 0; i < currentSound; i++)
			if(soundDelay[i] <= 0)
			{
                boolean flag = false;
                try
                {
                	if(sound[i] == anInt874 && soundType[i] == anInt1289)
                    {
                		if(!replayWave())
                			flag = true;
                    } else {
                        Stream stream1 = Sounds.method241(soundType[i], sound[i]);
                        if(System.currentTimeMillis() + (long)(stream1.currentOffset / 22) > aLong1172 + (long)(anInt1257 / 22))
                        {
                            anInt1257 = stream1.currentOffset;
                            aLong1172 = System.currentTimeMillis();
                            if(saveWave(stream1.buffer, stream1.currentOffset))
                            {
                                anInt874 = sound[i];
                                anInt1289 = soundType[i];
                            } else
                            {
                                flag = true;
                            }
                        }
                    }
                } catch(Exception exception) { }
                if(!flag || soundDelay[i] == -5)
                {
                    currentSound--;
                    for(int j = i; j < currentSound; j++)
                    {
                        sound[j] = sound[j + 1];
                        soundType[j] = soundType[j + 1];
                        soundDelay[j] = soundDelay[j + 1];
                    }

                    i--;
                } else
                {
                    soundDelay[i] = -5;
                }
            } else {
                soundDelay[i]--;
            }

        if(previousSong > 0)
        {
            previousSong -= 20;
            if(previousSong < 0)
                previousSong = 0;
            if(previousSong == 0 && musicEnabled && !lowMem)
            {
                nextSong = currentSong;
                songChanging = true;
                onDemandFetcher.method558(2, nextSong);
            }
        }
    }
*/
	void startUp() {
		drawLoadingText(20, "Starting up");
		new CacheDownloader(this).downloadCache();
		try {
			checkClientVersion();
		} catch(Exception _ex) { }
		try {
			readSettings();
		} catch(Exception e) {}
		if(Signlink.sunjava)
			super.minDelay = 5;
		if (Signlink.cache_dat != null) {
			for (int i = 0; i < 5; i++)
				decompressors[i] = new Decompressor(Signlink.cache_dat, Signlink.cache_idx[i], i + 1);
		}
		try {
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
			this.mediaStreamLoader = streamLoader_2;
			try {
				SpriteLoader.loadSprites(streamLoader_2);
				cacheSprite = SpriteLoader.sprites;
			} catch (Exception e) {
				System.out.println("Unable to load sprite cache.");
			}
			StreamLoader streamLoader_3 = streamLoaderForName(6, "textures", "textures", expectedCRCs[6], 45);
			StreamLoader streamLoader_4 = streamLoaderForName(7, "chat system", "wordenc", expectedCRCs[7], 50);
			StreamLoader streamLoader_5 = streamLoaderForName(8, "sound effects", "sounds", expectedCRCs[8], 55);
			byteGroundArray = new byte[4][104][104];
			intGroundArray = new int[4][105][105];
			worldController = new WorldController(intGroundArray);
			for (int j = 0; j < 4; j++)
				aClass11Array1230[j] = new Class11();

			minimapImage = new Sprite(512, 512);
			StreamLoader streamLoader_6 = streamLoaderForName(5, "update list", "versionlist", expectedCRCs[5], 60);
			drawLoadingText(60, "Connecting to update server");
			onDemandFetcher = new OnDemandFetcher();
			onDemandFetcher.start(streamLoader_6, this);
			Model.method459(onDemandFetcher.getModelCount(), onDemandFetcher);
			if(!lowMem && loginMusicEnabled && MusicEnabled)
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
					//	loadError();
						return;
					}
				}
			}
			
			//repackCacheIndex(1); //models
			//repackCacheIndex(2); //animations
			//repackCacheIndex(3); //music
			//repackCacheIndex(4); //maps
			
			drawLoadingText(80, "Unpacking media");
			background = new Sprite("background");
			mascotInv = new Sprite("mascot_inv");
			mascotChat = new Sprite("mascot_chat");
			multiOverlay = new Sprite(streamLoader_2, "overlay_multiway", 0);
			mapBack = new Background(streamLoader_2, "mapback", 0);
			for (int j3 = 0; j3 <= 14; j3++)
				sideIcons[j3] = new Sprite(streamLoader_2, "sideicons", j3);
			compass = new Sprite(streamLoader_2, "compass", 0);
			mapEdge = new Sprite(streamLoader_2, "mapedge", 0);
			mapEdge.method345();
			try {
				for (int k3 = 0; k3 < 100; k3++)
					mapScenes[k3] = new Background(streamLoader_2, "mapscene", k3);
			} catch (Exception _ex) {
			}
			try {
				for (int l3 = 0; l3 < 100; l3++)
					mapFunctions[l3] = new Sprite(streamLoader_2, "mapfunction", l3);
			} catch (Exception _ex) {
			}
			try {
				for (int i4 = 0; i4 < 20; i4++)
					hitMarks[i4] = new Sprite(streamLoader_2, "hitmarks", i4);
			} catch (Exception _ex) {
			}
			try {
				for (int h1 = 0; h1 < 6; h1++)
					headIconsHint[h1] = new Sprite(streamLoader_2, "headicons_hint", h1);
			} catch (Exception _ex) {
			}
			try {
				for (int j4 = 0; j4 < 8; j4++)
					headIcons[j4] = new Sprite(streamLoader_2, "headicons_prayer", j4);
				for (int j45 = 0; j45 < 3; j45++)
					skullIcons[j45] = new Sprite(streamLoader_2, "headicons_pk", j45);
			} catch (Exception _ex) {
			}
			mapFlag = new Sprite(streamLoader_2, "mapmarker", 0);
			mapMarker = new Sprite(streamLoader_2, "mapmarker", 1);
			for (int k4 = 0; k4 < 8; k4++)
				crosses[k4] = new Sprite(streamLoader_2, "cross", k4);
			mapDotItem = new Sprite(streamLoader_2, "mapdots", 0);
			multiWay = new Sprite(streamLoader_2, "Overlay_multiway", 0);
			mapDotNPC = new Sprite(streamLoader_2, "mapdots", 1);
			mapDotPlayer = new Sprite(streamLoader_2, "mapdots", 2);
			mapDotFriend = new Sprite(streamLoader_2, "mapdots", 3);
			mapDotTeam = new Sprite(streamLoader_2, "mapdots", 4);
			mapDotClan = new Sprite(streamLoader_2, "mapdots", 5);
			scrollBar1 = new Sprite(streamLoader_2, "scrollbar", 0);
			scrollBar2 = new Sprite(streamLoader_2, "scrollbar", 1);
			for (int l4 = 0; l4 < 2; l4++)
				modIcons[l4] = new Background(streamLoader_2, "mod_icons", l4);
			Sprite sprite = new Sprite(streamLoader_2, "screenframe", 0);
			leftFrame = new RSImageProducer(sprite.myWidth, sprite.myHeight, getGameComponent());
			sprite.method346(0, 0);
			sprite = new Sprite(streamLoader_2, "screenframe", 1);
			topFrame = new RSImageProducer(sprite.myWidth, sprite.myHeight, getGameComponent());
			sprite.method346(0, 0);
			sprite = new Sprite(streamLoader_2, "mapedge", 0);
			mapEdgeIP = new RSImageProducer(sprite.myWidth, sprite.myHeight, getGameComponent());
			sprite.method346(0, 0);
			int i5 = (int) (Math.random() * 21D) - 10;
			int j5 = (int) (Math.random() * 21D) - 10;
			int k5 = (int) (Math.random() * 21D) - 10;
			int l5 = (int) (Math.random() * 41D) - 20;
			for (int i6 = 0; i6 < 100; i6++) {
				if (mapFunctions[i6] != null)
					mapFunctions[i6].method344(i5 + l5, j5 + l5, k5 + l5);
				if (mapScenes[i6] != null)
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
			TextDrawingArea aclass30_sub2_sub1_sub4s[] = { smallText, aTextDrawingArea_1271, chatTextDrawingArea, aTextDrawingArea_1273 };
			RSInterface.unpack(streamLoader_1, aclass30_sub2_sub1_sub4s, streamLoader_2);
			drawLoadingText(100, "Preparing game engine");
			for (int j6 = 0; j6 < 33; j6++) {
				int k6 = 999;
				int i7 = 0;
				for (int k7 = 0; k7 < 34; k7++) {
					if (mapBack.aByteArray1450[k7 + j6 * mapBack.anInt1452] == 0) {
						if (k6 == 999)
							k6 = k7;
						continue;
					}
					if (k6 == 999)
						continue;
					i7 = k7;
					break;
				}
				anIntArray968[j6] = k6;
				anIntArray1057[j6] = i7 - k6;
			}
			for (int l6 = 1; l6 < 153; l6++) {
				int j7 = 999;
				int l7 = 0;
				for (int j8 = 24; j8 < 177; j8++) {
					if (mapBack.aByteArray1450[j8 + l6 * mapBack.anInt1452] == 0 && (j8 > 34 || l6 > 34)) {
						if (j7 == 999) {
							j7 = j8;
						}
						continue;
					}
					if (j7 == 999) {
						continue;
					}
					l7 = j8;
					break;
				}
				anIntArray1052[l6 - 1] = j7 - 24;
				anIntArray1229[l6 - 1] = l7 - j7;
			}
			Texture.method365(765, 503);
			fullScreenTextureArray = Texture.anIntArray1472;
			Texture.method365(516, 165);
			anIntArray1180 = Texture.anIntArray1472;
			Texture.method365(250, 335);
			anIntArray1181 = Texture.anIntArray1472;
			Texture.method365(512, 334);
			anIntArray1182 = Texture.anIntArray1472;
			int ai[] = new int[9];
			for (int i8 = 0; i8 < 9; i8++) {
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
		} catch (Exception exception) {
			exception.printStackTrace();
			Signlink.reporterror("loaderror " + aString1049 + " " + anInt1079);
		}
		loadingError = true;
	}

	private void method91(Stream stream, int i) {
		while (stream.bitPosition + 10 < i * 8) {
			int j = stream.readBits(11);
			if (j == 2047)
				break;
			if (playerArray[j] == null) {
				playerArray[j] = new Player();
				if (aStreamArray895s[j] != null)
					playerArray[j].updatePlayer(aStreamArray895s[j]);
			}
			playerIndices[playerCount++] = j;
			Player player = playerArray[j];
			player.anInt1537 = loopCycle;
			int k = stream.readBits(1);
			if (k == 1)
				anIntArray894[anInt893++] = j;
			int l = stream.readBits(1);
			int i1 = stream.readBits(5);
			if (i1 > 15)
				i1 -= 32;
			int j1 = stream.readBits(5);
			if (j1 > 15)
				j1 -= 32;
			player.setPos(myPlayer.smallX[0] + j1, myPlayer.smallY[0] + i1, l == 1);
		}
		stream.finishBitAccess();
	}

	public String indexLocation(int cacheIndex, int index) {
		return Signlink.findcachedir() + "index" + cacheIndex + "/" + (index != -1 ? index + ".gz" : "");
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

	public boolean inCircle(int circleX, int circleY, int clickX, int clickY, int radius) {
		return java.lang.Math.pow((circleX + radius - clickX), 2) + java.lang.Math.pow((circleY + radius - clickY), 2) < java.lang.Math.pow(radius, 2);
	}

	private void processMainScreenClick() {
		if(anInt1021 != 0)
			return;
		if(super.clickMode3 == 1) {
			int i = (clientSize == 0 ? super.saveClickX - 25 - 545 : super.saveClickX - clientWidth + 183);
			int j = (clientSize == 0 ? super.saveClickY - 9 : super.saveClickY - 10);
			if (i >= 0 && j >= 0 && i < 146 && j < 151 && canClickMap()) {
				if(inCircle(0, 0, i, j, 77)) {
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
				if(flag1) {
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
				if(anInt1117 > 1151) {
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
	}

	private String interfaceIntToString(int j) {
		if (j < 0x3b9ac9ff)
			return String.valueOf(j);
		else
			return "*";
	}

	private void showErrorScreen() {
		Graphics g = getGameComponent().getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, 765, 503);
		method4(1);
		if (loadingError) {
			aBoolean831 = false;
			g.setFont(new Font("Helvetica", 1, 16));
			g.setColor(Color.yellow);
			int k = 35;
			g.drawString("Sorry, an error has occured whilst loading /v/scape", 30, k);
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
		if (genericLoadingError) {
			aBoolean831 = false;
			g.setFont(new Font("Helvetica", 1, 20));
			g.setColor(Color.white);
			g.drawString("Error - unable to load game!", 50, 50);
			g.drawString("To play /v/scape make sure you play from", 50, 100);
			g.drawString("http://www.UrlHere.com", 50, 150);
		}
		if (rsAlreadyLoaded) {
			aBoolean831 = false;
			g.setColor(Color.yellow);
			int l = 35;
			g.drawString("Error a copy of /v/scape already appears to be loaded", 30, l);
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

	public URL getCodeBase() {
		try {
			return new URL(server + ":" + (80 + portOff));
		} catch (Exception _ex) {
		}
		return null;
	}

	private void method95() {
		for (int j = 0; j < npcCount; j++) {
			int k = npcIndices[j];
			NPC npc = npcArray[k];
			if (npc != null)
				method96(npc);
		}
	}

	private void method96(Entity entity) {
		if (entity.x < 128 || entity.y < 128 || entity.x >= 13184 || entity.y >= 13184) {
			entity.anim = -1;
			entity.anInt1520 = -1;
			entity.anInt1547 = 0;
			entity.anInt1548 = 0;
			entity.x = entity.smallX[0] * 128 + entity.anInt1540 * 64;
			entity.y = entity.smallY[0] * 128 + entity.anInt1540 * 64;
			entity.method446();
		}
		if (entity == myPlayer && (entity.x < 1536 || entity.y < 1536 || entity.x >= 11776 || entity.y >= 11776)) {
			entity.anim = -1;
			entity.anInt1520 = -1;
			entity.anInt1547 = 0;
			entity.anInt1548 = 0;
			entity.x = entity.smallX[0] * 128 + entity.anInt1540 * 64;
			entity.y = entity.smallY[0] * 128 + entity.anInt1540 * 64;
			entity.method446();
		}
		if (entity.anInt1547 > loopCycle)
			method97(entity);
		else if (entity.anInt1548 >= loopCycle)
			method98(entity);
		else
			method99(entity);
		method100(entity);
		method101(entity);
	}

	private void method97(Entity entity) {
		int i = entity.anInt1547 - loopCycle;
		int j = entity.anInt1543 * 128 + entity.anInt1540 * 64;
		int k = entity.anInt1545 * 128 + entity.anInt1540 * 64;
		entity.x += (j - entity.x) / i;
		entity.y += (k - entity.y) / i;
		entity.anInt1503 = 0;
		if (entity.anInt1549 == 0)
			entity.turnDirection = 1024;
		if (entity.anInt1549 == 1)
			entity.turnDirection = 1536;
		if (entity.anInt1549 == 2)
			entity.turnDirection = 0;
		if (entity.anInt1549 == 3)
			entity.turnDirection = 512;
	}

	private void method98(Entity entity) {
		if (entity.anInt1548 == loopCycle || entity.anim == -1 || entity.anInt1529 != 0 || entity.anInt1528 + 1 > Animation.anims[entity.anim].method258(entity.anInt1527)) {
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
		if (entity.anInt1549 == 0)
			entity.turnDirection = 1024;
		if (entity.anInt1549 == 1)
			entity.turnDirection = 1536;
		if (entity.anInt1549 == 2)
			entity.turnDirection = 0;
		if (entity.anInt1549 == 3)
			entity.turnDirection = 512;
		entity.anInt1552 = entity.turnDirection;
	}

	private void method99(Entity entity) {
		entity.anInt1517 = entity.anInt1511;
		if (entity.smallXYIndex == 0) {
			entity.anInt1503 = 0;
			return;
		}
		if (entity.anim != -1 && entity.anInt1529 == 0) {
			Animation animation = Animation.anims[entity.anim];
			if (entity.anInt1542 > 0 && animation.anInt363 == 0) {
				entity.anInt1503++;
				return;
			}
			if (entity.anInt1542 <= 0 && animation.anInt364 == 0) {
				entity.anInt1503++;
				return;
			}
		}
		int i = entity.x;
		int j = entity.y;
		int k = entity.smallX[entity.smallXYIndex - 1] * 128 + entity.anInt1540 * 64;
		int l = entity.smallY[entity.smallXYIndex - 1] * 128 + entity.anInt1540 * 64;
		if (k - i > 256 || k - i < -256 || l - j > 256 || l - j < -256) {
			entity.x = k;
			entity.y = l;
			return;
		}
		if (i < k) {
			if (j < l)
				entity.turnDirection = 1280;
			else if (j > l)
				entity.turnDirection = 1792;
			else
				entity.turnDirection = 1536;
		} else if (i > k) {
			if (j < l)
				entity.turnDirection = 768;
			else if (j > l)
				entity.turnDirection = 256;
			else
				entity.turnDirection = 512;
		} else if (j < l)
			entity.turnDirection = 1024;
		else
			entity.turnDirection = 0;
		int i1 = entity.turnDirection - entity.anInt1552 & 0x7ff;
		if (i1 > 1024)
			i1 -= 2048;
		int j1 = entity.anInt1555;
		if (i1 >= -256 && i1 <= 256)
			j1 = entity.anInt1554;
		else if (i1 >= 256 && i1 < 768)
			j1 = entity.anInt1557;
		else if (i1 >= -768 && i1 <= -256)
			j1 = entity.anInt1556;
		if (j1 == -1)
			j1 = entity.anInt1554;
		entity.anInt1517 = j1;
		int k1 = 4;
		if (entity.anInt1552 != entity.turnDirection && entity.interactingEntity == -1 && entity.anInt1504 != 0)
			k1 = 2;
		if (entity.smallXYIndex > 2)
			k1 = 6;
		if (entity.smallXYIndex > 3)
			k1 = 8;
		if (entity.anInt1503 > 0 && entity.smallXYIndex > 1) {
			k1 = 8;
			entity.anInt1503--;
		}
		if (entity.aBooleanArray1553[entity.smallXYIndex - 1])
			k1 <<= 1;
		if (k1 >= 8 && entity.anInt1517 == entity.anInt1554 && entity.anInt1505 != -1)
			entity.anInt1517 = entity.anInt1505;
		if (i < k) {
			entity.x += k1;
			if (entity.x > k)
				entity.x = k;
		} else if (i > k) {
			entity.x -= k1;
			if (entity.x < k)
				entity.x = k;
		}
		if (j < l) {
			entity.y += k1;
			if (entity.y > l)
				entity.y = l;
		} else if (j > l) {
			entity.y -= k1;
			if (entity.y < l)
				entity.y = l;
		}
		if (entity.x == k && entity.y == l) {
			entity.smallXYIndex--;
			if (entity.anInt1542 > 0)
				entity.anInt1542--;
		}
	}

	private void method100(Entity entity) {
		if (entity.anInt1504 == 0)
			return;
		if (entity.interactingEntity != -1 && entity.interactingEntity < 32768) {
			NPC npc = npcArray[entity.interactingEntity];
			if (npc != null) {
				int i1 = entity.x - npc.x;
				int k1 = entity.y - npc.y;
				if (i1 != 0 || k1 != 0)
					entity.turnDirection = (int) (Math.atan2(i1, k1) * 325.94900000000001D) & 0x7ff;
			}
		}
		if (entity.interactingEntity >= 32768) {
			int j = entity.interactingEntity - 32768;
			if (j == unknownInt10)
				j = myPlayerIndex;
			Player player = playerArray[j];
			if (player != null) {
				int l1 = entity.x - player.x;
				int i2 = entity.y - player.y;
				if (l1 != 0 || i2 != 0)
					entity.turnDirection = (int) (Math.atan2(l1, i2) * 325.94900000000001D) & 0x7ff;
			}
		}
		if ((entity.anInt1538 != 0 || entity.anInt1539 != 0) && (entity.smallXYIndex == 0 || entity.anInt1503 > 0)) {
			int k = entity.x - (entity.anInt1538 - baseX - baseX) * 64;
			int j1 = entity.y - (entity.anInt1539 - baseY - baseY) * 64;
			if (k != 0 || j1 != 0)
				entity.turnDirection = (int) (Math.atan2(k, j1) * 325.94900000000001D) & 0x7ff;
			entity.anInt1538 = 0;
			entity.anInt1539 = 0;
		}
		int l = entity.turnDirection - entity.anInt1552 & 0x7ff;
		if (l != 0) {
			if (l < entity.anInt1504 || l > 2048 - entity.anInt1504)
				entity.anInt1552 = entity.turnDirection;
			else if (l > 1024)
				entity.anInt1552 -= entity.anInt1504;
			else
				entity.anInt1552 += entity.anInt1504;
			entity.anInt1552 &= 0x7ff;
			if (entity.anInt1517 == entity.anInt1511 && entity.anInt1552 != entity.turnDirection) {
				if (entity.anInt1512 != -1) {
					entity.anInt1517 = entity.anInt1512;
					return;
				}
				entity.anInt1517 = entity.anInt1554;
			}
		}
	}

	public void method101(Entity entity) {
		entity.aBoolean1541 = false;
		if (entity.anInt1517 != -1) {
			Animation animation = Animation.anims[entity.anInt1517];
			entity.anInt1519++;
			if (entity.anInt1518 < animation.anInt352 && entity.anInt1519 > animation.method258(entity.anInt1518)) {
				entity.anInt1519 = 0;
				entity.anInt1518++;
			}
			if (entity.anInt1518 >= animation.anInt352) {
				entity.anInt1519 = 0;
				entity.anInt1518 = 0;
			}
		}
		if (entity.anInt1520 != -1 && loopCycle >= entity.anInt1523) {
			if (entity.anInt1521 < 0)
				entity.anInt1521 = 0;
			Animation animation_1 = SpotAnim.cache[entity.anInt1520].aAnimation_407;
			for (entity.anInt1522++; entity.anInt1521 < animation_1.anInt352 && entity.anInt1522 > animation_1.method258(entity.anInt1521); entity.anInt1521++)
				entity.anInt1522 -= animation_1.method258(entity.anInt1521);

			if (entity.anInt1521 >= animation_1.anInt352 && (entity.anInt1521 < 0 || entity.anInt1521 >= animation_1.anInt352))
				entity.anInt1520 = -1;
		}
		if (entity.anim != -1 && entity.anInt1529 <= 1) {
			Animation animation_2 = Animation.anims[entity.anim];
			if (animation_2.anInt363 == 1 && entity.anInt1542 > 0 && entity.anInt1547 <= loopCycle && entity.anInt1548 < loopCycle) {
				entity.anInt1529 = 1;
				return;
			}
		}
		if (entity.anim != -1 && entity.anInt1529 == 0) {
			Animation animation_3 = Animation.anims[entity.anim];
			for (entity.anInt1528++; entity.anInt1527 < animation_3.anInt352 && entity.anInt1528 > animation_3.method258(entity.anInt1527); entity.anInt1527++)
				entity.anInt1528 -= animation_3.method258(entity.anInt1527);

			if (entity.anInt1527 >= animation_3.anInt352) {
				entity.anInt1527 -= animation_3.anInt356;
				entity.anInt1530++;
				if (entity.anInt1530 >= animation_3.anInt362)
					entity.anim = -1;
				if (entity.anInt1527 < 0 || entity.anInt1527 >= animation_3.anInt352)
					entity.anim = -1;
			}
			entity.aBoolean1541 = animation_3.aBoolean358;
		}
		if (entity.anInt1529 > 0)
			entity.anInt1529--;
	}

	private void drawGameScreen()
	{
		if (fullscreenInterfaceID != -1 && (loadingStage == 2 || super.fullGameScreen != null)) {
			if (loadingStage == 2) {
				method119(anInt945, fullscreenInterfaceID);
				if (openInterfaceID != -1) {
					method119(anInt945, openInterfaceID);
				}
				anInt945 = 0;
				resetAllImageProducers();
				super.fullGameScreen.initDrawingArea();
				Texture.anIntArray1472 = fullScreenTextureArray;
				DrawingArea.setAllPixelsToZero();
				welcomeScreenRaised = true;
				if (openInterfaceID != -1) {
					RSInterface rsInterface_1 = RSInterface.interfaceCache[openInterfaceID];
					if (rsInterface_1.width == 512 && rsInterface_1.height == 334 && rsInterface_1.type == 0) {
						rsInterface_1.width = (clientSize == 0 ? 765 : clientWidth);
						rsInterface_1.height = (clientSize == 0 ? 503 : clientHeight);
					}
					drawInterface(0, clientSize == 0 ? 0 : (clientWidth / 2) - 765 / 2, rsInterface_1,
							clientSize == 0 ? 8 : (clientHeight / 2) - 503 / 2);
				}
				RSInterface rsInterface = RSInterface.interfaceCache[fullscreenInterfaceID];
				if (rsInterface.width == 512 && rsInterface.height == 334 && rsInterface.type == 0) {
					rsInterface.width = (clientSize == 0 ? 765 : clientWidth);
					rsInterface.height = (clientSize == 0 ? 503 : clientHeight);
				}
				drawInterface(0, clientSize == 0 ? 0 : (clientWidth / 2) - 765 / 2, rsInterface,
						clientSize == 0 ? 8 : (clientHeight / 2) - 503 / 2);
			}
			drawCount++;
			super.fullGameScreen.drawGraphics(0, super.graphics, 0);
			return;
		} else {
			if (drawCount != 0) {
				resetImageProducers2();
			}
		}
		if(welcomeScreenRaised) {
			welcomeScreenRaised = false;
			if (clientSize == 0) {
				topFrame.drawGraphics(0, super.graphics, 0);
				leftFrame.drawGraphics(4, super.graphics, 0);
				//mapEdgeIP.drawGraphics(4, super.graphics, 516);
			}
			needDrawTabArea = true;
			inputTaken = true;
			tabAreaAltered = true;
			if(loadingStage != 2) {
				if (clientSize == 0) {
					aRSImageProducer_1165.drawGraphics(4, super.graphics, 4);
					aRSImageProducer_1164.drawGraphics(0, super.graphics, 516);
				} else {
					aRSImageProducer_1165.drawGraphics(0, super.graphics, 0);
				}
			}
		}
		if (!menuOpen) {
			processRightClick();
			drawTooltip();
		} else {
			drawMenu(4, 4);
		}
		if(menuOpen && menuScreenArea == 1) {
		}
		if(invOverlayInterfaceID != -1) {
			boolean flag1 = method119(anInt945, invOverlayInterfaceID);
			if(flag1) {
			}
		}
		if(atInventoryInterfaceType == 2) {
		}
		if(activeInterfaceType == 2) {
		}
		drawTabArea();
		if (backDialogID == -1)
        {
			aClass9_1059.scrollPosition = (short) (chatScrollMax - chatScrollPos - 114);
            if (clientSize == 0)
            {
            	if(super.mouseX > 478 && super.mouseX < 580 && super.mouseY > 342)
    				moveScroller(494, 110, super.mouseX - 0, super.mouseY - 348, aClass9_1059, 0, false, chatScrollMax);
            }
            else 
            	if (super.mouseX >= 460 && super.mouseX <= 513 && super.mouseY >= clientHeight - 158 && super.mouseY <= clientHeight - 44)
            {
            		moveScroller(496 - 16, 114, super.mouseX - 16, super.mouseY, aClass9_1059, clientHeight - 158, false, chatScrollMax);
            }
            int i = chatScrollMax - 114 - aClass9_1059.scrollPosition;
            if (i < 0)
                i = 0;
            if (i > chatScrollMax - 114)
                i = chatScrollMax - 114;
            if (chatScrollPos != i)
            {
                chatScrollPos = i;
                inputTaken = true;
            }
        }
		if(backDialogID != -1) {
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
		if(inputTaken) {
			drawChatArea();
		}
		if(loadingStage == 2)
			method146();
		if(loadingStage == 2) {
			if (clientSize == 0) {
				drawMinimap();
				aRSImageProducer_1164.drawGraphics(0, super.graphics, 516);
			}
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

	private boolean buildFriendsListMenu(RSInterface class9) {
		int i = class9.contentType;
		if (i >= 1 && i <= 200 || i >= 701 && i <= 900) {
			if (i >= 801)
				i -= 701;
			else if (i >= 701)
				i -= 601;
			else if (i >= 101)
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
		if (i >= 401 && i <= 500) {
			menuActionName[menuActionRow] = "Remove @whi@" + class9.message;
			menuActionID[menuActionRow] = 322;
			menuActionRow++;
			return true;
		} else {
			return false;
		}
	}

	private void method104() {
		Animable_Sub3 class30_sub2_sub4_sub3 = (Animable_Sub3) aClass19_1056.reverseGetFirst();
		for (; class30_sub2_sub4_sub3 != null; class30_sub2_sub4_sub3 = (Animable_Sub3) aClass19_1056.reverseGetNext())
			if (class30_sub2_sub4_sub3.anInt1560 != plane || class30_sub2_sub4_sub3.aBoolean1567)
				class30_sub2_sub4_sub3.unlink();
			else if (loopCycle >= class30_sub2_sub4_sub3.anInt1564) {
				class30_sub2_sub4_sub3.method454(anInt945);
				if (class30_sub2_sub4_sub3.aBoolean1567)
					class30_sub2_sub4_sub3.unlink();
				else
					worldController.method285(class30_sub2_sub4_sub3.anInt1560, 0, class30_sub2_sub4_sub3.anInt1563, -1, class30_sub2_sub4_sub3.anInt1562, 60, class30_sub2_sub4_sub3.anInt1561, class30_sub2_sub4_sub3, false);
			}

	}

	public void drawBlackBox(int xPos, int yPos) {
		DrawingArea.drawPixels(71, yPos - 1, xPos - 2, 0x726451, 1);
		DrawingArea.drawPixels(69, yPos, xPos + 174, 0x726451, 1);
		DrawingArea.drawPixels(1, yPos - 2, xPos - 2, 0x726451, 178);
		DrawingArea.drawPixels(1, yPos + 68, xPos, 0x726451, 174);
		DrawingArea.drawPixels(71, yPos - 1, xPos - 1, 0x2E2B23, 1);
		DrawingArea.drawPixels(71, yPos - 1, xPos + 175, 0x2E2B23, 1);
		DrawingArea.drawPixels(1, yPos - 1, xPos, 0x2E2B23, 175);
		DrawingArea.drawPixels(1, yPos + 69, xPos, 0x2E2B23, 175);
		DrawingArea.method335(0, yPos, 174, 68, 220, xPos);
	}

	public void drawOnBankInterface(){
		try {
			if(openInterfaceID == 5292 && RSInterface.interfaceCache[50021].message.equals("1")){//Sent on bank opening etc, refresh tabs
				int tabs = Integer.parseInt(RSInterface.interfaceCache[50022].message);//# of tabs used
				int tab = Integer.parseInt(RSInterface.interfaceCache[50023].message);//current tab selected
				for (int i = 0; i <= tabs; i++) {
					RSInterface.interfaceCache[50003+i].sprite1 = cacheSprite[71];
					RSInterface.interfaceCache[50003+i].tooltip = "Click here to select tab " + (int)(i+1);
				}
				for (int i = tabs+1; i <= 8; i++) {
					RSInterface.interfaceCache[50002+i].sprite1 = new Sprite("");
					RSInterface.interfaceCache[50002+i].tooltip = "";
				}
				if(tabs != 8) {
					RSInterface.interfaceCache[50003+tabs].sprite1 = cacheSprite[72];
					RSInterface.interfaceCache[50003+tabs].tooltip = "Drag an item here to create a new tab";
				}
				if(tab > 0) {
					RSInterface.interfaceCache[50002+tab].sprite1 = cacheSprite[70];
					RSInterface.interfaceCache[50002].sprite1 = cacheSprite[69];
				} else {
					RSInterface.interfaceCache[50002].sprite1 = cacheSprite[68];
					//TODO: Highlight first item
				}
				RSInterface.interfaceCache[50021].message = "0";
			}
		} catch (Exception e) {
		}
	}
	
	private void processBankClick(){
		int x = clientSize == 0 ? 0 : (clientWidth / 2) - 356;
		int y = clientSize == 0 ? 0 : (clientHeight / 2) - 230;
		if(anInt1084 == 5382 && super.mouseY >= 40+y && super.mouseY <= 77+y){//check if bank interface
			if (super.mouseX >= 28+x && super.mouseX <= 74+x){// tab 1
				stream.createFrame(214);
				stream.method433(50025);//5 = maintab
				stream.method424(0);
				stream.method433(anInt1085);//Selected item slot
				stream.method431(mouseInvInterfaceIndex);//unused
			}
			if (super.mouseX >= 75+x && super.mouseX <= 121+x){// tab 1
				stream.createFrame(214);
				stream.method433(50026);//tab # x 13 (originally movewindow)
				stream.method424(0);
				stream.method433(anInt1085);//Selected item slot
				stream.method431(mouseInvInterfaceIndex);//unused
			}
			if (super.mouseX >= 122+x && super.mouseX <= 168+x){// tab 2
				stream.createFrame(214);
				stream.method433(50027);//tab # x 13 (originally movewindow)
				stream.method424(0);
				stream.method433(anInt1085);//Selected item slot
				stream.method431(mouseInvInterfaceIndex);//unused

			}
			if (super.mouseX >= 169+x && super.mouseX <= 215+x){// tab 3
				stream.createFrame(214);
				stream.method433(50028);//tab # x 13 (originally movewindow)
				stream.method424(0);
				stream.method433(anInt1085);//Selected item slot
				stream.method431(mouseInvInterfaceIndex);//unused

			}
			if (super.mouseX >= 216+x && super.mouseX <= 262+x){// tab 4
				stream.createFrame(214);
				stream.method433(50029);//tab # x 13 (originally movewindow)
				stream.method424(0);
				stream.method433(anInt1085);//Selected item slot
				stream.method431(mouseInvInterfaceIndex);//unused

			}
			if (super.mouseX >= 263+x && super.mouseX <= 309+x){// tab 5
				stream.createFrame(214);
				stream.method433(50030);//tab # x 13 (originally movewindow)
				stream.method424(0);
				stream.method433(anInt1085);//Selected item slot
				stream.method431(mouseInvInterfaceIndex);//unused

			}
			if (super.mouseX >= 310+x && super.mouseX <= 356+x){// tab 6
				stream.createFrame(214);
				stream.method433(50031);//tab # x 13 (originally movewindow)
				stream.method424(0);
				stream.method433(anInt1085);//Selected item slot
				stream.method431(mouseInvInterfaceIndex);//unused

			}
			if (super.mouseX >= 357+x && super.mouseX <= 403+x){// tab 7
				stream.createFrame(214);
				stream.method433(50032);//tab # x 13 (originally movewindow)
				stream.method424(0);
				stream.method433(anInt1085);//Selected item slot
				stream.method431(mouseInvInterfaceIndex);//unused

			}
			if (super.mouseX >= 404+x && super.mouseX <= 450+x){// tab 8
				stream.createFrame(214);
				stream.method433(50033);//tab # x 13 (originally movewindow)
				stream.method424(0);
				stream.method433(anInt1085);//Selected item slot
				stream.method431(mouseInvInterfaceIndex);//unused

			}
		}
	}
	
	private void drawInterface(int j, int k, RSInterface class9, int l) {
		if (class9.parentID == 197 && clientSize != 0) {
			k = clientWidth - 120;
			l = 170;
		}
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
		for(int j2 = 0; j2 < i2; j2++) 	{
			int k2 = class9.childX[j2] + k;
			int l2 = (class9.childY[j2] + l) - j;
			RSInterface class9_1 = RSInterface.interfaceCache[class9.children[j2]];
			k2 += class9_1.anInt263;
			l2 += class9_1.anInt265;
			if(class9_1.contentType > 0)
				drawFriendsListOrWelcomeScreen(class9_1);
			if(class9_1.type == 0) {
				drawOnBankInterface();
				if(class9_1.scrollPosition > class9_1.scrollMax - class9_1.height)
					class9_1.scrollPosition = class9_1.scrollMax - class9_1.height;
				if(class9_1.scrollPosition < 0)
					class9_1.scrollPosition = 0;
				drawInterface(class9_1.scrollPosition, k2, class9_1, l2);
				if(class9_1.scrollMax > class9_1.height)
					drawScrollbar(class9_1.height, class9_1.scrollPosition, l2, k2 + class9_1.width, class9_1.scrollMax);
			} else if(class9_1.type != 1)
				if(class9_1.type == 2) {
					int i3 = 0;
					for(int l3 = 0; l3 < class9_1.height; l3++) {
						for(int l4 = 0; l4 < class9_1.width; l4++) {
							int k5 = k2 + l4 * (32 + class9_1.invSpritePadX);
							int j6 = l2 + l3 * (32 + class9_1.invSpritePadY);
							if(i3 < 20) {
								k5 += class9_1.spritesX[i3];
								j6 += class9_1.spritesY[i3];
							}
							if(class9_1.inv[i3] > 0) {
								int k6 = 0;
								int j7 = 0;
								int j9 = class9_1.inv[i3] - 1;
								if(k5 > DrawingArea.topX - 32 && k5 < DrawingArea.bottomX && j6 > DrawingArea.topY - 32 && j6 < DrawingArea.bottomY || activeInterfaceType != 0 && anInt1085 == i3) {
									int l9 = 0;
									if(itemSelected == 1 && anInt1283 == i3 && anInt1284 == class9_1.id)
										l9 = 0xffffff;
									Sprite class30_sub2_sub1_sub1_2 = ItemDef.getSprite(j9, class9_1.invStackSizes[i3], l9);
									if(class30_sub2_sub1_sub1_2 != null) {
										if(activeInterfaceType != 0 && anInt1085 == i3 && anInt1084 == class9_1.id) {
											k6 = super.mouseX - anInt1087;
											j7 = super.mouseY - anInt1088;
											if(k6 < 5 && k6 > -5)
												k6 = 0;
											if(j7 < 5 && j7 > -5)
												j7 = 0;
											if(anInt989 < 5) {
												k6 = 0;
												j7 = 0;
											}
											class30_sub2_sub1_sub1_2.drawSprite1(k5 + k6, j6 + j7);
											if(j6 + j7 < DrawingArea.topY && class9.scrollPosition > 0) {
												int i10 = (anInt945 * (DrawingArea.topY - j6 - j7)) / 3;
												if(i10 > anInt945 * 10)
													i10 = anInt945 * 10;
												if(i10 > class9.scrollPosition)
													i10 = class9.scrollPosition;
												class9.scrollPosition -= i10;
												anInt1088 += i10;
											}
											if(j6 + j7 + 32 > DrawingArea.bottomY && class9.scrollPosition < class9.scrollMax - class9.height) {
												int j10 = (anInt945 * ((j6 + j7 + 32) - DrawingArea.bottomY)) / 3;
												if(j10 > anInt945 * 10)
													j10 = anInt945 * 10;
												if(j10 > class9.scrollMax - class9.height - class9.scrollPosition)
													j10 = class9.scrollMax - class9.height - class9.scrollPosition;
												class9.scrollPosition += j10;
												anInt1088 -= j10;
											}
										} else if(atInventoryInterfaceType != 0 && atInventoryIndex == i3 && atInventoryInterface == class9_1.id)
											class30_sub2_sub1_sub1_2.drawSprite1(k5, j6);
										else
											class30_sub2_sub1_sub1_2.drawSprite(k5, j6);
										if(class30_sub2_sub1_sub1_2.anInt1444 == 33 || class9_1.invStackSizes[i3] != 1) {
											int k10 = class9_1.invStackSizes[i3];
											smallText.method385(0, intToKOrMil(k10), j6 + 10 + j7, k5 + 1 + k6);
											smallText.method385(0xffff00, intToKOrMil(k10), j6 + 9 + j7, k5 + k6);
										}
									}
								}
							} else if(class9_1.sprites != null && i3 < 20) {
								Sprite class30_sub2_sub1_sub1_1 = class9_1.sprites[i3];
								if(class30_sub2_sub1_sub1_1 != null)
									class30_sub2_sub1_sub1_1.drawSprite(k5, j6);
							}
							i3++;
						}
					}
				} else if(class9_1.type == 3) {
					boolean flag = false;
					if(anInt1039 == class9_1.id || anInt1048 == class9_1.id || anInt1026 == class9_1.id)
						flag = true;
					int j3;
					if(interfaceIsSelected(class9_1)) {
						j3 = class9_1.anInt219;
						if(flag && class9_1.anInt239 != 0)
							j3 = class9_1.anInt239;
					} else {
						j3 = class9_1.textColor;
						if(flag && class9_1.anInt216 != 0)
							j3 = class9_1.anInt216;
					}
					if(class9_1.aByte254 == 0) {
						if(class9_1.aBoolean227)
							DrawingArea.drawPixels(class9_1.height, l2, k2, j3, class9_1.width);
						else
							DrawingArea.fillPixels(k2, class9_1.width, class9_1.height, j3, l2);
					} else if(class9_1.aBoolean227)
						DrawingArea.method335(j3, l2, class9_1.width, class9_1.height, 256 - (class9_1.aByte254 & 0xff), k2);
					else
						DrawingArea.method338(l2, class9_1.height, 256 - (class9_1.aByte254 & 0xff), j3, class9_1.width, k2);
				} else if(class9_1.type == 4) {
					TextDrawingArea textDrawingArea = class9_1.textDrawingAreas;
					String s = class9_1.message;
					boolean flag1 = false;
					if(anInt1039 == class9_1.id || anInt1048 == class9_1.id || anInt1026 == class9_1.id)
						flag1 = true;
					int i4;
					if(interfaceIsSelected(class9_1)) {
						i4 = class9_1.anInt219;
						if(flag1 && class9_1.anInt239 != 0)
							i4 = class9_1.anInt239;
						if(class9_1.aString228.length() > 0)
							s = class9_1.aString228;
					} else {
						i4 = class9_1.textColor;
						if(flag1 && class9_1.anInt216 != 0)
							i4 = class9_1.anInt216;
					}
					if(class9_1.atActionType == 6 && aBoolean1149) {
						s = "Please wait...";
						i4 = class9_1.textColor;
					}
					if((backDialogID != -1 || dialogID != -1 || class9_1.message.contains("Click here to continue")) && (class9.id == backDialogID || class9.id == dialogID )){
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
				} else if(class9_1.type == 5)
				{
					Sprite sprite;
					if(interfaceIsSelected(class9_1))
						sprite = class9_1.sprite2;
					else
						sprite = class9_1.sprite1;
					if (sprite != null) {
						if(spellSelected == 1 && class9_1.id == spellID && spellID != 0)
							sprite.drawSprite(k2, l2, 0xffffff);
						else 
							sprite.drawSprite(k2, l2);
					}
				} else if(class9_1.type == 6) {
					int k3 = Texture.textureInt1;
					int j4 = Texture.textureInt2;
					Texture.textureInt1 = k2 + class9_1.width / 2;
					Texture.textureInt2 = l2 + class9_1.height / 2;
					int i5 = Texture.anIntArray1470[class9_1.modelRotation1] * class9_1.modelZoom >> 16;
					int l5 = Texture.anIntArray1471[class9_1.modelRotation1] * class9_1.modelZoom >> 16;
					boolean flag2 = interfaceIsSelected(class9_1);
					int i7;
					if(flag2)
						i7 = class9_1.anInt258;
					else
						i7 = class9_1.anInt257;
					Model model;
					if(i7 == -1) {
						model = class9_1.method209(-1, -1, flag2);
					} else {
						Animation animation = Animation.anims[i7];
						model = class9_1.method209(animation.anIntArray354[class9_1.anInt246], animation.anIntArray353[class9_1.anInt246], flag2);
					}
					if(model != null)
						model.method482(class9_1.modelRotation2, 0, class9_1.modelRotation1, 0, i5, l5);
					Texture.textureInt1 = k3;
					Texture.textureInt2 = j4;
				} else if(class9_1.type == 7) {
					TextDrawingArea textDrawingArea_1 = class9_1.textDrawingAreas;
					int k4 = 0;
					for(int j5 = 0; j5 < class9_1.height; j5++) {
						for(int i6 = 0; i6 < class9_1.width; i6++) {
							if(class9_1.inv[k4] > 0) {
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
				} else if (class9_1.type == 8
						&& (anInt1500 == class9_1.id
						|| anInt1044 == class9_1.id || anInt1129 == class9_1.id)
						&& anInt1501 == 0 && !menuOpen) {
					int boxWidth = 0;
					int boxHeight = 0;
					TextDrawingArea textDrawingArea_2 = aTextDrawingArea_1271;
					for (String s1 = class9_1.message; s1.length() > 0;) {
						if (s1.indexOf("%") != -1) {
							do {
								int k7 = s1.indexOf("%1");
								if (k7 == -1)
									break;
								s1 = s1.substring(0, k7)
										+ interfaceIntToString(extractInterfaceValues(
												class9_1, 0))
												+ s1.substring(k7 + 2);
							} while (true);
							do {
								int l7 = s1.indexOf("%2");
								if (l7 == -1)
									break;
								s1 = s1.substring(0, l7)
										+ interfaceIntToString(extractInterfaceValues(
												class9_1, 1))
												+ s1.substring(l7 + 2);
							} while (true);
							do {
								int i8 = s1.indexOf("%3");
								if (i8 == -1)
									break;
								s1 = s1.substring(0, i8)
										+ interfaceIntToString(extractInterfaceValues(
												class9_1, 2))
												+ s1.substring(i8 + 2);
							} while (true);
							do {
								int j8 = s1.indexOf("%4");
								if (j8 == -1)
									break;
								s1 = s1.substring(0, j8)
										+ interfaceIntToString(extractInterfaceValues(
												class9_1, 3))
												+ s1.substring(j8 + 2);
							} while (true);
							do {
								int k8 = s1.indexOf("%5");
								if (k8 == -1)
									break;
								s1 = s1.substring(0, k8)
										+ interfaceIntToString(extractInterfaceValues(
												class9_1, 4))
												+ s1.substring(k8 + 2);
							} while (true);
						}
						int l7 = s1.indexOf("\\n");
						String s4;
						if (l7 != -1) {
							s4 = s1.substring(0, l7);
							s1 = s1.substring(l7 + 2);
						} else {
							s4 = s1;
							s1 = "";
						}
						int j10 = textDrawingArea_2.getTextWidth(s4);
						if (j10 > boxWidth) {
							boxWidth = j10;
						}
						boxHeight += textDrawingArea_2.anInt1497 + 1;
					}
					boxWidth += 6;
					boxHeight += 7;
					int xPos = (k2 + class9_1.width) - 5 - boxWidth;
					int yPos = l2 + class9_1.height + 5;
					if (xPos < k2 + 5)
						xPos = k2 + 5;
					if (xPos + boxWidth > k + class9.width)
						xPos = (k + class9.width) - boxWidth;
					if (yPos + boxHeight > l + class9.height)
						yPos = (l2 - boxHeight);
					DrawingArea.drawPixels(boxHeight, yPos, xPos, 0xFFFFA0,
							boxWidth);
					DrawingArea.fillPixels(xPos, boxWidth, boxHeight, 0, yPos);
					String s2 = class9_1.message;
					for (int j11 = yPos + textDrawingArea_2.anInt1497 + 2; s2
							.length() > 0; j11 += textDrawingArea_2.anInt1497 + 1) {// anInt1497
						if (s2.indexOf("%") != -1) {
							do {
								int k7 = s2.indexOf("%1");
								if (k7 == -1)
									break;
								s2 = s2.substring(0, k7)
										+ interfaceIntToString(extractInterfaceValues(
												class9_1, 0))
												+ s2.substring(k7 + 2);
							} while (true);
							do {
								int l7 = s2.indexOf("%2");
								if (l7 == -1)
									break;
								s2 = s2.substring(0, l7)
										+ interfaceIntToString(extractInterfaceValues(
												class9_1, 1))
												+ s2.substring(l7 + 2);
							} while (true);
							do {
								int i8 = s2.indexOf("%3");
								if (i8 == -1)
									break;
								s2 = s2.substring(0, i8)
										+ interfaceIntToString(extractInterfaceValues(
												class9_1, 2))
												+ s2.substring(i8 + 2);
							} while (true);
							do {
								int j8 = s2.indexOf("%4");
								if (j8 == -1)
									break;
								s2 = s2.substring(0, j8)
										+ interfaceIntToString(extractInterfaceValues(
												class9_1, 3))
												+ s2.substring(j8 + 2);
							} while (true);
							do {
								int k8 = s2.indexOf("%5");
								if (k8 == -1)
									break;
								s2 = s2.substring(0, k8)
										+ interfaceIntToString(extractInterfaceValues(
												class9_1, 4))
												+ s2.substring(k8 + 2);
							} while (true);
						}
						int l11 = s2.indexOf("\\n");
						String s5;
						if (l11 != -1) {
							s5 = s2.substring(0, l11);
							s2 = s2.substring(l11 + 2);
						} else {
							s5 = s2;
							s2 = "";
						}
						if (class9_1.centerText) {
							textDrawingArea_2.method382(yPos, xPos
									+ class9_1.width / 2, s5, j11, false);
						} else {
							if (s5.contains("\\r")) {
								String text = s5
										.substring(0, s5.indexOf("\\r"));
								String text2 = s5
										.substring(s5.indexOf("\\r") + 2);
								textDrawingArea_2.method389(false, xPos + 3, 0,
										text, j11);
								int rightX = boxWidth + xPos
										- textDrawingArea_2.getTextWidth(text2)
										- 2;
								textDrawingArea_2.method389(false, rightX, 0,
										text2, j11);
								System.out.println("Box: " + boxWidth + "");
							} else
								textDrawingArea_2.method389(false, xPos + 3, 0,
										s5, j11);
						}
					}
				}
		}
		DrawingArea.setDrawingArea(l1, i1, k1, j1);
	}

	private void randomizeBackground(Background background) {
		int j = 256;
		for (int k = 0; k < anIntArray1190.length; k++)
			anIntArray1190[k] = 0;

		for (int l = 0; l < 5000; l++) {
			int i1 = (int) (Math.random() * 128D * (double) j);
			anIntArray1190[i1] = (int) (Math.random() * 256D);
		}
		for (int j1 = 0; j1 < 20; j1++) {
			for (int k1 = 1; k1 < j - 1; k1++) {
				for (int i2 = 1; i2 < 127; i2++) {
					int k2 = i2 + (k1 << 7);
					anIntArray1191[k2] = (anIntArray1190[k2 - 1] + anIntArray1190[k2 + 1] + anIntArray1190[k2 - 128] + anIntArray1190[k2 + 128]) / 4;
				}

			}
			int ai[] = anIntArray1190;
			anIntArray1190 = anIntArray1191;
			anIntArray1191 = ai;
		}
		if (background != null) {
			int l1 = 0;
			for (int j2 = 0; j2 < background.anInt1453; j2++) {
				for (int l2 = 0; l2 < background.anInt1452; l2++)
					if (background.aByteArray1450[l1++] != 0) {
						int i3 = l2 + 16 + background.anInt1454;
						int j3 = j2 + 16 + background.anInt1455;
						int k3 = i3 + (j3 << 7);
						anIntArray1190[k3] = 0;
					}
			}
		}
	}

	private void method107(int i, int j, Stream stream, Player player) {
		if ((i & 0x400) != 0) {
			player.anInt1543 = stream.method428();
			player.anInt1545 = stream.method428();
			player.anInt1544 = stream.method428();
			player.anInt1546 = stream.method428();
			player.anInt1547 = stream.method436() + loopCycle;
			player.anInt1548 = stream.method435() + loopCycle;
			player.anInt1549 = stream.method428();
			player.method446();
		}
		if ((i & 0x100) != 0) {
			player.anInt1520 = stream.method434();
			int k = stream.readDWord();
			player.anInt1524 = k >> 16;
						player.anInt1523 = loopCycle + (k & 0xffff);
						player.anInt1521 = 0;
						player.anInt1522 = 0;
						if (player.anInt1523 > loopCycle)
							player.anInt1521 = -1;
						if (player.anInt1520 == 65535)
							player.anInt1520 = -1;
		}
		if ((i & 8) != 0) {
			int l = stream.method434();
			if (l == 65535)
				l = -1;
			int i2 = stream.method427();
			if (l == player.anim && l != -1) {
				int i3 = Animation.anims[l].anInt365;
				if (i3 == 1) {
					player.anInt1527 = 0;
					player.anInt1528 = 0;
					player.anInt1529 = i2;
					player.anInt1530 = 0;
				}
				if (i3 == 2)
					player.anInt1530 = 0;
			} else if (l == -1 || player.anim == -1 || Animation.anims[l].anInt359 >= Animation.anims[player.anim].anInt359) {
				player.anim = l;
				player.anInt1527 = 0;
				player.anInt1528 = 0;
				player.anInt1529 = i2;
				player.anInt1530 = 0;
				player.anInt1542 = player.smallXYIndex;
			}
		}
		if ((i & 4) != 0) {
			player.textSpoken = stream.readString();
			if (player.textSpoken.charAt(0) == '~') {
				player.textSpoken = player.textSpoken.substring(1);
				pushMessage(player.textSpoken, 2, player.name);
			} else if (player == myPlayer)
				pushMessage(player.textSpoken, 2, player.name);
			player.anInt1513 = 0;
			player.anInt1531 = 0;
			player.textCycle = 150;
		}
		if ((i & 0x80) != 0) {
			// right fucking here
			int i1 = stream.method434();
			int j2 = stream.readUnsignedByte();
			int j3 = stream.method427();
			int k3 = stream.currentOffset;
			if (player.name != null && player.visible) {
				long l3 = TextClass.longForName(player.name);
				boolean flag = false;
				if (j2 <= 1) {
					for (int i4 = 0; i4 < ignoreCount; i4++) {
						if (ignoreListAsLongs[i4] != l3)
							continue;
						flag = true;
						break;
					}

				}
				if (!flag && anInt1251 == 0)
					try {
						aStream_834.currentOffset = 0;
						stream.method442(j3, 0, aStream_834.buffer);
						aStream_834.currentOffset = 0;
						String s = TextInput.method525(j3, aStream_834);
						// s = Censor.doCensor(s);
						player.textSpoken = s;
						player.anInt1513 = i1 >> 8;
					player.privelage = j2;
					player.anInt1531 = i1 & 0xff;
					player.textCycle = 150;
					if (j2 == 2 || j2 == 3)
						pushMessage(s, 1, "@cr2@" + player.name);
					else if (j2 == 1)
						pushMessage(s, 1, "@cr1@" + player.name);
					else
						pushMessage(s, 2, player.name);
					} catch (Exception exception) {
						Signlink.reporterror("cde2");
					}
			}
			stream.currentOffset = k3 + j3;
		}
		if ((i & 1) != 0) {
			player.interactingEntity = stream.method434();
			if (player.interactingEntity == 65535)
				player.interactingEntity = -1;
		}
		if ((i & 0x10) != 0) {
			int j1 = stream.method427();
			byte abyte0[] = new byte[j1];
			Stream stream_1 = new Stream(abyte0);
			stream.readBytes(j1, 0, abyte0);
			aStreamArray895s[j] = stream_1;
			player.updatePlayer(stream_1);
		}
		if ((i & 2) != 0) {
			player.anInt1538 = stream.method436();
			player.anInt1539 = stream.method434();
		}
		if ((i & 0x20) != 0) {
			int k1 = stream.readUnsignedByte();
			int k2 = stream.method426();
			player.updateHitData(k2, k1, loopCycle);
			player.loopCycleStatus = loopCycle + 300;
			player.currentHealth = stream.method427();
			player.maxHealth = stream.readUnsignedByte();
		}
		if ((i & 0x200) != 0) {
			int l1 = stream.readUnsignedByte();
			int l2 = stream.method428();
			player.updateHitData(l2, l1, loopCycle);
			player.loopCycleStatus = loopCycle + 300;
			player.currentHealth = stream.readUnsignedByte();
			player.maxHealth = stream.method427();
		}
	}

	private void method108() {
		try {
			int j = myPlayer.x + anInt1278;
			int k = myPlayer.y + anInt1131;
			if (anInt1014 - j < -500 || anInt1014 - j > 500 || anInt1015 - k < -500 || anInt1015 - k > 500) {
				anInt1014 = j;
				anInt1015 = k;
			}
			if (anInt1014 != j)
				anInt1014 += (j - anInt1014) / 16;
			if (anInt1015 != k)
				anInt1015 += (k - anInt1015) / 16;
			if (super.keyArray[1] == 1)
				anInt1186 += (-24 - anInt1186) / 2;
			else if (super.keyArray[2] == 1)
				anInt1186 += (24 - anInt1186) / 2;
			else
				anInt1186 /= 2;
			if (super.keyArray[3] == 1)
				anInt1187 += (12 - anInt1187) / 2;
			else if (super.keyArray[4] == 1)
				anInt1187 += (-12 - anInt1187) / 2;
			else
				anInt1187 /= 2;
			minimapInt1 = minimapInt1 + anInt1186 / 2 & 0x7ff;
			anInt1184 += anInt1187 / 2;
			if (anInt1184 < 128)
				anInt1184 = 128;
			if (anInt1184 > 383)
				anInt1184 = 383;
			int l = anInt1014 >> 7;
					int i1 = anInt1015 >> 7;
				int j1 = method42(plane, anInt1015, anInt1014);
				int k1 = 0;
				if (l > 3 && i1 > 3 && l < 100 && i1 < 100) {
					for (int l1 = l - 4; l1 <= l + 4; l1++) {
						for (int k2 = i1 - 4; k2 <= i1 + 4; k2++) {
							int l2 = plane;
							if (l2 < 3 && (byteGroundArray[1][l1][k2] & 2) == 2)
								l2++;
							int i3 = j1 - intGroundArray[l2][l1][k2];
							if (i3 > k1)
								k1 = i3;
						}

					}

				}
				anInt1005++;
				if (anInt1005 > 1512) {
					anInt1005 = 0;
					stream.createFrame(77);
					stream.writeWordBigEndian(0);
					int i2 = stream.currentOffset;
					stream.writeWordBigEndian((int) (Math.random() * 256D));
					stream.writeWordBigEndian(101);
					stream.writeWordBigEndian(233);
					stream.writeWord(45092);
					if ((int) (Math.random() * 2D) == 0)
						stream.writeWord(35784);
					stream.writeWordBigEndian((int) (Math.random() * 256D));
					stream.writeWordBigEndian(64);
					stream.writeWordBigEndian(38);
					stream.writeWord((int) (Math.random() * 65536D));
					stream.writeWord((int) (Math.random() * 65536D));
					stream.writeBytes(stream.currentOffset - i2);
				}
				int j2 = k1 * 192;
				if (j2 > 0x17f00)
					j2 = 0x17f00;
				if (j2 < 32768)
					j2 = 32768;
				if (j2 > anInt984) {
					anInt984 += (j2 - anInt984) / 24;
					return;
				}
				if (j2 < anInt984) {
					anInt984 += (j2 - anInt984) / 80;
				}
		} catch (Exception _ex) {
			Signlink.reporterror("glfc_ex " + myPlayer.x + "," + myPlayer.y + "," + anInt1014 + "," + anInt1015 + "," + anInt1069 + "," + anInt1070 + "," + baseX + "," + baseY);
			throw new RuntimeException("eek");
		}
	}

	public void processDrawing() {
		if (rsAlreadyLoaded || loadingError || genericLoadingError) {
			showErrorScreen();
			return;
		}
		if (!loggedIn)
			drawLoginScreen(false);
		else
			drawGameScreen();
		anInt1213 = 0;
	}

	private boolean isFriendOrSelf(String s) {
		if (s == null)
			return false;
		for (int i = 0; i < friendsCount; i++)
			if (s.equalsIgnoreCase(friendsList[i]))
				return true;
		return s.equalsIgnoreCase(myPlayer.name);
	}

	private static String combatDiffColor(int i, int j) {
		int k = i - j;
		if (k < -9)
			return "@red@";
		if (k < -6)
			return "@or3@";
		if (k < -3)
			return "@or2@";
		if (k < 0)
			return "@or1@";
		if (k > 9)
			return "@gre@";
		if (k > 6)
			return "@gr3@";
		if (k > 3)
			return "@gr2@";
		if (k > 0)
			return "@gr1@";
		else
			return "@yel@";
	}

	private void setWaveVolume(int i) {
		Signlink.wavevol = i;
	}

	public static int returnGeneralInterfaceOffsetX()
	{
		return (int) (clientWidth <= 1000 ? (clientWidth / 2) - 256 - (clientWidth * 0.1) : (clientWidth / 2) - 256);
	}
	
	private boolean fullyFaded = false;
	private int curFadeAlpha = 0;
	private int maxFadeAlpha = 255;
	private void fadeToBlack()
	{
		if(!fullyFaded)
		{
			curFadeAlpha += 5;
			if(curFadeAlpha >= maxFadeAlpha)
			{
				curFadeAlpha = maxFadeAlpha;
				fullyFaded = true;
			}
		}
		if(curFadeAlpha <= 0){
			return;
		}
		DrawingArea.method335(0, 0, clientWidth, clientHeight, curFadeAlpha, 0);
	}
	
	private void resetFade()
	{
		fullyFaded = false;
		curFadeAlpha = 0;
	}

	private static final int lightingAreas[][] = {
			{3082, 3132, 3324, 3384, 90, 0}
		};
	public static int[] lightingForArea(int x, int y) {
		int[] setting = new int[2];
		setting[0] = 0;
		setting[1] = 0;
		for(int j = 0; j < lightingAreas.length; j++) {
			if(x > lightingAreas[j][0] && x < lightingAreas[j][1] && y > lightingAreas[j][2] && y < lightingAreas[j][3]) {
				setting[0] = lightingAreas[j][4];
				setting[1] = lightingAreas[j][5];
				return setting;
			}
		}
		return setting;
	}
	private int curAreaAlpha = 0;
	private int areaAlpha = 0;
	private int areaColor = 0;
	private void handleLighting(){
		int x = baseX + (myPlayer.x - 6 >> 7);
		int y = baseY + (myPlayer.y - 6 >> 7);
		int[] lightSettings = lightingForArea(x, y);
		areaAlpha = lightSettings[0];
		areaColor = lightSettings[1];
		if(curAreaAlpha >= 0){
			if(areaAlpha < curAreaAlpha)
			{
				if(curAreaAlpha > areaAlpha)
					curAreaAlpha -= 2;
			}else if(areaAlpha > curAreaAlpha)
			{
				if(curAreaAlpha < areaAlpha)
					curAreaAlpha += 2;
			}
		}
		if(curAreaAlpha <= 0){
			return;
		}
		DrawingArea.method335(areaColor, 0, clientWidth, clientHeight, curAreaAlpha, 0);
	}
	
	private void draw3dScreen() {
		if (showChat)
			drawSplitPrivateChat();

		if (crossType == 1)
		{
			crosses[crossIndex / 100].drawSprite(crossX - 8 - (clientSize == 0 ? 4 : 0), crossY - 8 - (clientSize == 0 ? 4 : 0));
		}
		if (crossType == 2)
			crosses[4 + crossIndex / 100].drawSprite(crossX - 8 - (clientSize == 0 ? 4 : 0), crossY - 8 - (clientSize == 0 ? 4 : 0));
		if (anInt1055 == 1)
			multiWay.drawSprite((clientSize == 0 ? 472 : (anInt1018 == 197 ? clientWidth - 268 : clientWidth - 255)), (clientSize == 0 ? 296 : (anInt1018 == 197 ? 48 : 20)));
		if (anInt1018 != -1)
		{
			method119(anInt945, anInt1018);
			if ((anInt1018 == 11146 || anInt1018 == 24126 || anInt1018 == 24131) && clientSize != 0)
			{
				drawInterface(0, 0, RSInterface.interfaceCache[anInt1018], -5);
			}
			else if ((anInt1018 == 2804 || anInt1018 == 11479) && clientSize != 0)
			{
				drawInterface(0, clientWidth / 2 - 256, RSInterface.interfaceCache[anInt1018], -5);
			}
			else if (anInt1018 == 4535 && clientSize != 0)
			{
				drawInterface(0, -418, RSInterface.interfaceCache[anInt1018], -285);
			}
			else if ((anInt1018 == 15892 || anInt1018 == 15917 || anInt1018 == 15931 || anInt1018 == 15962) && clientSize != 0)
			{
				drawInterface(0, (anInt1018 == 15892 ? -325 : -349), RSInterface.interfaceCache[anInt1018], 25);
			}
			else
				drawInterface(0, clientSize == 0 ? 0 : (clientWidth / 2) - -80, RSInterface.interfaceCache[anInt1018], clientSize == 0 ? 0 : (clientHeight / 2) - 550);
		}
		if (openInterfaceID != -1)
		{
			method119(anInt945, openInterfaceID);
			if (openInterfaceID == 8677)
			{
				fadeToBlack();
			}else{
				drawInterface(0, clientSize == 0 ? 0 : (clientWidth / 2) - 356, RSInterface.interfaceCache[openInterfaceID], clientSize == 0 ? 0 : (clientHeight / 2) - 230);
			}
		}
		method70();
		if (!menuOpen)
		{
			processRightClick();
			drawTooltip();
		}
		else
			drawMenu((clientSize == 0 ? 4 : 0), (clientSize == 0 ? 4 : 0));
		/*drawSplitPrivateChat();
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
			drawInterface(0, clientSize == 0 ? 0 : (clientWidth / 2) - -80, RSInterface.interfaceCache[anInt1018], clientSize == 0 ? 0 : (clientHeight / 2) - 550);
			//drawInterface(0, 0, RSInterface.interfaceCache[anInt1018], 0);
		}
		if(openInterfaceID != -1)
		{
			method119(anInt945, openInterfaceID);
			drawInterface(0, clientSize == 0 ? 0 : (clientWidth / 2) - 256, RSInterface.interfaceCache[openInterfaceID], clientSize == 0 ? 0 : (clientHeight / 2) - 167);
			//drawInterface(0, 0, RSInterface.interfaceCache[openInterfaceID], 0);
		}
		method70();
		if(!menuOpen)
		{

			processRightClick();

			drawTooltip();
		} else
		if(menuScreenArea == 0)
			drawMenu(4, 4);
		if(anInt1055 == 1)
			headIcons[1].drawSprite(472, 296);*/
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
		int x = baseX + (myPlayer.x - 6 >> 7);
		int y = baseY + (myPlayer.y - 6 >> 7);
		if (clientData) {
			int calculateWidth = clientWidth - super.mouseX;
			int calculateHeight = clientHeight - super.mouseY;
			aTextDrawingArea_1271.method385(0xffff00, "Dimensions: " + clientWidth + " x " +  clientHeight, clientSize == 0 ? 225 : showChat ? clientHeight - 277 : clientHeight - 140, 5);
			aTextDrawingArea_1271.method385(0xffff00, "Mouse Positions: " + "clientWidth - " + calculateWidth + ", " + "clientHeight - " + calculateHeight, clientSize == 0 ? 240 : showChat ? clientHeight - 262 : clientHeight - 125, 5);
			for(int num = 0; num < anIntArray1235.length; num++) {
				int[] flo = anIntArray1235;
				aTextDrawingArea_1271.method385(0xffff00, "Floor Map Array: " + Arrays.toString(flo), clientSize == 0 ? 255 : showChat ? clientHeight - 247 : clientHeight - 110, 5);
			} 
			for(int num = 0; num < anIntArray1236.length; num++) {
				int[] obj = anIntArray1236;
				aTextDrawingArea_1271.method385(0xffff00,"Object Map Array: " + Arrays.toString(obj), clientSize == 0 ? 270 : showChat ? clientHeight - 232 : clientHeight - 95, 5);
			}
			aTextDrawingArea_1271.method385(0xffff00, "Fps: " + super.fps, clientSize == 0 ? 285 : showChat ? clientHeight - 217 : clientHeight - 80, 5);
			Runtime runtime = Runtime.getRuntime();
			int j1 = (int)((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
			aTextDrawingArea_1271.method385(0xffff00, "Mem: " + j1 + "k", clientSize == 0 ? 299 : showChat ? clientHeight - 202 : clientHeight - 65, 5);
			aTextDrawingArea_1271.method385(0xffff00, "Mouse X: " + super.mouseX + " , Mouse Y: " + super.mouseY, clientSize == 0 ? 314 : showChat ? clientHeight - 187 : clientHeight - 50, 5);
			aTextDrawingArea_1271.method385(0xffff00, "Coords: " + x + ", " + y, clientSize == 0 ? 329 : showChat ? clientHeight - 172 : clientHeight - 35, 5);
		}
		if(anInt1104 != 0)
		{
			int j = anInt1104 / 50;
			int l = j / 60;
			j %= 60;
			if(j < 10)
				aTextDrawingArea_1271.method385(0xffff00, "System update in: " + l + ":0" + j, 329, 4);
			else
				aTextDrawingArea_1271.method385(0xffff00, "System update in: " + l + ":" + j, 329, 4);
			anInt849++;
			if(anInt849 > 75)
			{
				anInt849 = 0;
				stream.createFrame(148);
			}
		}
	}

	private void addIgnore(long l) {
		try {
			if (l == 0L)
				return;
			if (ignoreCount >= 100) {
				pushMessage("Your ignore list is full. Max of 100 hit", 0, "");
				return;
			}
			String s = TextClass.fixName(TextClass.nameForLong(l));
			for (int j = 0; j < ignoreCount; j++)
				if (ignoreListAsLongs[j] == l) {
					pushMessage(s + " is already on your ignore list", 0, "");
					return;
				}
			for (int k = 0; k < friendsCount; k++)
				if (friendsListAsLongs[k] == l) {
					pushMessage("Please remove " + s + " from your friend list first", 0, "");
					return;
				}

			ignoreListAsLongs[ignoreCount++] = l;
			needDrawTabArea = true;
			stream.createFrame(133);
			stream.writeQWord(l);
			return;
		} catch (RuntimeException runtimeexception) {
			Signlink.reporterror("45688, " + l + ", " + 4 + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	private void method114() {
		for (int i = -1; i < playerCount; i++) {
			int j;
			if (i == -1)
				j = myPlayerIndex;
			else
				j = playerIndices[i];
			Player player = playerArray[j];
			if (player != null)
				method96(player);
		}

	}

	private void method115() {
		if (loadingStage == 2) {
			for (Class30_Sub1 class30_sub1 = (Class30_Sub1) aClass19_1179.reverseGetFirst(); class30_sub1 != null; class30_sub1 = (Class30_Sub1) aClass19_1179.reverseGetNext()) {
				if (class30_sub1.anInt1294 > 0)
					class30_sub1.anInt1294--;
				if (class30_sub1.anInt1294 == 0) {
					if (class30_sub1.anInt1299 < 0 || ObjectManager.method178(class30_sub1.anInt1299, class30_sub1.anInt1301)) {
						method142(class30_sub1.anInt1298, class30_sub1.anInt1295, class30_sub1.anInt1300, class30_sub1.anInt1301, class30_sub1.anInt1297, class30_sub1.anInt1296, class30_sub1.anInt1299);
						class30_sub1.unlink();
					}
				} else {
					if (class30_sub1.anInt1302 > 0)
						class30_sub1.anInt1302--;
					if (class30_sub1.anInt1302 == 0 && class30_sub1.anInt1297 >= 1 && class30_sub1.anInt1298 >= 1 && class30_sub1.anInt1297 <= 102 && class30_sub1.anInt1298 <= 102 && (class30_sub1.anInt1291 < 0 || ObjectManager.method178(class30_sub1.anInt1291, class30_sub1.anInt1293))) {
						method142(class30_sub1.anInt1298, class30_sub1.anInt1295, class30_sub1.anInt1292, class30_sub1.anInt1293, class30_sub1.anInt1297, class30_sub1.anInt1296, class30_sub1.anInt1291);
						class30_sub1.anInt1302 = -1;
						if (class30_sub1.anInt1291 == class30_sub1.anInt1299 && class30_sub1.anInt1299 == -1)
							class30_sub1.unlink();
						else if (class30_sub1.anInt1291 == class30_sub1.anInt1299 && class30_sub1.anInt1292 == class30_sub1.anInt1300 && class30_sub1.anInt1293 == class30_sub1.anInt1301)
							class30_sub1.unlink();
					}
				}
			}

		}
	}

	private void determineMenuSize() {
		int boxLength = chatTextDrawingArea.getTextWidth("Choose option");
		for(int row = 0; row < menuActionRow; row++) {
			int actionLength = chatTextDrawingArea.getTextWidth(menuActionName[row]);
			if(actionLength > boxLength)
				boxLength = actionLength;
		}
		boxLength += 8;
		int offset = 15 * menuActionRow + 21;
		if(super.saveClickX > 0 && super.saveClickY > 0 && super.saveClickX < 765 && super.saveClickY < 503) {
			int xClick = super.saveClickX - boxLength / 2;
			if(xClick + boxLength > 761) {
				xClick = 761 - boxLength;
			}
			if(xClick < 0){
				xClick = 0;
			}
			int yClick = super.saveClickY - 0;
			if(yClick + offset > 497) {
				yClick = 497 - offset;
			}
			if(yClick < 0) {
				yClick = 0;
			}
			menuOpen = true;
			menuOffsetX = xClick;
			menuOffsetY = yClick;
			menuWidth = boxLength;
			menuHeight = 15 * menuActionRow + 22;
		}
		if (clientSize != 0) {
			if (super.saveClickX > 0 && super.saveClickY > 0 && super.saveClickX < clientWidth && super.saveClickY < clientHeight) {
				int i1 = super.saveClickX - 0 - boxLength / 2;
				if (i1 + boxLength > clientWidth)
					i1 = clientWidth - boxLength;
				if (i1 < 0)
					i1 = 0;
				int l1 = super.saveClickY - 0;
				if (l1 + offset > clientHeight)
					l1 = clientHeight - offset;
				if (l1 < 0)
					l1 = 0;
				menuOpen = true;
				menuScreenArea = 0;
				menuOffsetX = i1;
				menuOffsetY = l1;
				menuWidth = boxLength;
				menuHeight = 15 * menuActionRow + 22;
			}
		}
	}

	public int centerX = clientWidth / 2;
	public int centerY = clientHeight / 2;

	public void refreshClientScreen() {
		aRSImageProducer_1109 = new RSImageProducer(clientWidth, clientHeight, getGameComponent());
		DrawingArea.setAllPixelsToZero();
	}

	public void checkClientSize() {
		clientWidth = getGameComponent().getWidth();
		clientHeight = getGameComponent().getHeight();
		centerX = clientWidth / 2;
		centerY = clientHeight / 2;
	}
/*
	private void method117(Stream stream) {
		stream.initBitAccess();
		int j = stream.readBits(1);
		if (j == 0)
			return;
		int k = stream.readBits(2);
		if (k == 0) {
			anIntArray894[anInt893++] = myPlayerIndex;
			return;
		}
		if (k == 1) {
			int l = stream.readBits(3);
			myPlayer.moveInDir(false, l);
			int k1 = stream.readBits(1);
			if (k1 == 1)
				anIntArray894[anInt893++] = myPlayerIndex;
			return;
		}
		if (k == 2) {
			int i1 = stream.readBits(3);
			myPlayer.moveInDir(true, i1);
			int l1 = stream.readBits(3);
			myPlayer.moveInDir(true, l1);
			int j2 = stream.readBits(1);
			if (j2 == 1)
				anIntArray894[anInt893++] = myPlayerIndex;
			return;
		}
		if (k == 3) {
			plane = stream.readBits(2);
			int j1 = stream.readBits(1);
			int i2 = stream.readBits(1);
			if (i2 == 1)
				anIntArray894[anInt893++] = myPlayerIndex;
			int k2 = stream.readBits(7);
			int l2 = stream.readBits(7);
			myPlayer.setPos(l2, k2, j1 == 1);
		}
	}*/

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
	
	private void nullLoader() {
		aBoolean831 = false;
		while (drawingFlames) {
			aBoolean831 = false;
			try {
				Thread.sleep(50L);
			} catch (Exception _ex) {
			}
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

	private boolean method119(int i, int j) {
		boolean flag1 = false;
		RSInterface class9 = RSInterface.interfaceCache[j];
		for (int k = 0; k < class9.children.length; k++) {
			if (class9.children[k] == -1)
				break;
			RSInterface class9_1 = RSInterface.interfaceCache[class9.children[k]];
			if (class9_1.type == 1)
				flag1 |= method119(i, class9_1.id);
			if (class9_1.type == 6 && (class9_1.anInt257 != -1 || class9_1.anInt258 != -1)) {
				boolean flag2 = interfaceIsSelected(class9_1);
				int l;
				if (flag2)
					l = class9_1.anInt258;
				else
					l = class9_1.anInt257;
				if (l != -1) {
					Animation animation = Animation.anims[l];
					for (class9_1.anInt208 += i; class9_1.anInt208 > animation.method258(class9_1.anInt246);) {
						class9_1.anInt208 -= animation.method258(class9_1.anInt246) + 1;
						class9_1.anInt246++;
						if (class9_1.anInt246 >= animation.anInt352) {
							class9_1.anInt246 -= animation.anInt356;
							if (class9_1.anInt246 < 0 || class9_1.anInt246 >= animation.anInt352)
								class9_1.anInt246 = 0;
						}
						flag1 = true;
					}

				}
			}
		}

		return flag1;
	}

	private int method120() {
		if (roofsToggled) {
			return plane;
		}
		int j = 3;
		if (yCameraCurve < 310) {
			int k = xCameraPos >> 7;
		int l = yCameraPos >> 7;
					int i1 = myPlayer.x >> 7;
				int j1 = myPlayer.y >> 7;
				if ((byteGroundArray[plane][k][l] & 4) != 0)
					j = plane;
				int k1;
				if (i1 > k)
					k1 = i1 - k;
				else
					k1 = k - i1;
				int l1;
				if (j1 > l)
					l1 = j1 - l;
				else
					l1 = l - j1;
				if (k1 > l1) {
					int i2 = (l1 * 0x10000) / k1;
					int k2 = 32768;
					while (k != i1) {
						if (k < i1)
							k++;
						else if (k > i1)
							k--;
						if ((byteGroundArray[plane][k][l] & 4) != 0)
							j = plane;
						k2 += i2;
						if (k2 >= 0x10000) {
							k2 -= 0x10000;
							if (l < j1)
								l++;
							else if (l > j1)
								l--;
							if ((byteGroundArray[plane][k][l] & 4) != 0)
								j = plane;
						}
					}
				} else {
					int j2 = (k1 * 0x10000) / l1;
					int l2 = 32768;
					while (l != j1) {
						if (l < j1)
							l++;
						else if (l > j1)
							l--;
						if ((byteGroundArray[plane][k][l] & 4) != 0)
							j = plane;
						l2 += j2;
						if (l2 >= 0x10000) {
							l2 -= 0x10000;
							if (k < i1)
								k++;
							else if (k > i1)
								k--;
							if ((byteGroundArray[plane][k][l] & 4) != 0)
								j = plane;
						}
					}
				}
		}
		if ((byteGroundArray[plane][myPlayer.x >> 7][myPlayer.y >> 7] & 4) != 0)
			j = plane;
		return j;
	}

	private int method121() {
		if (roofsToggled) {
			return plane;
		}
		int j = method42(plane, yCameraPos, xCameraPos);
		if (j - zCameraPos < 800 && (byteGroundArray[plane][xCameraPos >> 7][yCameraPos >> 7] & 4) != 0)
			return plane;
		else
			return 3;
	}

	private void delIgnore(long l) {
		try {
			if (l == 0L)
				return;
			for (int j = 0; j < ignoreCount; j++)
				if (ignoreListAsLongs[j] == l) {
					ignoreCount--;
					needDrawTabArea = true;
					System.arraycopy(ignoreListAsLongs, j + 1, ignoreListAsLongs, j, ignoreCount - j);

					stream.createFrame(74);
					stream.writeQWord(l);
					return;
				}

			return;
		} catch (RuntimeException runtimeexception) {
			Signlink.reporterror("47229, " + 3 + ", " + l + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	private void chatJoin(long l) {
		try {
			if (l == 0L)
				return;
			stream.createFrame(60);
			stream.writeQWord(l);
			return;
		} catch (RuntimeException runtimeexception) {
			Signlink.reporterror("47229, " + 3 + ", " + l + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();

	}

	public String getParameter(String s) {
		if (Signlink.mainapp != null)
			return Signlink.mainapp.getParameter(s);
		else
			return super.getParameter(s);
	}
/*
	private void adjustVolume(boolean flag, int i) {
		Signlink.midiVolume = i;
		if (flag)
			Signlink.midi = "voladjust";
	}*/
	
	public void playSong(int id) {
	    if(MusicEnabled) {
		if (currentSong != id) {
			nextSong = id;
			songChanging = true;
			onDemandFetcher.method558(2, nextSong);
			currentSong = id;
		}
	    }
	}
/*
	public void stopMidi() {
		Signlink.music.stop();
		Signlink.midifade = 0;
		Signlink.midi = "stop";
	}
	*/
	private void stopMidi()
	{
	    if(MusicEnabled) {
		MidiPlayer.getSingleton().stop();
		Signlink.midifade = 0;
		Signlink.midi = "stop";
	    }
	}

/*	private void adjustVolume(boolean updateMidi, int volume) {
		Signlink.setVolume(volume);
		if (updateMidi) {
			Signlink.midi = "voladjust";
		}
	}*/
	
	private void adjustVolume(boolean flag, int i)
	{
	    if(MusicEnabled) {
		MidiPlayer.getSingleton().setVolume(0, i);
		Signlink.midiVolume = i;
		if(flag){
			Signlink.midi = "voladjust";
		}
	    }
	}


	private int extractInterfaceValues(RSInterface class9, int j) {
		if (class9.valueIndexArray == null || j >= class9.valueIndexArray.length)
			return -2;
		try {
			int ai[] = class9.valueIndexArray[j];
			int k = 0;
			int l = 0;
			int i1 = 0;
			do {
				int j1 = ai[l++];
				int k1 = 0;
				byte byte0 = 0;
				if (j1 == 0)
					return k;
				if (j1 == 1)
					k1 = currentStats[ai[l++]];
				if (j1 == 2)
					k1 = maxStats[ai[l++]];
				if (j1 == 3)
					k1 = currentExp[ai[l++]];
				if (j1 == 4) {
					RSInterface class9_1 = RSInterface.interfaceCache[ai[l++]];
					int k2 = ai[l++];
					if (k2 >= 0 && k2 < ItemDef.totalItems && (!ItemDef.forID(k2).membersObject || isMembers)) {
						for (int j3 = 0; j3 < class9_1.inv.length; j3++)
							if (class9_1.inv[j3] == k2 + 1)
								k1 += class9_1.invStackSizes[j3];

					}
				}
				if (j1 == 5)
					k1 = variousSettings[ai[l++]];
				if (j1 == 6)
					k1 = anIntArray1019[maxStats[ai[l++]] - 1];
				if (j1 == 7)
					k1 = (variousSettings[ai[l++]] * 100) / 46875;
				if (j1 == 8)
					k1 = myPlayer.combatLevel;
				if (j1 == 9) {
					for (int l1 = 0; l1 < Skills.skillsCount; l1++)
						if (Skills.skillEnabled[l1])
							k1 += maxStats[l1];

				}
				if (j1 == 10) {
					RSInterface class9_2 = RSInterface.interfaceCache[ai[l++]];
					int l2 = ai[l++] + 1;
					if (l2 >= 0 && l2 < ItemDef.totalItems && isMembers) {
						for (int k3 = 0; k3 < class9_2.inv.length; k3++) {
							if (class9_2.inv[k3] != l2)
								continue;
							k1 = 0x3b9ac9ff;
							break;
						}

					}
				}
				if (j1 == 11)
					k1 = energy;
				if (j1 == 12)
					k1 = weight;
				if (j1 == 13) {
					int i2 = variousSettings[ai[l++]];
					int i3 = ai[l++];
					k1 = (i2 & 1 << i3) == 0 ? 0 : 1;
				}
				if (j1 == 14) {
					int j2 = ai[l++];
					VarBit varBit = VarBit.cache[j2];
					int l3 = varBit.anInt648;
					int i4 = varBit.anInt649;
					int j4 = varBit.anInt650;
					int k4 = anIntArray1232[j4 - i4];
					k1 = variousSettings[l3] >> i4 & k4;
				}
				if (j1 == 15)
					byte0 = 1;
				if (j1 == 16)
					byte0 = 2;
				if (j1 == 17)
					byte0 = 3;
				if (j1 == 18)
					k1 = (myPlayer.x >> 7) + baseX;
				if (j1 == 19)
					k1 = (myPlayer.y >> 7) + baseY;
				if (j1 == 20)
					k1 = ai[l++];
				if (byte0 == 0) {
					if (i1 == 0)
						k += k1;
					if (i1 == 1)
						k -= k1;
					if (i1 == 2 && k1 != 0)
						k /= k1;
					if (i1 == 3)
						k *= k1;
					i1 = 0;
				} else {
					i1 = byte0;
				}
			} while (true);
		} catch (Exception _ex) {
			return -1;
		}
	}

	private void drawTooltip() {
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

	private void markMinimap(Sprite sprite, int i, int j) {
		int k = minimapInt1 + minimapInt2 & 0x7ff;
		int l = sprite.myWidth + sprite.myHeight + i * i + j * j;
		if (l > 4850 && clientSize != 0)
			return;
		int sine = Model.modelIntArray1[k];
		int cosine = Model.modelIntArray2[k];
		sine = (sine * 256) / (minimapInt3 + 256);
		cosine = (cosine * 256) / (minimapInt3 + 256);
		int k1 = j * sine + i * cosine >> 16;
		int l1 = j * cosine - i * sine >> 16;
		sprite.drawSprite((((clientSize == 0 ? 129 - 5 : clientWidth + 129 - 5 - 238) + k1) - sprite.anInt1444 / 2) + 4, (clientSize == 0 ? 88 : 88 + 3) - l1 - sprite.anInt1445 / 2 - 4);
	}

	private void drawMinimap() {
		if(clientSize == 0) {
			aRSImageProducer_1164.initDrawingArea();
		}
		if(clientSize != 0) {
			for(int i = 0; i < 76; i++) {
				int amt = (int)(Math.sqrt(Math.pow(77, 2) - Math.pow(75-i, 2)));
				anIntArray1229[i] = 2 * amt + 2;
				anIntArray1229[150-i] = 2 * amt + 2;
				anIntArray1052[i] = -amt + 73;
				anIntArray1052[150-i] = -amt + 73;
			}
		}
		if(clientSize == 0) {
			for (int x = 0; x < anIntArray1229.length; x++){
				anIntArray1229[x] = 170;
				anIntArray1052[x] = -23;
			}
		}
		
		if(anInt1021 == 2) {
		/*	byte abyte0[] = mapBack.aByteArray1450;
			int ai[] = DrawingArea.pixels;
			int k2 = abyte0.length;
			for(int i5 = 0; i5 < k2; i5++)
				if(abyte0[i5] == 0)
					ai[i5] = 0;*/
			if(clientSize == 0) {
				cacheSprite[32].drawSprite(0, 0);
			}
			if(clientSize != 0) {
				cacheSprite[34].drawSprite(clientWidth - 238, 3);
			}
			compass.method352(33, minimapInt1, anIntArray1057, 256, anIntArray968, 25, (clientSize == 0 ? 4 : 4 + 3), (clientSize == 0 ? 34 - 5 : clientWidth + 34 - 5 - 238), 33, 25);
			drawOrbs();
			aRSImageProducer_1165.initDrawingArea();
			return;
		}
		int i = minimapInt1 + minimapInt2 & 0x7ff;
		int j = 48 + myPlayer.x / 32;
		int l2 = 464 - myPlayer.y / 32;
		minimapImage.method352(151, i, anIntArray1229, 256 + minimapInt3, anIntArray1052, l2, (clientSize == 0 ? 9 : 12), (clientSize == 0 ? 54 : clientWidth + 59 - 5 - 238), 146, j);
		
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
				if(entityDef != null && entityDef.drawMapDot && entityDef.hasActions) {
					int i1 = npc.x / 32 - myPlayer.x / 32;
					int k3 = npc.y / 32 - myPlayer.y / 32;
					markMinimap(mapDotNPC, i1, k3);
				}
			}
		}
		for (int j6 = 0; j6 < playerCount; j6++) {
			Player player = playerArray[playerIndices[j6]];
			if (player != null && player.isVisible()) {
				int j1 = player.x / 32 - myPlayer.x / 32;
				int l3 = player.y / 32 - myPlayer.y / 32;
				boolean flag1 = false;
				boolean flag3 = false;
				for (int j3 = 0; j3 < clanList.length; j3++) {
					if (clanList[j3] == null)
						continue;
					if (!clanList[j3].equalsIgnoreCase(player.name))
						continue;
					flag3 = true;
					break;
				}
				long l6 = TextClass.longForName(player.name);
				for (int k6 = 0; k6 < friendsCount; k6++) {
					if (l6 != friendsListAsLongs[k6] || friendsNodeIDs[k6] == 0)
						continue;
					flag1 = true;
					break;
				}
				boolean flag2 = false;
				if (myPlayer.team != 0 && player.team != 0
						&& myPlayer.team == player.team)
					flag2 = true;
				if (flag1)
					markMinimap(mapDotFriend, j1, l3);
				else if (flag3)
					markMinimap(mapDotClan, j1, l3);
				else if (flag2)
					markMinimap(mapDotTeam, j1, l3);
				else
					markMinimap(mapDotPlayer, j1, l3);
			}
		}
		if(anInt855 != 0 && loopCycle % 20 < 10) {
			if(anInt855 == 1 && anInt1222 >= 0 && anInt1222 < npcArray.length) {
				NPC class30_sub2_sub4_sub1_sub1_1 = npcArray[anInt1222];
				if(class30_sub2_sub4_sub1_sub1_1 != null) {
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
			if(anInt855 == 10 && anInt933 >= 0 && anInt933 < playerArray.length) {
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
		
		if(clientSize != 0) {
			cacheSprite[33].drawSprite(clientWidth - 238, 3);
			compass.method352(33, minimapInt1, anIntArray1057, 256, anIntArray968, 25, (clientSize == 0 ? 4 : 4 + 3), (clientSize == 0 ? 34 - 5 : clientWidth + 34 - 5 - 238), 33, 25);
		}

		DrawingArea.drawPixels(3, (clientSize == 0 ? 9 : 12) + 74, (clientSize == 0 ? 130 - 5 : clientWidth + 130 - 5 - 238), 0xffffff, 3);
		
		if (clientSize == 0) {
			cacheSprite[24].drawSprite(0, 0);
			cacheSprite[26].method352(33, minimapInt1, anIntArray1057, 256, anIntArray968, 25, 4, 29, 33, 25);
		}
		
		drawOrbs();
		
		if(menuOpen && clientSize == 0) 
			drawMenu(516, 0);
	     else if (menuOpen && menuScreenArea == 3 && clientSize != 0) 
			drawMenu(516, 0);
		
		aRSImageProducer_1165.initDrawingArea();
	}

	private void npcScreenPos(Entity entity, int i) {
		calcEntityScreenPos(entity.x, i, entity.y);
	}

	private void calcEntityScreenPos(int i, int j, int l) {
		if (i < 128 || l < 128 || i > 13056 || l > 13056) {
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
		if (l >= 50) {
			spriteDrawX = Texture.textureInt1 + (i << log_view_dist) / l;
			spriteDrawY = Texture.textureInt2 + (i1 << log_view_dist) / l;
		} else {
			spriteDrawX = -1;
			spriteDrawY = -1;
		}
	}
	
	private void drawOrbs()
	{
		drawPrayerOrb();
		drawHPOrb();
	}
	
	private void drawPrayerOrb()
	{
		try {
			int currentPP = currentStats[5];
            int maxPP = maxStats[5];
            int prayer = (int)(((double) currentPP / (double) maxPP) * 100D);
            double percent = (prayer / 100D);
	        int x = (clientSize == 0 ? 190 : clientWidth - 260);
	        int y = (clientSize == 0 ? 45 : 75);
			cacheSprite[(clientSize == 0 ? 54 : 53)].drawSprite(x, y);
			cacheSprite[50].drawSprite(x+(clientSize == 0 ? 3 : 27), y+3);
			Sprite orbFill = cacheSprite[56];
			int depleteFill = 27 - (int)(27 * percent);
			orbFill.myHeight = depleteFill;
			orbFill.height = depleteFill;
			orbFill.drawSprite(x+(clientSize == 0 ? 3 : 27),y+3);
			int colour;
			if(prayer >= 75)
				colour = 65280;
			else if(prayer >= 50)
				colour = 0xffff00;
			else if(prayer >= 25)
				colour = 0xfca607;
			else
				colour = 0xf50d0d;
			smallText.method382(colour, x+(clientSize == 0 ? 41 : 14), currentPP+"", y+26, true);
			cacheSprite[52].drawSprite(x+(clientSize == 0 ? 7 : 30), y+7);
		} catch (Exception e) { }
	}
	
	private void drawHPOrb()
	{
		try {
			int currentHP = currentStats[3];
            int maxHP = maxStats[3];
            int health = (int)(((double) currentHP / (double) maxHP) * 100D);
            double percent = (health / 100D);
	        int x = (clientSize == 0 ? 180 : clientWidth - 250);
	        int y = (clientSize == 0 ? 10 : 40);
			cacheSprite[(clientSize == 0 ? 54 : 53)].drawSprite(x, y);
			cacheSprite[49].drawSprite(x+(clientSize == 0 ? 3 : 27), y+3);
			Sprite orbFill = cacheSprite[56];
			int depleteFill = 27 - (int)(27 * percent);
			orbFill.myHeight = depleteFill;
			orbFill.height = depleteFill;
			orbFill.drawSprite(x+(clientSize == 0 ? 3 : 27),y+3);
			int colour;
			if(health >= 75)
				colour = 65280;
			else if(health >= 50)
				colour = 0xffff00;
			else if(health >= 25)
				colour = 0xfca607;
			else
				colour = 0xf50d0d;
			smallText.method382(colour, x+(clientSize == 0 ? 41 : 14), currentHP+"", y+26, true);
			cacheSprite[51].drawSprite(x+(clientSize == 0 ? 10 : 33), y+10);
		} catch (Exception e) { }
	}

	private void buildSplitPrivateChatMenu() {
		if (splitPrivateChat == 0)
			return;
		int i = 0;
		if (anInt1104 != 0)
			i = 1;
		for (int j = 0; j < 100; j++)
			if (chatMessages[j] != null) {
				int k = chatTypes[j];
				String s = chatNames[j];
				if (s != null && s.startsWith("@cr1@")) {
					s = s.substring(5);
				}
				if (s != null && s.startsWith("@cr2@")) {
					s = s.substring(5);
				}
				if ((k == 3 || k == 7) && (k == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(s))) {
					int l = 329 - i * 13;
					if (super.mouseX > 4 && super.mouseY - 4 > l - 10 && super.mouseY - 4 <= l + 3) {
						int i1 = aTextDrawingArea_1271.getTextWidth("From:  " + s + chatMessages[j]) + 25;
						if (i1 > 450)
							i1 = 450;
						if (super.mouseX < 4 + i1) {
							if (myPrivilege >= 1) {
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
					if (++i >= 5)
						return;
				}
				if ((k == 5 || k == 6) && privateChatMode < 2 && ++i >= 5)
					return;
			}

	}

	private void method130(int j, int k, int l, int i1, int j1, int k1, int l1, int i2, int j2) {
		Class30_Sub1 class30_sub1 = null;
		for (Class30_Sub1 class30_sub1_1 = (Class30_Sub1) aClass19_1179.reverseGetFirst(); class30_sub1_1 != null; class30_sub1_1 = (Class30_Sub1) aClass19_1179.reverseGetNext()) {
			if (class30_sub1_1.anInt1295 != l1 || class30_sub1_1.anInt1297 != i2 || class30_sub1_1.anInt1298 != j1 || class30_sub1_1.anInt1296 != i1)
				continue;
			class30_sub1 = class30_sub1_1;
			break;
		}

		if (class30_sub1 == null) {
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

	private boolean interfaceIsSelected(RSInterface class9) {
		if (class9.anIntArray245 == null)
			return false;
		for (int i = 0; i < class9.anIntArray245.length; i++) {
			int j = extractInterfaceValues(class9, i);
			int k = class9.anIntArray212[i];
			if (class9.anIntArray245[i] == 2) {
				if (j >= k)
					return false;
			} else if (class9.anIntArray245[i] == 3) {
				if (j <= k)
					return false;
			} else if (class9.anIntArray245[i] == 4) {
				if (j == k)
					return false;
			} else if (j != k)
				return false;
		}

		return true;
	}

	private DataInputStream openJagGrabInputStream(String s) throws IOException {
		if (aSocket832 != null) {
			try {
				aSocket832.close();
			} catch (Exception _ex) {
			}
			aSocket832 = null;
		}
		aSocket832 = openSocket(43595);
		aSocket832.setSoTimeout(10000);
		java.io.InputStream inputstream = aSocket832.getInputStream();
		OutputStream outputstream = aSocket832.getOutputStream();
		outputstream.write(("JAGGRAB /" + s + "\n\n").getBytes());
		return new DataInputStream(inputstream);
	}
/*
	private void doFlamesDrawing() {
		char c = '\u0100';
		if (anInt1040 > 0) {
			for (int i = 0; i < 256; i++)
				if (anInt1040 > 768)
					anIntArray850[i] = method83(anIntArray851[i], anIntArray852[i], 1024 - anInt1040);
				else if (anInt1040 > 256)
					anIntArray850[i] = anIntArray852[i];
				else
					anIntArray850[i] = method83(anIntArray852[i], anIntArray851[i], 256 - anInt1040);

		} else if (anInt1041 > 0) {
			for (int j = 0; j < 256; j++)
				if (anInt1041 > 768)
					anIntArray850[j] = method83(anIntArray851[j], anIntArray853[j], 1024 - anInt1041);
				else if (anInt1041 > 256)
					anIntArray850[j] = anIntArray853[j];
				else
					anIntArray850[j] = method83(anIntArray853[j], anIntArray851[j], 256 - anInt1041);

		} else {
			System.arraycopy(anIntArray851, 0, anIntArray850, 0, 256);

		}
		System.arraycopy(aClass30_Sub2_Sub1_Sub1_1201.myPixels, 0, aRSImageProducer_1110.anIntArray315, 0, 33920);

		int i1 = 0;
		int j1 = 1152;
		for (int k1 = 1; k1 < c - 1; k1++) {
			int l1 = (anIntArray969[k1] * (c - k1)) / c;
			int j2 = 22 + l1;
			if (j2 < 0)
				j2 = 0;
			i1 += j2;
			for (int l2 = j2; l2 < 128; l2++) {
				int j3 = anIntArray828[i1++];
				if (j3 != 0) {
					int l3 = j3;
					int j4 = 256 - j3;
					j3 = anIntArray850[j3];
					int l4 = aRSImageProducer_1110.anIntArray315[j1];
					aRSImageProducer_1110.anIntArray315[j1++] = ((j3 & 0xff00ff) * l3 + (l4 & 0xff00ff) * j4 & 0xff00ff00) + ((j3 & 0xff00) * l3 + (l4 & 0xff00) * j4 & 0xff0000) >> 8;
				} else {
					j1++;
				}
			}

			j1 += j2;
		}

		aRSImageProducer_1110.drawGraphics(0, super.graphics, 0);
		System.arraycopy(aClass30_Sub2_Sub1_Sub1_1202.myPixels, 0, aRSImageProducer_1111.anIntArray315, 0, 33920);

		i1 = 0;
		j1 = 1176;
		for (int k2 = 1; k2 < c - 1; k2++) {
			int i3 = (anIntArray969[k2] * (c - k2)) / c;
			int k3 = 103 - i3;
			j1 += i3;
			for (int i4 = 0; i4 < k3; i4++) {
				int k4 = anIntArray828[i1++];
				if (k4 != 0) {
					int i5 = k4;
					int j5 = 256 - k4;
					k4 = anIntArray850[k4];
					int k5 = aRSImageProducer_1111.anIntArray315[j1];
					aRSImageProducer_1111.anIntArray315[j1++] = ((k4 & 0xff00ff) * i5 + (k5 & 0xff00ff) * j5 & 0xff00ff00) + ((k4 & 0xff00) * i5 + (k5 & 0xff00) * j5 & 0xff0000) >> 8;
				} else {
					j1++;
				}
			}

			i1 += 128 - k3;
			j1 += 128 - k3 - i3;
		}

		aRSImageProducer_1111.drawGraphics(0, super.graphics, 637);
	}
*/
	private void method134(Stream stream) {
		int j = stream.readBits(8);
		if (j < playerCount) {
			for (int k = j; k < playerCount; k++)
				anIntArray840[anInt839++] = playerIndices[k];

		}
		if (j > playerCount) {
			Signlink.reporterror(myUsername + " Too many players");
			throw new RuntimeException("eek");
		}
		playerCount = 0;
		for (int l = 0; l < j; l++) {
			int i1 = playerIndices[l];
			Player player = playerArray[i1];
			int j1 = stream.readBits(1);
			if (j1 == 0) {
				playerIndices[playerCount++] = i1;
				player.anInt1537 = loopCycle;
			} else {
				int k1 = stream.readBits(2);
				if (k1 == 0) {
					playerIndices[playerCount++] = i1;
					player.anInt1537 = loopCycle;
					anIntArray894[anInt893++] = i1;
				} else if (k1 == 1) {
					playerIndices[playerCount++] = i1;
					player.anInt1537 = loopCycle;
					int l1 = stream.readBits(3);
					player.moveInDir(false, l1);
					int j2 = stream.readBits(1);
					if (j2 == 1)
						anIntArray894[anInt893++] = i1;
				} else if (k1 == 2) {
					playerIndices[playerCount++] = i1;
					player.anInt1537 = loopCycle;
					int i2 = stream.readBits(3);
					player.moveInDir(true, i2);
					int k2 = stream.readBits(3);
					player.moveInDir(true, k2);
					int l2 = stream.readBits(1);
					if (l2 == 1)
						anIntArray894[anInt893++] = i1;
				} else if (k1 == 3)
					anIntArray840[anInt839++] = i1;
			}
		}
	}
	
	public static boolean rememberMe = false;
	public static String savedUsername = "";
	public static String savedPassword = "";

	public void writeSettings() throws IOException {
		try {
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(Signlink.findcachedir() + "settings.dat")));
		out.writeUTF(savedUsername);
		out.writeUTF(savedPassword);
		out.writeUTF(Boolean.toString(rememberMe));
		out.writeUTF(Boolean.toString(loginMusicEnabled));
		out.flush();
		out.close();
		} catch(Exception e) {	}
	}  
	
	public void readSettings() throws IOException {
		try {
		DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(Signlink.findcachedir() + "settings.dat")));
		savedUsername = in.readUTF();
		myUsername = savedUsername;
		savedPassword = in.readUTF();
		myPassword = savedPassword;
		rememberMe = Boolean.parseBoolean(in.readUTF());
		loginMusicEnabled = Boolean.parseBoolean(in.readUTF());
		in.close();
		} catch(Exception e) {}
	}
	
	private void drawLoginScreen(boolean flag) {
	    refreshClientScreen();
	    if (loginScreenState == 2) {
	    	if(background != null)
	    		background.drawARGBSprite((clientWidth / 2) - (background.myWidth / 2), (clientHeight / 2) - (background.myHeight / 2));
			
	    	if (MusicEnabled) {
			    if (loginMusicEnabled) {
				cacheSprite[47].drawSprite(clientWidth - 52, 10);
			    } else {
				cacheSprite[48].drawSprite(clientWidth - 52, 10);
			    }
			}
			cacheSprite[35].drawARGBSprite(centerX - 145, centerY - 135);
			if (mouseInRegion(centerX - 110, centerY - 64, centerX + 115, centerY - 38)) {
			    cacheSprite[36].drawSprite(centerX - 110, centerY - 64);
			}
			if (mouseInRegion(centerX - 110, centerY - 19, centerX + 115, centerY + 8)) {
			    cacheSprite[36].drawSprite(centerX - 110, centerY - 17);
			}
			if (mouseInRegion(centerX - 90, centerY + 38, centerX + 99, centerY + 64)) {
			    cacheSprite[37].drawSprite(centerX - 90, centerY + 39);
			}
			TextDrawingArea textDrawingArea = aTextDrawingArea_1271;
			chatTextDrawingArea.method389(true, centerX - 105, 0xf3b13f, myUsername + ((loginScreenCursorPos == 0) & (loopCycle % 40 < 20) ? "|" : ""), centerY - 43);
			chatTextDrawingArea.method389(true, centerX - 105, 0xf3b13f, TextClass.passwordAsterisks(myPassword) + ((loginScreenCursorPos == 1) & (loopCycle % 40 < 20) ? "|" : ""), centerY + 5);
			chatTextDrawingArea.method389(true, centerX - (textDrawingArea.getTextWidth(loginMessage1) / 2) - (loginMessage1.length() > 25 ? 16 : loginMessage1.length() > 20 ? 14 : loginMessage1.length() > 15 ? 12 : 10), 0xf3b13f, loginMessage1, centerY + 90);
			chatTextDrawingArea.method389(true, centerX - (textDrawingArea.getTextWidth(loginMessage2) / 2) - (loginMessage2.length() > 25 ? 16 : loginMessage2.length() > 20 ? 14 : loginMessage2.length() > 15 ? 12 : 10), 0xf3b13f, loginMessage2, centerY + 113);
	
			if (rememberMe) {
			    cacheSprite[39].drawSprite(centerX - 108, centerY + 12);
			} else {
			    cacheSprite[38].drawSprite(centerX - 108, centerY + 12);
			    if (mouseInRegion(centerX - 108, centerY + 12, centerX - 80, centerY + 24)) {
			    	cacheSprite[40].drawSprite(centerX - 108, centerY + 12);
			    }
			}
			chatTextDrawingArea.method389(true, centerX - 92, 0xf3b13f, "Remember Me", centerY + 24);
			
			if(outDated)
			{
				chatTextDrawingArea.method389(true, centerX-90, 0xff0000, "New Client Version available",  centerY - 90);
			}
	    }
	    aRSImageProducer_1109.drawGraphics(0, super.graphics, 0);
	}
	
	private void checkClientVersion() throws IOException {
		String versionURL = "https://dl.dropboxusercontent.com/u/31306161/vscape/clientVersion.dat";
    	BufferedReader cacheVerReader = new BufferedReader(new InputStreamReader(new URL(versionURL).openStream()));
		String line;
		try {
			if((line = cacheVerReader.readLine()) != null)
			{
				outDated = !line.equalsIgnoreCase(CLIENT_VERSION);
			}
			cacheVerReader.close();
		} catch(IOException e) {
			System.out.println("problem reading remote client version");
			cacheVerReader.close();
		}
	}
	
	private void drawFlames() {
	/*	drawingFlames = true;
		try {
			long l = System.currentTimeMillis();
			int i = 0;
			int j = 20;
			while (aBoolean831) {
				calcFlamesPosition();
				calcFlamesPosition();
				doFlamesDrawing();
				if (++i > 10) {
					long l1 = System.currentTimeMillis();
					int k = (int) (l1 - l) / 10 - j;
					j = 40 - k;
					if (j < 5)
						j = 5;
					i = 0;
					l = l1;
				}
				try {
					Thread.sleep(j);
				} catch (Exception _ex) {
				}
			}
		} catch (Exception _ex) {
		}
		drawingFlames = false;*/
	}

	public void raiseWelcomeScreen() {
		welcomeScreenRaised = true;
	}

	private void method137(Stream stream, int j) {
		if (j == 84) {
			int k = stream.readUnsignedByte();
			int j3 = anInt1268 + (k >> 4 & 7);
			int i6 = anInt1269 + (k & 7);
			int l8 = stream.readUnsignedWord();
			int k11 = stream.readUnsignedWord();
			int l13 = stream.readUnsignedWord();
			if (j3 >= 0 && i6 >= 0 && j3 < 104 && i6 < 104) {
				NodeList class19_1 = groundArray[plane][j3][i6];
				if (class19_1 != null) {
					for (Item class30_sub2_sub4_sub2_3 = (Item) class19_1.reverseGetFirst(); class30_sub2_sub4_sub2_3 != null; class30_sub2_sub4_sub2_3 = (Item) class19_1.reverseGetNext()) {
						if (class30_sub2_sub4_sub2_3.ID != (l8 & 0x7fff) || class30_sub2_sub4_sub2_3.anInt1559 != k11)
							continue;
						class30_sub2_sub4_sub2_3.anInt1559 = l13;
						break;
					}

					spawnGroundItem(j3, i6);
				}
			}
			return;
		}
		if (j == 105) {
		/*	int l = stream.readUnsignedByte();
			int k3 = anInt1268 + (l >> 4 & 7);
			int j6 = anInt1269 + (l & 7);
			int i9 = stream.readUnsignedWord();
			int l11 = stream.readUnsignedByte();
			int i14 = l11 >> 4 & 0xf;
				int i16 = l11 & 7;
				if (myPlayer.smallX[0] >= k3 - i14 && myPlayer.smallX[0] <= k3 + i14 && myPlayer.smallY[0] >= j6 - i14 && myPlayer.smallY[0] <= j6 + i14 && aBoolean848 && !lowMem && anInt1062 < 50) {
					anIntArray1207[anInt1062] = i9;
					anIntArray1241[anInt1062] = i16;
					anIntArray1250[anInt1062] = Sounds.anIntArray326[i9];
					anInt1062++;
				}*/
            int k = stream.readUnsignedByte();
            int j3 = anInt1268 + (k >> 4 & 7);
            int i6 = anInt1269 + (k & 7);
            int l8 = stream.readUnsignedWord();
            int k11 = stream.readUnsignedByte();
            int l13 = k11 >> 4 & 0xf;
            int l15 = k11 & 7;
            if(myPlayer.smallX[0] >= j3 - l13 && myPlayer.smallX[0] <= j3 + l13 && myPlayer.smallY[0] >= i6 - l13 && myPlayer.smallY[0] <= i6 + l13 && aBoolean848 && !lowMem && currentSound < 50)
            {
                sound[currentSound] = l8;
                soundType[currentSound] = l15;
                soundDelay[currentSound] = Sounds.anIntArray326[l8];
                currentSound++;
            }
		}
		if (j == 215) {
			int i1 = stream.method435();
			int l3 = stream.method428();
			int k6 = anInt1268 + (l3 >> 4 & 7);
			int j9 = anInt1269 + (l3 & 7);
			int i12 = stream.method435();
			int j14 = stream.readUnsignedWord();
			if (k6 >= 0 && j9 >= 0 && k6 < 104 && j9 < 104 && i12 != unknownInt10) {
				Item class30_sub2_sub4_sub2_2 = new Item();
				class30_sub2_sub4_sub2_2.ID = i1;
				class30_sub2_sub4_sub2_2.anInt1559 = j14;
				if (groundArray[plane][k6][j9] == null)
					groundArray[plane][k6][j9] = new NodeList();
				groundArray[plane][k6][j9].insertHead(class30_sub2_sub4_sub2_2);
				spawnGroundItem(k6, j9);
			}
			return;
		}
		if (j == 156) {
			int j1 = stream.method426();
			int i4 = anInt1268 + (j1 >> 4 & 7);
			int l6 = anInt1269 + (j1 & 7);
			int k9 = stream.readUnsignedWord();
			if (i4 >= 0 && l6 >= 0 && i4 < 104 && l6 < 104) {
				NodeList class19 = groundArray[plane][i4][l6];
				if (class19 != null) {
					for (Item item = (Item) class19.reverseGetFirst(); item != null; item = (Item) class19.reverseGetNext()) {
						if (item.ID != (k9 & 0x7fff))
							continue;
						item.unlink();
						break;
					}

					if (class19.reverseGetFirst() == null)
						groundArray[plane][i4][l6] = null;
					spawnGroundItem(i4, l6);
				}
			}
			return;
		}
		if (j == 160) {
			int k1 = stream.method428();
			int j4 = anInt1268 + (k1 >> 4 & 7);
			int i7 = anInt1269 + (k1 & 7);
			int l9 = stream.method428();
			int j12 = l9 >> 2;
			int k14 = l9 & 3;
			int j16 = anIntArray1177[j12];
			int j17 = stream.method435();
			if (j4 >= 0 && i7 >= 0 && j4 < 103 && i7 < 103) {
				int j18 = intGroundArray[plane][j4][i7];
				int i19 = intGroundArray[plane][j4 + 1][i7];
				int l19 = intGroundArray[plane][j4 + 1][i7 + 1];
				int k20 = intGroundArray[plane][j4][i7 + 1];
				if (j16 == 0) {
					Object1 class10 = worldController.method296(plane, j4, i7);
					if (class10 != null) {
						int k21 = class10.uid >> 14 & 0x7fff;
			if (j12 == 2) {
				class10.aClass30_Sub2_Sub4_278 = new Animable_Sub5(k21, 4 + k14, 2, i19, l19, j18, k20, j17, false);
				class10.aClass30_Sub2_Sub4_279 = new Animable_Sub5(k21, k14 + 1 & 3, 2, i19, l19, j18, k20, j17, false);
			} else {
				class10.aClass30_Sub2_Sub4_278 = new Animable_Sub5(k21, k14, j12, i19, l19, j18, k20, j17, false);
			}
					}
				}
				if (j16 == 1) {
					Object2 class26 = worldController.method297(j4, i7, plane);
					if (class26 != null)
						class26.aClass30_Sub2_Sub4_504 = new Animable_Sub5(class26.uid >> 14 & 0x7fff, 0, 4, i19, l19, j18, k20, j17, false);
				}
				if (j16 == 2) {
					Object5 class28 = worldController.method298(j4, i7, plane);
					if (j12 == 11)
						j12 = 10;
					if (class28 != null)
						class28.aClass30_Sub2_Sub4_521 = new Animable_Sub5(class28.uid >> 14 & 0x7fff, k14, j12, i19, l19, j18, k20, j17, false);
				}
				if (j16 == 3) {
					Object3 class49 = worldController.method299(i7, j4, plane);
					if (class49 != null)
						class49.aClass30_Sub2_Sub4_814 = new Animable_Sub5(class49.uid >> 14 & 0x7fff, k14, 22, i19, l19, j18, k20, j17, false);
				}
			}
			return;
		}
		if (j == 147) {
			int l1 = stream.method428();
			int k4 = anInt1268 + (l1 >> 4 & 7);
			int j7 = anInt1269 + (l1 & 7);
			int i10 = stream.readUnsignedWord();
			byte byte0 = stream.method430();
			int l14 = stream.method434();
			byte byte1 = stream.method429();
			int k17 = stream.readUnsignedWord();
			int k18 = stream.method428();
			int j19 = k18 >> 2;
			int i20 = k18 & 3;
			int l20 = anIntArray1177[j19];
			byte byte2 = stream.readSignedByte();
			int l21 = stream.readUnsignedWord();
			byte byte3 = stream.method429();
			Player player;
			if (i10 == unknownInt10)
				player = myPlayer;
			else
				player = playerArray[i10];
			if (player != null) {
				ObjectDef class46 = ObjectDef.forID(l21);
				int i22 = intGroundArray[plane][k4][j7];
				int j22 = intGroundArray[plane][k4 + 1][j7];
				int k22 = intGroundArray[plane][k4 + 1][j7 + 1];
				int l22 = intGroundArray[plane][k4][j7 + 1];
				Model model = class46.method578(j19, i20, i22, j22, k22, l22, -1);
				if (model != null) {
					method130(k17 + 1, -1, 0, l20, j7, 0, plane, k4, l14 + 1);
					player.anInt1707 = l14 + loopCycle;
					player.anInt1708 = k17 + loopCycle;
					player.aModel_1714 = model;
					int i23 = class46.anInt744;
					int j23 = class46.anInt761;
					if (i20 == 1 || i20 == 3) {
						i23 = class46.anInt761;
						j23 = class46.anInt744;
					}
					player.anInt1711 = k4 * 128 + i23 * 64;
					player.anInt1713 = j7 * 128 + j23 * 64;
					player.anInt1712 = method42(plane, player.anInt1713, player.anInt1711);
					if (byte2 > byte0) {
						byte byte4 = byte2;
						byte2 = byte0;
						byte0 = byte4;
					}
					if (byte3 > byte1) {
						byte byte5 = byte3;
						byte3 = byte1;
						byte1 = byte5;
					}
					player.anInt1719 = k4 + byte2;
					player.anInt1721 = k4 + byte0;
					player.anInt1720 = j7 + byte3;
					player.anInt1722 = j7 + byte1;
				}
			}
		}
		if (j == 151) {
			int i2 = stream.method426();
			int l4 = anInt1268 + (i2 >> 4 & 7);
			int k7 = anInt1269 + (i2 & 7);
			int j10 = stream.method434();
			int k12 = stream.method428();
			int i15 = k12 >> 2;
			int k16 = k12 & 3;
			int l17 = anIntArray1177[i15];
			if (l4 >= 0 && k7 >= 0 && l4 < 104 && k7 < 104)
				method130(-1, j10, k16, l17, k7, i15, plane, l4, 0);
			return;
		}
		if (j == 4) {
			int j2 = stream.readUnsignedByte();
			int i5 = anInt1268 + (j2 >> 4 & 7);
			int l7 = anInt1269 + (j2 & 7);
			int k10 = stream.readUnsignedWord();
			int l12 = stream.readUnsignedByte();
			int j15 = stream.readUnsignedWord();
			if (i5 >= 0 && l7 >= 0 && i5 < 104 && l7 < 104) {
				i5 = i5 * 128 + 64;
				l7 = l7 * 128 + 64;
				Animable_Sub3 class30_sub2_sub4_sub3 = new Animable_Sub3(plane, loopCycle, j15, k10, method42(plane, l7, i5) - l12, l7, i5);
				aClass19_1056.insertHead(class30_sub2_sub4_sub3);
			}
			return;
		}
		if (j == 44) {
			int k2 = stream.method436();
			int j5 = stream.readUnsignedWord();
			int i8 = stream.readUnsignedByte();
			int l10 = anInt1268 + (i8 >> 4 & 7);
			int i13 = anInt1269 + (i8 & 7);
			if (l10 >= 0 && i13 >= 0 && l10 < 104 && i13 < 104) {
				Item class30_sub2_sub4_sub2_1 = new Item();
				class30_sub2_sub4_sub2_1.ID = k2;
				class30_sub2_sub4_sub2_1.anInt1559 = j5;
				if (groundArray[plane][l10][i13] == null)
					groundArray[plane][l10][i13] = new NodeList();
				groundArray[plane][l10][i13].insertHead(class30_sub2_sub4_sub2_1);
				spawnGroundItem(l10, i13);
			}
			return;
		}
		if (j == 101) {
			int l2 = stream.method427();
			int k5 = l2 >> 2;
			int j8 = l2 & 3;
			int i11 = anIntArray1177[k5];
			int j13 = stream.readUnsignedByte();
			int k15 = anInt1268 + (j13 >> 4 & 7);
			int l16 = anInt1269 + (j13 & 7);
			if (k15 >= 0 && l16 >= 0 && k15 < 104 && l16 < 104)
				method130(-1, -1, j8, i11, l16, k5, plane, k15, 0);
			return;
		}
		if (j == 117) {
			int i3 = stream.readUnsignedByte();
			int l5 = anInt1268 + (i3 >> 4 & 7);
			int k8 = anInt1269 + (i3 & 7);
			int j11 = l5 + stream.readSignedByte();
			int k13 = k8 + stream.readSignedByte();
			int l15 = stream.readSignedWord();
			int i17 = stream.readUnsignedWord();
			int i18 = stream.readUnsignedByte() * 4;
			int l18 = stream.readUnsignedByte() * 4;
			int k19 = stream.readUnsignedWord();
			int j20 = stream.readUnsignedWord();
			int i21 = stream.readUnsignedByte();
			int j21 = stream.readUnsignedByte();
			if (l5 >= 0 && k8 >= 0 && l5 < 104 && k8 < 104 && j11 >= 0 && k13 >= 0 && j11 < 104 && k13 < 104 && i17 != 65535) {
				l5 = l5 * 128 + 64;
				k8 = k8 * 128 + 64;
				j11 = j11 * 128 + 64;
				k13 = k13 * 128 + 64;
				Animable_Sub4 class30_sub2_sub4_sub4 = new Animable_Sub4(i21, l18, k19 + loopCycle, j20 + loopCycle, j21, plane, method42(plane, k8, l5) - i18, k8, l5, l15, i17);
				class30_sub2_sub4_sub4.method455(k19 + loopCycle, k13, method42(plane, k13, j11) - l18, j11);
				aClass19_1013.insertHead(class30_sub2_sub4_sub4);
			}
		}
	}

	private void method139(Stream stream) {
		stream.initBitAccess();
		int k = stream.readBits(8);
		if (k < npcCount) {
			for (int l = k; l < npcCount; l++)
				anIntArray840[anInt839++] = npcIndices[l];

		}
		if (k > npcCount) {
			Signlink.reporterror(myUsername + " Too many npcs");
			throw new RuntimeException("eek");
		}
		npcCount = 0;
		for (int i1 = 0; i1 < k; i1++) {
			int j1 = npcIndices[i1];
			NPC npc = npcArray[j1];
			int k1 = stream.readBits(1);
			if (k1 == 0) {
				npcIndices[npcCount++] = j1;
				npc.anInt1537 = loopCycle;
			} else {
				int l1 = stream.readBits(2);
				if (l1 == 0) {
					npcIndices[npcCount++] = j1;
					npc.anInt1537 = loopCycle;
					anIntArray894[anInt893++] = j1;
				} else if (l1 == 1) {
					npcIndices[npcCount++] = j1;
					npc.anInt1537 = loopCycle;
					int i2 = stream.readBits(3);
					npc.moveInDir(false, i2);
					int k2 = stream.readBits(1);
					if (k2 == 1)
						anIntArray894[anInt893++] = j1;
				} else if (l1 == 2) {
					npcIndices[npcCount++] = j1;
					npc.anInt1537 = loopCycle;
					int j2 = stream.readBits(3);
					npc.moveInDir(true, j2);
					int l2 = stream.readBits(3);
					npc.moveInDir(true, l2);
					int i3 = stream.readBits(1);
					if (i3 == 1)
						anIntArray894[anInt893++] = j1;
				} else if (l1 == 3)
					anIntArray840[anInt839++] = j1;
			}
		}

	}

	private void processLoginScreenInput()
	{
        int centerX = clientWidth / 2, centerY = clientHeight / 2;
        int messageWidth = 270;
        int messageHeight = 100;
        int x1 = centerX - (messageWidth / 2);
        int y1 = centerY - (messageHeight / 2);
        if (super.clickMode3 == 1 && clickInRegion(clientWidth - 52, 10, clientWidth - 10, 52)) { //login music box sprite
	    if(MusicEnabled) {
		   if (loginMusicEnabled) {
			   loginMusicEnabled = false;
		       stopMidi();
		   } else if (!loginMusicEnabled) {
			   loginMusicEnabled = true;
		       musicVolume = 200;
		       nextSong = 0;
		       try {
		           nextSong = Integer.parseInt(getParameter("music"));
		       } catch (Exception _ex) {}
		       onDemandFetcher.method558(2, nextSong);
		   }
			try {
				writeSettings();
			} catch(Exception e) {	}
	    }
        }
        if (loginMessage1.length() > 0 || loginMessage2.length() > 0) {
            if (loginMessage1.length() > 0) {
                aTextDrawingArea_1271.method382(0xffffff, centerX + 5, loginMessage1, centerY + 80, true);
                aTextDrawingArea_1271.method382(0xffffff, centerX + 5, loginMessage2, centerY + 80, true);
            } else
                aTextDrawingArea_1271.method382(0xffffff, centerX + 5, loginMessage2, centerY + 80, true);
        }
        if (loginScreenState == 2) {
        	//remember me
        	if (super.clickMode3 == 1 && clickInRegion(centerX - 108, centerY + 12, centerX - 90, centerY + 24))
        	{
        		if(rememberMe)
        		{
        			rememberMe = false;
        			try {
						savedUsername = "";
						savedPassword = "";
        				writeSettings();
        			} catch(Exception e) {	}
        		}else{
        			rememberMe = true;
        		}
        	}
            if (super.clickMode3 == 1 && clickInRegion(centerX - 100, centerY - 64, centerX + (cacheSprite[36].myWidth / 2), centerY - 38))
            	loginScreenCursorPos = 0;
            if (super.clickMode3 == 1 && clickInRegion(centerX - 100, centerY - 19, centerX + (cacheSprite[36].myWidth / 2), centerY + 7))
                loginScreenCursorPos = 1;
            if (super.clickMode3 == 1 && clickInRegion(centerX - 80, centerY + 38, centerX + (cacheSprite[37].myWidth / 2), centerY + 64)) {
                if (myUsername.length() > 0 && myPassword.length() > 0) {
                    loginFailures = 0;
                    login(myUsername, myPassword, false);
            		if(rememberMe)
            		{
	        			try {
							savedUsername = myUsername;
							savedPassword = myPassword;
	        				writeSettings();
	        			} catch(Exception e) {	}
            		}
            		stopMidi();
                    if (loggedIn) {
                        return;
                    }
                } else {
                    loginMessage1 = "Please enter a valid login.";
                }
            }
            do {
                int l1 = readChar(-796);
                if (l1 == -1)
                    break;
                boolean flag1 = false;
                for (int i2 = 0; i2 < validUserPassChars.length(); i2++) {
                    if (l1 != validUserPassChars.charAt(i2))
                        continue;
                    flag1 = true;
                    break;
                }
                if (loginScreenCursorPos == 0) {
                    if (l1 == 8 && myUsername.length() > 0)
                        myUsername = myUsername.substring(0, myUsername.length() - 1);
                    if (l1 == 9 || l1 == 10 || l1 == 13)
                        loginScreenCursorPos = 1;
                    if (flag1)
                        myUsername += (char) l1;
                    if (myUsername.length() > 12)
                        myUsername = myUsername.substring(0, 12);
                } else if (loginScreenCursorPos == 1) {
                    if (l1 == 8 && myPassword.length() > 0)
                        myPassword = myPassword.substring(0, myPassword.length() - 1);
                    if (l1 == 9 || l1 == 10 || l1 == 13)
                        loginScreenCursorPos = 0;
                    if (flag1)
                        myPassword += (char) l1;
                    if (myPassword.length() > 20)
                        myPassword = myPassword.substring(0, 20);
                } else if (loginScreenCursorPos == 2) {
                	aTextDrawingArea_1271.method382(0xffffff, centerX - 308, loginMessage1, centerY - 47, true);
                	aTextDrawingArea_1271.method382(0xffffff, centerX + 5, loginMessage1, centerY - 180, true);
                    if (l1 == 8 && server.length() > 0)
                        server = server.substring(0, server.length() - 1);
                    if (l1 == 9 || l1 == 10 || l1 == 13)
                        loginScreenCursorPos = 0;
                    if (flag1)
                        server += (char) l1;
                    if (myPassword.length() > 20)
                        server = server.substring(0, 20);
                }
                aTextDrawingArea_1271.method382(0xffffff, centerX - 308, loginMessage1, centerY - 47, true);
            	aTextDrawingArea_1271.method382(0xffffff, centerX + 5, loginMessage1, centerY - 180, true);

            	
            } while (true);
            return;
        }
	}
	
	private void method142(int i, int j, int k, int l, int i1, int j1, int k1) {
		if (i1 >= 1 && i >= 1 && i1 <= 102 && i <= 102) {
			if (lowMem && j != plane)
				return;
			int i2 = 0;
			if (j1 == 0)
				i2 = worldController.method300(j, i1, i);
			if (j1 == 1)
				i2 = worldController.method301(j, i1, i);
			if (j1 == 2)
				i2 = worldController.method302(j, i1, i);
			if (j1 == 3)
				i2 = worldController.method303(j, i1, i);
			if (i2 != 0) {
				int i3 = worldController.method304(j, i1, i, i2);
				int j2 = i2 >> 14 & 0x7fff;
		int k2 = i3 & 0x1f;
		int l2 = i3 >> 6;
		if (j1 == 0) {
			worldController.method291(i1, j, i, (byte) -119);
			ObjectDef class46 = ObjectDef.forID(j2);
			if (class46.aBoolean767)
				aClass11Array1230[j].method215(l2, k2, class46.aBoolean757, i1, i);
		}
		if (j1 == 1)
			worldController.method292(i, j, i1);
		if (j1 == 2) {
			worldController.method293(j, i1, i);
			ObjectDef class46_1 = ObjectDef.forID(j2);
			if (i1 + class46_1.anInt744 > 103 || i + class46_1.anInt744 > 103 || i1 + class46_1.anInt761 > 103 || i + class46_1.anInt761 > 103)
				return;
			if (class46_1.aBoolean767)
				aClass11Array1230[j].method216(l2, class46_1.anInt744, i1, i, class46_1.anInt761, class46_1.aBoolean757);
		}
		if (j1 == 3) {
			worldController.method294(j, i, i1);
			ObjectDef class46_2 = ObjectDef.forID(j2);
			if (class46_2.aBoolean767 && class46_2.hasActions)
				aClass11Array1230[j].method218(i, i1);
		}
			}
			if (k1 >= 0) {
				int j3 = j;
				if (j3 < 3 && (byteGroundArray[1][i1][i] & 2) == 2)
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
			Signlink.reporterror("Error packet size mismatch in getplayer pos:" + stream.currentOffset + " psize:" + i);
			throw new RuntimeException("eek");
		}
		for(int i1 = 0; i1 < playerCount; i1++)
			if(playerArray[playerIndices[i1]] == null)
			{
				Signlink.reporterror(myUsername + " null entry in pl list - pos:" + i1 + " size:" + playerCount);
				throw new RuntimeException("eek");
			}

	}
	
	private void setCameraPos(int j, int k, int l, int i1, int j1, int k1) {
		int l1 = 2048 - k & 0x7ff;
		int i2 = 2048 - j1 & 0x7ff;
		int j2 = 0;
		int k2 = 0;
		int l2 = j;
		if (l1 != 0) {
			int i3 = Model.modelIntArray1[l1];
			int k3 = Model.modelIntArray2[l1];
			int i4 = k2 * k3 - l2 * i3 >> 16;
		l2 = k2 * i3 + l2 * k3 >> 16;
				k2 = i4;
		}
		if (i2 != 0) {
			/*
			 * xxx if(cameratoggle){ if(zoom == 0) zoom = k2; if(lftrit == 0)
			 * lftrit = j2; if(fwdbwd == 0) fwdbwd = l2; k2 = zoom; j2 = lftrit;
			 * l2 = fwdbwd; }
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

	public void updateStrings(String str, int i) {
		switch (i) {
		case 1675:
			sendFrame126(str, 17508);
			break;// Stab
		case 1676:
			sendFrame126(str, 17509);
			break;// Slash
		case 1677:
			sendFrame126(str, 17510);
			break;// Cursh
		case 1678:
			sendFrame126(str, 17511);
			break;// Magic
		case 1679:
			sendFrame126(str, 17512);
			break;// Range
		case 1680:
			sendFrame126(str, 17513);
			break;// Stab
		case 1681:
			sendFrame126(str, 17514);
			break;// Slash
		case 1682:
			sendFrame126(str, 17515);
			break;// Crush
		case 1683:
			sendFrame126(str, 17516);
			break;// Magic
		case 1684:
			sendFrame126(str, 17517);
			break;// Range
		case 1686:
			sendFrame126(str, 17518);
			break;// Strength
		case 1687:
			sendFrame126(str, 17519);
			break;// Prayer
		}
	}

	public void sendFrame126(String str, int i) {
		RSInterface.interfaceCache[i].message = str;
		if (RSInterface.interfaceCache[i].parentID == tabInterfaceIDs[tabID]) {
			needDrawTabArea = true;
		}
	}

	public void sendPacket185(int button, int toggle, int type) {
		switch (type) {
		case 135:
			RSInterface class9 = RSInterface.interfaceCache[button];
			boolean flag8 = true;
			if (class9.contentType > 0)
				flag8 = promptUserForInput(class9);
			if (flag8) {
				stream.createFrame(185);
				stream.writeWord(button);
			}
			break;
		case 646:
			stream.createFrame(185);
			stream.writeWord(button);
			RSInterface class9_2 = RSInterface.interfaceCache[button];
			if (class9_2.valueIndexArray != null && class9_2.valueIndexArray[0][0] == 5) {
				if (variousSettings[toggle] != class9_2.anIntArray212[0]) {
					variousSettings[toggle] = class9_2.anIntArray212[0];
					method33(toggle);
					needDrawTabArea = true;
				}
			}
			break;
		case 169:
			stream.createFrame(185);
			stream.writeWord(button);
			RSInterface class9_3 = RSInterface.interfaceCache[button];
			if (class9_3.valueIndexArray != null && class9_3.valueIndexArray[0][0] == 5) {
				variousSettings[toggle] = 1 - variousSettings[toggle];
				method33(toggle);
				needDrawTabArea = true;
			}
			switch (button) {
			case 74214:
				System.out.println("toggle = " + toggle);
				if (toggle == 0)
					sendFrame36(173, toggle);
				if (toggle == 1)
					sendPacket185(153, 173, 646);
				break;
			}
			break;
		}
	}

	public void sendFrame36(int id, int state) {
		anIntArray1045[id] = state;
		if (variousSettings[id] != state) {
			variousSettings[id] = state;
			method33(id);
			needDrawTabArea = true;
			if (dialogID != -1)
				inputTaken = true;
		}
	}

	public void sendFrame219() {
		if (invOverlayInterfaceID != -1) {
			invOverlayInterfaceID = -1;
			tabAreaAltered = true;
			needDrawTabArea = true;
		}
		if (backDialogID != -1) {
			backDialogID = -1;
			inputTaken = true;
		}
		if (inputDialogState != 0) {
			inputDialogState = 0;
			inputTaken = true;
		}
		openInterfaceID = -1;
		aBoolean1149 = false;
	}

	public void sendFrame248(int interfaceID, int sideInterfaceID) {
		if (backDialogID != -1) {
			backDialogID = -1;
			inputTaken = true;
		}
		if (inputDialogState != 0) {
			inputDialogState = 0;
			inputTaken = true;
		}
		openInterfaceID = interfaceID;
		invOverlayInterfaceID = sideInterfaceID;
		needDrawTabArea = true;
		tabAreaAltered = true;
		aBoolean1149 = false;
	}

	private boolean parsePacket() {
		if (socketStream == null)
			return false;
		try {
			int i = socketStream.available();
			if (i == 0)
				return false;
			if (pktType == -1) {
				socketStream.flushInputStream(inStream.buffer, 1);
				pktType = inStream.buffer[0] & 0xff;
				if (encryption != null)
					pktType = pktType - encryption.getNextKey() & 0xff;
				pktSize = SizeConstants.packetSizes[pktType];
				i--;
			}
			if (pktSize == -1)
				if (i > 0) {
					socketStream.flushInputStream(inStream.buffer, 1);
					pktSize = inStream.buffer[0] & 0xff;
					i--;
				} else {
					return false;
				}
			if (pktSize == -2)
				if (i > 1) {
					socketStream.flushInputStream(inStream.buffer, 2);
					inStream.currentOffset = 0;
					pktSize = inStream.readUnsignedWord();
					i -= 2;
				} else {
					return false;
				}
			if (i < pktSize)
				return false;
			inStream.currentOffset = 0;
			socketStream.flushInputStream(inStream.buffer, pktSize);
			anInt1009 = 0;
			anInt843 = anInt842;
			anInt842 = anInt841;
			anInt841 = pktType;
			switch (pktType) {
			case 81:
				updatePlayers(pktSize, inStream);
				aBoolean1080 = false;
				pktType = -1;
				return true;

			case 176:
				daysSinceRecovChange = inStream.method427();
				unreadMessages = inStream.method435();
				membersInt = inStream.readUnsignedByte();
				anInt1193 = inStream.method440();
				daysSinceLastLogin = inStream.readUnsignedWord();
				if (anInt1193 != 0 && openInterfaceID == -1) {
					Signlink.dnslookup(TextClass.method586(anInt1193));
					clearTopInterfaces();
					char c = '\u028A';
					if (daysSinceRecovChange != 201 || membersInt == 1)
						c = '\u028F';
					reportAbuseInput = "";
					canMute = false;
					for (int k9 = 0; k9 < RSInterface.interfaceCache.length; k9++) {
						if (RSInterface.interfaceCache[k9] == null || RSInterface.interfaceCache[k9].contentType != c)
							continue;
						openInterfaceID = RSInterface.interfaceCache[k9].parentID;

					}
				}
				pktType = -1;
				return true;

			case 64:
				anInt1268 = inStream.method427();
				anInt1269 = inStream.method428();
				for (int j = anInt1268; j < anInt1268 + 8; j++) {
					for (int l9 = anInt1269; l9 < anInt1269 + 8; l9++)
						if (groundArray[plane][j][l9] != null) {
							groundArray[plane][j][l9] = null;
							spawnGroundItem(j, l9);
						}
				}
				for (Class30_Sub1 class30_sub1 = (Class30_Sub1) aClass19_1179.reverseGetFirst(); class30_sub1 != null; class30_sub1 = (Class30_Sub1) aClass19_1179.reverseGetNext())
					if (class30_sub1.anInt1297 >= anInt1268 && class30_sub1.anInt1297 < anInt1268 + 8 && class30_sub1.anInt1298 >= anInt1269 && class30_sub1.anInt1298 < anInt1269 + 8 && class30_sub1.anInt1295 == plane)
						class30_sub1.anInt1294 = 0;
				pktType = -1;
				return true;

			case 185:
				int k = inStream.method436();
				RSInterface.interfaceCache[k].anInt233 = 3;
				if (myPlayer.desc == null)
					RSInterface.interfaceCache[k].mediaID = (myPlayer.anIntArray1700[0] << 25) + (myPlayer.anIntArray1700[4] << 20) + (myPlayer.equipment[0] << 15) + (myPlayer.equipment[8] << 10) + (myPlayer.equipment[11] << 5) + myPlayer.equipment[1];
				else
					RSInterface.interfaceCache[k].mediaID = (int) (0x12345678L + myPlayer.desc.interfaceType);
				pktType = -1;
				return true;

				/* Clan chat packet */
	/*		case 217:
				try {
					name = inStream.readString();
					message = inStream.readString();
					clanname = inStream.readString();
					rights = inStream.readUnsignedWord();
					// message = TextInput.processText(message);
					// message = Censor.doCensor(message);
					System.out.println(clanname);
					pushMessage(message, 16, name);
				} catch (Exception e) {
					e.printStackTrace();
				}
				pktType = -1;
				return true;
				*/
			case 217:
				try {
					globalPrefix = inStream.readString();
					String name = inStream.readString();
					String message = inStream.readString();
					rights = inStream.readUnsignedByte();
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
				
			case 107:
				aBoolean1160 = false;
				for (int l = 0; l < 5; l++)
					aBooleanArray876[l] = false;
				pktType = -1;
				return true;

			case 72:
				int i1 = inStream.method434();
				RSInterface class9 = RSInterface.interfaceCache[i1];
				for (int k15 = 0; k15 < class9.inv.length; k15++) {
					class9.inv[k15] = -1;
					class9.inv[k15] = 0;
				}
				pktType = -1;
				return true;

			case 214:
				ignoreCount = pktSize / 8;
				for (int j1 = 0; j1 < ignoreCount; j1++) {
					ignoreListAsLongs[j1] = inStream.readQWord();
				}
				pktType = -1;
				return true;

			case 166:
				aBoolean1160 = true;
				anInt1098 = inStream.readUnsignedByte();
				anInt1099 = inStream.readUnsignedByte();
				anInt1100 = inStream.readUnsignedWord();
				anInt1101 = inStream.readUnsignedByte();
				anInt1102 = inStream.readUnsignedByte();
				if (anInt1102 >= 100) {
					xCameraPos = anInt1098 * 128 + 64;
					yCameraPos = anInt1099 * 128 + 64;
					zCameraPos = method42(plane, yCameraPos, xCameraPos) - anInt1100;
				}
				pktType = -1;
				return true;

			case 134:
				needDrawTabArea = true;
				int k1 = inStream.readUnsignedByte();
				int i10 = inStream.method439();
				int l15 = inStream.readUnsignedByte();
				currentExp[k1] = i10;
				currentStats[k1] = l15;
				maxStats[k1] = 1;
				for (int k20 = 0; k20 < 98; k20++)
					if (i10 >= anIntArray1019[k20])
						maxStats[k1] = k20 + 2;
				pktType = -1;
				return true;

			case 71:
				int l1 = inStream.readUnsignedWord();
				int j10 = inStream.method426();
				if (l1 == 65535)
					l1 = -1;
				tabInterfaceIDs[j10] = l1;
				needDrawTabArea = true;
				tabAreaAltered = true;
				pktType = -1;
				return true;

	/*		case 74:
				int i2 = inStream.method434();
				if (i2 == 65535)
					i2 = -1;
				if (i2 != currentSong && musicEnabled && !lowMem && previousSong == 0) {
					nextSong = i2;
					songChanging = true;
					onDemandFetcher.method558(2, nextSong);
				}
				currentSong = i2;
				pktType = -1;
				return true;
				*/
		/*	case 121:
				int j2 = inStream.method436();
				int k10 = inStream.method435();
				if (musicEnabled && !lowMem) {
					nextSong = j2;
					songChanging = false;
					onDemandFetcher.method558(2, nextSong);
					previousSong = k10;
				}
				pktType = -1;
				return true;*/
				
			case 74:
				int songID = inStream.method434();
				if (songID == 65535) {
					songID = -1;
				}
				if(MusicEnabled) {
				    if (songID != currentSong && musicEnabled && !lowMem && previousSong == 0) {
						nextSong = songID;
						songChanging = true;
						onDemandFetcher.method558(2, nextSong);
				    }
				}
				currentSong = songID;
				pktType = -1;
				return true;
				
			case 121:
				int songId = inStream.method436();
				int songDelay = inStream.method435();
				if(MusicEnabled) {
				    if (musicEnabled && !lowMem) {
						nextSong = songId;
						songChanging = false;
						onDemandFetcher.method558(2, nextSong);
						previousSong = songDelay;
				    }
				}
				pktType = -1;
				return true;

			case 109:
				resetLogout();
				pktType = -1;
				return false;

			case 70:
				int k2 = inStream.readSignedWord();
				int l10 = inStream.method437();
				int i16 = inStream.method434();
				RSInterface class9_5 = RSInterface.interfaceCache[i16];
				class9_5.anInt263 = k2;
				class9_5.anInt265 = l10;
				pktType = -1;
				return true;

			case 73:
			case 241:
				int l2 = anInt1069;
				int i11 = anInt1070;
				if(pktType == 73) {
					l2 = inStream.method435();
					i11 = inStream.readUnsignedWord();
					aBoolean1159 = false;
				}
				if(pktType == 241) {
					i11 = inStream.method435();
					inStream.initBitAccess();
					for(int j16 = 0; j16 < 4; j16++) {
						for(int l20 = 0; l20 < 13; l20++) {
							for(int j23 = 0; j23 < 13; j23++) {
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
				if(anInt1069 == l2 && anInt1070 == i11 && loadingStage == 2) {
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
				/*aTextDrawingArea_1271.drawText(0, "Loading - please wait.", 151, 257);
				aTextDrawingArea_1271.drawText(0xffffff, "Loading - please wait.", 150, 256);*/
				int loadXBox = clientSize == 0 ? 200 : (clientWidth / 2) - 55;
				int loadYBox = clientSize == 0 ? 150 : (clientHeight / 2) - 45;
				DrawingArea.fillPixels(loadXBox, 130, 22, 0xffffff, loadYBox);
				DrawingArea.drawPixels(20, loadYBox+1, loadXBox+1, 0, 128);
				aTextDrawingArea_1271.drawText(0, "Loading - please wait.", loadYBox+18, loadXBox+68);
				aTextDrawingArea_1271.drawText(0xffffff, "Loading - please wait.", loadYBox+17, loadXBox+67);
				aRSImageProducer_1165.drawGraphics(clientSize == 0 ? 4 : 0, super.graphics, clientSize == 0 ? 4 : 0);
				if(pktType == 73) {
					int k16 = 0;
					for(int i21 = (anInt1069 - 6) / 8; i21 <= (anInt1069 + 6) / 8; i21++) {
						for(int k23 = (anInt1070 - 6) / 8; k23 <= (anInt1070 + 6) / 8; k23++)
							k16++;
					}
					aByteArrayArray1183 = new byte[k16][];
					aByteArrayArray1247 = new byte[k16][];
					anIntArray1234 = new int[k16];
					anIntArray1235 = new int[k16];
					anIntArray1236 = new int[k16];
					k16 = 0;
					for(int l23 = (anInt1069 - 6) / 8; l23 <= (anInt1069 + 6) / 8; l23++) {
						for(int j26 = (anInt1070 - 6) / 8; j26 <= (anInt1070 + 6) / 8; j26++) {
							anIntArray1234[k16] = (l23 << 8) + j26;
							if(aBoolean1141 && (j26 == 49 || j26 == 149 || j26 == 147 || l23 == 50 || l23 == 49 && j26 == 47)) {
								anIntArray1235[k16] = -1;
								anIntArray1236[k16] = -1;
								k16++;
							} else {
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
				if(pktType == 241) {
					int l16 = 0;
					int ai[] = new int[676];
					for(int i24 = 0; i24 < 4; i24++) {
						for(int k26 = 0; k26 < 13; k26++) {
							for(int l28 = 0; l28 < 13; l28++) {
								int k30 = anIntArrayArrayArray1129[i24][k26][l28];
								if(k30 != -1) {
									int k31 = k30 >> 14 & 0x3ff;
							int i32 = k30 >> 3 & 0x7ff;
					int k32 = (k31 / 8 << 8) + i32 / 8;
					for(int j33 = 0; j33 < l16; j33++) {
						if(ai[j33] != k32)
							continue;
						k32 = -1;

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
					for(int l26 = 0; l26 < l16; l26++) {
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
				for(int j24 = 0; j24 < 16384; j24++) {
					NPC npc = npcArray[j24];
					if(npc != null) {
						for(int j29 = 0; j29 < 10; j29++) {
							npc.smallX[j29] -= i17;
							npc.smallY[j29] -= j21;
						}
						npc.x -= i17 * 128;
						npc.y -= j21 * 128;
					}
				}
				for(int i27 = 0; i27 < maxPlayers; i27++) {
					Player player = playerArray[i27];
					if(player != null) {
						for(int i31 = 0; i31 < 10; i31++) {
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
				if(i17 < 0) {
					byte1 = 103;
					byte2 = -1;
					byte3 = -1;
				}
				byte byte4 = 0;
				byte byte5 = 104;
				byte byte6 = 1;
				if(j21 < 0) {
					byte4 = 103;
					byte5 = -1;
					byte6 = -1;
				}
				for(int k33 = byte1; k33 != byte2; k33 += byte3) {
					for(int l33 = byte4; l33 != byte5; l33 += byte6) {
						int i34 = k33 + i17;
						int j34 = l33 + j21;
						for(int k34 = 0; k34 < 4; k34++)
							if(i34 >= 0 && j34 >= 0 && i34 < 104 && j34 < 104)
								groundArray[k34][k33][l33] = groundArray[k34][i34][j34];
							else
								groundArray[k34][k33][l33] = null;
					}
				}
				for(Class30_Sub1 class30_sub1_1 = (Class30_Sub1)aClass19_1179.reverseGetFirst(); class30_sub1_1 != null; class30_sub1_1 = (Class30_Sub1)aClass19_1179.reverseGetNext()) {
					class30_sub1_1.anInt1297 -= i17;
					class30_sub1_1.anInt1298 -= j21;
					if(class30_sub1_1.anInt1297 < 0 || class30_sub1_1.anInt1298 < 0 || class30_sub1_1.anInt1297 >= 104 || class30_sub1_1.anInt1298 >= 104)
						class30_sub1_1.unlink();
				}
				if(destX != 0) {
					destX -= i17;
					destY -= j21;
				}
				aBoolean1160 = false;
				pktType = -1;
				return true;

			case 208:
				int i3 = inStream.method437();
				if (i3 >= 0)
				{
					method60(i3);
					walkableInterfaceMode = true;
				}
				else
					walkableInterfaceMode = false;
				anInt1018 = i3;
				pktType = -1;
				return true;

			case 99:
				anInt1021 = inStream.readUnsignedByte();
				pktType = -1;
				return true;

			case 75:
				int j3 = inStream.method436();
				int j11 = inStream.method436();
				RSInterface.interfaceCache[j11].anInt233 = 2;
				RSInterface.interfaceCache[j11].mediaID = j3;
				pktType = -1;
				return true;

			case 114:
				anInt1104 = inStream.method434() * 30;
				pktType = -1;
				return true;

			case 60:
				anInt1269 = inStream.readUnsignedByte();
				anInt1268 = inStream.method427();
				while (inStream.currentOffset < pktSize) {
					int k3 = inStream.readUnsignedByte();
					method137(inStream, k3);
				}
				pktType = -1;
				return true;

			case 35:
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

			case 174:
				int i4 = inStream.readUnsignedWord();
				int l11 = inStream.readUnsignedByte();
				int k17 = inStream.readUnsignedWord();
				if (aBoolean848 && !lowMem && currentSound < 50) {
					sound[currentSound] = i4;
					soundType[currentSound] = l11;
					soundDelay[currentSound] = k17 + Sounds.anIntArray326[i4];
					currentSound++;
				}
				pktType = -1;
				return true;

			case 104:
				int j4 = inStream.method427();
				int i12 = inStream.method426();
				String s6 = inStream.readString();
				if (j4 >= 1 && j4 <= 5) {
					if (s6.equalsIgnoreCase("null"))
						s6 = null;
					atPlayerActions[j4 - 1] = s6;
					atPlayerArray[j4 - 1] = i12 == 0;
				}
				pktType = -1;
				return true;
				
			case 78:
				destX = 0;
				pktType = -1;
				return true;

			case 253:
				String s = inStream.readString();
				if (s.endsWith(":tradereq:")) {
					String s3 = s.substring(0, s.indexOf(":"));
					long l17 = TextClass.longForName(s3);
					boolean flag2 = false;
					for (int j27 = 0; j27 < ignoreCount; j27++) {
						if (ignoreListAsLongs[j27] != l17)
							continue;
						flag2 = true;

					}
					if (!flag2 && anInt1251 == 0)
						pushMessage("wishes to trade with you.", 4, s3);
				}else if (s.endsWith(":clan:")) {
					String s4 = s.substring(0, s.indexOf(":"));
					TextClass.longForName(s4);
					pushMessage("Clan: ", 8, s4);
				} else if (s.endsWith("#url#")) {
					String link = s.substring(0, s.indexOf("#"));
					pushMessage("Join us at: ", 9, link);
				} else if (s.endsWith(":duelreq:")) {
					String s4 = s.substring(0, s.indexOf(":"));
					long l18 = TextClass.longForName(s4);
					boolean flag3 = false;
					for (int k27 = 0; k27 < ignoreCount; k27++) {
						if (ignoreListAsLongs[k27] != l18)
							continue;
						flag3 = true;

					}
					if (!flag3 && anInt1251 == 0)
						pushMessage("wishes to duel with you.", 8, s4);
				} else if (s.endsWith(":chalreq:")) {
					String s5 = s.substring(0, s.indexOf(":"));
					long l19 = TextClass.longForName(s5);
					boolean flag4 = false;
					for (int l27 = 0; l27 < ignoreCount; l27++) {
						if (ignoreListAsLongs[l27] != l19)
							continue;
						flag4 = true;

					}
					if (!flag4 && anInt1251 == 0) {
						String s8 = s.substring(s.indexOf(":") + 1, s.length() - 9);
						pushMessage(s8, 8, s5);
					}
				} else if (s.endsWith(":resetautocast:")) {
					autocast = false;
					autoCastId = 0;
				} else {
					pushMessage(s, 0, "");
				}
				pktType = -1;
				return true;

			case 1:
				for (int k4 = 0; k4 < playerArray.length; k4++)
					if (playerArray[k4] != null)
						playerArray[k4].anim = -1;
				for (int j12 = 0; j12 < npcArray.length; j12++)
					if (npcArray[j12] != null)
						npcArray[j12].anim = -1;
				pktType = -1;
				return true;

			case 50:
				long l4 = inStream.readQWord();
				int i18 = inStream.readUnsignedByte();
				String s7 = TextClass.fixName(TextClass.nameForLong(l4));
				for (int k24 = 0; k24 < friendsCount; k24++) {
					if (l4 != friendsListAsLongs[k24])
						continue;
					if (friendsNodeIDs[k24] != i18) {
						friendsNodeIDs[k24] = i18;
						needDrawTabArea = true;
						if (i18 >= 2) {
							pushMessage(s7 + " has logged in.", 5, "");
						}
						if (i18 <= 1) {
							pushMessage(s7 + " has logged out.", 5, "");
						}
					}
					s7 = null;

				}
				if (s7 != null && friendsCount < 200) {
					friendsListAsLongs[friendsCount] = l4;
					friendsList[friendsCount] = s7;
					friendsNodeIDs[friendsCount] = i18;
					friendsCount++;
					needDrawTabArea = true;
				}
				for (boolean flag6 = false; !flag6;) {
					flag6 = true;
					for (int k29 = 0; k29 < friendsCount - 1; k29++)
						if (friendsNodeIDs[k29] != nodeID && friendsNodeIDs[k29 + 1] == nodeID || friendsNodeIDs[k29] == 0 && friendsNodeIDs[k29 + 1] != 0) {
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

			case 110:
				if (tabID == 12) {
					needDrawTabArea = true;
				}
				energy = inStream.readUnsignedByte();
				pktType = -1;
				return true;

			case 254:
				anInt855 = inStream.readUnsignedByte();
				if (anInt855 == 1)
					anInt1222 = inStream.readUnsignedWord();
				if (anInt855 >= 2 && anInt855 <= 6) {
					if (anInt855 == 2) {
						anInt937 = 64;
						anInt938 = 64;
					}
					if (anInt855 == 3) {
						anInt937 = 0;
						anInt938 = 64;
					}
					if (anInt855 == 4) {
						anInt937 = 128;
						anInt938 = 64;
					}
					if (anInt855 == 5) {
						anInt937 = 64;
						anInt938 = 0;
					}
					if (anInt855 == 6) {
						anInt937 = 64;
						anInt938 = 128;
					}
					anInt855 = 2;
					anInt934 = inStream.readUnsignedWord();
					anInt935 = inStream.readUnsignedWord();
					anInt936 = inStream.readUnsignedByte();
				}
				if (anInt855 == 10)
					anInt933 = inStream.readUnsignedWord();
				pktType = -1;
				return true;

			case 248:
				int i5 = inStream.method435();
				int k12 = inStream.readUnsignedWord();
				if (backDialogID != -1) {
					backDialogID = -1;
					inputTaken = true;
				}
				if (inputDialogState != 0) {
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

			case 79:
				int j5 = inStream.method434();
				int l12 = inStream.method435();
				RSInterface class9_3 = RSInterface.interfaceCache[j5];
				if (class9_3 != null && class9_3.type == 0) {
					if (l12 < 0)
						l12 = 0;
					if (l12 > class9_3.scrollMax - class9_3.height)
						l12 = class9_3.scrollMax - class9_3.height;
					class9_3.scrollPosition = l12;
				}
				pktType = -1;
				return true;

			case 68:
				for (int k5 = 0; k5 < variousSettings.length; k5++)
					if (variousSettings[k5] != anIntArray1045[k5]) {
						variousSettings[k5] = anIntArray1045[k5];
						method33(k5);
						needDrawTabArea = true;
					}
				pktType = -1;
				return true;

			case 196:
				long l5 = inStream.readQWord();
				int j18 = inStream.readDWord();
				int l21 = inStream.readUnsignedByte();
				boolean flag5 = false;
				/*for (int i28 = 0; i28 < 100; i28++) {
					if (anIntArray1240[i28] != j18)
						continue;
					flag5 = true;
				}*/
				if(l21 <= 1) {
					for(int l29 = 0; l29 < ignoreCount; l29++) {
						if(ignoreListAsLongs[l29] != l5)
							continue;
						flag5 = true;
						
					}
				}
				if(!flag5 && anInt1251 == 0){
					try {
						// anIntArray1240[anInt1169] = j18;
						// anInt1169 = (anInt1169 + 1) % 100;
						// if(l21 != 3)
						// s9 = Censor.doCensor(s9);
						String s9 = TextInput.method525(pktSize - 13, inStream);
						if(l21 == 2 || l21 == 3)
							pushMessage(s9, 7, "@cr2@" + TextClass.fixName(TextClass.nameForLong(l5)));
						else
						if(l21 == 1)
							pushMessage(s9, 7, "@cr1@" + TextClass.fixName(TextClass.nameForLong(l5)));
						else
							pushMessage(s9, 3, TextClass.fixName(TextClass.nameForLong(l5)));
					} catch(Exception exception1) {
						Signlink.reporterror("cde1");
					}
				}
				pktType = -1;
				return true;

			case 85:
				anInt1269 = inStream.method427();
				anInt1268 = inStream.method427();
				pktType = -1;
				return true;

			case 24:
				anInt1054 = inStream.method428();
				if (anInt1054 == tabID) {
					if (anInt1054 == 3)
						tabID = 1;
					else
						tabID = 3;
					needDrawTabArea = true;
				}
				pktType = -1;
				return true;

			case 246:
				int i6 = inStream.method434();
				int i13 = inStream.readUnsignedWord();
				int k18 = inStream.readUnsignedWord();
				if (k18 == 65535) {
					RSInterface.interfaceCache[i6].anInt233 = 0;
					pktType = -1;
					return true;
				} else {
					ItemDef itemDef = ItemDef.forID(k18);
					RSInterface.interfaceCache[i6].anInt233 = 4;
					RSInterface.interfaceCache[i6].mediaID = k18;
					RSInterface.interfaceCache[i6].modelRotation1 = itemDef.modelRotation1;
					RSInterface.interfaceCache[i6].modelRotation2 = itemDef.modelRotation2;
					RSInterface.interfaceCache[i6].modelZoom = (itemDef.modelZoom * 100) / i13;
					pktType = -1;
					return true;
				}

			case 171:
				boolean flag1 = inStream.readUnsignedByte() == 1;
				int j13 = inStream.readUnsignedWord();
				RSInterface.interfaceCache[j13].isMouseoverTriggered = flag1;
				pktType = -1;
				return true;

			case 142:
				int j6 = inStream.method434();
				method60(j6);
				if (backDialogID != -1) {
					backDialogID = -1;
					inputTaken = true;
				}
				if (inputDialogState != 0) {
					inputDialogState = 0;
					inputTaken = true;
				}
				invOverlayInterfaceID = j6;
				tabAreaAltered = true;
				needDrawTabArea = true;
				openInterfaceID = -1;
				aBoolean1149 = false;
				pktType = -1;
				return true;

			case 126:
				try {
					String text = inStream.readString();
					int frame = inStream.method435();
					if (text.startsWith("www.")) {
						launchURL(text);
					}
					//updateStrings(text, frame);
					sendFrame126(text, frame);
					if (frame >= 18144 && frame <= 18244) {
						clanList[frame - 18144] = text;
					}
				} catch (Exception e) {
				}
				pktType = -1;
				return true;

			case 206:
				publicChatMode = inStream.readUnsignedByte();
				privateChatMode = inStream.readUnsignedByte();
				tradeMode = inStream.readUnsignedByte();
				inputTaken = true;
				pktType = -1;
				return true;

			case 240:
				if (tabID == 12) {
					needDrawTabArea = true;
				}
				weight = inStream.readSignedWord();
				pktType = -1;
				return true;

			case 8:
				int k6 = inStream.method436();
				int l13 = inStream.readUnsignedWord();
				RSInterface.interfaceCache[k6].anInt233 = 1;
				RSInterface.interfaceCache[k6].mediaID = l13;
				pktType = -1;
				return true;

			case 122:
				int l6 = inStream.method436();
				int i14 = inStream.method436();
				int i19 = i14 >> 10 & 0x1f;
					int i22 = i14 >> 5 & 0x1f;
				int l24 = i14 & 0x1f;
				RSInterface.interfaceCache[l6].textColor = (i19 << 19) + (i22 << 11) + (l24 << 3);
				pktType = -1;
				return true;

				case 53:
					needDrawTabArea = true;
					int i7 = inStream.readUnsignedWord();
					RSInterface class9_1 = RSInterface.interfaceCache[i7];
					int j19 = inStream.readUnsignedWord();
					for (int j22 = 0; j22 < j19; j22++) {
						int i25 = inStream.readUnsignedByte();
						if (i25 == 255)
							i25 = inStream.method440();
						class9_1.inv[j22] = inStream.method436();
						class9_1.invStackSizes[j22] = i25;
					}
					for (int j25 = j19; j25 < class9_1.inv.length; j25++) {
						class9_1.inv[j25] = 0;
						class9_1.invStackSizes[j25] = 0;
					}
					pktType = -1;
					return true;

				case 230:
					int j7 = inStream.method435();
					int j14 = inStream.readUnsignedWord();
					int k19 = inStream.readUnsignedWord();
					int k22 = inStream.method436();
					RSInterface.interfaceCache[j14].modelRotation1 = k19;
					RSInterface.interfaceCache[j14].modelRotation2 = k22;
					RSInterface.interfaceCache[j14].modelZoom = j7;
					pktType = -1;
					return true;

				case 221:
					anInt900 = inStream.readUnsignedByte();
					needDrawTabArea = true;
					pktType = -1;
					return true;

				case 177:
					aBoolean1160 = true;
					anInt995 = inStream.readUnsignedByte();
					anInt996 = inStream.readUnsignedByte();
					anInt997 = inStream.readUnsignedWord();
					anInt998 = inStream.readUnsignedByte();
					anInt999 = inStream.readUnsignedByte();
					if (anInt999 >= 100) {
						int k7 = anInt995 * 128 + 64;
						int k14 = anInt996 * 128 + 64;
						int i20 = method42(plane, k14, k7) - anInt997;
						int l22 = k7 - xCameraPos;
						int k25 = i20 - zCameraPos;
						int j28 = k14 - yCameraPos;
						int i30 = (int) Math.sqrt(l22 * l22 + j28 * j28);
						yCameraCurve = (int) (Math.atan2(k25, i30) * 325.94900000000001D) & 0x7ff;
						xCameraCurve = (int) (Math.atan2(l22, j28) * -325.94900000000001D) & 0x7ff;
						if (yCameraCurve < 128)
							yCameraCurve = 128;
						if (yCameraCurve > 383)
							yCameraCurve = 383;
					}
					pktType = -1;
					return true;

				case 249:
					anInt1046 = inStream.method426();
					unknownInt10 = inStream.method436();
					pktType = -1;
					return true;

				case 65:
					updateNPCs(inStream, pktSize);
					pktType = -1;
					return true;

				case 27:
					messagePromptRaised = false;
					inputDialogState = 1;
					amountOrNameInput = "";
					inputTaken = true;
					pktType = -1;
					return true;

				case 187:
					messagePromptRaised = false;
					inputDialogState = 2;
					amountOrNameInput = "";
					inputTaken = true;
					pktType = -1;
					return true;

				case 97:
					int l7 = inStream.readUnsignedWord();
					method60(l7);
					if (invOverlayInterfaceID != -1) {
						needDrawTabArea = true;
						invOverlayInterfaceID = -1;
						tabAreaAltered = true;
					}
					if (backDialogID != -1) {
						backDialogID = -1;
						inputTaken = true;
					}
					if (inputDialogState != 0) {
						inputDialogState = 0;
						inputTaken = true;
					}
					openInterfaceID = l7;
					aBoolean1149 = false;
					resetFade();
					pktType = -1;
					return true;

				case 218:
					int i8 = inStream.method438();
					dialogID = i8;
					inputTaken = true;
					pktType = -1;
					return true;

				case 87:
					int j8 = inStream.method434();
					int l14 = inStream.method439();
					anIntArray1045[j8] = l14;
					if (variousSettings[j8] != l14) {
						variousSettings[j8] = l14;
						method33(j8);
						needDrawTabArea = true;
						if (dialogID != -1)
							inputTaken = true;
					}
					pktType = -1;
					return true;

				case 36:
					int k8 = inStream.method434();
					byte byte0 = inStream.readSignedByte();
					anIntArray1045[k8] = byte0;
					if (variousSettings[k8] != byte0) {
						variousSettings[k8] = byte0;
						method33(k8);
						needDrawTabArea = true;
						if (dialogID != -1)
							inputTaken = true;
					}
					pktType = -1;
					return true;

				case 61:
					anInt1055 = inStream.readUnsignedByte();
					pktType = -1;
					return true;

				case 200:
					int l8 = inStream.readUnsignedWord();
					int i15 = inStream.readSignedWord();
					RSInterface class9_4 = RSInterface.interfaceCache[l8];
					class9_4.anInt257 = i15;
					if(i15 == -1) {
						class9_4.anInt246 = 0;
						class9_4.anInt208 = 0;
					}
					pktType = -1;
					return true;
				case 219:
					if (invOverlayInterfaceID != -1) {
						invOverlayInterfaceID = -1;
						needDrawTabArea = true;
						tabAreaAltered = true;
					}
					if (backDialogID != -1) {
						backDialogID = -1;
						inputTaken = true;
					}
					if (inputDialogState != 0) {
						inputDialogState = 0;
						inputTaken = true;
					}
					openInterfaceID = -1;
					aBoolean1149 = false;
					resetFade();
					pktType = -1;
					return true;

				case 34:
					needDrawTabArea = true;
					int i9 = inStream.readUnsignedWord();
					RSInterface class9_2 = RSInterface.interfaceCache[i9];
					while (inStream.currentOffset < pktSize) {
						int j20 = inStream.method422();
						int i23 = inStream.readUnsignedWord();
						int l25 = inStream.readUnsignedByte();
						if (l25 == 255)
							l25 = inStream.readDWord();
						if (j20 >= 0 && j20 < class9_2.inv.length) {
							class9_2.inv[j20] = i23;
							class9_2.invStackSizes[j20] = l25;
						}
					}
					pktType = -1;
					return true;

				case 4:
				case 44:
				case 84:
				case 101:
				case 105:
				case 117:
				case 147:
				case 151:
				case 156:
				case 160:
				case 215:
					method137(inStream, pktType);
					pktType = -1;
					return true;

				case 106:
					tabID = inStream.method427();
					needDrawTabArea = true;
					tabAreaAltered = true;
					pktType = -1;
					return true;

				case 164:
					int j9 = inStream.method434();
					method60(j9);
					if (invOverlayInterfaceID != -1) {
						needDrawTabArea = true;
						invOverlayInterfaceID = -1;
						tabAreaAltered = true;
					}
					backDialogID = j9;
					inputTaken = true;
					openInterfaceID = -1;
					aBoolean1149 = false;
					pktType = -1;
					return true;

			}
			Signlink.reporterror("T1 - " + pktType + "," + pktSize + " - " + anInt842 + "," + anInt843);
		} catch (IOException _ex) {
			dropClient();
		} catch (Exception exception) {
			String s2 = "T2 - " + pktType + "," + anInt842 + "," + anInt843 + " - " + pktSize + "," + (baseX + myPlayer.smallX[0]) + "," + (baseY + myPlayer.smallY[0]) + " - ";
			for (int j15 = 0; j15 < pktSize && j15 < 50; j15++)
				s2 = s2 + inStream.buffer[j15] + ",";
			Signlink.reporterror(s2);
		}
		pktType = -1;
		return true;
	}

	public static int log_view_dist = 9;
	
	private void method146() {
		anInt1265++;
		method47(true);
		method26(true);
		method47(false);
		method26(false);
		method55();
		method104();
		if(!aBoolean1160) {
			int i = anInt1184;
			if(anInt984 / 256 > i)
				i = anInt984 / 256;
			if(aBooleanArray876[4] && anIntArray1203[4] + 128 > i)
				i = anIntArray1203[4] + 128;
			int k = minimapInt1 + anInt896 & 0x7ff;
			setCameraPos(cameraPos2 + i * (clientSize == 0 ? cameraPos1 : 5), i, anInt1014, method42(plane, myPlayer.y, myPlayer.x) - 50, k, anInt1015);
		}
		int j;
		if(!aBoolean1160)
			j = method120();
		else
			j = method121();
		int l = xCameraPos;
		int i1 = zCameraPos;
		int j1 = yCameraPos;
		int k1 = yCameraCurve;
		int l1 = xCameraCurve;
		for(int i2 = 0; i2 < 5; i2++)
			if(aBooleanArray876[i2]) {
				int j2 = (int)((Math.random() * (double)(anIntArray873[i2] * 2 + 1) - (double)anIntArray873[i2]) + Math.sin((double)anIntArray1030[i2] * ((double)anIntArray928[i2] / 100D)) * (double)anIntArray1203[i2]);
				if(i2 == 0)
					xCameraPos += j2;
				if(i2 == 1)
					zCameraPos += j2;
				if(i2 == 2)
					yCameraPos += j2;
				if(i2 == 3)
					xCameraCurve = xCameraCurve + j2 & 0x7ff;
				if(i2 == 4) {
					yCameraCurve += j2;
					if(yCameraCurve < 128)
						yCameraCurve = 128;
					if(yCameraCurve > 383)
						yCameraCurve = 383;
				}
			}
		int k2 = Texture.anInt1481;
		Model.aBoolean1684 = true;
		Model.anInt1687 = 0;
		Model.anInt1685 = super.mouseX - 4;
		Model.anInt1686 = super.mouseY - 4;
		DrawingArea.setAllPixelsToZero();
	//	DrawingArea.method336(clientHeight, 0, 0, 0xC8C0A8, clientWidth);
		worldController.method313(xCameraPos, yCameraPos, xCameraCurve, zCameraPos, j, yCameraCurve);
		worldController.clearObj5Cache();
	//	handleLighting();
		updateEntities();
		drawHeadIcon();
		method37(k2);
		if (loggedIn) {
			drawUnfixedGame();
			draw3dScreen();
		}
		if (loggedIn) {
			aRSImageProducer_1165.drawGraphics(clientSize == 0 ? 4 : 0, super.graphics, clientSize == 0 ? 4 : 0);
		}
		xCameraPos = l;
		zCameraPos = i1;
		yCameraPos = j1;
		yCameraCurve = k1;
		xCameraCurve = l1;
	}

	public void clearTopInterfaces() {
		stream.createFrame(130);
		if (invOverlayInterfaceID != -1) {
			invOverlayInterfaceID = -1;
			needDrawTabArea = true;
			aBoolean1149 = false;
			tabAreaAltered = true;
		}
		if (backDialogID != -1) {
			backDialogID = -1;
			inputTaken = true;
			aBoolean1149 = false;
		}
		openInterfaceID = -1;
		fullscreenInterfaceID = -1;
	}

	public Client() {
		fullscreenInterfaceID = -1;
		this.toggleSize(0);
		chatRights = new int[500];
		chatTypeView = 0;
		clanChatMode = 0;
		globalMode = 0;
		gameMode = 0;
		cButtonHPos = -1;
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
		aBooleanArray876 = new boolean[5];
		
		anInt874 = -1;
		anInt1289 = -1;
		
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
		aByteArray912 = new byte[16384];
		currentStats = new int[Skills.skillsCount];
		ignoreListAsLongs = new long[100];
		loadingError = false;
		anIntArray928 = new int[5];
		anIntArrayArray929 = new int[104][104];
		chatTypes = new int[500];
		chatNames = new String[500];
		chatMessages = new String[500];
		sideIcons = new Sprite[15];
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
		anIntArray1052 = new int[152];
		anIntArray1229 = new int[152];
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
		skullIcons = new Sprite[20];
		headIconsHint = new Sprite[20];
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
		musicEnabled = true;
		needDrawTabArea = false;
		loggedIn = false;
		canMute = false;
		aBoolean1159 = false;
		aBoolean1160 = false;
		anInt1171 = 1;
	//	myUsername = "";
	//	myPassword = "";
		genericLoadingError = false;
		reportAbuseInterfaceID = -1;
		aClass19_1179 = new NodeList();
		anInt1184 = 128;
		invOverlayInterfaceID = -1;
		stream = Stream.create();
		menuActionName = new String[500];
		anIntArray1203 = new int[5];
		sound = new int[50];
		anInt1210 = 2;
		chatScrollMax = 78;
		promptInput = "";
		modIcons = new Background[2];
		tabID = 3;
		inputTaken = false;
		songChanging = true;
		aClass11Array1230 = new Class11[4];
		anIntArray1240 = new int[100];
		soundType = new int[50];
		aBoolean1242 = false;
		soundDelay = new int[50];
		soundVolume = new int[50];
		rsAlreadyLoaded = false;
		welcomeScreenRaised = false;
		messagePromptRaised = false;
		loginMessage1 = "";
		loginMessage2 = "";
		backDialogID = -1;
		anInt1279 = 2;
		bigX = new int[4000];
		bigY = new int[4000];
	}
	
	public static int getMaxWidth() {
		return (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	}

	public static int getMaxHeight() {
		return (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	}

	public boolean isWebclient() {
		return gameFrame == null && isApplet == true;
	}  
	
	public boolean outDated = true;
	
	public double chosenBrightness = 0.80000000000000004D;
	private boolean roofsToggled = false;
	public boolean autocast;
	public int rights;
	public String clanname;
	public String globalPrefix;
	private final int[] chatRights;
	public int chatTypeView;
	public int clanChatMode;
	public int duelMode;
	public int globalMode;
	public int gameMode;
	public int autoCastId = 0;
	public Sprite[] cacheSprite;
	private Sprite background;
	public Sprite mascotInv;
	public Sprite mascotChat;
	/**/
	private RSImageProducer leftFrame;
	private RSImageProducer topFrame;
	private int ignoreCount;
	private long aLong824;
	private int[][] anIntArrayArray825;
	private int[] friendsNodeIDs;
	private NodeList[][][] groundArray;
	private int[] anIntArray828;
	private int[] anIntArray829;
	private volatile boolean aBoolean831;
	private Socket aSocket832;
	private int loginScreenState = 2;
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
	static int openInterfaceID;
	private int xCameraPos;
	private int zCameraPos;
	private int yCameraPos;
	private int yCameraCurve;
	private int xCameraCurve;
	private int myPrivilege;
	private final int[] currentExp;
	private Sprite mapFlag;
	private Sprite mapMarker;
	private boolean aBoolean872;
	private final int[] anIntArray873;
	private final boolean[] aBooleanArray876;
	
	private int anInt874;
	private int anInt1289;
	private long aLong1172;
	private int anInt1257;
	
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
	private String[] clanList = new String[100];
	private int currentSong;
	private static int nodeID = 10;
	static int portOff;
	static boolean clientData;
	private static boolean isMembers = true;
	private static boolean lowMem;
	private volatile boolean drawingFlames;
	private int spriteDrawX;
	private int spriteDrawY;
	private final int[] anIntArray965 = { 0xffff00, 0xff0000, 65280, 65535, 0xff00ff, 0xffffff };
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
	private final boolean aBoolean994;
	private int anInt995;
	private int anInt996;
	private int anInt997;
	private int anInt998;
	private int anInt999;
	private ISAACRandomGen encryption;
	private Sprite mapEdge;
	private Sprite multiOverlay;
	static final int[][] anIntArrayArray1003 = { { 6798, 107, 10283, 16, 4797, 7744, 5799, 4634, 33697, 22433, 2983, 54193 }, { 8741, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153, 56621, 4783, 1341, 16578, 35003, 25239 }, { 25238, 8742, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153, 56621, 4783, 1341, 16578, 35003 }, { 4626, 11146, 6439, 12, 4758, 10270 }, { 4550, 4537, 5681, 5673, 5790, 6806, 8076, 4574 } };
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
	private boolean walkableInterfaceMode;
	private static final int[] anIntArray1019;
	private int anInt1021;
	private int anInt1022;
	private int loadingStage;
	private Sprite scrollBar1;
	private Sprite scrollBar2;
	private int anInt1026;
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
	private int currentSound;
	private final int barFillColor;
	private int friendsListAction;
	private final int[] anIntArray1065;
	private int mouseInvInterfaceIndex;
	private int lastActiveInvInterface;
	public OnDemandFetcher onDemandFetcher;
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
	private Sprite mapDotClan;
	private int anInt1079;
	private boolean aBoolean1080;
	private String[] friendsList;
	private Stream inStream;
	private int anInt1084;
	private int anInt1085;
	private int activeInterfaceType;
	private int anInt1087;
	private int anInt1088;
	public static int chatScrollPos;
	public static int spellID = 0;
	public int cameraPos1= 3;
	public static int cameraPos2= 600;
	public static int clientWidth = 765, clientHeight = 503;
	public static int clientSize = 0;
	public int appletWidth = 765;
	public int appletHeight = 503;
	private static final int resizableWidth = 900;
	private static final int resizableHeight = 600;
	private int gameAreaWidth = 512;
	private int gameAreaHeight = 334;
	public boolean showTab = true;
	public static boolean showChat = true;
	private int smallTabs = 1000;
	public static int totalRead = 0;
	private final int[] expectedCRCs;
	private int[] menuActionCmd2;
	private int[] menuActionCmd3;
	private int[] menuActionID;
	private int[] menuActionCmd1;
	private Sprite multiWay;
	private Sprite[] headIcons;
	private Sprite[] skullIcons;
	private Sprite[] headIconsHint;
	private static int anInt1097;
	private int anInt1098;
	private int anInt1099;
	private int anInt1100;
	private int anInt1101;
	private int anInt1102;
	private static boolean tabAreaAltered;
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
	public static final int[] tabInterfaceIDs = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
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
	private static boolean musicEnabled;
	private static boolean loginMusicEnabled;
	private Background[] aBackgroundArray1152s;
	static boolean needDrawTabArea;
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
	private RSImageProducer mapEdgeIP;
	private RSImageProducer aRSImageProducer_1164;
	private RSImageProducer aRSImageProducer_1165;
	private RSImageProducer aRSImageProducer_1166;
	private int daysSinceRecovChange;
	private RSSocket socketStream;
	private int anInt1169;
	private int minimapInt3;
	private int anInt1171;
	private static String myUsername = "";
	private static String myPassword = "";
	private static int anInt1175;
	private boolean genericLoadingError;
	private final int[] anIntArray1177 = { 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3 };
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
	static final int[] anIntArray1204 = { 9104, 10275, 7595, 3610, 7975, 8526, 918, 38802, 24466, 10145, 58654, 5027, 1457, 16565, 34991, 25486 };
	private static boolean flagged;
	private final int[] sound;
	private int minimapInt2;
	private int anInt1210;
	static int chatScrollMax;
	private String promptInput;
	private int anInt1213;
	private int[][][] intGroundArray;
	private long aLong1215;
	private int loginScreenCursorPos;
	private final Background[] modIcons;
	private long aLong1220;
	static int tabID;
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
	private int[] anIntArray1234;
	private int[] anIntArray1235;
	private int[] anIntArray1236;
	private int anInt1237;
	private int anInt1238;
	public final int anInt1239 = 100;
	private final int[] anIntArray1240;
	private final int[] soundType;
	private boolean aBoolean1242;
	private int atInventoryLoopCycle;
	private int atInventoryInterface;
	private int atInventoryIndex;
	private int atInventoryInterfaceType;
	private byte[][] aByteArrayArray1247;
	private int tradeMode;
	private int anInt1249;
	private final int[] soundDelay;
	private final int[] soundVolume;
	private int anInt1251;
	private final boolean rsAlreadyLoaded;
	private int anInt1253;
	private int anInt1254;
	private boolean welcomeScreenRaised;
	private boolean messagePromptRaised;
	private byte[][][] byteGroundArray;
	private int previousSong;
	private int destX;
	private int destY;
	private Sprite minimapImage;
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
	public static int anInt1290;
	public static String server = "";
	public int drawCount;
	public int fullscreenInterfaceID;
	public int anInt1044;// 377
	public int anInt1129;// 377
	public int anInt1315;// 377
	public int anInt1500;// 377
	public int anInt1501;// 377
	public int[] fullScreenTextureArray;

	public void resetAllImageProducers() {
		if (super.fullGameScreen != null) {
			return;
		}
		aRSImageProducer_1166 = null;
		aRSImageProducer_1164 = null;
		aRSImageProducer_1163 = null;
		aRSImageProducer_1165 = null;
		aRSImageProducer_1123 = null;
		aRSImageProducer_1124 = null;
		aRSImageProducer_1125 = null;
		aRSImageProducer_1107 = null;
		aRSImageProducer_1108 = null;
		aRSImageProducer_1109 = null;
		aRSImageProducer_1110 = null;
		aRSImageProducer_1111 = null;
		aRSImageProducer_1112 = null;
		aRSImageProducer_1113 = null;
		aRSImageProducer_1114 = null;
		aRSImageProducer_1115 = null;
		super.fullGameScreen = new RSImageProducer(clientWidth, clientHeight, getGameComponent());
		welcomeScreenRaised = true;
	}

	public void launchURL(String url) {
		String osName = System.getProperty("os.name");
		try {
			if (osName.startsWith("Mac OS")) {
				Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
				Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] { String.class });
				openURL.invoke(null, new Object[] { url });
			} else if (osName.startsWith("Windows"))
				Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
			else { // assume Unix or Linux
				String[] browsers = { "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape", "safari" };
				String browser = null;
				for (int count = 0; count < browsers.length && browser == null; count++)
					if (Runtime.getRuntime().exec(new String[] { "which", browsers[count] }).waitFor() == 0)
						browser = browsers[count];
				if (browser == null) {
					throw new Exception("Could not find web browser");
				} else
					Runtime.getRuntime().exec(new String[] { browser, url });
			}
		} catch (Exception e) {
			pushMessage("Failed to open URL.", 0, "");
		}
	}

	static {
		anIntArray1019 = new int[99];
		int i = 0;
		for (int j = 0; j < 99; j++) {
			int l = j + 1;
			int i1 = (int) ((double) l + 300D * Math.pow(2D, (double) l / 7D));
			i += i1;
			anIntArray1019[j] = i / 4;
		}
		anIntArray1232 = new int[32];
		i = 2;
		for (int k = 0; k < 32; k++) {
			anIntArray1232[k] = i - 1;
			i += i;
		}
	}
}
