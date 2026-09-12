package com.trello.dto;

import java.time.Instant;
import java.util.UUID;

public record CardResponse(
        UUID id,
        UUID listId,
        String title,
        String description,
        String rank,
        Instant createdAt
) {
}