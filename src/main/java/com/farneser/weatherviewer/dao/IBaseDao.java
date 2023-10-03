package com.farneser.weatherviewer.dao;

import com.farneser.weatherviewer.exceptions.InternalServerException;

public interface IBaseDao<T, K> {
    T create(T entity) throws InternalServerException;

    T getById(K id) throws InternalServerException;

    void delete(K id) throws InternalServerException;
}
