package com.farneser.weatherviewer.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Locations")
public class Location implements IEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private int id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "UserId")
    private User user;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;
}
