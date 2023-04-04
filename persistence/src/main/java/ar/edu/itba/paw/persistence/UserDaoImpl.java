package ar.edu.itba.paw.persistence;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.models.User;

@Repository
public class UserDaoImpl implements UserDao {

    @Override
    public User create(String email, String password) {
        return new User(email, password);
    }

    @Override
    public String getEmail(int id){return "sballerini@itba.edu.ar";}
    //for testing purposes the static email will be sballerini@itba.edu.ar for the time being
}


