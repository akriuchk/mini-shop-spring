package org.akriuchk.minishop.controller;

import lombok.RequiredArgsConstructor;
import org.akriuchk.minishop.dto.ImportFileDto;
import org.akriuchk.minishop.model.ImportFile;
import org.akriuchk.minishop.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Secured("ROLE_ADMIN")
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileImportController {
    public final FileService fileService;

    @PostMapping
    public ResponseEntity<ImportFileDto> importFile(@RequestParam("file") MultipartFile file) {
        ImportFileDto importResult = fileService.importNew(file);
        return new ResponseEntity<>(importResult, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ImportFileDto>> search(@RequestParam("status") ImportFile.IMPORT_STATUS status) {
        return new ResponseEntity<>(fileService.search(status), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<List<ImportFileDto>> deleteCompleted() {
        return new ResponseEntity<>(fileService.deleteFinished(), HttpStatus.CREATED);
    }

}
