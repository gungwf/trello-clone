package com.trello.domain.card.dto;

import java.util.UUID;

public record CardMovedEvent(
        String eventType,
        UUID boardId,
        UUID cardId,
        UUID targetListId,
        String rank
) {
}
