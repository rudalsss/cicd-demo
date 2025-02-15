package com.example.oliveback.dto.user;

import com.example.oliveback.domain.user.Role;
import com.example.oliveback.domain.user.Users;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private Role role; // 관리자 여부 추가

    public static UserResponse fromEntity(Users user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
