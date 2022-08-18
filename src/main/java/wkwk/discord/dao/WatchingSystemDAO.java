package wkwk.discord.dao;

import wkwk.discord.dao.core.DAOBase;
import wkwk.discord.parameter.DAOParameters;
import wkwk.discord.parameter.ServerPropertyParameters;
import wkwk.discord.parameter.WatchingParameters;
import wkwk.discord.record.WatchingRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WatchingSystemDAO extends DAOBase {
    // madeW
    public long getFirstChannelId(long serverId) {
        this.open();
        PreparedStatement preStatement = null;
        long FirstId = -1;
        try {
            String sql = "SELECT "
                    + ServerPropertyParameters.FIRST_CHANNEL_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_SERVER_PROPERTY.getParam()
                    + " WHERE " + ServerPropertyParameters.SERVER_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, serverId);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                FirstId =  rs.getLong(ServerPropertyParameters.FIRST_CHANNEL_ID.getParam());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return FirstId;
    }

    public WatchingRecord getWatchingSetting() {
        this.open();
        PreparedStatement preStatement = null;
        WatchingRecord watchingRecord = null;
        try {
            String sql = "SELECT * FROM " + DAOParameters.TABLE_WATCHING.getParam();
            preStatement = con.prepareStatement(sql);
            ResultSet rs = preStatement.executeQuery();
            watchingRecord = new WatchingRecord();
            while (rs.next()) {
                watchingRecord.setServerId(rs.getLong(WatchingParameters.SERVER_ID.getParam()));
                watchingRecord.setCreateWId(rs.getLong(WatchingParameters.TEMP_CREATE_ID.getParam()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return watchingRecord;
    }
}
