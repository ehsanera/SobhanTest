package com.example.testsobhan.service;

import com.example.testsobhan.dto.ProductCreateDto;
import com.example.testsobhan.dto.ProductDto;
import com.example.testsobhan.dto.ProductUpdateDto;
import com.example.testsobhan.entity.Product;
import com.example.testsobhan.exceptionHandeling.ApplicationException;
import com.example.testsobhan.exceptionHandeling.Errors;
import com.example.testsobhan.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public PageImpl<ProductDto> findAll(PageRequest pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return new PageImpl<>(products.get().map(product -> modelMapper.map(product, ProductDto.class)).collect(Collectors.toList()));
    }

    public ProductDto findById(long id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            throw new ApplicationException(Errors.PRODUCT_NOT_FOUND, Map.of("id", id));
        }

        return modelMapper.map(product, ProductDto.class);
    }

    public ProductDto save(ProductCreateDto createDto) {
        try {
            return modelMapper.map(
                    productRepository.save(
                            modelMapper.map(
                                    createDto,
                                    Product.class
                            )
                    ),
                    ProductDto.class
            );
        } catch (Exception ex) {
            throw new ApplicationException(Errors.PRODUCT_FOUND, Map.of("code", createDto.getCode()));
        }
    }

    public ProductDto update(long id, ProductUpdateDto updateDto) {
        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            throw new ApplicationException(Errors.PRODUCT_NOT_FOUND, Map.of("id", id));
        }

        modelMapper.map(updateDto, product);
        productRepository.save(product);

        return modelMapper.map(
                product,
                ProductDto.class
        );
    }

    public void delete(long id) {
        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            throw new ApplicationException(Errors.PRODUCT_NOT_FOUND, Map.of("id", id));
        }

        productRepository.delete(product);
    }
}
