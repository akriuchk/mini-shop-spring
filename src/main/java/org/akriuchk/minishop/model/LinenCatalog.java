package org.akriuchk.minishop.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode
@ToString
public class LinenCatalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "catalog_id")
    private long id;

    @Column(unique = true)
    private String name;

    @Column
    private String displayName;

    @OneToMany(
            mappedBy = "linenCatalog",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @EqualsAndHashCode.Exclude
    private Set<Linen> linens = new HashSet<>();
}