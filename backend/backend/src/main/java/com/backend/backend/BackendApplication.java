package com.backend.backend;

import com.backend.backend.Data.*;
import com.backend.backend.Repository.RoleRepository;
import com.backend.backend.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder encoder;

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Override
	public void run(String[] args) throws Exception {
		if(roleRepository.findByName(RoleEnum.ROLE_ADMIN).isEmpty()) roleRepository.save(new Role(RoleEnum.ROLE_ADMIN));
		if(roleRepository.findByName(RoleEnum.ROLE_USER).isEmpty()) roleRepository.save(new Role(RoleEnum.ROLE_USER));
		if(!userRepository.existsByUsername("admin")) {
			Set<Role> roles = new HashSet<>();
			Optional<Role> adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN);
			User admin = new User("admin", "admin@mail.com", encoder.encode("admin"), new ArrayList<Caff>(), new ArrayList<Comment>());
			adminRole.ifPresent(roles::add);
			admin.setRoles(roles);
			userRepository.save(admin);
		}
	}

}
