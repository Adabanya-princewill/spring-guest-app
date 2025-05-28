package com.nova.guestApp.service.impl;

import com.nova.guestApp.dtos.request.LoginRequest;
import com.nova.guestApp.dtos.request.UserRequest;
import com.nova.guestApp.dtos.response.AuthResponse;
import com.nova.guestApp.dtos.response.LoginResponse;
import com.nova.guestApp.model.User;
import com.nova.guestApp.repository.UserRepository;
import com.nova.guestApp.service.AuthService;
import com.nova.guestApp.utils.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse createAccount(UserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return AuthResponse.builder()
                    .responseCode("000")
                    .responseMessage("Account already exists")
                    .response(null)
                    .build();
        }

        try {
            User newUser = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .email(request.getEmail())
                    .username(request.getEmail())
                    .name(request.getFirstName() + " " + request.getLastName())
                    .build();
            userRepository.save(newUser);

            return AuthResponse.builder()
                    .responseCode("001")
                    .responseMessage("Account created successfully")
                    .response(null)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = userRepository.findByEmail(request.getEmail());

            String accessToken = jwtTokenService.generateAccessToken(user);
            String refreshToken = jwtTokenService.generateRefreshToken(user);

            return AuthResponse.builder()
                    .responseCode("001")
                    .responseMessage("Login successful")
                    .response(
                            LoginResponse.builder()
                                    .accessToken(accessToken)
                                    .refreshToken(refreshToken)
                                    .name(user.getName())
                                    .build()
                    )
                    .build();

        } catch (Exception e) {
            return AuthResponse.builder()
                    .responseCode("000")
                    .responseMessage("Login not successful")
                    .response(
                            LoginResponse.builder()
                                    .accessToken(null)
                                    .refreshToken(null)
                                    .name(null)
                                    .build()
                    )
                    .build();
        }
    }

    @Override
    public AuthResponse getAccessToken(String refreshToken) {
        String email = jwtTokenService.getUsernameFromToken(refreshToken);
        User user = userRepository.findByEmail(email);

        if (jwtTokenService.isValidRefreshToken(refreshToken, user.getEmail())) {
            String newAccessToken = jwtTokenService.generateAccessToken(user);

            return AuthResponse.builder()
                    .responseCode("001")
                    .responseMessage("Token refreshed")
                    .response(
                            LoginResponse.builder()
                                    .accessToken(newAccessToken)
                                    .refreshToken(refreshToken)
                                    .name(user.getName())
                                    .build()
                    )
                    .build();
        }

        return AuthResponse.builder()
                .responseCode("000")
                .responseMessage("Invalid refresh token")
                .response(
                        LoginResponse.builder()
                                .accessToken(null)
                                .refreshToken(refreshToken)
                                .name(null)
                                .build()
                )
                .build();
    }
}
