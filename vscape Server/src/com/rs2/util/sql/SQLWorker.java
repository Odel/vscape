package com.rs2.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * An abstract {@link Runnable} class that allows for thread pooled SQL work via
 * independent connections.
 * 
 * @author Blake Beaupain
 */
public abstract class SQLWorker implements Runnable {

	/**
	 * The database worker thread local fields. Instances of these fields can be
	 * found in the thread local object.
	 * 
	 * @author Blake Beaupain
	 */
	public class ThreadLocalFields {

		/** The thread local connection. */
		private Connection connection;

		/** The thread local prepared statements. */
		private Map<String, PreparedStatement> preparedStatements;

	}

	/** The SQL query. */
	private final String query;

	/** The thread local fields. */
	private ThreadLocal<ThreadLocalFields> localFields;

	/** The SQL completion handler. */
	private SQLCompletionHandler completionHandler;
    
    private SQLEngine engine;

	/**
	 * Instantiates a new {@code SQLWorker}.
	 * 
	 * @param query
	 *            The query to execute
	 */
	public SQLWorker(String query) {
		this.query = query;
	}

	/**
	 * Executes the SQL query.
	 * 
	 * @param c
	 *            The connection
	 * @param st
	 *            The prepared statement
	 * @throws SQLException
	 */
	public abstract ResultSet executeSQL(Connection c, PreparedStatement st) throws SQLException;

	@Override
	public void run() {
		try {
			ThreadLocalFields fields = localFields.get();
			if (fields == null) {
				fields = new ThreadLocalFields();
				localFields.set(fields);
			}

			// Obtain the connection.
			Connection c = fields.connection;
			if (c == null || c.isClosed()) {
				c = engine.openConnection();
				fields.connection = c;
			}

			// Initialize the prepared statements map.
			if (fields.preparedStatements == null) {
				fields.preparedStatements = new HashMap<String, PreparedStatement>();
			}

			// Obtain the prepared statement.
			PreparedStatement st = fields.preparedStatements.get(query);
			if (st == null) {
				st = c.prepareStatement(query);
				fields.preparedStatements.put(query, st);
			}

			// Execute the query.
			st.setQueryTimeout(5);
			ResultSet results = executeSQL(c, st);
            if (completionHandler != null)
			    completionHandler.onComplete(results);
            if (results != null)
			    results.close();
		} catch (SQLException ex) {
            if (completionHandler != null)
			    completionHandler.onException(ex);
		}
	}

	/**
	 * Gets the thread local fields.
	 * 
	 * @return The thread local fields
	 */
	protected ThreadLocal<ThreadLocalFields> getLocalFields() {
		return localFields;
	}

	/**
	 * Sets the thread local fields.
	 * 
	 * @param localFields
	 *            The thread local fields
	 */
	protected void setLocalFields(ThreadLocal<ThreadLocalFields> localFields) {
		this.localFields = localFields;
	}

    /**
     * Sets the SQL Engine
     * @param engine the engine
     */
    protected void setSQLEngine(SQLEngine engine) {
        this.engine = engine;
    }

	/**
	 * Gets the completion handler.
	 * 
	 * @return The completion handler
	 */
	protected SQLCompletionHandler getCompletionHandler() {
		return completionHandler;
	}

	/**
	 * Sets the completion handler.
	 * 
	 * @param completionHandler
	 *            The completion handler
	 */
	protected void setCompletionHandler(SQLCompletionHandler completionHandler) {
		this.completionHandler = completionHandler;
	}

}
