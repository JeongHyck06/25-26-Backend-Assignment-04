package com.gdg.jwtexample.dto.todo;

import com.gdg.jwtexample.domain.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TodoUpdateReq(
    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 200, message = "제목은 200자 이하여야 합니다.")
    String title,

    String description,

    @NotNull(message = "완료 여부는 필수입니다.")
    Boolean completed,

    @NotNull(message = "우선순위는 필수입니다.")
    Priority priority,

    LocalDate dueDate
) {}

