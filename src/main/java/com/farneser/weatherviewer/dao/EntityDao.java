package com.farneser.weatherviewer.dao;

import com.farneser.weatherviewer.helpers.factory.HibernateFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Arrays;
import java.util.List;

public abstract class EntityDao<T, K> {
    protected final Session session;
    protected final Class<T> entityClass;

    public EntityDao(Session session, Class<T> entityClass) {
        this.session = session;
        this.entityClass = entityClass;
    }

    public void create(T entity) {
        var session = HibernateFactory.getSessionFactory().openSession();

        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            session.merge(entity);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));

            throw new RuntimeException(e);
        }
    }

    public T getById(K id) {
        return session.get(entityClass, id);
    }

    public void delete(K id) {
        var transaction = session.beginTransaction();
        var entity = getById(id);

        if (entity != null) {
            session.remove(entity);
        }

        transaction.commit();
    }

    public abstract List<T> get();
}
