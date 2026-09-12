package com.trello.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record CardMoveRequest(
        @NotNull(message = "targetListId is required")
        UUID targetListId,

        @Size(max = 64, message = "prevRank must not exceed 64 characters")
        @Pattern(regexp = "[0-9a-z]+", message = "prevRank must contain only lowercase letters and digits")
        String prevRank,

        @Size(max = 64, message = "nextRank must not exceed 64 characters")
        @Pattern(regexp = "[0-9a-z]+", message = "nextRank must contain only lowercase letters and digits")
        String nextRank
) {
}