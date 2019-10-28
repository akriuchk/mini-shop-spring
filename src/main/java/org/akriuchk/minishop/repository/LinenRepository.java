package org.akriuchk.minishop.repository;

import org.akriuchk.minishop.model.Linen;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LinenRepository extends CrudRepository<Linen, Long> {
    Optional<Linen> findByName(String name);
    List<Linen> findByNameContainingIgnoreCase(String startChars);
}
