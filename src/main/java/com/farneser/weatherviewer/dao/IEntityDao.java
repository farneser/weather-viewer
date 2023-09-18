package com.farneser.weatherviewer.dao;

import java.util.List;

public interface IEntityDao<T, K> {
    public T create(T entity);

    public T getById(K id);

    public void delete(K id);

    public List<T> get();
}
