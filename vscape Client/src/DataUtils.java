import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.zip.CRC32;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class DataUtils {

	public static void writeFile(File f, byte[] data) {
		try {
			RandomAccessFile raf = new RandomAccessFile(f, "rw");
			try {
				raf.write(data);
			} finally {
				raf.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeFile(byte[] data, String fileName) throws IOException {
		OutputStream out = new FileOutputStream(fileName);
		out.write(data);
		out.close();
	}

	public static int readJAGHash(String string) {
		int id = 0;
		string = string.toUpperCase();
		for (int j = 0; j < string.length(); j++)
			id = (id * 61 + string.charAt(j)) - 32;
		return id;
	}

	public static int getCRCFromData(byte[] data) {
		CRC32 crc = new CRC32();
		crc.update(data);
		return (int) crc.getValue();
	}

	public static byte[] gZipDecompress(byte[] b) throws IOException {
		GZIPInputStream gzi = new GZIPInputStream(new ByteArrayInputStream(b));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len;
		try {
			while ((len = gzi.read(buf, 0, buf.length)) > 0) {
				out.write(buf, 0, len);
			}
		} finally {
			out.close();
		}
		return out.toByteArray();
	}

	public static byte[] gzDecompress(byte[] b) throws IOException {
		GZIPInputStream gzi = new GZIPInputStream(new ByteArrayInputStream(b));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len;
		while ((len = gzi.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		out.close();
		return out.toByteArray();
	}

	static byte[] unzip(byte[] data) throws IOException {
		InputStream in = new ByteArrayInputStream(data);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			in = new GZIPInputStream(in);
			byte[] buffer = new byte[65536];
			int noRead;
			while ((noRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, noRead);
			}
		} finally {
			try {
				out.close();
			} catch (Exception e) {
			}
		}
		return out.toByteArray();
	}

	public static byte[] readFile(String name) {
		try {
			RandomAccessFile raf = new RandomAccessFile(name, "r");
			ByteBuffer buf = raf.getChannel().map(
					FileChannel.MapMode.READ_ONLY, 0, raf.length());
			try {
				if (buf.hasArray()) {
					return buf.array();
				} else {
					byte[] array = new byte[buf.remaining()];
					buf.get(array);
					return array;
				}
			} finally {
				raf.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] gZipCompress(byte[] data, int off, int len)
			throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		GZIPOutputStream gzo = new GZIPOutputStream(bos);
		try {
			gzo.write(data, off, len);
		} finally {
			gzo.close();
			bos.close();
		}
		return bos.toByteArray();
	}
}
