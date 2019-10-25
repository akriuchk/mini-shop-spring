package org.akriuchk.minishop.controller;

import lombok.AllArgsConstructor;
import org.akriuchk.minishop.model.LinenCatalog;
import org.akriuchk.minishop.repository.LinenCatalogRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class CatalogController {

    private final LinenCatalogRepository linencatalogRepository;

    @GetMapping("/catalog")
    public List<LinenCatalog> getPictures() {
        return (List<LinenCatalog>) linencatalogRepository.findAll();
    }

}
