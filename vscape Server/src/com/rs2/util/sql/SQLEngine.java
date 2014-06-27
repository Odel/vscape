package com.rs2.util.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main SQL engine.
 * 
 * @author Blake Beaupain
 */
public class SQLEngine {
	
	/** The lookup player query. */
	public static final String LOOKUP_PLAYER = "SELECT password,password_salt,disabled,members,id,rights,server_rights FROM `users` WHERE username = ?";
    public static final String UPDATE_HIGH_SCORES = "REPLACE INTO `hiscores` (`userid`, `playerRights`, `LVL`, `XP`, `0`, `1`, `2`, `3`, `4`, `5`, `6`, `7`, `8`, `9`, `10`, `11`, `12`, `13`, `14`, `15`, `16`, `17`, `18`, `19`, `20`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    public static final String UPDATE_PLAYER_COUNT = "REPLACE INTO `worlds` (`id`, `players`) VALUES (?, ?);";

	/**
	 * The lookup trusted players query. public static final String
	 * LOOKUP_TRUSTED = "SELECT id FROM trusted_users WHERE user_id = ?"";
	 * 
	 * /** The instance.
	 */
	private static SQLEngine gameDatabase;
    
    private static SQLEngine forumDatabase;

	/** The SQL thread pool. */
	private ExecutorService threadPool;

	/** The thread local. */
	private ThreadLocal<SQLWorker.ThreadLocalFields> threadLocal;

	/** The database URL. */
	private final String url;

	/** The database username. */
	private final String username;

	/** The database password. */
	private final String password;

	/**
	 * Instantiates a new {@code SQLEngine}.
	 * 
	 * @param connections
	 *            The maximum number of connections.
	 * @param driver
	 *            The database driver string
	 * @param url
	 *            The database URL
	 * @param username
	 *            The database username
	 * @param password
	 *            The database password
	 * @throws ClassNotFoundException
	 *             If the driver could not be loaded
	 */
	public SQLEngine(final int connections, final String driver, final String url, final String username, final String password) throws ClassNotFoundException {
		Class.forName(driver);
		threadPool = Executors.newFixedThreadPool(connections);
		threadLocal = new ThreadLocal<SQLWorker.ThreadLocalFields>();
		this.url = url;
		this.username = username;
		this.password = password;
	}

	/**
	 * Executes an SQL task.
	 * 
	 * @param worker
	 *            The SQL worker
	 * @param completionHandler
	 *            The SQL completion handler
	 */
	public void execute(SQLWorker worker, SQLCompletionHandler completionHandler) {
		worker.setLocalFields(threadLocal);
		worker.setCompletionHandler(completionHandler);
        worker.setSQLEngine(this);
		threadPool.execute(worker);
	}

	/**
	 * Attempts to open a connection.
	 * 
	 * @return The connection
	 * @throws SQLException
	 *             If the connection could not be opened
	 */
	protected Connection openConnection() throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}

	/**
     * Gets the instance.
     *
     * @return The instance
     */
    public static SQLEngine getGameDatabase() {
        return gameDatabase;
    }

    /**
     * Sets the instance.
     *
     * @param gameDatabase
     *            The instance
     */
    public static void setGameDatabase(SQLEngine gameDatabase) {
        SQLEngine.gameDatabase = gameDatabase;
    }

    /**
     * Gets the instance.
     *
     * @return The instance
     */
    public static SQLEngine getForumDatabase() {
        return forumDatabase;
    }

    /**
     * Sets the instance.
     *
     * @param forumDatabase
     *            The instance
     */
    public static void setForumDatabase(SQLEngine forumDatabase) {
        SQLEngine.forumDatabase = forumDatabase;
    }

}
