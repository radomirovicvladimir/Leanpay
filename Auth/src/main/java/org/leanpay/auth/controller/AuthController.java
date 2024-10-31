package org.leanpay.auth.controller;

import jakarta.validation.Valid;
import org.leanpay.auth.dto.AuthRequest;
import org.leanpay.auth.dto.AuthResponse;
import org.leanpay.auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public AuthResponse authenticate(@Valid @RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(), authRequest.getPassword()
                )
        );

        String role = authentication.getAuthorities().iterator().next().getAuthority();

        String token = jwtUtil.generateToken(authRequest.getUsername(), role);

        return new AuthResponse(token);
    }
}
