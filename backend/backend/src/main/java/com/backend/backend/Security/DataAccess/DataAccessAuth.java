package com.backend.backend.Security.DataAccess;

import com.backend.backend.Security.UserDetailsImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class DataAccessAuth {

    public Boolean UserDataAccess(UUID id, UserDetailsImpl userDetails) {
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        if(roles.contains("ROLE_ADMIN")) return true;
        else return (userDetails.getId().equals(id));
    }
}
