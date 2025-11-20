package com.tradenest.Mapper;

import com.tradenest.dto.UserResponseDto;
import com.tradenest.dto.UserUpdateDto;
import com.tradenest.entity.User;

public class UserMapper {

    public static UserResponseDto toDto(User user) {
        if (user == null) return null;
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }

    public static User fromUpdateDto(UserUpdateDto dto) {
        if (dto == null) return null;
        return User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .passwordHash(dto.getPasswordHash())
                .roles(dto.getRoles())
                .build();
    }
}
