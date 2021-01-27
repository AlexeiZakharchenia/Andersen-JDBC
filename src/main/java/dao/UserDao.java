package dao;

import entity.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    User get(Integer id) ;
    List<User> getAll();
    void delete(Integer id);
    void update(User user);
    void save(User user);
}
