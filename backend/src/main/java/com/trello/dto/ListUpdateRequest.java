package com.trello.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ListUpdateRequest(
        @NotBlank(message = "title is required")
        @Size(max = 160, message = "title must not exceed 160 characters")
        String title
) {
}