package org.akriuchk.minishop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.akriuchk.minishop.converter.ImageToUrlSerializer;

import javax.persistence.*;
import java.util.Set;


@Entity
@Data
@EqualsAndHashCode
@ToString
public class Linen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private boolean isSmallAvailable;
    private boolean isMiddleAvailable;
    private boolean isEuroAvailable;
    private boolean isDuoAvailable;


    @OneToMany(
            mappedBy = "linen",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @EqualsAndHashCode.Exclude
    @JsonSerialize(using = ImageToUrlSerializer.class)
    private Set<Image> images;

    @ManyToOne
    @JoinColumn(name = "catalog_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private LinenCatalog linenCatalog;
}
