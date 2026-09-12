package com.trello.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class LexoRankServiceTest {

    private final LexoRankService lexoRankService = new LexoRankService();

    @Test
    void calculatesRankAtTheBeginning() {
        String rank = lexoRankService.calculateRank(null, "a");

        assertTrue(rank.compareTo("a") < 0);
    }

    @Test
    void calculatesRankAtTheEnd() {
        String rank = lexoRankService.calculateRank("z", null);

        assertTrue(rank.compareTo("z") > 0);
    }

    @Test
    void calculatesRankBetweenAdjacentRanks() {
        String rank = lexoRankService.calculateRank("aaa", "aab");

        assertTrue(rank.compareTo("aaa") > 0);
        assertTrue(rank.compareTo("aab") < 0);
    }

    @Test
    void calculatesStableMidpointWhenThereIsRoom() {
        assertEquals("h", lexoRankService.calculateRank("0", "z"));
    }

    @Test
    void rejectsRanksInTheWrongOrder() {
        assertThrows(IllegalArgumentException.class,
                () -> lexoRankService.calculateRank("b", "a"));
    }

    @Test
    void rejectsUnsupportedCharacters() {
        assertThrows(IllegalArgumentException.class,
                () -> lexoRankService.calculateRank("A", "z"));
    }

    @Test
    void rejectsInsertionBeforeTheSmallestRank() {
        assertThrows(IllegalStateException.class,
                () -> lexoRankService.calculateRank(null, "0"));
    }
}