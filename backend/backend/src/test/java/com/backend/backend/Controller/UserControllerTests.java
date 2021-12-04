package com.backend.backend.Controller;

import com.backend.backend.Communication.Request.SignUpRequest;
import com.backend.backend.Communication.Request.UpdateUserRequest;
import com.backend.backend.Repository.LogRepository;
import com.backend.backend.Repository.RoleRepository;
import com.backend.backend.Repository.UserRepository;
import com.backend.backend.Security.Auth.JwtUtils;
import com.backend.backend.Security.DataAccess.DataAccessAuth;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

@SpringBootTest
public class UserControllerTests {
    @Autowired
    private UserRepository ur;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private DataAccessAuth dataAccessAuth;

    @Autowired
    private UserController userController;

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(ur);
        Assertions.assertNotNull(roleRepository);
        Assertions.assertNotNull(logRepository);
        Assertions.assertNotNull(authenticationManager);
        Assertions.assertNotNull(userRepository);
        Assertions.assertNotNull(jwtUtils);
        Assertions.assertNotNull(encoder);
        Assertions.assertNotNull(dataAccessAuth);
    }

    @Test
    public void controllerLoads() {
        Assertions.assertNotNull(userController);
    }

    @Test
    public void userUpdateUserUnauthenticated() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        UUID uuid = new UUID(0, 1);
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> userController.userUpdateUser(updateUserRequest, uuid));
    }

    @Test
    public void adminUpdateUserUnauthenticated() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        UUID uuid = new UUID(0, 1);
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> userController.adminUpdateUser(updateUserRequest, uuid));
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void adminUpdateUserUserRole() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        UUID uuid = new UUID(0, 1);
        Assertions.assertThrows(AccessDeniedException.class, () -> userController.adminUpdateUser(updateUserRequest, uuid));
    }

    @Test
    public void adminAddUserUnauthenticated() {
        SignUpRequest signUpRequest = new SignUpRequest();
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> userController.adminAddUser(signUpRequest));
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void adminAddUserUserRole() {
        SignUpRequest signUpRequest = new SignUpRequest();
        Assertions.assertThrows(AccessDeniedException.class, () -> userController.adminAddUser(signUpRequest));
    }

    @Test
    public void deleteUserUnauthenticated() {
        UUID uuid = new UUID(0, 1);
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> userController.deleteUser(uuid));
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void deleteUserUserRole() {
        UUID uuid = new UUID(0, 1);
        Assertions.assertThrows(AccessDeniedException.class, () -> userController.deleteUser(uuid));
    }

    @Test
    public void getAllUnauthenticated() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> userController.getAll());
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void getAllUserRole() {
        Assertions.assertThrows(AccessDeniedException.class, () -> userController.getAll());
    }
}
