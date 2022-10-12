package wkwk.discord.dao.core;

import wkwk.discord.parameter.DAOParameters;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DAOBase {
    // madeW
    public Connection con;

    protected void open() {
        try {
            con = DriverManager.getConnection(DAOParameters.CONNECT_STRING.getParam(), DAOParameters.USER_ID.getParam(), DAOParameters.PASS_WORD.getParam());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void close(Statement stmt) {
        try {
            if (stmt != null) stmt.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
