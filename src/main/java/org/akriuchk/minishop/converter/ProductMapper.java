package org.akriuchk.minishop.converter;

import org.akriuchk.minishop.dto.ProductDto;
import org.akriuchk.minishop.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "linenCatalog", source = "product.linenCatalog.name")
    ProductDto toDto(Product product);

    @Mapping(target = "linenCatalog", ignore = true)
    Product toPojo(ProductDto dto);

    List<ProductDto> convert(List<Product> source);
}
