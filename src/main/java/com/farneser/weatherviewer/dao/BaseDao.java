package com.farneser.weatherviewer.dao;

import com.farneser.weatherviewer.factory.HibernateFactory;
import org.hibernate.Transaction;

import java.util.Arrays;

public abstract class BaseDao<T, K> {
    protected final Class<T> entityClass;

    public BaseDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public T create(T entity) {
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

                System.out.println(e.getMessage());
                System.out.println(Arrays.toString(e.getStackTrace()));

                throw new RuntimeException(e);
            }
        }
    }

    public T getById(K id) {
        try (var session = HibernateFactory.getSessionFactory().openSession()) {
            return session.get(entityClass, id);
        }
    }

    public void delete(K id) {
        try (var session = HibernateFactory.getSessionFactory().openSession()) {
            Transaction transaction;

            transaction = session.beginTransaction();
            var entity = getById(id);

            if (entity != null) {
                session.remove(entity);
            }

            transaction.commit();
        }
    }
}