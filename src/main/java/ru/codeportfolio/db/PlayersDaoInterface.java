package ru.codeportfolio.db;

import ru.codeportfolio.models.Player;

import java.util.List;

// CR..D (no update)

public interface PlayersDaoInterface {
    List<Player> getAll();

    int add(String name);

    Player findByName(String name);

    int delete(String name);
}
