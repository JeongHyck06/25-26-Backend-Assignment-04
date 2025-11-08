package com.gdg.jwtexample.controller;

import com.gdg.jwtexample.dto.user.UserInfoRes;
import com.gdg.jwtexample.dto.user.UserUpdateReq;
import com.gdg.jwtexample.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserInfoRes> getMyInfo(Authentication authentication) {
        String email = authentication.getName();
        UserInfoRes userInfo = userService.getUserInfo(email);
        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserInfoRes> getUserById(@PathVariable Long userId) {
        UserInfoRes userInfo = userService.getUserById(userId);
        return ResponseEntity.ok(userInfo);
    }

    @PutMapping("/me")
    public ResponseEntity<UserInfoRes> updateUser(
            Authentication authentication,
            @Valid @RequestBody UserUpdateReq request) {
        String email = authentication.getName();
        UserInfoRes userInfo = userService.updateUser(email, request);
        return ResponseEntity.ok(userInfo);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(Authentication authentication) {
        String email = authentication.getName();
        userService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }
}

