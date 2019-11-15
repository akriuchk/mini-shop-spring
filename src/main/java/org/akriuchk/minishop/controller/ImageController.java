package org.akriuchk.minishop.controller;

import lombok.AllArgsConstructor;
import org.akriuchk.minishop.model.Image;
import org.akriuchk.minishop.model.Linen;
import org.akriuchk.minishop.repository.ImageRepository;
import org.akriuchk.minishop.service.ImageImportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "${cors.url}")
public class ImageController {

    private final ImageRepository imageRepository;
    private final ImageImportService imageImportService;

    @GetMapping(
            value = "/image/{imageId}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody
    byte[] getImageById(@PathVariable long imageId) {
        byte[] image = imageRepository.findById(imageId).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Image not found")).getImageContent();
        if (image.length == 0) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Image not found");
        }
        return image;
    }

    @GetMapping(
            value = "/image",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public ResponseEntity<Set<Image>> getImageByLinen(@RequestParam long linenID) {
        Set<Image> images = imageRepository.findAllByLinen_id(linenID);
        if (images.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(images);
    }


    @GetMapping(
            value = "/images/unmatched",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<Image, Set<Linen>> getImagesForMatching() {
        return imageImportService.getUntagged()
                .entrySet()
                .stream().peek(e -> e.getKey().setImageContent(new byte[0]))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//        return imageImportService.getUntagged();
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

    @PostMapping(
            value = "/image",
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public long saveImage(@RequestParam("image") MultipartFile file, String imageName) throws IOException {
        Image image = new Image();
        image.setImageContent(file.getBytes());
        image.setName(imageName);
        image.setLinked(false);
        imageRepository.save(image);
        return image.getId();
    }

    @DeleteMapping(
            value = "/image/{imageId}"
    )
    public ResponseEntity deleteImage(@PathVariable long imageId) {
        imageRepository.deleteById(imageId);
        return ResponseEntity.noContent().build();
    }

}
