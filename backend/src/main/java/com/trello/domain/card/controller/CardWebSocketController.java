package com.trello.domain.card.controller;

import com.trello.domain.card.dto.CardMoveMessage;
import com.trello.domain.card.dto.CardMovedEvent;
import com.trello.domain.card.service.CardRankService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class CardWebSocketController {

    private static final String CARD_MOVED_EVENT = "CARD_MOVED";
    private static final String BOARD_TOPIC = "/topic/board/";

    private final SimpMessagingTemplate messagingTemplate;
    private final CardRankService cardRankService;

    public CardWebSocketController(
            SimpMessagingTemplate messagingTemplate,
            CardRankService cardRankService
    ) {
        this.messagingTemplate = messagingTemplate;
        this.cardRankService = cardRankService;
    }

    @MessageMapping("/boards/{boardId}/cards/move")
    public void moveCard(
            @DestinationVariable UUID boardId,
            @Valid @Payload CardMoveMessage message
    ) {
        String rank = cardRankService.calculateRank(message.previousRank(), message.nextRank());
        CardMovedEvent event = new CardMovedEvent(
                CARD_MOVED_EVENT,
                boardId,
                message.cardId(),
                message.targetListId(),
                rank
        );

        messagingTemplate.convertAndSend(BOARD_TOPIC + boardId, event);
    }
}
