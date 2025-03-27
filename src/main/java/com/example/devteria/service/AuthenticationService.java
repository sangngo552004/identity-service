package com.example.devteria.service;

import com.example.devteria.Exception.AppException;
import com.example.devteria.Exception.ErrorCode;
import com.example.devteria.dto.request.AuthenticationRequest;
import com.example.devteria.dto.response.AuthenticationResponse;
import com.example.devteria.entity.User;
import com.example.devteria.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    UserRepository userRepository;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        return  AuthenticationResponse.builder()
                .authenticated(passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .build();
    }
}
