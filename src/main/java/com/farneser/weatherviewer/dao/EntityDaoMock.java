package com.farneser.weatherviewer.dao;

import com.farneser.weatherviewer.models.IEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class EntityDaoMock<T extends IEntity<K>, K> implements IEntityDao<T, K> {
    private final ArrayList<T> userData = new ArrayList<>();

    @Override
    public T create(T entity) {
        entity.setId(generateNewId());
        get().add(entity);
        return entity;
    }

    @Override
    public List<T> get() {
        return userData;
    }

    public T getById(K id) {
        for (var entity : get()) {
            if (entity.getId() == id) {
                return entity;
            }
        }

        return null;
    }

    public void delete(K id) {
        get().removeIf(location -> location.getId() == id);
    }

    protected abstract K generateNewId();
}
