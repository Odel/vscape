package com.rs2.util.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An interface for handling completed SQL tasks via asynchronous callback.
 * 
 * @author Blake Beaupain
 */
public interface SQLCompletionHandler {

	/**
	 * Called when the SQL work completes.
	 * 
	 * @param results
	 *            The result set
	 * @throws SQLException
	 */
	public void onComplete(ResultSet results) throws SQLException;

	/**
	 * Called when an exception occurs during SQL execution.
	 * 
	 * @param ex
	 *            The exception
	 */
	public void onException(Exception ex);

}
