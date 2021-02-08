package org.akriuchk.minishop.converter;

import org.akriuchk.minishop.dto.CategoryDto;
import org.akriuchk.minishop.model.Category;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface CategoryMapper {

    @Named(value = "full")
    CategoryDto toDto(Category source);

    @Named(value = "light")
    @Mapping(target = "products", ignore = true)
    CategoryDto toShortDto(Category category);

    @Mapping(target = "products", ignore = true)
    Category toPojo(CategoryDto source);

    @IterableMapping(qualifiedByName = "full")
    List<CategoryDto> convert(List<Category> source);

    @IterableMapping(qualifiedByName = "light")
    List<CategoryDto> lightConvert(List<Category> source);

    @Mapping(target = "products", ignore = true)
    Category update(CategoryDto dto, @MappingTarget Category category);

}
