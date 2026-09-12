package com.trello.domain.card.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CardRankServiceTest {

    private final CardRankService cardRankService = new CardRankService();

    @Test
    void calculatesRankAtTheBeginning() {
        String rank = cardRankService.calculateRank(null, "a");

        assertTrue(rank.compareTo("a") < 0);
    }

    @Test
    void calculatesRankAtTheEnd() {
        String rank = cardRankService.calculateRank("z", null);

        assertTrue(rank.compareTo("z") > 0);
    }

    @Test
    void calculatesRankBetweenAdjacentRanks() {
        String rank = cardRankService.calculateRank("aaa", "aab");

        assertTrue(rank.compareTo("aaa") > 0);
        assertTrue(rank.compareTo("aab") < 0);
    }

    @Test
    void calculatesAStableMidpointWhenThereIsRoom() {
        assertEquals("h", cardRankService.calculateRank("0", "z"));
    }

    @Test
    void rejectsRanksInTheWrongOrder() {
        assertThrows(IllegalArgumentException.class,
                () -> cardRankService.calculateRank("b", "a"));
    }

    @Test
    void rejectsUnsupportedCharacters() {
        assertThrows(IllegalArgumentException.class,
                () -> cardRankService.calculateRank("A", "z"));
    }

    @Test
    void rejectsInsertionBeforeTheSmallestRank() {
        assertThrows(IllegalStateException.class,
                () -> cardRankService.calculateRank(null, "0"));
    }
}
