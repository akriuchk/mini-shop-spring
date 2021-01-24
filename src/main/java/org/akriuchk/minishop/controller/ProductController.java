package org.akriuchk.minishop.controller;

import org.akriuchk.minishop.dto.ProductDto;
import org.akriuchk.minishop.model.Product;
import org.akriuchk.minishop.rest.ApiResponse;
import org.akriuchk.minishop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts() {
        List<ProductDto> body = productService.listProducts();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable("id") long productID) {
        return new ResponseEntity<>(productService.findProduct(productID), HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping
    public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductDto product) {
        productService.addProduct(product);
        return new ResponseEntity<>(new ApiResponse(true, "Product has been added"), HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/{productID}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable("productID") long productID, @RequestBody @Valid Product product) {
        Product updateProduct = productService.updateProduct(productID, product);
        return new ResponseEntity<>(new ApiResponse(true, "Product has been updated"), HttpStatus.OK);
    }
}
