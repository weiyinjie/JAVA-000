package org.example.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
    public static ThreadLocal<Connection> connection = ThreadLocal.withInitial(ConnectionUtil::getConnection);

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://sh-cdb-mn9j3gay.sql.tencentcdb.com:60789/test?useUnicode=true&characterEncoding=utf-8",
                    "root",
                    "xxxxxx");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
