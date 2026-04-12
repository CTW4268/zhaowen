package com.example.news.service;

import com.example.news.dto.request.LoginRequest;
import com.example.news.dto.request.RegisterRequest;
import com.example.news.dto.response.LoginResponse;
import com.example.news.entity.User;
import com.example.news.security.JwtTokenProvider;
import com.example.news.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 认证服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    /**
     * 用户注册
     */
    @Transactional
    public LoginResponse register(RegisterRequest request) {
        // 检查用户名是否存在
        if (userService.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 创建用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAvatar("");

        user = userService.saveUser(user);
        log.info("用户注册成功：{}", user.getUsername());

        // 生成 Token
        String token = generateToken(user);

        return new LoginResponse(user.getId(), user.getUsername(), user.getAvatar(), token);
    }

    /**
     * 用户登录
     */
    @Transactional
    public LoginResponse login(LoginRequest request) {
        // 认证
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // 获取用户
        User user = userService.getUserByUsername(request.getUsername());
        log.info("用户登录成功：{}", user.getUsername());

        // 生成 Token
        String token = generateToken(user);

        return new LoginResponse(user.getId(), user.getUsername(), user.getAvatar(), token);
    }

    /**
     * 生成 JWT Token
     */
    private String generateToken(User user) {
        UserDetailsImpl userDetails = new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getPassword()
        );
        return jwtTokenProvider.generateToken(userDetails);
    }
}
