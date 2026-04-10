package com.agrovalue.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.agrovalue.backend.dto.ProductRequest;
import com.agrovalue.backend.dto.ProductResponse;
import com.agrovalue.backend.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 🔥 PUBLIC (or USER level)
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // 🔥 PUBLIC (or USER level)
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // 🔥 ONLY FARMER can add product
    @PreAuthorize("hasRole('FARMER') or hasRole('ADMIN')") 
    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.addProduct(request));
    }

    // 🔥 ONLY FARMER can update
    @PreAuthorize("hasRole('FARMER') or hasRole('ADMIN')") 
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
                                                         @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    // 🔥 ONLY FARMER or ADMIN can delete
    @PreAuthorize("hasAnyRole('FARMER','ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // 🔥 ONLY FARMER (with image upload)
    @PreAuthorize("hasRole('FARMER') or hasRole('ADMIN')") 
    @PostMapping("/with-image")
    public ResponseEntity<ProductResponse> addProductWithImage(
            @RequestPart("product") @Valid ProductRequest request,
            @RequestPart("file") MultipartFile file
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.addProductWithImage(request, file));
    }
}