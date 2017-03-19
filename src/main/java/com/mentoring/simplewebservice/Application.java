package com.mentoring.simplewebservice;

import com.mentoring.simplewebservice.configuration.binding.DependencyBinder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URI;

public class Application {
    final static Logger LOG = LoggerFactory.getLogger(Application.class);
    public static final String BASE_URI = "http://localhost:8080/webservice/";
    public static DataSource dataSource;
    public static NamedParameterJdbcTemplate jdbcTemplate;
    public static HttpServer server;

    public static void startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("com.mentoring.simplewebservice");
        rc.register(new DependencyBinder());
        server = GrizzlyHttpServerFactory
                .createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void dataSourceInit() {
        final EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        final EmbeddedDatabase db = builder
                .setType(EmbeddedDatabaseType.DERBY)
                .addScript("create-db.sql")
                .addScript("insert-data.sql")
                .build();
        dataSource = db;
    }

    public static void jdbcTemplateInit() {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public static void AppInit() {
        dataSourceInit();
        jdbcTemplateInit();
        startServer();
    }

    public static void main(String[] args) throws IOException {
        AppInit();

        LOG.debug("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI);
        System.in.read();
        server.shutdown();
    }
}

