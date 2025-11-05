package com.gdg.jwtexample.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenRes {

    private String accessToken;
    private String tokenType;

    public static TokenRes of(String accessToken) {
        return TokenRes.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .build();
    }
}
