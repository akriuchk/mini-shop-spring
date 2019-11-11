package org.akriuchk.minishop.serialize;

import org.akriuchk.minishop.model.Image;
import org.akriuchk.minishop.model.Linen;
import org.akriuchk.minishop.model.LinenCatalog;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SerializeService {
    public static ImageDto convert(Image img) {
        ImageDto dto = new ImageDto();
        dto.setId(img.getId());
        dto.setName(img.getName());
        dto.setLinked(img.isLinked());
        Optional.ofNullable(img.getLinen()).ifPresent(l -> dto.setLinen(l.getId()));
        return dto;
    }

    public static LinenDto convert(Linen linen) {
        return LinenDto.builder()
                .id(linen.getId())
                .isSmallAvailable(linen.isSmallAvailable())
                .isMiddleAvailable(linen.isMiddleAvailable())
                .isDuoAvailable(linen.isMiddleAvailable())
                .isEuroAvailable(linen.isEuroAvailable())
                .linenCatalog(linen.getLinenCatalog().getName())
                .images(linen.getImages().stream().map(Image::getId).collect(Collectors.toSet()))
                .build();
    }

    public static LinenCatalogDto convert(LinenCatalog c) {
        return LinenCatalogDto.builder()
                .id(c.getId())
                .displayName(c.getDisplayName())
                .name(c.getName())
                .linens(c.getLinens().stream().map(Linen::getId).collect(Collectors.toSet()))
                .build();
    }

    public static Set<ImageDto> convertImages(Collection<Image> collection) {
        return collection.stream().map(SerializeService::convert).collect(Collectors.toSet());
    }

    public static Set<LinenDto> convertLinens(Collection<Linen> collection) {
        return collection.stream().map(SerializeService::convert).collect(Collectors.toSet());
    }

    public static Set<LinenCatalogDto> convertCatalogs(Collection<LinenCatalog> collection) {
        return collection.stream().map(SerializeService::convert).collect(Collectors.toSet());
    }
}
