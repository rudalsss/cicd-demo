package com.example.oliveback.controller;

import com.example.oliveback.controller.user.UsersController;
import com.example.oliveback.dto.user.UserSignupRequest;
import com.example.oliveback.dto.user.UserLoginRequest;
import com.example.oliveback.dto.user.UserResponse;
import com.example.oliveback.service.user.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UsersController.class)  // ✅ 정확한 컨트롤러 지정
@AutoConfigureMockMvc(addFilters = false)  // ✅ Security 필터 비활성화 (필요한 경우)
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsersService usersService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        assertNotNull(usersService, "MockBean usersService가 주입되지 않았습니다.");  // ✅ 주입 확인
    }

    @Test
    void 회원가입_API_테스트() throws Exception {
        // given
        UserSignupRequest request = new UserSignupRequest("testuser", "password", "test@example.com", null);
        UserResponse response = new UserResponse(1L, "testuser", "test@example.com", null);

        when(usersService.signup(any(UserSignupRequest.class))).thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))  // ✅ JSON 변환
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void 로그인_API_테스트() throws Exception {
        // given
        UserLoginRequest request = new UserLoginRequest("testuser", "password123");
        UserResponse response = new UserResponse(1L, "testuser", "test@example.com", null);

        when(usersService.login(any(UserLoginRequest.class))).thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))  // ✅ JSON 변환
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }
}
