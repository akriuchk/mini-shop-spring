package org.akriuchk.minishop.service;

import lombok.RequiredArgsConstructor;
import org.akriuchk.minishop.converter.FileImportMapper;
import org.akriuchk.minishop.dto.ImportFileDto;
import org.akriuchk.minishop.dto.importinfo.ImportResultDto;
import org.akriuchk.minishop.model.ImportFile;
import org.akriuchk.minishop.model.SupportedCategories;
import org.akriuchk.minishop.repository.FilesRepository;
import org.akriuchk.minishop.service.parser.CatalogParserManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.akriuchk.minishop.model.ImportFile.IMPORT_STATUS.*;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FilesRepository repository;
    private final CatalogParserManager catalogParserManager;
    private final FileImportMapper mapper;

    public ImportFileDto importNew(MultipartFile file, SupportedCategories category) {
        ImportFile importFile = new ImportFile();

        try {
            ImportFileDto importFileDto = importInternal(importFile, file, category);
            importFile.setStatus(FINISHED);
            importFileDto.setStatus(FINISHED);
            return importFileDto;
        } catch (Exception e) {
            importFile.setStatus(ERROR);
            throw new RuntimeException(e);
        } finally {
            repository.save(importFile);
        }
    }

    private ImportFileDto importInternal(ImportFile importFile, MultipartFile file, SupportedCategories category) throws Exception {
        importFile.setFilename(file.getOriginalFilename());
        importFile.setContent(file.getBytes());
        importFile.setStatus(IN_DB);
        repository.save(importFile);

        List<ImportResultDto> catalofImportResult = catalogParserManager.importNewCatalog(file, category);
        ImportFileDto importFileResult = mapper.toDto(importFile);
        importFileResult.setResult(catalofImportResult);
        return importFileResult;
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
