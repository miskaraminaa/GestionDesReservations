package com.example.reservation.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Chambre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TypeChambre typeChambre;

    private double prix;

    @Enumerated(EnumType.STRING)
    private DispoChambre dispoChambre;

}