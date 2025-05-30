package com.nova.guestApp.controller;

import com.nova.guestApp.dtos.request.LoginRequest;
import com.nova.guestApp.dtos.request.RefreshTokenRequest;
import com.nova.guestApp.dtos.request.UserRequest;
import com.nova.guestApp.dtos.response.AuthResponse;
import com.nova.guestApp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private AuthService authService;

    @PostMapping("/create-account")
    public AuthResponse createAccount(@RequestBody UserRequest userRequest){
        return authService.createAccount(userRequest);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh-token")
    public AuthResponse getAccessToken(@RequestBody RefreshTokenRequest request) {
        return authService.getAccessToken(request.getRefreshToken());
    }
}
