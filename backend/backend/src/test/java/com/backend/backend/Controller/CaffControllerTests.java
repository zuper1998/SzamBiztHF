package com.backend.backend.Controller;

import com.backend.backend.Communication.Request.SearchRequest;
import com.backend.backend.Repository.CaffRepository;
import com.backend.backend.Repository.LogRepository;
import com.backend.backend.Repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@ContextConfiguration
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
        Assertions.assertNotNull(ur);
        Assertions.assertNotNull(cr);
        Assertions.assertNotNull(lr);
    }

    @Test
    public void controllerLoads() {
        Assertions.assertNotNull(caffController);
    }

    @Test
    public void addCaffUnauthenticated() {
        MultipartFile caffFile = new MockMultipartFile("testFile", "testData".getBytes());
        String title = "testTitle";
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> caffController.addCaff(caffFile, title));
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void addCaffAuthenticated() {
        MultipartFile caffFile = new MockMultipartFile("testFile", "testData".getBytes());
        String title = "testTitle";
        /*try {
            caffController.addCaff(caffFile, title);
        } catch (IOException e) {
            Assertions.fail(e.getMessage() + '\n' + e.getStackTrace());
        }*/
    }

    @Test
    public void getCaffLikeUnauthenticated() {
        SearchRequest searchRequest = new SearchRequest();
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> caffController.getCaffLike(searchRequest));
    }

    @Test
    public void downloadCaffUnauthenticated() {
        UUID uuid = new UUID(0, 1);
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> caffController.downloadCaff(uuid));
    }

    @Test
    public void getAllCaffUnauthorized() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> caffController.getAllCaff());
    }
}
