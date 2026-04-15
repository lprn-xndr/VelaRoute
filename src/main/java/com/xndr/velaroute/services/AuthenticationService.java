package com.xndr.velaroute.services;

import com.xndr.velaroute.auth.AuthenticationResponse;
import com.xndr.velaroute.auth.RegisterRequest;
import com.xndr.velaroute.models.LoginRequest;
import com.xndr.velaroute.models.Role;
import com.xndr.velaroute.models.User;
import com.xndr.velaroute.repositories.UserRepository;
import com.xndr.velaroute.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = new User();
        user.setUsername(request.getUsername());

        // This is the most important line!
        // It takes "Leon" and turns it into a hash before saving.
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(Role.USER);
        userRepository.save(user);

         var jwtToken = jwtService.generateToken(user);
         return AuthenticationResponse.builder()
                 .token(jwtToken)
                 .build();
    }

    public AuthenticationResponse authenticate(LoginRequest request) {
        // This is the line that actually checks the password.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

//        System.out.println("DEBUG: Authentication successful for: " + request.getUsername());

        // If we reach her, the password was correct.
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var jwtToken = jwtService.generateToken(user);

        // Return the token
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
