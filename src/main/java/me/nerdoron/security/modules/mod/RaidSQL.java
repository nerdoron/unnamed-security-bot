package me.nerdoron.security.modules.mod;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.nerdoron.security.modules._bot.database.Database;

public class RaidSQL {
    static Connection connection = Database.connect();
    static final Logger logger = LoggerFactory.getLogger(RaidSQL.class);

    public boolean checkIfGuildIsInTable(String guildId) {
        String sql = "SELECT gid FROM antiraid WHERE gid=?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, guildId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ps.close();
                return true;
            } else {
                ps.close();
                return false;
            }
        } catch (SQLException ex) {
            logger.error(ex.toString());
        }
        return false;
    }

    public void removeGuildFromTable(String guildId) {
        String sql = "DELETE FROM antiraid WHERE gid=?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, guildId);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            logger.error(ex.toString());
        }
    }

    public void addGuildToTable(String guildId) {
        String sql = "INSERT INTO antiraid(gid) VALUES(?)";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, guildId);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            logger.error(ex.toString());
        }
    }

}
