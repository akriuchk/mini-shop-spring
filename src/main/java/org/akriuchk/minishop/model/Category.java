package org.akriuchk.minishop.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "categories")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "catalog_id")
    private long id;

    @Column(unique = true)
    private String name;

    @Column
    private String displayName;

    @OneToMany(
            mappedBy = "category",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @EqualsAndHashCode.Exclude
    private Set<Product> products = new HashSet<>();
}
