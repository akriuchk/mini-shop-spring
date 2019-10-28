package org.akriuchk.minishop.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.akriuchk.minishop.model.Image;
import org.akriuchk.minishop.model.Linen;
import org.akriuchk.minishop.repository.ImageRepository;
import org.akriuchk.minishop.repository.LinenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
public class ImageImportService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private LinenRepository linenRepository;


    public void putLinenImage(String linenName, MultipartFile importFile) {
        Linen linen = linenRepository.findByName(linenName).orElseThrow(() -> new RuntimeException("Linen not found"));

        try {
            Image image = new Image();
            image.setImageContent(importFile.getBytes());
            imageRepository.save(image);

            linen.setImageId(image.getId());
            linenRepository.save(linen);
        } catch (IOException e) {
            throw new RuntimeException("Error in image saving!", e);
        }
    }
}
