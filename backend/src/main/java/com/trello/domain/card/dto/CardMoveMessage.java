package com.trello.domain.card.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CardMoveMessage(
        @NotNull UUID cardId,
        @NotNull UUID targetListId,
        String previousRank,
        String nextRank
) {
}
