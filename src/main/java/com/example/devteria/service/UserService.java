package com.example.devteria.service;

import com.example.devteria.enums.Role;
import com.example.devteria.exception.AppException;
import com.example.devteria.exception.ErrorCode;
import com.example.devteria.dto.request.CreateUserRequest;
import com.example.devteria.dto.request.UpdateUserRequest;
import com.example.devteria.dto.response.UserResponse;
import com.example.devteria.entity.User;
import com.example.devteria.mapper.UserMapper;
import com.example.devteria.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;

    public UserResponse createUser(CreateUserRequest request) {


        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());

        user.setRoles(roles);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public List<UserResponse> getUsers() {

        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    public UserResponse getUser(String userId) {
        return userMapper.toUserResponse(userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public UserResponse updateUser(String userId, UpdateUserRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new  AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);


        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId){

        userRepository.deleteById(userId);
    }
}
