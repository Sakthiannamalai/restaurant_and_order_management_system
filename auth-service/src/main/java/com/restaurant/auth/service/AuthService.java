package com.restaurant.auth.service;

import com.restaurant.auth.config.JwtUtils;
import com.restaurant.auth.dto.LoginRequest;
import com.restaurant.auth.dto.LoginResponse;
import com.restaurant.auth.dto.UserDTO;
import com.restaurant.auth.entity.User;
import com.restaurant.auth.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    
    public AuthService(UserRepository userRepository, 
                      PasswordEncoder passwordEncoder,
                      AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        User userEntity = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow();
        String token;
        if (passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword())) {
            token = jwtUtils.generateJwtToken(userEntity);
        } else {
            throw new RuntimeException("Username or password is invalid");
        }
        return new LoginResponse(token, userEntity);
    }
    
    public UserDTO register(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }
        
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }
        
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(userDTO.getRole() != null ? userDTO.getRole() : User.Role.WAITER);
        user.setTenantId(userDTO.getTenantId());
        
        User savedUser = userRepository.save(user);
        return new UserDTO(savedUser);
    }

    public UserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User userEntity = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        return new UserDTO(userEntity);
    }
}

