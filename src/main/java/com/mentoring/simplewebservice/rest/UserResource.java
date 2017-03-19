package com.mentoring.simplewebservice.rest;

import com.mentoring.simplewebservice.domain.User;
import com.mentoring.simplewebservice.service.UserService;
import com.sun.jersey.multipart.FormDataParam;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;

@Path("api/v1/user")
public class UserResource {
    final static Logger LOG = LoggerFactory.getLogger(UserResource.class);

    @Inject
    UserService userService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        return userService.findAll()
            .map(users -> {
                return Response.ok(users).build();
            })
            .orElse(Response.status(404).entity("Users not found.").build());
    }

    @GET
    @Path("{login}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("login") final String login) {
        return userService.findByLogin(login)
            .map(u -> Response.ok(u).build())
            .orElse(Response.status(404).entity("User not found.").build());
    }

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(User user) {
        return userService.save(user)
            .map(u -> Response.status(201).entity(u).build())
            .orElse(Response.status(400).entity("User does not created.").build());
    }

    @PUT
    @Path("/{login}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("login") String login, User user) {
        return userService.update(login, user)
            .map(u -> Response.status(202).entity(u).build())
            .orElse(Response.noContent().entity("User update failed.").build());
    }

    @DELETE
    @Path("/{login}")
    public Response delete(@PathParam("login") String login) {
        try {
            userService.delete(login);
            return Response.status(202).entity("User deleted successfully.").build();
        } catch (Exception ex) {
            return Response.noContent().entity("User update failed.").build();
        }
    }

    @POST
    @Path("/{login}/uploadlogo")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadUserLogo(@PathParam("login") String login, @FormDataParam("file") InputStream uploadedInputStream) {
        try {
            userService.uploadUserLogo(login, IOUtils.toByteArray(uploadedInputStream));
            return Response.status(200).entity("Logo successfully uploaded.").build();
        } catch (Exception e) {

            return Response.status(500).entity("Logo uploading failed.").build();
        }


    }
}
