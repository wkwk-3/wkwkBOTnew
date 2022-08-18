package wkwk.discord.dao;

import wkwk.discord.dao.core.DAOBase;
import wkwk.discord.parameter.DAOParameters;
import wkwk.discord.parameter.ReactRoleParameters;
import wkwk.discord.record.ReactionRoleRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReactionRoleDAO extends DAOBase {
    // madeW
    public boolean checkIfEmoji(ReactionRoleRecord record) {
        this.open();
        PreparedStatement preStatement = null;
        boolean isCheck = false;
        try {
            String sql = "SELECT EXISTS(SELECT " + ReactRoleParameters.ROLE_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_REACT_ROLE.getParam() + " WHERE "
                    + ReactRoleParameters.EMOJI.getParam() + " = ? AND " + ReactRoleParameters.MESSAGE_ID.getParam() + " = ? " +
                    "AND " + ReactRoleParameters.SERVER_ID.getParam() + " = ?) AS emoji_check";
            preStatement = con.prepareStatement(sql);
            preStatement.setString(1, record.getEmoji());
            preStatement.setLong(2, record.getMessageId());
            preStatement.setLong(3, record.getServerId());
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                isCheck = rs.getBoolean("emoji_check");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return isCheck;
    }

    public long getReactionRoleData(ReactionRoleRecord reactionRoleRecord) {
        this.open();
        PreparedStatement preStatement = null;
        long roleId = -1;
        try {
            String sql = "SELECT " + ReactRoleParameters.ROLE_ID.getParam()
                    + " FROM " + DAOParameters.TABLE_REACT_ROLE.getParam() + " WHERE "
                    + ReactRoleParameters.EMOJI.getParam() + " = ? AND " + ReactRoleParameters.MESSAGE_ID.getParam() + " = ? " +
                    "AND " + ReactRoleParameters.SERVER_ID.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setString(1, reactionRoleRecord.getEmoji());
            preStatement.setLong(2, reactionRoleRecord.getMessageId());
            preStatement.setLong(3, reactionRoleRecord.getServerId());
            ResultSet rs = preStatement.executeQuery();
            while (rs.next()) {
                roleId = rs.getLong(ReactRoleParameters.ROLE_ID.getParam());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return roleId;
    }
}
