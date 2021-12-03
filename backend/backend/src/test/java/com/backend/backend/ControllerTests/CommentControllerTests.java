package com.backend.backend.Controller;

import com.backend.backend.Repository.CaffRepository;
import com.backend.backend.Repository.CommentRepository;
import com.backend.backend.Repository.LogRepository;
import com.backend.backend.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CommentControllerTests {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CaffRepository caffRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LogRepository logRepository;

    @Test
    public void contextLoads() {
        assertThat(commentRepository).isNotNull();
        assertThat(caffRepository).isNotNull();
        assertThat(userRepository).isNotNull();
        assertThat(logRepository).isNotNull();
    }
}
