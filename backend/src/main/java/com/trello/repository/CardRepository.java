package com.trello.repository;

import com.trello.domain.entity.Card;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, UUID> {

    java.util.List<Card> findByListIdOrderByRankAsc(UUID listId);
}