package com.backend.backend.Controller;

import com.backend.backend.Repository.CaffRepository;
import com.backend.backend.Repository.LogRepository;
import com.backend.backend.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CaffControllerTests {

    @Autowired
    private UserRepository ur;
    @Autowired
    private CaffRepository cr;
    @Autowired
    private LogRepository lr;

    @Autowired
    private CaffController caffController;

    @Test
    public void contextLoads() {
        assertNotNull(ur);
        assertNotNull(cr);
        assertNotNull(lr);
    }

    @Test
    public void controllerLoads() {
        assertNotNull(caffController);
    }
}
