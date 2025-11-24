package com.tradenest.controller;

import com.tradenest.dto.AuthResponseDto;
import com.tradenest.dto.RegisterRequestDto;
import com.tradenest.entity.Role;
import com.tradenest.entity.User;
import com.tradenest.service.AuthService;
import com.tradenest.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService){
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody @Valid RegisterRequestDto request) {
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(request.getPassword())
                .roles(List.of(Role.USER))
                .build();

        String token = authService.register(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                AuthResponseDto.builder()
                        .token(token)
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .id(user.getId())
                        .roles(user.getRoles())
                        .build()
        );

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid RegisterRequestDto request) {

        String token = authService.authenticate(request.getUsername(), request.getPassword());

        User user = userService.findUserByUsername(request.getUsername());

        AuthResponseDto response = AuthResponseDto.builder()
                .token(token)
                .username(user.getUsername())
                .email(user.getEmail())
                .id(user.getId())
                .roles(user.getRoles())
                .build();
        return ResponseEntity.ok(response);
    }


}
