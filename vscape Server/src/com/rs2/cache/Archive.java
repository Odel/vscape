package com.rs2.cache;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;


/**
 * Manages an archive file in the cache.
 * 
 * @author Graham Edgecombe
 * 
 */
public class Archive {

	/**
	 * Compressed flag.
	 */
	private boolean compressed = false;

	/**
	 * Data buffer.
	 */
	private ByteBuffer data;

	/**
	 * File map.
	 */
	private Map<Integer, ArchiveFile> namedFiles = new HashMap<Integer, ArchiveFile>();

	/**
	 * Creates the archive.
	 * 
	 * @param cf
	 *            The cache file.
	 * @throws java.io.IOException
	 *             if an I/O error occurs.
	 */
	public Archive(CacheFile cf) throws IOException {
		ByteBuffer bb = cf.getBuffer();
		bb.position(0);
		int uncompressed = (bb.get() & 0xFF) << 16 | (bb.get() & 0xFF) << 8 | bb.get() & 0xFF;
		int compressed = (bb.get() & 0xFF) << 16 | (bb.get() & 0xFF) << 8 | bb.get() & 0xFF;
		if (uncompressed != compressed) {
			byte[] data = new byte[compressed];
			bb.get(data);
			byte[] decompressed = decompress(data);
			bb = ByteBuffer.allocate(decompressed.length);
			bb.put(decompressed);
			bb.flip();
			this.compressed = true;
		}
		int dataSize = bb.getShort() & 0xFFFF;
		int off = bb.position() + dataSize * 10;
		for (int i = 0; i < dataSize; i++) {
			int nameHash = bb.getInt();
			int uncompressedSize = (bb.get() & 0xFF) << 16 | (bb.get() & 0xFF) << 8 | bb.get() & 0xFF;
			int compressedSize = (bb.get() & 0xFF) << 16 | (bb.get() & 0xFF) << 8 | bb.get() & 0xFF;
			ArchiveFile nf = new ArchiveFile(nameHash, uncompressedSize, compressedSize, off);
			namedFiles.put(nf.getHash(), nf);
			off += nf.getCompressedSize();
		}
		data = bb;
	}

	/**
	 * Hashes a file name.
	 * 
	 * @param name
	 *            The file name.
	 * @return The hash.
	 */
	public static int hash(String name) {
		int hash = 0;
		name = name.toUpperCase();
		for (int j = 0; j < name.length(); j++) {
			hash = hash * 61 + name.charAt(j) - 32;
		}
		return hash;
	}

	/**
	 * Gets a file by its name.
	 * 
	 * @param name
	 *            The file name.
	 * @return The file contents.
	 * @throws java.io.IOException
	 *             if an I/O error occurs.
	 */
	public byte[] getFile(String name) throws IOException {
		int hash = hash(name);
		ArchiveFile nf = namedFiles.get(hash);
		if (nf == null) {
			return null;
		} else {
			byte[] buf = new byte[nf.getCompressedSize()];
			data.position(nf.getOffset());
			data.get(buf);
			if (compressed) {
				return buf;
			} else {
				return decompress(buf);
			}
		}
	}

	/**
	 * Gets a file as a bytebuffer.
	 * 
	 * @param name
	 *            The file name.
	 * @return The bytebuffer.
	 * @throws java.io.IOException
	 *             if an I/O error occurs.
	 */
	public ByteBuffer getFileAsByteBuffer(String name) throws IOException {
		byte[] data = getFile(name);
		if (data == null) {
			return null;
		} else {
			ByteBuffer buf = ByteBuffer.allocate(data.length);
			buf.put(data);
			buf.flip();
			return buf;
		}
	}

	/**
	 * Decompresses a byte array using BZIP2.
	 * 
	 * @param data
	 *            The compressed bytes.
	 * @return The uncompressed bytes.
	 * @throws java.io.IOException
	 *             if an I/O error occurs.
	 */
	private byte[] decompress(byte[] data) throws IOException {
		byte[] out;
		byte[] newData = new byte[data.length + 4];
		System.arraycopy(data, 0, newData, 4, data.length);
		newData[0] = 'B';
		newData[1] = 'Z';
		newData[2] = 'h';
		newData[3] = '1';
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			InputStream is = new BZip2CompressorInputStream(new ByteArrayInputStream(newData));
			try {
				while (true) {
					byte[] buf = new byte[512];
					int read = is.read(buf, 0, buf.length);
					if (read == -1) {
						break;
					}
					os.write(buf, 0, read);
				}
			} finally {
				is.close();
			}
			os.flush();
			out = os.toByteArray();
		} finally {
			os.close();
		}
		return out;
	}

}
