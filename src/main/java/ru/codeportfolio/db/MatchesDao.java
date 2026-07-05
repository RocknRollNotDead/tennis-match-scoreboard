package ru.codeportfolio.db;

import org.springframework.stereotype.Repository;
import ru.codeportfolio.models.entities.Match;

import java.util.List;

@Repository
public class MatchesDao implements MatchesDaoInterface {

    private final TransactionManager manager;

    public MatchesDao(TransactionManager manager) {
        this.manager = manager;
    }

    public Match save(Match match) {

        return manager.executeInTransaction(session -> {

            session.persist(match);
            return match;

        });

    }

    public List<Match> find(String playerName) {

        return manager.executeInTransaction(session ->
                {
                    return session.createQuery("from Match where homePlayer.name like :player " +
                                    "or guestPlayer.name like :player", Match.class)
                            .setParameter("player", "%" + playerName + "%")
                            .list();
                }
        );

    }

    @Override
    public List<Match> getAll() {
        return manager.executeInTransaction(session ->
                {
                    return session.createQuery("from Match ", Match.class).list();
                }
        );

    }
}
