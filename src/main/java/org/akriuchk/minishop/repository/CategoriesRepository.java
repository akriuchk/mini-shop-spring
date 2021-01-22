package org.akriuchk.minishop.repository;

import org.akriuchk.minishop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriesRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
