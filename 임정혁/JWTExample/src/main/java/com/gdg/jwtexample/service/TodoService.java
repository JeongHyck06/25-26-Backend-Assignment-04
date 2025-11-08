package com.gdg.jwtexample.service;

import com.gdg.jwtexample.domain.Priority;
import com.gdg.jwtexample.domain.Todo;
import com.gdg.jwtexample.domain.User;
import com.gdg.jwtexample.dto.todo.TodoCreateReq;
import com.gdg.jwtexample.dto.todo.TodoInfoRes;
import com.gdg.jwtexample.dto.todo.TodoUpdateReq;
import com.gdg.jwtexample.exception.CustomException;
import com.gdg.jwtexample.exception.ErrorCode;
import com.gdg.jwtexample.repository.TodoRepository;
import com.gdg.jwtexample.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    @Transactional
    public TodoInfoRes createTodo(String email, TodoCreateReq request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Priority priority = request.getPriority() != null ? request.getPriority() : Priority.MEDIUM;

        Todo todo = Todo.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(priority)
                .dueDate(request.getDueDate())
                .build();

        Todo savedTodo = todoRepository.save(todo);

        return TodoInfoRes.from(savedTodo);
    }

    public TodoInfoRes getTodoById(String email, Long todoId) {
        Todo todo = getTodoByIdAndValidateOwner(todoId, email);
        return TodoInfoRes.from(todo);
    }

    public List<TodoInfoRes> getAllTodos(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return todoRepository.findByUserId(user.getId()).stream()
                .map(TodoInfoRes::from)
                .collect(Collectors.toList());
    }

    public List<TodoInfoRes> getTodosByCompleted(String email, Boolean completed) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return todoRepository.findByUserIdAndCompleted(user.getId(), completed).stream()
                .map(TodoInfoRes::from)
                .collect(Collectors.toList());
    }

    public List<TodoInfoRes> getTodosByPriority(String email, Priority priority) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return todoRepository.findByUserIdAndPriority(user.getId(), priority).stream()
                .map(TodoInfoRes::from)
                .collect(Collectors.toList());
    }

    public List<TodoInfoRes> getOverdueTodos(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return todoRepository.findByUserIdAndDueDateBefore(user.getId(), LocalDate.now()).stream()
                .map(TodoInfoRes::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public TodoInfoRes updateTodo(String email, Long todoId, TodoUpdateReq request) {
        Todo todo = getTodoByIdAndValidateOwner(todoId, email);

        if (request.getTitle() != null) {
            todo.updateTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            todo.updateDescription(request.getDescription());
        }

        if (request.getPriority() != null) {
            todo.updatePriority(request.getPriority());
        }

        if (request.getDueDate() != null) {
            todo.updateDueDate(request.getDueDate());
        }

        if (request.getCompleted() != null) {
            if (request.getCompleted()) {
                todo.complete();
            } else {
                todo.incomplete();
            }
        }

        return TodoInfoRes.from(todo);
    }

    @Transactional
    public void deleteTodo(String email, Long todoId) {
        Todo todo = getTodoByIdAndValidateOwner(todoId, email);
        todoRepository.delete(todo);
    }

    private Todo getTodoByIdAndValidateOwner(Long todoId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));

        if (!todo.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.TODO_ACCESS_DENIED);
        }

        return todo;
    }
}

