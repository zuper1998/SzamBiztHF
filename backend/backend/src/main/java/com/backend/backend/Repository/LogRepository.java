package com.backend.backend.Repository;

import com.backend.backend.Data.Log;
import com.backend.backend.Data.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LogRepository extends JpaRepository<Log, UUID> {
}
