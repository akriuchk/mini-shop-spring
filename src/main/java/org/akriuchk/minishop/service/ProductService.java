package org.akriuchk.minishop.service;

import lombok.RequiredArgsConstructor;
import org.akriuchk.minishop.converter.ProductMapper;
import org.akriuchk.minishop.dto.ProductDto;
import org.akriuchk.minishop.model.Category;
import org.akriuchk.minishop.model.Image;
import org.akriuchk.minishop.model.Product;
import org.akriuchk.minishop.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProductMapper mapper;

    public void addProduct(ProductDto product) {
        Product pojo = mapper.toPojo(product);
        Category category = categoryService.findByName(product.getLinenCatalog());
        pojo.setLinenCatalog(category);
        productRepository.save(pojo);
    }

    public List<ProductDto> listProducts() {
        return mapper.convert(productRepository.findAll());
    }

    //todo change to product dto
    public Product updateProduct(long productID, Product newProduct) {
        Product oldProduct = productRepository.findById(productID).get();
        oldProduct.setName(newProduct.getName());
        oldProduct.setSmallAvailable(newProduct.isSmallAvailable());
        oldProduct.setMiddleAvailable(newProduct.isMiddleAvailable());
        oldProduct.setEuroAvailable(newProduct.isEuroAvailable());
        oldProduct.setDuoAvailable(newProduct.isDuoAvailable());
        return productRepository.save(oldProduct);
    }

    public void addImage(String productName, Image image) {
        Product product = productRepository.findByName(productName);
        product.getImages().add(image);
        image.setProduct(product);
    }

    public void replaceImage(Product product, Image existingImage, Image newImg) {
        product.getImages().remove(existingImage);
        product.getImages().add(newImg);
        newImg.setProduct(product);
        existingImage.setProduct(null);
    }
}