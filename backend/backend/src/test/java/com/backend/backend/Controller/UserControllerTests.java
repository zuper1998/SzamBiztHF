package com.backend.backend.Controller;

import com.backend.backend.Communication.Request.LoginRequest;
import com.backend.backend.Communication.Request.SignUpRequest;
import com.backend.backend.Communication.Request.UpdateUserRequest;
import com.backend.backend.Data.Role;
import com.backend.backend.Data.RoleEnum;
import com.backend.backend.Data.User;
import com.backend.backend.Repository.LogRepository;
import com.backend.backend.Repository.RoleRepository;
import com.backend.backend.Repository.UserRepository;
import com.backend.backend.Security.Auth.JwtUtils;
import com.backend.backend.Security.DataAccess.DataAccessAuth;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.Set;
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

    @WithMockUser(roles = { "ADMIN" }, username = "admin", password = "admin")
    @Test
    public void loginAdminBadCredentials() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("user");
        loginRequest.setPassword("user");
        Assertions.assertNotEquals("admin", loginRequest.getUsername());
        Assertions.assertNotEquals("admin", loginRequest.getPassword());
        Assertions.assertThrows(BadCredentialsException.class, () -> userController.loginAdmin(loginRequest));
    }

    @WithMockUser(roles = { "ADMIN" }, username = "admin", password = "admin")
    @Test
    public void loginAdminGoodCredentials() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("admin");
        Assertions.assertEquals("admin", loginRequest.getUsername());
        Assertions.assertEquals("admin", loginRequest.getPassword());
        Assertions.assertDoesNotThrow(() -> userController.loginAdmin(loginRequest));
    }

    @WithMockUser(roles = { "USER" }, username = "admin", password = "admin")
    @Test
    public void loginUserBadCredentials() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("user");
        loginRequest.setPassword("user");
        Assertions.assertNotEquals("admin", loginRequest.getUsername());
        Assertions.assertNotEquals("admin", loginRequest.getPassword());
        Assertions.assertThrows(BadCredentialsException.class, () -> userController.loginUser(loginRequest));
    }

    @WithMockUser(roles = { "USER" }, username = "admin", password = "admin")
    @Test
    public void loginUserGoodCredentials() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("admin");
        Assertions.assertEquals("admin", loginRequest.getUsername());
        Assertions.assertEquals("admin", loginRequest.getPassword());
        Assertions.assertDoesNotThrow(() -> userController.loginUser(loginRequest));
    }

    @Test
    public void registrationNewCredentials() {
        SignUpRequest signUpMessage = new SignUpRequest();
        String newUserName = "newuser";
        String newEmail = "new@new.com";
        String newPassword = "newpassword";
        User newUser = new User(newUserName, newEmail, encoder.encode(newPassword), new ArrayList<>(), new ArrayList<>());
        newUser.setRoles(Set.of(new Role(RoleEnum.ROLE_USER)));
        signUpMessage.setUsername(newUserName);
        signUpMessage.setEmail(newEmail);
        signUpMessage.setPassword(newPassword);
        ResponseEntity<Void> responseEntity = userController.registration(signUpMessage);
        Assertions.assertEquals("newuser", newUser.getUsername());
        Assertions.assertEquals("new@new.com", newUser.getEmail());
        Assertions.assertTrue(encoder.matches("newpassword", newUser.getPassword()));
        Assertions.assertEquals("newuser", signUpMessage.getUsername());
        Assertions.assertEquals("new@new.com", signUpMessage.getEmail());
        Assertions.assertEquals("newpassword", signUpMessage.getPassword());
        Assertions.assertTrue(newUser.getRoles().stream().anyMatch(role -> role.getName().equals(RoleEnum.ROLE_USER)));
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void registrationExistingCredentials() {
        SignUpRequest signUpMessage = new SignUpRequest();
        String newUserName = "existing";
        String newEmail = "new@new.com";
        String newPassword = "existing";
        User newUser = new User(newUserName, newEmail, encoder.encode(newPassword), new ArrayList<>(), new ArrayList<>());
        newUser.setRoles(Set.of(new Role(RoleEnum.ROLE_USER)));
        signUpMessage.setUsername(newUserName);
        signUpMessage.setEmail(newEmail);
        signUpMessage.setPassword(newPassword);
        ResponseEntity<Void> responseEntity = userController.registration(signUpMessage);
        Assertions.assertEquals("existing", newUser.getUsername());
        Assertions.assertEquals("new@new.com", newUser.getEmail());
        Assertions.assertTrue(encoder.matches("existing", newUser.getPassword()));
        Assertions.assertEquals("existing", signUpMessage.getUsername());
        Assertions.assertEquals("new@new.com", signUpMessage.getEmail());
        Assertions.assertEquals("existing", signUpMessage.getPassword());
        Assertions.assertTrue(newUser.getRoles().stream().anyMatch(role -> role.getName().equals(RoleEnum.ROLE_USER)));
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        SignUpRequest signUpMessage2 = new SignUpRequest();
        String newUserName2 = "existing";
        String newEmail2 = "new@new.com";
        String newPassword2 = "existing";
        User newUser2 = new User(newUserName2, newEmail2, encoder.encode(newPassword2), new ArrayList<>(), new ArrayList<>());
        newUser2.setRoles(Set.of(new Role(RoleEnum.ROLE_USER)));
        signUpMessage2.setUsername(newUserName2);
        signUpMessage2.setEmail(newEmail2);
        signUpMessage2.setPassword(newPassword2);
        ResponseEntity<Void> responseEntity2 = userController.registration(signUpMessage2);
        Assertions.assertEquals("existing", newUser2.getUsername());
        Assertions.assertEquals("new@new.com", newUser2.getEmail());
        Assertions.assertTrue(encoder.matches("existing", newUser2.getPassword()));
        Assertions.assertEquals("existing", signUpMessage2.getUsername());
        Assertions.assertEquals("new@new.com", signUpMessage2.getEmail());
        Assertions.assertEquals("existing", signUpMessage2.getPassword());
        Assertions.assertTrue(newUser2.getRoles().stream().anyMatch(role -> role.getName().equals(RoleEnum.ROLE_USER)));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity2.getStatusCode());
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
