package com.backend.backend;

import com.backend.backend.Data.Role;
import com.backend.backend.Data.RoleEnum;
import com.backend.backend.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Override
	public void run(String[] args) throws Exception {
		Role admin=new Role();
		admin.setName(RoleEnum.ROLE_ADMIN);
		roleRepository.save(admin);
		Role user=new Role();
		user.setName(RoleEnum.ROLE_USER);
		roleRepository.save(user);
	}

}
