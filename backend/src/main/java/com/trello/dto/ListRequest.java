package com.trello.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record ListRequest(
        @NotNull(message = "boardId is required")
        UUID boardId,

        @NotBlank(message = "title is required")
        @Size(max = 160, message = "title must not exceed 160 characters")
        String title,

        @NotBlank(message = "rank is required")
        @Size(max = 64, message = "rank must not exceed 64 characters")
        @Pattern(regexp = "[0-9a-z]+", message = "rank must contain only lowercase letters and digits")
        String rank
) {
}