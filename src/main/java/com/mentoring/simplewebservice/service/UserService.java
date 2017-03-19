package com.mentoring.simplewebservice.service;

import com.mentoring.simplewebservice.domain.User;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * Created by Aliaksandr_Liahushau on 3/18/2017.
 */
public interface UserService {
    Optional<User> save(final User user);
    Optional<User> update(final String login, final User user);
    Optional<User> findByLogin(final String login);
    Optional<List<User>> findAll();
    void delete(final String login);
    void uploadUserLogo(String login, byte[] image);
}
