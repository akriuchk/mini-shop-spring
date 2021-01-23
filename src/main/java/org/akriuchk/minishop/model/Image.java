package org.akriuchk.minishop.model;

import lombok.Data;
import lombok.ToString;
import org.akriuchk.minishop.validation.ValidExtension;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "images")
@Data
@Valid
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotNull
    @ValidExtension(value = {".jpg", ".png", ".jpeg"})
    private String filename;

    @Basic
    @Size(min = 1)
    private byte[] content;
    private boolean isAssigned = false; //eq to prod.isNull

    @ManyToOne
    @JoinColumn(name = "product_id")
    @ToString.Exclude
    private Product product;

    private Date createdAt;

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }

}
