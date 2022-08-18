package wkwk.discord.dao;

import wkwk.discord.dao.core.DAOBase;
import wkwk.discord.parameter.BotDataParameters;
import wkwk.discord.parameter.DAOParameters;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BotDataDAO extends DAOBase {
    // madeW
    public String BotGetToken() {
        this.open();
        String token = "";
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            String sql = "SELECT " + BotDataParameters.BOT_TOKEN.getParam() + " FROM " + DAOParameters.TABLE_BOT_DATA.getParam();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) token = rs.getString(BotDataParameters.BOT_TOKEN.getParam());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(stmt);
        }
        return token;
    }
}
