package org.akriuchk.minishop.controller;

import lombok.AllArgsConstructor;
import org.akriuchk.minishop.model.Linen;
import org.akriuchk.minishop.model.LinenCatalog;
import org.akriuchk.minishop.repository.LinenCatalogRepository;
import org.akriuchk.minishop.repository.LinenRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class CatalogController {

    private final LinenCatalogRepository linencatalogRepository;
    private final LinenRepository linenRepository;

    @GetMapping("/catalog")
    public List<LinenCatalog> getPictures() {
        return (List<LinenCatalog>) linencatalogRepository.findAll();
    }

    @GetMapping("/findByNameContainingIgnoreCase")
    public List<Linen> findByNameContainingIgnoreCase(@RequestParam String namePart) {
        return linenRepository.findByNameContainingIgnoreCase(namePart);
    }

}
