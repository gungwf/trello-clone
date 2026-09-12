package com.trello.controller;

import com.trello.dto.CardMoveRequest;
import com.trello.dto.CardRequest;
import com.trello.dto.CardResponse;
import com.trello.dto.CardUpdateRequest;
import com.trello.service.CardService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    public ResponseEntity<CardResponse> create(@Valid @RequestBody CardRequest request) {
        CardResponse response = cardService.create(request);
        return ResponseEntity.created(URI.create("/api/v1/cards/" + response.id())).body(response);
    }

    @PutMapping("/{id}")
    public CardResponse update(
            @PathVariable UUID id,
            @Valid @RequestBody CardUpdateRequest request
    ) {
        return cardService.update(id, request);
    }

    @PutMapping("/{id}/move")
    public CardResponse move(
            @PathVariable UUID id,
            @Valid @RequestBody CardMoveRequest request
    ) {
        return cardService.move(id, request);
    }
}