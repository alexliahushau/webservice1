package com.mentoring.simplewebservice;

import com.mentoring.simplewebservice.domain.User;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.sql.DriverManager;
import java.sql.Statement;

import static junit.framework.Assert.assertEquals;

public class UserResourceTest {

    private HttpServer server;
    private WebTarget target;
    private User expectedUser;

    @Before
    public void setUp() throws Exception {
        Application.AppInit();
        server = Application.server;
        final Client c = ClientBuilder.newClient();

        target = c.target(Application.BASE_URI);

        expectedUser = new User();
        expectedUser.setLogin("alex001");
        expectedUser.setFirstName("alex");
        expectedUser.setLastName("derby");
        expectedUser.setEmail("alex@yahoo.com");

    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
        Application.dataSource
                .getConnection()
                .createStatement()
                .execute("DROP TABLE USERS");
    }

    @Test
    public void testGetUserNotExist() {
        final Response response = target.path("api/v1/user/not_exixting_user").request().get();
        final int status = response.getStatus();
        final String message = response.readEntity(String.class);

        assertEquals(404, status);
        assertEquals("User not found.", message);
    }

    @Test
    public void testGetExistUser() {
        final Response response = target.path("api/v1/user/alex001").request().get();
        final int status = response.getStatus();
        final User actual = response.readEntity(User.class);

        assertEquals(200, status);
        assertEquals(expectedUser, actual);
    }

    @Test
    public void testCreateUser() {
        final User newUser = new User();
        newUser.setLogin("newUser001");
        newUser.setFirstName("newFirst");
        newUser.setLastName("newsLast");
        newUser.setEmail("new@yahoo.com");

        final Entity<User> entity = Entity.entity(newUser, MediaType.APPLICATION_XML);
        final Response response = target.path("api/v1/user").request().post(entity);
        final int status = response.getStatus();
        final User actual = response.readEntity(User.class);

        assertEquals(201, status);
        assertEquals(newUser, actual);
    }

    @Test
    public void testUpdateExistUser() {
        final User updateUser = new User();
        updateUser.setFirstName("Peter");
        updateUser.setLastName("H2");
        updateUser.setEmail("Peter@yahoo.com");

        final Entity<User> entity = Entity.entity(updateUser, MediaType.APPLICATION_XML);
        final Response response = target.path("api/v1/user/alex001").request().put(entity);
        final int status = response.getStatus();
        final User actual = response.readEntity(User.class);

        expectedUser.setFirstName("updateFirstName");

        assertEquals(202, status);
        assertEquals(updateUser, actual);
    }

    @Test
    public void testDeleteUser() {
        final int deleteStatus = target.path("api/v1/user/alex001").request().delete().getStatus();
        final int getStatus = target.path("api/v1/user/alex001").request().get().getStatus();

        assertEquals(202, deleteStatus);
        assertEquals(404, getStatus);
    }


}
