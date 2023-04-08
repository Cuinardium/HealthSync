package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface UserService {
    User createUser(String email, String password);

    String getEmail(int id);

    Optional<User> findById(long id);
} 
