package com.trello.controller;

import com.trello.dto.BoardRequest;
import com.trello.dto.BoardResponse;
import com.trello.service.BoardService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    public ResponseEntity<BoardResponse> create(@Valid @RequestBody BoardRequest request) {
        BoardResponse response = boardService.create(request);
        return ResponseEntity.created(URI.create("/api/v1/boards/" + response.id())).body(response);
    }

    @GetMapping
    public java.util.List<BoardResponse> findByOwner(@RequestParam UUID ownerId) {
        return boardService.findByOwner(ownerId);
    }

    @GetMapping("/{id}")
    public BoardResponse findById(@PathVariable UUID id) {
        return boardService.findById(id);
    }
}