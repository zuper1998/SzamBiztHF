package com.backend.backend.Controller;

import com.backend.backend.Communication.Request.LoginRequest;
import com.backend.backend.Communication.Request.SignUpRequest;
import com.backend.backend.Communication.Request.UpdateUserRequest;
import com.backend.backend.Communication.Response.GetAllUserResponse;
import com.backend.backend.Data.Role;
import com.backend.backend.Data.RoleEnum;
import com.backend.backend.Data.User;
import com.backend.backend.Repository.LogRepository;
import com.backend.backend.Repository.RoleRepository;
import com.backend.backend.Repository.UserRepository;
import com.backend.backend.Security.Auth.JwtUtils;
import com.backend.backend.Security.DataAccess.DataAccessAuth;
import com.backend.backend.Security.UserDetailsImpl;
import org.junit.BeforeClass;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.List;
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

    private SecurityContext securityContext;

    @BeforeClass
    private void initTests(){
        securityContext = SecurityContextHolder.createEmptyContext();
        SecurityContextHolder.setContext(securityContext);
    }

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

    public User registrateUser(String username, String email, String password, Set<Role> roles) {
        User newUser = new User(username, email, encoder.encode(password), new ArrayList<>(), new ArrayList<>());
        newUser.setRoles(roles);
        SignUpRequest signUpMessage = new SignUpRequest();
        signUpMessage.setUsername(username);
        signUpMessage.setEmail(email);
        signUpMessage.setPassword(password);
        userController.registration(signUpMessage);
        return newUser;
    }

    public void deleteUser(UUID id) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("admin");
        userController.loginAdmin(loginRequest);
        userController.deleteUser(id);
    }

    public void setSecurityContext(String username, String password) {
        SecurityContextHolder.clearContext();
        securityContext = SecurityContextHolder.createEmptyContext();
        SecurityContextHolder.setContext(securityContext);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication auth = authenticationManager.authenticate(token);
        securityContext.setAuthentication(new InteractiveAuthenticationSuccessEvent(auth, this.getClass()).getAuthentication());
    }

    @Test
    public void controllerLoads() {
        Assertions.assertNotNull(userController);
    }

    @Test
    public void loginAdminBadCredentials() {
        registrateUser("admin", "admin@admin.com", "admin", Set.of(new Role(RoleEnum.ROLE_USER), new Role(RoleEnum.ROLE_ADMIN)));
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("notadmin");
        loginRequest.setPassword("notadmin");
        Assertions.assertNotEquals("admin", loginRequest.getUsername());
        Assertions.assertNotEquals("admin", loginRequest.getPassword());
        Assertions.assertThrows(BadCredentialsException.class, () -> userController.loginAdmin(loginRequest));
    }

    @Test
    public void loginAdminGoodCredentials() {
        registrateUser("admin", "admin@admin.com", "admin", Set.of(new Role(RoleEnum.ROLE_USER), new Role(RoleEnum.ROLE_ADMIN)));
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("admin");
        Assertions.assertEquals("admin", loginRequest.getUsername());
        Assertions.assertEquals("admin", loginRequest.getPassword());
        Assertions.assertDoesNotThrow(() -> userController.loginAdmin(loginRequest));
    }

    @Test
    public void loginUserBadCredentials() {
        registrateUser("user", "user@user.com", "user", Set.of(new Role(RoleEnum.ROLE_USER)));
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("notuser");
        loginRequest.setPassword("notuser");
        Assertions.assertNotEquals("user", loginRequest.getUsername());
        Assertions.assertNotEquals("user", loginRequest.getPassword());
        Assertions.assertThrows(BadCredentialsException.class, () -> userController.loginUser(loginRequest));
    }

    @Test
    public void loginUserGoodCredentials() {
        registrateUser("user", "user@user.com", "user", Set.of(new Role(RoleEnum.ROLE_USER)));
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("user");
        loginRequest.setPassword("user");
        Assertions.assertEquals("user", loginRequest.getUsername());
        Assertions.assertEquals("user", loginRequest.getPassword());
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
        setSecurityContext("newuser", "newpassword");
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        deleteUser(userDetails.getId());
    }

    @Test
    public void registrationExistingCredentials() {
        registrateUser("existing", "existing@existing.com", "existing", Set.of(new Role(RoleEnum.ROLE_USER)));
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
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
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

    @Test
    public void userUpdateUserAuthenticated() {
        User newUser = registrateUser("user", "user@user.com", "user", Set.of(new Role(RoleEnum.ROLE_USER)));
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setEmail("newemail@newemail.com");
        updateUserRequest.setPassword("newpassword");
        setSecurityContext("user", "user");
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseEntity<String> responseEntity = userController.userUpdateUser(updateUserRequest, userDetails.getId());
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        deleteUser(userDetails.getId());
    }

    @Test
    public void userUpdateOtherUser() {
        User user = registrateUser("user", "user@user.com", "user", Set.of(new Role(RoleEnum.ROLE_USER)));
        User otherUser = registrateUser("otheruser", "otheruser@otheruser.com", "otheruser", Set.of(new Role(RoleEnum.ROLE_USER)));
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setEmail("newemail@newemail.com");
        updateUserRequest.setPassword("newpassword");
        setSecurityContext("user", "user");
        ResponseEntity<String> responseEntity = userController.userUpdateUser(updateUserRequest, otherUser.getId());
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        deleteUser(userDetails.getId());
    }

    @Test
    public void userUpdateUserNoPassword() {
        User newUser = registrateUser("user", "user@user.com", "user", Set.of(new Role(RoleEnum.ROLE_USER)));
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setEmail("newemail@newemail.com");
        updateUserRequest.setPassword("");
        setSecurityContext("user", "user");
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseEntity<String> responseEntity = userController.userUpdateUser(updateUserRequest, userDetails.getId());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        deleteUser(userDetails.getId());
    }

    @Test
    public void adminUpdateUserAuthenticated() {
        registrateUser("admin", "admin@admin.com", "admin", Set.of(new Role(RoleEnum.ROLE_USER), new Role(RoleEnum.ROLE_ADMIN)));
        User newUser = registrateUser("user", "user@user.com", "user", Set.of(new Role(RoleEnum.ROLE_USER)));
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setEmail("newemail@newemail.com");
        updateUserRequest.setPassword("newpassword");
        setSecurityContext("user", "user");
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID id = userDetails.getId();
        setSecurityContext("admin", "admin");
        ResponseEntity<Void> responseEntity = userController.adminUpdateUser(updateUserRequest, id);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        deleteUser(id);
    }

    @Test
    public void adminUpdateUserEmailOnly() {
        registrateUser("admin", "admin@admin.com", "admin", Set.of(new Role(RoleEnum.ROLE_USER), new Role(RoleEnum.ROLE_ADMIN)));
        User newUser = registrateUser("user", "user@user.com", "user", Set.of(new Role(RoleEnum.ROLE_USER)));
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setEmail("newemail@newemail.com");
        updateUserRequest.setPassword("");
        setSecurityContext("user", "user");
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID id = userDetails.getId();
        setSecurityContext("admin", "admin");
        ResponseEntity<Void> responseEntity = userController.adminUpdateUser(updateUserRequest, id);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        deleteUser(id);
    }

    @Test
    public void adminUpdateUserNotExisting() {
        registrateUser("admin", "admin@admin.com", "admin", Set.of(new Role(RoleEnum.ROLE_USER), new Role(RoleEnum.ROLE_ADMIN)));
        User newUser = registrateUser("user", "user@user.com", "user", Set.of(new Role(RoleEnum.ROLE_USER)));
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setEmail("newemail@newemail.com");
        updateUserRequest.setPassword("");
        setSecurityContext("user", "user");
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID id = userDetails.getId();
        deleteUser(id);
        setSecurityContext("admin", "admin");
        ResponseEntity<Void> responseEntity = userController.adminUpdateUser(updateUserRequest, id);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        deleteUser(id);
    }

    @Test
    public void userUpdateUserNotExisting() {
        User newUser = registrateUser("user", "user@user.com", "user", Set.of(new Role(RoleEnum.ROLE_USER)));
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setEmail("newemail@newemail.com");
        updateUserRequest.setPassword("newpassword");
        setSecurityContext("user", "user");
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        deleteUser(userDetails.getId());
        ResponseEntity<String> responseEntity = userController.userUpdateUser(updateUserRequest, userDetails.getId());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
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

    @Test
    public void adminAddUserExisting() {
        registrateUser("admin", "admin@admin.com", "admin", Set.of(new Role(RoleEnum.ROLE_USER), new Role(RoleEnum.ROLE_ADMIN)));
        SignUpRequest signUpMessage = new SignUpRequest();
        String newUserName = "admin";
        String newEmail = "admin@admin.com";
        String newPassword = "admin";
        User newUser = new User(newUserName, newEmail, encoder.encode(newPassword), new ArrayList<>(), new ArrayList<>());
        newUser.setRoles(Set.of(new Role(RoleEnum.ROLE_USER)));
        signUpMessage.setUsername(newUserName);
        signUpMessage.setEmail(newEmail);
        signUpMessage.setPassword(newPassword);
        setSecurityContext("admin", "admin");
        ResponseEntity<Void> responseEntity = userController.adminAddUser(signUpMessage);
        Assertions.assertEquals("admin", newUser.getUsername());
        Assertions.assertEquals("admin@admin.com", newUser.getEmail());
        Assertions.assertTrue(encoder.matches("admin", newUser.getPassword()));
        Assertions.assertEquals("admin", signUpMessage.getUsername());
        Assertions.assertEquals("admin@admin.com", signUpMessage.getEmail());
        Assertions.assertEquals("admin", signUpMessage.getPassword());
        Assertions.assertTrue(newUser.getRoles().stream().anyMatch(role -> role.getName().equals(RoleEnum.ROLE_USER)));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void adminAddUserNoRoleProvided() {
        registrateUser("admin", "admin@admin.com", "admin", Set.of(new Role(RoleEnum.ROLE_USER), new Role(RoleEnum.ROLE_ADMIN)));
        SignUpRequest signUpMessage = new SignUpRequest();
        String newUserName = "newuser";
        String newEmail = "newemail@newemail.com";
        String newPassword = "newpassword";
        User newUser = new User(newUserName, newEmail, encoder.encode(newPassword), new ArrayList<>(), new ArrayList<>());
        newUser.setRoles(Set.of(new Role(RoleEnum.ROLE_USER)));
        signUpMessage.setUsername(newUserName);
        signUpMessage.setEmail(newEmail);
        signUpMessage.setPassword(newPassword);
        setSecurityContext("admin", "admin");
        ResponseEntity<Void> responseEntity = userController.adminAddUser(signUpMessage);
        Assertions.assertEquals("newuser", newUser.getUsername());
        Assertions.assertEquals("newemail@newemail.com", newUser.getEmail());
        Assertions.assertTrue(encoder.matches("newpassword", newUser.getPassword()));
        Assertions.assertEquals("newuser", signUpMessage.getUsername());
        Assertions.assertEquals("newemail@newemail.com", signUpMessage.getEmail());
        Assertions.assertEquals("newpassword", signUpMessage.getPassword());
        Assertions.assertTrue(newUser.getRoles().stream().anyMatch(role -> role.getName().equals(RoleEnum.ROLE_USER)));
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        setSecurityContext("newuser", "newpassword");
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        deleteUser(userDetails.getId());
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
    public void getAllRoleUser() {
        Assertions.assertThrows(AccessDeniedException.class, () -> userController.getAll());
    }

    @Test
    public void getAllRoleAdmin() {
        registrateUser("admin", "admin@admin.com", "admin", Set.of(new Role(RoleEnum.ROLE_USER), new Role(RoleEnum.ROLE_ADMIN)));
        setSecurityContext("admin", "admin");
        ResponseEntity<List<GetAllUserResponse>> responseEntity = userController.getAll();
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
