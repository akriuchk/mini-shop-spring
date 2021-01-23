package org.akriuchk.minishop.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private boolean isSmallAvailable;
    private boolean isMiddleAvailable;
    private boolean isEuroAvailable;
    private boolean isDuoAvailable;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = false,
            fetch = FetchType.LAZY
    )
    @EqualsAndHashCode.Exclude
    private List<Image> images;

    @ManyToOne
    @JoinColumn(name = "catalog_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Category linenCatalog;
}
