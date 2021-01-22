package org.akriuchk.minishop.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

import static javax.validation.constraints.Pattern.Flag.CASE_INSENSITIVE;


@Entity
@Table(name = "import_files")
@Data
@Valid
public class ImportFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotNull
    @Pattern(regexp = ".*.xlsx", flags = CASE_INSENSITIVE)
//    @ValidExtension
    private String filename;

    @Basic
    private byte[] content;

    @Enumerated(EnumType.STRING)
    private IMPORT_STATUS status = IMPORT_STATUS.ACCEPTED;

    private Date createdAt;

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }

    public enum IMPORT_STATUS {
        ACCEPTED,
        IN_DB,
        IN_PROGRESS,
        ERROR,
        FINISHED
    }

}
