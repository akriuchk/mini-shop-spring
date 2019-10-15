package org.akriuchk.minishop.repository;

import org.akriuchk.minishop.model.Image;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends CrudRepository<Image, String> {
}
