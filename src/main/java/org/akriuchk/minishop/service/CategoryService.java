package org.akriuchk.minishop.service;


import lombok.RequiredArgsConstructor;
import org.akriuchk.minishop.converter.CategoryMapper;
import org.akriuchk.minishop.dto.CategoryDto;
import org.akriuchk.minishop.model.Category;
import org.akriuchk.minishop.repository.CategoriesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return mapper.convert(all);
    }

    public CategoryDto update(String name, CategoryDto dto) {
        Category category = repository.findByName(name).get();
        mapper.update(dto, category);
        repository.save(category);
        return mapper.toDto(category);
    }

    public Category findByName(String name) {
        return repository.findByName(name).get();
    }
}
