package ru.codeportfolio.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.TransientPropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import ru.codeportfolio.exceptions.AlreadyExistException;
import ru.codeportfolio.exceptions.DataAccessException;
import ru.codeportfolio.models.Player;
import ru.codeportfolio.models.Match;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MatchesDao implements MatchesDaoInterface {

    private final SessionFactory factory;

    public MatchesDao(SessionFactory factory) {
        this.factory = factory;
    }



    @Override
    public Match save(Player homePlayer, Player guestPlayer, Player winner) {
        // кидает TransientPropertyValueException, как и все кроме get
        try (Session session = factory.openSession()) {
            session.beginTransaction();

            Match match = new Match(homePlayer, guestPlayer, winner);
            session.persist(match);  // может кинуть ConstraintViolationException

            session.getTransaction().commit();
            return match;
        }
        // такое уже есть
        // ошибка базы
    }

    public Match save(Match match){
        try (Session session = factory.openSession()) {
            session.beginTransaction();

            session.persist(match); // может кинуть ConstraintViolationException

            session.getTransaction().commit();
            return match;
        }

    }



    @Override
    public Optional<Match> find(Player homePlayer, Player guestPlayer){
        try (Session session = factory.openSession()) {
            session.beginTransaction();

            Match match = session.createQuery("from Match where homePlayer = :homePlayer " +
                    "and guestPlayer = :guestPlayer", Match.class)
                    .setParameter("homePlayer", homePlayer)
                    .setParameter("guestPlayer", guestPlayer).uniqueResult(); // может кинуть NonUniqueResultException

            session.getTransaction().commit();
            return Optional.ofNullable(match); // может вернуть null
        }
        // не нашлось
        // ошибка базы


    }

    @Override
    public List<Match> getAll() {
        try (Session session = factory.openSession()) {
            session.beginTransaction();

            List<Match> matches = session.createQuery("from Match ", Match.class).list();

            session.getTransaction().commit();
            return matches;
        }
    }


    @Override
    public Optional<Match> update(Player homePlayer, Player guestPlayer, Player winner){
        try (Session session = factory.openSession()) {
            session.beginTransaction();

            Match match = session.createQuery("from Match where homePlayer = :homePlayer " +
                            "and guestPlayer = :guestPlayer", Match.class)
                    .setParameter("homePlayer", homePlayer)
                    .setParameter("guestPlayer", guestPlayer).uniqueResult(); // может вернуть null

            match.setWinner(winner);  // может кинуть ConstraintViolationException если null

            session.getTransaction().commit();
            return Optional.of(match);
        }
        // такого нет
        // такие значения уже есть (не обновилось)
        // ошибка базы
    }


    @Override
    public boolean delete(Player homePlayer, Player guestPlayer){
        boolean result = false;

        try (Session session = factory.openSession()) {
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
