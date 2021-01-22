package org.akriuchk.minishop.dto;

import lombok.Data;
import org.akriuchk.minishop.model.ImportFile;

@Data
public class ImportFileDto {

    private long id;
    private String filename;
    private ImportFile.IMPORT_STATUS status;
    private String createdAt;
}
