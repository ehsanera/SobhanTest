package com.example.testsobhan.controller;

import com.example.testsobhan.dto.ProductCreateDto;
import com.example.testsobhan.dto.ProductDto;
import com.example.testsobhan.dto.ProductUpdateDto;
import com.example.testsobhan.entity.Product;
import com.example.testsobhan.repository.ProductRepository;
import com.example.testsobhan.service.ProductService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
class ProductModelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListProducts() throws Exception {
        // Given
        PageRequest pageable = PageRequest.of(0, 10);
        PageImpl<ProductDto> products = new PageImpl<>(Collections.emptyList(), pageable, 0L);

        when(productService.findAll(any(PageRequest.class))).thenReturn(products);

        // When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/product")
                        .param("pageSize", "10")
                        .param("pageNumber", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String responseBody = new JSONObject(mvcResult.getResponse().getContentAsString()).getString("content");
        assertEquals("[]", responseBody);
    }

    @Test
    void testFindProductById() throws Exception {
        // Given
        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Product 1");
        productDto.setPrice(new BigDecimal("10.00"));

        when(productService.findById(1)).thenReturn(productDto);

        // When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/product/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String responseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponse = "{\"id\":1,\"code\":null,\"brand\":null,\"name\":\"Product 1\",\"price\":10.00,\"address\":null,\"description\":null}";
        assertEquals(expectedResponse, responseBody);
    }

    @Test
    void testCreateProduct() throws Exception {
        // Given
        ProductCreateDto createDto = new ProductCreateDto();
        createDto.setName("Product 1");
        createDto.setPrice(new BigDecimal("10.00"));

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Product 1");
        productDto.setPrice(new BigDecimal("10.00"));

        when(productService.save(any(ProductCreateDto.class))).thenReturn(productDto);

        // When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Product 1\",\"price\":10.00}"))
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        String responseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponse = "{\"id\":1,\"code\":null,\"brand\":null,\"name\":\"Product 1\",\"price\":10.00,\"address\":null,\"description\":null}";
        assertEquals(expectedResponse, responseBody);
    }

    @Test
    void testUpdateProduct() throws Exception {
        // Given
        ProductRepository productRepository = mock(ProductRepository.class);
        ModelMapper modelMapper = new ModelMapper();
        ProductService productService = new ProductService(productRepository, modelMapper);

        Product product = new Product();
        product.setId(1L);
        product.setName("Product 1");
        product.setPrice(new BigDecimal("10.00"));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductUpdateDto updateDto = new ProductUpdateDto();
        updateDto.setName("Product 2");
        updateDto.setPrice(new BigDecimal("20.00"));

        // When
        ProductDto updatedProductDto = productService.update(1L, updateDto);

        // Then
        assertEquals(1L, updatedProductDto.getId());
        assertEquals("Product 2", updatedProductDto.getName());
        assertEquals(new BigDecimal("20.00"), updatedProductDto.getPrice());
    }

}
