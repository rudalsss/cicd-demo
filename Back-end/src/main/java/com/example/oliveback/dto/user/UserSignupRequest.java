package com.example.oliveback.dto.user;

import com.example.oliveback.domain.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignupRequest {

    @NotBlank(message = "사용자 이름을 입력하세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;

    @Email(message = "올바른 이메일을 입력하세요.")
    private String email;

    private Role role; // 관리자 여부 추가
}
