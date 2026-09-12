package com.trello.repository;

import com.trello.domain.entity.Board;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, UUID> {

    List<Board> findByOwnerIdOrderByCreatedAtDesc(UUID ownerId);
}