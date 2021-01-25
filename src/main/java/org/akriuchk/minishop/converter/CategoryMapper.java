package org.akriuchk.minishop.converter;

import org.akriuchk.minishop.dto.CategoryDto;
import org.akriuchk.minishop.model.Category;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface CategoryMapper {

    @Named(value = "useMe")
    CategoryDto toDto(Category source);

    @Mapping(target = "products", ignore = true)
    CategoryDto toShortDto(Category category);

    @Mapping(target = "products", ignore = true)
    Category toPojo(CategoryDto source);

    @IterableMapping(qualifiedByName = "useMe")
    List<CategoryDto> convert(List<Category> source);

    @Mapping(target = "products", ignore = true)
    Category update(CategoryDto dto, @MappingTarget Category category);

}
