package com.xndr.velaroute.controllers;

import com.xndr.velaroute.auth.AuthenticationResponse;
import com.xndr.velaroute.auth.RegisterRequest;
import com.xndr.velaroute.models.LoginRequest;
import com.xndr.velaroute.security.JwtService;
import com.xndr.velaroute.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
            ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(@RequestBody LoginRequest request) {
        AuthenticationResponse response = service.authenticate(request);

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Successfully Logged In. Generating Token...");
        body.put("authResponse", response);

        return ResponseEntity.ok(body);
        // If authentication passes, generate the token
        // (You'll need a way to fetch the User from the DB here)
//        return ResponseEntity.ok("Successfully Logged In. Generating Token...");
    }

//    @PostMapping
//    public ResponseEntity<String> authenticate(@RequestBody LoginRequest request) {
//        System.out.println("DEBUG: Controller reached with: " + request.getUsername());
//        return ResponseEntity.ok(service.authenticate(request));
//    }

}
