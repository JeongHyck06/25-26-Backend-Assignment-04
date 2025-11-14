package com.gdg.jwtexample.service;

import com.gdg.jwtexample.domain.User;
import com.gdg.jwtexample.dto.user.UserInfoRes;
import com.gdg.jwtexample.dto.user.UserUpdateReq;
import com.gdg.jwtexample.exception.CustomException;
import com.gdg.jwtexample.exception.ErrorCode;
import com.gdg.jwtexample.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserInfoRes getUserInfo(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return UserInfoRes.from(user);
    }

    public UserInfoRes getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return UserInfoRes.from(user);
    }

    @Transactional(readOnly = false)
    public UserInfoRes updateUser(String email, UserUpdateReq request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updateUserName(request.username());
        String encodedPassword = passwordEncoder.encode(request.password());
        user.updatePassword(encodedPassword);

        return UserInfoRes.from(user);
    }

    @Transactional(readOnly = false)
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        userRepository.delete(user);
    }
}

