package org.akriuchk.minishop.repository;

import org.akriuchk.minishop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriesRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByProductsIsNotEmpty();
    Optional<Category> findByName(String name);
    Category getByName(String name);
}
