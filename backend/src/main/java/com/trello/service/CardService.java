package com.trello.service;

import com.trello.domain.entity.Card;
import com.trello.domain.entity.List;
import com.trello.dto.CardRequest;
import com.trello.dto.CardMoveRequest;
import com.trello.dto.CardResponse;
import com.trello.dto.CardRealtimeEvent;
import com.trello.dto.CardUpdateRequest;
import com.trello.repository.CardRepository;
import com.trello.repository.ListRepository;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final ListRepository listRepository;
    private final LexoRankService lexoRankService;
    private final SimpMessagingTemplate messagingTemplate;

    public CardService(
            CardRepository cardRepository,
            ListRepository listRepository,
            LexoRankService lexoRankService,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.cardRepository = cardRepository;
        this.listRepository = listRepository;
        this.lexoRankService = lexoRankService;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public CardResponse create(CardRequest request) {
        List list = getList(request.listId());
        Card card = Card.builder()
                .list(list)
                .title(request.title())
                .description(request.description())
                .rank(request.rank())
                .build();
        return toResponse(cardRepository.save(card));
    }

    @Transactional
    public CardResponse update(UUID id, CardUpdateRequest request) {
        Card card = getCard(id);
        card.setTitle(request.title());
        card.setDescription(request.description());
        CardResponse response = toResponse(cardRepository.saveAndFlush(card));
        publish("CARD_UPDATED", card, response);
        return response;
    }

    @Transactional
    public CardResponse move(UUID id, CardMoveRequest request) {
        Card card = getCard(id);
        card.setList(getList(request.targetListId()));
        card.setRank(lexoRankService.calculateRank(request.prevRank(), request.nextRank()));
        CardResponse response = toResponse(cardRepository.saveAndFlush(card));
        publish("CARD_MOVED", card, response);
        return response;
    }

    private Card getCard(UUID id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> notFound("Card", id));
    }

    private List getList(UUID id) {
        return listRepository.findById(id)
                .orElseThrow(() -> notFound("List", id));
    }

    private CardResponse toResponse(Card card) {
        return new CardResponse(
                card.getId(),
                card.getList().getId(),
                card.getTitle(),
                card.getDescription(),
                card.getRank(),
                card.getCreatedAt());
    }

    private void publish(String eventType, Card card, CardResponse response) {
        UUID boardId = card.getList().getBoard().getId();
        messagingTemplate.convertAndSend(
                "/topic/board/" + boardId,
                new CardRealtimeEvent(eventType, boardId, response));
    }

    private ResponseStatusException notFound(String resource, UUID id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, resource + " not found: " + id);
    }
}