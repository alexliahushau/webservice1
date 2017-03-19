package com.mentoring.simplewebservice.configuration.binding;

import com.mentoring.simplewebservice.Application;
import com.mentoring.simplewebservice.repository.UserRepository;
import com.mentoring.simplewebservice.repository.UserRepositoryImpl;
import com.mentoring.simplewebservice.service.UserService;
import com.mentoring.simplewebservice.service.UserServiceImpl;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by Aliaksandr_Liahushau on 3/18/2017.
 */
public class DependencyBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(Application.dataSource).to(DataSource.class);
        bind(Application.jdbcTemplate).to(NamedParameterJdbcTemplate.class);
        bind(UserServiceImpl.class).to(UserService.class);
        bind(UserRepositoryImpl.class).to(UserRepository.class);
    }
}
