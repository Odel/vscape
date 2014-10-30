package vscapeClient;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public final class ItemDef {

	public static void nullLoader() {
		mruNodes2 = null;
		mruNodes1 = null;
		streamIndices = null;
		cache = null;
		stream = null;
	}

	public boolean method192(int j) {
		int k = anInt175;
		int l = anInt166;
		if(j == 1)
		{
			k = anInt197;
			l = anInt173;
		}
		if(k == -1)
			return true;
		boolean flag = true;
		if(!Model.method463(k))
			flag = false;
		if(l != -1 && !Model.method463(l))
			flag = false;
		return flag;
	}

	public static void unpackConfig(StreamLoader streamLoader)	 {
		stream = new Stream(streamLoader.getDataForName("obj.dat"));
		Stream stream = new Stream(streamLoader.getDataForName("obj.idx"));
		totalItems = stream.readUnsignedWord();
		streamIndices = new int[totalItems + 15000];
		int i = 2;
		for(int j = 0; j < totalItems; j++) {
			streamIndices[j] = i;
			i += stream.readUnsignedWord();
		}
		cache = new ItemDef[10];
		for(int k = 0; k < 10; k++)
			cache[k] = new ItemDef();
	}

	public Model method194(int j) {
		int k = anInt175;
		int l = anInt166;
		if(j == 1) {
			k = anInt197;
			l = anInt173;
		}
		if(k == -1)
			return null;
		Model model = Model.method462(k);
		if(l != -1) {
			Model model_1 = Model.method462(l);
			Model aclass30_sub2_sub4_sub6s[] = {
					model, model_1
			};
			model = new Model(2, aclass30_sub2_sub4_sub6s);
		}
	   if (originalModelColors != null) {
			for (int i1 = 0; i1 < originalModelColors.length; i1++)
				model.method476(originalModelColors[i1], modifiedModelColors[i1]);

		}
		return model;
	}

	public boolean method195(int j) {
		int k = maleEquip1;
		int l = maleEquip2;
		int i1 = anInt185;
		if(j == 1) {
			k = femaleEquip1;
			l = femaleEquip2;
			i1 = anInt162;
		}
		if(k == -1)
			return true;
		boolean flag = true;
		if(!Model.method463(k))
			flag = false;
		if(l != -1 && !Model.method463(l))
			flag = false;
		if(i1 != -1 && !Model.method463(i1))
			flag = false;
		return flag;
	}

	public Model method196(int i) {
		int j = maleEquip1;
		int k = maleEquip2;
		int l = anInt185;
		if(i == 1) {
			j = femaleEquip1;
			k = femaleEquip2;
			l = anInt162;
		}
		if(j == -1)
			return null;
		Model model = Model.method462(j);
		if(k != -1)
			if(l != -1) {
				Model model_1 = Model.method462(k);
				Model model_3 = Model.method462(l);
				Model aclass30_sub2_sub4_sub6_1s[] = {
						model, model_1, model_3
				};
				model = new Model(3, aclass30_sub2_sub4_sub6_1s);
			} else {
				Model model_2 = Model.method462(k);
				Model aclass30_sub2_sub4_sub6s[] = {
						model, model_2
				};
				model = new Model(2, aclass30_sub2_sub4_sub6s);
			}
		if(i == 0 && aByte205 != 0)
			model.method475(0, aByte205, 0);
		if(i == 1 && aByte154 != 0)
			model.method475(0, aByte154, 0);
		if (originalModelColors != null) {
			for (int i1 = 0; i1 < originalModelColors.length; i1++)
				model.method476(originalModelColors[i1], modifiedModelColors[i1]);

		}
		return model;
	}

	
	public void setDefaults() {
		modelID = 0;
		name = null;
		description = null;
		originalModelColors = null;
		modifiedModelColors = null;
		modelZoom = 2000;
		modelRotation1 = 0;
		modelRotation2 = 0;
		anInt204 = 0;
		modelOffset1 = 0;
		modelOffset2 = 0;
		stackable = false;
		value = 1;
		membersObject = false;
		groundActions = null;
		itemActions = null;
		maleEquip1 = -1;
		maleEquip2 = -1;
		aByte205 = 0;
		femaleEquip1 = -1;
		femaleEquip2 = -1;
		aByte154 = 0;
		anInt185 = -1;
		anInt162 = -1;
		anInt175 = -1;
		anInt166 = -1;
		anInt197 = -1;
		anInt173 = -1;
		stackIDs = null;
		stackAmounts = null;
		certID = -1;
		certTemplateID = -1;
		anInt167 = 128;
		anInt192 = 128;
		anInt191 = 128;
		anInt196 = 0;
		anInt184 = 0;
		team = 0;
	}

	public static ItemDef forID(int i) {
		for(int j = 0; j < 10; j++)
			if(cache[j].id == i)
				return cache[j];
		cacheIndex = (cacheIndex + 1) % 10;
		ItemDef itemDef = cache[cacheIndex];
		stream.currentOffset = streamIndices[i];
		itemDef.id = i;
		itemDef.setDefaults();
		itemDef.readValues(stream);
		switch (i) {
			/*case 2568:
				itemDef.itemActions[2] = "Check";
				break;
			case 11074:
				itemDef.itemActions[2] = "Check";				break;
            */
		}
		if(itemDef.certTemplateID != -1)
			itemDef.toNote();
		if(!isMembers && itemDef.membersObject) {
			itemDef.name = "Members Object";
			itemDef.description = "Login to a members' server to use this object.".getBytes();
			itemDef.groundActions = null;
			itemDef.itemActions = null;
			itemDef.team = 0;
		}
		
		return itemDef;
	}

	public void actionData(int a, String b) {
		itemActions = new String[5];
		itemActions[a] = b;
	}
	public void totalColors(int total) {
	   originalModelColors = new int[total];	   
	   modifiedModelColors = new int[total];
	}
	public void colors(int id, int original, int modified) {
		originalModelColors[id] = original;
		modifiedModelColors[id] = modified;
	}
	public void itemData(String n, String d) {
		name = n;
		description = d.getBytes();
	}
	public void models(int mID, int mE, int fE, int mE2, int fE2) {
		modelID = mID;
		maleEquip1 = mE;
		femaleEquip1 = fE;
		maleEquip2 = mE2;
		femaleEquip2 = fE2;
	}
	public void modelData(int mZ, int mR1, int mR2, int mO1, int mO2) {
		modelZoom = mZ;
		modelRotation1 = mR1;
		modelRotation2 = mR2;
		modelOffset1 = mO1;
		modelOffset2 = mO2;
	}

	public void toNote() {
		ItemDef itemDef = forID(certTemplateID);
		modelID = itemDef.modelID;
		modelZoom = itemDef.modelZoom;
		modelRotation1 = itemDef.modelRotation1;
		modelRotation2 = itemDef.modelRotation2;
		anInt204 = itemDef.anInt204;
		modelOffset1 = itemDef.modelOffset1;
		modelOffset2 = itemDef.modelOffset2;
		originalModelColors = itemDef.originalModelColors;
		modifiedModelColors = itemDef.modifiedModelColors;
		ItemDef itemDef_1 = forID(certID);
		name = itemDef_1.name;
		membersObject = itemDef_1.membersObject;
		value = itemDef_1.value;
		String s = "a";
		char c = itemDef_1.name.charAt(0);
		if(c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U')
			s = "an";
		description = ("Swap this note at any bank for " + s + " " + itemDef_1.name + ".").getBytes();
		stackable = true;
	}

	public static Sprite getSprite(int i, int j, int k) {
		if(k == 0) {
			Sprite sprite = (Sprite) mruNodes1.insertFromCache(i);
			if(sprite != null && sprite.anInt1445 != j && sprite.anInt1445 != -1) {
				sprite.unlink();
				sprite = null;
			}
			if(sprite != null)
				return sprite;
		}
		ItemDef itemDef = forID(i);
		if(itemDef.stackIDs == null)
			j = -1;
		if(j > 1) {
			int i1 = -1;
			for(int j1 = 0; j1 < 10; j1++)
				if(j >= itemDef.stackAmounts[j1] && itemDef.stackAmounts[j1] != 0)
					i1 = itemDef.stackIDs[j1];
			if(i1 != -1)
				itemDef = forID(i1);
		}
		Model model = itemDef.method201(1);
		if(model == null)
			return null;
		Sprite sprite = null;
		if(itemDef.certTemplateID != -1) {
			sprite = getSprite(itemDef.certID, 10, -1);
			if(sprite == null)
				return null;
		}
		Sprite sprite2 = new Sprite(32, 32);
		int k1 = Texture.textureInt1;
		int l1 = Texture.textureInt2;
		int ai[] = Texture.anIntArray1472;
		int ai1[] = DrawingArea.pixels;
		int i2 = DrawingArea.width;
		int j2 = DrawingArea.height;
		int k2 = DrawingArea.topX;
		int l2 = DrawingArea.bottomX;
		int i3 = DrawingArea.topY;
		int j3 = DrawingArea.bottomY;
		Texture.aBoolean1464 = false;
		DrawingArea.initDrawingArea(32, 32, sprite2.myPixels);
		DrawingArea.drawPixels(32, 0, 0, 0, 32);
		Texture.method364();
		int k3 = itemDef.modelZoom;
		if(k == -1)
			k3 = (int)((double)k3 * 1.5D);
		if(k > 0)
			k3 = (int)((double)k3 * 1.04D);
		int l3 = Texture.anIntArray1470[itemDef.modelRotation1] * k3 >> 16;
		int i4 = Texture.anIntArray1471[itemDef.modelRotation1] * k3 >> 16;
		model.method482(itemDef.modelRotation2, itemDef.anInt204, itemDef.modelRotation1, itemDef.modelOffset1, l3 + model.modelHeight / 2 + itemDef.modelOffset2, i4 + itemDef.modelOffset2);
		for(int i5 = 31; i5 >= 0; i5--) {
			for(int j4 = 31; j4 >= 0; j4--)
				if(sprite2.myPixels[i5 + j4 * 32] == 0)
					if(i5 > 0 && sprite2.myPixels[(i5 - 1) + j4 * 32] > 1)
						sprite2.myPixels[i5 + j4 * 32] = 1;
					else if(j4 > 0 && sprite2.myPixels[i5 + (j4 - 1) * 32] > 1)
						sprite2.myPixels[i5 + j4 * 32] = 1;
					else if(i5 < 31 && sprite2.myPixels[i5 + 1 + j4 * 32] > 1)
						sprite2.myPixels[i5 + j4 * 32] = 1;
					else if(j4 < 31 && sprite2.myPixels[i5 + (j4 + 1) * 32] > 1)
						sprite2.myPixels[i5 + j4 * 32] = 1;
		}
		if(k > 0) {
			for(int j5 = 31; j5 >= 0; j5--) {
				for(int k4 = 31; k4 >= 0; k4--)
					if(sprite2.myPixels[j5 + k4 * 32] == 0)
						if(j5 > 0 && sprite2.myPixels[(j5 - 1) + k4 * 32] == 1)
							sprite2.myPixels[j5 + k4 * 32] = k;
						else if(k4 > 0 && sprite2.myPixels[j5 + (k4 - 1) * 32] == 1)
							sprite2.myPixels[j5 + k4 * 32] = k;
						else if(j5 < 31 && sprite2.myPixels[j5 + 1 + k4 * 32] == 1)
							sprite2.myPixels[j5 + k4 * 32] = k;
						else if(k4 < 31 && sprite2.myPixels[j5 + (k4 + 1) * 32] == 1)
							sprite2.myPixels[j5 + k4 * 32] = k;
			}
		} else if(k == 0) {
			for(int k5 = 31; k5 >= 0; k5--) {
				for(int l4 = 31; l4 >= 0; l4--)
					if(sprite2.myPixels[k5 + l4 * 32] == 0 && k5 > 0 && l4 > 0 && sprite2.myPixels[(k5 - 1) + (l4 - 1) * 32] > 0)
						sprite2.myPixels[k5 + l4 * 32] = 0x302020;
			}
		}
		if(itemDef.certTemplateID != -1) {
			int l5 = sprite.anInt1444;
			int j6 = sprite.anInt1445;
			sprite.anInt1444 = 32;
			sprite.anInt1445 = 32;
			sprite.drawSprite(0, 0);
			sprite.anInt1444 = l5;
			sprite.anInt1445 = j6;
		}
		if(k == 0)
			mruNodes1.removeFromCache(sprite2, i);
		DrawingArea.initDrawingArea(j2, i2, ai1);
		DrawingArea.setDrawingArea(j3, k2, l2, i3);
		Texture.textureInt1 = k1;
		Texture.textureInt2 = l1;
		Texture.anIntArray1472 = ai;
		Texture.aBoolean1464 = true;
		if(itemDef.stackable)
			sprite2.anInt1444 = 33;
		else
			sprite2.anInt1444 = 32;
		sprite2.anInt1445 = j;
		return sprite2;
	}

	public Model method201(int i) {
		if(stackIDs != null && i > 1) {
			int j = -1;
			for(int k = 0; k < 10; k++)
				if(i >= stackAmounts[k] && stackAmounts[k] != 0)
					j = stackIDs[k];
			if(j != -1)
				return forID(j).method201(1);
		}
		Model model = (Model) mruNodes2.insertFromCache(id);
		if(model != null)
			return model;
		model = Model.method462(modelID);
		if(model == null)
			return null;
		if(anInt167 != 128 || anInt192 != 128 || anInt191 != 128)
			model.method478(anInt167, anInt191, anInt192);
		if (originalModelColors != null) {
			for (int l = 0; l < originalModelColors.length; l++)
				model.method476(originalModelColors[l], modifiedModelColors[l]);

		}
		model.method479(64 + anInt196, 768 + anInt184, -50, -10, -50, true);
		model.aBoolean1659 = true;
		mruNodes2.removeFromCache(model, id);
		return model;
	}

	public Model method202(int i) {
		if(stackIDs != null && i > 1) {
			int j = -1;
			for(int k = 0; k < 10; k++)
				if(i >= stackAmounts[k] && stackAmounts[k] != 0)
					j = stackIDs[k];
			if(j != -1)
				return forID(j).method202(1);
		}
		Model model = Model.method462(modelID);
		if(model == null)
			return null;
		if (originalModelColors != null) {
			for (int l = 0; l < originalModelColors.length; l++)
				model.method476(originalModelColors[l], modifiedModelColors[l]);

		}
		return model;
	}
	
	public static void writeConfig()
	{
		try {
			final DataOutputStream dat = new DataOutputStream(
					new FileOutputStream("obj.dat"));
			final DataOutputStream idx = new DataOutputStream(
					new FileOutputStream("obj.idx"));
			idx.writeShort(totalItems);
			dat.writeShort(totalItems);
			for (int i = 0; i < totalItems; i++) {
				final ItemDef item = ItemDef.forID(i);
				final int offset1 = dat.size();
				if (item.modelID != 0) {
					dat.writeByte(1);
					dat.writeShort(item.modelID);
				}
				if (item.name != null) {
					dat.writeByte(2);
					writeString(dat, item.name);
				}
				if(item.description != null)
				{
					dat.writeByte(3);
					writeString(dat, new String(item.description));
				}
				if (item.modelZoom != 2000) {
					dat.writeByte(4);
					dat.writeShort(item.modelZoom);
				}
				if (item.modelRotation1 != 0) {
					dat.writeByte(5);
					dat.writeShort(item.modelRotation1);
				}
				if (item.modelRotation2 != 0) {
					dat.writeByte(6);
					dat.writeShort(item.modelRotation2);
				}
				if (item.modelOffset1 != 0) {
					dat.writeByte(7);
					dat.writeShort(item.modelOffset1);
				}
				if (item.modelOffset2 != 0) {
					dat.writeByte(8);
					dat.writeShort(item.modelOffset2);
				}
				if (item.stackable) {
					dat.writeByte(11);
				}
				if (item.value != 1) {
					dat.writeByte(12);
					dat.writeInt(item.value);
				}
				if (item.membersObject) {
					dat.writeByte(16);
				}
				if (item.maleEquip1 != -1) {
					dat.writeByte(23);
					dat.writeShort(item.maleEquip1);
					dat.writeByte(0);
				}
				if (item.maleEquip2 != -1) {
					dat.writeByte(24);
					dat.writeShort(item.maleEquip2);
				}
				if (item.femaleEquip1 != -1) {
					dat.writeByte(25);
					dat.writeShort(item.femaleEquip1);
					dat.writeByte(0);
				}
				if (item.femaleEquip2 != -1) {
					dat.writeByte(26);
					dat.writeShort(item.femaleEquip2);
				}
				if (item.groundActions != null) {
					for (int ii = 0; ii < item.groundActions.length; ii++) {
						if (item.groundActions[ii] == null) {
							continue;
						}
						dat.writeByte(30 + ii);
						writeString(dat, item.groundActions[ii]);
					}
				}
				if (item.itemActions != null) {
					for (int z = 0; z < item.itemActions.length; z++) {
						if (item.itemActions[z] == null) {
							continue;
						}
						dat.writeByte(35 + z);
						writeString(dat, item.itemActions[z]);
					}
				}
				if (item.originalModelColors != null) {
					dat.writeByte(40);
					dat.writeByte(item.originalModelColors.length);
					for (int ii = 0; ii < item.originalModelColors.length; ii++) {
						if(item.originalModelColors != null)
						dat.writeShort(item.originalModelColors[ii]);
						if( item.modifiedModelColors != null)
						dat.writeShort(item.modifiedModelColors[ii]);
					}
				}
				if (item.anInt185 != -1) {
					dat.writeByte(78);
					dat.writeShort(item.anInt185);
				}
				if (item.anInt162 != -1) {
					dat.writeByte(79);
					dat.writeShort(item.anInt162);
				}
				if (item.anInt175 != -1) {
					dat.writeByte(90);
					dat.writeShort(item.anInt175);
				}
				if (item.anInt197 != -1) {
					dat.writeByte(91);
					dat.writeShort(item.anInt197);
				}
				if (item.anInt166 != -1) {
					dat.writeByte(92);
					dat.writeShort(item.anInt166);
				}
				if (item.anInt173 != -1) {
					dat.writeByte(93);
					dat.writeShort(item.anInt173);
				}
				if (item.anInt204 != 0) {
					dat.writeByte(95);
					dat.writeShort(item.anInt204);
				}
				if (item.certID != -1) {
					dat.writeByte(97);
					dat.writeShort(item.certID);
				}
				if (item.certTemplateID != -1) {
					dat.writeByte(98);
					dat.writeShort(item.certTemplateID);
				}
				if (item.stackIDs != null) {
					for (int ii = 0; ii < item.stackIDs.length; ii++) {
						dat.writeByte(100 + ii);
						dat.writeShort(item.stackIDs[ii]);
						dat.writeShort(item.stackAmounts[ii]);
					}
				}
				if (item.anInt167 != 128) {
					dat.writeByte(110);
					dat.writeShort(item.anInt167);
				}
				if (item.anInt192 != 128) {
					dat.writeByte(111);
					dat.writeShort(item.anInt192);
				}
				if (item.anInt191 != 128) {
					dat.writeByte(112);
					dat.writeShort(item.anInt191);
				}
				if (item.anInt196 != 0) {
					dat.writeByte(113);
					dat.writeByte(item.anInt196);
				}
				if (item.anInt184 != 0) {
					dat.writeByte(114);
					dat.writeByte(item.anInt184 / 5);
				}
				if (item.team != 0) {
					dat.writeByte(115);
					dat.writeByte(item.team);
				}
				dat.writeByte(0);
				final int offset2 = dat.size();
				final int writeOffset = offset2 - offset1;
				idx.writeShort(writeOffset);
			}
			dat.close();
			idx.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeString(final DataOutputStream dos,
			final String input) throws IOException {
		dos.write(input.getBytes());
		dos.writeByte(10);
	}
	
	public void readValues(Stream stream) {
		do {
			int i = stream.readUnsignedByte();
			if(i == 0)
				return;
			if(i == 1)
				modelID = stream.readUnsignedWord();
			else if(i == 2)
				name = stream.readString();
			else if(i == 3)
				description = stream.readBytes();
			else if(i == 4)
				modelZoom = stream.readUnsignedWord();
			else if(i == 5)
				modelRotation1 = stream.readUnsignedWord();
			else if(i == 6)
				modelRotation2 = stream.readUnsignedWord();
			else if(i == 7) {
				modelOffset1 = stream.readUnsignedWord();
				if(modelOffset1 > 32767)
					modelOffset1 -= 0x10000;
			} else if(i == 8) {
				modelOffset2 = stream.readUnsignedWord();
				if(modelOffset2 > 32767)
					modelOffset2 -= 0x10000;
			} else if(i == 10)
				stream.readUnsignedWord();
			else if(i == 11)
				stackable = true;
			else if(i == 12)
				value = stream.readDWord();
			else if(i == 16)
				membersObject = true;
			else if(i == 23) {
				maleEquip1 = stream.readUnsignedWord();
				aByte205 = stream.readSignedByte();
			} else if(i == 24)
				maleEquip2 = stream.readUnsignedWord();
			else if(i == 25) {
				femaleEquip1 = stream.readUnsignedWord();
				aByte154 = stream.readSignedByte();
			} else if(i == 26)
				femaleEquip2 = stream.readUnsignedWord();
			else if(i >= 30 && i < 35) {
				if(groundActions == null)
					groundActions = new String[5];
				groundActions[i - 30] = stream.readString();
				if(groundActions[i - 30].equalsIgnoreCase("hidden"))
					groundActions[i - 30] = null;
			} else if(i >= 35 && i < 40) {
				if(itemActions == null)
					itemActions = new String[5];
				itemActions[i - 35] = stream.readString();
			} else if(i == 40) {
				int j = stream.readUnsignedByte();
				originalModelColors = new int[j];
				modifiedModelColors = new int[j];
				for(int k = 0; k < j; k++) {
					originalModelColors[k] = stream.readUnsignedWord();
					modifiedModelColors[k] = stream.readUnsignedWord();
				}
			} else if(i == 78)
				anInt185 = stream.readUnsignedWord();
			else if(i == 79)
				anInt162 = stream.readUnsignedWord();
			else if(i == 90)
				anInt175 = stream.readUnsignedWord();
			else if(i == 91)
				anInt197 = stream.readUnsignedWord();
			else if(i == 92)
				anInt166 = stream.readUnsignedWord();
			else if(i == 93)
				anInt173 = stream.readUnsignedWord();
			else if(i == 95)
				anInt204 = stream.readUnsignedWord();
			else if(i == 97)
				certID = stream.readUnsignedWord();
			else if(i == 98)
				certTemplateID = stream.readUnsignedWord();
			else if(i >= 100 && i < 110) {
				if(stackIDs == null) {
					stackIDs = new int[10];
					stackAmounts = new int[10];
				}
				stackIDs[i - 100] = stream.readUnsignedWord();
				stackAmounts[i - 100] = stream.readUnsignedWord();
			} else if(i == 110)
				anInt167 = stream.readUnsignedWord();
			else if(i == 111)
				anInt192 = stream.readUnsignedWord();
			else if(i == 112)
				anInt191 = stream.readUnsignedWord();
			else if(i == 113)
				anInt196 = stream.readSignedByte();
			else if(i == 114)
				anInt184 = stream.readSignedByte() * 5;
			else if(i == 115)
				team = stream.readUnsignedByte();
		} while(true);
	}

	public ItemDef() {
		id = -1;
	}

	public byte aByte154;
	public int value;
	public int[] modifiedModelColors;
	public int id;
	static MRUNodes mruNodes1 = new MRUNodes(100);
	public static MRUNodes mruNodes2 = new MRUNodes(50);
	public int[] originalModelColors;
	public boolean membersObject;
	public int anInt162;
	public int certTemplateID;
	public int femaleEquip2;
	public int maleEquip1;
	public int anInt166;
	public int anInt167;
	public String groundActions[];
	public int modelOffset1;
	public String name;
	public static ItemDef[] cache;
	public int anInt173;
	public int modelID;
	public int anInt175;
	public boolean stackable;
	public byte description[];
	public int certID;
	public static int cacheIndex;
	public int modelZoom;
	public static boolean isMembers = true;
	public static Stream stream;
	public int anInt184;
	public int anInt185;
	public int maleEquip2;
	public String itemActions[];
	public int modelRotation1;
	public int anInt191;
	public int anInt192;
	public int[] stackIDs;
	public int modelOffset2;
	public static int[] streamIndices;
	public int anInt196;
	public int anInt197;
	public int modelRotation2;
	public int femaleEquip1;
	public int[] stackAmounts;
	public int team;
	public static int totalItems;
	public int anInt204;
	public byte aByte205;
	public int anInt164;
	public int anInt199;
	public int anInt188;
}