package org.akriuchk.minishop.repository;

import org.akriuchk.minishop.model.BedSheet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BedSheetRepository extends CrudRepository<BedSheet, Long> {
}
