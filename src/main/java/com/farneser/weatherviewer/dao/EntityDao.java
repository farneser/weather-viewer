package com.farneser.weatherviewer.dao;

import com.farneser.weatherviewer.models.Location;
import org.hibernate.Session;

import java.util.List;

public abstract class EntityDao<T, K> {
    protected final Session session;
    protected final Class<T> entityClass;

    public EntityDao(Session session, Class<T> entityClass) {
        this.session = session;
        this.entityClass = entityClass;
    }

    public void create(T entity) {
        var transaction = session.beginTransaction();
        session.persist(entity);
        transaction.commit();
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
