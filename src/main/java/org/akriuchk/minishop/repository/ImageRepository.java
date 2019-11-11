package org.akriuchk.minishop.repository;

import org.akriuchk.minishop.model.Image;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ImageRepository extends CrudRepository<Image, Long> {
    Set<Image> findByIsLinked(boolean isLinked);

    Set<Image> findAllByLinen_id(long linenId);
}
