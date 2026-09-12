package com.trello.dto;

import java.time.Instant;
import java.util.UUID;

public record BoardResponse(
        UUID id,
        String title,
        UUID ownerId,
        Instant createdAt,
        java.util.List<ListResponse> lists
) {
}