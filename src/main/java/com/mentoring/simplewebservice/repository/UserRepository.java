package com.mentoring.simplewebservice.repository;

import com.mentoring.simplewebservice.domain.User;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> save(User user);
    Optional<User> update(String login, User user);
    void delete(String user);
    Optional<User> findOneByLogin(String login);
    Optional<List<User>> findAll();
    void uploadUserLogo(String login, byte[] image);
}
