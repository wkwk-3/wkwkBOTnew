package wkwk.discord.dao;

import wkwk.discord.dao.core.DAOBase;
import wkwk.discord.parameter.*;
import wkwk.discord.record.MessageRecord;
import wkwk.discord.record.ServerDataRecord;
import wkwk.discord.record.TempChannelRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InfoMessageDAO extends DAOBase {
    // madeW
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
                    + " WHERE " + TempChannelsParameters.TEXT_CHANNEL_ID.getParam() + " = ?";
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

    public void updateTempData(TempChannelRecord record) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_TEMP_CHANNEL.getParam() + " SET "
                    + TempChannelsParameters.OWNER_USER_ID.getParam() + " = ?,"
                    + TempChannelsParameters.INFO_MESSAGE_ID.getParam() + " = ?,"
                    + TempChannelsParameters.HIDE_BY.getParam() + " = ?,"
                    + TempChannelsParameters.LOCK_BY.getParam() + " = ?"
                    + " WHERE " + TempChannelsParameters.VOICE_CHANNEL_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, record.getOwnerUserId());
            preStatement.setLong(2, record.getInfoMessageId());
            preStatement.setBoolean(3, record.isHideBy());
            preStatement.setBoolean(4, record.isLockBy());
            preStatement.setLong(5, record.getVoiceChannelId());
            preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public ArrayList<String> getNamePreset(long serverId) {
        this.open();
        PreparedStatement preStatement = null;
        ArrayList<String> arrayList = null;
        try {
            String sql = "SELECT "
                    + NamePresetParameters.NAME_SELECT.getParam()
                    + " FROM " + DAOParameters.TABLE_NAME_PRESET.getParam()
                    + " WHERE " + NamePresetParameters.SERVER_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, serverId);
            ResultSet rs = preStatement.executeQuery();
            arrayList = new ArrayList<>();
            while (rs.next()) {
                arrayList.add(rs.getString(NamePresetParameters.NAME_SELECT.getParam()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return arrayList;
    }

    public ServerDataRecord getMentionData(long serverId) {
        this.open();
        PreparedStatement preStatement = null;
        ServerDataRecord record = null;
        try {
            String sql = "SELECT "
                    + ServerPropertyParameters.MENTION_CHANNEL_ID.getParam()
                    + ", " + ServerPropertyParameters.STEREOTYPED.getParam()
                    + " FROM " + DAOParameters.TABLE_SERVER_PROPERTY.getParam()
                    + " WHERE " + ServerPropertyParameters.SERVER_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, serverId);
            ResultSet rs = preStatement.executeQuery();
            record = new ServerDataRecord();
            while (rs.next()) {
                record.setMentionChannelId(rs.getLong(ServerPropertyParameters.MENTION_CHANNEL_ID.getParam()));
                record.setStereoTyped(rs.getString(ServerPropertyParameters.STEREOTYPED.getParam()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return record;
    }
    public void setMentionMessage (MessageRecord record) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "INSERT INTO " + DAOParameters.TABLE_MENTION_MESSAGE.getParam() + " VALUES (?, ?, ?, ?, ?)";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, record.getServerId());
            preStatement.setLong(2, record.getMessageId());
            preStatement.setLong(3, record.getTextChannelId());
            preStatement.setLong(4, record.getVoiceChannelId());
            preStatement.setString(5, record.getLink());
            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public ArrayList<MessageRecord> getMentionMessage(long textChanelId) {
        this.open();
        PreparedStatement preStatement = null;
        ArrayList<MessageRecord> arrayList =  new ArrayList<>();
        try {
            String sql = "SELECT * "
                    + " FROM " + DAOParameters.TABLE_MENTION_MESSAGE.getParam()
                    + " WHERE " + MentionMessageParameters.TEXT_CHANNEL_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, textChanelId);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                MessageRecord record = new MessageRecord();
                record.setServerId(rs.getLong(MentionMessageParameters.SERVER_ID.getParam()));
                record.setMessageId(rs.getLong(MentionMessageParameters.MESSAGE_ID.getParam()));
                record.setTextChannelId(rs.getLong(MentionMessageParameters.TEXT_CHANNEL_ID.getParam()));
                record.setVoiceChannelId(rs.getLong(MentionMessageParameters.VOICE_CHANNEL_ID.getParam()));
                record.setLink(rs.getString(MentionMessageParameters.MESSAGE_LINK.getParam()));
                arrayList.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return arrayList;
    }

    public void updateTempChannelOwner(long userId, long voiceChannelId) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_TEMP_CHANNEL.getParam() + " SET "
                    + TempChannelsParameters.OWNER_USER_ID.getParam() + " = ?"
                    + " WHERE " + TempChannelsParameters.VOICE_CHANNEL_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, userId);
            preStatement.setLong(2, voiceChannelId);
            preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void deleteMentionMessage(long textChannelId) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "DELETE FROM " + DAOParameters.TABLE_MENTION_MESSAGE.getParam() + " WHERE " + MentionMessageParameters.TEXT_CHANNEL_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, textChannelId);
            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }
}
