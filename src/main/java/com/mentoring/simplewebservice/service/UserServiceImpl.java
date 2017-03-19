package com.mentoring.simplewebservice.service;

import com.mentoring.simplewebservice.domain.User;
import com.mentoring.simplewebservice.repository.UserRepository;

import javax.inject.Inject;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * Created by Aliaksandr_Liahushau on 3/17/2017.
 */
public class UserServiceImpl implements UserService {

    @Inject
    UserRepository userRepository;

    public Optional<User> save(final User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> update(final String login, final User user) {
        return userRepository.update(login, user);
    }

    public Optional<User> findByLogin(final String login) {
        return userRepository.findOneByLogin(login);
    }

    @Override
    public Optional<List<User>> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void delete(final String login) {
        userRepository.delete(login);
    }

    @Override
    public void uploadUserLogo(final String login, final byte[] image) {
        userRepository.uploadUserLogo(login, image);
    }
}