package com.century21.deliveryserviceapp.user.controller;

import com.century21.deliveryserviceapp.common.annotaion.Auth;
import com.century21.deliveryserviceapp.common.exception.ApiException;
import com.century21.deliveryserviceapp.common.response.ErrorResponse;
import com.century21.deliveryserviceapp.common.response.SuccessResponse;
import com.century21.deliveryserviceapp.user.auth.AuthUser;
import com.century21.deliveryserviceapp.user.dto.request.LoginRequest;
import com.century21.deliveryserviceapp.user.dto.request.SignUpRequest;
import com.century21.deliveryserviceapp.user.dto.response.LoginResponse;
import com.century21.deliveryserviceapp.user.dto.response.SignUpResponse;
import com.century21.deliveryserviceapp.user.dto.response.UserResponse;
import com.century21.deliveryserviceapp.user.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequestDto) {
        try {
            SignUpResponse signUpResponseDto = userService.register(signUpRequestDto);
            return ResponseEntity.ok(SuccessResponse.of(signUpResponseDto));
        } catch (ApiException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(ErrorResponse.of(e.getHttpStatus().value(), e.getMessage()));
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequestDto) {
        try {
            LoginResponse loginResponseDto = userService.login(loginRequestDto);
            return ResponseEntity.ok(SuccessResponse.of(loginResponseDto));
        } catch (ApiException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(ErrorResponse.of(e.getHttpStatus().value(), e.getMessage()));
        }
    }

    // 회원 정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserInfo(@PathVariable Long userId, @Auth AuthUser authUser) {
        try {
            // TODO [USER] 수정 예정 : authUser 의 id와 요청으로 들어온 userId를 비교해서 같지 않다면 동일한 유저가 아니기 때문에 실패 처리
            UserResponse userResponseDto = userService.getUserInfo(userId);
            return ResponseEntity.ok(SuccessResponse.of(userResponseDto));
        } catch (ApiException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(ErrorResponse.of(e.getHttpStatus().value(), e.getMessage()));
        }
    }

    // 회원 탈퇴
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, @RequestParam String password) {
        try {
            userService.deleteUser(userId, password);
            return ResponseEntity.ok(SuccessResponse.of(null));
        } catch (ApiException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(ErrorResponse.of(e.getHttpStatus().value(), e.getMessage()));
        }
    }
}