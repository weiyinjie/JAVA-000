package org.example.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TestJDBC {


    public static void main(String[] args) {
        TestJDBC testJDBC = new TestJDBC();
        System.out.println("before insert: " + testJDBC.getUserList());
        Long id = testJDBC.insert("before");
        System.out.println("new record id:" + id);
        System.out.println("after insert: " + testJDBC.getUserList());
        User user = new User(id, "after");
        testJDBC.update(user);
        System.out.println("update before to after where id = " + id);
        System.out.println("after update: " + testJDBC.getUserList());
        testJDBC.delete(id);
        System.out.println("after delete: " + testJDBC.getUserList());

    }

    public void delete(Long id) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("delete from user where id = ?");
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void update(User user) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("update user set name = ? where id = ?");
            preparedStatement.setString(1, user.getName());
            preparedStatement.setLong(2, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Long insert(String name) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "insert into user (name) values (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            while (generatedKeys.next()) {
                return generatedKeys.getLong(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public List<User> getUserList() {

        List<User> result = null;
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "select * from user";
            result = new ArrayList<>();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                result.add(new User(resultSet.getLong(1), resultSet.getString(2)));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }

}
