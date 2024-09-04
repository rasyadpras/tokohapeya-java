package com.enigma.challenge.tokohapeya.entity;

import com.enigma.challenge.tokohapeya.helper.TableName;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = TableName.PRODUCT_TABLE)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "phone_type", nullable = false)
    private String phoneType;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "stock", nullable = false)
    private Integer stock;
}
