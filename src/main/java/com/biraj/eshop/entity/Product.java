package com.biraj.eshop.entity;


import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

    @EqualsAndHashCode(callSuper = true)
    @Entity
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    @Data
    public class Product extends Audit {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        private String name;
        private String description;
        private double price;
        private boolean isAvailable;

        //@Transient
        @OneToMany(cascade = CascadeType.ALL)
        private List<Discount> discounts;

    }