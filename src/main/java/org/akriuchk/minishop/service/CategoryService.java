package org.akriuchk.minishop.service;


import lombok.RequiredArgsConstructor;
import org.akriuchk.minishop.converter.CategoryMapper;
import org.akriuchk.minishop.dto.CategoryDto;
import org.akriuchk.minishop.model.Category;
import org.akriuchk.minishop.repository.CategoriesRepository;
import org.springframework.stereotype.Service;

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

    public List<CategoryDto> listAll() {
        List<Category> all = repository.findAll();
        return mapper.lightConvert(all);
    }

    public Optional<Category> findByName(String name) {
        return repository.findByName(name);
    }

    public Category getByName(String name) {
        return repository.getByName(name);
    }

    public CategoryDto getByCategoryName(String categoryName) {
        return mapper.toDto(repository.getByName(categoryName));
    }

    public CategoryDto update(String name, CategoryDto dto) {
        Category category = repository.findByName(name).get();
        mapper.update(dto, category);
        repository.save(category);
        return mapper.toDto(category);
    }
}
