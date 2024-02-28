package org.quarkus.resource;

import io.vertx.ext.auth.User;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.annotation.security.RolesAllowed;
import org.quarkus.dto.UserDto;
import org.quarkus.entity.UserEntity;

import org.quarkus.service.UserService;

import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    @GET
    @PermitAll
    @Path("/listAll")
    public Response listAll(){
        List<UserEntity> list = userService.listAllUser();
        return Response.ok(list).build();
    }

    @GET
    @PermitAll
    @Path("/getUser/{id}")
    public Response getUserById(@PathParam("id") Integer id) {
        UserEntity user = userService.getUserById(id);
        return Response.ok(user).build();
    }

    @POST
//    @RolesAllowed({"Admin"})
    @Path("/add")
    public Response addUser(UserDto dto){
        UserEntity user = userService.addUser(dto);
        return Response.ok(user).status(201).build();
    }

    @PUT
    @Path("/update/{id}")
    public Response updateUser(@PathParam("id") Integer id, UserDto dto) {
        userService.updateUser(id, dto);
        return Response.ok("Edit Success").build();
    }

    @DELETE
//    @RolesAllowed("Admin")
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Integer id){
        userService.removeUser(id);
        return Response.ok("Delete Success").build();
    }
}
