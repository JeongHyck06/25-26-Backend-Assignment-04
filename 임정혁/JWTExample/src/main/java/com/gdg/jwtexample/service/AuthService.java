package com.gdg.jwtexample.service;

import com.gdg.jwtexample.domain.RefreshToken;
import com.gdg.jwtexample.domain.Role;
import com.gdg.jwtexample.domain.User;
import com.gdg.jwtexample.dto.jwt.TokenRes;
import com.gdg.jwtexample.dto.user.UserLoginReq;
import com.gdg.jwtexample.dto.user.UserSignUpReq;
import com.gdg.jwtexample.exception.CustomException;
import com.gdg.jwtexample.exception.ErrorCode;
import com.gdg.jwtexample.repository.RefreshTokenRepository;
import com.gdg.jwtexample.repository.UserRepository;
import com.gdg.jwtexample.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
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

    @Transactional
    public TokenRes login(UserLoginReq request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        String accessToken = jwtTokenProvider.createToken(user.getEmail(), user.getRole().toString());
        String refreshToken = createAndSaveRefreshToken(user.getEmail());

        return TokenRes.of(accessToken, refreshToken);
    }

    @Transactional
    public TokenRes refresh(String refreshToken) {
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN));

        if (storedToken.isExpired()) {
            refreshTokenRepository.delete(storedToken);
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        }

        User user = userRepository.findByEmail(storedToken.getUserEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String newAccessToken = jwtTokenProvider.createToken(user.getEmail(), user.getRole().toString());
        String newRefreshToken = createAndSaveRefreshToken(user.getEmail());

        refreshTokenRepository.delete(storedToken);

        return TokenRes.of(newAccessToken, newRefreshToken);
    }

    @Transactional
    public void logout(String userEmail) {
        refreshTokenRepository.deleteByUserEmail(userEmail);
    }

    private String createAndSaveRefreshToken(String userEmail) {
        refreshTokenRepository.findByUserEmail(userEmail)
                .ifPresent(refreshTokenRepository::delete);

        String refreshToken = jwtTokenProvider.createRefreshToken();
        LocalDateTime expiryDate = LocalDateTime.now()
                .plusSeconds(jwtTokenProvider.getRefreshTokenValidityInMilliseconds() / 1000);

        RefreshToken token = RefreshToken.builder()
                .token(refreshToken)
                .userEmail(userEmail)
                .expiryDate(expiryDate)
                .build();

        refreshTokenRepository.save(token);
        return refreshToken;
    }
}

