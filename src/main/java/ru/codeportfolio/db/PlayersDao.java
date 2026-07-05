package ru.codeportfolio.db;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.codeportfolio.models.entities.Player;

import java.util.List;
import java.util.Optional;

@Repository
public class PlayersDao implements PlayersDaoInterface {

    private final TransactionManager manager;

    private static final Logger log = LoggerFactory.getLogger(PlayersDao.class);

    public PlayersDao(TransactionManager manager) {
        this.manager = manager;
    }

    public Player save(Player player) {

        return manager.executeInTransaction(session ->
        {
            session.persist(player);
            return player;
        });

    }

    @Override
    public Optional<Player> findByName(String name) {

        return manager.executeInTransaction(session ->
        {
            Player player = session.createQuery("from Player where name = :name", Player.class)
                    .setParameter("name", name).uniqueResult();
            return Optional.ofNullable(player);
        });
    }

    @Override
    public List<Player> getAll() {
        return manager.executeInTransaction(session ->
        {
            return session.createQuery("from Player", Player.class).list();
        });

    }
}

