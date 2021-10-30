package com.backend.backend.Controller;

import com.backend.backend.Communication.Request.LoginRequest;
import com.backend.backend.Communication.Request.UpdateUserRequest;
import com.backend.backend.Communication.Response.LoginResponse;
import com.backend.backend.Communication.Request.SignUpRequest;
import com.backend.backend.Data.*;
import com.backend.backend.Repository.LogRepository;
import com.backend.backend.Repository.RoleRepository;
import com.backend.backend.Repository.UserRepository;
import com.backend.backend.Security.Auth.JwtUtils;
import com.backend.backend.Security.DataAccess.DataAccessAuth;
import com.backend.backend.Security.UserDetailsImpl;
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
import javax.validation.constraints.NotNull;
import java.util.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserRepository ur;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private DataAccessAuth dataAccessAuth;

    @PostMapping("/registration")
    public ResponseEntity<Void> registration(@Valid @RequestBody @NotNull SignUpRequest signUpMessage) {
        if (ur.existsByUsername(signUpMessage.getUsername())) return ResponseEntity.badRequest().build();
        User NewUser = new User(signUpMessage.getUsername(), signUpMessage.getEmail(), encoder.encode(signUpMessage.getPassword()), new ArrayList<>(), new ArrayList<>());
        Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER);
        NewUser.setRoles(Set.of(userRole));
        ur.save(NewUser);
        logRepository.save(new Log("Registration for user: " + NewUser.getUsername(), java.time.LocalDateTime.now()));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody @NotNull LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        logRepository.save(new Log("Login for user: " + loginRequest.getUsername(), java.time.LocalDateTime.now()));
        return ResponseEntity.ok(new LoginResponse(userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), jwt));
    }

    @PutMapping("/userUpdate/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    public ResponseEntity<String> userUpdateUser(@Valid @RequestBody @NotNull UpdateUserRequest updateRequest, @PathVariable UUID id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!dataAccessAuth.UserDataAccess(id, userDetails)) return ResponseEntity.badRequest().build();
        Optional<User> user = ur.findById(id);
        if (user.isEmpty()) return ResponseEntity.badRequest().build();
        user.get().setEmail(updateRequest.getEmail());
        user.get().setPassword(encoder.encode(updateRequest.getPassword()));
        ur.save(user.get());
        logRepository.save(new Log("User data Update for: " + user.get().getUsername() + " by user: " + userDetails.getUsername(), java.time.LocalDateTime.now()));
        return ResponseEntity.ok(user.get().getEmail());
    }

    @PostMapping("/adminAddUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> adminAddUser(@Valid @RequestBody @NotNull SignUpRequest signUpMessage) {
        if (ur.existsByUsername(signUpMessage.getUsername())) return ResponseEntity.badRequest().build();
        User newUser = new User(signUpMessage.getUsername(), signUpMessage.getEmail(), encoder.encode(signUpMessage.getPassword()), new ArrayList<>(), new ArrayList<>());
        Set<Role> roleSet = new HashSet<>();
        if(signUpMessage.getRoles() != null) {
            if (!signUpMessage.getRoles().isEmpty()) {
                for (String roles : signUpMessage.getRoles()) {
                    Role role;
                    switch (roles) {
                        case "ROLE_ADMIN":
                            role = roleRepository.findByName(RoleEnum.ROLE_ADMIN);
                            logRepository.save(new Log("Registration for admin: " + newUser.getUsername() + " by admin" + ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(), java.time.LocalDateTime.now()));
                            break;
                        default:
                            role = roleRepository.findByName(RoleEnum.ROLE_USER);
                            logRepository.save(new Log("Registration for user: " + newUser.getUsername() + " by admin" + ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(), java.time.LocalDateTime.now()));
                            break;
                    }
                    roleSet.add(role);
                    newUser.getRoles().add(role);
                }
            }
        }
        Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER);
        newUser.setRoles(Set.of(userRole));
        ur.save(newUser);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        Optional<User> user = ur.findById(id);
        if(user.isEmpty()) return ResponseEntity.badRequest().build();
        ur.delete(user.get());
        logRepository.save(new Log("Deleted user with id: " + id + " by admin: " + ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(), java.time.LocalDateTime.now()));
        return ResponseEntity.ok().build();
    }

}
