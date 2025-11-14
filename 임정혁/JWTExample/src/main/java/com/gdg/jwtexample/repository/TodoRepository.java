package com.gdg.jwtexample.repository;

import com.gdg.jwtexample.domain.Priority;
import com.gdg.jwtexample.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByUserId(Long userId);

    List<Todo> findByUserIdAndCompleted(Long userId, Boolean completed);

    List<Todo> findByUserIdAndPriority(Long userId, Priority priority);

    List<Todo> findByUserIdAndDueDateBefore(Long userId, LocalDate date);

    boolean existsByIdAndUserId(Long todoId, Long userId);

    Optional<Todo> findByIdAndUserId(Long id, Long userId);
}
