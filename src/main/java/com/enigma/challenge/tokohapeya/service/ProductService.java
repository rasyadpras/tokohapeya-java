package com.enigma.challenge.tokohapeya.service;

import com.enigma.challenge.tokohapeya.dto.request.CreateProductRequest;
import com.enigma.challenge.tokohapeya.dto.request.UpdateProductRequest;
import com.enigma.challenge.tokohapeya.dto.response.ProductResponse;
import com.enigma.challenge.tokohapeya.entity.Product;

import java.util.List;

public interface ProductService {
    ProductResponse create(CreateProductRequest request);
    ProductResponse getById(String id);
    List<ProductResponse> getAll(String brand, String type);
    ProductResponse update(UpdateProductRequest request);
    void deleteById(String id);
    Product getProductById(String id);
}
