package com.trello.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record CardRequest(
        @NotNull(message = "listId is required")
        UUID listId,

        @NotBlank(message = "title is required")
        @Size(max = 240, message = "title must not exceed 240 characters")
        String title,

        @Size(max = 10000, message = "description must not exceed 10000 characters")
        String description,

        @NotBlank(message = "rank is required")
        @Size(max = 64, message = "rank must not exceed 64 characters")
        @Pattern(regexp = "[0-9a-z]+", message = "rank must contain only lowercase letters and digits")
        String rank
) {
}