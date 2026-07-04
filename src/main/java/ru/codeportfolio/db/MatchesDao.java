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

@Repository
public class MatchesDao implements MatchesDaoInterface {

    @Autowired
    private final SessionFactory sessionFactory;

    public MatchesDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Match save(Match match) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.persist(match);

            session.getTransaction().commit();
            return match;
        } catch (ConstraintViolationException e) {
            throw new AlreadyExistException(e);
        } catch (RuntimeException e) {
            throw new DataAccessException(e);
        }

    }


    public List<Match> find(Player player) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            List<Match> matches = session.createQuery("from Match where homePlayer = :player " +
                            "or guestPlayer = :player", Match.class)
                    .setParameter("player", player)
                    .list();

            session.getTransaction().commit();
            return matches;
        }  catch (NonUniqueResultException e) {
            throw new CannotFindNessesaryEntity(e);
        } catch (RuntimeException e) {
            throw new DataAccessException("Database Error", e);
        }
    }


    @Override
    public List<Match> getAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            List<Match> matches = session.createQuery("from Match ", Match.class).list();

            session.getTransaction().commit();
            return matches;
        } catch (RuntimeException e) {
            throw new DataAccessException("Database Error", e);
        }
    }


    @Override
    public Optional<Match> update(Player homePlayer, Player guestPlayer, Player winner) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Match match = session.createQuery("from Match where homePlayer = :homePlayer " +
                            "and guestPlayer = :guestPlayer", Match.class)
                    .setParameter("homePlayer", homePlayer)
                    .setParameter("guestPlayer", guestPlayer).uniqueResult();

            if (match == null) {
                return Optional.empty();
            }
            match.setWinner(winner);

            session.getTransaction().commit();
            return Optional.of(match);
        } catch (NonUniqueResultException e) {
            throw new CannotFindNessesaryEntity(e);
        } catch (RuntimeException e) {
            throw new DataAccessException("Database Error", e);
        }
    }


    @Override
    public boolean delete(Player homePlayer, Player guestPlayer) {
        boolean result = false;

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Match match = session.createQuery("from Match where homePlayer = :homePlayer " +
                            "and guestPlayer = :guestPlayer", Match.class)
                    .setParameter("homePlayer", homePlayer)
                    .setParameter("guestPlayer", guestPlayer).uniqueResult();
            if (match != null) {
                session.remove(match);
                result = true;
            }

            session.getTransaction().commit();
        }  catch (NonUniqueResultException e) {
            throw new CannotFindNessesaryEntity(e);
        } catch (RuntimeException e) {
            throw new DataAccessException("Database Error", e);
        }

        return result;
    }

}
