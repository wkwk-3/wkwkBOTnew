package wkwk.discord.dao;

import wkwk.discord.dao.core.DAOBase;
import wkwk.discord.parameter.DAOParameters;
import wkwk.discord.parameter.DeleteMessagesParameters;
import wkwk.discord.record.DeleteMessageRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AutoDeleteRunDAO extends DAOBase {
    public boolean CheckIfDeleteMessageDate(String Date) {
        this.open();
        PreparedStatement preStatement = null;
        boolean isCheck = false;
        try {
            String sql = "SELECT EXISTS(SELECT * FROM " + DAOParameters.TABLE_DELETE_MESSAGES.getParam() + " WHERE " + DeleteMessagesParameters.DELETE_TIME.getParam() + " < ?) AS MESSAGE_CHECK";
            preStatement = con.prepareStatement(sql);
            preStatement.setString(1, Date);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                isCheck = rs.getBoolean("MESSAGE_CHECK");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return isCheck;
    }

    public ArrayList<DeleteMessageRecord> getDeleteMessage(String date) {
        ArrayList<DeleteMessageRecord> list = new ArrayList<>();
        this.open();
        PreparedStatement preStatement;
        String sql = "SELECT * FROM " + DAOParameters.TABLE_DELETE_MESSAGES.getParam() + " WHERE " + DeleteMessagesParameters.DELETE_TIME.getParam() + " < ?";
        try {
            preStatement = con.prepareStatement(sql);
            preStatement.setString(1, date);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                DeleteMessageRecord message = new DeleteMessageRecord();
                message.setMessageId(rs.getLong(DeleteMessagesParameters.MESSAGE_ID.getParam()));
                message.setTextChannelId(rs.getLong(DeleteMessagesParameters.TEXT_CHANNEL_ID.getParam()));
                list.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void deleteMessage(ArrayList<DeleteMessageRecord> dates) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String builder = "?" + ",?".repeat(dates.size() - 1);
            String sql = "DELETE FROM " + DAOParameters.TABLE_DELETE_MESSAGES.getParam() + " WHERE " + DeleteMessagesParameters.MESSAGE_ID.getParam() + " IN (" + builder + ")";
            preStatement = con.prepareStatement(sql);
            for (int i = 0; i < dates.size(); i++) {
                preStatement.setLong(i + 1, dates.get(i).getMessageId());
            }
            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }
}
