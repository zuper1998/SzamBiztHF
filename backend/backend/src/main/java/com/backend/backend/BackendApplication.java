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
		Role adminRole = roleRepository.save(new Role(RoleEnum.ROLE_ADMIN));
		roleRepository.save(new Role(RoleEnum.ROLE_USER));
		User admin = new User("admin", "admin@mail.com", encoder.encode("admin"), new ArrayList<Caff>(), new ArrayList<Comment>());
		Set<Role> roles = new HashSet<>();
		roles.add(adminRole);
		admin.setRoles(roles);
		userRepository.save(admin);
	}

}
