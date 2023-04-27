package com.example.testsobhan.repository.elastic;

import com.example.testsobhan.document.ProductModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductElasticRepository extends ElasticsearchRepository<ProductModel, Long> {
}