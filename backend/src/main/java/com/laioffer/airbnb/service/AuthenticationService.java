package com.laioffer.airbnb.service;

import com.laioffer.airbnb.entity.Authority;
import com.laioffer.airbnb.entity.Token;
import com.laioffer.airbnb.entity.User;
import com.laioffer.airbnb.entity.UserRole;
import com.laioffer.airbnb.exception.UserNotExistException;
import com.laioffer.airbnb.repository.AuthorityRepository;
import com.laioffer.airbnb.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

@Service
public class AuthenticationService {
    private AuthenticationManager authenticationManager;
    private AuthorityRepository authorityRepository;
    private JwtUtil jwtUtil;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager, AuthorityRepository authorityRepository, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.authorityRepository = authorityRepository;
        this.jwtUtil = jwtUtil;
    }

    public Token authenticate(User user, UserRole role) throws UserNotExistException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (AuthenticationException exception) {
            throw new UserNotExistException("User Doesn't Exist");
        }

        Authority authority = authorityRepository.findById(user.getUsername()).orElse(null);
        if (!authority.getAuthority().equals(role.name())) {
            throw new UserNotExistException("User Doesn't Exist");
        }

        return new Token(jwtUtil.generateToken(user.getUsername()));
    }
}
