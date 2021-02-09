package org.akriuchk.minishop.service;


import lombok.RequiredArgsConstructor;
import org.akriuchk.minishop.converter.CategoryMapper;
import org.akriuchk.minishop.dto.CategoryDto;
import org.akriuchk.minishop.model.Category;
import org.akriuchk.minishop.repository.CategoriesRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoriesRepository repository;
    private final CategoryMapper mapper;

    public void addCategory(CategoryDto dto) {
        Category newCategory = mapper.toPojo(dto);
        repository.save(newCategory);
    }

    public List<CategoryDto> listAll(boolean onlyAvailable) {
        List<Category> categories;
        if (onlyAvailable) {
            categories = repository.findAllByProductsIsNotEmpty();
        } else {
            categories = repository.findAll();
        }
        return mapper.lightConvert(categories);
    }

    public Optional<Category> findByName(String name) {
        return repository.findByName(name);
    }

    public Category getByName(String name) {
        return repository.getByName(name);
    }

    public CategoryDto getByCategoryName(String categoryName) {
        CategoryDto categoryDto = mapper.toDto(repository.getByName(categoryName));
        categoryDto.getProducts()
                .sort(Comparator.comparing(p -> p.getImages().isEmpty(), Comparator.naturalOrder()));
        return categoryDto;
    }

    public CategoryDto update(String name, CategoryDto dto) {
        Category category = repository.findByName(name).get();
        mapper.update(dto, category);
        repository.save(category);
        return mapper.toDto(category);
    }
}
