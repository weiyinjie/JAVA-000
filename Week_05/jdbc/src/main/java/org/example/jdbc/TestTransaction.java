package org.example.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestTransaction {
    public static void main(String[] args) {
        TestTransaction testTransaction = new TestTransaction();
        testTransaction.doTransaction();
    }

    private static final long id = 1L;

    public void doTransaction() {
        Connection connection = HikariCPConnectionUtil.getConnection();
        try {
            connection.setAutoCommit(false);
            System.out.println("user: " + this.getById(id));
            this.update(new User(id, "ttt"));
            System.out.println("update name = ttt where id = " + id);
            System.out.println("after update: " + this.getById(id));
            int i = 1 / 0;
            this.delete(id);
            connection.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            System.out.println("after rollback: " + this.getById(id));
        }
    }

    public void delete(Long id) {
        try {
            Connection connection = HikariCPConnectionUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("delete from user where id = ?");
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void update(User user) {
        try {
            Connection connection = HikariCPConnectionUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("update user set name = ? where id = ?");
            preparedStatement.setString(1, user.getName());
            preparedStatement.setLong(2, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public User getById(Long id) {
        try {
            Connection connection = ConnectionUtil.connection.get();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from user where id = ?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return new User(resultSet.getLong(1), resultSet.getString(2));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
