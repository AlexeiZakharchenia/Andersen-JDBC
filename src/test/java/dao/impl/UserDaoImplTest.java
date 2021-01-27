package dao.impl;


import dao.UserDao;
import entity.User;
import org.junit.Assert;


public class UserDaoImplTest {
    UserDao userDao = new UserDaoImpl();

    @org.junit.Test
    public void saveAndGet() {
        Integer old = userDao.getAll().size();
        User user = new User(23, "Alexei", 20, "admin");
        userDao.save(user);

        Assert.assertEquals(1, userDao.getAll().size() - old);
        Assert.assertEquals(user, userDao.get(user.getId()));
        userDao.delete(user.getId());
    }


    @org.junit.Test
    public void delete() {

        User user = new User(23, "Alexei", 20, "admin");
        userDao.save(user);
        Integer old = userDao.getAll().size();
        userDao.delete(user.getId());

        Assert.assertEquals(1, old - userDao.getAll().size());

    }

    @org.junit.Test
    public void update() {
        User user = new User(23, "Aleexei", 20, "admin");
        userDao.save(user);
        user.setAge(22);
        user.setRole("moderator");
        user.setName("Alexei");

        userDao.update(user);


        Assert.assertEquals(user, userDao.get(user.getId()));

        userDao.delete(user.getId());
    }

}