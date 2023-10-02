package com.farneser.weatherviewer.dao;

import java.util.List;

public interface IBaseDao<T, K> {
    T create(T entity);

    T getById(K id);

    void delete(K id);

    List<T> get();
}
