package org.akriuchk.minishop.repository;

import org.akriuchk.minishop.model.Linen;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinenRepository extends CrudRepository<Linen, Long> {
}
