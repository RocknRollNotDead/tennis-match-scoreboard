package ru.codeportfolio.db;

import ru.codeportfolio.models.entities.Match;
import ru.codeportfolio.models.entities.Player;

import java.util.List;
import java.util.Optional;

public interface MatchesDaoInterface {

    Match save(Match match);

    List<Match> find(Player player);
    List<Match> getAll();

    Optional<Match> update(Player homePlayer, Player guestPlayer, Player winner);

    boolean delete(Player homePlayer, Player guestPlayer);
}
