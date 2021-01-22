package org.akriuchk.minishop.service;

import lombok.RequiredArgsConstructor;
import org.akriuchk.minishop.converter.FileImportMapper;
import org.akriuchk.minishop.dto.ImportFileDto;
import org.akriuchk.minishop.model.ImportFile;
import org.akriuchk.minishop.repository.FilesRepository;
import org.akriuchk.minishop.validation.ValidExtension;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.akriuchk.minishop.model.ImportFile.IMPORT_STATUS.IN_DB;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FilesRepository repository;
    private final FileImportMapper mapper;

    @ValidExtension
    public ImportFileDto importNew(MultipartFile file) {
        ImportFile importFile = new ImportFile();
        System.out.println("FILEFILEFILE:" + file.getOriginalFilename());
        importFile.setFilename(file.getOriginalFilename());

        try {
            importFile.setContent(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error in file reading", e);
        }

        repository.save(importFile);
        importFile.setStatus(IN_DB);
        return mapper.toDto(importFile);
    }

    public List<ImportFileDto> search(ImportFile.IMPORT_STATUS status) {
       return mapper.convert(repository.findAllByStatus(status));
    }

    public List<ImportFileDto> deleteFinished() {
        List<ImportFileDto> result = new ArrayList<>();
        List<ImportFile> finishedFiles = repository.deleteByStatus(ImportFile.IMPORT_STATUS.FINISHED);
        List<ImportFile> errorFiles = repository.deleteByStatus(ImportFile.IMPORT_STATUS.ERROR);

        result.addAll(mapper.convert(finishedFiles));
        result.addAll(mapper.convert(errorFiles));
        return result;

    }
}
