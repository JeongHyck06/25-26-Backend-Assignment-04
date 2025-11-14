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

    @Transactional(readOnly = false)
    public TodoInfoRes createTodo(String email, TodoCreateReq request) {
        User user = findUserByEmail(email);

        Priority priority = request.priority() != null ? request.priority() : Priority.MEDIUM;

        Todo todo = Todo.builder()
                .user(user)
                .title(request.title())
                .description(request.description())
                .priority(priority)
                .dueDate(request.dueDate())
                .build();

        Todo savedTodo = todoRepository.save(todo);

        return TodoInfoRes.from(savedTodo);
    }

    public TodoInfoRes getTodoById(String email, Long todoId) {
        Todo todo = findTodoWithOwnerValidation(todoId, email);
        return TodoInfoRes.from(todo);
    }

    public List<TodoInfoRes> getAllTodos(String email) {
        User user = findUserByEmail(email);
        return todoRepository.findByUserId(user.getId()).stream()
                .map(TodoInfoRes::from)
                .collect(Collectors.toList());
    }

    public List<TodoInfoRes> getTodosByCompleted(String email, Boolean completed) {
        User user = findUserByEmail(email);
        return todoRepository.findByUserIdAndCompleted(user.getId(), completed).stream()
                .map(TodoInfoRes::from)
                .collect(Collectors.toList());
    }

    public List<TodoInfoRes> getTodosByPriority(String email, Priority priority) {
        User user = findUserByEmail(email);
        return todoRepository.findByUserIdAndPriority(user.getId(), priority).stream()
                .map(TodoInfoRes::from)
                .collect(Collectors.toList());
    }

    public List<TodoInfoRes> getOverdueTodos(String email) {
        User user = findUserByEmail(email);
        return todoRepository.findByUserIdAndDueDateBefore(user.getId(), LocalDate.now()).stream()
                .map(TodoInfoRes::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = false)
    public TodoInfoRes updateTodo(String email, Long todoId, TodoUpdateReq request) {
        Todo todo = findTodoWithOwnerValidation(todoId, email);

        todo.updateTitle(request.title());
        todo.updateDescription(request.description());
        todo.updatePriority(request.priority());
        todo.updateDueDate(request.dueDate());

        if (request.completed()) {
            todo.complete();
        } else {
            todo.incomplete();
        }

        return TodoInfoRes.from(todo);
    }

    @Transactional(readOnly = false)
    public void deleteTodo(String email, Long todoId) {
        Todo todo = findTodoWithOwnerValidation(todoId, email);
        todoRepository.delete(todo);
    }

    private Todo findTodoWithOwnerValidation(Long todoId, String email) {
        User user = findUserByEmail(email);
        Todo todo = findTodoById(todoId);
        validateOwner(todo, user);
        return todo;
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private Todo findTodoById(Long todoId) {
        return todoRepository.findById(todoId)
                .orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));
    }

    private void validateOwner(Todo todo, User user) {
        if (!todo.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.TODO_ACCESS_DENIED);
        }
    }
}

