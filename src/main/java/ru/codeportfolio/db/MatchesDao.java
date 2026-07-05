package ru.codeportfolio.db;

import org.springframework.stereotype.Repository;
import ru.codeportfolio.models.entities.Match;

import java.util.List;

@Repository
public class MatchesDao implements MatchesDaoInterface {

    private final TransactionManager manager;

    private static final String SEARCH_FOR_NAME = " where homePlayer.name like :player " +
            "or guestPlayer.name like :player ";

    public MatchesDao(TransactionManager manager) {
        this.manager = manager;
    }

    public Match save(Match match) {

        return manager.executeInTransaction(session -> {

            session.persist(match);
            return match;

        });

    }

    @Override
    public List<Match> find(String name, int offset, int limit) {

        return manager.executeInTransaction(session ->
                {
                    return session.createQuery("from Match" + SEARCH_FOR_NAME +
                                    "order by homePlayer.name", Match.class)
                            .setParameter("player", "%" + name + "%")
                            .setFirstResult(offset)
                            .setMaxResults(limit)
                            .list();
                }
        );

    }

    @Override
    public List<Match> getAll(int offset, int limit) {
        return manager.executeInTransaction(session ->
                {
                    return session.createQuery("from Match order by homePlayer.name", Match.class)
                            .setFirstResult(offset)
                            .setMaxResults(limit)
                            .list();
                }
        );

    }

    public long countMatches() {
        return manager.executeInTransaction(session ->
                session.createQuery("select count(m) from Match m", Long.class)
                        .uniqueResult()
        );
    }

    public long countMatches(String name) {
        return manager.executeInTransaction(session ->
                session.createQuery("select count(m) from Match m where m.homePlayer.name like :player " +
                                "or m.guestPlayer.name like :player", Long.class)
                        .setParameter("player", "%" + name + "%")
                        .uniqueResult()
        );
    }

}
