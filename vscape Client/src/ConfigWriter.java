
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigWriter {
	
	public static String conDir =  "Configs/";

	public static void writeItemConfig()
	{
		try {
			final DataOutputStream dat = new DataOutputStream(new FileOutputStream(conDir+"obj.dat"));
			final DataOutputStream idx = new DataOutputStream(new FileOutputStream(conDir+"obj.idx"));
			idx.writeShort(ItemDef.totalItems);
			dat.writeShort(ItemDef.totalItems);
			for (int i = 0; i < ItemDef.totalItems; i++) {
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
	
    public static void writeString(DataOutputStream dos, String input) throws IOException {
        dos.write(input.getBytes());
        dos.writeByte(10);
    }

    public static void writeDWordBigEndian(DataOutputStream dat, int i) throws IOException {
        dat.write((byte) (i >> 16));
        dat.write((byte) (i >> 8));
        dat.write((byte) (i >> 8));
    }
}