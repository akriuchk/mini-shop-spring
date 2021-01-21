package org.akriuchk.minishop.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;

    @NotNull
    private String name;

    @NotNull
    private String imageURL;

    @NotNull
    private double price;

    @NotNull
    private String description;

}
