package com.healthDocs.healthDocs.service;

import com.healthDocs.healthDocs.model.Role;
import com.healthDocs.healthDocs.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User register(String username, String password, String repeatPassword, String firstName, String lastName, String EMBG, Role role);
}