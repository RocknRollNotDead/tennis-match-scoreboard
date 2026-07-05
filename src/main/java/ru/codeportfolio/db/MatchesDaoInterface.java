package ru.codeportfolio.db;

import ru.codeportfolio.models.entities.Match;

import java.util.List;

public interface MatchesDaoInterface {

    Match save(Match match);

    List<Match> find(String player, int offset, int limit);
    List<Match> getAll(int offset, int limit);
    long countMatches();
    long countMatches(String name);

}
