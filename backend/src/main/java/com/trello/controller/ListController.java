package com.trello.controller;

import com.trello.dto.ListMoveRequest;
import com.trello.dto.ListRequest;
import com.trello.dto.ListResponse;
import com.trello.dto.ListUpdateRequest;
import com.trello.service.ListService;
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
@RequestMapping("/api/v1/lists")
public class ListController {

    private final ListService listService;

    public ListController(ListService listService) {
        this.listService = listService;
    }

    @PostMapping
    public ResponseEntity<ListResponse> create(@Valid @RequestBody ListRequest request) {
        ListResponse response = listService.create(request);
        return ResponseEntity.created(URI.create("/api/v1/lists/" + response.id())).body(response);
    }

    @PutMapping("/{id}")
    public ListResponse rename(
            @PathVariable UUID id,
            @Valid @RequestBody ListUpdateRequest request
    ) {
        return listService.rename(id, request.title());
    }

    @PutMapping("/{id}/move")
    public ListResponse move(
            @PathVariable UUID id,
            @Valid @RequestBody ListMoveRequest request
    ) {
        return listService.move(id, request);
    }
}