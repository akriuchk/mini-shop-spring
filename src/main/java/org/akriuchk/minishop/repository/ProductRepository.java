package org.akriuchk.minishop.repository;

import org.akriuchk.minishop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String productName);

    Stream<Product> findAllByImagesIsEmpty();

    List<Product> findTop10ByNameContainingIgnoreCaseOrderByName(String startChars);

}
