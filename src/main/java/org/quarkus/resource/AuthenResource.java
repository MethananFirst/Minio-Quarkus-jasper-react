package org.quarkus.resource;

import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.Claims;
import org.quarkus.entity.UserEntity;
import org.quarkus.repository.UserRepository;

@Path("/auth")
public class AuthenResource {
    @Inject
    UserRepository userRepository; // Inject the UserRepository

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(UserEntity credentials) {
        // Check if credentials are valid
        UserEntity user = userRepository.findByUserNameAndPassword(credentials.getUserName(), credentials.getPassword());
        if (user != null) {
            String accessToken = generateToken(user.getUserName(), user.getRole());
            String refreshToken = generateRefreshToken(user.getUserName());
            JsonObject tokenJson = Json.createObjectBuilder()
                    .add("accessToken", accessToken)
                    .add("refreshToken", refreshToken).build();
            return Response.ok(tokenJson).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    // Method to generate JWT token
    private String generateToken(String userName, String roles) {
        return Jwt.issuer("https://example.com/issuer")
                .upn(userName)
                .claim(Claims.groups.name(), roles)
                .claim(Claims.birthdate.name(), "2001-07-13")
                .sign();
    }

    private String generateRefreshToken(String userName) {
        return Jwt.issuer("https://example.com/issuer")
                .upn(userName)
                .expiresAt(System.currentTimeMillis() + (24 * 60 * 60 * 1000)) // 24 hours
                .claim("refresh", true)
                .sign();
    }

//    @POST
//    @Path("/refresh")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response refreshToken(JsonObject requestBody) {
//        try {
//            String refreshToken = requestBody.getString("refreshToken");
//            if (isValidRefreshToken(refreshToken)) {
//                String newAccessToken = generateToken(userRepository.findby);
//                return Response.ok(Json.createObjectBuilder().add("accessToken", newAccessToken).build()).build();
//            } else {
//                return Response.status(Response.Status.UNAUTHORIZED).build();
//            }
//        } catch (Exception e) {
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    private boolean isValidRefreshToken(String refreshToken) {
//        try {
//            return refreshToken != null;
//        } catch (Exception e) {
//            return false;
//        }
//    }
}
