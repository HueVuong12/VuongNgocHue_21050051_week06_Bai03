package fit.se.democart.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBUtil {

    public static Connection getConnection() throws SQLException {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/shopdb");
            if (ds != null) {
                return ds.getConnection();
            }
        } catch (Exception e) {
            // DataSource lookup fallback to DBConnection
        }
        return vn.fit.vuongngochue_bai3_tuan06.util.DBConnection.getConnection();
    }
}
