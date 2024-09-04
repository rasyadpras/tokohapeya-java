package com.enigma.challenge.tokohapeya.repository;

import com.enigma.challenge.tokohapeya.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, String> {
    List<Product> findAllByBrandLikeIgnoreCaseOrPhoneTypeLikeIgnoreCase(String brand, String type);
}
