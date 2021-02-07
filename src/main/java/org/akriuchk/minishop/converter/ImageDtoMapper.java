package org.akriuchk.minishop.converter;

import org.akriuchk.minishop.dto.ImageDto;
import org.akriuchk.minishop.model.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
public interface ImageDtoMapper {

    @Mapping(source = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss", target = "createdAt")
    @Mapping(target = "product", source = "product.name")
    ImageDto toDto(Image source);

    List<ImageDto> convert(List<Image> source);
    List<ImageDto> convert(Stream<Image> source);
}
