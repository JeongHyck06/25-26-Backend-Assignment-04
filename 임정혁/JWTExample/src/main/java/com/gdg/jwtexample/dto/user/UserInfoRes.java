package com.gdg.jwtexample.dto.user;

import com.gdg.jwtexample.domain.Role;
import com.gdg.jwtexample.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoRes {

    private Long id;
    private String username;
    private String email;
    private Role role;
    private LocalDateTime createdAt;

    public static UserInfoRes from(User user) {
        return UserInfoRes.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}

