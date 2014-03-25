package com.rs2.cache;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel.MapMode;

import com.rs2.cache.index.IndexTable;

/**
 * Manages the game cache.
 * 
 * @author Graham Edgecombe
 * 
 */
public class Cache implements Closeable {

    private static Cache singleton;

    public static void load() {
        try {
            singleton = new Cache(new File("./data/cache/"));
        } catch (InvalidCacheException e) {
            e.printStackTrace();
        }
    }

    public static Cache getSingleton() {
        return singleton;
    }
	/**
	 * The size of a block in the index file.
	 */
	public static final int INDEX_SIZE = 6;

	/**
	 * The size of a data block in the data file.
	 */
	public static final int DATA_BLOCK_SIZE = 512;

	/**
	 * The size of a header block in the data file.
	 */
	public static final int DATA_HEADER_SIZE = 8;

	/**
	 * The overall size of a block in the data file.
	 */
	public static final int DATA_SIZE = DATA_BLOCK_SIZE + DATA_HEADER_SIZE;

	/**
	 * The data random access file.
	 */
	private final RandomAccessFile dataFile;

	/**
	 * The index random access files.
	 */
	private final RandomAccessFile[] indexFiles;

	/**
	 * The index table.
	 */
	private final IndexTable indexTable;

	/**
	 * Creates the cache.
	 * 
	 * @param directory
	 *            The directory where the cache is stored.
	 * @throws com.rs2.cache.InvalidCacheException
	 *             if the cache is corrupt or invalid.
	 */
	public Cache(File directory) throws InvalidCacheException {
		try {
			int count = 0;
			for (int i = 0; i < 255; i++) {
				File indexFile = new File(directory.getAbsolutePath() + "/main_file_cache.idx" + i);
				if (!indexFile.exists()) {
					break;
				} else {
					count++;
				}
			}
			if (count == 0) {
				throw new InvalidCacheException("No index files present.");
			}
			indexFiles = new RandomAccessFile[count];
			dataFile = new RandomAccessFile(directory.getAbsolutePath() + "/main_file_cache.dat", "r");
			for (int i = 0; i < indexFiles.length; i++) {
				indexFiles[i] = new RandomAccessFile(directory.getAbsolutePath() + "/main_file_cache.idx" + i, "r");
			}
			indexTable = new IndexTable(this);
		} catch (FileNotFoundException ex) {
			throw new InvalidCacheException(ex);
		} catch (IOException ex) {
			throw new InvalidCacheException(ex);
		}
	}

	/**
	 * Gets the index table.
	 * 
	 * @return The index table.
	 */
	public IndexTable getIndexTable() {
		return indexTable;
	}

	/**
	 * Gets a file from the cache.
	 * 
	 * @param cache
	 *            The cache id.
	 * @param file
	 *            The file id.
	 * @return The file.
	 * @throws java.io.IOException
	 *             if an I/O error occurs.
	 */
	public CacheFile getFile(int cache, int file) throws IOException {
		if (cache < 0 || cache >= indexFiles.length) {
			throw new IOException("Cache does not exist.");
		}

		RandomAccessFile indexFile = indexFiles[cache];
		cache += 1;

		if (file < 0 || file >= indexFile.length() * INDEX_SIZE) {
			throw new IOException("File does not exist.");
		}

		ByteBuffer index = indexFile.getChannel().map(MapMode.READ_ONLY, INDEX_SIZE * file, INDEX_SIZE);
		int fileSize = (index.get() & 0xFF) << 16 | (index.get() & 0xFF) << 8 | index.get() & 0xFF;
		int fileBlock = (index.get() & 0xFF) << 16 | (index.get() & 0xFF) << 8 | index.get() & 0xFF;

		int remainingBytes = fileSize;
		int currentBlock = fileBlock;

		ByteBuffer fileBuffer = ByteBuffer.allocate(fileSize);
		int cycles = 0;

		while (remainingBytes > 0) {

			int size = DATA_SIZE;
			int rem = (int) (dataFile.length() - currentBlock * DATA_SIZE);
			if (rem < DATA_SIZE) {
				size = rem;
			}

			ByteBuffer block = dataFile.getChannel().map(MapMode.READ_ONLY, currentBlock * DATA_SIZE, size);
			int nextFileId = block.getShort() & 0xFFFF;
			int currentPartId = block.getShort() & 0xFFFF;
			int nextBlockId = (block.get() & 0xFF) << 16 | (block.get() & 0xFF) << 8 | block.get() & 0xFF;
			int nextCacheId = block.get() & 0xFF;

			size -= 8;

			int bytesThisCycle = remainingBytes;
			if (bytesThisCycle > DATA_BLOCK_SIZE) {
				bytesThisCycle = DATA_BLOCK_SIZE;
			}

			byte[] temp = new byte[bytesThisCycle];
			block.get(temp);

			fileBuffer.put(temp, 0, bytesThisCycle);

			remainingBytes -= bytesThisCycle;

			if (cycles != currentPartId) {
				throw new IOException("Cycle does not match part id.");
			}

			if (remainingBytes > 0) {
				if (nextCacheId != cache) {
					throw new IOException("Unexpected next cache id.");
				}
				if (nextFileId != file) {
					throw new IOException("Unexpected next file id.");
				}
			}

			cycles++;
			currentBlock = nextBlockId;
		}
		return new CacheFile(cache, file, (ByteBuffer) fileBuffer.flip());
	}

	/**
	 * Closes the cache.
	 * 
	 * @throws java.io.IOException
	 *             if an I/O error occurs.
	 */
	@Override
	public void close() throws IOException {
		dataFile.close();
		for (RandomAccessFile indexFile : indexFiles) {
			indexFile.close();
		}
	}

	/**
	 * Gets the number of caches.
	 * 
	 * @return The number of caches.
	 * @throws java.io.IOException
	 *             if an I/O error occurs.
	 */
	public int getCacheCount() throws IOException {
		return indexFiles.length;
	}

	/**
	 * Gets the number of files.
	 * 
	 * @param cache
	 *            The cache.
	 * @return The number of files.
	 * @throws java.io.IOException
	 *             if an I/O error occurs.
	 */
	public int getFileCount(int cache) throws IOException {
		if (cache < 0 || cache >= indexFiles.length) {
			throw new IOException("Cache does not exist.");
		}
		return (int) (indexFiles[cache].length() / INDEX_SIZE) - 1;
	}

}
