package com.tradenest.controller;

import com.tradenest.Mapper.UserMapper;
import com.tradenest.dto.UserResponseDto;
import com.tradenest.dto.UserUpdateDto;
import com.tradenest.entity.User;
import com.tradenest.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('Admin')")
    public List<UserResponseDto> getAllUsers() {
        return userService.findAllUsers().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('Admin') or #id.toString() == authentication.name")
    public UserResponseDto getUserById(@PathVariable UUID id) {
        User user = userService.findUserById(id);
        return UserMapper.toDto(user);
    }

    @PutMapping("/{id}")
    @PreAuthorize("#id.toString() == authentication.name")
    public UserResponseDto updateUser(@PathVariable UUID id, @RequestBody @Valid UserUpdateDto updateDto) {
        User userToUpdate = UserMapper.fromUpdateDto(updateDto);
        User updatedUser = userService.updateUser(id, userToUpdate);
        return UserMapper.toDto(updatedUser);
    }
}
