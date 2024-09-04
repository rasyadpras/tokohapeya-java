package com.enigma.challenge.tokohapeya.controller;

import com.enigma.challenge.tokohapeya.dto.request.CreateProductRequest;
import com.enigma.challenge.tokohapeya.dto.request.UpdateProductRequest;
import com.enigma.challenge.tokohapeya.dto.response.JsonResponse;
import com.enigma.challenge.tokohapeya.dto.response.ProductResponse;
import com.enigma.challenge.tokohapeya.helper.ApiUrl;
import com.enigma.challenge.tokohapeya.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ApiUrl.PRODUCT_API)
public class ProductController {
    private final ProductService productService;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'OWNER_SHOP')")
    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<JsonResponse<ProductResponse>> createProduct(@RequestBody CreateProductRequest product) {
        ProductResponse create = productService.create(product);
        JsonResponse<ProductResponse> response = JsonResponse.<ProductResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(create)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(
            path = ApiUrl.PATH_ID,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<JsonResponse<ProductResponse>> getProductById(@PathVariable String id) {
        ProductResponse productById = productService.getById(id);
        JsonResponse<ProductResponse> response = JsonResponse.<ProductResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(productById)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonResponse<List<ProductResponse>>> getAllProducts(
            @RequestParam(name = "brand", required = false) String brand,
            @RequestParam(name = "type", required = false) String type
    ) {
        List<ProductResponse> products = productService.getAll(brand, type);
        JsonResponse<List<ProductResponse>> response = JsonResponse.<List<ProductResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(products)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'OWNER_SHOP')")
    @PutMapping(
            path = ApiUrl.PATH_ID,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<JsonResponse<ProductResponse>> updateProduct(
            @PathVariable String id,
            @RequestBody UpdateProductRequest product
    ) {
        product.setId(id);
        ProductResponse update = productService.update(product);
        JsonResponse<ProductResponse> response = JsonResponse.<ProductResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase() + " updated")
                .data(update)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'OWNER_SHOP')")
    @DeleteMapping(
            path = ApiUrl.PATH_ID,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<JsonResponse<String>> deleteProductById(@PathVariable String id) {
        productService.deleteById(id);
        JsonResponse<String> response = JsonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase() + " deleted product with id " + id)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
