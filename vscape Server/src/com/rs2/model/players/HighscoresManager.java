package com.rs2.model.players;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

import com.rs2.Constants;
import com.rs2.model.content.skills.Skill;
import com.rs2.util.Misc;
import com.rs2.util.sql.SQLEngine;
import com.rs2.util.sql.SQLWorker;

public class HighscoresManager implements Runnable {

    private Socket highscores;
    private DataOutputStream out;
    private DataInputStream in;

    private final Queue<Player> toSave = new LinkedList<Player>();
    private Misc.Stopwatch connectionTimeout = new Misc.Stopwatch();
    private boolean heardFromServer;
    Thread thread;
    public static boolean debug;
    public static boolean running;
    
    
    private static HighscoresManager singleton;
    
    /**
     * Save all players at end of cycle
     */
    public void run() {
        if (Constants.DEVELOPER_MODE || !Constants.HIGHSCORES_ENABLED)
            return;
        while (true) {

            if (!running)  {
                synchronized (toSave) {
                    toSave.clear();
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                if (in != null && in.available() > 0) {
                    if (debug)
                        System.out.println("Received Heartbeat");
                    connectionTimeout.reset();
                    in.read(new byte[in.available()]);
                    heardFromServer = true;
                } else {
                    heardFromServer = false;
                    if (debug)
                        System.out.println("Did not hear from server");
                    if (connectionTimeout.elapsed() >= 10000) {
                        synchronized (toSave) {
                            toSave.clear();
                        }
                        if (debug)
                            System.out.println("Re-establishing Highscores connection");
                        reconnect();
                        connectionTimeout.reset();
                    }
                }
            } catch (IOException e) {
                if (debug)
                    e.printStackTrace();
            }

            if (heardFromServer && out != null) {
                if (debug) {
                    System.out.println("Saving Highscores");
                }
                synchronized (toSave) {
                    try {
                        if (!toSave.isEmpty()) {
                        //Misc.Stopwatch watch = new Misc.Stopwatch();
                            out.writeInt(toSave.size());
                            while (!toSave.isEmpty()) {
                                Player player = toSave.poll();
                                //each player
                                out.writeInt(player.getUniqueId());
                                out.writeByte(player.getStaffRights());
                                for (int i = 0; i < Skill.SKILL_NAME.length; i++)
                                    out.writeInt((int) player.getSkill().getExp()[i]);
                            }
                        } else out.writeInt(0);
                        out.flush();
                    } catch (Exception e) {
                        if (debug)
                            e.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void load() { 
        singleton = new HighscoresManager();
        if (!Constants.DEVELOPER_MODE && Constants.HIGHSCORES_ENABLED) {
            running = true;
            singleton.thread = new Thread(singleton);
            singleton.thread.start();
        }
    }

    private void reconnect() {
        
        if (Constants.DEVELOPER_MODE || !Constants.HIGHSCORES_ENABLED)
            return;
        try {
            highscores = new Socket("96.125.163.248", 3306);
            if (highscores.isConnected()) {
                out = new DataOutputStream(highscores.getOutputStream());
                in = new DataInputStream(highscores.getInputStream());
            }
            else {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
                in = null;
                out = null;
            }
        } catch (Exception e) {
            if (debug)
                e.printStackTrace();
        }
    }

	/**
	 * The main method that is called upon logout
     * @param player the player
     */
	@SuppressWarnings("unused")
	public void savePlayer(final Player player) {
        if (!running)
            return;
        SQLWorker worker = new SQLWorker(SQLEngine.UPDATE_HIGH_SCORES) {
            @Override
            public ResultSet executeSQL(Connection c, PreparedStatement st)
                    throws SQLException {
                st.setInt(1, player.getUniqueId());
                st.setInt(2, player.getStaffRights());
                st.setInt(3, player.getSkill().getTotalLevel());
                st.setLong(4, player.getSkill().getTotalXp());
                st.setInt(5, (int)player.getSkill().getExp()[0]);
                st.setInt(6, (int)player.getSkill().getExp()[1]);
                st.setInt(7, (int)player.getSkill().getExp()[2]);
                st.setInt(8, (int)player.getSkill().getExp()[3]);
                st.setInt(9, (int)player.getSkill().getExp()[4]);
                st.setInt(10, (int)player.getSkill().getExp()[5]);
                st.setInt(11, (int)player.getSkill().getExp()[6]);
                st.setInt(12, (int)player.getSkill().getExp()[7]);
                st.setInt(13, (int)player.getSkill().getExp()[8]);
                st.setInt(14, (int)player.getSkill().getExp()[9]);
                st.setInt(15, (int)player.getSkill().getExp()[10]);
                st.setInt(16, (int)player.getSkill().getExp()[11]);
                st.setInt(17, (int)player.getSkill().getExp()[12]);
                st.setInt(18, (int)player.getSkill().getExp()[13]);
                st.setInt(19, (int)player.getSkill().getExp()[14]);
                st.setInt(20, (int)player.getSkill().getExp()[15]);
                st.setInt(21, (int)player.getSkill().getExp()[16]);
                st.setInt(22, (int)player.getSkill().getExp()[17]);
                st.setInt(23, (int)player.getSkill().getExp()[18]);
                st.setInt(24, (int)player.getSkill().getExp()[19]);
                st.setInt(25, (int)player.getSkill().getExp()[20]);
                st.executeUpdate();
                return null;
            }
        };

        if (!Constants.DEVELOPER_MODE && Constants.HIGHSCORES_ENABLED) {
            synchronized (toSave) {
                if (!toSave.contains(player))
                    toSave.offer(player);
            }
        }
    }

    public static HighscoresManager getSingleton() {
        return singleton;
    }


}
