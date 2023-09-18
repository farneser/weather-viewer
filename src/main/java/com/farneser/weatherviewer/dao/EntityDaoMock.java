package com.farneser.weatherviewer.dao;

public abstract class EntityDaoMock<T, K> implements IEntityDao<T, K> {
    @Override
    public T create(T entity) {
        get().add(entity);
        return entity;
    }
}
