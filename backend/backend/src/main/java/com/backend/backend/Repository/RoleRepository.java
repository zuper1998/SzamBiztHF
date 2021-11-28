package com.backend.backend.Repository;

import com.backend.backend.Data.Role;
import com.backend.backend.Data.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(RoleEnum name);
}
