package com.microservices.demo;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api/product")
@Slf4j
public class ProductController 
{
	 private final ProductService productService;
	    
	    public ProductController(ProductService productService) {
	        this.productService = productService;
	    }
	
	@PostMapping("/save")
	@ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest) 
    {
         productService.createProduct(productRequest);
    }
	
	@GetMapping("/getAllProducts")
	@ResponseStatus(HttpStatus.OK)
	public List<ProductResponse> getAllProducts()
	{
		return productService.getAllProducts();
	}
	 @PostMapping
	    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
	        Product savedProduct = productService.saveProduct(product);
	        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
	
}}
