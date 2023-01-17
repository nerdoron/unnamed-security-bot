package me.nerdoron.security.modules.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.nerdoron.security.modules._bot.database.Database;

public class SettingsSQL {
    static Connection connection = Database.connect();
    static final Logger logger = LoggerFactory.getLogger(SettingsSQL.class);

    // check if guild is setup
    public boolean isGuildSetup(String guildId) {
        String sql = "SELECT gid FROM guilds where gid=?";
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

    // init setup of guild
    public void setupGuild(String guildId, String channelId, String roleId, Boolean pingModRole, Boolean autoScan) {
        String sql = "INSERT INTO guilds(gid,flagsChannel,modRoleId,pingModsOnFlag,autoScan) VALUES(?,?,?,?,?)";
        int pingValue;
        int autoScanValue;
        if (pingModRole) {
            pingValue = 1;
        } else {
            pingValue = 0;
        }
        if (autoScan) {
            autoScanValue = 1;
        } else {
            autoScanValue = 0;
        }

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, guildId);
            ps.setString(2, channelId);
            ps.setString(3, roleId);
            ps.setInt(4, pingValue);
            ps.setInt(5, autoScanValue);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            logger.error(ex.toString());
        }
    }

    // update the alerts channel
    public void updateAlertsChannel(String channelId, String guildId) {
        String sql = "UPDATE guilds SET flagsChannel = ? WHERE gid = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, channelId);
            ps.setString(2, guildId);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            logger.error(ex.toString());
        }

    }

    // update the modeartor role
    public void updateModRole(String roleId, String guildId) {
        String sql = "UPDATE guilds SET modRoleId = ? WHERE gid = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, roleId);
            ps.setString(2, guildId);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            logger.error(ex.toString());
        }
    }

    // update whether to ping mods on alert or not
    public void updatePing(Boolean pingModRoles, String guildId) {
        String sql = "UPDATE guilds SET pingModsOnFlag = ? WHERE gid = ?";
        int pingValue;
        if (pingModRoles) {
            pingValue = 1;
        } else {
            pingValue = 0;
        }
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, pingValue);
            ps.setString(2, guildId);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            logger.error(ex.toString());
        }
    }

    // update whether to autoscan or not
    public void updateAutoScan(Boolean autoScan, String guildId) {
        String sql = "UPDATE guilds SET autoScan = ? WHERE gid = ?";
        int autoScanValue;
        if (autoScan) {
            autoScanValue = 1;
        } else {
            autoScanValue = 0;
        }
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, autoScanValue);
            ps.setString(2, guildId);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            logger.error(ex.toString());
        }
    }

    public String getflagsChannelId(String guildId) {
        String sql = "SELECT flagsChannel FROM guilds WHERE gid = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, guildId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String channelId = rs.getString(1);
                rs.close();
                ps.close();
                return channelId;
            } else {
                ps.close();
                rs.close();
                return "Error";
            }
        } catch (SQLException ex) {
            logger.error(ex.toString());
            return "Error!";
        }
    }

    public String getRoleId(String guildId) {
        String sql = "SELECT modRoleId FROM guilds WHERE gid = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, guildId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String roleId = rs.getString(1);
                rs.close();
                ps.close();
                return roleId;
            } else {
                ps.close();
                rs.close();
                return "Error";
            }
        } catch (SQLException ex) {
            logger.error(ex.toString());
            return "Error!";
        }
    }

    public String getPingMods(String guildId) {
        String sql = "SELECT pingModsOnFlag FROM guilds WHERE gid = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, guildId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int pingMods = rs.getInt(1);
                rs.close();
                ps.close();
                if (pingMods == 0) {
                    return "False";
                } else {
                    return "True";
                }
            } else {
                ps.close();
                rs.close();
                return "Error";
            }
        } catch (SQLException ex) {
            logger.error(ex.toString());
            return "Error!";
        }
    }

    public String getAutoScan(String guildId) {
        String sql = "SELECT autoScan FROM guilds WHERE gid = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, guildId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int pingMods = rs.getInt(1);
                rs.close();
                ps.close();
                if (pingMods == 0) {
                    return "False";
                } else {
                    return "True";
                }
            } else {
                ps.close();
                rs.close();
                return "Error";
            }
        } catch (SQLException ex) {
            logger.error(ex.toString());
            return "Error!";
        }
    }

}
