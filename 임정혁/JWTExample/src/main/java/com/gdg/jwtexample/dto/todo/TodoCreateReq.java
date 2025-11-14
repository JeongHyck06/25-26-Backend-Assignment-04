package com.gdg.jwtexample.dto.todo;

import com.gdg.jwtexample.domain.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TodoCreateReq(
    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 200, message = "제목은 200자 이하여야 합니다.")
    String title,

    String description,

    Priority priority,

    LocalDate dueDate
) {}

