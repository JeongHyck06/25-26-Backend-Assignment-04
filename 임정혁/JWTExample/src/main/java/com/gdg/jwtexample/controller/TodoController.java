package com.gdg.jwtexample.controller;

import com.gdg.jwtexample.domain.Priority;
import com.gdg.jwtexample.dto.todo.TodoCreateReq;
import com.gdg.jwtexample.dto.todo.TodoInfoRes;
import com.gdg.jwtexample.dto.todo.TodoUpdateReq;
import com.gdg.jwtexample.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<TodoInfoRes> createTodo(
            Authentication authentication,
            @Valid @RequestBody TodoCreateReq request) {
        String email = authentication.getName();
        TodoInfoRes todo = todoService.createTodo(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(todo);
    }

    @GetMapping("/{todoId}")
    public ResponseEntity<TodoInfoRes> getTodoById(
            Authentication authentication,
            @PathVariable Long todoId) {
        String email = authentication.getName();
        TodoInfoRes todo = todoService.getTodoById(email, todoId);
        return ResponseEntity.ok(todo);
    }

    @GetMapping
    public ResponseEntity<List<TodoInfoRes>> getAllTodos(Authentication authentication) {
        String email = authentication.getName();
        List<TodoInfoRes> todos = todoService.getAllTodos(email);
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/filter/completed")
    public ResponseEntity<List<TodoInfoRes>> getTodosByCompleted(
            Authentication authentication,
            @RequestParam Boolean completed) {
        String email = authentication.getName();
        List<TodoInfoRes> todos = todoService.getTodosByCompleted(email, completed);
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/filter/priority")
    public ResponseEntity<List<TodoInfoRes>> getTodosByPriority(
            Authentication authentication,
            @RequestParam Priority priority) {
        String email = authentication.getName();
        List<TodoInfoRes> todos = todoService.getTodosByPriority(email, priority);
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/filter/overdue")
    public ResponseEntity<List<TodoInfoRes>> getOverdueTodos(Authentication authentication) {
        String email = authentication.getName();
        List<TodoInfoRes> todos = todoService.getOverdueTodos(email);
        return ResponseEntity.ok(todos);
    }

    @PutMapping("/{todoId}")
    public ResponseEntity<TodoInfoRes> updateTodo(
            Authentication authentication,
            @PathVariable Long todoId,
            @Valid @RequestBody TodoUpdateReq request) {
        String email = authentication.getName();
        TodoInfoRes todo = todoService.updateTodo(email, todoId, request);
        return ResponseEntity.ok(todo);
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> deleteTodo(
            Authentication authentication,
            @PathVariable Long todoId) {
        String email = authentication.getName();
        todoService.deleteTodo(email, todoId);
        return ResponseEntity.noContent().build();
    }
}

