package wkwk.discord.dao;

import wkwk.discord.dao.core.DAOBase;
import wkwk.discord.parameter.DAOParameters;
import wkwk.discord.parameter.DeleteTimesParameters;
import wkwk.discord.record.DeleteMessageRecord;
import wkwk.discord.record.DeleteTimeRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class AutoDeleteRegisterDAO extends DAOBase {
    // madeW
    public boolean CheckIfDeleteChannel(long channelId) {
        this.open();
        PreparedStatement preStatement = null;
        boolean isCheck = false;
        try {
            String sql = "SELECT EXISTS(SELECT " + DeleteTimesParameters.SERVER_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_DELETE_TIMES.getParam() + " WHERE "
                    + DeleteTimesParameters.TEXT_CHANNEL_ID.getParam() + " = ?) AS delete_check";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, channelId);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                isCheck = rs.getBoolean("delete_check");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return isCheck;
    }
    public DeleteTimeRecord getDeleteTime(long textChanelId) {
        this.open();
        PreparedStatement preStatement = null;
        DeleteTimeRecord record =  new DeleteTimeRecord();
        try {
            String sql = "SELECT *"
                    + " FROM " + DAOParameters.TABLE_DELETE_TIMES.getParam()
                    + " WHERE " + DeleteTimesParameters.TEXT_CHANNEL_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, textChanelId);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                record.setTextChannelId(rs.getLong(DeleteTimesParameters.TEXT_CHANNEL_ID.getParam()));
                record.setTime(rs.getLong(DeleteTimesParameters.DELETE_TIME.getParam()));
                record.setUnit(rs.getString(DeleteTimesParameters.TIME_UNIT.getParam()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return record;
    }

    public void addDeleteMessage(DeleteMessageRecord deleteMessageRecord) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "INSERT INTO " + DAOParameters.TABLE_DELETE_MESSAGES.getParam() + " VALUES (?,?,?,?,?)";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, deleteMessageRecord.getServerId());
            preStatement.setLong(2, deleteMessageRecord.getMessageId());
            preStatement.setString(3, deleteMessageRecord.getMessageLink());
            preStatement.setString(4, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(deleteMessageRecord.getDeleteDate()));
            preStatement.setLong(5, deleteMessageRecord.getTextChannelId());
            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }
}
