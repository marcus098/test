package com.example.authservice.resources;

import com.example.authservice.model.User;
import com.example.authservice.requests.RegisterUser;
import com.example.authservice.service.UserService;
import com.example.commonlib.dto.UserDto;
import com.example.commonlib.request.LoginRequest;
import com.example.commonlib.response.AuthResponse;
import com.example.commonlib.security.JwtUtils;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    private UserService userService;

    @Inject
    private JwtUtils jwtUtils;

    @Inject
    private PasswordEncoder passwordEncoder;

    @POST
    @Path("/register")
    public Response register(@Valid RegisterUser registerUser) {
        registerUser.setPassword(passwordEncoder.encode(registerUser.getPassword()));
        User user = userService.saveUser(registerUser);

        if (user == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("User could not be created").build();
        }

        return Response.ok("registrato con successo").build();
    }

    @POST
    @Path("/login")
    public Response login(@Valid LoginRequest loginRequest) {
        Optional<User> userOpt = userService.findByUsername(loginRequest.getUsername());

        if (userOpt.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), userOpt.get().getPassword())) {
            User user = userOpt.get();
            String token = jwtUtils.generateJwtToken(user.getId(), user.getRole(), user.getLocation().getX(), user.getLocation().getY());

            AuthResponse response = new AuthResponse(token,
                    new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole()));

            return Response.ok(response).build();
        }

        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("Invalid credentials").build();
    }
}
