package com.farneser.weatherviewer.dao;

public interface IBaseDao<T, K> {
    T create(T entity);

    T getById(K id);

    void delete(K id);
}
