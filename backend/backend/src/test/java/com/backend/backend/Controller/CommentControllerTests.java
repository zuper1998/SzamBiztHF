package com.backend.backend.Controller;

import com.backend.backend.Communication.Request.AddCommentRequest;
import com.backend.backend.Communication.Request.UpdateCommentRequest;
import com.backend.backend.Repository.CaffRepository;
import com.backend.backend.Repository.CommentRepository;
import com.backend.backend.Repository.LogRepository;
import com.backend.backend.Repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

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

    @Test
    public void addCommentUnauthenticated() {
        AddCommentRequest addCommentRequest = new AddCommentRequest();
        UUID uuid = new UUID(0, 1);
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> commentController.addComment(addCommentRequest, uuid));
    }

    @Test
    public void getCaffCommentsUnauthenticated() {
        UUID uuid = new UUID(0, 1);
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> commentController.getCaffComments(uuid));
    }

    @Test
    public void updateCommentUnauthenticated() {
        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest();
        UUID uuid = new UUID(0, 1);
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> commentController.updateComment(updateCommentRequest, uuid));
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void updateCommentUserRole() {
        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest();
        UUID uuid = new UUID(0, 1);
        Assertions.assertThrows(AccessDeniedException.class, () -> commentController.updateComment(updateCommentRequest, uuid));
    }

    @Test
    public void deleteCommentUnauthenticated() {
        UUID uuid = new UUID(0, 1);
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> commentController.deleteComment(uuid));
    }

    @Test
    public void deleteCommentUserProfile() {
        UUID uuid = new UUID(0, 1);
        Assertions.assertThrows(AccessDeniedException.class, () -> commentController.deleteComment(uuid));
    }
}
