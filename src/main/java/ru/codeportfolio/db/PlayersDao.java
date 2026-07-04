package ru.codeportfolio.db;


import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.codeportfolio.controllers.other.MatchesExceptionHandler;
import ru.codeportfolio.exceptions.AlreadyExistException;
import ru.codeportfolio.exceptions.CannotFindNessesaryEntity;
import ru.codeportfolio.exceptions.DataAccessException;
import ru.codeportfolio.models.entities.Player;

import java.util.List;
import java.util.Optional;
@Repository
public class PlayersDao implements PlayersDaoInterface {


    @Autowired
    private final SessionFactory factory;

    private static final Logger log = LoggerFactory.getLogger(PlayersDao.class);
    public PlayersDao(SessionFactory factory) {
        this.factory = factory;
    }


//todo переделать под transactionhelper


    public Player save(Player player) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();

            session.persist(player);

            session.getTransaction().commit();
            return player;
        } catch (ConstraintViolationException e){
            throw  new AlreadyExistException(e);
        } catch (RuntimeException e) {
            throw new DataAccessException("Database Error", e);
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
        } catch (NonUniqueResultException e) {
            throw new CannotFindNessesaryEntity(e);
        } catch (RuntimeException e) {
            throw new DataAccessException("Database Error", e);
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
        } catch (RuntimeException e) {
            throw new DataAccessException("Database Error", e);
        }
    }



    @Override
    public boolean delete(String name) {
        boolean result = false;
        try (Session session = factory.openSession()) {

            session.beginTransaction();

            Player player = session.createQuery("from Player where name = :name", Player.class)
                    .setParameter("name", name).uniqueResult();
            if(player != null){
                session.remove(player);
                result = true;
            }

            session.getTransaction().commit();
        } catch (NonUniqueResultException e) {
            throw new CannotFindNessesaryEntity(e);
        } catch (RuntimeException e) {
            throw new DataAccessException("Database Error", e);
        }
        return result;

    }
}

