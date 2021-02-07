package org.akriuchk.minishop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.akriuchk.minishop.converter.ImageDtoMapper;
import org.akriuchk.minishop.converter.ProductMapper;
import org.akriuchk.minishop.dto.ImageDto;
import org.akriuchk.minishop.dto.ProductDto;
import org.akriuchk.minishop.model.Image;
import org.akriuchk.minishop.model.Product;
import org.akriuchk.minishop.repository.ImageRepository;
import org.akriuchk.minishop.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageProductMatcherService {
    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;

    private final ProductMapper productMapper;
    private final ImageDtoMapper imageDtoMapper;

    private final Pattern DIGIT_PATTERN = Pattern.compile("\\d+");

    @Transactional(readOnly = true)
    public List<ProductDto> suggestImagesForAllProducts() {
        List<Product> products = productRepository.findAllByImagesIsEmpty()
                .map(product -> {
                    List<Image> matchedImages = findMatchesForProduct(product.getName());
                    product.getImages().addAll(matchedImages);
                    matchedImages.forEach(img -> img.setProduct(product));
                    return product;
                })
                .filter(product -> product.getImages().size() != 0)
                .sorted(Comparator.comparing(Product::getName))
                .collect(Collectors.toList());

        return productMapper.convert(products);
    }

    //products
    //  9489 Кружева
    //  9427.0
    //  9392
    //  9394
    //  9418/1 новый год ХЮГГЕ

    //images
    //  5454_вихрь_бабочек.jpg
    //  5455_вихрь_бабочек.jpg
    //  5479_каллейдоскоп-1.jpg
    //  5479_каллейдоскоп-2.jpg
    //  5479_каллейдоскоп-3.jpg
    public List<Image> findMatchesForProduct(String productName) {
        String productNumber = extractNumber(productName);
        if (productNumber.isEmpty()) {
            return Collections.emptyList();
        }

        return imageRepository.findByProductIsNull()
                .filter(image -> {
                    String filename = image.getFilename();
                    List<String> nameParts = Arrays.asList(filename.substring(0, filename.lastIndexOf(".")).split("_"));
                    return nameParts.contains(productNumber);
                })
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<ImageDto> suggestImagesForProduct(String productName) {
        return imageDtoMapper.convert(findMatchesForProduct(productName));
    }

    private String extractNumber(String imageFilename) {
        Matcher m = DIGIT_PATTERN.matcher(imageFilename);
        if (m.find()) {
            return m.group();
        }
        return "";
    }

}
