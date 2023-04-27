package com.example.testsobhan.controller;

import com.example.testsobhan.dto.ProductCreateDto;
import com.example.testsobhan.dto.ProductDto;
import com.example.testsobhan.dto.ProductUpdateDto;
import com.example.testsobhan.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<PageImpl<ProductDto>> list(
            @RequestParam @DefaultValue("10") @Valid @Min(10) @Max(50) int pageSize,
            @RequestParam @DefaultValue("1") @Valid @Min(1) int pageNumber
    ) {
        PageRequest pageable = PageRequest.of(pageNumber - 1, pageSize);
        return ResponseEntity.ok(productService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProductDto> save(@RequestBody ProductCreateDto createDto) {
        return new ResponseEntity<>(productService.save(createDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable long id, @RequestBody ProductUpdateDto updateDto) {
        return ResponseEntity.ok(productService.update(id, updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        productService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
