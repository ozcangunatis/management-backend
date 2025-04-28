package com.example.management.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "offices")
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_office")
    private Long id;

    @Column(name = "office_name", nullable = false)
    private String officeName;

    @Column(name = "city")
    private String city;

    @Column(name = "authorized_person")
    private Integer authorizedPerson;

    // commit
}
