package ru.codeportfolio.db;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import ru.codeportfolio.exceptions.AlreadyExistException;
import ru.codeportfolio.exceptions.CurrencyAlreadyExistException;
import ru.codeportfolio.exceptions.DataAccessException;
import ru.codeportfolio.models.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayersDao implements PlayersDaoInterface {



    private final SessionFactory factory;

    public PlayersDao(SessionFactory factory) {
        this.factory = factory;
    }


//todo переделать под transactionhelper

    @Override
    public Player save(String name) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();

            Player player = new Player(name);
            session.persist(player);

            session.getTransaction().commit();
            return player;
        }
        // сценарии
        // такое уже есть
        // ошибка базы (например, недоступна)


    }

    public Player save(Player player) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();

            session.persist(player);

            session.getTransaction().commit();
            return player;
        } catch (ConstraintViolationException e){
            throw  new AlreadyExistException(e); // условно
        }
    }

    @Override
    public Optional<Player> findByName(String name){

        try (Session session = factory.openSession()) {
            session.beginTransaction();

            Player player = session.createQuery("from Player where name = :name", Player.class)
                    .setParameter("name", name).uniqueResult(); // может кинуть NonUniqueResultException

            session.getTransaction().commit();
            return Optional.ofNullable(player);
        }

        // не нашлось
    }

    @Override
    public List<Player> getAll() {
        try (Session session = factory.openSession()) {
            session.beginTransaction();

            List<Player> players = session.createQuery("from Player", Player.class).list();

            session.getTransaction().commit();
            return players;
        }
        // ошибка базы
    }



    @Override
    public boolean delete(String name) {
        boolean result = false;
        try (Session session = factory.openSession()) {

            session.beginTransaction();

            Player player = session.createQuery("from Player where name = :name", Player.class)
                    .setParameter("name", name).uniqueResult();  // может кинуть NonUniqueResultException
            if(player != null){
                session.remove(player);
                result = true;
            }

            session.getTransaction().commit();
        }
        return result;

        // ошибка базы
    }
}

