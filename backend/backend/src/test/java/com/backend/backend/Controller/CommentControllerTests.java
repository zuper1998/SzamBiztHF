package com.backend.backend.Controller;

import com.backend.backend.Repository.CaffRepository;
import com.backend.backend.Repository.CommentRepository;
import com.backend.backend.Repository.LogRepository;
import com.backend.backend.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @Autowired
    private CommentController commentController;

    @Test
    public void contextLoads() {
        assertNotNull(commentRepository);
        assertNotNull(caffRepository);
        assertNotNull(userRepository);
        assertNotNull(logRepository);
    }

    @Test
    public void controllerLoads() {
        assertNotNull(commentController);
    }
}
