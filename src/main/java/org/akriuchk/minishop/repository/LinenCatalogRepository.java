package org.akriuchk.minishop.repository;

import org.akriuchk.minishop.model.LinenCatalog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LinenCatalogRepository extends CrudRepository<LinenCatalog, Long> {
    Optional<LinenCatalog> findByName(String name);
}
