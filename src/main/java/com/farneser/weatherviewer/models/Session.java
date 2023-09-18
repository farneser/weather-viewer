package com.farneser.weatherviewer.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "Sessions")
public class Session implements IEntity<UUID> {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "UserId")
    private User user;

    @Column(nullable = false)
    private Date expiresAt;
}