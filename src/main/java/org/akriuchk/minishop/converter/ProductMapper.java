package org.akriuchk.minishop.converter;

import org.akriuchk.minishop.dto.ProductDto;
import org.akriuchk.minishop.model.Product;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(Product product);

    List<ProductDto> convert(Stream<Product> source);

    List<ProductDto> convert(List<Product> source);
}
