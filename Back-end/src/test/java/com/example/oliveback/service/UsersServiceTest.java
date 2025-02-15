package com.example.oliveback.service;

import com.example.oliveback.domain.user.Role;
import com.example.oliveback.domain.user.Users;
import com.example.oliveback.dto.user.UserLoginRequest;
import com.example.oliveback.dto.user.UserSignupRequest;
import com.example.oliveback.dto.user.UserResponse;
import com.example.oliveback.exception.CustomException;
import com.example.oliveback.repository.user.UsersRepository;
import com.example.oliveback.service.user.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersServiceTest {

    @InjectMocks
    private UsersService usersService;

    @Mock
    private UsersRepository usersRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 회원가입_성공() {
        // given
        UserSignupRequest request = new UserSignupRequest("testuser", "password123", "test@example.com", Role.USER);

        when(usersRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(usersRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Users savedUser = Users.builder()
                .id(1L)
                .username(request.getUsername())
                .password(request.getPassword())  // 비밀번호 그대로 저장
                .email(request.getEmail())
                .role(request.getRole())
                .build();

        when(usersRepository.save(any(Users.class))).thenReturn(savedUser);

        // when
        UserResponse response = usersService.signup(request);

        // then
        assertNotNull(response);
        assertEquals(request.getUsername(), response.getUsername());
        assertEquals(request.getEmail(), response.getEmail());
        assertEquals(request.getPassword(), savedUser.getPassword());  // 비밀번호 그대로 저장 확인
        assertEquals(request.getRole(), response.getRole());
    }

    @Test
    void 회원가입_실패_이미존재하는_사용자() {
        // given
        UserSignupRequest request = new UserSignupRequest("testuser", "password123", "test@example.com", Role.USER);
        Users existingUser = new Users(1L, "testuser", "password123", "test@example.com", Role.USER);

        when(usersRepository.findByUsername("testuser")).thenReturn(Optional.of(existingUser));

        // when & then
        assertThrows(CustomException.DuplicateUserException.class, () -> usersService.signup(request),
                "이미 존재하는 사용자 이름입니다.");
    }

    @Test
    void 회원가입_실패_이미존재하는_이메일() {
        // given
        UserSignupRequest request = new UserSignupRequest("newuser", "password123", "test@example.com", Role.USER);
        Users existingUser = new Users(1L, "testuser", "password123", "test@example.com", Role.USER);

        when(usersRepository.findByEmail("test@example.com")).thenReturn(Optional.of(existingUser));

        // when & then
        assertThrows(CustomException.DuplicateEmailException.class, () -> usersService.signup(request),
                "이미 존재하는 이메일입니다.");
    }

    @Test
    void 회원가입_실패_비밀번호_유효성_검사() {
        // given
        UserSignupRequest request = new UserSignupRequest("testuser", "123", "test@example.com", Role.USER); // 비밀번호 6자 미만

        // when & then
        assertThrows(CustomException.InvalidPasswordException.class, () -> usersService.signup(request),
                "비밀번호는 최소 6자 이상이어야 합니다.");
    }

    @Test
    void 로그인_성공() {
        // given
        UserLoginRequest request = new UserLoginRequest("testuser", "password123");
        Users user = Users.builder()
                .id(1L)
                .username(request.getUsername())
                .password("password123")  // 저장된 비밀번호 그대로 비교
                .email("test@example.com")
                .role(Role.USER)
                .build();

        when(usersRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        // when
        UserResponse response = usersService.login(request);

        // then
        assertNotNull(response);
        assertEquals(user.getUsername(), response.getUsername());
        assertEquals(user.getEmail(), response.getEmail());
        assertEquals(user.getPassword(), request.getPassword());  // 비밀번호 그대로 비교
        assertEquals(user.getRole(), response.getRole());
    }

    @Test
    void 로그인_실패_비밀번호_불일치() {
        // given
        UserLoginRequest request = new UserLoginRequest("testuser", "wrongpassword");
        Users user = Users.builder()
                .id(1L)
                .username(request.getUsername())
                .password("password123")  // 저장된 비밀번호 그대로 비교
                .email("test@example.com")
                .role(Role.USER)
                .build();

        when(usersRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        // when & then
        assertThrows(CustomException.InvalidPasswordException.class, () -> usersService.login(request),
                "비밀번호가 일치하지 않습니다.");
    }
}
