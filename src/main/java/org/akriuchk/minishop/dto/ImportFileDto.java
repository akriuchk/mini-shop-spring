package org.akriuchk.minishop.dto;

import lombok.Data;
import org.akriuchk.minishop.dto.importinfo.ImportResultDto;
import org.akriuchk.minishop.model.ImportFile;

import java.util.List;

@Data
public class ImportFileDto {

    private long id;
    private String filename;
    private ImportFile.IMPORT_STATUS status;
    private String createdAt;
    private List<ImportResultDto> result;
}
