package com.trello.service;

import com.trello.domain.entity.Board;
import com.trello.domain.entity.List;
import com.trello.dto.CardResponse;
import com.trello.dto.ListRealtimeEvent;
import com.trello.dto.ListMoveRequest;
import com.trello.dto.ListRequest;
import com.trello.dto.ListResponse;
import com.trello.repository.BoardRepository;
import com.trello.repository.ListRepository;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ListService {

    private final ListRepository listRepository;
    private final BoardRepository boardRepository;
    private final LexoRankService lexoRankService;
    private final SimpMessagingTemplate messagingTemplate;

    public ListService(
            ListRepository listRepository,
            BoardRepository boardRepository,
            LexoRankService lexoRankService,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.listRepository = listRepository;
        this.boardRepository = boardRepository;
        this.lexoRankService = lexoRankService;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public ListResponse create(ListRequest request) {
        Board board = boardRepository.findById(request.boardId())
                .orElseThrow(() -> notFound("Board", request.boardId()));
        List list = List.builder()
                .board(board)
                .title(request.title())
                .rank(request.rank())
                .build();
        return toResponse(listRepository.save(list));
    }

    @Transactional
    public ListResponse rename(UUID id, String title) {
        List list = getList(id);
        list.setTitle(title);
        ListResponse response = toResponse(listRepository.saveAndFlush(list));
        publish("LIST_UPDATED", list, response);
        return response;
    }

    @Transactional
    public ListResponse move(UUID id, ListMoveRequest request) {
        List list = getList(id);
        list.setRank(lexoRankService.calculateRank(request.prevRank(), request.nextRank()));
        ListResponse response = toResponse(listRepository.saveAndFlush(list));
        publish("LIST_MOVED", list, response);
        return response;
    }

    private List getList(UUID id) {
        return listRepository.findById(id)
                .orElseThrow(() -> notFound("List", id));
    }

    private ListResponse toResponse(List list) {
        java.util.List<CardResponse> cards = list.getCards().stream()
                .map(card -> new CardResponse(
                        card.getId(), card.getList().getId(), card.getTitle(),
                        card.getDescription(), card.getRank(), card.getCreatedAt()))
                .toList();
        return new ListResponse(
                list.getId(), list.getBoard().getId(), list.getTitle(),
                list.getRank(), list.getCreatedAt(), cards);
    }

    private void publish(String eventType, List list, ListResponse response) {
        UUID boardId = list.getBoard().getId();
        messagingTemplate.convertAndSend(
                "/topic/board/" + boardId,
                new ListRealtimeEvent(eventType, boardId, response));
    }

    private ResponseStatusException notFound(String resource, UUID id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, resource + " not found: " + id);
    }
}