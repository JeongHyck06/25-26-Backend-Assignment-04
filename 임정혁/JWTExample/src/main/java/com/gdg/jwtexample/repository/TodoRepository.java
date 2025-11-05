package com.gdg.jwtexample.repository;

import com.gdg.jwtexample.domain.Priority;
import com.gdg.jwtexample.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Todo t WHERE t.id = :todoId AND t.user.id = :userId")
    boolean existsByIdAndUserId(@Param("todoId") Long todoId, @Param("userId") Long userId);

    Optional<Todo> findByIdAndUserId(Long id, Long userId);
}
