package wkwk.discord.dao;

import wkwk.discord.dao.core.DAOBase;
import wkwk.discord.parameter.*;
import wkwk.discord.record.LoggingRecord;
import wkwk.discord.record.NamePresetRecord;
import wkwk.discord.record.ReactionRoleRecord;
import wkwk.discord.record.TempChannelRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DeleteSelectSystemDAO extends DAOBase {
    public TempChannelRecord getTempChannelData(long channelId) {
        TempChannelRecord record = new TempChannelRecord();
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "SELECT "
                    + TempChannelsParameters.VOICE_CHANNEL_ID.getParam()
                    + ", " + TempChannelsParameters.TEXT_CHANNEL_ID.getParam()
                    + ", " + TempChannelsParameters.OWNER_USER_ID.getParam()
                    + ", " + TempChannelsParameters.INFO_MESSAGE_ID.getParam()
                    + ", " + TempChannelsParameters.HIDE_BY.getParam()
                    + ", " + TempChannelsParameters.LOCK_BY.getParam()
                    + " FROM " + DAOParameters.TABLE_TEMP_CHANNEL.getParam()
                    + " WHERE " + TempChannelsParameters.VOICE_CHANNEL_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, channelId);
            ResultSet rs = preStatement.executeQuery();
            if (rs.next()) {
                record.setExistBy(true);
                record.setVoiceChannelId(rs.getLong(TempChannelsParameters.VOICE_CHANNEL_ID.getParam()));
                record.setTextChannelId(rs.getLong(TempChannelsParameters.TEXT_CHANNEL_ID.getParam()));
                record.setOwnerUserId(rs.getLong(TempChannelsParameters.OWNER_USER_ID.getParam()));
                record.setInfoMessageId(rs.getLong(TempChannelsParameters.INFO_MESSAGE_ID.getParam()));
                record.setHideBy(rs.getBoolean(TempChannelsParameters.HIDE_BY.getParam()));
                record.setLockBy(rs.getBoolean(TempChannelsParameters.LOCK_BY.getParam()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return record;
    }

    public void deleteReactionRole(ReactionRoleRecord roleRecord) {
        this.open();
        PreparedStatement preStatement = null;
        ArrayList<String> emojis = roleRecord.getEmojis();
        String emojiQ = "?" + ",?".repeat(Math.max(0, emojis.size() - 1));
        try {
            String sql = "DELETE FROM " + DAOParameters.TABLE_REACT_ROLE.getParam() + " WHERE " + ReactRoleParameters.MESSAGE_ID.getParam() + " = ? AND " + ReactRoleParameters.EMOJI.getParam() + " IN (" + emojiQ + ")";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, roleRecord.getMessageId());
            for (int i = 0; i < emojiQ.length(); i++) {
                preStatement.setString(i + 2, emojis.get(i));
            }
            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public String getReactMessageLink(long messageId) {
        this.open();
        PreparedStatement preStatement = null;
        String messageLink = null;
        try {
            String sql = "SELECT "
                    + ReactMessageParameters.MESSAGE_LINK.getParam()
                    + " FROM " + DAOParameters.TABLE_REACT_MESSAGE.getParam()
                    + " WHERE " + ReactMessageParameters.MESSAGE_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, messageId);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                messageLink = rs.getString(ReactMessageParameters.MESSAGE_LINK.getParam());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return messageLink;
    }

    public void deleteNamePreset(ArrayList<NamePresetRecord> namePresetRecords) {
        this.open();
        PreparedStatement preStatement = null;
        long serverId = namePresetRecords.get(0).getServerId();
        String nameQ = "?" + ",?".repeat(Math.max(0, namePresetRecords.size() - 1));
        try {
            String sql = "DELETE FROM " + DAOParameters.TABLE_NAME_PRESET.getParam() + " WHERE " + NamePresetParameters.SERVER_ID.getParam() + " = ? AND " + NamePresetParameters.NAME_SELECT.getParam() + " IN (" + nameQ + ")";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, serverId);
            for (int i = 0; i < nameQ.length(); i++) {
                preStatement.setString(i + 2, namePresetRecords.get(i).getName());
            }
            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void deleteLogging(ArrayList<LoggingRecord> userLoggingRecords, ArrayList<LoggingRecord> chatLoggingRecords) {
        this.open();
        Statement statement = null;
        String usersSql = null;
        StringBuilder chatSql = null;

        if (userLoggingRecords.size() > 0) {
            StringBuilder userIn = new StringBuilder(Long.toString(userLoggingRecords.get(0).getTextChannelId()));
            for (LoggingRecord record : userLoggingRecords) {
                userIn.append(",").append(record.getTextChannelId());
            }
            usersSql = "DELETE FROM " + DAOParameters.TABLE_LOGGING.getParam() + " WHERE " + LoggingParameters.LOG_TYPE.getParam() + " = 'user' AND " + LoggingParameters.CHANNEL_ID.getParam() + " IN (" + userIn + ")";
        }

        if (chatLoggingRecords.size() > 0) {
            String sql = "DELETE FROM " + DAOParameters.TABLE_LOGGING.getParam() + " WHERE " + LoggingParameters.LOG_TYPE.getParam() + " = 'chat' AND " + LoggingParameters.CHANNEL_ID.getParam() + " = " + chatLoggingRecords.get(0).getTextChannelId() + " AND " + LoggingParameters.TARGET_CHANNEL_ID.getParam() + " = " + chatLoggingRecords.get(0).getTargetChannelId();
            chatSql = new StringBuilder(sql);
            for (LoggingRecord record : chatLoggingRecords) {
                if (chatLoggingRecords.indexOf(record) == 0) continue;
                sql = "DELETE FROM " + DAOParameters.TABLE_LOGGING.getParam() + " WHERE " + LoggingParameters.LOG_TYPE.getParam() + " = 'chat' AND " + LoggingParameters.CHANNEL_ID.getParam() + " = " + record.getTextChannelId() + " AND " + LoggingParameters.TARGET_CHANNEL_ID.getParam() + " = " + record.getTargetChannelId();
                chatSql.append(";").append(sql);
            }
        }

        try {
            statement = con.createStatement();
            String sql = (usersSql == null ? "" : usersSql) +
                    (usersSql == null && chatSql == null ? "" : ";") +
                    (chatSql == null ? "" : chatSql);
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(statement);
        }
    }
}
