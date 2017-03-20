package com.mentoring.simplewebservice.repository;

import com.mentoring.simplewebservice.Application;
import com.mentoring.simplewebservice.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.mentoring.simplewebservice.Application.jdbcTemplate;

/**
 * Created by Aliaksandr_Liahushau on 3/18/2017.
 */
public class UserRepositoryImpl implements UserRepository {
    final static Logger LOG = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @Context
    Request request;

    @Inject
    private NamedParameterJdbcTemplate template;

    @Override
    public Optional<User> save(User user) {
        final String SQL = "INSERT INTO Users (login, firstName, lastName, email) VALUES (:login, :firstName, :lastName, :email)";
        final Map namedParameters = new HashMap();
        namedParameters.put("login", user.getLogin());
        namedParameters.put("firstName", user.getFirstName());
        namedParameters.put("lastName", user.getLastName());
        namedParameters.put("email", user.getEmail());

        int result = 0;
        try {
            result = template.update(SQL, namedParameters);
        } catch (final DataAccessException ex) {
            LOG.error(ex.getMessage());
        }

        return Optional.ofNullable(result == 1 ? user : null);
    }

    @Override
    public Optional<User> update(String login, User user) {
        String SQL = "UPDATE Users SET firstName = :firstName, lastName = :lastName, email = :email WHERE login = :login";
        final Map namedParameters = new HashMap();
        namedParameters.put("login", login);
        namedParameters.put("firstName", user.getFirstName());
        namedParameters.put("lastName", user.getLastName());
        namedParameters.put("email", user.getEmail());

        int result = 0;
        try {
            result = template.update(SQL, namedParameters);
        } catch (final DataAccessException ex) {
            LOG.error(ex.getMessage());
        }

        return Optional.ofNullable(result == 1 ? user : null);
    }

    @Override
    public void delete(String login) {
        String SQL = "DELETE FROM Users WHERE login = :login";
        final Map namedParameters = new HashMap();
        namedParameters.put("login", login);

        int result = 0;
        try {
            result = template.update(SQL, namedParameters);
        } catch (final DataAccessException ex) {
            LOG.error(ex.getMessage());
        }
    }

    @Override
    public Optional<User> findOneByLogin(final String login) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("login", login);

        final String sql = "SELECT * FROM users WHERE login=:login";
        User user = null;

        try {
            user = template.queryForObject(
                    sql,
                    params,
                    new UserMapper());
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }

        return Optional.ofNullable(user);
    }

    @Override
    public Optional<List<User>> findAll() {
        List<User> users = null;

        final String sql = "SELECT * FROM users";
        
        try {
            users = template.query(sql, new UserMapper());
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }

        return Optional.ofNullable(users);
    }

    @Override
    public void uploadUserLogo(String login, byte[] image) {
        final String SQL = "UPDATE Users SET image = :image WHERE login = :login";
        final Map namedParameters = new HashMap();
        namedParameters.put("login", login);
        namedParameters.put("image", new SqlParameterValue(Types.BLOB, new SqlLobValue(image, new DefaultLobHandler())));

        int ser = 0;
        try {
            ser = template.update(SQL, namedParameters);
        } catch (final Exception ex) {
            LOG.error(ex.getMessage());
        }

        LOG.debug(String.valueOf(ser));
    }

    private static final class UserMapper implements RowMapper<User> {

        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setLogin(rs.getString("login"));
            user.setFirstName(rs.getString("firstName"));
            user.setLastName(rs.getString("lastName"));
            user.setEmail(rs.getString("email"));
            user.setImage(rs.getBytes("image"));
            return user;
        }
    }
}
