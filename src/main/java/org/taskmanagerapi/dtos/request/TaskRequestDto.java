package org.taskmanagerapi.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskRequestDto(
        @NotBlank(message = "Title is required")
        @Size(max = 100, message = "Title must be under 100 characters")
        String title,

        @Size(max = 500, message = "Description must be under 500 characters")
        String description,

        String status
) {
}
