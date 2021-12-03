package com.backend.backend.Controller;

import com.backend.backend.Repository.LogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LogControllerTests {
    @Autowired
    private LogRepository logRepository;

    @Test
    public void contextLoads() {
        assertThat(logRepository).isNotNull();
    }
}
