package com.gdg.jwtexample.repository;

import com.gdg.jwtexample.domain.RefreshToken;
import com.gdg.jwtexample.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByExpiryDateBefore(LocalDateTime now);
}

