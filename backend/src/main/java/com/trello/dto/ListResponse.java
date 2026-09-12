package com.trello.dto;

import java.time.Instant;
import java.util.UUID;

public record ListResponse(
        UUID id,
        UUID boardId,
        String title,
        String rank,
        Instant createdAt,
        java.util.List<CardResponse> cards
) {
}