package com.agrovalue.backend.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.agrovalue.backend.dto.ProductRequest;
import com.agrovalue.backend.dto.ProductResponse;

public interface ProductService {
    List<ProductResponse> getAllProducts();
    ProductResponse getProductById(Long id);
    ProductResponse addProduct(ProductRequest request);
    ProductResponse updateProduct(Long id, ProductRequest request);
    void deleteProduct(Long id);
    List<ProductResponse> getProductsByFarmer(Long farmerId);
    ProductResponse addProductWithImage(ProductRequest request, MultipartFile file);
}
