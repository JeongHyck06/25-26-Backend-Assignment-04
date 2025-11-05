package com.gdg.jwtexample.dto.todo;

import com.gdg.jwtexample.domain.Priority;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoUpdateReq {

    @Size(max = 200, message = "제목은 200자 이하여야 합니다.")
    private String title;

    private String description;

    private Boolean completed;

    private Priority priority;

    private LocalDate dueDate;
}

