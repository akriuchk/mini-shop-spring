package org.akriuchk.minishop.controller;

import org.akriuchk.minishop.config.ApiResponse;
import org.akriuchk.minishop.model.Product;
import org.akriuchk.minishop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Iterable<Product>> getProducts() {
        Iterable<Product> body = productService.listProducts();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @Secured("ADMIN")
    @PostMapping
    public ResponseEntity<ApiResponse> addProduct(@RequestBody @Valid Product product) {
        productService.addProduct(product);
        return new ResponseEntity<>(new ApiResponse(true, "Product has been added"), HttpStatus.CREATED);
    }

    @Secured("ADMIN")
    @PostMapping("/{productID}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable("productID") long productID, @RequestBody @Valid Product product) {
        Product updateProduct = productService.updateProduct(productID, product);
        return new ResponseEntity<>(new ApiResponse(true, "Product has been updated"), HttpStatus.OK);
    }
}
