package wkwk.discord.dao;

import wkwk.discord.dao.core.DAOBase;
import wkwk.discord.parameter.DAOParameters;
import wkwk.discord.parameter.MentionMessageParameters;
import wkwk.discord.parameter.ServerPropertyParameters;
import wkwk.discord.parameter.TempChannelsParameters;
import wkwk.discord.record.MessageRecord;
import wkwk.discord.record.ServerDataRecord;
import wkwk.discord.record.TempChannelRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TempChannelDAO extends DAOBase {
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

    public ServerDataRecord getTempSetting(long serverId) {
        this.open();
        PreparedStatement preStatement = null;
        ServerDataRecord record = null;
        try {
            String sql = "SELECT "
                    + ServerPropertyParameters.VOICE_CATEGORY_ID.getParam()
                    + ", " + ServerPropertyParameters.TEXT_CATEGORY_ID.getParam()
                    + ", " + ServerPropertyParameters.TEMP_BY.getParam()
                    + ", " + ServerPropertyParameters.TEXT_BY.getParam()
                    + ", " + ServerPropertyParameters.DEFAULT_SIZE.getParam()
                    + ", " + ServerPropertyParameters.DEFAULT_NAME.getParam()
                    + ", " + ServerPropertyParameters.MENTION_CHANNEL_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_SERVER_PROPERTY.getParam()
                    + " WHERE " + ServerPropertyParameters.SERVER_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, serverId);
            ResultSet rs = preStatement.executeQuery();
            record = new ServerDataRecord();
            while (rs.next()) {
                record.setVoiceCategoryId(rs.getLong(ServerPropertyParameters.VOICE_CATEGORY_ID.getParam()));
                record.setTextCategoryId(rs.getLong(ServerPropertyParameters.TEXT_CATEGORY_ID.getParam()));
                record.setTempBy(rs.getBoolean(ServerPropertyParameters.TEMP_BY.getParam()));
                record.setTextBy(rs.getBoolean(ServerPropertyParameters.TEXT_BY.getParam()));
                record.setDefaultSize(rs.getInt(ServerPropertyParameters.DEFAULT_SIZE.getParam()));
                record.setDefaultName(rs.getString(ServerPropertyParameters.DEFAULT_NAME.getParam()));
                record.setMentionChannelId(rs.getLong(ServerPropertyParameters.MENTION_CHANNEL_ID.getParam()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return record;
    }

    public void setTempChannelData(TempChannelRecord record) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "INSERT INTO " + DAOParameters.TABLE_TEMP_CHANNEL.getParam() + " VALUES (?,?,?,?,?,?,?)";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, record.getVoiceChannelId());
            preStatement.setLong(2, record.getTextChannelId());
            preStatement.setLong(3, record.getServerId());
            preStatement.setLong(4, record.getOwnerUserId());
            preStatement.setLong(5, record.getInfoMessageId());
            preStatement.setBoolean(6, record.isHideBy());
            preStatement.setBoolean(7, record.isLockBy());
            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public long getVoiceCateGory(long serverId) {
        this.open();
        PreparedStatement preStatement = null;
        long voiceCategoryId = -1;
        try {
            String sql = "SELECT "
                    + ServerPropertyParameters.VOICE_CATEGORY_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_SERVER_PROPERTY.getParam()
                    + " WHERE " + ServerPropertyParameters.SERVER_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, serverId);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                voiceCategoryId = rs.getLong(ServerPropertyParameters.VOICE_CATEGORY_ID.getParam());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return voiceCategoryId;
    }

    public TempChannelRecord getTempChannelData(long channelId) {
        this.open();
        PreparedStatement preStatement = null;
        TempChannelRecord record = null;
        try {
            String sql = "SELECT "
                    + TempChannelsParameters.VOICE_CHANNEL_ID.getParam()
                    + ", " + TempChannelsParameters.TEXT_CHANNEL_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_TEMP_CHANNEL.getParam()
                    + " WHERE " + TempChannelsParameters.VOICE_CHANNEL_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, channelId);
            ResultSet rs = preStatement.executeQuery();
            record = new TempChannelRecord();
            while (rs.next()) {
                record.setVoiceChannelId(rs.getLong(TempChannelsParameters.VOICE_CHANNEL_ID.getParam()));
                record.setTextChannelId(rs.getLong(TempChannelsParameters.TEXT_CHANNEL_ID.getParam()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return record;
    }
    public void deleteTempChannelData(long channelId) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "DELETE FROM " + DAOParameters.TABLE_TEMP_CHANNEL.getParam() + " WHERE " + TempChannelsParameters.VOICE_CHANNEL_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, channelId);
            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }
    public long getOwnerUserId (long voiceChannelId) {
        this.open();
        PreparedStatement preStatement = null;
        long ownerUserId = -1;
        try {
            String sql = "SELECT "
                    + TempChannelsParameters.OWNER_USER_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_TEMP_CHANNEL.getParam()
                    + " WHERE " + TempChannelsParameters.VOICE_CHANNEL_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, voiceChannelId);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                ownerUserId = rs.getLong(TempChannelsParameters.OWNER_USER_ID.getParam());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return ownerUserId;
    }

    public ArrayList<MessageRecord> getMentionMessage(long voiceChanelId) {
        this.open();
        PreparedStatement preStatement = null;
        ArrayList<MessageRecord> arrayList =  new ArrayList<>();
        try {
            String sql = "SELECT *"
                    + " FROM " + DAOParameters.TABLE_MENTION_MESSAGE.getParam()
                    + " WHERE " + MentionMessageParameters.VOICE_CHANNEL_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, voiceChanelId);
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

    public ArrayList<TempChannelRecord> getTempChannels(long serverId) {
        this.open();
        ArrayList<TempChannelRecord> records = new ArrayList<>();
        PreparedStatement preStatement = null;
        try {
            String sql = "SELECT "
                    + TempChannelsParameters.VOICE_CHANNEL_ID.getParam()
                    + ", " + TempChannelsParameters.TEXT_CHANNEL_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_TEMP_CHANNEL.getParam()
                    + " WHERE " + TempChannelsParameters.SERVER_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, serverId);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                TempChannelRecord record = new TempChannelRecord();
                record.setVoiceChannelId(rs.getLong(TempChannelsParameters.VOICE_CHANNEL_ID.getParam()));
                record.setTextChannelId(rs.getLong(TempChannelsParameters.TEXT_CHANNEL_ID.getParam()));
                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return records;
    }

    public void deleteTempChannelDates(ArrayList<Long> deleteTargetVoiceIds) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            if (deleteTargetVoiceIds.size() > 0) {
                String voiceQ = "?" + ",?".repeat(Math.max(0, deleteTargetVoiceIds.size() - 1));
                String sql = "DELETE FROM " + DAOParameters.TABLE_TEMP_CHANNEL.getParam() + " WHERE " + TempChannelsParameters.VOICE_CHANNEL_ID.getParam() + " IN (" + voiceQ + ")";
                preStatement = con.prepareStatement(sql);
                for (int i = 0; i < deleteTargetVoiceIds.size(); i++) {
                    preStatement.setLong(i + 1, deleteTargetVoiceIds.get(i));
                }
                preStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }
}
