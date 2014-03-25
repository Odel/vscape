package com.rs2.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.rs2.Constants;
import com.rs2.model.World;
import com.rs2.model.tick.Tick;
import com.rs2.util.sql.SQLEngine;
import com.rs2.util.sql.SQLWorker;

/**
 *
 */
public class PlayerCountMonitor extends Tick {

    @SuppressWarnings("unused")
	private SQLWorker worker;
    public PlayerCountMonitor() {
        super((int)Misc.secondsToTicks(10), true);
        worker = new SQLWorker(SQLEngine.UPDATE_PLAYER_COUNT) {
            @Override
            public ResultSet executeSQL(Connection c, PreparedStatement st)
                    throws SQLException {
                st.setInt(1, Constants.WORLD_NUMBER);
                st.setInt(2, World.playerAmount());
                st.executeUpdate();
                return null;
            }
        };
    }

    @Override
    public void execute() {
        //SQLEngine.getForumDatabase().execute(worker, null);
    }
}
