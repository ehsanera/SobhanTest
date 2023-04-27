package com.example.testsobhan.document;

import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;
import java.util.Date;

@Document(indexName = "product")
public class ProductModel {
    private Long id;
    private String code;
    private String brand;
    private String name;
    private BigDecimal price;
    private String address;
    private String description;
    private Date modificationDate;
}
