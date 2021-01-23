package org.akriuchk.minishop.repository;

import org.akriuchk.minishop.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
