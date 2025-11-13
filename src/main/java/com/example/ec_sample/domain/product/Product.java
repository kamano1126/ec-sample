package com.example.ec_sample.domain.product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String imagePath;
    private Integer price;
    private String description;
    private Integer stock;
    private String size;
    private String category;
    private String period;
    private String bland;
    private LocalDate joinDate;
    private LocalDate deleteDate;
}
