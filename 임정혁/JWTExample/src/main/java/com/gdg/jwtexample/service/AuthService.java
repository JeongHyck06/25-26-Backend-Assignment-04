package com.gdg.jwtexample.service;

import com.gdg.jwtexample.domain.Role;
import com.gdg.jwtexample.domain.User;
import com.gdg.jwtexample.dto.jwt.TokenRes;
import com.gdg.jwtexample.dto.user.UserLoginReq;
import com.gdg.jwtexample.dto.user.UserSignUpReq;
import com.gdg.jwtexample.exception.CustomException;
import com.gdg.jwtexample.exception.ErrorCode;
import com.gdg.jwtexample.repository.UserRepository;
import com.gdg.jwtexample.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Long signUp(UserSignUpReq request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Role role = request.getRole() != null ? request.getRole() : Role.USER;

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(encodedPassword)
                .role(role)
                .build();

        return userRepository.save(user).getId();
    }

    public TokenRes login(UserLoginReq request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        String token = jwtTokenProvider.createToken(user.getEmail(), user.getRole().toString());

        return TokenRes.of(token);
    }
}

