package org.akriuchk.minishop.controller;

import lombok.AllArgsConstructor;
import org.akriuchk.minishop.model.Image;
import org.akriuchk.minishop.repository.ImageRepository;
import org.akriuchk.minishop.service.ImageImportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "${cors.url}")
public class ImageController {

    private final ImageRepository imageRepository;
    private final ImageImportService imageImportService;

    @PostMapping(
            value = "/image",
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public long saveImage(@RequestParam("image") MultipartFile file) throws IOException {
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
    byte[] getImageWithMediaType(@PathVariable long imageId) {
        byte[] image = imageRepository.findById(imageId).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Image not found")).getImageContent();
        if ( image.length == 0) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Image not found");
        }
        return image;
    }


    @PostMapping(
            value = "/linenimage",
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public void saveLinenImage(
            @RequestParam("linenName") String linenName,
            @RequestParam("image") MultipartFile file) {
        imageImportService.putLinenImage(linenName, file);
    }


}
