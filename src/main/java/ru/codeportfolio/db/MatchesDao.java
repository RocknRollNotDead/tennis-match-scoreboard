package ru.codeportfolio.db;

import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.codeportfolio.exceptions.AlreadyExistException;
import ru.codeportfolio.exceptions.CannotFindNessesaryEntity;
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



    @Override
    public Match save(Player homePlayer, Player guestPlayer, Player winner) {
        // кидает TransientPropertyValueException, как и все кроме get
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Match match = new Match(homePlayer, guestPlayer, winner);
            session.persist(match);  // может кинуть ConstraintViolationException

            session.getTransaction().commit();
            return match;
        } catch (ConstraintViolationException e){
            throw  new AlreadyExistException(e);
        }
        // такое уже есть
        // ошибка базы
    }

    public Match save(Match match){
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.persist(match); // может кинуть ConstraintViolationException

            session.getTransaction().commit();
            return match;
        } catch (ConstraintViolationException e){
            throw  new AlreadyExistException(e);
        }

    }



    @Override
    public List<Match> find(Player homePlayer, Player guestPlayer){
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            List<Match> matches = session.createQuery("from Match where homePlayer = :homePlayer " +
                    "and guestPlayer = :guestPlayer", Match.class)
                    .setParameter("homePlayer", homePlayer)
                    .setParameter("guestPlayer", guestPlayer).list();

            session.getTransaction().commit();
            return matches;
        }
        // не нашлось
        // ошибка базы


    }


    public List<Match> find(Player player){
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            List<Match> matches = session.createQuery("from Match where homePlayer = :player " +
                            "or guestPlayer = :player", Match.class)
                    .setParameter("player", player)
                    .list();

            session.getTransaction().commit();
            return matches;
        }
        // не нашлось
        // ошибка базы


    }


    @Override
    public List<Match> getAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            List<Match> matches = session.createQuery("from Match ", Match.class).list();

            session.getTransaction().commit();
            return matches;
        }
    }


    @Override
    public Optional<Match> update(Player homePlayer, Player guestPlayer, Player winner){
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Match match = session.createQuery("from Match where homePlayer = :homePlayer " +
                            "and guestPlayer = :guestPlayer", Match.class)
                    .setParameter("homePlayer", homePlayer)
                    .setParameter("guestPlayer", guestPlayer).uniqueResult(); // может вернуть null

            if(match == null){
                return Optional.empty();
            }
            match.setWinner(winner);  // может кинуть ConstraintViolationException если null

            session.getTransaction().commit();
            return Optional.of(match);
        } catch (NonUniqueResultException e){
            throw new CannotFindNessesaryEntity(e);
        }
        // такого нет
        // такие значения уже есть (не обновилось)
        // ошибка базы
    }


    @Override
    public boolean delete(Player homePlayer, Player guestPlayer){
        boolean result = false;

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Match match = session.createQuery("from Match where homePlayer = :homePlayer " +
                            "and guestPlayer = :guestPlayer", Match.class)
                    .setParameter("homePlayer", homePlayer)
                    .setParameter("guestPlayer", guestPlayer).uniqueResult();  // может кинуть NonUniqueResultException
            if (match != null){
                session.remove(match);
                result = true;
            }

            session.getTransaction().commit();
        }

        return result;

        // не нашлось
        // ошибка бд
    }

}
