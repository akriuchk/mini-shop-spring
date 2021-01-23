package org.akriuchk.minishop.controller;

import lombok.RequiredArgsConstructor;
import org.akriuchk.minishop.dto.ImageDto;
import org.akriuchk.minishop.rest.ApiResponse;
import org.akriuchk.minishop.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {
    public final ImageService imageService;

    @Secured("ROLE_ADMIN")
    @PostMapping
    public ResponseEntity<ApiResponse> importFile(@RequestParam("file") MultipartFile image, String product) {
        imageService.importNew(image, product);
        return new ResponseEntity<>(new ApiResponse(true, "Image to product added"), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImageDto> search(@PathVariable("id") Long id) {
        return new ResponseEntity<>(imageService.find(id), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/raw", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> findRaw(@PathVariable("id") Long id) {
        return new ResponseEntity<>(imageService.findRaw(id), HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN") //todo may be not right place?
    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse> replaceImage(@PathVariable("id") Long id, @RequestParam("file") MultipartFile image) {
        imageService.replace(id, image);
        return new ResponseEntity<>(new ApiResponse(true, "Image was replaced"), HttpStatus.OK);
    }
}
