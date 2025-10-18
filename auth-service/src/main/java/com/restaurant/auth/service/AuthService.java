package com.restaurant.auth.service;

import com.restaurant.JwtUtils;
import com.restaurant.auth.dto.LoginRequest;
import com.restaurant.auth.dto.LoginResponse;
import com.restaurant.auth.dto.UserDTO;
import com.restaurant.auth.entity.User;
import com.restaurant.auth.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.util.Date;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final long jwtExpiration = 3600000L;

    private JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager) throws Exception {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public LoginResponse login(LoginRequest loginRequest) throws Exception {
        User userEntity = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow();
        String token;
        if (passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword())) {
            token = generateJwtToken(userEntity);
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

    private String generateJwtToken(com.restaurant.auth.entity.User user) throws Exception {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId())
                .claim("role", user.getRole().name())
                .claim("tenantId", user.getTenantId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(jwtUtils.loadPrivateKey("keys/private.pem"), SignatureAlgorithm.RS256)
                .compact();
    }
}

