package com.trello.dto;

import java.util.UUID;

public record CardRealtimeEvent(
        String eventType,
        UUID boardId,
        CardResponse card
) {
}