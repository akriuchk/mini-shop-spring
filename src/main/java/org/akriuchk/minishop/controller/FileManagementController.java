package org.akriuchk.minishop.controller;

import lombok.AllArgsConstructor;
import org.akriuchk.minishop.model.CatalogEnum;
import org.akriuchk.minishop.model.LinenCatalog;
import org.akriuchk.minishop.service.FileImportService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "${cors.url}")
public class FileManagementController {
    private final FileImportService fileImportService;

    @PostMapping(
            value = "/file",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    List<LinenCatalog> parseFile(@RequestParam("catalogFile") MultipartFile file) {
        return fileImportService.parse(file);
    }

    @PostMapping(
            value = "/parse",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    List<LinenCatalog> parseCatalog(@RequestParam("catalogFile") MultipartFile file, @RequestParam CatalogEnum catalog) {
        return fileImportService.parse(file, catalog);
    }
}
