package com.backend.backend.Controller;

import com.backend.backend.Repository.LogRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@AutoConfigureMockMvc
@SpringBootTest
public class LogControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private LogController logController;

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(logRepository);
    }

    @Test
    public void controllerLoads() {
        Assertions.assertNotNull(logController);
    }

    @Test
    public void getLogUnauthenticated() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> logController.getLog());
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void getLogUserRole() {
        Assertions.assertThrows(AccessDeniedException.class, () -> logController.getLog());
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void getLogAdminRole() {
        var log = logController.getLog();
        Assertions.assertNotNull(log);
    }
}
