package com.gdg.jwtexample.dto.todo;

import com.gdg.jwtexample.domain.Priority;
import com.gdg.jwtexample.domain.Todo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoInfoRes {

    private Long id;
    private Long userId;
    private String username;
    private String title;
    private String description;
    private Boolean completed;
    private Priority priority;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TodoInfoRes from(Todo todo) {
        return TodoInfoRes.builder()
                .id(todo.getId())
                .userId(todo.getUser().getId())
                .username(todo.getUser().getUsername())
                .title(todo.getTitle())
                .description(todo.getDescription())
                .completed(todo.getCompleted())
                .priority(todo.getPriority())
                .dueDate(todo.getDueDate())
                .createdAt(todo.getCreatedAt())
                .updatedAt(todo.getUpdatedAt())
                .build();
    }
}

