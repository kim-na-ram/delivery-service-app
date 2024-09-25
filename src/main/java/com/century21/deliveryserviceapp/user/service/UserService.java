package com.century21.deliveryserviceapp.user.service;

import com.century21.deliveryserviceapp.common.config.PasswordEncoder;
import com.century21.deliveryserviceapp.common.exception.DuplicateException;
import com.century21.deliveryserviceapp.common.exception.InvalidParameterException;
import com.century21.deliveryserviceapp.common.exception.NotFoundException;
import com.century21.deliveryserviceapp.common.exception.ResponseCode;
import com.century21.deliveryserviceapp.entity.User;
import com.century21.deliveryserviceapp.token.service.TokenService;
import com.century21.deliveryserviceapp.user.dto.request.LoginRequest;
import com.century21.deliveryserviceapp.user.dto.request.SignUpRequest;
import com.century21.deliveryserviceapp.user.dto.response.LoginResponse;
import com.century21.deliveryserviceapp.user.dto.response.SignUpResponse;
import com.century21.deliveryserviceapp.user.dto.response.UserResponse;
import com.century21.deliveryserviceapp.user.jwt.JwtUtil;
import com.century21.deliveryserviceapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.century21.deliveryserviceapp.common.constant.Constant.ACCESS_TOKEN_EXPIRE_TIME;
import static com.century21.deliveryserviceapp.common.constant.Constant.REFRESH_TOKEN_EXPIRE_TIME;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final TokenService tokenService;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 회원가입
    @Transactional
    public SignUpResponse register(SignUpRequest signUpRequestDto) {
        if (userRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new DuplicateException(ResponseCode.DUPLICATE_EMAIL);
        }
        User user = User.from(signUpRequestDto, passwordEncoder);
        userRepository.save(user);
        return new SignUpResponse(user.getId(), user.getEmail(), user.getNickname(), user.getAuthority().name());
    }


    // 로그인
    public LoginResponse login(LoginRequest loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_USER));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new InvalidParameterException(ResponseCode.INVALID_PASSWORD);
        }
        // JWT Access Token 생성 (userId, email, 권한)
        String accessToken = jwtUtil.createToken(
                user.getId(),                   // userId
                user.getAuthority().name(),     // 권한(Authority Enum을 문자열로 변환)
                ACCESS_TOKEN_EXPIRE_TIME
        );

        String refreshToken = jwtUtil.createToken(
                user.getId(),
                user.getAuthority().name(),
                REFRESH_TOKEN_EXPIRE_TIME
        );

        tokenService.saveToken(accessToken, refreshToken);

        return LoginResponse.of(accessToken);
    }


    // 회원정보 조회
    @Transactional(readOnly = true)
    public UserResponse getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_USER));

        return new UserResponse(user.getId(), user.getEmail(), user.getNickname(), user.getAuthority().name());
    }

    // 회원 탈퇴 (soft delete)
    @Transactional
    public void deleteUser(Long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_USER));

        // 비밀번호가 일치하지 않으면 예외 처리
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidParameterException(ResponseCode.INVALID_PASSWORD);
        }

        // 회원 탈퇴 (deletedAt 값 설정)
        user.deleteUser();
        userRepository.save(user);
    }

}