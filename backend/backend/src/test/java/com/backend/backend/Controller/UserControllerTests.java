package com.backend.backend.Controller;

import com.backend.backend.Repository.LogRepository;
import com.backend.backend.Repository.RoleRepository;
import com.backend.backend.Repository.UserRepository;
import com.backend.backend.Security.Auth.JwtUtils;
import com.backend.backend.Security.DataAccess.DataAccessAuth;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

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
}
