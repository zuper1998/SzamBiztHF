package com.backend.backend;

import com.backend.backend.Repository.RoleRepository;
import com.backend.backend.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BackendApplicationTests {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Test
    void contextLoads() {
        assertThat(roleRepository).isNotNull();
        assertThat(userRepository).isNotNull();
        assertThat(encoder).isNotNull();
    }

}
