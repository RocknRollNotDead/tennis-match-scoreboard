package ru.codeportfolio.db;

import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.codeportfolio.exceptions.AlreadyExistException;
import ru.codeportfolio.exceptions.CannotFindNessesaryEntity;
import ru.codeportfolio.exceptions.DataAccessException;
import ru.codeportfolio.models.entities.Player;
import ru.codeportfolio.models.entities.Match;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
public class MatchesDao implements MatchesDaoInterface {

    @Autowired
    private final SessionFactory sessionFactory;

    @Autowired
    private final TransactionManager manager;

    public MatchesDao(SessionFactory sessionFactory, TransactionManager manager) {
        this.sessionFactory = sessionFactory;
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


    @Override
    public Optional<Match> update(Player homePlayer, Player guestPlayer, Player winner) {
        return manager.executeInTransaction(session ->
        {

            Match match = session.createQuery("from Match where homePlayer = :homePlayer " +
                            "and guestPlayer = :guestPlayer", Match.class)
                    .setParameter("homePlayer", homePlayer)
                    .setParameter("guestPlayer", guestPlayer).uniqueResult();

            if (match == null) {
                return Optional.empty();
            }
            match.setWinner(winner);
            return Optional.of(match);

        });

    }


    @Override
    public boolean delete(Player homePlayer, Player guestPlayer) {

        return manager.executeInTransaction(session ->
        {
            boolean result = false;
            Match match = session.createQuery("from Match where homePlayer = :homePlayer " +
                            "and guestPlayer = :guestPlayer", Match.class)
                    .setParameter("homePlayer", homePlayer)
                    .setParameter("guestPlayer", guestPlayer).setMaxResults(1).uniqueResult();
            if (match != null) {
                session.remove(match);
                result = true;
            }
            return result;
        });
    }

}
