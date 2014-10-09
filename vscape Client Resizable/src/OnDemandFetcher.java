import java.io.*;
import java.net.Socket;
import java.util.zip.GZIPInputStream;

public final class OnDemandFetcher extends OnDemandFetcherParent implements Runnable {

	private void readData() {
		try {
			int j = inputStream.available();
			if (expectedSize == 0 && j >= 6) {
				waiting = true;
				for (int k = 0; k < 6; k += inputStream.read(ioBuffer, k, 6 - k))
					;
				int l = ioBuffer[0] & 0xff;
				int j1 = ((ioBuffer[1] & 0xff) << 8) + (ioBuffer[2] & 0xff);
				int l1 = ((ioBuffer[3] & 0xff) << 8) + (ioBuffer[4] & 0xff);
				int i2 = ioBuffer[5] & 0xff;
				current = null;
				for (OnDemandData onDemandData = (OnDemandData) requested.reverseGetFirst(); onDemandData != null; onDemandData = (OnDemandData) requested.reverseGetNext()) {
					if (onDemandData.dataType == l && onDemandData.ID == j1)
						current = onDemandData;
					if (current != null)
						onDemandData.loopCycle = 0;
				}

				if (current != null) {
					loopCycle = 0;
					if (l1 == 0) {
						Signlink.reporterror("Rej: " + l + "," + j1);
						current.buffer = null;
						if (current.incomplete)
							synchronized (aClass19_1358) {
								aClass19_1358.insertHead(current);
							}
						else
							current.unlink();
						current = null;
					} else {
						if (current.buffer == null && i2 == 0)
							current.buffer = new byte[l1];
						if (current.buffer == null && i2 != 0)
							throw new IOException("missing start of file");
					}
				}
				completedSize = i2 * 500;
				expectedSize = 500;
				if (expectedSize > l1 - i2 * 500)
					expectedSize = l1 - i2 * 500;
			}
			if (expectedSize > 0 && j >= expectedSize) {
				waiting = true;
				byte abyte0[] = ioBuffer;
				int i1 = 0;
				if (current != null) {
					abyte0 = current.buffer;
					i1 = completedSize;
				}
				for (int k1 = 0; k1 < expectedSize; k1 += inputStream.read(abyte0, k1 + i1, expectedSize - k1))
					;
				if (expectedSize + completedSize >= abyte0.length && current != null) {
					if (clientInstance.decompressors[0] != null)
						clientInstance.decompressors[current.dataType + 1].method234(abyte0.length, abyte0, current.ID);
					if (!current.incomplete && current.dataType == 3) {
						current.incomplete = true;
						current.dataType = 93;
					}
					if (current.incomplete)
						synchronized (aClass19_1358) {
							aClass19_1358.insertHead(current);
						}
					else
						current.unlink();
				}
				expectedSize = 0;
			}
		} catch (IOException ioexception) {
			try {
				socket.close();
			} catch (Exception _ex) {
			}
			socket = null;
			inputStream = null;
			outputStream = null;
			expectedSize = 0;
		}
	}

	public int mapAmount = 0;

/*	public void start(StreamLoader streamLoader, Client client1) {
		byte[] abyte2 = streamLoader.getDataForName("map_index");
		Stream stream2 = new Stream(abyte2);
		int j1 = abyte2.length / 7;
		mapIndices1 = new int[j1];
		mapIndices2 = new int[j1];
		mapIndices3 = new int[j1];
		for (int i2 = 0; i2 < j1; i2++) {
			mapIndices1[i2] = stream2.readUnsignedWord();
			mapIndices2[i2] = stream2.readUnsignedWord();
			mapIndices3[i2] = stream2.readUnsignedWord();
			mapAmount++;
		}
		System.out.println("Map Amount: " + mapAmount + "");
		abyte2 = streamLoader.getDataForName("midi_index");
		stream2 = new Stream(abyte2);
		j1 = abyte2.length;
		anIntArray1348 = new int[j1];
		for (int k2 = 0; k2 < j1; k2++)
			anIntArray1348[k2] = stream2.readUnsignedByte();

		clientInstance = client1;
		running = true;
		clientInstance.startRunnable(this, 2);
	}*/
	
	public void start(StreamLoader streamLoader, Client client1) {
        byte[] abyte2 = streamLoader.getDataForName("map_index");
        Stream stream2 = new Stream(abyte2);
        int j1 = abyte2.length / 7;
        mapIndices1 = new int[j1];
        mapIndices2 = new int[j1];
        mapIndices3 = new int[j1];
        mapIndices4 = new int[j1];
        for (int i2 = 0; i2 < j1; i2++) {
            mapIndices1[i2] = stream2.readUnsignedWord();
            mapIndices2[i2] = stream2.readUnsignedWord();
            mapIndices3[i2] = stream2.readUnsignedWord();
           mapIndices4[i2] = stream2.readUnsignedByte();
        }

        abyte2 = streamLoader.getDataForName("midi_index");
        stream2 = new Stream(abyte2);
        j1 = abyte2.length;
        anIntArray1348 = new int[j1];
        for (int k2 = 0; k2 < j1; k2++)
            anIntArray1348[k2] = stream2.readUnsignedByte();

        clientInstance = client1;
        running = true;
        clientInstance.startRunnable(this, 2);
    }

	public int getNodeCount() {
		synchronized (nodeSubList) {
			return nodeSubList.getNodeCount();
		}
	}

	public void disable() {
		running = false;
	}

	public void method554(boolean flag) {
		int j = mapIndices1.length;
		for (int k = 0; k < j; k++)
			if (flag || mapIndices4[k] != 0) {
				method563((byte) 2, 3, mapIndices3[k]);
				method563((byte) 2, 3, mapIndices2[k]);
			}

	}

	public int getVersionCount(int j) {
		return versions[j].length;
	}

	private void closeRequest(OnDemandData onDemandData) {
		try {
			if (socket == null) {
				long l = System.currentTimeMillis();
				if (l - openSocketTime < 4000L)
					return;
				openSocketTime = l;
				socket = clientInstance.openSocket(43594 + Client.portOff);
				inputStream = socket.getInputStream();
				outputStream = socket.getOutputStream();
				outputStream.write(15);
				for (int j = 0; j < 8; j++)
					inputStream.read();

				loopCycle = 0;
			}
			ioBuffer[0] = (byte) onDemandData.dataType;
			ioBuffer[1] = (byte) (onDemandData.ID >> 8);
			ioBuffer[2] = (byte) onDemandData.ID;
			if (onDemandData.incomplete)
				ioBuffer[3] = 2;
			else if (!clientInstance.loggedIn)
				ioBuffer[3] = 1;
			else
				ioBuffer[3] = 0;
			outputStream.write(ioBuffer, 0, 4);
			writeLoopCycle = 0;
			anInt1349 = -10000;
			return;
		} catch (IOException ioexception) {
		}
		try {
			socket.close();
		} catch (Exception _ex) {
		}
		socket = null;
		inputStream = null;
		outputStream = null;
		expectedSize = 0;
		anInt1349++;
	}

	public int getAnimCount() {
		return anIntArray1360.length;
	}

	public int getModelCount() {
		return 29191;
	}

	public void method558(int i, int j) {
		synchronized (nodeSubList) {
			for (OnDemandData onDemandData = (OnDemandData) nodeSubList.reverseGetFirst(); onDemandData != null; onDemandData = (OnDemandData) nodeSubList.reverseGetNext())
				if (onDemandData.dataType == i && onDemandData.ID == j)
					return;

			OnDemandData onDemandData_1 = new OnDemandData();
			onDemandData_1.dataType = i;
			onDemandData_1.ID = j;
			onDemandData_1.incomplete = true;
			synchronized (aClass19_1370) {
				aClass19_1370.insertHead(onDemandData_1);
			}
			nodeSubList.insertHead(onDemandData_1);
		}
	}

	public int getModelIndex(int i) {
		return modelIndices[i] & 0xff;
	}

	public void run() {
		try {
			while (running) {
				onDemandCycle++;
				int i = 20;
				if (anInt1332 == 0 && clientInstance.decompressors[0] != null)
					i = 50;
				try {
					Thread.sleep(i);
				} catch (Exception _ex) {
				}
				waiting = true;
				for (int j = 0; j < 100; j++) {
					if (!waiting)
						break;
					waiting = false;
					checkReceived();
					handleFailed();
					if (uncompletedCount == 0 && j >= 5)
						break;
					method568();
					if (inputStream != null)
						readData();
				}

				boolean flag = false;
				for (OnDemandData onDemandData = (OnDemandData) requested.reverseGetFirst(); onDemandData != null; onDemandData = (OnDemandData) requested.reverseGetNext())
					if (onDemandData.incomplete) {
						flag = true;
						onDemandData.loopCycle++;
						if (onDemandData.loopCycle > 50) {
							onDemandData.loopCycle = 0;
							closeRequest(onDemandData);
						}
					}

				if (!flag) {
					for (OnDemandData onDemandData_1 = (OnDemandData) requested.reverseGetFirst(); onDemandData_1 != null; onDemandData_1 = (OnDemandData) requested.reverseGetNext()) {
						flag = true;
						onDemandData_1.loopCycle++;
						if (onDemandData_1.loopCycle > 50) {
							onDemandData_1.loopCycle = 0;
							closeRequest(onDemandData_1);
						}
					}

				}
				if (flag) {
					loopCycle++;
					if (loopCycle > 750) {
						try {
							socket.close();
						} catch (Exception _ex) {
						}
						socket = null;
						inputStream = null;
						outputStream = null;
						expectedSize = 0;
					}
				} else {
					loopCycle = 0;
					statusString = "";
				}
				if (clientInstance.loggedIn && socket != null && outputStream != null && (anInt1332 > 0 || clientInstance.decompressors[0] == null)) {
					writeLoopCycle++;
					if (writeLoopCycle > 500) {
						writeLoopCycle = 0;
						ioBuffer[0] = 0;
						ioBuffer[1] = 0;
						ioBuffer[2] = 0;
						ioBuffer[3] = 10;
						try {
							outputStream.write(ioBuffer, 0, 4);
						} catch (IOException _ex) {
							loopCycle = 5000;
						}
					}
				}
			}
		} catch (Exception exception) {
			Signlink.reporterror("od_ex " + exception.getMessage());
		}
	}

	public void method560(int i, int j) {
		if (clientInstance.decompressors[0] == null)
			return;
		if (anInt1332 == 0)
			return;
		OnDemandData onDemandData = new OnDemandData();
		onDemandData.dataType = j;
		onDemandData.ID = i;
		onDemandData.incomplete = false;
		synchronized (aClass19_1344) {
			aClass19_1344.insertHead(onDemandData);
		}
	}

	public OnDemandData getNextNode() {
		OnDemandData onDemandData;
		synchronized (aClass19_1358) {
			onDemandData = (OnDemandData) aClass19_1358.popHead();
		}
		if (onDemandData == null)
			return null;
		synchronized (nodeSubList) {
			onDemandData.unlinkSub();
		}
		if (onDemandData.buffer == null)
			return onDemandData;
		int i = 0;
		try {
			GZIPInputStream gzipinputstream = new GZIPInputStream(new ByteArrayInputStream(onDemandData.buffer));
			do {
				if (i == gzipInputBuffer.length)
					throw new RuntimeException("buffer overflow!");
				int k = gzipinputstream.read(gzipInputBuffer, i, gzipInputBuffer.length - i);
				if (k == -1)
					break;
				i += k;
			} while (true);
		} catch (IOException _ex) {
			// RuntimeException("error unzipping");
			System.out.println("Failed to unzip model [" + onDemandData.ID + "] type = " + onDemandData.dataType);
			_ex.printStackTrace();
			return null;
		}
		onDemandData.buffer = new byte[i];
		System.arraycopy(gzipInputBuffer, 0, onDemandData.buffer, 0, i);

		return onDemandData;
	}

	public int method562(int i, int k, int l) {
		int i1 = (l << 8) + k;
		int mapNigga2;
		int mapNigga3;
		for (int j1 = 0; j1 < mapIndices1.length; j1++) {
			if (mapIndices1[j1] == i1) {
				if (i == 0) {
					mapNigga2 = mapIndices2[j1] > 3535 ? -1 : mapIndices2[j1];
					return mapNigga2;
				} else {
					mapNigga3 = mapIndices3[j1] > 3535 ? -1 : mapIndices3[j1];
					return mapNigga3;
				}
			}
		}
		return -1;
	}

	public void method548(int i) {
		method558(0, i);
	}

	public void method563(byte byte0, int i, int j) {
		if (clientInstance.decompressors[0] == null)
			return;
		if (versions[i][j] == 0)
			return;
		clientInstance.decompressors[i + 1].decompress(j);
		fileStatus[i][j] = byte0;
		if (byte0 > anInt1332)
			anInt1332 = byte0;
		totalFiles++;
	}

	public boolean method564(int i) {
		for (int k = 0; k < mapIndices1.length; k++)
			if (mapIndices3[k] == i)
				return true;
		return false;
	}

	private void handleFailed() {
		uncompletedCount = 0;
		completedCount = 0;
		for (OnDemandData onDemandData = (OnDemandData) requested.reverseGetFirst(); onDemandData != null; onDemandData = (OnDemandData) requested.reverseGetNext())
			if (onDemandData.incomplete) {
				uncompletedCount++;
				System.out.println("Error: model is incomplete or missing  [ type = " + onDemandData.dataType + "]  [id = " + onDemandData.ID + "]");
			} else
				completedCount++;

		while (uncompletedCount < 10) {
			try {
				OnDemandData onDemandData_1 = (OnDemandData) aClass19_1368.popHead();
				if (onDemandData_1 == null)
					break;
				if (fileStatus[onDemandData_1.dataType][onDemandData_1.ID] != 0)
					filesLoaded++;
				fileStatus[onDemandData_1.dataType][onDemandData_1.ID] = 0;
				requested.insertHead(onDemandData_1);
				uncompletedCount++;
				closeRequest(onDemandData_1);
				waiting = true;
				System.out.println("Error: file is missing  [ type = " + onDemandData_1.dataType + "]  [id = " + onDemandData_1.ID + "]");
			} catch (Exception _ex) {
			}
		}
	}

	public void method566() {
		synchronized (aClass19_1344) {
			aClass19_1344.removeAll();
		}
	}

	private void checkReceived() {
		OnDemandData onDemandData;
		synchronized (aClass19_1370) {
			onDemandData = (OnDemandData) aClass19_1370.popHead();
		}
		while (onDemandData != null) {
			waiting = true;
			byte abyte0[] = null;
			if (clientInstance.decompressors[0] != null)
				abyte0 = clientInstance.decompressors[onDemandData.dataType + 1].decompress(onDemandData.ID);
			synchronized (aClass19_1370) {
				if (abyte0 == null) {
					aClass19_1368.insertHead(onDemandData);
				} else {
					onDemandData.buffer = abyte0;
					synchronized (aClass19_1358) {
						aClass19_1358.insertHead(onDemandData);
					}
				}
				onDemandData = (OnDemandData) aClass19_1370.popHead();
			}
		}
	}

	private void method568() {
		while (uncompletedCount == 0 && completedCount < 10) {
			if (anInt1332 == 0)
				break;
			OnDemandData onDemandData;
			synchronized (aClass19_1344) {
				onDemandData = (OnDemandData) aClass19_1344.popHead();
			}
			while (onDemandData != null) {
				if (fileStatus[onDemandData.dataType][onDemandData.ID] != 0) {
					fileStatus[onDemandData.dataType][onDemandData.ID] = 0;
					requested.insertHead(onDemandData);
					closeRequest(onDemandData);
					waiting = true;
					if (filesLoaded < totalFiles)
						filesLoaded++;
					statusString = "Loading extra files - " + (filesLoaded * 100) / totalFiles + "%";
					completedCount++;
					if (completedCount == 10)
						return;
				}
				synchronized (aClass19_1344) {
					onDemandData = (OnDemandData) aClass19_1344.popHead();
				}
			}
			for (int j = 0; j < 4; j++) {
				byte abyte0[] = fileStatus[j];
				int k = abyte0.length;
				for (int l = 0; l < k; l++)
					if (abyte0[l] == anInt1332) {
						abyte0[l] = 0;
						OnDemandData onDemandData_1 = new OnDemandData();
						onDemandData_1.dataType = j;
						onDemandData_1.ID = l;
						onDemandData_1.incomplete = false;
						requested.insertHead(onDemandData_1);
						closeRequest(onDemandData_1);
						waiting = true;
						if (filesLoaded < totalFiles)
							filesLoaded++;
						statusString = "Loading extra files - " + (filesLoaded * 100) / totalFiles + "%";
						completedCount++;
						if (completedCount == 10)
							return;
					}

			}

			anInt1332--;
		}
	}

	public boolean method569(int i) {
		return anIntArray1348[i] == 1;
	}

	public OnDemandFetcher() {
		requested = new NodeList();
		statusString = "";
		ioBuffer = new byte[500];
		fileStatus = new byte[4][];
		aClass19_1344 = new NodeList();
		running = true;
		waiting = false;
		aClass19_1358 = new NodeList();
		gzipInputBuffer = new byte[0x71868];
		nodeSubList = new NodeSubList();
		versions = new int[4][];
		aClass19_1368 = new NodeList();
		aClass19_1370 = new NodeList();
	}

	private int totalFiles;
	private final NodeList requested;
	private int anInt1332;
	public String statusString;
	private int writeLoopCycle;
	private long openSocketTime;
	private int[] mapIndices3;
	private final byte[] ioBuffer;
	public int onDemandCycle;
	private final byte[][] fileStatus;
	private Client clientInstance;
	private final NodeList aClass19_1344;
	private int completedSize;
	private int expectedSize;
	private int[] anIntArray1348;
	public int anInt1349;
	private int[] mapIndices2;
	private int filesLoaded;
	private boolean running;
	private OutputStream outputStream;
	private int[] mapIndices4;
	private boolean waiting;
	private final NodeList aClass19_1358;
	private final byte[] gzipInputBuffer;
	private int[] anIntArray1360;
	private final NodeSubList nodeSubList;
	private InputStream inputStream;
	private Socket socket;
	private final int[][] versions;
	private int uncompletedCount;
	private int completedCount;
	private final NodeList aClass19_1368;
	private OnDemandData current;
	private final NodeList aClass19_1370;
	private int[] mapIndices1;
	private byte[] modelIndices;
	private int loopCycle;
}
