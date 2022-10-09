package wkwk.discord.dao;

import wkwk.discord.dao.core.DAOBase;
import wkwk.discord.parameter.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JoinAndLeaveServerDAO extends DAOBase {
    // madeW
    public void addServerData(long serverId) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "INSERT INTO " + DAOParameters.TABLE_SERVER_PROPERTY.getParam() + "(" + ServerPropertyParameters.SERVER_ID.getParam() + ") VALUES (?)";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, serverId);
            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }

    public void deleteServerData(long serverId) {
        this.open();
        PreparedStatement preStatement = null;
        try {
            String sql = "DELETE "
                    + "Property, "
                    + "TeChannel, "
                    + "ReRole, "
                    + "ReMessage, "
                    + "NaPreset, "
                    + "MeMessage, "
                    + "Log, "
                    + "DeTime, "
                    + "DeMessage, "
                    + "SeMessage "
                    + "FROM "
                    + DAOParameters.TABLE_SERVER_PROPERTY.getParam() + " AS Property "
                    + "LEFT JOIN " + DAOParameters.TABLE_TEMP_CHANNEL.getParam() + " AS TeChannel ON Property." + ServerPropertyParameters.SERVER_ID.getParam() + " = TeChannel." + TempChannelsParameters.SERVER_ID.getParam()
                    + " LEFT JOIN " + DAOParameters.TABLE_REACT_ROLE.getParam() + " AS ReRole ON Property." + ServerPropertyParameters.SERVER_ID.getParam() + " = ReRole." + ReactRoleParameters.SERVER_ID.getParam()
                    + " LEFT JOIN " + DAOParameters.TABLE_REACT_MESSAGE.getParam() + " AS ReMessage ON Property." + ServerPropertyParameters.SERVER_ID.getParam() + " = ReMessage." + ReactMessageParameters.SERVER_ID.getParam()
                    + " LEFT JOIN " + DAOParameters.TABLE_NAME_PRESET.getParam() + " AS NaPreset ON Property." + ServerPropertyParameters.SERVER_ID.getParam() + " = NaPreset." + NamePresetParameters.SERVER_ID.getParam()
                    + " LEFT JOIN " + DAOParameters.TABLE_MENTION_MESSAGE.getParam() + " AS MeMessage ON Property." + ServerPropertyParameters.SERVER_ID.getParam() + " = MeMessage." + MentionMessageParameters.SERVER_ID.getParam()
                    + " LEFT JOIN " + DAOParameters.TABLE_LOGGING.getParam() + " AS Log ON Property." + ServerPropertyParameters.SERVER_ID.getParam() + " = Log." + LoggingParameters.SERVER_ID.getParam()
                    + " LEFT JOIN " + DAOParameters.TABLE_DELETE_TIMES.getParam() + " AS DeTime ON Property." + ServerPropertyParameters.SERVER_ID.getParam() + " = DeTime." + DeleteTimesParameters.SERVER_ID.getParam()
                    + " LEFT JOIN " + DAOParameters.TABLE_DELETE_MESSAGES.getParam() + " AS DeMessage ON Property." + ServerPropertyParameters.SERVER_ID.getParam() + " = DeMessage." + DeleteMessagesParameters.SERVER_ID.getParam()
                    + " LEFT JOIN " + DAOParameters.TABLE_BOT_SEND_MESSAGES.getParam() + " AS SeMessage ON Property." + ServerPropertyParameters.SERVER_ID.getParam() + " = SeMessage." + BotSendMessageParameters.SERVER_ID.getParam()
                    + " WHERE Property." + ServerPropertyParameters.SERVER_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setLong(1, serverId);
            preStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
    }
}
