package ru.codeportfolio.db;

import ru.codeportfolio.models.Match;

import java.util.List;

public interface MatchesDaoInterface {
    List<Match> getAll();

    int add(int homePlayerId, int guestPlayerId, int winnerId);

    int delete(int homePlayerId, int guestPlayerId);

    Match findById(int baseCurrencyId, int targetCurrencyId);

    int update(int homePlayerId, int guestPlayerId, int winnerId);
}
