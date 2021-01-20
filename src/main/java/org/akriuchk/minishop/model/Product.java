package org.akriuchk.minishop.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "products")
@Data
public class Product {
    private @GeneratedValue(strategy = GenerationType.IDENTITY) @Id long id;
    private @NotNull String name;
    private @NotNull String imageURL;
    private @NotNull double price;
    private @NotNull String description;
}
