package com.trello.service;

import com.trello.domain.entity.Board;
import com.trello.domain.entity.Card;
import com.trello.domain.entity.List;
import com.trello.domain.entity.User;
import com.trello.dto.BoardRequest;
import com.trello.dto.BoardResponse;
import com.trello.dto.CardResponse;
import com.trello.dto.ListResponse;
import com.trello.repository.BoardRepository;
import com.trello.repository.UserRepository;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public BoardService(BoardRepository boardRepository, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public BoardResponse create(BoardRequest request) {
        User owner = userRepository.findById(request.ownerId())
                .orElseThrow(() -> notFound("User", request.ownerId()));
        Board board = Board.builder()
                .owner(owner)
                .title(request.title())
                .build();
        return toResponse(boardRepository.save(board));
    }

    @Transactional(readOnly = true)
    public java.util.List<BoardResponse> findByOwner(UUID ownerId) {
        return boardRepository.findByOwnerIdOrderByCreatedAtDesc(ownerId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BoardResponse findById(UUID id) {
        return toResponse(boardRepository.findById(id)
                .orElseThrow(() -> notFound("Board", id)));
    }

    private BoardResponse toResponse(Board board) {
        java.util.List<ListResponse> lists = board.getLists().stream()
                .map(this::toListResponse)
                .toList();
        return new BoardResponse(
                board.getId(),
                board.getTitle(),
                board.getOwner().getId(),
                board.getCreatedAt(),
                lists
        );
    }

    private ListResponse toListResponse(List list) {
        java.util.List<CardResponse> cards = list.getCards().stream()
                .map(this::toCardResponse)
                .toList();
        return new ListResponse(
                list.getId(),
                list.getBoard().getId(),
                list.getTitle(),
                list.getRank(),
                list.getCreatedAt(),
                cards
        );
    }

    private CardResponse toCardResponse(Card card) {
        return new CardResponse(
                card.getId(),
                card.getList().getId(),
                card.getTitle(),
                card.getDescription(),
                card.getRank(),
                card.getCreatedAt()
        );
    }

    private ResponseStatusException notFound(String resource, UUID id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, resource + " not found: " + id);
    }
}