package ru.codeportfolio.db;

import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.codeportfolio.exceptions.AlreadyExistException;
import ru.codeportfolio.exceptions.CannotFindNessesaryEntity;
import ru.codeportfolio.exceptions.DataAccessException;
import java.util.function.Function;

@Component
public class TransactionManager {
    @Autowired
    private final SessionFactory sessionFactory;

    public TransactionManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public <T> T executeInTransaction(Function<Session, T> action) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                T result = action.apply(session);
                tx.commit();
                return result;
            } catch (NonUniqueResultException e) {
                tx.rollback();
                throw new CannotFindNessesaryEntity(e);
            } catch (ConstraintViolationException e) {
                tx.rollback();
                throw new AlreadyExistException(e);
            } catch (RuntimeException e) {
                tx.rollback();
                throw new DataAccessException("Database Error", e);
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }
}
