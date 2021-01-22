package org.akriuchk.minishop.repository;

import org.akriuchk.minishop.model.ImportFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilesRepository extends JpaRepository<ImportFile, Long> {
    List<ImportFile> deleteByStatus(ImportFile.IMPORT_STATUS status);
    List<ImportFile> findAllByStatus(ImportFile.IMPORT_STATUS status);
}
