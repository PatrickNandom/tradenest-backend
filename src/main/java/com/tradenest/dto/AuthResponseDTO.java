package com.tradenest.dto;

import com.tradenest.entity.Role;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDto {
    private UUID id;
    private String username;
    private String email;
    private String token;
    private List<Role> roles;
}