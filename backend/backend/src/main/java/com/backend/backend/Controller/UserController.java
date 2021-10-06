package com.backend.backend.Controller;

import com.backend.backend.Data.Role;
import com.backend.backend.Data.RoleEnum;
import com.backend.backend.Data.User;
import com.backend.backend.Repository.RoleRepository;
import com.backend.backend.Repository.UserRepository;
import com.backend.backend.Security.Auth.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserRepository ur;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private PasswordEncoder encoder;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @PostMapping("/registration")
    public ResponseEntity<String> registration(@Valid @RequestBody User user) {

        User NewUser = new User(user.getUsername(), user.getEmail(), encoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER);
        NewUser.setRoles(Set.of(userRole));

        ur.save(NewUser);
        logger.info("new user successfully added to the database with the name of "+NewUser.getUsername() + " at " + java.time.LocalDateTime.now());
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        logger.info("successful login for "+user.getUsername() + " at " + java.time.LocalDateTime.now());
        return ResponseEntity.ok(jwt);
    }

    @GetMapping("/UserInfo/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    public ResponseEntity<User> GetUser(@PathVariable long id) {
        Optional<User> tmp = ur.findById(id);
        if (!tmp.isPresent()) {
            logger.error("there is no such user in database");
            return ResponseEntity.notFound().build();
        } else {
            logger.info(tmp.get().getUsername() + " Successful UserInfo Get at " + java.time.LocalDateTime.now());
            return ResponseEntity.ok(tmp.get());
        }
    }
}
