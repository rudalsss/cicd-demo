package com.example.oliveback.repository;

import com.example.oliveback.domain.user.Role;
import com.example.oliveback.domain.user.Users;
import com.example.oliveback.repository.user.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersRepositoryTest {

    @Mock
    private UsersRepository usersRepository;  // UsersRepository를 Mock 객체로 생성

    @Mock
    private Users mockUser;  // 가상의 사용자 데이터

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Mock 객체 초기화
    }

    @Test
    void 사용자_조회_성공() {
        // given
        Users user = new Users(1L, "testuser", "password123", "test@example.com", Role.USER);
        when(usersRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // when
        Optional<Users> foundUser = usersRepository.findByUsername("testuser");

        // then
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
        assertEquals("test@example.com", foundUser.get().getEmail());
        assertEquals(Role.USER, foundUser.get().getRole());
    }

    @Test
    void 존재하지_않는_사용자_조회() {
        // given
        when(usersRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        // when
        Optional<Users> foundUser = usersRepository.findByUsername("unknown");

        // then
        assertFalse(foundUser.isPresent());
    }
}
