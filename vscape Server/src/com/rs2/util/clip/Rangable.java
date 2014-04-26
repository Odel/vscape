package com.rs2.util.clip;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.zip.GZIPInputStream;

import com.rs2.cache.object.GameObjectData;

public class Rangable {

	private static Rangable[] rangables;
	private int id;
	private int[][][] clips = new int[4][][];

	public Rangable(int id) {
		this.id = id;
	}

	public int id() {
		return id;
	}

	private void addClip(int x, int y, int height, int shift) {
		try {
			int regionAbsX = (id >> 8) * 64;
			int regionAbsY = (id & 0xff) * 64;
			if (clips[height] == null) {
				clips[height] = new int[64][64];
			}
			clips[height][x - regionAbsX][y - regionAbsY] |= shift;
		} catch (Exception e) {
		}
	}

	private void removeClip(int x, int y, int height, int shift) {
		int regionAbsX = (id >> 8) * 64;
		int regionAbsY = (id & 0xff) * 64;
		if (clips == null) {
			return;
		}
		if (clips[height] == null) {
			return;
		}
		clips[height][x - regionAbsX][y - regionAbsY] &= 16777215 - shift;
	}

	private int getClip(int x, int y, int height) {
		int regionAbsX = (id >> 8) * 64;
		int regionAbsY = (id & 0xff) * 64;
		if (clips[height] == null) {
			return 0;
		}
		return clips[height][x - regionAbsX][y - regionAbsY];
	}

	private static void addClipping(int x, int y, int height, int shift) {
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = (regionX / 8 << 8) + regionY / 8;
		for (Rangable r : rangables) {
			if (r.id() == regionId) {
				r.addClip(x, y, height, shift);
				break;
			}
		}
	}

	private static void removeClipping(int x, int y, int height, int shift) {
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		for (Rangable r : rangables) {
			if (r.id() == regionId) {
				r.removeClip(x, y, height, shift);
				break;
			}
		}
	}

	public static void addClippingForVariableObject(int x, int y, int height, int type, int direction, boolean flag) {
		if (type == 0) {
			if (direction == 0) {
				addClipping(x, y, height, 128);
				addClipping(x - 1, y, height, 8);
			} else if (direction == 1) {
				addClipping(x, y, height, 2);
				addClipping(x, y + 1, height, 32);
			} else if (direction == 2) {
				addClipping(x, y, height, 8);
				addClipping(x + 1, y, height, 128);
			} else if (direction == 3) {
				addClipping(x, y, height, 32);
				addClipping(x, y - 1, height, 2);
			}
		} else if (type == 1 || type == 3) {
			if (direction == 0) {
				addClipping(x, y, height, 1);
				addClipping(x - 1, y, height, 16);
			} else if (direction == 1) {
				addClipping(x, y, height, 4);
				addClipping(x + 1, y + 1, height, 64);
			} else if (direction == 2) {
				addClipping(x, y, height, 16);
				addClipping(x + 1, y - 1, height, 1);
			} else if (direction == 3) {
				addClipping(x, y, height, 64);
				addClipping(x - 1, y - 1, height, 4);
			}
		} else if (type == 2) {
			if (direction == 0) {
				addClipping(x, y, height, 130);
				addClipping(x - 1, y, height, 8);
				addClipping(x, y + 1, height, 32);
			} else if (direction == 1) {
				addClipping(x, y, height, 10);
				addClipping(x, y + 1, height, 32);
				addClipping(x + 1, y, height, 128);
			} else if (direction == 2) {
				addClipping(x, y, height, 40);
				addClipping(x + 1, y, height, 128);
				addClipping(x, y - 1, height, 2);
			} else if (direction == 3) {
				addClipping(x, y, height, 160);
				addClipping(x, y - 1, height, 2);
				addClipping(x - 1, y, height, 8);
			}
		}
		if (flag) {
			if (type == 0) {
				if (direction == 0) {
					addClipping(x, y, height, 65536);
					addClipping(x - 1, y, height, 4096);
				} else if (direction == 1) {
					addClipping(x, y, height, 1024);
					addClipping(x, y + 1, height, 16384);
				} else if (direction == 2) {
					addClipping(x, y, height, 4096);
					addClipping(x + 1, y, height, 65536);
				} else if (direction == 3) {
					addClipping(x, y, height, 16384);
					addClipping(x, y - 1, height, 1024);
				}
			}
			if (type == 1 || type == 3) {
				if (direction == 0) {
					addClipping(x, y, height, 512);
					addClipping(x - 1, y + 1, height, 8192);
				} else if (direction == 1) {
					addClipping(x, y, height, 2048);
					addClipping(x + 1, y + 1, height, 32768);
				} else if (direction == 2) {
					addClipping(x, y, height, 8192);
					addClipping(x + 1, y + 1, height, 512);
				} else if (direction == 3) {
					addClipping(x, y, height, 32768);
					addClipping(x - 1, y - 1, height, 2048);
				}
			} else if (type == 2) {
				if (direction == 0) {
					addClipping(x, y, height, 66560);
					addClipping(x - 1, y, height, 4096);
					addClipping(x, y + 1, height, 16384);
				} else if (direction == 1) {
					addClipping(x, y, height, 5120);
					addClipping(x, y + 1, height, 16384);
					addClipping(x + 1, y, height, 65536);
				} else if (direction == 2) {
					addClipping(x, y, height, 20480);
					addClipping(x + 1, y, height, 65536);
					addClipping(x, y - 1, height, 1024);
				} else if (direction == 3) {
					addClipping(x, y, height, 81920);
					addClipping(x, y - 1, height, 1024);
					addClipping(x - 1, y, height, 4096);
				}
			}
		}
	}

	public static void removeClippingForVariableObject(int x, int y, int height, int type, int direction, boolean flag) {
		if (type == 0) {
			if (direction == 0) {
				removeClipping(x, y, height, 128);
				removeClipping(x - 1, y, height, 8);
			} else if (direction == 1) {
				removeClipping(x, y, height, 2);
				removeClipping(x, y + 1, height, 32);
			} else if (direction == 2) {
				removeClipping(x, y, height, 8);
				removeClipping(x + 1, y, height, 128);
			} else if (direction == 3) {
				removeClipping(x, y, height, 32);
				removeClipping(x, y - 1, height, 2);
			}
		} else if (type == 1 || type == 3) {
			if (direction == 0) {
				removeClipping(x, y, height, 1);
				removeClipping(x - 1, y, height, 16);
			} else if (direction == 1) {
				removeClipping(x, y, height, 4);
				removeClipping(x + 1, y + 1, height, 64);
			} else if (direction == 2) {
				removeClipping(x, y, height, 16);
				removeClipping(x + 1, y - 1, height, 1);
			} else if (direction == 3) {
				removeClipping(x, y, height, 64);
				removeClipping(x - 1, y - 1, height, 4);
			}
		} else if (type == 2) {
			if (direction == 0) {
				removeClipping(x, y, height, 130);
				removeClipping(x - 1, y, height, 8);
				removeClipping(x, y + 1, height, 32);
			} else if (direction == 1) {
				removeClipping(x, y, height, 10);
				removeClipping(x, y + 1, height, 32);
				removeClipping(x + 1, y, height, 128);
			} else if (direction == 2) {
				removeClipping(x, y, height, 40);
				removeClipping(x + 1, y, height, 128);
				removeClipping(x, y - 1, height, 2);
			} else if (direction == 3) {
				removeClipping(x, y, height, 160);
				removeClipping(x, y - 1, height, 2);
				removeClipping(x - 1, y, height, 8);
			}
		}
		if (flag) {
			if (type == 0) {
				if (direction == 0) {
					removeClipping(x, y, height, 65536);
					removeClipping(x - 1, y, height, 4096);
				} else if (direction == 1) {
					removeClipping(x, y, height, 1024);
					removeClipping(x, y + 1, height, 16384);
				} else if (direction == 2) {
					removeClipping(x, y, height, 4096);
					removeClipping(x + 1, y, height, 65536);
				} else if (direction == 3) {
					removeClipping(x, y, height, 16384);
					removeClipping(x, y - 1, height, 1024);
				}
			}
			if (type == 1 || type == 3) {
				if (direction == 0) {
					removeClipping(x, y, height, 512);
					removeClipping(x - 1, y + 1, height, 8192);
				} else if (direction == 1) {
					removeClipping(x, y, height, 2048);
					removeClipping(x + 1, y + 1, height, 32768);
				} else if (direction == 2) {
					removeClipping(x, y, height, 8192);
					removeClipping(x + 1, y + 1, height, 512);
				} else if (direction == 3) {
					removeClipping(x, y, height, 32768);
					removeClipping(x - 1, y - 1, height, 2048);
				}
			} else if (type == 2) {
				if (direction == 0) {
					removeClipping(x, y, height, 66560);
					removeClipping(x - 1, y, height, 4096);
					removeClipping(x, y + 1, height, 16384);
				} else if (direction == 1) {
					removeClipping(x, y, height, 5120);
					removeClipping(x, y + 1, height, 16384);
					removeClipping(x + 1, y, height, 65536);
				} else if (direction == 2) {
					removeClipping(x, y, height, 20480);
					removeClipping(x + 1, y, height, 65536);
					removeClipping(x, y - 1, height, 1024);
				} else if (direction == 3) {
					removeClipping(x, y, height, 81920);
					removeClipping(x, y - 1, height, 1024);
					removeClipping(x - 1, y, height, 4096);
				}
			}
		}
	}

	public static void addClippingForSolidObject(int x, int y, int height, int xLength, int yLength, boolean flag) {
		int clipping = 256;
		if (flag) {
			clipping += 0x20000;
		}
		for (int i = x; i < x + xLength; i++) {
			for (int i2 = y; i2 < y + yLength; i2++) {
				addClipping(i, i2, height, clipping);
			}
		}
	}

	public static void removeClippingForSolidObject(int x, int y, int height, int xLength, int yLength, boolean flag) {
		int clipping = 256;
		if (flag) {
			clipping += 0x20000;
		}
		for (int i = x; i < x + xLength; i++) {
			for (int i2 = y; i2 < y + yLength; i2++) {
				removeClipping(i, i2, height, clipping);
			}
		}
	}

	public static void addDoorClip(int x, int y, int height, int face) {
		addClippingForVariableObject(x, y, height, 0, face, false);
	}

	public static void removeDoorClip(int x, int y, int height, int face) {
		removeClippingForVariableObject(x, y, height, 0, face, true);
	}

	public static void updateDoorClip(int x, int y, int height, int oldFace, int newFace) {
		removeClippingForVariableObject(x, y, height, 0, oldFace, true);
		addClippingForVariableObject(x, y, height, 0, newFace, false);
		/*
		 * removeClipping(x, y, height); if (original == 0) { removeClipping(x,
		 * y - 1, height); } else if (original == 1) { removeClipping(x + 1, y,
		 * height); } else if (original == 2) { removeClipping(x, y - 1,
		 * height); } else if (original == 3) { removeClipping(x - 1, y,
		 * height); } addClippingForVariableObject(x, y, height, 0, face,
		 * false);
		 */
	}

	public static void updateDoorClip(int x, int y, int height, int face) {
		addClippingForVariableObject(x, y, height, 0, face, false);
	}

	public static void addObject(int objectId, int x, int y, int height, int direction, int type, boolean startUp) {
		if (objectId < 0 || GameObjectData.forId(objectId).canShootThru()) {
			if (!startUp) {
				removeClippingForSolidObject(x, y, height, 0, 0, true);
			}
			return;
		}
		if (x >=3256 && x <= 3259 && y >= 3926 && y <= 3928) {
			System.out.println(objectId);
		}
        GameObjectData data = GameObjectData.forId(objectId);
        if (data == null) {
            return;
        }
        int xLength = data.getSizeX(direction);
        int yLength = data.getSizeY(direction);

		if (type == 22) {
			if (data.hasActions() && data.isSolid()) {
				if (!startUp) {
					removeClippingForVariableObject(x, y, height, type, direction, data.unknown());
				}
				addClipping(x, y, height, 0x200000);
			}
		} else if (type >= 9) {
			if (data.isSolid()) {
				if (!startUp) {
					removeClippingForSolidObject(x, y, height, xLength, yLength, data.unknown());
				}
				addClippingForSolidObject(x, y, height, xLength, yLength, data.unknown());
			}
		} else if (type >= 0 && type <= 3) {
			if (data.isSolid()) {
				if (!startUp) {
					removeClippingForSolidObject(x, y, height, xLength, yLength, data.unknown());
				}
				addClippingForVariableObject(x, y, height, type, direction, data.unknown());
			}
		}
	}

	public static void removeObjectAndClip(int id, int x, int y, int height, int direction, int type) {
        GameObjectData data = GameObjectData.forId(id);
        if (data == null) {
            System.out.println("ID: "+id+" HAS NO DEF");
            return;
        }
        int xLength = data.getSizeX(direction);
        int yLength = data.getSizeY(direction);
		removeClippingForSolidObject(x, y, height, 1, 1, data.unknown());
		if (type == 22) {
			removeClipping(x, y, height, 0x200000);
		} else if (type >= 9) {
			removeClippingForSolidObject(x, y, height, xLength, yLength, data.unknown());
		} else if (type >= 0 && type <= 3) {
			removeClippingForVariableObject(x, y, height, type, direction, data.unknown());
		}
	}

	public static void removeObject(int id, int x, int y, int height, int direction, int type) {
        GameObjectData data = GameObjectData.forId(id);
        if (data == null) {
            System.out.println("ID: "+id+" HAS NO DEF");
            return;
        }
        int xLength = data.getSizeX(direction);
        int yLength = data.getSizeY(direction);
		if (type == 22) {
			removeClipping(x, y, height, 0x200000);
		} else if (type >= 9) {
			removeClippingForSolidObject(x, y, height, xLength, yLength, data.unknown());
		} else if (type >= 0 && type <= 3) {
			removeClippingForVariableObject(x, y, height, type, direction, data.unknown());
		}
	}

	public static int getClipping(int x, int y, int height) {
		if (height > 3) {
			height = 0;
		}
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = (regionX / 8 << 8) + regionY / 8;
		for (Rangable r : rangables) {
			if (r.id() == regionId) {
				return r.getClip(x, y, height);
			}
		}
		return 0;
	}

   public static int[] getNextStep(int baseX, int baseY, int toX, int toY, int height, int xLength, int yLength) {
        int moveX = 0;
        int moveY = 0;
        if (baseX - toX > 0) {
            moveX--;
        } else if (baseX - toX < 0) {
            moveX++;
        }
        if (baseY - toY > 0) {
            moveY--;
        } else if (baseY - toY < 0) {
            moveY++;
        }
        if (canMove(baseX, baseY, baseX + moveX, baseY + moveY, height, xLength, yLength)) {
            return new int[] { baseX + moveX, baseY + moveY };
        } else if (moveX != 0 && canMove(baseX, baseY, baseX + moveX, baseY, height, xLength, yLength)) {
            return new int[] { baseX + moveX, baseY };
        } else if (moveY != 0 && canMove(baseX, baseY, baseX, baseY + moveY, height, xLength, yLength)) {
            return new int[] { baseX, baseY + moveY };
        }
        return new int[] { baseX, baseY };
    }
  
    public static boolean canMove(int startX, int startY, int endX, int endY, int height, int xLength, int yLength) {
        int diffX = endX - startX;
        int diffY = endY - startY;
        int max = Math.max(Math.abs(diffX), Math.abs(diffY));
        for (int ii = 0; ii < max; ii++) {
            int currentX = endX - diffX;
            int currentY = endY - diffY;
            for (int i = 0; i < xLength; i++) {
                for (int i2 = 0; i2 < yLength; i2++) {
                    if (diffX < 0 && diffY < 0) {
                        if ((getClipping(currentX + i - 1, currentY + i2 - 1, height) & 0x128010e) != 0 || (getClipping(currentX + i - 1, currentY + i2, height) & 0x1280108) != 0
                                || (getClipping(currentX + i, currentY + i2 - 1, height) & 0x1280102) != 0) {
                            return false;
                        }
                    } else if (diffX > 0 && diffY > 0) {
                        if ((getClipping(currentX + i + 1, currentY + i2 + 1, height) & 0x12801e0) != 0 || (getClipping(currentX + i + 1, currentY + i2, height) & 0x1280180) != 0
                                || (getClipping(currentX + i, currentY + i2 + 1, height) & 0x1280120) != 0) {
                            return false;
                        }
                    } else if (diffX < 0 && diffY > 0) {
                        if ((getClipping(currentX + i - 1, currentY + i2 + 1, height) & 0x1280138) != 0 || (getClipping(currentX + i - 1, currentY + i2, height) & 0x1280108) != 0
                                || (getClipping(currentX + i, currentY + i2 + 1, height) & 0x1280120) != 0) {
                            return false;
                        }
                    } else if (diffX > 0 && diffY < 0) {
                        if ((getClipping(currentX + i + 1, currentY + i2 - 1, height) & 0x1280183) != 0 || (getClipping(currentX + i + 1, currentY + i2, height) & 0x1280180) != 0
                                || (getClipping(currentX + i, currentY + i2 - 1, height) & 0x1280102) != 0) {
                            return false;
                        }
                    } else if (diffX > 0 && diffY == 0) {
                        if ((getClipping(currentX + i + 1, currentY + i2, height) & 0x1280180) != 0) {
                            return false;
                        }
                    } else if (diffX < 0 && diffY == 0) {
                        if ((getClipping(currentX + i - 1, currentY + i2, height) & 0x1280108) != 0) {
                            return false;
                        }
                    } else if (diffX == 0 && diffY > 0) {
                        if ((getClipping(currentX + i, currentY + i2 + 1, height) & 0x1280120) != 0) {
                            return false;
                        }
                    } else if (diffX == 0 && diffY < 0) {
                        if ((getClipping(currentX + i, currentY + i2 - 1, height) & 0x1280102) != 0) {
                            return false;
                        }
                    }
                }
            }
            if (diffX < 0) {
                diffX++;
            } else if (diffX > 0) {
                diffX--;
            }
            if (diffY < 0) {
                diffY++;
            } else if (diffY > 0) {
                diffY--;
            }
        }
        return true;
    }

	public static void load() {
		try {
			File f = new File("./data/world/map_index");
			byte[] buffer = new byte[(int) f.length()];
			DataInputStream dis = new DataInputStream(new FileInputStream(f));
			dis.readFully(buffer);
			dis.close();
			ByteStream in = new ByteStream(buffer);
			int size = in.length() / 7;
			rangables = new Rangable[size];
			int[] regionIds = new int[size];
			int[] mapGroundFileIds = new int[size];
			int[] mapObjectsFileIds = new int[size];
			for (int i = 0; i < size; i++) {
				regionIds[i] = in.getUShort();
				mapGroundFileIds[i] = in.getUShort();
				mapObjectsFileIds[i] = in.getUShort();
				in.getUByte();
			}
			for (int i = 0; i < size; i++) {
				rangables[i] = new Rangable(regionIds[i]);
			}
			for (int i = 0; i < size; i++) {
				byte[] file1 = getBuffer(new File("./data/world/map/" + mapObjectsFileIds[i] + ".gz"));
				byte[] file2 = getBuffer(new File("./data/world/map/" + mapGroundFileIds[i] + ".gz"));
				if (file1 == null || file2 == null) {
					continue;
				}
				try {
					loadMaps(regionIds[i], new ByteStream(file1), new ByteStream(file2));
				} catch (Exception e) {
					System.out.println("Error loading map region: " + regionIds[i]);
				}
			}
			System.out.println("[Rangable] DONE LOADING REGION CONFIGURATIONS");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void loadMaps(int regionId, ByteStream str1, ByteStream str2) {
		int absX = (regionId >> 8) * 64;
		int absY = (regionId & 0xff) * 64;
		int[][][] someArray = new int[4][64][64];
		for (int i = 0; i < 4; i++) {
			for (int i2 = 0; i2 < 64; i2++) {
				for (int i3 = 0; i3 < 64; i3++) {
					while (true) {
						int v = str2.getUByte();
						if (v == 0) {
							break;
						} else if (v == 1) {
							str2.skip(1);
							break;
						} else if (v <= 49) {
							str2.skip(1);
						} else if (v <= 81) {
							someArray[i][i2][i3] = v - 49;
						}
					}
				}
			}
		}
		int objectId = -1;
		int incr;
		while ((incr = str1.getUSmart()) != 0) {
			objectId += incr;
			int location = 0;
			int incr2;
			while ((incr2 = str1.getUSmart()) != 0) {
				location += incr2 - 1;
				int localX = location >> 6 & 0x3f;
				int localY = location & 0x3f;
				int height = location >> 12;
				int objectData = str1.getUByte();
				int type = objectData >> 2;
				int direction = objectData & 0x3;
				if (localX < 0 || localX >= 64 || localY < 0 || localY >= 64) {
					continue;
				}
				if ((someArray[1][localX][localY] & 2) == 2) {
					height--;
				}
				if (height >= 0 && height <= 3 && !GameObjectData.forId(objectId).canShootThru()) { //.isRangeAble()
					addObject(objectId, absX + localX, absY + localY, height, direction, type, true);
				}
			}
		}
	}

	public static byte[] getBuffer(File f) throws Exception {
		if (!f.exists()) {
			return null;
		}
		byte[] buffer = new byte[(int) f.length()];
		DataInputStream dis = new DataInputStream(new FileInputStream(f));
		dis.readFully(buffer);
		dis.close();
		byte[] gzipInputBuffer = new byte[999999];
		int bufferlength = 0;
		GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(buffer));
		do {
			if (bufferlength == gzipInputBuffer.length) {
				System.out.println("Error inflating data.\nGZIP buffer overflow.");
				break;
			}
			int readByte = gzip.read(gzipInputBuffer, bufferlength, gzipInputBuffer.length - bufferlength);
			if (readByte == -1) {
				break;
			}
			bufferlength += readByte;
		} while (true);
		byte[] inflated = new byte[bufferlength];
		System.arraycopy(gzipInputBuffer, 0, inflated, 0, bufferlength);
		buffer = inflated;
		if (buffer.length < 10) {
			return null;
		}
		return buffer;
	}

}