package org.akriuchk.minishop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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


//    @OneToMany(
//            mappedBy = "linen",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true,
//            fetch = FetchType.EAGER
//    )
//    @EqualsAndHashCode.Exclude
//    private Set<Long> images;

    @ManyToOne
    @JoinColumn(name = "catalog_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Category linenCatalog;
}
