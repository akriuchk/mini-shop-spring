package org.akriuchk.minishop.converter;

import org.akriuchk.minishop.dto.ImportFileDto;
import org.akriuchk.minishop.model.ImportFile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FileImportMapper {

    @Mapping(source = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss", target = "createdAt")
    ImportFileDto toDto(ImportFile source);

    List<ImportFileDto> convert(List<ImportFile> source);
}
