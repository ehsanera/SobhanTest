package com.example.testsobhan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductUpdateDto {
    private String code;
    private String brand;
    private String name;
    private BigDecimal price;
    private String address;
    private String description;
}
