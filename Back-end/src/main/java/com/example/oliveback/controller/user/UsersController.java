package com.example.oliveback.controller.user;

import com.example.oliveback.dto.user.UserLoginRequest;
import com.example.oliveback.dto.user.UserSignupRequest;
import com.example.oliveback.dto.user.UserResponse;
import com.example.oliveback.service.user.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody UserSignupRequest request) {
        return ResponseEntity.ok(usersService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody UserLoginRequest request) {
        return ResponseEntity.ok(usersService.login(request));
    }

//    @PostMapping("/logout/{username}")  // ✅ PathVariable을 통해 사용자 로그아웃 요청 처리
//    public ResponseEntity<String> logout(@PathVariable String username) {
//        return ResponseEntity.ok(usersService.logout(username));
//    }
}

