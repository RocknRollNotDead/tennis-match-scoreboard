package ru.codeportfolio.db;

import ru.codeportfolio.models.Match;
import ru.codeportfolio.models.Player;

import java.util.List;
import java.util.Optional;

public interface MatchesDaoInterface {

    Match save(Player homePlayer, Player guestPlayer, Player winner);
    Match save(Match match);

    List<Match> find(Player homePlayer, Player guestPlayer);
    List<Match> getAll();

    Optional<Match> update(Player homePlayer, Player guestPlayer, Player winner);

    boolean delete(Player homePlayer, Player guestPlayer);
}
