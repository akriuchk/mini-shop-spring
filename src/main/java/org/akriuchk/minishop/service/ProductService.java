package org.akriuchk.minishop.service;

import lombok.RequiredArgsConstructor;
import org.akriuchk.minishop.converter.ProductMapper;
import org.akriuchk.minishop.dto.ProductDto;
import org.akriuchk.minishop.model.Product;
import org.akriuchk.minishop.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public Product updateProduct(long productID, Product newProduct) {
        Product oldProduct = productRepository.findById(productID).get();
        oldProduct.setName(newProduct.getName());
        oldProduct.setSmallAvailable(newProduct.isSmallAvailable());
        oldProduct.setMiddleAvailable(newProduct.isMiddleAvailable());
        oldProduct.setEuroAvailable(newProduct.isEuroAvailable());
        oldProduct.setDuoAvailable(newProduct.isDuoAvailable());
        //image
        return productRepository.save(oldProduct);
    }

    public List<ProductDto> listProducts() {
        return mapper.convert(productRepository.findAll());
    }
}