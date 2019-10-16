package org.akriuchk.minishop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode
public class Linen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private boolean isSmallAvailable;
    private boolean isMiddleAvailable;
    private boolean isEuroAvailable;
    private boolean isDuoAvailable;

    @ManyToOne
    @JoinColumn(name="catalog_id", nullable=false)
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private LinenCatalog linenCatalog;
}
