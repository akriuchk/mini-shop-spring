package org.akriuchk.minishop.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.ToString;
import org.akriuchk.minishop.converter.ImageContentSerializer;

import javax.persistence.*;

@Entity
@Data
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Basic
    @JsonSerialize(using = ImageContentSerializer.class)
    private byte[] imageContent;

    private String name;
    private boolean isLinked = true;

    @OneToOne
    @JoinColumn(name = "linen_id")
    @ToString.Exclude
    private Linen linen;
}
