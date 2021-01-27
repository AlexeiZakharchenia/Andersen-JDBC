package dao.impl;

import connection.JDBCConnection;
import dao.UserDao;
import entity.User;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private final String getQuery = "SELECT users.id, users.name, users.age, roles.name as role " +
            "FROM users " +
            "join users_roles on users.id = users_roles.user_id " +
            "join roles on users_roles.role_id = roles.id " +
            "WHERE users.id = ?";

    private final String getAllQuery = "SELECT users.id, users.name, users.age, roles.name as role " +
            "FROM users " +
            "join users_roles on users.id = users_roles.user_id " +
            "join roles on users_roles.role_id = roles.id ";

    private final String deleteQuery = "DELETE " +
            "FROM users " +
            "WHERE id = ?;";

    private final String saveQuery = "INSERT into users(id, name, age) " +
            "values(?,?,?)";

    private final String getIdOfRoleByName = "Select id from roles where name = ?";

    private final String saveUserRole = "INSERT into users_roles(user_id, role_id) " +
            "values(?,?)";

    private final String updateAgeAndName = "UPDATE users SET name=?,age=? where id=?";

    private final String updateUserRole = "UPDATE users_roles SET role_id = ? where user_id=?";



@Override
    public User get(Integer id) {
        try (Connection connection = JDBCConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(getQuery);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                return getPersonFromResultSet(resultSet);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        try (Connection connection = JDBCConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(getAllQuery);

            ResultSet resultSet = statement.executeQuery();
            List<User> list = new LinkedList<>();

            while(resultSet.next()){
                list.add(getPersonFromResultSet(resultSet));
            }
            return list;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new LinkedList<>();
    }

    @Override
    public void delete(Integer id){
        try (Connection connection = JDBCConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(deleteQuery);
            statement.setInt(1, id);
            statement.execute();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(User user){
        try (Connection connection = JDBCConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(getIdOfRoleByName);
            statement.setString(1, user.getRole());
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                int roleId = resultSet.getInt("id");

                statement = connection.prepareStatement(updateAgeAndName);
                statement.setString(1, user.getName());
                statement.setInt(2, user.getAge());
                statement.setInt(3, user.getId());
                statement.execute();

                statement = connection.prepareStatement(updateUserRole);
                statement.setInt(1, roleId);
                statement.setInt(2, user.getId());
                statement.execute();

            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(User user) {
        try (Connection connection = JDBCConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(getIdOfRoleByName);
            statement.setString(1, user.getRole());
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                int roleId = resultSet.getInt("id");

                statement = connection.prepareStatement(saveQuery);
                statement.setInt(1, user.getId());
                statement.setString(2, user.getName());
                statement.setInt(3, user.getAge());
                statement.execute();

                statement = connection.prepareStatement(saveUserRole);
                statement.setInt(1, user.getId());
                statement.setInt(2, roleId);
                statement.execute();
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private User getPersonFromResultSet(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("id");
        Integer age = resultSet.getInt("age");
        String name = resultSet.getString("name");
        String role = resultSet.getString("role");
        return new User(id, name,age,role);
    }

}
