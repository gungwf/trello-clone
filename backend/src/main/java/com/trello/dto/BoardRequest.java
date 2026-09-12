package com.trello.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record BoardRequest(
        @NotNull(message = "ownerId is required")
        UUID ownerId,

        @NotBlank(message = "title is required")
        @Size(max = 160, message = "title must not exceed 160 characters")
        String title
) {
}