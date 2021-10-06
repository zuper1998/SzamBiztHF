package com.backend.backend.Repository;

import com.backend.backend.Data.Role;
import com.backend.backend.Data.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleEnum name);
}
