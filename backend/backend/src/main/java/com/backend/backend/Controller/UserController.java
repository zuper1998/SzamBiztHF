package com.backend.backend.Controller;

import com.backend.backend.Communication.Request.LoginRequest;
import com.backend.backend.Communication.Request.UpdateUserRequest;
import com.backend.backend.Communication.Response.GetAllUserResponse;
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
    private UserRepository userRepository;
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
        logRepository.save(new Log("Registration for user: " + NewUser.getUsername(), java.time.LocalDateTime.now().toString()));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login/admin")
    public ResponseEntity<LoginResponse> loginAdmin(@Valid @RequestBody @NotNull LoginRequest loginRequest) {
        Role role = roleRepository.findByName(RoleEnum.ROLE_ADMIN);
        return login(loginRequest, role);
    }

    @PostMapping("/login/user")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody @NotNull LoginRequest loginRequest) {
        Role role = roleRepository.findByName(RoleEnum.ROLE_USER);
        return login(loginRequest, role);
    }

    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest, Role role) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if(!userDetails.hasRole(role)) return ResponseEntity.badRequest().build();
        String jwt = jwtUtils.generateJwtToken(authentication);
        logRepository.save(new Log("Login for user: " + loginRequest.getUsername(), java.time.LocalDateTime.now().toString()));
        return ResponseEntity.ok(new LoginResponse(userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), jwt));
    }

    @PutMapping("/userUpdate/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    public ResponseEntity<String> userUpdateUser(@Valid @RequestBody @NotNull UpdateUserRequest updateRequest, @PathVariable UUID id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!dataAccessAuth.UserDataAccess(id, userDetails)) return ResponseEntity.badRequest().build();
        Optional<User> user = ur.findById(id);
        if (user.isEmpty()) return ResponseEntity.badRequest().build();
        if(updateRequest.getPassword().isEmpty() || updateRequest.getPassword().equals("")) return ResponseEntity.badRequest().build();
        user.get().setEmail(updateRequest.getEmail());
        user.get().setPassword(encoder.encode(updateRequest.getPassword()));
        ur.save(user.get());
        logRepository.save(new Log("User data Update for: " + user.get().getUsername() + " by user: " + userDetails.getUsername(), java.time.LocalDateTime.now().toString()));
        return ResponseEntity.ok(user.get().getEmail());
    }

    @PutMapping("/adminUpdate/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> adminUpdateUser(@Valid @RequestBody @NotNull UpdateUserRequest updateRequest, @PathVariable UUID id) {
        Optional<User> user = ur.findById(id);
        if(user.isPresent()) {
            if(updateRequest.getPassword().equals("")) {
                user.get().setEmail(updateRequest.getEmail());
            }
            else {
                user.get().setEmail(updateRequest.getEmail());
                user.get().setPassword(encoder.encode(updateRequest.getPassword()));
            }
        }
        else return ResponseEntity.badRequest().build();
        ur.save(user.get());
        return ResponseEntity.ok().build();
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
                            logRepository.save(new Log("Registration for admin: " + newUser.getUsername() + " by admin" + ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(), java.time.LocalDateTime.now().toString()));
                            break;
                        default:
                            role = roleRepository.findByName(RoleEnum.ROLE_USER);
                            logRepository.save(new Log("Registration for user: " + newUser.getUsername() + " by admin" + ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(), java.time.LocalDateTime.now().toString()));
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
        logRepository.save(new Log("Deleted user with id: " + id + " by admin: " + ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(), java.time.LocalDateTime.now().toString()));
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<GetAllUserResponse>> getAll() {
        List<GetAllUserResponse> response = new ArrayList<>();
        List<User> users = userRepository.findAll();
        for (User user: users) {
            if(!((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername().equals(user.getUsername())) response.add(new GetAllUserResponse(user.getId(), user.getUsername(), user.getEmail()));
        }
        logRepository.save(new Log("admin with name: " + ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername() + " requested all users", java.time.LocalDateTime.now().toString()));
        return  ResponseEntity.ok(response);
    }

}
