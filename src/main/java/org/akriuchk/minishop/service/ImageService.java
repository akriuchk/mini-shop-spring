package org.akriuchk.minishop.service;

import lombok.RequiredArgsConstructor;
import org.akriuchk.minishop.converter.ImageDtoMapper;
import org.akriuchk.minishop.dto.ImageDto;
import org.akriuchk.minishop.model.Image;
import org.akriuchk.minishop.model.Product;
import org.akriuchk.minishop.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository repository;
    private final ProductService productService;
    private final ImageDtoMapper mapper;

    public static String ENCODED_FLAG = "EncodedFName";

    /**
     * Import new image, if product name is present, add there
     *
     * @param file        image file
     * @param productName product name of image
     */
    public void importNew(MultipartFile file, String productName) {
        Image image = new Image();

        if (file.getOriginalFilename().startsWith(ENCODED_FLAG)) {
            byte[] decodedFilename = Base64.getDecoder().decode(file.getOriginalFilename().substring(ENCODED_FLAG.length() + 1));
            image.setFilename(new String(decodedFilename));
        } else {
            image.setFilename(file.getOriginalFilename());
        }

        if (nonNull(productName) && !productName.isEmpty()) {
            productService.addImage(productName, image);
        }

        image.setContent(extract(file));
        repository.save(image);
    }

    /**
     * Find by id and return image dto
     *
     * @param id key of image
     * @return dto
     */
    public ImageDto find(Long id) {
        return mapper.toDto(repository.getOne(id));
    }

    /**
     * Find by id and return image itself
     *
     * @param id key of image
     * @return image(bytes)
     */
    public byte[] findRaw(Long id) {
        return repository.findById(id).get().getContent();
    }

    /**
     * replace image of product, save old picture in db
     *
     * @param id           key of image
     * @param newImageFile replacement for old one
     */
    public void replace(Long id, MultipartFile newImageFile) {
        Image existingImage = repository.findById(id).get();

        Image newImg = new Image();
        newImg.setFilename(newImageFile.getOriginalFilename());
        newImg.setContent(extract(newImageFile));
        if (nonNull(existingImage.getProduct())) {
            productService.replaceImage(existingImage.getProduct(), existingImage, newImg);
        }

        repository.save(existingImage);
        repository.save(newImg);
    }

    private byte[] extract(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Error in file reading", e);
        }
    }

    /**
     * Update image info: product
     *
     * @param patchList patch body
     * @return updated image dto
     */
    public List<ImageDto> update(List<ImageDto> patchList) {
        return patchList.stream().map(patch -> {
                    Image existingImage = repository.getOne(patch.getId());
                    if (nonNull(patch.getProduct())) {
                        Product product = productService.getByName(patch.getProduct());
                        existingImage.setProduct(product);
                    } else {
                        existingImage.setProduct(null);
                    }

                    return repository.save(existingImage);
                }
        )
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Method provides list of images(max 50 items) without product
     *
     * @return images
     */
    @Transactional(readOnly = true)
    public List<ImageDto> getUnmatched() {
        return mapper.convert(repository.findByProductIsNull().limit(50));
    }
}
