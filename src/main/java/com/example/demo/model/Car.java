package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String carName;
    private String mfg;
    private Long price;
    private String owner;
    @ManyToOne
    @JoinColumn(name = "car_brand_id", referencedColumnName = "id")
    private Brand brand;
    @Temporal(TemporalType.TIMESTAMP)
    private Instant createdAt;

}
