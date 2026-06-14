package com.codemarket.controller;

import com.codemarket.dto.LoginRequest;
import com.codemarket.service.JwtService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        if ("admin".equals(request.getUsername())
                &&
                "1234".equals(request.getPassword())) {

            return jwtService.generateToken(
                    request.getUsername()
            );
        }

        return "Invalid Credentials";
    }
} 
