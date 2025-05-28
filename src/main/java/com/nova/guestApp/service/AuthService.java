package com.nova.guestApp.service;

import com.nova.guestApp.dtos.request.LoginRequest;
import com.nova.guestApp.dtos.request.UserRequest;
import com.nova.guestApp.dtos.response.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    AuthResponse createAccount(UserRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse getAccessToken(String refreshToken);
}
