package com.trello.repository;

import com.trello.domain.entity.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListRepository extends JpaRepository<List, UUID> {

    java.util.List<List> findByBoardIdOrderByRankAsc(UUID boardId);
}