package org.akriuchk.minishop.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
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

//    @ManyToOne
//    @JoinColumn(name = "catalog_id", nullable = false)
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    @JsonIgnore
//    private LinenCatalog linenCatalog;
}
