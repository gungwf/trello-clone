package com.trello.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CardUpdateRequest(
        @NotBlank(message = "title is required")
        @Size(max = 240, message = "title must not exceed 240 characters")
        String title,

        @Size(max = 10000, message = "description must not exceed 10000 characters")
        String description
) {
}