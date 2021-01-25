package org.akriuchk.minishop.converter;

import org.akriuchk.minishop.dto.ProductDto;
import org.akriuchk.minishop.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = ImageDtoMapper.class)
public interface ProductMapper {
    @Mapping(target = "category", source = "product.category.name")
    ProductDto toDto(Product product);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "images", ignore = true)
    Product toPojo(ProductDto dto);

    List<ProductDto> convert(List<Product> source);
}
