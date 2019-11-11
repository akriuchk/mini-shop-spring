package org.akriuchk.minishop.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.akriuchk.minishop.model.Image;
import org.akriuchk.minishop.model.Linen;
import org.akriuchk.minishop.repository.ImageRepository;
import org.akriuchk.minishop.repository.LinenRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@AllArgsConstructor
public class ImageImportService {

    private final ImageRepository imageRepository;
    private final LinenRepository linenRepository;

    public void putLinenImage(String linenName, MultipartFile importFile) {
        Linen linen = linenRepository.findByName(linenName).orElseThrow(() -> new RuntimeException("Linen not found"));
        Image image = imageRepository.findById(linen.getId()).orElseGet(Image::new);
        try {
            image.setImageContent(importFile.getBytes());
            imageRepository.save(image);
        } catch (IOException e) {
            throw new RuntimeException("Error in image saving!", e);
        }
    }

    public Map<Image, Set<Linen>> getUntagged() {
        Map<Image, Set<Linen>> resultMap = new HashMap<>();

        Set<Image> notLinkedImages = imageRepository.findByIsLinked(false);
        Map<String, Image> nameImageMap = notLinkedImages.stream().filter(img -> Objects.nonNull(img.getName())).collect(Collectors.toMap(Image::getName, img -> img));
        Map<String, String[]> namePartsMap = nameImageMap.keySet().stream().collect(Collectors.toMap(name -> name, name -> name.split("_")));
        namePartsMap.forEach(
                (name, nameParts) -> {
                    Image preImage = nameImageMap.get(name);
                    Set<Linen> linens = Stream.of(nameParts).filter(str -> str.length() > 1).flatMap(part -> linenRepository.findByNameContainingIgnoreCase(part).stream()).collect(Collectors.toSet());
                    resultMap.put(preImage, linens);
                }
        );

        resultMap.forEach(
                (img, linens) -> {
                    if (linens.size() == 1) {
                        Linen linen = linens.iterator().next();
                        linen.getImages().add(img);
                        img.setLinked(true);
                        img.setLinen(linen);
                        imageRepository.save(img);
                        log.debug("linen {} -> img {}", linen.getName(), img.getName());
                    }
                    img.setImageContent(new byte[0]); //to not send content
                }
        );
        return resultMap;
    }
}
