package com.trello.domain.card.service;

import org.springframework.stereotype.Service;

@Service
public class CardRankService {

    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final int MAX_RANK_LENGTH = 64;

    /**
     * Calculates a lexicographical rank strictly between two existing ranks.
     * A null previous rank means the card is inserted at the beginning; a null
     * next rank means it is inserted at the end.
     */
    public String calculateRank(String previousRank, String nextRank) {
        validateRank(previousRank, "previousRank");
        validateRank(nextRank, "nextRank");

        if (previousRank != null && nextRank != null
                && previousRank.compareTo(nextRank) >= 0) {
            throw new IllegalArgumentException("previousRank must be less than nextRank");
        }

        StringBuilder rank = new StringBuilder();
        for (int position = 0; position < MAX_RANK_LENGTH; position++) {
            int lowerDigit = digitAt(previousRank, position, -1);
            int upperDigit = digitAt(nextRank, position, ALPHABET.length());

            if (lowerDigit + 1 < upperDigit) {
                rank.append(ALPHABET.charAt((lowerDigit + upperDigit) / 2));
                return rank.toString();
            }

            if (lowerDigit < 0 && upperDigit == 0) {
                throw new IllegalStateException("No rank available before the smallest rank");
            }

            if (lowerDigit >= 0) {
                rank.append(ALPHABET.charAt(lowerDigit));
            }
        }

        throw new IllegalStateException("No rank available within 64 characters");
    }

    private int digitAt(String value, int position, int absentValue) {
        if (value == null || position >= value.length()) {
            return absentValue;
        }

        return ALPHABET.indexOf(value.charAt(position));
    }

    private void validateRank(String rank, String parameterName) {
        if (rank == null) {
            return;
        }
        if (rank.isEmpty() || rank.length() > MAX_RANK_LENGTH) {
            throw new IllegalArgumentException(parameterName + " must contain 1 to 64 characters");
        }
        for (int position = 0; position < rank.length(); position++) {
            if (ALPHABET.indexOf(rank.charAt(position)) < 0) {
                throw new IllegalArgumentException(parameterName + " contains an unsupported character");
            }
        }
    }
}
