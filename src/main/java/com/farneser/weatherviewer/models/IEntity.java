package com.farneser.weatherviewer.models;

public interface IEntity<K> {
    K getId();
    void setId(K id);
}
