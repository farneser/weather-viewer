package com.farneser.weatherviewer.dao;

import com.farneser.weatherviewer.exceptions.InternalServerException;
import com.farneser.weatherviewer.factory.HibernateFactory;
import org.hibernate.Transaction;

import java.util.Arrays;
import java.util.logging.Logger;

public abstract class BaseDao<T, K> {
    protected final Class<T> entityClass;
    private final Logger logger = Logger.getLogger(BaseDao.class.getName());

    public BaseDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public T create(T entity) throws InternalServerException {
        try (var session = HibernateFactory.getSessionFactory().openSession()) {

            Transaction transaction = null;

            try {
                transaction = session.beginTransaction();

                var result = session.merge(entity);

                transaction.commit();

                return result;
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }

                logger.warning(e.getMessage());
                logger.warning(Arrays.toString(e.getStackTrace()));

                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public T getById(K id) throws InternalServerException {
        try (var session = HibernateFactory.getSessionFactory().openSession()) {
            return session.get(entityClass, id);
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public void delete(K id) throws InternalServerException {
        try (var session = HibernateFactory.getSessionFactory().openSession()) {
            Transaction transaction;

            transaction = session.beginTransaction();
            var entity = getById(id);

            if (entity != null) {
                session.remove(entity);
            }

            transaction.commit();
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }
}
