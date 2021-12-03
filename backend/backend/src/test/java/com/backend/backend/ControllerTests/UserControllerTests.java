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

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    public void contextLoads() {
        assertThat(ur).isNotNull();
        assertThat(roleRepository).isNotNull();
        assertThat(logRepository).isNotNull();
        assertThat(authenticationManager).isNotNull();
        assertThat(userRepository).isNotNull();
        assertThat(jwtUtils).isNotNull();
        assertThat(encoder).isNotNull();
        assertThat(dataAccessAuth).isNotNull();
    }
}
