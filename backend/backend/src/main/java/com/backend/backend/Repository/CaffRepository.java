package com.backend.backend.Repository;

import com.backend.backend.Data.Caff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CaffRepository extends JpaRepository<Caff, UUID> {
    List<Caff> findByTitleContaining(String title);

    Optional<Caff> findById(UUID id);
}
