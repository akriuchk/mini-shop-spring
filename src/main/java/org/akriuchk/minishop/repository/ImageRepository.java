package org.akriuchk.minishop.repository;

import org.akriuchk.minishop.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Stream;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Stream<Image> findByProductIsNull();
    List<Image> findByFilenameContainingIgnoreCase(String startChars);
}
