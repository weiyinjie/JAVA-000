package org.example.jdbc;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

public class HikariCPConnectionUtil {
    private static HikariDataSource hikariDataSource;

    static {
        hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariDataSource.setJdbcUrl("jdbc:mysql://sh-cdb-mn9j3gay.sql.tencentcdb.com:60789/test?useUnicode=true&characterEncoding=utf-8");
        hikariDataSource.setUsername("root");
        hikariDataSource.setPassword("Weiyinjie1992");
        hikariDataSource.setMaximumPoolSize(Runtime.getRuntime().availableProcessors() * 2 + 1);
    }

    public static Connection getConnection() {
        try {
            return hikariDataSource.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

}
