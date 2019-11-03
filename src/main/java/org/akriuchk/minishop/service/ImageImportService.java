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
}
