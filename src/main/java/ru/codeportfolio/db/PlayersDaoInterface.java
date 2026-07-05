package ru.codeportfolio.db;

import ru.codeportfolio.models.entities.Player;

import java.util.List;
import java.util.Optional;

// CR..D (no update)

public interface PlayersDaoInterface {

    Player save(Player player);

    Optional<Player> findByName(String name);
    List<Player> getAll();
}
