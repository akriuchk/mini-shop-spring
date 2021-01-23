package org.akriuchk.minishop.model;

import lombok.Data;
import org.akriuchk.minishop.validation.ValidExtension;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;


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
    @ValidExtension(value = {".xlsx"})
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
