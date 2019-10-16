package org.akriuchk.minishop.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode
public class LinenCatalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "catalog_id")
    private long id;

    private String name;

    @OneToMany(
            mappedBy = "linenCatalog",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )

    @EqualsAndHashCode.Exclude
    private Set<Linen> linens = new HashSet<>();
}
