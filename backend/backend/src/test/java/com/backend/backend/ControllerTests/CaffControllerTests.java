package com.backend.backend.Controller;

import com.backend.backend.Repository.CaffRepository;
import com.backend.backend.Repository.LogRepository;
import com.backend.backend.Repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CaffControllerTests {

    @Autowired
    private UserRepository ur;
    @Autowired
    private CaffRepository cr;
    @Autowired
    private LogRepository lr;

    @Test
    public void contextLoads() {
        assertThat(ur).isNotNull();
        assertThat(cr).isNotNull();
        assertThat(lr).isNotNull();
    }
}
