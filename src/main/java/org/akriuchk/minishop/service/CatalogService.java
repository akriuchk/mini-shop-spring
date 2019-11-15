package org.akriuchk.minishop.service;

import lombok.RequiredArgsConstructor;
import org.akriuchk.minishop.model.Linen;
import org.akriuchk.minishop.model.LinenCatalog;
import org.akriuchk.minishop.repository.LinenCatalogRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogService {
    private final LinenCatalogRepository linenCatalogRepository;

    public List<LinenCatalog> getCatalogs(boolean onlyAvailable) {
        List<LinenCatalog> result;
        if (onlyAvailable) {
            result = linenCatalogRepository.findAllByLinensIsNotEmpty().stream()
                    .peek(cat -> {
                        Set<Linen> availableLinens = cat.getLinens().stream().filter(linen ->
                                linen.isSmallAvailable() || linen.isMiddleAvailable() || linen.isEuroAvailable() || linen.isDuoAvailable()
                        ).collect(Collectors.toSet());
                        cat.setLinens(availableLinens);
                    }).collect(Collectors.toList());
        } else {
            result = (List<LinenCatalog>) linenCatalogRepository.findAll();
        }
        return result;
    }
}
