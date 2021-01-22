package org.akriuchk.minishop.converter;

import org.akriuchk.minishop.dto.CategoryDto;
import org.akriuchk.minishop.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface CategoryMapper {

    CategoryDto toDto(Category source);

    @Mapping(target = "linens", ignore = true)
    Category toPojo(CategoryDto source);

    List<CategoryDto> convert(List<Category> source);

    @Mapping(target = "linens", ignore = true)
    Category update(CategoryDto dto, @MappingTarget Category category);
}
