package ar.edu.itba.paw.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.UserDao;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User createUser(String email, String password) {
        return userDao.create(email, password);
    }

    @Override
    public String getEmail(int id){return userDao.getEmail(id);}

    @Override
    public Optional<User> findById(long id) {
        return userDao.findById(id);
    }


}
