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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Transactional
    public LoginResponse register(RegisterRequest request) {
        if (userService.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAvatar("");

        user = userService.saveUser(user);
        log.info("用户注册成功：{}", user.getUsername());

        String token = generateToken(user);
        String refreshToken = generateRefreshToken(user);

        return new LoginResponse(user.getId(), user.getUsername(), user.getAvatar(), token, refreshToken);
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userService.getUserByUsername(request.getUsername());
        log.info("用户登录成功：{}", user.getUsername());

        String token = generateToken(user);
        String refreshToken = generateRefreshToken(user);

        return new LoginResponse(user.getId(), user.getUsername(), user.getAvatar(), token, refreshToken);
    }

    @Transactional(readOnly = true)
    public LoginResponse refreshToken(String refreshToken) {
        try {
            String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
            UserDetails userDetails = userService.loadUserByUsername(username);

            if (jwtTokenProvider.validateRefreshToken(refreshToken, userDetails)) {
                User user = userService.getUserByUsername(username);
                String newAccessToken = jwtTokenProvider.generateToken(userDetails);
                String newRefreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

                log.info("Token 刷新成功：{}", username);
                return new LoginResponse(user.getId(), user.getUsername(), user.getAvatar(), newAccessToken, newRefreshToken);
            } else {
                throw new RuntimeException("Refresh Token 无效或已过期");
            }
        } catch (Exception e) {
            log.error("Token 刷新失败: {}", e.getMessage());
            throw new RuntimeException("Token 刷新失败：" + e.getMessage());
        }
    }

    private String generateToken(User user) {
        UserDetailsImpl userDetails = new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getPassword()
        );
        return jwtTokenProvider.generateToken(userDetails);
    }

    private String generateRefreshToken(User user) {
        UserDetailsImpl userDetails = new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getPassword()
        );
        return jwtTokenProvider.generateRefreshToken(userDetails);
    }
}