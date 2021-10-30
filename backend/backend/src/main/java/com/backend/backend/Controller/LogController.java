package com.backend.backend.Controller;

import com.backend.backend.Data.Log;
import com.backend.backend.Repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Repository
@RequestMapping("/api/log")
public class LogController {

    @Autowired
    private LogRepository logRepository;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Log>> getLog() {
        return  ResponseEntity.ok(logRepository.findAll());
    }

}
