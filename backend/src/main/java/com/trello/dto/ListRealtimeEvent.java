package com.trello.dto;

import java.util.UUID;

public record ListRealtimeEvent(
        String eventType,
        UUID boardId,
        ListResponse list
) {
}