package org.akriuchk.minishop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.akriuchk.minishop.model.CatalogEnum;
import org.akriuchk.minishop.model.LinenCatalog;
import org.akriuchk.minishop.repository.LinenCatalogRepository;
import org.akriuchk.minishop.repository.LinenRepository;
import org.akriuchk.minishop.service.parser.AbstractParser;
import org.akriuchk.minishop.service.parser.DefaultParser;
import org.akriuchk.minishop.service.parser.VasilisaBjazParser;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileImportService {
    private final LinenCatalogRepository linenCatalogRepository;
    private final LinenRepository linenRepository;
    private AbstractParser parser;


    public List<LinenCatalog> parse(MultipartFile file) {
        List<LinenCatalog> catalogs = new ArrayList<>();
        try {
            Workbook book = WorkbookFactory.create(file.getInputStream());
            parser = determineParser(book);
            catalogs = parser.parse(book);
            linenCatalogRepository.saveAll(catalogs);
        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException("Error in file reading", e);
        }
        return catalogs;
    }

    private AbstractParser determineParser(Workbook workbook) {
        return new DefaultParser(linenCatalogRepository, linenRepository);
    }


    public List<LinenCatalog> parse(MultipartFile file, CatalogEnum catalog) {
        List<LinenCatalog> catalogs = new ArrayList<>();
        try {
            Workbook book = WorkbookFactory.create(file.getInputStream());
            parser = getAndValidateParser(book, catalog);
            catalogs = parser.parse(book);
            linenCatalogRepository.saveAll(catalogs);
        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException("Error in file reading", e);
        }
        return catalogs;
    }


    private AbstractParser getAndValidateParser(Workbook workbook, CatalogEnum catalog) {
        AbstractParser parser = null;
        if (catalog == CatalogEnum.DEFAULT) {
            parser = new DefaultParser(linenCatalogRepository, linenRepository);
        } else if (catalog == CatalogEnum.VASILISA_BYAZ) {
            parser = new VasilisaBjazParser(linenCatalogRepository, linenRepository);
        } else {
            throw new RuntimeException("Parser for catalog not found");
        }
        if (!parser.isFileValid(workbook)) {
            throw new RuntimeException("Catalog type is wrong for this file");
        }
        return parser;
    }


}
