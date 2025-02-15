package com.example.oliveback.service.user;

import com.example.oliveback.domain.user.Role;
import com.example.oliveback.domain.user.Users;
import com.example.oliveback.dto.user.UserLoginRequest;
import com.example.oliveback.dto.user.UserSignupRequest;
import com.example.oliveback.dto.user.UserResponse;
import com.example.oliveback.exception.CustomException;
import com.example.oliveback.repository.user.UsersRepository;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
//    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse signup(UserSignupRequest request) {
        // username 중복 체크
        if (usersRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new CustomException.DuplicateUserException();
        }

        // 이메일 중복 체크
        if (usersRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomException.DuplicateEmailException();
        }

        // 비밀번호 유효성 검사
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new CustomException.InvalidPasswordException();
        }

        // 회원 저장
        Users newUser = Users.builder()
                .username(request.getUsername())
                .password(request.getPassword()) // 비밀번호 해싱 필요
//                .password(passwordEncoder.encode(request.getPassword())) // 비밀번호 해싱
                .email(request.getEmail())
                .role(request.getRole() == null ? Role.USER : request.getRole())
                .build();

        usersRepository.save(newUser);
        return UserResponse.fromEntity(newUser);
    }

    //로그인
    @Transactional(readOnly = true)
    public UserResponse login(UserLoginRequest request) {
        Users user = usersRepository.findByUsername(request.getUsername())
                .orElseThrow(CustomException.UserNotFoundException::new);

        // 비밀번호 검증 (평문 비교)
        if (!request.getPassword().equals(user.getPassword())) {
            throw new CustomException.InvalidPasswordException();
        }

        return UserResponse.fromEntity(user);
    }

//    //로그아웃
//    public String logout(String username) {
//        if (username == null || username.isEmpty()) {
//            throw new CustomException.NotLoggedInException();
//        }
//        return "로그아웃 성공";
//    }
}


