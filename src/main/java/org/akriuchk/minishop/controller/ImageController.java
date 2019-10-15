package org.akriuchk.minishop.controller;

import lombok.AllArgsConstructor;
import org.akriuchk.minishop.model.Image;
import org.akriuchk.minishop.repository.ImageRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ImageController {

    private final ImageRepository imageRepository;

    @PostMapping(
            value = "/image",
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String saveImage(@RequestParam("image") MultipartFile file) throws IOException {
        Image image = new Image();
        image.setImageContent(file.getBytes());
        imageRepository.save(image);
        return image.getId();
    }


    @GetMapping(
            value = "/image/{imageId}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody
    byte[] getImageWithMediaType(@PathVariable String imageId) throws IOException {
        Optional<Image> image = imageRepository.findById(imageId);
        if (image.isPresent()) {
            return image.get().getImageContent();
        } else {
            throw new RuntimeException("No images found with id: " + imageId);
        }
    }
}
