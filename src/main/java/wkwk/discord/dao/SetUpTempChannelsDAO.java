package wkwk.discord.dao;

import wkwk.discord.dao.core.DAOBase;
import wkwk.discord.parameter.DAOParameters;
import wkwk.discord.parameter.ServerPropertyParameters;
import wkwk.discord.record.ServerDataRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SetUpTempChannelsDAO extends DAOBase {
    // madeW
    public void updateServerData(ServerDataRecord record) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_SERVER_PROPERTY.getParam() + " SET "
                    + ServerPropertyParameters.VOICE_CATEGORY_ID.getParam() + " = ?,"
                    + ServerPropertyParameters.TEXT_CATEGORY_ID.getParam() + " = ?,"
                    + ServerPropertyParameters.FIRST_CHANNEL_ID.getParam() + " = ?,"
                    + ServerPropertyParameters.MENTION_CHANNEL_ID.getParam() + " = ?"
                    + " WHERE " + ServerPropertyParameters.SERVER_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, record.getVoiceCategoryId());
            preStatement.setLong(2, record.getTextCategoryId());
            preStatement.setLong(3, record.getFirstChannelId());
            preStatement.setLong(4, record.getMentionChannelId());
            preStatement.setLong(5, record.getServerId());
            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public ServerDataRecord getServerData(long serverId) {
        this.open();
        PreparedStatement preStatement = null;
        ServerDataRecord record = null;
        try {
            String sql = "SELECT "
                    + ServerPropertyParameters.VOICE_CATEGORY_ID.getParam() + ","
                    + ServerPropertyParameters.TEXT_CATEGORY_ID.getParam() + ","
                    + ServerPropertyParameters.FIRST_CHANNEL_ID.getParam() + ","
                    + ServerPropertyParameters.MENTION_CHANNEL_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_SERVER_PROPERTY.getParam()
                    + " WHERE " + ServerPropertyParameters.SERVER_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, serverId);
            ResultSet rs = preStatement.executeQuery();
            record = new ServerDataRecord();
            while (rs.next()) {
                record.setVoiceCategoryId(rs.getLong(ServerPropertyParameters.VOICE_CATEGORY_ID.getParam()));
                record.setTextCategoryId(rs.getLong(ServerPropertyParameters.TEXT_CATEGORY_ID.getParam()));
                record.setFirstChannelId(rs.getLong(ServerPropertyParameters.FIRST_CHANNEL_ID.getParam()));
                record.setMentionChannelId(rs.getLong(ServerPropertyParameters.MENTION_CHANNEL_ID.getParam()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return record;
    }

    public boolean checkIfTempChannelData(long id) {
        this.open();
        PreparedStatement preStatement = null;
        boolean isCheck = false;
        try {
            String sql = "SELECT " + ServerPropertyParameters.MENTION_CHANNEL_ID.getParam()
                    + "," + ServerPropertyParameters.FIRST_CHANNEL_ID.getParam()
                    + "," + ServerPropertyParameters.VOICE_CATEGORY_ID.getParam()
                    + "," + ServerPropertyParameters.TEXT_CATEGORY_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_SERVER_PROPERTY.getParam() + " WHERE "
                    + ServerPropertyParameters.SERVER_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, id);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                boolean mentionCheck = rs.getLong(ServerPropertyParameters.MENTION_CHANNEL_ID.getParam()) != 0;
                boolean firstCheck = rs.getLong(ServerPropertyParameters.FIRST_CHANNEL_ID.getParam()) != 0;
                boolean voiceCheck = rs.getLong(ServerPropertyParameters.VOICE_CATEGORY_ID.getParam()) != 0;
                boolean textCheck = rs.getLong(ServerPropertyParameters.TEXT_CATEGORY_ID.getParam()) != 0;
                isCheck = mentionCheck || firstCheck || voiceCheck || textCheck;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return isCheck;
    }
}
