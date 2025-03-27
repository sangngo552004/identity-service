package com.example.devteria.mapper;

import com.example.devteria.dto.request.CreateUserRequest;
import com.example.devteria.dto.request.UpdateUserRequest;
import com.example.devteria.dto.response.UserResponse;
import com.example.devteria.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(CreateUserRequest request);

    UserResponse  toUserResponse(User user);

    void updateUser(@MappingTarget User user, UpdateUserRequest request);
}
