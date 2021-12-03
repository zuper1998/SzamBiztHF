package com.backend.backend.Controller;

import com.backend.backend.Repository.LogRepository;
import com.backend.backend.Repository.RoleRepository;
import com.backend.backend.Repository.UserRepository;
import com.backend.backend.Security.Auth.JwtUtils;
import com.backend.backend.Security.DataAccess.DataAccessAuth;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        assertNotNull(ur);
        assertNotNull(roleRepository);
        assertNotNull(logRepository);
        assertNotNull(authenticationManager);
        assertNotNull(userRepository);
        assertNotNull(jwtUtils);
        assertNotNull(encoder);
        assertNotNull(dataAccessAuth);
    }

    @Test
    public void controllerLoads() {
        assertNotNull(userController);
    }
}
