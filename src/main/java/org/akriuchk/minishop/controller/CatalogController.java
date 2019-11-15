package org.akriuchk.minishop.controller;

import lombok.AllArgsConstructor;
import org.akriuchk.minishop.model.Linen;
import org.akriuchk.minishop.model.LinenCatalog;
import org.akriuchk.minishop.repository.LinenRepository;
import org.akriuchk.minishop.service.CatalogService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "${cors.url}")
public class CatalogController {
    private final CatalogService catalogService;
    private final LinenRepository linenRepository;

    @GetMapping("/catalog")
    public List<LinenCatalog> getCatalogs(@RequestParam boolean onlyAvailable) {
        return catalogService.getCatalogs(onlyAvailable);
    }

    @GetMapping("/findByNameContainingIgnoreCase")
    public List<Linen> findByNameContainingIgnoreCase(@RequestParam String namePart) {
        return linenRepository.findByNameContainingIgnoreCase(namePart);
    }

}
