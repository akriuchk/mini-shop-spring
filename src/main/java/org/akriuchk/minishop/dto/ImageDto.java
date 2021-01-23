package org.akriuchk.minishop.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ImageDto {

    private long id;
    private String filename;
    private String link;
    private boolean isAssigned;
    private Date createdAt;
    private String product;
}