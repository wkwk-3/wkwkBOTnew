package wkwk.discord.dao;

import wkwk.discord.dao.core.DAOBase;
import wkwk.discord.parameter.*;
import wkwk.discord.record.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SlashCommandDAO extends DAOBase {
    // madeW
    public void UpDataVoiceCategory(long serverId, long voiceCategoryId) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_SERVER_PROPERTY.getParam() + " SET "
                    + ServerPropertyParameters.VOICE_CATEGORY_ID.getParam() + " = ? WHERE "
                    + ServerPropertyParameters.SERVER_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, voiceCategoryId);
            preStatement.setLong(2, serverId);
            preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void UpDataTextCategory(long serverId, long textCategoryId) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_SERVER_PROPERTY.getParam() + " SET "
                    + ServerPropertyParameters.TEXT_CATEGORY_ID.getParam() + " = ? WHERE "
                    + ServerPropertyParameters.SERVER_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, textCategoryId);
            preStatement.setLong(2, serverId);
            preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void UpDataFirstChannel(long serverId, long firstChannelId) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_SERVER_PROPERTY.getParam() + " SET "
                    + ServerPropertyParameters.FIRST_CHANNEL_ID.getParam() + " = ? WHERE "
                    + ServerPropertyParameters.SERVER_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, firstChannelId);
            preStatement.setLong(2, serverId);
            preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void UpDataMentionChannel(long serverId, long mentionChannelId) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_SERVER_PROPERTY.getParam() + " SET "
                    + ServerPropertyParameters.MENTION_CHANNEL_ID.getParam() + " = ? WHERE "
                    + ServerPropertyParameters.SERVER_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, mentionChannelId);
            preStatement.setLong(2, serverId);
            preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void UpDataTempAll(long serverId, boolean by) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_SERVER_PROPERTY.getParam() + " SET "
                    + ServerPropertyParameters.TEMP_BY.getParam() + " = ? WHERE "
                    + ServerPropertyParameters.SERVER_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setBoolean(1, by);
            preStatement.setLong(2, serverId);
            preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void UpDataTempText(long serverId, boolean by) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_SERVER_PROPERTY.getParam() + " SET "
                    + ServerPropertyParameters.TEXT_BY.getParam() + " = ? WHERE "
                    + ServerPropertyParameters.SERVER_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setBoolean(1, by);
            preStatement.setLong(2, serverId);
            preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void UpDataDefaultSize(long serverId, int size) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_SERVER_PROPERTY.getParam() + " SET "
                    + ServerPropertyParameters.DEFAULT_SIZE.getParam() + " = ? WHERE "
                    + ServerPropertyParameters.SERVER_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setInt(1, size);
            preStatement.setLong(2, serverId);
            preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public boolean CheckIfRole(long messageId) {
        this.open();
        PreparedStatement preStatement = null;
        boolean isCheck = false;
        try {
            String sql = "SELECT EXISTS(SELECT " + ReactMessageParameters.MESSAGE_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_REACT_MESSAGE.getParam() + " WHERE "
                    + ReactMessageParameters.MESSAGE_ID.getParam() + " = ?) AS message_check";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, messageId);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                isCheck = rs.getBoolean("message_check");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return !isCheck;
    }

    public long getReactChanel(long messageId) {
        this.open();
        PreparedStatement preStatement = null;
        long channelId = -1;
        try {
            String sql = "SELECT " + ReactMessageParameters.TEXT_CHANNEL_ID.getParam() + " FROM "
                    + DAOParameters.TABLE_REACT_MESSAGE.getParam() + " WHERE " + ReactMessageParameters.MESSAGE_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, messageId);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                channelId = rs.getLong(ReactMessageParameters.TEXT_CHANNEL_ID.getParam());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return channelId;
    }

    public boolean CheckIfManage(long userId, long channel) {
        this.open();
        PreparedStatement preStatement = null;
        boolean isCheck = false;
        try {
            String sql = "SELECT EXISTS(SELECT " + TempChannelsParameters.OWNER_USER_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_TEMP_CHANNEL.getParam() + " WHERE "
                    + TempChannelsParameters.OWNER_USER_ID.getParam() + " = ? AND " + TempChannelsParameters.VOICE_CHANNEL_ID.getParam() + " = ?) AS manage_check";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, userId);
            preStatement.setLong(2, channel);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                isCheck = rs.getBoolean("manage_check");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return isCheck;
    }

    public boolean CheckIfChannel(long channel) {
        this.open();
        PreparedStatement preStatement = null;
        boolean isCheck = false;
        try {
            String sql = "SELECT EXISTS(SELECT " + TempChannelsParameters.OWNER_USER_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_TEMP_CHANNEL.getParam() + " WHERE "
                    + TempChannelsParameters.VOICE_CHANNEL_ID.getParam() + " = ?) AS channel_check";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, channel);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                isCheck = rs.getBoolean("channel_check");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return isCheck;
    }

    public void setReactionRole(MessageRecord record) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "INSERT INTO " + DAOParameters.TABLE_REACT_ROLE.getParam() + " VALUES (?, ?, ?, ?)";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, record.getMessageId());
            preStatement.setLong(2, record.getRole().getId());
            preStatement.setString(3, record.getEmoji());
            preStatement.setLong(4, record.getServerId());
            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void setReactMessageData(MessageRecord record) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "INSERT INTO " + DAOParameters.TABLE_REACT_MESSAGE.getParam() + " VALUES (?, ?, ?)";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, record.getServerId());
            preStatement.setLong(2, record.getTextChannelId());
            preStatement.setLong(3, record.getMessageId());
            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public int getReactMessageSize(long messageId) {
        this.open();
        PreparedStatement preStatement = null;
        int messageSize = 0;
        try {
            String sql = "SELECT COUNT(" + ReactRoleParameters.EMOJI.getParam() + ") AS role_size FROM " + DAOParameters.TABLE_REACT_ROLE.getParam() + " WHERE " + ReactRoleParameters.MESSAGE_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, messageId);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                messageSize = rs.getInt("role_size");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return messageSize;
    }

    public int getServerReactMessageSize(long serverId) {
        this.open();
        PreparedStatement preStatement = null;
        int messageSize = 0;
        try {
            String sql = "SELECT COUNT(" + ReactMessageParameters.MESSAGE_ID.getParam() + ") AS message_size FROM " + DAOParameters.TABLE_REACT_MESSAGE.getParam() + " WHERE " + ReactMessageParameters.SERVER_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, serverId);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                messageSize = rs.getInt("message_size");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return messageSize;
    }

    public boolean CheckIfMessage(long messageId) {
        this.open();
        PreparedStatement preStatement = null;
        boolean isCheck = false;
        try {
            String sql = "SELECT EXISTS(SELECT " + ReactMessageParameters.MESSAGE_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_REACT_MESSAGE.getParam() + " WHERE "
                    + ReactMessageParameters.MESSAGE_ID.getParam() + " = ?) AS message_check";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, messageId);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                isCheck = rs.getBoolean("message_check");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return isCheck;
    }

    public boolean CheckIfNamePreset(String name) {
        this.open();
        PreparedStatement preStatement = null;
        boolean isCheck = false;
        try {
            String sql = "SELECT EXISTS(SELECT " + NamePresetParameters.SERVER_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_NAME_PRESET.getParam() + " WHERE "
                    + NamePresetParameters.NAME_SELECT.getParam() + " = ?) AS preset_check";
            preStatement = con.prepareStatement(sql);
            preStatement.setString(1, name);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                isCheck = rs.getBoolean("preset_check");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return isCheck;
    }

    public int getNamePresetSize(long serverId) {
        this.open();
        PreparedStatement preStatement = null;
        int messageSize = 0;
        try {
            String sql = "SELECT COUNT(" + NamePresetParameters.NAME_SELECT.getParam() + ") AS namepreset_size FROM " + DAOParameters.TABLE_NAME_PRESET.getParam() + " WHERE " + NamePresetParameters.SERVER_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, serverId);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                messageSize = rs.getInt("namepreset_size");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return messageSize;
    }

    public void setNamePreset(NamePresetRecord record) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "INSERT INTO " + DAOParameters.TABLE_NAME_PRESET.getParam() + " VALUES (?, ?)";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, record.getServerId());
            preStatement.setString(2, record.getName());
            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public int getLoggingSize(long serverId) {
        this.open();
        PreparedStatement preStatement = null;
        int messageSize = 0;
        try {
            String sql = "SELECT COUNT(*) AS logging_size FROM " + DAOParameters.TABLE_LOGGING.getParam() + " WHERE " + NamePresetParameters.SERVER_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, serverId);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                messageSize = rs.getInt("logging_size");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return messageSize;
    }

    public boolean CheckIfLogging(LoggingRecord record) {
        this.open();
        PreparedStatement preStatement = null;
        boolean isCheck = false;
        try {
            String sql = "SELECT EXISTS(SELECT " + LoggingParameters.SERVER_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_LOGGING.getParam() + " WHERE "
                    + LoggingParameters.SERVER_ID.getParam() + " = ? " +
                    "AND " + LoggingParameters.CHANNEL_ID.getParam() + " = ? " +
                    "AND " + LoggingParameters.LOG_TYPE.getParam() + " = ? " +
                    "AND " + LoggingParameters.TARGET_CHANNEL_ID.getParam() + " = ? " +
                    ") AS logging_check";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, record.getServerId());
            preStatement.setLong(2, record.getTextChannelId());
            preStatement.setString(3, record.getType());
            preStatement.setLong(4, record.getTargetChannelId());
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                isCheck = rs.getBoolean("logging_check");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return isCheck;
    }

    public void setLogging(LoggingRecord record) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "INSERT INTO " + DAOParameters.TABLE_LOGGING.getParam() + " VALUES (?, ?, ?, ?)";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, record.getServerId());
            preStatement.setLong(2, record.getTextChannelId());
            preStatement.setString(3, record.getType());
            preStatement.setLong(4, record.getTargetChannelId());
            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void updateStereoTyped(long serverId, String stereoTyped) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_SERVER_PROPERTY.getParam() + " SET "
                    + ServerPropertyParameters.STEREOTYPED.getParam() + " = ?"
                    + " WHERE " + ServerPropertyParameters.SERVER_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setString(1, stereoTyped);
            preStatement.setLong(2, serverId);
            preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void setDeleteTime(DeleteTimeRecord record) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "INSERT INTO " + DAOParameters.TABLE_DELETE_TIMES.getParam() + " VALUES (?, ?, ?, ?)";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, record.getServerId());
            preStatement.setLong(2, record.getTextChannelId());
            preStatement.setLong(3, record.getTime());
            preStatement.setString(4, record.getUnit());
            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public boolean checkIfDeleteTime(long channelId) {
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

    public void deleteDeleteTime(long channelId) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "DELETE "
                    + "DeTime, "
                    + "DeMessage"
                    + " FROM "
                    + DAOParameters.TABLE_DELETE_TIMES.getParam() + " AS DeTime"
                    + " LEFT JOIN " + DAOParameters.TABLE_DELETE_MESSAGES.getParam() + " AS DeMessage ON DeTime." + DeleteTimesParameters.TEXT_CHANNEL_ID.getParam() + " = DeMessage." + DeleteMessagesParameters.TEXT_CHANNEL_ID.getParam()
                    + " WHERE DeTime." + DeleteTimesParameters.TEXT_CHANNEL_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, channelId);
            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public ReactionRoleRecord getReactAllData(long messageId) {
        this.open();
        PreparedStatement preStatement = null;
        ReactionRoleRecord record = null;
        try {
            String sql = "SELECT * FROM " + DAOParameters.TABLE_REACT_ROLE.getParam() + " WHERE " + ReactRoleParameters.MESSAGE_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, messageId);
            ResultSet rs = preStatement.executeQuery();
            record = new ReactionRoleRecord();
            record.setMessageId(messageId);
            ArrayList<Long> roleIds = new ArrayList<>();
            ArrayList<String> emojis = new ArrayList<>();
            while (rs.next()) {
                roleIds.add(rs.getLong(ReactRoleParameters.ROLE_ID.getParam()));
                emojis.add(rs.getString(ReactRoleParameters.EMOJI.getParam()));
            }
            record.setRoleIds(roleIds);
            record.setEmojis(emojis);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return record;

    }

    public ArrayList<String> getNamePresetAllData(long serverId) {
        this.open();
        ArrayList<String> names = new ArrayList<>();
        PreparedStatement preStatement = null;
        try {
            String sql = "SELECT " + NamePresetParameters.NAME_SELECT.getParam() + " FROM " + DAOParameters.TABLE_NAME_PRESET.getParam() + " WHERE " + NamePresetParameters.SERVER_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, serverId);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                names.add(rs.getString(NamePresetParameters.NAME_SELECT.getParam()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return names;
    }

    public ArrayList<LoggingRecord> getLogging(long serverId) {
        this.open();
        PreparedStatement preStatement;
        ArrayList<LoggingRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM " + DAOParameters.TABLE_LOGGING.getParam() + " WHERE " + LoggingParameters.SERVER_ID.getParam() + " = ?";
        try {
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, serverId);
            ResultSet rsx = preStatement.executeQuery();
            while (rsx.next()) {
                LoggingRecord record = new LoggingRecord();
                record.setServerId(rsx.getLong(LoggingParameters.SERVER_ID.getParam()));
                record.setTextChannelId(rsx.getLong(LoggingParameters.CHANNEL_ID.getParam()));
                record.setType(rsx.getString(LoggingParameters.LOG_TYPE.getParam()));
                record.setTargetChannelId(rsx.getLong(LoggingParameters.TARGET_CHANNEL_ID.getParam()));
                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    public ServerDataRecord getTempSetting(long serverId) {
        this.open();
        PreparedStatement preStatement = null;
        ServerDataRecord record = null;
        try {
            String sql = "SELECT "
                    + ServerPropertyParameters.VOICE_CATEGORY_ID.getParam()
                    + ", " + ServerPropertyParameters.TEXT_CATEGORY_ID.getParam()
                    + ", " + ServerPropertyParameters.FIRST_CHANNEL_ID.getParam()
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
                record.setFirstChannelId(rs.getLong(ServerPropertyParameters.FIRST_CHANNEL_ID.getParam()));
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

    public ArrayList<DeleteTimeRecord> getDeleteTimes(long serverId) {
        this.open();
        PreparedStatement preStatement = null;
        ArrayList<DeleteTimeRecord> records = new ArrayList<>();
        try {
            String sql = "SELECT "
                    + DeleteTimesParameters.TEXT_CHANNEL_ID.getParam()
                    + ", " + DeleteTimesParameters.DELETE_TIME.getParam()
                    + ", " + DeleteTimesParameters.TIME_UNIT.getParam()
                    + " FROM " + DAOParameters.TABLE_DELETE_TIMES.getParam()
                    + " WHERE " + DeleteTimesParameters.SERVER_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, serverId);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                DeleteTimeRecord record = new DeleteTimeRecord();
                record.setTextChannelId(rs.getLong(DeleteTimesParameters.TEXT_CHANNEL_ID.getParam()));
                record.setTime(rs.getLong(DeleteTimesParameters.DELETE_TIME.getParam()));
                record.setUnit(rs.getString(DeleteTimesParameters.TIME_UNIT.getParam()));
                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return records;
    }


    public ArrayList<MessageRecord> getReactMessages(long serverId) {
        this.open();
        PreparedStatement preStatement = null;
        ArrayList<MessageRecord> records = new ArrayList<>();
        try {
            String sql = "SELECT "
                    + ReactMessageParameters.TEXT_CHANNEL_ID.getParam()
                    + ", " + ReactMessageParameters.MESSAGE_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_REACT_MESSAGE.getParam()
                    + " WHERE " + ReactMessageParameters.SERVER_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, serverId);
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                MessageRecord record = new MessageRecord();
                record.setTextChannelId(rs.getLong(ReactMessageParameters.TEXT_CHANNEL_ID.getParam()));
                record.setMessageId(rs.getLong(ReactMessageParameters.MESSAGE_ID.getParam()));
                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return records;
    }

    public TempChannelRecord getTempChannelData(long channelId) {
        this.open();
        PreparedStatement preStatement = null;
        TempChannelRecord record = null;
        try {
            String sql = "SELECT " + TempChannelsParameters.TEXT_CHANNEL_ID.getParam()
                    + "," + TempChannelsParameters.OWNER_USER_ID.getParam()
                    + " FROM "
                    + DAOParameters.TABLE_TEMP_CHANNEL.getParam()
                    + " WHERE " + TempChannelsParameters.VOICE_CHANNEL_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, channelId);
            ResultSet rs = preStatement.executeQuery();
            record = new TempChannelRecord();
            while (rs.next()) {
                record.setOwnerUserId(rs.getLong(TempChannelsParameters.OWNER_USER_ID.getParam()));
                record.setTextChannelId(rs.getLong(TempChannelsParameters.TEXT_CHANNEL_ID.getParam()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return record;
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

    public void setMentionMessage(MessageRecord messageRecord) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "INSERT INTO " + DAOParameters.TABLE_MENTION_MESSAGE.getParam() + " VALUES (?, ?, ?, ?, ?)";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, messageRecord.getServerId());
            preStatement.setLong(2, messageRecord.getMessageId());
            preStatement.setLong(3, messageRecord.getTextChannelId());
            preStatement.setLong(4, messageRecord.getVoiceChannelId());
            preStatement.setString(5, messageRecord.getLink());
            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }
}
