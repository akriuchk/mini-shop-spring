package org.akriuchk.minishop.service;

import org.akriuchk.minishop.model.Product;
import org.akriuchk.minishop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public Product updateProduct(long productID, Product newProduct) {
        Product oldProduct = productRepository.findById(productID).get();
        oldProduct.setName(newProduct.getName());
        oldProduct.setImageURL(newProduct.getImageURL());
        oldProduct.setPrice(newProduct.getPrice());
        oldProduct.setDescription(newProduct.getDescription());

        return productRepository.save(oldProduct);
    }

    public Iterable<Product> listProducts() {
        return productRepository.findAll();
    }
}