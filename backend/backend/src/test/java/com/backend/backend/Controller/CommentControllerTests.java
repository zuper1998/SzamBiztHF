package com.backend.backend.Controller;

import com.backend.backend.Repository.CaffRepository;
import com.backend.backend.Repository.CommentRepository;
import com.backend.backend.Repository.LogRepository;
import com.backend.backend.Repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        Assertions.assertNotNull(commentRepository);
        Assertions.assertNotNull(caffRepository);
        Assertions.assertNotNull(userRepository);
        Assertions.assertNotNull(logRepository);
    }

    @Test
    public void controllerLoads() {
        Assertions.assertNotNull(commentController);
    }
}
