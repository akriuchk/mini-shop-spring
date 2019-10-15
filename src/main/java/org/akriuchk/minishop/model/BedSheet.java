package org.akriuchk.minishop.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class BedSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String content;
    private String imageId;
}
