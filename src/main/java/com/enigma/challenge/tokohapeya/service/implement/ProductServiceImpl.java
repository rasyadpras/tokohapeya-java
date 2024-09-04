package com.enigma.challenge.tokohapeya.service.implement;

import com.enigma.challenge.tokohapeya.dto.request.CreateProductRequest;
import com.enigma.challenge.tokohapeya.dto.request.UpdateProductRequest;
import com.enigma.challenge.tokohapeya.dto.response.ProductResponse;
import com.enigma.challenge.tokohapeya.entity.Product;
import com.enigma.challenge.tokohapeya.repository.ProductRepo;
import com.enigma.challenge.tokohapeya.service.ProductService;
import com.enigma.challenge.tokohapeya.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final ValidationUtil validation;

    private ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .brand(product.getBrand())
                .type(product.getPhoneType())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductResponse create(CreateProductRequest request) {
        validation.validate(request);
        Product product = Product.builder()
                .brand(request.getBrand())
                .phoneType(request.getType())
                .price(request.getPrice())
                .stock(request.getStock())
                .build();
        productRepo.saveAndFlush(product);
        return toResponse(product);
    }

    @Transactional(readOnly = true)
    @Override
    public ProductResponse getById(String id) {
        Product product = getProductById(id);
        return toResponse(product);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductResponse> getAll(String brand, String type) {
        List<Product> products;
        if (brand != null || type != null) {
            String keywordBrand = brand != null ? "%" + brand + "%" : null;
            String keywordType = type != null ? "%" + type + "%" : null;
            products = productRepo.findAllByBrandLikeIgnoreCaseOrPhoneTypeLikeIgnoreCase(keywordBrand, keywordType);
        } else {
            products = productRepo.findAll();
        }
        return products.stream().map(this::toResponse).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductResponse update(UpdateProductRequest request) {
        Product product = getProductById(request.getId());
        validation.validate(request);
        product.setBrand(request.getBrand());
        product.setPhoneType(request.getType());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        productRepo.saveAndFlush(product);
        return toResponse(product);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        Product product = getProductById(id);
        productRepo.delete(product);
    }

    @Transactional(readOnly = true)
    @Override
    public Product getProductById(String id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }
}
